package com.pack.Gatepassissuelist;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.siSocietyMgmtCreate;
import com.social.common.CommonDaoService;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.GatepassEntryTblVO;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;

public class ConfirmgatepassEntry extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log log=new Log();
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	CommonDaoService locgatepassDaoObj=null;
	SmsInpojo sms = new SmsInpojo();
	private SmsTemplatepojo smsTemplate;
	private SmsEngineServices smsService = new SmsEngineDaoServices();
	public String execute(){

		Commonutility.toWriteConsole("************ConfirmgatepassEntry******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="",is_mobile=null;
		String locDldispQry=null;
		boolean	lvrgatepassUpdaSts=false;
		Integer gatepass_in_out_cnt=0;
		try{
			CommonUtilsDao common=new CommonUtilsDao();
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			/*fileUploadFileName="Chrysanthemum.jpg";
			fileUpload=new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg");*/
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			Commonutility.toWriteConsole("gatepassentryivrparams----------------"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			Common locCommonObj = null;
			if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				locCommonObj=new CommonDao();
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String visitor_pass_no = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "visitor_pass_no");

				  is_mobile=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");//1-mob,0-web
//				JSONObject functionStartTimeObj=commjvm.getdateAndTimeFromDateTime(functionStartTime);
//				String stdate = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "date");
//				String time = (String) Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj, "time");

				 if(is_mobile!=null && !is_mobile.equalsIgnoreCase("0")){
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


				 } else {
					flg = true;
				}
				if(flg){
					boolean result=false;
			if(is_mobile !=null && !is_mobile.equalsIgnoreCase("0"))
			{
				result=otp.checkTownshipKey(rid,townshipKey);
			}
			else
			{
				result =true;
			}
			if(result==true){
			Commonutility.toWriteConsole("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			if(is_mobile !=null && !is_mobile.equalsIgnoreCase("0")){
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			}
			else
			{
				userMst=otp.checkUserDetails(rid);
			}
			if(userMst!=null){
				Commonutility.toWriteConsole("::::1 "+userMst.getSocietyId().getSocietyId());
			int count=0;String locVrSlQry="";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//	Date dt=new Date();

			GatepassEntryTblVO gatepassEntObj=new GatepassEntryTblVO();
			MvpGatepassDetailTbl gatepassDetObj1=new MvpGatepassDetailTbl();
			GatepassDao gatebasehbm=new GatepassDaoServices();
			gatepassDetObj1=mobiCommon.getGatepassid(visitor_pass_no);
			//gate-pass checked in or out count qry
			 gatepass_in_out_cnt =mobiCommon.checkgatepass_in_out(gatepassDetObj1);
			Commonutility.toWriteConsole("gatepass_in_out_cnt== "+gatepass_in_out_cnt);
			Commonutility.toWriteConsole("passid:          "+gatepassDetObj1.getPassId());
			if(gatepass_in_out_cnt>0)
			{
				gatepassEntObj=mobiCommon.getGatepasstatus_in_out(gatepassDetObj1.getPassId());
				Commonutility.toWriteConsole("in or out :  "+gatepassEntObj.getEntryId());

					if(gatepassEntObj.getStatus()==1)//in
				{
					try{
						locgatepassDaoObj = new CommonDaoService();
						locDldispQry= "update GatepassEntryTblVO set status=2 , outDatetime ='"+sdf1.format(common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"))+"' where passId ="+gatepassDetObj1.getPassId();
						lvrgatepassUpdaSts=locgatepassDaoObj.commonUpdate(locDldispQry);

					}catch(Exception e){
					}finally{
						locDldispQry=null;locgatepassDaoObj=null;
					}
					if(lvrgatepassUpdaSts){
						serverResponse(servicecode,"00","R0211",mobiCommon.getMsg("R0211"),locObjRspdataJson,is_mobile);
						}
				}
				else // status out already gatepass used scenerio
				{
					if(gatepassDetObj1.getPassType() == 2)
					{
						//last passid get
						flg =true;
						gatepass_in_out_cnt=0;
					}
					else{
					Commonutility.toWriteConsole("already gatepass used");
					serverResponse(servicecode,"01","R0212",mobiCommon.getMsg("R0212"),locObjRspdataJson,is_mobile);
					}
				}
			}
			if(gatepass_in_out_cnt == 0){
				Commonutility.toWriteConsole("entry insert************************");
				Commonutility.toWriteConsole("passid==="+gatepassDetObj1.getPassId() );
//				if(gatepassDetObj1.getPassType()==1) // temporary visitor only allowed
//				{
				//check expiry or visit to update gatepass detail table
				if(gatepassDetObj1.getPassType()==1){ // temporary visitor only allowed
					if (gatepassDetObj1.getIssueDate()!=null) {
						String chkvisitordate = locCommonObj.getDateobjtoFomatDateStr(gatepassDetObj1.getIssueDate(), "yyyy-MM-dd");
						Date curdate = Commonutility.getCurrentDateTime("yyyy-MM-dd");
						String stringDate = sdf.format(curdate);
				        Date date1 = sdf.parse(chkvisitordate);
				        Date date2 = sdf.parse(stringDate);
				        	if( date1.equals(date2)){
				            	//pending or visit on cur date
				            	Commonutility.toWriteConsole("current date");
				            	locgatepassDaoObj = new CommonDaoService();
								locDldispQry= "update MvpGatepassDetailTbl set visitorstatus=2  where passId ="+gatepassDetObj1.getPassId();
								lvrgatepassUpdaSts=locgatepassDaoObj.commonUpdate(locDldispQry);
				            } else if(date1.after(date2))  {
				            	//before expiry
				            	Commonutility.toWriteConsole("before expiry");
				            	flg =false;
				            	locObjRspdataJson=new JSONObject();
								serverResponse(servicecode,"01","R0213",mobiCommon.getMsg("R0213"),locObjRspdataJson,is_mobile);
				            } else {
				            	//expiry
				            	Commonutility.toWriteConsole("expiry............");
				            	Commonutility.toWriteConsole("before expiry");
				            	flg =false;
				            	locObjRspdataJson=new JSONObject();
								serverResponse(servicecode,"01","R0214",mobiCommon.getMsg("R0214"),locObjRspdataJson,is_mobile);
				            }
					} else {
						//pending or visit on cur date
		            	Commonutility.toWriteConsole("current date");
		            	locgatepassDaoObj = new CommonDaoService();
						locDldispQry= "update MvpGatepassDetailTbl set visitorstatus=2  where passId ="+gatepassDetObj1.getPassId();
						lvrgatepassUpdaSts=locgatepassDaoObj.commonUpdate(locDldispQry);
					}
					
								} else {
									if (gatepassDetObj1.getIssueDate()!=null) {
										//permenant gatepass expiry date
										String chkvisitordate = locCommonObj.getDateobjtoFomatDateStr(gatepassDetObj1.getExpiryDate(), "yyyy-MM-dd");
										Date curdate =Commonutility.getCurrentDateTime("yyyy-MM-dd");
										String stringDate = sdf.format(curdate);
										Date date1 = sdf.parse(chkvisitordate);
										Date date2 = sdf.parse(stringDate);
						            	if( date1.equals(date2) || date1.after(date2) ){
							            	//pending or visit on cur date
							            	Commonutility.toWriteConsole("current date");
							            	locgatepassDaoObj = new CommonDaoService();
											locDldispQry= "update MvpGatepassDetailTbl set visitorstatus=2  where passId ="+gatepassDetObj1.getPassId();
											lvrgatepassUpdaSts=locgatepassDaoObj.commonUpdate(locDldispQry);
						            	}  else{
							            	//expiry
							            	Commonutility.toWriteConsole("expiry............");
							            	Commonutility.toWriteConsole("before expiry");
							            	flg =false;
							            	locObjRspdataJson=new JSONObject();
											serverResponse(servicecode,"01","R0214",mobiCommon.getMsg("R0214"),locObjRspdataJson,is_mobile);
						            	}
									} else {
										//pending or visit on cur date
						            	Commonutility.toWriteConsole("current date");
						            	locgatepassDaoObj = new CommonDaoService();
										locDldispQry= "update MvpGatepassDetailTbl set visitorstatus=2  where passId ="+gatepassDetObj1.getPassId();
										lvrgatepassUpdaSts=locgatepassDaoObj.commonUpdate(locDldispQry);
									}
									
				}
					if(flg){
			        gatepassDetObj1.setPassId(gatepassDetObj1.getPassId());
					gatepassEntObj.setPassId(gatepassDetObj1);
				    gatepassEntObj.setStatus(1);
					UserMasterTblVo userObj =new UserMasterTblVo();
					userObj.setUserId(Integer.parseInt(rid));
					gatepassEntObj.setEntryBy(userObj);
					gatepassEntObj.setInDatetime(common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					gatepassEntObj.setEntryDateTime(common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				int  gpassentryid=gatebasehbm.saveGatepassEntryData(gatepassEntObj);
				Commonutility.toWriteConsole("isupd-------------------"+gpassentryid);
						if(gpassentryid > 0){
							if(gatepassDetObj1.getPassType()==1 ){
							// Sending SMS
				  	          Commonutility.toWriteConsole("Step 2: SMS will Send [START]");
					          try {
					  	            String mailRandamNumber;
					  	            mailRandamNumber = common.randInt(5555, 999999999);
					  	            String qry = "FROM SmsTemplatepojo where "
					  	                + "templateName ='Security Gatepass allowed' and status ='1'";
					  	            smsTemplate = smsService.smsTemplateData(qry);
					  	            String smsMessage = smsTemplate.getTemplateContent();
					  	            qry = common.smsTemplateParser(smsMessage, 1, "", gatepassDetObj1.getGatepassNo());
					  	            String[] qrySplit = qry.split("!_!");
					  	            String qryform = qrySplit[0]
					  	                + " FROM MvpGatepassDetailTbl as cust where cust.mobileNo='"
					  	                + gatepassDetObj1.getMobileNo() + "' and passId ='"+gatepassDetObj1.getPassId()+"'";
					  	            smsMessage = smsService.smsTemplateParserChange(qryform,
					  	                Integer.parseInt(qrySplit[1]), smsMessage);
					  	          Commonutility.toWriteConsole("splval : "+qrySplit[1]);
					  	            sms.setSmsCardNo(mailRandamNumber);
					  	            sms.setSmsEntryDateTime(common
					  	                .getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					  	            sms.setSmsMobNumber(gatepassDetObj1.getEntryBy().getMobileNo());
					  	            sms.setSmspollFlag("F");
					  	            sms.setSmsMessage(smsMessage);
					  	            smsService.insertSmsInTable(sms);
					  	          } catch (Exception ex) {
					  	        	  Commonutility.toWriteConsole("Excption found in society creation smssend siSocietyMgmtCreate.class : " + ex);
					  		            log.logMessage("Exception in society creation flow emailFlow : " + ex, "error",siSocietyMgmtCreate.class);
					  	          }
							}
							String flatno=mobiCommon.getBlockName(Integer.parseInt(rid));
							locObjRspdataJson=new JSONObject();
							/*locObjRspdataJson.put("rid", rid);
							locObjRspdataJson.put("visitor_name", gatepassDetObj1.getVisitorName());
							locObjRspdataJson.put("visitor_mobile_no", gatepassDetObj1.getMobileNo());
							locObjRspdataJson.put("date_of_birth", gatepassDetObj1.getDateOfBirth());
							locObjRspdataJson.put("visitor_email", gatepassDetObj1.getEmail());
							locObjRspdataJson.put("visitor_id_type", String.valueOf(gatepassDetObj1.getMvpIdcardTbl().getiVOcradid()));
							locObjRspdataJson.put("visitor_id_no", gatepassDetObj1.getIdCardNumber());
							locObjRspdataJson.put("visitor_accompanies", String.valueOf(gatepassDetObj1.getNoOfAccompanies()));
							locObjRspdataJson.put("issue_date", gatepassDetObj1.getIssueDate());
							locObjRspdataJson.put("issue_time", gatepassDetObj1.getIssueDate());
							locObjRspdataJson.put("expiry_date", gatepassDetObj1.getExpiryDate());
							locObjRspdataJson.put("skill_id", String.valueOf(gatepassDetObj1.getMvpSkillTbl().getIvrBnSKILL_ID()));
							locObjRspdataJson.put("other_detail", gatepassDetObj1.getDescription());
							locObjRspdataJson.put("pass_type", String.valueOf(gatepassDetObj1.getPassId()));
							locObjRspdataJson.put("vehicle_no", gatepassDetObj1.getVehicleNumber());
							locObjRspdataJson.put("visitor_pass_no", gatepassDetObj1.getGatepassNo());
							locObjRspdataJson.put("visitor_pass_id", String.valueOf(gatepassDetObj1.getPassId()));
							locObjRspdataJson.put("flat_no", flatno);

							String soclogoimg=userMst.getSocietyId().getLogoImage();
							int societyid =userMst.getSocietyId().getSocietyId();
							Commonutility.toWriteConsole("societyid::::::::::  "+societyId);
							if(soclogoimg!=null && soclogoimg.length()>0){
							locObjRspdataJson.put("society_logo", getText("external.uploadfile.society.mobilepath")+societyid+"/"+soclogoimg);
							}
							else{
								locObjRspdataJson.put("society_logo", "");
							}
*/
							serverResponse(servicecode,"00","R0215",mobiCommon.getMsg("R0215"),locObjRspdataJson,is_mobile);
						}
						else{

						}
						}else{
							locObjRspdataJson=new JSONObject();
							serverResponse(servicecode,"01","R0003",getText("R0003"),locObjRspdataJson,is_mobile);
						}
//			}
//				else
//				{
//					locObjRspdataJson=new JSONObject();
//					serverResponse(servicecode,"01","R0029","Only allowed temporary visitor only",locObjRspdataJson,is_mobile);
//				}
			}

			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",getText("R0029"),locObjRspdataJson,is_mobile);
			}

			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",getText("R0015"),locObjRspdataJson,is_mobile);
			}


			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson,is_mobile);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", GatepassPost.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson,is_mobile);
		}
		}catch(Exception ex){
			Commonutility.toWriteConsole("=======Create Event====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", GatepassPost.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson,is_mobile);
		}

		return SUCCESS;
	}


	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla ) {
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();
		try {
			if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
				out = response.getWriter();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				mobiCommon mobicomn=new mobiCommon();
				String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
				out.print(as);
				out.close();
			}
			else{
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
			out.print(as);
			out.close();
			}
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