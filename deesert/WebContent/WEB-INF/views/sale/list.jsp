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
function setInputDate(option, value) {
	var startDate = document.getElementById("startDate");
	var endDate = document.getElementById("endDate");
	
	var date = new Date();
	endDate.value = dateToString(date);
	
	if(option == "day") {
		startDate.value = dateToString(date);
	} else if(option == "week") {
		date.setDate(date.getDate()-7);
		startDate.value = dateToString(date);
	} else if(option == "month") {
		date.setMonth(date.getMonth()-value);
		date.setDate(date.getDate()+1);
		startDate.value = dateToString(date);
	} else if(option == "year") {
		date.setFullYear(date.getFullYear()-value);
		date.setDate(date.getDate()+1);
		startDate.value = dateToString(date);
	} 	
}

function check() {
	var f = document.searchForm;
	
	if(! $("#startDate").val().trim()) {
		$("#startDate").focus();
		return false;
	}
	if(! $("#endDate").val().trim()) {
		$("#endDate").focus();
		return false;
	}			
	if(! isValidDateFormat(f.startDate.value)) {
		f.startDate.focus();
		return false;
	}
	if(! isValidDateFormat(f.endDate.value)) {
		f.endDate.focus();
		return false;
	}
	if(getDiffDays(f.endDate.value, dateToString(new Date())) < 0) {
		alert('종료일은 오늘보다 클 수 없습니다.');
		f.endDate.focus();
		return false;
	}
	return true;
}

function searchList() {
	var f = document.searchForm;
		
	var uid = "${sessionScope.member.userId}";
	if(!uid) {
		alert('로그인이 필요합니다.');
		return;
	}
	
	var query = "startDate=" + $("#startDate").val() + "&endDate=" + $("#endDate").val();	
	var url = "<%=cp%>/sale/search.do";
		
	$.ajax({
		type:"post",
		url:url,
		data:query,
		success:function(data) {
			$("#saleList").html(data);
		},
		beforeSend:function(jqXHR) {
			if(! check()) {
				return false;
			}
			
			if(getDiffDays(f.startDate.value, f.endDate.value) < 0) {
				alert('시작일은 종료일보다 클 수 없습니다.');
				f.startDate.focus();
				return false;
			}
			
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status == 403) {
				location.href = "<%=cp%>/member/login.do";
				return;
			}
			console.log(e.responseText);
		}
	});
}

$(function() {
	$("#chkAll").click(function () {
		if($("#chkAll").is(":checked")) {
			$("input[name=chks]").prop("checked", "true");
		} else {
			$("input[name=chks]").removeAttr("checked");
		}
	});
});
 
function deleteIncoming() {
	var uid = "${sessionScope.member.userId}";
	if(!uid) {
		alert('로그인이 필요합니다.');
		return;
	}	
	
	var f = document.searchForm;	
	var cnt = $("input[name=saleselect]:checked").length;
	if(cnt == 0) {
		alert('삭제할 항목을 선택하세요!');
		return false;
	} 
	
	if(!confirm('정말 삭제하시겠습니까?')) {
		return false;		
	}
	
	var spiltarr = $("input[name=saleselect]:checked").val().split("::");	
	var query = "saleDate=" + spiltarr[0] + "&division=" + spiltarr[1] + 
				"&startDate=" + $("#startDate").val() + "&endDate=" + $("#endDate").val();  
	
	var url = "<%=cp%>/sale/delete.do";
	$.ajax({
		type:"post",
		url:url,
		data:query,
		success:function(data) {
			$("#saleList").html(data);
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status == 403) {
				location.href = "<%=cp%>/member/login.do";
				return;
			}
			console.log(e.responseText);
		}
	});

}

function updateIncoming() {
	var uid = "${sessionScope.member.userId}";
	if(!uid) {
		alert('로그인이 필요합니다.');
		return;
	}
	
	var f = document.searchForm;
	
	var cnt = $("input[name=saleselect]:checked").length;
	if(cnt == 0) {
		alert('수정할 항목을 선택하세요!');
		return false;
	} 
	
	var spiltarr = $("input[name=saleselect]:checked").val().split("::");	
	var query = "saleDate=" + spiltarr[0] + "&division=" + spiltarr[1];	
	location.href='<%=cp%>/sale/update.do?' + query;	
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
            <h3><span style="font-family: Webdings">2</span> 판매내역 </h3>
        </div>
        
        
		<form name="searchForm" method="post">
        
        <div>
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/sale/list.do';">새로고침</button>
			      </td>
			      <td align="right" width="100">
			          <button type="button" class="btn" onclick="deleteIncoming();">판매삭제</button>
			          <button type="button" class="btn" onclick="updateIncoming();">판매수정</button>
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/sale/created.do';">판매등록</button>
			      </td>
			   </tr>
			</table>
			
			<div style="width: 700px; margin: 10px auto;">
					<button type="button" class="btn" onclick="setInputDate('day')">오늘</button>
					<button type="button" class="btn" onclick="setInputDate('week', 1)">1주일</button>
					<button type="button" class="btn" onclick="setInputDate('month', 1)">1개월</button>
					<button type="button" class="btn" onclick="setInputDate('month', 3)">3개월</button>
					<button type="button" class="btn" onclick="setInputDate('month', 6)">6개월</button>
					<button type="button" class="btn" onclick="setInputDate('year', 1)">1년</button>
					
					<input type="text" name="startDate" id="startDate" class="boxTF" size="15" value=""> 
					~
					<input type="text" name="endDate" id="endDate" class="boxTF" size="15" value="">
					<button type="button" class="btn" onclick="searchList()">검색</button>
			</div>
			<div align="center" style="color: red; font-weight: bold;"> ${msg} </div>
			<br>
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="center" bgcolor="#eeeeee" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <th width="20%" style="color: #787878;">선택</th>
			      <th width="30%" style="color: #787878;">판매일</th>   
			      <th width="20%" style="color: #787878;">구분</th> 
			      <th width="30%" style="color: #787878;">금액</th> 
			  </tr>	
			</table>	
				 
				 <!--  판매 리스트  -->
			<div id="saleList" style="width:100%; margin: 0px auto;"></div>
        </div>

		</form>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>