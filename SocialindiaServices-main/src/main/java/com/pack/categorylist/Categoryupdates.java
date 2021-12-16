package com.pack.categorylist;

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

public class Categoryupdates extends ActionSupport {
	
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
			Session locObjsession = null;		
			String ivrservicecode=null,ivrCurntusridstr=null;
			String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
			int ivrEntryByusrid=0;
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
						
						if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert category
							log.logMessage("Step 1 : category Insert precess will start.", "info", Categoryupdates.class);
							String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.categorymstfldr")+getText("external.inner.webpath");
							String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.categorymstfldr")+getText("external.inner.mobilepath");						
							locvrFnrst = CategoryUtility.toInsertcategory(locObjRecvdataJson,locWebImgFldrPath,locMobImgFldrPath);//-call method
							String locSbt[]=locvrFnrst.split("!_!");
							
							if(locSbt!=null && locSbt.length>=2){
								Commonutility.toWriteConsole("locSbt[0] RPK :: "+locSbt[0]);
								if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
									locRtnSrvId="SI15001";locRtnStsCd="0"; locRtnRspCode="S15001";locRtnMsg=getText("Category.create.success");
									AuditTrial.toWriteAudit(getText("CATYAD001"), "CATYAD001", ivrEntryByusrid);
								}else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("input") && !locSbt[0].equalsIgnoreCase("-1")){
									locRtnSrvId="SI15001";locRtnStsCd="2"; locRtnRspCode="S15001";locRtnMsg=getText("Category.create.exist");
									AuditTrial.toWriteAudit(getText("CATYAD001"), "CATYAD001", ivrEntryByusrid);
								} else{
									locRtnSrvId="SI15001";locRtnStsCd="1"; locRtnRspCode="E15001";locRtnMsg=getText("Category.create.error");
									AuditTrial.toWriteAudit(getText("CATYAD002"), "CATYAD002", ivrEntryByusrid);
								}
							}else{
								locRtnSrvId="SI15001";locRtnStsCd="1"; locRtnRspCode="E15001";locRtnMsg=getText("Category.create.error");
								AuditTrial.toWriteAudit(getText("CATYAD002"), "CATYAD002", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : category Insert Process End.", "info", Categoryupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive category
							log.logMessage("Step 1 : category Dactive precess will start.", "info", Categoryupdates.class);
							
							locvrFnrst = CategoryUtility.toDeactivecategory(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI15002";locRtnStsCd="0"; locRtnRspCode="S15002";locRtnMsg=getText("Category.deactive.success");
								AuditTrial.toWriteAudit(getText("CATYAD003"), "CATYAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI15002";locRtnStsCd="1"; locRtnRspCode="E15002";locRtnMsg=getText("Category.deactive.error");
								AuditTrial.toWriteAudit(getText("CATYAD004"), "CATYAD004", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : category Dactive Process End.", "info", Categoryupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// deActive category
							log.logMessage("Step 1 : category active precess will start.", "info", Categoryupdates.class);
							
							locvrFnrst = CategoryUtility.toactivecategory(locObjRecvdataJson);//-call method
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
								locRtnSrvId="SI15003";locRtnStsCd="0"; locRtnRspCode="S15003";locRtnMsg=getText("Category.active.success");
								AuditTrial.toWriteAudit(getText("CATYAD005"), "CATYAD005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI15003";locRtnStsCd="1"; locRtnRspCode="E15003";locRtnMsg=getText("Category.active.error");
								AuditTrial.toWriteAudit(getText("CATYAD006"), "CATYAD006", ivrEntryByusrid);
							}
							
							log.logMessage("Step 5 : category Dactive Process End.", "info", Categoryupdates.class);
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// view  Category
							JSONObject locRspjson=CategoryUtility.toSltSingleCategoryDtl(locObjRecvdataJson,locObjsession, getText("Category Selected"), "WRKTYPAD006");					
							locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
							if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
								locObjRspdataJson=locRspjson;
								locRtnSrvId="SI15004";locRtnStsCd="0"; locRtnRspCode="S15004";locRtnMsg=getText("category.select.success");
								//AuditTrial.toWriteAudit(getText("LBAD006"), "LBAD006", ivrEntryByusrid);
							}else{
								locObjRspdataJson=new JSONObject();
								locObjRspdataJson.put("status", locvrFnrst);
								locObjRspdataJson.put("message", "Labor not selected.");
								locRtnSrvId="SI15004";locRtnStsCd="0"; locRtnRspCode="E15004";locRtnMsg=getText("category.select.error");
								//AuditTrial.toWriteAudit(getText("LBAD012"), "LBAD012", ivrEntryByusrid);
							}
							
						}
						else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// update category
							String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.categorymstfldr")+getText("external.inner.webpath");
							String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.categorymstfldr")+getText("external.inner.mobilepath");					
							locvrFnrst=CategoryUtility.toUpdtCategory(locObjRecvdataJson, getText("LBAD004"), "LBAD004",locWebImgFldrPath,locMobImgFldrPath);						
							if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){	
								locObjRspdataJson.put("status", locvrFnrst);
								locObjRspdataJson.put("message", "Category updated successfully.");
								locRtnSrvId="SI3001";locRtnStsCd="0"; locRtnRspCode="S3001";locRtnMsg=getText("category.update.success");
								AuditTrial.toWriteAudit(getText("Category Updated"), "CATYAD007", ivrEntryByusrid);
							}else{
								locObjRspdataJson=new JSONObject();
								locObjRspdataJson.put("status", locvrFnrst);
								locObjRspdataJson.put("message", "Category not updated");
								locRtnSrvId="SI3001";locRtnStsCd="1"; locRtnRspCode="E3001"; locRtnMsg=getText("category.update.error");
								AuditTrial.toWriteAudit(getText("Category Updating error"), "CATYAD008", ivrEntryByusrid);
							}
																		
						}
						else{
							locvrFnrst="";
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI11001, "+getText("service.notmach"), "info", Categoryupdates.class);
							locRtnSrvId="SI11001";locRtnStsCd="1"; locRtnRspCode="SN0001"; locRtnMsg=getText("service.notmach");						
						}	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", Categoryupdates.class);
						serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.values.empty")+"", "info", Categoryupdates.class);
					serverResponse("SI11001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

				}	
			}catch(Exception e){
				System.out.println("Exception found Idcardupdates.class execute() Method : "+e);			
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001, Sorry, an unhandled error occurred", "error", Categoryupdates.class);
				serverResponse("SI11001","2","ER0002",getText("catch.error"),locObjRspdataJson);
			}finally{
				if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
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
