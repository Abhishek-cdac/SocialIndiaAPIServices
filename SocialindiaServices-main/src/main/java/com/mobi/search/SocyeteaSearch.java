package com.mobi.search;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.publishskill.ListSearch;
import com.mobi.publishskillvo.PublishSkillTblVO;
import com.mobi.publishskillvo.persistence.PubilshSkillDao;
import com.mobi.publishskillvo.persistence.PubilshSkillDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class SocyeteaSearch extends ActionSupport{
	
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
			int startLimit = 0;
			String locRid = null;
			String locTimeStamp = null;
			String locStartLmit = null;
			String title = null;
			String searchQury = "";
			String retTimeStamp = null;
			int totCnt = 0;
			int postByflg = 0;
			log.logMessage("Enter into ListSearch ", "info", ListSearch.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("ListSearch ivrparams :" + ivrparams, "info", ListSearch.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"title");
						locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						locStartLmit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
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
						
						if (Commonutility.checknull(locTimeStamp)) {
							if (Commonutility.checkempty(locTimeStamp)) {
								retTimeStamp = locTimeStamp;
								locTimeStamp = "and entryDatetime<str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') ";
							} else {
								locTimeStamp = Commonutility.timeStampRetStringVal();
								retTimeStamp = locTimeStamp;
								locTimeStamp = "and entryDatetime<str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') ";
							}
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timestamp.error")));
						}						
						if (Commonutility.checknull(locStartLmit)) {
							if (Commonutility.checkempty(locStartLmit) && Commonutility.toCheckisNumeric(locStartLmit)) {
								startLimit = Commonutility.stringToInteger(locStartLmit);
							} else {
								startLimit = 0;
							}
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("startlimit.error")));
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
			log.logMessage("flg :" +flg, "info", ListSearch.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						if(title!=null){}else{
							title="";
						}
						
						JSONArray jsonDataList = new JSONArray();
						FeedDAO feedserviceObj = new FeedDAOService();
						List<Object[]> feedListObj = new ArrayList<Object[]>();
						System.out.println("retTimeStamp---------------->>" + retTimeStamp);
						/*PrivacyInfoTblVO privacyDetail = new PrivacyInfoTblVO();
						privacyDetail = commonServ.getDefaultPrivacyFlg(rid);
						if (privacyDetail != null) {
							postByflg = privacyDetail.getPrivacyFlg();
						}*/
						System.out.println("postByflg---------------->>" + postByflg);
						totCnt = feedserviceObj.feedListTotalCountGet(rid, societyKey, retTimeStamp, "", postByflg, "",title);
						feedListObj = feedserviceObj.feedListProcDetails(rid, societyKey, retTimeStamp, "", postByflg, "", startLimit, Commonutility.stringToInteger(getText("end.limit")),title);
						
						if (feedListObj != null) {
							JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
							/**
							 *  if any change in path please update to share feed
							 */
							String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath");
							
							String imagePathWeb = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
							String imagePathMobi = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
							String videoPath = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
							String videoPathThumb = System.getenv("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
							
							jsonDataList = jsonDataPack.feedListDetails(feedListObj, imagePathWeb,imagePathMobi,videoPath,videoPathThumb,profileimgPath);					
							
						}		
						locObjRspdataJson.put("timestamp", retTimeStamp);
						locObjRspdataJson.put("totalrecords", Commonutility.intnullChek(totCnt));
						locObjRspdataJson.put("timelinedetails", jsonDataList);
						if (totCnt != 0) {
							serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						} else {
							locObjRspdataJson = new JSONObject();
							serverResponse(ivrservicecode,getText("status.warning"),"R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", ListSearch.class);
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