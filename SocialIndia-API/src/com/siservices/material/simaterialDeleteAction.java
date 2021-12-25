package com.siservices.material;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class simaterialDeleteAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	materialDao material=new materialDaoServices();
	CommonUtils comutil=new CommonUtilsServices();
	JSONObject jsonFinalObj=new JSONObject();
	CommonUtilsDao common=new CommonUtilsDao();	
	Log log=new Log();	
	boolean flag = true;
	
	public String execute() throws JSONException{		
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		try{
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				int materialId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialId");				
				boolean result=material.materialDelete(materialId);		
				if (result==true) {			
					serverResponse(servicecode,"00", "R0193", mobiCommon.getMsg("R0193"), jsonFinalObj);
				} else {
					serverResponse(servicecode,"01", "R0194", mobiCommon.getMsg("R0194"), jsonFinalObj);
				}
			} else {
				json=new JSONObject();
				serverResponse("SI0007","01","R0067",mobiCommon.getMsg("R0067"),json);
			}
		} catch(Exception ex){
			try {
			log.logMessage("Service code : R0003, Sorry, siCommitteeCreationAction an unhandled error occurred", "info", simaterialDeleteAction.class);
			serverResponse("SI0007","02","R0003",mobiCommon.getMsg("R0003"),json);
			} catch (Exception ee){}
		} finally {
			
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
