package com.mobi.networkUserList;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.networkUserListVO.MvpNetworkTbl;
import com.mobi.networkUserListVO.networkDao;
import com.mobi.networkUserListVO.networkDaoServices;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;

public class networkInvite extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	networkDao network=new networkDaoServices();
	profileDao profile=new profileDaoServices();
	List<MvpNetworkTbl> networkMstList=new ArrayList<MvpNetworkTbl>();
	UserMasterTblVo userMst=new UserMasterTblVo();
	UserMasterTblVo userInfo=new UserMasterTblVo();
	
	public String execute(){
		
		System.out.println("*********network Invite* services**************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		MvpNetworkTbl networkMst=new MvpNetworkTbl();
		String servicecode="";
		MvpNetworkTbl networInfo=new MvpNetworkTbl();
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("=========ivrparams======="+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String from_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "from_id");
				String to_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "to_id");
				String request_type = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "request_type");
				String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String networkId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "network_id");
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
			if(result==true){
				boolean checkFlag=true;
			System.out.println("********result*****************"+result);
			Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				
				networkMst=network.checkInviteExist(from_id,to_id,request_type);
				System.out.println("networkMst-------------"+networkMst);
				if(networkMst!=null){
						
					if(request_type.equalsIgnoreCase("3") && networkMst.getConnStsFlg()==2){
					boolean approveSts=network.checkApproveStatus(from_id,to_id,request_type);
					if(approveSts==true){
						//For Notification
						networInfo=network.getNetWorkDetailById(Integer.parseInt(networkId));
						System.out.println("accept request-------------"+networInfo.getNetworkId());
						NotificationDao notificationHbm=new NotificationDaoServices();
						AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
						String additionaldata=additionalDatafunc.formAdditionalDataForNetworkTbl(networInfo,userMst.getSocietyId().getSocietyId(),networInfo.getUsrParentUsrId());
						
						
						CommonMobiDao commonHbm=new CommonMobiDaoService();
						userInfo=commonHbm.getProfileDetails(Integer.parseInt(from_id));
						
						String desc="";
						if(userMst!=null && userMst.getFirstName()!=null && !userMst.getFirstName().equalsIgnoreCase("")){
							desc+=Commonutility.toCheckNullEmpty(userMst.getFirstName())+Commonutility.toCheckNullEmpty(userMst.getLastName());
							}
						desc+=" accepted your friend request";
						System.out.println("desc--------------------33333--------------"+desc);
						notificationHbm.insertnewNotificationDetails(userInfo, desc, 10, 1, networInfo.getNetworkId(), userMst, additionaldata);
						//end
						
						
						//For notification
						 additionalDatafunc=new AdditionalDataDaoServices();
						additionaldata=additionalDatafunc.formAdditionalDataForNetworkTbl(networInfo,userMst.getSocietyId().getSocietyId(),userMst);
						System.out.println("additionaldata---------------333-----"+additionaldata);
						System.out.println("networkId---------333----------"+networkId);
						notificationHbm.updateNotificationDetails(10, Integer.parseInt(networkId), additionaldata);
						/*END*/
						
						
						
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("invite_status", request_type);
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}
					}else if(request_type.equalsIgnoreCase("4") && networkMst.getConnStsFlg()==2){
						//For Notification
						NotificationDao notificationHbm=new NotificationDaoServices();
						notificationHbm.deleteNotificationDetails(10, Integer.parseInt(networkId));
						//End
						boolean deleteSts=network.deleteNetwork(from_id,to_id,request_type);
						if(deleteSts==true){
							
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("invite_status", "1");
							serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						}
						
					}else if(request_type.equalsIgnoreCase("5") && networkMst.getConnStsFlg()==3){
						//For Notification
						NotificationDao notificationHbm=new NotificationDaoServices();
						notificationHbm.deleteNotificationDetails(10, Integer.parseInt(networkId));
						//End
						
						boolean deleteSts=network.deleteNetwork(from_id,to_id,request_type);
						if(deleteSts==true){
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("invite_status", "1");
							serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						}
						
					}else if(request_type.equalsIgnoreCase("2")){
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("invite_status", "-1");//mobile page refresh
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						
					}else{
						checkFlag=false;
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
					}
					
				}else{
					boolean insertResult=false;
					if(Integer.parseInt(from_id)!=Integer.parseInt(to_id)){
						CommonMobiDao commonHbm=new CommonMobiDaoService();
						userInfo=commonHbm.getProfileDetails(Integer.parseInt(from_id));
						userMst=commonHbm.getProfileDetails(Integer.parseInt(to_id));
					networInfo.setUsrParentUsrId(userInfo);
					networInfo.setUsrConnectedUsrId(userMst);
					networInfo.setConnStsFlg(2);
					networInfo.setEntryDatetime(date1);
					insertResult=network.insertInvite(networInfo);
					}
					if(insertResult==true){
						System.out.println("networInfo-------------"+networInfo.getNetworkId());
						NotificationDao notificationHbm=new NotificationDaoServices();
						AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
						String additionaldata=additionalDatafunc.formAdditionalDataForNetworkTbl(networInfo,userMst.getSocietyId().getSocietyId(),userInfo);
						
						String desc="";
						if(userMst!=null && userMst.getFirstName()!=null && !userMst.getFirstName().equalsIgnoreCase("")){
							desc+=Commonutility.toCheckNullEmpty(userInfo.getFirstName())+Commonutility.toCheckNullEmpty(userInfo.getLastName());
							}
						desc+=" have sent you a friend request";
						notificationHbm.insertnewNotificationDetails(userMst, desc, 10, 1, networInfo.getNetworkId(), userInfo, additionaldata);
						
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("invite_status","2");
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", networkInvite.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======networkInvite====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, networkInvite an unhandled error occurred", "info", networkInvite.class);
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
