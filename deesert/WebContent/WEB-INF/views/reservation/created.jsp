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
function sendOk() {
    var f = document.productForm;

	var str = f.memName.value;
    if(!str || !isValidKorean(str)) {
    	memNameMsg.style.display="";	// display 하도록
        return;
    } else {
    	memNameMsg.style.display="none";
    }

	str = f.tel.value;
    if(!str || !isValidPhone(str)) {
    	telMsg.style.display="";
        f.tel.focus();
        return;
    } else {
    	telMsg.style.display="none";
    }
    
	str = f.isPaid.value;
    if(!str) {
    	isPaidMsg.style.display="";
        f.isPaid.focus();
        return;
    } else {
    	isPaidMsg.style.display="none";
    }
    
	str = f.isDiscounted.value;
    if(!str) {
    	isDiscountedMsg.style.display="";
        f.isDiscounted.focus();
        return;
    } else {
    	isDiscountedMsg.style.display="none";
    }
    
	str = f.isLowTemperature.value;
    if(!str) {
    	isLowTemperatureMsg.style.display="";
        f.isLowTemperature.focus();
        return;
    } else {
    	isLowTemperatureMsg.style.display="none";
    }
    
	str = f.orderDate.value;
    if(!str) {
    	orderDateMsg.style.display="";
        f.orderDate.focus();
        return;
    } else {
    	orderDateMsg.style.display="none";
    }
    
	str = f.reservationDate.value;
    if(!str) {
    	reservationDateMsg.style.display="";
        f.reservationDate.focus();
        return;
    } else {
    	reservationDateMsg.style.display="none";
    }
    
	str = f.reservationHour.value;
    if(!str) {
    	reservationDateMsg.style.display="";
        f.reservationHour.focus();
        return;
    } else {
    	reservationDateMsg.style.display="none";
    }
    
    
	str = f.reservationMin.value;
    if(!str) {
    	reservationDateMsg.style.display="";
        f.reservationMin.focus();
        return;
    } else {
    	reservationDateMsg.style.display="none";
    }

    str = parseInt($("#totalBuyAmt").text());
    if(str == 0) {
    	totalReservationMsg.style.display="";
        return;
    } else {
    	totalReservationMsg.style.display="none";
    }
    
    
    // 총 상품금액은 span 에 담겨 있어서 따로... 
    
    if(${mode == 'created'}) {
 		f.action="<%=cp%>/reservation/${mode}_ok.do?totalBuyAmt=" + str; 	    	
    } else {
    	
    }
 	
	f.submit();
}
    
$(function() {
	$("#orderDate").datepicker({
		showOn: "button",
		maxDate:0,
    	buttonImage: "<%=cp%>/resource/images/calendar.gif",
	    buttonImageOnly: true,
	});
});
    
$(function() {
	$("#reservationDate").datepicker({
		showOn: "button",
		minDate:0,
    	buttonImage: "<%=cp%>/resource/images/calendar.gif",
	    buttonImageOnly: true,
	});
});
    
$(function(){
	$(".buyAdd").click(function(){
		var totalBuyQty = parseInt($("#totalBuyQty").text());
		var totalBuyAmt = parseInt($("#totalBuyAmt").text());
		
		var title=$(this).parent().parent().children().eq(0).text();	// 상품명
		var code=$(this).attr("data-code");								// 상품번호
		var price=parseInt($(this).attr("data-price"));					// 상품가격
		var qty=1;														// 추가 했을 때 수량은 무조건 1
		var enableCnt = $(this).attr("data-enable");					// 최대 구매 가능 수량
		
		if(enableCnt < qty) {
			alert('최대 구매 가능한 수량을 넘었습니다!!!');
			return;
		}
		
		var t="#buyTr"+code;											// 구매리스트 상품 하나에 해당하는 tr
		if($(t).length) { // 해당 코드가 존재하면
			// qty=$(t).children().children("input[name=quantity]").val();
			qty=$(t+" input[name=" + code + "quantity]").val();						// 구매리스트의 수량 
			if(! qty) qty=0;	// 없으면 0으로 셋팅
			
			pty=parseInt(qty)+1;
			
			// 수량 넘어가면 return;
			if(enableCnt < pty) {
				alert('최대 구매 가능한 수량을 넘었습니다!!!');
				return;
			}
			
			$(t+" input[name=" + code + "quantity]").val(pty);		// 새로운 수량 셋팅
			$(t+" .productPrice").text(pty*price);		// 새로운 금액 셋팅
			
			totalBuyQty=totalBuyQty+1;
			totalBuyAmt=totalBuyAmt+price;
			$("#totalBuyQty").text(totalBuyQty);
			$("#totalBuyAmt").text(totalBuyAmt);
		
			return;
		}
		
		// 해당 코드가 존재하지 않을 경우 - 새로 만들어 준다.
		var $tr, $td, $input;
		
		var vprice = "<span class='productPrice'>"+price+"</span>원 <span class='buyCancel' data-code='"+code+"' data-enable='" + enableCnt + "' data-price='"+price+"'>×</span>";
	    $tr=$("<tr height='40' style='border-bottom: 1px solid #cccccc;' id='buyTr"+code+"'>");
	    $td=$("<td>", {width:"150", style:"text-align: center;", html:title});	// 상품명
	    $tr.append($td);
	    $td=$("<td width='180' style='text-align: right;'>");
	    $input=$("<input>", {type:"text", name:code+"quantity", class:"boxTF", style:"width: 30%;", value:qty, readonly:"readonly"});
	    $td.append($input);
	    $input=$("<input>", {type:"button", class:"btn btnPlus", value:"+"});
	    $td.append($input);
	    $input=$("<input>", {type:"button", class:"btn btnMinus", value:"-"});
	    $td.append($input);
	    $input=$("<input>", {type:"hidden", name:"code", value:code});
	    $td.append($input);
	    $tr.append($td);
	    $td=$("<td>", {width:"200", style:"text-align: right; padding-right: 5px;", html:vprice});
	    $tr.append($td);
	    
	    $("#buyList").append($tr);
		
		totalBuyQty=totalBuyQty+1;
		totalBuyAmt=totalBuyAmt+price;
		$("#totalBuyQty").text(totalBuyQty);
		$("#totalBuyAmt").text(totalBuyAmt);
	    
	});
});


$(function(){
	$("body").on("click", ".buyCancel", function(){
		var code=$(this).attr("data-code");
		var price=parseInt($(this).attr("data-price"));
		var t="#buyTr"+code;	// x 누른 항목의 id
		var qty=$(t+" input[name=" + code + "quantity]").val();
		if(! qty) qty=0;
		
		$(t).remove();	// 구매리스트에서 삭제
		
		// 수량과 금액 갱신
		var totalBuyQty = parseInt($("#totalBuyQty").text());
		var totalBuyAmt = parseInt($("#totalBuyAmt").text());
		totalBuyQty=totalBuyQty-parseInt(qty);
		totalBuyAmt=totalBuyAmt-(price*parseInt(qty));
		$("#totalBuyQty").text(totalBuyQty);
		$("#totalBuyAmt").text(totalBuyAmt);
	});
});

$(function(){
	$("body").on("click", ".btnPlus", function(){
		var code=$(this).siblings("input[name=code]").val();
		var price=parseInt($(this).parent().next().children(".buyCancel").attr("data-price"));
		var enableCnt=parseInt($(this).parent().next().children(".buyCancel").attr("data-enable"));
		var qty=parseInt($(this).parent().children("input[name=" + code + "quantity]").val());
		var productPrice=parseInt($(this).parent().next().children(".productPrice").text());
		var totalBuyQty = parseInt($("#totalBuyQty").text());
		var totalBuyAmt = parseInt($("#totalBuyAmt").text());

		qty=qty+1;
		
		if(enableCnt < qty) {
			alert('최대 구매 가능한 수량을 넘었습니다!!!');
			return;
		}
		
		productPrice=productPrice+price;
		$(this).parent().children("input[name=" + code + "quantity]").val(qty);
		$(this).parent().next().children(".productPrice").text(productPrice);
		
		totalBuyQty=totalBuyQty+1;
		totalBuyAmt=totalBuyAmt+price;
		$("#totalBuyQty").text(totalBuyQty);
		$("#totalBuyAmt").text(totalBuyAmt);
	});

	$("body").on("click", ".btnMinus", function(){
		var code=$(this).siblings("input[name=code]").val();
		var price=parseInt($(this).parent().next().children(".buyCancel").attr("data-price"));
		var qty=parseInt($(this).parent().children("input[name=" + code + "quantity]").val());
		var productPrice=parseInt($(this).parent().next().children(".productPrice").text());
		var totalBuyQty = parseInt($("#totalBuyQty").text());
		var totalBuyAmt = parseInt($("#totalBuyAmt").text());
		if(qty<=0)
			return;
		
		qty=qty-1;
		productPrice=productPrice-price;
		$(this).parent().children("input[name=" + code + "quantity]").val(qty);
		$(this).parent().next().children(".productPrice").text(productPrice);
		
		totalBuyQty=totalBuyQty-1;
		totalBuyAmt=totalBuyAmt-price;
		$("#totalBuyQty").text(totalBuyQty);
		$("#totalBuyAmt").text(totalBuyAmt);
	});
});
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
	
<div class="container">
    <div class="body-container" style="width: 900px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> ${mode=='update'?'예약수정':'예약등록'} </h3>
        </div>
        
        <div>
			<form name="productForm" method="post" >
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">회&nbsp;원&nbsp;명</td>
			      <td style="padding-left:10px;" onclick="memNameMsg.style.display = 'none'"> 
			          <input type="text" name="memName" class="boxTF" style="width: 20%;" value="">
				      <span id="memNameMsg" style="display: none; color: red;">회원명을 입력하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">연&nbsp;락&nbsp;처</td>
			      <td style="padding-left:10px;" onclick="tdlMsg.style.display = 'none'"> 
			          <input type="text" name="tel" class="boxTF" style="width: 20%;" value="">
				      <span id="telMsg" style="display: none; color: red;">연락처 형식으로 입력하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">결제&nbsp;여부</td>
			      <td style="padding-left:10px;" onclick="isPaidMsg.style.display = 'none'"> 
			      	  <label><input type="radio" name="isPaid" class="boxTF" value="yes" ${dto.isPaid=="yes" ? "checked" : ""}> 예</label>
			      	  <label><input type="radio" name="isPaid" class="boxTF" value="no" ${dto.isPaid=="no" ? "checked" : ""}> 아니요 (매장 방문 시 결제)</label>
			          <span id="isPaidMsg" style="display: none; color: red;">결제 여부를 선택하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">할인&nbsp;여부</td>
			      <td style="padding-left:10px;" onclick="isDiscountedMsg.style.display = 'none'"> 
			      	  <label><input type="radio" name="isDiscounted" class="boxTF" value="yes" ${dto.isDiscounted=="yes" ? "checked" : ""}> 예</label>
			      	  <label><input type="radio" name="isDiscounted" class="boxTF" value="no" ${dto.isDiscounted=="no" ? "checked" : ""}> 
			      	  			아니요 </label> <span style="padding-left: 150px; font-weight: bold;">[ 3일 전 예약시 10% 할인 ]</span>  
			          <span id="isDiscountedMsg" style="display: none; color: red;">할인 여부를 선택하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">저온보관&nbsp;여부</td>
			      <td style="padding-left:10px;" onclick="isLowTemperatureMsg.style.display = 'none'"> 
			      	  <label><input type="radio" name="isLowTemperature" class="boxTF" value="yes" ${dto.isDiscounted=="yes" ? "checked" : ""}> 예</label>
			      	  <label><input type="radio" name="isLowTemperature" class="boxTF" value="no" ${dto.isDiscounted=="no" ? "checked" : ""}> 
			      	  			아니요 </label>
			          <span id="isLowTemperatureMsg" style="display: none; color: red;">저온보관 여부를 선택하세요.</span>
			      </td>
			  </tr>
			 			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">주문&nbsp;날짜</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" id="orderDate" name="orderDate" readonly="readonly" class="boxTF" style="width: 20%;" value="">
				      <span id="orderDateMsg" style="display: none; color: red;">주문 날짜를 입력하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">예약&nbsp;날짜</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" id="reservationDate" name="reservationDate" readonly="readonly" class="boxTF" style="width: 20%;" value="">&nbsp;&nbsp;
			          <input type="text" id="reservationHour" name="reservationHour" class="boxTF" style="width: 5%;" value=""> 시
			          <input type="text" id="reservationMin" name="reservationMin" class="boxTF" style="width: 5%;" value=""> 분
				      <span id="reservationDateMsg" style="display: none; color: red;">예약 날짜 및 시간을 입력하세요.</span>
			      </td>
			  </tr>
			  
			  
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">메&nbsp;&nbsp;&nbsp;&nbsp;모</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			          <textarea name="memo" rows="3" style="width: 95%; resize: none;" placeholder="특이사항 예) 생일 초 개수">${dto.memo}</textarea> 
			      </td>
			  </tr>
			    
			  </table>
			<br>
				<div align="right"><span id="totalReservationMsg" style="display: none; color: red; font-weight: bold; margin-right: 50px;">↓ 예약 항목을 선택하세요</span></div>
			<br>   
				<div style="clear: both;">
					<div style="width: 400px; float: left;">
						<table style="width: 100%; border-spacing: 0px; border-collapse: collapse;">
							<tr height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
								<td bgcolor="#eeeeee" style="text-align: center;">상품명</td>
								<td width="50" bgcolor="#eeeeee" style="text-align: center;">금액</td>
								<td width="60" bgcolor="#eeeeee" style="text-align: center;">예약</td>
							</tr>
							
							<c:forEach var="dto" items="${list}">
								<tr height="35" style="border-bottom: 1px solid #cccccc;">
									<td style="text-align: center;">${dto.productGroupKor} | ${dto.name}</td>
									<td style="text-align: right; padding-right: 5px;">${dto.price}</td>
									<td style="text-align: center;"><span class="buyAdd" data-code="${dto.productNum}" 
											data-enable="${dto.cnt}" data-price="${dto.price}">추가</span></td>
								</tr>
							</c:forEach>
						</table>
					</div> 
					
					<div style="width: 450px; float: left; margin-left:20px; padding: 0 5px 5px; box-sizing: border-box;">		
					    <table style="width: 100%; border-spacing: 0px; border-collapse: collapse; border: 1px solid #cccccc; background: #eeeeee;">
					        <thead>
						    	<tr height="40" style="border-bottom: 1px solid #cccccc;">
						    		<td colspan="3"><span style="font-weight: 700; padding-left: 10px;">| 예약 리스트</span></td>
						    	</tr>
					    	</thead>
					    	
					    	<tbody id="buyList">
					    	</tbody>
					    	
					    	<tfoot>
						    	<tr height="40" style="border-top: 1px solid #cccccc;">
						    		<td align="right" colspan="3">
						    		   <span>총수량 : </span><span id="totalBuyQty">0</span>개 | 
						    		   <span style="font-weight: 700;">총 상품금액 : </span>
						    		   <span id="totalBuyAmt" style="font-weight: 900; color: #2eb1d3; font-size: 17px;">0</span>
						    		   <span style="padding-right: 10px; font-weight: 700; color: #2eb1d3;">원</span>
						    		 </td>
						    	</tr>
					    	</tfoot>
					    </table>		
					</div>	
					<br><br>	
				</div>
			
			
			<br><br>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			      
			      	<c:if test="${mode=='update'}">
			      		<input type="hidden" name="num" value="${dto.productNum}">
			      		<input type="hidden" name="imageFilename" value="${dto.fileName}">
			      	</c:if>
			      
			        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/reservation/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>

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

<div id="photoDialog">
	<div id="photoLayout"></div>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>