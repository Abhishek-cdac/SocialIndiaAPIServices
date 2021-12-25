package com.pack.Gatepassissuelist;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;

public class GatepassEntry extends ActionSupport{

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
		String is_mobile=null, visitor_pass_no=null;
		String loc_slQry=null;
		Query locQryObj= null;
		Session locSession=null;
		MvpGatepassDetailTbl locLDTblvoObj=null;
		Common locCommonObj = null;
		try {
			locCommonObj=new CommonDao();
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			int startLimit = 0;
			Date timeStamp = null;
			String locRid = null;
			String locTimeStamp = null;
			String locStartLmit = null,filepathvisitor=null;
			locSession=HibernateUtil.getSession();
			log.logMessage("Enter into GatepassEntry ", "info", GatepassEntry.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("GatepassEntry ivrparams :" + ivrparams, "info", GatepassEntry.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						 visitor_pass_no = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "visitor_pass_no");
						 is_mobile=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");//1-mob,0-web
					}
					 filepathvisitor=getText("SOCIAL_INDIA_BASE_URL")+"/templogo/gatepass/web/";
					if(is_mobile!=null && !is_mobile.equalsIgnoreCase("0")){
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


					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
				}
					 else
					 {
						 flg=true;
					 }
				}else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("flg :" +flg, "info", GatepassEntry.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				if(is_mobile !=null && !is_mobile.equalsIgnoreCase("0")){
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				}
				else{
					flg =true;
				}
				if(flg){
					if(is_mobile !=null && !is_mobile.equalsIgnoreCase("0")){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					}
					else{
						flg =true;
					}
					if (flg) {
						if (flg) {
							//insert code
							Integer gatepass_no_check =mobiCommon.checkgatepass_no(visitor_pass_no);
							if(gatepass_no_check > 0){
							System.out.println("gatepass_in_out_cnt== "+gatepass_no_check);
							loc_slQry="from MvpGatepassDetailTbl where gatepassNo='"+visitor_pass_no+"' ";
							System.out.println("loc_slQry  :  "+loc_slQry);
							locQryObj=locSession.createQuery(loc_slQry);
							//locLPTblvoObj=(LaborProfileTblVO) locQryObj.uniqueResult();
							locLDTblvoObj = (MvpGatepassDetailTbl)locQryObj.uniqueResult();
							System.out.println("Step 2 : Select labor detail query executed."+locLDTblvoObj.getPassId());
							String flatno=mobiCommon.getFlatno(locLDTblvoObj.getEntryBy().getUserId());
							locObjRspdataJson.put("rid", Commonutility.toCheckNullEmpty(rid));

							locObjRspdataJson.put("visitor_name",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getVisitorName()));
							locObjRspdataJson.put("visitor_mobile_no",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getMobileNo()));
							if(is_mobile!=null && is_mobile.equalsIgnoreCase("1"))
							{
							locObjRspdataJson.put("date_of_birth",  Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(locLDTblvoObj.getDateOfBirth(), "yyyy-MM-dd")));
							}
							else
							{
								locObjRspdataJson.put("date_of_birth",  Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(locLDTblvoObj.getDateOfBirth(), "dd-MM-yyyy")));
							}
							locObjRspdataJson.put("visitor_email",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getEmail()));
							if(locLDTblvoObj.getMvpIdcardTbl()!=null)
							{
								locObjRspdataJson.put("visitor_id_type",Commonutility.toCheckNullEmpty(locLDTblvoObj.getMvpIdcardTbl().getiVOcradid()));
								locObjRspdataJson.put("idcard_name",Commonutility.toCheckNullEmpty(locLDTblvoObj.getMvpIdcardTbl().getiVOcradname()));
							}
							else
							{
								locObjRspdataJson.put("visitor_id_type","");
								locObjRspdataJson.put("idcard_name","");
							}

							locObjRspdataJson.put("visitor_id_no",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getIdCardNumber()));
							locObjRspdataJson.put("visitor_accompanies",  Commonutility.toCheckNullEmpty(String.valueOf(locLDTblvoObj.getNoOfAccompanies())));
							if(is_mobile!=null && is_mobile.equalsIgnoreCase("1"))
							{
							locObjRspdataJson.put("issue_date",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getIssueDate()));
							}
							else{
								locObjRspdataJson.put("issue_date",  Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(locLDTblvoObj.getIssueDate(), "dd-MM-yyyy")));
							}
							locObjRspdataJson.put("issue_time",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getIssueTime()));
							if(locLDTblvoObj.getPassType() ==1 )
							{
								locObjRspdataJson.put("expiry_days", getText("visitor.expiry.days"));

							}
							else{
								if(is_mobile!=null && is_mobile.equalsIgnoreCase("1"))
								{
								locObjRspdataJson.put("issue_expirydate",  Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(locLDTblvoObj.getExpiryDate(), "yyyy-MM-dd")));
								}
								else{
									locObjRspdataJson.put("issue_expirydate",  Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(locLDTblvoObj.getExpiryDate(), "dd-MM-yyyy")));
								}
								}


							if(locLDTblvoObj.getMvpSkillTbl()!=null)
							{
								locObjRspdataJson.put("skill_id",  Commonutility.toCheckNullEmpty(String.valueOf(locLDTblvoObj.getMvpSkillTbl().getIvrBnSKILL_ID())));
								locObjRspdataJson.put("skills_name",Commonutility.toCheckNullEmpty(locLDTblvoObj.getMvpSkillTbl().getIvrBnSKILL_NAME()));

							}
							else
							{
								locObjRspdataJson.put("skill_id","");
								locObjRspdataJson.put("skills_name","");
							}


							locObjRspdataJson.put("other_detail",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getDescription()));
							locObjRspdataJson.put("pass_type",  Commonutility.toCheckNullEmpty(String.valueOf(locLDTblvoObj.getPassType())));
							locObjRspdataJson.put("vehicle_no",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getVehicleNumber()));
							locObjRspdataJson.put("visitor_pass_no",  Commonutility.toCheckNullEmpty(locLDTblvoObj.getGatepassNo()));
							locObjRspdataJson.put("visitor_pass_id",  Commonutility.toCheckNullEmpty(String.valueOf(locLDTblvoObj.getPassId())));
							String blockname=mobiCommon.getBlockName(locLDTblvoObj.getEntryBy().getUserId());
								if(blockname !=null && !blockname.equalsIgnoreCase("") && !blockname.equalsIgnoreCase("null")) {
									locObjRspdataJson.put("block_name", Commonutility.toCheckNullEmpty(blockname));
								} else {
									locObjRspdataJson.put("block_name", "");
								}
								if(flatno !=null && !flatno.equalsIgnoreCase("") && !flatno.equalsIgnoreCase("null")) {
									locObjRspdataJson.put("flat_no",  Commonutility.toCheckNullEmpty(flatno));
								} else {
									locObjRspdataJson.put("flat_no",  "");
								}
							System.out.println("----- "+flatno);
							String soclogoimg=locLDTblvoObj.getEntryBy().getSocietyId().getLogoImage();
							int societyid =locLDTblvoObj.getEntryBy().getSocietyId().getSocietyId();
							System.out.println("societyid::::::::::  "+societyid);
							if(soclogoimg!=null && soclogoimg.length()>0){
							locObjRspdataJson.put("society_logo",  Commonutility.toCheckNullEmpty(getText("external.uploadfile.society.mobilepath")+societyid+"/"+soclogoimg));
							}
							else{
								locObjRspdataJson.put("society_logo", "");
							}
							locObjRspdataJson.put("generator_date",  Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(locLDTblvoObj.getEntryDatetime(), "yyyy-MM-dd")));
							String visitorimg=Commonutility.toCheckNullEmpty(locLDTblvoObj.getVisitorImage());
							if(visitorimg!=null && !visitorimg.equalsIgnoreCase("")){
								locObjRspdataJson.put("visitor_image", Commonutility.toCheckNullEmpty(filepathvisitor+locLDTblvoObj.getPassId()+"/"+locLDTblvoObj.getVisitorImage()));
								locObjRspdataJson.put("imageName", Commonutility.toCheckNullEmpty(locLDTblvoObj.getVisitorImage()));
							}else
							{
								locObjRspdataJson.put("visitor_image", "");
								locObjRspdataJson.put("imageName", "");
							}
							locObjRspdataJson.put("visitor_location",Commonutility.toCheckNullEmpty(locLDTblvoObj.getVisitorLocation()));

							serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson,is_mobile);
							} else {
								serverResponse(ivrservicecode,"01","R0216",mobiCommon.getMsg("R0216"),locObjRspdataJson,is_mobile);
							}
						} else {
							serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0006"),locObjRspdataJson,is_mobile);
						}
					} else {
						locObjRspdataJson=new JSONObject();
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson,is_mobile);
					}
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson,is_mobile);
				}

			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson,is_mobile);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", GatepassEntry.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson,is_mobile);
		} finally {
			loc_slQry=null;locQryObj= null;
			if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;}
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla) {
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();
		try {
			if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
				out = response.getWriter();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				mobiCommon mobicomn=new mobiCommon();
				String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
				out.print(as);
				out.close();
			}
			else{
			out = response.getWriter();
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			out.print(as);
			out.close();
			}
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
