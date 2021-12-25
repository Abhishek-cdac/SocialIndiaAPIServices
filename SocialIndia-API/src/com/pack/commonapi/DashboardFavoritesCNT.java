package com.pack.commonapi;



import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.Notificationlistvo.persistance.NotificationDao;
import com.pack.Notificationlistvo.persistance.NotificationDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class DashboardFavoritesCNT extends ActionSupport {

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
		//Session locObjsession = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		try{
			//locObjsession = HibernateUtil.getSession();
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
				locObjRspdataJson=toDashboardFavoritesCNT(locObjRecvdataJson);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						//AuditTrial.toWriteAudit(getText("NOTFYAD014"), "NOTFYAD014", ivrCurrntusrid);
						serverResponse("SI34000","0","E34000",getText("Favorites countall error "),locObjRspdataJson);
					}else{
					//	AuditTrial.toWriteAudit(getText("NOTFYAD013"), "NOTFYAD013", ivrCurrntusrid);
						serverResponse("SI34000","0","S34000",getText("Favorites countall success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI34000, "+getText("request.format.notmach"), "info", DashboardFavoritesCNT.class);
					serverResponse("SI34000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI34000, "+getText("request.values.empty"), "info", DashboardFavoritesCNT.class);
				serverResponse("SI34000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI34000, "+getText("catch.error"), "error", DashboardFavoritesCNT.class);
			serverResponse("SI34000","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject toDashboardFavoritesCNT(JSONObject pDataJson) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		String locvrLBR_Usrid=null,locvrCntflg=null,locvrFlterflg=null,locvrNotify_cntQry=null;
			Date entrydatetime=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		TownshipMstTbl lbrDtlVoObj=null;	
		CommonUtils locCommutillObj =null;
		Session locObjsession = null;
		int count=0;
		try {
			locObjsession = HibernateUtil.getSession();
			log=new Log();	lbrDtlVoObj=new TownshipMstTbl();		
			locCommutillObj = new CommonUtilsDao();
			DateFormat time = new SimpleDateFormat("hh:mm");
			 DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			Commonutility.toWriteConsole("Step 1 :Fav count Type  select all block enter");
			log.logMessage("Step 1 :Fav count Type  select all block enter", "info", DashboardFavoritesCNT.class);			
			locvrLBR_Usrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			//locvrNotify_cntQry="select count(townshipId) from TownshipMstTbl where status =1";
			locvrNotify_cntQry="SELECT CONCAT_WS('!_!',T.NO_OF_TOWNSHIP,T.NO_OF_COMMITTEE_MEMBERS,NO_OF_RESIDENT_MEMBERS,NO_OF_SOCIETY,NO_OF_COMPANY,NO_OF_STAFF,NO_OF_LABOUR,NO_OF_MERCHANT) FROM (SELECT (SELECT COUNT(TOWNSHIP_ID) FROM TOWNSHIP_MST_TBL WHERE STATUS=1) NO_OF_TOWNSHIP,(SELECT  COUNT(USR_ID) FROM USR_REG_TBL  WHERE ACT_STS=1 AND GROUP_ID=5) NO_OF_COMMITTEE_MEMBERS,(SELECT  COUNT(USR_ID) FROM USR_REG_TBL  WHERE ACT_STS=1 AND GROUP_ID=6) NO_OF_RESIDENT_MEMBERS,(SELECT  COUNT(SOCIETY_ID) FROM SOCIETY_MST_TBL  WHERE STATUS=1)  NO_OF_SOCIETY,(SELECT  COUNT(COMPANY_ID) FROM company_mst_tbl  WHERE STATUS=1)  NO_OF_COMPANY,(SELECT  COUNT(STAFF_ID) FROM mvp_staff_prof_tbl  WHERE STATUS=1)  NO_OF_STAFF,(SELECT  COUNT( LBR_ID) FROM mvp_lbr_reg_tbl  WHERE LBR_STS=1)  NO_OF_LABOUR,(SELECT  COUNT(MRCHNT_ID) FROM mvp_mrch_tbl  WHERE MRCHNT_ACT_STS=1)  NO_OF_MERCHANT FROM DUAL)T";
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrNotify_cntQry, "info", DashboardFavoritesCNT.class);
				
				NotificationDao FavCNTDaoObj=new NotificationDaoservice();
			String	countval=FavCNTDaoObj.getInitSQLTotal(locvrNotify_cntQry);
			System.out.println("count:*******!!!*******   "+countval);
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("count", countval);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", DashboardFavoritesCNT.class);	
			
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			if(locObjsession!=null){
				locObjsession.clear();locObjsession.close();locObjsession =null;
			}
	
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
			locvrLBR_Usrid=null;locCommutillObj=null;
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
