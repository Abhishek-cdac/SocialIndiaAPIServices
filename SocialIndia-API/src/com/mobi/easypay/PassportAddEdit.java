package com.mobi.easypay;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.easypayvo.PassportTblVo;
import com.mobi.easypayvo.persistence.PassportDao;
import com.mobi.easypayvo.persistence.PassportDaoServices;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class PassportAddEdit extends ActionSupport {
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	JsonpackDao jsonPack = new JsonpackDaoService();
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int passportId = 0;
			int categoryId = 0;
			int skillId = 0;
			int durationFlg = 0;
			float fees = 0.0f;
			int addEdit = 0;
			int duration = 0;
			int feedId = 0;
			String locRid = null;
			String locPassportId = null;
			String locProfileName = null;
			String locMobileNo = null;
			String locDthNo = null;
			String locDatacardNo = null;
			String locGasNo = null;
			String locElectricityNo = null;
			String locLandlineNo = null;
			String locBroadbandNo = null;
			String locBankaccNo = null;
			String locReaccountNo = null;
			String locBankaccName = null;
			String locaccountType = null;
			String locBankName = null;
			String locIfscName = null;
			String locVirtualPayaddr = null;
			JSONArray daysJsnArr = new JSONArray();
			//FunctionUtility commonFn = new FunctionUtilityServices();
			//String daysJsnArr = null;
			log.logMessage("Enter into PassportAddEdit ", "info", PassportAddEdit.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("PassportAddEdit ivrparams :" + ivrparams, "info", PassportAddEdit.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						locPassportId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"passid");
						locProfileName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"profile_name");
						locMobileNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mobile_no");
						locDthNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"dth_no");
						locDatacardNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"datacard_no");
						locGasNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"gas_no");
						locElectricityNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"electricity_no");
						locLandlineNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"landline_no");
						locBroadbandNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"broadband_no");
						locBankaccNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"bankacc_no");
						locReaccountNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"reaccount_no");
						locBankaccName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"bankacc_name");
						locaccountType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"accountType");
						locBankName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"bank_name");
						locIfscName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"ifsc_name");
						locVirtualPayaddr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"virtual_payaddr");
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
						
						if (Commonutility.checknull(locPassportId)) {
							if (Commonutility.checkLengthNotZero(locPassportId)) {
								if (Commonutility.toCheckisNumeric(locPassportId)) {
									passportId = Commonutility.stringToInteger(locPassportId);
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("passport.id.error")));
								}
							}
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				}else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("flg :" +flg, "info", PassportAddEdit.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						int entrybyUsr = rid;
						PassportTblVo passportObj = new PassportTblVo();
						UserMasterTblVo userobj = new UserMasterTblVo();
						PassportDao passportDaoObj = new PassportDaoServices();
						userobj.setUserId(rid);
						if (passportId == 0 ) {							
							passportObj.setUsrId(userobj);
							passportObj.setProfileName(locProfileName);
							passportObj.setMobileNumber(locMobileNo);
							passportObj.setDthConsumerNumber(locDthNo);
							passportObj.setDatacardConsumerNumber(locDatacardNo);
							passportObj.setGasConsumerNumber(locGasNo);
							passportObj.setElectricityConsumerNumber(locElectricityNo);
							passportObj.setLandlineNo(locLandlineNo);
							passportObj.setBroadbandNumber(locBroadbandNo);
							passportObj.setBankAccountNumber(locBankaccNo);
							passportObj.setReEnterAccountNumber(locReaccountNo);
							passportObj.setBankAccountName(locBankaccName);
							passportObj.setAccountType(locaccountType);
							passportObj.setBankName(locBankName);
							passportObj.setIfscName(locIfscName);
							passportObj.setUpiIdVirtualPaymentAddress(locVirtualPayaddr);
							passportObj.setStatus(1);
							passportObj.setEntryBy(entrybyUsr);
							passportObj.setEntryDateTime(Commonutility.enteyUpdateInsertDateTime());
							passportObj.setModifyDateTime(Commonutility.enteyUpdateInsertDateTime());
							int passportUniqId = passportDaoObj.insetPassportData(passportObj);
							log.logMessage("passportUniqId :::" + passportUniqId , "info", PassportAddEdit.class);
							if (passportUniqId != -1) {	
								addEdit = 1;
							}
						} else if (passportId > 0) {
							// publishSkillId feedId
							if (passportId != 0 && rid != 0) {
								userobj.setUserId(rid);
								passportObj.setUsrId(userobj);
								passportObj.setPassId(passportId);
								if (Commonutility.checkempty(locProfileName)) {
									passportObj.setProfileName(locProfileName);
								}
								if (Commonutility.checkempty(locMobileNo)) {
									passportObj.setMobileNumber(locMobileNo);
								}
								if (Commonutility.checkempty(locDthNo)) {
									passportObj.setDthConsumerNumber(locDthNo);
								}
								if (Commonutility.checkempty(locDatacardNo)) {
									passportObj.setDatacardConsumerNumber(locDatacardNo);
								}
								if (Commonutility.checkempty(locGasNo)) {
									passportObj.setGasConsumerNumber(locGasNo);
								}
								if (Commonutility.checkempty(locElectricityNo)) {
									passportObj.setElectricityConsumerNumber(locElectricityNo);
								}
								if (Commonutility.checkempty(locLandlineNo)) {
									passportObj.setLandlineNo(locLandlineNo);
								}
								if (Commonutility.checkempty(locBroadbandNo)) {
									passportObj.setBroadbandNumber(locBroadbandNo);
								}
								if (Commonutility.checkempty(locBankaccNo)) {
									passportObj.setBankAccountNumber(locBankaccNo);
								}
								if (Commonutility.checkempty(locReaccountNo)) {
									passportObj.setReEnterAccountNumber(locReaccountNo);
								}
								if (Commonutility.checkempty(locBankaccName)) {
									passportObj.setBankAccountName(locBankaccName);
								}
								if (Commonutility.checkempty(locaccountType)) {
									passportObj.setAccountType(locaccountType);
								}
								if (Commonutility.checkempty(locBankName)) {
									passportObj.setBankName(locBankName);
								}
								if (Commonutility.checkempty(locIfscName)) {
									passportObj.setIfscName(locIfscName);
								}
								if (Commonutility.checkempty(locVirtualPayaddr)) {
									passportObj.setUpiIdVirtualPaymentAddress(locVirtualPayaddr);
								}
								
								/*if (Commonutility.checkIntempty(durationFlg)) {
									pubSkilObj.setDurationFlg(durationFlg);
								}*/
								passportObj.setStatus(1);
								passportObj.setEntryBy(entrybyUsr);
								passportObj.setModifyDateTime(Commonutility.enteyUpdateInsertDateTime());
								boolean chk = passportDaoObj.editPassportData(passportObj);
								if (chk) {
									addEdit = 2;
								} else {
									flg = false;
								}
							} else {
								flg = false;
							}							
						} else {
							flg = false;
						}
						log.logMessage("Passport success flg:" + flg + " :: passportId=" + passportId, "info", PassportAddEdit.class);
						if (flg) {
							if (addEdit == 1 ) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0218"),locObjRspdataJson);
							} else if (addEdit == 2 ) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0220"),locObjRspdataJson);
							} else {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
							}
							
						} else {							
							if (addEdit == 1 ) {
								serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0219"),locObjRspdataJson);
							} else if (addEdit == 2 ) {
								serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0221"),locObjRspdataJson);
							} else {
								log.logMessage("Passport id not valid", "info", PassportAddEdit.class);
								serverResponse(ivrservicecode,getText("status.warning"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
							}
						}
					} else {
						locObjRspdataJson=new JSONObject();
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);	
					}
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);	
				}								
				
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", PassportAddEdit.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {			
		}
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
			out.print(as);
			out.close();
		} catch (Exception ex) {
			try{
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
			}catch(Exception e){}finally{}
		}
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

}
