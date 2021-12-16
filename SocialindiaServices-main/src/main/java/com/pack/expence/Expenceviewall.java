package com.pack.expence;


import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.expencevo.ExpenceTblVO;
import com.pack.tenderVO.persistance.TenderDao;
import com.pack.tenderVO.persistance.TenderDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

public class Expenceviewall extends ActionSupport {
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
	String ivrservicecode=null;
	String ivrcurrntusridobj=null;
	int ivrCurrntusrid=0;
    try {
      logWrite = new Log();
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
        	    
        	    locObjRspdataJson=toexpenceselect(locObjRecvdataJson);		
    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
    				AuditTrial.toWriteAudit(getText("EXPSAD0010"), "EXPSAD0010", ivrCurrntusrid);
    				serverResponse("SI13006","0","E13006",getText("expence.selectall.error"),locObjRspdataJson);
    			}else{
    				AuditTrial.toWriteAudit(getText("EXPSAD009"), "EXPSAD009", ivrCurrntusrid);
    				serverResponse("SI13006","0","S13006",getText("expence.selectall.success"),locObjRspdataJson);					
    			}
    		}else {
    			locObjRspdataJson=new JSONObject();
    	    	logWrite.logMessage("Service code : SI13006,"+getText("request.values.empty")+"", "info", Expenceviewall.class);
    			serverResponse("SI13006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
    	    }			    	   
    	  }else {
          locObjRspdataJson=new JSONObject();
          logWrite.logMessage("Service code : SI13006,"+getText("request.format.notmach")+"", "info", Expenceviewall.class);
		  serverResponse("SI13006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
    	  }
      }else {
    	locObjRspdataJson=new JSONObject();
    	logWrite.logMessage("Service code : SI13006,"+getText("request.values.empty")+"", "info", Expenceviewall.class);
		serverResponse("SI13006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
      }      
    } catch (Exception expObj) {      
      locObjRspdataJson=new JSONObject();
      logWrite.logMessage("Service code : SI13006, Sorry, an unhandled error occurred : "+expObj, "error", Expenceviewall.class);
	  serverResponse("SI13006","2","ER0002",getText("catch.error"),locObjRspdataJson);
	} finally {
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
   * To select expences.
   */
  private JSONObject toexpenceselect(JSONObject praDatajson){	
	JSONObject lvrRtnjsonobj = null;
	JSONObject lvrInrJSONObj = null;	
	JSONArray lvrexpencejsonaryobj = null;
	Log logWrite = null;
	Common locCommonObj = null;
	Date lvrEntrydate = null;
	Iterator lvrObjexpencelstitr = null;
	ExpenceTblVO lvrexpencetblvoobj = null;
	String lvrexpencecountqry = null,locvrexpenceSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null;	
	int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
	String lvSlqry = null,locvrexpencesociety=null;
	String lvrSlqry ="";
	Query lvrQrybj=null;
	Session locHbsession = null;

	UserMasterTblVo locUsertblobj=null;
    try {
    	logWrite = new Log();
    	locCommonObj=new CommonDao();
    	locHbsession = HibernateUtil.getSession();
    	System.out.println("Step 1 : Select expence process start.");
    	logWrite.logMessage("Step 1 : Select expence process start.", "info", Expenceviewall.class);
    	
    	locvrexpenceSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "expencestatus");
    	locvrexpencesociety = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "expencesocietyid");
		locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
		locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
		locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
		locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
		locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
		srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
		srchField=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchField");
		srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
		
		String societyId=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "society");
		String slectfield=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "slectfield");
		String searchWord=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
		System.out.println("society id;;; "+locvrexpencesociety);
		String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
		
		
		String glbSearch="";
		String locOrderby =" order by entryDatetime desc";
		
		if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
			if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
			glbSearch = "  and (" + " expenceFor like ('%" + locTochenull + "%') or " 
		             + " noOfProduct like ('%" + locTochenull + "%') or "
		             + " productPerAmt like ('%" + locTochenull + "%') or "
		             + "  expenceTotAmt like ('%" + locTochenull + "%') or "
		                + "  usrid.societyId is not null and usrid.societyId.societyName like ('%" + locTochenull + "%') "
		             + ")";
			}
			else
			{
				glbSearch = "  and (" + " expenceFor like ('%" + locTochenull + "%') or " 
			             + " noOfProduct like ('%" + locTochenull + "%') or "
			             + " productPerAmt like ('%" + locTochenull + "%') or "
			             + "  expenceTotAmt like ('%" + locTochenull + "%') or "
			             + "  usrid.societyId is not null and usrid.societyId.societyName like ('%" + locTochenull + "%') "
			             + ") and usrid.societyId.societyId =" + Integer.parseInt(societyId);
			}
				lvSlqry = "from ExpenceTblVO where expenceStatus="+locvrexpenceSTS + glbSearch+" "+locOrderby;	
				lvrexpencecountqry="select count(*) from ExpenceTblVO   where  expenceStatus="+locvrexpenceSTS + glbSearch+"";
		}else {
			if(searchWord!=null && !searchWord.equalsIgnoreCase("null") && !searchWord.equalsIgnoreCase("")){// Top search box
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
						glbSearch += "( expenceFor like ('%" + searchWord + "%') )";
						/*glbSearch = "and (" + " expenceFor like ('%" + searchWord + "%') or " 
					             + " noOfProduct like ('%" + searchWord + "%') or "
					             + " productPerAmt like ('%" + searchWord + "%') or "
					             + "  expenceTotAmt like ('%" + searchWord + "%') or "
					             + "  usrid.societyId is not null and usrid.societyId.societyName like ('%" + searchWord + "%') "
					             + ")";*/
					}else{
					/*	glbSearch += "(usrid.societyId.societyName like ('%" + searchWord + "%') or ";					
				 		 
						glbSearch += " expenceFor like ('%" + searchWord + "%') )";*/
						glbSearch = " and (" + " expenceFor like ('%" + searchWord + "%') or " 
					             + " noOfProduct like ('%" + searchWord + "%') or "
					             + " productPerAmt like ('%" + searchWord + "%') or "
					             + "  expenceTotAmt like ('%" + searchWord + "%') or "
					             + "  usrid.societyId is not null and usrid.societyId.societyName like ('%" + searchWord + "%') "
					             + ")";
					}									 		 
					glbSearch += " and usrid.societyId.societyId = "+societyId;
				}
				else
				{
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
						glbSearch += "( expenceFor like ('%" + searchWord + "%') )";
					}else{
						glbSearch = " and (" + " expenceFor like ('%" + searchWord + "%') or " 
					             + " noOfProduct like ('%" + searchWord + "%') or "
					             + " productPerAmt like ('%" + searchWord + "%') or "
					             + "  expenceTotAmt like ('%" + searchWord + "%') or "
					             + "  usrid.societyId is not null and usrid.societyId.societyName like ('%" + searchWord + "%') "
					             + ")";
						/*glbSearch += "(usrid.societyId.societyName like ('%" + searchWord + "%') or ";					
						glbSearch += " expenceFor like ('%" + searchWord + "%') )";*/
					}	
				}
			}
			else{
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and usrid.societyId.societyId = "+societyId;
				}else{
					glbSearch = "";
				}
				
			}	
			lvSlqry = "from ExpenceTblVO where expenceStatus=" + locvrexpenceSTS + " "+glbSearch +" "+locOrderby;
			lvrexpencecountqry = "select count(*) from ExpenceTblVO where   expenceStatus=" + locvrexpenceSTS + "  "+glbSearch;
		}
		if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
		
				//lvrexpencecountqry = "select count(expnId) from ExpenceTblVO where expenceStatus='"+locvrexpenceSTS+"'";
				TenderDao lvrexpenceDobj = new TenderDaoservice();
				count = lvrexpenceDobj.getInitTotal(lvrexpencecountqry);
				countFilter = count;
			System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrexpencecountqry);
			logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrexpencecountqry, "info", Expenceviewall.class);
			
		}else {
			count=0;countFilter=0;
			System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
			logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Expenceviewall.class);
		}
		
		if (Commonutility.toCheckisNumeric(locStrRow)) {
			 startrowfrom=Integer.parseInt(locStrRow);
		}
		if (Commonutility.toCheckisNumeric(locMaxrow)) {
			totalNorow=Integer.parseInt(locMaxrow);
		}
		
		System.out.println("Step 3 : expence Details Query : "+lvSlqry);
		logWrite.logMessage("Step 3 : expence Details Query : "+lvSlqry, "info", Expenceviewall.class);
		lvrObjexpencelstitr=locHbsession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
		lvrexpencejsonaryobj = new JSONArray();
		while ( lvrObjexpencelstitr.hasNext() ) {
			lvrInrJSONObj=new JSONObject();
			lvrexpencetblvoobj = (ExpenceTblVO) lvrObjexpencelstitr.next();
			lvrInrJSONObj.put("expencetitle", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getExpenceFor()));
			lvrInrJSONObj.put("expencedesc", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getDescr()));
			lvrInrJSONObj.put("expenceid", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getExpnId()));
			lvrInrJSONObj.put("expencecurntusrid", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getEntryBy()));
			lvrInrJSONObj.put("expencecurntgrpid", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getEntryByGrp()));
			lvrInrJSONObj.put("expenceprdtamt", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getProductPerAmt()));
			lvrInrJSONObj.put("expencenoofprdts", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getNoOfProduct()));
			lvrInrJSONObj.put("expencetotamt", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getExpenceTotAmt()));
			
			/*String expenceentryidval = Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getEntryBy());
			System.out.println("expenceentryidval()))  "+expenceentryidval);
			lvrSlqry = "from UserMasterTblVo where societyId = "+Integer.parseInt(expenceentryidval);
			lvrQrybj = locHbsession.createQuery(lvrSlqry);
			locUsertblobj = (UserMasterTblVo)lvrQrybj.uniqueResult();
			System.out.println("userobj== "+locUsertblobj.getUserName());*/
			if(lvrexpencetblvoobj.getUsrid().getSocietyId()!=null){
				lvrInrJSONObj.put("expencesocietyname",  Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getUsrid().getSocietyId().getSocietyName()));
				}
				else
				{
					lvrInrJSONObj.put("expencesocietyname", "");
				}
			
			lvrInrJSONObj.put("expenceentryby", Commonutility.toCheckNullEmpty(lvrexpencetblvoobj.getEntryBy()));
			lvrEntrydate=lvrexpencetblvoobj.getEntryDatetime();
			lvrInrJSONObj.put("expenceentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
			
			lvrexpencejsonaryobj.put(lvrInrJSONObj);
			lvrInrJSONObj = null;
		}
		
		
		lvrRtnjsonobj=new JSONObject();	
		lvrRtnjsonobj.put("InitCount", count);
		lvrRtnjsonobj.put("countAfterFilter", countFilter);
		lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
		lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
		lvrRtnjsonobj.put("expencedetails", lvrexpencejsonaryobj);
		System.out.println("Step 4 : Select expence process End");
		logWrite.logMessage("Step 4 : Select expence process End", "info", Expenceviewall.class);
    return lvrRtnjsonobj;
    }catch(Exception expObj) {
    	try {
			System.out.println("Exception in toexpenceselect() : "+expObj);
			logWrite.logMessage("Step -1 : expence select all block Exception : "+expObj, "error", Expenceviewall.class);	
			lvrRtnjsonobj=new JSONObject();
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("expencedetails", "");
			lvrRtnjsonobj.put("CatchBlock", "Error");
			System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
			}catch(Exception ee){}finally{}
     return lvrRtnjsonobj;
    }finally {
    	if(locHbsession!=null){locHbsession.flush(); locHbsession.clear();locHbsession.close();locHbsession=null;}
    	lvrRtnjsonobj = null;
    	lvrInrJSONObj = null;	
    	lvrexpencejsonaryobj = null;
    	logWrite = null;
    	locCommonObj = null;
    	lvrEntrydate = null;
    	lvrObjexpencelstitr = null;
    	lvrexpencetblvoobj = null;
    	lvrexpencecountqry = null;locvrexpenceSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
    	lvSlqry = null;
    	lvrSlqry="";
    	lvrQrybj=null;
    	locUsertblobj=null;
    }
  }
  
  private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
  {
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
