package com.pack.monitoringmgmt;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Imagemonitoring extends ActionSupport {

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
				locObjRspdataJson=imagemonitoringSelectAll(locObjRecvdataJson,locObjsession);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("IMGMNTAD01"), "IMGMNTAD01", ivrCurrntusrid);
						serverResponse("SI10008","0","E10008",getText("imagemonitoring.view.error"),locObjRspdataJson);
					}else{
						AuditTrial.toWriteAudit(getText("IMGMNTAD00"), "IMGMNTAD00", ivrCurrntusrid);
						serverResponse("SI10008","0","S10008",getText("imagemonitoring.view.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI10008, "+getText("request.format.notmach"), "info", Imagemonitoring.class);
					serverResponse("SI10008","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI10008, "+getText("request.values.empty"), "info", Imagemonitoring.class);
				serverResponse("SI10008","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI10008, "+getText("catch.error"), "error", Imagemonitoring.class);
			serverResponse("SI10008","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject imagemonitoringSelectAll(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrEdu_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		FeedattchTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;		
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();	lbrDtlVoObj=new FeedattchTblVO();			
			System.out.println("Step 1 : image monitoring  select all block enter");
			log.logMessage("Step 1 : image monitoring  select all block enter", "info", Imagemonitoring.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			/*
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				if(loctocheNull.equalsIgnoreCase("")) {
					locvrEdu_cntQry="select count(ivrBnATTCH_ID) from FeedattchTblVO where ivrBnSTATUS in(0,1)";
				} else {
					locvrEdu_cntQry="select count(ivrBnATTCH_ID) from FeedattchTblVO where ivrBnSTATUS in(0,1) AND (" + "ivrBnFEED_TXT like ('%" + loctocheNull+ "%')  " + ")";
				}
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry, "info", Imagemonitoring.class);
				
				WorktypeDao IdcardDaoObj=new WorktypeDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrEdu_cntQry);
				countFilter=IdcardDaoObj.getTotalFilter(locvrEdu_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Imagemonitoring.class);
			} */
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}				
				String globalsearch=null;
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "ivrBnSTATUS like ('%" + loctocheNull+ "%') or "  + "ivrBnFEED_TXT like ('%" + loctocheNull + "%'))";
				  loc_slQry="from FeedattchTblVO  where ivrBnSTATUS in(0,1) " +globalsearch;
			} else{
				loc_slQry="from FeedattchTblVO  where ivrBnSTATUS in(0,1)";	
			}
			
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				locvrEdu_cntQry="select count(ivrBnATTCH_ID) " + loc_slQry;
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry, "info", Imagemonitoring.class);
				
				WorktypeDao IdcardDaoObj=new WorktypeDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrEdu_cntQry);
				countFilter=IdcardDaoObj.getTotalFilter(locvrEdu_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Imagemonitoring.class);
			}
						
			System.out.println("Step 3 : image monitoring  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : image monitoring  Details Query : "+loc_slQry, "info", Imagemonitoring.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();								
			
			
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (FeedattchTblVO) locObjLbrlst_itr.next();								
				locInrJSONObj.put("imgmont_id",lbrDtlVoObj.getIvrBnATTCH_ID());	
				locInrJSONObj.put("imgmont_attachname",lbrDtlVoObj.getIvrBnATTACH_NAME());
				locInrJSONObj.put("imgmont_filetype",lbrDtlVoObj.getIvrBnFILE_TYPE());
				locInrJSONObj.put("imgmont_status",lbrDtlVoObj.getIvrBnSTATUS());	
				locInrJSONObj.put("imgmont_feedid",lbrDtlVoObj.getIvrBnFEED_ID().getFeedId());
				//locInrJSONObj.put("imgmont_usrname",lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getFirstName());	
				
				if(Commonutility.checkempty(lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getFirstName())){
					locInrJSONObj.put("imgmont_usrname",lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getFirstName());	
				} else {
					if(Commonutility.checkempty(lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getLastName())){
						locInrJSONObj.put("imgmont_usrname",lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getLastName());	
					} else {
						locInrJSONObj.put("imgmont_usrname",lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getIsdCode() + lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getMobileNo());	
					}
				}
				
				locInrJSONObj.put("imgmont_usrid",lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getUserId());	
				locInrJSONObj.put("imgmont_entrydate",lbrDtlVoObj.getIvrBnENTRY_DATETIME());	
				locInrJSONObj.put("imgmontusr_status",lbrDtlVoObj.getIvrBnFEED_ID().getUsrId().getStatusFlag());	
				locInrJSONObj.put("imgmont_tblflag",rb.getString("notification.reflg.timelineimagemontoring"));	
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			
										
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("imgmontdetails", locLBRJSONAryobj);			
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", Imagemonitoring.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", Imagemonitoring.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("imgmontdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
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
