package com.mobi.event;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.paswordservice.Forgetpassword;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class MakeEventContribution extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	
	public String execute(){
		
		System.out.println("************mobile otp services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("ivrparams--------------"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String eventId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_id");
				String amount = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "amount");
				
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
				EventDao eventhbm=new EventDaoServices();
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int societyId=userMst.getSocietyId().getSocietyId(); 
			String sqlqry="select count(*) from EventDispTblVO where ivrBnEVENT_ID.ivrBnEVENT_ID='"+eventId+"' and ivrBnUAMMSTtbvoobj.userId='"+rid+"' and  STR_TO_DATE(concat(ivrBnEVENT_ID.ivrBnENDDATE,ivrBnEVENT_ID.ivrBnENDTIME),'%d-%m-%Y %H:%i')>now() ";
			CommonMobiDao commonHbm=new CommonMobiDaoService();
			int count=commonHbm.getTotalCountQuery(sqlqry);
			int updstatus=-1;
			if(count>0){
			
			String query="update EventDispTblVO set contributeAmount=ifnull(contributeAmount,0)+"+amount+" where ivrBnUAMMSTtbvoobj.userId='"+rid+"' and ivrBnEVENT_ID.ivrBnEVENT_ID='"+eventId+"'";
			updstatus=eventhbm.updateRsvp(query);
			}else{
				 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			      Date dateFor = new Date();
			      Date date = dateFormat.parse(dateFormat.format(dateFor));
			      EventTblVO eventobj=new EventTblVO();
					eventobj.setIvrBnEVENT_ID(Integer.parseInt(eventId));
					
				EventDispTblVO eventdispobj=new EventDispTblVO();
				eventdispobj.setIvrBnEVENT_ID(eventobj);
				eventdispobj.setIvrBnUAMMSTtbvoobj(userMst);
				eventdispobj.setIvrBnGROUPMSTvoobj(userMst.getGroupCode());
				eventdispobj.setIvrBnEVENTSTATUS(1);
				eventdispobj.setIvrBnENTRY_BY(Integer.parseInt(rid));
				eventdispobj.setIvrBnENTRY_DATETIME(date);
				eventdispobj.setContributeAmount(Float.parseFloat(amount));
				eventdispobj.setRsvpFlag(0);
				updstatus=eventhbm.toInsertEventDispTbl(eventdispobj);
			}
					if(updstatus>0){
						try{
						EventTblVO updatedeventObj=new EventTblVO();
						String qry="from EventTblVO where ivrBnEVENT_ID="+eventId;
						updatedeventObj=eventhbm.geteventobjectByQuery(qry);
						FeedsTblVO existfeedObj = new FeedsTblVO();
						FeedDAO  feadhbm=new FeedDAOService();
						existfeedObj=feadhbm.getFeedDetailsByEventIdRid(Integer.parseInt(eventId), 10,Integer.parseInt(rid));
						FeedsTblVO feedObj = new FeedsTblVO();
						feedObj.setUsrId(userMst);
						feedObj.setFeedType(10);
						feedObj.setFeedTypeId(Integer.parseInt(eventId));
						feedObj.setFeedTitle(updatedeventObj.getIvrBnEVENT_TITLE());
						String tmpcont="";
						String eventdesc="";
						if(updatedeventObj.getFunctionTemplateId()!=null && updatedeventObj.getFunctionTemplateId().getTemplateText()!=null){
							tmpcont+=updatedeventObj.getFunctionTemplateId().getTemplateText();
						}
						if(updatedeventObj.getIvrBnEVENT_DESC()!=null){
							eventdesc=updatedeventObj.getIvrBnEVENT_DESC();
						}
						if(tmpcont!=null && tmpcont.length()>0){
							tmpcont+="<BR>"+eventdesc;
						}else{
							tmpcont+=eventdesc;
						}
						
						feedObj.setFeedDesc(tmpcont);
						
						feedObj.setPostBy(Integer.parseInt(rid));
						
						feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
						feedObj.setSocietyId(userMst.getSocietyId());
						if(userMst.getCityId()!=null){
						feedObj.setFeedLocation(userMst.getCityId().getCityName());
						}
						feedObj.setOriginatorId(Integer.parseInt(rid));
						feedObj.setEntryBy(Integer.parseInt(rid));																												
						feedObj.setFeedStatus(1);
						feedObj.setFeedPrivacyFlg(1);							
						feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setIsMyfeed(1);
						feedObj.setIsShared(0);
						if(eventId!=null && eventId.length()>0){
							JsonpackDao jsonPack = new JsonpackDaoService();
							/*Additional data insert*/
							JSONObject jsonPublishObj = new JSONObject(); 
							
							jsonPublishObj = jsonPack.eventTableJasonpackValues(Integer.parseInt(eventId));
							System.out.println("jsonPublishObj--------------"+jsonPublishObj.toString());
							if (jsonPublishObj != null) {
								feedObj.setAdditionalData(jsonPublishObj.toString());
							} 
							}else {
								feedObj.setAdditionalData("");
							}
						
						
						try{
							float contamt=Float.parseFloat(amount);
							if(existfeedObj!=null && existfeedObj.getAmount()!=null){
								feedObj.setAmount(contamt+existfeedObj.getAmount());
							}else{
								feedObj.setAmount(contamt);
							}
						}catch (Exception ed){
							System.out.println("exception--------------"+ed);
						}
						if(updatedeventObj.getIvrBnFUNCTION_ID()!=null && updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName()!=null){
							feedObj.setFeedCategory("Contribute: Rs."+feedObj.getAmount());
							}
						
						if(existfeedObj!=null){
							System.out.println("---------------feed contribute edit----------------");
							feedObj.setFeedId(existfeedObj.getFeedId());
							feadhbm.feedEdit(feedObj, null, null, null, null, null);
							
							if (flg) {
								JsonpackDao jsonPack = new JsonpackDaoService();
								String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
								String imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
								String imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
								String videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
								String videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
								List<Object[]> feedListObj = new ArrayList<Object[]>();
								feedListObj = feadhbm.feedDetailsProc(Commonutility.stringToInteger(rid), societykey, existfeedObj.getFeedId(),"");								
								Object[] objList;
								for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
									objList = it.next();									
									if (objList != null) {
										locObjRspdataJson = jsonPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
										if (objList[0]!=null) {
											System.out.println((int)objList[0]);
										}
									}
								}
							
							} else {
								locObjRspdataJson=new JSONObject();
							}
						}else{
							System.out.println("---------------feed contribute insert----------------");
							feadhbm.feedInsert(null, feedObj, null, null, null);
							
							if (flg) {
								JsonpackDao jsonPack = new JsonpackDaoService();
								FeedsTblVO getfeedObj = new FeedsTblVO();
								getfeedObj=feadhbm.getFeedDetailsByEventIdRid(Integer.parseInt(eventId), 10,Integer.parseInt(rid));
								String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
								String imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.feed.img.webpath");
								String imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.img.mobilepath");
								String videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.videopath");
								String videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.feed.video.thumbpath");
								List<Object[]> feedListObj = new ArrayList<Object[]>();
								feedListObj = feadhbm.feedDetailsProc(Commonutility.stringToInteger(rid), societykey, getfeedObj.getFeedId(),"");								
								Object[] objList;
								for(Iterator<Object[]> it=feedListObj.iterator();it.hasNext();) {
									objList = it.next();									
									if (objList != null) {
										locObjRspdataJson = jsonPack.jsonFeedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
										if (objList[0]!=null) {
											System.out.println((int)objList[0]);
										}
									}
								}
							
							} else {
								locObjRspdataJson=new JSONObject();
							}
						}
						
						}catch(Exception ex){
							System.out.println("exception in contribution feed insert---------------"+ex);
						}
						serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);
					}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",getText("R0029"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",getText("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======signUpMobile====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson);
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

	
}