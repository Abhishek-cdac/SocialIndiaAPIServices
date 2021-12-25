package com.siservices.SMSConfigVO;

public class SMSConfTbl implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int sno;
	private String usrName;
	private String passwd;
	private String httpurl;
	private String sender;
	private String cdmasender;
	private int actflg;
	private String proname;
	private int fetchrow;
	private int blklmtfechrow;
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getHttpurl() {
		return httpurl;
	}
	public void setHttpurl(String httpurl) {
		this.httpurl = httpurl;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getCdmasender() {
		return cdmasender;
	}
	public void setCdmasender(String cdmasender) {
		this.cdmasender = cdmasender;
	}

	public String getProname() {
		return proname;
	}
	public void setProname(String proname) {
		this.proname = proname;
	}
	public int getActflg() {
		return actflg;
	}
	public void setActflg(int actflg) {
		this.actflg = actflg;
	}
	public int getFetchrow() {
		return fetchrow;
	}
	public void setFetchrow(int fetchrow) {
		this.fetchrow = fetchrow;
	}
	public int getBlklmtfechrow() {
		return blklmtfechrow;
	}
	public void setBlklmtfechrow(int blklmtfechrow) {
		this.blklmtfechrow = blklmtfechrow;
	}

	
		
	
}
