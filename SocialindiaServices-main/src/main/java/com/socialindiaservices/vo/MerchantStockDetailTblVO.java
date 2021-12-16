package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class MerchantStockDetailTblVO {
	private Integer uniqId;
	private MerchantTblVO mrchntId;
	private MerchantCategoryTblVO mrchCategoryId;
	private Integer quantity;
	private String typeName;
	private String cuisines;
	private Float power;
	private String companyName;
	private UserMasterTblVo entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	public Integer getUniqId() {
		return uniqId;
	}
	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}
	public MerchantTblVO getMrchntId() {
		return mrchntId;
	}
	public void setMrchntId(MerchantTblVO mrchntId) {
		this.mrchntId = mrchntId;
	}
	public MerchantCategoryTblVO getMrchCategoryId() {
		return mrchCategoryId;
	}
	public void setMrchCategoryId(MerchantCategoryTblVO mrchCategoryId) {
		this.mrchCategoryId = mrchCategoryId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCuisines() {
		return cuisines;
	}
	public void setCuisines(String cuisines) {
		this.cuisines = cuisines;
	}
	public Float getPower() {
		return power;
	}
	public void setPower(Float power) {
		this.power = power;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
