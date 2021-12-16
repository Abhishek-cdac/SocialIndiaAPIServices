package com.pack.billpaymentvo;

import java.io.Serializable;
import java.util.Date;

public class CyberplatoptrsTblVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnBILLER_ID;	
	private String ivrBnBILLER_NAME;	
	private Integer ivrBILLER_GATE_ID_CODE;
	private Integer ivrBILLER_CATEGORY;
	private String ivrBnVALIDATE_URL;
	private String ivrBnPAYMENT_URL;
	private String ivrBnSTATUS_CHECK_URL;
	private String ivrBnADDITIONAL_VALIDATION;
	private Integer ivrBnSTATUS;
	private Date ivrBnENTRY_DATETIME;
	
	public Integer getIvrBnBILLER_ID() {
		return ivrBnBILLER_ID;
	}
	public void setIvrBnBILLER_ID(Integer ivrBnBILLER_ID) {
		this.ivrBnBILLER_ID = ivrBnBILLER_ID;
	}
	public String getIvrBnBILLER_NAME() {
		return ivrBnBILLER_NAME;
	}
	public void setIvrBnBILLER_NAME(String ivrBnBILLER_NAME) {
		this.ivrBnBILLER_NAME = ivrBnBILLER_NAME;
	}
	public Integer getIvrBILLER_GATE_ID_CODE() {
		return ivrBILLER_GATE_ID_CODE;
	}
	public void setIvrBILLER_GATE_ID_CODE(Integer ivrBILLER_GATE_ID_CODE) {
		this.ivrBILLER_GATE_ID_CODE = ivrBILLER_GATE_ID_CODE;
	}
	public Integer getIvrBILLER_CATEGORY() {
		return ivrBILLER_CATEGORY;
	}
	public void setIvrBILLER_CATEGORY(Integer ivrBILLER_CATEGORY) {
		this.ivrBILLER_CATEGORY = ivrBILLER_CATEGORY;
	}
	public String getIvrBnVALIDATE_URL() {
		return ivrBnVALIDATE_URL;
	}
	public void setIvrBnVALIDATE_URL(String ivrBnVALIDATE_URL) {
		this.ivrBnVALIDATE_URL = ivrBnVALIDATE_URL;
	}
	public String getIvrBnPAYMENT_URL() {
		return ivrBnPAYMENT_URL;
	}
	public void setIvrBnPAYMENT_URL(String ivrBnPAYMENT_URL) {
		this.ivrBnPAYMENT_URL = ivrBnPAYMENT_URL;
	}
	public String getIvrBnSTATUS_CHECK_URL() {
		return ivrBnSTATUS_CHECK_URL;
	}
	public void setIvrBnSTATUS_CHECK_URL(String ivrBnSTATUS_CHECK_URL) {
		this.ivrBnSTATUS_CHECK_URL = ivrBnSTATUS_CHECK_URL;
	}
	public String getIvrBnADDITIONAL_VALIDATION() {
		return ivrBnADDITIONAL_VALIDATION;
	}
	public void setIvrBnADDITIONAL_VALIDATION(String ivrBnADDITIONAL_VALIDATION) {
		this.ivrBnADDITIONAL_VALIDATION = ivrBnADDITIONAL_VALIDATION;
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
	
	
	
}
