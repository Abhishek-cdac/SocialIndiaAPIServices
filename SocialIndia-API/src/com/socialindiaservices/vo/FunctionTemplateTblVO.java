package com.socialindiaservices.vo;

import java.util.Date;

public class FunctionTemplateTblVO {
	private int functionTempId;
	private String templateText;
	private int statusFlag;
	private Date entryDatetime;
	private Date modifyDatetime;
	private FunctionMasterTblVO functionId;
	
	public FunctionTemplateTblVO(){}

	public int getFunctionTempId() {
		return functionTempId;
	}

	public void setFunctionTempId(int functionTempId) {
		this.functionTempId = functionTempId;
	}

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
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

	public FunctionMasterTblVO getFunctionId() {
		return functionId;
	}

	public void setFunctionId(FunctionMasterTblVO functionId) {
		this.functionId = functionId;
	}
	
	

}
