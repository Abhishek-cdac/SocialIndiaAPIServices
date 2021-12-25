package com.social.failedSignonvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class FailedSignOnTbl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String failUsername;	
	private String failPassword;
	private int status;
	private Date entryDate;
	private Date modifyDatetime;
	private UserMasterTblVo ivrBnENTRY_BY;
	private Integer failcount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFailUsername() {
		return failUsername;
	}
	public void setFailUsername(String failUsername) {
		this.failUsername = failUsername;
	}
	public String getFailPassword() {
		return failPassword;
	}
	public void setFailPassword(String failPassword) {
		this.failPassword = failPassword;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public Date getModifyDatetime() {
		return modifyDatetime;
	}
	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}
	public UserMasterTblVo getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}
	public void setIvrBnENTRY_BY(UserMasterTblVo ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}
	
	public Integer getFailcount() {
		return failcount;
	}
	
	public void setFailcount(Integer failcount) {
		this.failcount = failcount;
	}
	
	
	
	
	
}
