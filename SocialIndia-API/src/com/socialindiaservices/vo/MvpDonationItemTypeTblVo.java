package com.socialindiaservices.vo;

import java.util.Date;

import com.pack.commonvo.SkillMasterTblVO;

public class MvpDonationItemTypeTblVo implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Integer itemtypeId;
	private String itemtypeName;
	private SkillMasterTblVO donationSubcategory;
	private String itemtImageName;
	private Integer status;
	private Integer entryBy;
	private Date entryDate;
	private Date modifyDate;
	
	public MvpDonationItemTypeTblVo() {
	}

	public Integer getItemtypeId() {
		return itemtypeId;
	}

	public void setItemtypeId(Integer itemtypeId) {
		this.itemtypeId = itemtypeId;
	}

	public String getItemtypeName() {
		return itemtypeName;
	}

	public void setItemtypeName(String itemtypeName) {
		this.itemtypeName = itemtypeName;
	}

	public SkillMasterTblVO getDonationSubcategory() {
		return donationSubcategory;
	}

	public void setDonationSubcategory(SkillMasterTblVO donationSubcategory) {
		this.donationSubcategory = donationSubcategory;
	}

	public String getItemtImageName() {
		return itemtImageName;
	}

	public void setItemtImageName(String itemtImageName) {
		this.itemtImageName = itemtImageName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
