package com.mobi.skillhelp;

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
import com.mobi.skillhelp.persistence.SkillhelpDao;
import com.mobi.skillhelp.persistence.SkillhelpDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.laborvo.LaborDetailsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class SkillDirectoryListSearch extends ActionSupport{
	
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
			ivrservicecode = getText("report.issue");
			//locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String timeStamp = null;
			int startLimit = 0;
			String serachQuery = "";
			int rid = 0;
			int skillCategory = 0;
			int yearExp = 0;
			int rating = 0;
			int location = 0;
			String societyKey = null;
			String fromYearExp = null;
			String toYearExp = null;
			log.logMessage("Enter into report issue", "info", SkillDirectoryListSearch.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams:" + ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
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
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
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
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
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
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						String locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}						
						String locSkillCategory = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"skill_category");
						if (Commonutility.checkempty(locSkillCategory) && Commonutility.toCheckisNumeric(locSkillCategory)) {
							skillCategory = Commonutility.stringToInteger(locSkillCategory);
							if (skillCategory != 0) {
								serachQuery += "and lbr_skill.category_id ="+skillCategory+" ";
						//		serachQuery += "and lbr_skill.category_id like '%"+skillCategory+"%' ";
							}
						} else {
//							flg = false;
//							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.skill.category.error")));
						}
						fromYearExp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"from_year_exp");
						if (Commonutility.checkempty(fromYearExp) && Commonutility.toCheckisNumeric(fromYearExp)) {
							yearExp = Commonutility.stringToInteger(fromYearExp);
//							serachQuery += "and lbr.experiance ="+yearExp+" ";
							serachQuery += "and lbr.experiance like '%"+yearExp+"%' ";
						} else {
//							flg = false; 
//							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.year.exp.error")));
						}
						toYearExp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"to_year_exp");
						if (Commonutility.checkempty(toYearExp) && Commonutility.checkempty(fromYearExp)) {
//							if (Commonutility.checkempty(yearExp)) {
//								yearExp = yearExp + "-" + toYearExp;
//							}
						}
						String locRating = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rating");
						if (Commonutility.checkempty(locRating) && Commonutility.toCheckisNumeric(locRating)) {
							rating = Commonutility.stringToInteger(locRating);
//							serachQuery += "and lbr.lbr_rating ="+rating+" ";
							serachQuery += "and lbr.lbr_rating like '%"+rating+"%' ";
						} else {
//							flg = false;
//							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.rating.error")));
						}
						String locLocaton = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"locaton");
						if (Commonutility.checkempty(locLocaton) && Commonutility.toCheckisNumeric(locLocaton)) {
							location = Commonutility.stringToInteger(locLocaton);
//							serachQuery += "and lbr.city_id ="+location+" ";
							serachQuery += "and lbr.city_id like '%"+location+"%' ";
						} else {
//							flg = false;
//							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("booking.update.locaton.error")));
						}
						timeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"timestamp");
						log.logMessage("timeStamp :" + timeStamp, "info",SkillDirectoryListSearch.class);
						if (Commonutility.checknull(timeStamp)) {
							String locTimeStamp = Commonutility.timeStampRetStringVal();
							if (Commonutility.checkempty(timeStamp)) {
								serachQuery += "and lbr.entry_datetime<STR_TO_DATE('" + timeStamp + "','%Y-%m-%d %H:%i:%S') ";
							} else {
								timeStamp = locTimeStamp;
								serachQuery += "and lbr.entry_datetime<STR_TO_DATE('" + timeStamp + "','%Y-%m-%d %H:%i:%S') ";
							}							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("timestamp.error")));
						}
						String locstaLimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"startlimit");
						if (Commonutility.checknull(locstaLimit)) {
							if (Commonutility.checkempty(locstaLimit) && Commonutility.toCheckisNumeric(locstaLimit)) {
								startLimit = Commonutility.stringToInteger(locstaLimit);
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
			log.logMessage("flg :" +flg, "info", SkillDirectoryListSearch.class);
			log.logMessage("serachQuery :::" +serachQuery, "info", SkillDirectoryListSearch.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			            Date da = new Date();
						timeStamp = df.format(da).toString();
						System.out.println("timeStamp :" + timeStamp);
						LaborDetailsTblVO laborObj = new LaborDetailsTblVO();
						locObjRspdataJson=new JSONObject();
//						String locQuery = "select * FROM LaborProfileTblVO l  Left Join  LaborSkillTblVO ls ON l.ivrBnLBR_ID=ls.ivrBnLBR_ID WHERE l.ivrBnLBR_STS=1 AND ls.ivrBnSKILL_ID=1";
						
						String locQuery = "SELECT lbr.LBR_ID, lbr.LBR_NAME, lbr.LBR_EMAIL, lbr.LBR_PH_NO, lbr.IMAGE_NAME_WEB, lbr.IMAGE_NAME_MOBILE, lbr.LBR_DESCP, cty.CITY_NAME, stt.STATE_NAME, cnt.COUNTRY_NAME, lbr.LBR_RATING, lbr.cost, lbr.COST_PER_TIME, lbr.experiance, catg.category_name, skill.SKILL_NAME"
								+ " FROM mvp_lbr_reg_tbl lbr"
								+ " LEFT JOIN mvp_lbr_skill_tbl lbr_skill ON lbr.lbr_id = lbr_skill.lbr_id"
								+ " LEFT JOIN category_mst_tbl catg ON lbr_skill.CATEGORY_ID = catg.CATEGORY_ID"
								+ " LEFT JOIN mvp_skill_tbl skill ON lbr_skill.SKILL_ID = skill.SKILL_ID"
								+ " LEFT JOIN city_mst_tbl cty ON lbr.CITY_ID = cty.CITY_ID"
								+ " LEFT JOIN state_mst_tbl stt ON lbr.STATE_ID = stt.STATE_ID"
								+ " LEFT JOIN country_mst_tbl cnt ON lbr.COUNTRY_ID = cnt.COUNTRY_ID"
								+ " WHERE lbr.LBR_STS =1 and lbr.CITY_ID ="+location+" and lbr.experiance ="+yearExp+" "
								+ " and lbr.LBR_RATING ="+rating+" and lbr.ENTRY_DATETIME<STR_TO_DATE('" + timeStamp + "','%Y-%m-%d %H:%i:%S') and lbr_skill.category_id ="+skillCategory+" group by lbr.lbr_id LIMIT "+startLimit+" , 20";
						String locQueryAuto = "SELECT lbr.LBR_ID, lbr.LBR_NAME, lbr.LBR_EMAIL, lbr.LBR_PH_NO, lbr.IMAGE_NAME_WEB, lbr.IMAGE_NAME_MOBILE, lbr.LBR_DESCP, cty.CITY_NAME, stt.STATE_NAME, cnt.COUNTRY_NAME, lbr.LBR_RATING, lbr.cost, lbr.COST_PER_TIME, lbr.experiance, catg.category_name, skill.SKILL_NAME"
								+ " FROM mvp_lbr_reg_tbl lbr"
								+ " LEFT JOIN mvp_lbr_skill_tbl lbr_skill ON lbr.lbr_id = lbr_skill.lbr_id"
								+ " LEFT JOIN category_mst_tbl catg ON lbr_skill.CATEGORY_ID = catg.CATEGORY_ID"
								+ " LEFT JOIN mvp_skill_tbl skill ON lbr_skill.SKILL_ID = skill.SKILL_ID"
								+ " LEFT JOIN city_mst_tbl cty ON lbr.CITY_ID = cty.CITY_ID"
								+ " LEFT JOIN state_mst_tbl stt ON lbr.STATE_ID = stt.STATE_ID"
								+ " LEFT JOIN country_mst_tbl cnt ON lbr.COUNTRY_ID = cnt.COUNTRY_ID"
								+ " WHERE lbr.LBR_STS =1 and " + serachQuery+" group by lbr.lbr_id";
						
						String countQuery = "select count(*) from (SELECT count(lbr.LBR_ID) FROM mvp_lbr_reg_tbl lbr LEFT JOIN mvp_lbr_skill_tbl lbr_skill ON lbr.lbr_id = lbr_skill.lbr_id WHERE lbr.LBR_STS =1 " + serachQuery+" group by lbr.LBR_ID) tt";
						System.out.println("locQuery:" + locQuery);
						System.out.println("locQueryAuto:" + locQueryAuto);
						System.out.println("countQuery:" + countQuery);
						
						SkillhelpDao skillService = new SkillhelpDaoServices();
						JsonpackDao jsonDataPack = new JsonpackDaoService();
						List<Object[]> loborDataObj = new ArrayList<Object[]>();
						// LaborProfileTblVO - mvp_lbr_reg_tbl  
						// LaborSkillTblVO - mvp_lbr_skill_tbl
//						loborDataObj = skillService.skillDirectoryLaborData(rid);
						JSONArray jsonArrObj = new JSONArray();								
						loborDataObj = skillService.testskillDirectoryLaborData(serachQuery, startLimit, Commonutility.stringToInteger(getText("end.limit")),rid);
						if (loborDataObj != null) {
							
							String profileimgPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.inner.laborfldr") + getText("external.inner.mobilepath");
							jsonArrObj =  jsonDataPack.laborRegDetail(loborDataObj,profileimgPath);
							if (jsonArrObj != null) {						
								locObjRspdataJson.put("labor_det", jsonArrObj);					
							} else {
								locObjRspdataJson.put("labor_det", jsonArrObj);
							}					
						} else {
							locObjRspdataJson.put("labor_det", jsonArrObj);
						}
						locObjRspdataJson.put("timestamp", timeStamp);
						int totalCnt = skillService.getTotalCountSqlQuery(countQuery);
						locObjRspdataJson.put("totalrecords", Commonutility.intnullChek(totalCnt));
						if (flg) {
							if (totalCnt != 0) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",getText("R0001"),locObjRspdataJson);
							} else {
								locObjRspdataJson = new JSONObject();
								serverResponse(ivrservicecode,getText("status.warning"),"R0025",getText("R0025"),locObjRspdataJson);
							}							
						} else {
							locObjRspdataJson.put("labor_det", jsonArrObj);
							serverResponse(ivrservicecode,getText("status.warning"),"R0006",getText("R0006"),locObjRspdataJson);
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
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", SkillDirectoryListSearch.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",getText("R0003"),locObjRspdataJson);
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
