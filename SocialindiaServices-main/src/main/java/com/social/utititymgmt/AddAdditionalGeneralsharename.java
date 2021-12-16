package com.social.utititymgmt;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

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

public class AddAdditionalGeneralsharename   extends ActionSupport implements Serializable{

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
		
		Session session = null;
		Transaction tx = null;
		boolean isupdate=false;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			System.out.println("ivrparams----------------"+ivrparams);
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams---------------"+ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					System.out.println("true------------------");
					DocumentManageTblVO documentmng=new DocumentManageTblVO();
					DocumentShareTblVO documentShare=new DocumentShareTblVO();
					UserMasterTblVo userdet=new UserMasterTblVo();
					UserMasterTblVo sharedet=new UserMasterTblVo();
					GroupMasterTblVo groupdet=new GroupMasterTblVo();
					DoctypMasterTblVO docmasdet=new DoctypMasterTblVO();
					NotificationTblVO notificationdet=new NotificationTblVO();
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					System.out.println("ivrservicecode----------------"+ivrservicecode);
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
					System.out.println("ivrEntryByusrid---------------------"+ivrEntryByusrid);
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }
					
					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					System.out.println("locObjRecvdataJson----------------------"+locObjRecvdataJson);
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					int documentId= (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "documentId");
					int sdocShareUsrId=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "sdocShareUsrId");
					System.out.println("documentId-------------"+documentId);
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					
					JSONArray docShareId=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docShareId");
					
					int docsiz=docShareId.length();
					System.out.println("docsiz------------"+docsiz);
				Date curDate=new Date();
				String imagefile="";
				String filename="";
			
						documentmng=new DocumentManageTblVO();
						System.out.println("userdet----------------"+userdet);
						System.out.println("documentId--------------"+documentId);
						
						documentmng.setDocumentId(documentId);
						
						DocumentManageTblVO pastDacmngData=new DocumentManageTblVO();
						List<DocumentShareTblVO> pastDocStareData=new ArrayList<DocumentShareTblVO>();
						DocumentShareTblVO pastdocShareView=new DocumentShareTblVO();
						pastDacmngData=docHibernateUtilService.getdocumentmanagebydocId(documentmng);
						docmasdet=pastDacmngData.getDocTypId();
						String sql="From DocumentShareTblVO where documentId.documentId="+pastDacmngData.getDocumentId();
						String oldpath="";
						String oldfile="";
						String doctempPath="";
						
						if(pastDacmngData!=null && pastDacmngData.getDocFolder()==2){
						pastDocStareData=docHibernateUtilService.getdocumentsharetblByDocId(sql);
						
						String commonNewFolder=common.getDateYYMMDDFormat();
						sql="From DocumentShareTblVO where documentId.documentId="+pastDacmngData.getDocumentId()+"and userId="+sdocShareUsrId;
						pastdocShareView=docHibernateUtilService.getdocumentsharetblByQuery(sql);
						String pastoneuserId="";
						pastoneuserId=Integer.toString(pastdocShareView.getUserId().getUserId());
						
						if(docsiz>0){
								oldpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastoneuserId+"/"+pastDacmngData.getDocDateFolderName();
								oldfile=pastDacmngData.getDocFileName();
								doctempPath=rb.getString("external.documnet.fldr")+"temp";
								common.createDirIfNotExist(doctempPath);
								Commonutility.toFileMoving(oldpath, doctempPath, oldfile, "NO");
								oldpath=doctempPath;
							
							
							documentmng.setUserId(userdet);
							documentmng.setUsrTyp(groupdet);
							documentmng.setDocTypId(docmasdet);
							documentmng.setDocFolder(1);
							documentmng.setDocSubFolder(2);
							documentmng.setDocDateFolderName(commonNewFolder);
							documentmng.setDocFileName(pastDacmngData.getDocFileName());
							documentmng.setSubject(pastDacmngData.getSubject());
							documentmng.setDescr(pastDacmngData.getDescr());
							documentmng.setEmailStatus(pastDacmngData.getEmailStatus());
							documentmng.setStatusFlag(1);
							documentmng.setEntryBy(userdet);
							documentmng.setEntryDatetime(curDate);
							isupdate=docHibernateUtilService.updateDocumentManageTbl(documentmng,session);
							
							for(int dk=0;dk<docsiz;dk++){
								documentShare=new DocumentShareTblVO();
								notificationdet=new NotificationTblVO();
								sharedet=new UserMasterTblVo();
								int userd=Integer.parseInt(docShareId.get(dk).toString());
								
								documentShare.setDocumentId(documentmng);
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
								notificationdet.setStatusFlag(Integer.parseInt(rb.getString("Initial.statusflag")));
								notificationdet.setDescr(rb.getString("notification.document"));		
								notificationdet.setEntryBy(userdet);
								notificationdet.setEntryDatetime(curDate);
								if(Commonutility.checkempty(rb.getString("notification.reflg.document"))){
									notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.document")));
								} else {
									notificationdet.setTblrefFlag(3);
								}															
								notificationdet.setTblrefID(Commonutility.insertchkintnull(documentId));								
								isupdate=docHibernateUtilService.insertNotificationTbl(notificationdet,session);
								
								String topath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+userd+"/"+commonNewFolder;
								System.out.println("topath-----------------"+topath);
								Commonutility.toFileMoving(oldpath, topath, oldfile, "No");
								}
							}
							if(pastDocStareData!=null && pastDocStareData.size()>0){
								DocumentShareTblVO docshareobj;
								for(Iterator<DocumentShareTblVO> it=pastDocStareData.iterator();it.hasNext();){
									docshareobj=it.next();
									String todelfolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+docshareobj.getUserId().getUserId()+"/"+pastDacmngData.getDocDateFolderName();
									String todelfile=pastDacmngData.getDocFileName();
									common.deleteIfFileExist(todelfolder, todelfile);
									String tocrefolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+docshareobj.getUserId().getUserId()+"/"+commonNewFolder;
									Commonutility.toFileMoving(oldpath, tocrefolder, oldfile, "No");
									
								}
							}
							
							
							common.deleteIfFileExist(oldpath,oldfile);
						}
						
						}else{
							String commonNewFolder=common.getDateYYMMDDFormat();
							System.out.println("-----------------else-----------------------------");
							if(docsiz>0){
									oldpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastDacmngData.getDocDateFolderName();
									System.out.println("oldpath-----------"+oldpath);
									oldfile=pastDacmngData.getDocFileName();
									doctempPath=rb.getString("external.documnet.fldr")+"temp";
									common.createDirIfNotExist(doctempPath);
									Commonutility.toFileMoving(oldpath, doctempPath, oldfile, "NO");
									oldpath=doctempPath;
								
								String todelfolder=rb.getString("external.documnet.fldr") + rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+pastDacmngData.getDocDateFolderName();
								String todelfile=pastDacmngData.getDocFileName();
								common.deleteIfFileExist(todelfolder, todelfile);
								
								documentmng.setUserId(userdet);
								documentmng.setUsrTyp(groupdet);
								documentmng.setDocTypId(docmasdet);
								documentmng.setDocFolder(1);
								documentmng.setDocSubFolder(2);
								documentmng.setDocDateFolderName(commonNewFolder);
								documentmng.setDocFileName(pastDacmngData.getDocFileName());
								documentmng.setSubject(pastDacmngData.getSubject());
								documentmng.setDescr(pastDacmngData.getDescr());
								documentmng.setEmailStatus(pastDacmngData.getEmailStatus());
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
									documentShare=new DocumentShareTblVO();
									notificationdet=new NotificationTblVO();
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
									
									conbytetoarr=Commonutility.toDecodeB64StrtoByary(imagefile);
									String topath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+userd+"/"+commonNewFolder;
									System.out.println("topath-----------------"+topath);
									Commonutility.toFileMoving(oldpath, topath, oldfile, "No");
									}
								}
								String destpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.public.fldr")+rb.getString("external.documnet.generaldoc.fldr")+commonNewFolder;
								common.createDirIfNotExist(doctempPath);
								Commonutility.toFileMoving(oldpath, destpath, oldfile, "No");
								common.deleteIfFileExist(oldpath, oldfile);
							}
						}
					System.out.println("ivrservicecode---------------"+ivrservicecode);
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("servicecode", ivrservicecode);
					serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);	
					if(isupdate){
						tx.commit();
					}
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6415,"+getText("request.format.notmach")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6415","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6415,"+getText("request.values.empty")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6415","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
			}
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("Exception found NewGeneralDocument.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6415, Sorry, an unhandled error occurred", "error", NewGeneralDocument.class);
			serverResponse("SI6415","2","ER0002",getText("catch.error"),locObjRspdataJson);
		} finally {
			if(session!=null){
				session.clear();session.close();session=null;
			}			
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
			System.out.println("as----------------"+as);
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