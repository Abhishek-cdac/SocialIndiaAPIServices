package com.pack.labor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.feedbackvo.FeedbackTblVO;
import com.pack.laborvo.LaborDetailsTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;

public class LaborSkilledSearchDetailsview extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;

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
		String societyKey="";
		String iswebmobilefla=null;
		try{
			locErrorvalStrBuil = new StringBuilder();
			locObjsession = HibernateUtil.getSession();
			CommonMobiDao commonServ = new CommonMobiDaoService();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");
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
							System.out.println("success=== ");
						//	desflg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
							desflg=commonServ.checkTownshipKey(townShipid,ivrcurrntusridStr);
							System.out.println("desflg=== "+desflg);
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
				if(ivrservicecode.equalsIgnoreCase("SI3005"))
				{
					CommonDao ccc =new CommonDao();

					int societyidval= (Integer)ccc.getuniqueColumnVal("SocietyMstTbl", "societyId", "activationKey", societyKey);
				locObjRspdataJson=toLBRSelectAll(locObjRecvdataJson,locObjsession,societyidval);
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
				JSONArray labourdetails=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "labordetails");
				if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
					//AuditTrial.toWriteAudit(getText("LBAD014"), "LBAD014", ivrCurrntusrid);
					serverResponse("SI3005","0","E3005",getText("labor.selectall.error"),locObjRspdataJson,iswebmobilefla);
				}else{
					//AuditTrial.toWriteAudit(getText("LBAD013"), "LBAD013", ivrCurrntusrid);
					if(labourdetails==null || labourdetails.length() <=0 ){//bookingdetails found
						locObjRspdataJson=null;
						serverResponse("SI00033","01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson,iswebmobilefla);
					}
					else
					{
						serverResponse("SI3005","00","SI3005",getText("labor.selectall.success"),locObjRspdataJson,iswebmobilefla);
					}
				}
				}
				else
				{
					locObjRspdataJson=toLBRSelectAllCmplt(locObjRecvdataJson,locObjsession);
					String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					JSONArray labourdetails=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "labordetails");
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("LBAD014"), "LBAD014", ivrCurrntusrid);
						serverResponse("SI3005","0","E3005",getText("labor.selectall.error"),locObjRspdataJson,iswebmobilefla);
					}else{
						if(labourdetails==null || labourdetails.length() <=0 ){//bookingdetails found
							locObjRspdataJson=null;
							serverResponse("SI00033","01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson,iswebmobilefla);
						}
						else
						{
							serverResponse("SI3005","00","SI3005",getText("labor.selectall.success"),locObjRspdataJson,iswebmobilefla);
						}
					}
				}
				}



				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI3005, "+getText("request.format.notmach"), "info", LaborSkilledSearchDetailsview.class);
					serverResponse("SI3005","1","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);

				}
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3005, "+getText("request.values.empty"), "info", LaborSkilledSearchDetailsview.class);
				serverResponse("SI3005","1","ER0001",getText("request.values.empty"),locObjRspdataJson,iswebmobilefla);

			}
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3005, "+getText("catch.error"), "error", LaborSkilledSearchDetailsview.class);
			serverResponse("SI3005","2","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;
		}
		return SUCCESS;
	}

	public static JSONObject toLBRSelectAll(JSONObject pDataJson, Session pSession,int societyidval) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;

		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;
		Log log=null;
		Iterator locObjLbrlst_itr=null,locObjLbrratinglst_itr=null;
		LaborDetailsTblVO lbrDtlVoObj=null;
		String locvrLBR_ID=null,locvrLBR_SERVICE_ID=null, locvrLBR_name=null, locvrLBR_Email=null;
		int count=0,countFilter=0, startrowfrom=0, totalNorow=10;

		String loc_slQry_categry=null,loc_slQry_Rating=null;
		Iterator locObjLbrcateglst_itr=null;
		LaborSkillTblVO locLbrSkiltbl=null;
		IssueTblVO locLbrIssReptbl=null;
		FeedbackTblVO locLbrrattingtbl=null;
		JSONArray locLBRSklJSONAryobj=null,locLBRSklJSONAryobj1=null,locLBRRATTINGJSONAryobj=null,locLBRISSREPJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null,locInrLbrSklJSONObj1=null,locInrLbrIRepJSONObj1=null;
		JSONObject locInrLbrRattingJSONObj=null;
		List<Object[]> nwListObj = null;
		JSONArray lvrEventjsonaryobj = null;
		try {
			nwListObj = new ArrayList<Object[]>();
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();	lbrDtlVoObj=new LaborDetailsTblVO();
			System.out.println("Step 1 : labor select all block enter");
			log.logMessage("Step 1 : labor select all block enter", "info", LaborSkilledSearchDetailsview.class);
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");

			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			String societyId = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "society");
			String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"timestamp");
			String skilledsearch = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isskilledsearch");
			String ismobileno = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ismobileno");
			String isservicebookno = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isservicebookno");
			String iscatgid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "iscatgid");
			String isworktype = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isworktype");
			String isratting = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "isratting");
			String skilledsearchflg="";
			System.out.println("*************skilledsearch========= "+skilledsearch);
			if (Commonutility.checkempty(societyId)) {

			} else {
				societyId=String.valueOf(societyidval);}
			if (Commonutility.checkempty(timestamp)) {
			} else {
				timestamp=Commonutility.timeStampRetStringVal();
			}



			 if(locvrCntflg!=null  &&  (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){ // search  service booking......
				System.out.println("skilledsearch=condition======== "+skilledsearch);
				if(ismobileno!=null && ismobileno.length() > 0){
					System.out.println("search skilled1****");
					skilledsearchflg +=" and ivrBnLBR_PH_NO='"+ismobileno+"' ";
				}
				if(isworktype!=null && isworktype.length() > 0){
					System.out.println("search skilled isworktype****");
					skilledsearchflg +=" and wrktypId='"+isworktype+"' ";
				}
				if(isratting!=null  && isratting.length() > 0){
					System.out.println("search skilled ivrBnLBR_RATING****");
					skilledsearchflg +=" and ivrBnLBR_RATING='"+isratting+"' ";
				}
				if(isservicebookno!=null && isservicebookno.length() > 0){//service booking no in labour
					//List<Object[]> bookingObj = new ArrayList<Object[]>();
					List bookingObj;
					String sqlQuery ="select LABOUR_ID from mvp_service_booking_tbl where BOOKING_ID="+isservicebookno+"";
			System.out.println("sqlQuery::" + sqlQuery);
			Query queryObj = pSession.createSQLQuery(sqlQuery);
//			queryObj.setParameter("USERID", bookingnewObj.getUsrId());
			bookingObj = queryObj.list();
			System.out.println("bookingObj== "+bookingObj.get(0));
					skilledsearchflg +=" and ivrBnLBR_ID='"+bookingObj.get(0)+"' ";
				}
				if(iscatgid!=null && iscatgid.length() > 0){
					System.out.println("search skilled category****");
					if(ismobileno!=null && ismobileno.length() > 0 ){
					//String skilledsearchflg1 =" and iVOCATEGORY_ID.iVOCATEGORY_ID='"+iscatgid+"' ";
					locvrLBR_cntQry="select  count(*) from mvp_lbr_reg_tbl  lbr_reg left join mvp_lbr_skill_tbl lbr_skill on lbr_reg.lbr_id=lbr_skill.lbr_id where  lbr_skill.category_id="+iscatgid+" and  lbr_reg.LBR_PH_NO="+ismobileno+" group by lbr_reg.LBR_ID ";
					CommonMobiDao lbrDaoObj=new CommonMobiDaoService();
					count=lbrDaoObj.getTotalCountSqlQuery(locvrLBR_cntQry);
					System.out.println("count-***-- "+count);
					countFilter=count;
					}else{
						locvrLBR_cntQry="select count(*) from mvp_lbr_reg_tbl  lbr_reg left join mvp_lbr_skill_tbl lbr_skill on lbr_reg.lbr_id=lbr_skill.lbr_id where lbr_skill.category_id="+iscatgid+"  group by lbr_reg.LBR_ID  ";
						CommonMobiDao lbrDaoObj=new CommonMobiDaoService();
						count=lbrDaoObj.getTotalCountSqlQuery(locvrLBR_cntQry);
						System.out.println("count--- "+count);
						countFilter=count;
					}
				}else{

				locvrLBR_cntQry="select count(ivrBnLBR_ID) from LaborDetailsTblVO where ivrBnLBR_STS="+locvrLBR_STS+" "+skilledsearchflg+" and ivrBnENTRY_DATETIME<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')";

				LaborDaoservice lbrDaoObj=new LaborDaoservice();
				count=lbrDaoObj.getInitTotal(locvrLBR_cntQry);
				countFilter=count;
				}

			}
			else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LaborSkilledSearchDetailsview.class);
			}

				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}

				String globalsearch=null;
				String locOrderby =" order by ivrBnENTRY_DATETIME desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "ivrBnLBR_NAME like ('%" + loctocheNull+ "%') or "
			                                + "ivrBnLBR_PH_NO like ('%" + loctocheNull + "%')  or "
			                                + "ivrBnLBR_EMAIL like ('%" + loctocheNull + "%')  "
			                               // + "ivrBNLbrSkilObj.iVOCATEGORY_ID.iVOCATEGORY_NAME like ('%" + loctocheNull + "%') or "
			                                + ")";
				  loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS=" + locvrLBR_STS + "  and ivrBnENTRY_DATETIME<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') " + globalsearch + " " + locOrderby;
				  locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			}else{
				if(ismobileno!=null && ismobileno.length() > 0){
					System.out.println("search skilled1****");
					skilledsearchflg +=" and ivrBnLBR_PH_NO='"+ismobileno+"' ";
				}
				if(isworktype!=null && isworktype.length() > 0){
					System.out.println("search skilled isworktype****");
					skilledsearchflg +=" and wrktypId='"+isworktype+"' ";
				}
				if(isratting!=null  && isratting.length() > 0){
					System.out.println("search skilled ivrBnLBR_RATING****");
					skilledsearchflg +=" and ivrBnLBR_RATING='"+isratting+"' ";
				}
				if(iscatgid!=null && iscatgid.length() > 0){
					System.out.println("search skilled category!!!!****");
					if(ismobileno!=null && ismobileno.length() > 0 ){
					//String skilledsearchflg1 =" and iVOCATEGORY_ID.iVOCATEGORY_ID='"+iscatgid+"' ";
						loc_slQry="select  lbr_reg.LBR_ID, lbr_reg.LBR_SERVICE_ID, lbr_reg.LBR_NAME, lbr_reg.LBR_EMAIL, lbr_reg.LBR_PH_NO, lbr_reg.LBR_ISD_CODE, lbr_reg.CARD_ID,lbr_reg.ID_CARD_NO, lbr_reg.LBR_ADD_1, lbr_reg.LBR_ADD_2,lbr_reg.VERIFIED_STATUS, lbr_reg.KEY_FOR_SEARCH, lbr_reg.IMAGE_NAME_WEB, lbr_reg.IMAGE_NAME_MOBILE, lbr_reg.WORK_TYPE_ID, lbr_reg.LBR_DESCP, lbr_reg.PSTL_ID, lbr_reg.CITY_ID, lbr_reg.STATE_ID, lbr_reg.COUNTRY_ID, lbr_reg.LBR_STS,lbr_reg.LBR_KYC_NAME, lbr_reg.LBR_RATING, lbr_reg.ENTRY_BY, lbr_reg.ENTRY_DATETIME,lbr_reg.MODIFY_DATETIME,lbr_reg.GROUP_ID, lbr_reg.COST, lbr_reg.COST_PER_TIME, lbr_reg.EXPERIENCE from mvp_lbr_reg_tbl  lbr_regleft join mvp_lbr_skill_tbl lbr_skill on lbr_reg.lbr_id=lbr_skill.lbr_id where lbr_skill.category_id="+iscatgid+" ";
						locObjLbrlst_itr=pSession.createSQLQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
					}else{
						System.out.println("QWWWWW ");
						loc_slQry="select  lbr_reg.LBR_ID, lbr_reg.LBR_SERVICE_ID, lbr_reg.LBR_NAME, lbr_reg.LBR_EMAIL, lbr_reg.LBR_PH_NO, lbr_reg.LBR_ISD_CODE, lbr_reg.CARD_ID,lbr_reg.ID_CARD_NO, lbr_reg.LBR_ADD_1, lbr_reg.LBR_ADD_2, lbr_reg.VERIFIED_STATUS, lbr_reg.KEY_FOR_SEARCH, lbr_reg.IMAGE_NAME_WEB, lbr_reg.IMAGE_NAME_MOBILE, lbr_reg.WORK_TYPE_ID, lbr_reg.LBR_DESCP, lbr_reg.PSTL_ID, lbr_reg.CITY_ID, lbr_reg.STATE_ID, lbr_reg.COUNTRY_ID, lbr_reg.LBR_STS,lbr_reg.LBR_KYC_NAME, lbr_reg.LBR_RATING, lbr_reg.ENTRY_BY, lbr_reg.ENTRY_DATETIME,lbr_reg.MODIFY_DATETIME,lbr_reg.GROUP_ID, lbr_reg.COST, lbr_reg.COST_PER_TIME, lbr_reg.EXPERIENCE from mvp_lbr_reg_tbl  lbr_reg left join mvp_lbr_skill_tbl lbr_skill on lbr_reg.lbr_id=lbr_skill.lbr_id where lbr_skill.category_id="+iscatgid+" group by lbr_reg.LBR_ID ";
						System.out.println("loc_slQryloc_slQryloc_slQry::::::::::  "+loc_slQry);
						Query queryObj = pSession.createSQLQuery(loc_slQry).addScalar("lbr_reg.LBR_ID",Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_SERVICE_ID", Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_NAME", Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_EMAIL", Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_PH_NO", Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_ISD_CODE", Hibernate.TEXT)
								.addScalar("lbr_reg.CARD_ID", Hibernate.TEXT)
								.addScalar("lbr_reg.ID_CARD_NO", Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_ADD_1", Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_ADD_2", Hibernate.TEXT)
								.addScalar("lbr_reg.VERIFIED_STATUS", Hibernate.TEXT)
								.addScalar("lbr_reg.KEY_FOR_SEARCH", Hibernate.TEXT)
								.addScalar("lbr_reg.IMAGE_NAME_WEB", Hibernate.TEXT)
								.addScalar("lbr_reg.IMAGE_NAME_MOBILE", Hibernate.TEXT)
								.addScalar("lbr_reg.WORK_TYPE_ID", Hibernate.TEXT)
								.addScalar("lbr_reg.LBR_DESCP", Hibernate.TEXT)
								.addScalar("lbr_reg.PSTL_ID", Hibernate.TEXT)
								.addScalar("lbr_reg.CITY_ID", Hibernate.TEXT)
								.addScalar("lbr_reg.STATE_ID", Hibernate.TEXT)
								.addScalar("lbr_reg.COUNTRY_ID", Hibernate.TEXT)
						.addScalar("lbr_reg.LBR_STS", Hibernate.TEXT)
						.addScalar("lbr_reg.LBR_KYC_NAME", Hibernate.TEXT)
						.addScalar("lbr_reg.LBR_RATING", Hibernate.TEXT)
						.addScalar("lbr_reg.ENTRY_BY", Hibernate.TEXT)
						.addScalar("lbr_reg.ENTRY_DATETIME", Hibernate.TEXT)
						.addScalar("lbr_reg.MODIFY_DATETIME", Hibernate.TEXT)
						.addScalar("lbr_reg.GROUP_ID", Hibernate.TEXT)
						.addScalar("lbr_reg.COST", Hibernate.TEXT)
						.addScalar("lbr_reg.COST_PER_TIME", Hibernate.TEXT)
						.addScalar("lbr_reg.EXPERIENCE", Hibernate.TEXT);
					//	locObjLbrlst_itr=queryObj.setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
						nwListObj = queryObj.setFirstResult(startrowfrom).setMaxResults(totalNorow).list();


					}
				}else{
					if(isservicebookno!=null && isservicebookno.length() > 0){//service booking no in labour
						//List<Object[]> bookingObj = new ArrayList<Object[]>();
						List bookingObj;
						String sqlQuery ="select LABOUR_ID from mvp_service_booking_tbl where BOOKING_ID="+isservicebookno+"";
				System.out.println("sqlQuery::" + sqlQuery);
				Query queryObj = pSession.createSQLQuery(sqlQuery);
//				queryObj.setParameter("USERID", bookingnewObj.getUsrId());
				bookingObj = queryObj.list();
				System.out.println("bookingObj== "+bookingObj.get(0));
						skilledsearchflg +=" and ivrBnLBR_ID='"+bookingObj.get(0)+"' ";
					}
				loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS=" + locvrLBR_STS + " "+skilledsearchflg+" and ivrBnENTRY_DATETIME<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') " + locOrderby;
				locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
				}
				}
			//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_STS+"";
			System.out.println("Step 3 : Labor Details Query : "+loc_slQry);
			log.logMessage("Step 3 : Labor Details Query : "+loc_slQry, "info", LaborSkilledSearchDetailsview.class);



			locLBRJSONAryobj=new JSONArray();
			String filepath=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/labor/mobile/";
			String filepathweb=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/labor/web/";
			System.out.println("1*****");
			if(iscatgid!=null && iscatgid.length() > 0)
			{
				System.out.println("skilled search>>>>>>>>>>>>>>");
				Object[] objList;
				lvrEventjsonaryobj = new JSONArray();
				for(Iterator<Object[]> it=nwListObj.iterator();it.hasNext();) {

					objList = it.next();
					JSONObject jsonPack = new JSONObject();
					String uidPath="";
					if (objList[0] != null) {
						jsonPack.put("lbr_id",Commonutility.stringToStringempty((String)objList[0]));
					} else {
						jsonPack.put("lbr_id","");
					}
					if (objList[1] != null) {
						jsonPack.put("lbr_serviceid",Commonutility.stringToStringempty((String)objList[1]));
					} else {
						jsonPack.put("lbr_serviceid","");
					}
					if (objList[2] != null) {
						jsonPack.put("lbr_name",Commonutility.stringToStringempty((String)objList[2]));
					} else {
						jsonPack.put("lbr_name","");
					}
					if (objList[3] != null) {
						jsonPack.put("lbr_email",Commonutility.stringToStringempty((String)objList[3]));
					} else {
						jsonPack.put("lbr_email","");
					}

					if (objList[4] != null) {
						jsonPack.put("lbr_mobno",Commonutility.stringToStringempty((String)objList[4]));
					} else {
						jsonPack.put("lbr_mobno","");
					}

					if (objList[5] != null) {
						jsonPack.put("lbr_isdcode",Commonutility.stringToStringempty((String)objList[5]));
					} else {
						jsonPack.put("lbr_isdcode","");
					}
					if (objList[6] != null) {
						jsonPack.put("intv_lbr_cardtyp",Commonutility.stringToStringempty((String)objList[6]));
					} else {
						jsonPack.put("intv_lbr_cardtyp","");
					}
					if (objList[7] != null) {
						jsonPack.put("lbr_idcardno",Commonutility.stringToStringempty((String)objList[7]));
					} else {
						jsonPack.put("lbr_idcardno","");
					}
					if (objList[8] != null) {
						jsonPack.put("lbr_address1",Commonutility.stringToStringempty((String)objList[8]));
					} else {
						jsonPack.put("lbr_address1","");
					}
					if (objList[9] != null) {
						jsonPack.put("lbr_address2",Commonutility.stringToStringempty((String)objList[9]));
					} else {
						jsonPack.put("lbr_address2","");
					}
					if (objList[10] != null) {
						jsonPack.put("lbr_verifedstatus",Commonutility.stringToStringempty((String)objList[10]));
					} else {
						jsonPack.put("lbr_verifedstatus","");
					}
					if (objList[11] != null) {
						jsonPack.put("lbr_kyforsrch",Commonutility.stringToStringempty((String)objList[11]));
					} else {
						jsonPack.put("lbr_kyforsrch","");
					}
					if (objList[12] != null) {
						jsonPack.put("str_lbr_webimage",Commonutility.stringToStringempty((String)objList[12]));
					} else {
						jsonPack.put("str_lbr_webimage","");
					}
					if (objList[13] != null) {
						jsonPack.put("str_lbr_mobileimage",Commonutility.stringToStringempty((String)objList[13]));
						jsonPack.put("profilepic_thumbnail",Commonutility.toCheckNullEmpty(filepath+(String)objList[0]+"/"+(String)objList[13]));
						jsonPack.put("profilepic",Commonutility.toCheckNullEmpty(filepathweb+(String)objList[0]+"/"+(String)objList[13]));
					} else {
						jsonPack.put("str_lbr_mobileimage","");
						jsonPack.put("profilepic_thumbnail","");
						jsonPack.put("profilepic","");
					}


					if (objList[14] != null) {
						jsonPack.put("lbr_wrktypid",Commonutility.stringToStringempty((String)objList[14]));
					} else {
						jsonPack.put("lbr_wrktypid","");
					}
					if (objList[15] != null) {
						jsonPack.put("lbr_desc",Commonutility.stringToStringempty((String)objList[15]));
					} else {
						jsonPack.put("lbr_desc","");
					}
					if (objList[16] != null) {
						jsonPack.put("lbr_pincodeid",Commonutility.stringToStringempty((String)objList[16]));
					} else {
						jsonPack.put("lbr_pincodeid","");
					}
					if (objList[17] != null) {
						jsonPack.put("lbr_cityid",Commonutility.stringToStringempty((String)objList[17]));
					} else {
						jsonPack.put("lbr_cityid","");
					}
					if (objList[18] != null) {
						jsonPack.put("lbr_stateid",Commonutility.stringToStringempty((String)objList[18]));
					} else {
						jsonPack.put("lbr_stateid","");
					}
					if (objList[19] != null) {
						jsonPack.put("lbr_cntryid",Commonutility.stringToStringempty((String)objList[19]));
					} else {
						jsonPack.put("lbr_cntryid","");
					}

					/*if (objList[20] != null) {
						jsonPack.put("lbr_id",Commonutility.stringToStringempty((String)objList[20]));
					} else {
						jsonPack.put("lbr_id","");
					}
					if (objList[21] != null) {
						jsonPack.put("lbr_serviceid",Commonutility.stringToStringempty((String)objList[21]));
					} else {
						jsonPack.put("lbr_serviceid","");
					}*/
					if (objList[22] != null) {
						jsonPack.put("lbr_rating",Commonutility.stringToStringempty((String)objList[22]));
					} else {
						jsonPack.put("lbr_rating","");
					}
					/*if (objList[23] != null) {
						jsonPack.put("lbr_email",Commonutility.stringToStringempty((String)objList[23]));
					} else {
						jsonPack.put("lbr_email","");
					}

					if (objList[24] != null) {
						jsonPack.put("lbr_mobno",Commonutility.stringToStringempty((String)objList[24]));
					} else {
						jsonPack.put("lbr_mobno","");
					}

					if (objList[25] != null) {
						jsonPack.put("lbr_isdcode",Commonutility.stringToStringempty((String)objList[25]));
					} else {
						jsonPack.put("lbr_isdcode","");
					}
					if (objList[26] != null) {
						jsonPack.put("intv_lbr_cardtyp",Commonutility.stringToStringempty((String)objList[26]));
					} else {
						jsonPack.put("intv_lbr_cardtyp","");
					}
					*/
					if (objList[27] != null) {
						jsonPack.put("lbr_costper",Commonutility.stringToStringempty((String)objList[27]));
					} else {
						jsonPack.put("lbr_costper","");
					}
					if (objList[28] != null) {
						jsonPack.put("lbr_ratecard",Commonutility.stringToStringempty((String)objList[28]));
					} else {
						jsonPack.put("lbr_ratecard","");
					}
					if (objList[29] != null) {
						jsonPack.put("lbr_experience",Commonutility.stringToStringempty((String)objList[29]));
					} else {
						jsonPack.put("lbr_experience","");
					}
					String loc_slQry_categry1="from LaborSkillTblVO where ivrBnLBR_ID="+objList[0]+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"  order by ivrBnENTRY_DATETIME desc 1";
					Iterator locObjLbrcateglst_itr1=pSession.createQuery(loc_slQry_categry1).list().iterator();
					System.out.println("Step 3 : Select labor category detail query executed.....");
					locLBRSklJSONAryobj1=new JSONArray();
					if(locObjLbrcateglst_itr1!=null && locObjLbrcateglst_itr1.hasNext())
					{
						locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr1.next();
						locInrLbrSklJSONObj1=new JSONObject();
						if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){

							locInrLbrSklJSONObj1.put("skilled_categoryname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
							locInrLbrSklJSONObj1.put("skilled_categoryid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));

						}
						else
						{
							locInrLbrSklJSONObj1.put("skilled_categoryname", "");
							locInrLbrSklJSONObj1.put("skilled_categoryid", "");

						}
						if(locLbrSkiltbl.getIvrBnSKILL_ID()!=null)
						{
							locInrLbrSklJSONObj1.put("skilled_skillsid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_ID()));
							locInrLbrSklJSONObj1.put("skilled_skillsname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_NAME()));
						}
						else
						{
							locInrLbrSklJSONObj1.put("skilled_skillsid", "");
							locInrLbrSklJSONObj1.put("skilled_skillsname", "");
						}
						locLBRSklJSONAryobj1.put(locInrLbrSklJSONObj1);
					}

					jsonPack.put("jArry_lbr_skilledhelp", locLBRSklJSONAryobj1);
					loc_slQry_categry="from LaborSkillTblVO where ivrBnLBR_ID="+objList[0]+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"";
					locObjLbrcateglst_itr=pSession.createQuery(loc_slQry_categry).list().iterator();
					System.out.println("Step 4: Select labor category detail query executed.");

					locLBRSklJSONAryobj=new JSONArray();
					while (locObjLbrcateglst_itr!=null &&  locObjLbrcateglst_itr.hasNext() ) {
						locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr.next();
						locInrLbrSklJSONObj=new JSONObject();
						if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){

							locInrLbrSklJSONObj.put("categoryname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
							locInrLbrSklJSONObj.put("categoryid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));

						}
						else
						{
							locInrLbrSklJSONObj.put("categoryname", "");
							locInrLbrSklJSONObj.put("categoryid", "");

						}
						if(locLbrSkiltbl.getIvrBnSKILL_ID()!=null)
						{
							locInrLbrSklJSONObj.put("skillsid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_ID()));
							locInrLbrSklJSONObj.put("skillsname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_NAME()));
						}
						else
						{
							locInrLbrSklJSONObj.put("skillsid", "");
							locInrLbrSklJSONObj.put("skillsname", "");
						}
						locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
					}
					//log.logMessage("Step 3: Select Labor categoey detail block end.", "info", LaborDetailsview.class);
					//System.out.println("Step 4 : Select labor category detail are formed into JSONObject - json array - Filan JSON.");
					jsonPack.put("jArry_lbr_catg", locLBRSklJSONAryobj);
					System.out.println("Step 4: Select category detail query executed.");
					locLBRJSONAryobj.put(jsonPack);
				}
			}
			else{
				System.out.println("list search>>>>>>>>>>>>>>>");
			while ( locObjLbrlst_itr.hasNext() ) {

				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (LaborDetailsTblVO) locObjLbrlst_itr.next();
				//System.out.println("------------------------Start-------------------------");
				//System.out.println("Name : "+lbrDtlVoObj.getIvrBnLBR_NAME());
				//System.out.println("Email : "+lbrDtlVoObj.getIvrBnLBR_EMAIL());
				locInrJSONObj.put("lbr_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ID()));
				System.out.println("lbr_id"+lbrDtlVoObj.getIvrBnLBR_ID());
				locInrJSONObj.put("lbr_grpid",String.valueOf(lbrDtlVoObj.getIvrBnGRP_CODE()));
				locInrJSONObj.put("lbr_serviceid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_SERVICE_ID()));
				locInrJSONObj.put("lbr_name",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_NAME()));
				locInrJSONObj.put("lbr_email",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_EMAIL()));
				locInrJSONObj.put("lbr_isdcode",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ISD_CODE()));
				locInrJSONObj.put("lbr_mobno",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_PH_NO()));

				locInrJSONObj.put("lbr_verifedstatus",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_VERIFIED_STATUS()));// 0 - unverified, 1- verified
				locInrJSONObj.put("lbr_kyforsrch",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_KEY_FOR_SEARCH()));

				locInrJSONObj.put("lbr_desc",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_DESCP()));
				locInrJSONObj.put("lbr_address1",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ADD_1()));
				locInrJSONObj.put("lbr_address2",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ADD_2()));
				locInrJSONObj.put("lbr_experience",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_EXPERIENCE()));
				locInrJSONObj.put("lbr_costper",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_COSTPER()));
				locInrJSONObj.put("lbr_ratecard",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_COST()));
				locInrJSONObj.put("lbr_idcardno", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnID_CARD_NO()));
				if(lbrDtlVoObj.getIvrBnIdrcardObj()!=null){
					locInrJSONObj.put("intv_lbr_cardtyp", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIdrcardObj().getiVOcradid()));
					locInrJSONObj.put("intv_lbr_cardtyp_name", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIdrcardObj().getiVOcradname()));
				}else{
					locInrJSONObj.put("intv_lbr_cardtyp", "");
					locInrJSONObj.put("intv_lbr_cardtyp_name", "");
				}
				if(lbrDtlVoObj.getPstlId()!=null){
					//System.out.println("Postal Code : "+lbrDtlVoObj.getPstlId().getPstlCode()));
					//System.out.println("City : "+lbrDtlVoObj.getPstlId().getCityId().getCityName()));
					//System.out.println("State : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName()));
					//System.out.println("Country : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName()));
//					locInrJSONObj.put("lbr_pincodeid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getPstlId()));
//					locInrJSONObj.put("lbr_pincodeName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getPstlCode()));
//					locInrJSONObj.put("lbr_cityid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getCityId()));
//					locInrJSONObj.put("lbr_cityName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getCityName()));
//					locInrJSONObj.put("lbr_stateid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateId()));
//					locInrJSONObj.put("lbr_stateName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName()));
//					locInrJSONObj.put("lbr_cntryid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//					locInrJSONObj.put("lbr_cntryName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName()));
					
					locInrJSONObj.put("lbr_pincodeid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId()));
					locInrJSONObj.put("lbr_pincodeName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId()));
					
					if(lbrDtlVoObj.getCityId() !=null){
					locInrJSONObj.put("lbr_cityid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getCityId()));
					locInrJSONObj.put("lbr_cityName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getCityName()));
					
						if(lbrDtlVoObj.getCityId().getStateId() !=null){
						locInrJSONObj.put("lbr_stateid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getStateId()));
						locInrJSONObj.put("lbr_stateName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getStateName()));
						
							if(lbrDtlVoObj.getCityId().getStateId().getCountryId() !=null){
							locInrJSONObj.put("lbr_cntryid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryId()));
							locInrJSONObj.put("lbr_cntryName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryName()));
							}
							else{
								locInrJSONObj.put("lbr_cntryid","");
								locInrJSONObj.put("lbr_cntryName","");
							}
						}
						else{
							locInrJSONObj.put("lbr_stateid","");
							locInrJSONObj.put("lbr_stateName","");
							locInrJSONObj.put("lbr_cntryid","");
							locInrJSONObj.put("lbr_cntryName","");
						}
					}
					else{
						locInrJSONObj.put("lbr_cityid","");
						locInrJSONObj.put("lbr_cityName","");
						locInrJSONObj.put("lbr_stateid","");
						locInrJSONObj.put("lbr_stateName","");
						locInrJSONObj.put("lbr_cntryid","");
						locInrJSONObj.put("lbr_cntryName","");
					}
				}else{
					locInrJSONObj.put("lbr_pincodeid","");
					locInrJSONObj.put("lbr_pincodeName","");
					locInrJSONObj.put("lbr_cityid","");
					locInrJSONObj.put("lbr_cityName","");
					locInrJSONObj.put("lbr_stateid","");
					locInrJSONObj.put("lbr_stateName","");
					locInrJSONObj.put("lbr_cntryid","");
					locInrJSONObj.put("lbr_cntryName","");
				}

				if(lbrDtlVoObj.getWrktypId()!=null){
					locInrJSONObj.put("lbr_wrktypid",String.valueOf(lbrDtlVoObj.getWrktypId().getWrktypId()));
					locInrJSONObj.put("lbr_wrktypName",lbrDtlVoObj.getWrktypId().getIVOlbrWORK_TYPE());
				}else{
					locInrJSONObj.put("lbr_wrktypid","");
					locInrJSONObj.put("lbr_wrktypName","");
				}
				if(lbrDtlVoObj.getIvrBnLBR_RATING()!=null){
					double lvrtem = (Double) lbrDtlVoObj.getIvrBnLBR_RATING();
					locInrJSONObj.put("lbr_rating",  Commonutility.toCheckNullEmpty(lvrtem));// 0 - , 1 - , 2 - , 3 - , 4 -
				} else {
					double lvrtem = 0.0;
					locInrJSONObj.put("lbr_rating", Commonutility.toCheckNullEmpty(lvrtem));// 0 - , 1 - , 2 - , 3 - , 4 -
				}

				locInrJSONObj.put("str_lbr_webimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIMAGE_NAME_WEB()));
				locInrJSONObj.put("str_lbr_mobileimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIMAGE_NAME_MOBILE()));
				if(lbrDtlVoObj.getIvrBnIMAGE_NAME_MOBILE()!=null){
					locInrJSONObj.put("profilepic_thumbnail",Commonutility.toCheckNullEmpty(filepath+lbrDtlVoObj.getIvrBnLBR_ID()+"/"+lbrDtlVoObj.getIvrBnIMAGE_NAME_MOBILE()));}
					else
					{
						locInrJSONObj.put("profilepic_thumbnail","");
					}
				if(lbrDtlVoObj.getIvrBnIMAGE_NAME_MOBILE()!=null){
					locInrJSONObj.put("profilepic",Commonutility.toCheckNullEmpty(filepathweb+lbrDtlVoObj.getIvrBnLBR_ID()+"/"+lbrDtlVoObj.getIvrBnIMAGE_NAME_MOBILE()));}
					else
					{
						locInrJSONObj.put("profilepic","");
					}

				String loc_slQry_categry1="from LaborSkillTblVO where ivrBnLBR_ID="+lbrDtlVoObj.getIvrBnLBR_ID()+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"  order by ivrBnENTRY_DATETIME desc 1";
				Iterator locObjLbrcateglst_itr1=pSession.createQuery(loc_slQry_categry1).list().iterator();
				System.out.println("Step 3 : Select labor category detail query executed.....");
				locLBRSklJSONAryobj1=new JSONArray();
				if(locObjLbrcateglst_itr1!=null && locObjLbrcateglst_itr1.hasNext())
				{
					locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr1.next();
					locInrLbrSklJSONObj1=new JSONObject();
					if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){

						locInrLbrSklJSONObj1.put("skilled_categoryname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
						locInrLbrSklJSONObj1.put("skilled_categoryid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));

					}
					else
					{
						locInrLbrSklJSONObj1.put("skilled_categoryname", "");
						locInrLbrSklJSONObj1.put("skilled_categoryid", "");

					}
					if(locLbrSkiltbl.getIvrBnSKILL_ID()!=null)
					{
						locInrLbrSklJSONObj1.put("skilled_skillsid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_ID()));
						locInrLbrSklJSONObj1.put("skilled_skillsname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_NAME()));
					}
					else
					{
						locInrLbrSklJSONObj1.put("skilled_skillsid", "");
						locInrLbrSklJSONObj1.put("skilled_skillsname", "");
					}
					locLBRSklJSONAryobj1.put(locInrLbrSklJSONObj1);
				}

				locInrJSONObj.put("jArry_lbr_skilledhelp", locLBRSklJSONAryobj1);


				loc_slQry_categry="from LaborSkillTblVO where ivrBnLBR_ID="+lbrDtlVoObj.getIvrBnLBR_ID()+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"";
				locObjLbrcateglst_itr=pSession.createQuery(loc_slQry_categry).list().iterator();
				System.out.println("Step 3 : Select labor category detail query executed.");

				locLBRSklJSONAryobj=new JSONArray();
				while (locObjLbrcateglst_itr!=null &&  locObjLbrcateglst_itr.hasNext() ) {
					locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr.next();
					locInrLbrSklJSONObj=new JSONObject();
					if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){

						locInrLbrSklJSONObj.put("categoryname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
						locInrLbrSklJSONObj.put("categoryid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));

					}
					else
					{
						locInrLbrSklJSONObj.put("categoryname", "");
						locInrLbrSklJSONObj.put("categoryid", "");

					}
					if(locLbrSkiltbl.getIvrBnSKILL_ID()!=null)
					{
						locInrLbrSklJSONObj.put("skillsid", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_ID()));
						locInrLbrSklJSONObj.put("skillsname", Commonutility.toCheckNullEmpty(locLbrSkiltbl.getIvrBnSKILL_ID().getIvrBnSKILL_NAME()));
					}
					else
					{
						locInrLbrSklJSONObj.put("skillsid", "");
						locInrLbrSklJSONObj.put("skillsname", "");
					}
					locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
				}
				//log.logMessage("Step 3: Select Labor categoey detail block end.", "info", LaborDetailsview.class);
				//System.out.println("Step 4 : Select labor category detail are formed into JSONObject - json array - Filan JSON.");
				locInrJSONObj.put("jArry_lbr_catg", locLBRSklJSONAryobj);

				loc_slQry_Rating="from FeedbackTblVO where ivrBnFEEDBK_FOR_USR_ID="+lbrDtlVoObj.getIvrBnLBR_ID()+" and ivrBnFEEDBK_STATUS = 1";
				locObjLbrratinglst_itr=pSession.createQuery(loc_slQry_Rating).list().iterator();
				System.out.println("Step 3 : Select labor feedback category detail query executed.");

				locLBRRATTINGJSONAryobj=new JSONArray();
				int i=0,j=0,k=0,m=0,n=0;
				boolean rattingflg =false;
				System.out.println("rating*************** "+locObjLbrratinglst_itr.hasNext());
				if(locObjLbrratinglst_itr.hasNext()==true){
					while (locObjLbrratinglst_itr!=null &&  locObjLbrratinglst_itr.hasNext()  ) {
						locLbrrattingtbl=(FeedbackTblVO)locObjLbrratinglst_itr.next();
						locInrLbrRattingJSONObj=new JSONObject();
						if(locLbrrattingtbl.getIvrBnFEEDBK_FOR_USR_ID()!=null){
							if(locLbrrattingtbl.getIvrBnRATING()==1)
							{
								i++;

							}else if(locLbrrattingtbl.getIvrBnRATING()==2){
								j++;

							}else if(locLbrrattingtbl.getIvrBnRATING()==3){
								k++;

							}else if(locLbrrattingtbl.getIvrBnRATING()==4){
								m++;

							}else if(locLbrrattingtbl.getIvrBnRATING()==5){
								n++;

							}
							else{
								i=0;j=0;k=0;m=0;n=0;
							}

						}
						System.out.println("I,J,K,M,N  "+i+","+j+","+k+","+m+","+n);
						locInrLbrRattingJSONObj.put("ratingchart",  ""+i+","+j+","+k+","+m+","+n+"");

					}
					}else{
						rattingflg =true;
						locInrLbrRattingJSONObj=new JSONObject();
						i=0;j=0;k=0;m=0;n=0;
						locInrLbrRattingJSONObj.put("ratingchart",  ""+i+","+j+","+k+","+m+","+n+"");
						locLBRRATTINGJSONAryobj.put(locInrLbrRattingJSONObj);
						System.out.println("locInrLbrRattingJSONObj   "+locInrLbrRattingJSONObj);
					}

					if(rattingflg!=true ){
					locLBRRATTINGJSONAryobj.put(locInrLbrRattingJSONObj);}
					else{
						/*locInrLbrRattingJSONObj=new JSONObject();
						locInrLbrRattingJSONObj.put("ratingchart",  ""+i+","+j+","+k+","+m+","+n+"");
						locLBRRATTINGJSONAryobj.put(locInrLbrRattingJSONObj);*/
						}
					locInrJSONObj.put("jArry_lbr_ratting", locLBRRATTINGJSONAryobj);
				//Report issues in labour

				GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
				String pGrpName = rb.getString("Grp.labor");
				Integer lvrMrcnhtgrpid = null;
				 // Select Group Code on Merchant
				String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
				Query locQryrst=pSession.createQuery(locSlqry);
				lvrGrpmstobj = (GroupMasterTblVo) locQryrst.uniqueResult();
				 if(lvrGrpmstobj!=null){
					 lvrMrcnhtgrpid = lvrGrpmstobj.getGroupCode();
				 }else {
					 lvrMrcnhtgrpid = null;
				 }



				Iterator lvrObjfunctionlstitr = null;

				//String lvrfunSlqry = "from MerchantIssueTblVO where mcrctLaborId="+lbrDtlVoObj.getIvrBnLBR_ID()+" and status=1 and ivrGrpcodeobj = "+lvrMrcnhtgrpid;
				String lvrfunSlqry = "from IssueTblVO where status=1 ";
				System.out.println("lvrfunSlqry -------- "+lvrfunSlqry);
				lvrObjfunctionlstitr=pSession.createQuery(lvrfunSlqry).list().iterator();
				locLBRISSREPJSONAryobj=new JSONArray();
				while(lvrObjfunctionlstitr!=null && lvrObjfunctionlstitr.hasNext())
				{
					locLbrIssReptbl=(IssueTblVO)lvrObjfunctionlstitr.next();
					locInrLbrIRepJSONObj1=new JSONObject();
					if(locLbrIssReptbl.getIssueId()!=null){

						locInrLbrIRepJSONObj1.put("id", Commonutility.toCheckNullEmpty(locLbrIssReptbl.getIssueId()));
						locInrLbrIRepJSONObj1.put("name", Commonutility.toCheckNullEmpty(locLbrIssReptbl.getIssueList()));

					}
					else
					{
						locInrLbrIRepJSONObj1.put("id", "");
						locInrLbrIRepJSONObj1.put("name", "");

					}

					locLBRISSREPJSONAryobj.put(locInrLbrIRepJSONObj1);
				}
				locInrJSONObj.put("issue", locLBRISSREPJSONAryobj);
				//locInrJSONObj.put("jArry_lbr_skilledhelp", locLBRSklJSONAryobj1);
				log.logMessage("Step 3: Select labor feedback categoey detail block end.", "info", LaborSkilledSearchDetailsview.class);
				System.out.println("Step 4 : Select labor feedback category detail are formed into JSONObject - json array - Filan JSON.");
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null;
			}//while end
		}
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("timestamp", Commonutility.toCheckNullEmpty(timestamp));
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("labordetails", locLBRJSONAryobj);

			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 4 : Select labor detail block end.");
			log.logMessage("Step 4: Select Labor detail block end.", "info", LaborSkilledSearchDetailsview.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toLBRSelectAll() : "+e);
			log.logMessage("Step -1 : labor select all block Exception : "+e, "error", LaborSkilledSearchDetailsview.class);
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("labordetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;locObjLbrratinglst_itr=null;locLbrrattingtbl=null;locLBRRATTINGJSONAryobj=null; locInrLbrRattingJSONObj=null; lvrEventjsonaryobj = null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;locLBRISSREPJSONAryobj=null;
			locObjLbrcateglst_itr=null;locLbrSkiltbl=null;locInrLbrSklJSONObj=null;locLBRSklJSONAryobj=null;loc_slQry_categry=null;locInrLbrIRepJSONObj1=null;

		}
	}

	public static JSONObject toLBRSelectAllCmplt(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;

		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry=null, locsrchdtblsrch=null,loc_slQry_Rating=null;
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		LaborDetailsTblVO lbrDtlVoObj=null;
		String locvrLBR_ID=null,locvrLBR_SERVICE_ID=null, locvrLBR_name=null, locvrLBR_Email=null;
		int count=0,countFilter=0;

		String loc_slQry_categry=null;
		Iterator locObjLbrcateglst_itr=null,locObjLbrratinglst_itr=null;
		LaborSkillTblVO locLbrSkiltbl=null;
		FeedbackTblVO locLbrrattingtbl=null;
		JSONArray locLBRSklJSONAryobj=null,locLBRRATTINGJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null;
		JSONObject locInrLbrRattingJSONObj=null;
		try {
		log=new Log();	lbrDtlVoObj=new LaborDetailsTblVO();
		System.out.println("Step 1 : labor feedback select all block enter");
		log.logMessage("Step 1 : labor feedback select all block enter", "info", LaborSkilledSearchDetailsview.class);
		locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrstatus");
		locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
		locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");

		locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
		if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web
			locvrLBR_cntQry="select count(ivrBnLBR_ID) from LaborDetailsTblVO where ivrBnLBR_STS="+locvrLBR_STS+"";
			System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry);
			log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry, "info", LaborSkilledSearchDetailsview.class);

			LaborDaoservice lbrDaoObj=new LaborDaoservice();
			count=lbrDaoObj.getInitTotal(locvrLBR_cntQry);
			countFilter=lbrDaoObj.getTotalFilter(locvrLBR_cntQry);
		}else{ // for mobile
			 count=0;countFilter=0;
			 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
			 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LaborSkilledSearchDetailsview.class);
		}


			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			String globalsearch=null;
			String locOrderby =" order by ivrBnENTRY_DATETIME desc";
		if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
			  globalsearch = " AND (" + "ivrBnLBR_NAME like ('%" + loctocheNull+ "%') or "
		                                + "ivrBnLBR_PH_NO like ('%" + loctocheNull + "%')  or "
		                                + "ivrBnLBR_EMAIL like ('%" + loctocheNull + "%') or "
		                               // + "ivrBNLbrSkilObj.iVOCATEGORY_ID.iVOCATEGORY_NAME like ('%" + loctocheNull + "%') or "
		                                + "wrktypId.IVOlbrWORK_TYPE like ('%" + loctocheNull + "%'))";
			  loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS=" + locvrLBR_STS + " " + globalsearch + " " + locOrderby;
		}else{
			loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS=" + locvrLBR_STS + " " + locOrderby;
		}
		//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_STS+"";
		System.out.println("Step 3 : labor feedback Details Query : "+loc_slQry);
		log.logMessage("Step 3 : labor feedback Details Query : "+loc_slQry, "info", LaborSkilledSearchDetailsview.class);
		locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(0).setMaxResults(count).list().iterator();


		locLBRJSONAryobj=new JSONArray();
		while ( locObjLbrlst_itr.hasNext() ) {
			locInrJSONObj=new JSONObject();
			lbrDtlVoObj = (LaborDetailsTblVO) locObjLbrlst_itr.next();
			//System.out.println("------------------------Start-------------------------");
			//System.out.println("Name : "+lbrDtlVoObj.getIvrBnLBR_NAME());
			//System.out.println("Email : "+lbrDtlVoObj.getIvrBnLBR_EMAIL());

			locInrJSONObj.put("lbr_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ID()));
			locInrJSONObj.put("lbr_serviceid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_SERVICE_ID()));
			locInrJSONObj.put("lbr_name",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_NAME()));
			locInrJSONObj.put("lbr_email",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_EMAIL()));
			locInrJSONObj.put("lbr_isdcode",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ISD_CODE()));
			locInrJSONObj.put("lbr_mobno",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_PH_NO()));

			locInrJSONObj.put("lbr_verifedstatus",Commonutility.toCheckNullEmpty(String.valueOf(lbrDtlVoObj.getIvrBnLBR_VERIFIED_STATUS())));// 0 - unverified, 1- verified
			locInrJSONObj.put("lbr_kyforsrch",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_KEY_FOR_SEARCH()));

			locInrJSONObj.put("lbr_desc",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_DESCP()));
			locInrJSONObj.put("lbr_address1",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ADD_1()));
			locInrJSONObj.put("lbr_address2",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_ADD_2()));

			if(lbrDtlVoObj.getPstlId()!=null){
				//System.out.println("Postal Code : "+lbrDtlVoObj.getPstlId().getPstlCode());
				//System.out.println("City : "+lbrDtlVoObj.getPstlId().getCityId().getCityName());
				//System.out.println("State : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName());
				//System.out.println("Country : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());
//				locInrJSONObj.put("lbr_pincodeid",Commonutility.toCheckNullEmpty(String.valueOf(lbrDtlVoObj.getPstlId().getPstlId())));
//				locInrJSONObj.put("lbr_pincodeName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getPstlCode()));
//				locInrJSONObj.put("lbr_cityid",Commonutility.toCheckNullEmpty(String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getCityId())));
//				locInrJSONObj.put("lbr_cityName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getCityName()));
//				locInrJSONObj.put("lbr_stateid",Commonutility.toCheckNullEmpty(String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateId())));
//				locInrJSONObj.put("lbr_stateName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName()));
//				locInrJSONObj.put("lbr_cntryid",Commonutility.toCheckNullEmpty(String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId())));
//				locInrJSONObj.put("lbr_cntryName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName()));
				
				locInrJSONObj.put("lbr_pincodeid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId()));
				locInrJSONObj.put("lbr_pincodeName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getPstlId()));
				
			}else{
				locInrJSONObj.put("lbr_pincodeid","");
				locInrJSONObj.put("lbr_pincodeName","");
//				locInrJSONObj.put("lbr_cityid","");
//				locInrJSONObj.put("lbr_cityName","");
//				locInrJSONObj.put("lbr_stateid","");
//				locInrJSONObj.put("lbr_stateName","");
//				locInrJSONObj.put("lbr_cntryid","");
//				locInrJSONObj.put("lbr_cntryName","");
			}
			
			if(lbrDtlVoObj.getCityId() !=null){
				locInrJSONObj.put("lbr_cityid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getCityId()));
				locInrJSONObj.put("lbr_cityName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getCityName()));
				
					if(lbrDtlVoObj.getCityId().getStateId() !=null){
					locInrJSONObj.put("lbr_stateid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getStateId()));
					locInrJSONObj.put("lbr_stateName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getStateName()));
					
						if(lbrDtlVoObj.getCityId().getStateId().getCountryId() !=null){
						locInrJSONObj.put("lbr_cntryid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryId()));
						locInrJSONObj.put("lbr_cntryName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryName()));
						}
						else{
							locInrJSONObj.put("lbr_cntryid","");
							locInrJSONObj.put("lbr_cntryName","");
						}
					}
					else{
						locInrJSONObj.put("lbr_stateid","");
						locInrJSONObj.put("lbr_stateName","");
						locInrJSONObj.put("lbr_cntryid","");
						locInrJSONObj.put("lbr_cntryName","");
					}
				}
				else{
					locInrJSONObj.put("lbr_cityid","");
					locInrJSONObj.put("lbr_cityName","");
					locInrJSONObj.put("lbr_stateid","");
					locInrJSONObj.put("lbr_stateName","");
					locInrJSONObj.put("lbr_cntryid","");
					locInrJSONObj.put("lbr_cntryName","");
				}

			if(lbrDtlVoObj.getWrktypId()!=null){
				locInrJSONObj.put("lbr_wrktypid",Commonutility.toCheckNullEmpty(String.valueOf(lbrDtlVoObj.getWrktypId().getWrktypId())));
				locInrJSONObj.put("lbr_wrktypName",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getWrktypId().getIVOlbrWORK_TYPE()));
			}else{
				locInrJSONObj.put("lbr_wrktypid","");
				locInrJSONObj.put("lbr_wrktypName","");
			}
			locInrJSONObj.put("lbr_rating",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnLBR_RATING()));// 0 - , 1 - , 2 - , 3 - , 4 -
			locInrJSONObj.put("str_lbr_webimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIMAGE_NAME_WEB()));
			locInrJSONObj.put("str_lbr_mobileimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIMAGE_NAME_MOBILE()));

			loc_slQry_categry="from LaborSkillTblVO where ivrBnLBR_ID="+lbrDtlVoObj.getIvrBnLBR_ID()+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"";

			locObjLbrcateglst_itr=pSession.createQuery(loc_slQry_categry).list().iterator();
			System.out.println("Step 3 : Select labor feedback category detail query executed.");

			locLBRSklJSONAryobj=new JSONArray();
			while (locObjLbrcateglst_itr!=null &&  locObjLbrcateglst_itr.hasNext() ) {
				locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr.next();
				locInrLbrSklJSONObj=new JSONObject();
				if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){
					locInrLbrSklJSONObj.put("categoryid",  Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));
					locInrLbrSklJSONObj.put("categoryname",  Commonutility.toCheckNullEmpty(locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
				}
				locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
			}

			locInrJSONObj.put("jArry_lbr_catg", locLBRSklJSONAryobj);
			loc_slQry_Rating="from FeedbackTblVO where ivrBnFEEDBK_FOR_USR_ID="+lbrDtlVoObj.getIvrBnLBR_ID()+" and ivrBnFEEDBK_STATUS = 1";
			locObjLbrratinglst_itr=pSession.createQuery(loc_slQry_Rating).list().iterator();
			System.out.println("Step 3 : Select labor feedback category detail query executed.");

			locLBRRATTINGJSONAryobj=new JSONArray();
			int i=0,j=0,k=0,m=0,n=0;
			while (locObjLbrratinglst_itr!=null &&  locObjLbrratinglst_itr.hasNext() ) {
				locLbrrattingtbl=(FeedbackTblVO)locObjLbrratinglst_itr.next();
				locInrLbrRattingJSONObj=new JSONObject();
				if(locLbrrattingtbl.getIvrBnFEEDBK_FOR_USR_ID()!=null){
					if(locLbrrattingtbl.getIvrBnRATING()==1)
					{
						i=i+1;
						locInrLbrRattingJSONObj.put("rating1",  Commonutility.toCheckNullEmpty(i));
					}else if(locLbrrattingtbl.getIvrBnRATING()==2){
						j=j+1;
						locInrLbrRattingJSONObj.put("rating2",  Commonutility.toCheckNullEmpty(j));
					}else if(locLbrrattingtbl.getIvrBnRATING()==3){
						k=k+1;
						locInrLbrRattingJSONObj.put("rating3",  Commonutility.toCheckNullEmpty(k));
					}else if(locLbrrattingtbl.getIvrBnRATING()==4){
						m=m+1;
						locInrLbrRattingJSONObj.put("rating4",  Commonutility.toCheckNullEmpty(m));
					}else if(locLbrrattingtbl.getIvrBnRATING()==5){
						n=n+1;
						locInrLbrRattingJSONObj.put("rating5",  Commonutility.toCheckNullEmpty(n));
					}

				}
				locLBRRATTINGJSONAryobj.put(locInrLbrRattingJSONObj);
			}
			log.logMessage("Step 3: Select labor feedback categoey detail block end.", "info", LaborSkilledSearchDetailsview.class);
			System.out.println("Step 4 : Select labor feedback category detail are formed into JSONObject - json array - Filan JSON.");
			locInrJSONObj.put("jArry_lbr_ratting", locLBRRATTINGJSONAryobj);


			locLBRJSONAryobj.put(locInrJSONObj);
			locInrJSONObj=null;
		}
		System.out.println("Step 5 : Return JSON Array DATA : "+locLBRJSONAryobj);

		locFinalRTNObj=new JSONObject();
		locFinalRTNObj.put("InitCount", count);
		locFinalRTNObj.put("countAfterFilter", countFilter);
		locFinalRTNObj.put("rowstart", "");
		locFinalRTNObj.put("totalnorow", "");
		locFinalRTNObj.put("labordetails", locLBRJSONAryobj);

		System.out.println("locFinalRTNObj : "+locFinalRTNObj);
		System.out.println("Step 6 : Select labor detail block end.");
		log.logMessage("Step 4: Select Labor detail block end.", "info", LaborSkilledSearchDetailsview.class);
		return locFinalRTNObj;
	} catch (Exception e) {
		try{
		System.out.println("Exception in toLBRSelectAll() : "+e);
		log.logMessage("Step -1 : labor select all block Exception : "+e, "error", LaborSkilledSearchDetailsview.class);
		locFinalRTNObj=new JSONObject();
		locFinalRTNObj.put("InitCount", count);
		locFinalRTNObj.put("countAfterFilter", countFilter);
		locFinalRTNObj.put("labordetails", "");
		locFinalRTNObj.put("CatchBlock", "Error");
		System.out.println("locFinalRTNObj : "+locFinalRTNObj);
		}catch(Exception ee){}finally{}
		return locFinalRTNObj;
	} finally {

		lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
		locObjLbrcateglst_itr=null;locObjLbrratinglst_itr=null;locLbrSkiltbl=null;locLbrrattingtbl=null;locInrLbrSklJSONObj=null;locLBRSklJSONAryobj=null;loc_slQry_categry=null;

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
