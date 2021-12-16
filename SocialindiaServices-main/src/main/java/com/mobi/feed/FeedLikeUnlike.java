package com.mobi.feed;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.mobi.common.mobiCommon;
import com.mobi.feedvo.FeedLikeUnlikeTBLVO;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FeedLikeUnlike extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		//Session locObjsession = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			//locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int feedId = 0;
			int likeStatus = 0;
			String locFeedId = "";
			log.logMessage("Enter into FeedLikeUnlike ", "info", FeedLikeUnlike.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("like unlike ivrparams:" + ivrparams, "info", FeedLikeUnlike.class);
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
						String locLikeSts = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"like_sts");
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
							if (rid !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.id.error")));
						}
						if (Commonutility.checkempty(locLikeSts) && Commonutility.toCheckisNumeric(locLikeSts)) {
							likeStatus = Commonutility.stringToInteger(locLikeSts);							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.like.sts.error")));
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
			log.logMessage("flg :" +flg, "info", FeedLikeUnlike.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				FeedDAO cmtObj = new FeedDAOService();
				FeedLikeUnlikeTBLVO feedLikeUnlikeObj = new FeedLikeUnlikeTBLVO();
				FeedsTblVO feedObj = new FeedsTblVO();
				feedLikeUnlikeObj.setFeedId(feedId);
				feedLikeUnlikeObj.setUsrId(rid);
				feedLikeUnlikeObj.setLikeUnlikeFlg(likeStatus);
				feedLikeUnlikeObj.setFeedFlg(0);
				feedLikeUnlikeObj.setStatus(1);
				feedLikeUnlikeObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
				feedLikeUnlikeObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
				int likeID = -1;
				System.out.println("enter into check is likes or nor");
				likeID = cmtObj.getIsFeedLikedOrNot(feedId, rid);
				System.out.println("is liked check :: true means liked Id:" + likeID);
				if (likeID == -1) {
					likeID = cmtObj.commentLikeUnlike(feedLikeUnlikeObj);
				} else {
					System.out.println("----else block---");
					flg = cmtObj.feedLikeUnlikeUpdateAg(likeID, likeStatus);
					System.out.println("----- update flg:" + flg);
				}
				log.logMessage("feed like id :" +likeID, "info", FeedLikeUnlike.class);
				if (likeID != -1 && flg) {
					flg = cmtObj.likeUnlikeCountUpdate(feedId,likeStatus);
					if (flg) {
						List<Object[]> likeList = new ArrayList<Object[]>();
						likeList = cmtObj.likeCountList(feedId);
						if (likeList != null && likeList.size() > 0) {
							Object[] objList;;
							for(Iterator<Object[]> it=likeList.iterator();it.hasNext();) {
								objList = it.next();
								if (objList[0]!=null) {								 
									FunctionUtility utilObj = new FunctionUtilityServices();
									System.out.println("#### Like coount :" + (Long)objList[0]);
									locObjRspdataJson.put("like_count",utilObj.likeCountFormat((Long)objList[0]));
								} else {
									locObjRspdataJson.put("like_count","");
								}
								if (objList[1]!=null) {
									String likeUserName = Commonutility.stringToStringempty((String)objList[1]);
									JSONArray jsonarry = new JSONArray();
									if (likeUserName.contains(",")) {
										String[] userArry = likeUserName.split(",");
										for (int i=0;i<userArry.length;i++) {
											jsonarry.add(userArry[i]);
										}
									} else {
										jsonarry.add(likeUserName);
									}
									locObjRspdataJson.put("recent_likes", jsonarry);
								}
							}
						} else {
							JSONArray jsonarry = new JSONArray();
							locObjRspdataJson.put("like_count","");
							locObjRspdataJson.put("recent_likes", jsonarry);
						}
					}
					locObjRspdataJson.put("feed_id", locFeedId);
					if (likeStatus==1) {
						serverResponse(ivrservicecode,getText("status.success"),"R0159",mobiCommon.getMsg("R0159"),locObjRspdataJson);
					} else if (likeStatus==2) {
						serverResponse(ivrservicecode,getText("status.success"),"R0160",mobiCommon.getMsg("R0160"),locObjRspdataJson);
					}
					
				} else {
					serverResponse(ivrservicecode,getText("status.warning"),"R0161",mobiCommon.getMsg("R0161"),locObjRspdataJson);
				}
//				serverResponse(ivrservicecode,getText("status.success"),"R0001",getText("R0001"),locObjRspdataJson);
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", FeedLikeUnlike.class);
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
