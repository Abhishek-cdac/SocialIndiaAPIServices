package com.socialindiaservices.reports;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.contactusreportissuevo.ReportIssueTblVO;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.event.Eventutility;
import com.pack.event.Eventviewall;
import com.pack.eventvo.EventTblVO;
import com.pack.resident.ResidentUtility;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantProductItemsTblVO;
import com.socialindiaservices.vo.MerchantProductOrderTblVO;

public class Viewreportissuetable  extends ActionSupport {
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
	        	    
	        	    locObjRspdataJson=toGetreportissueselect(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				//AuditTrial.toWriteAudit(getText("EVEAD012"), "EVEAD012", ivrCurrntusrid);
	    				serverResponse("SI39000","0","E39000",getText("productorder.selectall.error"),locObjRspdataJson);
	    			}else{
	    			//	AuditTrial.toWriteAudit(getText("EVEAD011"), "EVEAD011", ivrCurrntusrid);
	    				serverResponse("SI39000","0","S39000",getText("order.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI39000,"+getText("request.values.empty")+"", "info", Eventviewall.class);
	    			serverResponse("SI39000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI39000,"+getText("request.format.notmach")+"", "info", Eventviewall.class);
			  serverResponse("SI39000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI39000,"+getText("request.values.empty")+"", "info", Eventviewall.class);
			serverResponse("SI39000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI39000, Sorry, an unhandled error occurred : "+expObj, "error", Eventviewall.class);
		  serverResponse("SI39000","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	
	
	public static JSONObject toGetreportissueselect(JSONObject pDataJson,Session praSession ){ 
	    String lvrorderid = null;
		String lvrSlqry = null;
		Query lvrQrybj = null;
		Log log = null;
		Date lvrEntrydate=null;
		ReportIssueTblVO locordertblobj = null;
		
		JSONObject locRtndatajson = null;
		Session locHbsession = null;
		Common locCommonObj = null;
		Iterator locObjorderlst_itr=null;
		JSONArray locLBRSklJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null;
		Date entrydatetime=null;	
		CommonUtils locCommutillObj =null;
		try {
			log = new Log();
			locHbsession = HibernateUtil.getSession();
			locCommonObj = new CommonDao();
			 DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				locCommutillObj = new CommonUtilsDao();
			log.logMessage("Step 1 : Select order detail block enter", "info", Viewreportissuetable.class);
			locRtndatajson =  new JSONObject();
			lvrorderid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "repid");
			System.out.println("Step 1 : order detail block enter   "+lvrorderid);
			lvrSlqry = "from ReportIssueTblVO where repId = "+Integer.parseInt(lvrorderid);
			lvrQrybj = locHbsession.createQuery(lvrSlqry);
			locordertblobj = (ReportIssueTblVO)lvrQrybj.uniqueResult();
			System.out.println("Step 2 : Select order detail query executed.");
			locRtndatajson.put("userid",Commonutility.toCheckNullEmpty(locordertblobj.getUserId()));
			locRtndatajson.put("uniqueid", Commonutility.toCheckNullEmpty(locordertblobj.getRepId()));
			locRtndatajson.put("usrname",Commonutility.toCheckNullEmpty( locordertblobj.getName()));
			locRtndatajson.put("mobno",Commonutility.toCheckNullEmpty( locordertblobj.getMobileNo()));
			locRtndatajson.put("emailid",Commonutility.toCheckNullEmpty( locordertblobj.getEmailId()));
			locRtndatajson.put("status",Commonutility.toCheckNullEmpty( locordertblobj.getStatus()));
			locRtndatajson.put("desc",Commonutility.toCheckNullEmpty( locordertblobj.getDescr()));
			locRtndatajson.put("reportto",Commonutility.toCheckNullEmpty( locordertblobj.getReportTo()));
			locRtndatajson.put("reporttotype",Commonutility.toCheckNullEmpty( locordertblobj.getReportToType()));
			entrydatetime = locordertblobj.getEntryDatetime();
				locRtndatajson.put("entrydate",Commonutility.toCheckNullEmpty(date.format(entrydatetime)));
		
			System.out.println("Step 3 : order details are put into json.");
			System.out.println("Step 4 : order Block end.");
			return locRtndatajson;
		}catch(Exception e) {
			try{
				System.out.println("Step -1 : Select order detail Exception found in orderutility.toseletordersingle : "+e);
				log.logMessage("Exception : "+e, "error", Viewreportissuetable.class);
				locRtndatajson=new JSONObject();
				locRtndatajson.put("catch", "Error");
				}catch(Exception ee){}
				return locRtndatajson;
		}finally {
				if(locHbsession!=null){locHbsession.close();locHbsession=null;}
				 lvrorderid = null; lvrSlqry = null; lvrQrybj = null;
				 log = null; lvrEntrydate=null; locordertblobj = null; locRtndatajson = null;
				 
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


