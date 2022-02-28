package kr.product.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.vo.MemberVO;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;
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
		HttpSession session = request.getSession();
		Integer user = (Integer)session.getAttribute("user");
		if(user!=null) { // 로그인한 경우
			boolean exist = dao.existsMyProduct(product, user);
			request.setAttribute("exist", exist);
		}

		// 실시간 중고 더보기
		List<ProductVO> list = dao.getListProduct(1, 7, null, null, (String)session.getAttribute("sido"), (String)session.getAttribute("sigungu"), null); // (로그인한 경우에는 시/군/구 수준까지 검색하여) 최신 7건의 목록 가져오기
		int self = -1;
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getProduct()==product) self = i;
		}
		if(self>-1) list.remove(self); // 자기 자신이 목록에 포함되어 있으면 자기 자신을 제외하고
		else list.remove(list.size()-1); // 자기 자신이 목록에 포함되어 있지 않으면 가장 오래된 물품을 제외하기
		request.setAttribute("list", list);
		
		// JSP 경로 반환
		return "/WEB-INF/views/product/detail.jsp";
	}

}