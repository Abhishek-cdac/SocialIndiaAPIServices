package com.mobi.complaints;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.familymember.familyMemberDao;
import com.mobile.familymember.familyMemberDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ComplaintList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	ComplaintsDao complains=new ComplaintsDaoServices();
	profileDao profile=new profileDaoServices();
	List<ComplaintsTblVO> complaintsList=new ArrayList<ComplaintsTblVO>();
	List<ComplaintattchTblVO> complaintsAttachList=new ArrayList<ComplaintattchTblVO>();
	private JSONArray locLBRSklJSONAryobjImgNew;
	
	public String execute(){
		
		System.out.println("************ComplaintList services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="",statusflag="",searchtext="";
		String societykey =null;
		Date lvrEntrydate = null;
		Common locCommonObj = null;
		try{
			locCommonObj=new CommonDao();
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("ivrparams  "+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				 societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String post_to = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "post_to");
				String is_resident = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_resident");
				String search_text = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "search_text");
				String statusflg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "statusflg");
				String complaintId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "complaint_id");
				
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
			boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
			if(societyKeyCheck==true){
			int count=0;String locVrSlQry="";
			
			CommonDao ccc =new CommonDao();
			System.out.println("search_text============= "+search_text);
			int societyidval= (Integer)ccc.getuniqueColumnVal("SocietyMstTbl", "societyId", "activationKey", societykey);
			System.out.println("societyid============== "+societyidval);
				if(is_resident!=null && !is_resident.equalsIgnoreCase("null")&& is_resident.equalsIgnoreCase("0")){
					System.out.println("posted=========1  "+post_to);
					
					//Comiittee
				complaintsList=complains.getComplaintListPost_1(rid,timestamp,startlimit,getText("total.row"),societyidval,search_text,statusflg,post_to,complaintId);
				}
				else
				{
					//Resident 
					complaintsList=complains.getComplaintList(rid,timestamp,startlimit,getText("total.row"),search_text,statusflg,complaintId);
				}
				
				
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				if( complaintsList!=null && complaintsList.size()>0){
					ComplaintsTblVO complaintsObj;
				for (Iterator<ComplaintsTblVO> it = complaintsList
						.iterator(); it.hasNext();) {
					complaintsObj = it.next();
					finalJsonarr = new JSONObject();
					
					if(complaintsObj.getComplaintsId()!=null){
						finalJsonarr.put("complaint_id", Commonutility.toCheckNullEmpty((complaintsObj.getComplaintsId())));
						}else{
							finalJsonarr.put("complaint_id", "");
						}
					if(complaintsObj.getComplaintsDesc()!=null){
						finalJsonarr.put("desc", Commonutility.toCheckNullEmpty(complaintsObj.getComplaintsDesc()));
						}else{
							finalJsonarr.put("desc", "");
						}
					if(complaintsObj.getComplaintsDesc()!=null){
						finalJsonarr.put("committee_desc", Commonutility.toCheckNullEmpty(complaintsObj.getClosereason()));
						}else{
							finalJsonarr.put("committee_desc", "");
						}
					if(complaintsObj.getComplaintsTitle()!=null){
						finalJsonarr.put("title", Commonutility.toCheckNullEmpty(complaintsObj.getComplaintsTitle()));
						}else{
							finalJsonarr.put("title", "");
						}
					if(Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()).equalsIgnoreCase("0")){
						finalJsonarr.put("post_to",  Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()));
						finalJsonarr.put("email_id", Commonutility.toCheckNullEmpty((complaintsObj.getComplaintsOthersEmail())));
						}
					else if(Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()).equalsIgnoreCase("1")){
						finalJsonarr.put("post_to", Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()));
						}
					else{
							finalJsonarr.put("post_to", Commonutility.toCheckNullEmpty(complaintsObj.getCompliantsToFlag()));
						}
					if(complaintsObj.getStatus()!=null){
						finalJsonarr.put("status", Commonutility.toCheckNullEmpty(complaintsObj.getStatus()));
						}else{
							finalJsonarr.put("status", "");
						}
					lvrEntrydate = complaintsObj.getEntryDatetime();
					
					if(complaintsObj.getEntryDatetime()!=null){
						finalJsonarr.put("post_date", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
						}else{
							finalJsonarr.put("post_date", "");
						}
					
					finalJsonarr.put("profile_name", Commonutility.toCheckNullEmpty(complaintsObj.getUsrRegTblByFromUsrId().getFirstName()));
					finalJsonarr.put("profile_location", Commonutility.toCheckNullEmpty(complaintsObj.getUsrRegTblByFromUsrId().getAddress1()));
					finalJsonarr.put("usr_id", Commonutility.toCheckNullEmpty(complaintsObj.getUsrRegTblByFromUsrId().getUserId()));
					if(complaintsObj.getUsrRegTblByFromUsrId().getImageNameMobile()!=null){
						finalJsonarr.put("profile_pic",Commonutility.toCheckNullEmpty(System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+complaintsObj.getUsrRegTblByFromUsrId().getImageNameMobile()));}
						else
						{
							finalJsonarr.put("profile_pic","");
						}
					
					finalJsonarr.put("feed_id", Commonutility.toCheckNullEmpty(complaintsObj.getFeedId()));
				int varComplaintAttach = complaintsObj.getComplaintsId();
				
				JSONArray locLBRSklJSONAryobj=null;
				JSONArray locLBRSklJSONAryobjImg=null;
				locLBRSklJSONAryobjImgNew = new JSONArray();
				JSONArray locLBRSklJSONAryobjVid=null;
				JSONArray locLBRSklJSONAryobjVidNew=new JSONArray();
				ComplaintattchTblVO locLbrSkiltbl=null;
				Iterator locObjLbrcateglst_itr=null;
				JSONObject locInrLbrSklJSONObj=null;
				
				JSONObject comVido = new JSONObject();
				JSONObject images = new JSONObject();
				JSONArray imagesArr = new JSONArray();
				JSONArray comVidoArr = new JSONArray();
				
				boolean flag=false;
				complaintsAttachList=complains.getComplaintAttachList(varComplaintAttach);
				System.out.println("===complaintsAttachList==="+complaintsAttachList.size());
				ComplaintattchTblVO complaintsAttachObj;
				for (Iterator<ComplaintattchTblVO> itObj = complaintsAttachList
						.iterator(); itObj.hasNext();) {
					complaintsAttachObj = itObj.next();
					flag=true;
					//finalJsonarr = new JSONObject();
					images = new JSONObject();
					comVido = new JSONObject();
						if(complaintsAttachObj.getIvrBnFILE_TYPE() == 1)
						{
							System.out.println("======sss===locLBRSklJSONAryobjImgNew===="+locLBRSklJSONAryobjImgNew.length());
							//locLBRSklJSONAryobjImgNew.put(getText("project.image.url.mobile")+"/templogo/complaint/web/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
							//String comma=",";
							if(complaintsAttachObj.getIvrBnFILE_TYPE()!=null){
							images.put("img_id",String.valueOf(complaintsAttachObj.getIvrBnATTCH_ID()));
							}else{
								images.put("img_id","");
							}if(complaintsAttachObj.getIvrBnATTACH_NAME()!=null){
							images.put("img_url",System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/mobile/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
							}else{
								images.put("img_url","");
							}
							/*if(locLBRSklJSONAryobjImgNew!=null && locLBRSklJSONAryobjImgNew.length()>2 ){
							images.append("img_url", comma);
							}*/
						}
						/*else{
							images.put("img_id","");
							images.put("img_url","");
						}*/
						 if(complaintsAttachObj.getIvrBnFILE_TYPE() == 2)
						{
							System.out.println("==thumb_img===="+complaintsAttachObj.getThumbImage());
							if(complaintsAttachObj.getIvrBnFILE_TYPE()!=null){
							comVido.put("video_id",String.valueOf(complaintsAttachObj.getIvrBnATTCH_ID()) );
							}else{
								comVido.put("video_id","");
							}if(complaintsAttachObj.getThumbImage()!=null){
							comVido.put("thumb_img", System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/thumbimage/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getThumbImage());
							}else{
								comVido.put("thumb_img","");
							}if(complaintsAttachObj.getIvrBnATTACH_NAME()!=null){
							comVido.put("video_url", System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/videos/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
							}else{
								comVido.put("video_url","");
							}
							
							/*String comma=",";
							if(comVido!=null && comVido.length()>1 ){
								finalJsonarr.append("thumb_img", comma);
							finalJsonarr.append("video_url", comma);*/
							//}
						}
						 /*else{
							comVido.put("video_id","");
							comVido.put("thumb_img","");
							comVido.put("video_url","");
						}*/
						
						 if(images!=null && images.length()>0){
						 imagesArr.put(images);
						 finalJsonarr.put("images",imagesArr);
						 }else{
							 finalJsonarr.put("images",imagesArr); 
						 }
						 if(comVido!=null && comVido.length()>0){
						 comVidoArr.put(comVido);
						finalJsonarr.put("videos",comVidoArr);
						 }else{
							 finalJsonarr.put("videos",comVidoArr); 
						 }
						
					}				
					//locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
				if(flag==false){
					/*images.put("img_id","");
					images.put("img_url","");
					comVido.put("video_id","");
					comVido.put("thumb_img","");
					comVido.put("video_url","");
					imagesArr.put(images);
					comVidoArr.put(comVido);*/
					finalJsonarr.put("images",imagesArr);
					finalJsonarr.put("videos",comVidoArr);
				}	
				jArray.put(finalJsonarr);
					
				}
				locObjRspdataJson.put("complaints", jArray);
				locObjRspdataJson.put("timestamp",timestamp);
				if(is_resident!=null && !is_resident.equalsIgnoreCase("null") && is_resident.equalsIgnoreCase("0")){
					//committee
					if(statusflg!=null && !statusflg.equalsIgnoreCase("null") &&  statusflg.equalsIgnoreCase("1"))
					{
						statusflag = "in(1)";
					}
					else if(statusflg!=null && !statusflg.equalsIgnoreCase("null") &&  statusflg.equalsIgnoreCase("2"))
							{
						statusflag = "in(2,3)";
							}
					else
					{
						statusflag = "in(1,2,3)";
						
					}
					String complntTo = "";
					if(post_to!=null && !post_to.equalsIgnoreCase("null") && post_to.equalsIgnoreCase("1")){
						complntTo = " compliantsToFlag in(1,2) ";
						
					}else if(post_to!=null && !post_to.equalsIgnoreCase("null") && post_to.equalsIgnoreCase("0")){
						complntTo = " usrRegTblByFromUsrId.userId="+Integer.parseInt(rid)+" and compliantsToFlag in(0) ";
						
					}
					String serachcrit="";
					if(complaintId!=null && !complaintId.equalsIgnoreCase("")&& complaintId.length()>0){
						serachcrit+=" and complaintsId='"+complaintId+"'";
					}
					
					if(search_text!=null && !search_text.equalsIgnoreCase("null") && !search_text.equalsIgnoreCase("")){
					
						
				locVrSlQry="SELECT count(complaintsId) FROM ComplaintsTblVO where usrRegTblByFromUsrId.societyId.societyId=" +societyidval+" and status "+statusflag+" and "+complntTo+"  and entryDatetime <='"+timestamp+"' "+serachcrit
						+ "  and (" + "complaintsTitle like ('%" + search_text + "%') or " 
			             + " complaintsDesc like ('%" + search_text + "%') or"
			               + " closereason like ('%" + search_text + "%')"
			             + ")order by modifyDatetime desc";
					}
					else
					{
						locVrSlQry="SELECT count(complaintsId) FROM ComplaintsTblVO where usrRegTblByFromUsrId.societyId.societyId=" +societyidval+" and  status "+statusflag+" and "+complntTo+"   and entryDatetime <='"+timestamp+"' "+serachcrit;
					}
				}
				else
				{
					if(statusflg!=null && !statusflg.equalsIgnoreCase("null")&& statusflg.equalsIgnoreCase("1"))
					{
						statusflag = "in(1)";
					}
					else if(statusflg!=null && !statusflg.equalsIgnoreCase("null")&& statusflg.equalsIgnoreCase("2"))
							{
						statusflag = "in(2,3)";
							}
					else
					{
						statusflag = "in(1,2,3)";
					}
					String serachcrit="";
					if(complaintId!=null && !complaintId.equalsIgnoreCase("")&& complaintId.length()>0){
						serachcrit+=" and complaintsId='"+complaintId+"'";
					}
					if(search_text!=null && !search_text.equalsIgnoreCase("null") && !search_text.equalsIgnoreCase("")){
	
					locVrSlQry="SELECT count(complaintsId) FROM from ComplaintsTblVO where usrRegTblByFromUsrId.userId='"+Integer.parseInt(rid)+"' and status "+statusflag+" and compliantsToFlag <> '0' and entryDatetime <='"+timestamp+"' "+serachcrit
							+ "  and (" + "complaintsTitle like ('%" + search_text + "%') or " 
					             + " complaintsDesc like ('%" + search_text + "%') or "
					               + " closereason like ('%" + search_text + "%')"
					             + ") order by modifyDatetime desc";
				}
					else
					{
						locVrSlQry = "SELECT count(complaintsId)  from ComplaintsTblVO where usrRegTblByFromUsrId.userId='"+Integer.parseInt(rid)+"' and status "+statusflag+" and  compliantsToFlag <> '0' and entryDatetime <='"+timestamp+"' "+serachcrit
								+ "  order by modifyDatetime desc";
					}
				}
				
				

				System.out.println("countqry   "+locVrSlQry);
				
				count=profile.getInitTotal(locVrSlQry);
				System.out.println("=======count======="+count);
				locObjRspdataJson.put("totalrecords",String.valueOf(count));
				serverResponse(servicecode,"00","R0137",mobiCommon.getMsg("R0137"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", ComplaintList.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======ComplaintList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, ComplaintList an unhandled error occurred", "info", ComplaintList.class);
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


	public List<ComplaintsTblVO> getComplaintsList() {
		return complaintsList;
	}


	public void setComplaintsList(List<ComplaintsTblVO> complaintsList) {
		this.complaintsList = complaintsList;
	}

	
}
