package com.siservices.login;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jettison.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.utils.Log;

public class loginPrevent extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	uamDao uam = new uamDaoServices();
	UserMasterTblVo userInfo = new UserMasterTblVo();

	public String execute() {
		JSONObject locrecvejson = null;
		Log log = null;
		try{
			log = new Log();
			log.logMessage("Step 1 : loginPrevent Service [Start].", "info", loginPrevent.class);
			log.logMessage("System Veriable test" + getText("SOCIAL_INDIA_BASE_URL"), "info", loginPrevent.class);
			Commonutility.toWriteConsole("Step 1 : loginPrevent Service [Start].");
			ivrparams = EncDecrypt.decrypt(ivrparams);
			Commonutility.toWriteConsole("Receive Param : " + ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			Commonutility.toWriteConsole("isJson? : ["+ivIsJson+"]");
			if (ivIsJson) {		
				locrecvejson = new JSONObject(ivrparams);
				String servicecode = locrecvejson.getString("servicecode");
				String username = locrecvejson.getString("userName");
				String password = locrecvejson.getString("password");
				int societyId = locrecvejson.getInt("societyId");
				int flag = locrecvejson.getInt("flag");
				
				String resetLogin = locrecvejson.getString("resetLogin");
				
				if(resetLogin!=null && resetLogin.equalsIgnoreCase("true")){
					uam.resetLogin(username, password, societyId, flag);
				}
				
				userInfo = uam.getUserDetails(username, password, societyId, flag);
				
				if(userInfo != null){
					Commonutility.toWriteConsole("Response userInfo : " + userInfo + " userInfo.getIvrBnISONLINEFLG():"+userInfo.getIvrBnISONLINEFLG() + " userInfo.getLoggedOut():"+userInfo.getLoggedOut());
						
					if(flag == 2){
							org.json.JSONObject jsonObj = new org.json.JSONObject();
							jsonObj.put("online_status", Commonutility.toCheckNullEmpty(userInfo.getIvrBnISONLINEFLG()));
							jsonObj.put("logged_out", Commonutility.toCheckNullEmpty(userInfo.getLoggedOut()));
							jsonObj.put("numOfLogins", userInfo.getNumOfLogins());
							jsonObj.put("currentLogins", userInfo.getCurrentLogins());
							jsonObj.put("resetDatetime", userInfo.getResetDatetime() == null ? "null" : userInfo.getResetDatetime());
							
							Commonutility.toWriteConsole("resetDatetime ->:  " + userInfo.getResetDatetime());
							
							serverResponseMobile(servicecode, "00", "0", "sucess", jsonObj);
					}
					else{
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("online_status", Commonutility.toCheckNullEmpty(userInfo.getIvrBnISONLINEFLG()));
							jsonObj.put("logged_out", Commonutility.toCheckNullEmpty(userInfo.getLoggedOut()));
							jsonObj.put("numOfLogins", userInfo.getNumOfLogins());
							jsonObj.put("currentLogins", userInfo.getCurrentLogins());
							jsonObj.put("resetDatetime", userInfo.getResetDatetime() == null ? "null" : userInfo.getResetDatetime());
							
							serverResponse(servicecode, "0000", "0", "sucess", jsonObj);
					}
				}
				else{
					Commonutility.toWriteConsole("Response userInfo : " + userInfo);
					
					if(flag == 2){
							org.json.JSONObject jsonObj = new org.json.JSONObject();
							jsonObj.put("online_status", 0);
							jsonObj.put("logged_out", "null");
							jsonObj.put("numOfLogins", 2);
							jsonObj.put("currentLogins", 0);
							jsonObj.put("resetDatetime", "null");
							
							Commonutility.toWriteConsole("resetDatetime ->:  " + null);
							
							serverResponseMobile(servicecode, "00", "0", "sucess", jsonObj);
					}
					else{
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("online_status", 0);
							jsonObj.put("logged_out", "null");
							jsonObj.put("numOfLogins", 2);
							jsonObj.put("currentLogins", 0);
							jsonObj.put("resetDatetime", "null");
							
							serverResponse(servicecode, "0000", "0", "sucess", jsonObj);
					}
				}
					
				
			} else {
				JSONObject jsonObject = new JSONObject(ivrparams);
				int flag = jsonObject.getInt("flag");
				
				if(flag == 2){
					org.json.JSONObject _locrecvejson=new org.json.JSONObject();
					serverResponseMobile("SI0012","01","E0001","Request values not correct format", _locrecvejson);
				}
				else{
					locrecvejson=new JSONObject();
					serverResponse("SI0012","1","E0001","Request values not correct format",locrecvejson);
				}
			}
		} catch (Exception ex) {
			try {	
				log.logMessage("Step -1 : Exception Found : "+ex, "error", loginPrevent.class);
				Commonutility.toWriteConsole("Step -1 : Exception Found : "+ex);
				
				JSONObject jsonObject = new JSONObject(ivrparams);
				int flag = jsonObject.getInt("flag");
				
				if(flag == 2){
					org.json.JSONObject _locrecvejson=new org.json.JSONObject();
					serverResponseMobile("SI0012","01","E0001","Request values not not correct format", _locrecvejson);
				}
				else{
					locrecvejson=new JSONObject();
					serverResponse("SI0012","1","E0001","Request values not not correct format",locrecvejson);
				}
			} 
			catch (Exception e) {
			}
		} finally {
				log.logMessage("Step 4 : loginPrevent Service [End]. : ", "info", loginPrevent.class);
		}
		
		return SUCCESS;

	}
	
	public String getResetDatetime() {
		JSONObject locrecvejson = null;
		Log log = null;
		try{
			log = new Log();
			log.logMessage("Step 1 : loginPrevent getResetDatetime [Start].", "info", loginPrevent.class);
			Commonutility.toWriteConsole("Step 1 : loginPrevent getResetDatetime [Start]. ivrparams  => " + ivrparams);
			ivrparams = EncDecrypt.decrypt(ivrparams);
			Commonutility.toWriteConsole("Receive Param : " + ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locrecvejson = new JSONObject(ivrparams);
				String servicecode = locrecvejson.getString("servicecode");
				String userid = locrecvejson.getString("userid");
				
				userInfo = uam.getregistertable(Integer.parseInt(userid));
				Commonutility.toWriteConsole("Response getResetDatetime : "+userInfo.getResetDatetime());
					
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("resetDatetime", userInfo.getResetDatetime() == null ? "null" : userInfo.getResetDatetime());
				
				serverResponse(servicecode, "0000", "0", "sucess", jsonObj);
				
			} else {
					locrecvejson=new JSONObject();
					serverResponse("SI0012","1","E0001","Request values not correct format",locrecvejson);
			}
		} catch (Exception ex) {
			try {	
				log.logMessage("Step -1 : Exception Found getResetDatetime: "+ex, "error", loginPrevent.class);
				Commonutility.toWriteConsole("Step -1 : Exception Found getResetDatetime: "+ex);
				locrecvejson=new JSONObject();
				serverResponse("SI0012","1","E0001","Request values not not correct format",locrecvejson);
			} 
			catch (Exception e) {
			}
		} finally {
				log.logMessage("Step 4 : loginPrevent getResetDatetime [End]. : ", "info", loginPrevent.class);
		}
		
		return SUCCESS;

	}
	
	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
			throws Exception {
		PrintWriter out;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response;
		response = ServletActionContext.getResponse();
		out = response.getWriter();
		try {
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			out.print(as);
			out.close();
		} catch (Exception ex) {
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
		}
	}
	
	private void serverResponseMobile(String serviceCode, String statusCode, String respCode, String message, org.json.JSONObject dataJson)
	{
		PrintWriter out=null;
		org.json.JSONObject responseMsg = new org.json.JSONObject();
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
	public UserMasterTblVo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserMasterTblVo userInfo) {
		this.userInfo = userInfo;
	}
}
