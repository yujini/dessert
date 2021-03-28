package com.reservation;

import com.product.ProductDTO;

public class ReservationDTO {
	int listNum;
	int reservationNum;
	int memberNum;
	String memName;
	String memTel;
	String isPaid;
	String isDiscounted;
	String isLowTemperature;
	String orderDate;
	String reservationDate;
	String reservationHour;
	String reservationMin;
	String pickupDate;
	int cnt; 	// 총 구매 수량 - 예약  list 에서 사용
	int price;	// 총 구매 가격
	String memo;
	ProductDTO product; // 대표 상품


	public int getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int getReservationNum() {
		return reservationNum;
	}
	public void setReservationNum(int reservationNum) {
		this.reservationNum = reservationNum;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemTel() {
		return memTel;
	}
	public void setMemTel(String memTel) {
		this.memTel = memTel;
	}
	public String getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}
	public String getIsDiscounted() {
		return isDiscounted;
	}
	public void setIsDiscounted(String isDiscounted) {
		this.isDiscounted = isDiscounted;
	}
	public String getIsLowTemperature() {
		return isLowTemperature;
	}
	public void setIsLowTemperature(String isLowTemperature) {
		this.isLowTemperature = isLowTemperature;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}
	public String getReservationHour() {
		return reservationHour;
	}
	public void setReservationHour(String reservationHour) {
		this.reservationHour = reservationHour;
	}
	public String getReservationMin() {
		return reservationMin;
	}
	public void setReservationMin(String reservationMin) {
		this.reservationMin = reservationMin;
	}
	public String getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
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
	public ProductDTO getProduct() {
		return product;
	}
	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	
}
