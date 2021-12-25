package com.mobile.otp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.MvpSystemParameterTbl;
import com.mobile.otpVo.MvpUserOtpTbl;
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
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class mobValidateOtp extends ActionSupport{
	
	private String ivrparams;
	Log log=new Log();	
	private MvpSystemParameterTbl systemParams=new MvpSystemParameterTbl();
	TownshipMstTbl townshipData = new TownshipMstTbl();
	UserMasterTblVo userInfo=new UserMasterTblVo();
	MvpUserOtpTbl userOtpInfo = new MvpUserOtpTbl();
	MvpFamilymbrTbl userfamily=new MvpFamilymbrTbl();
	List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
	otpDao otp=new otpDaoService();
	profileDao profile=new profileDaoServices();
	signUpDao signup=new signUpDaoServices();
	 otpValidateUtillity otpUtill=new otpValidateUtillity();
	CommonUtils comutil=new CommonUtilsServices();
		CommonUtilsDao common=new CommonUtilsDao();		
	public String execute(){
		
		System.out.println("************mobValidate Otp services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		Query locQryrst=null;
		String locSlqry=null;
		CommonUtils comutil=new CommonUtilsServices();
		StringBuilder locErrorvalStrBuil =null;
		GroupMasterTblVo locGrpMstrVOobj=null,locGRPMstrvo=null;
		boolean flg = true;
		 Session pSession = HibernateUtil.getSession();
		 Session pSession1 = HibernateUtil.getSession();
		try{
			pSession = HibernateUtil.getSession();
			pSession1 = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				System.out.println("====locObjRecvJson==="+locObjRecvJson);
		String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
		//String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
		locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		String mobileno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileno");
		String otpcheck = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "otp");
		String otpfor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "otpfor");
		String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
		
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
			if (Commonutility.checkempty(mobileno)) {
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("mobile.error")));
			}
		}
		
		if(flg){
		
		System.out.println("=====townshipKey=="+townshipKey);
		boolean result=otp.checkTownshipKey(rid,townshipKey);
		System.out.println("********result*****************"+result);
		System.out.println("========********result*****************========"+otpfor);
		if(result==true){
			int committeeGrpcode=0;
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
			System.out.println("committeeGrpcode="+committeeGrpcode);
			/*boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
			if(societyKeyCheck==true){*/
				String Otp_exceed_time = "Otp_exceed_time_limit";
				String otpExceedsTime=otp.getSysParamsInfo(Otp_exceed_time);
				System.out.println("=otpExceedsTime===="+otpExceedsTime);
				
				
			
			if(otpfor!=null && otpfor.equalsIgnoreCase("1")){
				userOtpInfo=otp.getOtpValidateInfo(mobileno, rid,otpfor,otpcheck);
				if(userOtpInfo!=null){
				System.out.println("=otpExceedsTime===="+userOtpInfo.getOtpGeneratedTime());
				long exceedMins=mobiCommon.getDateToMins(userOtpInfo.getOtpGeneratedTime());
				System.out.println("======exceedMins======"+exceedMins);
				
				long exceedTimeDb=Long.parseLong(otpExceedsTime);
			if(exceedTimeDb>exceedMins){
				
			/*userInfo=otp.checkUserMobileInfo(mobileno, rid,otpcheck,otpfor);
			if(userInfo!=null){*/
			userOtpInfo=otp.checkUserOtpValidate(mobileno, "",otpcheck,otpfor);
		if(userOtpInfo!=null){
			
			//userInfoList=otp.getUserDeatils(mobileno, rid,otpcheck,otpfor);
			userInfoList=otp.getUserDeatils(mobileno, rid,committeeGrpcode,residenteGrpcode);
			System.out.println("========userInfoList===size===="+userInfoList.size());
			JSONObject finalJsonarr=new JSONObject();
			 JSONObject finalJson = new JSONObject();
			 JSONArray jArray =new JSONArray();
				if( userInfoList!=null && userInfoList.size()>0){
					UserMasterTblVo userObj;
				for (Iterator<UserMasterTblVo> it = userInfoList
						.iterator(); it.hasNext();) {
					userObj = it.next();
					finalJsonarr = new JSONObject();
					String societyImage="";
					if(userObj.getSocietyId().getLogoImage()!=null){
					finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
					}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
						finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
					
					}else{
						finalJsonarr.put("societylogo", "");//need to doo	
					}
					System.out.println("=======as=====ds================");
					finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
					finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
					if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
						finalJsonarr.put("societydesc", userObj.getSocietyId().getImprintName());
						}else{
							finalJsonarr.put("societydesc", "");	
						}
					if( userObj.getSocietyId().getNoOfMemebers()>0){
					finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
					}else{
						finalJsonarr.put("totalusers","");	
					}
					System.out.println("============ds================");
					if( userObj.getUserId()>0){
						finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
						}else{
							finalJsonarr.put("rid","");	
						}
					jArray.put(finalJsonarr);
				}
				finalJson.put("societies", jArray);
				}else{
					finalJsonarr.put("societylogo","" );
					finalJsonarr.put("societyname","" );
					finalJsonarr.put("societykey", "");
					finalJsonarr.put("societydesc", "");
					finalJsonarr.put("totalusers", "");
					finalJsonarr.put("rid", "");
					jArray.put(finalJsonarr);
					finalJson.put("societies", jArray);
					finalJson.put("otpfor", "1");
				}
			
				serverResponse("0004","00","R0001",mobiCommon.getMsg("R0001"),finalJson);
				
		}else{
			locObjRspdataJson=new JSONObject();
			serverResponse("0004","01","R0021",mobiCommon.getMsg("R0021"),locObjRspdataJson);
			
		}
		
		/*}else{
			locObjRspdataJson=new JSONObject();
			serverResponse("0004","01","R0018",getText("R0018"),locObjRspdataJson);
			
		}*/
			
			}else{       //otp expires
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",mobiCommon.getMsg("R0029"),locObjRspdataJson);
			}
			
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0021",mobiCommon.getMsg("R0021"),locObjRspdataJson);
				}
			
			
			}else if(otpfor!=null && otpfor.equalsIgnoreCase("2")){
				userOtpInfo=otp.getOtpValidateInfo(mobileno, rid,otpfor,otpcheck);
				if(userOtpInfo!=null){
					
				System.out.println("=otpExceedsTime===="+userOtpInfo.getOtpGeneratedTime());
				long exceedMins=mobiCommon.getDateToMins(userOtpInfo.getOtpGeneratedTime());
				System.out.println("======exceedMinsfddddddddddddddddddddddddd======");
				System.out.println("======exceedMins======"+exceedMins);
				long exceedTimeDb=Long.parseLong(otpExceedsTime);
			if(exceedTimeDb>exceedMins){
				
				/*userInfo=otp.checkUserMobileInfo(mobileno, rid,otpcheck,otpfor);
				if(userInfo!=null){*/
				userOtpInfo=otp.checkUserOtpValidate(mobileno, "",otpcheck,otpfor);
			if(userOtpInfo!=null){
				userInfoList=otp.getUserDeatils(mobileno, rid,committeeGrpcode,residenteGrpcode);
				System.out.println("========userInfoList===size===="+userInfoList.size());
				JSONObject finalJsonarr=new JSONObject();
				 JSONObject finalJson = new JSONObject();
				 JSONArray jArray =new JSONArray();
					if( userInfoList!=null && userInfoList.size()>0){
						UserMasterTblVo userObj;
					for (Iterator<UserMasterTblVo> it = userInfoList
							.iterator(); it.hasNext();) {
						userObj = it.next();
						finalJsonarr = new JSONObject();
						String societyImage="";
						if(userObj.getSocietyId().getLogoImage()!=null){
						finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
						}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
							finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
						
						}else{
							finalJsonarr.put("societylogo", "");//need to doo	
						}
						finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
						finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
						if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
							finalJsonarr.put("societydesc", userObj.getSocietyId().getImprintName());
							}else{
								finalJsonarr.put("societydesc", "");	
							}
						if( userObj.getSocietyId().getNoOfMemebers()>0){
							finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
							}else{
								finalJsonarr.put("totalusers","");	
							}
							if( userObj.getUserId()>0){
								finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
								}else{
									finalJsonarr.put("rid","");	
								}
						jArray.put(finalJsonarr);
					}
					finalJson.put("societies", jArray);
					}else{
						finalJsonarr.put("societylogo","" );
						finalJsonarr.put("societyname","" );
						finalJsonarr.put("societykey", "");
						finalJsonarr.put("societydesc", "");
						finalJsonarr.put("totalusers", "");
						finalJsonarr.put("rid", "");
						jArray.put(finalJsonarr);
						finalJson.put("societies", jArray);
						finalJson.put("otpfor", "2");
					}
				serverResponse("0004","00","R0001",mobiCommon.getMsg("R0001"),finalJson);
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse("0004","01","R0021",mobiCommon.getMsg("R0021"),locObjRspdataJson);
			}
			/*}else{
				locObjRspdataJson=new JSONObject();
				serverResponse("0004","01","R0018",getText("R0018"),locObjRspdataJson);
			}*/
			}else{       //otp expires
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",mobiCommon.getMsg("R0029"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse("0004","01","R0021",mobiCommon.getMsg("R0021"),locObjRspdataJson);
			}
				
			}else if(otpfor!=null && otpfor.equalsIgnoreCase("3")){
				userOtpInfo=otp.getOtpValidateInfo(mobileno, rid,otpfor,otpcheck);
				if(userOtpInfo!=null){
				System.out.println("=otpExceedsTime===="+userOtpInfo.getOtpGeneratedTime());
				long exceedMins=mobiCommon.getDateToMins(userOtpInfo.getOtpGeneratedTime());
				System.out.println("======exceedMins======"+exceedMins);
				
				long exceedTimeDb=Long.parseLong(otpExceedsTime);
			if(exceedTimeDb>exceedMins){
				
			/*userInfo=otp.checkUserMobileInfo(mobileno, rid,otpcheck,otpfor);
			if(userInfo!=null && userInfo.getParentId()!=null){*/
			userOtpInfo=otp.checkUserOtpValidate(mobileno, "",otpcheck,otpfor);
		if(userOtpInfo!=null){
			
			//userInfoList=otp.getUserDeatils(mobileno, rid,otpcheck,otpfor);
			userInfoList=otp.getUserDeatils(mobileno, rid,committeeGrpcode,residenteGrpcode);
			System.out.println("========userInfoList===size===="+userInfoList.size());
			JSONObject finalJsonarr=new JSONObject();
			 JSONObject finalJson = new JSONObject();
			 JSONArray jArray =new JSONArray();
				if( userInfoList!=null && userInfoList.size()>0){
					UserMasterTblVo userObj;
				for (Iterator<UserMasterTblVo> it = userInfoList
						.iterator(); it.hasNext();) {
					userObj = it.next();
					finalJsonarr = new JSONObject();
					String societyImage="";
					if(userObj.getSocietyId().getLogoImage()!=null){
					finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/society/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getLogoImage());//need to doo
					}else if(userObj.getSocietyId()!=null && userObj.getSocietyId().getTownshipId()!=null && userObj.getSocietyId().getTownshipId().getTownshiplogoname()!=null && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("") && !userObj.getSocietyId().getTownshipId().getTownshiplogoname().equalsIgnoreCase("null")){
						finalJsonarr.put("societylogo", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/township/mobile/"+userObj.getUserId()+"/"+userObj.getSocietyId().getTownshipId().getTownshiplogoname());//need to doo
					
					}else{
						finalJsonarr.put("societylogo", "");//need to doo	
					}
					finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
					finalJsonarr.put("societykey", userObj.getSocietyId().getActivationKey());
					if(userObj.getSocietyId().getImprintName()!=null && userObj.getSocietyId().getImprintName()!=""&& !userObj.getSocietyId().getImprintName().equalsIgnoreCase("")){
						finalJsonarr.put("societydesc", userObj.getSocietyId().getImprintName());
						}else{
							finalJsonarr.put("societydesc", "");	
						}
					if( userObj.getSocietyId().getNoOfMemebers()>0){
						finalJsonarr.put("totalusers",String.valueOf( userObj.getSocietyId().getNoOfMemebers()));
						}else{
							finalJsonarr.put("totalusers","");	
						}
						if( userObj.getUserId()>0){
							finalJsonarr.put("rid",String.valueOf( userObj.getUserId()));
							}else{
								finalJsonarr.put("rid","");	
							}
					jArray.put(finalJsonarr);
				}
				finalJson.put("societies", jArray);
				}else{
					finalJsonarr.put("societylogo","" );
					finalJsonarr.put("societyname","" );
					finalJsonarr.put("societykey", "");
					finalJsonarr.put("societydesc", "");
					finalJsonarr.put("totalusers", "");
					finalJsonarr.put("rid", "");
					jArray.put(finalJsonarr);
					finalJson.put("societies", jArray);
					finalJson.put("otpfor", "3");
				}
			
				serverResponse("0004","00","R0001",mobiCommon.getMsg("R0001"),finalJson);
				
		}else{
			locObjRspdataJson=new JSONObject();
			serverResponse("0004","01","R0021",mobiCommon.getMsg("R0021"),locObjRspdataJson);
			
		}
		
		/*}else{
			locObjRspdataJson=new JSONObject();
			serverResponse("0004","01","R0018",getText("R0018"),locObjRspdataJson);
			
		}*/
			
			}else{       //otp expires
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",mobiCommon.getMsg("R0029"),locObjRspdataJson);
			}
			
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0021",mobiCommon.getMsg("R0021"),locObjRspdataJson);
				}
			}
		}else{
				locObjRspdataJson=new JSONObject();
				serverResponse("0004","01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
			}
		}else{
			locObjRspdataJson=new JSONObject();
			locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
			serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}
	}else{
		locObjRspdataJson=new JSONObject();
		log.logMessage("Service code : 0004, Request values are not correct format", "info", mobValidateOtp.class);
		serverResponse("0004","01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
	}
			
		}catch(Exception ex){
			System.out.println("=======mobOtp validate====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, mobOtp validate an unhandled error occurred", "info", mobValidateOtp.class);
			locObjRspdataJson=new JSONObject();
			serverResponse("0004","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		} finally{
			if(pSession != null){
				pSession.flush();pSession.clear();pSession.close();pSession=null;
			}
			if(pSession1 != null){
				pSession1.flush();pSession1.clear();pSession1.close();pSession1=null;
			}
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

	public TownshipMstTbl getTownshipData() {
		return townshipData;
	}

	public void setTownshipData(TownshipMstTbl townshipData) {
		this.townshipData = townshipData;
	}

	public UserMasterTblVo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserMasterTblVo userInfo) {
		this.userInfo = userInfo;
	}

}
