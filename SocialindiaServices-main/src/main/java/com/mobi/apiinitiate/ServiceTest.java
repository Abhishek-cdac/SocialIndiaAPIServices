package com.mobi.apiinitiate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.social.login.EncDecrypt;

public class ServiceTest {
	public static void main(String arg[]){
		//http://74.208.66.192/socialindiaLive
		//http://74.208.66.192/socialindiaservices		
	String srverUrl_live="http://74.208.66.192/";
		String srverUrl_local="http://192.168.1.16:8080/";
		String projetName="socialindiaservices/";
		try{
			// Country list
			String srvcUrl=srverUrl_local+"/socialindiaservices/" + ServiceTestData.feedLikeUnlikeA;	
			JSONObject finaljj=new JSONObject();
			finaljj.put("servicecode", "SI1001");
			String postdata=ServiceTestData.feedLikeUnlikeD;
			//postdata = Servicetestmobidata.bookingListD; // feedPostA  feedListA  feedShareD 
			System.out.println("postdata ------------- "+postdata);	
			// 260<!_!>IMG-20161124-WA0005_201611250524201120.jpg<!_!><!_!>1
			String postparam="ivrparams=";				
			String postparamval=EncDecrypt.encrypt(postdata);
			//String postparamval=postdata;
			System.out.println(postparam+postdata);		
			System.out.println(srvcUrl+postparam+URLEncoder.encode(postparamval));		
			toCheckService(postparam+URLEncoder.encode(postparamval),srvcUrl);					
			
		}catch(Exception eee){}finally{}
					
	}
	

	public static void toCheckService(String pPostData, String serviceUrl) {
		URL locvURLObj = null;
		URLConnection locvConnObj = null;
		HttpURLConnection httpConn = null;
		OutputStream locvOutStrmObj = null;

		InputStreamReader locISReadr = null;
		BufferedReader locBfReadr = null;
		try {
			String locWrtnData = pPostData;
			String ulrfr = serviceUrl;
			String lSRtnVal = "";
			locvURLObj = new URL(ulrfr);
			locvConnObj = locvURLObj.openConnection();
			httpConn = (HttpURLConnection) locvConnObj;
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("POST");
			
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			httpConn.setRequestProperty("charset", "utf-8");
			httpConn.setRequestProperty("Content-Length", "" + Integer.toString(locWrtnData.getBytes().length));
			httpConn.setUseCaches(false);
			locvOutStrmObj = httpConn.getOutputStream();
			locvOutStrmObj.write(locWrtnData.getBytes("UTF-8"));
			locvOutStrmObj.close();
			locvOutStrmObj = null;
			//httpConn.setUseCaches(false);
			//httpConn.setConnectTimeout(5000);
			httpConn.connect();
			locISReadr = new InputStreamReader(httpConn.getInputStream());
			locBfReadr = new BufferedReader(locISReadr);
			String inputLine = null;
			while ((inputLine = locBfReadr.readLine()) != null) {
				lSRtnVal += inputLine + "\n";
			}
			System.out.println("---------Response[Start]--------------");
			System.out.println(EncDecrypt.decrypt(lSRtnVal));
			System.out.println("---------Response[End]--------------");
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		} finally {
			try {
				if (locISReadr != null) {
					locISReadr.close();
					locISReadr = null;
				}
				if (locBfReadr != null) {
					locBfReadr.close();
					locBfReadr = null;
				}
				if (httpConn != null) {
					httpConn = null;
				}
			} catch (Exception ee) {
			} finally {
			}
		}
	}
}
