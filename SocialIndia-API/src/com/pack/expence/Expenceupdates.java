package com.pack.expence;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Expenceupdates extends ActionSupport {
  
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
					
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Expence
						log.logMessage("Step 1 : Expence Insert precess will start.", "info", Expenceupdates.class);						
				
						locvrFnrst = Expenceutility.toInsertExpence(locObjRecvdataJson,log);//-call method
						String locSbt[]=locvrFnrst.split("!_!");					
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){								
								locRtnSrvId="SI13000";locRtnStsCd="0"; locRtnRspCode="S13000";locRtnMsg=getText("expence.create.success");
								AuditTrial.toWriteAudit(getText("EXPSAD001"), "EXPSAD001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI13000";locRtnStsCd="1"; locRtnRspCode="E13000";locRtnMsg=getText("expence.create.error");
								AuditTrial.toWriteAudit(getText("EXPSAD002"), "EXPSAD002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI13000";locRtnStsCd="1"; locRtnRspCode="E13000";locRtnMsg=getText("expence.create.error");
							AuditTrial.toWriteAudit(getText("TENAD002"), "TENAD002", ivrEntryByusrid);
						}
						
						log.logMessage("Step 7 : Expence Insert Process End.", "info", Expenceupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// Update Expence
						log.logMessage("Step 1 : Expence Update precess will start.", "info", Expenceupdates.class);
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.expencefldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.expencefldr")+getText("external.inner.mobilepath");
						locvrFnrst = Expenceutility.toUpdateExpence(locObjRecvdataJson,getText("Grp.resident"), getText("TENAD000"), "TENAD000", locWebImgFldrPath,locMobImgFldrPath);//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
							locRtnSrvId="SI13002";locRtnStsCd="0"; locRtnRspCode="S13002";locRtnMsg=getText("expence.update.success");
							AuditTrial.toWriteAudit(getText("TENAD007"), "TENAD007", ivrEntryByusrid);
						}else{							
							locRtnSrvId="SI13002";locRtnStsCd="1"; locRtnRspCode="E13002";locRtnMsg=getText("expence.update.error");
							AuditTrial.toWriteAudit(getText("TENAD008"), "TENAD008", ivrEntryByusrid);
						}
												
						log.logMessage("Step 7 : Expence update Process End.", "info", Expenceupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// Delete Expence
						log.logMessage("Step 1 : Expence Delete precess will start.", "info", Expenceupdates.class);
						
						locvrFnrst = Expenceutility.toDeleteExpence(locObjRecvdataJson);//-call method
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI13004";locRtnStsCd="0"; locRtnRspCode="S13004";locRtnMsg=getText("expence.delete.success");
							AuditTrial.toWriteAudit(getText("EXPSAD003"), "EXPSAD003", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI13004";locRtnStsCd="1"; locRtnRspCode="E13004";locRtnMsg=getText("expence.delete.error");
							AuditTrial.toWriteAudit(getText("EXPSAD004"), "EXPSAD004", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Expence Delete Process End.", "info", Expenceupdates.class);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// Single Select Expence
						log.logMessage("Step 1 : Expence Select precess will start.", "info", Expenceupdates.class);										
						JSONObject locRspjson = Expenceutility.toseletExpencesingledata(locObjRecvdataJson);//-call method
						locvrFnrst = (String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
						if(locvrFnrst == null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locRspjson;
							locRtnSrvId="SI13001";locRtnStsCd="0"; locRtnRspCode="S13001";locRtnMsg=getText("expence.sigselect.success");
							AuditTrial.toWriteAudit(getText("TENAD005"), "TENAD005", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locRtnSrvId="SI13001";locRtnStsCd="1"; locRtnRspCode="E13001";locRtnMsg=getText("expence.sigselect.error");
							AuditTrial.toWriteAudit(getText("TENAD006"), "TENAD006", ivrEntryByusrid);
						}
						
						log.logMessage("Step 5 : Expence Select Process End.", "info", Expenceupdates.class);
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI8001, "+getText("service.notmach"), "info", Expenceupdates.class);
						locRtnSrvId="SI8001";locRtnStsCd="1"; locRtnRspCode="SE8001"; locRtnMsg=getText("service.notmach");						
					}	
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI8001,"+getText("request.format.notmach")+"", "info", Expenceupdates.class);
					serverResponse("SI8001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}					
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI8001,"+getText("request.values.empty")+"", "info", Expenceupdates.class);
				serverResponse("SI8001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found Expenceupdates.class execute() Method : "+e);			
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI8001, Sorry, an unhandled error occurred", "error", Expenceupdates.class);
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
