package com.siservices.signup;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class userProfileExceptAdminEdit extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	MvpFamilymbrTbl userFamilyData=new MvpFamilymbrTbl();
	uamDao uam = new uamDaoServices();
	JSONObject jsonFinalObj = new JSONObject();
	private List<SocietyMstTbl> societyList=new ArrayList<SocietyMstTbl>();
	List<MvpFamilymbrTbl> userFamilyList = new ArrayList<MvpFamilymbrTbl>();
	private List<TownshipMstTbl> Townshiplist = new ArrayList<TownshipMstTbl>();
	private List<FlatMasterTblVO> flatMstList = new ArrayList<FlatMasterTblVO>();
	Log log = new Log();
	public String execute() throws Exception {
		JSONObject json = new JSONObject();
		try {
			boolean result;
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			json = new JSONObject(ivrparams);
			String servicecode = json.getString("servicecode");
			if (ivIsJson) {
				Integer useruniqueId = json.getInt("useruniqueId");
				userData = uam.editUser(useruniqueId);
				flatMstList=uam.getUserFlatDeatil(useruniqueId);
				userFamilyList=uam.getUserFamilyList(useruniqueId);
				JSONObject finalJson = new JSONObject();
				finalJson.put("userId", userData.getUserId());
				if(userData.getUserName()==null){
					finalJson.put("userName","");
				}else{
					finalJson.put("userName", userData.getUserName());
				}					
				if(userData.getFirstName()==null){
					finalJson.put("fName","");
				}else{
				finalJson.put("fName", userData.getFirstName());
				}	
				if(userData.getiVOcradid()!=null){
					finalJson.put("idCardNo", userData.getiVOcradid().getiVOcradid());
					finalJson.put("idCardtype", userData.getiVOcradid().getiVOcradname());
					finalJson.put("idCardtypeNo", userData.getIdProofNo());
				}else{
					finalJson.put("idCardNo", 0);
					finalJson.put("idCardtype", "");
					finalJson.put("idCardtypeNo", "");
				}				
				//finalJson.put("idCardtypeNo", userData.getIdProofNo());				
				if(userData.getGender()!=null){
					finalJson.put("gender", userData.getGender());	
				}else{
					finalJson.put("gender", "");
				}if(userData.getDob()!=null && !userData.getDob().equalsIgnoreCase("")){
				finalJson.put("dob", userData.getDob());
				}else{
					finalJson.put("dob","");
				}
				
				if(userData.getNoofflats()==0){
					finalJson.put("noofFlats", "1");
				} else {
					finalJson.put("noofFlats", Commonutility.toCheckNullEmpty(userData.getNoofflats()));
				}
				if(userData.getFlatNo()==null){
					finalJson.put("flatNo", "");	
				}else{
					finalJson.put("flatNo", Commonutility.toCheckNullEmpty(userData.getFlatNo()));	
				}
				if(userData.getNoofblocks()!=null && Commonutility.checkempty(userData.getNoofblocks())){
					finalJson.put("noofwings", Commonutility.toCheckNullEmpty(userData.getNoofblocks()));	
				}else{
					finalJson.put("noofwings", Commonutility.toCheckNullEmpty("1"));	
				}
				CountryMasterTblVO coInfo=new CountryMasterTblVO();
				if(userData.getCountryId()!=null){
					
					finalJson.put("contryCode", userData.getCountryId().getCountryId());
					finalJson.put("contryName", userData.getCountryId().getCountryName());
				}else{
					finalJson.put("contryCode", 0);
					finalJson.put("contryName", "");
				}
				if(userData.getStateId()!=null){
					finalJson.put("stateId", userData.getStateId().getStateId());	
					finalJson.put("stateName", userData.getStateId().getStateName());
				}else{
					finalJson.put("stateId", 0);	
					finalJson.put("stateName", "");
				}
				if(userData.getCityId()!=null){
					finalJson.put("cityId", userData.getCityId().getCityId());
					finalJson.put("cityName", userData.getCityId().getCityName());
				}else{
					finalJson.put("cityId", 0);
					finalJson.put("cityName", "");
				}
				if(userData.getPstlId()!=null){
					finalJson.put("pincode", userData.getPstlId());
					finalJson.put("pincodeName", userData.getPstlId());
				}else{
					finalJson.put("pincode", 0);
					finalJson.put("pincodeName", "");
				}
					
				if(userData.getOccupation()==null){
					finalJson.put("occupation", "");
				}else{					
					finalJson.put("occupation", userData.getOccupation());
				}
				if(userData.getMembersInFamily()==0){
					finalJson.put("memberofFamily",1);
				}else{
					finalJson.put("memberofFamily", userData.getMembersInFamily());
				}
				finalJson.put("accesschannal", userData.getAccessChannel());
				if(userData.getLastName()==null){
					finalJson.put("lName","");
				}else{
					finalJson.put("lName", userData.getLastName());
				}
				if(userData.getAddress1()==null){
					finalJson.put("address1","");
				}else{
					finalJson.put("address1", userData.getAddress1());
				}	if(userData.getAddress2()==null){
					finalJson.put("address2","");
				}else{
					finalJson.put("address2", userData.getAddress2());
				}
				if(userData.getBloodType()==null){
					finalJson.put("bloodType","");
				}else{
					finalJson.put("bloodType", userData.getBloodType());
				}
				finalJson.put("emailId", Commonutility.toCheckNullEmpty(userData.getEmailId()));
				finalJson.put("mobileNo", Commonutility.toCheckNullEmpty(userData.getMobileNo()));				
				if(userData.getIsdCode()==null){
					finalJson.put("isdNo","");	
				}else{
					finalJson.put("isdNo", userData.getIsdCode());
				}if(userData.getSocietyId()!=null){
				finalJson.put("societyId", userData.getSocietyId().getSocietyId());
				finalJson.put("societyname", userData.getSocietyId().getSocietyName());
				finalJson.put("townshipId", userData.getSocietyId().getTownshipId().getTownshipId());
				finalJson.put("townshipname", userData.getSocietyId().getTownshipId().getTownshipName());
				}else{
					finalJson.put("societyId", "");
					finalJson.put("societyname", "");
					finalJson.put("townshipId", 0);
					finalJson.put("townshipname", "");
				}
			//	finalJson.put("groupCodeType", userData.getGroupCode().getGroupCode());
				if(userData.getGroupCode()==null){
					finalJson.put("groupCodeType", 0);
				}else{
					finalJson.put("groupCodeType", userData.getGroupCode().getGroupCode());
				}
				if(userData.getGroupCode()!=null){
					finalJson.put("groupName", userData.getGroupCode().getGroupCode());
				}else{
					finalJson.put("groupName", "");
				}				
				JSONObject finalJsonarr=new JSONObject();
				JSONArray jArray =new JSONArray();
				if( flatMstList!=null && flatMstList.size()>0){
					FlatMasterTblVO flatObj;
				for (Iterator<FlatMasterTblVO> it = flatMstList.iterator(); it.hasNext();) {
					flatObj = it.next();
					finalJsonarr = new JSONObject();
					finalJsonarr.put("userwingsname", Commonutility.toCheckNullEmpty(flatObj.getWingsname()));
					finalJsonarr.put("userflatno", Commonutility.toCheckNullEmpty(flatObj.getFlatno()));
					jArray.put(finalJsonarr);
				}
					finalJson.put("userflatdetail", jArray);
				}else{
					finalJsonarr.put("userwingsname", "");
					finalJsonarr.put("userflatno", "");
					jArray.put(finalJsonarr);
					finalJson.put("userflatdetail", jArray);
				}
				

				JSONObject finalJsonarr1=new JSONObject();
				JSONArray jArray1 =new JSONArray();
				if( userFamilyList!=null && userFamilyList.size()>0){
					MvpFamilymbrTbl userObj;
				for (Iterator<MvpFamilymbrTbl> it = userFamilyList.iterator(); it.hasNext();) {
					userObj = it.next();
					finalJsonarr1 = new JSONObject();
					finalJsonarr1.put("childmobile", Commonutility.toCheckNullEmpty(userObj.getFmbrPhNo()));
					finalJsonarr1.put("childemail", Commonutility.toCheckNullEmpty(userObj.getFmbrEmail()));
					finalJsonarr1.put("childname", Commonutility.toCheckNullEmpty(userObj.getFmbrName()));
					finalJsonarr1.put("fmbrisd", Commonutility.toCheckNullEmpty(userObj.getFmbrIsdCode()));
					finalJsonarr1.put("fmbruniqid", Commonutility.toCheckNullEmpty(userObj.getFmbrTntId()));
					
					if(userObj.getFmbrType() == null){
						finalJsonarr1.put("fmbrmemtype",1);
					} else{
						finalJsonarr1.put("fmbrmemtype", userObj.getFmbrType());
					}
					if(userObj.getFmbrProfAcc() == null){
						finalJsonarr1.put("fmbrprfaccess", 0);
					} else{
						finalJsonarr1.put("fmbrprfaccess", userObj.getFmbrProfAcc());
					}
					
					//finalJsonarr1.put("fmbrprfaccess", userObj.getFmbrProfAcc());
					jArray1.put(finalJsonarr1);
				}
				finalJson.put("userfamilydetail", jArray1);
				}else{
					finalJsonarr1.put("childmobile", "");
					finalJsonarr1.put("childemail", "");
					finalJsonarr1.put("childname", "");
					finalJsonarr1.put("fmbrisd", "");
					finalJsonarr1.put("fmbruniqid","");
					finalJsonarr1.put("fmbrmemtype", 1);
					finalJsonarr1.put("fmbrprfaccess", 0);
					jArray1.put(finalJsonarr1);
					finalJson.put("userfamilydetail", jArray1);
				}
				
				
				System.out.println("---------Completed services Block-----------------");
				serverResponse(servicecode, "0", "0000", "sucess", finalJson);
			} else {
				json = new JSONObject();
				serverResponse(servicecode, "1", "E0001","Request values not not correct format", json);
			}
		} catch (Exception ex) {
			System.out.println("=======userEditAction====Exception====" + ex);
			log.logMessage("Service : userEditAction error occurred : " + ex, "error",userProfileExceptAdminEdit.class);
			json = new JSONObject();
			serverResponse("SI0008", "2", "E0002","Sorry, an unhandled error occurred", json);
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
			log.logMessage("Service : userEditAction error occurred : " + ex, "error",userProfileExceptAdminEdit.class);
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

	public List<FlatMasterTblVO> getFlatMstList() {
		return flatMstList;
	}

	public void setFlatMstList(List<FlatMasterTblVO> flatMstList) {
		this.flatMstList = flatMstList;
	}

	public List<MvpFamilymbrTbl> getUserFamilyList() {
		return userFamilyList;
	}

	public void setUserFamilyList(List<MvpFamilymbrTbl> userFamilyList) {
		this.userFamilyList = userFamilyList;
	}

	

}
