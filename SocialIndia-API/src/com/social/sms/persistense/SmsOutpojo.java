package com.social.sms.persistense;

import java.io.Serializable;
import java.util.Date;

public class SmsOutpojo implements Serializable {

  private static final long serialVersionUID = 1L;

  private int smid;
  private String mobNo;
  private String cardNo;
  private String msgid;
  private String message;
  private String tempId;
  private String tempVAl;
  private String poolFlag;
  private String smstempId;
  private Date entryDate;

  public SmsOutpojo() {
  }

  /**
   * toString.
   */
  public String toString() {
    return "{[smid=" + smid + ",mobNo=" + mobNo + ",cardNo=" + cardNo
        + ",msgid=" + msgid + ",message=" + message + ",tempId=" + tempId
        + ",tempVAl=" + tempVAl + ",poolFlag=" + poolFlag + ",smstempId="
        + smstempId + ",entryDate=" + entryDate + "]}";
  }

  public int getSmid() {
    return smid;
  }

  public void setSmid(int smid) {
    this.smid = smid;
  }

  public String getMobNo() {
    return mobNo;
  }

  public void setMobNo(String mobNo) {
    this.mobNo = mobNo;
  }

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public String getMsgid() {
    return msgid;
  }

  public void setMsgid(String msgid) {
    this.msgid = msgid;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getTempId() {
    return tempId;
  }

  public void setTempId(String tempId) {
    this.tempId = tempId;
  }

  public String getTempVAl() {
    return tempVAl;
  }

  public void setTempVAl(String tempVAl) {
    this.tempVAl = tempVAl;
  }

  public String getPoolFlag() {
    return poolFlag;
  }

  public void setPoolFlag(String poolFlag) {
    this.poolFlag = poolFlag;
  }

  public String getSmstempId() {
    return smstempId;
  }

  public void setSmstempId(String smstempId) {
    this.smstempId = smstempId;
  }

  public Date getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
  }
}
