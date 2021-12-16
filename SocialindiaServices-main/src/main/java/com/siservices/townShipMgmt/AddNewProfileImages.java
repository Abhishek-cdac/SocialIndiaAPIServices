package com.siservices.townShipMgmt;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.DocMasterlst;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;

public class AddNewProfileImages extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchanthbm=new MerchantManageDaoServices();
	private CommonUtils common=new CommonUtils();
	byte conbytetoarr[]= new byte[1024];
	byte conbytetoarr1[]= new byte[1024];
	ResourceBundle rb=ResourceBundle.getBundle("applicationResources");

	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode=null,tshiplogosrchpth = null, lvrTShipicoimgsrchpth;
		Integer ivrEntryByusrid = 0;
		List<Object> locObjDoclst = null;
		try{
			String logoImageFileName=null,icoImageFileName=null;//townshiplogo=null,townshipico=null;
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"userId");
				Integer townshipid=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"townshipid");
				//townshiplogo=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshiplogo");
				//townshipico=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipico");
				logoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "logoImageFileName");
				icoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "icoImageFileName");
				Integer isdelete=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"updatestatus");
				tshiplogosrchpth = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "tshiplogosrchpth");// New Img Upload [PL0019]
				lvrTShipicoimgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipicosrchpath");// New ico Img Upload [PL0019]
				Commonutility.toWriteConsole("tshiplogosrchpth : "+tshiplogosrchpth);
				Commonutility.toWriteConsole("lvrTShipicoimgsrchpth : "+lvrTShipicoimgsrchpth);
				if(logoImageFileName!=null && logoImageFileName!=" " && townshipid>0){					
					 logoImageFileName = logoImageFileName.replaceAll(" ", "_");
					//// New Img Upload [PL0019]
					 String lvrWebImagpath = rb.getString("external.uploadfile.township.webpath");
					 String lvrMobImgpath = rb.getString("external.uploadfile.township.mobilepath");
					// String delrs = Commonutility.todelete("", lvrWebImagpath+townshipid+"/");
					 String delrs_mob= Commonutility.todelete("", lvrMobImgpath+townshipid+"/");					  
					 Commonutility.toWriteImageMobWebNewUtill(townshipid, logoImageFileName,tshiplogosrchpth,lvrWebImagpath,lvrMobImgpath,getText("thump.img.width"),getText("thump.img.height"), log, getClass());
															
					/*					
					conbytetoarr=Commonutility.toDecodeB64StrtoByary(townshiplogo);					
					String topath=rb.getString("external.uploadfile.township.webpath")+townshipid;
					if(isdelete!=null && isdelete>0){
						File dir = new File(topath);
						FileUtils.deleteDirectory(dir);
						Commonutility.crdDir(topath);
					}
					String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, logoImageFileName);	
					String limgSourcePath=rb.getString("external.uploadfile.township.webpath")+townshipid+"/"+logoImageFileName;					
					String limgDisPath = rb.getString("external.uploadfile.township.mobilepath")+townshipid+"/";		 		 	
					String limgName = FilenameUtils.getBaseName(logoImageFileName);
	 	    	 	String limageFomat = FilenameUtils.getExtension(logoImageFileName);		 	    	 
	 	    	 	String limageFor = "all";
	 	    	 	int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
	 	    	 	int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));		
	        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
					 */
				}
				
		    	 if(icoImageFileName!=null && icoImageFileName!="" && townshipid>0){	
		    		 	icoImageFileName = icoImageFileName.replaceAll(" ", "_");		    		 	
		    		 	String pWebimgpath = rb.getString("external.uploadfile.township.webpath");
		    		 	Commonutility.toWriteConsole("File Upload Start");	
						File lvrimgSrchPathFileobj = new File(lvrTShipicoimgsrchpth);// source path
						Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
						File lvrFileToCreate = new File (pWebimgpath+townshipid+"/",icoImageFileName);  
						Commonutility.toWriteConsole("lvrFileToCreate : "+lvrimgSrchPathFileobj.getAbsolutePath());						
				        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file 
		    		 
		    		 /*
				        conbytetoarr1 = Commonutility.toDecodeB64StrtoByary(townshipico);
						String topath1 = rb.getString("external.uploadfile.township.webpath")+townshipid;
						if(isdelete!=null && isdelete>0){
							File dir = new File(topath1);
							FileUtils.deleteDirectory(dir);
							Commonutility.crdDir(topath1);
						}
						String wrtsts1=Commonutility.toByteArraytoWriteFiles(conbytetoarr1, topath1, icoImageFileName);	
						*/												
					}
		    	 
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("datalst", locObjDoclst);	
				
				serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6432, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6432","1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6432, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6432","1","E0001","Request values are empty",locObjRspdataJson);
			}	
		}catch(Exception e){
			System.out.println("Exception found AddNewProfileImages.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6432, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6432","2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
		}finally{
			locObjRecvJson=null;locObjRspdataJson=null;	//locObjRecvdataJson =null;
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	

}
