package com.mobi.notification;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.merchant.MerchantDao;
import com.mobi.merchant.MerchantDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.NotificationTblVO;

public class MarkNotificationRead   extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	MerchantDao merchanthbm=new MerchantDaoServices();
	List<NotificationTblVO> notificationList=new ArrayList<NotificationTblVO>();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
	
	public String execute(){
		
		System.out.println("************mobile Mark NotificationRead services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		Date timeStamp = null;
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
				String notificationId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "notification_id");
				String readStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "read_status");
				System.out.println("rid----------------"+rid);
				System.out.println("townshipKey-------------------"+townshipKey);
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
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				NotificationDao notifiHbm=new NotificationDaoServices();
				
				String updqry="update NotificationTblVO set readStatus="+readStatus+" where userId.userId='"+userMst.getUserId()+"'";
				
				if(notificationId!=null && notificationId.trim().length()>0){
					updqry="update NotificationTblVO set readStatus="+readStatus+" where notificationId="+notificationId;
				}
				
				boolean isUpdate=commonHbm.updateTableByQuery(updqry);
				if(isUpdate){
					locObjRspdataJson=new JSONObject();
					CommonMobiDao commonServ = new CommonMobiDaoService();
					String locVrSlQry = "SELECT count(notificationId) FROM NotificationTblVO where statusFlag='1' and readStatus=1 and userId.userId='"+userMst.getUserId()+"'";
					int count = commonServ.getTotalCountQuery(locVrSlQry);
					locObjRspdataJson.put("notification_count", Commonutility.intToString(count));
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
			log.logMessage("Service code : ivrservicecode, Sorry, MarkNotificationRead an unhandled error occurred", "info", familyMemberList.class);
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