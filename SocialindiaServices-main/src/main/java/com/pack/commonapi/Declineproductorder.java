package com.pack.commonapi;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.event.Eventviewall;
import com.pack.resident.ResidentUtility;
import com.pack.resident.persistance.ResidentDao;
import com.pack.resident.persistance.ResidentDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantProductItemsTblVO;
import com.socialindiaservices.vo.MerchantProductOrderTblVO;

public class Declineproductorder  extends ActionSupport {
	  /**
	   *sdsd.
	   */
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
	        	    
	        	    locvrFnrst=toDeclineproductorder(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("MPRDORD002"), "MPRDORD002", ivrCurrntusrid);
	    				serverResponse("SI39002","1","E39002",getText("productorder not Accept "),locObjRspdataJson);
	    			}else{
	    				AuditTrial.toWriteAudit(getText("MPRDORD001"), "MPRDORD001", ivrCurrntusrid);
	    				serverResponse("SI39002","0","S39002",getText("productorder Accept "),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI39002,"+getText("request.values.empty")+"", "info", Eventviewall.class);
	    			serverResponse("SI39002","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI39002,"+getText("request.format.notmach")+"", "info", Eventviewall.class);
			  serverResponse("SI39002","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI39002,"+getText("request.values.empty")+"", "info", Eventviewall.class);
			serverResponse("SI39002","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI39002, Sorry, an unhandled error occurred : "+expObj, "error", Eventviewall.class);
		  serverResponse("SI39002","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	
	
	public static String toDeclineproductorder(JSONObject pDataJson, Session pSession) {// Update
		//JSONObject locRtnJson=null;
		Log log=null;
		String locvrORDER_ID=null;
		String locvracceptstatus=null;
		String locLbrUpdqry="";
		boolean locLbrUpdSts=false;
		 ResidentDao locLabrObj=null;
			CommonUtils locCommutillObj =null;
			
		try {
			locCommutillObj = new CommonUtilsDao();
			log=new Log();
			 locvrORDER_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "orderid");
			 locvracceptstatus=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "acceptstatus");
			
						 					
			 locLbrUpdqry ="update MerchantProductOrderTblVO set  orderAcceptStatus='"+locvracceptstatus+"'";
			 
			  			 
			 locLbrUpdqry+="  where orderId ="+locvrORDER_ID+"";			 	
			
			 
			 log.logMessage("Updqry : "+locLbrUpdqry, "info", Declineproductorder.class);
			 locLabrObj=new ResidentDaoservice();
			 locLbrUpdSts = locLabrObj.updateProductOrdertbl(locLbrUpdqry);				 			
			
			// boolean dlrst=locLabrObj.deleteFlatdblInfo(Integer.parseInt(locvrORDER_ID));
					 			 		
			 if(locLbrUpdSts){
				 return "success";
			 }else{
				 return "error";
			 }	 	
		} catch (Exception e) {
			System.out.println("Exception found in Declineproductorder.toUpdtresident : "+e);
			log.logMessage("Exception : "+e, "error", Declineproductorder.class);
			return "error";						
		} finally {
			 log=null; locLabrObj=null;
			locvracceptstatus=null;
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


