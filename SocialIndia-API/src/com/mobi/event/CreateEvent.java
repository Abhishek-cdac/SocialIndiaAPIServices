package com.mobi.event;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.FacilityMstTblVO;
import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public class CreateEvent extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	JsonpackDao jsonPack = new JsonpackDaoService();
	public String execute(){
		
		System.out.println("************mobile Create Event services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		JSONArray jsonarr = null;
		JSONArray userArray = null;
		boolean flg = true;
		String servicecode="";
		int retFeedId = 0;
		try{
			/*fileUploadFileName="Chrysanthemum.jpg";
			fileUpload=new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg");*/
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("ivrparams----------------"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String functionId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "function_id");
				String functionTemplateId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "function_template_id");
				String functionStartTime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "function_start_time");
				String functionEndTime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "function_end_time");
				//String facilityBookingId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "facility_booking_id");
				String locationAddr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "location-addr");
				String latitudeLongitude = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "location-lat_longt");
				String description = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "desc");
				String isRsvp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_need_rsvp");
				String eventDetailId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_id");
				String facilityId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "facility_id");
				String eventtype = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_type");
				String eventTitle=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_title");
				String feedId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "feed_id");
				jsonarr =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"remove_attach");
				String userprivacy=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "privacy");
				userArray =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");
				//8-SOCIETY EVENT, 9-PERSONAL EVENT, 12-comittee meeting
				//2-SOCIETY EVENT, 1-PERSONAL EVENT, 3-comittee meeting
				
				
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
			
			if(eventDetailId!=null && eventDetailId.length()>0){
				EventTblVO eventObj=new EventTblVO();
				FunctionMasterTblVO functionobj=new FunctionMasterTblVO();
				FunctionTemplateTblVO  functionTemplateobj=new FunctionTemplateTblVO();
				EventDao eventhbm=new EventDaoServices();
				String sql="";
				if(functionId!=null && functionId.length()>0){
				sql="from FunctionMasterTblVO where functionId="+functionId;
				functionobj=eventhbm.getfunctionMasterObjByQry(sql);
				}
				FacilityMstTblVO facilityobj=new FacilityMstTblVO();
			
				JSONObject functionStartTimeObj=commjvm.getdateAndTimeFromDateTime(functionStartTime);
				String stdate = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "date");
				String time = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "time");
				if(functionTemplateId!=null && functionTemplateId.length()>0){
				sql="from FunctionTemplateTblVO where functionTempId="+functionTemplateId;
				functionTemplateobj=eventhbm.getfunctionTemplateObjByQry(sql);
				}
				eventObj.setIvrBnFUNCTION_ID(functionobj);
				eventObj.setIvrBnEVENT_TITLE(eventTitle);
				if(facilityId!=null && !facilityId.equalsIgnoreCase("")){
					facilityobj.setFacilityId(Integer.parseInt(facilityId));
					eventObj.setFaciltyTemplateId(facilityobj);
				/*}else if(facilityBookingId!=null && !facilityBookingId.equalsIgnoreCase("")){
				eventObj.setFaciltyTemplateId(facilityobj);
				eventObj.setFaciltyBookingId(facilitybookingobd);*/
				}else{
					eventObj.setFaciltyBookingId(null);
				}
				eventObj.setIvrBnLAT_LONG(latitudeLongitude);
				if(functionTemplateobj!=null){
				eventObj.setIvrBnSHORT_DESC(functionTemplateobj.getTemplateText());
				eventObj.setFunctionTemplateId(functionTemplateobj);
				}
				eventObj.setIvrBnEVENT_DESC(description);
			
				if(userMst.getGroupCode()!=null){
				eventObj.setIvrBnEVENT_CRT_GROUP_ID(userMst.getGroupCode().getGroupCode());
				}
				if(jsonarr!=null && jsonarr.length()>0){
					eventObj.setIvrBnEVENT_FILE_NAME("");
					}
				if(fileUploadFileName!=null && fileUploadFileName.length()>0){
				eventObj.setIvrBnEVENT_FILE_NAME(fileUploadFileName);
				}
				
				eventObj.setIvrBnSTARTDATE(stdate);
				eventObj.setIvrBnSTARTTIME(time);
				functionStartTimeObj=null;
				functionStartTimeObj=commjvm.getdateAndTimeFromDateTime(functionEndTime);
				stdate = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "date");
				time = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "time");
				eventObj.setIvrBnENDDATE(stdate);
				eventObj.setIvrBnENDTIME(time);
				
				eventObj.setIvrBnEVENT_LOCATION(locationAddr);
				System.out.println("eventDetailId-----------------"+eventDetailId);
				eventObj.setIvrBnISRSVP(Integer.parseInt(isRsvp));
				eventObj.setIvrBnEVENTSTATUS(1);
				eventObj.setIvrBnEVENTT_TYPE(Integer.parseInt(eventtype));
				eventObj.setIvrBnEVENT_CRT_USR_ID(userMst);
				eventObj.setIvrBnEVENT_ID(Integer.parseInt(eventDetailId));
				System.out.println("isRsvp-----------------"+isRsvp);
				boolean isupd=eventhbm.updateEvent(eventObj);
				System.out.println("isupd-------------------"+isupd);
						if(isupd){
							try{
								
								
							EventTblVO updatedeventObj=new EventTblVO();
							String qry="from EventTblVO where ivrBnEVENT_ID='"+eventDetailId+"'";
							updatedeventObj=eventhbm.geteventobjectByQuery(qry);
							FeedsTblVO editfeedObj = new FeedsTblVO();
							FeedDAO  feadhbm=new FeedDAOService();
							if(feedId!=null && feedId.length()>0){
							editfeedObj=feadhbm.feedData(Integer.parseInt(feedId));
							if(editfeedObj!=null){
							FeedsTblVO feedObj = new FeedsTblVO();
							feedObj.setFeedId(Integer.parseInt(feedId));
							feedObj.setUsrId(userMst);
							int feedTypFlg = 0;
							if (eventtype.equalsIgnoreCase("1")) {  //personal event
								feedTypFlg = 9;
							} else if (eventtype.equalsIgnoreCase("2")) {  //society event
								feedTypFlg = 8;
							} else if (eventtype.equalsIgnoreCase("3")) {  //committee meeting
								feedTypFlg = 12;
							}
							feedObj.setFeedType(feedTypFlg);
							feedObj.setFeedTypeId(Integer.parseInt(eventDetailId));
							System.out.println("updatedeventObj.getIvrBnEVENT_TITLE()---------------"+updatedeventObj.getIvrBnEVENT_TITLE());
							feedObj.setFeedTitle(updatedeventObj.getIvrBnEVENT_TITLE());
							String tmpcont="";
							String eventdesc="";
							if(updatedeventObj.getFunctionTemplateId()!=null && updatedeventObj.getFunctionTemplateId().getTemplateText()!=null){
								tmpcont+=updatedeventObj.getFunctionTemplateId().getTemplateText();
							}
							if(updatedeventObj.getIvrBnEVENT_DESC()!=null){
								eventdesc=updatedeventObj.getIvrBnEVENT_DESC();
							}
							if(tmpcont!=null && tmpcont.length()>0){
								tmpcont+="<BR>"+eventdesc;
							}else{
								tmpcont+=eventdesc;
							}
							System.out.println("tmpcont------------"+tmpcont);
							if(updatedeventObj.getIvrBnFUNCTION_ID()!=null && updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName()!=null){
							feedObj.setFeedCategory(updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName());
							}
							feedObj.setFeedDesc(tmpcont);
							
							feedObj.setPostBy(Integer.parseInt(rid));
							if(userMst.getFirstName()!=null){
							feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
							}
							feedObj.setSocietyId(userMst.getSocietyId());
							System.out.println("userMst.getCityId()----------------"+userMst.getCityId());
							if(userMst.getCityId()!=null){
								feedObj.setFeedLocation(userMst.getCityId().getCityName());
								}
							
							CommonMobiDao comondaohbm=new CommonMobiDaoService();
							comondaohbm.getDefaultPrivacyFlg(Integer.parseInt(rid));
							
							feedObj.setOriginatorId(Integer.parseInt(rid));
							feedObj.setEntryBy(Integer.parseInt(rid));																												
							feedObj.setFeedStatus(1);
							feedObj.setFeedPrivacyFlg(1);							
							feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							feedObj.setIsMyfeed(1);
							feedObj.setIsShared(0);
							System.out.println("updating feed table");
							feadhbm.feedEditPrivacyflag(feedObj, "", null, null, null, null);
							
							
							FeedDAO feedHbm=new FeedDAOService();
							feedObj=feedHbm.feedData(Integer.parseInt(feedId));
							NotificationDao notificationHbm=new NotificationDaoServices();
							AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
							String additionaldata=additionalDatafunc.formAdditionalDataForFeedTbl(feedObj);
							System.out.println("additionaldata-------------------------"+additionaldata);
							notificationHbm.updateNotificationDetails(1,Integer.parseInt(eventDetailId),additionaldata );
							
							
							}
							}
							}catch (Exception ex){
								System.out.println("ex-------edit--------"+ex);
							}
							
							boolean additionUpdate = false;
							/*Additional data insert*/
							JSONObject jsonPublishObj = new JSONObject(); 
							ComplaintsDao complains=new ComplaintsDaoServices();
							jsonPublishObj = jsonPack.eventTableJasonpackValues(Integer.parseInt(eventDetailId));
							System.out.println("jsonPublishObj--------------"+jsonPublishObj.toString());
							if (jsonPublishObj != null) {
								additionUpdate = complains.additionalFeedUpdate(jsonPublishObj,Integer.parseInt(feedId));
							}
							
							System.out.println("fileUploadFileName--------------------"+fileUploadFileName);
							if(fileUploadFileName!=null && fileUploadFileName.length()>0){
							int lneedWidth=0,lneedHeight = 0;
							//String destPath =getText("external.path")+"PostData/";
							String destPath =getText("external.imagesfldr.path")+"event/web/"+eventDetailId;
							System.out.println("destPath----------"+destPath);
							commjvm.deleteallFileInDirectory(destPath);
							
							System.out.println("destPath----------"+destPath);
							 int imgHeight=mobiCommon.getImageHeight(fileUpload);
			        		   int imgwidth=mobiCommon.getImageWidth(fileUpload);
			        		   System.out.println("imgHeight------"+imgHeight);
			        		   System.out.println("imgwidth-----------"+imgwidth);
							 String imageHeightPro=getText("thump.img.height");
			        		   String imageWidthPro=getText("thump.img.width");
			        		   File destFile  = new File(destPath, fileUploadFileName);
			        		   
	   		       	    	FileUtils.copyFile(fileUpload, destFile);
	   		       	    	
			        		   if(imgHeight>Integer.parseInt(imageHeightPro)){
		        				   lneedHeight = Integer.parseInt(imageHeightPro);	
		    	        		 //mobile - small image
		        			   }else{
		        				   lneedHeight = imgHeight;	  
		        			   }
		        			   if(imgwidth>Integer.parseInt(imageWidthPro)){
		        				   lneedWidth = Integer.parseInt(imageWidthPro);	  
		        			   }else{
		        				   lneedWidth = imgwidth;
		        			   }
		        			   System.out.println("lneedHeight-----------"+lneedHeight);
		        			   System.out.println("lneedWidth-------------"+lneedWidth);
		        			String limgSourcePath=getText("external.imagesfldr.path")+"event/web/"+eventDetailId+"/"+fileUploadFileName;
	   		 		 		String limgDisPath = getText("external.imagesfldr.path")+"event/mobile/"+eventDetailId;
	   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
	   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);
	   		 		 	 	
		        			   String limageFor = "all";
	   		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
							}else{
								String destPath =getText("external.imagesfldr.path")+"event/web/"+eventDetailId;
								System.out.println("destPath----------"+destPath);
								commjvm.deleteallFileInDirectory(destPath);
							}
							
							if (additionUpdate) {
							FeedDAO  feadhbm=new FeedDAOService();
							String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
							String imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
							String imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
							String videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
							String videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
							List<Object[]> feedListObj = new ArrayList<Object[]>();
							feedListObj = feadhbm.feedDetailsProc(Commonutility.stringToInteger(rid), societykey, Integer.parseInt(feedId),"");								
							Object[] objList;
							for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
								objList = it.next();									
								if (objList != null) {
									locObjRspdataJson = jsonPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
									if (objList[0]!=null) {
										System.out.println((int)objList[0]);
									}
								}
							}
						
						} else {
							locObjRspdataJson=new JSONObject();
						}
							serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
						}else{
							locObjRspdataJson=new JSONObject();
							serverResponse(servicecode,"01","R0003",getText("R0003"),locObjRspdataJson);
						}
						
						
			}else{
			EventTblVO eventObj=new EventTblVO();
			FunctionMasterTblVO functionobj=new FunctionMasterTblVO();
			FunctionTemplateTblVO  functionTemplateobj=new FunctionTemplateTblVO();
			EventDao eventhbm=new EventDaoServices();
			String sql="from FunctionMasterTblVO where functionId="+functionId;
			functionobj=eventhbm.getfunctionMasterObjByQry(sql);
			FacilityMstTblVO facilityobj=new FacilityMstTblVO();
			
			sql="from FunctionTemplateTblVO where functionTempId="+functionTemplateId;
			functionTemplateobj=eventhbm.getfunctionTemplateObjByQry(sql);
			eventObj.setIvrBnFUNCTION_ID(functionobj);
			eventObj.setIvrBnEVENT_TITLE(eventTitle);
			if(facilityId!=null && !facilityId.equalsIgnoreCase("")){
				facilityobj.setFacilityId(Integer.parseInt(facilityId));
				eventObj.setFaciltyTemplateId(facilityobj);
			/*}else if(facilityBookingId!=null && !facilityBookingId.equalsIgnoreCase("")){
			eventObj.setFaciltyTemplateId(facilityobj);
			eventObj.setFaciltyBookingId(facilitybookingobd);*/
			}
			eventObj.setIvrBnSHORT_DESC(functionTemplateobj.getTemplateText());
			eventObj.setFunctionTemplateId(functionTemplateobj);
			eventObj.setIvrBnEVENT_DESC(description);
			eventObj.setIvrBnEVENTT_TYPE(Integer.parseInt(eventtype));
			if(userMst.getGroupCode()!=null){
			eventObj.setIvrBnEVENT_CRT_GROUP_ID(userMst.getGroupCode().getGroupCode());
			}
			if(fileUploadFileName!=null && !fileUploadFileName.equalsIgnoreCase("")){
			eventObj.setIvrBnEVENT_FILE_NAME(fileUploadFileName);
			}
			
			JSONObject functionStartTimeObj=commjvm.getdateAndTimeFromDateTime(functionStartTime);
			String stdate = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "date");
			String time = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "time");
			eventObj.setIvrBnSTARTDATE(stdate);
			eventObj.setIvrBnSTARTTIME(time);
			functionStartTimeObj=null;
			functionStartTimeObj=commjvm.getdateAndTimeFromDateTime(functionEndTime);
			stdate = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "date");
			time = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "time");
			eventObj.setIvrBnENDDATE(stdate);
			eventObj.setIvrBnENDTIME(time);
			eventObj.setIvrBnLAT_LONG(latitudeLongitude);
			eventObj.setIvrBnEVENT_LOCATION(locationAddr);
			System.out.println("isRsvp-----------------"+isRsvp);
			eventObj.setIvrBnISRSVP(Integer.parseInt(isRsvp));
			eventObj.setIvrBnEVENTSTATUS(1);
			eventObj.setIvrBnENTRY_DATETIME(dt);
			eventObj.setIvrBnEVENT_CRT_USR_ID(userMst);
			int eventId=eventhbm.saveCreateNewEvent(eventObj);
			System.out.println("eventId---------------"+eventId);
					if(eventId>0){
						try{
							// notification ennable later
							
					
							
						EventTblVO updatedeventObj=new EventTblVO();
						String qry="from EventTblVO where ivrBnEVENT_ID="+eventId;
						updatedeventObj=eventhbm.geteventobjectByQuery(qry);
						FeedDAO  feadhbm=new FeedDAOService();
						FeedsTblVO feedObj = new FeedsTblVO();
						feedObj.setUsrId(userMst);
						int feedTypFlg = 0;
						if (eventtype.equalsIgnoreCase("1")) {  //personal event
							feedTypFlg = 9;
						} else if (eventtype.equalsIgnoreCase("2")) {  //society event
							feedTypFlg = 8;
						} else if (eventtype.equalsIgnoreCase("3")) {  //committee meeting
							feedTypFlg = 12;
						}
						feedObj.setFeedType(feedTypFlg);
						feedObj.setFeedTypeId(eventId);
						if(updatedeventObj.getIvrBnEVENT_TITLE()!=null){
						feedObj.setFeedTitle(updatedeventObj.getIvrBnEVENT_TITLE());
						}
						String tmpcont="";
						String eventdesc="";
						if(updatedeventObj.getFunctionTemplateId()!=null && updatedeventObj.getFunctionTemplateId().getTemplateText()!=null){
							tmpcont+=updatedeventObj.getFunctionTemplateId().getTemplateText();
						}
						if(updatedeventObj.getIvrBnEVENT_DESC()!=null){
							eventdesc=updatedeventObj.getIvrBnEVENT_DESC();
						}
						System.out.println("eventdesc-----------"+eventdesc);
						if(tmpcont!=null && tmpcont.length()>0){
							tmpcont+="<BR>"+eventdesc;
						}else{
							tmpcont+=eventdesc;
						}
						if(updatedeventObj.getIvrBnFUNCTION_ID()!=null && updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName()!=null){
						feedObj.setFeedCategory(updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName());
						}
						feedObj.setFeedDesc(tmpcont);
						
						feedObj.setPostBy(Integer.parseInt(rid));
						
						feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
						feedObj.setSocietyId(userMst.getSocietyId());
						System.out.println("userMst.getFirstName()-----------------"+userMst.getFirstName());
						if(userMst.getCityId()!=null){
						feedObj.setFeedLocation(userMst.getCityId().getCityName());
						}
						
						feedObj.setOriginatorId(Integer.parseInt(rid));
						feedObj.setEntryBy(Integer.parseInt(rid));																												
						feedObj.setFeedStatus(1);
						feedObj.setFeedPrivacyFlg(1);							
						feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setIsMyfeed(1);
						feedObj.setIsShared(0);
						
						System.out.println("feed insert---------------"+userArray+"___");
						retFeedId = feadhbm.feedInsertPrivacyflag(null, feedObj, null, null, null,Integer.parseInt(userprivacy),userArray.toString());
						if(eventtype!=null && Integer.parseInt(eventtype)!=1){
							FeedDAO feedHbm=new FeedDAOService();
							feedObj=feedHbm.feedData(retFeedId);
							CommonMobiDao commonHbm=new CommonMobiDaoService();
							List<UserMasterTblVo> userobjList=new ArrayList<UserMasterTblVo>();
							NotificationDao notificationHbm=new NotificationDaoServices();
							String userQuery="from UserMasterTblVo where societyId.activationKey='"+societykey+"' and societyId.statusFlag=1 and   statusFlag=1 and groupCode.groupName= '"+getText("Grp.resident")+"'";
							userobjList=commonHbm.getUserRegisterDeatils(userQuery);
							UserMasterTblVo userMasteriterObj;
							AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
							String additionaldata=additionalDatafunc.formAdditionalDataForFeedTbl(feedObj);
							System.out.println("additionaldata-------------------------"+additionaldata);
							for(Iterator<UserMasterTblVo> it=userobjList.iterator();it.hasNext();){
								userMasteriterObj=it.next();
								System.out.println("userMasteriterObj.getUserId()-------------"+userMasteriterObj.getUserId());
								System.out.println("userMst.getUserId()------------"+userMst.getUserId());
								if(userMasteriterObj.getUserId()!=userMst.getUserId()){
								notificationHbm.insertnewNotificationDetails(userMasteriterObj, "", 1, 1, eventId, userMst, additionaldata);
								}
							}
							}
			
						
						}catch (Exception ex){
							System.out.println("ex---------------"+ex);
						}
						boolean additionUpdate = false;
						if (retFeedId != -1 && eventId>0) {
								ComplaintsDao complains=new ComplaintsDaoServices();
								/*Additional data insert*/
								JSONObject jsonPublishObj = new JSONObject(); 
								
								jsonPublishObj = jsonPack.eventTableJasonpackValues(eventId);
								System.out.println("jsonPublishObj--------------"+jsonPublishObj.toString());
								if (jsonPublishObj != null) {
									additionUpdate = complains.additionalFeedUpdate(jsonPublishObj,retFeedId);
								}
						}
						
						EventDispTblVO lvrEvDsiptblvoobj=new EventDispTblVO();
						UserMasterTblVo locUsobj = null;
						EventTblVO eventobj=new EventTblVO();
						eventobj.setIvrBnEVENT_ID(eventId);
						
						if(Commonutility.toCheckisNumeric(rid)){
							lvrEvDsiptblvoobj.setIvrBnENTRY_BY(null);
						}else{
							lvrEvDsiptblvoobj.setIvrBnENTRY_BY(Integer.parseInt(rid));
						}	
						lvrEvDsiptblvoobj.setIvrBnEVENTSTATUS(1);
						lvrEvDsiptblvoobj.setIvrBnEVENT_ID(eventobj);
				 
						locUsobj=new UserMasterTblVo();
						locUsobj.setUserId(Integer.parseInt(rid));
						lvrEvDsiptblvoobj.setIvrBnUAMMSTtbvoobj(locUsobj);
				 
							lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(userMst.getGroupCode());
							lvrEvDsiptblvoobj.setContributeAmount(0);
							lvrEvDsiptblvoobj.setRsvpFlag(0);
						
							eventhbm.toInsertEventDispTbl(lvrEvDsiptblvoobj);
						
						if(fileUploadFileName!=null && !fileUploadFileName.equalsIgnoreCase("")){
						int lneedWidth=0,lneedHeight = 0;
						//String destPath =getText("external.path")+"PostData/";
						String destPath =getText("external.imagesfldr.path")+"event/web/"+eventId;
						System.out.println("destPath----------"+destPath);
						 int imgHeight=mobiCommon.getImageHeight(fileUpload);
		        		   int imgwidth=mobiCommon.getImageWidth(fileUpload);
		        		   System.out.println("imgHeight------"+imgHeight);
		        		   System.out.println("imgwidth-----------"+imgwidth);
						 String imageHeightPro=getText("thump.img.height");
		        		   String imageWidthPro=getText("thump.img.width");
		        		   File destFile  = new File(destPath, fileUploadFileName);
		        		   
   		       	    	FileUtils.copyFile(fileUpload, destFile);
   		       	    	
		        		   if(imgHeight>Integer.parseInt(imageHeightPro)){
	        				   lneedHeight = Integer.parseInt(imageHeightPro);	
	    	        		 //mobile - small image
	        			   }else{
	        				   lneedHeight = imgHeight;	  
	        			   }
	        			   if(imgwidth>Integer.parseInt(imageWidthPro)){
	        				   lneedWidth = Integer.parseInt(imageWidthPro);	  
	        			   }else{
	        				   lneedWidth = imgwidth;
	        			   }
	        			   System.out.println("lneedHeight-----------"+lneedHeight);
	        			   System.out.println("lneedWidth-------------"+lneedWidth);
	        			String limgSourcePath=getText("external.imagesfldr.path")+"event/web/"+eventId+"/"+fileUploadFileName;
   		 		 		String limgDisPath = getText("external.imagesfldr.path")+"event/mobile/"+eventId;
   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);
   		 		 	 	
	        			   String limageFor = "all";
   		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
						}
   		        		
						if (additionUpdate) {
						FeedDAO  feadhbm=new FeedDAOService();
						String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
						String imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
						String imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
						String videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
						String videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
						List<Object[]> feedListObj = new ArrayList<Object[]>();
						feedListObj = feadhbm.feedDetailsProc(Commonutility.stringToInteger(rid), societykey, retFeedId,"");								
						Object[] objList;
						for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
							objList = it.next();									
							if (objList != null) {
								locObjRspdataJson = jsonPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
								if (objList[0]!=null) {
									System.out.println((int)objList[0]);
								}
							}
						}
					
					} else {
						locObjRspdataJson=new JSONObject();
					}
						serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);
					}
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", CreateEvent.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======Create Event====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", CreateEvent.class);
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


	public File getFileUpload() {
		return fileUpload;
	}


	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}


	public String getFileUploadContentType() {
		return fileUploadContentType;
	}


	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}


	public String getFileUploadFileName() {
		return fileUploadFileName;
	}


	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	
}