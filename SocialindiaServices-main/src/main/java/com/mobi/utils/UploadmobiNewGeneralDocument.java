package com.mobi.utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class UploadmobiNewGeneralDocument extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();	
	CommonUtils common=new CommonUtils();
	private DocumentUtilitiServices docHibernateUtilService=new DocumentUtilitiDaoServices();
	
	public String execute() {
//		fileUpload.add(new File("F://Raghu//Books.pdf"));
//		fileUploadFileName.add("Books.pdf");
//		fileUpload.add(new File("/home/sasikumar/Downloads/16-01.jpg"));
//		fileUploadFileName.add("16-01.jpg");
//		fileUpload.add(new File("/home/sasikumar/Downloads/video/ben.mp4"));
//		fileUploadFileName.add("ben.mp4");
//		fileUpload.add(new File("/home/sasikumar/Downloads/ben.jpg"));
//		fileUploadFileName.add("ben.jpg");
		Log log= null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		Date dt=new Date();
		System.out.println("service start time-------------"+dt);
		try {
			Session session = null;
			Transaction tx = null;
			String topath="";
			log = new Log();
			ivrservicecode = getText("report.issue");
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			String locRid = null;
			String locUserType = null;
			String locDocTypeId = null;
			String locDocSubFolder = null;
			String locSubject = null;
			String locDescr = null;
			String locEmailStatus = null;
			int rid = 0;
			JSONArray locDocShareId = new JSONArray();
			System.out.println("ivrparams:" + ivrparams);
			Commonutility.toWriteConsole("Step 1 : General Document Create Called [Start]");
			log.logMessage("Step 1 : General Document Create Called [Start]", "info", UploadmobiNewGeneralDocument.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				System.out.println("ivrparams ##:" + ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					System.out.println("1121");
					if (Commonutility.checkempty(ivrservicecode)) {
						if (ivrservicecode.length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
							
						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.error")));
					}					
					if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					
					if (Commonutility.checkempty(societyKey)) {
						if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {
							
						} else {
							String[] passData = { getText("societykey.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}				
					System.out.println("111");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						locUserType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"usrTyp");
						locDocTypeId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"docTypId");
						locDocShareId = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"docShareId");
						locDocSubFolder= (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"docSubFolder");
						locSubject = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"subject");
						locDescr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"descr");
						locEmailStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"emailStatus");
						
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
							if (rid !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}	
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				}else {
					System.out.println("111ad");
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				System.out.println("11dsd1");
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("validation flg :" +flg, "info", UploadmobiNewGeneralDocument.class);
			if (flg) {
				log.logMessage("Enter into UploadmobiNewGeneralDocument check" , "info", UploadmobiNewGeneralDocument.class);
				locObjRspdataJson=new JSONObject();			
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						DocumentManageTblVO documentmng=new DocumentManageTblVO();
						DocumentShareTblVO documentShare=new DocumentShareTblVO();
						UserMasterTblVo userdet=new UserMasterTblVo();
						UserMasterTblVo sharedet=new UserMasterTblVo();
						GroupMasterTblVo groupdet=new GroupMasterTblVo();
						DoctypMasterTblVO docmasdet=new DoctypMasterTblVO();
						NotificationTblVO notificationdet=new NotificationTblVO();	
						
						userdet.setUserId(rid);
						groupdet.setGroupCode(Commonutility.stringToInteger(locUserType));
						docmasdet.setIvrBnDOC_TYP_ID(Commonutility.stringToInteger(locDocTypeId));
						
						int docsiz = locDocShareId.length();					
						Date curDate = new Date();
						
						if(fileUpload.size()>0){
							for(int i=0;i<fileUpload.size();i++){
								try {
									session = HibernateUtil.getSessionFactory().openSession();
									tx = session.beginTransaction();
									
									File uploadedFile1 = fileUpload.get(i);
									System.out.println("uploadedFile1------------------" + fileUpload.get(i) + 
											"---------uploadedFile1---->" + uploadedFile1);
						        	String fileName1 = fileUploadFileName.get(i);
						        	System.out.println("fileUploadFileName.get(i)------------------" + fileUploadFileName.get(i));
						            String limgName1 = FilenameUtils.getBaseName(fileName1);
						 		 	String limageFomat1 = FilenameUtils.getExtension(fileName1);
						 		 	System.out.println("limgName1------------------" + limgName1 + "---------limageFomat1-------" + limageFomat1);
						 		 	
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
									String lvrRenamefilename = "";
									lvrRenamefilename = limgName1 +lvrtimestam+"."+limageFomat1;
									
									documentmng.setDocSubFolder(Commonutility.stringToInteger(locDocSubFolder));
									documentmng.setDocDateFolderName(common.getDateYYMMDDFormat());
									documentmng.setDocFileName(lvrRenamefilename);
									documentmng.setSubject(locSubject);
									documentmng.setDescr(locDescr);
									documentmng.setEmailStatus(Commonutility.stringToInteger(locEmailStatus));
									documentmng.setStatusFlag(1);
									documentmng.setEntryBy(userdet);
									documentmng.setEntryDatetime(curDate);							
									docHibernateUtilService.insertDocumentManageTbl(documentmng,session);							
									int docsharsiz = locDocShareId.length();
									if (docsharsiz > 0) {
										for (int dk = 0; dk < docsharsiz; dk++) {
											documentShare = new DocumentShareTblVO();
											notificationdet = new NotificationTblVO();
											sharedet = new UserMasterTblVo();
											int userd = Integer.parseInt(locDocShareId.get(dk).toString());

											documentShare.setDocumentId(documentmng);
											sharedet.setUserId(userd);
											documentShare.setUserId(sharedet);
											documentShare.setStatus(1);
											documentShare.setEntryBy(userdet);
											documentShare.setEntryDatetime(curDate);
											boolean isshare=docHibernateUtilService.insertDocumentShareTbl(documentShare,session);
											if (isshare) {
												NotificationDao notificationHbm=new NotificationDaoServices();
												AdditionalDataDao additionalDatafunc=new AdditionalDataDaoServices();
												String additionaldata=additionalDatafunc.formAdditionalDataForDocumentTbl(documentmng,documentShare);
												notificationHbm.insertnewNotificationDetails(sharedet, "", 3, 1, documentmng.getDocumentId(), userdet, additionaldata);
												
												String docfold=getText("external.documnet.fldr");
												common.createDirIfNotExist(docfold);
												String privatepath=docfold+getText("external.documnet.private.fldr");
												common.createDirIfNotExist(privatepath);
												String genfolder=privatepath+getText("external.documnet.generaldoc.fldr");
												common.createDirIfNotExist(genfolder);
												common.createDirIfNotExist(genfolder+getText("external.inner.webpath"));
												common.createDirIfNotExist(genfolder+getText("external.inner.webpath")+userd);
									
												topath=genfolder+getText("external.inner.webpath")+userd+"/"+common.getDateYYMMDDFormat();
												System.out.println("topath-----------------"+topath);
												
												File lvrFileToCreate = new File (topath+"/",documentmng.getDocFileName());
												FileUtils.copyFile(uploadedFile1, lvrFileToCreate);//copying source file to new file
												flg = true;
											}
										}
									} else {
										String docfold=getText("external.documnet.fldr");
										common.createDirIfNotExist(docfold);
										String publicpath=docfold+getText("external.documnet.public.fldr");
										common.createDirIfNotExist(publicpath);
										String genfolder=publicpath+getText("external.documnet.generaldoc.fldr");
										common.createDirIfNotExist(genfolder);
										common.createDirIfNotExist(genfolder+getText("external.inner.webpath"));
										
										topath = genfolder + getText("external.inner.webpath") + common.getDateYYMMDDFormat();				
										
										File lvrFileToCreate = new File (topath+"/",documentmng.getDocFileName()); 
										FileUtils.copyFile(uploadedFile1, lvrFileToCreate);//copying source file to new file
										flg = true;
									}
									tx.commit();
								} catch (Exception e) {
									if(tx!=null){
										tx.rollback();
									}
									Commonutility.toWriteConsole("Step -1 : General Document Create Exception : "+e);
									log.logMessage("Step -1 : General Document Create Exception : "+e, "info", UploadmobiNewGeneralDocument.class);
								} finally {
									if(session!=null){
										session.close();
									}
								}
							}
						}
						
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("servicecode", ivrservicecode);
						locObjRspdataJson.put("topath", topath);
						
						if (flg) {
							serverResponse(ivrservicecode,getText("status.success"),"R0068",mobiCommon.getMsg("R0068"),locObjRspdataJson);
						} else {
							locObjRspdataJson = new JSONObject();
							serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0006"),locObjRspdataJson);
						}
						
					} else {
						locObjRspdataJson = new JSONObject();
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);	
					}
				} else {
					locObjRspdataJson = new JSONObject();
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);	
				}									
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			log.logMessage(getText("Eex") + ex, "error", UploadmobiNewGeneralDocument.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			
		}
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message,  JSONObject dataJson)
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
			System.out.println("=====as==="+as);
			as=EncDecrypt.encrypt(as);
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

	public List<File> getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(List<File> fileUpload) {
		this.fileUpload = fileUpload;
	}

	public List<String> getFileUploadContentType() {
		return fileUploadContentType;
	}

	public void setFileUploadContentType(List<String> fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}

	public List<String> getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(List<String> fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}
	
	

}
