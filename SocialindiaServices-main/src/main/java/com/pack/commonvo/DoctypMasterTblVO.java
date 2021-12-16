package com.pack.commonvo;

import java.io.Serializable;
import java.util.Date;

public class DoctypMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnDOC_TYP_ID;
	private String ivrBnDOC_TYP_NAME;
	private String ivrBnDESCR;
	private Integer ivrBnENTRY_BY;
	private Integer ivrBnACT_STS;
	private Date ivrBnENTRY_DATETIME;
	
	public Integer getIvrBnDOC_TYP_ID() {
		return ivrBnDOC_TYP_ID;
	}
	public void setIvrBnDOC_TYP_ID(Integer ivrBnDOC_TYP_ID) {
		this.ivrBnDOC_TYP_ID = ivrBnDOC_TYP_ID;
	}
	public String getIvrBnDOC_TYP_NAME() {
		return ivrBnDOC_TYP_NAME;
	}
	public void setIvrBnDOC_TYP_NAME(String ivrBnDOC_TYP_NAME) {
		this.ivrBnDOC_TYP_NAME = ivrBnDOC_TYP_NAME;
	}
	public String getIvrBnDESCR() {
		return ivrBnDESCR;
	}
	public void setIvrBnDESCR(String ivrBnDESCR) {
		this.ivrBnDESCR = ivrBnDESCR;
	}
	public Integer getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}
	public void setIvrBnENTRY_BY(Integer ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}
	public Integer getIvrBnACT_STS() {
		return ivrBnACT_STS;
	}
	public void setIvrBnACT_STS(Integer ivrBnACT_STS) {
		this.ivrBnACT_STS = ivrBnACT_STS;
	}
	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}
	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}
	
	
}
