package com.mobi.apiinitiate;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.apiinitiatevo.DeviceinfoAccesslogVO;
import com.mobi.apiinitiatevo.persistence.DeviceinfoDao;
import com.mobi.apiinitiatevo.persistence.DeviceinfoDaoservice;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.mobi.contents.ContentDao;
import com.mobi.contents.ContentDaoServices;
import com.mobi.contents.FlashNewsTblVO;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class Deviceinfo extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private String ivrparams;
	private String ivrservicecode;
	Log log = new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		//Session locObjsession = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		try {
			ivrservicecode = getText("device.info");
			//locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townshipid = null;
			String societyKey = null;
			String isfirst = null;
			String devicekey = null;
			String imeino = null;
			String deviceid = null;
			String macaddr = null;
			String serialno = null;
			String androidversion = null;
			String displayinfo = null;
			String ipaddress = null;
			String latlogitude = null;
			String additionalinfoone = null;
			String additionalinfotwo = null;
			int rid = 0;
			int jsonDbVersion = 0;
			String mobileNo = null;
			log.logMessage("ivrservicecode : " + ivrservicecode , "info", Deviceinfo.class);
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				System.out.println("ivrparams:" + ivrparams);
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				System.out.println("ivrparams:" + ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
		    		ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					if (Commonutility.checkempty(ivrservicecode)) {
						if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
							
						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.error")));
					}
					townshipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					if (Commonutility.checkempty(townshipid)) {
						if (townshipid.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					if (Commonutility.checknull(societyKey)) {
						if (Commonutility.checkLengthNotZero(societyKey)) {
							if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {
								
							} else {
								String[] passData = { getText("societykey.fixed.length") };
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
							}						
						}
						
					} else {
//						flg = false;
//						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}
					System.out.println("------------");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						isfirst = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"isfirst");
						mobileNo  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"profile_mobile");
						String locdbVersion  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"db_version");
						if (Commonutility.checkempty(isfirst)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("isfirst.error")));
						} // jsonDbVersion
						if (Commonutility.checknull(mobileNo)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("report.mobileno.error")));
						}
						if (Commonutility.checknull(locdbVersion)) {
							if (Commonutility.toCheckisNumeric(locdbVersion)) {
								jsonDbVersion = Commonutility.stringToInteger(locdbVersion);
							}
						} else {
//							flg = false;
//							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("report.mobileno.error")));
						}
						if (Commonutility.checkempty(isfirst) && isfirst.equalsIgnoreCase("yes")) {
							JSONObject locDevdatajson = new JSONObject();
							locDevdatajson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"deviceinfo");
							if (locDevdatajson != null) {
								devicekey = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"devicekey");
								if (Commonutility.checkempty(devicekey)) {
									
								} else {
//									flg = false;
//									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("devicekey.error")));
								}
								imeino = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"imeino");
								if (Commonutility.checkempty(imeino)) {
									
								} else {
//									flg = false;
//									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("imeino.error")));
								}
								deviceid = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"deviceid");
								if (Commonutility.checkempty(deviceid)) {
									
								} else {
//									flg = false;
//									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("deviceid.error")));
								}
								macaddr = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"macaddr");
								serialno = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"serialno");
								androidversion = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"androidversion");
								displayinfo = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"displayinfo");
								ipaddress = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"ipaddress");
								latlogitude = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"latlogitude");
								additionalinfoone = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"additionalinfoone");
								additionalinfotwo = (String) Commonutility.toHasChkJsonRtnValObj(locDevdatajson,"additionalinfotwo");
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("deviceinfo.error")));
							}
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			
			log.logMessage("flg : " + flg , "info", Deviceinfo.class);
			if (flg) {			
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townshipid,"");	
				System.out.println("----------townshipid check----- townshipid:" + townshipid + "--- validate flg :" + flg);
				if(flg){			
					if (Commonutility.checkempty(mobileNo)) {
						System.out.println("----------exciting mobile user login----- societyKey:" + societyKey);
//						flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
						flg = commonServ.checkSocietyKey(societyKey, "");
						System.out.println("----------exciting mobile user login----- societyKey validate flg:" + flg);
					} else {
						System.out.println("----------New mobile user login-----");
//						flg = commonServ.checkSocietyKey(societyKey, "");
					}
					if (flg) {
						DeviceinfoAccesslogVO locDevicelogObj = new DeviceinfoAccesslogVO();
						DeviceinfoDao locDeviceinfoservice = new DeviceinfoDaoservice();
						String locStatusCode = null;
						String locResponseCode = null;
						if (Commonutility.checkempty(isfirst) && isfirst.equalsIgnoreCase("yes")) {
							locDevicelogObj.setDevicekey(Commonutility.insertchkstringnull(devicekey));
							locDevicelogObj.setImeino(Commonutility.insertchkstringnull(imeino));
							locDevicelogObj.setDeviceid(Commonutility.insertchkstringnull(deviceid));
							locDevicelogObj.setMacaddr(Commonutility.insertchkstringnull(macaddr));
							locDevicelogObj.setSerialno(Commonutility.insertchkstringnull(serialno));
							locDevicelogObj.setAndroidversion(Commonutility.insertchkstringnull(androidversion));
							locDevicelogObj.setDisplayinfo(Commonutility.insertchkstringnull(displayinfo));
							locDevicelogObj.setIpaddress(Commonutility.insertchkstringnull(ipaddress));
							locDevicelogObj.setLatlogitude(Commonutility.insertchkstringnull(latlogitude));
							locDevicelogObj.setAdditionalinfoone(Commonutility.insertchkstringnull(additionalinfoone));
							locDevicelogObj.setAdditionalinfotwo(Commonutility.insertchkstringnull(additionalinfotwo));
							log.logMessage("block", "info", Deviceinfo.class);
							int locDeviceinfoUniqid = locDeviceinfoservice.toInsertDeviceinfo(locDevicelogObj);
							log.logMessage("locDeviceinfoUniqid :" + locDeviceinfoUniqid, "info", Deviceinfo.class);
							if (locDeviceinfoUniqid != -1) {
								locStatusCode = getText("status.success");
								locResponseCode = "R0001";
							} else {
								locStatusCode = getText("status.warning");
								locResponseCode = "R0006";
							}					
						} else if (Commonutility.checkempty(isfirst) && isfirst.equalsIgnoreCase("no")) {	
							locStatusCode = getText("status.success");
							locResponseCode = "R0001";
						}
						JSONObject locResDataObj = new JSONObject();
						JSONArray locResDataArObj = new JSONArray();
						String locFilepath = null;
						String locFileTypepath = null;
						locFilepath = getText("external.path") + getText("mobi.devices.info.path");
						//int dbVersion = Commonutility.stringToInteger(getText("currentdbversion"));// RPK
						//int dbVersion = Commonutility.stringToInteger(ResourceBundle.getBundle("apiversioninfo").getString("currentdbversion"));
						String lvrpropath = new Commonutility().togetFullpath("apiversioninfo.properties").replaceAll("%20", " ");
						String lvrCrntdbver = new Commonutility().toReadpropertiesfile(lvrpropath, "currentdbversion");
						int dbVersion = Commonutility.stringToInteger(lvrCrntdbver);
						System.out.println("RPK--------> " + dbVersion);
						int msgVersion = Commonutility.stringToInteger(getText("currentmsgversion"));
						int validatVersion = Commonutility.stringToInteger(getText("currentvalidversion"));
						// Message File Download
						if (dbVersion != jsonDbVersion) {
							
						}
						locFileTypepath = getText("mobi.external.msg.path") + getText("currentmsgversion") + getText("mobi.msg.valid.lengthrange.extension");
						locFilepath = locFilepath+ locFileTypepath;
						locResDataObj.put("version", getText("currentmsgversion"));
						locResDataObj.put("filetype", "2");
						locResDataObj.put("filesize", Commonutility.filesizeget(locFilepath));
//						locResDataObj.put("fileurl", getText("project.image.url.mobile") + getText("mobi.msg.url") + "?ivrparams=" + Commonutility.urlencode(getText("mobi.type.msg")));
						locResDataObj.put("fileurl", getText("SOCIAL_INDIA_BASE_URL")  + getText("mobi.msg.url") + "?ivrparams=");
						locResDataArObj.put(locResDataObj);
						// Validation details File Download
//						locFileTypepath = getText("mobi.external.lengthrange.path") + getText("currentvalidversion") + getText("mobi.msg.valid.lengthrange.extension");
						locFileTypepath = getText("mobi.external.lengthrange.path") + getText("currentvalidversion") + getText("mobi.msg.valid.lengthrange.extension");
						locFilepath = locFilepath+ locFileTypepath;
						locResDataObj = new JSONObject();
						locResDataObj.put("version", getText("currentvalidversion"));
						locResDataObj.put("filetype", "3");
						locResDataObj.put("filesize", Commonutility.filesizeget(locFilepath));
//						locResDataObj.put("fileurl", getText("project.image.url.mobile") + getText("mobi.lengthrange.url") + "?ivrparams=" + Commonutility.urlencode(getText("mobi.type.reange")));
						locResDataObj.put("fileurl", getText("SOCIAL_INDIA_BASE_URL")  + getText("mobi.lengthrange.url") + "?ivrparams=");
						locResDataArObj.put(locResDataObj);
						// Database File Download
						//locFileTypepath = getText("Mobi.external.DBversion.path") + getText("currentdbversion") + getText("Mobi.external.DBversion.path.extension"); // RPK
						locFileTypepath = getText("Mobi.external.DBversion.path") + dbVersion + getText("Mobi.external.DBversion.path.extension");
						locFilepath = locFilepath+ locFileTypepath;
						locResDataObj = new JSONObject();
						System.out.println("getText(currentdbversion)--------------------------------"+getText("currentdbversion"));
						System.out.println("dbVersion--------------------------------"+dbVersion);// RPK
						//locResDataObj.put("version", getText("currentdbversion"));
						locResDataObj.put("version", dbVersion);
						locResDataObj.put("filetype", "1");
						locResDataObj.put("filesize", Commonutility.filesizeget(locFilepath));
//						locResDataObj.put("fileurl", getText("mobi.db.url") + "?ivrparams=" + getText("mobi.type.db")+"&dbVersion=");// RPK
						locResDataObj.put("fileurl", getText("SOCIAL_INDIA_BASE_URL")  + getText("mobi.db.url") + "?ivrparams=");
						locResDataArObj.put(locResDataObj);
						locResDataObj = new JSONObject();
						locResDataObj.put("files", locResDataArObj);						
						if (Commonutility.checkempty(mobileNo)) {
							String committee = getText("Grp.committee");
							String resident = getText("Grp.resident");
							String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/";
							String societyimgPath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/society/mobile/";
							JSONArray jssonArr = new JSONArray();
							jssonArr = commonServ.getloginverify(mobileNo, committee, resident, profileimgPath, societyimgPath);
							if (jssonArr != null) {
								locResDataObj.put("societies", jssonArr);
							} else {
								locResDataObj.put("societies", "");
							}							
							
						} else {
							locResDataObj.put("societies", "");
						}
						UserMasterTblVo userTblObj = new UserMasterTblVo();
						String admin = getText("report.mobile.no.name");
						userTblObj = commonServ.adminContactNo(admin);
						if (userTblObj != null) {
							String reportMobileNo = userTblObj.getMobileNo();
							if (Commonutility.checkempty(reportMobileNo)) {
								locResDataObj.put("contact_us", reportMobileNo);
							} else {
								locResDataObj.put("contact_us", "");
							}
						} else {
							locResDataObj.put("contact_us", "");
						}
						List<WhyShouldIUpdateTblVO> listObj = new ArrayList<WhyShouldIUpdateTblVO>();
						listObj = commonServ.whyShouldIUpdateContent();
						if (listObj != null) {
							JsonSimplepackDao jsonPack = new JsonSimplepackDaoService(); 
							locResDataObj.put("prof_content", jsonPack.whyIupdateDetails(listObj));
						} else {
							JSONArray retArryData = new JSONArray();
							locResDataObj.put("prof_content", retArryData);
						}
						String serverUrl = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo");
						if(serverUrl!=null && serverUrl.length()>0){
						serverUrl=serverUrl.substring(0,serverUrl.length()-1);
						}
						locResDataObj.put("server_path", serverUrl);
//						locResDataObj.put("prof_content", "");						
//						locObjRspdataJson.put("", locResDataArObj);
						
						
						List<FlashNewsTblVO> flashNewsList=new ArrayList<FlashNewsTblVO>();
						SocietyMstTbl societyObj=new SocietyMstTbl();
						CommonMobiDao commonHbm=new CommonMobiDaoService();
						CommonUtils commonjvm=new CommonUtils();
						String dateFormat="yyyy-MM-dd HH:mm:ss";
						Date dt=new Date();
						String curDate=commonjvm.dateToStringModify(dt,dateFormat);
						String searchField="where status=1 and  expiryDate >='"+curDate+"' ";
						
						societyObj=commonServ.getSocietymstDetail(societyKey);
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
						locObjRspdataJson=new JSONObject();
						JSONArray jArray =new JSONArray();
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
							
						locObjRspdataJson.put("flash_news", jArray);
						locObjRspdataJson.put("totalrecords",totalcnt);
						}
						locResDataObj.put("flash_news_detail", locObjRspdataJson);
						
						serverResponse(ivrservicecode,locStatusCode,locResponseCode,mobiCommon.getMsg(locResponseCode),locResDataObj);
																		
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
		} catch(Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : " + ivrservicecode + ", Sorry, an unhandled error occurred", "error", Deviceinfo.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			locErrorvalStrBuil =null;locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
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
