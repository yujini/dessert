package com.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.incoming.IncomingDTO;
import com.sale.SaleDTO;
import com.util.DBCPConn;

public class ProductDAO {
	
	// ������ ���� ���� ��ǰ list
	public List<ProductDTO> listReservationProduct() {
		List<ProductDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select productNum, productGroup, name, price, cnt "
					+ "from product "
					+ "where issale = 'yes' and instr(productgroup, 'whole') >= 1 "
					+ "order by productgroup desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ProductDTO dto = new ProductDTO();
				dto.setProductNum(rs.getInt("productNum"));
				dto.setProductGroup(rs.getString("productGroup"));
				switch(dto.getProductGroup()) {
				case "wholecake4":
					dto.setProductGroupKor("(4ȣ)");
					break;
				case "wholecake2":
					dto.setProductGroupKor("(2ȣ)");
					break;
				case "wholecake1":
					dto.setProductGroupKor("(1ȣ)");
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
	
	// �Ǹ� ���� ����� ���� �� ��ǰ list
	public List<SaleDTO> listSaleProduct() {
		List<SaleDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select productNum, productGroup, name, cnt from product order by productGroup desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				SaleDTO dto = new SaleDTO();
				dto.setProductNum(rs.getInt("productNum"));
				dto.setProductGroup(rs.getString("productGroup"));
				switch(dto.getProductGroup()) {
				case "wholecake4":
					dto.setProductGroupKor("Ȧ����(4ȣ)");
					break;
				case "wholecake2":
					dto.setProductGroupKor("Ȧ����(2ȣ)");
					break;
				case "wholecake1":
					dto.setProductGroupKor("Ȧ����(1ȣ)");
					break;
				case "piececake":
					dto.setProductGroupKor("����(����)");
					break;
				case "macaron":
					dto.setProductGroupKor("��ī��");
					break;
				}
				dto.setName(rs.getString("name"));
				dto.setProductCnt(rs.getInt("cnt"));
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
	
	// �԰� ���� ����� ���� �� ��ǰ list
	public List<IncomingDTO> listProduct() {
		List<IncomingDTO> list = new ArrayList<>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select productNum, productGroup, name from product order by productGroup desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				IncomingDTO dto = new IncomingDTO();
				dto.setProductNum(rs.getInt("productNum"));
				dto.setProductGroup(rs.getString("productGroup"));
				switch(dto.getProductGroup()) {
				case "wholecake4":
					dto.setProductGroupKor("Ȧ����(4ȣ)");
					break;
				case "wholecake2":
					dto.setProductGroupKor("Ȧ����(2ȣ)");
					break;
				case "wholecake1":
					dto.setProductGroupKor("Ȧ����(1ȣ)");
					break;
				case "piececake":
					dto.setProductGroupKor("����(����)");
					break;
				case "macaron":
					dto.setProductGroupKor("��ī��");
					break;
				}
				dto.setName(rs.getString("name"));
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

	// ����Ʈ(pGroup ����)
	public List<ProductDTO> listProduct(String pGroup) {
		List<ProductDTO> list = new ArrayList<ProductDTO>();
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select productNum, productGroup, name, filename, price, memo, cnt, isBest, isSale "
					+ "from product where productGroup = ? "
					+ "order by isBest desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pGroup);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ProductDTO dto = new ProductDTO();
				dto.setProductNum(rs.getInt("productNum"));
				dto.setProductGroup(rs.getString("productGroup"));
				dto.setName(rs.getString("name"));
				dto.setFileName(rs.getString("filename"));
				dto.setPrice(rs.getInt("price"));
				dto.setMemo(rs.getString("memo"));
				dto.setCnt(rs.getInt("cnt"));
				dto.setIsBest(rs.getString("isBest"));
				dto.setIsSale(rs.getString("isSale"));
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
	
	// dataCount(pGroup ����)
	public int dataCount(String pGroup) {
		int result = 0;
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {		
			sql = "select count(*) from product where productgroup = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pGroup);
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
					// TODO: handle exception
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn); // �ݵ�� ���� ���߿� �ݴ´�.
		}
		return result;
	}	
	
	// �ϳ��� product read - ��ǰ �ڵ�
	public ProductDTO readProduct(int num) {
		ProductDTO dto = null;
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select productNum, productGroup, name, filename, price, memo, cnt, isSale, isBest "
					+ "from product where productNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new ProductDTO();
				dto.setProductNum(rs.getInt("productNum"));
				dto.setProductGroup(rs.getString("productGroup"));
				dto.setName(rs.getString("name"));
				dto.setFileName(rs.getString("filename"));
				dto.setPrice(rs.getInt("price"));
				dto.setMemo(rs.getString("memo"));
				dto.setCnt(rs.getInt("cnt"));
				dto.setIsSale(rs.getString("isSale"));
				dto.setIsBest(rs.getString("isBest"));
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
			DBCPConn.close(conn);		/// �� �ݾ��ֱ�!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}
		return dto;
	}
			
	// �ϳ���  product insert
	public void insertProduct(ProductDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into product(productNum, productGroup, name, filename, price, memo, cnt, isBest) "
					+ "values(pro_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getProductGroup());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getFileName());
			pstmt.setInt(4, dto.getPrice());
			pstmt.setString(5, dto.getMemo());
			pstmt.setInt(6, dto.getCnt());
			pstmt.setString(7, dto.getIsBest());
			
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
			DBCPConn.close(conn); // �ݵ�� ���� ���߿� �ݴ´�.
		}
	}	
	
	// �ϳ���  product update
	public void updateProduct(ProductDTO dto) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update product set productGroup = ?, name = ?, filename = ?, price = ?, memo = ?, "
					+ "cnt = ?, isSale = ?, isBest = ? "
					+ "where productNum = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getProductGroup());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getFileName());
			pstmt.setInt(4, dto.getPrice());
			pstmt.setString(5, dto.getMemo());
			pstmt.setInt(6, dto.getCnt());
			pstmt.setString(7, dto.getIsSale());
			pstmt.setString(8, dto.getIsBest());
			pstmt.setInt(9, dto.getProductNum());
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
			DBCPConn.close(conn); // �ݵ�� ���� ���߿� �ݴ´�.
		}
	}	
		
	
	
	public void deleteProduct(int productNum) {
		Connection conn = DBCPConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete from product where productNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, productNum);
			pstmt.executeQuery();		
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			DBCPConn.close(conn); // �ݵ�� ���� ���߿� �ݴ´�.
		}
	}		
}
