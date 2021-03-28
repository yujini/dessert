<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>
<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
<c:if test="${dataCount != 0}">
   <c:forEach var="vo" items="${list}">
       <tr height='35' align="center">     
          <td width="20%">
              <span><b><input type="radio" name="saleselect" value="${vo.saleDate}::${vo.division}"></b></span>
           </td>
          <td width="30%">  <span>${vo.saleDate}</span>   </td>
          <td width="20%">	
	          <span>
		          <c:if test="${vo.division == 'morning'}">
		          	오전
		          </c:if>
		          <c:if test="${vo.division == 'afternoon'}">
		          	오후
		          </c:if>
		          <c:if test="${vo.division == 'finish'}">
		          	마감
		          </c:if>          
	          </span>   
          </td>
          <td width="30%">  <fmt:formatNumber value='${vo.price}' type='currency'/>    </td>
       </tr>   
   </c:forEach>
</c:if>
<c:if test="${dataCount == 0}">
<tr align='center' bgcolor='#ffffff' height='35' style='border-bottom: 1px solid #cccccc;'>
<td> 검색 결과가 없습니다. </td>
</tr>
</c:if>
</table>
