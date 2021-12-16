package com.siservices.committeeMgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.bitly.GetBitlyLink;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.siservices.committeeMgmt.persistense.committeeDao;
import com.siservices.committeeMgmt.persistense.committeeServices;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class siCommitteeCreationAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<UserMasterTblVo> userList = new ArrayList<UserMasterTblVo>();
	private GroupMasterTblVo groupMaster = new GroupMasterTblVo();
	private SocietyMstTbl societyMaster=new SocietyMstTbl();
	uamDao uam=new uamDaoServices();
	committeeDao committee=new committeeServices();
	CommittteeRoleMstTbl committeeMst=new CommittteeRoleMstTbl();
	signUpDao signup = new signUpDaoServices();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	CommonUtils comutil=new CommonUtilsServices();
	JSONObject jsonFinalObj=new JSONObject();
	CommonUtilsDao common=new CommonUtilsDao();	
	SmsInpojo sms = new SmsInpojo();
	 private SmsTemplatepojo smsTemplate;
		  private SmsEngineServices smsService = new SmsEngineDaoServices();
	Log log=new Log();	
	boolean flag = true;
	ResourceBundle rb = null;
	
	public String execute() throws JSONException{
		rb = ResourceBundle.getBundle("applicationResources");
		Commonutility.toWriteConsole("Step 1 : Committee Creation Block [Start]");
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		Common lvrCommdaosrobj = null;	
		try{
			lvrCommdaosrobj = new CommonDao();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String firstName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "firstName");
				String lastName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "lastName");
				String noofblocks = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofblocks");
				int accesschannel = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "accesschannel");
				String emailId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailId");
				String mobileNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileNo");
				String dob = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "dob");
				String gender = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "gender");
				String isdcode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "isdcode");
				int societyId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyId");
				int committeeRole = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "committeeRole");
				//int groupCodeType=json.getInt("groupCodeType");
				String password=comutil.getRandomval("AZ_09", 10);
				Date date;
				date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				String committeeUserName=Commonutility.toGetKeyIDforTbl("UserMasterTblVo","max(userId)","2","7");
				userInfo.setUserName(committeeUserName);
				userInfo.setPassword(comutil.stringToMD5(password));
				userInfo.setEncryprPassword(EncDecrypt.encrypt(password));
				userInfo.setNoofblocks(noofblocks);
				userInfo.setAccessChannel(accesschannel);
				userInfo.setGroupUniqId("temp");
				userInfo.setFirstName(firstName);
				userInfo.setLastName(lastName);
				userInfo.setDob(dob);
				userInfo.setGender(gender);
				if(emailId!=null && !emailId.equalsIgnoreCase("")){
					userInfo.setEmailId(emailId);
				}else{
					userInfo.setEmailId(null);
				}
				userInfo.setMobileNo(mobileNo);
				userInfo.setStatusFlag(1);
				userInfo.setEntryDatetime(date);
				committeeMst.setRoleId(committeeRole);
				societyMaster.setSocietyId(societyId);
				userInfo.setSocietyId(societyMaster);
				String logrpcode = null;							
				logrpcode = lvrCommdaosrobj.getGroupcodeexistornew(getText("Grp.committee"));
				if(logrpcode!= null && !logrpcode.equalsIgnoreCase("error")){
					if(Commonutility.toCheckisNumeric(logrpcode)){
						//locGrpmstvoobj.setGroupCode(Integer.parseInt(logrpcode));					
						groupMaster.setGroupCode(Integer.parseInt(logrpcode));
					}				
				}else{	
					groupMaster.setGroupCode(5);
				}
				groupMaster.setStatusFlag("1");
				userInfo.setRoleId(committeeMst);
				userInfo.setGroupCode(groupMaster);
				userInfo.setIsdCode(isdcode);
				String result = committee.userCreation(userInfo);
				if (result.equalsIgnoreCase("mobnoExist")) {
					json = null;
					serverResponse(servicecode, "03", "R0092", mobiCommon.getMsg("R0092"), json);
				}else if (result.equalsIgnoreCase("insertsuccess")) {
					//Send Email
					if(userInfo.getEmailId()!=null && Commonutility.checkempty(userInfo.getEmailId())){
						EmailEngineServices emailservice = new EmailEngineDaoServices();
						EmailTemplateTblVo emailTemplate;
						try {
							String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Customer' and status ='1'";
				            emailTemplate = emailservice.emailTemplateData(emqry);
				            String emailMessage = emailTemplate.getTempContent();
				            
				            emqry = common.templateParser(emailMessage, 1, "", password);
				            String[] qrySplit = emqry.split("!_!");
				            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.mobileNo='"+userInfo.getMobileNo()+"'";
				            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
				            emailTemplate.setTempContent(emailMessage);
				            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
				            
				            EmailInsertFuntion emailfun = new EmailInsertFuntion();
				            emailfun.test2(userInfo.getEmailId(), emailTemplate.getSubject(), emailMessage);
						} catch (Exception ex) {
							Commonutility.toWriteConsole("Excption found in Emailsend siCommitteeCreationAction.class : " + ex);
							log.logMessage("Exception in signup admin flow emailFlow : " + ex, "error",siCommitteeCreationAction.class);
	            
						}	
						jsonFinalObj.put("resultType", result);		
					}			
					 // Sending SMS	       
			          try { 
			        	 String lvrCurnturl = System.getenv("SOCIAL_INDIA_BASE_URL") + rb.getString("project.login.url");
						String lvrWebprojname = rb.getString("web.project.name");
						String pAccesstkn = rb.getString("bitly.accesstocken");
						String btlySrvurl = rb.getString("bitly.server.url");
						String	lvrBitlylnk = GetBitlyLink.toGetBitlyLinkMthd(lvrCurnturl+lvrWebprojname, "yes", pAccesstkn, btlySrvurl);
			            String mailRandamNumber;
			            mailRandamNumber = common.randInt(5555, 999999999);
			            String qry = "FROM SmsTemplatepojo where " + "templateName ='Create Customer' and status ='1'";
			            smsTemplate = smsService.smsTemplateData(qry);
			            String smsMessage = smsTemplate.getTemplateContent();
			            smsMessage = smsMessage.replace("[WEBBTLYLINK]", lvrBitlylnk);
			            if(!Commonutility.checkempty(firstName) && !Commonutility.checkempty(lastName)){
			            	  smsMessage = smsMessage.replace("[FIRST NAME]", "Sir/Madam");
			            } else{
			            	if(Commonutility.checkempty(firstName)){
			            		smsMessage = smsMessage.replace("[FIRST NAME]", firstName);
			            	} else if(Commonutility.checkempty(lastName)){
			            		smsMessage = smsMessage.replace("[FIRST NAME]", lastName);
			            	} else {
			            		smsMessage = smsMessage.replace("[FIRST NAME]", "Sir/Madam");
			            	}
			            }
			            qry = common.smsTemplateParser(smsMessage, 1, "", password);
			            String[] qrySplit = qry.split("!_!");
			            String qryform = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.mobileNo='"  + userInfo.getMobileNo() + "'";
			            smsMessage = smsService.smsTemplateParserChange(qryform, Integer.parseInt(qrySplit[1]), smsMessage);
			            sms.setSmsCardNo(mailRandamNumber);
			            sms.setSmsEntryDateTime(common
			                .getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			            sms.setSmsMobNumber(userInfo.getMobileNo());
			            sms.setSmspollFlag("F");
			            sms.setSmsMessage(smsMessage.trim());
			            smsService.insertSmsInTable(sms);
			          } catch (Exception ex) {
			        	  Commonutility.toWriteConsole("Excption found in smssend siCommitteeCreationAction.class : " + ex);
				          log.logMessage("Exception in signup admin flow siCommitteeCreationAction----> " + ex, "error",siCommitteeCreationAction.class);			           
			          }
			          serverResponse(servicecode,"00", "R0090", mobiCommon.getMsg("R0090"), jsonFinalObj);
				} else {
					serverResponse(servicecode,"00", "R0091", mobiCommon.getMsg("R0091"), jsonFinalObj);
				}
			} else {
				json=new JSONObject();
				serverResponse("SI0007","01","R0067",mobiCommon.getMsg("R0067"),json);
			}
		}catch(Exception ex){
			Commonutility.toWriteConsole("Step -1 : Committee Creation Exception Block : "+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, siCommitteeCreationAction an unhandled error occurred", "info", siCommitteeCreationAction.class);
			json=new JSONObject();
			try {
				serverResponse("SI0007","02","R0091",mobiCommon.getMsg("R0091"),json);
			} catch (Exception e) {			
			}
		}finally{
			lvrCommdaosrobj=null;
		}
		Commonutility.toWriteConsole("Step 2 : Committee Creation Block [End]");
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

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	
}
