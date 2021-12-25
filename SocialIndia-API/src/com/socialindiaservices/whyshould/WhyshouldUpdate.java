package com.socialindiaservices.whyshould;

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

public class WhyshouldUpdate extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;

	public String execute() {

		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrCurntusridstr = null;
		String ivrDecissBlkflag = null; // 1- new create, 2- update, 3 - select
										// single[], 4 - deActive , 5 - delete ,
										// 6 - Block
		int ivrEntryByusrid = 0;
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
						locvrFnrst = WhyshouldUtility.toInsertWhyshould(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI370001";locRtnStsCd="0"; locRtnRspCode="S370001";locRtnMsg=getText("whyshould.insert.success");
								AuditTrial.toWriteAudit(getText("WHY001"), "WHY001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI370001";locRtnStsCd="1"; locRtnRspCode="E370001";locRtnMsg=getText("whyshould.insert.error");
								AuditTrial.toWriteAudit(getText("WHY002"), "WHY002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI370001";locRtnStsCd="1"; locRtnRspCode="E370001";locRtnMsg=getText("whyshould.insert.error");
							AuditTrial.toWriteAudit(getText("WHY002"), "WHY002", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : Why should Insert process end", "info", WhyshouldUpdate.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// insert Feedback
						locObjRspdataJson = WhyshouldUtility.toSelectwhy(locObjRecvdataJson,locObjsession);
						locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson,"catch");
						if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locObjRspdataJson;
							locRtnSrvId="SI3700003";locRtnStsCd="0"; locRtnRspCode="S3700003";locRtnMsg=getText("whyshould.select.success");
							//AuditTrial.toWriteAudit(getText("WHY003"), "WHY003", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "whyshould not selected.");
							locRtnSrvId="SI3700003";locRtnStsCd="1"; locRtnRspCode="E3600003";locRtnMsg=getText("whyshould.select.error");
							AuditTrial.toWriteAudit(getText("WHY004"), "WHY004", ivrEntryByusrid);
						}
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// insert Feedback
						locvrFnrst = WhyshouldUtility.toModifywhy(locObjRecvdataJson,locObjsession);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI3700002";locRtnStsCd="0"; locRtnRspCode="S370002";locRtnMsg=getText("whyshould.update.success");
								AuditTrial.toWriteAudit(getText("WHY005"), "WHY005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI3600002";locRtnStsCd="1"; locRtnRspCode="E370002";locRtnMsg=getText("whyshould.update.error");
								AuditTrial.toWriteAudit(getText("WHY006"), "WHY006", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI3700002";locRtnStsCd="1"; locRtnRspCode="E370002";locRtnMsg=getText("whyshould.update.error");
							AuditTrial.toWriteAudit(getText("WHY006"), "WHY006", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : Why should Insert process end", "info", WhyshouldUpdate.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// insert Feedback
						locvrFnrst = WhyshouldUtility.toActiveorDeactive(locObjRecvdataJson,locObjsession);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI3700004";locRtnStsCd="0"; locRtnRspCode="S370004";locRtnMsg=getText("whyshould.deactive.success");
								AuditTrial.toWriteAudit(getText("WHY007"), "WHY007", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI3700004";locRtnStsCd="1"; locRtnRspCode="E370004";locRtnMsg=getText("whyshould.deactive.error");
								AuditTrial.toWriteAudit(getText("WHY008"), "WHY008", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI3700002";locRtnStsCd="1"; locRtnRspCode="E370004";locRtnMsg=getText("whyshould.deactive.error");
							AuditTrial.toWriteAudit(getText("WHY008"), "WHY008", ivrEntryByusrid);
						}
						log.logMessage("Step 5 :  Why should Insert process end", "info", WhyshouldUpdate.class);
						
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						AuditTrial.toWriteAudit(getText("WHY008"), "WHY008", ivrEntryByusrid);
						log.logMessage("Service code : SI370004, "+getText("service.notmach"), "info", WhyshouldUpdate.class);
						locRtnSrvId="SI370004";locRtnStsCd="1"; locRtnRspCode="SE370004"; locRtnMsg=getText("service.notmach");	
					}
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					AuditTrial.toWriteAudit(getText("WHY008"), "WHY008", ivrEntryByusrid);
					log.logMessage("Service code : SI370001,"+getText("request.format.notmach")+"", "info", WhyshouldUpdate.class);
					serverResponse("SI370001","1","EF370001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				AuditTrial.toWriteAudit(getText("WHY0017"), "WHY0017", ivrEntryByusrid);
				log.logMessage("Service code : SI370001,"+getText("request.values.empty")+"", "info", WhyshouldUpdate.class);
				serverResponse("SI370001","1","ER370001",getText("request.values.empty"),locObjRspdataJson);

			}	
			
		}catch(Exception e){
			System.out.println("Exception found WhyshouldUpdate.class execute() Method : " + e);
			AuditTrial.toWriteAudit(getText("WHY008"), "WHY008", ivrEntryByusrid);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI360001, Sorry, an unhandled error occurred", "error", WhyshouldUpdate.class);
			serverResponse("SI370001","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
