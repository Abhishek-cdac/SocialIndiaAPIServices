package com.mobi.contents;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.publishskill.ListSearch;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtilFunctions;
import com.socialindiaservices.common.CommonUtils;

public class FlashNewsList extends ActionSupport{
private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
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
			String locRid = null;
			String searchQury = "";
			String societyId="";
			int totCnt = 0;
			log.logMessage("Enter into FlashNewsList ", "info", ListSearch.class);
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
						societyId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"society_id");
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
					if (Commonutility.checkempty(societyId)) {
				
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societyid.error")));
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
						List<FlashNewsTblVO> flashNewsList=new ArrayList<FlashNewsTblVO>();
						CommonUtilFunctions commonDateFunc=new CommonUtilFunctions();
						String dateExpFormat="yyyy-MM-dd HH:mm:ss";
						Date dt=new Date();
						String curDate=commonDateFunc.dateToStringModify(dt,dateExpFormat);
						//String searchField="where status=1 and  expiryDate <='"+curDate+"' ";
						String searchField="where status=1 ";
						if(societyId!=null && societyId.length()>0){
							searchField+=" and societyId.activationKey='"+societyId+"'";
						}
						String totcountqry="select count(*) from FlashNewsTblVO "+searchField;
						int totalcnt=commonHbm.getTotalCountQuery(totcountqry);
						String locVrSlQry="";
						locVrSlQry="from FlashNewsTblVO "+searchField+"  order by newsId desc";
						ContentDao contentHbm=new ContentDaoServices();
						flashNewsList=contentHbm.getFlashNewsList(locVrSlQry);
						JSONObject finalJsonarr=new JSONObject();
						locObjRspdataJson=new JSONObject();
						JSONArray jArray =new JSONArray();
						
						
						
						if(flashNewsList!=null && flashNewsList.size()>0){
							FlashNewsTblVO flashNewsObj;
							String dateFormat="yyyy-MM-dd HH:mm:ss";
							CommonUtils commonjvm=new CommonUtils();
							Date curCate=new Date();
							for(Iterator<FlashNewsTblVO> it=flashNewsList.iterator();it.hasNext();){
								flashNewsObj=it.next();
								finalJsonarr=new JSONObject();
								finalJsonarr.put("news_id", flashNewsObj.getNewsId());
								finalJsonarr.put("society_id", flashNewsObj.getSocietyId().getSocietyId());
								finalJsonarr.put("news_content", flashNewsObj.getNewsContent());
								finalJsonarr.put("expiry_date", commonjvm.dateToStringModify(flashNewsObj.getExpiryDate(),dateFormat));
								Date expdt=flashNewsObj.getExpiryDate();
								if(expdt.compareTo(curCate) > 0){
									finalJsonarr.put("is_expired", "1");
								}else{
									finalJsonarr.put("is_expired", "0");
								}
								if (Commonutility.checkempty(flashNewsObj.getTitle())) {
									finalJsonarr.put("flash_title", flashNewsObj.getTitle());
								} else {
									finalJsonarr.put("flash_title", "");
								}
								if (Commonutility.checkempty(flashNewsObj.getNewsimageName())) {
									finalJsonarr.put("img_url",System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/flashnews/mobile/"+flashNewsObj.getNewsId()+"/"+flashNewsObj.getNewsimageName());
								} else {
									finalJsonarr.put("img_url","");
								}
								jArray.put(finalJsonarr);
							}
							
						locObjRspdataJson.put("flash_news", jArray);
						locObjRspdataJson.put("totalrecords",totalcnt);
						serverResponse(ivrservicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
						}else{
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