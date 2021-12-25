package com.mobi.feed;

import java.io.File;
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
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FeedPost extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();	
	Log log= new Log();
	
	public String execute() {
//		fileUpload.add(new File("/home/sasikumar/Downloads/abstract_color_background_picture_8016-1680x1050.jpg"));
//		fileUploadFileName.add("abstract_color_background_picture_8016-1680x1050.jpg");
//		fileUpload.add(new File("/home/sasikumar/Downloads/16-01.jpg"));
//		fileUploadFileName.add("16-01.jpg");
//		fileUpload.add(new File("/home/sasikumar/Downloads/video/ben.mp4"));
//		fileUploadFileName.add("ben.mp4");
//		fileUpload.add(new File("/home/sasikumar/Downloads/ben.jpg"));
//		fileUploadFileName.add("ben.jpg");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		 org.json.simple.JSONObject locObjRspdataJson = null;// Response Data Json
//		Session locObjsession = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		Date dt=new Date();
		System.out.println("service start time-------------"+dt);
		try {
			ivrservicecode = getText("report.issue");
//			locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int feedOldPrivacy = 0;
			int feedPrivacy = 0;
			String feedMsg = null;
			String users = null;
			String totalSize = null;
			String thumbImg = "";
			String title = "";
			String pageurl = "";
			int feedId = 0;
			String removeAttach = null;
			JSONArray removeAttachJsnarrObj = new JSONArray();
			log.logMessage("Enter into FeedPost ", "info", FeedPost.class);
			System.out.println("ivrparams:" + ivrparams);
			FunctionUtility commonFn = new FunctionUtilityServices();
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				System.out.println("ivrparams ##:" + ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					System.out.println("1121");
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
					System.out.println("111");
					if (locObjRecvdataJson !=null) {
						String locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						feedMsg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_msg");
						String locFeedId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
						String locFeedViewId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_view_id");
//						String locOldPrivacy = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"privacy");
						String locPrivacy = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"privacy");
//						users = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");
						JSONArray usersJsnArr = new JSONArray();
						System.out.println("----json arry-");
						usersJsnArr = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");//[1,2,4,7]
						JSONArray jsonArr = new JSONArray();
						jsonArr = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"urls");//array
						System.out.println("----");
						removeAttachJsnarrObj = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"remove_attach");
						
						if (jsonArr !=null) {
							for (int i=0;i<jsonArr.length();i++) {
								JSONObject locJsonObj = new JSONObject();
								locJsonObj = jsonArr.getJSONObject(i);
								String locthumbImg = (String) Commonutility.toHasChkJsonRtnValObj(locJsonObj,"thumb_img");								
								String loctitle = (String) Commonutility.toHasChkJsonRtnValObj(locJsonObj,"title");
								String locpageurl = (String) Commonutility.toHasChkJsonRtnValObj(locJsonObj,"pageurl");
								if (Commonutility.checkempty(locthumbImg)) {
									thumbImg += Commonutility.spTagAdd(locthumbImg);
								}
								if (Commonutility.checkempty(loctitle)) {
									title += Commonutility.spTagAdd(loctitle);
								}
								if (Commonutility.checkempty(locpageurl)) {
									pageurl += Commonutility.spTagAdd(locpageurl);
								}
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.pageurl.error")));
						}
						System.out.println("11d1");
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
						System.out.println("111s");
						if (Commonutility.checknull(feedMsg)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.msg.error")));
						}
						if (Commonutility.checknull(locFeedId)) {
							if (Commonutility.checkLengthNotZero(locFeedId)) {
								if (Commonutility.toCheckisNumeric(locFeedId)) {
									feedId = Commonutility.stringToInteger(locFeedId);
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
								}
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
						}
						if (Commonutility.checknull(locPrivacy)) {
							if (Commonutility.checkLengthNotZero(locPrivacy)) {
								if (Commonutility.toCheckisNumeric(locPrivacy)) {
									feedPrivacy = Commonutility.stringToInteger(locPrivacy);
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.privacy.error")));
								}
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.privacy.error")));
						}
						System.out.println("111sd");
						if (usersJsnArr != null) {
							users = "";
							users = commonFn.jsnArryIntoStrBasedOnComma(usersJsnArr);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.users.error")));
						}
						if (removeAttachJsnarrObj != null) {
//							if (Commonutility.checkempty(removeAttach)) {
//								removeAttachJsnarrObj = new JSONArray(removeAttach);
//							}							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.removeattach.error")));
						}	
						System.out.println("11sddf1");
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				}else {
					System.out.println("111ad");
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				System.out.println("11dsd1");
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("validation flg :" +flg, "info", FeedPost.class);
			if (flg) {
				log.logMessage("Enter into feed post common check" , "info", FeedPost.class);
				locObjRspdataJson=new  org.json.simple.JSONObject();			
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						log.logMessage("Enter into feed post" , "info", FeedPost.class);
						FeedsTblVO feedObj = new FeedsTblVO();
						FeedDAO feedserviceObj = new FeedDAOService();
						UserMasterTblVo userMsterObj = new UserMasterTblVo();
						userMsterObj.setUserId(rid);
						int retFeedId = 0;
						JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
						String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
						
						String imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
						String imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
						String videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
						String videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
						
						if (feedId == 0) { // new feed
							log.logMessage("Enter into feed post :: New Feed Insert ::" , "info", FeedPost.class);
//							if (feedPrivacy == 0) {
//								feedPrivacy = 3;
//							}
							feedObj.setUsrId(userMsterObj);
							feedObj.setFeedType(2);
							feedObj.setFeedMsg(feedMsg);
							feedObj.setUrlsThumbImg(thumbImg);
							feedObj.setUrlsTitle(title);
							feedObj.setUrlsPageurl(pageurl);
							feedObj.setPostBy(rid);
							// get user name 
							UserMasterTblVo usrmas = new UserMasterTblVo();
							usrmas = commonServ.getProfileDetails(rid);
							if (usrmas != null) {
								log.logMessage("Feed originater name :::" + usrmas.getFirstName() , "info", FeedPost.class);
								feedObj.setOriginatorName(Commonutility.stringToStringempty(usrmas.getFirstName()));
								SocietyMstTbl socityObj = new SocietyMstTbl();
								if (usrmas.getSocietyId() != null) {
									socityObj.setSocietyId(usrmas.getSocietyId().getSocietyId());
								}								
								feedObj.setSocietyId(socityObj);
								if (usrmas.getCityId() != null) {
									feedObj.setFeedLocation(usrmas.getCityId().getCityName());
								}								
							} else {
								log.logMessage("User information error :: Peronal details featch error"  , "info", FeedPost.class);
								flg =  false;
							}						
							feedObj.setOriginatorId(rid);
							feedObj.setEntryBy(rid);																												
							feedObj.setFeedStatus(1);
							feedObj.setFeedPrivacyFlg(feedPrivacy);							
							feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							feedObj.setIsMyfeed(1);
							feedObj.setIsShared(0);
							long dafltVal = 0;
							feedObj.setLikeCount(dafltVal);
							feedObj.setShareCount(dafltVal);
							feedObj.setShareCount(dafltVal);
							System.out.println("service start file upload time-------------"+new Date());
							if (fileUpload != null) {
								System.out.println("-----file upload----");
								System.out.println("######---- sizee:" + fileUpload.size());
							} else {
								System.out.println("----else file-----");
							}
							if (flg) {
								retFeedId = feedserviceObj.feedInsert(users,feedObj,fileUpload,fileUploadContentType,fileUploadFileName);							
								log.logMessage("My feed id:::" + feedId , "info", FeedPost.class);
							}							
							if (retFeedId != -1) {
								log.logMessage("Feed post success ::  Feed ID ::" + retFeedId , "info", FeedPost.class);
								List<Object[]> feedListObj = new ArrayList<Object[]>();
								feedListObj = feedserviceObj.feedDetailsProc(rid, societyKey, retFeedId,"");								
								System.out.println("################");
								Object[] objList;
								for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
									System.out.println("%%^%^^%%^");
									objList = it.next();									
									System.out.println("--j----");
									if (objList != null) {
										System.out.println("---d11111---111111111111111111");
										locObjRspdataJson = jsonDataPack.feedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
										if (objList[0]!=null) {
											System.out.println("---l---");
											System.out.println((int)objList[0]);
										}
									}
								}
								System.out.println("################");															
							
							} else {
								flg =  false;
							}														 
						} else if (feedId !=0) { // Feed edit
							log.logMessage("Enter into feed post ::  Feed Edit ::" , "info", FeedPost.class);
//							if (feedPrivacy == 0) {
//								feedPrivacy = 3;
//							}
							feedObj.setUsrId(userMsterObj);
							feedObj.setFeedId(feedId);
							feedObj.setFeedMsg(feedMsg);
							feedObj.setFeedPrivacyFlg(feedPrivacy);
							feedObj.setUrlsThumbImg(thumbImg);
							feedObj.setUrlsTitle(title);
							feedObj.setUrlsPageurl(pageurl);
							feedObj.setEntryBy(rid);
							// get user name 
							UserMasterTblVo usrmas = new UserMasterTblVo();
							usrmas = commonServ.getProfileDetails(rid);
							if (usrmas != null) {
								log.logMessage("Feed originater societyId :::" + usrmas.getSocietyId().getSocietyId() , "info", FeedPost.class);
								SocietyMstTbl socityObj = new SocietyMstTbl();
								if (usrmas.getSocietyId()!=null) {
									socityObj.setSocietyId(usrmas.getSocietyId().getSocietyId());
								}								
								feedObj.setSocietyId(socityObj);
							} else {
								log.logMessage("Edit User information error :: Edit Peronal details featch error"  , "info", FeedPost.class);
								flg =  false;
							}
							boolean editFlg = feedserviceObj.feedEdit(feedObj, users, removeAttachJsnarrObj, fileUpload, fileUploadContentType,fileUploadFileName);							
							log.logMessage("Feed edit successfuly edited editFlg:" + editFlg, "info", FeedPost.class);
							if (editFlg) {
								List<Object[]> feedListObj = new ArrayList<Object[]>();
								feedListObj = feedserviceObj.feedDetailsProc(rid, societyKey, feedId,"");								
								System.out.println("################");
								Object[] objList;
								for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
									System.out.println("%%^%^^%%^");
									objList = it.next();									
									System.out.println("--j----");
									if (objList != null) {
										System.out.println("---d11111---222222222222222");
										locObjRspdataJson = jsonDataPack.feedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
										if (objList[0]!=null) {
											System.out.println("---l---");
											System.out.println((int)objList[0]);
										}
									}
								}
//								objList = feedserviceObj.feedDetailsProc(rid, societyKey, feedId);								
																							
							} else {
								flg = false;
							}
						} else {
							flg = false;
						}	
						log.logMessage("Enter into edit response : flg:" + flg + " ::Feedid:" + feedId, "info", FeedPost.class);
						if (flg) {
							if (feedId ==0) { // new feed
								serverResponse(ivrservicecode,getText("status.success"),"R0037",mobiCommon.getMsg("R0037"),locObjRspdataJson);
							} else if (feedId !=0) { // Feed edit
								serverResponse(ivrservicecode,getText("status.success"),"R0038",mobiCommon.getMsg("R0038"),locObjRspdataJson);
							} else {
								serverResponse(ivrservicecode,getText("status.success"),"R0056",mobiCommon.getMsg("R0056"),locObjRspdataJson);
							}
						} else {
							if (feedId ==0) { // new feed
								serverResponse(ivrservicecode,getText("status.warning"),"R0041",mobiCommon.getMsg("R0041"),locObjRspdataJson);
							} else if (feedId ==0) { // Feed edit
								serverResponse(ivrservicecode,getText("status.warning"),"R0042",mobiCommon.getMsg("R0042"),locObjRspdataJson);
							} else {
								serverResponse(ivrservicecode,getText("status.success"),"R0056",mobiCommon.getMsg("R0056"),locObjRspdataJson);
							}
						}
					} else {
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);	
					}
				} else {
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);	
				}									
			} else {
				locObjRspdataJson=new  org.json.simple.JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			log.logMessage(getText("Eex") + ex, "error", FeedPost.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			
		}
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message,  org.json.simple.JSONObject dataJson)
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
			System.out.println("=====as==="+as);
			as=EncDecrypt.encrypt(as);
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
