package com.siservices.committeeMgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.siservices.committeeMgmt.persistense.committeeDao;
import com.siservices.committeeMgmt.persistense.committeeServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.social.utils.Log;

public class siCommitteeUpdateAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<TownshipMstTbl> townShipList = new ArrayList<TownshipMstTbl>();
	TownshipMstTbl townShipMst =new TownshipMstTbl();
	SocietyMstTbl societyMst=new SocietyMstTbl();
	committeeDao committee=new committeeServices();
	CommittteeRoleMstTbl committeeMst=new CommittteeRoleMstTbl();
	JSONObject jsonFinalObj=new JSONObject();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	boolean result=false;

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
	//	Session locObjsession = null;		
		String ivrservicecode=null;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				
				int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
				String firstName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "firstName");
				String lastName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "lastName");
				String dob = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "dob");
				int townshipId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipId");
				int societyId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyId");
				int committeerole = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "committeerole");
				int accesschannel = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "accesschannel");
				String gender = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "gender");
				String emailid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailid");
				userInfo.setUserId(userId);
				userInfo.setFirstName(firstName);
				userInfo.setLastName(lastName);
				userInfo.setDob(dob);
				userInfo.setEmailId(emailid);
				userInfo.setGender(gender);
				userInfo.setAccessChannel(accesschannel);				
				result = committee.committeeInfoUpdate(userInfo,townshipId,societyId,committeerole);
					if (result == true) {				
						serverResponse(ivrservicecode, "00","R0093", mobiCommon.getMsg("R0093"), jsonFinalObj);				
					} else if(result==false){
						jsonFinalObj=null;
						serverResponse(ivrservicecode, "01","R0094", mobiCommon.getMsg("R0094"), jsonFinalObj);
					} else{
						serverResponse(ivrservicecode,"01","R0094",mobiCommon.getMsg("R0094"),locObjRspdataJson);
					}
					
				}else{
					//Response call
					serverResponse(ivrservicecode,"01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siCommitteeUpdateAction.class);
			try {
				serverResponse(ivrservicecode,"02","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			//if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
			throws Exception {
		PrintWriter out;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response;
		response = ServletActionContext.getResponse();
		out = response.getWriter();
		try {
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
		} catch (Exception ex) {
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	public TownshipMstTbl getTownShipMst() {
		return townShipMst;
	}

	public void setTownShipMst(TownshipMstTbl townShipMst) {
		this.townShipMst = townShipMst;
	}

}
