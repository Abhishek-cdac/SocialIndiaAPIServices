package com.socialindiaservices.reports;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.common.CommonDaoService;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Solvedreportandotptable extends ActionSupport{

	
		  private static final long serialVersionUID = 1L;
		private String ivrparams;
		
		public String execute() {
		    Log logWrite = null;
			JSONObject locObjRecvJson = null;//Receive over all Json	[String Received]
			JSONObject locObjRecvdataJson = null;// Receive Data Json		
			JSONObject locObjRspdataJson = null;// Response Data Json
			Session locObjsession = null;		
			String ivrservicecode=null;
			String ivrcurrntusridobj=null;
			int ivrCurrntusrid=0;
		    try {
		      logWrite = new Log();
		      locObjsession = HibernateUtil.getSession();
		      String locvrFnrst=null;
		      if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
		    	  ivrparams = EncDecrypt.decrypt(ivrparams);
		    	  boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
		    	  if (ivIsJson) {
		    		locObjRecvJson = new JSONObject(ivrparams);
		    		ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		    		if(ivrservicecode!=null && !ivrservicecode.equalsIgnoreCase("null") && !ivrservicecode.equalsIgnoreCase("")){
		    			locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		    			ivrcurrntusridobj =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
		        	    if (ivrcurrntusridobj!=null && Commonutility.toCheckisNumeric(ivrcurrntusridobj)) {
		    			ivrCurrntusrid= Integer.parseInt(ivrcurrntusridobj);
		    			}else { ivrCurrntusrid=0; }
		        	    
		        	    locvrFnrst=tosolvereportissue(locObjRecvdataJson,locObjsession);		
		    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
		    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
		    				AuditTrial.toWriteAudit(getText("report issue  solved "), "REPISAD002", ivrCurrntusrid);
		    				serverResponse("SI39001","1","E39001",getText("report issue not solved "),locObjRspdataJson);
		    			}else{
		    				AuditTrial.toWriteAudit(getText("report issue  solved "), "REPISAD001", ivrCurrntusrid);
		    				serverResponse("SI39001","0","S39001",getText("report issue  solved "),locObjRspdataJson);					
		    			}
		    		}else {
		    			locObjRspdataJson=new JSONObject();
		    	    	logWrite.logMessage("Service code : SI39001,"+getText("request.values.empty")+"", "info", Solvedreportandotptable.class);
		    			serverResponse("SI39001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		    	    }			    	   
		    	  }else {
		          locObjRspdataJson=new JSONObject();
		          logWrite.logMessage("Service code : SI39001,"+getText("request.format.notmach")+"", "info", Solvedreportandotptable.class);
				  serverResponse("SI39001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
		    	  }
		      }else {
		    	locObjRspdataJson=new JSONObject();
		    	logWrite.logMessage("Service code : SI39001,"+getText("request.values.empty")+"", "info", Solvedreportandotptable.class);
				serverResponse("SI39001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		      }      
		    } catch (Exception expObj) {      
		      locObjRspdataJson=new JSONObject();
		      logWrite.logMessage("Service code : SI39001, Sorry, an unhandled error occurred : "+expObj, "error", Solvedreportandotptable.class);
			  serverResponse("SI39001","2","ER0002",getText("catch.error"),locObjRspdataJson);
			} finally {
				if (locObjsession!=null) {locObjsession.flush();locObjsession.clear();locObjsession.close(); locObjsession = null;}
				logWrite = null;
				locObjRecvJson = null;//Receive over all Json	[String Received]
				locObjRecvdataJson = null;// Receive Data Json		
				locObjRspdataJson = null;// Response Data Json	
				ivrservicecode=null;
				ivrcurrntusridobj=null;
				ivrCurrntusrid=0;
			}	 
			return SUCCESS;
		  }
		
public static String tosolvereportissue(JSONObject pDataJson, Session pSession) {
	String locvrSLOVE_ID=null,locvrstatus=null,locvrmobno=null;
	String locrestUpdqry=null,locrestUpdqry1=null;		
	boolean locrestUpdSts=false;
	boolean locrestUpdSts1=false;
	CommonDaoService locrestObj=null,locOTPObj=null;
	Log log=null;

	try {
		log=new Log();
		locvrSLOVE_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "solveid");
		locvrstatus  = "2";
		locvrmobno  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "mobno");
		locrestUpdqry ="update ReportIssueTblVO set status='0' where repId ="+locvrSLOVE_ID+"";
		log.logMessage("Updqry : "+locrestUpdqry, "info", Solvedreportandotptable.class);
		locrestObj=new CommonDaoService();
		locrestUpdSts = locrestObj.commonUpdate(locrestUpdqry);
		locrestUpdqry1 ="update MvpUserOtpTbl set status='3' where mobileNo ="+locvrmobno+" and status ='2'";
		log.logMessage("Updqry : "+locrestUpdqry1, "info", Solvedreportandotptable.class);
		locOTPObj=new CommonDaoService();
		locrestUpdSts1 = locOTPObj.commonUpdate(locrestUpdqry1);
		 if(locrestUpdSts){
			 return "success";
		 }else{
			 return "error";
		 }	
	} catch (Exception e) {
		System.out.println("Exception found in Solvedreportandotptable.toDeActResident : "+e);
		log.logMessage("Exception : "+e, "error", Solvedreportandotptable.class);
		return "error";
	} finally {
		 log=null; locrestObj=null;locvrstatus=null;locvrmobno=null;
		 locvrSLOVE_ID=null; 	
		 locrestUpdqry=null;locrestUpdSts=false;				
	}
}
private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
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