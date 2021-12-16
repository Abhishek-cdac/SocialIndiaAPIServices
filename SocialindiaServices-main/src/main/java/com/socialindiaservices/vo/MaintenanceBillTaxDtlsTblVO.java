package com.socialindiaservices.vo;

import java.io.Serializable;

public class MaintenanceBillTaxDtlsTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ID;
	private String modOfPayment;
	private int fixedCharges;
	private int percentageVO;
	private int amntGt1000;
	private int amntLt1000;
	private String calcLogic;
	private int convCharge;
	private int gstVO;
	
	public MaintenanceBillTaxDtlsTblVO(){}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getModOfPayment() {
		return modOfPayment;
	}

	public void setModOfPayment(String modOfPayment) {
		this.modOfPayment = modOfPayment;
	}

	public int getFixedCharges() {
		return fixedCharges;
	}

	public void setFixedCharges(int fixedCharges) {
		this.fixedCharges = fixedCharges;
	}

	public int getPercentageVO() {
		return percentageVO;
	}

	public void setPercentageVO(int percentageVO) {
		this.percentageVO = percentageVO;
	}

	public int getAmntGt1000() {
		return amntGt1000;
	}

	public void setAmntGt1000(int amntGt1000) {
		this.amntGt1000 = amntGt1000;
	}

	public int getAmntLt1000() {
		return amntLt1000;
	}

	public void setAmntLt1000(int amntLt1000) {
		this.amntLt1000 = amntLt1000;
	}

	public String getCalcLogic() {
		return calcLogic;
	}

	public void setCalcLogic(String calcLogic) {
		this.calcLogic = calcLogic;
	}

	public int getConvCharge() {
		return convCharge;
	}

	public void setConvCharge(int convCharge) {
		this.convCharge = convCharge;
	}

	public int getGstVO() {
		return gstVO;
	}

	public void setGstVO(int gstVO) {
		this.gstVO = gstVO;
	}

}