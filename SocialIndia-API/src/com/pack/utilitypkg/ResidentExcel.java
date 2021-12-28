package com.pack.utilitypkg;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.pack.bitly.GetBitlyLink;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.resident.ResidentUtility;
import com.pack.resident.persistance.ResidentDao;
import com.pack.resident.persistance.ResidentDaoservice;
import com.pack.residentvo.UsrUpldfrmExceFailedTbl;
import com.pack.residentvo.UsrUpldfrmExceTbl;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.SocietyWingsDetailTbl;
import com.siservices.uam.persistense.GroupMasterTblVo;
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

public class ResidentExcel{

	private String ivrUpload=null;
	private String ivrBackfldr=null;
	private String ivrGrpName=null;
	private String ivrUploadFilePath=null;
	private int ivrCurnUsrid=0;
	private String ivrExtnce=null;
	private int ivrFileid=0;

	public ResidentExcel(){
		System.out.println(" default ResidentExcel (): [ResidentExcel] Thread.");
	}

	public ResidentExcel(String uploadFldrname, String pBkupfladrName, String pgrpName, String pUpldFilepath,int pCurntUsrid, String pFileExtnce,int pFileid){
		Commonutility.toWriteConsole("ResidentExcel (): [ResidentExcel] Thread.");
		ivrUpload=uploadFldrname;
		ivrBackfldr=pBkupfladrName;
		ivrGrpName = pgrpName;
		ivrUploadFilePath=pUpldFilepath;
		ivrCurnUsrid=pCurntUsrid;
		ivrExtnce = pFileExtnce;
		ivrFileid=pFileid;
	}

	public void run() {
		System.out.println("Step 1 : [ResidentExcel] Thread Started.");
		String rsss=toInsertResident(ivrGrpName,ivrUploadFilePath,ivrCurnUsrid,1,6,ivrExtnce,ivrFileid);
		if(rsss!=null && rsss.equalsIgnoreCase("done")){	
			System.out.println("Step 3 : [ResidentExcel] Thread Stoped.");
		}

	}

	public String toInsertResident(String pGrpName,String locuplfilepath,int ivrEntryByusrid, int startrow, int columnlength, String pFileXtnce,int pFileid){
		Log log=null;
		String file_name=null, locvrFnrst=null, password=null, locSlqry=null, locSocirtyIdSlQry=null, locBlockNameSlQry=null;
		Query locSocietyidQryrst=null,locQryrst=null,locBlockNameQryrst=null;
		Session locSession=null;
		SocietyMstTbl locSctyIdObj=null, locSctyMstrvo=null;
		SocietyWingsDetailTbl locBlockNameObj=null;
		UserMasterTblVo locObjRestprfvo=null,locUSRMstrvo=null,locUSRFltrvo=null;
		UsrUpldfrmExceFailedTbl locObjUplErrvo=null;
		UsrUpldfrmExceTbl locObjUplFpathvo=null;
		CommonUtils locCommutillObj =null;
		GroupMasterTblVo locGrpMstrVOobj=null,locGRPMstrvo=null;
		FlatMasterTblVO flatMstvo = null;
		ResidentDao locrestObj=null, locUplErrObj=null,locFlatinst = null;;
		JSONObject jsnobject =null;
		JSONArray jsonArray =null;
		String society = null;
		SmsTemplatepojo smsTemplate;
		SmsEngineServices smsService = new SmsEngineDaoServices();
		ResourceBundle rb = null;
		profileDao profile= null;
		try {
			rb = ResourceBundle.getBundle("applicationResources");
			log = new Log();
			locSession=HibernateUtil.getSession();
			locCommutillObj = new CommonUtilsDao();
			locObjRestprfvo = new UserMasterTblVo();
			locObjUplErrvo = new UsrUpldfrmExceFailedTbl();
			locObjUplFpathvo = new UsrUpldfrmExceTbl();
			flatMstvo = new FlatMasterTblVO();
			System.out.println("Step 2 : [ResidentExcel] toInsertResident Block Enter. ResidentUtility.readExcel() Start");
			locvrFnrst = ResidentUtility.readExcel(locuplfilepath, ivrEntryByusrid,startrow,columnlength,pFileXtnce);//resident xlsfilename passing
			System.out.println("Step 3 : [ResidentExcel] toInsertResident ResidentUtility.readExcel() End." + locvrFnrst);
			file_name=new File(locuplfilepath).getName();
			String lvrGplyurl = rb.getString("gply.storeurl");
			String pAccesstkn = rb.getString("bitly.accesstocken");
			String btlySrvurl = rb.getString("bitly.server.url");

			String lvrBitlylnk = GetBitlyLink.toGetBitlyLinkMthd(lvrGplyurl, "yes", pAccesstkn, btlySrvurl);
			if((locvrFnrst!=null && !locvrFnrst.equalsIgnoreCase("null") && !locvrFnrst.equalsIgnoreCase(""))) {
				jsnobject = new JSONObject(locvrFnrst);
				jsonArray = jsnobject.getJSONArray("row");
				int locUsrid=0,locUplErrid=0;
				String locerr_id="";
				boolean flg = true;
				log.logMessage("ResidentExcel Thread via insert will start.", "info", ResidentExcel.class);
				//i value of row
				//j value of column
				System.out.println("jsonArray====="+jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						locUsrid=0;
						JSONObject explrObject = jsonArray.getJSONObject(i);
						JSONArray jsonColumnArray = explrObject.getJSONArray("column");
						System.out.println("jsonColumnArray====="+jsonColumnArray.length());
						String blkname = null,flatno = null;
						for (int j = 0; j < jsonColumnArray.length();j++) {
							society=null;
							society = jsonColumnArray.getString(0);
							locSocirtyIdSlQry=	"from SocietyMstTbl where upper(activationKey) = upper('"+society+"') or activationKey=upper('"+society+"')";
							locSocietyidQryrst=locSession.createQuery(locSocirtyIdSlQry);
							locSctyIdObj=(SocietyMstTbl) locSocietyidQryrst.uniqueResult();
							if(locSctyIdObj!=null){
								locSctyMstrvo=new SocietyMstTbl();
								locSctyMstrvo.setSocietyId(locSctyIdObj.getSocietyId());
								locObjRestprfvo.setSocietyId(locSctyMstrvo);

							}else{
								locObjRestprfvo.setSocietyId(null);
							}
							password=locCommutillObj.getRandomval("AZ_09", 10);
							//								password="1234567890";
							locObjRestprfvo.setPassword(locCommutillObj.stringToMD5(password));
							locObjRestprfvo.setEncryprPassword(EncDecrypt.encrypt(password));
							//locObjRestprfvo.setPassword(password);
							System.out.println(jsonColumnArray.get(1));
							locObjRestprfvo.setIsdCode(jsonColumnArray.getString(1));
							System.out.println(jsonColumnArray.get(2) );
							locObjRestprfvo.setMobileNo(jsonColumnArray.getString(2));
							System.out.println(jsonColumnArray.get(3));
							locObjRestprfvo.setEmailId(jsonColumnArray.getString(3)); 
							System.out.println(jsonColumnArray.get(4));
							locObjRestprfvo.setFirstName(jsonColumnArray.getString(4));
							System.out.println(jsonColumnArray.get(5));
							locObjRestprfvo.setLastName(jsonColumnArray.getString(5));

							if(jsonColumnArray.length() >= 8){
								System.out.println(jsonColumnArray.get(7));
								blkname = jsonColumnArray.getString(7);
							}

							if(jsonColumnArray.length() >= 9){
								System.out.println(jsonColumnArray.get(8));
								flatno = jsonColumnArray.getString(8);
							}

							if(jsonColumnArray.length() >= 7){
								System.out.println(jsonColumnArray.get(6));
								locObjRestprfvo.setDob(jsonColumnArray.getString(6));
							}

							if(jsonColumnArray.length() >= 10){
								System.out.println(jsonColumnArray.get(9));
								locObjRestprfvo.setAddress1(jsonColumnArray.getString(9));
							}

							if(jsonColumnArray.length() >= 11){
								System.out.println(jsonColumnArray.get(10));
								locObjRestprfvo.setAddress2(jsonColumnArray.getString(10));
							}

							if(jsonColumnArray.length() >= 12){
								System.out.println(jsonColumnArray.get(11));
								locObjRestprfvo.setOccupation(jsonColumnArray.getString(11));
							}

							if(jsonColumnArray.length() >= 13){
								System.out.println(jsonColumnArray.get(12));
								locObjRestprfvo.setBloodType(jsonColumnArray.getString(12));
							}

							if(jsonColumnArray.length() >= 14){
								System.out.println("Gender : "+jsonColumnArray.get(13));
								locObjRestprfvo.setGender(jsonColumnArray.getString(13));
							}

							if(jsonColumnArray.length() >= 15){
								System.out.println("Members in family : "+jsonColumnArray.get(14));
								int value = (jsonColumnArray.getString(14) != null) && !(jsonColumnArray.getString(14).equals("")) ? jsonColumnArray.getInt(14) : 0;
								System.out.println("Members in family : "+value);
								locObjRestprfvo.setMembersInFamily(value);
							}
							
							
							String lvrGrpunqid = jsonColumnArray.getString(2) + locCommutillObj.getRandomval("AZ_09", 10);
							locObjRestprfvo.setGroupUniqId(lvrGrpunqid);
							// Select Group Code on REsident	
							locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";
							locQryrst=locSession.createQuery(locSlqry);
							locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
							if(locGrpMstrVOobj!=null){
								locGRPMstrvo=new GroupMasterTblVo();
								locGRPMstrvo.setGroupCode(locGrpMstrVOobj.getGroupCode());
								locObjRestprfvo.setGroupCode(locGRPMstrvo);
							}else{// new group create
								uamDao uam=new uamDaoServices();
								int locGrpcode=uam.createnewgroup_rtnId(pGrpName);
								if(locGrpcode!=-1 && locGrpcode!=0){ 
									locGRPMstrvo=new GroupMasterTblVo();
									locGRPMstrvo.setGroupCode(locGrpcode);
									locObjRestprfvo.setGroupCode(locGRPMstrvo);
								}else{
									locObjRestprfvo.setGroupCode(null);
								}				 				
							}	 

						}
						locObjRestprfvo.setAccessChannel(2);
						locObjRestprfvo.setStatusFlag(Integer.parseInt("0"));
						locObjRestprfvo.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
						//----------- REsident reg Start-----------							
						locrestObj = new ResidentDaoservice();

						System.out.println("ResidentDaoservice getSocietyId ====="+locObjRestprfvo.getSocietyId());

						locUsrid = locrestObj.saveResidentInfo_int(locObjRestprfvo,null,null,null,null,null,null);
						System.out.println("locUsrid====="+locUsrid);
						System.out.println("jsonColumnAr====="+jsonColumnArray.length());
						if(locSctyIdObj!=null && locUsrid > 0 && jsonColumnArray.length() >= 8) {
							System.out.println("blkname====="+blkname);
							if(blkname != null && !blkname.equalsIgnoreCase("") && !blkname.equalsIgnoreCase("null")){
								profile=new profileDaoServices();
								locBlockNameSlQry=	"SELECT count(societyId) from SocietyWingsDetailTbl where societyId ='"+locSctyIdObj.getSocietyId()+"' and wingsName ='"+blkname+"'";
								int count = profile.getInitTotal(locBlockNameSlQry);
								System.out.println("count====="+count);
								if(count > 0){
									System.out.println("flatno====="+flatno);
									if(jsonColumnArray.length() >= 9){
										if(flatno != null && !flatno.equalsIgnoreCase("") && !flatno.equalsIgnoreCase("null")) {
											flatMstvo.setFlatno(flatno);
											flatMstvo.setWingsname(blkname);
											locUSRFltrvo=new UserMasterTblVo();
											locUSRFltrvo.setUserId(locUsrid);
											flatMstvo.setUsrid(locUSRFltrvo);
											flatMstvo.setEntryby(ivrEntryByusrid);
											flatMstvo.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
											flatMstvo.setStatus(1);
											flatMstvo.setIvrBnismyself(1);
											locFlatinst = new ResidentDaoservice();
											int locflatrst = locFlatinst.saveResidentFlatInfo_intRtn(flatMstvo);
											System.out.println("locflatrst====="+locflatrst);
										}
									}
								}
							}
						}
						boolean lvrRsdemsms = true; // not need to send
						boolean lvrRsdememail = true; // not need to send
						if (lvrRsdememail){	

							if (locObjRestprfvo.getEmailId()!=null && Commonutility.checkempty(locObjRestprfvo.getEmailId())){
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
									System.out.println("Excption found in Emailsend ResidentUtility.class : " + ex);
									log.logMessage("Exception in ResidentUtility create resident flow emailFlow----> " + ex, "error",ResidentUtility.class);            
								}

							}
						}
						if (lvrRsdemsms) {
							// Sending SMS
							System.out.println("===================sms===========================");
							SmsInpojo sms = null;
							try {
								sms = new SmsInpojo();
								String mailRandamNumber;
								mailRandamNumber = locCommutillObj.randInt(5555, 999999999);
								String qry = "FROM SmsTemplatepojo where " + "templateName ='Create Resident' and status ='1'";
								smsTemplate = smsService.smsTemplateData(qry);
								String smsMessage = smsTemplate.getTemplateContent();

								smsMessage = smsMessage.replace("[APPBITLYLINK]", lvrBitlylnk);
								qry = locCommutillObj.smsTemplateParser(smsMessage, 1, "", password);
								String[] qrySplit = qry.split("!_!");
								String qryform = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.mobileNo='" + locObjRestprfvo.getMobileNo() + "'";
								smsMessage = smsService.smsTemplateParserChange(qryform, Integer.parseInt(qrySplit[1]), smsMessage);
								sms.setSmsCardNo(mailRandamNumber);
								sms.setSmsEntryDateTime(locCommutillObj.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
								sms.setSmsMobNumber(locObjRestprfvo.getMobileNo());
								sms.setSmspollFlag("F");
								sms.setSmsMessage(smsMessage);
								smsService.insertSmsInTable(sms);
							} catch (Exception ex) {
								System.out.println("Excption found in smssend ResidentUtility.class : " + ex);
								log.logMessage("Exception in signup admin flow ResidentUtility----> " + ex, "error",ResidentUtility.class);

							}
						}	

						if (locUsrid<=0){
							locerr_id+=i+", ";
							flg = false;
							//  append error id								
						} else {

						}
						if (Commonutility.checkempty(locerr_id) && locerr_id.endsWith(", ")) {
							locerr_id = locerr_id.substring(0, locerr_id.length()-2);
						}
						//----------- REsident reg End-----------			
					}catch(Exception ex){	
						System.out.println("Exception Found in ResidentExcel toInsertResident() : "+ex);
						//insert into error report table
						//locrestObj=new ResidentDaoservice();
						//locUsrid=locrestObj.saveResidentInfo_int(locObjRestprfvo);
					}finally{			
					}
				}

				if (!flg) {
					//insert into error report table
					locUplErrObj=new ResidentDaoservice();
					locUSRMstrvo=new UserMasterTblVo();
					locUSRMstrvo.setUserId(ivrEntryByusrid);
					locObjUplErrvo.setUsrId(locUSRMstrvo);
					locObjUplFpathvo.setFileId(pFileid);
					locObjUplErrvo.setFileId(locObjUplFpathvo);
					locObjUplErrvo.setExcelErrRowId(locerr_id);
					locObjUplErrvo.setStatus(1);
					locObjUplErrvo.setEnrtyBy(ivrEntryByusrid);
					locObjUplErrvo.setEntryDate(locCommutillObj.getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));;
					locUplErrid=locUplErrObj.saUplErr_Row(locObjUplErrvo);
				}
				String locupFldrPath=ivrUpload,locuplbackupPath=ivrBackfldr;
				//String srcpath=locupFldrPath+ivrEntryByusrid+"/";
				String srcpath=locupFldrPath;
				String distnationpath=locuplbackupPath+ivrEntryByusrid+"/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"/";
				String delrs= Commonutility.toFileMoving(srcpath, distnationpath, file_name, "Remove");
			}else{
				System.out.println("[Empty File Uploaded]");
				log.logMessage("Empty File Uploaded", "info", ResidentExcel.class);
			}
			return "done";
		} catch (Exception ee) {
			System.out.println("Step -1 : Exception Found in ResidentExcel : "+ee);
			log.logMessage("Exception : "+ee, "error", ResidentExcel.class);
			return "done";

		} finally {
			if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;}
			log=null;
			file_name=null;
			locvrFnrst=null;password=null;locSlqry=null;
			locSocirtyIdSlQry=null;locBlockNameSlQry=null;
			locSocietyidQryrst=null;locQryrst=null;locBlockNameQryrst=null;
			locSession=null;
			locSctyIdObj=null; locSctyMstrvo=null;locBlockNameObj=null;locFlatinst=null;profile=null;
			locObjRestprfvo=null;locCommutillObj =null;
			locGrpMstrVOobj=null;locGRPMstrvo=null;locrestObj=null;locUplErrObj=null;jsnobject=null;jsonArray=null;
		}		
	}

}
