package com.sale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBCPConn;

public class SaleDAO {
	
	public void updateSale(SaleDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update sale set cnt = ? "
					+ "where saleDate = ? and division = ? and productNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getSaleCnt());
			pstmt.setString(2, dto.getSaleDate());
			pstmt.setString(3, dto.getDivision());
			pstmt.setInt(4, dto.getProductNum());
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
	
	public void deleteSale(String saleDate, String division) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete from sale where saledate = ? and division = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, saleDate);
			pstmt.setString(2, division);
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
	
	
	public void insertSale(SaleDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into sale(saleNum, saleDate, division, productNum, cnt) "
					+ "values(sale_seq.nextval, to_date(?, 'yyyy-mm-dd'), ?, ?, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSaleDate());
			pstmt.setString(2, dto.getDivision());
			pstmt.setInt(3, dto.getProductNum());
			pstmt.setInt(4, dto.getSaleCnt());
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select count(*) from ( ");
			sb.append("select saledate, division, sum(s.cnt * price) price ");
			sb.append("		from sale s join product p");
			sb.append("		on s.productnum = p.productnum");
			sb.append("		where to_char(saledate, 'yyyy-mm-dd') >= ?");
			sb.append("		    and to_char(saledate, 'yyyy-mm-dd') <= ?");
			sb.append("		group by (saledate, division))");
			
			pstmt = conn.prepareStatement(sb.toString());
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
	
	public List<SaleDTO> listDetailSale(String saledate, String division) {
		List<SaleDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select p.productnum, productgroup, name, price, p.cnt productCnt,t.cnt saleCnt ");
			sb.append("from product p ");
			sb.append("join ( select productnum, cnt from sale where saledate = ? and division = ? ) t ");
			sb.append("on p.productnum = t.productnum ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, saledate);
			pstmt.setString(2, division);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				SaleDTO dto = new SaleDTO();
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
				dto.setPrice(rs.getInt("price"));
				dto.setProductCnt(rs.getInt("productCnt"));
				dto.setSaleCnt(rs.getInt("saleCnt"));				
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
	
	public List<SaleDTO> listSale(String startDate, String endDate) {
		List<SaleDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("select to_char(saledate, 'yyyy-mm-dd') saledate, division, sum(s.cnt * price) price ");
			sb.append("		from sale s join product p");
			sb.append("		on s.productnum = p.productnum");
			sb.append("		where to_char(saledate, 'yyyy-mm-dd') >= ?");
			sb.append("		    and to_char(saledate, 'yyyy-mm-dd') <= ?");
			sb.append("		group by (saledate, division)");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				SaleDTO dto = new SaleDTO();
				dto.setSaleDate(rs.getString("saledate"));
				dto.setDivision(rs.getString("division"));
				dto.setPrice(rs.getInt("price"));
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
