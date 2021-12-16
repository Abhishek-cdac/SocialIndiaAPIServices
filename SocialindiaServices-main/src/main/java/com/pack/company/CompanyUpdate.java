package com.pack.company;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class CompanyUpdate extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private String ivrparams;	
	private String ivrservicefor;
	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		//String lsvSlQry = null;
		Session locObjsession = null;
		String ivrservicecode = null;
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
		String ivrCurntusridstr=null;
		int ivrEntryByusrid=0;
		try {
			locObjsession = HibernateUtil.getSession();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"currentloginid");
					if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
						ivrEntryByusrid= Integer.parseInt(ivrCurntusridstr);
					}else{ ivrEntryByusrid=0; }
					
					if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
						ivrDecissBlkflag = ivrservicefor;
					}else{//post
						ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
					}
					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
					String locvrFnrst=null;
					String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;					
					locObjRspdataJson=new JSONObject();					
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert company
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.companyfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.companyfldr")+getText("external.inner.mobilepath");
						locvrFnrst = CompanyUtility.toInsrtLabor(locObjRecvdataJson, getText("Grp.company"), getText("CMPYAD001"), "CMPYAD001",locWebImgFldrPath,locMobImgFldrPath);//company group name passing
						String locSbt[]=locvrFnrst.split("!_!");						
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locObjRspdataJson.put("status", locSbt[0]);
								locObjRspdataJson.put("message", mobiCommon.getMsg("R0095"));
								locObjRspdataJson.put("cmpnyid", Commonutility.toCheckNullEmpty(locSbt[1]));
								locRtnSrvId="SI6002"; locRtnStsCd="00"; locRtnRspCode="R0095"; /*locRtnMsg=getText("Company.created.success");*/locRtnMsg=mobiCommon.getMsg("R0095");
								AuditTrial.toWriteAudit(getText("CMPYAD001"), "CMPYAD001", ivrEntryByusrid);
							}else{
								locObjRspdataJson.put("status", locSbt[0]);
								locObjRspdataJson.put("message", mobiCommon.getMsg("R0096"));
								locObjRspdataJson.put("cmpnyid", Commonutility.toCheckNullEmpty(locSbt[1]));
								locRtnSrvId="SI6002";locRtnStsCd="01"; locRtnRspCode="R0096"; /*locRtnMsg=getText("company.create.error");*/locRtnMsg=mobiCommon.getMsg("R0096");						
								AuditTrial.toWriteAudit(getText("CMPYAD002"), "CMPYAD002", ivrEntryByusrid);
							}
						}else{
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0096"));
							locObjRspdataJson.put("cmpnyid", "");
							locRtnSrvId="SI6002";locRtnStsCd="01"; locRtnRspCode="R0096"; /*locRtnMsg=getText("labor.create.error");*/locRtnMsg=mobiCommon.getMsg("R0096");
							AuditTrial.toWriteAudit(getText("CMPYAD002"), "CMPYAD002", ivrEntryByusrid);
						}																
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// update company
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.companyfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.companyfldr")+getText("external.inner.mobilepath");						
						locvrFnrst=CompanyUtility.toUpdtCompany(locObjRecvdataJson, getText("CMPYAD005"), "CMPYAD005",locWebImgFldrPath,locMobImgFldrPath);						
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){	
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0097"));
							locRtnSrvId="SI6005";locRtnStsCd="00"; locRtnRspCode="R0097";/*locRtnMsg=getText("company.update.success");*/locRtnMsg=mobiCommon.getMsg("R0097");
							AuditTrial.toWriteAudit(getText("CMPYAD006"), "CMPYAD006", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0098"));
							locRtnSrvId="SI6005";locRtnStsCd="01"; locRtnRspCode="R0098"; /*locRtnMsg=getText("company.update.error");*/locRtnMsg=mobiCommon.getMsg("R0098");
							AuditTrial.toWriteAudit(getText("CMPYAD007"), "CMPYAD007", ivrEntryByusrid);
						}
																	
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){//select single labor detail						
						JSONObject locRspjson=CompanyUtility.toSltSingleCompanyDtl(locObjRecvdataJson,locObjsession, getText("CMPYAD004"), "CMPYAD004");					
						locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
						if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locRspjson;
							locRtnSrvId="SI6004";locRtnStsCd="0"; locRtnRspCode="S6004";locRtnMsg=getText("company.select.success");
							AuditTrial.toWriteAudit(getText("CMPYAD004"), "CMPYAD004", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", getText("company.select.error"));
							locRtnSrvId="SI6004";locRtnStsCd="1"; locRtnRspCode="E6004";locRtnMsg=getText("company.select.error");
							AuditTrial.toWriteAudit(getText("CMPYAD005"), "CMPYAD005", ivrEntryByusrid);
						}
					
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive labor						
						locvrFnrst = CompanyUtility.toDeActLabor(locObjRecvdataJson,locObjsession, getText("CMPYAD003"), "CMPYAD003");
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){	
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0099"));
							locRtnSrvId="SI6003";locRtnStsCd="00"; locRtnRspCode="R0099";/*locRtnMsg=getText("company.deactivate.success");*/locRtnMsg=mobiCommon.getMsg("R0099");
							AuditTrial.toWriteAudit(getText("CMPYAD003"), "CMPYAD003", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0100"));
							locRtnSrvId="SI6003";locRtnStsCd="01"; locRtnRspCode="R0100"; /*locRtnMsg=getText("company.deactivate.error");*/locRtnMsg=mobiCommon.getMsg("R0100");
							AuditTrial.toWriteAudit(getText("CMPYAD008"), "CMPYAD008", ivrEntryByusrid);
						}										
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI3001, R0111 - "+mobiCommon.getMsg("R0111"), "info", CompanyUpdate.class);
						locRtnSrvId="SI3001";locRtnStsCd="1"; locRtnRspCode="R0111"; /*locRtnMsg=getText("service.notmach");*/locRtnMsg=mobiCommon.getMsg("R0111");					
					}					
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI3001,"+getText("request.format.notmach")+"", "info", CompanyUpdate.class);
					serverResponse("SI3001","1","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3001,"+getText("request.values.empty")+" [or] "+mobiCommon.getMsg("R0002"), "info", CompanyUpdate.class);
				serverResponse("SI3001","1","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception e) {
			System.out.println("Exception found CompanyUpdate.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3001, Sorry, an unhandled error occurred", "error", CompanyUpdate.class);
			serverResponse("SI3001","2","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			if (locObjsession != null) {locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession = null;}
			locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
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
