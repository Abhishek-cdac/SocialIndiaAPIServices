package com.mobi.addpost;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.familymember.familyMemberDao;
import com.mobile.familymember.familyMemberDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.paswordservice.Forgetpassword;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class AddPostDetail extends ActionSupport {
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
		
		System.out.println("************adpost details services******************");
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
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("=============ivrparams==========="+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String ad_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ad_id");
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
			boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
			if(societyKeyCheck==true){
			int count=0;String locVrSlQry="";
			
				
			adPostList=adPost.getAdPostDetailForUniqueList(rid,ad_id);
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
					finalJsonarr.put("feed_category", Commonutility.toCheckNullEmpty(adPostObj.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
					finalJsonarr.put("feed_desc", Commonutility.toCheckNullEmpty(adPostObj.getDescr()));
					finalJsonarr.put("feed_title", Commonutility.toCheckNullEmpty(adPostObj.getPostTitle()));
					finalJsonarr.put("feed_stitle", Commonutility.toCheckNullEmpty(adPostObj.getShortDesc()));
					
					finalJsonarr.put("ad_user_type", Commonutility.toCheckNullEmpty(adPostObj.getBuyerSellerFlag()));
					finalJsonarr.put("amount", Commonutility.toCheckNullEmpty(adPostObj.getPrice()));
					finalJsonarr.put("post_as", "");   //need to do
					finalJsonarr.put("post_by", Commonutility.toCheckNullEmpty(adPostObj.getUserId().getUserId()));
					finalJsonarr.put("post_id", "");   //need to do
					/*String profileFirstName="",profileName; 
					String profileLastName="";
					profileFirstName=Commonutility.toCheckNullEmpty(adPostObj.getUserId().getFirstName());
					profileLastName=Commonutility.toCheckNullEmpty(adPostObj.getUserId().getFirstName());
					if(!profileFirstName.equalsIgnoreCase("")){
						profileName=profileFirstName+" "+profileLastName;
					}else{
						profileName=profileLastName;
					}*/
					//finalJsonarr.put("profile_name", Commonutility.toCheckNullEmpty(profileName));
					if(adPostObj.getUserId()!=null && adPostObj.getUserId().getImageNameMobile()!=null){
						finalJsonarr.put("profile_picture", System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/mobile/"+adPostObj.getUserId().getUserId()+"/"+adPostObj.getUserId().getImageNameMobile());	
					}else if(adPostObj.getUserId()!=null && adPostObj.getUserId().getSocialProfileUrl()!=null){
						finalJsonarr.put("profile_picture", adPostObj.getUserId().getSocialProfileUrl());	
					}else{
					finalJsonarr.put("profile_picture","");
					}
					
					feedMst=feed.getFeedDetails(adPostObj.getPostUniqueId());
					if(feedMst!=null){
						finalJsonarr.put("is_liked", Commonutility.toCheckNullEmpty(feedMst.getIsMyfeed()));
						finalJsonarr.put("like_count", Commonutility.toCheckNullEmpty(feedMst.getLikeCount()));
						finalJsonarr.put("share_count",  Commonutility.toCheckNullEmpty(feedMst.getShareCount()));
						finalJsonarr.put("comment_count",  Commonutility.toCheckNullEmpty(feedMst.getCommentCount()));
						finalJsonarr.put("originator_name", Commonutility.toCheckNullEmpty(feedMst.getOriginatorName()));
						finalJsonarr.put("originator_id", Commonutility.toCheckNullEmpty(feedMst.getOriginatorId()));
					}else{
						finalJsonarr.put("is_liked", "");
						finalJsonarr.put("like_count", "");
						finalJsonarr.put("share_count", "");
						finalJsonarr.put("comment_count", "");
						finalJsonarr.put("originator_name","");
						finalJsonarr.put("originator_id", "");
					}
					
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
							System.out.println("======sss===locLBRSklJSONAryobjImgNew===="+locLBRSklJSONAryobjImgNew.length());
							//locLBRSklJSONAryobjImgNew.put(getText("project.image.url.mobile")+"/templogo/complaint/web/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
							//String comma=",";
							if(adPostAttachObj.getIvrBnFILE_TYPE()!=null){
							images.put("img_id",String.valueOf(adPostAttachObj.getAdpostAttchId()));
							}else{
								images.put("img_id","");
							}if(adPostAttachObj.getAttachName()!=null){
							images.put("img_url",System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/mobile/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getAttachName());
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
							comVido.put("thumb_img", System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/thumbimage/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getThumbImage());
							}else{
								comVido.put("thumb_img","");
							}if(adPostAttachObj.getAttachName()!=null){
							comVido.put("video_url", System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/videos/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getAttachName());
							}else{
								comVido.put("video_url","");
							}
							System.out.println("==========dsf=comVido============"+comVido);
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
						 System.out.println("==========comVidocomVidocomVido=========="+comVido);
						 
						 System.out.println("==========imagesimages=========="+images);
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
						System.out.println("ds=fd=s===");
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
				
				//jArray.put(finalJsonarr);
					
				}
				//locObjRspdataJson.put("ads", jArray);
				
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),finalJsonarr);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", AddPostDetail.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======ComplaintList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, ComplaintList an unhandled error occurred", "info", AddPostDetail.class);
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
