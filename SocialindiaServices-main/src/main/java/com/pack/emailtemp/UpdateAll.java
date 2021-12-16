package com.pack.emailtemp;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class UpdateAll extends ActionSupport{
	/**
	 * 
	 */
	 private static final long serialVersionUID = 1L;
	 private String ivrparams;
	 public String execute() {
		 JSONObject locObjRecvdataJson = null;// Receive Data Json
		 JSONObject locObjRecvJson = null;//Receive String to json
		 JSONObject locObjResJson = null;//Receive String to json
		 JSONObject locObjRspdataJson=null;
		 String ivrservicecode=null;
		 String tempid=null;
		 String subject=null;
		 String emailcontent=null;
		 String locUpdQry=null;
		 Common commonupdate=null;
		 boolean lvrUpdaSts=false;
		 Log logWrite = null;
		 try{
			 ivrparams = EncDecrypt.decrypt(ivrparams);
			 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			 if (ivIsJson) {
				 commonupdate=new CommonDao();
				 locObjRecvJson = new JSONObject(ivrparams);
				 ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
				 locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				 if(locObjRecvdataJson!=null){
					tempid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "templateid");
					subject = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "subject");
					emailcontent = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailcontent");
					locUpdQry ="update EmailTemplateTblVo set subject = '"+subject+"', tempContent = '"+emailcontent+"' where tempid ="+Integer.parseInt(tempid)+"";
					lvrUpdaSts=commonupdate.commonUpdate(locUpdQry);
					if(lvrUpdaSts){
						serverResponse(ivrservicecode,"0","0000","success",locObjResJson);
					}else{
						serverResponse(ivrservicecode,"1","E0001","Request values are not correct format",locObjResJson);	
					}
				}
			}
		 }catch(Exception ex){
			  System.out.println(UpdateAll.class +"ex:::: "+ex);
			  locObjRspdataJson=new JSONObject();
		      logWrite.logMessage("Service code : SI300002, Sorry, an unhandled error occurred : "+ex, "error", UpdateAll.class);
			  serverResponse("SI300002","2","ER0002",getText("catch.error"),locObjRspdataJson);
		 }finally{
			 locObjRecvdataJson = null;// Receive Data Json
			 locObjRecvJson = null;//Receive String to json
			 locObjResJson = null;//Receive String to json
			 ivrservicecode=null;
			 tempid=null;
			 subject=null;
			 emailcontent=null;
			 locUpdQry=null;
			 commonupdate=null;
			 lvrUpdaSts=false; 
		 }
		 return SUCCESS;
	 }
	 private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
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
