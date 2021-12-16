package com.pack.commonvo;

import java.io.Serializable;

public class EduMstrTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iVoEDU_ID;
	private String iVoEDU_NAME;
	private Integer iVoACT_STS;
	public Integer getiVoEDU_ID() {
		return iVoEDU_ID;
	}
	public void setiVoEDU_ID(Integer iVoEDU_ID) {
		this.iVoEDU_ID = iVoEDU_ID;
	}
	public String getiVoEDU_NAME() {
		return iVoEDU_NAME;
	}
	public void setiVoEDU_NAME(String iVoEDU_NAME) {
		this.iVoEDU_NAME = iVoEDU_NAME;
	}
	public Integer getiVoACT_STS() {
		return iVoACT_STS;
	}
	public void setiVoACT_STS(Integer iVoACT_STS) {
		this.iVoACT_STS = iVoACT_STS;
	}
	
	
}
