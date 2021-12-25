package com.socialindiaservices.reports;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
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
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.ReportDaoServices;
import com.socialindiaservices.services.ReportServices;

public class LoadSocietyReportTableData  extends ActionSupport {
	  /**
	   *sdsd.
	   */
	  private static final long serialVersionUID = 1L;
	  private CommonUtils common=new CommonUtils();
	  private ReportServices reportDao=new ReportDaoServices();
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
	    				AuditTrial.toWriteAudit(getText("RPT0010"), "RPT0010", ivrCurrntusrid);
	    				serverResponse("SI6428","0","E8006",getText("society.rpt.view.error"),locObjRspdataJson);
	    			}else{
	    			//	AuditTrial.toWriteAudit(getText("RPT0009"), "RPT0009", ivrCurrntusrid);
	    				serverResponse("SI6428","0","S8006",getText("society.rpt.view.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI6428,"+getText("request.values.empty")+"", "info", LoadSocietyReportTableData.class);
	    			serverResponse("SI6428","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI6428,"+getText("request.format.notmach")+"", "info", LoadSocietyReportTableData.class);
			  serverResponse("SI6428","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI6428,"+getText("request.values.empty")+"", "info", LoadSocietyReportTableData.class);
			serverResponse("SI6428","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {
	    	AuditTrial.toWriteAudit(getText("RPT0010"), "RPT0010", ivrCurrntusrid);
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI6428, Sorry, an unhandled error occurred : "+expObj, "error", LoadSocietyReportTableData.class);
		  serverResponse("SI6428","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
		UserMasterTblVo residentTblObj = null;
		String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,
		srchField=null,srchFieldval=null,srchflg=null,entryDatetime=null,modifyDatetime=null,fromdate=null,todate=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
	    try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select Society Report process start.");
	    	logWrite.logMessage("Step 1 : Select Society Report process start.", "info", LoadSocietyReportTableData.class);
	    	
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
			String locSlqry=null;
			Query locQryrst=null;
			GroupMasterTblVo locGrpMstrVOobj=null;
			int locGrpcode=0;
			String pGrpName = getText("Grp.society");
			
			locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";
			locGrpMstrVOobj = reportDao.selectGroupByQry(locSlqry);
			if(locGrpMstrVOobj!=null){
				locGrpcode=locGrpMstrVOobj.getGroupCode();
			}
			
			if(locTochenull!=null && locTochenull.length()>0){
				searchField=" and (userName like('%"+locTochenull+"%') or emailId like('%"+locTochenull+"%') or mobileNo like('%"+locTochenull+"%') "
						+ "or dob like('%"+locTochenull+"%') )";
			}
			
			String manualsearch="";
			if (fromdate.length() >0 && todate.length() >0){
					manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
			} else if(fromdate.length() >0){
					manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
			} else if(todate.length() >0){
					manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
			}
								
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			
			String locOrderby =" order by societyId desc";							
			lvSlqry="from UserMasterTblVo where statusFlag=1 and groupCode= "+locGrpcode+" "+searchField + manualsearch+" "+locOrderby;
			
			if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
				lvrevntcountqry = "select count(userId) "+ lvSlqry;
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry);
				logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry, "info", LoadSocietyReportTableData.class);
				EventDao lvrEvntDobj = new EventDaoservice();
				count = lvrEvntDobj.getInitTotal(lvrevntcountqry);
				countFilter = count;
			}else {
				count=0;countFilter=0;
				System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LoadSocietyReportTableData.class);
			}
			
			System.out.println("Step 3 : Society Report Details Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Society Report Details Query : "+lvSlqry, "info", LoadSocietyReportTableData.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			String format="dd-MM-yyyy HH:mm:ss";
					
			while ( lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				residentTblObj = (UserMasterTblVo) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("userId", Commonutility.toCheckNullEmpty(residentTblObj.getUserId()));
				lvrInrJSONObj.put("userName", Commonutility.toCheckNullEmpty(residentTblObj.getUserName()));
				lvrInrJSONObj.put("firstName", Commonutility.toCheckNullEmpty(residentTblObj.getFirstName()));
				lvrInrJSONObj.put("lastName", Commonutility.toCheckNullEmpty(residentTblObj.getLastName()));
				lvrInrJSONObj.put("dob", Commonutility.toCheckNullEmpty(residentTblObj.getDob()));
				lvrInrJSONObj.put("mobileNo", Commonutility.toCheckNullEmpty(residentTblObj.getMobileNo()));
				lvrInrJSONObj.put("emailId", Commonutility.toCheckNullEmpty(residentTblObj.getEmailId()));
				
				if (residentTblObj.getSocietyId()!=null){	
					lvrInrJSONObj.put("societyname", Commonutility.toCheckNullEmpty(residentTblObj.getSocietyId().getSocietyName()));
					lvrInrJSONObj.put("societyuniqid", Commonutility.toCheckNullEmpty(residentTblObj.getSocietyId().getSocietyUniqyeId()));
					lvrInrJSONObj.put("societyactivationkey", Commonutility.toCheckNullEmpty(residentTblObj.getSocietyId().getActivationKey()));
					if (residentTblObj.getSocietyId().getTownshipId()!=null) {
						lvrInrJSONObj.put("townshipname", Commonutility.toCheckNullEmpty(residentTblObj.getSocietyId().getTownshipId().getTownshipName()));
						lvrInrJSONObj.put("townshipuniqid", Commonutility.toCheckNullEmpty(residentTblObj.getSocietyId().getTownshipId().getTownshipUniqueId()));
					} else {
						lvrInrJSONObj.put("townshipname", "");
						lvrInrJSONObj.put("townshipuniqid","");
					}
				} else{
					lvrInrJSONObj.put("societyname","");
					lvrInrJSONObj.put("societyuniqid","");
					lvrInrJSONObj.put("societyactivationkey", "");
					lvrInrJSONObj.put("townshipname", "");
					lvrInrJSONObj.put("townshipuniqid","");
				}
				
				if(residentTblObj.getCountryId()!=null){
					lvrInrJSONObj.put("countryName", Commonutility.toCheckNullEmpty(residentTblObj.getCountryId().getCountryName()));
				} else {
					lvrInrJSONObj.put("countryName", "");
				}
				if(residentTblObj.getStateId()!=null){
					lvrInrJSONObj.put("stateName", Commonutility.toCheckNullEmpty(residentTblObj.getStateId().getStateName()));
				} else {
					lvrInrJSONObj.put("stateName","");
				}
				if(residentTblObj.getCityId()!=null){
					lvrInrJSONObj.put("cityName", Commonutility.toCheckNullEmpty(residentTblObj.getCityId().getCityName()));
				} else {
					lvrInrJSONObj.put("cityName","");
				}
				if(residentTblObj.getEntryDatetime()!=null){
					lvrInrJSONObj.put("entryDatetime", Commonutility.toCheckNullEmpty(common.dateToString(residentTblObj.getEntryDatetime(), format) ));
				} else {
					lvrInrJSONObj.put("entryDatetime", "");
				}
				if(residentTblObj.getModifyDatetime()!=null){
					lvrInrJSONObj.put("modifyDatetime", Commonutility.toCheckNullEmpty(common.dateToString(residentTblObj.getModifyDatetime(), format) ));
				} else {
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
			System.out.println("Step 4 : Select Society Report process End");
			logWrite.logMessage("Step 4 : Select Society Report process End", "info", LoadSocietyReportTableData.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toResidentselect() : "+expObj);
				logWrite.logMessage("Step -1 : Resident select all block Exception : "+expObj, "error", LoadSocietyReportTableData.class);	
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
	    	residentTblObj = null;
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