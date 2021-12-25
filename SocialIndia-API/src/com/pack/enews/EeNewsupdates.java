package com.pack.enews;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class EeNewsupdates extends ActionSupport {
	
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
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Event
							log.logMessage("Step 1 : eNews Insert precess will start.", "info", EeNewsupdates.class);
							String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.enewsfldr")+getText("external.inner.webpath");
							String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.enewsfldr")+getText("external.inner.mobilepath");							
							locvrFnrst = EeNewsUtility.toInserteNews(locObjRecvdataJson,  locWebImgFldrPath, locMobImgFldrPath);//-call method
							String locSbt[]=locvrFnrst.split("!_!");					
							if(locSbt!=null && locSbt.length>=2){
								if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
									locRtnSrvId="SI11001";locRtnStsCd="0"; locRtnRspCode="S11001";locRtnMsg=getText("enews.create.success");
									AuditTrial.toWriteAudit(getText("ENWSAD001"), "ENWSAD001", ivrEntryByusrid);
								}else{
									locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="E11001";locRtnMsg=getText("enews.create.error");
									AuditTrial.toWriteAudit(getText("ENWSAD002"), "ENWSAD002", ivrEntryByusrid);
								}
							}else{
								locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="E11001";locRtnMsg=getText("enews.create.error");
								AuditTrial.toWriteAudit(getText("ENWSAD002"), "ENWSAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : eNews Insert Process End.", "info", EeNewsupdates.class);
						}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// Update Event
							log.logMessage("Step 1 : eNews Update precess will start.", "info", EeNewsupdates.class);
							String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.enewsfldr")+getText("external.inner.webpath");
							String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.enewsfldr")+getText("external.inner.mobilepath");							
							locvrFnrst = EeNewsUtility.toUpdateeNews(locObjRecvdataJson, locWebImgFldrPath, locMobImgFldrPath);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI11002";locRtnStsCd="0"; locRtnRspCode="S11002";locRtnMsg=getText("enews.update.success");
								AuditTrial.toWriteAudit(getText("ENWSAD003"), "ENWSAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI11002";locRtnStsCd="1"; locRtnRspCode="E11002";locRtnMsg=getText("enews.update.error");
								AuditTrial.toWriteAudit(getText("ENWSAD004"), "ENWSAD004", ivrEntryByusrid);
							}
													
							log.logMessage("Step 5 : eNews update Process End.", "info", EeNewsupdates.class);
						}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive Event
							log.logMessage("Step 1 : eNews Dactive precess will start.", "info", EeNewsupdates.class);
							
							locvrFnrst = EeNewsUtility.toDeactiveeNews(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI11003";locRtnStsCd="0"; locRtnRspCode="S11003";locRtnMsg=getText("enews.deactive.success");
								AuditTrial.toWriteAudit(getText("ENWSAD005"), "ENWSAD005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI11003";locRtnStsCd="1"; locRtnRspCode="E11003";locRtnMsg=getText("enews.deactive.error");
								AuditTrial.toWriteAudit(getText("ENWSAD006"), "ENWSAD006", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : eNews Dactive Process End.", "info", EeNewsupdates.class);
						}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// Delete Event
							log.logMessage("Step 1 : eNews Delete precess will start.", "info", EeNewsupdates.class);
							
							locvrFnrst = EeNewsUtility.toDeleteeNews(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI11004";locRtnStsCd="0"; locRtnRspCode="S11004";locRtnMsg=getText("enews.delete.success");
								AuditTrial.toWriteAudit(getText("ENWSAD007"), "ENWSAD007", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI11004";locRtnStsCd="1"; locRtnRspCode="E11004";locRtnMsg=getText("enews.delete.error");
								AuditTrial.toWriteAudit(getText("ENWSAD008"), "ENWSAD008", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : eNews Delete Process End.", "info", EeNewsupdates.class);
						}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// Single Select Event
							log.logMessage("Step 1 : eNews Select precess will start.", "info", EeNewsupdates.class);
							String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.enewsfldr")+getText("external.inner.webpath");
							String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.enewsfldr")+getText("external.inner.mobilepath");
							JSONObject locRspjson = EeNewsUtility.toSelectSngleeNews(locObjRecvdataJson,locWebImgFldrPath,locMobImgFldrPath);//-call method
							locvrFnrst = (String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
							if(locvrFnrst == null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
								locObjRspdataJson=locRspjson;
								locRtnSrvId="SI11005";locRtnStsCd="0"; locRtnRspCode="S11005";locRtnMsg=getText("enews.sigselect.success");
								AuditTrial.toWriteAudit(getText("ENWSAD009"), "ENWSAD009", ivrEntryByusrid);
							}else{
								locObjRspdataJson = new JSONObject();
								locRtnSrvId = "SI11005";locRtnStsCd = "1"; locRtnRspCode="E11005";locRtnMsg=getText("enews.sigselect.error");
								AuditTrial.toWriteAudit(getText("ENWSAD010"), "ENWSAD010", ivrEntryByusrid);
							}
							
							log.logMessage("Step 6 : eNews Select Process End.", "info", EeNewsupdates.class);
						}else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI11001, "+getText("service.notmach"), "info", EeNewsupdates.class);
							locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="SN0001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", EeNewsupdates.class);
						serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.values.empty")+"", "info", EeNewsupdates.class);
					serverResponse("SI11001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found EeNewsupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001, Sorry, an unhandled error occurred", "error", EeNewsupdates.class);
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
