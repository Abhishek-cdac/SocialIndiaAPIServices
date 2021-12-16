package com.pack.commonapi;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrialvo.AuditlogTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class CyberPlatbalance extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ivrparams;
	uamDao uam=null;
	JSONObject jsonFinalObj=new JSONObject();
	Log log=new Log();	
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		String ivrservicecode=null;String locVrSlQry="";int count=0;
		JSONObject locInrJSONObj=null;
		String cyberplatesd=null;
		String cyberplatap=null;
		String cyberplatop=null;
		Common common=null;
		try{
			common=new CommonDao();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				if(ivrservicecode!="" && ivrservicecode!=null && !ivrservicecode.equalsIgnoreCase("")){
					cyberplatesd="select extranalValue from MvpExternalOperatorConfigTbl where extranalKey='"+getText("extranalKey.cyberplate.SD")+"'";
					String cyberplatesdval=common.getcyberval(cyberplatesd);
					
					cyberplatap="select extranalValue from MvpExternalOperatorConfigTbl where extranalKey='"+getText("extranalKey.cyberplate.AP")+"'";
					String cyberplatapval=common.getcyberval(cyberplatap);
				
					cyberplatop="select extranalValue from MvpExternalOperatorConfigTbl where extranalKey='"+getText("extranalKey.cyberplate.OP")+"'";
					String cyberplatopval=common.getcyberval(cyberplatop);
					System.out.println(cyberplatopval+"---------"+cyberplatapval+"--------- "+cyberplatesdval);
					
					
					locInrJSONObj=new JSONObject();
					locInrJSONObj.put("cyberplatesd", cyberplatesdval);
					locInrJSONObj.put("cyberplatap", cyberplatapval);
					locInrJSONObj.put("cyberplatop", cyberplatopval);
					serverResponse(ivrservicecode, "0000","0", "sucess", locInrJSONObj);		
				}
				
				}
			}
		}catch(Exception ex){
			System.out.println("Exception found .class execute() Method : "+ex);
			log.logMessage("Service : userMgmtAction error occurred : " + ex, "error",CommonDashboardqry.class);
			try {
				serverResponse(ivrservicecode,"2","E0002","Sorry, an unhandled error occurred",locObjRecvJson);
		}catch (Exception e1) {}
	}finally{
		locObjRecvJson = null;	
		ivrservicecode=null;locVrSlQry="";count=0;
		locInrJSONObj=null;
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
			log.logMessage("Service : userMgmtAction error occurred : " + ex, "error",CommonDashboardqry.class);
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
