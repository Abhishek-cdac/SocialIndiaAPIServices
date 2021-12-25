package com.socialindiaservices.vo;

import java.io.Serializable;

public class BiometricDeviceInfoTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ID;
	private String socyteaId;
	
	private String bioDeviceLogId;
	  private String bioDeviceId;
	  private String bioUserId;
	  private String bioLogDate;
	  private String bioStatusCode;
	  private String bioDuration;
	  private String bioVerificationMode;
	  private String bioinsertdate;
	
	public BiometricDeviceInfoTblVO(){}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getSocyteaId() {
		return socyteaId;
	}

	public void setSocyteaId(String socyteaId) {
		this.socyteaId = socyteaId;
	}

	public String getBioDeviceLogId() {
		return bioDeviceLogId;
	}

	public void setBioDeviceLogId(String bioDeviceLogId) {
		this.bioDeviceLogId = bioDeviceLogId;
	}

	public String getBioDeviceId() {
		return bioDeviceId;
	}

	public void setBioDeviceId(String bioDeviceId) {
		this.bioDeviceId = bioDeviceId;
	}

	public String getBioUserId() {
		return bioUserId;
	}

	public void setBioUserId(String bioUserId) {
		this.bioUserId = bioUserId;
	}

	public String getBioLogDate() {
		return bioLogDate;
	}

	public void setBioLogDate(String bioLogDate) {
		this.bioLogDate = bioLogDate;
	}

	public String getBioStatusCode() {
		return bioStatusCode;
	}

	public void setBioStatusCode(String bioStatusCode) {
		this.bioStatusCode = bioStatusCode;
	}

	public String getBioDuration() {
		return bioDuration;
	}

	public void setBioDuration(String bioDuration) {
		this.bioDuration = bioDuration;
	}

	public String getBioVerificationMode() {
		return bioVerificationMode;
	}

	public void setBioVerificationMode(String bioVerificationMode) {
		this.bioVerificationMode = bioVerificationMode;
	}

	

	public String getBioinsertdate() {
		return bioinsertdate;
	}

	public void setBioinsertdate(String bioinsertdate) {
		this.bioinsertdate = bioinsertdate;
	}


}