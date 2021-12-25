package com.mobi.evoting;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.carpooling.SearchCarPoolList;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpEvotingImageTbl;
import com.socialindiaservices.vo.MvpEvotingMstTbl;

public class EvotingCreation extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	EvotingDao EvotingHbm=new EvotingDaoServices();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();
	
	public String execute(){
		
		System.out.println("************mobile Evoting Creation services******************");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"));
		fileUploadFileName.add("Chrysanthemum.jpg");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
		fileUploadFileName.add("Wildlife.wmv");*/

		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("====ivrparams====="+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				JSONArray removeAttachJsnarrObj = new JSONArray();
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "title");
				String description = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "desc");
				String addEdit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "add_edit");
				String evotingId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "evoting_id");
				removeAttachJsnarrObj = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"remove_attach");
				
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
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				System.out.println("userMst------------"+userMst);
				CommonUtils comutil=new CommonUtilsServices();
				Date date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				MvpEvotingMstTbl evotingmstobj=new MvpEvotingMstTbl();
				if(addEdit!=null && addEdit.equalsIgnoreCase("1")){
					//Add block
					System.out.println("111111111");
				evotingmstobj.setUsrRegTbl(userMst);
				evotingmstobj.setTitile(title);
				evotingmstobj.setDescription(description);
				evotingmstobj.setStatusFlag(1);
				evotingmstobj.setPublishFlag(1);
				evotingmstobj.setEntryDatetime(date);
				evotingmstobj.setModifyDatetime(date);
				System.out.println("222222222222222");
				evotingmstobj=EvotingHbm.insertEvotingMstTbl(evotingmstobj);
				if(evotingmstobj!=null ){
					
					if(fileUpload.size()>0){
						 mobiCommon mobCom=new mobiCommon();
						 MvpEvotingImageTbl evotingImgObj=new MvpEvotingImageTbl();
				    	 for(int i=0;i<fileUpload.size();i++){
				        		
			        		 File uploadedFile = fileUpload.get(i);
			        	 String fileName = fileUploadFileName.get(i);
			        	 System.out.println("uploadedFile--------------"+uploadedFile.getPath());
			        	 System.out.println("uploadedFile---------------"+uploadedFile.toString());
			        	 System.out.println("fileName---------------"+fileName);
			           String limgName = FilenameUtils.getBaseName(fileName);
			 		 	 	String limageFomat = FilenameUtils.getExtension(fileName);
			 		 	  Integer fileType=mobCom.getFileExtensionType(limageFomat);
			 		 	evotingImgObj=new MvpEvotingImageTbl();
			 		 	evotingImgObj.setMvpEvotingMstTbl(evotingmstobj);
			 		 	evotingImgObj.setImageName(fileName);
			 		 	evotingImgObj.setFileType(fileType);
			 		 	evotingImgObj.setStatusFlag(1);
			 		 	evotingImgObj.setEntryDatetime(date);
			 		 	evotingImgObj.setModifyDatetime(date);
			 		 	evotingImgObj=EvotingHbm.insertEvotingImageTbl(evotingImgObj);
			 		 	
			 			String destPath =getText("external.imagesfldr.path")+getText("external.evoting.images")+"web/"+evotingmstobj.getEvotingId();
			 			String limgDisPath = getText("external.imagesfldr.path")+getText("external.evoting.images")+"mobile/"+evotingmstobj.getEvotingId();
			 	/*		int lneedWidth=0,lneedHeight = 0;
			 			int imgHeight=mobiCommon.getImageHeight(uploadedFile);
		        		   int imgwidth=mobiCommon.getImageWidth(uploadedFile);
		        		   System.out.println("imgHeight------"+imgHeight);
		        		   System.out.println("imgwidth-----------"+imgwidth);
		        		   String imageHeightPro=getText("thump.img.height");
		        		   String imageWidthPro=getText("thump.img.width");*/
		        		   File destFile  = new File(destPath, fileName);
		        		   File destmobilFile  = new File(limgDisPath, fileName);
		        		   
		        		   FileUtils.copyFile(uploadedFile, destFile);
		        		   FileUtils.copyFile(uploadedFile, destmobilFile);
		        		   
		        			/*
		        		   if(imgHeight>Integer.parseInt(imageHeightPro)){
	        				   lneedHeight = Integer.parseInt(imageHeightPro);	
	    	        		 //mobile - small image
	        			   }else{
	        				   lneedHeight = imgHeight;	  
	        			   }
	        			   if(imgwidth>Integer.parseInt(imageWidthPro)){
	        				   lneedWidth = Integer.parseInt(imageWidthPro);	  
	        			   }else{
	        				   lneedWidth = imgwidth;
	        			   }
	        			   System.out.println("lneedHeight-----------"+lneedHeight);
	        			   System.out.println("lneedWidth-------------"+lneedWidth);
	        			String limgSourcePath=getText("external.imagesfldr.path")+getText("external.evoting.images")+"/web/"+evotingmstobj.getEvotingId()+"/"+fileName;
   		 		 		String limgDisPath = getText("external.imagesfldr.path")+getText("external.evoting.images")+"mobile/"+evotingmstobj.getEvotingId();
   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileName);
   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileName);
		        		   
		        		   String limageFor = "all";
	   		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
			 		*/
			        	 }
						
					}
					
				serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}
				
			}else{
				//Edit block
				System.out.println("evotingId------------------"+evotingId);
				evotingmstobj.setEvotingId(Integer.parseInt(evotingId));
				evotingmstobj.setUsrRegTbl(userMst);
				evotingmstobj.setTitile(title);
				evotingmstobj.setDescription(description);
				evotingmstobj.setStatusFlag(1);
				evotingmstobj.setPublishFlag(1);
				evotingmstobj.setEntryDatetime(date);
				evotingmstobj.setModifyDatetime(date);
				evotingmstobj.setModifyBy(userMst);
				boolean isupdate=EvotingHbm.updateEvotingMstTbl(evotingmstobj);
				if(isupdate){
					
					if (removeAttachJsnarrObj != null) {
					FunctionUtility commonutil = new FunctionUtilityServices();
					String removeAttachIds = commonutil.jsnArryIntoStrBasedOnComma(removeAttachJsnarrObj);
					if (Commonutility.checkempty(removeAttachIds)) {
						if (!removeAttachIds.contains(",")) {
							removeAttachIds += ",";
						}
						if (removeAttachIds.contains(",")) {
							String[] removeIds = removeAttachIds.split(",");
							for (int j = 0; j < removeIds.length; j++) {
								String attchId = removeIds[j];
								if (Commonutility.checkempty(attchId)
										&& Commonutility.toCheckisNumeric(attchId)) {
									log.logMessage("Enter into AttachmentDetails attachId: "+ Commonutility.stringToInteger(attchId),"info", EvotingCreation.class);
									 MvpEvotingImageTbl evotingobj=new MvpEvotingImageTbl();
									String qry = " from MvpEvotingImageTbl where statusFlag='1'  and  mvpEvotingMstTbl.evotingId='"
											+ evotingmstobj.getEvotingId() + "' and evotingImageId='"+attchId+"'";
									evotingobj=EvotingHbm.selectEvotingImageTblbyQuery(qry);
									// FeedattchTblVO attachDelObj =
									// commonServ.getAttachmentDetails(Commonutility.stringToInteger(attchId));
									if (evotingobj != null) {
										File deleteFile = null;
										File deleteFileTwo = null;
											String imagePathWeb = getText("external.imagesfldr.path")+ getText("external.evoting.images")+ "web/"+evotingmstobj.getEvotingId()+"/"+evotingobj.getImageName();
											String imagePathMobi =getText("external.imagesfldr.path")+ getText("external.evoting.images")+ "mobile/"+evotingmstobj.getEvotingId()+"/"+evotingobj.getImageName();
											System.out.println("imagePathWeb=-----------"+imagePathWeb);
											System.out.println("imagePathMobi------------"+imagePathMobi);
											deleteFile = new File(imagePathWeb);
											deleteFileTwo = new File(imagePathMobi);
										try {
											if (deleteFile != null
													&& deleteFile.exists()) {
												FileUtils.forceDelete(deleteFile);
											}
											if (deleteFileTwo != null
													&& deleteFileTwo.exists()) {
												FileUtils
														.forceDelete(deleteFileTwo);
											}
										} catch (Exception ex) {
											log.logMessage("feedEdit Exception found in file delete filename="
															+ evotingobj.getImageName() + " :: "
															+ ex, "error",
															EvotingCreation.class);
										}
										String query = "delete MvpEvotingImageTbl where mvpEvotingMstTbl.evotingId='"+ evotingmstobj.getEvotingId() + "'";
										EvotingHbm.updateEvotingMstTblbyQuery(query);
									}
								}
							}
					}
					}
					
					if(fileUpload.size()>0){
						 mobiCommon mobCom=new mobiCommon();
						 MvpEvotingImageTbl evotingImgObj=new MvpEvotingImageTbl();
				    	 for(int i=0;i<fileUpload.size();i++){
				        		
			        		 File uploadedFile = fileUpload.get(i);
			        	 String fileName = fileUploadFileName.get(i);
			           String limgName = FilenameUtils.getBaseName(fileName);
			 		 	 	String limageFomat = FilenameUtils.getExtension(fileName);
			 		 	  Integer fileType=mobCom.getFileExtensionType(limageFomat);
			 		 	evotingImgObj=new MvpEvotingImageTbl();
			 		 	evotingImgObj.setMvpEvotingMstTbl(evotingmstobj);
			 		 	evotingImgObj.setImageName(fileName);
			 		 	evotingImgObj.setStatusFlag(1);
			 		 	evotingImgObj.setEntryDatetime(date);
			 		 	evotingImgObj.setModifyDatetime(date);
			 		 	evotingImgObj=EvotingHbm.insertEvotingImageTbl(evotingImgObj);
			 			String destPath =getText("external.imagesfldr.path")+getText("external.evoting.images")+"web/"+evotingmstobj.getEvotingId();
			 			String limgDisPath = getText("external.imagesfldr.path")+getText("external.evoting.images")+"mobile/"+evotingmstobj.getEvotingId();
			 	/*		int lneedWidth=0,lneedHeight = 0;
			 			int imgHeight=mobiCommon.getImageHeight(uploadedFile);
		        		   int imgwidth=mobiCommon.getImageWidth(uploadedFile);
		        		   System.out.println("imgHeight------"+imgHeight);
		        		   System.out.println("imgwidth-----------"+imgwidth);
		        		   String imageHeightPro=getText("thump.img.height");
		        		   String imageWidthPro=getText("thump.img.width");*/
		        		   
		        		   
		        		   File destFile  = new File(destPath, fileName);
		        		   File destmobilFile  = new File(limgDisPath, fileName);
		        		   
		        		   FileUtils.copyFile(uploadedFile, destFile);
		        		   FileUtils.copyFile(uploadedFile, destmobilFile);
		        		   
		        			
		        		  /* if(imgHeight>Integer.parseInt(imageHeightPro)){
	        				   lneedHeight = Integer.parseInt(imageHeightPro);	
	    	        		 //mobile - small image
	        			   }else{
	        				   lneedHeight = imgHeight;	  
	        			   }
	        			   if(imgwidth>Integer.parseInt(imageWidthPro)){
	        				   lneedWidth = Integer.parseInt(imageWidthPro);	  
	        			   }else{
	        				   lneedWidth = imgwidth;
	        			   }
	        			   System.out.println("lneedHeight-----------"+lneedHeight);
	        			   System.out.println("lneedWidth-------------"+lneedWidth);
	        			String limgSourcePath=getText("external.imagesfldr.path")+getText("external.evoting.images")+"/web/"+evotingmstobj.getEvotingId()+"/"+fileName;
   		 		 		String limgDisPath = getText("external.imagesfldr.path")+getText("external.evoting.images")+"mobile/"+evotingmstobj.getEvotingId()+"/"+fileName;
   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileName);
   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileName);
		        		   
		        		   String limageFor = "all";
	   		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
*/			 		
			        	 }
						
					}
					
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					}
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
				}
				
				
				
			}
				
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", SearchCarPoolList.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======Evoting Creation====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, EvotingCreation an unhandled error occurred", "info", SearchCarPoolList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
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