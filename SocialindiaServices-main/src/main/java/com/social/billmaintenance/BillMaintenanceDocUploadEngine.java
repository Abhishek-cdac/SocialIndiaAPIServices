package com.social.billmaintenance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.billmaintenanceDao.BillMaintenanceDao;
import com.social.billmaintenanceDao.BillMaintenanceDaoServices;
import com.social.billmaintenanceVO.BillmaintenanceVO;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class BillMaintenanceDocUploadEngine{
	Session prasession = null;
	String checksumval = null;
	int userid = 0;
	String lvSlqry = null;
	JSONArray lvrjsonaryobj = null;
	Iterator lvrObjmaindocbillstitr = null;
	BillmaintenanceVO lvrBilltblvoobj = null;
	JSONObject lvrInrJSONObj = null;
	int societycode = 0;
	int ivrCurnGrpCode = 0;
	String filename = "";
	String lvrJxml = null;
	ResourceBundle rb = ResourceBundle.getBundle("inputfilespecify");
	Log logWrite = null;
	
	BillMaintenanceDocUploadEngine(String checksum,int userId,int societyCode,int pCurntGrpCode,String file_name,String lvrJrxml){
		logWrite = new Log();
		checksumval = checksum;
		userid = userId;
		societycode = societyCode;
		ivrCurnGrpCode = pCurntGrpCode;
		filename = file_name;
		lvrJxml = lvrJrxml;
	}
	public void run() {
		prasession = HibernateUtil.getSession();
		Commonutility.toWriteConsole("Step 1 : [BillMaintenanceDocUploadEngine] Thread Started.");
		logWrite.logMessage("Step 1 : [BillMaintenanceDocUploadEngine] Thread Started.", "info", BillMaintenanceDocUploadEngine.class);
		String rsss = togetMaintenance();
		if (rsss !=null && rsss.equalsIgnoreCase("done")){
			Commonutility.toWriteConsole("Step 3 : [BillMaintenanceDocUploadEngine] Thread Stoped.");
		} 
	}

	public String togetMaintenance() {
		int end = 0;
		int start = 0;				
		ResourceBundle rb = ResourceBundle.getBundle("inputfilespecify");
		ResourceBundle rbapp = ResourceBundle.getBundle("applicationResources");
		ResourceBundle rst=ResourceBundle.getBundle("responsestatusmsg");
		BillMaintenanceDao billmainDao=new BillMaintenanceDaoServices();
		
		//String societyid = "";
		Date billldate = null;
		boolean ifsaved = false;
		Transaction tx = null;
		String lvrErrorMsg = null;
		boolean lvrHeaderstatus = false, lvrFooterstatus = false, lvrDatastatus = false;
		String lvrInfileSocyUnqid = null, lvrTwnshipUnqid = null;
		try {
			lvSlqry = "from BillmaintenanceVO  where status=1";
			lvrObjmaindocbillstitr=prasession.createQuery(lvSlqry).list().iterator();
			tx = prasession.beginTransaction();
			while (lvrObjmaindocbillstitr.hasNext()) {
				lvrInrJSONObj = new JSONObject();
				lvrBilltblvoobj = (BillmaintenanceVO) lvrObjmaindocbillstitr.next();
				String data = lvrBilltblvoobj.getData();
				Commonutility.toWriteConsole("Step 2 : File Data Extract [Start] Data Length : "+ data.length());
				if (Commonutility.checkempty(data) && (data.length() == Integer.parseInt(rb.getString("input.header.data")))) {
					Commonutility.toWriteConsole("Step 3 : Header Line Read : "+data);
					String lvrStartchar = rb.getString("starting_char");
					if(Commonutility.checkempty(lvrStartchar) && data.startsWith(lvrStartchar)) {
						Commonutility.toWriteConsole("Step 4 : Start with validation success.");
						try {							
							String date = rb.getString("Date");
							String[] billdateval = date.split("-");
							start = Integer.parseInt(billdateval[0]);
							end = Integer.parseInt(billdateval[1]);			

							String billdate = data.substring(start, end);
							String day = billdate.substring(0, 2);
							String mon = billdate.substring(2, 4);
							String year = billdate.substring(4, 8);
							String billdates = year + "-" + mon + "-" + day;
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								billldate = sdf.parse(billdates);
								Commonutility.toWriteConsole("Step 4 : Billdate : " + billldate);
							} catch (Exception ex) {
								Commonutility.toWriteConsole("Step 4 : Exception found in date conversion : " + ex);
							}
							end = 0;
							start = 0;

							String townId = rb.getString("Township_ID");
							String[] townsplit = townId.split("-");
							// for(int i=0; i<townsplit.length;i++){
							start = Integer.parseInt(townsplit[0]);
							end = Integer.parseInt(townsplit[1]);
							// }
							String township = data.substring(start, end);
							end = 0;
							start = 0;

							String socId = rb.getString("Society_ID");
							String[] socsplit = socId.split("-");
							start = Integer.parseInt(socsplit[0]);
							end = Integer.parseInt(socsplit[1]); 
							String society = data.substring(start, end);
							end = 0;
							start = 0;	
							
							lvrTwnshipUnqid =  township.trim();
							lvrInfileSocyUnqid =  society.trim();
							Commonutility.toWriteConsole("Township : "+lvrTwnshipUnqid);
							Commonutility.toWriteConsole("Society : "+lvrInfileSocyUnqid);
							boolean result = billmainDao.checksocietytown(lvrTwnshipUnqid,lvrInfileSocyUnqid);
							lvrHeaderstatus = result;
							if (!lvrHeaderstatus){
								lvrErrorMsg = "Header Line Validation Failed, Township and Society Id not found.";
								break;
							}
						} catch (Exception ex) {
							lvrHeaderstatus = false;
							lvrErrorMsg = "Header Line Validation Failed";
							Commonutility.toWriteConsole("Step -1 : Header Line Found Exception : "+ ex);
							break;
						}
					} else {
						lvrHeaderstatus = false;
						lvrErrorMsg = "Header Line Validation Failed, File does not start with "+lvrStartchar;
						Commonutility.toWriteConsole("Step 4 : Start with validation Falied. Mag : "+ lvrErrorMsg);
						break;
					}
					
				
				} else if(lvrHeaderstatus && Commonutility.checkempty(data) &&  data.length()==Integer.parseInt(rb.getString("input.bodycontent.data"))) {
					Commonutility.toWriteConsole("tobodyDatapackage-------------------------");
					DocumentUtilitiServices docHibernateUtilService = null;
					DocumentManageTblVO documentmng = null;
					DocumentShareTblVO documentShare = null;
					DoctypMasterTblVO docmasdet = null;
					GroupMasterTblVo groupdet = null;
					NotificationTblVO notificationdet = null;
					try {
						notificationdet = new NotificationTblVO();
						docHibernateUtilService = new DocumentUtilitiDaoServices();
						documentmng = new DocumentManageTblVO();
						documentShare = new DocumentShareTblVO();
						docmasdet = new DoctypMasterTblVO();
						groupdet = new GroupMasterTblVo();
						groupdet.setGroupCode(ivrCurnGrpCode);
						docmasdet.setIvrBnDOC_TYP_ID(8);
						// Data - 1
						String qalmainId = rb.getString("Monthly_QTLY_maintenance");
						String[] qalmainsplt = qalmainId.split("-");					
						start = Integer.parseInt(qalmainsplt[0]);
						end = Integer.parseInt(qalmainsplt[1]);					
						String serviceCharge = data.substring(start, end);
						end = 0;
						start = 0;
						// Data - 2
						String muntaxId = rb.getString("Municipal_tax");
						String[] muntaxsplt = muntaxId.split("-");					
						start = Integer.parseInt(muntaxsplt[0]);
						end = Integer.parseInt(muntaxsplt[1]);					
						String municipalTax = data.substring(start, end);
						end = 0;
						start = 0;
						// Data - 3
						String watrchrgId = rb.getString("Water_charge");
						String[] watrchrgsplt = watrchrgId.split("-");					
						start = Integer.parseInt(watrchrgsplt[0]);
						end = Integer.parseInt(watrchrgsplt[1]);
						String waterCharge = data.substring(start, end);
						end = 0;
						start = 0;
						// Data - 4
						String fedchrgId = rb.getString("Federation_charges");
						String[] fedchrgIdsplt = fedchrgId.split("-");					
						start = Integer.parseInt(fedchrgIdsplt[0]);
						end = Integer.parseInt(fedchrgIdsplt[1]);					
						String culturalCharge = data.substring(start, end);
						end = 0;
						start = 0;

						String reprfundId = rb.getString("Repair_fund");
						String[] reprfundIdsplt = reprfundId.split("-");					
						start = Integer.parseInt(reprfundIdsplt[0]);
						end = Integer.parseInt(reprfundIdsplt[1]);					
						String repairFund = data.substring(start, end);
						end = 0;
						start = 0;

						String sknfundId = rb.getString("Sinking_fund");
						String[] sknfundIdsplt = sknfundId.split("-");					
						start = Integer.parseInt(sknfundIdsplt[0]);
						end = Integer.parseInt(sknfundIdsplt[1]);
						String sinkingFund = data.substring(start, end);
						end = 0;
						start = 0;

						String mjrrprfunId = rb.getString("Major_repair_funds");
						String[] mjrrprfunIdsplt = mjrrprfunId.split("-");					
						start = Integer.parseInt(mjrrprfunIdsplt[0]);
						end = Integer.parseInt(mjrrprfunIdsplt[1]);					
						String majorRepairFund = data.substring(start, end);
						end = 0;
						start = 0;

						String nonocuchrgId = rb.getString("Non_occupancy_charges");
						String[] nonocuchrgIdsplt = nonocuchrgId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(nonocuchrgIdsplt[0]);
						end = Integer.parseInt(nonocuchrgIdsplt[1]);
						// }
						String nonOccupancyCharge = data.substring(start, end);
						end = 0;
						start = 0;
	
						String plyzonfesId = rb.getString("Play_zone_fees");
						String[] plyzonfesIdsplt = plyzonfesId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(plyzonfesIdsplt[0]);
						end = Integer.parseInt(plyzonfesIdsplt[1]);
						// }
						String plyzonfes = data.substring(start, end);
						end = 0;
						start = 0;
	
						String penaltiesId = rb.getString("Penalties");
						String[] penaltiesIdsplt = penaltiesId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(penaltiesIdsplt[0]);
						end = Integer.parseInt(penaltiesIdsplt[1]);
						// }
						String penalty = data.substring(start, end);
						end = 0;
						start = 0;
	
						String interestId = rb.getString("Interest");
						String[] interestIdsplt = interestId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(interestIdsplt[0]);
						end = Integer.parseInt(interestIdsplt[1]);
						// }
						String interest = data.substring(start, end);
						end = 0;
						start = 0;
	
						String latefeesId = rb.getString("Late_fees");
						String[] latefeesIdsplt = latefeesId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(latefeesIdsplt[0]);
						end = Integer.parseInt(latefeesIdsplt[1]);
						// }
						String latefees = data.substring(start, end);
						end = 0;
						start = 0;
						String carpar1 = rb.getString("Car_parking_1");
						String[] carpark = carpar1.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(carpark[0]);
						end = Integer.parseInt(carpark[1]);
						// }
						String carpark1 = data.substring(start, end);
						end = 0;
						start = 0;
						String carpar2 = rb.getString("Car_parking_2");
						String[] carp2= carpar2.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(carp2[0]);
						end = Integer.parseInt(carp2[1]);
						// }
						String carpark2 = data.substring(start, end);
						end = 0;
						start = 0;
						String twowheeler = rb.getString("Two_wheeler_1");
						String[] twowheler = twowheeler.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(twowheler[0]);
						end = Integer.parseInt(twowheler[1]);
						// }
						String twowheler1 = data.substring(start, end);
						end = 0;
						start = 0;
						String twowheleer = rb.getString("Two_wheeler_2");
						String[] twowheleer2= twowheleer.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(twowheleer2[0]);
						end = Integer.parseInt(twowheleer2[1]);
						// }
						String twowheler2 = data.substring(start, end);
						end = 0;
						start = 0;
						
						String sunadjust = rb.getString("Sundry_adjustment");
						String[] sunadjusts= sunadjust.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(sunadjusts[0]);
						end = Integer.parseInt(sunadjusts[1]);
						// }
						String sunadjustment = data.substring(start, end);
						end = 0;
						start = 0;
						
						String inscostId = rb.getString("Insurance_cost");
						String[] inscostIdsplt = inscostId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(inscostIdsplt[0]);
						end = Integer.parseInt(inscostIdsplt[1]);
						// }
						String insuranceCharges = data.substring(start, end);
						end = 0;
						start = 0;
					
						String propertytaxId = rb.getString("Property_tax");
						String[] propertytaxIdsplt = propertytaxId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(propertytaxIdsplt[0]);
						end = Integer.parseInt(propertytaxIdsplt[1]);
						// }
						String propertytax = data.substring(start, end);
						end = 0;
						start = 0;
	
						String excesspmtId = rb.getString("Excess_Payments");
						String[] excesspmtIdsplt = excesspmtId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(excesspmtIdsplt[0]);
						end = Integer.parseInt(excesspmtIdsplt[1]);
						// }
						String excesspmt = data.substring(start, end);
						end = 0;
						start = 0;
	
						String clubhouseId = rb.getString("Club_House");
						String[] clubhouseIdsplt = clubhouseId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(clubhouseIdsplt[0]);
						end = Integer.parseInt(clubhouseIdsplt[1]);
						// }
						String clubhouse = data.substring(start, end);
						end = 0;
						start = 0;
	
						String arrearsId = rb.getString("Arrears");
						String[] arrearsIdsplt = arrearsId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(arrearsIdsplt[0]);
						end = Integer.parseInt(arrearsIdsplt[1]);
						// }
						String arrears = data.substring(start, end);
						end = 0;
						start = 0;
	
						String preduesId = rb.getString("Previous_Dues");
						String[] preduesIdsplt = preduesId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(preduesIdsplt[0]);
						end = Integer.parseInt(preduesIdsplt[1]);
						// }
						String previousdues = data.substring(start, end);
						end = 0;
						start = 0;
	
						String appsubfeeId = rb.getString("APP_Subscription_Fee");
						String[] appsubfeeIdsplt = appsubfeeId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(appsubfeeIdsplt[0]);
						end = Integer.parseInt(appsubfeeIdsplt[1]);
						// }
						String appsubfee = data.substring(start, end);
						end = 0;
						start = 0;
	
						String totalpayId = rb.getString("Total_Payable");
						String[] totalpayIdsplt = totalpayId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(totalpayIdsplt[0]);
						end = Integer.parseInt(totalpayIdsplt[1]);
						// }
						String totbillamt = data.substring(start, end);
						end = 0;
						start = 0;
	
						String amtinwrdsId = rb.getString("Amount_in_Words");
						String[] amtinwrdsIdsplt = amtinwrdsId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(amtinwrdsIdsplt[0]);
						end = Integer.parseInt(amtinwrdsIdsplt[1]);
						// }
						String amtinwrds = data.substring(start, end);
						end = 0;
						start = 0;
	
						String billnoId = rb.getString("Bill_No");
						String[] billnoIdsplt = billnoId.split("-");						
						start = Integer.parseInt(billnoIdsplt[0]);
						end = Integer.parseInt(billnoIdsplt[1]); 
						String billno = data.substring(start, end);
						end = 0;
						start = 0;
	
						String flatareaId = rb.getString("Flat_Area");
						String[] flatareaIdsplt = flatareaId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(flatareaIdsplt[0]);
						end = Integer.parseInt(flatareaIdsplt[1]);
						// }
						String flatarea = data.substring(start, end);
						end = 0;
						start = 0;
	
						String notesId = rb.getString("Notes");
						String[] notesIdsplt = notesId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(notesIdsplt[0]);
						end = Integer.parseInt(notesIdsplt[1]);
						// }
						String notes = data.substring(start, end);
						end = 0;
						start = 0;
	
						String miscellaneousId = rb.getString("Miscellaneous");
						String[] miscellaneousIdsplt = miscellaneousId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(miscellaneousIdsplt[0]);
						end = Integer.parseInt(miscellaneousIdsplt[1]);
						// }
						String miscellaneous = data.substring(start, end);
						end = 0;
						start = 0;
						
						String flatnoId = rb.getString("Flat_No");
						String[] flatnoIdsplt = flatnoId.split("-");
						// for(int i=0; i<townsplit.length;i++){
						start = Integer.parseInt(flatnoIdsplt[0]);
						end = Integer.parseInt(flatnoIdsplt[1]);
						// }
						String flatno = data.substring(start, end);
						end = 0;
						start = 0;
						
						Commonutility.toWriteConsole(lvrInfileSocyUnqid+ "----societyid--flatno---------- " + flatno);
						Integer usrId = billmainDao.togetusrvalue(lvrInfileSocyUnqid,flatno.trim());
						Commonutility.toWriteConsole("User Id : "+usrId+"---"+ (usrId > 0));
						Commonutility.toWriteConsole("flatno : "+flatno.trim());
						if (usrId > 0) {	
							System.out.println(lvrInfileSocyUnqid+ "----societyid-121-flatno---------- " + flatno);
							CommonUtils common = new CommonUtils();
							MaintenanceFeeTblVO maintanencefee = new MaintenanceFeeTblVO();
							Date curDate = new Date();
							UserMasterTblVo entrydet = new UserMasterTblVo();
							UserMasterTblVo usrid = new UserMasterTblVo();
							usrid.setUserId(usrId);
				
							entrydet.setUserId(userid);
							maintanencefee.setUserId(usrid);
							maintanencefee.setServiceCharge(Float.parseFloat(serviceCharge));				
							maintanencefee.setBillDate(billldate);
							maintanencefee.setMunicipalTax(Float.parseFloat(municipalTax));
							maintanencefee.setPenalty(Float.parseFloat(penalty));
							maintanencefee.setWaterCharge(Float.parseFloat(waterCharge));
							maintanencefee.setNonOccupancyCharge(Float.parseFloat(nonOccupancyCharge));
							maintanencefee.setCulturalCharge(Float.parseFloat(culturalCharge));
							maintanencefee.setSinkingFund(Float.parseFloat(sinkingFund));
							maintanencefee.setRepairFund(Float.parseFloat(repairFund));
							maintanencefee.setInsuranceCharges(Float.parseFloat(insuranceCharges));
							maintanencefee.setPlayZoneCharge(Float.parseFloat(plyzonfes));
							maintanencefee.setMajorRepairFund(Float.parseFloat(majorRepairFund));
							maintanencefee.setInterest(Float.parseFloat(interest));
							maintanencefee.setTotbillamt(Float.parseFloat(totbillamt));
							maintanencefee.setLatefee(Float.parseFloat(latefees));
							maintanencefee.setCarpark1(Float.parseFloat(carpark1));
							maintanencefee.setCarpark2(Float.parseFloat(carpark2));
							maintanencefee.setTwowheeler1(Float.parseFloat(twowheler1));
							maintanencefee.setTwowheeler2(Float.parseFloat(twowheler2));
							maintanencefee.setSundayadjust(Float.parseFloat(sunadjustment));
							maintanencefee.setProtax(Float.parseFloat(propertytax));
							maintanencefee.setExceespay(Float.parseFloat(excesspmt));
							maintanencefee.setClubhouse(Float.parseFloat(clubhouse));
							maintanencefee.setArrears(Float.parseFloat(arrears));
							maintanencefee.setPreviousdue(Float.parseFloat(previousdues));
							maintanencefee.setAppfees(Float.parseFloat(appsubfee));
							maintanencefee.setAmountword(amtinwrds);
							maintanencefee.setBillno(billno);
							maintanencefee.setFlatno(flatno);
							maintanencefee.setFlatarea(flatarea);
							maintanencefee.setNotes(notes);
							maintanencefee.setMiscellious(miscellaneous);
//							totalAmount+=totbillamt;
							maintanencefee.setEntryDatetime(curDate);
							maintanencefee.setPdfstatus("P");
							//maintanencefee.setUploadFileName(filename+".pdf");
							maintanencefee.setEntryBy(entrydet);
							maintanencefee.setStatusFlag(1);
							maintanencefee.setPaidStatus(0);
							documentmng.setDocFolder(2);
							ifsaved = billmainDao.toInsertmaintenance(maintanencefee,prasession); 
							Commonutility.toWriteConsole("ifsaved ---------maintanencefee-------------- "+ifsaved);
							if (ifsaved) {
								documentmng.setUserId(usrid);
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
								//String lvrfilenmae = FilenameUtils.removeExtension(filename);
								//String lvrfileexitension = FilenameUtils.getExtension(filename);
								//Commonutility.getCurrentDateTime("yyyyMMddmmss");
								String lvrMbilname = "Maintencbill_fno"+flatno.trim()+"_"+Commonutility.getCurrentDate("yyyyMMddmmss");
								documentmng.setDocFileName(lvrMbilname + ".pdf");
								ifsaved=docHibernateUtilService.insertDocumentManageTbl(documentmng,prasession);
								Commonutility.toWriteConsole("ifsaved ---------documentmng-------------- "+ifsaved);
							}
							
							if (ifsaved) {
								documentShare.setDocumentId(documentmng);
								documentShare.setUserId(usrid);
								documentShare.setStatus(1);
								documentShare.setEntryBy(entrydet);
								documentShare.setEntryDatetime(curDate);
								ifsaved=docHibernateUtilService.insertDocumentShareTbl(documentShare,prasession);
								Commonutility.toWriteConsole("ifsaved ---------documentShare-------------- "+ifsaved);
							}
							
							if(ifsaved){
								Commonutility.toWriteConsole("ifsaved ---------notificationdet-------------- "+ ifsaved);
								notificationdet.setUserId(usrid);
								String randnumber=common.randomString(Integer.parseInt(rbapp.getString("Notification.random.string.length")));
								notificationdet.setUniqueId(randnumber);
								notificationdet.setReadStatus(Integer.parseInt(rbapp.getString("Initial.read.Status")));
								notificationdet.setSentStatus(Integer.parseInt(rbapp.getString("Initial.sent.Status")));
								notificationdet.setStatusFlag(Integer.parseInt(rbapp.getString("Initial.statusflag")));
								notificationdet.setDescr(rbapp.getString("notification.document"));		
								notificationdet.setEntryBy(entrydet);
								notificationdet.setEntryDatetime(curDate);
								if(Commonutility.checkempty(rbapp.getString("notification.reflg.document"))) {
									notificationdet.setTblrefFlag(Commonutility.stringToInteger(rbapp.getString("notification.reflg.document")));
								} else {
									notificationdet.setTblrefFlag(3);
								}						
								notificationdet.setTblrefID(Commonutility.insertchkintnull(documentmng.getDocumentId()));
								ifsaved=docHibernateUtilService.insertNotificationTbl(notificationdet,prasession);
							}
							lvrDatastatus = true;
						} else {
							if (tx != null) {
								tx.rollback();															
								tx = null;
							}						
							lvrErrorMsg = "Data Vaildation Failed, Flat details not found.";
							Commonutility.toWriteConsole("Step : "+lvrErrorMsg);
							lvrDatastatus = false;
							break;
						}
					} catch (Exception ex) {
						if (tx != null) {
							tx.rollback(); tx = null;							
						}						
						Commonutility.toWriteConsole("Exception Found : " + ex);
						lvrDatastatus = false;
						break;

					}
				
				} else if (lvrHeaderstatus && lvrDatastatus && Commonutility.checkempty(data) && data.length() == Integer.parseInt(rb.getString("input.fooder.data"))) {
					Commonutility.toWriteConsole("fooder-------------------------");
					String lvrEndchar = rb.getString("ending_char");
					if (Commonutility.checkempty(lvrEndchar) && data.endsWith(lvrEndchar)) {
						try {
							String totalrecordsId = rb.getString("Count_of_Data_records");
							String[] totalrecordsIdsplt = totalrecordsId.split("-");						
							start = Integer.parseInt(totalrecordsIdsplt[0]);
							end = Integer.parseInt(totalrecordsIdsplt[1]);						
							String totalrecords = data.substring(start, end);
							end = 0;
							start = 0;
	
							String sumofallamtrecordsId = rb.getString("Sum_of_all_amounts_in_all_records");
							String[] sumofallamtrecordsIdsplt = sumofallamtrecordsId.split("-");						
							start = Integer.parseInt(sumofallamtrecordsIdsplt[0]);
							end = Integer.parseInt(sumofallamtrecordsIdsplt[1]);
							String sumofallamtrecords = data.substring(start, end);
							
							end = 0;
							start = 0;
							String locVrSlQry = "";
							Common common = null;
							boolean lvrfunmtr = false;
							boolean lvrdelete = false;
							try {
								common = new CommonDao();
								locVrSlQry="update MaintenanceFileUploadTblVO set GRAND_TOTAL='"+sumofallamtrecords+"',NO_OF_RECORDS='"+totalrecords+"',status=1 where CHECK_SUM='"+checksumval+"'";
								lvrfunmtr = common.commonUpdate(locVrSlQry);
								if (lvrfunmtr) {
									String qry = "delete BillmaintenanceVO where refId ="+ societycode;								
									lvrdelete = billmainDao.toDeleteEvent(qry);
									lvrFooterstatus = true;
								}
							} catch (Exception e) {
								lvrErrorMsg = "Footer Line Validation Failed";
								Commonutility.toWriteConsole("BillmaintenanceVO Exception toupdate : "+ e);
								lvrFooterstatus = false;
								break;
							}
							
						} catch (Exception e) {
							lvrErrorMsg = "Footer Line Validation Failed";
							Commonutility.toWriteConsole("Exception Fooder : " + e);
							lvrFooterstatus = false;
							break;
						}
					} else {
						lvrErrorMsg = "Footer Line Validation Failed, File does not end with "+lvrEndchar;
						Commonutility.toWriteConsole("Step : " + lvrErrorMsg);
						lvrFooterstatus = false;
						break;
					}
				}
			
			}
			// Final data status
			if (lvrHeaderstatus && lvrDatastatus && lvrFooterstatus) {
				Commonutility.toWriteConsole("Step : All table inserted successfully");
				tx.commit();
				Commonutility.toWriteConsole("Step : PDF Generate will Called");
				CommonBillPack conn = new CommonBillPack();
				conn.pdfGeneration(lvrJxml);
			} else {
				// insert / update status into table
				Commonutility.toWriteConsole("Step : Transaction not commited.");
				Commonutility.toWriteConsole("Error Message : "+lvrErrorMsg);
				Commonutility.toWriteConsole("Header status : "+lvrHeaderstatus);
				Commonutility.toWriteConsole("Data status : "+lvrDatastatus);
				Commonutility.toWriteConsole("Footer status : "+lvrFooterstatus);
				if (tx != null) {
					tx.rollback();
					tx = null;							
				}	
			}
			

		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			Commonutility.toWriteConsole("BillMaintenanceDocUploadEngine ----- Exception -0----"+ ex);
		} finally {
			if(prasession != null ) {prasession.flush();prasession.clear();prasession.close();prasession=null;}
		}
		return "";
	}

}
