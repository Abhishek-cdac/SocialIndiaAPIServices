package com.social.utititymgmt;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
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

public class EditGeneralDocument  extends ActionSupport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;	
	private String ivrservicefor;
	CommonUtils common=new CommonUtils();
	private DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	byte conbytetoarr[] = new byte[1024];
		
	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json		
		String ivrservicecode = null;
		Integer ivrEntryByusrid=0;		
		Session session = null;
		Transaction tx = null;
		boolean isupdate=false;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();						
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {						
					DocumentManageTblVO documentmng = new DocumentManageTblVO();
					DocumentShareTblVO documentShare = new DocumentShareTblVO();
					UserMasterTblVo userdet = new UserMasterTblVo();
					UserMasterTblVo sharedet = new UserMasterTblVo();
					GroupMasterTblVo groupdet = new GroupMasterTblVo();
					DoctypMasterTblVO docmasdet = new DoctypMasterTblVO();
					NotificationTblVO notificationdet = new NotificationTblVO();	
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");					
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");					
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }																		
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					int docTypId=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docTypId");					
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					//userdet.setGroupCode(groupdet);
					docmasdet.setIvrBnDOC_TYP_ID(docTypId);					
					
					String imageDetail = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageDetail");
					String fileName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileName");
					JSONArray docShareId=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docShareId");
					int docSubFolder=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docSubFolder");
					int documentId=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "documentId");
					String subject=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "subject");
					String descr=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "descr");
					int emailStatus=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailStatus");
					
					int docsiz=docShareId.length();
					Date curDate=new Date();
					String imagefile="";
					String filename="";
					documentmng=new DocumentManageTblVO();												
					documentmng.setDocumentId(documentId);						
					DocumentManageTblVO pastDacmngData=new DocumentManageTblVO();
					List<DocumentShareTblVO> pastDocStareData=new ArrayList<DocumentShareTblVO>();
					pastDacmngData = docHibernateUtilService.getdocumentmanagebydocId(documentmng);
					
					String oldpath="";
					String oldfile="";
					String doctempPath="";
					if(fileName!=null){
						fileName = fileName.trim().replaceAll(" ", "_");
						imagefile = imageDetail;
						String lvrExtension = FilenameUtils.getExtension(fileName);
						String lvrtimestam = Commonutility.getCurrentDate("yyyyMMddHHmmSSS");
						String lvrNameonly =  FilenameUtils.removeExtension(fileName);
						fileName =lvrNameonly+lvrtimestam+"."+lvrExtension;						
						filename = fileName;
						
					}
					Commonutility.toWriteConsole("getDocFolder() : 1 - Public, 2 - Private");
					Commonutility.toWriteConsole("getDocFolder : "+pastDacmngData.getDocFolder());
					Commonutility.toWriteConsole("DocDateFolderName : "+pastDacmngData.getDocDateFolderName());
					if (pastDacmngData!=null && pastDacmngData.getDocFolder() == 2) {
						Commonutility.toWriteConsole("Step : Private document block enter.");
						String sql = "From DocumentShareTblVO where documentId.documentId="+pastDacmngData.getDocumentId();
						pastDocStareData = docHibernateUtilService.getdocumentsharetblByDocId(sql);
						String commonNewFolder = common.getDateYYMMDDFormat();		
						commonNewFolder = pastDacmngData.getDocDateFolderName();
						if (docsiz > 0) {
							if (filename!=null && !filename.equalsIgnoreCase("")) {
								conbytetoarr = Commonutility.toDecodeB64StrtoByary(imagefile);
								doctempPath = rb.getString("external.documnet.fldr")+"temp";
								oldfile = filename;
								oldpath = doctempPath;
								String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, doctempPath, filename);							
							} else {
								String pastoneuserId="";
								if(pastDocStareData!=null && pastDocStareData.size()>0){
									DocumentShareTblVO docshareobj;
									for(Iterator<DocumentShareTblVO> it = pastDocStareData.iterator();it.hasNext();){
										docshareobj=it.next();
										pastoneuserId = Integer.toString(docshareobj.getUserId().getUserId());
										break;
									}
								}								
								oldpath = rb.getString("external.documnet.fldr") + rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastoneuserId+"/"+pastDacmngData.getDocDateFolderName();
								oldfile = pastDacmngData.getDocFileName();								
								doctempPath = rb.getString("external.documnet.fldr") + "temp";
								Commonutility.toWriteConsole("oldpath : "+oldpath);
								Commonutility.toWriteConsole("oldfile : "+oldfile);
								Commonutility.toWriteConsole("doctempPath : "+doctempPath);
								common.createDirIfNotExist(doctempPath);
								Commonutility.toFileMoving(oldpath, doctempPath, oldfile, "NO");
								oldpath=doctempPath;
							}													
							documentmng.setUserId(userdet);
							documentmng.setUsrTyp(groupdet);
							documentmng.setDocTypId(docmasdet);
							documentmng.setDocFolder(1);
							documentmng.setDocSubFolder(2);
							documentmng.setDocDateFolderName(pastDacmngData.getDocDateFolderName());
							documentmng.setDocFileName(filename);
							documentmng.setSubject(subject);
							documentmng.setDescr(descr);
							documentmng.setEmailStatus(emailStatus);
							documentmng.setStatusFlag(1);
							documentmng.setEntryBy(userdet);
							documentmng.setEntryDatetime(curDate);
							isupdate = docHibernateUtilService.updateDocumentManageTbl(documentmng,session);							
							for (int dk = 0; dk < docsiz; dk++) {
								documentShare = new DocumentShareTblVO();
								notificationdet = new NotificationTblVO();
								sharedet = new UserMasterTblVo();
								int userd=Integer.parseInt(docShareId.get(dk).toString());								
								documentShare.setDocumentId(documentmng);
								sharedet.setUserId(userd);
								documentShare.setUserId(sharedet);
								documentShare.setStatus(1);
								documentShare.setEntryBy(userdet);
								documentShare.setEntryDatetime(curDate);
								boolean isShare = docHibernateUtilService.insertDocumentShareTbl(documentShare,session);
								if(isShare){
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
								isupdate=docHibernateUtilService.insertNotificationTbl(notificationdet,session);
								}
								String topath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+userd+"/"+commonNewFolder;
								System.out.println("topath-----------------"+topath);
								Commonutility.toFileMoving(oldpath, topath, oldfile, "No");
							}
							if(pastDocStareData!=null && pastDocStareData.size()>0){
								DocumentShareTblVO docshareobj;
								for(Iterator<DocumentShareTblVO> it=pastDocStareData.iterator();it.hasNext();){
									docshareobj=it.next();
									String todelfolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+docshareobj.getUserId().getUserId()+"/"+pastDacmngData.getDocDateFolderName();
									String todelfile=pastDacmngData.getDocFileName();
									common.deleteIfFileExist(todelfolder, todelfile);
									String tocrefolder = rb.getString("external.documnet.fldr") + rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+docshareobj.getUserId().getUserId()+"/"+commonNewFolder;
									Commonutility.toFileMoving(oldpath, tocrefolder, oldfile, "No");
									
								}
							}													
							common.deleteIfFileExist(oldpath,oldfile);
						} else {
							if(filename!=null && !filename.equalsIgnoreCase("")){
								conbytetoarr=Commonutility.toDecodeB64StrtoByary(imagefile);
								doctempPath=rb.getString("external.documnet.fldr")+"temp";
								oldfile=filename;
								oldpath=doctempPath;
								String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, doctempPath, filename);							
							}else{
								String pastoneuserId="";
								if(pastDocStareData!=null && pastDocStareData.size()>0){
									DocumentShareTblVO docshareobj;
									for(Iterator<DocumentShareTblVO> it=pastDocStareData.iterator();it.hasNext();){
										docshareobj=it.next();
										pastoneuserId=docshareobj.getUserId().toString();
										break;
									}
								}
								
								oldpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastoneuserId+"/"+pastDacmngData.getDocDateFolderName();
								oldfile=pastDacmngData.getDocFileName();
								doctempPath=rb.getString("external.documnet.fldr")+"temp";
								common.createDirIfNotExist(doctempPath);
								Commonutility.toFileMoving(oldpath, doctempPath, oldfile, "NO");
								oldpath=doctempPath;
							}
													
							
							documentmng.setUserId(userdet);
							documentmng.setUsrTyp(groupdet);
							documentmng.setDocTypId(docmasdet);
							documentmng.setDocFolder(1);
							documentmng.setDocSubFolder(2);
							documentmng.setDocDateFolderName(commonNewFolder);
							documentmng.setDocFileName(filename);
							documentmng.setSubject(subject);
							documentmng.setDescr(descr);
							documentmng.setEmailStatus(emailStatus);
							documentmng.setStatusFlag(1);
							documentmng.setEntryBy(userdet);
							documentmng.setEntryDatetime(curDate);
							isupdate=docHibernateUtilService.updateDocumentManageTbl(documentmng,session);
							
							
							if(pastDocStareData!=null && pastDocStareData.size()>0){
								DocumentShareTblVO docshareobj;
								for(Iterator<DocumentShareTblVO> it=pastDocStareData.iterator();it.hasNext();){
									docshareobj=it.next();
									String todelfolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+docshareobj.getUserId()+"/"+pastDacmngData.getDocDateFolderName();
									String todelfile=pastDacmngData.getDocFileName();
									common.deleteIfFileExist(todelfolder, todelfile);
									String tocrefolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+docshareobj.getUserId()+"/"+commonNewFolder;
									Commonutility.toFileMoving(oldpath, tocrefolder, oldfile, "No");
									
								}
							}
						}
						
						} else { // Public Document Edit
							Commonutility.toWriteConsole("Step : Public document updateblock enter.");
							String commonNewFolder=common.getDateYYMMDDFormat();
							commonNewFolder = pastDacmngData.getDocDateFolderName();
							Commonutility.toWriteConsole("Step : Document Date Folder : "+commonNewFolder);
							if(docsiz>0){
								if(filename!=null && !filename.equalsIgnoreCase("")){
									conbytetoarr=Commonutility.toDecodeB64StrtoByary(imagefile);
									doctempPath=rb.getString("external.documnet.fldr")+"temp";
									oldfile=filename;
									oldpath=doctempPath;
									String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, doctempPath, filename);
								
								}else{
									oldpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastDacmngData.getDocDateFolderName();
									System.out.println("oldpath-----------"+oldpath);
									oldfile=pastDacmngData.getDocFileName();
									doctempPath=rb.getString("external.documnet.fldr")+"temp";
									common.createDirIfNotExist(doctempPath);
									Commonutility.toFileMoving(oldpath, doctempPath, oldfile, "NO");
									oldpath=doctempPath;
								}
								
								String todelfolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastDacmngData.getDocDateFolderName();
								String todelfile=pastDacmngData.getDocFileName();
								common.deleteIfFileExist(todelfolder, todelfile);
								
								documentmng.setUserId(userdet);
								documentmng.setUsrTyp(groupdet);
								documentmng.setDocTypId(docmasdet);
								documentmng.setDocFolder(1);
								documentmng.setDocSubFolder(2);
								documentmng.setDocDateFolderName(commonNewFolder);
								documentmng.setDocFileName(filename);
								documentmng.setSubject(subject);
								documentmng.setDescr(descr);
								documentmng.setEmailStatus(emailStatus);
								documentmng.setStatusFlag(1);
								documentmng.setEntryBy(userdet);
								documentmng.setEntryDatetime(curDate);
								isupdate=docHibernateUtilService.updateDocumentManageTbl(documentmng,session);
								DocumentManageTblVO insDocMng=new DocumentManageTblVO();
								insDocMng=docHibernateUtilService.getdocumentmanagebydocId(documentmng);
								insDocMng.setUserId(userdet);
								insDocMng.setUsrTyp(groupdet);
								insDocMng.setDocFolder(2);
								insDocMng.setEntryDatetime(curDate);
								insDocMng.setStatusFlag(1);
								isupdate=docHibernateUtilService.insertDocumentManageTbl(insDocMng,session);
								for(int dk=0;dk<docsiz;dk++){
									documentShare = new DocumentShareTblVO();
									notificationdet = new NotificationTblVO();
									sharedet=new UserMasterTblVo();
									int userd=Integer.parseInt(docShareId.get(dk).toString());
									
									documentShare.setDocumentId(insDocMng);
									sharedet.setUserId(userd);
									documentShare.setUserId(sharedet);
									documentShare.setStatus(1);
									documentShare.setEntryBy(userdet);
									documentShare.setEntryDatetime(curDate);
									
									boolean isShare=docHibernateUtilService.insertDocumentShareTbl(documentShare,session);
									if(isShare){
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
									isupdate=docHibernateUtilService.insertNotificationTbl(notificationdet,session);
									
									System.out.println("------------Inserted into Notification Table-----------------");
									}
									conbytetoarr=Commonutility.toDecodeB64StrtoByary(imagefile);
									String topath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+userd+"/"+commonNewFolder;
									System.out.println("topath-----------------"+topath);
									Commonutility.toFileMoving(oldpath, topath, oldfile, "No");
								}
								
								String destpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+commonNewFolder;
								common.createDirIfNotExist(doctempPath);
								Commonutility.toFileMoving(oldpath, destpath, oldfile, "No");
								common.deleteIfFileExist(oldpath, oldfile);
																
							}else{
								if(filename!=null && !filename.equalsIgnoreCase("")){
									conbytetoarr=Commonutility.toDecodeB64StrtoByary(imagefile);
									doctempPath=rb.getString("external.documnet.fldr")+"temp";
									oldfile=filename;
									oldpath=doctempPath;
									String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, doctempPath, filename);
								
								}else{
									oldpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastDacmngData.getDocDateFolderName();
									oldfile=pastDacmngData.getDocFileName();
									doctempPath=rb.getString("external.documnet.fldr")+"temp";
									common.createDirIfNotExist(doctempPath);
									Commonutility.toFileMoving(oldpath, doctempPath, oldfile, "NO");
									oldpath=doctempPath;
								}
								String todelfolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastDacmngData.getDocDateFolderName();
								String todelfile=pastDacmngData.getDocFileName();
								common.deleteIfFileExist(todelfolder, todelfile);
								
								
								documentmng.setUserId(userdet);
								documentmng.setUsrTyp(groupdet);
								documentmng.setDocTypId(docmasdet);
								documentmng.setDocFolder(1);
								documentmng.setDocSubFolder(2);
								documentmng.setDocDateFolderName(commonNewFolder);
								documentmng.setDocFileName(filename);
								documentmng.setSubject(subject);
								documentmng.setDescr(descr);
								documentmng.setEmailStatus(emailStatus);
								documentmng.setStatusFlag(1);
								documentmng.setEntryBy(userdet);
								documentmng.setEntryDatetime(curDate);
								isupdate=docHibernateUtilService.updateDocumentManageTbl(documentmng,session);
								String destpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+commonNewFolder;
								common.createDirIfNotExist(destpath);
								Commonutility.toFileMoving(oldpath, destpath, oldfile, "No");
								common.deleteIfFileExist(oldpath, oldfile);
							}																
						}											
					System.out.println("ivrservicecode---------------"+ivrservicecode);
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("servicecode", ivrservicecode);
					serverResponse(ivrservicecode,"00","R0079",mobiCommon.getMsg("R0079"),locObjRspdataJson);	
					if(isupdate){
						tx.commit();
					}
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6409, Resp : R0067 - "+mobiCommon.getMsg("R0067")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6409","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6409,"+getText("request.values.empty")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6409","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("Exception found NewGeneralDocument.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6409, Sorry, an unhandled error occurred : R0083 - "+mobiCommon.getMsg("R0083"), "error", NewGeneralDocument.class);
			serverResponse("SI6409","02","R0083",mobiCommon.getMsg("R0083"),locObjRspdataJson);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
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