package com.pack.Notificationlist;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.Notificationlistvo.persistance.NotificationDao;
import com.pack.Notificationlistvo.persistance.NotificationDaoservice;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.NotificationTblVO;

public class Notificationlistviewall extends ActionSupport {

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
		Session locObjsession = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		try{
			locObjsession = HibernateUtil.getSession();
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
				locObjRspdataJson=tonotificationlistSelectAll(locObjRecvdataJson,locObjsession);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("NOTFYAD014"), "NOTFYAD014", ivrCurrntusrid);
						serverResponse("SI25000","0","E25000",getText("notification.selectall.error"),locObjRspdataJson);
					}else{
						//AuditTrial.toWriteAudit(getText("NOTFYAD013"), "NOTFYAD013", ivrCurrntusrid);
						serverResponse("SI25000","0","S25000",getText("notification.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI25000, "+getText("request.format.notmach"), "info", Notificationlistviewall.class);
					serverResponse("SI25000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI25000, "+getText("request.values.empty"), "info", Notificationlistviewall.class);
				serverResponse("SI25000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI25000, "+getText("catch.error"), "error", Notificationlistviewall.class);
			serverResponse("SI25000","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject tonotificationlistSelectAll(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		Date entrydatetime=null;	
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrNotify_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		NotificationTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;	
		CommonUtils locCommutillObj =null;
		try {
			locCommutillObj = new CommonUtilsDao();
			log=new Log();	lbrDtlVoObj=new NotificationTblVO();	
			DateFormat time = new SimpleDateFormat("hh:mm");
			 DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
			Commonutility.toWriteConsole("Step 1 : notification Type  select all block enter");
			log.logMessage("Step 1 : notification Type  select all block enter", "info", Notificationlistviewall.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				if (loctocheNull.equalsIgnoreCase("")) {
					locvrNotify_cntQry="select count(notificationId) from NotificationTblVO where statusFlag=1 and tblrefFlag not in(8,10) ";
				} else {
					locvrNotify_cntQry="select count(notificationId) from NotificationTblVO where statusFlag=1 AND tblrefFlag not in(8,10) and (" + "descr like ('%" + loctocheNull+ "%')  " 
			                                + ")";
				}
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry, "info", Notificationlistviewall.class);
				
				NotificationDao IdcardDaoObj=new NotificationDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrNotify_cntQry);
				countFilter=IdcardDaoObj.getTotalFilter(locvrNotify_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Notificationlistviewall.class);
			}
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
				String globalsearch=null;
				String lvOrderby = "";
				if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "readStatus like ('%" + loctocheNull+ "%') or " 
			                                + "descr like ('%" + loctocheNull + "%'))";
				    lvOrderby = " order by entryDatetime desc";
				  	loc_slQry="from NotificationTblVO  where statusFlag=1  and tblrefFlag not in(8,10) " +globalsearch + lvOrderby;
				}else{
					lvOrderby = " order by entryDatetime desc";
					loc_slQry="from NotificationTblVO  where statusFlag=1  and tblrefFlag not in(8,10) " +lvOrderby;	
				}				
			//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_STS+"";	
			Commonutility.toWriteConsole("Step 3 : notification Type  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : notification Type  Details Query : "+loc_slQry, "info", Notificationlistviewall.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();								
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (NotificationTblVO) locObjLbrlst_itr.next();								
				locInrJSONObj.put("notification_id",lbrDtlVoObj.getNotificationId());	
				locInrJSONObj.put("notification_name",lbrDtlVoObj.getDescr());
				locInrJSONObj.put("notification_status",lbrDtlVoObj.getReadStatus());	
				Date currentdate= locCommutillObj.getCurrentDateTime("dd-MM-yyyy HH:mm:ss");
				if (currentdate.compareTo(lbrDtlVoObj.getEntryDatetime()) < 0) {
					entrydatetime = lbrDtlVoObj.getEntryDatetime();
					locInrJSONObj.put("notification_time", date.format(entrydatetime));
				} else if (lbrDtlVoObj.getEntryDatetime()
						.compareTo(currentdate) == 0) {// both date are same
					entrydatetime = lbrDtlVoObj.getEntryDatetime();
					locInrJSONObj.put("notification_time", time.format(entrydatetime));
				} else {// expired
					entrydatetime = lbrDtlVoObj.getEntryDatetime();
					locInrJSONObj.put("notification_time", date.format(entrydatetime));
				}
				if(lbrDtlVoObj.getTblrefFlag()!=null){
					if (lbrDtlVoObj.getTblrefFlag() == 0) {
						locInrJSONObj.put("notification_tblrefname","Complaints");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else if (lbrDtlVoObj.getTblrefFlag() == 1) {
						locInrJSONObj.put("notification_tblrefname","Events");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else if (lbrDtlVoObj.getTblrefFlag() == 2) {
						locInrJSONObj.put("notification_tblrefname","Feedbacks");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else if (lbrDtlVoObj.getTblrefFlag() == 3) {
						locInrJSONObj.put("notification_tblrefname","Documents");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else if (lbrDtlVoObj.getTblrefFlag() == 4) {
						locInrJSONObj.put("notification_tblrefname","Forum");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else if (lbrDtlVoObj.getTblrefFlag() == 5) {
						locInrJSONObj.put("notification_tblrefname","Facility");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else if (lbrDtlVoObj.getTblrefFlag() == 6) {
						locInrJSONObj.put("notification_tblrefname","Tender");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else if (lbrDtlVoObj.getTblrefFlag() == 7) {
						locInrJSONObj.put("notification_tblrefname","Expenses");
						locInrJSONObj.put("notification_tblrefid",String.valueOf(lbrDtlVoObj.getTblrefID()));
					} else {
						locInrJSONObj.put("notification_tblrefname","");
						locInrJSONObj.put("notification_tblrefid","");	
					}
				} else {
					locInrJSONObj.put("notification_tblrefname","");
					locInrJSONObj.put("notification_tblrefid","");	
				}
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			Commonutility.toWriteConsole("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("notificationdetails", locLBRJSONAryobj);
			Commonutility.toWriteConsole("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", Notificationlistviewall.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ Thread.currentThread().getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			log.logMessage("Step -1 : Exception found in : "+Thread.currentThread().getClass().getSimpleName()+".class : "+e, "error", Notificationlistviewall.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("notificationdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;

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
