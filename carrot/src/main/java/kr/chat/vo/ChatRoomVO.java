package kr.chat.vo;

import kr.member.vo.MemberVO;
import kr.product.vo.ProductVO;

public class ChatRoomVO {
	private int chatroom;
	private int product;
	private int seller;
	private int buyer;
	
	// 테이블에 없지만 UI에 필요한 정보
	private ProductVO productVO; // 물품 정보
	private MemberVO sellerVO; // 판매자 정보
	private MemberVO buyerVO; // 구매자 정보
	private ChatVO chatVO; // 특정 채팅방에서 가장 최근 메시지 1건 정보
	private int unread;
	
	public int getChatroom() {
		return chatroom;
	}
	public void setChatroom(int chatroom) {
		this.chatroom = chatroom;
	}
	public int getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	public int getSeller() {
		return seller;
	}
	public void setSeller(int seller) {
		this.seller = seller;
	}
	public int getBuyer() {
		return buyer;
	}
	public void setBuyer(int buyer) {
		this.buyer = buyer;
	}
	
	// 테이블에 없지만 UI에 필요한 정보
	public ProductVO getProductVO() {
		return productVO;
	}
	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}
	public MemberVO getSellerVO() {
		return sellerVO;
	}
	public void setSellerVO(MemberVO sellerVO) {
		this.sellerVO = sellerVO;
	}
	public MemberVO getBuyerVO() {
		return buyerVO;
	}
	public void setBuyerVO(MemberVO buyerVO) {
		this.buyerVO = buyerVO;
	}
	public ChatVO getChatVO() {
		return chatVO;
	}
	public void setChatVO(ChatVO chatVO) {
		this.chatVO = chatVO;
	}
	public int getUnread() {
		return unread;
	}
	public void setUnread(int unread) {
		this.unread = unread;
	}
}