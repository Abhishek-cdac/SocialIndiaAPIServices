package com.pack.flashnews;

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

public class FlashnewUpdate  extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	public String execute(){

		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json	
		String ivrCurntusridstr=null;  
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
		int ivrEntryByusrid=0;
		Session locObjsession = null;
		try{
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
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
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Feedback
						locvrFnrst = FlashNewUtility.toInsertFunction(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI360001";locRtnStsCd="0"; locRtnRspCode="S360001";locRtnMsg=getText("fnews.insert.success");
								AuditTrial.toWriteAudit(getText("FNEWS001"), "FNEWS001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI360001";locRtnStsCd="1"; locRtnRspCode="E330001";locRtnMsg=getText("fnews.insert.error");
								AuditTrial.toWriteAudit(getText("FNEWS002"), "FNEWS002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI360001";locRtnStsCd="1"; locRtnRspCode="E360001";locRtnMsg=getText("fnews.insert.error");
							AuditTrial.toWriteAudit(getText("FNEWS002"), "FNEWS002", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : function Insert process end", "info", FlashnewUpdate.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// insert Feedback
						locObjRspdataJson = FlashNewUtility.toSingleSelect(locObjRecvdataJson,locObjsession);
						locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson,"catch");
						if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locObjRspdataJson;
							locRtnSrvId="SI3600003";locRtnStsCd="0"; locRtnRspCode="S3600003";locRtnMsg=getText("fnews.select.success");
						//	AuditTrial.toWriteAudit(getText("FNEWS003"), "FNEWS003", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Facility not selected.");
							locRtnSrvId="SI3600003";locRtnStsCd="1"; locRtnRspCode="E3600003";locRtnMsg=getText("fnews.select.error");
							AuditTrial.toWriteAudit(getText("FNEWS004"), "FNEWS004", ivrEntryByusrid);
						}
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// insert Feedback
						locvrFnrst = FlashNewUtility.toUpdateFunction(locObjRecvdataJson,locObjsession);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI3600002";locRtnStsCd="0"; locRtnRspCode="S330001";locRtnMsg=getText("fnews.update.success");
								AuditTrial.toWriteAudit(getText("FNEWS005"), "FNEWS005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI3600002";locRtnStsCd="1"; locRtnRspCode="E330001";locRtnMsg=getText("fnews.update.error");
								AuditTrial.toWriteAudit(getText("FNEWS006"), "FNEWS006", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI3600002";locRtnStsCd="1"; locRtnRspCode="E330001";locRtnMsg=getText("fnews.update.error");
							AuditTrial.toWriteAudit(getText("FNEWS006"), "FNEWS006", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : function Insert process end", "info", FlashnewUpdate.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// insert Feedback
						locvrFnrst = FlashNewUtility.toDeleteaction(locObjRecvdataJson,locObjsession);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI3600002";locRtnStsCd="0"; locRtnRspCode="S330001";locRtnMsg=getText("fnews.deactive.success");
								AuditTrial.toWriteAudit(getText("FNEWS007"), "FNEWS007", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI3600002";locRtnStsCd="1"; locRtnRspCode="E330001";locRtnMsg=getText("fnews.deactive.error");
								AuditTrial.toWriteAudit(getText("FNEWS008"), "FNEWS008", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI3600002";locRtnStsCd="1"; locRtnRspCode="E330001";locRtnMsg=getText("fnews.deactive.error");
							AuditTrial.toWriteAudit(getText("FNEWS008"), "FNEWS008", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : function Insert process end", "info", FlashnewUpdate.class);
						
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						AuditTrial.toWriteAudit(getText("FNEWS009"), "FNEWS009", ivrEntryByusrid);
						log.logMessage("Service code : SI360001, "+getText("service.notmach"), "info", FlashnewUpdate.class);
						locRtnSrvId="SI360001";locRtnStsCd="1"; locRtnRspCode="SE330001"; locRtnMsg=getText("service.notmach");	
					}
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					AuditTrial.toWriteAudit(getText("FNEWS0010"), "FNEWS0010", ivrEntryByusrid);
					log.logMessage("Service code : SI360001,"+getText("request.format.notmach")+"", "info", FlashnewUpdate.class);
					serverResponse("SI360001","1","EF330001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				AuditTrial.toWriteAudit(getText("FNEWS0014"), "FNEWS0014", ivrEntryByusrid);
				log.logMessage("Service code : SI5001,"+getText("request.values.empty")+"", "info", FlashnewUpdate.class);
				serverResponse("SI360001","1","ER3300001",getText("request.values.empty"),locObjRspdataJson);

			}	
			
		}catch(Exception e){
			System.out.println("Exception found Flash newselectAll.class execute() Method : " + e);
			AuditTrial.toWriteAudit(getText("FNEWS0015"), "FNEWS0015", ivrEntryByusrid);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI360001, Sorry, an unhandled error occurred", "error", FlashnewUpdate.class);
			serverResponse("SI360001","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
