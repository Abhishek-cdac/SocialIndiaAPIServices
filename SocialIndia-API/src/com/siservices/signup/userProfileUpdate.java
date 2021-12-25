package com.siservices.signup;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class userProfileUpdate extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	signUpDao signup = new signUpDaoServices();
	UserMasterTblVo userData = new UserMasterTblVo();
	Log log = new Log();

	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		JSONObject data = new JSONObject();		
		String ivrservicecode=null;
		int userid;
		String occupation=null;
		String cityCode=null,idcardtype=null,countryCode=null,stateCode=null,postalCode=null;
		String address1=null,dob=null,idproofno=null,flatno=null;
		int noofmember,accessMedia;
		String lastname=null,address2=null,bloodtype=null,firstname=null,getnder=null;
		String imageweb=null,profileImageFileName=null,srcimgpath = null;
		int noofflats;
		byte conbytetoarr[]= new byte[1024];
		uamDao uam=new uamDaoServices();
		CommonUtilsDao common=new CommonUtilsDao();		
		ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);									
					locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");					
					ivrservicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
					userid=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userid");
					occupation=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "occupation");
					cityCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "cityCode");
					idcardtype=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "idcardtype");
					countryCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countryCode");
					address1=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "address1");
					dob=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "dob");
					idproofno=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "idproofno");
					
					flatno=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "flatno");
					stateCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "stateCode");
					noofmember=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofmember");
					postalCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "postalCode");
					
					lastname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "lastname");
					address2=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "address2");
					bloodtype=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "bloodtype");
					firstname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "firstname");
					accessMedia=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "accessMedia");
					noofflats=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofflats");
					getnder=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "getnder");
					imageweb=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageweb");
					profileImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "profileImageFileName");	
					srcimgpath = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srcimgpath");	
					if (profileImageFileName!=null && profileImageFileName!="") {
						
						profileImageFileName = profileImageFileName.replaceAll(" ", "_");
						userData.setImageNameWeb(profileImageFileName);
						userData.setImageNameMobile(profileImageFileName);
					}					
					userData.setUserId(userid);
					userData.setFirstName(firstname);	
					userData.setLastName(lastname);
					userData.setDob(dob);
					userData.setBloodType(bloodtype);
					userData.setGender(getnder);
					userData.setIdProofNo(idproofno);
					userData.setFlatNo(flatno);
					userData.setOccupation(occupation);
					userData.setMembersInFamily(noofmember);
					userData.setAddress1(address1);
					userData.setAddress2(address2);
					userData.setAccessChannel(accessMedia);
					userData.setNoofflats(noofflats);					
					boolean status=signup.userProfileUpdate(userData,countryCode,stateCode,cityCode,postalCode,idcardtype);
					locObjRspdataJson.put("servicecode", ivrservicecode);					
					if(profileImageFileName!=null && profileImageFileName!=""){
						
						String lvrWebImagpath = rb.getString("external.uploadfile.webpath");
						String lvrMobImgpath = rb.getString("external.uploadfile.mobilepath");
						String delrs = Commonutility.todelete("", lvrWebImagpath+userid+"/");
						String delrs_mob = Commonutility.todelete("", lvrMobImgpath+userid+"/");
						  
						/*// Testing purpose
						Commonutility.toWriteConsole("File Upload Start");	
						File imgfliii = new File(srcimgpath);
						Commonutility.toWriteConsole("imgfliii : "+imgfliii.getAbsolutePath());
						File fileToCreate = new File(lvrWebImagpath+userid+"/",profileImageFileName);  
						Commonutility.toWriteConsole("fileToCreate : "+fileToCreate.getAbsolutePath());						
				        FileUtils.copyFile(imgfliii, fileToCreate);//copying source file to new file  
				   
				        String limgSourcePath=lvrWebImagpath+userid+"/"+profileImageFileName;									 		 	
			 		 	String limgDisPath = lvrMobImgpath+userid+"/";	
				        String limgName = FilenameUtils.getBaseName(profileImageFileName);
			 		 	String limageFomat = FilenameUtils.getExtension(profileImageFileName);		 		 	
			        	String limageFor = "all";
			        	int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
		        		int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));	
			        	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile				        				       
			        	Commonutility.toWriteConsole("File Upload End");
						*/
				   		Commonutility.toWriteImageMobWebNewUtill(userid,profileImageFileName,srcimgpath,lvrWebImagpath,lvrMobImgpath,getText("thump.img.width"),getText("thump.img.height"), log, getClass());
						
						/*
						conbytetoarr = Commonutility.toDecodeB64StrtoByary(imageweb);					
						String topath = rb.getString("external.uploadfile.webpath") + userid;					
						String delrs = Commonutility.todelete("", topath + "/");	
						String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, profileImageFileName);
						
						String limgSourcePath=rb.getString("external.uploadfile.webpath")+userid+"/"+profileImageFileName;									 		 	
			 		 	String limgDisPath = rb.getString("external.uploadfile.mobilepath")+userid+"/";	
			 		 	String delrs_mob= Commonutility.todelete("", limgDisPath+"/");
			 		 	
			 		 	String limgName = FilenameUtils.getBaseName(profileImageFileName);
			 		 	String limageFomat = FilenameUtils.getExtension(profileImageFileName);		 		 	
			        	String limageFor = "all";
			        	int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
		        		int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));	
			        	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
					*/
					}
					if(status==true){
						userData=uam.getregistertable(userid);
						if(profileImageFileName!=null && profileImageFileName!=""){
							locObjRspdataJson.put("profileimage", userData.getImageNameWeb());
						}else{
							locObjRspdataJson.put("profileimage", "");
						}
						serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);
					
					}else{
						serverResponse(ivrservicecode,"1","E0001","Error in updating",locObjRspdataJson);
					}
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI0024, Request values are empty", "info", userProfileUpdate.class);
				serverResponse(ivrservicecode,"1","E0001","Request values are empty",locObjRspdataJson);
			}
			
			}	
		}catch(Exception e){
			System.out.println("Service code : SI0024, Exception found in Forgetpassword.action execute() Method : "+e);
			log.logMessage("Service code : SI0024, Sorry, an unhandled error occurred", "info", userProfileUpdate.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(ivrservicecode,"2","SI0024","Sorry, an unhandled error occurred",locObjRspdataJson);
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
	
	
}
