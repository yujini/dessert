package com.sale;

public class SaleDTO {
	int listNum;
	int saleNum;
	String saleDate;
	String division;
	int productNum;
	String name; 			// ��ǰ��
	String productGroup; 	// ��ǰ�з�
	String productGroupKor; // ��ǰ�з� �ѱ���
	int productCnt;			// ������
	int saleCnt;			// �Ǹż���
	int price;				// �ݾ� - �Ǹų��� ����Ʈ 
	
	public int getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public int getProductNum() {
		return productNum;
	}
	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductGroup() {
		return productGroup;
	}
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}
	public String getProductGroupKor() {
		return productGroupKor;
	}
	public void setProductGroupKor(String productGroupKor) {
		this.productGroupKor = productGroupKor;
	}
	public int getProductCnt() {
		return productCnt;
	}
	public void setProductCnt(int productCnt) {
		this.productCnt = productCnt;
	}
	public int getSaleCnt() {
		return saleCnt;
	}
	public void setSaleCnt(int saleCnt) {
		this.saleCnt = saleCnt;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
