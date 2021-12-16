package com.social.utititymgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public class GetDocumentFolderDetails extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	CommonUtils common = new CommonUtils();
	private DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	byte conbytetoarr[] = new byte[1024];
	JSONObject jsonFinalObj = new JSONObject();
	
	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		//String lsvSlQry = null;
		//Session locObjsession = null;
		String ivrservicecode = null;
		String ivrCurntusridstr=null;
		Integer ivrEntryByusrid=0;
		uamDao uam = new uamDaoServices();
		try {
			Commonutility.toWriteConsole("Step 1 : get Document Folder Details Called [Start]");
			log.logMessage("Step 1 : get Document Folder List Called [Start]", "info", GetDocumentFolderDetails.class);
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {						
					DocumentManageTblVO documentmng=new DocumentManageTblVO();
					DocumentShareTblVO documentShare=new DocumentShareTblVO();
					DoctypMasterTblVO docmasdet=new DoctypMasterTblVO();
					MaintenanceFeeTblVO maintanencefee = new MaintenanceFeeTblVO();
					
					List<DocumentManageTblVO> documentmngList=new ArrayList<DocumentManageTblVO>();
					List<DocumentShareTblVO> documentShareList=new ArrayList<DocumentShareTblVO>();
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
					if(ivrEntryByusrid!=null){						
					}else{ ivrEntryByusrid=0; }
					
					uamDao lvrUmdo = new uamDaoServices();
					UserMasterTblVo lvrUSm = lvrUmdo.editUser(ivrEntryByusrid);
				
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					Integer docFolder = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docFolder");
					Integer docSubFolder = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docSubFolder");
					String docFolderDate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docFolderDate");
					String docFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docFileName");
					Integer docShareUsrId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docShareUsrId");
					String searchVal=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "searchVal");
					String searchFlag=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "searchFlag");
					JSONArray jArray = new JSONArray();
					JSONArray docIdArray =new JSONArray();
					JSONArray mintTypeArray =new JSONArray();
					JSONArray lvrSocityAry  = new JSONArray();		
					JSONArray lvrdwonpathAry  = new JSONArray();		
					Commonutility.toWriteConsole("Step 2 : get Document Folder Details ivrEntryByusrid : "+ivrEntryByusrid);
					log.logMessage("Step 2 : get Document Folder Details ivrEntryByusrid : "+ivrEntryByusrid, "info", GetDocumentFolderDetails.class);
					if (docFolder!=null && docFolder==0 && docSubFolder!=null && docSubFolder==0 && docShareUsrId!=null && docShareUsrId==0 && docFolderDate!=null && docFolderDate.length()==0){						
						Commonutility.toWriteConsole("Step 3 : get Document Folder Details - Block 1");
						log.logMessage("Step 3 : get Document Folder Details - Block 1", "info", GetDocumentFolderDetails.class);
						
						String qry="";
						String lvrSoctyqyr = "";
						if (lvrUSm.getSocietyId() != null) {
							int lvrsocytid=lvrUSm.getSocietyId().getSocietyId();
							lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"' or userId.societyId = "+null+")";							
						} else {							
							lvrSoctyqyr = "";
						}
						
						if(searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
							qry="FROM DocumentManageTblVO where statusFlag=1  " + lvrSoctyqyr + " and (docFileName like ('%"+searchVal+"%') or subject like ('%"+searchVal+"%'))  group by docFolder";
						} else {
							qry="FROM DocumentManageTblVO where statusFlag=1  " + lvrSoctyqyr + " group by docFolder";
						}											
						documentmngList=docHibernateUtilService.getdocumentmanagegroupBy(qry);						
						DocumentManageTblVO documentobj;
						for (Iterator<DocumentManageTblVO> it = documentmngList.iterator(); it.hasNext();) {
							documentobj=it.next();
							Integer docfolder=documentobj.getDocFolder();
							Integer documentId=documentobj.getDocumentId();
							docIdArray.put(documentId.toString());
							jArray.put(docfolder.toString());
							if(documentobj.getDocSubFolder()==1){
								mintTypeArray.put(documentobj.getMaintenanceId().getDataUploadFrom());
							}else{
								mintTypeArray.put(0);
							}
							
						}
					} else if(docFolder!=null && docFolder>0 && docSubFolder!=null && docSubFolder==0 && docShareUsrId!=null && docShareUsrId==0 && docFolderDate!=null && docFolderDate.length()==0){
						Commonutility.toWriteConsole("Step 3 : get Document Folder Details - Block 2");
						log.logMessage("Step 3 : get Document Folder Details - Block 2", "info", GetDocumentFolderDetails.class);
						String qry="";
						String lvrSoctyqyr = "";
						if(lvrUSm.getSocietyId()!=null){
							int lvrsocytid = lvrUSm.getSocietyId().getSocietyId();
							lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"'  or userId.societyId = "+null+")";							
						} else {
							lvrSoctyqyr= "";						
						}		
						if (searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
							qry="FROM DocumentManageTblVO where docFolder="+docFolder+" and statusFlag=1 "+lvrSoctyqyr+" and (docFileName like ('%"+searchVal+"%') or subject like ('%"+searchVal+"%')) group by docSubFolder";
						} else {
							qry="FROM DocumentManageTblVO where docFolder="+docFolder+" and statusFlag=1 "+lvrSoctyqyr+" group by docSubFolder";
						}
						documentmngList=docHibernateUtilService.getdocumentmanagegroupBy(qry);						
						DocumentManageTblVO documentobj;
						for (Iterator<DocumentManageTblVO> it = documentmngList.iterator(); it.hasNext();) {
							documentobj=it.next();
							Integer docSFolder=documentobj.getDocSubFolder();
							Integer documentId=documentobj.getDocumentId();
							docIdArray.put(documentId.toString());
							jArray.put(docSFolder.toString());
							if(documentobj.getDocSubFolder()==1){
								mintTypeArray.put(documentobj.getMaintenanceId().getDataUploadFrom());
							}else{
								mintTypeArray.put(0);
							}
						}
					} else if (docFolder!=null && docFolder>0 && docSubFolder!=null && docSubFolder>0 && docShareUsrId!=null && docShareUsrId==0 && docFolderDate!=null && docFolderDate.length()==0){						
						Commonutility.toWriteConsole("Step 3 : get Document Folder Details - Block 3");
						log.logMessage("Step 3 : get Document Folder Details - Block 3", "info", GetDocumentFolderDetails.class);
						if (docFolder==1) {
							String lvrSoctyqyr = "";							
							if (lvrUSm.getSocietyId()!=null) {
								int lvrsocytid = lvrUSm.getSocietyId().getSocietyId();
								lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"'  or userId.societyId = "+null+")";							
							} else {
								lvrSoctyqyr= "";						
							}													
							String qry="";
							if(searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
								qry="FROM DocumentManageTblVO where docFolder="+docFolder+" and docSubFolder="+docSubFolder+" and statusFlag=1 "+ lvrSoctyqyr +" and (docFileName like ('%"+searchVal+"%') or subject like ('%"+searchVal+"%')) group by docDateFolderName";
							} else {
								qry="FROM DocumentManageTblVO where docFolder="+docFolder+" and docSubFolder="+docSubFolder+" and statusFlag=1 "+ lvrSoctyqyr +" group by docDateFolderName";
							}
							documentmngList=docHibernateUtilService.getdocumentmanagegroupBy(qry);							
							DocumentManageTblVO documentobj;
							for (Iterator<DocumentManageTblVO> it = documentmngList.iterator(); it.hasNext();) {
								documentobj=it.next();
								String docDFolder=documentobj.getDocDateFolderName();
								Integer documentId=documentobj.getDocumentId();
								docIdArray.put(documentId.toString());
								jArray.put(docDFolder);
								if (documentobj.getDocSubFolder()==1){
									mintTypeArray.put(documentobj.getMaintenanceId().getDataUploadFrom());
								}else{
									mintTypeArray.put(0);
								}
							}
						} else {
							String lvrSoctyqyr = "";							
							if(lvrUSm.getSocietyId()!=null){
								int lvrsocytid = lvrUSm.getSocietyId().getSocietyId();
								lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"'  or userId.societyId = "+null+")";							
							} else {
								lvrSoctyqyr = "";						
							}								
							
							String qry = "";
							if(searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
								qry = "FROM DocumentShareTblVO where documentId.docFolder="+docFolder+" and documentId.docSubFolder="+docSubFolder+" and status=1 "+ lvrSoctyqyr +" and (documentId.docFileName like ('%"+searchVal+"%') or documentId.subject like ('%"+searchVal+"%'))  group by userId.userId";
							} else {
								qry = "FROM DocumentShareTblVO where documentId.docFolder="+docFolder+" and documentId.docSubFolder="+docSubFolder+" and status=1 "+ lvrSoctyqyr +" group by userId.userId";
							}
							documentShareList=docHibernateUtilService.getdocumentshareGroupByQry(qry);							
							DocumentShareTblVO documentShareobj;
							for (Iterator<DocumentShareTblVO> it = documentShareList.iterator(); it.hasNext();) {
								documentShareobj=it.next();
								Integer docSFolder = documentShareobj.getUserId().getUserId();
								if (documentShareobj.getUserId().getSocietyId()!=null) {
									String lvrSocid = documentShareobj.getUserId().getSocietyId().getSocietyUniqyeId();		
									String lvrFltdtls =uam.UserFlatandwings(docSFolder);
									if(Commonutility.checkempty(lvrFltdtls)) {
										lvrFltdtls = " - F.No. : ["+lvrFltdtls+"]";
									} else {
										lvrFltdtls = "";
									}
									lvrSocityAry.put(lvrSocid + lvrFltdtls);
								} else {
									lvrSocityAry.put("Admin");
								}
								
								Integer documentId=documentShareobj.getDocumentId().getDocumentId();
								docIdArray.put(documentId.toString());								
								jArray.put(docSFolder.toString());
								
								if(documentShareobj.getDocumentId().getDocSubFolder()==1){
									mintTypeArray.put(documentShareobj.getDocumentId().getMaintenanceId().getDataUploadFrom());
								}else{
									mintTypeArray.put(0);
								}
								
							}
						}
					} else if (docFolder!=null && docFolder>0 && docSubFolder!=null && docSubFolder>0 && docShareUsrId!=null && docShareUsrId==0 && docFolderDate!=null && docFolderDate.length()>0 ){
						Commonutility.toWriteConsole("Step 3 : get Document Folder Details - Block 4");
						log.logMessage("Step 3 : get Document Folder Details - Block 4", "info", GetDocumentFolderDetails.class);
						String lvrSoctyqyr = "";							
						if(lvrUSm.getSocietyId()!=null){
							int lvrsocytid = lvrUSm.getSocietyId().getSocietyId();
							lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"'  or userId.societyId = "+null+")";							
						} else {
							lvrSoctyqyr = "";						
						}
						
						String qry = "";
						if(searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
							qry = "FROM DocumentManageTblVO where docDateFolderName="+docFolderDate+"  and docSubFolder="+docSubFolder+" and docFolder="+docFolder+" and statusFlag=1 "+ lvrSoctyqyr +" and (docFileName like ('%"+searchVal+"%') or subject like ('%"+searchVal+"%'))";
						} else {
							qry = "FROM DocumentManageTblVO where docDateFolderName="+docFolderDate+"  and docSubFolder="+docSubFolder+" and docFolder="+docFolder+" and statusFlag=1 "+lvrSoctyqyr+" ";
						}
						documentmngList=docHibernateUtilService.getdocumentmanagegroupBy(qry);						
						DocumentManageTblVO documentobj;
						for (Iterator<DocumentManageTblVO> it = documentmngList.iterator(); it.hasNext();) {
							documentobj=it.next();
							String  fileName=documentobj.getDocFileName();
							Integer documentId=documentobj.getDocumentId();
							jArray.put(fileName);
							docIdArray.put(documentId.toString());
							if(documentobj.getDocSubFolder()==1){
								mintTypeArray.put(documentobj.getMaintenanceId().getDataUploadFrom());
							}else{
								mintTypeArray.put(0);
							}
						}
					} else if (docFolder!=null && docFolder>0 && docSubFolder!=null && docSubFolder>0 && docShareUsrId!=null && docShareUsrId>0 && docFolderDate!=null && docFolderDate.length()==0 ){
						Commonutility.toWriteConsole("Step 3 : get Document Folder Details - Block 5");
						log.logMessage("Step 3 : get Document Folder Details - Block 5", "info", GetDocumentFolderDetails.class);
						String lvrSoctyqyr = "";							
						if(lvrUSm.getSocietyId()!=null){
							int lvrsocytid = lvrUSm.getSocietyId().getSocietyId();
							lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"'  or userId.societyId = "+null+")";							
						} else {
							lvrSoctyqyr = "";						
						}
												
						String qry = "";
						if(searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
							qry = "FROM DocumentShareTblVO where documentId.docFolder="+docFolder+" and documentId.docSubFolder="+docSubFolder+" and userId.userId="+docShareUsrId+" and status=1 "+ lvrSoctyqyr +" and (documentId.docFileName like ('%"+searchVal+"%') or documentId.subject like ('%"+searchVal+"%')) group by documentId.docDateFolderName";
						} else {
							qry = "FROM DocumentShareTblVO where documentId.docFolder="+docFolder+" and documentId.docSubFolder="+docSubFolder+" and userId.userId="+docShareUsrId+" and status=1 "+ lvrSoctyqyr +" group by documentId.docDateFolderName";
						}
						documentShareList=docHibernateUtilService.getdocumentshareGroupByQry(qry);
						DocumentShareTblVO documentShareobj;
						for (Iterator<DocumentShareTblVO> it = documentShareList.iterator(); it.hasNext();) {
							documentShareobj=it.next();
							String docSFolder = documentShareobj.getDocumentId().getDocDateFolderName();							
							Integer documentId = documentShareobj.getDocumentId().getDocumentId();
							docIdArray.put(documentId.toString());							
							jArray.put(docSFolder);
							if(documentShareobj.getDocumentId().getDocSubFolder()==1){
								mintTypeArray.put(documentShareobj.getDocumentId().getMaintenanceId().getDataUploadFrom());
							}else{
								mintTypeArray.put(0);
							}							
						}
					} else if (docFolder!=null && docFolder>0 && docSubFolder!=null && docSubFolder>0 && docShareUsrId!=null && docShareUsrId>0 && docFolderDate!=null && docFolderDate.length()>0 ){
						Commonutility.toWriteConsole("Step 3 : get Document Folder Details - Block 6");
						log.logMessage("Step 3 : get Document Folder Details - Block 6", "info", GetDocumentFolderDetails.class);
						String lvrSoctyqyr = "";							
						if(lvrUSm.getSocietyId()!=null){
							int lvrsocytid = lvrUSm.getSocietyId().getSocietyId();
							lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"'  or userId.societyId = "+null+")";							
						} else {
							lvrSoctyqyr = "";						
						}
						
						String qry = null;
						if(searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
							qry = "FROM DocumentShareTblVO where documentId.docFolder="+docFolder+" and documentId.docSubFolder="+docSubFolder+" and userId.userId="+docShareUsrId+" and documentId.docDateFolderName='"+docFolderDate+"' and status=1 " + lvrSoctyqyr + " and (documentId.docFileName like ('%"+searchVal+"%') or documentId.subject like ('%"+searchVal+"%')) group by documentId.docFileName";
						} else {
							qry = "FROM DocumentShareTblVO where documentId.docFolder="+docFolder+" and documentId.docSubFolder="+docSubFolder+" and userId.userId="+docShareUsrId+" and documentId.docDateFolderName='"+docFolderDate+"' and status=1 " + lvrSoctyqyr + " group by documentId.docFileName";
						}
						documentShareList=docHibernateUtilService.getdocumentshareGroupByQry(qry);
						DocumentShareTblVO documentShareobj;
						for (Iterator<DocumentShareTblVO> it = documentShareList.iterator(); it.hasNext();) {
							documentShareobj=it.next();							
							String docSFolder = documentShareobj.getDocumentId().getDocFileName();
							String docsubFol="", docFol = "", sdocShareUsrId = "";							
							
							
							if(docSFolder!=null){
								Integer documentId=documentShareobj.getDocumentId().getDocumentId();
								docIdArray.put(documentId.toString());
								jArray.put(docSFolder);
								if(documentShareobj.getDocumentId().getDocSubFolder()==1){
									mintTypeArray.put(documentShareobj.getDocumentId().getMaintenanceId().getDataUploadFrom());
									docsubFol=rb.getString("external.documnet.maintenancedoc.fldr");
								} else {
									docsubFol=rb.getString("external.documnet.generaldoc.fldr");
									mintTypeArray.put(0);
								}
								if(documentShareobj.getDocumentId().getDocFolder()==1){
									docFol=rb.getString("external.documnet.public.fldr");
									sdocShareUsrId="";
								}else{
									docFol=rb.getString("external.documnet.private.fldr");
									sdocShareUsrId = documentShareobj.getUserId().getUserId()+"/";
								}
								
								String lvrdpth = rb.getString("external.fldr") + "document/"+docFol+docsubFol+rb.getString("external.inner.webpath")+sdocShareUsrId+documentShareobj.getDocumentId().getDocDateFolderName()+"/"+documentShareobj.getDocumentId().getDocFileName();
								lvrdwonpathAry.put(lvrdpth);
							}
							
						}
					} else if (docFolder!=null && docFolder>0 && docSubFolder!=null && docSubFolder>0 && docShareUsrId!=null && docShareUsrId==0 && docFolderDate!=null && docFolderDate.length()>0 ){
						Commonutility.toWriteConsole("Step 3 : get Document Folder Details - Block 7");
						log.logMessage("Step 3 : get Document Folder Details - Block 7", "info", GetDocumentFolderDetails.class);
						String lvrSoctyqyr = "";							
						if(lvrUSm.getSocietyId()!=null){
							int lvrsocytid = lvrUSm.getSocietyId().getSocietyId();
							lvrSoctyqyr = "and (userId.societyId.societyId = '"+lvrsocytid+"'  or userId.societyId = "+null+")";							
						} else {
							lvrSoctyqyr = "";						
						}
						
						String qry = null;
						if(searchFlag!=null && searchFlag.equalsIgnoreCase("true")){
							qry = "FROM DocumentManageTblVO where docFolder="+docFolder+" and docSubFolder="+docSubFolder+" and docDateFolderName='"+docFolderDate+"' and statusFlag=1 " + lvrSoctyqyr + " and (docFileName like ('%"+searchVal+"%') or subject like ('%"+searchVal+"%')) group by docFileName";
						} else {
							qry = "FROM DocumentManageTblVO where docFolder="+docFolder+" and docSubFolder="+docSubFolder+" and docDateFolderName='"+docFolderDate+"' and statusFlag=1 " + lvrSoctyqyr + " group by docFileName";
						}
						documentmngList=docHibernateUtilService.getdocumentmanagegroupBy(qry);						
						DocumentManageTblVO documentobj;
						for (Iterator<DocumentManageTblVO> it = documentmngList.iterator(); it.hasNext();) {
							documentobj=it.next();
							String docFile=documentobj.getDocFileName();
							Integer documentId=documentobj.getDocumentId();
							docIdArray.put(documentId.toString());
							jArray.put(docFile);
							if(documentobj.getDocSubFolder()==1){
								mintTypeArray.put(documentobj.getMaintenanceId().getDataUploadFrom());
							}else{
								mintTypeArray.put(0);
							}
							
					}
					}
					Commonutility.toWriteConsole("mintTypeArray-----------------"+mintTypeArray.toString());
					Commonutility.toWriteConsole("docIdArray-----------------"+docIdArray.toString());
					Commonutility.toWriteConsole("jArray-----------------"+jArray.toString());
					Commonutility.toWriteConsole("lvrSocityAry-----------------"+lvrSocityAry.toString());
					Commonutility.toWriteConsole("lvrdwonpathAry-----------------"+lvrdwonpathAry.toString());
					jsonFinalObj.put("foldernames", jArray);
					jsonFinalObj.put("documentId", docIdArray);
					jsonFinalObj.put("maintenanceType", mintTypeArray);
					jsonFinalObj.put("societyuniqidary", lvrSocityAry);
					jsonFinalObj.put("downloadpathary", lvrdwonpathAry);
					Commonutility.toWriteConsole("Step 4 : get Document Folder Details - [End]");
					log.logMessage("Step 4 : get Document Folder Details - [End]", "info", GetDocumentFolderDetails.class);
					Commonutility.toWriteConsole("ivrservicecode---------------"+ivrservicecode);
					serverResponse(ivrservicecode, "00","0000", "sucesss", jsonFinalObj);
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6401,"+mobiCommon.getMsg("R0067")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6401","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				Commonutility.toWriteConsole("Step 2 : get Document Folder Details request format in not correct.");
				log.logMessage("Step 2 : get Document Folder Details request format in not correct.", "info", GetDocumentFolderDetails.class);
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6401,"+mobiCommon.getMsg("R0003")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6401","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception found NewGeneralDocument.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6401, "+mobiCommon.getMsg("R0087") + " : "+e, "error", NewGeneralDocument.class);
			serverResponse("SI6401","02","R0087",mobiCommon.getMsg("R0087"),locObjRspdataJson);
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
	public String getIvrservicefor() {
		return ivrservicefor;
	}
	public void setIvrservicefor(String ivrservicefor) {
		this.ivrservicefor = ivrservicefor;
	}
	
}
