package kr.product.action;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;

public class AddFormAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer user = (Integer)request.getSession().getAttribute("user");
		if(user==null) { // 로그인되어 있지 않은 경우
			return "redirect:/main/main.do";
		}
		
		// 카테고리 목록
		List<CategoryVO> listCategory = ProductDAO.getInstance().getListCategory(false);
		if(listCategory==null) {
			listCategory = Collections.emptyList(); // 데이터가 없는 경우 null 대신 비어 있는 리스트를 반환
		}
		request.setAttribute("listCategory", listCategory);
		
		// JSP 경로 반환
		return "/WEB-INF/views/product/addForm.jsp";
	}

}