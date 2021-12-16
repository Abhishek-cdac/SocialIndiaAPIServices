package com.socialindiaservices.vo;

// default package
// Generated Sep 17, 2016 5:02:57 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * DisputeRiseTbl generated by hbm2java
 */
public class DisputeRiseTbl implements java.io.Serializable {

	private Integer disputeId;
	private UserMasterTblVo usrRegTbl;
	private String disputeTitle;
	private String disputeDesc;
	private Integer disputeT0Id;
	private Integer disputeT0Groupid;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;
	private String disputeclosereason;
	private String comments;
	public DisputeRiseTbl() {
	}

	public DisputeRiseTbl(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public DisputeRiseTbl(UserMasterTblVo usrRegTbl, String disputeTitle,
			String disputeDesc, Integer disputeT0Id, Integer disputeT0Groupid,
			Integer status, Date entryDatetime, Date modifyDatetime) {
		this.usrRegTbl = usrRegTbl;
		this.disputeTitle = disputeTitle;
		this.disputeDesc = disputeDesc;
		this.disputeT0Id = disputeT0Id;
		this.disputeT0Groupid = disputeT0Groupid;
		this.status = status;
		this.entryDatetime = entryDatetime;
		this.modifyDatetime = modifyDatetime;
	}

	public Integer getDisputeId() {
		return this.disputeId;
	}

	public void setDisputeId(Integer disputeId) {
		this.disputeId = disputeId;
	}

	
	public UserMasterTblVo getUsrRegTbl() {
		return usrRegTbl;
	}

	public void setUsrRegTbl(UserMasterTblVo usrRegTbl) {
		this.usrRegTbl = usrRegTbl;
	}

	public String getDisputeTitle() {
		return this.disputeTitle;
	}

	public void setDisputeTitle(String disputeTitle) {
		this.disputeTitle = disputeTitle;
	}

	public String getDisputeDesc() {
		return this.disputeDesc;
	}

	public void setDisputeDesc(String disputeDesc) {
		this.disputeDesc = disputeDesc;
	}

	public Integer getDisputeT0Id() {
		return this.disputeT0Id;
	}

	public void setDisputeT0Id(Integer disputeT0Id) {
		this.disputeT0Id = disputeT0Id;
	}

	public Integer getDisputeT0Groupid() {
		return this.disputeT0Groupid;
	}

	public void setDisputeT0Groupid(Integer disputeT0Groupid) {
		this.disputeT0Groupid = disputeT0Groupid;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getDisputeclosereason() {
		return disputeclosereason;
	}

	public void setDisputeclosereason(String disputeclosereason) {
		this.disputeclosereason = disputeclosereason;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	

}
