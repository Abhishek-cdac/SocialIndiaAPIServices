package com.pack.tender;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Tenderupdates extends ActionSupport {
  
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
					
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Tender
						log.logMessage("Step 1 : Tender Insert precess will start.", "info", Tenderupdates.class);						
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.tenderfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.tenderfldr")+getText("external.inner.mobilepath");
						locvrFnrst = Tenderutility.toInsertTender(locObjRecvdataJson, getText("Grp.resident"), getText("TENAD000"), "TENAD000", locWebImgFldrPath,locMobImgFldrPath,log);//-call method
						String locSbt[]=locvrFnrst.split("!_!");					
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){								
								locRtnSrvId="SI12000";locRtnStsCd="0"; locRtnRspCode="S12000";locRtnMsg=getText("tender.create.success");
								AuditTrial.toWriteAudit(getText("TENAD001"), "TENAD001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI12000";locRtnStsCd="1"; locRtnRspCode="E12000";locRtnMsg=getText("tender.create.error");
								AuditTrial.toWriteAudit(getText("TENAD002"), "TENAD002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI12000";locRtnStsCd="1"; locRtnRspCode="E12000";locRtnMsg=getText("tender.create.error");
							AuditTrial.toWriteAudit(getText("TENAD002"), "TENAD002", ivrEntryByusrid);
						}
						
						log.logMessage("Step 7 : Tender Insert Process End.", "info", Tenderupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// Update Tender
						log.logMessage("Step 1 : Tender Update precess will start.", "info", Tenderupdates.class);
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.tenderfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.tenderfldr")+getText("external.inner.mobilepath");
						locvrFnrst = Tenderutility.toUpdateTender(locObjRecvdataJson,getText("Grp.resident"), getText("TENAD000"), "TENAD000", locWebImgFldrPath,locMobImgFldrPath);//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
							locRtnSrvId="SI12002";locRtnStsCd="0"; locRtnRspCode="S12002";locRtnMsg=getText("tender.update.success");
							AuditTrial.toWriteAudit(getText("TENAD007"), "TENAD007", ivrEntryByusrid);
						}else{							
							locRtnSrvId="SI12002";locRtnStsCd="1"; locRtnRspCode="E12002";locRtnMsg=getText("tender.update.error");
							AuditTrial.toWriteAudit(getText("TENAD008"), "TENAD008", ivrEntryByusrid);
						}
												
						log.logMessage("Step 7 : Tender update Process End.", "info", Tenderupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive Tender
						log.logMessage("Step 1 : Tender Dactive precess will start.", "info", Tenderupdates.class);
						
						//locvrFnrst = Tenderutility.toDeactivateTender(locObjRecvdataJson);//-call method 
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI8003";locRtnStsCd="0"; locRtnRspCode="S8003";locRtnMsg=getText("tender.deactive.success");
							AuditTrial.toWriteAudit(getText("EVEADOO5"), "EVEADOO5", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI8003";locRtnStsCd="1"; locRtnRspCode="E8003";locRtnMsg=getText("tender.deactive.error");
							AuditTrial.toWriteAudit(getText("EVEADOO6"), "EVEADOO6", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Tender Dactive Process End.", "info", Tenderupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// Delete Tender
						log.logMessage("Step 1 : Tender Delete precess will start.", "info", Tenderupdates.class);
						
						locvrFnrst = Tenderutility.toDeleteTender(locObjRecvdataJson);//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI12004";locRtnStsCd="0"; locRtnRspCode="S12004";locRtnMsg=getText("tender.delete.success");
							AuditTrial.toWriteAudit(getText("TENAD003"), "TENAD003", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI12004";locRtnStsCd="1"; locRtnRspCode="E12004";locRtnMsg=getText("tender.delete.error");
							AuditTrial.toWriteAudit(getText("TENAD004"), "TENAD004", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Tender Delete Process End.", "info", Tenderupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// Single Select Tender
						log.logMessage("Step 1 : Tender Select precess will start.", "info", Tenderupdates.class);										
						JSONObject locRspjson = Tenderutility.toseletTendersingledata(locObjRecvdataJson);//-call method
						locvrFnrst = (String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
						if(locvrFnrst == null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locRspjson;
							locRtnSrvId="SI12001";locRtnStsCd="0"; locRtnRspCode="S12001";locRtnMsg=getText("tender.sigselect.success");
							AuditTrial.toWriteAudit(getText("TENAD005"), "TENAD005", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locRtnSrvId="SI12001";locRtnStsCd="1"; locRtnRspCode="E12001";locRtnMsg=getText("tender.sigselect.error");
							AuditTrial.toWriteAudit(getText("TENAD006"), "TENAD006", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Tender Select Process End.", "info", Tenderupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("6")){// Tender Shared
						log.logMessage("Step 1 : Tender Share process will start.", "info", Tenderupdates.class);
						//locvrFnrst = Tenderutility.toShareTender(locObjRecvdataJson,getText("Grp.resident"));//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI8007";locRtnStsCd="0"; locRtnRspCode="S8007";locRtnMsg=getText("tender.shared.success");
							AuditTrial.toWriteAudit(getText("EVEAD013"), "EVEAD013", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI8007";locRtnStsCd="1"; locRtnRspCode="E8007";locRtnMsg=getText("tender.shared.error");
							AuditTrial.toWriteAudit(getText("EVEAD014"), "EVEAD014", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Tender Share Process End.", "info", Tenderupdates.class);
					
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI8001, "+getText("service.notmach"), "info", Tenderupdates.class);
						locRtnSrvId="SI8001";locRtnStsCd="1"; locRtnRspCode="SE8001"; locRtnMsg=getText("service.notmach");						
					}	
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI8001,"+getText("request.format.notmach")+"", "info", Tenderupdates.class);
					serverResponse("SI8001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}					
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI8001,"+getText("request.values.empty")+"", "info", Tenderupdates.class);
				serverResponse("SI8001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found Tenderupdates.class execute() Method : "+e);			
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI8001, Sorry, an unhandled error occurred", "error", Tenderupdates.class);
			serverResponse("SI8001","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			log=null;		
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
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
