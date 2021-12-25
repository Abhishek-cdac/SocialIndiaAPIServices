package com.pack.resident;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.resident.persistance.ResidentDao;
import com.pack.resident.persistance.ResidentDaoservice;
import com.pack.residentvo.UsrUpldfrmExceTbl;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ResidentExcel;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class Xlsupload  extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	private String ivrservicecode;
	private String entryby;
	int ivrEntryByusrid=0;
	Log log=new Log();	
	public String execute(){
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		JSONObject locObjdataJson =null;		
		Session locObjsession=null;
		//String xlsfilenameenc="";
		ResidentDao locuplpathObj=null;
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
		try{
			locObjsession = HibernateUtil.getSession();
			CommonUtils locCommutillObj = null;
			locCommutillObj = new CommonUtilsDao();
			UsrUpldfrmExceTbl locObjuplfpathvo = new UsrUpldfrmExceTbl();
			SocietyMstTbl scociety = new SocietyMstTbl();
			IDCardMasterTblVO idObj = new IDCardMasterTblVO();
			int startrow = 1;
			int columnlength = 7;
			int locuplfileid = 0;
			String password = "";
			String pGrpName = getText("Grp.resident");
			GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
			SocietyMstTbl locSctyIdObj = null, locSctyMstrvo = null;
			//boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){	
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					entryby=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "entryby");
					if (entryby != null && Commonutility.toCheckisNumeric(entryby)) {
						ivrEntryByusrid = Integer.parseInt(entryby);
					} else {
						ivrEntryByusrid = 0;
					}
				
					if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
						ivrDecissBlkflag = ivrservicefor;
					} else {//post
						ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
					}
					//xlsfilenameenc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileencdata");
					String xlsfilesrchpath = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "filesrchpath");
					String filename =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "filename");
					
					String limgName = FilenameUtils.getBaseName(filename);
					if (limgName == null || limgName.equalsIgnoreCase("null")) {
						limgName = "";
					}
					String limageFomat = FilenameUtils.getExtension(filename);
					if (limageFomat == null || limageFomat.equalsIgnoreCase("null")) {
						limageFomat = "";
					}
					Date date = new Date() ;
				 	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss") ;
				 	SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
				 	String file_name="";
					if (limgName.length() > 0) {
						file_name =limgName+dateFormat.format(date)+"."+limageFomat;
					}
				
					String locupFldrPath=getText("external.fldr")+getText("external.uplfldr");//file upload path
				//backupfile path
					String locuplbackupPath=getText("external.fldr")+getText("external.backupfldr");
				 //Create group name
					String grpname= getText("Grp.resident");
					String lvrFldrpath=locupFldrPath+ivrEntryByusrid+"/";
					//if((xlsfilenameenc!=null && !xlsfilenameenc.equalsIgnoreCase("null") && !xlsfilenameenc.equalsIgnoreCase("")) && (xlsfilenameenc!=null && !xlsfilenameenc.equalsIgnoreCase("") && !xlsfilenameenc.equalsIgnoreCase("null"))){
					if((xlsfilesrchpath!=null && !xlsfilesrchpath.equalsIgnoreCase("null") && !xlsfilesrchpath.equalsIgnoreCase("")) && (filename!=null && !filename.equalsIgnoreCase("") && !filename.equalsIgnoreCase("null"))){
						//byte imgbytee[]= new byte[1024];
						//imgbytee=Commonutility.toDecodeB64StrtoByary(xlsfilenameenc);				
						//String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, locupFldrPath+ivrEntryByusrid+"/", file_name);
						
						Commonutility.crdDir(lvrFldrpath);
						Commonutility.toWriteConsole("File Upload Start");	
						File lvrimgSrchPathFileobj = new File(xlsfilesrchpath);// source path
						Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
						File lvrFileToCreate = new File (locupFldrPath+ivrEntryByusrid+"/",file_name);  
						Commonutility.toWriteConsole("lvrFileToCreate : "+lvrFileToCreate.getAbsolutePath());						
				        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file
					
					}	
					String locuplFldrPath=locupFldrPath+ivrEntryByusrid+"/"+file_name;
				 
				//xls
				//name
				//namedaate.xls
				//up/3/namedaate.xls	
				//bk/u/2016-07025/4/x.xls		
//				String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
//				String locvrFnrst=null;
				String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;	
//				String dup_row="";
				
				
				locObjuplfpathvo.setFileName(file_name);
				locObjuplfpathvo.setUsrId(ivrEntryByusrid);
				locObjuplfpathvo.setFolderName(locupFldrPath+ivrEntryByusrid+"/");
				locObjuplfpathvo.setStatus(1);
				locObjuplfpathvo.setEnrtyBy(ivrEntryByusrid);
				locObjuplfpathvo.setEntryDate(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				
				Commonutility.toWriteConsole("Before: usr_upldfrm_exce_tbl");	
				
				// insert into usr_upldfrm_exce_tbl
				locuplpathObj =new ResidentDaoservice();
				locuplfileid =locuplpathObj.saveUplFilepath(locObjuplfpathvo);	
				
				Commonutility.toWriteConsole("After: usr_upldfrm_exce_tbl - locupFldrPath:" + locupFldrPath +"	locuplbackupPath:"+locuplbackupPath +" locuplFldrPath:"+locuplFldrPath+"	limageFomat:"+limageFomat);	
				
				ResidentExcel excel = new ResidentExcel(locupFldrPath+ivrEntryByusrid+"/", locuplbackupPath,grpname,locuplFldrPath,ivrEntryByusrid,limageFomat,locuplfileid);
				excel.run();
				
				//ResidentExcelEngine locREnginThreadObj=new ResidentExcelEngine("E:/FileMoveTest/", "E:/backup/","Resident","E:/FileMoveTest/Testexcel.xls",1,"xls");
				//locREnginThreadObj.setName("RPKARANThread");		
				//locREnginThreadObj.start();			
				
//				ResidentExcelEngine locREnginThreadObj = new ResidentExcelEngine(locupFldrPath+ivrEntryByusrid+"/", locuplbackupPath,grpname,locuplFldrPath,ivrEntryByusrid,limageFomat,locuplfileid);
//				locREnginThreadObj.start();
				
				
				locObjRspdataJson=new JSONObject();	
				locObjRspdataJson.put("status", "SUCCESS");
				locObjRspdataJson.put("message", "Successfully Uploaded Data Will be Extracted.");
				locRtnSrvId="SI7005";locRtnStsCd="0"; locRtnRspCode="S7005";locRtnMsg=getText("resident.fileupload.success");
				Commonutility.toWriteConsole("Before: toWriteAudit");
				AuditTrial.toWriteAudit(getText("RESTAD011"), "RESTAD011", ivrEntryByusrid);
				Commonutility.toWriteConsole("After: toWriteAudit");
				serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				
			}else{
				locObjdataJson=new JSONObject();				
				serverResponse("SI1002","1","E0001","Request values not not correct format",locObjdataJson);
			}
			}else{
				locObjdataJson=new JSONObject();
				log.logMessage("Service code : SI2002, Request values are not correct format", "info", Xlsupload.class);
				serverResponse("SI2002","1","E0001","Request values are not correct format",locObjdataJson);
			}
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found in xlsupload.action execute() Method : "+e);
			locObjdataJson=new JSONObject();			
			serverResponse("SI1002","2","E0002","Sorry, an unhandled error occurred",locObjdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;
			locObjdataJson =null;locObjRspdataJson = null;
		}		
		return SUCCESS;
	}
	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
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
	public String getIvrservicecode() {
		return ivrservicecode;
	}
	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}
	public String getIvrservicefor() {
		return ivrservicefor;
	}
	public void setIvrservicefor(String ivrservicefor) {
		this.ivrservicefor = ivrservicefor;
	}
	
}
