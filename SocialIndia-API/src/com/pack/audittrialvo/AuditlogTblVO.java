package com.pack.audittrialvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class AuditlogTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnAUDIT_ID;
	private String ivrBnOPERATIONS;
	private String ivrBnLOG_CODE;
	private Integer ivrBnENTRY_BY;
	private Date ivrBnENTRY_DATETIME;
	private Date ivrBnMODIFY_DATETIME;
	private UserMasterTblVo ivrBnUserMstrTblobj;
	
	public Integer getIvrBnAUDIT_ID() {
		return ivrBnAUDIT_ID;
	}
	public void setIvrBnAUDIT_ID(Integer ivrBnAUDIT_ID) {
		this.ivrBnAUDIT_ID = ivrBnAUDIT_ID;
	}
	public String getIvrBnOPERATIONS() {
		return ivrBnOPERATIONS;
	}
	public void setIvrBnOPERATIONS(String ivrBnOPERATIONS) {
		this.ivrBnOPERATIONS = ivrBnOPERATIONS;
	}
	public String getIvrBnLOG_CODE() {
		return ivrBnLOG_CODE;
	}
	public void setIvrBnLOG_CODE(String ivrBnLOG_CODE) {
		this.ivrBnLOG_CODE = ivrBnLOG_CODE;
	}
	public Integer getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}
	public void setIvrBnENTRY_BY(Integer ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}
	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}
	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}	
	public UserMasterTblVo getIvrBnUserMstrTblobj() {
		return ivrBnUserMstrTblobj;
	}
	public void setIvrBnUserMstrTblobj(UserMasterTblVo ivrBnUserMstrTblobj) {
		this.ivrBnUserMstrTblobj = ivrBnUserMstrTblobj;
	}
	public Date getIvrBnMODIFY_DATETIME() {
		return ivrBnMODIFY_DATETIME;
	}
	public void setIvrBnMODIFY_DATETIME(Date ivrBnMODIFY_DATETIME) {
		this.ivrBnMODIFY_DATETIME = ivrBnMODIFY_DATETIME;
	}
	
	
	
}
