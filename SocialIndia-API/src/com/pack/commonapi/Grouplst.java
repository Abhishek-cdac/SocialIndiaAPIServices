package com.pack.commonapi;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Grouplst extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;

	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;	
		Session locObjsession = null;		
		String ivrservicecode=null;
		UserMasterTblVo userData=new UserMasterTblVo();
		uamDao uam=new uamDaoServices();
		List<Object> locObjGroupgorylst = null;
		try{
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson=(JSONObject)Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String UniqueJson = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"grpid");
				int unqueid=Integer.parseInt(UniqueJson);
				lsvSlQry="select groupCode,groupName from GroupMasterTblVo where statusFlag=1 and groupName not in('Township','Labor','Merchant','Admin')";
				System.out.println("Select Query : "+lsvSlQry);
				locObjGroupgorylst=locObjsession.createQuery(lsvSlQry).list();
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("datalst", locObjGroupgorylst);			
				String groupCodeid="";
				userData=uam.editUser(unqueid);
				if(userData.getGroupCode()!=null){
					Integer groupCode=userData.getGroupCode().getGroupCode();
					groupCodeid=String.valueOf(groupCode);
					locObjRspdataJson.put("groupCode", groupCodeid);
				}else{
					groupCodeid="";
					locObjRspdataJson.put("groupCode", groupCodeid);
				}
				serverResponse("groupCode", groupCodeid,"0000","success",locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI1008, Request values are not correct format", "info", Grouplst.class);
					serverResponse("SI1008","1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI1008, Request values are empty", "info", Grouplst.class);
				serverResponse("SI1008","1","E0001","Request values are empty",locObjRspdataJson);
			}	
		}catch(Exception e){
			System.out.println("Exception found Grouplst.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI1008, Sorry, an unhandled error occurred", "error", Grouplst.class);
			serverResponse("SI1008","2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRspdataJson=null;	//locObjRecvdataJson =null;
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
