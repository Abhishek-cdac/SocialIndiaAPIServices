package com.pack.company;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.company.persistance.CompanyDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class CompanyDetailsview extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json		
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
				locObjRspdataJson=toCMPYSelectAll(locObjRecvdataJson,locObjsession);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("CMPY014"), "CMPY014", ivrCurrntusrid);
						serverResponse("SI6001","0","E6001",getText("Company.selectall.error"),locObjRspdataJson);
					}else{
						AuditTrial.toWriteAudit(getText("CMPYAD013"), "CMPYAD013", ivrCurrntusrid);
						serverResponse("SI6001","0","S6001",getText("Company.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6001, "+getText("request.format.notmach"), "info", CompanyDetailsview.class);
					serverResponse("SI6001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6001, "+getText("request.values.empty"), "info", CompanyDetailsview.class);
				serverResponse("SI6001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6001, "+getText("catch.error"), "error", CompanyDetailsview.class);
			serverResponse("SI6001","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject toCMPYSelectAll(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		CompanyMstTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;						
		try {
			log=new Log();	lbrDtlVoObj=new CompanyMstTblVO();			
			Commonutility.toWriteConsole("Step 1 : company select all block enter");
			log.logMessage("Step 1 : company select all block enter", "info", CompanyDetailsview.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "lbrstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				if (loctocheNull.equalsIgnoreCase("")) {
					locvrLBR_cntQry="select count(companyId) from CompanyMstTblVO where status="+locvrLBR_STS+"";
				} else {
					locvrLBR_cntQry="select count(companyId) from CompanyMstTblVO where status="+locvrLBR_STS+" AND (" + "companyName like ('%" + loctocheNull+ "%') or " 
			                                + "mobileNo like ('%" + loctocheNull + "%')  or "			                                
			                                + "companyEmail like ('%" + loctocheNull + "%') or "
			                                + "cmpnyRegno like ('%" + loctocheNull + "%'))";
				}
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry, "info", CompanyDetailsview.class);
				
				CompanyDaoservice cmpyDaoObj=new CompanyDaoservice();
				count=cmpyDaoObj.getInitTotal(locvrLBR_cntQry);
				countFilter=cmpyDaoObj.getTotalFilter(locvrLBR_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", CompanyDetailsview.class);
			}
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
			String globalsearch=null;
			String locOrderby =" order by entryDatetime desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "companyName like ('%" + loctocheNull+ "%') or " 
			                                + "mobileNo like ('%" + loctocheNull + "%')  or "
			                                + "companyEmail like ('%" + loctocheNull + "%') or "
				  							+ "cmpnyRegno like ('%" + loctocheNull + "%'))";
				  loc_slQry="from CompanyMstTblVO  where status="+locvrLBR_STS+" " +globalsearch + " " +locOrderby;
			}else{
				loc_slQry="from CompanyMstTblVO  where status="+locvrLBR_STS+" "+locOrderby;	
			}				
			//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_STS+"";	
			Commonutility.toWriteConsole("Step 3 : company Details Query : "+loc_slQry);
			log.logMessage("Step 3 : company Details Query : "+loc_slQry, "info", CompanyDetailsview.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();								
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (CompanyMstTblVO) locObjLbrlst_itr.next();								
				locInrJSONObj.put("cmpy_id",lbrDtlVoObj.getCompanyId());	
				
				locInrJSONObj.put("cmpy_name",lbrDtlVoObj.getCompanyName());
				locInrJSONObj.put("cmpy_regno",lbrDtlVoObj.getCmpnyRegno());
				locInrJSONObj.put("cmpy_email",lbrDtlVoObj.getCompanyEmail());	
				locInrJSONObj.put("cmpy_isdcode",lbrDtlVoObj.getIsdCode());
				locInrJSONObj.put("cmpy_mobno",lbrDtlVoObj.getMobileNo());
				
				locInrJSONObj.put("cmpy_verifedstatus",String.valueOf(lbrDtlVoObj.getVerifyStatus()));// 0 - unverified, 1- verified
				locInrJSONObj.put("cmpy_kyforsrch",lbrDtlVoObj.getKeyforSrch());
				
				locInrJSONObj.put("cmpy_desc",lbrDtlVoObj.getDescr());				
				locInrJSONObj.put("cmpy_address1",lbrDtlVoObj.getAddress1());
				locInrJSONObj.put("cmpy_address2",lbrDtlVoObj.getAddress2());
				
				if(lbrDtlVoObj.getPstlId()!=null){
					//Commonutility.toWriteConsole("Postal Code : "+lbrDtlVoObj.getPstlId().getPstlCode());
					//Commonutility.toWriteConsole("City : "+lbrDtlVoObj.getPstlId().getCityId().getCityName());
					//Commonutility.toWriteConsole("State : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName());
					//Commonutility.toWriteConsole("Country : "+lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());								
//					locInrJSONObj.put("cmpy_pincodeid",String.valueOf(lbrDtlVoObj.getPstlId().getPstlId()));
//					locInrJSONObj.put("cmpy_pincodeName",lbrDtlVoObj.getPstlId().getPstlCode());
//					locInrJSONObj.put("cmpy_cityid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getCityId()));
//					locInrJSONObj.put("cmpy_cityName",lbrDtlVoObj.getPstlId().getCityId().getCityName());
//					locInrJSONObj.put("cmpy_stateid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateId()));
//					locInrJSONObj.put("cmpy_stateName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName());
//					locInrJSONObj.put("cmpy_cntryid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//					locInrJSONObj.put("cmpy_cntryName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());	
					
					locInrJSONObj.put("cmpy_pincodeid",String.valueOf(lbrDtlVoObj.getPstlId()));
					locInrJSONObj.put("cmpy_pincodeName",lbrDtlVoObj.getPstlId());
				}else{
					locInrJSONObj.put("cmpy_pincodeid","");
					locInrJSONObj.put("cmpy_pincodeName","");
//					locInrJSONObj.put("cmpy_cityid","");
//					locInrJSONObj.put("cmpy_cityName","");
//					locInrJSONObj.put("cmpy_stateid","");
//					locInrJSONObj.put("cmpy_stateName","");
//					locInrJSONObj.put("cmpy_cntryid","");
//					locInrJSONObj.put("cmpy_cntryName","");
				}
					
				if(lbrDtlVoObj.getCityId() != null){
				locInrJSONObj.put("cmpy_cityid",String.valueOf(lbrDtlVoObj.getCityId().getCityId()));
				locInrJSONObj.put("cmpy_cityName",lbrDtlVoObj.getCityId().getCityName());
				
					if(lbrDtlVoObj.getCityId().getStateId() != null){
					locInrJSONObj.put("cmpy_stateid",String.valueOf(lbrDtlVoObj.getCityId().getStateId().getStateId()));
					locInrJSONObj.put("cmpy_stateName",lbrDtlVoObj.getCityId().getStateId().getStateName());
					
						if(lbrDtlVoObj.getCityId().getStateId().getCountryId() != null){
						locInrJSONObj.put("cmpy_cntryid",String.valueOf(lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryId()));
						locInrJSONObj.put("cmpy_cntryName",lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryName());
						}
						else{
							locInrJSONObj.put("cmpy_cntryid","");
							locInrJSONObj.put("cmpy_cntryName","");
						}
					}
					else{
						locInrJSONObj.put("cmpy_stateid","");
						locInrJSONObj.put("cmpy_stateName","");
						locInrJSONObj.put("cmpy_cntryid","");
						locInrJSONObj.put("cmpy_cntryName","");
					}
				}
				else{
					locInrJSONObj.put("cmpy_cityid","");
					locInrJSONObj.put("cmpy_cityName","");
					locInrJSONObj.put("cmpy_stateid","");
					locInrJSONObj.put("cmpy_stateName","");
					locInrJSONObj.put("cmpy_cntryid","");
					locInrJSONObj.put("cmpy_cntryName","");
				}
			
				locInrJSONObj.put("str_cmpy_webimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getImageNameWeb()));
				locInrJSONObj.put("str_cmpy_mobileimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getImageNameMob()));
								
				//loc_slQry_categry="from CompanyMstTblVO where companyId="+lbrDtlVoObj.getCompanyId()+" and ivrBnLBR_SKILL_STS = "+locvrLBR_STS+"";
								
				//locObjLbrcateglst_itr=pSession.createQuery(loc_slQry_categry).list().iterator();
				//Commonutility.toWriteConsole("Step 3 : Select company category detail query executed.");
				
				//locLBRSklJSONAryobj=new JSONArray();
				/*while (locObjLbrcateglst_itr!=null &&  locObjLbrcateglst_itr.hasNext() ) {
					locLbrSkiltbl=(LaborSkillTblVO)locObjLbrcateglst_itr.next();
					locInrLbrSklJSONObj=new JSONObject();
					if(locLbrSkiltbl.getiVOCATEGORY_ID()!=null){
						locInrLbrSklJSONObj.put("categoryid", locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_ID());
						locInrLbrSklJSONObj.put("categoryname", locLbrSkiltbl.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
					}				
					locLBRSklJSONAryobj.put(locInrLbrSklJSONObj);
				}*/												
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			Commonutility.toWriteConsole("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("companydetails", locLBRJSONAryobj);			
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			Commonutility.toWriteConsole("Step 6 : Select company detail block end.");
			log.logMessage("Step 4: Select company detail block end.", "info", CompanyDetailsview.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
				Commonutility.toWriteConsole("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : company select all block Exception : "+e, "error", CompanyDetailsview.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("companydetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
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
