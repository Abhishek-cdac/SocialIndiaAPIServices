package com.mobi.skillhelp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.merchant.CustomerProductOrder;
import com.mobi.skillhelp.persistence.SkillhelpDao;
import com.mobi.skillhelp.persistence.SkillhelpDaoServices;
import com.mobi.skillhelpvo.ServiceBookingVO;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class NewBooking extends ActionSupport{

	private static final long serialVersionUID =1l;

	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	 private SmsEngineServices smsService = new SmsEngineDaoServices();
	 private SmsTemplatepojo smsTemplate;
	 SmsInpojo sms = new SmsInpojo();
	 CommonUtilsDao common=new CommonUtilsDao();
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		//Session locObjsession = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		try {
			ivrservicecode = getText("report.issue");
			//locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int laborId = 0;
			String problem = null;
			Date preferedDatetime = null;
			Date startDat=null;
			Date endDat=null;
			log.logMessage("Enter into NewBooking ", "info", NewBooking.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
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
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {

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
					if (Commonutility.checkempty(societyKey)) {
						if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {

						} else {
							String[] passData = { getText("societykey.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						String locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						if (Commonutility.checkempty(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
							if (rid !=0 ) {

							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}
						String loclabid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"labor_id");
						if (Commonutility.checkempty(loclabid)) {
							laborId = Commonutility.stringToInteger(loclabid);
							if (laborId !=0 ) {

							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.new.laborId.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.new.laborId.error")));
						}
						problem = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"problem");
						if (Commonutility.checkempty(problem)) {

						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.new.problem.error")));
						}
						String locDatetime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"prefered_datetime");


						if (Commonutility.checkempty(locDatetime) && locDatetime.contains("-")) {
							String[] fromToDate=locDatetime.split("\\s-\\s");
							System.out.println("fromToDate[0].trim()-------------------"+fromToDate[0].trim());
							preferedDatetime = Commonutility.stringtoTimestamp(fromToDate[0].trim());
							System.out.println("preferedDatetime------------"+preferedDatetime);
							startDat=preferedDatetime;
							String prfdat=locDatetime.substring(0,10);
							System.out.println("prfdat-------------------"+prfdat);
							System.out.println("fromToDate[1]------------------"+fromToDate[1]);
							endDat=Commonutility.stringtoTimestamp(prfdat+" "+fromToDate[1].trim());
							System.out.println("endDat--------------"+endDat);
//							preferedDatetime = Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
							if (preferedDatetime != null) {

							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.new.preferedDatetime.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.new.preferedDatetime.error")));
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
			log.logMessage("flg :" +flg, "info", NewBooking.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				ServiceBookingVO servicebookVO = new ServiceBookingVO();
				LaborProfileTblVO laborProfile = new LaborProfileTblVO();
				UserMasterTblVo usermaster = new UserMasterTblVo();
				laborProfile.setIvrBnLBR_ID(laborId);
				usermaster.setUserId(rid);
				servicebookVO.setUsrId(usermaster);
				servicebookVO.setLabourId(laborProfile);
				servicebookVO.setProblem(problem);
				// locCommonObj.getDateobjtoFomatDateStr(forumTopicsData.getEntryDatetime(), "dd-MM-yyyy HH:mm:ss")
				servicebookVO.setPreferedDate(preferedDatetime);
				servicebookVO.setStartTime(startDat);
				servicebookVO.setEndTime(endDat);
				servicebookVO.setEntryBy(rid);
				servicebookVO.setStatus(1);
				servicebookVO.setEntryDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				servicebookVO.setModifyDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				System.out.println(servicebookVO.getUsrId().getUserId());
				System.out.println(":::   "+servicebookVO.getLabourId());
				System.out.println(servicebookVO.getProblem());
				System.out.println(servicebookVO.getPreferedDate());
				SkillhelpDao skillService = new SkillhelpDaoServices();
				int newBookingId = skillService.bookigNewInsert(servicebookVO);
				List<Object[]> loborDataObj = new ArrayList<Object[]>();
				laborProfile = skillService.Laborgetmobno(servicebookVO.getLabourId().getIvrBnLBR_ID());			
				System.out.println("lbrmobno:   "+laborProfile.getIvrBnLBR_PH_NO());
				System.out.println("lbrid:   "+servicebookVO.getLabourId().getIvrBnLBR_ID());
				if (newBookingId != -1) {
					uamDao lvrUamdaosrobj = null;
					lvrUamdaosrobj = new uamDaoServices();
					String lvrSocyname = "", lvrFname="", lvrMob = "";
					UserMasterTblVo userInfo = null;
					userInfo = lvrUamdaosrobj.editUser(rid);
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
					//SMS to service booking
					  try {
						  String mailRandamNumber;
				            mailRandamNumber = common.randInt(5555, 999999999);
				            String qry = "FROM SmsTemplatepojo where " + "templateName ='Services Booking' and status ='1'";
				            smsTemplate = smsService.smsTemplateData(qry);
				            System.out.println("aaa=222= "+smsTemplate.getTemplateContent());
				            String smsMessage = smsTemplate.getTemplateContent();
				            if(Commonutility.checkempty(lvrSocyname)) {
				            	 smsMessage  = smsMessage.replace("[SOCIETY NAME]", lvrSocyname);
				            } else {
				            	 smsMessage  = smsMessage.replace("[SOCIETY NAME]", "SOCYTEA");
				            }				           			           
				            qry = common.smsTemplateParser(smsMessage, 1, "", problem);
				            String[] qrySplit = qry.split("!_!");				           
				            String qryform = qrySplit[0]+"" +" FROM ServiceBookingVO as cust where bookingId ='"+newBookingId+"'";
				            smsMessage = smsService.smsTemplateParserChange(qryform, Integer.parseInt(qrySplit[1]), smsMessage);
				            smsMessage=smsMessage.substring(0, smsMessage.length() - 2);
				            sms.setSmsCardNo(mailRandamNumber);
				            sms.setSmsEntryDateTime(common.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				            sms.setSmsMobNumber(laborProfile.getIvrBnLBR_PH_NO());
				            sms.setSmspollFlag("F");
				            sms.setSmsMessage(smsMessage);
				            smsService.insertSmsInTable(sms);
				          } catch (Exception ex) {
				            System.out.println("Exception sms send servicebooking---->> " + ex);
				            log.logMessage("Exception in servicebooking() smsFlow----> " + ex, "error", CustomerProductOrder.class);

				          }
					  serverResponse(ivrservicecode,getText("status.success"),"R0076",getText("R0076"),locObjRspdataJson);
				} else {
					serverResponse(ivrservicecode,getText("status.warning"),"R0006",getText("R0006"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", NewBooking.class);
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
