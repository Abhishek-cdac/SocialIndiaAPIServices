package com.pack.billpayment;

import org.json.JSONObject;

public interface CyberplatvalidationIntface {

	public JSONObject toValidatePostpaid (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidatePrepaid (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidateDTH (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidateElectricity (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidateLandLine (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidateGasBill (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidateInsurance (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidateBroadBand (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidateDirectMoneyTransfer (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidatePostPaidDataCard (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
	public JSONObject toValidatePrePaidDataCard (String prmNumber, String prmGateid, String prmCateg, Double prmAmount);
	
}
