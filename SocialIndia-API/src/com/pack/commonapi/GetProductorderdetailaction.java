package com.pack.commonapi;

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
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.event.Eventviewall;
import com.pack.resident.ResidentUtility;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantProductItemsTblVO;

public class GetProductorderdetailaction  extends ActionSupport {
	  /**
	   *sdsd.
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
	        	    
	        	    locObjRspdataJson=toGetproductorderselect(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				//AuditTrial.toWriteAudit(getText("EVEAD012"), "EVEAD012", ivrCurrntusrid);
	    				serverResponse("SI39000","0","E39000",getText("productorder.selectall.error"),locObjRspdataJson);
	    			}else{
	    			//	AuditTrial.toWriteAudit(getText("EVEAD011"), "EVEAD011", ivrCurrntusrid);
	    				serverResponse("SI39000","0","S39000",getText("order.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI39000,"+getText("request.values.empty")+"", "info", Eventviewall.class);
	    			serverResponse("SI39000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI39000,"+getText("request.format.notmach")+"", "info", Eventviewall.class);
			  serverResponse("SI39000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI39000,"+getText("request.values.empty")+"", "info", Eventviewall.class);
			serverResponse("SI39000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI39000, Sorry, an unhandled error occurred : "+expObj, "error", Eventviewall.class);
		  serverResponse("SI39000","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	
	
	public static JSONObject toGetproductorderselect(JSONObject pDataJson,Session praSession ){ 
	    String lvrorderid = null;
		String lvrSlqry = null;
		Query lvrQrybj = null;
		Log log = null;
		Date lvrEntrydate=null;
		MerchantProductItemsTblVO locordertblobj = null;
		MerchantProductItemsTblVO locordertblobj1 = null;
		FlatMasterTblVO locflattblobj1=null;
		JSONObject locRtndatajson = null;
		Session locHbsession = null;
		Common locCommonObj = null;
		Iterator locObjorderlst_itr=null,locObjflatlst_itr=null;
		JSONArray locLBRSklJSONAryobj=null,locFLATJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null;
		JSONObject locInrFLATJSONObj=null;
		try {
			log = new Log();
			locHbsession = HibernateUtil.getSession();
			locCommonObj = new CommonDao();
			
			log.logMessage("Step 1 : Select order detail block enter", "info", GetProductorderdetailaction.class);
			locRtndatajson =  new JSONObject();
			lvrorderid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "orderid");
			System.out.println("Step 1 : order detail block enter   "+lvrorderid);
			lvrSlqry = "from MerchantProductItemsTblVO where orderId = "+Integer.parseInt(lvrorderid);
			lvrQrybj = locHbsession.createQuery(lvrSlqry);
			locordertblobj = (MerchantProductItemsTblVO)lvrQrybj.setMaxResults(1).uniqueResult();
			System.out.println("Step 2 : Select order detail query executed.");
			locRtndatajson.put("orderid",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderId()));
			locRtndatajson.put("restname",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getFirstName()));
			locRtndatajson.put("restlname",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getLastName()));
			locRtndatajson.put("restmobno",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getMobileNo()));
			locRtndatajson.put("restblockname",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getAddress1()));
			locRtndatajson.put("restflatno",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getAddress2()));
			locRtndatajson.put("restadd1",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getAddress1()));
			locRtndatajson.put("restadd2",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getAddress2()));
			locRtndatajson.put("mrchcomments",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getSupplyComments()));
			if(locordertblobj.getOrderId().getOrderBy().getCountryId()!=null){
			locRtndatajson.put("restcountry",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getCountryId().getCountryName()));
			locRtndatajson.put("reststate",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getStateId().getStateName()));
			locRtndatajson.put("restcity",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getCityId().getCityName()));
//			locRtndatajson.put("restpincode",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getPstlId().getPstlCode()));
			locRtndatajson.put("restpincode",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderBy().getPstlId()));
			}
			else
			{
				locRtndatajson.put("restcountry","");
				locRtndatajson.put("reststate","");
				locRtndatajson.put("restcity","");
				locRtndatajson.put("restpincode","");
			}
			
			locRtndatajson.put("restcomments",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderComments()));
			String loc_slQry_order="from MerchantProductItemsTblVO where orderId="+lvrorderid+"";
			locObjorderlst_itr=praSession.createQuery(loc_slQry_order).list().iterator();	
			System.out.println("Step 3 : Select resident order wings detail query executed.");
			locLBRSklJSONAryobj=new JSONArray();
			while (locObjorderlst_itr!=null &&  locObjorderlst_itr.hasNext() ) {
				locordertblobj1=(MerchantProductItemsTblVO)locObjorderlst_itr.next();
				locInrLbrSklJSONObj=new JSONObject();
				if(locordertblobj1.getOrderId()!=null){
					locInrLbrSklJSONObj.put("orderfeatures", locordertblobj1.getItemName());
					locInrLbrSklJSONObj.put("orderqty", locordertblobj1.getOrderQty());
					locInrLbrSklJSONObj.put("supplyqty", locordertblobj1.getSupplyQty());
					locInrLbrSklJSONObj.put("itemstatus", locordertblobj1.getStatus());
					locInrLbrSklJSONObj.put("commentid", locordertblobj1.getCommentId());
				}				
				
				locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
			}
			
			
			locRtndatajson.put("jArry_prdt_order", locLBRSklJSONAryobj);
			
			log.logMessage("Step 3: Select resident orderandwingsname detail block end.", "info", ResidentUtility.class);
			String loc_slQry_order1="from FlatMasterTblVO where usrid="+locordertblobj.getOrderId().getOrderBy().getUserId()+"";
			locObjflatlst_itr=praSession.createQuery(loc_slQry_order1).list().iterator();	
			System.out.println("Step 3 : Select resident order wings detail query executed.");
			locFLATJSONAryobj=new JSONArray();
			while (locObjflatlst_itr!=null &&  locObjflatlst_itr.hasNext() ) {
				locflattblobj1=(FlatMasterTblVO)locObjflatlst_itr.next();
				locInrFLATJSONObj=new JSONObject();
				if(locflattblobj1.getFlat_id()!=null){
					locInrFLATJSONObj.put("flatnumber", locflattblobj1.getFlatno());
					locInrFLATJSONObj.put("blackname", locflattblobj1.getWingsname());
					
				}				
				
				locFLATJSONAryobj.put(locInrFLATJSONObj);
			}
			locRtndatajson.put("jArry_flat_details", locFLATJSONAryobj);
			/*locRtndatajson.put("ordernoofperson",Commonutility.toCheckNullEmpty(locordertblobj.getCommentId()));*/
			
			locRtndatajson.put("acceptstatus",Commonutility.toCheckNullEmpty(locordertblobj.getOrderId().getOrderAcceptStatus()));
			System.out.println("Step 3 : order details are put into json.");
			System.out.println("Step 4 : order Block end.");
			return locRtndatajson;
		}catch(Exception e) {
			try{
				System.out.println("Step -1 : Select order detail Exception found in orderutility.toseletordersingle : "+e);
				log.logMessage("Exception : "+e, "error", GetProductorderdetailaction.class);
				locRtndatajson=new JSONObject();
				locRtndatajson.put("catch", "Error");
				}catch(Exception ee){}
				return locRtndatajson;
		}finally {
				if(locHbsession!=null){locHbsession.close();locHbsession=null;}
				 lvrorderid = null; lvrSlqry = null; lvrQrybj = null;
				 log = null; lvrEntrydate=null; locordertblobj = null; locRtndatajson = null;
				 locflattblobj1=null;
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


