package com.mobile.login.verify;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.contents.ContentDao;
import com.mobi.contents.ContentDaoServices;
import com.mobi.contents.FlashNewsTblVO;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.otpVo.otpValidateUtillity;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.mobile.signup.signUpDao;
import com.mobile.signup.signUpDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtilFunctions;


public class loginVerify extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	UserMasterTblVo userInfo=new UserMasterTblVo();
	UserMasterTblVo userData=new UserMasterTblVo();
	profileDao profile=new profileDaoServices();
	uamDao uam = new uamDaoServices();
	String familyResult = "";
	MvpFamilymbrTbl userFamily = new MvpFamilymbrTbl();
	List<MvpFamilymbrTbl> familyInfoList=new ArrayList<MvpFamilymbrTbl>();
	List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
	Query locQryrst=null;
	String locSlqry=null;
	CommonUtils comutil=new CommonUtilsServices();
	
	GroupMasterTblVo locGrpMstrVOobj=null,locGRPMstrvo=null;
	public String execute(){
		
		Commonutility.toWriteConsole("***********login Verify services******************");
		//Session pSession = HibernateUtil.getSession();
		// Session pSession1 = HibernateUtil.getSession();
		JSONObject json = new JSONObject();
		 otpValidateUtillity otpUtill=new otpValidateUtillity();
		Integer otpcount=0;
		signUpDao signup=new signUpDaoServices();
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		String servicecode="";
		boolean flg = true;
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
		 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
		//String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
		locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		String userid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userid");
		String passwd = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "passwd");
		
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
		/*if (Commonutility.checkempty(societykey)) {
			if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
				
			} else {
				String[] passData = { getText("society.fixed.length") };
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
			}
		} else {
			flg = false;
			locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
		}*/
		if (locObjRecvdataJson !=null) {
			if (Commonutility.checkempty(userid)) {
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
			}
		}
		if (locObjRecvdataJson !=null) {
			if (Commonutility.checkempty(passwd)) {
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("password.error")));
			}
		}
		
		if(flg){
			boolean checkUserActive=false;
			boolean result=otp.checkTownshipKeyWithEmailOrMobno(townshipKey,userid);
			Commonutility.toWriteConsole("********result*****************"+result);
			if(result==true){
				/*	boolean societyKeyCheck=otp.checkSocietyKeyWithoutRid(societykey);
					if(societyKeyCheck==true){*/
				/*int committeeGrpcode=0;
				int residenteGrpcode=0;
				
				locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.committee")+"') or groupName=upper('"+getText("Grp.committee")+"')";			 
				locQryrst=pSession.createQuery(locSlqry);			
				 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
				 if(locGrpMstrVOobj!=null){
					 locGRPMstrvo=new GroupMasterTblVo();
					 committeeGrpcode=locGrpMstrVOobj.getGroupCode();
									 
				 }else{// new group create
					 uamDao uam=new uamDaoServices();
					 committeeGrpcode=uam.createnewgroup_rtnId(getText("Grp.committee"));
					 		 				
				 }	
				
					locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.resident")+"') or groupName=upper('"+getText("Grp.resident")+"')";			 
					locQryrst=pSession1.createQuery(locSlqry);			
					 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
					 if(locGrpMstrVOobj!=null){
						 locGRPMstrvo=new GroupMasterTblVo();
						 residenteGrpcode=locGrpMstrVOobj.getGroupCode();
										 
					 }else{// new group create
						 uamDao uam=new uamDaoServices();
						 residenteGrpcode=uam.createnewgroup_rtnId(getText("Grp.resident"));
						 		 				
					 }	
					 
				Commonutility.toWriteConsole("committeeGrpcode="+committeeGrpcode);*/
				String committee = getText("Grp.committee");
				String resident = getText("Grp.resident");
				int committeeGrpcode = 0;
				int residenteGrpcode = 0;
				CommonMobiDao commonMob=new CommonMobiDaoService();
				int commiteResidArr[] =commonMob.getCommiteResiIds(committee,resident);
				if (commiteResidArr.length == 2) {
					committeeGrpcode = commiteResidArr[0];
					residenteGrpcode = commiteResidArr[1];
				}
				
				checkUserActive=signup.checkUserBlockedActSts(userid,passwd,committeeGrpcode,residenteGrpcode);
				
				userData=profile.verifyLoginDetail(userid,passwd);
				if(userData!=null ){
					
				boolean checkEmailFlag=true;
				if(userid.contains("@")){
					Commonutility.toWriteConsole("=d=fd=s====");
					 checkEmailFlag=profile.verifyLoginDetailWithEmail(userid,passwd);
				}
				Commonutility.toWriteConsole("========checkEmailFlag========"+checkEmailFlag);
					
					if(checkEmailFlag==false){
					
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0033",mobiCommon.getMsg("R0033"),locObjRspdataJson);
					}	else{
						
				
							
					userInfo=profile.checkAccessPermission(userid,passwd,committeeGrpcode,residenteGrpcode);
					if(userInfo!=null){
						
						String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/";
						String societyimgPath = System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/";
						JSONArray jsonArry = new JSONArray();
						 JSONObject finalJson = new JSONObject();
						JsonpackDao jsonPack = new JsonpackDaoService();
						jsonArry = jsonPack.loginUserDetail(userid, profileimgPath, societyimgPath, committeeGrpcode, residenteGrpcode);
						Common commonhbm=new CommonDao();
						String updt="update UserMasterTblVo set loginDatetime=now(), ivrBnISONLINEFLG=1, loggedOut=0, currentLogins=currentLogins+1 where userId="+userInfo.getUserId();
						Commonutility.toWriteConsole("updt------------------"+updt);
						commonhbm.commonUpdate(updt);
					
						finalJson.put("societies", jsonArry);	
						
						List<FlashNewsTblVO> flashNewsList=new ArrayList<FlashNewsTblVO>();
						SocietyMstTbl societyObj=new SocietyMstTbl();
						CommonMobiDao commonHbm=new CommonMobiDaoService();
						CommonUtilFunctions commonjvm=new CommonUtilFunctions();
						String dateFormat="yyyy-MM-dd HH:mm:ss";
						Date dt=new Date();
						String curDate=commonjvm.dateToStringModify(dt,dateFormat);
						String searchField="where status=1 and  expiryDate >='"+curDate+"' ";
						
						societyObj=commonHbm.getSocietymstDetail(userInfo.getSocietyId().getActivationKey());
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
									finalJsonarr.put("img_url",System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/flashnews/mobile/"+flashNewsObj.getNewsId()+"/"+flashNewsObj.getNewsimageName());
								} else {
									finalJsonarr.put("img_url","");
								}
								jArray.put(finalJsonarr);
							}
							
						locObjRspdataJson.put("flash_news", jArray);
						locObjRspdataJson.put("totalrecords",totalcnt);
						}
						finalJson.put("flash_news_detail", locObjRspdataJson);
						
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),finalJson);
						
					
					}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0022",mobiCommon.getMsg("R0022"),locObjRspdataJson);	
				}
					}
				}else if(checkUserActive==true){
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0057",mobiCommon.getMsg("R0057"),locObjRspdataJson);
			}
				else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0024",mobiCommon.getMsg("R0024"),locObjRspdataJson);
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
			log.logMessage("Service code : 0003, Request values are not correct format", "info", loginVerify.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			Commonutility.toWriteConsole("=======login verify====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, login verify= an unhandled error occurred", "info", loginVerify.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}finally{
			//if(pSession1!=null){pSession1.clear();pSession1.close();pSession1 = null;}
		//if(pSession!=null){pSession.clear();pSession.close();pSession = null;}
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
