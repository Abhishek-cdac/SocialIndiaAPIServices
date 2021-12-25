package com.socialindiaservices.reports;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.laborvo.LaborDetailsTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class LoadLabourReportTableData  extends ActionSupport {

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
		try{
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");														
					ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
					if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
						ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
					}else{
						ivrCurrntusrid=0;
					}
					if(ivrservicecode.equalsIgnoreCase("SI3005")) {
						locObjRspdataJson=toLBRSelectAll(locObjRecvdataJson,locObjsession);	
					} else {
						locObjRspdataJson = toLBRSelectAllCmplt(
								locObjRecvdataJson, locObjsession);
					}
					String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if (errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("LBAD014"), "LBAD014", ivrCurrntusrid);
						serverResponse("SI3005","0","E3005",getText("labor.selectall.error"),locObjRspdataJson);
					} else {
						//AuditTrial.toWriteAudit(getText("LBAD013"), "LBAD013", ivrCurrntusrid);
						serverResponse("SI3005","0","S3005",getText("labor.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI3005, "+getText("request.format.notmach"), "info", LoadLabourReportTableData.class);
					serverResponse("SI3005","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3005, "+getText("request.values.empty"), "info", LoadLabourReportTableData.class);
				serverResponse("SI3005","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			AuditTrial.toWriteAudit(getText("LBAD014"), "LBAD014", ivrCurrntusrid);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3005, "+getText("catch.error"), "error", LoadLabourReportTableData.class);
			serverResponse("SI3005","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject toLBRSelectAll(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		LaborDetailsTblVO lbrDtlVoObj=null;
		String locvrLBR_ID=null,locvrLBR_SERVICE_ID=null, locvrLBR_name=null, locvrLBR_Email=null,fromdate=null,todate=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;
		
		String loc_slQry_categry=null;		
		Iterator locObjLbrcateglst_itr=null;
		LaborSkillTblVO locLbrSkiltbl=null;
		JSONArray locLBRSklJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null;
		
		try {
			log=new Log();	lbrDtlVoObj=new LaborDetailsTblVO();			
			System.out.println("Step 1 : labor select all block enter");
			log.logMessage("Step 1 : labor select all block enter", "info", LoadLabourReportTableData.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			fromdate=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fromdate");
			todate=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "todate");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			
			String globalsearch=null;
			String locOrderby =" order by modifyDatetime desc";
			String manualsearch="";
			if (fromdate.length() >0 && todate.length() >0){
					manualsearch += " and date(ivrBnENTRY_DATETIME) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
			} else if (fromdate.length() > 0) {
					manualsearch += " and date(ivrBnENTRY_DATETIME) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
			} else if (todate.length() > 0) {
					manualsearch += " and date(ivrBnENTRY_DATETIME) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
			}
			
			
			
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				startrowfrom = Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow = Integer.parseInt(locMaxrow);
			}
			
				
			if (loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "ivrBnLBR_NAME like ('%" + loctocheNull+ "%') or " 
			                                + "ivrBnLBR_PH_NO like ('%" + loctocheNull + "%')  or "
			                                + "ivrBnLBR_EMAIL like ('%" + loctocheNull + "%'))"; 
			                               // + "ivrBNLbrSkilObj.iVOCATEGORY_ID.iVOCATEGORY_NAME like ('%" + loctocheNull + "%') or " 
			                             //   + "wrktypId.IVOlbrWORK_TYPE like ('%" + loctocheNull + "%'))";
				  loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS=" + locvrLBR_STS + manualsearch+ " " + globalsearch + " " + locOrderby;
			}else{
				loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS=" + locvrLBR_STS + manualsearch+ " " + locOrderby;
			}				
			
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web
				locvrLBR_cntQry = "select count(*) " + loc_slQry;
				LaborDaoservice lbrDaoObj=new LaborDaoservice();
				count=lbrDaoObj.getInitTotal(locvrLBR_cntQry);
				countFilter=count;
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry);
				System.out.println("locvrLBR_cntQry---------------------455556"+locvrLBR_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry, "info", LoadLabourReportTableData.class);
			} else {
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LoadLabourReportTableData.class);
			}
			
			System.out.println("Step 3 : Labor Details Query : "+loc_slQry);
			log.logMessage("Step 3 : Labor Details Query : "+loc_slQry, "info", LoadLabourReportTableData.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			
					
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (LaborDetailsTblVO) locObjLbrlst_itr.next();
								
				locInrJSONObj.put("lbr_id",lbrDtlVoObj.getIvrBnLBR_ID());	
				locInrJSONObj.put("lbr_grpid",String.valueOf(lbrDtlVoObj.getIvrBnGRP_CODE()));	
				locInrJSONObj.put("lbr_serviceid",lbrDtlVoObj.getIvrBnLBR_SERVICE_ID());	
				locInrJSONObj.put("lbr_name",lbrDtlVoObj.getIvrBnLBR_NAME());	
				locInrJSONObj.put("lbr_email",lbrDtlVoObj.getIvrBnLBR_EMAIL());	
				locInrJSONObj.put("lbr_isdcode",lbrDtlVoObj.getIvrBnLBR_ISD_CODE());
				locInrJSONObj.put("lbr_mobno",lbrDtlVoObj.getIvrBnLBR_PH_NO());
				
				locInrJSONObj.put("lbr_verifedstatus",String.valueOf(lbrDtlVoObj.getIvrBnLBR_VERIFIED_STATUS()));// 0 - unverified, 1- verified
				locInrJSONObj.put("lbr_kyforsrch",lbrDtlVoObj.getIvrBnLBR_KEY_FOR_SEARCH());
				
				locInrJSONObj.put("lbr_desc",lbrDtlVoObj.getIvrBnLBR_DESCP());				
				locInrJSONObj.put("lbr_address1",lbrDtlVoObj.getIvrBnLBR_ADD_1());
				locInrJSONObj.put("lbr_address2",lbrDtlVoObj.getIvrBnLBR_ADD_2());
				
				if ( lbrDtlVoObj.getCompany() != null){
					locInrJSONObj.put("lbr_cmpnyname",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCompany().getCompanyName()));
				} else {
					locInrJSONObj.put("lbr_cmpnyname","");
				}
				
				
				if(lbrDtlVoObj.getPstlId()!=null){
//					locInrJSONObj.put("lbr_pincodeid",String.valueOf(lbrDtlVoObj.getPstlId().getPstlId()));
//					locInrJSONObj.put("lbr_pincodeName",lbrDtlVoObj.getPstlId().getPstlCode());
//					locInrJSONObj.put("lbr_cityid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getCityId()));
//					locInrJSONObj.put("lbr_cityName",lbrDtlVoObj.getPstlId().getCityId().getCityName());
//					locInrJSONObj.put("lbr_stateid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateId()));
//					locInrJSONObj.put("lbr_stateName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName());
//					locInrJSONObj.put("lbr_cntryid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//					locInrJSONObj.put("lbr_cntryName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());
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
				
				String format="dd-MM-yyyy hh:mm:ss";
				CommonUtils commonutils=new CommonUtils();
				if(lbrDtlVoObj.getIvrBnENTRY_DATETIME()!=null){
				locInrJSONObj.put("entryDatetime", Commonutility.toCheckNullEmpty(commonutils.dateToString(lbrDtlVoObj.getIvrBnENTRY_DATETIME(), format) ));
				}
				if(lbrDtlVoObj.getModifyDatetime()!=null){
				locInrJSONObj.put("modifyDatetime", Commonutility.toCheckNullEmpty(commonutils.dateToString(lbrDtlVoObj.getModifyDatetime(), format) ));
				}		
				loc_slQry_categry="from LaborSkillTblVO where ivrBnLBR_ID="+lbrDtlVoObj.getIvrBnLBR_ID()+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"";
								
				locObjLbrcateglst_itr=pSession.createQuery(loc_slQry_categry).list().iterator();
				System.out.println("Step 3 : Select labor category detail query executed.");
				
				locLBRSklJSONAryobj=new JSONArray();
				while (locObjLbrcateglst_itr!=null &&  locObjLbrcateglst_itr.hasNext() ) {
					locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr.next();
					locInrLbrSklJSONObj=new JSONObject();
					if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){
						locInrLbrSklJSONObj.put("categoryid", locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID());
						locInrLbrSklJSONObj.put("categoryname", locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
					}				
					locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
				}
				log.logMessage("Step 3: Select Labor categoey detail block end.", "info", LoadLabourReportTableData.class);
				System.out.println("Step 4 : Select labor category detail are formed into JSONObject - json array - Filan JSON.");
				locInrJSONObj.put("jArry_lbr_catg", locLBRSklJSONAryobj);									
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			System.out.println("Step 5 : Return JSON Array DATA : "+locLBRJSONAryobj);	
			
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("labordetails", locLBRJSONAryobj);
			
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 6 : Select labor detail block end.");
			log.logMessage("Step 4: Select Labor detail block end.", "info", LoadLabourReportTableData.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toLBRSelectAll() : "+e);
			log.logMessage("Step -1 : labor select all block Exception : "+e, "error", LoadLabourReportTableData.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("labordetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
			locObjLbrcateglst_itr=null;locLbrSkiltbl=null;locInrLbrSklJSONObj=null;locLBRSklJSONAryobj=null;loc_slQry_categry=null;

		}
	}
	
	public static JSONObject toLBRSelectAllCmplt(JSONObject pDataJson, Session pSession) {
		System.out.println("complaint loading side labor===");
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		LaborDetailsTblVO lbrDtlVoObj=null;
		String locvrLBR_ID=null,locvrLBR_SERVICE_ID=null, locvrLBR_name=null, locvrLBR_Email=null;
		int count=0,countFilter=0;
		
		String loc_slQry_categry=null;		
		Iterator locObjLbrcateglst_itr=null;
		LaborSkillTblVO locLbrSkiltbl=null;
		JSONArray locLBRSklJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null;
		
		try {
		log=new Log();	lbrDtlVoObj=new LaborDetailsTblVO();			
		System.out.println("Step 1 : labor select all block enter");
		log.logMessage("Step 1 : labor select all block enter", "info", LoadLabourReportTableData.class);			
		locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrstatus");
		locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
		locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
	
		locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
		if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
			locvrLBR_cntQry="select count(ivrBnLBR_ID) from LaborDetailsTblVO where ivrBnLBR_STS="+locvrLBR_STS+"";
			System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry);
			log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry, "info", LoadLabourReportTableData.class);
			
			LaborDaoservice lbrDaoObj=new LaborDaoservice();
			count=lbrDaoObj.getInitTotal(locvrLBR_cntQry);
			countFilter=lbrDaoObj.getTotalFilter(locvrLBR_cntQry);
		}else{ // for mobile
			 count=0;countFilter=0;
			 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
			 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LoadLabourReportTableData.class);
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
		System.out.println("Step 3 : Labor Details Query : "+loc_slQry);
		log.logMessage("Step 3 : Labor Details Query : "+loc_slQry, "info", LoadLabourReportTableData.class);
		locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(0).setMaxResults(count).list().iterator();
		
				
		locLBRJSONAryobj=new JSONArray();
		while ( locObjLbrlst_itr.hasNext() ) {
			locInrJSONObj=new JSONObject();
			lbrDtlVoObj = (LaborDetailsTblVO) locObjLbrlst_itr.next();
			//System.out.println("------------------------Start-------------------------");					
			//System.out.println("Name : "+lbrDtlVoObj.getIvrBnLBR_NAME());					
			//System.out.println("Email : "+lbrDtlVoObj.getIvrBnLBR_EMAIL());
			
			locInrJSONObj.put("lbr_id",lbrDtlVoObj.getIvrBnLBR_ID());	
			locInrJSONObj.put("lbr_serviceid",lbrDtlVoObj.getIvrBnLBR_SERVICE_ID());	
			locInrJSONObj.put("lbr_name",lbrDtlVoObj.getIvrBnLBR_NAME());	
			locInrJSONObj.put("lbr_email",lbrDtlVoObj.getIvrBnLBR_EMAIL());	
			locInrJSONObj.put("lbr_isdcode",lbrDtlVoObj.getIvrBnLBR_ISD_CODE());
			locInrJSONObj.put("lbr_mobno",lbrDtlVoObj.getIvrBnLBR_PH_NO());
			
			locInrJSONObj.put("lbr_verifedstatus",String.valueOf(lbrDtlVoObj.getIvrBnLBR_VERIFIED_STATUS()));// 0 - unverified, 1- verified
			locInrJSONObj.put("lbr_kyforsrch",lbrDtlVoObj.getIvrBnLBR_KEY_FOR_SEARCH());
			
			locInrJSONObj.put("lbr_desc",lbrDtlVoObj.getIvrBnLBR_DESCP());				
			locInrJSONObj.put("lbr_address1",lbrDtlVoObj.getIvrBnLBR_ADD_1());
			locInrJSONObj.put("lbr_address2",lbrDtlVoObj.getIvrBnLBR_ADD_2());
			
			if(lbrDtlVoObj.getPstlId()!=null){
				//System.out.println("Postal Code : "+lbrDtlVoObj.getPstlId().getPstlCode());
				//System.out.println("City : "+lbrDtlVoObj.getPstlId().getCityId().getCityName());
				//System.out.println("State : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName());
				//System.out.println("Country : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());								
//				locInrJSONObj.put("lbr_pincodeid",String.valueOf(lbrDtlVoObj.getPstlId().getPstlId()));
//				locInrJSONObj.put("lbr_pincodeName",lbrDtlVoObj.getPstlId().getPstlCode());
//				locInrJSONObj.put("lbr_cityid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getCityId()));
//				locInrJSONObj.put("lbr_cityName",lbrDtlVoObj.getPstlId().getCityId().getCityName());
//				locInrJSONObj.put("lbr_stateid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateId()));
//				locInrJSONObj.put("lbr_stateName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName());
//				locInrJSONObj.put("lbr_cntryid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//				locInrJSONObj.put("lbr_cntryName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());
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
				locInrJSONObj.put("lbr_wrktypid",String.valueOf(lbrDtlVoObj.getWrktypId().getWrktypId()));
				locInrJSONObj.put("lbr_wrktypName",lbrDtlVoObj.getWrktypId().getIVOlbrWORK_TYPE());					
			}else{
				locInrJSONObj.put("lbr_wrktypid","");
				locInrJSONObj.put("lbr_wrktypName","");
			}							
			locInrJSONObj.put("lbr_rating",lbrDtlVoObj.getIvrBnLBR_RATING());// 0 - , 1 - , 2 - , 3 - , 4 -  
			locInrJSONObj.put("str_lbr_webimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIMAGE_NAME_WEB()));
			locInrJSONObj.put("str_lbr_mobileimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIvrBnIMAGE_NAME_MOBILE()));
							
			loc_slQry_categry="from LaborSkillTblVO where ivrBnLBR_ID="+lbrDtlVoObj.getIvrBnLBR_ID()+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"";
							
			locObjLbrcateglst_itr=pSession.createQuery(loc_slQry_categry).list().iterator();
			System.out.println("Step 3 : Select labor category detail query executed.");
			
			locLBRSklJSONAryobj=new JSONArray();
			while (locObjLbrcateglst_itr!=null &&  locObjLbrcateglst_itr.hasNext() ) {
				locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr.next();
				locInrLbrSklJSONObj=new JSONObject();
				if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){
					locInrLbrSklJSONObj.put("categoryid", locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID());
					locInrLbrSklJSONObj.put("categoryname", locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
				}				
				locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
			}
			log.logMessage("Step 3: Select Labor categoey detail block end.", "info", LoadLabourReportTableData.class);
			System.out.println("Step 4 : Select labor category detail are formed into JSONObject - json array - Filan JSON.");
			locInrJSONObj.put("jArry_lbr_catg", locLBRSklJSONAryobj);
			
														
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
		log.logMessage("Step 4: Select Labor detail block end.", "info", LoadLabourReportTableData.class);
		return locFinalRTNObj;
	} catch (Exception e) {
		try{
		System.out.println("Exception in toLBRSelectAll() : "+e);
		log.logMessage("Step -1 : labor select all block Exception : "+e, "error", LoadLabourReportTableData.class);	
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
		locObjLbrcateglst_itr=null;locLbrSkiltbl=null;locInrLbrSklJSONObj=null;locLBRSklJSONAryobj=null;loc_slQry_categry=null;

	}
		
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
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
