package com.socialindiaservices.merchantmgmt;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.eventvo.persistence.EventDao;
import com.pack.eventvo.persistence.EventDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantUtilitiImageTblVO;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public class LoadMerchantOfferTableData   extends ActionSupport {
	  /**
	   *sdsd.
	   */
	  private static final long serialVersionUID = 1L;
	  private String ivrparams;
	  /**
	   * Executed Method .
	   */
	  ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	  private MerchantManageServices merchHbm=new MerchantManageDaoServices();
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
	        	    System.out.println("locObjRspdataJson------------------"+locObjRspdataJson.toString());
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("MCH0004"), "MCH0004", ivrCurrntusrid);
	    				serverResponse("SI6423","0","E8006",getText("mrchnt.offers.select.error"),locObjRspdataJson);
	    			}else{
	    				AuditTrial.toWriteAudit(getText("MCH0003"), "MCH0003", ivrCurrntusrid);
	    				serverResponse("SI6423","0","S8006",getText("mrchnt.offers.select.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI6423,"+getText("request.values.empty")+"", "info", LoadMerchantOfferTableData.class);
	    			serverResponse("SI6423","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI6423,"+getText("request.format.notmach")+"", "info", LoadMerchantOfferTableData.class);
			  serverResponse("SI6423","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI6423,"+getText("request.values.empty")+"", "info", LoadMerchantOfferTableData.class);
			serverResponse("SI6423","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI6423, Sorry, an unhandled error occurred : "+expObj, "error", LoadMerchantOfferTableData.class);
		  serverResponse("SI6423","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
		Date lvrEntrydate = null;
		Iterator lvrObjeventlstitr = null;
		MerchantUtilitiTblVO merchantTblObj = null;
		CommonUtils common=new CommonUtils();
		String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10,merchantId=0;
		String lvSlqry = null;
	    try {
	    	logWrite = new Log();
	    	System.out.println("praDatajson----------------"+praDatajson.toString());
	    	System.out.println("Step 1 : Select Event process start.");
	    	logWrite.logMessage("Step 1 : Select Event process start.", "info", LoadMerchantOfferTableData.class);
	    	System.out.println("11111111");
	    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eventstatus");
	    	System.out.println("11111111-----"+locvrEventSTS);
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
			srchField=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchField");
			srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
			merchantId =  (Integer) Commonutility.toHasChkJsonRtnValObj(praDatajson, "mrchntId");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			System.out.println("locvrCntflg-----"+locvrCntflg);
			System.out.println("locTochenull-----"+locTochenull);
			String searchField="";
			if(locTochenull!=null && locTochenull.length()>0){
				searchField=" and (broucherName like('%"+locTochenull+"%') or offerName like('%"+locTochenull+"%') or offerPrice like('%"+locTochenull+"%') "
						+ "or actualPrice like('%"+locTochenull+"%') )";
			}
			if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
				lvrevntcountqry = "select count(mrchntId) from MerchantUtilitiTblVO where statusFlag=1 and mrchntId.mrchntId="+merchantId+searchField;
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry);
				logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry, "info", LoadMerchantOfferTableData.class);
				EventDao lvrEvntDobj = new EventDaoservice();
				count = lvrEvntDobj.getInitTotal(lvrevntcountqry);
				countFilter = count;
			}else {
				count=0;countFilter=0;
				System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LoadMerchantOfferTableData.class);
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			
			
			String glbSearch=null;
			String locOrderby =" order by modifyDatetime desc";
			lvSlqry="from MerchantUtilitiTblVO where statusFlag=1 and mrchntId.mrchntId="+merchantId+searchField+" "+locOrderby;
			System.out.println("Step 3 : Merchant Details Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Merchant Details Query : "+lvSlqry, "info", LoadMerchantOfferTableData.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			MerchantUtilitiImageTblVO merchImageObj=new MerchantUtilitiImageTblVO();
			while ( lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				merchImageObj=new MerchantUtilitiImageTblVO();
				merchantTblObj = (MerchantUtilitiTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("merchantUtilId", Commonutility.toCheckNullEmpty(merchantTblObj.getMerchantUtilId()));
				lvrInrJSONObj.put("broucherName", Commonutility.toCheckNullEmpty(merchantTblObj.getBroucherName()));
				lvrInrJSONObj.put("offerName", Commonutility.toCheckNullEmpty(merchantTblObj.getOfferName()));
				lvrInrJSONObj.put("offerPrice", Commonutility.toCheckNullEmpty(Float.toString(merchantTblObj.getOfferPrice())));
				lvrInrJSONObj.put("actualPrice", Commonutility.toCheckNullEmpty(Float.toString(merchantTblObj.getActualPrice())));
				if(merchantTblObj.getOfferValidFrom()!=null){
				lvrInrJSONObj.put("offerValidFrom", Commonutility.toCheckNullEmpty(common.dateToString(merchantTblObj.getOfferValidFrom(),rb.getString("calendar.dateformat"))));
				}else{
					lvrInrJSONObj.put("offerValidFrom", "");
				}
				if(merchantTblObj.getOfferValidTo()!=null){
				lvrInrJSONObj.put("offerValidTo", Commonutility.toCheckNullEmpty(common.dateToString(merchantTblObj.getOfferValidTo(),rb.getString("calendar.dateformat"))));
				}else{
					lvrInrJSONObj.put("offerValidTo", "");
				}
				lvrInrJSONObj.put("mrchntId", Commonutility.toCheckNullEmpty(merchantTblObj.getMrchntId().getMrchntId()));
				String qry="From MerchantUtilitiImageTblVO where merchantUtilId.merchantUtilId="+merchantTblObj.getMerchantUtilId() +" order by merchantImageId desc";
				merchImageObj=merchHbm.getMerchantImageTblObjByQuery(qry,praSession);
					lvrInrJSONObj.put("mrchntId", Commonutility.toCheckNullEmpty(merchantTblObj.getMrchntId().getMrchntId()));
				if(merchImageObj!=null){
					lvrInrJSONObj.put("imageName", Commonutility.toCheckNullEmpty(merchImageObj.getImageName()));
					lvrInrJSONObj.put("imagePath", "/externalPath/offers/"+rb.getString("external.inner.mobilepath")+merchantTblObj.getMerchantUtilId()+"/"+merchImageObj.getDocDateFolderName()+"/"+Commonutility.toCheckNullEmpty(merchImageObj.getImageName()));
					lvrInrJSONObj.put("imagewebPath","/externalPath/offers/"+rb.getString("external.inner.webpath")+merchantTblObj.getMerchantUtilId()+"/"+merchImageObj.getDocDateFolderName()+"/"+Commonutility.toCheckNullEmpty(merchImageObj.getImageName()));
					
				}else{
					lvrInrJSONObj.put("imageName", "");
					lvrInrJSONObj.put("imagePath","");
					lvrInrJSONObj.put("imagewebPath","");
				}
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;
				merchImageObj=null;
			}
			
			
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("eventdetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select Merchant process End");
			logWrite.logMessage("Step 4 : Select Merchant process End", "info", LoadMerchantOfferTableData.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toMerchantselect() : "+expObj);
				logWrite.logMessage("Step -1 : Merchant select all block Exception : "+expObj, "error", LoadMerchantOfferTableData.class);	
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
