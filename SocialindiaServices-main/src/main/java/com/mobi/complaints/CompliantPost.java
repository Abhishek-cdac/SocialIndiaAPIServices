package com.mobi.complaints;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.commonvo.PrivacyInfoTblVO;
import com.mobi.feed.FeedPost;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.paswordservice.Forgetpassword;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.utils.Log;

public class CompliantPost extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();
	otpDao otp=new otpDaoService();
	ComplaintsDao complains=new ComplaintsDaoServices();
	profileDao profile=new profileDaoServices();
	ComplaintsTblVO compliantMst=new ComplaintsTblVO();	
	UserMasterTblVo userMst=new UserMasterTblVo();
	FeedDAO feed=new FeedDAOService();
	GroupMasterTblVo groupMst=new GroupMasterTblVo();
	SocietyMstTbl societyMst=new SocietyMstTbl();
	PrivacyInfoTblVO privacyDetail = new PrivacyInfoTblVO();
	mobiCommon mobiCom=new mobiCommon();
	//FeedsTblVO feedMst=new FeedsTblVO();
	//FeedsTblVO feedInfo=new FeedsTblVO();
	public String execute(){
		
		
	/*	fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"));
		fileUploadFileName.add("Chrysanthemum.jpg");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
		fileUploadFileName.add("Wildlife.wmv");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.jpg"));
		fileUploadFileName.add("Wildlife.jpeg");
		fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Desert.jpg"));
		fileUploadFileName.add("Desert.jpg");*/
		System.out.println("********Compliant Post****************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		org.json.simple.JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		JSONArray jsonarr = null;
		 ComplaintattchTblVO compliantsAttachMst=new ComplaintattchTblVO();
		boolean flg = true;
		String servicecode="";
		int retFeedId = 0;
		try{
			locErrorvalStrBuil = new StringBuilder();
			//ivrparams = URLDecoder.decode(ivrparams, "UTF-8");
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("======ivrparams============"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "title");
				String desc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "desc");
				String post_to = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "post_to");
				
				String complaint_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "complaint_id");
				String feed_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "feed_id");
				jsonarr =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"remove_attach");
				String external_emailid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "email_id");
				String is_resident = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_resident");
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
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(title)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("title")));
					}
				}
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(desc)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("desc")));
					}
				}
				
				if(flg){
					Date date;
					CommonUtils comutil=new CommonUtilsServices();
					CommonMobiDao commonServ = new CommonMobiDaoService();
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					boolean result=otp.checkTownshipKey(rid,townshipKey);
					locObjRspdataJson=new  org.json.simple.JSONObject();
			if(result==true){
			System.out.println("********result*****************"+result);
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				if(complaint_id.length()==0){
				compliantMst.setComplaintsTitle(title);
				compliantMst.setComplaintsDesc(desc);
				compliantMst.setShrtDesc("Complaint");
				compliantMst.setCompliantsToFlag(Integer.parseInt(post_to));
				if(post_to.equalsIgnoreCase("0"))
				{
					compliantMst.setComplaintsOthersEmail(external_emailid);
				}
				userMst.setUserId(Integer.parseInt(rid));
				compliantMst.setUsrRegTblByFromUsrId(userMst);
				groupMst.setGroupCode(userMst.getGroupCode().getGroupCode());
				compliantMst.setGroupMstTblByFromGroupId(groupMst);
				compliantMst.setStatus(1);
				compliantMst.setEntryDatetime(date);
				compliantMst.setEntryBy(Integer.parseInt(rid));
				int complainUniqId = complains.compliantsPostInsert(compliantMst,fileUpload,fileUploadContentType,fileUploadFileName);
				
				if(complainUniqId != -1) {
					//For notification
					CommonMobiDao commonHbm=new CommonMobiDaoService();
					List<UserMasterTblVo> userobjList=new ArrayList<UserMasterTblVo>();
					NotificationDao notificationHbm=new NotificationDaoServices();
					String userQuery="from UserMasterTblVo where societyId.activationKey='"+societykey+"' and societyId.statusFlag=1 and   statusFlag=1 and groupCode.groupName= '"+getText("Committee")+"'";
					userobjList=commonHbm.getUserRegisterDeatils(userQuery);
					UserMasterTblVo userMasteriterObj;
					AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
					String additionaldata=additionalDatafunc.formAdditionalDataForComplaintMastTbl(compliantMst);
					for(Iterator<UserMasterTblVo> it=userobjList.iterator();it.hasNext();){
						userMasteriterObj=it.next();
						String notifdesc="You have tagged a "+getText("notification.reflg.7.desc")+" by "+Commonutility.toCheckNullEmpty(userMst.getFirstName())+Commonutility.toCheckNullEmpty(userMst.getLastName());
						notificationHbm.insertnewNotificationDetails(userMasteriterObj, notifdesc, 0, 1, complainUniqId, userMst, additionaldata);
					}
					/*END*/
					
					if (is_resident.equalsIgnoreCase("1") && (feed_id == null || feed_id.length() == 0 || 
							feed_id.equalsIgnoreCase("") || feed_id.equalsIgnoreCase("null"))) {     // Resident feed insert start
					
					FeedsTblVO feedObj=new FeedsTblVO();
					feedObj.setUsrId(userMst);
					feedObj.setFeedType(7);
					feedObj.setFeedTypeId(complainUniqId);
					feedObj.setFeedTitle(title);
					feedObj.setFeedStitle("Complaint");
					feedObj.setFeedDesc(desc);
					feedObj.setPostBy(Integer.parseInt(rid));
					// get user name 
					UserMasterTblVo usrmas = new UserMasterTblVo();
					usrmas = commonServ.getProfileDetails(Integer.parseInt(rid));
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
					feedObj.setOriginatorId(Integer.parseInt(rid));
					feedObj.setEntryBy(Integer.parseInt(rid));																												
					feedObj.setFeedStatus(1);
					feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
					feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
					feedObj.setFeedTime(Commonutility.enteyUpdateInsertDateTime());
					feedObj.setIsMyfeed(1);
					feedObj.setIsShared(0);
					long dafltVal = 0;
					feedObj.setLikeCount(dafltVal);
					feedObj.setShareCount(dafltVal);
					feedObj.setCommentCount(dafltVal);
					
					privacyDetail = commonServ.getDefaultPrivacyFlg(Integer.parseInt(rid));
					if (privacyDetail != null) {
						feedObj.setFeedPrivacyFlg(privacyDetail.getPrivacyFlg());
					} else {
						feedObj.setFeedPrivacyFlg(Commonutility.stringToInteger(getText("default.privacy.category")));
					}
					
					retFeedId = feed.feedInsert("",feedObj,fileUpload,fileUploadContentType,fileUploadFileName);
					if (retFeedId != -1) {
						boolean feedidUpdate = false;
						boolean additionUpdate = false;
						
						/*Feed id insert into complaint*/
						feedidUpdate = complains.updateFeedIdtoComplaint(complainUniqId,retFeedId,Integer.parseInt(rid));
						
						/*Get complaint info*/
						ComplaintsTblVO complaintGetObj = new ComplaintsTblVO(); 
						complaintGetObj = complains.ComplaintUniqResult(complainUniqId,Integer.parseInt(rid));
						
						/*Additional data insert*/
						if (feedidUpdate && complaintGetObj != null) {
							JSONObject jsonComplaintObj = new JSONObject();
							jsonComplaintObj = complains.complaintJasonpackValues(complaintGetObj);
							if (jsonComplaintObj != null) {
								additionUpdate = complains.additionalFeedUpdate(jsonComplaintObj,retFeedId);
							}
						}
						
							log.logMessage("Feed Insert success ::  Feed ID ::" + retFeedId , "info", FeedPost.class);
							JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
							String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
							
							String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
							String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
							String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
							String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
							List<Object[]> feedListObj = new ArrayList<Object[]>();
							feedListObj = feed.feedDetailsProc(Integer.parseInt(rid), societykey, retFeedId,"");								
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
				}    // Resident feed insert end
				} else {
					flg =  false;
				}
				if (flg && is_resident.equalsIgnoreCase("0")) {
					locObjRspdataJson=new  org.json.simple.JSONObject();
					System.out.println("if---------------is_resident===============" + is_resident + "-----locObjRspdataJson------------" + locObjRspdataJson);		
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				} else if (flg && is_resident.equalsIgnoreCase("1")) {
					System.out.println("else if---------------is_resident===============" + is_resident + "-----locObjRspdataJson------------" + locObjRspdataJson);
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				} else {
					locObjRspdataJson=new  org.json.simple.JSONObject();
					System.out.println("else---------------is_resident===============" + is_resident + "-----locObjRspdataJson------------" + locObjRspdataJson);
					serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
				}
			}else if(complaint_id.length()>0){
				 String attachId =new String();
				boolean CheckCompExist=complains.compliantsUpdate(complaint_id,rid);
				if(CheckCompExist==true){
					for(int i=0;i<jsonarr.length();i++){
						attachId=null;
						attachId=jsonarr.getString(i);
						System.out.println("==attachId==="+attachId);
						compliantsAttachMst=complains.getDeleteCompliantList(attachId);
						System.out.println("==========compliantsAttachMst=========="+compliantsAttachMst);
						if(compliantsAttachMst!=null && compliantsAttachMst.getIvrBnFILE_TYPE()==2){
							
							String videoPath =getText("external.imagesfldr.path")+"complaint/videos/"+complaint_id;
							boolean delFlag=mobiCom.deleteIfFileExist(videoPath, compliantsAttachMst.getIvrBnATTACH_NAME());
							System.out.println("=======delFlag====videoPath======="+delFlag);
							String thumbpath =getText("external.imagesfldr.path")+"complaint/thumbimage/"+complaint_id;
							 delFlag=mobiCom.deleteIfFileExist(thumbpath, compliantsAttachMst.getThumbImage());
							 System.out.println("=======delFlag====thumbpath======="+delFlag);
							 
						}else if(compliantsAttachMst!=null && compliantsAttachMst.getIvrBnFILE_TYPE()==1){
							
						 String webPath=getText("external.imagesfldr.path")+"complaint/web/"+complaint_id;
						 System.out.println("============webPath======webPath===="+webPath);
						 boolean delFlag=mobiCom.deleteIfFileExist(webPath, compliantsAttachMst.getIvrBnATTACH_NAME());
						 System.out.println("=======delFlag====webPath===gg===="+delFlag);
 		 		 		String mobilePath = getText("external.imagesfldr.path")+"complaint/mobile/"+complaint_id;
 		 		 		 delFlag=mobiCom.deleteIfFileExist(mobilePath, compliantsAttachMst.getIvrBnATTACH_NAME());
 		 		 		System.out.println("=======delFlag====mobilePath==gg====="+delFlag);
 		 		 		
						}else if(compliantsAttachMst!=null){
							 String webPath=getText("external.imagesfldr.path")+"complaint/web/"+complaint_id;
							 boolean delFlag=mobiCom.deleteIfFileExist(webPath, compliantsAttachMst.getIvrBnATTACH_NAME());
							 System.out.println("=======delFlag====webPath==gg====="+webPath);
	 		 		 		String mobilePath = getText("external.imagesfldr.path")+"complaint/mobile/"+complaint_id;
	 		 		 		 delFlag=mobiCom.deleteIfFileExist(mobilePath, compliantsAttachMst.getIvrBnATTACH_NAME());
	 		 		 		System.out.println("=======delFlag====mobilePath==gg====="+delFlag);
						}
					System.out.println("=========resultDel========="+compliantsAttachMst);
					boolean deleteAttach=complains.deleteAttachInfo(attachId);
					}
					
				boolean Result=complains.compliantsUpdate(fileUpload,fileUploadContentType,fileUploadFileName,title,desc,post_to,complaint_id,rid,jsonarr,feed_id);
				
				
				if(Result==true){
					//For notification
					AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
					compliantMst=complains.ComplaintUniqResultByComplaintId(Integer.parseInt(complaint_id));
					String additionaldata=additionalDatafunc.formAdditionalDataForComplaintMastTbl(compliantMst);
					
					NotificationDao notificationHbm=new NotificationDaoServices();
					notificationHbm.updateNotificationDetails(0, Integer.parseInt(complaint_id), additionaldata);
					/*END*/
					
					
					if (is_resident.equalsIgnoreCase("1") && feed_id != null && feed_id.length() > 0 && 
							!feed_id.equalsIgnoreCase("") && !feed_id.equalsIgnoreCase("null")) {   // Resident feed edit start
						FeedsTblVO feedObj=new FeedsTblVO();
						feedObj.setFeedId(Commonutility.stringToInteger(feed_id));
						feedObj.setFeedTypeId(Commonutility.stringToInteger(complaint_id));
						feedObj.setFeedTitle(title);
						feedObj.setFeedDesc(desc);
						feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setFeedTime(Commonutility.enteyUpdateInsertDateTime());
						privacyDetail = commonServ.getDefaultPrivacyFlg(Integer.parseInt(rid));
						if (privacyDetail != null) {
							feedObj.setFeedPrivacyFlg(privacyDetail.getPrivacyFlg());
						} else {
							feedObj.setFeedPrivacyFlg(Commonutility.stringToInteger(getText("default.privacy.category")));
						}
						UserMasterTblVo usrmas = new UserMasterTblVo();
						usrmas = commonServ.getProfileDetails(Commonutility.stringToInteger(rid));
						if (usrmas != null) {
							SocietyMstTbl socityObj = new SocietyMstTbl();
							socityObj.setSocietyId(usrmas.getSocietyId().getSocietyId());
							feedObj.setSocietyId(socityObj);
							feedObj.setUsrId(usrmas);
						} else {
							log.logMessage("User information error :: Peronal details featch error"  , "info", FeedPost.class);
							flg =  false;
						}
						feedObj.setEntryBy(Commonutility.stringToInteger(rid));												
						
						boolean editFlg = feed.feedEdit(feedObj, "", jsonarr, fileUpload, fileUploadContentType,fileUploadFileName);
						if (editFlg) {
							boolean additionUpdate = false;
							
							/*Get complaint info*/
							ComplaintsTblVO complaintGetObj = new ComplaintsTblVO(); 
							complaintGetObj = complains.ComplaintUniqResult(Commonutility.stringToInteger(complaint_id),Integer.parseInt(rid));
							System.out.println("complaintGetObj===========" + complaintGetObj.toString());
							System.out.println("complaintGetObj.getComplaintsTitle===========" + complaintGetObj.getComplaintsTitle());
							System.out.println("complaintGetObj.getFeedId===========" + complaintGetObj.getFeedId());
							
							/*Additional data insert*/
							if (complaintGetObj != null) {
								JSONObject jsonComplaintObj = new JSONObject();
								jsonComplaintObj = complains.complaintJasonpackValues(complaintGetObj);
								if (jsonComplaintObj != null) {
									additionUpdate = complains.additionalFeedUpdate(jsonComplaintObj,Commonutility.stringToInteger(feed_id));
								}
							}
							
							JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
							String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
							
							String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
							String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
							String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
							String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
							List<Object[]> feedListObj = new ArrayList<Object[]>();
							feedListObj = feed.feedDetailsProc(Integer.parseInt(rid), societykey, Commonutility.stringToInteger(feed_id),"");								
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
							flg = false;
						}
					}  // Resident feed edit end
				} else {
					flg = false;
				}
				
				if (flg && is_resident.equalsIgnoreCase("0")) {
					locObjRspdataJson=new  org.json.simple.JSONObject();
					System.out.println("edit if---------------is_resident===============" + is_resident + "-----locObjRspdataJson------------" + locObjRspdataJson);
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				} else if (flg && is_resident.equalsIgnoreCase("1")) {
					System.out.println("edit else if---------------is_resident===============" + is_resident + "-----locObjRspdataJson------------" + locObjRspdataJson);
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				} else {
					locObjRspdataJson=new  org.json.simple.JSONObject();
					System.out.println("edit else---------------is_resident===============" + is_resident + "-----locObjRspdataJson------------" + locObjRspdataJson);
					serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
				}
			}else{
				locObjRspdataJson=new  org.json.simple.JSONObject();
				serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new  org.json.simple.JSONObject();
				serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
			}
			
			}else{
				locObjRspdataJson=new  org.json.simple.JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);		
			}
			}else{
				locObjRspdataJson=new  org.json.simple.JSONObject();
				serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);
			}
			
			}else{
				locObjRspdataJson=new  org.json.simple.JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new  org.json.simple.JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", CompliantPost.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======CompliantPost====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, familyMemberLoginInvite an unhandled error occurred", "info", CompliantPost.class);
			locObjRspdataJson=new  org.json.simple.JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}
		
		return SUCCESS;
	}

	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, org.json.simple.JSONObject dataJson)
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
			System.out.println("=====responseMsg==="+ responseMsg);
			String as = responseMsg.toString();
			as=EncDecrypt.encrypt(as);
			System.out.println("=====as==="+as);
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
