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
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Contentmonitoring extends ActionSupport {

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
				locObjRspdataJson=contentmonitoringSelectAll(locObjRecvdataJson,locObjsession);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("CNTMNTAD01"), "CNTMNTAD01", ivrCurrntusrid);
						serverResponse("SI10003","0","E10003",getText("content.view.error"),locObjRspdataJson);
					}else{
						AuditTrial.toWriteAudit(getText("CNTMNTAD00"), "CNTMNTAD00", ivrCurrntusrid);
						serverResponse("SI10003","0","S10003",getText("content.view.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI10003, "+getText("request.format.notmach"), "info", Contentmonitoring.class);
					serverResponse("SI10003","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI10003, "+getText("request.values.empty"), "info", Contentmonitoring.class);
				serverResponse("SI10003","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI10003, "+getText("catch.error"), "error", Contentmonitoring.class);
			serverResponse("SI10003","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject contentmonitoringSelectAll(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrEdu_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		FeedsTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;		
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();	lbrDtlVoObj=new FeedsTblVO();			
			System.out.println("Step 1 : content monitoring  select all block enter");
			log.logMessage("Step 1 : content monitoring  select all block enter", "info", Contentmonitoring.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
				String globalsearch=null;
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "feedStatus like ('%" + loctocheNull+ "%') or "  + "feedDesc like ('%" + loctocheNull + "%') or feedTitle like ('%" + loctocheNull + "%') or feedStitle like ('%" + loctocheNull + "%') or feedMsg like ('%" + loctocheNull + "%'))";
				  loc_slQry="from FeedsTblVO  where feedStatus in(0,1) " +globalsearch;
			}else{
				loc_slQry="from FeedsTblVO  where feedStatus in(0,1)";	
			}	
			
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 				
				locvrEdu_cntQry="select count(*) "+ loc_slQry;				
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry, "info", Contentmonitoring.class);
				
				WorktypeDao IdcardDaoObj=new WorktypeDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrEdu_cntQry);
				countFilter=IdcardDaoObj.getTotalFilter(locvrEdu_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Contentmonitoring.class);
			}
			System.out.println("Step 3 : content monitoring  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : content monitoring  Details Query : "+loc_slQry, "info", Contentmonitoring.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();								
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (FeedsTblVO) locObjLbrlst_itr.next();								
				locInrJSONObj.put("feed_id",lbrDtlVoObj.getFeedId());	
				locInrJSONObj.put("feed_text",lbrDtlVoObj.getFeedMsg() +"\n" +Commonutility.toCheckNullEmpty(lbrDtlVoObj.getFeedTitle())+"\n" +Commonutility.toCheckNullEmpty(lbrDtlVoObj.getFeedStitle()) + "\n" +Commonutility.toCheckNullEmpty(lbrDtlVoObj.getFeedDesc()));
				locInrJSONObj.put("feed_status",lbrDtlVoObj.getFeedStatus());	
				locInrJSONObj.put("feed_usrid",lbrDtlVoObj.getUsrId().getUserId());
				if(Commonutility.checkempty(lbrDtlVoObj.getUsrId().getFirstName())){
					locInrJSONObj.put("feed_usrname",lbrDtlVoObj.getUsrId().getFirstName());	
				} else {
					if(Commonutility.checkempty(lbrDtlVoObj.getUsrId().getLastName())){
						locInrJSONObj.put("feed_usrname",lbrDtlVoObj.getUsrId().getLastName());	
					} else {
						locInrJSONObj.put("feed_usrname",lbrDtlVoObj.getUsrId().getIsdCode() + lbrDtlVoObj.getUsrId().getMobileNo());	
					}
				}
				
				locInrJSONObj.put("feed_entrydate",lbrDtlVoObj.getEntryDatetime());	
				locInrJSONObj.put("feedusr_status",lbrDtlVoObj.getUsrId().getStatusFlag());	
				locInrJSONObj.put("feed_tblflag",rb.getString("notification.reflg.timelinefeed"));	
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("feeddetails", locLBRJSONAryobj);			
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", Contentmonitoring.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", Contentmonitoring.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("feeddetails", "");
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
