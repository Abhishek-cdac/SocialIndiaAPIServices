package com.mobi.contactusreportissue;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.contactusreportissuevo.ReportIssueTblVO;
import com.mobi.contactusreportissuevo.persistence.ReportissueDao;
import com.mobi.contactusreportissuevo.persistence.ReportissueDaoservice;
import com.mobile.otpVo.MvpUserOtpTbl;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Reportissue extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		Session locObjsession = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			ivrservicecode = getText("report.issue");//contact.us
			locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townshipid = null;
			String emailid = null;
			String mobileno = null;
			String name = null;
			String issuedesc = null;
			String issueRcontact = null;
			String ppResident = null;
			String responseCode = null;
			String societyKey = null;
			log.logMessage("Enter into report issue", "info", Reportissue.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);				
				if (ivIsJson) {
					System.out.println("ivrparams:" + ivrparams);
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					if (Commonutility.checkempty(ivrservicecode)) {
						if (ivrservicecode.length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
							
						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.error")));
					}
					townshipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					if (Commonutility.checkempty(townshipid)) {
						if (townshipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					if (Commonutility.checknull(societyKey)) {
//						if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {
//							
//						} else {
//							String[] passData = { getText("societykey.fixed.length") };
//							flg = false;
//							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
//						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						emailid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"emailid");
						mobileno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mobileno");
						name = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"name");
						issuedesc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"issuedesc");
						ppResident = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"pp_resident");
						if (Commonutility.checknull(emailid)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("report.emailid.error")));
						}						
						if (Commonutility.checkempty(mobileno)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("report.mobileno.error")));
						}						
						if (Commonutility.checknull(name)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("report.name.error")));
						}																	
						
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				}else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("flg :" +flg, "info", Reportissue.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townshipid,"");
				if(flg){
//					flg = commonServ.checkSocietyKey(societyKey, "");
					if (flg) {
						ReportIssueTblVO reportissueObj = new ReportIssueTblVO();
						ReportissueDao reportIssueInsert = new ReportissueDaoservice();
//						if (Commonutility.checkempty(mobileno)) {
//							UserMasterTblVo userObj = new UserMasterTblVo();
//							userObj = commonServ.getUserInfo(mobileno);
//							if (userObj != null) {
//								System.out.println("Enter into get data");
//								Integer parentFlg = userObj.getParentChildFlag();
//								if (parentFlg != null && parentFlg == 1) {
//									System.out.println("child flg");
//									reportissueObj.setParentId(Commonutility.insertchkintnull(userObj.getParentId()));
//								} else {
//									System.out.println("parent flg");
//								}
//								reportissueObj.setUserId(Commonutility.insertchkintnull(userObj.getUserId()));								
//							} else {
//								flg = false;
//								System.out.println("########### User object nulll ###########");
//							}
//						}
						reportissueObj.setEmailId(Commonutility.insertchkstringnull(emailid));
						reportissueObj.setMobileNo(Commonutility.insertchkstringnull(mobileno));
						reportissueObj.setName(Commonutility.insertchkstringnull(name));
						reportissueObj.setDescr(Commonutility.insertchkstringnull(issuedesc));
						if (Commonutility.checkempty(ppResident)) {
							reportissueObj.setParentId(Commonutility.insertchkintnull(Commonutility.stringToInteger(ppResident)));
						}							
						reportissueObj.setIssueContactus(1);						
						reportissueObj.setStatus(1);
						Date date = Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
						reportissueObj.setEntryDatetime(date);	
						reportissueObj.setModyDatetime(date);
						int reportId = reportIssueInsert.toInsertReportissue(reportissueObj);
						System.out.println("reportId ### :" + reportId);
						if (reportId != -1) {
							if (flg) {
								List<Object[]> useridList = new ArrayList<Object[]>();
								Integer userId = reportIssueInsert.toGetUserId(mobileno);
								System.out.println("userid----------------------"+userId);
								if (userId!=null && userId != 0) {
									flg = reportIssueInsert.updateOTPIssue(userId);
								}
								log.logMessage("report isue size:" + userId, "info", Reportissue.class);								
							}	
							System.out.println("successss");
							serverResponse(ivrservicecode,getText("status.success"),"R0009",getText("R0009"),locObjRspdataJson);
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0006",getText("R0006"),locObjRspdataJson);
						}
					} else {
//						locObjRspdataJson=new JSONObject();
//						serverResponse(ivrservicecode,"01","R0026",getText("R0026"),locObjRspdataJson);	
					}
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode,"01","R0015",getText("R0015"),locObjRspdataJson);	
				}				
			} else {
				log.logMessage("Field error block", "info", Reportissue.class);
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", Reportissue.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",getText("R0003"),locObjRspdataJson);
		} finally {
			
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}
	
	

}
