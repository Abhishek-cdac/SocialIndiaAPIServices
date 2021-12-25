package com.mobile.profile;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;

public class profileDetails extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	profileDao profile=new profileDaoServices();
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	public String execute(){
		
		System.out.println("************profileDetails services******************");
		
		//fileUpload=new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg");
		//fileUploadFileName="Chrysanthemum.jpg";
		
		JSONObject json = new JSONObject();
		profileValidateUtill profileUtill=new profileValidateUtill();
		Integer otpcount=0;
		UserMasterTblVo userInfo=new UserMasterTblVo();
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String servicecode="";
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("======ivrparams========"+ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
		 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
		locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
		
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
		
		if (locObjRecvdataJson !=null) {
			if (Commonutility.checkempty(rid)) {
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
			}
		}
		
		if(flg){
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			System.out.println("********result*****************"+result);
			if(result==true){
				
				if(fileUpload!=null){
				int lneedWidth=0,lneedHeight = 0;
				//String destPath =getText("external.path")+"PostData/";
				String destPath =getText("external.imagesfldr.path")+"profile/web/"+rid;
				System.out.println("destPath----------"+destPath);
				 int imgHeight=mobiCommon.getImageHeight(fileUpload);
        		   int imgwidth=mobiCommon.getImageWidth(fileUpload);
        		   System.out.println("imgHeight------"+imgHeight);
        		   System.out.println("imgwidth-----------"+imgwidth);
				 String imageHeightPro=getText("thump.img.height");
        		   String imageWidthPro=getText("thump.img.width");
        		   File destFile  = new File(destPath, fileUploadFileName);
        		   
	       	    	FileUtils.copyFile(fileUpload, destFile);
	       	    	
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
    			String limgSourcePath=getText("external.imagesfldr.path")+"profile/web/"+rid+"/"+fileUploadFileName;
	 		 		String limgDisPath = getText("external.imagesfldr.path")+"profile/mobile/"+rid;
	 		 	 	String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
	 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);
	 		 	  String imagesmallheight=getText("thump.img.small.height");
       		   String imagesmallwidth=getText("thump.img.small.width");
	 		 	 	
    			   String limageFor = "all";
	        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
	        		
	        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1+"_small", limageFomat1, limageFor, Integer.parseInt(imagesmallheight), Integer.parseInt(imagesmallwidth));// 160, 130 is best for mobile
				boolean updateImage=profile.updateImageName(fileUploadFileName,rid);
				}
	        		
	        		userInfo=profile.getUserPrfileInfo(rid);	
				System.out.println("========userInfo===="+userInfo);
				if(userInfo!=null){
					
					 locObjRspdataJson=new JSONObject();
					/*if(userInfo.getImageNameWeb()!=null && !userInfo.getImageNameWeb().equalsIgnoreCase("")){
						locObjRspdataJson.put("profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/web/"+userInfo.getUserId()+"/"+userInfo.getImageNameWeb());
						
						}else{
							locObjRspdataJson.put("profilepic", "");
						}*/
					if(userInfo.getImageNameMobile()!=null && !userInfo.getImageNameMobile().equalsIgnoreCase("")){
						String imageName = FilenameUtils.getBaseName(userInfo.getImageNameMobile());
						String limageFomat = FilenameUtils.getExtension(userInfo.getImageNameMobile());
						locObjRspdataJson.put("profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userInfo.getUserId()+"/"+imageName+"_small."+limageFomat);
						locObjRspdataJson.put("profilepic", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+userInfo.getUserId()+"/"+userInfo.getImageNameMobile());
					}else{
						locObjRspdataJson.put("profilepic_thumbnail", "");	
						locObjRspdataJson.put("profilepic", "");
					}
					locObjRspdataJson.put("rid",rid);
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);		
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
			log.logMessage("Service code : 0011, Request values are not correct format", "info", profileDetails.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======profile details ====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, =profile details  an unhandled error occurred", "info", profileDetails.class);
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
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			System.out.println("========as===final==="+as);
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


	public File getFileUpload() {
		return fileUpload;
	}


	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}


	public String getFileUploadContentType() {
		return fileUploadContentType;
	}


	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}


	public String getFileUploadFileName() {
		return fileUploadFileName;
	}


	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	
}
