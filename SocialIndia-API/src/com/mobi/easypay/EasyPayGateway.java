package com.mobi.easypay;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.easypayvo.MvpPaygateTbl;
import com.mobi.easypayvo.MvpTransactionTbl;
import com.mobi.easypayvo.MvpUtilityPayLogTbl;
import com.mobi.easypayvo.MvpUtilityPayTbl;
import com.mobi.easypayvo.persistence.EasyPaymentDao;
import com.mobi.easypayvo.persistence.EasyPaymentDaoServices;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.billpayment.Cyberplatvalidation;
import com.pack.billpaymentvo.CyberplatoptrsTblVo;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class EasyPayGateway extends ActionSupport {
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	private String payHttpurl;
	private String cyperPlateurl;
	private String finaldata;
	private String responseData;
	private String redirectUrl;
	Log log= new Log();
	JsonpackDao jsonPack = new JsonpackDaoService();
	
	String locRid = null;
	String locNumber = null;
	String locAccount = null;
	String locAmount = null;
	String payMode = null;
	String locAmountAll = null;
	String locComment = null;
	String locTermId = null;
	String locAuthenticator = null;
	String locBiller = null;
	String locOperatorId = null;
	String locPassUniqId = null;
	String locMaintenanceId = null;
	String locPaidStatus = null;
	String locPaymentType = null;
	
	public String execute() {
		redirectUrl = getText("SOCIAL_INDIA_BASE_URL") + getText("paymentRes.url");
		System.out.println("Enter EasyPayGateway============================================");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		JSONObject validationMsg = null;
		boolean flg = true;
		String remarksMsg = "";
		String rescode = "";
		String rescodeStsMsg = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;	
			int paymentType = 0;
			int maintenanceId = 0;
			JSONObject paymentCallObj = new JSONObject();
			log.logMessage("Enter into EasyPayGateway ", "info", EasyPayGateway.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("EasyPayGateway ivrparams :" + ivrparams, "info", EasyPayGateway.class);
				System.out.println("Enter===========1==================================");
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					System.out.println("Enter=========2====================================");
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						System.out.println("Enter===========3=================================="); 
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						locOperatorId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"operator_id");
						locPassUniqId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"pass_uniqid");
						locNumber = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"number");
						locAccount = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"account");
						locAmount = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"amount");
						payMode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"payment_mode");
						locAmountAll = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"amount_all");
						locComment = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"comment");
						locTermId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"term_id");
						locAuthenticator = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"authenticator");
						locBiller = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"biller_category");
						
						locMaintenanceId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"maintenance_id");
						locPaidStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"paid_status");
						locPaymentType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"payment_type"); //1-utility pay, 2- Maintenance pay
					}
					if (Commonutility.checkempty(ivrservicecode)) {
						if (ivrservicecode.length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
							
						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.error")));
					}					
					if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					
					if (Commonutility.checkempty(societyKey)) {
						if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {
							
						} else {
							String[] passData = { getText("societykey.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}				
					
					if (locObjRecvdataJson !=null) {		
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
							if (rid !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}
						if (Commonutility.checkempty(locPaymentType) && Commonutility.toCheckisNumeric(locPaymentType)) {
							paymentType = Commonutility.stringToInteger(locPaymentType);
							if (paymentType != 0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("payment.type.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("payment.type.error")));
						}
						if (paymentType == 1) {
							
						} else if (paymentType == 2) {
							if (Commonutility.checkempty(locMaintenanceId) && Commonutility.toCheckisNumeric(locMaintenanceId)) {
								maintenanceId = Commonutility.stringToInteger(locMaintenanceId);
								if (maintenanceId !=0 ) {
									
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("maintenance.id.error")));
								}
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("maintenance.id.error")));
							}
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("flg :" +flg, "info", EasyPayGateway.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						
						String genorderno = "";
						float amtval = 0.0f;
						EasyPaymentDao easypayDaoObj = new EasyPaymentDaoServices();
						MvpTransactionTbl transActObj = new MvpTransactionTbl();
						MvpPaygateTbl payGateObj = new MvpPaygateTbl();
						
						CommonUtilsDao common=new CommonUtilsDao();	
						genorderno = common.getRandomval("09",8);
						System.out.println("genorderno------------>> " + genorderno);
						if (Commonutility.checkempty(genorderno)) {
						} else {
							genorderno = "";
						}
						if (Commonutility.checkempty(locAmount)) {
							amtval = Commonutility.stringToFloat(locAmount);
						}
						
						transActObj.setOrderNo(genorderno);
						transActObj.setServiceType(Commonutility.stringToInteger(locBiller));
						transActObj.setTxnAmount(amtval);
						transActObj.setTxnDatetime(Commonutility.enteyUpdateInsertDateTime());
						transActObj.setGmtDatetime(Commonutility.getGmtCurrentTme());
						transActObj.setPamentGatewayStatus(1); //Pending
						transActObj.setFinalStatus(1); //Pending
						transActObj.setUserId(rid);
						transActObj.setPaymentType(paymentType);
						transActObj.setMaintenanceId(maintenanceId);
						transActObj.setEntryDateTime(Commonutility.enteyUpdateInsertDateTime());
						transActObj.setModifyDateTime(Commonutility.enteyUpdateInsertDateTime());
						int transId = easypayDaoObj.transactionFirstInsert(transActObj);
						int paymGateid = -1;
						
						if (transId !=-1 ) {
							payGateObj.setAgId(easypayDaoObj.getConfigDetails("payurl.agid"));
							payGateObj.setMeId(easypayDaoObj.getConfigDetails("MID"));
							payGateObj.setOrderNo(genorderno);
							payGateObj.setAmount(amtval);
							payGateObj.setCountry(easypayDaoObj.getConfigDetails("payurl.Country"));
							payGateObj.setCurrency(easypayDaoObj.getConfigDetails("payurl.Currency"));
							payGateObj.setTxnType(easypayDaoObj.getConfigDetails("payurl.txntype"));
							payGateObj.setSuccessUrl(easypayDaoObj.getConfigDetails("transaction.successurl"));
							payGateObj.setFailureUrl(easypayDaoObj.getConfigDetails("transaction.errorurl"));
							payGateObj.setChannel(easypayDaoObj.getConfigDetails("payurl.Channel"));
							payGateObj.setFinalStatus(1); //Pending
							payGateObj.setUsrId(rid);
							payGateObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							payGateObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							
							UserMasterTblVo usrDetails = new UserMasterTblVo();
							profileDao profile = new profileDaoServices();
							usrDetails = profile.getUserPrfileInfo(String.valueOf(rid));
							if (usrDetails != null) {
								payGateObj.setUdf_1Data(Commonutility.intToString(usrDetails.getSocietyId().getTownshipId().getTownshipId()));
								payGateObj.setUdf_2Data(Commonutility.intToString(usrDetails.getSocietyId().getSocietyId()));
								String usrMobEmail = "";
								if (Commonutility.checkempty(usrDetails.getMobileNo()) && Commonutility.checkempty(usrDetails.getEmailId())) {
									usrMobEmail = usrDetails.getMobileNo()+","+usrDetails.getEmailId();
								} else {
									if (Commonutility.checkempty(usrDetails.getMobileNo())) {
									usrMobEmail = usrDetails.getMobileNo();
									} else { 
										if (Commonutility.checkempty(usrDetails.getEmailId())) {
											usrMobEmail = usrDetails.getEmailId();
										}
									}
								}
								payGateObj.setUdf_3Data(usrMobEmail);
								String profileName = "";
								String udf4data = "";
								if (Commonutility.checkIntempty(usrDetails.getUserId()) && Commonutility.checkempty(usrDetails.getFirstName())) {
									profileName = usrDetails.getFirstName();
									if (Commonutility.checkempty(usrDetails.getLastName())) {
										profileName = usrDetails.getFirstName()+usrDetails.getLastName();
									}
									udf4data = usrDetails.getUserId()+","+profileName;
								} else { 
									if (Commonutility.checkIntempty(usrDetails.getUserId())) {
									udf4data = Commonutility.intToString(usrDetails.getUserId());
									} else {
										if (Commonutility.checkempty(usrDetails.getFirstName())) {
										profileName = usrDetails.getFirstName();
										if (Commonutility.checkempty(usrDetails.getLastName())) {
										profileName = usrDetails.getFirstName()+usrDetails.getLastName();
										}
										udf4data = profileName;
										}
									}
								}
								payGateObj.setUdf_4Data(udf4data);
								payGateObj.setUdf_5Data("");
							}
							paymGateid = easypayDaoObj.payGateInsert(payGateObj);
							
							if (paymentType == 1) {
							int utilityId = -1;
							MvpUtilityPayTbl utilityPayObj = new MvpUtilityPayTbl();
							utilityPayObj.setSd(easypayDaoObj.getConfigDetails("cyberplate.SD"));
							utilityPayObj.setAp(easypayDaoObj.getConfigDetails("cyberplate.AP"));
							utilityPayObj.setOp(easypayDaoObj.getConfigDetails("cyberplate.OP"));
							utilityPayObj.setSession(genorderno);
							utilityPayObj.setNumber(Commonutility.stringToStringempty(locNumber));
							utilityPayObj.setAmount(amtval);
							utilityPayObj.setAmountAll(Commonutility.stringToStringempty(locAmountAll));
							utilityPayObj.setComment(Commonutility.stringToStringempty(locComment));
							utilityPayObj.setTermId(Commonutility.stringToStringempty(locTermId));
							utilityPayObj.setAuthenticator3(Commonutility.stringToStringempty(locAuthenticator));
							utilityPayObj.setAccount(Commonutility.stringToStringempty(locAccount));
							//utilityPayObj.setFinalStatus(1); //Pending
							utilityPayObj.setUsrId(rid);
							utilityPayObj.setOperatorCodeId(Commonutility.stringToInteger(locOperatorId));
							utilityPayObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							utilityPayObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							utilityId = easypayDaoObj.utilityPayInsert(utilityPayObj);
							
							if (paymGateid != -1 && utilityId != -1) {
								String response="";
								payGateObj = new MvpPaygateTbl();
								payGateObj = easypayDaoObj.getPaygateDetails(paymGateid,rid);
								
								validationMsg = new JSONObject();
								Cyberplatvalidation cyberValidObj = new Cyberplatvalidation();
								double amtvalue = amtval;
								if (locBiller.equalsIgnoreCase("1")) {
									validationMsg = cyberValidObj.toValidatePrepaid(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("2")) {
									validationMsg = cyberValidObj.toValidateDTH(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("3")) {
									validationMsg = cyberValidObj.toValidatePrePaidDataCard(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("4")) {
									validationMsg = cyberValidObj.toValidatePostpaid(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("5")) {
									validationMsg = cyberValidObj.toValidateLandLine(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("6")) {
									validationMsg = cyberValidObj.toValidateElectricity(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("7")) {
									validationMsg = cyberValidObj.toValidateGasBill(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("8")) {
									validationMsg = cyberValidObj.toValidateInsurance(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("9")) {
									validationMsg = cyberValidObj.toValidateBroadBand(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("10")) {
									validationMsg = cyberValidObj.toValidateDirectMoneyTransfer(locNumber, locOperatorId, locBiller, amtvalue);
								} else if (locBiller.equalsIgnoreCase("11")) {
									validationMsg = cyberValidObj.toValidatePostPaidDataCard(locNumber, locOperatorId, locBiller, amtvalue);
								}
								System.out.println("validationMsg----------------" + validationMsg.toString());
								String validMessage = "";
								JSONObject validData = null;
								String validStausMsg = "";
								String validStausCode = "";
								if (validationMsg != null) {
									validMessage = (String) Commonutility.toHasChkJsonRtnValObj(validationMsg,"message");
									validData = (JSONObject) Commonutility.toHasChkJsonRtnValObj(validationMsg,"data");
									validStausMsg = (String) Commonutility.toHasChkJsonRtnValObj(validationMsg,"statusmsg");
									validStausCode = (String) Commonutility.toHasChkJsonRtnValObj(validationMsg,"statuscode");
									System.out.println("validStausMsg----------->" + validStausMsg + "-----validStausCode------> " + validStausCode);
								}
								if (payGateObj != null && Commonutility.stringToStringempty(payGateObj.getStatus()) == "" 
										&& validStausCode.equalsIgnoreCase("0") && validStausMsg.equalsIgnoreCase("success")) { 									
									System.out.println("Enter --->>>validStausMsg----------->" + validStausMsg + "-----validStausCode------> " + validStausCode);
									JSONObject payDataArr = new JSONObject();
									JSONObject txndetailObj = new JSONObject();
									paymentCallObj.put("request","Transaction");
									
									txndetailObj.put("ag_id", payGateObj.getAgId());
									txndetailObj.put("me_id", payGateObj.getMeId());
									txndetailObj.put("order_no", payGateObj.getOrderNo());
									if (payGateObj.getAmount() != 0) {
										float priceAmt = payGateObj.getAmount();
										DecimalFormat df = new DecimalFormat("#.00");
										df.setMaximumFractionDigits(2);
										txndetailObj.put("Amount", Commonutility.toCheckNullEmpty(df.format(priceAmt)));
									} else if (payGateObj.getAmount() == 0 || payGateObj.getAmount() == 0.0) {
										txndetailObj.put("Amount", "0");
									} else {
										txndetailObj.put("Amount", "");
									}
									txndetailObj.put("Country", payGateObj.getCountry());
									txndetailObj.put("Currency", payGateObj.getCurrency());
									txndetailObj.put("txn_type", payGateObj.getTxnType());
									txndetailObj.put("success_url", payGateObj.getSuccessUrl());
									txndetailObj.put("failure_url", payGateObj.getFailureUrl());
									txndetailObj.put("Channel", payGateObj.getChannel());
									payDataArr.put("txn_details",txndetailObj);
									
									JSONObject pgdetailObj = new JSONObject();
									pgdetailObj.put("pg_id", "");
									pgdetailObj.put("Paymode", payMode);
									pgdetailObj.put("Scheme", "");
									pgdetailObj.put("emi_months", "");
									payDataArr.put("pg_details",pgdetailObj);
									
									JSONObject cardDetailObj = new JSONObject();
									cardDetailObj.put("card_no", "");
									cardDetailObj.put("exp_month", "");
									cardDetailObj.put("exp_year", "");
									cardDetailObj.put("cvv2", "");
									cardDetailObj.put("card_name", "");
									payDataArr.put("card_details",cardDetailObj);
									
									JSONObject custdetailObj = new JSONObject();
									custdetailObj.put("cust_name", "");
									custdetailObj.put("email_id", "");
									custdetailObj.put("mobile_no", "");
									custdetailObj.put("unique_id", "");
									custdetailObj.put("is_logged_in", "");
									payDataArr.put("cust_details",custdetailObj);
									
									JSONObject billdetailObj = new JSONObject();
									billdetailObj.put("bill_address", "");
									billdetailObj.put("bill_city", "");
									billdetailObj.put("bill_state", "");
									billdetailObj.put("bill_country", "");
									billdetailObj.put("bill_zip", "");
									payDataArr.put("bill_details",billdetailObj);
									
									JSONObject shipdetailObj = new JSONObject();
									shipdetailObj.put("ship_address", "");
									shipdetailObj.put("ship_city", "");
									shipdetailObj.put("ship_state", "");
									shipdetailObj.put("ship_country", "");
									shipdetailObj.put("ship_zip", "");
									shipdetailObj.put("ship_days", "");
									shipdetailObj.put("address_count", "");
									payDataArr.put("ship_details",shipdetailObj);
									
									JSONObject itemdetailObj = new JSONObject();
									itemdetailObj.put("item_count", "");
									itemdetailObj.put("item_value", "");
									itemdetailObj.put("item_category", "");
									itemdetailObj.put("ship_country", "");
									payDataArr.put("item_details",itemdetailObj);
									
									JSONObject otherdetailObj = new JSONObject();
									otherdetailObj.put("udf_1", Commonutility.stringToStringempty(payGateObj.getUdf_1Data()));
									otherdetailObj.put("udf_2", Commonutility.stringToStringempty(payGateObj.getUdf_2Data()));
									otherdetailObj.put("udf_3", Commonutility.stringToStringempty(payGateObj.getUdf_3Data()));
									otherdetailObj.put("udf_4", Commonutility.stringToStringempty(payGateObj.getUdf_4Data()));
									otherdetailObj.put("udf_5", Commonutility.stringToStringempty(payGateObj.getUdf_5Data()));
									payDataArr.put("other_details",otherdetailObj);
									
									paymentCallObj.put("paydata",payDataArr);
									paymentCallObj.put("transaction_url", easypayDaoObj.getConfigDetails("payurl.transaction.test"));
									response = paymentCallObj.toString().trim();
									System.out.println("Payment call response------------------"+response);
									//response=EncDecrypt.decrypt(response);
									System.out.println("Payment call Encrypt-----------------"+EncDecrypt.encrypt(response));
									payHttpurl = getText("SOCIAL_INDIA_BASE_URL") + getText("paygate.url")+EncDecrypt.encrypt(response);
									System.out.println("Payment call 111111111111111111----------------" + payHttpurl);
									//return "success";
								} else {
									/* Error msg */
									rescode = "R0223";
									rescodeStsMsg = validStausMsg;
									remarksMsg = validMessage;
									flg = false;
								}
							} else {
								rescode = "R0223";
								rescodeStsMsg = "Failed";
								remarksMsg = mobiCommon.getMsg("R0020");
								flg = false;
							}
						  } else if (paymentType == 2) {
							  if (paymGateid != -1) {
									String response="";
									payGateObj = new MvpPaygateTbl();
									payGateObj = easypayDaoObj.getPaygateDetails(paymGateid,rid);
									
									if (payGateObj != null && Commonutility.stringToStringempty(payGateObj.getStatus()) == "" && locPaidStatus.equalsIgnoreCase("0") ) { 									
										JSONObject payDataArr = new JSONObject();
										JSONObject txndetailObj = new JSONObject();
										paymentCallObj.put("request","Transaction");
										
										txndetailObj.put("ag_id", payGateObj.getAgId());
										txndetailObj.put("me_id", payGateObj.getMeId());
										txndetailObj.put("order_no", payGateObj.getOrderNo());
										if (payGateObj.getAmount() != 0) {
											float priceAmt = payGateObj.getAmount();
											DecimalFormat df = new DecimalFormat("#.00");
											df.setMaximumFractionDigits(2);
											txndetailObj.put("Amount", Commonutility.toCheckNullEmpty(df.format(priceAmt)));
										} else if (payGateObj.getAmount() == 0 || payGateObj.getAmount() == 0.0) {
											txndetailObj.put("Amount", "0");
										} else {
											txndetailObj.put("Amount", "");
										}
										txndetailObj.put("Country", payGateObj.getCountry());
										txndetailObj.put("Currency", payGateObj.getCurrency());
										txndetailObj.put("txn_type", payGateObj.getTxnType());
										txndetailObj.put("success_url", payGateObj.getSuccessUrl());
										txndetailObj.put("failure_url", payGateObj.getFailureUrl());
										txndetailObj.put("Channel", payGateObj.getChannel());
										payDataArr.put("txn_details",txndetailObj);
										
										JSONObject pgdetailObj = new JSONObject();
										pgdetailObj.put("pg_id", "");
										pgdetailObj.put("Paymode", payMode);
										pgdetailObj.put("Scheme", "");
										pgdetailObj.put("emi_months", "");
										payDataArr.put("pg_details",pgdetailObj);
										
										JSONObject cardDetailObj = new JSONObject();
										cardDetailObj.put("card_no", "");
										cardDetailObj.put("exp_month", "");
										cardDetailObj.put("exp_year", "");
										cardDetailObj.put("cvv2", "");
										cardDetailObj.put("card_name", "");
										payDataArr.put("card_details",cardDetailObj);
										
										JSONObject custdetailObj = new JSONObject();
										custdetailObj.put("cust_name", "");
										custdetailObj.put("email_id", "");
										custdetailObj.put("mobile_no", "");
										custdetailObj.put("unique_id", "");
										custdetailObj.put("is_logged_in", "");
										payDataArr.put("cust_details",custdetailObj);
										
										JSONObject billdetailObj = new JSONObject();
										billdetailObj.put("bill_address", "");
										billdetailObj.put("bill_city", "");
										billdetailObj.put("bill_state", "");
										billdetailObj.put("bill_country", "");
										billdetailObj.put("bill_zip", "");
										payDataArr.put("bill_details",billdetailObj);
										
										JSONObject shipdetailObj = new JSONObject();
										shipdetailObj.put("ship_address", "");
										shipdetailObj.put("ship_city", "");
										shipdetailObj.put("ship_state", "");
										shipdetailObj.put("ship_country", "");
										shipdetailObj.put("ship_zip", "");
										shipdetailObj.put("ship_days", "");
										shipdetailObj.put("address_count", "");
										payDataArr.put("ship_details",shipdetailObj);
										
										JSONObject itemdetailObj = new JSONObject();
										itemdetailObj.put("item_count", "");
										itemdetailObj.put("item_value", "");
										itemdetailObj.put("item_category", "");
										itemdetailObj.put("ship_country", "");
										payDataArr.put("item_details",itemdetailObj);
										
										JSONObject otherdetailObj = new JSONObject();
										otherdetailObj.put("udf_1", Commonutility.stringToStringempty(payGateObj.getUdf_1Data()));
										otherdetailObj.put("udf_2", Commonutility.stringToStringempty(payGateObj.getUdf_2Data()));
										otherdetailObj.put("udf_3", Commonutility.stringToStringempty(payGateObj.getUdf_3Data()));
										otherdetailObj.put("udf_4", Commonutility.stringToStringempty(payGateObj.getUdf_4Data()));
										otherdetailObj.put("udf_5", Commonutility.stringToStringempty(payGateObj.getUdf_5Data()));
										payDataArr.put("other_details",otherdetailObj);
										
										paymentCallObj.put("paydata",payDataArr);
										paymentCallObj.put("transaction_url", easypayDaoObj.getConfigDetails("payurl.transaction.test"));
										response = paymentCallObj.toString().trim();
										System.out.println("Payment call response------------------"+response);
										//response=EncDecrypt.decrypt(response);
										System.out.println("Payment call Encrypt-----------------"+EncDecrypt.encrypt(response));
										payHttpurl = getText("SOCIAL_INDIA_BASE_URL") + getText("paygate.url")+EncDecrypt.encrypt(response);
										System.out.println("Payment call 111111111111111111----------------" + payHttpurl);
										//return "success";
									} else {
										/* Error msg */
										String paidstsMsg = "";
										if (locPaidStatus.equalsIgnoreCase("1")) {
											paidstsMsg = "Already paid";
										} else {
											paidstsMsg = "Data updation error";
										}
										rescode = "R0223";
										rescodeStsMsg = "Failed";
										remarksMsg = paidstsMsg;
										flg = false;
									}
								} else {
									rescode = "R0223";
									rescodeStsMsg = "Failed";
									remarksMsg = mobiCommon.getMsg("R0020");
									flg = false;
								}
						  }
						} else {
							rescode = "R0223";
							rescodeStsMsg = "Failed";
							remarksMsg = mobiCommon.getMsg("R0020");
							flg = false;
						}		
						System.out.println("First before flg execute genorderno---------" + genorderno);
						if (flg == false) {
							MvpTransactionTbl transRemarks = new MvpTransactionTbl();
							transRemarks.setOrderNo(genorderno);
							transRemarks.setRemarksMsg(remarksMsg);
							boolean remarkUpdate = easypayDaoObj.transRemarksUpdate(transRemarks,"ON");
							
							locObjRspdataJson=new JSONObject();
							MvpTransactionTbl transActgetData = new MvpTransactionTbl();
							System.out.println("First after flg execute genorderno---------" + genorderno);
							transActgetData.setOrderNo(genorderno);
							transActgetData = easypayDaoObj.transReturnResponse(transActgetData);
							locObjRspdataJson = jsonSuccessErrorFormation(rescodeStsMsg,transActgetData.getOrderNo(),transActgetData.getTxnAmount(),
									transActgetData.getServiceType(),"",transActgetData.getTxnDatetime(),"",transActgetData.getRemarksMsg());
							/*locObjRspdataJson.put("payment_status", rescodeStsMsg);
							locObjRspdataJson.put("reference_no", Commonutility.stringToStringempty(transActgetData.getOrderNo()));
							if (transActgetData.getTxnAmount() != 0) {
								float priceAmt = transActgetData.getTxnAmount();
								DecimalFormat df = new DecimalFormat("#.00");
								df.setMaximumFractionDigits(2);
								locObjRspdataJson.put("amount",Commonutility.toCheckNullEmpty(df.format(priceAmt)));
							} else if (transActgetData.getTxnAmount() == 0 || transActgetData.getTxnAmount() == 0.0) {
								locObjRspdataJson.put("amount","0");
							} else {
								locObjRspdataJson.put("amount","");
							}
							locObjRspdataJson.put("service", Commonutility.intToString(transActgetData.getServiceType()));
							locObjRspdataJson.put("bank_details", "");
							if (transActgetData.getTxnDatetime() != null) {
								DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
						        DateFormat time = new SimpleDateFormat("hh:mm:ss");
						        locObjRspdataJson.put("transaction_date", date.format(transActgetData.getTxnDatetime()));
								locObjRspdataJson.put("transaction_time", time.format(transActgetData.getTxnDatetime()));
							} else {
								locObjRspdataJson.put("transaction_date", "");
								locObjRspdataJson.put("transaction_time", "");
							}
							locObjRspdataJson.put("payment_mode", "");*/
							responseData = finalMobiResponse(locObjRspdataJson);
							request.setAttribute("responseData", responseData);
							return "input";
						}
						
					} else {
						rescode = "R0223";
						rescodeStsMsg = "Failed";
						remarksMsg = mobiCommon.getMsg("R0026");
						flg = false;
					}
				} else {
					rescode = "R0223";
					rescodeStsMsg = "Failed";
					remarksMsg = mobiCommon.getMsg("R0015");
					flg = false;
				}								
				
			} else {
				rescode = "R0223";
				rescodeStsMsg = "Failed";
				remarksMsg = mobiCommon.getMsg("R0002");
				flg = false;
			}
			if (flg == false) {
				locObjRspdataJson = new JSONObject();
				locObjRspdataJson = jsonSuccessErrorFormation(rescodeStsMsg,"",Commonutility.stringToFloat(locAmount),
						Commonutility.stringToInteger(locBiller), "",null, "",remarksMsg);
				responseData = finalMobiResponse(locObjRspdataJson);
				request.setAttribute("responseData", responseData);
				return "input";
			}
		} catch (Exception ex) {
			log.logMessage("Exception found in EasyPayGateway execute() : "+ex, "error", EasyPayGateway.class);
			if (flg == false) {
				locObjRspdataJson = new JSONObject();
				locObjRspdataJson = jsonSuccessErrorFormation(rescodeStsMsg,"",Commonutility.stringToFloat(locAmount),
						Commonutility.stringToInteger(locBiller), "",null, "",remarksMsg);
				responseData = finalMobiResponse(locObjRspdataJson);
				request.setAttribute("responseData", responseData);
				return "input";
			}
		} finally {			
		}
		return SUCCESS;
	}
	
	public String payGateResponseFunc() {
		redirectUrl = getText("SOCIAL_INDIA_BASE_URL") + getText("paymentRes.url");
		JSONObject locObjRspdataJson = null; //Response
		HttpServletRequest request = ServletActionContext.getRequest();
		boolean flg = true;
		String remarksMsg = "";
		String rescode = "";
		String rescodeStsMsg = "";
		String redirectResponseMsg = "";
		try {
			JSONObject cyperCallObj = new JSONObject();
			EasyPaymentDao easypayDaoObj = new EasyPaymentDaoServices();
			System.out.println("request---finaldata------payGateResponseFunc---------------" + finaldata);
			if (Commonutility.checkempty(finaldata)) {
				finaldata = finaldata.replace(" ", "+");
				finaldata = EncDecrypt.decrypt(finaldata);
				log.logMessage("finaldata :" + finaldata, "info", EasyPayGateway.class);
				boolean paygateResJson = Commonutility.toCheckIsJSON(finaldata);
				JSONObject paygateRecvJson = null;
				JSONObject paygateTxnRes = null;
				JSONObject payGatepgdetails = null;
				JSONObject payGatefrauddetails = null;
				JSONObject payGateotherdetails = null;
				System.out.println("request--=================1-");
				String payGateFinalstsRes = null;
				if (paygateResJson) {
					System.out.println("request--=================2-");
					paygateRecvJson = new JSONObject(finaldata);
					paygateTxnRes = (JSONObject) Commonutility.toHasChkJsonRtnValObj(paygateRecvJson,"txn_response");
					payGatepgdetails = (JSONObject) Commonutility.toHasChkJsonRtnValObj(paygateRecvJson,"pg_details");
					payGatefrauddetails = (JSONObject) Commonutility.toHasChkJsonRtnValObj(paygateRecvJson,"fraud_details");
					payGateotherdetails = (JSONObject) Commonutility.toHasChkJsonRtnValObj(paygateRecvJson,"other_details");
					payGateFinalstsRes = (String) Commonutility.toHasChkJsonRtnValObj(paygateRecvJson,"final_status");
					String resAgid = "";String resMeid = "";String resOrderno = "";String resAmount = "";String resCountry = "";
					String resCurrency = "";String resTxndate = "";String resTxntime = "";String resAgref = "";String resPgref = "";
					String resStatus = "";String resResCode = "";String resMessage = "";
					if (paygateTxnRes != null) {
						System.out.println("request--=================3-");
						resAgid = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"ag_id");
						resMeid = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"me_id");
						resOrderno = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"order_no");
						resAmount = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"Amount");
						resCountry = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"Country");
						resCurrency = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"Currency");
						resTxndate = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"txn_date");
						resTxntime = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"txn_time");
						resAgref = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"ag_ref");
						resPgref = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"pg_ref");
						resStatus = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"Status");
						resResCode = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"res_code");
						resMessage = (String) Commonutility.toHasChkJsonRtnValObj(paygateTxnRes,"res_message");
					}
					System.out.println("request--=================4-");
					String pgResPgid = "";String pgResPgName = "";String pgResPaymode = "";String pgResEmi = "";
					String pgCondata = "";
					if (payGatepgdetails != null) {
						pgResPgid = (String) Commonutility.toHasChkJsonRtnValObj(payGatepgdetails,"pg_id");
						pgResPgName = (String) Commonutility.toHasChkJsonRtnValObj(payGatepgdetails,"pg_name");
						pgResPaymode = (String) Commonutility.toHasChkJsonRtnValObj(payGatepgdetails,"Paymode");
						pgResEmi = (String) Commonutility.toHasChkJsonRtnValObj(payGatepgdetails,"emi_months");
						pgCondata = pgResPgid+"!_!"+pgResPgName+"!_!"+pgResPaymode+"!_!"+pgResEmi;
					}
					String fraudActRes = "";String fraudMessRes = "";String fraudScoreRes = "";
					String fraudConcat = "";
					if (payGatefrauddetails != null) {
						fraudActRes = (String) Commonutility.toHasChkJsonRtnValObj(payGatefrauddetails,"fraud_action");
						fraudMessRes = (String) Commonutility.toHasChkJsonRtnValObj(payGatefrauddetails,"fraud_message");
						fraudScoreRes = (String) Commonutility.toHasChkJsonRtnValObj(payGatefrauddetails,"score");
						fraudConcat = fraudActRes+"!_!"+fraudMessRes+"!_!"+fraudScoreRes;
					}
					String udf1Res = "";String udf2Res = "";String udf3Res = "";String udf4Res = "";String udf5Res = "";
					String otherConcat = "";
					if (payGateotherdetails != null) {
						udf1Res = (String) Commonutility.toHasChkJsonRtnValObj(payGateotherdetails,"udf_1");
						udf2Res = (String) Commonutility.toHasChkJsonRtnValObj(payGateotherdetails,"udf_2");
						udf3Res = (String) Commonutility.toHasChkJsonRtnValObj(payGateotherdetails,"udf_3");
						udf4Res = (String) Commonutility.toHasChkJsonRtnValObj(payGateotherdetails,"udf_4");
						udf5Res = (String) Commonutility.toHasChkJsonRtnValObj(payGateotherdetails,"udf_5");
						otherConcat = udf1Res+"!_!"+udf2Res+"!_!"+udf3Res+"!_!"+udf4Res+"!_!"+udf5Res;
					}
					
					MvpPaygateTbl payGateUpdateObj = new MvpPaygateTbl();
					System.out.println("request--=================6-");
					payGateUpdateObj.setOrderNo(resOrderno);
					payGateUpdateObj.setTxnDate(resTxndate);
					payGateUpdateObj.setTxnTime(resTxntime);
					payGateUpdateObj.setAgRef(resAgref);
					payGateUpdateObj.setPgRef(resPgref);
					payGateUpdateObj.setStatus(resStatus);
					payGateUpdateObj.setResCode(resResCode);
					payGateUpdateObj.setResMessage(resMessage);
					payGateUpdateObj.setFinalStatus(Commonutility.stringToInteger(payGateFinalstsRes)); //Payment success
					payGateUpdateObj.setPgDetails(pgCondata);
					payGateUpdateObj.setFraudDetails(fraudConcat);
					payGateUpdateObj.setOtherDetails(otherConcat);
					payGateUpdateObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
					boolean paygateUpdateRes = easypayDaoObj.payGateUpdate(payGateUpdateObj);
					
					MvpTransactionTbl transActUpdateObj = new MvpTransactionTbl();
					transActUpdateObj.setOrderNo(resOrderno);
					transActUpdateObj.setSession(resOrderno);
					transActUpdateObj.setPamentGatewayStatus(Commonutility.stringToInteger(payGateFinalstsRes));
					transActUpdateObj.setFinalStatus(Commonutility.stringToInteger(payGateFinalstsRes)); //Payment success
					transActUpdateObj.setModifyDateTime(Commonutility.enteyUpdateInsertDateTime());
					boolean transUpdateRes = easypayDaoObj.transactionUpdate(transActUpdateObj);
					System.out.println("request--=================5-");
					if (resStatus.equalsIgnoreCase("Successful") && payGateFinalstsRes.equalsIgnoreCase("0")) {
						
						System.out.println("request--=================7-");
						if (paygateUpdateRes == true && transUpdateRes == true) {
							
								MvpTransactionTbl transActgetData = new MvpTransactionTbl();
								transActgetData.setSession(resOrderno);
								//transActgetData.setFinalStatus(3);
								transActgetData = easypayDaoObj.transGetFinalResponse(transActgetData);
								if (transActgetData != null && transActgetData.getPaymentType() == 2) { //Maintenance pay success
									
									boolean lvrfunmtr=false;
									Common common=new CommonDao();
									String updatesql ="update MaintenanceFeeTblVO set paidStatus='1' where maintenanceId="+transActgetData.getMaintenanceId()+" and statusFlag=1";
									lvrfunmtr = common.commonUpdate(updatesql);
									locObjRspdataJson=new JSONObject();
									locObjRspdataJson = jsonSuccessErrorFormation("Success",transActgetData.getOrderNo(),transActgetData.getTxnAmount(),
											transActgetData.getServiceType(),"",transActgetData.getTxnDatetime(),"",transActgetData.getRemarksMsg());
									responseData = finalMobiResponse(locObjRspdataJson);
									System.out.println("responseData Maintenance easypayres------->>-" + responseData);
							        request.setAttribute("responseData", responseData);
							        redirectResponseMsg = "input";
								} else { //Utility pay success
									
								MvpTransactionTbl transActSecondUpdate = new MvpTransactionTbl();
								transActSecondUpdate.setOrderNo(resOrderno);
								transActSecondUpdate.setSession(resOrderno); //utility session
								transActSecondUpdate.setUtilityPaymentStatus(1); //Pending
								transActSecondUpdate.setFinalStatus(Commonutility.stringToInteger(payGateFinalstsRes)); //Cyperplate call
								transActSecondUpdate.setModifyDateTime(Commonutility.enteyUpdateInsertDateTime());
								boolean transSecondUpdateRes = easypayDaoObj.transactionSecondUpdate(transActSecondUpdate);
								System.out.println("request--=================8-");
								
								
								MvpUtilityPayTbl utilityPayObj = new MvpUtilityPayTbl();
								utilityPayObj = easypayDaoObj.getUtilityPayDetails(resOrderno);
								System.out.println("request--=================9-");
								

								String cyperResponse="";
								String serviceTypNm = "";
								if (transActgetData.getServiceType() == 1) {
									serviceTypNm = "PREPAID";
								} else if (transActgetData.getServiceType() == 2) {
									serviceTypNm = "DTH";
								} else if (transActgetData.getServiceType() == 3) {
									serviceTypNm = "PREPAID DATACARD";
								} else if (transActgetData.getServiceType() == 4) {
									serviceTypNm = "POSTPAID";
								} else if (transActgetData.getServiceType() == 5) {
									serviceTypNm = "LANDLINE";
								} else if (transActgetData.getServiceType() == 6) {
									serviceTypNm = "ELECTRICITY";
								} else if (transActgetData.getServiceType() == 7) {
									serviceTypNm = "GAS BILL";
								} else if (transActgetData.getServiceType() == 8) {
									serviceTypNm = "INSANCE";
								} else if (transActgetData.getServiceType() == 9) {
									serviceTypNm = "BROADBAND";
								} else if (transActgetData.getServiceType() == 10) {
									serviceTypNm = "DIRECT MONEY TRANSFER";
								} else if (transActgetData.getServiceType() == 11) {
									serviceTypNm = "POSTPAID DATACARD";
								} else {
									serviceTypNm = "";
								}
								cyperCallObj.put("PAYFOR",serviceTypNm);
								//JSONObject cyperpayArr = new JSONObject();
								
								if (utilityPayObj != null) {
									System.out.println("request--=================10-");
									JSONObject cyperpaydetailObj = new JSONObject();
									cyperpaydetailObj.put("SD", Commonutility.stringToStringempty(utilityPayObj.getSd()));
									cyperpaydetailObj.put("AP", Commonutility.stringToStringempty(utilityPayObj.getAp()));
									cyperpaydetailObj.put("OP", Commonutility.stringToStringempty(utilityPayObj.getOp()));
									cyperpaydetailObj.put("SESSION", Commonutility.stringToStringempty(utilityPayObj.getSession()));
									cyperpaydetailObj.put("NUMBER", Commonutility.stringToStringempty(utilityPayObj.getNumber()));
									cyperpaydetailObj.put("ACCOUNT", Commonutility.stringToStringempty(utilityPayObj.getAccount()));
									if (utilityPayObj.getAmount() != 0) {
										float priceAmt = utilityPayObj.getAmount();
										DecimalFormat df = new DecimalFormat("#.00");
										df.setMaximumFractionDigits(2);
										cyperpaydetailObj.put("AMOUNT", Commonutility.toCheckNullEmpty(df.format(priceAmt)));
									} else if (utilityPayObj.getAmount() == 0 || utilityPayObj.getAmount() == 0.0) {
										cyperpaydetailObj.put("AMOUNT", "0");
									} else {
										cyperpaydetailObj.put("AMOUNT", "");
									}
									cyperpaydetailObj.put("AMOUNT_ALL", Commonutility.stringToStringempty(utilityPayObj.getAmountAll()));
									cyperpaydetailObj.put("COMMENT", Commonutility.stringToStringempty(utilityPayObj.getComment()));
									cyperpaydetailObj.put("TERM_ID",Commonutility.stringToStringempty(utilityPayObj.getTermId()));
									cyperpaydetailObj.put("Authenticator3",Commonutility.stringToStringempty(utilityPayObj.getAuthenticator3()));
									CyberplatoptrsTblVo cyperOperObj = new CyberplatoptrsTblVo();
									cyperOperObj = easypayDaoObj.getCyperPlateOperator(transActgetData.getServiceType(),utilityPayObj.getOperatorCodeId());
									if (cyperOperObj != null) {
										cyperpaydetailObj.put("VALIDATE_URL", cyperOperObj.getIvrBnVALIDATE_URL());
										cyperpaydetailObj.put("PAYMENT_URL", cyperOperObj.getIvrBnPAYMENT_URL());
										cyperpaydetailObj.put("STATUS_URL", cyperOperObj.getIvrBnSTATUS_CHECK_URL());
									} else {
										cyperpaydetailObj.put("VALIDATE_URL", "");
										cyperpaydetailObj.put("PAYMENT_URL", "");
										cyperpaydetailObj.put("STATUS_URL", "");
									}
									//cyperpayArr.put(cyperpaydetailObj);
									System.out.println("request--=================11-");
									cyperCallObj.put("paydata",cyperpaydetailObj);
									cyperResponse = cyperCallObj.toString();
									System.out.println("Cyper plate call response------------------"+cyperResponse);
									cyperResponse=EncDecrypt.encrypt(cyperResponse);
									cyperPlateurl = getText("SOCIAL_INDIA_BASE_URL") + getText("cyperplate.url")+cyperResponse;
									System.out.println("request-cyperPlateurl-=================12-" + cyperPlateurl);
									redirectResponseMsg = "success";
									//return "success";
								} else {
									/* error msg*/
									rescode = "R0223";
									rescodeStsMsg = "Failed";
									remarksMsg = mobiCommon.getMsg("R0026");
									flg = false;
								}
								
							}
								
						} else {
							/* error msg*/
							rescode = "R0223";
							rescodeStsMsg = "Failed";
							remarksMsg = mobiCommon.getMsg("R0026");
							flg = false;
						}
						
					} else if (payGateFinalstsRes.equalsIgnoreCase("1")) {
						rescode = "R0222";
						rescodeStsMsg = "Pending";
						remarksMsg = resMessage;
						flg = false;
					} else if (payGateFinalstsRes.equalsIgnoreCase("2")) {
						rescode = "R0223";
						rescodeStsMsg = "Failed";
						remarksMsg = resMessage;
						flg = false;
					} else if (payGateFinalstsRes.equalsIgnoreCase("3")) {
						rescode = "R0223";
						rescodeStsMsg = "Error";
						remarksMsg = resMessage;
						flg = false;
					} else {
						/* Error msg*/
						rescode = "R0223";
						rescodeStsMsg = "Failed";
						remarksMsg = resMessage;
						flg = false;
					}
					System.out.println("Second before flg execute resOrderno---------" + resOrderno);
					if (flg == false) {
						MvpTransactionTbl transRemarks = new MvpTransactionTbl();
						transRemarks.setSession(resOrderno);
						transRemarks.setRemarksMsg(Commonutility.stringToStringempty(remarksMsg));
						boolean remarkUpdate = easypayDaoObj.transRemarksUpdate(transRemarks,"SN");
						
						locObjRspdataJson=new JSONObject();
						MvpTransactionTbl transActgetData = new MvpTransactionTbl();
						transActgetData.setSession(resOrderno);
						System.out.println("Second After flg execute resOrderno---------" + resOrderno);
						transActgetData = easypayDaoObj.transGetFinalResponse(transActgetData);
						locObjRspdataJson = jsonSuccessErrorFormation(rescodeStsMsg,transActgetData.getOrderNo(),transActgetData.getTxnAmount(),
								transActgetData.getServiceType(),"",transActgetData.getTxnDatetime(),"",transActgetData.getRemarksMsg());
						/*locObjRspdataJson.put("payment_status", rescodeStsMsg);
						locObjRspdataJson.put("reference_no", Commonutility.stringToStringempty(transActgetData.getOrderNo()));
						if (transActgetData.getTxnAmount() != 0) {
							float priceAmt = transActgetData.getTxnAmount();
							DecimalFormat df = new DecimalFormat("#.00");
							df.setMaximumFractionDigits(2);
							locObjRspdataJson.put("amount",Commonutility.toCheckNullEmpty(df.format(priceAmt)));
						} else if (transActgetData.getTxnAmount() == 0 || transActgetData.getTxnAmount() == 0.0) {
							locObjRspdataJson.put("amount","0");
						} else {
							locObjRspdataJson.put("amount","");
						}
						locObjRspdataJson.put("service", Commonutility.intToString(transActgetData.getServiceType()));
						locObjRspdataJson.put("bank_details", "");
						if (transActgetData.getTxnDatetime() != null) {
							DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
					        DateFormat time = new SimpleDateFormat("hh:mm:ss");
					        locObjRspdataJson.put("transaction_date", date.format(transActgetData.getTxnDatetime()));
							locObjRspdataJson.put("transaction_time", time.format(transActgetData.getTxnDatetime()));
						} else {
							locObjRspdataJson.put("transaction_date", "");
							locObjRspdataJson.put("transaction_time", "");
						}
						locObjRspdataJson.put("payment_mode", "");*/
						responseData = finalMobiResponse(locObjRspdataJson);
						request.setAttribute("responseData", responseData);
						return "input";
					}
				}
			} else {
				/* Error msg*/				
			}
		} catch (Exception ex) {
			log.logMessage("Exception in payGateResponseFunc" + ex, "error", EasyPayGateway.class);
			if (flg == false) {
				locObjRspdataJson = new JSONObject();
				locObjRspdataJson = jsonSuccessErrorFormation("Failed","",Commonutility.stringToFloat(locAmount),
						Commonutility.stringToInteger(locBiller), "",null, "",mobiCommon.getMsg("R0003"));
				responseData = finalMobiResponse(locObjRspdataJson);
				request.setAttribute("responseData", responseData);
				return "input";
			}
		}
		return redirectResponseMsg;
	}
	
	
	public String cyperPlateResponseFunc() {
		redirectUrl = getText("SOCIAL_INDIA_BASE_URL") + getText("paymentRes.url");
		JSONObject locObjRspdataJson = null; //Response
		HttpServletRequest request = ServletActionContext.getRequest();
		boolean flg = true;
		String remarksMsg = "";
		String rescode = "";
		String rescodeStsMsg = "";
		try {
			EasyPaymentDao easypayDaoObj = new EasyPaymentDaoServices();
			System.out.println("request---finaldata------cyperPlateResponseFunc---------------" + finaldata);
			if (Commonutility.checkempty(finaldata)) {
				finaldata = finaldata.replace(" ", "+");
				finaldata = EncDecrypt.decrypt(finaldata);
				Commonutility.toWriteConsole("Before replacce :: "+finaldata);								
//				finaldata = finaldata.replace("\\\"", "\\\\\\\"");								
				finaldata = finaldata.replace("\\r", "<SP>");
				Commonutility.toWriteConsole("After replacce \\r :: "+finaldata);
				finaldata = finaldata.replace("\\n", "");
				log.logMessage("finaldata :" + finaldata, "info", EasyPayGateway.class);
				boolean paygateResJson = Commonutility.toCheckIsJSON(finaldata);
				System.out.println("request--=================13-");
				JSONObject cyperRecvJson = null;
				JSONObject cyperResValue = null;
				String cyperRequest = null;
				String cyperStsRes = null;
				String cypervalidateRes = null;
				String paygateRes = null;
				String cyperFinalstsRes = null;
				int utilPayLogId = -1;
				boolean utilityUpdate = false;
				boolean transUpdatethirdRes = false;
				if (paygateResJson) {
					System.out.println("request-finaldata-=================14-" + finaldata);
					cyperRecvJson = new JSONObject(finaldata);
					System.out.println("request--=================14====+++++++++-" + cyperRecvJson.toString());
					cyperResValue = (JSONObject) Commonutility.toHasChkJsonRtnValObj(cyperRecvJson,"response_value");
					System.out.println("request--=================1ee-");
					cyperRequest = (String) Commonutility.toHasChkJsonRtnValObj(cyperRecvJson,"request");
					System.out.println("request--=================1rr-");
					cyperStsRes = (String) Commonutility.toHasChkJsonRtnValObj(cyperRecvJson,"status_gateway_response");
					System.out.println("request--=================188");
					cypervalidateRes = (String) Commonutility.toHasChkJsonRtnValObj(cyperRecvJson,"validate_gateway_response");
					paygateRes = (String) Commonutility.toHasChkJsonRtnValObj(cyperRecvJson,"pay_gateway_response");
					cyperFinalstsRes = (String) Commonutility.toHasChkJsonRtnValObj(cyperRecvJson,"final_status");
					String cyperDate = "";String cyperSession = "";String cyperError = "";String cyperResult = "";String cyperTransid = "";
					String cyperPhoneno = "";String cyperOperatorid = "";String cyperAccount = "";String cyperValue1 = "";String cyperAuthcode = "";
					String cyperAddInfo = "";String cyperPrice = "";String cyperBilldate = "";String cyperBillNo = "";String cyperErrmsg = "";
					String cyperTxnsts = "";
					if (cyperResValue != null) {
						System.out.println("request--=================15-");
						cyperDate = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"DATE");
						cyperSession = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"SESSION");
						cyperError = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"ERROR");
						cyperResult = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"RESULT");
						cyperTransid = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"TRANSID");
						cyperPhoneno = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"PHONENUMBER");
						cyperOperatorid = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"OPERATORID");
						cyperAccount = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"ACCOUNT");
						cyperValue1 = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"VALUE1");
						cyperAuthcode = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"AUTHCODE");
						cyperAddInfo = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"ADDINFO");
						cyperPrice = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"PRICE");
						cyperBilldate = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"BILLDATE");
						cyperBillNo = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"BILLNUMBER");
						cyperErrmsg = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"ERRMSG");
						cyperTxnsts = (String) Commonutility.toHasChkJsonRtnValObj(cyperResValue,"TRNXSTATUS");
					}
					
					MvpUtilityPayTbl utilityPayUpdaeObj = new MvpUtilityPayTbl();
					System.out.println("request--=================16-");
					utilityPayUpdaeObj.setDate(cyperDate);
					utilityPayUpdaeObj.setSession(cyperSession);
					utilityPayUpdaeObj.setError(cyperError);
					utilityPayUpdaeObj.setResult(cyperResult);
					utilityPayUpdaeObj.setTransid(cyperTransid);
					utilityPayUpdaeObj.setPhonenumber(cyperPhoneno);
					utilityPayUpdaeObj.setCircle(cyperOperatorid);
					utilityPayUpdaeObj.setAccount(cyperAccount);
					utilityPayUpdaeObj.setAuthcode(cyperAuthcode);
					utilityPayUpdaeObj.setAddinfo(cyperAddInfo);
					utilityPayUpdaeObj.setPrice(cyperPrice);
					utilityPayUpdaeObj.setBilldate(cyperBilldate);
					utilityPayUpdaeObj.setBillnumber(cyperBillNo);
					utilityPayUpdaeObj.setErrmsg(cyperErrmsg);
					utilityPayUpdaeObj.setTrnxstatus(cyperTxnsts);
					utilityPayUpdaeObj.setFinalStatus(Commonutility.stringToInteger(cyperFinalstsRes));
					//utilityPayUpdaeObj.setUsrId(Commonutility.stringToInteger(locRid));
					utilityPayUpdaeObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
					utilityUpdate = easypayDaoObj.utilityPayUpdate(utilityPayUpdaeObj); 
					
					MvpUtilityPayTbl utilityPayObj = new MvpUtilityPayTbl();
					utilityPayObj = easypayDaoObj.getUtilityPayDetails(cyperSession);
					MvpUtilityPayLogTbl utilpayLogObj = new MvpUtilityPayLogTbl();
					if (Commonutility.checkempty(cyperStsRes)) {
						utilpayLogObj.setSResponseLog(cyperStsRes);
					} else {
						cyperStsRes = null;
						utilpayLogObj.setSResponseLog(cyperStsRes);
					}
					if (Commonutility.checkempty(cypervalidateRes)) {
						utilpayLogObj.setVResponseLog(cypervalidateRes);
					} else {
						cypervalidateRes = null;
						utilpayLogObj.setVResponseLog(cypervalidateRes);
					}
					if (Commonutility.checkempty(paygateRes)) {
						utilpayLogObj.setPResponseLog(paygateRes);
					} else {
						paygateRes = null;
						utilpayLogObj.setPResponseLog(paygateRes);
					}
					utilpayLogObj.setUtilityPayId(utilityPayObj.getUtilityPayId());
					utilpayLogObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
					utilpayLogObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
					utilPayLogId = easypayDaoObj.payLogInsert(utilpayLogObj);
					
					MvpTransactionTbl transActthirdUpdate = new MvpTransactionTbl();
					transActthirdUpdate.setSession(cyperSession);
					transActthirdUpdate.setUtilityPaymentStatus(Commonutility.stringToInteger(cyperFinalstsRes));
					transActthirdUpdate.setFinalStatus(Commonutility.stringToInteger(cyperFinalstsRes)); //cyperplate success
					transActthirdUpdate.setRemarksMsg(Commonutility.stringToStringempty(cyperErrmsg));
					transActthirdUpdate.setModifyDateTime(Commonutility.enteyUpdateInsertDateTime());
					transUpdatethirdRes = easypayDaoObj.transactionThirdUpdate(transActthirdUpdate);	
					
					if (cyperFinalstsRes.equalsIgnoreCase("0")) {
											
						System.out.println("request--=================17-");
						if (utilityUpdate && utilPayLogId !=-1 && transUpdatethirdRes) {
							transActthirdUpdate = new MvpTransactionTbl();
							System.out.println("cyperSession-------------" + cyperSession);
							transActthirdUpdate.setSession(cyperSession);
							//transActthirdUpdate.setFinalStatus(4);
							transActthirdUpdate = easypayDaoObj.transGetFinalResponse(transActthirdUpdate);
						}
						if (transActthirdUpdate != null) {
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson = jsonSuccessErrorFormation("Success",transActthirdUpdate.getOrderNo(),transActthirdUpdate.getTxnAmount(),
									transActthirdUpdate.getServiceType(),"",transActthirdUpdate.getTxnDatetime(),"",transActthirdUpdate.getRemarksMsg());
							/*locObjRspdataJson.put("payment_status", "Success");
							locObjRspdataJson.put("reference_no", Commonutility.stringToStringempty(transActthirdUpdate.getOrderNo()));
							if (transActthirdUpdate.getTxnAmount() != 0) {
								float priceAmt = transActthirdUpdate.getTxnAmount();
								DecimalFormat df = new DecimalFormat("#.00");
								df.setMaximumFractionDigits(2);
								locObjRspdataJson.put("amount",Commonutility.toCheckNullEmpty(df.format(priceAmt)));
							} else if (transActthirdUpdate.getTxnAmount() == 0 || transActthirdUpdate.getTxnAmount() == 0.0) {
								locObjRspdataJson.put("amount","0");
							} else {
								locObjRspdataJson.put("amount","");
							}
							locObjRspdataJson.put("service", Commonutility.intToString(transActthirdUpdate.getServiceType()));
							locObjRspdataJson.put("bank_details", "");
							if (transActthirdUpdate.getTxnDatetime() != null) {
								DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
						        DateFormat time = new SimpleDateFormat("hh:mm:ss");
						        System.out.println("Date: " + date.format(transActthirdUpdate.getTxnDatetime()));
						        System.out.println("Time: " + time.format(transActthirdUpdate.getTxnDatetime()));
						        locObjRspdataJson.put("transaction_date", date.format(transActthirdUpdate.getTxnDatetime()));
								locObjRspdataJson.put("transaction_time", time.format(transActthirdUpdate.getTxnDatetime()));
							} else {
								locObjRspdataJson.put("transaction_date", "");
								locObjRspdataJson.put("transaction_time", "");
							}
							locObjRspdataJson.put("payment_mode", "");*/
							System.out.println("request--=================18-");
						}
						System.out.println("request--=================19-");
						responseData = finalMobiResponse(locObjRspdataJson);
						System.out.println("responseData easypayres------->>-" + responseData);
				        request.setAttribute("responseData", responseData);
						
					} else if (cyperFinalstsRes.equalsIgnoreCase("1")) {
						rescode = "R0222";
						rescodeStsMsg = "Pending";
						remarksMsg = cyperErrmsg;
						flg = false;
					} else if (cyperFinalstsRes.equalsIgnoreCase("2")) {
						rescode = "R0223";
						rescodeStsMsg = "Failed";
						remarksMsg = cyperErrmsg;
						flg = false;
					} else if (cyperFinalstsRes.equalsIgnoreCase("3")) {
						rescode = "R0223";
						rescodeStsMsg = "Error";
						remarksMsg = cyperErrmsg;
						flg = false;
					} else {
						/* Error msg*/
						rescode = "R0223";
						rescodeStsMsg = "Failed";
						remarksMsg = cyperErrmsg;
						flg = false;
					}
					System.out.println("third before flg execute cyperSession---------" + cyperSession);
					if (flg == false) {
						MvpTransactionTbl transRemarks = new MvpTransactionTbl();
						transRemarks.setSession(cyperSession);
						transRemarks.setRemarksMsg(Commonutility.stringToStringempty(remarksMsg));
						boolean remarkUpdate = easypayDaoObj.transRemarksUpdate(transRemarks,"SN");
						
						locObjRspdataJson=new JSONObject();
						MvpTransactionTbl transActgetData = new MvpTransactionTbl();
						transActgetData.setSession(cyperSession);
						System.out.println("third After flg execute cyperSession---------" + cyperSession);
						transActgetData = easypayDaoObj.transGetFinalResponse(transActgetData);
						locObjRspdataJson = jsonSuccessErrorFormation(rescodeStsMsg,transActgetData.getOrderNo(),transActgetData.getTxnAmount(),
								transActgetData.getServiceType(),"",transActgetData.getTxnDatetime(),"",Commonutility.stringToStringempty(transActgetData.getRemarksMsg()));
						/*locObjRspdataJson.put("payment_status", rescodeStsMsg);
						locObjRspdataJson.put("reference_no", Commonutility.stringToStringempty(transActgetData.getOrderNo()));
						if (transActgetData.getTxnAmount() != 0) {
							float priceAmt = transActgetData.getTxnAmount();
							DecimalFormat df = new DecimalFormat("#.00");
							df.setMaximumFractionDigits(2);
							locObjRspdataJson.put("amount",Commonutility.toCheckNullEmpty(df.format(priceAmt)));
						} else if (transActgetData.getTxnAmount() == 0 || transActgetData.getTxnAmount() == 0.0) {
							locObjRspdataJson.put("amount","0");
						} else {
							locObjRspdataJson.put("amount","");
						}
						locObjRspdataJson.put("service", Commonutility.intToString(transActgetData.getServiceType()));
						locObjRspdataJson.put("bank_details", "");
						if (transActgetData.getTxnDatetime() != null) {
							DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
					        DateFormat time = new SimpleDateFormat("hh:mm:ss");
					        locObjRspdataJson.put("transaction_date", date.format(transActgetData.getTxnDatetime()));
							locObjRspdataJson.put("transaction_time", time.format(transActgetData.getTxnDatetime()));
						} else {
							locObjRspdataJson.put("transaction_date", "");
							locObjRspdataJson.put("transaction_time", "");
						}
						locObjRspdataJson.put("payment_mode", "");*/
						responseData = finalMobiResponse(locObjRspdataJson);
						request.setAttribute("responseData", responseData);
						return "input";
					}
				}
			} else {
				/* Error msg */
			}
		} catch (Exception ex) {
			    log.logMessage(getText("Eex") + ex, "error", EasyPayGateway.class);
				locObjRspdataJson = new JSONObject();
				locObjRspdataJson = jsonSuccessErrorFormation("Failed","",Commonutility.stringToFloat(locAmount),
						Commonutility.stringToInteger(locBiller), "",null, "",mobiCommon.getMsg("R0003"));
				responseData = finalMobiResponse(locObjRspdataJson);
				request.setAttribute("responseData", responseData);
				return "input";
		}
		return "success";
	}
	
	private String serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		String response="";
		JSONObject responseMsg = new JSONObject();
		try{
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			response = responseMsg.toString();
			System.out.println("response------------------"+response);
			response=EncDecrypt.encrypt(response);
		} catch (Exception ex) {
			log.logMessage(getText("Eex") + ex, "error", EasyPayGateway.class);
			System.out.println("Response formation error--------- "+ ex);
		}
		return response;
	}
	
	private String finalMobiResponse(JSONObject dataJson)
	{
		String response="";
		try{
			response = dataJson.toString();
			System.out.println("response------------------"+response);
			response=EncDecrypt.encrypt(response);
		} catch (Exception ex) {
			log.logMessage(getText("Eex") + ex, "error", EasyPayGateway.class);
			System.out.println("Response formation error--------- "+ ex);
		}
		return response;
	}
	
	private JSONObject jsonSuccessErrorFormation(String rescodeStsMsg, String refno, Float finalAmt, Integer servType, 
			String bdetails, Date txnDateTm, String payMode, String remarkMsg){
		JSONObject responseFormObj = null;
		try{
			responseFormObj = new JSONObject();
			responseFormObj.put("payment_status", rescodeStsMsg);
			responseFormObj.put("reference_no", Commonutility.stringToStringempty(refno));
			if (finalAmt != 0) {
				float priceAmt = finalAmt;
				DecimalFormat df = new DecimalFormat("#.00");
				df.setMaximumFractionDigits(2);
				responseFormObj.put("amount",Commonutility.toCheckNullEmpty(df.format(priceAmt)));
			} else if (finalAmt == 0 || finalAmt == 0.0) {
				responseFormObj.put("amount","0");
			} else {
				responseFormObj.put("amount","");
			}
			if (servType == 0) {
				responseFormObj.put("service", "0");
			} else {
				responseFormObj.put("service", Commonutility.intToString(servType));
			}
			responseFormObj.put("bank_details", "");
			if (txnDateTm != null) {
				DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		        DateFormat time = new SimpleDateFormat("hh:mm:ss");
		        responseFormObj.put("transaction_date", date.format(txnDateTm));
		        responseFormObj.put("transaction_time", time.format(txnDateTm));
			} else {
				responseFormObj.put("transaction_date", "");
				responseFormObj.put("transaction_time", "");
			}
			responseFormObj.put("payment_mode", "");
			responseFormObj.put("remarks", remarkMsg);
		} catch (Exception ex) {
			log.logMessage(getText("Eex") + ex, "error", EasyPayGateway.class);
			System.out.println("jsonSuccessErrorFormation formation error--------- "+ ex);
		}
		return responseFormObj;
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

	public String getPayHttpurl() {
		return payHttpurl;
	}

	public void setPayHttpurl(String payHttpurl) {
		this.payHttpurl = payHttpurl;
	}
	
	
	/*Mobile params*/
	public String getLocRid() {
		return locRid;
	}

	public void setLocRid(String locRid) {
		this.locRid = locRid;
	}

	public String getLocNumber() {
		return locNumber;
	}

	public void setLocNumber(String locNumber) {
		this.locNumber = locNumber;
	}

	public String getLocAccount() {
		return locAccount;
	}

	public void setLocAccount(String locAccount) {
		this.locAccount = locAccount;
	}

	public String getLocAmount() {
		return locAmount;
	}

	public void setLocAmount(String locAmount) {
		this.locAmount = locAmount;
	}

	public String getLocAmountAll() {
		return locAmountAll;
	}

	public void setLocAmountAll(String locAmountAll) {
		this.locAmountAll = locAmountAll;
	}

	public String getLocComment() {
		return locComment;
	}

	public void setLocComment(String locComment) {
		this.locComment = locComment;
	}

	public String getLocTermId() {
		return locTermId;
	}

	public void setLocTermId(String locTermId) {
		this.locTermId = locTermId;
	}

	public String getLocAuthenticator() {
		return locAuthenticator;
	}

	public void setLocAuthenticator(String locAuthenticator) {
		this.locAuthenticator = locAuthenticator;
	}

	public String getLocBiller() {
		return locBiller;
	}

	public void setLocBiller(String locBiller) {
		this.locBiller = locBiller;
	}

	public String getFinaldata() {
		return finaldata;
	}

	public void setFinaldata(String finaldata) {
		this.finaldata = finaldata;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public String getCyperPlateurl() {
		return cyperPlateurl;
	}

	public void setCyperPlateurl(String cyperPlateurl) {
		this.cyperPlateurl = cyperPlateurl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	

}
