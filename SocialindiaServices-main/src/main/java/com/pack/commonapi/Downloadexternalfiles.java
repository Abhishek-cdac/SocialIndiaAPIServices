package com.pack.commonapi;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;



import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Downloadexternalfiles extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private InputStream dbfiledownloadstream;
	private String filename;
	public String execute() {
		Log log = null;
		JSONObject locObjRecvJson = null;//Receive String to json
		//JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json		
		String ivrservicecode=null,ivrdocfilepath=null,ivrdocfilename=null;				
		File fileToDown = null;
		try {
		    	log = new Log();
		    	log.logMessage("Step 1 : Downlod File [Start] Request Received.", "info", Downloadexternalfiles.class);
				if(ivrparams!= null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
					
					ivrparams = EncDecrypt.decrypt(ivrparams);
					boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
					if (ivIsJson) {
						locObjRecvJson = new JSONObject(ivrparams);						
						ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");				
						ivrdocfilepath = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"docfilepath");
						ivrdocfilename = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"docfilename");
						log.logMessage("Step 2 : Request File Name : "+ivrdocfilename, "info", Downloadexternalfiles.class);
						log.logMessage("Step 3 : Request File Path : "+ivrdocfilepath, "info", Downloadexternalfiles.class);
						String filepath = ivrdocfilepath;						
						fileToDown = new File(filepath);
						if(fileToDown.exists() && fileToDown.isFile()) {
							filename = ivrdocfilename;
							dbfiledownloadstream = new FileInputStream(fileToDown);
							log.logMessage("Step 4 : Request File Will Download.", "info", Downloadexternalfiles.class);
						} else {
							log.logMessage("Step 4 : Request File Not Found Error file Will Download.", "info", Downloadexternalfiles.class);
							filename = "Nofile.txt";
							String filepatherror=getText("external.fldr")+getText("external.errorfile")+"Errfile.txt";
							fileToDown = new File(filepatherror);
							if(fileToDown!=null && !fileToDown.exists()){
								log.logMessage("Step 5 : Error File Not Found. New Error File Created Implicitly.", "info", Downloadexternalfiles.class);
								Commonutility.crdDir(filepatherror);
								String locErro="File Not Found. Sorry For Inconvience.";
								byte locBytt [] = locErro.getBytes();
								String lvrWrtrst = Commonutility.toByteArraytoWriteFiles(locBytt, filepatherror, "");
								if(lvrWrtrst != null && lvrWrtrst.equalsIgnoreCase("success")){
									locBytt = null;locErro = null;
								}else{
									locBytt = null;locErro = null;
								}								
							}else{
								log.logMessage("Step 5 : Error File Found. It Will Download.", "info", Downloadexternalfiles.class);
							}
							dbfiledownloadstream = new FileInputStream(fileToDown);
							
						}						
					} else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI12015,"+getText("request.format.notmach")+"", "info", Downloadexternalfiles.class);
						serverResponse("SI12015","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
					}					
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI12015,"+getText("request.values.empty")+"", "info", Downloadexternalfiles.class);
					serverResponse("SI12015","1","EF0001",getText("request.values.empty"),locObjRspdataJson);
				}
				log.logMessage("Step 6 : Download File Service End.", "info", Downloadexternalfiles.class);
		    } catch(Exception Ex){
		    	log.logMessage("Step -1: Exception Found in Downloadexternalfiles.class : "+Ex, "info", Downloadexternalfiles.class);
		    	System.out.println("Exception Found in Downloadexternalfiles.class : "+Ex);
		    	try {
		    		filename = "Nofile.txt";
		    		String filepatherror=getText("external.fldr")+getText("external.errorfile")+"Errfile.txt";
		    		fileToDown = new File(filepatherror);
		    		dbfiledownloadstream = new FileInputStream(fileToDown);
				}catch (FileNotFoundException e) {	}
		    }finally{
		    	log = null;if(fileToDown!=null){fileToDown = null;}
		    }
		return SUCCESS;
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
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
			responseMsg.put("data", dataJson);
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

	public InputStream getDbfiledownloadstream() {
		return dbfiledownloadstream;
	}

	public void setDbfiledownloadstream(InputStream dbfiledownloadstream) {
		this.dbfiledownloadstream = dbfiledownloadstream;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	
}
