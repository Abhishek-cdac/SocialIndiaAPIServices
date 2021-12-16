package com.siservices.signup;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class ProfileEditAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	uamDao uam=new uamDaoServices();
	UserMasterTblVo userData = new UserMasterTblVo();
	Log log=new Log();		
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		//JSONObject data=new JSONObject();
		
		String ivrservicecode=null;
		int userid;
		//CommonUtilsDao common=new CommonUtilsDao();		
		try {
			Commonutility.toWriteConsole("Step 1 : ProfileEditAction Called for Profile detail fetched . [Start]");
			log.logMessage("Step 1 : ProfileEditAction Called for Profile detail fetched . [Start]", "info", ProfileEditAction.class);
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					ivrservicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
					userid=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userid");
					userData=uam.editUser(userid);
				//	data.put("servicecode", ivrservicecode);					
					locObjRspdataJson.put("userid", userData.getUserId());
					if(userData.getFirstName()==null){
						locObjRspdataJson.put("firstname", "");
					}else{
					locObjRspdataJson.put("firstname", userData.getFirstName());
					}if(userData.getLastName()==null){
						locObjRspdataJson.put("lastname", "");
					}else{
					locObjRspdataJson.put("lastname", userData.getLastName());
					}if(userData.getDob()==null){
						locObjRspdataJson.put("dob","");
					}else{
					locObjRspdataJson.put("dob", userData.getDob());
					}if(userData.getGender()==null){
						locObjRspdataJson.put("gender", "");
					}else{
					locObjRspdataJson.put("gender", userData.getGender());
					}if(userData.getiVOcradid()==null){
						locObjRspdataJson.put("idcardtype", 0);
					}else{
					locObjRspdataJson.put("idcardtype", userData.getiVOcradid().getiVOcradid());
					}
					if(userData.getiVOcradid()!=null){
						locObjRspdataJson.put("idcardtypename", userData.getiVOcradid().getiVOcradname());
					}else{
						locObjRspdataJson.put("idcardtypename", "");
					}
					if(userData.getIdProofNo()==null){
						locObjRspdataJson.put("idproofno", "");
					}else{
					locObjRspdataJson.put("idproofno", userData.getIdProofNo());
					}if(userData.getFlatNo()==null){
						locObjRspdataJson.put("flatno","");
					}else{
					locObjRspdataJson.put("flatno", userData.getFlatNo());
					}if( userData.getOccupation()==null){
						locObjRspdataJson.put("occupation", "");
					}else{
					locObjRspdataJson.put("occupation", userData.getOccupation());
					}if(userData.getBloodType()==null){
						locObjRspdataJson.put("bloodtype", "");
					}else{
					locObjRspdataJson.put("bloodtype", userData.getBloodType());
					}
					locObjRspdataJson.put("noofmember", userData.getMembersInFamily());
					if(userData.getCountryId()==null){
						locObjRspdataJson.put("countryid", 0);
					}else{
					locObjRspdataJson.put("countryid", userData.getCountryId().getCountryId());
					}if(userData.getCountryId()!=null){
						locObjRspdataJson.put("countryName", userData.getCountryId().getCountryName());
					}else{
						locObjRspdataJson.put("countryName", "");
					}if(userData.getStateId()==null){
						locObjRspdataJson.put("stateid", 0);
					}else{
					locObjRspdataJson.put("stateid", userData.getStateId().getStateId());
					}if(userData.getStateId()!=null){
						locObjRspdataJson.put("stateName", userData.getStateId().getStateName());
					}else{
					locObjRspdataJson.put("stateName", "");
					}if( userData.getCityId()==null){
						locObjRspdataJson.put("cityid", 0);
					}else{
					locObjRspdataJson.put("cityid", userData.getCityId().getCityId());
					}if( userData.getCityId()!=null){
						locObjRspdataJson.put("cityName", userData.getCityId().getCityName());
					}else{
					locObjRspdataJson.put("cityName", "");
					}if(userData.getPstlId()==null){
					locObjRspdataJson.put("pincode", 0);
					}else{
//						locObjRspdataJson.put("pincode", userData.getPstlId().getPstlId());
						locObjRspdataJson.put("pincode", userData.getPstlId());
					}
					if(userData.getPstlId()!=null){
//						locObjRspdataJson.put("pincodeNo", userData.getPstlId().getPstlCode());
						locObjRspdataJson.put("pincodeNo", userData.getPstlId());
						}else{
						locObjRspdataJson.put("pincodeNo", "");
						}if( userData.getAddress1()==null){
						locObjRspdataJson.put("address1","");
					}else{
						locObjRspdataJson.put("address1", userData.getAddress1());
					}if(userData.getAddress2()==null){
					locObjRspdataJson.put("address2", "");
					}else{
						locObjRspdataJson.put("address2", userData.getAddress2());
					}if(userData.getNoofflats()==0){
						locObjRspdataJson.put("noofflats",0);
						}else{
							locObjRspdataJson.put("noofflats", userData.getNoofflats());
						}
					if(userData.getAccessChannel()==0){
						locObjRspdataJson.put("accessmedia", 0);
						}else{
							locObjRspdataJson.put("accessmedia", userData.getAccessChannel());
						}
					locObjRspdataJson.put("servicecode", ivrservicecode);
					serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI2002, Request values are empty", "info", ProfileEditAction.class);
				serverResponse(ivrservicecode,"1","E0001","Request values are empty",locObjRspdataJson);
			}
			
			}	
		}catch(Exception e){
			System.out.println("Service code : SI2002, Exception found in Forgetpassword.action execute() Method : "+e);
			log.logMessage("Service code : SI2002, Sorry, an unhandled error occurred", "info", ProfileEditAction.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(ivrservicecode,"2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
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
