package com.pack.labor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.laborvo.LaborDetailsTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.laborvo.persistence.LaborDao;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
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
import com.socialindia.generalmgnt.staffcreation;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantIssueTblVO;


public class LaborUtility {
	/*private static File fileUpload;
	private String fileUploadContentType;
	private static String fileUploadFileName;*/
	public static String toInsrtLabor(JSONObject pDataJson, String pGrpName, String pAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath,String iswebmobilefla, String fileUploadFileName, File fileUpload) {	// Insert
		CommonUtils locCommutillObj =null;
		CommonUtilsDao common=new CommonUtilsDao();
		Log log=null; LaborDao locLabrObj=null;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null, locvrID_CARD_TYP = null,locvrLBR_COMPANY;
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_PSTL_ID=null,locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null, locvrLBR_RATING = null, locvrENTRY_BY = null,locvrCITY_NAME=null;
		String locvrLBR_ISD_CODE=null,locvrLBR_VERIFIED_STATUS=null, locvrLBR_KEY_FOR_SEARCH=null, locvrLBR_WORK_TYPE_ID=null, locvrLBR_DESCP=null,locvrLBR_COST=null,locvrLBR_COSTPER=null,locvrLBR_EXP=null;
		int locLbrID=0;
		String locimagename=null, locimg_encdata=null, lvrLbrimgsrcpth = null;
		String locvrLBR_Image_web=null,locvrLBR_Image_Mobile=null;
		//CommonUtils commjvm=new CommonUtils();
		Session locSession=null;
		String locSlqry=null;
		Query locQryrst=null;
		GroupMasterTblVo locGrpMstrVOobj=null,locGRPMstrvo=null;
		MerchantIssueTblVO mechtissuetbl=null;
		 String offerisstxt=null;
		 MerchantManageServices merchanthbm=new MerchantManageDaoServices();
		 Integer companyid=0;
		try {
			
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			locSession=HibernateUtil.getSession();
			locCommutillObj = new CommonUtilsDao();
			log=new Log();
			//locvrENTRY_DATETIME=Commonutility.toGetcurrentutilldatetime("1");
			if(iswebmobilefla != null && !iswebmobilefla.equalsIgnoreCase("null")  && iswebmobilefla.equalsIgnoreCase("1")){
				/*fileUploadFileName="11.jpg";
				fileUpload=new File("D://11.jpg");*/
			}
			 locvrLBR_SERVICE_ID=locCommutillObj.getRandomval("aZ_09", 25);
			 locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "name");
			 locvrLBR_EMAIL = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "emailid");
			 locvrLBR_PH_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "mobno");
			 locvrLBR_COMPANY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "laborcompanyname");
			 System.out.println("locvrLBR_COMPANY---------------1475---------"+locvrLBR_COMPANY);
			 locvrID_CARD_TYP = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cardtyp");
			 locvrID_CARD_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cardno");
			 locvrLBR_ADD_1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add1");
			 locvrLBR_ADD_2 =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add2");
			 locvrLBR_PSTL_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "postalcode");
			 locvrLBR_CITY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "city");
			 locvLBR_STATE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "sate");
			 locvrLBR_COUNTRY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "country");
			 locvLBR_STS = "1";
			 locvrLBR_KYC_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "kycname");
			 locvrLBR_RATING = "0";
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryby");

			 locvrLBR_ISD_CODE=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ISDcode");
			 locvrLBR_VERIFIED_STATUS=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "verifystatus");
			 locvrLBR_KEY_FOR_SEARCH =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "keyforsrch");
			 locvrLBR_WORK_TYPE_ID=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "wktypid");
			 locvrLBR_DESCP=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "descrp");

			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imagename");
			 locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgencdata");
			 locvrLBR_COST=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cost");
			 locvrLBR_COSTPER=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "costper");
			 locvrLBR_EXP=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "experience");
			 locvrCITY_NAME=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cityname");
			 lvrLbrimgsrcpth = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrimgsrchpath");
			 offerisstxt=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "offerisstxt");


			 LaborProfileTblVO locObjLbrprfvo=new LaborProfileTblVO();
			 locObjLbrprfvo.setIvrBnLBR_SERVICE_ID(locvrLBR_SERVICE_ID);
			 locObjLbrprfvo.setIvrBnLBR_NAME(locvrLBR_NAME);
			 if(locvrLBR_COMPANY!=null && !locvrLBR_COMPANY.equalsIgnoreCase("null") && !locvrLBR_COMPANY.equalsIgnoreCase("") && !locvrLBR_COMPANY.equalsIgnoreCase("0")){
				 CompanyMstTblVO companysid=new CompanyMstTblVO();
				 companysid.setCompanyId(Integer.parseInt(locvrLBR_COMPANY));
				 locObjLbrprfvo.setIvrCOMPANY(companysid);
			 } else {
				 locObjLbrprfvo.setIvrCOMPANY(null);
			 }
			 locObjLbrprfvo.setIvrBnLBR_EMAIL(locvrLBR_EMAIL);
			 locObjLbrprfvo.setIvrBnLBR_ISD_CODE(locvrLBR_ISD_CODE);
			 locObjLbrprfvo.setIvrBnLBR_PH_NO(locvrLBR_PH_NO);
			 if(!Commonutility.toCheckisNumeric(locvrID_CARD_TYP)){
				 locObjLbrprfvo.setIvrBnLBR_CITY_ID(null);
			 }else{
				 locObjLbrprfvo.setIvrBnID_CARD_TYP(Integer.parseInt(locvrID_CARD_TYP));
			 }
			 locObjLbrprfvo.setIvrBnID_CARD_NO(locvrID_CARD_NO);
			 locObjLbrprfvo.setIvrBnLBR_ADD_1(locvrLBR_ADD_1);
			 locObjLbrprfvo.setIvrBnLBR_ADD_2(locvrLBR_ADD_2);
			 if(locvrLBR_COST!=null && !locvrLBR_COST.equalsIgnoreCase("null") && !locvrLBR_COST.equalsIgnoreCase("")){
				 locObjLbrprfvo.setIvrBnLBR_COST(Float.parseFloat(locvrLBR_COST));
			 }
			 if(locvrLBR_EXP!=null && !locvrLBR_EXP.equalsIgnoreCase("null") && !locvrLBR_EXP.equalsIgnoreCase("")){
				 locObjLbrprfvo.setIvrBnLBR_EXPERIENCE(Float.parseFloat(locvrLBR_EXP));
			 }
			 if(locvrLBR_COSTPER!=null && !locvrLBR_COSTPER.equalsIgnoreCase("null") && !locvrLBR_COSTPER.equalsIgnoreCase("")){
				 locObjLbrprfvo.setIvrBnLBR_COSTPER(Integer.parseInt(locvrLBR_COSTPER));
			 }

			 if(!Commonutility.toCheckisNumeric(locvrLBR_PSTL_ID)|| locvrLBR_PSTL_ID.equalsIgnoreCase("0")){
				 locObjLbrprfvo.setIvrBnLBR_PSTL_ID(null);
			 }else{
				 locObjLbrprfvo.setIvrBnLBR_PSTL_ID(Integer.parseInt(locvrLBR_PSTL_ID));
			 }

			 if(!Commonutility.toCheckisNumeric(locvrLBR_CITY_ID) || locvrLBR_CITY_ID.equalsIgnoreCase("0")){
				 locObjLbrprfvo.setIvrBnLBR_CITY_ID(null);
			 }else{
				 locObjLbrprfvo.setIvrBnLBR_CITY_ID(Integer.parseInt(locvrLBR_CITY_ID));
			 }

			 if(!Commonutility.toCheckisNumeric(locvLBR_STATE_ID)  || locvLBR_STATE_ID.equalsIgnoreCase("0")){
				 locObjLbrprfvo.setIvrBnLBR_STATE_ID(null);
			 }else{
				 locObjLbrprfvo.setIvrBnLBR_STATE_ID(Integer.parseInt(locvLBR_STATE_ID));
			 }

			 if(!Commonutility.toCheckisNumeric(locvrLBR_COUNTRY_ID)  || locvrLBR_COUNTRY_ID.equalsIgnoreCase("0")){
				 locObjLbrprfvo.setIvrBnLBR_COUNTRY_ID(null);
			 }else{
				 locObjLbrprfvo.setIvrBnLBR_COUNTRY_ID(Integer.parseInt(locvrLBR_COUNTRY_ID));
			 }

			 locObjLbrprfvo.setIvrBnLBR_STS(Integer.parseInt(locvLBR_STS));
			 locObjLbrprfvo.setIvrBnLBR_KYC_NAME(locvrLBR_KYC_NAME);

			 if(!Commonutility.toCheckisNumeric(locvrLBR_RATING) || locvrLBR_RATING.equalsIgnoreCase("0") ){
				 locObjLbrprfvo.setIvrBnLBR_RATING(0.0);
			 }else{
				 locObjLbrprfvo.setIvrBnLBR_RATING(Double.parseDouble(locvrLBR_RATING));
			 }

			 if(!Commonutility.toCheckisNumeric(locvrLBR_VERIFIED_STATUS) || locvrLBR_VERIFIED_STATUS.equalsIgnoreCase("0")){
				 locObjLbrprfvo.setIvrBnVERIFIED_STATUS(0);
			 }else{
				 locObjLbrprfvo.setIvrBnVERIFIED_STATUS(Integer.parseInt(locvrLBR_VERIFIED_STATUS));
			 }

			 locObjLbrprfvo.setIvrBnKEY_FOR_SEARCH(locvrLBR_KEY_FOR_SEARCH);
			 if(!Commonutility.toCheckisNumeric(locvrLBR_WORK_TYPE_ID) || locvrLBR_WORK_TYPE_ID.equalsIgnoreCase("0")){
				 locObjLbrprfvo.setWrktypId(null);
			 }else{
				 LaborWrkTypMasterTblVO locLbrWrktypobj=new LaborWrkTypMasterTblVO();
				 locLbrWrktypobj.setWrktypId(Integer.parseInt(locvrLBR_WORK_TYPE_ID));
				 locObjLbrprfvo.setWrktypId(locLbrWrktypobj);
				 //locObjLbrprfvo.setIvrBnWORK_TYPE_ID(Integer.parseInt(locvrLBR_WORK_TYPE_ID));
			 }

			 locObjLbrprfvo.setIvrBnLBR_DESCP(locvrLBR_DESCP);

			 if(!Commonutility.toCheckisNumeric(locvrENTRY_BY)){
				 locObjLbrprfvo.setIvrBnENTRY_BY(null);
			 }else{
				 UserMasterTblVo locuamobj = new UserMasterTblVo();
				 locuamobj.setUserId(Integer.parseInt(locvrENTRY_BY));
				 locObjLbrprfvo.setIvrBnENTRY_BY(locuamobj);
			 }


			 // Select Group Code on Labor
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";
			 locQryrst=locSession.createQuery(locSlqry);
			 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
			 if(locGrpMstrVOobj!=null){
				 locGRPMstrvo=new GroupMasterTblVo();
				 locGRPMstrvo.setGroupCode(locGrpMstrVOobj.getGroupCode());
				 locObjLbrprfvo.setIvrGrpcode(locGRPMstrvo);
			 }else{// new group create
				 uamDao uam=new uamDaoServices();
				 int locGrpcode=uam.createnewgroup_rtnId(pGrpName);
				 if(locGrpcode!=-1 && locGrpcode!=0){
					 locGRPMstrvo=new GroupMasterTblVo();
					 locGRPMstrvo.setGroupCode(locGrpcode);
					 locObjLbrprfvo.setIvrGrpcode(locGRPMstrvo);
				 }else{
					 locObjLbrprfvo.setIvrGrpcode(null);
				 }
			 }
			 if(Commonutility.checkempty(locimagename)){
				 locimagename = locimagename.replaceAll(" ", "_");
			 }			 
			 if(iswebmobilefla != null && !iswebmobilefla.equalsIgnoreCase("null") && iswebmobilefla.equalsIgnoreCase("1")) {
				 locObjLbrprfvo.setIvrBnIMAGE_NAME_WEB(fileUploadFileName);
				 locObjLbrprfvo.setIvrBnIMAGE_NAME_MOBILE(fileUploadFileName);
			 } else  {
				 System.out.println("iswebmobilefla  "+iswebmobilefla);
				 locObjLbrprfvo.setIvrBnIMAGE_NAME_WEB(locimagename);
				 locObjLbrprfvo.setIvrBnIMAGE_NAME_MOBILE(locimagename);
			 }
			 System.out.println("iswebmobilefla  "+iswebmobilefla);
			 locObjLbrprfvo.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			 //----------- labor reg Start-----------
			 log.logMessage("Labor Detail insert will start.", "info", LaborUtility.class);
			 locLabrObj=new LaborDaoservice();
			 locLbrID = locLabrObj.saveLaborInfo_int(locObjLbrprfvo);

			 System.out.println(locLbrID+": id labour");
			 log.logMessage("Labor Detail insert End Labor Id : "+locLbrID, "info", LaborUtility.class);
			// Merchant Group code insert into mvp_merchant_issue_tbl
			/*	GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
				String pGrpNames = rb.getString("Grp.merchant");
				 // Select Group Code on Merchant
				String locSlqrys="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
				Query locQryrsts=locSession.createQuery(locSlqrys);
				lvrGrpmstobj = (GroupMasterTblVo) locQryrsts.uniqueResult();
				 if(lvrGrpmstobj!=null){
					 lvrGrpmstIDobj = new GroupMasterTblVo();
					 lvrGrpmstIDobj.setGroupCode(lvrGrpmstobj.getGroupCode());
				 }else {
					 lvrGrpmstIDobj = null;
				 }*/
			
			 // -----------labor reg end------------
			 if(locLbrID!=0 && locLbrID!=-1 && locLbrID > 0) {
				 System.out.println("offerisstxt ------------------ "+fileUploadFileName);
				 if(offerisstxt!=null && !offerisstxt.equalsIgnoreCase("null") && !offerisstxt.equalsIgnoreCase("")){
						String[] offerisst=offerisstxt.split(",");
						for(int k=0;k<offerisst.length;k++){
							mechtissuetbl=new MerchantIssueTblVO();
							mechtissuetbl.setMcrctLaborId(locLbrID);
							mechtissuetbl.setDescription(offerisst[k]);
							mechtissuetbl.setEntryBy(Integer.parseInt(locvrENTRY_BY));
							mechtissuetbl.setIvrGrpcodeobj(locGRPMstrvo);
							mechtissuetbl.setStatus(1);
							mechtissuetbl.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							boolean issuesuss = merchanthbm.insertMerchantissueTbl(mechtissuetbl, locSession);
							mechtissuetbl=null;
						}
					}				 				
				try {
					//image write into folder
					 System.out.println("image write into folder==="+fileUploadFileName);
					 if(iswebmobilefla != null && !iswebmobilefla.equalsIgnoreCase("null") && iswebmobilefla.equalsIgnoreCase("1")){
						 if(fileUploadFileName!=null && fileUploadFileName.length()>0){
							int lneedWidth=0,lneedHeight = 0;
							//String destPath =getText("external.path")+"PostData/";
							String destPath =rb.getString("external.imagesfldr.path")+"labor/web/"+locLbrID;
							locCommutillObj.deleteallFileInDirectory(destPath);
							int imgHeight = mobiCommon.getImageHeight(fileUpload);
							int imgwidth = mobiCommon.getImageWidth(fileUpload);
							String imageHeightPro=rb.getString("thump.img.height");
				        		   String imageWidthPro=rb.getString("thump.img.width");
							File destFile = new File(destPath, fileUploadFileName);
							FileUtils.copyFile(fileUpload, destFile);
							if (imgHeight>Integer.parseInt(imageHeightPro)){
			        				   lneedHeight = Integer.parseInt(imageHeightPro);
			    	        		 //mobile - small image
							} else {
			        				   lneedHeight = imgHeight;
							}
							if (imgwidth>Integer.parseInt(imageWidthPro)){
			        			lneedWidth = Integer.parseInt(imageWidthPro);
							} else {
								lneedWidth = imgwidth;
							}
			        			String limgSourcePath=rb.getString("external.imagesfldr.path")+"labor/web/"+locLbrID+"/"+fileUploadFileName;
		   		 		 		String limgDisPath = rb.getString("external.imagesfldr.path")+"labor/mobile/"+locLbrID;
		   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
		   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);

			        			   String limageFor = "all";
		   		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
								}
					 } else {
						 if((lvrLbrimgsrcpth!=null && !lvrLbrimgsrcpth.equalsIgnoreCase("null") && !lvrLbrimgsrcpth.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
								//String delrs = Commonutility.todelete("", pWebImagpath+result+"/");
								//String delrs_mob= Commonutility.todelete("", pMobImgpath+result+"/");
								Commonutility.toWriteImageMobWebNewUtill(locLbrID, locimagename,lvrLbrimgsrcpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, staffcreation.class);

							/*
							 byte imgbytee[]= new byte[1024];
							 imgbytee=Commonutility.toDecodeB64StrtoByary(locimg_encdata);
							 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+locLbrID+"/", locimagename);

							 //mobile - small image
							 String limgSourcePath=pWebImagpath+locLbrID+"/"+locimagename;
							 String limgDisPath = pMobImgpath+locLbrID+"/";
							 String limgName = FilenameUtils.getBaseName(locimagename);
							 String limageFomat = FilenameUtils.getExtension(locimagename);
							 String limageFor = "all";
							 int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
							 int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));
							 ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
							 */
						}
					}
				 } catch(Exception imger){
					 System.out.println("Exception in  image write on labor insert : "+imger);
					 log.logMessage("Exception in Image write on labor insert", "info", LaborUtility.class);
				 }finally{

				 }

				 JSONArray jsonArray = (JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cateoryJary");

				 LaborSkillTblVO inrlocLbrskill=null;
				 log.logMessage("Labor  Category Detail insert will start.", "info", LaborUtility.class);
				 boolean lbrskilflg=false;
				 if (jsonArray!=null ) {
					 for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject rec = jsonArray.getJSONObject(i);
						String catid = rec.getString("cate_id");
						String skillid = rec.getString("skill_id");
						if (Commonutility.toCheckisNumeric(catid)) {
							lbrskilflg=true;
							inrlocLbrskill=new LaborSkillTblVO();
							inrlocLbrskill.setIvrBnLBR_ID(locLbrID);
							CategoryMasterTblVO locCateid=new CategoryMasterTblVO();
							locCateid.setiVOCATEGORY_ID(Integer.parseInt(catid));
							SkillMasterTblVO locskillsid=new SkillMasterTblVO();
							locskillsid.setIvrBnSKILL_ID(Integer.parseInt(skillid));
						 //inrlocLbrskill.setIvrBnLBR_CATEG_ID(locCateid);
							inrlocLbrskill.setiVOCATEGORY_ID(locCateid);
							inrlocLbrskill.setIvrBnSKILL_ID(locskillsid);
							inrlocLbrskill.setIvrBnLBR_SKILL_STS(1);
						 	inrlocLbrskill.setIvrBnLBR_SKILL_ID(null);
						 	inrlocLbrskill.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
						 	int locSkilinsrtrst = locLabrObj.saveLaborSkilInfo_intRtn(inrlocLbrskill);
						 	inrlocLbrskill=null;
						}
				 }
				 }
				 if(lbrskilflg){// Audit trial use only skill/Category update
					 if(Commonutility.toCheckisNumeric(locvrENTRY_BY)){
						 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, Integer.parseInt(locvrENTRY_BY));
					 }else{
						 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, 1);
					 }
				 }
				 log.logMessage("Labor  Category Detail insert will end.", "info", LaborUtility.class);
				// jry=null;//category json array clear
				//Send Email
				if(locObjLbrprfvo.getIvrBnLBR_EMAIL()!=null && Commonutility.checkempty(locObjLbrprfvo.getIvrBnLBR_EMAIL())){
					EmailEngineServices emailservice = new EmailEngineDaoServices();
					EmailTemplateTblVo emailTemplate;
					try {
			            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Labour' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();

			            emqry = common.templateParser(emailMessage, 1, "", "");
			            String[] qrySplit = emqry.split("!_!");
			            String qry = qrySplit[0] + " FROM LaborProfileTblVO as labour where labour.ivrBnLBR_ID='"+locLbrID+"'";
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();

			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(locObjLbrprfvo.getIvrBnLBR_EMAIL(), emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            System.out.println("Excption found in company creation Emailsend LaborUtility.class : " + ex);
			            log.logMessage("Exception in staff admin flow emailFlow : " + ex, "error",LaborUtility.class);

			          } finally {			        	 
			          }
					}
				
				if(locObjLbrprfvo.getIvrBnLBR_PH_NO()!=null &&  Commonutility.checkempty(locObjLbrprfvo.getIvrBnLBR_PH_NO())) {
					Commonutility.toWriteConsole("===================sms===========================");
					SmsTemplatepojo smsTemplate = null;
					SmsEngineServices smsService = new SmsEngineDaoServices();
					SmsInpojo sms = new SmsInpojo();
					try {
						String mailRandamNumber;
						mailRandamNumber = common.randInt(5555, 999999999);
						String qry = "FROM SmsTemplatepojo where " + "templateName ='Create Labor' and status ='1'";
						smsTemplate = smsService.smsTemplateData(qry);
						String smsMessage = smsTemplate.getTemplateContent();
						//smsMessage = smsMessage.replace("[SOCIETY NAME]", lvrSocyname);
						qry = common.smsTemplateParser(smsMessage, 1, "", "");
						String[] qrySplit = qry.split("!_!");
						String qryform =  qrySplit[0] + " FROM LaborProfileTblVO as labour where labour.ivrBnLBR_ID='"+locLbrID+"'";
						smsMessage = smsService.smsTemplateParserChange(qryform, Integer.parseInt(qrySplit[1]), smsMessage);
						sms.setSmsCardNo(mailRandamNumber);
						sms.setSmsEntryDateTime(common.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
						sms.setSmsMobNumber(locObjLbrprfvo.getIvrBnLBR_PH_NO());
						sms.setSmspollFlag("F");
						sms.setSmsMessage(smsMessage);
						smsService.insertSmsInTable(sms);
		          } catch (Exception ex) {
		        	  Commonutility.toWriteConsole("Excption found in smssend LaborUtility.class : " + ex);
			            log.logMessage("Excption found in smssend LaborUtility.class : " + ex, "error",LaborUtility.class);
		           
		          }	finally {
		        	  
		          }
				} else {
					 Commonutility.toWriteConsole("===================No SMS [Labor Mobile No Not found]===========================");
				}
				
					String filepath=rb.getString("external.mobiledbfldr.path");
					String textvalue="INSERT INTO labor_cities(CITY_ID,CITY_NAME,STATUS) VALUES("+locObjLbrprfvo.getIvrBnLBR_CITY_ID()+",'"+locvrCITY_NAME+"',1);";
					if(locObjLbrprfvo.getIvrBnLBR_CITY_ID()!=null && locObjLbrprfvo.getIvrBnLBR_CITY_ID()!=-1){
						Commonutility.TextFileWriting(filepath, textvalue);
					}
				 return "success!_!"+locLbrID;
			 } else if (locLbrID==-2 && locLbrID<=-2) {
				 return "duplicate!_!"+locLbrID;
			 }else {
			
				 return "error!_!"+locLbrID;
			 }
		} catch (Exception e) {
			System.out.println("Exception found in LaborUtility.toInsrtLabor : "+e);
			log.logMessage("Exception : "+e, "error", LaborUtility.class);
			locLbrID=0;
			return "error!_!"+locLbrID;
		} finally {
			 locCommutillObj =null; locLabrObj=null;
			 locvrLBR_SERVICE_ID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null; locvrLBR_PH_NO = null; locvrID_CARD_TYP = null;
			 locvrID_CARD_NO = null; locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrLBR_KYC_NAME = null; locvrLBR_RATING = null; locvrENTRY_BY = null;
			 if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;} locSlqry=null; locQryrst=null;locGrpMstrVOobj=null;locGRPMstrvo=null;
		}

	}

	public static JSONObject toSltSingleLabourDtl(JSONObject pDataJson, Session pSession, String pAuditMsg, String pAuditCode,String iswebmobilefla) {// Select on single user data
		String locvrLBR_ID=null, locvrLBR_SERVICE_ID=null, locvrLBR_STS=null;
		//LaborProfileTblVO locLPTblvoObj=null;
		LaborDetailsTblVO locLDTblvoObj=null;
		String loc_slQry=null;
		Query locQryObj= null;
		JSONObject locRtnDataJSON=null;

		String loc_slQry_categry=null;
		Iterator locObjLbrcateglst_itr=null;
		LaborSkillTblVO locLbrSkiltbl=null;
		JSONArray locLBRSklJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null;
		Log log=null;
		try {
			log=new Log();
			System.out.println("Step 1 : labor detail block enter");
			log.logMessage("Step 1 : Select labor detail block enter", "info", LaborUtility.class);
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			locRtnDataJSON=new JSONObject();
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrid");
			locvrLBR_SERVICE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrserviceid");
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrstatus");
			String filepath=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/labor/mobile/";
			String filepathweb=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/labor/web/";
			//loc_slQry="from LaborProfileTblVO where ivrBnLBR_ID="+locvrLBR_ID+" and ivrBnLBR_SERVICE_ID='"+locvrLBR_SERVICE_ID+"' and ivrBnLBR_STS="+locvrLBR_STS+"";
			if (Commonutility.checkempty(locvrLBR_SERVICE_ID)) {
				loc_slQry="from LaborDetailsTblVO where ivrBnLBR_ID="+locvrLBR_ID+" and ivrBnLBR_SERVICE_ID='"+locvrLBR_SERVICE_ID+"' ";
			} else {
				loc_slQry="from LaborDetailsTblVO where ivrBnLBR_ID="+locvrLBR_ID+" ";
			}
			
			locQryObj=pSession.createQuery(loc_slQry);
			//locLPTblvoObj=(LaborProfileTblVO) locQryObj.uniqueResult();
			locLDTblvoObj = (LaborDetailsTblVO)locQryObj.uniqueResult();
			System.out.println("Step 2 : Select labor detail query executed.");

			locRtnDataJSON.put("intv_lbr_id", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_ID()));
			locRtnDataJSON.put("strv_lbr_serviceid", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_SERVICE_ID()));
			locRtnDataJSON.put("strv_lbr_name", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_NAME()));
			locRtnDataJSON.put("strv_lbr_email", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_EMAIL()));
			locRtnDataJSON.put("strv_lbr_isdcode",Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_ISD_CODE()));
			locRtnDataJSON.put("strv_lbr_phno", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_PH_NO()));

			if(locLDTblvoObj.getIvrBnIdrcardObj()!=null){
				locRtnDataJSON.put("intv_lbr_cardtyp", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnIdrcardObj().getiVOcradid()));
				locRtnDataJSON.put("intv_lbr_cardtyp_name", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnIdrcardObj().getiVOcradname()));
			}else{
				locRtnDataJSON.put("intv_lbr_cardtyp", "");
				locRtnDataJSON.put("intv_lbr_cardtyp_name", "");
			}
			locRtnDataJSON.put("strv_lbr_cardno", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnID_CARD_NO()));
			locRtnDataJSON.put("strv_lbr_add1", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_ADD_1()));
			locRtnDataJSON.put("strv_lbr_add2", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_ADD_2()));

			if(locLDTblvoObj.getPstlId()!=null){

//				locRtnDataJSON.put("intv_lbr_pstlid",String.valueOf(locLDTblvoObj.getPstlId().getPstlId()));
//				locRtnDataJSON.put("lbr_pincodeName",locLDTblvoObj.getPstlId().getPstlCode());
//				locRtnDataJSON.put("intv_lbr_cityid",String.valueOf(locLDTblvoObj.getPstlId().getCityId().getCityId()));
//				locRtnDataJSON.put("lbr_cityName",locLDTblvoObj.getPstlId().getCityId().getCityName());
//				locRtnDataJSON.put("intv_lbr_stateid",String.valueOf(locLDTblvoObj.getPstlId().getCityId().getStateId().getStateId()));
//				locRtnDataJSON.put("lbr_stateName",locLDTblvoObj.getPstlId().getCityId().getStateId().getStateName());
//				locRtnDataJSON.put("intv_lbr_cntry",String.valueOf(locLDTblvoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//				locRtnDataJSON.put("lbr_cntryName",locLDTblvoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());
				
				locRtnDataJSON.put("intv_lbr_pstlid",String.valueOf(locLDTblvoObj.getPstlId()));
				locRtnDataJSON.put("lbr_pincodeName",String.valueOf(locLDTblvoObj.getPstlId()));
			}else{
				locRtnDataJSON.put("intv_lbr_pstlid","");
				locRtnDataJSON.put("lbr_pincodeName","");
//				locRtnDataJSON.put("intv_lbr_cityid","");
//				locRtnDataJSON.put("lbr_cityName","");
//				locRtnDataJSON.put("intv_lbr_stateid","");
//				locRtnDataJSON.put("lbr_stateName","");
//				locRtnDataJSON.put("intv_lbr_cntry","");
//				locRtnDataJSON.put("lbr_cntryName","");
			}
			

			if(locLDTblvoObj.getCityId() != null){
				locRtnDataJSON.put("intv_lbr_cityid",String.valueOf(locLDTblvoObj.getCityId().getCityId()));
				locRtnDataJSON.put("lbr_cityName",locLDTblvoObj.getCityId().getCityName());
					if(locLDTblvoObj.getCityId().getStateId() != null){
						locRtnDataJSON.put("intv_lbr_stateid",String.valueOf(locLDTblvoObj.getCityId().getStateId().getStateId()));
						locRtnDataJSON.put("lbr_stateName",locLDTblvoObj.getCityId().getStateId().getStateName());
						
						if(locLDTblvoObj.getCityId().getStateId().getCountryId() != null){
						locRtnDataJSON.put("intv_lbr_cntry",String.valueOf(locLDTblvoObj.getCityId().getStateId().getCountryId().getCountryId()));
						locRtnDataJSON.put("lbr_cntryName",locLDTblvoObj.getCityId().getStateId().getCountryId().getCountryName());
						}
						else{
							locRtnDataJSON.put("intv_lbr_cntry","");
							locRtnDataJSON.put("lbr_cntryName","");
						}
					}
					else{
						locRtnDataJSON.put("intv_lbr_stateid","");
						locRtnDataJSON.put("lbr_stateName","");
						locRtnDataJSON.put("intv_lbr_cntry","");
						locRtnDataJSON.put("lbr_cntryName","");
					}
			}
			else{
				locRtnDataJSON.put("intv_lbr_cityid","");
				locRtnDataJSON.put("lbr_cityName","");
				locRtnDataJSON.put("intv_lbr_stateid","");
				locRtnDataJSON.put("lbr_stateName","");
				locRtnDataJSON.put("intv_lbr_cntry","");
				locRtnDataJSON.put("lbr_cntryName","");
			}

			locRtnDataJSON.put("strv_lbr_kycname", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_KYC_NAME()));
			locRtnDataJSON.put("intv_lbr_rating", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_RATING()));
			locRtnDataJSON.put("str_lbr_keyfrsrch",Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_KEY_FOR_SEARCH()));
			locRtnDataJSON.put("str_lbr_descp",Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_DESCP()));
			locRtnDataJSON.put("intv_lbr_verifysts", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_VERIFIED_STATUS()));
			if(locLDTblvoObj.getWrktypId()!=null){
				locRtnDataJSON.put("intv_lbr_wrktypid", Commonutility.toCheckNullEmpty(locLDTblvoObj.getWrktypId().getWrktypId()));
				locRtnDataJSON.put("str_lbr_wrktypname", Commonutility.toCheckNullEmpty(locLDTblvoObj.getWrktypId().getIVOlbrWORK_TYPE()));
			}else{
				locRtnDataJSON.put("intv_lbr_wrktypid", "0");
				locRtnDataJSON.put("str_lbr_wrktypname", "");
			}
			System.out.println("dddd    --- ");
			if(locLDTblvoObj.getIvrBnIMAGE_NAME_MOBILE()!=null){
				locRtnDataJSON.put("profilepic_thumbnail",Commonutility.toCheckNullEmpty(filepath+locLDTblvoObj.getIvrBnLBR_ID()+"/"+locLDTblvoObj.getIvrBnIMAGE_NAME_MOBILE()));}
				else
				{
					locRtnDataJSON.put("profilepic_thumbnail","");
				}
			if(locLDTblvoObj.getIvrBnIMAGE_NAME_MOBILE()!=null){
				locRtnDataJSON.put("profilepic",Commonutility.toCheckNullEmpty(filepathweb+locLDTblvoObj.getIvrBnLBR_ID()+"/"+locLDTblvoObj.getIvrBnIMAGE_NAME_MOBILE()));
			} else {
					locRtnDataJSON.put("profilepic","");
			}
			locRtnDataJSON.put("str_lbr_webimage", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnIMAGE_NAME_WEB()));
			locRtnDataJSON.put("str_lbr_mobileimage", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnIMAGE_NAME_MOBILE()));
			locRtnDataJSON.put("experience", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_EXPERIENCE()));
			locRtnDataJSON.put("cost", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_COST()));
			locRtnDataJSON.put("costper", Commonutility.toCheckNullEmpty(locLDTblvoObj.getIvrBnLBR_COSTPER()));
			if (locLDTblvoObj.getCompany()!=null) {
				locRtnDataJSON.put("companyId", Commonutility.toCheckNullEmpty(locLDTblvoObj.getCompany().getCompanyId()));
				locRtnDataJSON.put("companyName", Commonutility.toCheckNullEmpty(locLDTblvoObj.getCompany().getCompanyName()));
			} else {
				locRtnDataJSON.put("companyId", "");
				locRtnDataJSON.put("companyName", "");
			}
			
			// Merchant Group code insert into mvp_merchant_issue_tbl
			GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
			String pGrpName = rb.getString("Grp.labor");
			System.out.println("pGrpName ---------------- "+pGrpName);
			Integer lvrMrcnhtgrpid = null;
			 // Select Group Code on Merchant
			String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
			Query locQryrst=pSession.createQuery(locSlqry);
			lvrGrpmstobj = (GroupMasterTblVo) locQryrst.uniqueResult();
			 if(lvrGrpmstobj!=null){
				 lvrMrcnhtgrpid = lvrGrpmstobj.getGroupCode();
			 }else {
				 lvrMrcnhtgrpid = null;
			 }
			 System.out.println("lvrfunSlqry -------- "+lvrGrpmstobj.getGroupCode());

			List<Object> lvrObjfunctionlstitr = null;
			String lvrfunSlqry = "select issueId,description from MerchantIssueTblVO where mcrctLaborId="+locvrLBR_ID+" and status=1 and ivrGrpcodeobj = "+lvrMrcnhtgrpid;
			System.out.println("lvrfunSlqry -------- "+lvrfunSlqry);
			lvrObjfunctionlstitr=pSession.createQuery(lvrfunSlqry).list();
			locRtnDataJSON.put("issuedata", lvrObjfunctionlstitr);
			log.logMessage("Step 2: Select Labor categoey detail block start.", "info", LaborUtility.class);
			loc_slQry_categry="from LaborSkillTblVO where ivrBnLBR_ID="+locvrLBR_ID+" and ivrBnLBR_SKILL_STS = 1";
			locObjLbrcateglst_itr=pSession.createQuery(loc_slQry_categry).list().iterator();
			System.out.println("Step 3 : Select labor category detail query executed.");

			locLBRSklJSONAryobj=new JSONArray();
			while (locObjLbrcateglst_itr!=null &&  locObjLbrcateglst_itr.hasNext() ) {
				locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr.next();
				locInrLbrSklJSONObj=new JSONObject();
				if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){
					locInrLbrSklJSONObj.put("categoryid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));
					locInrLbrSklJSONObj.put("categoryname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
					locInrLbrSklJSONObj.put("skillsid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_ID()));
					locInrLbrSklJSONObj.put("skillsname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_NAME()));
				}
				locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
			}
			log.logMessage("Step 3: Select Labor categoey detail block end.", "info", LaborUtility.class);
			System.out.println("Step 4 : Select labor category detail are formed into JSONObject - json array - Filan JSON.");
			locRtnDataJSON.put("jArry_lbr_catg", locLBRSklJSONAryobj);

			System.out.println("Step 5 : Return JSON DATA : "+locRtnDataJSON);
			System.out.println("Step 6 : Select labor detail block end.");
			log.logMessage("Step 4: Select Labor detail block end.", "info", LaborUtility.class);
			return locRtnDataJSON;
		} catch (Exception e) {
			try{
			System.out.println("Step -1 : Select labor detail Exception found in LaborUtility.toSltSingleLabourDtl : "+e);
			log.logMessage("Exception : "+e, "error", LaborUtility.class);
			locRtnDataJSON=new JSONObject();
			locRtnDataJSON.put("catch", "Error");
			}catch(Exception ee){}
			return locRtnDataJSON;
		} finally {
			 locvrLBR_ID=null; locvrLBR_SERVICE_ID=null; locLDTblvoObj=null; loc_slQry=null; locRtnDataJSON=null;log=null;
			 locObjLbrcateglst_itr=null;locLbrSkiltbl=null;locInrLbrSklJSONObj=null;locLBRSklJSONAryobj=null;loc_slQry_categry=null;
		}
	}

	private static String getText(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String toUpdtLabor(JSONObject pDataJson,String pAuditMsg, String pAuditCode,String pWebImagpath, String pMobImgpath,String iswebmobilefla, String fileUploadFileName, File fileUpload) {// Update
		//JSONObject locRtnJson=null;
		Log log=null;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null,locvrLBR_EMAIL=null,locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null;
		String locvLBR_STATE_ID = null,locvrLBR_PSTL_ID=null,locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null;
		String locvrENTRY_BY = null; /*locvrLBR_RATING = null,*/
		String locvrLBR_ISD_CODE=null,locvrLBR_VERIFIED_STATUS=null, locvrLBR_KEY_FOR_SEARCH=null, locvrLBR_WORK_TYPE_ID=null, locvrLBR_DESCP=null;
		String locvrLBR_ID=null,locvrLBR_Image_Mobile=null,locvrLBR_Image_web=null,locvrID_CARD_TYP=null,locvrID_CARD_NO=null;
		String locimagename=null, locimg_encdata=null,locvrLBR_COST=null,locvrLBR_COSTPER=null,locvrLBR_EXP=null,locvrCITY_NAME=null,companyid=null,companyname=null;
		String locLbrUpdqry="", lvrLbrimgsrcpth = null,laborissuetxt=null;
		boolean locLbrUpdSts=false;
		 LaborDao locLabrObj=null;
		 ResourceBundle rb = null;
		 CommonUtils locCommutillObj =null;
		 Session locSession=null;
		try {
			locSession=locSession=HibernateUtil.getSession();
			rb = ResourceBundle.getBundle("applicationResources");
			locCommutillObj = new CommonUtilsDao();
			 CompanyMstTblVO companysid=new CompanyMstTblVO();
			log=new Log();
			if(iswebmobilefla != null && !iswebmobilefla.equalsIgnoreCase("null")  && iswebmobilefla.equalsIgnoreCase("1"))
			{
			/*fileUploadFileName="11.jpg";
			fileUpload=new File("D://11.jpg");*/
			}
			 locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrid");
			 locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "name");
			 locvrLBR_EMAIL = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "emailid");
			 locvrLBR_ADD_1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add1");
			 locvrLBR_ADD_2 =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add2");
			 locvrLBR_PSTL_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "postalcode");
			 locvrLBR_CITY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "city");
			 locvLBR_STATE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "sate");
			 locvrLBR_COUNTRY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "country");
			 locvLBR_STS = "1";
			 locvrLBR_KYC_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "kycname");
			// locvrLBR_RATING = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "rating");
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryby");
			String locvrLBR_PH_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "mobno");
			 locvrLBR_ISD_CODE=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ISDcode");
			 locvrLBR_VERIFIED_STATUS=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "verifystatus");
			 locvrLBR_KEY_FOR_SEARCH =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "keyforsrch");
			 locvrLBR_WORK_TYPE_ID=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "wktypid");
			 locvrLBR_DESCP=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "descrp");
			 locvrLBR_COST=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cost");
			 locvrLBR_COSTPER=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "costper");
			 locvrLBR_EXP=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "experience");
			 locvrCITY_NAME=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cityname");
			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imagename");
			 //locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgencdata");
			 lvrLbrimgsrcpth = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrimgsrchpath");
			 companyid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "laborcompanyname");
			 companyname = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "laborcompanyname");
			 System.out.println(companyid+"companyname---------1444---"+companyname);
			 if(Commonutility.checkempty(locimagename)){
				 locimagename = locimagename.replaceAll(" ", "_");
			 }

			 locvrLBR_Image_web=locimagename;
			 locvrLBR_Image_Mobile=locimagename;


			 locvrID_CARD_TYP = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cardtyp");
			 locvrID_CARD_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cardno");

			 locLbrUpdqry ="update LaborProfileTblVO set ivrBnLBR_NAME='"+locvrLBR_NAME+"', ivrBnLBR_EMAIL ='"+locvrLBR_EMAIL+"',ivrBnLBR_PH_NO='"+locvrLBR_PH_NO+"',ivrBnLBR_ISD_CODE='"+locvrLBR_ISD_CODE+"', ivrBnLBR_ADD_1='"+locvrLBR_ADD_1+"',ivrBnLBR_ADD_2='"+locvrLBR_ADD_2+"',ivrBnID_CARD_NO='"+locvrID_CARD_NO+"',ivrCOMPANY="+companyid+",";

			 if(locvrLBR_COST!=null && !locvrLBR_COST.equalsIgnoreCase("null") && !locvrLBR_COST.equalsIgnoreCase("")){
				// locObjLbrprfvo.setIvrBnLBR_COST(Float.parseFloat(locvrLBR_COST));
				 locLbrUpdqry+="ivrBnLBR_COST = "+Float.parseFloat(locvrLBR_COST)+",";
			 }
			 if(locvrLBR_EXP!=null && !locvrLBR_EXP.equalsIgnoreCase("null") && !locvrLBR_EXP.equalsIgnoreCase("")){
				 locLbrUpdqry+="ivrBnLBR_EXPERIENCE = "+Float.parseFloat(locvrLBR_EXP)+",";
			 }
			 if(locvrLBR_COSTPER!=null && !locvrLBR_COSTPER.equalsIgnoreCase("null") && !locvrLBR_COSTPER.equalsIgnoreCase("")){
				 locLbrUpdqry+="ivrBnLBR_COSTPER = "+Float.parseFloat(locvrLBR_COSTPER)+",";
			 }

			 if(!Commonutility.toCheckisNumeric(locvrID_CARD_TYP) || locvrID_CARD_TYP.equalsIgnoreCase("0")){
				 locLbrUpdqry+="ivrBnID_CARD_TYP = "+null+",";
			 }else{
				 locLbrUpdqry+="ivrBnID_CARD_TYP = "+Integer.parseInt(locvrID_CARD_TYP)+",";
			 }

			 if(!Commonutility.toCheckisNumeric(locvrLBR_PSTL_ID) || locvrLBR_PSTL_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="ivrBnLBR_PSTL_ID = "+null+",";
			 }else{
				 locLbrUpdqry+="ivrBnLBR_PSTL_ID = "+Integer.parseInt(locvrLBR_PSTL_ID)+",";
			 }

			 if(!Commonutility.toCheckisNumeric(locvrLBR_CITY_ID) || locvrLBR_CITY_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="ivrBnLBR_CITY_ID = "+null+",";
			 }else{
				 locLbrUpdqry+="ivrBnLBR_CITY_ID = "+Integer.parseInt(locvrLBR_CITY_ID)+",";
			 }

			 if(!Commonutility.toCheckisNumeric(locvLBR_STATE_ID) || locvLBR_STATE_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="ivrBnLBR_STATE_ID="+null+",";
			 }else{
				 locLbrUpdqry+="ivrBnLBR_STATE_ID="+Integer.parseInt(locvLBR_STATE_ID)+",";
			 }

			 if(!Commonutility.toCheckisNumeric(locvrLBR_COUNTRY_ID) || locvrLBR_COUNTRY_ID.equalsIgnoreCase("0")){
				 locLbrUpdqry+="ivrBnLBR_COUNTRY_ID= "+null+",";
			 }else{
				 locLbrUpdqry+="ivrBnLBR_COUNTRY_ID= "+Integer.parseInt(locvrLBR_COUNTRY_ID)+",";
			 }

			 //if(!Commonutility.toCheckisNumeric(locvrLBR_RATING) || locvrLBR_RATING.equalsIgnoreCase("0")){
				// locLbrUpdqry+="ivrBnLBR_RATING= "+0+",";
			// }else{
				// locLbrUpdqry+="ivrBnLBR_RATING= "+Integer.parseInt(locvrLBR_RATING)+",";
			// }


			 if (!Commonutility.toCheckisNumeric(locvrLBR_VERIFIED_STATUS) || locvrLBR_VERIFIED_STATUS.equalsIgnoreCase("0")){
				 locLbrUpdqry+="ivrBnVERIFIED_STATUS= "+0+",";
			 } else{
				 locLbrUpdqry+="ivrBnVERIFIED_STATUS= "+Integer.parseInt(locvrLBR_VERIFIED_STATUS)+",";
			 }


			 locLbrUpdqry+="ivrBnKEY_FOR_SEARCH= '"+locvrLBR_KEY_FOR_SEARCH+"',";

			 if (!Commonutility.toCheckisNumeric(locvrLBR_WORK_TYPE_ID) || locvrLBR_WORK_TYPE_ID.equalsIgnoreCase("0")) {
				 locLbrUpdqry+="wrktypId= "+null+",";
			 } else {
				 locLbrUpdqry+="wrktypId= "+Integer.parseInt(locvrLBR_WORK_TYPE_ID)+",";
			 }
			 if (!Commonutility.toCheckisNumeric(locvLBR_STS) || locvLBR_STS.equalsIgnoreCase("0")) {
				 locLbrUpdqry+="ivrBnLBR_STS= "+locvLBR_STS+",";
			 } else {
				 locLbrUpdqry+="ivrBnLBR_STS= "+Integer.parseInt(locvLBR_STS)+",";
			 }
			 if (iswebmobilefla != null && !iswebmobilefla.equalsIgnoreCase("null") && iswebmobilefla.equalsIgnoreCase("1")) {
				 /*locObjLbrprfvo.setIvrBnIMAGE_NAME_WEB(fileUploadFileName);
				 locObjLbrprfvo.setIvrBnIMAGE_NAME_MOBILE(fileUploadFileName);	*/
				 if(fileUploadFileName!=null && !fileUploadFileName.equalsIgnoreCase("null") && !fileUploadFileName.equalsIgnoreCase("")){
				 locLbrUpdqry+="ivrBnIMAGE_NAME_WEB='"+fileUploadFileName+"',";
				 locLbrUpdqry+="ivrBnIMAGE_NAME_MOBILE='"+fileUploadFileName+"',";
				 }
			 } else {
				 if(locvrLBR_Image_web!=null && !locvrLBR_Image_web.equalsIgnoreCase("null") && !locvrLBR_Image_web.equalsIgnoreCase("")){
					 locLbrUpdqry+="ivrBnIMAGE_NAME_WEB='"+locvrLBR_Image_web+"',";
				 }
				 if(locvrLBR_Image_Mobile!=null && !locvrLBR_Image_Mobile.equalsIgnoreCase("null") && !locvrLBR_Image_Mobile.equalsIgnoreCase("")){
					 locLbrUpdqry+="ivrBnIMAGE_NAME_MOBILE='"+locvrLBR_Image_Mobile+"',";
				 }
			 }
			locLbrUpdqry+="ivrBnLBR_DESCP= '"+locvrLBR_DESCP+"',";
			locLbrUpdqry+="ivrBnLBR_KYC_NAME ='"+locvrLBR_KYC_NAME+"',ivrBnENTRY_BY ="+locvrENTRY_BY+"  where ivrBnLBR_ID ="+locvrLBR_ID+"";
			//locLbrUpdqry ="update mvp_lbr_reg_tbl set LBR_NAME='"+locvrLBR_NAME+"', LBR_EMAIL ='"+locvrLBR_EMAIL+"',LBR_ADD_1='"+locvrLBR_ADD_1+"',LBR_ADD_2='"+locvrLBR_ADD_2+"'," +
			//"LBR_PSTL_ID ="+locvrLBR_PSTL_ID+",LBR_CITY_ID ="+locvrLBR_CITY_ID+", LBR_STATE_ID ="+locvLBR_STATE_ID+",LBR_COUNTRY_ID ="+locvrLBR_COUNTRY_ID+"," +
			//"LBR_KYC_NAME ='"+locvrLBR_KYC_NAME+"',LBR_RATING ="+locvrLBR_RATING+",ENTRY_BY ="+locvrENTRY_BY+" where locvrLBR_ID ="+locvrLBR_ID+"";
			
			/*if(companyname!=null && !companyname.equalsIgnoreCase("null") && !companyname.equalsIgnoreCase("")){
				companysid.setCompanyName(companyname);
				 locLbrUpdqry+="ivrCOMPANY = "+companyname+",";
			 }else{
				 locLbrUpdqry+="ivrCOMPANY = "+" "+",";
			 }*/
			
			
			log.logMessage("Updqry : "+locLbrUpdqry, "info", LaborUtility.class);
			locLabrObj=new LaborDaoservice();
			locLbrUpdSts = locLabrObj.updateLaborInfo(locLbrUpdqry);

			// Merchant Group code insert into mvp_merchant_issue_tbl
			GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
			String pGrpName = rb.getString("Grp.labor");
			String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
			Query locQryrst=locSession.createQuery(locSlqry);
			lvrGrpmstobj = (GroupMasterTblVo) locQryrst.uniqueResult();
			 if(lvrGrpmstobj!=null){
				 lvrGrpmstIDobj = new GroupMasterTblVo();
				 lvrGrpmstIDobj.setGroupCode(lvrGrpmstobj.getGroupCode());
			 }else {
				 lvrGrpmstIDobj = null;
			 }
			JSONArray jryid=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "issuetxt");
			 String lvrfunmtr="";
			 MerchantIssueTblVO inrlocfun=null;
			String mvpMrchTblid="mcrctLaborId";
			String pojo="MerchantIssueTblVO";
			 Common common=new CommonDao();
				lvrfunmtr=common.commonLarmctDelete(Integer.parseInt(locvrLBR_ID), pojo, mvpMrchTblid,lvrGrpmstobj.getGroupCode());
				System.out.println("lvrfunmtr=== "+lvrfunmtr);
				 MerchantManageServices merchanthbm=new MerchantManageDaoServices();
				 if(jryid!=null){
			 for (int i = 0; i < jryid.length(); i++) {
				 String textname=(String)jryid.getString(i);
					 if(textname!=null && !textname.equalsIgnoreCase("") && !textname.equalsIgnoreCase("null")){
						 inrlocfun=new MerchantIssueTblVO();
						 inrlocfun.setDescription(textname.trim());
						 inrlocfun.setEntryBy(Integer.parseInt(locvrENTRY_BY));
						 inrlocfun.setStatus(1);
						 inrlocfun.setMcrctLaborId(Integer.parseInt(locvrLBR_ID));
						 inrlocfun.setIvrGrpcodeobj(lvrGrpmstIDobj);
						 inrlocfun.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
						 boolean issuesuss = merchanthbm.insertMerchantissueTbl(inrlocfun, locSession);
						inrlocfun=null;

				 }
			 }
				 }

			if (Commonutility.toCheckNullEmpty(locvrLBR_ID)!=null && !Commonutility.toCheckNullEmpty(locvrLBR_ID).equalsIgnoreCase("null")){
				try {
				 //image write into folder
					if (iswebmobilefla != null && !iswebmobilefla.equalsIgnoreCase("null") && iswebmobilefla.equalsIgnoreCase("1")){
						if (fileUploadFileName != null && fileUploadFileName.length() > 0) {
								int lneedWidth=0,lneedHeight = 0;
								//String destPath =getText("external.path")+"PostData/";
								String destPath =rb.getString("external.imagesfldr.path")+"labor/web/"+locvrLBR_ID;
								locCommutillObj.deleteallFileInDirectory(destPath);
								int imgHeight=mobiCommon.getImageHeight(fileUpload);
				        		int imgwidth=mobiCommon.getImageWidth(fileUpload);
								String imageHeightPro=rb.getString("thump.img.height");
				        		String imageWidthPro=rb.getString("thump.img.width");
				        		File destFile  = new File(destPath, fileUploadFileName);
		   		       	    	FileUtils.copyFile(fileUpload, destFile);
							if (imgHeight>Integer.parseInt(imageHeightPro)){
			        			lneedHeight = Integer.parseInt(imageHeightPro);
			    	        	//mobile - small image
							} else {
								lneedHeight = imgHeight;
							}

							if (imgwidth>Integer.parseInt(imageWidthPro)){
								lneedWidth = Integer.parseInt(imageWidthPro);
							} else {
								lneedWidth = imgwidth;
							}
			        			String limgSourcePath=rb.getString("external.imagesfldr.path")+"labor/web/"+locvrLBR_ID+"/"+fileUploadFileName;
		   		 		 		String limgDisPath = rb.getString("external.imagesfldr.path")+"labor/mobile/"+locvrLBR_ID;
		   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
		   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);
		   		 		 	 	String limageFor = "all";
		   		 		 	 	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
							}
					 }  else {
						 if((lvrLbrimgsrcpth!=null && !lvrLbrimgsrcpth.equalsIgnoreCase("null") && !lvrLbrimgsrcpth.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
							 String delrs = Commonutility.todelete("", pWebImagpath+locvrLBR_ID+"/");
							 String delrs_mob= Commonutility.todelete("", pMobImgpath+locvrLBR_ID+"/");
							 Commonutility.toWriteImageMobWebNewUtill(Integer.parseInt(locvrLBR_ID), locimagename,lvrLbrimgsrcpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, staffcreation.class);
							 /*
					 			String delrs= Commonutility.todelete("", pWebImagpath+locvrLBR_ID+"/");
					 			byte imgbytee[]= new byte[1024];
					 			imgbytee=Commonutility.toDecodeB64StrtoByary(locimg_encdata);
					 			String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+locvrLBR_ID+"/", locimagename);

					 String delrs_mob= Commonutility.todelete("", pMobImgpath+locvrLBR_ID+"/");
					//mobile - small image
					String limgSourcePath=pWebImagpath+locvrLBR_ID+"/"+locimagename;
		 		 	String limgDisPath = pMobImgpath+locvrLBR_ID+"/";
		 		 	String limgName = FilenameUtils.getBaseName(locimagename);
		 		 	String limageFomat = FilenameUtils.getExtension(locimagename);
		 	    	String limageFor = "all";
		 	    	int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
	        		int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));
		        	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
		        	*/
				 }
				 }
				 } catch(Exception imger){
					 System.out.println("Exception in  image write on labor insert : "+imger);
					 log.logMessage("Exception in Image write on labor insert", "info", LaborUtility.class);
				 }finally{

				 }
			 }
			 SimpleDateFormat locSmft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cateoryJary");// category insert into
			 LaborSkillTblVO inrlocLbrskill=null;
			 log.logMessage("Labor  Category Detail insert will start.", "info", LaborUtility.class);

			 boolean dlrst=locLabrObj.deleteLabrSkillInfo(Integer.parseInt(locvrLBR_ID));
			 boolean lbrskilflg=false;
			 System.out.println("==deleteskills = "+dlrst);
			 if(dlrst){
				 if(jry!=null ){
					 for (int i = 0; i < jry.length(); i++) {
						 JSONObject rec = jry.getJSONObject(i);
						    String catid = rec.getString("cate_id");
						    String skillid = rec.getString("skill_id");
						 if(Commonutility.toCheckisNumeric(catid)){
							 lbrskilflg=true;
						 inrlocLbrskill=new LaborSkillTblVO();
						 inrlocLbrskill.setIvrBnLBR_ID(Integer.parseInt(locvrLBR_ID));
							 CategoryMasterTblVO locCateid=new CategoryMasterTblVO();
						 locCateid.setiVOCATEGORY_ID(Integer.parseInt(catid));
						 SkillMasterTblVO locskillsid=new SkillMasterTblVO();
						 locskillsid.setIvrBnSKILL_ID(Integer.parseInt(skillid));
							 //inrlocLbrskill.setIvrBnLBR_CATEG_ID(locCateid);
							 inrlocLbrskill.setiVOCATEGORY_ID(locCateid);
							 inrlocLbrskill.setIvrBnSKILL_ID(locskillsid);
							 inrlocLbrskill.setIvrBnLBR_SKILL_STS(1);
							 inrlocLbrskill.setIvrBnLBR_SKILL_ID(null);
							 inrlocLbrskill.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							int locSkilinsrtrst = locLabrObj.saveLaborSkilInfo_intRtn(inrlocLbrskill);
							inrlocLbrskill=null;
					 }
					 }
					 }
				 if(lbrskilflg){// Audit trial use only skill/Category update
					 if(Commonutility.toCheckisNumeric(locvrENTRY_BY)){
						 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, Integer.parseInt(locvrENTRY_BY));
					 }else{
						 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, 1);
					 }
				 }
			 }else{

			 }

			 if(locLbrUpdSts){
				 String filepath=rb.getString("external.mobiledbfldr.path");
				 String textvalue="update labor_cities set  CITY_NAME='"+locvrCITY_NAME+"' and CITY_ID='"+locvrLBR_CITY_ID+"'  where CITY_ID= "+locvrLBR_CITY_ID+" and STATUS=1;";
				// String textvalue="INSERT INTO labor_cities(CITY_ID,CITY_NAME,STATUS) VALUES("+locvrLBR_CITY_ID+",'"+locvrCITY_NAME+"',1);";
					if(locvrLBR_CITY_ID!=null && Commonutility.checkempty(locvrLBR_CITY_ID) && Commonutility.checkempty(locvrCITY_NAME)){
						Commonutility.TextFileWriting(filepath, textvalue);
					}
				 return "success";
			 }else{
				 return "error";
			 }
		} catch (Exception e) {
			System.out.println("Exception found in LaborUtility.toUpdtLabor : "+e);
			log.logMessage("Exception : "+e, "error", LaborUtility.class);
			return "error";
		} finally {
			 log=null; locLabrObj=null;
			 locvrLBR_SERVICE_ID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null;
			 locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;locvrLBR_COST=null;locvrLBR_COSTPER=null;locvrLBR_EXP=null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrLBR_KYC_NAME = null; locvrENTRY_BY = null;locvrID_CARD_NO=null;locvrID_CARD_TYP=null;
		}
	}


	public static String toDeActLabor(JSONObject pDataJson, Session pSession,String pAuditMsg, String pAuditCode) {
		String locvrLBR_ID=null, locvrLBR_SERVICE_ID=null;
		String locLbrUpdqry=null;
		boolean locLbrUpdSts=false;
		LaborDao locLabrObj=null;
		Log log=null;
		try {
			log=new Log();
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrid");
		/*	locvrLBR_SERVICE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrserviceid");*/
			locLbrUpdqry ="update LaborProfileTblVO set ivrBnLBR_STS=0 where ivrBnLBR_ID ="+locvrLBR_ID+" ";
			log.logMessage("Updqry : "+locLbrUpdqry, "info", LaborUtility.class);
			locLabrObj=new LaborDaoservice();
			locLbrUpdSts = locLabrObj.updateLaborInfo(locLbrUpdqry);
			 if(locLbrUpdSts){
				 return "success";
			 }else{
				 return "error";
			 }
		} catch (Exception e) {
			System.out.println("Exception found in LaborUtility.toDeActLabor : "+e);
			log.logMessage("Exception : "+e, "error", LaborUtility.class);
			return "error";
		} finally {
			 log=null; locLabrObj=null;
			 locvrLBR_ID=null; locvrLBR_SERVICE_ID=null;
		     locLbrUpdqry=null;locLbrUpdSts=false;
		}
	}


	public static String toDltLabor(JSONObject pDataJson, Session pSession) {
		try {
			return "success";
		} catch (Exception e) {
			return "error";
		} finally {

		}
	}

	public static String toBLkLabor(JSONObject pDataJson, Session pSession) {
		try {
			return "success";
		} catch (Exception e) {
			return "error";
		} finally {

		}
	}



}
