package com.siservices.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

public class common extends ActionSupport {
	
	  private HttpServletResponse response;
	  private JSONObject responseMsg = new JSONObject();
	private void serverResponse(String serviceCode, String statusCode, String respCode,
		      String message, JSONObject dataJson) throws Exception {
		    PrintWriter out;
		    try {
		      out = response.getWriter();
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

}
