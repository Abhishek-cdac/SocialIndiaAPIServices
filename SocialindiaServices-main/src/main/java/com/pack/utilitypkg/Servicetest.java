package com.pack.utilitypkg;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.social.login.EncDecrypt;

public class Servicetest {

	public static void main(String arg[]) {// http://139.59.25.167:8080/socialindiaservices
		// http://74.208.66.192/socialindiaLive
		// http://74.208.66.192/socialindiaservices
		// String srverUrl_live="http://74.208.66.192/";
		String srverUrl_live = "http://139.59.25.167:8080/";
		String srverUrl_local = "http://192.168.1.19:8088/";
		String projetName = "socialindiaservices/";
		//srverUrl_live = srverUrl_local;
		try {
			String srvcUrl = "";
			String postdata = "";

			// Country list
			srvcUrl = srverUrl_live + "/socialindiaservices/countrylist";
			JSONObject finaljj = new JSONObject();
			finaljj.put("servicecode", "SI1001");
			postdata = finaljj.toString();

			/*
			 * // Report issue srvcUrl =
			 * srverUrl_live+"/socialindiaservices/reportissue"; JSONObject
			 * dataJson=new JSONObject(); JSONObject finaljj=new JSONObject();
			 * finaljj.put("servicecode", "SI1001");
			 * postdata=finaljj.toString();
			 */

			/*
			 * // - Function data List String srvcUrl =
			 * srverUrl_local+"/socialindiaservices/functiondataall"; JSONObject
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "SI1001");
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //----------------------------------Dbversion download JSONObject
			 * dataJson=new JSONObject(); dataJson.put("dbversion", "1");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI1000"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local
			 * +"socialindiaservices/dbversiondownload?dbVersion=1"; String
			 * postdata=finaljj.toString();
			 */

			// -----------------------------Forget password
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("emailid",
			 * "kpalli.prabhakar@gmail.com"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI2002");
			 * finaljj.put("data", dataJson); String srvcUrl=
			 * "http://192.168.1.19:8087/socialindiaservices/forgetPasswordservice"
			 * ; String postdata=finaljj.toString();
			 */

			// -----------------------------Id card Master
			/*
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI1005"); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/idcardlist"
			 * ; String postdata=finaljj.toString();
			 */

			// -----------------------------Known Us
			/*
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI1006"); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/knownuslist"
			 * ; String postdata=finaljj.toString();
			 */

			// -----------------------------Education
			/*
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI1007"); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/edulst";
			 * String postdata=finaljj.toString();
			 */

			// -----------------------------Change password
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("userid",
			 * "166"); dataJson.put("oldpassword", "12345");
			 * dataJson.put("newpassword", "12345678"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI2002");
			 * finaljj.put("data", dataJson); String srvcUrl=
			 * "http://192.168.1.19:8087/socialindiaservices/changePasswordservice"
			 * ; String postdata=finaljj.toString();
			 */

			// -----------------------------Caategory
			/*
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI1008"); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/categorylst"
			 * ; String postdata=finaljj.toString();
			 */

			// -----------------------------Skill
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("categoryid",
			 * "10"); JSONObject finaljj=new JSONObject();
			 * finaljj.put("servicecode", "SI1009"); finaljj.put("data",
			 * dataJson); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/skilllst";
			 * String postdata=finaljj.toString();
			 */

			// -----------------------------Document Type
			/*
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI1010"); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/doclst";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //------------Labor Insert JSONObject dataJson=new JSONObject();
			 * dataJson.put("name", "DOUBLE_DEMY"); dataJson.put("emailid",
			 * "DOUBLEDEMY_NEW@peninlog.com"); dataJson.put("isdcode", "91");
			 * dataJson.put("mobno", "5656564823"); dataJson.put("cardtyp",
			 * "4"); dataJson.put("cardno", "45534532WE"); dataJson.put("add1",
			 * "Nungambakkam"); dataJson.put("add2", "chennai");
			 * dataJson.put("postalcode", "107"); dataJson.put("city", "30290");
			 * dataJson.put("sate", "93"); dataJson.put("country", "102");
			 * dataJson.put("kycname", "55521dd.txt");
			 * dataJson.put("verifystatus", "1");// 0 - unverified, 1 - Verified
			 * dataJson.put("keyforsrch", "sprinter sales,");
			 * dataJson.put("wktypid", "4"); dataJson.put("descrp",
			 * "asdsad, sdsd s2132ad,sasadsadsad, sa sds,adsa,dsad,sad as,dsa");
			 * dataJson.put("imagename","Desert.jpg"); dataJson.put("entryby",
			 * "1");// New dataJson.put("currentloginid","3");// current login
			 * usr id
			 * 
			 * JSONArray ja = new JSONArray(); ja.put("1"); ja.put("2");
			 * ja.put("3"); ja.put("4"); dataJson.put("cateoryJary", ja);
			 * 
			 * String fromPathFile="E:/FileMoveTest/fromimg/Desert.jpg"; File
			 * nn=new File(fromPathFile); byte imgByt[]=new byte[1024];
			 * imgByt=Commonutility.toReadFiletoBytes(nn);
			 * dataJson.put("imgencdata"
			 * ,Commonutility.toByteAryToBase64EncodeStr(imgByt));// encrypted
			 * string on image
			 * 
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI3001"); finaljj.put("servicefor", "1"); finaljj.put("data",
			 * dataJson); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/lbrupdates"
			 * ; String postdata=finaljj.toString();
			 */

			/*
			 * //------------Labor Edit JSONObject dataJson=new JSONObject();
			 * dataJson.put("lbrid", "28"); dataJson.put("name", "RAMCO25");
			 * dataJson.put("emailid", "RAMCO@peninlog.com");
			 * //dataJson.put("mobno", "9787103260"); //dataJson.put("cardtyp",
			 * "1"); //dataJson.put("cardno", "f22232312h323");
			 * 
			 * dataJson.put("add1", "Narasima iyer street, Near water tang,");
			 * dataJson.put("add2", "Kondayampalli [post]");
			 * dataJson.put("postalcode", "107"); dataJson.put("city", "30290");
			 * dataJson.put("sate", "93"); dataJson.put("country", "102");
			 * dataJson.put("rating", "5"); dataJson.put("kycname",
			 * "w55444d.txt");
			 * 
			 * dataJson.put("verifystatus", "1");// 0 - unverified, 1 - Verified
			 * dataJson.put("keyforsrch",
			 * "system, monitor, motherborad, ups, printer");
			 * dataJson.put("wktypid", "1"); dataJson.put("descrp",
			 * "asdsad, sdsd sad,sasadsadsad, sa sds,adsa,dsad,sad as,dsa");
			 * dataJson.put("imagename","Koala.jpg"); dataJson.put("entryby",
			 * "1"); dataJson.put("currentloginid","3");// current login usr id
			 * 
			 * JSONArray ja = new JSONArray(); ja.put("5"); ja.put("6");
			 * ja.put("7"); ja.put("8"); dataJson.put("cateoryJary", ja);
			 * 
			 * String fromPathFile="E:/FileMoveTest/fromimg/Koala.jpg"; File
			 * nn=new File(fromPathFile); byte imgByt[]=new byte[1024];
			 * imgByt=Commonutility.toReadFiletoBytes(nn); String
			 * cmpimg=Commonutility.toByteAryToBase64EncodeStr(imgByt);
			 * dataJson.put("imgencdata",cmpimg);// encrypted string on image
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI3001"); finaljj.put("servicefor", "2");// Edit
			 * finaljj.put("data", dataJson); String srvcUrl=
			 * "http://192.168.1.19:8087/socialindiaservices/lbrupdates?ivrservicefor=2"
			 * ; String postdata=finaljj.toString();
			 */

			/*
			 * // ------------Labor select JSONObject dataJson=new JSONObject();
			 * dataJson.put("lbrid", "14"); dataJson.put("lbrserviceid",
			 * "ZATUQ4S0J4W5FK6TB66YMGKF0"); dataJson.put("lbrstatus", "1");
			 * dataJson.put("currentloginid","3");// current login usr id
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI3002"); finaljj.put("servicefor", "3");// select
			 * finaljj.put("data", dataJson); String srvcUrl=srverUrl_local+
			 * "/socialindiaservices/lbrupdates?ivrservicefor=3"; String
			 * postdata=finaljj.toString();
			 */

			/*
			 * // ------------------Labor Select All String sdtSearch="HOUR";
			 * JSONObject dataJson=new JSONObject(); dataJson.put("lbrstatus",
			 * "1"); dataJson.put("countflg", "yes");
			 * dataJson.put("countfilterflg", "yes"); dataJson.put("startrow",
			 * "1");// starting row dataJson.put("totalrow", "10");// total row
			 * dataJson.put("srchdtsrch", sdtSearch);
			 * dataJson.put("currentloginid","3");// current login usr id
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI3005"); finaljj.put("data", dataJson); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/lbrselectall"
			 * ; String postdata=finaljj.toString();
			 */

			/*
			 * // ------------------Labor Deactivate JSONObject dataJson=new
			 * JSONObject(); dataJson.put("lbrid", "6");
			 * dataJson.put("lbrserviceid", "AN3V3OD6BFAM6SGWPMVAGDPWJ");
			 * dataJson.put("lbrstatus", "1");
			 * dataJson.put("currentloginid","3");// current login usr id
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI3003"); finaljj.put("servicefor", "4");// select
			 * finaljj.put("data", dataJson); String srvcUrl=
			 * "http://192.168.1.19:8087/socialindiaservices/lbrupdates?ivrservicefor=4"
			 * ; String postdata=finaljj.toString();
			 */

			/*
			 * // ------------Postal Code LaborWrkTypMaster JSONObject
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "SI1004");
			 * finaljj.put("cityid", "30290"); String
			 * srvcUrl="http://192.168.1.19:8087/socialindiaservices/postallist"
			 * ; String postdata=finaljj.toString();
			 */

			/*
			 * // ------------Staff Category JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI1011"); String
			 * srvcUrl
			 * ="http://192.168.1.19:8087/socialindiaservices/staffcatgeorylst";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //---------------labor Work Type JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI1012"); String
			 * srvcUrl
			 * ="http://192.168.1.19:8087/socialindiaservices/laborwrktyplst";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * // ------------------Feedback Update per Labor/Merchant/admin
			 * JSONObject dataJson=new JSONObject();
			 * dataJson.put("currentloginid", "51");// Current login id
			 * dataJson.put("feedbckid", "4"); dataJson.put("fdbkdesc",
			 * "merchant groupmerchant groupmerchant group merchant group merchant group merchant groupmerchant groupmerchant groupmerchant groupmerchant groupmerchant group"
			 * ); dataJson.put("fdbkrating", "5"); dataJson.put("ivrservicefor",
			 * "2");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI5002"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"fdbkupdates?ivrservicefor=2";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * // ------------------Feedback Deactive per Labor/Merchant/admin
			 * JSONObject dataJson=new JSONObject();
			 * dataJson.put("currentloginid", "51");// Current login id
			 * dataJson.put("feedbckid", "4"); dataJson.put("ivrservicefor",
			 * "4");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI5002"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"fdbkupdates?ivrservicefor=4";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * // ------------------Feedback Select JSONObject dataJson=new
			 * JSONObject(); dataJson.put("currentloginid", "51");// Current
			 * login id dataJson.put("feedbckid", "4");
			 * dataJson.put("ivrservicefor", "3");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI5003"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"fdbkupdates?ivrservicefor=3";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Event Create--------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("crntusrloginid",
			 * "51");// Current login id dataJson.put("crntusrgrpid", "5");//
			 * Current group id dataJson.put("eventtitle",
			 * "Birthday party for my daugter"); dataJson.put("shortdesc",
			 * "The party start at 8:30 pm. Come and attend my daugter birthday celebration."
			 * ); dataJson.put("eventdesc",
			 * "Gmail is a free, advertising-supported email service provided by Google similar to an Internet forum."
			 * ); dataJson.put("eventvideopath", "");// //invitation video path
			 * dataJson.put("eventstartdate", "19-08-2016");// start date
			 * dataJson.put("eventenddate", "19-09-2016");// end date
			 * dataJson.put("eventstarttime", "8:00 pm");// start time
			 * dataJson.put("eventendtime", "10:00 pm");// end time
			 * dataJson.put("eventlocation", "Valuvarkottam, Chennai - 42");//
			 * Event Location dataJson.put("ivrservicefor", "1");
			 * 
			 * //String fromPathFile="E:/FileMoveTest/fromimg/Desert.jpg";
			 * //File nn=new File(fromPathFile); //byte imgByt[]=new byte[1024];
			 * 
			 * //imgByt=Commonutility.toReadFiletoBytes(nn);
			 * 
			 * //dataJson.put("eventfiledata",Commonutility.
			 * toByteAryToBase64EncodeStr(imgByt));// encrypted string on image
			 * //dataJson.put("eventfilename", "Desert.jpg");//invitation image
			 * name
			 * 
			 * dataJson.put("eventfiledata","");// encrypted string on image
			 * dataJson.put("eventfilename", "");//invitation image name
			 * 
			 * //dataJson.put("shareusrgrpid", "6");//resident grpid JSONArray
			 * ja = new JSONArray(); //ja.put("52"); //ja.put("54");
			 * ja.put("55"); ja.put("94"); dataJson.put("shareusridJary", ja);
			 * 
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI8001"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"eventupdates?ivrservicefor=1";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Event Update--------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("eventid", "95");
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("crntusrgrpid", "5");// Current group id
			 * dataJson.put("eventtitle", "birthday party for my son");
			 * dataJson.put("shortdesc",
			 * "UPDATE The party start at 7:30 pm. Come and attend my son birthday celebration."
			 * ); dataJson.put("eventdesc",
			 * "UPDATE The party start at 7:30 pm. Come and attend my son birthday celebration.The party start at 7:30 pm. Come and attend my son birthday celebration.The party start at 7:30 pm. Come and attend my son birthday celebration."
			 * ); dataJson.put("eventvideopath", "");// //invitation video path
			 * dataJson.put("eventstartdate", "15-07-2016");// start date
			 * dataJson.put("eventenddate", "15-08-2016");// end date
			 * dataJson.put("eventstarttime", "7:00pm");// start time
			 * dataJson.put("eventendtime", "10:00pm");// end time
			 * dataJson.put("eventlocation", "Valuvarkottam, Chennai - 42");//
			 * Event Location dataJson.put("ivrservicefor", "2");
			 * 
			 * //String fromPathFile="E:/FileMoveTest/fromimg/Koala.jpg"; //File
			 * nn=new File(fromPathFile); //byte imgByt[]=new byte[1024];
			 * //imgByt=Commonutility.toReadFiletoBytes(nn);
			 * //dataJson.put("eventfiledata"
			 * ,Commonutility.toByteAryToBase64EncodeStr(imgByt));// encrypted
			 * string on image //dataJson.put("eventfilename",
			 * "Koala.jpg");//invitation image name
			 * 
			 * dataJson.put("eventfiledata","");// encrypted string on image
			 * dataJson.put("eventfilename", "");//invitation image name
			 * 
			 * //dataJson.put("shareusrgrpid", "6");//resident grpid - no use
			 * romeved JSONArray ja = new JSONArray(); ja.put("52");
			 * ja.put("54"); ja.put("55"); ja.put("94");
			 * dataJson.put("shareusridJary", ja);
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI8002"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"eventupdates?ivrservicefor=2";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Event DeActive--------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("eventid", "12");
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("ivrservicefor", "4"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI8003");
			 * finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"eventupdates?ivrservicefor=4";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Event Delete--------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("eventid", "12");
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("ivrservicefor", "5"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI8004");
			 * finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"eventupdates?ivrservicefor=5";
			 * String postdata=finaljj.toString();
			 * 
			 * //---------------------Event Sigle Select---------------
			 * JSONObject dataJson=new JSONObject(); dataJson.put("eventid",
			 * "92"); dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("ivrservicefor", "3"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI8005");
			 * finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"eventupdates?ivrservicefor=3";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Event Select All--------------- String
			 * sdtSearch="7"; JSONObject dataJson=new JSONObject();
			 * dataJson.put("eventstatus", "1"); dataJson.put("countflg",
			 * "yes"); dataJson.put("countfilterflg", "yes");
			 * dataJson.put("startrow", "0");// starting row
			 * dataJson.put("totalrow", "10");// total row
			 * dataJson.put("srchdtsrch", sdtSearch);
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI8006"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"eventviewall"; String
			 * postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Event Share to Resident---------------
			 * JSONObject dataJson=new JSONObject(); dataJson.put("eventid",
			 * "92"); dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("ivrservicefor", "6");
			 * 
			 * //dataJson.put("shareusrgrpid", "6");//resident grpid - not use
			 * removed JSONArray ja = new JSONArray(); //ja.put("52");
			 * //ja.put("54"); ja.put("55"); ja.put("94");
			 * dataJson.put("shareusridJary", ja);
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI8007"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"eventupdates?ivrservicefor=6";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Complaint Create--------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("ivrservicefor", "1");
			 * dataJson.put("crntusrloginid", "31"); // Current login id
			 * dataJson.put("crntusrgrpid", "5"); // Current group id
			 * dataJson.put("cmplinttitle", "Parking problem");
			 * dataJson.put("cmplintdesc", "not clean in my flats ");
			 * dataJson.put("cmplintvideopath", "");// //invitation video path
			 * dataJson.put("cmplintousrid", "");// labor id or merchant id
			 * dataJson.put("cmplintogrpid", "");// labor grp or merchant grp
			 * String fromPathFile="D:/FileMoveTest/fromimg/11.jpg"; File nn=new
			 * File(fromPathFile); byte imgByt[]=new byte[1024];
			 * imgByt=Commonutility.toReadFiletoBytes(nn); String
			 * fromPathFile2="D:/FileMoveTest/fromimg/Desert.jpg"; File nn2=new
			 * File(fromPathFile2); byte imgByt2[]=new byte[1024];
			 * imgByt2=Commonutility.toReadFiletoBytes(nn2);
			 * //dataJson.put("cmplintfiledata1"
			 * ,Commonutility.toByteAryToBase64EncodeStr(imgByt));// encrypted
			 * string on image //dataJson.put("cmplintfilename1",
			 * "11.jpg");//invitation image name
			 * dataJson.put("cmplintfiledata1","");// encrypted string on image
			 * dataJson.put("cmplintfilename1", "");
			 * dataJson.put("cmplintfiledata2","");// encrypted string on image
			 * dataJson.put("cmplintfilename2","");
			 * dataJson.put("cmplintfiledata3","");// encrypted string on image
			 * dataJson.put("cmplintfilename3", "");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI9001"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local
			 * +projetName+"complaintupdates?ivrservicefor=1"; String
			 * postdata=finaljj.toString(); //End-Complaint create
			 * 
			 * //---------------------Complaint update--------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("complaintid", "4");
			 * dataJson.put("ivrservicefor", "2");
			 * dataJson.put("crntusrloginid", "51"); // Current login id
			 * dataJson.put("crntusrgrpid", "5"); // Current group id
			 * dataJson.put("cmplinttitle", "dwedffdfdfgfffgf");
			 * dataJson.put("cmplintdesc", "sdsdff .");
			 * dataJson.put("cmplintvideopath", "");// //invitation video path
			 * dataJson.put("cmplintousrid", "1");// labor id or merchant id
			 * dataJson.put("cmplintogrpid", "7");// labor grp or merchant grp
			 * String fromPathFile="D:/FileMoveTest/fromimg/11.jpg"; File nn=new
			 * File(fromPathFile); byte imgByt[]=new byte[1024];
			 * imgByt=Commonutility.toReadFiletoBytes(nn);
			 * dataJson.put("cmplintfiledata1"
			 * ,Commonutility.toByteAryToBase64EncodeStr(imgByt));// encrypted
			 * string on image dataJson.put("cmplintfilename1",
			 * "11.jpg");//invitation image name
			 * dataJson.put("cmplintfiledata2","");// encrypted string on image
			 * dataJson.put("cmplintfilename2","");
			 * dataJson.put("cmplintfiledata3","");// encrypted string on image
			 * dataJson.put("cmplintfilename3", ""); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI9002");
			 * finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName
			 * +"complaintupdates?ivrservicefor=2"; String
			 * postdata=finaljj.toString(); //End-Complaint Updated
			 * 
			 * 
			 * 
			 * //---------------------Complaint DeActive---------------
			 * JSONObject dataJson=new JSONObject(); dataJson.put("complaintid",
			 * "7"); dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("cmplintclosedby", "51");
			 * dataJson.put("ivrservicefor", "4"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI9003");
			 * finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName
			 * +"complaintupdates?ivrservicefor=4"; String
			 * postdata=finaljj.toString();
			 * 
			 * //---------------------Complaint Delete--------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("complaintid", "7");
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("ivrservicefor", "5"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI9004");
			 * finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName
			 * +"complaintupdates?ivrservicefor=5"; String
			 * postdata=finaljj.toString();
			 * 
			 * //---------------------Event Sigle Select---------------
			 * JSONObject dataJson=new JSONObject(); dataJson.put("complaintid",
			 * "4"); dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("ivrservicefor", "3"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI9005");
			 * finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName
			 * +"complaintupdates?ivrservicefor=3"; String
			 * postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Complaint Select All--------------- String
			 * sdtSearch=""; JSONObject dataJson=new JSONObject();
			 * dataJson.put("complaintstatus", "1"); dataJson.put("countflg",
			 * "yes"); dataJson.put("countfilterflg", "yes");
			 * dataJson.put("startrow", "0");// starting row
			 * dataJson.put("totalrow", "10");// total row
			 * dataJson.put("srchdtsrch", sdtSearch);
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI9006"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"complaintviewall"; String
			 * postdata=finaljj.toString();
			 */

			/*
			 * //---------------------Image upload in
			 * Complaint------------------ JSONObject dataJson=new JSONObject();
			 * dataJson.put("complaintid", "17"); dataJson.put("ivrservicefor",
			 * "7"); String fromPathFile="D:/FileMoveTest/fromimg/11.jpg"; File
			 * nn=new File(fromPathFile); byte imgByt[]=new byte[1024];
			 * imgByt=Commonutility.toReadFiletoBytes(nn);
			 * dataJson.put("cmplintfiledata1"
			 * ,Commonutility.toByteAryToBase64EncodeStr(imgByt));// encrypted
			 * string on image dataJson.put("cmplintfilename1",
			 * "11.jpg");//invitation image name
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI9011"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local
			 * +projetName+"complaintupdates?ivrservicefor=7"; String
			 * postdata=finaljj.toString();
			 */

			/*
			 * //----------------eNews Create ----------------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("crntusrloginid",
			 * "51");// Current login id dataJson.put("crntusrgrpid", "5");//
			 * Current group id dataJson.put("enewstitle", "Hot News Today.");
			 * dataJson.put("enewsdesc",
			 * "Today 10:30 am match will start. so watch every one support to them."
			 * );
			 * 
			 * String fromPathFile="E:/FileMoveTest/fromimg/Koala.jpg"; File
			 * nn=new File(fromPathFile); byte imgByt[]=new byte[1024]; imgByt =
			 * Commonutility.toReadFiletoBytes(nn); String
			 * tmm=Commonutility.toByteAryToBase64EncodeStr(imgByt);
			 * dataJson.put("eventimagedatafirst",tmm);// encrypted string on
			 * image dataJson.put("enewsimagefirst", "Koala.jpg");//invitation
			 * image name
			 * 
			 * //String fromPathFile1="E:/FileMoveTest/fromimg/Desert.jpg";
			 * //File nn1=new File(fromPathFile1); //byte imgByt1[]=new
			 * byte[1024]; //imgByt1=Commonutility.toReadFiletoBytes(nn1);
			 * 
			 * //dataJson.put("eventimagedatasecnd",Commonutility.
			 * toByteAryToBase64EncodeStr(imgByt1));// encrypted string on image
			 * //dataJson.put("enewsimagesecnd", "Desert.jpg");//invitation
			 * image name
			 * 
			 * //dataJson.put("eventimagedatathrd",Commonutility.
			 * toByteAryToBase64EncodeStr(imgByt));// encrypted string on image
			 * //dataJson.put("enewsimagethrd", "Koala.jpg");//invitation image
			 * name
			 * 
			 * //dataJson.put("eventimagedatafrth",Commonutility.
			 * toByteAryToBase64EncodeStr(imgByt));// encrypted string on image
			 * //dataJson.put("enewsimagefrth", "Koala.jpg");//invitation image
			 * name
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI11001"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"enewsupdates?ivrservicefor=1";
			 * String postdata=finaljj.toString();
			 */
			/*
			 * //----------------eNews Update ----------------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("crntusrloginid",
			 * "51");// Current login id dataJson.put("crntusrgrpid", "5");//
			 * Current group id dataJson.put("enewsid", "2");
			 * dataJson.put("enewstitle", "Hot News Today.00-90-9");
			 * dataJson.put("enewsdesc",
			 * "Today 10:30 am match will start. so watch every one support to them."
			 * );
			 * 
			 * String
			 * fromPathFile="E:/FileMoveTest/fromimg/IMG_20150212_163825.jpg";
			 * File nn=new File(fromPathFile); byte imgByt[]=new byte[1024];
			 * imgByt = Commonutility.toReadFiletoBytes(nn); String
			 * tmm=Commonutility.toByteAryToBase64EncodeStr(imgByt);
			 * dataJson.put("eventimagedatafirst",tmm);// encrypted string on
			 * image dataJson.put("enewsimagefirst",
			 * "IMG_20150212_163825.jpg");//invitation image name
			 * 
			 * //String fromPathFile1="E:/FileMoveTest/fromimg/Desert.jpg";
			 * //File nn1=new File(fromPathFile1); //byte imgByt1[]=new
			 * byte[1024]; //imgByt1=Commonutility.toReadFiletoBytes(nn1);
			 * 
			 * //dataJson.put("eventimagedatasecnd",Commonutility.
			 * toByteAryToBase64EncodeStr(imgByt1));// encrypted string on image
			 * //dataJson.put("enewsimagesecnd", "Desert.jpg");//invitation
			 * image name
			 * 
			 * //dataJson.put("eventimagedatathrd",Commonutility.
			 * toByteAryToBase64EncodeStr(imgByt));// encrypted string on image
			 * //dataJson.put("enewsimagethrd", "Koala.jpg");//invitation image
			 * name
			 * 
			 * //dataJson.put("eventimagedatafrth",Commonutility.
			 * toByteAryToBase64EncodeStr(imgByt));// encrypted string on image
			 * //dataJson.put("enewsimagefrth", "Koala.jpg");//invitation image
			 * name
			 * 
			 * 
			 * //dataJson.put("eventimagedatafirst","");// encrypted string on
			 * image //dataJson.put("enewsimagefirst", "");//invitation image
			 * name
			 * 
			 * dataJson.put("eventimagedatasecnd","");// encrypted string on
			 * image dataJson.put("enewsimagesecnd", "");//invitation image name
			 * 
			 * dataJson.put("eventimagedatathrd","");// encrypted string on
			 * image dataJson.put("enewsimagethrd", "");//invitation image name
			 * 
			 * dataJson.put("eventimagedatafrth","");// encrypted string on
			 * image dataJson.put("enewsimagefrth", "");//invitation image name
			 * 
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI11002"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"enewsupdates?ivrservicefor=2";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //----------------eNews DeActivate -----------------------
			 * JSONObject dataJson=new JSONObject();
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("crntusrgrpid", "5");// Current group id
			 * dataJson.put("enewsid", "2");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI11003"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"enewsupdates?ivrservicefor=4";
			 * String postdata=finaljj.toString();
			 */

			/*
			 * //----------------eNews Delete ----------------------- JSONObject
			 * dataJson=new JSONObject(); dataJson.put("crntusrloginid",
			 * "51");// Current login id dataJson.put("crntusrgrpid", "5");//
			 * Current group id dataJson.put("enewsid", "2");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI11004"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"enewsupdates?ivrservicefor=5";
			 * String postdata=finaljj.toString();
			 */

			// ----------------eNews Single Select -----------------------
			/*
			 * JSONObject dataJson=new JSONObject();
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * dataJson.put("crntusrgrpid", "5");// Current group id
			 * dataJson.put("enewsid", "3");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI11005"); finaljj.put("data", dataJson); String
			 * srvcUrl=srverUrl_local+projetName+"enewsupdates?ivrservicefor=3";
			 * String postdata=finaljj.toString();
			 * 
			 * 
			 * String postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * //System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// -----------------------dashboard-------------------=------------
			/*
			 * JSONObject dataJson=new JSONObject();
			 * dataJson.put("crntusrloginid", "51");// Current login id
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI24000"); finaljj.put("data", dataJson);
			 * 
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"commonselect"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// otp request for mobile api
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("mobileno",
			 * "67767766");// Current login id dataJson.put("rid", "");//
			 * Current login id dataJson.put("type", "2");// Current login id
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0003"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"requestotp"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// otp validate for mobile api
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("mobileno",
			 * "987654");// Current login id dataJson.put("rid", "");//
			 * dataJson.put("otp", "913680"); dataJson.put("otpfor", "1");//
			 * Current login id JSONObject finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0004"); finaljj.put("townshipid",
			 * "O7PW0SUUML91G3N48LXQ75NTU"); finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"validateotp"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// Principal Resident signupmobile and signup now not use
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("mobileno",
			 * "987654");// Current login id dataJson.put("emailid",
			 * "12345@gmail.com");// JSONObject finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0008"); finaljj.put("townshipid",
			 * "O7PW0SUUML91G3N48LXQ75NTU"); finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"signupmobile"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id JSONObject finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0011"); finaljj.put("townshipid",
			 * "O7PW0SUUML91G3N48LXQ75NTU"); finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"profilepicupdate";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("timestamp",
			 * "2016-09-28 03:10:44"); dataJson.put("startlimit", "0");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0012"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String
			 * srvcUrl=srverUrl_local+projetName+"addedpersonal_skillslist";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("pskill_uid", "1");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0014"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"delete_pskill"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// profile insert or update
			/*
			 * JSONObject dataJson=new JSONObject(); JSONObject dataJson1=new
			 * JSONObject(); JSONObject dataJson2=new JSONObject(); JSONArray ja
			 * = new JSONArray(); dataJson1.put("pskill_id","1");
			 * dataJson1.put("pskill_value","Account");
			 * 
			 * dataJson2.put("pskill_id","2");
			 * dataJson2.put("pskill_value","Cash");
			 * 
			 * ja.put(dataJson1); ja.put(dataJson2);
			 * 
			 * dataJson.put("rid", "48");// Current login id
			 * dataJson.put("skilldetails", ja); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0013");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"update_pskill"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * //login verify JSONObject dataJson=new JSONObject();
			 * dataJson.put("userid", "54321");// Current login id
			 * dataJson.put("passwd", "54321"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0009");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"loginverify"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// family member delete
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "607");// Current login id dataJson.put("fm_uid", "155");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0014"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"delete_familymember";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// get wishlist all
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("timestamp",
			 * "2016-09-29 08:10:44"); dataJson.put("startlimit", "0");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0020"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"my_wishlist"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// delete wishlist
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("wishlist_id", "1");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0022"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"delete_wishlist";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// add wishlist
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("category_name", "1");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0021"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"add_wishlist"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// http://localhost:8086/socialindia/added_familymember_list
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("timestamp",
			 * "2016-09-30 03:10:44"); dataJson.put("startlimit", "0");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0015"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String
			 * srvcUrl=srverUrl_local+projetName+"added_familymember_list";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * //http://localhost:8086/socialindia/addedit_familymember
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("add_edit", "2");
			 * dataJson.put("fm_uid", "591"); dataJson.put("is_invited", "0");
			 * 
			 * dataJson.put("name_title_id", ""); dataJson.put("fm_name",
			 * "bzbzbzbbz");
			 * 
			 * dataJson.put("gender", "f"); dataJson.put("age", "");
			 * dataJson.put("bloodgrp_id", "1"); dataJson.put("relationship_id",
			 * ""); dataJson.put("mobile", "9767664646");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0016"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * srvcUrl=srverUrl_local+projetName+"addedit_familymember";
			 * postdata=finaljj.toString();
			 */

			/*
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// http://localhost:8086/socialindia/createlogin //
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("passwd",
			 * "xxxxxxx"); dataJson.put("emailid", "kannanr@gmail.com");
			 * dataJson.put("is_social_media", "2");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0005"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"createlogin"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// http://localhost:8086/socialindia/createpasswd
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("passwd",
			 * "rrrrrrr"); dataJson.put("mobileno", "12345678");
			 * dataJson.put("otp", "12345");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0005"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"createpasswd"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// http://localhost:8086/socialindia/user_profile_view //user
			// profile view
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("user_id", "48");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0025"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"user_profile_view";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// http://localhost:8086/socialindia/update_relationship
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("fm_uid", "383");
			 * dataJson.put("relationship_id", "3"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0017");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"update_relationship";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// http://localhost:8086/socialindia/update_relationship
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("fm_uid", "48");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0019"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String
			 * srvcUrl=srverUrl_local+projetName+"login_invite_familymember";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * JSONObject dataJson=new JSONObject(); //partial
			 * dataJson.put("rid", "48");// Current login id
			 * dataJson.put("feed_id", "1"); dataJson.put("like_sts", "1"); //1
			 * or 2 JSONObject finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0027"); finaljj.put("townshipid",
			 * "O7PW0SUUML91G3N48LXQ75NTU"); finaljj.put("societykey",
			 * "KARKZD0LPTFRHMO4JD26DE9RB"); finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"feed_like_unlike";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("search_data", "1");
			 * dataJson.put("timestamp", "2016-10-03 03:10:44");
			 * dataJson.put("startlimit", "0"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0027");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"network_userlist";
			 * String postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("nwuser_id", "49");
			 * dataJson.put("request_type", "3"); dataJson.put("timestamp",
			 * "2016-10-03 03:10:44"); dataJson.put("startlimit", "0");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0027"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * String srvcUrl=srverUrl_local+projetName+"invite_network"; String
			 * postdata=finaljj.toString();
			 * System.out.println("postdata ------------- "+postdata); String
			 * postparam="ivrparams="; String
			 * postparamval=EncDecrypt.encrypt(postdata);
			 * System.out.println(postparam+postdata);
			 * System.out.println(srvcUrl
			 * +postparam+URLEncoder.encode(postparamval));
			 * toCheckService(postparam
			 * +URLEncoder.encode(postparamval),srvcUrl);
			 */

			// toZIPUNGIP();

			/*
			 * Date loccrntdate=null; SimpleDateFormat sdf = new
			 * SimpleDateFormat("yyyy-MM-dd"); loccrntdate = new Date(); Date
			 * olddate = sdf.parse("2010-01-31");
			 * if(loccrntdate.after(olddate)){
			 * System.out.println("loccrntdate is after Date2"); }
			 * 
			 * if(loccrntdate.before(olddate)){
			 * System.out.println("loccrntdate is before Date2"); }
			 * 
			 * if(loccrntdate.equals(olddate)){
			 * System.out.println("loccrntdate is equal Date2"); }
			 * 
			 * String old="29-07-2016"; Date nn=null; SimpleDateFormat ff=new
			 * SimpleDateFormat("dd-MM-yyyy"); SimpleDateFormat fft=new
			 * SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); nn=ff.parse(old); String
			 * tt=fft.format(nn); System.out.println("old : "+old);
			 * System.out.println("nn : "+tt);
			 */
			// toTestt();

			/*
			 * //to test http://192.168.1.31:8080/socialindia/info_library
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "930");// Current login id dataJson.put("timestamp",
			 * "2016-11-10 03:10:44"); dataJson.put("startlimit", "0");
			 * dataJson.put("folder_type", "2"); dataJson.put("search_text",
			 * ""); finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * srvcUrl=srverUrl_local+projetName+"info_library";
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/search_carlist
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("car_number", "12");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson);
			 * 
			 * 
			 * srvcUrl=srverUrl_local+projetName+"search_carlist";
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.1.31:8080/socialindia/info_library_delete
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("doc_id", "22");
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .info_library_deleteA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/offer_carpool -
			 * ADD JSONObject dataJson=new JSONObject(); JSONObject
			 * frequencyJson=new JSONObject(); dataJson.put("rid", "48");//
			 * Current login id dataJson.put("car_model", "hundai I10");
			 * dataJson.put("car_number", "tn10AB1222");
			 * dataJson.put("look_for", "1"); dataJson.put("from_loc",
			 * "velachery"); dataJson.put("from_place_id",
			 * "sfsaq421432132323412341ssfsad"); dataJson.put("start_datetime",
			 * "2016-11-02 03:10:44"); dataJson.put("to_loc", "madhurandhagam");
			 * dataJson.put("to_place_id", "err34535345433232trtret54654");
			 * dataJson.put("end_datetime", "2016-11-30 03:10:44");
			 * dataJson.put("preference", "1,3,4,5,7");
			 * dataJson.put("seatsAvaliable", "3"); dataJson.put("is_return",
			 * "1"); frequencyJson.put("mode", "4"); frequencyJson.put("days",
			 * "1,2,5,7"); dataJson.put("frequency", frequencyJson);
			 * dataJson.put("add_info", "1,3,4,5,7"); dataJson.put("add_edit",
			 * "1"); finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0057"); finaljj.put("townshipid", "O4TMCW5UGVK2LAAPLJMLG6O7J");
			 * finaljj.put("societykey", "P47REGJI766HVN4TXN8S32774");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .add_car_poolA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/offer_carpool -
			 * EDiT JSONObject dataJson=new JSONObject(); JSONObject
			 * frequencyJson=new JSONObject(); dataJson.put("rid", "48");//
			 * Current login id dataJson.put("car_pool_id", "1");
			 * dataJson.put("car_model", "2000"); dataJson.put("car_number",
			 * "tn10AB1222"); dataJson.put("look_for", "1");
			 * dataJson.put("from_loc", "egmore");
			 * dataJson.put("start_datetime", "2016-09-28 03:10:44");
			 * dataJson.put("to_loc", "guindy"); dataJson.put("end_datetime",
			 * "2016-09-30 03:10:44"); dataJson.put("preference", "1,3,4,5,7");
			 * dataJson.put("seatsAvaliable", "4"); dataJson.put("is_return",
			 * "1"); frequencyJson.put("mode", "4"); frequencyJson.put("days",
			 * "1,2,5,7"); dataJson.put("frequency", frequencyJson);
			 * dataJson.put("add_info", "1,3,4,5,7"); dataJson.put("add_edit",
			 * "2"); finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .edit_car_poolA; postdata=finaljj.toString();
			 * 
			 * //to test http://192.168.1.31:8081/socialindia/search_carpool
			 * /*JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("from_loc", "");
			 * dataJson.put("from_place_id", ""); dataJson.put("to_loc", "");
			 * dataJson.put("to_place_id", "");
			 * dataJson.put("depature_datetime", ""); dataJson.put("timestamp",
			 * ""); dataJson.put("startlimit", "0");
			 * dataJson.put("is_mycarpool", "1"); JSONObject frequencyobj=new
			 * JSONObject(); frequencyobj.put("mode", "");
			 * frequencyobj.put("days", ""); dataJson.put("frequency",
			 * frequencyobj); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.search_car_poolA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8081/socialindia/offer_carbooking
			 * JSONObject dataJson=new JSONObject(); JSONObject
			 * frequencyJson=new JSONObject(); dataJson.put("rid", "48");//
			 * Current login id dataJson.put("car_pool_id", "8");
			 * dataJson.put("comments",
			 * " text sdkfsld sldfjsd fsldjflsj flskj");
			 * dataJson.put("car_booking_id", ""); dataJson.put("add_edit",
			 * "1"); finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .book_car_poolA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8081/socialindia/delete_carbooking
			 * JSONObject dataJson=new JSONObject(); JSONObject
			 * frequencyJson=new JSONObject(); dataJson.put("rid", "48");//
			 * Current login id dataJson.put("car_pool_id", "11"); finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .delete_carbookingA; postdata=finaljj.toString();
			 */

			// Merchant Search - List
			// to test http://localhost:8086/socialindia/merchant_category_list
			/*
			 * JSONObject dataJson=new JSONObject(); JSONObject
			 * frequencyJson=new JSONObject(); dataJson.put("rid", "48");//
			 * Current login id dataJson.put("startlimit", "0");
			 * dataJson.put("key_for_search", "san"); dataJson.put("timestamp",
			 * "2016-10-25 17:58:30"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "O7PW0SUUML91G3N48LXQ75NTU"); finaljj.put("societykey",
			 * "KARKZD0LPTFRHMO4JD26DE9RB"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.merchant_list_searchA
			 * ; postdata=Servicetestmobidata.merchant_list_searchD;
			 */

			/*
			 * //to test
			 * http://localhost:8086/socialindia/customer_product_order
			 * JSONObject dataJson=new JSONObject(); JSONObject
			 * frequencyJson=new JSONObject(); JSONObject itemJsonobj=new
			 * JSONObject(); JSONArray item_list=new JSONArray();
			 * dataJson.put("rid", "48");// Current login id
			 * dataJson.put("merchant_id", "1"); dataJson.put("order_comments",
			 * "test sdsfs"); itemJsonobj.put("item_name","aaa");
			 * itemJsonobj.put("order_quantity","12");
			 * item_list.put(itemJsonobj); dataJson.put("item_list",item_list);
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .customer_product_orderA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/shop_catogory_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("category", "1");
			 * dataJson.put("shop_name", ""); dataJson.put("key_for_search",
			 * ""); dataJson.put("startlimit", "0"); dataJson.put("timestamp",
			 * ""); finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .merchant_list_searchA; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.1.31:8080/socialindia/merchant_issue_posting
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "51");// Current login id dataJson.put("merchant_id", "4");
			 * dataJson.put("merchant_issue_ids", "1,2,3,4");
			 * dataJson.put("email", ""); dataJson.put("text", ""); finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O4TMCW5UGVK2LAAPLJMLG6O7J");
			 * finaljj.put("societykey", "P47REGJI766HVN4TXN8S32774");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .merchant_issue_postingA; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://localhost:8086/socialindia/merchant_customer_rating
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "51");// Current login id dataJson.put("rating", "5");
			 * dataJson.put("merchant_id", "12"); dataJson.put("email", "");
			 * dataJson.put("text", ""); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "O4TMCW5UGVK2LAAPLJMLG6O7J"); finaljj.put("societykey",
			 * "P47REGJI766HVN4TXN8S32774"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.
			 * merchant_customer_ratingA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://localhost:8086/socialindia/merchant_search_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("category", "1");
			 * dataJson.put("shop_name", ""); dataJson.put("key_for_search",
			 * ""); dataJson.put("startlimit", "0"); dataJson.put("timestamp",
			 * "2016-10-20 16:10:44"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "O7PW0SUUML91G3N48LXQ75NTU"); finaljj.put("societykey",
			 * "KARKZD0LPTFRHMO4JD26DE9RB"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.merchant_list_searchA
			 * ; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.1.31:8080/socialindia/merchant_issue_search_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("category", "");
			 * //dataJson.put("shop_name", ""); dataJson.put("key_for_search",
			 * "san"); dataJson.put("startlimit", "0");
			 * dataJson.put("timestamp", "2016-10-20 16:10:44"); finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .merchant_issue_searchA; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://localhost:8086/socialindia/merchant_issue_search_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("merchant_id", "4");
			 * dataJson.put("order_comments", "customer order the product");
			 * JSONArray orderList=new JSONArray(); JSONObject orderDetail=new
			 * JSONObject(); orderDetail.put("item_name", "bbbb");
			 * orderDetail.put("order_quantity", "5");
			 * orderList.put(orderDetail); orderDetail=new JSONObject();
			 * orderDetail.put("item_name", "ccccc");
			 * orderDetail.put("order_quantity", "4");
			 * orderList.put(orderDetail); dataJson.put("item_list",orderList);
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .merchant_product_orderA; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.1.31:8080/socialindia/facility_booked_list
			 * JSONObject dataJson=new JSONObject(); finaljj=new JSONObject();
			 * dataJson.put("rid", "902");// Current login id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.facility_booked_listA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://localhost:8086/socialindia/facility_booking_addedit
			 * dataJson=new JSONObject(); finaljj=new JSONObject();
			 * dataJson.put("rid", "7");// Current login id
			 * dataJson.put("facility_id", "1"); dataJson.put("title",
			 * "B DAY PARTY"); dataJson.put("place", " chennai ");
			 * dataJson.put("desc", "saaaaaaaaaaa jgj jgjgjhg jug assssssssss");
			 * dataJson.put("start_time", "2016-11-20 16:10:44");
			 * dataJson.put("end_time", "2016-11-20 20:10:44");
			 * //dataJson.put("contactno", "98879865465432");
			 * dataJson.put("add_edit", "1");
			 * 
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.facility_booking_addeditA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.131:8080/socialindia/createNewEvent
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("function_id", "2");
			 * dataJson.put("function_template_id", "5");
			 * dataJson.put("function_start_time", "2016-11-23 17:00:24");
			 * dataJson.put("function_end_time", "2016-11-23 17:00:26");
			 * dataJson.put("facility_id", ""); dataJson.put("location-addr",
			 * "Manpada Road,Dombivli, 421021");
			 * dataJson.put("location-lat_longt",
			 * "19.20883523035998,73.0957816913724"); dataJson.put("desc",
			 * "bkhbk"); dataJson.put("event_id",""); dataJson.put("event_type",
			 * "1"); dataJson.put("is_need_rsvp", "0"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.event_createA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/event_search_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("timestamp",
			 * "2016-09-28 03:10:44"); dataJson.put("event_search",
			 * "hghfhfhchfh"); dataJson.put("startlimit", "0"); JSONObject
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.event_contributeListA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://localhost:8086/socialindia/facility_master_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "51");// Current login id dataJson.put("timestamp",
			 * "2016-11-01 03:10:44"); dataJson.put("startlimit", "0");
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O4TMCW5UGVK2LAAPLJMLG6O7J");
			 * finaljj.put("societykey", "P47REGJI766HVN4TXN8S32774");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.facility_master_listA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://localhost:8086/socialindia/make_event_contribution
			 * JSONObject dataJson=new JSONObject(); finaljj=new JSONObject();
			 * dataJson.put("rid", "52");// Current login id
			 * dataJson.put("event_id", "3"); dataJson.put("amount", "12");
			 * 
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.make_event_contributionA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://localhost:8086/socialindia/merchant JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "51");// Current
			 * login id dataJson.put("key_for_search", "sa"); finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O4TMCW5UGVK2LAAPLJMLG6O7J");
			 * finaljj.put("societykey", "P47REGJI766HVN4TXN8S32774");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.merchant_keword_listA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://localhost:8086/socialindia/facility_booking_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("timestamp", "");
			 * dataJson.put("startlimit", "0"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.facility_booking_listA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://localhost:8086/socialindia/make_event_rsvp
			 * JSONObject dataJson=new JSONObject(); finaljj=new JSONObject();
			 * dataJson.put("rid", "52");// Current login id
			 * dataJson.put("event_id", "3"); dataJson.put("rsvp_flow", "1");
			 * 
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.make_RSVPA; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://localhost:8086/socialindia/merchant_issue_search_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("doc_id", "22");
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .info_library_deleteA; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://localhost:8086/socialindia/merchant_issue_search_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "48");// Current login id dataJson.put("folder_type", "1");
			 * dataJson.put("timestamp", "2016-11-01 03:10:44");
			 * dataJson.put("startlimit", "0"); dataJson.put("search_text","");
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "O7PW0SUUML91G3N48LXQ75NTU");
			 * finaljj.put("societykey", "KARKZD0LPTFRHMO4JD26DE9RB");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .info_libraryA; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.1.31:8080/socialindia/eventContributorList
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "52");// Current login id dataJson.put("event_id", "3");
			 * dataJson.put("attender_contributor", "0");
			 * dataJson.put("startlimit", "0"); dataJson.put("timestamp",
			 * "2016-11-01 03:10:44"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.eventContributorListA
			 * ; postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.1.31:8080/socialindia/facility_booking_delete
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("facility_booking_id",
			 * "6"); finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .facility_booking_deleteA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/event_delete
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("event_id", "150");
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .event_deleteA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/notificationStatus
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("notificationstatus",
			 * "1"); dataJson.put("mobile_number", "9551401307"); finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .notificationStatusA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/onlineStatusupdate
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "51");// Current login id finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "O4TMCW5UGVK2LAAPLJMLG6O7J"); finaljj.put("societykey",
			 * "P47REGJI766HVN4TXN8S32774"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.onlineStatusupdateA
			 * ; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/onlineStatusupdate
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "51");// Current login id finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "O4TMCW5UGVK2LAAPLJMLG6O7J"); finaljj.put("societykey",
			 * "P47REGJI766HVN4TXN8S32774"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.onlineStatusupdateA
			 * ; postdata=finaljj.toString();
			 */

			// System.out.println("---------------------"+System.currentTimeMillis());

			/*
			 * String starttime="2016-11-18 13:23:00"; SimpleDateFormat
			 * simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 * SimpleDateFormat dtToStr = new SimpleDateFormat("dd-MM-yyyy");
			 * SimpleDateFormat dtToStrTime = new SimpleDateFormat("HH:mm a");
			 * Date dat=simpleDateFormat.parse(starttime);
			 * System.out.println("dat-------------"+dat);
			 * 
			 * System.out.println("getdate=---------"+dtToStr.format(dat));
			 * System.out.println("getdate=---------"+dtToStrTime.format(dat));
			 */

			/*
			 * //to test http://localhost:8086/socialindia/eventContributorList
			 * /*JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "930");// Current login id dataJson.put("startlimit", "0");
			 * dataJson.put("timestamp", ""); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI00033");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 */

			/*
			 * dataJson = new JSONObject();
			 * 
			 * dataJson.put("countflg", "yes"); dataJson.put("countfilterflg",
			 * "yes"); dataJson.put("startrow","0");// starting row
			 * dataJson.put("totalrow", "10");// overalltotal row
			 * dataJson.put("srchdtsrch", "");// overalltotal row
			 * dataJson.put("currentloginid", "902"); dataJson.put("timestamp",
			 * ""); finaljj = new JSONObject(); finaljj.put("servicecode",
			 * "SI00033"); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("is_mobile","1");
			 * finaljj.put("data", dataJson);
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/ads_post_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("search_text", "aa");
			 * dataJson.put("startlimit", "0"); dataJson.put("timestamp", "");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0033"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.ads_post_listA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.1.31:8080/socialindia/ads_post
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("ad_id", "32");
			 * dataJson.put("category", "3"); dataJson.put("category_name",
			 * "Engineering"); dataJson.put("ad_title", "test");
			 * dataJson.put("ad_desc", "desc addd dsfsafsd dfs");
			 * dataJson.put("ad_amount", "102.35"); dataJson.put("ad_user_type",
			 * "2"); dataJson.put("ad_stitle", "weqeq"); dataJson.put("feed_id",
			 * "1"); JSONArray jsarray=new JSONArray();
			 * dataJson.put("remove_attach", jsarray);
			 * 
			 * dataJson.put("ad_stitle", "weqeq"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0033");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.ads_postA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //complaints details show dataJson = new JSONObject();
			 * dataJson.put("complaintstatus", "1"); dataJson.put("countflg",
			 * "yes"); dataJson.put("countfilterflg", "yes");
			 * dataJson.put("startrow","0");// starting row
			 * dataJson.put("totalrow", "10");// overalltotal row
			 * dataJson.put("srchdtsrch", "");// overalltotal row
			 * dataJson.put("crntusrloginid", "902"); dataJson.put("timestamp",
			 * ""); dataJson.put("slectfield", ""); dataJson.put("searchname",
			 * ""); dataJson.put("society", ""); finaljj = new JSONObject();
			 * finaljj.put("servicecode", "R0137"); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("is_mobile","1");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .cmpltselectall; postdata=finaljj.toString();
			 * 
			 * 
			 * ///createComplaint committee
			 * 
			 * 
			 * /* //to test
			 * http://192.168.131:8080/socialindia/broadcastEventList JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "902");// Current
			 * login id dataJson.put("event_search", "");
			 * dataJson.put("timestamp", ""); dataJson.put("startlimit", "0");
			 * dataJson.put("event_type", "3");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.broadcastEventListA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.131:8080/socialindia/broadcastEventFileUpload
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("event_id", "189");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.broadcastEventFileUploadA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test
			 * http://192.168.131:8080/socialindia/broadcastEventImageList
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("event_id", "190");
			 * dataJson.put("timestamp", ""); dataJson.put("startlimit", "0");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.broadcastEventImageListA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.131:8080/socialindia/complaint_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("timestamp", "");
			 * dataJson.put("startlimit", "0");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.complaint_listA; postdata=finaljj.toString();
			 */

			/*
			 * //to test http://192.168.131:8080/socialindia/complaint_post
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("title", "");
			 * dataJson.put("desc", ""); dataJson.put("post_to", "1");
			 * dataJson.put("complaint_id", ""); dataJson.put("feed_id", "");
			 * dataJson.put("timestamp", ""); dataJson.put("startlimit", "0");
			 * JSONArray rematt=new JSONArray(); dataJson.put("remove_attach",
			 * rematt);
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.complaint_postA; postdata=finaljj.toString();
			 */
			/*
			 * ///createComplaint committee JSONObject dataJson=new
			 * JSONObject(); dataJson.put("servicefor", "1");
			 * dataJson.put("crntusrloginid", "902"); // Current login id
			 * dataJson.put("crntusrgrpid", "5"); // Current group id
			 * dataJson.put("cmplintemail", "test@ttt.com");
			 * dataJson.put("cmplinttitle"
			 * ,"test of complaint committee ccccvc");
			 * dataJson.put("cmplintshrtdesc", "ddfff dfdf");
			 * dataJson.put("cmplintdesc",
			 * "ddfff dfdfddfff dfdfddfff dfdfddfff dfdf");
			 * dataJson.put("complainttoflag", "0");// 0 - external,
			 * 1-committee, 2 - admin dataJson.put("cmplintvideopath", "");//
			 * //invitation video path dataJson.put("cmplintousrid", "");
			 * dataJson.put("cmplintogrpid", ""); JSONObject finaljj=new
			 * JSONObject(); finaljj=new JSONObject(); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("is_mobile","1");
			 * finaljj.put("servicefor", "1"); finaljj.put("servicecode",
			 * "R0164"); finaljj.put("data", dataJson);
			 * 
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.
			 * complaintupdates; postdata=finaljj.toString();
			 */

			// complaint deactive
			/*
			 * dataJson.put("complaintid", "349");
			 * dataJson.put("crntusrloginid", "902");// Current login id
			 * dataJson.put("cmplintclosedby","902");
			 * dataJson.put("cmplintclosereason", "complaint closed");
			 * finaljj=new JSONObject(); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("is_mobile","1");
			 * finaljj.put("servicefor", "4"); finaljj.put("servicecode",
			 * "SI9003"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName
			 * +Servicetestmobidata.complaintupdates;
			 * postdata=finaljj.toString();
			 */

			// to test http://192.168.131:8080/socialindia/complaint_post
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("timestamp", "");
			 * dataJson.put("startlimit", "0");
			 * 
			 * dataJson.put("is_resident", "0"); dataJson.put("post_to", "0");
			 * dataJson.put("title", "ddffffb  fg"); dataJson.put("email_id",
			 * "ttwe@ggg.bom"); dataJson.put("desc", "ttwe@ggg.bom");
			 * dataJson.put("complaint_id", ""); dataJson.put("feed_id", "0");
			 * 
			 * JSONArray jsarray=new JSONArray(); dataJson.put("remove_attach",
			 * jsarray);
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.complaint_postA; postdata=finaljj.toString();
			 * 
			 * //to test http://192.168.131:8080/socialindia/complaint_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "4");//
			 * Current login id dataJson.put("timestamp", "");
			 * dataJson.put("startlimit", "0"); dataJson.put("search_text",
			 * "a"); dataJson.put("is_resident", "0");//0-committee 1-resdient
			 * dataJson.put("post_to", "1");//0 external--- 1 internal
			 * dataJson.put("statusflg", "2");//1-app JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.complaint_listA; postdata=finaljj.toString();
			 * 
			 * //to test http://192.168.131:8080/socialindia/complaint_close
			 * JSONObject dataJson=new JSONObject(); dataJson.put("complaintid",
			 * "352"); dataJson.put("crntusrloginid","902");// Current login id
			 * dataJson.put("cmplintclosedby","902"); dataJson.put("desc",
			 * "complaint closed"); dataJson.put("status", "2");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "R0171"); finaljj.put("servicefor", "4");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.complaintupdates;
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * /* dataJson=new JSONObject(); dataJson.put("rid", "902");//
			 * Current login id dataJson.put("timestamp", "");
			 * dataJson.put("startlimit", "0"); dataJson.put("search_text", "");
			 * dataJson.put("is_resident", "0"); dataJson.put("post_to", "0");
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.complaintupdates;
			 * postdata=finaljj.toString();
			 * 
			 * //postdata = Servicetestmobidata.bookingListD;
			 * 
			 * 
			 * //Booking approval JSONObject dataJson=new JSONObject();
			 * dataJson.put("statusflg", "1");
			 * dataJson.put("crntusrloginid","902");// Current login id
			 * dataJson.put("committeecomments","ssas dfggg");
			 * dataJson.put("bookingid", "8");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "SI00035"); finaljj.put("servicefor", "4");
			 * finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.bookinglistupdates;
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * //to test http://192.168.131:8080/socialindia/bookingtabledetails
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "16");// Current login id dataJson.put("timestamp", "");
			 * dataJson.put("startrow", "0"); dataJson.put("srchdtsrch", "");
			 * dataJson.put("countflg", "yes"); dataJson.put("countfilterflg",
			 * "yes"); //dataJson.put("society", "");
			 * dataJson.put("booking_status", "1"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI00033");
			 * finaljj.put("townshipid", "PVEKSRONJ5L5L2STNAXU3P9NN");
			 * finaljj.put("societykey", "SWY5351634YN1C4WKNLYW2PAK");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.FacilityBookinglistA;
			 * postdata=finaljj.toString();
			 * 
			 * //Publish_Skillslist /* JSONObject dataJson=new JSONObject();
			 * dataJson.put("rid", "16");// Current login id
			 * dataJson.put("timestamp", ""); dataJson.put("category_id", "");
			 * dataJson.put("search_flg", "1"); dataJson.put("skill_id", "");
			 * dataJson.put("startlimit", "0"); dataJson.put("title", "");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0102"); finaljj.put("townshipid", "PVEKSRONJ5L5L2STNAXU3P9NN");
			 * finaljj.put("societykey", "SWY5351634YN1C4WKNLYW2PAK");
			 * 
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.skillpublichSearchA;
			 * postdata=finaljj.toString();
			 */

			// Publish_Delete
			/*
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("feed_id", "");
			 * dataJson.put("publish_skill_id", "9");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0102"); finaljj.put("townshipid", "6ZVG6UNCR4OR9TAFUPEFL6W2O");
			 * finaljj.put("societykey", "L5NUFFK9FDID4OIH2U5ESJ2YT");
			 * 
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.skillpublichDelA;
			 * postdata=finaljj.toString();8
			 * 
			 * 
			 * //http://192.168.131:8080/socialindia/issue_gatepass_post
			 * 
			 * 
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "4");//
			 * Current login id dataJson.put("visitor_name", "suresh ccss");
			 * dataJson.put("visitor_mobile_no", "23345566");
			 * dataJson.put("add_edit", "1"); dataJson.put("date_of_birth", "");
			 * dataJson.put("visitor_email", "Dharma.ss@gmail.com");
			 * dataJson.put("visitor_id_type", "1");
			 * dataJson.put("visitor_id_no", "dddfffvs12");
			 * dataJson.put("visitor_accompanies", "3");
			 * dataJson.put("issue_date","2016-12-02");
			 * dataJson.put("issue_time","19:24:26");
			 * dataJson.put("expiry_date","2016-12-03");
			 * dataJson.put("skill_id","1");
			 * dataJson.put("other_detail","other_detail");
			 * dataJson.put("pass_type", "1"); dataJson.put("vehicle_no",
			 * "TN32AB3268"); dataJson.put("visitor_location", "");
			 * dataJson.put("gatepass_id", "");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.Gatepass_PostA; postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * //http://192.168.131:8080/socialindia/deleteGatepass JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("pass_id", "1");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.deleteGatepassA; postdata=finaljj.toString();
			 * 
			 * 
			 * //http://192.168.131:8080/socialindia/gatepass_list JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "5");// Current
			 * login id dataJson.put("timestamp", ""); dataJson.put("startrow",
			 * "0"); dataJson.put("startlimit", "0"); dataJson.put("srchdtsrch",
			 * "zzdddd"); dataJson.put("countflg", "yes");
			 * dataJson.put("countfilterflg", "yes"); //dataJson.put("society",
			 * "");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.GatepassIssuelistA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/confirmgatepassEntryA
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "5");//
			 * Current login id dataJson.put("visitor_pass_no", "SL7X7S17");
			 * dataJson.put("confirmation_flag", "1"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.confirmgatepassEntryA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/createEvoting JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("title", "qwqsadad"); dataJson.put("desc",
			 * "asdadad sdasda"); dataJson.put("add_edit", "1");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.create_evotingA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/createEvoting -- Edit
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "4");//
			 * Current login id dataJson.put("title", "qqqqqqq");
			 * dataJson.put("desc", "wwwwwwwwwwww w  wdwtttc sttt");
			 * dataJson.put("add_edit", "2"); dataJson.put("evoting_id", "35");
			 * JSONArray removelist=new JSONArray(); //removelist.put("11");
			 * dataJson.put("remove_attach", removelist); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.create_evotingA; postdata=finaljj.toString();
			 */
			/*
			 * //http://192.168.131:8080/socialindia/deleteEvoting JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("pass_id", "37"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.deleteEvotingA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/evoting_list JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("search", "");
			 * dataJson.put("published_status", ""); dataJson.put("evoting_id",
			 * ""); dataJson.put("timestamp", ""); dataJson.put("startlimit",
			 * "0"); dataJson.put("order_by", ""); dataJson.put("title", "");
			 * dataJson.put("from_date", ""); dataJson.put("to_date", "");
			 * dataJson.put("voting_status", "1"); dataJson.put("min_vote", "");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.evoting_listA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/residentAddVoting
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "7");//
			 * Current login id dataJson.put("evoting_id", "3");
			 * dataJson.put("voting_status", "3"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.residentAddVotingA;
			 * postdata=finaljj.toString();
			 * 
			 * //http://192.168.131:8080/socialindia/publishEvoting JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("evoting_id", "48"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.publishEvotingA; postdata=finaljj.toString();
			 */
			/*
			 * //http://192.168.131:8080/socialindia/residentevoting_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "4");//
			 * Current login id dataJson.put("search", "");
			 * dataJson.put("published_status", ""); dataJson.put("evoting_id",
			 * ""); dataJson.put("timestamp", ""); dataJson.put("startlimit",
			 * "0"); dataJson.put("order_by", ""); dataJson.put("title", "");
			 * dataJson.put("from_date", ""); dataJson.put("to_date", "");
			 * dataJson.put("min_vote", "0"); JSONObject finaljj = new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * 
			 * srvcUrl = srverUrl_local+projetName +
			 * Servicetestmobidata.residentEvoting_listA;
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * //http://192.168.131:8080/socialindia/gatepasshistory_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "5");//
			 * Current login id dataJson.put("timestamp", "");
			 * dataJson.put("startrow", "0"); dataJson.put("startlimit", "0");
			 * dataJson.put("srchdtsrch", ""); dataJson.put("countflg", "yes");
			 * dataJson.put("countfilterflg", "yes"); //dataJson.put("society",
			 * "");
			 * 
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "0"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.GatepassIssuehistorylistA;
			 * postdata=finaljj.toString();
			 * //http://192.168.131:8080/socialindia/gatepassEntry JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "5");// Current
			 * login id dataJson.put("visitor_pass_no", "RJOP2536");
			 * 
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.gatepassEntryA; postdata=finaljj.toString();
			 */
			/*
			 * //http://192.168.131:8080/socialindia/confirmgatepassEntryA
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "5");//
			 * Current login id dataJson.put("visitor_pass_no", "RJOP2536");
			 * dataJson.put("confirmation_flag", "1"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "JN7EUUHYLNBJ19040GP3SCB2Z"); finaljj.put("societykey",
			 * "99BJ2INLNWZ9LXNZ1O2W3NO07"); finaljj.put("is_mobile", "1");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.confirmgatepassEntryA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/createEvoting JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("title", "qwqsadad"); dataJson.put("desc",
			 * "asdadad sdasda"); dataJson.put("add_edit", "1");
			 * 
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.create_evotingA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/createEvoting -- Edit
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "4");//
			 * Current login id dataJson.put("title", "qqqqqqq");
			 * dataJson.put("desc", "wwwwwwwwwwww w  wdwtttc sttt");
			 * dataJson.put("add_edit", "2"); dataJson.put("evoting_id", "35");
			 * JSONArray removelist=new JSONArray(); //removelist.put("11");
			 * dataJson.put("remove_attach", removelist); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.create_evotingA; postdata=finaljj.toString();
			 */
			/*
			 * //http://192.168.131:8080/socialindia/deleteEvoting JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("pass_id", "37"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.deleteEvotingA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/evoting_list JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("search", "");
			 * dataJson.put("published_status", ""); dataJson.put("evoting_id",
			 * ""); dataJson.put("timestamp", ""); dataJson.put("startlimit",
			 * "0"); dataJson.put("order_by", ""); dataJson.put("title", "");
			 * dataJson.put("from_date", ""); dataJson.put("to_date", "");
			 * dataJson.put("voting_status", "1"); dataJson.put("min_vote", "");
			 * JSONObject finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.evoting_listA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/residentAddVoting
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "7");//
			 * Current login id dataJson.put("evoting_id", "3");
			 * dataJson.put("voting_status", "3"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.residentAddVotingA;
			 * postdata=finaljj.toString();
			 * 
			 * //http://192.168.131:8080/socialindia/publishEvoting JSONObject
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("evoting_id", "48"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.publishEvotingA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/residentevoting_list
			 * JSONObject dataJson=new JSONObject(); dataJson.put("rid", "4");//
			 * Current login id dataJson.put("search", "");
			 * dataJson.put("published_status", ""); dataJson.put("evoting_id",
			 * ""); dataJson.put("timestamp", ""); dataJson.put("startlimit",
			 * "0"); dataJson.put("order_by", ""); dataJson.put("title", "");
			 * dataJson.put("from_date", ""); dataJson.put("to_date", "");
			 * dataJson.put("min_vote", "0"); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.residentEvoting_listA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/reportissue JSONObject
			 * dataJson=new JSONObject(); dataJson.put("emailid", "bah@h.hj");//
			 * Current login id dataJson.put("mobileno", "9000000000");
			 * dataJson.put("name", "mxz"); dataJson.put("issuedesc", "hsh");
			 * dataJson.put("pp_resident", ""); JSONObject finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0006");
			 * finaljj.put("townshipid", "SDF67GH68J876586J87J78DRR");
			 * finaljj.put("societykey", ""); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_live+projetName+ Servicetestmobidata.reoprtA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/reportissue JSONObject
			 * dataJson=new JSONObject(); dataJson.put("wishlist_type", "1");//
			 * Current login id dataJson.put("wishlist_type_id", "1");
			 * dataJson.put("is_favour", "0"); dataJson.put("rid", "20");
			 * finaljj.put("servicecode", "0006"); finaljj.put("townshipid",
			 * "SDF67GH68J876586J87J78DRR"); finaljj.put("societykey",
			 * "9QE6TYEVN3WC2WUHIL9Z5GK6K"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.addwishlistA; postdata=finaljj.toString();
			 * //http://192.168.131:8080/socialindia/skilled_directory_search
			 * JSONObject dataJson=new JSONObject();
			 * dataJson.put("skill_category", "");// Current login id
			 * dataJson.put("from_year_exp", ""); dataJson.put("to_year_exp",
			 * ""); dataJson.put("rating", ""); dataJson.put("locaton", "");
			 * dataJson.put("timestamp", ""); dataJson.put("startlimit", "0");
			 * dataJson.put("rid", "20"); finaljj.put("servicecode", "0006");
			 * finaljj.put("townshipid", "SDF67GH68J876586J87J78DRR");
			 * finaljj.put("societykey", "9QE6TYEVN3WC2WUHIL9Z5GK6K");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.skilled_directory_searchA;
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * /* //to test
			 * http://192.168.1.31:8080/socialindia/LaborUpdate?ivrservicefor
			 * -singleview dataJson=new JSONObject(); dataJson.put("rid",
			 * "902");// Current login id dataJson.put("lbrid", "138");
			 * dataJson.put("lbrserviceid", "KDH2UK0DFZ2LL5WUHTT5UDNVM");
			 * 
			 * dataJson.put("timestamp", ""); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("is_mobile","1");
			 * finaljj.put("servicefor", "3"); finaljj.put("data", dataJson);
			 * 
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.LabouraddList
			 * ; postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * //to test
			 * http://192.168.1.31:8080/socialindia/LaborUpdate?ivrservicefor
			 * delete dataJson=new JSONObject(); dataJson.put("rid", "1");//
			 * Current login id dataJson.put("lbrid", "11");
			 * dataJson.put("lbrserviceid", "0"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "R0128"); finaljj.put("townshipid",
			 * "6ZVG6UNCR4OR9TAFUPEFL6W2O"); finaljj.put("societykey",
			 * "L5NUFFK9FDID4OIH2U5ESJ2YT"); finaljj.put("is_mobile","1");
			 * finaljj.put("servicefor", "4"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * ="http://192.168.1.41:8080/socialindiaservices/lbrupdates";
			 * postdata=finaljj.toString();
			 * 
			 * /* //mobilabourinsert //------------Labor Insert dataJson=new
			 * JSONObject(); dataJson.put("name", "suresh.peninlog");
			 * dataJson.put("cost", "100"); dataJson.put("costper", "1");
			 * dataJson.put("experience", "3"); dataJson.put("emailid",
			 * "DqMsssW1@peninlog.com"); dataJson.put("isdcode", "91");
			 * dataJson.put("mobno", "92211642"); dataJson.put("cardtyp", "4");
			 * dataJson.put("cardno", "4xxQQ4532WE"); dataJson.put("add1",
			 * "Nungambakkam"); dataJson.put("add2", "chennai");
			 * dataJson.put("postalcode", "107"); dataJson.put("city", "30290");
			 * dataJson.put("sate", "93"); dataJson.put("country", "102");
			 * dataJson.put("kycname", "55521dd.txt");
			 * dataJson.put("verifystatus", "1");// 0 - unverified, 1 - Verified
			 * dataJson.put("keyforsrch", "sprinter sales,");
			 * dataJson.put("wktypid", "4"); dataJson.put("descrp",
			 * "asdsad, sdsd s2132ad,sasadsadsad, sa sds,adsa,dsad,sad as,dsa");
			 * dataJson.put("imagename","Desert.jpg"); dataJson.put("entryby",
			 * "1");// New dataJson.put("currentloginid","3");// current login
			 * usr id dataJson.put("status","1"); JSONArray ja = new
			 * JSONArray(); JSONObject jobj =new JSONObject();
			 * jobj.put("cate_id","1"); jobj.put("skill_id","3"); ja.put(jobj);
			 * dataJson.put("cateoryJary", ja);
			 * 
			 * 
			 * 
			 * 
			 * //http://192.168.131:8080/socialindia/get_notification
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("timestamp", ""); dataJson.put("startrow",
			 * "0"); finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058"); finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile", "1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.get_notificationA;
			 * postdata=finaljj.toString();
			 * 
			 * /* //http://192.168.131:8080/socialindia/notification_read
			 * dataJson=new JSONObject(); dataJson.put("rid", "5");// Current
			 * login id dataJson.put("notification_id", "289");
			 * dataJson.put("read_status", "0"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "JN7EUUHYLNBJ19040GP3SCB2Z"); finaljj.put("societykey",
			 * "99BJ2INLNWZ9LXNZ1O2W3NO07"); finaljj.put("is_mobile", "1");
			 * finaljj.put("data", dataJson); srvcUrl=srverUrl_local+projetName+
			 * Servicetestmobidata.notification_readA;
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * //mobiDonationinsert //------------donatiion Insert dataJson=new
			 * JSONObject(); dataJson.put("Donation_id", "");
			 * dataJson.put("category_id", "1"); dataJson.put("subcategory_id",
			 * "1"); dataJson.put("title", "suresh donation money");
			 * dataJson.put("quantity", "3"); dataJson.put("item_type",
			 * "DqMsssW1@peninlog.com"); dataJson.put("other_desc", "92211642");
			 * dataJson.put("desc",
			 * "asdsad, sdsd s2132ad,sasadsadsad, sa sds,adsa,dsad,sad as,dsa");
			 * dataJson.put("imagename","Desert.jpg");
			 * dataJson.put("donate_to","1"); dataJson.put("entryby", "5");//
			 * New dataJson.put("rid","5");// current login usr id
			 * dataJson.put("status","1"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "R0124");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.DonationaddList;
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * // ------------------Feedback Insert per Labor/Merchant/admin
			 * 
			 * dataJson=new JSONObject(); dataJson.put("rid", "5");// Current
			 * login id dataJson.put("ratings", "5");
			 * dataJson.put("merchant_id", "1"); dataJson.put("email_id", "");
			 * dataJson.put("feedback_comments",
			 * "labour ddfdffdf good person And satisfy our work..");
			 * dataJson.put("islabour", "1");//0-merchant 1-labour finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .labour_customer_ratingA; postdata=finaljj.toString();
			 * 
			 * // ------------------Feedback Select All per Labor/Merchant/admin
			 * String sdtSearch=""; dataJson=new JSONObject();
			 * dataJson.put("laborfdbkflg", "laborfbdkview");
			 * dataJson.put("searchflg", ""); dataJson.put("fdbkforsocid", "1");
			 * dataJson.put("fdbkstatus", "1"); dataJson.put("countflg", "yes");
			 * dataJson.put("countfilterflg", "yes"); dataJson.put("startrow",
			 * "0");// starting row dataJson.put("totalrow", "1000");// total
			 * row dataJson.put("srchdtsrch", sdtSearch);
			 * dataJson.put("fdbkforuser", "14");//labor id / merchant id/
			 * admin/user id dataJson.put("fdbkforusergrp", "5");//labor group
			 * id / merchant group id/ admin group id/user group id
			 * dataJson.put("currentloginid", "5");// Current login id
			 * 
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "SI5000");
			 * finaljj.put("data", dataJson); finaljj.put("townshipid",
			 * "JN7EUUHYLNBJ19040GP3SCB2Z"); finaljj.put("societykey",
			 * "99BJ2INLNWZ9LXNZ1O2W3NO07"); finaljj.put("is_mobile","1");
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.fdbkviewall;
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * // ------------------Merchant_Laborreport_issues Select All
			 * sdtSearch=""; dataJson=new JSONObject();
			 * dataJson.put("lbrstatus", "1"); dataJson.put("grpcode",
			 * "8");//7-labor 8-merchant dataJson.put("countflg", "yes");
			 * dataJson.put("countfilterflg", "yes"); dataJson.put("startrow",
			 * "0");// starting row dataJson.put("totalrow", "10");// total row
			 * dataJson.put("srchdtsrch", sdtSearch);
			 * dataJson.put("currentloginid","3");// current login usr id
			 * 
			 * finaljj=new JSONObject(); finaljj.put("servicecode", "SI3005");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .labour_repor_issues; postdata=finaljj.toString();
			 * 
			 * //------------------Issue report mvp_labour_issue_tbl Select All
			 * per Labor/Merchant/admin dataJson.put("crntusrloginid","5");
			 * dataJson.put("mrchntId","1"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "SI3005"); finaljj.put("servicefor",
			 * "3"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName
			 * +Servicetestmobidata.fdbkviewall; postdata=finaljj.toString();
			 * //to test
			 * http://192.168.1.31:8080/socialindia/merchant_issue_posting
			 * dataJson=new JSONObject(); dataJson.put("rid", "5");// Current
			 * login id dataJson.put("merchant_id", "1");
			 * dataJson.put("merchant_issue_ids", "1,2,3,4");
			 * dataJson.put("email", ""); dataJson.put("text", "");
			 * dataJson.put("islabour", "1"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058"); finaljj.put("townshipid",
			 * "JN7EUUHYLNBJ19040GP3SCB2Z"); finaljj.put("societykey",
			 * "99BJ2INLNWZ9LXNZ1O2W3NO07"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.
			 * merchant_issue_postingA; postdata=finaljj.toString();
			 * 
			 * 
			 * 
			 * 
			 * //------------Labor Edit dataJson=new JSONObject();
			 * dataJson.put("lbrid", "28"); dataJson.put("name", "RAMCO25");
			 * dataJson.put("emailid", "RAMCO@peninlog.com");
			 * //dataJson.put("mobno", "9787103260"); //dataJson.put("cardtyp",
			 * "1"); //dataJson.put("cardno", "f22232312h323");
			 * 
			 * dataJson.put("add1", "Narasima iyer street, Near water tang,");
			 * dataJson.put("add2", "Kondayampalli [post]");
			 * dataJson.put("postalcode", "107"); dataJson.put("city", "30290");
			 * dataJson.put("sate", "93"); dataJson.put("country", "102");
			 * dataJson.put("rating", "5"); dataJson.put("kycname",
			 * "w55444d.txt");
			 * 
			 * dataJson.put("verifystatus", "1");// 0 - unverified, 1 - Verified
			 * dataJson.put("keyforsrch",
			 * "system, monitor, motherborad, ups, printer");
			 * dataJson.put("wktypid", "1"); dataJson.put("descrp",
			 * "asdsad, sdsd sad,sasadsadsad, sa sds,adsa,dsad,sad as,dsa");
			 * dataJson.put("imagename","Koala.jpg"); dataJson.put("entryby",
			 * "1"); dataJson.put("currentloginid","3");// current login usr id
			 * JSONArray ja = new JSONArray(); JSONObject jobj =new
			 * JSONObject(); jobj.put("cate_id","1"); jobj.put("skill_id","3");
			 * ja.put(jobj); dataJson.put("cateoryJary", ja); finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "SI3001");
			 * finaljj.put("servicefor", "2");// Edit finaljj.put("data",
			 * dataJson);
			 * 
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.LabouraddList
			 * ;
			 * 
			 * postdata=finaljj.toString();
			 * 
			 * 
			 * //to test
			 * http://192.168.1.41:8080/socialindia/donationcategorylist
			 * dataJson=new JSONObject(); dataJson.put("rid", "4");// Current
			 * login id dataJson.put("startlimit", "0");
			 * dataJson.put("timestamp", "2016-11-01 03:10:44"); finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.donationcategorylist
			 * ; postdata=finaljj.toString();
			 * 
			 * //labour details show =======
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .LabouraddList; postdata=finaljj.toString();
			 */

			/*
			 * //labour details show
			 * 
			 * dataJson = new JSONObject(); dataJson.put("lbrstatus", "1");
			 * dataJson.put("countflg", "yes"); dataJson.put("countfilterflg",
			 * "yes"); dataJson.put("startrow","0");// starting row
			 * dataJson.put("totalrow", "10");// overalltotal row
			 * dataJson.put("srchdtsrch", "");// overalltotal row
			 * dataJson.put("currentloginid", "4"); dataJson.put("timestamp",
			 * ""); dataJson.put("isskilledsearch", "1");
			 * dataJson.put("ismobileno", ""); dataJson.put("isservicebookno",
			 * ""); dataJson.put("iscatgid", "2"); dataJson.put("isworktype",
			 * ""); dataJson.put("isratting", ""); finaljj = new JSONObject();
			 * finaljj.put("servicecode", "SI3005"); finaljj.put("townshipid",
			 * "JN7EUUHYLNBJ19040GP3SCB2Z"); finaljj.put("societykey",
			 * "99BJ2INLNWZ9LXNZ1O2W3NO07"); finaljj.put("is_mobile","1");
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .lbrskilledselectall; postdata=finaljj.toString();
			 */

			/*
			 * //Transaction details (detailed and mini)
			 * //http://192.168.131:8080/socialindia/transactionList dataJson =
			 * new JSONObject(); dataJson.put("reference_number", "");
			 * dataJson.put("timestamp", ""); dataJson.put("startlimit", "");
			 * dataJson.put("payment_status","");// starting row
			 * dataJson.put("from_date", "2017-01-16 03:21:19");// overalltotal
			 * row dataJson.put("to_date", "2017-01-19 03:21:19");//
			 * overalltotal row dataJson.put("service_type", "");
			 * dataJson.put("rid", "4"); dataJson.put("statement_type", "1");
			 * finaljj = new JSONObject(); finaljj.put("servicecode", "3005");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.transactionListA;
			 * postdata=finaljj.toString();
			 */

			// Transaction details (detailed and mini)

			/*
			 * //http://192.168.131:8080/socialindia/transactionList dataJson =
			 * new JSONObject(); dataJson.put("rid", "4");
			 * dataJson.put("usrTyp", "5"); dataJson.put("docTypId", "8");
			 * JSONArray shareIds=new JSONArray(); shareIds.put(1);
			 * shareIds.put(2); shareIds.put(3); shareIds.put(4);
			 * dataJson.put("docShareId", shareIds);
			 * dataJson.put("docSubFolder", "2"); dataJson.put("subject",
			 * "test subject"); dataJson.put("descr", "desc sddf ");
			 * dataJson.put("emailStatus", "1"); finaljj = new JSONObject();
			 * finaljj.put("servicecode", "3005"); finaljj.put("townshipid",
			 * "JN7EUUHYLNBJ19040GP3SCB2Z"); finaljj.put("societykey",
			 * "99BJ2INLNWZ9LXNZ1O2W3NO07"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.generalDocumentA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //Service booking dataJson=new JSONObject();
			 * dataJson.put("labor_id", "1"); dataJson.put("problem",
			 * "dddddf "); dataJson.put("prefered_datetime",
			 * "2017-01-24 18:23:49 - 20:23:53"); dataJson.put("rid","4");//
			 * current login usr id finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0106"); finaljj.put("townshipid",
			 * "SDF67GH68J876586J87J78DRR"); finaljj.put("societykey",
			 * "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * 
			 * finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata
			 * .service_booking_new; postdata=finaljj.toString();
			 */

			/*
			 * //------------donatiion delete dataJson=new JSONObject();
			 * dataJson.put("Donation_id", "8"); // dataJson.put("feed_typ",
			 * "4"); dataJson.put("rid","5");// current login usr id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.Donation_Delete;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //mobiDonationinsert //------------donatiion Insert dataJson=new
			 * JSONObject(); dataJson.put("Donation_id", "");
			 * dataJson.put("category_id", "64"); dataJson.put("subcategory_id",
			 * "1831"); dataJson.put("title",
			 * " donationzzz aaxxcccvvaasssxxccvb ghjhj ");
			 * dataJson.put("quantity", "11"); dataJson.put("item_type", "1");
			 * dataJson.put("other_desc", "92211111642"); dataJson.put("desc",
			 * "asdsad, sdsd s2132ad,sasadsadsad, sa sds,adsa,dsad,sad as,dsa");
			 * dataJson.put("donate_to","1"); dataJson.put("entryby", "5");//
			 * New dataJson.put("rid","5");// current login usr id
			 * dataJson.put("status","1"); finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.DonationaddList;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/send_message
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("group_contact", "2");
			 * dataJson.put("group_contact_id", "1"); dataJson.put("msgtype",
			 * "1"); dataJson.put("content", "fsdfsd sfdsdf");
			 * dataJson.put("rid","5");// current login usr id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.sendMsgA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/create_rename_group
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("group_name", "fsfsdfs"); JSONArray jsar=new
			 * JSONArray(); jsar.put(1); jsar.put(2); jsar.put(3);
			 * dataJson.put("member_id", jsar); dataJson.put("group_id", "");
			 * dataJson.put("rid","5");// current login usr id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.createGroupA;
			 * postdata=finaljj.toString();
			 * System.out.println("postdata-----------------"+postdata);
			 */

			/*
			 * //http://192.168.131:8080/socialindia/delete_group
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("group_id", "1"); dataJson.put("rid","5");// current
			 * login usr id finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.deleteGroupA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/update_group_user
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("group_id", "1"); dataJson.put("update_type", "2");
			 * JSONArray jsar=new JSONArray(); jsar.put("1"); jsar.put("2");
			 * dataJson.put("member_id", jsar); dataJson.put("rid","5");//
			 * current login usr id finaljj=new JSONObject();
			 * finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.update_group_userA
			 * ; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/search_group_contact
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("group_contact", "2"); dataJson.put("search_text",
			 * "sadas"); dataJson.put("timestamp", "");
			 * dataJson.put("startlimit", "0"); JSONArray jsar=new JSONArray();
			 * jsar.put("1"); jsar.put("2"); dataJson.put("member_id", jsar);
			 * dataJson.put("rid","5");// current login usr id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.listGrpMsgA;
			 * postdata=finaljj.toString();
			 */

			// http://192.168.131:8080/socialindia/chat_history
			// ------------send message Insert
			/*dataJson = new JSONObject();
			dataJson.put("group_contact", "2");
			dataJson.put("group_contact_id", "4");
			dataJson.put("timestamp", "");
			dataJson.put("startlimit", "0");
			dataJson.put("rid", "7");// current login usr id
			finaljj = new JSONObject();
			finaljj.put("servicecode", "0058");

			finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			finaljj.put("is_mobile", "1");
			finaljj.put("data", dataJson);
			srvcUrl = srverUrl_local + projetName
					+ Servicetestmobidata.listMsgA;
			postdata = finaljj.toString();
*/
			/*
			 * //http://192.168.131:8080/socialindia/searchContact
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("chat_type", "3"); dataJson.put("search", "");
			 * dataJson.put("timestamp", ""); dataJson.put("startlimit", "0");
			 * dataJson.put("rid","5");// current login usr id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.searchContactA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/privacy_users
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("chat_type", "3"); dataJson.put("search", "");
			 * dataJson.put("timestamp", ""); dataJson.put("startlimit", "0");
			 * dataJson.put("rid","5");// current login usr id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.searchContactA;
			 * postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/notification_send_list
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("startrow", "0"); dataJson.put("autoload", "");
			 * dataJson.put("timestamp", ""); dataJson.put("rid","7");// current
			 * login usr id finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl=srverUrl_local+projetName+Servicetestmobidata.
			 * notification_send_listA; postdata=finaljj.toString();
			 */

			/*
			 * //http://192.168.131:8080/socialindia/notification_send_list
			 * //------------send message Insert dataJson=new JSONObject();
			 * dataJson.put("startlimit", "0"); dataJson.put("timestamp", "");
			 * dataJson.put("title", ""); dataJson.put("rid","7");// current
			 * login usr id finaljj=new JSONObject(); finaljj.put("servicecode",
			 * "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.socialsearchA;
			 * postdata=finaljj.toString();
			 */

			// http://192.168.131:8080/socialindia/flashnewslist
			// ------------send message Insert
			/*
			 * dataJson=new JSONObject(); dataJson.put("society_id", "1");
			 * dataJson.put("rid","7");// current login usr id finaljj=new
			 * JSONObject(); finaljj.put("servicecode", "0058");
			 * 
			 * finaljj.put("townshipid", "JN7EUUHYLNBJ19040GP3SCB2Z");
			 * finaljj.put("societykey", "99BJ2INLNWZ9LXNZ1O2W3NO07");
			 * finaljj.put("is_mobile","1"); finaljj.put("data", dataJson);
			 * srvcUrl
			 * =srverUrl_local+projetName+Servicetestmobidata.flashnewslistA;
			 * postdata=finaljj.toString();
			 */

			String postparam = "ivrparams=";
			String postparamval = EncDecrypt.encrypt(postdata);
			// String postparamval=postdata;
			System.out.println(postparam + postdata);
			System.out.println(srvcUrl + postparam + URLEncoder.encode(postparamval));
			toCheckService(postparam + URLEncoder.encode(postparamval), srvcUrl);

		} catch (Exception eee) {
		} finally {
		}

	}

	/*
	 * public static void toZIPUNGIP(){ String
	 * fromPathFile="E:/FileMoveTest/fromimg/Koala.jpg"; File nn=new
	 * File(fromPathFile); byte imgByt[]=new byte[1024]; imgByt =
	 * Commonutility.toReadFiletoBytes(nn); String deencdatt =
	 * Commonutility.toByteAryToBase64EncodeStr(imgByt);
	 * System.out.println(deencdatt); byte imgbytee[] =
	 * Commonutility.toDecodeB64StrtoByary(deencdatt);
	 * 
	 * System.out.println(imgbytee.toString());
	 * 
	 * }
	 */

	/*
	 * public static void toTestt(){
	 * 
	 * try{ HashMap hm=new HashMap(); hm.put("1", "Java"); hm.put("2",
	 * "Oracle"); hm.put("3", "jsp"); List lll=new ArrayList();
	 * lll.add(1);lll.add("prabha"); List ll2=new ArrayList();
	 * ll2.add(2);ll2.add("kanna"); List ll3=new ArrayList();
	 * ll3.add(3);ll3.add("suresh");
	 * 
	 * List fina=new ArrayList(); fina.add(lll); fina.add(ll2); fina.add(ll3);
	 * 
	 * JSONObject jj=new JSONObject(); jj.put("hmap", hm); jj.put("lss", fina);
	 * System.out.println(jj); JSONArray lineItems = jj.getJSONArray("lss"); for
	 * (int i = 0; i < lineItems.length(); i++) { JSONArray
	 * temm=lineItems.getJSONArray(i); int indx=(Integer)temm.get(0); String
	 * vl=(String)temm.get(1);
	 * System.out.println("<option value=\""+indx+"\">"+vl+"</option>"); }
	 * }catch(Exception e){ System.out.println("xception e : "+e); }finally{} }
	 */
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

			httpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpConn.setRequestProperty("charset", "utf-8");
			httpConn.setRequestProperty("Content-Length",
					"" + Integer.toString(locWrtnData.getBytes().length));
			httpConn.setUseCaches(false);
			locvOutStrmObj = httpConn.getOutputStream();
			locvOutStrmObj.write(locWrtnData.getBytes("UTF-8"));
			locvOutStrmObj.close();
			locvOutStrmObj = null;
			// httpConn.setUseCaches(false);
			// httpConn.setConnectTimeout(5000);
			httpConn.connect();
			locISReadr = new InputStreamReader(httpConn.getInputStream());
			locBfReadr = new BufferedReader(locISReadr);
			String inputLine = null;
			while ((inputLine = locBfReadr.readLine()) != null) {
				lSRtnVal += inputLine + "\n";
			}
			System.out.println("---------Response[Start]--------------");
			System.out.println(lSRtnVal);
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
