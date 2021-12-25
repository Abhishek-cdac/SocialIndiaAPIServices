package com.pack.function;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Eventfacility extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	public String execute(){

		Log log= null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json			
		String ivrservicecode=null, ivrcurrntusridobj = null;
		Integer ivrCurrntusrid = null;
		try{
			log= new Log();
			
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrcurrntusridobj =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				if(ivrcurrntusridobj!=null && Commonutility.toCheckisNumeric(ivrcurrntusridobj)){
					ivrCurrntusrid= Integer.parseInt(ivrcurrntusridobj);
				}else{ ivrCurrntusrid=0; }
				
				locObjRspdataJson = togetallfunction(locObjRecvdataJson, ivrCurrntusrid);
				System.out.println("locObjRspdataJson ----------- "+locObjRspdataJson);
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						serverResponse("SI5000","0","E5000","error",locObjRspdataJson);
					}else{
						serverResponse("SI5000","0","S5000","success",locObjRspdataJson);					
					}

				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI5000,"+getText("request.format.notmach")+"", "info", Eventfacility.class);
					serverResponse("SI5000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI5000,"+getText("request.values.empty")+"", "info", Eventfacility.class);
				serverResponse("SI5000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Step -1 : Select Function data process [Exception] : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI5000, Sorry, an unhandled error occurred", "error", Eventfacility.class);
			serverResponse("SI5000","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
				
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	
		
	}
	private JSONObject togetallfunction(JSONObject prmDatajson, int ivrCurrntusrid){
		JSONObject lvrRtnjson = null;
		Session locObjsession = null;	
		Log log = null;
		String lvrSql = null;
		List<Object> locObjfunctionlst = null;
		otpDao usrDao = null;
		try{
			log = new Log();
			locObjsession = HibernateUtil.getSession();
			//Commonutility.toWriteConsole("Step 1 : Select Function data process [start].");
			//log.logMessage("Step 1 : Select Feedback process start.", "info", Feedbackviewall.class);	
			
			Commonutility.toWriteConsole(" Facility list by socytea ivrCurrntusrid : "+ivrCurrntusrid);
			
			usrDao = new otpDaoService();
			String societyKey = usrDao.checkUserDetails(ivrCurrntusrid+"").getSocietyId().getActivationKey();
			Commonutility.toWriteConsole(" Facility list by socytea societyKey : "+societyKey);
			
			lvrSql = "select facilityId,facilityName, place from FacilityMstTblVO where statusFlag = 1 and societyKey = '" + societyKey + "'";
			
			Commonutility.toWriteConsole(" Facility list by socytea sql : "+lvrSql);			
			
			locObjfunctionlst = locObjsession.createQuery(lvrSql).list();
			lvrRtnjson=new JSONObject();
			lvrRtnjson.put("datalst", locObjfunctionlst);
		} catch(Exception e){			
			try {lvrRtnjson=new JSONObject();
				lvrRtnjson.put("CatchBlock", "Error");
			} catch (JSONException e1) {				
			}
		} finally {
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}		
		}
		return lvrRtnjson;
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson) {
		PrintWriter out = null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response = null;
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
