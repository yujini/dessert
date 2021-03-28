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
	listPage(1);
});

function listPage(page) {
	var url = "<%=cp%>/reservation/listReply.do"
	var query = "pageNo="+page;		// ajax 는 pageNo, ajax 아니면 page 
	
	// 순서는 상관 없다.
	$.ajax({	// 앞으로 get, post 쓸 수 없다. ajax 메소드만 써야 하는 이유 : setRequestHeader이런 메소드 써야 하므로
		type:"post",
		url:url,
		data:query,
		success:function(data) {
			$("#listReply").html(data);
		},
		beforeSend:function(jqXHR) {	
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status == 403) {	// serlvet에서 오류일 경우 403으로 에러 던졌다.
				location.href = "<%=cp%>/member/login.do";
				return;
			}	
			console.log(jqXHR.responseText);
		}			
	});
	
}

$(function () {
	$("body").on("click", ".btnReplyAnswerLayout", function() {
		var $tr = $(this).closest("tr").next();
		var replyNum = $(this).attr("data-replyNum");
		
		if($tr.is(":visible")) {
			$tr.hide();
		} else {
			$tr.show();
			listAnswer(replyNum);
			countAnswer(replyNum); ////////////////////////////
		}
	});
});

$(function() {
	$("body").on("click", ".btnSendReplyAnswer", function() {
		var replyNum = $(this).attr("data-replyNum");
		
		var $td = $(this).closest("td");
		var content = $td.find("textarea").val().trim();
		if(! content) {
			$td.find("textarea").focus();
			return;
		}
		content = encodeURIComponent(content);
		
		var query = "content=" + content + "&answer=" + replyNum;
		var url = "<%=cp%>/reservation/insertReply.do";
		
		$.ajax({
			type:"post",
			url:url,
			data:query,
			dataType:"json",
			success:function(data) {
				$td.find("textarea").val("");
				
				var state = data.state;
				if(state == "true") {
					listAnswer(replyNum);
					countAnswer(replyNum); /////////////////////////////////
				}
			},
			beforeSend:function(jqXHR) {
				jqXHR.setRequestHeader("AJAX", true);
			},
			error:function(jqXHR) {
				if(jqXHR.status == 403) {
					location.href = "<%=cp%>/member/login.do";
					return;
				}
				console.log(jqXHR.responseText);
			}
		});
		
	});
});

function countAnswer(replyNum) {
	var url = "<%=cp%>/reservation/countAnswer.do"
	var query = "replyNum=" + replyNum;
	
	$.ajax({
		type:"post",
		url:url,
		data:query,
		dataType:"json",
		success:function(data) {
			$("#answerCount" + replyNum).html(data.count);
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status == 403) {	// serlvet에서 오류일 경우 403으로 에러 던졌다.
				location.href = "<%=cp%>/member/login.do";
				return;
			}	
			console.log(jqXHR.responseText);
		}		
	});
}

function listAnswer(replyNum) {
	var url = "<%=cp%>/reservation/listAnswer.do";
	var query = "replyNum=" + replyNum;
	
	$.ajax({
		type:"post",
		url:url,
		data:query,
		success:function(data) {
			$("#listReplyAnswer" + replyNum).html(data);
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status == 403) {	// serlvet에서 오류일 경우 403으로 에러 던졌다.
				location.href = "<%=cp%>/member/login.do";
				return;
			}	
			console.log(jqXHR.responseText);
		}
	});
}

function deleteReplyAnswer(replyNum, answer) {
	if(!confirm('답글을 삭제하시겠습니까?')) {
		return;
	}
	
	var page = $("#listReplyAnswer" + answer).attr("data-page");
	var url = "<%=cp%>/reservation/deleteReply.do";
	
	$.ajax({
		type:"post",
		url:url,
		data:{replyNum:replyNum},
		dataType:"json",
		success:function(data) {
			listAnswer(answer); ///////////////// listPage 아니고 listAnswer.......
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status == 403) {	// serlvet에서 오류일 경우 403으로 에러 던졌다.
				location.href = "<%=cp%>/member/login.do";
				return;
			}	
			console.log(jqXHR.responseText);
		}
	});
	
}

$(function() {
	$("body").on("click", ".deleteReply", function() {
		if(!confirm('게시물을 삭제하시겠습니까?')) {
			return;
		}
		
		var replyNum = $(this).attr("data-replyNum");
		var page = $(this).attr("data-pageNo");
		
		var url = "<%=cp%>/reservation/deleteReply.do";
		
		$.ajax({
			type:"post",
			url:url,
			data:{replyNum:replyNum},
			dataType:"json",
			success:function(data) {
				listPage(page);
			},
			beforeSend:function(jqXHR) {
				jqXHR.setRequestHeader("AJAX", true);
			},
			error:function(jqXHR) {
				if(jqXHR.status == 403) {	// serlvet에서 오류일 경우 403으로 에러 던졌다.
					location.href = "<%=cp%>/member/login.do";
					return;
				}	
				console.log(jqXHR.responseText);
			}
		});
	});
	
});

$(function() {
	$(".btnSendReply").click(function() {
		// 형제를 찾을 때 위로는 못찾고 아래로만 찾을 수 있다.
		var $tb = $(this).closest("table");
		var content = $tb.find("textarea").val().trim(); // textarea는 하나이므로 찾을 수 있다.
		if(! content) {
			$tb.find("textarea").focus();
			return;
		}		
		content = encodeURIComponent(content);
		
		var query = "content=" + content;
		var url = "<%=cp%>/reservation/insertReply.do";
				
		$.ajax({	// 앞으로 get, post 쓸 수 없다. ajax 메소드만 써야 하는 이유 : setRequestHeader이런 메소드 써야 하므로
			type:"post",
			url:url,
			data:query,
			dataType:"json",
			success:function(data) {
				$tb.find("textarea").val("");
				
				var state = data.state;
				if(state == "true") {
					listPage(1);
				} 
			},
			beforeSend:function(jqXHR) {	
				// 클라이언트가 서버한테 ajax 인지 아닌지 알려주기 위한 목적 - header에 정보를 싣어서 보낸다.
				jqXHR.setRequestHeader("AJAX", true);	// jquery 에서 제공해주는 메소드  - setRequestHeader 
				// 기본 헤더에 ajax 라는 말을 추가
			},
			error:function(jqXHR) {
				if(jqXHR.status == 403) {	// serlvet에서 오류일 경우 403으로 에러 던졌다.
					location.href = "<%=cp%>/member/login.do";
					return;
				}	
				console.log(jqXHR.responseText);
			}			
		});	
	});
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
            <h3><span style="font-family: Webdings">2</span> 예약 문의 </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td colspan="2" align="center" style="font-weight: bold;">
						Q. 할인받을 수 있는 방법이 있나요?
				    </td>
				</tr>			
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td colspan="2" align="center">
						홀케익의 경우 3일 전 예약 주시면 금액의 10%가 할인됩니다.
				    </td>
				</tr>			
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td colspan="2" align="center" style="font-weight: bold;">
						Q. 바로 구매 가능한 케익은 어떤 종류가 있나요?
				    </td>
				</tr>			
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td colspan="2" align="center">
						예약 > 상품소개 탭을 보시면 구매 가능한 수량이 있습니다.
				    </td>
				</tr>			
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td colspan="2" align="center" style="font-weight: bold;">
						Q. 날씨가 더운데 케익 보관은 어떻게 해야 하나요?
				    </td>
				</tr>			
				<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
				    <td colspan="2" align="center">
						바로 냉장 보관을 권해드립니다. 거리가 먼 경우 얼음팩을 제공하고 있으니 참고하시기 바랍니다.
				    </td>
				</tr>			
			</table>
        </div>
	<br>
 	    <div>
            <table style='width: 100%; margin: 15px auto 0px; border-spacing: 0px;'>
	            <tr height='30'> 
		            <td align='left'>
		            	<span style='font-weight: bold;' >문의 남기기</span><span> - 문의 전 위의 공지를 참고해 주세요.^^</span>
		            </td>
	            </tr>
	            <tr>
	               <td style='padding:5px 5px 0px;'>
	                    <textarea class='boxTA' style='width:99%; height: 70px;'></textarea>
	                </td>
	            </tr>
	            <tr>
	               <td align='right'>
	                    <button type='button' class='btn btnSendReply' style='padding:10px 20px;'>등록</button>
	                </td>
	            </tr>
            </table>
            
            <div id="listReply">
            
            </div>
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