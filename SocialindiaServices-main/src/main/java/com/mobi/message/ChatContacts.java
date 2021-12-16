package com.mobi.message;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.messagevo.ChatBlockUnblockTblVO;
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ChatContacts extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
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
			String locRid =null;
			String chatType =null;
			String startLim =null;
			String searchByName =null;
			int rid = 0;
			int startLimit = 0;
			String timeStamp = null;
			int groupId = 0;
			List<Integer> memListObj = new ArrayList<Integer>();
			log.logMessage("Enter into ChatContacts ", "info", CreateGroup.class);
			System.out.println("Enter ChatContacts------------------------1-----");
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
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						timeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						chatType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"chat_type");
						startLim = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
						searchByName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"search_text");
						System.out.println("Enter ChatContacts------------------------2-----");
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
			System.out.println("Enter ChatContacts------------------------3-----" + locErrorvalStrBuil.toString());
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				boolean result=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(result==true){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
/*						procedure paramater flag:
							1-individual :conversation between a giver user and his friend
							2-list of group chats of given user 
							3- chat_flag-2:indivdual,chat_flag-3:group chat,chat_flag-1:unknown user*/
							
							
						JSONObject jsonPack = new JSONObject();
						JSONArray jsonArr=new JSONArray();
						System.out.println("Enter into group create action");
						UserMasterTblVo usrMasterObj = new UserMasterTblVo();
						MessageDAO msgService = new MessageDAOService();
						usrMasterObj.setUserId(rid);
						List<Object[]> chatListObj = new ArrayList<Object[]>();
						if(searchByName!=null&& searchByName.length()>0){}else{searchByName="";}
						chatListObj=msgService.getmessageContactsByProc(Integer.parseInt(locRid), Integer.parseInt(chatType), Integer.parseInt(startLim),searchByName);
						if (chatListObj != null) {
							
							Object[] objList;
							for(Iterator<Object[]> it=chatListObj.iterator();it.hasNext();) {
								objList = it.next();
								jsonPack = new JSONObject();
								
								if(objList[0]!=null){
									jsonPack.put("group_name",objList[0]);
								}else{
									jsonPack.put("group_name","");
								}
								if(objList[3]!=null){
									jsonPack.put("online_status",objList[3]);
								}else{
									jsonPack.put("online_status","");
								}
								if(objList[4]!=null){
									jsonPack.put("from_id",objList[4]);
								}else{
									jsonPack.put("from_id","");
								}
								if(objList[5]!=null){
									jsonPack.put("to_id",objList[5]);
								}else{
									jsonPack.put("to_id","");
								}
								if(objList[6]!=null){
									jsonPack.put("message",objList[6]);
								}else{
									jsonPack.put("message","");
								}
								if(objList[10]!=null){
									
									jsonPack.put("message_time",mobiCommon.getchatDateTime((java.util.Date)objList[10]));
								}else{
									jsonPack.put("message_time","");
								}
								
								if(objList[8]!=null){
									jsonPack.put("read_status",objList[8]);
								}else{
									jsonPack.put("read_status","");
								}
								
								if(objList[1]!=null && objList[11]!=null && objList[5]!=null && objList[11].toString().equals("3")){
									jsonPack.put("profile_picture", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/chat/mobile/"+objList[5]+"/"+objList[1]);
								//}else if(objList[1]!=null && (objList[4]!=null || objList[5]!=null)){
								}else if(objList[1]!=null && (objList[11].toString().equals("1") || objList[11].toString().equals("2"))){
									String useri="";
									if(objList[4]!=null){
										useri=objList[4].toString();
									}else if(objList[5]!=null){
										useri=objList[5].toString();
									}
									//jsonPack.put("profile_picture", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+useri+"/"+objList[1]);
									if(Commonutility.checkempty((String) objList[13])){
										useri = objList[13].toString();
										jsonPack.put("profile_picture", System.getenv("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+useri+"/"+objList[1]);
									} else {
										jsonPack.put("profile_picture","");
									}
										
								}else if(objList[2]!=null){
									jsonPack.put("profile_picture", objList[2]);	
								}else{
									jsonPack.put("profile_picture","");
								}
								
								
								/* if(objList[11]!=null){
										jsonPack.put("chat_type",objList[11]);
									}else{
										jsonPack.put("chat_type","");
									}
								 */
								//RPK --start
								if(objList[11]!=null){
									
									if(objList[11].toString().equalsIgnoreCase("1") || objList[11].toString().equalsIgnoreCase("2")){
										jsonPack.put("chat_type","2");
									} else {
										jsonPack.put("chat_type",objList[11]);
									}										
								}else{
									jsonPack.put("chat_type","");
								}
								//RPK end
								
								if(objList[13]!=null){
									jsonPack.put("group_contact_id",objList[13]);
								}else{
									jsonPack.put("group_contact_id","");
								}
								
								if(objList[14]!=null) {
									String userData = objList[14].toString();
									if (Commonutility.checkempty(userData)) {
										jsonPack.put("grp_user",userData);
									} else {
										jsonPack.put("grp_user","");
									}
								} else {
									jsonPack.put("grp_user","");
								}
								
								if(objList[15]!=null){
									jsonPack.put("creator_id",objList[15]);
								}else{
									jsonPack.put("creator_id","");
								}

								
								if(objList[4] !=null && objList[5] != null){
									ChatBlockUnblockTblVO _chatObj = new ChatBlockUnblockTblVO();
									_chatObj.setFromUsrId(Integer.parseInt((String) objList[4]));
									_chatObj.setToUsrId(Integer.parseInt((String) objList[5]));
									
									List<Object[]> list = msgService.selectBlockChat(_chatObj);
									if(list != null && list.size() > 0){
										Object[] objs = list.get(0);
										jsonPack.put("is_blocked", objs[2]);
									}
									else{
										jsonPack.put("is_blocked", false);
									}
								}
								else{
									jsonPack.put("is_blocked", false);
								}
								
								jsonArr.put(jsonPack);
							}
							locObjRspdataJson.put("messages", jsonArr);
							locObjRspdataJson.put("totalrecords", chatListObj.size());
							serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						}else{
							locObjRspdataJson=new JSONObject();
							serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
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
	
	

}
