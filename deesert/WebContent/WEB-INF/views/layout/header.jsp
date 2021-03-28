<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<script type="text/javascript">
//엔터 처리
$(function(){
	   $("input").not($(":button")).keypress(function (evt) {
	        if (evt.keyCode == 13) {
	            var fields = $(this).parents('form,body').find('button,input,textarea,select');
	            var index = fields.index(this);
	            if ( index > -1 && ( index + 1 ) < fields.length ) {
	                fields.eq( index + 1 ).focus();
	            }
	            return false;
	        }
	     });
});
</script>

<div class="header-top">
    <div class="header-left">
        <p style="margin: 2px;">
            <a href="<%=cp%>/" style="text-decoration: none;">
                <span style="width: 200px; height: 70; position: relative; left: 0; top:20px; color: #2984ff; filter: mask(color=red) shadow(direction=135) chroma(color=red);font-style: italic; font-family: arial black; font-size: 30px; font-weight: bold;">
					<img alt="" src="<%=cp%>/resource/images/logo.JPG">
				</span>
            </a>
        </p>
    </div>
    
    <div class="header-right">
        <div style="padding-top: 20px;  float: right;">
            <c:if test="${empty sessionScope.member}">
                <a href="<%=cp%>/member/login.do">로그인</a>
                    &nbsp;|&nbsp;
                <a href="<%=cp%>/member/member.do">회원가입</a>
            </c:if>
            <c:if test="${not empty sessionScope.member}">	<!--  존재하면 -->
                <span style="color:blue;">${sessionScope.member.userName}</span>님
                    &nbsp;|&nbsp;
                    <a href="<%=cp%>/member/logout.do">로그아웃</a>
                    &nbsp;|&nbsp;
                    <a href="<%=cp%>/member/pwd.do?mode=update">정보수정</a>	<!-- pwd : 회원탈퇴, 정보수정 할 때 쓰므로 mode를 넘긴다. -->
            </c:if>
        </div>
    </div>
</div>

<div class="menu">
    <ul class="nav">
        <li>
            <a href="<%=cp%>/company/greeting.do">회사소개</a>
            <!-- 
            <ul>
                <li><a href="/company/greeting.do">인사말</a></li>
                <li><a href="#">찾아오시는길</a></li>
            </ul>
             -->
        </li>
			
        <li>
            <a href="#">예약</a>
            <ul>
                <li><a href="<%=cp%>/product/list.do" style="margin-left:100px; ">상품소개</a></li>
                <li><a href="<%=cp%>/reservation/inquiry.do">예약문의</a></li>
            </ul>
        </li>

		<c:if test="${sessionScope.member.userId == 'admin'}">
        <li>
            <a href="#">상품관리</a>
            <ul>
                <li><a href="<%=cp%>/product/list.do" style="margin-left:150px; " onmouseover="this.style.marginLeft='150px';">재고관리</a></li>
                <li><a href="<%=cp%>/incoming/list.do">입고관리</a></li>
                <li><a href="<%=cp%>/sale/list.do">판매등록</a></li>
            </ul>
        </li>
        </c:if>
        
		<c:if test="${sessionScope.member.userId == 'admin'}">
        <li>
            <a href="#">예약관리</a>
            <ul>
                <li><a href="<%=cp%>/reservation/calendar.do" style="margin-left:300px;" >예약현황</a></li>	<!-- 달력으로 한눈에 -->
                <li><a href="<%=cp%>/reservation/list.do">예약등록</a></li>		<!-- 게시글 형태로 리스트로 보는 것 -->
            </ul>
        </li>
        </c:if>

<!-- 
        <c:if test="${not empty sessionScope.member}">
        <li>
            <a href="#">마이페이지</a>
            <ul>
                <li><a href="#" style="margin-left:350px; " onmouseover="this.style.marginLeft='230px';">정보확인</a></li>
                <li><a href="#">일정관리</a></li>
            </ul>
        </li>
        </c:if>

			
        <li style="float: right;"><a href="#">전체보기</a></li>
-->
    </ul>      
</div>

<div class="navigation">
<!-- 
	<div class="nav-bar">홈 &gt; 회사소개</div>
-->
</div>