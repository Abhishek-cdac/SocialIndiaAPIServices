package com.pack.commonvo;

import java.io.Serializable;

public class IDCardMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iVOcradid;
	private String iVOcradname;
	private Integer iVOcardstatus;
	public Integer getiVOcradid() {
		return iVOcradid;
	}
	public void setiVOcradid(Integer iVOcradid) {
		this.iVOcradid = iVOcradid;
	}
	public String getiVOcradname() {
		return iVOcradname;
	}
	public void setiVOcradname(String iVOcradname) {
		this.iVOcradname = iVOcradname;
	}
	public Integer getiVOcardstatus() {
		return iVOcardstatus;
	}
	public void setiVOcardstatus(Integer iVOcardstatus) {
		this.iVOcardstatus = iVOcardstatus;
	}
	
	
}
