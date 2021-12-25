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

public class SkillMasterlst extends	ActionSupport{

	static final long serialVersionUID = 1L;

	private String ivrparams;

	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;
		String ivrservicecode=null;
		String ivrcategoryid=null;
		List<Object> locObjSkilllst = null;
		try{
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrcategoryid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "categoryid");
				if(ivrcategoryid!=null && ivrcategoryid.equalsIgnoreCase("gate_pass"))	//gatepass loading skills
				{
					lsvSlQry="select ivrBnSKILL_ID,ivrBnSKILL_NAME from SkillMasterTblVO where ivrBnACT_STAT='1'";
				}
				else{
				lsvSlQry="select ivrBnSKILL_ID,ivrBnSKILL_NAME from SkillMasterTblVO where ivrBnACT_STAT='1' and ivrBnCategoryid='"+ivrcategoryid+"'";
				}
				System.out.println("Select Query : "+lsvSlQry);
				locObjSkilllst=locObjsession.createQuery(lsvSlQry).list();
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("datalst", locObjSkilllst);
				serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);

				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI1009, Request values are not correct format", "info", SkillMasterlst.class);
					serverResponse("SI1009","1","E0001","Request values are not correct format",locObjRspdataJson);
				}
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI1009, Request values are empty", "info", SkillMasterlst.class);
				serverResponse("SI1009","1","E0001","Request values are empty",locObjRspdataJson);
			}
		}catch(Exception e){
			System.out.println("Exception found SkillMasterlst.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI1009, Sorry, an unhandled error occurred", "error", SkillMasterlst.class);
			serverResponse("SI1009","2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.close();locObjsession=null;}
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;
		}
		return SUCCESS;
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
