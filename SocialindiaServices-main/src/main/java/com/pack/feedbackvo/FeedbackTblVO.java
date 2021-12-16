package com.pack.feedbackvo;

import java.io.Serializable;
import java.util.Date;

import com.pack.commonvo.CategoryMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

public class FeedbackTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnFEEDBK_ID;	
	//private Integer ivrBnUSR_ID;
	private String ivrBnFEEDBK_TXT;
	private String email;
	private Integer ivrBnFEEDBK_FOR_USR_ID;
	private Integer ivrBnFEEDBK_FOR_USR_TYP;
	private Integer ivrBnRATING;
	private Integer ivrBnFEEDBK_STATUS;
	private Date ivrBnENTRY_DATETIME;
	private UserMasterTblVo ivrBnUarmsttbvoobj;
	public Integer getIvrBnFEEDBK_ID() {
		return ivrBnFEEDBK_ID;
	}
	public void setIvrBnFEEDBK_ID(Integer ivrBnFEEDBK_ID) {
		this.ivrBnFEEDBK_ID = ivrBnFEEDBK_ID;
	}
//	public Integer getIvrBnUSR_ID() {
//		return ivrBnUSR_ID;
//	}
//	public void setIvrBnUSR_ID(Integer ivrBnUSR_ID) {
//		this.ivrBnUSR_ID = ivrBnUSR_ID;
//	}
	
	public String getIvrBnFEEDBK_TXT() {
		return ivrBnFEEDBK_TXT;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setIvrBnFEEDBK_TXT(String ivrBnFEEDBK_TXT) {
		this.ivrBnFEEDBK_TXT = ivrBnFEEDBK_TXT;
	}
	public Integer getIvrBnFEEDBK_FOR_USR_ID() {
		return ivrBnFEEDBK_FOR_USR_ID;
	}
	public void setIvrBnFEEDBK_FOR_USR_ID(Integer ivrBnFEEDBK_FOR_USR_ID) {
		this.ivrBnFEEDBK_FOR_USR_ID = ivrBnFEEDBK_FOR_USR_ID;
	}
	public Integer getIvrBnFEEDBK_FOR_USR_TYP() {
		return ivrBnFEEDBK_FOR_USR_TYP;
	}
	public void setIvrBnFEEDBK_FOR_USR_TYP(Integer ivrBnFEEDBK_FOR_USR_TYP) {
		this.ivrBnFEEDBK_FOR_USR_TYP = ivrBnFEEDBK_FOR_USR_TYP;
	}
	public Integer getIvrBnRATING() {
		return ivrBnRATING;
	}
	public void setIvrBnRATING(Integer ivrBnRATING) {
		this.ivrBnRATING = ivrBnRATING;
	}
	public Integer getIvrBnFEEDBK_STATUS() {
		return ivrBnFEEDBK_STATUS;
	}
	public void setIvrBnFEEDBK_STATUS(Integer ivrBnFEEDBK_STATUS) {
		this.ivrBnFEEDBK_STATUS = ivrBnFEEDBK_STATUS;
	}
	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}
	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}
	public UserMasterTblVo getIvrBnUarmsttbvoobj() {
		return ivrBnUarmsttbvoobj;
	}
	public void setIvrBnUarmsttbvoobj(UserMasterTblVo ivrBnUarmsttbvoobj) {
		this.ivrBnUarmsttbvoobj = ivrBnUarmsttbvoobj;
	}
	
	
	
}
