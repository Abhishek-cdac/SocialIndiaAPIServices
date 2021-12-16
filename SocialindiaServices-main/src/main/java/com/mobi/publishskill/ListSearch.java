package com.mobi.publishskill;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.jsonpack.JsonSimplepackDao;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.publishskillvo.PublishSkillTblVO;
import com.mobi.publishskillvo.persistence.PubilshSkillDao;
import com.mobi.publishskillvo.persistence.PubilshSkillDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ListSearch extends ActionSupport{
	
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
			int categoryId = 0;
			int skillId = 0;
			int startLimit = 0;
			Date timeStamp = null;
			String locRid = null;
			String locTimeStamp = null;
			String locStartLmit = null;
			String title = null;
			String loccategoryId = null;
			String locskillId = null;
			String searchQury = "";
			String searchFlg = null;
			String retTimeStamp = null;
			int totCnt = 0;
			log.logMessage("Enter into ListSearch ", "info", ListSearch.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("ListSearch ivrparams :" + ivrparams, "info", ListSearch.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						loccategoryId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"category_id");
						locskillId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"skill_id");
						title = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"title");
						locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						locStartLmit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
						searchFlg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"search_flg");
					}
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
						if (Commonutility.checkempty(searchFlg)) { 
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}
						if (searchFlg.equalsIgnoreCase("1")) { 
						if (!loccategoryId.equalsIgnoreCase("") && !loccategoryId.equalsIgnoreCase("null") 
								&& loccategoryId != null && loccategoryId.length() > 0) {
							//if (Commonutility.checkLengthNotZero(loccategoryId)) {
									categoryId = Commonutility.stringToInteger(loccategoryId);
									searchQury += " and pubtl.category_id =" + categoryId+"";
							/*} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.cat.id.error")));
							}	*/													
						}
						if (!locskillId.equalsIgnoreCase("") && !locskillId.equalsIgnoreCase("null") 
								&& locskillId != null && locskillId.length() > 0) {
							//if (Commonutility.checkLengthNotZero(locskillId)) {
								skillId = Commonutility.stringToInteger(locskillId);
								searchQury += " and pubtl.skill_id = "+ skillId + "" ;
							/*} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.skilid.error")));
							}*/
						}
						if (!title.equalsIgnoreCase("") && !title.equalsIgnoreCase("null") 
								&& title != null && title.length() > 0) {
							//if (Commonutility.checkempty(title)) {
								FunctionUtility utilFn = new FunctionUtilityServices();
								title = utilFn.trimSplEscSrchdata(title);								 
								searchQury += " and pubtl.title like ('%" + title + "%') ";
							/*} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("publish.skill.title.error")));
							}*/
						}
						searchQury += " and pubtl.user_id!='"+rid+"' ";
						} else {
							searchQury = " and pubtl.user_id='"+rid+"' ";
						}
						if (Commonutility.checknull(locTimeStamp)) {
							if (Commonutility.checkempty(locTimeStamp)) {
								retTimeStamp = locTimeStamp;
								locTimeStamp = "and pubtl.enrty_datetime<str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') ";
							} else {
								locTimeStamp = Commonutility.timeStampRetStringVal();
								retTimeStamp = locTimeStamp;
								locTimeStamp = "and pubtl.enrty_datetime<str_to_date('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') ";
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
			log.logMessage("flg :" +flg, "info", ListSearch.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						PubilshSkillDao pubskilDaoObj = new PubilshSkillDaoServices();
						PublishSkillTblVO pubSkilObj = new PublishSkillTblVO();
						List<Object[]> listObj = new ArrayList<Object[]>();
						listObj = pubskilDaoObj.publishSkilSrchList(searchQury, locTimeStamp, startLimit, Commonutility.stringToInteger(getText("end.limit")));
						JSONArray jsonArr = new JSONArray();
						if (listObj != null && listObj.size() > 0) {
							totCnt = listObj.size();
							JsonSimplepackDao jsonDataPack = new JsonSimplepackDaoService();
							String profileimgPath = System.getenv("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.view.profile.mobilepath");							
							jsonArr = jsonDataPack.pubSkillListDetails(listObj, profileimgPath);
							String contQuery = "select count(pubSkilId) from  PublishSkillTblVO where status <>'0'" + searchQury;
							log.logMessage("publish skill contQuery:" + contQuery, "info", ListSearch.class);
							//totCnt = commonServ.getTotalCountQuery(contQuery);
							locObjRspdataJson.put("publish_details", jsonArr);
							locObjRspdataJson.put("timestamp", retTimeStamp);
							locObjRspdataJson.put("totalrecords", totCnt);
							if (flg) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
							} else {
								locObjRspdataJson = new JSONObject();
								serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0006"),locObjRspdataJson);
							}
						} else{
							locObjRspdataJson=new JSONObject();
							serverResponse(ivrservicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
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
			log.logMessage(getText("Eex") + ex, "error", ListSearch.class);
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
