package com.pack.commonapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Dbversion extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private String dbVersion;
	private String filename;
	private long contentLength;
	private InputStream dbfiledownloadstream;

	private HttpServletResponse response;
	private HttpServletRequest request;
	private JSONObject responseMsg = new JSONObject();

	public String execute() {
		boolean flg = true;		
		String dbversion = null;
		Log log =null;
		try {
			log = new Log();
			if (ivrparams != null && ivrparams.trim().length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println(ivrparams);
				log.logMessage("AF Enter into DB download params :" + ivrparams, "info", Dbversion.class);
				JSONObject ivrparmJson = new JSONObject(ivrparams);
				if (ivrparmJson != null) {
					if (ivrparmJson.has("servicecode")) {
						ivrservicecode = ivrparmJson.getString("servicecode");
					} else {
						flg = false;
					}
					System.out.println("ivrservicecode : "+ivrservicecode);
					JSONObject datajson = new JSONObject();
					if (ivrparmJson.has("data")) {
						datajson = ivrparmJson.getJSONObject("data");
					} else {
						flg = false;
					}					
					if (datajson != null && datajson.has("dbversion")) {
						dbversion = datajson.getString("dbversion");
						System.out.println("dbversion.trim().length() : "+dbversion.trim().length());
						if (dbversion != null && dbversion.trim().length() > 0) {
							flg = true;
						}
					} else {
						flg = false;
					}
					
				}else{
					  flg = false; 
				}
			} else {
				flg = false;
			}
			if (!flg && dbVersion != null && dbVersion.trim().length() > 0) {						
					dbversion = dbVersion;
					flg = true;				
			}
			
			if (flg) {				
				filename = dbversion+getText("Mobi.external.DBversion.path.extension");
				String filepath = getText("external.path")
						+ getText("mobi.devices.info.path")
						+ getText("Mobi.external.DBversion.path") + dbversion
						+ getText("Mobi.external.DBversion.path.extension");
				 log.logMessage("filepath:" + filepath, "info", Dbversion.class);
				File fileToDown = new File(filepath);
				dbfiledownloadstream = new FileInputStream(fileToDown);
				contentLength = fileToDown.length();
			} else {
				filename = "error"+getText("Mobi.external.DBversion.path.extension");
				String filepath = getText("external.path") + getText("mobi.devices.info.path") + getText("Mobi.external.DBversion.path") + "error"
						+ getText("Mobi.external.DBversion.path.extension");
				 log.logMessage("Incorrect request check your version params filepath:" + filepath, "info", Dbversion.class);
			}
		} catch (Exception ex) {			
			System.out.println("Exception found in Dbversion.action : "+ex);
			JSONObject datajs = new JSONObject();
			try {
				serverResponse(getText("db.download"), getText("errorwith.showmessage"), "E0006", "", datajs);
			} catch (Exception e1) {
								
			}			
		}finally{
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
	public String getIvrservicecode() {
		return ivrservicecode;
	}
	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}
	public String getDbVersion() {
		return dbVersion;
	}
	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public InputStream getDbfiledownloadstream() {
		return dbfiledownloadstream;
	}
	public void setDbfiledownloadstream(InputStream dbfiledownloadstream) {
		this.dbfiledownloadstream = dbfiledownloadstream;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public JSONObject getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(JSONObject responseMsg) {
		this.responseMsg = responseMsg;
	}

	
}
