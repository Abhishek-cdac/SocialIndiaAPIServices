package com.mobi.evoting;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.carpooling.SearchCarPoolList;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MvpEvotingMstTbl;

public class ResidentEvotingList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	EvotingDao evotingmobhbm=new EvotingDaoServices();
	List<Object[]> evotingMstList = new ArrayList<Object[]>();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
	public String execute(){
		
		System.out.println("************mobile Resident EvotingList search List services******************");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
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
				String isMobile=(String)  Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String search = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "search");
				String publishedStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "published_status");
				String evotingId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "evoting_id");
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				
				String orderBy = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "order_by");
				String title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "title");
				String fromDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "from_date");
				String toDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "to_date");
				//String votingStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "voting_status");
				String minVote = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "min_vote");
				//String servicecode,String isMobile,String search,String rid,String publishedStatus,String evotingId,String locTimeStamp,String startlimit,String orderBy,String title,String fromDate,String toDate,String minVote
				
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
				
				if (Commonutility.checkempty(minVote)) {
					
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("minVote.length.error")));
				}
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				
				
					
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
				if(flg){
					if (Commonutility.checkempty(locTimeStamp)) {
					} else {
						locTimeStamp=Commonutility.timeStampRetStringVal();
					}
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				int societyId=userMst.getSocietyId().getSocietyId();
				if(minVote!=null && minVote.equalsIgnoreCase("1")){
				getEvotingListForVotedTopic(servicecode,isMobile,search,rid,publishedStatus,evotingId,locTimeStamp,startlimit,orderBy,title,fromDate,toDate,minVote,userMst,societyId);
				}else if(minVote!=null && minVote.equalsIgnoreCase("0")){
					getEvotingListForNotVotedTopic(servicecode,isMobile,search,rid,publishedStatus,evotingId,locTimeStamp,startlimit,orderBy,title,fromDate,toDate,minVote,userMst,societyId);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0206",mobiCommon.getMsg("R0206"),locObjRspdataJson);
					}
				
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", SearchCarPoolList.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======Search CarPool List====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", SearchCarPoolList.class);
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
	
	public void getEvotingListForVotedTopic(String servicecode,String isMobile,String search,String rid,String publishedStatus,String evotingId,String locTimeStamp,String startlimit,String orderBy,String title,String fromDate,String toDate,String minVote,UserMasterTblVo userMst,int societyId){
		try{
			String searchField="where EMST.STATUS_FLAG=1 and usr.SOCIETY_ID='"+userMst.getSocietyId().getSocietyId()+"' and EMPOL.VOTING_BY="+rid;
			if(search!=null && search.length()>0){
				searchField+=" and (EMST.TITILE like('%"+search+"%') or EMST.DESCRIPTION like ('%"+search+"%')) ";
			}
			if(publishedStatus!=null && publishedStatus.length()>0){
				searchField+=" and EMST.PUBLISH_FLAG='"+publishedStatus+"'";
			}
			if(evotingId!=null && evotingId.length()>0){
				searchField+=" and EMST.EVOTING_ID='"+evotingId+"'";
			}
			
			if(title!=null && title.length()>0){
				searchField+=" and EMST.TITILE like'%"+title+"%'";
			}
			/*if(userMst.getGroupCode().getGroupCode()==5){
				searchField+=" and EMST.ENTRY_BY='"+rid+"'";
			}*/
			
			if(fromDate.length() >0 && toDate.length() >0){
				searchField += " and DATE(EMST.ENTRY_DATETIME) between date('" + fromDate + "')  and date('" + toDate + "') ";
			}else if(fromDate.length() >0){
				searchField += " and DATE(EMST.ENTRY_DATETIME) >= date('" + fromDate + "' ) ";
			}else if(toDate.length() >0){
				searchField += " and DATE(EMST.ENTRY_DATETIME) <= date('" + toDate + "') ";
			}
		
			
			
			String orderbyqry=" order by EMPOL.VOTING_DATE desc";
			if(orderBy!=null && orderBy.length()>0){
				orderbyqry=" order by EMPOL.VOTING_DATE "+orderBy;
			}
			
			String totcountqry="SELECT count(*) "
					+ "FROM MVP_EVOTING_MST_TBL EMST "
					+ "INNER join MVP_EVOTE_POOLING_TBL EMPOL ON EMST.EVOTING_ID=EMPOL.EVOTING_MST_ID "
					+ "LEFT JOIN MVP_EVOTING_IMAGE_TBL EIMG ON EMST.EVOTING_ID=EIMG.EVOTING_ID "
					+ "LEFT JOIN USR_REG_TBL USR ON EMST.ENTRY_BY=USR.USR_ID  "+searchField;
			int totalcnt=commonHbm.getTotalCountSqlQuery(totcountqry);
			System.out.println("totalcnt--------"+totalcnt);
			String societyuser="select count(*) from UserMasterTblVo where societyId.societyId='"+societyId+"' and statusFlag=1";
			int totalsocietyuser=commonHbm.getTotalCountQuery(societyuser);
			String locVrSlQry="SELECT USR.USR_ID rid,USR.EMAIL_ID creator_emailid,USR.MOBILE_NO as mobileno,usr.FNAME firstName,usr.LNAME lastName,"
					+ "usr.IMAGE_NAME_MOBILE imagename,usr.SOCIAL_PICTURE socialImage,"
					+ "EMST.EVOTING_ID EVOTING_ID,EMST.TITILE TITILE,EMST.DESCRIPTION DESCRIPTION,EMST.STATUS_FLAG STATUS_FLAG,EMST.PUBLISH_FLAG PUBLISH_FLAG,"
					+ "EIMG.IMAGE_NAME IMAGE_NAME,EMPOL.VOTING_DATE,EMPOL.VOTING_STATUS,EMPOL.VOTING_BY,EMST.ENTRY_DATETIME entryTime,EIMG.EVOTING_IMAGE_ID EVOTING_IMG_ID "
					+ "FROM MVP_EVOTING_MST_TBL EMST "
					+ "INNER join MVP_EVOTE_POOLING_TBL EMPOL ON EMST.EVOTING_ID=EMPOL.EVOTING_MST_ID "
					+ "LEFT JOIN MVP_EVOTING_IMAGE_TBL EIMG ON EMST.EVOTING_ID=EIMG.EVOTING_ID "
					+ "LEFT JOIN USR_REG_TBL USR ON EMST.ENTRY_BY=USR.USR_ID  "+searchField+orderbyqry;
			evotingMstList=evotingmobhbm.getResidentEvotingMastergListbysqlQuery(locVrSlQry,startlimit,getText("total.row"),locTimeStamp);
			//docMangList=infoLibrary.getDocumentList(rid,timestamp,startlimit,getText("total.row"),societyId);
			System.out.println("=========evotingMstList======="+evotingMstList.size());
			JSONObject finalJsonarr=new JSONObject();
			JSONObject locObjRspdataJson=new JSONObject();
			JSONArray jArray =new JSONArray();
			if(evotingMstList!=null && evotingMstList.size()>0){
				List<Object[]> eventListObj1 = new ArrayList<Object[]>();
				Object[] objList;
				
				MvpEvotingMstTbl evotingObj;
				FunctionUtility common = new FunctionUtilityServices();
				String formatToString="yyyy-MM-dd HH:mm:ss";
				for(Iterator<Object[]> it=evotingMstList.iterator();it.hasNext();) {
					objList=it.next();
					finalJsonarr=new JSONObject();
					int resevotingId=(Integer)objList[7];
					System.out.println("rid------------"+(Integer)objList[0]);
					int creatorUsrId=(Integer)objList[0];
					finalJsonarr.put("rid", creatorUsrId);
					String creatorEmail=(String)objList[1];
					if(creatorEmail!=null && creatorEmail.length()>0){
					finalJsonarr.put("creator_emailid",  creatorEmail);
					}else{
						finalJsonarr.put("creator_emailid",  "");
					}
					System.out.println("11111");
					String mobileno=(String)objList[2];
					if(mobileno!=null && mobileno.length()>0){
						finalJsonarr.put("creator_mobno", mobileno);
						}else{
							finalJsonarr.put("creator_mobno",  "");
						}
					System.out.println("2222222222222");
					String firstname=(String)objList[3];
					String lastname=(String)objList[4];
					
					String name="";
					if(firstname!=null){
						name=firstname;
					}
					if(lastname!=null){
						if(!name.equalsIgnoreCase("")){
							name=" "+lastname;
						}else{
							name=lastname;
						}
					}
					finalJsonarr.put("creator_name", name);
					System.out.println("33333333333");
					String imageName=(String)objList[5];
					String socialImage=(String)objList[6];
					String externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+creatorUsrId +"/";
					if(imageName!=null && imageName.length()>0){
					finalJsonarr.put("creator_image",  externalUserImagePath+Commonutility.stringToStringempty(imageName));
					}else if(socialImage!=null && socialImage.length()>0){
						finalJsonarr.put("creator_image",  Commonutility.stringToStringempty(socialImage));
						}else{
							finalJsonarr.put("creator_image","");
						}
					System.out.println("4444444");
					finalJsonarr.put("title", (String)objList[8]);
					finalJsonarr.put("description", (String)objList[9]);
					finalJsonarr.put("evoting_id", ""+resevotingId);
					finalJsonarr.put("published_status", ""+(Integer)objList[11]);
					finalJsonarr.put("society_user_count", ""+totalsocietyuser);
					finalJsonarr.put("min_vote", ""+minVote);
					
					String entrytimr=(String)objList[16];
					System.out.println("entrytimr------------"+entrytimr);
					finalJsonarr.put("evoting_start_date", entrytimr);
					String evotingImage=(String)objList[12];
					JSONArray imagearray=new JSONArray();
					JSONObject imageObj=new JSONObject();
					if(evotingImage!=null){
						String evotImageId=(String)objList[17];
						imageObj.put("img_id", ""+evotImageId);
					String externalevotingImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/Evoting/mobile/"+resevotingId +"/";
					imageObj.put("img_url", externalevotingImagePath+Commonutility.stringToStringempty(evotingImage));
					imagearray.put(imageObj);
					}else{
						/*imageObj.put("img_id", "");
						imageObj.put("img_url",  "");*/
					}
					
					finalJsonarr.put("images",imagearray);
					String votingDate=(String)objList[13];
					String votingStatus=(String)objList[14];
					String voteBy=(String)objList[15];
					if(votingDate!=null){
					finalJsonarr.put("voting_date",""+votingDate);
					}else{
						finalJsonarr.put("voting_date","");
					}
					if(votingStatus!=null){
						finalJsonarr.put("voting_status",""+votingStatus);
						}else{
							finalJsonarr.put("voting_status","");
						}
					if(voteBy!=null){
						finalJsonarr.put("vote_by",""+voteBy);
						}else{
							finalJsonarr.put("vote_by","");
						}
					
					jArray.put(finalJsonarr);
				}
				
			locObjRspdataJson.put("evoting_detail", jArray);
			locObjRspdataJson.put("totalrecords",totalcnt);
			locObjRspdataJson.put("timestamp",locTimeStamp);
			serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void getEvotingListForNotVotedTopic(String servicecode,String isMobile,String search,String rid,String publishedStatus,String evotingId,String locTimeStamp,String startlimit,String orderBy,String title,String fromDate,String toDate,String minVote,UserMasterTblVo userMst,int societyId){
		try{
			String searchField="where EMST.STATUS_FLAG=1 and usr.SOCIETY_ID='"+userMst.getSocietyId().getSocietyId()+"' and EMST.PUBLISH_FLAG=1";
			if(search!=null && search.length()>0){
				searchField+=" and (EMST.TITILE like('%"+search+"%') or EMST.DESCRIPTION like ('%"+search+"%')) ";
			}
			if(publishedStatus!=null && publishedStatus.length()>0){
				searchField+=" and EMST.PUBLISH_FLAG='"+publishedStatus+"'";
			}
			if(evotingId!=null && evotingId.length()>0){
				searchField+=" and EMST.EVOTING_ID='"+evotingId+"'";
			}
			
			if(title!=null && title.length()>0){
				searchField+=" and EMST.TITILE like'%"+title+"%'";
			}
			/*if(userMst.getGroupCode().getGroupCode()==5){
				searchField+=" and EMST.ENTRY_BY='"+rid+"'";
			}*/
			
			if(fromDate.length() >0 && toDate.length() >0){
				searchField += " and DATE(EMST.ENTRY_DATETIME) between date('" + fromDate + "')  and date('" + toDate + "') ";
			}else if(fromDate.length() >0){
				searchField += " and DATE(EMST.ENTRY_DATETIME) >= date('" + fromDate + "' ) ";
			}else if(toDate.length() >0){
				searchField += " and DATE(EMST.ENTRY_DATETIME) <= date('" + toDate + "') ";
			}
		
			
			
			String orderbyqry=" order by EMST.MODIFY_DATETIME desc";
			if(orderBy!=null && orderBy.length()>0){
				orderbyqry=" order by EMST.MODIFY_DATETIME "+orderBy;
			}
			
			String totcountqry="select count(*) "
					+ "from (SELECT USR.USR_ID rid,USR.EMAIL_ID creator_emailid,USR.MOBILE_NO as mobileno,usr.FNAME firstName,usr.LNAME lastName,"
					+ "usr.IMAGE_NAME_MOBILE imagename,usr.SOCIAL_PICTURE socialImage,"
					+ "EMST.EVOTING_ID EVOTING_ID,EMST.TITILE TITILE,EMST.DESCRIPTION DESCRIPTION,EMST.STATUS_FLAG STATUS_FLAG,EMST.PUBLISH_FLAG PUBLISH_FLAG,"
					+ "EIMG.IMAGE_NAME IMAGE_NAME,EMPOL.VOTING_DATE VOTING_DATE,EMPOL.VOTING_STATUS,EMPOL.VOTING_BY,EMST.ENTRY_DATETIME entryTime,EIMG.EVOTING_IMAGE_ID EVOTING_IMG_ID,"
					+ "case when  (SELECT count(*) FROM mvp_evote_pooling_tbl WHERE VOTING_BY='"+rid+"' and EVOTING_MST_ID=EMST.EVOTING_ID)>0 then 1 else 0 end flg "
					+ "FROM MVP_EVOTING_MST_TBL EMST LEFT join MVP_EVOTE_POOLING_TBL EMPOL ON EMST.EVOTING_ID=EMPOL.EVOTING_MST_ID "
					+ "LEFT JOIN MVP_EVOTING_IMAGE_TBL EIMG ON EMST.EVOTING_ID=EIMG.EVOTING_ID LEFT JOIN USR_REG_TBL USR ON EMST.ENTRY_BY=USR.USR_ID "+ searchField+")tt where flg=0";
			int totalcnt=commonHbm.getTotalCountSqlQuery(totcountqry);
			System.out.println("totalcnt--------"+totalcnt);
			String societyuser="select count(*) from UserMasterTblVo where societyId.societyId='"+societyId+"' and statusFlag=1";
			int totalsocietyuser=commonHbm.getTotalCountQuery(societyuser);
			/*String locVrSlQry="SELECT USR.USR_ID rid,USR.EMAIL_ID creator_emailid,USR.MOBILE_NO as mobileno,usr.FNAME firstName,usr.LNAME lastName,"
					+ "usr.IMAGE_NAME_MOBILE imagename,usr.SOCIAL_PICTURE socialImage,"
					+ "EMST.EVOTING_ID EVOTING_ID,EMST.TITILE TITILE,EMST.DESCRIPTION DESCRIPTION,EMST.STATUS_FLAG STATUS_FLAG,EMST.PUBLISH_FLAG PUBLISH_FLAG,"
					+ "EIMG.IMAGE_NAME IMAGE_NAME,EMPOL.VOTING_DATE .VOTING_DATE,EMPOL.VOTING_STATUS,EMPOL.VOTING_BY,EMST.ENTRY_DATETIME entryTime,EIMG.EVOTING_IMAGE_ID EVOTING_IMG_ID "
					+ "FROM MVP_EVOTING_MST_TBL EMST "
					+ "LEFT join MVP_EVOTE_POOLING_TBL EMPOL ON EMST.EVOTING_ID=EMPOL.EVOTING_MST_ID "
					+ "LEFT JOIN MVP_EVOTING_IMAGE_TBL EIMG ON EMST.EVOTING_ID=EIMG.EVOTING_ID "
					+ "LEFT JOIN USR_REG_TBL USR ON EMST.ENTRY_BY=USR.USR_ID  "+searchField+orderbyqry;*/
			
			String locVrSlQry="select rid,creator_emailid,mobileno,firstName,lastName,imagename,socialImage,EVOTING_ID,TITILE, DESCRIPTION,STATUS_FLAG,PUBLISH_FLAG,"
					+ "IMAGE_NAME,VOTING_DATE,VOTING_STATUS,VOTING_BY,entryTime,EVOTING_IMG_ID "
					+ "from (SELECT USR.USR_ID rid,USR.EMAIL_ID creator_emailid,USR.MOBILE_NO as mobileno,usr.FNAME firstName,usr.LNAME lastName,"
					+ "usr.IMAGE_NAME_MOBILE imagename,usr.SOCIAL_PICTURE socialImage,"
					+ "EMST.EVOTING_ID EVOTING_ID,EMST.TITILE TITILE,EMST.DESCRIPTION DESCRIPTION,EMST.STATUS_FLAG STATUS_FLAG,EMST.PUBLISH_FLAG PUBLISH_FLAG,"
					+ "EIMG.IMAGE_NAME IMAGE_NAME,EMPOL.VOTING_DATE VOTING_DATE,EMPOL.VOTING_STATUS,EMPOL.VOTING_BY,EMST.ENTRY_DATETIME entryTime,EIMG.EVOTING_IMAGE_ID EVOTING_IMG_ID,"
					+ "case when  (SELECT count(*) FROM mvp_evote_pooling_tbl WHERE VOTING_BY='"+rid+"' and EVOTING_MST_ID=EMST.EVOTING_ID)>0 then 1 else 0 end flg "
					+ "FROM MVP_EVOTING_MST_TBL EMST LEFT join MVP_EVOTE_POOLING_TBL EMPOL ON EMST.EVOTING_ID=EMPOL.EVOTING_MST_ID "
					+ "LEFT JOIN MVP_EVOTING_IMAGE_TBL EIMG ON EMST.EVOTING_ID=EIMG.EVOTING_ID LEFT JOIN USR_REG_TBL USR ON EMST.ENTRY_BY=USR.USR_ID "+ searchField+orderbyqry+")tt where flg=0";
			
			evotingMstList=evotingmobhbm.getResidentEvotingMastergListbysqlQuery(locVrSlQry,startlimit,getText("total.row"),locTimeStamp);
			//docMangList=infoLibrary.getDocumentList(rid,timestamp,startlimit,getText("total.row"),societyId);
			System.out.println("=========evotingMstList======="+evotingMstList.size());
			JSONObject finalJsonarr=new JSONObject();
			JSONObject locObjRspdataJson=new JSONObject();
			JSONArray jArray =new JSONArray();
			if(evotingMstList!=null && evotingMstList.size()>0){
				List<Object[]> eventListObj1 = new ArrayList<Object[]>();
				Object[] objList;
				
				MvpEvotingMstTbl evotingObj;
				FunctionUtility common = new FunctionUtilityServices();
				String formatToString="yyyy-MM-dd HH:mm:ss";
				for(Iterator<Object[]> it=evotingMstList.iterator();it.hasNext();) {
					objList=it.next();
					finalJsonarr=new JSONObject();
					int resevotingId=(Integer)objList[7];
					System.out.println("rid------------"+(Integer)objList[0]);
					int creatorUsrId=(Integer)objList[0];
					finalJsonarr.put("rid", creatorUsrId);
					String creatorEmail=(String)objList[1];
					if(creatorEmail!=null && creatorEmail.length()>0){
					finalJsonarr.put("creator_emailid",  creatorEmail);
					}else{
						finalJsonarr.put("creator_emailid",  "");
					}
					System.out.println("11111");
					String mobileno=(String)objList[2];
					if(mobileno!=null && mobileno.length()>0){
						finalJsonarr.put("creator_mobno", mobileno);
						}else{
							finalJsonarr.put("creator_mobno",  "");
						}
					System.out.println("2222222222222");
					String firstname=(String)objList[3];
					String lastname=(String)objList[4];
					
					String name="";
					if(firstname!=null){
						name=firstname;
					}
					if(lastname!=null){
						if(!name.equalsIgnoreCase("")){
							name=" "+lastname;
						}else{
							name=lastname;
						}
					}
					finalJsonarr.put("creator_name", name);
					System.out.println("33333333333");
					String imageName=(String)objList[5];
					String socialImage=(String)objList[6];
					String externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+creatorUsrId +"/";
					if(imageName!=null && imageName.length()>0){
					finalJsonarr.put("creator_image",  externalUserImagePath+Commonutility.stringToStringempty(imageName));
					}else if(socialImage!=null && socialImage.length()>0){
						finalJsonarr.put("creator_image",  Commonutility.stringToStringempty(socialImage));
						}else{
							finalJsonarr.put("creator_image","");
						}
					System.out.println("4444444");
					finalJsonarr.put("title", (String)objList[8]);
					finalJsonarr.put("description", (String)objList[9]);
					finalJsonarr.put("evoting_id", ""+resevotingId);
					finalJsonarr.put("published_status", ""+(Integer)objList[11]);
					finalJsonarr.put("society_user_count", ""+totalsocietyuser);
					finalJsonarr.put("min_vote", ""+minVote);
					
					String entrytimr=(String)objList[16];
					System.out.println("entrytimr------------"+entrytimr);
					finalJsonarr.put("evoting_start_date", entrytimr);
					String evotingImage=(String)objList[12];
					JSONArray imagearray=new JSONArray();
					JSONObject imageObj=new JSONObject();
					if(evotingImage!=null){
						String evotImageId=(String)objList[17];
						imageObj.put("img_id", ""+evotImageId);
					String externalevotingImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/Evoting/mobile/"+resevotingId +"/";
					imageObj.put("img_url", externalevotingImagePath+Commonutility.stringToStringempty(evotingImage));
					imagearray.put(imageObj);
					}else{
						/*imageObj.put("img_id", "");
						imageObj.put("img_url",  "");*/
					}
					
					finalJsonarr.put("images",imagearray);
					String votingDate=(String)objList[13];
					String votingStatus=(String)objList[14];
					String voteBy=(String)objList[15];
					if(votingDate!=null){
					finalJsonarr.put("voting_date",""+votingDate);
					}else{
						finalJsonarr.put("voting_date","");
					}
					if(votingStatus!=null){
						finalJsonarr.put("voting_status",""+votingStatus);
						}else{
							finalJsonarr.put("voting_status","");
						}
					if(voteBy!=null){
						finalJsonarr.put("vote_by",""+voteBy);
						}else{
							finalJsonarr.put("vote_by","");
						}
					
					jArray.put(finalJsonarr);
				}
				
			locObjRspdataJson.put("evoting_detail", jArray);
			locObjRspdataJson.put("totalrecords",totalcnt);
			locObjRspdataJson.put("timestamp",locTimeStamp);
			serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}