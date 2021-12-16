package com.social.utititymgmt;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public class DocumentModification extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;	
	private String ivrservicefor;
	CommonUtils common=new CommonUtils();
	private DocumentUtilitiServices docHibernateUtilService=new DocumentUtilitiDaoServices();
	ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
	byte conbytetoarr[]= new byte[1024];
	JSONObject jsonFinalObj=new JSONObject();
	
	public String execute(){
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		//String lsvSlQry = null;
	//	Session locObjsession = null;
		String ivrservicecode = null;
		Integer ivrEntryByusrid=0;
		try {
		//	locObjsession = HibernateUtil.getSession();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams---------------"+ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					DocumentManageTblVO documentmng=new DocumentManageTblVO();
					DocumentShareTblVO documentShare=new DocumentShareTblVO();
					MaintenanceFeeTblVO maintanencefee = new MaintenanceFeeTblVO();
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");					
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");					
					if (ivrEntryByusrid != null) {
					} else {
						ivrEntryByusrid = 0;
					}					
					Integer documentId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "documentId");
					Integer docSubFolder = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docSubFolder");	
					String lvrshareUsrid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docShareUsrId");	
					boolean isupdte = docHibernateUtilService.deleteDocumentManageTbl(documentId, docSubFolder,ivrEntryByusrid,lvrshareUsrid);
					
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode, "0","0", "sucess", locObjRspdataJson);
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6405,"+getText("request.format.notmach")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6405","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6405,"+getText("request.values.empty")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6405","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
			}
		} catch (Exception e) {
			System.out.println("Exception found NewGeneralDocument.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6405, Sorry, an unhandled error occurred", "error", NewGeneralDocument.class);
			serverResponse("SI6405","2","ER0002",getText("catch.error"),locObjRspdataJson);
		} finally {			
			locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
		}
		
		return SUCCESS;
	}
	
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
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


	public String getIvrservicefor() {
		return ivrservicefor;
	}


	public void setIvrservicefor(String ivrservicefor) {
		this.ivrservicefor = ivrservicefor;
	}
	
	

}
