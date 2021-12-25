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
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
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
import com.socialindiaservices.vo.NotificationTblVO;

public class NotificationSendList extends ActionSupport {
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
				String sentStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "sent_status");
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
						String locTimeStamp=Commonutility.timeStampRetStringVal();
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				NotificationDao notifiHbm=new NotificationDaoServices();
				sentStatus="0";
					String totcountqry="select count(*) from NotificationTblVO where userId.userId='"+rid+"' and statusFlag=1 and sentStatus="+sentStatus;
					int totalcnt=commonHbm.getTotalCountQuery(totcountqry);
					String Query="from NotificationTblVO where userId.userId='"+rid+"' and statusFlag=1 and sentStatus="+sentStatus;
					notificationList=notifiHbm.getnotificationlistByQuery(Query,"0",getText("total.row"));
				System.out.println("=======notificationList size======="+notificationList.size());
				JSONObject finalJsonarr=new JSONObject();
				JSONObject notificationObj=new JSONObject();
				JSONObject chatmessageObj=new JSONObject();
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
						finalJsonarr.put("description", ""+notificationiterObj.getDescr());
						jArray.put(finalJsonarr);
						if(i==0){
							sentIds=""+notificationiterObj.getNotificationId();
							i++;
						}else{
							sentIds+=","+notificationiterObj.getNotificationId();
						}
					}
					System.out.println("sentIds----------------------------"+sentIds);
					String updqry="update NotificationTblVO set sentStatus=1 where notificationId in ("+sentIds+")";
					commonHbm.updateTableByQuery(updqry);
				}
					notificationObj.put("notification_list", jArray);
					notificationObj.put("totalrecords",totalcnt);
					notificationObj.put("timestamp",locTimeStamp);
					locObjRspdataJson.put("notification_detail", notificationObj);
				MessageDAO msgService = new MessageDAOService();
				String searchByName="";
				List<Object[]> chatListObj = new ArrayList<Object[]>();
				if(searchByName!=null&& searchByName.length()>0){}else{searchByName="";}
				chatListObj=msgService.getNotificationmessageContactsByProc(userMst.getUserId());
				if (chatListObj != null) {
					JSONObject jsonPack = new JSONObject();
					JSONArray jsonArr=new JSONArray();
					int j=0;
					int loopcnt = 0;
					Object[] objList;
					for(Iterator<Object[]> it=chatListObj.iterator();it.hasNext();) {
						objList = it.next();
						jsonPack = new JSONObject();
						if(j==0){
							chatmessageObj.put("unread_message",objList[13]);
							chatmessageObj.put("unread_chats",objList[14]);
							j++;
						}
						loopcnt++;
						if (loopcnt < 8) { 
						if(objList[0]!=null){
							jsonPack.put("group_name",objList[0]);
						}else{
							jsonPack.put("group_name","");
						}
						/*if(objList[3]!=null){
							jsonPack.put("online_status",objList[3]);
						}else{
							jsonPack.put("online_status","");
						}
						if(objList[4]!=null){
							jsonPack.put("from_id",objList[4]);
						}else{
							jsonPack.put("from_id","");
						}
						if(objList[5]!=null){
							jsonPack.put("to_id",objList[5]);
						}else{
							jsonPack.put("to_id","");
						}*/
						if(objList[6]!=null){
							jsonPack.put("message",objList[6]);
						}else{
							jsonPack.put("message","");
						}
						/*if(objList[10]!=null){
							
							jsonPack.put("message_time",mobiCommon.getchatDateTime((java.util.Date)objList[10]));
						}else{
							jsonPack.put("message_time","");
						}
						
						if(objList[8]!=null){
							jsonPack.put("read_status",objList[8]);
						}else{
							jsonPack.put("read_status","");
						}*/
						
						if(objList[1]!=null && objList[11]!=null && objList[5]!=null && objList[11].toString().equals("3")){
							jsonPack.put("profile_picture", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/chat/mobile/"+objList[5]+"/"+objList[1]);
						}else if(objList[1]!=null && (objList[4]!=null || objList[5]!=null)){
							String useri="";
							if(objList[4]!=null){
								useri=objList[4].toString();
							}else if(objList[5]!=null){
								useri=objList[5].toString();
							}
							jsonPack.put("profile_picture", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+useri+"/"+objList[1]);	
						}else if(objList[2]!=null){
							jsonPack.put("profile_picture", objList[2]);	
						}else{
							jsonPack.put("profile_picture","");
						}
						if(objList[11]!=null) {
							jsonPack.put("chat_type",objList[11]);
							if (Commonutility.stringToInteger(objList[11].toString()) == 2) {
								if (objList[4]!=null) {
									jsonPack.put("group_contact_id",objList[4]);
								} else {
									jsonPack.put("group_contact_id","");
								}
							} else if (Commonutility.stringToInteger(objList[11].toString()) == 3) {
								if(objList[12]!=null) {
									jsonPack.put("group_contact_id",objList[12]);
								} else {
									jsonPack.put("group_contact_id","");
								}
							}
						} else {
							jsonPack.put("chat_type","");
						}
						
						jsonArr.put(jsonPack);
						}
					}
					chatmessageObj.put("messages", jsonArr);
					
					
				}
				
				locObjRspdataJson.put("chatmessage_detail",chatmessageObj);
				
				
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				
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

	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}
