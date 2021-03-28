package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

// ȸ�����Կ� �ش��ϴ� DB �۾�
public class MemberDAO {	//  finally�� �˾Ƽ� �߰�..
	private Connection conn=DBConn.getConnection();
	
	public List<String> idList() {
		List<String> list = new ArrayList<String>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql = "select id from member1 where id is not null";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("id"));
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
		}
		
		return list;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param tel
	 * @return ȸ����ȣ - 0 �̸� ���� ȸ��
	 */
	public MemberDTO readMember(String name, String tel) {	// �̸�, ��ȭ��ȣ�� ȸ���˻�
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb = new StringBuffer();
		MemberDTO dto = null;
		
		sb.append("SELECT m1.memberNum, id, name, pwd, tel, ismember, ");
		sb.append("      TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
		sb.append("      zip, addr1, addr2, email");
		sb.append("      FROM member1 m1");
		sb.append("      LEFT OUTER JOIN member2 m2 ");		// han ����ڰ� member2�� �����Ͱ� ����. // ����(m1)�� �� ������ ������(m2)�� ������ �� ���´�.
		sb.append("      ON m1.memberNum=m2.memberNum");
		sb.append("      WHERE name=? and tel = ? and isMember='Y'");
		
		try {
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, name);
			pstmt.setString(2, tel);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setMemberNum(rs.getInt("memberNum"));
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				if(dto.getTel()!=null) {
					String[] ss=dto.getTel().split("-");
					if(ss.length==3) {
						dto.setTel1(ss[0]);
						dto.setTel2(ss[1]);
						dto.setTel3(ss[2]);
					}
				}				
				dto.setIsMember(rs.getString("ismember"));
				dto.setBirth(rs.getString("birth"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setEmail(rs.getString("email"));
				if(dto.getEmail()!=null) {
					String[] ss=dto.getEmail().split("@");
					if(ss.length==2) {
						dto.setEmail1(ss[0]);
						dto.setEmail2(ss[1]);
					}
				}
			}
			rs.close();
			pstmt.close();
			pstmt=null;
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return dto;
	}
	
	
	public MemberDTO readMember(String id) {
		MemberDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT m1.memberNum, id, name, pwd, tel, ismember, ");
			sb.append("      TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
			sb.append("      zip, addr1, addr2, email");
			sb.append("      FROM member1 m1");
			sb.append("      LEFT OUTER JOIN member2 m2 ");		// han ����ڰ� member2�� �����Ͱ� ����. // ����(m1)�� �� ������ ������(m2)�� ������ �� ���´�.
			sb.append("      ON m1.memberNum=m2.memberNum");
			sb.append("      WHERE id=? and isMember='Y'");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setMemberNum(rs.getInt("memberNum"));
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				if(dto.getTel()!=null) {
					String[] ss=dto.getTel().split("-");
					if(ss.length==3) {
						dto.setTel1(ss[0]);
						dto.setTel2(ss[1]);
						dto.setTel3(ss[2]);
					}
				}				
				dto.setIsMember(rs.getString("ismember"));
				dto.setBirth(rs.getString("birth"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setEmail(rs.getString("email"));
				if(dto.getEmail()!=null) {
					String[] ss=dto.getEmail().split("@");
					if(ss.length==2) {
						dto.setEmail1(ss[0]);
						dto.setEmail2(ss[1]);
					}
				}
			}
			rs.close();
			pstmt.close();
			pstmt=null;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;
	}
	
	
	public void insertMember(String name, String tel) {
		PreparedStatement pstmt=null;
		String sql;
		try {
			sql = "insert into member1(memberNum, name, tel) values(mem_seq.nextval, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, tel);
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
		} catch (Exception e) {
			System.out.println("insertMember::::" + e.toString());	// �̹� ��ϵ� ���̵�ų� ȸ������ ����
		}
		
	}
	
	public int insertMember(MemberDTO dto) {	// ���̵� �ߺ��̸� �߰� �ȵȴ�.
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			// ������ ******* nextval, currval �ϸ� ��ų���� �ֳ�?	
			
			sb.append("INSERT INTO member1(memberNum, id, name, pwd, tel) VALUES (mem_seq.nextval, ?, ?, ?, ?)");
			pstmt=conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPwd());
			pstmt.setString(4, dto.getTel());
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			// sb=new StringBuffer();
			sb.delete(0, sb.length());			
			sb.append("INSERT INTO member2(memberNum, birth, zip, addr1, addr2, email) VALUES (mem_seq.currval, ?, ?, ?, ?, ?)");
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getZip());
			pstmt.setString(3, dto.getAddr1());
			pstmt.setString(4, dto.getAddr2());
			pstmt.setString(5, dto.getEmail());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
		} catch (Exception e) {
			System.out.println(e.toString());	// �̹� ��ϵ� ���̵�ų� ȸ������ ����
		}
		
		return result;
	}
	
	public int updateMember(MemberDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try {	// modify_date : �ֱ� �α����� ����
			sb.append("UPDATE member1 SET pwd=?, tel=? WHERE id=?");
			pstmt=conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getPwd());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getId());	// id�� �ص� �ǰ� memberNum ���� �ص� �ȴ�.
			
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			sb.delete(0, sb.length());
			
			sb.append("UPDATE member2 SET birth=to_date(?, 'yyyy-mm-dd'), zip=?, addr1=?, addr2=?, email=? WHERE memberNum=?");
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getZip());
			pstmt.setString(3, dto.getAddr1());
			pstmt.setString(4, dto.getAddr2());
			pstmt.setString(5, dto.getEmail());
			pstmt.setInt(6, dto.getMemberNum());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	public int deleteMember(int memberNum) {	// member2 ������ memberNum �����ϰ� �� �����. 
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {	// 
			sql="UPDATE member1 SET isMember='N' WHERE memberNum = ?";	// member1�� ������ ������ �ʴ´�. 
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, memberNum);
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			sql="DELETE FROM member2 WHERE memberNum = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, memberNum);
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
		} catch (Exception e) {
			System.out.println();
		}
		
		return result;
	}

}
