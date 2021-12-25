package com.mobi.carpooling;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.CarMasterTblVO;
import com.socialindiaservices.vo.CarPoolingTblVO;

public class OfferCarPoolingAddEdit extends ActionSupport {
	Log log=new Log();	
	otpDao otp=new otpDaoService();
	private String ivrparams;
	CarPoolDao carPoolinghbm=new CarPoolDaoServices();
	List<CarPoolingTblVO> carpoolingList=new ArrayList<CarPoolingTblVO>();
	CommonUtils commjvm=new CommonUtils();
	
	public String execute(){
		
		System.out.println("************Offer CarPooling Add Edit services******************");
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
				String carpoolId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "car_pool_id");
				String carModel = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "car_model");
				String carNumber = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "car_number");
				String lookingFor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "look_for");
				String fromLocation = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "from_loc");
				String fromLocationId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "from_place_id");
				String startDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "start_datetime");
				String toLocation = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "to_loc");
				String toLocationId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "to_place_id");
				String endDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "end_datetime");
				String prefference = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "preference");
				String seatsAvaliable=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "seatsAvaliable");
				String isReturnFlag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_return");
				JSONObject frequencyJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "frequency");
				String freqmode = (String) Commonutility.toHasChkJsonRtnValObj(frequencyJson, "mode");
				String freqdays = (String) Commonutility.toHasChkJsonRtnValObj(frequencyJson, "days");
				String additionalInfo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "add_info");
				String add_edit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "add_edit");
				
				JSONArray userArray =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");
				String userprivacy=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "privacy");
				String feedId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "feed_id");
				
				System.out.println("carNumber------------------"+carNumber);
				if(carNumber!=null){
					carNumber=carNumber.replaceAll("\\s", "");
					System.out.println("carNumber------------------"+carNumber);
				}
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
				if (carNumber !=null) {
					flg =commjvm.isAlphaNumeric(carNumber);
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("invalid.carNumber")));
				}
				
				if(flg){
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int count=0;String locVrSlQry="";
			CarPoolingTblVO carpoolobj=new CarPoolingTblVO();
			CarMasterTblVO carMasterObj=new CarMasterTblVO();
			Date dt=new Date();
			if(add_edit.equalsIgnoreCase("1")){
				carMasterObj.setCarModel(carModel);
				carMasterObj.setCarNumber(carNumber);
				carMasterObj.setPreference(prefference);
				carMasterObj.setStatus(1);
				carMasterObj.setEntryDateTime(dt);
				carMasterObj.setEntryByGroup(userMst.getGroupCode());
				carMasterObj.setEntryBy(userMst);
				
				
				int societyId=userMst.getSocietyId().getSocietyId();
				carpoolobj.setYouWillBe(Integer.parseInt(lookingFor));
				carpoolobj.setReturnTrip(Integer.parseInt(isReturnFlag));
				carpoolobj.setTripFromCity(fromLocation);
				carpoolobj.setTripfromPlaceId(fromLocationId);
				carpoolobj.setTripToCity(toLocation);
				carpoolobj.setTriptoPlaceId(toLocationId);
				carpoolobj.setTripStartDate(commjvm.stringTODateParser(startDate, "yyyy-MM-dd HH:mm:ss"));
				carpoolobj.setTripEndDate(commjvm.stringTODateParser(endDate, "yyyy-MM-dd HH:mm:ss"));
				carpoolobj.setDays(freqdays);
				carpoolobj.setTripFrequency(Integer.parseInt(freqmode));
				carpoolobj.setAdditionalinfo(additionalInfo);
				carpoolobj.setSeatsAvaliable(Integer.parseInt(seatsAvaliable));
				carpoolobj.setStatus(1);
				carpoolobj.setEntryByGroup(userMst.getGroupCode());
				carpoolobj.setEntryBy(userMst);
				carpoolobj.setEntryDateTime(dt);
				
				int carPoolingId=carPoolinghbm.saveCarPoolingObject(carpoolobj,carMasterObj);
				System.out.println("=======count======="+count);
				
				//:start Insert into feed
				FeedDAO  feadhbm=new FeedDAOService();
				FeedsTblVO feedObj = new FeedsTblVO();
				feedObj.setUsrId(userMst);
				int feedTypFlg = 76; // car pooling - category_mst_tbl
				feedObj.setFeedType(feedTypFlg);
				feedObj.setFeedTypeId(carPoolingId);
				feedObj.setFeedTitle("Offer Car Pooling");
				feedObj.setFeedCategory("Car Pooling");
				feedObj.setFeedDesc(additionalInfo);
				feedObj.setPostBy(Integer.parseInt(rid));
				
				feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
				feedObj.setSocietyId(userMst.getSocietyId());
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
				
				//additional info
				JSONObject additionalData = getAdditionalFeedInfo(carpoolobj);
				feedObj.setAdditionalData(additionalData.toString());
				
				int retFeedId = feadhbm.feedInsertPrivacyflag(null, feedObj, null, null, null,Integer.parseInt(userprivacy),userArray.toString());
				
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
						notificationHbm.insertnewNotificationDetails(userMasteriterObj, "", 1, 1, carPoolingId, userMst, additionaldata);
						}
				}
				
				//:end Insert into Feed
				
				locObjRspdataJson=new JSONObject();
				if(carPoolingId>0){
					String locTimeStamp=Commonutility.timeStampRetStringVal();
					locVrSlQry="from CarPoolingTblVO where poolId="+carPoolingId;
					carpoolingList=carPoolinghbm.getCarPoolingList(locVrSlQry,"0",getText("total.row"),locTimeStamp);
					System.out.println("=======count======="+count);
					//docMangList=infoLibrary.getDocumentList(rid,timestamp,startlimit,getText("total.row"),societyId);
					System.out.println("=========userSkillList======="+carpoolingList.size());
					locObjRspdataJson=new JSONObject();
						CarPoolingTblVO carPoolingObj;
						String formatToString="yyyy-MM-dd HH:mm:ss";
						for(Iterator<CarPoolingTblVO> it=carpoolingList.iterator();it.hasNext();){
							carPoolingObj=it.next();
							System.out.println("============fffffffffffff="+carPoolingObj.getCarId().getCarNumber());
							locObjRspdataJson.put("car_number", carPoolingObj.getCarId().getCarNumber());
							locObjRspdataJson.put("car_model", carPoolingObj.getCarId().getCarModel());
							locObjRspdataJson.put("preference", carPoolingObj.getCarId().getPreference());
							String name="";
							if(carPoolingObj!=null && carPoolingObj.getEntryBy().getFirstName()!=null){
								name=carPoolingObj.getEntryBy().getFirstName();
							}else if(carPoolingObj!=null && carPoolingObj.getEntryBy().getLastName()!=null){
								if(!name.equalsIgnoreCase("")){
									name=" "+carPoolingObj.getEntryBy().getLastName();
								}else{
									name=carPoolingObj.getEntryBy().getLastName();
								}
							}
							locObjRspdataJson.put("usr_name", name);
							locObjRspdataJson.put("usr_id", ""+carPoolingObj.getEntryBy().getUserId());
							String externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+carPoolingObj.getEntryBy().getUserId() +"/";
							if(carPoolingObj!=null && carPoolingObj.getEntryBy()!=null &&carPoolingObj.getEntryBy().getImageNameMobile()!=null && carPoolingObj.getEntryBy().getImageNameMobile().length()>0){
								locObjRspdataJson.put("usr_pic",  externalUserImagePath+Commonutility.stringToStringempty(carPoolingObj.getEntryBy().getImageNameMobile()));
							}else if(carPoolingObj!=null && carPoolingObj.getEntryBy()!=null &&carPoolingObj.getEntryBy().getSocialProfileUrl()!=null && carPoolingObj.getEntryBy().getSocialProfileUrl().length()>0){
								locObjRspdataJson.put("usr_pic",  Commonutility.stringToStringempty(carPoolingObj.getEntryBy().getSocialProfileUrl()));
								}else{
									locObjRspdataJson.put("usr_pic","");
								}
							if(carPoolingObj.getEntryBy().getCityId()!=null){
								locObjRspdataJson.put("location", carPoolingObj.getEntryBy().getCityId().getCityName());
							}else{
								locObjRspdataJson.put("location", "");
							}
							if(carPoolingObj.getEntryBy().getEmailId()!=null){
								locObjRspdataJson.put("email", carPoolingObj.getEntryBy().getEmailId());
							}else{
								locObjRspdataJson.put("email", "");
							}
							locObjRspdataJson.put("contact_no", carPoolingObj.getEntryBy().getMobileNo());
							locObjRspdataJson.put("pool_id", ""+carPoolingObj.getPoolId());
							locObjRspdataJson.put("trip_from", carPoolingObj.getTripFromCity());
							locObjRspdataJson.put("trip_to", carPoolingObj.getTripToCity());
							locObjRspdataJson.put("driver_passnger", ""+carPoolingObj.getYouWillBe());
							locObjRspdataJson.put("desc", carPoolingObj.getAdditionalinfo());
							locObjRspdataJson.put("is_return_trip", Integer.toString(carPoolingObj.getReturnTrip()));
							locObjRspdataJson.put("trip_frequency", ""+carPoolingObj.getTripFrequency());
							locObjRspdataJson.put("trip_days", carPoolingObj.getDays());
							if(carPoolingObj.getTripfromPlaceId()!=null && carPoolingObj.getTripfromPlaceId().length()>0){
							locObjRspdataJson.put("from_place_id", carPoolingObj.getTripfromPlaceId());
							}else{
								locObjRspdataJson.put("from_place_id", "");
							}
							if(carPoolingObj.getTriptoPlaceId()!=null && carPoolingObj.getTriptoPlaceId().length()>0){
								locObjRspdataJson.put("to_place_id", carPoolingObj.getTriptoPlaceId());
								}else{
									locObjRspdataJson.put("to_place_id", "");
								}
							
							if(carPoolingObj.getTripStartDate()!=null){
							locObjRspdataJson.put("trip_start_date", commjvm.dateToString(carPoolingObj.getTripStartDate(),formatToString));
							}else{
								locObjRspdataJson.put("trip_start_date", "");
							}
							if(carPoolingObj.getTripEndDate()!=null){
							locObjRspdataJson.put("trip_end_date", commjvm.dateToString(carPoolingObj.getTripEndDate(),formatToString));
							}else{
								locObjRspdataJson.put("trip_end_date","");
							}
						}
					
					locObjRspdataJson.put("feed_id", retFeedId+"");
					
					serverResponse(servicecode,"00", "R0247", "Carpool updated successfully", locObjRspdataJson);
				}else{
					if(carPoolingId==-2){
					locObjRspdataJson=new JSONObject();
					locErrorvalStrBuil=new StringBuilder();
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("carnumber.already.registered")));
					locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
					serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);
					}
				}
			}else if(add_edit.equalsIgnoreCase("2")){
				if(carpoolId!=null){
					int societyId=userMst.getSocietyId().getSocietyId();
					
					carMasterObj.setCarModel(carModel);
					carMasterObj.setCarNumber(carNumber);
					carMasterObj.setPreference(prefference);
					carMasterObj.setStatus(1);
					carMasterObj.setEntryDateTime(dt);
					carMasterObj.setEntryByGroup(userMst.getGroupCode());
					carMasterObj.setEntryBy(userMst);
					
					carpoolobj.setCarId(carMasterObj);
					
					carpoolobj.setPoolId(Integer.parseInt(carpoolId));
					carpoolobj.setYouWillBe(Integer.parseInt(lookingFor));
					carpoolobj.setReturnTrip(Integer.parseInt(isReturnFlag));
					carpoolobj.setTripFromCity(fromLocation);
					carpoolobj.setTripfromPlaceId(fromLocationId);
					carpoolobj.setTripToCity(toLocation);
					carpoolobj.setTriptoPlaceId(toLocationId);
					carpoolobj.setTripStartDate(commjvm.stringTODateParser(startDate, "yyyy-MM-dd HH:mm:ss"));
					carpoolobj.setTripEndDate(commjvm.stringTODateParser(endDate, "yyyy-MM-dd HH:mm:ss"));
					carpoolobj.setDays(freqdays);
					carpoolobj.setTripFrequency(Integer.parseInt(freqmode));
					carpoolobj.setAdditionalinfo(additionalInfo);
					carpoolobj.setSeatsAvaliable(Integer.parseInt(seatsAvaliable));
					carpoolobj.setStatus(1);
					carpoolobj.setEntryByGroup(userMst.getGroupCode());
					carpoolobj.setEntryBy(userMst);
					carpoolobj.setEntryDateTime(dt);
					
				
				System.out.println("carpoolId--------"+carpoolId);
				boolean failBookingId=carPoolinghbm.updateCarPoolingObject(carpoolobj,carMasterObj);
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
						int feedTypFlg = 76; // car pooling - category_mst_tbl
						feedObj.setFeedType(feedTypFlg);
						feedObj.setFeedTypeId(Integer.parseInt(carpoolId));
						feedObj.setFeedTitle("Offer Car Pooling");
						feedObj.setFeedCategory("Car Pooling");
						feedObj.setFeedDesc(additionalInfo);
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
						JSONObject additionalData = getAdditionalFeedInfo(carpoolobj);
						feedObj.setAdditionalData(additionalData.toString());
						
						System.out.println("updating feed table");
						feadhbm.feedEditPrivacyflag(feedObj, "", null, null, null, null);
						
						
						FeedDAO feedHbm=new FeedDAOService();
						feedObj=feedHbm.feedData(Integer.parseInt(feedId));
						NotificationDao notificationHbm=new NotificationDaoServices();
						AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
						String additionaldata=additionalDatafunc.formAdditionalDataForFeedTbl(feedObj);
						System.out.println("additionaldata-------------------------"+additionaldata);
						notificationHbm.updateNotificationDetails(1,Integer.parseInt(carpoolId),additionaldata );
					}
				}
				
				//end: update into feed
				
				locObjRspdataJson=new JSONObject();
				if(failBookingId){
					String locTimeStamp=Commonutility.timeStampRetStringVal();
					locVrSlQry="from CarPoolingTblVO where poolId="+carpoolobj.getPoolId();
					carpoolingList=carPoolinghbm.getCarPoolingList(locVrSlQry,"0",getText("total.row"),locTimeStamp);
					System.out.println("=======count======="+count);
					//docMangList=infoLibrary.getDocumentList(rid,timestamp,startlimit,getText("total.row"),societyId);
					System.out.println("=========userSkillList======="+carpoolingList.size());
					JSONObject finalJsonarr=new JSONObject();
					locObjRspdataJson=new JSONObject();
						CarPoolingTblVO carPoolingObj;
						String formatToString="yyyy-MM-dd HH:mm:ss";
						for(Iterator<CarPoolingTblVO> it=carpoolingList.iterator();it.hasNext();){
							carPoolingObj=it.next();
							System.out.println("============fffffffffffff="+carPoolingObj.getCarId().getCarNumber());
							locObjRspdataJson.put("car_number", carPoolingObj.getCarId().getCarNumber());
							locObjRspdataJson.put("car_model", carPoolingObj.getCarId().getCarModel());
							locObjRspdataJson.put("preference", carPoolingObj.getCarId().getPreference());
							String name="";
							if(carPoolingObj!=null && carPoolingObj.getEntryBy().getFirstName()!=null){
								name=carPoolingObj.getEntryBy().getFirstName();
							}else if(carPoolingObj!=null && carPoolingObj.getEntryBy().getLastName()!=null){
								if(!name.equalsIgnoreCase("")){
									name=" "+carPoolingObj.getEntryBy().getLastName();
								}else{
									name=carPoolingObj.getEntryBy().getLastName();
								}
							}
							locObjRspdataJson.put("usr_name", name);
							locObjRspdataJson.put("usr_id", ""+carPoolingObj.getEntryBy().getUserId());
							String externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+carPoolingObj.getEntryBy().getUserId() +"/";
							if(carPoolingObj!=null && carPoolingObj.getEntryBy()!=null &&carPoolingObj.getEntryBy().getImageNameMobile()!=null && carPoolingObj.getEntryBy().getImageNameMobile().length()>0){
							locObjRspdataJson.put("usr_pic",  externalUserImagePath+Commonutility.stringToStringempty(carPoolingObj.getEntryBy().getImageNameMobile()));
							}else if(carPoolingObj!=null && carPoolingObj.getEntryBy()!=null &&carPoolingObj.getEntryBy().getSocialProfileUrl()!=null && carPoolingObj.getEntryBy().getSocialProfileUrl().length()>0){
								locObjRspdataJson.put("usr_pic",  Commonutility.stringToStringempty(carPoolingObj.getEntryBy().getSocialProfileUrl()));
								}else{
									locObjRspdataJson.put("usr_pic","");
								}
							if(carPoolingObj.getEntryBy().getCityId()!=null){
							locObjRspdataJson.put("location", carPoolingObj.getEntryBy().getCityId().getCityName());
							}else{
								locObjRspdataJson.put("location", "");
							}
							if(carPoolingObj.getEntryBy().getEmailId()!=null){
								locObjRspdataJson.put("email", carPoolingObj.getEntryBy().getEmailId());
							}else{
								locObjRspdataJson.put("email", "");
							}
							locObjRspdataJson.put("contact_no", carPoolingObj.getEntryBy().getMobileNo());
							locObjRspdataJson.put("pool_id", ""+carPoolingObj.getPoolId());
							locObjRspdataJson.put("trip_from", carPoolingObj.getTripFromCity());
							locObjRspdataJson.put("trip_to", carPoolingObj.getTripToCity());
							locObjRspdataJson.put("driver_passnger", ""+carPoolingObj.getYouWillBe());
							locObjRspdataJson.put("desc", carPoolingObj.getAdditionalinfo());
							locObjRspdataJson.put("is_return_trip", Integer.toString(carPoolingObj.getReturnTrip()));
							locObjRspdataJson.put("trip_frequency", ""+carPoolingObj.getTripFrequency());
							locObjRspdataJson.put("trip_days", carPoolingObj.getDays());
							if(carPoolingObj.getTripStartDate()!=null){
							locObjRspdataJson.put("trip_start_date", commjvm.dateToString(carPoolingObj.getTripStartDate(),formatToString));
							}else{
								locObjRspdataJson.put("trip_start_date", "");
							}
							if(carPoolingObj.getTripEndDate()!=null){
							locObjRspdataJson.put("trip_end_date", commjvm.dateToString(carPoolingObj.getTripEndDate(),formatToString));
							}else{
								locObjRspdataJson.put("trip_end_date","");
							}
						}
						
					serverResponse(servicecode,"00","R0228",mobiCommon.getMsg("R0228"),locObjRspdataJson);
					
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", OfferCarPoolingAddEdit.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======Offer CarPooling Add/Edit====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", OfferCarPoolingAddEdit.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}
		
		return SUCCESS;
	}

	private JSONObject getAdditionalFeedInfo(CarPoolingTblVO carPoolingObj) {
		String formatToString = "yyyy-MM-dd HH:mm:ss";
		JSONObject finalJsonarr = new JSONObject();

		try {
			finalJsonarr.put("car_number", carPoolingObj.getCarId()
					.getCarNumber());
			finalJsonarr.put("car_model", carPoolingObj.getCarId()
					.getCarModel());
			finalJsonarr.put("preference", carPoolingObj.getCarId()
					.getPreference());
			String name = "";
			if (carPoolingObj != null
					&& carPoolingObj.getEntryBy().getFirstName() != null) {
				name = carPoolingObj.getEntryBy().getFirstName();
			} else if (carPoolingObj != null
					&& carPoolingObj.getEntryBy().getLastName() != null) {
				if (!name.equalsIgnoreCase("")) {
					name = " " + carPoolingObj.getEntryBy().getLastName();
				} else {
					name = carPoolingObj.getEntryBy().getLastName();
				}
			}
			finalJsonarr.put("usr_name", name);
			finalJsonarr.put("usr_id", ""
					+ carPoolingObj.getEntryBy().getUserId());
			String externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") 
					+ "/templogo/profile/mobile/"
					+ carPoolingObj.getEntryBy().getUserId() + "/";
			if (carPoolingObj != null
					&& carPoolingObj.getEntryBy() != null
					&& carPoolingObj.getEntryBy().getImageNameMobile() != null
					&& carPoolingObj.getEntryBy().getImageNameMobile().length() > 0) {
				finalJsonarr.put(
						"usr_pic",
						externalUserImagePath
								+ Commonutility
										.stringToStringempty(carPoolingObj
												.getEntryBy()
												.getImageNameMobile()));
			} else if (carPoolingObj != null
					&& carPoolingObj.getEntryBy() != null
					&& carPoolingObj.getEntryBy().getSocialProfileUrl() != null
					&& carPoolingObj.getEntryBy().getSocialProfileUrl()
							.length() > 0) {
				finalJsonarr.put("usr_pic", Commonutility
						.stringToStringempty(carPoolingObj.getEntryBy()
								.getSocialProfileUrl()));
			} else {
				finalJsonarr.put("usr_pic", "");
			}
			if (carPoolingObj.getEntryBy().getCityId() != null) {
				finalJsonarr.put("location", carPoolingObj.getEntryBy()
						.getCityId().getCityName());
			} else {
				finalJsonarr.put("location", "");
			}
			if (carPoolingObj.getEntryBy().getEmailId() != null) {
				finalJsonarr.put("email", carPoolingObj.getEntryBy()
						.getEmailId());
			} else {
				finalJsonarr.put("email", "");
			}
			finalJsonarr.put("contact_no", carPoolingObj.getEntryBy()
					.getMobileNo());
			finalJsonarr.put("pool_id", "" + carPoolingObj.getPoolId());
			finalJsonarr.put("trip_from", carPoolingObj.getTripFromCity());
			finalJsonarr.put("trip_to", carPoolingObj.getTripToCity());
			finalJsonarr.put("driver_passnger",
					"" + carPoolingObj.getYouWillBe());
			finalJsonarr.put("desc", carPoolingObj.getAdditionalinfo());
			finalJsonarr.put("is_return_trip",
					Integer.toString(carPoolingObj.getReturnTrip()));
			finalJsonarr.put("trip_frequency",
					"" + carPoolingObj.getTripFrequency());
			finalJsonarr.put("trip_days", carPoolingObj.getDays());

			if (carPoolingObj.getTripfromPlaceId() != null
					&& carPoolingObj.getTripfromPlaceId().length() > 0) {
				finalJsonarr.put("from_place_id",
						carPoolingObj.getTripfromPlaceId());
			} else {
				finalJsonarr.put("from_place_id", "");
			}
			if (carPoolingObj.getTriptoPlaceId() != null
					&& carPoolingObj.getTriptoPlaceId().length() > 0) {
				finalJsonarr.put("to_place_id",
						carPoolingObj.getTriptoPlaceId());
			} else {
				finalJsonarr.put("to_place_id", "");
			}

			if (carPoolingObj.getTripStartDate() != null) {
				finalJsonarr.put("trip_start_date", commjvm.dateToString(
						carPoolingObj.getTripStartDate(), formatToString));
			} else {
				finalJsonarr.put("trip_start_date", "");
			}
			if (carPoolingObj.getTripEndDate() != null) {
				finalJsonarr.put("trip_end_date", commjvm.dateToString(
						carPoolingObj.getTripEndDate(), formatToString));
			} else {
				finalJsonarr.put("trip_end_date", "");
			}
		} catch (Exception e) {
			System.out
					.println("=======Offer CarPooling getAdditionalFeedInfo ====Exception===="
							+ e);
			log.logMessage(
					"Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred",
					"info", OfferCarPoolingAddEdit.class);
			e.printStackTrace();
		}
		return finalJsonarr;
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