package com.pack.reconcile;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;

public class PaygatCyberDatelete extends ActionSupport {
	  private static final long serialVersionUID = 1L;
	  private String ivrparams;
	  /**
	   * Executed Method .
	   */
	  ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	  public String execute() {
		JSONObject locObjRecvJson = null;//Receive over all Json	[String Received]
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode=null;
		String ivrcurrntusridobj=null;
		int ivrCurrntusrid=0;
	    try {
	      if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
	    	  ivrparams = EncDecrypt.decrypt(ivrparams);
	    	  boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
	    	  if (ivIsJson) {
	    		locObjRecvJson = new JSONObject(ivrparams);
	    		ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
	    		if(ivrservicecode!=null && !ivrservicecode.equalsIgnoreCase("null") && !ivrservicecode.equalsIgnoreCase("")){
	    			locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
	    			ivrcurrntusridobj =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
	    			String lvrDeleteid  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "deleteid");
	    			String flg  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "flg");
	        	    if (ivrcurrntusridobj!=null && Commonutility.toCheckisNumeric(ivrcurrntusridobj)) {
	    			ivrCurrntusrid= Integer.parseInt(ivrcurrntusridobj);
	    			}else { ivrCurrntusrid=0; }
	        		String lvrEventUpdaSts = null;
	        		String locDldispQry=null;
	        		String whereid=null;
	        		Common locdeleteObj = new CommonDao();
	        		
	        			if(flg!=null && flg.equalsIgnoreCase("cyber")){
	        			locDldispQry= "CyberplatsetmtfileTblVo";
	        			whereid="ivrBnCBPLT_FILE_ID";
	        			}else{
	        				locDldispQry= "PaygatesetmtfileTblVo";	
	        				whereid="ivrBnPAYGATE_SETTLE_ID";
	        			}
	        			lvrEventUpdaSts=locdeleteObj.commonDelete(Integer.parseInt(lvrDeleteid),locDldispQry,whereid);
	        			if(lvrEventUpdaSts.equalsIgnoreCase("Successfully Deleted")){
	        				serverResponse("SI8006","0","S8006",getText("paygate.delete.success"),locObjRspdataJson);	
	    		}else{
	    			serverResponse("SI8006","0","S8006",getText("paygate.delete.error"),locObjRspdataJson);	
	        			}
	    							
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
			  serverResponse("SI8006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
		  serverResponse("SI8006","2","ER0002",getText("catch.error"),locObjRspdataJson);
		} finally {
			locObjRecvJson = null;//Receive over all Json	[String Received]
			locObjRecvdataJson = null;// Receive Data Json		
			locObjRspdataJson = null;// Response Data Json	
			ivrservicecode=null;
			ivrcurrntusridobj=null;
			ivrCurrntusrid=0;
		}	 
		return SUCCESS;
	  }

	  /*
	   * To select Events.
	   */
	  private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson) {
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
