package com.mobile.profile;

import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public class profileValidateUtill  extends ActionSupport{

	
	public String profilevalidate(Map<String, String> mapValid){
		String otpValidateCheck="";
		
		String serviceCode=mapValid.get("servicecode");
		System.out.println("==serviceCode=="+serviceCode);
		String townshipKey=mapValid.get("townshipKey");
		String rid=mapValid.get("rid");
		if(serviceCode!=null &&serviceCode.length()==4){
		}else{
			otpValidateCheck+="serviceCode,";
		}
		if(townshipKey!=null && townshipKey.length()==25){
		}else{
			otpValidateCheck+="townshipKey,";	
		}
		
		return otpValidateCheck;
	}
}
