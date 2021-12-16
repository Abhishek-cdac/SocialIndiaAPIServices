package com.pack.compaign;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.contents.ContentDao;
import com.mobi.contents.ContentDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpDonationInstitutionTbl;

public class DonateViewAll extends ActionSupport{	
	  private static final long serialVersionUID = 1L;
	  private String ivrparams;
	  /**
	   * Executed Method .
	   */
	  ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
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
	        	    
	        	    locObjRspdataJson=tocyberplat(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("CAMP00012"), "CAMP00012", ivrCurrntusrid);
	    				serverResponse("SI8006","0","E8006",getText("campaign.selectall.error"),locObjRspdataJson);
	    			}else{
	    				AuditTrial.toWriteAudit(getText("CAMP00011"), "CAMP00011", ivrCurrntusrid);
	    				serverResponse("SI8006","0","S8006",getText("campaign.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", DonateViewAll.class);
	    			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI8006,"+getText("request.format.notmach")+"", "info", DonateViewAll.class);
			  serverResponse("SI8006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", DonateViewAll.class);
			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI8006, Sorry, an unhandled error occurred : "+expObj, "error", DonateViewAll.class);
		  serverResponse("SI8006","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	  private JSONObject tocyberplat(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Common locCommonObj = null;
		Iterator lvrObjeventlstitr = null;
		MvpDonationInstitutionTbl donate = null;
		String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String orderby=null;
	    try {
	    	orderby=" order by entryDate desc";
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select MvpDonationInstitutionTbl process start.");
	    	logWrite.logMessage("Step 1 : Select MvpDonationInstitutionTbl process start.", "info", DonateViewAll.class);
	    	
	    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eventstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
			srchField=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchField");
			srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
			String searchWord=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "searchWord");
			String societyId=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyId");
			String slectfield=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "selectfield");
			int sSoctyId=(int) Commonutility.toHasChkJsonRtnValObj(praDatajson, "sSoctyId");
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			String glbSearch="";
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
				glbSearch = " and (" + " institutionName like ('%" + locTochenull + "%')  " 
						             + ")";
				}
				else{
					glbSearch = " and (" + " institutionName like ('%" + locTochenull + "%')  " 
				             + ") and societyId ="+societyId;
				}
			}else {/*
				if(searchWord!=null && !searchWord.equalsIgnoreCase("null") && !searchWord.equalsIgnoreCase("")){// Top search box
					if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
						glbSearch = " and ";
						if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
							glbSearch = " and (" + " newsContent like ('%" + searchWord + "%') " 
									             + ") and societyId ="+societyId;
						}else if(slectfield!=null && slectfield.equalsIgnoreCase("2")){
							glbSearch = " and (" + " expiryDate like ('%" + searchWord + "%') " 
									             + ") and societyId ="+societyId;
						}
						else{
							glbSearch = " and (" + " newsContent like ('%" + searchWord + "%') or " 
									+ " expiryDate like ('%" + searchWord + "%')  " 
									             + ") and societyId ="+societyId;
						}
					}else{
						glbSearch = " and ";
						if(slectfield!=null && slectfield.equalsIgnoreCase("1")){
							glbSearch = " and (" + "newsContent like ('%" + searchWord + "%') ) "; 
						}else if(slectfield!=null && slectfield.equalsIgnoreCase("2")){
							glbSearch = " and (" + "   expiryDate like ('%" + searchWord + "%'))" ;
						
						}else{
							glbSearch = " and (" + " newsContent like ('%" + searchWord + "%') or " 
									+ " expiryDate like ('%" + searchWord + "%')  " 
									             + ") ";
						}
					}
				}else{
					if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
						glbSearch = "and societyId ="+societyId;
					}else{
						glbSearch = "";
					}
					
				}										
				
			*/}
			lvSlqry = "from MvpDonationInstitutionTbl  where status=" + locvrEventSTS + " " + glbSearch+" "+orderby;	
			lvrevntcountqry = "select count(*) from MvpDonationInstitutionTbl   where  status=" + locvrEventSTS+ " " + glbSearch;	
			ContentDao reconDobj=new ContentDaoServices();
			count = reconDobj.getInitTotal(lvrevntcountqry);
			countFilter = count;
			
			System.out.println("Step 3 : MvpDonationInstitutionTbl Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : MvpDonationInstitutionTbl  Query : "+lvSlqry, "info", DonateViewAll.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while ( lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				donate = (MvpDonationInstitutionTbl) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("donateId", Commonutility.toCheckNullEmpty(donate.getInstitutionId()));
				lvrInrJSONObj.put("donateName", Commonutility.toCheckNullEmpty(donate.getInstitutionName()));
				lvrInrJSONObj.put("donateContact", Commonutility.toCheckNullEmpty(donate.getInstitutionContact()));
				lvrInrJSONObj.put("donateImage", Commonutility.toCheckNullEmpty(donate.getImageName()));
				lvrInrJSONObj.put("entryDate", Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(donate.getEntryDate(), "dd-MM-yyyy hh:mm:ss")));
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("donation", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select MvpDonationInstitutionTbl process End");
			logWrite.logMessage("Step 4 : Select MvpDonationInstitutionTbl process End", "info", DonateViewAll.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in MvpDonationInstitutionTbl() : "+expObj);
				logWrite.logMessage("Step -1 : MvpDonationInstitutionTbl select all block Exception : "+expObj, "error", DonateViewAll.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("InitCount", count);
				lvrRtnjsonobj.put("countAfterFilter", countFilter);
				lvrRtnjsonobj.put("donation", "");
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
	    	donate = null;
	    	lvrevntcountqry = null;locvrEventSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
	    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
	    	lvSlqry = null;
	    }
	  }
	  
	  private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson) {
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
