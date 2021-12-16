package com.siservices.committeeMgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class siCommitteeEditAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	uamDao uam = new uamDaoServices();
	JSONObject jsonFinalObj = new JSONObject();
	private List<SocietyMstTbl> societyList=new ArrayList<SocietyMstTbl>();
	private List<TownshipMstTbl> Townshiplist = new ArrayList<TownshipMstTbl>();

	public String execute() throws Exception {
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		String servicecode=null;
		boolean result;
		Log logWrite = null;
		try {
			logWrite = new Log();
			logWrite.logMessage("Step 1 : Committee Single Select Called siCommitteeEditAction.class [Start]", "info", siCommitteeEditAction.class);
			Commonutility.toWriteConsole("Step 1 : Committee Single Select Called siCommitteeEditAction.class [Start]");
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				locObjRecvJson = new JSONObject(ivrparams);			
				if (ivIsJson) {
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					servicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
					Integer useruniqueId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqcommitteeId");
					userData = uam.editUser(useruniqueId);
					logWrite.logMessage("Step 2 : Committee details selected. siCommitteeEditAction.class", "info", siCommitteeEditAction.class);
					Commonutility.toWriteConsole("Step 2 : Committee details selected. siCommitteeEditAction.class");
					JSONObject finalJson = new JSONObject();
					finalJson.put("userId", userData.getUserId());
					if(userData.getUserName()!=null){
					finalJson.put("userName", userData.getUserName());
					}else{
						finalJson.put("userName", "");	
					}
					if(userData.getEmailId()!=null){
					finalJson.put("emailId", userData.getEmailId());
					}else{
						finalJson.put("emailId", "");
					}
					finalJson.put("mobileNo", userData.getMobileNo());
					finalJson.put("townshipname", userData.getSocietyId().getTownshipId().getTownshipName());
					finalJson.put("societyname", userData.getSocietyId().getSocietyName());
					if(userData.getRoleId()!=null){
						finalJson.put("committeerole", userData.getRoleId().getRoleName());
						finalJson.put("committeeid", userData.getRoleId().getRoleId());
					}else{
						finalJson.put("committeerole", "");
						finalJson.put("committeeid", "");
					}
					
					if(userData.getSocietyId()!=null){
					finalJson.put("societyId", userData.getSocietyId().getSocietyId());
					finalJson.put("townshipId", userData.getSocietyId().getTownshipId().getTownshipId());
					}else{
						finalJson.put("societyId", "");
						finalJson.put("townshipId", "");
					}
					
					finalJson.put("accesschannel", userData.getAccessChannel());
					if(userData.getFirstName()==null){
						finalJson.put("fName","");
					}else{
					finalJson.put("fName", userData.getFirstName());
					}
					
					if(userData.getLastName()==null){
						finalJson.put("lName","");
					}else{
						finalJson.put("lName", userData.getLastName());
					}
					if(userData.getGender()==null){
						finalJson.put("gender","");
					}else{
						finalJson.put("gender", userData.getGender());
					}if(userData.getDob()==null){
						finalJson.put("dob","");
					}else{
						finalJson.put("dob", userData.getDob());
					}				
					logWrite.logMessage("Step 3 : Committee Single Select Called siCommitteeEditAction.class [End]", "info", siCommitteeEditAction.class);
					Commonutility.toWriteConsole("Step 3 : Committee Single Select Called siCommitteeEditAction.class [End]");
					serverResponse(servicecode, "00", "0000", "success", finalJson);
			} else {
				json = new JSONObject();
				serverResponse(servicecode, "01", "R0067", mobiCommon.getMsg("R0067"), json);
			}
			}
		} catch (Exception ex) {			
			json = new JSONObject();
			serverResponse("SI0008", "02", "E0002","Sorry, an unhandled error occurred", json);
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
