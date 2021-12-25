package com.socialindiaservices.function;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

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
import com.socialindiaservices.vo.FunctionMasterTblVO;

public class Functionviewall extends ActionSupport{
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
	        	    
	        	    locObjRspdataJson=toFunctionselect(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	    				serverResponse("SI310001","0","E8006",getText("function.selectall.error"),locObjRspdataJson);
	    			}else{
	    				//AuditTrial.toWriteAudit(getText("FUNC011"), "FUNC011", ivrCurrntusrid);
	    				serverResponse("SI310001","0","S310001",getText("function.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI310001,"+getText("request.values.empty")+"", "info", Functionviewall.class);
	    			serverResponse("SI310001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	          logWrite.logMessage("Service code : SI310001,"+getText("request.format.notmach")+"", "info", Functionviewall.class);
			  serverResponse("SI310001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	    	logWrite.logMessage("Service code : SI310001,"+getText("request.values.empty")+"", "info", Functionviewall.class);
			serverResponse("SI310001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      AuditTrial.toWriteAudit(getText("FUNC012"), "FUNC012", ivrCurrntusrid);
	      logWrite.logMessage("Service code : SI310001, Sorry, an unhandled error occurred : "+expObj, "error", Functionviewall.class);
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
	private JSONObject toFunctionselect(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Common locCommonObj = null;
		Iterator lvrObjeventlstitr = null;
		List<Object> lvrObjfunctionlstitr = null;
		FunctionMasterTblVO lvrEvnttblvoobj = null;
		String lvrevntcountqry = null,locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,societyid=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String lvSlfunqry=null;
		String glbSearch="";
	    try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	Commonutility.toWriteConsole("Step 1 : Select Function process start.");
	    	logWrite.logMessage("Step 1 : Select Function process start.", "info", Functionviewall.class);
	    	
	    	locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			Common lvrEvntDobj = new CommonDao();
			String locOrderby =" order by entryDatetime desc";
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
				/*if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {					
					lvrevntcountqry="select count(functionId) from FunctionMasterTblVO";
					count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
					countFilter = count;
				}else{*/
				glbSearch = " (" + " functionName like ('%" + locTochenull + "%')" 
	             + ") "  ; 	
					
				/*}*/			
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
					Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
					logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Functionviewall.class);	
				}
		/*	}*/
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			if(locTochenull!=null && !locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("null")) {
				lvSlqry = "from FunctionMasterTblVO where"+glbSearch +" "+locOrderby;
				lvrevntcountqry="select count(functionId) from FunctionMasterTblVO where "+glbSearch;
				count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
				countFilter = count;
			}else{
				lvSlqry = "from FunctionMasterTblVO "+locOrderby;
				lvrevntcountqry="select count(functionId) from FunctionMasterTblVO";
				count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
				countFilter = count;
				
			}
			Commonutility.toWriteConsole("Step 3 : Function Details Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Function Details Query : "+lvSlqry, "info", Functionviewall.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				lvrEvnttblvoobj = (FunctionMasterTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("functionid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getFunctionId()));
				lvrInrJSONObj.put("functionname", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getFunctionName()));
				lvrInrJSONObj.put("status", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getStatusFlag()));
				lvrInrJSONObj.put("functiontype",  Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnobjEVENT_TYPE()));
				lvSlfunqry = "select templateText from FunctionTemplateTblVO where functionId="+lvrEvnttblvoobj.getFunctionId();
				lvrObjfunctionlstitr=praSession.createQuery(lvSlfunqry).list();
				lvrInrJSONObj.put("eventemp", lvrObjfunctionlstitr);	
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("functiondetails", lvrEventjsonaryobj);
			Commonutility.toWriteConsole("Step 4 : Select Function process End " +lvrRtnjsonobj);
			logWrite.logMessage("Step 4 : Select Function process End", "info", Functionviewall.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
	    		Commonutility.toWriteConsole("Step -1 : Exception found in "+ Thread.currentThread().getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + expObj);
				logWrite.logMessage("Step -1 : Function select all block Exception : "+expObj, "error", Functionviewall.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("InitCount", count);
				lvrRtnjsonobj.put("countAfterFilter", countFilter);
				lvrRtnjsonobj.put("functiondetails", "");
				lvrRtnjsonobj.put("CatchBlock", "Error");
				Commonutility.toWriteConsole("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
	     return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	lvrEventjsonaryobj = null;
	    	logWrite = null;
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
