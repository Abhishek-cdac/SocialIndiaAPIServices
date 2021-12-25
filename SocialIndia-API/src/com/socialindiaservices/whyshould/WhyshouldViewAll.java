package com.socialindiaservices.whyshould;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class WhyshouldViewAll extends ActionSupport{

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
	        	    
	        	    locObjRspdataJson=toWhyselect(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("WHY012"), "WHYC012", ivrCurrntusrid);
	    				serverResponse("SI370000","0","E8006",getText("whyshould.selectall.error"),locObjRspdataJson);
	    			}else{
	    			//	AuditTrial.toWriteAudit(getText("WHY011"), "WHY011", ivrCurrntusrid);
	    				serverResponse("SI370000","0","S370000",getText("whyshould.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    			AuditTrial.toWriteAudit(getText("WHY012"), "WHYC012", ivrCurrntusrid);
	    	    	logWrite.logMessage("Service code : SI370000,"+getText("request.values.empty")+"", "info", WhyshouldViewAll.class);
	    			serverResponse("SI370000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          AuditTrial.toWriteAudit(getText("WHY0016"), "WHY0016", ivrCurrntusrid);
	          logWrite.logMessage("Service code : SI370000,"+getText("request.format.notmach")+"", "info", WhyshouldViewAll.class);
			  serverResponse("SI370000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	AuditTrial.toWriteAudit(getText("WHY0015"), "WHY0015", ivrCurrntusrid);
	    	logWrite.logMessage("Service code : SI370000,"+getText("request.values.empty")+"", "info", WhyshouldViewAll.class);
			serverResponse("SI370000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      AuditTrial.toWriteAudit(getText("WHY012"), "WHYC012", ivrCurrntusrid);
	      logWrite.logMessage("Service code : SI370000, Sorry, an unhandled error occurred : "+expObj, "error", WhyshouldViewAll.class);
		  serverResponse("SI370000","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	private JSONObject toWhyselect(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Common locCommonObj = null;
		Iterator lvrObjeventlstitr = null;
		String lvrevntcountqry = null,locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,societyid=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		WhyShouldIUpdateTblVO lvrwhytblvoobj = null;
		String glbSearch="";
		String locOrderby =" order by entryDatetime desc";
	    try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select Why Should process start.");
	    	logWrite.logMessage("Step 1 : Select Why Should process start.", "info", WhyshouldViewAll.class);
	    	
	    	locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			Common lvrEvntDobj = new CommonDao();
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
				glbSearch = "  (" + " shortDescp like ('%" + locTochenull + "%') or " 
			             + " descp like ('%" + locTochenull + "%') "
			             + ")";	
			}else {
				
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			if(locTochenull==null || locTochenull.equalsIgnoreCase("") || locTochenull.equalsIgnoreCase("null")) {
				lvSlqry = "from WhyShouldIUpdateTblVO "+locOrderby;
				lvrevntcountqry="select count(uniqId) from WhyShouldIUpdateTblVO";
			}else{
				lvSlqry = "from WhyShouldIUpdateTblVO where"+glbSearch;
				lvrevntcountqry="select count(uniqId) from WhyShouldIUpdateTblVO where "+glbSearch+" "+locOrderby;
				
			}
			count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
			countFilter = count;
			System.out.println("Step 3 : Why Should Details Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Why Should Details Query : "+lvSlqry, "info", WhyshouldViewAll.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				lvrwhytblvoobj = (WhyShouldIUpdateTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("whyId", lvrwhytblvoobj.getUniqId());
				lvrInrJSONObj.put("shortDescp", lvrwhytblvoobj.getShortDescp());
				lvrInrJSONObj.put("status", lvrwhytblvoobj.getStatus());
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("functiondetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select Why Should process End " +lvrRtnjsonobj);
			logWrite.logMessage("Step 4 : Select Why Should process End", "info", WhyshouldViewAll.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toWhyselect() : "+expObj);
				logWrite.logMessage("Step -1 : Function select all block Exception : "+expObj, "error", WhyshouldViewAll.class);	
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
	    	lvrObjeventlstitr = null;
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
