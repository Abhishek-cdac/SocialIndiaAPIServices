package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.eventvo.EventTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;

public class DocumentManageTblVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int documentId;
	private MaintenanceFeeTblVO maintenanceId;
	private int docFolder;
	private Integer docSubFolder;
	private String docDateFolderName;
	private String docFileName;
	private String subject;
	private String descr;
	private int emailStatus;
	private Integer statusFlag;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo userId;
	private DoctypMasterTblVO docTypId;
	private GroupMasterTblVo usrTyp;
	private UserMasterTblVo entryBy;
	private EventTblVO eventId;
	
	
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public MaintenanceFeeTblVO getMaintenanceId() {
		return maintenanceId;
	}
	public void setMaintenanceId(MaintenanceFeeTblVO maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	public int getDocFolder() {
		return docFolder;
	}
	public void setDocFolder(int docFolder) {
		this.docFolder = docFolder;
	}
	public Integer getDocSubFolder() {
		return docSubFolder;
	}
	public void setDocSubFolder(Integer docSubFolder) {
		this.docSubFolder = docSubFolder;
	}
	public String getDocDateFolderName() {
		return docDateFolderName;
	}
	public void setDocDateFolderName(String docDateFolderName) {
		this.docDateFolderName = docDateFolderName;
	}
	public String getDocFileName() {
		return docFileName;
	}
	public void setDocFileName(String docFileName) {
		this.docFileName = docFileName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public int getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
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
	public UserMasterTblVo getUserId() {
		return userId;
	}
	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}
	public DoctypMasterTblVO getDocTypId() {
		return docTypId;
	}
	public void setDocTypId(DoctypMasterTblVO docTypId) {
		this.docTypId = docTypId;
	}
	public GroupMasterTblVo getUsrTyp() {
		return usrTyp;
	}
	public void setUsrTyp(GroupMasterTblVo usrTyp) {
		this.usrTyp = usrTyp;
	}
	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}
	public Integer getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}
	public EventTblVO getEventId() {
		return eventId;
	}
	public void setEventId(EventTblVO eventId) {
		this.eventId = eventId;
	}
	

}
