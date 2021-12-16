package com.socialindiaservices.facility;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FacilityViewall extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	public String execute(){
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
		        	    
		        	    locObjRspdataJson=toFacilityselect(locObjRecvdataJson,locObjsession);		
		    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
		    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
		    				AuditTrial.toWriteAudit(getText("FACILITY012"), "FACILITY012", ivrCurrntusrid);
		    				serverResponse("SI360001","0","E360001",getText("function.selectall.error"),locObjRspdataJson);
		    			}else{
		    			//	AuditTrial.toWriteAudit(getText("FACILITY011"), "FACILITY011", ivrCurrntusrid);
		    				serverResponse("SI360001","0","S360001",getText("function.selectall.success"),locObjRspdataJson);					
		    			}
		    		}else {
		    			locObjRspdataJson=new JSONObject();
		    			AuditTrial.toWriteAudit(getText("FACILITY011"), "FACILITY011", ivrCurrntusrid);
		    	    	logWrite.logMessage("Service code : SI360001,"+getText("request.values.empty")+"", "info", FacilityViewall.class);
		    			serverResponse("SI360001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		    	    }			    	   
		    	  }else {
		          locObjRspdataJson=new JSONObject();
		          AuditTrial.toWriteAudit(getText("FACILITY009"), "FACILITY009", ivrCurrntusrid);
		          logWrite.logMessage("Service code : SI360001,"+getText("request.format.notmach")+"", "info", FacilityViewall.class);
				  serverResponse("SI360001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
		    	  }
		      }else {
		    	locObjRspdataJson=new JSONObject();
		    	AuditTrial.toWriteAudit(getText("FACILITY010"), "FACILITY010", ivrCurrntusrid);
		    	logWrite.logMessage("Service code : SI360001,"+getText("request.values.empty")+"", "info", FacilityViewall.class);
				serverResponse("SI360001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
		      }   
		 }catch(Exception ex){
			 locObjRspdataJson=new JSONObject();
			 AuditTrial.toWriteAudit(getText("FACILITY015"), "FACILITY015", ivrCurrntusrid);
		      logWrite.logMessage("Service code : SI360001, Sorry, an unhandled error occurred : "+ex, "error", FacilityViewall.class);
			  serverResponse("SI360001","2","ER0002",getText("catch.error"),locObjRspdataJson);
		 }finally {
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
	private JSONObject toFacilityselect(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		String lvrevntcountqry = null,locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,societyid=null,locVrSlQry="";
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String glbSearch="";
		List<Object[]> nwListObj = null;
		societyMgmtDao society=null;
		String activeKey=null;
	    try {
	    	society=new societyMgmtDaoServices();
	    	nwListObj = new ArrayList<Object[]>();
	    	logWrite = new Log();
	    	System.out.println("Step 1 : Select Facility process start.");
	    	logWrite.logMessage("Step 1 : Select Facility process start.", "info", FacilityViewall.class);
	    	
	    	locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			if(societyid!=null && !societyid.equalsIgnoreCase("null") && !societyid.equalsIgnoreCase("-1")){
				locVrSlQry="SELECT activationKey FROM SocietyMstTbl where societyId="+societyid+" and statusFlag = 1";
				activeKey= society.getactiveKey(locVrSlQry);
			}
		/*	Common lvrfaclityDobj = new CommonDao();*/
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
				if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {					
					glbSearch = " and (" + "  FMT.Facility_id like ('%" + locTochenull + "%') or " 
				             + " FMT.FACILITY_NAME like ('%" + locTochenull + "%') or "	
				              + " FMT.PLACE like ('%" + locTochenull + "%') or "	
				               + " SMT.SOCIETY_NAME like ('%" + locTochenull + "%') or "	
				                + " TMT.TOWNSHIP_NAME like ('%" + locTochenull + "%') "	
				             + ")";	
					
				}else{
					glbSearch = " and (" + "  FMT.Facility_id like ('%" + locTochenull + "%') or " 
				             + " FMT.FACILITY_NAME like ('%" + locTochenull + "%') or "	
				              + " FMT.PLACE like ('%" + locTochenull + "%') or "	
				               + " SMT.SOCIETY_NAME like ('%" + locTochenull + "%') or "	
				                + " TMT.TOWNSHIP_NAME like ('%" + locTochenull + "%') "	
				             + ")";	
				
				}			
			}else {
				if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {
				/*	glbSearch = " and (" + "  FMT.Facility_id like ('%" + locTochenull + "%') or " 
				             + " FMT.FACILITY_NAME like ('%" + locTochenull + "%') or "	
				              + " FMT.PLACE like ('%" + locTochenull + "%') or "	
				               + " SMT.SOCIETY_NAME like ('%" + locTochenull + "%') or "	
				                + " TMT.TOWNSHIP_NAME like ('%" + locTochenull + "%') "	
				             + ")";	*/
					
				}else{
					glbSearch = " and (" + "  FMT.Facility_id like ('%" + locTochenull + "%') or " 
				             + " FMT.FACILITY_NAME like ('%" + locTochenull + "%') or "	
				              + " FMT.PLACE like ('%" + locTochenull + "%') or "	
				               + " SMT.SOCIETY_NAME like ('%" + locTochenull + "%') or "	
				                + " TMT.TOWNSHIP_NAME like ('%" + locTochenull + "%') "	
				             + ")";	
					
				}		
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			
			String lvrOrderby = " order by FMT.Facility_id desc";
			if(societyid==null || societyid.equalsIgnoreCase("") || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")) {
				lvSlqry = "select FMT.Facility_id,FMT.FACILITY_NAME,FMT.PLACE,FMT.DESCRIPTION,FMT.FACILITY_IMG,FMT.ACT_STAT,FMT.SOCIETY_KEY,  SMT.SOCIETY_ID,SMT.TOWNSHIP_ID,SMT.SOCIETY_NAME,TMT.TOWNSHIP_NAME  FROM  facility_mst_tbl FMT  INNER JOIN  society_mst_tbl SMT ON  FMT.SOCIETY_KEY=SMT.ACTIVATION_KEY  INNER JOIN  township_mst_tbl TMT ON  TMT.TOWNSHIP_ID=SMT.TOWNSHIP_ID "+glbSearch+" " + lvrOrderby;
				lvrevntcountqry ="select count(*) as cntt FROM  facility_mst_tbl FMT  INNER JOIN  society_mst_tbl SMT ON  FMT.SOCIETY_KEY=SMT.ACTIVATION_KEY  INNER JOIN  township_mst_tbl TMT ON  TMT.TOWNSHIP_ID=SMT.TOWNSHIP_ID "+glbSearch+"";;
			}else{
				lvSlqry = "select FMT.Facility_id,FMT.FACILITY_NAME,FMT.PLACE,FMT.DESCRIPTION,FMT.FACILITY_IMG,FMT.ACT_STAT,FMT.SOCIETY_KEY,  SMT.SOCIETY_ID,SMT.TOWNSHIP_ID,SMT.SOCIETY_NAME,TMT.TOWNSHIP_NAME  FROM  facility_mst_tbl FMT  INNER JOIN  society_mst_tbl SMT ON  FMT.SOCIETY_KEY=SMT.ACTIVATION_KEY  INNER JOIN  township_mst_tbl TMT ON  TMT.TOWNSHIP_ID=SMT.TOWNSHIP_ID where FMT.society_key='"+activeKey+"' "+glbSearch+" " + lvrOrderby;
				lvrevntcountqry ="select count(*) as cntt FROM  facility_mst_tbl FMT  INNER JOIN  society_mst_tbl SMT ON  FMT.SOCIETY_KEY=SMT.ACTIVATION_KEY  INNER JOIN  township_mst_tbl TMT ON  TMT.TOWNSHIP_ID=SMT.TOWNSHIP_ID where FMT.society_key='"+activeKey+"' "+glbSearch+"";
			}
		
			Transaction tx = null;
			Query queryObjs = praSession.createSQLQuery(lvrevntcountqry);
			System.out.println("Step 3 : Facility Details Query : "+lvSlqry);
			tx = praSession.beginTransaction();
			Object obj = queryObjs.uniqueResult();
			String ss=String.valueOf(obj);
			count=Integer.parseInt(ss);
			System.out.println("count ----------------------- "+count);
			countFilter = count;
			Query queryObj = praSession.createSQLQuery(lvSlqry)
					.addScalar("FMT.Facility_id",Hibernate.TEXT)
					.addScalar("FMT.FACILITY_NAME", Hibernate.TEXT)
					.addScalar("FMT.PLACE", Hibernate.TEXT)
					.addScalar("FMT.DESCRIPTION", Hibernate.TEXT)
					.addScalar("FMT.FACILITY_IMG", Hibernate.TEXT)
					.addScalar("FMT.ACT_STAT", Hibernate.TEXT)
					.addScalar("FMT.SOCIETY_KEY", Hibernate.TEXT)
					.addScalar("SMT.SOCIETY_ID", Hibernate.TEXT)
					.addScalar("SMT.TOWNSHIP_ID", Hibernate.TEXT)
					.addScalar("SMT.SOCIETY_NAME", Hibernate.TEXT)
					.addScalar("TMT.TOWNSHIP_NAME", Hibernate.TEXT);
				nwListObj = queryObj.setFirstResult(startrowfrom).setMaxResults(totalNorow).list();
				Object[] objList;
				lvrEventjsonaryobj = new JSONArray();
				for(Iterator<Object[]> it=nwListObj.iterator();it.hasNext();) {
					objList = it.next();
					JSONObject jsonPack = new JSONObject();
					String uidPath="";
					if (objList[0] != null) {
						jsonPack.put("facid",Commonutility.stringToStringempty((String)objList[0]));
					} else {
						jsonPack.put("facid","");
					}				
					if (objList[1] != null) {
						jsonPack.put("facname",Commonutility.stringToStringempty((String)objList[1]));					
					} else {
						jsonPack.put("facname","");
					}
					if (objList[2] != null) {
						jsonPack.put("fascplace",Commonutility.stringToStringempty((String)objList[2]));					
					} else {
						jsonPack.put("fascplace","");
					}
					if (objList[3] != null) {					
						jsonPack.put("facdesc",Commonutility.stringToStringempty((String)objList[3]));
					} else {
						jsonPack.put("facdesc","");
					}	
					if (objList[4] != null) {
						jsonPack.put("facimg",Commonutility.stringToStringempty((String)objList[4]));
						jsonPack.put("imagePath", rb.getString("external.templogo") + rb.getString("external.inner.facilityfdr") + rb.getString("external.inner.webpath") + (String) objList[0] + "/" + Commonutility.toCheckNullEmpty((String)objList[4]));
						jsonPack.put("imagewebPath", rb.getString("external.templogo") + rb.getString("external.inner.facilityfdr") + rb.getString("external.inner.webpath") + (String) objList[0] + "/" + Commonutility.toCheckNullEmpty((String)objList[4]));
					} else {
						jsonPack.put("facimg", "");
						jsonPack.put("imagePath", "");
						jsonPack.put("imagewebPath","");
					}
					if (objList[5] != null) {					
						jsonPack.put("status",Commonutility.stringToStringempty((String)objList[5]));
					} else {
						jsonPack.put("status","");
					}
					if (objList[6] != null) {					
						jsonPack.put("activeKey",Commonutility.stringToStringempty((String)objList[6]));
					} else {
						jsonPack.put("activeKey","");
					}
					if (objList[7] != null) {					
						jsonPack.put("socityId",Commonutility.stringToStringempty((String)objList[7]));
					} else {
						jsonPack.put("socityId","");
					}
					if (objList[8] != null) {					
						jsonPack.put("townshipId",Commonutility.stringToStringempty((String)objList[8]));
					} else {
						jsonPack.put("townshipId","");
					}
					if (objList[9] != null) {					
						jsonPack.put("socityName",Commonutility.stringToStringempty((String)objList[9]));
					} else {
						jsonPack.put("socityName","");
					}
					if (objList[7] != null) {					
						jsonPack.put("townshipName",Commonutility.stringToStringempty((String)objList[10]));
					} else {
						jsonPack.put("townshipName","");
					}
					lvrEventjsonaryobj.put(jsonPack);
				}
				
				
				
			logWrite.logMessage("Step 3 : Facility Details Query : "+lvSlqry, "info", FacilityViewall.class);			
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", Commonutility.toCheckNullEmpty(count));
			lvrRtnjsonobj.put("countAfterFilter", Commonutility.toCheckNullEmpty(countFilter));
			lvrRtnjsonobj.put("rowstart", Commonutility.toCheckNullEmpty(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", Commonutility.toCheckNullEmpty(totalNorow));
			lvrRtnjsonobj.put("facilitydetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select Facility process End " +lvrRtnjsonobj);
			logWrite.logMessage("Step 4 : Select Facility process End", "info", FacilityViewall.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toFacilityselect() : "+expObj);
				logWrite.logMessage("Step -1 : Facility select all block Exception : "+expObj, "error", FacilityViewall.class);	
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
	    	lvrEventjsonaryobj = null;
	    	logWrite = null;
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
