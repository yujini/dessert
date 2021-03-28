package com.product;

public class ProductDTO {
	private int productNum;
	private String productGroup;
	private String productGroupKor;
	private String name;
	private String fileName;
	private int price;
	private String memo;
	private int cnt;
	private String isSale;	// 판매중인지 아닌지
	private String isBest;
	
	
	
	
	public String getProductGroupKor() {
		return productGroupKor;
	}
	public void setProductGroupKor(String productGroupKor) {
		this.productGroupKor = productGroupKor;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getIsSale() {
		return isSale;
	}
	public void setIsSale(String isSale) {
		this.isSale = isSale;
	}
	public String getIsBest() {
		return isBest;
	}
	public void setIsBest(String isBest) {
		this.isBest = isBest;
	}
}
