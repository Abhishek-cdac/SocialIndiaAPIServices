package com.mobi.networkUserList;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gdata.data.DateTime;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.networkUserListVO.MvpNetworkTbl;
import com.mobi.networkUserListVO.networkDao;
import com.mobi.networkUserListVO.networkDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
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

public class networkUserList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	networkDao network=new networkDaoServices();
	profileDao profile=new profileDaoServices();
	List<MvpNetworkTbl> networkMstList=new ArrayList<MvpNetworkTbl>();
	UserMasterTblVo userMst=new UserMasterTblVo();
	UserMasterTblVo userInfo=new UserMasterTblVo();
	FunctionUtility commonFn = new FunctionUtilityServices();
	
	public String execute(){
		
		System.out.println("*********network User List***************");
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
			System.out.println("=========ivrparams======="+ivrparams);
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
				String search_data = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "search_data");
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
					if (Commonutility.checkempty(timestamp)) {
					} else {
						timestamp=Commonutility.timeStampRetStringVal();
					}
					
					boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			//boolean societyKeyCheck=otp.checkSocietyKey(societykey,rid);
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			System.out.println("==userMst id ==="+userMst.getSocietyId().getSocietyId());
			if(userMst!=null){
			int count=0;String locVrSlQry="";
			String qry="";
			
			
				
				/*locVrSlQry="SELECT count(networkId) FROM MvpNetworkTbl where  usrParentUsrId.userId='"+Integer.parseInt(rid)+"'";
				count=profile.getInitTotal(locVrSlQry);*/
				String globalsearch="";
				if(search_data!=null && !search_data.equalsIgnoreCase("null") && !search_data.equalsIgnoreCase("")){
					globalsearch = " AND (" + "USR.FNAME like ('%" + search_data+ "%') or " 
                            + "USR.LNAME like ('%" + search_data + "%') or "
                            + "USR.MOBILE_NO like ('%" + search_data + "%'))";
				}
				String networkidSerch="";
				if(networkId!=null && networkId.length()>0){
					networkidSerch=" and NTWRK.NETWORK_ID="+networkId;
				}
				int totalcnt=0;
				
				qry="select USR_ID,FNAME,LNAME,IMAGE_NAME_MOBILE,SOCIAL_PICTURE,"
						+ "LAST_LOGIN,ONLINE_STATUS,CONN_STS_FLG,PARENT_USR_ID,"
						+ "CONNECTED_USR_ID,NETWORK_ID from (SELECT USR.USR_ID USR_ID,USR.FNAME FNAME,USR.LNAME LNAME,"
						+ "USR.IMAGE_NAME_MOBILE IMAGE_NAME_MOBILE,USR.SOCIAL_PICTURE SOCIAL_PICTURE,USR.LAST_LOGIN LAST_LOGIN,"
						+ "USR.ONLINE_STATUS ONLINE_STATUS,NTWRK.CONN_STS_FLG CONN_STS_FLG,NTWRK.PARENT_USR_ID,"
						+ "ifnull(NTWRK.CONNECTED_USR_ID,USR.USR_ID) CONNECTED_USR_ID,CASE WHEN NTWRK.PARENT_USR_ID IS NULL THEN 1 ELSE "
						+ "(select  ACT_STS  from USR_REG_TBL where USR_ID=NTWRK.PARENT_USR_ID ) END ACT_STS1, "
						+ "(select ACT_STS from USR_REG_TBL where USR_ID=ifnull(NTWRK.CONNECTED_USR_ID,USR.USR_ID) ) ACT_STS2,"
						+ "NTWRK.NETWORK_ID NETWORK_ID,(select count(*) "
						+ "from mvp_network_tbl ntwrk where (NTWRK.PARENT_USR_ID='"+userMst.getUserId()+"' and NTWRK.CONNECTED_USR_ID=USR.USR_ID) or "
						+ "(NTWRK.PARENT_USR_ID=USR.USR_ID and NTWRK.CONNECTED_USR_ID='"+userMst.getUserId()+"') ) flg FROM USR_REG_TBL USR  "
						+ "LEFT JOIN MVP_NETWORK_TBL NTWRK ON (USR.USR_ID=NTWRK.PARENT_USR_ID OR USR.USR_ID=NTWRK.CONNECTED_USR_ID) and USR.USR_ID='"+userMst.getUserId()+"'   "
						+ " WHERE   USR.ACT_STS=1 AND USR.GROUP_ID IN(5,6) AND USR.SOCIETY_ID='"+userMst.getSocietyId().getSocietyId()+"'  AND USR.ENTRY_DATETIME <=  '"+timestamp+"' "+globalsearch + networkidSerch+" ) tt where flg=0 AND ACT_STS1=1 AND ACT_STS2=1";
		
				String tCountqry="select count(*) from (SELECT USR.USR_ID USR_ID,USR.FNAME FNAME,USR.LNAME LNAME,"
						+ "USR.IMAGE_NAME_MOBILE IMAGE_NAME_MOBILE,USR.SOCIAL_PICTURE SOCIAL_PICTURE,USR.LAST_LOGIN LAST_LOGIN,"
						+ "USR.ONLINE_STATUS ONLINE_STATUS,NTWRK.CONN_STS_FLG CONN_STS_FLG,NTWRK.PARENT_USR_ID,"
						+ "ifnull(NTWRK.CONNECTED_USR_ID,USR.USR_ID) CONNECTED_USR_ID,CASE WHEN NTWRK.PARENT_USR_ID IS NULL THEN 1 ELSE "
						+ "(select  ACT_STS  from USR_REG_TBL where USR_ID=NTWRK.PARENT_USR_ID ) END ACT_STS1,"
						+ "(select ACT_STS from USR_REG_TBL where USR_ID=ifnull(NTWRK.CONNECTED_USR_ID,USR.USR_ID) ) ACT_STS2,"
						+ "NTWRK.NETWORK_ID NETWORK_ID,(select count(*) "
						+ "from mvp_network_tbl ntwrk where (NTWRK.PARENT_USR_ID='"+userMst.getUserId()+"' and NTWRK.CONNECTED_USR_ID=USR.USR_ID) or "
						+ "(NTWRK.PARENT_USR_ID=USR.USR_ID and NTWRK.CONNECTED_USR_ID='"+userMst.getUserId()+"') ) flg FROM USR_REG_TBL USR  "
						+ "LEFT JOIN MVP_NETWORK_TBL NTWRK ON (USR.USR_ID=NTWRK.PARENT_USR_ID OR USR.USR_ID=NTWRK.CONNECTED_USR_ID) and USR.USR_ID='"+userMst.getUserId()+"'   "
						+ " WHERE   USR.ACT_STS=1 AND USR.GROUP_ID IN(5,6) AND USR.SOCIETY_ID='"+userMst.getSocietyId().getSocietyId()+"'  AND USR.ENTRY_DATETIME <=  '"+timestamp+"' "+globalsearch + networkidSerch+" ) tt where flg=0 AND ACT_STS1=1 AND ACT_STS2=1";
				CommonMobiDao commonDao=new CommonMobiDaoService();
				totalcnt=commonDao.getTotalCountSqlQuery(tCountqry);
				
				List<Object[]> networkListObj = new ArrayList<Object[]>();
				System.out.println("===========qry==========="+qry);
				networkListObj=network.getnetworkListSearch(qry,startlimit,getText("total.row"));
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				
				if(networkListObj!=null && networkListObj.size()>0){
					Object[] objList;
					
					for(Iterator<Object[]> it=networkListObj.iterator();it.hasNext();) {
						objList = it.next();
						Integer userid= (Integer)objList[0];
						Integer connectedUser= (Integer)objList[9];
						Integer parentdUser= (Integer)objList[8];
						//UserMasterTblVo chConnObj = new UserMasterTblVo();
						//UserMasterTblVo chParentObj = new UserMasterTblVo();

						//chConnObj=otp.checkUserDetails(String.valueOf(objList[9]));
						//chParentObj=otp.checkUserDetails(String.valueOf(objList[8]));
						//if (chConnObj != null && chParentObj != null) {
						System.out.println("userid=========="+userid+"=========userMst.getUserId()======"+userMst.getUserId()+"====connectedUser===="+connectedUser+"============parentdUser+======="+parentdUser);
						if(userid!=null && connectedUser!=null && Integer.parseInt(userid.toString())==Integer.parseInt(connectedUser.toString()) && Integer.parseInt(userid.toString())==userMst.getUserId() && parentdUser==null){
							System.out.println("if-------------------------");
						}else{
						finalJsonarr=new JSONObject();
						
						System.out.println("userid-------"+userid);
						//finalJsonarr.put("nwuser_id", Commonutility.toCheckNullEmpty(String.valueOf(userid)));
						String username="";
						if((String)objList[1]!=null){
							username= (String)objList[1];
							}
						if((String)objList[2]!=null){
							if(username!=""){
								username +=" "+(String)objList[2];
							}else{
							username += (String)objList[2];
							}
						}
						
						//finalJsonarr.put("from_nwuser_name",Commonutility.toCheckNullEmpty(username));
						/*if((String)objList[3]!=null){
							finalJsonarr.put("from_nwuser_img_url", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userid+"/"+(String)objList[3]);
						}
						else if((String)objList[4]!=null){
							finalJsonarr.put("from_nwuser_img_url", Commonutility.toCheckNullEmpty((String)objList[4]));
						}else{
							finalJsonarr.put("from_nwuser_img_url","");		
						}*/
						
						finalJsonarr.put("is_online", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[6])));
						if((Integer)objList[7]!=null){
							finalJsonarr.put("invite_status", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[7])));	
						}else{
							finalJsonarr.put("invite_status", "1");		
						}
						
						
						
						
						if((Integer)objList[8]!=null){
							finalJsonarr.put("from_id", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[8])));
							System.out.println("=f=dgf=dg===================");
							userInfo=otp.checkUserDetails(String.valueOf(objList[8]));
							String fromusername="";
							System.out.println("userInfo------------"+userInfo.getUserId());
							System.out.println("==entrytime=="+userInfo.getLoginDatetime());
							if(userInfo.getLoginDatetime()!=null){
								String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
								finalJsonarr.put("last_accessed_time", finalDate);	
							}else{
								finalJsonarr.put("last_accessed_time", "");	
							}
							
							if(userInfo.getFirstName()!=null){
								fromusername= userInfo.getFirstName();
								}
							
							if(userInfo.getLastName()!=null){
								if(fromusername!=""){
									fromusername +=" "+userInfo.getLastName();
								}
							}
							if(fromusername!=null && fromusername.equalsIgnoreCase("")){
								fromusername=getText("anonymous.user.name");
							}
							finalJsonarr.put("from_nwuser_name",Commonutility.toCheckNullEmpty(fromusername));
							if(userInfo.getImageNameMobile()!=null){
								finalJsonarr.put("from_nwuser_img_url", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+objList[8]+"/"+userInfo.getImageNameMobile());
								}else if(userInfo.getSocialProfileUrl()!=null){
									finalJsonarr.put("from_nwuser_img_url", userInfo.getSocialProfileUrl());	
								}
								else{
									finalJsonarr.put("from_nwuser_img_url", "");	
								}
						if(userInfo.getSocietyId()!=null && userInfo.getSocietyId().getSocietyName()!=null && userInfo.getSocietyId().getSocietyName().length()>0){
							finalJsonarr.put("society_name", userInfo.getSocietyId().getSocietyName());
						}else{
							finalJsonarr.put("society_name", "");
						}
							
							
						}else{
							System.out.println("=df=dsf=s=="+userid);
							finalJsonarr.put("to_id", Commonutility.toCheckNullEmpty(String.valueOf(userid)));
							userInfo=otp.checkUserDetails(String.valueOf(userid));
							if(userInfo!=null){
							String tousername="";
							System.out.println("userInfo---- else--------"+userInfo.getUserId());
							System.out.println("==entrytime=="+userInfo.getLoginDatetime());
							if(userInfo.getLoginDatetime()!=null){
								String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
								finalJsonarr.put("last_accessed_time", finalDate);	
							}else{
								finalJsonarr.put("last_accessed_time", "");	
							}
							
							if(userInfo.getFirstName()!=null){
								tousername= userInfo.getFirstName();
								}
							if(userInfo.getLastName()!=null){
								if(tousername!=""){
									tousername +=" "+userInfo.getLastName();
								}
							}
							if(tousername!=null && tousername.equalsIgnoreCase("")){
								tousername=getText("anonymous.user.name");
							}
							finalJsonarr.put("to_nwuser_name",Commonutility.toCheckNullEmpty(tousername));
							if(userInfo.getImageNameMobile()!=null){
								finalJsonarr.put("to_nwuser_img_url", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userid+"/"+userInfo.getImageNameMobile());
								}else if(userInfo.getSocialProfileUrl()!=null){
									finalJsonarr.put("to_nwuser_img_url", userInfo.getSocialProfileUrl());	
								}else{
									finalJsonarr.put("to_nwuser_img_url", "");	
								}
							
							}
							finalJsonarr.put("from_nwuser_img_url", "");
							finalJsonarr.put("from_nwuser_name","");
							
							if(userInfo.getSocietyId()!=null && userInfo.getSocietyId().getSocietyName()!=null && userInfo.getSocietyId().getSocietyName().length()>0){
								finalJsonarr.put("society_name", userInfo.getSocietyId().getSocietyName());
							}else{
								finalJsonarr.put("society_name", "");
							}
						}
						System.out.println("=f=dgf=dg=====eeeeeeeeee=============="+String.valueOf(objList[9]));
						if((Integer)objList[9]!=null){
							finalJsonarr.put("to_id", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[9])));
							System.out.println("=f=dgf=dg===================");
							String logusid=Integer.toString(userMst.getUserId());
							System.out.println("=f=dgf=dg=========logusid=========="+logusid);
							if(objList[8]!=null && logusid.equalsIgnoreCase(String.valueOf(objList[9]))){
								userInfo=otp.checkUserDetails(String.valueOf(objList[8]));
							}else{
								userInfo=otp.checkUserDetails(String.valueOf(objList[9]));
							}
							
							String tousername="";
							System.out.println("==entrytime=="+userInfo.getLoginDatetime());
							if(userInfo.getLoginDatetime()!=null){
								String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
								finalJsonarr.put("last_accessed_time", finalDate);	
							}else{
								finalJsonarr.put("last_accessed_time", "");	
							}
							if(userInfo.getFirstName()!=null){
								tousername= userInfo.getFirstName();
								}
							if(userInfo.getLastName()!=null){
								if(tousername!=""){
									tousername +=" "+userInfo.getLastName();
								}
							}
							if(tousername!=null && tousername.equalsIgnoreCase("")){
								tousername=getText("anonymous.user.name");
							}
							finalJsonarr.put("to_nwuser_name",Commonutility.toCheckNullEmpty(tousername));
							if(userInfo.getImageNameMobile()!=null){
								finalJsonarr.put("to_nwuser_img_url", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+objList[9]+"/"+userInfo.getImageNameMobile());
								}else if(userInfo.getSocialProfileUrl()!=null){
									finalJsonarr.put("to_nwuser_img_url", userInfo.getSocialProfileUrl());	
								}else{
									finalJsonarr.put("to_nwuser_img_url", "");	
								}
							if(objList[10]!=null){
							finalJsonarr.put("network_id", String.valueOf(objList[10]));
							}else{
								finalJsonarr.put("network_id","");
							}
							
							
						}else{
							System.out.println("=df=dsf=s=="+userid);
							finalJsonarr.put("to_id", Commonutility.toCheckNullEmpty(String.valueOf(userid)));
							userInfo=otp.checkUserDetails(String.valueOf(userid));
							if(userInfo!=null){
							String tousername="";
							System.out.println("==entrytime=="+userInfo.getLoginDatetime());
							if(userInfo.getLoginDatetime()!=null){
								String finalDate=commonFn.getPostedDateTime(userInfo.getLoginDatetime());
								finalJsonarr.put("last_accessed_time", finalDate);	
							}else{
								finalJsonarr.put("last_accessed_time", "");	
							}
							if(userInfo.getFirstName()!=null){
								tousername= userInfo.getFirstName();
								}
							if(userInfo.getLastName()!=null){
								if(tousername!=""){
									tousername +=" "+userInfo.getLastName();
								}
							}
							if(tousername!=null && tousername.equalsIgnoreCase("")){
								tousername=getText("anonymous.user.name");
							}
							finalJsonarr.put("to_nwuser_name",Commonutility.toCheckNullEmpty(tousername));
							if(userInfo.getImageNameMobile()!=null){
							finalJsonarr.put("to_nwuser_img_url", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userid+"/"+userInfo.getImageNameMobile());
							}else if(userInfo.getSocialProfileUrl()!=null){
								finalJsonarr.put("to_nwuser_img_url", userInfo.getSocialProfileUrl());	
							}else{
								finalJsonarr.put("to_nwuser_img_url", "");	
							}
							}else{
								finalJsonarr.put("to_nwuser_img_url", "");	
								finalJsonarr.put("to_nwuser_name","");
							}
						}
						finalJsonarr.put("from_id", Commonutility.toCheckNullEmpty(String.valueOf((Integer)objList[8])));
						if(objList[10]!=null){
						finalJsonarr.put("network_id", String.valueOf(objList[10]));
						}else{
							finalJsonarr.put("network_id", "");
						}
						jArray.put(finalJsonarr);
						
						}
					//}
					}
					
					locObjRspdataJson.put("networklist", jArray);
					locObjRspdataJson.put("timestamp",timestamp);
					locObjRspdataJson.put("totalrecords",String.valueOf(totalcnt));
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}
				
				/*System.out.println("=========networkMstList======="+networkMstList.size());
				JSONObject finalJsonarr=new JSONObject();
				locObjRspdataJson=new JSONObject();
				JSONArray jArray =new JSONArray();
				if( networkMstList!=null && networkMstList.size()>0){
					MvpNetworkTbl networkObj;
				for (Iterator<MvpNetworkTbl> it = networkMstList
						.iterator(); it.hasNext();) {
					networkObj = it.next();
					finalJsonarr = new JSONObject();
					
					if(networkObj.getUsrParentUsrId()!=null){
						finalJsonarr.put("mynetwork_id", String.valueOf(networkObj.getUsrParentUsrId().getUserId()));
						if(networkObj.getUsrParentUsrId().getFirstName()!=null && networkObj.getUsrParentUsrId().getFirstName()!=""){
							finalJsonarr.put("nwuser_name", networkObj.getUsrParentUsrId().getFirstName());
						}else if(networkObj.getUsrParentUsrId().getLastName()!=null && networkObj.getUsrParentUsrId().getLastName()!=""){
							finalJsonarr.put("nwuser_name", networkObj.getUsrParentUsrId().getLastName());
						}	else{
							finalJsonarr.put("nwuser_name", networkObj.getUsrParentUsrId().getMobileNo());
						}
					}else{
						finalJsonarr.put("mynetwork_id", "");
					}
					if(networkObj.getUsrConnectedUsrId()!=null){
						finalJsonarr.put("nwuser_id", String.valueOf(networkObj.getUsrConnectedUsrId().getUserId()));
					}else{
						finalJsonarr.put("nwuser_id", "");
					}
					if(networkObj.getUsrConnectedUsrId().getSocietyId().getLogoImage()!=null){
						finalJsonarr.put("nwuser_img_url", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/web/"+networkObj.getUsrConnectedUsrId().getUserId()+"/"+networkObj.getUsrConnectedUsrId().getSocietyId().getLogoImage());
						}else{
							finalJsonarr.put("nwuser_img_url", "");
						}
					
					if(networkObj.getUsrConnectedUsrId().getEntryDatetime()!=null){
						Date entrytime=networkObj.getUsrParentUsrId().getModifyDatetime();
						String finalDate=mobiCommon.convertDateToDateTimeDiff(entrytime);
						finalJsonarr.put("last_accessed_time", finalDate);	
					}else{
						finalJsonarr.put("last_accessed_time", "");
					}
					
					if(networkObj.getUsrConnectedUsrId()!=null){
						finalJsonarr.put("is_online",String.valueOf( networkObj.getUsrConnectedUsrId().getIvrBnISONLINEFLG()));	
					}else{
						finalJsonarr.put("is_online", "");
					}
					if(networkObj.getUsrConnectedUsrId()!=null){
						finalJsonarr.put("invite_status", String.valueOf(networkObj.getConnStsFlg()));	
					}else{
						finalJsonarr.put("invite_status", "");
					}
					
					jArray.put(finalJsonarr);
				}
				locObjRspdataJson.put("networklist", jArray);
				locObjRspdataJson.put("timestamp",timestamp);
				locObjRspdataJson.put("totalrecords",String.valueOf(count));
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}
				
				else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", networkUserList.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======network User ====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, network User  an unhandled error occurred", "info", networkUserList.class);
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
