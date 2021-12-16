package com.socialindiaservices.reports;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.MaintenanceFileUploadTblVO;

public class MaintenancebillReport extends ActionSupport{
	/**
	 * 
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
	        	    
	        	    locObjRspdataJson=tomaintenancebill(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	    				serverResponse("SI310001","0","E8006",getText("MaintenanceBill.selectall.error"),locObjRspdataJson);
	    			}else{
	    				//AuditTrial.toWriteAudit(getText("FUNC011"), "FUNC011", ivrCurrntusrid);
	    				serverResponse("SI310001","0","S310001",getText("MaintenanceBill.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI310001,"+getText("request.values.empty")+"", "info", MaintenancebillReport.class);
	    			serverResponse("SI310001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	          logWrite.logMessage("Service code : SI310001,"+getText("request.format.notmach")+"", "info", MaintenancebillReport.class);
			  serverResponse("SI310001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	    	logWrite.logMessage("Service code : SI310001,"+getText("request.values.empty")+"", "info", MaintenancebillReport.class);
			serverResponse("SI310001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	      logWrite.logMessage("Service code : SI310001, Sorry, an unhandled error occurred : "+expObj, "error", MaintenancebillReport.class);
		  serverResponse("SI310001","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	private JSONObject tomaintenancebill(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Common locCommonObj = null;
		Iterator lvrObjeventlstitr = null;
		List<Object> lvrObjfunctionlstitr = null;
		MaintenanceFileUploadTblVO lvrEvnttblvoobj = null;
		String lvrevntcountqry = null,locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,societyid=null,fromdate="",todate="";	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String glbSearch="";
		Common lvrEvntDobj=null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	    try {
	    	System.out.println("Step 1 : Select MaintenanceBill process start.");
	    	lvrEvntDobj = new CommonDao();
	    	locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
			fromdate=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "fromdate");
			todate=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "todate");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
						
			}else {
				/*if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {
					lvrevntcountqry="select count(functionId) from FunctionMasterTblVO";
					count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
					countFilter = count;
				}else{*/
					/*lvrevntcountqry="select count(functionId) from FunctionMasterTblVO";
					count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
					countFilter = count;*/
				glbSearch="";
					System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				}
		/*	}*/
			if(fromdate.length() >0 && todate.length() >0){
				glbSearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
			}else if(fromdate.length() >0){
			glbSearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
			}else if(todate.length() >0){
			glbSearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			if(locTochenull!=null && !locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("null")) {
				lvSlqry = "from MaintenanceFileUploadTblVO where statusFlag =1 and "+glbSearch;
				lvrevntcountqry="select count(fileUpId) from MaintenanceFileUploadTblVO where statusFlag =1 and "+glbSearch;
				/*count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
				countFilter = count;*/
			}else{
				if(societyid==null || societyid.equalsIgnoreCase("null") ||societyid.equalsIgnoreCase("-1")){
					lvSlqry = "from MaintenanceFileUploadTblVO where status =1";
					lvrevntcountqry="select count(fileUpId) from MaintenanceFileUploadTblVO where status =1";
				}else{
				lvSlqry = "from MaintenanceFileUploadTblVO where societyId="+societyid+" and status =1 "+glbSearch;
				lvrevntcountqry="select count(fileUpId) from MaintenanceFileUploadTblVO where societyId="+societyid+" and status =1 "+glbSearch;
				/*count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
				countFilter = count;	*/
				}
				
			}
			count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
			countFilter = count;
			System.out.println("Step 3 : MaintenanceBill Details Query : "+lvSlqry);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext()) {
				lvrInrJSONObj=new JSONObject();
				lvrEvnttblvoobj = (MaintenanceFileUploadTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("maintenanceId", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getFileUpId()));
				lvrInrJSONObj.put("filename", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getFileName()));
				lvrInrJSONObj.put("foldername", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getNoofRecords()));
				lvrInrJSONObj.put("totalamt", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getGrandTotal()));
				lvrInrJSONObj.put("status", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getStatus()));
				lvrInrJSONObj.put("enterdatetime", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getEntryDatetime()));
				lvrInrJSONObj.put("docname", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getNoofRecords()));
				if(lvrEvnttblvoobj.getEntryBy().getSocietyId() != null) {
					lvrInrJSONObj.put("socityNme", lvrEvnttblvoobj.getEntryBy().getSocietyId().getSocietyName());
				} else {
					lvrInrJSONObj.put("socityNme", "");
				}				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
				String datefolder = dateFormat.format(lvrEvnttblvoobj.getEntryDatetime());
				//String downloadImagePath = rb.getString("external.fldr") + "backup/"+lvrEvnttblvoobj.getEntryBy().getUserId()+"/"+datefolder+"/"+lvrEvnttblvoobj.getFileName();
				
				String downloadImagePath = rb.getString("external.fldr") + rb.getString("external.maintenancefld")+lvrEvnttblvoobj.getEntryBy().getUserId()+"/"+lvrEvnttblvoobj.getFileName();
				lvrInrJSONObj.put("downloadPath", downloadImagePath);	
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("maintenancebill", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select  process End " +lvrRtnjsonobj);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in tomaintenancebill() : "+expObj);
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("InitCount", count);
				lvrRtnjsonobj.put("countAfterFilter", countFilter);
				lvrRtnjsonobj.put("labordetails", "");
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
	     return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	lvrEventjsonaryobj = null;
	    	locCommonObj = null;
	    	lvrObjeventlstitr = null;
	    	lvrEvnttblvoobj = null;
	    	lvrevntcountqry = null;locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
	    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
	    	lvSlqry = null;societyid=null;
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