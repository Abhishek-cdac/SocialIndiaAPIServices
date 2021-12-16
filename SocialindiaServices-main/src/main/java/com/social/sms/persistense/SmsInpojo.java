package com.social.sms.persistense;

import java.io.Serializable;
import java.util.Date;

public class SmsInpojo implements Serializable {

  private static final long serialVersionUID = 1L;

  private int smsId;
  private String smsMobNumber;
  private String smsCardNo;
  private String smsMessage;
  private String smsTmpltId;
  private String smsTmpltVal;
  private String smspollFlag;
  private String smsTempId;
  private Date smsEntryTime;
  private String smsEntryDateTime;
  private int trycount;

  /**
   * toString.
   */
  public String toString() {
    return "{[smsId=" + smsId + ",smsMobNumber=" + smsMobNumber + ",smsCardNo="
        + smsCardNo + ",smsMessage=" + smsMessage + ",smsTmpltId=" + smsTmpltId
        + ",smsTmpltVal=" + smsTmpltVal + ",smspollFlag=" + smspollFlag
        + ",smsTempId=" + smsTempId + ",smsEntryTim=" + smsEntryTime
        + ",smsEntryDateTime=" + smsEntryDateTime + ",trycount=" + trycount
        + "]}";
  }

  public SmsInpojo() {
  }

  public int getSmsId() {
    return smsId;
  }

  public void setSmsId(int smsId) {
    this.smsId = smsId;
  }

  public String getSmsMobNumber() {
    return smsMobNumber;
  }

  public void setSmsMobNumber(String smsMobNumber) {
    this.smsMobNumber = smsMobNumber;
  }

  public String getSmsCardNo() {
    return smsCardNo;
  }

  public void setSmsCardNo(String smsCardNo) {
    this.smsCardNo = smsCardNo;
  }

  public String getSmsMessage() {
    return smsMessage;
  }

  public void setSmsMessage(String smsMessage) {
    this.smsMessage = smsMessage;
  }

  public String getSmsTmpltId() {
    return smsTmpltId;
  }

  public void setSmsTmpltId(String smsTmpltId) {
    this.smsTmpltId = smsTmpltId;
  }

  public String getSmsTmpltVal() {
    return smsTmpltVal;
  }

  public void setSmsTmpltVal(String smsTmpltVal) {
    this.smsTmpltVal = smsTmpltVal;
  }

  public String getSmspollFlag() {
    return smspollFlag;
  }

  public void setSmspollFlag(String smspollFlag) {
    this.smspollFlag = smspollFlag;
  }

  public String getSmsTempId() {
    return smsTempId;
  }

  public void setSmsTempId(String smsTempId) {
    this.smsTempId = smsTempId;
  }

  public Date getSmsEntryTime() {
    return smsEntryTime;
  }

  public void setSmsEntryTime(Date smsEntryTime) {
    this.smsEntryTime = smsEntryTime;
  }

  public String getSmsEntryDateTime() {
    return smsEntryDateTime;
  }

  public void setSmsEntryDateTime(String smsEntryDateTime) {
    this.smsEntryDateTime = smsEntryDateTime;
  }

  public int getTrycount() {
    return trycount;
  }

  public void setTrycount(int trycount) {
    this.trycount = trycount;
  }
}
