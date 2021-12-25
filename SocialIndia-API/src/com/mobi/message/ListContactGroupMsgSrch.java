package com.mobi.message;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.mobi.messagevo.ChatTblVO;
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ListContactGroupMsgSrch extends ActionSupport{
	
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
			//locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int startLimit = 0;
			int groupContactFlg = 0;
			Date timeStamp = null;
			String locTimeStamp = null;
			String searchText = null;
			String locGroupContactFlg = "";
			log.logMessage("Enter into ListContactWithMsg ", "info", ListContactGroupMsgSrch.class);
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
						locGroupContactFlg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"group_contact"); // 1 = contact, 2= group
						searchText = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"search_text");
						locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						String locStartLmit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
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
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.search.text.error")));
						}
						if (Commonutility.checknull(searchText)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.search.list.flg.error")));
						}
										
						if (Commonutility.checkempty(locStartLmit) && Commonutility.toCheckisNumeric(locStartLmit)) {
							startLimit = Commonutility.stringToInteger(locStartLmit);
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
			log.logMessage("flg :" +flg, "info", ListContactGroupMsgSrch.class);
			if (flg) {
				if (Commonutility.checkempty(locTimeStamp)) {
				} else {
					locTimeStamp=Commonutility.timeStampRetStringVal();
				}
				
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				boolean result=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(result==true){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));				
					if (flg) {
						ChatTblVO chatObj = new ChatTblVO();
						UserMasterTblVo usrMasterObj = new UserMasterTblVo();
						MessageDAO msgService = new MessageDAOService();
						JsonpackDao jsonPack = new JsonpackDaoService(); 
						usrMasterObj.setUserId(rid);
						List<Object[]> contactListObj = new ArrayList<Object[]>();
						DateFormat dateFormatw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date dateForw = new Date();
						locTimeStamp = dateFormatw.format(dateForw);
						System.out.println("locTimeStamp::" + locTimeStamp);
						String countQuery = "";
						if (Commonutility.checkempty(searchText)) { //search block
							FunctionUtility utilFn = new FunctionUtilityServices();
							searchText = utilFn.trimSplEscSrchdata(searchText);
							log.logMessage("searchText :" + searchText, "info", ListContactGroupMsgSrch.class);
							if (groupContactFlg == 1) { // private message contact list
								contactListObj = msgService.privateChatListSearch(rid, searchText, locTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")));
								countQuery = "select count(*) "
										+ "from mvp_chat_tbl cht "
										+ "left join usr_reg_tbl ur on ur.usr_id=cht.to_usr_id "
										+ "where ur.username like '%" +searchText+ "%' and cht.from_usr_id='" +rid+ "' and cht.entry_datetime<STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
							System.out.println("countQuery::" + countQuery);
							} else if (groupContactFlg == 2) { // group message contact list
								contactListObj = msgService.groupChatListSearch(rid, searchText, locTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")));
								countQuery = "select count(*) "
										+ "from (select grp_mem.grp_chat_id as grp_chat_id ,grp_mstr.grp_name as grp_name,grp_mstr.grp_image_mobile as image_mobile, "
										+ "(select entry_datetime from mvp_grpchat_dtl_tbl where grp_chat_id=grp_mem.grp_chat_id order by entry_datetime desc limit 1) as msg_time, "
										+ "grp_mem.entry_datetime as grp_mem_date from mvp_grpchat_dtl_tbl grp_dtl  "
										+ "left join mvp_grpchat_mem_tbl grp_mem on grp_dtl.user_id=grp_mem.usr_id "
										+ "left join mvp_grpchat_mstr_tbl grp_mstr on grp_mem.grp_chat_id=grp_mstr.grp_chat_id "
										+ "where grp_mem.usr_id='" +rid+ "' and grp_mstr.grp_name like '%" + searchText + "%' group by grp_mstr.grp_chat_id  "
										+ "order by case when  msg_time is not null then msg_time else grp_mem.entry_datetime end desc)tt "
										+ "where case when tt.msg_time is not null then  tt.msg_time<=str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') else tt.grp_mem_date<=str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') end ";
								System.out.println("countQuery::" + countQuery);
							} else {
								log.logMessage("Group or private message missing", "info", ListContactGroupMsgSrch.class);
//								serverResponse(ivrservicecode,getText("status.success"),"R0001",getText("R0001"),locObjRspdataJson);
								flg = false;
							}
						} else { // List of group or private message block
							if (groupContactFlg == 1) {  // private message contact list
								contactListObj = msgService.privateChatList(rid, locTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")));
								countQuery = "select count(*) "
										   + " from mvp_chat_tbl cht "
										   + "left join usr_reg_tbl ur on ur.usr_id=cht.TO_USR_ID "
										   + "where  cht.from_usr_id='" +rid+ "' and cht.entry_datetime<STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') ";
								System.out.println("countQuery::" + countQuery);
							} else if (groupContactFlg == 2) { // group message contact list
								contactListObj = msgService.groupChatList(rid, locTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")));
								countQuery = "select count(*) "
										+ "from (select grp_mem.grp_chat_id as grp_chat_id ,grp_mstr.grp_name as grp_name,grp_mstr.grp_image_mobile as grp_image, (select entry_datetime from mvp_grpchat_dtl_tbl where grp_chat_id=grp_mem.grp_chat_id order by entry_datetime desc limit 1) as msg_time, grp_mem.entry_datetime as grp_mem_date "
										+ "from mvp_grpchat_dtl_tbl grp_dtl  "
										+ "left join mvp_grpchat_mem_tbl grp_mem on grp_dtl.user_id=grp_mem.usr_id "
										+ "left join mvp_grpchat_mstr_tbl grp_mstr on grp_mem.grp_chat_id=grp_mstr.grp_chat_id "
										+ "where grp_mem.usr_id='" +rid+ "'  group by grp_mstr.grp_chat_id  "
										+ "order by case when  msg_time is not null then msg_time else grp_mem.entry_datetime end desc)tt "
										+ "where case when tt.msg_time is not null then tt.msg_time<=str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') else tt.grp_mem_date<=str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') end ";
							System.out.println("countQuery::" + countQuery);
							} else {
								log.logMessage("Group or private message missing", "info", ListContactGroupMsgSrch.class);
//								serverResponse(ivrservicecode,getText("status.success"),"R0001",getText("R0001"),locObjRspdataJson);
								flg = false;
							}
						}				
						if (flg) {
							String totalcount = ""+commonServ.getTotalCountSqlQuery(countQuery);
							
							locObjRspdataJson.put("timestamp", locTimeStamp);
							System.out.println("totalcount:" + totalcount);
							locObjRspdataJson.put("totalrecords", totalcount);
							locObjRspdataJson.put("group_contact", locGroupContactFlg);
							JSONArray jsonAry = new JSONArray();
							String externalUserImagePath = getText("external.imagesfldr.path")+getText("external.inner.mobilepath") + rid +"/";
							externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath")+ rid +"/";
							jsonAry = jsonPack.listOfGroupPrivateContact(contactListObj, externalUserImagePath);
							locObjRspdataJson.put("list", jsonAry);
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
			log.logMessage(getText("Eex") + ex, "error", ListContactGroupMsgSrch.class);
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
