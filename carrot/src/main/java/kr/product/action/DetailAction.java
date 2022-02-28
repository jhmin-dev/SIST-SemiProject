package kr.product.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.vo.MemberVO;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;
import kr.product.vo.MyProductVO;
import kr.product.vo.ProductVO;
import kr.util.StringUtil;

public class DetailAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProductDAO dao = ProductDAO.getInstance();
		
		int product = Integer.parseInt(request.getParameter("product"));
		
		// 물품 상세 정보
		ProductVO productVO = dao.getProduct(product);
		MemberVO sellerVO = productVO.getMemberVO();
		CategoryVO categoryVO = productVO.getCategoryVO();
		
		// 제목에 HTML 태그를 허용하지 않음
		productVO.setTitle(StringUtil.useNoHtml(productVO.getTitle()));
		// 내용에 줄바꿈을 제외한 HTML 태그를 허용하지 않음
		productVO.setContent(StringUtil.useBrNoHtml(productVO.getContent()));
		
		request.setAttribute("productVO", productVO);
		request.setAttribute("sellerVO", sellerVO);
		request.setAttribute("categoryVO", categoryVO);

		// 관심 상품 정보
		Integer user = (Integer)request.getSession().getAttribute("user");
		if(user!=null) { // 로그인한 경우
			boolean exist = dao.existsMyProduct(product, user);
			request.setAttribute("exist", exist);
		}

		// 실시간 중고 더보기
		List<ProductVO> list = dao.getListProduct(1, 6, null, null, null, null, null);
		request.setAttribute("list", list);
		
		// JSP 경로 반환
		return "/WEB-INF/views/product/detail.jsp";
	}

}