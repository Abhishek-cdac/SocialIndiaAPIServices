package com.social.sms.persistense;

import java.io.Serializable;
import java.util.Date;

public class SmsTemplatepojo implements Serializable {

  private static final long serialVersionUID = 1L;

  private int templateId;
  private String templateName;
  private String templateContent;
  private int status;
  private Date entryDateTime;
  private Date updateDateTime;
  private int entryPerson;

  public SmsTemplatepojo() {
  }

  public int getTemplateId() {
    return templateId;
  }

  public void setTemplateId(int templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateContent() {
    return templateContent;
  }

  public void setTemplateContent(String templateContent) {
    this.templateContent = templateContent;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getEntryDateTime() {
    return entryDateTime;
  }

  public void setEntryDateTime(Date entryDateTime) {
    this.entryDateTime = entryDateTime;
  }

  public Date getUpdateDateTime() {
    return updateDateTime;
  }

  public void setUpdateDateTime(Date updateDateTime) {
    this.updateDateTime = updateDateTime;
  }

  public int getEntryPerson() {
    return entryPerson;
  }

  public void setEntryPerson(int entryPerson) {
    this.entryPerson = entryPerson;
  }

}
