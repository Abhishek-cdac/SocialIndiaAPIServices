package com.siservices.societyMgmt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class siSocietyMgmtCreate extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	List<TownshipMstTbl> townShipList = new ArrayList<TownshipMstTbl>();
	TownshipMstTbl townShipMst = new TownshipMstTbl();
	SocietyMstTbl societyMst = new SocietyMstTbl();
	societyMgmtDao society = new societyMgmtDaoServices();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	GroupMasterTblVo groupData = new GroupMasterTblVo();
	MvpSocietyAccTbl societyAccData = new MvpSocietyAccTbl();
	CommonUtilsDao common = new CommonUtilsDao();
	JSONObject jsonFinalObj = new JSONObject();
	SmsInpojo sms = new SmsInpojo();
	private SmsTemplatepojo smsTemplate;
	private SmsEngineServices smsService = new SmsEngineDaoServices();
	boolean result = false;
	Log log = new Log();

	public String execute() {
		
		
		
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json
		String lsvSlQry = null;
		byte conbytetoarr[] = new byte[1024];
		byte conbytetoarr1[] = new byte[1024];
		// Session locObjsession = null;
		int uniqueVal;
		String locSlqry = null;
		Query locQryrst = null;
		GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
		String ivrservicecode = null, accountno = null;
		String bankname = null, accountname = null, ifsccode = null;
		String emailid = null, mobileno = null, isdcode = null, gstin = null, hsn = null;
		String imprintname = null, societycolor = null, logoImageFileName = null, icoImageFileName = null;
		String noofblockswings = null, registerno = null, flatnames = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		Session pSession = null;
		String password = null;
		String activationKey = null;
		try {
			pSession = HibernateUtil.getSession();
			;
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null")
					&& ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson,
									"servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					int townshipId = (int) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "townshipId");
					String societyname = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"societyname");
					int noofmembers = (int) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"noofmembers");
					emailid = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "emailid");
					mobileno = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "mobileno");
					
					gstin = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "GSTIN");
					
					hsn = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "HSN");
					
					isdcode = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "isdcode");
					accountno = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "accountno");
					bankname = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "bankname");
					accountname = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "accountname");
					ifsccode = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "ifsccode");
					flatnames = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "flatnames");
					noofblockswings = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"noofblockswings");
					registerno = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "registerno");
					imprintname = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "imprintname");
					societycolor = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"colorcode");
					// societylogo=(String)
					// Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,
					// "societylogo");
					// societyico=(String)
					// Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,
					// "societyico");
					logoImageFileName = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"logoImageFileName");
					icoImageFileName = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"icoImageFileName");

					locSlqry = "from GroupMasterTblVo where upper(groupName) = upper('"
							+ getText("Grp.society")
							+ "') or groupName = upper('"
							+ getText("Grp.society") + "')";
					locQryrst = pSession.createQuery(locSlqry);
					locGrpMstrVOobj = (GroupMasterTblVo) locQryrst
							.uniqueResult();
					int locGrpcode = 0;
					String userregUniId = null, societyUniId = null;
					if (locGrpMstrVOobj != null) {
						locGRPMstrvo = new GroupMasterTblVo();
						locGrpcode = locGrpMstrVOobj.getGroupCode();
						userregUniId = Commonutility
								.toGetKeyIDforTbl("UserMasterTblVo",
										"max(userId)", Commonutility
												.toCheckNullEmpty(locGrpcode),
										"7");
						societyUniId = Commonutility
								.toGetKeyIDforTbl("SocietyMstTbl",
										"max(societyId)", Commonutility
												.toCheckNullEmpty(locGrpcode),
										"7");
					} else {// new group create
						uamDao uam = new uamDaoServices();
						locGrpcode = uam
								.createnewgroup_rtnId(getText("Grp.society"));
						userregUniId = Commonutility
								.toGetKeyIDforTbl("UserMasterTblVo",
										"max(userId)", Commonutility
												.toCheckNullEmpty(locGrpcode),
										"7");
						societyUniId = Commonutility
								.toGetKeyIDforTbl("SocietyMstTbl",
										"max(societyId)", Commonutility
												.toCheckNullEmpty(locGrpcode),
										"7");
					}
					if (logoImageFileName != null && logoImageFileName != "") {
						societyMst.setLogoImage(logoImageFileName);
					}
					if (icoImageFileName != null && icoImageFileName != "") {
						societyMst.setIcoImage(icoImageFileName);
					}
					societyMst.setNoOfBlocksWings(noofblockswings);
					societyMst.setRegisterNo(registerno);
					societyMst.setImprintName(imprintname);
					societyMst.setColourCode(societycolor);
					townShipMst.setTownshipId(townshipId);
					societyMst.setTownshipId(townShipMst);
					societyMst.setSocietyName(societyname);
					
					societyMst.setGstin(gstin);
					societyMst.setHsn(hsn);
					
					societyMst.setNoOfMemebers(noofmembers);
					CommonUtils comutil = new CommonUtilsServices();
					Date date;
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					activationKey = comutil.getRandomval("AZ_09", 25);
					societyMst.setActivationKey(activationKey);
					societyMst.setSocietyUniqyeId(societyUniId);
					societyMst.setEntryDatetime(date);
					societyMst.setStatusFlag(1);
					societyMst.setEntryby(1);
					societyAccData.setBankAccNo(accountno);
					societyAccData.setBankName(bankname);
					societyAccData.setBankAccName(accountname);
					societyAccData.setIfscCode(ifsccode);
					if (emailid != null && !emailid.equalsIgnoreCase("")) {
						userInfo.setEmailId(emailid);
					} else {
						userInfo.setEmailId(null);
					}
					userInfo.setIsdCode(isdcode);
					userInfo.setMobileNo(mobileno);
					groupData.setGroupCode(locGrpcode);

					password = comutil.getRandomval("AZ_09", 10);
					Date date1;
					date1 = comutil
							.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					userInfo.setGroupCode(groupData);
					userInfo.setPassword(comutil.stringToMD5(password));
					userInfo.setEncryprPassword(EncDecrypt.encrypt(password));
					userInfo.setGroupUniqId(userregUniId);
					userInfo.setUserName(userregUniId);
					userInfo.setFirstName(societyname);
					userInfo.setStatusFlag(1);
					userInfo.setEntryDatetime(date1);
					if (Commonutility.toCheckisNumeric(rb
							.getString("access.channel.both"))) {
						userInfo.setAccessChannel(Commonutility
								.stringToInteger(rb
										.getString("access.channel.both")));
					} else {
						userInfo.setAccessChannel(3);
					}
					
					userInfo.setNumOfLogins(2);
					userInfo.setCurrentLogins(0);
					
					uniqueVal = society.societyInfoSave(societyMst, userInfo,flatnames, societyAccData);
					if (uniqueVal == -2) {
						serverResponse(ivrservicecode, "02", "R0065",mobiCommon.getMsg("R0065"), jsonFinalObj);
					} else if (uniqueVal == -3) {
						serverResponse(ivrservicecode, "01", "R0003", mobiCommon.getMsg("R0003"), jsonFinalObj);
					} else {
						// File to write Mobiledb
						String filepath = rb.getString("external.mobiledbfldr.path");
						String textvalue = "INSERT INTO SOCIETY_MST_TBL (SOCIETY_ID, SOCIETY_UNIQUE_ID, TOWNSHIP_ID, SOCIETY_NAME, NO_OF_BLOCK_WINGS, NO_OF_COMMITTEE_MEMBRS, ACTIVATION_KEY, OLD_ACTIVATION_KEY, REGISTRATION_NO, IMPRINT_NAME, COLOR_CODE, LOGO_NAME, ICO_NAME,ENTRYBY,STATUS,ENTRY_DATETIME,MODIFY_DATETIME,GSTIN, HSN)"
								+ " VALUES ("
								+ societyMst.getSocietyId()
								+ ",'"
								+ societyMst.getSocietyUniqyeId()
								+ "', "
								+ townshipId
								+ ", '"
								+ societyMst.getSocietyName()
								+ "', "
								+ " '"
								+ societyMst.getNoOfBlocksWings()
								+ "', "
								+ societyMst.getNoOfMemebers()
								+ ", '"
								+ societyMst.getActivationKey()
								+ "', "
								+ " '"
								+ societyMst.getOldActivationKey()
								+ "', '"
								+ societyMst.getRegisterNo()
								+ "', '"
								+ societyMst.getImprintName()
								+ "', '"
								+ societyMst.getColourCode()
								+ "', '"
								+ societyMst.getLogoImage()
								+ "', '"
								+ societyMst.getIcoImage()
								+ "',1,1,'"
								+ date1
								+ "','" + date1
								+ "','"
								+ societyMst.getGstin()
								+ "','"		
								+ societyMst.getHsn()
								+ "');";
						Commonutility.TextFileWriting(filepath, textvalue);

						if (userInfo.getEmailId() != null
								&& Commonutility.checkempty(userInfo
										.getEmailId())) {
							EmailEngineServices emailservice = new EmailEngineDaoServices();
							EmailTemplateTblVo emailTemplate;

							try {
								String emqry = "FROM EmailTemplateTblVo where "
										+ "tempName ='Society Email Send' and status ='1'";
								emailTemplate = emailservice
										.emailTemplateData(emqry);
								String emailMessage = emailTemplate
										.getTempContent();
								emqry = common.templateParser(emailMessage, 1,
										"", password);
								String[] qrySplit = emqry.split("!_!");
								String qry = qrySplit[0]
										+ " FROM UserMasterTblVo as cust where cust.mobileNo='"
										+ userInfo.getMobileNo() + "'";
								emailMessage = emailservice
										.templateParserChange(qry,
												Integer.parseInt(qrySplit[1]),
												emailMessage);
								emailMessage = emailMessage.replace(
										"[ACTIVATIONKEY]", activationKey);
								emailTemplate.setTempContent(emailMessage);
								String lvrHeader = emailTemplate.getHeader();
								if (Commonutility.checkempty(lvrHeader)) {
									lvrHeader = lvrHeader.replace(
											"[SOCIETY NAME]", societyname);
								}
								emailMessage = lvrHeader
										+ emailTemplate.getTempContent()
										+ emailTemplate.getFooter();

								EmailInsertFuntion emailfun = new EmailInsertFuntion();
								emailfun.test2(userInfo.getEmailId(),
										emailTemplate.getSubject(),
										emailMessage);
							} catch (Exception ex) {
								Commonutility
										.toWriteConsole("Excption found society creation emailsending siSocietyMgmtCreate.class : "
												+ ex);
								log.logMessage(
										"Step -1 : Exception in society creation flow emailFlow :"
												+ ex, "error",
										siSocietyMgmtCreate.class);

							}
						}
						// Sending SMS
						Commonutility
								.toWriteConsole("Step 2: SMS will Send [START]");
						try {
							String mailRandamNumber;
							mailRandamNumber = common.randInt(5555, 999999999);
							String qry = "FROM SmsTemplatepojo where "
									+ "templateName ='Create Society' and status ='1'";
							smsTemplate = smsService.smsTemplateData(qry);
							String smsMessage = smsTemplate
									.getTemplateContent();
							qry = common.smsTemplateParser(smsMessage, 1, "",
									password);
							String[] qrySplit = qry.split("!_!");
							String qryform = qrySplit[0]
									+ " FROM UserMasterTblVo as cust where cust.mobileNo='"
									+ userInfo.getMobileNo() + "'";
							smsMessage = smsService.smsTemplateParserChange(
									qryform, Integer.parseInt(qrySplit[1]),
									smsMessage);
							sms.setSmsCardNo(mailRandamNumber);
							sms.setSmsEntryDateTime(common
									.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							sms.setSmsMobNumber(userInfo.getMobileNo());
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
						locObjRspdataJson.put("societyid", uniqueVal);

						
						// Create MNTNC BILL EXCEL
						JSONObject chkboxs = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "chkboxs");
						
						filepath = rb.getString("external.mntnc_bill_excel") + "/" + uniqueVal;
						Commonutility.toWriteConsole("siSocietyMgmtCreate.class execute() Method filepath: "+ filepath);
						File file = new File(filepath);
						if(!file.exists()){
							file.mkdirs();
						}
						createExcelTemplate(filepath + "/" + uniqueVal + ".xlsx", chkboxs);

						serverResponse(ivrservicecode, "00", "R0066", mobiCommon.getMsg("R0066"), locObjRspdataJson);
					}

				} else {
					// Response call
					serverResponse(ivrservicecode, "01", "R0067",
							mobiCommon.getMsg("R0067"), locObjRspdataJson);
				}
			} else {
				// Response call
				serverResponse(ivrservicecode, "01", "R0002",
						mobiCommon.getMsg("R0002"), locObjRspdataJson);
			}
		} catch (Exception e) {
			Commonutility
					.toWriteConsole("Step -1 : Exception found siSocietyMgmtCreate.class execute() Method : "
							+ e);
			log.logMessage("Step -1 : Exception siSocietyMgmtCreate.class : "
					+ e, "info", siSocietyMgmtCreate.class);
			try {
				serverResponse(ivrservicecode, "02", "R0003",
						mobiCommon.getMsg("R0003"), locObjRspdataJson);
			} catch (Exception e1) {
			}
		} finally {
			if (pSession != null) {
				pSession.flush();
				pSession.clear();
				pSession.close();
				pSession = null;
			}
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
		}
		return SUCCESS;
	}

	private void createExcelTemplate(String excelFileName, JSONObject chkboxs) throws IOException, JSONException {
		
//		Map<String,Integer> map = new HashMap<String,Integer>();
//		map.put("Monthly_QTLY_Maintenances", 1);
//		map.put("Municipal_Tax", 2);
//		map.put("Water_Charge", 3);
//		map.put("Federation_Charges", 4);
//		map.put("Repair_Fund", 5);
//		map.put("Sinking_Fund", 6);
//		map.put("Major_Repair_Funds", 7);
//		map.put("Non_Occupancy_Charges", 8);
//		map.put("Play_Zone_Fees", 9);
//		map.put("Penalties", 10);
//		map.put("Interest", 11);
//		map.put("Late_Fees", 12);
//		map.put("Insurance_Cost", 13);
//		map.put("Car_Parking_1", 14);
//		map.put("Car_Parking_2", 15);
//		map.put("Two_Wheeler_1", 16);
//		map.put("Two_Wheeler_2", 17);
//		map.put("Sundry_Adjustment", 18);
//		map.put("Property_Tax", 19);
//		map.put("Excess_Payments", 20);
//		map.put("Club_House", 21);
//		map.put("Arrears", 22);
//		map.put("Previous_Dues", 23);
//		map.put("APP_Subscription_Fee", 24);
//		map.put("Total_Payable", 25);
//		map.put("Amount_in_Words", 26);
//		map.put("Bill_No", 27);
//		map.put("Flat_No", 28);
//		map.put("Flat_Area", 29);
//		map.put("Notes", 30);
//		map.put("RSRVD1", 31);
//		map.put("RSRVD2", 32);
//		map.put("RSRVD3", 33);
//		map.put("RSRVD4", 34);
//		map.put("RSRVD5", 35);
//		map.put("RSRVD6", 36);
//		map.put("RSRVD7", 37);
//		map.put("RSRVD8", 38);
//		map.put("RSRVD9", 39);
//		map.put("RSRVD10", 40);

		String sheetName = "bill";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		XSSFRow row = sheet.createRow(0);

		// iterating c number of columns
//		String[] cols = { "Monthly / QTLY maintenances(Numeric) ", "Municipal tax(Numeric)",
//				"Water charge(Numeric)", "Federation charges(Numeric)", "Repair fund(Numeric)",
//				"Sinking fund(Numeric)", "Major repair funds(Numeric)", "Non-Occupancy charges(Numeric)",
//				"Play zone fees(Numeric)", "Penalties(Numeric)", "Interest(Numeric)", "Late fees(Numeric)",
//				"Insurance cost(Numeric)", "Car parking 1(Numeric)", "Car parking 2(Numeric)",
//				"Two wheeler 1(Numeric)", "Two wheeler 2(Numeric)", "Sundry adjustment(Numeric)",
//				"Property tax(Numeric)", "Excess Payments(Numeric)", "Club House(Numeric)", "Arrears(Numeric)",
//				"Previous Dues(Numeric)", "APP Subscription Fee(Numeric)", "Total Payable(Numeric)",
//				"Amount in Words(Text)", "Bill No(Alpha Numeric)", "Flat No(Alpha Numeric)", "Flat Area(Alpha Numeric)", "Notes(Text)",
//				"RSRVD 1(Alpha Numeric)", "RSRVD 2(Alpha Numeric)", "RSRVD 3(Alpha Numeric)", "RSRVD 4(Alpha Numeric)", "RSRVD 5(Alpha Numeric)",
//				"RSRVD 6(Alpha Numeric)", "RSRVD 7(Alpha Numeric)", "RSRVD 8(Alpha Numeric)", "RSRVD 9(Alpha Numeric)", "RSRVD 10(Alpha Numeric)" };
//
//		System.out.println(cols.length);

		int cnt = 0;
		Iterator<String> keys = chkboxs.keys();
		while(keys.hasNext()){
			String key = keys.next();
		    String val = chkboxs.getString(key);
//		    Commonutility.toWriteConsole("Step 1 : Society Create Checkbox : key = "+ key + " val = " +val);
			if(val!=null && val.equalsIgnoreCase("true")){
//				XSSFCell cell = row.createCell(map.get(key));
				XSSFCell cell = row.createCell(cnt);
				cell.setCellValue(key);
				cnt++;
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);

		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
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

	public TownshipMstTbl getTownShipMst() {
		return townShipMst;
	}

	public void setTownShipMst(TownshipMstTbl townShipMst) {
		this.townShipMst = townShipMst;
	}

}
