package com.social.utititymgmt;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
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
import com.pack.utilitypkg.ImageCompress;
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

public class SaveMaintenanceImageDocument extends ActionSupport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	private String ivrservicecode;
	private DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	private String entryby;
	int ivrEntryByusrid=0;
	Log log=new Log();
	CommonUtils common=new CommonUtils();
	
	public String execute(){
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		
		Session locObjsession = null;
		String ivrservicecode = null;
		String ivrCurntusridstr=null;
		Integer ivrEntryByusrid=0;
		String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					
					DocumentManageTblVO documentmng = new DocumentManageTblVO();
					DocumentShareTblVO documentShare = new DocumentShareTblVO();
					MaintenanceFeeTblVO maintanencefee = new MaintenanceFeeTblVO();
					UserMasterTblVo userdet = new UserMasterTblVo();
					UserMasterTblVo sharedet = new UserMasterTblVo();
					GroupMasterTblVo groupdet = new GroupMasterTblVo();
					DoctypMasterTblVO docmasdet = new DoctypMasterTblVO();
					NotificationTblVO notificationdet=new NotificationTblVO();
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"currentloginid");
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }
					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					int docTypId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "docTypId");
					int docSubFolder = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"docSubFolder");
					String subject = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"subject");
					int emailStatus = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"emailStatus");										
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					docmasdet.setIvrBnDOC_TYP_ID(docTypId);
					
					Date curDate = new Date();
					JSONArray docShareId = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"docShareId");					
					int docsiz = docShareId.length();										
					String imageDetail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageDetail");
					String fileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileName");
					if(Commonutility.checkempty(fileName)){
						fileName = fileName.replaceAll(" ","_");
					}
					String limgName = FilenameUtils.getBaseName(fileName);
					if(limgName==null || limgName.equalsIgnoreCase("null")){
						limgName ="";
					}
					String limageFomat = FilenameUtils.getExtension(fileName);
					if(limageFomat==null || limageFomat.equalsIgnoreCase("null")){
		 		 		limageFomat ="";
					}
										 
						String filename=common.getUniqStringueDateTime();
						maintanencefee.setUserId(userdet);
						maintanencefee.setEntryBy(userdet);
						maintanencefee.setStatusFlag(1);
						maintanencefee.setPaidStatus(0);
						maintanencefee.setDataUploadFrom(3);
						boolean ifsaved=docHibernateUtilService.insertMaintenanceFeeTbl(maintanencefee,session); 
						if(ifsaved){
						if (docsiz > 0) {
							documentmng.setDocFolder(2);
						} else {
							documentmng.setDocFolder(1);
						}
						System.out.println("------------Inserted into Maintenance Table-----------------");
						documentmng.setUserId(userdet);
						documentmng.setUsrTyp(groupdet);
						documentmng.setDocTypId(docmasdet);
						documentmng.setMaintenanceId(maintanencefee);
						documentmng.setDocSubFolder(docSubFolder);
						documentmng.setDocDateFolderName(common.getDateYYMMDDFormat());
						documentmng.setSubject(subject);
						documentmng.setDescr("");
						documentmng.setEmailStatus(emailStatus);
						documentmng.setStatusFlag(1);
						documentmng.setEntryBy(userdet);
						documentmng.setEntryDatetime(curDate);
						documentmng.setDocFileName(fileName);
						ifsaved=docHibernateUtilService.insertDocumentManageTbl(documentmng,session);												
						int docsharsiz = docShareId.length();
						if (docsharsiz > 0 && ifsaved==true) {
							for (int dk = 0; dk < docsharsiz; dk++) {
								
								sharedet = new UserMasterTblVo();
								int userd = Integer.parseInt(docShareId.get(dk).toString());
								sharedet.setUserId(userd);
								String userpath=Integer.toString(userd);
								documentShare = new DocumentShareTblVO();
								
								String imageType=rb.getString("external.inner.webpath");
								String topath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+imageType+userpath+"/"+common.getDateYYMMDDFormat();																							
								 String locupFldrPath=getText("external.fldr")+getText("external.uplfldr");//file upload path
								//backupfile path
									if((imageDetail!=null && !imageDetail.equalsIgnoreCase("null") && !imageDetail.equalsIgnoreCase("")) && (imageDetail!=null && !imageDetail.equalsIgnoreCase("") && !imageDetail.equalsIgnoreCase("null"))){
										byte imgbytee[]= new byte[1024];
										 imgbytee=Commonutility.toDecodeB64StrtoByary(imageDetail);																				 
										 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, topath, fileName);
										 
											imageType=rb.getString("external.inner.mobilepath");
											String mobtopath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+imageType+userpath+"/"+common.getDateYYMMDDFormat();
											String limageFor = "all";
											int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
							        		int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));	
								        	topath+="/"+fileName;								        	
								        	wrtsts=ImageCompress.toCompressImage(topath, mobtopath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
										}
								
								System.out.println("------------File Saved To External Folder-----------------");
								System.out.println("------------Inserted into DocumentManage Table-----------------");
								documentShare.setDocumentId(documentmng);
								documentShare.setUserId(sharedet);
								documentShare.setStatus(1);
								documentShare.setEntryBy(userdet);
								documentShare.setEntryDatetime(curDate);
								ifsaved=docHibernateUtilService.insertDocumentShareTbl(documentShare,session);
								System.out.println("------------Inserted into DocumentShare Table-----------------");
								if(ifsaved){
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
								ifsaved=docHibernateUtilService.insertNotificationTbl(notificationdet,session);
								
								}

							}
						} else {
							// no user to share
							System.out.println("------------No user to share the document (No insert in tables)-----------------");
						}
						}
					if (ifsaved) {
						tx.commit();
					} else {
						tx.rollback();
					}
						
						locObjRspdataJson=new JSONObject();	
						locObjRspdataJson.put("status", "SUCCESS");
						locObjRspdataJson.put("message", mobiCommon.getMsg("R0088"));
						locRtnSrvId="SI6406";locRtnStsCd="00"; locRtnRspCode="R0088";locRtnMsg=mobiCommon.getMsg("R0088");
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);									
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6407,"+mobiCommon.getMsg("R0067")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6403","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6407,"+mobiCommon.getMsg("R0003")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6403","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
			
		}catch (Exception ex){
			if(tx!=null){
				tx.rollback();
			}
			log.logMessage("Service code : SI6407, "+mobiCommon.getMsg("R0089")+" : "+ex, "error", NewGeneralDocument.class);
			serverResponse("SI6403","02","R0089",mobiCommon.getMsg("R0089"),locObjRspdataJson);
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
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
	public String getIvrservicecode() {
		return ivrservicecode;
	}
	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}
	public String getEntryby() {
		return entryby;
	}
	public void setEntryby(String entryby) {
		this.entryby = entryby;
	}
	public int getIvrEntryByusrid() {
		return ivrEntryByusrid;
	}
	public void setIvrEntryByusrid(int ivrEntryByusrid) {
		this.ivrEntryByusrid = ivrEntryByusrid;
	}
	
	

}
