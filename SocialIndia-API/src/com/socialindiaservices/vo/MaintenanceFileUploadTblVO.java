package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;


public class MaintenanceFileUploadTblVO {
	private int fileUpId;
	private String fileName;
	private int noofRecords;
	private float grandTotal;
	private String checksum;
	private int status;
	private Integer societyId;
	private UserMasterTblVo entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public int getFileUpId() {
		return fileUpId;
	}
	public void setFileUpId(int fileUpId) {
		this.fileUpId = fileUpId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getNoofRecords() {
		return noofRecords;
	}
	public void setNoofRecords(int noofRecords) {
		this.noofRecords = noofRecords;
	}
	public float getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(float grandTotal) {
		this.grandTotal = grandTotal;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer getSocietyId() {
		return societyId;
	}
	public void setSocietyId(Integer societyId) {
		this.societyId = societyId;
	}
	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
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
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	
	
}
