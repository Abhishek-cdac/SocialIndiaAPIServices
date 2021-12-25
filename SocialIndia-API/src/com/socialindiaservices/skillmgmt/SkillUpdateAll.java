package com.socialindiaservices.skillmgmt;

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

public class SkillUpdateAll extends ActionSupport{
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
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"entryby");									
					if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
						ivrEntryByusrid= Integer.parseInt(ivrCurntusridstr);
					}else{ ivrEntryByusrid=0; }
					
					if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
						ivrDecissBlkflag = ivrservicefor;
					}else{//post
						ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
					}		
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					System.out.println("ivrEntryByusrid -----------ivrEntryByusrid----- "+ivrEntryByusrid);
					String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
					String locvrFnrst=null;
					String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
					locObjRspdataJson=new JSONObject();						
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Feedback
						locvrFnrst = SkillUtility.toInsertSkill(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI380001";locRtnStsCd="0"; locRtnRspCode="S380001";locRtnMsg=getText("skill.insert.success");
								AuditTrial.toWriteAudit(getText("SKILL001"), "SKILL001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI380001";locRtnStsCd="1"; locRtnRspCode="E380001";locRtnMsg=getText("skill.insert.error");
								AuditTrial.toWriteAudit(getText("SKILL002"), "SKILL002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI380001";locRtnStsCd="1"; locRtnRspCode="E370001";locRtnMsg=getText("skill.insert.error");
							AuditTrial.toWriteAudit(getText("SKILL002"), "SKILL002", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : Why should Insert process end", "info", SkillUpdateAll.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// insert Feedback
						locObjRspdataJson = SkillUtility.toSelectSkill(locObjRecvdataJson,locObjsession);
						locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson,"catch");
						if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locObjRspdataJson;
							locRtnSrvId="SI3800003";locRtnStsCd="0"; locRtnRspCode="S3800003";locRtnMsg=getText("skill.select.success");
							AuditTrial.toWriteAudit(getText("SKILL003"), "SKILL003", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "SKILL not selected.");
							locRtnSrvId="SI3800003";locRtnStsCd="1"; locRtnRspCode="E3800003";locRtnMsg=getText("skill.select.error");
							AuditTrial.toWriteAudit(getText("SKILL004"), "SKILL004", ivrEntryByusrid);
						}
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// insert Feedback
						locvrFnrst = SkillUtility.toModifyskill(locObjRecvdataJson,locObjsession);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI3800002";locRtnStsCd="0"; locRtnRspCode="S380002";locRtnMsg=getText("skill.update.success");
								AuditTrial.toWriteAudit(getText("SKILL005"), "SKILL005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI3800002";locRtnStsCd="1"; locRtnRspCode="E370002";locRtnMsg=getText("skill.update.error");
								AuditTrial.toWriteAudit(getText("SKILL006"), "SKILL006", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI3700002";locRtnStsCd="1"; locRtnRspCode="E370002";locRtnMsg=getText("skill.update.error");
							AuditTrial.toWriteAudit(getText("SKILL006"), "SKILL006", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : SKILL Update process end", "info", SkillUpdateAll.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// insert Feedback
						locvrFnrst = SkillUtility.toActiveorDeactive(locObjRecvdataJson,locObjsession);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI3800004";locRtnStsCd="0"; locRtnRspCode="S380004";locRtnMsg=getText("skill.deactive.success");
								AuditTrial.toWriteAudit(getText("SKILL007"), "SKILL007", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI3800004";locRtnStsCd="1"; locRtnRspCode="E380004";locRtnMsg=getText("skill.deactive.error");
								AuditTrial.toWriteAudit(getText("SKILL008"), "SKILL008", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI3800004";locRtnStsCd="1"; locRtnRspCode="E380004";locRtnMsg=getText("skill.deactive.error");
							AuditTrial.toWriteAudit(getText("SKILL008"), "SKILL008", ivrEntryByusrid);
						}
						log.logMessage("Step 5 :  Skill active process end", "info", SkillUpdateAll.class);
						
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						AuditTrial.toWriteAudit(getText("SKILL008"), "SKILL008", ivrEntryByusrid);
						log.logMessage("Service code : SI380004, "+getText("service.notmach"), "info", SkillUpdateAll.class);
						locRtnSrvId="SI380004";locRtnStsCd="1"; locRtnRspCode="SE380004"; locRtnMsg=getText("service.notmach");	
					}
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					AuditTrial.toWriteAudit(getText("SKILL0016"), "SKILL0016", ivrEntryByusrid);
					log.logMessage("Service code : SI380001,"+getText("request.format.notmach")+"", "info", SkillUpdateAll.class);
					serverResponse("SI380001","1","EF380001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				AuditTrial.toWriteAudit(getText("SKILL0015"), "SKILL0015", ivrEntryByusrid);
				log.logMessage("Service code : SI380001,"+getText("request.values.empty")+"", "info", SkillUpdateAll.class);
				serverResponse("SI380001","1","ER380001",getText("request.values.empty"),locObjRspdataJson);

			}	
			
		}catch(Exception e){
			System.out.println("Exception found Skill.class execute() Method : " + e);
			 AuditTrial.toWriteAudit(getText("SKILL012"), "SKILL012", ivrEntryByusrid);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI380001, Sorry, an unhandled error occurred", "error", SkillUpdateAll.class);
			serverResponse("SI380001","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
