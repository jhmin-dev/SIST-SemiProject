package kr.member.dao; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.member.vo.MemberVO;
import kr.util.DBUtil;

public class MemberDAO {
	// 싱글턴 패턴
	private static MemberDAO instance = new MemberDAO();
	public static MemberDAO getInstance() {
		return instance;
	}
	private MemberDAO() {}
	
	// 회원 가입
	public void registerMember(MemberVO memberVO) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt =  null;
		PreparedStatement pstmt2 =  null;
		PreparedStatement pstmt3 =  null;
		ResultSet rs = null;
		String sql = null;
		int member = 0; // 시퀀스로 생성한 회원 번호를 보관할 변수
		
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			// 시퀀스로 회원 번호를 생성
			sql = "SELECT member_seq.NEXTVAL FROM DUAL";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				member = rs.getInt(1);
			}
			
			// MEMBER 테이블에 회원 정보 삽입
			sql = "INSERT INTO member (member, id) VALUES(?, ?)";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, member); // 시퀀스로 생성한 회원 번호
			pstmt2.setString(2, memberVO.getId());
			pstmt2.executeUpdate();
			
			// MEMBER_DETAIL 테이블에 회원 상세 정보 삽입
			sql = "INSERT INTO member_detail (member, nickname, password, "
				+ "home, sido, sigungu, bname, phone, email) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt3 = conn.prepareStatement(sql);
			pstmt3.setInt(1, member); // 시퀀스로 생성한 회원 번호
			pstmt3.setString(2, memberVO.getNickname());
			pstmt3.setString(3, memberVO.getPassword());
			pstmt3.setString(4, memberVO.getHome());
			pstmt3.setString(5, memberVO.getSido());
			pstmt3.setString(6, memberVO.getSigungu());
			pstmt3.setString(7, memberVO.getBname());
			pstmt3.setString(8, memberVO.getPhone());
			pstmt3.setString(9, memberVO.getEmail());			
			pstmt3.executeUpdate();
			
			conn.commit();
		}
		catch(Exception e) {
			conn.rollback();
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
	
	// 로그인시 입력한 아이디에 해당하는 회원 정보 조회
	public MemberVO existsMember(String id) throws Exception {
		MemberVO memberVO = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT member, auth, password FROM member LEFT JOIN member_detail USING(member) WHERE id=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				memberVO = new MemberVO();
				memberVO.setMember(rs.getInt("member"));
				memberVO.setId(id);
				memberVO.setAuth(rs.getInt("auth"));
				memberVO.setPassword(rs.getString("password"));
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return memberVO;
	}
	
	// 회원 상세 정보 조회
	public MemberVO getMember(MemberVO memberVO) throws Exception {		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT * FROM member LEFT JOIN member_detail USING(member) WHERE member=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, memberVO.getMember());
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				memberVO.setNickname(rs.getString("nickname"));
				memberVO.setHome(rs.getString("home"));
				memberVO.setSido(rs.getString("sido"));
				memberVO.setSigungu(rs.getString("sigungu"));
				memberVO.setBname(rs.getString("bname"));
				memberVO.setMain(rs.getString("main"));
				memberVO.setPhone(rs.getString("phone"));
				memberVO.setEmail(rs.getString("email"));
				memberVO.setProfile(rs.getString("profile"));
				if(rs.getString("rate")!=null) memberVO.setRate(rs.getDouble("rate")); // 매너 평가 점수 평균이 존재할 때만 값을 VO에 보관
				memberVO.setRegistered(rs.getString("registered"));
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return memberVO;
	}

}