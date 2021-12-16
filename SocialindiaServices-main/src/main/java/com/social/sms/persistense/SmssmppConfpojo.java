package com.social.sms.persistense;

import java.io.Serializable;

/**
 * SmssmppConfpojo.
 * 
 * @author PL0038.
 *
 */
public class SmssmppConfpojo implements Serializable {
  private static final long serialVersionUID = 1L;
  private int serno;
  private String httpUrl;
  private String userName;
  private String senderName;
  private String cdmaSender;
  private String providerName;
  private String systemid;
  private String password;
  private String serverip;
  private Integer port;
  private String systemtype;
  private String sourceaddress;
  private String servietype;
  private int ton;
  private int npi;
  private String sdt;
  private int pid;
  private int rdy;
  private String pushbindtype;
  private String pullbindtype;
  private int configtype;
  private int actflag;
  private int fetchcount;

  public int getSerno() {
    return serno;
  }

  public void setSerno(int serno) {
    this.serno = serno;
  }

  public String getSystemid() {
    return systemid;
  }

  public void setSystemid(String systemid) {
    this.systemid = systemid;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getServerip() {
    return serverip;
  }

  public void setServerip(String serverip) {
    this.serverip = serverip;
  }

  public String getSystemtype() {
    return systemtype;
  }

  public void setSystemtype(String systemtype) {
    this.systemtype = systemtype;
  }

  public String getSourceaddress() {
    return sourceaddress;
  }

  public void setSourceaddress(String sourceaddress) {
    this.sourceaddress = sourceaddress;
  }

  public String getServietype() {
    return servietype;
  }

  public void setServietype(String servietype) {
    this.servietype = servietype;
  }

  public int getTon() {
    return ton;
  }

  public void setTon(int ton) {
    this.ton = ton;
  }

  public int getNpi() {
    return npi;
  }

  public void setNpi(int npi) {
    this.npi = npi;
  }

  public String getSdt() {
    return sdt;
  }

  public void setSdt(String sdt) {
    this.sdt = sdt;
  }

  public int getPid() {
    return pid;
  }

  public void setPid(int pid) {
    this.pid = pid;
  }

  public int getRdy() {
    return rdy;
  }

  public void setRdy(int rdy) {
    this.rdy = rdy;
  }

  public int getActflag() {
    return actflag;
  }

  public void setActflag(int actflag) {
    this.actflag = actflag;
  }

  public int getFetchcount() {
    return fetchcount;
  }

  public void setFetchcount(int fetchcount) {
    this.fetchcount = fetchcount;
  }

  public String getHttpUrl() {
    return httpUrl;
  }

  public void setHttpUrl(String httpUrl) {
    this.httpUrl = httpUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getCdmaSender() {
    return cdmaSender;
  }

  public void setCdmaSender(String cdmaSender) {
    this.cdmaSender = cdmaSender;
  }

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  public String getPushbindtype() {
    return pushbindtype;
  }

  public void setPushbindtype(String pushbindtype) {
    this.pushbindtype = pushbindtype;
  }

  public String getPullbindtype() {
    return pullbindtype;
  }

  public void setPullbindtype(String pullbindtype) {
    this.pullbindtype = pullbindtype;
  }

  public int getConfigtype() {
    return configtype;
  }

  public void setConfigtype(int configtype) {
    this.configtype = configtype;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }
}
