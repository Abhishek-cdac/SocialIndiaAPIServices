package com.social.utititymgmt;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.carpooling.OfferCarPoolingAddEdit;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.common.CommonDaoService;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.FacilityBookingTblVO;

public class BookingUtility {
	Log log=new Log();
	
	
	public static JSONObject toSltSingleBookingDtl(JSONObject pDataJson, Session pSession, String pAuditMsg, String pAuditCode) {// Select on single user data
		String locvrBOOKING_ID=null, locvrBOOKING_STS=null; 
		FacilityBookingTblVO locLDTblvoObj=null;
		String loc_slQry=null;
		Query locQryObj= null;
		JSONObject locRtnDataJSON=null;
			Log log=null;
			String locusrname=null;	
			Date lvrEntrydate = null;
			Date lvrenddate = null;
			Common locCommonObj = null;
		try {
			log=new Log();		
			locCommonObj=new CommonDao();
			Commonutility.toWriteConsole("Step 1 : booking detail block enter");
			log.logMessage("Step 1 : Select booking detail block enter", "info", BookingUtility.class);
			
			locRtnDataJSON=new JSONObject();
			locvrBOOKING_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "bookingid");
			locvrBOOKING_STS = "1";										
			loc_slQry="from FacilityBookingTblVO where bookingId="+locvrBOOKING_ID+"";
			locQryObj=pSession.createQuery(loc_slQry);						
			locLDTblvoObj = (FacilityBookingTblVO)locQryObj.uniqueResult();;
			Commonutility.toWriteConsole("Step 2 : Select booking detail query executed.");
			locRtnDataJSON.put("booking_id",Commonutility.toCheckNullEmpty(locLDTblvoObj.getBookingId()));	
			locRtnDataJSON.put("booking_facilityname",Commonutility.toCheckNullEmpty(locLDTblvoObj.getFacilityId().getFacilityName()));
			locRtnDataJSON.put("booking_facid",Commonutility.toCheckNullEmpty(locLDTblvoObj.getFacilityId().getFacilityId()));
			locRtnDataJSON.put("booking_facbktitle",Commonutility.toCheckNullEmpty(locLDTblvoObj.getTitle()));
			locRtnDataJSON.put("booking_facbkdesc",Commonutility.toCheckNullEmpty(locLDTblvoObj.getDescription()));
			locRtnDataJSON.put("booking_facdesc",Commonutility.toCheckNullEmpty(locLDTblvoObj.getFacilityId().getDescription()));
			locRtnDataJSON.put("booking_place",Commonutility.toCheckNullEmpty(locLDTblvoObj.getFacilityId().getPlace()));	
			locRtnDataJSON.put("booking_mobno",Commonutility.toCheckNullEmpty(locLDTblvoObj.getContactNo()));	
			if(!Commonutility.checkempty(locLDTblvoObj.getContactNo())) {
				if (locLDTblvoObj.getEntryBy()!=null) {
					locRtnDataJSON.put("booking_mobno", Commonutility.toCheckNullEmpty(locLDTblvoObj.getEntryBy().getMobileNo()));
				} else {
					locRtnDataJSON.put("booking_mobno",Commonutility.toCheckNullEmpty(locLDTblvoObj.getContactNo()));
				}
				
			} else {
				locRtnDataJSON.put("booking_mobno","");
			}
			lvrEntrydate=locLDTblvoObj.getStartTime();
			locRtnDataJSON.put("booking_startdate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "dd-MM-yyyy HH:mm:ss"));
			lvrenddate=locLDTblvoObj.getEndTime();
			locRtnDataJSON.put("booking_enddate", locCommonObj.getDateobjtoFomatDateStr(lvrenddate, "dd-MM-yyyy HH:mm:ss"));	
			
			if (locLDTblvoObj.getEntryBy()!=null) {
				if (locLDTblvoObj.getEntryBy().getFirstName()!=null && Commonutility.checkempty(locLDTblvoObj.getEntryBy().getFirstName())) {
					locusrname = locLDTblvoObj.getEntryBy().getFirstName();
				}
				if (locLDTblvoObj.getEntryBy().getLastName()!=null && Commonutility.checkempty(locLDTblvoObj.getEntryBy().getLastName())) {
					locusrname += " " + locLDTblvoObj.getEntryBy().getLastName();
				}
			}
			
			if (locusrname != null && Commonutility.checkempty(locusrname)) {
				locRtnDataJSON.put("booking_name",Commonutility.toCheckNullEmpty(locusrname));	
			} else {
				locRtnDataJSON.put("booking_name",Commonutility.toCheckNullEmpty(locLDTblvoObj.getEntryBy().getMobileNo()));	
			}
			
			locRtnDataJSON.put("booking_usrid",Commonutility.toCheckNullEmpty(locLDTblvoObj.getEntryBy().getUserId()));	
			locRtnDataJSON.put("booking_status",Commonutility.toCheckNullEmpty(locLDTblvoObj.getBookingStatus()));	
			locRtnDataJSON.put("booking_facplace",Commonutility.toCheckNullEmpty(locLDTblvoObj.getFacilityId().getPlace()));	
			locRtnDataJSON.put("commitee_comments",Commonutility.toCheckNullEmpty(locLDTblvoObj.getCommiteecomment()));	
			return locRtnDataJSON;
		} catch (Exception e) {
			try{			
				Commonutility.toWriteConsole("Step -1 : Exception found in "+ Thread.currentThread().getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
				log.logMessage("Exception : "+e, "error", BookingUtility.class);
				locRtnDataJSON=new JSONObject();
				locRtnDataJSON.put("catch", "Error");
			}catch(Exception ee){}
			return locRtnDataJSON;
		} finally {
			locvrBOOKING_ID=null; locLDTblvoObj=null; loc_slQry=null; locRtnDataJSON=null;log=null;			 
		}
	}
		
	public static String[] toApprovalBooking(JSONObject pDataJson, Session pSession,String pAuditMsg, String pAuditCode, 
			UserMasterTblVo userMst,FacilityBookingTblVO facilityBookingobj, JSONArray userArray, String userprivacy) {
		
		String[] response = new String[2];
		
		String locvrBOOKING_ID=null,locvrstatus=null,locvrcomment=null;
		String locrestUpdqry=null;		
		boolean locrestUpdSts=false;
		CommonDaoService locrestObj=null;
		Log log=null;
		Integer lvrCnt = null;
		Session lvrHbsession = null;
		Query lvrQry = null, lvrQrycnt = null;
		FacilityBookingTblVO lvrFbkvoobj = null;
		Common locCommonObj = null;
		int facilityIdint =0, retCntVal = -1;
		String startDateTime = null, endDateTime = null, facilityIdstr = null;
		try {
			log=new Log();
			locvrBOOKING_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "bookingid");
			locvrstatus  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "statusflg");
			locvrcomment  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "committeecomments");
			Commonutility.toWriteConsole("Step 1 :locvrstatus flag : "+locvrstatus);
			if (Commonutility.checkempty(locvrstatus) && locvrstatus.equalsIgnoreCase("1")){
				Commonutility.toWriteConsole("Step 2 : toenter to check");
				lvrHbsession = HibernateUtil.getSession();							
				if (lvrHbsession.isConnected()){
					locCommonObj=new CommonDao();
					String lvrSlqrybookdtls = "from FacilityBookingTblVO where bookingId ="+locvrBOOKING_ID+""; //  getbooking details
					Commonutility.toWriteConsole("Step 3 : lvrSlqrybookdtls : "+lvrSlqrybookdtls);
					lvrQry = lvrHbsession.createQuery(lvrSlqrybookdtls);
					lvrQry.setMaxResults(1);
					lvrFbkvoobj = (FacilityBookingTblVO) lvrQry.uniqueResult();
					
					if (lvrFbkvoobj!=null){
						startDateTime = locCommonObj.getDateobjtoFomatDateStr(lvrFbkvoobj.getStartTime(), "yyyy-MM-dd HH:mm:ss");
						endDateTime = locCommonObj.getDateobjtoFomatDateStr(lvrFbkvoobj.getEndTime(), "yyyy-MM-dd HH:mm:ss");
						if (lvrFbkvoobj.getFacilityId()!=null){
							facilityIdint = lvrFbkvoobj.getFacilityId().getFacilityId();
						} else {
							facilityIdint =0;
						}
						Commonutility.toWriteConsole("Step 4 : facilityIdint : "+facilityIdint);
					String query = "SELECT count(facilityId.facilityId) FROM FacilityBookingTblVO WHERE  (((startTime <= '" + startDateTime + "' and endTime >= '" + startDateTime + "') or (startTime <= '" + endDateTime + "' and endTime >= '" + endDateTime + "')) or  (('" + startDateTime + "' <= startTime and '" + endDateTime + "' >= startTime) or ('" + startDateTime + "' <= endTime and '" + endDateTime + "' >= endTime))) and facilityId.facilityId = '"+facilityIdint+"' and statusFlag = 1 and bookingStatus = 1";
					Commonutility.toWriteConsole("Step 5 : query ==== "+query);					
lvrQrycnt = lvrHbsession.createQuery(query);
					//lvrQrycnt.setMaxResults(1);
Commonutility.toWriteConsole("Step 5.0");
					lvrCnt = ((Number) lvrQrycnt.uniqueResult()).intValue(); 					
Commonutility.toWriteConsole("Step 5.0.1");
					} else {
						Commonutility.toWriteConsole("Step 4 : lvrFbkvoobj Detail query rtn object : "+lvrFbkvoobj);
						lvrCnt = -1;
					}
				} else {
					Commonutility.toWriteConsole("Step 3 : Connection not found");
					lvrCnt = -1;
				}													
			} else {
				Commonutility.toWriteConsole("Step 2 : For Deactive purpose");
				lvrCnt = -1;
			}
			
			Commonutility.toWriteConsole("Step 5.1 : lvrCnt ==== "+lvrCnt);			
			if (lvrCnt!=null && lvrCnt<=0){
				locrestUpdqry ="update FacilityBookingTblVO set bookingStatus="+locvrstatus+",commiteecomment='"+locvrcomment+"' where bookingId ="+locvrBOOKING_ID+" and statusFlag=1" ;
				log.logMessage("Updqry : "+locrestUpdqry, "info", BookingUtility.class);
				locrestObj=new CommonDaoService();
				locrestUpdSts = locrestObj.commonUpdate(locrestUpdqry);
			} else {
				locrestUpdSts = false;
			}
			
			Commonutility.toWriteConsole("Step 5.1.1 : locrestUpdSts ==== "+locrestUpdSts + " userMst:"+userMst + " facilityBookingobj:"+facilityBookingobj);	
			
			 if(locrestUpdSts){
				//start: add feed info
				 if(userMst !=null && facilityBookingobj != null){
					 int bookingid = Integer.parseInt(locvrBOOKING_ID);
					 facilityBookingobj.setBookingStatus(Integer.parseInt(locvrstatus));
					 facilityBookingobj.setCommiteecomment(locvrcomment);
					
					 FeedsTblVO feedObj = null;
					 FeedDAO feadhbm = new FeedDAOService();
					 
					 //check is feed already present in database
					 Commonutility.toWriteConsole("BookingUtility - getFeedDetailsByFacilityBookId bookingid:"+bookingid);
					 feedObj = feadhbm.getFeedDetailsByFacilityBookId(bookingid);
					 Commonutility.toWriteConsole("BookingUtility - getFeedDetailsByFacilityBookId getFeedId:"+feedObj.getFeedId() + " locvrstatus:"+locvrstatus + " locvrcomment:"+locvrcomment);
					 
					 if(feedObj !=null && feedObj.getFeedId()!=null){
//						 feedObj = new FeedsTblVO();
//						 feedObj.setFeedId(feedObj.getFeedId());
//						 feedObj.setUsrId(userMst);
//						 int feedTypFlg = 77; // facility booking - category_mst_tbl
//						 feedObj.setFeedType(feedTypFlg);
//						 feedObj.setFeedTypeId(bookingid);
//						 feedObj.setFeedTitle(facilityBookingobj.getTitle());
//						 feedObj.setFeedCategory("Facility Booking");
//						 feedObj.setFeedDesc(facilityBookingobj.getDescription());
//						 feedObj.setPostBy(userMst.getUserId());
//							
//						 if(userMst.getFirstName()!=null){
//							 feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
//						 }
//						 feedObj.setSocietyId(userMst.getSocietyId());
//							
//						 if(userMst.getCityId()!=null){
//							 feedObj.setFeedLocation(userMst.getCityId().getCityName());
//						 }
//							
//						 CommonMobiDao comondaohbm=new CommonMobiDaoService();
//						 comondaohbm.getDefaultPrivacyFlg(userMst.getUserId());
//							
//						 feedObj.setOriginatorId(userMst.getUserId());
//						 feedObj.setEntryBy(userMst.getUserId());																												
//						 feedObj.setFeedStatus(1);
//						 feedObj.setFeedPrivacyFlg(1);							
//						 feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
//						 feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
//						 feedObj.setIsMyfeed(1);
//						 feedObj.setIsShared(0);
							
						 //additional info
						 JSONObject additionalData = getAdditionalFeedInfo(facilityBookingobj, userMst);
						 feedObj.setAdditionalData(additionalData.toString());
							
						 Commonutility.toWriteConsole("BookingUtility - Before feedEditPrivacyflag getFeedId:"+feedObj.getFeedId());
						 feadhbm.feedEditPrivacyflag(feedObj, "", null, null, null, null);
						 Commonutility.toWriteConsole("BookingUtility - After feedEditPrivacyflag getFeedId:"+feedObj.getFeedId());
						 
						 response[0] = "";
					 }
					 else{				 
						 Commonutility.toWriteConsole("BookingUtility - feedInsertPrivacyflag");
						 
						feedObj = new FeedsTblVO();
						feedObj.setUsrId(userMst);
						int feedTypFlg = 77; // facility booking - category_mst_tbl
						feedObj.setFeedType(feedTypFlg);
						feedObj.setFeedTypeId(bookingid);
						feedObj.setFeedTitle(facilityBookingobj.getTitle());
						feedObj.setFeedCategory("Facility Booking");
						feedObj.setFeedDesc(facilityBookingobj.getDescription());
						feedObj.setPostBy(userMst.getUserId());
						
						feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
						feedObj.setSocietyId(userMst.getSocietyId());
						if(userMst.getCityId()!=null){
							feedObj.setFeedLocation(userMst.getCityId().getCityName());
						}
						
						feedObj.setOriginatorId(userMst.getUserId());
						feedObj.setEntryBy(userMst.getUserId());																												
						feedObj.setFeedStatus(1);
						feedObj.setFeedPrivacyFlg(1);							
						feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setIsMyfeed(1);
						feedObj.setIsShared(0);
						
						//additional info
						JSONObject additionalData = getAdditionalFeedInfo(facilityBookingobj, userMst);
						feedObj.setAdditionalData(additionalData.toString());
						
						System.out.println("BookingUtility - Before feedInsertPrivacyflag");
						int retFeedId = feadhbm.feedInsertPrivacyflag(null, feedObj, null, null, null,Integer.parseInt(userprivacy),userArray.toString());
						response[0] = retFeedId+"";
						System.out.println("BookingUtility - Before feedInsertPrivacyflag retFeedId:"+retFeedId);
						//end: add feed info
					 }
				 }
				 else{
					 response[0] = "";
				 }
				 
				 response[1] = "success";
				 return response;
//				 return "success";
			 }else{
				 response[0] = "";
				 response[1] = "error";
				 return response;
//				 return "error";
			 }	
		} catch (Exception e) {
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ Thread.currentThread().getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			log.logMessage("Exception : "+e, "error", BookingUtility.class);
//			return "error";
			response[1] = "error";
			return response;
		} finally {
			if (lvrHbsession!=null){lvrHbsession.flush();lvrHbsession.clear();lvrHbsession.close();lvrHbsession = null;}
			 log=null; locrestObj=null;locvrstatus=null;locvrcomment=null;
			 locvrBOOKING_ID=null; 	
			 locrestUpdqry=null;locrestUpdSts=false;				
		}
	}
	
	public static JSONObject getAdditionalFeedInfo(FacilityBookingTblVO facilityObj, UserMasterTblVo userMst) {
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		JSONObject finalJsonarr = new JSONObject();
		CommonUtils commonjvm=new CommonUtils();

		try {
			finalJsonarr.put("booking_id", facilityObj.getBookingId());
			finalJsonarr.put("title", facilityObj.getTitle());
			finalJsonarr.put("place", facilityObj.getPlace());

			finalJsonarr.put("start_time", commonjvm.dateToStringModify(
					facilityObj.getStartTime(), dateFormat));
			finalJsonarr.put("end_time", commonjvm.dateToStringModify(
					facilityObj.getEndTime(), dateFormat));
			if (userMst.getMobileNo() != null) {
				finalJsonarr.put("contactno", userMst.getMobileNo());
			} else {
				finalJsonarr.put("contactno", "");
			}

			finalJsonarr.put("desc", facilityObj.getDescription());
			finalJsonarr.put("facility_id", ""
					+ facilityObj.getFacilityId().getFacilityId());
			finalJsonarr.put("booking_status",
					"" + facilityObj.getBookingStatus());
			if (facilityObj.getCommiteecomment() != null) {
				finalJsonarr.put("committee_desc",
						facilityObj.getCommiteecomment());
			} else {
				finalJsonarr.put("committee_desc", "");
			}
		} catch (Exception e) {
			System.out
					.println("=======Facility Booking getAdditionalFeedInfo ====Exception===="+e.getMessage());
			e.printStackTrace();
		}
		return finalJsonarr;
	}
}
	
	
	
	


