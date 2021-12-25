package com.social.sms.persistense;

import java.io.Serializable;
import java.util.Date;

public class SmsTemplateParserpojo implements Serializable {

  private static final long serialVersionUID = 1L;

  private int parserid;
  private int sno;
  private String templateName;
  private String templateParser;
  private String columnName;
  private int status;
  private Date updateDate;

  public SmsTemplateParserpojo() {
  }

  public int getParserid() {
    return parserid;
  }

  public void setParserid(int parserid) {
    this.parserid = parserid;
  }

  public int getSno() {
    return sno;
  }

  public void setSno(int sno) {
    this.sno = sno;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateParser() {
    return templateParser;
  }

  public void setTemplateParser(String templateParser) {
    this.templateParser = templateParser;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
