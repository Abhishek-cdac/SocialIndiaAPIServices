package com.pack.merchantCategorylist;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class merchantCategorylistupdates extends ActionSupport {
	
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
			Session locObjsession=null;
			try{
				locObjsession = HibernateUtil.getSession();
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
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Merchant Category
							log.logMessage("Step 1 : Merchant Category Insert precess will start.", "info", merchantCategorylistupdates.class);
							String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.mrchcategorymstfldr")+getText("external.inner.webpath");
							String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.mrchcategorymstfldr")+getText("external.inner.mobilepath");				
							locvrFnrst = merchantCategorylistUtility.toInsertmerchantCategory(locObjRecvdataJson,locWebImgFldrPath,locMobImgFldrPath);//-call method
							String locSbt[]=locvrFnrst.split("!_!");
							System.out.println("[]]]] "+locSbt);
							if(locSbt!=null && locSbt.length>=2){
								if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
									locRtnSrvId="SI21001";locRtnStsCd="0"; locRtnRspCode="S21001";locRtnMsg=getText("merchantCategory.create.success");
									AuditTrial.toWriteAudit(getText("MERCCATAD001"), "MERCCATAD001", ivrEntryByusrid);
								}else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("input") && !locSbt[0].equalsIgnoreCase("-1")){
									locRtnSrvId="SI21001";locRtnStsCd="0"; locRtnRspCode="S21001";locRtnMsg=getText("merchantCategory.create.exist");
									AuditTrial.toWriteAudit(getText("MERCCATAD001"), "MERCCATAD001", ivrEntryByusrid);
								}
								else{
									locRtnSrvId="SI21001";locRtnStsCd="1"; locRtnRspCode="E21001";locRtnMsg=getText("merchantCategory.create.error");
									AuditTrial.toWriteAudit(getText("MERCCATAD002"), "MERCCATAD002", ivrEntryByusrid);
								}
							}else{
								locRtnSrvId="SI21001";locRtnStsCd="1"; locRtnRspCode="E21001";locRtnMsg=getText("merchantCategory.create.error");
								AuditTrial.toWriteAudit(getText("MERCCATAD002"), "MERCCATAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Merchant Category Insert Process End.", "info", merchantCategorylistupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive Merchant Category
							log.logMessage("Step 1 : Merchant Category Dactive precess will start.", "info", merchantCategorylistupdates.class);
							
							locvrFnrst = merchantCategorylistUtility.toDeactivemerchantCategory(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI21002";locRtnStsCd="0"; locRtnRspCode="S21002";locRtnMsg=getText("merchantCategory.deactive.success");
								AuditTrial.toWriteAudit(getText("MERCCATAD003"), "MERCCATAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI21002";locRtnStsCd="1"; locRtnRspCode="E21002";locRtnMsg=getText("merchantCategory.deactive.error");
								AuditTrial.toWriteAudit(getText("MERCCATAD004"), "MERCCATAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Merchant Category Dactive Process End.", "info", merchantCategorylistupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// deActive Merchant Category
							log.logMessage("Step 1 : Merchant Category active precess will start.", "info", merchantCategorylistupdates.class);
							
							locvrFnrst = merchantCategorylistUtility.toactivemerchantCategory(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI21003";locRtnStsCd="0"; locRtnRspCode="S21003";locRtnMsg=getText("merchantCategory.active.success");
								AuditTrial.toWriteAudit(getText("MERCCATAD005"), "MERCCATAD005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI21003";locRtnStsCd="1"; locRtnRspCode="E21003";locRtnMsg=getText("merchantCategory.active.error");
								AuditTrial.toWriteAudit(getText("MERCCATAD006"), "MERCCATAD006", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : Merchant Category Dactive Process End.", "info", merchantCategorylistupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// view  Category
							JSONObject locRspjson=merchantCategorylistUtility.toSltSingleMrchCategoryDtl(locObjRecvdataJson,locObjsession, getText("Category Selected"), "MERCCATAD006");					
							locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
							if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
								locObjRspdataJson=locRspjson;
								locRtnSrvId="SI15004";locRtnStsCd="0"; locRtnRspCode="S15004";locRtnMsg=getText("merchantCategory.select.success");
								//AuditTrial.toWriteAudit(getText("LBAD006"), "LBAD006", ivrEntryByusrid);
							}else{
								locObjRspdataJson=new JSONObject();
								locObjRspdataJson.put("status", locvrFnrst);
								locObjRspdataJson.put("message", "Labor not selected.");
								locRtnSrvId="SI15004";locRtnStsCd="0"; locRtnRspCode="E15004";locRtnMsg=getText("merchantCategory.select.error");
								//AuditTrial.toWriteAudit(getText("LBAD012"), "LBAD012", ivrEntryByusrid);
							}
							
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// update category
							String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.mrchcategorymstfldr")+getText("external.inner.webpath");
							String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.mrchcategorymstfldr")+getText("external.inner.mobilepath");					
							locvrFnrst=merchantCategorylistUtility.toUpdtMrchCategory(locObjRecvdataJson, getText("LBAD004"), "LBAD004",locWebImgFldrPath,locMobImgFldrPath);						
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){	
								locObjRspdataJson.put("status", locvrFnrst);
								locObjRspdataJson.put("message", "Merchant Category updated successfully.");
								locRtnSrvId="SI3001";locRtnStsCd="0"; locRtnRspCode="S3001";locRtnMsg=getText("merchantCategory.update.success");
								AuditTrial.toWriteAudit(getText("Category Updated"), "CATYAD007", ivrEntryByusrid);
							}else{
								locObjRspdataJson=new JSONObject();
								locObjRspdataJson.put("status", locvrFnrst);
								locObjRspdataJson.put("message", " Merchant Category not updated");
								locRtnSrvId="SI3001";locRtnStsCd="1"; locRtnRspCode="E3001"; locRtnMsg=getText("merchantCategory.update.error");
								AuditTrial.toWriteAudit(getText("Category Updating error"), "CATYAD008", ivrEntryByusrid);
							}
																		
						}
						else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI11001, "+getText("service.notmach"), "info", merchantCategorylistupdates.class);
							locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="SN0001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", merchantCategorylistupdates.class);
						serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.values.empty")+"", "info", merchantCategorylistupdates.class);
					serverResponse("SI11001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found merchantCategorylistupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001, Sorry, an unhandled error occurred", "error", merchantCategorylistupdates.class);
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
