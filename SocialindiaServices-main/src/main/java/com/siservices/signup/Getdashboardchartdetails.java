package com.siservices.signup;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.social.utititymgmt.NewGeneralDocument;
import com.socialindiaservices.common.CommonUtils;

public class Getdashboardchartdetails extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	
	JSONObject locObjRecvJson = null;// Receive String to json
	JSONObject locObjRecvdataJson = null;// Receive Data Json
	JSONObject locObjRspdataJson = null;// Response Data Json
	JSONObject jsonFinalObj=new JSONObject();
	CommonUtils common=new CommonUtils();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	uamDao uam=new uamDaoServices();
	
	public String execute(){
		Log log= new Log();
		Integer ivrEntryByusrid=0;
		try{
			UserMasterTblVo userdet=new UserMasterTblVo();
			GroupMasterTblVo groupdet=new GroupMasterTblVo();			
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				String imgpath="";
				String mobimgpath="";
				String downloadImagePath="";
				if (ivIsJson) {	
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }
					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					String retval=uam.getDashboardDetails(userId);
					jsonFinalObj.put("dashboardDteail", retval);
					serverResponse(ivrservicecode, "0000","0", "sucess", jsonFinalObj);
					
				}else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6412,"+getText("request.format.notmach")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6412","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}
			}else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6412,"+getText("request.values.empty")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6412","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
			}
			
		}catch (Exception ex){
			
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
			System.out.println("statusCode------------"+statusCode+"-----dataJson-------"+dataJson);
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
	public String getIvrservicecode() {
		return ivrservicecode;
	}
	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

}
