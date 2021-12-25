package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

public class BiometricUserInfoTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ID;
	private String socyteaId;
	  private String bioResidentId;
	  private String bioResidentName;
	  private String bioMobileNo;
	  private String bioEmail;
	  private String bioRecordStatus;
	  private String bioLocation;
	  private String bioIsSendPushNotification;
	  private Date bioinsertdate;
	  
	  private String bioDeviceLogId;
	  private String bioDeviceId;
	  private String bioUserId;
	  private String bioLogDate;
	  private String bioStatusCode;
	  private String bioDuration;
	  private String bioVerificationMode;
	
	public BiometricUserInfoTblVO(){}

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

	public String getBioResidentId() {
		return bioResidentId;
	}

	public void setBioResidentId(String bioResidentId) {
		this.bioResidentId = bioResidentId;
	}

	public String getBioResidentName() {
		return bioResidentName;
	}

	public void setBioResidentName(String bioResidentName) {
		this.bioResidentName = bioResidentName;
	}

	public String getBioMobileNo() {
		return bioMobileNo;
	}

	public void setBioMobileNo(String bioMobileNo) {
		this.bioMobileNo = bioMobileNo;
	}

	public String getBioEmail() {
		return bioEmail;
	}

	public void setBioEmail(String bioEmail) {
		this.bioEmail = bioEmail;
	}

	public String getBioRecordStatus() {
		return bioRecordStatus;
	}

	public void setBioRecordStatus(String bioRecordStatus) {
		this.bioRecordStatus = bioRecordStatus;
	}

	public String getBioLocation() {
		return bioLocation;
	}

	public void setBioLocation(String bioLocation) {
		this.bioLocation = bioLocation;
	}

	public String getBioIsSendPushNotification() {
		return bioIsSendPushNotification;
	}

	public void setBioIsSendPushNotification(String bioIsSendPushNotification) {
		this.bioIsSendPushNotification = bioIsSendPushNotification;
	}

	public Date getBioinsertdate() {
		return bioinsertdate;
	}

	public void setBioinsertdate(Date bioinsertdate) {
		this.bioinsertdate = bioinsertdate;
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

	

	
	
}