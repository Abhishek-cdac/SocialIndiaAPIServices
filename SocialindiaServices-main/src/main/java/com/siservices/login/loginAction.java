package com.siservices.login;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.CountryDao;
import com.pack.commonapi.CountryDaoServices;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.RightsMasterTblVo;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.failedSignon.FailedSignOn;
import com.social.utils.Log;
import com.socialindia.generalmgnt.generalmgmtDao;
import com.socialindia.generalmgnt.generalmgntDaoServices;
import com.socialindiaservices.vo.BiometricDatabaseInfoTblVO;
import com.socialindiaservices.vo.ManageGSTTblVO;

public class loginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	uamDao uam = new uamDaoServices();
	CountryDao country = new CountryDaoServices();
	GroupMasterTblVo groupData = new GroupMasterTblVo();
	String result = "";
	String familyResult = "";
	UserMasterTblVo userInfo = new UserMasterTblVo();
	private List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
	MvpFamilymbrTbl userFamily = new MvpFamilymbrTbl();
	private List<TownshipMstTbl> Townshiplist = new ArrayList<TownshipMstTbl>();
	JSONObject jsonFinalObj = new JSONObject();

	public String execute() {
		JSONObject locrecvejson = null;
		Log log = null;
		FailedSignOn fail = null;
		Common common=null;
		String cyberplatesd=null;
		String cyberplatap=null;
		String cyberplatop=null;
		try{
			common=new CommonDao();
			log = new Log();
			log.logMessage("Step 1 : loginAction Service [Start].", "info", loginAction.class);
			Commonutility.toWriteConsole("Step 1 : loginAction Service [Start].");
			ivrparams = EncDecrypt.decrypt(ivrparams);
			Commonutility.toWriteConsole("Receive Param : " + ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locrecvejson = new JSONObject(ivrparams);
				String servicecode = locrecvejson.getString("servicecode");
				String username = locrecvejson.getString("userName");
				String password = locrecvejson.getString("password");
				int societyId = locrecvejson.getInt("societyId");
				int flag = locrecvejson.getInt("flag");// for login from web / mobile app
				userInfo.setUserName(username);
				userInfo.setPassword(password);				
				userFamily = uam.checkFamilyLogin(userInfo);				
				familyResult = uam.checkFamilyMember(userInfo, flag);				
				Commonutility.toWriteConsole("userFamily : " + userFamily);
				Commonutility.toWriteConsole("familyResult : " + familyResult);				 
				if (userFamily != null && familyResult.equalsIgnoreCase("valid")) {// family member login
						Commonutility.toWriteConsole("[ if Block ]");
						rightsList=uam.getUserForFamilyRights(userFamily.getUserId().getGroupCode().getGroupCode());
				} else { // direct resident / member login
						 Commonutility.toWriteConsole("[ else Block ]");
						 userInfo = uam.loginusercheck(userInfo,societyId); //  to Check Login
				}
				
				 if (userFamily==null && userInfo==null) {// Non Register user
					fail = new FailedSignOn();
					fail.toInsertfailed(username, password);
					serverResponse(servicecode, "E0001", "1", "E0001", locrecvejson);
				 } else if(userFamily==null && userInfo!=null) { // He in memeber
					 userInfo.setUserName(username);
					 userInfo.setPassword(password);					
					 result = uam.loginusercondition(userInfo,flag,societyId);	
					 rightsList = uam.getUserForFamilyRights(userInfo.getGroupCode().getGroupCode());
				 }
				 if(familyResult.equalsIgnoreCase("invalid") && result.equalsIgnoreCase("invalid")){
					 log.logMessage("Step 2 : Login user doesnot have to access permission this media.", "info", loginAction.class);
					 Commonutility.toWriteConsole("Step 2 : Login user doesnot have to access permission this media.");
					 serverResponse(servicecode, "2","E0001", "You don't have access permission in this media. Please Try another media", locrecvejson);
				 } else if (familyResult.equalsIgnoreCase("invalid") && userInfo==null){
					 log.logMessage("Step 2 : Login user doesnot have to access permission this media.", "info", loginAction.class);
					 Commonutility.toWriteConsole("Step 2 : Login user doesnot have to access permission this media.");
					 serverResponse(servicecode, "2","E0001", "You don't have access permission in this media. Please Try another media", locrecvejson);
				 } else {
					 
				 }
				log.logMessage("Step 3 : User Details Fetched.", "info", loginAction.class);
				Commonutility.toWriteConsole("Step 3 : User Details Fetched.");
				
				/*if(userInfo!=null && result.equalsIgnoreCase("valid")){
					rightsList=uam.getUserForFamilyRights(userInfo.getGroupCode().getGroupCode());
				}*/
				if((userFamily!=null && familyResult.equalsIgnoreCase("valid")) || (userInfo!=null && result.equalsIgnoreCase("valid"))){
					//rightsList=uam.getUserRights(userInfo);
					//countryList=country.getcountryList();					
					Townshiplist = uam.gettownshiplist();
					RightsMasterTblVo rightsObj;
					CountryMasterTblVO countryObj;
					TownshipMstTbl townshipObj;
					JSONObject jsonObj = new JSONObject();
					JSONObject jsontown = new JSONObject();
					JSONArray jArray = new JSONArray();
					JSONObject jsoncountryObj = new JSONObject();
					JSONArray jArrayctry = new JSONArray();
					JSONArray jArraytown = new JSONArray();
					for (Iterator<RightsMasterTblVo> it = rightsList.iterator(); it.hasNext();) {
						rightsObj = it.next();
						JSONObject finalJson = new JSONObject();
						finalJson.put("menuID", rightsObj.getMenuId().getMenuId());
						finalJson.put("menuType", rightsObj.getMenuId().getMenuType());
						finalJson.put("rootDesc", rightsObj.getMenuId().getRootDesc());
						finalJson.put("menuDesc", rightsObj.getMenuId().getMenuDesc());
						finalJson.put("menuPath", rightsObj.getMenuId().getMenuPath());	
						if(rightsObj.getMenuId().getMenuClass()!=null){
							finalJson.put("menuClass", rightsObj.getMenuId().getMenuClass());
						}else{
							finalJson.put("menuClass", "");
						}
					
					jArray.put(finalJson);				
			}			
			for (Iterator<TownshipMstTbl> it1 = Townshiplist.iterator(); it1.hasNext();) {
					townshipObj = it1.next();
					JSONObject finalJsontoenship = new JSONObject();
					finalJsontoenship.put("townshipuniid", townshipObj.getTownshipId());
					finalJsontoenship.put("townshipname", townshipObj.getTownshipName());							
					jArraytown.put(finalJsontoenship);				
			}			
			if(userInfo.getSocietyId()!=null){				
					jsontown.put("townshiplogoname", userInfo.getSocietyId().getTownshipId().getTownshiplogoname());
					jsontown.put("townshipiconame", userInfo.getSocietyId().getTownshipId().getTownshipiconame());
					jsontown.put("townshipcolourcode", userInfo.getSocietyId().getTownshipId().getTownshipcolourcode());
					jsontown.put("usersocietyid", userInfo.getSocietyId().getSocietyId());
					jsontown.put("usersocietyname", userInfo.getSocietyId().getSocietyName());
					jsontown.put("townshipid", userInfo.getSocietyId().getTownshipId().getTownshipId());
					if(userInfo.getSocietyId().getTownshipId().getImprintName()!=null){
						jsontown.put("townshipimprintname", userInfo.getSocietyId().getTownshipId().getImprintName());
					}else{
						jsontown.put("townshipimprintname", "");
					}			
					jsontown.put("societyimpname",Commonutility.toCheckNullEmpty(userInfo.getSocietyId().getImprintName()));
					jsontown.put("societyicoimg",Commonutility.toCheckNullEmpty(userInfo.getSocietyId().getIcoImage()));
					jsontown.put("societylogoimg",Commonutility.toCheckNullEmpty(userInfo.getSocietyId().getLogoImage()));
					jsontown.put("societycolorcode",Commonutility.toCheckNullEmpty(userInfo.getSocietyId().getColourCode()));												
			}else{
					jsontown.put("townshiplogoname", "");
					jsontown.put("townshipiconame","");
					jsontown.put("townshipcolourcode", "");
					jsontown.put("usersocietyid", "");
					jsontown.put("townshipid", "");				
					jsontown.put("townshipimprintname", "");					
					jsontown.put("societyimpname","");
					jsontown.put("societyicoimg","");
					jsontown.put("societylogoimg","");
					jsontown.put("societycolorcode","");	
			}
					jsontown.put("userId", userInfo.getUserId());
					if (userInfo.getUserName() != null) {
						jsontown.put("userName", userInfo.getUserName());
					} else {
						jsontown.put("userName", "");
					}
					if (userInfo.getFirstName() != null) {
						jsontown.put("firstname", userInfo.getFirstName());
					} else {
						jsontown.put("firstname", "");
					}
					if (userInfo.getLastName() != null) {
						jsontown.put("lastname", userInfo.getLastName());
					} else {
						jsontown.put("lastname", "");
					}
					if (userInfo.getMobileNo() != null) {
						jsontown.put("mobileno", userInfo.getMobileNo());
					} else {
						jsontown.put("mobileno", "");
					}
					if (userInfo.getGroupCode() != null) {
						jsontown.put("groupcode", userInfo.getGroupCode()
								.getGroupCode());
					} else {
						jsontown.put("groupcode", "");
					}
					
					if (userInfo.getImageNameWeb() != null) {
						jsontown.put("profileimagename", userInfo.getImageNameWeb());
					} else {
						jsontown.put("profileimagename", "");
					}
					cyberplatesd="select extranalValue from MvpExternalOperatorConfigTbl where extranalKey='"+getText("extranalKey.cyberplate.SD")+"'";
					String cyberplatesdval=common.getcyberval(cyberplatesd);
					
					cyberplatap="select extranalValue from MvpExternalOperatorConfigTbl where extranalKey='"+getText("extranalKey.cyberplate.AP")+"'";
					String cyberplatapval=common.getcyberval(cyberplatap);
				
					cyberplatop="select extranalValue from MvpExternalOperatorConfigTbl where extranalKey='"+getText("extranalKey.cyberplate.OP")+"'";
					String cyberplatopval=common.getcyberval(cyberplatop);
					if (cyberplatopval!=null && !cyberplatopval.equalsIgnoreCase("null") && !cyberplatopval.equalsIgnoreCase("")){
						jsontown.put("cyberplatesd", cyberplatesdval);
					} else {
						jsontown.put("cyberplatesd", "");
					}
					if (cyberplatapval!=null && !cyberplatapval.equalsIgnoreCase("null") && !cyberplatapval.equalsIgnoreCase("")){
						jsontown.put("cyberplatap", cyberplatapval);
					} else {
						jsontown.put("cyberplatap", "");
					}
					
					if (cyberplatopval!=null && !cyberplatopval.equalsIgnoreCase("null") && !cyberplatopval.equalsIgnoreCase("")){
						jsontown.put("cyberplatop", cyberplatopval);
					} else {
						jsontown.put("cyberplatop", "");
					}
					
					jsontown.put("passwordchgflg", Commonutility.toCheckNullEmpty(userInfo.getIvrBnPASSWORD_FLAG()));
					
					//biometric data: start
					generalmgmtDao generalmgmtDao = new generalmgntDaoServices();
					if(userInfo !=null && userInfo.getSocietyId() != null && userInfo.getSocietyId().getSocietyId() != null){
						BiometricDatabaseInfoTblVO biodatabaseinfo = generalmgmtDao.getBioMetricDB(userInfo.getSocietyId().getSocietyId());
						if(biodatabaseinfo!=null){
							jsontown.put("biohostname", biodatabaseinfo.getBioHostname());
							jsontown.put("biousername", biodatabaseinfo.getBioUsername());
							jsontown.put("biopassword", biodatabaseinfo.getBioPassword());
							jsontown.put("biodatabase", biodatabaseinfo.getBioDatabase());
							jsontown.put("bioport", biodatabaseinfo.getBioPort());
						}
						//biometric data: end
					}
					
					ManageGSTTblVO gstTblVO = generalmgmtDao.getGSTInfo();
					jsontown.put("gstNum", gstTblVO.getGstNum());
					jsontown.put("minGstAmt", gstTblVO.getMinGstAmt());
					jsontown.put("minAmt", gstTblVO.getMinAmt());
					
					jsonObj.put("townshipdetail", jsontown);
					jsonObj.put("menu", jArray);
					jsonObj.put("country", jArrayctry);
					jsonObj.put("townshiptbl", jArraytown);
					
					
					serverResponse(servicecode, "0000", "0", "sucess", jsonObj);		
			}
				
			} else {
				locrecvejson=new JSONObject();
				serverResponse("SI0012","1","E0001","Request values not not correct format",locrecvejson);
			}
		} catch (Exception ex) {
			String error = "";
			StackTraceElement[] el = ex.getStackTrace();
			for (int i = 0; i < el.length; i++) {
				error = error + el[i].toString();
			}
			try {	
			log.logMessage("Step -1 : Exception Found : "+error, "error", loginAction.class);
			Commonutility.toWriteConsole("Step -1 : Exception Found : "+error);				
			serverResponse("SI0012","1","E0001","Request values not not correct format",locrecvejson);
			} catch (Exception e) {
			}
		} finally {
				log.logMessage("Step 4 : loginAction Service [End]. : ", "info", loginAction.class);
				fail=null;
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
	public UserMasterTblVo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserMasterTblVo userInfo) {
		this.userInfo = userInfo;
	}
	public MvpFamilymbrTbl getUserFamily() {
		return userFamily;
	}
	public void setUserFamily(MvpFamilymbrTbl userFamily) {
		this.userFamily = userFamily;
	}
	
}
