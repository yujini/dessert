package com.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.product.ProductDTO;
import com.util.DBCPConn;

public class ReservationDAO {
	
	public List<ReservationDTO> listMonth(String startDate, String endDate) {
		List<ReservationDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select reservationnum, name, to_char(reservationdate, 'yyyymmdd') reservationdate, reservationhour, reservationmin ");
			sb.append("from reservation r join member1 m ");
			sb.append("on r.membernum = m.membernum ");
			sb.append("where reservationdate >= to_date(?, 'yyyymmdd') ");	// startDate 
			sb.append("and reservationdate <= to_date(?, 'yyyymmdd') ");		// endDate
			sb.append("order by reservationdate asc, reservationhour asc");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setReservationNum(rs.getInt("reservationnum"));
				dto.setMemName(rs.getString("name"));
				dto.setReservationDate(rs.getString("reservationdate"));
				dto.setReservationHour(rs.getString("reservationhour"));
				dto.setReservationMin(rs.getString("reservationmin"));
				list.add(dto);
			}			
			
		}  catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
		return list;
	}
	
	public void deleteReplyAnswer(int replyNum) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		
		String sql;
		
		try {
			sql = "delete from reply where replyNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, replyNum);
			pstmt.executeUpdate();
			
		}  catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
		
	}
	
	
	public void deleteReply(int replyNum) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		
		String sql;
		
		try {
			sql = "delete from reply "
					+ "where replyNum in ( "
					+ "		select replyNum from reply "
					+ "		start with replyNum = ? "
					+ "		connect by prior replyNum = answer "
					+ ")";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, replyNum);
			pstmt.executeUpdate();
			
		}  catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
		
	}
	
	public int dataCountReplyAnswer(int answer) {
		Connection conn = DBCPConn.getConnection();
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select count(*) from Reply where answer = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, answer);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		}  catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
		
		return result;
	}
	
	public List<ReplyDTO> listReplyAnswer(int answer) {
		Connection conn = DBCPConn.getConnection();
		List<ReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {	
			sql = "select replynum, m.id, name, content, created, answer "
					+ "from reply r join member1 m "
					+ "on r.userid = m.id "
					+ "where answer = ? "
					+ "order by replynum desc ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, answer);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReplyDTO dto = new ReplyDTO();
				dto.setReplyNum(rs.getInt("replynum"));
				dto.setUserId(rs.getString("id"));
				dto.setUserName(rs.getString("name"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setAnswer(rs.getInt("answer"));
				list.add(dto);
			}
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
		return list;
	}
	
	public int dataCountReply() {
		Connection conn = DBCPConn.getConnection();
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {	// answer = 0 넣어야 하는 이유? 답글만 나와야 하므로	(답글의 답글은 제외)
			sql = "select count(*) from Reply where answer = 0";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
		return result;
	}
	
	public List<ReplyDTO> listReply(int start, int end) {
		Connection conn = DBCPConn.getConnection();
		List<ReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select * from ( ");
			sb.append("    select rownum rnum, tb.* from ( ");
			sb.append("        select replynum, userid, name, content, created, ");
			sb.append("            nvl(answerCount, 0) answerCount ");
			sb.append("        from reply r join member1 m ");
			sb.append("        on r.userid = m.id ");
			sb.append("        left outer join ( ");
			sb.append("            select answer, count(*) answercount ");
			sb.append("            from reply where answer != 0");
			sb.append("            group by answer");
			sb.append("        ) a on r.replynum = a.answer ");
			sb.append("        where r.answer = 0");
			sb.append("        order by r.replynum desc");
			sb.append("    ) tb where rownum <= ? ");
			sb.append(")where rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReplyDTO dto = new ReplyDTO();
				dto.setReplyNum(rs.getInt("replynum"));
				dto.setUserId(rs.getString("userid"));
				dto.setUserName(rs.getString("name"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setAnswerCount(rs.getInt("answerCount"));
				list.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
		return list;
	}
	
	public void insertReply(ReplyDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into reply(replynum, userid, content, answer) "
					+ "values(reply_seq.nextval, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getAnswer());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("reservationdao, insertReply" + e.toString());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}
	}
	
	public int dataCount() {
		int result = 0;
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select count(*) from (");
			sb.append("        select r.reservationnum, m.name, RESERVATIONDATE, RESERVATIONHOUR, RESERVATIONMIN, cnt, productName, productgroup");
			sb.append("        from RESERVATION r join member1 m");
			sb.append("        on r.membernum = m.membernum");
			sb.append("        join (");
			sb.append("            select re.reservationnum, re.cnt, re.productnum, p.name productName, productgroup ");
			sb.append("            from product p join (");
			sb.append("                select RESERVATIONNUM, count(productnum) cnt, max(productnum) productnum");
			sb.append("                from DETAILRESERVATION");
			sb.append("                group by RESERVATIONNUM ) re");
			sb.append("            on p.productnum = re.productnum");
			sb.append("        ) temp ");
			sb.append("        on r.RESERVATIONNUM = temp.RESERVATIONNUM");
			sb.append("        order by r.RESERVATIONDATE, reservationhour, r.RESERVATIONMIN");
			sb.append(")");
			
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println("reservationdao, datatcount" + e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
		
		
		return result;
	}

	public int dataCount(String searchKey, String searchValue) {
		int result = 0;
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select count(*) from (");
			sb.append("        select r.reservationnum, m.name, RESERVATIONDATE, RESERVATIONHOUR, RESERVATIONMIN, cnt, productName, productgroup");
			sb.append("        from RESERVATION r join member1 m");
			sb.append("        on r.membernum = m.membernum");
			sb.append("        join (");
			sb.append("            select re.reservationnum, re.cnt, re.productnum, p.name productName, productgroup ");
			sb.append("            from product p join (");
			sb.append("                select RESERVATIONNUM, count(productnum) cnt, max(productnum) productnum");
			sb.append("                from DETAILRESERVATION");
			sb.append("                group by RESERVATIONNUM ) re");
			sb.append("            on p.productnum = re.productnum");
			sb.append("        ) temp ");
			sb.append("        on r.RESERVATIONNUM = temp.RESERVATIONNUM");
			
			if(searchKey.equals("userName")) {
				sb.append("		where instr(name, ?) = 1");
			} else {
				sb.append("		where to_char(RESERVATIONDATE, 'yyyy-mm-dd') = ?");
			}
			
			sb.append(")");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println("reservationdao, datatcount" + e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
		
		
		return result;
	}
	
	public List<ReservationDTO> listReservation (int start, int end) {
		List<ReservationDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select * from (");
			sb.append("    select rownum rnum, tb.* from (");
			sb.append("        select r.reservationnum, m.name, to_char(RESERVATIONDATE, 'yyyy-mm-dd') RESERVATIONDATE, RESERVATIONHOUR, RESERVATIONMIN, cnt, pickupdate, productName, productgroup");
			sb.append("        from RESERVATION r join member1 m");
			sb.append("        on r.membernum = m.membernum");
			sb.append("        join (");
			sb.append("            select re.reservationnum, re.cnt, re.productnum, p.name productName, productgroup ");
			sb.append("            from product p join (");
			sb.append("                select RESERVATIONNUM, count(productnum) cnt, max(productnum) productnum");
			sb.append("                from DETAILRESERVATION");
			sb.append("                group by RESERVATIONNUM ) re");
			sb.append("            on p.productnum = re.productnum");
			sb.append("        ) temp ");
			sb.append("        on r.RESERVATIONNUM = temp.RESERVATIONNUM");
			sb.append("        order by r.RESERVATIONDATE desc, reservationhour, r.RESERVATIONMIN");
			sb.append("    ) tb where rownum <= ?");
			sb.append(") where rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setReservationNum(rs.getInt("reservationnum"));
				dto.setMemName(rs.getString("name"));
				dto.setReservationDate(rs.getString("RESERVATIONDATE"));
				dto.setReservationHour(rs.getString("RESERVATIONHOUR"));
				dto.setReservationMin(rs.getString("RESERVATIONMIN"));
				dto.setCnt(rs.getInt("cnt"));
				dto.setPickupDate(rs.getString("pickupdate"));
				
				ProductDTO pdto = new ProductDTO();
				pdto.setName(rs.getString("productName"));
				pdto.setProductGroup(rs.getString("productgroup"));
				switch(pdto.getProductGroup()) {
				case "wholecake4":
					pdto.setProductGroupKor("(4호)");
					break;
				case "wholecake2":
					pdto.setProductGroupKor("(2호)");
					break;
				case "wholecake1":
					pdto.setProductGroupKor("(1호)");
					break;
				}
				
				dto.setProduct(pdto);
				list.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println("reservationdao, listReservation" + e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
		return list;
	}
	
	public List<ReservationDTO> listReservation (int start, int end, String searchKey, String searchValue) {
		List<ReservationDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select * from (");
			sb.append("    select rownum rnum, tb.* from (");
			sb.append("        select r.reservationnum, m.name, to_char(RESERVATIONDATE, 'yyyy-mm-dd') RESERVATIONDATE, RESERVATIONHOUR, RESERVATIONMIN, cnt, pickupdate, productName, productgroup");
			sb.append("        from RESERVATION r join member1 m");
			sb.append("        on r.membernum = m.membernum");
			sb.append("        join (");
			sb.append("            select re.reservationnum, re.cnt, re.productnum, p.name productName, productgroup ");
			sb.append("            from product p join (");
			sb.append("                select RESERVATIONNUM, count(productnum) cnt, max(productnum) productnum");
			sb.append("                from DETAILRESERVATION");
			sb.append("                group by RESERVATIONNUM ) re");
			sb.append("            on p.productnum = re.productnum");
			sb.append("        ) temp ");
			sb.append("        on r.RESERVATIONNUM = temp.RESERVATIONNUM");
			
			if(searchKey.equals("userName")) {
				sb.append("		where instr(name, ?) = 1");
			} else {
				sb.append("		where to_char(RESERVATIONDATE, 'yyyy-mm-dd') = ?");
			}
			
			sb.append("        order by r.RESERVATIONDATE desc, reservationhour, r.RESERVATIONMIN");
			sb.append("    ) tb where rownum <= ?");
			sb.append(") where rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setReservationNum(rs.getInt("reservationnum"));
				dto.setMemName(rs.getString("name"));
				dto.setReservationDate(rs.getString("RESERVATIONDATE"));
				dto.setReservationHour(rs.getString("RESERVATIONHOUR"));
				dto.setReservationMin(rs.getString("RESERVATIONMIN"));
				dto.setCnt(rs.getInt("cnt"));
				dto.setPickupDate(rs.getString("pickupdate"));
				
				ProductDTO pdto = new ProductDTO();
				pdto.setName(rs.getString("productName"));
				pdto.setProductGroup(rs.getString("productgroup"));
				switch(pdto.getProductGroup()) {
				case "wholecake4":
					pdto.setProductGroupKor("(4호)");
					break;
				case "wholecake2":
					pdto.setProductGroupKor("(2호)");
					break;
				case "wholecake1":
					pdto.setProductGroupKor("(1호)");
					break;
				}
				
				dto.setProduct(pdto);
				list.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println("reservationdao, listReservation" + e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
		return list;
	}
	
	
	
	public ReservationDTO readReservation(int reservationnum) {
		ReservationDTO dto = null;
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select reservationNum, m.name, tel, ispaid, ISDISCOUNTED, ");
			sb.append("ISLOWTEMPERATURE, to_char(ORDERDATE, 'yyyy-mm-dd') ORDERDATE, to_char(RESERVATIONDATE, 'yyyy-mm-dd') RESERVATIONDATE, RESERVATIONHOUR, RESERVATIONMIN, ");
			sb.append("PICKUPDATE, PRICE, memo ");
			sb.append("from RESERVATION r join Member1 m ");
			sb.append("on r.membernum = m.membernum ");
			sb.append("where reservationnum = ?");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, reservationnum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ReservationDTO();
				dto.setReservationNum(rs.getInt("reservationNum"));
				dto.setMemName(rs.getString("name"));
				dto.setMemTel(rs.getString("tel"));
				dto.setIsPaid(rs.getString("ispaid"));
				dto.setIsDiscounted(rs.getString("ISDISCOUNTED"));
				dto.setIsLowTemperature(rs.getString("ISLOWTEMPERATURE"));
				dto.setOrderDate(rs.getString("ORDERDATE"));
				dto.setReservationDate(rs.getString("RESERVATIONDATE"));
				dto.setReservationHour(rs.getString("RESERVATIONHOUR"));
				dto.setReservationMin(rs.getString("RESERVATIONMIN"));
				dto.setPickupDate(rs.getString("PICKUPDATE"));
				dto.setPrice(rs.getInt("PRICE"));
				dto.setMemo(rs.getString("memo"));				
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}	
		return dto;
	}
	
	
	public List<ProductDTO> readReservationProductList(int reservationnum) {
		List<ProductDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select d.productnum, productgroup, name, price, d.cnt ");
			sb.append("from DETAILRESERVATION d join product p ");
			sb.append("on d.productnum = p.productnum ");
			sb.append("where RESERVATIONNUM = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, reservationnum);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductDTO dto = new ProductDTO();
				dto.setProductNum(rs.getInt("productnum"));
				dto.setProductGroup(rs.getString("productgroup"));
				switch(dto.getProductGroup()) {
				case "wholecake4":
					dto.setProductGroupKor("(4호)");
					break;
				case "wholecake2":
					dto.setProductGroupKor("(2호)");
					break;
				case "wholecake1":
					dto.setProductGroupKor("(1호)");
					break;
				}
				dto.setName(rs.getString("name"));
				dto.setPrice(rs.getInt("price"));
				dto.setCnt(rs.getInt("cnt"));
				list.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}	
		
		
		
		return list;
	}

	public void updatePickupDate(String pickupdate, int reservationnum) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql="";
		
		try {
			if(pickupdate.equals("no")) {
				sql = "update RESERVATION set pickupdate = null where reservationnum = ?";
			} else {
				sql = "update RESERVATION set pickupdate = sysdate where reservationnum = ?";				
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reservationnum);			
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
	}
	
	
	// detailreservation 에 상품 insert
	public void insertDetailReservation(DetailReservationDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into DetailReservation(reservationNum, productNum, cnt) "
					+ "values(res_seq.currval, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getProductNum());
			pstmt.setInt(2, dto.getCnt());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
	}
	
	public void deleteReservation(int reservationNum) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete from RESERVATION where reservationNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reservationNum);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
	}
			
	public void deleteDetailReservation(int reservationNum) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete from detailRESERVATION where reservationNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reservationNum);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}	
	}		
	
	// reservation 에 예약 insert
	public void insertReservation(ReservationDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into Reservation(reservationNum, memberNum, isPaid, isDiscounted, "
					+ " isLowTemperature, orderDate, reservationDate, reservationHour, "
					+ " reservationMin, price, memo) "
					+ "values(res_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getMemberNum());
			pstmt.setString(2, dto.getIsPaid());
			pstmt.setString(3, dto.getIsDiscounted());
			pstmt.setString(4, dto.getIsLowTemperature());
			pstmt.setString(5, dto.getOrderDate());
			pstmt.setString(6, dto.getReservationDate());
			pstmt.setString(7, dto.getReservationHour());
			pstmt.setString(8, dto.getReservationMin());
			pstmt.setInt(9, dto.getPrice());
			pstmt.setString(10, dto.getMemo());
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn);
		}		
	}
	
}
