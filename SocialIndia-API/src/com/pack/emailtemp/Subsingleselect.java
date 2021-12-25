package com.pack.emailtemp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.emailtempvo.EmailParsereditTbl;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.sisocial.load.HibernateUtil;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.Log;

public class Subsingleselect extends ActionSupport{
private static final long serialVersionUID = 1L;
private String ivrparams;
List<EmailTemplateTblVo> emailsubject = new ArrayList<EmailTemplateTblVo>();
EmailTemplateTblVo email=new EmailTemplateTblVo();
Log log=new Log();
public String execute(){
	 Log logWrite = null;
	JSONObject locObjRecvJson = null;//Receive String to json	
	JSONObject locObjRecvdataJson = null;// Receive Data Json		
	//	Session locObjsession = null;		
	String ivrservicecode=null;
	EmailParsereditTbl locemailtblobj = null;
	Session locHbsession = null;
	Query lvrQrybj = null;
	JSONObject locRtndatajson = null;
	JSONObject locObjRspdataJson=null;
	try{
		locHbsession = HibernateUtil.getSession();
		locRtndatajson =  new JSONObject();
		if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {
			locObjRecvJson = new JSONObject(ivrparams);
			ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
			locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");							
			String uniqTownShipIdEdit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "templateid");
			String locVrSlQry="";
			locVrSlQry = "from EmailParsereditTbl where template_id = "+Integer.parseInt(uniqTownShipIdEdit);
			lvrQrybj = locHbsession.createQuery(locVrSlQry);
			locemailtblobj = (EmailParsereditTbl)lvrQrybj.uniqueResult();
			locRtndatajson.put("emailsub",Commonutility.toCheckNullEmpty(locemailtblobj.getTemplate_id().getSubject()));
			locRtndatajson.put("emailcontent",Commonutility.toCheckNullEmpty(locemailtblobj.getTemplate_id().getTempContent()));
			locRtndatajson.put("emailcolum",Commonutility.toCheckNullEmpty(locemailtblobj.getTEMP_PAR()));
			locObjRspdataJson=new JSONObject();
			locObjRspdataJson.put("datalst", locRtndatajson);
			serverResponse(ivrservicecode,"0","000","success",locRtndatajson);
			}else{
				serverResponse(ivrservicecode,"1","E0001","database error occured",locRtndatajson);
			}
				
			}else{
				//Response call
				serverResponse(ivrservicecode,"1","E0001","Request values are not correct format",locRtndatajson);
			}	
	}catch(Exception ex){
		System.out.println(Subsingleselect.class +"ex:::: "+ex);
		 locRtndatajson=new JSONObject();
	      logWrite.logMessage("Service code : SI300001, Sorry, an unhandled error occurred : "+ex, "error", Subsingleselect.class);
		  try {
			serverResponse("SI300001","2","ER0002",getText("catch.error"),locObjRspdataJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}finally{
		locObjRecvJson = null;//Receive String to json	
		locObjRecvdataJson = null;// Receive Data Json		
		//		Session locObjsession = null;		
		ivrservicecode=null;
		locemailtblobj = null;
		locHbsession = null;
		lvrQrybj = null;
		locRtndatajson = null;
		locObjRspdataJson=null;
	}
	
	return SUCCESS;
	
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
