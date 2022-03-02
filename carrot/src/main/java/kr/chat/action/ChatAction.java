package kr.chat.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.chat.dao.ChatDAO;
import kr.chat.vo.ChatRoomVO;
import kr.controller.Action;
import kr.member.vo.MemberVO;
import kr.product.vo.ProductVO;

public class ChatAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer user = (Integer)request.getSession().getAttribute("user");
		if(user==null) { // 로그인되어 있지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		// 로그인되어 있는 경우
		ChatDAO dao = ChatDAO.getInstance();
		
		// 채팅 목록 불러오기
		List<ChatRoomVO> chatrooms = dao.getListChatRoom(user, request.getParameter("filter"));
		request.setAttribute("chatrooms", chatrooms);
		
		// 파라미터 값과 채팅 목록 길이를 이용하여 현재 채팅방 번호 지정
		int chatroom = 0;
		if(request.getParameter("chatroom")!=null) chatroom = Integer.parseInt(request.getParameter("chatroom"));
		else if(chatrooms.size()>0) chatroom = chatrooms.get(0).getChatroom();
		
		// 현재 채팅방 정보 불러오기
		ChatRoomVO chatroomVO = null;
		ProductVO productVO = null;
		MemberVO opponentVO = null;
		if(chatroom>0) {
			chatroomVO = dao.getChatRoom(chatroom);
			productVO = chatroomVO.getProductVO();
			if(user==chatroomVO.getSeller()) { // 로그인한 회원이 물품 판매자인 경우
				opponentVO = chatroomVO.getBuyerVO(); // 구매(희망)자 정보 불러오기
			}
			else { // 로그인한 회원이 물품 구매(희망)자인 경우
				opponentVO = chatroomVO.getSellerVO(); // 판매자 정보 불러오기
			}
		}
		
		request.setAttribute("chatroomVO", chatroomVO);
		request.setAttribute("productVO", productVO);
		request.setAttribute("opponentVO", opponentVO);
		
		// JSP 경로 반환
		return "/WEB-INF/views/chat/chat.jsp";
	}

}