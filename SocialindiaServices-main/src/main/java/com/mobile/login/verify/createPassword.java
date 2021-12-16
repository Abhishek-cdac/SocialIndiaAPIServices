package com.mobile.login.verify;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.otpVo.otpValidateUtillity;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class createPassword extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	UserMasterTblVo userInfo=new UserMasterTblVo();
	profileDao profile=new profileDaoServices();
	public String execute(){
		
		System.out.println("************create Password services******************");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		String servicecode="";
		boolean flg = true;
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("======ivrparams========"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
		 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
		//String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
		locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		String passwd = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "passwd");
		String mobileno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileno");
		String otpval = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "otp");
		
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
		/*if (Commonutility.checkempty(societykey)) {
			if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
				
			} else {
				String[] passData = { getText("society.fixed.length") };
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
			}
		} else {
			flg = false;
			locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
		}*/
		
		if (locObjRecvdataJson !=null) {
			if (Commonutility.checkempty(mobileno)) {
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("mobile.error")));
			}
		}
		
		if(flg){
			boolean result=otp.checkTownshipKeyWithoutRid(townshipKey);
			System.out.println("********result*****************"+result);
			if(result==true){
				/*boolean societyKeyCheck=otp.checkSocietyKeyWithoutRid(societykey);
				if(societyKeyCheck==true){*/
				boolean checkResult=profile.checkMobnoOtp(mobileno,otpval);
				System.out.println("========checkResult=="+checkResult);
				if(checkResult==true){
				boolean upPassword=profile.updateNewPassword(mobileno,passwd);	
				CommonUtils locCommutillObj =new CommonUtilsDao();
				 List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
				
				 userInfoList=profile.getRestPasswordUsers(mobileno,passwd);
				 UserMasterTblVo userObj;
					for (Iterator<UserMasterTblVo> it = userInfoList
							.iterator(); it.hasNext();) {
						userObj = it.next();
						if(userObj.getEmailId()!=null && userObj.getEmailId().length()>0){
						EmailEngineServices emailservice = new EmailEngineDaoServices();
						EmailTemplateTblVo emailTemplate;
						try {
			            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Reset Password Mobile' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();
			            
			            emqry = locCommutillObj.templateParser(emailMessage, 1, "", "");
			            String[] qrySplit = emqry.split("!_!");
			            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.userId='"+userObj.getUserId()+"'";
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(userObj.getEmailId(), emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            Commonutility.toWriteConsole("Excption found in Emailsend createPassword.class : " + ex);
			            log.logMessage("Exception in createPassword  flow emailFlow----> " + ex, "error",createPassword.class);
			            
			          }	
						}
					}
				 
				
				if(upPassword==true){
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"00","R0011",mobiCommon.getMsg("R0011"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0019",mobiCommon.getMsg("R0019"),locObjRspdataJson);
				}
					
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0019",mobiCommon.getMsg("R0019"),locObjRspdataJson);	
				}
				
				/*}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0026",getText("R0026"),locObjRspdataJson);
				}*/
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
			log.logMessage("Service code : 0003, Request values are not correct format", "info", createPassword.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("===createPassword===Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, createPassword an unhandled error occurred", "info", createPassword.class);
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
