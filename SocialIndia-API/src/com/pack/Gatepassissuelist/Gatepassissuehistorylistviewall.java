package com.pack.Gatepassissuelist;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDao;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.GatepassEntryTblVO;

public class Gatepassissuehistorylistviewall extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json	
		Session locObjsession = null;
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		StringBuilder locErrorvalStrBuil =null;
		String sqlQury = "";
		String societyKey="";
		String iswebmobilefla ="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			locObjsession = HibernateUtil.getSession();
			CommonMobiDao commonServ = new CommonMobiDaoService();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				Commonutility.toWriteConsole("ivpram:::=== "+ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);

				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				 iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile"); // 1 - mobile, 0 - web, null - web
				 	if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
				 		ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
					} else {
						ivrCurrntusrid = 0;
					}

				 	boolean desflg =false;
				 	if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){

				 		String townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				 		societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				 		// call all mobile check

				 		if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							//success
							Commonutility.toWriteConsole("success=== ");
						//	desflg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
							desflg=commonServ.checkTownshipKey(townShipid,ivrcurrntusridStr);
							Commonutility.toWriteConsole("desflg=== "+desflg);
							if(desflg)
							{
							desflg = commonServ.checkSocietyKey(societyKey, ivrcurrntusridStr);
							}
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							desflg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
						} else {
							desflg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
						}				
					} else {
						desflg = true;
					}

				if(desflg){
					CommonDao ccc =new CommonDao();
					int societyidval= (Integer)ccc.getuniqueColumnVal("SocietyMstTbl", "societyId", "activationKey", societyKey);
					locObjRspdataJson=toBookinglistSelectAll(locObjRecvdataJson,locObjsession,societyidval,ivrCurrntusrid,iswebmobilefla);
					String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					JSONArray issuegatepasshistorydetails=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "issuegatepasshistorydetails");
					Commonutility.toWriteConsole("issuegatepasshistorydetails:::  "+issuegatepasshistorydetails);
						if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
							serverResponse("SI00033","01","E00033",getText("gatepass.selectall.error"),locObjRspdataJson,iswebmobilefla);
						} else {
						if(issuegatepasshistorydetails==null || issuegatepasshistorydetails.length() <=0 ){//gatepass historydetails found
							locObjRspdataJson=null;
							serverResponse("R0001","01","R0001",mobiCommon.getMsg("R0025"),locObjRspdataJson,iswebmobilefla);
							} else {
							serverResponse("0058","00","R0001",getText("R0001"),locObjRspdataJson,iswebmobilefla);
						}

					}
				} else {
					serverResponse("0058","00","R0001",getText("R0001"),locObjRspdataJson,iswebmobilefla);
				}
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI00033, "+getText("request.format.notmach"), "info", Gatepassissuehistorylistviewall.class);
					serverResponse("SI00033","01","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);

				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI00033, "+getText("request.values.empty"), "info", Gatepassissuehistorylistviewall.class);
				serverResponse("SI00033","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson,iswebmobilefla);

			}
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI00033, "+getText("catch.error"), "error", Gatepassissuehistorylistviewall.class);
			serverResponse("SI00033","02","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
		} finally {
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	locErrorvalStrBuil =null;
		}
		return SUCCESS;
	}

	public static JSONObject toBookinglistSelectAll(JSONObject pDataJson, Session pSession,int societyidval,int pCrtnusrid, String pCalfrmMobWeb) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;

		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrMCat_cntQry=null,currentloginid=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null,locToidSLqry=null,gatepassentry_status=null;
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		GatepassEntryTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=0, totalNorow=10;
		//Date lvrEntrydate = null;
		//Date lvrenddate = null;
		Common locCommonObj = null;
		String timestamp="";
		Query lvrQryToidqry=null;
		GatepassEntryTblVO locLabnameObj=null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			locCommonObj=new CommonDao();
			log=new Log();	lbrDtlVoObj=new GatepassEntryTblVO();
			Commonutility.toWriteConsole("Step 1 : gatepass history Type  select all block enter");
			log.logMessage("Step 1 : gatepass history Type  select all block enter", "info", Gatepassissuehistorylistviewall.class);
			String rid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "rid");
			String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startlimit");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String societyId = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "society");
			String lvrPasstyp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "passtype");
			 timestamp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"timestamp");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			String gatepassSts="";

			if (Commonutility.checkempty(societyId)) {

			} else {
				societyId = String.valueOf(societyidval);
			}
			if (Commonutility.checkempty(timestamp)) {
			} else {
				timestamp = Commonutility.timeStampRetStringVal();
			}

			String lvrPtypqry = "";
			if (Commonutility.checkempty(lvrPasstyp)){
				//lvrPtypqry =" and passId.passType = "+lvrPasstyp+" ";
				if(pCalfrmMobWeb!=null && pCalfrmMobWeb.equalsIgnoreCase("1")){
					if(lvrPasstyp!=null && lvrPasstyp.equalsIgnoreCase("1")){
						lvrPtypqry =" and passId.passType = "+lvrPasstyp+" and passId.entryBy ="+pCrtnusrid+" ";
					} else {
						lvrPtypqry =" and passId.passType = "+lvrPasstyp+" and passId.entryBy.societyId.societyId ="+societyidval+" ";
					}
				} else {
					lvrPtypqry =" and passId.passType = "+lvrPasstyp+" ";
				}
			} else {
				lvrPtypqry = "";
			}
			String globalsearch = null;
			String locOrderby =" order by entryDateTime desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					globalsearch = " AND (" + "passId.visitorName like ('%" + loctocheNull+ "%') or "
						  		+ "passId.mobileNo like ('%" + loctocheNull+ "%') "
						  		+ "or passId.gatepassNo like ('%" + loctocheNull + "%'))";
					loc_slQry="from GatepassEntryTblVO  where   entryDateTime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')   " +globalsearch +locOrderby;
				} else {
					 globalsearch = " AND (" + "passId.visitorName like ('%" + loctocheNull+ "%') or "
							  + "passId.mobileNo like ('%" + loctocheNull+ "%') or passId.gatepassNo like ('%" + loctocheNull + "%'))";
					 loc_slQry="from GatepassEntryTblVO  where entryDateTime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')  and entryBy.societyId.societyId=" + Integer.parseInt(societyId) + lvrPtypqry + globalsearch +locOrderby;
				}
			} else {
				
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					loc_slQry="from GatepassEntryTblVO  where  entryDateTime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')  "  + lvrPtypqry + locOrderby;
				} else {

					loc_slQry="from GatepassEntryTblVO  where  entryDateTime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')  and entryBy.societyId.societyId=" + Integer.parseInt(societyId) + lvrPtypqry +locOrderby;
				}

			}
			
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web				
				locvrMCat_cntQry = "select count(entryId) " + loc_slQry;
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+locvrMCat_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrMCat_cntQry, "info", Gatepassissuehistorylistviewall.class);

				merchantCategoryDao IdcardDaoObj=new merchantCategoryDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrMCat_cntQry);
				Commonutility.toWriteConsole("count  : "+count);
				countFilter=count;
			}else{ // for mobile
				 count=0;countFilter=0;
				 Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Gatepassissuehistorylistviewall.class);
			}

				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
			Commonutility.toWriteConsole("Step 3 : gatepass history Type  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : gatepass history Type  Details Query : "+loc_slQry, "info", Gatepassissuehistorylistviewall.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();			
			locLBRJSONAryobj=new JSONArray();
			String filepathvisitor=rb.getString("SOCIAL_INDIA_BASE_URL")+"/templogo/gatepass/web/";
			String filepath=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/";
			String filepathweb=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/web/";
			String facilityimgpathweb=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/"+rb.getString("external.inner.facilityfdr")+"mobile/";			
			String timestampval="";
			while ( locObjLbrlst_itr.hasNext() ) {
				
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (GatepassEntryTblVO) locObjLbrlst_itr.next();
				locInrJSONObj.put("visitor_entry_id", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryId()));
				locInrJSONObj.put("visitor_pass_id", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getPassId()));
				locInrJSONObj.put("visitor_pass_no", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getGatepassNo()));
				locInrJSONObj.put("visitor_name", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getVisitorName()));
				String visitorimg=Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getVisitorImage());
				if(visitorimg!=null && !visitorimg.equalsIgnoreCase("")){
					locInrJSONObj.put("visitor_image", Commonutility.toCheckNullEmpty(filepathvisitor+lbrDtlVoObj.getPassId().getPassId()+"/"+lbrDtlVoObj.getPassId().getVisitorImage()));
				} else {
					locInrJSONObj.put("visitor_image", "");
				}				
				locInrJSONObj.put("pass_type",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getPassType()));				
				locInrJSONObj.put("date_of_birth", locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getPassId().getDateOfBirth(), "yyyy-MM-dd"));				
				if(lbrDtlVoObj.getPassId().getMobileNo()!=null){
					locInrJSONObj.put("visitor_mobile_no", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getMobileNo()));
				}else{
					locInrJSONObj.put("visitor_mobile_no","");
				}
				
				if(lbrDtlVoObj.getPassId().getEmail()!=null && !lbrDtlVoObj.getPassId().getEmail().equalsIgnoreCase("null") && !lbrDtlVoObj.getPassId().getEmail().equalsIgnoreCase("")){
				locInrJSONObj.put("visitor_email",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getEmail()));
				}
				else{
					locInrJSONObj.put("visitor_email","");
				}
				if (lbrDtlVoObj.getPassId().getMvpIdcardTbl() != null) {
					locInrJSONObj.put("visitor_id_type",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getMvpIdcardTbl().getiVOcradid()));
				} else {
					locInrJSONObj.put("visitor_id_type","");
				}
				locInrJSONObj.put("visitor_id_no",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getIdCardNumber()));
				
				locInrJSONObj.put("issue_date",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getIssueDate()));
				locInrJSONObj.put("issue_time",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getIssueTime()));
				
				locInrJSONObj.put("issue_expirydate",locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getPassId().getExpiryDate(), "yyyy-MM-dd"));
				
				if(lbrDtlVoObj.getPassId().getMvpSkillTbl()!=null){
					locInrJSONObj.put("skill_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getMvpSkillTbl().getIvrBnSKILL_ID()));
				}
			
				locInrJSONObj.put("visitor_accompanies",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getNoOfAccompanies()));
				locInrJSONObj.put("vehicle_no",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getVehicleNumber()));
				locInrJSONObj.put("creator_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getEntryBy().getUserId()));
				locInrJSONObj.put("creator_emailid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getEntryBy().getEmailId()));
				locInrJSONObj.put("creator_mobno",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getEntryBy().getMobileNo()));
				locInrJSONObj.put("creator_name",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getEntryBy().getFirstName()));
				if(lbrDtlVoObj.getEntryBy().getImageNameWeb()!=null){
				locInrJSONObj.put("creator_image",Commonutility.toCheckNullEmpty(filepath+lbrDtlVoObj.getEntryBy().getUserId()+"/"+lbrDtlVoObj.getEntryBy().getImageNameWeb()));
				}
				else{
				locInrJSONObj.put("creator_image","");
				}
				String blockname=mobiCommon.getBlockName(lbrDtlVoObj.getEntryBy().getUserId());
				Commonutility.toWriteConsole("blockname:::::::::::::::: "+blockname);
				if(blockname !=null && !blockname.equalsIgnoreCase("") && !blockname.equalsIgnoreCase("null")) {
					locInrJSONObj.put("block_name", blockname);
				} else {
					locInrJSONObj.put("block_name", "");
				}
				String flatno=mobiCommon.getFlatno(lbrDtlVoObj.getEntryBy().getUserId());
				if(flatno !=null && !flatno.equalsIgnoreCase("") && !flatno.equalsIgnoreCase("null"))
				{
					locInrJSONObj.put("flat_no", flatno);
				}
				else
				{
					locInrJSONObj.put("flat_no", "");
				}
				locInrJSONObj.put("other_detail", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getDescription()));

				locInrJSONObj.put("visitor_status",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getVisitorstatus()));
				locInrJSONObj.put("generator_date",locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getPassId().getEntryDatetime(), "yyyy-MM-dd"));
				locInrJSONObj.put("visitor_location",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId().getVisitorLocation()));
				locInrJSONObj.put("expiry_days", rb.getString("visitor.expiry.days"));
				locInrJSONObj.put("visitor_in_datetime",Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getInDatetime(), "yyyy-MM-dd hh:mm:ss")));
				locInrJSONObj.put("visitor_out_datetime", Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getOutDatetime(), "yyyy-MM-dd hh:mm:ss")));				
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null;

			}
			Commonutility.toWriteConsole("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("timestamp", Commonutility.toCheckNullEmpty(timestamp));
			locFinalRTNObj.put("InitCount", Commonutility.toCheckNullEmpty(count));
			locFinalRTNObj.put("countAfterFilter", Commonutility.toCheckNullEmpty(countFilter));
			locFinalRTNObj.put("rowstart", Commonutility.toCheckNullEmpty(startrowfrom));
			locFinalRTNObj.put("totalnorow", Commonutility.toCheckNullEmpty(totalNorow));
			locFinalRTNObj.put("issuegatepasshistorydetails", locLBRJSONAryobj);

			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", Gatepassissuehistorylistviewall.class);
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("issuegatepasshistorydetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			locFinalRTNObj.put("timestamp",timestamp);
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
			count=0; countFilter = 0; startrowfrom = 0; totalNorow = 0;
		}
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
			} else{
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

}
