package com.mobi.easypayvo;

import java.util.Date;

/**
 * MvpUtilityPayLogTbl generated by hbm2java
 */
public class MvpUtilityPayLogTbl implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer logId;
	private Integer utilityPayId;
	private String VRequestLog;
	private String VResponseLog;
	private String PRequestLog;
	private String PResponseLog;
	private String SRequestLog;
	private String SResponseLog;
	private Date entryDatetime;
	private Date modifyDatetime;

	public MvpUtilityPayLogTbl() {
	}

	public Integer getLogId() {
		return this.logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getUtilityPayId() {
		return utilityPayId;
	}

	public void setUtilityPayId(Integer utilityPayId) {
		this.utilityPayId = utilityPayId;
	}

	public String getVRequestLog() {
		return this.VRequestLog;
	}

	public void setVRequestLog(String VRequestLog) {
		this.VRequestLog = VRequestLog;
	}

	public String getVResponseLog() {
		return this.VResponseLog;
	}

	public void setVResponseLog(String VResponseLog) {
		this.VResponseLog = VResponseLog;
	}

	public String getPRequestLog() {
		return this.PRequestLog;
	}

	public void setPRequestLog(String PRequestLog) {
		this.PRequestLog = PRequestLog;
	}

	public String getPResponseLog() {
		return this.PResponseLog;
	}

	public void setPResponseLog(String PResponseLog) {
		this.PResponseLog = PResponseLog;
	}

	public String getSRequestLog() {
		return this.SRequestLog;
	}

	public void setSRequestLog(String SRequestLog) {
		this.SRequestLog = SRequestLog;
	}

	public String getSResponseLog() {
		return this.SResponseLog;
	}

	public void setSResponseLog(String SResponseLog) {
		this.SResponseLog = SResponseLog;
	}

	public Date getEntryDatetime() {
		return this.entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModifyDatetime() {
		return this.modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

}