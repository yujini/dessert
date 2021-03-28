<%@page import="java.text.DecimalFormat"%>
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

<style type="text/css">
.imgLayout {
	width: 190px;
	height: 205px;
	padding: 10px 5px 10px;
	margin: 5px;
	border: none;
}
.subject {
	width: 180px;
	height: 25px;
	line-height: 25px;
	margin: 5px auto;
	border-top: 1px solid #dad9ff;
	display: inline-block;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	cursor: pointer;
}
.imgText > input{
	border: 0px;
	text-align: center;
	padding: 5px;
}
</style>

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">
function changeProductGroup() {
	var pGroup = $("select").val();
	location.href="<%=cp%>/product/list.do?pGroup=" + pGroup;
}

function updateProduct() {
	var productNum = $("input[name=proNum]:checked").val();		// :checked 까지 안하면 계속 첫번째 radio의 productNum 만 나온다!!!!

	if(!productNum) {
		alert('상품을 선택하세요!!!');
		return;
	}
	
	var url = '<%=cp%>/product/update.do?productNum=' + productNum;
	location.href = url;
}

function deleteProduct() {
	var productNum = $("input[name=proNum]:checked").val();		// :checked 까지 안하면 계속 첫번째 radio의 productNum 만 나온다!!!!
	var pGroup = $("select[name=pGroup]").val();
	
	if(!productNum) {
		alert('상품을 선택하세요!!!');
		return;
	}
	
	if(confirm('정말 삭제하시겠습니까?')) {
		var url = '<%=cp%>/product/delete.do?productNum=' + productNum + '&pGroup=' + pGroup;
		location.href = url;
	}
	
}
</script>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>	
<div class="container">
    <div class="body-container" style="width: 630px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 상품목록 </h3>  </div>
        
        <div>
       		<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			   <tr height="35">
			      <td align="left" width="20%">
			          ${dataCount}개
			      </td>
			      <td align="right" width="80%">
			      	<!-- 선택박스 - pGroup -->			<!-- change 이벤트 추가하기!!!!!!! -->
			          <select name="pGroup" onchange="changeProductGroup();">
			                <option value="wholecake1" ${pGroup=="wholecake1" ? "selected='selected'" : ""}>홀케익(1호)</option>
			                <option value="wholecake2" ${pGroup=="wholecake2" ? "selected='selected'" : ""}>홀케익(2호)</option>
			                <option value="wholecake4" ${pGroup=="wholecake4" ? "selected='selected'" : ""}>홀케익(4호)</option>			                
			                <option value="piececake" ${pGroup=="piececake" ? "selected='selected'" : ""}>케익(조각)</option>
			                <option value="macaron" ${pGroup=="macaron" ? "selected='selected'" : ""}>마카롱</option>
			         </select>       
			     	  <c:if test="${sessionScope.member.userId == 'admin'}">
			          		<button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/product/created.do;'">상품등록</button>
			          		<button type="button" class="btn" onclick="updateProduct();">상품수정</button>
			          		<button type="button" class="btn" onclick="deleteProduct();">상품삭제</button>
			          </c:if>
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
				<c:forEach var="dto" items="${list}" varStatus="status"> <!-- list 최대 6개 -->
					<c:if test="${status.index==0}"><tr></c:if>		<!--  처음시작했으면 tr을 찍어라 -->
					<c:if test="${status.index!=0 && status.index%3==0}">	<!-- 한 줄에 3개 이므로 : 3번 찍었으면 tr 막고 tr 만든다 -->
						<c:out value="</tr></tr>" escapeXml="false"></c:out>	<!-- </tr><tr> 만 적으면 느낌표 생기므로 c:out 으로  -->
					</c:if>
					<td width="210" height="450" align="center" valign="top">
						<div class="imgLayout" >
						<!-- 다운로드 아니고 보여주는 것. src 에 경로 -->
							<img alt="" src="<%=cp%>/uploads/photo/${dto.fileName}"					
								width="180" height="180" border="0" align="top"><br>
							<c:if test="${sessionScope.member.userId == 'admin'}">
								<input type="radio" name="proNum" value="${dto.productNum}">		
							</c:if>						
						</div>
						<div class="imgText">
							<div>
								<c:if test="${dto.isBest == 'yes'}">		
									<img alt="" src="<%=cp %>/resource/images/best_Icon.gif">
								</c:if>				<!-- best gif 찾아서 추가하기!!! -->
								&nbsp;
							</div>
							<div>
								<c:if test="${dto.isSale == 'no'}">		
									<i><b style="color: tomato">판매 중단</b></i>
								</c:if>				<!-- best gif 찾아서 추가하기!!! -->
								&nbsp;
							</div>
							<input type="text" name="name" size="25" value="${dto.name}" readonly="readonly" style="font-weight: bold;" ><br>	
							<div>남은 수량 : <span style="color: red; font-weight: bold;">${dto.cnt}</span></div>
							<c:if test="${pGroup != 'wholecake'}">
								<!-- <input type="text" name="price" size="25" value="￦ ${dto.price}" readonly="readonly"><br> -->	
								<input type="text" name="price" size="25" value="<fmt:formatNumber value='${dto.price}' type='currency'/>" readonly="readonly"><br>	
							</c:if>
							
							<hr width="150px"><br>
							<span style="display:block; width:200px;">${dto.memo}</span>
						</div>				
					</td>
				</c:forEach>
				
				<c:set var="n" value="${list.size()}"></c:set>
				<c:if test="${n > 0 && n % 3 != 0}">
					<c:forEach var="i" begin="${n%3+1}" end="3" step="1">	<!-- step 생략 가능. 생략 시 1 -->
						<td width="210">
							<div class="imgLayout">&nbsp;</div>
						</td>
					</c:forEach>
				</c:if>
				
				<!-- 마지막 tr 태그 닫기 -->
				<c:if test="${list.size() != 0}">
					<c:out value="</tr>" escapeXml="false"></c:out>
				</c:if>
				
			</table>
			 
			
        </div>
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>