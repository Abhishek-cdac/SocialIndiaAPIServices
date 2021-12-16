package com.mobi.carpooling;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gdata.data.Feed;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.CarPoolingTblVO;

public class SearchCarPoolList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	CarPoolDao carPoolinghbm=new CarPoolDaoServices();
	List<CarPoolingTblVO> carpoolingList=new ArrayList<CarPoolingTblVO>();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
	public String execute(){
		
		System.out.println("************mobile Search CarPool List services******************");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("====ivrparams====="+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String isMycarpool = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_mycarpool");
				String lookingFor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "look_for");
				String fromLocation = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "from_loc");
				String fromLocationId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "from_place_id");
				String toLocation = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "to_loc");
				String toLocationId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "to_place_id");
				String depatureDateTime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "depature_datetime");
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				JSONObject frequencyObj =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"frequency");
				String mode = (String) Commonutility.toHasChkJsonRtnValObj(frequencyObj, "mode");
				String days = (String) Commonutility.toHasChkJsonRtnValObj(frequencyObj, "days");
				
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
				
				if(isMycarpool!=null && !isMycarpool.equalsIgnoreCase("1") && !isMycarpool.equalsIgnoreCase("2")){
//					if (Commonutility.checkempty(depatureDateTime)) {
//						
//					} else {
//						flg = false;
//						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("depature.datetime.notempty")));
//					}
//					if (Commonutility.checkempty(fromLocation)) {
//						
//					} else {
//						flg = false;
//						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("from.location.notempty")));
//					}
					if (Commonutility.checkempty(toLocation)) {
						
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("to.location.notempty")));
					}
				}
				
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				
				
				
				if(flg){
					if (Commonutility.checkempty(locTimeStamp)) {
					} else {
						locTimeStamp=Commonutility.timeStampRetStringVal();
					}
					
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int count=0;String locVrSlQry="";
			
				int societyId=userMst.getSocietyId().getSocietyId();
				// defect fix: start
				//String searchField="where status=1 and entryBy.societyId.societyId='"+userMst.getSocietyId().getSocietyId()+"'";
				// defect fix: end
				String searchField="where status=1 ";
				if(fromLocation!=null && fromLocation.length()>0){
					searchField+=" and tripFromCity='"+fromLocation+"'";
				}
				if(fromLocationId!=null && fromLocationId.length()>0){
					searchField+=" and tripfromPlaceId='"+fromLocationId+"'";
				}
				if(toLocation!=null && toLocation.length()>0){
					searchField+=" and tripToCity='"+toLocation+"'";
				}
				if(toLocationId!=null && toLocationId.length()>0){
					searchField+=" and triptoPlaceId='"+toLocationId+"'";
				}
				if(depatureDateTime!=null && depatureDateTime.length()>0){
					// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				    //  Date depDate = df.parse(depatureDateTime);
					searchField+=" and STR_TO_DATE(tripStartDate,'%Y-%m-%d')='"+depatureDateTime+"'";
				}
				if(mode!=null && mode.length()>0){
					searchField+=" and tripFrequency='"+mode+"'";
				}
				if(lookingFor!=null && lookingFor.length()>0){
					searchField+=" and (youWillBe='"+lookingFor+"' or youWillBe='3')";
				}
				if(isMycarpool!=null && isMycarpool.equalsIgnoreCase("1")){
					searchField+=" and entryBy.userId='"+rid+"'";
				}else{
					searchField+=" and entryBy.userId<>'"+rid+"'";
				}
				
				if(days!=null && days.length()>0){
					if(days.contains(",")){
					String[] dayarray=days.split(",",-1);
					searchField+=" and(";
					for(int k=0;k<dayarray.length;k++){
						if(k==0){
						searchField+=" days like ('%"+dayarray[k]+"%')";
						}else{
							searchField+=" or days like ('%"+dayarray[k]+"%')";
						}
					}
					searchField+=") ";
					
					}else{
						searchField+=" and days like ('%"+days+"%')";
					}
				}
				
				String totcountqry="select count(*) from CarPoolingTblVO "+searchField+" and entryDateTime <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
				
				if(isMycarpool!=null && isMycarpool.equalsIgnoreCase("2")){
					totcountqry = "select count(*) from CarPoolingTblVO where status=1 and entryBy.userId <> '"+rid+"' and entryDateTime <= STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
				}
				System.out.println("=======totcountqry======="+totcountqry);
				
				int totalcnt=commonHbm.getTotalCountQuery(totcountqry);
				
				locVrSlQry="from CarPoolingTblVO "+searchField+" and entryDateTime <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') order by poolId desc";
				
				if(isMycarpool!=null && isMycarpool.equalsIgnoreCase("2")){
					locVrSlQry="from CarPoolingTblVO where status=1 and entryBy.userId <> '"+rid+"' and entryDateTime <= STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') order by poolId desc";
				}
				System.out.println("=======locVrSlQry======="+locVrSlQry);
				
				carpoolingList=carPoolinghbm.getCarPoolingList(locVrSlQry,startlimit,getText("total.row"),locTimeStamp);
				System.out.println("=======count======="+count);
				//docMangList=infoLibrary.getDocumentList(rid,timestamp,startlimit,getText("total.row"),societyId);
				System.out.println("=========userSkillList======="+carpoolingList.size());
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				if(carpoolingList!=null && carpoolingList.size()>0){
					FeedDAO feedDao;
					CarPoolingTblVO carPoolingObj;
					String formatToString="yyyy-MM-dd HH:mm:ss";
					for(Iterator<CarPoolingTblVO> it=carpoolingList.iterator();it.hasNext();){
						carPoolingObj=it.next();
						finalJsonarr=new JSONObject();
						System.out.println("============fffffffffffff="+carPoolingObj.getCarId().getCarNumber());
						finalJsonarr.put("car_number", carPoolingObj.getCarId().getCarNumber());
						finalJsonarr.put("car_model", carPoolingObj.getCarId().getCarModel());
						finalJsonarr.put("preference", carPoolingObj.getCarId().getPreference());
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
						finalJsonarr.put("usr_name", name);
						finalJsonarr.put("usr_id", ""+carPoolingObj.getEntryBy().getUserId());
						String externalUserImagePath = System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+carPoolingObj.getEntryBy().getUserId() +"/";
						if(carPoolingObj!=null && carPoolingObj.getEntryBy()!=null &&carPoolingObj.getEntryBy().getImageNameMobile()!=null && carPoolingObj.getEntryBy().getImageNameMobile().length()>0){
						finalJsonarr.put("usr_pic",  externalUserImagePath+Commonutility.stringToStringempty(carPoolingObj.getEntryBy().getImageNameMobile()));
						}else if(carPoolingObj!=null && carPoolingObj.getEntryBy()!=null &&carPoolingObj.getEntryBy().getSocialProfileUrl()!=null && carPoolingObj.getEntryBy().getSocialProfileUrl().length()>0){
							finalJsonarr.put("usr_pic",  Commonutility.stringToStringempty(carPoolingObj.getEntryBy().getSocialProfileUrl()));
							}else{
								finalJsonarr.put("usr_pic","");
							}
						if(carPoolingObj.getEntryBy().getCityId()!=null){
						finalJsonarr.put("location", carPoolingObj.getEntryBy().getCityId().getCityName());
						}else{
							finalJsonarr.put("location", "");
						}
						if(carPoolingObj.getEntryBy().getEmailId()!=null){
							finalJsonarr.put("email", carPoolingObj.getEntryBy().getEmailId());
						}else{
							finalJsonarr.put("email", "");
						}
						finalJsonarr.put("contact_no", carPoolingObj.getEntryBy().getMobileNo());
						finalJsonarr.put("pool_id", ""+carPoolingObj.getPoolId());
						finalJsonarr.put("trip_from", carPoolingObj.getTripFromCity());
						finalJsonarr.put("trip_to", carPoolingObj.getTripToCity());
						finalJsonarr.put("driver_passnger", ""+carPoolingObj.getYouWillBe());
						finalJsonarr.put("desc", carPoolingObj.getAdditionalinfo());
						finalJsonarr.put("is_return_trip", Integer.toString(carPoolingObj.getReturnTrip()));
						finalJsonarr.put("trip_frequency", ""+carPoolingObj.getTripFrequency());
						finalJsonarr.put("trip_days", carPoolingObj.getDays());
						
						if(carPoolingObj.getTripfromPlaceId()!=null && carPoolingObj.getTripfromPlaceId().length()>0){
							finalJsonarr.put("from_place_id", carPoolingObj.getTripfromPlaceId());
							}else{
								finalJsonarr.put("from_place_id", "");
							}
							if(carPoolingObj.getTriptoPlaceId()!=null && carPoolingObj.getTriptoPlaceId().length()>0){
								finalJsonarr.put("to_place_id", carPoolingObj.getTriptoPlaceId());
								}else{
									finalJsonarr.put("to_place_id", "");
								}
							
						/*finalJsonarr.put("from_place_id", carPoolingObj.getTripfromPlaceId());
						finalJsonarr.put("to_place_id", carPoolingObj.getTriptoPlaceId());*/
						if(carPoolingObj.getTripStartDate()!=null){
						finalJsonarr.put("trip_start_date", commjvm.dateToString(carPoolingObj.getTripStartDate(),formatToString));
						}else{
							finalJsonarr.put("trip_start_date", "");
						}
						if(carPoolingObj.getTripEndDate()!=null){
						finalJsonarr.put("trip_end_date", commjvm.dateToString(carPoolingObj.getTripEndDate(),formatToString));
						}else{
							finalJsonarr.put("trip_end_date","");
						}
						
						//get feed id
						feedDao = new FeedDAOService();
						FeedsTblVO feedsTblVO = feedDao.getFeedDetailsByOfferCarpoolId(carPoolingObj.getPoolId());
						if(feedsTblVO !=null && feedsTblVO.getFeedId()!=null){
							finalJsonarr.put("feed_id", feedsTblVO.getFeedId()+"");
						}
						else{
							finalJsonarr.put("feed_id","");
						}
						
						jArray.put(finalJsonarr);
					}
					
				locObjRspdataJson.put("carpools", jArray);
				locObjRspdataJson.put("totalrecords",totalcnt);
				locObjRspdataJson.put("timestamp",locTimeStamp);
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", SearchCarPoolList.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======Search CarPool List====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", SearchCarPoolList.class);
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

	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}