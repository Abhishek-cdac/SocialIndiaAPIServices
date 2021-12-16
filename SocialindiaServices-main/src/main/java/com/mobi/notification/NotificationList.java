package com.mobi.notification;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.merchant.MerchantDao;
import com.mobi.merchant.MerchantDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.NotificationStatusTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class NotificationList  extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	MerchantDao merchanthbm=new MerchantDaoServices();
	List<NotificationTblVO> notificationList=new ArrayList<NotificationTblVO>();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
	
	public String execute(){
		
		System.out.println("************mobile Notification List services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		Date timeStamp = null;
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
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
				String autoLoad = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "autoload");
				System.out.println("rid----------------"+rid);
				System.out.println("townshipKey-------------------"+townshipKey);
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
				NotificationDao notifiHbm=new NotificationDaoServices();
				String autoSearch="";
				if(autoLoad!=null && autoLoad.equalsIgnoreCase("1")){
					autoSearch+=" and readStatus=0 and sentStatus=0";
				}
					String totcountqry="select count(*) from NotificationTblVO where userId.userId='"+rid+"' and statusFlag=1"+autoSearch;
					int totalcnt=commonHbm.getTotalCountQuery(totcountqry);
					
					notificationList=notifiHbm.getnotificationlist(Integer.parseInt(rid),startlimit,getText("total.row"),autoSearch);
				System.out.println("=======notificationList size======="+notificationList.size());
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				if(notificationList!=null && notificationList.size()>0){
					NotificationTblVO notificationiterObj;
					String sentIds=""; 
					FunctionUtility functionutil=new FunctionUtilityServices();
					int i=0;
					for(Iterator<NotificationTblVO> it=notificationList.iterator();it.hasNext();){
						notificationiterObj=it.next();
						finalJsonarr=new JSONObject();
						System.out.println("notificationiterObj.getUniqueId()--------------------"+notificationiterObj.getNotificationId());
						finalJsonarr.put("notification_id", notificationiterObj.getNotificationId());
						finalJsonarr.put("read_status", ""+notificationiterObj.getReadStatus());
						finalJsonarr.put("description", ""+notificationiterObj.getDescr());
						finalJsonarr.put("notification_type", ""+notificationiterObj.getTblrefFlag());
						System.out.println("11111111111111");
						if(notificationiterObj.getTblrefFlagType()!=null){
						finalJsonarr.put("notification_subtype", ""+notificationiterObj.getTblrefFlagType());
						}else{
							finalJsonarr.put("notification_subtype", "");
						}
						finalJsonarr.put("notification_type_id", ""+notificationiterObj.getTblrefID());
						System.out.println("222222222222222"+notificationiterObj.getModifyDatetime());
						finalJsonarr.put("notification_time", ""+functionutil.getPostedDateTime(notificationiterObj.getModifyDatetime()));
						if(notificationiterObj.getAdditionalData()!=null){
							finalJsonarr.put("additional_data", ""+notificationiterObj.getAdditionalData());
							}else{
								finalJsonarr.put("additional_data", "");
							}
						String name="";
						
						if(notificationiterObj.getEntryBy()!=null && notificationiterObj.getEntryBy().getFirstName()!=null){
							name=notificationiterObj.getEntryBy().getFirstName();
						}else if(notificationiterObj.getEntryBy()!=null && notificationiterObj.getEntryBy().getLastName()!=null){
							if(!name.equalsIgnoreCase("")){
								name=" "+notificationiterObj.getEntryBy().getLastName();
							}else{
								name=notificationiterObj.getEntryBy().getLastName();
							}
						}
						System.out.println("33333333333"+name);
						finalJsonarr.put("usr_name", name);
						if(notificationiterObj.getEntryBy()!=null && notificationiterObj.getEntryBy().getUserId()>0){
						finalJsonarr.put("usr_id", ""+notificationiterObj.getEntryBy().getUserId());
						}else{
							finalJsonarr.put("usr_id", "");
						}
						
						if(notificationiterObj!=null && notificationiterObj.getEntryBy()!=null && notificationiterObj.getEntryBy().getImageNameMobile()!=null && notificationiterObj.getEntryBy().getImageNameMobile().length()>0 && notificationiterObj.getEntryBy().getUserId()>0){
							String externalUserImagePath = System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+notificationiterObj.getEntryBy().getUserId() +"/";
							finalJsonarr.put("usr_pic",  externalUserImagePath+Commonutility.stringToStringempty(notificationiterObj.getEntryBy().getImageNameMobile()));
						}else if(notificationiterObj!=null && notificationiterObj.getEntryBy()!=null && notificationiterObj.getEntryBy().getSocialProfileUrl()!=null && notificationiterObj.getEntryBy().getSocialProfileUrl().length()>0){
							finalJsonarr.put("usr_pic",  Commonutility.stringToStringempty(notificationiterObj.getEntryBy().getSocialProfileUrl()));
							}else{
								finalJsonarr.put("usr_pic","");
							}
						System.out.println("444444444444444"+name);
						jArray.put(finalJsonarr);
						if(i==0){
							sentIds=""+notificationiterObj.getNotificationId();
							i++;
						}else{
							sentIds+=","+notificationiterObj.getNotificationId();
						}
					}
					
				locObjRspdataJson.put("notification_detail", jArray);
				locObjRspdataJson.put("totalrecords",totalcnt);
				locObjRspdataJson.put("timestamp",locTimeStamp);
				System.out.println("sentIds----------------------------"+sentIds);
				String updqry="update NotificationTblVO set sentStatus=1 where notificationId in ("+sentIds+")";
				commonHbm.updateTableByQuery(updqry);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======Notification List====Exception===="+ex);
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

	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}