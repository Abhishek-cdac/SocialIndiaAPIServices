package com.pack.DisputeLaborlist;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.DisputeAdminlist.DisputeAdminlistUtility;
import com.pack.DisputeAdminlist.DisputeAdminupdates;
import com.pack.DisputeMerchantlist.DisputeMerchantlistUtility;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class DisputeLaborupdates extends ActionSupport {
	
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
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert dispute Labour
							log.logMessage("Step 1 : dispute Labour Insert precess will start.", "info", DisputeLaborupdates.class);													
							locvrFnrst =  DisputeAdminlistUtility.toInsertComplaint(locObjRecvdataJson);//-call method
							String locSbt[]=locvrFnrst.split("!_!");
							if(locSbt!=null && locSbt.length>=2){
								if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
									locRtnSrvId="SI31001";locRtnStsCd="0"; locRtnRspCode="S31001";locRtnMsg=getText("disputelabor.create.success");
									AuditTrial.toWriteAudit(getText("DISPLBRAD001"), "DISPLBRAD001", ivrEntryByusrid);
								}else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("input") && !locSbt[0].equalsIgnoreCase("-1")){
									locRtnSrvId="SI31001";locRtnStsCd="0"; locRtnRspCode="S31001";locRtnMsg=getText("disputelabor.create.exist");
									AuditTrial.toWriteAudit(getText("DISPLBRAD001"), "DISPLBRAD001", ivrEntryByusrid);
								}
								else{
									locRtnSrvId="SI31001";locRtnStsCd="1"; locRtnRspCode="E31001";locRtnMsg=getText("disputelabor.create.error");
									AuditTrial.toWriteAudit(getText("DISPLBRAD002"), "DISPLBRAD002", ivrEntryByusrid);
								}
							}else{
								locRtnSrvId="SI31001";locRtnStsCd="1"; locRtnRspCode="E31001";locRtnMsg=getText("disputelabor.create.error");
								AuditTrial.toWriteAudit(getText("DISPLBRAD002"), "DISPLBRAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute Labour Insert Process End.", "info", DisputeLaborupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive dispute Labour
							log.logMessage("Step 1 : dispute Labour Dactive precess will start.", "info", DisputeLaborupdates.class);
							
							locvrFnrst = DisputeAdminlistUtility.toCloseddisputeadmin(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI31002";locRtnStsCd="0"; locRtnRspCode="S31002";locRtnMsg=getText("disputelabor.deactive.success");
								AuditTrial.toWriteAudit(getText("DISPLBRAD003"), "DISPLBRAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI31002";locRtnStsCd="1"; locRtnRspCode="E31002";locRtnMsg=getText("disputelabor.deactive.error");
								AuditTrial.toWriteAudit(getText("DISPLBRAD004"), "DISPLBRAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute Labour Dactive Process End.", "info", DisputeLaborupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// deActive dispute merchant
							log.logMessage("Step 1 : dispute tolabor Dactive precess will start.", "info", DisputeAdminupdates.class);
							
							locvrFnrst = DisputeAdminlistUtility.toDeletedisputeadmin(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI31005";locRtnStsCd="0"; locRtnRspCode="S31005";locRtnMsg=getText("disputelabor.delete.success");
								AuditTrial.toWriteAudit(getText("DISPLBRAD003"), "DISPLBRAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI31005";locRtnStsCd="1"; locRtnRspCode="E31005";locRtnMsg=getText("disputelabor.delete.error");
								AuditTrial.toWriteAudit(getText("DISPLBRAD004"), "DISPLBRAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute merchant Dactive Process End.", "info", DisputeAdminupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// View dispute Labour
							log.logMessage("Step 1 : dispute Labour active precess will start.", "info", DisputeLaborupdates.class);
							JSONObject locRspjson = DisputeLaborlistUtility.tolaborcmpltview(locObjRecvdataJson);//-call method
							locvrFnrst = (String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
							if(locvrFnrst == null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
								locObjRspdataJson=locRspjson;
								locRtnSrvId="SI31003";locRtnStsCd="0"; locRtnRspCode="S31003";locRtnMsg=getText("disputelabor.sigselect.success");
								AuditTrial.toWriteAudit(getText("DISPLBRAD014"), "DISPLBRAD014", ivrEntryByusrid);
							}else{
								locObjRspdataJson = new JSONObject();
								locRtnSrvId="SI31003";locRtnStsCd="1"; locRtnRspCode="E31003";locRtnMsg=getText("disputelabor.sigselect.error");
								AuditTrial.toWriteAudit(getText("DISPLBRAD013"), "DISPLBRAD013", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute Labour Dactive Process End.", "info", DisputeLaborupdates.class);
						}
						else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI11001, "+getText("service.notmach"), "info", DisputeLaborupdates.class);
							locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="SN0001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", DisputeLaborupdates.class);
						serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.values.empty")+"", "info", DisputeLaborupdates.class);
					serverResponse("SI11001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found Worktypelistupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001, Sorry, an unhandled error occurred", "error", DisputeLaborupdates.class);
				serverResponse("SI11001","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
