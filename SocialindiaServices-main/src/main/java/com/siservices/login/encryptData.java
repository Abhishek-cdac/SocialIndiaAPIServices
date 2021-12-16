package com.siservices.login;

import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jettison.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;

public class encryptData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String ivrparams;

	public String execute() {
		JSONObject locrecvejson = null;
		try {
			Commonutility.toWriteConsole("encryptData Receive Param : " + ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {
				String jsonTextFinal = EncDecrypt.encrypt(ivrparams);
				jsonTextFinal = "ivrparams="+ URLEncoder.encode(jsonTextFinal) + "";

				locrecvejson = new JSONObject();
				locrecvejson.put("ivrparams", jsonTextFinal);

				serverResponse("encryptData", "0000", "0", "sucess",locrecvejson);
			} else {
				locrecvejson = new JSONObject();
				serverResponse("encryptData", "1", "E0001","Request values not correct format", locrecvejson);
			}
		} catch (Exception ex) {
			try {
				Commonutility.toWriteConsole("Step -1 : Exception Found : "+ ex);
				locrecvejson = new JSONObject();
				serverResponse("encryptData", "1", "E0001",ex.getMessage(), locrecvejson);
			} catch (Exception e) {
			}
		} finally {
			Commonutility.toWriteConsole("Step 4 : encryptData Service [End]. : ");
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
