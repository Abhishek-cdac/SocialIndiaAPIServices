package com.mobi.feed;

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
import com.mobi.contents.ContentDao;
import com.mobi.contents.ContentDaoServices;
import com.mobi.contents.FlashNewsTblVO;
import com.mobi.feedvo.FeedCommentTblVO;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtilFunctions;

public class FeedList extends ActionSupport{
	
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
			ivrservicecode = getText("feed.list");
		//	locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int startLimit = 0;
			String locfeedtype = "";
			String feedCategory = null;
			int postBy = 0;
			String locTimeStamp="";
			Date timeStamp = null;
			String sqlQury = "";
			log.logMessage("Enter into FeedList ", "info", FeedList.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("Enter into FeedList ivrparams:" + ivrparams, "info", FeedList.class);
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
						locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						String locstartLmit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
						locfeedtype = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_type");
						String locfeedCategory = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"feed_category");
						String locpostby = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"post_by"); // 1-only me / 2-others / 3-both
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
							if (rid !=0 ) {
//								sqlQury = "USR_ID='" + rid + "' and ENTRY_BY='" + rid + "'";
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}																						
						if (Commonutility.checknull(locstartLmit)) {
							if (Commonutility.checkempty(locstartLmit) && Commonutility.toCheckisNumeric(locstartLmit)) {
								startLimit = Commonutility.stringToInteger(locstartLmit);	
							} else {
								startLimit = 0;
							}												
						} else {
							
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("startlimit.error")));
						}
						System.out.println("locfeedtype---------"+locfeedtype);
						if (Commonutility.checknull(locfeedtype) ) {
							System.out.println("locfeedtype---------"+locfeedtype);
							if (Commonutility.checkempty(locfeedtype) ) {
									sqlQury+=" and feed.feed_typ in (" + locfeedtype + ") ";
									System.out.println("sqlQury---------"+sqlQury);
							}							
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.type.error")));
						}
						if (Commonutility.checknull(locfeedCategory)) {
							feedCategory = locfeedCategory;
							if (Commonutility.checkempty(feedCategory)) {
								sqlQury+= " or feed.feed_category='" + feedCategory + "'";
//								sqlQury+= " feed.feed_category like '%" + feedCategory + "%'";
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.feed.category.error")));
						}
						if (Commonutility.checknull(locpostby)) {
							if (Commonutility.checkempty(locpostby) && Commonutility.toCheckisNumeric(locpostby)) {
								postBy = Commonutility.stringToInteger(locpostby);
							}							
							if (postBy != 0) {
								sqlQury+= " or feed.feed_privacy_flg='" + postBy + "'";
							}							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timeline.post.by.error")));
						}
						if (Commonutility.checknull(locTimeStamp)) {							
							System.out.println(locTimeStamp);
							if (Commonutility.checkempty(locTimeStamp)) {
								sqlQury+=" and feed.entry_datetime<str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
							} else {
								locTimeStamp = Commonutility.timeStampRetStringVal();
								sqlQury+=" and feed.entry_datetime<str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
							}
						} else {
							
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timestamp.error")));
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
			log.logMessage("flg :" +flg, "info", FeedList.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();				 
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						FeedDAO feedserviceObj = new FeedDAOService();
						List<Object[]> feedListObj = new ArrayList<Object[]>();
						//String sqlQuryCommon = "(timeview.usr_id ='" + rid + "' or timeview.usr_id is null) and scty.activation_key='" + societyKey + "' " + sqlQury;
						//sqlQury = " " + sqlQuryCommon + " group by feedatt.feed_id order by feed.entry_datetime desc limit " + startLimit + "," + Commonutility.stringToInteger(getText("end.limit"));
						//System.out.println("sqlQury::" + sqlQury);
						//sqlQury = " timeview.usr_id ='" + rid + "' or timeview.usr_id=-1 or   timeview.usr_id=-2 ) and scty.activation_key='" + societyKey + "' and feed.entry_datetime<=str_to_date('"+locTimeStamp+"','%Y-%m-%d %H:%i:%S') "+ sqlQury;
						//feedListObj = feedserviceObj.feedList(sqlQury,rid,startLimit);
//						proc call
//						int totCnt = 0;
						int totCnt = feedserviceObj.feedListTotalCountGet(rid, societyKey, locTimeStamp, locfeedtype, postBy, feedCategory,"");
						JSONArray jsonDataList = new JSONArray();
						log.logMessage("#### total count :" + totCnt, "info", FeedList.class);
						if (totCnt != 0) {
							feedListObj = feedserviceObj.feedListProcDetails(rid, societyKey, locTimeStamp, locfeedtype, postBy, feedCategory, startLimit, Commonutility.stringToInteger(getText("end.limit")),"");														
							
							if (feedListObj != null) {
								JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
								/**
								 *  if any change in path please update to share feed
								 */
								String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
								
								String imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
								String imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
								String videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
								String videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
								
								jsonDataList = jsonDataPack.feedListDetails(feedListObj, imagePathWeb,imagePathMobi,videoPath,videoPathThumb,profileimgPath);					
								
							}
						}
						
						locObjRspdataJson.put("timestamp", locTimeStamp);
//						String countQuery = "select count(FEED_ID) from mvp_timeline_feed feed where " + sqlQury;
//						countQuery = "select count(feed.FEED_ID) from mvp_timeline_feed feed "
//								+ "inner join mvp_timeline_view_tbl timeview on feed.feed_id = timeview.feed_id and feed.feed_status=1 and feed.society_id=timeview.society_id "
//								+ "left join society_mst_tbl scty on scty.society_id=feed.society_id where " + sqlQuryCommon;
//						System.out.println("countQuery:" + countQuery);
//						int totCnt = feedserviceObj.getTotalCountSqlQuery(countQuery);
						// call porc						
						locObjRspdataJson.put("totalrecords", Commonutility.intnullChek(totCnt));
//						locObjRspdataJson.put("timelinedetails", jsonDataList);
						if (totCnt != 0) {
							
							//fix: start
							for (int i = 0; i < jsonDataList.size(); i++) {
								org.json.simple.JSONObject objectInArray = (org.json.simple.JSONObject) jsonDataList.get(i);
								System.out.println("fix of 1 - feed_id:" + objectInArray.get("feed_id"));
						      
								FeedDAO cmtObj = new FeedDAOService();
								FeedCommentTblVO cmtPostListObj = new FeedCommentTblVO();
								cmtPostListObj.setUsrId(rid);
								cmtPostListObj.setFeedId(Integer.parseInt(objectInArray.get("feed_id").toString()));
								
								JsonpackDao jsonDataPack = new JsonpackDaoService();
								List<Object[]> commentDataObj = new ArrayList<Object[]>();
								commentDataObj = cmtObj.latestCommentList(cmtPostListObj, locTimeStamp, 0, 1);
								
								org.json.JSONArray jsonArry = new org.json.JSONArray();
								String externalUserImagePath = getText("external.imagesfldr.path")+getText("external.inner.mobilepath") + rid +"/";
								externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
								jsonArry = jsonDataPack.feedCommentListDetil(commentDataObj,externalUserImagePath);
								if (jsonArry != null) {
									objectInArray.put("comments", jsonArry);
								}
						    }
							locObjRspdataJson.put("timelinedetails", jsonDataList);
							//fix: end
							
							//Reset Login Flag
							uamDao uam = new uamDaoServices();
							UserMasterTblVo usr = uam.getregistertable(rid);
							locObjRspdataJson.put("resetDatetime", usr.getResetDatetime() == null ? "null" : usr.getResetDatetime() );
							
							//Flash news
							List<FlashNewsTblVO> flashNewsList=new ArrayList<FlashNewsTblVO>();
							SocietyMstTbl societyObj=new SocietyMstTbl();
							CommonMobiDao commonHbm=new CommonMobiDaoService();
							CommonUtilFunctions commonjvm=new CommonUtilFunctions();
							
							String dateFormat="yyyy-MM-dd HH:mm:ss";
							Date dt=new Date();
							String curDate=commonjvm.dateToStringModify(dt,dateFormat);
							
							String searchField="where status=1 and  expiryDate >='"+curDate+"' ";
							societyObj=commonHbm.getSocietymstDetail(societyKey);
							if(societyObj!=null && societyObj.getSocietyId()>0){
								searchField+=" and societyId.societyId='"+societyObj.getSocietyId()+"'";
							}
							
							String totcountqry="select count(*) from FlashNewsTblVO "+searchField;
							int totalcnt=commonHbm.getTotalCountQuery(totcountqry);
							
							String locVrSlQry="";
							locVrSlQry="from FlashNewsTblVO "+searchField+"  order by newsId desc";
							
							ContentDao contentHbm=new ContentDaoServices();
							flashNewsList=contentHbm.getFlashNewsList(locVrSlQry);
							
							JSONObject finalJsonarr=new JSONObject();
							org.json.JSONArray jArray =new org.json.JSONArray();
							if(flashNewsList!=null && flashNewsList.size()>0){
								FlashNewsTblVO flashNewsObj;
								for(Iterator<FlashNewsTblVO> it=flashNewsList.iterator();it.hasNext();){
									flashNewsObj=it.next();
									finalJsonarr=new JSONObject();
									finalJsonarr.put("news_id", flashNewsObj.getNewsId());
									finalJsonarr.put("society_id", flashNewsObj.getSocietyId().getSocietyId());
									finalJsonarr.put("news_content", flashNewsObj.getNewsContent());
									finalJsonarr.put("expiry_date", commonjvm.dateToStringModify(flashNewsObj.getExpiryDate(),dateFormat));
									if (Commonutility.checkempty(flashNewsObj.getTitle())) {
										finalJsonarr.put("flash_title", flashNewsObj.getTitle());
									} else {
										finalJsonarr.put("flash_title", "");
									}
									if (Commonutility.checkempty(flashNewsObj.getNewsimageName())) {
										finalJsonarr.put("img_url",getText("SOCIAL_INDIA_BASE_URL") +"/templogo/flashnews/mobile/"+flashNewsObj.getNewsId()+"/"+flashNewsObj.getNewsimageName());
									} else {
										finalJsonarr.put("img_url","");
									}
									jArray.put(finalJsonarr);
								}
							}
								
							locObjRspdataJson.put("flash_news", jArray);
							
							locVrSlQry = "SELECT count(notificationId) FROM NotificationTblVO where statusFlag='1' and readStatus=1 and userId.userId='"+usr.getUserId()+"'";
							int count = commonServ.getTotalCountQuery(locVrSlQry);
							locObjRspdataJson.put("notification_count",Commonutility.intToString(count));
							
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
			log.logMessage(getText("Eex") + ex, "error", FeedList.class);
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
