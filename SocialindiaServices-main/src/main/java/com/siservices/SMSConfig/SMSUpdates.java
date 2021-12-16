package com.siservices.SMSConfig;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.siservices.SMSConfigVO.SMSConfTbl;
import com.siservices.emailvo.persistance.EmailDao;
import com.siservices.emailvo.persistance.EmailDaoservice;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.sms.SmsEngineCntrl;
import com.social.utils.Log;

public class SMSUpdates extends ActionSupport{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	public String execute(){
		 Log logWrite = null;
			JSONObject locObjRecvJson = null;//Receive over all Json	[String Received]
			JSONObject locObjRecvdataJson = null;// Receive Data Json		
			JSONObject locObjRspdataJson = null;// Response Data Json
			Session locObjsession = null;		
			String ivrservicecode=null;
			String ivrcurrntusridobj=null;
			String ivrservicefor=null;
			int ivrCurrntusrid=0;
			String locvrFnrst=null;
			String locRtnSrvId=null,locRtnStsCd=null,locRtnRspCode=null,locRtnMsg=null;
		    try {
		      logWrite = new Log();
		      locObjsession = HibernateUtil.getSession();
		      if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
		    	  ivrparams = EncDecrypt.decrypt(ivrparams);
		    	  boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
		    	  if (ivIsJson) {
		    		locObjRecvJson = new JSONObject(ivrparams);
		    		ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		    		ivrservicefor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
		    		if(ivrservicecode!=null && !ivrservicecode.equalsIgnoreCase("null") && !ivrservicecode.equalsIgnoreCase("")){
		    			locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		    			ivrcurrntusridobj =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
		        	    if (ivrcurrntusridobj!=null && Commonutility.toCheckisNumeric(ivrcurrntusridobj)) {
		    			ivrCurrntusrid= Integer.parseInt(ivrcurrntusridobj);
		    			}else { ivrCurrntusrid=0; }
		        	    if(ivrservicefor!="" && !ivrservicefor.equalsIgnoreCase("") && ivrservicefor.equalsIgnoreCase("3")){
		        	    	locObjRspdataJson = toSMSselect(locObjRecvdataJson,locObjsession);		
		        	    
		        	    } else if(ivrservicefor!="" && !ivrservicefor.equalsIgnoreCase("") && ivrservicefor.equalsIgnoreCase("2")){
			        	   locvrFnrst=toSMSUpdate(locObjRecvdataJson);		
			        	    if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
								locRtnSrvId="SI23000";locRtnStsCd="0"; locRtnRspCode="S23000";locRtnMsg=getText("sms.update.success");
								AuditTrial.toWriteAudit(getText("EMAILSOO1"), "EMAILSOO1", ivrCurrntusrid);
							}else{							
								locRtnSrvId="SI23000";locRtnStsCd="1"; locRtnRspCode="E23000";locRtnMsg=getText("sms.update.error");
								AuditTrial.toWriteAudit(getText("EMAILSOO2"), "EMAILSOO2", ivrCurrntusrid);
							}
			        	} else if(ivrservicefor!="" && !ivrservicefor.equalsIgnoreCase("") && ivrservicefor.equalsIgnoreCase("4")){
			        				SmsEngineCntrl.smsThreadConf = 0; // for sms config reloaded.
			        				SmsEngineCntrl.getInstance().startThread();
			        	        	//Thread.sleep(5000);
					    } else if(ivrservicefor!="" && !ivrservicefor.equalsIgnoreCase("") && ivrservicefor.equalsIgnoreCase("5")){
					               SmsEngineCntrl.getInstance().stopThread();
						}
		    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
		    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
		    				AuditTrial.toWriteAudit(getText("SMSSOO3"), "SMSSOO3", ivrCurrntusrid);
		    				serverResponse("SI23000","0","E23000",getText("sms.selectall.error"),locObjRspdataJson);
		    			}else{
		    				AuditTrial.toWriteAudit(getText("SMSSOO4"), "SMSSOO4", ivrCurrntusrid);
		    				serverResponse("SI23000","0","S23000",getText("sms.selectall.success"),locObjRspdataJson);					
		    			}
		    		}else {
		    			locObjRspdataJson=new JSONObject();
		    	    	logWrite.logMessage("Service code : SI23000,"+getText("request.values.empty")+"", "info", SMSUpdates.class);
		    			serverResponse("SI23000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		    	    }			    	   
		    	  }else {
		          locObjRspdataJson=new JSONObject();
		          logWrite.logMessage("Service code : SI23000,"+getText("request.format.notmach")+"", "info", SMSUpdates.class);
				  serverResponse("SI23000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
		    	  }
		      }else{
		    	locObjRspdataJson=new JSONObject();
		    	logWrite.logMessage("Service code : SI23000,"+getText("request.values.empty")+"", "info", SMSUpdates.class);
				serverResponse("SI23000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		      }      
		    } catch (Exception expObj) {      
		      locObjRspdataJson=new JSONObject();
		      logWrite.logMessage("Service code : SI23000, Sorry, an unhandled error occurred : "+expObj, "error", SMSUpdates.class);
			  serverResponse("SI23000","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	private JSONObject toSMSselect(JSONObject praDatajson, Session praSession) throws JSONException{
		 String lvSlqry = null;
		 Query lvrObjeventlstitr = null;
		 SMSConfTbl lvrEmailtblidvoobj = null;
		 lvSlqry ="from SMSConfTbl where actflg = 1";
		 JSONObject lvrInrJSONObj=new JSONObject();
		 lvrObjeventlstitr =praSession.createQuery(lvSlqry);
		 System.out.println(lvrObjeventlstitr.list());
		 lvrEmailtblidvoobj = (SMSConfTbl)lvrObjeventlstitr.uniqueResult();	
		 lvrInrJSONObj.put("httpurl", lvrEmailtblidvoobj.getHttpurl());
		 if(lvrEmailtblidvoobj.getUsrName()!=null && !lvrEmailtblidvoobj.getUsrName().equalsIgnoreCase("") && !lvrEmailtblidvoobj.getUsrName().equalsIgnoreCase("null")){
			 lvrInrJSONObj.put("usrName", lvrEmailtblidvoobj.getUsrName());
		 }else{
			 lvrInrJSONObj.put("usrName", "");
		 }
		 if(lvrEmailtblidvoobj.getPasswd()!=null && !lvrEmailtblidvoobj.getPasswd().equalsIgnoreCase("") && !lvrEmailtblidvoobj.getPasswd().equalsIgnoreCase("null")){
			 lvrInrJSONObj.put("password", lvrEmailtblidvoobj.getPasswd());
		 }else{
			 lvrInrJSONObj.put("password", "");
		 }if(lvrEmailtblidvoobj.getCdmasender()!=null && !lvrEmailtblidvoobj.getCdmasender().equalsIgnoreCase("") && !lvrEmailtblidvoobj.getCdmasender().equalsIgnoreCase("null")){
			 lvrInrJSONObj.put("cdmaname", lvrEmailtblidvoobj.getCdmasender());
		 }else{
			 lvrInrJSONObj.put("cdmaname", "");
		 }
		 
		 lvrInrJSONObj.put("sender", lvrEmailtblidvoobj.getSender());
		 lvrInrJSONObj.put("proname", lvrEmailtblidvoobj.getProname());
		 lvrInrJSONObj.put("fetchrow", lvrEmailtblidvoobj.getFetchrow());	
		 lvrInrJSONObj.put("blkfetchrow", lvrEmailtblidvoobj.getBlklmtfechrow());
		 return lvrInrJSONObj;	 
	 }
	 private String toSMSUpdate(JSONObject praDatajson){
		 String lochttpurl=null;String lochttploginid=null;String lochttppass=null;
		 String loccdmaname=null;String locsenderName=null;String locproviderName=null;
		 int locfetchRow=0;int locbulkFetch=0;
		 String locUpdQry=null;
		 EmailDao locemailDaoObj=null;
		 boolean lvrEventUpdaSts=false;
		try{
			lochttpurl = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "httpurl");
			lochttploginid = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "httploginid");
			lochttppass = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "httppass");
			loccdmaname = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "cdmaname");
			locsenderName = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "senderName");
			locproviderName = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "providerName");
			locfetchRow = (Integer) Commonutility.toHasChkJsonRtnValObj(praDatajson, "fetchRow");
			locbulkFetch = (Integer) Commonutility.toHasChkJsonRtnValObj(praDatajson, "bulkFetch");
			locUpdQry = "update SMSConfTbl set usrName = '"+lochttploginid+"', passwd = '"+lochttppass+"', httpurl ='"+lochttpurl+"', cdmasender = '"+loccdmaname+"',sender ='"+locsenderName+"', proname = '"+locproviderName+"',fetchrow ='"+locfetchRow+"', blklmtfechrow = '"+locbulkFetch+"' where actflg = 1";
			locemailDaoObj= new EmailDaoservice();
			lvrEventUpdaSts=locemailDaoObj.toUpdateEmail(locUpdQry);
			System.out.println("---success ----"+lvrEventUpdaSts);
		}catch(Exception ex){
			System.out.println("Email update  services Exception-----"+ex);
		}finally{
			 lochttpurl=null;lochttploginid=null;lochttppass=null;
			 loccdmaname=null;locsenderName=null;locproviderName=null;
			 locfetchRow=0;locbulkFetch=0;
			 locUpdQry=null;
			 locemailDaoObj=null;			 
		}
		 if(lvrEventUpdaSts){
			 SmsEngineCntrl.smsThreadConf = 0; // for sms config reloaded.
			 return "success";
		 }else{
			 return "error";
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
