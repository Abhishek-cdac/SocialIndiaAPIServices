package com.pack.passwordvo;

import java.io.Serializable;

public class ForgetpasswordTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer usrId;
	private String emailId;
	private String pswd;
	private String encryptPswd;
	private String mobileno;
	private Integer ivrBnPASSWORD_FLAG;
	private Integer ivrBnStatus;
	
	public Integer getIvrBnPASSWORD_FLAG() {
		return ivrBnPASSWORD_FLAG;
	}
	public void setIvrBnPASSWORD_FLAG(Integer ivrBnPASSWORD_FLAG) {
		this.ivrBnPASSWORD_FLAG = ivrBnPASSWORD_FLAG;
	}
	public Integer getUsrId() {
		return usrId;
	}
	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getEncryptPswd() {
		return encryptPswd;
	}
	public void setEncryptPswd(String encryptPswd) {
		this.encryptPswd = encryptPswd;
	}	
	public Integer getIvrBnStatus() {
		return ivrBnStatus;
	}	
	public void setIvrBnStatus(Integer ivrBnStatus) {
		this.ivrBnStatus = ivrBnStatus;
	}
	
}
