package com.social.email.persistense;

import java.io.Serializable;

public class EmailConfigTblVo implements Serializable {

  private static final long serialVersionUID = 1L;
  private int sid;
  private String userName;
  private String passWord;
  private String hostName;
  private String portNo;
  private String fromId;
  private String protoCol;
  private int fetchConf;
  private String bodcontFileIp;
  private String imgHostIp;
  private String logFilePath;
  private String tempPath;
  private int actStatus;
  private int maxThread;

  public EmailConfigTblVo() {
  }

  public int getSid() {
    return sid;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
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

  public String getFromId() {
    return fromId;
  }

  public void setFromId(String fromId) {
    this.fromId = fromId;
  }

  public String getProtoCol() {
    return protoCol;
  }

  public void setProtoCol(String protoCol) {
    this.protoCol = protoCol;
  }

  public int getFetchConf() {
    return fetchConf;
  }

  public void setFetchConf(int fetchConf) {
    this.fetchConf = fetchConf;
  }

  public String getBodcontFileIp() {
    return bodcontFileIp;
  }

  public void setBodcontFileIp(String bodcontFileIp) {
    this.bodcontFileIp = bodcontFileIp;
  }

  public String getImgHostIp() {
    return imgHostIp;
  }

  public void setImgHostIp(String imgHostIp) {
    this.imgHostIp = imgHostIp;
  }

  public String getLogFilePath() {
    return logFilePath;
  }

  public void setLogFilePath(String logFilePath) {
    this.logFilePath = logFilePath;
  }

  public String getTempPath() {
    return tempPath;
  }

  public void setTempPath(String tempPath) {
    this.tempPath = tempPath;
  }

  public int getActStatus() {
    return actStatus;
  }

  public void setActStatus(int actStatus) {
    this.actStatus = actStatus;
  }

  public int getMaxThread() {
    return maxThread;
  }

  public void setMaxThread(int maxThread) {
    this.maxThread = maxThread;
  }

}
