package com.socialindia.generalmgnt;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.Log;

public class WorkUpdate extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String ivrparams;	
	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		String ivrservicecode = null;
		int ivrstaffid;
		String ivr = null;
		String ivrDecissBlkflag = null; //  1- new create, 2- update, 3- select single[], 4- deActive ,5- delete, 
		try {
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");					
				locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
				ivrstaffid = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffid");
				ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ivrDecissBlkflag");
				String entryby = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"entryby");									
				String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);					
				String locvrFnrst=null;
				boolean result;
				//String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
				if (locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson = WorkUtility.toSelectWorkalertStaff(locObjRecvdataJson);									
					serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);										
				} else if (locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){				
					locvrFnrst = WorkUtility.toInsrtwrkAlert(locObjRecvdataJson);
					if (locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
						serverResponse("SI4008","00","R0114",mobiCommon.getMsg("R0114"),locObjRecvdataJson);
						AuditTrial.toWriteAudit(getText("STFAD005"), "STFAD005",Integer.parseInt(entryby));
					} else {
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("status", locvrFnrst);
						locObjRspdataJson.put("message", mobiCommon.getMsg("R0115"));
						//locRtnSrvId="SI3001";locRtnStsCd="01"; locRtnRspCode="R0115"; /*locRtnMsg=getText("salary.create.error");*/locRtnMsg=mobiCommon.getMsg("R0115");
						AuditTrial.toWriteAudit(getText("STFAD010"), "STFAD010",Integer.parseInt(entryby));
						serverResponse("SI4008","01","R0115",mobiCommon.getMsg("R0115"),locObjRecvdataJson);
					}
				} else if (locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){
						
				} else if (locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){										
				} else {
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI4003, Request services not correct", "info", staffUpdate.class);
						serverResponse("SI4003","01","R0111",mobiCommon.getMsg("R0111"),locObjRspdataJson);
				}					
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI4003, Request values are empty", "info", staffUpdate.class);
				serverResponse("SI4003","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception e) {
			System.out.println("Exception found SalaryUpdate.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI4003, Sorry, an unhandled error occurred", "error", staffUpdate.class);
			serverResponse("SI4003","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
		}
		return SUCCESS;
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson) {
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
	
	

}