package com.siservices.society.reports;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;

public class siSocietyPrintReport extends ActionSupport {
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	societyMgmtDao society=new societyMgmtDaoServices();
	uamDao uam = new uamDaoServices();
	JSONObject jsonFinalObj = new JSONObject();
	private List<UserMasterTblVo> userList=new ArrayList<UserMasterTblVo>();
	private List<TownshipMstTbl> Townshiplist = new ArrayList<TownshipMstTbl>();

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
				Integer uniqSocietyId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqSocietyId");
				Integer groupcode=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "groupcode");
				userData = society.getSocietyPrintData(uniqSocietyId,groupcode);
				System.out.println("========userData====="+userData.getPassword());
				if(userData!=null){
					System.out.println("===========dsfds=f==="+userData.getSocietyId().getActivationKey());
					if(userData.getUserName()==null){
						jsonFinalObj.put("username", "");
					}else{
						jsonFinalObj.put("username", userData.getUserName());
					}
					if(userData.getPassword()==null){
						jsonFinalObj.put("password", "");
					}else{
						jsonFinalObj.put("password", EncDecrypt.decrypt(userData.getEncryprPassword()));
					}
					if(userData.getSocietyId().getActivationKey()==null){
						jsonFinalObj.put("activationkey", "");
					}else{
						jsonFinalObj.put("activationkey", userData.getSocietyId().getActivationKey());
					}if(userData.getSocietyId().getTownshipId()!=null){
						jsonFinalObj.put("townshipname", userData.getSocietyId().getTownshipId().getTownshipName());
					}else{
						jsonFinalObj.put("townshipname", "");
					}if(userData.getSocietyId()!=null){
						jsonFinalObj.put("societyname", userData.getSocietyId().getSocietyName());
					}else{
						jsonFinalObj.put("societyname", "");
					}if(userData.getMobileNo()!=null){
						jsonFinalObj.put("mobileno", userData.getMobileNo());
					}else{
						jsonFinalObj.put("mobileno", "");
					}if(userData.getEmailId()!=null){
						jsonFinalObj.put("emailid", userData.getEmailId());
					}else{
						jsonFinalObj.put("emailid", "");
					}
					if(userData.getSocietyId()!=null){
						jsonFinalObj.put("societyidd",Commonutility.toCheckNullEmpty(userData.getSocietyId().getSocietyId()));
						jsonFinalObj.put("societyimagename",Commonutility.toCheckNullEmpty(userData.getSocietyId().getLogoImage()));
						jsonFinalObj.put("twnshipidd",Commonutility.toCheckNullEmpty(userData.getSocietyId().getTownshipId().getTownshipId()));
						jsonFinalObj.put("twnshipimagename",Commonutility.toCheckNullEmpty(userData.getSocietyId().getTownshipId().getTownshiplogoname()));
					} else {
						jsonFinalObj.put("societyidd","");
						jsonFinalObj.put("societyimagename","");
						jsonFinalObj.put("twnshipidd","");
						jsonFinalObj.put("twnshipimagename","");
					}
					serverResponse(servicecode, "0", "0000", "success", jsonFinalObj);
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
