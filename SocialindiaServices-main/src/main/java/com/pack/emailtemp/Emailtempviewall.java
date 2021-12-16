package com.pack.emailtemp;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Emailtempviewall extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  private String ivrparams;
	  Log log=new Log();
	 public String execute() {
		    Log logWrite = null;
			JSONObject locObjRecvJson = null;//Receive over all Json	[String Received]
			JSONObject locObjRspdataJson = null;// Response Data Json
			Session locObjsession = null;		
			String ivrservicecode=null;
			List<Object> locObjCompylst = null;
			String lvSlqry="";
			try {
				locObjsession = HibernateUtil.getSession();
				if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
					ivrparams = EncDecrypt.decrypt(ivrparams);
					boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
					if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					lvSlqry="select tempid,tempName from EmailTemplateTblVo where status='1'";				
					locObjCompylst=locObjsession.createQuery(lvSlqry).list();
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("datalst", locObjCompylst);				
					serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI300001, Request values are not correct format", "info", Emailtempviewall.class);
						serverResponse("SI300001","1","E0001","Request values are not correct format",locObjRspdataJson);
					}	
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6432, Request values are empty", "info", Emailtempviewall.class);
					serverResponse("SI300001","1","E0001","Request values are empty",locObjRspdataJson);
				}	
			} catch (Exception expObj) {  
				System.out.println(Emailtempviewall.class +"Exception ----- "+expObj);
		      locObjRspdataJson=new JSONObject();
		      logWrite.logMessage("Service code : SI300001, Sorry, an unhandled error occurred : "+expObj, "error", Emailtempviewall.class);
			  serverResponse("SI300001","2","ER0002",getText("catch.error"),locObjRspdataJson);
			} finally {
				if (locObjsession!=null) {locObjsession.flush();locObjsession.clear();locObjsession.close(); locObjsession = null;}
				logWrite = null;
				locObjRecvJson = null;//Receive over all Json	[String Received]
				locObjRspdataJson = null;// Response Data Json	
				ivrservicecode=null;
				locObjsession = null;
			}	 
			return SUCCESS;
		  }
	 
	
	  private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
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
