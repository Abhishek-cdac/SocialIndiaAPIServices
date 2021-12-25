package com.pack.paswordservice;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.commonapi.Educationlst;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class Changepassowrd extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String ivrparams;	

	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		String ivrservicecode = null;
		int ivruserid = 0;
		String ivroldpassword=null,ivrnewpassword=null,ivremailid=null,ivrmobno=null;
		try {
			CommonUtilsDao common=new CommonUtilsDao();	
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					ivroldpassword = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"oldpassword");
					ivrnewpassword = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"newpassword");
					ivruserid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"userid");
					//ivremailid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"emailid");
					//ivrmobno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"emailid");
					CommonDao locCmObj=new CommonDao();
					boolean flag=locCmObj.checkusername(common.stringToMD5(ivroldpassword),ivruserid);
					if(flag==true){
						int lvrPswrdflg =2;// Password changed
						String locUpdQry="update ForgetpasswordTblVO set pswd ='"+common.stringToMD5(ivrnewpassword)+"', encryptPswd= '"+EncDecrypt.encrypt(ivrnewpassword)+"', ivrBnPASSWORD_FLAG="+lvrPswrdflg+" where usrId = '"+ivruserid+"' and pswd='"+common.stringToMD5(ivroldpassword)+"'";					
						locCmObj.commonUpdate(locUpdQry);
						locObjRspdataJson=new JSONObject();

				/*	//Send Email
					EmailEngineServices emailservice = new EmailEngineDaoServices();
					EmailTemplateTblVo emailTemplate;
					
					try {
			            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Reset Password' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();
			            
			            emqry = common.templateParser(emailMessage, 1, "", ivrnewpassword);
			            String[] qrySplit = emqry.split("!_!");
			            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.emailId='"+locsvemail+"'";
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(locsvemail, emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            Commonutility.toWriteConsole("Excption found in Emailsend Forgetpassword.class : " + ex);
			            log.logMessage("Exception in usercreationFunction() emailFlow----> " + ex, "error",Forgetpassword.class);
			            
			          }	*/											
						AuditTrial.toWriteAudit(getText("PSW001"), "PSW001", ivruserid);
					serverResponse(ivrservicecode,"0","0000","Change Successfully",locObjRspdataJson);
					
				}else{
					AuditTrial.toWriteAudit(getText("PSW002"), "PSW002", ivruserid);
					serverResponse(ivrservicecode,"4","E0001","Oldpassword wrong",locObjRspdataJson);
				}
				}else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI2002, Request values are not correct format", "info", Educationlst.class);
					serverResponse("SI2002","1","E0001","Request values are not correct format",locObjRspdataJson);

				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI1007, Request values are not correct format", "info", Educationlst.class);
				serverResponse("SI2002","1","E0001","Request values are not correct format",locObjRspdataJson);

			}
		} catch (Exception e) {
			AuditTrial.toWriteAudit(getText("PSW003"), "PSW003", ivruserid);
			Commonutility.toWriteConsole("Exception found Changepassowrd.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI1007, Request values are empty", "info", Educationlst.class);
			serverResponse("SI2002","2","E0002","Request values are empty",locObjRspdataJson);
		} finally {			
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
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
