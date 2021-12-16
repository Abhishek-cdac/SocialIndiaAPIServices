package com.mobi.feed;

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

public class ShareFeed extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
//		JSONObject  = null;// Response Data Json
		org.json.simple.JSONObject locObjRspdataJson = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int feedId = 0;
			int feedViewId = 0;
			int startLimit = 0;
			int privacy = 0;
			Date timeStamp = null;
			String locRid = null;
			String locFeedId = null;
			String locFeedViewId = null;
			String locPrivacy = null;
			String users = null;
			log.logMessage("Enter into ShareFeed ", "info", ShareFeed.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("ShareFeed ivrparams :" + ivrparams, "info", ShareFeed.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				JSONArray usersJsnArr = new JSONArray();
				FunctionUtility commonFn = new FunctionUtilityServices();
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						locFeedId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
						locFeedViewId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_view_id");
						locPrivacy = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"privacy");
//						users = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");						
						usersJsnArr = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");//[1,2,4,7]
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
						if (Commonutility.checkempty(locFeedId)) {
//							if (Commonutility.checkLengthNotZero(locFeedId)) {
								if (Commonutility.toCheckisNumeric(locFeedId)) {
									feedId = Commonutility.stringToInteger(locFeedId);
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
								}
//							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
						}
						if (Commonutility.checkempty(locFeedViewId)) {
//							if (Commonutility.checkLengthNotZero(locFeedViewId)) {
								if (Commonutility.toCheckisNumeric(locFeedViewId)) {
									feedViewId = Commonutility.stringToInteger(locFeedViewId);
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.view.id.error")));
								}
//							}
						} else {
							if (Commonutility.checkempty(locFeedId) && Commonutility.toCheckisNumeric(locFeedId) && locPrivacy!=null){
								
								
							}else{
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.view.id.error")));
							}
						}
						if (Commonutility.checknull(locPrivacy)) {
							if (Commonutility.checkLengthNotZero(locPrivacy)) {
								if (Commonutility.toCheckisNumeric(locPrivacy)) {
									privacy = Commonutility.stringToInteger(locPrivacy);
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.privacy.error")));
								}
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.privacy.error")));
						}
						if (usersJsnArr != null) {
							users = "";
							users = commonFn.jsnArryIntoStrBasedOnComma(usersJsnArr);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.users.error")));
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
			log.logMessage("flg :" +flg, "info", ShareFeed.class);
			if (flg) {
				locObjRspdataJson=new org.json.simple.JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						UserMasterTblVo usrmas = new UserMasterTblVo();
						usrmas = commonServ.getProfileDetails(rid);
						int socityId = 0;
						if (usrmas != null) {
							log.logMessage("Feed originater socity id :::" + usrmas.getSocietyId().getSocietyId() , "info", ShareFeed.class);
							socityId = usrmas.getSocietyId().getSocietyId();
						} else {
							log.logMessage("User information error :: Peronal details featch error"  , "info", ShareFeed.class);
							flg =  false;
						}
						if (flg) {
							System.out.println("locFeedViewId-----------"+locFeedViewId);
							if (Commonutility.checkempty(locFeedId) && Commonutility.toCheckisNumeric(locFeedId) && !Commonutility.checkempty(locFeedViewId)){
								int userid = 0;
								int feedPrivacy=Integer.parseInt(locPrivacy);
								if (feedPrivacy == 2) {
									userid = -1;
								} else if (feedPrivacy == 3) {
									userid = -2;
								} else {
									userid = rid;
								}
								FeedDAO feedserviceObj = new FeedDAOService();
								feedViewId = feedserviceObj.getFeedViewId(rid, socityId, feedId, userid,0);
							}
							System.out.println("feedViewId--------"+feedViewId);
							FeedDAO feedserviceObj = new FeedDAOService();
							flg = feedserviceObj.shareFeed(rid, feedId, privacy, socityId, users,feedViewId);
							System.out.println("----dsd--" + flg);
							if (flg) {
								System.out.println("update feed"); 
								FeedsTblVO feedObjas = new FeedsTblVO();// feedId
								feedObjas.setIsShared(1);
								feedObjas.setFeedId(feedId);
								System.out.println("------ sharre feedId:" + feedId);
//								commitFlg = updateFeed(feedObj);
								System.out.println("7777777");
								flg = feedserviceObj.updateFeed(feedObjas);
								System.out.println("-after u[date sfdsdf---:" + flg);
								flg = feedserviceObj.shareFeedCountUpdate(feedId);
								if (flg) {
									/**
									 * procedure to run featch data
									 */
									FeedsTblVO feedObj = new FeedsTblVO();
									JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
									// feedDetailsPack 
									JSONObject jsonOrg = new JSONObject();
//									Object[] objList;
									/**
									 *  if any change in path please update to feed list
									 */
//									objList = feedserviceObj.feedDetailsProc(rid, societyKey, feedId);
									String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
									
									String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
									String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
									String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
									String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
									
									List<Object[]> feedListObj = new ArrayList<Object[]>();
									System.out.println("----rid---"+rid+"---feedId--"+feedId+"===sockey==="+societyKey);
									feedListObj = feedserviceObj.feedDetailsProc(rid, societyKey, feedId,"");								
									System.out.println("################");
									Object[] objList;
									for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
										System.out.println("%%^%^^%%^");
										objList = it.next();									
										System.out.println("--j----");
										if (objList != null) {
											System.out.println("---d11111---333333333333333");
											locObjRspdataJson = jsonDataPack.feedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
											if (objList[0]!=null) {
												System.out.println("---l---");
												System.out.println((int)objList[0]);
											}
										}
									}																									
								}								
							}							
						}
						if (flg) {
							serverResponse(ivrservicecode,getText("status.success"),"R0149",mobiCommon.getMsg("R0149"),locObjRspdataJson);
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0150",mobiCommon.getMsg("R0150"),locObjRspdataJson);
						}
					} else {
						locObjRspdataJson=null;
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);	
					}
				} else {
					locObjRspdataJson=null;
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);	
				}								
				
			} else {
				locObjRspdataJson=null;
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			StackTraceElement[] error = ex.getStackTrace();
			for (int i = 0; i < error.length; i++) {
				log.logMessage(getText("Eex") + error[i], "error", ShareFeed.class);
			}
			locObjRspdataJson=null;
			log.logMessage(getText("Eex") + ex, "error", ShareFeed.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {			
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
			String as = responseMsg.toString();
			System.out.println("response----------------------"+as);
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
	
	

}
