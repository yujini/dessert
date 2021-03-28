package com.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

public class FileManager {
	/**
	 * 파일 다운로드 메소드
	 * @param saveFilename 서버에저장된파일명
	 * @param originalFilename 클라이언트가업로드한파일명
	 * @param pathname 서버에저장된경로
	 * @param resp HttpServletResponse 객체
	 * @return 다운로드성공여부
	 */
	public static boolean doFiledownload(String saveFilename, String originalFilename, String pathname, HttpServletResponse resp) {
		boolean flag=false;
		
		if(pathname==null || saveFilename==null || saveFilename.length()==0 ||
				originalFilename==null || originalFilename.length()==0) {
			return flag;
		}
		
		try {
			// 파일명을  euc-kr => 8859_1 로 고쳐야 한글이 안깨진다. http는 기본이 8859-1 로 다닌다?
			originalFilename=new String(originalFilename.getBytes("euc-kr"), "8859_1");
			pathname=pathname+File.separator+saveFilename;
			File f=new File(pathname);
			if(! f.exists()) {
				return flag;
			}
			
			// 지금까지 jsp 에서는 "text/html; charset=UTF-8"
			// 클라이언트에게 전송할 문서의 타입 설정 : 파일은 application/octet-stream	=> stream으로 데이터를 전송하겠다
			// 클라이언트에게 전송할 문서타입이 파일(스트림)이라고 설정
			resp.setContentType("application/octet-stream");
			
			// 파일명 전송 : 파일명은 헤더에 정보를 실어서 보낸다.	=> 헤더가 간 뒤에 바디가 간다.		=> 이후 웹 브라우저가 파일명을 받아서 화면에 뿌려준다.
		
			resp.setHeader("Content-disposition", "attachment;filename="+originalFilename);
			
			// 클라이언트에게 파일의 내용을 전송
			byte[] b=new byte[1024];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			
			// 클라이언트에게 전송할 출력 스트림
			OutputStream os=resp.getOutputStream();
			
			int n;
			while((n=bis.read(b, 0, b.length))!=-1) {
				os.write(b, 0, n);
			}
			os.flush(); // 버퍼가 안차면 안보내므로 flush
			os.close();
			bis.close(); // 원래는 finally 에서 해야 하지만.. try 안에서 생성했으므로!
			
			flag=true;	// 전송 성공
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return flag;
	}
	
	/**
	 * 파일 이름 변경(년월일시분초나노초)
	 * @param pathname 파일이저장된 경로
	 * @param filename 변경할 파일명
	 * @return 새로운파일명
	 */
	public static String doFilerename(String pathname, String filename) { // 동기화 문제 발생가능성 있으므로 동기화 블럭 넣어주기
		String newname="";
		
    	String fileExt = filename.substring(filename.lastIndexOf(".")); // 파일 확장자 이전까지 파일명 가져온다.
    	String s = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance()); // 년월일시분초
    	s += System.nanoTime(); // 년월일 시분초만 하면 중복이 되기 때문에 나노타임 더한다. 
    	s += fileExt;
    	
    	try {
	    	File f1=new File(pathname+File.separator+filename);	// 본래
	    	File f2=new File(pathname+File.separator+s);		// 새로운
	    	// f2가 중복인지 확인하는 작업 추후 추가
	    	f1.renameTo(f2);	
	    	
	    	newname = s;
    	}catch(Exception e) {
    	}
		
		return newname;
	}
	
	/**
	 * 파일 삭제
	 * @param pathname 파일이 저장된 경로
	 * @param filename 삭제할 파일명
	 * @return 파일 삭제 성공 여부
	 */
	public static boolean doFiledelete(String pathname, String filename) {
		String path=pathname+File.separator+filename;
		
		try {
			File f=new File(path);
			
			if(! f.exists()) // 파일이 없으면
				return false;
			
			f.delete();
		} catch (Exception e) {
		}
		
		return true;
	}
	
}
