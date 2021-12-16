package com.social.utititymgmt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.calendar.OutCalendar;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.billmaintenance.CommonBillPack;
import com.social.billmaintenanceDao.BillMaintenanceDao;
import com.social.billmaintenanceDao.BillMaintenanceDaoServices;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.email.EmailInsertFuntion;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.MaintenanceFileUploadTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class MaintenanceExcelDocUploadEngine{
	private String ivrUpload=null;
	private String ivrBackfldr=null;
	private String ivrGrpName=null;
	private String ivrUploadFilePath=null;
	private String cmnfileName=null;
	private int ivrCurnGrpCode=0;
	private int ivrCurnUsrid=0;
	private String ivrExtnce=null;
	private String lvrJrxmlthread=null;
	private int socetyCode=0;
	private CommonUtils common = new CommonUtils();
	private ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	private ResourceBundle rst=ResourceBundle.getBundle("responsestatusmsg");
	private DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
	private String checksum = null;
	
	public MaintenanceExcelDocUploadEngine(String uploadFldrname, String pBkupfladrName, String pUpldFilepath,int pCurntUsrid, String pFileExtnce,int pCurntGrpCode,String lvrJrxml,int societyCode,String filename, String checksum){
		ivrUpload=uploadFldrname;
		ivrBackfldr=pBkupfladrName;
		ivrUploadFilePath=pUpldFilepath;
		ivrCurnUsrid=pCurntUsrid;
		ivrCurnGrpCode=pCurntGrpCode;
		ivrExtnce = pFileExtnce;
		lvrJrxmlthread=lvrJrxml;
		socetyCode=societyCode;
		cmnfileName=filename;
		this.checksum = checksum;
	}
	
	public String run(){
		System.out.println("Step 1 : [MaintenanceExcelDocumentUploadEngine] Thread Started.");
		return toInsertMaintenance(ivrUpload,ivrUploadFilePath,ivrCurnUsrid,1,40,ivrExtnce);	
//		System.out.println("Step 3 : [ResidentExcelEngine] Thread Stoped.");
	}
	
	public String toInsertMaintenance(String uploadFldrname,String locuplfilepath,int ivrEntryByusrid, int startrow, int columnlength, String pFileXtnce){
		Log log=null;
		Session session = null;
		Transaction tx = null;
		String locvrFnrst=null;
		JSONObject jsnobject =null;
		JSONArray jsonArray =null;
		String file_name=null;
		MaintenanceFeeTblVO maintanencefee = new MaintenanceFeeTblVO();
		UserMasterTblVo userdet = new UserMasterTblVo();
		UserMasterTblVo entrydet = new UserMasterTblVo();
		UserMasterTblVo societyde = new UserMasterTblVo();
		SocietyMstTbl societydet = new SocietyMstTbl();
		DocumentManageTblVO documentmng = new DocumentManageTblVO();
		DocumentShareTblVO documentShare = new DocumentShareTblVO();
		DoctypMasterTblVO docmasdet = new DoctypMasterTblVO();
		GroupMasterTblVo groupdet = new GroupMasterTblVo();
		NotificationTblVO notificationdet=new NotificationTblVO();
		MaintenanceFileUploadTblVO maintenancefiles=new MaintenanceFileUploadTblVO();
		BillMaintenanceDao billmainDao=new BillMaintenanceDaoServices();
		
		String lvrErrorMsg = "";
		int filecount=0;
		try{
			log = new Log();
			boolean ifsaved=false,lvrDatastatus = false;
			locvrFnrst = CommonUtils.readExcelMNTNCBill(locuplfilepath, ivrEntryByusrid,startrow,columnlength,pFileXtnce);//resident xlsfilename passing
			file_name=new File(locuplfilepath).getName();
			
			if((locvrFnrst!=null && !locvrFnrst.equalsIgnoreCase("null") && !locvrFnrst.equalsIgnoreCase(""))){
				session = HibernateUtil.getSessionFactory().openSession();
				Date curDates = new Date();
				maintenancefiles.setFileName(file_name);
				maintenancefiles.setChecksum(checksum);
				maintenancefiles.setStatus(0);
				maintenancefiles.setEntryBy(entrydet);
				maintenancefiles.setEntryDatetime(curDates);
				maintenancefiles.setSocietyId(socetyCode);
				int id=docHibernateUtilService.insertMaintenanceFilesTbl(maintenancefiles, session);
				System.out.println("id -------------------------- "+id);
				tx = session.beginTransaction();
				jsnobject = new JSONObject(locvrFnrst);
				jsonArray = jsnobject.getJSONArray("row");
				entrydet.setUserId(ivrEntryByusrid);
				groupdet.setGroupCode(ivrCurnGrpCode);				
				docmasdet.setIvrBnDOC_TYP_ID(8);				
				maintanencefee.setEntryBy(entrydet);								
				
				float xlFlatArea=0,xlusername=0,xlmobileno=0,xlbillDate=0,xlServiceCharge=0,xlMunicipalTax=0,xlPenalties=0,xlWaterCharge=0,
						xlNonOccCharge=0,xlFedeCultural=0,xlSinkingFund=0,xlRepairFund=0,xlInsuranceCharge=0,xlPlayZone=0,xlMajorRepFund=0,XlInterest=0,
						XlLateFees=0,xlCarParking1=0,xlCarParking2=0,xlTwoWheeler1=0,xlTwoWheeler2=0,xlsundryadjustmebt=0,xlPropertyTax=0,
						xlExcessPayment=0,xlClubHouse=0,xlArrears=0,xlPreviousDues=0,xlAppSubscriptionFee=0,xlTotalPayable=0;
				
				String xlRSRVD1=null,xlRSRVD2=null,xlRSRVD3=null,xlRSRVD4=null,xlRSRVD5=null,xlRSRVD6=null,xlRSRVD7=null, xlRSRVD8=null,xlRSRVD9=null,xlRSRVD10=null;
				
				String xlBillNo = null;
				String xlNotes = null;
				String xlAmountInWords=null;
//				String xlFlatArea=null;
				
				List<String> mails = new ArrayList<String>();
				uamDao uam = null;
				UserMasterTblVo usr = null;
				
				float totalAmount=0;
				log.logMessage("MaintenanceExcelDocumentUploadEngine Thread via insert will start.", "info", MaintenanceExcelDocUploadEngine.class);
				for (int i = 0; i < jsonArray.length(); i++) {
					filecount=i+1;	
					JSONObject explrObject = jsonArray.getJSONObject(i);							
//					JSONArray jsonColumnArray = explrObject.getJSONArray("column");	
					JSONObject jsonColumnArray = explrObject.getJSONObject("column");	
					societyMgmtDao society=new societyMgmtDaoServices();
					ifsaved=false;
					try {
						Date curDate = new Date();
						float serviceCharge = 0;
						float mtnyl_qtrly_mtnc=0;
						
//						String xlFlatNo = jsonColumnArray.getString(27);
						String xlFlatNo = jsonColumnArray.getString("Flat_No(Numeric)");
						if(xlFlatNo!=null && !StringUtils.isAlphanumeric(xlFlatNo)){
							return lvrErrorMsg = "'Flat No' should be Numeric.";
						}
						
//						float _xlFlatNo = getFloatValue(jsonColumnArray.getString("Flat_No(Numeric)"));
//						if(_xlFlatNo == -1){
//							return lvrErrorMsg = "'Flat No' should be numeric.";
//						}
//						String xlFlatNo = _xlFlatNo+"";
						
						String _socetyCode = getSocyteaUniqId(socetyCode+"");
						Commonutility.toWriteConsole("----------------------- socetyCode:"+_socetyCode+" xlFlatNo:"+xlFlatNo);
						
						Integer usrId = billmainDao.togetusrvalue(_socetyCode, xlFlatNo);
						Commonutility.toWriteConsole("----------------------- usrId:"+usrId);
						
						uam = new uamDaoServices();
						usr = uam.getregistertable(usrId);
						if(usr!=null){
							String mailid = usr.getEmailId();
							if(mailid!=null && mailid.trim().length() > 0){
								mails.add(mailid.trim());
							}
						}
						uam = null;
						usr = null;
						
						if (usrId > 0) {
							if(jsonColumnArray.has("Monthly_QTLY_Maintenances(Numeric)"))
								mtnyl_qtrly_mtnc = getFloatValue(jsonColumnArray.getString("Monthly_QTLY_Maintenances(Numeric)"));
							
							if(mtnyl_qtrly_mtnc == -1){
								return lvrErrorMsg = "'Monthly / QTLY maintenance' should be numeric.";
							}
							
							if(jsonColumnArray.has("Municipal_Tax(Numeric)"))
								xlMunicipalTax = getFloatValue(jsonColumnArray.getString("Municipal_Tax(Numeric)"));
							
							if(xlMunicipalTax == -1){
								return lvrErrorMsg = "'Municipal tax' should be numeric.";
							}
							
							if(jsonColumnArray.has("Water_Charge(Numeric)"))
								xlWaterCharge = getFloatValue(jsonColumnArray.getString("Water_Charge(Numeric)"));
							
							if(xlWaterCharge == -1){
								return lvrErrorMsg = "'Water charge' should be numeric.";
							}
							
							
							if(jsonColumnArray.has("Federation_Charges(Numeric)"))
								xlFedeCultural = getFloatValue(jsonColumnArray.getString("Federation_Charges(Numeric)"));
							
							if(xlFedeCultural == -1){
								return lvrErrorMsg = "'Federation charges' should be numeric.";
							}
							
							
							if(jsonColumnArray.has("Repair_Fund(Numeric)"))
								xlRepairFund = getFloatValue(jsonColumnArray.getString("Repair_Fund(Numeric)"));
							
							if(xlRepairFund == -1){
								return lvrErrorMsg = "'Repair fund' should be numeric.";
							}
							
							if(jsonColumnArray.has("Sinking_Fund(Numeric)"))
								xlSinkingFund = getFloatValue(jsonColumnArray.getString("Sinking_Fund(Numeric)"));
							
							if(xlSinkingFund == -1){
								return lvrErrorMsg = "'Sinking fund' should be numeric.";
							}
						
							
							if(jsonColumnArray.has("Major_Repair_Funds(Numeric)"))
								xlMajorRepFund = getFloatValue(jsonColumnArray.getString("Major_Repair_Funds(Numeric)"));
							
							if(xlMajorRepFund == -1){
								return lvrErrorMsg = "'Major repair funds' should be numeric.";
							}
							
							if(jsonColumnArray.has("Non_Occupancy_Charges(Numeric)"))
								xlNonOccCharge = getFloatValue(jsonColumnArray.getString("Non_Occupancy_Charges(Numeric)"));
							
							if(xlNonOccCharge == -1){
								return lvrErrorMsg = "'Non-Occupancy charges' should be numeric.";
							}
							
							
							if(jsonColumnArray.has("Play_Zone_Fees(Numeric)"))
								xlPlayZone = getFloatValue(jsonColumnArray.getString("Play_Zone_Fees(Numeric)"));
							
							if(xlPlayZone == -1){
								return lvrErrorMsg = "'Play zone fees' should be numeric.";
							}
							
							if(jsonColumnArray.has("Penalties(Numeric)"))
								xlPenalties = getFloatValue(jsonColumnArray.getString("Penalties(Numeric)"));
							
							if(xlPenalties == -1){
								return lvrErrorMsg = "'Penalties' should be numeric.";
							}
							
							if(jsonColumnArray.has("Interest(Numeric)"))
								XlInterest = getFloatValue(jsonColumnArray.getString("Interest(Numeric)"));
							
							if(XlInterest == -1){
								return lvrErrorMsg = "'Interest' should be numeric.";
							}
							
							if(jsonColumnArray.has("Late_Fees(Numeric)"))
								XlLateFees = getFloatValue(jsonColumnArray.getString("Late_Fees(Numeric)"));
							
							if(XlLateFees == -1){
								return lvrErrorMsg = "'Late fees' should be numeric.";
							}
							
							if(jsonColumnArray.has("Insurance_Cost(Numeric)"))
								xlInsuranceCharge = getFloatValue(jsonColumnArray.getString("Insurance_Cost(Numeric)"));
							
							if(xlInsuranceCharge == -1){
								return lvrErrorMsg = "'Insurance cost' should be numeric.";
							}
							
							if(jsonColumnArray.has("Car_Parking_1(Numeric)"))
								xlCarParking1 = getFloatValue(jsonColumnArray.getString("Car_Parking_1(Numeric)"));
							
							if(xlCarParking1 == -1){
								return lvrErrorMsg = "'Car parking 1' should be numeric.";
							}
							
							if(jsonColumnArray.has("Car_Parking_2(Numeric)"))
								xlCarParking2 = getFloatValue(jsonColumnArray.getString("Car_Parking_2(Numeric)"));
							
							if(xlCarParking2 == -1){
								return lvrErrorMsg = "'Car parking 2' should be numeric.";
							}
							
							if(jsonColumnArray.has("Two_Wheeler_1(Numeric)"))
								xlTwoWheeler1 = getFloatValue(jsonColumnArray.getString("Two_Wheeler_1(Numeric)"));
							
							if(xlTwoWheeler1 == -1){
								return lvrErrorMsg = "'Two wheeler 1' should be numeric.";
							}
							
							if(jsonColumnArray.has("Two_Wheeler_2(Numeric)"))
								xlTwoWheeler2 = getFloatValue(jsonColumnArray.getString("Two_Wheeler_2(Numeric)"));
							
							if(xlTwoWheeler2 == -1){
								return lvrErrorMsg = "'Two wheeler 2' should be numeric.";
							}
							
							if(jsonColumnArray.has("Sundry_Adjustment(Numeric)"))
								xlsundryadjustmebt = getFloatValue(jsonColumnArray.getString("Sundry_Adjustment(Numeric)"));
							
							if(xlsundryadjustmebt == -1){
								return lvrErrorMsg = "'Sundry adjustment' should be numeric.";
							}
							
							if(jsonColumnArray.has("Property_Tax(Numeric)"))
								xlPropertyTax = getFloatValue(jsonColumnArray.getString("Property_Tax(Numeric)"));
							
							if(xlPropertyTax == -1){
								return lvrErrorMsg = "'Property tax' should be numeric.";
							}
							
							if(jsonColumnArray.has("Excess_Payments(Numeric)"))
								xlExcessPayment = getFloatValue(jsonColumnArray.getString("Excess_Payments(Numeric)"));
							
							if(xlExcessPayment == -1){
								return lvrErrorMsg = "'Excess Payments' should be numeric.";
							}
							
							if(jsonColumnArray.has("Club_House(Numeric)"))
								xlClubHouse = getFloatValue(jsonColumnArray.getString("Club_House(Numeric)"));
							
							if(xlClubHouse == -1){
								return lvrErrorMsg = "'Club House' should be numeric.";
							}
							
							if(jsonColumnArray.has("Arrears(Numeric)"))
								xlArrears = getFloatValue(jsonColumnArray.getString("Arrears(Numeric)"));
							
							if(xlArrears == -1){
								return lvrErrorMsg = "'Arrears' should be numeric.";
							}
							
							if(jsonColumnArray.has("Previous_Dues(Numeric)"))
								xlPreviousDues = getFloatValue(jsonColumnArray.getString("Previous_Dues(Numeric)"));
							
							if(xlPreviousDues == -1){
								return lvrErrorMsg = "'Previous Dues' should be numeric.";
							}
							
							if(jsonColumnArray.has("APP_Subscription_Fee(Numeric)"))
								xlAppSubscriptionFee = getFloatValue(jsonColumnArray.getString("APP_Subscription_Fee(Numeric)"));
							
							if(xlAppSubscriptionFee == -1){
								return lvrErrorMsg = "'APP Subscription Fee' should be numeric.";
							}
							
							if(jsonColumnArray.has("Total_Payable(Numeric)"))
								xlTotalPayable = getFloatValue(jsonColumnArray.getString("Total_Payable(Numeric)"));
							
							if(xlTotalPayable == -1){
								return lvrErrorMsg = "'Total Payable' should be numeric.";
							}
							
							if(jsonColumnArray.has("Amount_in_Words(Text)"))
								xlAmountInWords = jsonColumnArray.getString("Amount_in_Words(Text)");
							
							//Commonutility.toWriteConsole("----------------------- xlAmountInWords:"+xlAmountInWords);
							
							if(xlAmountInWords==null){
								return lvrErrorMsg = "'Amount in Words' should be Text.";
							}
							
							if(jsonColumnArray.has("Flat_Area(Numeric)"))
								xlFlatArea = getFloatValue(jsonColumnArray.getString("Flat_Area(Numeric)"));
							
							if(xlFlatArea == -1){
								return lvrErrorMsg = "'Flat Area' should be numeric.";
							}
							
							if(jsonColumnArray.has("Bill_No(Alpha Numeric)"))
								xlBillNo = jsonColumnArray.getString("Bill_No(Alpha Numeric)");
							
							if(xlBillNo!=null && !StringUtils.isAlphanumeric(xlBillNo)){
								return lvrErrorMsg = "'Bill No' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("Notes(Text)"))
								xlNotes = jsonColumnArray.getString("Notes(Text)");
							
//							if(xlNotes!=null && !StringUtils.isAlphaSpace(xlAmountInWords)){
//								return lvrErrorMsg = "'Notes' should be Text.";
//							}
							
							if(jsonColumnArray.has("RSRVD1(Alpha Numeric)"))
								xlRSRVD1 = jsonColumnArray.getString("RSRVD1(Alpha Numeric)");
							
							if(xlRSRVD1!=null && !StringUtils.isAlphanumeric(xlRSRVD1)){
								return lvrErrorMsg = "'RSRVD1' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD2(Alpha Numeric)"))
								xlRSRVD2 = jsonColumnArray.getString("RSRVD2(Alpha Numeric)");
							
							if(xlRSRVD2!=null && !StringUtils.isAlphanumeric(xlRSRVD2)){
								return lvrErrorMsg = "'RSRVD2' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD3(Alpha Numeric)"))
								xlRSRVD3 = jsonColumnArray.getString("RSRVD3(Alpha Numeric)");
							
							if(xlRSRVD3!=null && !StringUtils.isAlphanumeric(xlRSRVD3)){
								return lvrErrorMsg = "'RSRVD3' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD4(Alpha Numeric)"))
								xlRSRVD4 = jsonColumnArray.getString("RSRVD4(Alpha Numeric)");
							
							if(xlRSRVD4!=null && !StringUtils.isAlphanumeric(xlRSRVD4)){
								return lvrErrorMsg = "'RSRVD4' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD5(Alpha Numeric)"))
								xlRSRVD5 = jsonColumnArray.getString("RSRVD5(Alpha Numeric)");
							
							if(xlRSRVD5!=null && !StringUtils.isAlphanumeric(xlRSRVD5)){
								return lvrErrorMsg = "'RSRVD5' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD6(Alpha Numeric)"))
								xlRSRVD6 = jsonColumnArray.getString("RSRVD6(Alpha Numeric)");
							
							if(xlRSRVD6!=null && !StringUtils.isAlphanumeric(xlRSRVD6)){
								return lvrErrorMsg = "'RSRVD6' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD7(Alpha Numeric)"))
								xlRSRVD7 = jsonColumnArray.getString("RSRVD7(Alpha Numeric)");
							
							if(xlRSRVD7!=null && !StringUtils.isAlphanumeric(xlRSRVD7)){
								return lvrErrorMsg = "'RSRVD7' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD8(Alpha Numeric)"))
								xlRSRVD8 = jsonColumnArray.getString("RSRVD8(Alpha Numeric)");
							
							if(xlRSRVD8!=null && !StringUtils.isAlphanumeric(xlRSRVD8)){
								return lvrErrorMsg = "'RSRVD8' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD9(Alpha Numeric)"))
								xlRSRVD9 = jsonColumnArray.getString("RSRVD9(Alpha Numeric)");
							
							if(xlRSRVD9!=null && !StringUtils.isAlphanumeric(xlRSRVD9)){
								return lvrErrorMsg = "'RSRVD9' should be Alpha Numeric.";
							}
							
							if(jsonColumnArray.has("RSRVD10(Alpha Numeric)"))
								xlRSRVD10 = jsonColumnArray.getString("RSRVD10(Alpha Numeric)");
							
							if(xlRSRVD10!=null && !StringUtils.isAlphanumeric(xlRSRVD10)){
								return lvrErrorMsg = "'RSRVD10' should be Alpha Numeric.";
							}
							
							float totbillamt;
							try {
								totbillamt = mtnyl_qtrly_mtnc  + serviceCharge + xlMunicipalTax
										+ xlPenalties + xlWaterCharge
										+ xlNonOccCharge + xlFedeCultural
										+ xlSinkingFund + xlRepairFund
										+ xlInsuranceCharge + xlPlayZone
										+ xlMajorRepFund + XlInterest + XlLateFees + xlCarParking1 + xlCarParking2 + xlTwoWheeler1 + xlTwoWheeler2 + xlsundryadjustmebt + xlPropertyTax + xlExcessPayment + xlClubHouse + xlPreviousDues + xlAppSubscriptionFee ;
							} catch (Exception e) {
								totbillamt = 0;
							}

							Date bilDt = common.stringTODate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
							String filename = common.getUniqStringueDateTime();
							String userpath = Integer.toString(userdet.getUserId());
							System.out.println("userpath---------------"+ userpath);

							userdet.setUserId(usrId);
							maintanencefee.setUserId(userdet);
							maintanencefee.setServiceCharge(serviceCharge);
							maintanencefee.setBillDate(bilDt);
							maintanencefee.setMunicipalTax(xlMunicipalTax);
							maintanencefee.setPenalty(xlPenalties);
							maintanencefee.setWaterCharge(xlWaterCharge);
							maintanencefee.setNonOccupancyCharge(xlNonOccCharge);
							maintanencefee.setCulturalCharge(xlFedeCultural);
							maintanencefee.setSinkingFund(xlSinkingFund);
							maintanencefee.setRepairFund(xlRepairFund);
							maintanencefee.setInsuranceCharges(xlInsuranceCharge);
							maintanencefee.setPlayZoneCharge(xlPlayZone);
							maintanencefee.setMajorRepairFund(xlMajorRepFund);
							maintanencefee.setInterest(XlInterest);
							maintanencefee.setTotbillamt(totbillamt);
							totalAmount += totbillamt;
							
							maintanencefee.setLatefee(XlLateFees);
							maintanencefee.setCarpark1(xlCarParking1);
							maintanencefee.setCarpark2(xlCarParking2);
							maintanencefee.setTwowheeler1(xlTwoWheeler1);
							maintanencefee.setTwowheeler2(xlTwoWheeler2);
							maintanencefee.setSundayadjust(xlsundryadjustmebt);
							maintanencefee.setProtax(xlPropertyTax);
							maintanencefee.setExceespay(xlExcessPayment);
							maintanencefee.setClubhouse(xlClubHouse);
							maintanencefee.setArrears(xlArrears);
							maintanencefee.setPreviousdue(xlPreviousDues);
							maintanencefee.setAppfees(xlAppSubscriptionFee);
							maintanencefee.setAmountword(xlAmountInWords);
							maintanencefee.setBillno(xlBillNo);
							maintanencefee.setFlatno(xlFlatNo);
							maintanencefee.setFlatarea(xlFlatArea+"");
							maintanencefee.setNotes(xlNotes);
//							maintanencefee.setMiscellious(xlm);
							maintanencefee.setEntryDatetime(curDate);
							
							maintanencefee.setPdfstatus("P");
							maintanencefee.setEntryBy(entrydet);
							maintanencefee.setStatusFlag(1);
							maintanencefee.setPaidStatus(0);
							documentmng.setDocFolder(2);

							ifsaved = docHibernateUtilService.insertMaintenanceFeeTbl(maintanencefee,session);
							Commonutility.toWriteConsole("ifsaved ---------maintanencefee-------------- "+ifsaved);

							if (ifsaved) {
								if (i % 20 == 0) {
									session.flush();
									session.clear();
								}
								documentmng.setUserId(userdet);
								documentmng.setUsrTyp(groupdet);
								documentmng.setDocTypId(docmasdet);
								documentmng.setMaintenanceId(maintanencefee);
								documentmng.setDocSubFolder(1);
								documentmng.setDocDateFolderName(common.getDateYYMMDDFormat());
								documentmng.setSubject(rst.getString("maintenance.document.lable"));
								documentmng.setEmailStatus(Integer.parseInt(rst.getString("maintenance.document.emailstatus")));
								documentmng.setEntryBy(entrydet);
								documentmng.setEntryDatetime(curDate);
								documentmng.setStatusFlag(1);
								String lvrMbilname = "Maintencbill_fno"+xlFlatNo.trim()+"_"+Commonutility.getCurrentDate("yyyyMMddmmss");
								documentmng.setDocFileName(lvrMbilname + ".pdf");

								ifsaved = docHibernateUtilService.insertDocumentManageTbl(documentmng,session);
								Commonutility.toWriteConsole("ifsaved ---------documentmng-------------- "+ifsaved);
								if (ifsaved) {
									if (i % 20 == 0) {
										session.flush();
										session.clear();
									}
									documentShare.setDocumentId(documentmng);
									documentShare.setUserId(userdet);
									documentShare.setStatus(1);
									documentShare.setEntryBy(entrydet);
									documentShare.setEntryDatetime(curDate);
									ifsaved = docHibernateUtilService.insertDocumentShareTbl(documentShare, session);
									Commonutility.toWriteConsole("ifsaved ---------documentShare-------------- "+ifsaved);
									if (ifsaved) {
										if (i % 20 == 0) {
											session.flush();
											session.clear();
										}
										notificationdet.setUserId(userdet);
										String randnumber = common.randomString(Integer.parseInt(rb.getString("Notification.random.string.length")));
										notificationdet.setUniqueId(randnumber);
										notificationdet.setReadStatus(Integer.parseInt(rb.getString("Initial.read.Status")));
										notificationdet.setSentStatus(Integer.parseInt(rb.getString("Initial.sent.Status")));
										notificationdet.setStatusFlag(Integer.parseInt(rb.getString("Initial.statusflag")));
										notificationdet.setDescr(rb.getString("notification.document"));
										notificationdet.setEntryBy(entrydet);
										notificationdet.setEntryDatetime(curDate);
										if (Commonutility.checkempty(rb.getString("notification.reflg.document"))) {
											notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.document")));
										} else {
											notificationdet.setTblrefFlag(3);
										}
										notificationdet.setTblrefID(Commonutility.insertchkintnull(documentmng.getDocumentId()));
										ifsaved = docHibernateUtilService.insertNotificationTbl(notificationdet,session);
										Commonutility.toWriteConsole("ifsaved ---------notificationdet-------------- "+ifsaved);
									}
									lvrDatastatus = true;
								} 
							}
						}
						else {
							if (tx != null) {
								tx.rollback();
								tx = null;
							}
							Commonutility.toWriteConsole("Step : Data Vaildation Failed, Flat details not found.");
							lvrErrorMsg = "Data Vaildation Failed, Flat details not found.";
							lvrDatastatus = false;
							break;
						}

					} catch (Exception ex) {
						lvrErrorMsg = ex.getMessage();
						if (tx != null) {
							tx.rollback(); tx = null;							
						}						
						Commonutility.toWriteConsole("Exception Found : " + ex);
						lvrDatastatus = false;
						break;

					} finally {
						
					}
				}
				
//				Common common = null;
//				if(lvrDatastatus && ifsaved){
//					try {
//						common = new CommonDao();
//						String locVrSlQry="update MaintenanceFileUploadTblVO set GRAND_TOTAL='"+totalAmount+"',NO_OF_RECORDS='"+jsonArray.length()+"',status=1 where CHECK_SUM='"+checksum+"'";
//						boolean lvrfunmtr = common.commonUpdate(locVrSlQry);
//						if (lvrfunmtr) {
//							String qry = "delete BillmaintenanceVO where refId ="+ socetyCode;								
//							boolean lvrdelete = billmainDao.toDeleteEvent(qry);
//						}
//					} catch (Exception e) {
//						lvrErrorMsg = "Footer Line Validation Failed";
//						Commonutility.toWriteConsole("BillmaintenanceVO Exception toupdate : "+ e);
//						break;
//					}
//				}
				
				if (id > 0) {
					if (ifsaved) {
						String ssqlQry = "update MaintenanceFileUploadTblVO set noofRecords ="+ filecount+ ",grandTotal="+ totalAmount+ ",status =1 where fileUpId=" + id;
						ifsaved = docHibernateUtilService.updatemaintenancefileTbl(ssqlQry, session);
					} else {
						String ssqlQry = "update MaintenanceFileUploadTblVO set status =0 where fileUpId="+ id;
						ifsaved = docHibernateUtilService.updatemaintenancefileTbl(ssqlQry, session);
					}
				}
				
				// Final data status
				if (ifsaved && lvrDatastatus ) {
					Commonutility.toWriteConsole("Step : All table inserted successfully");
					tx.commit();
					Commonutility.toWriteConsole("Step : PDF Generate will Called");
					CommonBillPack conn = new CommonBillPack();
					conn.pdfGeneration(lvrJrxmlthread);
					
					OutCalendar.sendOutlookMail(mails, "Maintenance Bill", "Maintenance Bill Generated.");
		            
					lvrErrorMsg = "success";
				} else {
					// insert / update status into table
					Commonutility.toWriteConsole("Step : Transaction not commited.");
					Commonutility.toWriteConsole("Error Message : "+lvrErrorMsg);
					Commonutility.toWriteConsole("Data status : "+lvrDatastatus);
					if (tx != null) {
						tx.rollback();
						tx = null;							
					}	
					lvrErrorMsg = "error";
				}
				
				String locupFldrPath=ivrUpload,locuplbackupPath=ivrBackfldr;
				String srcpath=locupFldrPath;
				String distnationpath=locuplbackupPath+ivrEntryByusrid+"/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"/";
				String delrs= Commonutility.toFileMoving(srcpath, distnationpath, file_name, "Remove");
				
			}
			
		}catch (Exception ex){
			lvrErrorMsg = ex.getMessage();
			System.out.println("--------------------Thread Stopped--------------------");
			 System.out.println("Exception Found in MaintenanceExcelDocumentUploadEngine : "+ex);
			 log.logMessage("Exception : "+ex, "error", MaintenanceExcelDocUploadEngine.class);
		}finally{
			log=null;
			if (session != null) {
				session.close();
			}
		}
		
		return lvrErrorMsg;
	}
	
	public static String getSocyteaUniqId(String id){
		String result = "SC";
		int size = 7 - (id.length() + 2);
		for (int i = 0; i < size; i++) {
			result = result + "0";
		}
		return result + id;
	}
	
	public float getFloatValue(String val){
		float result = 0;
		try{
			if(val!=null && !val.isEmpty()){
				val = val.trim();
				result = Float.parseFloat(val);
			}
		}
		catch (Exception e) {
			result = -1;
		}
		
		return result;
	}
	
//	public static void main(String[] args) {
//		String xlAmountInWords = "ten test";
//		if(xlAmountInWords!=null && !StringUtils.isAlpha(xlAmountInWords) || !StringUtils.isAlphaSpace(xlAmountInWords)){
//			System.out.println("test");
//		}
//	}
}
