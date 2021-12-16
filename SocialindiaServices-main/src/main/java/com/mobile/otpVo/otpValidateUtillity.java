package com.mobile.otpVo;

import java.util.Map;

//import org.omg.PortableInterceptor.SUCCESSFUL;

import com.opensymphony.xwork2.ActionSupport;

public class otpValidateUtillity extends ActionSupport {
	
	public String otpcommonvalidate(Map<String, String> mapValid){
		String otpValidateCheck="";
		
		String serviceCode=mapValid.get("servicecode");
		System.out.println("==serviceCode=="+serviceCode);
		String townshipKey=mapValid.get("townshipKey");
		String mobileno=mapValid.get("mobileno");
		String type=mapValid.get("type");
		String rid=mapValid.get("rid");
		if(serviceCode!=null &&serviceCode.length()==4){
		}else{
			otpValidateCheck+="serviceCode,";
		}
		if(townshipKey!=null && townshipKey.length()==25){
		}else{
			otpValidateCheck+="townshipKey,";	
		}
		if(type!=null && (Integer.parseInt(type)==1 || Integer.parseInt(type)==2)){
		}else{
			otpValidateCheck+="type,";	
		}
		
		return otpValidateCheck;
	}
	
	public String otpValidatecommonvalidate(Map<String, String> mapValid){
		String otpValidateCheck="";
		
		String serviceCode=mapValid.get("servicecode");
		System.out.println("==serviceCode=="+serviceCode);
		String townshipKey=mapValid.get("townshipKey");
		String mobileno=mapValid.get("mobileno");
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

	public String signupvalidate(Map<String, String> mapValid){
		
			String otpValidateCheck="";
		
		String serviceCode=mapValid.get("servicecode");
		String townshipKey=mapValid.get("townshipKey");
		String mobileno=mapValid.get("mobileno");
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
