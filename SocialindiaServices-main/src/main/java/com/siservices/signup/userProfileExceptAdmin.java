package com.siservices.signup;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class userProfileExceptAdmin extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	uamDao uam = new uamDaoServices();
	JSONObject jsonFinalObj = new JSONObject();
	String lvrImgwebsrcpath = null, profileImageFileName = null;
	
	public String execute() {
		JSONObject json = null;
		Log log = null;
		try {
			log = new Log();
			ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
			String  result;
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			json = new JSONObject(ivrparams);
			String servicecode = json.getString("servicecode");
			if (ivIsJson) {			
				Integer useruniqueId = json.getInt("userId");
				String username = json.getString("userName");
				String emailId = json.getString("emailId");			
				int idcardtype = json.getInt("idcardtype");
				String idcardno = json.getString("idcardno");
				String fname = json.getString("firstName");
				String lname = json.getString("lastName");
				String gender = json.getString("gender");
				String dob = json.getString("dob");
				String noofFlats = json.getString("noofflats");
				int no_flats=0;
				if (!Commonutility.toCheckisNumeric(noofFlats)) {
					no_flats = 0;
				} else {
					no_flats = Integer.parseInt(noofFlats);
				}
				// String flatNo=json.getString("flatno");
				String noofwings = json.getString("noofwings");
				String address1 = json.getString("address1");
				String address2 = json.getString("address2");
				int country = json.getInt("countryid");
				int state = json.getInt("stateid");
				int city = json.getInt("cityid");
				int pin = json.getInt("pinid");
				String occupation = json.getString("occupation");
				String bloodtype = json.getString("bloodgroup");
				int familyno = json.getInt("familyno");
				int accesschannel = json.getInt("accesschannel");
				// int townshipId=json.getInt("townshipId");
				// int societyId=json.getInt("societyId");
				String blockNameList = json.getString("blockNameList");
				String flatNameList = json.getString("flatNameList");
				String famName = json.getString("famName");
				String famMobileNo = json.getString("famMobileNo");
				String famEmailId = json.getString("famEmailId");
				String locvrfmbrISD=(String) Commonutility.toHasChkJsonRtnValObj(json, "famisdcode");
				String locvrfmbrMtype =(String) Commonutility.toHasChkJsonRtnValObj(json, "fammemtype");
				String locvrfmbrPrfaccess=(String) Commonutility.toHasChkJsonRtnValObj(json, "famprfaccess");
				String lvrFmbrUniquid = (String) Commonutility.toHasChkJsonRtnValObj(json, "fmemberuniqueid");//family member unique id
				
				lvrImgwebsrcpath = (String) Commonutility.toHasChkJsonRtnValObj(json, "imagewebsrchpath");
				profileImageFileName = (String) Commonutility.toHasChkJsonRtnValObj(json, "profileImageFileName");
				if(profileImageFileName!=null && profileImageFileName!=""){
					profileImageFileName = profileImageFileName.replaceAll(" ", "_");					
					 String lvrWebImagpath = rb.getString("external.uploadfile.webpath");
					 String lvrMobImgpath = rb.getString("external.uploadfile.mobilepath");
					 String delrs = Commonutility.todelete("", lvrWebImagpath+useruniqueId+"/");
					 String delrs_mob= Commonutility.todelete("", lvrMobImgpath+useruniqueId+"/");
					  
					 Commonutility.toWriteImageMobWebNewUtill(useruniqueId, profileImageFileName,lvrImgwebsrcpath,lvrWebImagpath,lvrMobImgpath,getText("thump.img.width"),getText("thump.img.height"), log, getClass());
					
					/*
					String topath=rb.getString("external.uploadfile.webpath")+useruniqueId;
				//String delrs = Commonutility.todelete("", topath + "/");// Delete Existing image	
					String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, profileImageFileName);					
			
					String limgSourcePath=rb.getString("external.uploadfile.webpath")+useruniqueId+"/"+profileImageFileName;
					String limgDisPath = rb.getString("external.uploadfile.mobilepath")+useruniqueId+"/"; 
				//String delrs_mob= Commonutility.todelete("", limgDisPath+"/");// Delete Existing image
					
					String limgName = FilenameUtils.getBaseName(profileImageFileName);
					String limageFomat = FilenameUtils.getExtension(profileImageFileName);					
					String limageFor = "all";
					int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
					int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));	
					ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
				*/
				}
				/*
				 * Integer groupCode;
				 * System.out.println(json.getInt("groupCode"));
				 */
				/*
				 * if(json.getInt("groupCode")!=0){
				 * groupCode=json.getInt("groupCode"); }else{ groupCode=0; }
				 */			
			//String societyname=json.getString("societyname");
					result=uam.updateUserProfileExceptAdmin(useruniqueId,username,fname,lname,idcardtype,idcardno,
					gender,dob,no_flats,address1,address2,country,state,city,pin,occupation,bloodtype,familyno,accesschannel,blockNameList,flatNameList,
					famName,famMobileNo,famEmailId,noofwings,profileImageFileName,emailId,locvrfmbrISD,locvrfmbrMtype,locvrfmbrPrfaccess,lvrFmbrUniquid);
					JSONObject finalJson = new JSONObject();
				if (result.equalsIgnoreCase("success")) {
					json = new JSONObject();
					userData = uam.getregistertable(useruniqueId);
					if(userData.getImageNameWeb()!=null && userData.getImageNameWeb()!=""){
						json.put("profileimage", userData.getImageNameWeb());
					}else{
						json.put("profileimage", "");
					}				
					serverResponse(servicecode,"0", "0000", "success", json);
				} else {
				serverResponse(servicecode, "2", "E0001", "Error in updating", finalJson);
				}
			} else {
					json = new JSONObject();
					serverResponse(servicecode, "1", "E0001", "Request values not not correct format", json);
			}
		} catch (Exception ex) {
				Commonutility.toWriteConsole("Step -1 : Exception Found in "+getClass().getSimpleName()+".class : "+ex);
		} finally {
			log = null;
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
