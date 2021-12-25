package com.mobi.broadcast;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.event.EventDao;
import com.mobi.event.EventDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;

public class BroadCastFileUpload extends ActionSupport{
	Log log=new Log();	
	private String ivrparams;
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();
	private DocumentUtilitiServices docHibernateUtilService=new DocumentUtilitiDaoServices();
	otpDao otp=new otpDaoService();
	CommonUtils common=new CommonUtils();
	public String execute(){
		System.out.println("************Broadcas Event List services******************");
		
		/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"));
		fileUploadFileName.add("Chrysanthemum.jpg");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
		fileUploadFileName.add("Wildlife.wmv");
		
		fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Desert.jpg"));
		fileUploadFileName.add("Desert.jpg");*/
		
		System.out.println("fileUploadFileName-----------------"+fileUploadFileName.toString());
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			System.out.println("ivrparams-------------"+ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String eventId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "event_id");
				
				if (Commonutility.checkempty(servicecode)) {
					if (servicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
						
					} else {
						String[] passData = { getText("service.code.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
				}
				if (Commonutility.checkempty(townshipKey)) {
					if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
						
					} else {
						String[] passData = { getText("townshipid.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
				}
				if (Commonutility.checkempty(societykey)) {
					if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
						
					} else {
						String[] passData = { getText("society.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
				}
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				
				if(flg){
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result* 1111111****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int societyId=userMst.getSocietyId().getSocietyId();
			boolean isInsert=false;
			mobiCommon mobCom=new mobiCommon();
			if(fileUpload.size()>0){
				BroadCastDao brosdcasthbm=new BroadCastDaoService();
				EventDao eventhbm=new EventDaoServices();
				String qry="from EventTblVO WHERE ivrBnEVENTSTATUS=1 and ivrBnEVENT_ID='"+eventId+"'";
				EventTblVO eventobj=new EventTblVO();
				eventobj=eventhbm.geteventobjectByQuery(qry);
				DoctypMasterTblVO docmasdet=new DoctypMasterTblVO();
				
				Date curDate = new Date();
		     	 for(int i=0;i<fileUpload.size();i++){
		     		DocumentManageTblVO documentmng=new DocumentManageTblVO();
	        		 File uploadedFile = fileUpload.get(i);
	        	 String fileName = fileUploadFileName.get(i);
	 		 	String dateFolderPath=common.getDateYYMMDDFormat();
	 		 	documentmng.setUserId(userMst);
	 		 	documentmng.setUsrTyp(userMst.getGroupCode());
				documentmng.setDocTypId(docmasdet);
	 		 	documentmng.setDocFolder(1);
	 		 	documentmng.setDocSubFolder(2);
				documentmng.setDocDateFolderName(dateFolderPath);
				documentmng.setDocFileName(fileName);
				documentmng.setSubject(eventobj.getIvrBnEVENT_TITLE());
				documentmng.setDescr(eventobj.getIvrBnEVENT_DESC());
				documentmng.setEmailStatus(1);
				documentmng.setStatusFlag(1);
				documentmng.setEntryBy(userMst);
				documentmng.setEntryDatetime(curDate);
				docmasdet.setIvrBnDOC_TYP_ID(10);
				documentmng.setEventId(eventobj);
				boolean isshare=docHibernateUtilService.insertIntoDocumentManageTbl(documentmng);
				if(isshare){
					String destPath =getText("external.documnet.fldr");
					String publicOrPrivateTath=getText("external.documnet.public.fldr");
					String generalpath=getText("external.documnet.generaldoc.fldr");
					String webpath=getText("external.inner.webpath");
					String mobilepath=getText("external.inner.mobilepath");
					dateFolderPath=dateFolderPath+"/";
					String mobilePath=destPath+publicOrPrivateTath+generalpath+mobilepath+dateFolderPath;
					String destfilePath=destPath+publicOrPrivateTath+generalpath+webpath+dateFolderPath;
					 File destFile  = new File(destfilePath, fileName);
					 File mobileFile  = new File(mobilePath, fileName);
					FileUtils.copyFile(uploadedFile, destFile);
					FileUtils.copyFile(uploadedFile, mobileFile);
				}
				
	        	 }
				
		     	serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",getText("R0029"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",getText("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======Broadcast Event  List====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, BroadcastEventList an unhandled error occurred", "info", familyMemberList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson);
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
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
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
