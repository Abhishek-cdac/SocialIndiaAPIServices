package com.pack.emailtempvo;

import java.io.Serializable;
import java.util.Date;

import com.social.email.persistense.EmailTemplateTblVo;

public class EmailParsereditTbl implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int template_auto_id;
	private EmailTemplateTblVo template_id;
	private String TEMP_NAME;
	private String TEMP_PAR;
	private String COLUMN_NAME;
	private int status;
	private Date entrydate;
	
	public int getTemplate_auto_id() {
		return template_auto_id;
	}
	public void setTemplate_auto_id(int template_auto_id) {
		this.template_auto_id = template_auto_id;
	}

	public EmailTemplateTblVo getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(EmailTemplateTblVo template_id) {
		this.template_id = template_id;
	}
	public String getTEMP_NAME() {
		return TEMP_NAME;
	}
	public void setTEMP_NAME(String TEMP_NAME) {
		TEMP_NAME = TEMP_NAME;
	}
	public String getTEMP_PAR() {
		return TEMP_PAR;
	}
	public void setTEMP_PAR(String tEMP_PAR) {
		TEMP_PAR = tEMP_PAR;
	}
	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}
	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(Date entrydate) {
		this.entrydate = entrydate;
	}
	
	
}
