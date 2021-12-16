package com.mobile.facilityBooking;

import java.io.PrintWriter;
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
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobile.familymember.familyMemberList;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.FacilityBookingTblVO;

public class FacilityBookingList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	List<FacilityBookingTblVO> facilityList=new ArrayList<FacilityBookingTblVO>();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
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
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String facilityBookingId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "facility_booking_id");
				
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
					
					
					if (Commonutility.checkempty(timestamp)) {
					} else {
						timestamp=Commonutility.timeStampRetStringVal();
					}
					
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int count=0;String locVrSlQry="";
			String search="";
			if(facilityBookingId!=null && facilityBookingId.length()>0){
				search+=" and bookingId='"+facilityBookingId+"'";
			}
			
				int societyId=userMst.getSocietyId().getSocietyId();
				String totcountqry="select count(*) from FacilityBookingTblVO where statusFlag=1 and bookedBy="+rid+"and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') " +search;
				int totalcnt=commonHbm.getTotalCountQuery(totcountqry);
				
				locVrSlQry="from FacilityBookingTblVO where statusFlag=1 and bookedBy="+rid+"and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')"+search+" order by bookingId desc";
				facilityList=facilityhbm.getFacilityList(locVrSlQry,startlimit,getText("total.row"),timestamp);
				System.out.println("=======count======="+count);
				//docMangList=infoLibrary.getDocumentList(rid,timestamp,startlimit,getText("total.row"),societyId);
				System.out.println("=========userSkillList======="+facilityList.size());
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				String dateFormat="yyyy-MM-dd HH:mm:ss";
				CommonUtils commonjvm=new CommonUtils();
				if( facilityList!=null && facilityList.size()>0){
					FacilityBookingTblVO facilityObj;
					FeedDAO feedDao;
					for(Iterator<FacilityBookingTblVO> it=facilityList.iterator();it.hasNext();){
						facilityObj=it.next();
						finalJsonarr=new JSONObject();
						finalJsonarr.put("booking_id", facilityObj.getBookingId());
						finalJsonarr.put("title", facilityObj.getTitle());
						finalJsonarr.put("place", facilityObj.getPlace());
						
						finalJsonarr.put("start_time",commonjvm.dateToStringModify(facilityObj.getStartTime(),dateFormat));
						finalJsonarr.put("end_time", commonjvm.dateToStringModify(facilityObj.getEndTime(),dateFormat));
						if(userMst.getMobileNo()!=null){
							finalJsonarr.put("contactno", userMst.getMobileNo());
						}else{
							finalJsonarr.put("contactno","");
						}
						
						finalJsonarr.put("desc", facilityObj.getDescription());
						finalJsonarr.put("facility_id", ""+facilityObj.getFacilityId().getFacilityId());
						finalJsonarr.put("booking_status", ""+facilityObj.getBookingStatus());
						if(facilityObj.getCommiteecomment()!=null){
						finalJsonarr.put("committee_desc", facilityObj.getCommiteecomment());
						}else{
							finalJsonarr.put("committee_desc", "");
						}
						
						//get feed id
						feedDao = new FeedDAOService();
						FeedsTblVO feedsTblVO = feedDao.getFeedDetailsByFacilityBookId(facilityObj.getBookingId());
						if(feedsTblVO !=null && feedsTblVO.getFeedId()!=null){
							finalJsonarr.put("feed_id", feedsTblVO.getFeedId()+"");
						}
						else{
							finalJsonarr.put("feed_id","");
						}
						
						jArray.put(finalJsonarr);
					}
					
				locObjRspdataJson.put("bookings", jArray);
				locObjRspdataJson.put("totalrecords",totalcnt);
				locObjRspdataJson.put("timestamp",timestamp);
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}
				
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======signUpMobile====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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