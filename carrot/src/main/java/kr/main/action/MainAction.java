package kr.main.action;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.AddressVO;
import kr.product.vo.CategoryVO;
import kr.product.vo.ProductVO;
import kr.util.PagingUtil;

public class MainAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		ProductDAO dao = ProductDAO.getInstance();

		// 동네 목록
		List<AddressVO> listAddress = dao.getListAddress();
		if(listAddress==null) {
			listAddress = Collections.emptyList(); // 데이터가 없는 경우 null 대신 비어 있는 리스트를 반환
		}
		request.setAttribute("listAddress", listAddress);

		// 카테고리 목록
		List<CategoryVO> listCategory = ProductDAO.getInstance().getListCategory(false);
		if(listCategory==null) {
			listCategory = Collections.emptyList(); // 데이터가 없는 경우 null 대신 비어 있는 리스트를 반환
		}
		request.setAttribute("listCategory", listCategory);
		
		// 검색 처리
		String category = request.getParameter("category");
		String keyword = request.getParameter("keyword");
		String sido = request.getParameter("sido");
		String sigungu = request.getParameter("sigungu");
		String bname = request.getParameter("bname");
		
		// 로그인한 사용자의 동네 필터링
		if(request.getParameter("sido")==null) { // 동네로 검색하지 않았을 때
			if(request.getSession().getAttribute("main")!=null) { // 선호 동네를 설정한 경우
				String[] mains = ((String)request.getSession().getAttribute("main")).split(" ");
				sido = mains[0];
				if(mains.length>1) sigungu = mains[1];
				if(mains.length>2) bname = mains[2];
			}
			else { // 선호 동네를 설정하지 않은 경우
				sido = (String)request.getSession().getAttribute("sido");
				sigungu = (String)request.getSession().getAttribute("sigungu");
				bname = (String)request.getSession().getAttribute("bname");
			}
			
			// 로그인한 사용자의 선호 동네 혹은 거주 동네로 설정된 법정동/리에 물품이 없으면 시/군/구 단위로 검색하도록 처리
			if(bname!=null && dao.getCountProduct(category, keyword, sido, sigungu, bname)==0) bname = null;
		}
		
		// 페이지 처리
		int count = dao.getCountProduct(category, keyword, sido, sigungu, bname);
		String pageNum = request.getParameter("pageNum");
		if(pageNum==null) pageNum = "1";
		PagingUtil page = new PagingUtil(category, keyword, Integer.parseInt(pageNum), count, 10, 5, "main.do");
		
		// 물품 목록 불러오기
		List<ProductVO> list = null;
		if(count>0) {
			list = dao.getListProduct(page.getStartCount(), page.getEndCount(), category, keyword, sido, sigungu, bname);
		}
		else {
			list = Collections.emptyList(); // 데이터가 없는 경우 null 대신 비어 있는 리스트를 반환
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("pagingHtml", page.getPagingHtml());
		
		//JSP 경로 반환
		return "/WEB-INF/views/main/main.jsp";
	}

}