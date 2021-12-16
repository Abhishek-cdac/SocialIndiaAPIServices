package com.social.billmaintenanceVO;

import java.util.Date;

public class BillmaintenanceVO {
	private Integer autoid;
	private Integer refId;
	private String data;
	private Integer header;
	private Integer fooder;
	private Integer status;
	private Date entryDatetime;
	
	
	public Integer getAutoid() {
		return autoid;
	}
	public void setAutoid(Integer autoid) {
		this.autoid = autoid;
	}
	public Integer getRefId() {
		return refId;
	}
	public void setRefId(Integer refId) {
		this.refId = refId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Integer getHeader() {
		return header;
	}
	public void setHeader(Integer header) {
		this.header = header;
	}
	public Integer getFooder() {
		return fooder;
	}
	public void setFooder(Integer fooder) {
		this.fooder = fooder;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getEntryDatetime() {
		return entryDatetime;
	}
	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}
	
	

}
