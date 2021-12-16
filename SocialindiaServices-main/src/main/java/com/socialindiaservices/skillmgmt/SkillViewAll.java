package com.socialindiaservices.skillmgmt;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class SkillViewAll extends ActionSupport{
	/**
	 * 
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
	        	    
	        	    locObjRspdataJson=toSkillselect(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("SKILL012"), "SKILL012", ivrCurrntusrid);
	    				serverResponse("SI380001","0","E8006",getText("Skill.selectall.error"),locObjRspdataJson);
	    			}else{
	    				//AuditTrial.toWriteAudit(getText("SKILL011"), "SKILL011", ivrCurrntusrid);
	    				serverResponse("SI380001","0","S310001",getText("Skill.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    			AuditTrial.toWriteAudit(getText("SKILL012"), "SKILL012", ivrCurrntusrid);
	    	    	logWrite.logMessage("Service code : SI310001,"+getText("request.values.empty")+"", "info", SkillViewAll.class);
	    			serverResponse("SI380001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          AuditTrial.toWriteAudit(getText("SKILL0018"), "SKILL0018", ivrCurrntusrid);
	          logWrite.logMessage("Service code : SI310001,"+getText("request.format.notmach")+"", "info", SkillViewAll.class);
			  serverResponse("SI380001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	AuditTrial.toWriteAudit(getText("SKILL0017"), "SKILL0017", ivrCurrntusrid);
	    	logWrite.logMessage("Service code : SI310001,"+getText("request.values.empty")+"", "info", SkillViewAll.class);
			serverResponse("SI380001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      AuditTrial.toWriteAudit(getText("SKILL012"), "SKILL012", ivrCurrntusrid);
	      logWrite.logMessage("Service code : SI310001, Sorry, an unhandled error occurred : "+expObj, "error", SkillViewAll.class);
		  serverResponse("SI380001","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
	private JSONObject toSkillselect(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Iterator lvrObjeventlstitr = null;
		SkillMasterTblVO lvrSkillvoobj = null;
		String lvrevntcountqry = null,locvrskillSTS = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchflg=null,societyid=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String glbSearch="";
		String locOrderby =" order by entryDatetime desc";
	    try {
	    	logWrite = new Log();
	    	System.out.println("Step 1 : Select Skill process start.");
	    	logWrite.logMessage("Step 1 : Select Skill process start.", "info", SkillViewAll.class);
	    	
	    	locvrskillSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "status");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
			societyid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
				glbSearch = "(" + " ivrBnSKILL_NAME like ('%" + locTochenull + "%') or" 
			             + " ivrBnCategoryid.iVOCATEGORY_NAME like ('%" + locTochenull + "%')"
			             + ")"; 		
			}
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			if(locTochenull==null || locTochenull.equalsIgnoreCase("") || locTochenull.equalsIgnoreCase("null") ) {
				lvSlqry = "from SkillMasterTblVO "+locOrderby;
				lvrevntcountqry="select count(ivrBnSKILL_ID) from SkillMasterTblVO";
			}else{
				lvSlqry = "from SkillMasterTblVO where "+glbSearch +" "+locOrderby;
				lvrevntcountqry="select count(ivrBnSKILL_ID) from SkillMasterTblVO where "+glbSearch;
			}
			Common lvrcommDobj = new CommonDao();
			System.out.println("Step 3 : Skill Details Query : "+lvSlqry);
			count = lvrcommDobj.gettotalcount(lvrevntcountqry);
			countFilter = count;
			logWrite.logMessage("Step 3 : Skill Details Query : "+lvSlqry, "info", SkillViewAll.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				lvrSkillvoobj = (SkillMasterTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("skillId", Commonutility.toCheckNullEmpty(lvrSkillvoobj.getIvrBnSKILL_ID()));
				lvrInrJSONObj.put("skillName", Commonutility.toCheckNullEmpty(lvrSkillvoobj.getIvrBnSKILL_NAME()));
				lvrInrJSONObj.put("categoryId", Commonutility.toCheckNullEmpty(lvrSkillvoobj.getIvrBnCategoryid().getiVOCATEGORY_ID()));
				lvrInrJSONObj.put("categoryName",  Commonutility.toCheckNullEmpty(lvrSkillvoobj.getIvrBnCategoryid().getiVOCATEGORY_NAME()));
				lvrInrJSONObj.put("status", Commonutility.toCheckNullEmpty(lvrSkillvoobj.getIvrBnACT_STAT()));
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;
			}
			lvrRtnjsonobj = new JSONObject();
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("skilldetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select SKILL process End "+ lvrRtnjsonobj);
			logWrite.logMessage("Step 4 : Select SKILL process End", "info",SkillViewAll.class);
			return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toFunctionselect() : "+expObj);
				logWrite.logMessage("Step -1 : Function select all block Exception : "+expObj, "error", SkillViewAll.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("InitCount", count);
				lvrRtnjsonobj.put("countAfterFilter", countFilter);
				lvrRtnjsonobj.put("skilldetails", "");
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
			return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	lvrEventjsonaryobj = null;
	    	logWrite = null;
	    	lvrObjeventlstitr = null;
	    	lvrSkillvoobj = null;
	    	lvrevntcountqry = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
	    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
	    	lvSlqry = null;societyid=null;
	    }
	  }
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
		PrintWriter out = null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response = null;
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
			try {
				out = response.getWriter();
				out.print("{\"servicecode\":\"" + serviceCode + "\",");
				out.print("{\"statuscode\":\"2\",");
				out.print("{\"respcode\":\"E0002\",");
				out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
				out.print("{\"data\":\"{}\"}");
				out.close();
				ex.printStackTrace();
			} catch (Exception e) {
			} finally {
			}
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}


}
