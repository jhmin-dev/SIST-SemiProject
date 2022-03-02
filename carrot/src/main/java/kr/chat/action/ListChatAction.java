package kr.chat.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import kr.controller.Action;
import kr.chat.dao.ChatDAO;
import kr.chat.vo.ChatVO;
import kr.util.PagingUtil;

public class ListChatAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNum = request.getParameter("pageNum");
		if(pageNum==null) pageNum = "1";
		
		Map<String, Object> mapAjax = new HashMap<String, Object>();
		
		Integer user = (Integer)request.getSession().getAttribute("user");
		if(user==null) { // 로그인되어 있지 않은 경우
			mapAjax.put("result", "logout");
		}
		else { // 로그인되어 있는 경우
			int chatroom = Integer.parseInt(request.getParameter("chatroom"));
			
			ChatDAO dao = ChatDAO.getInstance();
			
			// 페이지 처리
			int count = dao.getCountChat(chatroom);
			int rowCount = 10;
			PagingUtil page = new PagingUtil(Integer.parseInt(pageNum), count, rowCount, 1, null);

			// 주고 받은 메시지 불러오기
			List<ChatVO> chats = null;
			if(count>0) {
				chats = dao.getListChat(chatroom, user, page.getStartCount(), page.getEndCount());
			}
			else {
				chats = Collections.emptyList(); // 데이터가 없는 경우 null 대신 비어 있는 리스트를 반환
			}
			
			mapAjax.put("count", count);
			mapAjax.put("rowCount", rowCount);
			mapAjax.put("chats", chats);
			mapAjax.put("result", "success");
		}
		
		String ajaxData = new ObjectMapper().writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		// JSP 경로 반환
		return "/WEB-INF/views/common/ajax_view.jsp";
	}

}