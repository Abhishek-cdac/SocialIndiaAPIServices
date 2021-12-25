package com.mobi.event;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.complaints.CompliantPost;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class EventFileattachment extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	otpDao otp=new otpDaoService();
	profileDao profile=new profileDaoServices();
	UserMasterTblVo userMst=new UserMasterTblVo();
	GroupMasterTblVo groupMst=new GroupMasterTblVo();
	public String execute(){
		
		
		/*fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"));
		fileUploadFileName.add("Chrysanthemum.jpg");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
		fileUploadFileName.add("Wildlife.wmv");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.jpg"));
		fileUploadFileName.add("Wildlife.jpg");
		fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Desert.jpg"));
		fileUploadFileName.add("Desert.jpg");*/
		System.out.println("********Event File attachment****************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		JSONArray jsonarr = null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			//ivrparams = URLDecoder.decode(ivrparams, "UTF-8");
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "title");
				String desc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "desc");
				String post_to = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "post_to");
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
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			
				boolean upResult=true;
				//boolean upResult=complains.compliantsPostInsert(compliantMst,fileUpload,fileUploadContentType,fileUploadFileName);
				
				if(upResult==true){
				
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0020",getText("R0020"),locObjRspdataJson);
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
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", CompliantPost.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======CompliantPost====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, familyMemberLoginInvite an unhandled error occurred", "info", CompliantPost.class);
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


	public Log getLog() {
		return log;
	}


	public void setLog(Log log) {
		this.log = log;
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


	public profileDao getProfile() {
		return profile;
	}


	public void setProfile(profileDao profile) {
		this.profile = profile;
	}


	public UserMasterTblVo getUserMst() {
		return userMst;
	}


	public void setUserMst(UserMasterTblVo userMst) {
		this.userMst = userMst;
	}


	public GroupMasterTblVo getGroupMst() {
		return groupMst;
	}


	public void setGroupMst(GroupMasterTblVo groupMst) {
		this.groupMst = groupMst;
	}


	

	
}
