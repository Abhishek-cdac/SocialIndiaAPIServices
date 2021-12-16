package com.pack.complaints;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.event.Eventsupdates;
import com.pack.event.Eventutility;
import com.pack.feedback.FeedbackUtility;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.FacilityBookingTblVO;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class Complaintsupdates extends ActionSupport {
  private static final long serialVersionUID = 1L;
  private String ivrparams;
  private String ivrservicecode;
  private String ivrservicefor;
/**
 * execute .
 * need to change AuditTrial id,locRtnSrvId="SI5001"; 
 * locRtnStsCd="0"; locRtnRspCode="E5001";
 * locRtnMsg=getText("feedbck.insert.error"); on depend page
 */
  
public String execute() {

Log log = null;
JSONObject locObjRecvJson = null;//Receive String to json
JSONObject locObjRecvdataJson = null;// Receive Data Json
JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode=null,ivrCurntusridstr=null;
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block,7-image upload
    int ivrEntryByusrid = 0;
    boolean flg = true;
    StringBuilder locErrorvalStrBuil =null;
    String iswebmobilefla="";
try {
	CommonMobiDao commonServ = new CommonMobiDaoService();
	locErrorvalStrBuil = new StringBuilder();
			log = new Log();
			if(ivrparams!= null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("upd cpm ivrparams  "+ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");				
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
					 iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");
					String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
					String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
					if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
						ivrEntryByusrid= Integer.parseInt(ivrCurntusridstr);
					} else { ivrEntryByusrid=0; }
				
					if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
						ivrDecissBlkflag = ivrservicefor;
					} else {//post
						ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
					}	
					
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
					String locvrFnrst="";
					String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
					locObjRspdataJson=new JSONObject();						
					System.out.println("locvrDecissBlkflagchk   "+locvrDecissBlkflagchk);
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Complaint
						System.out.println("insert Complaint "+locvrDecissBlkflagchk);
						log.logMessage("Step 1 : Complaint Insert precess will start.", "info", Complaintsupdates.class);						
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.mobilepath");
						 if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
							 System.out.println("-----"+iswebmobilefla);
							 if (Commonutility.checkempty(ivrservicecode)) {
								 System.out.println("--ivrservicecode--"+ivrservicecode);
									if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
										 System.out.println("--ivrservicecode11--"+ivrservicecode);
										
										if(flg)
										{
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
										System.out.println("desflg=== "+flg);
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
							 }
							 else{
								 flg = true;
							 }
						 if(flg){
						locvrFnrst = Complaintstutility.toInsertComplaint(locObjRecvdataJson, getText("Grp.resident"), getText("CMPTAD013"), "CMPTAD013", locWebImgFldrPath,locMobImgFldrPath,log,iswebmobilefla);//-call method
						 }
						String locSbt[]=locvrFnrst.split("!_!");					
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){								
								locRtnSrvId="R0164";locRtnStsCd="00"; locRtnRspCode="R0164";locRtnMsg=mobiCommon.getMsg("R0164");
								AuditTrial.toWriteAudit(getText("CMPTAD001"), "CMPTAD001", ivrEntryByusrid);
								locObjRspdataJson.put("complaintid", Commonutility.toCheckNullEmpty(locSbt[1]));
							}else{
								locRtnSrvId="R0166";locRtnStsCd="01"; locRtnRspCode="R0166";locRtnMsg=mobiCommon.getMsg("R0166");
								AuditTrial.toWriteAudit(getText("CMPTAD002"), "CMPTAD002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="R0166";locRtnStsCd="01"; locRtnRspCode="R0166";locRtnMsg=mobiCommon.getMsg("R0166");
							AuditTrial.toWriteAudit(getText("CMPTAD002"), "CMPTAD002", ivrEntryByusrid);
						}
						
						log.logMessage("Step 7 : Complaint Insert Process End.", "info", Complaintsupdates.class);
					} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// Update Complaint
						log.logMessage("Step 1 : Complaint Update precess will start.", "info", Complaintsupdates.class);
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.mobilepath");						
						locvrFnrst = Complaintstutility.toUpdateComplaint(locObjRecvdataJson,getText("Grp.resident"), getText("CMPTAD013"), "CMPTAD013", locWebImgFldrPath,locMobImgFldrPath);//-call method
						System.out.println("return --- "+locvrFnrst);
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
							locRtnSrvId="SI9002";locRtnStsCd="0"; locRtnRspCode="S9002";locRtnMsg=getText("complaint.update.success");
							AuditTrial.toWriteAudit(getText("CMPTAD003"), "CMPTAD003", ivrEntryByusrid);
						}else{							
							locRtnSrvId="SI9002";locRtnStsCd="1"; locRtnRspCode="E9002";locRtnMsg=getText("complaint.update.error");
							AuditTrial.toWriteAudit(getText("CMPTAD004"), "CMPTAD004", ivrEntryByusrid);
						}
												
						log.logMessage("Step 7 : Complaint update Process End.", "info", Complaintsupdates.class);
					} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive Complaint
						 if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){							 
							 if (Commonutility.checkempty(ivrservicecode)) {
								 System.out.println("--ivrservicecode--"+ivrservicecode);
									if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
										 System.out.println("--ivrservicecode11--"+ivrservicecode);
										
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
										System.out.println("desflg=== "+flg);
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
							 } else{
								 flg = true;
							 }
						if (flg) {
						log.logMessage("Step 1 : Complaint Dactive precess will start.", "info", Complaintsupdates.class);						
						locvrFnrst = Complaintstutility.toDeactivateComplaint(locObjRecvdataJson);//-call method 
						if (iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")) {	
								String lvrComplaintid  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "complaintid");
								String cmplintstatusflg  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "status");
								System.out.println("lvrComplaintid--------------------"+lvrComplaintid);
								CommonMobiDao commonHbm=new CommonMobiDaoService();
								ComplaintsTblVO compliantMst=new ComplaintsTblVO();
								UserMasterTblVo userMst=new UserMasterTblVo();
								otpDao otp=new otpDaoService();
								userMst=otp.checkSocietyKeyForList(societykey,ivrCurntusridstr);
								compliantMst=commonHbm.getcomplaintMastTblById(Integer.parseInt(lvrComplaintid));
								NotificationDao notificationHbm=new NotificationDaoServices();
								AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
								String additionaldata=additionalDatafunc.formAdditionalDataForComplaintMastTbl(compliantMst);
								System.out.println("additionaldata------------"+additionaldata);
								String decs=getText("notification.cmplt.accepted");
								if(cmplintstatusflg!=null && cmplintstatusflg.equalsIgnoreCase("2")){
									decs=getText("notification.cmplt.declined");
								}
								String currUserId  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "crntusrloginid");
								System.out.println("currUserId======================" + currUserId);
								userMst.setUserId(Commonutility.stringToInteger(currUserId));
								System.out.println("userMst======================" + userMst.getUserId());
									notificationHbm.insertnewNotificationDetails(compliantMst.getUsrRegTblByFromUsrId(), decs, 0, 2, Integer.parseInt(lvrComplaintid), userMst, additionaldata);
							} else {

							}
						}
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
							locRtnSrvId="R0171";locRtnStsCd="00"; locRtnRspCode="R0171";locRtnMsg=mobiCommon.getMsg("R0171");
							AuditTrial.toWriteAudit(getText("CMPTAD005"), "CMPTAD005", ivrEntryByusrid);
						}else{							
							locRtnSrvId="R0172";locRtnStsCd="01"; locRtnRspCode="R0172";locRtnMsg=mobiCommon.getMsg("R0172");
							AuditTrial.toWriteAudit(getText("CMPTAD006"), "CMPTAD006", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Complaint Dactive Process End.", "info", Complaintsupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// Delete Complaint
						log.logMessage("Step 1 : Complaint Delete precess will start.", "info", Complaintsupdates.class);
						 if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
							 System.out.println("-----"+iswebmobilefla);
							 if (Commonutility.checkempty(ivrservicecode)) {
								 System.out.println("--ivrservicecode--"+ivrservicecode);
									if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
										 System.out.println("--ivrservicecode11--"+ivrservicecode);
										
										if(flg)
										{
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
										System.out.println("desflg=== "+flg);
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
							 }
							 else{
								 flg = true;
							 }
						if(flg){
						 locvrFnrst = Complaintstutility.toDeleteComplaint(locObjRecvdataJson);//-call method
						}
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
							locRtnSrvId="R0171";locRtnStsCd="00"; locRtnRspCode="R0171";locRtnMsg=mobiCommon.getMsg("R0171");
							AuditTrial.toWriteAudit(getText("CMPTAD007"), "CMPTAD007", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="R0171";locRtnStsCd="01"; locRtnRspCode="R0171";locRtnMsg=mobiCommon.getMsg("R0171");
							AuditTrial.toWriteAudit(getText("CMPTAD008"), "CMPTAD008", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Complaint Delete Process End.", "info", Complaintsupdates.class);
					} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// Single Select Complaint
						log.logMessage("Step 1 : Complaint Select precess will start.", "info", Complaintsupdates.class);					
						JSONObject locRspjson=Complaintstutility.toseletComplaintsingle(locObjRecvdataJson);//-call method	
						System.out.println("locRspjson:: "+locRspjson);
						locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
						System.out.println("locvrFnrst:: "+locvrFnrst);
						if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locRspjson;
							locRtnSrvId="SI9005";locRtnStsCd="0"; locRtnRspCode="S9005";locRtnMsg=getText("complaint.sigselect.success");
							AuditTrial.toWriteAudit(getText("CMPTAD009"), "CMPTAD009", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locRtnSrvId="SI9005";locRtnStsCd="1"; locRtnRspCode="E9005";locRtnMsg=getText("complaint.sigselect.error");
							AuditTrial.toWriteAudit(getText("CMPTAD010"), "CMPTAD010", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Complaint Select Process End.", "info", Complaintsupdates.class);
					} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("6")){// complaint Shared
						log.logMessage("Step 1 : complaint Share process will start.", "info", Eventsupdates.class);
						locvrFnrst = Complaintstutility.toShareCmplt(locObjRecvdataJson,getText("Grp.labor"));//-call method
						System.out.println("return::::: "+locvrFnrst);
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI8007";locRtnStsCd="0"; locRtnRspCode="S9007";locRtnMsg=getText("complaint.shared.success");
							AuditTrial.toWriteAudit(getText("CMPTAD013"), "CMPTAD013", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI8007";locRtnStsCd="1"; locRtnRspCode="E9007";locRtnMsg=getText("complaint.shared.error");
							AuditTrial.toWriteAudit(getText("CMPTAD014"), "CMPTAD014", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : complaint Share Process End.", "info", Complaintsupdates.class);
					
					}
					else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("7")){// image upload
						log.logMessage("Step 1 : complaint image upload process will start.", "info", Complaintsupdates.class);
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.mobilepath");
						locvrFnrst = Complaintstutility.toimguploadCmplt(locObjRecvdataJson,getText("Grp.resident"), getText("CMPTAD015"), "CMPTAD015", locWebImgFldrPath,locMobImgFldrPath);//-call method
						System.out.println("return::::: "+locvrFnrst);
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI8007";locRtnStsCd="0"; locRtnRspCode="S9011";locRtnMsg=getText("complaint.imgupload.success");
							AuditTrial.toWriteAudit(getText("CMPTAD015"), "CMPTAD015", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI8007";locRtnStsCd="1"; locRtnRspCode="E9011";locRtnMsg=getText("complaint.imgupload.error");
							AuditTrial.toWriteAudit(getText("CMPTAD016"), "CMPTAD016", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : complaint Image upload Process End.", "info", Complaintsupdates.class);
					
					}
					else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : R0164, "+getText("service.notmach"), "info", Complaintsupdates.class);
						locRtnSrvId="R0164";locRtnStsCd="1"; locRtnRspCode="SE9001"; locRtnMsg=getText("service.notmach");						
					}	
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson,iswebmobilefla);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : R0164,"+getText("request.format.notmach")+"", "info", Complaintsupdates.class);
					serverResponse("R0164","1","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);
				}					
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : R0164,"+getText("request.values.empty")+"", "info", Complaintsupdates.class);
				serverResponse("R0164","1","ER0001",getText("request.values.empty"),locObjRspdataJson,iswebmobilefla);

			}	
		}catch(Exception e){
			System.out.println("Exception found Complaintsupdates.class execute() Method : "+e);			
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : R0164, Sorry, an unhandled error occurred", "error", Complaintsupdates.class);
			serverResponse("R0164","2","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
		}finally{
			log=null;		
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
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
