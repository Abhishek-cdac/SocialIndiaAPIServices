package com.pack.resident;

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

public class ResidentUpdate extends ActionSupport {
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
				if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert resident
					locvrFnrst = ResidentUtility.toInsrtResident(locObjRecvdataJson, getText("Grp.resident"), getText("RESTAD009"), "RESTAD009");//resident group name passing
					String locSbt[]=locvrFnrst.split("!_!");						
					if(locSbt!=null && locSbt.length>=2){
						if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[1].equalsIgnoreCase("-1")&& !locSbt[1].equalsIgnoreCase("0")){
							locObjRspdataJson.put("status", locSbt[0]);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0101"));
							locRtnSrvId="SI7001";locRtnStsCd="00"; locRtnRspCode="R0101";/*locRtnMsg=getText("Resident.created.success");*/locRtnMsg=mobiCommon.getMsg("R0101");
							AuditTrial.toWriteAudit(getText("RESTAD0003"), "RESTAD0003", ivrEntryByusrid);
						} else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("existmob")){
							locObjRspdataJson.put("status", locSbt[0]);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0101"));
							locRtnSrvId="SI7001";locRtnStsCd="00"; locRtnRspCode="R0101";/*locRtnMsg=getText("Resident.created.success");*/locRtnMsg=mobiCommon.getMsg("R0101");
							AuditTrial.toWriteAudit(getText("RESTAD0003"), "RESTAD0003", ivrEntryByusrid);
//							locObjRspdataJson.put("status", locSbt[0]);
//							locObjRspdataJson.put("message", mobiCommon.getMsg("R0092"));
//							locRtnSrvId="SI7001";locRtnStsCd="01"; locRtnRspCode="R0092"; /*locRtnMsg=getText("Resident.created.error");*/locRtnMsg=mobiCommon.getMsg("R0092");					
//							AuditTrial.toWriteAudit(getText("RESTAD0004"), "RESTAD0004", ivrEntryByusrid);
						} else{
						
							locObjRspdataJson.put("status", locSbt[0]);
							locObjRspdataJson.put("message", mobiCommon.getMsg("R0102"));
							locRtnSrvId="SI7001";locRtnStsCd="01"; locRtnRspCode="R0102"; /*locRtnMsg=getText("Resident.created.error");*/locRtnMsg=mobiCommon.getMsg("R0102");					
							AuditTrial.toWriteAudit(getText("RESTAD0004"), "RESTAD0004", ivrEntryByusrid);
						}
					}else{
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", mobiCommon.getMsg("R0102"));
						locRtnSrvId="SI7001";locRtnStsCd="01"; locRtnRspCode="R0102"; /*locRtnMsg=getText("Resident.created.error");*/locRtnMsg=mobiCommon.getMsg("R0102");
						AuditTrial.toWriteAudit(getText("RESTAD0004"), "RESTAD0004", ivrEntryByusrid);
					}																
				} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// update resident
										
					locvrFnrst=ResidentUtility.toUpdtResident(locObjRecvdataJson, getText("RESTAD009"), "RESTAD009");						
					if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){	
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", mobiCommon.getMsg("R0103"));
						locRtnSrvId="SI7004";locRtnStsCd="00"; locRtnRspCode="R0103";/*locRtnMsg=getText("resident.update.success");*/locRtnMsg=mobiCommon.getMsg("R0103");
						AuditTrial.toWriteAudit(getText("RESTAD007"), "RESTAD007", ivrEntryByusrid);
					}else{
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", mobiCommon.getMsg("R0104"));
						locRtnSrvId="SI7004";locRtnStsCd="01"; locRtnRspCode="R0104"; /*locRtnMsg=getText("resident.update.error");*/locRtnMsg=mobiCommon.getMsg("R0104");
						AuditTrial.toWriteAudit(getText("RESTAD008"), "RESTAD008", ivrEntryByusrid);
					}
																
				} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){//select single resident detail						
					JSONObject locRspjson=ResidentUtility.toSltSingleResidentDtl(locObjRecvdataJson,locObjsession, getText("RESTAD005"), "RESTAD005");					
					locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
					if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
						locObjRspdataJson=locRspjson;
						locRtnSrvId="SI7003";locRtnStsCd="0"; locRtnRspCode="S7003";locRtnMsg=getText("resident.select.success");
						AuditTrial.toWriteAudit(getText("RESTAD005"), "RESTAD005", ivrEntryByusrid);
					}else{
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", "Resident not selected.");
						locRtnSrvId="SI7003";locRtnStsCd="1"; locRtnRspCode="E7003";locRtnMsg=getText("resident.select.error");
						AuditTrial.toWriteAudit(getText("RESTAD006"), "RESTAD006", ivrEntryByusrid);
					}
				
				} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive resident						
					locvrFnrst = ResidentUtility.toDeActResident(locObjRecvdataJson,locObjsession, getText("RESTAD003"), "RESTAD003");
					if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){	
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", mobiCommon.getMsg("R0105"));
						locRtnSrvId="SI7002";locRtnStsCd="00"; locRtnRspCode="R0105";/*locRtnMsg=getText("resident.deactivate.success");*/locRtnMsg=mobiCommon.getMsg("R0105");
						AuditTrial.toWriteAudit(getText("RESTAD003"), "RESTAD003", ivrEntryByusrid);
					}else{
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", mobiCommon.getMsg("R0106"));
						locRtnSrvId="SI7002";locRtnStsCd="01"; locRtnRspCode="R0106"; /*locRtnMsg=getText("resident.deactivate.error");*/locRtnMsg=mobiCommon.getMsg("R0106");
						AuditTrial.toWriteAudit(getText("RESTAD004"), "RESTAD004", ivrEntryByusrid);
					}										
				}else{
					locvrFnrst="";
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI3001, "+getText("service.notmach"), "info", ResidentUpdate.class);
					locRtnSrvId="SI3001";locRtnStsCd="01"; locRtnRspCode="R0004"; /*locRtnMsg=getText("service.notmach");*/locRtnMsg=mobiCommon.getMsg("R0004");						
				}					
				serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3001,"+getText("request.format.notmach")+", R0067: "+mobiCommon.getMsg("R0067"), "info", ResidentUpdate.class);
				serverResponse("SI3001","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
			}
		} else {
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3001,"+getText("request.values.empty")+" [or] "+mobiCommon.getMsg("R0002"), "info", ResidentUpdate.class);
			serverResponse("SI3001","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}
	} catch (Exception e) {
		System.out.println("Exception found ResidentUpdate.class execute() Method : " + e);
		locObjRspdataJson=new JSONObject();
		log.logMessage("Service code : SI3001, Sorry, an unhandled error occurred R0003 : "+mobiCommon.getMsg("R0003"), "error", ResidentUpdate.class);
		serverResponse("SI3001","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
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
