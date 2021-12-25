package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class NotificationStatusTblVO {
	
	private int notificationId;
	private String mobileNo;
	private int notificationFlag;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo entryBy;
	
	public NotificationStatusTblVO(){}

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getNotificationFlag() {
		return notificationFlag;
	}

	public void setNotificationFlag(int notificationFlag) {
		this.notificationFlag = notificationFlag;
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

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}
	
	

}
