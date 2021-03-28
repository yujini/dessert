package com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtil {
	/**
	 * ������ �� ���ϱ�
	 * @param numPerPage �� ȭ�鿡 ǥ���� ������ ����
	 * @param dataCount	  ��ü ������ ��
	 * @return			  �� ������ ��
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
	 * ����¡ ó��(get���)
	 * @param current_page	���� ȭ�鿡 ǥ���� ������
	 * @param total_page	��ü ������ ��
	 * @param list_url		��ũ�� ������ url
	 * @return				����¡ ���(a �±׷� �������� ��ũ ����)
	 */
	// ������ ����Ʈ�� ���� �����ִ� �Լ� - a �±�
	// ����¡(paging) ó��(GET ���)
	public String paging(int current_page, int total_page, String list_url) {
		StringBuffer sb=new StringBuffer();
		
		int numPerBlock=10; // ������ ����
		int currentPageSetup;
		int n, page;
		
		if(current_page<1 || total_page < 1)
			return "";
		
		if(list_url.indexOf("?")!=-1) // list.jsp?searchKey=subject&searchValue=java 	// �̷������� �Ѿ�� ���
			list_url+="&";
		else //  list.jsp 	// �̷��Ը� �Ѿ�� ��� - ?�� �ٿ��ش�.
			list_url+="?";
		
		// �ٸ� ��Ÿ�� : �տ� �ټ���, �ڿ� �ټ��� �̷��� �����ִ� ���� �ִ�.	// ������ ���� 
				// ���� ���� ���̴� �������� 33�̸� 		// 31~40 ���� ���;� �Ѵ�.
				// ex) 57 -> 51	, 60 -> 51
				// currentPageSetup : ǥ���� ù ������ - 1
				
		// currentPageSetup : ǥ����ù������-1
		currentPageSetup=(current_page/numPerBlock)*numPerBlock;
		if(current_page%numPerBlock==0)
			currentPageSetup=currentPageSetup-numPerBlock;

		sb.append("<style type='text/css'>");
		sb.append("#paginate {clear:both;font:12px \"���� ���\",NanumGothic,����,Dotum,AppleGothic;padding:15px 0px 0px 0px;text-align:center;height:28px;white-space:nowrap;}");
		sb.append("#paginate a {border:1px solid #ccc;height:28px;color:#000000;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;outline:none; select-dummy: expression(this.hideFocus=true);}");
		sb.append("#paginate a:hover, a:active {border:1px solid #ccc;color:#6771ff;vertical-align:middle; line-height:normal;}");
		sb.append("#paginate .curBox {border:1px solid #e28d8d; background: #fff; color:#cb3536; font-weight:bold;height:28px;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("#paginate .numBox {border:1px solid #ccc;height:28px;font-weight:bold;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("</style>");
		
		sb.append("<div id='paginate'>");
		// ó��������, ����(10������ ��)
		// ó��, ����(10������ ���� �϶��� ���δ�.)
				// 1 ������ : currentPageSetup : 0
				
		n=current_page-numPerBlock;
		if(total_page > numPerBlock && currentPageSetup > 0) {
			sb.append("<a href='"+list_url+"page=1'>ó��</a>");
			sb.append("<a href='"+list_url+"page="+n+"'>����</a>");
		}
		
		// �ٷΰ���
		page=currentPageSetup+1;
		while(page<=total_page && page <=(currentPageSetup+numPerBlock)) {
			if(page==current_page) {
				sb.append("<span class='curBox'>"+page+"</span>");
			} else {
				sb.append("<a href='"+list_url+"page="+page+"' class='numBox'>"+page+"</a>");
			}
			page++;
		}
		
		// ����(10������ ��), ������������
		n=current_page+numPerBlock;
		if(n>total_page) n=total_page;
		if(total_page-currentPageSetup>numPerBlock) {
			sb.append("<a href='"+list_url+"page="+n+"'>����</a>");
			sb.append("<a href='"+list_url+"page="+total_page+"'>��</a>");
		}
		sb.append("</div>");
	
		return sb.toString();
	}

    //********************************************
	// javascript ������ ó��(javascript listPage() �Լ� ȣ��)
    public String paging(int current_page, int total_page) {
		if(current_page < 1 || total_page < 1)
			return "";

        int numPerBlock = 10;   // ����Ʈ�� ��Ÿ�� ������ ��
        int currentPageSetUp;
        int n;
        int page;
        StringBuffer sb=new StringBuffer();
        
        // ǥ���� ù ������
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0)
            currentPageSetUp = currentPageSetUp - numPerBlock;

		sb.append("<style type='text/css'>");
		sb.append("#paginate {clear:both;font:12px \"���� ���\",NanumGothic,����,Dotum,AppleGothic;padding:15px 0px 0px 0px;text-align:center;height:28px;white-space:nowrap;}");
		sb.append("#paginate a {border:1px solid #ccc;height:28px;color:#000000;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;outline:none; select-dummy: expression(this.hideFocus=true);}");
		sb.append("#paginate a:hover, a:active {border:1px solid #ccc;color:#6771ff;vertical-align:middle; line-height:normal;}");
		sb.append("#paginate .curBox {border:1px solid #e28d8d; background: #fff; color:#cb3536; font-weight:bold;height:28px;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("#paginate .numBox {border:1px solid #ccc;height:28px;font-weight:bold;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("</style>");
		
		sb.append("<div id='paginate'>");
        
        // ó��������, ����(10������ ��)
        n = current_page - numPerBlock;
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
			sb.append("<a onclick='listPage(1);'>ó��</a>");
			sb.append("<a onclick='listPage("+n+");'>����</a>");
        }

        // �ٷΰ��� ������ ����
        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
        	   sb.append("<span class='curBox'>"+page+"</span>");
           } else {
			   sb.append("<a onclick='listPage("+page+");' class='numBox'>"+page+"</a>");
           }
           page++;
        }

        // ����(10������ ��), ������ ������
        n = current_page + numPerBlock;
		if(n>total_page) n=total_page;
        if (total_page - currentPageSetUp > numPerBlock) {
			sb.append("<a onclick='listPage("+n+");'>����</a>");
			sb.append("<a onclick='listPage("+total_page+");'>��</a>");
        }
		sb.append("</div>");

        return sb.toString();
    }

    //********************************************
	// javascript ������ ó��(javascript ���� �Լ� ȣ��, methodName:ȣ���� ��ũ��Ʈ �Լ���)
    public String pagingMethod(int current_page, int total_page, String methodName) {
		if(current_page < 1 || total_page < 1)
			return "";

        int numPerBlock = 10;   // ����Ʈ�� ��Ÿ�� ������ ��
        int currentPageSetUp;
        int n;
        int page;
        StringBuffer sb=new StringBuffer();
        
        // ǥ���� ù ������
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0)
            currentPageSetUp = currentPageSetUp - numPerBlock;

		sb.append("<style type='text/css'>");
		sb.append("#paginate {clear:both;font:12px \"���� ���\",NanumGothic,����,Dotum,AppleGothic;padding:15px 0px 0px 0px;text-align:center;height:28px;white-space:nowrap;}");
		sb.append("#paginate a {border:1px solid #ccc;height:28px;color:#000000;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;outline:none; select-dummy: expression(this.hideFocus=true);}");
		sb.append("#paginate a:hover, a:active {border:1px solid #ccc;color:#6771ff;vertical-align:middle; line-height:normal;}");
		sb.append("#paginate .curBox {border:1px solid #e28d8d; background: #fff; color:#cb3536; font-weight:bold;height:28px;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("#paginate .numBox {border:1px solid #ccc;height:28px;font-weight:bold;text-decoration:none;padding:4px 7px 4px 7px;margin-left:3px;line-height:normal;vertical-align:middle;}");
		sb.append("</style>");
		
		sb.append("<div id='paginate'>");
        
        // ó��������, ����(10������ ��)
        n = current_page - numPerBlock;
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
			sb.append("<a onclick='"+methodName+"(1);'>ó��</a>");
			sb.append("<a onclick='"+methodName+"("+n+");'>����</a>");
        }

        // �ٷΰ��� ������ ����
        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
        	   sb.append("<span class='curBox'>"+page+"</span>");
           } else {
			   sb.append("<a onclick='"+methodName+"("+page+");' class='numBox'>"+page+"</a>");
           }
           page++;
        }

        // ����(10������ ��), ������ ������
        n = current_page + numPerBlock;
		if(n>total_page) n=total_page;
        if (total_page - currentPageSetUp > numPerBlock) {
			sb.append("<a onclick='"+methodName+"("+n+");'>����</a>");
			sb.append("<a onclick='"+methodName+"("+total_page+");'>��</a>");
        }
		sb.append("</div>");

        return sb.toString();
    }
    
    //********************************************
    // Ư�����ڸ� HTML ���ڷ� ����
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
    // Ư�����ڸ� HTML ���ڷ� ���� �� ���͸� <br>�� ���� 
     public String htmlSymbols(String str) {
		if(str==null||str.length()==0)
			return "";

    	 str=str.replaceAll("&", "&amp;")	// ���� �߿�!!!!!!
    	 	.replaceAll("\"", "&quot;")
    	 	.replaceAll(">", "&gt;")
    	 	.replaceAll("<", "&lt;")
    	 
    	 	.replaceAll("\n", "<br>")
    	 	.replaceAll("\\s", "&nbsp;");	// ��ġ�� �Ʒ����� ������ �� ó���� �ȴ�.
    	 
    	 return str;
     }

    //********************************************
 	// ���ڿ��� ������ ���ϴ� ���ڿ��� �ٸ� ���ڿ��� ��ȯ
 	// String str = replaceAll(str, "\n", "<br>"); // ���͸� <br>�� ��ȯ
 	public String replaceAll(String str, String oldStr, String newStr) throws Exception {
 		if(str == null)
 			return "";

         Pattern p = Pattern.compile(oldStr);
         
         // �Է� ���ڿ��� �Բ� ���� Ŭ���� ����
         Matcher m = p.matcher(str);

         StringBuffer sb = new StringBuffer();
         // ���ϰ� ��ġ�ϴ� ���ڿ��� newStr �� ��ü�ذ��� ���ο� ���ڿ��� �����.
         while(m.find()) {
             m.appendReplacement(sb, newStr);
         }

         // ������ �κ��� ���ο� ���ڿ� ���� �� ���δ�.
         m.appendTail(sb);

 		return sb.toString();
 	}

    //********************************************
 	// E-Mail �˻�
     public boolean isValidEmail(String email) {
         if (email==null) return false;
         boolean b = Pattern.matches(
        	 "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", 
             email.trim());
         return b;
     }

    //********************************************
 	// NULL �� ��� ""�� 
     public String checkNull(String str) {
         String strTmp;
         if (str == null)
             strTmp = "";
         else
             strTmp = str;
         return strTmp;
     }
}
