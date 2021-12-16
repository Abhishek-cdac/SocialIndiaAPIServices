package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class MaintenanceFeeTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maintenanceId;
	private float serviceCharge;
	private Date billDate;
	private float municipalTax;
	private float penalty;
	private float waterCharge;
	private float nonOccupancyCharge;
	private float culturalCharge;
	private float sinkingFund;
	private float repairFund;
	private float insuranceCharges;
	private float playZoneCharge;
	private float majorRepairFund;
	private String uploadFileName;
	private float interest;
	private float latefee;
	private float carpark1;
	private float carpark2;
	private float twowheeler1;
	private float twowheeler2;
	private float sundayadjust;
	private float protax;
	private float exceespay;
	private float clubhouse;
	private float arrears;
	private float previousdue;
	private float appfees;
	private String amountword;
	private String billno;
	private String flatno;
	private String flatarea;
	private String notes;
	private String miscellious;	
	private int statusFlag;
	private int paidStatus;
	private Date entryDatetime;
	private Date modifyDatetime;
	private UserMasterTblVo userId;
	private UserMasterTblVo entryBy;
	private int dataUploadFrom;
	private float totbillamt;
	private String pdfstatus;
	
	public MaintenanceFeeTblVO(){}

	public int getMaintenanceId() {
		return maintenanceId;
	}

	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}

	public float getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(float serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public float getMunicipalTax() {
		return municipalTax;
	}

	public void setMunicipalTax(float municipalTax) {
		this.municipalTax = municipalTax;
	}

	public float getPenalty() {
		return penalty;
	}

	public void setPenalty(float penalty) {
		this.penalty = penalty;
	}

	public float getWaterCharge() {
		return waterCharge;
	}

	public void setWaterCharge(float waterCharge) {
		this.waterCharge = waterCharge;
	}

	public float getNonOccupancyCharge() {
		return nonOccupancyCharge;
	}

	public void setNonOccupancyCharge(float nonOccupancyCharge) {
		this.nonOccupancyCharge = nonOccupancyCharge;
	}

	public float getCulturalCharge() {
		return culturalCharge;
	}

	public void setCulturalCharge(float culturalCharge) {
		this.culturalCharge = culturalCharge;
	}

	public float getSinkingFund() {
		return sinkingFund;
	}

	public void setSinkingFund(float sinkingFund) {
		this.sinkingFund = sinkingFund;
	}

	public float getRepairFund() {
		return repairFund;
	}

	public void setRepairFund(float repairFund) {
		this.repairFund = repairFund;
	}

	public float getInsuranceCharges() {
		return insuranceCharges;
	}

	public void setInsuranceCharges(float insuranceCharges) {
		this.insuranceCharges = insuranceCharges;
	}

	public float getPlayZoneCharge() {
		return playZoneCharge;
	}

	public void setPlayZoneCharge(float playZoneCharge) {
		this.playZoneCharge = playZoneCharge;
	}

	public float getMajorRepairFund() {
		return majorRepairFund;
	}

	public void setMajorRepairFund(float majorRepairFund) {
		this.majorRepairFund = majorRepairFund;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public float getInterest() {
		return interest;
	}

	public void setInterest(float interest) {
		this.interest = interest;
	}

	public float getLatefee() {
		return latefee;
	}

	public void setLatefee(float latefee) {
		this.latefee = latefee;
	}

	public float getCarpark1() {
		return carpark1;
	}

	public void setCarpark1(float carpark1) {
		this.carpark1 = carpark1;
	}

	public float getCarpark2() {
		return carpark2;
	}

	public void setCarpark2(float carpark2) {
		this.carpark2 = carpark2;
	}

	public float getTwowheeler1() {
		return twowheeler1;
	}

	public void setTwowheeler1(float twowheeler1) {
		this.twowheeler1 = twowheeler1;
	}

	public float getTwowheeler2() {
		return twowheeler2;
	}

	public void setTwowheeler2(float twowheeler2) {
		this.twowheeler2 = twowheeler2;
	}

	public float getSundayadjust() {
		return sundayadjust;
	}

	public void setSundayadjust(float sundayadjust) {
		this.sundayadjust = sundayadjust;
	}

	public float getProtax() {
		return protax;
	}

	public void setProtax(float protax) {
		this.protax = protax;
	}

	public float getExceespay() {
		return exceespay;
	}

	public void setExceespay(float exceespay) {
		this.exceespay = exceespay;
	}

	public float getClubhouse() {
		return clubhouse;
	}

	public void setClubhouse(float clubhouse) {
		this.clubhouse = clubhouse;
	}

	public float getArrears() {
		return arrears;
	}

	public void setArrears(float arrears) {
		this.arrears = arrears;
	}

	public float getPreviousdue() {
		return previousdue;
	}

	public void setPreviousdue(float previousdue) {
		this.previousdue = previousdue;
	}

	public float getAppfees() {
		return appfees;
	}

	public void setAppfees(float appfees) {
		this.appfees = appfees;
	}

	public String getAmountword() {
		return amountword;
	}

	public void setAmountword(String amountword) {
		this.amountword = amountword;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getFlatno() {
		return flatno;
	}

	public void setFlatno(String flatno) {
		this.flatno = flatno;
	}

	public String getFlatarea() {
		return flatarea;
	}

	public void setFlatarea(String flatarea) {
		this.flatarea = flatarea;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getMiscellious() {
		return miscellious;
	}

	public void setMiscellious(String miscellious) {
		this.miscellious = miscellious;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}

	public int getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(int paidStatus) {
		this.paidStatus = paidStatus;
	}

	public Date getEntryDatetime() {
		return entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public UserMasterTblVo getUserId() {
		return userId;
	}

	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}

	public int getDataUploadFrom() {
		return dataUploadFrom;
	}

	public void setDataUploadFrom(int dataUploadFrom) {
		this.dataUploadFrom = dataUploadFrom;
	}

	public float getTotbillamt() {
		
		return totbillamt;
	}

	public void setTotbillamt(float totbillamt) {
		this.totbillamt = totbillamt;
	}

	public String getPdfstatus() {
		return pdfstatus;
	}

	public void setPdfstatus(String pdfstatus) {
		this.pdfstatus = pdfstatus;
	}

	

}
