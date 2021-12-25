package com.pack.billpayment;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.pack.billpaymentvo.CyberplatoptrsTblVo;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class Cyberplatvalidation implements CyberplatvalidationIntface{
	
	public static CyberplatoptrsTblVo toGetDetailsofbiler(String prmBillerid, String prmCateg, String prmBillername){
		Log logWrite = null;	
		Session lvrSession = null;		
		Query lvrQry = null;
		String lvrSltqry = null;
		CyberplatoptrsTblVo lvrObj =null;
		try {
			lvrSession = HibernateUtil.getSession();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : toGetDetailsbiler Called [Start]", "info", Cyberplatvalidation.class);
			Commonutility.toWriteConsole("Step 1 : toGetDetailsbiler Called [Start]");
			lvrSltqry = "from CyberplatoptrsTblVo where ivrBILLER_CATEGORY = " + prmCateg + " and ivrBnSTATUS = 1 and ivrBnBILLER_ID = " + prmBillerid;
			lvrQry = lvrSession.createQuery(lvrSltqry);
			lvrObj = (CyberplatoptrsTblVo) lvrQry.uniqueResult();
			//for demo payment - if live comment these line -start
			String lvrValdaturl ="http://ru-demo.cyberplat.com/cgi-bin/DealerSertification/de_pay_check.cgi";
			String lvrPaymturl = "http://ru-demo.cyberplat.com/cgi-bin/DealerSertification/de_pay.cgi";
			String lvrStschkurl = "http://ru-demo.cyberplat.com/cgi-bin/DealerSertification/de_pay_status.cgi";
			lvrObj.setIvrBnVALIDATE_URL(lvrValdaturl);
			lvrObj.setIvrBnSTATUS_CHECK_URL(lvrStschkurl);
			lvrObj.setIvrBnPAYMENT_URL(lvrPaymturl);
			//for demo payment - if live comment these line - start
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in Cyberplatvalidation.class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", Cyberplatvalidation.class);
			Commonutility.toWriteConsole("Step -1 : Exception found in  Cyberplatvalidation.class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
		} finally {
			if(lvrSession!=null) {lvrSession.flush();lvrSession.clear();lvrSession.close();lvrSession = null;}
			logWrite = null; lvrQry = null;
		}
		return lvrObj;
	}
	
	public static void main(String prm[]) {
		Cyberplatvalidation lvrObj = new Cyberplatvalidation();
		//lvrObj.toValidatePostpaid ("9787103262","1", "4",100000.00);
		//lvrObj.toValidatePrepaid("9787103262","39", "1",12.00);
		//lvrObj.toValidateDTH("9787103262","54", "2",102.00);
		lvrObj.toValidateElectricity("1007103262","24", "6",102.00);
		//{message : "success",statusmsg:"success", errorfield:"", status :"0", data : {}}
		//{message : "Amount less then 10000",statusmsg:"error", errorfield:"amount", status :"1", data : {}}
		//{"minlength":"10","maxlength":"12","partialpay":"Yes","landlinewithstd":"No","minamount":"10.00","maxamount":"10000.00","afterduedateaccept":"Yes","perdayrechargelimti":"","duplictrchrgtimegap":"","plans":"","flexidenomination":"","notransperday":"","reversal":"No"}
	}
	
	@Override
	public JSONObject toValidatePostpaid(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;		
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null, lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrPerdayrchrlimit = null;
		
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null; 
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate postpaid  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate postpaid  method Called [Start]");			
			lvrObj =  toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");		
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")) {
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)) {
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}																
											
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if (lvrValidationrslt) {
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
								}
							} else {
								logWrite.logMessage("Step 10 : Resharge Amount should min of "+lvrMinamt +", Given AMoount  : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Amount should min of "+lvrMinamt +", Given AMoount  : "+prmAmount);
								lvrValidationrslt = false;
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount,prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharges limit check [End]
						
						// -------------------------------------------------Duplicate Recharges time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 	
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharge limit check [Start]											
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
					
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";
						
					}
				} else {
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");		
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 11 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 11 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 12 : Validate postpaid  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 12 : Validate postpaid  method Finished [End] : "+lvrRtnjson);	
		} catch (Exception e) {
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
			
			
		} finally {			
			logWrite = null;
		}		
		return lvrRtnjson;
	}
	
	@Override
	public JSONObject toValidatePrepaid(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;				
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {			
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate Prepaid  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate Prepaid  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}			
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")) {
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){						
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							
							if (lvrValidationrslt) { // Minimum amount validation success
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
								}
							} else {
								logWrite.logMessage("Step 10 : Resharge Amount should min of "+lvrMinamt +", Given AMoount  : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Amount should min of "+lvrMinamt +", Given AMoount  : "+prmAmount);
								lvrValidationrslt = false;
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
							}
																											
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharge limit check [End]
						
						// -------------------------------------------------Duplicate Recharge time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						}
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharge limit check [Start]
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty(lvrPerdayrchrlimit));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";						
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate prepaid  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate prepaid  method Finished [End] : "+lvrRtnjson);	
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {			
			logWrite = null;
		}		
		return lvrRtnjson;
	}

	@Override
	public JSONObject toValidateDTH(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null, lvrPlnavailable = null;
		String lvrTillduedtact = null, lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null,lvrAdditionalval = null;		
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate DTH  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate DTH  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							
							if (lvrValidationrslt) {	
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
							  }
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
							
						} else {
							//logWrite.logMessage("Step 9 : Amount should not empty", "info",this.getClass());
							//Commonutility.toWriteConsole("Step 9 : Amount should not empty");
							
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
							//lvrMsgforerr = "Number length should min "+lvrMinlen + "char";													
							//lvrValidationrslt = false;
							//lvrMsgforerr = "Amount should not empty";
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharges limit check [End]
						
						// -------------------------------------------------Duplicate Recharges time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 	
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharge limit check [Start]
						
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate DTH  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate DTH  method Finished [End] : "+lvrRtnjson);	
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * @see com.pack.billpayment.CyberplatvalidationIntface#toValidateElectricity(java.lang.String, java.lang.String, java.lang.String, java.lang.Double)
	 * Valid on : recharge number - Min max length and start with
	 */
	@Override
	public JSONObject toValidateElectricity(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null, lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrStrwith = null, lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null,lvrAdditionalval = null;		
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr =null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate Electricity  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate Electricity  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						lvrStrwith = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "startwith");
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							
							if (lvrValidationrslt) {
								if (lvrMaxamt == null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
								}
								}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Start with Validation [Start]
						if(Commonutility.checkempty(lvrStrwith)){
							if(Commonutility.checkempty(prmNumber)){
								if(prmNumber.startsWith(lvrStrwith)){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Reacharge Number Should Start With "+lvrStrwith +"";
								} 
							}
						} else {
							lvrValidationrslt = true;
						}						
						// -------------------------------------------------Start with Validation [End]		
												
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharge limit check [End]
												
						// -------------------------------------------------Duplicate Recharge time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharge time gap  check [End]
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";		
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate DTH  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate DTH  method Finished [End] : "+lvrRtnjson);
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}

	@Override
	public JSONObject toValidateLandLine(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrStrwith = null, lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate LandLine  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate LandLine  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						lvrStrwith = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "startwith");
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if (lvrValidationrslt) {	
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
								}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Start with Validation [Start]
						if(Commonutility.checkempty(lvrStrwith)){
							if(Commonutility.checkempty(prmNumber)){
								if(prmNumber.startsWith(lvrStrwith)){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Reacharge Number Should Start With "+lvrStrwith +"";
								} 
							}
						} else {
							lvrValidationrslt = true;
						}						
						// -------------------------------------------------Start with Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharges limit check [End]
						
						// -------------------------------------------------Duplicate Recharges time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 	
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharge time gap  check [End]
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty(lvrPerdayrchrlimit));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate LandLine  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate LandLine  method Finished [End] : "+lvrRtnjson);	
			
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}

	/*
	 * (non-Javadoc)
	 * @see com.pack.billpayment.CyberplatvalidationIntface#toValidateGasBill(java.lang.String, java.lang.String, java.lang.String, java.lang.Double)
	 * Valid on : recharge number - Min max length and start with
	 */
	@Override
	public JSONObject toValidateGasBill(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrStrwith = null, lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate GasBill  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate GasBill  method Called [Start]");			
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						lvrStrwith = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "startwith");
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if (lvrValidationrslt) {	
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
								}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Start with Validation [Start]
						if(Commonutility.checkempty(lvrStrwith)){
							if(Commonutility.checkempty(prmNumber)){
								if(prmNumber.startsWith(lvrStrwith)){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Reacharge Number Should Start With "+lvrStrwith +"";
								} 
							}
						} else {
							lvrValidationrslt = true;
						}						
						// -------------------------------------------------Start with Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharges limit check [End]
						
						// -------------------------------------------------Duplicate Recharges time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 	
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharge time gap  check [End]
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate GasBill  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate GasBill  method Finished [End] : "+lvrRtnjson);				
			
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
			
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}

	@Override
	public JSONObject toValidateInsurance(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate Insurance  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate Insurance  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if (lvrValidationrslt) {
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
								}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharges limit check [End]
						
						// -------------------------------------------------Duplicate Recharges time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 	
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharges time gap  check [End]
																		
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate GasBill  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate GasBill  method Finished [End] : "+lvrRtnjson);
			
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}

	@Override
	public JSONObject toValidateBroadBand(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null; 
		String lvrPerdayrchrlimit;
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate BroadBand  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate BroadBand  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if (lvrValidationrslt) {
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
								}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharges limit check [End]
						
						// -------------------------------------------------Duplicate Recharges time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 		
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharge time gap  check [End]
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate GasBill  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate GasBill  method Finished [End] : "+lvrRtnjson);
			
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}

	@Override
	public JSONObject toValidateDirectMoneyTransfer(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No 
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate DirectMoneyTransfer  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate DirectMoneyTransfer  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ) {
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ) {
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if(lvrValidationrslt) {
								if (lvrMaxamt==null) {
									logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
									lvrValidationrslt = true;
								
								} else {
									if (prmAmount <= lvrMaxamt){
										logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
										lvrValidationrslt = true;
									} else {
										logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
										Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
										lvrValidationrslt = false;
										lvrMsgforerr = "Amount should max of "+lvrMaxamt;
									}
								}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharge limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							lvrValidationrslt = false;
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharge limit check [End]
						
						// -------------------------------------------------Duplicate Recharge time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 			
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharge time gap  check [End]
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate GasBill  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate GasBill  method Finished [End] : "+lvrRtnjson);
			
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}
	
	@Override
	public JSONObject toValidatePostPaidDataCard(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate PostPaidDataCard  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate PostPaidDataCard  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if (lvrValidationrslt) {
							if (lvrMaxamt==null) {
								logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
								lvrValidationrslt = true;
								
							} else {
								if (prmAmount <= lvrMaxamt){
									logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should max of "+lvrMaxamt;
								}
							}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharges limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharges limit check [End]
											
						// -------------------------------------------------Duplicate Recharges time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 			
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharge time gap  check [End]
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate GasBill  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate GasBill  method Finished [End] : "+lvrRtnjson);
			
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}
	
	@Override
	public JSONObject toValidatePrePaidDataCard(String prmNumber, String prmBillerid, String prmCateg, Double prmAmount) {
		JSONObject lvrRtnjson = null, lvrDatajson = null;
		Log logWrite = null;
		String lvrPratialPay = null; // Yes / No
		Integer lvrMinlen = null, lvrMaxlen = null;
		Double lvrMinamt =null, lvrMaxamt = null;
		String lvrTillduedtact = null;// Yes / No
		String lvrRvslallow = null;// Yes / No
		String lvrValdaturl = null, lvrPaymturl = null, lvrStschkurl = null;
		String lvrDuplicattimegap = null, lvrDuplicattimeon = null;
		String lvrPerdayrchrlimit = null;
		
		Integer lvrGateid = null, lvrCategory = null;
		String lvrBillername = null, lvrAdditionalval = null;
		CyberplatoptrsTblVo lvrObj =null;
		
		boolean lvrValidationrslt = false;	
		String lvrStscode = null, lvrStsmsg = null, lvrMessage = null, lvrErrfield = null, lvrMsgforerr = null;
		try {
			lvrRtnjson = new JSONObject();
			lvrDatajson = new JSONObject();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : Validate PrePaidDataCard  Method Called [Start]", "info",this.getClass());
			Commonutility.toWriteConsole("Step 1 : Validate PrePaidDataCard  method Called [Start]");
			if(prmAmount!=null){
				prmAmount = Commonutility.toRoundedVal(prmAmount);
				Commonutility.toWriteConsole("prmAmount : "+String.format( "%.2f", prmAmount ));	
			}
			lvrObj = toGetDetailsofbiler(prmBillerid,prmCateg,"");
			if(lvrObj!=null) {
				logWrite.logMessage("Step 2 : getbiller details found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details found");
				lvrAdditionalval = lvrObj.getIvrBnADDITIONAL_VALIDATION();
				lvrGateid = lvrObj.getIvrBILLER_GATE_ID_CODE();
				lvrCategory = lvrObj.getIvrBILLER_CATEGORY();
				lvrBillername = lvrObj.getIvrBnBILLER_NAME();
				lvrValdaturl = lvrObj.getIvrBnVALIDATE_URL();
				lvrPaymturl = lvrObj.getIvrBnPAYMENT_URL();
				lvrStschkurl = lvrObj.getIvrBnSTATUS_CHECK_URL();
				lvrDatajson.put("gateidcode", Commonutility.toCheckNullEmpty(lvrGateid));
				lvrDatajson.put("category", Commonutility.toCheckNullEmpty(lvrCategory));
				lvrDatajson.put("billername", Commonutility.toCheckNullEmpty(lvrBillername));
				lvrDatajson.put("validateurl", Commonutility.toCheckNullEmpty(lvrValdaturl));
				lvrDatajson.put("paymenturl", Commonutility.toCheckNullEmpty(lvrPaymturl));
				lvrDatajson.put("statuschkurl", Commonutility.toCheckNullEmpty(lvrStschkurl));
				lvrDatajson.put("rchamount", Commonutility.toCheckNullEmpty(String.format( "%.2f", prmAmount )));
				lvrDatajson.put("rchnumber", Commonutility.toCheckNullEmpty(prmNumber));
				if(Commonutility.checkempty(lvrAdditionalval) && !lvrAdditionalval.equalsIgnoreCase("Nill")){
					logWrite.logMessage("Step 3 : Additional validation found.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : Additional validation found.");	
					if(Commonutility.toCheckIsJSON(lvrAdditionalval)){
						logWrite.logMessage("Step 4 : Additional validation found on json format", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : Additional validation found on json format");
						JSONObject lvrAddValidJson = new JSONObject(lvrAdditionalval);
						String lvrMinlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minlength");
						String lvrMaxlentghtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxlength");
						String lvrMinamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "minamount");
						String lvrMaxamtstr = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "maxamount");
						lvrTillduedtact = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept");
						lvrRvslallow = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal");
						
						
						if(Commonutility.checkempty(lvrMinlentghtstr) ){
							lvrMinlen = Commonutility.stringToInteger(lvrMinlentghtstr);
						} else {
							lvrMinlen = null;
						}
						
						if(Commonutility.checkempty(lvrMaxlentghtstr) ){
							 lvrMaxlen = Commonutility.stringToInteger(lvrMaxlentghtstr);
						} else {
							 lvrMaxlen = null;
						}
						// -------------------------------------------------Number length validation [Start]
						logWrite.logMessage("Step 5 : Recharge number validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 5 : Recharge number validation start");
						if(Commonutility.checkempty(prmNumber)){
							logWrite.logMessage("Step 6 : Recharge number is "+prmNumber, "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number is "+prmNumber);
							if(lvrMinlen!=null && prmNumber.length() >= lvrMinlen){										
								if(lvrMaxlen!=null && prmNumber.length() <= lvrMaxlen){
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 8 : Number length should max "+lvrMaxlen + "char", "info",this.getClass());
									Commonutility.toWriteConsole("Step 8 : Number length should max "+lvrMaxlen + "char");
									
									lvrValidationrslt = false;
									lvrMsgforerr = "Number length should max "+lvrMaxlen + "char";
								}								
							} else {
								logWrite.logMessage("Step 7 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
								Commonutility.toWriteConsole("Step 7 : Number length should min "+lvrMinlen + "char");
								
								lvrValidationrslt = false;
								lvrMsgforerr = "Number length should min "+lvrMinlen + "char";
							}							
						} else {
							logWrite.logMessage("Step 6 : Recharge number empty", "info",this.getClass());
							Commonutility.toWriteConsole("Step 6 : Recharge number empty");
							lvrValidationrslt = false;
							lvrMsgforerr = "Number should not empty";
						}
						// -------------------------------------------------Number length validation [End]	
						 
						// -------------------------------------------------Amount Validation [Start]
						if(Commonutility.checkempty(lvrMinamtstr) ){
							lvrMinamt = Double.parseDouble(lvrMinamtstr);
						} else {
							lvrMinamt = null;
						}
						
						if(Commonutility.checkempty(lvrMaxamtstr) ){
							lvrMaxamt =  Double.parseDouble(lvrMaxamtstr);
						} else {
							lvrMaxamt = null;
						}
						logWrite.logMessage("Step 8 : Recharge amount validation start", "info",this.getClass());
						Commonutility.toWriteConsole("Step 8 : Recharge amount validation start");
						if (lvrValidationrslt) {							
							if (lvrMinamt == null) {
								logWrite.logMessage("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 9 : Minimum amount validation not need.["+lvrMinamt+"]");
								lvrValidationrslt = true;
							} else {
								if (prmAmount >= lvrMinamt){
									logWrite.logMessage("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [if] "+ prmAmount +" - Amount grater than minimum amount "+lvrMinamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 9 : [else] Amount should min of "+lvrMinamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 9 : [else] Amount should min of "+lvrMinamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should min of "+lvrMinamt;
								}
							}
							if (lvrValidationrslt) {
							if (lvrMaxamt==null) {
								logWrite.logMessage("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]", "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Maximum amount validation not need.["+lvrMaxamt+"]");
								lvrValidationrslt = true;
								
							} else {
								if (prmAmount <= lvrMaxamt){
									logWrite.logMessage("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : [if] " +prmAmount + " - Recharge amount  is less than "+lvrMaxamt);
									lvrValidationrslt = true;
								} else {
									logWrite.logMessage("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt, "info",this.getClass());
									Commonutility.toWriteConsole("Step 10 : [else] "+ prmAmount +" - Amount should max of "+lvrMaxamt);
									lvrValidationrslt = false;
									lvrMsgforerr = "Amount should max of "+lvrMaxamt;
								}
							}
							} else {
								logWrite.logMessage("Step 10 : Minmum amount should ["+lvrMinamt+"], Given amount : "+prmAmount, "info",this.getClass());
								Commonutility.toWriteConsole("Step 10 : Minmum amount should ["+lvrMinamt+"] Given amount : "+prmAmount);
								lvrMsgforerr = "Amount should min of "+lvrMinamt;
								lvrValidationrslt = false;
							}
						} else {
							logWrite.logMessage("Step 9 : Number length should min "+lvrMinlen + "char", "info",this.getClass());
							Commonutility.toWriteConsole("Step 9 : Number length should min "+lvrMinlen + "char");
							
							lvrValidationrslt = false;
						}
						// -------------------------------------------------Amount Validation [End]
						
						// -------------------------------------------------Per day Recharge limit check [Start]
						logWrite.logMessage("Step 11 : Perday recharge limit check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 11 : Perday recharge limit check [start]");
						lvrPerdayrchrlimit = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti");	
						if (lvrValidationrslt) { // Minimum amount validation success
							if(Commonutility.checkempty(lvrPerdayrchrlimit)){
								boolean lvrPrdayvalirst = ValidatonUtility.toCheckPerdaylimit(lvrPerdayrchrlimit, prmAmount, prmNumber);
								if(lvrPrdayvalirst){
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Perday limit exceeded. Limit on "+lvrPerdayrchrlimit;
								}
							} else {
								lvrValidationrslt = true;
							}
						} else {
							
						}
						logWrite.logMessage("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 12 : Perday recharge limit check [End] status : "+lvrValidationrslt);
						// -------------------------------------------------Per day Recharge limit check [End]
						
						// -------------------------------------------------Duplicate Recharge time gap  check [Start]
						logWrite.logMessage("Step 13 : Duplicate recharge time gap check [start]", "info",this.getClass());
						Commonutility.toWriteConsole("Step 13 : Duplicate recharge time gap check [start]");
						lvrDuplicattimegap = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrgtimegap");
						lvrDuplicattimeon = (String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "duplictrchrdtimeon");
						if (lvrValidationrslt) {
							if(Commonutility.checkempty(lvrDuplicattimegap) && Commonutility.checkempty(lvrDuplicattimeon)){
								boolean lvrTimegapvali = ValidatonUtility.tocheckDuplicateTimegap(prmNumber, lvrDuplicattimegap, lvrDuplicattimeon);
								if(lvrTimegapvali) {
									lvrValidationrslt = true;
								} else {
									lvrValidationrslt = false;
									lvrMsgforerr = "Duplicate recharge time gap above "+lvrDuplicattimegap + lvrDuplicattimeon;
								}
							} else {
								lvrValidationrslt = true;
							}
						} 				
						logWrite.logMessage("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt, "info",this.getClass());
						Commonutility.toWriteConsole("Step 14 : Duplicate recharge time gap check [End] Status : "+lvrValidationrslt);
						// -------------------------------------------------Duplicate Recharge time gap  check [End]
						
						lvrDatajson.put("partialpay", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "partialpay")));
						lvrDatajson.put("afterduedateaccept", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "afterduedateaccept")));
						lvrDatajson.put("landlinewithstd", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "landlinewithstd")));
						lvrDatajson.put("perdayrechargelimti", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "perdayrechargelimti")));
						lvrDatajson.put("duplictrchrgtimegap", Commonutility.toCheckNullEmpty(lvrDuplicattimegap));
						lvrDatajson.put("duplictrchrgtimeon", Commonutility.toCheckNullEmpty(lvrDuplicattimeon));
						lvrDatajson.put("plans", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "plans")));
						lvrDatajson.put("flexidenomination", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "flexidenomination")));
						lvrDatajson.put("notransperday", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "notransperday")));
						lvrDatajson.put("reversal", Commonutility.toCheckNullEmpty((String) Commonutility.toHasChkJsonRtnValObj(lvrAddValidJson, "reversal")));
						
					} else {
						logWrite.logMessage("Step 4 : No Additional validation.", "info",this.getClass());
						Commonutility.toWriteConsole("Step 4 : No Additional validation.");
						
						lvrDatajson.put("partialpay", "");
						lvrDatajson.put("afterduedateaccept", "");
						lvrDatajson.put("landlinewithstd", "");
						lvrDatajson.put("perdayrechargelimti", "");
						lvrDatajson.put("duplictrchrgtimegap", "");
						lvrDatajson.put("duplictrchrgtimeon", "");
						lvrDatajson.put("plans", "");
						lvrDatajson.put("flexidenomination", "");
						lvrDatajson.put("notransperday", "");
						lvrDatajson.put("reversal", "");
						
						lvrValidationrslt = true;
						lvrMessage = "No validation";			
					}
				} else{
					logWrite.logMessage("Step 3 : No Additional validation.", "info",this.getClass());
					Commonutility.toWriteConsole("Step 3 : No Additional validation.");
					lvrDatajson.put("partialpay", "");
					lvrDatajson.put("afterduedateaccept", "");
					lvrDatajson.put("landlinewithstd", "");
					lvrDatajson.put("perdayrechargelimti", "");
					lvrDatajson.put("duplictrchrgtimegap", "");
					lvrDatajson.put("duplictrchrgtimeon", "");
					lvrDatajson.put("plans", "");
					lvrDatajson.put("flexidenomination", "");
					lvrDatajson.put("notransperday", "");
					lvrDatajson.put("reversal", "");
					
					lvrValidationrslt = true;
					lvrMessage = "No validation";
				}
			} else {
				logWrite.logMessage("Step 2 : getbiller details not found", "info",this.getClass());
				Commonutility.toWriteConsole("Step 2 : getbiller details not found");										
				lvrValidationrslt = false;
				lvrMessage = "Error found";
			}
			
			logWrite.logMessage("Step 15 : Validation Status : "+lvrValidationrslt, "info",this.getClass());
			Commonutility.toWriteConsole("Step 15 : Validation Status : "+lvrValidationrslt);
			
			if(lvrValidationrslt){
				lvrStscode = "0"; lvrStsmsg = "success"; lvrMessage = "success"; lvrErrfield = "";
			} else{
				lvrStscode = "1"; lvrStsmsg = "failed"; lvrMessage = lvrMsgforerr; lvrErrfield = "";
			}
			lvrRtnjson.put("message", lvrMessage);
			lvrRtnjson.put("statuscode", lvrStscode);
			lvrRtnjson.put("statusmsg", lvrStsmsg);
			//lvrRtnjson.put("errorfield", lvrErrfield);
			lvrRtnjson.put("data", lvrDatajson);
			logWrite.logMessage("Step 16 : Validate GasBill  Method Finished [End] : "+lvrRtnjson, "info",this.getClass());
			Commonutility.toWriteConsole("Step 16 : Validate GasBill  method Finished [End] : "+lvrRtnjson);
			
			
		} catch (Exception e){
			logWrite.logMessage("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e , "error", this.getClass());
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			try {
				lvrRtnjson = new JSONObject();lvrDatajson = new JSONObject();
				lvrStscode = "1"; lvrStsmsg = "error"; lvrMessage = "Exception Found"; lvrErrfield = "";
				lvrRtnjson.put("message", lvrMessage);
				lvrRtnjson.put("statuscode", lvrStscode);
				lvrRtnjson.put("statusmsg", lvrStsmsg);
				lvrRtnjson.put("errorfield", lvrErrfield);
				lvrRtnjson.put("data", lvrDatajson);
			} catch (Exception ee){ } finally { }
		} finally {
			logWrite = null;
		}		
		return lvrRtnjson;
	}
	
	
}
