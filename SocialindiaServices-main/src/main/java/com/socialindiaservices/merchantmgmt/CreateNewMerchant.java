package com.socialindiaservices.merchantmgmt;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.DocMasterlst;
import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.labor.LaborUtility;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.login.EncDecrypt;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantCategoryTblVO;
import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantStockDetailTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class CreateNewMerchant extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchanthbm=new MerchantManageDaoServices();
	
	public String execute(){
		Log log = null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		String ivrservicecode=null;
		Integer ivrEntryByusrid = 0;
		List<Object> locObjDoclst = null;
		Session session = null;
		Transaction tx = null;
		ResourceBundle rb =  null;
		Date dat = null;
		CommonUtilsDao common = null;	
		try {
			common=new CommonUtilsDao();
			log= new Log();
			Commonutility.toWriteConsole("Step 1 : Merchant Create Called [Start]");
			log.logMessage("Step 1 : Merchant Create Called [Start]", "info", CreateNewMerchant.class);
			rb = ResourceBundle.getBundle("applicationResources");
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.marchantmstfldr")+getText("external.inner.webpath");
			String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.marchantmstfldr")+getText("external.inner.mobilepath");
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);				
				if (ivIsJson) {
					MerchantStockDetailTblVO stockDetail=new MerchantStockDetailTblVO();
					MerchantTblVO merchantObj=new MerchantTblVO();
					MerchantCategoryTblVO merchantCategoryOlbj=new MerchantCategoryTblVO();
					CountryMasterTblVO countryobj=new CountryMasterTblVO();
					StateMasterTblVO stateobj=new StateMasterTblVO();
					CityMasterTblVO cityobj=new CityMasterTblVO();
					PostalCodeMasterTblVO pstlcodeobj=new PostalCodeMasterTblVO();
					UserMasterTblVo usermastobj=new UserMasterTblVo();
					GroupMasterTblVo groupmstObj=new GroupMasterTblVo();
					MerchantIssueTblVO mechtissuetbl=null;
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"userId");
					Integer groupcode=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"usrTyp");
					if (ivrEntryByusrid != null) {
					} else {
					ivrEntryByusrid = 0;
					}
				
					Integer mrchCategoryId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"merchantCategoryId");
					String mrchntName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntName");
					String mrchntEmail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntEmail");				
					String mrchntIsdCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntIsdCode");
					String mrchntPhNo=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntPhNo");
					String keyForSearch=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"keyForSearch");
					String storeLocation=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"storeLocation");
					String storeOpentime=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"storeOpentime");
					String storeClosetime=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"storeClosetime");
					String mrchntAdd1=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntAdd1");
					String mrchntAdd2=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntAdd2");
					Integer countryId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"countryId");
					Integer stateId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"stateId");
					Integer cityId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"cityId");				
					Integer pstlId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"pstlId");
					String merchDescription=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"merchDescription");
					String website=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"website");
					String shopName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"shopName");
					String locimagename=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imagename");						
					//String locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imgencdata");
					String lvrimgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mrchimgsrchpth");
					String offerisstxt=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "offerisstxt");											
					dat = new Date();
					usermastobj.setUserId(ivrEntryByusrid);
					groupmstObj.setGroupCode(groupcode);
					merchantCategoryOlbj.setMrchCategoryId(mrchCategoryId);
					merchantObj.setMrchCategoryId(merchantCategoryOlbj);
					merchantObj.setMrchntName(mrchntName);
					merchantObj.setMrchntEmail(mrchntEmail);
					merchantObj.setMrchntIsdCode(mrchntIsdCode);
					merchantObj.setMrchntPhNo(mrchntPhNo);
					merchantObj.setKeyForSearch(keyForSearch);
					merchantObj.setStoreLocation(storeLocation);
					merchantObj.setStoreOpentime(storeOpentime);
					merchantObj.setStoreClosetime(storeClosetime);
					merchantObj.setMrchntAdd1(mrchntAdd1);
					merchantObj.setMrchntAdd2(mrchntAdd2);
					merchantObj.setShopName(shopName);
					if (Commonutility.checkempty(locimagename)) {
						locimagename = locimagename.replaceAll(" ", "_");
					}
					merchantObj.setStoreimage(locimagename);
					if (countryId != null) {
						countryobj.setCountryId(countryId);
						merchantObj.setCountryId(countryobj);
					}
					if (stateId != null) {
						stateobj.setStateId(stateId);
						merchantObj.setStateId(stateobj);
					}
					if (cityId != null) {
						cityobj.setCityId(cityId);
						merchantObj.setCityId(cityobj);
					}
					if (pstlId != null) {
						pstlcodeobj.setPstlId(pstlId);
//						merchantObj.setPstlId(pstlcodeobj);
						merchantObj.setPstlId(pstlId);
					}
					merchantObj.setMerchDescription(merchDescription);
					merchantObj.setWebsite(website);
					merchantObj.setEntryBy(usermastobj);
					merchantObj.setGroupCode(groupmstObj);
					merchantObj.setMrchntActSts(1);
					merchantObj.setEntryDatetime(dat);
					merchantObj.setModifyDatetime(dat);

				boolean ishbmsuccess = merchanthbm.insertMerchantTbl(merchantObj, session);	
				Commonutility.toWriteConsole("Step 2 : Merchant inserted Status : "+ishbmsuccess);
				log.logMessage("Step 2 : Merchant inserted Status : "+ishbmsuccess, "info", CreateNewMerchant.class);
				if(ishbmsuccess){					
						try {
						 //image write into folder
							if ((lvrimgsrchpth!=null && !lvrimgsrchpth.equalsIgnoreCase("null") && !lvrimgsrchpth.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
								Commonutility.toWriteConsole("Step 3 : Merchant image writen.");
								log.logMessage("Step 3 : Merchant image writen.", "info", CreateNewMerchant.class);
								
								//String delrs = Commonutility.todelete("", locWebImgFldrPath+merchantObj.getMrchntId()+"/");
								//String delrs_mob= Commonutility.todelete("", locMobImgFldrPath+merchantObj.getMrchntId()+"/");					  
								Commonutility.toWriteImageMobWebNewUtill(merchantObj.getMrchntId(), locimagename,lvrimgsrchpth,locWebImgFldrPath,locMobImgFldrPath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, CreateNewMerchant.class);	
								
								/*
								byte imgbytee[] = new byte[1024];
								imgbytee = Commonutility.toDecodeB64StrtoByary(locimg_encdata);
								String wrtsts = Commonutility.toByteArraytoWriteFiles(imgbytee, locWebImgFldrPath+merchantObj.getMrchntId()+"/", locimagename);
						 					 
								//mobile - small image
								String limgSourcePath=locWebImgFldrPath+merchantObj.getMrchntId()+"/"+locimagename;			
								String limgDisPath = locMobImgFldrPath+merchantObj.getMrchntId()+"/";
								String limgName = FilenameUtils.getBaseName(locimagename);
								String limageFomat = FilenameUtils.getExtension(locimagename);		 	    			 	    	 
								String limageFor = "all";
								int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
								int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
								ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile				 				 				
							*/
							} else {
								Commonutility.toWriteConsole("Step 3 : Merchant image not uploaded by creator.");
								log.logMessage("Step 3 : Merchant image not uploaded by creator.", "info", CreateNewMerchant.class);
							}
						} catch (Exception imger){
							 System.out.println("Step -1 : Exception in  image write on CreateNewMerchant insert : "+imger);
							 log.logMessage("Step -1 : Exception in Image write on CreateNewMerchant insert", "info", CreateNewMerchant.class);
						 }finally{
							 
						 }	
						
						if (mrchCategoryId != null && mrchCategoryId == 1) {					
							String typeName= (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");
							String quantity= (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"quantity");					
							if (typeName != null && typeName.contains("!_!")) {
								String[] typeeventaray=typeName.split("!_!");
								String[] quantityeventarray=quantity.split("!_!");
								for (int k = 0; k < typeeventaray.length; k++) {
								stockDetail=new MerchantStockDetailTblVO();
								stockDetail.setMrchntId(merchantObj);
								stockDetail.setMrchCategoryId(merchantCategoryOlbj);
								stockDetail.setTypeName(typeeventaray[k]);
								stockDetail.setQuantity(Integer.parseInt(quantityeventarray[k]));
								stockDetail.setEntryBy(usermastobj);
								stockDetail.setEntryDatetime(dat);
								stockDetail.setModifyDatetime(dat);
								ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
									if (!ishbmsuccess) {
										break;
									}
								}
							} else {
								if(typeName!= null && quantity!=null && quantity.length()>0 && typeName.length()>0){
									stockDetail = new MerchantStockDetailTblVO();
									stockDetail.setMrchntId(merchantObj);
									stockDetail.setMrchCategoryId(merchantCategoryOlbj);
									stockDetail.setTypeName(typeName);
									stockDetail.setQuantity(Integer.parseInt(quantity));
									stockDetail.setEntryBy(usermastobj);
									stockDetail.setEntryDatetime(dat);
									stockDetail.setModifyDatetime(dat);
									ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
								}
							}
						} else if (mrchCategoryId != null && mrchCategoryId == 2) {
							String typeName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");
							String quantity=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"quantity");
							String power = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"power");
							String cmpny = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"companyName");
							if(typeName!=null && typeName.contains("!_!")){
								String[] typeeventaray=typeName.split("!_!");
								String[] quantityeventarray=quantity.split("!_!");
								String [] lvrcmpnyName = cmpny.split("!_!");
								String [] lvrPwr = power.split("!_!");
								for(int k=0;k<typeeventaray.length;k++){
									System.out.println("typeeventaray[k]---------------"+typeeventaray[k]);
									stockDetail=new MerchantStockDetailTblVO();
									stockDetail.setMrchntId(merchantObj);
									stockDetail.setMrchCategoryId(merchantCategoryOlbj);
									stockDetail.setTypeName(typeeventaray[k]);
									stockDetail.setQuantity(Integer.parseInt(quantityeventarray[k]));
									stockDetail.setCompanyName(lvrcmpnyName[k]);
									if(Commonutility.checkempty(lvrPwr[k])){
										stockDetail.setPower(Float.parseFloat(lvrPwr[k]));
									} else {
										stockDetail.setPower(null);
									}	
									stockDetail.setEntryBy(usermastobj);
									stockDetail.setEntryDatetime(dat);
									stockDetail.setModifyDatetime(dat);
									ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
									if (!ishbmsuccess) {
										break;
									}
						
								}
							} else {
								if(typeName!= null && quantity!=null && quantity.length()>0 && typeName.length()>0){
									stockDetail=new MerchantStockDetailTblVO();
									stockDetail.setMrchntId(merchantObj);
									stockDetail.setMrchCategoryId(merchantCategoryOlbj);
									stockDetail.setTypeName(typeName);
									stockDetail.setQuantity(Integer.parseInt(quantity));
									stockDetail.setCompanyName(cmpny);
									if(Commonutility.checkempty(power)){
										stockDetail.setPower(Float.parseFloat(power));
									} else {
										stockDetail.setPower(null);
									}
									stockDetail.setEntryBy(usermastobj);
									stockDetail.setEntryDatetime(dat);
									stockDetail.setModifyDatetime(dat);
									ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
								}
						
							}
						} else if (mrchCategoryId != null && mrchCategoryId == 3) {
							String typeName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");
							String quantity=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"quantity");
							if(typeName!=null && typeName.contains("!_!")){
								String[] typeeventaray=typeName.split("!_!");
								String[] quantityeventarray=quantity.split("!_!");
								for(int k=0;k<typeeventaray.length;k++){
									System.out.println("typeeventaray[k]---------------"+typeeventaray[k]);
									stockDetail=new MerchantStockDetailTblVO();
									stockDetail.setMrchntId(merchantObj);
									stockDetail.setMrchCategoryId(merchantCategoryOlbj);
									stockDetail.setTypeName(typeeventaray[k]);
									stockDetail.setQuantity(Integer.parseInt(quantityeventarray[k]));
									stockDetail.setEntryBy(usermastobj);
									stockDetail.setEntryDatetime(dat);
									stockDetail.setModifyDatetime(dat);
									ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
									if(!ishbmsuccess){
										break;
									}						
								}
							} else {
								if(typeName!= null && quantity!=null && quantity.length()>0 && typeName.length()>0){
									stockDetail=new MerchantStockDetailTblVO();
									stockDetail.setMrchntId(merchantObj);
									stockDetail.setMrchCategoryId(merchantCategoryOlbj);
									stockDetail.setTypeName(typeName);
									stockDetail.setQuantity(Integer.parseInt(quantity));
									stockDetail.setEntryBy(usermastobj);
									stockDetail.setEntryDatetime(dat);
									stockDetail.setModifyDatetime(dat);
									ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
								}
							}
						} else if (mrchCategoryId!=null && mrchCategoryId==4){
							String cuisines = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"cuisines");
							String typeName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");					
							stockDetail.setMrchntId(merchantObj);
							stockDetail.setMrchCategoryId(merchantCategoryOlbj);
							stockDetail.setCuisines(cuisines);
							stockDetail.setTypeName(typeName);
							stockDetail.setEntryBy(usermastobj);
							stockDetail.setEntryDatetime(dat);
							stockDetail.setModifyDatetime(dat);
							ishbmsuccess = merchanthbm.insertMerchantStockDetailTbl(stockDetail,session);
						} else {
							ishbmsuccess =  true;
						}
						Commonutility.toWriteConsole("Step 4 : Merchant category updated.");
						log.logMessage("Step 4 : Merchant category updated.", "info", CreateNewMerchant.class);
						if(offerisstxt!=null && !offerisstxt.equalsIgnoreCase("null") && !offerisstxt.equalsIgnoreCase("")){
							String[] offerisst=offerisstxt.split(",");
							// Merchant Group code insert into mvp_merchant_issue_tbl
							GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
							String pGrpName = getText("Grp.merchant");
							 // Select Group Code on Merchant
							String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
							Query locQryrst=session.createQuery(locSlqry);
							lvrGrpmstobj = (GroupMasterTblVo) locQryrst.uniqueResult();
							 if(lvrGrpmstobj!=null){
								 lvrGrpmstIDobj = new GroupMasterTblVo();
								 lvrGrpmstIDobj.setGroupCode(lvrGrpmstobj.getGroupCode());								 
							 }else {
								 lvrGrpmstIDobj = null;
							 }
							for(int k=0;k<offerisst.length;k++){
								mechtissuetbl=new MerchantIssueTblVO();
						//		mechtissuetbl.setMvpMrchTblid(merchantObj);
								mechtissuetbl.setMcrctLaborId(merchantObj.getMrchntId());
								mechtissuetbl.setDescription(offerisst[k]);
								mechtissuetbl.setEntryBy(1);
								mechtissuetbl.setStatus(1);
								mechtissuetbl.setEntryDatetime(dat);
							
								mechtissuetbl.setIvrGrpcodeobj(lvrGrpmstIDobj);
								boolean issuesuss = merchanthbm.insertMerchantissueTbl(mechtissuetbl, session);	
								mechtissuetbl=null;
							
							}
						}
					}

					if (ishbmsuccess) {
						locObjRspdataJson = new JSONObject();
						locObjRspdataJson.put("datalst", locObjDoclst);
						if (tx != null) {
							tx.commit();
						}
					//Send Email
					if(merchantObj.getMrchntEmail()!=null && Commonutility.checkempty(merchantObj.getMrchntEmail())){
						EmailEngineServices emailservice = new EmailEngineDaoServices();
						EmailTemplateTblVo emailTemplate;
					
					try {
			            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Merchant' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();
			            
			            emqry = common.templateParser(emailMessage, 1, "", "");
			            Commonutility.toWriteConsole("emqry : "+emqry);
			            String[] qrySplit = emqry.split("!_!");
			            String qry = qrySplit[0] + " FROM MerchantTblVO as merch where merch.mrchntId='"+merchantObj.getMrchntId()+"'";			            
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(merchantObj.getMrchntEmail(), emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            System.out.println("Excption found in merchant creation Emailsend CreateNewMerchant.class : " + ex);
			            log.logMessage("Exception in staff admin flow emailFlow : " + ex, "error",CreateNewMerchant.class);
			            
			          }	
				
					}
					if(merchantObj.getMrchntPhNo()!=null && Commonutility.checkempty(merchantObj.getMrchntPhNo())){
						Commonutility.toWriteConsole("===================sms===========================");
						SmsTemplatepojo smsTemplate = null;
						SmsEngineServices smsService = new SmsEngineDaoServices();
						SmsInpojo sms = new SmsInpojo();
						try {
							String mailRandamNumber;
							mailRandamNumber = common.randInt(5555, 999999999);
							String qry = "FROM SmsTemplatepojo where " + "templateName ='Create Merchant' and status ='1'";
							smsTemplate = smsService.smsTemplateData(qry);
							String smsMessage = smsTemplate.getTemplateContent();
							//smsMessage = smsMessage.replace("[SOCIETY NAME]", lvrSocyname);
							qry = common.smsTemplateParser(smsMessage, 1, "", "");
							String[] qrySplit = qry.split("!_!");
							String qryform =  qrySplit[0] + " FROM MerchantTblVO as merch where merch.mrchntId='"+merchantObj.getMrchntId()+"'";
							smsMessage = smsService.smsTemplateParserChange(qryform, Integer.parseInt(qrySplit[1]), smsMessage);
							sms.setSmsCardNo(mailRandamNumber);
							sms.setSmsEntryDateTime(common.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							sms.setSmsMobNumber(merchantObj.getMrchntPhNo());
							sms.setSmspollFlag("F");
							sms.setSmsMessage(smsMessage);
							smsService.insertSmsInTable(sms);
			          } catch (Exception ex) {
			        	  Commonutility.toWriteConsole("Excption found in smssend LaborUtility.class : " + ex);
				            log.logMessage("Excption found in smssend LaborUtility.class : " + ex, "error",LaborUtility.class);
			           
			          }	finally {
			        	  
			          }
					} else {
						 Commonutility.toWriteConsole("===================No SMS [Merchant Mobile No Not found]===========================");
					}
					
					
					Commonutility.toWriteConsole("Step 5 : Merchant created [End]");
					log.logMessage("Step 5 : Merchant created [End]", "info", CreateNewMerchant.class);
					serverResponse(ivrservicecode,"00","R0118",mobiCommon.getMsg("R0118"),locObjRspdataJson);
				
				} else {
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("datalst", locObjDoclst);				
					serverResponse(ivrservicecode,"01","R0119",mobiCommon.getMsg("R0119"),locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6417, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6417","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6417, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6417","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
				if(tx!=null){
					tx.rollback();
				}
			}	
		}catch(Exception e){
			System.out.println("Exception found CreateNewMerchant.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6417, Sorry, an unhandled error occurred  : R0003 - "+mobiCommon.getMsg("R0003") +", Exception : "+e, "error", DocMasterlst.class);
			serverResponse("SI6417","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			if(tx!=null){
				tx.rollback();
			}
		}finally{
			locObjRecvJson=null;locObjRspdataJson=null;	//locObjRecvdataJson =null;
			if(session!=null){ session.flush();session.clear();session.close();session = null;}
		}				
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	

}