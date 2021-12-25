package com.mobi.evoting;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.carpooling.CarPoolDelete;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MvpEvotePoolingTbl;
import com.socialindiaservices.vo.MvpEvotingMstTbl;

public class ResidentAddVoting  extends ActionSupport {
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
				String votingStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "voting_status");
				
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
				if(evotingId!=null && evotingId.length()>0){
					System.out.println("evotingId------------"+evotingId);
					MvpEvotingMstTbl evotingObj=new MvpEvotingMstTbl();
				String sql="from MvpEvotingMstTbl where statusFlag=1 and evotingId="+evotingId+" and usrRegTbl.societyId.societyId="+societyId;
				evotingObj=evotinghbm.selectEvotingmstTblbyQuery(sql);
				locObjRspdataJson=new JSONObject();
				if(evotingObj!=null){
					CommonMobiDao commonHbm=new CommonMobiDaoService();
					String countqry="select count(*) from MvpEvotePoolingTbl where statusflag=1 and usrRegTbl.userId='"+rid+"' and evotingMstId.evotingId="+evotingId+"";
					int count=commonHbm.getTotalCountQuery(countqry);
					if(count==0){
				      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				      Date dateFor = new Date();
				      Date date = dateFormat.parse(dateFormat.format(dateFor));
					MvpEvotePoolingTbl evotingPoolingObj=new MvpEvotePoolingTbl();
					evotingPoolingObj.setEvotingMstId(evotingObj);
					evotingPoolingObj.setStatusflag(1);
					evotingPoolingObj.setUsrRegTbl(userMst);
					evotingPoolingObj.setVotingDate(date);
					evotingPoolingObj.setVotingStatus(Integer.parseInt(votingStatus));
					evotinghbm.insertEvotingPoolingTbl(evotingPoolingObj);
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0210",mobiCommon.getMsg("R0210"),locObjRspdataJson);
					}
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
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
