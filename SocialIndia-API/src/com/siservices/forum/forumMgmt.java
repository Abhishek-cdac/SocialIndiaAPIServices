package com.siservices.forum;

import java.io.PrintWriter;
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
import com.siservices.forumVo.MvpFourmTopicsTbl;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class forumMgmt extends ActionSupport {
  /**
   *sdsd.
   */
  private static final long serialVersionUID = 1L;
  private String ivrparams;
  forumDao forum=new forumServices();
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
        	    
        	    locObjRspdataJson=toEventselect(locObjRecvdataJson,locObjsession);		
    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
    				AuditTrial.toWriteAudit(getText("FUR0002"), "FUR0002", ivrCurrntusrid);
    				serverResponse("SI8006","0","E8006",getText("forum.selectall.error"),locObjRspdataJson);
    			}else{
    				AuditTrial.toWriteAudit(getText("FUR0001"), "FUR0001", ivrCurrntusrid);
    				serverResponse("SI8006","0","S8006",getText("forum.selectall.success"),locObjRspdataJson);					
    			}
    		}else {
    			locObjRspdataJson=new JSONObject();
    	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", forumMgmt.class);
    			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
    	    }			    	   
    	  }else {
          locObjRspdataJson=new JSONObject();
          logWrite.logMessage("Service code : SI8006,"+getText("request.format.notmach")+"", "info", forumMgmt.class);
		  serverResponse("SI8006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
    	  }
      }else {
    	locObjRspdataJson=new JSONObject();
    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", forumMgmt.class);
		serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
      }      
    } catch (Exception expObj) {      
      locObjRspdataJson=new JSONObject();
      logWrite.logMessage("Service code : SI8006, Sorry, an unhandled error occurred : "+expObj, "error", forumMgmt.class);
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
  private JSONObject toEventselect(JSONObject praDatajson, Session praSession){	
	JSONObject lvrRtnjsonobj = null;
	JSONObject lvrInrJSONObj = null;	
	JSONArray lvrEventjsonaryobj = null;
	Log logWrite = null;
	Common locCommonObj = null;
	//Date lvrEntrydate = null;
	Iterator lvrObjeventlstitr = null;
	MvpFourmTopicsTbl forumTopicsData = null;
	String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null;	
	int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
	String lvSlqry = null;
    try {
			logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	Commonutility.toWriteConsole("Step 1 : Select Forum process start.");
	    	logWrite.logMessage("Step 1 : Select Forum process start.", "info", forumMgmt.class);
	    	
	    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eventstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
			String srchdtsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
			String searchWord=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "searchWord");
			String townShipId = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "townShipId");
			String societyId = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyId");
			String groupCode =(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "groupCode");
			
			String locTochenull=Commonutility.toCheckNullEmpty(srchdtsrch);
			String glbSearch=null;
			String locOrderby =" order by entryDatetime desc";
			int societyid = 0;
			int groupcode = 0;
			if (Commonutility.toCheckisNumeric(societyId)) {
				societyid = Integer.parseInt(societyId);
			}
			if (Commonutility.toCheckisNumeric(groupCode)) {
				groupcode = Integer.parseInt(groupCode);
			}

		if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
			if((societyId!=null && !societyId.equalsIgnoreCase("") && societyid>0) && (groupCode!=null && !groupCode.equalsIgnoreCase(""))){
				if (groupcode == 4) {
					glbSearch = " and (" + " topicsName like ('%" + locTochenull + "%') and " 
				             	+ " userId.societyId =" + societyid + " "
				             	+ ")";
				} else {
					glbSearch = " and (" + " topicsName like ('%" + locTochenull + "%') and " 		             
							  + " userId.societyId =" + societyid + " "
							  //+ " groupCode.groupCode = " + groupcode + " "
							  + ")";
				}
			} else {
					glbSearch = " and (" + " topicsName like ('%" + locTochenull + "%') or " 
					          + " entryDatetime like ('%" + locTochenull + "%') or "
					          + " userId.firstName like ('%" + locTochenull + "%') or "
					          + " groupCode.groupName like ('%" + locTochenull + "%') "
					          + ")";		
			}
			lvSlqry = "from MvpFourmTopicsTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;						
			lvrevntcountqry = "select count(*) from MvpFourmTopicsTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
		} else if(searchWord!=null && !searchWord.equalsIgnoreCase("")  && !searchWord.equalsIgnoreCase("null")) {
		  if(societyId!=null && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")) {
			if (groupcode==1){				
				 glbSearch = " and (" + " topicsName like ('%" + searchWord + "%') or " 
				          + " entryDatetime like ('%" + searchWord + "%') or "
				          + " userId.firstName like ('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " "
				          + ")";
			} else if(groupcode==2){
				glbSearch = " and (" + " topicsName like ('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " " 
		             	+ ")";
			} else if(groupcode==4) {
				glbSearch = " and (" + " entryDatetime like('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " " 
		             	  + ")";
			} else if(groupcode==3) {
				glbSearch = " and (" + " userId.firstName like('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " " 
		             	+ ")";
			} else {
				glbSearch = " and (" + " topicsName like ('%" + searchWord + "%') and " 		             
						  + " userId.societyId = " + societyid + " "
						  + ")";
			}
		  } else {
			  glbSearch = "";
		  }
		    lvSlqry = "from MvpFourmTopicsTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
			lvrevntcountqry = "select count(*) from MvpFourmTopicsTbl  where status=" + locvrEventSTS + " " + glbSearch +" "+locOrderby;
		} else if (societyId!=null && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")) {
			glbSearch = " and (userId.societyId =" + societyid + "  " + ")";
			lvSlqry = "from MvpFourmTopicsTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
			lvrevntcountqry = "select count(*) from MvpFourmTopicsTbl  where status=" + locvrEventSTS + " " + glbSearch+" "+locOrderby;
		}
		/*else if(societyId!=null && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")) {	
			if (groupcode==1){				
				 glbSearch = " and (" + " topicsName like ('%" + searchWord + "%') or " 
				          + " entryDatetime like ('%" + searchWord + "%') or "
				          + " userId.firstName like ('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " "
				          + ")";
			} else if(groupcode==2){
				glbSearch = " and (" + " topicsName like ('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " " 
		             	+ ")";
			} else if(groupcode==4) {
				glbSearch = " and (" + " entryDatetime like('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " " 
		             	  + ")";
			} else if(groupcode==3) {
				glbSearch = " and (" + " userId.firstName like('%" + searchWord + "%')  and " 		             
						  + " userId.societyId = " + societyid + " " 
		             	+ ")";
			} else if(searchWord==null && searchWord.equalsIgnoreCase("") && societyId!=null && !societyId.equalsIgnoreCase("") &&  groupCode!=null && !groupCode.equalsIgnoreCase("")){				
				glbSearch = " and userId.societyId =" + societyid + "  "
			              + ")";
			} else{
				glbSearch = " and (" + " topicsName like ('%" + searchWord + "%') and " 		             
						  + " userId.societyId = " + societyid + " "
						  + ")";
			}
				lvSlqry = "from MvpFourmTopicsTbl  where status=" + locvrEventSTS + "" + glbSearch;	
				lvrevntcountqry = "select count(*) from MvpFourmTopicsTbl  where status=" + locvrEventSTS + "" + glbSearch;
		} */
		else {
			lvSlqry = "from MvpFourmTopicsTbl  where status=" + locvrEventSTS + ""+locOrderby;	
			lvrevntcountqry = "select count(*) from MvpFourmTopicsTbl  where status=" + locvrEventSTS+""+locOrderby;
		}
		if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
			Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry);
			logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry, "info", forumMgmt.class);
			EventDao lvrEvntDobj = new EventDaoservice();
			count = lvrEvntDobj.getInitTotal(lvrevntcountqry);
			countFilter = count;
		}else {
			count=0;countFilter=0;
			Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
			logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", forumMgmt.class);
		}
		if (Commonutility.toCheckisNumeric(locStrRow)) {
			 startrowfrom=Integer.parseInt(locStrRow);
		}
		if (Commonutility.toCheckisNumeric(locMaxrow)) {
			totalNorow=Integer.parseInt(locMaxrow);
		}
						
		Commonutility.toWriteConsole("Step 3 : Forum Details Query : "+lvSlqry);
		logWrite.logMessage("Step 3 : Forum Details Query : "+lvSlqry, "info", forumMgmt.class);
		lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
		lvrEventjsonaryobj = new JSONArray();
		while ( lvrObjeventlstitr.hasNext() ) {
			lvrInrJSONObj=new JSONObject();
			forumTopicsData = (MvpFourmTopicsTbl) lvrObjeventlstitr.next();
			lvrInrJSONObj.put("topicsid", Commonutility.toCheckNullEmpty(forumTopicsData.getTopicsId()));
			if(forumTopicsData.getTopicsName()!=null){
			lvrInrJSONObj.put("topicsname", Commonutility.toCheckNullEmpty(forumTopicsData.getTopicsName()));
			}else{
				lvrInrJSONObj.put("topicsname", "");
			}if(forumTopicsData.getUserId().getFirstName()!=null){
			lvrInrJSONObj.put("firstname", Commonutility.toCheckNullEmpty(forumTopicsData.getUserId().getFirstName()));
			}else{
				lvrInrJSONObj.put("firstname", "");
			}if(forumTopicsData.getUserId().getLastName()!=null){
				lvrInrJSONObj.put("lastname", Commonutility.toCheckNullEmpty(forumTopicsData.getUserId().getLastName()));
			}else{
				lvrInrJSONObj.put("lastname", "");
			}if(forumTopicsData.getUserId().getMobileNo()!=null){
				lvrInrJSONObj.put("mobileno", Commonutility.toCheckNullEmpty(forumTopicsData.getUserId().getMobileNo()));	
			}else{
				lvrInrJSONObj.put("mobileno", "");
			}
			if(forumTopicsData.getGroupCode()!=null){
			lvrInrJSONObj.put("groupname", Commonutility.toCheckNullEmpty(forumTopicsData.getGroupCode().getGroupName()));
			}else{
				lvrInrJSONObj.put("groupname", "");
			}
			lvrInrJSONObj.put("userid", Commonutility.toCheckNullEmpty(forumTopicsData.getUserId().getUserId()));
			lvrInrJSONObj.put("createby", Commonutility.toCheckNullEmpty(forumTopicsData.getEntryBy()));
			//lvrInrJSONObj.put("entrydate", Commonutility.toCheckNullEmpty(forumTopicsData.getEntryDatetime()));
			lvrInrJSONObj.put("entrydate", locCommonObj.getDateobjtoFomatDateStr(forumTopicsData.getEntryDatetime(), "dd-MM-yyyy HH:mm:ss"));
			int topicscount=forum.gettopicsCount(forumTopicsData.getTopicsId());			
			lvrInrJSONObj.put("topicscounts", Commonutility.toCheckNullEmpty(topicscount));
			
			lvrEventjsonaryobj.put(lvrInrJSONObj);
			lvrInrJSONObj = null;
		}
		
		
		lvrRtnjsonobj=new JSONObject();	
		lvrRtnjsonobj.put("InitCount", count);
		lvrRtnjsonobj.put("countAfterFilter", countFilter);
		lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
		lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
		lvrRtnjsonobj.put("forumdetails", lvrEventjsonaryobj);
		Commonutility.toWriteConsole("Step 4 : Select forum process End");
		logWrite.logMessage("Step 4 : Select forum process End", "info", forumMgmt.class);
    return lvrRtnjsonobj;
    }catch(Exception expObj) {
    	try {
			Commonutility.toWriteConsole("Exception in toEventselect() : "+expObj);
			logWrite.logMessage("Step -1 : Event select all block Exception : "+expObj, "error", forumMgmt.class);	
			lvrRtnjsonobj=new JSONObject();
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("labordetails", "");
			lvrRtnjsonobj.put("CatchBlock", "Error");
			Commonutility.toWriteConsole("lvrRtnjsonobj : "+lvrRtnjsonobj);
			}catch(Exception ee){}finally{}
     return lvrRtnjsonobj;
    }finally {
    	lvrRtnjsonobj = null;
    	lvrInrJSONObj = null;	
    	lvrEventjsonaryobj = null;
    	logWrite = null;    	
    	//lvrEntrydate = null;
    	lvrObjeventlstitr = null;
    	forumTopicsData = null;
    	lvrevntcountqry = null;locvrEventSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;locCommonObj=null;
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
