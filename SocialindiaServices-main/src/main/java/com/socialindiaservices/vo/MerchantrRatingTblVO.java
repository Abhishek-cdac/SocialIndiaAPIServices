package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class MerchantrRatingTblVO {
	
	private int ratingId;
	private String mrchRating;
	private String comments;
	private String emailId;
	private int status;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo ratedBy;
	private MerchantTblVO mrchntId;
	private UserMasterTblVo entryBy;
	
	public MerchantrRatingTblVO(){}

	public int getRatingId() {
		return ratingId;
	}

	public void setRatingId(int ratingId) {
		this.ratingId = ratingId;
	}

	public String getMrchRating() {
		return mrchRating;
	}

	public void setMrchRating(String mrchRating) {
		this.mrchRating = mrchRating;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public UserMasterTblVo getRatedBy() {
		return ratedBy;
	}

	public void setRatedBy(UserMasterTblVo ratedBy) {
		this.ratedBy = ratedBy;
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
