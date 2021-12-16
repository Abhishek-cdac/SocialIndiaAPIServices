package com.pack.donation;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.commonvo.PrivacyInfoTblVO;
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.feed.FeedPost;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpDonationAttachTblVo;
import com.socialindiaservices.vo.MvpDonationTbl;
import com.socialindiaservices.vo.MvpDonationInstitutionTbl;

public class DonationAddEdit extends ActionSupport {
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();
	JsonpackDao jsonPack = new JsonpackDaoService();
	PrivacyInfoTblVO privacyDetail = new PrivacyInfoTblVO();
	public String execute() {
		/*fileUpload.add(new File("F://USER DATA//Desktop//sundial-c71v_5.jpg"));
		fileUploadFileName.add("sundial-c71v_5.jpg");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
		fileUploadFileName.add("Wildlife.wmv");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.jpg"));
		fileUploadFileName.add("Wildlife.jpeg");*/
		/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Desert.jpg"));
		fileUploadFileName.add("Desert.jpg");*/
		
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int donationId = 0;
			int skillId = 0;
			int addEdit = 0;
			int feedId = 0;
			String locRid = null;
			String loccategoryId = null;
			String locSubcategoryId = null;
			String locTitle = null;
			String locQuantity = null;
			String locItemtype = null;
			//String locOtherDesc = null;
			String locDesc = null;
			String locEntryby = null;
			String locDonationId = null;
			String locDonateto = null;
			String locFeedId = null;
			String locAuthor = null;
			String locPages = null;
			String locPublisher = null;
			String locRatings = null;
			String locAmount = null;
			JSONArray removeAttachJsnarrObj = new JSONArray();
			log.logMessage("Enter into DonationAddEdit ", "info", DonationAddEdit.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("DonationAddEdit ivrparams :" + ivrparams, "info", DonationAddEdit.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
						loccategoryId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "category_id");
						locSubcategoryId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "subcategory_id");
						locTitle = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "title");
						locQuantity = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "quantity");
						locItemtype = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "item_type");
						//locOtherDesc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "other_desc");
						locDesc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "desc");
						//locEntryby = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "entryby");
						locDonationId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "Donation_id");
						locDonateto = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "donate_to");
						locFeedId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
						locAuthor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "authour");
						locPages = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"pages");
						locPublisher = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "publisher");
						locRatings = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"ratings");
						locAmount = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "amount");
						removeAttachJsnarrObj = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"remove_attach");
					}
					if (Commonutility.checkempty(ivrservicecode)) {
						if (ivrservicecode.length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
							
						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.error")));
					}					
					if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					
					if (Commonutility.checkempty(societyKey)) {
						if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {
							
						} else {
							String[] passData = { getText("societykey.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}				
					
					if (locObjRecvdataJson !=null) {					
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
							if (rid !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}
						if (Commonutility.checknull(locDonationId)) {
							if (Commonutility.checkLengthNotZero(locDonationId)) {
								if (Commonutility.toCheckisNumeric(locDonationId)) {
									donationId = Commonutility.stringToInteger(locDonationId);
									if (donationId !=0 ) {
										
									} else {
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("donation.id.error")));
									}
									
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("donation.id.error")));
								}
							}
						}
						if (Commonutility.checknull(locFeedId)) {
							if (Commonutility.checkLengthNotZero(locFeedId)) {
								feedId = Commonutility.stringToInteger(locFeedId);
								if (feedId !=0) {
									
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
								}
							}
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("DonationAddEdit flg :" +flg, "info", DonationAddEdit.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						int entrybyUsr = rid;
						DonationDao donationDAOobj = new DonationDaoServices();
						FeedDAO feedService = new FeedDAOService();
						if (donationId == 0 ) {
							addEdit = 1;
							float amtval = 0.0f;
							MvpDonationTbl DonationObj=new MvpDonationTbl();
							FeedsTblVO feedObj = new FeedsTblVO();
							UserMasterTblVo userobj = new UserMasterTblVo();
							userobj.setUserId(rid);
							DonationObj.setUserId(userobj);
							
							if (Commonutility.stringToInteger(loccategoryId) != 0) {
							CategoryMasterTblVO donation_cat_obj = new CategoryMasterTblVO();
							donation_cat_obj.setiVOCATEGORY_ID(Commonutility.stringToInteger(loccategoryId));
							DonationObj.setMvpDonationCategoryMstTbl(donation_cat_obj);
							}
							
							if (Commonutility.stringToInteger(locSubcategoryId) != 0) {
							SkillMasterTblVO donation_catsub_obj = new SkillMasterTblVO();
							donation_catsub_obj.setIvrBnSKILL_ID(Commonutility.stringToInteger(locSubcategoryId));
							DonationObj.setMvpDonationSubcategoryMstTbl(donation_catsub_obj);
							}
							
							DonationObj.setTitle(locTitle);
							DonationObj.setQuantity(Commonutility.stringToInteger(locQuantity));
							if (!Commonutility.toCheckisNumeric(locItemtype)) {
								DonationObj.setItemType(null);
							} else {
								DonationObj.setItemType(Commonutility.stringToInteger(locItemtype));
							}
							DonationObj.setDescription(locDesc);
							String bookDetails = "";
							if (Commonutility.checkempty(locAuthor)) {
								bookDetails += locAuthor+"!_!";
							} else {
								bookDetails += "!_!";
							}
							if (Commonutility.checkempty(locPages)) {
								bookDetails += locPages+"!_!";
							} else {
								bookDetails += "!_!";
							}
							if (Commonutility.checkempty(locPublisher)) {
								bookDetails += locPublisher+"!_!";
							} else {
								bookDetails += "!_!";
							}
							if (Commonutility.checkempty(locRatings)) {
								bookDetails += locRatings+"!_!";
							} else {
								bookDetails += "!_!";
							}
							DonationObj.setOtherDescription(bookDetails);
							
							MvpDonationInstitutionTbl instObj = new MvpDonationInstitutionTbl();
							instObj.setInstitutionId(Commonutility.stringToInteger(locDonateto));
							DonationObj.setDonateTo(instObj);
							
							DonationObj.setStatus(1);
							DonationObj.setEntryBy(userobj);
							DonationObj.setEntryDate(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							DonationObj.setModifyDate(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							if (Commonutility.checkempty(locAmount)) {
								amtval = Commonutility.stringToFloat(locAmount);
								DonationObj.setAmount(amtval);
							}
							int donationUniqId = donationDAOobj.saveDonationBookingData(DonationObj);
							boolean fileAttachFlg = true;
							log.logMessage("donationUniqId :::" + donationUniqId , "info", DonationAddEdit.class);
							if (donationUniqId != -1) {
								System.out.println("fileUpload.size()---------" + fileUpload.size());
								if(fileUpload.size() > 0) {
									fileAttachFlg = donationDAOobj.donationAttachInsert(donationUniqId,fileUpload,fileUploadContentType,fileUploadFileName);
									System.out.println("fileAttachFlg---------" + fileAttachFlg);
						         }
								//Feed table insert
								feedObj.setFeedType(4);
								feedObj.setFeedTypeId(donationUniqId);
								// get category name
								CategoryMasterTblVO cateObj = new CategoryMasterTblVO();
								if (Commonutility.checkempty(loccategoryId)) {
									log.logMessage("Feed category name :::" + Commonutility.stringToInteger(loccategoryId) , "info", DonationAddEdit.class);
									cateObj = commonServ.getCategoryDetails(Commonutility.stringToInteger(loccategoryId));
									feedObj.setFeedCategory(cateObj.getCategoryDescription());
								} else {
									feedObj.setFeedCategory("");
								}
								feedObj.setFeedDesc(locDesc);
								privacyDetail = new PrivacyInfoTblVO();
								privacyDetail = commonServ.getDefaultPrivacyFlg(rid);
								if (privacyDetail != null) {
									feedObj.setFeedPrivacyFlg(privacyDetail.getPrivacyFlg());
								} else {
									feedObj.setFeedPrivacyFlg(2);
								}
								feedObj.setFeedStatus(1);
								feedObj.setFeedTitle(locTitle);
								feedObj.setEntryBy(rid);
								feedObj.setPostBy(rid);
								feedObj.setOriginatorId(rid);
								if (Commonutility.checkempty(locAmount)) {
									feedObj.setAmount(amtval);
								}
								feedObj.setIsShared(0);
								// get user name  userobj
								UserMasterTblVo usrmas = new UserMasterTblVo();
								usrmas = commonServ.getProfileDetails(rid);
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
								}
								feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
								feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
								feedObj.setIsMyfeed(1);
								
								int retFeedId = feedService.feedInsert("", feedObj, fileUpload,fileUploadContentType,fileUploadFileName);
								log.logMessage("DonationAddEdit Feed retFeedId :::" + retFeedId , "info", DonationAddEdit.class);
								if (retFeedId != -1 && flg && fileAttachFlg) {
									boolean additionUpdate = false;
									
									/*Feed id update into Donation Table*/
									MvpDonationTbl donationEditObj = new MvpDonationTbl();
									donationEditObj.setDonateId(donationUniqId);
									donationEditObj.setModifyDate(Commonutility.enteyUpdateInsertDateTime());
									FeedsTblVO updateFeedObj = new FeedsTblVO();
									updateFeedObj.setFeedId(retFeedId);
									donationEditObj.setFeedId(updateFeedObj);
									boolean chk = donationDAOobj.updateDonationData(donationEditObj);
									log.logMessage("DonationAddEdi tchk update flg  :::" + chk , "info", DonationAddEdit.class);
									
									/*Get PublishSkill info*/
									MvpDonationTbl donationGetObj = new MvpDonationTbl();
									donationGetObj = donationDAOobj.getDonationData(donationUniqId,rid);
									System.out.println("Before additional data insert--------1----------" + donationGetObj);
									if (chk && donationGetObj != null) {
										/*Additional data insert*/
										System.out.println("Enter additional data insert--------2----------");
										JSONObject jsonPublishObj = new JSONObject();
										jsonPublishObj = jsonPack.donationAdditonalJasonpackValues(donationGetObj);
										System.out.println("jsonPublishObj additional data insert--------3----------" + jsonPublishObj);
										if (jsonPublishObj != null) {
											System.out.println("jsonPublishObj additional data insert--------4----------" + jsonPublishObj.toString());
											ComplaintsDao complains=new ComplaintsDaoServices();
											additionUpdate = complains.additionalFeedUpdate(jsonPublishObj,retFeedId);
											System.out.println("additionUpdate additional data insert--------5----------" + additionUpdate);
										} else {
											jsonPublishObj = new JSONObject();
											additionUpdate = false;
										}
										System.out.println("additionUpdate additional data insert--------6----------" + additionUpdate);
										if (additionUpdate) {
											String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath");
											String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
											String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
											String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
											String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
											List<Object[]> feedListObj = new ArrayList<Object[]>();
											feedListObj = feedService.feedDetailsProc(rid, societyKey, retFeedId,"");								
											Object[] objList;
											for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
												objList = it.next();									
												if (objList != null) {
													locObjRspdataJson = jsonPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
													if (objList[0]!=null) {
														System.out.println((int)objList[0]);
													}
												}
											}
										} else {
											locObjRspdataJson=new JSONObject();
										}
									} else {
										flg = false;
									}
								} else {
									flg = false;
								}
							} else {
								flg = false;
							}
						} else if (donationId != 0 ) {
							// publishSkillId feedId
							addEdit = 2;
							if (donationId != 0 && feedId != 0) {
								System.out.println("removeAttachIdsObj obj------------------- " + removeAttachJsnarrObj);
								int getQryCnt = 0;
								if (removeAttachJsnarrObj != null) {
									String attachId=null;
									MvpDonationAttachTblVo doantaionAttach = new MvpDonationAttachTblVo();
									mobiCommon mobCom=new mobiCommon();
									for(int i=0;i<removeAttachJsnarrObj.length();i++) {
										attachId=null;
										attachId=removeAttachJsnarrObj.getString(i);
										doantaionAttach = donationDAOobj.getDonationAttachData(attachId);
										if (doantaionAttach.getFileType()==2) {
										  String videoPath = getText("external.imagesfldr.path")+"donation/videos/"+donationId;
										  boolean delFlag= mobCom.deleteIfFileExist(videoPath, doantaionAttach.getAttachName());
										  String thumbpath = getText("external.imagesfldr.path")+"donation/thumbimage/"+donationId;
										  delFlag=mobCom.deleteIfFileExist(thumbpath, doantaionAttach.getThumbImage());
										} else if(doantaionAttach.getFileType()==1){
										  String webPath = getText("external.imagesfldr.path")+"donation/web/"+donationId;
										  boolean delFlag=mobCom.deleteIfFileExist(webPath, doantaionAttach.getAttachName());
				 		 		 		  String mobilePath = getText("external.imagesfldr.path")+"donation/mobile/"+donationId;
				 		 		 		  delFlag= mobCom.deleteIfFileExist(mobilePath, doantaionAttach.getAttachName());
										} else {
										  String webPath = getText("external.imagesfldr.path")+"donation/web/"+donationId;
										  boolean delFlag=mobCom.deleteIfFileExist(webPath, doantaionAttach.getAttachName());
					 		 		 	  String mobilePath = getText("external.imagesfldr.path")+"donation/mobile/"+donationId;
					 		 		 	  delFlag=mobCom.deleteIfFileExist(mobilePath, doantaionAttach.getAttachName());
										}
									boolean deleteAttach = donationDAOobj.deleteDonationAttach(attachId);
									}
								}
								UserMasterTblVo userEditobj = new UserMasterTblVo();
								userEditobj.setUserId(rid);
								MvpDonationTbl DonationEditObj=new MvpDonationTbl();
								FeedsTblVO feedEditObj = new FeedsTblVO();
								DonationEditObj.setDonateId(donationId);
								if (Commonutility.stringToInteger(loccategoryId) != 0) {
									CategoryMasterTblVO donation_cat_obj = new CategoryMasterTblVO();
									donation_cat_obj.setiVOCATEGORY_ID(Commonutility.stringToInteger(loccategoryId));
									DonationEditObj.setMvpDonationCategoryMstTbl(donation_cat_obj);
									// for feed get category name
									CategoryMasterTblVO cateObj = new CategoryMasterTblVO();
									if (Commonutility.checkempty(loccategoryId)) {
										log.logMessage("Feed category name :::" + Commonutility.stringToInteger(loccategoryId) , "info", DonationAddEdit.class);
										cateObj = commonServ.getCategoryDetails(Commonutility.stringToInteger(loccategoryId));
										feedEditObj.setFeedCategory(cateObj.getCategoryDescription());
									} else {
										feedEditObj.setFeedCategory("");
									}
								}
								if (Commonutility.stringToInteger(locSubcategoryId) != 0) {
									SkillMasterTblVO donation_catsub_obj = new SkillMasterTblVO();
									donation_catsub_obj.setIvrBnSKILL_ID(Commonutility.stringToInteger(locSubcategoryId));
									DonationEditObj.setMvpDonationSubcategoryMstTbl(donation_catsub_obj);
								}
								if (Commonutility.checkempty(locTitle)) {
									DonationEditObj.setTitle(locTitle);
									// for feed update
									feedEditObj.setFeedTitle(locTitle);								
								}
								if (Commonutility.checkempty(locQuantity)) {
									DonationEditObj.setQuantity(Commonutility.stringToInteger(locQuantity));
								}
								if (Commonutility.checkempty(locItemtype)) {
									DonationEditObj.setItemType(Commonutility.stringToInteger(locItemtype));
								}
								if (Commonutility.checkempty(locDesc)) {
									DonationEditObj.setDescription(locDesc);
									// for feed update
									feedEditObj.setFeedDesc(locDesc);
								}
								
								String bookDetails = "";
								if (Commonutility.checkempty(locAuthor)) {
									bookDetails += locAuthor+"!_!";
								} else {
									bookDetails += "!_!";
								}
								if (Commonutility.checkempty(locPages)) {
									bookDetails += locPages+"!_!";
								} else {
									bookDetails += "!_!";
								}
								if (Commonutility.checkempty(locPublisher)) {
									bookDetails += locPublisher+"!_!";
								} else {
									bookDetails += "!_!";
								}
								if (Commonutility.checkempty(locRatings)) {
									bookDetails += locRatings+"!_!";
								} else {
									bookDetails += "!_!";
								}
								DonationEditObj.setOtherDescription(bookDetails);
								if (Commonutility.checkempty(locDonateto)) {
									MvpDonationInstitutionTbl instObj = new MvpDonationInstitutionTbl();
									instObj.setInstitutionId(Commonutility.stringToInteger(locDonateto));
									DonationEditObj.setDonateTo(instObj);
								}
								if (Commonutility.checkempty(locAmount)) {
									float amtval = 0.0f;
									amtval = Commonutility.stringToFloat(locAmount);
									DonationEditObj.setAmount(amtval);
									feedEditObj.setAmount(amtval);
								}
								if (feedId != 0) {
									FeedsTblVO updateFeedObj = new FeedsTblVO();
									updateFeedObj.setFeedId(feedId);
									DonationEditObj.setFeedId(updateFeedObj);
									feedEditObj.setFeedId(feedId);
								}
								DonationEditObj.setModifyDate(Commonutility.enteyUpdateInsertDateTime());
								boolean chk = donationDAOobj.updateDonationData(DonationEditObj);
								if (chk) {
									boolean fileAttachFlg = true;
									if(fileUpload.size() > 0) {
										fileAttachFlg = donationDAOobj.donationAttachInsert(donationId,fileUpload,fileUploadContentType,fileUploadFileName);
							         }
									boolean additionUpdate = false;
									//FeedsTblVO FeedEditObj = new FeedsTblVO();
									feedEditObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
									privacyDetail = new PrivacyInfoTblVO();
									privacyDetail = commonServ.getDefaultPrivacyFlg(rid);
									if (privacyDetail != null) {
										feedEditObj.setFeedPrivacyFlg(privacyDetail.getPrivacyFlg());
									} else {
										feedEditObj.setFeedPrivacyFlg(2);
									}
									UserMasterTblVo usrmas = new UserMasterTblVo();
									usrmas = commonServ.getProfileDetails(rid);
									if (usrmas != null) {
										SocietyMstTbl socityObj = new SocietyMstTbl();
										socityObj.setSocietyId(usrmas.getSocietyId().getSocietyId());
										feedEditObj.setSocietyId(socityObj);
										feedEditObj.setUsrId(usrmas);
									} else {
										log.logMessage("User information error :: Peronal details featch error"  , "info", FeedPost.class);
										flg =  false;
									}
									feedEditObj.setEntryBy(rid);
									//System.out.println("Feed Edit removeAttachJsnarrObj-----------------------" + removeAttachJsnarrObj);
									//System.out.println("Feed Edit removeAttachJsnarrObj.length()-----------------------" + removeAttachJsnarrObj.length());
									//System.out.println("Feed Edit fileUpload-----------------------" + fileUpload);
									//System.out.println("Feed Edit fileUpload.size()-----------------------" + fileUpload.size());
									flg = feedService.feedEdit(feedEditObj, "",null,null,null,null);
									if ((removeAttachJsnarrObj != null && removeAttachJsnarrObj.length() > 0) || (removeAttachJsnarrObj.length() == 0 && fileUpload.size() > 0)) {
									//Remove feed attachment 
									File deleteFile = null;
									File deleteFileTwo = null;
									File deleteVideoFile = null;
									File deleteVideoFileTwo = null;
									getQryCnt = 0;
									String imagePathDelWeb = getText("external.imagesfldr.path") + getText("external.uploadfile.feed.img.webpath") + feedId;
									String imagePathDelMobi = getText("external.imagesfldr.path") + getText("external.uploadfile.feed.img.mobilepath") + feedId;
									deleteFile = new File(imagePathDelWeb);
									deleteFileTwo = new File(imagePathDelMobi);
									String videoDelPath = getText("external.imagesfldr.path") + getText("external.uploadfile.feed.videopath") + feedId;
									String videoPathDelThumb = getText("external.imagesfldr.path") + getText("external.uploadfile.feed.video.thumbpath") + feedId;
									deleteVideoFile = new File(videoDelPath);
									deleteVideoFileTwo = new File(videoPathDelThumb);
									try {
										if (deleteFile != null && deleteFile.exists()) {
											FileUtils.forceDelete(deleteFile);
										}
										if (deleteFileTwo != null && deleteFileTwo.exists()) {
											FileUtils.forceDelete(deleteFileTwo);
										}
										if (deleteVideoFile != null && deleteVideoFile.exists()) {
											FileUtils.forceDelete(deleteVideoFile);
										}
										if (deleteVideoFileTwo != null && deleteVideoFileTwo.exists()) {
											FileUtils.forceDelete(deleteVideoFileTwo);
										}
										getQryCnt = donationDAOobj.deletFeedAttach(feedId);							
									} catch (Exception ex) {
										log.logMessage("Ads post file delete and delet attach fileType=" + ex, "error",
												FeedDAOService.class);
									}
									
									if (flg) {
									List<MvpDonationAttachTblVo> getDonatAttchInfo = new ArrayList<MvpDonationAttachTblVo>();
									getDonatAttchInfo = donationDAOobj.getDonationAttachList(String.valueOf(donationId));
									if (getDonatAttchInfo.size() > 0) {
										MvpDonationAttachTblVo postAttchObj;
										for (Iterator<MvpDonationAttachTblVo> it = getDonatAttchInfo.iterator(); it.hasNext();) {
											postAttchObj = it.next();
											if (postAttchObj.getFileType() == 1 || postAttchObj.getFileType() == 3) {
												String addlimgSourcePath=getText("external.imagesfldr.path")+"donation/web/"+donationId;
												String feedimagePathWeb = getText("external.imagesfldr.path") + getText("external.uploadfile.feed.img.webpath")+ feedId;
												File srcFolder2 = new File(addlimgSourcePath);
										    	File destFolder2 = new File(feedimagePathWeb);
												boolean fileResult2 = Commonutility.copyFolder(srcFolder2,destFolder2);
												
								 		 		String addlimgDisPath = getText("external.imagesfldr.path")+"donation/mobile/"+donationId;
								 		 		String feedlimgDisPath = getText("external.imagesfldr.path")+ getText("external.uploadfile.feed.img.mobilepath")+ feedId;
								 		 		File srcFolder3 = new File(addlimgDisPath);
										    	File destFolder3 = new File(feedlimgDisPath);
												boolean fileResult3 = Commonutility.copyFolder(srcFolder3,destFolder3);
											}
											if (postAttchObj.getFileType() == 2) {
												String addVideosSrcPath =getText("external.imagesfldr.path")+"donation/videos/"+donationId;
												String feedvideoPath = getText("external.imagesfldr.path")+ getText("external.uploadfile.feed.videopath")+ feedId;
												File srcFolder = new File(addVideosSrcPath);
										    	File destFolder = new File(feedvideoPath);
												boolean fileResult = Commonutility.copyFolder(srcFolder,destFolder);
												
												String addVideosSrcThumb =getText("external.imagesfldr.path")+"donation/thumbimage/"+donationId;
												String feedvideoPathThumb = getText("external.imagesfldr.path")+ getText("external.uploadfile.feed.video.thumbpath")+ feedId;
												File srcFolder1 = new File(addVideosSrcThumb);
										    	File destFolder1 = new File(feedvideoPathThumb);
												boolean fileResult1 = Commonutility.copyFolder(srcFolder1,destFolder1);
											}
											FeedattchTblVO feedAttchObj = new FeedattchTblVO();
											FeedsTblVO feedidObj = new FeedsTblVO();
								       	    feedidObj.setFeedId(feedId);
											feedAttchObj.setIvrBnFEED_ID(feedidObj);
											feedAttchObj.setIvrBnATTACH_NAME(postAttchObj.getAttachName());
											feedAttchObj.setIvrBnTHUMP_IMAGE(postAttchObj.getThumbImage());
											feedAttchObj.setIvrBnFILE_TYPE(postAttchObj.getFileType());
											feedAttchObj.setIvrBnSTATUS(1);
											feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility.enteyUpdateInsertDateTime());
											feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility.enteyUpdateInsertDateTime());
											int feedattachRes = donationDAOobj.editFeedAttach(feedAttchObj);
										}
									}
								}
							}
									
									//Get Doantion info
									MvpDonationTbl doantionDataObj = new MvpDonationTbl();
									doantionDataObj = donationDAOobj.getDonationData(donationId,rid);
									
									//Additional data insert
									JSONObject jsonPublishObj = new JSONObject();
									jsonPublishObj = jsonPack.donationAdditonalJasonpackValues(doantionDataObj);
									if (jsonPublishObj != null) {
										ComplaintsDao complains=new ComplaintsDaoServices();
										additionUpdate = complains.additionalFeedUpdate(jsonPublishObj,feedId);
									} else {
										jsonPublishObj = new JSONObject();
										additionUpdate = false;
									}
									
									if (flg && additionUpdate) {
										String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath");
										String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
										String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
										String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
										String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
										List<Object[]> feedListObj = new ArrayList<Object[]>();
										feedListObj = feedService.feedDetailsProc(rid, societyKey, feedId,"");								
										Object[] objList;
										for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
											objList = it.next();									
											if (objList != null) {
												locObjRspdataJson = jsonPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
												if (objList[0]!=null) {
													System.out.println((int)objList[0]);
												}
											}
										}
									
									} else {
										locObjRspdataJson=new JSONObject();
									}
								} else {
									flg = false;
								}
							} else {
								flg = false;
							}							
						} else {
							flg = false;
						}
						log.logMessage("DonationAddEdit success flg:" + flg, "info", DonationAddEdit.class);
						if (flg) {
							if (addEdit == 1 ) {
								serverResponse(ivrservicecode,getText("status.success"),"R0224",mobiCommon.getMsg("R0224"),locObjRspdataJson);
							} else if (addEdit == 2 ) {
								serverResponse(ivrservicecode,getText("status.success"),"R0226",mobiCommon.getMsg("R0226"),locObjRspdataJson);
							} else {
								serverResponse(ivrservicecode,getText("status.success"),"R0224",mobiCommon.getMsg("R0224"),locObjRspdataJson);
							}
							
						} else {							
							if (addEdit == 1 ) {
								serverResponse(ivrservicecode,getText("status.warning"),"R0225",mobiCommon.getMsg("R0225"),locObjRspdataJson);
							} else if (addEdit == 2 ) {
								serverResponse(ivrservicecode,getText("status.warning"),"R0230",mobiCommon.getMsg("R0230"),locObjRspdataJson);
							} else {
								log.logMessage("DonationAddEdit id not valid", "info", DonationAddEdit.class);
								serverResponse(ivrservicecode,getText("status.warning"),"R0225",mobiCommon.getMsg("R0225"),locObjRspdataJson);
							}
						}
					} else {
						locObjRspdataJson=new JSONObject();
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);	
					}
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);	
				}								
				
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", DonationAddEdit.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {			
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
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
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

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

	public List<File> getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(List<File> fileUpload) {
		this.fileUpload = fileUpload;
	}

	public List<String> getFileUploadContentType() {
		return fileUploadContentType;
	}

	public void setFileUploadContentType(List<String> fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}

	public List<String> getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(List<String> fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}
	

}
