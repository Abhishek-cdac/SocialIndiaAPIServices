package com.pack.DisputeLaborlist;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDao;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDaoservice;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;
import com.socialindiaservices.vo.MerchantIssuePostingTblVO;

public class SitoLaborSendEmail extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	LaborProfileTblVO laborData = new LaborProfileTblVO();
	DisputemerchantDao tomerchant=new DisputemerchantDaoservice();
	UserMasterTblVo usrtblvo = new UserMasterTblVo();//Resident Userdata
	uamDao uam=new uamDaoServices();
	Log log=new Log();
	JSONObject jsonFinalObj = new JSONObject();

	public String execute() throws Exception {
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		String servicecode=null;
		CommonUtilsDao common=new CommonUtilsDao();	
		boolean result;
		try {
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				locObjRecvJson = new JSONObject(ivrparams);
			
				if (ivIsJson) {
					Commonutility.toWriteConsole("===============json========" + json);
					locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
					Integer uniqadminId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "tolaborid");
					String commentval =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "comments");
					String Disputeid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "disputeid");
					String DisputeUsrid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "disputeusrid");
					Integer groupcode=2;
					laborData = tomerchant.getLabormasterData(uniqadminId,groupcode);				
					usrtblvo = tomerchant.getResidentmasterData(DisputeUsrid,groupcode);
					Commonutility.toWriteConsole("userdate::::::::; "+laborData.getIvrBnLBR_EMAIL()+"   ::::disputeusrid:::: "+usrtblvo.getEmailId());
					if (laborData !=null && usrtblvo!=null){
						
						uamDao lvrUamdaosrobj = null;
						lvrUamdaosrobj = new uamDaoServices();
						String lvrSocyname = "", lvrFname="", lvrMob = "";
						if (DisputeUsrid!=null && Commonutility.checkempty(DisputeUsrid)) {
							UserMasterTblVo userInfo = null;
							userInfo = lvrUamdaosrobj.editUser(Integer.parseInt(DisputeUsrid));
							if (userInfo.getFirstName()!=null) {
								lvrFname = userInfo.getFirstName();
							}
							if (userInfo.getLastName()!=null) {
								lvrFname += " "+ userInfo.getLastName();
							}
							lvrMob = userInfo.getMobileNo();
							if (userInfo.getSocietyId()!=null) {
								lvrSocyname = userInfo.getSocietyId().getSocietyName();
							}
						}
						Common locCommonObj = null;
						locCommonObj=new CommonDao();
						//Send Email
						if(laborData.getIvrBnLBR_EMAIL()!=null){
							EmailEngineServices emailservice = new EmailEngineDaoServices();
							EmailTemplateTblVo emailTemplate;
							Session locObjsession = HibernateUtil.getSession();
							try {
								String emqry = "FROM EmailTemplateTblVo where tempName ='Share Complaint ToLabour' and status ='1'";
								emailTemplate = emailservice.emailTemplateData(emqry);
								String emailMessage = emailTemplate.getTempContent();
								String loc_slQry="from MerchantIssuePostingTblVO  where issueId = "+Disputeid+"";
								MerchantIssuePostingTblVO lvrMrchissu = (MerchantIssuePostingTblVO) locObjsession.createQuery(loc_slQry).uniqueResult();
								String dispute_issuetype = "";
								
								String lvrLaborid = Commonutility.toCheckNullEmpty(laborData.getIvrBnLBR_ID());
								String lvrLbrname = Commonutility.toCheckNullEmpty(laborData.getIvrBnLBR_NAME());
								String lvrcmpldate = locCommonObj.getDateobjtoFomatDateStr(lvrMrchissu.getEntryDatetime(), "yyyy-MM-dd HH:mm:ss");
								
								if (lvrMrchissu.getIssueTypes()!=null) {
									String resport_issuetype=  Commonutility.toCheckNullEmpty(lvrMrchissu.getIssueTypes());
									String splitval[] = resport_issuetype.split(",");
									for (int i = 0; i < splitval.length; i++) {
										 String grpcodeappend =" and ivrGrpcodeobj = 7 ";
										 String locMrchSLqry1="from IssueTblVO where issueId ="+splitval[i]+"";
										 Query locMrchQryrst=locObjsession.createQuery(locMrchSLqry1);
										 IssueTblVO  locMrchIssVOobj= (IssueTblVO) locMrchQryrst.uniqueResult();
										 if (locMrchIssVOobj!=null){
											 if (Commonutility.checkempty(locMrchIssVOobj.getIssueList())) {
												 dispute_issuetype += locMrchIssVOobj.getIssueList()+", ";
											 }
										}
									}
								}
								if (dispute_issuetype!=null && dispute_issuetype.endsWith(", ")) {
									dispute_issuetype = dispute_issuetype.substring(0, dispute_issuetype.length()-2);
								}
								emailMessage =  emailMessage.replace("[LABOURNAME]", lvrLbrname);
								emailMessage =  emailMessage.replace("[COMPLAINTDATE]", lvrcmpldate);								
								emailMessage =  emailMessage.replace("[ISSUETYPES]", dispute_issuetype);
								if (Commonutility.checkempty(lvrFname)) {
					            	emailMessage = emailMessage.replace("[FIRSTNAME]", lvrFname);
					            } else {
					            	emailMessage = emailMessage.replace("[FIRSTNAME]", "Resident");
					            }
					            if (Commonutility.checkempty(lvrMob)) {
					            	emailMessage = emailMessage.replace("[MOBILENO]", lvrMob);
					            } else {
					            	emailMessage = emailMessage.replace("[MOBILENO]", "");
					            }
					            if (Commonutility.checkempty(lvrSocyname)) {
					            	emailMessage = emailMessage.replace("[SOCIETY NAME]", lvrSocyname);
					            } else {
					            	emailMessage = emailMessage.replace("[SOCIETY NAME]", "SOCYTEA");
					            }
								
					            //emailMessage =  emailMessage.replace("[COMMENTS]",commentval);															
								//emqry = common.templateParser(emailMessage, 1, "", "");
								/*
								String[] qrySplit = emqry.split("!_!");
								String qry = qrySplit[0] + " FROM DisputeRiseTbl as dispute where dispute.disputeId="+Disputeid+"";
								emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
								//  emailMessage =  emailMessage.replace("[COMMENTS]",commentval);*/
								emailTemplate.setTempContent(emailMessage);
								emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(laborData.getIvrBnLBR_EMAIL(), emailTemplate.getSubject(), emailMessage);
			            emailfun.test2(usrtblvo.getEmailId(), emailTemplate.getSubject(), emailMessage);
							} catch (Exception ex) {
								Commonutility.toWriteConsole("Excption found in siSocietySendEmail siSocietySendEmail.class : " + ex);
								log.logMessage("Exception in siSocietySendEmail admin flow emailFlow----> " + ex, "error",SitoLaborSendEmail.class);

							} finally {
					        	  if(locObjsession!=null) {
					        		  locObjsession.flush();locObjsession.clear();locObjsession.close(); locObjsession = null;
					        	  }
					          }

						}
						serverResponse(servicecode, "0", "0000", "sucess", jsonFinalObj);
					} else {
					jsonFinalObj=null;
					serverResponse(servicecode, "1", "E0001", "User not fount", jsonFinalObj);
				}
				
				
			} else {
				json = new JSONObject();
				serverResponse(servicecode, "1", "E0001", "Request values not not correct format", json);
			}
			}
		} catch (Exception ex) {
			Commonutility.toWriteConsole("Exception found in SitoLaborSendEmail.class : " + ex);
			json = new JSONObject();
			serverResponse("SI0008", "2", "E0002", "Sorry, an unhandled error occurred", json);
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

	

	public DisputemerchantDao getTomerchant() {
		return tomerchant;
	}

	public void setTomerchant(DisputemerchantDao tomerchant) {
		this.tomerchant = tomerchant;
	}

	public UserMasterTblVo getUsrtblvo() {
		return usrtblvo;
	}

	public void setUsrtblvo(UserMasterTblVo usrtblvo) {
		this.usrtblvo = usrtblvo;
	}

	public LaborProfileTblVO getLaborData() {
		return laborData;
	}

	public void setLaborData(LaborProfileTblVO laborData) {
		this.laborData = laborData;
	}

	
	

}
