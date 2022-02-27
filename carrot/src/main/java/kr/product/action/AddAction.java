package kr.product.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;
import kr.util.CommonsFileUpload;

public class AddAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer user = (Integer)request.getSession().getAttribute("user");
		if(user==null) { // 로그인되어 있지 않은 경우
			request.setAttribute("result", "로그인 후 물품을 등록할 수 있습니다!"); // 모달에 출력할 문구를 request 영역에 저장
		}
		else { // 로그인되어 있는 경우
			Map<String, String> multi = CommonsFileUpload.uploadFile(request);
			
			ProductVO productVO = new ProductVO();
			
			productVO.setMember(user);
			productVO.setPhoto1(multi.get("photo1"));
			productVO.setPhoto2(multi.get("photo2"));
			productVO.setPhoto3(multi.get("photo3"));
			productVO.setPhoto4(multi.get("photo4"));
			productVO.setPhoto5(multi.get("photo5"));
			productVO.setTitle(multi.get("title"));
			productVO.setPrice(Integer.parseInt(multi.get("price")));
			productVO.setContent(multi.get("content"));
			productVO.setCategory(Integer.parseInt(multi.get("category")));
			
			ProductDAO.getInstance().addProduct(productVO);
			
			request.setAttribute("result", "물품 등록이 완료되었습니다!"); // 모달에 출력할 문구를 request 영역에 저장
		}
		
		// JSP 경로 반환
		return "/WEB-INF/views/common/modal.jsp";
	}

}