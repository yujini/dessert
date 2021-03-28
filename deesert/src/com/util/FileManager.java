package com.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

public class FileManager {
	/**
	 * ���� �ٿ�ε� �޼ҵ�
	 * @param saveFilename ��������������ϸ�
	 * @param originalFilename Ŭ���̾�Ʈ�����ε������ϸ�
	 * @param pathname ����������Ȱ��
	 * @param resp HttpServletResponse ��ü
	 * @return �ٿ�ε强������
	 */
	public static boolean doFiledownload(String saveFilename, String originalFilename, String pathname, HttpServletResponse resp) {
		boolean flag=false;
		
		if(pathname==null || saveFilename==null || saveFilename.length()==0 ||
				originalFilename==null || originalFilename.length()==0) {
			return flag;
		}
		
		try {
			// ���ϸ���  euc-kr => 8859_1 �� ���ľ� �ѱ��� �ȱ�����. http�� �⺻�� 8859-1 �� �ٴѴ�?
			originalFilename=new String(originalFilename.getBytes("euc-kr"), "8859_1");
			pathname=pathname+File.separator+saveFilename;
			File f=new File(pathname);
			if(! f.exists()) {
				return flag;
			}
			
			// ���ݱ��� jsp ������ "text/html; charset=UTF-8"
			// Ŭ���̾�Ʈ���� ������ ������ Ÿ�� ���� : ������ application/octet-stream	=> stream���� �����͸� �����ϰڴ�
			// Ŭ���̾�Ʈ���� ������ ����Ÿ���� ����(��Ʈ��)�̶�� ����
			resp.setContentType("application/octet-stream");
			
			// ���ϸ� ���� : ���ϸ��� ����� ������ �Ǿ ������.	=> ����� �� �ڿ� �ٵ� ����.		=> ���� �� �������� ���ϸ��� �޾Ƽ� ȭ�鿡 �ѷ��ش�.
		
			resp.setHeader("Content-disposition", "attachment;filename="+originalFilename);
			
			// Ŭ���̾�Ʈ���� ������ ������ ����
			byte[] b=new byte[1024];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			
			// Ŭ���̾�Ʈ���� ������ ��� ��Ʈ��
			OutputStream os=resp.getOutputStream();
			
			int n;
			while((n=bis.read(b, 0, b.length))!=-1) {
				os.write(b, 0, n);
			}
			os.flush(); // ���۰� ������ �Ⱥ����Ƿ� flush
			os.close();
			bis.close(); // ������ finally ���� �ؾ� ������.. try �ȿ��� ���������Ƿ�!
			
			flag=true;	// ���� ����
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return flag;
	}
	
	/**
	 * ���� �̸� ����(����Ͻú��ʳ�����)
	 * @param pathname ����������� ���
	 * @param filename ������ ���ϸ�
	 * @return ���ο����ϸ�
	 */
	public static String doFilerename(String pathname, String filename) { // ����ȭ ���� �߻����ɼ� �����Ƿ� ����ȭ �� �־��ֱ�
		String newname="";
		
    	String fileExt = filename.substring(filename.lastIndexOf(".")); // ���� Ȯ���� �������� ���ϸ� �����´�.
    	String s = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance()); // ����Ͻú���
    	s += System.nanoTime(); // ����� �ú��ʸ� �ϸ� �ߺ��� �Ǳ� ������ ����Ÿ�� ���Ѵ�. 
    	s += fileExt;
    	
    	try {
	    	File f1=new File(pathname+File.separator+filename);	// ����
	    	File f2=new File(pathname+File.separator+s);		// ���ο�
	    	// f2�� �ߺ����� Ȯ���ϴ� �۾� ���� �߰�
	    	f1.renameTo(f2);	
	    	
	    	newname = s;
    	}catch(Exception e) {
    	}
		
		return newname;
	}
	
	/**
	 * ���� ����
	 * @param pathname ������ ����� ���
	 * @param filename ������ ���ϸ�
	 * @return ���� ���� ���� ����
	 */
	public static boolean doFiledelete(String pathname, String filename) {
		String path=pathname+File.separator+filename;
		
		try {
			File f=new File(path);
			
			if(! f.exists()) // ������ ������
				return false;
			
			f.delete();
		} catch (Exception e) {
		}
		
		return true;
	}
	
}
