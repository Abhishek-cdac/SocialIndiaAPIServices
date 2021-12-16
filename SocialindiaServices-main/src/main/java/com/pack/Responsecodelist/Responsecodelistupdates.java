package com.pack.Responsecodelist;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Responsecodelistupdates extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// use for [insert, update, select, deActive, Delete]
	private String ivrparams;
	private String ivrservicefor;
	public String execute() {// need to change AuditTrial id,locRtnSrvId,locRtnSrvId="SI5001"; locRtnStsCd="0"; locRtnRspCode="E5001";locRtnMsg=getText("feedbck.insert.error"); on depend page
			Log log= new Log();
			JSONObject locObjRecvJson = null;//Receive String to json	
			JSONObject locObjRecvdataJson = null;// Receive Data Json		
			JSONObject locObjRspdataJson = null;// Response Data Json
			String lsvSlQry = null;
			String ivrservicecode=null,ivrCurntusridstr=null;
			String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
			int ivrEntryByusrid=0;
			//Session locObjsession=null;
			try{
				//locObjsession = HibernateUtil.getSession();
				if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
					ivrparams = EncDecrypt.decrypt(ivrparams);
					boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
					if (ivIsJson) {
						locObjRecvJson = new JSONObject(ivrparams);
						ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");				
						ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"crntusrloginid");
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
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Blood type
							log.logMessage("Step 1 : Response Code Insert precess will start.", "info", Responsecodelistupdates.class);
													
							locvrFnrst = ResponsecodelistUtility.toInsertResponseCode(locObjRecvdataJson);//-call method
							String locSbt[]=locvrFnrst.split("!_!");
							if(locSbt!=null && locSbt.length>=2){
								if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
									locRtnSrvId="SI36001";locRtnStsCd="0"; locRtnRspCode="S37001";locRtnMsg=getText("Response Message Created.");
									AuditTrial.toWriteAudit(getText("RESPMSGAD001"), "RESPMSGAD001", ivrEntryByusrid);
								}else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("input") && !locSbt[0].equalsIgnoreCase("-1")){
									locRtnSrvId="SI37001";locRtnStsCd="0"; locRtnRspCode="S37001";locRtnMsg=getText("Response Message already exist.");
									AuditTrial.toWriteAudit(getText("RESPMSGAD001"), "RESPMSGAD001", ivrEntryByusrid);
								}
								else{
									locRtnSrvId="SI37001";locRtnStsCd="1"; locRtnRspCode="E37001";locRtnMsg=getText("Response Message Created.");
									AuditTrial.toWriteAudit(getText("RESPMSGAD002"), "RESPMSGAD002", ivrEntryByusrid);
								}
							}else{
								locRtnSrvId="SI37001";locRtnStsCd="1"; locRtnRspCode="E37001";locRtnMsg=getText("Response Message create error.");
								AuditTrial.toWriteAudit(getText("RESPMSGAD002"), "RESPMSGAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Response Code Insert Process End.", "info", Responsecodelistupdates.class);
						} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive Response Code
							log.logMessage("Step 1 : Response Code Dactive precess will start.", "info", Responsecodelistupdates.class);
							
							locvrFnrst = ResponsecodelistUtility.toDeactiveResponseCode(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI37003";locRtnStsCd="0"; locRtnRspCode="S37003";locRtnMsg=getText("responsecode.deactive.success");
								AuditTrial.toWriteAudit(getText("RESPMSGAD003"), "RESPMSGAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI37003";locRtnStsCd="1"; locRtnRspCode="E37003";locRtnMsg=getText("responsecode.deactive.error");
								AuditTrial.toWriteAudit(getText("RESPMSGAD004"), "RESPMSGAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Response Code Dactive Process End.", "info", Responsecodelistupdates.class);
						} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// deActive Response Code
							log.logMessage("Step 1 : Response Code Dactive precess will start.", "info", Responsecodelistupdates.class);
							System.out.println("********active****** "+locObjRecvdataJson);
							locvrFnrst = ResponsecodelistUtility.toactiveResponseCode(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI37005";locRtnStsCd="0"; locRtnRspCode="S37005";locRtnMsg=getText("responsecode.active.success");
								AuditTrial.toWriteAudit(getText("RESPMSGAD005"), "RESPMSGAD005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI37005";locRtnStsCd="1"; locRtnRspCode="E37005";locRtnMsg=getText("responsecode.active.error");
								AuditTrial.toWriteAudit(getText("RESPMSGAD006"), "RESPMSGAD006", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Response Code Dactive Process End.", "info", Responsecodelistupdates.class);
						} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// single select Response Code
							log.logMessage("Step 1 : Response Code updated precess will start.", "info", Responsecodelistupdates.class);
							System.out.println("************** "+locObjRecvdataJson);
							locvrFnrst = ResponsecodelistUtility.toSelectResponseMsg(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI37002";locRtnStsCd="0"; locRtnRspCode="S37002";locRtnMsg=getText("Response msg updated success");
								AuditTrial.toWriteAudit(getText("RESPMSGAD007"), "RESPMSGAD007", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI37002";locRtnStsCd="1"; locRtnRspCode="E37002";locRtnMsg=getText("Response msg updated error");
								AuditTrial.toWriteAudit(getText("RESPMSGAD008"), "RESPMSGAD008", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Response Code updated Process End.", "info", Responsecodelistupdates.class);
						} else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI11001, "+getText("service.notmach"), "info", Responsecodelistupdates.class);
							locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="SN0001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", Responsecodelistupdates.class);
						serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.values.empty")+"", "info", Responsecodelistupdates.class);
					serverResponse("SI11001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found Worktypelistupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001, Sorry, an unhandled error occurred", "error", Responsecodelistupdates.class);
				serverResponse("SI11001","2","ER0002",getText("catch.error"),locObjRspdataJson);
			}finally{					
				locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	System.gc();
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
