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

public class MearchantIssueList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	MerchantDao merchanthbm=new MerchantDaoServices();
	CommonUtils commjvm=new CommonUtils();
	
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
				//String tomerchentId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "merchant_id");
				String keyForSearch = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "key_for_search");
				/*String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");*/
				
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
				
				/*if (Commonutility.checkempty(locTimeStamp)) {
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timestamp.error")));
				}*/
				
				
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
				
				if(keyForSearch!=null && keyForSearch.length()>0){
					searchField+=" and  (MRCH.KEY_FOR_SEARCH like ('%"+keyForSearch+"%') or  MRCH.SHOP_NAME like ('%"+keyForSearch+"%'))";
				}
				
				/*if(tomerchentId!=null && tomerchentId.length()>0){
					searchField+=" and  MRCH.MRCHNT_ID =('"+tomerchentId+"')";
				}
				*/
				
				locVrSlQry="SELECT MRCH.MRCHNT_ID,MRCH.SHOP_NAME,MRCH.MERCH_DESCRIPTION,MRCH.STORE_IMAGE,MRCH_ISSUE.ISSUE_ID,MRCH_ISSUE.DESCRIPTION,MRCH.RATING,MRCH.STORE_LOCATION"
						+ " FROM MVP_MRCH_TBL MRCH LEFT JOIN mvp_merchant_issue_tbl MRCH_ISSUE ON MRCH.MRCHNT_ID=MRCH_ISSUE.MRCHNT_ID "
						+ "WHERE MRCH.MRCHNT_ACT_STS=1 AND MRCH_ISSUE.STATUS=1 "
						//+ "WHERE MRCH.MRCHNT_ACT_STS=1 AND MRCH_ISSUE.STATUS=1 and MRCH_ISSUE.GROUP_CODE=8 "
						+ " "+searchField+" order by  MRCH.MRCHNT_ID asc";
				
				System.out.println("locVrSlQry-----1111111111--------"+locVrSlQry);
				
				JSONObject finalJsonarr=new JSONObject();
				JSONObject issueDetail=new JSONObject();
				JSONArray issueDetailarray=new JSONArray();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				List<Object[]> contactListObj = new ArrayList<Object[]>();
				contactListObj=merchanthbm.getMobMerchantIssueSearchList(locVrSlQry);
				if(contactListObj!=null && contactListObj.size()>0){
				Object[] objList;
				int merchentId=0;
				int totalrec=0;
				for(Iterator<Object[]> it=contactListObj.iterator();it.hasNext();) {
					objList = it.next();
					issueDetail=new JSONObject();
					
					int merId=(Integer)objList[0];
					String merName=(String)objList[1];
					String merDesc=(String)objList[2];
					int rating=(Integer)objList[6];
					String storeLoction=(String)objList[7];
					if(merchentId>0 && merchentId!=merId){
						finalJsonarr.put("issue", issueDetailarray);
						jArray.put(finalJsonarr);
						finalJsonarr=null;
						issueDetailarray=null;
						finalJsonarr=new JSONObject();
						issueDetailarray=new JSONArray();
						
						}
					
					if(merchentId!=merId){
						merchentId=merId;
						totalrec++;
					finalJsonarr.put("mc_id",""+merId );
					if(merName!=null){
					finalJsonarr.put("mc_name",merName );
					}else{
						finalJsonarr.put("mc_name","" );
					}
					if(merDesc!=null){
					finalJsonarr.put("stitle", merDesc);
					}else{
						finalJsonarr.put("stitle","");
					}
					finalJsonarr.put("rating", ""+rating);
					if(storeLoction!=null){
					finalJsonarr.put("location", storeLoction);
					}else{
						finalJsonarr.put("location","");
					}
					String logPath="";
					if(objList[3]!=null){
					String merLogoName=(String)objList[3];
					logPath=getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/Merchant/mobile/"+merId+"/"+merLogoName;
					}
					finalJsonarr.put("mc_imgurl", logPath);
					}
					issueDetail.put("id", ""+(Integer)objList[4]);
					issueDetail.put("name", (String)objList[5]);
					issueDetailarray.put(issueDetail);
					System.out.println("issueDetailarray------------------"+issueDetailarray.toString());
					issueDetail=null;
				}
				finalJsonarr.put("issue", issueDetailarray);
				jArray.put(finalJsonarr);
				finalJsonarr=null;
				issueDetailarray=null;
				locObjRspdataJson.put("shops", jArray);
				jArray=null;
				//locObjRspdataJson.put("totalrecords",totalrec);
				//locObjRspdataJson.put("timestamp",locTimeStamp);
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