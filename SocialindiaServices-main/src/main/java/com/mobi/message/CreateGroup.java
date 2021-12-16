package com.mobi.message;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.feed.FeedFileUpload;
import com.mobi.messagevo.GroupChatMemTblVO;
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
import com.socialindiaservices.common.CommonUtils;

public class CreateGroup extends ActionSupport{
	
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
		//Session locObjsession = null;
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
		//	locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int startLimit = 0;
			String groupName = null;
			int groupId = 0;
			String getMemberUsr = "";
			List<Integer> memListObj = new ArrayList<Integer>();
			log.logMessage("Enter into CreateGroup ", "info", CreateGroup.class);
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
						groupName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_name");
						JSONArray locGroupMember = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"member_id"); // array
						String locGroupId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_id"); // array
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
						if (Commonutility.checkempty(groupName)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.name.error")));
						}
						if (Commonutility.checknull(locGroupId)) {
							groupId = Commonutility.stringToInteger(locGroupId);
							if (Commonutility.checkLengthNotZero(locGroupId)) {
								if (Commonutility.toCheckisNumeric(locGroupId)) {
									
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.id.error")));
								}
							}							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.id.error")));
						}
						if (locGroupMember!=null && locGroupMember.length()>0) {
								if (locGroupMember != null) {
									try {
										for (int i=0;i<locGroupMember.length();i++) {
											getMemberUsr += locGroupMember.getInt(i) + ",";
											int mem = locGroupMember.getInt(i);
											memListObj.add(mem);
											System.out.println("Memeber id :" + mem);
										}
									} catch (Exception ex) {
										log.logMessage(getText("Eex") + ex, "error", CreateGroup.class);
									}
								}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.mem.error")));
						}
						
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
			log.logMessage("flg :" +flg, "info", CreateGroup.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				boolean result=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(result==true){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						String updatStrmsg = "";
						System.out.println("Enter into group create action");
						GroupChatMstrTblVO groupChatMstrObj = new GroupChatMstrTblVO();
						GroupChatMemTblVO groupMemberObj = new GroupChatMemTblVO();
						UserMasterTblVo usrMasterObj = new UserMasterTblVo();
						MessageDAO msgService = new MessageDAOService();
						usrMasterObj.setUserId(rid);
						if ( groupId == 0) {
							groupChatMstrObj.setCreaterId(usrMasterObj);
							groupChatMstrObj.setGrpName(groupName);
							groupChatMstrObj.setStatus(1);
							groupChatMstrObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							groupChatMstrObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							if(fileUploadFileName!=null && fileUploadFileName.length()>0){
								groupChatMstrObj.setGrpImageWeb(fileUploadFileName);
								groupChatMstrObj.setGrpImageMobile(fileUploadFileName);
								}
							
							System.out.println("AF enter to call group create function");
							int grpChantId = msgService.createGroup(groupChatMstrObj);
							System.out.println("BF enter to call group create function");
							int cnt = 0;
							if (grpChantId != -1) {
								
								
								if(fileUploadFileName!=null && fileUploadFileName.length()>0){
									int lneedWidth=0,lneedHeight = 0;
									//String destPath =getText("external.path")+"PostData/";
									CommonUtils commjvm=new CommonUtils();
									
									String destPath =getText("external.imagesfldr.path")+"chat/web/"+grpChantId;
									System.out.println("destPath----------"+destPath);
									commjvm.deleteallFileInDirectory(destPath);
									
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
				        			String limgSourcePath=getText("external.imagesfldr.path")+"chat/web/"+grpChantId+"/"+fileUploadFileName;
			   		 		 		String limgDisPath = getText("external.imagesfldr.path")+"chat/mobile/"+grpChantId;
			   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
			   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);
			   		 		 	 	
				        			   String limageFor = "all";
			   		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
									}
								
								groupChatMstrObj.setGrpChatId(grpChantId);
								groupMemberObj.setGrpChatId(groupChatMstrObj);
								groupMemberObj.setCreaterId(rid);
								groupMemberObj.setStatus(1);
								groupMemberObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
								groupMemberObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
								groupMemberObj.setUsrId(usrMasterObj);						
								try {
									System.out.println("Before memListObj.size()-------------" + memListObj.size());
									memListObj.add(rid);
									System.out.println("memListObj.size()-------------" + memListObj.size());
									for (int i=0;i<memListObj.size();i++) {
										usrMasterObj.setUserId(memListObj.get(i));
										groupMemberObj.setUsrId(usrMasterObj);
										int memId = msgService.insertGroupMember(groupMemberObj);
										if (memId != -1) {
											cnt++;
										} else {
											flg = false;
										}
									}
								} catch(Exception ex) {
									flg = false;
									log.logMessage(getText("Eex") + ":: member insert ::" + ex, "error", CreateGroup.class);
								}
								log.logMessage(":: member insert flg false :: mem size" + memListObj.size() + ":insert count:" + cnt, "info", CreateGroup.class);
								if (cnt != memListObj.size()) {
									flg = false;
								}
								
								//Get group details
								List<Object[]> chatListObj = new ArrayList<Object[]>();
								chatListObj=msgService.getmessageContactsByProc(rid, 3, 0,groupName);
								locObjRspdataJson = msgService.getGroupNameDetails(chatListObj);
								System.out.println("locObjRspdataJson--if----------------->> " + locObjRspdataJson.toString());
							} else {
								flg = false;
							}
						} else {
							groupChatMstrObj.setCreaterId(usrMasterObj);
							groupChatMstrObj.setGrpChatId(groupId);
							groupChatMstrObj.setGrpName(groupName);
							if(fileUploadFileName!=null && fileUploadFileName.length()>0) {
								groupChatMstrObj.setGrpImageWeb(fileUploadFileName);
								groupChatMstrObj.setGrpImageMobile(fileUploadFileName);
							}
							flg = msgService.updateGroupName(groupChatMstrObj);
							if (flg) {
								int cnt = 0;
								if(fileUploadFileName!=null && fileUploadFileName.length()>0){
									int lneedWidth=0,lneedHeight = 0;
									//String destPath =getText("external.path")+"PostData/";
									CommonUtils commjvm=new CommonUtils();
									
									String destPath =getText("external.imagesfldr.path")+"chat/web/"+groupId;
									System.out.println("destPath----------"+destPath);
									commjvm.deleteallFileInDirectory(destPath);
									
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
				        			String limgSourcePath=getText("external.imagesfldr.path")+"chat/web/"+groupId+"/"+fileUploadFileName;
			   		 		 		String limgDisPath = getText("external.imagesfldr.path")+"chat/mobile/"+groupId;
			   		 		 	 	String limgName1 = FilenameUtils.getBaseName(fileUploadFileName);
			   		 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileUploadFileName);
			   		 		 	 	
				        			   String limageFor = "all";
			   		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
									}
								System.out.println("getMemberUsr---before--------------" + getMemberUsr);
								if (Commonutility.checkempty(getMemberUsr)) {
									getMemberUsr = getMemberUsr + rid;
									//getMemberUsr = getMemberUsr.substring(0,getMemberUsr.length()-1);
									System.out.println("getMemberUsr---susbsting--------------" + getMemberUsr);
								}
								System.out.println("getMemberUsr---After--------------" + getMemberUsr);
								String deletQry = "delete from GroupChatMemTblVO where grpChatId.grpChatId='"+groupId+"'";
								int deletCnt = msgService.deleteMemberList(deletQry);
								groupChatMstrObj = new GroupChatMstrTblVO();
								groupChatMstrObj.setGrpChatId(groupId);
								groupMemberObj.setGrpChatId(groupChatMstrObj);
								groupMemberObj.setCreaterId(rid);
								groupMemberObj.setStatus(1);
								groupMemberObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
								groupMemberObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
								groupMemberObj.setUsrId(usrMasterObj);						
								try {
									System.out.println("Before memListObj.size()-------------" + memListObj.size());
									memListObj.add(rid);
									System.out.println("memListObj.size()-------------" + memListObj.size());
									for (int i=0;i<memListObj.size();i++) {
										usrMasterObj.setUserId(memListObj.get(i));
										groupMemberObj.setUsrId(usrMasterObj);
										int memId = msgService.insertGroupMember(groupMemberObj);
										if (memId != -1) {
											cnt++;
										} else {
											flg = false;
										}
									}
								} catch(Exception ex) {
									flg = false;
									log.logMessage(getText("Eex") + ":: member insert ::" + ex, "error", CreateGroup.class);
								}
								log.logMessage(":: member insert flg false :: mem size" + memListObj.size() + ":insert count:" + cnt, "info", CreateGroup.class);
								if (cnt != memListObj.size()) {
									flg = false;
								}
								updatStrmsg = mobiCommon.getMsg("R0046");
								//Get group details
								List<Object[]> chatListObj = new ArrayList<Object[]>();
								chatListObj=msgService.getmessageContactsByProc(rid, 3, 0,groupName);
								locObjRspdataJson = msgService.getGroupNameDetails(chatListObj);
								System.out.println("locObjRspdataJson---else---------------->> " + locObjRspdataJson.toString());
							} else {
								updatStrmsg = "you don't have permission to update";
							}
							
						}
						
						if (flg) {
							if ( groupId == 0) {
								serverResponse(ivrservicecode,getText("status.success"),"R0053",mobiCommon.getMsg("R0053"),locObjRspdataJson);
							} else if ( groupId != 0) {
								serverResponse(ivrservicecode,getText("status.success"),"R0045",mobiCommon.getMsg("R0045"),locObjRspdataJson);
							}							
						} else {
							log.logMessage(":: Error in create group groupId::" + groupId, "info", CreateGroup.class);
							if ( groupId == 0) {
								serverResponse(ivrservicecode,getText("status.warning"),"R0051",mobiCommon.getMsg("R0051"),locObjRspdataJson);
							} else if ( groupId != 0) {
								serverResponse(ivrservicecode,getText("status.success"),"R0046",updatStrmsg,locObjRspdataJson);
							}					
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
			log.logMessage(getText("Eex") + ex, "error", CreateGroup.class);
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
