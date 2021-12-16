package com.pack.monitoringmgmt;

import java.io.File;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;


public class Systemtracking extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		try{			
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");														
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
					ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
				}else{
					ivrCurrntusrid=0;
				}
				locObjRspdataJson=toGetSystemtracking(locObjRecvdataJson);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("SYSTRKAD01"), "SYSTRKAD01", ivrCurrntusrid);
						serverResponse(ivrservicecode,"0","E"+ivrservicecode,getText("systemtracking.view.error"),locObjRspdataJson);
					}else{
						AuditTrial.toWriteAudit(getText("SYSTRKAD00"), "SYSTRKAD00", ivrCurrntusrid);
						serverResponse(ivrservicecode,"0","E"+ivrservicecode,getText("systemtracking.view.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : "+ivrservicecode+", "+getText("request.format.notmach"), "info", Systemtracking.class);
					serverResponse(ivrservicecode,"1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : "+ivrservicecode+","+getText("request.values.empty"), "info", Systemtracking.class);
				serverResponse("ivrservicecode","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+ivrservicecode+", "+getText("catch.error"), "error", Systemtracking.class);
			serverResponse(ivrservicecode,"2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{		
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject toGetSystemtracking(JSONObject pDataJson) {
		JSONObject locFinalRTNObj=null;		
		Log log=null;
				
		try {
			log=new Log();				
			int mb = 1024*1024;
			locFinalRTNObj =new JSONObject();
			//Getting the runtime reference from system
			Runtime runtime = Runtime.getRuntime();			   
			long diskSize = new File("/").getTotalSpace();
	        String userName = System.getProperty("user.name");
	        long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
	       
	        Commonutility.toWriteConsole("Size of C: "+diskSize+" Bytes");
	        
	        Commonutility.toWriteConsole("User Name : "+userName);
	       
	        Commonutility.toWriteConsole("RAM Size : "+memorySize+" Bytes");
			
	        Commonutility.toWriteConsole("RAM Size : "+(memorySize / mb)+" MB");
			
			
			Commonutility.toWriteConsole("##### Heap utilization statistics [MB] #####");
			//Print used memory
			Commonutility.toWriteConsole("Used Memory:" +  (runtime.totalMemory() - runtime.freeMemory()) / mb);
			locFinalRTNObj.put("UsedMemory",  +(runtime.totalMemory() - runtime.freeMemory()) / mb);
			locFinalRTNObj.put("FreeMemory",   +(runtime.freeMemory() / mb));
			locFinalRTNObj.put("TotalMemory", +(runtime.totalMemory() / mb));
			locFinalRTNObj.put("MaxMemory",  +(runtime.maxMemory() / mb));
								
			 com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		     long physicalMemorySize = os.getTotalPhysicalMemorySize();
		     long freePhysicalMemory = os.getFreePhysicalMemorySize();
		     long freeSwapSize = os.getFreeSwapSpaceSize();
		     long commitedVirtualMemorySize = os.getCommittedVirtualMemorySize();
		     
		     long rmsi =(physicalMemorySize / mb);// total Ram
		   
		     long vrmasize = (commitedVirtualMemorySize / mb); // Virtual ram
		    
		    // long usdrm = ((physicalMemorySize - freePhysicalMemory) / mb); // usd ram
		     long firs = commitedVirtualMemorySize - freePhysicalMemory;
		     
		     long usdrm = ((physicalMemorySize - firs) / mb); // usd ram
		     Commonutility.toWriteConsole("Used Ram : "+usdrm + "MB");		     
		     long freramsize = 	((commitedVirtualMemorySize - freePhysicalMemory) / mb); // free ram
		     Commonutility.toWriteConsole("commitedVirtualMemorySize : "+ commitedVirtualMemorySize);
		     Commonutility.toWriteConsole("freePhysicalMemory : "+ freePhysicalMemory);
		     Commonutility.toWriteConsole("freramsize- formula : "+ commitedVirtualMemorySize +"- "+freePhysicalMemory +"/"+ mb + "MB");
		     Commonutility.toWriteConsole("freramsize : "+freramsize + "MB");
		     locFinalRTNObj.put("Ramsize",  Commonutility.toCheckNullEmpty(rmsi));
			 locFinalRTNObj.put("freeramsize",  Commonutility.toCheckNullEmpty(freramsize));
		     locFinalRTNObj.put("virtualramsize",  Commonutility.toCheckNullEmpty(vrmasize));
		     locFinalRTNObj.put("usedramsize",  Commonutility.toCheckNullEmpty(usdrm));
			/* // Ram Size
		     Commonutility.toWriteConsole("physicalMemorySize : "+(physicalMemorySize / mb)+" MB");
		     // Free Ram Size
		     Commonutility.toWriteConsole("freePhysicalMemory : "+(freePhysicalMemory / mb)+" MB");
		     Commonutility.toWriteConsole("freeSwapSize : "+(freeSwapSize / mb)+" MB");
		     Commonutility.toWriteConsole("commitedVirtualMemorySize : "+(commitedVirtualMemorySize / mb)+" MB");	  
		     Commonutility.toWriteConsole("Used Ram : "+((physicalMemorySize - freePhysicalMemory - commitedVirtualMemorySize) / mb));
		     
		     
			//Print free memory
			Commonutility.toWriteConsole("Free Memory:"  + runtime.freeMemory() / mb);			
			//Print total available memory
			Commonutility.toWriteConsole("Total Memory:" + runtime.totalMemory() / mb);
			//Print Maximum available memory
			Commonutility.toWriteConsole("Max Memory:" + runtime.maxMemory() / mb);
			
			*/
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			Commonutility.toWriteConsole("Step 6 : Select toGetSystemtracking block end.");
			log.logMessage("Step 4: toGetSystemtracking detail block end.", "info", Systemtracking.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : toGetSystemtracking Exception : "+e, "error", Systemtracking.class);	
			
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			
			locFinalRTNObj=null;
			
		}
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
