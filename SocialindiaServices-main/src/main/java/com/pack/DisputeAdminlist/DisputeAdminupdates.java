package com.pack.DisputeAdminlist;

import com.opensymphony.xwork2.ActionSupport;
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

public class DisputeAdminupdates extends ActionSupport {
	
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
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert dispute admin
							log.logMessage("Step 1 : dispute admin Insert precess will start.", "info", DisputeAdminupdates.class);
													
							locvrFnrst = DisputeAdminlistUtility.toInsertComplaint(locObjRecvdataJson);//-call method
							String locSbt[]=locvrFnrst.split("!_!");
							System.out.println("[]]]] "+locSbt);
							if(locSbt!=null && locSbt.length>=2){
								if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
									locRtnSrvId="SI32001";locRtnStsCd="0"; locRtnRspCode="S32001";locRtnMsg=getText("disputeadmin.create.success");
									AuditTrial.toWriteAudit(getText("DISPADMAD001"), "DISPADMAD001", ivrEntryByusrid);
								}else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("input") && !locSbt[0].equalsIgnoreCase("-1")){
									locRtnSrvId="SI32001";locRtnStsCd="0"; locRtnRspCode="S32001";locRtnMsg=getText("disputeadmin.create.exist");
									AuditTrial.toWriteAudit(getText("DISPADMAD001"), "DISPADMAD001", ivrEntryByusrid);
								}
								else{
									locRtnSrvId="SI32001";locRtnStsCd="1"; locRtnRspCode="E32001";locRtnMsg=getText("disputeadmin.create.error");
									AuditTrial.toWriteAudit(getText("DISPADMAD002"), "DISPADMAD002", ivrEntryByusrid);
								}
							}else{
								locRtnSrvId="SI32001";locRtnStsCd="1"; locRtnRspCode="E32001";locRtnMsg=getText("disputeadmin.create.error");
								AuditTrial.toWriteAudit(getText("DISPADMAD002"), "DISPADMAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute admin Insert Process End.", "info", DisputeAdminupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive dispute admin
							log.logMessage("Step 1 : dispute admin Dactive precess will start.", "info", DisputeAdminupdates.class);
							
							locvrFnrst = DisputeAdminlistUtility.toCloseddisputeadmin(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI32002";locRtnStsCd="0"; locRtnRspCode="S32002";locRtnMsg=getText("disputeadmin.deactive.success");
								AuditTrial.toWriteAudit(getText("DISPADMAD003"), "DISPADMAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI32002";locRtnStsCd="1"; locRtnRspCode="E32002";locRtnMsg=getText("disputeadmin.deactive.error");
								AuditTrial.toWriteAudit(getText("DISPADMAD004"), "DISPADMAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute admin Dactive Process End.", "info", DisputeAdminupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// deActive dispute admin
							log.logMessage("Step 1 : dispute admin Dactive precess will start.", "info", DisputeAdminupdates.class);
							
							locvrFnrst = DisputeAdminlistUtility.toDeletedisputeadmin(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI32005";locRtnStsCd="0"; locRtnRspCode="S32005";locRtnMsg=getText("disputeadmin.delete.success");
								AuditTrial.toWriteAudit(getText("DISPADMAD003"), "DISPADMAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI32005";locRtnStsCd="1"; locRtnRspCode="E32005";locRtnMsg=getText("disputeadmin.delete.error");
								AuditTrial.toWriteAudit(getText("DISPADMAD004"), "DISPADMAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute admin Dactive Process End.", "info", DisputeAdminupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// View dispute admin
							log.logMessage("Step 1 : dispute admin active precess will start.", "info", DisputeAdminupdates.class);
							JSONObject locRspjson = DisputeAdminlistUtility.toadmincmpltview(locObjRecvdataJson);//-call method
							locvrFnrst = (String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
							if(locvrFnrst == null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
								locObjRspdataJson=locRspjson;
								locRtnSrvId="SI32003";locRtnStsCd="0"; locRtnRspCode="S32003";locRtnMsg=getText("disputeadmin.sigselect.success");
								AuditTrial.toWriteAudit(getText("DISPADMAD014"), "DISPADMAD014", ivrEntryByusrid);
							}else{
								locObjRspdataJson = new JSONObject();
								locRtnSrvId="SI32003";locRtnStsCd="1"; locRtnRspCode="E32003";locRtnMsg=getText("disputeadmin.sigselect.error");
								AuditTrial.toWriteAudit(getText("DISPADMAD013"), "DISPADMAD013", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : dispute admin Dactive Process End.", "info", DisputeAdminupdates.class);
						}
						else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI11001, "+getText("service.notmach"), "info", DisputeAdminupdates.class);
							locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="SN0001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", DisputeAdminupdates.class);
						serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.values.empty")+"", "info", DisputeAdminupdates.class);
					serverResponse("SI11001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found Worktypelistupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001, Sorry, an unhandled error occurred", "error", DisputeAdminupdates.class);
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
