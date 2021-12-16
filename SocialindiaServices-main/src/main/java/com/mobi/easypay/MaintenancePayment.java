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
import com.mobi.easypayvo.persistence.EasyPaymentDao;
import com.mobi.easypayvo.persistence.EasyPaymentDaoServices;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class MaintenancePayment extends ActionSupport {
	
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
	String locAmountAll = null;
	String locComment = null;
	String locTermId = null;
	String locAuthenticator = null;
	String locBiller = null;
	String locMaintenanceId = null;
	String locPaidStatus = null;
	
	public String execute() {
		redirectUrl = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("paymentRes.url");
		System.out.println("Enter MaintenancePayment page================================");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
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
			int maintenanceId = 0;
			JSONObject paymentCallObj = new JSONObject();
			log.logMessage("Enter into MaintenancePayment ", "info", MaintenancePayment.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("MaintenancePayment ivrparams :" + ivrparams, "info", MaintenancePayment.class);
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
						locMaintenanceId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"maintenance_id");
						locPaidStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"paid_status");
						//locNumber = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"number");
						//locAccount = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"account");
						locAmount = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"amount");
						//locAmountAll = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"amount_all");
						//locComment = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"comment");
						//locTermId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"term_id");
						//locAuthenticator = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"authenticator");
						locBiller = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"biller_category");
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
						if (Commonutility.checkempty(locMaintenanceId) && Commonutility.toCheckisNumeric(locMaintenanceId)) {
							maintenanceId = Commonutility.stringToInteger(locMaintenanceId);
							if (maintenanceId !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("maintenance.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
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
			log.logMessage("MaintenancePayment flg :" +flg, "info", MaintenancePayment.class);
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
						transActObj.setPaymentType(2); //Maintenance pay
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
									pgdetailObj.put("Paymode", "");
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
									payHttpurl = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("paygate.url")+EncDecrypt.encrypt(response);
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
			log.logMessage("Exception found in MaintenancePayment execute() : "+ex, "error", MaintenancePayment.class);
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
	
	/*public String payGateResponseFunc() {
		redirectUrl = getText("paymentRes.url");
		JSONObject locObjRspdataJson = null; //Response
		HttpServletRequest request = ServletActionContext.getRequest();
		boolean flg = true;
		String remarksMsg = "";
		String rescode = "";
		String rescodeStsMsg = "";
		try {
			JSONObject cyperCallObj = new JSONObject();
			EasyPaymentDao easypayDaoObj = new EasyPaymentDaoServices();
			System.out.println("request---finaldata------payGateResponseFunc---------------" + finaldata);
			if (Commonutility.checkempty(finaldata)) {
				finaldata = finaldata.replace(" ", "+");
				finaldata = EncDecrypt.decrypt(finaldata);
				log.logMessage("MaintenancePayment finaldata :" + finaldata, "info", MaintenancePayment.class);
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
						MvpTransactionTbl transRemarks = new MvpTransactionTbl();
						transRemarks.setSession(resOrderno);
						System.out.println("Second After flg execute resOrderno---------" + resOrderno);
						transRemarks = easypayDaoObj.transGetFinalResponse(transRemarks);
						if (transRemarks != null) {
						boolean lvrfunmtr=false;
						Common common=new CommonDao();
						String updatesql ="update MaintenanceFeeTblVO set paidStatus='1' where maintenanceId="+transRemarks.getMaintenanceId()+" and statusFlag=1";
						lvrfunmtr = common.commonUpdate(updatesql);
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson = jsonSuccessErrorFormation("Success",transRemarks.getOrderNo(),transRemarks.getTxnAmount(),
								transRemarks.getServiceType(),"",transRemarks.getTxnDatetime(),"",transRemarks.getRemarksMsg());
						responseData = finalMobiResponse(locObjRspdataJson);
						System.out.println("responseData easypayres------->>-" + responseData);
				        request.setAttribute("responseData", responseData);
						} else {
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
						 Error msg
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
						responseData = finalMobiResponse(locObjRspdataJson);
						request.setAttribute("responseData", responseData);
						return "input";
					}
				}
			} else {
				 Error msg				
			}
		} catch (Exception ex) {
			log.logMessage("Exception in MaintenancePayment" + ex, "error", MaintenancePayment.class);
			if (flg == false) {
				locObjRspdataJson = new JSONObject();
				locObjRspdataJson = jsonSuccessErrorFormation("Failed","",Commonutility.stringToFloat(locAmount),
						Commonutility.stringToInteger(locBiller), "",null, "",mobiCommon.getMsg("R0003"));
				responseData = finalMobiResponse(locObjRspdataJson);
				request.setAttribute("responseData", responseData);
				return "input";
			}
		}
		return "success";
	}*/
	
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
			log.logMessage(getText("Eex") + ex, "error", MaintenancePayment.class);
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
			log.logMessage(getText("Eex") + ex, "error", MaintenancePayment.class);
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
			responseFormObj.put("service", Commonutility.intToString(servType));
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
			log.logMessage(getText("Eex") + ex, "error", MaintenancePayment.class);
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
