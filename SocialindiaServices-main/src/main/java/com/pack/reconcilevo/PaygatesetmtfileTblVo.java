package com.pack.reconcilevo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class PaygatesetmtfileTblVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnPAYGATE_SETTLE_ID;	
	private String ivrBnFLIENAME;	
	private String ivrBnCHECK_SUM;
	private UserMasterTblVo ivrBnENTRY_BY;
	private Integer ivrBnExSTATUS;
	private Integer ivrBnSTATUS;
	private Date ivrBnENTRY_DATETIME;
	
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
	public Integer getIvrBnExSTATUS() {
		return ivrBnExSTATUS;
	}
	public void setIvrBnExSTATUS(Integer ivrBnExSTATUS) {
		this.ivrBnExSTATUS = ivrBnExSTATUS;
	}
	public Integer getIvrBnPAYGATE_SETTLE_ID() {
		return ivrBnPAYGATE_SETTLE_ID;
	}
	public void setIvrBnPAYGATE_SETTLE_ID(Integer ivrBnPAYGATE_SETTLE_ID) {
		this.ivrBnPAYGATE_SETTLE_ID = ivrBnPAYGATE_SETTLE_ID;
	}
	
	
}
