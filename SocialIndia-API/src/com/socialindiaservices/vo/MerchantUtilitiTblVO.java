package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class MerchantUtilitiTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int merchantUtilId;
	private String broucherName;
	private String offerName;
	private float offerPrice;
	private float actualPrice;
	private String description;
	private Date offerValidFrom;
	private Date offerValidTo;
	private int statusFlag;
	private Date entryDatetime;
	private Date modifyDatetime;
	private MerchantTblVO mrchntId;
	private UserMasterTblVo entryBy;
	
	public MerchantUtilitiTblVO(){}

	public int getMerchantUtilId() {
		return merchantUtilId;
	}

	public void setMerchantUtilId(int merchantUtilId) {
		this.merchantUtilId = merchantUtilId;
	}

	public String getBroucherName() {
		return broucherName;
	}

	public void setBroucherName(String broucherName) {
		this.broucherName = broucherName;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public float getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(float offerPrice) {
		this.offerPrice = offerPrice;
	}

	public float getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(float actualPrice) {
		this.actualPrice = actualPrice;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getOfferValidFrom() {
		return offerValidFrom;
	}

	public void setOfferValidFrom(Date offerValidFrom) {
		this.offerValidFrom = offerValidFrom;
	}

	public Date getOfferValidTo() {
		return offerValidTo;
	}

	public void setOfferValidTo(Date offerValidTo) {
		this.offerValidTo = offerValidTo;
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

	public MerchantTblVO getMrchntId() {
		return mrchntId;
	}

	public void setMrchntId(MerchantTblVO mrchntId) {
		this.mrchntId = mrchntId;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}
	
	

}
