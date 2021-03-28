<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String cp=request.getContextPath();
%>

<c:if test="${dataCount != 0}">
<table style='width: 100%; margin: 10px auto 30px; border-spacing: 0px;'>
	<tr height="35">
	    <td>
	       <div style="clear: both;">
	           <div style="float: left;"><span style="color: #3EA9CD; font-weight: bold;">댓글 ${dataCount}개</span> <span>[댓글 목록, ${pageNo}/${total_page} 페이지]</span></div>
	           <div style="float: right; text-align: right;"></div>
	       </div>
	    </td>
	</tr>

	<c:forEach var="vo" items="${list}">
	    <tr height='35' style='background: #eee;'>			<!--  여기 style article.jsp 에 옮겨 놓으면 여기에 적용된다. -->
	       <td width='50%' style='padding:5px 5px; border:1px solid #cccccc; border-right:none;'>
	           <span><b>${vo.userName}</b></span>
	        </td>
	       <td width='50%' style='padding:5px 5px; border:1px solid #cccccc; border-left:none;' align='right'>
	           <span>${vo.created}</span> |
			  			
			  <!-- 글쓴이면 삭제, 아니면 신고 -->			  <!-- 여기에 body 태그 들어가면 안된다. !!!!!!!!!!!!! --> 
			  <c:if test="${sessionScope.member.userId == vo.userId || sessionScope.member.userId == 'admin'}">
			  		<span class="deleteReply" style="cursor: pointer;" data-replyNum="${vo.replyNum}" data-pageNo="${pageNo}">삭제</span>	
			  </c:if>
			  <c:if test="${sessionScope.member.userId != vo.userId && sessionScope.member.userId != 'admin'}">
			  		<span class="notifyReply" style="cursor: pointer;" >신고</span>	
			  </c:if>
					
	        </td>
	    </tr>
	    <tr>
	        <td colspan='2' valign='top' style='padding:5px 5px;'>
	           ${vo.content}
	        </td>
	    </tr>
	    <tr>
	        <td style='padding:7px 5px;'>																		<!-- ******************************* -->
	            <button type='button' class='btn btnReplyAnswerLayout' data-replyNum='${vo.replyNum}'>답글 <span id="answerCount${vo.replyNum}">${vo.answerCount}</span></button>
	        </td>
	        <!-- 
	        <td style='padding:7px 5px;' align='right'>
	            <button type='button' class='btn' onclick="">좋아요 </button>
	            <button type='button' class='btn' onclick="">싫어요 </button>
	        </td>
	         -->
	    </tr>
	
	    <tr class='replyAnswer' style='display: none;'>
	        <td colspan='2'>
	            <div id='listReplyAnswer${vo.replyNum}' data-page="${pageNo}" class='answerList' style='border-top: 1px solid #ccc;'></div>	<!-- 댓글 나오는 자리 -->
	            <div style='clear: both; padding: 10px 10px;'>															<!-- 댓글 입력하는 자리 -->
	                <div style='float: left; width: 5%;'>└</div>
	                <div style='float: left; width:95%'>
	                    <textarea cols='72' rows='12' class='boxTA' style='width:98%; height: 70px;'></textarea>
	                 </div>
	            </div>
	             <div style='padding: 0px 13px 10px 10px; text-align: right;'>
	                <button type='button' class='btn btnSendReplyAnswer' data-replyNum="${vo.replyNum}">답글 등록</button>
	            </div>
	        
	        </td>
	    </tr>
    </c:forEach>
     
     <tr height="40">
         <td colspan='2'>
              ${paging}
         </td>
     </tr>
</table>
</c:if>