package com.social.email.persistense;

import java.io.Serializable;
import java.util.Date;

public class EmailInTblVo implements Serializable {

  private static final long serialVersionUID = 1L;
  private int emailId;
  private String fromId;
  private String toId;
  private String ccId;
  private String bccId;
  private String subId;
  private String statFlag;
  private String contFormat;
  private String bodyContent;
  private String attTempId;
  private Date inDate;
  private String tempId;
  private String fileName;
  private String pdfPass;
  private String tempVal;
  private String bodyContFlag;
  private int trycount;

  public EmailInTblVo() {
  }

  public int getEmailId() {
    return emailId;
  }

  public void setEmailId(int emailId) {
    this.emailId = emailId;
  }

  public String getFromId() {
    return fromId;
  }

  public void setFromId(String fromId) {
    this.fromId = fromId;
  }

  public String getToId() {
    return toId;
  }

  public void setToId(String toId) {
    this.toId = toId;
  }

  public String getCcId() {
    return ccId;
  }

  public void setCcId(String ccId) {
    this.ccId = ccId;
  }

  public String getBccId() {
    return bccId;
  }

  public void setBccId(String bccId) {
    this.bccId = bccId;
  }

  public String getSubId() {
    return subId;
  }

  public void setSubId(String subId) {
    this.subId = subId;
  }

  public String getStatFlag() {
    return statFlag;
  }

  public void setStatFlag(String statFlag) {
    this.statFlag = statFlag;
  }

  public String getContFormat() {
    return contFormat;
  }

  public void setContFormat(String contFormat) {
    this.contFormat = contFormat;
  }

  public String getBodyContent() {
    return bodyContent;
  }

  public void setBodyContent(String bodyContent) {
    this.bodyContent = bodyContent;
  }

  public String getAttTempId() {
    return attTempId;
  }

  public void setAttTempId(String attTempId) {
    this.attTempId = attTempId;
  }

  public Date getInDate() {
    return inDate;
  }

  public void setInDate(Date inDate) {
    this.inDate = inDate;
  }

  public String getTempId() {
    return tempId;
  }

  public void setTempId(String tempId) {
    this.tempId = tempId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getPdfPass() {
    return pdfPass;
  }

  public void setPdfPass(String pdfPass) {
    this.pdfPass = pdfPass;
  }

  public String getTempVal() {
    return tempVal;
  }

  public void setTempVal(String tempVal) {
    this.tempVal = tempVal;
  }

  public String getBodyContFlag() {
    return bodyContFlag;
  }

  public void setBodyContFlag(String bodyContFlag) {
    this.bodyContFlag = bodyContFlag;
  }

  public int getTrycount() {
    return trycount;
  }

  public void setTrycount(int trycount) {
    this.trycount = trycount;
  }

}
