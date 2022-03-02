package kr.chat.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import kr.chat.dao.ChatDAO;
import kr.chat.vo.ChatVO;
import kr.controller.Action;

public class SendChatAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8"); // content가 String이므로 필요
		
		Map<String, String> mapAjax = new HashMap<String, String>();
		
		Integer user = (Integer)request.getSession().getAttribute("user");
		if(user==null) { // 로그인되어 있지 않은 경우
			mapAjax.put("result", "logout");
		}
		else { // 로그인되어 있는 경우
			ChatVO chatVO = new ChatVO();
			chatVO.setChatroom(Integer.parseInt(request.getParameter("chatroom")));
			chatVO.setMember(user);
			chatVO.setOpponent(Integer.parseInt(request.getParameter("opponent")));
			chatVO.setContent(request.getParameter("content"));
			
			ChatDAO.getInstance().sendChat(chatVO);
			
			mapAjax.put("result", "success");
		}
		
		String ajaxData = new ObjectMapper().writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		// JSP 경로 반환
		return "/WEB-INF/views/common/ajax_view.jsp";
	}

}