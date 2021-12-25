package com.pack.donation;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feed.FeedPost;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MvpDonationInstitutionTbl;
import com.socialindiaservices.vo.MvpDonationTbl;


public class DonationPost extends ActionSupport {
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
	FeedDAO  feadhbm=new FeedDAOService();
	FeedDAO feedService = new FeedDAOService();
	CommonMobiDao commonServ = new CommonMobiDaoService();
	public String execute() {


		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="",is_mobile=null,lvrDonationfilename=null,lvrDonationfiledata=null, visitor_imgname=null;
		int retFeedId = 0;
		FeedsTblVO feedObj = new FeedsTblVO();
		try{
			Commonutility.toWriteConsole("Step 1 : Donation create started");
			CommonUtilsDao common=new CommonUtilsDao();
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			/*fileUploadFileName="Chrysanthemum.jpg";
			fileUpload=new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg");*/
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			Common locCommonObj = null;
			if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				locCommonObj=new CommonDao();
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String category_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "category_id");
				String subcategory_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "subcategory_id");
				String title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "title");
				String quantity = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "quantity");
				String item_type = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "item_type");
				String other_desc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "other_desc");
				String desc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "desc");
				String entryby = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "entryby");
				String status = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "status");
				String DonationId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "Donation_id");
				String donate_to = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "donate_to");
				is_mobile=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");//1-mob,0-web
				if (is_mobile!=null && !is_mobile.equalsIgnoreCase("0")){
					if (Commonutility.checkempty(servicecode)) {
						if (servicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {

						} else {
						String[] passData = { getText("service.code.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
				}
				if (Commonutility.checkempty(townshipKey)) {
					if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {

					} else {
						String[] passData = { getText("townshipid.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
				}
				if (Commonutility.checkempty(societykey)) {
					if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {

					} else {
						String[] passData = { getText("society.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
				}

				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				} else {
					flg = true;
				}
				if (flg) {
					boolean result = false;
					if (is_mobile !=null && !is_mobile.equalsIgnoreCase("0")) {
						result=otp.checkTownshipKey(rid,townshipKey);
					} else {
						result = true;
					}
					if (result == true) {
						System.out.println("********result*****************"+result);
						UserMasterTblVo userMst=new UserMasterTblVo();
						if(is_mobile !=null && !is_mobile.equalsIgnoreCase("0")){
						userMst=otp.checkSocietyKeyForList(societykey,rid);
						} else {
							userMst=otp.checkUserDetails(rid);
						}
						if (userMst != null) {
							Date dt=new Date();
							int societyId=userMst.getSocietyId().getSocietyId();

							System.out.println("insert************************");
							MvpDonationTbl DonationObj=new MvpDonationTbl();
							DonationDao donationDAOobj=new DonationDaoServices();
							CategoryMasterTblVO donation_cat_obj = new CategoryMasterTblVO();
						 	donation_cat_obj.setiVOCATEGORY_ID(Integer.parseInt(category_id));
						 	DonationObj.setMvpDonationCategoryMstTbl(donation_cat_obj);
						 	SkillMasterTblVO donation_catsub_obj = new SkillMasterTblVO();
						 	donation_catsub_obj.setIvrBnSKILL_ID(Integer.parseInt(subcategory_id));
						 	DonationObj.setMvpDonationSubcategoryMstTbl(donation_catsub_obj);
						 	DonationObj.setTitle(title);;
						 	DonationObj.setQuantity(Integer.parseInt(quantity));;
						 	if(!Commonutility.toCheckisNumeric(item_type)){
						 		DonationObj.setItemType(null);
						 	}else{
						 		DonationObj.setItemType(Integer.parseInt(item_type));
						 	}

						DonationObj.setDescription(desc);
						DonationObj.setOtherDescription(other_desc);
						MvpDonationInstitutionTbl instObj= new MvpDonationInstitutionTbl();
						instObj.setInstitutionId(Integer.parseInt(donate_to));
						DonationObj.setDonateTo(instObj);
						DonationObj.setStatus(Integer.parseInt(status));;
						UserMasterTblVo userObj =new UserMasterTblVo();
						 if(!Commonutility.toCheckisNumeric(rid)){
							 DonationObj.setEntryBy(null);
						 }else{
							 userObj.setUserId(Integer.parseInt(rid));
								DonationObj.setEntryBy(userObj);
						 }


							if(DonationId.length()==0){
								//insert donation
							System.out.println("::::1 "+userMst.getSocietyId().getSocietyId());

			DonationObj.setEntryDate(common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

			System.out.println("fileUploadFileName=== "+fileUploadFileName);
							if(fileUploadFileName!=null && fileUploadFileName.length()>0){
								//mobile
								DonationObj.setImageName(fileUploadFileName);
							} else {
								DonationObj.setImageName(null);
							}
							int Donation_Id=donationDAOobj.saveDonationBookingData(DonationObj);
							String oldpath="",oldpathmob="";
							String doctempPath="",doctempPathmob="";
							String oldfile ="";
							if (Donation_Id>0) {
								//web image upload
								if (fileUploadFileName!=null && !fileUploadFileName.equalsIgnoreCase("")){
									int lneedWidth=0,lneedHeight = 0;
									String destPath = getText("external.imagesfldr.path") + "Donation/web/" + Donation_Id;
									System.out.println("destPath----------" + destPath);
									int imgHeight = mobiCommon.getImageHeight(fileUpload);
									int imgwidth = mobiCommon.getImageWidth(fileUpload);
									System.out.println("imgHeight------"+ imgHeight);
									System.out.println("imgwidth-----------"+ imgwidth);
									String imageHeightPro = getText("thump.img.height");
									String imageWidthPro = getText("thump.img.width");
									File destFile = new File(destPath,fileUploadFileName);
									FileUtils.copyFile(fileUpload, destFile);
									if (imgHeight > Integer.parseInt(imageHeightPro)) {
										lneedHeight = Integer.parseInt(imageHeightPro);
										// mobile - small image
									} else {
										lneedHeight = imgHeight;
									}
									if (imgwidth > Integer.parseInt(imageWidthPro)) {
										lneedWidth = Integer.parseInt(imageWidthPro);
									} else {
										lneedWidth = imgwidth;
									}
									System.out.println("lneedHeight-----------"+lneedHeight);
			        			   	System.out.println("lneedWidth-------------"+lneedWidth);
			        			   	String limgSourcePath=getText("external.imagesfldr.path")+"Donation/web/"+Donation_Id+"/"+fileUploadFileName;
		   		 		 			String limgDisPath = getText("external.imagesfldr.path")+"Donation/mobile/"+Donation_Id;
		   		 		 			String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
		   		 		 			String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);

		   		 		 			String limageFor = "all";
		   		 		 			ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
								} else {

								}
								//Feed table insert

								feedObj.setFeedType(4);
								feedObj.setFeedTypeId(Donation_Id);
								// get category name
								CategoryMasterTblVO cateObj = new CategoryMasterTblVO();

								feedObj.setFeedDesc(desc);
								feedObj.setFeedPrivacyFlg(2);
								feedObj.setFeedStatus(1);
								feedObj.setFeedTitle(title);
								feedObj.setEntryBy(Integer.parseInt(rid));
								feedObj.setPostBy(Integer.parseInt(rid));
								feedObj.setOriginatorId(Integer.parseInt(rid));
							//	feedObj.setAmount(fees);
								feedObj.setIsShared(0);
								// get user name  userobj
								UserMasterTblVo usrmas = new UserMasterTblVo();
								usrmas = commonServ.getProfileDetails(Integer.parseInt(rid));
								if (usrmas != null) {
									log.logMessage("Feed originater name :::" + Commonutility.toCheckNullEmpty(usrmas.getFirstName())+Commonutility.toCheckNullEmpty(usrmas.getLastName()), "info", FeedPost.class);
									feedObj.setOriginatorName(Commonutility.stringToStringempty(usrmas.getFirstName()));
									SocietyMstTbl socityObj = new SocietyMstTbl();
									socityObj.setSocietyId(usrmas.getSocietyId().getSocietyId());
									feedObj.setSocietyId(socityObj);
									if(usrmas.getCityId()!=null){
									feedObj.setFeedLocation(usrmas.getCityId().getCityName());
									}
									feedObj.setUsrId(usrmas);
								} else {
									log.logMessage("User information error :: Peronal details featch error"  , "info", FeedPost.class);
									flg =  false;
								}
								feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
								feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
								feedObj.setIsMyfeed(1);
								retFeedId = feadhbm.feedInsert(null,feedObj,null,null,null);

						serverResponse(servicecode,"00","R0224",mobiCommon.getMsg("R0224"),locObjRspdataJson,is_mobile);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0225",getText("R0025"),locObjRspdataJson,is_mobile);
					}

						}
							else if(DonationId.length() >0){
								//Update donation
								DonationObj.setDonateId(Integer.parseInt(DonationId));
								if(fileUploadFileName!=null && fileUploadFileName.length()>0){
									//mobile
									DonationObj.setImageName(fileUploadFileName);
								} else {

								}
								//boolean Donationflg=donationDAOobj.updateDonationData(DonationObj,fileUploadFileName);
								//if(Donationflg){
									if (fileUploadFileName!=null && !fileUploadFileName.equalsIgnoreCase("")){
										int lneedWidth=0,lneedHeight = 0;
										String destPath = getText("external.imagesfldr.path") + "Donation/web/" + DonationId;
										System.out.println("destPath----------" + destPath);
										int imgHeight = mobiCommon.getImageHeight(fileUpload);
										int imgwidth = mobiCommon.getImageWidth(fileUpload);
										System.out.println("imgHeight------"+ imgHeight);
										System.out.println("imgwidth-----------"+ imgwidth);
										String imageHeightPro = getText("thump.img.height");
										String imageWidthPro = getText("thump.img.width");
										File destFile = new File(destPath,fileUploadFileName);
										FileUtils.copyFile(fileUpload, destFile);
										if (imgHeight > Integer.parseInt(imageHeightPro)) {
											lneedHeight = Integer.parseInt(imageHeightPro);
											// mobile - small image
										} else {
											lneedHeight = imgHeight;
										}
										if (imgwidth > Integer.parseInt(imageWidthPro)) {
											lneedWidth = Integer.parseInt(imageWidthPro);
										} else {
											lneedWidth = imgwidth;
										}
										System.out.println("lneedHeight-----------"+lneedHeight);
				        			   	System.out.println("lneedWidth-------------"+lneedWidth);
				        			   	String limgSourcePath=getText("external.imagesfldr.path")+"Donation/web/"+DonationId+"/"+fileUploadFileName;
			   		 		 			String limgDisPath = getText("external.imagesfldr.path")+"Donation/mobile/"+DonationId;
			   		 		 			String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
			   		 		 			String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);

			   		 		 			String limageFor = "all";
			   		 		 			ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
									} else {

									}
									//time line table update
									feedObj.setUsrId(userMst);
									feedObj.setFeedType(10);
									feedObj.setFeedTypeId(DonationObj.getDonateId());
									feedObj.setFeedTitle(title);
									String tmpcont="";
									String eventdesc="";
									feedObj.setFeedDesc(tmpcont);

									feedObj.setPostBy(Integer.parseInt(rid));

									feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
									feedObj.setSocietyId(userMst.getSocietyId());
									if(userMst.getCityId()!=null){
									feedObj.setFeedLocation(userMst.getCityId().getCityName());
									}
									feedObj.setOriginatorId(Integer.parseInt(rid));
									feedObj.setEntryBy(Integer.parseInt(rid));
									feedObj.setFeedStatus(1);
									feedObj.setFeedPrivacyFlg(1);
									feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
									feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
									feedObj.setIsMyfeed(1);
									feedObj.setIsShared(0);




									flg = feedService.feedEdit(feedObj, "", null, null, null, null);
								serverResponse(servicecode,"00","R0226",mobiCommon.getMsg("R0226"),locObjRspdataJson,is_mobile);
								/*}
								else{

								}*/
							}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",getText("R0029"),locObjRspdataJson,is_mobile);
			}

			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",getText("R0015"),locObjRspdataJson,is_mobile);
			}


			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson,is_mobile);
			}
		}
			else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", DonationPost.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson,is_mobile);
		}
		}
		catch(Exception ex){
			System.out.println("=======Create Event====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", DonationPost.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson,is_mobile);
		}
		finally
		{
			lvrDonationfilename=null;
		}
		return SUCCESS;
	}


	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla )
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();
		try {
			if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
				out = response.getWriter();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				mobiCommon mobicomn=new mobiCommon();
				String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
				out.print(as);
				out.close();
			}
			else{
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