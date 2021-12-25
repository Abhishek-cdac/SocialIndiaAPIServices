package com.mobi.message;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.messagevo.ChatBlockUnblockTblVO;
import com.mobi.messagevo.persistence.MessageDAO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class BlockChat extends ActionSupport{
	
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
			int sendTo = 0;
			boolean isBlock = false;
			log.logMessage("Enter into BlockChat ", "info", BlockChat.class);
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
						String from_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"from_id");
						String to_id = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"to_id"); 
						isBlock = (boolean) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"is_blocked"); 
						
						if (Commonutility.checkempty(from_id) && Commonutility.toCheckisNumeric(from_id)) {
							rid = Commonutility.stringToInteger(from_id);
							if (rid !=0 ) {
								
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}
						
						if (Commonutility.checkempty(to_id) && Commonutility.toCheckisNumeric(to_id)) {
								sendTo = Commonutility.stringToInteger(to_id);
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("msg.sendTo.id.error")));
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
			
			log.logMessage("flg :" +flg, "info", BlockChat.class);
			
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if (flg) {
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));				
					if (flg) {
						MessageDAO msgService = new MessageDAOService();
						
						ChatBlockUnblockTblVO chatObj = new ChatBlockUnblockTblVO();
						chatObj.setFromUsrId(rid);
						chatObj.setToUsrId(sendTo);
						chatObj.setBlocked(isBlock);
						chatObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
						flg = msgService.blockChat(chatObj);
						
						if (flg) {
							locObjRspdataJson.put("from_id", rid);
							locObjRspdataJson.put("to_id", sendTo);
							locObjRspdataJson.put("is_blocked", chatObj.getBlocked());
							
							chatObj = new ChatBlockUnblockTblVO();
							chatObj.setFromUsrId(sendTo);
							chatObj.setToUsrId(rid);
							
							List<Object[]> list = msgService.selectBlockChat(chatObj);
							if(list != null && list.size() > 0){
								Object[] objs = list.get(0);
								locObjRspdataJson.put("is_blocked2", objs[2]);
							}
							else{
								locObjRspdataJson.put("is_blocked2", false);
							}
							
							serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", BlockChat.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {			
		}
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
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
