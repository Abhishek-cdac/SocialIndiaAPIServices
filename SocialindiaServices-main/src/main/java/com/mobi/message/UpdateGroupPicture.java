package com.mobi.message;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feed.FeedFileUpload;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.messagevo.GroupChatMstrTblVO;
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class UpdateGroupPicture extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	Log log= new Log();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int groupId = 0;
			String groupName = null;
			String groupImage = null;
			log.logMessage("Enter into UpdateGroupDetails ", "info", UpdateGroupPicture.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (Commonutility.checkempty(ivrservicecode)) {
						if (ivrservicecode.length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
							
						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.error")));
					}					
					if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					
					if (Commonutility.checkempty(societyKey)) {
						if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {
							
						} else {
							String[] passData = { getText("societykey.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}				
					
					if (locObjRecvdataJson !=null) {
						String locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						String locGroupId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_id");
//						groupName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_name");
//						groupImage = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_image");
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
							if (rid !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}												
						if (Commonutility.checkempty(locGroupId) && Commonutility.toCheckisNumeric(locGroupId)) {
							groupId = Commonutility.stringToInteger(locGroupId);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.id.error")));
						}
				/*		if (Commonutility.checkempty(groupName)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.name.erro")));
						}	*/					
					/*	if (Commonutility.checkempty(groupImage)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.image.error")));
						}*/
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				}else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("flg :" +flg, "info", UpdateGroupPicture.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						try {			    	
					    	String desWeb = getText("external.uploadfile.grp.img.webpath") + rid + "/";
							String desMobi = getText("external.uploadfile.grp.img.mobilepath") + rid + "/";
							log.logMessage("desWeb: " + desWeb, "info", CreateGroup.class);
							log.logMessage("desMobi: " + desMobi, "info", CreateGroup.class);
							log.logMessage("fileUploadFileName: " + fileUploadFileName, "info", UpdateGroupPicture.class);
							File destFile  = new File(desWeb, fileUploadFileName);
							File destFilemobi  = new File(desMobi, fileUploadFileName);
					    	FileUtils.copyFile(fileUpload, destFile);
					    	FileUtils.copyFile(fileUpload, destFilemobi);
					    	System.out.println("Group image File upload complete");
					    	groupImage = fileUploadFileName;
					    	// for mobile, compress image upload
							int imgWitdh = 0;
							int imgHeight = 0;
							int imgOriginalWidth = mobiCommon.getImageWidth(destFile);
							int imgOriginalHeight = mobiCommon.getImageHeight(destFile);
							String imageHeightPro=getText("thump.img.height");
			        		String imageWidthPro=getText("thump.img.width");
							if (imgOriginalWidth>Commonutility.stringToInteger(imageWidthPro)) {
								imgWitdh = Commonutility.stringToInteger(imageWidthPro);
							} else {
								imgWitdh = imgOriginalWidth;
							}
							if (imgOriginalHeight>Commonutility.stringToInteger(imageHeightPro)) {
								imgHeight = Commonutility.stringToInteger(imageHeightPro);
							} else {
								imgHeight = imgOriginalHeight;
							}
							String imgSourcePath = desWeb + groupImage;
    		 		 	 	String imgName = FilenameUtils.getBaseName(groupImage);
    		 		 	 	String imageFomat = FilenameUtils.getExtension(groupImage);		 	    			 	    	 
    		 	    	 	String imageFor = "all";
							log.logMessage("imgOriginalWidth=" + imgOriginalWidth + ":imgOriginalHeight=" + imgOriginalHeight , "info", UpdateGroupPicture.class);
							log.logMessage("imageName for web compress : " + groupImage, "info", UpdateGroupPicture.class);
    		 	    	 	ImageCompress.toCompressImage(imgSourcePath, desMobi, imgName, imageFomat, imageFor, imgWitdh, imgHeight);	
							log.logMessage("File upload complete  " , "info", UpdateGroupPicture.class);
						} catch (Exception ex) {
							flg = false;
							log.logMessage("Exception found in : " + ex, "error", UpdateGroupPicture.class);
						}
						if (flg) {
							GroupChatMstrTblVO groupChatMstrObj = new GroupChatMstrTblVO();
							UserMasterTblVo usrMasterObj = new UserMasterTblVo();
							MessageDAO msgService = new MessageDAOService();
							usrMasterObj.setUserId(rid);
							groupChatMstrObj.setCreaterId(usrMasterObj);
							groupChatMstrObj.setGrpChatId(groupId);
							groupChatMstrObj.setGrpImageWeb(groupImage);
							groupChatMstrObj.setGrpImageMobile(groupImage);					
							flg = msgService.updateGroupPicture(groupChatMstrObj);
						}
						if (flg) {
							serverResponse(ivrservicecode,getText("status.success"),"R0045",mobiCommon.getMsg("R0045"),locObjRspdataJson);
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0046",mobiCommon.getMsg("R0046"),locObjRspdataJson);
						}
					} else {
						locObjRspdataJson=new JSONObject();
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);	
					}
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);	
				}	
				
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", UpdateGroupPicture.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
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
