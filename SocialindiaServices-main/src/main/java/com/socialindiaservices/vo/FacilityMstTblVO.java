package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class FacilityMstTblVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int facilityId;
	private String facilityName;
	private String place;
	private String description; 
	private String facilityImg;
	private int statusFlag;
	private String societyKey;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo entryBy;
	private UserMasterTblVo updatedBy;
	
	public FacilityMstTblVO(){}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}		

	public String getFacilityImg() {
		return facilityImg;
	}

	public void setFacilityImg(String facilityImg) {
		this.facilityImg = facilityImg;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}
	
	public String getSocietyKey() {
		return societyKey;
	}

	public void setSocietyKey(String societyKey) {
		this.societyKey = societyKey;
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

	public UserMasterTblVo getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserMasterTblVo updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}
