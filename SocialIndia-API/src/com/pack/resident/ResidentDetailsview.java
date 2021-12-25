package com.pack.resident;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.resident.persistance.ResidentDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ResidentDetailsview extends ActionSupport {

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
				locObjRspdataJson=toRESTSelectAll(locObjRecvdataJson,locObjsession,getText("Grp.resident"));	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("RESTAD006"), "RESTAD006", ivrCurrntusrid);
						serverResponse("SI7000","1","E7000",getText("resident.selectall.error"),locObjRspdataJson);
					}else{
						AuditTrial.toWriteAudit(getText("RESTAD005"), "RESTAD005", ivrCurrntusrid);
						serverResponse("SI7000","0","SI7000",getText("resident.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI7000, "+getText("request.format.notmach"), "info", ResidentDetailsview.class);
					serverResponse("SI7000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI7000, "+getText("request.values.empty"), "info", ResidentDetailsview.class);
				serverResponse("SI7000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI7000, "+getText("catch.error"), "error", ResidentDetailsview.class);
			serverResponse("SI7000","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject toRESTSelectAll(JSONObject pDataJson, Session pSession,String pGrpName) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null,locsocietyid=null,locvrEventSTS=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		UserMasterTblVo lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;
		String locSlqry=null;
		Query locQryrst=null;
		GroupMasterTblVo locGrpMstrVOobj=null,locGRPMstrvo=null;
		Iterator locObjLbrcateglst_itr=null;
		String lvrOrderby = null;
		try {
			log=new Log();	lbrDtlVoObj=new UserMasterTblVo();			
			System.out.println("Step 1 : resident select all block enter");
			log.logMessage("Step 1 : resident select all block enter", "info", ResidentDetailsview.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "rsdtstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			locsocietyid=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginsocietyid");
			String selectfield = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "slectfield");
			String searchword = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "searchWord");
			locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			int locGrpcode=0;
			//Group code get 
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";			 
			 locQryrst=pSession.createQuery(locSlqry);			
			 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
			 if(locGrpMstrVOobj!=null){
				 locGRPMstrvo=new GroupMasterTblVo();
				 locGrpcode=locGrpMstrVOobj.getGroupCode();
								 
			 }else{// new group create
				 uamDao uam=new uamDaoServices();
				 locGrpcode=uam.createnewgroup_rtnId(pGrpName);
				 		 				
			 }					 
			 String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);						
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
				String globalsearch=null;
				String lvrstats = " statusFlag in (0,1) ";
				lvrOrderby = "order by entryDatetime desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				if (locsocietyid.equalsIgnoreCase("-1")) {
				     globalsearch = " AND (" + "firstName like ('%" + loctocheNull+ "%') or " 
			                                + "mobileNo like ('%" + loctocheNull + "%')  or "
			                                + "emailId like ('%" + loctocheNull + "%') or "
			                                + " societyId.townshipId.townshipName like ('%" + loctocheNull + "%') or "
								             + " societyId.societyName like ('%" + loctocheNull + "%') "
								             + ")";
				} else {
					 globalsearch = " and societyId.societyId="+locsocietyid+" AND (" + "firstName like ('%" + loctocheNull+ "%') or " 
                             + "mobileNo like ('%" + loctocheNull + "%')  or "
                             + "emailId like ('%" + loctocheNull + "%') or"
                             + " societyId.townshipId.townshipName like ('%" + loctocheNull + "%') or "
				             + " societyId.societyName like ('%" + loctocheNull + "%') "
				             + ")and societyId.societyId ="+locsocietyid;		
				}				
				/*  loc_slQry="from UserMasterTblVo  where groupCode = "+locGrpcode + " " +globalsearch + lvrstats + lvrOrderby;*/
			} else {
				if(searchword!=null && !searchword.equalsIgnoreCase("null") && !searchword.equalsIgnoreCase("")){// Top search box
					if(!locsocietyid.equalsIgnoreCase("-1")){
						globalsearch = " and ";
						if(selectfield!=null && selectfield.equalsIgnoreCase("1")){														 		 
							globalsearch = " and (" + " mobileNo like ('%" + searchword + "%')"
									 + ") and societyId.societyId ="+locsocietyid;	
						}else if(selectfield!=null && selectfield.equalsIgnoreCase("2")){
							globalsearch = "  and(" + "firstName like ('%" + searchword + "%')"
									 + ") and societyId.societyId ="+locsocietyid;	
						}
						else if(selectfield!=null && selectfield.equalsIgnoreCase("3")){
							globalsearch = " and (" + " societyId.townshipId.townshipName like ('%" + searchword + "%')"
									 + ") and societyId.societyId ="+locsocietyid;	
							
						}
						else if(selectfield!=null && selectfield.equalsIgnoreCase("4")){
							globalsearch = " and (" + " societyId.societyName like ('%" + searchword + "%') "
									 + ") and societyId.societyId ="+locsocietyid;	
						}
						else if(selectfield!=null && selectfield.equalsIgnoreCase("5")){
							globalsearch = " and (" + " emailId like ('%" + searchword + "%') "
									 + ") and societyId.societyId ="+locsocietyid;	
						}
						else{
							globalsearch = " and (" + " emailId like ('%" + searchword + "%') or " 
						             + " mobileNo like ('%" + searchword + "%') or "
						             + " firstName like ('%" + searchword + "%') or "
						             + " societyId.townshipId.townshipName like ('%" + searchword + "%') or "
						             + " societyId.societyName like ('%" + searchword + "%') "
						             + ") and societyId.societyId ="+locsocietyid;	
						}
						
					}else{
						globalsearch = " and ";
						if(selectfield!=null && selectfield.equalsIgnoreCase("1")){														 		 
							globalsearch = " and (" + " mobileNo like ('%" + searchword + "%') ) "; 
						}else if(selectfield!=null && selectfield.equalsIgnoreCase("2")){
							globalsearch = "  and(" + "firstName like ('%" + searchword + "%')) " ; 
						}
						else if(selectfield!=null && selectfield.equalsIgnoreCase("3")){
							globalsearch = " and (" + " societyId.townshipId.townshipName like ('%" + searchword + "%') )" ;
						}
						else if(selectfield!=null && selectfield.equalsIgnoreCase("4")){
							globalsearch = " and (" + " societyId.societyName like ('%" + searchword + "%') )" ;
							
						}
						else if(selectfield!=null && selectfield.equalsIgnoreCase("5")){
							globalsearch = " and (" + " emailId like ('%" + searchword + "%'))" ;
						}else{
							globalsearch = " and (" + " emailId like ('%" + searchword + "%') or " 
						             + " mobileNo like ('%" + searchword + "%') or "
						             + " firstName like ('%" + searchword + "%') or "
						             + " societyId.townshipId.townshipName like ('%" + searchword + "%') or "
						             + " societyId.societyName like ('%" + searchword + "%') "
						             + ")";
						}
					}
					
				}else{
					if(!locsocietyid.equalsIgnoreCase("-1")){
						globalsearch = " and societyId.societyId = "+locsocietyid;
					}else{
						globalsearch = "";
					}
					
				}										
			
		 /*
				if (locsocietyid==null || locsocietyid.equalsIgnoreCase("-1")) {				
					loc_slQry="from UserMasterTblVo  where groupCode = "+locGrpcode+" " + lvrstats + lvrOrderby;
				} else {					
					loc_slQry="from UserMasterTblVo  where societyId.societyId="+locsocietyid+" AND groupCode = "+locGrpcode+" " + lvrstats + lvrOrderby;
				}*/
			}	
			//loc_slQry = "from UserMasterTblVo  where statusFlag=" + locvrEventSTS + "" + globalsearch+" and groupCode = "+locGrpcode + lvrOrderby;
			loc_slQry = "from UserMasterTblVo  where " + lvrstats + "" + globalsearch+" and groupCode = "+locGrpcode + lvrOrderby;
			System.out.println("loc_slQry -------- "+loc_slQry);
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				locvrLBR_cntQry = "select count(*) " + loc_slQry;
				ResidentDaoservice restDaoObj=new ResidentDaoservice();
				count=restDaoObj.getInitTotal(locvrLBR_cntQry);
				countFilter=restDaoObj.getTotalFilter(locvrLBR_cntQry);
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry, "info", ResidentDetailsview.class);
			} else {
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", ResidentDetailsview.class);
			}
			
			System.out.println("Step 3 : resident Details Query : "+loc_slQry);
			log.logMessage("Step 3 : resident Details Query : "+loc_slQry, "info", ResidentDetailsview.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();								
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (UserMasterTblVo) locObjLbrlst_itr.next();
				locInrJSONObj.put("rest_id",lbrDtlVoObj.getUserId());	
				locInrJSONObj.put("rest_name",lbrDtlVoObj.getUserName());
				locInrJSONObj.put("rest_dob",lbrDtlVoObj.getDob());
				locInrJSONObj.put("rest_email",lbrDtlVoObj.getEmailId());	
				locInrJSONObj.put("rest_isdcode",lbrDtlVoObj.getIsdCode());
				locInrJSONObj.put("rest_mobno",lbrDtlVoObj.getMobileNo());
				
				locInrJSONObj.put("rest_flatno",String.valueOf(lbrDtlVoObj.getFlatNo()));// 0 - unverified, 1- verified
				locInrJSONObj.put("rest_idprfno",lbrDtlVoObj.getIdProofNo());
				
				locInrJSONObj.put("rest_bloodtype",lbrDtlVoObj.getBloodType());				
				locInrJSONObj.put("rest_address1",lbrDtlVoObj.getAddress1());
				locInrJSONObj.put("rest_address2",lbrDtlVoObj.getAddress2());
				locInrJSONObj.put("rest_firstname",lbrDtlVoObj.getFirstName());
				locInrJSONObj.put("rest_lastname",lbrDtlVoObj.getLastName());
				locInrJSONObj.put("rest_fulname",lbrDtlVoObj.getFirstName() + lbrDtlVoObj.getLastName());
				locInrJSONObj.put("status",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getStatusFlag()));
				if(lbrDtlVoObj.getPstlId()!=null){				
//					locInrJSONObj.put("rest_pincodeid",String.valueOf(lbrDtlVoObj.getPstlId().getPstlId()));
//					locInrJSONObj.put("rest_pincodeName",lbrDtlVoObj.getPstlId().getPstlCode());
//					locInrJSONObj.put("rest_cityid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getCityId()));
//					locInrJSONObj.put("rest_cityName",lbrDtlVoObj.getPstlId().getCityId().getCityName());
//					locInrJSONObj.put("rest_stateid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateId()));
//					locInrJSONObj.put("rest_stateName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getStateName());
//					locInrJSONObj.put("rest_cntryid",String.valueOf(lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryId()));
//					locInrJSONObj.put("rest_cntryName",lbrDtlVoObj.getPstlId().getCityId().getStateId().getCountryId().getCountryName());		
					
					locInrJSONObj.put("rest_pincodeid",String.valueOf(lbrDtlVoObj.getPstlId()));
					locInrJSONObj.put("rest_pincodeName",lbrDtlVoObj.getPstlId());
					
				}else{
					locInrJSONObj.put("rest_pincodeid","");
					locInrJSONObj.put("rest_pincodeName","");
//					locInrJSONObj.put("rest_cityid","");
//					locInrJSONObj.put("rest_cityName","");
//					locInrJSONObj.put("rest_stateid","");
//					locInrJSONObj.put("rest_stateName","");
//					locInrJSONObj.put("rest_cntryid","");
//					locInrJSONObj.put("rest_cntryName","");
				}
				
				if(lbrDtlVoObj.getCityId() != null ){
					locInrJSONObj.put("rest_cityid",String.valueOf(lbrDtlVoObj.getCityId().getCityId()));
					locInrJSONObj.put("rest_cityName",lbrDtlVoObj.getCityId().getCityName());
					
					if(lbrDtlVoObj.getStateId() != null ){
						locInrJSONObj.put("rest_stateid",String.valueOf(lbrDtlVoObj.getCityId().getStateId().getStateId()));
						locInrJSONObj.put("rest_stateName",lbrDtlVoObj.getCityId().getStateId().getStateName());
						
						if(lbrDtlVoObj.getCountryId() != null ){
							locInrJSONObj.put("rest_cntryid",String.valueOf(lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryId()));
							locInrJSONObj.put("rest_cntryName",lbrDtlVoObj.getCityId().getStateId().getCountryId().getCountryName());
						}
						else{
							locInrJSONObj.put("rest_cntryid","");
							locInrJSONObj.put("rest_cntryName","");
						}
					}
					else{
						locInrJSONObj.put("rest_stateid","");
						locInrJSONObj.put("rest_stateName","");
						locInrJSONObj.put("rest_cntryid","");
						locInrJSONObj.put("rest_cntryName","");
					}
				}
				else{
					locInrJSONObj.put("rest_cityid","");
					locInrJSONObj.put("rest_cityName","");
					locInrJSONObj.put("rest_stateid","");
					locInrJSONObj.put("rest_stateName","");
					locInrJSONObj.put("rest_cntryid","");
					locInrJSONObj.put("rest_cntryName","");
				}
				
				if (lbrDtlVoObj.getSocietyId()!=null) {
					locInrJSONObj.put("societyname", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getSocietyId().getSocietyName()));
					if (lbrDtlVoObj.getSocietyId().getTownshipId()!=null) {
						locInrJSONObj.put("townshipname", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getSocietyId().getTownshipId().getTownshipName()));
					} else {
						locInrJSONObj.put("townshipname", "");
					}
					
				} else {
					locInrJSONObj.put("societyname", "");
					locInrJSONObj.put("townshipname", "");
				}
				
				/*locInrJSONObj.put("str_rest_webimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getImageNameWeb()));
				locInrJSONObj.put("str_rest_mobileimage", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getImageNameMob()));*/
												
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);	
			
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("residentdetails", locLBRJSONAryobj);
			
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 6 : Select resident detail block end.");
			log.logMessage("Step 4: Select resident detail block end.", "info", ResidentDetailsview.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toRESTSelectAll() : "+e);
			log.logMessage("Step -1 : resident select all block Exception : "+e, "error", ResidentDetailsview.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("residentdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
			locObjLbrcateglst_itr=null;locsocietyid=null;

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
