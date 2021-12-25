package com.pack.utilitypkg;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;



public class WebandMobServiceStart extends ActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Log log = new Log();
	private String ivrparams;
	otpDao otp = new otpDaoService();
	InfoLibraryDao infoLibrary = new InfoLibraryDaoServices();
	FacilityDao facilityhbm = new FacilityDaoServices();
	CommonUtils commjvm = new CommonUtils();
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="",is_mobile=null,lvrDonationfilename=null,lvrDonationfiledata=null, visitor_imgname=null;

		try{
			Commonutility.toWriteConsole("Step 1 : Donation create started");
			CommonUtilsDao common=new CommonUtilsDao();
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			/*fileUploadFileName="Chrysanthemum.jpg";
			fileUpload=new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg");*/
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			Common locCommonObj = null;
			if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				locCommonObj=new CommonDao();
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");

				is_mobile=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");//1-mob,0-web
				if (is_mobile!=null && !is_mobile.equalsIgnoreCase("0")){
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
				} else {
					flg = true;
				}
				if (flg) {
					boolean result = false;
					if (is_mobile !=null && !is_mobile.equalsIgnoreCase("0")) {
						result=otp.checkTownshipKey(rid,townshipKey);
					} else {
						result = true;
					}
					if (result == true) {
						System.out.println("********result*****************"+result);
						UserMasterTblVo userMst=new UserMasterTblVo();
						if(is_mobile !=null && !is_mobile.equalsIgnoreCase("0")){
						userMst=otp.checkSocietyKeyForList(societykey,rid);
						} else {
							userMst=otp.checkUserDetails(rid);
						}
						if (userMst != null) {
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",getText("R0029"),locObjRspdataJson,is_mobile);
			}

			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",getText("R0015"),locObjRspdataJson,is_mobile);
			}


			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson,is_mobile);
			}
		}
			else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", WebandMobServiceStart.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson,is_mobile);
		}
		}
		catch(Exception ex){
			System.out.println("=======Create Event====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", WebandMobServiceStart.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson,is_mobile);
		}
		finally
		{
			lvrDonationfilename=null;
		}
		return SUCCESS;
	}


	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla )
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();
		try {
			if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
				out = response.getWriter();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				mobiCommon mobicomn=new mobiCommon();
				String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
				out.print(as);
				out.close();
			}
			else{
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
			out.print(as);
			out.close();
			}
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