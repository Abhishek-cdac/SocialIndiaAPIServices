package com.siservices.uamm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.bitly.GetBitlyLink;
import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.signUpProcess;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class userCreation extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<UserMasterTblVo> userList = new ArrayList<UserMasterTblVo>();
	private GroupMasterTblVo groupMaster = new GroupMasterTblVo();
	private SocietyMstTbl societyMaster=new SocietyMstTbl();
	uamDao uam = new uamDaoServices();
	signUpDao signup = new signUpDaoServices();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	CommonUtils comutil = new CommonUtilsServices();
	JSONObject jsonFinalObj = new JSONObject();
	SmsInpojo sms = new SmsInpojo();
	String locSlqry = null;
	Query locQryrst = null;
	GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
	CommonUtilsDao common = new CommonUtilsDao();
	private SmsTemplatepojo smsTemplate;
	private SmsEngineServices smsService = new SmsEngineDaoServices();
	Log log = new Log();
	boolean flag = true;
	
	public String execute() throws JSONException{
		int no_ofFlats=0;
		JSONObject json = new JSONObject();
		Session pSession = null;
		try{
			pSession = HibernateUtil.getSession();;
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				 json = new JSONObject(ivrparams);
				 String servicecode=json.getString("servicecode");	
				 Object lvrSoctyidobj = (Object) Commonutility.toHasChkJsonRtnValObj(json, "societyId"); 		
				 String lvrSoctyidstr = Commonutility.toCheckNullEmpty(lvrSoctyidobj);
		
		
		String emailId = json.getString("emailId");
		String mobileNo=json.getString("mobileNo");	
		String isdNo=json.getString("isdno");	
		int idcardtype=json.getInt("idcardtype");
		String idcardno=json.getString("idcardno");
		String firstName=json.getString("firstName");
		String lastName = json.getString("lastName");			
		String gender=json.getString("gender");
		String dob = json.getString("dob");	
		String blockNameList = (String) Commonutility.toHasChkJsonRtnValObj(json, "blockNameList");
		String flatNameList = (String) Commonutility.toHasChkJsonRtnValObj(json, "flatNameList");
		String famName = (String) Commonutility.toHasChkJsonRtnValObj(json, "famName");
		String famMobileNo = (String) Commonutility.toHasChkJsonRtnValObj(json, "famMobileNo");
		String famEmailId = (String) Commonutility.toHasChkJsonRtnValObj(json, "famEmailId");
		String noofwings = (String) Commonutility.toHasChkJsonRtnValObj(json, "noofwings");
		String noOfFlats=json.getString("noofflats");
		String famISD = (String) Commonutility.toHasChkJsonRtnValObj(json, "famisdcode");
		String famMemTyp = (String) Commonutility.toHasChkJsonRtnValObj(json, "fammemtype");
		String famPrfAccess = (String) Commonutility.toHasChkJsonRtnValObj(json, "famprfaccess");
		if(noOfFlats!=null && !noOfFlats.equalsIgnoreCase("") && !noOfFlats.equalsIgnoreCase("null")){
			no_ofFlats=Integer.parseInt(noOfFlats);
		}else{
			no_ofFlats=0;	
		}
		int noOfLogins=json.getInt("numOfLogins");
		
		String address1=json.getString("address1");
		String address2 = json.getString("address2");		
		Integer countryid=json.getInt("countryid");
		Integer stateid = json.getInt("stateid");
		Integer cityid=json.getInt("cityid");
		int pinid = json.getInt("pinid");
		String occupation = json.getString("occupation");
		Integer familyno=json.getInt("familyno");
		String bloodType = json.getString("bloodgroup");
		Integer accesschannel=json.getInt("accesschannel");
		int groupCode=json.getInt("groupCode");		
		int locGrpcode=0;		
		locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.social.admin")+"') or groupName=upper('"+getText("Grp.social.admin")+"')";			 
		locQryrst=pSession.createQuery(locSlqry);			
		locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
		if(locGrpMstrVOobj!=null){
			 locGRPMstrvo=new GroupMasterTblVo();
			 locGrpcode=locGrpMstrVOobj.getGroupCode();
							 
		} else {// new group create
			 uamDao uam=new uamDaoServices();
			 locGrpcode=uam.createnewgroup_rtnId(getText("Grp.social.admin"));
			 		 				
		}	
		
		String password=comutil.getRandomval("AZ_09", 10);
		
//		String password="1234567890";
	
		Date date;
		date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");		
		//userInfo.setUserName(userName);
		int societyId = 0;
		
		if(lvrSoctyidstr!=null && lvrSoctyidstr!="0" && !lvrSoctyidstr.equalsIgnoreCase("0") && Commonutility.toCheckisNumeric(lvrSoctyidstr)){
			 societyId = Integer.parseInt(lvrSoctyidstr);			 
			 societyMaster.setSocietyId(societyId);
			 userInfo.setSocietyId(societyMaster);
		}else{			 
			userInfo.setSocietyId(null);
		}			
		userInfo.setPassword(comutil.stringToMD5(password));
		userInfo.setEncryprPassword(EncDecrypt.encrypt(password));
		userInfo.setGroupUniqId("temp");
		if(emailId!=null){
			userInfo.setEmailId(emailId);
		}		
		userInfo.setIsdCode(isdNo);
		userInfo.setMobileNo(mobileNo);
		IDCardMasterTblVO iVOcradid=new IDCardMasterTblVO();
		CountryMasterTblVO countryInfo=new CountryMasterTblVO();
		StateMasterTblVO stateInfo=new StateMasterTblVO();
		CityMasterTblVO cityInfo=new CityMasterTblVO();
		PostalCodeMasterTblVO postMst=new PostalCodeMasterTblVO();
		String userregUniId=Commonutility.toGetKeyIDforTbl("UserMasterTblVo","max(userId)","5","7");		
		if(idcardtype!=0){
		iVOcradid.setiVOcradid(idcardtype);
		userInfo.setiVOcradid(iVOcradid);
		}/*else{
			iVOcradid.setiVOcradid(null);
		}*/		
		if(idcardno!=null){
			userInfo.setIdProofNo(idcardno);	
		}
		userInfo.setUserName(userregUniId);
		
			
		userInfo.setFirstName(firstName);
		userInfo.setLastName(lastName);
		userInfo.setGender(gender);
		userInfo.setNoofblocks(noofwings);
		userInfo.setDob(dob);
		userInfo.setNoofflats(no_ofFlats);
		//userInfo.setFlatNo(flatNo);
		userInfo.setAddress1(address1);
		userInfo.setAddress2(address2);
		if(countryid!=0){
		countryInfo.setCountryId(countryid);
		userInfo.setCountryId(countryInfo);
		}if(stateid!=0){
		stateInfo.setStateId(stateid);
		userInfo.setStateId(stateInfo);
		}if(cityid!=0){
		cityInfo.setCityId(cityid);
		userInfo.setCityId(cityInfo);
		}if(pinid!=0){
			postMst.setPstlId(pinid);
//			userInfo.setPstlId(postMst);
			userInfo.setPstlId(pinid);
		}
		userInfo.setOccupation(occupation);
		userInfo.setMembersInFamily(familyno);
		userInfo.setBloodType(bloodType);	
		userInfo.setAccessChannel(accesschannel);
		userInfo.setStatusFlag(1);
		if(locGrpcode!=0 && groupCode!=0){
			groupMaster.setGroupCode(locGrpcode);
			userInfo.setGroupCode(groupMaster);	
		}
		userInfo.setEntryDatetime(date);	
		userInfo.setNumOfLogins(noOfLogins);
		
		//Commonutility.toWriteConsole("blockNameList >> "+blockNameList);
		//Commonutility.toWriteConsole("flatNameList >> "+flatNameList);
		String result = uam.userCreation(userInfo,blockNameList,flatNameList,famName,famMobileNo,famEmailId,societyMaster,groupMaster,userInfo.getAccessChannel(),famISD,famMemTyp,famPrfAccess);
		if (result.equalsIgnoreCase("EmailExist")) {
			json=null;
			serverResponse(servicecode,"E0001","1", "EmailExist", json);
		}else if(result.equalsIgnoreCase("mobnoExist")){
			json=null;
			serverResponse(servicecode,"E0001","1", "mobnoExist", json);
		}else if(result.equalsIgnoreCase("userExist")){
			json=null;
			serverResponse(servicecode,"E0001","1", "userExist", json);
		}
		if (result.equalsIgnoreCase("insertsuccess")) {
			// bitly url call
			String locbityurl = GetBitlyLink.toGetBitlyLinkMthd(System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.login.url"), "yes", getText("bitly.accesstocken"), getText("bitly.server.url"));
			//Send Email			
			if(userInfo.getEmailId()!=null && Commonutility.checkempty(userInfo.getEmailId())){
			EmailEngineServices emailservice = new EmailEngineDaoServices();
			EmailTemplateTblVo emailTemplate;
			
			try {
	            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Customer' and status ='1'";
	            emailTemplate = emailservice.emailTemplateData(emqry);
	            String emailMessage = emailTemplate.getTempContent();
	            
	            emqry = common.templateParser(emailMessage, 1, "", password);
	            String[] qrySplit = emqry.split("!_!");
	            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.mobileNo='"+userInfo.getMobileNo()+"'";
	            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
	            emailTemplate.setTempContent(emailMessage);
	            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
	            
	            EmailInsertFuntion emailfun = new EmailInsertFuntion();
	            emailfun.test2(userInfo.getEmailId(), emailTemplate.getSubject(), emailMessage);
	          } catch (Exception ex) {
	            System.out.println("Excption found in Emailsend Forgetpassword.class : " + ex);
	            log.logMessage("Exception in signup admin flow emailFlow----> " + ex, "error",signUpProcess.class);
	            
	          }	
			}
			 // Sending SMS

	          System.out
	              .println("===================sms===========================");
	          try {
	            String mailRandamNumber;
	            mailRandamNumber = common.randInt(5555, 999999999);
	            String qry = "FROM SmsTemplatepojo where "
	                + "templateName ='Create Customer' and status ='1'";
	            smsTemplate = smsService.smsTemplateData(qry);
	            String smsMessage = smsTemplate.getTemplateContent();
	            qry = common.smsTemplateParser(smsMessage, 1, "", password);
	            String[] qrySplit = qry.split("!_!");
	            String qryform = qrySplit[0]
	                + " FROM UserMasterTblVo as cust where cust.mobileNo='"
	                + userInfo.getMobileNo() + "'";
	            smsMessage = smsService.smsTemplateParserChange(qryform,
	                Integer.parseInt(qrySplit[1]), smsMessage);
	            sms.setSmsCardNo(mailRandamNumber);
	            sms.setSmsEntryDateTime(common
	                .getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
	            sms.setSmsMobNumber(userInfo.getMobileNo());
	            sms.setSmspollFlag("F");
	            sms.setSmsMessage(smsMessage);
	            smsService.insertSmsInTable(sms);
	          } catch (Exception ex) {
	        	  System.out.println("Excption found in smssend Forgetpassword.class : " + ex);
		            log.logMessage("Exception in signup admin flow emailFlow----> " + ex, "error",signUpProcess.class);
	           
	          }
			jsonFinalObj.put("resultType", result);
		
			serverResponse(servicecode,"0", "0000", "sucess", jsonFinalObj);
		}else{
			jsonFinalObj = new JSONObject();
			serverResponse(servicecode,"1", "E0001", "eorror", jsonFinalObj);
		}
			}
			else
			{
				json=new JSONObject();
				serverResponse("SI0007","1","E0001","Request values not not correct format",json);
			}
			System.out.println("---------Completed services Block-----------------");
		}catch(Exception ex){
			log.logMessage("Service : userCreation error occurred", "info", userCreation.class);
			System.out.println("Exception Found UserCreation.class [Services]: "+ex);
		} finally {
			if(pSession!=null){pSession.flush();pSession.clear();pSession.close();pSession=null;}
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
			log.logMessage("Service : userCreation error occurred : " + ex, "error",userCreation.class);
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
