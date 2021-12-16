package com.social.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BufferedReader br = null;
		int respCode;
		String respMsg;
		int year;
		try {
			String content = "";
			URL url = null;
			String online = "";
			String sender = "";
			Date date = new Date(); // your date
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			String mnt = "";
			if (month < 10) {
				mnt = "0";
			}
			int day = cal.get(Calendar.DAY_OF_MONTH);
			String dy = "";
			if (day < 10) {
				dy = "0";
			}
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			String hr = "";
			if (hour < 10) {
				hr = "0";
			}
			int minute = cal.get(Calendar.MINUTE);
			String min = "";
			if (minute < 10) {
				min = "0";
			}
			int second = cal.get(Calendar.SECOND);
			String sec = "";
			if (second < 10) {
				sec = "0";
			}
			String autonum = Integer.toString(year) + mnt + Integer.toString(month)
					+ dy + Integer.toString(day) + hr + Integer.toString(hour) + min
					+ Integer.toString(minute) + sec + Integer.toString(second);
			System.out.println(autonum);
			
			String mobnumber = "9890762151";
    	  		// msgg = "This is text message"&mask="+smsconf.getSenderName();
				if (mobnumber != null && mobnumber.indexOf("+91") > -1) {
					mobnumber = mobnumber.substring(1);
				} else if (mobnumber != null && mobnumber.length() == 10) {
					mobnumber = "91" + mobnumber;
				}
			String msgg = "Dear Vikram, Your SOCYTEA account has been created. Login id : 8888398350, Password is : MWEB502PVB. [WEBBTLYLINK] Click here to login.";
    	  		msgg = URLEncoder.encode(msgg);
    	  		content = "&userid=2000164228&password=abc123&send_to=" + mobnumber + "&msg=" + msgg+"&override_dnd=TRUE";
			try {
				url = new URL("http://enterprise.smsgupshup.com/GatewayAPI/rest?method=sendMessage&v=1.1&msg_type=TEXT&auth_scheme=PLAIN&mask=TESTIN" + content); // whenever send sms
			} catch (MalformedURLException ex) {
				System.out.println("Exception Found URL : " + ex);
			}

			System.out.println("API URL : http://enterprise.smsgupshup.com/GatewayAPI/rest?method=sendMessage&v=1.1&msg_type=TEXT&auth_scheme=PLAIN&mask=TESTIN" + content);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream())); // resd
			// data from URL
			respCode = conn.getResponseCode();// 200, 404, etc
			respMsg = conn.getResponseMessage();// OK, Forbidden, etc
			System.out.println("respMsg----------------"+respMsg);
			
				if (respCode == 200 && respMsg.equalsIgnoreCase("OK")) {
					StringBuilder getresponse = new StringBuilder();
					String ipline;
					while ((ipline = br.readLine()) != null) {
						getresponse.append(ipline);
					}
					if (getresponse != null && getresponse.toString().contains("|")){        		  
						String lvrRsp = getresponse.toString().substring(0,getresponse.indexOf("|")).trim();
						String lvrRsperr = getresponse.toString().substring(getresponse.lastIndexOf("|")+1,getresponse.length()).trim();
						if(lvrRsp!=null && lvrRsp.trim().equalsIgnoreCase("success")){
							System.out.println("gupshup : SMS send success");
							System.out.println("Send Success<IB>");
						} else if (lvrRsp != null && lvrRsp.trim().equalsIgnoreCase("error")) {
							System.out.println("Not Send<IB>" + lvrRsperr);
						} 
					}
				}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}