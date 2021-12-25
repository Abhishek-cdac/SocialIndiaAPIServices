package com.mobile.familymember;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.KnownusTblVO;
import com.pack.commonvo.MvpBloodGroupTbl;
import com.pack.commonvo.MvpTitleMstTbl;
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
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.persistence.ReportDao;
import com.socialindiaservices.persistence.ReportHibernateDao;

public class familyMemberLoginInvite extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	familyMemberDao family=new familyMemberDaoServices();
	profileDao profile=new profileDaoServices();
	List<MvpFamilymbrTbl> familtMemberList=new ArrayList<MvpFamilymbrTbl>();
	MvpFamilymbrTbl familyMst=new MvpFamilymbrTbl();
	UserMasterTblVo userInfo=new UserMasterTblVo();
	UserMasterTblVo userData=new UserMasterTblVo();
	GroupMasterTblVo groupMst=new GroupMasterTblVo();
	SocietyMstTbl societyMst=new SocietyMstTbl();
	KnownusTblVO knowUsMst=new KnownusTblVO();
	UserMasterTblVo userMst=new UserMasterTblVo();
	MvpTitleMstTbl titleMst=new MvpTitleMstTbl();
	MvpBloodGroupTbl bloodGrpMst=new MvpBloodGroupTbl();
	CommonUtilsDao common=new CommonUtilsDao();	
	public String execute(){
		
		Commonutility.toWriteConsole("********family Member Login Invite*****************");
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
			Commonutility.toWriteConsole("======ivrparams========"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String fm_uid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fm_uid");
				String society_key = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "society_key");
				String invite_revoke = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "invite_revoke");
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
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(invite_revoke)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("invite_revoke")));
					}
				}
				
				if(flg){
					boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			Commonutility.toWriteConsole("********result*****************"+result);
			boolean checkSociety=otp.checkSocietyKey(societykey,rid);
			if(checkSociety==true){
				
				familyMst=family.getFamilyInfoForUserTable(rid,fm_uid);
				userMst=otp.checkDupicateMobile(society_key,familyMst.getFmbrPhNo());
				if(userMst!=null && userMst.getStatusFlag()==2 && invite_revoke.equalsIgnoreCase("1")){
					boolean upResult=family.familyMemberLoginRevoke(rid,fm_uid,invite_revoke);
					
					if(upResult==true){
						
						String playstore_url = "playstore_url";
						String play_store_url=otp.getSysParamsInfo(playstore_url);
						System.out.println("=play_store_url===="+play_store_url);
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("url", play_store_url);
						locObjRspdataJson.put("share_message",mobiCommon.getMsg("R0060").replaceAll("<playstore_url>", play_store_url));
						serverResponse(servicecode,"00","R0012",mobiCommon.getMsg("R0012"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0014",mobiCommon.getMsg("R0014"),locObjRspdataJson);
					}
					
				}else if(userMst!=null && invite_revoke.equalsIgnoreCase("1") && userMst.getStatusFlag()!=3){
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0014",mobiCommon.getMsg("R0014"),locObjRspdataJson);	
					
				}else if(invite_revoke.equalsIgnoreCase("1") && (userMst==null || userMst.getStatusFlag()==3 ) ){
					Commonutility.toWriteConsole("====familyMst=yyyyyyyyyyyyy==");
					if (Commonutility.checkempty(society_key)) {
						if (society_key.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
							
						} else {
							String[] passData = { getText("society.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("society_key")));
					}
				userData=otp.checkSocietyKeyForList(society_key,"");
				
				Commonutility.toWriteConsole("====familyMst==="+familyMst);
				Commonutility.toWriteConsole("========userData======="+userData);
				//if(familyMst!=null && userData!=null){
					//if(invite_revoke.equalsIgnoreCase("1")){
				 String password=common.getRandomval("AZ_09", 10);
					userInfo.setParentId(familyMst.getUserId().getUserId());
					userInfo.setParentChildFlag(1);
					userInfo.setFirstName(familyMst.getFmbrName());
					if(familyMst.getFmbrPswd()!=null && !familyMst.getFmbrPswd().equalsIgnoreCase("")){
					userInfo.setPassword(familyMst.getFmbrPswd());
					}else{
						userInfo.setPassword(common.stringToMD5(password));
					}
					societyMst.setSocietyId(userData.getSocietyId().getSocietyId());;
					userInfo.setSocietyId(societyMst);
					Commonutility.toWriteConsole("====ggggggggggggg=rrrr==");
					//groupMst.setGroupCode(familyMst.getUserId().getGroupCode().getGroupCode());
					 uamDao uam=new uamDaoServices();
					 GroupMasterTblVo groupobj=new GroupMasterTblVo();
					 String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.resident")+"') or groupName=upper('"+getText("Grp.resident")+"')";
					 ReportDao reportHbm=new ReportHibernateDao();
					 groupobj=reportHbm.selectGroupByQry(locSlqry);
					groupMst.setGroupCode(groupobj.getGroupCode());
					userInfo.setGroupCode(groupMst);
					if(familyMst.getiVOKNOWN_US_ID()!=null){
					knowUsMst.setiVOKNOWN_US_ID(familyMst.getiVOKNOWN_US_ID().getiVOKNOWN_US_ID());
					userInfo.setiVOKNOWN_US_ID(knowUsMst);
					}
					
					Commonutility.toWriteConsole("====ggggggggggggg==rrrrr=");
					if(familyMst.getTitleId()!=null){
					titleMst.setTitleId(familyMst.getTitleId().getTitleId());
					userInfo.setTitleId(titleMst);
					}
					if(familyMst.getBloodGroupId()!=null){
					bloodGrpMst.setBloodGroupId(familyMst.getBloodGroupId().getBloodGroupId());
					userInfo.setBloodGroupId(bloodGrpMst);
					}
					userInfo.setAccessChannel(2);
					familyMst.setFmbrTntId(familyMst.getFmbrTntId());
					userInfo.setFmbrTntId(familyMst);
					if(familyMst.getFmbrEmail()!=null && !familyMst.getFmbrEmail().equalsIgnoreCase("")){
					userInfo.setEmailId(familyMst.getFmbrEmail());
					}
					userInfo.setMobileNo(familyMst.getFmbrPhNo());
					userInfo.setStatusFlag(0);
					userInfo.setGroupUniqId("temp");
					Date date1;
					 CommonUtils comutil=new CommonUtilsServices();
						date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
						userInfo.setEntryDatetime(date1);
				boolean upResult=family.familyMemberLoginInvite(userInfo,rid,fm_uid,invite_revoke);
				
				if(upResult==true){
					String playstore_url = "playstore_url";
					String play_store_url=otp.getSysParamsInfo(playstore_url);
					System.out.println("=play_store_url===="+play_store_url);
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("url", play_store_url);
					locObjRspdataJson.put("share_message",mobiCommon.getMsg("R0060").replaceAll("<playstore_url>", play_store_url));
					serverResponse(servicecode,"00","R0012",mobiCommon.getMsg("R0012"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0014",mobiCommon.getMsg("R0014"),locObjRspdataJson);
				}
			
				}else if(invite_revoke.equalsIgnoreCase("2") ){
					boolean upResult=family.familyMemberLoginRevoke(rid,fm_uid,invite_revoke);
					if(upResult==true){
						
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"00","R0013",mobiCommon.getMsg("R0013"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0014",mobiCommon.getMsg("R0014"),locObjRspdataJson);
					}
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", familyMemberLoginInvite.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			Commonutility.toWriteConsole("=======familyMemberLoginInvite====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, familyMemberLoginInvite an unhandled error occurred", "info", familyMemberLoginInvite.class);
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
