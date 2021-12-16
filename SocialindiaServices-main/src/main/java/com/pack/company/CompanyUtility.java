package com.pack.company;

import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.company.persistance.CompanyDao;
import com.pack.company.persistance.CompanyDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindia.generalmgnt.staffcreation;

public class CompanyUtility {
	public static String toInsrtLabor(JSONObject pDataJson, String pGrpName, String pAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath) {	// Insert
		CommonUtils locCommutillObj =null;
		CommonUtilsDao common=new CommonUtilsDao();	
		Log log=null; CompanyDao loccmpyObj=null;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null, locvrCMPY_REG_NO = null;
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_PSTL_ID=null,locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null, locvrLBR_RATING = null, locvrENTRY_BY = null;		
		String locvrLBR_ISD_CODE=null,locvrLBR_VERIFIED_STATUS=null, locvrLBR_KEY_FOR_SEARCH=null, locvrLBR_WORK_TYPE_ID=null, locvrLBR_DESCP=null;
		int locLbrID=0;
		String locimagename=null, locimg_encdata=null, lvrImgsrchpath = null;	
		ResourceBundle rb = null;
		try {
			 rb = ResourceBundle.getBundle("applicationResources");
			 locCommutillObj = new CommonUtilsDao();
			 log=new Log();
			//locvrENTRY_DATETIME=Commonutility.toGetcurrentutilldatetime("1");
			 locvrLBR_SERVICE_ID=locCommutillObj.getRandomval("aZ_09", 25);
			 locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "name");
			 locvrLBR_EMAIL = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "emailid");
			 locvrLBR_PH_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "mobno");
			 locvrCMPY_REG_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmpyregno");
			 
			 locvrLBR_ADD_1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add1");
			 locvrLBR_ADD_2 =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "add2");
			 locvrLBR_PSTL_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "postalcode");
			 locvrLBR_CITY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "city");
			 locvLBR_STATE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "sate");
			 locvrLBR_COUNTRY_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "country");
			 locvLBR_STS = "1";
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryby");
			 locvrLBR_ISD_CODE=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isdcode");
			 locvrLBR_VERIFIED_STATUS=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "verifystatus"); 
			 locvrLBR_KEY_FOR_SEARCH =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "keyforsrch");
			 locvrLBR_DESCP=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "descrp");
			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imagename");	
			 lvrImgsrchpath=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgsrchpath");	
			// locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgencdata");
			 CompanyMstTblVO locObjLbrprfvo=new CompanyMstTblVO();
			 locObjLbrprfvo.setCompanyUniqId(locvrLBR_SERVICE_ID);
			 locObjLbrprfvo.setCompanyName(locvrLBR_NAME);
			 locObjLbrprfvo.setCompanyEmail(locvrLBR_EMAIL);
			 locObjLbrprfvo.setIsdCode(locvrLBR_ISD_CODE);
			 locObjLbrprfvo.setMobileNo(locvrLBR_PH_NO);
			 locObjLbrprfvo.setCmpnyRegno(locvrCMPY_REG_NO);
			 CityMasterTblVO cityobj = new CityMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvrLBR_CITY_ID) || locvrLBR_PSTL_ID.equalsIgnoreCase("")){
				 locObjLbrprfvo.setCityId(null);
			 }else{
				 cityobj.setCityId(Integer.parseInt(locvrLBR_CITY_ID));
				 locObjLbrprfvo.setCityId(cityobj);
			 }	
			 
			 locObjLbrprfvo.setAddress1(locvrLBR_ADD_1);
			 locObjLbrprfvo.setAddress2(locvrLBR_ADD_2);			 
			 
			 PostalCodeMasterTblVO postalObj = new PostalCodeMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvrLBR_PSTL_ID)|| locvrLBR_PSTL_ID.equalsIgnoreCase("")){
				 locObjLbrprfvo.setPstlId(null);
			 }else{
				 postalObj.setPstlId(Integer.parseInt(locvrLBR_PSTL_ID));
//				 locObjLbrprfvo.setPstlId(postalObj);
				 locObjLbrprfvo.setPstlId(Integer.parseInt(locvrLBR_PSTL_ID));
			 }			 			
			 
			 StateMasterTblVO stateObj = new StateMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvLBR_STATE_ID)  || locvLBR_STATE_ID.equalsIgnoreCase("")){
				 locObjLbrprfvo.setStateId(null);
			 }else{
				 stateObj.setStateId(Integer.parseInt(locvLBR_STATE_ID));
				 locObjLbrprfvo.setStateId(stateObj);
			 }
			 //locObjLbrprfvo.setStateId(stateObj);
			 CountryMasterTblVO countryObj = new CountryMasterTblVO();
			 if(!Commonutility.toCheckisNumeric(locvrLBR_COUNTRY_ID)  || locvrLBR_COUNTRY_ID.equalsIgnoreCase("")){
				 locObjLbrprfvo.setCountryId(null);
			 }else{
				 countryObj.setCountryId(Integer.parseInt(locvrLBR_COUNTRY_ID));
				 locObjLbrprfvo.setCountryId(countryObj);
			 }			 						 
			 locObjLbrprfvo.setStatus(Integer.parseInt(locvLBR_STS));
			 if(!Commonutility.toCheckisNumeric(locvrLBR_VERIFIED_STATUS) || locvrLBR_VERIFIED_STATUS.equalsIgnoreCase("0")){
				 locObjLbrprfvo.setVerifyStatus(0);
			 }else{
				 locObjLbrprfvo.setVerifyStatus(Integer.parseInt(locvrLBR_VERIFIED_STATUS));
			 }			 
			 locObjLbrprfvo.setKeyforSrch(locvrLBR_KEY_FOR_SEARCH);			 						
			 locObjLbrprfvo.setDescr(locvrLBR_DESCP);			 						 
			 if(!Commonutility.toCheckisNumeric(locvrENTRY_BY)){
				 locObjLbrprfvo.setEntryBy(null);
			 }else{
				 locObjLbrprfvo.setEntryBy(Integer.parseInt(locvrENTRY_BY));
			 }
			 // Select Group Code on Labor	
			/* locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";			 
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
			 }*/			 			 				
			 locObjLbrprfvo.setImageNameWeb(locimagename);
			 locObjLbrprfvo.setImageNameMob(locimagename);			 			 
			 locObjLbrprfvo.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			 //----------- labor reg Start-----------
			 log.logMessage("company Detail insert will start.", "info", CompanyUtility.class);
			 loccmpyObj=new CompanyDaoservice();
			 locLbrID=loccmpyObj.saveCompanyInfo_int(locObjLbrprfvo);
			 System.out.println(locLbrID+": id compny");
			 log.logMessage("company Detail insert End compny Id : "+locLbrID, "info", CompanyUtility.class);
			 // -----------labor reg end------------			 
			 if(locLbrID!=0 && locLbrID!=-1){
				 if(Commonutility.checkempty(locimagename)){
					// String lvrWebImagpath = rb.getString("external.uploadfile.society.webpath");
					// String lvrMobImgpath = rb.getString("external.uploadfile.society.mobilepath");
					 String delrs = Commonutility.todelete("", pWebImagpath+locLbrID+"/");
					 String delrs_mob= Commonutility.todelete("", pMobImgpath+locLbrID+"/");					  
					 Commonutility.toWriteImageMobWebNewUtill(locLbrID, locimagename,lvrImgsrchpath,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, CompanyUtility.class);
				 } else {
					 
				 }
				/* try{
				 //image write into folder
				 if((locimg_encdata!=null && !locimg_encdata.equalsIgnoreCase("null") && !locimg_encdata.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
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
				 }
				 } catch(Exception imger){
					 System.out.println("Exception in  image write on company insert : "+imger);
					 log.logMessage("Exception in Image write on company insert", "info", CompanyUtility.class);
				 }finally{
					 
				 }		*/	
				 
				 	//Send Email
					if(locObjLbrprfvo.getCompanyEmail()!=null && Commonutility.checkempty(locObjLbrprfvo.getCompanyEmail())){
					EmailEngineServices emailservice = new EmailEngineDaoServices();
					EmailTemplateTblVo emailTemplate;	
					uamDao lvrUamdaosrobj = null;
					lvrUamdaosrobj = new uamDaoServices();
					String lvrSocyname = "", lvrFname="", lvrMob = "";
					if(Commonutility.toCheckisNumeric(locvrENTRY_BY)) {						
						UserMasterTblVo userInfo = null;
						userInfo = lvrUamdaosrobj.editUser(Integer.parseInt(locvrENTRY_BY));
						if (userInfo.getFirstName()!=null) {
							lvrFname = userInfo.getFirstName();
						}
						if (userInfo.getLastName()!=null) {
							lvrFname += " "+ userInfo.getLastName();
						}
						lvrMob = userInfo.getMobileNo();
						if (userInfo.getSocietyId()!=null) {
							lvrSocyname = userInfo.getSocietyId().getSocietyName();
						} else {
							lvrSocyname = "SOCYTEA";
						}
					}													
					
					try {
			            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Company' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();
			            if (Commonutility.checkempty(lvrSocyname)) {
			            	emailMessage = emailMessage.replace("[SOCIETY NAME]", lvrSocyname);
			            } else {
			            	emailMessage = emailMessage.replace("[SOCIETY NAME]", "SOCYTEA");
			            }
			            emqry = common.templateParser(emailMessage, 1, "", "");
			            String[] qrySplit = emqry.split("!_!");
			            String qry = qrySplit[0] + " FROM CompanyMstTblVO as company where company.mobileNo='"+locObjLbrprfvo.getMobileNo()+"'";
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(locObjLbrprfvo.getCompanyEmail(), emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            System.out.println("Excption found in company creation Emailsend comapnycreation.class : " + ex);
			            log.logMessage("Exception in staff admin flow emailFlow : " + ex, "error",staffcreation.class);
			            
			          }	
				
					}
				 return "success!_!"+locLbrID;
			 }else{
				 return "error!_!"+locLbrID;
			 }			 			 		
		} catch (Exception e) {
			System.out.println("Exception found in CompanyUtility.toInsrtLabor : "+e);
			log.logMessage("Exception : "+e, "error", CompanyUtility.class);
			locLbrID=0;
			return "error!_!"+locLbrID;
		} finally {
			 locCommutillObj =null; loccmpyObj=null;
			 locvrLBR_SERVICE_ID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null; locvrLBR_PH_NO = null; locvrCMPY_REG_NO = null;
			 locvrID_CARD_NO = null; locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrLBR_KYC_NAME = null; locvrLBR_RATING = null; locvrENTRY_BY = null;		
			rb = null;
		}
		
	}
	
	public static JSONObject toSltSingleCompanyDtl(JSONObject pDataJson, Session pSession, String pAuditMsg, String pAuditCode) {// Select on single user data
		String lvrCmpnyid = null, locvrLBR_STS = null; 		
		CompanyMstTblVO lvrCmpyTblvoObj = null;
		String loc_slQry=null;
		Query locQryObj= null;
		JSONObject locRtnDataJSON=null;	
		Log log=null;
		try {
			log=new Log();			
			Commonutility.toWriteConsole("Step 1 : company detail block enter");
			log.logMessage("Step 1 : Select company detail block enter", "info", CompanyUtility.class);
			
			locRtnDataJSON=new JSONObject();
			lvrCmpnyid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmpyid");
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmpystatus");
						
			//loc_slQry="from LaborProfileTblVO where ivrBnLBR_ID="+locvrLBR_ID+" and ivrBnLBR_SERVICE_ID='"+locvrLBR_SERVICE_ID+"' and ivrBnLBR_STS="+locvrLBR_STS+"";			
			loc_slQry="from CompanyMstTblVO where companyId="+lvrCmpnyid+"  and status="+locvrLBR_STS+"";
			locQryObj=pSession.createQuery(loc_slQry);			
			//locLPTblvoObj=(LaborProfileTblVO) locQryObj.uniqueResult();
			lvrCmpyTblvoObj = (CompanyMstTblVO)locQryObj.uniqueResult();;
			Commonutility.toWriteConsole("Step 2 : Select company detail query executed.");
			
			locRtnDataJSON.put("intv_cmpy_id", lvrCmpyTblvoObj.getCompanyId());
			locRtnDataJSON.put("strv_cmpy_serviceid", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getCompanyUniqId()));
			locRtnDataJSON.put("strv_cmpy_regno", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getCmpnyRegno()));
			locRtnDataJSON.put("strv_cmpy_name", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getCompanyName()));
			locRtnDataJSON.put("strv_cmpy_email", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getCompanyEmail()));
			locRtnDataJSON.put("strv_cmpy_isdcode",Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getIsdCode()));
			locRtnDataJSON.put("strv_cmpy_phno", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getMobileNo()));
			locRtnDataJSON.put("strv_cmpy_add1", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getAddress1()));
			locRtnDataJSON.put("strv_cmpy_add2", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getAddress2()));
			
			if(lvrCmpyTblvoObj.getPstlId()!=null){
							
//				locRtnDataJSON.put("intv_cmpy_pstlid",String.valueOf(lvrCmpyTblvoObj.getPstlId().getPstlId()));
//				locRtnDataJSON.put("cmpy_pincodeName",lvrCmpyTblvoObj.getPstlId().getPstlCode());
//				locRtnDataJSON.put("intv_cmpy_cityid",String.valueOf(lvrCmpyTblvoObj.getPstlId().getCityId().getCityId()));
//				locRtnDataJSON.put("cmpy_cityName",lvrCmpyTblvoObj.getPstlId().getCityId().getCityName());
//				locRtnDataJSON.put("intv_cmpy_stateid",String.valueOf(lvrCmpyTblvoObj.getPstlId().getCityId().getStateId().getStateId()));
//				locRtnDataJSON.put("cmpy_stateName",lvrCmpyTblvoObj.getPstlId().getCityId().getStateId().getStateName());
//				locRtnDataJSON.put("intv_cmpy_cntry",String.valueOf(lvrCmpyTblvoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//				locRtnDataJSON.put("cmpy_cntryName",lvrCmpyTblvoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());		
				
				locRtnDataJSON.put("intv_cmpy_pstlid",String.valueOf(lvrCmpyTblvoObj.getPstlId()));
				locRtnDataJSON.put("cmpy_pincodeName",lvrCmpyTblvoObj.getPstlId());
			}else{
				locRtnDataJSON.put("intv_cmpy_pstlid","");
				locRtnDataJSON.put("cmpy_pincodeName","");
//				locRtnDataJSON.put("intv_cmpy_cityid","");
//				locRtnDataJSON.put("cmpy_cityName","");
//				locRtnDataJSON.put("intv_cmpy_stateid","");
//				locRtnDataJSON.put("cmpy_stateName","");
//				locRtnDataJSON.put("intv_cmpy_cntry","");
//				locRtnDataJSON.put("cmpy_cntryName","");
			}
			

			if(lvrCmpyTblvoObj.getCityId() != null){
			locRtnDataJSON.put("intv_cmpy_cityid",String.valueOf(lvrCmpyTblvoObj.getCityId().getCityId()));
			locRtnDataJSON.put("cmpy_cityName",lvrCmpyTblvoObj.getCityId().getCityName());
			
				if(lvrCmpyTblvoObj.getCityId().getStateId() != null){
				locRtnDataJSON.put("intv_cmpy_stateid",String.valueOf(lvrCmpyTblvoObj.getCityId().getStateId().getStateId()));
				locRtnDataJSON.put("cmpy_stateName",lvrCmpyTblvoObj.getCityId().getStateId().getStateName());
					
					if(lvrCmpyTblvoObj.getCityId().getStateId().getCountryId() != null){
					locRtnDataJSON.put("intv_cmpy_cntry",String.valueOf(lvrCmpyTblvoObj.getCityId().getStateId().getCountryId().getCountryId()));
					locRtnDataJSON.put("cmpy_cntryName",lvrCmpyTblvoObj.getCityId().getStateId().getCountryId().getCountryName());	
					}
					else{
						locRtnDataJSON.put("intv_cmpy_cntry","");
						locRtnDataJSON.put("cmpy_cntryName","");
					}
				}
				else{
					locRtnDataJSON.put("intv_cmpy_stateid","");
					locRtnDataJSON.put("cmpy_stateName","");
					locRtnDataJSON.put("intv_cmpy_cntry","");
					locRtnDataJSON.put("cmpy_cntryName","");
				}
			}
			else{
				locRtnDataJSON.put("intv_cmpy_cityid","");
				locRtnDataJSON.put("cmpy_cityName","");
				locRtnDataJSON.put("intv_cmpy_stateid","");
				locRtnDataJSON.put("cmpy_stateName","");
				locRtnDataJSON.put("intv_cmpy_cntry","");
				locRtnDataJSON.put("cmpy_cntryName","");
			}
					
			locRtnDataJSON.put("str_cmpy_keyfrsrch",Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getKeyforSrch()));
			locRtnDataJSON.put("str_cmpy_descp",Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getDescr()));
			locRtnDataJSON.put("intv_cmpy_verifysts", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getVerifyStatus()));
			
			locRtnDataJSON.put("str_cmpy_webimage", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getImageNameWeb()));
			locRtnDataJSON.put("str_cmpy_mobileimage", Commonutility.toCheckNullEmpty(lvrCmpyTblvoObj.getImageNameMob()));
			log.logMessage("Step 2: Select company categoey detail block start.", "info", CompanyUtility.class);
			
		
			log.logMessage("Step 3: Select company categoey detail block end.", "info", CompanyUtility.class);
			
			Commonutility.toWriteConsole("Step 5 : Return JSON DATA : "+locRtnDataJSON);						
			Commonutility.toWriteConsole("Step 6 : Select company detail block end.");
			log.logMessage("Step 4: Select company detail block end.", "info", CompanyUtility.class);			
			return locRtnDataJSON;
		} catch (Exception e) {
			try{
				Commonutility.toWriteConsole("Step -1 : Select company detail Exception found in CompanyUtility.toSltSingleLabourDtl : "+e);
				log.logMessage("Exception : "+e, "error", CompanyUtility.class);
				locRtnDataJSON=new JSONObject();
				locRtnDataJSON.put("catch", "Error");
			}catch(Exception ee){}
			return locRtnDataJSON;
		} finally {
			lvrCmpnyid=null; lvrCmpyTblvoObj = null; loc_slQry=null; locRtnDataJSON=null;log=null;			 			
		}
	}
	
	public static String toUpdtCompany(JSONObject pDataJson,String pAuditMsg, String pAuditCode,String pWebImagpath, String pMobImgpath) {// Update
		//JSONObject locRtnJson=null;
		Log log=null;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null,locvrLBR_EMAIL=null,locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null;
		String locvLBR_STATE_ID = null,locvrLBR_PSTL_ID=null,locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null;
		String locvrLBR_RATING = null, locvrENTRY_BY = null;
		String locvrLBR_ISD_CODE=null,locvrLBR_VERIFIED_STATUS=null, locvrLBR_KEY_FOR_SEARCH=null, locvrLBR_WORK_TYPE_ID=null, locvrLBR_DESCP=null;
		String locvrLBR_ID=null,locvrLBR_Image_Mobile=null,locvrLBR_Image_web=null;
		String locimagename=null, locimg_encdata=null, lvrImgsrchpath = null;
		String locLbrUpdqry="";
		boolean locLbrUpdSts=false;
		 CompanyDao locLabrObj=null;ResourceBundle rb = null;
		try {
			 rb = ResourceBundle.getBundle("applicationResources");
			 log=new Log();
			 locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmpyid");
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
			 locvrLBR_RATING = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "rating");
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryby");
			 
			 locvrLBR_ISD_CODE=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isdcode");
			 locvrLBR_VERIFIED_STATUS=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "verifystatus"); 
			 locvrLBR_KEY_FOR_SEARCH =(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "keyforsrch");
			 locvrLBR_WORK_TYPE_ID=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "wktypid");
			 locvrLBR_DESCP=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "descrp");
			 			 						 
			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imagename");						
			 locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgencdata");
			 lvrImgsrchpath=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgsrchpath");	
			 
			 locvrLBR_Image_web=locimagename;
			 locvrLBR_Image_Mobile=locimagename;
			 
			 locLbrUpdqry ="update CompanyMstTblVO set companyName='"+locvrLBR_NAME+"', companyEmail ='"+locvrLBR_EMAIL+"',address1='"+locvrLBR_ADD_1+"',address2='"+locvrLBR_ADD_2+"',";
			
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
			 
			 			 			 
			 if(!Commonutility.toCheckisNumeric(locvrLBR_VERIFIED_STATUS) || locvrLBR_VERIFIED_STATUS.equalsIgnoreCase("0")){
				 locLbrUpdqry+="verifyStatus= "+0+",";
			 }else{
				 locLbrUpdqry+="verifyStatus= "+Integer.parseInt(locvrLBR_VERIFIED_STATUS)+",";	
			 }
			 
			 locLbrUpdqry+="keyforSrch= '"+locvrLBR_KEY_FOR_SEARCH+"',";			 
			 
			 			 
			 if(!Commonutility.toCheckisNumeric(locvLBR_STS) || locvLBR_STS.equalsIgnoreCase("0")){
				 locLbrUpdqry+="status= "+locvLBR_STS+",";
			 }else{
				 locLbrUpdqry+="status= "+Integer.parseInt(locvLBR_STS)+",";				
			 }
			 
			 if(locvrLBR_Image_web!=null && !locvrLBR_Image_web.equalsIgnoreCase("null") && !locvrLBR_Image_web.equalsIgnoreCase("")){
				 locLbrUpdqry+="imageNameWeb='"+locvrLBR_Image_web+"',";
			 }
			 if(locvrLBR_Image_Mobile!=null && !locvrLBR_Image_Mobile.equalsIgnoreCase("null") && !locvrLBR_Image_Mobile.equalsIgnoreCase("")){
				 locLbrUpdqry+="imageNameMob='"+locvrLBR_Image_Mobile+"',";
			 }
			 
			 locLbrUpdqry+="descr= '"+locvrLBR_DESCP+"',";				 			 			
			 locLbrUpdqry+="entryBy ="+locvrENTRY_BY+"  where companyId ="+locvrLBR_ID+"";			 
			//locLbrUpdqry ="update mvp_cmpy_reg_tbl set LBR_NAME='"+locvrLBR_NAME+"', LBR_EMAIL ='"+locvrLBR_EMAIL+"',LBR_ADD_1='"+locvrLBR_ADD_1+"',LBR_ADD_2='"+locvrLBR_ADD_2+"'," +
			//"LBR_PSTL_ID ="+locvrLBR_PSTL_ID+",LBR_CITY_ID ="+locvrLBR_CITY_ID+", LBR_STATE_ID ="+locvLBR_STATE_ID+",LBR_COUNTRY_ID ="+locvrLBR_COUNTRY_ID+"," +
			//"LBR_KYC_NAME ='"+locvrLBR_KYC_NAME+"',LBR_RATING ="+locvrLBR_RATING+",ENTRY_BY ="+locvrENTRY_BY+" where locvrLBR_ID ="+locvrLBR_ID+"";
			 log.logMessage("Updqry : "+locLbrUpdqry, "info", CompanyUtility.class);
			 locLabrObj=new CompanyDaoservice();
			 locLbrUpdSts = locLabrObj.updateCompanyInfo(locLbrUpdqry);			 						
			 if(Commonutility.toCheckNullEmpty(locvrLBR_ID)!=null && !Commonutility.toCheckNullEmpty(locvrLBR_ID).equalsIgnoreCase("null")){
				
				 if(Commonutility.checkempty(locimagename)){
						// String lvrWebImagpath = rb.getString("external.uploadfile.society.webpath");
						// String lvrMobImgpath = rb.getString("external.uploadfile.society.mobilepath");
						 String delrs = Commonutility.todelete("", pWebImagpath+locvrLBR_ID+"/");
						 String delrs_mob= Commonutility.todelete("", pMobImgpath+locvrLBR_ID+"/");					  
						 Commonutility.toWriteImageMobWebNewUtill(Commonutility.stringToInteger(locvrLBR_ID), locimagename,lvrImgsrchpath,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, CompanyUtility.class);
					 } else {
						 
					 }				
				 /*try{
				 //image write into folder
				 if((locimg_encdata!=null && !locimg_encdata.equalsIgnoreCase("null") && !locimg_encdata.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
					 
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
				 }
				 } catch(Exception imger){
					 System.out.println("Exception in  image write on company insert : "+imger);
					 log.logMessage("Exception in Image write on company insert", "info", CompanyUtility.class);
				 }finally{
					 
				 }	*/
			 }			
		
			if (locLbrUpdSts) {
				return "success";
			} else {
				return "error";
			}	
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception found in CompanyUtility.toUpdtcompany : "+e);
			log.logMessage("Exception : "+e, "error", CompanyUtility.class);
			return "error";						
		} finally {
			 log=null; locLabrObj=null;
			 locvrLBR_SERVICE_ID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null;
			 locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrLBR_KYC_NAME = null; locvrLBR_RATING = null; locvrENTRY_BY = null;
			 rb = null;
		}		
	}
	
	
	public static String toDeActLabor(JSONObject pDataJson, Session pSession,String pAuditMsg, String pAuditCode) {
		String locvrLBR_ID=null, locvrLBR_SERVICE_ID=null; 		
		String loccmpyUpdqry=null;		
		boolean locCmpyUpdSts=false;
		CompanyDao locCmpyObj=null;
		Log log=null;
		try {
			log=new Log();
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmpyid");
			loccmpyUpdqry ="update CompanyMstTblVO set status=0 where companyId ="+locvrLBR_ID+"";
			log.logMessage("Updqry : "+loccmpyUpdqry, "info", CompanyUtility.class);
			locCmpyObj=new CompanyDaoservice();
			locCmpyUpdSts = locCmpyObj.updateCompanyInfo(loccmpyUpdqry);
			 if(locCmpyUpdSts){
				 return "success";
			 }else{
				 return "error";
			 }	
		} catch (Exception e) {
			System.out.println("Exception found in CompanyUtility.toDeActLabor : "+e);
			log.logMessage("Exception : "+e, "error", CompanyUtility.class);
			return "error";
		} finally {
			 log=null; locCmpyObj=null;
			 locvrLBR_ID=null; 	
			 loccmpyUpdqry=null;locCmpyUpdSts=false;				
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
