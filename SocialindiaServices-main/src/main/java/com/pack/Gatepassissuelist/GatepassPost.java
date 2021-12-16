package com.pack.Gatepassissuelist;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.tender.Tenderutility;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.siSocietyMgmtCreate;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;

public class GatepassPost extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log log = new Log();
	private String ivrparams;
	otpDao otp = new otpDaoService();
	InfoLibraryDao infoLibrary = new InfoLibraryDaoServices();
	FacilityDao facilityhbm = new FacilityDaoServices();
	CommonUtils commjvm = new CommonUtils();
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	SmsInpojo sms = new SmsInpojo();
	private SmsTemplatepojo smsTemplate;
	private SmsEngineServices smsService = new SmsEngineDaoServices();

	public String execute() {

		JSONObject json = new JSONObject();
		Integer otpcount = 0;
		boolean updateResult = false;
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil = null;
		boolean flg = true;
		String servicecode = "", is_mobile = null, lvrGatepassfilename = null, lvrGatepassfiledata = null, visitor_imgname = null;

		try {
			Commonutility.toWriteConsole("Step 1 : Gatepass create started");
			CommonUtilsDao common = new CommonUtilsDao();
			ResourceBundle rb = ResourceBundle
					.getBundle("applicationResources");
			/*
			 * fileUploadFileName="Chrysanthemum.jpg"; fileUpload=new File(
			 * "C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"
			 * );
			 */
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			Common locCommonObj = null;
			if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				locCommonObj = new CommonDao();
				servicecode = (String) Commonutility.toHasChkJsonRtnValObj(
						locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson = (JSONObject) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvJson, "data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(
						locObjRecvdataJson, "rid");
				String visitor_name = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"visitor_name");
				String visitor_mobile_no = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"visitor_mobile_no");
				String date_of_birth = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"date_of_birth");
				String visitor_email = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"visitor_email");
				String visitor_id_type = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"visitor_id_type");
				String visitor_id_no = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"visitor_id_no");
				String visitor_accompanies = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"visitor_accompanies");
				String issue_date = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson, "issue_date");
				String issue_time = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson, "issue_time");
				String expiry_date = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"expiry_date");
				String skill_id = (String) Commonutility.toHasChkJsonRtnValObj(
						locObjRecvdataJson, "skill_id");
				String other_detail = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"other_detail");
				String pass_type = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson, "pass_type");
				String vehicle_no = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson, "vehicle_no");
				String gatepassId = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"gatepass_id");
				String visitor_location = (String) Commonutility
						.toHasChkJsonRtnValObj(locObjRecvdataJson,
								"visitor_location");
				is_mobile = (String) Commonutility.toHasChkJsonRtnValObj(
						locObjRecvJson, "is_mobile");// 1-mob,0-web
				// JSONObject
				// functionStartTimeObj=commjvm.getdateAndTimeFromDateTime(functionStartTime);
				// String stdate = (String)
				// Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj,
				// "date");
				// String time = (String)
				// Commonutility.toHasChkJsonRtnValObj(functionStartTimeObj,
				// "time");
				if (is_mobile != null && is_mobile.equalsIgnoreCase("0")) {
					lvrGatepassfilename = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"gatepassfilename");
					lvrGatepassfiledata = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"gatepassfiledata");
				}
				if (is_mobile != null && !is_mobile.equalsIgnoreCase("0")) {
					if (Commonutility.checkempty(servicecode)) {
						if (servicecode.trim().length() == Commonutility
								.stringToInteger(getText("service.code.fixed.length"))) {

						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility
									.validateErrmsgForm(getText(
											"service.code.length", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil
								.append(Commonutility
										.validateErrmsgForm(getText("Service code cannot be empty")));
					}
					if (Commonutility.checkempty(townshipKey)) {
						if (townshipKey.trim().length() == Commonutility
								.stringToInteger(getText("townshipid.fixed.length"))) {

						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility
									.validateErrmsgForm(getText(
											"townshipid.length", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil
								.append(Commonutility
										.validateErrmsgForm(getText("townshipid.error")));
					}
					if (Commonutility.checkempty(societykey)) {
						if (societykey.trim().length() == Commonutility
								.stringToInteger(getText("society.fixed.length"))) {

						} else {
							String[] passData = { getText("society.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility
									.validateErrmsgForm(getText(
											"societykey.length", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil
								.append(Commonutility
										.validateErrmsgForm(getText("societykey.error")));
					}

					if (locObjRecvdataJson != null) {
						if (Commonutility.checkempty(rid)) {
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility
									.validateErrmsgForm(getText("rid.error")));
						}
					}
					if (pass_type != null && pass_type.equalsIgnoreCase("1")) {
						if (Commonutility.checkempty(visitor_name)) {
						} else {
							flg = false;
							locErrorvalStrBuil
									.append(Commonutility
											.validateErrmsgForm(getText("visitor.name.notempty")));
						}
						if (Commonutility.checkempty(visitor_mobile_no)) {
						} else {
							flg = false;
							locErrorvalStrBuil
									.append(Commonutility
											.validateErrmsgForm(getText("visitor.mobno.notempty")));
						}
						if (Commonutility.checkempty(issue_date)) {
						} else {
							// flg = false;
							// locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("visitor.issuedate.notempty")));
						}
					} else if (pass_type != null
							&& pass_type.equalsIgnoreCase("2")) {
						if (Commonutility.checkempty(visitor_name)) {
						} else {
							flg = false;
							locErrorvalStrBuil
									.append(Commonutility
											.validateErrmsgForm(getText("visitor.name.notempty")));
						}
						if (Commonutility.checkempty(visitor_mobile_no)) {
						} else {
							flg = false;
							locErrorvalStrBuil
									.append(Commonutility
											.validateErrmsgForm(getText("visitor.mobno.notempty")));
						}

						if (Commonutility.checkempty(issue_date)) {
						} else {
							// flg = false;
							// locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("visitor.issuedate.notempty")));
						}

						if (Commonutility.checkempty(expiry_date)) {
						} else {
							// flg = false;
							// locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("visitor.expirydate.notempty")));
						}
						if (Commonutility.checkempty(visitor_id_type)) {
						} else {
							// flg = false;
							// locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("visitor.idcardtype.notempty")));
						}
						if (Commonutility.checkempty(visitor_id_no)) {
						} else {
							// flg = false;
							// locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("visitor.idcardno.notempty")));
						}
						if (Commonutility.checkempty(skill_id)) {
						} else {
							// flg = false;
							// locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("visitor.skill.notempty")));
						}
					}
				} else {
					flg = true;
				}
				if (flg) {
					boolean result = false;
					if (is_mobile != null && !is_mobile.equalsIgnoreCase("0")) {
						result = otp.checkTownshipKey(rid, townshipKey);
					} else {
						result = true;
					}
					if (result == true) {
						System.out.println("********result*****************"
								+ result);
						UserMasterTblVo userMst = new UserMasterTblVo();
						if (is_mobile != null
								&& !is_mobile.equalsIgnoreCase("0")) {
							userMst = otp.checkSocietyKeyForList(societykey,
									rid);
						} else {
							userMst = otp.checkUserDetails(rid);
						}
						if (userMst != null) {
							System.out.println("::::1 "
									+ userMst.getSocietyId().getSocietyId());
							int count = 0;
							String locVrSlQry = "";
							Date dt = new Date();
							int societyId = userMst.getSocietyId()
									.getSocietyId();

							System.out
									.println("insert************************");
							MvpGatepassDetailTbl gatepassObj = new MvpGatepassDetailTbl();

							GatepassDao gatebasehbm = new GatepassDaoServices();
							String gatepassNo = common.getRandomval("AZ_09", 8);
							gatepassObj.setGatepassNo(gatepassNo);
							gatepassObj.setVisitorName(visitor_name);
							gatepassObj.setVisitorLocation(visitor_location);
							gatepassObj.setMobileNo(visitor_mobile_no);
							// lvrEntrydate=locLDTblvoObj.getStartTime();

							DateFormat formatter = new SimpleDateFormat(
									"yyyy-MM-dd");
							if (is_mobile != null
									&& is_mobile.equalsIgnoreCase("1")) {
								String dateStr = date_of_birth;
								System.out.println("--- " + date_of_birth);
								if (dateStr != null
										&& !dateStr.equalsIgnoreCase("")
										&& !dateStr.equalsIgnoreCase("null")) {
									Date Startdate = (Date) formatter
											.parse(dateStr);
									gatepassObj.setDateOfBirth(Startdate);
								} else {
									gatepassObj.setDateOfBirth(null);
								}
							} else {
								System.out.println("web**** ");
								String visitdob = date_of_birth; // its in
																	// MM/dd/yyyy
								if (visitdob != null
										&& !visitdob.equalsIgnoreCase("")
										&& !visitdob.equalsIgnoreCase("null")) {
									System.out.println("web**** " + visitdob);
									Date initDate = new SimpleDateFormat(
											"dd-MM-yyyy").parse(visitdob);
									SimpleDateFormat formatter4 = new SimpleDateFormat(
											"yyyy-MM-dd");
									String parsedDate = formatter4
											.format(initDate);
									System.out.println(parsedDate);
									Date visitor_dob = (Date) formatter
											.parse(parsedDate);
									gatepassObj.setDateOfBirth(visitor_dob);
								} else {
									gatepassObj.setDateOfBirth(null);
								}
							}
							gatepassObj.setEmail(visitor_email);
							IDCardMasterTblVO idcardObj = new IDCardMasterTblVO();
							if (!Commonutility.checkempty(visitor_id_type)
									&& !Commonutility
											.toCheckisNumeric(visitor_id_type)) {
								gatepassObj.setMvpIdcardTbl(null);
							} else {
								idcardObj.setiVOcradid(Integer
										.parseInt(visitor_id_type));
								gatepassObj.setMvpIdcardTbl(idcardObj);
							}
							gatepassObj.setIdCardNumber(visitor_id_no);
							if (!Commonutility.checkempty(visitor_accompanies)
									&& !Commonutility
											.toCheckisNumeric(visitor_accompanies)) {
								gatepassObj.setNoOfAccompanies(null);
							} else {
								gatepassObj.setNoOfAccompanies(Integer
										.parseInt(visitor_accompanies));
							}
							SkillMasterTblVO skillObj = new SkillMasterTblVO();
							if (!Commonutility.checkempty(skill_id)
									&& !Commonutility
											.toCheckisNumeric(skill_id)) {
								gatepassObj.setMvpSkillTbl(null);
							} else {
								skillObj.setIvrBnSKILL_ID(Integer
										.parseInt(skill_id));
								gatepassObj.setMvpSkillTbl(skillObj);
							}
							if (!Commonutility.checkempty(pass_type)
									&& !Commonutility
											.toCheckisNumeric(pass_type)) {
								gatepassObj.setPassType(null);
							} else {
								gatepassObj.setPassType(Integer
										.parseInt(pass_type));
							}

							gatepassObj.setDescription(other_detail);
							gatepassObj.setVehicleNumber(vehicle_no);
							if (is_mobile != null
									&& is_mobile.equalsIgnoreCase("1")) {
								String dateStr1 = issue_date;
								System.out.println("--- " + issue_date);
								if (dateStr1 != null
										&& !dateStr1.equalsIgnoreCase("")
										&& !dateStr1.equalsIgnoreCase("null")) {

									Date issuedate = (Date) formatter
											.parse(dateStr1);
									gatepassObj.setIssueDate(issuedate);
								} else {
									gatepassObj.setIssueDate(null);
								}
							} else {
								String visitdate = issue_date; // its in
																// MM/dd/yyyy
								if (Commonutility.checkempty(visitdate)) {
									System.out.println("web**** " + visitdate);
									Date initDate = new SimpleDateFormat(
											"dd-MM-yyyy").parse(visitdate);
									SimpleDateFormat formatter4 = new SimpleDateFormat(
											"yyyy-MM-dd");
									String parsedDate = formatter4
											.format(initDate);
									System.out.println(parsedDate);
									Date issuedate = (Date) formatter
											.parse(parsedDate);
									gatepassObj.setIssueDate(issuedate);
								} else {
									gatepassObj.setIssueDate(null);
								}

							}

							DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if (is_mobile != null && is_mobile.equalsIgnoreCase("1")) {
								String dateStr3 = issue_time;
								if (dateStr3 != null && !dateStr3.equalsIgnoreCase("") && !dateStr3.equalsIgnoreCase("null")) {

//									formatter1 = new SimpleDateFormat("HH:mm:ss");
									Date issuetime = formatter1.parse(dateStr3);

//									formatter1 = new SimpleDateFormat("HH:mm:ss");
//									String issuetimestr = formatter1.format(issuetime);

//									gatepassObj.setIssueTime(formatter1.parse(issuetimestr));
//									System.out.println("issuetime   "+ issuetimestr);
									gatepassObj.setIssueTime(issuetime);
								} else {
									gatepassObj.setIssueTime(null);
								}
							} else {
								String time = issue_time;
								if (time != null && !time.equalsIgnoreCase("")
										&& !time.equalsIgnoreCase("null")) {

									Date issuetime = formatter1.parse(time);

									formatter1 = new SimpleDateFormat(
											"HH:mm:ss");
									String issuetimestr = formatter1
											.format(issuetime);

									gatepassObj.setIssueTime(formatter1
											.parse(issuetimestr));
									System.out.println("issuetime   "
											+ issuetimestr);
								} else {
									gatepassObj.setIssueTime(null);
								}
							}
							// expiry date
							if (is_mobile != null
									&& is_mobile.equalsIgnoreCase("1")) {
								String dateStr2 = expiry_date;
								if (dateStr2 != null
										&& !dateStr2.equalsIgnoreCase("")
										&& !dateStr2.equalsIgnoreCase("null")) {
									Date expirydate = (Date) formatter
											.parse(dateStr2);
									gatepassObj.setExpiryDate(expirydate);
								} else {
									gatepassObj.setExpiryDate(null);
								}
							} else {
								String expirydate = expiry_date; // its in
																	// MM/dd/yyyy
								if (expirydate != null
										&& !expirydate.equalsIgnoreCase("")
										&& !expirydate.equalsIgnoreCase("null")) {
									System.out.println("expirydate**** "
											+ expirydate);
									Date initDate = new SimpleDateFormat(
											"dd-MM-yyyy").parse(expirydate);
									SimpleDateFormat formatter4 = new SimpleDateFormat(
											"yyyy-MM-dd");
									String parsedDate = formatter4
											.format(initDate);
									System.out.println(parsedDate);
									Date visitor_expirydate = (Date) formatter
											.parse(parsedDate);
									gatepassObj
											.setExpiryDate(visitor_expirydate);

								} else {
									System.out.println("expirydate**else** "
											+ expirydate);
									if (expirydate == null
											|| expirydate.equalsIgnoreCase("")
											|| expirydate
													.equalsIgnoreCase("null")) {
										System.out
												.println("empty of expirydate");
										gatepassObj.setExpiryDate(null);
									} else {
										Date initDate = new SimpleDateFormat(
												"dd-MM-yyyy").parse(expirydate);
										SimpleDateFormat formatter4 = new SimpleDateFormat(
												"yyyy-MM-dd");
										String parsedDate = formatter4
												.format(initDate);
										System.out.println(parsedDate);
										String expdays = getText("visitor.expiry.days");
										Date visitor_expday = (Date) formatter
												.parse(parsedDate);
										visitor_expday = Commonutility.addDays(
												visitor_expday,
												Integer.parseInt(expdays));
										gatepassObj
												.setExpiryDate(visitor_expday);
									}

								}
							}
							gatepassObj.setStatusFlag(1);
							UserMasterTblVo userObj = new UserMasterTblVo();
							if (!Commonutility.toCheckisNumeric(rid)) {
								gatepassObj.setEntryBy(null);
							} else {
								userObj.setUserId(Integer.parseInt(rid));
								gatepassObj.setEntryBy(userObj);
							}
							gatepassObj.setEntryDatetime(common
									.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							gatepassObj.setVisitorstatus(1);
							System.out.println("fileUploadFileName=== "
									+ fileUploadFileName);
							if (fileUploadFileName != null
									&& fileUploadFileName.length() > 0) {
								// mobile
								gatepassObj.setVisitorImage(fileUploadFileName);
							} else {
								if (is_mobile != null
										&& is_mobile.equalsIgnoreCase("0")) {
									System.out.println("imagename :   "
											+ lvrGatepassfilename);
									if (lvrGatepassfilename != null
											&& !lvrGatepassfilename
													.equalsIgnoreCase("null")
											&& !lvrGatepassfilename
													.equalsIgnoreCase("")) {
										gatepassObj
												.setVisitorImage(lvrGatepassfilename);
									} else {
										if (gatepassId != null) {
											visitor_imgname = mobiCommon
													.getVisitorName(Integer
															.parseInt(gatepassId));
											System.out
													.println("imagename1 :   "
															+ visitor_imgname);
											gatepassObj
													.setVisitorImage(visitor_imgname);
										} else {
											gatepassObj.setVisitorImage(null);
										}
									}
								} else {
									System.out.println("2---" + gatepassId);
									if (gatepassId != null
											&& !gatepassId.equalsIgnoreCase("")) {

										visitor_imgname = mobiCommon
												.getVisitorName(Integer
														.parseInt(gatepassId));
										System.out.println("mobimagename1 :   "
												+ visitor_imgname);
										gatepassObj
												.setVisitorImage(visitor_imgname);
									} else {
										gatepassObj.setVisitorImage(null);
									}
								}
							}
							int gatepass_Id = gatebasehbm
									.saveGatepassBookingData(gatepassObj);
							String oldpath = "", oldpathmob = "";
							String doctempPath = "", doctempPathmob = "";
							String oldfile = "";
							if (gatepass_Id > 0) {
								// web image upload
								if (is_mobile != null
										&& is_mobile.equalsIgnoreCase("0")) {

									if ((lvrGatepassfiledata != null
											&& !lvrGatepassfiledata
													.equalsIgnoreCase("null") && !lvrGatepassfiledata
												.equalsIgnoreCase(""))
											&& (lvrGatepassfilename != null
													&& !lvrGatepassfilename
															.equalsIgnoreCase("") && !lvrGatepassfilename
														.equalsIgnoreCase("null"))) {

										String lvrWebpath = getText("external.imagesfldr.path")
												+ getText("external.inner.gatepassfldr")
												+ getText("external.inner.webpath");
										String lvrMobpath = getText("external.imagesfldr.path")
												+ getText("external.inner.gatepassfldr")
												+ getText("external.inner.mobilepath");
										Commonutility
												.toWriteImageMobWebNewUtill(
														gatepass_Id,
														lvrGatepassfilename,
														lvrGatepassfiledata,
														lvrWebpath,
														lvrMobpath,
														rb.getString("thump.img.width"),
														rb.getString("thump.img.height"),
														log,
														Tenderutility.class);

										/*
										 * byte imgbytee[]= new byte[1024];
										 * imgbytee =
										 * Commonutility.toDecodeB64StrtoByary
										 * (lvrGatepassfiledata); String wrtsts
										 * =
										 * Commonutility.toByteArraytoWriteFiles
										 * (imgbytee,getText(
										 * "external.imagesfldr.path"
										 * )+"gatepass/web/"+gatepass_Id+"/",
										 * lvrGatepassfilename); //mobile -
										 * small image String
										 * limgSourcePath=getText
										 * ("external.imagesfldr.path"
										 * )+"gatepass/web/"
										 * +gatepass_Id+"/"+lvrGatepassfilename;
										 * String limgDisPath =
										 * getText("external.imagesfldr.path"
										 * )+"gatepass/mobile/"+gatepass_Id+"/";
										 * String limgName =
										 * FilenameUtils.getBaseName
										 * (lvrGatepassfilename); String
										 * limageFomat =
										 * FilenameUtils.getExtension
										 * (lvrGatepassfilename); String
										 * limageFor = "all"; int lneedWidth =
										 * Commonutility
										 * .stringToInteger(rb.getString
										 * ("thump.img.width")); int lneedHeight
										 * = Commonutility.stringToInteger(rb.
										 * getString("thump.img.height"));
										 * ImageCompress
										 * .toCompressImage(limgSourcePath,
										 * limgDisPath, limgName, limageFomat,
										 * limageFor, lneedWidth,
										 * lneedHeight);// 160, 130 is best for
										 * mobile
										 */
									} else {
										if (visitor_imgname != null
												&& !visitor_imgname
														.equalsIgnoreCase("")
												&& !visitor_imgname
														.equalsIgnoreCase("null")) {
											oldpath = getText("external.imagesfldr.path")
													+ "gatepass/web/"
													+ gatepassId + "";
											doctempPath = getText("external.imagesfldr.path")
													+ "gatepass/web/"
													+ gatepass_Id + "";
											oldpathmob = getText("external.imagesfldr.path")
													+ "gatepass/mobile/"
													+ gatepassId + "";
											doctempPathmob = getText("external.imagesfldr.path")
													+ "gatepass/mobile/"
													+ gatepass_Id + "";
											Commonutility.toFileMoving(oldpath,
													doctempPath,
													visitor_imgname, "NO");
											Commonutility.toFileMoving(
													oldpathmob, doctempPathmob,
													visitor_imgname, "NO");
										} else {

										}
									}
								}

								if (fileUploadFileName != null
										&& !fileUploadFileName
												.equalsIgnoreCase("")) {
									int lneedWidth = 0, lneedHeight = 0;
									String destPath = getText("external.imagesfldr.path")
											+ "gatepass/web/" + gatepass_Id;
									System.out.println("destPath----------"
											+ destPath);
									int imgHeight = mobiCommon
											.getImageHeight(fileUpload);
									int imgwidth = mobiCommon
											.getImageWidth(fileUpload);
									System.out.println("imgHeight------"
											+ imgHeight);
									System.out.println("imgwidth-----------"
											+ imgwidth);
									String imageHeightPro = getText("thump.img.height");
									String imageWidthPro = getText("thump.img.width");
									File destFile = new File(destPath,
											fileUploadFileName);
									FileUtils.copyFile(fileUpload, destFile);
									if (imgHeight > Integer
											.parseInt(imageHeightPro)) {
										lneedHeight = Integer
												.parseInt(imageHeightPro);
										// mobile - small image
									} else {
										lneedHeight = imgHeight;
									}
									if (imgwidth > Integer
											.parseInt(imageWidthPro)) {
										lneedWidth = Integer
												.parseInt(imageWidthPro);
									} else {
										lneedWidth = imgwidth;
									}
									System.out.println("lneedHeight-----------"
											+ lneedHeight);
									System.out
											.println("lneedWidth-------------"
													+ lneedWidth);
									String limgSourcePath = getText("external.imagesfldr.path")
											+ "gatepass/web/"
											+ gatepass_Id
											+ "/" + fileUploadFileName;
									String limgDisPath = getText("external.imagesfldr.path")
											+ "gatepass/mobile/" + gatepass_Id;
									String limgName1 = FilenameUtils
											.getBaseName(fileUploadFileName);
									String limageFomat1 = FilenameUtils
											.getExtension(fileUploadFileName);

									String limageFor = "all";
									ImageCompress.toCompressImage(
											limgSourcePath, limgDisPath,
											limgName1, limageFomat1, limageFor,
											lneedWidth, lneedHeight);// 160, 130
																		// is
																		// best
																		// for
																		// mobile
								} else {
									if (visitor_imgname != null
											&& !visitor_imgname
													.equalsIgnoreCase("")
											&& !visitor_imgname
													.equalsIgnoreCase("null")) {
										oldpath = getText("external.imagesfldr.path")
												+ "gatepass/web/"
												+ gatepassId
												+ "";
										doctempPath = getText("external.imagesfldr.path")
												+ "gatepass/web/"
												+ gatepass_Id
												+ "";
										Commonutility.toFileMoving(oldpath,
												doctempPath, visitor_imgname,
												"NO");
									} else {

									}
								}

								String flatno = mobiCommon.getBlockName(Integer
										.parseInt(rid));
								locObjRspdataJson = new JSONObject();
								locObjRspdataJson.put("rid", rid);
								locObjRspdataJson.put("visitor_name",
										visitor_name);
								locObjRspdataJson.put("visitor_mobile_no",
										visitor_mobile_no);
								locObjRspdataJson.put("date_of_birth",
										date_of_birth);
								locObjRspdataJson.put("visitor_email",
										visitor_email);
								locObjRspdataJson.put("visitor_id_type",
										visitor_id_type);
								locObjRspdataJson.put("visitor_id_no",
										visitor_id_no);
								locObjRspdataJson.put("visitor_accompanies",
										visitor_accompanies);
								locObjRspdataJson.put("issue_date", issue_date);
								locObjRspdataJson.put("issue_time", issue_time);
								locObjRspdataJson.put("expiry_date",
										expiry_date);
								locObjRspdataJson.put("expiry_days",
										getText("visitor.expiry.days"));
								locObjRspdataJson.put("skill_id", skill_id);
								locObjRspdataJson.put("other_detail",
										other_detail);
								locObjRspdataJson.put("pass_type", pass_type);
								locObjRspdataJson.put("vehicle_no", vehicle_no);
								locObjRspdataJson.put("visitor_pass_no",
										gatepassNo);
								locObjRspdataJson.put("visitor_pass_id",
										String.valueOf(gatepass_Id));
								locObjRspdataJson.put("flat_no", flatno);

								String soclogoimg = userMst.getSocietyId()
										.getLogoImage();
								int societyid = userMst.getSocietyId()
										.getSocietyId();
								String lvrSocytname = null, lvrInvitorname = null;
								if (userMst.getSocietyId() != null) {
									lvrSocytname = userMst.getSocietyId()
											.getSocietyName();
								}

								if (Commonutility.checkempty(userMst
										.getFirstName())) {
									lvrInvitorname = userMst.getFirstName();
								}
								if (Commonutility.checkempty(userMst
										.getLastName())) {
									lvrInvitorname += " "
											+ userMst.getLastName();
								}
								if (Commonutility.checkempty(lvrInvitorname)) {
									lvrInvitorname = lvrInvitorname.trim();
								}

								System.out.println("societyid::::::::::  "
										+ societyId);
								if (soclogoimg != null
										&& soclogoimg.length() > 0) {
									locObjRspdataJson
											.put("society_logo",
													getText("external.uploadfile.society.mobilepath")
															+ societyid
															+ "/"
															+ soclogoimg);
								} else {
									locObjRspdataJson.put("society_logo", "");
								}
								// Sending SMS
								Commonutility
										.toWriteConsole("Step 2: SMS will Send [START]");
								try {
									String mailRandamNumber;
									mailRandamNumber = common.randInt(5555,
											999999999);
									String qry = "FROM SmsTemplatepojo where "
											+ "templateName ='Issue Gatepass' and status ='1'";
									smsTemplate = smsService
											.smsTemplateData(qry);
									String smsMessage = smsTemplate
											.getTemplateContent();
									if (Commonutility.checkempty(smsMessage)) {
										smsMessage = smsMessage.replace(
												"[SOCIETY NAME]", lvrSocytname);
										smsMessage = smsMessage.replace(
												"[FIRSTNAME]", lvrInvitorname);
									}
									qry = common.smsTemplateParser(smsMessage,
											1, "", gatepassNo);
									String[] qrySplit = qry.split("!_!");
									String qryform = qrySplit[0]
											+ " FROM MvpGatepassDetailTbl as cust where cust.mobileNo='"
											+ visitor_mobile_no
											+ "' and passId ='" + gatepass_Id
											+ "'";
									smsMessage = smsService
											.smsTemplateParserChange(
													qryform,
													Integer.parseInt(qrySplit[1]),
													smsMessage);
									System.out.println("splval : "
											+ qrySplit[1]);
									sms.setSmsCardNo(mailRandamNumber);
									sms.setSmsEntryDateTime(common
											.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
									sms.setSmsMobNumber(visitor_mobile_no);
									sms.setSmspollFlag("F");
									sms.setSmsMessage(smsMessage);
									smsService.insertSmsInTable(sms);
								} catch (Exception ex) {
									Commonutility
											.toWriteConsole("Excption found in society creation smssend siSocietyMgmtCreate.class : "
													+ ex);
									log.logMessage(
											"Exception in society creation flow emailFlow : "
													+ ex, "error",
											siSocietyMgmtCreate.class);
								}
								serverResponse(servicecode, "00", "R0202",
										mobiCommon.getMsg("R0202"),
										locObjRspdataJson, is_mobile);
							} else {
								locObjRspdataJson = new JSONObject();
								serverResponse(servicecode, "01", "R0025",
										getText("R0025"), locObjRspdataJson,
										is_mobile);
							}

						} else {
							locObjRspdataJson = new JSONObject();
							serverResponse(servicecode, "01", "R0029",
									getText("R0029"), locObjRspdataJson,
									is_mobile);
						}

					} else {
						locObjRspdataJson = new JSONObject();
						serverResponse(servicecode, "01", "R0015",
								getText("R0015"), locObjRspdataJson, is_mobile);
					}

				} else {
					locObjRspdataJson = new JSONObject();
					locObjRspdataJson.put("fielderror", Commonutility
							.removeSPtag(locErrorvalStrBuil.toString()));
					serverResponse(servicecode,
							getText("status.validation.error"), "R0002",
							getText("R0002"), locObjRspdataJson, is_mobile);
				}
			} else {
				locObjRspdataJson = new JSONObject();
				log.logMessage("Service code : " + servicecode
						+ " Request values are not correct format", "info",
						GatepassPost.class);
				serverResponse(servicecode, "01", "R0016", getText("R0016"),
						locObjRspdataJson, is_mobile);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("=======Create Event====Exception====" + ex);
			log.logMessage(
					"Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred",
					"info", GatepassPost.class);
			locObjRspdataJson = new JSONObject();
			serverResponse(servicecode, "01", "R0002", getText("R0002"),
					locObjRspdataJson, is_mobile);
		} finally {
			lvrGatepassfilename = null;
			lvrGatepassfiledata = null;
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson,
			String iswebmobilefla) {
		PrintWriter out = null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response = null;
		response = ServletActionContext.getResponse();
		try {
			if (iswebmobilefla != null && iswebmobilefla.equalsIgnoreCase("1")) {
				out = response.getWriter();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				mobiCommon mobicomn = new mobiCommon();
				String as = mobicomn.getServerResponse(serviceCode, statusCode,
						respCode, message, dataJson);
				out.print(as);
				out.close();
			} else {
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
			try {
				out = response.getWriter();
				out.print("{\"servicecode\":\"" + serviceCode + "\",");
				out.print("{\"statuscode\":\"2\",");
				out.print("{\"respcode\":\"E0002\",");
				out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
				out.print("{\"data\":\"{}\"}");
				out.close();
				ex.printStackTrace();
			} catch (Exception e) {
			} finally {
			}
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getFileUploadContentType() {
		return fileUploadContentType;
	}

	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}

	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

}