package com.social.email.persistense;

import java.io.Serializable;
import java.util.Date;

public class EmailTemplateTblVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private int tempid;
  private String tempName;
  private String subject;
  private String tempContent;
  private int status;
  private Date entryDate;
  private Date updateDatetime;
  private String header;
  private String footer;
  private int entryPerson;

  public EmailTemplateTblVo() {
  }

  public int getTempid() {
    return tempid;
  }

  public void setTempid(int tempid) {
    this.tempid = tempid;
  }

  public String getTempName() {
    return tempName;
  }

  public void setTempName(String tempName) {
    this.tempName = tempName;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTempContent() {
    return tempContent;
  }

  public void setTempContent(String tempContent) {
    this.tempContent = tempContent;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
  }

  public Date getUpdateDatetime() {
    return updateDatetime;
  }

  public void setUpdateDatetime(Date updateDatetime) {
    this.updateDatetime = updateDatetime;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getFooter() {
    return footer;
  }

  public void setFooter(String footer) {
    this.footer = footer;
  }

  public int getEntryPerson() {
    return entryPerson;
  }

  public void setEntryPerson(int entryPerson) {
    this.entryPerson = entryPerson;
  }

}
