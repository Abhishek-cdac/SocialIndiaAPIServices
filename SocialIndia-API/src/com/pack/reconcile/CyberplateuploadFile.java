package com.pack.reconcile;

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

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.reconcilevo.CyberplatsetmtfileTblVo;
import com.pack.reconcilevo.persistance.ReconcileDao;
import com.pack.reconcilevo.persistance.ReconcileDaoService;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.billmaintenanceDao.BillMaintenanceDao;
import com.social.billmaintenanceDao.BillMaintenanceDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class CyberplateuploadFile extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
		Session session = null;
		UserMasterTblVo entrydet =null;
		String ivrservicecode=null;
		CyberplatsetmtfileTblVo cyberplatedata=null;
		BillMaintenanceDao billmainDao=null;
		try{
			billmainDao=new BillMaintenanceDaoServices();
			cyberplatedata=new CyberplatsetmtfileTblVo();
			entrydet = new UserMasterTblVo();
			session = HibernateUtil.getSessionFactory().openSession();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					String userId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "entryby");
					String imageDetail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "cyberFile");
					String fileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "cyberFileName");	
					String limgName = FilenameUtils.getBaseName(fileName);
					entrydet.setUserId(Integer.parseInt(userId));
					if(limgName==null || limgName.equalsIgnoreCase("null")){
						limgName ="";
					}
					String limageFomat = FilenameUtils.getExtension(fileName);
					if(limageFomat==null || limageFomat.equalsIgnoreCase("null")){
		 		 		limageFomat ="";
					}
					 Date date = new Date() ;
					 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss") ;
					 String file_name="";
					if (limgName.length() > 0) {
						  file_name =limgName+dateFormat.format(date)+"."+limageFomat;
					}
					 String locupFldrPath=getText("external.fldr")+getText("external.cyberplateuplfldr");//file upload path
					//backupfile path
					// String locuplbackupPath=getText("external.fldr")+getText("external.backupfldr");
						if((imageDetail!=null && !imageDetail.equalsIgnoreCase("null") && !imageDetail.equalsIgnoreCase("")) && (imageDetail!=null && !imageDetail.equalsIgnoreCase("") && !imageDetail.equalsIgnoreCase("null"))){
							Commonutility.toWriteConsole("File Upload Start");	
							File lvrimgSrchPathFileobj = new File(imageDetail);// source path
							Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
							File lvrFileToCreate = new File (locupFldrPath+"/",file_name);  
							Commonutility.toWriteConsole("lvrFileToCreate : "+lvrFileToCreate.getAbsolutePath());						
					        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file 
					        String filepath=locupFldrPath+"/"+file_name;
					        String checksum=Commonutility.getChecksumValue(filepath);
							String qryCheck="select ivrBnCHECK_SUM from CyberplatsetmtfileTblVo where ivrBnCHECK_SUM='"+checksum+"'";
							System.out.println("Step 2: ------qryCheck ---------- "+qryCheck);
							String checkdubval=billmainDao.checkDuplicate(qryCheck);
							System.out.println("Step 3: ------checkdubval ---------- "+checkdubval);
							ReconcileDao reconile=null;
							entrydet.setUserId(Integer.parseInt(userId));
							if(checkdubval==null || checkdubval.equalsIgnoreCase("null") || checkdubval.equalsIgnoreCase("")){
							reconile=new ReconcileDaoService();
							cyberplatedata.setIvrBnCHECK_SUM(checksum);
							cyberplatedata.setIvrBnFLIENAME(file_name);
							cyberplatedata.setIvrBnENTRY_BY(entrydet);
							cyberplatedata.setIvrBnExctSTATUS(0);
							cyberplatedata.setIvrBnSTATUS(1);
					        cyberplatedata.setIvrBnENTRY_DATETIME(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					        int rowid = reconile.toInsertcyberplateData(cyberplatedata);
					       Commonutility.toWriteConsole("rowid : "+rowid);
					        if(rowid>0){
					        	CyberPaygateCall cybercall=new CyberPaygateCall("cyberplat");
					        	cybercall.start();
					        	 System.out.println("Step 3: ------rowid ---------- "+rowid);
					        }else{
					        	
					        }
							}
							locObjRspdataJson=new JSONObject();	
							locObjRspdataJson.put("status", "SUCCESS");
							locObjRspdataJson.put("message", "Successfully Uploaded");
							locRtnSrvId="SI40000";locRtnStsCd="00"; locRtnRspCode="R0084";locRtnMsg=mobiCommon.getMsg("R0084");	
							serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
						}else{
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI40000,"+getText("request.format.notmach")+"", "info", CyberplateuploadFile.class);
							serverResponse("SI40000","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
						}
					
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI40000,"+getText("request.format.notmach")+"", "info", CyberplateuploadFile.class);
					serverResponse("SI40000","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6406,"+mobiCommon.getMsg("R0003")+"", "info", CyberplateuploadFile.class);
				serverResponse("SI40000","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
			
		}catch (Exception ex){
			log.logMessage("Service code : SI40000,"+mobiCommon.getMsg("R0087")+"", "error", CyberplateuploadFile.class);
			serverResponse("SI40000", "02", "R0087", mobiCommon.getMsg("R0087"), locObjRspdataJson);
		}
		return SUCCESS;
	
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
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
}
