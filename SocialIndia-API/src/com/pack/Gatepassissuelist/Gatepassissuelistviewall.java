package com.pack.Gatepassissuelist;



import java.io.PrintWriter;
import java.util.Date;
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
import com.socialindiaservices.vo.MvpGatepassDetailTbl;

public class Gatepassissuelistviewall extends ActionSupport {

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
		String lsvSlQry = null;
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
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				 iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile"); // 1 - mobile, 0 - web, null - web


				if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
					ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
				}else{
					ivrCurrntusrid=0;
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
					//desflg =  true;

					//desflg =  false;
				} else{
					desflg = true;
				}

				if(desflg){
					CommonDao ccc =new CommonDao();

					int societyidval= (Integer)ccc.getuniqueColumnVal("SocietyMstTbl", "societyId", "activationKey", societyKey);

					locObjRspdataJson=toBookinglistSelectAll(locObjRecvdataJson,locObjsession,societyidval,ivrCurrntusrid,iswebmobilefla);
					String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					JSONArray issuegatepassdetails=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "issuegatepassdetails");
					Commonutility.toWriteConsole("issuegatepassdetails:::  "+issuegatepassdetails);
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						serverResponse("SI00033","01","E00033",getText("gatepass.selectall.error"),locObjRspdataJson,iswebmobilefla);
					} else{
						if(issuegatepassdetails==null || issuegatepassdetails.length() <=0 ){//bookingdetails found
							locObjRspdataJson=null;
							serverResponse("R0001","01","R0001",mobiCommon.getMsg("R0025"),locObjRspdataJson,iswebmobilefla);
						}
						else
						{
							serverResponse("0058","00","R0001",getText("R0001"),locObjRspdataJson,iswebmobilefla);
						}

					}
				} else {
					serverResponse("0058","00","R0001",getText("R0001"),locObjRspdataJson,iswebmobilefla);
				}




				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI00033, "+getText("request.format.notmach"), "info", Gatepassissuelistviewall.class);
					serverResponse("SI00033","01","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);

				}
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI00033, "+getText("request.values.empty"), "info", Gatepassissuelistviewall.class);
				serverResponse("SI00033","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson,iswebmobilefla);

			}
		}catch(Exception e){
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI00033, "+getText("catch.error"), "error", Gatepassissuelistviewall.class);
			serverResponse("SI00033","02","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	locErrorvalStrBuil =null;
		}
		return SUCCESS;
	}

	public static JSONObject toBookinglistSelectAll(JSONObject pDataJson, Session pSession,int societyidval, int pCrtnusrid, String pCalfrmMobWeb) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;

		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrMCat_cntQry=null,currentloginid=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null,locToidSLqry=null,gatepassentry_status=null;
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		MvpGatepassDetailTbl lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=0, totalNorow=10;
		Date lvrEntrydate = null;
		Date lvrenddate = null;
		Common locCommonObj = null;
		String timestamp="";
		Query lvrQryToidqry=null;
		GatepassEntryTblVO locLabnameObj=null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			locCommonObj=new CommonDao();
			log=new Log();	lbrDtlVoObj=new MvpGatepassDetailTbl();
			Commonutility.toWriteConsole("Step 1 : booking Type  select all block enter");
			log.logMessage("Step 1 : booking Type  select all block enter", "info", Gatepassissuelistviewall.class);
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
			String bookingSts="";

			if (Commonutility.checkempty(societyId)) {

			} else {
				societyId=String.valueOf(societyidval);}
			if (Commonutility.checkempty(timestamp)) {
			} else {
				timestamp=Commonutility.timeStampRetStringVal();
			}

			String lvrPtypqry = "";
			if (Commonutility.checkempty(lvrPasstyp)){
				//lvrPtypqry =" and passType = "+lvrPasstyp+" ";
				if(pCalfrmMobWeb!=null && pCalfrmMobWeb.equalsIgnoreCase("1")){
					if(lvrPasstyp!=null && lvrPasstyp.equalsIgnoreCase("1")){						
						lvrPtypqry =" and passType = "+lvrPasstyp+" and entryBy ="+rid+" ";
					} else {
						lvrPtypqry =" and passType = "+lvrPasstyp+" and entryBy.societyId.societyId ="+societyidval+" ";
					}
				} else {
					lvrPtypqry =" and passType = "+lvrPasstyp+" ";
				}
			} else {
				lvrPtypqry = "";
			}
			String globalsearch = null;
//			String locOrderby = " order by visitorstatus asc";
			String locOrderby = " order by entryDatetime desc";
			if (loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
						globalsearch = " AND (" + "visitorName like ('%" + loctocheNull+ "%') or "
								     + "mobileNo like ('%" + loctocheNull+ "%') "
								     + "or gatepassNo like ('%" + loctocheNull + "%'))";
				  loc_slQry="from MvpGatepassDetailTbl  where   entryDatetime < STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and statusFlag =1 " + lvrPtypqry + globalsearch +locOrderby;
				} else {
					  globalsearch = " AND (" + "visitorName like ('%" + loctocheNull+ "%') or "
							  + "mobileNo like ('%" + loctocheNull+ "%') or gatepassNo like ('%" + loctocheNull + "%'))";
					  loc_slQry="from MvpGatepassDetailTbl  where entryDatetime < STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and statusFlag =1 and entryBy.societyId.societyId=" + Integer.parseInt(societyId) + lvrPtypqry + globalsearch +locOrderby;
				}
			} else {
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					loc_slQry="from MvpGatepassDetailTbl  where  entryDatetime < STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and statusFlag =1 " + lvrPtypqry + locOrderby;
				} else {

					loc_slQry="from MvpGatepassDetailTbl  where  entryDatetime < STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and statusFlag =1  and entryBy.societyId.societyId=" + Integer.parseInt(societyId) + lvrPtypqry + locOrderby;
				}

			}
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web
				/*if (loctocheNull.equalsIgnoreCase("")) {
					if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					locvrMCat_cntQry="select count(passId) from MvpGatepassDetailTbl where  entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and statusFlag =1 ";
					} else {
						locvrMCat_cntQry="select count(passId) from MvpGatepassDetailTbl where   entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and statusFlag =1  and entryBy.societyId.societyId=" + Integer.parseInt(societyId);
					}
				} else {
					if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					locvrMCat_cntQry="select count(passId) from MvpGatepassDetailTbl where    statusFlag =1 and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') AND (" + "visitorName like ('%" + loctocheNull+ "%') or "
						  + "mobileNo like ('%" + loctocheNull+ "%') or "
						  + "visitorName like ('%" + loctocheNull+ "%') or "
						  + "gatepassNo like ('%" + loctocheNull+ "%') or gatepassNo like ('%" + loctocheNull + "%'))";
					} else {
						locvrMCat_cntQry="select count(passId) from MvpGatepassDetailTbl where   entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')  and statusFlag =1  and entryBy.societyId.societyId=" + Integer.parseInt(societyId)+" AND (" + "visitorName like ('%" + loctocheNull+ "%') or "
								  + "mobileNo like ('%" + loctocheNull+ "%') or "
								  + "visitorName like ('%" + loctocheNull+ "%') or "
								  + "gatepassNo like ('%" + loctocheNull+ "%') or gatepassNo like ('%" + loctocheNull + "%'))";
					}
				} */
				locvrMCat_cntQry = " select count(passId) "+loc_slQry;
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+locvrMCat_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrMCat_cntQry, "info", Gatepassissuelistviewall.class);

				merchantCategoryDao IdcardDaoObj=new merchantCategoryDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrMCat_cntQry);
				Commonutility.toWriteConsole("count  : "+count);
				countFilter=IdcardDaoObj.getTotalFilter(locvrMCat_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Gatepassissuelistviewall.class);
			}

				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
			Commonutility.toWriteConsole("Step 3 : booking Type  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : booking Type  Details Query : "+loc_slQry, "info", Gatepassissuelistviewall.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			Commonutility.toWriteConsole("locObjLbrlst_itr=== : "+locObjLbrlst_itr.toString());

			locLBRJSONAryobj=new JSONArray();
			String filepathvisitor=rb.getString("SOCIAL_INDIA_BASE_URL")+"/templogo/gatepass/web/";
			String filepath=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/";
			String filepathweb=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/web/";
			String facilityimgpathweb=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/"+rb.getString("external.inner.facilityfdr")+"mobile/";			
			String timestampval="";			
			while ( locObjLbrlst_itr.hasNext() ) {				
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (MvpGatepassDetailTbl) locObjLbrlst_itr.next();
				locInrJSONObj.put("visitor_pass_id", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassId()));
				locInrJSONObj.put("visitor_pass_no", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getGatepassNo()));
				locInrJSONObj.put("visitor_name", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getVisitorName()));
				String visitorimg=Commonutility.toCheckNullEmpty(lbrDtlVoObj.getVisitorImage());
				if(visitorimg!=null && !visitorimg.equalsIgnoreCase("")){
				locInrJSONObj.put("visitor_image", Commonutility.toCheckNullEmpty(filepathvisitor+lbrDtlVoObj.getPassId()+"/"+lbrDtlVoObj.getVisitorImage()));
				}else
				{
					locInrJSONObj.put("visitor_image", "");
				}
				Commonutility.toWriteConsole("1---");
				locInrJSONObj.put("pass_type",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPassType()));
				Commonutility.toWriteConsole("2---");
				locInrJSONObj.put("date_of_birth", locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getDateOfBirth(), "yyyy-MM-dd"));
				Commonutility.toWriteConsole("3---");
				if(lbrDtlVoObj.getMobileNo()!=null){
					locInrJSONObj.put("visitor_mobile_no", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getMobileNo()));
				}else{
					locInrJSONObj.put("visitor_mobile_no","");
				}
				Commonutility.toWriteConsole("4---");
				if(lbrDtlVoObj.getEmail()!=null && !lbrDtlVoObj.getEmail().equalsIgnoreCase("null") && !lbrDtlVoObj.getEmail().equalsIgnoreCase("")){
				locInrJSONObj.put("visitor_email",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEmail()));
				}
				else{
					locInrJSONObj.put("visitor_email","");
				}
				Commonutility.toWriteConsole("5---");
				if(lbrDtlVoObj.getMvpIdcardTbl()!=null)
				{
				locInrJSONObj.put("visitor_id_type",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getMvpIdcardTbl().getiVOcradid()));
				}
				else
				{
					locInrJSONObj.put("visitor_id_type","");
				}
				Commonutility.toWriteConsole("6---");
				locInrJSONObj.put("visitor_id_no",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIdCardNumber()));
				Commonutility.toWriteConsole("7---");
				locInrJSONObj.put("issue_date",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueDate()));
				locInrJSONObj.put("issue_time",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueTime()));
				Commonutility.toWriteConsole("8---");
				locInrJSONObj.put("issue_expirydate",locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getExpiryDate(), "yyyy-MM-dd"));
				Commonutility.toWriteConsole("9---");
				if(lbrDtlVoObj.getMvpSkillTbl()!=null){
				locInrJSONObj.put("skill_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getMvpSkillTbl().getIvrBnSKILL_ID()));
				}
				Commonutility.toWriteConsole("10---");
				locInrJSONObj.put("visitor_accompanies",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getNoOfAccompanies()));
				locInrJSONObj.put("vehicle_no",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getVehicleNumber()));
				locInrJSONObj.put("creator_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getUserId()));
				locInrJSONObj.put("creator_emailid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getEmailId()));
				locInrJSONObj.put("creator_mobno",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getMobileNo()));
				locInrJSONObj.put("creator_name",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getFirstName()));
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
				String flatno = mobiCommon.getFlatno(lbrDtlVoObj.getEntryBy().getUserId());
				if(flatno !=null && !flatno.equalsIgnoreCase("") && !flatno.equalsIgnoreCase("null")) {
					locInrJSONObj.put("flat_no", flatno);
				} else {
					locInrJSONObj.put("flat_no", "");
				}
				locInrJSONObj.put("other_detail", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDescription()));
//				if (lbrDtlVoObj.getPassId()!=null) {
//					 locToidSLqry="from GatepassEntryTblVO where passId ="+lbrDtlVoObj.getPassId()+"";
//					 lvrQryToidqry=pSession.createQuery(locToidSLqry);
//					 locLabnameObj=(GatepassEntryTblVO) lvrQryToidqry.uniqueResult();
//					 	if(locLabnameObj!=null){
//							 locLabnameObj=new GatepassEntryTblVO();
//							 gatepassentry_status=Commonutility.toCheckNullEmpty(locLabnameObj.getStatus());
//
//							 locInrJSONObj.put("visitor_status",gatepassentry_status);
//					 	}
//					 	else{
//					 	if(lbrDtlVoObj.getExpiryDate()!=null){
//					 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//					 	String expdate = locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getExpiryDate(), "yyyy-MM-dd");
//					 	Date curdate =Commonutility.getCurrentDateTime("yyyy-MM-dd");
//					 	 String stringDate = sdf.format(curdate);
//					            Date date1 = sdf.parse(expdate);
//					            Date date2 = sdf.parse(stringDate);
//					            if(date1.after(date2)){
//					            	locInrJSONObj.put("visitor_status","1");
//					            }
//					            else{
//					            	locInrJSONObj.put("visitor_status","3");
//					            }
//
//					 	}
//					 	}
//					}
				locInrJSONObj.put("visitor_status",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getVisitorstatus()));
				locInrJSONObj.put("generator_date",locCommonObj.getDateobjtoFomatDateStr(lbrDtlVoObj.getEntryDatetime(), "yyyy-MM-dd"));
				locInrJSONObj.put("visitor_location",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getVisitorLocation()));
				locInrJSONObj.put("expiry_days", Commonutility.toCheckNullEmpty(rb.getString("visitor.expiry.days")));
				Commonutility.toWriteConsole("locInrJSONObj======== "+locInrJSONObj);
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
			locFinalRTNObj.put("issuegatepassdetails", locLBRJSONAryobj);

			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Step -1 : Exception found in Gatepassissuelistviewall.class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", Gatepassissuelistviewall.class);
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("issuegatepassdetails", "");
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

	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla)
	{
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

}
