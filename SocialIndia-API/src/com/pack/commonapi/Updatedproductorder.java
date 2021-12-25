package com.pack.commonapi;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.event.Eventviewall;
import com.pack.resident.persistance.ResidentDao;
import com.pack.resident.persistance.ResidentDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantProductItemsTblVO;

public class Updatedproductorder  extends ActionSupport {
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
	        	    
	        	    locvrFnrst=toUpdateproductorder(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("MPRDORD002"), "MPRDORD002", ivrCurrntusrid);
	    				serverResponse("SI39001","1","E39001",getText("productorder not Accept "),locObjRspdataJson);
	    			}else{
	    				AuditTrial.toWriteAudit(getText("MPRDORD001"), "MPRDORD001", ivrCurrntusrid);
	    				serverResponse("SI39001","0","S39001",getText("productorder Accept "),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI39001,"+getText("request.values.empty")+"", "info", Eventviewall.class);
	    			serverResponse("SI39001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI39001,"+getText("request.format.notmach")+"", "info", Eventviewall.class);
			  serverResponse("SI39001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI39001,"+getText("request.values.empty")+"", "info", Eventviewall.class);
			serverResponse("SI39001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI39001, Sorry, an unhandled error occurred : "+expObj, "error", Eventviewall.class);
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
	
	
	public static String toUpdateproductorder(JSONObject pDataJson, Session pSession) {// Update
		//JSONObject locRtnJson=null;
		Log log=null;
		String locvrSOCIETYID=null, locvrORDER_SUPPLYQTY = null,locvrORDER_EMAIL=null,locvrORDER_ADD_1 = null, locvrORDER_ADD_2 = null, locvrORDER_CITY_ID = null;
		String locvORDER_STATE_ID = null,locvrORDER_PSTL_ID=null,locvrORDER_COUNTRY_ID = null, locvORDER_STS = null, locvrID_TYP = null;
		String locvrID_NO = null, locvrENTRY_BY = null;
		String locvrORDER_SUPPLYCMTS=null,locvrMOB_NO=null, locvrFNAME=null, locvrLNAME=null, locvrORDER_DESCP=null;
		String locvrORDER_ID=null,locvrGENDER=null,locvrDOB=null;
		String locvrACC_CH=null, locvrNO_BLOCKS=null,locvrNO_FLATS=null,locvrOCCUPUATION=null,locvrF_MEMBER=null,locvrBLDGRP=null,locvrfamName=null,locvrfammobno=null,locvrfemail=null,locvrfmbrISD=null,locvrfmbrMtype=null,locvracceptstatus=null;
		String locLbrUpdqry="";
		boolean locLbrUpdSts=false;
		 ResidentDao locLabrObj=null;
			CommonUtils locCommutillObj =null;
			
		try {
			locCommutillObj = new CommonUtilsDao();
			log=new Log();
			 locvrORDER_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "orderid");
			  locvrORDER_SUPPLYQTY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "supplyqty");
			 locvrORDER_SUPPLYCMTS=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "supplycomments");
			 locvracceptstatus=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "acceptstatus");
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryby");
						 					
			 locLbrUpdqry ="update MerchantProductOrderTblVO set  supplyComments ='"+locvrORDER_SUPPLYCMTS+"',orderAcceptStatus='"+locvracceptstatus+"'";			 			  			 
			 locLbrUpdqry+="  where orderId ="+locvrORDER_ID+"";			 							
			 log.logMessage("Updqry : "+locLbrUpdqry, "info", Updatedproductorder.class);
			 locLabrObj=new ResidentDaoservice();
			 locLbrUpdSts = locLabrObj.updateProductOrdertbl(locLbrUpdqry);				 			
			 JSONArray accptstsjry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "acceptstatusJary");// mvp_mrch_product_items_tbl insert into 
			 JSONArray suplyqtyjry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "supplyqualityJary");
			 JSONArray cmtidjry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "commentidJary");
			 MerchantProductItemsTblVO inrlocflatdbl=null;
			 log.logMessage("Resident  Flat Detail insert will start.", "info", Updatedproductorder.class);
			 
			// boolean dlrst=locLabrObj.deleteFlatdblInfo(Integer.parseInt(locvrORDER_ID));
			 boolean lbrflg=false;
			 if(locLbrUpdSts){
				 for (int i = 0; i < accptstsjry.length(); i++) {
					 String statusflg=(String)accptstsjry.getString(i);
					 String supplyqtycount=(String)suplyqtyjry.getString(i);
					 String commentid=(String)cmtidjry.getString(i);
					 locLbrUpdqry ="update MerchantProductItemsTblVO set  supplyQty ='"+supplyqtycount+"',status='"+statusflg+"'";
					 locLbrUpdqry+="  where commentId ="+commentid+"";			 	
					 log.logMessage("Updqry : "+locLbrUpdqry, "info", Updatedproductorder.class);
					 locLabrObj=new ResidentDaoservice();
					 locLbrUpdSts = locLabrObj.updateProductOrdertbl(locLbrUpdqry);	

				 } 
				 
			 }else{
				 
			 }			 			 		
			 if(locLbrUpdSts){
				 return "success";
			 }else{
				 return "error";
			 }	 	
		} catch (Exception e) {
			System.out.println("Exception found in Updatedproductorder.toUpdtresident : "+e);
			log.logMessage("Exception : "+e, "error", Updatedproductorder.class);
			return "error";						
		} finally {
			 log=null; locLabrObj=null;
			 locvrSOCIETYID=null; locvrORDER_SUPPLYQTY = null; locvrORDER_EMAIL = null;
			 locvrORDER_ADD_1 = null; locvrORDER_ADD_2 = null; locvrORDER_CITY_ID = null; locvORDER_STATE_ID = null;
			 locvrORDER_COUNTRY_ID = null; locvORDER_STS = null; locvrID_TYP = null; locvrACC_CH = null; locvrENTRY_BY = null;locvrfammobno=null;locvrfamName=null;locvrfmbrISD=null;
			 locvrfmbrMtype=null;locvracceptstatus=null;
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


