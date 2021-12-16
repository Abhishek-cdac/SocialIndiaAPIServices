package com.mobi.publishskill;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.addpost.AddPostDao;
import com.mobi.addpost.AddPostServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.publishskillvo.persistence.PubilshSkillDao;
import com.mobi.publishskillvo.persistence.PubilshSkillDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.timelinefeedvo.FeedsViewTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class DeletePublish extends ActionSupport{
	
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
		boolean feedflg = true;
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int startLimit = 0;
			Date timeStamp = null;
			String locRid = null;
			String locTimeStamp = null;
			String locStartLmit = null;
			int pubSkilId = 0;
			int feedId = 0;
			log.logMessage("Enter into DeletePublish ", "info", DeletePublish.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("DeletePublish ivrparams :" + ivrparams, "info", DeletePublish.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						pubSkilId = Integer.parseInt((String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"publish_skill_id"));
						String lvrfeedid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id");
						if(lvrfeedid!=null && !lvrfeedid.equalsIgnoreCase("null") && !lvrfeedid.equalsIgnoreCase(""))
						{
						feedId = Integer.parseInt((String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_id"));
						}
						else
						{
							feedId =0;
						}
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
						if (Commonutility.checkIntempty(pubSkilId)) {
							if (pubSkilId != 0) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.id.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.id.error")));
						}
					/*	if (Commonutility.checkIntempty(feedId)) {
							if (feedId != 0) {
								
							} else {
								System.out.println("ddd===");
								flg = false;
								//locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
							}
						} else {
							System.out.println("ccc===");
							flg = false;
							//locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
						}*/
						
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
			log.logMessage("flg :" +flg, "info", DeletePublish.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						PubilshSkillDao pubskilDaoObj = new PubilshSkillDaoServices();
						FeedsTblVO feedObj = new FeedsTblVO();
						FeedDAO feedService = new FeedDAOService();
						FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
						AddPostDao adPost =new  AddPostServices();
						flg = pubskilDaoObj.deletepublish(pubSkilId);
						System.out.println("pub skill delete flg:" + flg);
						UserMasterTblVo userobj = new UserMasterTblVo();
						userobj.setUserId(rid);
							if (Commonutility.checkIntempty(feedId)) {
								if (feedId != 0) {
									
								} else {
									System.out.println("ddd===");
									feedflg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
								}
							} else {
								System.out.println("ccc===");
								feedflg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.id.error")));
							}
							if (feedflg) {
							feedObj.setFeedId(feedId);
							feedObj.setUsrId(userobj);
							feedObj=feedService.getFeedDetailsByEventId(pubSkilId,5);
//							flg = feedService.feedDelete(feedObj);
							flg = feedService.feedDelete(feedObj.getFeedId()+"", rid+"");
							System.out.println("pub skill delete feedid flg:" + flg);
							if (flg) {
								int retFeedViewId = 0;
								int userid = 0;
								if (feedObj.getFeedPrivacyFlg() == 2) {
									userid = -1;
								} else if (feedObj.getFeedPrivacyFlg() == 3) {
									userid = -2;
								} else {
									userid = rid;
								}
								retFeedViewId = adPost.getadpostFeedViewId(rid, feedObj.getSocietyId().getSocietyId(), feedObj.getFeedId(), userid,0,"1");
								if (retFeedViewId != 0) {
									feedViewObj = feedService.getFeedViewDetails(retFeedViewId);
									if (feedViewObj != null) {
										flg = feedService.feedViewDelete(feedViewObj,2);
									} 	
							}
							}
							}
						
						if (flg) {
							serverResponse(ivrservicecode,getText("status.success"),"R0071",mobiCommon.getMsg("R0071"),locObjRspdataJson);
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0072",mobiCommon.getMsg("R0072"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", DeletePublish.class);
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
