package com.social.login;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.eventvo.persistence.EventDao;
import com.pack.eventvo.persistence.EventDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class UserOfflineUpdate extends ActionSupport {
	private String ivrparams;
	private static final long serialVersionUID = 1L;
	public String execute() {
		
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrusroffline=null,ivrcurrntusridStr=null;
		Log log = new Log();
		JSONObject json = new JSONObject();
		Session locObjsession = null;	
		try{	
			locObjsession = HibernateUtil.getSession();
			System.out.println("ivrparams   "+ivrparams);
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {	
				locObjRecvJson = new JSONObject(ivrparams);
				String servicecode = (String) Commonutility.toHasChkJsonRtnValObj(json, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
				System.out.println("locObjRecvdataJson   "+locObjRecvdataJson);
				ivrusroffline =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "offlinestatus");
				String locvrFnrst=null;
				locvrFnrst=userStatuschange(locObjRecvdataJson,locObjsession);
			//result=uam.updateUser();	
			}
			else
			{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3001,"+getText("request.format.notmach")+"", "info", UserOfflineUpdate.class);
				serverResponse("SI3001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
			}
			}
			else
			{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3001,"+getText("request.values.empty")+"", "info", UserOfflineUpdate.class);
				serverResponse("SI3001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			System.out.println("=======userUpdate====Exception====" + ex);
		}
		finally {
			if (locObjsession != null) {locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession = null;}
			locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
		}
		return SUCCESS;
	}
	public static String userStatuschange(JSONObject pDataJson, Session pSession) {
		ResourceBundle ivrRbuilder = null;
		String locvrLBR_ID=null;
		String locrestUpdqry=null;		
		boolean locrestUpdSts=false;
		EventDao locrestObj=null;
		Log log=null;
		try {			
			ivrRbuilder = ResourceBundle.getBundle("aduit");
			log=new Log();
			locrestObj = new EventDaoservice();
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");
			System.out.println("userStatuschange  locvrLBR_ID = "+locvrLBR_ID);
			Commonutility.toWriteConsole("userStatuschange  locvrLBR_ID = "+locvrLBR_ID);
			String flag  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "flag");
			System.out.println("userStatuschange  flag = "+flag);
			Commonutility.toWriteConsole("userStatuschange  flag = "+flag);
			
			AuditTrial.toWriteAudit(ivrRbuilder.getString("LG0002"), "LG0002", Commonutility.stringToInteger(locvrLBR_ID));
			if(Commonutility.checkempty(locvrLBR_ID)){
			locrestUpdqry ="update UserMasterTblVo set ivrBnISONLINEFLG=0, loggedOut=1 where userId ="+locvrLBR_ID+"";
			log.logMessage("Updqry : "+locrestUpdqry, "info", UserOfflineUpdate.class);
			locrestUpdSts=locrestObj.toDeactiveEvent(locrestUpdqry);
			
			
			if(!flag.equalsIgnoreCase("true")){
				locrestUpdqry ="update UserMasterTblVo set currentLogins = currentLogins - 1 where currentLogins > 0 and userId ="+locvrLBR_ID+"";
				log.logMessage("Updqry : "+locrestUpdqry, "info", UserOfflineUpdate.class);
				locrestUpdSts=locrestObj.toDeactiveEvent(locrestUpdqry);
			}
			
			 if(locrestUpdSts){
				
				 return "success";
			 }else{
				 return "error";
			 }	
			}else {
				 return "error";
			}
		} catch (Exception e) {
			System.out.println("Exception found in UserOfflineUpdate.toDeActResident : "+e);
			log.logMessage("Exception : "+e, "error", UserOfflineUpdate.class);
			return "error";
		} finally {
			 log=null; locrestObj=null;
			 locvrLBR_ID=null; 	
			 locrestUpdqry=null;locrestUpdSts=false;	ivrRbuilder =null;			
		}
	}
	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
			throws Exception {
		PrintWriter out;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response;
		response = ServletActionContext.getResponse();
		out = response.getWriter();
		try {
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
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}
