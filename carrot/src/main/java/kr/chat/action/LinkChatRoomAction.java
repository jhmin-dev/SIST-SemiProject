package kr.chat.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import kr.controller.Action;
import kr.chat.dao.ChatDAO;

public class LinkChatRoomAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> mapAjax = new HashMap<String, Object>();
		
		Integer user = (Integer)request.getSession().getAttribute("user");
		if(user==null) { // 로그인되어 있지 않은 경우
			mapAjax.put("result", "logout");
		}
		else { // 로그인되어 있는 경우
			ChatDAO dao = ChatDAO.getInstance();
			
			int product = Integer.parseInt(request.getParameter("product"));
			int seller = Integer.parseInt(request.getParameter("seller"));
			boolean exist = dao.existsChatRoom(product, seller, user);
			if(exist==false) { // 이전에 채팅한 적이 없는 경우
				dao.insertChatRoom(product, seller, user); // 채팅방 생성
			}
			
			int chatroom = dao.getChatRoom(product, seller, user); // 채팅방 번호 구하기
			if(chatroom>0) { // 정상적인 채팅방 번호가 반환된 경우
				mapAjax.put("chatroom", chatroom);
				mapAjax.put("result", "success");
			}
			else {
				mapAjax.put("result", "wrongAccess");
			}
		}
		
		String ajaxData = new ObjectMapper().writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		// JSP 경로 반환
		return "/WEB-INF/views/common/ajax_view.jsp";
	}

}