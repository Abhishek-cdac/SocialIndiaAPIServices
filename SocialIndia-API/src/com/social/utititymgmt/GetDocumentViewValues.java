package com.social.utititymgmt;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;

public class GetDocumentViewValues  extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	
	JSONObject locObjRecvJson = null;// Receive String to json
	JSONObject locObjRecvdataJson = null;// Receive Data Json
	JSONObject locObjRspdataJson = null;// Response Data Json
	private DocumentUtilitiServices docHibernateUtilService=new DocumentUtilitiDaoServices();
	JSONObject jsonFinalObj=new JSONObject();
	CommonUtils common=new CommonUtils();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	
	public String execute(){
		Log log= new Log();
		String lsvSlQry = null;		
		String ivrservicecode=null;		
		Integer ivrEntryByusrid=0;
		try{
			UserMasterTblVo userdet=new UserMasterTblVo();
			GroupMasterTblVo groupdet=new GroupMasterTblVo();			
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				String imgpath="";
				String mobimgpath="";
				String downloadImagePath="";
				if (ivIsJson) {	
					DocumentManageTblVO documentmng=new DocumentManageTblVO();
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");					
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");					
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }
					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");					
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					int documentId=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "documentId");
					String sdocShareUsrId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "sdocShareUsrId");										
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					
					documentmng.setDocumentId(documentId);
					documentmng=docHibernateUtilService.getdocumentmanagebydocId(documentmng);					
					if(documentmng!=null){
						
					jsonFinalObj.put("dmdocumentId", documentmng.getDocumentId());
					jsonFinalObj.put("dmdocTypId", documentmng.getDocTypId().getIvrBnDOC_TYP_ID());
					jsonFinalObj.put("dmdocTypName", documentmng.getDocTypId().getIvrBnDOC_TYP_NAME());
					jsonFinalObj.put("dmdocFolder", documentmng.getDocFolder());
					jsonFinalObj.put("dmdocSubFolder", documentmng.getDocSubFolder());
					jsonFinalObj.put("dmdocDateFolder", documentmng.getDocDateFolderName());
					jsonFinalObj.put("dmdocFileName", documentmng.getDocFileName());
					jsonFinalObj.put("dmsubject", documentmng.getSubject());
					jsonFinalObj.put("dmdescr", documentmng.getDescr());
					jsonFinalObj.put("dmemailStatus", documentmng.getEmailStatus());
					String docFol="";
					
					if(documentmng.getDocFolder()==1){
						docFol=rb.getString("external.documnet.public.fldr");
						sdocShareUsrId="";
					}else{
						docFol=rb.getString("external.documnet.private.fldr");
						sdocShareUsrId+="/";
					}
					String docsubFol="";
					if(documentmng.getDocSubFolder()==1){
						docsubFol=rb.getString("external.documnet.maintenancedoc.fldr");
					}else{
						docsubFol=rb.getString("external.documnet.generaldoc.fldr");
					}
					
					
					imgpath="/externalPath/document/"+docFol+docsubFol+rb.getString("external.inner.webpath")+sdocShareUsrId+documentmng.getDocDateFolderName()+"/"+documentmng.getDocFileName();
					mobimgpath="/externalPath/document/"+docFol+docsubFol+rb.getString("external.inner.mobilepath")+sdocShareUsrId+documentmng.getDocDateFolderName()+"/"+documentmng.getDocFileName();
					
					downloadImagePath=rb.getString("external.fldr") + "document/"+docFol+docsubFol+rb.getString("external.inner.webpath")+sdocShareUsrId+documentmng.getDocDateFolderName()+"/"+documentmng.getDocFileName();
					//downloadImagePath="/externalPath/document/"+docFol+docsubFol+rb.getString("external.inner.webpath")+sdocShareUsrId+documentmng.getDocDateFolderName()+"/"+documentmng.getDocFileName();
					
					System.out.println("imgpath-----------"+imgpath);
					System.out.println("downloadImagePath-------------"+downloadImagePath);
					
					jsonFinalObj.put("extImagePath", imgpath);
					jsonFinalObj.put("mobileImagePath", mobimgpath);
					jsonFinalObj.put("downloadImagePath", downloadImagePath);
					if(documentmng.getDocSubFolder()==1){
						jsonFinalObj.put("dmmaintenanceId", documentmng.getMaintenanceId().getMaintenanceId());
						
						jsonFinalObj.put("mtmaintenanceId", documentmng.getMaintenanceId().getMaintenanceId());
					jsonFinalObj.put("mtserviceCharge", Float.toString(documentmng.getMaintenanceId().getServiceCharge()));
					jsonFinalObj.put("mtbillDate", common.dateToString(documentmng.getMaintenanceId().getBillDate(),rb.getString("calendar.dateformat")));
					jsonFinalObj.put("mtmunicipalTax", Float.toString(documentmng.getMaintenanceId().getMunicipalTax()));
					jsonFinalObj.put("mtpenalty", Float.toString(documentmng.getMaintenanceId().getPenalty()));
					jsonFinalObj.put("mtwaterCharge", Float.toString(documentmng.getMaintenanceId().getWaterCharge()));
					jsonFinalObj.put("mtnonOccupancyCharge", Float.toString(documentmng.getMaintenanceId().getNonOccupancyCharge()));
					jsonFinalObj.put("mtculturalCharge", Float.toString(documentmng.getMaintenanceId().getCulturalCharge()));
					jsonFinalObj.put("mtsinkingFund", Float.toString(documentmng.getMaintenanceId().getSinkingFund()));
					jsonFinalObj.put("mtrepairFund",Float.toString( documentmng.getMaintenanceId().getRepairFund()));
					jsonFinalObj.put("mtinsuranceCharges", Float.toString(documentmng.getMaintenanceId().getInsuranceCharges()));
					jsonFinalObj.put("mtplayZoneCharge", Float.toString(documentmng.getMaintenanceId().getPlayZoneCharge()));
					jsonFinalObj.put("mtmajorRepairFund", Float.toString(documentmng.getMaintenanceId().getMajorRepairFund()));
					jsonFinalObj.put("mtinterest", Float.toString(documentmng.getMaintenanceId().getInterest()));
					jsonFinalObj.put("total", Float.toString(documentmng.getMaintenanceId().getTotbillamt()));
					jsonFinalObj.put("mtuserId", documentmng.getMaintenanceId().getUserId().getUserId());
					jsonFinalObj.put("dataUploadFrom", documentmng.getMaintenanceId().getDataUploadFrom());
					}
					}
					
					serverResponse(ivrservicecode, "0000","00", "sucess", jsonFinalObj);
					
				}else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6412,"+getText("request.format.notmach")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6412","01","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}
			}else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6412,"+getText("request.values.empty")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6412","01","ER0001",getText("request.values.empty"),locObjRspdataJson);
			}
		}catch (Exception e){
			
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
			System.out.println("statusCode------------"+statusCode+"-----dataJson-------"+dataJson);
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
	public String getIvrservicecode() {
		return ivrservicecode;
	}
	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

}
