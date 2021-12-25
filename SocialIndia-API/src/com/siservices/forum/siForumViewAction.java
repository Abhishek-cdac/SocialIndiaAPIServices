package com.siservices.forum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
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

public class siForumViewAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<MvpFourmDiscussTbl> forumDiscussList = new ArrayList<MvpFourmDiscussTbl>();
	MvpFourmTopicsTbl forumTopicMst=new MvpFourmTopicsTbl();
	MvpFourmDiscussTbl forumDiscussMst=new MvpFourmDiscussTbl();
	forumDao forum=new forumServices();
	CommonUtils comutil=new CommonUtilsServices();
	JSONObject jsonFinalObj=new JSONObject();
	CommonUtilsDao common=new CommonUtilsDao();	
	Log log=new Log();	
	JSONObject finalJson = new JSONObject();
	boolean flag = true;
	
	public String execute() throws JSONException{
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json	
		try{
			if (ivrparams!= null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				String servicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				int topicsId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "topicsId");		
				forumDiscussList=forum.forumViewAction(topicsId);
				forumTopicMst=forum.getforumtopicsdetails(topicsId);
				JSONObject finalJsonarr=new JSONObject();
				JSONArray jArray =new JSONArray();		
				if (forumDiscussList!=null && forumDiscussList.size()>0) {
					MvpFourmDiscussTbl forumDisObj;
					for (Iterator<MvpFourmDiscussTbl> it = forumDiscussList.iterator(); it.hasNext();) {
						forumDisObj = it.next();
						finalJsonarr = new JSONObject();
						if (forumDisObj.getUserId().getFirstName()!=null){
							finalJsonarr.put("userfirstname", forumDisObj.getUserId().getFirstName());
						} else {
							finalJsonarr.put("userfirstname","");
						}
						if (forumDisObj.getUserId().getLastName()!=null){
						finalJsonarr.put("userlastname", forumDisObj.getUserId().getLastName());
						} else {
							finalJsonarr.put("userlastname", "");
						}
						if (forumDisObj.getUserId().getMobileNo()!=null){
							finalJsonarr.put("usermobileno", forumDisObj.getUserId().getMobileNo());
						} else {
							finalJsonarr.put("usermobileno", "");
						}
						finalJsonarr.put("userid", forumDisObj.getUserId().getUserId());
						finalJsonarr.put("imagename", Commonutility.toCheckNullEmpty(forumDisObj.getUserId().getImageNameWeb()));
						finalJsonarr.put("postcomments", Commonutility.toCheckNullEmpty(forumDisObj.getDiscussionDesc()));
						finalJsonarr.put("entrydate", Commonutility.toCheckNullEmpty(forumDisObj.getEntryDatetime()));
						jArray.put(finalJsonarr);
					}
			finalJson.put("forumdiscussedetails", jArray);
			if(forumTopicMst.getTopicsDesc()!=null){
				finalJson.put("topicdesc", forumTopicMst.getTopicsDesc());
				}else{
					finalJson.put("topicdesc","");
					}
			finalJson.put("topicsid", forumTopicMst.getTopicsId());
			if(forumTopicMst.getTopicsName()!=null){
				finalJson.put("topicname", forumTopicMst.getTopicsName());
			}else{
				finalJson.put("topicname", "");
			}
			if(forumTopicMst.getUserId().getFirstName()!=null){
				finalJson.put("loginuserfirstname", forumTopicMst.getUserId().getFirstName());
			}else{
				finalJson.put("loginuserfirstname", forumTopicMst.getUserId().getFirstName());
			}if(forumTopicMst.getUserId().getLastName()!=null){
				finalJson.put("loginuserlastname", forumTopicMst.getUserId().getLastName());
			}else{
				finalJson.put("loginuserlastname", "");
			}
				if(forumTopicMst.getUserId().getMobileNo()!=null){
					finalJson.put("loginusermobileno", forumTopicMst.getUserId().getMobileNo());
				} else{
				finalJson.put("loginusermobileno", "");
			}
					serverResponse(servicecode,"00","R0155", mobiCommon.getMsg("R0155"), finalJson);			
				} else {
					if (forumTopicMst!=null){
						if(forumTopicMst.getTopicsDesc()!=null){
							finalJson.put("topicdesc", forumTopicMst.getTopicsDesc());
						} else {
							finalJson.put("topicdesc", "");
						}
						if (forumTopicMst.getTopicsName() != null) {
							finalJson.put("topicname",forumTopicMst.getTopicsName());
						} else {
							finalJson.put("topicname", "");
						}
						finalJson.put("topicsid", forumTopicMst.getTopicsId());
						if (forumTopicMst.getUserId().getFirstName() != null) {
							finalJson.put("userfirstname", forumTopicMst.getUserId().getFirstName());
						} else {
							finalJson.put("userfirstname", "");
						}
						if (forumTopicMst.getUserId().getLastName() != null) {
							finalJson.put("userlastname", forumTopicMst.getUserId().getLastName());
						} else {
							finalJson.put("userlastname", "");
						}
						serverResponse(servicecode, "01", "R0156",mobiCommon.getMsg("R0156"), finalJson);
					} else {
						serverResponse(servicecode, "01", "R0156",mobiCommon.getMsg("R0156"), finalJson);
					}
				}
		
				} else {
					json=new JSONObject();
					serverResponse("SI0007","01","R0067",mobiCommon.getMsg("R0067"),json);
				}
			} else {
				json=new JSONObject();
				serverResponse("SI0007","01","R0002",mobiCommon.getMsg("R0002"),json);
			}
		}catch(Exception ex){try{
			Commonutility.toWriteConsole("Step -1 : Fourm view action Exception : "+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, siCommitteeCreationAction an unhandled error occurred", "info", siForumViewAction.class);
			json=new JSONObject();
			serverResponse("SI0007","02","R0003",mobiCommon.getMsg("R0003"),json);
		} catch (Exception e){}
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