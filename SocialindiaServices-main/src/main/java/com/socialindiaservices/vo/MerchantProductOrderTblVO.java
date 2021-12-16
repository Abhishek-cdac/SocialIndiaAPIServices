package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class MerchantProductOrderTblVO {
	private int orderId;
	private String orderComments;
	private String supplyComments;
	private int orderAcceptStatus;
	private int status;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo orderBy;
	private MerchantTblVO mrchntId;
	private String orderUrl;
	
	public MerchantProductOrderTblVO(){}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderComments() {
		return orderComments;
	}

	public void setOrderComments(String orderComments) {
		this.orderComments = orderComments;
	}

	public String getSupplyComments() {
		return supplyComments;
	}

	public void setSupplyComments(String supplyComments) {
		this.supplyComments = supplyComments;
	}
	
	public int getOrderAcceptStatus() {
		return orderAcceptStatus;
	}

	public void setOrderAcceptStatus(int orderAcceptStatus) {
		this.orderAcceptStatus = orderAcceptStatus;
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

	public UserMasterTblVo getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(UserMasterTblVo orderBy) {
		this.orderBy = orderBy;
	}

	public MerchantTblVO getMrchntId() {
		return mrchntId;
	}

	public void setMrchntId(MerchantTblVO mrchntId) {
		this.mrchntId = mrchntId;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}

}
