package com.mobi.evoting;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.carpooling.CarPoolDelete;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MerchantTblVO;
import com.socialindiaservices.vo.MvpEvotePoolingTbl;
import com.socialindiaservices.vo.MvpEvotingImageTbl;
import com.socialindiaservices.vo.MvpEvotingMstTbl;

public class PublishEvoting  extends ActionSupport {
	Log log=new Log();	
	otpDao otp=new otpDaoService();
	private String ivrparams;
	EvotingDao evotinghbm=new EvotingDaoServices();
	CommonUtils commjvm=new CommonUtils();
	
	public String execute(){
		
		System.out.println("************mobile Delete Evoting services******************");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			System.out.println("ivrparams--------------"+ivrparams);
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("ivrparams--------------"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				System.out.println("ivrparams--------------"+ivrparams);
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String evotingId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "evoting_id");
				
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
				int societyId=userMst.getSocietyId().getSocietyId();
				System.out.println("societyId---------"+societyId);
				if(evotingId!=null && evotingId.length()>0 ){
					if(userMst.getGroupCode().getGroupCode()==5){
					System.out.println("evotingId------------"+evotingId);
					MvpEvotingMstTbl evotingObj=new MvpEvotingMstTbl();
				String sql="from MvpEvotingMstTbl where statusFlag=1 and evotingId="+evotingId+" and usrRegTbl.societyId.societyId="+societyId;
				evotingObj=evotinghbm.selectEvotingmstTblbyQuery(sql);
				locObjRspdataJson=new JSONObject();
				if(evotingObj!=null){
				     /* DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				      Date dateFor = new Date();
				      Date date = dateFormat.parse(dateFormat.format(dateFor));*/
					Common commonhbm=new CommonDao();
				      String updateqry="update MvpEvotingMstTbl set publishFlag=2 where evotingId="+evotingId;
				      boolean isupdate=commonhbm.commonUpdate(updateqry);
			if(isupdate){
				FeedDAO feed=new FeedDAOService();
				FeedsTblVO feedMst=new FeedsTblVO();
				feedMst.setUsrId(userMst);
				feedMst.setFeedType(11);
				feedMst.setFeedTypeId(Integer.parseInt(evotingId));
				feedMst.setIsMyfeed(1);
				feedMst.setIsShared(0);
				feedMst.setFeedPrivacyFlg(2);
				feedMst.setFeedStatus(1);
				feedMst.setFeedTitle(evotingObj.getTitile());
				feedMst.setFeedDesc(evotingObj.getDescription());
				feedMst.setFeedCategory("Evoting");
				feedMst.setSocietyId(userMst.getSocietyId());
				String additionalData="";
				String voteCountQry="SELECT AGREE,DISAGREE,NEED_MEETING,NO_VOTE,TOTAL_VOTE FROM "
						+ "(SELECT IFNULL((SELECT COUNT(VOTING_STATUS)  FROM MVP_EVOTE_POOLING_TBL WHERE EVOTING_MST_ID=EMST.EVOTING_ID AND VOTING_STATUS=1 GROUP BY EVOTING_MST_ID),0) AGREE,"
						+ "IFNULL((SELECT COUNT(VOTING_STATUS)  FROM MVP_EVOTE_POOLING_TBL WHERE EVOTING_MST_ID=EMST.EVOTING_ID AND VOTING_STATUS=2 GROUP BY EVOTING_MST_ID),0) DISAGREE,"
						+ "IFNULL((SELECT COUNT(VOTING_STATUS)  FROM MVP_EVOTE_POOLING_TBL WHERE EVOTING_MST_ID=EMST.EVOTING_ID AND VOTING_STATUS=3 GROUP BY EVOTING_MST_ID),0) NEED_MEETING,"
						+ "IFNULL((SELECT COUNT(VOTING_STATUS)  FROM MVP_EVOTE_POOLING_TBL WHERE EVOTING_MST_ID=EMST.EVOTING_ID AND VOTING_STATUS=4 GROUP BY EVOTING_MST_ID),0) NO_VOTE,"
						+ "IFNULL((SELECT COUNT(VOTING_STATUS)  FROM MVP_EVOTE_POOLING_TBL WHERE EVOTING_MST_ID=EMST.EVOTING_ID  GROUP BY EVOTING_MST_ID),0) TOTAL_VOTE "
						+ "FROM MVP_EVOTING_MST_TBL EMST where EMST.EVOTING_ID='"+evotingId+"')TT";
				Object[] votecountobj=evotinghbm.getEvotingCountByQuery(voteCountQry);
				//total<SI>aggree<SI>disagree<SI>need meeting<SI>novote
				additionalData=votecountobj[4]+"<SI>"+votecountobj[0]+"<SI>"+votecountobj[1]+"<SI>"+votecountobj[2]+"<SI>"+votecountobj[3];
				feedMst.setAdditionalData(additionalData);
				feedMst.setEntryBy(userMst.getUserId());
				feedMst.setFeedTime(Commonutility.enteyUpdateInsertDateTime());
				feedMst.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
				feedMst.setOriginatorId(userMst.getUserId());
				feedMst.setOriginatorName(Commonutility.toCheckNullEmpty(userMst.getFirstName())+Commonutility.toCheckNullEmpty(userMst.getLastName()));
				feedMst.setPostBy(userMst.getUserId());
				feedMst.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
				
				int feedid=feed.feedInsert("",feedMst,null,null,null);
				feedMst.setFeedId(feedid);
				 List<MvpEvotingImageTbl> evotingListobj=new ArrayList<MvpEvotingImageTbl>();
				String hsql="from MvpEvotingImageTbl where mvpEvotingMstTbl.evotingId='"+evotingId+"'";
				evotingListobj=evotinghbm.selectEvotingmstTblListbyQuery(hsql);
				MvpEvotingImageTbl MvpEvotingImageTblObj;
				FeedDAO feeddaohbm=new FeedDAOService();
					for(Iterator<MvpEvotingImageTbl> it=evotingListobj.iterator();it.hasNext();){
						MvpEvotingImageTblObj=it.next();
						FeedattchTblVO feedAttchObj = new FeedattchTblVO();
						feedAttchObj.setIvrBnFEED_ID(feedMst);
						feedAttchObj.setIvrBnATTACH_NAME(MvpEvotingImageTblObj.getImageName());
						feedAttchObj.setIvrBnFILE_TYPE(1);
						feedAttchObj.setIvrBnSTATUS(1);
						feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feeddaohbm.feedInsertAttachment(feedAttchObj);
						String sourcePath =getText("external.imagesfldr.path")+"Evoting/web/"+evotingId;
						String destwebPath =getText("external.imagesfldr.path")+"feed/web/"+feedid;
						String destmobilePath =getText("external.imagesfldr.path")+"feed/mobile/"+feedid;
						
						System.out.println("sourcePath-----"+sourcePath);
						System.out.println("destwebPath----------"+destwebPath);
						System.out.println("destmobilePath---------"+destmobilePath);
						File sourceFile  = new File(sourcePath, MvpEvotingImageTblObj.getImageName());
						File destFile  = new File(destwebPath, MvpEvotingImageTblObj.getImageName());
						File destmobileFile  = new File(destmobilePath, MvpEvotingImageTblObj.getImageName());
						
						FileUtils.copyFile(sourceFile, destFile);
						FileUtils.copyFile(sourceFile, destmobileFile);
				}
				
				serverResponse(servicecode,"00","R0244",mobiCommon.getMsg("R0244"),locObjRspdataJson);
			}else{
				serverResponse(servicecode,"01","R0020",mobiCommon.getMsg("R0020"),locObjRspdataJson);
			}
			
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0209",mobiCommon.getMsg("R0209"),locObjRspdataJson);
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", CarPoolDelete.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======CarPool Delete====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", CarPoolDelete.class);
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
