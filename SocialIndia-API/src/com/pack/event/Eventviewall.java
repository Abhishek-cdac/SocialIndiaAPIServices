package com.pack.event;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.eventvo.EventTblVO;
import com.pack.eventvo.persistence.EventDao;
import com.pack.eventvo.persistence.EventDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Eventviewall extends ActionSupport {
  /**
   *sdsd.
   */
  private static final long serialVersionUID = 1L;
  private String ivrparams;
  /**
   * Executed Method .
   */
  
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
        	    
        	    locObjRspdataJson=toEventselect(locObjRecvdataJson,locObjsession);		
    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
    				AuditTrial.toWriteAudit(getText("EVEAD012"), "EVEAD012", ivrCurrntusrid);
    				serverResponse("SI8006","01","R0139",mobiCommon.getMsg("R0139"),locObjRspdataJson);
    			}else{
    				AuditTrial.toWriteAudit(getText("EVEAD011"), "EVEAD011", ivrCurrntusrid);
    				serverResponse("SI8006","00","R0138",mobiCommon.getMsg("R0138"),locObjRspdataJson);					
    			}
    		}else {
    			locObjRspdataJson=new JSONObject();
    	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", Eventviewall.class);
    			serverResponse("SI8006","01","R0004",mobiCommon.getMsg("R0004"),locObjRspdataJson);
    	    }			    	   
    	  }else {
          locObjRspdataJson=new JSONObject();
          logWrite.logMessage("Service code : SI8006,"+mobiCommon.getMsg("R0067")+"", "info", Eventviewall.class);
		  serverResponse("SI8006","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);  
    	  }
      }else {
    	locObjRspdataJson=new JSONObject();
    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", Eventviewall.class);
		serverResponse("SI8006","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
      }      
    } catch (Exception expObj) {      
      locObjRspdataJson=new JSONObject();
      logWrite.logMessage("Service code : SI8006, Sorry, an unhandled error occurred : "+expObj, "error", Eventviewall.class);
	  serverResponse("SI8006","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
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

  
  
  
  /*
   * To select Events.
   */
  private JSONObject toEventselect(JSONObject praDatajson, Session praSession){	
	JSONObject lvrRtnjsonobj = null;
	JSONObject lvrInrJSONObj = null;	
	JSONArray lvrEventjsonaryobj = null;
	Log logWrite = null;
	Common locCommonObj = null;
	Date lvrEntrydate = null;
	Iterator lvrObjeventlstitr = null;
	EventTblVO lvrEvnttblvoobj = null;
	String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null,societyid=null;	
	int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
	String lvSlqry = null;
	String glbSearch="";
    try {
    	logWrite = new Log();
    	locCommonObj=new CommonDao();
    	System.out.println("Step 1 : Select Event process start.");
    	logWrite.logMessage("Step 1 : Select Event process start.", "info", Eventviewall.class);
    	
    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eventstatus");
		locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
		locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
		locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
		locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
		locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
		srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
		srchField=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchField");
		srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
		societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
		String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
		EventDao lvrEvntDobj = new EventDaoservice();
		
		if (Commonutility.toCheckisNumeric(locStrRow)) {
			 startrowfrom=Integer.parseInt(locStrRow);
		}
		if (Commonutility.toCheckisNumeric(locMaxrow)) {
			totalNorow=Integer.parseInt(locMaxrow);
		}
		String locOrderby =" order by ivrBnENTRY_DATETIME desc";
		if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {
			if(srchField!=null && !srchField.equalsIgnoreCase("") && !srchField.equalsIgnoreCase("null")){
				if(srchField.equalsIgnoreCase("1")){
					glbSearch = " and (" + " ivrBnEVENT_TITLE like ('%" + srchFieldval + "%') or " 
			             + " ivrBnSHORT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnEVENT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTTIME like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDTIME like ('%" + srchFieldval + "%') "
			             + ")"; 
						lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}else if(srchField.equalsIgnoreCase("2")){
					glbSearch = " and (" + " ivrBnEVENT_TITLE like ('%" + srchFieldval + "%')) " ;
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;	
				}else if(srchField.equalsIgnoreCase("3")){
					glbSearch = " and (" + " ivrBnSTARTDATE like ('%" + srchFieldval + "%'))" ;
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}else if(srchField.equalsIgnoreCase("4")){
					glbSearch = " and (" + " ivrBnENDDATE like ('%" + srchFieldval + "%'))" ;
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}else {
					glbSearch = " and (" + " ivrBnEVENT_TITLE like ('%" + srchFieldval + "%') or " 
			             + " ivrBnSHORT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnEVENT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTTIME like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDTIME like ('%" + srchFieldval + "%') "
			             + ")"; 
						lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;	
				}
			} else {
				if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
					glbSearch = " and (" + " ivrBnEVENT_TITLE like ('%" + locTochenull + "%') or " 
				             + " ivrBnSHORT_DESC like ('%" + locTochenull + "%') or "
				             + " ivrBnEVENT_DESC like ('%" + locTochenull + "%') or "
				             + " ivrBnSTARTDATE like ('%" + locTochenull + "%') or "
				             + " ivrBnENDDATE like ('%" + locTochenull + "%') or "
				             + " ivrBnSTARTTIME like ('%" + locTochenull + "%') or "
				             + " ivrBnENDTIME like ('%" + locTochenull + "%') "
				             + ")";	
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}else{
					
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS +" "+locOrderby;
				}
				
			}
		} else {
			if(srchField!=null && !srchField.equalsIgnoreCase("") && !srchField.equalsIgnoreCase("null")){
				if (srchField.equalsIgnoreCase("1")) {
					glbSearch = " and (" + " ivrBnEVENT_TITLE like ('%" + srchFieldval + "%') or " 
			             + " ivrBnSHORT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnEVENT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTTIME like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDTIME like ('%" + srchFieldval + "%') "
			             + ") and  ivrBnEVENT_CRT_USR_ID.societyId.societyId=" + Integer.parseInt(societyid) ; 
						lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}	else {
					glbSearch = " and (" + " ivrBnEVENT_TITLE like ('%" + srchFieldval + "%') or " 
			             + " ivrBnSHORT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnEVENT_DESC like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDDATE like ('%" + srchFieldval + "%') or "
			             + " ivrBnSTARTTIME like ('%" + srchFieldval + "%') or "
			             + " ivrBnENDTIME like ('%" + srchFieldval + "%') "
			             + ") and  ivrBnEVENT_CRT_USR_ID.societyId.societyId=" + Integer.parseInt(societyid) ; 
						lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;	
				}
			} else {
				if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
					glbSearch = " and (" + " ivrBnEVENT_TITLE like ('%" + locTochenull + "%') or " 
				             + " ivrBnSHORT_DESC like ('%" + locTochenull + "%') or "
				             + " ivrBnEVENT_DESC like ('%" + locTochenull + "%') or "
				             + " ivrBnSTARTDATE like ('%" + locTochenull + "%') or "
				             + " ivrBnENDDATE like ('%" + locTochenull + "%') or "
				             + " ivrBnSTARTTIME like ('%" + locTochenull + "%') or "
				             + " ivrBnENDTIME like ('%" + locTochenull + "%') "			           
				             + ") and  ivrBnEVENT_CRT_USR_ID.societyId.societyId=" + Integer.parseInt(societyid) ; 	
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}else{
					glbSearch = " and ivrBnEVENT_CRT_USR_ID.societyId.societyId=" + Integer.parseInt(societyid) ; 
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}			
			}
		}
		lvrevntcountqry = "select count(*) "+lvSlqry;
		count = lvrEvntDobj.getInitTotal(lvrevntcountqry);
		countFilter = count;
		System.out.println("Step 3 : Event Details Query : "+lvSlqry);
		logWrite.logMessage("Step 3 : Event Details Query : "+lvSlqry, "info", Eventviewall.class);
		lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
		lvrEventjsonaryobj = new JSONArray();
		while ( lvrObjeventlstitr.hasNext() ) {
			lvrInrJSONObj=new JSONObject();
			lvrEvnttblvoobj = (EventTblVO) lvrObjeventlstitr.next();
			if(lvrEvnttblvoobj.getIvrBnFUNCTION_ID()!=null){
				lvrInrJSONObj.put("eventfuntxt", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnFUNCTION_ID().getFunctionName()));
				lvrInrJSONObj.put("eventfunid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnFUNCTION_ID().getFunctionId()));
			} else{
				lvrInrJSONObj.put("eventfuntxt", "");
				lvrInrJSONObj.put("eventfunid", "");
			}
			
			lvrInrJSONObj.put("evetitle", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnEVENT_TITLE()));
			lvrInrJSONObj.put("eveshdesc", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnSHORT_DESC()));
			lvrInrJSONObj.put("eventid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnEVENT_ID()));
			if(lvrEvnttblvoobj.getIvrBnEVENT_CRT_USR_ID()!=null){
				lvrInrJSONObj.put("evecurntusrid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnEVENT_CRT_USR_ID().getUserId()));
			} else {
				lvrInrJSONObj.put("evecurntusrid", "");
			}
			
			lvrInrJSONObj.put("evecurntgrpid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnEVENT_CRT_GROUP_ID()));
			lvrInrJSONObj.put("evefilename", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnEVENT_FILE_NAME()));
			lvrInrJSONObj.put("evevideopath", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnVIDEO_PATH()));
			lvrInrJSONObj.put("evestartdate", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnSTARTDATE()));
			lvrInrJSONObj.put("eveenddate", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnENDDATE()));
			lvrInrJSONObj.put("evestarttime", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnSTARTTIME()));
			lvrInrJSONObj.put("eveendtime", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnENDTIME()));
			lvrInrJSONObj.put("eveentryby", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnENTRY_BY()));
			lvrEntrydate=lvrEvnttblvoobj.getIvrBnENTRY_DATETIME();
			lvrInrJSONObj.put("eveentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
			
			lvrEventjsonaryobj.put(lvrInrJSONObj);
			lvrInrJSONObj = null;
		}
		lvrRtnjsonobj=new JSONObject();	
		lvrRtnjsonobj.put("InitCount", count);
		lvrRtnjsonobj.put("countAfterFilter", countFilter);
		lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
		lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
		lvrRtnjsonobj.put("eventdetails", lvrEventjsonaryobj);
		System.out.println("Step 4 : Select Event process End");
		logWrite.logMessage("Step 4 : Select Event process End", "info", Eventviewall.class);
    return lvrRtnjsonobj;
    }catch(Exception expObj) {
    	try {
			System.out.println("Exception in toEventselect() : "+expObj);
			logWrite.logMessage("Step -1 : Event select all block Exception : "+expObj, "error", Eventviewall.class);	
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
    	logWrite = null;
    	locCommonObj = null;
    	lvrEntrydate = null;
    	lvrObjeventlstitr = null;
    	lvrEvnttblvoobj = null;
    	lvrevntcountqry = null;locvrEventSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
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

  public JSONObject toEventselectNew(JSONObject praDatajson, Session praSession){	
	  JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Common locCommonObj = null;
		Date lvrEntrydate = null;
		Iterator lvrObjeventlstitr = null;
		EventTblVO lvrEvnttblvoobj = null;
		String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null,societyid=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String glbSearch="";
		try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select Event process start.");
	    	logWrite.logMessage("Step 1 : Select Event process start.", "info", Eventviewall.class);
	    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eventstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
			srchField=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchField");
			srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
			societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			EventDao lvrEvntDobj = new EventDaoservice();			
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			String locOrderby =" order by ivrBnENTRY_DATETIME desc";
			if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) { // admin , super admin
				if(Commonutility.checkempty(srchField)){ // Manual search - top
					if(srchField.equalsIgnoreCase("1")){
						
					} else if(srchField.equalsIgnoreCase("2")){
						
					} else if(srchField.equalsIgnoreCase("3")){
						
					} else if(srchField.equalsIgnoreCase("4")){
						
					} else{
						
					}
				} else { //
					if (Commonutility.checkempty(locTochenull)) { // data table search
						
					} else { // without search
						lvSlqry = "from EventDispTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS +" "+locOrderby;
					}
				}
				
			} else {
				if(Commonutility.checkempty(srchField)){ // Manual search - top
					if (srchField.equalsIgnoreCase("1")) {
						
					} else if (srchField.equalsIgnoreCase("2")) {
						
					} else if (srchField.equalsIgnoreCase("3")) {
						
					} else if (srchField.equalsIgnoreCase("4")) {
						
					} else{
						
					}
				} else { //
					if (Commonutility.checkempty(locTochenull)) { // data table search
						
					} else { // without search
						
						lvSlqry = "from EventDispTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS +" "+locOrderby;
					}
				}
			}
	    		    		    	
	    	lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("eventdetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select Event process End");
			logWrite.logMessage("Step 4 : Select Event process End", "info", Eventviewall.class);
			return lvrRtnjsonobj;
	    	
		} catch(Exception expObj){
			try {
				System.out.println("Exception in toEventselect() : "+expObj);
				logWrite.logMessage("Step -1 : Event select all block Exception : "+expObj, "error", Eventviewall.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("InitCount", count);
				lvrRtnjsonobj.put("countAfterFilter", countFilter);
				lvrRtnjsonobj.put("labordetails", "");
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
				return lvrRtnjsonobj;
		} finally {
			lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	lvrEventjsonaryobj = null;
	    	logWrite = null;
	    	locCommonObj = null;
	    	lvrEntrydate = null;
	    	lvrObjeventlstitr = null;
	    	lvrEvnttblvoobj = null;
	    	lvrevntcountqry = null;locvrEventSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
	    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
	    	lvSlqry = null;societyid=null;
		}
		
  }
  
public String getIvrparams() {
	return ivrparams;
}

public void setIvrparams(String ivrparams) {
	this.ivrparams = ivrparams;
}
  
}
