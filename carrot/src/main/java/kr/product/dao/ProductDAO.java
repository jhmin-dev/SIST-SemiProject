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

	/*
	// 동네 목록
	public List<AddressVO> getListAddress() throws Exception {
		List<AddressVO> list = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT address, "
					// 광역자치단체
					+ "SUBSTR(address, 1, INSTR(address, ' ')-1) AS sido, "
					// 시군구
					+ "SUBSTR(address, INSTR(address, ' ')+1, INSTR(address, ' ', 1, 2)-1-INSTR(address, ' ')) AS sigungu, "
					// 읍면동
					+ "SUBSTR(address, INSTR(address, ' ', 1, 2)+1) AS bname "
				+ "FROM (SELECT DISTINCT(address) FROM aproduct JOIN amember_detail USING(amember_num)) "
				+ "ORDER BY address";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			list = new ArrayList<AddressVO>();
			while(rs.next()) {
				AddressVO addressVO = new AddressVO();
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
	*/
	
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
	public int getCountProduct(String category, String keyword, String address) throws Exception {
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
			if(address!=null && !address.isEmpty()) sub_sql += "AND address LIKE ? ";

			sql = "SELECT COUNT(*) FROM aproduct JOIN amember_detail USING(amember_num) "
				+ "WHERE status=2 AND complete=0 " + sub_sql;
			
			pstmt = conn.prepareStatement(sql);
			
			if(category!=null && !category.isEmpty()) pstmt.setInt(++bind, Integer.parseInt(category));
			if(keyword!=null && !keyword.isEmpty()) pstmt.setString(++bind, "%" + keyword + "%");
			if(address!=null && !address.isEmpty()) pstmt.setString(++bind, "%" + address + "%");
			
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
	public List<ProductVO> getListProduct(int startCount, int endCount, String category, String keyword, String address) throws Exception {
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
			if(address!=null && !address.isEmpty()) sub_sql += "AND address LIKE ? ";
			
			sql = "SELECT * FROM (SELECT r.*, ROWNUM AS rnum "
				// 판매자 동네 결합
				+ "FROM (SELECT * FROM (SELECT p.*, d.address "
					+ "FROM aproduct p JOIN amember_detail d ON p.amember_num=d.amember_num) "
					// 상품별 채팅 수 계산
					+ "LEFT JOIN (SELECT COUNT(achatroom_num) AS chats, aproduct_num FROM achatroom "
						+ "JOIN	(SELECT COUNT(achat_num), achatroom_num FROM achatroom "
							+ "JOIN achat USING(achatroom_num) GROUP BY(achatroom_num)) "
							+ "USING(achatroom_num) GROUP BY aproduct_num) "
					+ "USING(aproduct_num) "
					// 상품별 댓글 수 계산
					+ "JOIN (SELECT aproduct.aproduct_num, COUNT(acomment.aproduct_num) AS replies "
						+ "FROM aproduct LEFT JOIN acomment "
						+ "ON aproduct.aproduct_num=acomment.aproduct_num "
						+ "GROUP BY aproduct.aproduct_num) "
					+ "USING(aproduct_num) "
					// 상품별 관심 수 계산
					+ "JOIN (SELECT aproduct.aproduct_num, COUNT(amyproduct.aproduct_num) AS likes "
						+ "FROM aproduct LEFT JOIN amyproduct "
						+ "ON aproduct.aproduct_num=amyproduct.aproduct_num "
						+ "GROUP BY aproduct.aproduct_num) "
					+ "USING(aproduct_num) "
				// 검색 처리
				+ "WHERE status=2 AND complete=0 " + sub_sql
				// 정렬
				+ "ORDER BY aproduct_num DESC) r)"
				// 페이지 처리
				+ "WHERE rnum >= ? AND rnum <=?";
			
			pstmt = conn.prepareStatement(sql);
			
			if(category!=null && !category.isEmpty()) pstmt.setInt(++bind, Integer.parseInt(category));
			if(keyword!=null && !keyword.isEmpty()) pstmt.setString(++bind, "%" + keyword + "%");
			if(address!=null && !address.isEmpty()) pstmt.setString(++bind, "%" + address + "%");
			pstmt.setInt(++bind, startCount);
			pstmt.setInt(++bind, endCount);
			
			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while(rs.next()) {
				ProductVO vo = new ProductVO();
				vo.setAproduct_num(rs.getInt("aproduct_num"));
				vo.setAmember_num(rs.getInt("amember_num"));
				vo.setPhoto1(rs.getString("photo1"));
				vo.setTitle(StringUtil.useNoHtml(rs.getString("title"))); // 제목에 HTML 태그를 허용하지 않음
				vo.setPrice(rs.getInt("price"));
				vo.setCategory(rs.getInt("category"));
				vo.setAddress(rs.getString("address"));
				vo.setChats(rs.getInt("chats"));
				vo.setReplies(rs.getInt("replies"));
				vo.setLikes(rs.getInt("likes"));
				vo.setReg_date(rs.getDate("reg_date"));
				vo.setModify_date(rs.getDate("modify_date"));
				
				list.add(vo);
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
	public ProductVO getProduct(int aproduct_num) throws Exception {
		ProductVO product = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT p.*, m.nickname, m.photo, m.address, m.rate, c.name AS cname, "
				+ "ch.chats, cmt.replies, my.likes FROM aproduct p "
				// 판매자 정보 결합
				+ "JOIN amember_detail m ON p.amember_num=m.amember_num "
				// 상품 분류명 결합
				+ "JOIN acategory c ON p.category=c.category "
				// 채팅 수 계산
				+ "LEFT JOIN (SELECT COUNT(achatroom_num) AS chats, aproduct_num FROM achatroom "
						+ "JOIN (SELECT COUNT(achat_num), achatroom_num FROM achatroom JOIN achat "
						+ "USING(achatroom_num) GROUP BY(achatroom_num)) "
					+ "USING(achatroom_num) GROUP BY aproduct_num) ch "
				+ "ON p.aproduct_num=ch.aproduct_num "
				// 댓글 수 계산
				+ "JOIN (SELECT aproduct.aproduct_num, COUNT(acomment.aproduct_num) AS replies "
					+ "FROM aproduct LEFT JOIN acomment "
					+ "ON aproduct.aproduct_num=acomment.aproduct_num "
					+ "GROUP BY aproduct.aproduct_num) cmt "
				+ "ON p.aproduct_num=cmt.aproduct_num "
				// 관심 상품 수 계산
				+ "JOIN (SELECT aproduct.aproduct_num, COUNT(amyproduct.aproduct_num) AS likes "
					+ "FROM aproduct LEFT JOIN amyproduct "
					+ "ON aproduct.aproduct_num=amyproduct.aproduct_num "
					+ "GROUP BY aproduct.aproduct_num) my "
				+ "ON p.aproduct_num=my.aproduct_num "
				// 조건절
				+ "WHERE p.aproduct_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, aproduct_num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				// 물품 상세 정보
				product = new ProductVO();
				product.setAproduct_num(aproduct_num);
				product.setAmember_num(rs.getInt("amember_num"));
				// 사진
				product.setPhoto1(rs.getString("photo1"));
				product.setPhoto2(rs.getString("photo2"));
				product.setPhoto3(rs.getString("photo3"));
				product.setPhoto4(rs.getString("photo4"));
				product.setPhoto5(rs.getString("photo5"));
				// 판매글 정보
				product.setTitle(rs.getString("title"));
				product.setContent(rs.getString("content"));
				product.setReg_date(rs.getDate("reg_date"));
				product.setModify_date(rs.getDate("modify_date"));
				product.setPrice(rs.getInt("price"));
				// 채팅, 댓글, 관심
				product.setChats(rs.getInt("chats"));
				product.setReplies(rs.getInt("replies"));
				product.setLikes(rs.getInt("likes"));
				// 판매글 상태
				product.setComplete(rs.getInt("complete"));
				product.setStatus(rs.getInt("status"));
				product.setBuyer_num(rs.getInt("buyer_num"));
				
				// 판매자 정보
				MemberVO member = new MemberVO();
				member.setNickname(rs.getString("nickname"));
				member.setAddress(rs.getString("address"));
				member.setPhoto(rs.getString("photo"));
				if(rs.getString("rate")!=null) member.setRate(rs.getDouble("rate"));
				product.setMemberVO(member);
				
				// 카테고리 정보
				CategoryVO category = new CategoryVO();
				category.setCategory(rs.getInt("category"));
				category.setName(rs.getString("cname"));
				product.setCategoryVO(category);
			}
		}
		catch(Exception e) {
			throw new Exception();
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return product;
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
	
	/*
	// 거래 완료하기
	public void setComplete(int aproduct_num, int buyer_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "UPDATE aproduct SET complete=1, buyer_num=? WHERE aproduct_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, buyer_num);
			pstmt.setInt(2, aproduct_num);
			
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
	public void insertMyProduct(MyProductVO vo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();

			sql = "INSERT INTO amyproduct (amyproduct_num, aproduct_num, amember_num) "
				+ "VALUES (amyproduct_seq.NEXTVAL, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, vo.getAproduct_num());
			pstmt.setInt(2, vo.getAmember_num());
			
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
	public boolean existsMyProduct(MyProductVO vo) throws Exception {
		boolean exist = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT * FROM amyproduct WHERE aproduct_num=? AND amember_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, vo.getAproduct_num());
			pstmt.setInt(2, vo.getAmember_num());
			
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
	public void deleteMyProduct(MyProductVO vo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "DELETE FROM amyproduct WHERE aproduct_num=? AND amember_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, vo.getAproduct_num());
			pstmt.setInt(2, vo.getAmember_num());
			
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	*/
}