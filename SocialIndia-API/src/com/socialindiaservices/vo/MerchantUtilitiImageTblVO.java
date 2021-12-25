package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class MerchantUtilitiImageTblVO {
	
	private int merchantImageId;
	private String imageName;
	private int statusFlag;
	private Date entryDatetime;
	private Date modifyDatetime;
	private String docDateFolderName;
	private MerchantUtilitiTblVO merchantUtilId;
	private UserMasterTblVo entryBy;
	
	public MerchantUtilitiImageTblVO(){}

	public int getMerchantImageId() {
		return merchantImageId;
	}

	public void setMerchantImageId(int merchantImageId) {
		this.merchantImageId = merchantImageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
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

	public MerchantUtilitiTblVO getMerchantUtilId() {
		return merchantUtilId;
	}

	public void setMerchantUtilId(MerchantUtilitiTblVO merchantUtilId) {
		this.merchantUtilId = merchantUtilId;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}

	public String getDocDateFolderName() {
		return docDateFolderName;
	}

	public void setDocDateFolderName(String docDateFolderName) {
		this.docDateFolderName = docDateFolderName;
	}
	
	

}
