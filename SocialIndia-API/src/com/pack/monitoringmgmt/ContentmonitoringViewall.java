package com.pack.monitoringmgmt;

import java.io.PrintWriter;
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

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ContentmonitoringViewall extends ActionSupport {

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
		int count=0,countFilter=0;
		Integer startrowfrom=1, totalNorow=10;
		Query queryObj = null;
		JSONArray CONTmontMstList = null;
		
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
								
			
			
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 				
						
				 System.out.println("Step 2 : Count / Filter Count need.[WEB Call]");
				 log.logMessage("Step 2 : Count / Filter Count need.[WEB Call]", "info", Contentmonitoring.class);
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Contentmonitoring.class);
			}
			System.out.println("Step 3 : content monitoring  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : content monitoring  Details Query : "+loc_slQry, "info", Contentmonitoring.class);
			/*locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();	*/							
			locLBRJSONAryobj=new JSONArray();
			CONTmontMstList=new JSONArray ();
			
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				queryObj= pSession
						.createSQLQuery("CALL PR_CONTENT_MONITORING_FILTER (:STARTROW,:TOTROW,:SRCHVAL)")
						.addScalar("ID", Hibernate.INTEGER)
						.addScalar("ATTACH_ID", Hibernate.INTEGER)
						.addScalar("NAME", Hibernate.TEXT)
						.addScalar("TITLE", Hibernate.TEXT)
						.addScalar("SUBTITLE", Hibernate.TEXT)
						.addScalar("DESCRIPTION", Hibernate.TEXT)
						.addScalar("SHORTDESCRIPTION", Hibernate.TEXT)
						.addScalar("MESSAGES", Hibernate.TEXT)
						.addScalar("USR_ID", Hibernate.INTEGER)
						.addScalar("FNAME", Hibernate.TEXT)
						.addScalar("LNAME", Hibernate.TEXT)
						.addScalar("MOBILE_NO", Hibernate.TEXT)
				        .addScalar("ENTRY_DATE", Hibernate.TEXT)
				        .addScalar("STATUS", Hibernate.INTEGER)
				        .addScalar("USER_STATUS", Hibernate.INTEGER)
				        .addScalar("NO_OF_RECORD", Hibernate.INTEGER)
				        .addScalar("TABLE_NAME", Hibernate.TEXT);
						
	              
				        queryObj.setInteger("STARTROW", startrowfrom);
				        queryObj.setInteger("TOTROW", totalNorow);
				        queryObj.setString("SRCHVAL", loctocheNull);
			}else{
				queryObj= pSession
						.createSQLQuery("CALL PR_CONTENT_MONITORING(:STARTROW,:TOTROW)")
						.addScalar("ID", Hibernate.INTEGER)
						.addScalar("ATTACH_ID", Hibernate.INTEGER)
						.addScalar("NAME", Hibernate.TEXT)
						.addScalar("TITLE", Hibernate.TEXT)
						.addScalar("SUBTITLE", Hibernate.TEXT)
						.addScalar("DESCRIPTION", Hibernate.TEXT)
						.addScalar("SHORTDESCRIPTION", Hibernate.TEXT)
						.addScalar("MESSAGES", Hibernate.TEXT)
						.addScalar("USR_ID", Hibernate.INTEGER)
						.addScalar("FNAME", Hibernate.TEXT)
						.addScalar("LNAME", Hibernate.TEXT)
						.addScalar("MOBILE_NO", Hibernate.TEXT)
				        .addScalar("ENTRY_DATE", Hibernate.TEXT)
				        .addScalar("STATUS", Hibernate.INTEGER)
				        .addScalar("USER_STATUS", Hibernate.INTEGER)
				        .addScalar("NO_OF_RECORD", Hibernate.INTEGER)
				        .addScalar("TABLE_NAME", Hibernate.TEXT);
						
	              
				        queryObj.setInteger("STARTROW", startrowfrom);
				        queryObj.setInteger("TOTROW", totalNorow);
			}	
			
			    	List result = queryObj.list();	
					System.out.println("---------------------------1");
					System.out.println("---------------------------1ENGHT" + result.size());
					String lvrCntent = "";
					for (int i = 0; i < result.size(); i++) {
						JSONObject nn = null;
						Object[] obj = (Object[]) result.get(i);
						if (obj != null && obj.length >= 15) {
							lvrCntent = "";
							Integer unique_id = (Integer) obj[0];
							Integer attach_id = (Integer) obj[1];
							String lvrEeventName = (String) obj[2];
							String lvrTitle = (String) obj[3];
							String lvrSUBTITLE = (String) obj[4];
							String lvrDESCRIPTION = (String) obj[5];
							String lvrSHORTDESCRIPTION = (String) obj[6];
							String lvrMESSAGES = (String) obj[7];
							Integer usr_id = (Integer) obj[8];
							String  fname = (String) obj[9];
							String  lname = (String) obj[10];
							String mobile_no = (String) obj[11];
							String entry_date = (String) obj[12];
							Integer status = (Integer) obj[13];
							Integer userstatus = (Integer) obj[14];
							String table_name = (String) obj[16];
							Integer lvrtotalcnt = (Integer) obj[15];
							nn= null;
							 nn = new JSONObject();
							 if(Commonutility.checkempty(lvrEeventName)) {
								 lvrCntent += " <br/> <div class=\"grey-text text-darken-4 left\"> Name :&nbsp;</div> <div class=\"left \"> "+lvrEeventName+"</div>";
							 }
							 if(Commonutility.checkempty(lvrTitle)) {
								 lvrCntent += " <br/> <div class=\"grey-text text-darken-4 left\"> Title :&nbsp;</div> <div class=\"left \"> "+lvrTitle+"</div>";
							 }
							 if(Commonutility.checkempty(lvrSUBTITLE)) {
								 lvrCntent += " <br/> <div class=\"grey-text text-darken-4 left\"> Sub Title :&nbsp;</div> <div class=\"left \"> "+lvrSUBTITLE+"</div>";
							 }
							 if(Commonutility.checkempty(lvrSHORTDESCRIPTION)) {
								 lvrCntent += " <br/> <div class=\"grey-text text-darken-4 left\"> Short Desc. :&nbsp;</div> <div class=\"left \"> "+lvrSHORTDESCRIPTION+"</div>";
							 }
							 if(Commonutility.checkempty(lvrDESCRIPTION)) {
								 lvrCntent += " <br/> <div class=\"grey-text text-darken-4 left\"> Desc. :&nbsp;</div> <div class=\"left \"> "+lvrDESCRIPTION+"</div>";
							 }
							 if(Commonutility.checkempty(lvrMESSAGES)) {
								 lvrCntent += " <br/> <div class=\"grey-text text-darken-4 left\"> Desc. :&nbsp;</div> <div class=\"left \"> "+lvrMESSAGES+"</div>";
							 }
							 nn.put("unique_id", Commonutility.toCheckNullEmpty(unique_id));
							nn.put("attach_id", Commonutility.toCheckNullEmpty(attach_id));
							nn.put("content", Commonutility.toCheckNullEmpty(lvrCntent));
							nn.put("usr_id", Commonutility.toCheckNullEmpty(usr_id));
							nn.put("fname", Commonutility.toCheckNullEmpty(fname));
							nn.put("lname", Commonutility.toCheckNullEmpty(lname));
							nn.put("mobile_no", Commonutility.toCheckNullEmpty(mobile_no));
							entry_date = Commonutility.tochangeonefrmtoanother("yyyy-MM-dd HH:mm:ss","dd-MM-yyyy HH:mm:ss",entry_date);
							nn.put("entry_datetime", Commonutility.toCheckNullEmpty(entry_date));														
							nn.put("status", Commonutility.toCheckNullEmpty(status));
							nn.put("userstatus", Commonutility.toCheckNullEmpty(userstatus));
							nn.put("mvp_feeds_tbl", Commonutility.toCheckNullEmpty(table_name));	
						//	nn.put("totalcount", Commonutility.toCheckNullEmpty(lvrtotalcnt));	
							CONTmontMstList.put(nn);
							countFilter = lvrtotalcnt;
							count = lvrtotalcnt;
						}			
					}
			
			
			
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("feeddetails", CONTmontMstList);			
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
