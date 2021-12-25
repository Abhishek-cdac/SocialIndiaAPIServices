package com.mobile.otp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.MvpUserOtpTbl;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.otpVo.otpValidateUtillity;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.mobile.signup.signUpDao;
import com.mobile.signup.signUpDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class mobRequestOtp extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	Query locQryrst=null;
	String locSlqry=null;
	GroupMasterTblVo locGrpMstrVOobj=null,locGRPMstrvo=null;
	 private SmsEngineServices smsService = new SmsEngineDaoServices();
	UserMasterTblVo userData=new UserMasterTblVo();
	MvpFamilymbrTbl userfamily=new MvpFamilymbrTbl();
	CommonUtils comutil=new CommonUtilsServices();
	profileDao profile=new profileDaoServices();
	List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
	SmsInpojo sms = new SmsInpojo();
	 private SmsTemplatepojo smsTemplate;
	CommonUtilsDao common=new CommonUtilsDao();	
	UserMasterTblVo userInfo=new UserMasterTblVo();
	UserMasterTblVo userMst=new UserMasterTblVo();
	MvpUserOtpTbl userOtpInfo=new MvpUserOtpTbl();
	otpDao otp=new otpDaoService();
	signUpDao signup=new signUpDaoServices();
	public String execute(){
		
		System.out.println("**********sign up services******************");
		JSONObject json = new JSONObject();
		 otpValidateUtillity otpUtill=new otpValidateUtillity();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		Session pSession1 = HibernateUtil.getSession();
		Session pSession = HibernateUtil.getSession();
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
		 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
		//String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
		locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		String mobileno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileno");
		String type = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "type");
		String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
		String otpfor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "otpfor");
		if (Commonutility.checkempty(servicecode)) {
			if (servicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
				
			} else {
				String[] passData = { getText("service.code.fixed.length") };
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
			}
		} else {
			flg = false;
			locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
		}
		if (Commonutility.checkempty(townshipKey)) {
			if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
				
			} else {
				String[] passData = { getText("townshipid.fixed.length") };
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
			}
		} else {
			flg = false;
			locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
		}
		/*if (Commonutility.checkempty(societykey)) {
			if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
				
			} else {
				String[] passData = { getText("society.fixed.length") };
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
			}
		} else {
			flg = false;
			locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
		}*/
		if(flg){
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			System.out.println("********result*****************"+result);
			if(result==true){
				/*boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
				System.out.println("===societyKeyCheck===="+societyKeyCheck);
				if(societyKeyCheck==true){*/
				int committeeGrpcode=0,residenteGrpcode=0;
				locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.committee")+"') or groupName=upper('"+getText("Grp.committee")+"')";			 
				locQryrst=pSession.createQuery(locSlqry);			
				 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
				 if(locGrpMstrVOobj!=null){
					 locGRPMstrvo=new GroupMasterTblVo();
					 committeeGrpcode=locGrpMstrVOobj.getGroupCode();
									 
				 }else{// new group create
					 uamDao uam=new uamDaoServices();
					 committeeGrpcode=uam.createnewgroup_rtnId(getText("Grp.committee"));
					 		 				
				 }	
				 
					locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.resident")+"') or groupName=upper('"+getText("Grp.resident")+"')";			 
					locQryrst=pSession1.createQuery(locSlqry);			
					 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
					 if(locGrpMstrVOobj!=null){
						 locGRPMstrvo=new GroupMasterTblVo();
						 residenteGrpcode=locGrpMstrVOobj.getGroupCode();
										 
					 }else{// new group create
						 uamDao uam=new uamDaoServices();
						 residenteGrpcode=uam.createnewgroup_rtnId(getText("Grp.resident"));
						 		 				
					 }	
					 String Otp_Length = "Otp_Length",Otp_Max_Count="Otp_Max_Count";
					 boolean upstsflg=true;
					 boolean checkUserActive=false;	
						String otpLength=otp.getSysParamsInfo(Otp_Length);
						System.out.println("=otpLength===="+otpLength);
						String otpMaxCount=otp.getSysParamsInfo(Otp_Max_Count);
//						String password=comutil.getRandomval("09", Integer.parseInt(otpLength));
						String password="123456";
				System.out.println("committeeGrpcode="+committeeGrpcode);
				System.out.println("residenteGrpcode="+residenteGrpcode);
				
				checkUserActive=signup.checkUserActSts(mobileno);
				
						userInfo=signup.checkUserSignUpInfo(mobileno,committeeGrpcode,residenteGrpcode,rid,otpfor);	
				Integer otpmaxcount=Integer.parseInt(otpMaxCount);
				if(userInfo!=null){
					boolean accessResult=signup.checkAccessMedia(mobileno);
					if(accessResult==true){
						
					
						userOtpInfo=otp.checkOtpInfo(String.valueOf(userInfo.getMobileNo()));
						System.out.println("============userOtpInfo======"+userOtpInfo);
//						if(userOtpInfo!=null && userOtpInfo.getOtpCount()!=null){
//							otpcount=userOtpInfo.getOtpCount();
//						}else{
							otpcount=0;
//						}
						boolean userResult=false;
					
						userResult=signup.checkUserOtpVerify(mobileno,committeeGrpcode,residenteGrpcode,rid,otpfor);	
						
						System.out.println("==========w===userResult=======dfdsdsfds=========="+userResult);
						if(userResult==true){
							
							System.out.println("=============userResult=======dfdsdsfds=========="+accessResult);
							System.out.println("===========otpmaxcount========"+otpmaxcount);
							System.out.println("===========otpcount========"+otpcount);
						if(otpmaxcount>otpcount ){
							  // userInfoList=otp.getUserDeatils(mobileno, rid,committeeGrpcode,residenteGrpcode);
							  userInfoList=profile.getUserSocietyDeatils(mobileno,committeeGrpcode,residenteGrpcode,otpfor);
						if(userOtpInfo!=null &&  type.equalsIgnoreCase("1") && userInfoList.size()==1){
							
							 updateResult=signup.updateOtpUser(mobileno, String.valueOf(userInfo.getUserId()),password,otpcount);
							    
						          
						       
									
									
									System.out.println("==userInfoList=2===="+userInfoList.size());
									JSONObject finalJsonarr=new JSONObject();
									 JSONArray jArray =new JSONArray();
										if( userInfoList!=null && userInfoList.size()>0){
											
											System.out.println("===================sms===========================");
									          try {
									            String mailRandamNumber;
									            mailRandamNumber = common.randInt(5555, 999999999);
									            String qry = "FROM SmsTemplatepojo where "
									                + "templateName ='OTP' and status ='1'";
									            smsTemplate = smsService.smsTemplateData(qry);
									            String smsMessage = smsTemplate.getTemplateContent();
									            qry = common.smsTemplateParser(smsMessage, 1, "", password);
									            String[] qrySplit = qry.split("!_!");
									            String qryform = qrySplit[0]+""
									           +" FROM UserMasterTblVo as cust where cust.userId='"  + userInfo.getUserId() + "'";
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
									            System.out.println("Exception sms send usercreate---->> " + ex);
									            log.logMessage("Exception in Forgetpassword() smsFlow----> "
									                + ex, "error", Forgetpassword.class);
									          }
									          
											UserMasterTblVo userObj;
										for (Iterator<UserMasterTblVo> it = userInfoList
												.iterator(); it.hasNext();) {
											userObj = it.next();
											locObjRspdataJson=new JSONObject();
											System.out.println("=userObj=========="+userObj.getFirstName());
											System.out.println("=userObj====dd======"+userObj.getMobileNo());
											finalJsonarr = new JSONObject();
											if(userObj.getSocietyId()!=null && userObj.getSocietyId().getLogoImage()!=null && !userObj.getSocietyId().getLogoImage().equalsIgnoreCase("") && !userObj.getSocietyId().getLogoImage().equalsIgnoreCase("null")){
											finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
											}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
												finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
											}else{
												finalJsonarr.put("societylogo", "");	
											}
											if( userObj.getSocietyId()!=null && userObj.getSocietyId().getSocietyName()!=null){
												finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());	
											}else{
												finalJsonarr.put("societyname","");
											}
											if(userObj.getSocietyId()!=null &&  userObj.getSocietyId().getActivationKey()!=null){
											finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
											}else{
												finalJsonarr.put("societykey", "");
											}
											if(userObj.getSocietyId()!=null &&  userObj.getSocietyId().getImprintName()!=null && !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
												finalJsonarr.put("societydesc", userObj.getSocietyId().getImprintName());
												}else{
													finalJsonarr.put("societydesc", "");	
												}
											if(userObj.getSocietyId().getNoOfMemebers()>0){
											finalJsonarr.put("totalusers",String.valueOf(userObj.getSocietyId().getNoOfMemebers()));
											}else{
												finalJsonarr.put("totalusers","");
											}
											if(userObj.getUserId()!=0){
												finalJsonarr.put("rid", String.valueOf(userObj.getUserId()));
											}else{
												finalJsonarr.put("rid", "");
											}
												System.out.println("====userObj.getParentId()========"+userObj.getParentId());
											
											if(userObj.getParentId()!=null && userObj.getParentId()!=0){
											userData=otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), rid,committeeGrpcode,residenteGrpcode);
											if(userData.getImageNameMobile()!=null && !userData.getImageNameMobile().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
												finalJsonarr.put("ppr_profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
											}else if(userData.getSocialProfileUrl()!=null && !userData.getSocialProfileUrl().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", userData.getSocialProfileUrl());
												finalJsonarr.put("ppr_profilepic", userData.getSocialProfileUrl());
											}else{
												finalJsonarr.put("ppr_profilepic_thumbnail", "");
												finalJsonarr.put("ppr_profilepic", "");
											}
												if(userData.getFirstName()!=null && userData.getFirstName()!=""&& !userData.getFirstName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_name", userData.getFirstName());
											}else{
												finalJsonarr.put("ppr_name", "");
											}
												if(userData.getSocietyId().getImprintName()!=null && userData.getSocietyId().getImprintName()!=""&& !userData.getSocietyId().getImprintName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_desc", userData.getSocietyId().getImprintName());
												}else{
													finalJsonarr.put("ppr_desc", "");	
												}if(userData.getMobileNo()!=null && !userData.getMobileNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_mobile", mobiCommon.maskMobileNo(userData.getMobileNo()));
												}else{
													finalJsonarr.put("ppr_mobile", "");
												}
												if(userData.getFlatNo()!=null && userData.getFlatNo()!="" && !userData.getFlatNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_block",String.valueOf(userData.getFlatNo()));
												}else{
													finalJsonarr.put("ppr_block", "");
												}
												if(userData.getUserId()!=0){
													finalJsonarr.put("ppr_id", String.valueOf(userData.getUserId()));
												}else{
													finalJsonarr.put("ppr_id", "");
												}
											}else{
												System.out.println("fgfgfgfgfgfgfgfgfgfgfgfgh");
												finalJsonarr.put("ppr_profilepic", "");
												finalJsonarr.put("ppr_profilepic_thumbnail", "");	
												finalJsonarr.put("ppr_name", "");
												finalJsonarr.put("ppr_desc", "");	
												finalJsonarr.put("ppr_mobile", "");
												finalJsonarr.put("ppr_block", "");
												finalJsonarr.put("ppr_id", "");
											}
											jArray.put(finalJsonarr);
											locObjRspdataJson.put("societies", jArray);
									 //locObjRspdataJson.put("otpfor", "1");
									
										}
										locObjRspdataJson.put("otp", password);
										locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
										locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
										serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
										
										}else{
												locObjRspdataJson=new JSONObject();
												finalJsonarr = new JSONObject();
												locObjRspdataJson.put("otp", "");
												locObjRspdataJson.put("resendcount", "");
												locObjRspdataJson.put("resendmaxcount", "");
												finalJsonarr.put("ppr_profilepic", "");
												finalJsonarr.put("ppr_profilepic_thumbnail", "");	
												finalJsonarr.put("ppr_name", "");
												finalJsonarr.put("ppr_desc", "");	
												finalJsonarr.put("ppr_mobile", "");
												finalJsonarr.put("ppr_block", "");
												finalJsonarr.put("ppr_id", "");
												jArray.put(finalJsonarr);
											serverResponse(servicecode,"01","R0018",mobiCommon.getMsg("R0018"),locObjRspdataJson);	
										}
									
						}else if(userInfoList.size()==1 && userOtpInfo==null){
								Date date;
								CommonUtils comutil=new CommonUtilsServices();
								date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
								System.out.println("============rid======"+userInfo.getUserId());
								//userData.setUserId(userInfo.getUserId());
								userOtpInfo=new MvpUserOtpTbl();
								//userOtpInfo.setUserId(userData);
								userOtpInfo.setMobileNo(mobileno);
								userOtpInfo.setOtp(password);
								userOtpInfo.setOtpCount(1);
								userOtpInfo.setStatus(1);
								userOtpInfo.setEntryBy(userInfo.getUserId());
								userOtpInfo.setOtpGeneratedTime(date);
								userOtpInfo.setEntryDatetime(date);
							boolean insertresult=otp.insertOtpInfo(userOtpInfo);
							System.out.println("====insertresult=="+insertresult);
						    
					          
					          System.out.println("========userInfoList===size===="+userInfoList.size());
								JSONObject finalJsonarr=new JSONObject();
								 JSONArray jArray =new JSONArray();
									if( userInfoList!=null && userInfoList.size()>0){
										System.out.println("===================sms===========================");
								          try {
								            String mailRandamNumber;
								            mailRandamNumber = common.randInt(5555, 999999999);
								            String qry = "FROM SmsTemplatepojo where "
								                + "templateName ='OTP' and status ='1'";
								            smsTemplate = smsService.smsTemplateData(qry);
								            String smsMessage = smsTemplate.getTemplateContent();
								            qry = common.smsTemplateParser(smsMessage, 1, "", password);
								            String[] qrySplit = qry.split("!_!");
								            String qryform = qrySplit[0]+""
								           +" FROM UserMasterTblVo as cust where cust.userId='"  + userInfo.getUserId() + "'";
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
								            System.out.println("Exception sms send usercreate---->> " + ex);
								            log.logMessage("Exception in Forgetpassword() smsFlow----> "
								                + ex, "error", Forgetpassword.class);
								          }
										UserMasterTblVo userObj;
									for (Iterator<UserMasterTblVo> it = userInfoList
											.iterator(); it.hasNext();) {
										userObj = it.next();
										finalJsonarr = new JSONObject();
										  locObjRspdataJson=new JSONObject();
										if(userObj.getSocietyId().getLogoImage()!=null){
										finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
										}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
											finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
										
										}else{
											finalJsonarr.put("societylogo", "");//need to doo	
										}
										finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
										finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
										if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
											finalJsonarr.put("societydesc", userInfo.getSocietyId().getImprintName());
											}else{
												finalJsonarr.put("societydesc", "");	
											}
										if( userObj.getSocietyId().getNoOfMemebers()>0){
										finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
										}else{
											finalJsonarr.put("totalusers","");	
										}
										if( userObj.getUserId()>0){
											finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
											}else{
												finalJsonarr.put("rid","");	
											}
										
										System.out.println("====userObj.getParentId()========"+userObj.getParentId());
										
										if(userObj.getParentId()!=null && userObj.getParentId()!=0){
										userData=otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), rid,committeeGrpcode,residenteGrpcode);
										
										if(userData.getImageNameMobile()!=null && !userData.getImageNameMobile().equalsIgnoreCase("")){
											finalJsonarr.put("ppr_profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
											finalJsonarr.put("ppr_profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
										}else if(userData.getSocialProfileUrl()!=null && !userData.getSocialProfileUrl().equalsIgnoreCase("")){
											finalJsonarr.put("ppr_profilepic_thumbnail", userData.getSocialProfileUrl());
											finalJsonarr.put("ppr_profilepic", userData.getSocialProfileUrl());
										}else{
											finalJsonarr.put("ppr_profilepic_thumbnail", "");
											finalJsonarr.put("ppr_profilepic", "");
										}
											if(userData.getFirstName()!=null && userData.getFirstName()!=""&& !userData.getFirstName().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_name", userData.getFirstName());
										}else{
											finalJsonarr.put("ppr_name", "");
										}
											if(userData.getSocietyId().getImprintName()!=null && userData.getSocietyId().getImprintName()!=""&& !userData.getSocietyId().getImprintName().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_desc", userData.getSocietyId().getImprintName());
											}else{
												finalJsonarr.put("ppr_desc", "");	
											}if(userData.getMobileNo()!=null && !userData.getMobileNo().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_mobile", mobiCommon.maskMobileNo(userData.getMobileNo()));
											}else{
												finalJsonarr.put("ppr_mobile", "");
											}
											if(userData.getFlatNo()!=null && userData.getFlatNo()!="" && !userData.getFlatNo().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_block",String.valueOf(userData.getFlatNo()));
											}else{
												finalJsonarr.put("ppr_block", "");
											}
											if(userData.getUserId()!=0){
												finalJsonarr.put("ppr_id", String.valueOf(userData.getUserId()));
											}else{
												finalJsonarr.put("ppr_id", "");
											}
										}else{
											System.out.println("fgfgfgfgfgfgfgfgfgfgfgfgh");
											finalJsonarr.put("ppr_profilepic", "");
											finalJsonarr.put("ppr_profilepic_thumbnail", "");	
											finalJsonarr.put("ppr_name", "");
											finalJsonarr.put("ppr_desc", "");	
											finalJsonarr.put("ppr_mobile", "");
											finalJsonarr.put("ppr_block", "");
											finalJsonarr.put("ppr_id", "");
										}
										jArray.put(finalJsonarr);
									}
									locObjRspdataJson.put("otp", password);
									locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
									locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
									locObjRspdataJson.put("societies", jArray);
									serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
									}else{
											locObjRspdataJson=new JSONObject();
											finalJsonarr = new JSONObject();
											locObjRspdataJson.put("otp", "");
											locObjRspdataJson.put("resendcount", "");
											locObjRspdataJson.put("resendmaxcount", "");
											finalJsonarr.put("ppr_profilepic", "");
											finalJsonarr.put("ppr_profilepic_thumbnail", "");	
											finalJsonarr.put("ppr_name", "");
											finalJsonarr.put("ppr_desc", "");	
											finalJsonarr.put("ppr_mobile", "");
											finalJsonarr.put("ppr_block", "");
											finalJsonarr.put("ppr_id", "");
											jArray.put(finalJsonarr);
										serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
									}
								
							}else if(userOtpInfo!=null && type.equalsIgnoreCase("1") && userInfoList.size()>1){
								updateResult=otp.updateOtpUser(mobileno, String.valueOf(userInfo.getUserId()),password,type,otpcount);
								 System.out.println("========userInfoList=wwww==size===="+userInfoList.size());
									JSONObject finalJsonarr=new JSONObject();
									 JSONArray jArray =new JSONArray();
										if( userInfoList!=null && userInfoList.size()>0){
											System.out.println("===================sms===========================");
									          try {
									            String mailRandamNumber;
									            mailRandamNumber = common.randInt(5555, 999999999);
									            String qry = "FROM SmsTemplatepojo where "
									                + "templateName ='OTP' and status ='1'";
									            smsTemplate = smsService.smsTemplateData(qry);
									            String smsMessage = smsTemplate.getTemplateContent();
									            qry = common.smsTemplateParser(smsMessage, 1, "", password);
									            String[] qrySplit = qry.split("!_!");
									            String qryform = qrySplit[0]+""
									           +" FROM UserMasterTblVo as cust where cust.userId='"  + userInfo.getUserId() + "'";
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
									            System.out.println("Exception sms send usercreate---->> " + ex);
									            log.logMessage("Exception in Forgetpassword() smsFlow----> "
									                + ex, "error", Forgetpassword.class);
									          }
											UserMasterTblVo userObj;
										for (Iterator<UserMasterTblVo> it = userInfoList
												.iterator(); it.hasNext();) {
											userObj = it.next();
											finalJsonarr = new JSONObject();
											  locObjRspdataJson=new JSONObject();
											if(userObj.getSocietyId().getLogoImage()!=null){
											finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
											}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
												finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
											
											}else{
												finalJsonarr.put("societylogo", "");//need to doo	
											}
											finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
											finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
											if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
												finalJsonarr.put("societydesc", userInfo.getSocietyId().getImprintName());
												}else{
													finalJsonarr.put("societydesc", "");	
												}
											if( userObj.getSocietyId().getNoOfMemebers()>0){
											finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
											}else{
												finalJsonarr.put("totalusers","");	
											}
											if( userObj.getUserId()>0){
												finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
												}else{
													finalJsonarr.put("rid","");	
												}
											
											System.out.println("====userObj.getParentId()========"+userObj.getParentId());
											
											if(userObj.getParentId()!=null && userObj.getParentId()!=0){
											userData=otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), rid,committeeGrpcode,residenteGrpcode);
											
											if(userData.getImageNameMobile()!=null && !userData.getImageNameMobile().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
												finalJsonarr.put("ppr_profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
											}else if(userData.getSocialProfileUrl()!=null && !userData.getSocialProfileUrl().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", userData.getSocialProfileUrl());
												finalJsonarr.put("ppr_profilepic", userData.getSocialProfileUrl());
											}else{
												finalJsonarr.put("ppr_profilepic_thumbnail", "");
												finalJsonarr.put("ppr_profilepic", "");
											}
												if(userData.getFirstName()!=null && userData.getFirstName()!=""&& !userData.getFirstName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_name", userData.getFirstName());
											}else{
												finalJsonarr.put("ppr_name", "");
											}
												if(userData.getSocietyId().getImprintName()!=null && userData.getSocietyId().getImprintName()!=""&& !userData.getSocietyId().getImprintName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_desc", userData.getSocietyId().getImprintName());
												}else{
													finalJsonarr.put("ppr_desc", "");	
												}if(userData.getMobileNo()!=null && !userData.getMobileNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_mobile", mobiCommon.maskMobileNo(userData.getMobileNo()));
												}else{
													finalJsonarr.put("ppr_mobile", "");
												}
												if(userData.getFlatNo()!=null && userData.getFlatNo()!="" && !userData.getFlatNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_block",String.valueOf(userData.getFlatNo()));
												}else{
													finalJsonarr.put("ppr_block", "");
												}
												if(userData.getUserId()!=0){
													finalJsonarr.put("ppr_id", String.valueOf(userData.getUserId()));
												}else{
													finalJsonarr.put("ppr_id", "");
												}
											}else{
												System.out.println("fgfgfgfgfgfgfgfgfgfgfgfgh");
												finalJsonarr.put("ppr_profilepic", "");
												finalJsonarr.put("ppr_profilepic_thumbnail", "");	
												finalJsonarr.put("ppr_name", "");
												finalJsonarr.put("ppr_desc", "");	
												finalJsonarr.put("ppr_mobile", "");
												finalJsonarr.put("ppr_block", "");
												finalJsonarr.put("ppr_id", "");
											}
											jArray.put(finalJsonarr);
										}
										locObjRspdataJson.put("otp", password);
										locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
										locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
										locObjRspdataJson.put("societies", jArray);
										serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
										}
							}
							else if( type.equalsIgnoreCase("1") && userInfoList.size()>1 && userOtpInfo==null ){
								Date date;
								CommonUtils comutil=new CommonUtilsServices();
								date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
								System.out.println("=====ssss=======rid======"+userInfo.getUserId());
								//userData.setUserId(userInfo.getUserId());
								userOtpInfo=new MvpUserOtpTbl();
								//userOtpInfo.setUserId(userData);
								userOtpInfo.setMobileNo(mobileno);
								userOtpInfo.setOtp(password);
								userOtpInfo.setOtpCount(1);
								userOtpInfo.setStatus(1);
								userOtpInfo.setEntryBy(userInfo.getUserId());
								userOtpInfo.setOtpGeneratedTime(date);
								userOtpInfo.setEntryDatetime(date);
							boolean insertresult=otp.insertOtpInfo(userOtpInfo);
							System.out.println("====insertresult=="+insertresult);
						    
					          
					          System.out.println("=====eee===userInfoList===size===="+userInfoList.size());
								JSONObject finalJsonarr=new JSONObject();
								 JSONArray jArray =new JSONArray();
									if( userInfoList!=null && userInfoList.size()>0){
										System.out.println("===================sms===========================");
								          try {
								            String mailRandamNumber;
								            mailRandamNumber = common.randInt(5555, 999999999);
								            String qry = "FROM SmsTemplatepojo where "
								                + "templateName ='OTP' and status ='1'";
								            smsTemplate = smsService.smsTemplateData(qry);
								            String smsMessage = smsTemplate.getTemplateContent();
								            qry = common.smsTemplateParser(smsMessage, 1, "", password);
								            String[] qrySplit = qry.split("!_!");
								            String qryform = qrySplit[0]+""
								           +" FROM UserMasterTblVo as cust where cust.userId='"  + userInfo.getUserId() + "'";
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
								            System.out.println("Exception sms send usercreate---->> " + ex);
								            log.logMessage("Exception in Forgetpassword() smsFlow----> "
								                + ex, "error", Forgetpassword.class);
								          }
										UserMasterTblVo userObj;
									for (Iterator<UserMasterTblVo> it = userInfoList
											.iterator(); it.hasNext();) {
										userObj = it.next();
										finalJsonarr = new JSONObject();
										  locObjRspdataJson=new JSONObject();
										if(userObj.getSocietyId().getLogoImage()!=null){
										finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
										}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
											finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
										
										}else{
											finalJsonarr.put("societylogo", "");//need to doo	
										}
										finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
										finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
										if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
											finalJsonarr.put("societydesc", userInfo.getSocietyId().getImprintName());
											}else{
												finalJsonarr.put("societydesc", "");	
											}
										if( userObj.getSocietyId().getNoOfMemebers()>0){
										finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
										}else{
											finalJsonarr.put("totalusers","");	
										}
										if( userObj.getUserId()>0){
											finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
											}else{
												finalJsonarr.put("rid","");	
											}
										
										if(userObj.getParentId()!=null && userObj.getParentId()!=0){
											userData=otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), rid,committeeGrpcode,residenteGrpcode);
											
											if(userData.getImageNameMobile()!=null && !userData.getImageNameMobile().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
												finalJsonarr.put("ppr_profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
											}else if(userData.getSocialProfileUrl()!=null && !userData.getSocialProfileUrl().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", userData.getSocialProfileUrl());
												finalJsonarr.put("ppr_profilepic", userData.getSocialProfileUrl());
											}else{
												finalJsonarr.put("ppr_profilepic_thumbnail", "");
												finalJsonarr.put("ppr_profilepic", "");
											}
												if(userData.getFirstName()!=null && userData.getFirstName()!=""&& !userData.getFirstName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_name", userData.getFirstName());
											}else{
												finalJsonarr.put("ppr_name", "");
											}
												if(userData.getSocietyId().getImprintName()!=null && userData.getSocietyId().getImprintName()!=""&& !userData.getSocietyId().getImprintName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_desc", userData.getSocietyId().getImprintName());
												}else{
													finalJsonarr.put("ppr_desc", "");	
												}if(userData.getMobileNo()!=null && !userData.getMobileNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_mobile", mobiCommon.maskMobileNo(userData.getMobileNo()));
												}else{
													finalJsonarr.put("ppr_mobile", "");
												}
												if(userData.getFlatNo()!=null && userData.getFlatNo()!="" && !userData.getFlatNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_block",String.valueOf(userData.getFlatNo()));
												}else{
													finalJsonarr.put("ppr_block", "");
												}
												if(userData.getUserId()!=0){
													finalJsonarr.put("ppr_id", String.valueOf(userData.getUserId()));
												}else{
													finalJsonarr.put("ppr_id", "");
												}
											}else{
												System.out.println("fgfgfgfgfgfgfgfgfgfgfgfgh");
												finalJsonarr.put("ppr_profilepic", "");
												finalJsonarr.put("ppr_profilepic_thumbnail", "");	
												finalJsonarr.put("ppr_name", "");
												finalJsonarr.put("ppr_desc", "");	
												finalJsonarr.put("ppr_mobile", "");
												finalJsonarr.put("ppr_block", "");
												finalJsonarr.put("ppr_id", "");
											}
										jArray.put(finalJsonarr);
									}
									locObjRspdataJson.put("otp", password);
									locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
									locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
									locObjRspdataJson.put("societies", jArray);
									serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
										}
							}
						
						
						else if(userOtpInfo!=null && otpmaxcount>otpcount && type.equalsIgnoreCase("2") && userInfoList.size()==1){
							System.out.println("==otpmaxcount==="+otpmaxcount);
							System.out.println("==otpcountotpcountotpcountotpcount==="+otpcount);
							// updateResult=signup.updateOtpUser(mobileno, String.valueOf(userInfo.getUserId()),password,otpcount);
							updateResult=otp.updateOtpUser(mobileno, String.valueOf(userInfo.getUserId()),password,type,otpcount);
							//userOtpInfo=otp.checkOtpInfo(String.valueOf(userInfo.getUserId()));
							/*if(userOtpInfo!=null && userOtpInfo.getOtpCount()!=null){
								otpcount=userOtpInfo.getOtpCount();
							}else{
								otpcount=0;
							}*/
							System.out.println("=========otpcount===222======"+otpcount);
							System.out.println("=========otpmaxcount===2222======"+otpmaxcount);
							
								
						          userInfoList=profile.getUserSocietyDeatils(mobileno,committeeGrpcode,residenteGrpcode,otpfor);
						          //userInfoList=otp.getUserDeatils(mobileno, rid,committeeGrpcode,residenteGrpcode);
						          System.out.println("========userInfoList==33333333=size===="+userInfoList.size());
									JSONObject finalJsonarr=new JSONObject();
									 JSONArray jArray =new JSONArray();
										if( userInfoList!=null && userInfoList.size()>0){
											 // Sending SMS
									          System.out.println("===================sms===========================");
									          try {
									            String mailRandamNumber;
									            mailRandamNumber = common.randInt(5555, 999999999);
									            String qry = "FROM SmsTemplatepojo where "
									                + "templateName ='OTP' and status ='1'";
									            smsTemplate = smsService.smsTemplateData(qry);
									            String smsMessage = smsTemplate.getTemplateContent();
									            qry = common.smsTemplateParser(smsMessage, 1, "", password);
									            String[] qrySplit = qry.split("!_!");
									            String qryform = qrySplit[0]+""
									           +" FROM UserMasterTblVo as cust where cust.userId='"  + userInfo.getUserId() + "'";
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
									            System.out.println("Exception sms send usercreate---->> " + ex);
									            log.logMessage("Exception in Forgetpassword() smsFlow----> "
									                + ex, "error", Forgetpassword.class);
									          }
											UserMasterTblVo userObj;
										for (Iterator<UserMasterTblVo> it = userInfoList
												.iterator(); it.hasNext();) {
											userObj = it.next();
											finalJsonarr = new JSONObject();
											  locObjRspdataJson=new JSONObject();
											if(userObj.getSocietyId().getLogoImage()!=null){
											finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
											}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
												finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
											
											}else{
												finalJsonarr.put("societylogo", "");//need to doo	
											}
											finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
											finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
											if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
												finalJsonarr.put("societydesc", userInfo.getSocietyId().getImprintName());
												}else{
													finalJsonarr.put("societydesc", "");	
												}
											if( userObj.getSocietyId().getNoOfMemebers()>0){
											finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
											}else{
												finalJsonarr.put("totalusers","");	
											}
											if( userObj.getUserId()>0){
												finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
												}else{
													finalJsonarr.put("rid","");	
												}
											
												System.out.println("====userObj.getParentId()========"+userObj.getParentId());
											
											if(userObj.getParentId()!=null && userObj.getParentId()!=0){
											userData=otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), rid,committeeGrpcode,residenteGrpcode);
											
											if(userData.getImageNameMobile()!=null && !userData.getImageNameMobile().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
												finalJsonarr.put("ppr_profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
											}else if(userData.getSocialProfileUrl()!=null && !userData.getSocialProfileUrl().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_profilepic_thumbnail", userData.getSocialProfileUrl());
												finalJsonarr.put("ppr_profilepic", userData.getSocialProfileUrl());
											}else{
												finalJsonarr.put("ppr_profilepic_thumbnail", "");
												finalJsonarr.put("ppr_profilepic", "");
											}
												if(userData.getFirstName()!=null && userData.getFirstName()!=""&& !userData.getFirstName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_name", userData.getFirstName());
											}else{
												finalJsonarr.put("ppr_name", "");
											}
												if(userData.getSocietyId().getImprintName()!=null && userData.getSocietyId().getImprintName()!=""&& !userData.getSocietyId().getImprintName().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_desc", userData.getSocietyId().getImprintName());
												}else{
													finalJsonarr.put("ppr_desc", "");	
												}if(userData.getMobileNo()!=null && !userData.getMobileNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_mobile", mobiCommon.maskMobileNo(userData.getMobileNo()));
												}else{
													finalJsonarr.put("ppr_mobile", "");
												}
												if(userData.getFlatNo()!=null && userData.getFlatNo()!="" && !userData.getFlatNo().equalsIgnoreCase("")){
													finalJsonarr.put("ppr_block",String.valueOf(userData.getFlatNo()));
												}else{
													finalJsonarr.put("ppr_block", "");
												}
												if(userData.getUserId()!=0){
													finalJsonarr.put("ppr_id", String.valueOf(userData.getUserId()));
												}else{
													finalJsonarr.put("ppr_id", "");
												}
											}else{
												finalJsonarr.put("ppr_profilepic", "");
												finalJsonarr.put("ppr_profilepic_thumbnail", "");	
												finalJsonarr.put("ppr_name", "");
												finalJsonarr.put("ppr_desc", "");	
												finalJsonarr.put("ppr_mobile", "");
												finalJsonarr.put("ppr_block", "");
												finalJsonarr.put("ppr_id", "");
											}
											jArray.put(finalJsonarr);
										}
										locObjRspdataJson.put("otp", password);
										locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
										locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
										locObjRspdataJson.put("societies", jArray);
										serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
										}else{
											locObjRspdataJson=new JSONObject();
											finalJsonarr = new JSONObject();
											locObjRspdataJson.put("otp", "");
											locObjRspdataJson.put("resendcount", "");
											locObjRspdataJson.put("resendmaxcount", "");
											finalJsonarr.put("ppr_profilepic", "");
											finalJsonarr.put("ppr_profilepic_thumbnail", "");	
											finalJsonarr.put("ppr_name", "");
											finalJsonarr.put("ppr_desc", "");	
											finalJsonarr.put("ppr_mobile", "");
											finalJsonarr.put("ppr_block", "");
											finalJsonarr.put("ppr_id", "");
											jArray.put(finalJsonarr);
										serverResponse(servicecode,"01","R0018",mobiCommon.getMsg("R0018"),locObjRspdataJson);
									}
								
						}else if(type.equalsIgnoreCase("2") &&  userInfoList.size()>1){
							
							updateResult=otp.updateOtpUser(mobileno, String.valueOf(userInfo.getUserId()),password,type,otpcount);
							 System.out.println("========userInfoList===size===="+userInfoList.size());
								JSONObject finalJsonarr=new JSONObject();
								 JSONArray jArray =new JSONArray();
									if( userInfoList!=null && userInfoList.size()>0){
										System.out.println("===================sms===========================");
								          try {
								            String mailRandamNumber;
								            mailRandamNumber = common.randInt(5555, 999999999);
								            String qry = "FROM SmsTemplatepojo where "
								                + "templateName ='OTP' and status ='1'";
								            smsTemplate = smsService.smsTemplateData(qry);
								            String smsMessage = smsTemplate.getTemplateContent();
								            qry = common.smsTemplateParser(smsMessage, 1, "", password);
								            String[] qrySplit = qry.split("!_!");
								            String qryform = qrySplit[0]+""
								           +" FROM UserMasterTblVo as cust where cust.userId='"  + userInfo.getUserId() + "'";
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
								            System.out.println("Exception sms send usercreate---->> " + ex);
								            log.logMessage("Exception in Forgetpassword() smsFlow----> "
								                + ex, "error", Forgetpassword.class);
								          }
										UserMasterTblVo userObj;
									for (Iterator<UserMasterTblVo> it = userInfoList
											.iterator(); it.hasNext();) {
										userObj = it.next();
										finalJsonarr = new JSONObject();
										  locObjRspdataJson=new JSONObject();
										if(userObj.getSocietyId().getLogoImage()!=null){
										finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
										}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
											finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
										
										}else{
											finalJsonarr.put("societylogo", "");//need to doo	
										}
										finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
										finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
										if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
											finalJsonarr.put("societydesc", userInfo.getSocietyId().getImprintName());
											}else{
												finalJsonarr.put("societydesc", "");	
											}
										if( userObj.getSocietyId().getNoOfMemebers()>0){
										finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
										}else{
											finalJsonarr.put("totalusers","");	
										}
										if( userObj.getUserId()>0){
											finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
											}else{
												finalJsonarr.put("rid","");	
											}
										
										System.out.println("====userObj.getParentId()========"+userObj.getParentId());
										
										if(userObj.getParentId()!=null && userObj.getParentId()!=0){
										userData=otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), rid,committeeGrpcode,residenteGrpcode);
										
										if(userData.getImageNameMobile()!=null && !userData.getImageNameMobile().equalsIgnoreCase("")){
											finalJsonarr.put("ppr_profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
											finalJsonarr.put("ppr_profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userData.getUserId()+"/"+userData.getImageNameMobile());
										}else if(userData.getSocialProfileUrl()!=null && !userData.getSocialProfileUrl().equalsIgnoreCase("")){
											finalJsonarr.put("ppr_profilepic_thumbnail", userData.getSocialProfileUrl());
											finalJsonarr.put("ppr_profilepic", userData.getSocialProfileUrl());
										}else{
											finalJsonarr.put("ppr_profilepic_thumbnail", "");
											finalJsonarr.put("ppr_profilepic", "");
										}
											if(userData.getFirstName()!=null && userData.getFirstName()!=""&& !userData.getFirstName().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_name", userData.getFirstName());
										}else{
											finalJsonarr.put("ppr_name", "");
										}
											if(userData.getSocietyId().getImprintName()!=null && userData.getSocietyId().getImprintName()!=""&& !userData.getSocietyId().getImprintName().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_desc", userData.getSocietyId().getImprintName());
											}else{
												finalJsonarr.put("ppr_desc", "");	
											}if(userData.getMobileNo()!=null && !userData.getMobileNo().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_mobile", mobiCommon.maskMobileNo(userData.getMobileNo()));
											}else{
												finalJsonarr.put("ppr_mobile", "");
											}
											if(userData.getFlatNo()!=null && userData.getFlatNo()!="" && !userData.getFlatNo().equalsIgnoreCase("")){
												finalJsonarr.put("ppr_block",String.valueOf(userData.getFlatNo()));
											}else{
												finalJsonarr.put("ppr_block", "");
											}
											if(userData.getUserId()!=0){
												finalJsonarr.put("ppr_id", String.valueOf(userData.getUserId()));
											}else{
												finalJsonarr.put("ppr_id", "");
											}
										}else{
											System.out.println("fgfgfgfgfgfgfgfgfgfgfgfgh");
											finalJsonarr.put("ppr_profilepic", "");
											finalJsonarr.put("ppr_profilepic_thumbnail", "");	
											finalJsonarr.put("ppr_name", "");
											finalJsonarr.put("ppr_desc", "");	
											finalJsonarr.put("ppr_mobile", "");
											finalJsonarr.put("ppr_block", "");
											finalJsonarr.put("ppr_id", "");
										}
										jArray.put(finalJsonarr);
									}
									locObjRspdataJson.put("otp", password);
									locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
									locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
									locObjRspdataJson.put("societies", jArray);
									serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
									}
									
						}
						
						/*else{
							 upstsflg=otp.updateOtpUserStaFlg(String.valueOf(userInfo.getUserId()));
							 JSONObject finalJsonarr=new JSONObject();
							 JSONArray jArray =new JSONArray();
								locObjRspdataJson=new JSONObject();
								finalJsonarr = new JSONObject();
								locObjRspdataJson.put("otp", "");
								locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
								locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
								finalJsonarr.put("ppr_profilepic", "");
								finalJsonarr.put("ppr_profilepic_thumbnail", "");	
								finalJsonarr.put("ppr_name", "");
								finalJsonarr.put("ppr_desc", "");	
								finalJsonarr.put("ppr_mobile", "");
								finalJsonarr.put("ppr_block", "");
								finalJsonarr.put("ppr_id", "");
								jArray.put(finalJsonarr);
								locObjRspdataJson.put("societies", jArray);
							serverResponse(servicecode,"00","R0017",getText("R0017"),locObjRspdataJson);
							}*/
						System.out.println("===upstsflg==="+upstsflg);
						
						
						}else{
						 upstsflg=otp.updateOtpUserStaFlg(mobileno);
						 JSONObject finalJsonarr=new JSONObject();
						 JSONArray jArray =new JSONArray();
							locObjRspdataJson=new JSONObject();
							finalJsonarr = new JSONObject();
							locObjRspdataJson.put("otp", "");
							locObjRspdataJson.put("resendcount", String.valueOf(otpcount));
							locObjRspdataJson.put("resendmaxcount", String.valueOf(otpmaxcount));
							finalJsonarr.put("ppr_profilepic", "");
							finalJsonarr.put("ppr_profilepic_thumbnail", "");	
							finalJsonarr.put("ppr_name", "");
							finalJsonarr.put("ppr_desc", "");	
							finalJsonarr.put("ppr_mobile", "");
							finalJsonarr.put("ppr_block", "");
							finalJsonarr.put("ppr_id", "");
							jArray.put(finalJsonarr);
							locObjRspdataJson.put("societies", jArray);
							serverResponse(servicecode,"00","R0017",mobiCommon.getMsg("R0017"),locObjRspdataJson);
						}
						}else{
							 JSONObject finalJsonarr=new JSONObject();
							 JSONArray jArray =new JSONArray();
								locObjRspdataJson=new JSONObject();
								finalJsonarr = new JSONObject();
								locObjRspdataJson.put("otp", "");
								locObjRspdataJson.put("resendcount", "");
								locObjRspdataJson.put("resendmaxcount", "");
								finalJsonarr.put("ppr_profilepic", "");
								finalJsonarr.put("ppr_profilepic_thumbnail", "");	
								finalJsonarr.put("ppr_name", "");
								finalJsonarr.put("ppr_desc", "");	
								finalJsonarr.put("ppr_mobile", "");
								finalJsonarr.put("ppr_block", "");
								finalJsonarr.put("ppr_id", "");
								jArray.put(finalJsonarr);
								locObjRspdataJson.put("societies", jArray);
							serverResponse(servicecode,"01","R0032",mobiCommon.getMsg("R0032"),locObjRspdataJson);
						}
					}else{
						 JSONObject finalJsonarr=new JSONObject();
						 JSONArray jArray =new JSONArray();
							locObjRspdataJson=new JSONObject();
							finalJsonarr = new JSONObject();
							locObjRspdataJson.put("otp", "");
							locObjRspdataJson.put("resendcount", "");
							locObjRspdataJson.put("resendmaxcount", "");
							finalJsonarr.put("ppr_profilepic", "");
							finalJsonarr.put("ppr_profilepic_thumbnail", "");	
							finalJsonarr.put("ppr_name", "");
							finalJsonarr.put("ppr_desc", "");	
							finalJsonarr.put("ppr_mobile", "");
							finalJsonarr.put("ppr_block", "");
							finalJsonarr.put("ppr_id", "");
							jArray.put(finalJsonarr);
						serverResponse(servicecode,"01","R0022",mobiCommon.getMsg("R0022"),locObjRspdataJson);
					}
					
				}else if(checkUserActive==true){
					 JSONObject finalJsonarr=new JSONObject();
					 JSONArray jArray =new JSONArray();
						locObjRspdataJson=new JSONObject();
						finalJsonarr = new JSONObject();
						locObjRspdataJson.put("otp", "");
						locObjRspdataJson.put("resendcount", "");
						locObjRspdataJson.put("resendmaxcount", "");
						finalJsonarr.put("ppr_profilepic", "");
						finalJsonarr.put("ppr_profilepic_thumbnail", "");	
						finalJsonarr.put("ppr_name", "");
						finalJsonarr.put("ppr_desc", "");	
						finalJsonarr.put("ppr_mobile", "");
						finalJsonarr.put("ppr_block", "");
						finalJsonarr.put("ppr_id", "");
						jArray.put(finalJsonarr);
					serverResponse(servicecode,"01","R0032",mobiCommon.getMsg("R0032"),locObjRspdataJson);
					}else{
						 JSONObject finalJsonarr=new JSONObject();
						 JSONArray jArray =new JSONArray();
							locObjRspdataJson=new JSONObject();
							finalJsonarr = new JSONObject();
							locObjRspdataJson.put("otp", "");
							locObjRspdataJson.put("resendcount", "");
							locObjRspdataJson.put("resendmaxcount", "");
							finalJsonarr.put("ppr_profilepic", "");
							finalJsonarr.put("ppr_profilepic_thumbnail", "");	
							finalJsonarr.put("ppr_name", "");
							finalJsonarr.put("ppr_desc", "");	
							finalJsonarr.put("ppr_mobile", "");
							finalJsonarr.put("ppr_block", "");
							finalJsonarr.put("ppr_id", "");
							jArray.put(finalJsonarr);
						serverResponse(servicecode,"01","R0018",mobiCommon.getMsg("R0018"),locObjRspdataJson);
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
				}
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+", Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======mobile request otp====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, mobile request otp an unhandled error occurred", "info", mobRequestOtp.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}finally{
			if(pSession1!=null){pSession1.clear();pSession1.close();pSession1 = null;}
		if(pSession!=null){pSession.clear();pSession.close();pSession = null;}
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
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
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
