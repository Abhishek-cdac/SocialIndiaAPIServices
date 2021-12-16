package com.mobi.contents;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;

public class FlashNewsTblVO implements Serializable{
	
	private int newsId;
	private String newsContent;
	private int status;
	private Date expiryDate;
	private UserMasterTblVo entryBy;
	private Date entryDateTime;
	private Date modifyDateTime;
	private SocietyMstTbl societyId;
	private String newsimageName;
	private String title;
	
	public FlashNewsTblVO(){}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public String getNewsContent() {
		return newsContent;
	}
	
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
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

	public SocietyMstTbl getSocietyId() {
		return societyId;
	}

	public void setSocietyId(SocietyMstTbl societyId) {
		this.societyId = societyId;
	}

	public String getNewsimageName() {
		return newsimageName;
	}

	public void setNewsimageName(String newsimageName) {
		this.newsimageName = newsimageName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
