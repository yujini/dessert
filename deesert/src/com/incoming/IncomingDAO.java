package com.incoming;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBCPConn;

public class IncomingDAO {
	
	public void updateIncoming(IncomingDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update incoming set cnt = ? where incomingNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getCnt());
			pstmt.setInt(2, dto.getIncomingNum());
			pstmt.executeUpdate();			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn); // 반드시 가장 나중에 닫는다.
		}
	}
	
	public void deleteIncoming(String incomingNum) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete from incoming where incomingNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, incomingNum);
			pstmt.executeUpdate();			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn); // 반드시 가장 나중에 닫는다.
		}
	}
	
	public void insertIncoming(IncomingDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into incoming(incomingNum, productNum, idate, cnt) "
					+ "values(inc_seq.nextval, ?, to_date(?, 'yyyy-mm-dd'), ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getProductNum());
			pstmt.setString(2, dto.getiDate());
			pstmt.setInt(3, dto.getCnt());
			pstmt.executeUpdate();		
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn); // 반드시 가장 나중에 닫는다.
		}
	}

	public int dataCount(String startDate, String endDate) {
		int result = 0;
		Connection conn = DBCPConn.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "select count(*) "
					+ "from incoming i join product p "
					+ "on i.productnum = p.productnum "
					+ "where to_char(idate, 'yyyy-mm-dd') >= ? and to_char(idate, 'yyyy-mm-dd') <= ? and i.cnt > 0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
			DBCPConn.close(conn); // 반드시 가장 나중에 닫는다.
		}
		
		
		return result;
	}
	
	
	public List<IncomingDTO> listIncoming(String startDate, String endDate) {
		List<IncomingDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select incomingnum, i.productnum, productgroup, name, to_char(idate, 'yyyy-mm-dd') idate, i.cnt "
					+ "from incoming i join product p "
					+ "on i.productnum = p.productnum "
					+ "where to_char(idate, 'yyyy-mm-dd') >= ? and to_char(idate, 'yyyy-mm-dd') <= ? and i.cnt > 0 "
					+ "order by idate desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				IncomingDTO dto = new IncomingDTO();
				dto.setIncomingNum(rs.getInt("incomingnum"));
				dto.setProductNum(rs.getInt("productnum"));
				dto.setProductGroup(rs.getString("productgroup"));
				switch(dto.getProductGroup()) {
				case "wholecake4":
					dto.setProductGroupKor("홀케익(4호)");
					break;
				case "wholecake2":
					dto.setProductGroupKor("홀케익(2호)");
					break;
				case "wholecake1":
					dto.setProductGroupKor("홀케익(1호)");
					break;
				case "piececake":
					dto.setProductGroupKor("케익(조각)");
					break;
				case "macaron":
					dto.setProductGroupKor("마카롱");
					break;
				}
				dto.setName(rs.getString("name"));
				dto.setiDate(rs.getString("idate"));
				dto.setCnt(rs.getInt("cnt"));
				list.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
			DBCPConn.close(conn); // 반드시 가장 나중에 닫는다.
		}
		
		return list;
	}
	
}
