package com.socialindiaservices.vo;

import java.util.Date;


import com.siservices.signup.persistense.UserMasterTblVo;

public class GatepassEntryTblVO implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Integer entryId;
	private MvpGatepassDetailTbl passId;
	private UserMasterTblVo entryBy;

	private Integer status;
	private Date inDatetime;
	private Date outDatetime;
	private Date entryDateTime;
	private Date modifyDateTime;
	public Integer getEntryId() {
		return entryId;
	}
	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}
	public MvpGatepassDetailTbl getPassId() {
		return passId;
	}
	public void setPassId(MvpGatepassDetailTbl passId) {
		this.passId = passId;
	}
	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getInDatetime() {
		return inDatetime;
	}
	public void setInDatetime(Date inDatetime) {
		this.inDatetime = inDatetime;
	}
	public Date getOutDatetime() {
		return outDatetime;
	}
	public void setOutDatetime(Date outDatetime) {
		this.outDatetime = outDatetime;
	}
	public Date getEntryDateTime() {
		return entryDateTime;
	}
	public void setEntryDateTime(Date entryDateTime) {
		this.entryDateTime = entryDateTime;
	}
	public Date getModifyDateTime() {
		return modifyDateTime;
	}
	public void setModifyDateTime(Date modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}



}
