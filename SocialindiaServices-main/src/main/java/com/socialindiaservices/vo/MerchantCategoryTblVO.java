package com.socialindiaservices.vo;

// default package
// Generated Aug 17, 2016 11:27:34 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MerchantCategoryTblVO generated by hbm2java
 */
public class MerchantCategoryTblVO implements java.io.Serializable {

	private Integer mrchCategoryId;
	private String mrchCategoryName;
	private Integer status;
	private UserMasterTblVo entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	private String mrchCategoryDesc;
	private String mrchCategoryImage;
	public MerchantCategoryTblVO() {
	}

	
	public String getMrchCategoryDesc() {
		return mrchCategoryDesc;
	}


	public void setMrchCategoryDesc(String mrchCategoryDesc) {
		this.mrchCategoryDesc = mrchCategoryDesc;
	}


	public String getMrchCategoryImage() {
		return mrchCategoryImage;
	}


	public void setMrchCategoryImage(String mrchCategoryImage) {
		this.mrchCategoryImage = mrchCategoryImage;
	}


	public Integer getMrchCategoryId() {
		return mrchCategoryId;
	}

	public void setMrchCategoryId(Integer mrchCategoryId) {
		this.mrchCategoryId = mrchCategoryId;
	}

	public String getMrchCategoryName() {
		return mrchCategoryName;
	}

	public void setMrchCategoryName(String mrchCategoryName) {
		this.mrchCategoryName = mrchCategoryName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
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



}