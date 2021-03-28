package com.incoming;

public class IncomingDTO {
	int incomingNum;
	int productNum;
	String productGroup; 	// 상품분류
	String productGroupKor; // 상품분류 한국어
	String name;	// 상품명
	String iDate;
	int cnt;
	
	public String getProductGroupKor() {
		return productGroupKor;
	}
	public void setProductGroupKor(String productGroupKor) {
		this.productGroupKor = productGroupKor;
	}
	public int getIncomingNum() {
		return incomingNum;
	}
	public void setIncomingNum(int incomingNum) {
		this.incomingNum = incomingNum;
	}
	public int getProductNum() {
		return productNum;
	}
	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}
	public String getProductGroup() {
		return productGroup;
	}
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getiDate() {
		return iDate;
	}
	public void setiDate(String iDate) {
		this.iDate = iDate;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}	
}
