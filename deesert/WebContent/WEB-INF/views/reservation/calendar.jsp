<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>


<%
		request.setCharacterEncoding("utf-8");
		Calendar cal = Calendar.getInstance();
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		
		String sy = request.getParameter("year");
		String sm = request.getParameter("month");
		if(sy!=null)
			year = Integer.parseInt(sy);
		if(sm!=null)
			month = Integer.parseInt(sm);
		
		// 월이 12를 넘어가면 년을 +1	// 월이 1보다 작아지면 년을 -1
		cal.set(year, month-1, 1);	// 자동으로 월이 증감	// y년, m월, 1일로 calendar 객체를 설정
		year = cal.get(Calendar.YEAR);	// 변경된 날짜를 다시 가져온다!!!! 가져와야 화면상에도 다르게 표시된다.
		month = cal.get(Calendar.MONTH)+1;
		
		int week = cal.get(Calendar.DAY_OF_WEEK); // 1(일)~7(토)
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<style type="text/css">
.titleDate {
   display: inline-block;
   font-weight: 600; 
   font-size: 19px;
   font-family: 나눔고딕, "맑은 고딕", 돋움, sans-serif;
   padding:2px 4px 4px;
   text-align:center;
   position: relative;
   top: 4px;
}
.btnDate {
   display: inline-block;
   font-size: 10px;
   font-family: 나눔고딕, "맑은 고딕", 돋움, sans-serif;
   color:#333333;
   padding:3px 5px 5px;
   border:1px solid #cccccc;
    background-color:#fff;
    text-align:center;
    cursor: pointer;
    border-radius:2px;
}
.textDate {
      font-weight: 500; cursor: pointer;  display: block; color:#333333;
}
.preMonthDate, .nextMonthDate {
      color:#aaaaaa;
}
.nowDate {
      color:#111111;
}
.saturdayDate{
      color:#0000ff;
}
.sundayDate{
      color:#ff0000;
}

.scheduleSubject {
   display:block;
   /*width:100%;*/
   width:110px;
   margin:1.5px 0;
   font-size:13px;
   color:#555555;
   background:#eeeeee;
   cursor: pointer;
   white-space:nowrap; overflow:hidden; text-overflow:ellipsis;
}
.scheduleMore {
   display:block;
   width:110px;
   margin:0 0 1.5px;
   font-size:13px;
   color:#555555;
   cursor: pointer;
   text-align:right;
}
</style>

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.12.4.min.js"></script>

<script type="text/javascript">

function changeDate(year, month) {
	var url = "<%=cp%>/reservation/calendar.do?year="+year+"&month="+month;
	location.href=url;
}

$(function() {
	$(".scheduleSubject").click(function() {
		var reservationNum = $(this).attr("data-num");
		var url = "<%=cp%>/reservation/article.do?reservationNum=" + reservationNum + "&page=1";
		location.href=url;
	});
});

</script>


</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="container">
    <div class="body-container" style="width: 800px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 예약 현황 </h3> 
        </div>

         <div id="tab-content" style="clear:both; padding: 20px 0px 0px;">
         
               <table style="width: 840px; margin:0px auto; border-spacing: 0;" >
                  <tr height="60">
                       <td width="200">&nbsp;</td>
                       <td align="center">
                           <span class="btnDate" onclick="changeDate(${year}, ${month-1});">＜</span>
                           <span class="titleDate">${year}年 ${month}月</span>
                           <span class="btnDate" onclick="changeDate(${year}, ${month+1});">＞</span>
                       </td>
                       <td width="200">&nbsp;</td>
                  </tr>
               </table>
               
             <table id="largeCalendar" style="width: 840px; margin:0px auto; border-spacing: 1px; background: #cccccc;" >
               <tr align="center" height="30" bgcolor="#ffffff">
                  <td width="120" style="color:#ff0000;">일</td>
                  <td width="120">월</td>
                  <td width="120">화</td>
                  <td width="120">수</td>
                  <td width="120">목</td>
                  <td width="120">금</td>
                  <td width="120" style="color:#0000ff;">토</td>
               </tr>

	            <c:forEach var="row" items="${days}" >   <!-- days :  2차원 배열           row : 한 줄-->
	                  <tr align="left" height="120" valign="top" bgcolor="#ffffff">
	                     <c:forEach var="d" items="${row}">
	                        <td style="padding: 5px; box-sizing:border-box;">
	                           ${d}
	                        </td>
	                     </c:forEach>
	                  </tr>
	            </c:forEach>
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
