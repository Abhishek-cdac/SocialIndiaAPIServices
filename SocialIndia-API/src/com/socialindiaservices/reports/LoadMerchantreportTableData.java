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
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.merchantmgmt.LoadMerchantTableData;
import com.socialindiaservices.vo.MerchantTblVO;

public class LoadMerchantreportTableData   extends ActionSupport {
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
	    				AuditTrial.toWriteAudit(getText("RPT0006"), "RPT0006", ivrCurrntusrid);
	    				serverResponse("SI6426","0","E8006",getText("merchant.rpt.view.error"),locObjRspdataJson);
	    			}else{
	    			//	AuditTrial.toWriteAudit(getText("RPT0005"), "RPT0005", ivrCurrntusrid);
	    				serverResponse("SI6426","0","S8006",getText("merchant.rpt.view.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI6426,"+getText("request.values.empty")+"", "info", LoadMerchantTableData.class);
	    			serverResponse("SI6426","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI6426,"+getText("request.format.notmach")+"", "info", LoadMerchantTableData.class);
			  serverResponse("SI6426","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI6426,"+getText("request.values.empty")+"", "info", LoadMerchantTableData.class);
			serverResponse("SI6426","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	    	AuditTrial.toWriteAudit(getText("RPT0006"), "RPT0006", ivrCurrntusrid);
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI6426, Sorry, an unhandled error occurred : "+expObj, "error", LoadMerchantTableData.class);
	    	serverResponse("SI6426","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
		MerchantTblVO merchantTblObj = null;
		String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,
				srchField=null,srchFieldval=null,srchflg=null,entryDatetime=null,modifyDatetime=null,fromdate=null,todate=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
	    try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select Event process start.");
	    	logWrite.logMessage("Step 1 : Select Event process start.", "info", LoadMerchantTableData.class);
	    	
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
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			String searchField="";
			if(locTochenull!=null && locTochenull.length()>0){
				searchField=" and (mrchntName like('%"+locTochenull+"%') or mrchntEmail like('%"+locTochenull+"%') or mrchntPhNo like('%"+locTochenull+"%') "
						+ "or shopName like('%"+locTochenull+"%') )";
				 /*or mrchCategoryId.mrchCategoryName like('%"+locTochenull+"%') or countryId.countryName like('%"+locTochenull+"%') "
					+ "or stateId.stateName like('%"+locTochenull+"%') or cityId.cityName like('%"+locTochenull+"%')
*/			}
			String manualsearch="";
			if(fromdate.length() >0 && todate.length() >0){
					manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
			}else if(fromdate.length() >0){
					manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
			}else if(todate.length() >0){
					manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
			}
			
			if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
				lvrevntcountqry = "select count(mrchntId) from MerchantTblVO where mrchntActSts=1"+searchField+manualsearch;
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry);
				logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry, "info", LoadMerchantTableData.class);
				EventDao lvrEvntDobj = new EventDaoservice();
				count = lvrEvntDobj.getInitTotal(lvrevntcountqry);
				countFilter = count;
			}else {
				count=0;countFilter=0;
				System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LoadMerchantTableData.class);
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			
			String locOrderby =" order by modifyDatetime desc";
			
			String glbSearch=null;
			
			lvSlqry="from MerchantTblVO where mrchntActSts=1 "+searchField+manualsearch+" "+locOrderby;
			System.out.println("Step 3 : Merchant Details Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Merchant Details Query : "+lvSlqry, "info", LoadMerchantTableData.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			String format="dd-MM-yyyy hh:mm:ss";
					
			while ( lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				merchantTblObj = (MerchantTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("mrchntId", Commonutility.toCheckNullEmpty(merchantTblObj.getMrchntId()));
				lvrInrJSONObj.put("mrchntName", Commonutility.toCheckNullEmpty(merchantTblObj.getMrchntName()));
				lvrInrJSONObj.put("mrchntEmail", Commonutility.toCheckNullEmpty(merchantTblObj.getMrchntEmail()));
				lvrInrJSONObj.put("shopName", Commonutility.toCheckNullEmpty(merchantTblObj.getShopName()));
				lvrInrJSONObj.put("mrchntPhNo", Commonutility.toCheckNullEmpty(merchantTblObj.getMrchntPhNo()));
				if(merchantTblObj.getCountryId()!=null){
				lvrInrJSONObj.put("countryName", Commonutility.toCheckNullEmpty(merchantTblObj.getCountryId().getCountryName()));
				}else{
					lvrInrJSONObj.put("countryName", "");
				}
				if(merchantTblObj.getStateId()!=null){
					lvrInrJSONObj.put("stateName", Commonutility.toCheckNullEmpty(merchantTblObj.getStateId().getStateName()));
				}else{
					lvrInrJSONObj.put("stateName","");
				}
				if(merchantTblObj.getCityId()!=null){
					lvrInrJSONObj.put("cityName", Commonutility.toCheckNullEmpty(merchantTblObj.getCityId().getCityName()));
				}else{
					lvrInrJSONObj.put("cityName","");
				}
				if(merchantTblObj.getMrchCategoryId()!=null){
					lvrInrJSONObj.put("mrchCategoryName", Commonutility.toCheckNullEmpty(merchantTblObj.getMrchCategoryId().getMrchCategoryName()));
				}else{
					lvrInrJSONObj.put("mrchCategoryName", "");
				}
				if(merchantTblObj.getEntryDatetime()!=null){
					lvrInrJSONObj.put("entryDatetime", Commonutility.toCheckNullEmpty(common.dateToString(merchantTblObj.getEntryDatetime(), format) ));
				}else{
					lvrInrJSONObj.put("entryDatetime", "");
				}
				if(merchantTblObj.getModifyDatetime()!=null){
					lvrInrJSONObj.put("modifyDatetime", Commonutility.toCheckNullEmpty(common.dateToString(merchantTblObj.getModifyDatetime(), format) ));
				}else{
					lvrInrJSONObj.put("modifyDatetime", "");
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
			logWrite.logMessage("Step 4 : Select Merchant process End", "info", LoadMerchantTableData.class);
			return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toMerchantselect() : "+expObj);
				logWrite.logMessage("Step -1 : Merchant select all block Exception : "+expObj, "error", LoadMerchantTableData.class);	
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
	    	merchantTblObj = null;
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