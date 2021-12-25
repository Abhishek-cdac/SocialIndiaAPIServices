package com.pack.resident;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.audittrial.AuditTrial;
import com.pack.bitly.GetBitlyLink;
import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.resident.persistance.ResidentDao;
import com.pack.resident.persistance.ResidentDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.SocietyMstTbl;
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
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class ResidentUtility {

	public static String toInsrtResident(JSONObject pDataJson, String pGrpName, String pAuditMsg, String pAuditCode) {	// Insert
		CommonUtils locCommutillObj =null;
		Log log=null; ResidentDao locrestObj=null;
		String locvrSCT_NAME=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null, locvrIDCARD_TYE = null;
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_PSTL_ID=null,locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrOCCUPATION = null, locvrF_MEMBER = null, locvrENTRY_BY = null;		
		String locvrLBR_ISD_CODE=null,locvrLNAME=null, locvrFNAME=null, locvrBLOODTYP=null, locvrGENDER=null,locvrACC_CH=null,locvrNO_FLATS=null,locvrNO_BLOCKS=null,CURRENTLOGIN=null,locvrDOB=null;
		int locLbrID=0;
		//CommonUtils comutil=new CommonUtilsServices();
		Session locSession=null;
		String locSlqry=null;
		Query locQryrst=null;
		SmsInpojo sms = new SmsInpojo();
		CommonUtilsDao common=new CommonUtilsDao();	
		SmsTemplatepojo smsTemplate;
		SmsEngineServices smsService = new SmsEngineDaoServices();
		GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle("applicationResources");
			locSession=HibernateUtil.getSession();
			locCommutillObj = new CommonUtilsDao();
			
			log=new Log();
			//locvrENTRY_DATETIME=Commonutility.toGetcurrentutilldatetime("1");
			 locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "townshipname");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_NAME " + locvrLBR_NAME);
			 locvrSCT_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "societyname");
			// Commonutility.toWriteConsole("Step 3 : Resident Add : locvrSCT_NAME " + locvrSCT_NAME);
			 locvrLBR_EMAIL = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "emailid");
			// Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_EMAIL " + locvrLBR_EMAIL);
			 
			 locvrLBR_ISD_CODE=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isdcode");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_ISD_CODE " + locvrLBR_ISD_CODE);
			 
			 locvrLBR_PH_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "mobno");
			// Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_PH_NO " + locvrLBR_PH_NO);
			 
			 locvrIDCARD_TYE = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "idcardtyp");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrIDCARD_TYE " + locvrIDCARD_TYE);
			 
			 locvrID_CARD_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "idcardno");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrID_CARD_NO " + locvrID_CARD_NO);
			 
			 locvrLBR_PSTL_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "pstlid");
			 locvrLBR_PSTL_ID = locvrLBR_PSTL_ID.trim();
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_PSTL_ID " + locvrLBR_PSTL_ID);
			 
			 locvrLBR_CITY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "city");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_CITY_ID " + locvrLBR_CITY_ID);
			 
			 locvLBR_STATE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "sate");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvLBR_STATE_ID " + locvLBR_STATE_ID);
			 
			 locvrLBR_COUNTRY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "country");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_COUNTRY_ID " + locvrLBR_COUNTRY_ID);
			 
			 locvrOCCUPATION = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "occupation");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrOCCUPATION " + locvrOCCUPATION);
			 
			 locvrF_MEMBER = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "familymem");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrF_MEMBER " + locvrF_MEMBER);
			 
			 locvrBLOODTYP = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "bloodtyp");
			// Commonutility.toWriteConsole("Step 3 : Resident Add : locvrBLOODTYP " + locvrBLOODTYP);
			 
			 locvrFNAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "frstname");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrFNAME " + locvrFNAME);
			 
			 locvrLNAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lastname");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLNAME " + locvrLNAME);
			 
			 locvrGENDER = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "gender");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrGENDER " + locvrGENDER);
			 
			 locvrDOB = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "dob");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrDOB " + locvrDOB);
			 
			 locvrACC_CH= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "accesschannel");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrACC_CH " + locvrACC_CH);
			 
			 locvrNO_BLOCKS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "noofblocks");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrNO_BLOCKS " + locvrNO_BLOCKS);
			 
			 locvrNO_FLATS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "noofflats");	
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrNO_FLATS " + locvrNO_FLATS);
			 
			 locvrLBR_ADD_1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add1");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_ADD_1 " + locvrLBR_ADD_1);
			 
			 locvrLBR_ADD_2 =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add2");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrLBR_ADD_2 " + locvrLBR_ADD_2);
			 
			 locvLBR_STS = "0";
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvLBR_STS " + locvLBR_STS);
			 
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryby");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : locvrENTRY_BY " + locvrENTRY_BY);
			 
			 CURRENTLOGIN =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : CURRENTLOGIN " + CURRENTLOGIN);
			 
			 String famName = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famName");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : famName " + famName);
			 
			 String famMobileNo = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famMobileNo");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : famMobileNo " + famMobileNo);
			 
			 String famEmailId = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famEmailId");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : famEmailId " + famEmailId);
			 
			 String famISD = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famisdcode");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : famISD " + famISD);
			 
			 String famMemTyp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fammemtype");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : famMemTyp " + famMemTyp);
			 
			 String famPrfAccess = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famprfaccess");
			 //Commonutility.toWriteConsole("Step 3 : Resident Add : famPrfAccess " + famPrfAccess);
			 
			 String userregUniId=null;						 
			 UserMasterTblVo locObjRestprfvo=new UserMasterTblVo();
			 String password = locCommutillObj.getRandomval("AZ_09", 10);	
//			 String password="1234567890";
			 locObjRestprfvo.setFirstName(locvrFNAME);
			 locObjRestprfvo.setPassword(locCommutillObj.stringToMD5(password));
			 SocietyMstTbl scociety = new SocietyMstTbl();
			 scociety.setSocietyId(Integer.parseInt(locvrSCT_NAME));
			 locObjRestprfvo.setSocietyId(scociety);			 
			 locObjRestprfvo.setEmailId(locvrLBR_EMAIL);
			 locObjRestprfvo.setIsdCode(locvrLBR_ISD_CODE);
			 locObjRestprfvo.setMobileNo(locvrLBR_PH_NO);
			 IDCardMasterTblVO idObj = new IDCardMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvrIDCARD_TYE) || locvrIDCARD_TYE.equalsIgnoreCase("")||locvrIDCARD_TYE.equalsIgnoreCase("0")){
				 locObjRestprfvo.setiVOcradid(null);
			 }else{
				 idObj.setiVOcradid(Integer.parseInt(locvrIDCARD_TYE));
				 locObjRestprfvo.setiVOcradid(idObj);
				
			 }			 
			locObjRestprfvo.setIdProofNo(locvrID_CARD_NO);
			
			locObjRestprfvo.setFirstName(locvrFNAME);
			locObjRestprfvo.setLastName(locvrLNAME);						
			locObjRestprfvo.setGender(locvrGENDER);
			locObjRestprfvo.setDob(locvrDOB);
			locObjRestprfvo.setNoofblocks(locvrNO_BLOCKS);
			locObjRestprfvo.setNoofflats(Integer.parseInt(locvrNO_FLATS));
			locObjRestprfvo.setAddress1(locvrLBR_ADD_1);
			locObjRestprfvo.setAddress2(locvrLBR_ADD_2);
			locObjRestprfvo.setAccessChannel(Integer.parseInt(locvrACC_CH));
			CityMasterTblVO cityobj = new CityMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvrLBR_CITY_ID) || locvrLBR_CITY_ID.equalsIgnoreCase("0")){
				 locObjRestprfvo.setCityId(null);
			 }else{
				 cityobj.setCityId(Integer.parseInt(locvrLBR_CITY_ID));
				 locObjRestprfvo.setCityId(cityobj);
			 }	
			 
			 PostalCodeMasterTblVO postalObj = new PostalCodeMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvrLBR_PSTL_ID) && locvrLBR_PSTL_ID.equalsIgnoreCase("")){
//				 locObjRestprfvo.setPstlId(null);
				 locObjRestprfvo.setPstlId(0);
			 }else{
//				 postalObj.setPstlId(Integer.parseInt(locvrLBR_PSTL_ID));
//				 locObjRestprfvo.setPstlId(postalObj);
				 locObjRestprfvo.setPstlId(Integer.parseInt(locvrLBR_PSTL_ID));
			 }			 			
			 
			 StateMasterTblVO stateObj = new StateMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvLBR_STATE_ID)  || locvLBR_STATE_ID.equalsIgnoreCase("0")){
				 locObjRestprfvo.setStateId(null);
			 }else{
				 stateObj.setStateId(Integer.parseInt(locvLBR_STATE_ID));
				 locObjRestprfvo.setStateId(stateObj);
			 }
			
			 //locObjRestprfvo.setStateId(stateObj);
			 CountryMasterTblVO countryObj = new CountryMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvrLBR_COUNTRY_ID)  || locvrLBR_COUNTRY_ID.equalsIgnoreCase("0")){
				 locObjRestprfvo.setCountryId(null);
			 }else{
				 countryObj.setCountryId(Integer.parseInt(locvrLBR_COUNTRY_ID));
				 locObjRestprfvo.setCountryId(countryObj);
			 }	
			
			 locObjRestprfvo.setOccupation(locvrOCCUPATION);
			 /*if(!Commonutility.toCheckisNumeric(locvrF_MEMBER)  || locvrF_MEMBER.equalsIgnoreCase("")){
				 locObjRestprfvo.setMembersInFamily(null);
			 }else{
				 locObjRestprfvo.setMembersInFamily(Integer.parseInt(locvrF_MEMBER));
				 
			 }	*/
			locObjRestprfvo.setMembersInFamily(Integer.parseInt(locvrF_MEMBER));
			locObjRestprfvo.setBloodType(locvrBLOODTYP);
			String lvrGrpunqid = locvrLBR_PH_NO + locCommutillObj.getRandomval("AZ_09", 10);
			locObjRestprfvo.setGroupUniqId(lvrGrpunqid);
			locObjRestprfvo.setStatusFlag(Integer.parseInt(locvLBR_STS));
			locObjRestprfvo.setEntryBy(Integer.parseInt(locvrENTRY_BY));
				 
			JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "blacknameJary");// blackname insert into 
		    JSONArray flatjary=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "flatnameJary");//flatname insert into
			//FlatMasterTblVO inrlocLbrblack1=null;
			locrestObj=new ResidentDaoservice();
			//boolean lbrblkflg1=false;
				// for (int i = 0; i < jry.length(); i++) {
					 //String blackname=(String)jry.getString(i);
					// String flatname =(String)flatjary.getString(i);
						 //lbrblkflg1=true;
					
				// }
				
			 // Select Group Code on REsident	
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";		
			 Commonutility.toWriteConsole("resident code get---- "+locSlqry);
			 locQryrst=locSession.createQuery(locSlqry);			
			 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
			 if(locGrpMstrVOobj!=null){				
				 locGRPMstrvo=new GroupMasterTblVo();
				 locGRPMstrvo.setGroupCode(locGrpMstrVOobj.getGroupCode());	
				 locObjRestprfvo.setGroupCode(locGRPMstrvo);
				 Integer lvrgrpcde = locGrpMstrVOobj.getGroupCode();
				// Commonutility.toWriteConsole("Group Code : "+Commonutility.toCheckNullEmpty(lvrgrpcde));
				 String grpcode = (String) Commonutility.toCheckNullEmpty(lvrgrpcde);
				 userregUniId = Commonutility.toGetKeyIDforTbl("UserMasterTblVo","max(userId)",grpcode,"7");
				 locGRPMstrvo = null;
			 }else{// new group create				
				uamDao uam = new uamDaoServices();
				int locGrpcode = uam.createnewgroup_rtnId(pGrpName);
				userregUniId = Commonutility.toGetKeyIDforTbl("UserMasterTblVo","max(userId)",Commonutility.toCheckNullEmpty(locGrpcode),"7");
				if (locGrpcode != -1 && locGrpcode != 0) {
					locGRPMstrvo = new GroupMasterTblVo();
					locGRPMstrvo.setGroupCode(locGrpcode);
					locObjRestprfvo.setGroupCode(locGRPMstrvo);
				} else {
					 locObjRestprfvo.setGroupCode(null);
				}	
				uam = null; locGRPMstrvo = null;
			 }			
			 locObjRestprfvo.setUserName(userregUniId);
			 locObjRestprfvo.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			 //----------- REsident reg Start-----------
			 log.logMessage("Resident Detail insert will start.", "info", ResidentUtility.class);
			 //locLbrID=locrestObj.saveResidentInfo_int(locObjRestprfvo);
			boolean lvrTochkexitsuser = locrestObj.duplicateResident_id(locObjRestprfvo);
			if(lvrTochkexitsuser){
				 locLbrID = locrestObj.saveResidentInfo_int(locObjRestprfvo,famName,famEmailId,famMobileNo,famISD,famMemTyp,famPrfAccess);
				 Commonutility.toWriteConsole(locLbrID+": id User");
				 log.logMessage("resident Detail insert End resident Id : "+locLbrID, "info", ResidentUtility.class);
				// -----------userid reg end------------			 
				 if (locLbrID!=0 && locLbrID!=-1) {
					// JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "blacknameJary");// blackname insert into 
					 //JSONArray flatjary=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "flatnameJary");//flatname insert into
					 FlatMasterTblVO inrlocLbrblack=null;
					 locrestObj=new ResidentDaoservice();
					 log.logMessage("Resident  Flat Detail insert will start.", "info", ResidentUtility.class);
					 boolean lbrblkflg=false;
					 UserMasterTblVo userId=new UserMasterTblVo();
					 
					 for (int i = 0; i < jry.length(); i++) {
						String blackname = (String) jry.getString(i);
						String flatname = (String) flatjary.getString(i);						
						if (Commonutility.checkempty(flatname) && Commonutility.checkempty(blackname)) {
							lbrblkflg = true;
							inrlocLbrblack = new FlatMasterTblVO();
							userId.setUserId(locLbrID);
							inrlocLbrblack.setUsrid(userId);
							inrlocLbrblack.setWingsname(blackname);
							inrlocLbrblack.setFlatno(flatname);
							inrlocLbrblack.setStatus(1);
							if (i == 0) {
								inrlocLbrblack.setIvrBnismyself(1);
							} else {
								inrlocLbrblack.setIvrBnismyself(0);
							}
							inrlocLbrblack.setEntryby(Integer.parseInt(CURRENTLOGIN));
							inrlocLbrblack.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							int locflatinsrtrst = locrestObj.saveResidentFlatInfo_intRtn(inrlocLbrblack);
							Commonutility.toWriteConsole("return flat_id::: "+locflatinsrtrst);
							inrlocLbrblack=null;
						}
					 }
					 if(!lbrblkflg){
						 Commonutility.toWriteConsole("[Deafult flat insert]");
						 	inrlocLbrblack = new FlatMasterTblVO();
						 	userId.setUserId(locLbrID);
							inrlocLbrblack.setUsrid(userId);
							inrlocLbrblack.setWingsname("");
							inrlocLbrblack.setFlatno("");
							inrlocLbrblack.setStatus(1);							
							inrlocLbrblack.setIvrBnismyself(1);							
							inrlocLbrblack.setEntryby(Integer.parseInt(CURRENTLOGIN));
							inrlocLbrblack.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							int locflatinsrtrst = locrestObj.saveResidentFlatInfo_intRtn(inrlocLbrblack);
					 }
					 
					 
					 if(lbrblkflg){// Audit trial use only Flat update
						 if(Commonutility.toCheckisNumeric(locvrENTRY_BY)){
							 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, Integer.parseInt(locvrENTRY_BY));
						 }else{
							 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, 1);
						 }				 
					 }
					 log.logMessage("Resident  Flat Detail insert will end.", "info", ResidentUtility.class);
					 boolean lvrRsdemsms = false; // not need to send
					 if(lvrRsdemsms){
						String lvrGplyurl = rb.getString("gply.storeurl");
						String pAccesstkn = rb.getString("bitly.accesstocken");
						String btlySrvurl = rb.getString("bitly.server.url");
							
						String lvrBitlylnk = GetBitlyLink.toGetBitlyLinkMthd(lvrGplyurl, "yes", pAccesstkn, btlySrvurl);
					 //Send Email							
					 if(locObjRestprfvo.getEmailId()!=null && Commonutility.checkempty(locObjRestprfvo.getEmailId())){
								EmailEngineServices emailservice = new EmailEngineDaoServices();
								EmailTemplateTblVo emailTemplate;
								try {
					            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Resident' and status ='1'";
					            emailTemplate = emailservice.emailTemplateData(emqry);
					            String emailMessage = emailTemplate.getTempContent();
					            
					            emqry = locCommutillObj.templateParser(emailMessage, 1, "", password);
					            String[] qrySplit = emqry.split("!_!");
					            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.mobileNo='"+locObjRestprfvo.getMobileNo()+"'";
					            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
					            emailTemplate.setTempContent(emailMessage);
					            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
					            
					            EmailInsertFuntion emailfun = new EmailInsertFuntion();
					            emailfun.test2(locObjRestprfvo.getEmailId(), emailTemplate.getSubject(), emailMessage);
					          } catch (Exception ex) {
					            Commonutility.toWriteConsole("Excption found in Emailsend ResidentUtility.class : " + ex);
					            log.logMessage("Exception in ResidentUtility create resident flow emailFlow----> " + ex, "error",ResidentUtility.class);
					            
					          }	
							
							}	
					 
						 // Sending SMS
					          Commonutility.toWriteConsole("===================sms===========================");
					          try {
					            String mailRandamNumber;
					            mailRandamNumber = common.randInt(5555, 999999999);
					            String qry = "FROM SmsTemplatepojo where " + "templateName ='Create Resident' and status ='1'";
					            smsTemplate = smsService.smsTemplateData(qry);
					            String smsMessage = smsTemplate.getTemplateContent();
					            smsMessage = smsMessage.replace("[APPBITLYLINK]", lvrBitlylnk);
					            qry = common.smsTemplateParser(smsMessage, 1, "", password);
					            String[] qrySplit = qry.split("!_!");
					            String qryform = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.mobileNo='" + locObjRestprfvo.getMobileNo() + "'";
					            smsMessage = smsService.smsTemplateParserChange(qryform,
					                Integer.parseInt(qrySplit[1]), smsMessage);
					            sms.setSmsCardNo(mailRandamNumber);
					            sms.setSmsEntryDateTime(common.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					            sms.setSmsMobNumber(locObjRestprfvo.getMobileNo());
					            sms.setSmspollFlag("F");
					            sms.setSmsMessage(smsMessage);
					            smsService.insertSmsInTable(sms);
					          } catch (Exception ex) {
					        	  Commonutility.toWriteConsole("Excption found in smssend ResidentUtility.class : " + ex);
						            log.logMessage("Exception in signup admin flow ResidentUtility----> " + ex, "error",ResidentUtility.class);
					           
					          }			
					 } else {
						 
					 }
					 Commonutility.toWriteConsole("success!_!"+locLbrID );
					 return "success!_!"+locLbrID;				 
				 }else{
					 Commonutility.toWriteConsole("error!_!"+locLbrID );
					 return "error!_!"+locLbrID;
				 }	
			} else {
				 Commonutility.toWriteConsole("existmob!_!"+locLbrID );
				 return "existmob!_!"+locLbrID;
			}
			
			 
			 		 			 		
		} catch (Exception e) {
			String error = "";
			StackTraceElement[] array = e.getStackTrace();
			for (int i = 0; i < array.length; i++) {
				error = error + array[i];
			}
			Commonutility.toWriteConsole("Exception found in ResidentUtility.toInsrtResident : "+error);
			log.logMessage("Exception : "+e, "error", ResidentUtility.class);
			locLbrID=0;
			return "error!_!"+locLbrID;
		} finally {
			 locCommutillObj =null; locrestObj=null;
			 locvrLBR_NAME = null; locvrLBR_EMAIL = null; locvrLBR_PH_NO = null; locvrBLOODTYP = null;
			 locvrID_CARD_NO = null; locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrENTRY_BY = null;
			 if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;} locSlqry=null; locQryrst=null;locGrpMstrVOobj=null;locGRPMstrvo=null;
		}
		
	}
	
	public static JSONObject toSltSingleResidentDtl(JSONObject pDataJson, Session pSession, String pAuditMsg, String pAuditCode) {// Select on single user data
		String locvrLBR_ID=null, locvrLBR_STS=null; 
		UserMasterTblVo locLDTblvoObj=null;
		String loc_slQry=null;
		Query locQryObj= null;
		JSONObject locRtnDataJSON=null;
			Log log=null;
			String loc_slQry_flat=null;	
			Iterator locObjflatlst_itr=null;
			JSONArray locLBRSklJSONAryobj=null;
			JSONObject locInrLbrSklJSONObj=null;
			FlatMasterTblVO locflatdbtbl=null;
			ResidentDao locrest_memObj=null;
			List<MvpFamilymbrTbl> userFamilyList = new ArrayList<MvpFamilymbrTbl>();
		try {
			log=new Log();			
			Commonutility.toWriteConsole("Step 1 : resident detail block enter");
			log.logMessage("Step 1 : Select resident detail block enter", "info", ResidentUtility.class);
			
			locRtnDataJSON=new JSONObject();
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "restid");
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "reststatus");
						
					
			loc_slQry="from UserMasterTblVo where userId="+locvrLBR_ID+"  ";
			locQryObj=pSession.createQuery(loc_slQry);			
			
			locLDTblvoObj = (UserMasterTblVo)locQryObj.uniqueResult();;
			Commonutility.toWriteConsole("Step 2 : Select resident detail query executed.");
			locRtnDataJSON.put("usrid", locLDTblvoObj.getUserId());
			
			if(locLDTblvoObj.getSocietyId()!=null){
				locRtnDataJSON.put("townshipname", Commonutility.toCheckNullEmpty(locLDTblvoObj.getSocietyId().getTownshipId().getTownshipName()));
				locRtnDataJSON.put("societyname", Commonutility.toCheckNullEmpty(locLDTblvoObj.getSocietyId().getSocietyName()));
				locRtnDataJSON.put("township_id", Commonutility.toCheckNullEmpty(locLDTblvoObj.getSocietyId().getTownshipId().getTownshipId()));
				locRtnDataJSON.put("society_id", Commonutility.toCheckNullEmpty(locLDTblvoObj.getSocietyId().getSocietyId()));
				locRtnDataJSON.put("status", Commonutility.toCheckNullEmpty(locLDTblvoObj.getStatusFlag()));
			}else{
				locRtnDataJSON.put("townshipname", "");
				locRtnDataJSON.put("societyname", "");
				locRtnDataJSON.put("township_id", "");
				locRtnDataJSON.put("society_id", "");
			}
			
			locRtnDataJSON.put("firstname", Commonutility.toCheckNullEmpty(locLDTblvoObj.getFirstName()));
			locRtnDataJSON.put("lastname", Commonutility.toCheckNullEmpty(locLDTblvoObj.getLastName()));
			locRtnDataJSON.put("email",Commonutility.toCheckNullEmpty(locLDTblvoObj.getEmailId()));
			locRtnDataJSON.put("isd", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIsdCode()));
			locRtnDataJSON.put("mobno", Commonutility.toCheckNullEmpty(locLDTblvoObj.getMobileNo()));
			if(locLDTblvoObj.getiVOcradid()!=null){
				locRtnDataJSON.put("idcard_typ", Commonutility.toCheckNullEmpty((locLDTblvoObj.getiVOcradid().getiVOcradid())));
				locRtnDataJSON.put("idcard_typname",Commonutility.toCheckNullEmpty(locLDTblvoObj.getiVOcradid().getiVOcradname()));
			} else {
				locRtnDataJSON.put("idcard_typ", "");
				locRtnDataJSON.put("idcard_typname", "");
			}
			
			//locRtnDataJSON.put("idcard_typ", Commonutility.toCheckNullEmpty(locLDTblvoObj.getiVOcradid().getiVOcradid()));
			locRtnDataJSON.put("idproofno", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIdProofNo()));
			if(locLDTblvoObj.getPstlId()!=null){	
//				locRtnDataJSON.put("intv_rest_pstlid",String.valueOf(locLDTblvoObj.getPstlId().getPstlId()));
//				locRtnDataJSON.put("rest_pincodeName",locLDTblvoObj.getPstlId().getPstlCode());
//				locRtnDataJSON.put("intv_rest_cityid",String.valueOf(locLDTblvoObj.getPstlId().getCityId().getCityId()));
//				locRtnDataJSON.put("rest_cityName",locLDTblvoObj.getPstlId().getCityId().getCityName());
//				locRtnDataJSON.put("intv_rest_stateid",String.valueOf(locLDTblvoObj.getPstlId().getCityId().getStateId().getStateId()));
//				locRtnDataJSON.put("rest_stateName",locLDTblvoObj.getPstlId().getCityId().getStateId().getStateName());
//				locRtnDataJSON.put("intv_rest_cntry",String.valueOf(locLDTblvoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//				locRtnDataJSON.put("rest_cntryName",locLDTblvoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());
				
				locRtnDataJSON.put("intv_rest_pstlid",String.valueOf(locLDTblvoObj.getPstlId()));
				locRtnDataJSON.put("rest_pincodeName",locLDTblvoObj.getPstlId());
				
			}else{
				locRtnDataJSON.put("intv_rest_pstlid","");
				locRtnDataJSON.put("cmpy_pincodeName","");
//				locRtnDataJSON.put("intv_rest_cityid","");
//				locRtnDataJSON.put("cmpy_cityName","");
//				locRtnDataJSON.put("intv_rest_stateid","");
//				locRtnDataJSON.put("cmpy_stateName","");
//				locRtnDataJSON.put("intv_rest_cntry","");
//				locRtnDataJSON.put("rest_cntryName","");
			}
			
			if(locLDTblvoObj.getCityId()!=null){
				locRtnDataJSON.put("intv_rest_cityid",String.valueOf(locLDTblvoObj.getCityId().getCityId()));
				locRtnDataJSON.put("rest_cityName",locLDTblvoObj.getCityId().getCityName());
				
				if(locLDTblvoObj.getStateId()!=null){
					locRtnDataJSON.put("intv_rest_stateid",String.valueOf(locLDTblvoObj.getCityId().getStateId().getStateId()));
					locRtnDataJSON.put("rest_stateName",locLDTblvoObj.getCityId().getStateId().getStateName());
					
					if(locLDTblvoObj.getCountryId()!=null){
						locRtnDataJSON.put("intv_rest_cntry",String.valueOf(locLDTblvoObj.getCityId().getStateId().getCountryId().getCountryId()));
						locRtnDataJSON.put("rest_cntryName",locLDTblvoObj.getCityId().getStateId().getCountryId().getCountryName());
					}
					else{
						locRtnDataJSON.put("intv_rest_cntry","");
						locRtnDataJSON.put("rest_cntryName","");
					}
				}
				else{
					locRtnDataJSON.put("intv_rest_stateid","");
					locRtnDataJSON.put("cmpy_stateName","");
					locRtnDataJSON.put("intv_rest_cntry","");
					locRtnDataJSON.put("rest_cntryName","");
				}
			}
			else{
				locRtnDataJSON.put("intv_rest_cityid","");
				locRtnDataJSON.put("cmpy_cityName","");
				locRtnDataJSON.put("intv_rest_stateid","");
				locRtnDataJSON.put("cmpy_stateName","");
				locRtnDataJSON.put("intv_rest_cntry","");
				locRtnDataJSON.put("rest_cntryName","");
			}
					
			locRtnDataJSON.put("gender",Commonutility.toCheckNullEmpty(locLDTblvoObj.getGender()));
			locRtnDataJSON.put("dob",Commonutility.toCheckNullEmpty(locLDTblvoObj.getDob()));
			locRtnDataJSON.put("bloodgroup", Commonutility.toCheckNullEmpty(locLDTblvoObj.getBloodType()));
			locRtnDataJSON.put("accesschennel", Commonutility.toCheckNullEmpty(locLDTblvoObj.getAccessChannel()));
			locRtnDataJSON.put("address1", Commonutility.toCheckNullEmpty(locLDTblvoObj.getAddress1()));
			locRtnDataJSON.put("address2", Commonutility.toCheckNullEmpty(locLDTblvoObj.getAddress2()));
			locRtnDataJSON.put("noofblocks", Commonutility.toCheckNullEmpty(locLDTblvoObj.getNoofblocks()));
			locRtnDataJSON.put("noofflats", Commonutility.toCheckNullEmpty(locLDTblvoObj.getNoofflats()));
			locRtnDataJSON.put("occupation", Commonutility.toCheckNullEmpty(locLDTblvoObj.getOccupation()));
			locRtnDataJSON.put("member", Commonutility.toCheckNullEmpty(locLDTblvoObj.getMembersInFamily()));
			if(locLDTblvoObj.getImageNameWeb()!=null){
				locRtnDataJSON.put("userImage", Commonutility.toCheckNullEmpty(locLDTblvoObj.getImageNameWeb()));	
			}else{
				locRtnDataJSON.put("userImage", "");	
			}
			
			
			log.logMessage("Step 2: Select resident flat detail block start.", "info", ResidentUtility.class);
			loc_slQry_flat="from FlatMasterTblVO where usrid="+locvrLBR_ID+" and status = 1";
			locObjflatlst_itr=pSession.createQuery(loc_slQry_flat).list().iterator();	
			Commonutility.toWriteConsole("Step 3 : Select resident flat wings detail query executed.");
			locLBRSklJSONAryobj=new JSONArray();
			while (locObjflatlst_itr!=null &&  locObjflatlst_itr.hasNext() ) {
				locflatdbtbl=(FlatMasterTblVO)locObjflatlst_itr.next();
				locInrLbrSklJSONObj=new JSONObject();
				if(locflatdbtbl.getFlat_id()!=null){
					locInrLbrSklJSONObj.put("wings_name", locflatdbtbl.getWingsname());
					locInrLbrSklJSONObj.put("flat_no", locflatdbtbl.getFlatno());
				}				
				locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
			}
			
			log.logMessage("Step 3: Select resident flatandwingsname detail block end.", "info", ResidentUtility.class);
			locRtnDataJSON.put("jArry_wing_flat", locLBRSklJSONAryobj);
			Commonutility.toWriteConsole("Step 5 : Return JSON DATA : "+locRtnDataJSON);						
			Commonutility.toWriteConsole("Step 6 : Select resident detail block end.");
			log.logMessage("Step 4: Select resident detail block end.", "info", ResidentUtility.class);	
			Commonutility.toWriteConsole("Step 7 : Select resident Member  detail query executed.");
			locrest_memObj = new ResidentDaoservice();
			userFamilyList = locrest_memObj.getUserFamilyList(locLDTblvoObj.getUserId());
			JSONObject finalJsonarr1 = new JSONObject();
			JSONArray jArray1 = new JSONArray();
			Commonutility.toWriteConsole("userFamilyList.size()   "+userFamilyList.size());
			if( userFamilyList!=null && userFamilyList.size()>0){
				MvpFamilymbrTbl userObj;
				for (Iterator<MvpFamilymbrTbl> it = userFamilyList.iterator(); it.hasNext();) {
					userObj = it.next();
					finalJsonarr1 = new JSONObject();
					if(userObj!=null){
						finalJsonarr1.put("childmobile", Commonutility.toCheckNullEmpty(userObj.getFmbrPhNo()));
						finalJsonarr1.put("childemail", Commonutility.toCheckNullEmpty(userObj.getFmbrEmail()));
						finalJsonarr1.put("childname", Commonutility.toCheckNullEmpty(userObj.getFmbrName()));
						finalJsonarr1.put("fmbrisd", Commonutility.toCheckNullEmpty(userObj.getFmbrIsdCode()));
						if(userObj.getFmbrType()!=null){
							finalJsonarr1.put("fmbrmemtype", userObj.getFmbrType());
						} else {
							finalJsonarr1.put("fmbrmemtype", 0);
						}
					
						if(userObj.getFmbrProfAcc()!=null){
							finalJsonarr1.put("fmbrprfaccess", userObj.getFmbrProfAcc());
						} else {
							finalJsonarr1.put("fmbrprfaccess", 1);
						}							
						finalJsonarr1.put("fmbruniqid", Commonutility.toCheckNullEmpty(userObj.getFmbrTntId()));
						jArray1.put(finalJsonarr1);
					} else{
						
					}					
					
			}
				locRtnDataJSON.put("userfamilydetail", jArray1);
			}else{
				finalJsonarr1.put("childmobile", "");
				finalJsonarr1.put("childemail", "");
				finalJsonarr1.put("childname", "");
				finalJsonarr1.put("fmbrisd", "");
				finalJsonarr1.put("fmbrmemtype", 0);
				finalJsonarr1.put("fmbrprfaccess", 0);
				finalJsonarr1.put("fmbruniqid", "");
				jArray1.put(finalJsonarr1);
				locRtnDataJSON.put("userfamilydetail", jArray1);
			}
			return locRtnDataJSON;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Step -1 : Select resident detail Exception found in ResidentUtility.toSltSingleresidentDtl : "+e);
			log.logMessage("Exception : "+e, "error", ResidentUtility.class);
			locRtnDataJSON=new JSONObject();
			locRtnDataJSON.put("catch", "Error");
			}catch(Exception ee){}
			return locRtnDataJSON;
		} finally {
			locvrLBR_ID=null;  locrest_memObj=null; locLDTblvoObj=null; loc_slQry=null; locRtnDataJSON=null;log=null;			 
		}
	}
	
	
	public static String toUpdtResident(JSONObject pDataJson,String pAuditMsg, String pAuditCode) {// Update
		//JSONObject locRtnJson=null;
		Log log=null;
		String locvrSOCIETYID=null,locvrLBR_NAME = null,locvrLBR_EMAIL=null,locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null;
		String locvLBR_STATE_ID = null,locvrLBR_PSTL_ID=null,locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrID_TYP = null;
		String locvrID_NO = null, locvrENTRY_BY = null;
		String locvrLBR_ISD_CODE=null,locvrMOB_NO=null, locvrFNAME=null, locvrLNAME=null, locvrLBR_DESCP=null;
		String locvrLBR_ID=null,locvrGENDER=null,locvrDOB=null;
		String locvrACC_CH=null, locvrNO_BLOCKS=null,locvrNO_FLATS=null,locvrOCCUPUATION=null,locvrF_MEMBER=null,locvrBLDGRP=null,locvrfamName=null,locvrfammobno=null,locvrfemail=null,locvrfmbrISD=null,locvrfmbrMtype=null,locvrfmbrPrfaccess=null;
		String locLbrUpdqry="";
		boolean locLbrUpdSts=false;
		 ResidentDao lvrResidntdoObj=null;
			CommonUtils locCommutillObj =null;
		try {
			locCommutillObj = new CommonUtilsDao();
			log=new Log();
			 locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "restid");
			 locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "townshipname");
			 locvrSOCIETYID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "societyname");
			 locvrLBR_ISD_CODE=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isdcode");
			 locvrMOB_NO=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "mobno"); 
			 locvrID_TYP=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "idcard_typ");
			 
			 locvrID_NO=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "idproofno");
			 locvrLBR_EMAIL = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "emailid");
			 locvrFNAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fname");
			 locvrLNAME =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lname");
			 locvrGENDER = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "gender");
			 locvrDOB =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "dob");
			 locvrACC_CH=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "accesschannel");
			 locvrNO_BLOCKS=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "noofblocks");
			 locvrNO_FLATS=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "noofflats");
			 locvrLBR_PSTL_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "postalcode");
			 locvrLBR_CITY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "city");
			 locvLBR_STATE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "sate");
			 locvrLBR_COUNTRY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "country");
			 locvrLBR_ADD_1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add1");
			 locvrLBR_ADD_2 =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add2");
			 locvrOCCUPUATION =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "occupation");
			 locvrF_MEMBER = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fmember");
			 locvrBLDGRP = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "bloodgrp");
			 
			 
			 System.out.println(Commonutility.toHasChkJsonRtnValObj(pDataJson, "status"));
			 String status = ""+ Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			 locvLBR_STS = (status=="") ? "1" : status;
			 System.out.println(" User current status : ["+locvLBR_STS+"]");
			 //locvLBR_STS = "1";
			 locvrfamName=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famName");
			 locvrfammobno =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famMobileNo");
			 locvrfemail=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famEmailId");
			 locvrfmbrISD=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famisdcode");
			 locvrfmbrMtype =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fammemtype");
			 locvrfmbrPrfaccess=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "famprfaccess");
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryby");
			 String lvrFmbrUniquid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fmemberuniqueid");//family member unique id
			 locLbrUpdqry ="update UserMasterTblVo set  idProofNo ='"+locvrID_NO+"',firstName='"+locvrFNAME+"',lastName='"+locvrLNAME+"',emailId='"+locvrLBR_EMAIL+"',gender='"+locvrGENDER+"',dob='"+locvrDOB+"',accessChannel='"+locvrACC_CH+"',noofblocks='"+locvrNO_BLOCKS+"',noofflats='"+locvrNO_FLATS+"',address1='"+locvrLBR_ADD_1+"',address2='"+locvrLBR_ADD_2+"',membersInFamily='"+locvrF_MEMBER+"',occupation='"+locvrOCCUPUATION+"',bloodType='"+locvrBLDGRP+"',";
			 
			 if(!Commonutility.toCheckisNumeric(locvrID_TYP) || locvrID_TYP.equalsIgnoreCase("0")){
				 locLbrUpdqry+="iVOcradid = "+null+",";
			 }else{
				 locLbrUpdqry+="iVOcradid = "+Integer.parseInt(locvrID_TYP)+",";		
			 }		
			 if(!Commonutility.toCheckisNumeric(locvrLBR_PSTL_ID) || locvrLBR_PSTL_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="pstlId = "+null+",";
			 }else{
				 locLbrUpdqry+="pstlId = "+Integer.parseInt(locvrLBR_PSTL_ID)+",";				
			 }			 			
			 
			 if(!Commonutility.toCheckisNumeric(locvrLBR_CITY_ID) || locvrLBR_CITY_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="cityId = "+null+",";
			 }else{
				 locLbrUpdqry+="cityId = "+Integer.parseInt(locvrLBR_CITY_ID)+",";				
			 }
			 
			 if(!Commonutility.toCheckisNumeric(locvLBR_STATE_ID) || locvLBR_STATE_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="stateId="+null+",";				
			 }else{
				 locLbrUpdqry+="stateId="+Integer.parseInt(locvLBR_STATE_ID)+",";
			 }			 
			 if(!Commonutility.toCheckisNumeric(locvrLBR_COUNTRY_ID) || locvrLBR_COUNTRY_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="countryId= "+null+",";
			 }else{
				 locLbrUpdqry+="countryId= "+Integer.parseInt(locvrLBR_COUNTRY_ID)+",";
			 }				 			 			 			 					 			 			 
			 if(!Commonutility.toCheckisNumeric(locvLBR_STS) || locvLBR_STS.equalsIgnoreCase("0")){
				 locLbrUpdqry+="statusFlag= "+locvLBR_STS+"";
			 }else{
				 locLbrUpdqry+="statusFlag= "+Integer.parseInt(locvLBR_STS)+"";				
			 }			 						 						 			 
			 locLbrUpdqry+="  where userId ="+locvrLBR_ID+"";			 			
			 log.logMessage("Updqry : "+locLbrUpdqry, "info", ResidentUtility.class);
			 lvrResidntdoObj=new ResidentDaoservice();
			 locLbrUpdSts = lvrResidntdoObj.updateResidentInfo(locLbrUpdqry,locvrfamName,locvrfammobno,locvrfemail,Integer.parseInt(locvrLBR_ID),locvrfmbrISD,locvrfmbrMtype,locvrfmbrPrfaccess,lvrFmbrUniquid);				 			
			 JSONArray jry = (JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "blacknameJary");// Flatdetails insert into 
			 JSONArray flatjary =(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "flatnameJary");
			 FlatMasterTblVO inrlocflatdbl=null;
			 log.logMessage("Resident  Flat Detail insert will start.", "info", ResidentUtility.class);
			 
			 boolean dlrst = lvrResidntdoObj.deleteFlatdblInfo(Integer.parseInt(locvrLBR_ID));
			 boolean lbrflg=false;
			 if(dlrst){
				 UserMasterTblVo userId=new UserMasterTblVo();
				 for (int i = 0; i < jry.length(); i++) {
					String blackname = (String) jry.getString(i);
					String flatname = (String) flatjary.getString(i);	
					if(Commonutility.checkempty(flatname) && Commonutility.checkempty(blackname)){
					inrlocflatdbl = new FlatMasterTblVO();
					userId.setUserId(Integer.parseInt(locvrLBR_ID));
					inrlocflatdbl.setUsrid(userId);
					inrlocflatdbl.setWingsname(blackname);
					inrlocflatdbl.setFlatno(flatname);
					inrlocflatdbl.setStatus(1);
					if (i == 0) {
						inrlocflatdbl.setIvrBnismyself(1);
					} else {
						inrlocflatdbl.setIvrBnismyself(0);
					}
					inrlocflatdbl.setEntryby(Integer.parseInt(locvrENTRY_BY));
					inrlocflatdbl.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					int locSkilinsrtrst = lvrResidntdoObj.saveResidentFlatInfo_intRtn(inrlocflatdbl);
					inrlocflatdbl=null;
					lbrflg=true;
					}
				 } 
				 if(lbrflg){// Audit trial use only Wings/flats update
					 if(Commonutility.toCheckisNumeric(locvrENTRY_BY)){
						 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, Integer.parseInt(locvrENTRY_BY));
					 }else{
						 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, 1);
					 }				 
				 }
			 }else{
				 
			 }			 			 					 						 
			 if(locLbrUpdSts){
				 return "success";
			 }else{
				 return "error";
			 }	 	
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception found in ResidentUtility.toUpdtresident : "+e);
			log.logMessage("Exception : "+e, "error", ResidentUtility.class);
			return "error";						
		} finally {
			 log=null; lvrResidntdoObj=null;
			 locvrSOCIETYID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null;
			 locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrID_TYP = null; locvrACC_CH = null; locvrENTRY_BY = null;locvrfammobno=null;locvrfamName=null;locvrfmbrISD=null;
			 locvrfmbrMtype=null;locvrfmbrPrfaccess=null;
		}		
	}
	
	
	public static String toDeActResident(JSONObject pDataJson, Session pSession,String pAuditMsg, String pAuditCode) {
		String locvrLBR_ID=null;
		String locrestUpdqry=null;		
		boolean locrestUpdSts=false;
		ResidentDao locrestObj=null;
		Log log=null;
		try {
			log=new Log();
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "restid");
			locrestUpdqry ="update UserMasterTblVo set statusFlag=3 where userId ="+locvrLBR_ID+"";
			log.logMessage("Updqry : "+locrestUpdqry, "info", ResidentUtility.class);
			locrestObj=new ResidentDaoservice();
			locrestUpdSts = locrestObj.updateResidentInfo(locrestUpdqry,null,null,null,0,null,null,null,null);
			 if(locrestUpdSts){
				 return "success";
			 }else{
				 return "error";
			 }	
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception found in ResidentUtility.toDeActResident : "+e);
			log.logMessage("Exception : "+e, "error", ResidentUtility.class);
			return "error";
		} finally {
			 log=null; locrestObj=null;
			 locvrLBR_ID=null; 	
			 locrestUpdqry=null;locrestUpdSts=false;				
		}
	}

	public static String readExcel(String xlsname, int usrid,int startrow,int columnlength,String extension) {
		Log log=null;
        JSONObject saveRow=null;
        try {
        	log=new Log();
        	saveRow=new JSONObject();
            FileInputStream file = new FileInputStream(new File(xlsname));
            if (extension.equalsIgnoreCase("xls")) {
            	Commonutility.toWriteConsole("extension::xls:::::::: "+extension);
            	HSSFWorkbook workbook = new HSSFWorkbook(file);
                HSSFSheet sheet = workbook.getSheetAt(0);
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                DataFormatter formatter = new DataFormatter();
                String rowstr = "";
                StringBuilder sb = new StringBuilder();         
                Iterator<Row> rowIterator = sheet.iterator();
                JSONArray json_data1 =null;
                JSONArray json_data =null;
                JSONObject saveColumn=new JSONObject();
                json_data1= new JSONArray();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    int rownum = row.getRowNum(); 
                    boolean flgr=false;
                    if (rownum >= startrow) {
                    	 saveColumn=new JSONObject();
                    	 json_data= new JSONArray();
                    	for(int i=0; i<columnlength;i++)
                    	{  	
                    		rowstr="";
                    		 Cell cell = row.getCell(i);                    		 
                             if (cell != null) {                                 	 
                                 switch (cell.getCellType()) {
                                     case Cell.CELL_TYPE_FORMULA:
                                         rowstr = formatter.formatCellValue(cell, evaluator);
                                         break;
                                     case Cell.CELL_TYPE_NUMERIC:
                                         rowstr = formatter.formatCellValue(cell);
                                         break;
                                     case Cell.CELL_TYPE_STRING:
                                    		rowstr =cell.getStringCellValue();
                                       break;                                       
                                       default:
                                    	   rowstr ="";                                     	
                                 }                                 
                                 rowstr=rowstr.trim();                                                                                               
                                 if(i==0){                                	
                                	 if(rowstr==null || rowstr.equalsIgnoreCase("")){                                		                                		
                                		 flgr=true;
                                		 break;
                                	 }else{                                  		
                                		 rowstr = rowstr.trim();                           
                                         json_data.put(rowstr);
                                	 }
                                 }else{                                	
                                	 rowstr = rowstr.trim();  
                                	 Commonutility.toWriteConsole("rowstr : "+rowstr);
                                     json_data.put(rowstr);
                                 }
                                
                             } else {   
                            	 //Society Cell Empty check
                            	 if(i==0){                              		
                                		 flgr=true;
                                		 break;                                	 
                                 }else{
                                	 rowstr = rowstr.trim();                           
                                     json_data.put(rowstr);
                                 }
                             }            
                    	}                     	
                    	if(!flgr){
                    		 saveColumn.put("column",json_data);                       
                             json_data1.put(saveColumn);
                    	}
                    	
                    }                    
                }
                saveRow.put("row", json_data1);
            }else  if (extension.equalsIgnoreCase("xlsx")) {            	
            	Workbook workBook=null;
            	 XSSFSheet xssfSheet=null;
            	 Sheet sheet;
				try {
					workBook = new XSSFWorkbook(file);
					// Read data at sheet 0
				} catch (Exception e) {
					Commonutility.toWriteConsole("read data xlsx fromat==> " + e);
				}
            	sheet = workBook.getSheetAt(0);
            	FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
                DataFormatter formatter = new DataFormatter();
                Iterator rowIteration = sheet.rowIterator();       
                String rowstr = "";
                JSONArray json_data1 =null;
                JSONArray json_data =null;
                JSONObject saveColumn=new JSONObject();
                json_data1= new JSONArray();
                boolean flgr=false;
                // Looping every row at sheet 0
                while (rowIteration.hasNext()) {
                    XSSFRow xssfRow = (XSSFRow) rowIteration.next();
                    Iterator cellIteration = xssfRow.cellIterator();
                    int rownum = xssfRow.getRowNum();
                    if (rownum >= startrow) {
                    	 saveColumn=new JSONObject();
                    	 json_data= new JSONArray();
                    	 int noOfColumns = sheet.getRow(rownum).getPhysicalNumberOfCells();
						for (int i = 0; i < noOfColumns; i++) {
							rowstr = "";
							Cell cell = xssfRow.getCell(i);
							if (cell != null) {          	 
	                            switch (cell.getCellType()) {
	                            case Cell.CELL_TYPE_FORMULA:
	                                rowstr = formatter.formatCellValue(cell, evaluator);
	                                break;
	                            case Cell.CELL_TYPE_NUMERIC:
	                                rowstr = formatter.formatCellValue(cell);
	                                break;
	                            case Cell.CELL_TYPE_STRING:
	                           		 rowstr =cell.getStringCellValue();
	                              break;
	                              default:
	                           	   rowstr =""; 
								}
								rowstr = rowstr.trim();
								if (i == 0) {                      	
                           	 		if(rowstr==null || rowstr.equalsIgnoreCase("")){                              	 			
                           	 			flgr=true;
                           	 			break;
									} else {
										rowstr = rowstr.trim();
										json_data.put(rowstr);
									}
								} else {									
									rowstr = rowstr.trim();									 
									json_data.put(rowstr);
								}
                        } else{   
                       	 //Society Cell Empty check
                       	 if(i==0){                              		
                           		 flgr=true;
                           		 break;                                	 
                            }else{                            	
                            	 rowstr = rowstr.trim();
									json_data.put(rowstr);
                            }
                        } 
                     	}
                        if(!flgr){
                        	saveColumn.put("column",json_data);                       
                            json_data1.put(saveColumn);
                   	}
                     	}

                }
                saveRow.put("row", json_data1);
            }else if(extension.equalsIgnoreCase("csv")){
            	Commonutility.toWriteConsole("CSV FileFormat.....");
            	File arr = new File(xlsname);
                if (arr.exists()) {
                	BufferedReader products = new BufferedReader(new FileReader(arr));
                    String line = "";
                    String rowstr = "";
                    JSONArray json_data1 =null;
                    JSONArray json_data =null;
                    JSONObject saveColumn=new JSONObject();
                    json_data1= new JSONArray();
                    int j = 0;
                    while ((line = products.readLine()) != null) {
                        String fields = "";
                        String[] dataval = line.split(",");                       
                        saveColumn=new JSONObject();
                   	 json_data= new JSONArray();
                        for (int i = 0; i <= 5; i++) {                                                        
                            String cellval = dataval[i];
                            if (!cellval.equalsIgnoreCase("")) {
                                fields = "" + dataval[i] + "";
                            }
                            else
                            {
                            	 fields = "";
                            }
                            fields =fields.trim();
                            
                            json_data.put(fields);
                        }
                        saveColumn.put("column",json_data);                        
                        json_data1.put(saveColumn);
                        j++;
                    }
                    saveRow.put("row", json_data1);
                    products.close();
                }
            }            
            file.close();
            return saveRow.toString();
        } catch (Exception ex) {
        	ex.printStackTrace();
        	Commonutility.toWriteConsole("Exception xlsx reading---> "+ex);
            log.write.error("ex == " + ex);
            return "";
		} finally {

		}

	}
	
	 private static int countOccurrences(String srcStr, char findchar) {
	        int count = 0;
	        for (int i = 0; i < srcStr.length(); i++) {
	            if (srcStr.charAt(i) == findchar) {
	                count++;
	            }
	        }
	        return count;
	    }
	
}