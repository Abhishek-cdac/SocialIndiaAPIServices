package com.siservices.committeeMgmt;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.committeeDao;
import com.siservices.committeeMgmt.persistense.committeeServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;

public class siCommitteeViewAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	JSONObject jsonFinalObj=new JSONObject();
	committeeDao committee=new committeeServices();
	UserMasterTblVo userData = new UserMasterTblVo();
	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
	//	Session locObjsession = null;		
		String ivrservicecode=null;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
								
				int uniqcommitteeId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqcommitteeId");
				String locVrSlQry="";				
				userData = committee.getCommitteeDetailView(uniqcommitteeId);
				if(userData!=null){
					if(userData.getSocietyId()!=null){
						jsonFinalObj.put("townshipname", userData.getSocietyId().getTownshipId().getTownshipName());
						jsonFinalObj.put("societyname", userData.getSocietyId().getSocietyName());
					}else{
						jsonFinalObj.put("townshipname", "");
						jsonFinalObj.put("societyname", "");
					}
					if(userData.getRoleId()!=null){
						jsonFinalObj.put("committeeRole",  userData.getRoleId().getRoleName());
					}else{
						jsonFinalObj.put("committeeRole",  "");
					}
					if(userData.getUserName()!=null){
						jsonFinalObj.put("username", userData.getUserName());
					}else{
						jsonFinalObj.put("username", "");
					}
					if(userData.getFirstName()!=null){
						jsonFinalObj.put("firstname", userData.getFirstName());
					}else{
						jsonFinalObj.put("firstname", "");
					}
					if(userData.getLastName()!=null){
						jsonFinalObj.put("lastname", userData.getLastName());
					}else{
						jsonFinalObj.put("lastname", "");	
					}
				if(userData.getEmailId()!=null){
				jsonFinalObj.put("emailid", userData.getEmailId());
				}else{
					jsonFinalObj.put("emailid", "");	
				}
				jsonFinalObj.put("isdcode", userData.getIsdCode());
				jsonFinalObj.put("mobileno", userData.getMobileNo());
				jsonFinalObj.put("gender", userData.getGender());
				jsonFinalObj.put("dob", userData.getDob());
				jsonFinalObj.put("accesschannel", userData.getAccessChannel());
				
				serverResponse(ivrservicecode, "0","0000", "success", jsonFinalObj);				
				}else{
					serverResponse(ivrservicecode,"01","E0001","database error occured",locObjRspdataJson);
				}
					
				}else{
					//Response call
					serverResponse(ivrservicecode,"01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siCommitteeViewAction.class);
			try {
				serverResponse(ivrservicecode,"02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			//if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
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
