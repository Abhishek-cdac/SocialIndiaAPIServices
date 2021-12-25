package com.pack.commonvo;

import java.io.Serializable;

public class KnownusTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iVOKNOWN_US_ID;
	private String iVOKNOWNUS;
	private Integer iVOACT_STS;
	public Integer getiVOKNOWN_US_ID() {
		return iVOKNOWN_US_ID;
	}
	public void setiVOKNOWN_US_ID(Integer iVOKNOWN_US_ID) {
		this.iVOKNOWN_US_ID = iVOKNOWN_US_ID;
	}
	public String getiVOKNOWNUS() {
		return iVOKNOWNUS;
	}
	public void setiVOKNOWNUS(String iVOKNOWNUS) {
		this.iVOKNOWNUS = iVOKNOWNUS;
	}
	public Integer getiVOACT_STS() {
		return iVOACT_STS;
	}
	public void setiVOACT_STS(Integer iVOACT_STS) {
		this.iVOACT_STS = iVOACT_STS;
	}
		
}
