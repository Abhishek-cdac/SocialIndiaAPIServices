package com.mobile.profile;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.bitly.GetBitlyLink;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.signUpProcess;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.Log;

public class profileUpdate extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	profileDao profile=new profileDaoServices();
	List<MvpUsrSkillTbl> userSkillList=new ArrayList<MvpUsrSkillTbl>();
	UserMasterTblVo userMst=new UserMasterTblVo();
	public String execute(){
		
		System.out.println("************profile Update services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("============ivrparams================"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				
				String profile_name = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "profile_name");
				String profile_email = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "profile_email");
				String profile_proof_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "profile_proof_id");
				String profile_proof_value = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "profile_proof_value");
				String flatId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "flat_id");
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
				if (Commonutility.checkempty(societykey)) {
					if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
						
					} else {
						String[] passData = { getText("society.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
				}
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(profile_email)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("profile_email")));
					}
				}
				
				
				if(flg){
					
					
					boolean result=otp.checkTownshipKey(rid,townshipKey);
			System.out.println("********result*****************"+result);
			int count=0;String locVrSlQry="";
			if(result==true){
				boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
				if(societyKeyCheck==true){
					boolean checkResult=profile.checkProfileInfo(rid,profile_email);
					/**
					 * update flat detals   CommonMobiDaoService implements CommonMobiDao
					 */
					CommonMobiDao commonServ = new CommonMobiDaoService();
					if (Commonutility.checkempty(flatId) && Commonutility.toCheckisNumeric(flatId) && Commonutility.stringToInteger(flatId)>0) {
						boolean flatUpdate = commonServ.flatDetailsUpdate(Commonutility.stringToInteger(rid),Commonutility.stringToInteger(flatId));
					}					
					if(checkResult==true){
						
					boolean upresult=profile.updateUserData(rid,profile_name,"",profile_proof_id,profile_proof_value);
					if(upresult==true){
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("is_display", 0);
						List<FlatMasterTblVO> flatMastList = mobiCommon.blockDetailData(Commonutility.stringToInteger(rid));
						FlatMasterTblVO flatMastObj;
						JSONArray flatMastArray = new JSONArray();
						if (flatMastList != null) {
							for (Iterator<FlatMasterTblVO> fmt = flatMastList.iterator(); fmt.hasNext();) {
								flatMastObj = fmt.next();
								JSONObject jsonData = new JSONObject();
								jsonData.put("unique_id", Commonutility.intnullChek(flatMastObj.getFlat_id()));
								if (flatMastObj.getWingsname() != null && !flatMastObj.getWingsname().equalsIgnoreCase("") 
										&& !flatMastObj.getWingsname().equalsIgnoreCase("null")) {
									jsonData.put("block_flat", flatMastObj.getWingsname()+"-"+flatMastObj.getFlatno());
								} else {
									jsonData.put("block_flat", flatMastObj.getFlatno());
								}
								jsonData.put("is_myself", Commonutility.intnullChek(flatMastObj.getIvrBnismyself()));
								flatMastArray.put(jsonData);
							}
						}
						System.out.println("ppr_block else check===================44444==================");
						locObjRspdataJson.put("block_detail", flatMastArray);
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
					}
					}else{
						boolean upresult=profile.updateUserData(rid,profile_name,profile_email,profile_proof_id,profile_proof_value);
						if(upresult==true){
							if(profile_email!=null && !profile_email.equalsIgnoreCase("")){
								userMst=profile.getUserPrfileInfo(rid);
								if(userMst!=null){
									mobiCommon mobcom=new mobiCommon();
									String emailLink=System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.email.verify.link");
									String encryptEmail = EncDecrypt.encrypt(profile_email);
									String encryptflg = EncDecrypt.encrypt("1");
									String encryptuserid = EncDecrypt.encrypt(rid);
									String finalEmail =URLEncoder.encode(encryptEmail);
									String finalFlag=URLEncoder.encode(encryptflg);
									String finalUserId =URLEncoder.encode(encryptuserid);
									System.out.println("=====userInfo.getUserId()==="+encryptuserid);
									String finalLink=emailLink+"?"+"EX0iOFM0RiD0="+finalEmail+"&FN0EM0oGL="+finalFlag+"&Umo0MqzFIg="+finalUserId;
									System.out.println("====finalLink===="+finalLink);
									String lvrName = "";
									if (Commonutility.checkempty(userMst.getFirstName())) {
										lvrName = userMst.getFirstName();
									}
									if (Commonutility.checkempty(userMst.getLastName())) {
										lvrName += " " +userMst.getLastName();
									}
									if (lvrName!=null && lvrName.trim().length()<=0) {
										lvrName = "Resident";
									} else {
										lvrName = lvrName.trim();
									}
									
									//Send Email
									// bitly url call
									String locbityurl = GetBitlyLink.toGetBitlyLinkMthd(finalLink, "yes", getText("bitly.accesstocken"), getText("bitly.server.url"));
									System.out.println("===bitly==="+locbityurl);
									if(profile_email!=null){
									EmailEngineServices emailservice = new EmailEngineDaoServices();
									EmailTemplateTblVo emailTemplate;
									
									try {
							            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Email Verification' and status ='1'";
							            emailTemplate = emailservice.emailTemplateData(emqry);
							            String emailMessage = emailTemplate.getTempContent();
							            
							            emailMessage =  emailMessage.replace("[VERIFYEMAILURL]", locbityurl);
							            emailMessage = emailMessage.replace("[FIRSTNAME]", lvrName);
							            emailTemplate.setTempContent(emailMessage);
							            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
							            
							            EmailInsertFuntion emailfun = new EmailInsertFuntion();
							            emailfun.test2(profile_email, emailTemplate.getSubject(), emailMessage);
							          } catch (Exception ex) {
							            System.out.println("Excption found in createLogin.class : " + ex);
							            log.logMessage("Exception in createLogin flow emailFlow----> " + ex, "error",signUpProcess.class);
							            
							          }	
									locObjRspdataJson=new JSONObject();
									}
									locObjRspdataJson=new JSONObject();
									locObjRspdataJson.put("is_display",1);
									List<FlatMasterTblVO> flatMastList = mobiCommon.blockDetailData(Commonutility.stringToInteger(rid));
									FlatMasterTblVO flatMastObj;
									JSONArray flatMastArray = new JSONArray();
									if (flatMastList != null) {
										for (Iterator<FlatMasterTblVO> fmt = flatMastList.iterator(); fmt.hasNext();) {
											flatMastObj = fmt.next();
											JSONObject jsonData = new JSONObject();
											jsonData.put("unique_id", Commonutility.intnullChek(flatMastObj.getFlat_id()));
											if (flatMastObj.getWingsname() != null && !flatMastObj.getWingsname().equalsIgnoreCase("") 
													&& !flatMastObj.getWingsname().equalsIgnoreCase("null")) {
												jsonData.put("block_flat", flatMastObj.getWingsname()+"-"+flatMastObj.getFlatno());
											} else {
												jsonData.put("block_flat", flatMastObj.getFlatno());
											}
											jsonData.put("is_myself", Commonutility.intnullChek(flatMastObj.getIvrBnismyself()));
											flatMastArray.put(jsonData);
										}
									}
									System.out.println("ppr_block else check===================44444==================");
									locObjRspdataJson.put("block_detail", flatMastArray);
									serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
								}else{
									locObjRspdataJson=new JSONObject();
									serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
								}
								
							}
							
						}
					}
					
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+", Request values are not correct format", "info", profileUpdate.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======personalProfileList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, personalProfileList an unhandled error occurred", "info", profileUpdate.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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
			System.out.println("=====as==="+as);
			as=EncDecrypt.encrypt(as);
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


	public List<MvpUsrSkillTbl> getUserSkillList() {
		return userSkillList;
	}


	public void setUserSkillList(List<MvpUsrSkillTbl> userSkillList) {
		this.userSkillList = userSkillList;
	}


	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}
