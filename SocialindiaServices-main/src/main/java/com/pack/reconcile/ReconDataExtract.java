package com.pack.reconcile;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;

public class ReconDataExtract extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	public String execute(){
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
/*		Session session = null;*/	
		String ivrservicecode=null;
		String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
		try{
			/*session = HibernateUtil.getSessionFactory().openSession();*/
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					String userId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "crntusrloginid");				
						if((userId!=null && !userId.equalsIgnoreCase("null") && !userId.equalsIgnoreCase("")) ){
							PaygateReconcile reconicile=new PaygateReconcile();
							String returns=reconicile.toCallReconcileFiles(userId);
							if(returns.equalsIgnoreCase("success")){
								System.out.println("returns ---------- "+returns);
								locObjRspdataJson=new JSONObject();	
								locObjRspdataJson.put("status", "SUCCESS");
								locObjRspdataJson.put("message", "Successfully Uploaded");
								locRtnSrvId="SI40000";locRtnStsCd="00"; locRtnRspCode="R0084";locRtnMsg="";	
								serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
							}else{
								locObjRspdataJson=new JSONObject();	
								locObjRspdataJson.put("status", "Error");
								locObjRspdataJson.put("message", "Error");
								locRtnSrvId="SI40000";locRtnStsCd="11"; locRtnRspCode="R0084";locRtnMsg="";	
								serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
							}
							
						}else{
							locObjRspdataJson=new JSONObject();
							serverResponse("SI40000","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
						}
					
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse("SI40000","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				serverResponse("SI40000","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
			
		}catch (Exception ex){
			serverResponse("SI40000", "02", "R0087", mobiCommon.getMsg("R0087"), locObjRspdataJson);
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

