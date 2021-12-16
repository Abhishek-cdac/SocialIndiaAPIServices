package com.pack.complaints;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintsvo.persistence.ComplaintsDao;
import com.pack.complaintsvo.persistence.ComplaintsDaoservice;
import com.pack.enews.EeNewsupdates;
import com.pack.tender.Tenderutility;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.common.CommonUtilsServices;
import com.siservices.townShipMgmt.townShipMgmtDao;
import com.siservices.townShipMgmt.townShipMgmtDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
public class Complaintsimageupdate extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Finaly we do
	//struts.xml
	//hbm.xml
	//cgf.xml
	//getter and setter
	// Use for Select All or General service receive 
	private String ivrparams;
	private String ivrservicecode;
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;	
		String ivrservicecode=null;
		String lvrImagename = null;
		String lvrImagedatstr = null;
		String Ivrimagedeleteflg=null,Ivrcmplaintid=null;
		ComplaintattchTblVO iocCmpltattachObj =null;
		CommonUtils locCommutillObj = null;
		ComplaintsDao CompanyDaoobj=null;
		Session session = HibernateUtil.getSession();
		
		try{
			locCommutillObj = new CommonUtilsDao();
			CompanyDaoobj = new ComplaintsDaoservice();
			mobiCommon mobCom=new mobiCommon();
			 townShipMgmtDao townShip=new townShipMgmtDaoServices();
			 ComplaintattchTblVO compliantsAttachMst=new ComplaintattchTblVO();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);		
					/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"));
					fileUploadFileName.add("Chrysanthemum.jpg");
					fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
					fileUploadFileName.add("Wildlife.wmv");
					fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.jpg"));
					fileUploadFileName.add("Wildlife.jpeg");*/
					/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Desert.jpg"));
					fileUploadFileName.add("Desert.jpg");*/
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");				
					lvrImagename = (String)Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileName");				
					//lvrImagedataarry = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageDetail");	
					lvrImagedatstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imgsrchpth");
					Ivrimagedeleteflg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "isdeleteImage");				
					Ivrcmplaintid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "cmplaintid");	
					String iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile"); 
					//Response call
					String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.webpath");
					String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.complaintfldr")+getText("external.inner.mobilepath");		
					 HashMap<String,File> videoMap=new HashMap<String,File>();
					 HashMap<String,File> imageMap=new HashMap<String,File>();
					System.out.println("Step2:Imageupload started in ");
					try {
						Date date1;
						CommonUtilsServices comutil = new CommonUtilsServices();
						date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
						String limageFor = "all";
						int lneedWidth = 200;
						int lneedHeight = 180;
						int filetype;
						String limgName = FilenameUtils.getBaseName(lvrImagename);
						if(limgName==null || limgName.equalsIgnoreCase("null")) {
							limgName ="";
						}
						String limageFomat = FilenameUtils.getExtension(lvrImagename);
						if(limageFomat==null || limageFomat.equalsIgnoreCase("null")) {
							limageFomat ="";
						}
						System.out.println("limageFomat==== "+limageFomat);
						if(limageFomat.equalsIgnoreCase("jpeg")||limageFomat.equalsIgnoreCase("jpg")||limageFomat.equalsIgnoreCase("png")||limageFomat.equalsIgnoreCase("gif")||limageFomat.equalsIgnoreCase("tiff") || limageFomat.equalsIgnoreCase("bmp") ){
							filetype=1;
						} else if(limageFomat.equalsIgnoreCase("mp4")||limageFomat.equalsIgnoreCase("mkv")||limageFomat.equalsIgnoreCase("3gb")||limageFomat.equalsIgnoreCase("avi")||limageFomat.equalsIgnoreCase("mng") || limageFomat.equalsIgnoreCase("asf")||limageFomat.equalsIgnoreCase("mov")||limageFomat.equalsIgnoreCase("flv")) {
							filetype=2;
						} else {
							filetype=3;
						}						
						if (iswebmobilefla != null && iswebmobilefla.equalsIgnoreCase("1")) {
							if (fileUpload.size()>0){
								for (int i = 0; i < fileUpload.size(); i++) {
									File uploadedFile1 = fileUpload.get(i);
									String fileName1 = fileUploadFileName.get(i);
									String limgName1 = FilenameUtils.getBaseName(fileName1);
									String limageFomat1 = FilenameUtils.getExtension(fileName1);
									Integer fileType = mobCom.getFileExtensionType(limageFomat1);
									String sqlQry = "SELECT max(ivrBnATTCH_ID)+1 FROM ComplaintattchTblVO ";
									int attach_count = townShip.getInitTotal(sqlQry);
									System.out.println("===attach_count=="+ attach_count);
									if (fileType == 2) {
										videoMap.put(limgName1 + "_" + attach_count + "." + limageFomat1, uploadedFile1);
									} else {
										imageMap.put(limgName1 + "_" + attach_count + "." + limageFomat1, uploadedFile1);
									}
								}
								if(videoMap.size()>0){
			 		        	 for (Entry<String, File> entry : videoMap.entrySet()) {
			 		        		 compliantsAttachMst=new ComplaintattchTblVO();
			 		        		   compliantsAttachMst.setIvrBnCOMPLAINTS_ID(Integer.parseInt(Ivrcmplaintid));
			 		    	           compliantsAttachMst.setIvrBnSTATUS(1);
			 		    	           compliantsAttachMst.setIvrBnENTRY_DATETIME(date1);
			 		        		    System.out.println("Key = " + entry.getKey());
			 		        		    System.out.println("Value = " + entry.getValue());
			 		        		    String mapFileName=entry.getKey();
			 		        		    String mapVideoName = FilenameUtils.getBaseName(mapFileName);
			 			 		 	 	compliantsAttachMst.setIvrBnFILE_TYPE(2);   
			 			 		 	 	compliantsAttachMst.setIvrBnATTACH_NAME(mapFileName);
			 			 		 	 	String destPath =getText("external.imagesfldr.path")+"complaint/videos/"+Ivrcmplaintid;			 			 		 	 	
			 		  	        		   File destFile  = new File(destPath, mapFileName);
			 		  		       	    	FileUtils.copyFile( entry.getValue(), destFile);
			 		  		       	    	compliantsAttachMst.setThumbImage(mapVideoName+".jpg");
			 		  		       	    	if(imageMap.containsKey(mapVideoName+".jpeg")){
			 		  		       	   File ImageFileName=imageMap.get(mapVideoName+".jpeg");
			 		  		       	String destPathThumb =getText("external.imagesfldr.path")+"complaint/thumbimage/"+Ivrcmplaintid;
			 		        		   File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
			 			       	    	FileUtils.copyFile(ImageFileName, destFileThumb);
			 			       	    	System.out.println("=============g=======================");
			 			       	    	imageMap.remove(mapVideoName+".jpeg");
			 		  		       	    	}
			 			       	     session.save(compliantsAttachMst);
			 		        	 }
			 		        	 	System.out.println("======sssssssssssssss after=====imageMap=====size======"+imageMap.size());
			 		        	 }
				        	
			 		 		if(imageMap.size()>0){
			 		        	 for (Entry<String, File> imageentry : imageMap.entrySet()) {
			 		        		 compliantsAttachMst=new ComplaintattchTblVO();
			 		        		   compliantsAttachMst.setIvrBnCOMPLAINTS_ID(Integer.parseInt(Ivrcmplaintid));
			 		    	           compliantsAttachMst.setIvrBnSTATUS(1);
			 		    	           compliantsAttachMst.setIvrBnENTRY_DATETIME(date1);
			 		        		    System.out.println("Key = " + imageentry.getKey());
			 		        		    System.out.println("Value = " + imageentry.getValue());
			 		        		    String ImageFileName=imageentry.getKey();
			 		        		    String mapVideoName = FilenameUtils.getBaseName(ImageFileName);
			 		        			String limageFomatmobi = FilenameUtils.getExtension(ImageFileName);  
			 		        		    Integer fileType=mobCom.getFileExtensionType(limageFomatmobi);
			 		        		    File ImageFilePath=imageMap.get(ImageFileName);
			 			        		int lneedWidthmobi=0,lneedHeightmobi = 0;
			 			        		 if(fileType==1){
			 			        			   System.out.println("==ImageFileName=="+ImageFileName);
			 			        			   compliantsAttachMst.setIvrBnFILE_TYPE(1); 
			 			        			   compliantsAttachMst.setIvrBnATTACH_NAME(ImageFileName);
			 			        			   int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
			 				        		   int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
			 				        		   String imageHeightPro=getText("thump.img.height");
			 				        		   String imageWidthPro=getText("thump.img.width");
			 				        		   String destPath =getText("external.imagesfldr.path")+"complaint/web/"+Ivrcmplaintid;
			 		    	        		   File destFile  = new File(destPath, ImageFileName);
			 		    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
			 				        			   if(imgHeight>Integer.parseInt(imageHeightPro)){
			 				        				  lneedHeightmobi = Integer.parseInt(imageHeightPro);	
			 				    	        		 //mobile - small image
			 				        			   }else{
			 				        				  lneedHeightmobi = imgHeight;	  
			 				        			   }
			 				        			   if(imgwidth>Integer.parseInt(imageWidthPro)){
			 				        				  lneedWidthmobi = Integer.parseInt(imageWidthPro);	  
			 				        			   }else{
			 				        				  lneedWidthmobi = imgwidth;
			 				        			   }
			 				        			   String limgSourcePath=getText("external.imagesfldr.path")+"complaint/web/"+Ivrcmplaintid+"/"+ImageFileName;
			 			    		 		 		String limgDisPath = getText("external.imagesfldr.path")+"complaint/mobile/"+Ivrcmplaintid;
			 			    		 		 	 	String limgName1 = FilenameUtils.getBaseName(ImageFileName);
			 			    		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
			 			    		 	    	 	String limageFormobi = "all";
			 			    		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFormobi, lneedWidthmobi, lneedHeightmobi);// 160, 130 is best for mobile
			 			        		   }else{
			 			        			   compliantsAttachMst.setIvrBnFILE_TYPE(3); 
			 			        			   compliantsAttachMst.setIvrBnATTACH_NAME(ImageFileName);
			 			        			   String destPath =getText("external.imagesfldr.path")+"complaint/mobile/"+Ivrcmplaintid;
			 		    	        		   File destFile  = new File(destPath, ImageFileName);
			 		    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
			 		    		       	     String otherdest =getText("external.imagesfldr.path")+"complaint/web/"+Ivrcmplaintid;
			 		    		       	     File destFileOther  = new File(otherdest, ImageFileName);
			 		  		       	    	FileUtils.copyFile(ImageFilePath, destFileOther);
			 			        		   }
			 			        		 session.save(compliantsAttachMst);
			 		        	 }
			 		        	 }
							}
						} else {
							System.out.println("web image upload>>>>>>>>");
							if (Commonutility.checkempty(lvrImagename)) {
								lvrImagename = lvrImagename.replaceAll(" ", "");
							}
							String imagename = (String) lvrImagename;
							String imageData = (String) lvrImagedatstr;
							//Commonutility.toWriteImgWebAndMob(Integer.parseInt(Ivrcmplaintid), imagename, imageData, locWebImgFldrPath, locMobImgFldrPath,getText("thump.img.width"),getText("thump.img.height"), log, Commonutility.class);
							Commonutility.toWriteImageMobWebNewUtill(Integer.parseInt(Ivrcmplaintid), imagename,imageData,locWebImgFldrPath,locMobImgFldrPath,getText("thump.img.width"),getText("thump.img.height"), log, Commonutility.class);
						
							iocCmpltattachObj=new ComplaintattchTblVO();						 
							iocCmpltattachObj.setIvrBnCOMPLAINTS_ID(Integer.parseInt(Ivrcmplaintid));
							iocCmpltattachObj.setIvrBnATTACH_NAME(lvrImagename);
							iocCmpltattachObj.setIvrBnFILE_TYPE(filetype);
							iocCmpltattachObj.setIvrBnSTATUS(1);						
							iocCmpltattachObj.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							int locdocinsrtrst = CompanyDaoobj.savecmpltattachfile(iocCmpltattachObj);
							System.out.println("return imageattach_id::: "+locdocinsrtrst);		
						}	 	 
			}catch(Exception e){
				System.out.println("Exception in  image write on event insert : "+e);
				 log.logMessage("step -2 : Exception in Image write on event insert", "info", Complaintstutility.class);
			}finally{
				CompanyDaoobj=null;
			}
				
				}else{
					//Response call
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", EeNewsupdates.class);
					serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				//Response call
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI11001,"+getText("request.format.notmach")+"", "info", EeNewsupdates.class);
				serverResponse("SI11001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
			}	
		}catch(Exception e){
			System.out.println("Exception found "+getClass().getSimpleName()+".class execute() Method : "+e);
		}finally{			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
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
