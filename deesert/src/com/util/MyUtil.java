package com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtil {
	/**
	 * 페이지 수 구하기
	 * @param numPerPage 한 화면에 표시할 데이터 개수
	 * @param dataCount	  전체 데이터 수
	 * @return			  총 페이지 수
	 */
	public int pageCount(int numPerPage, int dataCount) {
		int pageCount=0;
		
		if(dataCount > 0) {
			if(dataCount % numPerPage == 0)
				pageCount=dataCount/numPerPage;
			else
				pageCount=dataCount/numPerPage+1;
		}
		
		return pageCount;
	}
	
	/**
	 * 페이징 처리(get방식)
	 * @param current_page	현재 화면에 표시할 페이지
	 * @param total_page	전체 페이지 수
	 * @param list_url		링크를 설정할 url
	 * @return				페이징 결과(a 태그로 페이지에 링크 설정)
	 */
	// 페이지 리스트를 만들어서 보내주는 함수 - a 태그
	// 페이징(paging) 처리(GET 방식)
	public String paging(int current_page, int total_page, String list_url) {
		StringBuffer sb=new StringBuffer();
		
		int numPerBlock=10; // 페이지 묶음
		int currentPageSetup;
		int n, page;
		
		if(current_page<1 || total_page < 1)
			return "";
		
		if(list_url.indexOf("?")!=-1) // list.jsp?searchKey=subject&searchValue=java 	// 이런식으로 넘어온 경우
			list_url+="&";
		else //  list.jsp 	// 이렇게만 넘어온 경우 - ?을 붙여준다.
			list_url+="?";
		
		// 다른 스타일 : 앞에 다섯개, 뒤에 다섯개 이렇게 보여주는 곳도 있다.	// 오늘의 문제 
				// 내가 눈에 보이는 페이지가 33이면 		// 31~40 까지 나와야 한다.
				// ex) 57 -> 51	, 60 -> 51
				// currentPageSetup : 표시할 첫 페이지 - 1
				
		// currentPageSetup : 표시할첫페이지-1
		currentPageSetup=(current_page/numPerBlock)*numPerBlock;
		if(current_page%numPerBlock==0)
			currentPageSetup=currentPageSetup-numPerBlock;

		sb.append("<style type='text/css'>");
		sb.append("#paginate {clear:both;font:12px \"맑은 고딕\",NanumGothic,돋움,Dotum,AppleGothic;padding:15px 0px 0px 0px;text-align:center;height:28px;white-space:nowrap;}");
		sb.append("#paginate a {border:1px solid #ccc;height:28px;color:#000000;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;outline:none; select-dummy: expression(this.hideFocus=true);}");
		sb.append("#paginate a:hover, a:active {border:1px solid #ccc;color:#6771ff;vertical-align:middle; line-height:normal;}");
		sb.append("#paginate .curBox {border:1px solid #e28d8d; background: #fff; color:#cb3536; font-weight:bold;height:28px;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("#paginate .numBox {border:1px solid #ccc;height:28px;font-weight:bold;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("</style>");
		
		sb.append("<div id='paginate'>");
		// 처음페이지, 이전(10페이지 전)
		// 처음, 이전(10페이지 이하 일때만 보인다.)
				// 1 페이지 : currentPageSetup : 0
				
		n=current_page-numPerBlock;
		if(total_page > numPerBlock && currentPageSetup > 0) {
			sb.append("<a href='"+list_url+"page=1'>처음</a>");
			sb.append("<a href='"+list_url+"page="+n+"'>이전</a>");
		}
		
		// 바로가기
		page=currentPageSetup+1;
		while(page<=total_page && page <=(currentPageSetup+numPerBlock)) {
			if(page==current_page) {
				sb.append("<span class='curBox'>"+page+"</span>");
			} else {
				sb.append("<a href='"+list_url+"page="+page+"' class='numBox'>"+page+"</a>");
			}
			page++;
		}
		
		// 다음(10페이지 후), 마지막페이지
		n=current_page+numPerBlock;
		if(n>total_page) n=total_page;
		if(total_page-currentPageSetup>numPerBlock) {
			sb.append("<a href='"+list_url+"page="+n+"'>다음</a>");
			sb.append("<a href='"+list_url+"page="+total_page+"'>끝</a>");
		}
		sb.append("</div>");
	
		return sb.toString();
	}

    //********************************************
	// javascript 페이지 처리(javascript listPage() 함수 호출)
    public String paging(int current_page, int total_page) {
		if(current_page < 1 || total_page < 1)
			return "";

        int numPerBlock = 10;   // 리스트에 나타낼 페이지 수
        int currentPageSetUp;
        int n;
        int page;
        StringBuffer sb=new StringBuffer();
        
        // 표시할 첫 페이지
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0)
            currentPageSetUp = currentPageSetUp - numPerBlock;

		sb.append("<style type='text/css'>");
		sb.append("#paginate {clear:both;font:12px \"맑은 고딕\",NanumGothic,돋움,Dotum,AppleGothic;padding:15px 0px 0px 0px;text-align:center;height:28px;white-space:nowrap;}");
		sb.append("#paginate a {border:1px solid #ccc;height:28px;color:#000000;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;outline:none; select-dummy: expression(this.hideFocus=true);}");
		sb.append("#paginate a:hover, a:active {border:1px solid #ccc;color:#6771ff;vertical-align:middle; line-height:normal;}");
		sb.append("#paginate .curBox {border:1px solid #e28d8d; background: #fff; color:#cb3536; font-weight:bold;height:28px;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("#paginate .numBox {border:1px solid #ccc;height:28px;font-weight:bold;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("</style>");
		
		sb.append("<div id='paginate'>");
        
        // 처음페이지, 이전(10페이지 전)
        n = current_page - numPerBlock;
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
			sb.append("<a onclick='listPage(1);'>처음</a>");
			sb.append("<a onclick='listPage("+n+");'>이전</a>");
        }

        // 바로가기 페이지 구현
        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
        	   sb.append("<span class='curBox'>"+page+"</span>");
           } else {
			   sb.append("<a onclick='listPage("+page+");' class='numBox'>"+page+"</a>");
           }
           page++;
        }

        // 다음(10페이지 후), 마지막 페이지
        n = current_page + numPerBlock;
		if(n>total_page) n=total_page;
        if (total_page - currentPageSetUp > numPerBlock) {
			sb.append("<a onclick='listPage("+n+");'>다음</a>");
			sb.append("<a onclick='listPage("+total_page+");'>끝</a>");
        }
		sb.append("</div>");

        return sb.toString();
    }

    //********************************************
	// javascript 페이지 처리(javascript 지정 함수 호출, methodName:호출할 스크립트 함수명)
    public String pagingMethod(int current_page, int total_page, String methodName) {
		if(current_page < 1 || total_page < 1)
			return "";

        int numPerBlock = 10;   // 리스트에 나타낼 페이지 수
        int currentPageSetUp;
        int n;
        int page;
        StringBuffer sb=new StringBuffer();
        
        // 표시할 첫 페이지
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0)
            currentPageSetUp = currentPageSetUp - numPerBlock;

		sb.append("<style type='text/css'>");
		sb.append("#paginate {clear:both;font:12px \"맑은 고딕\",NanumGothic,돋움,Dotum,AppleGothic;padding:15px 0px 0px 0px;text-align:center;height:28px;white-space:nowrap;}");
		sb.append("#paginate a {border:1px solid #ccc;height:28px;color:#000000;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;outline:none; select-dummy: expression(this.hideFocus=true);}");
		sb.append("#paginate a:hover, a:active {border:1px solid #ccc;color:#6771ff;vertical-align:middle; line-height:normal;}");
		sb.append("#paginate .curBox {border:1px solid #e28d8d; background: #fff; color:#cb3536; font-weight:bold;height:28px;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("#paginate .numBox {border:1px solid #ccc;height:28px;font-weight:bold;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("</style>");
		
		sb.append("<div id='paginate'>");
        
        // 처음페이지, 이전(10페이지 전)
        n = current_page - numPerBlock;
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
			sb.append("<a onclick='"+methodName+"(1);'>처음</a>");
			sb.append("<a onclick='"+methodName+"("+n+");'>이전</a>");
        }

        // 바로가기 페이지 구현
        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
        	   sb.append("<span class='curBox'>"+page+"</span>");
           } else {
			   sb.append("<a onclick='"+methodName+"("+page+");' class='numBox'>"+page+"</a>");
           }
           page++;
        }

        // 다음(10페이지 후), 마지막 페이지
        n = current_page + numPerBlock;
		if(n>total_page) n=total_page;
        if (total_page - currentPageSetUp > numPerBlock) {
			sb.append("<a onclick='"+methodName+"("+n+");'>다음</a>");
			sb.append("<a onclick='"+methodName+"("+total_page+");'>끝</a>");
        }
		sb.append("</div>");

        return sb.toString();
    }
    
    //********************************************
    // 특수문자를 HTML 문자로 변경
	public String escape(String str) {
		if(str==null||str.length()==0)
			return "";
		
		StringBuilder builder = new StringBuilder((int)(str.length() * 1.2f));

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '\"':
				builder.append("&quot;");
				break;
			default:
				builder.append(c);
			}
		}
		return builder.toString();
	}

    //********************************************
    // 특수문자를 HTML 문자로 변경 및 엔터를 <br>로 변경 
     public String htmlSymbols(String str) {
		if(str==null||str.length()==0)
			return "";

    	 str=str.replaceAll("&", "&amp;")	// 순서 중요!!!!!!
    	 	.replaceAll("\"", "&quot;")
    	 	.replaceAll(">", "&gt;")
    	 	.replaceAll("<", "&lt;")
    	 
    	 	.replaceAll("\n", "<br>")
    	 	.replaceAll("\\s", "&nbsp;");	// 위치가 아래여야 공백이 잘 처리가 된다.
    	 
    	 return str;
     }

    //********************************************
 	// 문자열의 내용중 원하는 문자열을 다른 문자열로 변환
 	// String str = replaceAll(str, "\n", "<br>"); // 엔터를 <br>로 변환
 	public String replaceAll(String str, String oldStr, String newStr) throws Exception {
 		if(str == null)
 			return "";

         Pattern p = Pattern.compile(oldStr);
         
         // 입력 문자열과 함께 매쳐 클래스 생성
         Matcher m = p.matcher(str);

         StringBuffer sb = new StringBuffer();
         // 패턴과 일치하는 문자열을 newStr 로 교체해가며 새로운 문자열을 만든다.
         while(m.find()) {
             m.appendReplacement(sb, newStr);
         }

         // 나머지 부분을 새로운 문자열 끝에 덫 붙인다.
         m.appendTail(sb);

 		return sb.toString();
 	}

    //********************************************
 	// E-Mail 검사
     public boolean isValidEmail(String email) {
         if (email==null) return false;
         boolean b = Pattern.matches(
        	 "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", 
             email.trim());
         return b;
     }

    //********************************************
 	// NULL 인 경우 ""로 
     public String checkNull(String str) {
         String strTmp;
         if (str == null)
             strTmp = "";
         else
             strTmp = str;
         return strTmp;
     }
}
