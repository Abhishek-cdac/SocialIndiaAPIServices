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
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ImagemonitoringViewall extends ActionSupport {

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
					log.logMessage("Service code : SI10008, "+getText("request.format.notmach"), "info", ImagemonitoringViewall.class);
					serverResponse("SI10008","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI10008, "+getText("request.values.empty"), "info", ImagemonitoringViewall.class);
				serverResponse("SI10008","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI10008, "+getText("catch.error"), "error", ImagemonitoringViewall.class);
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
		/*pSession = HibernateUtil.getSessionFactory().openSession();*/
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrEdu_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null, data=null,sqlQuery=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		FeedattchTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0;
		Integer startrowfrom=1, totalNorow=10;
		Query queryObj = null;
		 JSONArray IMGmontMstList = null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();	lbrDtlVoObj=new FeedattchTblVO();			
			System.out.println("Step 1 : image monitoring  select all block enter");
			log.logMessage("Step 1 : image monitoring  select all block enter", "info", ImagemonitoringViewall.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
						
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				startrowfrom = Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow = Integer.parseInt(locMaxrow);
			}
			
			
			
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry, "info", ImagemonitoringViewall.class);				
				 count=0;countFilter=0;
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", ImagemonitoringViewall.class);
			}
						
			System.out.println("Step 3 : image monitoring  Details Procedure will start");
			log.logMessage("Step 3 : image monitoring  Details Procedure will start", "info", ImagemonitoringViewall.class);			
			locLBRJSONAryobj = new JSONArray();		
			IMGmontMstList = new JSONArray();				
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				queryObj = pSession
						.createSQLQuery("CALL PR_IMAGE_MONITORING_FILTER (:STARTROW,:TOTROW,:SEARCHVAL)")
						.addScalar("ID", Hibernate.INTEGER)
						.addScalar("ATTACH_ID", Hibernate.INTEGER)
						.addScalar("ATTACH_NAME", Hibernate.TEXT)
						.addScalar("THUMP_IMAGE", Hibernate.TEXT)
						.addScalar("ATTACH_TYP", Hibernate.INTEGER)
						.addScalar("USR_ID", Hibernate.INTEGER)
						.addScalar("FNAME", Hibernate.TEXT)
						.addScalar("LNAME", Hibernate.TEXT)
						.addScalar("MOBILE_NO", Hibernate.TEXT)
						.addScalar("ENTRY_DATE", Hibernate.TEXT)
						.addScalar("STATUS", Hibernate.INTEGER)
						.addScalar("USER_STATUS", Hibernate.INTEGER)
						.addScalar("NO_OF_RECORD", Hibernate.INTEGER)
						.addScalar("MASTER_ID", Hibernate.INTEGER) //  master relevent
						.addScalar("TABLE_NAME", Hibernate.TEXT);							
				queryObj.setInteger("STARTROW", startrowfrom);
				queryObj.setInteger("TOTROW", totalNorow);
				queryObj.setString("SEARCHVAL", loctocheNull);
			} else{
				queryObj = pSession
						.createSQLQuery("CALL PR_IMAGE_MONITORING (:STARTROW,:TOTROW)")
						.addScalar("ID", Hibernate.INTEGER)
						.addScalar("ATTACH_ID", Hibernate.INTEGER) // Autoincrement id
						.addScalar("ATTACH_NAME", Hibernate.TEXT)
						.addScalar("THUMP_IMAGE", Hibernate.TEXT)
						.addScalar("ATTACH_TYP", Hibernate.INTEGER)
						.addScalar("USR_ID", Hibernate.INTEGER)
						.addScalar("FNAME", Hibernate.TEXT)
						.addScalar("LNAME", Hibernate.TEXT)
						.addScalar("MOBILE_NO", Hibernate.TEXT)
						.addScalar("ENTRY_DATE", Hibernate.TEXT)
						.addScalar("STATUS", Hibernate.INTEGER)
						.addScalar("USER_STATUS", Hibernate.INTEGER)
						.addScalar("NO_OF_RECORD", Hibernate.INTEGER)
						.addScalar("MASTER_ID", Hibernate.INTEGER) //  master relevent
						.addScalar("TABLE_NAME", Hibernate.TEXT);							
				queryObj.setInteger("STARTROW", startrowfrom);
				queryObj.setInteger("TOTROW", totalNorow);
			}						
			List result = queryObj.list();	
			System.out.println("---------------------------1");
			System.out.println("---------------------------1ENGHT" + result.size());
			for (int i = 0; i < result.size(); i++) {
				JSONObject nn = null;
				Object[] obj = (Object[]) result.get(i);
				if (obj != null && obj.length >= 11) {
					Integer unique_id = (Integer) obj[0];
					Integer attach_id = (Integer) obj[1];
					String attach_name = (String) obj[2];
					String thump_image = (String) obj[3];
					Integer attach_typ = (Integer) obj[4];
					Integer usr_id = (Integer) obj[5];
					String  fname = (String) obj[6];
					String  lname = (String) obj[7];
					String mobile_no = (String) obj[8];
					String entry_date = (String) obj[9];
					Integer status = (Integer) obj[10];
					Integer userstatus = (Integer) obj[11];
					Integer lvrtotalcnt = (Integer) obj[12];
					Integer lvrMastertblid = (Integer) obj[13];
					String table_name = (String) obj[14];
					nn= null;
					 nn = new JSONObject();
					nn.put("unique_id", Commonutility.toCheckNullEmpty(unique_id));
					nn.put("feed_id", Commonutility.toCheckNullEmpty(attach_id));
					nn.put("attachname", Commonutility.toCheckNullEmpty(attach_name));
					nn.put("thump_image", Commonutility.toCheckNullEmpty(thump_image));
					nn.put("file_type", Commonutility.toCheckNullEmpty(attach_typ));
					nn.put("usr_id", Commonutility.toCheckNullEmpty(usr_id));
					nn.put("fname", Commonutility.toCheckNullEmpty(fname));
					nn.put("lname", Commonutility.toCheckNullEmpty(lname));
					nn.put("mobile_no", Commonutility.toCheckNullEmpty(mobile_no));
					entry_date = Commonutility.tochangeonefrmtoanother("yyyy-MM-dd HH:mm:ss","dd-MM-yyyy HH:mm:ss",entry_date);
					nn.put("entry_datetime", Commonutility.toCheckNullEmpty(entry_date));
					nn.put("status", Commonutility.toCheckNullEmpty(status));
					nn.put("userstatus", Commonutility.toCheckNullEmpty(userstatus));
					nn.put("mastertblid", Commonutility.toCheckNullEmpty(lvrMastertblid));
					nn.put("mvp_feed_attach_tbl", Commonutility.toCheckNullEmpty(table_name));								
					IMGmontMstList.put(nn);
					count = lvrtotalcnt;
					countFilter = lvrtotalcnt;
				}			
			}					
			//IMGmontMstList = imagedao.executeimagemonitorProcedure(startrowfrom, totalNorow);
			System.out.println("IMGmontMstList--------------------1477---------"+IMGmontMstList);									
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("imgmontdetails", IMGmontMstList);			
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
