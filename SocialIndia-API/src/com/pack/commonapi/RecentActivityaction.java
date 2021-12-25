package com.pack.commonapi;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.Notificationlistvo.persistance.NotificationDao;
import com.pack.Notificationlistvo.persistance.NotificationDaoservice;
import com.pack.audittrialvo.AuditlogTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class RecentActivityaction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		//Session locObjsession = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		try{
			//locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");														
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
					ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
				}else{
					ivrCurrntusrid=0;
				}
				locObjRspdataJson=torecentactivitylistalert(locObjRecvdataJson);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						//AuditTrial.toWriteAudit(getText("NOTFYAD014"), "NOTFYAD014", ivrCurrntusrid);
						serverResponse("SI34002","0","E34002",getText("recentactivity.selectall.error"),locObjRspdataJson);
					}else{
					//	AuditTrial.toWriteAudit(getText("NOTFYAD013"), "NOTFYAD013", ivrCurrntusrid);
						serverResponse("SI34002","0","S34002",getText("recentactivity.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI34002, "+getText("request.format.notmach"), "info", RecentActivityaction.class);
					serverResponse("SI34002","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI34002, "+getText("request.values.empty"), "info", RecentActivityaction.class);
				serverResponse("SI34002","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI34002, "+getText("catch.error"), "error", RecentActivityaction.class);
			serverResponse("SI34002","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject torecentactivitylistalert(JSONObject pDataJson) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_Usrid=null,locvrCntflg=null,locvrFlterflg=null,locvrNotify_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;
		Date entrydatetime=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		AuditlogTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;		
		CommonUtils locCommutillObj =null;
		Session locObjsession = null;
		Query query = null;
		Common lvrComobj = null;
		try {
			lvrComobj = new CommonDao();
			locObjsession = HibernateUtil.getSession();
			log=new Log();	lbrDtlVoObj=new AuditlogTblVO();		
			locCommutillObj = new CommonUtilsDao();
			DateFormat time = new SimpleDateFormat("hh:mm");
			 DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			Commonutility.toWriteConsole("Step 1 : recentactivity Type  select all block enter");
			log.logMessage("Step 1 : recentactivity Type  select all block enter", "info", RecentActivityaction.class);			
			locvrLBR_Usrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				if(loctocheNull.equalsIgnoreCase(""))
				{
					locvrNotify_cntQry="select count(ivrBnAUDIT_ID) from AuditlogTblVO ";
				}
				else
				{
					locvrNotify_cntQry="select count(ivrBnAUDIT_ID) from AuditlogTblVO " 
			                                + ")";
				}
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry, "info", RecentActivityaction.class);
				
				NotificationDao IdcardDaoObj=new NotificationDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrNotify_cntQry);
				countFilter=IdcardDaoObj.getTotalFilter(locvrNotify_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", RecentActivityaction.class);
			}
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				String locOrderby =" order by ivrBnENTRY_DATETIME desc";
				String globalsearch=null;
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "readStatus like ('%" + loctocheNull+ "%') or " 
			                                + "descr like ('%" + loctocheNull + "%'))";
				  loc_slQry="from AuditlogTblVO  "+locOrderby;
			}else{
				loc_slQry="from AuditlogTblVO "+locOrderby;	
			}				
			//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_Usrid+"";	
			Commonutility.toWriteConsole("Step 3 : recentactivity Type  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : recentactivity Type  Details Query : "+loc_slQry, "info", RecentActivityaction.class);
			query = locObjsession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow);
			if(query!=null){			
				locObjLbrlst_itr = query.list().iterator();
			}				
			locLBRJSONAryobj=new JSONArray();
			while (locObjLbrlst_itr!=null && locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (AuditlogTblVO) locObjLbrlst_itr.next();	
				 Date lvrMdatetime = lbrDtlVoObj.getIvrBnENTRY_DATETIME();
				locInrJSONObj.put("recentactivity_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnAUDIT_ID()));	
				locInrJSONObj.put("recentactivity_name",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnOPERATIONS()));
				locInrJSONObj.put("recentactivity_entry",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnENTRY_BY()));
				locInrJSONObj.put("recentactivity_entrydate",Commonutility.toCheckNullEmpty(lvrComobj.getDateobjtoFomatDateStr(lvrMdatetime, "dd-MM-yyyy HH:mm:ss")));
				
					
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			Commonutility.toWriteConsole("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("recentactdetails", locLBRJSONAryobj);			
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			Commonutility.toWriteConsole("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", RecentActivityaction.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", RecentActivityaction.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("recentactdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			if(locObjsession!=null){
				locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession =null;
			}
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
			locvrLBR_Usrid=null;locCommutillObj=null;
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
