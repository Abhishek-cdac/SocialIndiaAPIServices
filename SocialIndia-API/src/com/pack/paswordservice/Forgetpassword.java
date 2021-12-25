package com.pack.paswordservice;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.login.EncDecrypt;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class Forgetpassword extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	Log log = new Log();
	SmsInpojo sms = new SmsInpojo();
	private SmsTemplatepojo smsTemplate;
	private SmsEngineServices smsService = new SmsEngineDaoServices();
	UserMasterTblVo userDetails = new UserMasterTblVo();

	public String execute() {
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode = null;
		String locsvemail = null;
		CommonUtilsDao common = new CommonUtilsDao();
		try{				
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");						
					locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");					
					locsvemail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailid");										
					CommonUtils comutil=new CommonUtilsServices();
					String locpswd=comutil.getRandomval("AZ_09", 10);
					CommonDao locCmObj=new CommonDao();
					Commonutility.toWriteConsole("New password : "+locpswd);
					userDetails = locCmObj.commoncheck(locsvemail);
					if(userDetails!=null) {			
						int lvrPswrdflg =1;// New Password [User will change - login time redirect change password page]
						String locUpdQry = "update ForgetpasswordTblVO set pswd ='"+common.stringToMD5(locpswd)+"', encryptPswd= '"+EncDecrypt.encrypt(locpswd)+"', ivrBnPASSWORD_FLAG="+lvrPswrdflg+" where emailId = '"+locsvemail+"' or mobileno ='"+locsvemail+"' and ivrBnStatus in (0,1,2)";					
						boolean uprss = locCmObj.commonUpdate(locUpdQry);
						locObjRspdataJson=new JSONObject();
						Commonutility.toWriteConsole("Update Status : "+uprss);
						if (uprss) {
							//Send Email
							EmailEngineServices emailservice = new EmailEngineDaoServices();
							EmailTemplateTblVo emailTemplate;
							String[] qrySplit = null;
							try {
					            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Reset Password' and status ='1'";
					            emailTemplate = emailservice.emailTemplateData(emqry);
					            String emailMessage = emailTemplate.getTempContent();
					            emqry = common.templateParser(emailMessage, 1, "", locpswd);
					            qrySplit = emqry.split("!_!");
					            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.emailId='"+locsvemail+"' or cust.mobileNo='"+locsvemail+"' ";
					            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
					            emailTemplate.setTempContent(emailMessage);
					            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
					            Commonutility.toWriteConsole("userDetails.getEmailId() : "+userDetails.getEmailId());
					            EmailInsertFuntion emailfun = new EmailInsertFuntion();
					            if(userDetails.getEmailId()!=null && !userDetails.getEmailId().equalsIgnoreCase("")&& !userDetails.getEmailId().equalsIgnoreCase("null")){
					            	emailfun.test2(userDetails.getEmailId(), emailTemplate.getSubject(), emailMessage);
					            } else {
					            	emailfun.test2(locsvemail, emailTemplate.getSubject(), emailMessage);
					            }
				            
							} catch (Exception ex) {
				        	  Commonutility.toWriteConsole("Excption found in Emailsend Forgetpassword.class : " + ex);
				        	  log.logMessage("Exception in usercreationFunction() emailFlow----> " + ex, "error",Forgetpassword.class);
				            
				          } finally{
				        	  qrySplit = null;
				          }
						 // Sending SMS
	
				         Commonutility.toWriteConsole("SMS Send will start");
				         String[] smsqrySplit = null;
				          try {
				        	  Commonutility.toWriteConsole("userDetails.getMobileNo() : "+ userDetails.getMobileNo());
				            String mailRandamNumber;
				            mailRandamNumber = common.randInt(5555, 999999999);
				            String qry = "FROM SmsTemplatepojo where templateName ='Reset Password' and status ='1'";
				            smsTemplate = smsService.smsTemplateData(qry);
				            String smsMessage = smsTemplate.getTemplateContent();
				            qry = common.smsTemplateParser(smsMessage, 1, "", locpswd);
				            smsqrySplit = qry.split("!_!");
				            String qryform = smsqrySplit[0] + " FROM UserMasterTblVo as cust where cust.emailId='" + locsvemail + "' or cust.mobileNo='"+locsvemail+"' ";
				            smsMessage = smsService.smsTemplateParserChange(qryform, Integer.parseInt(smsqrySplit[1]), smsMessage);
				            sms.setSmsCardNo(mailRandamNumber);
				            sms.setSmsEntryDateTime(common.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));			            
				            sms.setSmsMobNumber(userDetails.getMobileNo());
				            sms.setSmspollFlag("F");
				            sms.setSmsMessage(smsMessage);
				            smsService.insertSmsInTable(sms);
				            Commonutility.toWriteConsole("SMS send block End");
				          } catch (Exception ex) {			        	  
				        	  Commonutility.toWriteConsole("Exception sms send Forgetpassword :  " + ex);
				              log.logMessage("Exception in Forgetpassword() : " + ex, "error", Forgetpassword.class);
				           
				          }	finally {
				        	  smsqrySplit = null;
				          }		
			          		serverResponse(ivrservicecode,"0","0000",getText("forgot.password.success"),locObjRspdataJson);
						} else {
							Commonutility.toWriteConsole("SMS / Email not send. [Not Updated. Deleted user or some other reason]");
							serverResponse(ivrservicecode,"3","E2001",getText("forgot.password.success"),locObjRspdataJson);
						}

					} else {
						Commonutility.toWriteConsole("SMS / Email not send.");
						locObjRspdataJson=new JSONObject();						
						serverResponse(ivrservicecode,"4","E2001",getText("forgot.password.error"),locObjRspdataJson);
					}
					
					
					
					//serverResponse(ivrservicecode,"0","0000","Send",locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI2002, Request values are not correct format", "info", Forgetpassword.class);
					serverResponse("SI2002","1","E0001","Request values are not correct format",locObjRspdataJson);
				}
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI2002, Request values are empty", "info", Forgetpassword.class);
				serverResponse("SI2002","1","E0001","Request values are empty",locObjRspdataJson);
			}
			
			
		}catch(Exception e){
			Commonutility.toWriteConsole("Service code : SI2002, Exception found in Forgetpassword.action execute() Method : "+e);
			log.logMessage("Service code : SI2002, Sorry, an unhandled error occurred", "info", Forgetpassword.class);
			locObjRspdataJson=new JSONObject();
			serverResponse("SI2002","2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
		}finally{		
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;
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
