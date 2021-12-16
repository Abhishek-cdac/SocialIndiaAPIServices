package com.mobile.login.verify;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.siservices.login.EncDecrypt;
import com.social.utils.Log;

public class verifyEmail extends  ActionSupport{
	Log log=new Log();	
	private String EX0iOFM0RiD0;   //  email id 
	private String FN0EM0oGL;      //flag
	private String Umo0MqzFIg;   //user id
	profileDao profile=new profileDaoServices();
	JSONObject locObjRspdataJson=null;
	private String toStringval=null;
	private String actionurl=null;
	public String execute(){
		try{
		if(EX0iOFM0RiD0!=null && !EX0iOFM0RiD0.equalsIgnoreCase("")&& !EX0iOFM0RiD0.equalsIgnoreCase("null")){	
		String email = EncDecrypt.decrypt(EX0iOFM0RiD0);
		String flag = EncDecrypt.decrypt(FN0EM0oGL);
		String userid = EncDecrypt.decrypt(Umo0MqzFIg);
		boolean checkUserDetail=profile.emailExistCheck(email,flag,userid);
		System.out.println("=========verify email============================");
		if(checkUserDetail==true){
		boolean result=profile.emailVerifivcation(email,flag,userid);
		if(result==true){
			locObjRspdataJson=new JSONObject();
			locObjRspdataJson.put("servicecode", "00");
			locObjRspdataJson.put("respCode", "R0001");
			locObjRspdataJson.put("message", mobiCommon.getMsg("R0001"));
			toStringval=locObjRspdataJson.toString().trim();
			actionurl= System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.login.url") + getText("web.project.name") + "societyprelog";
			System.out.println("======email verify success====="+toStringval);
			/*	servicecode="00";
			statusCode="R0001";*/
			//serverResponse("servicecode","00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
		}
		}else{
			System.out.println("======error in user email verify=====");
			locObjRspdataJson=new JSONObject();
			locObjRspdataJson.put("servicecode", "01");
			locObjRspdataJson.put("respCode", "R0020");
			locObjRspdataJson.put("message", mobiCommon.getMsg("R0020"));
			toStringval=locObjRspdataJson.toString().trim();
			actionurl= System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.login.url") + getText("web.project.name") + "societyprelog";
			System.out.println("======email verify Error====="+toStringval);
			//serverResponse("servicecode","01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
			}
		}else{
			mobiCommon mobicomn=new mobiCommon();
			locObjRspdataJson=new JSONObject();
			locObjRspdataJson.put("servicecode", "01");
			locObjRspdataJson.put("respCode", "R0020");
			locObjRspdataJson.put("message", mobicomn.getMsg("R0020"));
			toStringval=locObjRspdataJson.toString().trim();
			actionurl= System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.login.url") + getText("web.project.name") + "societyprelog";
			System.out.println("======email verify Error====="+toStringval);
		
		}
			
		}catch(Exception ex){
			
			System.out.println("==Exception===verifyEmail==="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", verifyEmail.class);
		}
		
		return SUCCESS;
	}

	public String getEX0iOFM0RiD0() {
		return EX0iOFM0RiD0;
	}

	public void setEX0iOFM0RiD0(String eX0iOFM0RiD0) {
		EX0iOFM0RiD0 = eX0iOFM0RiD0;
	}

	public String getFN0EM0oGL() {
		return FN0EM0oGL;
	}

	public void setFN0EM0oGL(String fN0EM0oGL) {
		FN0EM0oGL = fN0EM0oGL;
	}

	public String getUmo0MqzFIg() {
		return Umo0MqzFIg;
	}

	public void setUmo0MqzFIg(String umo0MqzFIg) {
		Umo0MqzFIg = umo0MqzFIg;
	}
	
	
	public String getToStringval() {
		return toStringval;
	}

	public void setToStringval(String toStringval) {
		this.toStringval = toStringval;
	}


	public String getActionurl() {
		return actionurl;
	}

	public void setActionurl(String actionurl) {
		this.actionurl = actionurl;
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

}
