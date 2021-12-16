package com.pack.DisputeAdminlist;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDao;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.DisputeRiseTbl;

public class SitoAdminSendEmail extends ActionSupport {
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	DisputemerchantDao toadmin=new DisputemerchantDaoservice();
	uamDao uam=new uamDaoServices();
	Log log=new Log();
	JSONObject jsonFinalObj = new JSONObject();

	public String execute() throws Exception {
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		String servicecode=null;
		CommonUtilsDao common=new CommonUtilsDao();	
		boolean result;
		try {
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			locObjRecvJson = new JSONObject(ivrparams);
			
			if (ivIsJson) {
				System.out.println("===============json========" + json);
				locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
				servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
				Integer uniqadminId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "tomerchantid");
				String commentval =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "comments");
				String Disputeid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "disputeid");
				Integer groupcode=2;
				userData = toadmin.getusermasterData(uniqadminId,groupcode);
				System.out.println("userdate::::::::; "+userData.getEmailId());
				if(userData!=null){
					//Send Email
					if(userData.getEmailId()!=null){
					EmailEngineServices emailservice = new EmailEngineDaoServices();
					EmailTemplateTblVo emailTemplate;
					try {
			            String emqry = "FROM EmailTemplateTblVo where tempName ='Share Complaint ToAdmin' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();
			            emailMessage =  emailMessage.replace("[COMMENTS]",commentval);
			            emqry = common.templateParser(emailMessage, 1, "", "");
			            String[] qrySplit = emqry.split("!_!");
			            String qry = qrySplit[0] + " FROM DisputeRiseTbl as dispute where dispute.disputeId="+Disputeid+"";
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
			          //  emailMessage =  emailMessage.replace("[COMMENTS]",commentval);
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(userData.getEmailId(), emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            System.out.println("Excption found in siSocietySendEmail siSocietySendEmail.class : " + ex);
			            log.logMessage("Exception in siSocietySendEmail admin flow emailFlow----> " + ex, "error",SitoAdminSendEmail.class);
			            
			          }	
					
					}
					serverResponse(servicecode, "0",
							"0000", "sucess", jsonFinalObj);
				}else{
					jsonFinalObj=null;
					serverResponse(servicecode, "1", "E0001", "User not fount", jsonFinalObj);
				}
				
				
			} else {
				json = new JSONObject();
				serverResponse(servicecode, "1", "E0001",
						"Request values not not correct format", json);
			}
			}
		} catch (Exception ex) {
			System.out.println("=======userEditAction====Exception====" + ex);
			json = new JSONObject();
			serverResponse("SI0008", "2", "E0002",
					"Sorry, an unhandled error occurred", json);
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

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	public UserMasterTblVo getUserData() {
		return userData;
	}

	public void setUserData(UserMasterTblVo userData) {
		this.userData = userData;
	}

}
