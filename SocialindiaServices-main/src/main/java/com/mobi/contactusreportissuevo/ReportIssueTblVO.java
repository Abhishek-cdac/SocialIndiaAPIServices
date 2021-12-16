package com.mobi.contactusreportissuevo;

import java.io.Serializable;
import java.util.Date;

public class ReportIssueTblVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer repId;
	private Integer userId;
	private Integer parentId;
	private String emailId;
	private String mobileNo;
	private String name;
	private String descr;
	private Integer reportTo;
	private Integer reportToType;
	private Integer issueContactus;	
	private Integer entryBy;
	private Integer status;
	private Date entryDatetime;
	private Date modyDatetime;

	public ReportIssueTblVO() {
	}

	public Integer getRepId() {
		return repId;
	}

	public void setRepId(Integer repId) {
		this.repId = repId;
	}
	
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getIssueContactus() {
		return issueContactus;
	}

	public void setIssueContactus(Integer issueContactus) {
		this.issueContactus = issueContactus;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getReportTo() {
		return reportTo;
	}

	public void setReportTo(Integer reportTo) {
		this.reportTo = reportTo;
	}

	public Integer getReportToType() {
		return reportToType;
	}

	public void setReportToType(Integer reportToType) {
		this.reportToType = reportToType;
	}

	public Integer getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getEntryDatetime() {
		return entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModyDatetime() {
		return modyDatetime;
	}

	public void setModyDatetime(Date modyDatetime) {
		this.modyDatetime = modyDatetime;
	}
	
	

}
