package com.pack.commonapi;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;

public class AppDBQueries extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String execute(){
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		String filepath=rb.getString("external.mobiledbfldr.path");
		String textvalue="";
		Commonutility.TextFilechangeWriting(filepath, textvalue);
		serverResponse("SI39002","1","E39002",getText("App Database file created successfully"));
		} catch (Exception ex) {
			
		}
		return SUCCESS;
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message){
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			String as = responseMsg.toString();
			out.print(as);
			out.close();
		} catch (Exception ex) {
			try{
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.close();
			ex.printStackTrace();
			}catch(Exception e){}finally{}
		}
	}

}
