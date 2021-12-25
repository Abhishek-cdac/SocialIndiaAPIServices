package com.socialindiaservices.vo;

import java.util.Date;

public class MerchantProductItemsTblVO {

	private int commentId;
	private String itemName;
	private int orderQty;
	private int supplyQty;
	private int status;
	private Date entryDatetime;
	private Date modifyDatetime;
	private MerchantProductOrderTblVO orderId;
	
	public MerchantProductItemsTblVO(){}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}

	public int getSupplyQty() {
		return supplyQty;
	}

	public void setSupplyQty(int supplyQty) {
		this.supplyQty = supplyQty;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getEntryDatetime() {
		return entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public MerchantProductOrderTblVO getOrderId() {
		return orderId;
	}

	public void setOrderId(MerchantProductOrderTblVO orderId) {
		this.orderId = orderId;
	}
	
	
}
