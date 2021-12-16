package com.siservices.socialmediaa;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.socialmedia.persistense.MvpSocialProTbl;
import com.siservices.socialmedia.persistense.socialMediaDao;
import com.siservices.socialmedia.persistense.socialMediaServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.RightsMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDao;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class facebookInfoSave extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	MvpSocialProTbl socialpro = new MvpSocialProTbl();
	UserMasterTblVo userData=new UserMasterTblVo();
	GroupMasterTblVo groupData=new GroupMasterTblVo();
	socialMediaDao social = new socialMediaServices();
	private List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
	Log log = new Log();

	public String execute() {
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode = null;
		String facebookuserid = null;
		Integer socialtype=0;
		String picurl = null;
		String societyKey=null;
		String firstname = null;
		String lastname = null;
		String emailid = null;
		String Peninlogjav = null;
		String gender = null;
		String mobileNo = null;
		String groupcode = null;
		String name = null;
		String permtcity = null;
		CommonUtilsDao common = new CommonUtilsDao();
		try {
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null")
					&& ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("=================ivrparams=======ff======="
						+ ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);

					locObjRecvdataJson = (JSONObject) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson, "data");

					ivrservicecode = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson,
									"servicecode");

					facebookuserid = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson, "userid");
					socialtype = (Integer) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "socialtype");
					picurl = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "picurl");
					firstname = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "firstname");
					lastname = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "lastname");
					emailid = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "email");
					mobileNo = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "mobileNo");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "societyKey");
					gender = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "gender");
					name = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "name");
					permtcity = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "permtcity");
					groupcode = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "groupcode");

					System.out.println("=======facebookuserid==="
							+ facebookuserid);
					System.out.println("======picurl========="+picurl);
					CommonUtils comutil = new CommonUtilsServices();
					String locpswd = comutil.getRandomval("AZ_09", 10);
					Date date;
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					userData.setUserName(facebookuserid);
					userData.setEmailId(emailid);
					userData.setMobileNo(mobileNo);
					userData.setGroupUniqId("11");
					userData.setStatusFlag(1);
					groupData.setGroupCode(Integer.parseInt(groupcode));
					userData.setGroupCode(groupData);
					userData.setFirstName(firstname);
					userData.setLastName(lastname);
					userData.setAddress1(permtcity);
					userData.setPassword(comutil.stringToMD5(locpswd));
					userData.setEntryDatetime(date);
					
					String result = social.savefacebookInfo(userData,picurl,socialtype,societyKey,gender);
					System.out.println("=======flag===" + result);
					if(result.equalsIgnoreCase("success")){
					
						rightsList=social.getUserRightstwitter(2);
						RightsMasterTblVo rightsObj;
						JSONObject jsonObj=new JSONObject();
						JSONArray jArray =new JSONArray();
						for (Iterator<RightsMasterTblVo> it = rightsList
								.iterator(); it.hasNext();) {
							rightsObj = it.next();
							JSONObject finalJson = new JSONObject();
							finalJson.put("menuID", rightsObj.getMenuId().getMenuId());
							finalJson.put("menuType", rightsObj.getMenuId().getMenuType());
							finalJson.put("rootDesc", rightsObj.getMenuId().getRootDesc());
							finalJson.put("menuDesc", rightsObj.getMenuId().getMenuDesc());
							finalJson.put("menuPath", rightsObj.getMenuId().getMenuPath());
							finalJson.put("menuClass", rightsObj.getMenuId().getMenuClass());
							jArray.put(finalJson);
							
							}
						
						jsonObj.put("menu", jArray);
						serverResponse(ivrservicecode, "0", "0000",
								"success", jsonObj);
						
					}else if(result.equalsIgnoreCase("alreadyExist")){

						rightsList=social.getUserRightstwitter(2);
						RightsMasterTblVo rightsObj;
						JSONObject jsonObj=new JSONObject();
						JSONArray jArray =new JSONArray();
						for (Iterator<RightsMasterTblVo> it = rightsList
								.iterator(); it.hasNext();) {
							rightsObj = it.next();
							JSONObject finalJson = new JSONObject();
							finalJson.put("menuID", rightsObj.getMenuId().getMenuId());
							finalJson.put("menuType", rightsObj.getMenuId().getMenuType());
							finalJson.put("rootDesc", rightsObj.getMenuId().getRootDesc());
							finalJson.put("menuDesc", rightsObj.getMenuId().getMenuDesc());
							finalJson.put("menuPath", rightsObj.getMenuId().getMenuPath());
							finalJson.put("menuClass", rightsObj.getMenuId().getMenuClass());
							jArray.put(finalJson);
							
					}
						jsonObj.put("menu", jArray);
						serverResponse(ivrservicecode, "0", "0000",
								"already Exist user", jsonObj);
					}else if(result.equalsIgnoreCase("activekeyerror")){
						JSONObject jsonObj=null;
						serverResponse(ivrservicecode, "2", "E0001",
								"Society Key Wrong", jsonObj);
					}
					
					else{
						serverResponse(ivrservicecode, "1", "E0001",
								"Db Error", locObjRspdataJson);
					}

					locObjRspdataJson = new JSONObject();

				} else {
					locObjRspdataJson = new JSONObject();
					log.logMessage(
							"Service code : SI2002, Request values are not correct format",
							"info", facebookInfoSave.class);
					serverResponse("SI2002", "1", "E0001",
							"Request values are not correct format",
							locObjRspdataJson);
				}
			} else {
				locObjRspdataJson = new JSONObject();
				log.logMessage(
						"Service code : SI2002, Request values are empty",
						"info", facebookInfoSave.class);
				serverResponse("SI2002", "1", "E0001",
						"Request values are empty", locObjRspdataJson);
			}

		} catch (Exception e) {
			System.out
					.println("Service code : SI2002, Exception found in Forgetpassword.action execute() Method : "
							+ e);
			log.logMessage(
					"Service code : SI2002, Sorry, an unhandled error occurred",
					"info", facebookInfoSave.class);
			locObjRspdataJson = new JSONObject();
			serverResponse("SI2002", "2", "E0002",
					"Sorry, an unhandled error occurred", locObjRspdataJson);
		} finally {			
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson) {
		PrintWriter out = null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response = null;
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
			try {
				out = response.getWriter();
				out.print("{\"servicecode\":\"" + serviceCode + "\",");
				out.print("{\"statuscode\":\"2\",");
				out.print("{\"respcode\":\"E0002\",");
				out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
				out.print("{\"data\":\"{}\"}");
				out.close();
				ex.printStackTrace();
			} catch (Exception e) {
			} finally {
			}
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

}
