package com.mobile.otpVo;

// default package
// Generated Sep 24, 2016 10:39:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * MvpSystemParameterTbl generated by hbm2java
 */
public class MvpSystemParameterTbl implements java.io.Serializable {

	private Integer uniqId;
	private String key;
	private String value;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;

	public MvpSystemParameterTbl() {
	}

	public MvpSystemParameterTbl(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public MvpSystemParameterTbl(String key, String value, Integer status,
			Date entryDatetime, Date modifyDatetime) {
		this.key = key;
		this.value = value;
		this.status = status;
		this.entryDatetime = entryDatetime;
		this.modifyDatetime = modifyDatetime;
	}

	public Integer getUniqId() {
		return this.uniqId;
	}

	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
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

}