package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class NotificationTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int notificationId;
	private String uniqueId;
	private int readStatus;
	private int sentStatus;
	private int statusFlag;
	private String descr;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo userId;
	private UserMasterTblVo entryBy;
	private Integer tblrefFlag;
	private Integer tblrefFlagType;
	private Integer tblrefID;
	private String additionalData;
	
	public NotificationTblVO(){}

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}

	public int getSentStatus() {
		return sentStatus;
	}

	public void setSentStatus(int sentStatus) {
		this.sentStatus = sentStatus;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
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

	public UserMasterTblVo getUserId() {
		return userId;
	}

	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}

	public Integer getTblrefFlag() {
		return tblrefFlag;
	}

	public void setTblrefFlag(Integer tblrefFlag) {
		this.tblrefFlag = tblrefFlag;
	}

	public Integer getTblrefFlagType() {
		return tblrefFlagType;
	}

	public void setTblrefFlagType(Integer tblrefFlagType) {
		this.tblrefFlagType = tblrefFlagType;
	}

	public Integer getTblrefID() {
		return tblrefID;
	}

	public void setTblrefID(Integer tblrefID) {
		this.tblrefID = tblrefID;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	

}
