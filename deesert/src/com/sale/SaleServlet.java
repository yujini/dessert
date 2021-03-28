package com.sale;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.product.ProductDAO;
import com.util.MyServlet;

@WebServlet("/sale/*")
public class SaleServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();

		// 로그인 정보
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		
		if(uri.indexOf("list.do")!=-1) {	// 처음에 입고 내역 처음 들어가는
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
		}  else if(uri.indexOf("search.do")!=-1) {	// 검색 시 리스트 보여주는
			search(req, resp);
		}		
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException   {
		forward(req, resp, "/WEB-INF/views/sale/list.jsp");		
	}

	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException   {

		ProductDAO dao = new ProductDAO();
		List<SaleDTO> list = dao.listSaleProduct();
		
		req.setAttribute("mode", "created");
		req.setAttribute("list", list);
		forward(req, resp, "/WEB-INF/views/sale/created.jsp");
		
	}
	
	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException   {
		ProductDAO pdao = new ProductDAO();
		SaleDAO sdao = new SaleDAO();
		
		List<SaleDTO> list = pdao.listSaleProduct();
		for(SaleDTO dto : list) {
			dto.setSaleDate(req.getParameter("date1"));
			dto.setDivision(req.getParameter("division"));
			String saleCnt = req.getParameter("saleCnt" + dto.getProductNum());
			dto.setSaleCnt(Integer.parseInt(saleCnt));
			sdao.insertSale(dto);
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/sale/list.do");	
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException   {
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");		// 시간 초과 됐을 경우
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		String saleDate = req.getParameter("saleDate");
		String division = req.getParameter("division");
		
		SaleDAO dao = new SaleDAO();
		List<SaleDTO> list = dao.listDetailSale(saleDate, division);
		
		req.setAttribute("list", list);
		req.setAttribute("mode", "update");
		req.setAttribute("date", saleDate);
		req.setAttribute("division", division);

		forward(req, resp, "/WEB-INF/views/sale/created.jsp");
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException   {
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");		// 시간 초과 됐을 경우
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		String saleDate = req.getParameter("date1");
		String division = req.getParameter("divisionUpdate");
		
		SaleDAO dao = new SaleDAO();
		List<SaleDTO> list = dao.listDetailSale(saleDate, division);
		
		for(SaleDTO dto : list) {
			dto.setSaleDate(saleDate);
			dto.setDivision(division);
			String saleCnt = req.getParameter("saleCnt" + dto.getProductNum());
			dto.setSaleCnt(Integer.parseInt(saleCnt));
			dao.updateSale(dto);
		}

		forward(req, resp, "/WEB-INF/views/sale/list.jsp");		
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException   {
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");		// 시간 초과 됐을 경우
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		// 파라미터
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		String saleDate = req.getParameter("saleDate");
		String division = req.getParameter("division");
				
		SaleDAO dao = new SaleDAO();
		dao.deleteSale(saleDate, division);		
		
		
		int dataCount = dao.dataCount(startDate, endDate);
		List<SaleDTO> list = dao.listSale(startDate, endDate);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		
		forward(req, resp, "/WEB-INF/views/sale/listSale.jsp");
	}
	
	private void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException   {
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");		// 시간 초과 됐을 경우
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		// 파라미터
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
				
		SaleDAO dao = new SaleDAO();
		int dataCount = dao.dataCount(startDate, endDate);
		List<SaleDTO> list = dao.listSale(startDate, endDate);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		
		forward(req, resp, "/WEB-INF/views/sale/listSale.jsp");
	}
	
}
