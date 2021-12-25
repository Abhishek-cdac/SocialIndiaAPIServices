package com.pack.residentvo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;


public class UsrUpldfrmExceFailedTbl implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer uniqueId;
	private UsrUpldfrmExceTbl fileId;
	private UserMasterTblVo usrId;
	private String excelErrRowId;
	private Integer status;
	private Integer enrtyBy;
	private Date entryDate;
	private Date modifyDate;

	public UsrUpldfrmExceFailedTbl() {
	}

	public UsrUpldfrmExceFailedTbl(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public UsrUpldfrmExceFailedTbl(UsrUpldfrmExceTbl usrUpldfrmExceTbl,
			UserMasterTblVo usrId, String excelErrRowId, Integer status,
			Integer enrtyBy, Date entryDate, Date modifyDate) {
		this.fileId = usrUpldfrmExceTbl;
		this.usrId = usrId;
		this.excelErrRowId = excelErrRowId;
		this.status = status;
		this.enrtyBy = enrtyBy;
		this.entryDate = entryDate;
		this.modifyDate = modifyDate;
	}

	public Integer getUniqueId() {
		return this.uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	
	public UsrUpldfrmExceTbl getFileId() {
		return fileId;
	}

	public void setFileId(UsrUpldfrmExceTbl fileId) {
		this.fileId = fileId;
	}

	

	public UserMasterTblVo getUsrId() {
		return usrId;
	}

	public void setUsrId(UserMasterTblVo usrId) {
		this.usrId = usrId;
	}

	public String getExcelErrRowId() {
		return this.excelErrRowId;
	}

	public void setExcelErrRowId(String excelErrRowId) {
		this.excelErrRowId = excelErrRowId;
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
