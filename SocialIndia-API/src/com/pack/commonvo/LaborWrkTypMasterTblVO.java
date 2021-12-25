package com.pack.commonvo;

import java.io.Serializable;

public class LaborWrkTypMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer wrktypId;
	private String IVOlbrWORK_TYPE;
	private Integer status;
	private Integer ivrEntryby;
	
	public Integer getWrktypId() {
		return wrktypId;
	}
	public void setWrktypId(Integer wrktypId) {
		this.wrktypId = wrktypId;
	}
	public String getIVOlbrWORK_TYPE() {
		return IVOlbrWORK_TYPE;
	}
	public void setIVOlbrWORK_TYPE(String iVOlbrWORK_TYPE) {
		IVOlbrWORK_TYPE = iVOlbrWORK_TYPE;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}	
	public Integer getIvrEntryby() {
		return ivrEntryby;
	}	
	public void setIvrEntryby(Integer ivrEntryby) {
		this.ivrEntryby = ivrEntryby;
	}
	
	
}
