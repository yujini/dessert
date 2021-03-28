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
	// 실시간 계산은 keyup. change가 더 낫다;;
	$("input[name*=saleCnt]").change(function (evt) {
		var fields = $("input[name*=saleCnt]");
		var index = fields.index(this);
						
		var product = $(this).closest("tr").find("input[name=productCnt]").val().trim();
		var sale = $(this).val();
		var remain = $(this).closest("tr").find("input[name=remainCnt]");
		
		var p = $.isNumeric(product) ? parseInt(product) : 0;
		var s = $.isNumeric(sale) ? parseInt(sale) : 0;
		
		var sub = p - s;
		
		if(s > p) {
			$("#notice").show();
			$(this).val("");
			remain.val("");
		} else if(s >= 0) {
			remain.val(sub);
			$("#notice").hide();
		} else {
			$("#notice").hide();			
		}			
	});
	
	// 엔터 처리는 keydown으로 
	$("input[name*=saleCnt]").keydown(function (evt) {
		var fields = $("input[name*=saleCnt]");
		var index = fields.index(this);
		
		if (evt.keyCode == 13) {
            if ( index > -1 && index < fields.length ) {	
                fields.eq( index + 1 ).focus();
            } else {							
            	
            }
            return false;
        }		
	});
});

function check() {
	// 여기서 한번 더 검사해야 한다.
	
    if(!$("#date1").val()) {
		alert('날짜를 선택하세요!');
		return false;
	}
    if(!$("input[name=division]:checked").val()) {
    	alert('구분을 선택하세요!');
		return false;
    }
    
	// 모두 입력했는지 검사
    var cnts = $("input[name*=saleCnt]");
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
    
    var f = document.saleForm;
    
	var mode="${mode}";
	if(mode=="created")
		f.action="<%=cp%>/sale/created_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/sale/update_ok.do";

    f.submit();
}


$(function() {
	if(${mode=="created"}) {
		$("#date1").datepicker({
			showOn: "button",
			maxDate:0,
	    	buttonImage: "<%=cp%>/resource/images/calendar.gif",
		    buttonImageOnly: true,
		    buttonText: "Select date"
		});
	}
});
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> ${mode=='update'?'판매 수정':'판매 등록'} </h3>
        </div>
        
        <div>
			<form name="saleForm" method="post">
				<div align="center"> 판매일 : <input type="text" id="date1" name="date1" readonly="readonly" value="${date}"></div>
				<div align="center"> 구분 : &nbsp;&nbsp;

					<label><input type="radio" name="division" value="morning" ${division=="morning" ? "checked disabled" : ""}>오전</label>&nbsp;&nbsp;
					<label><input type="radio" name="division" value="afternoon" ${division=="afternoon" ? "checked disabled" : ""}>오후</label>&nbsp;&nbsp;
					<label><input type="radio" name="division" value="finish" ${division=="finish" ? "checked disabled" : ""}>마감</label>&nbsp;
					
					<!-- update시 넘어가는 값 -->
					<c:if test="${mode=='update'}">
						<input type="hidden" name="divisionUpdate" value="${division}">
					</c:if>
					
				</div>
				<br>
				<div id="notice" align="center" style="color: tomato;font-weight: bold; font-style: italic; display:none;" >
					 * 판매수량은 이전수량보다 작아야 합니다. * </div>
			
				<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
				  <tr align="center" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
				      <th width="20%" bgcolor="#eeeeee" style="text-align: center;">분&nbsp;&nbsp;&nbsp;류</th>
				      <th width="35%" bgcolor="#eeeeee" style="text-align: center;">상&nbsp;품&nbsp;명</th>
			     	  <th width="15%" bgcolor="#eeeeee" style="text-align: center;">이전&nbsp;수량</th>
			     	  <th width="15%" bgcolor="#eeeeee" style="text-align: center;">판매&nbsp;수량</th>
			     	  <th width="15%" bgcolor="#eeeeee" style="text-align: center;">남은&nbsp;수량</th>
				  </tr>
			
			 <c:forEach items="${list}" var="dto">
				  <tr align="center" height="40" style="border-bottom: 1px solid #cccccc;"> 
				      <td>
				      		<div>${dto.productGroupKor}</div>
					  </td>
				      <td> 
				      		<div>${dto.name}</div>
				      </td>
				      <td>  
				      		<input type="text" size="2" class="productCnt" name="productCnt" value="${dto.productCnt}" readonly="readonly" style="border: none; text-align: center;">
				      </td>
				      <td> 
				      		<c:if test="${mode=='created'}">		<!-- mode 가 create -->
						    <input type="text" size="2" maxlength="3" name="saleCnt${dto.productNum}" class="boxTF"> 
						    </c:if>

				      		<c:if test="${mode=='update'}">		<!-- mode 가 update -> value설정  -->
							    <input type="text" size="2" maxlength="3" name="saleCnt${dto.productNum}" class="boxTF" value="${dto.saleCnt}"> 
						    </c:if>
				      </td>
				      <td>    <!-- productCnt 보다 saleCnt 가 많으면 막기 !!!!!!!!!!!!!  -->
				       		<input type="text" size="2" name="remainCnt" readonly="readonly" style="border: none; text-align: center;"
				       				value="${dto.productCnt - dto.saleCnt}">				       						      		
				      </td>
				  </tr>		
			  </c:forEach>
			  	
			  	
			  </table> 
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn" onclick="return sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/sale/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
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