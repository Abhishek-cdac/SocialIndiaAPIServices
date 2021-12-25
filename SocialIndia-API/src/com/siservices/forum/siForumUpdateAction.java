package com.siservices.forum;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.forumVo.MvpFourmDiscussTbl;
import com.siservices.forumVo.MvpFourmTopicsTbl;
import com.siservices.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class siForumUpdateAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	MvpFourmTopicsTbl forumTopicMst = new MvpFourmTopicsTbl();
	MvpFourmDiscussTbl forumDiscussMst = new MvpFourmDiscussTbl();
	forumDao forum = new forumServices();
	CommonUtils comutil = new CommonUtilsServices();
	JSONObject jsonFinalObj = new JSONObject();
	CommonUtilsDao common = new CommonUtilsDao();
	JSONObject finalJson = new JSONObject();
	boolean flag = true;
	
	public String execute() throws JSONException{
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		Log log = null;	
		try{
			log = new Log();	
			if (ivrparams!= null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {		
					locObjRecvJson = new JSONObject(ivrparams);
					String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					int topicsId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "topicsid");
					String topicsname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "topicsname");
					String keysearch = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "keysearch");
					String validdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "validdate");
					String description = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "description");
					forumTopicMst.setTopicsId(topicsId);
					forumTopicMst.setTopicsName(topicsname);
					forumTopicMst.setKeyForSearch(keysearch);
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					Date startDate = df.parse(validdate);
					forumTopicMst.setValidOnDate(startDate);
					forumTopicMst.setTopicsDesc(description);
					boolean result=forum.getforumtopicsUpdate(forumTopicMst);
					if (result == true) {
						serverResponse(servicecode, "00", "R0153", mobiCommon.getMsg("R0153"), finalJson);
					} else {
						serverResponse(servicecode, "01", "R0154", mobiCommon.getMsg("R0154"), finalJson);
					}
				} else {
					json = new JSONObject();
					serverResponse("SI0007","01","E0001",mobiCommon.getMsg("R0067"),json);
				}
			} else {
				json =new JSONObject();
				serverResponse("SI0007","01","R0002",mobiCommon.getMsg("R0002"),json);
			}
		}catch(Exception ex){
			try{
				log.logMessage("Service code : ivrservicecode, Sorry, siForumUpdateAction an unhandled error occurred", "info", siForumUpdateAction.class);
				serverResponse("SI0007","01","R0003",mobiCommon.getMsg("R0003"),json);
			} catch(Exception e){}
		} finally {
			log = null;
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
	
}
