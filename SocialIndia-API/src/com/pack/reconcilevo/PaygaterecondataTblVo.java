package com.pack.reconcilevo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class PaygaterecondataTblVo implements Serializable{
  
  private static final long serialVersionUID = 1L;
  public Integer pgPaygatReconId;
  public String pgTransactionId;
  public String pgMerchantName;
  public String pgGatewayRef;
  public String pgOrderNo;
  public Double pgAmount;
  public Double pgTransactionFee;
  public Double pgServiceTax;
  public Double pgSwatBharatCess;
  public Double pgKrishKalyanCess;
  public Double pgNetAmount;
  public String pgCountry;
  public String pgCurrency;
  public String pgGateway;
  public String pgPaymode;
  public Integer pgScheme;
  public String pgEmail;
  public String pgMobile;
  public String pgStatus;
  public String pgSubStatus;
  public String pgMerchantId;
  public Date pgTransactionDate;
  public Date pgTransStatusDate;
  public String pgDateNum;
  public String pgUdf1;
  public String pgUdf3;
  public String pgUdf5;
  public Integer pgActStatus;
  public PaygatesetmtfileTblVo pgPaygateSettleId;
  private UserMasterTblVo pgEntryBy;
  public Date pgEntryDatetime;
  
  
  public Integer getPgPaygatReconId() {
    return pgPaygatReconId;
  }
  public void setPgPaygatReconId(Integer pgPaygatReconId) {
    this.pgPaygatReconId = pgPaygatReconId;
  }
  public String getPgTransactionId() {
    return pgTransactionId;
  }
  public void setPgTransactionId(String pgTransactionId) {
    this.pgTransactionId = pgTransactionId;
  }
  public String getPgMerchantName() {
    return pgMerchantName;
  }
  public void setPgMerchantName(String pgMerchantName) {
    this.pgMerchantName = pgMerchantName;
  }
  public String getPgGatewayRef() {
    return pgGatewayRef;
  }
  public void setPgGatewayRef(String pgGatewayRef) {
    this.pgGatewayRef = pgGatewayRef;
  }
  public String getPgOrderNo() {
    return pgOrderNo;
  }
  public void setPgOrderNo(String pgOrderNo) {
    this.pgOrderNo = pgOrderNo;
  }
  public Double getPgAmount() {
    return pgAmount;
  }
  public void setPgAmount(Double pgAmount) {
    this.pgAmount = pgAmount;
  }
  public Double getPgTransactionFee() {
    return pgTransactionFee;
  }
  public void setPgTransactionFee(Double pgTransactionFee) {
    this.pgTransactionFee = pgTransactionFee;
  }
  public Double getPgServiceTax() {
    return pgServiceTax;
  }
  public void setPgServiceTax(Double pgServiceTax) {
    this.pgServiceTax = pgServiceTax;
  }
  public Double getPgSwatBharatCess() {
    return pgSwatBharatCess;
  }
  public void setPgSwatBharatCess(Double pgSwatBharatCess) {
    this.pgSwatBharatCess = pgSwatBharatCess;
  }
  public Double getPgKrishKalyanCess() {
    return pgKrishKalyanCess;
  }
  public void setPgKrishKalyanCess(Double pgKrishKalyanCess) {
    this.pgKrishKalyanCess = pgKrishKalyanCess;
  }
  public Double getPgNetAmount() {
    return pgNetAmount;
  }
  public void setPgNetAmount(Double pgNetAmount) {
    this.pgNetAmount = pgNetAmount;
  }
  public String getPgCountry() {
    return pgCountry;
  }
  public void setPgCountry(String pgCountry) {
    this.pgCountry = pgCountry;
  }
  public String getPgCurrency() {
    return pgCurrency;
  }
  public void setPgCurrency(String pgCurrency) {
    this.pgCurrency = pgCurrency;
  }
  public String getPgGateway() {
    return pgGateway;
  }
  public void setPgGateway(String pgGateway) {
    this.pgGateway = pgGateway;
  }
  public String getPgPaymode() {
    return pgPaymode;
  }
  public void setPgPaymode(String pgPaymode) {
    this.pgPaymode = pgPaymode;
  }
  public Integer getPgScheme() {
    return pgScheme;
  }
  public void setPgScheme(Integer pgScheme) {
    this.pgScheme = pgScheme;
  }
  public String getPgEmail() {
    return pgEmail;
  }
  public void setPgEmail(String pgEmail) {
    this.pgEmail = pgEmail;
  }
  public String getPgMobile() {
    return pgMobile;
  }
  public void setPgMobile(String pgMobile) {
    this.pgMobile = pgMobile;
  }
  public String getPgStatus() {
    return pgStatus;
  }
  public void setPgStatus(String pgStatus) {
    this.pgStatus = pgStatus;
  }
  public String getPgSubStatus() {
    return pgSubStatus;
  }
  public void setPgSubStatus(String pgSubStatus) {
    this.pgSubStatus = pgSubStatus;
  }

  public String getPgMerchantId() {
    return pgMerchantId;
  }
  public void setPgMerchantId(String pgMerchantId) {
    this.pgMerchantId = pgMerchantId;
  }
  public Date getPgTransactionDate() {
    return pgTransactionDate;
  }
  public void setPgTransactionDate(Date pgTransactionDate) {
    this.pgTransactionDate = pgTransactionDate;
  }
  public Date getPgTransStatusDate() {
    return pgTransStatusDate;
  }
  public void setPgTransStatusDate(Date pgTransStatusDate) {
    this.pgTransStatusDate = pgTransStatusDate;
  }
  public String getPgDateNum() {
    return pgDateNum;
  }
  public void setPgDateNum(String pgDateNum) {
    this.pgDateNum = pgDateNum;
  }
  public String getPgUdf1() {
    return pgUdf1;
  }
  public void setPgUdf1(String pgUdf1) {
    this.pgUdf1 = pgUdf1;
  }
  public String getPgUdf3() {
    return pgUdf3;
  }
  public void setPgUdf3(String pgUdf3) {
    this.pgUdf3 = pgUdf3;
  }
  public String getPgUdf5() {
    return pgUdf5;
  }
  public void setPgUdf5(String pgUdf5) {
    this.pgUdf5 = pgUdf5;
  }
  public Integer getPgActStatus() {
    return pgActStatus;
  }
  public void setPgActStatus(Integer pgActStatus) {
    this.pgActStatus = pgActStatus;
  }
  public UserMasterTblVo getPgEntryBy() {
    return pgEntryBy;
  }
  public void setPgEntryBy(UserMasterTblVo pgEntryBy) {
    this.pgEntryBy = pgEntryBy;
  }
  public Date getPgEntryDatetime() {
    return pgEntryDatetime;
  }
  public void setPgEntryDatetime(Date pgEntryDatetime) {
    this.pgEntryDatetime = pgEntryDatetime;
  }
  public PaygatesetmtfileTblVo getPgPaygateSettleId() {
    return pgPaygateSettleId;
  }
  public void setPgPaygateSettleId(PaygatesetmtfileTblVo pgPaygateSettleId) {
    this.pgPaygateSettleId = pgPaygateSettleId;
  }

  

}
