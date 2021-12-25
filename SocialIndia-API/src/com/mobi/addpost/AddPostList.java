package com.mobi.addpost;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;

public class AddPostList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	ComplaintsDao complains=new ComplaintsDaoServices();
	profileDao profile=new profileDaoServices();
	List<ComplaintsTblVO> complaintsList=new ArrayList<ComplaintsTblVO>();
	List<MvpAdpostTbl> adPostList = new ArrayList<MvpAdpostTbl>();
	List<MvpAdpostAttchTbl> adPostAttachList=new ArrayList<MvpAdpostAttchTbl>();
	private JSONArray locLBRSklJSONAryobjImgNew;
	FeedDAO feed=new FeedDAOService();
	AddPostDao adPost =new  AddPostServices();
	FeedsTblVO feedMst=new FeedsTblVO();
	FunctionUtility commonFn = new FunctionUtilityServices();
	public String execute(){
		
		//System.out.println("************adpost List services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		AddPostDao adPost =new  AddPostServices();
		JsonpackDao jsonPack = new JsonpackDaoService(); 
		JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("=============ivrparams==========="+ivrparams);

			Log log = new Log();
			log.logMessage("=============ivrparams==========="+ivrparams,"info", AddPostList.class);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String searchText = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "search_text");
				String searchFlag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "search_flag");
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
					if (Commonutility.checkempty(timestamp)) {
					} else {
						timestamp=Commonutility.timeStampRetStringVal();
					}
					
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			//System.out.println("********result*****************"+result);
			boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
			if(societyKeyCheck==true){
			int count=0;String locVrSlQry="";
			String globalSearch="";
			String whSrch = "";
			String cntSrch = "";
			if(searchText!=null && searchText.length()>0){
				
				globalSearch=" and (postTitle like('%"+searchText+"%') or descr like('%"+searchText+"%') or price like('%"+searchText+"%') "
						+ "or iVOCATEGORY_ID.iVOCATEGORY_NAME like('%"+searchText+"%') or shortDesc like('%"+searchText+"%') )";
			}
			//System.out.println("searchFlag---------------------- " + searchFlag);
			if (searchFlag!=null && searchFlag.equalsIgnoreCase("2")) {
				UserMasterTblVo userMst=new UserMasterTblVo();
				userMst=otp.checkSocietyKeyForList(societykey,rid);
				if(userMst!=null){
				int societyId=userMst.getSocietyId().getSocietyId();
				//System.out.println("societyId---------------------- " + societyId);
				whSrch = "userId.userId<>:USER_ID and userId.societyId.societyId='"+societyId+"'";
				cntSrch = "userId.userId!='"+Integer.parseInt(rid)+"' and userId.societyId.societyId='"+societyId+"'";
				} else {
				whSrch = "userId.userId<>:USER_ID";
				cntSrch = "userId.userId!='"+Integer.parseInt(rid)+"'";
				}
			} else {
				whSrch = "userId.userId=:USER_ID";
				cntSrch = "userId.userId='"+Integer.parseInt(rid)+"'";
			}
			//System.out.println("whSrch---------------------- " + whSrch);	
			adPostList=adPost.getAdPostList(rid,timestamp,startlimit,getText("total.row"),globalSearch,whSrch);
				System.out.println("=========adPostList======="+adPostList.size());
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				if( adPostList!=null && adPostList.size()>0){
					MvpAdpostTbl adPostObj;
				for (Iterator<MvpAdpostTbl> it = adPostList
						.iterator(); it.hasNext();) {
					adPostObj = it.next();
					finalJsonarr = new JSONObject();
					
					finalJsonarr.put("ad_id", Commonutility.toCheckNullEmpty(adPostObj.getPostUniqueId()));
					finalJsonarr.put("feed_category_id", Commonutility.toCheckNullEmpty(adPostObj.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));
					finalJsonarr.put("feed_category", Commonutility.toCheckNullEmpty(adPostObj.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
					finalJsonarr.put("feed_desc", Commonutility.toCheckNullEmpty(adPostObj.getDescr()));
					finalJsonarr.put("feed_title", Commonutility.toCheckNullEmpty(adPostObj.getPostTitle()));
					finalJsonarr.put("feed_stitle", Commonutility.toCheckNullEmpty(adPostObj.getShortDesc()));
					
					finalJsonarr.put("ad_user_type", Commonutility.toCheckNullEmpty(adPostObj.getBuyerSellerFlag()));
					if (adPostObj.getPrice()!= 0) {
						float priceAmt = adPostObj.getPrice();
						DecimalFormat df = new DecimalFormat("#.00");
						df.setMaximumFractionDigits(2);
						finalJsonarr.put("amount", Commonutility.toCheckNullEmpty(df.format(priceAmt)));
					} else {
						finalJsonarr.put("amount", "");
					}
					
					if (adPostObj.getBuyerSellerFlag() == 1) {
						finalJsonarr.put("post_as", "Buyer");   //need to do
					} else if (adPostObj.getBuyerSellerFlag() == 2) {
						finalJsonarr.put("post_as", "Seller");   //need to do
					}
					finalJsonarr.put("post_by", Commonutility.toCheckNullEmpty(adPostObj.getUserId().getUserId()));
					finalJsonarr.put("post_id", "");   //need to do
					String profileFirstName="",profileName; 
					String profileLastName="";
					profileFirstName=Commonutility.toCheckNullEmpty(adPostObj.getUserId().getFirstName());
					profileLastName=Commonutility.toCheckNullEmpty(adPostObj.getUserId().getLastName());
					if(!profileFirstName.equalsIgnoreCase("")){
						profileName=profileFirstName+" "+profileLastName;
					}else{
						profileName=profileLastName;
					}
					finalJsonarr.put("profile_name", Commonutility.toCheckNullEmpty(profileName));
					
					if(adPostObj.getUserId()!=null && adPostObj.getUserId().getImageNameMobile()!=null){
						finalJsonarr.put("profile_picture", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/mobile/"+adPostObj.getUserId().getUserId()+"/"+adPostObj.getUserId().getImageNameMobile());	
					}else if(adPostObj.getUserId()!=null && adPostObj.getUserId().getSocialProfileUrl()!=null){
						finalJsonarr.put("profile_picture",adPostObj.getUserId().getSocialProfileUrl());	
					}else{
					finalJsonarr.put("profile_picture","");
					}
					
					feedMst=feed.getFeedDetails(adPostObj.getPostUniqueId());
					if(feedMst!=null){
						
						int isliked = feed.getFeedIsLikedFlg(feedMst.getFeedId(), Integer.parseInt(rid));
						System.out.println("isliked-----------"+isliked);
						finalJsonarr.put("is_liked", Commonutility.toCheckNullEmpty(Commonutility.intToString(isliked)));
						System.out.println("1111111111"+feedMst.getLikeCount());
						System.out.println("feedMst.getFeedPrivacyFlg()"+feedMst.getFeedPrivacyFlg());
						System.out.println("feedMst.getFeedTime()"+feedMst.getFeedTime());
						finalJsonarr.put("like_count", Commonutility.toCheckNullEmpty(commonFn.likeCountFormat(feedMst.getLikeCount())));
						finalJsonarr.put("share_count",  Commonutility.toCheckNullEmpty(commonFn.likeCountFormat(feedMst.getShareCount())));
						finalJsonarr.put("comment_count",  Commonutility.toCheckNullEmpty(commonFn.likeCountFormat(feedMst.getCommentCount())));
						finalJsonarr.put("originator_name", Commonutility.toCheckNullEmpty(feedMst.getOriginatorName()));
						finalJsonarr.put("originator_id", Commonutility.toCheckNullEmpty(feedMst.getOriginatorId()));
						finalJsonarr.put("feed_time", commonFn.getPostedDateTime(feedMst.getFeedTime()));
						finalJsonarr.put("feed_location", Commonutility.toCheckNullEmpty(feedMst.getFeedLocation()));
						finalJsonarr.put("feed_id", Commonutility.toCheckNullEmpty(feedMst.getFeedId()));
						finalJsonarr.put("feed_type", Commonutility.toCheckNullEmpty(feedMst.getFeedType()));
						finalJsonarr.put("feed_type_id", Commonutility.toCheckNullEmpty(feedMst.getFeedTypeId()));
						finalJsonarr.put("feed_privacy_flg", Commonutility.toCheckNullEmpty(feedMst.getFeedPrivacyFlg()));
						finalJsonarr.put("is_myfeed", Commonutility.toCheckNullEmpty(feedMst.getIsMyfeed()));
						finalJsonarr.put("privacy_categories", ((JsonSimplepackDaoService) jsonDataPack).catPack(feedMst.getFeedPrivacyFlg().intValue()));
						int retFeedViewId = 0;
						int userid = 0;
						if (feedMst.getFeedPrivacyFlg() !=null && feedMst.getFeedPrivacyFlg() == 2) {
							userid = -1;
						} else if (feedMst.getFeedPrivacyFlg() !=null && feedMst.getFeedPrivacyFlg() == 3) {
							userid = -2;
						} else {
							userid = Integer.parseInt(rid);
						}
						if (searchFlag!=null && searchFlag.equalsIgnoreCase("2")) {
						retFeedViewId = adPost.getadpostFeedViewId(Integer.parseInt(Commonutility.toCheckNullEmpty(adPostObj.getUserId().getUserId())), feedMst.getSocietyId().getSocietyId(), feedMst.getFeedId(), userid,0,searchFlag);
						}else{
						retFeedViewId = adPost.getadpostFeedViewId(Integer.parseInt(rid), feedMst.getSocietyId().getSocietyId(), feedMst.getFeedId(), userid,0,searchFlag);
						}
						finalJsonarr.put("feed_view_id", Commonutility.toCheckNullEmpty(retFeedViewId));
					}else{
						finalJsonarr.put("is_liked", "");
						finalJsonarr.put("like_count", "");
						finalJsonarr.put("share_count", "");
						finalJsonarr.put("comment_count", "");
						finalJsonarr.put("originator_name","");
						finalJsonarr.put("originator_id", "");
						
						finalJsonarr.put("feed_time", "");
						finalJsonarr.put("feed_location", "");
						finalJsonarr.put("feed_id", "");
						finalJsonarr.put("feed_type", "");
						finalJsonarr.put("feed_type_id", "");
						finalJsonarr.put("feed_privacy_flg", "");
						finalJsonarr.put("is_myfeed", "");
					}
					System.out.println("finalJsonarr------------"+finalJsonarr.toString());
					int varComplaintAttach = adPostObj.getPostUniqueId();
				
				locLBRSklJSONAryobjImgNew = new JSONArray();
				
				JSONObject comVido = new JSONObject();
				JSONObject images = new JSONObject();
				JSONArray imagesArr = new JSONArray();
				JSONArray comVidoArr = new JSONArray();
				
				boolean flag=false;
				adPostAttachList=adPost.getAdPostAttachList(String.valueOf(varComplaintAttach));
				System.out.println("===adPostAttachList==="+adPostAttachList.size());
				MvpAdpostAttchTbl adPostAttachObj;
				for (Iterator<MvpAdpostAttchTbl> itObj = adPostAttachList
						.iterator(); itObj.hasNext();) {
					adPostAttachObj = itObj.next();
					flag=true;
					//finalJsonarr = new JSONObject();
					images = new JSONObject();
					comVido = new JSONObject();
						if(adPostAttachObj.getIvrBnFILE_TYPE() == 1)
						{
							//System.out.println("======sss===locLBRSklJSONAryobjImgNew===="+locLBRSklJSONAryobjImgNew.length());
							//locLBRSklJSONAryobjImgNew.put(getText("project.image.url.mobile")+"/templogo/complaint/web/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
							//String comma=",";
							if(adPostAttachObj.getIvrBnFILE_TYPE()!=null){
							images.put("img_id",String.valueOf(adPostAttachObj.getAdpostAttchId()));
							}else{
								images.put("img_id","");
							}if(adPostAttachObj.getAttachName()!=null){
							images.put("img_url",getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/mobile/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getAttachName());
							}else{
								images.put("img_url","");
							}
							/*if(locLBRSklJSONAryobjImgNew!=null && locLBRSklJSONAryobjImgNew.length()>2 ){
							images.append("img_url", comma);
							}*/
						}
						/*else{
							images.put("img_id","");
							images.put("img_url","");
						}*/
						 if(adPostAttachObj.getIvrBnFILE_TYPE() == 2)
						{
							System.out.println("==thumb_img===="+adPostAttachObj.getThumbImage());
							if(adPostAttachObj.getIvrBnFILE_TYPE()!=null){
							comVido.put("video_id",String.valueOf(adPostAttachObj.getAdpostAttchId()) );
							}else{
								comVido.put("video_id","");
							}if(adPostAttachObj.getThumbImage()!=null){
							comVido.put("thumb_img", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/thumbimage/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getThumbImage());
							}else{
								comVido.put("thumb_img","");
							}if(adPostAttachObj.getAttachName()!=null){
							comVido.put("video_url", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/videos/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getAttachName());
							}else{
								comVido.put("video_url","");
							}
							//System.out.println("==========dsf=comVido============"+comVido);
							/*String comma=",";
							if(comVido!=null && comVido.length()>1 ){
								finalJsonarr.append("thumb_img", comma);
							finalJsonarr.append("video_url", comma);*/
							//}
						}
						 /*else{
							comVido.put("video_id","");
							comVido.put("thumb_img","");
							comVido.put("video_url","");
						}*/
						 //System.out.println("==========comVidocomVidocomVido=========="+comVido);
						 
						 //System.out.println("==========imagesimages=========="+images);
						 if(images!=null && images.length()>0){
						 imagesArr.put(images);
						 finalJsonarr.put("images",imagesArr);
						 }else{
							 finalJsonarr.put("images",imagesArr); 
						 }
						 if(comVido!=null && comVido.length()>0){
						 comVidoArr.put(comVido);
						finalJsonarr.put("videos",comVidoArr);
						 }else{
							 finalJsonarr.put("videos",comVidoArr); 
						 }
						//System.out.println("ds=fd=s===");
					}				
					//locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
				if(flag==false){
					/*images.put("img_id","");
					images.put("img_url","");
					comVido.put("video_id","");
					comVido.put("thumb_img","");
					comVido.put("video_url","");
					imagesArr.put(images);
					comVidoArr.put(comVido);*/
					finalJsonarr.put("images",imagesArr);
					finalJsonarr.put("videos",comVidoArr);
				}
				
				
				System.out.println("locLBRSklJSONAryobjImg===>"+locLBRSklJSONAryobjImgNew);
				
				jArray.put(finalJsonarr);
					
				}
				locObjRspdataJson.put("ads", jArray);
				locObjRspdataJson.put("timestamp",timestamp);
				//System.out.println("cntSrch-----------" + cntSrch);
				locVrSlQry="SELECT count(postUniqueId) FROM MvpAdpostTbl where "+cntSrch+" "
						+ " and actSts=1 and entryDatetime <='"+timestamp+"' "+globalSearch+" ";
				count=profile.getInitTotal(locVrSlQry);
				//System.out.println("=======count======="+count);
				locObjRspdataJson.put("totalrecords",String.valueOf(count));
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", AddPostList.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======ComplaintList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, ComplaintList an unhandled error occurred", "info", AddPostList.class);
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


	public List<ComplaintsTblVO> getComplaintsList() {
		return complaintsList;
	}


	public void setComplaintsList(List<ComplaintsTblVO> complaintsList) {
		this.complaintsList = complaintsList;
	}

	
}
