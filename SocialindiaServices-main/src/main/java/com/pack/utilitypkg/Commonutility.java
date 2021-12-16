package com.pack.utilitypkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;


public class Commonutility {

	public static Date olddate=null;
	public static String filename = "";
	
	static ResourceBundle ivrRsbundelappresorce = ResourceBundle.getBundle("applicationResources");

	public static Boolean toCheckIsJSON(String pStrval) {
		boolean iVBrst = false;
		if (pStrval != null && pStrval.startsWith("{") && pStrval.endsWith("}")) {
			iVBrst = true;
		} else {
			iVBrst = false;
		}
		return iVBrst;
	}

	public static Object toHasChkJsonRtnValObj(JSONObject pJsonObjVal, String pJsonKey) {// to check json object key to has() method
		Object locObj = null;
		try {
			if (pJsonObjVal != null && pJsonObjVal.has(pJsonKey)) {
				locObj = pJsonObjVal.get(pJsonKey);
			} else {
				locObj = null;
			}
		} catch (Exception e) {
			locObj = null;
		} finally {
			pJsonObjVal = null;
			pJsonKey = null;
		}
		return locObj;
	}

	public static String toCheckNullEmpty(Object pChkval) {
		if (pChkval != null) {
			if (!pChkval.toString().equalsIgnoreCase("null") && !pChkval.toString().equalsIgnoreCase("")) {

				if (toCheckisNumeric(String.valueOf(pChkval))) {
					return String.valueOf(pChkval);
				} else {
					return pChkval.toString();
				}

			} else {
				return "";
			}
		} else {
			return "";
		}

	}

	public static String toGetcurrentutilldatetime(String pFmt) {
		if (pFmt != null && pFmt.equalsIgnoreCase("1")) {
			SimpleDateFormat locSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return locSdf.format(new Date());
		} else if (pFmt != null && pFmt.equalsIgnoreCase("2")) {
			SimpleDateFormat locSdf = new SimpleDateFormat("yyyy-MM-dd");
			return locSdf.format(new Date());
		} else {
			SimpleDateFormat locSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return locSdf.format(new Date());
		}

	}

	public static boolean toCheckisNumeric(String pNmckval) {
		try {
			Integer.parseInt(pNmckval);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String toJSONArraytoString(JSONArray pJSONAryob) {
		// JSONArray lineItems = jj.getJSONArray("lss");
		String fin = "";
		for (int i = 0; i < pJSONAryob.length(); i++) {
			JSONArray temm;
			try {
				temm = pJSONAryob.getJSONArray(i);
				int indx = (Integer) temm.get(0);
				String vl = (String) temm.get(1);
				fin += "<option value=\"" + indx + "\">" + vl + "</option>";
				System.out.println("<option value=\"" + indx + "\">" + vl
						+ "</option>");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}

		}
		return fin;
	}

	public static void crdDir(String dirPathandName) {
		File loc_nFile = null;
		File loc_nFile_fcrt = null;
		String fName = "";
		String pathh = "";
		try {
			if (dirPathandName.contains(".")) {
				fName = dirPathandName.substring(
						dirPathandName.lastIndexOf("/"),
						dirPathandName.length());
				pathh = dirPathandName.substring(0,
						dirPathandName.lastIndexOf("/"));
			} else {
				pathh = dirPathandName;
			}
			loc_nFile = new File(pathh);
			if (loc_nFile.exists()) {
				if (loc_nFile.isDirectory()) {
					System.out.println("Directory already Exists");
				} else {
					loc_nFile.mkdirs();
					System.out.println("Directory Created Successfully");
				}
			} else {
				loc_nFile.mkdirs();
				System.out.println("Directory Created Successfully");
			}
			if (!fName.equalsIgnoreCase("")) {
				loc_nFile_fcrt = new File(pathh + "/" + fName);
				if (!loc_nFile_fcrt.exists()) {
					loc_nFile_fcrt.createNewFile();
					System.out.println("File Created Successfully");
				} else {
					System.out.println("File already Exists");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception found in Utilities crdDir() : " + e);
		} finally {
		}
	}

	public static byte[] toReadFiletoBytes(File filepath) {// read file to byte
		byte toredbyts[] = null;
		byte[] toreturnBytes = null;
		FileInputStream fins = null;
		ByteArrayOutputStream bout = null;
		try {
			System.out.println("Image Size : " + filepath.length() + " bytes");
			toredbyts = new byte[(int) filepath.length()];
			fins = new FileInputStream(filepath);
			bout = new ByteArrayOutputStream();
			for (int readNum; (readNum = fins.read(toredbyts)) != -1;) {
				bout.write(toredbyts, 0, readNum); // no doubt here is 0
			}
			toreturnBytes = bout.toByteArray();
		} catch (Exception e) {
		} finally {
			try {
				if (fins != null) {
					fins.close();
					fins = null;
				}
				if (bout != null) {
					bout.close();
					bout = null;
				}
			} catch (IOException e) {
			}
		}
		return toreturnBytes;
	}

	public static String toByteArraytoWriteFiles(byte[] inputstrm, String pFilepath, String pFilename) {// write byte to file
		FileOutputStream locFos = null;
		File locnewfile = null;
		String locRtnSts = null;
		try {
			if (pFilepath != null && !pFilepath.equalsIgnoreCase("null")
					&& pFilepath.contains(".")) {
				locnewfile = new File(pFilepath);
			} else {
				if (pFilename != null && !pFilename.equalsIgnoreCase("null")
						&& !pFilename.equalsIgnoreCase("")
						&& pFilename.contains(".")) {
					locnewfile = new File(pFilepath + "/" + pFilename);
				} else {
					pFilename = new SimpleDateFormat("yyyyMMddHHmmss")
							.format(new Date());
					locnewfile = new File(pFilepath + "/" + pFilename);
				}
			}

			if (!locnewfile.exists()) {
				Commonutility.crdDir(locnewfile.getParent() + "/"
						+ locnewfile.getName());
				locFos = new FileOutputStream(locnewfile);
				locFos.write(inputstrm);
				locFos.flush();
				// locFos.close();
				locRtnSts = "success";
			} else {
				System.out.println("Path : " + locnewfile.getParent()
						+ "\n Name : " + locnewfile.getName());
				String locDltsts = todelete(locnewfile.getName(),
						locnewfile.getParent());
				locFos = new FileOutputStream(locnewfile);
				locFos.write(inputstrm);
				locFos.flush();
				// locFos.close();
				locRtnSts = "success";
			}
		} catch (Exception e) {
			System.out.println("Exception found in Commonutlity.java toByteArraytoWriteFiles : "
							+ e);
			locRtnSts = "success";
		} finally {
			try {
				if (locnewfile != null) {
					locnewfile = null;
				}
				if (locFos != null) {
					locFos.close();
					locFos = null;
				}
			} catch (IOException e) {
			}
		}
		return locRtnSts;
	}

	public static String todelete(String pFileName, String pFilepath) {
		System.out.println("------|Image Delete|------");
		String indexpath = pFilepath + "/";
		if (pFileName != null && !pFileName.equalsIgnoreCase("null")
				&& !pFileName.equalsIgnoreCase("")) {
			indexpath += pFileName;
		} else {
		}
		File imgfile = new File(indexpath);
		try {
			if (imgfile.exists()) {
				if (imgfile.isDirectory()) {
					toClearFolder(indexpath);
				}
				if (imgfile.isFile()) {
					imgfile.delete();
				}
				System.out
						.println("--------Image file deleted successfully--------");
			} else {
				System.out.println("------File name not found-------");
			}
		} catch (Exception e) {
			System.out
					.println("Exception Found in [todelete commonutility.java] : "
							+ e);
		}
		return "Success fully deleted";
	}

	public static String toFirstCharUpper(String changedstr) {
		if (changedstr != null && changedstr.length() > 0) { // changedstr =
																// changedstr.toLowerCase();
			changedstr = Character.toString(changedstr.charAt(0)).toUpperCase()
					+ changedstr.substring(1);
		} else {
		}
		return changedstr;
	}

	public static String toClearFolder(String srcpath) {
		File imgfldr = new File(srcpath);
		/*
		 * File totFile[] = imgfldr.listFiles(); if (totFile != null) { for (int
		 * incc = 0; incc < totFile.length; incc++) { totFile[incc].delete(); }
		 * }
		 */
		String[] entries = imgfldr.list();
		for (String s : entries) {
			File currentFile = new File(imgfldr.getPath(), s);
			currentFile.delete();
		}
		return "success";
	}

	public static String toByteAryToBase64EncodeStr(byte[] pFileBytes) {
		String locRtnEncodeStr = null;
		try {
			locRtnEncodeStr = Base64.encodeBase64String(pFileBytes);
		} catch (Exception e) {
			System.out.println("Exception toByteAryToBase64EncodeStr() : " + e);
		} finally {
			pFileBytes = null;
		}
		return locRtnEncodeStr;
	}

	public static byte[] toDecodeB64StrtoByary(String pbase64Str) {
		byte locRtnEncodeStr[] = null;
		try {
			locRtnEncodeStr = Base64.decodeBase64(pbase64Str);
		} catch (Exception e) {
			System.out.println("Exception toByteAryToBase64EncodeStr() : " + e);
		} finally {
		}
		return locRtnEncodeStr;
	}

	public static String toFileMoving(String srcpath, String distnationpath,
			String filename, String deletornotflg) {
		// deletornotflg -- image delet from srvh path flg
		// FileMoving(E:\DocumentFldr,E:\DocumentUpdateFldr,Parthasarathy.doc,Remove)
		// // Remove/ Not remove
		// toFileMoving(externalpath+\imgefldepath\postedusid,externalpath+\imgefldepath\currentusid,filenameonlu.txt,"notremove")
		// // Remove/ Not remove
		System.out.println(" :: File Moving :: ");
		File newfile = null;
		File distnationfldr = null;
		File lcMovefile = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		boolean res = false;
		int Pubfilebackchk = 0;
		String RsId[] = new String[0];
		try {
			if (filename.length() > 0 && filename != null
					&& !filename.equalsIgnoreCase("null")) {
				if (filename.contains("<fileback>")) {
					RsId = filename.split("<fileback>");
					filename = RsId[0];
					lcMovefile = new File(srcpath + "/" + filename);
					Pubfilebackchk = 1;
				} else {
					lcMovefile = new File(srcpath + "/" + filename);
				}
				Commonutility.crdDir(srcpath);// create folder
				System.out.println(" File Moving from : " + srcpath);
				System.out.println(" File Moving to : " + distnationpath);
				if (lcMovefile.exists()) {
					distnationfldr = new File(distnationpath);
					if (!distnationfldr.exists()) {
						distnationfldr.mkdirs();
					}
					if (Pubfilebackchk == 1) {
						int extindex = filename.lastIndexOf(".");
						String Extention = filename.substring(extindex + 1);
						filename = filename.substring(0, extindex);
						filename = filename + "_" + RsId[1] + "." + Extention;
						newfile = new File(distnationpath + "/" + filename);
					} else {
						newfile = new File(distnationpath + "/" + filename);
					}
					if (!newfile.exists()) {
						newfile.createNewFile();
						fis = new FileInputStream(lcMovefile);
						fos = new FileOutputStream(newfile);
						byte byt[] = new byte[1024];
						int len = 0;
						while ((len = fis.read(byt)) > 0) {
							fos.write(byt, 0, len);
						}
						fos.flush();
						fis.close();
						fos.close();
						if (deletornotflg.equalsIgnoreCase("Remove")) {
							res = lcMovefile.delete();
						} else {
							res = true;
						}
						System.out.println(" Move Result : " + res + "\n");
					} else {
						res = true;
					}
				} else {
					System.out
							.println("-------Folder path doesnot exist. outer-------");
				}
				if (res) {
					return "Success Move";
				} else {
					return "Not Move";
				}
			} else {
				File srchpath = new File(srcpath);
				File dstfldr = new File(distnationpath);
				if (!dstfldr.exists()) {
					dstfldr.mkdirs();
				}
				File distFile = null;
				// System.out.println("eeeeeee : " + srchpath.isDirectory());
				if (srchpath.isDirectory()) {
					File srcLstFiles[] = srchpath.listFiles();
					System.out
							.println("Before Distination Folder no. of files : "
									+ dstfldr.listFiles().length);
					System.out.println("Source Folder no. of files : "
							+ srcLstFiles.length);
					if (srcLstFiles != null) {
						for (int i = 0; i < srcLstFiles.length; i++) {
							distFile = new File(dstfldr.getAbsolutePath() + "/"
									+ srcLstFiles[i].getName());
							if (!distFile.exists()) {
								distFile.createNewFile();
							}
							fis = new FileInputStream(
									srcLstFiles[i].getAbsolutePath());
							fos = new FileOutputStream(distFile);
							byte byt[] = new byte[1024];
							int len = 0;
							while ((len = fis.read(byt)) > 0) {
								fos.write(byt, 0, len);
							}
							fos.flush();
							fis.close();
							fos.close();
							if (deletornotflg.equalsIgnoreCase("Remove")) {
								res = srcLstFiles[i].delete();
							} else {
								res = true;
							}
						}
					}
				} else {
				}

				File AfterDstFldr = new File(distnationpath);
				System.out.println("After Distination Folder no of files : "
						+ AfterDstFldr.listFiles().length);
				if (res) {
					return "Success Move";
				} else {
					return "Not Move";
				}
			}
		} catch (Exception e) {
			System.out.println("Exception Found in [FileMove() Util.java] : "
					+ e);
			return "Not Move";
		} finally {
			newfile = null;
			distnationfldr = null;
			lcMovefile = null;
			fis = null;
			fos = null;
		}
	}

	public static String toGenerateID(String keyAlis, int pKeyid, String pNeedLength) { // 6
		String locRtnId = null;
		String locZeroApnd = "";
		int locNdLgth = 0;
		int needlght = 0;
		try {
			if (pNeedLength != null) {
				locNdLgth = Integer.parseInt(pNeedLength);
				needlght = locNdLgth - (keyAlis.length() + String.valueOf(pKeyid).length());
				int i = 1;
				while (i <= needlght) {
					locZeroApnd += "0";
					i++;
				}
				locRtnId = keyAlis + locZeroApnd + pKeyid;
			}
		} catch (Exception e) {
			System.out.println("Exception toGenerateID() : " + e);
		} finally {

		}
		return locRtnId;
	}

	public static String toGetKeyIDforTbl(String voName, String pRtnVoColumnName, String pGrpCode, String pKeyLength) {
		String locFrtn = null;
		try {
			CommonDao cmnObj = new CommonDao();
			String localis = (String) cmnObj.getuniqueColumnVal("GroupMasterTblVo", "groupAliasName", "groupCode", pGrpCode);
			// String locMaxofid="6";
			Object lvrObj = cmnObj.getuniqueColumnVal(voName, pRtnVoColumnName, "", "");
			int locMaxofid = 0 ;
			if(lvrObj!=null && Commonutility.toCheckisNumeric(String.valueOf(lvrObj))){
				 locMaxofid = (Integer) lvrObj;
			}
			locFrtn = toGenerateID(localis, (locMaxofid + 1), pKeyLength);
		} catch (Exception ee) {
			System.out.println("Exception found in toGetKeyIDforTbl() : " + ee);
		} finally {

		}
		return locFrtn;
	}

	public static Date toString2UtilDate(String pDate, String pSrcFrmt) {// string date  to util  date  object
		SimpleDateFormat locSrcFrmt = null;
		Date locDate = null;
		try {
			locSrcFrmt = new SimpleDateFormat(pSrcFrmt);
			locDate = locSrcFrmt.parse(pDate);
			return locDate;
		} catch (Exception e) {
			return locDate;
		} finally {

		}
	}

	public  String togetFullpath(String fname){
		String ee = getClass().getClassLoader().getResource(fname).getFile();
		return ee;
	}
	
	

	public static void smsGupShup(String mobnu, String msgwithtbplate){
	try {
		Date mydate = new Date(System.currentTimeMillis());
		String data = "";
		data += "method=sendMessage";
		data += "&userid=2000164228"; // your loginId
		data += "&password=" +URLEncoder.encode("SQ060LnN2", "UTF-8"); // your password
		data += "&msg=" + URLEncoder.encode(msgwithtbplate, "UTF-8");
		data += "&send_to=" + URLEncoder.encode(mobnu, "UTF-8"); // a valid 10 digit phone no.
		data += "&v=1.1" ;
		data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or "BINARY"
		data += "&auth_scheme=PLAIN";
		data += "&mask=TESTIN";
			
		/*URL url = new URL( "http://enterprise.smsgupshup.com/GatewayAPI/rest?" + data);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				buffer.append(line).append("\n");
			}System.out.println(buffer.toString());
			rd.close();
			conn.disconnect();
			*/
		System.out.println(readHttpUrl("http://enterprise.smsgupshup.com/GatewayAPI/rest?",data));
		//System.out.println(readHttpUrl("https://api.gupshup.io/sm/api/ent/sms/msg?",data));
		
			
			
		}
catch(Exception e){
e.printStackTrace();
}
}
	public static String readHttpUrl(String URL, String data) {
	StringBuffer response = null;	
	try {
		URL getpseudo = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) getpseudo
				.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		OutputStream locvOutStrmObj = null;
		locvOutStrmObj = con.getOutputStream();
		locvOutStrmObj.write(data.getBytes());
		locvOutStrmObj.close();
		locvOutStrmObj = null;
		int responsecode = con.getResponseCode();
		BufferedReader bfreader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		response = new StringBuffer();
		String ipline;
		while ((ipline = bfreader.readLine()) != null) {
			response.append(ipline);
		}
		bfreader.close();		

	} catch (Exception ex) {
		System.out.println("Exception Found SocialAction.jsonRequest() : " + ex);
	}
	return String.valueOf(response);

}
	
	public static void main(String rr[]) {
		perfeecnum();
		// String
		// keyidd=toGetKeyIDforTbl("TownshipMstTbl","max(townshipId)","6","6");
		// System.out.println(keyidd);
		 //System.out.println(toGeneraeeteID("R",78,"5"));
		
		// Send SMS GUPSUP PAI
		//smsGupShup("9894023135","This is text Message");
		
		//String getresponse = "success | 919962004888 | 3211840505130325298-13793071940592479";
		//String lvrRsp = getresponse.substring(0,getresponse.indexOf("|"));
		//String lvrRsperr = getresponse.toString().substring(getresponse.lastIndexOf("|")+1,getresponse.length()).trim();
		//System.out.println(lvrRsp);
		//System.out.println(lvrRsperr);
		
		//String path = new Commonutility().togetFullpath("apiversioninfo.properties").replaceAll("%20", " ");
		//System.out.println("path : "+path);
		//String rst = toWritepropertiesfile("D:/pl0019-eclipse workspace/socialindiaservices/src/apiversioninfo.properties","currentdbversion","2");
		//String rst = toWritepropertiesfile(path,"currentdbversion","2");
		//Commonutility.toWriteConsole("rst : "+ rst);
		//toGetJavaHeapMemory();
		/*

		JSONObject newJson = null;
		JSONObject strjson = null;
		byte imgByt[] = null;
		byte ttt[] = null;
		try {

			String fromPathFile = "E:/FileMoveTest/fromimg/IMG_20150212_163825.jpg";
			File nn = new File(fromPathFile);
			imgByt = new byte[(int) nn.length()];
			ttt = new byte[(int) nn.length()];
			String topath = "E:/FileMoveTest/toimg/";
			System.out.println("Start Time : "
					+ new SimpleDateFormat("yyyy - MM -dd hh:mm:ss")
							.format(new Date()));
			imgByt = toReadFiletoBytes(nn);

			// Byte array convert to base4 encode string, then put into JSON
			// System.out.println(toByteAryToBase64EncodeStr(imgByt));
			newJson = new JSONObject();
			newJson.put("imgg", toByteAryToBase64EncodeStr(imgByt));
			String imgStr = newJson.toString();

			// get base4 encode string from JSOn Then cover to byte
			strjson = new JSONObject(imgStr);
			String locImgstrr = (String) strjson.get("imgg");
			ttt = toDecodeB64StrtoByary(locImgstrr);

			String wrtsts = toByteArraytoWriteFiles(ttt, topath, "RPKNEW_2.jpg");
			System.out.println("End Time : "
					+ new SimpleDateFormat("yyyy - MM -dd hh:mm:ss")
							.format(new Date()));

			// image compresss
			System.out.println("image Write : " + wrtsts);
			String limgSourcePath = fromPathFile;
			String limgDisPath = "E:/FileMoveTest/toimg/compress/";
			String limgName = "RPK_mobile_iconview";
			String limageFomat = "png";
			String limageFor = "all";
			int lneedWidth = 200;
			int lneedHeight = 180;
			ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, // 130 // is // best // for // mobile

		} catch (Exception e) {
			System.out.println("Main : " + e);
		} finally {
			newJson = null;
			strjson = null;
			imgByt = null;
			ttt = null;
		}*/
	}

	public static String toWriteImgWebAndMob(int pLastinsertid, String pImgName, String imgData, String pWebimgpath, String pMobimpath, String pWidth, String pHeight, Log pLog, Class pClass) {
		String lvrRtn = null;
		byte imgbytee[] = null;
		try {
			if ((imgData != null && !imgData.equalsIgnoreCase("null") && !imgData.equalsIgnoreCase("")) && (pImgName != null && !pImgName.equalsIgnoreCase("") && !pImgName.equalsIgnoreCase("null"))) {
				pLog.logMessage("Step 1 : Image write start.", "info", pClass);
				
				// image write into folder - byte wise image old one
				imgbytee = Commonutility.toDecodeB64StrtoByary(imgData);
				String wrtsts = Commonutility.toByteArraytoWriteFiles(imgbytee,pWebimgpath + pLastinsertid + "/", pImgName);
				
				/*// Appache util image / file move
				Commonutility.toWriteConsole("File Upload Start");	
				File lvrimgSrchPathFileobj = new File(imgData);// source path
				Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
				File lvrFileToCreate = new File (pWebimgpath+pLastinsertid+"/",pImgName);  
				Commonutility.toWriteConsole("lvrFileToCreate : "+lvrimgSrchPathFileobj.getAbsolutePath());						
		        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file 
				*/
								
				// mobile - small image
				String limgSourcePath = pWebimgpath + pLastinsertid + "/" + pImgName;
				String limgDisPath = pMobimpath + pLastinsertid + "/";
				String limgName = FilenameUtils.getBaseName(pImgName);
				String limageFomat = FilenameUtils.getExtension(pImgName);
				String limageFor = "all";

				//int lneedWidth = 200;
				//int lneedHeight = 180;

				int lneedWidth = Commonutility.stringToInteger(pWidth);
				int lneedHeight = Commonutility.stringToInteger(pHeight);
				ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
				pLog.logMessage("Step 2 : Image write end.", "info", pClass);
			} else {
				pLog.logMessage("Step 1 : Image Not Found For Write.", "info",
						pClass);
			}
			lvrRtn = "Success";
			return lvrRtn;
		} catch (Exception e) {
			System.out.println("Step -1 : Exception Found in image write " + e);
			pLog.logMessage("Step -1 : Exception Found in image write " + e,
					"error", pClass);
			lvrRtn = "error";
			return lvrRtn;
		} finally {

		}

	}
	
	
	public static String toWriteImageMobWebNewUtill(Integer pLastinsertid, String pImgName, String imgSourcepath, String pWebimgpath, String pMobimpath, String pWidth, String pHeight, Log pLog, Class pClass) {
		String lvrRtn = null;
		byte imgbytee[] = null;
		try {
			if ((imgSourcepath != null && !imgSourcepath.equalsIgnoreCase("null") && !imgSourcepath.equalsIgnoreCase("")) && (pImgName != null && !pImgName.equalsIgnoreCase("") && !pImgName.equalsIgnoreCase("null"))) {
				pLog.logMessage("Step 1 : Image write start.", "info", pClass);
				String lvcrntidstr = "";
				if(pLastinsertid==null){
					lvcrntidstr = "";
				} else{
					lvcrntidstr = String.valueOf(pLastinsertid);
				}
				/*// image write into folder - byte wise image old one
					imgbytee = Commonutility.toDecodeB64StrtoByary(imgData);
					String wrtsts = Commonutility.toByteArraytoWriteFiles(imgbytee,pWebimgpath + pLastinsertid + "/", pImgName);
				*/
				// Appache util image / file move
				Commonutility.toWriteConsole("File Upload Start");	
				File lvrimgSrchPathFileobj = new File(imgSourcepath);// source path
				Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
				File lvrFileToCreate = new File (pWebimgpath+lvcrntidstr+"/",pImgName);  
				Commonutility.toWriteConsole("lvrFileToCreate : "+lvrFileToCreate.getAbsolutePath());						
		        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file 
				
								
				// mobile - small image
				String limgSourcePath = pWebimgpath + lvcrntidstr + "/" + pImgName;
				String limgDisPath = pMobimpath + lvcrntidstr + "/";
				String limgName = FilenameUtils.getBaseName(pImgName);
				String limageFomat = FilenameUtils.getExtension(pImgName);
				String limageFor = "all";

				//int lneedWidth = 200;
				//int lneedHeight = 180;
				if(Commonutility.toCheckIsImgfrmt(limageFomat)){
					int lneedWidth = Commonutility.stringToInteger(pWidth);
					int lneedHeight = Commonutility.stringToInteger(pHeight);
					ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
					pLog.logMessage("Step 2 : Small Image Created.", "info", pClass);
				} else {
					pLog.logMessage("Step 2 : It is not image file : "+ limgName +"."+limageFomat, "info", pClass);
				}
				pLog.logMessage("Step 3 : Image write end.", "info", pClass);
				
			} else {
				pLog.logMessage("Step 1 : Image Not Found For Write.", "info",
						pClass);
			}
			lvrRtn = "Success";
			return lvrRtn;
		} catch (Exception e) {
			System.out.println("Step -1 : Exception Found in image write " + e);
			pLog.logMessage("Step -1 : Exception Found in image write " + e,"error", pClass);
			lvrRtn = "error";
			return lvrRtn;
		} finally {

		}

	}

	public static String toGetCurrentDatetime(String pFmtType) {
		CommonUtils lvrCmutil = null;
		String lvrRtnStr = null;
		try {
			lvrCmutil = new CommonUtilsDao();
			lvrRtnStr = lvrCmutil.getStrCurrentDateTime(pFmtType);
		} catch (Exception e) {
			lvrRtnStr = null;
		} finally {
			lvrCmutil = null;
		}
		return lvrRtnStr;
	}

	public static String printGCStats() {
		 long totalGarbageCollections = 0;
		 long garbageCollectionTime = 0;
		 for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
		 long count = gc.getCollectionCount();

		 if (count >= 0) {
		 totalGarbageCollections += count;
		 }

		 long time = gc.getCollectionTime();

		 if (time >= 0) {
		 garbageCollectionTime += time;
		 }
		 }
		 return "Garbage Collections: " + totalGarbageCollections +"\n" +
		 "Garbage Collection Time (ms): " + garbageCollectionTime;
		}

	public static void toGetJavaHeapMemory() {
		try {
			int mb = 1024 * 1024;

			// Getting the runtime reference from system
			Runtime runtime = Runtime.getRuntime();

			System.out.println("##### Heap utilization statistics [MB] #####");

			// Print used memory
			System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);

			// Print free memory
			System.out.println("Free Memory:" + runtime.freeMemory() / mb);

			// Print total available memory
			System.out.println("Total Memory:" + runtime.totalMemory() / mb);

			// Print Maximum available memory
			System.out.println("Max Memory:" + runtime.maxMemory() / mb);


			long diskSize = new File("/").getTotalSpace();
	        String userName = System.getProperty("user.name");
	        long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();

	        System.out.println("Size of C: "+diskSize+" Bytes");

	        System.out.println("User Name : "+userName);

	        System.out.println("RAM Size : "+memorySize+" Bytes");

	        System.out.println("RAM Size : "+(memorySize / mb)+" MB");

	        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	        long physicalMemorySize = os.getTotalPhysicalMemorySize();
	        long freePhysicalMemory = os.getFreePhysicalMemorySize();
	        long freeSwapSize = os.getFreeSwapSpaceSize();
	        long commitedVirtualMemorySize = os.getCommittedVirtualMemorySize();
	        String cplod = os.getName();
	        System.out.println("physicalMemorySize : "+(physicalMemorySize / mb)+" MB");
	        System.out.println("freePhysicalMemory : "+(freePhysicalMemory / mb)+" MB");
	        System.out.println("freeSwapSize : "+(freeSwapSize / mb)+" MB");
	        System.out.println("commitedVirtualMemorySize : "+(commitedVirtualMemorySize / mb)+" MB");
	        System.out.println("Used Ram : "+((physicalMemorySize - freePhysicalMemory) / mb));

		} catch (Exception e) {

		} finally {

		}
	}

	public static String getPropertyValue(String pFname, String key) { // get value  from properties
		String kVal = "";
		FileInputStream fis = null;
		Properties prop = null;
		try {
			prop = new Properties();
			fis = new FileInputStream(pFname);
			prop.load(fis);
			kVal = prop.getProperty(key);
			if (kVal == null || kVal.equalsIgnoreCase("")) {
				kVal = key;
			}
		} catch (Exception ex) {
			kVal = "";
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
				if (prop != null) {
					prop.clear();
					prop = null;
				}
			} catch (Exception ex) {
			}
		}
		return kVal;
	}

	public static boolean checkempty(String str) {
		boolean retval = false;
		try {
			if (str != null) {
				str = str.trim();
				if (!str.equalsIgnoreCase("") && !str.equalsIgnoreCase("null")) {
					retval = true;
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception found in checkEmpty:" + ex);
		}
		return retval;
	}
	public static boolean checkIntempty(Integer val) {
		boolean retval = false;
		try {
			if (val != null && val != 0) {
				retval = true;
			}
		} catch (Exception ex) {
			System.out.println("Exception found in checkEmpty integer:" + ex);
		}
		return retval;
	}
	public static boolean checkIntnull(Integer val) {
		boolean retval = false;
		try {
			if (val != null) {
				retval = true;
			}
		} catch (Exception ex) {
			System.out.println("Exception found in checkIntnull integer:" + ex);
		}
		return retval;
	}
	public static boolean checknull(String str) {
		boolean retval = false;
		try {
			if (str != null) {
				if (!str.equalsIgnoreCase("null")) {
					retval = true;
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception found in checkEmpty:" + ex);
		}
		return retval;
	}

	public static boolean checkLengthNotZero(String str) {
		boolean retval = false;
		try {
			str = str.trim();
			if (str.length() != 0) {
				retval = true;
			}
		} catch (Exception ex) {
			System.out.println("Exception found in checkLengthNotZero:" + ex);
		}
		return retval;
	}

	public static int stringToInteger(String data) {
		int retval = 0;
		try {
			if (data != null) {
				data = data.trim();
				if (!data.equalsIgnoreCase("")
						&& !data.equalsIgnoreCase("null")) {
					retval = Integer.parseInt(data);
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception found in stringToInteger :" + ex);
			retval = 0;
		}
		return retval;
	}

	public static String validateErrmsgForm(String data) {
		try {
			if (checkempty(data)) {
				data = data + "<SP>";
			} else {
				data = "";
			}
		} catch (Exception ex) {
			System.out.println("Exception found in validateErrmsgForm" + ex);
		}
		return data;
	}
	public static String spTagAdd(String data) {
		try {
			if (checkempty(data)) {
				data = data + "<SP>";
			} else {
				data = "";
			}
		} catch (Exception ex) {
			System.out.println("Exception found in spTagAdd" + ex);
		}
		return data;
	}

	public static String removeSPtag(String data) {
		try {
			if (checkempty(data)) {
				if (data.contains("<SP>")) {
					data = data.substring(0, data.length() - 4);
				}
			} else {
				data = "";
			}
		} catch (Exception ex) {
			System.out.println("Exception found in validateErrmsgForm" + ex);
		}
		return data;
	}

	public static String insertchkstringnull(String data) {
		try {
			if (!checkempty(data)) {
				data = null;
			}
		} catch (Exception ex) {
			System.out.println("Exception found in:" + ex);
			data = null;
		}
		return data;
	}

	public static Integer insertchkintnull(Integer data) {
		try {
			if (data == null || data == 0 ) {
				data = null;
			}
		} catch (Exception ex) {
			System.out.println("Exception found in:" + ex);
			data = null;
		}
		return data;
	}

	public static String filesizeget(String filepath) {
		String retfilesize = "";
		try {
			File fileSize = new File(filepath);
			long filesizebyte = fileSize.getTotalSpace();
			retfilesize = Long.toString(filesizebyte);
		} catch (Exception ex) {
			System.out.println("Exception found in:" + ex);
			filepath = "";
		}
		return retfilesize;
	}

	public static String urlencode(String data) {
		String retfilesize = "";
		try {
			data = EncDecrypt.encrypt(data);
			retfilesize = URLEncoder.encode(data, "UTF-8");
			System.out.println("encodedata:" + retfilesize);
		} catch (Exception ex) {
			System.out.println("Exception found in:" + ex);
			retfilesize = "";
		}
		return retfilesize;
	}

	public static Date getCurrentDateTime(String dateformattype) {
	    Date date = new Date();
	    try {
	      DateFormat dateFormat = new SimpleDateFormat(dateformattype);
	      Date dateFor = new Date();
	      date = dateFormat.parse(dateFormat.format(dateFor));
	    } catch (Exception ex) { // empty block

	    }
	    return date;
	  }
	public static String getCurrentDate(String dateformattype) {
	    String retundate = "";
	    try {
	      DateFormat dateFormat = new SimpleDateFormat(dateformattype);
	      Date dateFor = new Date();
	      retundate = dateFormat.format(dateFor);
	    } catch (Exception ex) { // empty block

	    }
	    return retundate;
	  }

	public static void  TextFileWriting(String filepath,String txtvalue) {
		Date loccrntdate = null;
		SimpleDateFormat sdf = null;
		ResourceBundle ivrRsbundelapiversion = null;
		try {
			Commonutility.toWriteConsole("Qry file : "+ txtvalue);
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			loccrntdate = new Date();
			loccrntdate = sdf.parse(sdf.format(loccrntdate));
			if (olddate != null) {
				olddate = sdf.parse(sdf.format(olddate));
				// System.out.println("Old Date : "+sdf.format(olddate));
			}
			if ((olddate == null || loccrntdate.after(olddate)) || true) {
				if (olddate != null) {
						System.out.println("Current Date : ["+sdf.format(loccrntdate)+"] is after Old Date : ["+sdf.format(olddate)+"]");
				} else {
						System.out.println("Current Date : ["+sdf.format(loccrntdate)+"] is after Old Date : ["+olddate+"]");
				}
				olddate = loccrntdate;
				String lvrpropath = new Commonutility().togetFullpath("apiversioninfo.properties").replaceAll("%20", " ");
				ivrRsbundelapiversion = ResourceBundle.getBundle("apiversioninfo");
				String lvrCrntdbver = ivrRsbundelapiversion.getString("currentdbversion");				
				lvrCrntdbver = toReadpropertiesfile(lvrpropath, "currentdbversion");
				System.out.println("lvrCrntdbver : "+lvrCrntdbver);
				int lvrDbvrcunt = 0;
				if(Commonutility.toCheckisNumeric(lvrCrntdbver)){
					lvrDbvrcunt = Integer.parseInt(lvrCrntdbver);
					lvrDbvrcunt = lvrDbvrcunt + 1;
				}

				String  lvrMobildbfldr = ivrRsbundelappresorce.getString("external.fldr") + ivrRsbundelappresorce.getString("external.api.fldr")+ ivrRsbundelappresorce.getString("external.dbversion.fldr");				
				Commonutility.toWriteConsole("properties path - lvrpropath : "+lvrpropath);

				//String rst = toWritepropertiesfile("D:/pl0019-eclipse workspace/socialindiaservices/src/apiversioninfo.properties","currentdbversion",Commonutility.intToString(lvrDbvrcunt));
			//	String rst = toWritepropertiesfile(lvrpropath,"currentdbversion",Commonutility.intToString(lvrDbvrcunt));

				// to change get value from plus + 1 update property
				filename = lvrMobildbfldr+Commonutility.intToString(lvrDbvrcunt);// property file from - 1;()

			} else if (loccrntdate.equals(olddate)) {
				 //System.out.println("Current Date is equal Old Date");
			} else if (loccrntdate.before(olddate)) {
				//System.out.println("Current Date is before Old Date");
			} else { // Exception
			}
			filepath =""+filename+".txt"; // to change
			Commonutility.toWriteConsole("filepath : "+filepath);
			crdDir(filepath);
			FileWriter writer = new FileWriter(filepath, true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write(txtvalue);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (Exception e) {
			System.out.println("Exception : "+e);
		}

	}
	public static void  TextFileWritee(String filepath,String txtvalue) {
		FileWriter writer = null;
		BufferedWriter bufferedWriter = null;
		try{
			crdDir(filepath);
			writer = new FileWriter(filepath, true);
			bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write(txtvalue);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch(Exception e){
			Commonutility.toWriteConsole("Exception : "+ e);	
		} finally {
			
		}
	}
	public static void  TextFilechangeWriting(String filepath,String txtvalue) {
		Date loccrntdate = null;
		SimpleDateFormat sdf = null;
		ResourceBundle ivrRsbundelapiversion = null;
		File lvrFile = null;
		try {
				Commonutility.toWriteConsole("Qry file>>>> : "+ txtvalue);									
				String lvrpropath = new Commonutility().togetFullpath("apiversioninfo.properties").replaceAll("%20", " ");
			 Commonutility.toWriteConsole("2");
				ivrRsbundelapiversion = ResourceBundle.getBundle("apiversioninfo");
		 Commonutility.toWriteConsole("3");
				String lvrCrntdbver = ivrRsbundelapiversion.getString("currentdbversion");	
	 Commonutility.toWriteConsole("4");
				Commonutility.toWriteConsole("lvrCrntdbver first : "+lvrCrntdbver);
				lvrCrntdbver = toReadpropertiesfile(lvrpropath, "currentdbversion");
				Commonutility.toWriteConsole("lvrCrntdbver second : "+lvrCrntdbver);
				
				int lvrDbvrcunt = 0;
				if(Commonutility.toCheckisNumeric(lvrCrntdbver)){
					lvrDbvrcunt = Integer.parseInt(lvrCrntdbver);
					lvrDbvrcunt = lvrDbvrcunt + 1;
				}

				String  lvrMobildbfldr = ivrRsbundelappresorce.getString("external.fldr") + ivrRsbundelappresorce.getString("external.api.fldr")+ ivrRsbundelappresorce.getString("external.dbversion.fldr") + lvrDbvrcunt+".txt";				
				Commonutility.toWriteConsole("lvrMobildbfldr : "+lvrMobildbfldr);
				lvrFile = new File(lvrMobildbfldr);
				if(lvrFile!=null && lvrFile.exists()  && lvrFile.isFile()){
					Commonutility.toWriteConsole("properties path - lvrpropath : "+lvrpropath);
					Commonutility.toWriteConsole("lvrDbvrcunt : "+lvrDbvrcunt);
					
					//String rst = toWritepropertiesfile("D:/pl0019-eclipse workspace/socialindiaservices/src/apiversioninfo.properties","currentdbversion",Commonutility.intToString(lvrDbvrcunt));
					String rst = toWritepropertiesfile(lvrpropath,"currentdbversion",Commonutility.intToString(lvrDbvrcunt));
				} else {
					Commonutility.toWriteConsole("properties path - lvrpropath : "+lvrpropath);
					Commonutility.toWriteConsole("lvrDbvrcunt : "+lvrDbvrcunt);
					Commonutility.toWriteConsole("File Not Found in : "+lvrDbvrcunt+".txt");
				}
				
						
				
		} catch (Exception e) {
			//System.out.println("Exception : "+e);
		} finally {
			lvrFile = null;
		}

	}
	
	public static void TextFileReading(String filepath) {
		try {
            FileReader reader = new FileReader(filepath);
            int character;

            while ((character = reader.read()) != -1) {
                System.out.print((char) character);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static Date stringtoTimestamp(String timestamp) {
		Date date = new Date();
	    try {
	      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      date = dateFormat.parse(timestamp);
	      System.out.println(date);
	    } catch (Exception ex) { // empty block
	    	date = null;
	    }
	    return date;
	}
	public static float stringToFloat(String data) {
		float ret = 0.0f;
	    try {
	      if (data != null) {
	        data = data.trim();
	        if (!data.equalsIgnoreCase("") && !data.equalsIgnoreCase("null")) {
	          ret = Float.parseFloat(data);
	        }
	      }
	    } catch (Exception ex) {
	    	ret = 0.0f;
	      System.out.println("Exception found in commonutils stringToFloat:" + ex);
	    }
	    return ret;
	}
	public static String intToString(Integer data) {
		String retval = "";
		try {
			System.out.println("intToString fu call :" + data);
			if (data != null && data != 0 ) {
				retval = Integer.toString(data);
			}
		} catch (Exception ex) {
			System.out.println("Exception found in stringToInteger :" + ex);
			retval = "";
		}
		return retval;
	}
	public static String intnullChek(Integer data) {
		String retval = "0";
		try {
			System.out.println("intToString fu call :" + data);
			if (data != null) {
				retval = Integer.toString(data);
			}
		} catch (Exception ex) {
			System.out.println("Exception found in stringToInteger :" + ex);
			retval = "0";
		}
		return retval;
	}
	public static String floatToString(Float data) {
		String retval = "";
		try {
			if (data != null && data != 0 ) {
				retval = Float.toString(data);
			}
		} catch (Exception ex) {
			System.out.println("Exception found in stringToInteger :" + ex);
			retval = "";
		}
		return retval;
	}
	public static String dateToString(Date data) {
		String retval = "";
		try {
			if (data != null) {
				retval = data.toString();
			}
		} catch (Exception ex) {
			System.out.println("Exception found in stringToInteger :" + ex);
			retval = "";
		}
		return retval;
	}

	public static String stringToStringempty(String data) {
		String retval = "";
		try {
			if (data != null) {
				data = data.trim();
				if (!data.equalsIgnoreCase("") &&  !data.equalsIgnoreCase("null")) {
					retval = data;
				} else {
					retval = "";
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception found in stringToInteger :" + ex);
			retval = "";
		}
		return retval;
	}
	public static Date enteyUpdateInsertDateTime() {
	    Date date = new Date();
	    try {
	      String dateformattype = "yyyy-MM-dd HH:mm:ss";
	      DateFormat dateFormat = new SimpleDateFormat(dateformattype);
	      Date dateFor = new Date();
	      date = dateFormat.parse(dateFormat.format(dateFor));
	    } catch (Exception ex) { // empty block

	    }
	    return date;
	  }

	public static void toWriteConsole(Object pwritemsg){
		System.out.println("["+Commonutility.toGetCurrentDatetime("yyyy-MM-dd HH:mm:ss")+"] [WEB SERVICE] " + pwritemsg);
	}

	public static String timeStampRetStringVal(){
		String timeStamp = "";
		try {
			DateFormat dateFormatw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateForw = new Date();
			timeStamp = dateFormatw.format(dateForw);
		} catch (Exception ex) {
			System.out.println("Exception found in timeStampRetStringVal:" + ex);
		}
		return timeStamp;
	}

	public static String changedateformat(Date dat,String toformat) {
		String datestr="";
		if (toformat != null && toformat.equalsIgnoreCase("dd-MM-yyyy hh:mm:ss a")) {
			SimpleDateFormat locSdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			datestr=locSdf.format(dat).toString();
		}
		return datestr;
	}
	
	public static String tochangeonefrmtoanother(String fromformat,String toformat, String pDate) {
		String datestr="";
		SimpleDateFormat frmFrmtsdft = null, toFrmtsdft = null;
		Date frmdate = null;
		try {
			if (Commonutility.checkempty(fromformat) && Commonutility.checkempty(toformat) && Commonutility.checkempty(pDate)) {
			 frmFrmtsdft = new SimpleDateFormat(fromformat);
			 toFrmtsdft  = new SimpleDateFormat(toformat);				
				frmdate = frmFrmtsdft.parse(pDate);
				datestr = toFrmtsdft.format(frmdate);
			} else {
				datestr = pDate;
			}
		} catch (Exception e) {
		} finally {
			frmFrmtsdft = null;
			toFrmtsdft = null;
			frmdate = null;
		}
		return datestr;
	}
	
	public static String toReadpropertiesfile(String prmPropertiesfname, String prmKey){
		String lvrRtnstr = null;
		FileInputStream in = null;
		Properties props = null; // read existing
		Enumeration enmpro =null;
		try{
			  in = new FileInputStream(prmPropertiesfname); // input properties
		        props = new Properties();
		        props.load(in);
		        in.close();
		        enmpro = props.propertyNames();
		        while (enmpro.hasMoreElements()) {
		        	String keyss =  (String) enmpro.nextElement();
					String value = props.getProperty(keyss);
					if(keyss.equalsIgnoreCase(prmKey)){
						lvrRtnstr = value;
						break;
					} else {
						lvrRtnstr = ""; 
					}
		        }
		        System.out.println("lvrRtnstr >>> "+lvrRtnstr);
		} catch (Exception ee){
			System.out.println("Exception found in toReadpropertiesfile() : "+ee);
		} finally {
			 in = null;
			 props = null; // read existing
			 enmpro =null;
		}
		
		return lvrRtnstr;
	}
	
	public static String toWritepropertiesfile(String prmPropertiesfname, String prmKey, String prmVal){
		String lvrRtnstr = null;
		FileOutputStream out = null;
		FileInputStream in = null;
		Properties props = null; // read existing
		Properties newProps = null; // new  file for write
		Enumeration enmpro =null;
		try{


	        in = new FileInputStream(prmPropertiesfname); // input properties
	        props = new Properties();
	        props.load(in);
	        in.close();
			enmpro = props.propertyNames();
			newProps = new Properties();
			while (enmpro.hasMoreElements()) {
				String keyss =  (String) enmpro.nextElement();
				String value = props.getProperty(keyss);

				if(keyss.equalsIgnoreCase(prmKey)){
					toWriteConsole("Key if : "+keyss +" Update Key : "+prmKey);
					toWriteConsole("value if : "+value +" Update Value : "+prmVal);
					newProps.setProperty(prmKey, prmVal);
				} else {
					toWriteConsole("Key else : "+keyss );
					toWriteConsole("value else : "+value);
					newProps.setProperty(keyss, value);
				}
			}
			out = new FileOutputStream(prmPropertiesfname);
			newProps.store(out, null);
	        out.close();
	        lvrRtnstr = "Success";
	        return lvrRtnstr;
		} catch (Exception e){
			toWriteConsole("Exception toWritepropertiesfile() : "+e);
			 lvrRtnstr = "Catch";
			return lvrRtnstr;
		} finally {
			 out = null;  in = null; props = null;newProps = null;enmpro = null;
		}

	}
	 public static Date addDays(Date date, int days)
	    {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.DATE, days); //minus number would decrement the days
	        return cal.getTime();
	    }
	 public static boolean toCheckIsImgfrmt(String pImgfrmt) {
		 boolean lvrRtnval =  false;
		 try{
			 String lvrimgfrmt = ivrRsbundelappresorce.getString("Text.image.extensions");
			 if(Commonutility.checkempty(lvrimgfrmt)){
				 lvrimgfrmt = lvrimgfrmt.toLowerCase();
			 }
			 if (Commonutility.checkempty(pImgfrmt)){
				 pImgfrmt =pImgfrmt.toLowerCase();
			 }
			 if(Commonutility.checkempty(pImgfrmt) && lvrimgfrmt.contains(pImgfrmt)){
				 lvrRtnval =  true;
			 } else {
				 lvrRtnval =  false;
			 }
			 
		 } catch (Exception e) {
			 lvrRtnval =  false;
		 } finally {
			 
		 }
		 return lvrRtnval;
	 }
	 public static boolean copyFolder(File srcFolder, File destFolder) throws IOException {
			// TODO Auto-generated method stub
		 boolean result = false;
			if(srcFolder.isDirectory()){
				
				System.out.println("Enter srcFolder if path-------------------------");

	    		//if directory not exists, create it
	    		if(!destFolder.exists()){
	    			destFolder.mkdir();
	    		   System.out.println("Directory copied from "
	                              + srcFolder + "  to " + destFolder);
	    		}

	    		//list all the directory contents
	    		String files[] = srcFolder.list();

	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(srcFolder, file);
	    		   File destFile = new File(destFolder, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}

	    	}else{
	    		System.out.println("Enter srcFolder Else path-------------------------");
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(srcFolder);
	    	    OutputStream out = new FileOutputStream(destFolder);

	    	        byte[] buffer = new byte[1024];

	    	        int length;
	    	        //copy the file content in bytes
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }

	    	        in.close();
	    	        out.close();
	    	        System.out.println("File copied from " + srcFolder + " to " + destFolder);
	    	}
			result = true;
	    
			return result;
		}
	 
	 public static String getChecksumValue(String datafile) {
			String checkvalue = "";
			try {
				System.out.println("datafile============"+datafile);

				MessageDigest md = MessageDigest.getInstance("MD5");
				FileInputStream fis = new FileInputStream(datafile);
				byte[] dataBytes = new byte[1024];

				int nread = 0;
				while ((nread = fis.read(dataBytes)) != -1) {
					md.update(dataBytes, 0, nread);
				}

				byte[] mdbytes = md.digest();

				// convert the byte to hex format
				StringBuilder sb = new StringBuilder("");
				for (int i = 0; i < mdbytes.length; i++) {
					sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
							.substring(1));
				}

				System.out.println("Checksum val->" + sb.toString());
				checkvalue=sb.toString();
				if(checkvalue==null){
					checkvalue="";
				}
			} catch (Exception ex) {
				System.out.println("Exception ------checkvalue------- "+ex);
			}
			return checkvalue;
		}
	 
	public static Date getGmtCurrentTme() {
		Date retundate = new Date();
		try {
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			// Local time zone
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			// Date dateFor = new Date();
			retundate = dateFormatLocal.parse(dateFormatGmt.format(new Date()));
		} catch (Exception ex) { // empty block
			System.out.println("Exception getGmtCurrentTme------------->" + ex);
		}
		return retundate;
	}
	 
	 public static double toRoundedVal(double pRondVl) {
			double locRtnDbl = 0;
			DecimalFormat df = new DecimalFormat("####0.00");
			try {
				locRtnDbl = Double.valueOf(df.format(pRondVl));
				Commonutility.toWriteConsole("---------"+locRtnDbl);
			} catch (Exception e) {
			} finally {
			}
			return locRtnDbl;
		}
	 
	 public static String toAmontDecimalfrmt(double pRondVl) {
		 String locRtnDbl = "";
			DecimalFormat df = new DecimalFormat("####0.00");
			try {
				locRtnDbl = df.format(pRondVl);
				Commonutility.toWriteConsole("---------"+locRtnDbl);
			} catch (Exception e) {
			} finally {
			}
			return locRtnDbl;
		}
	 
	public static void perfeecnum() {
		String lvrStr="";
		int n, sum = 0;
		n = 10000;
		for (int jj = 1; jj <= n; jj++) {
			for (int i = 1; i < jj; i++) {
				if (jj % i == 0) {
					sum = sum + i;
					lvrStr += "" + i + " + ";
				}
			}
			if (sum == jj) {
				System.out.println( jj  + " : Given number is Perfect : "+lvrStr.substring(0,lvrStr.length()-3));
			} else {
				//System.out.println(jj + " : Given number is not Perfect");
			}
			sum = 0;lvrStr= "";
		}	
	}
}
