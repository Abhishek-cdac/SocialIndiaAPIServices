package com.mobi.message;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobi.messagevo.ChatBlockUnblockTblVO;
import com.mobi.messagevo.ChatTblVO;
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ListGroupPrivateMsgHistroy extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
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
			int startLimit = 0;
			int groupContactFlg = 0;
			int groupIdMsgId = 0;
			Date timeStamp = null;
			String locTimeStamp = null;
			String locSearchText = null;
			log.logMessage("Enter into ListGroupWithMsg ", "info", ListGroupPrivateMsgHistroy.class);
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
						String locGroupContactFlg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_contact"); // 1 = contact, 2= group
						String locMsgToid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_contact_id");
						locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						String locStartLmit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
						locSearchText = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"searchText");
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
						if (Commonutility.checkempty(locGroupContactFlg) && Commonutility.toCheckisNumeric(locGroupContactFlg)) {
							groupContactFlg = Commonutility.stringToInteger(locGroupContactFlg);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.search.list.flg.error")));
						}
						if (Commonutility.checknull(locMsgToid)) {
							if (Commonutility.checkempty(locMsgToid)) {
								if (Commonutility.toCheckisNumeric(locMsgToid)) {
									groupIdMsgId = Commonutility.stringToInteger(locMsgToid);
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.sendto.id.error")));
								}
								
							}
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.sendto.id.error")));
						}
						if (Commonutility.checknull(locTimeStamp)) {
							if (Commonutility.checkempty(locTimeStamp)) {
								
							} else {
								locTimeStamp = Commonutility.timeStampRetStringVal();
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timestamp.error")));
						}						
						if (Commonutility.checknull(locStartLmit)) {
							if (Commonutility.checkempty(locStartLmit) && Commonutility.toCheckisNumeric(locStartLmit)) {
								startLimit = Commonutility.stringToInteger(locStartLmit);
							} else {
								startLimit = 0;	
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("startlimit.error")));
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
			log.logMessage("flg :" +flg, "info", ListGroupPrivateMsgHistroy.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));				
					if (flg) {
						String sqlCntQury = null;
						String totalcount = "";
						JSONArray jsonAry = new JSONArray();
						ChatTblVO chatObj = new ChatTblVO();
						UserMasterTblVo usrMasterObj = new UserMasterTblVo();
						MessageDAO msgService = new MessageDAOService();
						JsonpackDao jsonPack = new JsonpackDaoService(); 
						usrMasterObj.setUserId(rid);
						List<Object[]> contactListObj = new ArrayList<Object[]>();
						String externalUserImagePath = getText("external.imagesfldr.path")+getText("external.inner.mobilepath") + rid +"/";
//						locTimeStamp = Commonutility.timeStampRetStringVal();
						String imagePathWeb = null;
						String imagePathMobi = null;
						String videoPath = null;
						String videoPathThumb = null;
						String profileimgPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath");
						if (groupContactFlg == 2) { // private msg
							contactListObj = msgService.privateChatMsgList(rid, groupIdMsgId, locTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")),locSearchText);
//							jsonAry = jsonPack.listOfGrpMsgPrivateMsg(contactListObj, externalUserImagePath);	
							String updateQuery="update ChatTblVO set readFlg=1 where (fromUsrId.userId='" + rid + "' and toUsrId.userId='" + groupIdMsgId + "') or (fromUsrId.userId='" + groupIdMsgId + "' and toUsrId.userId='" + rid + "')";
							commonServ.updateTableByQuery(updateQuery);
							String srchQry = "";
							if (Commonutility.checkempty(locSearchText)) {
								srchQry = " and (chtdtl.msg_content like ('%" + locSearchText + "%') or usr.fname like ('%" + locSearchText + "%') or usr.lname like ('%" + locSearchText + "%'))";
							} else {
								srchQry = "";
							}
							//sqlCntQury = "select count(*) from mvp_chat_tbl where (from_usr_id='" + rid + "' and to_usr_id='" + groupIdMsgId + "') or (from_usr_id='" + groupIdMsgId + "' and to_usr_id='" + rid + "') and status=1 and entry_datetime<=STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
							sqlCntQury = "select count(*) from (select chtdtl.chat_id,chtdtl.from_usr_id,usr.image_name_mobile,chtdtl.msg_content,chtdtl.msg_type,chtdtl.entry_datetime,"
									+ "group_concat(concat(ifnull(chtatt.uniq_id,''),ifnull(chtatt.attach_name,''),ifnull(chtatt.thump_image,''),ifnull(chtatt.file_type,'')) separator '<sp>') as attach_name,"
									+ "chtdtl.URLS_THUMB_IMG,chtdtl.URLS_TITLE,chtdtl.URLS_PAGEURL,chtdtl.TO_USR_ID "
										+ "from mvp_chat_tbl chtdtl "
										+ " left join mvp_chat_attch_tbl chtatt on chtdtl.chat_id=chtatt.chat_id"
										+ " left join usr_reg_tbl usr on usr.usr_id=chtdtl.to_usr_id "
										+ " where ((from_usr_id='" +rid+ "' and to_usr_id='" +groupIdMsgId+ "') or "
										+ " (from_usr_id='" +groupIdMsgId+ "' and to_usr_id='" +rid+ "')) and chtdtl.status=1 and chtdtl.entry_datetime<=STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') "+ srchQry +"  "
										+ " group by chtdtl.chat_id "
										+ "order by chtdtl.chat_id desc) as cnt";
							System.out.println("TotalCountSqlQuery----------------" + sqlCntQury);
							totalcount = Commonutility.intnullChek(commonServ.getTotalCountSqlQuery(sqlCntQury));
							
							imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.msg.img.webpath");
							imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.msg.img.mobilepath") ;
							videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.msg.videopath");
							videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.msg.video.thumbpath");
								
						} else if (groupContactFlg == 3) {// group
							contactListObj = msgService.groupChatMsgList(groupIdMsgId, locTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")),locSearchText);
//							jsonAry = jsonPack.listOfGroupMsg(contactListObj, externalUserImagePath);
							String updateQuery="update GroupChatTblVO set readFlg=1 where grpChatId='" + groupIdMsgId + "'";
							commonServ.updateTableByQuery(updateQuery);
							String srchQry = "";
							if (Commonutility.checkempty(locSearchText)) {
								srchQry = " and (chtdtl.msg_content like ('%" + locSearchText + "%') or usr.fname like ('%" + locSearchText + "%') or usr.lname like ('%" + locSearchText + "%'))";
							} else {
								srchQry = "";
							}
							//sqlCntQury = "select count(chat_id) from mvp_grpchat_dtl_tbl where grp_chat_id='" + groupIdMsgId + "' and status = 1 and entry_datetime<=STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
							sqlCntQury = "select count(*) from (select chtdtl.chat_id,chtdtl.user_id,usr.image_name_mobile,chtdtl.msg_content,chtdtl.msg_type,chtdtl.entry_datetime,"
									+ "group_concat(concat(ifnull(grpchtatt.uniq_id,''),ifnull(grpchtatt.attach_name,''),ifnull(grpchtatt.thump_image,''),ifnull(grpchtatt.file_type,'')) separator '<sp>') as attach_name,"
									+ "chtdtl.URLS_THUMB_IMG,chtdtl.URLS_TITLE,chtdtl.URLS_PAGEURL ,chtdtl.GRP_CHAT_ID "
										+ " from mvp_grpchat_dtl_tbl chtdtl "
										+ " left join mvp_grp_chat_attch_tbl grpchtatt on chtdtl.chat_id=grpchtatt.chat_id"
										+ " left join usr_reg_tbl usr on usr.usr_id=chtdtl.user_id "
										+ " where chtdtl.grp_chat_id='" + groupIdMsgId + "' and chtdtl.status=1 and chtdtl.entry_datetime<=STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') "+ srchQry +" "
										+ " group by chtdtl.chat_id"
										+ " order by chtdtl.chat_id desc) as cnt";
							System.out.println("group TotalCountSqlQuery----------------" + sqlCntQury);
							totalcount = Commonutility.intnullChek(commonServ.getTotalCountSqlQuery(sqlCntQury));
							
							imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.groupmsg.img.webpath");
							imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.groupmsg.img.mobilepath") ;
							videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.groupmsg.videopath");
							videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.groupmsg.video.thumbpath");
								
						} else {
							flg = false;
						}
						if (flg) {
							locObjRspdataJson.put("timestamp", locTimeStamp);
							System.out.println("totalcount:" + totalcount);
							locObjRspdataJson.put("totalrecords", totalcount);				
							jsonAry = jsonPack.listOfGrpMsgPrivateMsg(contactListObj, profileimgPath,imagePathWeb,imagePathMobi,videoPath,videoPathThumb,groupContactFlg);
							locObjRspdataJson.put("messages", jsonAry);
							
							if (groupContactFlg == 2) {
								ChatBlockUnblockTblVO _chatObj = new ChatBlockUnblockTblVO();
								_chatObj.setFromUsrId(rid);
								_chatObj.setToUsrId(groupIdMsgId);
								
								List<Object[]> list = msgService.selectBlockChat(_chatObj);
								if(list != null && list.size() > 0){
									Object[] objs = list.get(0);
									locObjRspdataJson.put("is_blocked", objs[2]);
								}
								else{
									locObjRspdataJson.put("is_blocked", false);
								}
								
								_chatObj = new ChatBlockUnblockTblVO();
								_chatObj.setFromUsrId(groupIdMsgId);
								_chatObj.setToUsrId(rid);
								
								list = msgService.selectBlockChat(_chatObj);
								if(list != null && list.size() > 0){
									Object[] objs = list.get(0);
									locObjRspdataJson.put("is_blocked2", objs[2]);
								}
								else{
									locObjRspdataJson.put("is_blocked2", false);
								}
							}
							else{
								locObjRspdataJson.put("is_blocked", "");
							}
							
							serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0006"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", ListGroupPrivateMsgHistroy.class);
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
	
	

}
