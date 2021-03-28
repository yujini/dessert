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

function printIncoming(data) {
	// dataCount, list
	var dataCount = data.dataCount;	
	
	var out = "";
	out += "<table style='width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;'>";
	if(dataCount != 0) {
		for(var idx = 0; idx<data.list.length; idx++) {
			// iDate, productGroupKor, productNum, name, cnt
			var incomingNum = data.list[idx].incomingNum;
			var iDate = data.list[idx].iDate;
			var productGroupKor = data.list[idx].productGroupKor;
			var productNum = data.list[idx].productNum;
			var name = data.list[idx].name;
			var cnt = data.list[idx].cnt;
			
			out += "<tr align='center' bgcolor='#ffffff' height='35' style='border-bottom: 1px solid #cccccc;'>"; 
			out += "<td width='60'><input type='checkbox' name='chks' value='"+incomingNum+"'></td>"; // 입고번호
			out += "<td width='100'>" + iDate + "</td>";
			out += "<td>" + productGroupKor + "</td>";
			out += "<td width='100'>" + productNum + "</td>";
			out += "<td width='250'>" + name + "</td>";
			out += "<td width='60'>" + cnt + "</td>";
			out += "</tr>"			
		}	  
	} else { // dataCount = 0
		out += "<tr align='center' bgcolor='#ffffff' height='35' style='border-bottom: 1px solid #cccccc;'>"; 
		out += "<td> 검색 결과가 없습니다. </td>";
		out += "</tr>"
	}
	out += "</table>";	
	
	$("#listIncoming").html(out);
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
	var uid = "${sessionScope.member.userId}";
	if(!uid) {
		alert('로그인이 필요합니다.');
		return;
	}
	
	var f = document.searchForm;
	
	var query = "startDate=" + $("#startDate").val() + "&endDate=" + $("#endDate").val();	
	var url = "<%=cp%>/incoming/search.do";
		
	$.ajax({
		type:"post",
		url:url,
		data:query,
		dataType:"json",
		success:function(data) {
			printIncoming(data);
		},
		beforeSend:function() {
			if(! check()) {
				return false;
			}
			
			if(getDiffDays(f.startDate.value, f.endDate.value) < 0) {
				alert('시작일은 종료일보다 클 수 없습니다.');
				f.startDate.focus();
				return false;
			}
		},
		error:function(e) {
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
	var cnt = $("input[name=chks]:checked").length;
		
	// ajax 로 해보려고 했으나 chks 배열 보내는 법을 모르겠다!
	if(cnt == 0) {
		alert('삭제할 항목을 선택하세요!');
		return false;
	} 
	
	if(!confirm('정말 삭제하시겠습니까?')) {
		return false;		
	}

	f.action = "<%=cp%>/incoming/delete.do";
	f.submit();

}

function updateIncoming() {
	var uid = "${sessionScope.member.userId}";
	if(!uid) {
		alert('로그인이 필요합니다.');
		return;
	}
	
	var f = document.searchForm;	

	if(! check()) {
		return false;
	}
	
	if(!(getDiffDays(f.endDate.value, f.startDate.value) == 0)) {
		alert('시작일과 종료일은 같아야 합니다.');
		f.endDate.focus();
		return false;
	}
	
	f.action = "<%=cp%>/incoming/update.do";
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
            <h3><span style="font-family: Webdings">2</span> 입고내역 </h3>
        </div>
        
        
		<form name="searchForm" method="post">
        
        <div>
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/incoming/list.do';">새로고침</button>
			      </td>
			      <td align="right" width="100">
			          <button type="button" class="btn" onclick="deleteIncoming();">입고삭제</button>
			          <button type="button" class="btn" onclick="updateIncoming();">입고수정</button>
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/incoming/created.do';">입고등록</button>
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
			      <th width="60" style="color: #787878;"><input type="checkbox" name="chkAll" id="chkAll"></th>
			      <th width="100" style="color: #787878;">입고일</th>   
			      <th style="color: #787878;">상품분류</th>
			      <th width="100" style="color: #787878;">상품코드</th>
			      <th width="250" style="color: #787878;">상품명</th>
			      <th width="60" style="color: #787878;">수량</th> 
			  </tr>	
			</table>	
				 
				 <!--  입고 리스트  -->
			<div id="listIncoming" style="width:100%; margin: 0px auto;"></div>
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