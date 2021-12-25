package com.pack.event;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Eventsupdates extends ActionSupport {
  
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
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode=null,ivrCurntusridstr=null;
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
		int ivrEntryByusrid = 0;
		try {
			log = new Log();
			if(ivrparams!= null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");				
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
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
					String locvrFnrst=null;
					String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
					locObjRspdataJson=new JSONObject();						
					
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Event
						log.logMessage("Step 1 : Event Insert precess will start.", "info", Eventsupdates.class);						
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.eventfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.eventfldr")+getText("external.inner.mobilepath");
						locvrFnrst = Eventutility.toInsertEvent(locObjRecvdataJson, getText("Grp.resident"), getText("EVEAD013"), "EVEAD013", locWebImgFldrPath,locMobImgFldrPath,log);//-call method
						String locSbt[]=locvrFnrst.split("!_!");					
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){								
								locRtnSrvId="SI8001";locRtnStsCd="00"; locRtnRspCode="R0140";/*locRtnMsg=getText("event.create.success");*/locRtnMsg=mobiCommon.getMsg("R0140");
								AuditTrial.toWriteAudit(getText("EVEAD001"), "EVEAD001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI8001";locRtnStsCd="01"; locRtnRspCode="R0141";/*locRtnMsg=getText("event.create.error");*/locRtnMsg=mobiCommon.getMsg("R0141");
								AuditTrial.toWriteAudit(getText("EVEAD002"), "EVEAD002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI8001";locRtnStsCd="01"; locRtnRspCode="R0141";/*locRtnMsg=getText("event.create.error");*/locRtnMsg=mobiCommon.getMsg("R0141");
							AuditTrial.toWriteAudit(getText("EVEAD002"), "EVEAD002", ivrEntryByusrid);
						}
						
						log.logMessage("Step 7 : Event Insert Process End.", "info", Eventsupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// Update Event
						log.logMessage("Step 1 : Event Update precess will start.", "info", Eventsupdates.class);
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.eventfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.eventfldr")+getText("external.inner.mobilepath");
						locvrFnrst = Eventutility.toUpdateEvent(locObjRecvdataJson,getText("Grp.resident"), getText("EVEAD013"), "EVEAD013", locWebImgFldrPath,locMobImgFldrPath);//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
							locRtnSrvId="SI8002";locRtnStsCd="00"; locRtnRspCode="R0142";/*locRtnMsg=getText("event.update.success");*/locRtnMsg=mobiCommon.getMsg("R0142");
							AuditTrial.toWriteAudit(getText("EVEAD003"), "EVEAD003", ivrEntryByusrid);
						}else{							
							locRtnSrvId="SI8002";locRtnStsCd="01"; locRtnRspCode="R0143";/*locRtnMsg=getText("event.update.error");*/locRtnMsg=mobiCommon.getMsg("R0143");
							AuditTrial.toWriteAudit(getText("EVEAD004"), "EVEAD004", ivrEntryByusrid);
						}
												
						log.logMessage("Step 7 : Event update Process End.", "info", Eventsupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive Event
						log.logMessage("Step 1 : Event Dactive precess will start.", "info", Eventsupdates.class);
						
						locvrFnrst = Eventutility.toDeactivateEvent(locObjRecvdataJson);//-call method 
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI8003";locRtnStsCd="00"; locRtnRspCode="R0144";/*locRtnMsg=getText("event.deactive.success");*/locRtnMsg=mobiCommon.getMsg("R0144");
							AuditTrial.toWriteAudit(getText("EVEAD005"), "EVEAD005", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI8003";locRtnStsCd="01"; locRtnRspCode="R0145";/*locRtnMsg=getText("event.deactive.error");*/locRtnMsg=mobiCommon.getMsg("R0145");
							AuditTrial.toWriteAudit(getText("EVEAD006"), "EVEAD006", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Event Dactive Process End.", "info", Eventsupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// Delete Event
						log.logMessage("Step 1 : Event Delete precess will start.", "info", Eventsupdates.class);
						
						locvrFnrst = Eventutility.toDeleteEvent(locObjRecvdataJson);//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI8004";locRtnStsCd="00"; locRtnRspCode="R0144";/*locRtnMsg=getText("event.delete.success");*/locRtnMsg=mobiCommon.getMsg("R0144");
							AuditTrial.toWriteAudit(getText("EVEAD007"), "EVEAD007", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI8004";locRtnStsCd="01"; locRtnRspCode="R0145";/*locRtnMsg=getText("event.delete.error");*/locRtnMsg=mobiCommon.getMsg("R0145");
							AuditTrial.toWriteAudit(getText("EVEAD008"), "EVEAD008", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Event Delete Process End.", "info", Eventsupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// Single Select Event
						log.logMessage("Step 1 : Event Select precess will start.", "info", Eventsupdates.class);										
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.eventfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.eventfldr")+getText("external.inner.mobilepath");
						JSONObject locRspjson = Eventutility.toseletEventsingledata(locObjRecvdataJson,locWebImgFldrPath,locMobImgFldrPath);//-call method
						locvrFnrst = (String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
						if(locvrFnrst == null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locRspjson;
							locRtnSrvId="SI8005";locRtnStsCd="00"; locRtnRspCode="R0146";/*locRtnMsg=getText("event.sigselect.success");*/locRtnMsg=mobiCommon.getMsg("R0146");
							AuditTrial.toWriteAudit(getText("EVEAD009"), "EVEAD009", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locRtnSrvId="SI8005";locRtnStsCd="01"; locRtnRspCode="R0147";/*locRtnMsg=getText("event.sigselect.error");*/locRtnMsg=mobiCommon.getMsg("R0147");
							AuditTrial.toWriteAudit(getText("EVEADO10"), "EVEADO10", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Event Select Process End.", "info", Eventsupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("6")){// Event Shared
						log.logMessage("Step 1 : Event Share process will start.", "info", Eventsupdates.class);
						locvrFnrst = Eventutility.toShareEvent(locObjRecvdataJson,getText("Grp.resident"));//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI8007";locRtnStsCd="00"; locRtnRspCode="S8007";locRtnMsg=getText("event.shared.success");
							AuditTrial.toWriteAudit(getText("EVEAD013"), "EVEAD013", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI8007";locRtnStsCd="01"; locRtnRspCode="E8007";locRtnMsg=getText("event.shared.error");
							AuditTrial.toWriteAudit(getText("EVEAD014"), "EVEAD014", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Event Share Process End.", "info", Eventsupdates.class);
					
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI8001, "+getText("service.notmach"), "info", Eventsupdates.class);
						locRtnSrvId="SI8001";locRtnStsCd="1"; locRtnRspCode="SE8001"; locRtnMsg=getText("service.notmach");						
					}	
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI8001,"+getText("request.format.notmach")+"", "info", Eventsupdates.class);
					serverResponse("SI8001","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}					
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI8001,"+getText("request.values.empty")+"", "info", Eventsupdates.class);
				serverResponse("SI8001","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found Eventsupdates.class execute() Method : "+e);			
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI8001, Sorry, an unhandled error occurred", "error", Eventsupdates.class);
			serverResponse("SI8001","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		}finally{
			log=null;			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
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
