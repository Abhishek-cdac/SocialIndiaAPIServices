package com.mobi.event;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.eventvo.EventDispTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class EventContributorList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	
	public String execute(){
		
		System.out.println("************mobile otp services******************");
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
				String attenderContributor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "attender_contributor");
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				
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
				List<EventDispTblVO> eventDispList=new ArrayList<EventDispTblVO>();
				EventDao eventhbm=new EventDaoServices();
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int societyId=userMst.getSocietyId().getSocietyId();
			int contFlag=Integer.parseInt(attenderContributor);
			String search="";
			if(contFlag>0 && contFlag<4){
				search+=" and rsvpFlag="+contFlag;
			}else{
				search+=" and contributeAmount>0";
			}
			JSONObject finalJsonarr=new JSONObject();
			JSONArray jArray =new JSONArray();
			
			String query="from EventDispTblVO where  ivrBnEVENT_ID.ivrBnEVENT_ID='"+eventId+"' "+search;
			
			System.out.println("query-------"+query);
			eventDispList=eventhbm.getEventDisplayList(query, startlimit, getText("total.row"), locTimeStamp);
			if(eventDispList!=null && eventDispList.size()>0){
				locObjRspdataJson=new JSONObject();
				EventDispTblVO eventDispObj;
				String formatToString="yyyy-MM-dd HH:mm:ss";
				System.out.println("eventDispList----------"+eventDispList.size());
				for(Iterator<EventDispTblVO> it=eventDispList.iterator();it.hasNext();){
					eventDispObj=it.next();
					finalJsonarr=new JSONObject();
					if(eventDispObj.getIvrBnUAMMSTtbvoobj().getFirstName()!=null){
					finalJsonarr.put("usrname", eventDispObj.getIvrBnUAMMSTtbvoobj().getFirstName());
					}else{
						finalJsonarr.put("usrname","");
					}
					finalJsonarr.put("usr_id", ""+eventDispObj.getIvrBnUAMMSTtbvoobj().getUserId());
					String externalUserImagePath = System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+eventDispObj.getIvrBnUAMMSTtbvoobj().getUserId() +"/";
					System.out.println("externalUserImagePath--------------"+externalUserImagePath);
					if(eventDispObj.getIvrBnUAMMSTtbvoobj().getImageNameMobile()!=null){
					finalJsonarr.put("usr_img",  externalUserImagePath+Commonutility.stringToStringempty(eventDispObj.getIvrBnUAMMSTtbvoobj().getImageNameMobile()));
					}else if(eventDispObj.getIvrBnUAMMSTtbvoobj().getSocialProfileUrl()!=null){
						finalJsonarr.put("usr_img",  eventDispObj.getIvrBnUAMMSTtbvoobj().getSocialProfileUrl());
						}else{
						finalJsonarr.put("usr_img","");
					}
					System.out.println("111111111111");
					finalJsonarr.put("amount", ""+eventDispObj.getContributeAmount());
					if(eventDispObj.getIvrBnUAMMSTtbvoobj().getFlatNo()!=null){
					finalJsonarr.put("flat_no", eventDispObj.getIvrBnUAMMSTtbvoobj().getFlatNo());
					}else{
						finalJsonarr.put("flat_no", "");
					}
					System.out.println("222222222222222");
					jArray.put(finalJsonarr);
				}
				
				locObjRspdataJson.put("users", jArray);
				locObjRspdataJson.put("totalrecords",eventDispList.size());
				locObjRspdataJson.put("timestamp",locTimeStamp);
				locObjRspdataJson.put("event_id",eventId);
				locObjRspdataJson.put("is_contribute",attenderContributor);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", EventContributorList.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======EventContributorList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", EventContributorList.class);
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