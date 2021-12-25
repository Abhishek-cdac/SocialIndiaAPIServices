package com.mobi.message;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.messagevo.ChatTblVO;
import com.mobi.messagevo.GroupChatMstrTblVO;
import com.mobi.messagevo.GroupChatTblVO;
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class SendMsg extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	private List<File> fileUpload = new ArrayList<File>();
	private List<String> fileUploadContentType = new ArrayList<String>();
	private List<String> fileUploadFileName = new ArrayList<String>();
	Log log= new Log();
	
	public String execute() {
		/*	fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Chrysanthemum.jpg"));
		fileUploadFileName.add("Chrysanthemum.jpg");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.wmv"));
		fileUploadFileName.add("Wildlife.wmv");
		fileUpload.add(new File("C://Users//Public//Videos//Sample Videos//Wildlife.jpg"));
		fileUploadFileName.add("Wildlife.jpeg");
		fileUpload.add(new File("C://Users//Public//Pictures//Sample Pictures//Desert.jpg"));
		fileUploadFileName.add("Desert.jpg");*/
		
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
			String message = null;
			String thumbImage="";
			String urlTitle="";
			String pageUrl="";
			int sendTo = 0;
			int groupChatId = 0;
			int type = 0;
			int msgType = 0;
			log.logMessage("Enter into SendMsg ", "info", SendMsg.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams--decrypt--------------------" + ivrparams);
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
						String loctype = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_contact");// 1-msd 2-group msg
						String locSendTo = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_contact_id"); 
						String locMsgType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"msgtype");
						message = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"content");
						JSONArray jsonArr = new JSONArray();
						jsonArr = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"urls");//array
						//thumbImage = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"thumb_img");
						//urlTitle = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"title");
						//pageUrl = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"pageurl");
						
						if (jsonArr !=null) {
							for (int i=0;i<jsonArr.length();i++) {
								JSONObject locJsonObj = new JSONObject();
								locJsonObj = jsonArr.getJSONObject(i);
								String locthumbImg = (String) Commonutility.toHasChkJsonRtnValObj(locJsonObj,"thumb_img");								
								String loctitle = (String) Commonutility.toHasChkJsonRtnValObj(locJsonObj,"title");
								String locpageurl = (String) Commonutility.toHasChkJsonRtnValObj(locJsonObj,"pageurl");
								if (Commonutility.checkempty(locthumbImg)) {
									thumbImage += Commonutility.spTagAdd(locthumbImg);
								}
								if (Commonutility.checkempty(loctitle)) {
									urlTitle += Commonutility.spTagAdd(loctitle);
								}
								if (Commonutility.checkempty(locpageurl)) {
									pageUrl += Commonutility.spTagAdd(locpageurl);
								}
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("feed.pageurl.error")));
						}
						
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
						if (Commonutility.checkempty(loctype) && Commonutility.toCheckisNumeric(loctype)) {
							type = Commonutility.stringToInteger(loctype);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.snder.type.error")));
						}
						if (type == 2) {
							if (Commonutility.checkempty(locSendTo) && Commonutility.toCheckisNumeric(locSendTo)) {
								sendTo = Commonutility.stringToInteger(locSendTo);
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.sendto.id.error")));
							}
						} else if (type == 3) {
							if (Commonutility.checkempty(locSendTo) && Commonutility.toCheckisNumeric(locSendTo)) {
								groupChatId = Commonutility.stringToInteger(locSendTo);
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.mem.id.error")));
							}
						}
												
						if (Commonutility.checkempty(message)) {
							
						} else {
							//flg = false;
							//locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.txt.error")));
						}
						if (Commonutility.checkempty(locMsgType) && Commonutility.toCheckisNumeric(locMsgType)) {
							msgType = Commonutility.stringToInteger(locMsgType);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.sned.type.error")));
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
			log.logMessage("flg :" +flg, "info", SendMsg.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if (flg) {
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));				
					if (flg) {
						UserMasterTblVo usrMasterObj = new UserMasterTblVo();
						UserMasterTblVo usrMasterToObj = new UserMasterTblVo();
						MessageDAO msgService = new MessageDAOService();
						usrMasterObj.setUserId(rid);				
						int chatId = -1;
						if (type == 2) {
							ChatTblVO chatObj = new ChatTblVO();
							usrMasterToObj.setUserId(sendTo);
							chatObj.setFromUsrId(usrMasterObj);
							chatObj.setToUsrId(usrMasterToObj);
							chatObj.setMsgContent(message);
							chatObj.setUrlThumbImage(thumbImage);
							chatObj.setUrlTitle(urlTitle);
							chatObj.setPageUrl(pageUrl);
							chatObj.setMsgType(msgType);
							chatObj.setStatus(1);
							chatObj.setReadFlg(0);
							chatObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							chatObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							chatId = msgService.chatInsert(chatObj,fileUpload,fileUploadContentType,fileUploadFileName);
						} else if (type == 3) {
							GroupChatTblVO grpChatobj = new GroupChatTblVO();
							GroupChatMstrTblVO groupChatMstrObj = new GroupChatMstrTblVO();
							groupChatMstrObj.setGrpChatId(groupChatId);
							grpChatobj.setUserId(usrMasterObj);
							grpChatobj.setGrpChatId(groupChatMstrObj);
							grpChatobj.setMsgContent(message);
							grpChatobj.setUrlThumbImage(thumbImage);
							grpChatobj.setUrlTitle(urlTitle);
							grpChatobj.setPageUrl(pageUrl);
							grpChatobj.setMsgType(msgType);
							grpChatobj.setReadFlg(0);
							grpChatobj.setStatus(1);
							grpChatobj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							grpChatobj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							chatId = msgService.groupChatInsert(grpChatobj,fileUpload,fileUploadContentType,fileUploadFileName);
						} else {
							log.logMessage("Feed inset type error type 1 or 2 ", "info", SendMsg.class);
						}
						if (chatId != -1) {
							String imagePathWeb = null;
							String imagePathMobi = null;
							String videoPath = null;
							String videoPathThumb = null;
							String profileimgPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath")+ rid +"/";
							if (type == 2) {
								List<ChatTblVO> sendChatMsgObj = new ArrayList<ChatTblVO>();
								sendChatMsgObj = msgService.getchatInsertDetails(chatId);
								imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.msg.img.webpath");
								imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.msg.img.mobilepath") ;
								videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.msg.videopath");
								videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.msg.video.thumbpath");
								if (sendChatMsgObj != null && sendChatMsgObj.size() > 0) {
									locObjRspdataJson = msgService.jsonChatPack(sendChatMsgObj, profileimgPath,imagePathWeb,imagePathMobi,videoPath,videoPathThumb);
								}
							} else if (type == 3) {
								List<GroupChatTblVO> sendGrpChatMsgObj = new ArrayList<GroupChatTblVO>();
								sendGrpChatMsgObj = msgService.getGroupChartInsertDetails(chatId);
								imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.groupmsg.img.webpath");
								imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.groupmsg.img.mobilepath") ;
								videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.groupmsg.videopath");
								videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.groupmsg.video.thumbpath");
								if (sendGrpChatMsgObj != null && sendGrpChatMsgObj.size() > 0) {
									locObjRspdataJson = msgService.jsonGroupChatPack(sendGrpChatMsgObj, profileimgPath,imagePathWeb,imagePathMobi,videoPath,videoPathThumb);
								}
							}
							serverResponse(ivrservicecode,getText("status.success"),"R0039",mobiCommon.getMsg("R0039"),locObjRspdataJson);
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0040",mobiCommon.getMsg("R0040"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", SendMsg.class);
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
