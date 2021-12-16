package com.pack.accessvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class AccessDetailsVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	/*private Integer usrId;*/
	private UserMasterTblVo usrId;
	private String ipaddress;
	private Integer accessCount;
	private String hostName;
	private Integer portNo;
	private String protocolType;
	private String methodType;
	private String countryName;
	private String language;
	private Date client;
	private Date gmtTime;
	private Date entryDate;
	private Date modifyDate;
	
	public AccessDetailsVO() {
	  }
	 
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	/*public Integer getUsrId() {
		return this.usrId;
	}

	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}*/

	public UserMasterTblVo getUsrId() {
		return usrId;
	}

	public void setUsrId(UserMasterTblVo usrId) {
		this.usrId = usrId;
	}

	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Integer getAccessCount() {
		return this.accessCount;
	}

	public void setAccessCount(Integer accessCount) {
		this.accessCount = accessCount;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getPortNo() {
		return this.portNo;
	}

	public void setPortNo(Integer portNo) {
		this.portNo = portNo;
	}

	public String getProtocolType() {
		return this.protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getMethodType() {
		return this.methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	public Date getClient() {
		return this.client;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setClient(Date client) {
		this.client = client;
	}

	public Date getGmtTime() {
		return this.gmtTime;
	}

	public void setGmtTime(Date gmtTime) {
		this.gmtTime = gmtTime;
	}

	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	
}
