package com.mobi.publishskill;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.feed.FeedPost;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobi.publishskillvo.PublishSkillTblVO;
import com.mobi.publishskillvo.persistence.PubilshSkillDao;
import com.mobi.publishskillvo.persistence.PubilshSkillDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class AddEdit extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	JsonpackDao jsonPack = new JsonpackDaoService();
	public String execute() {
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
			int publishSkillId = 0;
			int categoryId = 0;
			int skillId = 0;
			int durationFlg = 0;
			float fees = 0.0f;
			int addEdit = 0;
			int duration = 0;
			int feedId = 0;
			String locRid = null;
			String locpublishSkillId = null;
			String loccategoryId = null;
			String locskillId = null;
			String title = null;
			String locdurationFlg = null;
			String avbilDays = null;
			String briefDesc = null;
			String locFees = null;
			String locAddEdit = null;
			String locDuration = null;
			String locFeedId = null;
			JSONArray daysJsnArr = new JSONArray();
			//FunctionUtility commonFn = new FunctionUtilityServices();
			//String daysJsnArr = null;
			log.logMessage("Enter into AddEdit ", "info", AddEdit.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("AddEdit ivrparams :" + ivrparams, "info", AddEdit.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						locAddEdit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"add_edit");// 1-add,2-edit
						locpublishSkillId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"publish_skill_id");
						loccategoryId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"category_id");
						locskillId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"skill_id");
						title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"title");
						locDuration = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"duration");
						locdurationFlg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"duration_flg"); // 1 - Hours Per Day , 2 - Hours Per Month
						System.out.println("---0---");
						avbilDays = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"avbil_days");//[1,2,4,7]
						System.out.println("----2---");
						briefDesc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"brief_desc");
						locFees = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"fees");
						locFeedId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
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
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locAddEdit)) {
							addEdit = Commonutility.stringToInteger(locAddEdit);
							if (addEdit !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.addedit.flg.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.addedit.flg.error")));
						}
						if (Commonutility.checknull(locpublishSkillId)) {
							if (Commonutility.checkLengthNotZero(locpublishSkillId)) {
								if (Commonutility.toCheckisNumeric(locpublishSkillId)) {
									publishSkillId = Commonutility.stringToInteger(locpublishSkillId);
									if (publishSkillId !=0 ) {
										
									} else {
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.id.error")));
									}
									
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.id.error")));
								}
							}
						}
						if (Commonutility.checkempty(loccategoryId) && Commonutility.toCheckisNumeric(loccategoryId)) {
							categoryId = Commonutility.stringToInteger(loccategoryId);
							if (categoryId !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.cat.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.cat.id.error")));
						}						
						if (Commonutility.checkempty(locskillId) && Commonutility.toCheckisNumeric(locskillId)) {
							skillId = Commonutility.stringToInteger(locskillId);
							if (skillId !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.skilid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.skilid.error")));
						}
						if (Commonutility.checkempty(title)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.title.error")));
						}
						if (Commonutility.checknull(locDuration)) {
							if (Commonutility.checkLengthNotZero(locDuration)) {
								if (Commonutility.toCheckisNumeric(locDuration)) {
									duration = Commonutility.stringToInteger(locDuration);									
								}
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.duration.error")));
						}
						if (Commonutility.checknull(locdurationFlg)) {
							durationFlg = Commonutility.stringToInteger(locdurationFlg);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.duration.flg.error")));
						}
						if (Commonutility.checkempty(avbilDays)) {
							//avbilDays = commonFn.jsnArryIntoStrBasedOnComma(daysJsnArr);
//							try {
//								for (int i=0;i<daysJsnArr.length();i++) {
//									avbilDays += daysJsnArr.getInt(i) + ","; 
//								}
//								if (Commonutility.checkempty(avbilDays)) {							
//									avbilDays = avbilDays.substring(0, avbilDays.length()-1);
//								}
//							} catch(Exception ex) {
//								log.logMessage("avbilDays ex found in:" + ex, "error", AddEdit.class);
//								flg = false;
//								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.avbil.error")));
//							}
							
							log.logMessage("avbilDays:" + avbilDays, "info", AddEdit.class);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.avbil.error")));
						}
						if (Commonutility.checknull(briefDesc)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.desc.error")));
						}
						if (Commonutility.checknull(locFees)) {
							if (Commonutility.checkLengthNotZero(locFees)) {
								fees = Commonutility.stringToFloat(locFees);
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.fees.error")));
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
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
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
			log.logMessage("flg :" +flg, "info", AddEdit.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						int entrybyUsr = rid;
						PublishSkillTblVO pubSkilObj = new PublishSkillTblVO();
						UserMasterTblVo userobj = new UserMasterTblVo();
						CategoryMasterTblVO categObj = new CategoryMasterTblVO();
						SkillMasterTblVO skillObj = new SkillMasterTblVO();
						PubilshSkillDao pubskilDaoObj = new PubilshSkillDaoServices();
						FeedsTblVO feedObj = new FeedsTblVO();
						FeedDAO feedService = new FeedDAOService();
						userobj.setUserId(rid);
						if (addEdit == 1 ) {							
							categObj.setiVOCATEGORY_ID(categoryId);
							skillObj.setIvrBnSKILL_ID(skillId);
							pubSkilObj.setUserId(userobj);
							pubSkilObj.setCategoryId(categObj);
							pubSkilObj.setSkillId(skillObj);
							pubSkilObj.setTitle(title);
							pubSkilObj.setDuration(duration);
							pubSkilObj.setDurationFlg(durationFlg);
							pubSkilObj.setAvbilDays(avbilDays);
							pubSkilObj.setBriefDesc(briefDesc);
							pubSkilObj.setFees(fees);
							pubSkilObj.setStatus(1);
							pubSkilObj.setEntryBy(entrybyUsr);
							pubSkilObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							pubSkilObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							int pubskilId = pubskilDaoObj.insetPublishSkill(pubSkilObj);
							log.logMessage("pubskilId :::" + pubskilId , "info", AddEdit.class);
							if (pubskilId != -1) {
								feedObj.setFeedType(5);
								feedObj.setFeedTypeId(pubskilId);
								// get category name
								CategoryMasterTblVO cateObj = new CategoryMasterTblVO();
								if (Commonutility.checkIntempty(categoryId)) {
									log.logMessage("Feed category name :::" + categoryId , "info", AddEdit.class);
									cateObj = commonServ.getCategoryDetails(categoryId);
									feedObj.setFeedCategory(cateObj.getCategoryDescription());
								} else {
									feedObj.setFeedCategory("");
								}
								feedObj.setFeedDesc(briefDesc);
								feedObj.setFeedPrivacyFlg(2);
								feedObj.setFeedStatus(1);
								feedObj.setFeedTitle(title);
								feedObj.setEntryBy(rid);
								feedObj.setPostBy(rid);
								feedObj.setOriginatorId(rid);
								feedObj.setAmount(fees);								
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
								} else {
									log.logMessage("User information error :: Peronal details featch error"  , "info", FeedPost.class);
									flg =  false;
								}
								feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
								feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
								feedObj.setIsMyfeed(1);
								feedObj.setAmount(fees);
								feedObj.setFeedPrivacyFlg(2);
								
								int retFeedId = feedService.feedInsert("", feedObj, null, null, null);
								log.logMessage("Feed retFeedId :::" + retFeedId , "info", AddEdit.class);
								if (retFeedId != -1 && flg) {
									boolean additionUpdate = false;
									
									/*Feed id update into PublishSkill*/
									PublishSkillTblVO pubSkilFeedObj = new PublishSkillTblVO();
									pubSkilFeedObj.setPubSkilId(pubskilId);
									FeedsTblVO updateFeedObj = new FeedsTblVO();
									updateFeedObj.setFeedId(retFeedId);
									pubSkilFeedObj.setFeedId(updateFeedObj);
									boolean chk = pubskilDaoObj.editPublishSkill(pubSkilFeedObj);
									log.logMessage(" chk update flg  :::" + chk , "info", AddEdit.class);
									
									/*Get PublishSkill info*/
									PublishSkillTblVO publishSkilObj = new PublishSkillTblVO();
									publishSkilObj = pubskilDaoObj.getPublishSkilData(pubskilId,rid);
									
									if (chk && publishSkilObj != null) {
										/*Additional data insert*/
										JSONObject jsonPublishObj = new JSONObject();
										jsonPublishObj = jsonPack.publishSkilJasonpackValues(publishSkilObj);
										if (jsonPublishObj != null) {
											ComplaintsDao complains=new ComplaintsDaoServices();
											additionUpdate = complains.additionalFeedUpdate(jsonPublishObj,retFeedId);
										} else {
											jsonPublishObj = new JSONObject();
											additionUpdate = false;
										}
										
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
						} else if (addEdit == 2 ) {
							// publishSkillId feedId
							if (publishSkillId != 0 && feedId != 0) {
								userobj.setUserId(rid);
								pubSkilObj.setPubSkilId(publishSkillId);
								if (categoryId != 0) {
									categObj.setiVOCATEGORY_ID(categoryId);
									pubSkilObj.setCategoryId(categObj);
									// for feed get category name
									CategoryMasterTblVO cateObj = new CategoryMasterTblVO();
									log.logMessage("Feed category name :::" + categoryId , "info", FeedPost.class);
									cateObj = commonServ.getCategoryDetails(categoryId);
									feedObj.setFeedCategory(cateObj.getCategoryDescription());
								}
								if (skillId != 0) {
									skillObj.setIvrBnSKILL_ID(skillId);
									pubSkilObj.setSkillId(skillObj);
								}
								if (Commonutility.checkempty(title)) {
									pubSkilObj.setTitle(title);
									// for feed update
									feedObj.setFeedTitle(title);								
								}
								if (Commonutility.checkIntempty(duration)) {
									pubSkilObj.setDuration(duration);
								}
								if (Commonutility.checkIntempty(durationFlg)) {
									pubSkilObj.setDurationFlg(durationFlg);
								}
								if (Commonutility.checkempty(avbilDays)) {
									pubSkilObj.setAvbilDays(avbilDays);
								}
								if (Commonutility.checkempty(briefDesc)) {
									pubSkilObj.setBriefDesc(briefDesc);
									// for feed update
									feedObj.setFeedDesc(briefDesc);
								}
								if (fees != 0) {
									pubSkilObj.setFees(fees);//feedObj
									// for feed update
									feedObj.setAmount(fees);
								}
								if (feedId != 0) {
									feedObj.setFeedId(feedId);
								}
								pubSkilObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
								boolean chk = pubskilDaoObj.editPublishSkill(pubSkilObj);
								if (chk) {
									boolean additionUpdate = false;
									
									feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
									feedObj.setFeedPrivacyFlg(2);
									UserMasterTblVo usrmas = new UserMasterTblVo();
									usrmas = commonServ.getProfileDetails(rid);
									if (usrmas != null) {
										SocietyMstTbl socityObj = new SocietyMstTbl();
										socityObj.setSocietyId(usrmas.getSocietyId().getSocietyId());
										feedObj.setSocietyId(socityObj);
										feedObj.setUsrId(usrmas);
									} else {
										log.logMessage("User information error :: Peronal details featch error"  , "info", FeedPost.class);
										flg =  false;
									}
									feedObj.setEntryBy(rid);
									flg = feedService.feedEdit(feedObj, "", null, null, null, null);
									
									/*Get PublishSkill info*/
									PublishSkillTblVO publishSkilObj = new PublishSkillTblVO();
									publishSkilObj = pubskilDaoObj.getPublishSkilData(publishSkillId,rid);
									
									/*Additional data insert*/
									JSONObject jsonPublishObj = new JSONObject();
									jsonPublishObj = jsonPack.publishSkilJasonpackValues(publishSkilObj);
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
						log.logMessage("Publish skill success flg:" + flg + " :: publishSkillId=" + publishSkillId, "info", AddEdit.class);
						if (flg) {
							if (addEdit == 1 ) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0069"),locObjRspdataJson);
							} else if (addEdit == 2 ) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0073"),locObjRspdataJson);
							} else {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
							}
							
						} else {							
							if (addEdit == 1 ) {
								serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0070"),locObjRspdataJson);
							} else if (addEdit == 2 ) {
								serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0074"),locObjRspdataJson);
							} else {
								log.logMessage("Publish skill id not valid", "info", AddEdit.class);
								serverResponse(ivrservicecode,getText("status.warning"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", AddEdit.class);
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
