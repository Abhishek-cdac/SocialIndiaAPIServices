package com.socialindia.generalmgnt;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.ManageGSTTblVO;

public class ManageGST extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	Log log = new Log();
	JSONObject jsonFinalObj = new JSONObject();
	private ManageGSTTblVO manageGST = new ManageGSTTblVO();
	generalmgmtDao generalmgmtDao = new generalmgntDaoServices();

	public String execute() {
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode = null;
		try {
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null")
					&& ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"servicecode");
					
					String gstNum = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "gstNum");
					String minGstAmt = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "minGstAmt");
					String minAmt = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "minAmt");
					
					manageGST.setGstNum(gstNum);
					manageGST.setMinGstAmt(minGstAmt);
					manageGST.setMinAmt(minAmt);


					int result = generalmgmtDao.manageGSTInfo(manageGST);
					locObjRspdataJson = new JSONObject();
					if (result != -1) {
						
						String msg = "GST info created successfully.";
						if(result == 1){
							msg = "GST info updated successfully.";
						}
						
						serverResponse(ivrservicecode, "00", "R0107", msg, locObjRspdataJson);
					} else {
						serverResponse(ivrservicecode, "01", "R0108",mobiCommon.getMsg("R0108"), locObjRspdataJson);
					}

				} else {
					// Response call
					serverResponse(ivrservicecode, "01", "R0067",
							mobiCommon.getMsg("R0067"), locObjRspdataJson);
				}
			} else {
				// Response call
				serverResponse(ivrservicecode, "01", "R0002",
						mobiCommon.getMsg("R0002"), locObjRspdataJson);
			}

		} catch (Exception e) {
			try {
				Commonutility
						.toWriteConsole("Exception found .class execute() Method : "
								+ e);
				log.logMessage(
						"Service code : SI2002, Sorry, an unhandled error occurred",
						"info", null);
				serverResponse(ivrservicecode, "02", "R0003",
						mobiCommon.getMsg("R0003"), locObjRspdataJson);
			} catch (Exception ee) {
			} finally {
			}
		} finally {
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
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
