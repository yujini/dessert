<%@page import="java.net.URLDecoder"%>
<%@page import="javax.xml.crypto.URIDereferencer"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
$(function() {
	$("input[name=chk]").click(function() {
		var f=document.searchForm;
		
		var reservationNum = $(this).val();
		$("input[name=reservationNum]").val(reservationNum);
		
		if($(this).is(":checked")) {
			if(confirm('수취 확인하시겠습니까?')) {
				$("input[name=nullOrSysdate]").val("yes");				
				f.submit();
			} else {
				return;
			}
		} else {
			if(confirm('수취 취소하시겠습니까?')) {
				$("input[name=nullOrSysdate]").val("no");				
				f.submit();
			} else {
				return;
			}
		}
	});	
});
 
function searchList() {
	var f=document.searchForm;
	f.submit();
}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 예약 리스트 </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			   <tr height="35">
			      <td align="left" width="50%">
			          	${dataCount}개(${page}/${total_page} 페이지)  
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="center" bgcolor="#eeeeee" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <th width="60" style="color: #787878;">번호</th>
			      <th style="color: #787878;">상품명</th>
			      <th width="10%" style="color: #787878;">회원명</th>
			      <th width="30%" style="color: #787878;">예약날짜</th>
			      <th width="10%" style="color: #787878;">수취여부</th>
			  </tr>
			 
			 <c:forEach var="dto" items="${list}">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&reservationNum=${dto.reservationNum}">
			           ${dto.product.name}&nbsp;${dto.product.productGroupKor}&nbsp;
			           <c:if test="${dto.cnt > 1}">외 ${dto.cnt - 1} 개 상품</c:if>
			           </a>
			      </td>
			      <td>${dto.memName}</td>
			      <td>${dto.reservationDate} | ${dto.reservationHour} : ${dto.reservationMin}</td>
			      <td>
			      		<c:if test="${empty dto.pickupDate}">
			      			<input type="checkbox" name="chk" value="${dto.reservationNum}">
			      		</c:if>
			      		<c:if test="${not empty dto.pickupDate}">
			      			<input type="checkbox" name="chk" value="${dto.reservationNum}" checked="checked">
			      		</c:if>
			      </td>
			  </tr>
			 </c:forEach>

			</table>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			       	${paging}
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/reservation/list.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="<%=cp%>/reservation/list.do" method="post">
			              <select name="searchKey" class="selectField">
			                  <option value="userName" ${searchKey=="userName" ? "selected" : ""}>회원명</option>
			                  <option value="reservationDate" ${searchKey=="reservationDate" ? "selected" : ""}>예약일</option>
			            </select>
			            <input type="text" name="searchValue" class="boxTF" value="${searchValue}">
			            
			            
				        <input type="hidden" name="reservationNum" value="">			      		
			            <input type="hidden" name="nullOrSysdate" value="">
			            <input type="hidden" name="page" value="${page}"> 
			            
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
			      <td align="right" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/reservation/created.do';">예약등록</button>
			      </td>
			   </tr>
			</table>
        </div>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>