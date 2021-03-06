package com.socialindiaservices.vo;
// default package
// Generated Jan 5, 2017 11:25:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MvpDonationSubcategoryMstTbl generated by hbm2java
 */
public class MvpDonationSubcategoryMstTbl implements java.io.Serializable {

	private Integer subcategoryId;
	private MvpDonationCategoryMstTbl mvpDonationCategoryMstTbl;
	private String subcategoryName;
	private Integer status;
	private UserMasterTblVo entryBy;
	private Date entryDate;
	private Date modifyDate;


	public MvpDonationSubcategoryMstTbl() {
	}

	public MvpDonationSubcategoryMstTbl(Date modifyDate) {
		this.modifyDate = modifyDate;
	}



	public Integer getSubcategoryId() {
		return this.subcategoryId;
	}

	public void setSubcategoryId(Integer subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public MvpDonationCategoryMstTbl getMvpDonationCategoryMstTbl() {
		return this.mvpDonationCategoryMstTbl;
	}

	public void setMvpDonationCategoryMstTbl(
			MvpDonationCategoryMstTbl mvpDonationCategoryMstTbl) {
		this.mvpDonationCategoryMstTbl = mvpDonationCategoryMstTbl;
	}

	public String getSubcategoryName() {
		return this.subcategoryName;
	}

	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}

	public Integer getStatus() {
		return this.status;
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

	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}



}
