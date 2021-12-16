package com.mobi.feed;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FeedEdit extends ActionSupport{
	
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
			ivrservicecode = getText("report.issue");
			//locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int feedPrivacy = 0;
			int feedId = 0;
			String feedMsg = null;
			String users = null;
			String totalSize = null;
			String thumbImg = "";
			String title = "";
			String pageurl = "";
			log.logMessage("Enter into FeedEdit ", "info", FeedEdit.class);
			FunctionUtility commonFn = new FunctionUtilityServices();
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
						String locFeedId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
						feedMsg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_msg");
						String locPrivacy = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"privacy");
//						users = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");
						JSONArray usersJsnArr = new JSONArray();
						usersJsnArr = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"users");//[1,2,4,7]
						totalSize = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"total_size");
						JSONArray jsonArr = new JSONArray();
						jsonArr = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"urls");
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
						}
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
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
						}
						if (Commonutility.checknull(feedMsg)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.msg.error")));
						}						
						if (Commonutility.checkempty(locPrivacy) && Commonutility.toCheckisNumeric(locPrivacy)) {
							feedPrivacy= Commonutility.stringToInteger(locPrivacy);
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
						if (Commonutility.checknull(totalSize)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.total.size.error")));
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
			log.logMessage("flg :" +flg, "info", FeedEdit.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				FeedsTblVO feedObj = new FeedsTblVO();
				UserMasterTblVo userMsterObj = new UserMasterTblVo();
				userMsterObj.setUserId(rid);
				feedObj.setUsrId(userMsterObj);
				feedObj.setFeedId(feedId);
				feedObj.setFeedMsg(feedMsg);
				feedObj.setFeedPrivacyFlg(feedPrivacy);
				feedObj.setUrlsThumbImg(thumbImg);
				feedObj.setUrlsTitle(title);
				feedObj.setUrlsPageurl(pageurl);
				FeedDAO feedserviceObj = new FeedDAOService();								
				boolean feedIdUpdate = false;
				if (feedIdUpdate) {
					serverResponse(ivrservicecode,getText("status.success"),"R0038",mobiCommon.getMsg("R0038"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", FeedEdit.class);
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
