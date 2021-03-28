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

	var str = f.productGroup.value;
    if(!str) {
    	productGroupMsg.style.display="";	// display 하도록
        return;
    }

	str = f.name.value;
    if(!str) {
    	nameMsg.style.display="";
        f.name.focus();
        return;
    } else {
    	nameMsg.style.display="none";
    }
    
	str = f.price.value;
    if(!str) {
    	priceMsg1.style.display="";
        f.price.focus();
        return;
    } else {
    	priceMsg1.style.display="none";
    }
    
    if(isNaN(str)) {	// 숫자가 아니면 참 - 스펠링 조심!!!!
    	priceMsg2.style.display="";
    	f.price.value = "";
    	f.price.focus();
        return;
    } else {
    	priceMsg2.style.display="none";
    }

    str = f.cnt.value;
    if(!str) {
    	cntMsg1.style.display="";
    	f.cnt.value = "";
        f.cnt.focus();
        return;
    } else {
    	cntMsg1.style.display="none";
    }
    
    if(!isFinite(str)) {	// 숫자이면 참
    	cntMsg2.style.display="";
    	f.cnt.value = "";
    	f.cnt.focus();
    	return;
    } else {
    	cntMsg2.style.display="none";
    }
    
    str = f.isBest.value;
    if(!str) {
    	isBestMsg.style.display="";
        return;
    }

    str = f.memo.value;
    if(!str) {
    	memoMsg.style.display="";
        f.memo.focus();
        return;
    } else {
    	memoMsg.style.display="none";
    }
    
    str = f.upload.value;
    if(mode=="created"  && !str) {
    	uploadMsg1.style.display="";
        f.upload.focus();
        return;
    } else {
    	uploadMsg1.style.display="none";
    }
    
    /*어짜피  alt="image/*"  해줘서 괜찮지만 또 한다.*/
    // 정규식 i : 대소문자 구분
    var mode = "${mode}";
    if(mode=="created" || mode=="update" && f.upload.value != "") {
    	if(! /(\.gif|\.jpg|\.png|\.jpeg)$/i.test(f.upload.value)) {	
    		uploadMsg2.style.display="";
    		f.upload.focus();
    		return;
    	} else {
    		uploadMsg2.style.display="none";
    	}
    }    
    
	f.action="<%=cp%>/product/${mode}_ok.do";
    f.submit();
}
   
<c:if test="${mode == 'update'}">
	function viewerImage() {
		var url = "<%=cp%>/uploads/photo/${dto.fileName}";
		var img = "<img src='"+url+"' width='570' height='450'>";
		$("#photoLayout").html(img);
		
		$("#photoDialog").dialog({
			title:"이미지",
			width:600,
			height:520,
			modal:true			
		});
		
	}
</c:if>
    
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
	
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> ${mode=='update'?'상품수정':'상품등록'} </h3>
        </div>
        
        <div>
			<form name="productForm" method="post" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">분&nbsp;&nbsp;&nbsp;&nbsp;류</td>
			      <td style="padding-left:10px;" onclick="productGroupMsg.style.display = 'none'"> 
			      	  <!-- radio -->
			          <label><input type="radio" name="productGroup" class="boxTF" value="wholecake1" ${dto.productGroup=="wholecake1" ? "checked" : ""}>홀케이크(1호)</label>
			          <label><input type="radio" name="productGroup" class="boxTF" value="wholecake2" ${dto.productGroup=="wholecake2" ? "checked" : ""}>홀케이크(2호)</label>
			          <label><input type="radio" name="productGroup" class="boxTF" value="wholecake4" ${dto.productGroup=="wholecake4" ? "checked" : ""}>홀케이크(4호)</label>
			          <br>
			          <label><input type="radio" name="productGroup" class="boxTF" value="piececake" ${dto.productGroup=="piececake" ? "checked" : ""}>케이크(조각)</label>
			          <label><input type="radio" name="productGroup" class="boxTF" value="macaron" ${dto.productGroup=="macaron" ? "checked" : ""}>마카롱</label>
			          <span id="productGroupMsg" style="display: none; color: red;">상품 분류를 선택하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">상&nbsp;품&nbsp;명</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="name" class="boxTF" style="width: 40%;" value="${dto.name}">
			          <span id="nameMsg" style="display: none; color: red;">상품명을 입력하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">가&nbsp;&nbsp;&nbsp;&nbsp;격</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="price" class="boxTF" style="width: 40%;" value="${dto.price}">
			          <span id="priceMsg1" style="display: none; color: red;">가격을 입력하세요.</span>
			          <span id="priceMsg2" style="display: none; color: red;">가격은 숫자만 입력 가능합니다.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">수&nbsp;&nbsp;&nbsp;&nbsp;량</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="cnt" class="boxTF" style="width: 40%;" value="${dto.cnt}">
			          <span id="cntMsg1" style="display: none; color: red;">수량을 입력하세요.</span>
			          <span id="cntMsg2" style="display: none; color: red;">수량은 숫자만 입력 가능합니다.</span>
			      </td>
			  </tr>

			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">인&nbsp;기&nbsp;상&nbsp;품</td>
			      <td style="padding-left:10px;" onclick="isBestMsg.style.display = 'none'"> 
			      	  <label><input type="radio" name="isBest" class="boxTF" value="yes" ${dto.isBest=="yes" ? "checked" : ""}> 예</label>
			      	  <label><input type="radio" name="isBest" class="boxTF" value="no" ${dto.isBest=="no" ? "checked" : ""}> 아니요</label>
			          <span id="isBestMsg" style="display: none; color: red;">인기상품 여부를 선택하세요.</span>
			      </td>
			  </tr>
			  
			  <c:if test="${mode =='update'}">
				  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">판&nbsp;매&nbsp;여&nbsp;부</td>
				      <td style="padding-left:10px;"> 
				      	  <label><input type="radio" name="isSale" class="boxTF" value="yes" ${dto.isSale=="yes" ? "checked" : ""}> 예</label>
				      	  <label><input type="radio" name="isSale" class="boxTF" value="no" ${dto.isSale=="no" ? "checked" : ""}> 아니요</label>
				      </td>
				  </tr>
			  </c:if>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">설&nbsp;&nbsp;&nbsp;&nbsp;명</td>
			      <td height="185" valign="top" style="padding:5px 0px 5px 10px;"> 
			          <textarea name="memo" rows="5" class="boxTA" style="width: 95%;">${dto.memo}</textarea>
			          <span id="memoMsg" style="display: none; color: red;"> 상품 설명을 입력하세요.</span>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">상품이미지</td>
			      <td style="padding-left:10px;"> 
			          <input type="file" name="upload" alt="image/*" class="boxTF" size="53" style="height: 25px; width: 40%;">
			          <span id="uploadMsg1" style="display: none; color: red;"> 상품 사진을 선택하세요. </span>
			          <span id="uploadMsg2" style="display: none; color: red;"> 이미지 파일만 가능합니다! </span>
			       </td>
			  </tr> 
			  
			  <c:if test="${mode =='update'}">
				  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">등록이미지</td>
				      <td style="padding-left:10px;"> 
				          <img src="<%=cp%>/uploads/photo/${dto.fileName}" width="30" height="25" onclick="viewerImage();"
				          	style="cursor: pointer;">
				      </td>
				  </tr> 			  
			  </c:if>
			  			  
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			      
			      	<c:if test="${mode=='update'}">
			      		<input type="hidden" name="num" value="${dto.productNum}">
			      		<input type="hidden" name="imageFilename" value="${dto.fileName}">
			      	</c:if>
			      
			        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/product/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>

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