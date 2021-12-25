package com.socialindiaservices.issuemgmt;

import java.io.PrintWriter;
import java.util.Iterator;

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
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;

public class Issueviewall extends ActionSupport {
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
	        	    locObjRspdataJson=toIssueselect(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("ISSUE012"), "ISSUE012", ivrCurrntusrid);
	    				serverResponse("SI350000","0","E8006",getText("issue.selectall.error"),locObjRspdataJson);
	    			}else{
	    			//	AuditTrial.toWriteAudit(getText("ISSUE011"), "ISSUE011", ivrCurrntusrid);
	    				serverResponse("SI350000","0","S8006",getText("issue.selectall.success "),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", Issueviewall.class);
	    			serverResponse("SI350000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI8006,"+getText("request.format.notmach")+"", "info", Issueviewall.class);
			  serverResponse("SI350000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", Issueviewall.class);
			serverResponse("SI350000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      AuditTrial.toWriteAudit(getText("ISSUE012"), "ISSUE012", ivrCurrntusrid);
	      logWrite.logMessage("Service code : SI8006, Sorry, an unhandled error occurred : "+expObj, "error", Issueviewall.class);
		  serverResponse("SI350000","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	  private JSONObject toIssueselect(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Common locCommonObj = null;
		Iterator lvrObjeventlstitr = null;
		IssueTblVO lvrEvnttblvoobj = null;
		String lvrevntcountqry = null,locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,societyid=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String glbSearch="";
	    try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select Event process start.");
	    	logWrite.logMessage("Step 1 : Select Event process start.", "info", IssueTblVO.class);
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
				if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {
					glbSearch = " ";
					lvrevntcountqry="select count(issueId) from IssueTblVO where status=1";
					count = locCommonObj.gettotalcount(lvrevntcountqry);
					countFilter = count;
				}else{
					lvrevntcountqry="select count(issueId) from IssueTblVO where status=1";
					count = locCommonObj.gettotalcount(lvrevntcountqry);
					countFilter = count;
				}			
			}else {
				
				lvrevntcountqry="select count(issueId) from IssueTblVO where status=1";
				count = locCommonObj.gettotalcount(lvrevntcountqry);
				countFilter = count;
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			String locOrderby =" order by entryDatetime desc";
			if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {
				glbSearch = " AND (" + "status like ('%" + locTochenull+ "%') or " 
                         + "issueList like ('%" + locTochenull + "%'))";
				lvSlqry = "from IssueTblVO  where status=1 "+glbSearch+" "+locOrderby;
			}else{
				lvSlqry = "from IssueTblVO  where status=1 "+glbSearch+" "+locOrderby;
			}
			System.out.println("Step 3 : Event Details Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Event Details Query : "+lvSlqry, "info", IssueTblVO.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while ( lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				lvrEvnttblvoobj = (IssueTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("issueid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIssueId()));
				lvrInrJSONObj.put("issuedesc", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIssueList()));
				lvrInrJSONObj.put("issuestatus", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getStatus()));
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
			logWrite.logMessage("Step 4 : Select Event process End", "info", Issueviewall.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toEventselect() : "+expObj);
				logWrite.logMessage("Step -1 : Event select all block Exception : "+expObj, "error", Issueviewall.class);	
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
