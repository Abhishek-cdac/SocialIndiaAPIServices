package com.mobi.message;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.messagevo.GroupChatMemTblVO;
import com.mobi.messagevo.GroupChatMstrTblVO;
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class AddRemoveUserFrmGroup extends ActionSupport{
	
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
			int type = 0;
			int groupId = 0;
			int memberUserId = 0;
			List<Integer> memListObj = new ArrayList<Integer>();
			log.logMessage("Enter into AddRemoveUserFrmGroup ", "info", AddRemoveUserFrmGroup.class);
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
						String locType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"update_type");// 1-add,2-remove
						JSONArray locMemberId = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"member_id");// array
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
						if (Commonutility.checkempty(locType) && Commonutility.toCheckisNumeric(locType)) {
							type = Commonutility.stringToInteger(locType);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.type.error")));
						}
						if (locMemberId!=null) {
//							memberUserId = Commonutility.stringToInteger(locMemberId);
								try {
									for (int i=0;i<locMemberId.length();i++) {
										int mem = locMemberId.getInt(i);
										memListObj.add(mem);
										System.out.println("Memeber id :" + mem);
									}
								} catch (Exception ex) {
									log.logMessage(getText("Eex") + ex, "error", AddRemoveUserFrmGroup.class);
								}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.grp.mem.id.error")));
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
			log.logMessage("flg :" +flg, "info", AddRemoveUserFrmGroup.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				boolean result=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(result==true){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						GroupChatMstrTblVO groupChatMstrObj = new GroupChatMstrTblVO();
						GroupChatMemTblVO groupMemberObj = new GroupChatMemTblVO();
						UserMasterTblVo usrMasterObj = new UserMasterTblVo();
						MessageDAO msgService = new MessageDAOService();
						usrMasterObj.setUserId(memberUserId);
						groupChatMstrObj.setGrpChatId(groupId);
//						groupMemberObj.setUsrId(usrMasterObj);
						groupMemberObj.setGrpChatId(groupChatMstrObj);
						groupMemberObj.setCreaterId(rid);
						int cnt = 0;
						if (type == 1) {//add
							groupMemberObj.setStatus(1);
							groupMemberObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							groupMemberObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							
							try {							
								for (int i=0;i<memListObj.size();i++) {
									usrMasterObj.setUserId(memListObj.get(i));
									groupMemberObj.setUsrId(usrMasterObj);
									int memId = msgService.addGroupMember(groupMemberObj);
									if (memId != -1) {
										cnt++;
									} else {
										flg = false;
									}
								}
							} catch(Exception ex) {
								flg = false;
								log.logMessage(getText("Eex") + ":: member insert ::" + ex, "error", AddRemoveUserFrmGroup.class);
							}
							log.logMessage(":: member insert flg false :: mem size" + memListObj.size() + ":insert count:" + cnt, "info", AddRemoveUserFrmGroup.class);
							if (cnt != memListObj.size()) {
								flg = false;
							}										
							if (flg) {
								serverResponse(ivrservicecode,getText("status.success"),"R0047",mobiCommon.getMsg("R0047"),locObjRspdataJson);
							} else {						
								serverResponse(ivrservicecode,getText("status.success"),"R0048",mobiCommon.getMsg("R0048"),locObjRspdataJson);
							}
						} else if (type == 2) {//remove
							
							try {							
								for (int i=0;i<memListObj.size();i++) {
									usrMasterObj.setUserId(memListObj.get(i));
									groupMemberObj.setUsrId(usrMasterObj);
									flg = msgService.removeGroupMember(groupMemberObj);
									if (flg) {
										cnt++;
									} else {
										flg = false;
									}
								}
							} catch(Exception ex) {
								flg = false;
								log.logMessage(getText("Eex") + ":: member insert ::" + ex, "error", AddRemoveUserFrmGroup.class);
							}
							log.logMessage(":: member insert flg false :: mem size" + memListObj.size() + ":insert count:" + cnt, "info", AddRemoveUserFrmGroup.class);
							if (cnt != memListObj.size()) {
								flg = false;
							}										
							if (flg) {
								serverResponse(ivrservicecode,getText("status.success"),"R0049",mobiCommon.getMsg("R0049"),locObjRspdataJson);
							} else {
								serverResponse(ivrservicecode,getText("status.warning"),"R0050",mobiCommon.getMsg("R0050"),locObjRspdataJson);
							}
						} else {
							serverResponse(ivrservicecode,getText("status.success"),"R0048",mobiCommon.getMsg("R0048"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", AddRemoveUserFrmGroup.class);
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
