package com.mobile.wishlist;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.otpVo.otpValidateUtillity;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.social.utils.Log;

public class myWishListDelete extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	wishDao wishlist=new wishDaoServices();
	public String execute(){
		
		System.out.println("************myWishList Delete services******************");
		JSONObject json = new JSONObject();
		 otpValidateUtillity otpUtill=new otpValidateUtillity();
		Integer otpcount=0;
		String servicecode="";
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
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
		String wishlist_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "wishlist_id");
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
			if(result==true){
				boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
				if(societyKeyCheck==true){
				boolean checkExist=wishlist.checkExistWish(rid,wishlist_id);
					if(checkExist==true){
				boolean delResult=wishlist.deleteWishListData(rid,wishlist_id);
				
				if(delResult==true){
					
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"00","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"00","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+", Request values are not correct format", "info", myWishListDelete.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======myWishListDelete====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, myWishListDelete an unhandled error occurred", "info", myWishListDelete.class);
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
			System.out.println("===as=="+as);
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
