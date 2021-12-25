package com.social.utititymgmt;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public class SaveMaintenanceExcelDocument extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	private String ivrservicecode;
	private String entryby;
	int ivrEntryByusrid=0;
	Log log=new Log();
	CommonUtils common=new CommonUtils();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	public String execute(){
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		
		Session locObjsession = null;
		String ivrservicecode = null;
		String ivrCurntusridstr=null;
		Integer ivrEntryByusrid=0;
		String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
		try{
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					UserMasterTblVo userdet=new UserMasterTblVo();
					GroupMasterTblVo groupdet=new GroupMasterTblVo();
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"currentloginid");
					if(ivrEntryByusrid!=null){
					}else{ ivrEntryByusrid=0; }
					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					int societyCode = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyCode");
					
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					String imageDetail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageDetail");
					String fileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileName");	
					System.out.println("fileName --------------------------- "+fileName);
					String limgName = FilenameUtils.getBaseName(fileName);
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
					 String locupFldrPath=getText("external.fldr")+getText("external.uplfldr");//file upload path
					//backupfile path
					 String locuplbackupPath=getText("external.fldr")+getText("external.backupfldr");
						if((imageDetail!=null && !imageDetail.equalsIgnoreCase("null") && !imageDetail.equalsIgnoreCase("")) && (imageDetail!=null && !imageDetail.equalsIgnoreCase("") && !imageDetail.equalsIgnoreCase("null"))){
							byte imgbytee[]= new byte[1024];
							 imgbytee=Commonutility.toDecodeB64StrtoByary(imageDetail);
							
							 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, locupFldrPath+ivrEntryByusrid+"/", file_name);
							}
						String locuplFldrPath=locupFldrPath+ivrEntryByusrid+"/"+file_name;
				
						
						String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/maintenanceDocument.jrxml");
						System.out.println("Details==============="+locupFldrPath+ivrEntryByusrid+"/------"+locuplbackupPath+"-------------"+locuplFldrPath+"-----------"+userId+"----------"+limageFomat+"------------------"+usrTyp+"-------------"+lvrJrxml);
//						MaintenanceExcelDocumentUploadEngine maintenanceDocEngine=new MaintenanceExcelDocumentUploadEngine(locupFldrPath+ivrEntryByusrid+"/", locuplbackupPath,locuplFldrPath,userId,limageFomat,usrTyp,lvrJrxml,societyCode,fileName);
//						maintenanceDocEngine.start();
						
//						MaintenanceTxtDocumentUploadEngine maintenanceDocEngine=new MaintenanceTxtDocumentUploadEngine(locupFldrPath+ivrEntryByusrid+"/", locuplbackupPath,locuplFldrPath,userId,limageFomat,usrTyp,lvrJrxml,societyCode,fileName);
//						maintenanceDocEngine.run();
						
						locObjRspdataJson=new JSONObject();	
						locObjRspdataJson.put("status", "SUCCESS");
						locObjRspdataJson.put("message", "Successfully Uploaded Data Will be Extracted.");
						locRtnSrvId="SI6406";locRtnStsCd="00"; locRtnRspCode="R0084";locRtnMsg=mobiCommon.getMsg("R0084");	
						serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
					
					
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6406,"+getText("request.format.notmach")+"", "info", NewGeneralDocument.class);
					serverResponse("SI6403","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6406,"+mobiCommon.getMsg("R0003")+"", "info", NewGeneralDocument.class);
				serverResponse("SI6403","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
			
		}catch (Exception ex){
			log.logMessage("Service code : SI6406,"+mobiCommon.getMsg("R0087")+"", "error", NewGeneralDocument.class);
			serverResponse("SI6403", "02", "R0087", mobiCommon.getMsg("R0087"), locObjRspdataJson);
		}
		return SUCCESS;
	}public String pdfGenerator(){
		Session session = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Iterator lvrObjeventlstitr = null;
		List<Object> lvrObjfunctionlstitr = null;
		MaintenanceFeeTblVO lvrEvnttblvoobj = null;
		JSONObject lvroutdata=new JSONObject();
		String lvSlqry = null;
		String lvSlfunqry=null;
		Common common = null;
		try{
			common=new CommonDao();
			session = HibernateUtil.getSession();
			lvSlqry = "from MaintenanceFeeTblVO where pdfstatus='P' and statusFlag=1";
			lvrObjeventlstitr=session.createQuery(lvSlqry).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				lvrEvnttblvoobj = (MaintenanceFeeTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("usrid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getUserId().getUserId()));
				String userpath=String.valueOf(lvrEvnttblvoobj.getUserId().getUserId());
				lvrInrJSONObj.put("mainId", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getMaintenanceId()));
				lvrInrJSONObj.put("total", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getTotbillamt()));
				lvrInrJSONObj.put("servicescharge", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getServiceCharge()));
				lvrInrJSONObj.put("billdate", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getBillDate()));
				lvrInrJSONObj.put("munipaltax", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getMunicipalTax()));
				lvrInrJSONObj.put("penality", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getPenalty()));
				lvrInrJSONObj.put("watercharge", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getWaterCharge()));
				lvrInrJSONObj.put("nonoccupancy", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getNonOccupancyCharge()));
				lvrInrJSONObj.put("culturecharge", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getCulturalCharge()));
				lvrInrJSONObj.put("skinfund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getSinkingFund()));
				lvrInrJSONObj.put("repairfund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getRepairFund()));
				lvrInrJSONObj.put("insurancefund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getInsuranceCharges()));
				lvrInrJSONObj.put("playzone", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getPlayZoneCharge()));
				lvrInrJSONObj.put("majorrepairfund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getMajorRepairFund()));
				lvrInrJSONObj.put("interest", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getInterest()));
				lvSlfunqry = "select docDateFolderName,docFileName  from DocumentManageTblVO where maintenanceId="+lvrEvnttblvoobj.getMaintenanceId();
				lvrObjfunctionlstitr=session.createQuery(lvSlfunqry).list();
				lvrInrJSONObj.put("doclist", lvrObjfunctionlstitr);	
				JSONArray jary=(JSONArray) Commonutility.toHasChkJsonRtnValObj(lvrInrJSONObj,"doclist");
				String file="";
				String filename="";
				for(int i=0;i<jary.length();i++){			 			
		 			JSONArray temmauto = jary.getJSONArray(i);    					
		 			file=(String)temmauto.get(0);    
		 			filename=(String)temmauto.get(1);    	
				}
				DocumentShareTblVO documentShare = new DocumentShareTblVO();
				UserMasterTblVo userdet = new UserMasterTblVo();
				userdet.setUserId(lvrEvnttblvoobj.getUserId().getUserId());
				documentShare.setUserId(userdet);
				String topath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+rb.getString("external.inner.webpath")+userpath+"/"+file;
				
				GenerateMaintenancePdf generaepdf=new GenerateMaintenancePdf();	
				boolean fileuploadstatus=generaepdf.generatePdf(topath, filename,lvrEvnttblvoobj,documentShare);
				boolean lvrfunmtr=false;
				String updatesql ="update MaintenanceFeeTblVO set pdfstatus='S' where maintenanceId="+lvrEvnttblvoobj.getMaintenanceId()+" and statusFlag=1";
				lvrfunmtr=common.commonUpdate(updatesql);
				
				/*lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;*/
			}
			//lvroutdata.put("data", lvrEventjsonaryobj);
			
			System.out.println("lvrEventjsonaryobj ----------- "+lvrEventjsonaryobj);
		}catch(Exception e){
			
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
