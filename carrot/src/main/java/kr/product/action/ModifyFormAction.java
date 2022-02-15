package kr.product.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.product.dao.ProductDAO;

public class ModifyFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer amember_num = (Integer)session.getAttribute("user_num");
		if(amember_num == null) {//로그인 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		/*Integer buyer_num = (Integer)session.getAttribute("user_num");
		if(buyer_num == null) { //구매자로 로그인
			return "redirect:/main/main.do";
		}
		*/
		
		int aproduct_num = Integer.parseInt(request.getParameter("aproduct_num"));
		
		ProductDAO dao = ProductDAO.getInstance();
		List<Object> list = dao.getProduct(aproduct_num);
		
		request.setAttribute("list", list);
		
		return "/WEB-INF/views/product/modifyForm.jsp";
	}

}

	

