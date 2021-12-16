package com.pack.tender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.audittrial.AuditTrial;
import com.pack.event.Eventutility;
import com.pack.tenderVO.TenderDocTblVO;
import com.pack.tenderVO.TenderTblVO;
import com.pack.tenderVO.persistance.TenderDao;
import com.pack.tenderVO.persistance.TenderDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.persistence.DocumentUtilitiHibernateDao;

public class Tenderutility {
	/*
	 * to insert into Tender table.
	 */

	static String isvrClientfrmt = "dd-MM-yyyy hh:ss a";
	static String isvrClientfrmt_sub = "dd-MM-yyyy hh:ssa";
	static SimpleDateFormat locSmftclinetfrmt = new SimpleDateFormat("dd-MM-yyyy hh:ss a");// Client Select Date Format	
	public static String toInsertTender(JSONObject prDatajson,String pGrpName, String prAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath,Log log){
    String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
	String lvrTendertit=null, lvrTenderdate=null, lvrTenderdesc=null,lvrTenderfilename=null,lvrTenderfiledata=null,lvrTenderVdpath=null;
	String lvrCrntSocietyid=null,lvrTenderenddate=null, lvrTenderlocation=null, lvrTenderSttime=null, lvrTenderEndtime=null, lvrTenderSts=null,desc="You Have Received A New Tender Notification";
	String locSlqry=null;
	Query locQryrst=null;
	int usrid = 0;
	int sharid = 0; 
	DocumentUtilitiHibernateDao docHibernateUtilService = null;
	UserMasterTblVo userDocObj =new UserMasterTblVo();
	SocietyMstTbl societyObj =new SocietyMstTbl();
	TenderTblVO locTendervoObj=null;		
	CommonUtils locCommutillObj =null;
	TenderDao loctenderDaoObj = null;
	int locTenderid=0;
	Common lvrCommdaosrobj = null;	
	Session locSession=null;	
	GroupMasterTblVo locGrpmstvoobj=null;
	TenderDao locTenderdaoObj =null;
	UserMasterTblVo locUsrMstrVOobj=null;
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	Iterator locObjSocietyRestlst=null;
		try {
			docHibernateUtilService=new DocumentUtilitiHibernateDao();
			locSession=HibernateUtil.getSession();			
			locCommutillObj = new CommonUtilsDao();
			lvrCommdaosrobj = new CommonDao();
			log.logMessage("Step 2 : Tender Insert Block.", "info", Tenderutility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrCrntSocietyid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrsocietyid");
			lvrTendertit = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tendertitle");
			lvrTenderdate = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tenderdate");
			lvrTenderdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tenderdesc");
			//JSONArray imageDetail=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imageDetail");
			JSONArray lvrimgSrchpthjsnary=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imgsrchpath");
			JSONArray fileName=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "fileName");
			lvrTenderSts="1";			
			locTendervoObj=new TenderTblVO();	
			locTendervoObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			UserMasterTblVo userObj = new UserMasterTblVo();
			GroupMasterTblVo groupObj = new GroupMasterTblVo();
			if(lvrCrntSocietyid.equalsIgnoreCase("-1"))
			{
				lvrCrntSocietyid=null;
			}
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locTendervoObj.setUsrRegTbl(null);
				locTendervoObj.setEntryBy(null);
			}else{
				userObj.setUserId(Integer.parseInt(lvrCrntusrid));
				locTendervoObj.setUsrRegTbl(userObj);
				locTendervoObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
			}			
			if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
				locTendervoObj.setGroupMstTbl(null);
			}else{
				groupObj.setGroupCode(Integer.parseInt(lvrCrntgrpid));
				locTendervoObj.setGroupMstTbl(groupObj);
			}
			
			locTendervoObj.setTenderName(lvrTendertit);
			locTendervoObj.setTenderDetails(lvrTenderdesc);
			locTendervoObj.setTenderDate(lvrTenderdate);
			locTendervoObj.setIvrBnSTATUS(1);
			//----------- Tender Insert Start-----------			
			log.logMessage("Step 3 : Tender Detail insert will start.", "info", Tenderutility.class);
			loctenderDaoObj=new TenderDaoservice();
			locTenderid = loctenderDaoObj.toInsertTender(locTendervoObj);
			System.out.println(locTenderid+": id Tender");
			log.logMessage("Step 4 : Tender Detail insert End Tender Id : "+locTenderid, "info", Tenderutility.class);
			// -----------Tender Insert end------------		
			if (locTenderid>0) {				
				 try {				
					 log.logMessage("Step 5 : Tender Image Write will start.", "info", Tenderutility.class);
					 TenderDocTblVO iocTenderdocObj =null;
					 locTenderdaoObj = new TenderDaoservice();
					 boolean tenderflg=false;
					//image write into folder
					 for (int i = 0; i < fileName.length(); i++) {
						 if(lvrimgSrchpthjsnary!=null  && fileName!=null){
							tenderflg = true;
							lvrTenderfiledata = (String) lvrimgSrchpthjsnary.getString(i);
							lvrTenderfilename = (String) fileName.getString(i);
							if(Commonutility.checkempty(lvrTenderfilename) && Commonutility.checkempty(lvrTenderfiledata)){
								 lvrTenderfilename = lvrTenderfilename.replaceAll(" ", "_");
								
								 Commonutility.toWriteImageMobWebNewUtill(locTenderid, lvrTenderfilename,lvrTenderfiledata,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Tenderutility.class);									
									/*
									byte imgbytee[] = new byte[1024];
									imgbytee = Commonutility.toDecodeB64StrtoByary(lvrTenderfiledata);
									String wrtsts = Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+locTenderid+"/", lvrTenderfilename);						 
									//mobile - small image
									String limgSourcePath=pWebImagpath+locTenderid+"/"+lvrTenderfilename;			
							 		String limgDisPath = pMobImgpath+locTenderid+"/";
							 		String limgName = FilenameUtils.getBaseName(lvrTenderfilename);
							 		String limageFomat = FilenameUtils.getExtension(lvrTenderfilename);		 	    			 	    	 
							 	    String limageFor = "all";
							 	    int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
					        		int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
							        ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
							        */
								 	String limageFomat = FilenameUtils.getExtension(lvrTenderfilename);	
							        iocTenderdocObj=new TenderDocTblVO();
							        userDocObj.setUserId(Integer.parseInt(lvrCrntusrid));
									iocTenderdocObj.setUsrRegTbl(userDocObj);
									locTendervoObj.setTenderId(locTenderid);
									iocTenderdocObj.setMvpTenderTbl(locTendervoObj);
									if (!Commonutility.toCheckisNumeric(lvrCrntSocietyid)) {
										iocTenderdocObj.setSocietyMstTbl(null);
									} else {
										societyObj.setSocietyId(Integer.parseInt(lvrCrntSocietyid));
										iocTenderdocObj.setSocietyMstTbl(societyObj);
									}									 
									 iocTenderdocObj.setDocumentName(lvrTenderfilename);
									 iocTenderdocObj.setDocumentType(limageFomat);							 
									 iocTenderdocObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
									 iocTenderdocObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
									 iocTenderdocObj.setIvrBnSTATUS(1);
									int locdocinsrtrst = locTenderdaoObj.saveTenderDoc_intRtn(iocTenderdocObj);
									System.out.println("return tenderdoc_id::: "+locdocinsrtrst);
							}
																											
							iocTenderdocObj=null;
						 }else{
							 
						 }
					 }
					
					 if(tenderflg){// Audit trial use only TenderDocumeent update
						 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
							 AuditTrial.toWriteAudit(prAuditMsg, pAuditCode, Integer.parseInt(lvrCrntusrid));
						 }else{
							 AuditTrial.toWriteAudit(prAuditMsg, pAuditCode, 1);
						 }				 
					 }
					
					 log.logMessage("Step 6 : Tender Image Write will end.", "info", Tenderutility.class);
				 }catch(Exception imge){
					 System.out.println("Exception in  image write on tender insert : "+imge);
					 log.logMessage("step -2 : Exception in Image write on tender insert", "info", Tenderutility.class);
				 }finally {}	
				 
				
				 log.logMessage("Tender share [invite] table insert will end.", "info", Tenderutility.class);
				 locFtrnStr="success!_!"+locTenderid;
				 int tblflgg=0;
				 if(Commonutility.checkempty(rb.getString("notification.reflg.tender"))){
						 tblflgg=Commonutility.stringToInteger(rb.getString("notification.reflg.tender"));
					} else {
						tblflgg=6;
					}
				 
				 locSlqry = "from UserMasterTblVo where userId = "+Integer.parseInt(lvrCrntusrid);
				 locQryrst = locSession.createQuery(locSlqry);				 
				 locUsrMstrVOobj = (UserMasterTblVo)locQryrst.uniqueResult();
				 if(locUsrMstrVOobj.getSocietyId()!=null){
					 String cmplttosocietyid = Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getSocietyId().getSocietyId());
					 String grpcode = Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getGroupCode().getGroupCode());					 
					 if(Commonutility.checkempty(cmplttosocietyid) && Commonutility.toCheckisNumeric(cmplttosocietyid)){
						 String locSlqry1 = "from UserMasterTblVo where societyId = "+Integer.parseInt(cmplttosocietyid)+" and userId <> "+Integer.parseInt(lvrCrntusrid) +" and statusFlag =1  and groupCode= "+grpcode+"";
						 locObjSocietyRestlst=locSession.createQuery(locSlqry1).list().iterator();
						 while(locObjSocietyRestlst.hasNext()) {						
							locUsrMstrVOobj=(UserMasterTblVo)locObjSocietyRestlst.next();
							String usridforsoctid=Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getUserId());
							docHibernateUtilService.insertNotificationTblByValue(Integer.parseInt(usridforsoctid), rb.getString("notification.tender.raise"),Integer.parseInt(lvrCrntusrid),tblflgg, locTenderid);//cmplttoid(committee),desc,cmpltfrmid,tblflag(0-cmplttable),tblrowid)
						 }
					 }	 
					 
				 }
				 			 				 			
			}else{
				locFtrnStr="error!_!"+locTenderid;
			}
			 return locFtrnStr;
			 		
		}catch(Exception e){
			System.out.println("Exception found in Tenderutility.toInsrttender : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Tenderutility.class);
			locTenderid=0;
			locFtrnStr="error!_!"+locTenderid;
			return locFtrnStr;
		}finally{
			if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;}
			locFtrnStr=null;lvrCrntusrid=null; lvrCrntgrpid=null;
			lvrTendertit=null;lvrTenderdate=null; lvrTenderdesc=null;lvrTenderfilename=null;lvrTenderfiledata=null;lvrTenderVdpath=null;
			lvrCrntSocietyid=null;lvrTenderenddate=null; lvrTenderSttime=null; lvrTenderEndtime=null; lvrTenderSts=null;
			locTendervoObj=null;locCommutillObj =null;loctenderDaoObj = null;
			locTenderid=0;locGrpmstvoobj=null;locTenderdaoObj=null;
			desc=null;	
			usrid = 0;
			sharid = 0; docHibernateUtilService = null;
		}
	}
	
	public static String toUpdateTender(JSONObject pDataJson,String pGrpName, String pAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath){
		String locFtrnStr=null;
		String lvrCrntusrid=null, lvrCrntgrpid=null, lvrTenderid=null;
		String lvrTendertit=null,lvrTenderdate=null, lvrTenderdesc=null,lvrTenderfilename=null,lvrTenderfiledata=null,lvrTenderVdpath=null;
		String lvrCrntSocietyid=null,lvrTenderenddate=null, lvrTenderSttime=null, lvrTenderEndtime=null, lvrTenderlocation = null, lvrTenderSts=null;
		Log log=null;
		String locUpdQry=null;
		TenderDao loctenderDaoObj=null;
		boolean lvrTenderUpdaSts=false;
		
		Session locSession=null;
		String locSlqry=null;
		Query locQryrst=null;
		//GroupMasterTblVo locGrpMstrVOobj=null;
		Common lvrCommdaosrobj = null;
		GroupMasterTblVo locGrpmstvoobj=null;
		CommonUtils locCommutillObj =null;
		TenderTblVO locTendervoObj=null;	
		UserMasterTblVo userDocObj =new UserMasterTblVo();
		SocietyMstTbl societyObj =new SocietyMstTbl();
		JSONArray lvrimgSrchpthjsnary= null;
		//JSONArray imageDetail=null; 
		JSONArray fileName=null;
		 ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try{
			log=new Log();
			lvrCommdaosrobj = new CommonDao();
			locCommutillObj = new CommonUtilsDao();
			log.logMessage("Step 2 : Tender Update Block.", "info", Tenderutility.class);
			System.out.println("Step 2 : Tender Update Block.");
			lvrTenderid=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "tenderid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrgrpid");
			lvrCrntSocietyid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrsocietyid");
			lvrTendertit = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "tendertitle");
			lvrTenderdate = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "tenderdate");
			lvrTenderdesc = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "tenderdesc");
			
			lvrTenderSts="1";			
			locTendervoObj=new TenderTblVO();	
			locTendervoObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			UserMasterTblVo userObj = new UserMasterTblVo();
			GroupMasterTblVo groupObj = new GroupMasterTblVo();
			
			
			locTendervoObj.setTenderName(lvrTendertit);
			locTendervoObj.setTenderDetails(lvrTenderdesc);
			locTendervoObj.setTenderDate(lvrTenderdate);
			locUpdQry ="update TenderTblVO set tenderName = '"+lvrTendertit+"', tenderDetails = '"+lvrTenderdesc+"', tenderDate ='"+lvrTenderdate+"', ";
			if(lvrCrntSocietyid.equalsIgnoreCase("-1"))
			{
				lvrCrntSocietyid=null;
			}
			
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locUpdQry +="usrRegTbl ="+null+", ";
				locUpdQry +="entryBy ="+null+", ";				
			}else{
				
				locUpdQry +="usrRegTbl ="+Integer.parseInt(lvrCrntusrid)+", ";
				locUpdQry +="entryBy ="+Integer.parseInt(lvrCrntusrid)+", ";					
			}			
			if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
				locUpdQry +="groupMstTbl ="+null+", ";
			}else{
				locUpdQry +="groupMstTbl ="+Integer.parseInt(lvrCrntgrpid)+" ";				
			}
			
			locUpdQry +=" where tenderId ="+Integer.parseInt(lvrTenderid)+"";
			log.logMessage("Step 3 : Tender Updqry : "+locUpdQry, "info", Tenderutility.class);
			//--------Tender Update Start-------------
			loctenderDaoObj=new TenderDaoservice();
			lvrTenderUpdaSts=loctenderDaoObj.toUpdateTender(locUpdQry);
			System.out.println("Tender Update Status [lvrTenderUpdaSts] : "+lvrTenderUpdaSts);
			//--------Tender Update End-------------
			 boolean dlrst=loctenderDaoObj.deleteTenderDocdblInfo(Integer.parseInt(lvrTenderid));
			
			 if(Commonutility.toCheckNullEmpty(lvrTenderid)!=null && !Commonutility.toCheckNullEmpty(lvrTenderid).equalsIgnoreCase("null")&& !Commonutility.toCheckNullEmpty(lvrTenderid).equalsIgnoreCase("")){
				 try{				
					 log.logMessage("Step 5 : Tender Image Write will start.", "info", Tenderutility.class);
					 TenderDocTblVO iocTenderdocObj =null;
					 loctenderDaoObj = new TenderDaoservice();
					 boolean tenderflg=false;
					//image write into folder
					// imageDetail=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imageDetail");
					 lvrimgSrchpthjsnary=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgSrchpth");
					 fileName=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fileName");
					 for (int i = 0; i < fileName.length(); i++) {
						 if(lvrimgSrchpthjsnary!=null  && fileName!=null){
							 tenderflg=true;
							  lvrTenderfiledata = (String) lvrimgSrchpthjsnary.getString(i);
							  lvrTenderfilename =(String)fileName.getString(i);
							  Commonutility.toWriteImageMobWebNewUtill(Integer.parseInt(lvrTenderid), lvrTenderfilename,lvrTenderfiledata,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Tenderutility.class);
							  String limageFomat = FilenameUtils.getExtension(lvrTenderfilename);	
							  /*
							  byte imgbytee[]= new byte[1024];
							 imgbytee=Commonutility.toDecodeB64StrtoByary(lvrTenderfiledata);
							 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+lvrTenderid+"/", lvrTenderfilename);						 
							//mobile - small image
							String limgSourcePath=pWebImagpath+lvrTenderid+"/"+lvrTenderfilename;			
					 		String limgDisPath = pMobImgpath+lvrTenderid+"/";
					 		String limgName = FilenameUtils.getBaseName(lvrTenderfilename);
					 		String limageFomat = FilenameUtils.getExtension(lvrTenderfilename);		 	    			 	    	 
					 	    String limageFor = "all";
					 	    int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
			        		int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));		
					        ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
					       */
					        iocTenderdocObj=new TenderDocTblVO();
					        userDocObj.setUserId(Integer.parseInt(lvrCrntusrid));
							 iocTenderdocObj.setUsrRegTbl(userDocObj);
							 locTendervoObj.setTenderId(Integer.parseInt(lvrTenderid));
							 iocTenderdocObj.setMvpTenderTbl(locTendervoObj);
							 if(!Commonutility.toCheckisNumeric(lvrCrntSocietyid)){
								 iocTenderdocObj.setSocietyMstTbl(null);
								}else{
									societyObj.setSocietyId(Integer.parseInt(lvrCrntSocietyid));
									 iocTenderdocObj.setSocietyMstTbl(societyObj);
								}
							 
							 iocTenderdocObj.setDocumentName(lvrTenderfilename);
							 iocTenderdocObj.setDocumentType(limageFomat);
							 
							 iocTenderdocObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
							 iocTenderdocObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							 iocTenderdocObj.setIvrBnSTATUS(1);
							int locdocinsrtrst = loctenderDaoObj.saveTenderDoc_intRtn(iocTenderdocObj);
							System.out.println("return tenderdoc_id::: "+locdocinsrtrst);
							iocTenderdocObj=null;
						 }else{
							 
						 }
					 }
					
					 if(tenderflg){// Audit trial use only TenderDocumeent update
						 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
							 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, Integer.parseInt(lvrCrntusrid));
						 }else{
							 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, 1);
						 }				 
					 }
					
					 log.logMessage("Step 6 : Tender Image Write will end.", "info", Tenderutility.class);
				 }catch(Exception imge){
					 System.out.println("Exception in  image write on tender insert : "+imge);
					 log.logMessage("step -2 : Exception in Image write on tender insert", "info", Tenderutility.class);
				 }finally {}	
				 
				
				 log.logMessage("Tender share [invite] table insert will end.", "info", Tenderutility.class);
				 locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			 return locFtrnStr;
		}catch(Exception e){
			System.out.println("Exception found in Tenderutility.toUpdateTender : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Tenderutility.class);		
			locFtrnStr="error";
			return locFtrnStr;			
		}finally{
			locGrpmstvoobj=null;
			if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;}
			lvrCrntusrid=null; lvrCrntgrpid=null; lvrTenderid=null;
			lvrTendertit=null;lvrTenderdate=null; lvrTenderdesc=null;lvrTenderfilename=null;lvrTenderfiledata=null;lvrTenderVdpath=null;
			lvrCrntSocietyid=null;lvrTenderenddate=null; lvrTenderSttime=null; lvrTenderEndtime=null; lvrTenderSts=null;
			log=null;
			locUpdQry=null;
			loctenderDaoObj=null;
			lvrTenderUpdaSts=false;
			locSlqry=null;locQryrst=null;	rb = null;		
		}
	}

	
	public static String toDeleteTender(JSONObject pDataJson){
		String locFtrnStr = null;
		String lvrTenderid = null;
		boolean lvrTenderUpdaSts = false;
		boolean lvrTenderDispUpdaSts = false;
		Log log=null;
		String locDldQry=null;
		String locDldispQry=null;
		TenderDao loctenderDaoObj=null;
		
		try{
			log=new Log();
			loctenderDaoObj = new TenderDaoservice();
			log.logMessage("Step 2 : Tender Delete block enter.", "info", Tenderutility.class);
			lvrTenderid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "tenderid");			
			locDldispQry= "delete TenderDocTblVO where mvpTenderTbl="+Integer.parseInt(lvrTenderid)+"";
			log.logMessage("Step 3 : Tender Display Tbl Delete qry : "+locDldispQry, "info", Tenderutility.class);
			lvrTenderDispUpdaSts=loctenderDaoObj.toDeleteTenderDispTbl(locDldispQry);
			locDldQry="delete TenderTblVO where tenderId ="+Integer.parseInt(lvrTenderid);
			log.logMessage("Step 4 : Tender Delete qry : "+locDldQry, "info", Tenderutility.class);
			lvrTenderUpdaSts=loctenderDaoObj.toDeleteTender(locDldQry);
			
			if(lvrTenderUpdaSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			log.logMessage("Step 5 : Tender Delete block End.", "info", Tenderutility.class);
			return locFtrnStr;
		}catch(Exception e){
			return locFtrnStr;
		}finally{
			lvrTenderid=null;
			log=null;locDldQry=null;loctenderDaoObj=null;
		}
	}
	
   public static JSONObject toseletTendersingledata(JSONObject pDataJson){ 
    String lvrTenderid = null;
	String lvrSlqry = null,loc_slQry_file=null;
	Query lvrQrybj = null;
	Log log = null;
	Date lvrEntrydate=null;
	TenderTblVO locTendertblobj = null;
	JSONObject locRtndatajson = null;
	Session locHbsession = null;
	Common locCommonObj = null;
	Iterator locObjfilelst_itr=null;
	JSONArray locLBRFILEJSONAryobj=null;
	JSONObject locLBRFILEOBJJSONAryobj=null;
	TenderDocTblVO locfiledbtbl=null;
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	String downloadImagePath="";
	try {
		log = new Log();
		locHbsession = HibernateUtil.getSession();
		locCommonObj = new CommonDao();
		System.out.println("Step 1 : Tender detail block enter");
		log.logMessage("Step 1 : Select Tender detail block enter", "info", Tenderutility.class);
		locRtndatajson =  new JSONObject();
		lvrTenderid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "tenderid");
		lvrSlqry = "from TenderTblVO where tenderId = "+Integer.parseInt(lvrTenderid);
		lvrQrybj = locHbsession.createQuery(lvrSlqry);
		locTendertblobj = (TenderTblVO)lvrQrybj.uniqueResult();
		System.out.println("Step 2 : Select Tender detail query executed.");
		locRtndatajson.put("tenderid",Commonutility.toCheckNullEmpty(locTendertblobj.getTenderId()));
		locRtndatajson.put("tendertitle",Commonutility.toCheckNullEmpty(locTendertblobj.getTenderName()));
		locRtndatajson.put("tenderdate",Commonutility.toCheckNullEmpty(locTendertblobj.getTenderDate()));
		locRtndatajson.put("tenderdesc",Commonutility.toCheckNullEmpty(locTendertblobj.getTenderDetails()));
		locRtndatajson.put("tenderentryby",Commonutility.toCheckNullEmpty(locTendertblobj.getUsrRegTbl().getUserName()));
		if(locTendertblobj.getUsrRegTbl().getSocietyId()!=null)
		{
			locRtndatajson.put("tendersocietyname",Commonutility.toCheckNullEmpty(locTendertblobj.getUsrRegTbl().getSocietyId().getSocietyName()));
			locRtndatajson.put("tendertownshipname",Commonutility.toCheckNullEmpty(locTendertblobj.getUsrRegTbl().getSocietyId().getTownshipId().getTownshipName()));
		}else{
			locRtndatajson.put("tendersocietyname","");
			locRtndatajson.put("tendertownshipname","");
		}
		log.logMessage("Step 2: Select tender files detail block start.", "info",Tenderutility.class);
		loc_slQry_file="from TenderDocTblVO where mvpTenderTbl="+Integer.parseInt(lvrTenderid)+" ";
		locObjfilelst_itr=locHbsession.createQuery(loc_slQry_file).list().iterator();	
		System.out.println("Step 3 : Select tender files detail query executed.");
		locLBRFILEJSONAryobj=new JSONArray();
		while (locObjfilelst_itr!=null &&  locObjfilelst_itr.hasNext() ) {
			locfiledbtbl=(TenderDocTblVO)locObjfilelst_itr.next();
			locLBRFILEOBJJSONAryobj=new JSONObject();
			if(locfiledbtbl.getTenderDocId()!=null){
				locLBRFILEOBJJSONAryobj.put("filesname", locfiledbtbl.getDocumentName());
				locLBRFILEOBJJSONAryobj.put("fileformat", locfiledbtbl.getDocumentType());
				downloadImagePath=rb.getString("external.imagesfldr.path")+rb.getString("external.inner.tenderfldr")+rb.getString("external.inner.webpath")+locTendertblobj.getTenderId()+"/"+locfiledbtbl.getDocumentName();
				locLBRFILEOBJJSONAryobj.put("filepath",downloadImagePath);
			}				
			locLBRFILEJSONAryobj.put(locLBRFILEOBJJSONAryobj);
		}
		
		log.logMessage("Step 3: Select file name and type detail block end.", "info", Tenderutility.class);
		locRtndatajson.put("jArry_doc_files", locLBRFILEJSONAryobj);
		System.out.println("Step 5 : Return JSON DATA : "+locRtndatajson);						
		System.out.println("Step 6 : Select tenderdocment detail block end.");
		log.logMessage("Step 4:  Select tenderdocmentt detail block end.", "info", Tenderutility.class);	
		System.out.println("Step 3 : Tender details are put into json.");
		System.out.println("Step 4 : Tender Block end.");
		return locRtndatajson;
	}catch(Exception e) {
		try{
			System.out.println("Step -1 : Select Tender detail Exception found in Tenderutility.toseletTendersingle : "+e);
			log.logMessage("Exception : "+e, "error", Tenderutility.class);
			locRtndatajson=new JSONObject();
			locRtndatajson.put("catch", "Error");
			}catch(Exception ee){}
			return locRtndatajson;
	}finally {
			if(locHbsession!=null){locHbsession.close();locHbsession=null;}
			 lvrTenderid = null; lvrSlqry = null; lvrQrybj = null;
			 log = null; lvrEntrydate=null; loc_slQry_file=null;locTendertblobj = null; locRtndatajson = null;locLBRFILEJSONAryobj=null;locLBRFILEOBJJSONAryobj=null;
			 
	}
  }

   /* public static String toShareTender(JSONObject pDataJson,String pGrpName){
	    String locFtrnStr = null;
		String lvrTenderid = null,lvrCrntusrid = null;		
		Log log = null;
		String lvrDlQry = null;		
		TenderDao loctenderDaoObj = null;
		// get tender details
		String lvrTenderslqry = null;
		Query locTenderqryrst = null;
		TenderTblVO locTendertblobj = null;
		Common lvrCommdaosrobj = null;	
		String lvrTenderdesc = null, lvrTenderVdpath = null, lvrCrntSocietyid = null, lvrTenderSttime = null, lvrTenderenddate = null, lvrTenderEndtime = null;
		String lvrTendertit = null, lvrTenderdate = null, lvrTenderlocation = null,desc="You Have Received A New Tender Notification";
		int usrid = 0;
		int sharid = 0;
		DocumentUtilitiHibernateDao docHibernateUtilService = null;
		
		// get Group details
		Session locSession = null;
		
		// insert into tender disp table
		UserMasterTblVo locUsobj=null;
		GroupMasterTblVo locGrpmstvoobj=null;		
		TenderDispTblVO lvrEvDsiptblvoobj=null;
		uamDao	lvrUamdaosrv= null;
		try {
			docHibernateUtilService = new DocumentUtilitiHibernateDao();
			log = new Log();boolean shrusrflg=false;
			locSession=HibernateUtil.getSession();
			loctenderDaoObj = new TenderDaoservice();
			lvrCommdaosrobj = new CommonDao();
			lvrUamdaosrv = new uamDaoServices();
			
			log.logMessage("Step 2 : Tender Share block enter.", "info", Tenderutility.class);
			lvrTenderid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "tenderid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");			
			
			JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shareusridJary");// Tender disp user id insert into
			//String usrGrpcode=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shareusrgrpid");//  share user group id									 
			 // set group code	
			 String logrpcode = null;
			 locGrpmstvoobj=new GroupMasterTblVo();						 
			 logrpcode = lvrCommdaosrobj.getGroupcodeexistornew(pGrpName);
			 if(logrpcode!= null && !logrpcode.equalsIgnoreCase("error")){
				 if(Commonutility.toCheckisNumeric(logrpcode)){
					 locGrpmstvoobj.setGroupCode(Integer.parseInt(logrpcode));	
				 }				
			 }else{	
				 locGrpmstvoobj = null;
			 }			
			log.logMessage("Step 3 : Tender share [invite] table insert will start.[toShareTender]", "info", Tenderutility.class);
									
			// Get Tender Details
			lvrTenderslqry = "from TenderTblVO where ivrBnEVENT_ID = "+Integer.parseInt(lvrTenderid);
			locTenderqryrst = locSession.createQuery(lvrTenderslqry);
			locTendertblobj = (TenderTblVO) locTenderqryrst.uniqueResult();
			lvrTenderdesc = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnEVENT_DESC());
			lvrTenderVdpath = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnVIDEO_PATH());
			lvrCrntSocietyid = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnSTARTDATE());
			lvrTenderSttime = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnSTARTTIME());
			lvrTenderenddate = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnENDDATE());
			lvrTenderEndtime = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnENDTIME());
			lvrTendertit = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnEVENT_TITLE());
			lvrTenderdate = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnSHORT_DESC());
			lvrTenderlocation = Commonutility.toCheckNullEmpty(locTendertblobj.getIvrBnEVENT_LOCATION());
			for (int i = 0; i < jry.length(); i++) {
				 String locshrusrid=(String)jry.getString(i);
				 try{
				 if(Commonutility.toCheckisNumeric(locshrusrid)){
					 lvrDlQry ="delete TenderDispTblVO where ivrBnEVENT_ID="+Integer.parseInt(lvrTenderid)+" and ivrBnUAMMSTtbvoobj.userId = "+Integer.parseInt(locshrusrid)+"";
					 loctenderDaoObj.toDeleteTenderDispTbl(lvrDlQry);
					 shrusrflg=true;
					 lvrEvDsiptblvoobj= new TenderDispTblVO();
					 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
						 lvrEvDsiptblvoobj.setIvrBnENTRY_BY(null);
					 }else{
						 lvrEvDsiptblvoobj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
					 }	
					 lvrEvDsiptblvoobj.setIvrBnEVENTSTATUS(1);
					 lvrEvDsiptblvoobj.setIvrBnEVENT_ID(Integer.parseInt(lvrTenderid));
					 
					 locUsobj=new UserMasterTblVo();
					 locUsobj.setUserId(Integer.parseInt(locshrusrid));
					 lvrEvDsiptblvoobj.setIvrBnUAMMSTtbvoobj(locUsobj);
					 
					 if(locGrpmstvoobj!=null){
						 lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(locGrpmstvoobj);
					 }else{
						 lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(null);
					 }					 									
					// lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(locGrpmstvoobj);	
					 loctenderDaoObj.toInsertTenderDispTbl(lvrEvDsiptblvoobj);
							
					 usrid=Integer.parseInt(lvrCrntusrid);
					 sharid=Integer.parseInt(locshrusrid);											
					 docHibernateUtilService.insertNotificationTblByValue(sharid, desc,usrid );
					 
					// Outlook Calendar Invite --- Start
					 locUsobj = lvrUamdaosrv.getregistertable(Integer.parseInt(locshrusrid));						 
					 if (locUsobj.getEmailId()!=null && !locUsobj.getEmailId().equalsIgnoreCase("null") && !locUsobj.getEmailId().equalsIgnoreCase("")){
						String locDesc=lvrTenderdesc;
						if (lvrTenderVdpath!=null && !lvrTenderVdpath.equalsIgnoreCase("null") && !lvrTenderVdpath.equalsIgnoreCase("")) {
							locDesc += "Video : "+lvrTenderVdpath;
						}
						String pCategeory="Tender Invite";
						String lvrLocation = lvrTenderlocation;
						Date evntStdate= null;
						Date evntEnddate= null;
						if (lvrCrntSocietyid!=null && !lvrCrntSocietyid.equalsIgnoreCase("") && !lvrCrntSocietyid.equalsIgnoreCase("null")) {
							
							if(lvrTenderSttime!=null && !lvrTenderSttime.equalsIgnoreCase("") && !lvrTenderSttime.equalsIgnoreCase("null")) {
								Date locstDt= Commonutility.toString2UtilDate(lvrTenderenddate + " " + lvrTenderEndtime,isvrClientfrmt);
								if(locstDt==null){
									locstDt= Commonutility.toString2UtilDate(lvrTenderenddate + " " + lvrTenderEndtime,isvrClientfrmt_sub);
								}								
								evntStdate = locstDt;
								//evntStdate = locSmftclinetfrmt.parse(lvrCrntSocietyid +" "+ lvrTenderSttime);								
							}else {
								evntStdate = locSmftclinetfrmt.parse(lvrCrntSocietyid);	
							}
														
						}else {
							 evntStdate = new Date();
							 Calendar cal = Calendar.getInstance();
							 cal.setTime(evntStdate);
			                 cal.add(Calendar.MINUTE, 10);
			                 evntStdate = cal.getTime();
						}
						
						if(lvrTenderenddate!=null && !lvrTenderenddate.equalsIgnoreCase("") && !lvrTenderenddate.equalsIgnoreCase("null")) {
							
							if (lvrTenderEndtime!=null && !lvrTenderEndtime.equalsIgnoreCase("") && !lvrTenderEndtime.equalsIgnoreCase("null")) {
								Date loctestEnddt= Commonutility.toString2UtilDate(lvrTenderenddate + " " + lvrTenderEndtime,"dd-MM-yyyy hh:mm a");
								if(loctestEnddt==null){
									loctestEnddt= Commonutility.toString2UtilDate(lvrTenderenddate + " " + lvrTenderEndtime,"dd-MM-yyyy hh:mma");
								}
								//evntEnddate = locSmftclinetfrmt.parse(lvrTenderenddate + " " + lvrTenderEndtime);
								evntEnddate = loctestEnddt;
							}else {
								evntEnddate = locSmftclinetfrmt.parse(lvrTenderenddate);
							}
							
						}else{
							 evntEnddate = new Date();
							 Calendar cal = Calendar.getInstance();
							 cal.setTime(evntStdate);
							 cal.add(Calendar.DAY_OF_MONTH, 1);
			                 cal.add(Calendar.MINUTE, 10);
			                 evntEnddate = cal.getTime();
						}
						    String lcStdate = lvrCommdaosrobj.getDateobjtoFomatDateStr(evntStdate, "dd-MM-yyyy hh:mm a");
						    String lcEnddate = lvrCommdaosrobj.getDateobjtoFomatDateStr(evntEnddate, "dd-MM-yyyy hh:mm a");
						    System.out.println("----------------[Start]");
						    System.out.println("Step 1 : Calendar Invite Start ");
						    System.out.println("User Id : "+locUsobj.getUserId());
						    System.out.println("Email : "+locUsobj.getEmailId());								
							System.out.println("Tender Start Date : "+lcStdate);
							System.out.println("Tender End Date : "+lcEnddate);
										//setOutlookCallicsRemainder(String to,String stdate, String enddate, String usesubject,String optionsubject,String pCategeory, String bdyy, String pSummary, String alrDesc, String locat)
						    OutCalendar.setOutlookCallicsRemainder(locUsobj.getEmailId(), lcStdate, lcEnddate, lvrTendertit, lvrTendertit, pCategeory, locDesc, lvrTenderdate, lvrTenderdate, lvrLocation);
						    System.out.println("Step 2 : Calendar Invite End ");
						    System.out.println("------------------[End]");
					 }
		// Outlook Calendar Invite --- End					 					 					 			
					 lvrEvDsiptblvoobj=null;locUsobj=null;
					 lvrDlQry=null;
				 }
				}catch(Exception ee){
					log.logMessage("Exception found in Tender Update via share to Member id : "+locshrusrid+", Group id : "+logrpcode +" Exception : "+ee, "error", Tenderutility.class);
				}finally{
					lvrEvDsiptblvoobj=null;locUsobj=null; lvrDlQry=null;
				}				 
			 }
			 log.logMessage("Step 4 : Tender share [invite] table insert will end[toShareTender].", "info", Tenderutility.class);
			 jry=null;//user json array clear
			if(shrusrflg){// Audit trial use only Share user update
				locFtrnStr = "success";		 
			 }else{
				 locFtrnStr = "error";
			 }
			return locFtrnStr;
		}catch(Exception e){
			System.out.println("Exception found in Tenderutility.toShareTender : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Tenderutility.class);		
			locFtrnStr="error";
			return locFtrnStr;
		}finally{
			if(locSession!=null){locSession.close();locSession=null;}
			lvrTenderid = null;
			log = null;lvrDlQry=null;loctenderDaoObj = null;
			locFtrnStr = null; lvrCrntusrid = null;	
			locGrpmstvoobj=null;
			lvrTenderslqry = null;locTenderqryrst = null;locTendertblobj = null;lvrUamdaosrv=null;lvrCommdaosrobj = null;desc=null;	
			usrid=0;
			sharid=0;docHibernateUtilService = null;
		}
  }*/
}
