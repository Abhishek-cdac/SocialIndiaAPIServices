package com.siservices.signup;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.common.common;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MenuMasterTblVo;
import com.siservices.uam.persistense.RightsMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class memberSignUp extends ActionSupport {
	common com = new common();
	signUpDao signup = new signUpDaoServices();
	uamDao uam=new uamDaoServices();
	private UserMasterTblVo userMaster = new UserMasterTblVo();
	private GroupMasterTblVo groupMaster = new GroupMasterTblVo();
	private List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
	 List<MenuMasterTblVo> menuList = new ArrayList<MenuMasterTblVo>();
	 private SocietyMstTbl societyMaster=new SocietyMstTbl();
	 IDCardMasterTblVO idCardMst=new IDCardMasterTblVO();
	private String ivrparams;
	Log log=new Log();	
	CommonUtilsDao common=new CommonUtilsDao();		
	 CommonUtils comutil=new CommonUtilsServices();

	public String execute() throws Exception {
		JSONObject json = new JSONObject();
		JSONObject json_data = new JSONObject();
		String result="";
		System.out
				.println("======================ffff===================================");
		System.out.println("==============signUpProcess====" + ivrparams);
		boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
		if (ivIsJson) {		
			json_data = new JSONObject(ivrparams);
			 json=json_data.getJSONObject("data");
		System.out.println("===============json========" + json);
		String servicecode=json.getString("servicecode");
		try
		{
		//if (flag == true) {
			servicecode=json.getString("servicecode");
			System.out.println("===fd=g=====");
			String userName =  json.getString("userName");
			String emailId = (String) json.get("emailId");
			int groupCodeType = (int) json.get("groupCodeType");
			System.out.println("===fd=g=====");
			/*String firstName=json.getString("firstName");
			String lastName=json.getString("lastName");*/
			int idCardType=json.getInt("idCardType");
			System.out.println("=====idCardType==       gggg           ==="+idCardType);
			String idProofNo=json.getString("idProofNo");
			System.out.println("=====idProofNo=--------------===="+idProofNo);
			/*String dob=json.getString("dob");
			System.out.println("==========dob====="+dob);
			String bloodGroup=json.getString("bloodGroup");
			String address1=json.getString("address1");*/
			String mobileNo = (String) json.get("mobileNo");
			int societyId=json.getInt("societyId");
			String password=comutil.getRandomval("AZ_09", 10);
			Date date;
			CommonUtils comutil=new CommonUtilsServices();
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			System.out.println("===========userName===="+userName);
			userMaster.setUserName(userName);
			userMaster.setEmailId(emailId);
			userMaster.setPassword(comutil.stringToMD5(password));
			userMaster.setGroupUniqId("11");
			/*userMaster.setBloodType(bloodGroup);
			userMaster.setFirstName(firstName);                    //futute purpose
			userMaster.setLastName(lastName);
			userMaster.setDob(dob);*/
			idCardMst.setiVOcradid(idCardType);
			userMaster.setiVOcradid(idCardMst);
			userMaster.setIdProofNo(idProofNo);
			userMaster.setStatusFlag(1);
			userMaster.setMobileNo(mobileNo);
			userMaster.setEntryDatetime(date);
			societyMaster.setSocietyId(societyId);
			userMaster.setSocietyId(societyMaster);
			groupMaster.setGroupCode(groupCodeType);
			/*userMaster.setAddress1(address1);*/
			groupMaster.setStatusFlag("1");
			userMaster.setGroupCode(groupMaster);
			result= signup.saveUserInfo(userMaster);
			rightsList=uam.getUserRights(userMaster);
			System.out.println("===============rightsList===size======="+rightsList);
			if (result.equalsIgnoreCase("EmailExist")) {
				System.out.println("=========EmailExist===========");
				json=null;
				serverResponse(servicecode,"E0001",
						"1", "EmailExist", json);
			}else if(result.equalsIgnoreCase("mobnoExist")){
				System.out.println("=========mobnoExist===========");
				json=null;
				serverResponse(servicecode,"E0001",
						"1", "mobnoExist", json);
			}else if(result.equalsIgnoreCase("userExist")){
				System.out.println("=========userExist===========");
				json=null;
				serverResponse(servicecode,"E0001",
						"1", "userExist", json);
			}
			
			if (result.equalsIgnoreCase("insertsuccess")) {
				System.out.println("===============rightsList===size=====11=="+rightsList.size());
				RightsMasterTblVo rightsObj;
				JSONObject jsonObj=new JSONObject();
				JSONArray jArray =new JSONArray();
				JSONObject jsontown=new JSONObject();
				for (Iterator<RightsMasterTblVo> it = rightsList
						.iterator(); it.hasNext();) {
					rightsObj = it.next();
					JSONObject finalJson = new JSONObject();
					finalJson.put("menuID", rightsObj.getMenuId().getMenuId());
					finalJson.put("menuType", rightsObj.getMenuId().getMenuType());
					finalJson.put("rootDesc", rightsObj.getMenuId().getRootDesc());
					finalJson.put("menuDesc", rightsObj.getMenuId().getMenuDesc());
					finalJson.put("menuPath", rightsObj.getMenuId().getMenuPath());
					if(rightsObj.getMenuId().getMenuClass()!=null){
						finalJson.put("menuClass", rightsObj.getMenuId().getMenuClass());
					} else {
						finalJson.put("menuClass", "");
					}
					
					jArray.put(finalJson);
					
					}
				userMaster = uam.getregistertable(userMaster.getUserId());
				
				if(userMaster.getSocietyId()!=null){
					jsontown.put("townshiplogoname", userMaster.getSocietyId().getTownshipId().getTownshiplogoname());
					jsontown.put("townshipiconame", userMaster.getSocietyId().getTownshipId().getTownshipiconame());
					jsontown.put("townshipcolourcode", userMaster.getSocietyId().getTownshipId().getTownshipcolourcode());
					jsontown.put("usersocietyid", userMaster.getSocietyId().getSocietyId());
					jsonObj.put("townshipdetail", jsontown);
					}
				jsonObj.put("menu", jArray);
				
				if(userMaster.getEmailId()!=null && Commonutility.checkempty(userMaster.getEmailId())){
				//Send Email
				EmailEngineServices emailservice = new EmailEngineDaoServices();
				EmailTemplateTblVo emailTemplate;
				
				try {
		            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Customer' and status ='1'";
		            emailTemplate = emailservice.emailTemplateData(emqry);
		            String emailMessage = emailTemplate.getTempContent();
		            
		            emqry = common.templateParser(emailMessage, 1, "", password);
		            String[] qrySplit = emqry.split("!_!");
		            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.emailId='"+userMaster.getEmailId()+"'";
		            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
		            emailTemplate.setTempContent(emailMessage);
		            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
		            
		            EmailInsertFuntion emailfun = new EmailInsertFuntion();
		            emailfun.test2(userMaster.getEmailId(), emailTemplate.getSubject(), emailMessage);
		          } catch (Exception ex) {
		            System.out.println("Excption found in Emailsend Forgetpassword.class : " + ex);
		            log.logMessage("Exception in signup admin flow emailFlow----> " + ex, "error",signUpProcess.class);
		            
		          }	
				}
				serverResponse(servicecode, "0000",
						"0", "sucess", jsonObj);
			} else {
				json=null;
				serverResponse(servicecode,"E0001",
						"1", "E0001", json);
			}
				
		//}
		/*else {
			json=null;
			serverResponse(servicecode, "E0001", "1",
					"EM0001", json);
		}*/
		}
		
		
		catch(Exception ex)
		{
			json=null;
			serverResponse(servicecode, "E0002", "2",
					"EM0002", json);
		}
		}
		else
		{
			json=new JSONObject();
			serverResponse("SI0001","1","E0001","Request values not not correct format",json);
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

	

}
