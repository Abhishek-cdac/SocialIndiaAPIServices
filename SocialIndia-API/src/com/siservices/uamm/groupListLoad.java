package com.siservices.uamm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.Log;

public class groupListLoad extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<GroupMasterTblVo> groupList = new ArrayList<GroupMasterTblVo>();
	uamDao uam=new uamDaoServices();
	JSONObject jsonFinalObj=new JSONObject();
	Log logWrite = null;
	public String execute() {
		logWrite = new Log();;
		JSONObject json = new JSONObject();
		try{
			boolean result;
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				 json = new JSONObject(ivrparams);
				 String servicecode=json.getString("servicecode");		
				 groupList = uam.getGroupDetailList();			
				 JSONArray jArray =new JSONArray();
				 GroupMasterTblVo groupMaster;
				 for (Iterator<GroupMasterTblVo> it = groupList.iterator(); it.hasNext();) {
					 groupMaster = it.next();
					 JSONObject finalJson = new JSONObject();
					 finalJson.put("groupname", groupMaster.getGroupName());
					 finalJson.put("statusflg", groupMaster.getStatusFlag());
					 finalJson.put("entrydate", groupMaster.getEntryDatetime());
					 finalJson.put("groupcode", groupMaster.getGroupCode());
					 jArray.put(finalJson);				
				}		
				 jsonFinalObj.put("groupMgmt", jArray);
				 serverResponse(servicecode, "0","0000", "sucess", jsonFinalObj);
			} else {
				json=new JSONObject();
				serverResponse("SI0012","1","E0001","Request values not not correct format",json);
			}

		} catch(Exception ex){
				System.out.println("Exception Found groupListLoad.class : "+ex);
				logWrite.logMessage("Exception Found : "+ex, "error", groupListLoad.class);
		}finally{
			logWrite = null;
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
