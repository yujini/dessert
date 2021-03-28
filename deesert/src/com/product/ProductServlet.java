package com.product;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.FileManager;
import com.util.MyServlet;

@WebServlet("/product/*")
public class ProductServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		// �α��� ����
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		
		// �̹����� ������ ���
		String root = session.getServletContext().getRealPath("/"); // �� ��� ��Ʈ�� ���� ��ġ
		pathname = root + "uploads" + File.separator + "photo";
				
		File f = new File(pathname);
		if(! f.exists()) {
			f.mkdirs();	// s �� �پ�� �Ѵ�!!!
		}		
		
		
		// article ����.
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
		
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductDAO dao = new ProductDAO();
		
		String pGroup = req.getParameter("pGroup");	// ��ǰ ����
		if(pGroup == null) {
			pGroup = "piececake";
		}
		
		// jsp ���� �� ���� ����
		int dataCount = dao.dataCount(pGroup);
		List<ProductDTO> list = dao.listProduct(pGroup);
		
		// ������ �� jsp �� �ѱ� �� ����
		req.setAttribute("list", list);
		req.setAttribute("pGroup", pGroup);
		req.setAttribute("dataCount", dataCount);
		
		// ������		
		forward(req, resp, "/WEB-INF/views/product/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/product/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����
		String cp = req.getContextPath();
		
		ProductDAO dao = new ProductDAO();
		String encType = "utf-8";
		int maxSize = 5*1024*1024;
		
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		ProductDTO dto = new ProductDTO();
		
		if(mreq.getFile("upload") != null) {
			dto.setProductGroup(mreq.getParameter("productGroup"));
			dto.setName(mreq.getParameter("name"));
			
			String saveFilename = mreq.getFilesystemName("upload");
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setFileName(saveFilename);
			
			dto.setPrice(Integer.parseInt(mreq.getParameter("price")));
			dto.setMemo(mreq.getParameter("memo"));
			dto.setCnt(Integer.parseInt(mreq.getParameter("cnt")));
			dto.setIsBest(mreq.getParameter("isBest"));
						
			dao.insertProduct(dto);
		}
		
		resp.sendRedirect(cp + "/product/list.do");
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		ProductDAO dao = new ProductDAO();
		
		int num = Integer.parseInt(req.getParameter("productNum"));		
		ProductDTO dto = dao.readProduct(num);
		
		if(dto == null) {
			resp.sendRedirect(cp + "/product/list.do");
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("mode", "update");
		forward(req, resp, "/WEB-INF/views/product/created.jsp");
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		ProductDAO dao = new ProductDAO();
		
		String encType = "utf-8";
		int maxSize = 5*1024*1024;
		
		MultipartRequest mr = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		ProductDTO dto = new ProductDTO();
		dto.setProductNum(Integer.parseInt(mr.getParameter("num")));
		dto.setProductGroup(mr.getParameter("productGroup"));
		dto.setName(mr.getParameter("name"));
		dto.setPrice(Integer.parseInt(mr.getParameter("price")));
		dto.setMemo(mr.getParameter("memo"));
		dto.setCnt(Integer.parseInt(mr.getParameter("cnt")));
		dto.setIsBest(mr.getParameter("isBest"));
		dto.setIsSale(mr.getParameter("isSale")); 	
		
		// filname
		String imageFilename = mr.getParameter("imageFilename");
		if(mr.getFile("upload") != null) {	// ���� ����
			FileManager.doFiledelete(encType, imageFilename);	// ���� ���� �����
			
			String saveFilename = mr.getOriginalFileName("upload");
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setFileName(saveFilename);
		} else {
			dto.setFileName(mr.getParameter("imageFilename"));	// ���� �� �״��
		}
		
		dao.updateProduct(dto);
		
		resp.sendRedirect(cp + "/product/list.do?pGroup="+dto.getProductGroup());		
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		ProductDAO dao = new ProductDAO();
		
		// �Ķ���� - productNum, pGroup
		int productNum = Integer.parseInt(req.getParameter("productNum"));
		String pGroup = req.getParameter("pGroup");
		
		// admin ��  Ŭ���� �� �ְ� �����Ƿ� session ���� member ���� �������� ���� �����Ѵ�!
		
		ProductDTO dto = dao.readProduct(productNum);
		if(dto == null) {
			resp.sendRedirect(cp + "/product/list.do");
		}
		
		// ���� �����
		FileManager.doFiledelete(pathname, dto.getFileName());
		
		// db ���� �����
		dao.deleteProduct(productNum);
		
		resp.sendRedirect(cp + "/product/list.do?pGroup=" + pGroup);
	}
	
}
