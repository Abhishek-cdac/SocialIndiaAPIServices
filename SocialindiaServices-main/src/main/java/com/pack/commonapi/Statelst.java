package com.pack.commonapi;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.socialmediaa.twitterAction;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Statelst extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private String ivrcontryid;
	Log log=new Log();	
	public String execute(){
		JSONObject locObjRecvJson=null;	
		JSONObject locObjdataJson =null;		
		String locVrSlQry=null;
		Session locObjsession=null;
		List<Object> locObjStatlst=null;
		try{
			locObjsession = HibernateUtil.getSession();						
			//boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){	
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
				ivrcontryid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "countryid");
				locVrSlQry="select  stateId, stateName from StateMasterTblVO where countryId="+ivrcontryid+"";
				System.out.println("Select Query : "+locVrSlQry);
				locObjStatlst=locObjsession.createQuery(locVrSlQry).list();
				locObjdataJson=new JSONObject();
				locObjdataJson.put("datalst", locObjStatlst);				
				serverResponse(ivrservicecode,"0","0000","success",locObjdataJson);
			}else{
				locObjdataJson=new JSONObject();				
				serverResponse("SI1002","1","E0001","Request values not not correct format",locObjdataJson);
			}
			}else{
				locObjdataJson=new JSONObject();
				log.logMessage("Service code : SI2002, Request values are not correct format", "info", Statelst.class);
				serverResponse("SI2002","1","E0001","Request values are not correct format",locObjdataJson);
			}
		}catch(Exception e){
			System.out.println("Exception found in Statelst.action execute() Method : "+e);
			locObjdataJson=new JSONObject();			
			serverResponse("SI1002","2","E0002","Sorry, an unhandled error occurred",locObjdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;			 
			locObjdataJson =null;locObjStatlst=null;
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
