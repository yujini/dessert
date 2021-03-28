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
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  
<style type="text/css">

</style>

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
function check() {
    if(!$("#date1").val()) {
		alert('날짜를 선택하세요!');
		return false;
	}
    
	// 모두 입력했는지 검사
    var cnts = $("#cnt");
    for(var i = 0; i < cnts.length; i++) {
    	if(!$(cnts[i]).val()) {
    		alert('수량을 모두 입력하세요!');
	    	return false;
    	}
    }

    // 숫자 입력했는지 검사
    s = true;
    for(var i = 0; i < cnts.length; i++) {
    	if(!isValidNumber($(cnts[i]).val())) {
    		alert('수량은 숫자만 입력 가능합니다!');
    		return false;
    	}     
    }
    return true;
}

function sendOk() {	
    if(! check()) {
    	return false;
    }   
    
    var f = document.incomingForm;
    
	var mode="${mode}";
	if(mode=="created")
		f.action="<%=cp%>/incoming/created_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/incoming/update_ok.do";

    f.submit();
}


$(function() {
	$("#date1").datepicker({
		showOn: "button",
		maxDate:0,
    	buttonImage: "<%=cp%>/resource/images/calendar.gif",
	    buttonImageOnly: true,
	    buttonText: "Select date"
	});
});
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 500px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> ${mode=='update'?'입고내역 수정':'입고내역 등록'} </h3>
        </div>
        
        <div>
			<form name="incomingForm" method="post">
				<div align="center"> 입고일 : <input type="text" id="date1" name="date1" readonly="readonly" value="${date}"></div>
			
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
				  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
				      <th width="30%" bgcolor="#eeeeee" style="text-align: center;">분&nbsp;&nbsp;&nbsp;류</th>
				      <th width="10%" bgcolor="#eeeeee" style="text-align: center;">상&nbsp;품&nbsp;번&nbsp;호</th>
				      <th width="40%" bgcolor="#eeeeee" style="text-align: center;">상&nbsp;품&nbsp;명</th>
			     	  <th width="20%" bgcolor="#eeeeee" style="text-align: center;">수&nbsp;&nbsp;&nbsp;량</th>
				  </tr>
			
			 <c:forEach items="${list}" var="dto">
				  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
				      <td style="padding:0px 0px 0px 20px;">
				      		<div>${dto.productGroupKor}</div>
					  </td>
				      <td style="padding:0px 0px 0px 20px;"> 
				      		<div>${dto.productNum}</div>
				      </td>
				      <td style="padding:0px 0px 0px 20px;"> 
						    <div>${dto.name}</div>
				      </td>
				      <td align="center">  				      
					      <!-- create 일 경우 dto.productNum 이 나와야 하고 		특정 상품의 수량을 등록 -->
					      <c:if test="${mode=='created'}">
					          <input type="text" id="cnt" name="${dto.productNum}" value="${dto.cnt}" class="boxTF" style="width: 30%;"> 
					      </c:if>
	
					      <!-- update 일 경우 dto.incomingNum 이 나와야 한다. 	특정 입고번호의 수량을 변경.       이렇게 해야 상품이 여러개일 경우도 변경 가능 -->
					      <c:if test="${mode=='update'}">
					          <input type="text" id="cnt" name="${dto.incomingNum}" value="${dto.cnt}" class="boxTF" style="width: 30%;"> 
					      </c:if>
					  </td>
				  </tr>		
			  </c:forEach>
			  	
			  	
			  </table> 
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn" onclick="return sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/incoming/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			      </td>
			    </tr>
			  </table>
			</form>
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