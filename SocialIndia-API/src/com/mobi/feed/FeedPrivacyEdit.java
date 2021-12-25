package com.mobi.feed;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FeedPrivacyEdit extends ActionSupport{
	
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
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int feedId = 0;
			int feedViewId = 0;
			int feedPrivacy = 0;
			int feedFlg = 0;
			Date timeStamp = null;
			String locRid = null;
			String users = null;
			log.logMessage("Enter into FeedPrivacyEdit ", "info", FeedPrivacyEdit.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("FeedPrivacyEdit ivrparams :" + ivrparams, "info", FeedPrivacyEdit.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				String locFeedId = null;
				String locFeedViewId = null;
				String locPrivacy = null;
				String locFeedflg = null;
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
						locFeedflg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_flg");//1-feed,2-shared feed
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
						if (Commonutility.checkempty(locFeedflg) && Commonutility.toCheckisNumeric(locFeedflg)) {
							feedFlg = Commonutility.stringToInteger(locFeedflg);
							if (feedFlg !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.privacy.edit.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.privacy.edit.error")));
						}
						if (Commonutility.checkempty(locFeedId) && Commonutility.toCheckisNumeric(locFeedId)) {
							feedId = Commonutility.stringToInteger(locFeedId);
							if (feedId !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
						}
						if (Commonutility.checkempty(locFeedViewId) && Commonutility.toCheckisNumeric(locFeedViewId)) {
							feedViewId = Commonutility.stringToInteger(locFeedViewId);
							if (feedViewId !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.view.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.view.id.error")));
						}
						if (Commonutility.checkempty(locPrivacy)) {
							if (Commonutility.toCheckisNumeric(locPrivacy)) {
								feedPrivacy = Commonutility.stringToInteger(locPrivacy);
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.privacy.error")));
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
			log.logMessage("flg :" +flg, "info", FeedPrivacyEdit.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						int retFeedViewId = 0;
						JSONObject jsonObj = new JSONObject();
						FeedsTblVO feedObj = new FeedsTblVO();
						FeedDAO feedserviceObj = new FeedDAOService();
						UserMasterTblVo userMsterObj = new UserMasterTblVo();
						userMsterObj.setUserId(rid);
						log.logMessage("feedPrivacy :" +feedPrivacy, "info", FeedPrivacyEdit.class);
						if (feedFlg == 1) { // feed privacy update
							feedObj.setFeedId(feedId);
							feedObj.setUsrId(userMsterObj);
							feedObj.setFeedPrivacyFlg(feedPrivacy);
							// get socity id
							UserMasterTblVo usrmas = new UserMasterTblVo();
							usrmas = commonServ.getProfileDetails(rid);
							int societyId = 0;
							if (usrmas != null) {
								log.logMessage("Feed societyId:::" + usrmas.getSocietyId().getSocietyId() , "info", FeedPrivacyEdit.class);								
								feedObj.setOriginatorName(Commonutility.stringToStringempty(usrmas.getFirstName()));
								SocietyMstTbl socityObj = new SocietyMstTbl();
								if (usrmas.getSocietyId() != null) {
									socityObj.setSocietyId(usrmas.getSocietyId().getSocietyId());
									societyId = usrmas.getSocietyId().getSocietyId();
								}								
								feedObj.setSocietyId(socityObj);
								if (feedObj.getSocietyId() != null) {
									flg = false;
								}
							} else {
								log.logMessage("User feed privacy edit information error :: Peronal details featch error"  , "info", FeedPrivacyEdit.class);
								flg =  false;
							}
							feedObj.setEntryBy(rid);
							flg = feedserviceObj.feedEdit(feedObj, "", null, null, null, null);	
							if (flg) {
								int userid = 0;
								if (feedPrivacy == 2) {
									userid = -1;
								} else if (feedPrivacy == 3) {
									userid = -2;
								} else {
									userid = rid;
								}
								retFeedViewId = feedserviceObj.getFeedViewId(rid, societyId, feedId, userid,0);
								locObjRspdataJson.put("feed_view_id", Commonutility.intToString(retFeedViewId));
								System.out.println("retFeedViewId::" + retFeedViewId);
							}
							
						} else if (feedFlg == 2) { // shared feed privacy update
							// get socity id
							log.logMessage(":::Enter into feed shared privacy check:::" , "info", FeedPrivacyEdit.class);	
							UserMasterTblVo usrmas = new UserMasterTblVo();
							usrmas = commonServ.getProfileDetails(rid);
							int societyId = 0;
							if (usrmas != null) {
								log.logMessage("Feed societyId:::" + usrmas.getSocietyId().getSocietyId() , "info", FeedPrivacyEdit.class);								
								if (usrmas.getSocietyId() != null) {
									societyId = usrmas.getSocietyId().getSocietyId();
								}
								if (societyId==0) {
									flg = false;
								}
							} else {
								log.logMessage("User feed privacy edit information error :: Peronal details featch error"  , "info", FeedPrivacyEdit.class);
								flg =  false;
							}	
							if (flg) {
								flg = feedserviceObj.sharedFeedPrivacyEdit(rid, feedId, feedViewId, feedPrivacy, societyId, users);
							}
							if (flg) {
								int userid = 0;
								if (feedPrivacy == 2) {
									userid = -1;
								} else if (feedPrivacy == 3) {
									userid = -2;
								} else {
									userid = rid;
								}
								retFeedViewId = feedserviceObj.getFeedViewId(rid, societyId, feedId, userid,1);
								locObjRspdataJson.put("feed_view_id", Commonutility.intToString(retFeedViewId));
								System.out.println("retFeedViewId::" + retFeedViewId);
							}
						}
						if (flg) {
							serverResponse(ivrservicecode,getText("status.success"),"R0162",mobiCommon.getMsg("R0162"),locObjRspdataJson);
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0163",mobiCommon.getMsg("R0163"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", FeedPrivacyEdit.class);
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
