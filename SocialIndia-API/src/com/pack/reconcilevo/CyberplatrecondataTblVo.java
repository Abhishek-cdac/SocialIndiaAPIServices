package com.pack.reconcilevo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class CyberplatrecondataTblVo implements Serializable{
  
  private static final long serialVersionUID = 1L;
  public Integer cpCyberplatReconId;
  public Integer cpOutletCode;
  public String cpProviderName;
  public String cpNumber;
  public Double cpAmount;
  public String cpDealerTransId;
  public String cpCyberplatTransId;
  public String cpOptorTransId;
  public Date cpDateRecPayment;
  public Date cpDateCompPayment;
  public String cpStatus;
  public Integer cpReconStatus;
  public PaygatesetmtfileTblVo cpCbpltFileId;
  private UserMasterTblVo cpEntryBy;
  public Date cpEntryDatetime;

  
  
  public Integer getCpCyberplatReconId() {
    return cpCyberplatReconId;
  }
  public void setCpCyberplatReconId(Integer cpCyberplatReconId) {
    this.cpCyberplatReconId = cpCyberplatReconId;
  }
  public Integer getCpOutletCode() {
    return cpOutletCode;
  }
  public void setCpOutletCode(Integer cpOutletCode) {
    this.cpOutletCode = cpOutletCode;
  }
  public String getCpProviderName() {
    return cpProviderName;
  }
  public void setCpProviderName(String cpProviderName) {
    this.cpProviderName = cpProviderName;
  }
  public String getCpNumber() {
    return cpNumber;
  }
  public void setCpNumber(String cpNumber) {
    this.cpNumber = cpNumber;
  }
  public Double getCpAmount() {
    return cpAmount;
  }
  public void setCpAmount(Double cpAmount) {
    this.cpAmount = cpAmount;
  }
  public String getCpDealerTransId() {
    return cpDealerTransId;
  }
  
  public void setCpDealerTransId(String cpDealerTransId) {
    this.cpDealerTransId = cpDealerTransId;
  }

  public String getCpCyberplatTransId() {
    return cpCyberplatTransId;
  }
  public void setCpCyberplatTransId(String cpCyberplatTransId) {
    this.cpCyberplatTransId = cpCyberplatTransId;
  }
  public String getCpOptorTransId() {
    return cpOptorTransId;
  }
  public void setCpOptorTransId(String cpOptorTransId) {
    this.cpOptorTransId = cpOptorTransId;
  }
  public Date getCpDateRecPayment() {
    return cpDateRecPayment;
  }
  public void setCpDateRecPayment(Date cpDateRecPayment) {
    this.cpDateRecPayment = cpDateRecPayment;
  }
  public Date getCpDateCompPayment() {
    return cpDateCompPayment;
  }
  public void setCpDateCompPayment(Date cpDateCompPayment) {
    this.cpDateCompPayment = cpDateCompPayment;
  }
  public String getCpStatus() {
    return cpStatus;
  }
  public void setCpStatus(String cpStatus) {
    this.cpStatus = cpStatus;
  }
  public Integer getCpReconStatus() {
    return cpReconStatus;
  }
  public void setCpReconStatus(Integer cpReconStatus) {
    this.cpReconStatus = cpReconStatus;
  }
  public UserMasterTblVo getCpEntryBy() {
    return cpEntryBy;
  }
  public void setCpEntryBy(UserMasterTblVo cpEntryBy) {
    this.cpEntryBy = cpEntryBy;
  }
  public Date getCpEntryDatetime() {
    return cpEntryDatetime;
  }
  public void setCpEntryDatetime(Date cpEntryDatetime) {
    this.cpEntryDatetime = cpEntryDatetime;
  }
  public PaygatesetmtfileTblVo getCpCbpltFileId() {
    return cpCbpltFileId;
  }
  public void setCpCbpltFileId(PaygatesetmtfileTblVo cpCbpltFileId) {
    this.cpCbpltFileId = cpCbpltFileId;
  }

  
  

}
