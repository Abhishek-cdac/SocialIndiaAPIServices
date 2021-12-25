package com.mobi.feed;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.timelinefeedvo.FeedsViewTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FeedDelete extends ActionSupport{
	
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
			int feedViewId = 0;
			int isMyfeed = 0;
			log.logMessage("Enter into FeedDelete ", "info", FeedDelete.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("ivrparams:" + ivrparams, "info", FeedDelete.class);
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
						String locFeedId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
						String locFeedViewId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_view_id");
						String locIsMyfeed = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"is_myfeed");// feed_del_flg -1- my feed,2-share
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
						if (Commonutility.checkempty(locFeedId) && Commonutility.toCheckisNumeric(locFeedId)) {
							feedId = Commonutility.stringToInteger(locFeedId);
							if (feedId !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.id.error")));
						}
						if (Commonutility.checkempty(locFeedViewId) && Commonutility.toCheckisNumeric(locFeedViewId)) {
							feedViewId= Commonutility.stringToInteger(locFeedViewId);
							if (feedViewId !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.view.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.view.id.error")));
						}
						if (Commonutility.checkempty(locIsMyfeed) && Commonutility.toCheckisNumeric(locIsMyfeed)) {
							isMyfeed= Commonutility.stringToInteger(locIsMyfeed);
							if (isMyfeed !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.delete.feed.flg")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.delete.feed.flg")));
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
			log.logMessage("flg :" +flg, "info", FeedDelete.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				FeedsTblVO feedObj = new FeedsTblVO();
				UserMasterTblVo userMsterObj = new UserMasterTblVo();
				FeedDAO feedserviceObj = new FeedDAOService();
				userMsterObj.setUserId(rid);
				feedObj.setFeedId(feedId);
				feedObj.setUsrId(userMsterObj);				
				
				FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
				feedViewObj.setUniqId(feedViewId);
				feedViewObj.setFeedId(feedObj);
				feedViewObj.setParentViewId(feedViewId);
				
				FeedsTblVO feedDataObj = new FeedsTblVO();
				feedDataObj = feedserviceObj.feedData(feedId);
				boolean isOwnFeed = false;
				boolean isOwnShreadFeed = false;
				int isShreadflg = -1;
				if (feedDataObj != null) {
					System.out.println(rid + "<---###### entryby:" + feedDataObj.getEntryBy());
					if (Commonutility.checkIntempty(feedDataObj.getEntryBy())) {
						if (rid == feedDataObj.getEntryBy()) {
							isOwnFeed = true;
						}
					}
				}
				System.out.println("----check");
				FeedsViewTblVO feedViewDataObj = new FeedsViewTblVO();
				feedViewDataObj = feedserviceObj.feedViewData(feedViewId);
				if (feedViewDataObj != null) {
					System.out.println("----------share flg:" + feedViewDataObj.getIsShared());
					if (Commonutility.checkIntnull(feedViewDataObj.getIsShared())) {
						isShreadflg = feedViewDataObj.getIsShared();
						System.out.println("isShreadflg:" + isShreadflg);
						if (isShreadflg == 1) {
							if (rid == feedViewDataObj.getEntryBy()) {
								isOwnShreadFeed = true;
							}							
						} else {
							isShreadflg = 0;
						}
					}
				}
				System.out.println("isOwnFeed :" + isOwnFeed);
				System.out.println("isShreadflg :" + isShreadflg);
				System.out.println("isOwnShreadFeed :" + isOwnShreadFeed);
//				if (isShreadflg == 1) {
//					flg = feedserviceObj.feedViewDelete(feedViewObj,isMyfeed);
//					System.out.println("sahre View tbl chage flg:" + flg);
//					if (isOwnFeed && flg) { // own post share means delete feed from feed table
//						System.out.println("share original feed tbl chage flg:" + flg);
//						flg = feedserviceObj.feedDelete(feedObj);
//					}
//				} else if (isShreadflg == 0) {
//					if (isOwnFeed) {
//						
//					}
//					flg = feedserviceObj.feedViewDelete(feedViewObj,isMyfeed);
//					System.out.println("View tbl chage flg:" + flg);
//					if (isOwnFeed && flg) { // own post means delete feed from feed table
//						flg = feedserviceObj.feedDelete(feedObj);
//						System.out.println("feed delete flg:" + flg);
//					}
//				}	
				log.logMessage("isMyfeed flg:" + isMyfeed, "info", FeedDelete.class);
				if (isMyfeed !=0) {
					if (isMyfeed == 1) {
//						if (isShreadflg == 0) {
							if (isOwnFeed) {
								flg = feedserviceObj.feedViewDelete(feedViewObj,isMyfeed);
								System.out.println("View tbl chage flg:" + flg);
								if (flg) { // own post means delete feed from feed table
									flg = feedserviceObj.feedDelete(feedObj);
									System.out.println("feed delete flg:" + flg);
								}
							}
//						}						
					} else if(isMyfeed == 2) {
						if (isShreadflg == 1) {
							if (isOwnShreadFeed) {
								flg = feedserviceObj.feedViewDelete(feedViewObj,isMyfeed);
							}
						}						
					} else {
						flg = false;
					}
				} else {
					flg = false;
				}
				log.logMessage("response flg:" + flg, "info", FeedDelete.class);
				if (flg) {
					serverResponse(ivrservicecode,getText("status.success"),"R0036",mobiCommon.getMsg("R0036"),locObjRspdataJson);
				} else {
					serverResponse(ivrservicecode,getText("status.warning"),"R0185",mobiCommon.getMsg("R0185"),locObjRspdataJson);
				}
//				serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", FeedDelete.class);
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

