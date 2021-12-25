package com.social.utititymgmt;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.mobiCommon;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class NewGeneralDocument extends ActionSupport implements Serializable{

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
		
	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		//String lsvSlQry = null;
		String ivrservicecode = null;
		Integer ivrEntryByusrid=0;
		Log logwrite =null;
		try {
			Session session = null;
			Transaction tx = null;
			String topath="";		
			logwrite = new Log();
			Commonutility.toWriteConsole("Step 1 : General Document Create Called [Start]");
			logwrite.logMessage("Step 1 : General Document Create Called [Start]", "info", NewGeneralDocument.class);
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {						
					DocumentManageTblVO documentmng=new DocumentManageTblVO();
					DocumentShareTblVO documentShare=new DocumentShareTblVO();
					UserMasterTblVo userdet=new UserMasterTblVo();
					UserMasterTblVo sharedet=new UserMasterTblVo();
					GroupMasterTblVo groupdet=new GroupMasterTblVo();
					DoctypMasterTblVO docmasdet=new DoctypMasterTblVO();
					NotificationTblVO notificationdet=new NotificationTblVO();					
					locObjRecvJson = new JSONObject(ivrparams);					
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");					
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");					
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }
					
					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");					
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					int docTypId=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docTypId");					
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					docmasdet.setIvrBnDOC_TYP_ID(docTypId);										
					
					JSONArray imageDetail=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageDetail");
					JSONArray fileName = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileName");
					JSONArray docShareId=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docShareId");
					
					int docSubFolder = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docSubFolder");
					String subject = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "subject");
					String descr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "descr");
					int emailStatus = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailStatus");
					
					int docsiz = docShareId.length();					
					Date curDate = new Date();
					
					Commonutility.toWriteConsole("Step 2 : General Document Create fileName : "+fileName.length());
					logwrite.logMessage("Step 2 : General Document Create fileName : "+fileName.length(), "info", NewGeneralDocument.class);
					for (int f = 0; f < fileName.length(); f++) {
						try {
							session = HibernateUtil.getSessionFactory().openSession();
							tx = session.beginTransaction();
							String lvrfilesrchpth = imageDetail.getString(f).toString();
							documentmng = new DocumentManageTblVO();
							documentmng.setUserId(userdet);
							documentmng.setUsrTyp(groupdet);
							documentmng.setDocTypId(docmasdet);
							
							if (docsiz > 0) {
								documentmng.setDocFolder(2);
							} else {
								documentmng.setDocFolder(1);
							}
							String lvrtimestam = Commonutility.getCurrentDate("yyyyMMddHHmmSSS");
							String lvrorgFilename = fileName.get(f).toString();
							if(Commonutility.checkempty(lvrorgFilename)){
								lvrorgFilename = lvrorgFilename.trim().replaceAll(" ", "_");
							}
							String lvrRenamefilename = "";
							if(lvrorgFilename!=null && lvrorgFilename.contains(".")){
								String lvrfilenmae = FilenameUtils.removeExtension(lvrorgFilename);
								String lvrfileexitension = FilenameUtils.getExtension(lvrorgFilename);
								lvrRenamefilename = lvrfilenmae +lvrtimestam+"."+lvrfileexitension;
							} else {
								lvrRenamefilename=lvrorgFilename;
							}
							Commonutility.toWriteConsole("File Name : "+ lvrRenamefilename);
							Commonutility.toWriteConsole("Step 3 : General Document Create New File name : "+lvrRenamefilename);
							logwrite.logMessage("Step 3 : General Document Create New File name : "+lvrRenamefilename, "info", NewGeneralDocument.class);
							
							documentmng.setDocSubFolder(docSubFolder);
							documentmng.setDocDateFolderName(common.getDateYYMMDDFormat());
							documentmng.setDocFileName(lvrRenamefilename);
							documentmng.setSubject(subject);
							documentmng.setDescr(descr);
							documentmng.setEmailStatus(emailStatus);
							documentmng.setStatusFlag(1);
							documentmng.setEntryBy(userdet);
							documentmng.setEntryDatetime(curDate);							
							docHibernateUtilService.insertDocumentManageTbl(documentmng,session);							
							int docsharsiz = docShareId.length();
							if (docsharsiz > 0) {
								for (int dk = 0; dk < docsharsiz; dk++) {
									documentShare = new DocumentShareTblVO();
									notificationdet = new NotificationTblVO();
									sharedet = new UserMasterTblVo();
									int userd = Integer.parseInt(docShareId.get(dk).toString());

									documentShare.setDocumentId(documentmng);
									sharedet.setUserId(userd);
									documentShare.setUserId(sharedet);
									documentShare.setStatus(1);
									documentShare.setEntryBy(userdet);
									documentShare.setEntryDatetime(curDate);
									boolean isshare=docHibernateUtilService.insertDocumentShareTbl(documentShare,session);
							
									if(isshare){
										
										NotificationDao notificationHbm=new NotificationDaoServices();
										AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
										String additionaldata=additionalDatafunc.formAdditionalDataForDocumentTbl(documentmng,documentShare);
										notificationHbm.insertnewNotificationDetails(sharedet, "", 3, 1, documentmng.getDocumentId(), userdet, additionaldata);
		/*									
										notificationdet.setUserId(sharedet);
										String randnumber=common.randomString(Integer.parseInt(rb.getString("Notification.random.string.length")));
										notificationdet.setUniqueId(randnumber);
										notificationdet.setReadStatus(Integer.parseInt(rb.getString("Initial.read.Status")));
										notificationdet.setSentStatus(Integer.parseInt(rb.getString("Initial.sent.Status")));
										notificationdet.setStatusFlag(Integer.parseInt(rb.getString("Initial.statusflag")));
										notificationdet.setDescr(rb.getString("notification.document"));		
										notificationdet.setEntryBy(userdet);
										notificationdet.setEntryDatetime(curDate);
										if(Commonutility.checkempty(rb.getString("notification.reflg.document"))){
											notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.document")));
										} else {
											notificationdet.setTblrefFlag(3);
										}															
										notificationdet.setTblrefID(Commonutility.insertchkintnull(documentmng.getDocumentId()));
										docHibernateUtilService.insertNotificationTbl(notificationdet,session);*/
							
										//conbytetoarr = Commonutility.toDecodeB64StrtoByary(lvrfiledata);
										String docfold=rb.getString("external.documnet.fldr");
										common.createDirIfNotExist(docfold);
										String privatepath=docfold+rb.getString("external.documnet.private.fldr");
										common.createDirIfNotExist(privatepath);
										String genfolder=privatepath+rb.getString("external.documnet.generaldoc.fldr");
										common.createDirIfNotExist(genfolder);
										common.createDirIfNotExist(genfolder+rb.getString("external.inner.webpath"));
										common.createDirIfNotExist(genfolder+rb.getString("external.inner.webpath")+userd);
							
										topath=genfolder+rb.getString("external.inner.webpath")+userd+"/"+common.getDateYYMMDDFormat();
										System.out.println("topath-----------------"+topath);
										//String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, documentmng.getDocFileName());
										
									
										
										Commonutility.toWriteConsole("File Upload Start");	
										File lvrimgSrchPathFileobj = new File(lvrfilesrchpth);// source path
										Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
										File lvrFileToCreate = new File (topath+"/",documentmng.getDocFileName());  
										Commonutility.toWriteConsole("lvrFileToCreate : "+lvrFileToCreate.getAbsolutePath());						
								        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file
									
									
									}
						}
						} else {
							//conbytetoarr=Commonutility.toDecodeB64StrtoByary(lvrfiledata);
							String docfold=rb.getString("external.documnet.fldr");
							common.createDirIfNotExist(docfold);
							String publicpath=docfold+rb.getString("external.documnet.public.fldr");
							common.createDirIfNotExist(publicpath);
							String genfolder=publicpath+rb.getString("external.documnet.generaldoc.fldr");
							common.createDirIfNotExist(genfolder);
							common.createDirIfNotExist(genfolder+rb.getString("external.inner.webpath"));
							
							topath = genfolder + rb.getString("external.inner.webpath") + common.getDateYYMMDDFormat();							
							//String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, documentmng.getDocFileName());
						
							Commonutility.toWriteConsole("Document upload- File Upload Start - Else block");	
							File lvrimgSrchPathFileobj = new File(lvrfilesrchpth);// source path
							Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
							File lvrFileToCreate = new File (topath+"/",documentmng.getDocFileName());  
							Commonutility.toWriteConsole("lvrFileToCreate : "+lvrFileToCreate.getAbsolutePath());						
					        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file
						
						}
						System.out.println("Values----------docSubFolder----"+docSubFolder+"------common.getDateYYMMDDFormat()----"+common.getDateYYMMDDFormat()+"-----fileName.get(f).toString()----"+fileName.get(f).toString()+"----"+subject+"-----"+descr);
						
						Commonutility.toWriteConsole("Step 4 : General Document Create docSubFolder : "+docSubFolder);
						logwrite.logMessage("Step 4 : General Document Create docSubFolder : "+docSubFolder, "info", NewGeneralDocument.class);
						tx.commit();
					}catch (Exception e){
						if(tx!=null){
							tx.rollback();
						}
						Commonutility.toWriteConsole("Step -1 : General Document Create Exception : "+e);
						logwrite.logMessage("Step -1 : General Document Create Exception : "+e, "info", NewGeneralDocument.class);
					}finally{
						if(session!=null){
							session.close();
						}
					}
					}
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("servicecode", ivrservicecode);
					locObjRspdataJson.put("topath", topath);
					
					serverResponse(ivrservicecode,"00","R0068",mobiCommon.getMsg("R0068"),locObjRspdataJson);	
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6403,"+getText("request.format.notmach")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6403","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6403,"+mobiCommon.getMsg("R0002")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6403","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception e) {
			System.out.println("Exception found NewGeneralDocument.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6403, Sorry, an unhandled error occurred : "+mobiCommon.getMsg("R0003"), "error", NewGeneralDocument.class);
			serverResponse("SI6403","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
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
