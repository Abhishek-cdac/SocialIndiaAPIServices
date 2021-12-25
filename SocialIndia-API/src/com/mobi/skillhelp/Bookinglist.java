package com.mobi.skillhelp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobi.skillhelp.persistence.SkillhelpDao;
import com.mobi.skillhelp.persistence.SkillhelpDaoServices;
import com.mobi.skillhelpvo.ServiceBookingVO;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Bookinglist extends ActionSupport{
	
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
			//locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			Date timeStamp = null;
			String receiveTimeStamp = "";
			int startLimit = 0;
			int rid = 0;
			String societyKey = null;
			log.logMessage("Enter Bookinglist", "info", Bookinglist.class);
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
						receiveTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						if (Commonutility.checknull(receiveTimeStamp)) {
							if (Commonutility.checkempty(receiveTimeStamp)) {
								
							} else {
								receiveTimeStamp = Commonutility.timeStampRetStringVal();
							}							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timestamp.error")));
						}
						String locstartLimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
						if (Commonutility.checknull(locstartLimit)) {
							if (Commonutility.checkempty(locstartLimit) && Commonutility.toCheckisNumeric(locstartLimit)) {
								startLimit = Commonutility.stringToInteger(locstartLimit);	
							}
												
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("startlimit.error")));
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
			log.logMessage("flg :" +flg, "info", Bookinglist.class);
			if (flg) {
				
				locObjRspdataJson=new JSONObject();
				ServiceBookingVO servicebookVO = new ServiceBookingVO();
				UserMasterTblVo usermaster = new UserMasterTblVo();
				usermaster.setUserId(rid);
				servicebookVO.setUsrId(usermaster);
				servicebookVO.setEntryDatetime(timeStamp);
				JsonpackDao jsonDataPack = new JsonpackDaoService();
				List<Object[]> BOOKINGDataObj = new ArrayList<Object[]>();
				SkillhelpDao skillService = new SkillhelpDaoServices();
				JSONArray JsonArry = new JSONArray();
//				BOOKINGDataObj = skillService.bookingList(servicebookVO, timeStamp, 0, 20);
				BOOKINGDataObj = skillService.bookingListTest(servicebookVO, receiveTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")));
				System.out.println(BOOKINGDataObj.size());
				String profileimgPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.inner.laborfldr") + getText("external.inner.mobilepath");
				JsonArry = jsonDataPack.bookingListDetil(BOOKINGDataObj,profileimgPath);
				System.out.println("JsonArry:" + JsonArry);
				if (JsonArry != null) {
					locObjRspdataJson.put("booking_det", JsonArry);
				} else {
					locObjRspdataJson.put("booking_det", "");
				}
				String countQuery = "SELECT count(BOOKING_ID) FROM mvp_service_booking_tbl WHERE USR_ID='"+rid+"' and STATUS <> '0'";
				
				System.out.println("0000000");
				locObjRspdataJson.put("timestamp", receiveTimeStamp);
				int totalCnt = skillService.getTotalCountSqlQuery(countQuery);
				locObjRspdataJson.put("totalrecords", Commonutility.intnullChek(totalCnt));
				System.out.println("totalCnt:" + totalCnt);
				if (totalCnt != 0) {
					serverResponse(ivrservicecode,getText("status.success"),"R0001",getText("R0001"),locObjRspdataJson);
				} else {
					locObjRspdataJson = new JSONObject();
					serverResponse(ivrservicecode,getText("status.warning"),"R0025",getText("R0025"),locObjRspdataJson);
				}
				
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", Bookinglist.class);
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
