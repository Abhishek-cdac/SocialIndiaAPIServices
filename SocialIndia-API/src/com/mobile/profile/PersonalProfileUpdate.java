package com.mobile.profile;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.otpVo.otpValidateUtillity;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;

public class PersonalProfileUpdate extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	public String execute(){
		
		System.out.println("***********Personal Profile Update services******************");
		JSONObject json = new JSONObject();
		 otpValidateUtillity otpUtill=new otpValidateUtillity();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		MvpUsrSkillTbl userSkillInfo=new MvpUsrSkillTbl();
		UserMasterTblVo userData=new UserMasterTblVo();
		CategoryMasterTblVO categoryMst=new CategoryMasterTblVO();
		SkillMasterTblVO skillMst=new SkillMasterTblVO();
		profileDao profile=new profileDaoServices();
		List<MvpUsrSkillTbl> userSkillList=new ArrayList<MvpUsrSkillTbl>();
		String servicecode="";
		boolean flg = true;
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("====ivrparams====="+ivrparams);
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
		
		JSONArray skilldetails = new JSONArray();
		skilldetails = (JSONArray) Commonutility .toHasChkJsonRtnValObj(locObjRecvdataJson, "skilldetails");
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
			
			Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			int count=0;String locVrSlQry="";
			if(result==true){
				boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
				if(societyKeyCheck==true){
				
				//if(countCheck>0){
					boolean upresult=true;
					JSONObject skillInfo=new JSONObject();
					for(int i =0 ;i<skilldetails.length();i++)
					{
						userSkillInfo=new MvpUsrSkillTbl();
						String pskill_id = (String)Commonutility.toHasChkJsonRtnValObj(skilldetails.getJSONObject(i),"pskill_id");
						String pcate_id = (String)Commonutility.toHasChkJsonRtnValObj(skilldetails.getJSONObject(i),"pcate_id");
						boolean delResult=profile.deleteExistPersonalSkill(rid,pskill_id,pcate_id);
						System.out.println("===========delResult=="+delResult);
						if(delResult==false){
						categoryMst.setiVOCATEGORY_ID(Integer.parseInt(pcate_id));
						userSkillInfo.setiVOCATEGORY_ID(categoryMst);
						userData.setUserId(Integer.parseInt(rid));
						userSkillInfo.setUserId(userData);
						skillMst.setIvrBnSKILL_ID(Integer.parseInt(pskill_id));
						userSkillInfo.setIvrBnSKILL_ID(skillMst);
						userSkillInfo.setSkillSts(1);
						userSkillInfo.setEnrtyDatetime(date1);
						
						upresult=profile.updatePersonalSkill(userSkillInfo);
						}
					}
						System.out.println("==========upresult==========="+upresult);
					if(upresult==true){
						timestamp=Commonutility.timeStampRetStringVal();
						userSkillList=profile.getUserPrfileList(rid,timestamp,startlimit,getText("total.row"));
						System.out.println("=========userSkillList======="+userSkillList.size());
						JSONObject finalJsonarr=new JSONObject();
						locObjRspdataJson=new JSONObject();
						JSONArray jArray =new JSONArray();
						if( userSkillList!=null && userSkillList.size()>0){
							MvpUsrSkillTbl userSkillObj;
						for (Iterator<MvpUsrSkillTbl> it = userSkillList
								.iterator(); it.hasNext();) {
							userSkillObj = it.next();
							finalJsonarr = new JSONObject();
							if(userSkillObj.getiVOCATEGORY_ID()!=null){
							finalJsonarr.put("pcate_id", userSkillObj.getiVOCATEGORY_ID().getiVOCATEGORY_ID());
							finalJsonarr.put("pcate_name", userSkillObj.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
							}else{
								finalJsonarr.put("pcate_id", "");
								finalJsonarr.put("pcate_name", "");
							}
							if(userSkillObj.getUniqueId()!=null){
								finalJsonarr.put("pskill_uid", userSkillObj.getUniqueId());	
							}else{
								finalJsonarr.put("pskill_uid", "");
							}if(userSkillObj.getIvrBnSKILL_ID()!=null){
								finalJsonarr.put("pskill_id", userSkillObj.getIvrBnSKILL_ID().getIvrBnSKILL_ID());
								finalJsonarr.put("pskill_name", userSkillObj.getIvrBnSKILL_ID().getIvrBnSKILL_NAME());
							}else{
								finalJsonarr.put("pskill_id", "");
								finalJsonarr.put("pskill_name","");
							}
							
							
							jArray.put(finalJsonarr);
						}
						locObjRspdataJson.put("skilldetails", jArray);
						System.out.println("===========timestamp============="+timestamp);
						locObjRspdataJson.put("timestamp",timestamp);
						locVrSlQry="SELECT count(uniqueId) FROM MvpUsrSkillTbl where  userId.userId='"+Integer.parseInt(rid)+"' "
								+ " and enrtyDatetime <='"+timestamp+"'";
						count=profile.getInitTotal(locVrSlQry);
						System.out.println("====count==="+count);
						locObjRspdataJson.put("totalrecords",String.valueOf(count));
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						}else{
							locObjRspdataJson=new JSONObject();
							serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
						}
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
					}
					
					
					
				/*}else{
				if(skilldetails !=null && skilldetails.length() > 0)
				{
					System.out.println("============dsdsgds=="+skilldetails.length());
					JSONObject skillInfo=new JSONObject();
					for(int i =0 ;i<skilldetails.length();i++)
					{
						userSkillInfo=new MvpUsrSkillTbl();
						String pskill_id = (String)Commonutility.toHasChkJsonRtnValObj(skilldetails.getJSONObject(i),"pskill_id");
						String pcate_id = (String)Commonutility.toHasChkJsonRtnValObj(skilldetails.getJSONObject(i),"pcate_id");
						System.out.println("===pskill_id==="+pskill_id);
						System.out.println("===pcate_id==="+pcate_id);
						categoryMst.setiVOCATEGORY_ID(Integer.parseInt(pcate_id));
						userSkillInfo.setiVOCATEGORY_ID(categoryMst);
						userData.setUserId(Integer.parseInt(rid));
						userSkillInfo.setUserId(userData);
						skillMst.setIvrBnSKILL_ID(Integer.parseInt(pskill_id));
						userSkillInfo.setIvrBnSKILL_ID(skillMst);
						userSkillInfo.setSkillSts(1);
						userSkillInfo.setEnrtyDatetime(date1);
						
						boolean upresult=profile.updatePersonalSkill(userSkillInfo);
						if(upresult==true){
							timestamp=Commonutility.timeStampRetStringVal();
							userSkillList=profile.getUserPrfileList(rid,timestamp,startlimit,getText("total.row"));
							System.out.println("=========userSkillList======="+userSkillList.size());
							JSONObject finalJsonarr=new JSONObject();
							locObjRspdataJson=new JSONObject();
							JSONArray jArray =new JSONArray();
							if( userSkillList!=null && userSkillList.size()>0){
								MvpUsrSkillTbl userSkillObj;
							for (Iterator<MvpUsrSkillTbl> it = userSkillList
									.iterator(); it.hasNext();) {
								userSkillObj = it.next();
								finalJsonarr = new JSONObject();
								if(userSkillObj.getiVOCATEGORY_ID()!=null){
								finalJsonarr.put("pcate_id", userSkillObj.getiVOCATEGORY_ID().getiVOCATEGORY_ID());
								finalJsonarr.put("pcate_name", userSkillObj.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
								}else{
									finalJsonarr.put("pcate_id", "");
									finalJsonarr.put("pcate_name", "");
								}
								if(userSkillObj.getUniqueId()!=null){
									finalJsonarr.put("pskill_uid", userSkillObj.getUniqueId());	
								}else{
									finalJsonarr.put("pskill_uid", "");
								}if(userSkillObj.getIvrBnSKILL_ID()!=null){
									finalJsonarr.put("pskill_id", userSkillObj.getIvrBnSKILL_ID().getIvrBnSKILL_ID());
									finalJsonarr.put("pskill_name", userSkillObj.getIvrBnSKILL_ID().getIvrBnSKILL_NAME());
								}else{
									finalJsonarr.put("pskill_id", "");
									finalJsonarr.put("pskill_name","");
								}
								
								
								jArray.put(finalJsonarr);
							}
							locObjRspdataJson.put("skilldetails", jArray);
							locObjRspdataJson.put("timestamp",timestamp);
							locVrSlQry="SELECT count(uniqueId) FROM MvpUsrSkillTbl where  userId.userId='"+Integer.parseInt(rid)+"' "
									+ " and enrtyDatetime <='"+timestamp+"'";
							count=profile.getInitTotal(locVrSlQry);
							System.out.println("====count==="+count);
							locObjRspdataJson.put("totalrecords",String.valueOf(count));
							serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
							}else{
								locObjRspdataJson=new JSONObject();
								serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
							}
						}else{
							locObjRspdataJson=new JSONObject();
							serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
						}
					}
				}	
				
				}*/
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
			log.logMessage("Service code : 0003, Request values are not correct format", "info", PersonalProfileUpdate.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======PersonalProfileUpdate====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, PersonalProfileUpdate an unhandled error occurred", "info", PersonalProfileUpdate.class);
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
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			System.out.println("=====as==="+as);
			as=EncDecrypt.encrypt(as);
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
