package com.incoming;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.product.ProductDAO;
import com.util.MyServlet;

import net.sf.json.JSONObject;

@WebServlet("/incoming/*")
public class IncomingServlet extends MyServlet{
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
		
		// article 없다.
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

	private void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");		// 시간 초과 됐을 경우
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		// 파라미터
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		
		IncomingDAO dao = new IncomingDAO();
		int dataCount = dao.dataCount(startDate, endDate);
		List<IncomingDTO> list = dao.listIncoming(startDate, endDate);
		
		JSONObject job = new JSONObject();
		job.put("dataCount", dataCount);
		job.put("list", list);
		
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter pw = resp.getWriter();
		pw.println(job.toString());
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		// parameter : chks(incomingNum), startDate, endDate
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		String[] chks = req.getParameterValues("chks");
		IncomingDAO dao = new IncomingDAO();
		for(String s : chks) {
			dao.deleteIncoming(s);
		}

		resp.sendRedirect(cp + "/incoming/list.do");
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		IncomingDAO dao = new IncomingDAO();
		String startDate = req.getParameter("date1");
		String endDate = req.getParameter("date1");
		
		// form 에서 넘긴 name :  date1, date1에 해당하는  각각의 productNum
		List<IncomingDTO> list = dao.listIncoming(startDate, endDate);
		for(IncomingDTO dto : list) {
			int cnt = Integer.parseInt(req.getParameter(Integer.toString(dto.getIncomingNum())));	// name 이 incomingNum
			dto.setCnt(cnt);
			if(cnt == 0) {
				dao.deleteIncoming(Integer.toString(dto.getIncomingNum()));
			} else {
				dao.updateIncoming(dto);
			}
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/incoming/list.do");
	}

	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
	
		IncomingDAO dao = new IncomingDAO();
		int dataCount = dao.dataCount(startDate, endDate);
		if(dataCount == 0) {
			req.setAttribute("msg", "입고 수정할 내역이 없습니다. 다른 날짜를 선택하세요.");
			forward(req, resp, "/WEB-INF/views/incoming/list.jsp");
			return;
		}
		
		// 수정할 데이터가 있을 경우
		List<IncomingDTO> list = dao.listIncoming(startDate, endDate);
		
		req.setAttribute("list", list);
		req.setAttribute("mode", "update");
		req.setAttribute("date", startDate);
		forward(req, resp, "/WEB-INF/views/incoming/created.jsp");		
	}

	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		String cp = req.getContextPath();		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/main.do");
		}
		
		// parameter : ${dto.productNum} 으로 넘어온다 상품번호의 개수 만큼 수량이 넘어온다. 
		
		ProductDAO pdao = new ProductDAO();
		List<IncomingDTO> list = pdao.listProduct();
		String date = req.getParameter("date1");
		for(IncomingDTO dto : list) {
			String cnt = req.getParameter(Integer.toString(dto.getProductNum()));
			dto.setCnt(Integer.parseInt(cnt));
			dto.setiDate(date);
		}
		
		// 입고 table 에 insert
		// 재고 table 에 update - 자동으로 trigger
		IncomingDAO idao = new IncomingDAO();
		for(IncomingDTO dto : list) {
			if(dto.getCnt() > 0)
				idao.insertIncoming(dto);
		}
		
		resp.sendRedirect(cp + "/incoming/list.do");
	}

	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		// 상품 목록 불러와서 입고내역 등록할 때 출력
		ProductDAO dao = new ProductDAO();
		List<IncomingDTO> list = dao.listProduct();
		
		req.setAttribute("mode", "created");
		req.setAttribute("list", list);
		forward(req, resp, "/WEB-INF/views/incoming/created.jsp");
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		forward(req, resp, "/WEB-INF/views/incoming/list.jsp");
	}

}
