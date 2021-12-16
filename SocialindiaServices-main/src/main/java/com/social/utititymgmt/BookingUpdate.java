package com.social.utititymgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.FacilityBookingTblVO;
import com.socialindiaservices.vo.FacilityMstTblVO;

public class BookingUpdate extends ActionSupport {
	private String ivrparams;	
	private String ivrservicefor;
	
	public String execute() {
	Log log = new Log();
	JSONObject locObjRecvJson = null;// Receive String to json
	JSONObject locObjRecvdataJson = null;// Receive Data Json
	JSONObject locObjRspdataJson = null;// Response Data Json
	//String lsvSlQry = null;
	Session locObjsession = null;
	String ivrservicecode = null;
	String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
	String ivrCurntusridstr=null;
	int ivrEntryByusrid=0;
	boolean flg = true;
	StringBuilder locErrorvalStrBuil =null;
	String iswebmobilefla=null;
	
	String[] response = null;
	try {
		CommonMobiDao commonServ = new CommonMobiDaoService();
		locErrorvalStrBuil = new StringBuilder();
		locObjsession = HibernateUtil.getSession();
		if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {					
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
				ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"currentloginid");
				
				if(ivrCurntusridstr == null)
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"crntusrloginid");
					
				 iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
					ivrEntryByusrid= Integer.parseInt(ivrCurntusridstr);
				}else{ ivrEntryByusrid=0; }
				
				if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
					ivrDecissBlkflag = ivrservicefor;
				}else{//post
					ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
				}
				
				JSONArray userArray =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");
				String userprivacy=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "privacy");
				
				if(userArray == null)
					userArray = new JSONArray();
					
				if(userprivacy == null)
					userprivacy = "1";
				
				String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
				String locvrFnrst=null;
				String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;					
				locObjRspdataJson=new JSONObject();					
				 if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){//select single booking detail		
					 
					JSONObject locRspjson=BookingUtility.toSltSingleBookingDtl(locObjRecvdataJson,locObjsession, getText("RESTAD005"), "RESTAD005");					
					locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
					if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
						locObjRspdataJson=locRspjson;
						locRtnSrvId="SI00034";locRtnStsCd="0"; locRtnRspCode="S00034";locRtnMsg=getText("booking.select.success");
						//AuditTrial.toWriteAudit(getText("RESTAD005"), "RESTAD005", ivrEntryByusrid);
					}else{
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", "booking not selected.");
						locRtnSrvId="SI00034";locRtnStsCd="1"; locRtnRspCode="E00034";locRtnMsg=getText("booking.select.error");
						//AuditTrial.toWriteAudit(getText("RESTAD006"), "RESTAD006", ivrEntryByusrid);
					}
				
				}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// apporal booking		
					if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
						 if (Commonutility.checkempty(ivrservicecode)) {							 
								if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {																		
									if (flg) {
										flg = commonServ.checkSocietyKey(societykey, ivrCurntusridstr);
									}
								} else {
									String[] passData = { getText("service.code.fixed.length") };
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
								}
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
							}
						 if (Commonutility.checkempty(townshipKey)) {
								if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
									flg=commonServ.checkTownshipKey(townshipKey,ivrCurntusridstr);									
								} else {
									String[] passData = { getText("townshipid.fixed.length") };
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
								}
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
							}
						 if (Commonutility.checkempty(societykey)) {
								if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
									
								} else {
									String[] passData = { getText("society.fixed.length") };
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
								}
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
							}
						} else {
							flg = true;
						}
						if (flg) {
							//update booking status to aproved
//							UserMasterTblVo userMst=new UserMasterTblVo();
//							otpDao otp=new otpDaoService();
							
//							if(societykey == null){
//								userMst = otp.checkUserDetails(ivrCurntusridstr);
//								societykey = userMst.getSocietyId().getActivationKey();
//							}
//							else{
//								userMst=otp.checkSocietyKeyForList(societykey, ivrCurntusridstr);
//							}
							
							
							
							String locvrBOOKING_ID  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "bookingid");
							
							String query="from FacilityBookingTblVO where bookingId='"+locvrBOOKING_ID+"'";
							
							FacilityDao facilityHbm=new FacilityDaoServices();
							
							FacilityBookingTblVO facilityBookingobj=new FacilityBookingTblVO();
							facilityBookingobj=facilityHbm.getFacilityBookingObjbyQuery(query);
							
							UserMasterTblVo userMst = facilityBookingobj.getEntryBy();
							societykey = userMst.getSocietyId().getActivationKey();
									
							Commonutility.toWriteConsole("Step xx : societykey: "+societykey +" ivrCurntusridstr ==== "+ivrCurntusridstr+" userMst:"+userMst);
							
//							locvrFnrst = BookingUtility.toApprovalBooking(locObjRecvdataJson,locObjsession, getText("RESTAD003"), "RESTAD003",userMst,facilityBookingobj, userArray, userprivacy);
							response = BookingUtility.toApprovalBooking(locObjRecvdataJson,locObjsession, getText("RESTAD003"), "RESTAD003",userMst,facilityBookingobj, userArray, userprivacy);
							locvrFnrst = response[1];
							
							//notification insert data
							NotificationDao notificationHbm=new NotificationDaoServices();
							AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
							String additionaldata=additionalDatafunc.formAdditionalDataForFacilityBookingTbl(facilityBookingobj);
							String decs=getText("notification.facility.accepted");
							notificationHbm.insertnewNotificationDetails(facilityBookingobj.getBookedBy(), decs, 5, 2, Integer.parseInt(locvrBOOKING_ID), userMst, additionaldata);
						
							//For notification update
							notificationHbm.updateNotificationDetails(5, facilityBookingobj.getBookingId(), additionaldata);
							/*END*/
						}
						if (locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							locObjRspdataJson.put("feed_id", response[0]);
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Booking approval successfully.");
							locRtnSrvId="R0030";locRtnStsCd="00"; locRtnRspCode="R0030";locRtnMsg=mobiCommon.getMsg("R0030");
							AuditTrial.toWriteAudit(getText("BKN0001"), "BKN0001", ivrEntryByusrid);
						} else {
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Booking not approved");
							locRtnSrvId="R0251";locRtnStsCd="01"; locRtnRspCode="R0251"; locRtnMsg=mobiCommon.getMsg("R0251");
							AuditTrial.toWriteAudit(getText("BKN0002"), "BKN0002", ivrEntryByusrid);
					}										
				} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// deActive booking	
					if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){						 
						 if (Commonutility.checkempty(ivrservicecode)) {
								if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {									
									if (flg) {
										flg = commonServ.checkSocietyKey(societykey, ivrCurntusridstr);
									}
								} else {
									String[] passData = { getText("service.code.fixed.length") };
									flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
								}
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
							}
						 if (Commonutility.checkempty(townshipKey)) {
								if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
									//success
									flg=commonServ.checkTownshipKey(townshipKey,ivrCurntusridstr);
								} else {
									String[] passData = { getText("townshipid.fixed.length") };
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
								}
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
							}
						 if (Commonutility.checkempty(societykey)) {
								if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
									
								} else {
									String[] passData = { getText("society.fixed.length") };
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
								}
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
							}
						} else {
							flg = true;
						}
					
						if (flg) {										
//							locvrFnrst = BookingUtility.toApprovalBooking(locObjRecvdataJson,locObjsession, getText("RESTAD003"), "RESTAD003", null , null, null, null);
							
							response = BookingUtility.toApprovalBooking(locObjRecvdataJson,locObjsession, getText("RESTAD003"), "RESTAD003", null , null, null, null);
							locvrFnrst = response[1];
							
							UserMasterTblVo userMst=new UserMasterTblVo();
							otpDao otp=new otpDaoService();
							userMst=otp.checkSocietyKeyForList(societykey,ivrCurntusridstr);
							
							FacilityDao facilityHbm=new FacilityDaoServices();
							FacilityBookingTblVO facilityBookingobj=new FacilityBookingTblVO();
							
							String locvrBOOKING_ID  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "bookingid");
							String query="from FacilityBookingTblVO where bookingId='"+locvrBOOKING_ID+"'";
							
							facilityBookingobj=facilityHbm.getFacilityBookingObjbyQuery(query);
							
							//start: update into feed
							//check is feed already present in database
							FeedsTblVO feedObj = null;
							FeedDAO feadhbm = new FeedDAOService();
							 feedObj = feadhbm.getFeedDetailsByFacilityBookId(Integer.parseInt(locvrBOOKING_ID));
							 System.out.println("BookingUpdate - getFeedDetailsByFacilityBookId locvrBOOKING_ID:"+locvrBOOKING_ID + " feedObj.getFeedId():"+feedObj.getFeedId());
							 if(feedObj !=null && feedObj.getFeedId()!=null){
								 //additional info
								 JSONObject additionalData = BookingUtility.getAdditionalFeedInfo(facilityBookingobj, userMst);
								 feedObj.setAdditionalData(additionalData.toString());
									
								 System.out.println("BookingUpdate - Before feedEditPrivacyflag getFeedId:"+feedObj.getFeedId());
								 feadhbm.feedEditPrivacyflag(feedObj, "", null, null, null, null);
								 System.out.println("BookingUpdate - After feedEditPrivacyflag getFeedId:"+feedObj.getFeedId());
							 }
							//end: update into feed
							
							NotificationDao notificationHbm=new NotificationDaoServices();
							AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
							String additionaldata=additionalDatafunc.formAdditionalDataForFacilityBookingTbl(facilityBookingobj);
							String decs=getText("notification.facility.declined");
							notificationHbm.insertnewNotificationDetails(facilityBookingobj.getBookedBy(), decs, 5, 2, Integer.parseInt(locvrBOOKING_ID), userMst, additionaldata);
						}
						
						if (locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){	
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Booking deactivate successfully.");
							locRtnSrvId="SI00036";locRtnStsCd="00"; locRtnRspCode="SI00036";locRtnMsg=getText("booking.deactivate.success");
							AuditTrial.toWriteAudit(getText("BKN0003"), "BKN0003", ivrEntryByusrid);
						} else {
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Booking not deactivate");
							locRtnSrvId="SI00036";locRtnStsCd="01"; locRtnRspCode="E00036"; locRtnMsg=getText("booking.deactivate.error");
							AuditTrial.toWriteAudit(getText("BKN0004"), "BKN0004", ivrEntryByusrid);
						}
					} else {
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI3001, "+getText("service.notmach"), "info", BookingUpdate.class);
						locRtnSrvId="SI3001";locRtnStsCd="1"; locRtnRspCode="SE3001"; locRtnMsg=getText("service.notmach");						
				}					
				serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson,iswebmobilefla);
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3001,"+getText("request.format.notmach")+"", "info", BookingUpdate.class);
				serverResponse("SI3001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);
			}
		} else {
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3001,"+getText("request.values.empty")+"", "info", BookingUpdate.class);
			serverResponse("SI3001","1","ER0001",getText("request.values.empty"),locObjRspdataJson,iswebmobilefla);
		}
	} catch (Exception e) {	
		Commonutility.toWriteConsole("Step -1 : Exception found in "+ Thread.currentThread().getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
		locObjRspdataJson=new JSONObject();
		log.logMessage("Service code : SI3001, Sorry, an unhandled error occurred", "error", BookingUpdate.class);
		serverResponse("SI3001","2","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
	} finally {
		if (locObjsession != null) {locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession = null;}
		locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
	}
	return SUCCESS;
}
private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla)
{
	PrintWriter out=null;
	JSONObject responseMsg = new JSONObject();
	HttpServletResponse response=null;
	response = ServletActionContext.getResponse();		
	try {
		if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
			out.print(as);
			out.close();
		}
		else{
		out = response.getWriter();
		responseMsg = new JSONObject();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		responseMsg.put("servicecode", serviceCode);
		responseMsg.put("statuscode", statusCode);
		responseMsg.put("respcode", respCode);
		responseMsg.put("message", message);
		responseMsg.put("data", dataJson);
		String as = responseMsg.toString();
		out.print(as);
		out.close();
		}
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
public String getIvrservicefor() {
	return ivrservicefor;
}
public void setIvrservicefor(String ivrservicefor) {
	this.ivrservicefor = ivrservicefor;
}

}
