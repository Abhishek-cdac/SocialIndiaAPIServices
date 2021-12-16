package com.mobi.broadcast;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.DocumentManageTblVO;

public class BroadcastEventImageList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
	public String execute(){
		System.out.println("************Broadcas Event List services******************");
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
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String eventId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_id");
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String eventtype = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_type");
				
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
					if (Commonutility.checkempty(locTimeStamp)) {
					} else {
						locTimeStamp=Commonutility.timeStampRetStringVal();
					}
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result* 1111111****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int societyId=userMst.getSocietyId().getSocietyId();
			BroadCastDao brosdcasthbm=new BroadCastDaoService();
			List<DocumentManageTblVO> eventDocumentListObj = new ArrayList<DocumentManageTblVO>();
			String searchField=" and entryDatetime <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
			if(eventId!=null && eventId.length()>0){
				searchField+=" and eventId.ivrBnEVENT_ID ='"+eventId+"' ";
			}
			if(eventtype!=null && eventtype.length()>0){
				searchField+=" and eventId.ivrBnEVENTT_TYPE ='"+eventtype+"' ";
			}
			String query="select count(*) from DocumentManageTblVO WHERE statusFlag=1 "+searchField+" and eventId.ivrBnEVENT_CRT_USR_ID.userId='"+rid+"'";
			int totalcnt=commonHbm.getTotalCountQuery(query);
			
			query="from DocumentManageTblVO WHERE statusFlag=1 "+searchField+" and eventId.ivrBnEVENT_CRT_USR_ID.userId='"+rid+"' order by modifyDatetime desc";
			System.out.println("query--------------"+query);
			eventDocumentListObj=brosdcasthbm.getEventBbroadcastDocuumentSearchList(query, startlimit,getText("total.row"), locTimeStamp);
			JSONObject finalJsonarr=new JSONObject();
			JSONArray jArray =new JSONArray();
			locObjRspdataJson=new JSONObject();
				if(eventDocumentListObj!=null &&  eventDocumentListObj.size()>0){
					DocumentManageTblVO docObj;
					for(Iterator<DocumentManageTblVO> it=eventDocumentListObj.iterator();it.hasNext();){
						docObj=it.next();
						finalJsonarr=new JSONObject();
						finalJsonarr.put("event_id",""+docObj.getEventId().getIvrBnEVENT_ID());
						finalJsonarr.put("event_title",""+docObj.getEventId().getIvrBnEVENT_TITLE());
						
						String fileName=docObj.getDocFileName();
						String dateFolderPath=docObj.getDocDateFolderName();
						Integer documentType= docObj.getDocTypId().getIvrBnDOC_TYP_ID();
						System.out.println("fileName-------------"+fileName);
						System.out.println("dateFolderPath------------"+dateFolderPath);
						if(fileName!=null){
							String extension=commjvm.getFileExtension(fileName);
							System.out.println("extension------------"+extension);
							if(extension!=null){
								System.out.println("extension---------------"+extension);
								mobiCommon mobCom=new mobiCommon();
								 Integer fileType=mobCom.getFileExtensionType(extension);
								 finalJsonarr.put("file_type", ""+fileType);	
							}else{
								finalJsonarr.put("file_type", "9");	
								/*finalJsonarr.put("file_url", "");
								finalJsonarr.put("size", "0");*/
							}
							String docPath=System.getenv("SOCIAL_INDIA_BASE_URL") +"/externalPath/document/";
							String docsizPath=getText("external.documnet.fldr");
							String publicOrPrivateTath=getText("external.documnet.private.fldr");
							String generalpath="";
							String webpath=getText("external.inner.webpath");
							String userPath="";
								publicOrPrivateTath=getText("external.documnet.public.fldr");
								dateFolderPath=dateFolderPath+"/";
							
							if(documentType==8){
								generalpath=getText("external.documnet.maintenancedoc.fldr");
							}else{
								generalpath=getText("external.documnet.generaldoc.fldr");
								
							}
							String fileUrl=docPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
							String filesizeUrl=docsizPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+fileName;
							finalJsonarr.put("file_url",fileUrl);
							System.out.println("fileUrl-----------------"+filesizeUrl);
							System.out.println(" commjvm.getFileSizefileUrl-------------"+ commjvm.getFileSize(filesizeUrl, "MB"));
							finalJsonarr.put("size", commjvm.getFileSize(filesizeUrl, "MB"));
						
							finalJsonarr.put("file_name",fileName);
						
					}else{
						finalJsonarr.put("file_type", "");
						finalJsonarr.put("file_name", "");
						finalJsonarr.put("size", "0");
						finalJsonarr.put("file_url", "");
					}
						jArray.put(finalJsonarr);
					}
					
				locObjRspdataJson.put("docs", jArray);
				locObjRspdataJson.put("timestamp",locTimeStamp);
				locObjRspdataJson.put("totalrecords",totalcnt);
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
			System.out.println("=======Broadcast Event  List====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, BroadcastEventList an unhandled error occurred", "info", familyMemberList.class);
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