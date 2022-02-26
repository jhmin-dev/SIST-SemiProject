package kr.member.action;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class RegisterAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 전송된 데이터 인코딩 처리
		request.setCharacterEncoding("UTF-8");
		
		// 자바빈 생성
		MemberVO memberVO = new MemberVO();
		
		memberVO.setId(request.getParameter("id").toUpperCase()); // 아이디는 DB에 대문자로 저장
		memberVO.setPassword(request.getParameter("password"));
		memberVO.setNickname(request.getParameter("nickname"));	
		
		// 동네 처리
		String home = request.getParameter("home");
		memberVO.setHome(home);
		String[] homes = home.split(" ");
		memberVO.setSido(homes[0]);
		memberVO.setSigungu(homes[1]);
		memberVO.setBname(homes[2]);
		
		// 전화번호 처리
		StringBuffer phone = new StringBuffer(request.getParameterValues("phone")[0]).append("-");
		phone.append(new StringBuffer(request.getParameterValues("phone")[1]).insert(4, "-"));
		memberVO.setPhone(phone.toString());
		
		memberVO.setEmail(request.getParameter("email"));
		
		MemberDAO.getInstance().registerMember(memberVO);
		
		// JSP 경로 반환
		return "/WEB-INF/views/member/register.jsp";
	}

}