package com.siservices.email;

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
import com.siservices.emailvo.EmailConfTbl;
import com.siservices.emailvo.persistance.EmailDao;
import com.siservices.emailvo.persistance.EmailDaoservice;
import com.sisocial.load.HibernateUtil;
import com.social.email.EmailEngineCntrl;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Emailupdates extends ActionSupport {
	  private static final long serialVersionUID = 1L;
	  private String ivrparams;
	/*  EmailEngineMain emai_thrdobj = new EmailEngineCntrl().new EmailEngineMain();*/
	  
	 public String execute() {
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
		        	    locObjRspdataJson=toEmailselect(locObjRecvdataJson,locObjsession);		
		        	    }
		        	    else if(ivrservicefor!="" && !ivrservicefor.equalsIgnoreCase("") && ivrservicefor.equalsIgnoreCase("2")){
			        	   locvrFnrst=toEmailupdate(locObjRecvdataJson);		
			        	    if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){							
								locRtnSrvId="SI22000";locRtnStsCd="0"; locRtnRspCode="S22000";locRtnMsg=getText("email.update.success");
								AuditTrial.toWriteAudit(getText("EMAILSOO1"), "EMAILSOO1", ivrCurrntusrid);
							}else{							
								locRtnSrvId="SI22000";locRtnStsCd="1"; locRtnRspCode="E22000";locRtnMsg=getText("email.update.error");
								AuditTrial.toWriteAudit(getText("EMAILSOO2"), "EMAILSOO2", ivrCurrntusrid);
							}
			        	    } else if(ivrservicefor!="" && !ivrservicefor.equalsIgnoreCase("") && ivrservicefor.equalsIgnoreCase("4")){
			        	    	EmailEngineCntrl.getInstance().startThread();
			        	        Thread.sleep(5000);
					        	}else if(ivrservicefor!="" && !ivrservicefor.equalsIgnoreCase("") && ivrservicefor.equalsIgnoreCase("5")){
					        		 EmailEngineCntrl.getInstance().stopThread();
							     }
		    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
		    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
		    				AuditTrial.toWriteAudit(getText("EMAILSOO3"), "EMAILSOO3", ivrCurrntusrid);
		    				serverResponse("SI22000","0","E22000",getText("email.selectall.error"),locObjRspdataJson);
		    			}else{
		    				AuditTrial.toWriteAudit(getText("EMAILSOO4"), "EMAILSOO4", ivrCurrntusrid);
		    				serverResponse("SI22000","0","S22000",getText("email.selectall.success"),locObjRspdataJson);					
		    			}
		    		}else {
		    			locObjRspdataJson=new JSONObject();
		    	    	logWrite.logMessage("Service code : SI22000,"+getText("request.values.empty")+"", "info", Emailupdates.class);
		    			serverResponse("SI22000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		    	    }			    	   
		    	  }else {
		          locObjRspdataJson=new JSONObject();
		          logWrite.logMessage("Service code : SI22000,"+getText("request.format.notmach")+"", "info", Emailupdates.class);
				  serverResponse("SI22000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
		    	  }
		      }else{
		    	locObjRspdataJson=new JSONObject();
		    	logWrite.logMessage("Service code : SI22000,"+getText("request.values.empty")+"", "info", Emailupdates.class);
				serverResponse("SI22000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		      }      
		    } catch (Exception expObj) {      
		      locObjRspdataJson=new JSONObject();
		      logWrite.logMessage("Service code : SI22000, Sorry, an unhandled error occurred : "+expObj, "error", Emailupdates.class);
			  serverResponse("SI22000","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	 private JSONObject toEmailselect(JSONObject praDatajson, Session praSession) throws JSONException{
		 String lvSlqry = null;
		 Query lvrObjeventlstitr = null;
		 EmailConfTbl lvrEmailtblidvoobj = null;
		 lvSlqry ="from EmailConfTbl  where actStat=1";
		 JSONObject lvrInrJSONObj=new JSONObject();
		 lvrObjeventlstitr =praSession.createQuery(lvSlqry);
		 lvrEmailtblidvoobj = (EmailConfTbl)lvrObjeventlstitr.uniqueResult();			 
			 lvrInrJSONObj.put("emailid", lvrEmailtblidvoobj.getFrmId());
			 lvrInrJSONObj.put("password", lvrEmailtblidvoobj.getPasswd());
			 lvrInrJSONObj.put("hostname", lvrEmailtblidvoobj.getHostName());
			 lvrInrJSONObj.put("portno", lvrEmailtblidvoobj.getPortNo());
			 lvrInrJSONObj.put("protocol", lvrEmailtblidvoobj.getProtcl());
			 lvrInrJSONObj.put("nofetch", String.valueOf(lvrEmailtblidvoobj.getFetchConf()));			
			 
		 return lvrInrJSONObj;	 
	 }private String toEmailupdate(JSONObject praDatajson){
		 String locemail=null;String locpassword=null;String lochostname=null;
		 String locportno=null;String locprotocol=null;String locnooffetch=null;
		 String locUpdQry=null;
		 EmailDao locemailDaoObj=null;
		 boolean lvrEventUpdaSts=false;
		try{
			locemail=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "email");
			locpassword=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "passwd");
			lochostname=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "host");
			locportno=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "port");
			locprotocol=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "proto");
			locnooffetch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "nofet");
			locUpdQry ="update EmailConfTbl set frmId = '"+locemail+"', passwd = '"+locpassword+"', hostName ='"+lochostname+"', portNo = '"+locportno+"',protcl ='"+locprotocol+"', fetchConf = '"+Integer.parseInt(locnooffetch)+"' where actStat =1 ";
			locemailDaoObj= new EmailDaoservice();
			lvrEventUpdaSts=locemailDaoObj.toUpdateEmail(locUpdQry);
		}catch(Exception ex){
			System.out.println("Email update  services Exception-----"+ex);
		}
		 if(lvrEventUpdaSts){
			 EmailEngineCntrl.threadConf = 1; // Email Configuration Reloaded purpose
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
