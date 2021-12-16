package com.socialindiaservices.reports;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.eventvo.persistence.EventDao;
import com.pack.eventvo.persistence.EventDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;
import com.socialindiaservices.common.CommonUtils;

public class StaffMgmtReportTbl    extends ActionSupport {
	  /**
	   *sdsd.
	   */
	  private static final long serialVersionUID = 1L;
	  private CommonUtils common=new CommonUtils();
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
	        	    
	        	    locObjRspdataJson=toloadMerchant(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    			//	AuditTrial.toWriteAudit(getText("RPT0012"), "RPT0012", ivrCurrntusrid);
	    				serverResponse("SI6427","0","E8006",getText("staff.rpt.view.error"),locObjRspdataJson);
	    			}else{
	    				AuditTrial.toWriteAudit(getText("RPT0011"), "RPT0011", ivrCurrntusrid);
	    				serverResponse("SI6427","0","S8006",getText("staff.rpt.view.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI6427,"+getText("request.values.empty")+"", "info", StaffMgmtReportTbl.class);
	    			serverResponse("SI6427","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI6427,"+getText("request.format.notmach")+"", "info", StaffMgmtReportTbl.class);
			  serverResponse("SI6427","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI6427,"+getText("request.values.empty")+"", "info", StaffMgmtReportTbl.class);
			serverResponse("SI6427","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      AuditTrial.toWriteAudit(getText("RPT0011"), "RPT0011", ivrCurrntusrid);
	      logWrite.logMessage("Service code : SI6427, Sorry, an unhandled error occurred : "+expObj, "error", StaffMgmtReportTbl.class);
		  serverResponse("SI6427","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	  private JSONObject toloadMerchant(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Common locCommonObj = null;
		Date lvrEntrydate = null;
		Iterator lvrObjeventlstitr = null;
		StaffMasterTblVo staffMastTblObj = null;
		String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,
				lvrcmpname=null,srchField=null,srchFieldval=null,srchflg=null,entryDatetime=null,modifyDatetime=null,fromdate=null,todate=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10,sSoctyId=0,sTowshipId=0;
		String lvSlqry = null;
	    try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select Event process start.");
	    	logWrite.logMessage("Step 1 : Select Event process start.", "info", StaffMgmtReportTbl.class);
	    	
	    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eventstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
			srchField=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchField");
			srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
			fromdate=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "fromdate");
			todate=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "todate");
			sSoctyId=(Integer) Commonutility.toHasChkJsonRtnValObj(praDatajson, "sSoctyId");
			System.out.println("sSoctyId--------------"+sSoctyId);
			sTowshipId=(Integer) Commonutility.toHasChkJsonRtnValObj(praDatajson, "sTowshipId");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			String searchField="";
			if(locTochenull!=null && locTochenull.length()>0){
				searchField = " and (" + " staffName like ('%" + locTochenull + "%') or " 
						 + " staffEmail like ('%" + locTochenull + "%') or "
						 + " staffMobno like ('%" + locTochenull + "%') or "
						 + " staffAddr like ('%" + locTochenull + "%') or "
						 + " staffIdcardno like ('%" + locTochenull + "%') "									 
						 + ")";
			}
			String manualsearch="";
			if(sSoctyId>0){
				manualsearch += " and entryby.societyId.societyId="+sSoctyId;
			}
			if(sTowshipId>0){
				manualsearch += " and entryby.societyId.townshipId.townshipId="+sTowshipId;
			}
			if(fromdate.length() >0 && todate.length() >0){
					manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
			}else if(fromdate.length() >0){
					manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
			}else if(todate.length() >0){
					manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
			}
			
			if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
				lvrevntcountqry = "select count(staffID) from StaffMasterTblVo where status=1"+searchField+manualsearch;
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry);
				logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry, "info", StaffMgmtReportTbl.class);
				EventDao lvrEvntDobj = new EventDaoservice();
				count = lvrEvntDobj.getInitTotal(lvrevntcountqry);
				countFilter = count;
			}else {
				count=0;countFilter=0;
				System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", StaffMgmtReportTbl.class);
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			
			String locOrderby =" order by modifyDatetime desc";
			
			String glbSearch=null;
			
			lvSlqry="from StaffMasterTblVo where status=1 "+searchField+manualsearch+" "+locOrderby;
			System.out.println("Step 3 : Merchant Details Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Merchant Details Query : "+lvSlqry, "info", StaffMgmtReportTbl.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			String format="dd-MM-yyyy hh:mm:ss";
					
			while ( lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				staffMastTblObj = (StaffMasterTblVo) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("staffName", Commonutility.toCheckNullEmpty(staffMastTblObj.getStaffName()));
				lvrInrJSONObj.put("staffMobno", Commonutility.toCheckNullEmpty(staffMastTblObj.getStaffMobno()));
				lvrInrJSONObj.put("staffEmail", Commonutility.toCheckNullEmpty(staffMastTblObj.getStaffEmail()));
				lvrInrJSONObj.put("staffID", Commonutility.toCheckNullEmpty(staffMastTblObj.getStaffID()));
				lvrInrJSONObj.put("staffAddr", Commonutility.toCheckNullEmpty(staffMastTblObj.getStaffAddr()));
				if(staffMastTblObj.getEntryDatetime()!=null){
					lvrInrJSONObj.put("entryDatetime", Commonutility.toCheckNullEmpty(common.dateToString(staffMastTblObj.getEntryDatetime(), format) ));
				}else{
					lvrInrJSONObj.put("entryDatetime", "");
				}
				if(staffMastTblObj.getModifyDatetime()!=null){
					lvrInrJSONObj.put("modifyDatetime", Commonutility.toCheckNullEmpty(common.dateToString(staffMastTblObj.getModifyDatetime(), format) ));
				}else{
					lvrInrJSONObj.put("modifyDatetime", "");
				}
				if (staffMastTblObj.getCompanyId()!=null) {
					lvrInrJSONObj.put("staffcompanyname", Commonutility.toCheckNullEmpty(staffMastTblObj.getCompanyId().getCompanyName()));
				} else {
					lvrInrJSONObj.put("staffcompanyname", "");
				}
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;
			}
			
			
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("eventdetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select Merchant process End");
			logWrite.logMessage("Step 4 : Select Merchant process End", "info", StaffMgmtReportTbl.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toMerchantselect() : "+expObj);
				logWrite.logMessage("Step -1 : Merchant select all block Exception : "+expObj, "error", StaffMgmtReportTbl.class);	
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
	    	staffMastTblObj = null;
	    	lvrevntcountqry = null;locvrEventSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
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
