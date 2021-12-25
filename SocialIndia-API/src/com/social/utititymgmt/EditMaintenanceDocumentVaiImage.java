package com.social.utititymgmt;

import java.io.PrintWriter;
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

import com.opensymphony.xwork2.ActionSupport;
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
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class EditMaintenanceDocumentVaiImage   extends ActionSupport {

	private String ivrparams;
	private String ivrservicefor;
	CommonUtils common = new CommonUtils();
	private DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	byte conbytetoarr[] = new byte[1024];

	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		// String lsvSlQry = null;		
		String ivrservicecode = null;
		String ivrCurntusridstr = null;
		Integer ivrEntryByusrid = 0;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			System.out.println("ivrparams----------------" + ivrparams);
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null")
					&& ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams---------------" + ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					System.out.println("true------------------");
					DocumentManageTblVO documentmng = new DocumentManageTblVO();
					DocumentShareTblVO documentShare = new DocumentShareTblVO();
					MaintenanceFeeTblVO maintanencefee = new MaintenanceFeeTblVO();
					UserMasterTblVo userdet = new UserMasterTblVo();
					UserMasterTblVo sharedet = new UserMasterTblVo();
					GroupMasterTblVo groupdet = new GroupMasterTblVo();
					NotificationTblVO notificationdet=new NotificationTblVO();

					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson,
									"servicecode");
					System.out.println("ivrservicecode----------------"
							+ ivrservicecode);
					locObjRecvdataJson = (JSONObject) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					ivrEntryByusrid = (Integer) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson,
									"currentloginid");
					System.out.println("ivrCurntusridstr---------------------"
							+ ivrCurntusridstr);
					if (ivrEntryByusrid != null) {
					} else {
						ivrEntryByusrid = 0;
					}

					locObjRecvdataJson = (JSONObject) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					System.out
							.println("locObjRecvdataJson----------------------"
									+ locObjRecvdataJson);
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "usrTyp");
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					// userdet.setGroupCode(groupdet);
					JSONArray oldshareusers=new JSONArray();

					JSONArray docShareId = (JSONArray) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"docShareId");
					int emailStatus = (int) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"emailStatus");
					int documentId = (int) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"documentId");

					int docsiz = docShareId.length();
					System.out.println("docsiz------------" + docsiz);
					Date curDate = new Date();
				
					String billDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "billDate");
					String imageDetail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageDetail");
					String fileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileName");
					String limgName = FilenameUtils.getBaseName(fileName);
					if(limgName==null || limgName.equalsIgnoreCase("null"))
					{
						limgName ="";
					}
					String limageFomat = FilenameUtils.getExtension(fileName);
					if(limageFomat==null || limageFomat.equalsIgnoreCase("null"))
					{
		 		 		limageFomat ="";
					}
					if(fileName!=null && !fileName.equalsIgnoreCase("")){
						documentmng.setDocFileName(fileName);
						}else{
							fileName="";
						}
					String docFolderDate=common.getDateYYMMDDFormat();
					
					documentmng.setDocumentId(documentId);
					documentmng.setMaintenanceId(maintanencefee);
					documentmng.setDocDateFolderName(docFolderDate);
					documentmng.setEmailStatus(emailStatus);
					documentmng.setEntryBy(userdet);
					documentmng.setEntryDatetime(curDate);
					
					
					
					String doctempPath="";
					String oldpath="";
					String oldfile="";
					DocumentManageTblVO pastDacmngData=new DocumentManageTblVO();
					List<DocumentShareTblVO> pastDocStareData=new ArrayList<DocumentShareTblVO>();
					pastDacmngData=docHibernateUtilService.getdocumentmanagebydocId(documentmng);
					String dsqry="FROM DocumentShareTblVO where documentId.documentId="+documentId;
					pastDocStareData=docHibernateUtilService.getdocumentsharetblByDocId(dsqry);
					
					if(fileName!=null && !fileName.equalsIgnoreCase("")){
						conbytetoarr=Commonutility.toDecodeB64StrtoByary(imageDetail);
						doctempPath=rb.getString("external.documnet.fldr")+"temp";
						oldfile=fileName;
						String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, doctempPath, fileName);
						oldpath=doctempPath;
					
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
						
						oldpath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+pastoneuserId+"/"+pastDacmngData.getDocDateFolderName();
						oldfile=pastDacmngData.getDocFileName();
						doctempPath=rb.getString("external.documnet.fldr")+"temp";
						common.createDirIfNotExist(doctempPath);
						Commonutility.toFileMoving(oldpath, doctempPath, oldfile, "NO");
						oldpath=doctempPath;
					}
					
					documentmng.setDocFileName(oldfile);
					
					Date bilDt=new Date();
					if(billDate!=null && !billDate.equalsIgnoreCase("")){
					bilDt=common.stringTODate(billDate);
					}
					System.out.println("bilDt---------------"+bilDt);
					maintanencefee.setBillDate(bilDt);
					maintanencefee.setModifyDatetime(curDate);
					docHibernateUtilService.editUpdateMaintenanceFeeTbl(maintanencefee,session); 
					System.out.println("------------updated into Maintenance Table-----------------");
					System.out.println("documentmng.getDocFileName()------------"+ documentmng.getDocFileName());
					docHibernateUtilService.updateMtDocumentManageTbl(documentmng,session);
					System.out.println("------------updated into DocumentManage Table-----------------");
					
					
					if(pastDocStareData!=null && pastDocStareData.size()>0){
						DocumentShareTblVO docshareobj;
						for(Iterator<DocumentShareTblVO> it=pastDocStareData.iterator();it.hasNext();){
							docshareobj=it.next();
							String todelfolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+docshareobj.getUserId()+"/"+pastDacmngData.getDocDateFolderName();
							String todelfile=pastDacmngData.getDocFileName();
							common.deleteIfFileExist(todelfolder, todelfile);
							String tocrefolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+docshareobj.getUserId()+"/"+docFolderDate;
							Commonutility.toFileMoving(oldpath, tocrefolder, oldfile, "No");
							
						}
					}
					
					int docsharsiz = docShareId.length();
					System.out.println("docShareId------------------"+docShareId.toString());
					if (docsharsiz > 0) {
						for (int dk = 0; dk < docsharsiz; dk++) {
							sharedet = new UserMasterTblVo();
							int userd = Integer.parseInt(docShareId.get(dk)
									.toString());
							sharedet.setUserId(userd);
							String userpath=Integer.toString(userd);
							documentShare = new DocumentShareTblVO();
							notificationdet=new NotificationTblVO();
							String docfold=rb.getString("external.documnet.fldr");
							common.createDirIfNotExist(docfold);
							String privatepath=docfold+rb.getString("external.documnet.private.fldr");
							common.createDirIfNotExist(privatepath);
							String maintfolder=privatepath+rb.getString("external.documnet.maintenancedoc.fldr");
							common.createDirIfNotExist(maintfolder);
							common.createDirIfNotExist(maintfolder+userpath);
							
							String tocrefolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+userd+"/"+docFolderDate;
							Commonutility.toFileMoving(oldpath, tocrefolder, oldfile, "No");
							System.out.println("tocrefolder-----------------"+tocrefolder);
							System.out.println("------------File Saved To External Folder-----------------");
							
							documentShare.setDocumentId(documentmng);
							documentShare.setUserId(sharedet);
							documentShare.setStatus(1);
							documentShare.setEntryBy(userdet);
							documentShare.setEntryDatetime(curDate);
							docHibernateUtilService.insertDocumentShareTbl(documentShare,session);
							System.out.println("------------Inserted into DocumentShare Table-----------------");
							
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
							notificationdet.setTblrefID(Commonutility.insertchkintnull(documentId));
							docHibernateUtilService.insertNotificationTbl(notificationdet,session);
							System.out.println("------------Inserted into Notification Table-----------------");

						}
					} else {
						// no user to share
						System.out.println("------------No user to share the document (No insert in tables)-----------------");
					}
					
					System.out.println("ivrservicecode---------------"
							+ ivrservicecode);
					locObjRspdataJson = new JSONObject();
					locObjRspdataJson.put("servicecode", ivrservicecode);
					serverResponse(ivrservicecode, "0", "0000", "success",
							locObjRspdataJson);
				} else {
					locObjRspdataJson = new JSONObject();
					log.logMessage("Service code : SI6412,"
							+ getText("request.format.notmach") + "", "info",
							NewGeneralDocument.class);
					serverResponse("SI6412", "1", "EF0001",
							getText("request.format.notmach"),
							locObjRspdataJson);
				}
			} else {
				locObjRspdataJson = new JSONObject();
				log.logMessage("Service code : SI6412,"
						+ getText("request.values.empty") + "", "info",
						NewGeneralDocument.class);
				serverResponse("SI6412", "1", "ER0001",
						getText("request.values.empty"), locObjRspdataJson);
			}
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			System.out
					.println("Exception found NewMaintenanceDocument.class execute() Method : "
							+ e);
			locObjRspdataJson = new JSONObject();
			log.logMessage(
					"Service code : SI6412, Sorry, an unhandled error occurred",
					"error", NewGeneralDocument.class);
			serverResponse("SI6412", "2", "ER0002", getText("catch.error"),
					locObjRspdataJson);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}			
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson) {
		PrintWriter out = null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response = null;
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
			try {
				out = response.getWriter();
				out.print("{\"servicecode\":\"" + serviceCode + "\",");
				out.print("{\"statuscode\":\"2\",");
				out.print("{\"respcode\":\"E0002\",");
				out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
				out.print("{\"data\":\"{}\"}");
				out.close();
				ex.printStackTrace();
			} catch (Exception e) {
			} finally {
			}
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
