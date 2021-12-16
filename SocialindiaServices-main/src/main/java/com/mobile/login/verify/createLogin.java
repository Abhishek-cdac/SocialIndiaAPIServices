package com.mobile.login.verify;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otp.mobRequestOtp;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.otpVo.otpValidateUtillity;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.bitly.GetBitlyLink;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.signUpProcess;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class createLogin extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	UserMasterTblVo userInfo=new UserMasterTblVo();
	profileDao profile=new profileDaoServices();
	MvpFamilymbrTbl familyMst=new MvpFamilymbrTbl();
	CommonUtilsDao common=new CommonUtilsDao();	
	public String execute(){
		
		System.out.println("************createLogin services******************");
		JSONObject json = new JSONObject();
		 otpValidateUtillity otpUtill=new otpValidateUtillity();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONArray jsonarr = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		String servicecode="";
		JSONObject obj = new JSONObject();
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
		jsonarr =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
		String is_social_media = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_social_media");
		String social_type = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "social_type");
		String mobileno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileno");
		String social_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "social_id");
		String social_first_name = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "social_first_name");
		String social_last_name = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "social_last_name");
		String social_picture = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "social_picture");
		String emailid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailid");
		String passwd = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "passwd");
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
		
		if (locObjRecvdataJson !=null) {
			if (Commonutility.checkempty(emailid)) {
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("emailid.error")));
			}
		}
		boolean checkResult = false;
		 String userid =new String();
		if(flg){
			boolean result=otp.checkTownshipKeyWithoutRid(townshipKey);
			System.out.println("********result*****************"+result);
			System.out.println("==========jsonarr========"+jsonarr);
			if(result==true){
			/*boolean societyKeyCheck=otp.checkSocietyKeyWithoutRid(societykey);
				if(societyKeyCheck==true){*/
				/*userInfo=profile.checkSiLoginDetails(mobileno,emailid);
				if(userInfo!=null){*/
				if(is_social_media.equalsIgnoreCase("1")){
					System.out.println("ds=fds=f=f=s==");
					for(int i=0;i<jsonarr.length();i++){
						userid=null;
						 userid=jsonarr.getString(i);
						System.out.println("==userid==="+userid);
					 checkResult=profile.checkCreateLoginDetails(social_type,social_id,social_first_name,social_last_name,social_picture,emailid,passwd,"",Integer.parseInt(userid));
					System.out.println("=========checkResult========="+checkResult);
					}
					if(checkResult==true){
						
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"00","R0034",mobiCommon.getMsg("R0034"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0018",mobiCommon.getMsg("R0018"),locObjRspdataJson);
					}
					
				}else if(is_social_media.equalsIgnoreCase("2")){
					userInfo=profile.checkSiLoginDetails(mobileno,emailid);
					if(userInfo!=null){
						for(int i=0;i<jsonarr.length();i++){
							userid=null;
							 userid=jsonarr.getString(i);
							System.out.println("==userid==="+userid);
					 checkResult=profile.checkSICreateLoginDetails(mobileno,emailid,passwd,Integer.parseInt(userid));
						}
					if(checkResult==true){
					System.out.println("=====userInfo email ==="+userInfo.getEmailId());
					String emailLink=System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.email.verify.link");
					String encryptEmail = EncDecrypt.encrypt(emailid);
					String encryptflg = EncDecrypt.encrypt("1");
					String encryptuserid = EncDecrypt.encrypt(String.valueOf(userInfo.getUserId()));
					String finalEmail =URLEncoder.encode(encryptEmail);
					String finalFlag=URLEncoder.encode(encryptflg);
					String finalUserId =URLEncoder.encode(encryptuserid);
					System.out.println("=====userInfo.getUserId()==="+encryptuserid);
					String finalLink=emailLink+"?"+"EX0iOFM0RiD0="+finalEmail+"&FN0EM0oGL="+finalFlag+"&Umo0MqzFIg="+finalUserId;
					System.out.println("====finalLink===="+finalLink);
					String lvrName = "";
					if (Commonutility.checkempty(userInfo.getFirstName())) {
						lvrName = userInfo.getFirstName();
					}
					if (Commonutility.checkempty(userInfo.getLastName())) {
						lvrName += " " +userInfo.getLastName();
					}
					if (lvrName!=null && lvrName.trim().length()<=0) {
						lvrName = "Resident";
					} else {
						lvrName = lvrName.trim();
					}
					//Send Email
					// bitly url call
					String locbityurl = GetBitlyLink.toGetBitlyLinkMthd(finalLink, "yes", getText("bitly.accesstocken"), getText("bitly.server.url"));
					System.out.println("===bitly==="+locbityurl);
					if(emailid!=null){
					EmailEngineServices emailservice = new EmailEngineDaoServices();
					EmailTemplateTblVo emailTemplate;
					
					try {
						
			            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Email Verification' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();
			            
			            emailMessage =  emailMessage.replace("[VERIFYEMAILURL]", locbityurl);
			            emailMessage = emailMessage.replace("[FIRSTNAME]", lvrName);
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(emailid, emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            System.out.println("Excption found in createLogin.class : " + ex);
			            log.logMessage("Exception in createLogin flow emailFlow----> " + ex, "error",signUpProcess.class);
			            
			          }	
					locObjRspdataJson=new JSONObject();
					}
					serverResponse(servicecode,"00","R0007",mobiCommon.getMsg("R0007"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
					}
					
					}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0018",mobiCommon.getMsg("R0018"),locObjRspdataJson);
					}
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
			log.logMessage("Service code : "+servicecode+", Request values are not correct format", "info", createLogin.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======createlogin====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, createLogin an unhandled error occurred", "info", createLogin.class);
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
