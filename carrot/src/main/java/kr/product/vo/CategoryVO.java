package kr.product.vo;

public class CategoryVO {
	private int category; // 0=기타, 1=삽니다
	private String name;
	private int hidden; // 0=숨겨지지 않음, 1=숨겨짐
	private int sort; // 0=기본값, -1=최하단
	
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHidden() {
		return hidden;
	}
	public void setHidden(int hidden) {
		this.hidden = hidden;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}