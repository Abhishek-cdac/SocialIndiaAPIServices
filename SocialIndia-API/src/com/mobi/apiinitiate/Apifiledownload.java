package com.mobi.apiinitiate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionEventListener;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.ValueStack;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Apifiledownload extends ActionSupport implements
ServletRequestAware,ActionInvocation{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ivrparams;
	private String fileVersion;
	private InputStream downloadStream;
	private String filename;
	private HttpServletRequest servletRequest;
	public String execute() {
		Log log = new Log();
		boolean flg = false;
		try {
//			String filePath2 = servletRequest.getSession().getServletContext().getRealPath("/");
//			System.out.println("55Server path:" + filePath2);
//			
//			String filePath = ServletActionContext.getServletContext().getRealPath("/"); 
//			System.out.println("Server path:" + filePath);
//			//Server path:/home/sasikumar/eclipse_workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/socialindiaservices/
//			// #####  /home/sasikumar/eclipse_workspace/socialindiaservices/src/successErrorValues.properties
//			filePath = filePath + "src/successErrorValues.properties";
//			System.out.println("filePath::::" + filePath);
//			File fl = new File(filePath);
//			System.out.println(fl.getAbsolutePath());
//			System.out.println(fl.exists());
//			String contextPath = servletRequest.getContextPath();
//		    System.out.println("Context Path " + contextPath);
//		    contextPath = contextPath + "src/successErrorValues.properties";
//		    System.out.println("contextPath::" + contextPath);
//		    File fls = new File(contextPath);
//		    System.out.println(fls.exists());
			String downloadfilepath = getText("external.path") + getText("mobi.devices.info.path");
			String locparamfilepath = null;
			System.out.println(ivrparams);
			if (Commonutility.checkempty(ivrparams)) {
//				ivrparams = URLDecoder.decode(ivrparams, "UTF-8");
				System.out.println(ivrparams +":::");
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println(ivrparams +":::");
				if (Commonutility.checkempty(ivrparams)) {
					String ad="";
					if (ivrparams.contains("&")) {
						String[] dbFile = ivrparams.split("&");
						if (dbFile.length == 2 ) {
							ivrparams = dbFile[0];
							ad = dbFile[1];
						}
						if (ad.contains("=")) {
							String[] db = ad.split("=");
							if (db.length == 2) {
								fileVersion = db[1];
							}
						}
					}
					System.out.println(ivrparams +":DownloadType::");
					System.out.println(fileVersion +":fileVersion");
					if (ivrparams.equalsIgnoreCase("dbfile")) {
						locparamfilepath = getText("Mobi.external.DBversion.path") + fileVersion + getText("Mobi.external.DBversion.path.extension");
						downloadfilepath = downloadfilepath + locparamfilepath;
						filename = fileVersion + getText("Mobi.external.DBversion.path.extension");
						flg = true;
					} else if (ivrparams.equalsIgnoreCase("msgfile")) {
						locparamfilepath = getText("mobi.external.msg.path") + fileVersion + getText("mobi.msg.valid.lengthrange.extension");
						downloadfilepath = downloadfilepath + locparamfilepath;
						filename = locparamfilepath;
						flg = true;
					} else if (ivrparams.equalsIgnoreCase("reangefile")) {
						locparamfilepath = getText("mobi.external.lengthrange.path") + fileVersion + getText("mobi.msg.valid.lengthrange.extension");
						downloadfilepath = downloadfilepath + locparamfilepath;
						filename = locparamfilepath;
						flg = true;
					}
					if (flg) {
						log.logMessage("downloadfilepath :" + downloadfilepath, "info", Apifiledownload.class);
						File downloadpath = new File(downloadfilepath);
						downloadStream = new FileInputStream(downloadpath);
						System.out.println("----End of file----");
						System.out.println(downloadStream.toString());
					}
					System.out.println("------flg -----:" + flg);
				}
				System.out.println("------*");
			}
			System.out.println("----0");
			
		} catch(Exception ex) {
			ex.printStackTrace();
			log.logMessage("Exception found in :" + ex, "error", Apifiledownload.class);
		} finally {
			System.out.println("ffffff");
		}
		System.out.println("-----##### end");
		if (flg) {
			System.out.println("######return success######");
			return SUCCESS;
		} else {
			System.out.println("######return error######");
			JSONObject locObjRspdataJson = new JSONObject();
			serverResponse("0001",getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			return ERROR;
		}
		
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
			out.print(as);
			out.close();
		} catch (Exception ex) {
			try{
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
			}catch(Exception e){}finally{}
		}
	}
	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}		
	
	public InputStream getDownloadStream() {
		return downloadStream;
	}
	public void setDownloadStream(InputStream downloadStream) {
		this.downloadStream = downloadStream;
	}
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}
	@Override
	public void addPreResultListener(PreResultListener arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object getAction() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ActionContext getInvocationContext() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ActionProxy getProxy() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Result getResult() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getResultCode() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ValueStack getStack() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void init(ActionProxy arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String invoke() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String invokeActionOnly() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isExecuted() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setActionEventListener(ActionEventListener arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setResultCode(String arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
