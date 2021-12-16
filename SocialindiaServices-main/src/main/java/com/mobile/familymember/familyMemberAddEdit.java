package com.mobile.familymember;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
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
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.social.utils.Log;

public class familyMemberAddEdit extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	familyMemberDao family=new familyMemberDaoServices();
	profileDao profile=new profileDaoServices();
	MvpFamilymbrTbl familtMemberMst=new MvpFamilymbrTbl();
	MvpFamilymbrTbl familtMemberInfo=new MvpFamilymbrTbl();
	UserMasterTblVo userInfoMst=new UserMasterTblVo();
	KnownusTblVO knowUsMst=new KnownusTblVO();
	MvpTitleMstTbl titleMst=new MvpTitleMstTbl();
	MvpBloodGroupTbl bloodGrpMst=new MvpBloodGroupTbl();
	
	public String execute(){
		
		System.out.println("************family MemberAdd Edit services******************");
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
			System.out.println("=======ivrparams============"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				//String add_edit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "add_edit");
				String fm_uid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fm_uid");
				
				String name_title_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "name_title_id");
				String fm_name = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fm_name");
				String gender = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "gender");
				String age = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "age");
				String bloodgrp_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "bloodgrp_id");
				String relationship_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "relationship_id");
				String mobile = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobile");
				String is_invited = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_invited");
				
				
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
			System.out.println("********result*****************"+result);
			int count=0;String locVrSlQry="";
			if(result==true){
				boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
				int curage=0;
				if(societyKeyCheck==true){
					if(age!=null && !age.equalsIgnoreCase("")){
						curage=Integer.parseInt(age);
					}
					System.out.println("curage-----------"+curage);
					System.out.println("v----------"+fm_uid);
				if(fm_uid.length()==0){
					familtMemberInfo=family.checkFamilyMobileExist(rid,mobile);
					if(familtMemberInfo!=null){
						boolean update=family.updateFamilyInfo(rid,mobile,familtMemberInfo.getFmbrTntId());
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("fm_uid", familtMemberInfo.getFmbrTntId());	
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}
					else{
					userInfoMst.setUserId(Integer.parseInt(rid));
					familtMemberMst.setUserId(userInfoMst);	
					if(relationship_id!=null && !relationship_id.equalsIgnoreCase("")){
						System.out.println("relationship_id---------------"+relationship_id);
					knowUsMst.setiVOKNOWN_US_ID(Integer.parseInt(relationship_id));
					familtMemberMst.setiVOKNOWN_US_ID(knowUsMst);
					}
					if(name_title_id!=null && !name_title_id.equalsIgnoreCase("")){
				titleMst.setTitleId(Integer.parseInt(name_title_id));
				familtMemberMst.setTitleId(titleMst);
					}
					if(bloodgrp_id!=null && !bloodgrp_id.equalsIgnoreCase("")){
				bloodGrpMst.setBloodGroupId(Integer.parseInt(bloodgrp_id));
				familtMemberMst.setBloodGroupId(bloodGrpMst);
					}
					if(fm_name!=null && !fm_name.equalsIgnoreCase("")){
						familtMemberMst.setFmbrName(fm_name);
					}
					if(gender!=null && !gender.equalsIgnoreCase("")){
						familtMemberMst.setFmbrGender(gender);
					}
					if(age!=null && !age.equalsIgnoreCase("")){
						familtMemberMst.setFmbrAge(Integer.parseInt(age));
					}
				familtMemberMst.setFmbrPhNo(mobile);
				familtMemberMst.setStatus(1);
				familtMemberMst.setEntryBy(Integer.parseInt(rid));
				familtMemberMst.setIsInvited(0);
				Date date1;
			    CommonUtils comutil=new CommonUtilsServices();
				date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				familtMemberMst.setEntryDatetime(date1);
				Integer familyId=family.getFamilyMembersAddDetails(familtMemberMst);
				if(familyId!=null && familyId>0){
					
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("fm_uid", familyId);	
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"00","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
				}
				}
				}else if(fm_uid.length()>0){
					if (locObjRecvdataJson !=null) {
						if (Commonutility.checkempty(is_invited)) {
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("is_invited")));
						}
					}
				userInfoMst.setUserId(Integer.parseInt(rid));
				familtMemberMst.setFmbrTntId(Integer.parseInt(fm_uid));
				familtMemberMst.setUserId(userInfoMst);	
				if(mobile!=null && !mobile.equalsIgnoreCase("")){
				familtMemberMst.setFmbrPhNo(mobile);
				}else{
					familtMemberMst.setFmbrPhNo("");
				}
				familtMemberMst.setFmbrName(fm_name);
				familtMemberMst.setFmbrGender(gender);
				familtMemberMst.setFmbrProfAcc(1);
				familtMemberMst.setFmbrAge(curage);
				familtMemberMst.setStatus(1);
				familtMemberMst.setEntryBy(Integer.parseInt(rid));
				System.out.println("befor update");
				boolean updateresult=family.updateFamilyMemberDetails(familtMemberMst,relationship_id,name_title_id,bloodgrp_id,age,is_invited);
				System.out.println("updateresult---------"+updateresult);
					if(updateresult==true){
						locObjRspdataJson=new JSONObject();
					/*familtMemberMst=family.getFamilyMembersEditDetails(rid,fm_uid);	
					if(familtMemberMst!=null){
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("fm_uid", familtMemberMst.getFmbrTntId());
					if(familtMemberMst.getTitleId()!=null){
						locObjRspdataJson.put("name_title_id", familtMemberMst.getFmbrTntId());
					}else{
						locObjRspdataJson.put("name_title_id", "");	
					}
					if(familtMemberMst.getFmbrName()!=null){
						locObjRspdataJson.put("fm_name", familtMemberMst.getFmbrName());
					}else{
						locObjRspdataJson.put("fm_name", "");	
					}
					
					if(familtMemberMst.getFmbrGender()!=null){
						locObjRspdataJson.put("gender", familtMemberMst.getFmbrGender());
					}else{
						locObjRspdataJson.put("gender", "");	
					}if(familtMemberMst.getFmbrAge()!=null){
						locObjRspdataJson.put("age", familtMemberMst.getFmbrAge());
					}else{
						locObjRspdataJson.put("age", "");	
					}if(familtMemberMst.getBloodGroupId()!=null){
						locObjRspdataJson.put("bloodgrp_id", familtMemberMst.getBloodGroupId().getBloodGroupId());
					}else{
						locObjRspdataJson.put("bloodgrp_id", "");	
					}if(familtMemberMst.getiVOKNOWN_US_ID()!=null){
						locObjRspdataJson.put("relationship_id", familtMemberMst.getiVOKNOWN_US_ID().getiVOKNOWN_US_ID());
					}else{
						locObjRspdataJson.put("relationship_id", "");	
					}if(familtMemberMst.getFmbrPhNo()!=null){
						locObjRspdataJson.put("mobile", familtMemberMst.getFmbrPhNo());
					}else{
						locObjRspdataJson.put("mobile", "");	
					}if(familtMemberMst.getIsInvited()!=null){
						locObjRspdataJson.put("is_invited", familtMemberMst.getIsInvited());
					}else{
						locObjRspdataJson.put("is_invited", "");	
					}*/
						locObjRspdataJson.put("fm_uid", fm_uid);	
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					/*}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);		
					}*/
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);	
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", familyMemberAddEdit.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======familyMemberAddEdit====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, familyMemberAddEdit an unhandled error occurred", "info", familyMemberAddEdit.class);
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
