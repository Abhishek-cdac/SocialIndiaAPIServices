package com.mobile.otpVo;

// default package
// Generated Sep 26, 2016 12:16:05 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MvpUserOtpTbl generated by hbm2java
 */
public class MvpUserOtpTbl implements java.io.Serializable {

	private Integer otpId;
	private UserMasterTblVo userId;
	private String otp;
	private Integer otpCount;
	private Integer otpValidityFlag;
	private Integer otpStatusFlag;
	private Date otpGeneratedTime;
	private Integer status;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	private String mobileNo;

	public MvpUserOtpTbl() {
	}

	public MvpUserOtpTbl(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public MvpUserOtpTbl(UserMasterTblVo userId, String otp, Integer otpCount,
			Integer otpValidityFlag, Integer otpStatusFlag,
			Date otpGeneratedTime, Integer status, Integer entryBy,
			Date entryDatetime, Date modifyDatetime) {
		this.userId = userId;
		this.otp = otp;
		this.otpCount = otpCount;
		this.otpValidityFlag = otpValidityFlag;
		this.otpStatusFlag = otpStatusFlag;
		this.otpGeneratedTime = otpGeneratedTime;
		this.status = status;
		this.entryBy = entryBy;
		this.entryDatetime = entryDatetime;
		this.modifyDatetime = modifyDatetime;
	}

	public Integer getOtpId() {
		return this.otpId;
	}

	public void setOtpId(Integer otpId) {
		this.otpId = otpId;
	}

	
	public String getOtp() {
		return this.otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Integer getOtpCount() {
		return this.otpCount;
	}

	public void setOtpCount(Integer otpCount) {
		this.otpCount = otpCount;
	}

	public Integer getOtpValidityFlag() {
		return this.otpValidityFlag;
	}

	public void setOtpValidityFlag(Integer otpValidityFlag) {
		this.otpValidityFlag = otpValidityFlag;
	}

	public Integer getOtpStatusFlag() {
		return this.otpStatusFlag;
	}

	public void setOtpStatusFlag(Integer otpStatusFlag) {
		this.otpStatusFlag = otpStatusFlag;
	}

	public Date getOtpGeneratedTime() {
		return this.otpGeneratedTime;
	}

	public void setOtpGeneratedTime(Date otpGeneratedTime) {
		this.otpGeneratedTime = otpGeneratedTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
	}

	public Date getEntryDatetime() {
		return this.entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModifyDatetime() {
		return this.modifyDatetime;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

}