package com.mobi.common;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.event.EventDao;
import com.mobi.event.EventDaoServices;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.networkUserListVO.MvpNetworkTbl;
import com.mobi.networkUserListVO.networkDao;
import com.mobi.networkUserListVO.networkDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.FacilityBookingTblVO;

public class AdditionalDataDaoServices implements AdditionalDataDao{
	Log log=new Log();

	@Override
	public String formAdditionalDataForFacilityBookingTbl(
			FacilityBookingTblVO facitityObj) {
		// TODO Auto-generated method stub
		String additionalData="";
		try{
			
			JSONObject finalJsonarr=new JSONObject();
			String dateFormat="yyyy-MM-dd HH:mm:ss";
			CommonUtils commonjvm=new CommonUtils();
			
			finalJsonarr.put("booking_id", facitityObj.getBookingId());
			finalJsonarr.put("title", facitityObj.getTitle());
			finalJsonarr.put("place", facitityObj.getPlace());
			
			finalJsonarr.put("start_time",commonjvm.dateToStringModify(facitityObj.getStartTime(),dateFormat));
			finalJsonarr.put("end_time", commonjvm.dateToStringModify(facitityObj.getEndTime(),dateFormat));
			if(facitityObj.getBookedBy()!=null && facitityObj.getBookedBy().getMobileNo()!=null){
				finalJsonarr.put("contactno", facitityObj.getBookedBy().getMobileNo());
			}else{
				finalJsonarr.put("contactno","");
			}
			
			finalJsonarr.put("desc", facitityObj.getDescription());
			finalJsonarr.put("facility_id", ""+facitityObj.getFacilityId().getFacilityId());
			finalJsonarr.put("booking_status", ""+facitityObj.getBookingStatus());
			if(facitityObj.getCommiteecomment()!=null){
			finalJsonarr.put("committee_desc", facitityObj.getCommiteecomment());
			}else{
				finalJsonarr.put("committee_desc", "");
			}
			if(finalJsonarr!=null){
			additionalData=finalJsonarr.toString();
			}
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println(" AdditionalDataDaoServices.formAdditionalDataForFacilityBookingTbl======" + ex);
			log.logMessage("AdditionalDataDaoServices Exception formAdditionalDataForFacilityBookingTbl : " + ex,
					"error", AdditionalDataDaoServices.class);
		}
		
		return additionalData;
	}

	@Override
	public String formAdditionalDataForComplaintMastTbl(
			ComplaintsTblVO complaintsObj) {
		// TODO Auto-generated method stub
		String additionalData="";
		JSONObject finalJsonarr = new JSONObject();
		Date lvrEntrydate = null;
		Common locCommonObj = new CommonDao();
		JSONArray locLBRSklJSONAryobjImgNew=new JSONArray();
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		List<ComplaintattchTblVO> complaintsAttachList=new ArrayList<ComplaintattchTblVO>();
		ComplaintsDao complains=new ComplaintsDaoServices();
		try{
			
			
			if(complaintsObj.getComplaintsId()!=null){
				finalJsonarr.put("complaint_id", Commonutility.toCheckNullEmpty((complaintsObj.getComplaintsId())));
				}else{
					finalJsonarr.put("complaint_id", "");
				}
			if(complaintsObj.getComplaintsDesc()!=null){
				finalJsonarr.put("desc", Commonutility.toCheckNullEmpty(complaintsObj.getComplaintsDesc()));
				}else{
					finalJsonarr.put("desc", "");
				}
			if(complaintsObj.getComplaintsDesc()!=null){
				finalJsonarr.put("committee_desc", Commonutility.toCheckNullEmpty(complaintsObj.getClosereason()));
				}else{
					finalJsonarr.put("committee_desc", "");
				}
			if(complaintsObj.getComplaintsTitle()!=null){
				finalJsonarr.put("title", Commonutility.toCheckNullEmpty(complaintsObj.getComplaintsTitle()));
				}else{
					finalJsonarr.put("title", "");
				}
			if(Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()).equalsIgnoreCase("0")){
				finalJsonarr.put("post_to",  Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()));
				finalJsonarr.put("email_id", Commonutility.toCheckNullEmpty((complaintsObj.getComplaintsOthersEmail())));
				}
			else if(Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()).equalsIgnoreCase("1")){
				finalJsonarr.put("post_to", Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()));
				}
			else{
					finalJsonarr.put("post_to", Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()));
				}
			if(complaintsObj.getStatus()!=null){
				finalJsonarr.put("status", Commonutility.toCheckNullEmpty(complaintsObj.getStatus()));
				}else{
					finalJsonarr.put("status", "");
				}
			lvrEntrydate = complaintsObj.getEntryDatetime();
			
			if(complaintsObj.getEntryDatetime()!=null){
				finalJsonarr.put("post_date", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
				}else{
					finalJsonarr.put("post_date", "");
				}
			
			finalJsonarr.put("profile_name", Commonutility.toCheckNullEmpty(complaintsObj.getUsrRegTblByFromUsrId().getFirstName()));
			finalJsonarr.put("profile_location", Commonutility.toCheckNullEmpty(complaintsObj.getUsrRegTblByFromUsrId().getAddress1()));
			finalJsonarr.put("usr_id", Commonutility.toCheckNullEmpty(complaintsObj.getUsrRegTblByFromUsrId().getUserId()));
			if(complaintsObj.getUsrRegTblByFromUsrId().getImageNameMobile()!=null){
				finalJsonarr.put("profile_pic",Commonutility.toCheckNullEmpty(rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+complaintsObj.getUsrRegTblByFromUsrId().getImageNameMobile()));}
				else
				{
					finalJsonarr.put("profile_pic","");
				}
			
			finalJsonarr.put("feed_id", Commonutility.toCheckNullEmpty(complaintsObj.getFeedId()));
		int varComplaintAttach = complaintsObj.getComplaintsId();
		
		locLBRSklJSONAryobjImgNew = new JSONArray();
		
		JSONObject comVido = new JSONObject();
		JSONObject images = new JSONObject();
		JSONArray imagesArr = new JSONArray();
		JSONArray comVidoArr = new JSONArray();
		
		boolean flag=false;
		complaintsAttachList=complains.getComplaintAttachList(varComplaintAttach);
		System.out.println("===complaintsAttachList==="+complaintsAttachList.size());
		ComplaintattchTblVO complaintsAttachObj;
		for (Iterator<ComplaintattchTblVO> itObj = complaintsAttachList
				.iterator(); itObj.hasNext();) {
			complaintsAttachObj = itObj.next();
			flag=true;
			//finalJsonarr = new JSONObject();
			images = new JSONObject();
			comVido = new JSONObject();
				if(complaintsAttachObj.getIvrBnFILE_TYPE() == 1)
				{
					System.out.println("======sss===locLBRSklJSONAryobjImgNew===="+locLBRSklJSONAryobjImgNew.length());
					if(complaintsAttachObj.getIvrBnFILE_TYPE()!=null){
					images.put("img_id",String.valueOf(complaintsAttachObj.getIvrBnATTCH_ID()));
					}else{
						images.put("img_id","");
					}if(complaintsAttachObj.getIvrBnATTACH_NAME()!=null){
					images.put("img_url",rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/mobile/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
					}else{
						images.put("img_url","");
					}
				}
				 if(complaintsAttachObj.getIvrBnFILE_TYPE() == 2)
				{
					System.out.println("==thumb_img===="+complaintsAttachObj.getThumbImage());
					if(complaintsAttachObj.getIvrBnFILE_TYPE()!=null){
					comVido.put("video_id",String.valueOf(complaintsAttachObj.getIvrBnATTCH_ID()) );
					}else{
						comVido.put("video_id","");
					}if(complaintsAttachObj.getThumbImage()!=null){
					comVido.put("thumb_img", rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/thumbimage/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getThumbImage());
					}else{
						comVido.put("thumb_img","");
					}if(complaintsAttachObj.getIvrBnATTACH_NAME()!=null){
					comVido.put("video_url", rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/videos/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
					}else{
						comVido.put("video_url","");
					}
					
				}
				
				 if(images!=null && images.length()>0){
				 imagesArr.put(images);
				 finalJsonarr.put("images",imagesArr);
				 }else{
					 finalJsonarr.put("images",imagesArr); 
				 }
				 if(comVido!=null && comVido.length()>0){
				 comVidoArr.put(comVido);
				finalJsonarr.put("videos",comVidoArr);
				 }else{
					 finalJsonarr.put("videos",comVidoArr); 
				 }
				
			}				
		if(flag==false){
			finalJsonarr.put("images",imagesArr);
			finalJsonarr.put("videos",comVidoArr);
		}	
		additionalData=finalJsonarr.toString();
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println(" AdditionalDataDaoServices.formAdditionalDataForComplaintMastTbl======" + ex);
			log.logMessage("AdditionalDataDaoServices Exception formAdditionalDataForComplaintMastTbl : " + ex,
					"error", AdditionalDataDaoServices.class);
		}
		return additionalData;
	}

	@Override
	public String formAdditionalDataForDocumentTbl(
			DocumentManageTblVO documentObj,DocumentShareTblVO docahareobj) {
		// TODO Auto-generated method stub
		String additionalData="";
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try{
			
			JSONObject finalJsonarr=new JSONObject();
			CommonUtils commjvm=new CommonUtils();
			String fileName=docahareobj.getDocumentId().getDocFileName();
			Integer shareId= docahareobj.getUserId().getUserId();
			String dateFolderPath=docahareobj.getDocumentId().getDocDateFolderName();
			Integer documentType= docahareobj.getDocumentId().getDocTypId().getIvrBnDOC_TYP_ID();
			
			String extension=commjvm.getFileExtension(fileName);
			System.out.println("extension------------"+extension);
			if(extension!=null){
				System.out.println("extension---------------"+extension);
				mobiCommon mobCom=new mobiCommon();
				 Integer fileType=mobCom.getFileExtensionType(extension);
				 finalJsonarr.put("file_type", ""+fileType);	
			}else{
				finalJsonarr.put("file_type", "9");	
			}
				
				String docPath=rb.getString("SOCIAL_INDIA_BASE_URL") +"/externalPath/document/";
				String docsizPath=rb.getString("external.documnet.fldr");
				String publicOrPrivateTath=rb.getString("external.documnet.private.fldr");
				String generalpath="";
				String webpath=rb.getString("external.inner.webpath");
				String userPath="";
				if(shareId==null){
					publicOrPrivateTath=rb.getString("external.documnet.public.fldr");
					dateFolderPath=dateFolderPath+"/";
				}else{
					publicOrPrivateTath=rb.getString("external.documnet.private.fldr");
					userPath=shareId+"/";
					dateFolderPath=dateFolderPath+"/";
				}
				if(documentType==8){
					generalpath=rb.getString("external.documnet.maintenancedoc.fldr");
				}else{
					generalpath=rb.getString("external.documnet.generaldoc.fldr");
					
				}
				String fileUrl=docPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
				String filesizeUrl=docsizPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+fileName;
				finalJsonarr.put("file_url",fileUrl);
				System.out.println("fileUrl-----------------"+filesizeUrl);
				System.out.println(" commjvm.getFileSizefileUrl-------------"+ commjvm.getFileSize(filesizeUrl, "MB"));
				finalJsonarr.put("size", commjvm.getFileSize(filesizeUrl, "MB"));
			
			finalJsonarr.put("file_name",fileName);
			
			if(finalJsonarr!=null){
			additionalData=finalJsonarr.toString();
			System.out.println("additionalData----------------------"+additionalData);
			}
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println(" AdditionalDataDaoServices.formAdditionalDataForFacilityBookingTbl======" + ex);
			log.logMessage("AdditionalDataDaoServices Exception formAdditionalDataForFacilityBookingTbl : " + ex,
					"error", AdditionalDataDaoServices.class);
		}
		
		return additionalData;
	}

	@Override
	public String formAdditionalDataForFeedTbl(FeedsTblVO feedTblObj) {
		// TODO Auto-generated method stub
		String additionalData="";
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try{
			System.out.println("11111111111111111111111------enter into formAdditionalDataForFeedTbl");
			List<Object[]> feedListObj = new ArrayList<Object[]>();
			FeedDAO feedserviceObj = new FeedDAOService();
			JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
			 org.json.simple.JSONObject locObjRspdataJson = new  org.json.simple.JSONObject();
			 String imagePathWeb = rb.getString("SOCIAL_INDIA_BASE_URL")  + rb.getString("external.templogo") + rb.getString("external.uploadfile.feed.img.webpath");
				String imagePathMobi = rb.getString("SOCIAL_INDIA_BASE_URL")  + rb.getString("external.templogo") +  rb.getString("external.uploadfile.feed.img.mobilepath");
				String videoPath = rb.getString("SOCIAL_INDIA_BASE_URL")  + rb.getString("external.templogo") +  rb.getString("external.uploadfile.feed.videopath");
				String videoPathThumb = rb.getString("SOCIAL_INDIA_BASE_URL")  + rb.getString("external.templogo") +  rb.getString("external.uploadfile.feed.video.thumbpath");
				String profileimgPath = rb.getString("SOCIAL_INDIA_BASE_URL") + rb.getString("external.templogo") + rb.getString("external.view.profile.mobilepath");
			System.out.println("feedTblObj.getUsrId().getUserId()--------------"+feedTblObj);
			System.out.println("feedTblObj.getUsrId().getUserId()--------------"+feedTblObj.getUsrId());
			System.out.println("feedTblObj.getUsrId().getUserId()--------------"+feedTblObj.getUsrId().getUserId());
			System.out.println("feedTblObj.getUsrId().getSocietyId().getActivationKey()---------------------"+feedTblObj.getUsrId().getSocietyId().getActivationKey());
			System.out.println("feedTblObj.getFeedId()--------------"+feedTblObj.getFeedId());
			feedListObj = feedserviceObj.feedDetailsProc(feedTblObj.getUsrId().getUserId(), feedTblObj.getUsrId().getSocietyId().getActivationKey(), feedTblObj.getFeedId(),"");
			
			Object[] objList;
			for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
				System.out.println("%%^%^^%%^");
				objList = it.next();									
				System.out.println("--j----");
				if (objList != null) {
					System.out.println("---d11111---111111111111111111");
					locObjRspdataJson = jsonDataPack.feedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
					if (objList[0]!=null) {
						System.out.println("---l---");
						System.out.println((int)objList[0]);
					}
				}
			}
			
			
			
			if(locObjRspdataJson!=null){
			additionalData=locObjRspdataJson.toString();
			}
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println(" AdditionalDataDaoServices.formAdditionalDataForFacilityBookingTbl======" + ex);
			log.logMessage("AdditionalDataDaoServices Exception formAdditionalDataForFacilityBookingTbl : " + ex,
					"error", AdditionalDataDaoServices.class);
		}
		
		return additionalData;
	}

	@Override
	public String formAdditionalDataForEventTbl(EventTblVO eventObj) {
		// TODO Auto-generated method stub
		String additionalData="";
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try{
			EventDao eventhbm=new EventDaoServices();
			List<Object[]> eventListObj = new ArrayList<Object[]>();
			JSONObject locObjRspdataJson=new JSONObject();
			CommonUtils commjvm=new CommonUtils();
			
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
					+ "WHERE EVENT.EVENT_ID="+eventObj.getIvrBnEVENT_ID();
			
			eventListObj=eventhbm.getEventContributionSearchList(query, "0");
			List<Object[]> eventListObj1 = new ArrayList<Object[]>();
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
				feedobj=feedHbm.getFeedDetailsByEventId(eventId,9);
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
					String externalUserImagePath = rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+eventObj.getIvrBnEVENT_CRT_USR_ID()+"/"+(String)objList[9];
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
					String eventpath=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/"+rb.getString("external.inner.eventfldr")+"/mobile/"+eventId+"/"+(String)objList[16];
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
				
				
				
			}
			
			
			
			if(evntobj!=null){
			additionalData=evntobj.toString();
			}
			System.out.println("additionalData----------------"+additionalData);
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println(" AdditionalDataDaoServices.formAdditionalDataForFacilityBookingTbl======" + ex);
			log.logMessage("AdditionalDataDaoServices Exception formAdditionalDataForFacilityBookingTbl : " + ex,
					"error", AdditionalDataDaoServices.class);
		}
		
		return additionalData;
	}

	@Override
	public String formAdditionalDataForNetworkTbl(MvpNetworkTbl networkObj,int societyId,UserMasterTblVo userMst) {
		// TODO Auto-generated method stub
		String additionalData="";
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try{
			networkDao network=new networkDaoServices();
			JSONObject finalJsonarr=new JSONObject();
			otpDao otp=new otpDaoService();
			FunctionUtility commonFn = new FunctionUtilityServices();
			
			String qry="select USR_ID,FNAME,LNAME,IMAGE_NAME_MOBILE,SOCIAL_PICTURE,"
					+ "LAST_LOGIN,ONLINE_STATUS,CONN_STS_FLG,PARENT_USR_ID,"
					+ "CONNECTED_USR_ID,NETWORK_ID from (SELECT USR.USR_ID USR_ID,USR.FNAME FNAME,USR.LNAME LNAME,"
					+ "USR.IMAGE_NAME_MOBILE IMAGE_NAME_MOBILE,USR.SOCIAL_PICTURE SOCIAL_PICTURE,USR.LAST_LOGIN LAST_LOGIN,"
					+ "USR.ONLINE_STATUS ONLINE_STATUS,NTWRK.CONN_STS_FLG CONN_STS_FLG,NTWRK.PARENT_USR_ID,"
					+ "ifnull(NTWRK.CONNECTED_USR_ID,USR.USR_ID) CONNECTED_USR_ID,NTWRK.NETWORK_ID NETWORK_ID,(select count(*) "
					+ "from mvp_network_tbl ntwrk where (NTWRK.PARENT_USR_ID='"+userMst.getUserId()+"' and NTWRK.CONNECTED_USR_ID=USR.USR_ID) or "
					+ "(NTWRK.PARENT_USR_ID=USR.USR_ID and NTWRK.CONNECTED_USR_ID='"+userMst.getUserId()+"') ) flg FROM USR_REG_TBL USR  "
					+ "LEFT JOIN MVP_NETWORK_TBL NTWRK ON (USR.USR_ID=NTWRK.PARENT_USR_ID OR USR.USR_ID=NTWRK.CONNECTED_USR_ID) and USR.USR_ID='"+userMst.getUserId()+"'   "
					+ " WHERE   USR.ACT_STS=1 AND USR.SOCIETY_ID='"+userMst.getSocietyId().getSocietyId()+"' and ntwrk.NETWORK_ID='"+networkObj.getNetworkId()+"' ) tt where flg=0";
			
			List<Object[]> networkListObj = new ArrayList<Object[]>();
			System.out.println("===========qry==========="+qry);
			networkListObj=network.getnetworkListSearch(qry,"0","100");
			UserMasterTblVo userInfo=new UserMasterTblVo();
			
			Object[] objList;
			int totalcnt=0;
			for(Iterator<Object[]> it=networkListObj.iterator();it.hasNext();) {
				objList = it.next();
				userInfo=new UserMasterTblVo();
				
				Integer userid= (Integer)objList[0];
				Integer connectedUser= (Integer)objList[9];
				Integer parentdUser= (Integer)objList[8];
				System.out.println("userid=========="+userid+"=========userMst.getUserId()======"+userMst.getUserId()+"====connectedUser===="+connectedUser+"============parentdUser+======="+parentdUser);
				if(userid!=null && connectedUser!=null && Integer.parseInt(userid.toString())==Integer.parseInt(connectedUser.toString()) && Integer.parseInt(userid.toString())==userMst.getUserId() && parentdUser==null){
					System.out.println("if-------------------------");
				}else{
				totalcnt++;
				finalJsonarr=new JSONObject();
				
				System.out.println("userid-------"+userid);
				//finalJsonarr.put("nwuser_id", Commonutility.toCheckNullEmpty(String.valueOf(userid)));
				String username="";
				if((String)objList[1]!=null){
					username= (String)objList[1];
					}
				if((String)objList[2]!=null){
					if(username!=""){
						username +=" "+(String)objList[2];
					}else{
					username += (String)objList[2];
					}
				}
				
				//finalJsonarr.put("from_nwuser_name",Commonutility.toCheckNullEmpty(username));
				/*if((String)objList[3]!=null){
					finalJsonarr.put("from_nwuser_img_url", getText("project.image.url.mobile")+"/templogo/profile/mobile/"+userid+"/"+(String)objList[3]);
				}
				else if((String)objList[4]!=null){
					finalJsonarr.put("from_nwuser_img_url", Commonutility.toCheckNullEmpty((String)objList[4]));
				}else{
					finalJsonarr.put("from_nwuser_img_url","");		
				}*/
				
				finalJsonarr.put("is_online", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[6])));
				if((Integer)objList[7]!=null){
					finalJsonarr.put("invite_status", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[7])));	
				}else{
					finalJsonarr.put("invite_status", "1");		
				}
				
				
				
				
				if((Integer)objList[8]!=null){
					finalJsonarr.put("from_id", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[8])));
					System.out.println("=f=dgf=dg===================");
					userInfo=otp.checkUserDetails(String.valueOf(objList[8]));
					String fromusername="";
					System.out.println("userInfo------------"+userInfo.getUserId());
					System.out.println("==entrytime=="+userInfo.getLoginDatetime());
					if(userInfo.getLoginDatetime()!=null){
						String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
						finalJsonarr.put("last_accessed_time", finalDate);	
					}else{
						finalJsonarr.put("last_accessed_time", "");	
					}
					
					if(userInfo.getFirstName()!=null){
						fromusername= userInfo.getFirstName();
						}
					
					if(userInfo.getLastName()!=null){
						if(fromusername!=""){
							fromusername +=" "+userInfo.getLastName();
						}
					}
					if(fromusername!=null && fromusername.equalsIgnoreCase("")){
						fromusername= rb.getString("anonymous.user.name");
					}
					finalJsonarr.put("from_nwuser_name",Commonutility.toCheckNullEmpty(fromusername));
					if(userInfo.getImageNameMobile()!=null){
						finalJsonarr.put("from_nwuser_img_url", rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+objList[8]+"/"+userInfo.getImageNameMobile());
						}else if(userInfo.getSocialProfileUrl()!=null){
							finalJsonarr.put("from_nwuser_img_url", userInfo.getSocialProfileUrl());	
						}
						else{
							finalJsonarr.put("from_nwuser_img_url", "");	
						}
					
					
					
				}else{
					System.out.println("=df=dsf=s=="+userid);
					finalJsonarr.put("to_id", Commonutility.toCheckNullEmpty(String.valueOf(userid)));
					userInfo=otp.checkUserDetails(String.valueOf(userid));
					if(userInfo!=null){
					String tousername="";
					System.out.println("userInfo---- else--------"+userInfo.getUserId());
					System.out.println("==entrytime=="+userInfo.getLoginDatetime());
					if(userInfo.getLoginDatetime()!=null){
						String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
						finalJsonarr.put("last_accessed_time", finalDate);	
					}else{
						finalJsonarr.put("last_accessed_time", "");	
					}
					
					if(userInfo.getFirstName()!=null){
						tousername= userInfo.getFirstName();
						}
					if(userInfo.getLastName()!=null){
						if(tousername!=""){
							tousername +=" "+userInfo.getLastName();
						}
					}
					if(tousername!=null && tousername.equalsIgnoreCase("")){
						tousername= rb.getString("anonymous.user.name");
					}
					finalJsonarr.put("to_nwuser_name",Commonutility.toCheckNullEmpty(tousername));
					if(userInfo.getImageNameMobile()!=null){
						finalJsonarr.put("to_nwuser_img_url",  rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+userid+"/"+userInfo.getImageNameMobile());
						}else if(userInfo.getSocialProfileUrl()!=null){
							finalJsonarr.put("to_nwuser_img_url", userInfo.getSocialProfileUrl());	
						}else{
							finalJsonarr.put("to_nwuser_img_url", "");	
						}
					
					}
					finalJsonarr.put("from_nwuser_img_url", "");
					finalJsonarr.put("from_nwuser_name","");
				}
				System.out.println("=f=dgf=dg=====eeeeeeeeee=============="+String.valueOf(objList[9]));
				if((Integer)objList[9]!=null){
					finalJsonarr.put("to_id", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[9])));
					System.out.println("=f=dgf=dg===================");
					String logusid=Integer.toString(userMst.getUserId());
					System.out.println("=f=dgf=dg=========logusid=========="+logusid);
					if(objList[8]!=null && logusid.equalsIgnoreCase(String.valueOf(objList[9]))){
						userInfo=otp.checkUserDetails(String.valueOf(objList[8]));
					}else{
						userInfo=otp.checkUserDetails(String.valueOf(objList[9]));
					}
					
					String tousername="";
					System.out.println("==entrytime=="+userInfo.getLoginDatetime());
					if(userInfo.getLoginDatetime()!=null){
						String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
						finalJsonarr.put("last_accessed_time", finalDate);	
					}else{
						finalJsonarr.put("last_accessed_time", "");	
					}
					if(userInfo.getFirstName()!=null){
						tousername= userInfo.getFirstName();
						}
					if(userInfo.getLastName()!=null){
						if(tousername!=""){
							tousername +=" "+userInfo.getLastName();
						}
					}
					if(tousername!=null && tousername.equalsIgnoreCase("")){
						tousername= rb.getString("anonymous.user.name");
					}
					finalJsonarr.put("to_nwuser_name",Commonutility.toCheckNullEmpty(tousername));
					if(userInfo.getImageNameMobile()!=null){
						finalJsonarr.put("to_nwuser_img_url",  rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+objList[9]+"/"+userInfo.getImageNameMobile());
						}else if(userInfo.getSocialProfileUrl()!=null){
							finalJsonarr.put("to_nwuser_img_url", userInfo.getSocialProfileUrl());	
						}else{
							finalJsonarr.put("to_nwuser_img_url", "");	
						}
					
					finalJsonarr.put("network_id", String.valueOf(objList[10]));
					
					
				}else{
					System.out.println("=df=dsf=s=="+userid);
					finalJsonarr.put("to_id", Commonutility.toCheckNullEmpty(String.valueOf(userid)));
					userInfo=otp.checkUserDetails(String.valueOf(userid));
					if(userInfo!=null){
					String tousername="";
					System.out.println("==entrytime=="+userInfo.getLoginDatetime());
					if(userInfo.getLoginDatetime()!=null){
						String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
						finalJsonarr.put("last_accessed_time", finalDate);	
					}else{
						finalJsonarr.put("last_accessed_time", "");	
					}
					if(userInfo.getFirstName()!=null){
						tousername= userInfo.getFirstName();
						}
					if(userInfo.getLastName()!=null){
						if(tousername!=""){
							tousername +=" "+userInfo.getLastName();
						}
					}
					if(tousername!=null && tousername.equalsIgnoreCase("")){
						tousername= rb.getString("anonymous.user.name");
					}
					finalJsonarr.put("to_nwuser_name",Commonutility.toCheckNullEmpty(tousername));
					if(userInfo.getImageNameMobile()!=null){
					finalJsonarr.put("to_nwuser_img_url",  rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+userid+"/"+userInfo.getImageNameMobile());
					}else if(userInfo.getSocialProfileUrl()!=null){
						finalJsonarr.put("to_nwuser_img_url", userInfo.getSocialProfileUrl());	
					}else{
						finalJsonarr.put("to_nwuser_img_url", "");	
					}
					}else{
						finalJsonarr.put("to_nwuser_img_url", "");	
						finalJsonarr.put("to_nwuser_name","");
					}
				}
				finalJsonarr.put("from_id", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[8])));
				finalJsonarr.put("network_id", String.valueOf(objList[10]));
				
				}
			}
			
			
			
			if(finalJsonarr!=null){
			additionalData=finalJsonarr.toString();
			}
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println(" AdditionalDataDaoServices.formAdditionalDataForNetworkTbl======" + ex);
			log.logMessage("AdditionalDataDaoServices Exception formAdditionalDataForNetworkTbl : " + ex,
					"error", AdditionalDataDaoServices.class);
		}
		
		return additionalData;
	}
	
	
}
