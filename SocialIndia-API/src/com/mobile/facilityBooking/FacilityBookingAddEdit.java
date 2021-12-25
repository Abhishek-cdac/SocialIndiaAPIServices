package com.mobile.facilityBooking;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.carpooling.OfferCarPoolingAddEdit;
import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
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
import com.socialindiaservices.vo.FacilityBookingTblVO;
import com.socialindiaservices.vo.FacilityMstTblVO;

public class FacilityBookingAddEdit extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	
	public String execute(){
		
		System.out.println("************Facility Booking Add Edit services******************");
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
				String facilityId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "facility_id");
				String facilityBookingId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "booking_id");
				String title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "title");
				String place = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "place");
				String description = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "desc");
				String startTime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "start_time");
				String endTime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "end_time");
				//String contactNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "contactno");
				String add_edit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "add_edit");
				
				JSONArray userArray =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");
				String userprivacy=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "privacy");
				String feedId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "feed_id");
				
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
			FacilityBookingTblVO facilityobj=new FacilityBookingTblVO();
			FacilityMstTblVO facilityMasterObj=new FacilityMstTblVO();
			Date dt=new Date();
			
			int retFeedId = 0;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDateTime = df.parse(startTime);
			Date endDateTime = df.parse(endTime);
			if(add_edit.equalsIgnoreCase("1")){
				int failBookingId = 0;
				int chCntQry = 0;
				chCntQry = facilityhbm.checkCntBookingData(rid,startTime,endTime,facilityId);
				if (chCntQry > 0) {
					System.out.println("if chCntQry=============="+chCntQry);
					failBookingId = 0;
				} else {
					facilityMasterObj.setFacilityId(Integer.parseInt(facilityId));
					facilityobj.setFacilityId(facilityMasterObj);
					facilityobj.setTitle(title);
					facilityobj.setPlace(place);
					facilityobj.setDescription(description);
					facilityobj.setStartTime(startDateTime);
					facilityobj.setEndTime(endDateTime);
					/*if(userMst.getMobileNo()!=null){
					facilityobj.setContactNo(userMst.getMobileNo());
					}*/
					facilityobj.setStatusFlag(1);
					facilityobj.setEntryDatetime(dt);
					facilityobj.setModifyDatetime(dt);
					facilityobj.setBookedBy(userMst);
					facilityobj.setEntryBy(userMst);
					
					failBookingId=facilityhbm.saveFacilityBookingData(facilityobj);
					System.out.println("=======count======="+count);
					System.out.println("else chCntQry=============="+chCntQry);
					
					//start: add feed info
//					FeedDAO  feadhbm=new FeedDAOService();
//					FeedsTblVO feedObj = new FeedsTblVO();
//					feedObj.setUsrId(userMst);
//					int feedTypFlg = 77; // facility booking - category_mst_tbl
//					feedObj.setFeedType(feedTypFlg);
//					feedObj.setFeedTypeId(failBookingId);
//					feedObj.setFeedTitle(title);
//					feedObj.setFeedCategory("Facility Booking");
//					feedObj.setFeedDesc(description);
//					feedObj.setPostBy(Integer.parseInt(rid));
//					
//					feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
//					feedObj.setSocietyId(userMst.getSocietyId());
//					if(userMst.getCityId()!=null){
//						feedObj.setFeedLocation(userMst.getCityId().getCityName());
//					}
//					
//					feedObj.setOriginatorId(Integer.parseInt(rid));
//					feedObj.setEntryBy(Integer.parseInt(rid));																												
//					feedObj.setFeedStatus(1);
//					feedObj.setFeedPrivacyFlg(1);							
//					feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
//					feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
//					feedObj.setIsMyfeed(1);
//					feedObj.setIsShared(0);
//					
//					//additional info
//					JSONObject additionalData = getAdditionalFeedInfo(facilityobj, userMst);
//					feedObj.setAdditionalData(additionalData.toString());
//					
//					retFeedId = feadhbm.feedInsertPrivacyflag(null, feedObj, null, null, null,Integer.parseInt(userprivacy),userArray.toString());
//					
					//end: add feed info
					
					}
				
					//docMangList=infoLibrary.getDocumentList(rid,timestamp,startlimit,getText("total.row"),societyId);
					
					locObjRspdataJson=new JSONObject();
					if(failBookingId>0) {
						System.out.println("if failBookingId=============="+failBookingId + "=========chCntQry======" + chCntQry);
						CommonMobiDao commonHbm=new CommonMobiDaoService();
						List<UserMasterTblVo> userobjList=new ArrayList<UserMasterTblVo>();
						NotificationDao notificationHbm=new NotificationDaoServices();
						String userQuery="from UserMasterTblVo where societyId.activationKey='"+societykey+"' and societyId.statusFlag=1 and   statusFlag=1 and groupCode.groupName= '"+getText("Committee")+"'";
						userobjList=commonHbm.getUserRegisterDeatils(userQuery);
						UserMasterTblVo userMasteriterObj;
						AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
						String additionaldata=additionalDatafunc.formAdditionalDataForFacilityBookingTbl(facilityobj);
						for(Iterator<UserMasterTblVo> it=userobjList.iterator();it.hasNext();){
							userMasteriterObj=it.next();
							notificationHbm.insertnewNotificationDetails(userMasteriterObj, "", 5, 1, failBookingId, userMst, additionaldata);
						}
						
//						locObjRspdataJson.put("feed_id", retFeedId+"");
						
						serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
					}else{
						System.out.println("else failBookingId=============="+failBookingId + "=========chCntQry======" + chCntQry);
						locObjRspdataJson=new JSONObject();
						if (chCntQry > 0) {
							serverResponse(servicecode,"01","R0250",mobiCommon.getMsg("R0250"),locObjRspdataJson);	
						} else {
							serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);
					    }
					}
			}else if(add_edit.equalsIgnoreCase("2")){
				int chCntQry = 0;
				chCntQry = facilityhbm.checkCntBookingData(rid,startTime,endTime,facilityId);
				if (chCntQry > 0) {
					System.out.println("if chCntQry=============="+chCntQry);
					serverResponse(servicecode,"01","R0250",mobiCommon.getMsg("R0250"),locObjRspdataJson);
					return SUCCESS;
				} 
				
				if(facilityBookingId!=null){
					int societyId=userMst.getSocietyId().getSocietyId();
					facilityMasterObj.setFacilityId(Integer.parseInt(facilityId));
					facilityobj.setBookingId(Integer.parseInt(facilityBookingId));
					facilityobj.setFacilityId(facilityMasterObj);
					facilityobj.setTitle(title);
					facilityobj.setPlace(place);
					facilityobj.setDescription(description);
					facilityobj.setStartTime(startDateTime);
					facilityobj.setEndTime(endDateTime);
					if(userMst.getMobileNo()!=null){
					facilityobj.setContactNo(userMst.getMobileNo());
					}
					facilityobj.setEntryDatetime(dt);
					facilityobj.setModifyDatetime(dt);
					facilityobj.setBookedBy(userMst);
					facilityobj.setEntryBy(userMst);
					
					facilityobj.setStatusFlag(1);
					facilityobj.setCommiteecomment(null);
					facilityobj.setBookingStatus(0);
					
					boolean failBookingId=facilityhbm.updateFacilityBookingData(facilityobj);
					System.out.println("=======count======="+count);
				
					//start: update into feed
				
					FeedsTblVO editfeedObj = new FeedsTblVO();
					FeedDAO  feadhbm=new FeedDAOService();
					if(feedId!=null && feedId.length()>0){
						editfeedObj=feadhbm.feedData(Integer.parseInt(feedId));
						if(editfeedObj!=null){
							FeedsTblVO feedObj = new FeedsTblVO();
							feedObj.setFeedId(Integer.parseInt(feedId));
							feedObj.setUsrId(userMst);
							int feedTypFlg = 77; // facility booking - category_mst_tbl
							feedObj.setFeedType(feedTypFlg);
							feedObj.setFeedTypeId(Integer.parseInt(facilityBookingId));
							feedObj.setFeedTitle(title);
							feedObj.setFeedCategory("Facility Booking");
							feedObj.setFeedDesc(description);
							feedObj.setPostBy(Integer.parseInt(rid));
							
							if(userMst.getFirstName()!=null){
								feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
							}
							feedObj.setSocietyId(userMst.getSocietyId());
							
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
							
							//additional info
							JSONObject additionalData = getAdditionalFeedInfo(facilityobj, userMst);
							feedObj.setAdditionalData(additionalData.toString());
							
							System.out.println("updating feed table");
							feadhbm.feedEditPrivacyflag(feedObj, "", null, null, null, null);
							
						}
					}
				
					//end: update into feed
					
					locObjRspdataJson=new JSONObject();
					if(failBookingId){
						
						//For notification
						AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
						String additionaldata=additionalDatafunc.formAdditionalDataForFacilityBookingTbl(facilityobj);
						
						NotificationDao notificationHbm=new NotificationDaoServices();
						notificationHbm.updateNotificationDetails(5, Integer.parseInt(facilityBookingId), additionaldata);
						/*END*/
						
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0058",mobiCommon.getMsg("R0058"),locObjRspdataJson);
					}
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
					}
			}else{
				
			}
				
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======signUpMobile====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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

	private JSONObject getAdditionalFeedInfo(FacilityBookingTblVO facilityObj, UserMasterTblVo userMst) {
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
					.println("=======Facility Booking getAdditionalFeedInfo ====Exception===="
							+ e);
			log.logMessage(
					"Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred",
					"info", OfferCarPoolingAddEdit.class);
			e.printStackTrace();
		}
		return finalJsonarr;
	}
	
	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}