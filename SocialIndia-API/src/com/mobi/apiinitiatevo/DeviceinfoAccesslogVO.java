package com.mobi.apiinitiatevo;

import java.io.Serializable;
import java.util.Date;

public class DeviceinfoAccesslogVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer uniqid;
	private Integer userid;
	private String devicekey;
	private String imeino;
	private String deviceid;
	private String macaddr;
	private String serialno;
	private String androidversion;
	private String displayinfo;
	private String ipaddress;
	private String latlogitude;
	private String additionalinfoone;
	private String additionalinfotwo;
	private Integer entryby;
	private Integer status;
	private Date entryDate;
	private Date modifyDate;
	
	public DeviceinfoAccesslogVO() {
		
	}

	public Integer getUniqid() {
		return uniqid;
	}

	public void setUniqid(Integer uniqid) {
		this.uniqid = uniqid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getDevicekey() {
		return devicekey;
	}

	public void setDevicekey(String devicekey) {
		this.devicekey = devicekey;
	}

	public String getImeino() {
		return imeino;
	}

	public void setImeino(String imeino) {
		this.imeino = imeino;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getMacaddr() {
		return macaddr;
	}

	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getAndroidversion() {
		return androidversion;
	}

	public void setAndroidversion(String androidversion) {
		this.androidversion = androidversion;
	}

	public String getDisplayinfo() {
		return displayinfo;
	}

	public void setDisplayinfo(String displayinfo) {
		this.displayinfo = displayinfo;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getLatlogitude() {
		return latlogitude;
	}

	public void setLatlogitude(String latlogitude) {
		this.latlogitude = latlogitude;
	}

	public String getAdditionalinfoone() {
		return additionalinfoone;
	}

	public void setAdditionalinfoone(String additionalinfoone) {
		this.additionalinfoone = additionalinfoone;
	}

	public String getAdditionalinfotwo() {
		return additionalinfotwo;
	}

	public void setAdditionalinfotwo(String additionalinfotwo) {
		this.additionalinfotwo = additionalinfotwo;
	}

	public Integer getEntryby() {
		return entryby;
	}

	public void setEntryby(Integer entryby) {
		this.entryby = entryby;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
