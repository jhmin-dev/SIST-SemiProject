package kr.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;

public class LogoutAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 세션에 보관된 모든 속성 값 제거
		request.getSession().invalidate();
		
		// JSP 경로 반환
		return "redirect:/main/main.do";
	}

}