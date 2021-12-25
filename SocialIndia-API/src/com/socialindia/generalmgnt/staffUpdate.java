package com.socialindia.generalmgnt;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;

public class staffUpdate extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;	
	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		//Session locObjsession = null;
		String ivrservicecode = null, lvrStafimgsrchpth = null;
		byte conbytetoarr[] = new byte[1024];
		int ivrstaffid ;
		String ivr = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		String ivrDecissBlkflag=null; //  1- new create, 2- update, 3- select single[], 4- deActive ,5- delete, 
		try {
						
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					ivrstaffid = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffid");
					ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ivrDecissBlkflag");
					String imageweb = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"imageweb");
					String staffImageFileName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffImageFileName");
					String entryby = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"entryby");
					String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
					lvrStafimgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffimgsrchpath");
					boolean result;
				if (locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// Select Staff
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson = StaffUtility.toSelectStaff(locObjRecvdataJson);						
						serverResponse("SI4003","0","0000","success",locObjRspdataJson);										
				} else if (locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){	// Update Staff				
					StaffMasterTblVo staffeditup = StaffUtility.toUpdtStaff(locObjRecvdataJson);
					if (staffeditup!=null) {											
					locObjRspdataJson = new JSONObject();
					if (ivrstaffid != 0) {
						if (staffImageFileName != null && staffImageFileName!= "") {
							staffImageFileName = staffImageFileName.replaceAll(" ", "_");
							locObjRspdataJson.put("staffimage",staffImageFileName);
							locObjRspdataJson.put("staffid",ivrstaffid);
						} else {
							locObjRspdataJson.put("staffimage", "");
							locObjRspdataJson.put("staffid",ivrstaffid);
							
						}
						if (staffImageFileName != null && staffImageFileName != "") {					
							String pWebImagpath = rb.getString("external.uploadfilestaff.webpath");
							String pMobImgpath = rb.getString("external.uploadfilestaff.mobilepath");
							String delrs = Commonutility.todelete("", pWebImagpath+ivrstaffid+"/");
							String delrs_mob= Commonutility.todelete("", pMobImgpath+ivrstaffid+"/");					  
							Commonutility.toWriteImageMobWebNewUtill(ivrstaffid, staffImageFileName,lvrStafimgsrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, staffcreation.class);
							/*
							//web - org image
							conbytetoarr = Commonutility.toDecodeB64StrtoByary(imageweb);
							String topath = rb.getString("external.uploadfilestaff.webpath")+ ivrstaffid+"/";
							String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, staffImageFileName);															
							//mobile - small image
							String limgSourcePath=rb.getString("external.uploadfilestaff.webpath")+ivrstaffid+"/"+staffImageFileName;						
			 		 		String limgDisPath = rb.getString("external.uploadfilestaff.mobilepath")+ivrstaffid+"/";
			 		 	
			 		 	 	String limgName = FilenameUtils.getBaseName(staffImageFileName);
			 		 	 	String limageFomat = FilenameUtils.getExtension(staffImageFileName);		 	    			 	    	 
			 	    	 	String limageFor = "all";
			 	    	 	int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
			        		int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));	
			        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
			        		*/
						}
							AuditTrial.toWriteAudit(getText("STFAD002"), "STFAD002",Integer.parseInt(entryby));
							serverResponse(ivrservicecode, "00", "R0109", mobiCommon.getMsg("R0109"),locObjRspdataJson);
						} else {
							AuditTrial.toWriteAudit(getText("STFAD007"), "STFAD007",Integer.parseInt(entryby));
							serverResponse("SI4004","01","R0110",mobiCommon.getMsg("R0110"),locObjRecvdataJson);
						}
					} else {
						
						AuditTrial.toWriteAudit(getText("STFAD002"), "STFAD002", Integer.parseInt(entryby));
						serverResponse(ivrservicecode, "02", "R0110", "Staff mobile no. and company name already registered. Exists",locObjRspdataJson);
					}
					
				} else if (locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// deactive
					result = StaffUtility.toDltStaff(locObjRecvdataJson);
					if (result == true) {
							JSONObject finalJson = new JSONObject();
							finalJson.put("resultFlag",result);
							AuditTrial.toWriteAudit(getText("STFAD003"), "STFAD003",Integer.parseInt(entryby));
							serverResponse("SI4005","00","R0112",mobiCommon.getMsg("R0112"),finalJson);
					} else {
							AuditTrial.toWriteAudit(getText("STFAD003"), "STFAD008",Integer.parseInt(entryby));
							serverResponse("SI4005","01","R0113",mobiCommon.getMsg("R0113"),locObjRecvdataJson);
					}
				} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){
						
					
				} else {
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI4003, Request services not correct", "info", staffUpdate.class);
						serverResponse("SI4003","01","R0111",mobiCommon.getMsg("R0111"),locObjRspdataJson);
				}

			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI4003, Request values are empty : " + mobiCommon.getMsg("R0002"), "info", staffUpdate.class);
				serverResponse("SI4003","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception found staffUpdate.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI4003, Sorry, an unhandled error occurred : R0003 - " + mobiCommon.getMsg("R0003"), "error", staffUpdate.class);
			serverResponse("SI4003","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
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
	
	

}
