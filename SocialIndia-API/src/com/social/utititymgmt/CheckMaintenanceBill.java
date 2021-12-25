package com.social.utititymgmt;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class CheckMaintenanceBill extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;	
	private String ivrservicefor;
	public String execute() {
	Log log = new Log();
	JSONObject locObjRecvJson = null;// Receive String to json
	JSONObject locObjRecvdataJson = null;// Receive Data Json
	JSONObject locObjRspdataJson = null;// Response Data Json
	//String lsvSlQry = null;
	Session locObjsession = null;
	String ivrservicecode = null;
	String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
	String ivrCurntusridstr=null;
	String iswebmobilefla=null;
	try {
		CommonMobiDao commonServ = new CommonMobiDaoService();
		locObjsession = HibernateUtil.getSession();
		if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {					
				locObjRecvJson = new JSONObject(ivrparams);
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
				ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"currentloginid");
				if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
				}else{ }
				
				if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
					ivrDecissBlkflag = ivrservicefor;
				}else{//post
					ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
				}
				
				locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
				String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
				String locvrFnrst=null;
				String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;	
				Common lvrEvntDobj=null;
				locObjRspdataJson=new JSONObject();		
				 if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){//select single booking detail		
					 lvrEvntDobj = new CommonDao();
					 String lvrevntcountqry="select count(maintenanceId) from MaintenanceFeeTblVO where statusFlag=1 and pdfstatus='p'";
					 int count = lvrEvntDobj.gettotalcount(lvrevntcountqry);
					 String countFilter=String.valueOf(count);
					 if(countFilter==null || countFilter.equalsIgnoreCase("null") ){
						 countFilter="0";
					 }
					 locObjRspdataJson.put("countFilter", countFilter);
					 serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson,countFilter);
				
				}else{
					locvrFnrst="";
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI3001, "+getText("service.notmach"), "info", CheckMaintenanceBill.class);
					locRtnSrvId="SI3001";locRtnStsCd="1"; locRtnRspCode="SE3001"; locRtnMsg=getText("service.notmach");						
				}					
				serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson,iswebmobilefla);
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3001,"+getText("request.format.notmach")+"", "info", CheckMaintenanceBill.class);
				serverResponse("SI3001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);
			}
		} else {
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3001,"+getText("request.values.empty")+"", "info", CheckMaintenanceBill.class);
			serverResponse("SI3001","1","ER0001",getText("request.values.empty"),locObjRspdataJson,iswebmobilefla);
		}
	} catch (Exception e) {
		System.out.println("Exception found CheckMaintenanceBill.class execute() Method : " + e);
		locObjRspdataJson=new JSONObject();
		log.logMessage("Service code : SI3001, Sorry, an unhandled error occurred", "error", CheckMaintenanceBill.class);
		serverResponse("SI3001","2","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
	} finally {
		if (locObjsession != null) {locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession = null;}
		locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
	}
	return SUCCESS;
}
private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla)
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
public String getIvrservicefor() {
	return ivrservicefor;
}
public void setIvrservicefor(String ivrservicefor) {
	this.ivrservicefor = ivrservicefor;
}

}
