package com.pack.reconcilevo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class CyberplatsetmtfileTblVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnCBPLT_FILE_ID;	
	private String ivrBnFLIENAME;	
	private String ivrBnCHECK_SUM;
	private UserMasterTblVo ivrBnENTRY_BY;
	private Integer ivrBnExctSTATUS;
	private Integer ivrBnSTATUS;
	private Date ivrBnENTRY_DATETIME;
	public Integer getIvrBnCBPLT_FILE_ID() {
		return ivrBnCBPLT_FILE_ID;
	}
	public void setIvrBnCBPLT_FILE_ID(Integer ivrBnCBPLT_FILE_ID) {
		this.ivrBnCBPLT_FILE_ID = ivrBnCBPLT_FILE_ID;
	}
	public String getIvrBnFLIENAME() {
		return ivrBnFLIENAME;
	}
	public void setIvrBnFLIENAME(String ivrBnFLIENAME) {
		this.ivrBnFLIENAME = ivrBnFLIENAME;
	}
	public String getIvrBnCHECK_SUM() {
		return ivrBnCHECK_SUM;
	}
	public void setIvrBnCHECK_SUM(String ivrBnCHECK_SUM) {
		this.ivrBnCHECK_SUM = ivrBnCHECK_SUM;
	}
	public UserMasterTblVo getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}
	public void setIvrBnENTRY_BY(UserMasterTblVo ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}
	public Integer getIvrBnSTATUS() {
		return ivrBnSTATUS;
	}
	public void setIvrBnSTATUS(Integer ivrBnSTATUS) {
		this.ivrBnSTATUS = ivrBnSTATUS;
	}
	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}
	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}
	public Integer getIvrBnExctSTATUS() {
		return ivrBnExctSTATUS;
	}
	public void setIvrBnExctSTATUS(Integer ivrBnExctSTATUS) {
		this.ivrBnExctSTATUS = ivrBnExctSTATUS;
	}
	
	
}
