package com.reservation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.MemberDAO;
import com.member.MemberDTO;
import com.member.SessionInfo;
import com.product.ProductDAO;
import com.product.ProductDTO;
import com.util.MyServlet;
import com.util.MyUtil;

import net.sf.json.JSONObject;

@WebServlet("/reservation/*")
public class ReservationServlet extends MyServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		
		// 로그인 정보
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		
		if(uri.indexOf("list.do")!=-1) {	// 처음에 예약 내역 처음 들어가는
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		} else if(uri.indexOf("calendar.do")!=-1) {
			calendar(req, resp);
		} else if(uri.indexOf("inquiry.do")!=-1) {
			inquiry(req, resp);
		} else if(uri.indexOf("insertReply.do")!=-1) {
			insertReply(req, resp);
		} else if(uri.indexOf("listReply.do")!=-1) {
			listReply(req, resp);
		} else if(uri.indexOf("listAnswer.do")!=-1) {
			listAnswer(req, resp);
		} else if(uri.indexOf("countAnswer.do")!=-1) {
			countAnswer(req, resp);
		} else if(uri.indexOf("deleteReply.do")!=-1) {
			deleteReply(req, resp);
		} 
	}

	
	// deleteReply, deleteAnswer 둘 다 
	private void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		ReservationDAO dao = new ReservationDAO();
		int replyNum = Integer.parseInt(req.getParameter("replyNum"));
		dao.deleteReply(replyNum);
		
		JSONObject job = new JSONObject();
		PrintWriter pw = resp.getWriter();
		pw.println(job.toString());
	}

	private void countAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		ReservationDAO dao = new ReservationDAO();
		int replyNum = Integer.parseInt(req.getParameter("replyNum"));
		
		int count = dao.dataCountReplyAnswer(replyNum);
		
		JSONObject job = new JSONObject();
		job.put("count", count);
		
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.println(job.toString());
	}

	private void listAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		ReservationDAO dao = new ReservationDAO();
		String replyNum = req.getParameter("replyNum");
		List<ReplyDTO> list = dao.listReplyAnswer(Integer.parseInt(replyNum));
		for(ReplyDTO dto : list) {
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}

		req.setAttribute("list", list);
		
		forward(req, resp, "/WEB-INF/views/reservation/listReplyAnswer.jsp");
	}

	private void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReservationDAO dao = new ReservationDAO();
		MyUtil util = new MyUtil();
		
		String pageNo = req.getParameter("pageNo");
		int current_page = 1;
		if(pageNo != null) {
			current_page = Integer.parseInt(pageNo);
		}
		
		int dataCount = dao.dataCountReply();
		
		int rows = 5;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page)
			current_page = total_page;
		
		int start = (current_page-1)*rows +1;
		int end = (current_page)*rows;
		
		List<ReplyDTO> list = dao.listReply(start, end);
		for(ReplyDTO dto : list) {
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}
		
		String paging = util.paging(current_page, total_page);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("total_page", total_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("pageNo", pageNo);
		
		forward(req, resp, "/WEB-INF/views/reservation/listReply.jsp");
	}

	private void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		ReservationDAO dao = new ReservationDAO();
		
		ReplyDTO dto = new ReplyDTO();
		dto.setUserId(info.getUserId());
		dto.setContent(req.getParameter("content"));
		String answer = req.getParameter("answer");
		if(answer != null) {
			dto.setAnswer(Integer.parseInt(answer));
		}
		
		dao.insertReply(dto);
		
		JSONObject job = new JSONObject();
		job.put("state", "true");
		
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter pw = resp.getWriter();
		pw.println(job.toString());
	}

	private void inquiry(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {

		forward(req, resp, "/WEB-INF/views/reservation/inquiry.jsp");
	}

	private void calendar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		
		ReservationDAO dao = new ReservationDAO();
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int todayYear = year;
		int todayMonth = month;
		int todayDate = cal.get(Calendar.DATE);
		
		String y = req.getParameter("year");
		String m = req.getParameter("month");
		if(y!=null)
			year = Integer.parseInt(y);
		if(m!=null)
			month = Integer.parseInt(m);
		
		cal.set(year, month-1, 1);	
		year = cal.get(Calendar.YEAR);	
		month = cal.get(Calendar.MONTH)+1;
		int week = cal.get(Calendar.DAY_OF_WEEK); // 1(일)~7(토)
		
		Calendar scal = (Calendar)cal.clone();
		scal.add(Calendar.DATE, -(week-1));
		int syear = scal.get(Calendar.YEAR);
		int smonth = scal.get(Calendar.MONTH)+1;
		int sdate = scal.get(Calendar.DATE);
		
		Calendar ecal = (Calendar)cal.clone();
		ecal.add(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		ecal.add(Calendar.DATE, 7-ecal.get(Calendar.DAY_OF_WEEK));
		int eyear = ecal.get(Calendar.YEAR);
		int emonth = ecal.get(Calendar.MONTH)+1;
		int edate = ecal.get(Calendar.DATE);
		
		String startDate = String.format("%04d%02d%02d", syear, smonth, sdate);
		String endDate = String.format("%04d%02d%02d", eyear, emonth, edate);
		List<ReservationDTO> list = dao.listMonth(startDate, endDate);
		
		String s;
		String [][]days = new String[cal.getActualMaximum(Calendar.WEEK_OF_MONTH)][7];
		
		int cnt;
		for(int i=1; i<week; i++) {
			s = String.format("%04d%02d%02d", syear, smonth, sdate);
			days[0][i-1] = "<span class='textDate preMonthDate' data-date='" + s + "' >" + sdate + "</span>";
			
			cnt = 0;
			for(ReservationDTO dto : list) {
				int sd = Integer.parseInt(dto.getReservationDate().substring(4));
				int cn = Integer.parseInt(s.substring(4));
				
				if(cnt==4) {
					days[0][i-1] += "<span class='textDate preMonthDate' data-date='" + s + "' >" + "more..." + "</span>";
					break;
				}
				
				if(sd==cn) {
					days[0][i-1] += "<span class='textDate preMonthDate' data-date='" + s + "' data-num='" + dto.getReservationNum() + "' >" 
										+ dto.getReservationHour() + ":" + dto.getReservationMin() + " " + dto.getMemName() + "</span>";
					cnt++;
				} else if(sd > cn) {
					break;
				}
			}
			sdate++;
		}
		
		int row, n = 0;
		
		jump:
		for(row = 0; row<days.length; row++) {
			for(int i=week-1; i<7; i++) {
				n++;
				s = String.format("%04d%02d%02d", year, month, n);
				
				if(i==0) {
					days[row][i] = "<span class='textDate sundayDate' data-date='" + s + "' >" + n + "</span>";
				} else if(i==6) {
					days[row][i] = "<span class='textDate saturdayDate' data-date='" + s + "' >" + n + "</span>";
				} else {
					days[row][i] = "<span class='textDate nowDate' data-date='" + s + "' >" + n + "</span>";
				}
				
				cnt = 0;
				for(ReservationDTO dto : list) {
					int sd = Integer.parseInt(dto.getReservationDate().substring(4));
					int cn = Integer.parseInt(s.substring(4));
					
					if(cnt==4) {
						days[row][i] += "<span class='scheduleMore' data-date='" + s + "' >" + "more..." + "</span>";
						break;
					}
					
					if(sd==cn) {
						days[row][i] += "<span class='scheduleSubject' data-date='" + s + "' data-num='" + dto.getReservationNum() + "' >" 
											+ dto.getReservationHour() + ":" + dto.getReservationMin() + " " + dto.getMemName() + "</span>";
						cnt++;
					} else if(sd > cn) {
						break;
					}
				}
				
				if(n==cal.getActualMaximum(Calendar.DATE)) {
					week = i+1;
					break jump;
				}
			}
			week = 1;
		}
		
		
		if(week != 7) {
			n = 0;
			for(int i=week; i<7; i++) {
				n++;
				s = String.format("%04d%02d%02d", eyear, emonth, n);
				days[row][i] = "<span class='textDate nextMonthDate' data-date='" + s + "' >" + n + "</span>";
				
				cnt = 0;
				for(ReservationDTO dto : list) {
					int sd = Integer.parseInt(dto.getReservationDate().substring(4));
					int cn = Integer.parseInt(s.substring(4));
					
					if(cnt==4) {
						days[row][i] += "<span class='scheduleMore' data-date='" + s + "' >" + "more..." + "</span>";
						break;
					}
					
					if(sd==cn) {
						days[row][i] += "<span class='scheduleSubject' data-date='" + s + "' data-num='" + dto.getReservationNum() + "' >" 
											+ dto.getReservationHour() + ":" + dto.getReservationMin() + " " + dto.getMemName() + "</span>";
						cnt++;
					} else if(sd > cn) {
						break;
					}
				}
			}
		}
		
		String today = String.format("%04d%02d%02d", todayYear, todayMonth, todayDate);
		
		req.setAttribute("year", year);
		req.setAttribute("month", month);
		req.setAttribute("todayYear", todayYear);
		req.setAttribute("todayMonth", todayMonth);
		req.setAttribute("todayDate", todayDate);
		req.setAttribute("today", today);
		req.setAttribute("days", days);		

		forward(req, resp, "/WEB-INF/views/reservation/calendar.jsp");
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page = Integer.parseInt(page);
		}
		
		// 검색
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if(searchKey == null) {
			searchKey = "userName";
			searchValue = "";
		}
	
		if(req.getMethod().equalsIgnoreCase("get"))  {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
		}
		
		ReservationDAO dao = new ReservationDAO();
		int dataCount = 0;
		if(searchValue.length() != 0) {	// 검색
			dataCount = dao.dataCount(searchKey, searchValue);
		} else {
			dataCount = dao.dataCount();
		}
		
		// 전체 페이지수 구하기
		MyUtil util = new MyUtil();
		int numPerPage = 6;
		int total_page = util.pageCount(numPerPage, dataCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		int start = (current_page - 1) * numPerPage + 1;
		int end = current_page * numPerPage;
		
		// 수취여부 
		String reservationNum = req.getParameter("reservationNum");
		String nullOrSysdate = req.getParameter("nullOrSysdate");
		if(reservationNum != null && nullOrSysdate != null && reservationNum.length() != 0 && nullOrSysdate.length() != 0) {
			dao.updatePickupDate(nullOrSysdate, Integer.parseInt(reservationNum));
		}
		
		// 리스트 가져오기
		List<ReservationDTO> list = null;
		if(searchValue.length() != 0) {	// 검색
			list = dao.listReservation(start, end, searchKey, searchValue);
		} else {
			list = dao.listReservation(start, end);
		}
		
		int listNum, n = 0;
		for(ReservationDTO dto : list) {
			listNum = dataCount - (start+n-1);
			dto.setListNum(listNum);
			n++;
		}
		
		// query
		String query = "";
		if(searchValue.length() != 0) {
			query = "searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "utf-8");
		}
 		
		// 페이징 처리
		String cp = req.getContextPath();
		String listUrl = cp + "/reservation/list.do";
		String articleUrl = cp + "/reservation/article.do?page=" + current_page;
		if(searchValue.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		// 검색 시 뒤에 쿼리 추가
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		if(searchValue.length() != 0) {
			req.setAttribute("searchKey", searchKey);
			req.setAttribute("searchValue", URLDecoder.decode(searchValue, "utf-8"));
		}
		
		forward(req, resp, "/WEB-INF/views/reservation/list.jsp");
	}
	
	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductDAO dao = new ProductDAO();
		List<ProductDTO> list = dao.listReservationProduct();
		
		req.setAttribute("list", list);
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/reservation/created.jsp");
	}
	
	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// memName, tel, isPaid, isDiscounted, isLowTemperature, orderDate, 
		// reservationDate, reservationHour, reservationMin, memo
		// 결제 금액 - totalBuyAmt
		// 예약리스트의 상품 리스트  - name = productNumquantity	  예) 9quantity
		
		ReservationDAO rdao = new ReservationDAO();
		ReservationDTO dto = new ReservationDTO();
		
		MemberDAO mdao = new MemberDAO();
		String memName = req.getParameter("memName");
		String tel = req.getParameter("tel");
		
		MemberDTO m = mdao.readMember(memName, tel);
		int memberNum;
		if(m == null) { 				// 비회원, 회원 모두 존재하지 않을 경우 - 가입
			mdao.insertMember(memName, tel);
			memberNum = mdao.readMember(memName, tel).getMemberNum();
		} else {
			memberNum = m.getMemberNum();			
		}
		
		dto.setMemberNum(memberNum);
		dto.setIsPaid(req.getParameter("isPaid"));
		dto.setIsDiscounted(req.getParameter("isDiscounted"));
		dto.setIsLowTemperature(req.getParameter("isLowTemperature"));
		dto.setOrderDate(req.getParameter("orderDate"));
		dto.setReservationDate(req.getParameter("reservationDate"));
		dto.setReservationHour(req.getParameter("reservationHour"));
		dto.setReservationMin(req.getParameter("reservationMin"));
		dto.setMemo(req.getParameter("memo"));
		if(dto.getIsDiscounted().equals("yes")) {
			dto.setPrice((int)(Integer.parseInt(req.getParameter("totalBuyAmt")) * 0.9));
		} else {
			dto.setPrice(Integer.parseInt(req.getParameter("totalBuyAmt")));			
		}
		rdao.insertReservation(dto);
		
		
		// 상세 예약
		ProductDAO pdao = new ProductDAO();
		List<ProductDTO> list = pdao.listReservationProduct();
		for(ProductDTO p : list) {
			String cnt = req.getParameter(p.getProductNum() + "quantity");
			if(cnt == null) {
				continue;
			} else {
				DetailReservationDTO ddto = new DetailReservationDTO();
				ddto.setProductNum(p.getProductNum());
				ddto.setCnt(Integer.parseInt(cnt));
				rdao.insertDetailReservation(ddto);
			}
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/reservation/list.do");
	}
	
	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// page, searchKey, searchValue
		String page = req.getParameter("page");
		
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if(searchKey == null) {
			searchKey = "userName";
			searchValue = "";
		}

		// get이면 인코딩
		if(req.getMethod().equalsIgnoreCase("get")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
		}
				
		String query = "";
		if(searchValue.length() != 0) {
			query += "searchKey=" + searchKey + "&searchValue=" + searchValue;	
		}
		
		String cp = req.getContextPath();
		String listUrl = cp + "/reservation/list.do";
		if(query.length() != 0) {
			listUrl += "?" + query + "&page=" + page;
		} else {
			listUrl += "?" + "&page=" + page;
		}
		
		ReservationDAO rdao = new ReservationDAO();
		int reservationNum = Integer.parseInt(req.getParameter("reservationNum"));
		ReservationDTO dto = rdao.readReservation(reservationNum);
		List<ProductDTO> list = rdao.readReservationProductList(reservationNum);
		
		req.setAttribute("page", page);
		req.setAttribute("listUrl", listUrl);
		req.setAttribute("searchKey", searchKey);
		req.setAttribute("searchValue", searchValue);
		req.setAttribute("dto", dto);
		req.setAttribute("list", list);
		
		forward(req, resp, "/WEB-INF/views/reservation/article.jsp");
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int reservationNum = Integer.parseInt(req.getParameter("reservationNum"));
		
		ReservationDAO dao = new ReservationDAO();
		dao.deleteDetailReservation(reservationNum);
		dao.deleteReservation(reservationNum);

		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/reservation/list.do");
	}

}
