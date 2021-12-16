package com.mobile.profile;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.otpVo.otpValidateUtillity;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.utils.Log;
import com.socialindiaservices.vo.NotificationStatusTblVO;

public class NotificationStatusUpdate extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	public String execute(){
		
		System.out.println("**********Notification Status Update services******************");
		JSONObject json = new JSONObject();
		 otpValidateUtillity otpUtill=new otpValidateUtillity();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		MvpUsrSkillTbl userSkillInfo=new MvpUsrSkillTbl();
		UserMasterTblVo userData=new UserMasterTblVo();
		CategoryMasterTblVO categoryMst=new CategoryMasterTblVO();
		SkillMasterTblVO skillMst=new SkillMasterTblVO();
		profileDao profile=new profileDaoServices();
		String servicecode="";
		boolean flg = true;
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
		
		String notificationStatus = (String) Commonutility .toHasChkJsonRtnValObj(locObjRecvdataJson, "notificationstatus");
		String mobileNumber = (String) Commonutility .toHasChkJsonRtnValObj(locObjRecvdataJson, "mobile_number");
		
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
			Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			int count=0;String locVrSlQry="";
			if(result==true){
				UserMasterTblVo userMst=new UserMasterTblVo();
				userMst=otp.checkSocietyKeyForList(societykey,rid);
				if(userMst!=null){
					System.out.println("mobile number-----------"+userMst.getMobileNo());
					System.out.println("status flag-------------"+userMst.getStatusFlag());
					if(userMst.getMobileNo()!=null && mobileNumber!=null && userMst.getMobileNo().equalsIgnoreCase(mobileNumber) && userMst.getStatusFlag()==1){
						NotificationStatusTblVO notificationStat=new NotificationStatusTblVO();
						 uamDao uam=new uamDaoServices();
						 notificationStat.setMobileNo(mobileNumber);
						 notificationStat.setNotificationFlag(Integer.parseInt(notificationStatus));
						 notificationStat.setEntryDatetime(date1);
						 boolean isInsert=uam.updateNotificationStatus(notificationStat);
						 if(isInsert){
								locObjRspdataJson=new JSONObject();
								serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
							}else{
								locObjRspdataJson=new JSONObject();
								serverResponse(servicecode,"01","R0058",mobiCommon.getMsg("R0058"),locObjRspdataJson);
							}
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0075",mobiCommon.getMsg("R0075"),locObjRspdataJson);
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
			log.logMessage("Service code : 0003, Request values are not correct format", "info", PersonalProfileUpdate.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======PersonalProfileUpdate====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, PersonalProfileUpdate an unhandled error occurred", "info", PersonalProfileUpdate.class);
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
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			System.out.println("=====as==="+as);
			as=EncDecrypt.encrypt(as);
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