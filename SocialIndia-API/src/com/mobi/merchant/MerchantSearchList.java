package com.mobi.merchant;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
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

public class MerchantSearchList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	MerchantDao merchanthbm=new MerchantDaoServices();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
	public String execute(){
		
		System.out.println("************mobile Search CarPool List services******************");
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
				String category = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "category");
				String keyForSearch = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "key_for_search");
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				System.out.println("startlimit----------"+startlimit);
				
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
				
				if (Commonutility.checkempty(locTimeStamp)) {
				} else {
					locTimeStamp=Commonutility.timeStampRetStringVal();
				}
				
				
				if(flg){
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int count=0;String locVrSlQry="";
			
				int societyId=userMst.getSocietyId().getSocietyId();
				String searchField="";
				if(category!=null && category.length()>0){
					searchField+=" and  MRCH.MRCH_CATEGORY_ID="+category;
				}
				if(keyForSearch!=null && keyForSearch.length()>0){
					searchField+=" and  (MRCH.KEY_FOR_SEARCH like ('%"+keyForSearch+"%') or  MRCH.SHOP_NAME like ('%"+keyForSearch+"%'))";
				}
				
				/*String totcountqry="SELECT COUNT(DISTINCT MRCH.MRCHNT_ID)"
						+ " FROM MVP_MRCH_TBL MRCH LEFT JOIN MVP_MRCH_UTILITY_TBL MRCH_UTIL ON MRCH.MRCHNT_ID=MRCH_UTIL.MRCHNT_ID "
						+ "LEFT JOIN  MVP_MRCH_UTILITY_IMAGE_TBL IMAGE_TBL ON MRCH_UTIL.UNIQUE_ID=IMAGE_TBL.UNIQUE_ID "
						+ "WHERE MRCH.MRCHNT_ACT_STS=1 AND MRCH_UTIL.ACT_STS=1 AND IMAGE_TBL.STATUS=1 "
						+ " and MRCH.ENTRY_DATETIME <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')"+searchField +" order by MRCH.MRCHNT_ID asc";
				*/
								
				String totcountqry=" SELECT count(*) from "
						+ " (SELECT MRCH.MRCHNT_ID,MRCH.SHOP_NAME,MRCH.MERCH_DESCRIPTION,MRCH.STORE_IMAGE,"
						+ "group_concat(concat(ifnull(MRCH_UTIL.UNIQUE_ID,''),'/',ifnull(IMAGE_TBL.DOC_DATE_FLDR,''),'/',ifnull(IMAGE_TBL.IMAGE_NAME,''))) as OFFER_IMAGES,"
						+ "MRCH.STORE_LOCATION,MRCH.RATING,mrch.MRCHNT_PH_NO"
						+ " FROM MVP_MRCH_TBL MRCH LEFT JOIN MVP_MRCH_UTILITY_TBL MRCH_UTIL ON MRCH.MRCHNT_ID=MRCH_UTIL.MRCHNT_ID "
						+ " LEFT JOIN  MVP_MRCH_UTILITY_IMAGE_TBL IMAGE_TBL ON MRCH_UTIL.UNIQUE_ID=IMAGE_TBL.UNIQUE_ID "
						+ " WHERE MRCH.MRCHNT_ACT_STS=1 AND MRCH_UTIL.ACT_STS=1 AND IMAGE_TBL.STATUS=1 "
						+ " and MRCH.ENTRY_DATETIME <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')"+searchField +" group by MRCH.MRCHNT_ID) as count";
				
				int totalcnt=commonHbm.getTotalCountSqlQuery(totcountqry);
				
				
				locVrSlQry=" SELECT MRCH.MRCHNT_ID,MRCH.SHOP_NAME,MRCH.MERCH_DESCRIPTION,MRCH.STORE_IMAGE,"
						+ "group_concat(concat(ifnull(MRCH_UTIL.UNIQUE_ID,''),'/',ifnull(IMAGE_TBL.DOC_DATE_FLDR,''),'/',ifnull(IMAGE_TBL.IMAGE_NAME,''))) as OFFER_IMAGES,"
						+ "MRCH.STORE_LOCATION,MRCH.RATING,mrch.MRCHNT_PH_NO"
						+ " FROM MVP_MRCH_TBL MRCH LEFT JOIN MVP_MRCH_UTILITY_TBL MRCH_UTIL ON MRCH.MRCHNT_ID=MRCH_UTIL.MRCHNT_ID "
						+ " LEFT JOIN  MVP_MRCH_UTILITY_IMAGE_TBL IMAGE_TBL ON MRCH_UTIL.UNIQUE_ID=IMAGE_TBL.UNIQUE_ID "
						+ " WHERE MRCH.MRCHNT_ACT_STS=1 AND MRCH_UTIL.ACT_STS=1 AND IMAGE_TBL.STATUS=1 "
						+ " and MRCH.ENTRY_DATETIME <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')"+searchField +" group by MRCH.MRCHNT_ID order by MRCH.MRCHNT_ID asc limit "+startlimit+","+getText("total.row")+"";

							
				/*locVrSlQry="SELECT MRCH.MRCHNT_ID,MRCH.SHOP_NAME,MRCH.MERCH_DESCRIPTION,MRCH.STORE_IMAGE,IMAGE_TBL.UTILITY_IMAGE_ID,IMAGE_TBL.IMAGE_NAME,MRCH_UTIL.UNIQUE_ID,IMAGE_TBL.DOC_DATE_FLDR,MRCH.STORE_LOCATION,MRCH.RATING,mrch.MRCHNT_PH_NO"
						+ " FROM MVP_MRCH_TBL MRCH LEFT JOIN MVP_MRCH_UTILITY_TBL MRCH_UTIL ON MRCH.MRCHNT_ID=MRCH_UTIL.MRCHNT_ID "
						+ "LEFT JOIN  MVP_MRCH_UTILITY_IMAGE_TBL IMAGE_TBL ON MRCH_UTIL.UNIQUE_ID=IMAGE_TBL.UNIQUE_ID "
						+ "WHERE MRCH.MRCHNT_ACT_STS=1 AND MRCH_UTIL.ACT_STS=1 AND IMAGE_TBL.STATUS=1 "
						+ " and MRCH.ENTRY_DATETIME <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')"+searchField +"  order by MRCH.MRCHNT_ID asc limit "+startlimit+","+getText("total.row")+"";*/
				System.out.println("locVrSlQry--------------"+locVrSlQry);
				
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				List<Object[]> contactListObj = new ArrayList<Object[]>();
				System.out.println("startlimit------------------"+startlimit);
				contactListObj=merchanthbm.getMobMerchantSearchList(locVrSlQry,startlimit);
				if(contactListObj!=null && contactListObj.size()>0){
				Object[] objList;
				int merid=0;
				JSONArray mercahntUtilarray=new JSONArray();
				JSONObject imageobj=new JSONObject();
				for(Iterator<Object[]> it=contactListObj.iterator();it.hasNext();) {
					objList = it.next();
					imageobj=new JSONObject();
					finalJsonarr=new JSONObject();
					Integer merchantId= (Integer)objList[0];
					System.out.println("merchantId-------"+merchantId);
					finalJsonarr.put("merchant_id", ""+merchantId);
					if((String)objList[1]!=null){
						finalJsonarr.put("merchant_name", (String)objList[1]);
						}else{
							finalJsonarr.put("merchant_name","");
						}
					
					if((String)objList[2]!=null){
						finalJsonarr.put("merchant_description", (String)objList[2]);
					}else{
						finalJsonarr.put("merchant_description", "");
					}
					
					String logPath="";
					if(objList[3]!=null){
						logPath=getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/"+getText("external.inner.marchantmstfldr")+getText("external.inner.mobilepath")+merchantId+"/"+objList[3];
					}
					System.out.println("logPath-----------"+logPath);
					finalJsonarr.put("merchant_logo", logPath);
					
					mercahntUtilarray=new JSONArray();
					String offerImages=(String)objList[4];
					if(offerImages!=null){
						String[] offerImgArray=offerImages.split(",",-1);
						for (int i=0;i<offerImgArray.length;i++){
							String offerImg = getText("SOCIAL_INDIA_BASE_URL")  +"/externalPath/offers/mobile/"+offerImgArray[i];
							imageobj=new JSONObject();
							imageobj.put("img_id", "");
							imageobj.put("img_url",offerImg );
							System.out.println("offerImg--------------"+offerImg);
							mercahntUtilarray.put(imageobj);
						}
					}
					
					finalJsonarr.put("images", mercahntUtilarray);
					
					if((String)objList[5]!=null){
						finalJsonarr.put("address", (String)objList[5]);
						}else{
							finalJsonarr.put("address","");
						}
					finalJsonarr.put("rating", ""+(Integer)objList[6]);
					if((String)objList[7]!=null){
						finalJsonarr.put("merchant_contact", (String)objList[7]);
						}else{
							finalJsonarr.put("merchant_contact","");
						}
					finalJsonarr.put("is_fav", "1");
				
					jArray.put(finalJsonarr);
				}
				
				locObjRspdataJson.put("mearchant_detail", jArray);
				jArray=null;
				locObjRspdataJson.put("totalrecords",totalcnt);
				locObjRspdataJson.put("timestamp",locTimeStamp);
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
			System.out.println("=======MerchantSearchList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
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