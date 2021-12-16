package com.pack.donation;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.json.JSONArray;

import com.mobi.addpost.AddPostDao;
import com.mobi.addpost.AddPostServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MvpDonationAttachTblVo;
import com.socialindiaservices.vo.MvpDonationCategoryMstTbl;
import com.socialindiaservices.vo.MvpDonationItemTypeTblVo;
import com.socialindiaservices.vo.MvpDonationTbl;



public class DonationSearchList extends ActionSupport {
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		FunctionUtility commonFn = new FunctionUtilityServices();
		JsonSimplepackDao simpleJson = new JsonSimplepackDaoService();
		AddPostDao adPost =new  AddPostServices();
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int categoryId = 0;
			int skillId = 0;
			int startLimit = 0;
			Date timeStamp = null;
			String locRid = null;
			String loccategoryId = null;
			String locskillId = null;
			String searchQury = "";
			String searchFlg = null;
			String locTimeStamp = null;
			String startlimit = null;
			int totCnt = 0;
			log.logMessage("Enter into DonationSearchList ", "info", DonationSearchList.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("DonationSearchList ivrparams :" + ivrparams, "info", DonationSearchList.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						loccategoryId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"category_id");
						locskillId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"skill_id");
						locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
						startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
						searchFlg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"search_flg");
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
						if (searchFlg.equalsIgnoreCase("1")) { 
						if (!loccategoryId.equalsIgnoreCase("") && !loccategoryId.equalsIgnoreCase("null") 
								&& loccategoryId != null && loccategoryId.length() > 0) {
									categoryId = Commonutility.stringToInteger(loccategoryId);
									searchQury += " and mvpDonationCategoryMstTbl.iVOCATEGORY_ID = '" + categoryId+"'";
						}
						if (!locskillId.equalsIgnoreCase("") && !locskillId.equalsIgnoreCase("null") 
								&& locskillId != null && locskillId.length() > 0) {
								skillId = Commonutility.stringToInteger(locskillId);
								searchQury += " and mvpDonationSubcategoryMstTbl.ivrBnSKILL_ID = '"+ skillId + "'" ;
						}
						searchQury += " and userId.userId = '"+rid+"' ";
						} else {
							searchQury = " and userId.userId = '"+rid+"' ";
						}
						if (Commonutility.checkempty(locTimeStamp)) {
						} else {
							locTimeStamp=Commonutility.timeStampRetStringVal();
						}
						if(startlimit!=null && startlimit.length()>0){}else{
							startlimit="0";
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				}else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("DonationSearchList flg :" +flg, "info", DonationSearchList.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						MvpDonationTbl donateDataObj = new MvpDonationTbl();
						FeedsTblVO feedDataObj = new FeedsTblVO();
						DonationDao donationDAOobj = new DonationDaoServices();
						List<MvpDonationTbl> listObj = new ArrayList<MvpDonationTbl>();
						listObj = donationDAOobj.donationSearchList(searchQury,startlimit,getText("total.row"), locTimeStamp);
						JSONObject finalJsonarr=new JSONObject();
						JSONArray jsonArr = new JSONArray();
						if (listObj != null && listObj.size() > 0) {
							locObjRspdataJson = new JSONObject();
							donateDataObj = new MvpDonationTbl();
							feedDataObj = new FeedsTblVO();
							for (Iterator<MvpDonationTbl> it = listObj.iterator();it.hasNext();) {
								donateDataObj = it.next();
								finalJsonarr=new JSONObject();

								System.out.println("donateDataObj.getDonateId()----" + donateDataObj.getDonateId());
								if (donateDataObj.getDonateId() != 0) {				
									finalJsonarr.put("donate_id",Commonutility.intToString(donateDataObj.getDonateId()));					
								} else {
									finalJsonarr.put("donate_id","");
								}
								System.out.println("donateDataObj.getMvpDonationCategoryMstTbl()----" + donateDataObj.getMvpDonationCategoryMstTbl());
								if (donateDataObj.getMvpDonationCategoryMstTbl() != null) {
									finalJsonarr.put("category_id",Commonutility.intToString(donateDataObj.getMvpDonationCategoryMstTbl().getiVOCATEGORY_ID()));
									finalJsonarr.put("category_name",Commonutility.stringToStringempty(donateDataObj.getMvpDonationCategoryMstTbl().getiVOCATEGORY_NAME()));	
								} else {
									finalJsonarr.put("category_id","");
									finalJsonarr.put("category_name","");
								}
								System.out.println("donateDataObj.getMvpDonationSubcategoryMstTbl()----" + donateDataObj.getMvpDonationSubcategoryMstTbl());
								if (donateDataObj.getMvpDonationSubcategoryMstTbl() != null) {
									finalJsonarr.put("skill_id",Commonutility.intToString(donateDataObj.getMvpDonationSubcategoryMstTbl().getIvrBnSKILL_ID()));		
									finalJsonarr.put("skill_name",Commonutility.stringToStringempty(donateDataObj.getMvpDonationSubcategoryMstTbl().getIvrBnSKILL_NAME()));
								} else {
									finalJsonarr.put("skill_id","");
									finalJsonarr.put("skill_name","");
								}
								System.out.println("donateDataObj.getTitle()----" + donateDataObj.getTitle());
								if (donateDataObj.getTitle() != null && donateDataObj.getTitle() != "" 
										&& donateDataObj.getTitle() != "null" && donateDataObj.getTitle().length() > 0) {
									finalJsonarr.put("title",Commonutility.stringToStringempty(donateDataObj.getTitle()));		
								} else {
									finalJsonarr.put("title","");
								}
								System.out.println("donateDataObj.getQuantity()----" + donateDataObj.getQuantity());
								if (donateDataObj.getQuantity() != null && donateDataObj.getQuantity() != 0) {
									finalJsonarr.put("quantity",Commonutility.intToString(donateDataObj.getQuantity()));
								} else {
									finalJsonarr.put("quantity","");
								}
								System.out.println("donateDataObj.getItemType()----" + donateDataObj.getItemType());
								if (donateDataObj.getItemType() != null && donateDataObj.getItemType() != 0) {
									finalJsonarr.put("item_type_id",Commonutility.intToString(donateDataObj.getItemType()));
									MvpDonationItemTypeTblVo itemTypeObj = new MvpDonationItemTypeTblVo();
									itemTypeObj.setItemtypeId(donateDataObj.getItemType());
									itemTypeObj = donationDAOobj.itemTypeGetDetails(itemTypeObj);
									if (itemTypeObj != null) {
										finalJsonarr.put("item_type_name",itemTypeObj.getItemtypeName());
									} else {
										finalJsonarr.put("item_type_name","");
									}
								} else {
									finalJsonarr.put("item_type_id","");
									finalJsonarr.put("item_type_name","");
								}
								System.out.println("donateDataObj.getDescription()----" + donateDataObj.getDescription());
								if (donateDataObj.getDescription() != null && donateDataObj.getDescription() != "" 
										&& donateDataObj.getDescription() != "null" && donateDataObj.getDescription().length() > 0) {
									finalJsonarr.put("description",Commonutility.stringToStringempty(donateDataObj.getDescription()));			
								} else {
									finalJsonarr.put("description","");
								}
								System.out.println("donateDataObj.getOtherDescription()----" + donateDataObj.getOtherDescription());
								if (donateDataObj.getOtherDescription() != null && donateDataObj.getOtherDescription() != "" 
										&& donateDataObj.getOtherDescription() != "null" && donateDataObj.getOtherDescription().length() > 0) {
									String bookDetail = donateDataObj.getOtherDescription();
									String[] splitval = bookDetail.split("!_!");
									System.out.println("splitval-----------" + splitval.length);
									if (splitval.length > 0) {
										finalJsonarr.put("authour",Commonutility.stringToStringempty(splitval[0]));	
										finalJsonarr.put("pages",Commonutility.stringToStringempty(splitval[1]));
										finalJsonarr.put("publisher",Commonutility.stringToStringempty(splitval[2]));
										finalJsonarr.put("ratings",Commonutility.stringToStringempty(splitval[3]));
									} else {
										finalJsonarr.put("authour","");
										finalJsonarr.put("pages","");
										finalJsonarr.put("publisher","");
										finalJsonarr.put("ratings","");
									}
									
								} else {
									finalJsonarr.put("authour","");
									finalJsonarr.put("pages","");
									finalJsonarr.put("publisher","");
									finalJsonarr.put("ratings","");
								}
								System.out.println("donateDataObj.getDonateTo()----" + donateDataObj.getDonateTo());
								if (donateDataObj.getDonateTo() != null) {
									finalJsonarr.put("donate_to_id",Commonutility.intToString(donateDataObj.getDonateTo().getInstitutionId()));		
									finalJsonarr.put("donate_to_name",Commonutility.stringToStringempty(donateDataObj.getDonateTo().getInstitutionName()));
								} else {
									finalJsonarr.put("donate_to_id","");
									finalJsonarr.put("donate_to_name","");
								}
								System.out.println("donateDataObj.getAmount()----" + donateDataObj.getAmount());
								if (Commonutility.checkempty(Commonutility.floatToString(donateDataObj.getAmount()))) {
									finalJsonarr.put("amount",Commonutility.floatToString(donateDataObj.getAmount()));				
								} else {
									finalJsonarr.put("amount","");
								}
								System.out.println("donateDataObj.getUserId()----" + donateDataObj.getUserId());
								String userId = "";
								if (donateDataObj.getUserId() != null) {
									userId = Commonutility.intToString(donateDataObj.getUserId().getUserId());
									String profileFirstName="",profileName; 
									String profileLastName="";
									profileFirstName=Commonutility.toCheckNullEmpty(donateDataObj.getUserId().getFirstName());
									profileLastName=Commonutility.toCheckNullEmpty(donateDataObj.getUserId().getLastName());
									if(!profileFirstName.equalsIgnoreCase("")){
										profileName=profileFirstName+" "+profileLastName;
									}else{
										profileName=profileLastName;
									}
									finalJsonarr.put("profile_name", Commonutility.toCheckNullEmpty(profileName));
									String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath");
									String imgName = Commonutility.stringToStringempty(donateDataObj.getUserId().getImageNameMobile());
									if (Commonutility.checkempty(imgName) && Commonutility.checkempty(userId)) {
										finalJsonarr.put("profilepic", profileimgPath + userId + "/" + imgName);
									} else {
										finalJsonarr.put("profilepic","");
									}
									FlatMasterTblVO flatUserdata = new FlatMasterTblVO();
									profileDao profile=new profileDaoServices();
									flatUserdata = profile.getFlatUserInfo(donateDataObj.getUserId().getUserId());
								} else {
									finalJsonarr.put("profile_name", "");
								}
								
								System.out.println("donateDataObj.getFeedId()----" + donateDataObj.getFeedId());
								if (donateDataObj.getFeedId() != null) {
									finalJsonarr.put("feed_id",Commonutility.intToString(donateDataObj.getFeedId().getFeedId()));
									String feedid = Commonutility.intToString(donateDataObj.getFeedId().getFeedId());
									ComplaintsDao complains=new ComplaintsDaoServices();
									feedDataObj = complains.getFeedIdValue(feedid);
									if (feedDataObj != null) {
										finalJsonarr.put("feed_category", Commonutility.stringToStringempty(feedDataObj.getFeedCategory()));
										finalJsonarr.put("feed_desc", Commonutility.stringToStringempty(feedDataObj.getFeedDesc()));
										finalJsonarr.put("feed_title", Commonutility.stringToStringempty(feedDataObj.getFeedTitle()));
										//finalJsonarr.put("is_liked", feedDataObj.getLikeCount());
										finalJsonarr.put("like_count", feedDataObj.getLikeCount());
										finalJsonarr.put("share_count", feedDataObj.getShareCount());
										finalJsonarr.put("comment_count", feedDataObj.getCommentCount());
										finalJsonarr.put("originator_name", Commonutility.stringToStringempty(feedDataObj.getOriginatorName()));
										finalJsonarr.put("originator_id", Commonutility.toCheckNullEmpty(feedDataObj.getOriginatorId()));
										finalJsonarr.put("feed_time", commonFn.getPostedDateTime(feedDataObj.getFeedTime()));
										finalJsonarr.put("feed_id", Commonutility.toCheckNullEmpty(feedDataObj.getFeedId()));
										finalJsonarr.put("feed_type", Commonutility.toCheckNullEmpty(feedDataObj.getFeedType()));
										finalJsonarr.put("feed_type_id", Commonutility.toCheckNullEmpty(feedDataObj.getFeedTypeId()));
										finalJsonarr.put("is_myfeed", Commonutility.toCheckNullEmpty(feedDataObj.getIsMyfeed()));
										finalJsonarr.put("privacy_categories", ((JsonSimplepackDaoService) simpleJson).catPack(feedDataObj.getFeedPrivacyFlg().intValue()));
										finalJsonarr.put("post_by", Commonutility.toCheckNullEmpty(feedDataObj.getPostBy()));
										finalJsonarr.put("post_as", Commonutility.toCheckNullEmpty(feedDataObj.getPostAs()));
										int retFeedViewId = 0;
										int userid = 0;
										if (feedDataObj.getFeedPrivacyFlg() == 2) {
											userid = -1;
										} else if (feedDataObj.getFeedPrivacyFlg() == 3) {
											userid = -2;
										} else {
											userid = rid;
										}
										//retFeedViewId = adPost.getadpostFeedViewId(rid, feedDataObj.getSocietyId().getSocietyId(), feedDataObj.getFeedId(), userid,0,searchFlg);
										//finalJsonarr.put("feed_view_id", Commonutility.toCheckNullEmpty(retFeedViewId));
									} else {
										finalJsonarr.put("feed_category", "");
										finalJsonarr.put("feed_desc", "");
										finalJsonarr.put("feed_title", "");
										//finalJsonarr.put("is_liked", "");
										finalJsonarr.put("like_count", "");
										finalJsonarr.put("share_count", "");
										finalJsonarr.put("comment_count", "");
										finalJsonarr.put("originator_name", "");
										finalJsonarr.put("originator_id", "");
										finalJsonarr.put("feed_time", "");
										finalJsonarr.put("feed_type", "");
										finalJsonarr.put("feed_type_id", "");
										finalJsonarr.put("is_myfeed", "");
										finalJsonarr.put("feed_view_id", "");
										finalJsonarr.put("post_by", "");
										finalJsonarr.put("post_as", "");
									}
								} else {
									finalJsonarr.put("feed_id","");
								}
								
								int varDonatoinAttach = donateDataObj.getDonateId();
								List<MvpDonationAttachTblVo> donationAttachList=new ArrayList<MvpDonationAttachTblVo>();
								
								JSONObject comVido = new JSONObject();
								JSONObject images = new JSONObject();
								JSONArray imagesArr = new JSONArray();
								JSONArray comVidoArr = new JSONArray();
								
								boolean flag = false;
								System.out.println("===String.valueOf(varDonatoinAttach)==="+ String.valueOf(varDonatoinAttach));
								donationAttachList = donationDAOobj.getDonationAttachList(String.valueOf(varDonatoinAttach));
								System.out.println("===adPostAttachList==="+donationAttachList.size());
								MvpDonationAttachTblVo doanteAttachObj;
								for (Iterator<MvpDonationAttachTblVo> itObj = donationAttachList.iterator(); itObj.hasNext();) {
									doanteAttachObj = itObj.next();
									flag=true;
									//finalJsonarr = new JSONObject();
									images = new JSONObject();
									comVido = new JSONObject();
										if (doanteAttachObj.getFileType() == 1) {
											if (doanteAttachObj.getFileType()!=null) {
											images.put("img_id",String.valueOf(doanteAttachObj.getDonataionAttchId()));
											} else {
											images.put("img_id","");
											} if(doanteAttachObj.getAttachName()!=null) {
											images.put("img_url",System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/donation/mobile/"+doanteAttachObj.getDonateId().getDonateId()+"/"+doanteAttachObj.getAttachName());
											} else {
											images.put("img_url","");
											}
										}
										if (doanteAttachObj.getFileType() == 2) {
											System.out.println("==thumb_img===="+ doanteAttachObj.getThumbImage());
											if (doanteAttachObj.getFileType()!=null) {
											comVido.put("video_id",String.valueOf(doanteAttachObj.getDonataionAttchId()));
											} else {
											comVido.put("video_id","");
											} if (doanteAttachObj.getThumbImage()!=null) {
											comVido.put("thumb_img", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/donation/thumbimage/"+doanteAttachObj.getDonateId().getDonateId()+"/"+doanteAttachObj.getThumbImage());
											} else {
											comVido.put("thumb_img","");
											} if (doanteAttachObj.getAttachName()!=null) {
											comVido.put("video_url", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/donation/videos/"+doanteAttachObj.getDonateId().getDonateId()+"/"+doanteAttachObj.getAttachName());
											} else {
											comVido.put("video_url","");
											}
										}
										if (images!=null && images.length() > 0) {
											imagesArr.put(images);
											finalJsonarr.put("images",imagesArr);
										} else {
											finalJsonarr.put("images",imagesArr); 
										}
										if (comVido!=null && comVido.length() > 0) {
											comVidoArr.put(comVido);
											finalJsonarr.put("videos",comVidoArr);
										} else {
											finalJsonarr.put("videos",comVidoArr); 
										}
									}
								if (flag==false) {
									finalJsonarr.put("images",imagesArr);
									finalJsonarr.put("videos",comVidoArr);
								}
								jsonArr.put(finalJsonarr);
							}
							//totCnt = listObj.size();
							locObjRspdataJson.put("donation_details", jsonArr);
							locObjRspdataJson.put("timestamp", locTimeStamp);
							CommonMobiDao commonHbm=new CommonMobiDaoService();
							String locVrSlQry="SELECT count(donateId) FROM MvpDonationTbl where status=1 " + searchQury + " and entryDate <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') ";
							System.out.println("=======locVrSlQry======="+ locVrSlQry);
							totCnt = commonHbm.getTotalCountQuery(locVrSlQry);
							System.out.println("=======totCnt======="+ totCnt);
							locObjRspdataJson.put("totalrecords", totCnt);
							if (flg) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
							} else {
								locObjRspdataJson = new JSONObject();
								serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0006"),locObjRspdataJson);
							}
						} else{
							locObjRspdataJson=new JSONObject();
							serverResponse(ivrservicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", DonationSearchList.class);
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
	
	

}