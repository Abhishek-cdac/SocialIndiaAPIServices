package com.mobi.event;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class EventSearchList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	
	public String execute(){
		System.out.println("************mobile otp services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String eventSearch = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_search");
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String eventType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_type");
				
				if (Commonutility.checkempty(servicecode)) {
					if (servicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
						
					} else {
						String[] passData = { getText("service.code.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
				}
				if (Commonutility.checkempty(townshipKey)) {
					if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
						
					} else {
						String[] passData = { getText("townshipid.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
				}
				if (Commonutility.checkempty(societykey)) {
					if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
						
					} else {
						String[] passData = { getText("society.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
				}
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				
				if(flg){
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int count=0;String locVrSlQry="";
			Date dt=new Date();
			int societyId=userMst.getSocietyId().getSocietyId();
			EventDao eventhbm=new EventDaoServices();
			List<Object[]> eventListObj = new ArrayList<Object[]>();
			String searchField="";
			int feedTypFlg = 0;
			if(eventSearch!=null && eventSearch.length()>0){
				searchField=" and (EVENT.EVENT_TITLE like ('%"+eventSearch+"%') or EVENT.SHORT_DESC like ('%"+eventSearch+"%') or EVENT.EVENT_DESC like ('%"+eventSearch+"%')) ";
			}
			if(eventType!=null && eventType.length()>0){
				searchField+=" and EVENT.EVENT_TYPE='"+eventType+"' ";
				
				if (eventType.equalsIgnoreCase("1")) {  //personal event
					feedTypFlg = 9;
				} else if (eventType.equalsIgnoreCase("2")) {  //society event
					feedTypFlg = 8;
				} else if (eventType.equalsIgnoreCase("3")) {  //committee meeting
					feedTypFlg = 12;
				}
			}
			String query="SELECT EVENT.EVENT_ID AS event_id, EVENT.FUNCTION_ID AS function_id,FUNC.FUNCTION_NAME AS function_name,EVENT.FUNCTION_TEMPLATE_ID AS template_id "
					+ ",EVENT.SHORT_DESC AS template_msg,EVENT.EVENT_DESC AS descp,"
					+ "(SELECT COUNT(USR_ID) FROM mvp_event_displayto_tbl WHERE RESPONSE_RSVP=1 AND EVENT_ID=EVENT.EVENT_ID) AS attending,"
					+ "USR.FNAME AS INVITER,EVENT.EVENT_CRT_USR_ID AS INVITER_ID,USR.IMAGE_NAME_MOBILE AS INVITER_IMG,"
					+ "EVENT.BOOKING_ID AS facility_booking_id,EVENT.EVENT_LOCATION AS location,FCTLY.PLACE AS location_details ,"
					+ "EVENT.STARTDATE AS date,EVENT.STARTTIME AS starttime,EVENT.ENDTIME AS endtime,EVENT.EVENT_FILE_NAME,ifnull(EVENT.isrsvp,'0') AS is_rsvp,"
					+ "(SELECT IFNULL(SUM(CASE WHEN RESPONSE_RSVP=1 THEN 1 ELSE 0 END),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVENT.EVENT_ID ) AS will_attend,"
					+ "(SELECT IFNULL(SUM(CASE WHEN RESPONSE_RSVP=2 THEN 1 ELSE 0 END),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVENT.EVENT_ID) AS willnot_attend,"
					+ "(SELECT IFNULL(SUM(CASE WHEN RESPONSE_RSVP=3 THEN 1 ELSE 0 END),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVENT.EVENT_ID) AS maybe_attend,"
					+ "(SELECT IFNULL(SUM(ifnull(CONTRIBUTE_AMOUNT,0)),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVENT.EVENT_ID) AS contribute_amt,"
					+ "EVENT.EVENT_LOCATION,EVENT.LAT_LONG,ENDDATE ,USR.SOCIAL_PICTURE as socialimage,EVENT.EVENT_TITLE AS event_title,EVENT.FACILITY_ID AS facility_id "
					+ "FROM mvp_event_tbl EVENT LEFT JOIN USR_REG_TBL USR "
					+ "ON EVENT.EVENT_CRT_USR_ID=USR.USR_ID "
					+ "LEFT JOIN facility_mst_tbl FCTLY ON "
					+ "EVENT.FACILITY_ID=FCTLY.FACILITY_ID "
					+ "LEFT JOIN mvp_function_tbl FUNC ON "
					+ "EVENT.FUNCTION_ID=FUNC.FUNCTION_ID "
					+ "WHERE EVENT.EVENT_CRT_USR_ID='"+rid+"' and EVENT.STATUS=1 "+searchField+" GROUP BY EVENT.EVENT_ID order by EVENT.MODIFY_DATETIME desc";
			
			eventListObj=eventhbm.getEventContributionSearchList(query, startlimit);
			
			List<Object[]> eventListObj1 = new ArrayList<Object[]>();
		/*	query="SELECT EVNT_DISP.EVENT_ID AS event_id, EVENT.FUNCTION_ID AS function_id,EVENT.EVENT_TITLE AS function_name,EVENT.FUNCTION_TEMPLATE_ID AS template_id "
					+ ",EVENT.SHORT_DESC AS template_msg,EVENT.EVENT_DESC AS descp,"
					+ "(SELECT COUNT(USR_ID) FROM mvp_event_displayto_tbl WHERE RESPONSE_RSVP=1 AND EVENT_ID=EVNT_DISP.EVENT_ID) AS attending,"
					+ "USR.USERNAME AS INVITER,EVENT.EVENT_CRT_USR_ID AS INVITER_ID,USR.IMAGE_NAME_MOBILE AS INVITER_IMG,"
					+ "EVENT.FACILITY_ID AS facility_id,FCTLY.FACILITY_NAME AS location,FCTLY.PLACE AS location_details ,"
					+ "EVENT.STARTDATE AS date,EVENT.STARTTIME AS starttime,EVENT.ENDTIME AS endtime,EVENT.EVENT_FILE_NAME,ifnull(EVENT.isrsvp,'0') AS is_rsvp,"
					+ "(SELECT IFNULL(SUM(CASE WHEN RESPONSE_RSVP=1 THEN 1 ELSE 0 END),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVNT_DISP.EVENT_ID ) AS will_attend,"
					+ "(SELECT IFNULL(SUM(CASE WHEN RESPONSE_RSVP=2 THEN 1 ELSE 0 END),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVNT_DISP.EVENT_ID) AS willnot_attend,"
					+ "(SELECT IFNULL(SUM(CASE WHEN RESPONSE_RSVP=3 THEN 1 ELSE 0 END),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVNT_DISP.EVENT_ID) AS maybe_attend,"
					+ "(SELECT IFNULL(SUM(ifnull(CONTRIBUTE_AMOUNT,0)),0) FROM mvp_event_displayto_tbl WHERE EVENT_ID=EVNT_DISP.EVENT_ID) AS contribute_amt,"
					+ "EVENT.EVENT_LOCATION,EVENT.LAT_LONG "
					+ " FROM mvp_event_tbl EVENT LEFT JOIN "
					+ "mvp_event_displayto_tbl EVNT_DISP "
					+ "ON  EVENT.EVENT_ID=EVNT_DISP.EVENT_ID "
					+ "LEFT JOIN USR_REG_TBL USR ON EVENT.EVENT_CRT_USR_ID=USR.USR_ID LEFT JOIN facility_mst_tbl FCTLY ON "
					+ "EVENT.FACILITY_ID=FCTLY.FACILITY_ID WHERE EVNT_DISP.USR_ID='"+rid+"' and EVENT.STATUS=1 GROUP BY EVNT_DISP.EVENT_ID";*/
			
			//eventListObj1=eventhbm.getEventContributionSearchList(query, startlimit);
			
			//if(eventListObj!=null &&  eventListObj1!=null  && (eventListObj.size()>0 || eventListObj1.size()>0)){
				if(eventListObj!=null &&  eventListObj1!=null  && (eventListObj.size()>0 )){
				Object[] objList;
				int merid=0;
				JSONArray eventcontributionarray=new JSONArray();
				JSONObject evntobj=new JSONObject();
				int totalcnt=0;
				//totalcnt=eventListObj.size()+eventListObj1.size();
				totalcnt=eventListObj.size();
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				FeedDAO feedHbm=new FeedDAOService();
				for(Iterator<Object[]> it=eventListObj.iterator();it.hasNext();) {
					objList = it.next();
					evntobj=new JSONObject();
					Integer eventId= (Integer)objList[0];
					String functionId= (String)objList[1];
					System.out.println("eventId-------"+eventId);
					
					evntobj.put("event_id", ""+eventId);
					FeedsTblVO feedobj=new FeedsTblVO();
					feedobj=feedHbm.getFeedDetailsByEventId(eventId,feedTypFlg);
					if(feedobj!=null){
					evntobj.put("feed_id", feedobj.getFeedId());
					}else{
						evntobj.put("feed_id", "");
					}
					
					if(functionId!=null){
					evntobj.put("function_id",functionId);
					}else{
						evntobj.put("function_id","");
					}
					evntobj.put("function_name", (String)objList[2]);
					evntobj.put("event_title", (String)objList[26]);
					String facilityId=(String)objList[27];
					if(facilityId!=null){
					evntobj.put("facility_id", facilityId);
					}else{
						evntobj.put("facility_id", "");
					}
					if((String)objList[3]!=null){
					evntobj.put("template_id", (String)objList[3]);
					}else{
						evntobj.put("template_id","");
					}
					evntobj.put("template_msg", (String)objList[4]);
					evntobj.put("desc", (String)objList[5]);
					evntobj.put("attending", (String)objList[6]);
					String inviter=(String)objList[7];
					if(inviter!=null){
					evntobj.put("inviter", (String)objList[7]);
					}else{
						evntobj.put("inviter","");
					}
					evntobj.put("inviter_id", (String)objList[8]);
					
					if((String)objList[9]!=null){
						String externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+rid+"/"+(String)objList[9];
					evntobj.put("inviter_img", externalUserImagePath);
					}else if((String)objList[25]!=null){
					evntobj.put("inviter_img", (String)objList[25]);
					}else{
						evntobj.put("inviter_img", "");
					}
					if((String)objList[10]!=null){
					evntobj.put("facility_booking_id", (String)objList[10]);
					}else{
						evntobj.put("facility_booking_id","");
					}
					if((String)objList[11]!=null){
					evntobj.put("location", (String)objList[11]);
					}else{
						evntobj.put("location", "");
					}
					JSONObject locdetails=new JSONObject();
					if((String)objList[22]!=null){
						locdetails.put("address",(String)objList[22]);
					}else{
						locdetails.put("address","");
					}
					if((String)objList[23]!=null){
						locdetails.put("lat_long",(String)objList[23]);
					}else{
						locdetails.put("lat_long","");
					}
					if((String)objList[13]!=null && (String)objList[14]!=null){
						String startDt=(String)objList[13]+" "+(String)objList[14];
						evntobj.put("starttime",commjvm.dbEventToMobiledate(startDt));
					}else{
						evntobj.put("starttime","");
					}
					String endDt="";
					if((String)objList[24]!=null && (String)objList[15]!=null){
						 endDt=(String)objList[24]+" "+(String)objList[15];
						evntobj.put("endtime",commjvm.dbEventToMobiledate(endDt));
					}else{
						evntobj.put("endtime",endDt);
					}
				
					
					String isCloed=commjvm.dbEventGreaterThanCurrDate(endDt);
					evntobj.put("is_closed",isCloed);
					evntobj.put("location_details", locdetails);
					String enddt=(String)objList[15];
					System.out.println("enddt==================="+enddt);
					JSONArray imagearray=new JSONArray();
					if((String)objList[16]!=null){
						JSONObject imageObj=new JSONObject();
						String eventpath=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/"+getText("external.inner.eventfldr")+"/mobile/"+eventId+"/"+(String)objList[16];
						imageObj.put("img_id", ""+eventId);
						imageObj.put("img_url", eventpath);
						imagearray.put(imageObj);
					}
					
					evntobj.put("images",imagearray);
					if((String)objList[17]!=null){
					evntobj.put("is_rsvp",(String)objList[17]);
					}else{
						evntobj.put("is_rsvp","0");
					}
					evntobj.put("will_attend",(String)objList[18]);
					evntobj.put("willnot_attend",(String)objList[19]);
					evntobj.put("maybe_attend",(String)objList[20]);
					evntobj.put("contribute_amt",(String)objList[21]);
					
					
					
				eventcontributionarray.put(evntobj);
				}
				
				/*for(Iterator<Object[]> it=eventListObj1.iterator();it.hasNext();) {
					objList = it.next();
					evntobj=new JSONObject();
					Integer eventId= (Integer)objList[0];
					String functionId= (String)objList[1];
					System.out.println("eventId-------"+eventId);
					
					evntobj.put("event_id", ""+eventId);
					if(functionId!=null){
					evntobj.put("function_id",functionId);
					}else{
						evntobj.put("function_id","");
					}
					evntobj.put("function_name", (String)objList[2]);
					evntobj.put("template_id", (String)objList[3]);
					evntobj.put("template_msg", (String)objList[4]);
					evntobj.put("desc", (String)objList[5]);
					evntobj.put("attending", (String)objList[6]);
					evntobj.put("inviter", (String)objList[7]);
					evntobj.put("inviter_id", (String)objList[8]);
					if((String)objList[9]!=null){
						String externalUserImagePath = getText("project.image.url.mobile")+"/templogo/profile/mobile/"+rid+"/"+(String)objList[9];
					evntobj.put("inviter_img", externalUserImagePath);
					}
					if((String)objList[10]!=null){
					evntobj.put("facility_id", (String)objList[10]);
					}else{
						evntobj.put("facility_id","");
					}
					evntobj.put("location", (String)objList[11]);
					JSONObject locdetails=new JSONObject();
					if((String)objList[22]!=null){
						locdetails.put("address",(String)objList[22]);
					}else{
						locdetails.put("address","");
					}
					if((String)objList[23]!=null){
						locdetails.put("lat_long",(String)objList[23]);
					}else{
						locdetails.put("lat_long","");
					}
					evntobj.put("location_details", locdetails);
					evntobj.put("date",(String)objList[13]);
					evntobj.put("starttime",(String)objList[14]);
					evntobj.put("endtime",(String)objList[15]);
					if((String)objList[16]!=null){
						String eventpath=getText("project.image.url.mobile")+"/templogo/"+getText("external.inner.eventfldr")+"/mobile/"+eventId+"/"+(String)objList[16];
					evntobj.put("attachimg",eventpath);
					}else{
						evntobj.put("attachimg","");
					}
					evntobj.put("is_rsvp",(String)objList[17]);
					evntobj.put("will_attend",(String)objList[18]);
					evntobj.put("willnot_attend",(String)objList[19]);
					evntobj.put("maybe_attend",(String)objList[20]);
					evntobj.put("contribute_amt",(String)objList[21]);
					
				eventcontributionarray.put(evntobj);
				}*/
				
				
				
				System.out.println("eventcontributionarray----------------"+eventcontributionarray.toString());
				
				locObjRspdataJson.put("events", eventcontributionarray);
				locObjRspdataJson.put("totalrecords",totalcnt);
				locObjRspdataJson.put("timestamp",locTimeStamp);
				serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);
				}
			
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",getText("R0029"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",getText("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======EventSearchList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson);
		}
		
		return SUCCESS;
	}

	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
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