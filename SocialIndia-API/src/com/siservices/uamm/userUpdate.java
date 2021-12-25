package com.siservices.uamm;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;

public class userUpdate extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	uamDao uam = new uamDaoServices();
	JSONObject jsonFinalObj = new JSONObject();

	public String execute() {
		JSONObject json = new JSONObject();
		try{
			String result;

			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			json = new JSONObject(ivrparams);
			String servicecode = json.getString("servicecode");
			if (ivIsJson) {			
				Integer useruniqueId = json.getInt("userId");
				String username = json.getString("userName");
				String emailId = json.getString("emailId");
				String isdno = json.getString("isdno");
				String mobileNo = json.getString("mobileNo");
				int idcardtype = json.getInt("idcardtype");
				String idcardno = json.getString("idcardno");
				String fname = json.getString("firstName");
				String lname = json.getString("lastName");
				int noOfLogins = json.getInt("numOfLogins");
				String gender = json.getString("gender");
				String dob = json.getString("dob");
				String noofFlats = json.getString("noofflats");
			 int no_flats=0;
		      if(!Commonutility.toCheckisNumeric(noofFlats)){
		    	  no_flats=0;
		      }else{
		    	  no_flats=Integer.parseInt(noofFlats);
		      }
			//String flatNo=json.getString("flatno");
		  	String noofwings=json.getString("noofwings");
			String address1=json.getString("address1");
			String address2=json.getString("address2");
			int country=json.getInt("countryid");
			int state=json.getInt("stateid");
			int city=json.getInt("cityid");
			
			Integer pin=json.getInt("pinid");
			
			
			String occupation=json.getString("occupation");
			String bloodtype=json.getString("bloodgroup");
			int familyno=json.getInt("familyno");
			int accesschannel=json.getInt("accesschannel");
			int townshipId=json.getInt("townshipId");
			int societyId=json.getInt("societyId");
			String blockNameList=json.getString("blockNameList");
			String flatNameList=json.getString("flatNameList");
			String famName=json.getString("famName");
			String famMobileNo=json.getString("famMobileNo");
			String famEmailId=json.getString("famEmailId");
			String locvrfmbrISD = (String) Commonutility.toHasChkJsonRtnValObj(json, "famisdcode");
			String locvrfmbrMtype =(String) Commonutility.toHasChkJsonRtnValObj(json, "fammemtype");
			String locvrfmbrPrfaccess = (String) Commonutility.toHasChkJsonRtnValObj(json, "famprfaccess");
			String lvrFmbrUniquid = (String) Commonutility.toHasChkJsonRtnValObj(json, "fmemberuniqueid");//family member unique id	
			String societyname=json.getString("societyname");
			result = uam.updateUser(useruniqueId,username,fname,lname,townshipId,societyId,societyname,emailId,isdno,mobileNo,idcardtype,idcardno,
					gender,dob,no_flats,address1,address2,country,state,city,pin,occupation,bloodtype,familyno,accesschannel,blockNameList,flatNameList,
					famName,famMobileNo,famEmailId,noofwings,locvrfmbrISD,locvrfmbrMtype,locvrfmbrPrfaccess,lvrFmbrUniquid, noOfLogins);
			JSONObject finalJson = new JSONObject();
				if (result.equalsIgnoreCase("EmailExist")) {
					json = null;
					serverResponse(servicecode, "E0001", "1", "EmailExist",
							json);
				} else if (result.equalsIgnoreCase("mobnoExist")) {
					json = null;
					serverResponse(servicecode, "E0001", "1", "mobnoExist",
							json);
				} else if (result.equalsIgnoreCase("success")) {
					json = null;
					serverResponse(servicecode, "0", "0000", "success", json);
				} else {
					serverResponse(servicecode, "2", "E0001",
							"Error in updating", finalJson);
				}
			} else {
				json = new JSONObject();
				serverResponse(servicecode, "1", "E0001",
						"Request values not not correct format", json);
			}
		} catch (Exception ex) {
			System.out.println("=======userUpdate====Exception====" + ex);
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
