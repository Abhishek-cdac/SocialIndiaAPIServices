
package com.pack.access;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.access.persistance.AccessDaoservice;
import com.pack.accessvo.AccessDetailsVO;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class AccessInfo extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String ivrparams;	
	static Log log = new Log();
	public String execute() throws Exception {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicefor=null;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				Commonutility.toWriteConsole("Step 1 : AccessInfo.class Called");
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicefor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicefor");
				locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				if(ivrservicefor!="" && ivrservicefor.equalsIgnoreCase("1")){
					Commonutility.toWriteConsole("Step 2 : Select All Ip");
					locObjRspdataJson = toAccessDetailUpdateInsert(locObjRecvdataJson);	
					serverResponse("SI10000","0","S10000",getText("access.ipinfo.success"),locObjRspdataJson);
				}else if(ivrservicefor!="" && ivrservicefor.equalsIgnoreCase("2")){
					
					locObjRspdataJson=AccessUpdate.toAccessUpdate(locObjRecvdataJson);
					serverResponse("SI10001","0","S10001",getText("access.ipinfo.success"),locObjRspdataJson);
				}
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : S10000, "+getText("request.format.notmach"), "info", AccessInfo.class);
					serverResponse("SI10000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : S10000, "+getText("request.values.empty"), "info", AccessInfo.class);
				serverResponse("SI10000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);	
			}
		}catch(Exception ex){
			log.logMessage("Exception :main", "error", AccessInfo.class);
		}finally{			
			
		}
		return "success";
	}
	
	public static JSONObject toAccessDetailUpdateInsert(JSONObject pDataJson) {
		JSONObject locFinalRTNObj = null;
		AccessDaoservice locaccessObj = null;
		String ipaddress=null,clientHost=null,language=null,protocol=null,method=null,countryName=null,gmtTime=null,entryTime=null;
		int clientIP = 0;
		
		try {			
			Commonutility.toWriteConsole("Step 1 : AccessInfo.class toAccessDetailUpdateInsert() Enter.");
			ipaddress = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ipaddress");
			clientHost=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "clientHost");
			clientIP=(Integer) Commonutility.toHasChkJsonRtnValObj(pDataJson, "clientIP");
			language=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "language");
			protocol=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "protocol");
			method=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "method");
			countryName=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countryName");
		//	serverTime=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "serverTime");
			gmtTime=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "gmtTime");
			entryTime=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "entryTime");	
			AccessDetailsVO accessObj = new AccessDetailsVO();
			accessObj.setIpaddress(ipaddress);
			accessObj.setHostName(clientHost);
			accessObj.setAccessCount(1);
			accessObj.setPortNo(clientIP);
			accessObj.setLanguage(language);
			accessObj.setProtocolType(protocol);
			accessObj.setMethodType(method);
			accessObj.setCountryName(countryName);			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date clientdate = simpleDateFormat.parse(entryTime);
			String cilenttime=simpleDateFormat.format(clientdate);
			accessObj.setClient(clientdate);
			Date gmtdate = simpleDateFormat.parse(gmtTime);
			String locGmttime=simpleDateFormat.format(gmtdate);
			Date serverdate = simpleDateFormat.parse(togetServerDateTime());
			accessObj.setGmtTime(gmtdate);
			accessObj.setEntryDate(serverdate);
			locaccessObj=new AccessDaoservice();
			
			boolean ipval=locaccessObj.verificationIPInfo(ipaddress,locGmttime,cilenttime);			
			if(ipval!=true){
				locaccessObj.saveAccessInfo(accessObj);	
			}else{
			}
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
				log.logMessage("Exception :Socialindiaservice - AccessInfo : "+e, "error", AccessInfo.class);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			 locFinalRTNObj = null;
			 locaccessObj = null;
			 ipaddress=null;clientHost=null;language=null;protocol=null;method=null;countryName=null;gmtTime=null;entryTime=null;
			clientIP = 0;
		}
	}
	public static String togetServerDateTime(){
		String rtnDtm=null;
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
			 Date date = new Date();
			 rtnDtm=format.format(date);
		}catch(Exception e){
			log.logMessage("Exception foudn in Utilitymthds.togetServerDateTime() accessinfo ---"+e, "error", AccessInfo.class);
		}finally{
			
		}
		return rtnDtm;
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
			}catch(Exception e){
				log.logMessage("Exception :AccessInfo"+e, "error", AccessInfo.class);
			}finally{}
		}
	}
	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

}
