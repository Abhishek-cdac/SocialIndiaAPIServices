package com.socialindia.generalmgnt;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;

public class staffcreation extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	Log log = new Log();
	JSONObject jsonFinalObj = new JSONObject();
	StaffMasterTblVo staffInfo = new StaffMasterTblVo();
	CompanyMstTblVO companyinfo = new CompanyMstTblVO();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	generalmgmtDao staffmgmt = new generalmgntDaoServices();
	CommonUtilsDao common = new CommonUtilsDao();

	public String execute() {		
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		String ivrservicecode = null, lvrStafimgsrchpth = null;
		byte conbytetoarr[] = new byte[1024];
		CommonUtils locCommutillObj =null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try {
			locCommutillObj = new CommonUtilsDao();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {				
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);				
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"servicecode");
					/*
					 * int staffID =(int) Commonutility
					 * .toHasChkJsonRtnValObj(locObjRecvdataJson, "staffID");
					 */
					String staffname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"userName");
					String staffGender = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffGender");
					String ISDcode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"ISDcode");
					
					String staffCategory = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffCategory");
					String staffWorkhours = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffWorkhours");					
					String Postalcode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "Postalcode");
					String staffEmail = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffEmail");
					String staffMobno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffMobno");
					String staffidcardType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffIdtype");
					String staffidcardNo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffIdno");					
					String staffCountry = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffCountry");					
					// int staffCountry1 =Integer.parseInt(staffCountry);
					String staffState = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "staffState");
					String staffCity = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "staffCity");
					String staffAddr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffAddr");
					int status = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "status");
					String imageweb = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"imageweb");
					String staffImageFileName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffImageFileName");
					String entryid =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"entrybyid");					
					String company_id =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"company_id");
					lvrStafimgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffimgsrchpath");
					staffInfo.setStaffName(staffname);
					staffInfo.setStaffEmail(staffEmail);
					staffInfo.setStaffGender(staffGender);
					staffInfo.setStaffMobno(staffMobno);
					IDCardMasterTblVO idcardObj = new IDCardMasterTblVO();
					 if (!Commonutility.toCheckisNumeric(staffidcardType)) {
						staffInfo.setiVOcradid(null);
					 } else {
						idcardObj.setiVOcradid(Integer.parseInt(staffidcardType));
						staffInfo.setiVOcradid(idcardObj);
					 }
					 staffInfo.setStaffIdcardno(staffidcardNo);
					 if(!Commonutility.toCheckisNumeric(staffCountry)||staffCountry.equalsIgnoreCase("")){
						staffInfo.setStaffCountry(null);
					 } else {
						 staffInfo.setStaffCountry(Integer.parseInt(staffCountry));
					 }	
					
					 if(!Commonutility.toCheckisNumeric(staffState)||staffState.equalsIgnoreCase("")){
						staffInfo.setStaffState(null);
					 } else  {
						 staffInfo.setStaffState(Integer.parseInt(staffState));
					 }	
					 if (!Commonutility.toCheckisNumeric(staffCity)||staffCity.equalsIgnoreCase("")){
						staffInfo.setStaffCity(null);
					 } else {
						 staffInfo.setStaffCity(Integer.parseInt(staffCity));
					 }	
					if(!Commonutility.toCheckisNumeric(company_id)||company_id.equalsIgnoreCase("") ){
						
						companyinfo.setCompanyId(null);
					 }else{
						 companyinfo.setCompanyId(Integer.parseInt(company_id));
						
					 }	
					staffInfo.setCompanyId(companyinfo);
					staffInfo.setStaffAddr(staffAddr);
					staffInfo.setStatus(status);
					UserMasterTblVo userobj = new UserMasterTblVo();
					userobj.setUserId(Integer.parseInt(entryid));
					staffInfo.setEntryby(userobj);
					staffInfo.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					StaffCategoryMasterTblVO categoryinfo = new StaffCategoryMasterTblVO();
					if(!Commonutility.toCheckisNumeric(staffCategory)){
						staffInfo.setiVOstaffcategid(null);
					} else {
						 categoryinfo.setiVOstaffcategid(Integer.parseInt(staffCategory));
						 staffInfo.setiVOstaffcategid(categoryinfo);
					}	
					staffInfo.setISDcode(ISDcode);
					if(!Commonutility.toCheckisNumeric(staffWorkhours)){
						staffInfo.setWorkinghours(null);
					 }else{
						 staffInfo.setWorkinghours(Integer.parseInt(staffWorkhours));
					 }					
					PostalCodeMasterTblVO pstlcodeObj = new PostalCodeMasterTblVO();
					if (!Commonutility.toCheckisNumeric(Postalcode)) {
						staffInfo.setPstlId(null);
					} else {
//						pstlcodeObj.setPstlId(Integer.parseInt(Postalcode));
						staffInfo.setPstlId(Integer.parseInt(Postalcode));
					}
					if(Commonutility.checkempty(staffImageFileName)){
						staffImageFileName = staffImageFileName.replaceAll(" ", "_");
					}
					staffInfo.setImageNameWeb(staffImageFileName);
					staffInfo.setImageNameMobile(staffImageFileName);					
					int result = staffmgmt.staffCreation(staffInfo);
					locObjRspdataJson=new JSONObject();
					if (result != -1 && result != -2) {
						if (staffImageFileName != null && staffImageFileName != "") {
							locObjRspdataJson.put("staffimage",staffImageFileName);
							locObjRspdataJson.put("staffid",result);
						} else {
							locObjRspdataJson.put("staffimage", "");
							locObjRspdataJson.put("staffid",result);
							
						}
						if (staffImageFileName!=null && staffImageFileName!="") {														
							String pWebImagpath = rb.getString("external.uploadfilestaff.webpath");
							String pMobImgpath = rb.getString("external.uploadfilestaff.mobilepath");
							//String delrs = Commonutility.todelete("", pWebImagpath+result+"/");
							//String delrs_mob= Commonutility.todelete("", pMobImgpath+result+"/");					  
							Commonutility.toWriteImageMobWebNewUtill(result, staffImageFileName,lvrStafimgsrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, staffcreation.class);							
						/*
							//web - org image
							conbytetoarr = Commonutility.toDecodeB64StrtoByary(imageweb);
							String topath = rb.getString("external.uploadfilestaff.webpath")+ result+"/";
							String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, staffImageFileName);															
							//mobile - small image
							String limgSourcePath=rb.getString("external.uploadfilestaff.webpath")+result+"/"+staffImageFileName;						
			 		 		String limgDisPath = rb.getString("external.uploadfilestaff.mobilepath")+result+"/";
			 		 	
			 		 	 	String limgName = FilenameUtils.getBaseName(staffImageFileName);
			 		 	 	String limageFomat = FilenameUtils.getExtension(staffImageFileName);		 	    			 	    	 
			 	    	 	String limageFor = "all";
			        		int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
			        		int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));	
			        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
						*/
						}
						
						
						uamDao lvrUamdaosrobj = null;
						lvrUamdaosrobj = new uamDaoServices();
						String lvrSocyname = "", lvrFname="", lvrMob = "";
						if (entryid!=null && Commonutility.checkempty(entryid)) {
							UserMasterTblVo userInfo = null;
							userInfo = lvrUamdaosrobj.editUser(Integer.parseInt(entryid));
							if (userInfo.getFirstName()!=null) {
								lvrFname = userInfo.getFirstName();
							}
							if (userInfo.getLastName()!=null) {
								lvrFname += " "+ userInfo.getLastName();
							}
							lvrMob = userInfo.getMobileNo();
							if (userInfo.getSocietyId()!=null) {
								lvrSocyname = userInfo.getSocietyId().getSocietyName();
							} else {
								lvrSocyname ="SOCYTEA";
							}
						}
						
						//Send Email
						if(staffInfo.getStaffEmail()!=null && Commonutility.checkempty(staffInfo.getStaffEmail())){
							EmailEngineServices emailservice = new EmailEngineDaoServices();
							EmailTemplateTblVo emailTemplate;
							try {
					            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Staff' and status ='1'";
					            emailTemplate = emailservice.emailTemplateData(emqry);
					            String emailMessage = emailTemplate.getTempContent();				            
					            emqry = common.templateParser(emailMessage, 1, "", "");
					            String[] qrySplit = emqry.split("!_!");
					            String qry = qrySplit[0] + " FROM StaffMasterTblVo as staff where staff.staffMobno='"+staffInfo.getStaffMobno()+"'";
					            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
					            emailTemplate.setTempContent(emailMessage);
					            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
					            
					            EmailInsertFuntion emailfun = new EmailInsertFuntion();
					            emailfun.test2(staffInfo.getStaffEmail(), emailTemplate.getSubject(), emailMessage);
							} catch (Exception ex) {
								Commonutility.toWriteConsole("Excption found in staff creation Emailsend staffcreation.class : " + ex);
								log.logMessage("Exception in staff admin flow emailFlow : " + ex, "error",staffcreation.class);
							} finally {
								
							}

						}
						 // Sending SMS				         
						if(staffInfo.getStaffMobno()!=null && Commonutility.checkempty(staffInfo.getStaffMobno())){
							Commonutility.toWriteConsole("===================sms===========================");
							SmsTemplatepojo smsTemplate = null;
							SmsEngineServices smsService = new SmsEngineDaoServices();
							SmsInpojo sms = new SmsInpojo();
							try {
								String mailRandamNumber;
								mailRandamNumber = common.randInt(5555, 999999999);
								String qry = "FROM SmsTemplatepojo where " + "templateName ='Create Staff' and status ='1'";
								smsTemplate = smsService.smsTemplateData(qry);
								String smsMessage = smsTemplate.getTemplateContent();
								smsMessage = smsMessage.replace("[SOCIETY NAME]", lvrSocyname);
								qry = common.smsTemplateParser(smsMessage, 1, "", "");
								String[] qrySplit = qry.split("!_!");
								String qryform = qrySplit[0] + " FROM StaffMasterTblVo as staff where staff.staffMobno='"+staffInfo.getStaffMobno()+"'";
								smsMessage = smsService.smsTemplateParserChange(qryform, Integer.parseInt(qrySplit[1]), smsMessage);
								sms.setSmsCardNo(mailRandamNumber);
								sms.setSmsEntryDateTime(common.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
								sms.setSmsMobNumber(staffInfo.getStaffMobno());
								sms.setSmspollFlag("F");
								sms.setSmsMessage(smsMessage);
								smsService.insertSmsInTable(sms);
				          } catch (Exception ex) {
				        	  Commonutility.toWriteConsole("Excption found in smssend staffcreation.class : " + ex);
					            log.logMessage("Excption found in smssend staffcreation.class : " + ex, "error",staffcreation.class);
				           
				          }	finally {
				        	  
				          }
						} else {
							 Commonutility.toWriteConsole("===================No SMS [Staff Mobile No Not found]===========================");
						}
						
						AuditTrial.toWriteAudit(getText("STFAD001"),"STFAD001", Integer.parseInt(entryid));
						serverResponse(ivrservicecode, "00", "R0107", mobiCommon.getMsg("R0107"),locObjRspdataJson);
					} else if (result == -2){
						serverResponse(ivrservicecode, "02", "R0108", "Staff mobile no. and company name already registered. Exists", locObjRspdataJson);
					} else {
					
						serverResponse(ivrservicecode, "01", "R0108", mobiCommon.getMsg("R0108"), locObjRspdataJson);
					}
					
				} else {
					// Response call
					serverResponse(ivrservicecode, "01", "R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				// Response call
				serverResponse(ivrservicecode, "01", "R0002",mobiCommon.getMsg("R0002"), locObjRspdataJson);
			}

		} catch (Exception e) {try{
			Commonutility.toWriteConsole("Exception found .class execute() Method : " + e);
			log.logMessage("Service code : SI2002, Sorry, an unhandled error occurred","info", null);
			serverResponse(ivrservicecode, "02", "R0003",mobiCommon.getMsg("R0003"), locObjRspdataJson);
		}catch(Exception ee){}finally{}
		} finally {			
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
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
