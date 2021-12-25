package com.siservices.uamm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.Log;

public class groupMgmtAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;	
	JSONObject jsonFinalObj=new JSONObject();
	public String execute(){
		JSONObject json = new JSONObject();
		GroupMasterTblVo groupMaster = null;
		uamDao uam = null;
		Common locCommonObj = null;
		Date lvrEntrydate = null;
		List<GroupMasterTblVo> groupList = null;
		Log logwrite = null;
		try {
			groupList = new ArrayList<GroupMasterTblVo>();
			logwrite = new Log();
			locCommonObj=new CommonDao();
			Commonutility.toWriteConsole("Step 1 : Group Manage Action called.[Start]");
			logwrite.logMessage("Step 1 : Group Manage Action called.[Start]", "info", groupMgmtAction.class);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {
				json = new JSONObject(ivrparams);		
				String servicecode = (String) Commonutility.toHasChkJsonRtnValObj(json, "servicecode");
				String sqlQuery = (String) Commonutility.toHasChkJsonRtnValObj(json, "sqlQuery");
				String sqlQueryFilter = (String) Commonutility.toHasChkJsonRtnValObj(json, "sqlQueryFilter");	
				String lvrSrchstr = (String) Commonutility.toHasChkJsonRtnValObj(json, "sdtSearch");
				uam = new uamDaoServices();
				int count= uam.getInitTotal(sqlQuery);
				int countFilter= uam.getTotalFilter(sqlQueryFilter);
				if (Commonutility.checkempty(lvrSrchstr)) {
					groupList = uam.getGroupDetailListSrh(lvrSrchstr);	
				} else {
					groupList = uam.getGroupDetailList();	
				}				
				Commonutility.toWriteConsole("Step 2 : Group Manage Query executed.");
				logwrite.logMessage("Step 2 : Group Manage Query executed.", "info", groupMgmtAction.class);
				JSONArray jArray =new JSONArray();
		
			for (Iterator<GroupMasterTblVo> it = groupList.iterator(); it.hasNext();) {
				groupMaster = it.next();
				JSONObject finalJson = new JSONObject();
				Long lvrMembercnt = (Long) locCommonObj.getuniqueColumnVal("UserMasterTblVo", "count(*)", "groupCode", Commonutility.toCheckNullEmpty(groupMaster.getGroupCode()));
				finalJson.put("groupname", Commonutility.toCheckNullEmpty(groupMaster.getGroupName()));
				finalJson.put("statusflg", Commonutility.toCheckNullEmpty(groupMaster.getStatusFlag()));
				/*finalJson.put("entrydate", Commonutility.toCheckNullEmpty(groupMaster.getEntryDatetime()));*/
				lvrEntrydate=groupMaster.getEntryDatetime();
				finalJson.put("entrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "dd-MM-yyyy HH:mm:ss"));						
				finalJson.put("groupcode", Commonutility.toCheckNullEmpty(groupMaster.getGroupCode()));
				finalJson.put("grpmembercnt", Commonutility.toCheckNullEmpty(lvrMembercnt));
				jArray.put(finalJson);
			
			}
				jsonFinalObj.put("InitCount", count);
				jsonFinalObj.put("countAfterFilter", countFilter);
				jsonFinalObj.put("groupMgmt", jArray);
				Commonutility.toWriteConsole("Step 3 : Group Manage Action called.[End]");
				logwrite.logMessage("Step 3 : Group Manage Action called.[End]", "info", groupMgmtAction.class);
				serverResponse(servicecode, "0", "0000", "sucess", jsonFinalObj);
			} else {
				json = new JSONObject();
				serverResponse("SI0006","1","E0001","Request values not not correct format",json);
			}	
		} catch (Exception ex) {			
			Commonutility.toWriteConsole("Step -1 : Group Manage Action Exception : "+ex);
			logwrite.logMessage("Step -1 : Group Manage Action Exception : "+ex, "error", groupMgmtAction.class);
		} finally {
					
			jsonFinalObj=null;			 
			json =null; groupMaster = null; uam = null; groupList = null; logwrite = null;locCommonObj = null;lvrEntrydate = null;
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
