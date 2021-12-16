package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class MerchantIssuePostingTblVO {
	private int issueId;
	private String issueTypes;
	private String comments;
	private String emailId;
	private int status;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo issueRaisedBy;
	private Integer mrchntId;
	private UserMasterTblVo entryBy;
	private int grp_code;

	public MerchantIssuePostingTblVO(){}

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}

	public String getIssueTypes() {
		return issueTypes;
	}

	public void setIssueTypes(String issueTypes) {
		this.issueTypes = issueTypes;
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

	public UserMasterTblVo getIssueRaisedBy() {
		return issueRaisedBy;
	}

	public void setIssueRaisedBy(UserMasterTblVo issueRaisedBy) {
		this.issueRaisedBy = issueRaisedBy;
	}
	public Integer getMrchntId() {
		return mrchntId;
	}

	public void setMrchntId(Integer mrchntId) {
		this.mrchntId = mrchntId;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}

	public int getGrp_code() {
		return grp_code;
	}

	public void setGrp_code(int grp_code) {
		this.grp_code = grp_code;
	}



}
