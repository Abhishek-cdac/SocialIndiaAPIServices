package com.pack.validate;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ValidationUtilityService extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	public String execute(){
		Log lvrLog = null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null, ivrSocietyid=null, ivrTownshipid=null, ivrMobileNo=null;
		String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
		int ivrCurrntusrid=0;
		try {
			lvrLog = new Log();
			Commonutility.toWriteConsole("Step 1 : ValidationUtilityService called [Start]");
			lvrLog.logMessage("Step 1 : ValidationUtilityService called [Start]", "info", ValidationUtilityService.class);
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");														
					ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
					ivrSocietyid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyid");
					ivrTownshipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipid");
					ivrMobileNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileno");
					if (ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
						ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
					} else {
						ivrCurrntusrid = 0;
					}
					locObjsession = HibernateUtil.getSession();
					Commonutility.toWriteConsole("Step 2 : ivrSocietyid : "+ivrSocietyid + " Mobile no : "+ivrMobileNo);
					lvrLog.logMessage("Step 2 : ivrSocietyid : "+ivrSocietyid + " Mobile no : "+ivrMobileNo, "info", ValidationUtilityService.class);
					if (Commonutility.checkempty(ivrMobileNo) && Commonutility.checkempty(ivrSocietyid) ) {
						lsvSlQry = "select count(*) from UserMasterTblVo where statusFlag =1 and societyId = " + ivrSocietyid +" and mobileNo = '"+ivrMobileNo+"'";
						Query qy = locObjsession.createQuery(lsvSlQry);
						qy.setMaxResults(1);
						Long retval = (Long) qy.uniqueResult();	
				    	if (retval>0) {
				    		locObjRspdataJson = new JSONObject();
				    		locObjRspdataJson.put("existsflag","Exists");
				    		locRtnSrvId="SI1101";locRtnStsCd="00"; locRtnRspCode="SI1101";locRtnMsg="success";
				    	} else {
				    		locObjRspdataJson = new JSONObject();
				    		locObjRspdataJson.put("existsflag","NotExists");
				    		locRtnSrvId="SI1101";locRtnStsCd="00"; locRtnRspCode="SI1101";locRtnMsg="success";
				    	}
					} else {
						locObjRspdataJson = new JSONObject();
			    		locObjRspdataJson.put("existsflag","Error");
						locRtnSrvId="SI1101";locRtnStsCd="01"; locRtnRspCode="SI1101";locRtnMsg="Error";
					}
					
				} else {
					locObjRspdataJson = new JSONObject();
		    		locObjRspdataJson.put("existsflag","Error");
					locRtnSrvId="SI1101";locRtnStsCd="01"; locRtnRspCode="SI1101";locRtnMsg="Error";
				}
				serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);	
			}
		} catch(Exception e){ try {
			locObjRspdataJson = new JSONObject();
    		locObjRspdataJson.put("existsflag","Error"); }catch(Exception ee){}
			serverResponse("SI1101","02","SI1101","Error",locObjRspdataJson);	
			Commonutility.toWriteConsole("Step -1 : Exception found in ValidationUtilityService : "+e);
			lvrLog.logMessage("Step -1 : Exception : "+e, "info", ValidationUtilityService.class);
			
		} finally {
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}
			 ivrservicecode=null;ivrcurrntusridStr=null; ivrSocietyid=null; ivrTownshipid=null; ivrMobileNo=null;
			locRtnSrvId = null; locRtnStsCd = null; locRtnRspCode = null; locRtnMsg = null;
			locObjRecvJson = null;// Receive String to json
			locObjRecvdataJson = null;// Receive Data Json
			locObjRspdataJson = null;// Response Data Json
			lsvSlQry = null;
			locObjsession = null;	lvrLog = null;
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
}
