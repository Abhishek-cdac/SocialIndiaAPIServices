package com.pack.staffcategorylist;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Staffcategoryupdates extends ActionSupport {
	
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
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert staff category
							log.logMessage("Step 1 : staff category Insert precess will start.", "info", Staffcategoryupdates.class);
													
							locvrFnrst = StaffcategoryUtility.toInsertcategory(locObjRecvdataJson);//-call method
							String locSbt[]=locvrFnrst.split("!_!");
							System.out.println("[]]]] "+locSbt);
							if(locSbt!=null && locSbt.length>=2){
								if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
									locRtnSrvId="SI16001";locRtnStsCd="0"; locRtnRspCode="S16001";locRtnMsg=getText("StaffCategory.create.success");
									AuditTrial.toWriteAudit(getText("CATYAD001"), "CATYAD001", ivrEntryByusrid);
								}else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("input") && !locSbt[0].equalsIgnoreCase("-1")){
									locRtnSrvId="SI16001";locRtnStsCd="0"; locRtnRspCode="S16001";locRtnMsg=getText("StaffCategory.create.exist");
									AuditTrial.toWriteAudit(getText("CATYAD001"), "CATYAD001", ivrEntryByusrid);
								}
								else{
									locRtnSrvId="SI16001";locRtnStsCd="1"; locRtnRspCode="E16001";locRtnMsg=getText("StaffCategory.create.error");
									AuditTrial.toWriteAudit(getText("CATYAD002"), "CATYAD002", ivrEntryByusrid);
								}
							}else{
								locRtnSrvId="SI16001";locRtnStsCd="1"; locRtnRspCode="E16001";locRtnMsg=getText("StaffCategory.create.error");
								AuditTrial.toWriteAudit(getText("CATYAD002"), "CATYAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : staff category Insert Process End.", "info", Staffcategoryupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive staff category
							log.logMessage("Step 1 : staff category Dactive precess will start.", "info", Staffcategoryupdates.class);
							
							locvrFnrst = StaffcategoryUtility.toDeactivecategory(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI16002";locRtnStsCd="0"; locRtnRspCode="S16002";locRtnMsg=getText("StaffCategory.deactive.success");
								AuditTrial.toWriteAudit(getText("CATYAD003"), "CATYAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI16002";locRtnStsCd="1"; locRtnRspCode="E16002";locRtnMsg=getText("StaffCategory.deactive.error");
								AuditTrial.toWriteAudit(getText("CATYAD004"), "CATYAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : staff category Dactive Process End.", "info", Staffcategoryupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// deActive staff category
							log.logMessage("Step 1 : staff category active precess will start.", "info", Staffcategoryupdates.class);
							
							locvrFnrst = StaffcategoryUtility.toactivecategory(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI16003";locRtnStsCd="0"; locRtnRspCode="S16003";locRtnMsg=getText("StaffCategory.active.success");
								AuditTrial.toWriteAudit(getText("CATYAD005"), "CATYAD005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI16003";locRtnStsCd="1"; locRtnRspCode="E16003";locRtnMsg=getText("StaffCategory.active.error");
								AuditTrial.toWriteAudit(getText("CATYAD006"), "CATYAD006", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : staff category Dactive Process End.", "info", Staffcategoryupdates.class);
						}
						else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI11001, "+getText("service.notmach"), "info", Staffcategoryupdates.class);
							locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="SN0001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", Staffcategoryupdates.class);
						serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.values.empty")+"", "info", Staffcategoryupdates.class);
					serverResponse("SI11001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found Idcardupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001, Sorry, an unhandled error occurred", "error", Staffcategoryupdates.class);
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
