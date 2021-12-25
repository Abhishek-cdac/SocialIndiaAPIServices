package com.siservices.forum;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.forumVo.MvpFourmTopicsTbl;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class forumCreation extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<UserMasterTblVo> userList = new ArrayList<UserMasterTblVo>();
	private GroupMasterTblVo groupMaster = new GroupMasterTblVo();
	private SocietyMstTbl societyMaster=new SocietyMstTbl();
	uamDao uam=new uamDaoServices();
	MvpFourmTopicsTbl forumTopicMst=new MvpFourmTopicsTbl();
	signUpDao signup = new signUpDaoServices();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	forumDao forum=new forumServices();
	CommonUtils comutil=new CommonUtilsServices();
	JSONObject jsonFinalObj=new JSONObject();
	CommonUtilsDao common=new CommonUtilsDao();	
	Log log=new Log();	
	boolean flag = true;
	
	public String execute() throws JSONException {
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		try {
			if (ivrparams!= null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					String topicname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "topicname");
					String keysearch = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "keysearch");
					String forumdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "forumdate");
					String topicdesc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "topicdesc");
					int useruniqueId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "useruniqueId");
					int groupcode = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "groupcode");
					forumTopicMst.setTopicsName(topicname);
					forumTopicMst.setKeyForSearch(keysearch);
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					Date date1;
					date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					Date date = formatter.parse(forumdate);
					forumTopicMst.setValidOnDate(date);
					forumTopicMst.setTopicsDesc(topicdesc);
					userInfo.setUserId(useruniqueId);
					groupMaster.setGroupCode(groupcode);
					forumTopicMst.setUserId(userInfo);
					forumTopicMst.setGroupCode(groupMaster);
					forumTopicMst.setStatus(1);
					forumTopicMst.setEntryBy(1);
					forumTopicMst.setEntryDatetime(date1);
					String result = forum.forumCreationForm(forumTopicMst);
		
					if (result.equalsIgnoreCase("success")) {
						serverResponse(servicecode, "00", "R0151", mobiCommon.getMsg("R0151"), jsonFinalObj);
					} else {
						serverResponse(servicecode, "01", "R0152", mobiCommon.getMsg("R0152"), jsonFinalObj);
					}
				} else {
					json = new JSONObject();
					serverResponse("SI0007","01","R0067",mobiCommon.getMsg("R0067"),json);
				}
			} else {
				json = new JSONObject();
				serverResponse("SI0007","01","R0002",mobiCommon.getMsg("R0002"),json);
			}
		}catch(Exception ex){try{
			json = new JSONObject();
			Commonutility.toWriteConsole("Step -1 : Exception found in forumcreation.class : "+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, siCommitteeCreationAction an unhandled error occurred", "info", forumCreation.class);
			serverResponse("SI0007","01","R0003",mobiCommon.getMsg("R0003"),json);
		}catch(Exception ee){}}
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
	
}
