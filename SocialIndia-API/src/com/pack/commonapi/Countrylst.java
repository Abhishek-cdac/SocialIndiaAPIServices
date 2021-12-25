package com.pack.commonapi;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Countrylst extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;	
	Log log=new Log();			
	public String execute() throws Exception {
		System.out.println("country list services:::::");
		JSONObject locObjRecvJson=null;		
		JSONObject locObjdataJson =null;		
		String locVrslQry=null;
		Session locObjsession=null;
		List<Object> locObjcntrylist=null;
		try {
			locObjsession = HibernateUtil.getSession();	
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){	
				ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {				
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode=(String)Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				System.out.println();
				locVrslQry="select  countryId, countryName from CountryMasterTblVO";
				System.out.println("Select Query : "+locVrslQry);
				locObjcntrylist=locObjsession.createQuery(locVrslQry).list();
				locObjdataJson=new JSONObject();
				locObjdataJson.put("datalst", locObjcntrylist);				
				serverResponse(ivrservicecode,"0","0000","success",locObjdataJson);							
			} else {
				locObjdataJson=new JSONObject();
				serverResponse("SI1001","1","E0001","Request values not not correct format",locObjdataJson);
			}
			}
			else{
				locObjdataJson=new JSONObject();
				log.logMessage("Service code : SI1001, Request values are not correct format", "info", Countrylst.class);
				serverResponse("SI2002","1","E0001","Request values are not correct format",locObjdataJson);
			}
		} catch (Exception e) {
			locObjdataJson=new JSONObject();
			System.out.println("Exception found in Countrylst.action : " + e);
			serverResponse("SI1001","2","E0002","Sorry, an unhandled error occurred",locObjdataJson);
		} finally {
			if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;			 
			locObjdataJson =null;locObjcntrylist=null;
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

}
