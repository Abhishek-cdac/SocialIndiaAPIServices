package com.socialindiaservices.vo;

import java.util.Date;

public class FunctionMasterTblVO {
	private int functionId;
	private String functionName;
	private int statusFlag;
	private Date entryDatetime;
	private Date modifyDatetime;
	private Integer ivrBnobjEVENT_TYPE;

	public FunctionMasterTblVO() {
	}

	public int getFunctionId() {
		return functionId;
	}

	public Integer getIvrBnobjEVENT_TYPE() {
		return ivrBnobjEVENT_TYPE;
	}

	public void setIvrBnobjEVENT_TYPE(Integer ivrBnobjEVENT_TYPE) {
		this.ivrBnobjEVENT_TYPE = ivrBnobjEVENT_TYPE;
	}

	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
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

}
