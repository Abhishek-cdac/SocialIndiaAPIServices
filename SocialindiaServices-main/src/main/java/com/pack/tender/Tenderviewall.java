package com.pack.tender;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.tenderVO.TenderTblVO;
import com.pack.tenderVO.persistance.TenderDao;
import com.pack.tenderVO.persistance.TenderDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

public class Tenderviewall extends ActionSupport {
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
        	    
        	    locObjRspdataJson=totenderselect(locObjRecvdataJson,locObjsession);		
    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
    				AuditTrial.toWriteAudit(getText("TENAD006"), "TENAD006", ivrCurrntusrid);
    				serverResponse("SI12006","0","E12006",getText("tender.selectall.error"),locObjRspdataJson);
    			}else{
    				AuditTrial.toWriteAudit(getText("TENAD005"), "TENAD005", ivrCurrntusrid);
    				serverResponse("SI12006","0","S12006",getText("tender.selectall.success"),locObjRspdataJson);					
    			}
    		}else {
    			locObjRspdataJson=new JSONObject();
    	    	logWrite.logMessage("Service code : SI12006,"+getText("request.values.empty")+"", "info", Tenderviewall.class);
    			serverResponse("SI12006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
    	    }			    	   
    	  }else {
          locObjRspdataJson=new JSONObject();
          logWrite.logMessage("Service code : SI12006,"+getText("request.format.notmach")+"", "info", Tenderviewall.class);
		  serverResponse("SI12006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
    	  }
      }else {
    	locObjRspdataJson=new JSONObject();
    	logWrite.logMessage("Service code : SI12006,"+getText("request.values.empty")+"", "info", Tenderviewall.class);
		serverResponse("SI12006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
      }      
    } catch (Exception expObj) {      
      locObjRspdataJson=new JSONObject();
      logWrite.logMessage("Service code : SI12006, Sorry, an unhandled error occurred : "+expObj, "error", Tenderviewall.class);
	  serverResponse("SI12006","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
   * To select tenders.
   */
  private JSONObject totenderselect(JSONObject praDatajson, Session praSession){	
	JSONObject lvrRtnjsonobj = null;
	JSONObject lvrInrJSONObj = null;	
	JSONArray lvrtenderjsonaryobj = null;
	Log logWrite = null;
	Common locCommonObj = null;
	Date lvrEntrydate = null;
	Iterator lvrObjtenderlstitr = null;
	TenderTblVO lvrtendertblvoobj = null;
	String lvrtendercountqry = null,locvrtenderSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null;	
	int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
	String lvSlqry = null,locvrtendersociety=null;
    try {
    	logWrite = new Log();
    	locCommonObj=new CommonDao();
    	System.out.println("Step 1 : Select tender process start.");
    	logWrite.logMessage("Step 1 : Select tender process start.", "info", Tenderviewall.class);
    	locvrtenderSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "tenderstatus");
    	locvrtendersociety = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "tendersocietyid");
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
		System.out.println("society id;;; "+societyId);
		String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
		
		String glbSearch="";
		String locOrderby =" order by entryDatetime desc";
		if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {//datatable search

			if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
				glbSearch = "  (" + " tenderName like ('%" + locTochenull + "%') or " 
			             + " tenderDate like ('%" + locTochenull + "%') or "
			             + " usrRegTbl.userName like ('%" + locTochenull + "%') or "
			             + "  usrRegTbl.societyId.societyName like ('%" + locTochenull + "%') "
			             + ")";
			}
			else
			{
				glbSearch = "  (" + " tenderName like ('%" + locTochenull + "%') or " 
			             + " tenderDate like ('%" + locTochenull + "%') or "
			             + " usrRegTbl.userName like ('%" + locTochenull + "%') or "
			             + "  usrRegTbl.societyId.societyName like ('%" + locTochenull + "%') "
			             + ") and usrRegTbl.societyId.societyId ="+ Integer.parseInt(societyId);
			}
				lvSlqry = "from TenderTblVO   where  " + glbSearch+" "+locOrderby;	
				lvrtendercountqry=	"select count(*) from TenderTblVO   where  " + glbSearch;	
		}else {		
			if(searchWord!=null && !searchWord.equalsIgnoreCase("null") && !searchWord.equalsIgnoreCase("")){// Top search box
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = "  ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
						glbSearch += "( tenderName like ('%" + searchWord + "%') )";
					}
					else if(slectfield!=null && slectfield.equalsIgnoreCase("2"))
					{
						glbSearch += "( usrRegTbl.societyId.societyName like ('%" + searchWord + "%') )";
					}
					else if(slectfield!=null && slectfield.equalsIgnoreCase("3"))
					{
						glbSearch += " ( usrRegTbl.userName like ('%" + searchWord + "%') )";
					}
					else{
						glbSearch += "(usrRegTbl.societyId.societyName like ('%" + searchWord + "%') or ";					
						glbSearch += " tenderName like ('%" + searchWord + "%') or ";
						glbSearch += "  usrRegTbl.userName like ('%" + searchWord + "%') )";
					}
					glbSearch += " and usrRegTbl.societyId.societyId = "+societyId;
				}else{
					glbSearch = "  ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){	
						glbSearch += "("+"usrRegTbl.societyId.societyName like ('%" + searchWord + "%') or  tenderName like ('%" + searchWord + "%') "
								+ "or tenderDate like ('%" + searchWord + "%') or usrRegTbl.userName like ('%" + searchWord + "%'))";
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("3")){
						glbSearch += "( usrRegTbl.societyId.societyName like ('%" + searchWord + "%') )";
					}
					else if(slectfield!=null && slectfield.equalsIgnoreCase("2")){
						glbSearch += " (tenderName like ('%" + searchWord + "%'))";
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("4")){
						glbSearch += "(tenderDate like ('%" + searchWord + "%'))";
					}else{
						glbSearch += "(usrRegTbl.societyId.societyName like ('%" + searchWord + "%') or ";					
						glbSearch += " tenderName like ('%" + searchWord + "%') )";
						glbSearch += "  usrRegTbl.userName like ('%" + searchWord + "%') )";
					}									 		 
				}
			}else{	
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = "  usrRegTbl.societyId.societyId = "+societyId;
					
				}else{
					glbSearch = "tenderName like ('%%')";
					
				}
				
			}
			lvSlqry = "from TenderTblVO where  "+glbSearch +" "+locOrderby;
			lvrtendercountqry = "select count(*) from TenderTblVO where   "+glbSearch ;
		
		}
		if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
			
			//lvrtendercountqry = "select count(tenderId) from TenderTblVO where usrRegTbl.societyId.societyId="+locvrtendersociety+"";
			TenderDao lvrtenderDobj = new TenderDaoservice();
			count = lvrtenderDobj.getInitTotal(lvrtendercountqry);
			countFilter = count;
		System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrtendercountqry);
		logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrtendercountqry, "info", Tenderviewall.class);
		
	}else {
		count=0;countFilter=0;
		System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
		logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Tenderviewall.class);
	}
	
	if (Commonutility.toCheckisNumeric(locStrRow)) {
		 startrowfrom=Integer.parseInt(locStrRow);
	}
	if (Commonutility.toCheckisNumeric(locMaxrow)) {
		totalNorow=Integer.parseInt(locMaxrow);
	}

		System.out.println("Step 3 : tender Details Query : "+lvSlqry);
		logWrite.logMessage("Step 3 : tender Details Query : "+lvSlqry, "info", Tenderviewall.class);
		lvrObjtenderlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
		lvrtenderjsonaryobj = new JSONArray();
		while ( lvrObjtenderlstitr.hasNext() ) {
			lvrInrJSONObj=new JSONObject();
			lvrtendertblvoobj = (TenderTblVO) lvrObjtenderlstitr.next();
			lvrInrJSONObj.put("tendertitle", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getTenderName()));
			lvrInrJSONObj.put("tenderdesc", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getTenderDetails()));
			lvrInrJSONObj.put("tenderid", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getTenderId()));
			lvrInrJSONObj.put("tendercurntusrid", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getUsrRegTbl().getUserId()));
			lvrInrJSONObj.put("tendercurntgrpid", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getGroupMstTbl()));
			lvrInrJSONObj.put("tenderstartdate", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getTenderDate()));
			if(lvrtendertblvoobj.getUsrRegTbl().getSocietyId()!=null){
			lvrInrJSONObj.put("tendersocietyname", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getUsrRegTbl().getSocietyId().getSocietyName()));
			}
			else
			{
				lvrInrJSONObj.put("tendersocietyname", "");
			}
			if(lvrtendertblvoobj.getUsrRegTbl().getFirstName()!=null && Commonutility.checkempty(lvrtendertblvoobj.getUsrRegTbl().getFirstName())){
				lvrInrJSONObj.put("tenderusername", Commonutility.toFirstCharUpper(Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getUsrRegTbl().getFirstName())));
			} else if(lvrtendertblvoobj.getUsrRegTbl().getLastName()!=null && Commonutility.checkempty(lvrtendertblvoobj.getUsrRegTbl().getLastName())){
				lvrInrJSONObj.put("tenderusername", Commonutility.toFirstCharUpper(Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getUsrRegTbl().getLastName())));
			} else {
				lvrInrJSONObj.put("tenderusername", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getUsrRegTbl().getMobileNo()));
			}
			
			
			lvrInrJSONObj.put("tenderentryby", Commonutility.toCheckNullEmpty(lvrtendertblvoobj.getEntryBy()));
			lvrEntrydate=lvrtendertblvoobj.getEntryDatetime();
			lvrInrJSONObj.put("tenderentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd hh:mm:ss"));
			
			lvrtenderjsonaryobj.put(lvrInrJSONObj);
			lvrInrJSONObj = null;
		}
		lvrRtnjsonobj=new JSONObject();	
		lvrRtnjsonobj.put("InitCount", count);
		lvrRtnjsonobj.put("countAfterFilter", countFilter);
		lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
		lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
		lvrRtnjsonobj.put("tenderdetails", lvrtenderjsonaryobj);
		System.out.println("Step 4 : Select tender process End");
		logWrite.logMessage("Step 4 : Select tender process End", "info", Tenderviewall.class);
    return lvrRtnjsonobj;
    }catch(Exception expObj) {
    	try {
			System.out.println("Exception in totenderselect() : "+expObj);
			logWrite.logMessage("Step -1 : tender select all block Exception : "+expObj, "error", Tenderviewall.class);	
			lvrRtnjsonobj=new JSONObject();
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("tenderdetails", "");
			lvrRtnjsonobj.put("CatchBlock", "Error");
			System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
			}catch(Exception ee){}finally{}
     return lvrRtnjsonobj;
    }finally {
    	lvrRtnjsonobj = null;
    	lvrInrJSONObj = null;	
    	lvrtenderjsonaryobj = null;
    	logWrite = null;
    	locCommonObj = null;
    	lvrEntrydate = null;
    	lvrObjtenderlstitr = null;
    	lvrtendertblvoobj = null;
    	lvrtendercountqry = null;locvrtenderSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
    	lvSlqry = null;
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
