package com.mobi.easypay;

import com.opensymphony.xwork2.ActionSupport;
import com.siservices.login.EncDecrypt;

public class EasyPayResponseMob extends ActionSupport {

	private String responseData;
	private String finalData;
	
	public String execute() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		/// responseData = (String)request.getAttribute("responseData");
		 System.out.println("Final responseData-------------->> " + responseData);
	    if(responseData != null && !responseData.equalsIgnoreCase("")) {
	    	responseData = responseData.replace(" ", "+");
	    	finalData = EncDecrypt.decrypt(responseData);
	    } else {
	    	finalData = "{\"data\":" + "\"No Data Found\""
	          + ",\"Gateway\" : \"None\"}";
	    }
		System.out.println("Final encyrpt finalData-------------->> " + finalData);
		//System.out.println("Final decrypt responseData-------------->> " + EncDecrypt.decrypt(responseData));
		return SUCCESS;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public String getFinalData() {
		return finalData;
	}

	public void setFinalData(String finalData) {
		this.finalData = finalData;
	}
	
}
