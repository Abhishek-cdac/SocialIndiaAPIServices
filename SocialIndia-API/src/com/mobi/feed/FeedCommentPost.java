package com.mobi.feed;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.FeedCommentTblVO;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FeedCommentPost extends ActionSupport{
	
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
		
		try {
			ivrservicecode = getText("report.issue");
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int feedId = 0;
			int to_usr_id = 0;
			int feed_type = 0;
			
			Date timeStamp = null;
			String Comment = null;
			String locFeedId = null;
			log.logMessage("Enter into FeedCommentPost ", "info", FeedCommentPost.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
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
						String locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						locFeedId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
						Comment = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"comment");
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
						
						String toRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"to_id");
						if (Commonutility.checkempty(toRid) && Commonutility.toCheckisNumeric(toRid)) {
							to_usr_id = Commonutility.stringToInteger(toRid);
							if (to_usr_id !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm("Error: Request to_id not valid"));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm("Error: Request to_id not valid"));
						}
						
						String feedTyp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_type");
						if (Commonutility.checkempty(feedTyp) && Commonutility.toCheckisNumeric(feedTyp)) {
							feed_type = Commonutility.stringToInteger(feedTyp);
							if (feed_type !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm("Error: Request feed_type not valid"));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm("Error: Request feed_type not valid"));
						}
						
						if (Commonutility.checkempty(locFeedId) && Commonutility.toCheckisNumeric(locFeedId)) {
							feedId = Commonutility.stringToInteger(locFeedId);
							if (rid !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.id.error")));
						}
						if (Commonutility.checkempty(Comment)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.comment.post.error")));
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
			log.logMessage("flg :" +flg, "info", FeedCommentPost.class);
			System.out.println("Exceut gsdgfgg");
			if (flg) {
				locObjRspdataJson=new JSONObject();
				FeedDAO cmtObj = new FeedDAOService();
				FeedCommentTblVO cmtPostObj = new FeedCommentTblVO();
				cmtPostObj.setComment(Commonutility.insertchkstringnull(Comment));
				cmtPostObj.setFeedId(Commonutility.insertchkintnull(feedId));
				cmtPostObj.setUsrId(Commonutility.insertchkintnull(rid));
				cmtPostObj.setStatus(1);
				cmtPostObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
				cmtPostObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
				int cmtPostId = cmtObj.commentPost(cmtPostObj);
				if (cmtPostId != -1) {
					JsonpackDao jsonDataPack = new JsonpackDaoService();
					FeedCommentTblVO commentDataObj = new FeedCommentTblVO();
					commentDataObj = cmtObj.commentData(cmtPostId);
					JSONArray jsonArry = new JSONArray();
					String externalUserImagePath = getText("external.imagesfldr.path")+getText("external.inner.mobilepath") + rid +"/";
					externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");					
					if (commentDataObj != null) {
						/*Sandy*/
						FeedDAO feedsevicehbm=new FeedDAOService();
						feedsevicehbm.commentFeedCountUpdate(feedId);
						
						FeedsTblVO feedcommentobj=new FeedsTblVO();
						feedcommentobj=cmtObj.feedData(feedId);
						locObjRspdataJson.put("comment_count", ""+feedcommentobj.getCommentCount());
						
						
						FunctionUtility common = new FunctionUtilityServices();
						locObjRspdataJson.put("comment_id", Commonutility.intToString(commentDataObj.getCommentId()));
						locObjRspdataJson.put("usr_id", Commonutility.intToString(commentDataObj.getUsrId()));
						locObjRspdataJson.put("comment", Commonutility.stringToStringempty(commentDataObj.getComment()));
						locObjRspdataJson.put("comment_time", common.getPostedDateTime(commentDataObj.getEntryDatetime()));
						UserMasterTblVo usrmas = new UserMasterTblVo();
						CommonMobiDao commonServ = new CommonMobiDaoService();
						usrmas = commonServ.getProfileDetails(commentDataObj.getUsrId());						
						locObjRspdataJson.put("usr_name", Commonutility.stringToStringempty(usrmas.getFirstName()));
						if (Commonutility.checkempty(usrmas.getImageNameMobile())) {
							locObjRspdataJson.put("usr_img", externalUserImagePath + commentDataObj.getUsrId() + "/" + usrmas.getImageNameMobile());
						}else if (Commonutility.checkempty(usrmas.getSocialProfileUrl())) {
							locObjRspdataJson.put("usr_img", usrmas.getSocialProfileUrl());
						}  else {
							locObjRspdataJson.put("usr_img", "");
						}
						
						//start: notification
						if(to_usr_id != rid){
							uamDao uam = new uamDaoServices();
							UserMasterTblVo toUserMasterTblVo = uam.getregistertable(to_usr_id);
							
							AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
							String additionaldata=additionalDatafunc.formAdditionalDataForFeedTbl(feedcommentobj);
							
							NotificationDao notificationHbm=new NotificationDaoServices();
							notificationHbm.insertnewNotificationDetails(toUserMasterTblVo, usrmas.getFirstName() + " commented on your post.", 1, feedId, feed_type, usrmas, additionaldata);
						}
						//end
						
						
					} else {
						locObjRspdataJson.put("comment_id", "");
						locObjRspdataJson.put("usr_id", "");
						locObjRspdataJson.put("comment", "");
						locObjRspdataJson.put("comment_time", "");
						locObjRspdataJson.put("usr_name", "");
						locObjRspdataJson.put("usr_img", "");
					}
					serverResponse(ivrservicecode,getText("status.success"),"R0040",mobiCommon.getMsg("R0035"),locObjRspdataJson);
				} else {
					serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0006"),locObjRspdataJson);
				}
				
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", FeedCommentPost.class);
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

