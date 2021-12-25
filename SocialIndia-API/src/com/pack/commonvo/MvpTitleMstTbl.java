package com.pack.commonvo;

// default package
// Generated Sep 30, 2016 11:47:33 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.Set;

/**
 * MvpTitleMstTbl generated by hbm2java
 */
public class MvpTitleMstTbl implements java.io.Serializable {

	private Integer titleId;
	private String description;
	private Integer status;
	private int entryBy;
	private Date entryDatetime;
	private Date modyDatetime;

	public MvpTitleMstTbl() {
	}

	public MvpTitleMstTbl(int entryBy, Date modyDatetime) {
		this.entryBy = entryBy;
		this.modyDatetime = modyDatetime;
	}

	public MvpTitleMstTbl(String description, Integer status, int entryBy,
			Date entryDatetime, Date modyDatetime, Set mvpFamilymbrTbls) {
		this.description = description;
		this.status = status;
		this.entryBy = entryBy;
		this.entryDatetime = entryDatetime;
		this.modyDatetime = modyDatetime;
	}

	public Integer getTitleId() {
		return this.titleId;
	}

	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
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

	public int getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(int entryBy) {
		this.entryBy = entryBy;
	}

	public Date getEntryDatetime() {
		return this.entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModyDatetime() {
		return this.modyDatetime;
	}

	public void setModyDatetime(Date modyDatetime) {
		this.modyDatetime = modyDatetime;
	}


}
