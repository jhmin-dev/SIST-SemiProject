package kr.member.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class LoginAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		Map<String, Object> mapAjax = new HashMap<String, Object>();
		
		String id = request.getParameter("id").toUpperCase(); // 사용자 입력 값을 대문자로 변환
		String password = request.getParameter("password");
		
		MemberDAO dao = MemberDAO.getInstance();
		
		boolean login = false; // 로그인 결과를 보관할 변수
		
		MemberVO memberVO = dao.existsMember(id); // 사용자가 입력한 아이디가 존재하는지 조회
		if(memberVO!=null) { // 아이디가 존재하면
			login = memberVO.isValid(password); // 사용자가 입력한 비밀번호가 유효한지 검증
			
			mapAjax.put("auth", memberVO.getAuth()); // 회원 등급에 따른 UI 처리를 위해
		}
		
		if(login) { // 아이디와 비밀번호가 유효하면
			// 로그인 처리
			HttpSession session = request.getSession();
			session.setAttribute("user", memberVO.getMember());
			
			// 사이트 이용시 필요한 정보를 추가로 세션에 보관
			memberVO = dao.getMember(memberVO); // 회원 상세 정보 조회
			session.setAttribute("nickname", memberVO.getNickname());
			session.setAttribute("profile", memberVO.getProfile());
			
			// 로그인한 사용자의 동네 필터링을 위한 정보 처리
			String sido = null;
			String sigungu = null;
			String bname = null;
			if(memberVO.getMain()!=null) { // 선호 동네를 설정한 경우
				String[] mains = memberVO.getMain().split(" ");
				sido = mains[0];
				if(mains.length>1) sigungu = mains[1];
				if(mains.length>2) bname = mains[2];
			}
			else { // 선호 동네를 설정하지 않은 경우
				sido = memberVO.getSido();
				sigungu = memberVO.getSigungu();
				bname = memberVO.getBname();
			}
			session.setAttribute("sido", sido);
			session.setAttribute("sigungu", sigungu);
			session.setAttribute("bname", bname);
			
			mapAjax.put("result", "success");
		}
		else { // 아이디와 비밀번호가 유효하지 않으면
			mapAjax.put("result", "invalid");
		}
		
		String ajaxData = new ObjectMapper().writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		return "/WEB-INF/views/common/ajax_view.jsp";
	}

}