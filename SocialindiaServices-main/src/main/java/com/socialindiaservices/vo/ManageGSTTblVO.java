package com.socialindiaservices.vo;

import java.io.Serializable;

public class ManageGSTTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ID;
	private String gstNum;
	private String minGstAmt;
	private String minAmt;
	
	
	public ManageGSTTblVO(){}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getGstNum() {
		return gstNum;
	}

	public void setGstNum(String gstNum) {
		this.gstNum = gstNum;
	}

	public String getMinGstAmt() {
		return minGstAmt;
	}

	public void setMinGstAmt(String minGstAmt) {
		this.minGstAmt = minGstAmt;
	}

	public String getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(String minAmt) {
		this.minAmt = minAmt;
	}


}