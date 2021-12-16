package com.pack.bitly;

import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

public class GetBitlyLink {

	public static String toGetlinlk() { // testing
        try {
            String PROTECTED_RESOURCE_URL = "https://api-ssl.bitly.com/v3/shorten";
            OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
            request.addBodyParameter("format", "json");
            request.addBodyParameter("longUrl", "http://74.208.66.192/socialindiaLive/loginprocess");
            request.addBodyParameter("domain", "bit.ly");
            request.addBodyParameter("access_token", "10596752300b31bd09310151847d384e576b93f1");
            Response send = request.send();
            String body = send.getBody();
            System.out.println("send : " + body);
        } catch (Exception e) {
            System.out.println("e : " + e);
        }
        return "";
    }
	
	public static void main(String ee[]){
			//toGetlinlk();// Test
			String cnvrturl ="http://socytea.com/";
			//String cnvrturl ="http://192.168.1.48:8080/socialIN/";
			String  accesstkn = "10596752300b31bd09310151847d384e576b93f1";
			String btlySrvurl = "https://api-ssl.bitly.com/v3/shorten";
			String btlyurl = toGetBitlyLinkMthd(cnvrturl,"yes",accesstkn,btlySrvurl);
			System.out.println(btlyurl);
	}
	
	public static String toGetBitlyLinkMthd(String longurl, String errorcontinue, String pAccesstkn, String pBtlyisrvurl) {// current use
        JSONObject btlrsJobj = null, dataUrlJSON = null;
        OAuthRequest oAuthrequest = null;
        // String accesstkn = "10596752300b31bd09310151847d384e576b93f1";// login id : peninlogjava@gmail.com  password : peninlog123
        String accesstkn = null;
        String rtnUrl = null;// peninlogjava@gmail.com login
        try {
            //String bitlyserverUrl = "https://api-ssl.bitly.com/v3/shorten";           
            // accesstkn = "10596752300b31bd09310151847d384e576b93f1";
        	String bitlyserverUrl = pBtlyisrvurl;  
        	accesstkn = pAccesstkn;
            oAuthrequest = new OAuthRequest(Verb.POST, bitlyserverUrl);
            oAuthrequest.addBodyParameter("format", "json");
            oAuthrequest.addBodyParameter("longUrl", longurl);
            oAuthrequest.addBodyParameter("domain", "bit.ly");
            oAuthrequest.addBodyParameter("access_token", accesstkn);
            Response oAuthsend = oAuthrequest.send();
            String restBody = oAuthsend.getBody();
            try {
                btlrsJobj = new JSONObject(restBody);
                String statsCode = "";
                String statsTxt = "";
                String btlyLongurl = "", btlyshrturl = "";
                if (btlrsJobj != null && btlrsJobj instanceof JSONObject) {
                	if(btlrsJobj!=null && btlrsJobj.has("status_code") && btlrsJobj.getInt("status_code")!=-1){
                		statsCode = String.valueOf(btlrsJobj.getInt("status_code"));
                	}
                    
                    statsTxt = btlrsJobj.getString("status_txt");

                    if ((statsCode != null && statsCode.equalsIgnoreCase("200")) && (statsTxt != null && statsTxt.equalsIgnoreCase("OK"))) {
                        dataUrlJSON = btlrsJobj.getJSONObject("data");
                        if (dataUrlJSON != null) {
                            btlyLongurl = dataUrlJSON.getString("long_url");
                            btlyshrturl = dataUrlJSON.getString("url");
                            rtnUrl = btlyshrturl;
                        } else {
                            rtnUrl = longurl;
                        }
                    } else if (statsCode != null && statsCode.equalsIgnoreCase("403")) {// Rate Limit exceed RATE_LIMIT_EXCEEDED
//                        System.out.println("Bitly Error Code : " + statsCode + " : " + statsTxt);
                        rtnUrl = longurl;
                    } else if (statsCode != null && statsCode.equalsIgnoreCase("500")) {//INVALID_URI or MISSING_ARG_LOGIN
//                        System.out.println("Bitly Error Code : " + statsCode + " : " + statsTxt);
                        rtnUrl = longurl;
                    } else if (statsCode != null && statsCode.equalsIgnoreCase("503")) {// Unknown error
//                        System.out.println("Bitly Error Code : " + statsCode + " : " + statsTxt);
                        rtnUrl = longurl;
                    } else {
//                        System.out.println("Else block Bitly Error Code : " + statsCode + " : " + statsTxt);
                        rtnUrl = longurl;
                    }
                } else {
                    rtnUrl = longurl;
                }
            } catch (Exception e) {
                System.out.println("Exception found in GetBitlyLink.java: " + e);              
                rtnUrl = longurl;
            }
        } catch (Exception e) {
            System.out.println("Exception found in GetBitlyLink.java: " + e);           
            if (errorcontinue != null && errorcontinue.equalsIgnoreCase("yes")) {
                errorcontinue = "no";
                toGetBitlyLinkMthd(longurl, "no",pAccesstkn,pBtlyisrvurl);
            }

        } finally {
            btlrsJobj = null; oAuthrequest =null;
        }     
        return rtnUrl;
        //send : { 
        //"status_code": 200,
        //"status_txt": "OK",
        //"data": { 
        //"long_url": "http:\/\/www.giggzo.com\/rooster\/9WhOlpOUK7C==\/",
        //"url": "http:\/\/bit.ly\/1rBaAt6",
        //"hash": "1rBaAt6", 
        //"global_hash": "1rBaAt7",
        //"new_hash": 0 
        //}
        //}


//json { "status_code": 200, "status_txt": "OK", "data" : ... }
//json { "status_code": 403, "status_txt": "RATE_LIMIT_EXCEEDED", "data" : null }
//json { "status_code": 500, "status_txt": "INVALID_URI", "data" : null }
//json { "status_code": 500, "status_txt": "MISSING_ARG_LOGIN", "data" : null }
//json { "status_code": 503, "status_txt": "UNKNOWN_ERROR", "data" : null }
//jsonp callback_method({ "status_code": 200, "status_txt": "OK", "data" : ... })
    }
}
