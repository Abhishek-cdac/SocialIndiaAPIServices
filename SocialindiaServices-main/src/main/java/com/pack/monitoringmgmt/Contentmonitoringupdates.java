package com.pack.monitoringmgmt;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.timelinefeed.Timelinefeedupdates;
import com.pack.timelinefeed.Timelinefeedutility;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Contentmonitoringupdates extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	// use for [insert, update, select, deActive, Delete]
	private String ivrparams;
	private String ivrservicecode;
	private String ivrservicefor;
	public String execute(){// need to change AuditTrial id,locRtnSrvId,locRtnSrvId="SI5001"; locRtnStsCd="0"; locRtnRspCode="E5001";locRtnMsg=getText("feedbck.insert.error"); on depend page
			Log log= new Log();
			JSONObject locObjRecvJson = null;//Receive String to json	
			JSONObject locObjRecvdataJson = null;// Receive Data Json		
			JSONObject locObjRspdataJson = null;// Response Data Json
			String lsvSlQry = null;		
			String ivrservicecode=null,ivrCurntusridstr=null;
			String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
			int ivrEntryByusrid=0;
			try{
				if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
					ivrparams = EncDecrypt.decrypt(ivrparams);
					boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
					if (ivIsJson) {
						locObjRecvJson = new JSONObject(ivrparams);
						ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");				
						ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
						if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
							ivrEntryByusrid= Integer.parseInt(ivrCurntusridstr);
						}else{ ivrEntryByusrid=0; }
					
						if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
							ivrDecissBlkflag = ivrservicefor;
						}else{//post
							ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
						}	
						locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
						String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
						String locvrFnrst=null;
						String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
						locObjRspdataJson=new JSONObject();						
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive Work  Type
							log.logMessage("Step 1 : Feed Content Dactive precess will start.", "info", Contentmonitoringupdates.class);
							
							//locvrFnrst = Monitoringutility.toBlockedContent(locObjRecvdataJson);//-call method
							locvrFnrst = Monitoringutility.toBlockedContentprocall(locObjRecvdataJson);
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI20002";locRtnStsCd="0"; locRtnRspCode="S20002";locRtnMsg=getText("feedtable.deactive.success");
								AuditTrial.toWriteAudit(getText("CNTMNTAD03"), "CNTMNTAD03", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI20002";locRtnStsCd="1"; locRtnRspCode="E20002";locRtnMsg=getText("feedtable.deactive.error");
								AuditTrial.toWriteAudit(getText("CNTMNTAD02"), "CNTMNTAD02", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Feed Content Dactive Process End.", "info", Timelinefeedupdates.class);
						} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// unblock Feed Content
							log.logMessage("Step 1 : Feed Content active precess will start.", "info", Contentmonitoringupdates.class);
							
							//locvrFnrst = Monitoringutility.toUnblockedContent(locObjRecvdataJson);//-call method  
							locvrFnrst = Monitoringutility.toUnblockedContentprocall(locObjRecvdataJson);
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI20003";locRtnStsCd="0"; locRtnRspCode="S20003";locRtnMsg=getText("feedtable.active.success");
								AuditTrial.toWriteAudit(getText("CNTMNTAD05"), "CNTMNTAD05", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI20003";locRtnStsCd="1"; locRtnRspCode="E20003";locRtnMsg=getText("feedtable.active.error");
								AuditTrial.toWriteAudit(getText("CNTMNTAD04"), "CNTMNTAD04", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Feed Content Dactive Process End.", "info", Timelinefeedupdates.class);
						} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// Activate user
							log.logMessage("Step 1 : User active precess will start.", "info", Contentmonitoringupdates.class);
							
							locvrFnrst = Monitoringutility.toActivatedDeactiveUser(locObjRecvdataJson, "1");//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI10006";locRtnStsCd="0"; locRtnRspCode="S10006";locRtnMsg=getText("resident.activate.success");
								AuditTrial.toWriteAudit(getText("RSAD007"), "RSAD007", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI10006";locRtnStsCd="1"; locRtnRspCode="E10006";locRtnMsg=getText("resident.activate.error");
								AuditTrial.toWriteAudit(getText("RSAD008"), "RSAD008", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : User Dactive Process End.", "info", Timelinefeedupdates.class);
						} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("6")){// DeActivate user
							log.logMessage("Step 1 : User active precess will start.", "info", Contentmonitoringupdates.class);
							
							locvrFnrst = Monitoringutility.toActivatedDeactiveUser(locObjRecvdataJson, "0");//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI10006";locRtnStsCd="0"; locRtnRspCode="S10006";locRtnMsg=getText("resident.deactivate.success");
								AuditTrial.toWriteAudit(getText("RSAD007"), "RSAD007", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI10006";locRtnStsCd="1"; locRtnRspCode="E10006";locRtnMsg=getText("resident.deactivate.error");
								AuditTrial.toWriteAudit(getText("RSAD008"), "RSAD008", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : User Dactive Process End.", "info", Timelinefeedupdates.class);
						}
						
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("7")){// DeActivate Signon Failure
							log.logMessage("Step 1 : User active precess will start.", "info", Contentmonitoringupdates.class);
							
							locvrFnrst = Monitoringutility.toDeleteSingonfailureRept(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI10012";locRtnStsCd="0"; locRtnRspCode="S10012";locRtnMsg=getText("signonfail.deactivate.success");
								AuditTrial.toWriteAudit(getText("SGNFAILAD001"), "SGNFAILAD001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI10012";locRtnStsCd="1"; locRtnRspCode="E10012";locRtnMsg=getText("signonfail.deactivate.error");
								AuditTrial.toWriteAudit(getText("SGNFAILAD002"), "SGNFAILAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : User Dactive Process End.", "info", Timelinefeedupdates.class);
						}
						
						else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI5001, "+getText("service.notmach"), "info", Contentmonitoringupdates.class);
							locRtnSrvId="SI5001";locRtnStsCd="1"; locRtnRspCode="SE5001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI5001,"+getText("request.format.notmach")+"", "info", Contentmonitoringupdates.class);
						serverResponse("SI5001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI5001,"+getText("request.values.empty")+"", "info", Contentmonitoringupdates.class);
					serverResponse("SI5001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found Timelinefeedupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI5001, Sorry, an unhandled error occurred", "error", Contentmonitoringupdates.class);
				serverResponse("SI5001","2","ER0002",getText("catch.error"),locObjRspdataJson);
			}finally{				
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