package com.pack.residentvo;

import java.util.Date;
import java.util.Set;

/**
 * UsrUpldfrmExceTbl generated by hbm2java
 */
public class UsrUpldfrmExceTbl implements java.io.Serializable {

	private Integer fileId;
	private Integer usrId;
	private String fileName;
	private String folderName;
	private Integer status;
	private Integer enrtyBy;
	private Date entryDate;
	private Date modifyDate;
	

	public UsrUpldfrmExceTbl() {
	}

	public UsrUpldfrmExceTbl(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public UsrUpldfrmExceTbl(Integer usrId, String fileName, String folderName,
			Integer status, Integer enrtyBy, Date entryDate, Date modifyDate,
			Set usrUpldfrmExceFailedTbls) {
		this.usrId = usrId;
		this.fileName = fileName;
		this.folderName = folderName;
		this.status = status;
		this.enrtyBy = enrtyBy;
		this.entryDate = entryDate;
		this.modifyDate = modifyDate;
		
	}

	public Integer getFileId() {
		return this.fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getUsrId() {
		return this.usrId;
	}

	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEnrtyBy() {
		return this.enrtyBy;
	}

	public void setEnrtyBy(Integer enrtyBy) {
		this.enrtyBy = enrtyBy;
	}

	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	

}
