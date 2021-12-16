package com.siservices.emailvo;

public class EmailConfTbl implements java.io.Serializable {
	private int id;
	private String usrName;
	private String passwd;
	private String hostName;
	private String portNo;
	private String frmId;
	private String protcl;
	private int fetchConf;
	private String bodycontFileIp;
	private String imgHostIpv;
	private String logfilePath;
	private String templatesPath;
	private int actStat;
	private int maxThreadAlive;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	public String getFrmId() {
		return frmId;
	}
	public void setFrmId(String frmId) {
		this.frmId = frmId;
	}
	public String getProtcl() {
		return protcl;
	}
	public void setProtcl(String protcl) {
		this.protcl = protcl;
	}
	public int getFetchConf() {
		return fetchConf;
	}
	public void setFetchConf(int fetchConf) {
		this.fetchConf = fetchConf;
	}
	public String getBodycontFileIp() {
		return bodycontFileIp;
	}
	public void setBodycontFileIp(String bodycontFileIp) {
		this.bodycontFileIp = bodycontFileIp;
	}
	public String getImgHostIpv() {
		return imgHostIpv;
	}
	public void setImgHostIpv(String imgHostIpv) {
		this.imgHostIpv = imgHostIpv;
	}
	public String getLogfilePath() {
		return logfilePath;
	}
	public void setLogfilePath(String logfilePath) {
		this.logfilePath = logfilePath;
	}
	public String getTemplatesPath() {
		return templatesPath;
	}
	public void setTemplatesPath(String templatesPath) {
		this.templatesPath = templatesPath;
	}
	public int getActStat() {
		return actStat;
	}
	public void setActStat(int actStat) {
		this.actStat = actStat;
	}
	public int getMaxThreadAlive() {
		return maxThreadAlive;
	}
	public void setMaxThreadAlive(int maxThreadAlive) {
		this.maxThreadAlive = maxThreadAlive;
	}
	
	
	
}
