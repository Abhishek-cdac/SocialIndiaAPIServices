package com.pack.enewsvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;

public class EeNewsTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnENEWS_ID;	
	private String ivrBnTITLE;	
	private String ivrBnDESCRIPTION;
	private String ivrBnSHRTDESC;
	private Integer ivrBnSTATUS;	
	private Integer ivrBnENTRY_BY;	
	private Date ivrBnENTRY_DATETIME;	
	private UserMasterTblVo ivrBnUAMObj;
	private GroupMasterTblVo ivrBnGRPObj;
	public Integer getIvrBnENEWS_ID() {
		return ivrBnENEWS_ID;
	}
	public void setIvrBnENEWS_ID(Integer ivrBnENEWS_ID) {
		this.ivrBnENEWS_ID = ivrBnENEWS_ID;
	}
	public String getIvrBnTITLE() {
		return ivrBnTITLE;
	}
	
	public String getIvrBnSHRTDESC() {
		return ivrBnSHRTDESC;
	}
	public void setIvrBnSHRTDESC(String ivrBnSHRTDESC) {
		this.ivrBnSHRTDESC = ivrBnSHRTDESC;
	}
	public void setIvrBnTITLE(String ivrBnTITLE) {
		this.ivrBnTITLE = ivrBnTITLE;
	}
	public String getIvrBnDESCRIPTION() {
		return ivrBnDESCRIPTION;
	}
	public void setIvrBnDESCRIPTION(String ivrBnDESCRIPTION) {
		this.ivrBnDESCRIPTION = ivrBnDESCRIPTION;
	}
	
	public Integer getIvrBnSTATUS() {
		return ivrBnSTATUS;
	}
	public void setIvrBnSTATUS(Integer ivrBnSTATUS) {
		this.ivrBnSTATUS = ivrBnSTATUS;
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
	public UserMasterTblVo getIvrBnUAMObj() {
		return ivrBnUAMObj;
	}
	public void setIvrBnUAMObj(UserMasterTblVo ivrBnUAMObj) {
		this.ivrBnUAMObj = ivrBnUAMObj;
	}
	public GroupMasterTblVo getIvrBnGRPObj() {
		return ivrBnGRPObj;
	}
	public void setIvrBnGRPObj(GroupMasterTblVo ivrBnGRPObj) {
		this.ivrBnGRPObj = ivrBnGRPObj;
	}
	
	
}
