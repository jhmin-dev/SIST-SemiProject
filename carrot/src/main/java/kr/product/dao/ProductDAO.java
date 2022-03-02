package kr.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.member.vo.MemberVO;
import kr.product.vo.AddressVO;
import kr.product.vo.CategoryVO;
import kr.product.vo.MyProductVO;
import kr.product.vo.ProductVO;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class ProductDAO {
	//싱글턴 패턴
	private static ProductDAO instance = new ProductDAO();
	public static ProductDAO getInstance() {
		return instance;
	}
	private ProductDAO() {}

	// 동네 목록
	public List<AddressVO> getListAddress() throws Exception {
		List<AddressVO> list = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT DISTINCT(home), sido, sigungu, bname FROM product JOIN member_detail USING(member) ORDER BY home";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			list = new ArrayList<AddressVO>();
			while(rs.next()) {
				AddressVO addressVO = new AddressVO();
				addressVO.setAddress(rs.getString("home"));
				addressVO.setSido(rs.getString("sido"));
				addressVO.setSigungu(rs.getString("sigungu"));
				addressVO.setBname(rs.getString("bname"));
				
				list.add(addressVO);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}
	
	// 카테고리 목록
	public List<CategoryVO> getListCategory(boolean hidden) throws Exception {
		List<CategoryVO> list = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		
		try {
			conn = DBUtil.getConnection();
			
			if(!hidden) sub_sql = "WHERE hidden<1 "; // 인자가 false이면 숨겨진 카테고리를 제외하고 검색
			
			sql = "SELECT * FROM category " + sub_sql + "ORDER BY sort DESC, category ASC";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			list = new ArrayList<CategoryVO>();
			while(rs.next()) {
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setCategory(rs.getInt("category"));
				categoryVO.setName(rs.getString("name"));
				categoryVO.setHidden(rs.getInt("hidden"));
				categoryVO.setSort(rs.getInt("sort"));
				
				list.add(categoryVO);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}

	// 물품 수
	public int getCountProduct(String category, String keyword, String sido, String sigungu, String bname) throws Exception {
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int bind = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			// 검색 처리
			if(category!=null && !category.isEmpty()) sub_sql += "AND category = ? ";
			if(keyword!=null && !keyword.isEmpty()) sub_sql += "AND title LIKE ? ";
			if(sido!=null && !sido.isEmpty()) sub_sql += "AND sido=? ";
			if(sigungu!=null && !sigungu.isEmpty()) sub_sql += "AND sigungu=? ";
			if(bname!=null && !bname.isEmpty()) sub_sql += "AND bname=? ";

			sql = "SELECT COUNT(product) FROM product JOIN member_detail USING(member) "
				+ "WHERE deleted=0 AND complete=0 " + sub_sql;
			
			pstmt = conn.prepareStatement(sql);
			
			if(category!=null && !category.isEmpty()) pstmt.setInt(++bind, Integer.parseInt(category));
			if(keyword!=null && !keyword.isEmpty()) pstmt.setString(++bind, "%" + keyword + "%");
			if(sido!=null && !sido.isEmpty()) pstmt.setString(++bind, sido);
			if(sigungu!=null && !sigungu.isEmpty()) pstmt.setString(++bind, sigungu);
			if(bname!=null && !bname.isEmpty()) pstmt.setString(++bind, bname);
			
			rs = pstmt.executeQuery();	
			if(rs.next()) {
				count = rs.getInt(1);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return count;
	}
	
	// 물품 목록
	public List<ProductVO> getListProduct(int startCount, int endCount, String category, String keyword, String sido, String sigungu, String bname) throws Exception {
		List<ProductVO> list = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int bind = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			// 검색 처리
			if(category!=null && !category.isEmpty()) sub_sql += "AND category = ? ";
			if(keyword!=null && !keyword.isEmpty()) sub_sql += "AND title LIKE ? ";
			if(sido!=null && !sido.isEmpty()) sub_sql += "AND sido=? ";
			if(sigungu!=null && !sigungu.isEmpty()) sub_sql += "AND sigungu=? ";
			if(bname!=null && !bname.isEmpty()) sub_sql += "AND bname=? ";
			
			sql = "SELECT * FROM (SELECT r.*, ROWNUM AS rnum "
				// 판매자 동네 결합
				+ "FROM (SELECT * FROM (SELECT p.*, d.home, d.sido, d.sigungu, d.bname "
					+ "FROM product p JOIN member_detail d ON p.member=d.member) "
					// 상품별 채팅 수 계산
					+ "LEFT JOIN (SELECT COUNT(chatroom) AS chats, product FROM chatroom "
						+ "JOIN	(SELECT COUNT(chat), chatroom FROM chatroom "
							+ "JOIN chat USING(chatroom) GROUP BY(chatroom)) "
							+ "USING(chatroom) GROUP BY product) "
					+ "USING(product) "
					// 상품별 댓글 수 계산
					+ "JOIN (SELECT product.product, COUNT(reply.product) AS replies "
						+ "FROM product LEFT JOIN reply "
						+ "ON product.product=reply.product "
						+ "GROUP BY product.product) "
					+ "USING(product) "
					// 상품별 관심 수 계산
					+ "JOIN (SELECT product.product, COUNT(myproduct.product) AS likes "
						+ "FROM product LEFT JOIN myproduct "
						+ "ON product.product=myproduct.product "
						+ "GROUP BY product.product) "
					+ "USING(product) "
				// 검색 처리
				+ "WHERE deleted=0 AND complete=0 " + sub_sql
				// 정렬
				+ "ORDER BY product DESC) r)"
				// 페이지 처리
				+ "WHERE rnum >= ? AND rnum <=?";
			
			pstmt = conn.prepareStatement(sql);
			
			if(category!=null && !category.isEmpty()) pstmt.setInt(++bind, Integer.parseInt(category));
			if(keyword!=null && !keyword.isEmpty()) pstmt.setString(++bind, "%" + keyword + "%");
			if(sido!=null && !sido.isEmpty()) pstmt.setString(++bind, sido);
			if(sigungu!=null && !sigungu.isEmpty()) pstmt.setString(++bind, sigungu);
			if(bname!=null && !bname.isEmpty()) pstmt.setString(++bind, bname);
			pstmt.setInt(++bind, startCount);
			pstmt.setInt(++bind, endCount);
			
			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while(rs.next()) {
				ProductVO productVO = new ProductVO();
				productVO.setProduct(rs.getInt("product"));
				productVO.setPhoto1(rs.getString("photo1"));
				productVO.setTitle(StringUtil.useNoHtml(rs.getString("title"))); // 제목에 HTML 태그를 허용하지 않음
				productVO.setPrice(rs.getInt("price"));
				productVO.setRegistered(rs.getString("registered"));
				productVO.setModified(rs.getString("modified"));

				productVO.setChats(rs.getInt("chats"));
				productVO.setReplies(rs.getInt("replies"));
				productVO.setLikes(rs.getInt("likes"));

				MemberVO memberVO = new MemberVO();
				memberVO.setHome(rs.getString("home"));
				memberVO.setSido(rs.getString("sido"));
				memberVO.setSigungu(rs.getString("sigungu"));
				memberVO.setBname(rs.getString("bname"));
				productVO.setMemberVO(memberVO);
				
				list.add(productVO);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}
	
	// 물품 상세 정보
	public ProductVO getProduct(int product) throws Exception {
		ProductVO productVO = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT p.*, m.nickname, m.profile, m.home, m.sido, m.sigungu, m.bname, m.rate, c.name, "
				+ "NVL(ch.chats, 0) AS chats, rp.replies, my.likes FROM product p "
				// 판매자 정보 결합
				+ "JOIN member_detail m ON p.member=m.member "
				// 물품 분류명 결합
				+ "JOIN category c ON p.category=c.category "
				// 채팅 수 계산
				+ "LEFT JOIN (SELECT COUNT(chatroom) AS chats, product FROM chatroom "
						+ "JOIN (SELECT COUNT(chat), chatroom FROM chatroom JOIN chat "
						+ "USING(chatroom) GROUP BY(chatroom)) "
					+ "USING(chatroom) GROUP BY product) ch "
				+ "ON p.product=ch.product "
				// 댓글 수 계산
				+ "JOIN (SELECT product.product, COUNT(reply.product) AS replies "
					+ "FROM product LEFT JOIN reply "
					+ "ON product.product=reply.product "
					+ "GROUP BY product.product) rp "
				+ "ON p.product=rp.product "
				// 관심 상품 수 계산
				+ "JOIN (SELECT product.product, COUNT(myproduct.product) AS likes "
					+ "FROM product LEFT JOIN myproduct "
					+ "ON product.product=myproduct.product "
					+ "GROUP BY product.product) my "
				+ "ON p.product=my.product "
				// 조건절
				+ "WHERE p.product=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, product);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				// 물품 상세 정보
				productVO = new ProductVO();
				productVO.setProduct(product);
				productVO.setMember(rs.getInt("member"));
				// 사진
				productVO.setPhoto1(rs.getString("photo1"));
				productVO.setPhoto2(rs.getString("photo2"));
				productVO.setPhoto3(rs.getString("photo3"));
				productVO.setPhoto4(rs.getString("photo4"));
				productVO.setPhoto5(rs.getString("photo5"));
				// 판매글 정보
				productVO.setTitle(rs.getString("title"));
				productVO.setPrice(rs.getInt("price"));
				productVO.setContent(rs.getString("content"));
				productVO.setRegistered(rs.getString("registered"));
				productVO.setModified(rs.getString("modified"));
				// 채팅, 댓글, 관심
				productVO.setChats(rs.getInt("chats"));
				productVO.setReplies(rs.getInt("replies"));
				productVO.setLikes(rs.getInt("likes"));
				// 판매글 상태
				productVO.setComplete(rs.getInt("complete"));
				productVO.setDeleted(rs.getInt("deleted"));
				productVO.setBuyer(rs.getInt("buyer"));
				
				// 판매자 정보
				MemberVO member = new MemberVO();
				member.setNickname(rs.getString("nickname"));
				member.setHome(rs.getString("home"));
				member.setSido(rs.getString("sido"));
				member.setSigungu(rs.getString("sigungu"));
				member.setBname(rs.getString("bname"));
				member.setProfile(rs.getString("profile"));
				if(rs.getString("rate")!=null) member.setRate(rs.getDouble("rate"));
				productVO.setMemberVO(member);
				
				// 카테고리 정보
				CategoryVO category = new CategoryVO();
				category.setCategory(rs.getInt("category"));
				category.setName(rs.getString("name"));
				productVO.setCategoryVO(category);
			}
		}
		catch(Exception e) {
			throw new Exception();
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return productVO;
	}
	
	// 물품 등록
	public void addProduct(ProductVO productVO) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "INSERT INTO product (product, member, "
				+ "photo1, photo2, photo3, photo4, photo5, title, price, content, category) "
				+ "VALUES (product_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, productVO.getMember());
			pstmt.setString(2, productVO.getPhoto1());
			pstmt.setString(3, productVO.getPhoto2());
			pstmt.setString(4, productVO.getPhoto3());
			pstmt.setString(5, productVO.getPhoto4());
			pstmt.setString(6, productVO.getPhoto5());
			pstmt.setString(7, productVO.getTitle());
			pstmt.setInt(8, productVO.getPrice());
			pstmt.setString(9, productVO.getContent());
			pstmt.setInt(10, productVO.getCategory());
			
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 거래 완료하기
	public void setComplete(int product, int buyer) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "UPDATE product SET complete=1, buyer=? WHERE product=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, buyer);
			pstmt.setInt(2, product);
			
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 관심 상품 추가
	public void insertMyProduct(int product, int member) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();

			sql = "INSERT INTO myproduct (myproduct, product, member) "
				+ "VALUES (myproduct_seq.NEXTVAL, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, product);
			pstmt.setInt(2, member);
			
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 관심 상품 존재 확인
	public boolean existsMyProduct(int product, int member) throws Exception {
		boolean exist = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT myproduct FROM myproduct WHERE product=? AND member=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, product);
			pstmt.setInt(2, member);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				exist = true;
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return exist;
	}
	
	// 관심 상품 삭제
	public void deleteMyProduct(int product, int member) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "DELETE FROM myproduct WHERE product=? AND member=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, product);
			pstmt.setInt(2, member);
			
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

}