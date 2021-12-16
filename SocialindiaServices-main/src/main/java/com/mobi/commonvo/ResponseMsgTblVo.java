package com.mobi.commonvo;

import java.io.Serializable;
import java.util.Date;

public class ResponseMsgTblVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer uniqId;
	private String statusCode;
	private String responseCode;
	private String message;
	private Integer status;
	private Date enrtyDatetime;
	private Date modifyDatetime;
	
	public ResponseMsgTblVo() {
		
	}

	public Integer getUniqId() {
		return uniqId;
	}

	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getEnrtyDatetime() {
		return enrtyDatetime;
	}

	public void setEnrtyDatetime(Date enrtyDatetime) {
		this.enrtyDatetime = enrtyDatetime;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}
	
}
