package kr.chat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.chat.vo.ChatRoomVO;
import kr.chat.vo.ChatVO;
import kr.member.vo.MemberVO;
import kr.product.vo.ProductVO;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class ChatDAO {
	// 싱글턴 패턴
	private static ChatDAO instance = new ChatDAO();
	public static ChatDAO getInstance() {
		return instance;
	}
	private ChatDAO() {}
	
	// 채팅방 생성하기
	public void insertChatRoom(int product, int seller, int buyer) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "INSERT INTO chatroom (chatroom, product, seller, buyer) "
				+ "VALUES (chatroom_seq.NEXTVAL, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, product);
			pstmt.setInt(2, seller);
			pstmt.setInt(3, buyer);
			
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 채팅방 존재 확인하기
	public boolean existsChatRoom(int product, int seller, int buyer) throws Exception {
		boolean exist = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT chatroom FROM chatroom WHERE product=? AND seller=? AND buyer=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, product);
			pstmt.setInt(2, seller);
			pstmt.setInt(3, buyer);
			
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
	
	// 특정 채팅방 번호 가져오기
	public int getChatRoom(int product, int seller, int buyer) throws Exception {
		int chatroom = -1;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT chatroom FROM chatroom WHERE product=? AND seller=? AND buyer=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, product);
			pstmt.setInt(2, seller);
			pstmt.setInt(3, buyer);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chatroom = rs.getInt(1);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return chatroom;
	}
	
	// 특정 채팅방 정보 불러오기
	public ChatRoomVO getChatRoom(int chatroom) throws Exception {
		ChatRoomVO chatroomVO = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT c.*, p.title, p.price, p.photo1, p.deleted, p.complete, p.buyer AS p_buyer, "
				+ "s.nickname AS s_nickname, s.home AS s_home, s.bname AS s_bname, s.rate AS s_rate, s.profile AS s_profile, "
				+ "b.nickname AS b_nickname, b.home AS b_home, b.bname AS b_bname, b.rate AS b_rate, b.profile AS b_profile "
				+ "FROM chatroom c JOIN product p ON c.product=p.product "
				+ "JOIN member_detail s ON c.seller=s.member "
				+ "JOIN member_detail b ON c.buyer=b.member "
				+ "WHERE chatroom=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, chatroom);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chatroomVO = new ChatRoomVO();
				// 채팅방 정보 저장
				chatroomVO.setChatroom(chatroom);
				chatroomVO.setProduct(rs.getInt("product"));
				chatroomVO.setSeller(rs.getInt("seller"));
				chatroomVO.setBuyer(rs.getInt("buyer")); // 구매(희망)자 회원 번호
				// 물품 정보 저장
				ProductVO productVO = new ProductVO();
				productVO.setTitle(rs.getString("title"));
				productVO.setPrice(rs.getInt("price"));
				productVO.setPhoto1(rs.getString("photo1"));
				productVO.setDeleted(rs.getInt("deleted"));
				productVO.setComplete(rs.getInt("complete"));
				productVO.setBuyer(rs.getInt("p_buyer")); // 거래 완료된 경우의 실제 구매자 회원 번호
				chatroomVO.setProductVO(productVO);
				// 물품 판매자 정보 저장
				MemberVO sellerVO = new MemberVO();
				sellerVO.setNickname(rs.getString("s_nickname"));
				sellerVO.setHome(rs.getString("s_home"));
				sellerVO.setBname(rs.getString("s_bname"));
				if(rs.getString("s_rate")!=null) sellerVO.setRate(rs.getDouble("s_rate"));
				sellerVO.setProfile(rs.getString("s_profile"));
				chatroomVO.setSellerVO(sellerVO);
				// 물품 구매(희망)자 정보 저장
				MemberVO buyerVO = new MemberVO();
				buyerVO.setNickname(rs.getString("b_nickname"));
				buyerVO.setHome(rs.getString("b_home"));
				buyerVO.setBname(rs.getString("b_bname"));
				if(rs.getString("b_rate")!=null) buyerVO.setRate(rs.getDouble("b_rate"));
				buyerVO.setProfile(rs.getString("b_profile"));
				chatroomVO.setBuyerVO(buyerVO);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return chatroomVO;
	}
	
	// 특정 채팅방에서 메시지 보내기
	public void sendChat(ChatVO chatVO) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "INSERT INTO chat (chat, chatroom, member, opponent, content) "
				+ "VALUES (chat_seq.NEXTVAL, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, chatVO.getChatroom());
			pstmt.setInt(2, chatVO.getMember());
			pstmt.setInt(3, chatVO.getOpponent());
			pstmt.setString(4, chatVO.getContent());
			
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 특정 채팅방에서 주고 받은 메시지 수 구하기
	public int getCountChat(int chatroom) throws Exception {
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT COUNT(chat) FROM chat WHERE chatroom=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, chatroom);
			
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
	
	// 특정 채팅방에서 주고 받은 메시지 목록을 불러오고, 로그인한 회원이 받은 메시지는 읽음 처리하기
	public List<ChatVO> getListChat(int chatroom, int member, int startCount, int endCount) throws Exception {
		List<ChatVO> chats = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
		
			// 특정 채팅방에서 주고 받은 메시지 목록 불러오기
			sql = "SELECT * FROM (SELECT c.*, ROWNUM AS rnum "
					+ "FROM (SELECT * FROM chat WHERE chatroom=? ORDER BY chat DESC) c) "
				+ "WHERE rnum>=? AND rnum<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, chatroom);
			pstmt.setInt(2, startCount);
			pstmt.setInt(3, endCount);
			rs = pstmt.executeQuery();
			chats = new ArrayList<ChatVO>();
			while(rs.next()) {
				ChatVO chatVO = new ChatVO();
				chatVO.setChat(rs.getInt("chat"));
				chatVO.setChatroom(rs.getInt("chatroom"));
				chatVO.setMember(rs.getInt("member"));
				chatVO.setOpponent(rs.getInt("opponent"));
				chatVO.setContent(StringUtil.useBrNoHtml(rs.getString("content"))); // 내용에 줄바꿈을 제외한 HTML 태그를 허용하지 않음
				chatVO.setSent(rs.getString("sent"));
				chatVO.setReceived(rs.getString("received"));
				chatVO.setRead(rs.getInt("read"));
				chats.add(chatVO);
			}
			
			// 로그인한 회원이 받은 메시지 읽음 처리하기
			sql = "UPDATE chat SET read=1, received=CURRENT_DATE "
				+ "WHERE chatroom=? AND opponent=? AND chat<? AND read=0";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, chatroom);
			pstmt2.setInt(2, member); // 로그인한 회원이 받은 메시지를 검색
			pstmt2.setInt(3, chats.get(0).getChat()+1); // 불러온 메시지 중 가장 최신 메시지의 번호 구하기
			pstmt2.executeUpdate();
			
			conn.commit();
		}
		catch(Exception e) {
			conn.rollback();
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return chats;
	}
	
	// 특정 채팅방에서 안 읽은 메시지 수 구하기
	public int getCountUnread(int chatroom, int member) throws Exception {
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT COUNT(chat) FROM chat WHERE chatroom=? AND opponent=? AND read=0";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, chatroom);
			pstmt.setInt(2, member); // 로그인한 회원이 받은 메시지를 검색
			
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
	
	// 회원별로 채팅방 목록과 각 채팅방의 가장 최근 메시지 1건 불러오기
	public List<ChatRoomVO> getListChatRoom(int member, String filter) throws Exception {
		List<ChatRoomVO> chatrooms = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int bind = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			if(filter!=null && !filter.isEmpty()) {
				if(filter.equals("2")) sub_sql = "AND p.deleted=0 AND p.complete=0 "; // 거래 중
				else if(filter.equals("3")) sub_sql = "AND p.deleted=0 AND ((p.member=? OR p.buyer=?) AND p.complete=1) "; // 거래 완료
			}
			
			sql = "SELECT * FROM chat JOIN (SELECT MAX(chat) AS chat FROM chat "
					+ "JOIN (SELECT c.chatroom, p.deleted, p.complete FROM chatroom c "
						+ "JOIN product p ON c.product=p.product "
						+ "WHERE (c.seller=? OR c.buyer=?) " + sub_sql
					+ ") USING(chatroom) GROUP BY chatroom) "
				+ "USING(chat) ORDER BY chat DESC";
			
			pstmt = conn.prepareStatement(sql);
			
			if(filter!=null && !filter.isEmpty()) {
				if(filter.equals("3")) {
					pstmt.setInt(++bind, member); // 로그인한 회원이 판매 완료한 채팅방 검색
					pstmt.setInt(++bind, member); // 로그인한 회원이 구매 완료한 채팅방 검색
				}
			}
			pstmt.setInt(++bind, member); // 로그인한 회원이 판매자로 참여하고 있는 채팅방 검색
			pstmt.setInt(++bind, member); // 로그인한 회원이 구매자로 참여하고 있는 채팅방 검색
			
			rs = pstmt.executeQuery();
			chatrooms = new ArrayList<ChatRoomVO>();
			while(rs.next()) {
				int chatroom = rs.getInt("chatroom");
				ChatRoomVO chatroomVO = getChatRoom(chatroom);
				ChatVO chatVO = new ChatVO();
				chatVO.setContent(StringUtil.useBrNoHtml(rs.getString("content"))); // 내용에 줄바꿈을 제외한 HTML 태그를 허용하지 않음
				chatVO.setSent(rs.getString("sent"));
				chatroomVO.setChatVO(chatVO);
				chatroomVO.setUnread(getCountUnread(chatroom, member));
				chatrooms.add(chatroomVO);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return chatrooms;
	}
	
	// 회원별로 가장 최근에 받은 메시지 1건 불러오기
	public int getLatestChat(int member) throws Exception {
		int latest_chat = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT MAX(chat) FROM chat WHERE opponent=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, member);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				latest_chat = rs.getInt(1);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return latest_chat;
	}
	
}