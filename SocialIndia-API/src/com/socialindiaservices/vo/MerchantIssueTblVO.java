package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.uam.persistense.GroupMasterTblVo;

public class MerchantIssueTblVO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer issueId;
	private int mcrctLaborId;
	private String description;
	private Integer status;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	GroupMasterTblVo ivrGrpcodeobj;

	
	public int getMcrctLaborId() {
		return mcrctLaborId;
	}

	public void setMcrctLaborId(int mcrctLaborId) {
		this.mcrctLaborId = mcrctLaborId;
	}

	public Integer getIssueId() {
		return this.issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public GroupMasterTblVo getIvrGrpcodeobj() {
		return ivrGrpcodeobj;
	}

	public void setIvrGrpcodeobj(GroupMasterTblVo ivrGrpcodeobj) {
		this.ivrGrpcodeobj = ivrGrpcodeobj;
	}

}
