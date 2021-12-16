package com.pack.Notificationlist;

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
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.NotificationTblVO;

public class Notificationlistalert extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute(){
		Log log= null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		//Session locObjsession = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		try{
			log= new Log();
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
				locObjRspdataJson=tonotificationlistalert(locObjRecvdataJson);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						//AuditTrial.toWriteAudit(getText("NOTFYAD014"), "NOTFYAD014", ivrCurrntusrid);
						serverResponse("SI25003","0","E25003",getText("notification.selectall.error"),locObjRspdataJson);
					}else{
					//	AuditTrial.toWriteAudit(getText("NOTFYAD013"), "NOTFYAD013", ivrCurrntusrid);
						serverResponse("SI25003","0","S25003",getText("notification.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI25003, "+getText("request.format.notmach"), "info", Notificationlistalert.class);
					serverResponse("SI25003","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI25003, "+getText("request.values.empty"), "info", Notificationlistalert.class);
				serverResponse("SI25003","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI25003, "+getText("catch.error"), "error", Notificationlistalert.class);
			serverResponse("SI25003","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject tonotificationlistalert(JSONObject pDataJson) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_Usrid=null,locvrCntflg=null,locvrFlterflg=null,locvrNotify_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;
		Date entrydatetime=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		NotificationTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;		
		CommonUtils locCommutillObj =null;
		Session locObjsession = null;
		Query query = null;
		try {
			locObjsession = HibernateUtil.getSession();
			log = new Log();	lbrDtlVoObj=new NotificationTblVO();		
			locCommutillObj = new CommonUtilsDao();
			DateFormat time = new SimpleDateFormat("HH:mm");
			 DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
			Commonutility.toWriteConsole("Step 1 : notification Type  select all block enter");
			log.logMessage("Step 1 : notification Type  select all block enter", "info", Notificationlistalert.class);			
			locvrLBR_Usrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {// for web 
				if (loctocheNull.equalsIgnoreCase("")) {
					locvrNotify_cntQry="select count(notificationId) from NotificationTblVO where readStatus =1 and tblrefFlag not in(8,10) and userId ='"+locvrLBR_Usrid+"'";
				} else {
					locvrNotify_cntQry="select count(notificationId) from NotificationTblVO where readStatus =1 and tblrefFlag not in(8,10) AND (" + "descr like ('%" + loctocheNull+ "%') and userId ='"+locvrLBR_Usrid+"' " + ")";
				}
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry, "info", Notificationlistalert.class);
				
				NotificationDao IdcardDaoObj=new NotificationDaoservice();
				count = IdcardDaoObj.getInitTotal(locvrNotify_cntQry);
				countFilter =count;
			} else { // for mobile
				count = 0;
				countFilter = 0;
				 Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Notificationlistalert.class);
			}
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
				String globalsearch=null, lvrOrderby = "order by notificationId desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "readStatus like ('%" + loctocheNull+ "%') or "  + "descr like ('%" + loctocheNull + "%'))";
				  loc_slQry="from NotificationTblVO  where readStatus =1 and userId ='"+locvrLBR_Usrid+"' and tblrefFlag not in(8,10) " +globalsearch + lvrOrderby + "";
			}else{
				loc_slQry="from NotificationTblVO  where readStatus =1 and userId ='"+locvrLBR_Usrid+"' and tblrefFlag not in(8,10) " + lvrOrderby + "";	
			}				
			//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_Usrid+"";	
			Commonutility.toWriteConsole("Step 3 : notification Type  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : notification Type  Details Query : "+loc_slQry, "info", Notificationlistalert.class);
			query = locObjsession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow);
			if(query!=null){			
				locObjLbrlst_itr = query.list().iterator();
			}				
			locLBRJSONAryobj=new JSONArray();
			while (locObjLbrlst_itr!=null && locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (NotificationTblVO) locObjLbrlst_itr.next();								
				locInrJSONObj.put("notification_id",lbrDtlVoObj.getNotificationId());	
				locInrJSONObj.put("notification_name",lbrDtlVoObj.getDescr());
				locInrJSONObj.put("notification_status",lbrDtlVoObj.getReadStatus());				
				//Date currentdate = locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				Date currentdate = locCommutillObj.getCurrentDateTime("dd-MM-yyyy HH:mm:ss");
				if(currentdate.compareTo(lbrDtlVoObj.getEntryDatetime()) < 0) {
					entrydatetime =lbrDtlVoObj.getEntryDatetime();
		        	locInrJSONObj.put("notification_time",date.format(entrydatetime));	
				} else if(lbrDtlVoObj.getEntryDatetime().compareTo(currentdate)==0){// both date are same
					entrydatetime =lbrDtlVoObj.getEntryDatetime();
					locInrJSONObj.put("notification_time",time.format(entrydatetime));	
				}else{//expired
					entrydatetime =lbrDtlVoObj.getEntryDatetime();
					locInrJSONObj.put("notification_time",date.format(entrydatetime));	
				}
				if (lbrDtlVoObj.getTblrefFlag()!=null) {
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
			locFinalRTNObj.put("notifyalertdetails", locLBRJSONAryobj);			
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			Commonutility.toWriteConsole("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", Notificationlistalert.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
				Commonutility.toWriteConsole("Step -1 : Exception found in Notificationlistalert.class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
				log.logMessage("Step -1 : Exception found in Notificationlistalert.class in method of "+Thread.currentThread().getStackTrace()[1].getMethodName()+"() : "+e, "error", Notificationlistalert.class);	
				locFinalRTNObj=new JSONObject();
				locFinalRTNObj.put("InitCount", count);
				locFinalRTNObj.put("countAfterFilter", countFilter);
				locFinalRTNObj.put("notifyalertdetails", "");
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
