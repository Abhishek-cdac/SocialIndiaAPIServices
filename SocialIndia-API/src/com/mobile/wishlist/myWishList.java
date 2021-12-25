package com.mobile.wishlist;

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
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;

public class myWishList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	wishDao wishlist=new wishDaoServices();
	profileDao profile=new profileDaoServices();
	List<MvpWishlistTbl> wishList=new ArrayList<MvpWishlistTbl>();
	UserMasterTblVo userMst=new UserMasterTblVo();
	
	public String execute(){
		
		System.out.println("***********my wish list services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		String servicecode="";
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
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
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
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
			System.out.println("********result*****************"+result);
			int count=0;String locVrSlQry="";
			if(result==true){
				/*userMst=otp.checkSocietyKeyForList(societykey,rid);*/
				//if(userMst!=null){
				locVrSlQry="SELECT count(wishlistId) FROM MvpWishlistTbl where actFlg='"+1+"' and userId.userId='"+Integer.parseInt(rid)+"'"
						+ " and enrtyDatetime <='"+timestamp+"'";
				count=profile.getInitTotal(locVrSlQry);
				System.out.println("=======count======="+count);
				wishList=wishlist.getWishList(rid,timestamp,startlimit,getText("total.row"));
				System.out.println("=====wishList==="+wishList.size());
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				if( wishList!=null && wishList.size()>0){
					MvpWishlistTbl wishObj;
				for (Iterator<MvpWishlistTbl> it = wishList
						.iterator(); it.hasNext();) {
					wishObj = it.next();
					finalJsonarr = new JSONObject();
					String societyImage="";
					if(wishObj.getWishlistId()!=null){
					finalJsonarr.put("wishlist_id", wishObj.getWishlistId());//need to doo
					}else{
						finalJsonarr.put("wishlist_id", "");
					}
					if(wishObj.getiVOCATEGORY_ID()!=null){
						finalJsonarr.put("category_id", wishObj.getiVOCATEGORY_ID().getiVOCATEGORY_ID());	
					}else{
						finalJsonarr.put("category_id", "");
					}
					if(wishObj.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()!=null){
						finalJsonarr.put("category_name", wishObj.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
					}else{
						finalJsonarr.put("category_name", "");
					}
					
					
					if(wishObj.getiVOCATEGORY_ID().getCategoryDescription()!=null){
						finalJsonarr.put("category_desc", wishObj.getiVOCATEGORY_ID().getCategoryDescription());
					}else{
						finalJsonarr.put("category_desc", "");
					}
					if(wishObj.getiVOCATEGORY_ID().getCategoryImageName()!=null){
						finalJsonarr.put("category_img_url",  getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/category/mobile/"+wishObj.getiVOCATEGORY_ID().getCategoryImageName());
					}else{
						finalJsonarr.put("category_img_url", "");
					}
					
					
					jArray.put(finalJsonarr);
				}
				locObjRspdataJson.put("wishlist", jArray);
				locObjRspdataJson.put("timestamp",timestamp);
				locObjRspdataJson.put("totalrecords",String.valueOf(count));
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}
				/*}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}	*/
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);		
			}
		
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : 0003, Request values are not correct format", "info", myWishList.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======myWishList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, myWishList an unhandled error occurred", "info", myWishList.class);
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
