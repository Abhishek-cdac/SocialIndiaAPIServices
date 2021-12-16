package com.mobi.addpost;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
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
import com.social.utils.Log;

public class ads_post extends ActionSupport {
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
	GroupMasterTblVo groupMst=new GroupMasterTblVo();
	mobiCommon mobiCom=new mobiCommon();
	FeedDAO feed=new FeedDAOService();
	MvpAdpostAttchTbl adPostAttach=new MvpAdpostAttchTbl();
	CategoryMasterTblVO categoryMst=new CategoryMasterTblVO();
	SkillMasterTblVO skillMst=new SkillMasterTblVO();
	MvpAdpostTbl adPostMst = new MvpAdpostTbl();
	AddPostDao adPost =new  AddPostServices();
	FeedsTblVO feedMst=new FeedsTblVO();
	FeedsTblVO feedInfo=new FeedsTblVO();
	JsonpackDao jsonDataPack = new JsonpackDaoService();
	
	public String execute(){
		
		
		/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"));
		fileUploadFileName.add("Chrysanthemum.jpg");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
		fileUploadFileName.add("Wildlife.wmv");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.jpg"));
		fileUploadFileName.add("Wildlife.jpeg");*/
		/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Desert.jpg"));
		fileUploadFileName.add("Desert.jpg");*/
		System.out.println("********ad  post Post****************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		JSONArray jsonarr = null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			//ivrparams = URLDecoder.decode(ivrparams, "UTF-8");
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("=============ivrparams=========="+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String ad_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ad_id");
				String category = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "category");
				String category_name = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "category_name");
				String ad_title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ad_title");
				String ad_desc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ad_desc");
				String ad_amount = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ad_amount");
				String ad_user_type = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ad_user_type");
				
				String ad_stitle = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ad_stitle");
				String feed_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "feed_id");
				jsonarr =(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"remove_attach");
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
				
				
				if(flg){
					boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				if(ad_id.length()==0){
					Date date;
					CommonUtils comutil=new CommonUtilsServices();
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					categoryMst.setiVOCATEGORY_ID(Integer.parseInt(category));
					adPostMst.setiVOCATEGORY_ID(categoryMst);
					userMst.setUserId(Integer.parseInt(rid));
					adPostMst.setUserId(userMst);
					adPostMst.setPostTitle(ad_title);
					adPostMst.setShortDesc(ad_stitle);
					adPostMst.setDescr(ad_desc);
					if(ad_amount!=null && !ad_amount.equalsIgnoreCase("")){
					adPostMst.setPrice(Float.parseFloat(ad_amount));
					}
					adPostMst.setBuyerSellerFlag(Integer.parseInt(ad_user_type));
					adPostMst.setActSts(1);
					adPostMst.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
					adPostMst.setEntryBy(Integer.parseInt(rid));
				boolean upResult=adPost.adPostInsert(adPostMst,fileUpload,fileUploadContentType,fileUploadFileName,category_name);
				boolean additionUpdate = adPost.additionalFeedUpdate(adPostMst);
				if(upResult==true && additionUpdate == true){
					
					/*Feed details json pack*/
					String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
					String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
					String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
					String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
					String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
					List<Object[]> feedListObj = new ArrayList<Object[]>();
					FeedsTblVO getFeedDetail = new FeedsTblVO();
					getFeedDetail = feed.getFeedDetailsByEventId(adPostMst.getPostUniqueId(),1);
					feedListObj = feed.feedDetailsProc(Integer.parseInt(rid), societykey, getFeedDetail.getFeedId(),"");								
					Object[] objList;
					for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
						objList = it.next();									
						if (objList != null) {
							locObjRspdataJson = jsonDataPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
							if (objList[0]!=null) {
								System.out.println((int)objList[0]);
							}
						}
					}
					
					if (locObjRspdataJson != null) {
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					} else {
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
				}
			}else if(ad_id.length()>0){
				boolean Result=false;String attachId =new String();
				boolean CheckAddExist=adPost.checkAddPostExist(ad_id,rid);
				if(CheckAddExist==true){
					
					for(int i=0;i<jsonarr.length();i++){
						attachId=null;
						attachId=jsonarr.getString(i);
						System.out.println("==attachId==="+attachId);
						adPostAttach=adPost.getDeleteAddPostList(attachId);
						System.out.println("==========adPostAttach=========="+adPostAttach.getIvrBnFILE_TYPE());
						if(adPostAttach.getIvrBnFILE_TYPE()==2){
							
							String videoPath =getText("external.imagesfldr.path")+"addpost/videos/"+ad_id;
							boolean delFlag=mobiCom.deleteIfFileExist(videoPath, adPostAttach.getAttachName());
							System.out.println("=======delFlag====videoPath======="+delFlag);
							String thumbpath =getText("external.imagesfldr.path")+"addpost/thumbimage/"+ad_id;
							 delFlag=mobiCom.deleteIfFileExist(thumbpath, adPostAttach.getThumbImage());
							 System.out.println("=======delFlag====thumbpath======="+delFlag);
							 
						}else if(adPostAttach.getIvrBnFILE_TYPE()==1){
							
						 String webPath=getText("external.imagesfldr.path")+"addpost/web/"+ad_id;
						 System.out.println("============webPath======webPath===="+webPath);
						 boolean delFlag=mobiCom.deleteIfFileExist(webPath, adPostAttach.getAttachName());
						 System.out.println("=======delFlag====webPath===gg===="+delFlag);
 		 		 		String mobilePath = getText("external.imagesfldr.path")+"addpost/mobile/"+ad_id;
 		 		 		 delFlag=mobiCom.deleteIfFileExist(mobilePath, adPostAttach.getAttachName());
 		 		 		System.out.println("=======delFlag====mobilePath==gg====="+delFlag);
 		 		 		
						}else{
							 String webPath=getText("external.imagesfldr.path")+"addpost/web/"+ad_id;
							 boolean delFlag=mobiCom.deleteIfFileExist(webPath, adPostAttach.getAttachName());
							 System.out.println("=======delFlag====webPath==gg====="+webPath);
	 		 		 		String mobilePath = getText("external.imagesfldr.path")+"addpost/mobile/"+ad_id;
	 		 		 		 delFlag=mobiCom.deleteIfFileExist(mobilePath, adPostAttach.getAttachName());
	 		 		 		System.out.println("=======delFlag====mobilePath==gg====="+delFlag);
						}
					System.out.println("=========resultDel========="+adPostAttach);
					boolean deleteAttach=adPost.deleteAttachInfo(attachId);
					}
					System.out.println("jsonarr removeattach--------- " + jsonarr);
					float adspostPrice  = 0.0f;
					if(ad_amount!=null && !ad_amount.equalsIgnoreCase("")){
						adspostPrice = Float.parseFloat(ad_amount);
					}
					boolean AdPostUpdateResult=adPost.addPostUpdate(fileUpload,fileUploadContentType,fileUploadFileName,ad_title,ad_stitle,category_name,adspostPrice,ad_desc,ad_id,rid,jsonarr,feed_id,category);
					adPostMst.setPostUniqueId(Integer.parseInt(ad_id));
					userMst.setUserId(Integer.parseInt(rid));
					adPostMst.setUserId(userMst);
					boolean additionUpdate = adPost.additionalFeedUpdate(adPostMst);
					
					
				if(AdPostUpdateResult==true && additionUpdate == true) {
					/*Feed details json pack*/
					String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
					String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
					String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
					String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
					String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
					List<Object[]> feedListObj = new ArrayList<Object[]>();
					FeedsTblVO getFeedDetail = new FeedsTblVO();
					getFeedDetail = feed.getFeedDetailsByEventId(Commonutility.stringToInteger(ad_id),1);
					feedListObj = feed.feedDetailsProc(Integer.parseInt(rid), societykey, Commonutility.stringToInteger(feed_id),"");								
					Object[] objList;
					for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
						objList = it.next();									
						if (objList != null) {
							locObjRspdataJson = jsonDataPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
							if (objList[0]!=null) {
								System.out.println((int)objList[0]);
							}
						}
					}
					if (locObjRspdataJson != null) {
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					} else {
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
				}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
			}
			
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);		
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);
			}
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", ads_post.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======CompliantPost====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, familyMemberLoginInvite an unhandled error occurred", "info", ads_post.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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
