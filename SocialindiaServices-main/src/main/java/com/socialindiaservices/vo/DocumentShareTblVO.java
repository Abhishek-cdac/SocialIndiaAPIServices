package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class DocumentShareTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int documentShareId;
	private int status;
	private Date entryDatetime;
	private Date modifyDatetime;
	private DocumentManageTblVO documentId;
	private UserMasterTblVo userId;
	private UserMasterTblVo entryBy;
	
	public DocumentShareTblVO(){}

	public int getDocumentShareId() {
		return documentShareId;
	}

	public void setDocumentShareId(int documentShareId) {
		this.documentShareId = documentShareId;
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

	public DocumentManageTblVO getDocumentId() {
		return documentId;
	}

	public void setDocumentId(DocumentManageTblVO documentId) {
		this.documentId = documentId;
	}

	public UserMasterTblVo getUserId() {
		return userId;
	}

	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}
	
	
	

}
