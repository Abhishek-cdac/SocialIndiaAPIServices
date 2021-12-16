package com.social.utititymgmt;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
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
import com.socialindiaservices.vo.DocumentShareTblVO;

public class GetDocumentEditValues extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	
	JSONObject locObjRecvJson = null;// Receive String to json
	JSONObject locObjRecvdataJson = null;// Receive Data Json
	JSONObject locObjRspdataJson = null;// Response Data Json
	private DocumentUtilitiServices docHibernateUtilService=new DocumentUtilitiDaoServices();
	JSONObject jsonFinalObj=new JSONObject();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	CommonUtils common=new CommonUtils();
	
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
				System.out.println("ivrparams---------------"+ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					DocumentManageTblVO documentmng=new DocumentManageTblVO();
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }									
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					int documentId=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "documentId");
					int shareDocument=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "shareDocument");					
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					
					documentmng.setDocumentId(documentId);
					documentmng=docHibernateUtilService.getdocumentmanagebydocId(documentmng);					
					if(documentmng!=null){
					jsonFinalObj.put("dmdocumentId", documentmng.getDocumentId());
					jsonFinalObj.put("dmdocTypId", documentmng.getDocTypId().getIvrBnDOC_TYP_ID());
					jsonFinalObj.put("dmdocTypName", documentmng.getDocTypId().getIvrBnDOC_TYP_NAME());
					
					jsonFinalObj.put("dmdocSubFolder", documentmng.getDocSubFolder());
					jsonFinalObj.put("dmsubject", documentmng.getSubject());
					jsonFinalObj.put("dmdescr", documentmng.getDescr());
					jsonFinalObj.put("dmemailStatus", documentmng.getEmailStatus());
					jsonFinalObj.put("dmdocTypName", documentmng.getDocTypId().getIvrBnDESCR());
					String privpubfold="";
					String maintGenerfold="";
					String webPath="";
					
					//String downloadPath=privpubfold+maintGenerfold+webPath+documentmng.ge;
					jsonFinalObj.put("dmdownloadPath", documentmng.getDocTypId().getIvrBnDESCR());
					
					if(shareDocument>0){
						String shardet="FROM DocumentShareTblVO where documentId.documentId="+documentId+" and userId.userId="+shareDocument;
						DocumentShareTblVO docShareDet=new DocumentShareTblVO();
						docShareDet=docHibernateUtilService.getdocumentsharetblByQuery(shardet);
						jsonFinalObj.put("dcshareid", docShareDet.getDocumentShareId());
						jsonFinalObj.put("dcuserid", docShareDet.getUserId().getUserId());
						jsonFinalObj.put("dcsharefname", docShareDet.getUserId().getFirstName());
						jsonFinalObj.put("dcsharelname", docShareDet.getUserId().getLastName());
						jsonFinalObj.put("dcshareemail", docShareDet.getUserId().getEmailId());
					}
					
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
					log.logMessage("Service code : SI6408,"+getText("request.format.notmach")+" : R0067 - "+mobiCommon.getMsg("R0067"), "info", GetDocumentEditValues.class);
					serverResponse("SI6408","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			}else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6408,"+getText("request.values.empty")+" : R0003 - "+mobiCommon.getMsg("R0003"), "info", GetDocumentEditValues.class);
				serverResponse("SI6408","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
		}catch (Exception e){
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6401, "+mobiCommon.getMsg("R0087") + " : "+e, "error", GetDocumentEditValues.class);
			serverResponse("SI6401","02","R0087",mobiCommon.getMsg("R0087"),locObjRspdataJson);
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
