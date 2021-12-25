package com.pack.complaints;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDao;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantTblVO;


public class siLaborMerchantSendSMS extends ActionSupport {
	private String ivrparams;
	MerchantTblVO merchantData = new MerchantTblVO();
	DisputemerchantDao tomerchant=new DisputemerchantDaoservice();
	UserMasterTblVo usrtblvo = new UserMasterTblVo();//Resident merchantData
	uamDao uam=new uamDaoServices();
	Log log=new Log();
	JSONObject jsonFinalObj = new JSONObject();
	SmsInpojo sms = new SmsInpojo();
	SmsInpojo sms1 = new SmsInpojo();
	 private SmsTemplatepojo smsTemplate;
	 private SmsEngineServices smsService = new SmsEngineDaoServices();
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
				Integer uniqadminId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqcmpltid");
				String mobno=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "complaintfrmmobno");
				Integer groupcode=2;
				merchantData = tomerchant.getMerchantmasterData(uniqadminId,groupcode);
				
				System.out.println("userdate mobile no::::::::; "+merchantData.getMrchntPhNo());
				if(merchantData!=null){
					//Send Email
					if(merchantData.getMrchntPhNo()!=null){
					
					try {
					
			            String qry = "FROM SmsTemplatepojo where "
			                + "templateName ='Received Complaint' and status ='1'";
			            smsTemplate = smsService.smsTemplateData(qry);
			            String smsMessage = smsTemplate.getTemplateContent();
			          
			           /* qry = common.smsTemplateParser(smsMessage, 1, "", "");
			            String[] qrySplit = qry.split("!_!");
			            System.out.println("qrySplit===="+qrySplit[0]);
			            String qryform = qrySplit[0]+ " FROM  MerchantTblVO as dispute where dispute.disputeId="+uniqadminId+"";
			            smsMessage = smsService.smsTemplateParserChange(qryform,
			                Integer.parseInt(qrySplit[1]), smsMessage);
			            */
			            sms.setSmsEntryDateTime(common
			                .getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			            sms.setSmsMobNumber(mobno);
			            sms.setSmspollFlag("F");
			            sms.setSmsMessage(smsMessage);
			            sms1.setSmsEntryDateTime(common
				                .getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				            sms1.setSmsMobNumber(mobno);
				            sms1.setSmspollFlag("F");
				            sms1.setSmsMessage(smsMessage);
			            smsService.insertSmsInTable(sms);
			           // smsService.insertSmsInTable(sms1);
			          } catch (Exception ex) {
			            System.out.println("Excption found in SitomerchantSendSMS SitomerchantSendSMS.class : " + ex);
			            log.logMessage("Exception in SitomerchantSendSMS admin flow smsFlow----> " + ex, "error",siLaborMerchantSendSMS.class);
			            
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

	
	public MerchantTblVO getMerchantData() {
		return merchantData;
	}

	public void setMerchantData(MerchantTblVO merchantData) {
		this.merchantData = merchantData;
	}

	public UserMasterTblVo getUsrtblvo() {
		return usrtblvo;
	}

	public void setUsrtblvo(UserMasterTblVo usrtblvo) {
		this.usrtblvo = usrtblvo;
	}
	

}
