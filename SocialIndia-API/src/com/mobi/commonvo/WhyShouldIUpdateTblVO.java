package com.mobi.commonvo;

import java.io.Serializable;
import java.util.Date;

public class WhyShouldIUpdateTblVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer uniqId;
	private String shortDescp;
	private String descp;
	private Integer status;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public WhyShouldIUpdateTblVO() {
		
	}

	public Integer getUniqId() {
		return uniqId;
	}

	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}

	public String getShortDescp() {
		return shortDescp;
	}

	public void setShortDescp(String shortDescp) {
		this.shortDescp = shortDescp;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	

}
