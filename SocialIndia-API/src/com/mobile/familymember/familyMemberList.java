package com.mobile.familymember;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.social.utils.Log;

public class familyMemberList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	familyMemberDao family=new familyMemberDaoServices();
	profileDao profile=new profileDaoServices();
	List<MvpFamilymbrTbl> familtMemberList=new ArrayList<MvpFamilymbrTbl>();
	UserMasterTblVo userMst=new UserMasterTblVo();
	
	public String execute(){
		
		System.out.println("************family Member List services******************");
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
			System.out.println("======ivrparams========"+ivrparams);
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
				String search = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "search");
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
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			System.out.println("==userMst id ==="+userMst.getSocietyId().getSocietyId());
			if(userMst!=null){
			int count=0;String locVrSlQry="";
			
			String searchquery="";
			
			if(search!=null && search.length()>0 ){
				searchquery=" and (fmbrPhNo like ('%"+search+"%') or fmbrName like ('%"+search+"%') )";
			}
				
				locVrSlQry="SELECT count(fmbrTntId) FROM MvpFamilymbrTbl where status='"+1+"' and userId.userId='"+Integer.parseInt(rid)+"' "
						+ "and entryDatetime <='"+timestamp+"' "+searchquery;
				count=profile.getInitTotal(locVrSlQry);
				System.out.println("=======count======="+count);
				familtMemberList=family.getFamilyMembersList(rid,timestamp,startlimit,getText("total.row"),userMst.getSocietyId().getSocietyId(),searchquery);
				System.out.println("=========familtMemberList======="+familtMemberList.size());
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				if( familtMemberList!=null && familtMemberList.size()>0){
					MvpFamilymbrTbl familyMemObj;
				for (Iterator<MvpFamilymbrTbl> it = familtMemberList
						.iterator(); it.hasNext();) {
					familyMemObj = it.next();
					finalJsonarr = new JSONObject();
					if(familyMemObj.getFmbrTntId()!=null){
					finalJsonarr.put("fm_uid", String.valueOf(familyMemObj.getFmbrTntId()));
					}else{
						finalJsonarr.put("fm_uid", "");
					}
					if(familyMemObj.getTitleId()!=null){
						finalJsonarr.put("name_title_id", String.valueOf(familyMemObj.getTitleId().getTitleId()));	
						finalJsonarr.put("name_title_value", familyMemObj.getTitleId().getDescription());
					}else{
						finalJsonarr.put("name_title_id", "");
						finalJsonarr.put("name_title_value", "");
					}
					
					if(familyMemObj.getSocialProfileUrl()!=null){
						finalJsonarr.put("fm_picture", familyMemObj.getSocialProfileUrl());	
					}else{
						finalJsonarr.put("fm_picture", "");
					}
					if(familyMemObj.getFmbrName()!=null){
						finalJsonarr.put("fm_name", familyMemObj.getFmbrName());	
					}else{
						finalJsonarr.put("fm_name", "");
					}if(familyMemObj.getFmbrGender()!=null){
						finalJsonarr.put("gender", familyMemObj.getFmbrGender());
					}else{
						finalJsonarr.put("gender", "");
					}
					if(familyMemObj.getFmbrAge()!=null){
						finalJsonarr.put("age", String.valueOf(familyMemObj.getFmbrAge()));	
					}else{
						finalJsonarr.put("age", "");
					}
					if(familyMemObj.getBloodGroupId()!=null){
						finalJsonarr.put("bloodgrp_id", String.valueOf(familyMemObj.getBloodGroupId().getBloodGroupId()));
						finalJsonarr.put("bloodgrp_value", familyMemObj.getBloodGroupId().getBloodGroupName());
					}else{
						finalJsonarr.put("bloodgrp_id", "");
						finalJsonarr.put("bloodgrp_value", "");
					}
					if(familyMemObj.getiVOKNOWN_US_ID()!=null){
						finalJsonarr.put("relationship_id", String.valueOf(familyMemObj.getiVOKNOWN_US_ID().getiVOKNOWN_US_ID()));
						finalJsonarr.put("relationship_value", familyMemObj.getiVOKNOWN_US_ID().getiVOKNOWNUS());
					}else{
						finalJsonarr.put("relationship_id", "");
						finalJsonarr.put("relationship_value", "");
					}
					
					if(familyMemObj.getFmbrPhNo()!=null){
						finalJsonarr.put("mobile", familyMemObj.getFmbrPhNo());	
					}else{
						finalJsonarr.put("mobile", "");
					}
					
					if(familyMemObj.getIsInvited()!=null){
						finalJsonarr.put("is_invited", String.valueOf(familyMemObj.getIsInvited()));	
					}else{
						finalJsonarr.put("is_invited", "");
					}
					if(familyMemObj.getUserId().getSocietyId().getSocietyName()!=null){
						finalJsonarr.put("society_name", familyMemObj.getUserId().getSocietyId().getSocietyName());	
					}else{
						finalJsonarr.put("society_name", "");
					}
					
					jArray.put(finalJsonarr);
				}
				locObjRspdataJson.put("familydetails", jArray);
				locObjRspdataJson.put("timestamp",timestamp);
				locObjRspdataJson.put("totalrecords",String.valueOf(count));
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
			System.out.println("=======familyMemberList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, familyMemberList an unhandled error occurred", "info", familyMemberList.class);
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
