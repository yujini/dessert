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
function deleteReservation(num) {
	if(confirm("예약을 취소 하시겠습니까 ?")) {
		var url="<%=cp%>/reservation/delete.do?reservationNum=" + num;
		location.href=url;
	}
}

</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 400px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 예약 상세 </h3> 
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   회원명
				    </td>
				    <td align="left" colspan="2">
					   ${dto.memName}
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   연락처
				    </td>
				    <td align="left" colspan="2">
					   ${dto.memTel}
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   결제
				    </td>
				    <td align="left" colspan="2">
					   ${dto.isPaid=="yes" ? "O" : "X"} &nbsp;&nbsp;(<fmt:formatNumber value='${dto.price}' type='currency'/>)
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   할인
				    </td>
				    <td align="left" colspan="2">
					   ${dto.isDiscounted=="yes" ? "O" : "X"}
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   저온보관
				    </td>
				    <td align="left" colspan="2">
					   ${dto.isLowTemperature=="yes" ? "O" : "X"}
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   주문날짜
				    </td>
				    <td align="left" colspan="2">
					   ${dto.orderDate}
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   예약날짜
				    </td>
				    <td align="left" colspan="2">
					   ${dto.reservationDate}&nbsp;&nbsp;&nbsp;${dto.reservationHour} : ${dto.reservationMin}
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" style="color: darkstategray; font-weight: bold;">
					   메모
				    </td>
				    <td align="left" colspan="2">
					   ${dto.memo}
				    </td>				    
				</tr>
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td align="center" colspan="3" style="color: darkstategray; font-weight: bold;">
					   <hr> 주문 내역 <hr>
				    </td>	 		    
				</tr> 
				<c:forEach var="vo" items="${list}">
					<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
					    <td align="center">
						   ${vo.name} ${vo.productGroupKor}
					    </td>
					    <td align="left">
						   <fmt:formatNumber value='${vo.price}' type='currency'/>
					    </td>				    
					    <td align="left">
						   ${vo.cnt} 개
					    </td>				    
					</tr>
				</c:forEach>
			</table>
			
			<table style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="left">
			          <button type="button" class="btn" onclick="deleteReservation('${dto.reservationNum}');">삭제</button>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='${listUrl}';">리스트</button>
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