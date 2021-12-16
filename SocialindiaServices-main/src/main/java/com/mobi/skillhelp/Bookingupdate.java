package com.mobi.skillhelp;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.skillhelp.persistence.SkillhelpDao;
import com.mobi.skillhelp.persistence.SkillhelpDaoServices;
import com.mobi.skillhelpvo.ServiceBookingVO;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.feedbackvo.FeedbackTblVO;
import com.pack.feedbackvo.persistence.FeedbackDao;
import com.pack.feedbackvo.persistence.FeedbackDaoservice;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Bookingupdate extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		//Session locObjsession = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			ivrservicecode = getText("report.issue");
		//	locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			int rid = 0;
			int bookingId =0;
			int status = 0;
			Date startDatetime = null;
			Date endDatetime = null;
			float serviceCost = 0.0f;
			int rating = 0;
			int laborId = 0;
			String feedBack = null;
			log.logMessage("Enter Bookingupdate ", "info", Bookingupdate.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams:" + ivrparams);
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
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						String locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
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
						String locbookingId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"booking_id");
						System.out.println(locbookingId);
						System.out.println(Commonutility.checkempty(locbookingId));
						System.out.println(Commonutility.toCheckisNumeric(locbookingId));
						if (Commonutility.checkempty(locbookingId) && Commonutility.toCheckisNumeric(locbookingId)) {
							bookingId = Commonutility.stringToInteger(locbookingId);
							System.out.println("bookingId:" + bookingId);
							System.out.println("99999");
						} else {
							System.out.println("88888");
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.bookingId.error")));
						}
						String locstatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"status");
						if (Commonutility.checkempty(locstatus) && Commonutility.toCheckisNumeric(locstatus)) {
							status = Commonutility.stringToInteger(locstatus);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.status.error")));
						}
						String locDatetime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"start_datetime");
						if (Commonutility.checkempty(locDatetime)) {
							startDatetime = Commonutility.stringtoTimestamp(locDatetime);
//							startDatetime = Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.startDateTime.error")));
						}
						String locsDatetime = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"end_datetime");
						if (Commonutility.checkempty(locsDatetime)) {
							endDatetime = Commonutility.stringtoTimestamp(locsDatetime);
//							endDatetime = Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.endDateTime.error")));
						}
						String locCost = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"service_cost");
						if (Commonutility.checkempty(locCost)) {
							serviceCost = Commonutility.stringToFloat(locCost);
							if (serviceCost != 0.0f) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.serviceCost.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.serviceCost.error")));
						}
						String locRating = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rating");
						if (Commonutility.checkempty(locRating) && Commonutility.toCheckisNumeric(locRating)) {
							rating = Commonutility.stringToInteger(locRating);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.rating.error")));
						}
						feedBack = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feedback");
						if (Commonutility.checkempty(feedBack)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.feedBack.error")));
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
			log.logMessage("flg :" +flg, "info", Bookingupdate.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				ServiceBookingVO servicebookVO = new ServiceBookingVO();
				servicebookVO.setBookingId(bookingId);
				servicebookVO.setStatus(status);
				servicebookVO.setStartTime(startDatetime);//rating
				servicebookVO.setEndTime(endDatetime);
				servicebookVO.setServiceCost(serviceCost);
				servicebookVO.setModifyDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				SkillhelpDao skillService = new SkillhelpDaoServices();
				flg = skillService.bookigUpdate(servicebookVO);
				if (flg) {
					FeedbackDao feedbackService = new FeedbackDaoservice();
					FeedbackTblVO feedbackObj = new FeedbackTblVO();
					LaborProfileTblVO laborProfile = new LaborProfileTblVO();
					UserMasterTblVo usermaster = new UserMasterTblVo();
					usermaster.setUserId(rid);
					laborProfile.setIvrBnLBR_ID(laborId);				
					feedbackObj.setIvrBnUarmsttbvoobj(usermaster);
					feedbackObj.setIvrBnFEEDBK_TXT(feedBack);
					feedbackObj.setIvrBnFEEDBK_FOR_USR_ID(laborId);
					feedbackObj.setIvrBnFEEDBK_FOR_USR_TYP(7);
					feedbackObj.setIvrBnRATING(rating);
					feedbackObj.setIvrBnFEEDBK_STATUS(1);
					feedbackObj.setIvrBnENTRY_DATETIME(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					int feedbackId = feedbackService.toInsertFeedback(feedbackObj);
					if (feedbackId != -1) {
						ServiceBookingVO servicebookVOFeedBack = new ServiceBookingVO();
						FeedbackTblVO feedBackObj = new FeedbackTblVO();
						feedBackObj.setIvrBnFEEDBK_ID(feedbackId);
						servicebookVOFeedBack.setFeedbackId(feedBackObj);
						servicebookVOFeedBack.setBookingId(bookingId);
						flg = skillService.bookigUpdate(servicebookVOFeedBack);
					} else {
						flg = false;						
					}
				} else {
					flg = false;
				}
				if (flg) {
					serverResponse(ivrservicecode,getText("status.success"),"R0030",getText("R0030"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", Bookingupdate.class);
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

