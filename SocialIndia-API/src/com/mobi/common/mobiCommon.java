package com.mobi.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.GatepassEntryTblVO;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;

public class mobiCommon extends ActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	profileDao profile=new profileDaoServices();
	public static String convertDateToDateTimeDiff(Date entrytime){
		String finalDate="";
		try{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   Date date = new Date();
		   String curdate=dateFormat.format(date);
		   date = dateFormat.parse(curdate);

		   long diff =  date.getTime() - entrytime.getTime();
		    long diffSeconds = diff / 1000 % 60;
		    long diffMinutes = diff / (60 * 1000) % 60;
		    long diffHours = diff / (60 * 60 * 1000);
		    long diffDays = diff / (24 * 60 * 60 * 1000);
		    if(diffDays>0){
		    	finalDate=diffDays+" Days "+diffHours+" Hrs ago";
		    }else if(diffHours>0){
		    	finalDate=diffHours+" Hrs "+diffMinutes+" Min ago";
		    }else{
		    	finalDate=diffMinutes+" Min ago";
		    }
		    System.out.println("====finalDate====="+finalDate);
		}catch(Exception e){
			System.out.println("===mobiCommon====convertDateToDateTimeDiff====="+e);
		}
		return finalDate;
	}

	public static long getDateToMins(Date otpGenDate){
		long dateDifff = 0;
		try{
			Date date=new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String otpGenDateTime=format.format(otpGenDate);
			String curdate=format.format(date);
			Date d1 = null;
			Date d2 = null;
				d1 = format.parse(otpGenDateTime);
				d2 = format.parse(curdate);
				long diff = d2.getTime() - d1.getTime();
			    dateDifff=diff/(1000*60);
		}catch(Exception e){
			System.out.println("===mobiCommon====getDateToMins====="+e);
		}
		return dateDifff;
	}
	public  Integer getFileExtensionType(String extension){
		int fileExtensiontype=0;
		try{
			extension="."+extension;
			//System.out.println("extension---------------"+extension);
			if(getText("Text.image.extensions").contains(extension)){
				fileExtensiontype=1;
			}else if(getText("Text.video.extensions").contains(extension)){
				fileExtensiontype=2;
			}else if(getText("Text.pdf.extensions").contains(extension)){
				fileExtensiontype=3;
			}else if(getText("Text.doc.extensions").contains(extension)){
				fileExtensiontype=4;
			}else if(getText("Text.excel.extensions").contains(extension)){
				fileExtensiontype=5;
			}else if(getText("Text.text.extensions").contains(extension)){
				fileExtensiontype=6;
			}else if(getText("Text.ppt.extensions").contains(extension)){
				fileExtensiontype=7;
			}else if(getText("Text.audio.extensions").contains(extension)){
				fileExtensiontype=8;
			}else{
				fileExtensiontype=9;
			}

		}catch(Exception e){
			System.out.println("==mobiCommon=dsf=getFileExtensionType==="+e);
		}
		return fileExtensiontype;
	}

	public static int getImageHeight(File FilePath){
		int height=0;
		try{
			BufferedImage bimg = ImageIO.read(FilePath);
	    	System.out.println("====d=ff=ds====");
	    	height   = bimg.getHeight();
		}catch(Exception e){
			System.out.println("===mobiCommon====getImageHeight====="+e);
		}
		return height;
	}

	public static int getImageWidth(File FilePath){
		int width=0;
		try{
			BufferedImage bimg = ImageIO.read(FilePath);
	    	System.out.println("====d=ff=ds====");
	    	width   = bimg.getWidth();
		}catch(Exception e){
			System.out.println("===mobiCommon====getImageWidth====="+e);
		}
		return width;
	}

	public static String maskMobileNo(String key) {
		int mobLen = key.length()-4;
		String firstHalf = key.substring(key.length()-4);
		String mask = "";
	for (int i = 0; i < mobLen; i++) {
			mask += "X";
		}
		return mask + firstHalf;

	}

	public int getProfilePercentageCal(UserMasterTblVo userData){
		int percentage=0;
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		
		try{
			
			String sqlQuery = "CALL PR_PROFILE_METER(:USERID)";
			System.out.println("CALL PR_PROFILE_METER("+userData.getUserId()+")");
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("TOT_PERCENT", Hibernate.INTEGER);
			
			queryObj.setInteger("USERID", userData.getUserId());
			percentage = (int) queryObj.uniqueResult();
			
			
			
			
		/*if(userData.getFirstName()!=null && !userData.getFirstName().equalsIgnoreCase("") && !userData.getFirstName().equalsIgnoreCase("null")){
			percentage=Integer.parseInt(getText("profile.percentage.firstname"));
		}
		if(userData.getLastName()!=null && !userData.getLastName().equalsIgnoreCase("") && !userData.getLastName().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.lastName"));
		}
		int flatcount=0;String locVrSlQry="";
		locVrSlQry="SELECT count(flat_id) FROM FlatMasterTblVO where status='1' and usrid='"+userData.getUserId()+"'";
		flatcount=profile.getInitTotal(locVrSlQry);
		System.out.println("=======flatcount======="+flatcount);
		flatcount=flatcount*2;
		if(flatcount < Integer.parseInt(getText("profile.percentage.flatNo"))){
			percentage+=flatcount;
		}else{
			percentage+=Integer.parseInt(getText("profile.percentage.flatNo"));
		}
		if(userData.getDob()!=null && !userData.getDob().equalsIgnoreCase("") && !userData.getDob().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.dob"));
		}
		if(userData.getMobileNo()!=null && !userData.getMobileNo().equalsIgnoreCase("") && !userData.getMobileNo().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.mobileNo"));
		}
		if(userData.getBloodType()!=null && !userData.getBloodType().equalsIgnoreCase("") && !userData.getBloodType().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.bloodType"));
		}
		if(userData.getEmailId()!=null && !userData.getEmailId().equalsIgnoreCase("") && !userData.getEmailId().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.emailId"));
		}
		if(userData.getAddress1()!=null && !userData.getAddress1().equalsIgnoreCase("") && !userData.getAddress1().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.address1"));
		}
		if(userData.getGender()!=null && !userData.getGender().equalsIgnoreCase("") && !userData.getGender().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.gender"));
		}
		if(userData.getIdProofNo()!=null && !userData.getIdProofNo().equalsIgnoreCase("") && !userData.getIdProofNo().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.idProofNo"));
		}
		if(userData.getOccupation()!=null && !userData.getOccupation().equalsIgnoreCase("") && !userData.getOccupation().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.occupation"));
		}
		if(userData.getImageNameMobile()!=null && !userData.getImageNameMobile().equalsIgnoreCase("") && !userData.getImageNameMobile().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.imageNameMobile"));
		}
		if(userData.getCountryId()!=null && !userData.getCountryId().getCountryName().equalsIgnoreCase("") && !userData.getCountryId().getCountryName().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.countryname"));
		}
		if(userData.getStateId()!=null && !userData.getStateId().getStateName().equalsIgnoreCase("") && !userData.getStateId().getStateName().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.statename"));
		}
		if(userData.getCityId()!=null && !userData.getCityId().getCityName().equalsIgnoreCase("") && !userData.getCityId().getCityName().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.cityname"));
		}
		if(userData.getPstlId()!=null && !userData.getPstlId().getPstlCode().equalsIgnoreCase("") && !userData.getPstlId().getPstlCode().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.postalcode"));
		}
		int wishlistcount=0;
		locVrSlQry="SELECT count(uniqueId) FROM MvpUsrSkillTbl where skillSts='1' and userId.userId='"+userData.getUserId()+"'";
		wishlistcount=profile.getInitTotal(locVrSlQry);
		
		if(wishlistcount!=0 && wishlistcount < Integer.parseInt(getText("profile.percentage.personalskill"))){
			System.out.println("=======wishlistcount======="+wishlistcount);
			percentage+=wishlistcount;
		}else{
			percentage+=Integer.parseInt(getText("profile.percentage.personalskill"));
			System.out.println("=======wishlistcount======="+getText("profile.percentage.personalskill"));
		}

		int networklistcount=0;String locVrSlQry1="";
		locVrSlQry1="SELECT count(networkId) FROM MvpNetworkTbl where connStsFlg='1' and usrParentUsrId.userId='"+userData.getUserId()+"'";
		networklistcount=profile.getInitTotal(locVrSlQry1);
		System.out.println("=======networklistcount======="+networklistcount);
		if(networklistcount!=0 && networklistcount < Integer.parseInt(getText("profile.percentage.networklist"))){
			percentage+=networklistcount;
		}else{
			percentage+=Integer.parseInt(getText("profile.percentage.networklist"));
		}*/

		}catch(Exception e){
			System.out.println("===mobiCommon====getProfilePercentageCal====="+e);
		}
		return percentage;
	}

	public int getProfileForFamilyPercentageCal(MvpFamilymbrTbl userData){
		int percentage=0;
		try{
		if(userData.getFmbrName()!=null && !userData.getFmbrName().equalsIgnoreCase("") && !userData.getFmbrName().equalsIgnoreCase("null")){
			percentage=Integer.parseInt(getText("profile.percentage.family.firstname"));
		}
		/*if(userData.getFmbrDob()!=null ){
			percentage+=Integer.parseInt(getText("profile.percentage.family.dob"));
		}*/
	

		if(userData.getFmbrPhNo()!=null && !userData.getFmbrPhNo().equalsIgnoreCase("") && !userData.getFmbrPhNo().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.family.mobileNo"));
		}
		/*if(userData.getFmbrEmail()!=null && !userData.getFmbrEmail().equalsIgnoreCase("") && !userData.getFmbrEmail().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.family.emailId"));
		}

		if(userData.getFmbrGender()!=null && !userData.getFmbrGender().equalsIgnoreCase("") && !userData.getFmbrGender().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.family.gender"));
		}
		if(userData.getFmbrIdCardNo()!=null && !userData.getFmbrIdCardNo().equalsIgnoreCase("") && !userData.getFmbrIdCardNo().equalsIgnoreCase("null")){
			percentage+=Integer.parseInt(getText("profile.percentage.family.idProofNo"));
		}
		if(userData.getFmbrCntry()!=null ){
			percentage+=Integer.parseInt(getText("profile.percentage.family.countryname"));
		}
		if(userData.getFmbrState()!=null ){
			percentage+=Integer.parseInt(getText("profile.percentage.family.statename"));
		}
		if(userData.getFmbrCity()!=null ){
			percentage+=Integer.parseInt(getText("profile.percentage.family.cityname"));
		}

		int wishlistcount=0;String locVrSlQry="";
		locVrSlQry="SELECT count(wishlistId) FROM MvpWishlistTbl where actFlg='1' and userId.userId='"+userData.getUserId().getUserId()+"'";
		wishlistcount=profile.getInitTotal(locVrSlQry);
		System.out.println("=======wishlistcount======="+wishlistcount);
		if(wishlistcount < Integer.parseInt(getText("profile.percentage.wishlistlist"))){
			percentage+=wishlistcount;
		}else{
			percentage+=Integer.parseInt(getText("profile.percentage.wishlistlist"));
		}

		int networklistcount=0;String locVrSlQry1="";
		locVrSlQry1="SELECT count(networkId) FROM MvpNetworkTbl where connStsFlg='1' and usrParentUsrId.userId='"+userData.getUserId().getUserId()+"'";
		networklistcount=profile.getInitTotal(locVrSlQry1);
		System.out.println("=======networklistcount======="+networklistcount);
		if(networklistcount < Integer.parseInt(getText("profile.percentage.networklist"))){
			percentage+=networklistcount;
		}else{
			percentage+=Integer.parseInt(getText("profile.percentage.networklist"));
		}*/

		}catch(Exception e){
			System.out.println("===mobiCommon====getProfilePercentageCal====="+e);
		}
		return percentage;
	}
	public String getServerResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
		String response="";
		JSONObject responseMsg = new JSONObject();
		try{
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			response = responseMsg.toString();
			System.out.println("response------------------"+response);
			response=EncDecrypt.encrypt(response);
		}catch (Exception ex){
			try{
				dataJson=new JSONObject();
				responseMsg.put("servicecode", serviceCode);
				responseMsg.put("statuscode", "2");
				responseMsg.put("respcode", "E0002");
				responseMsg.put("message", "Sorry, an unhandled error occurred");
				responseMsg.put("data", dataJson);
				response = responseMsg.toString();
			}catch (Exception e){}

			ex.printStackTrace();

		}
		return response;
	}

	public static String getMsg(String resCode) {
		String retMsg = "";
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			log.logMessage("Enter into get response msg", "info", mobiCommon.class);
			session = HibernateUtil.getSession();
			if (Commonutility.checkempty(resCode)) {
				String qry = "select message from ResponseMsgTblVo where responseCode=:RESCODE and status=:STATUS";
				qury = session.createQuery(qry);
				qury.setString("RESCODE", resCode);
				qury.setInteger("STATUS", 1);
				retMsg = (String) qury.uniqueResult();
				log.logMessage("Response Code :" + resCode + " Response Message : "  + retMsg, "info", mobiCommon.class);
			} else {
				retMsg = "";
			}
		} catch (Exception ex) {
			log.logMessage("Exception found in getMsg: " + ex, "error", mobiCommon.class);
			retMsg = "Sorry,Unable to fetch the response";
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return retMsg;
	}
	public boolean deleteIfFileExist(String filepath, String filename) {
		File lcMovefile = null;
		File directoryfile = null;
		try {
			lcMovefile = new File(filepath + "/" + filename);
			directoryfile = new File(filepath);
			if (lcMovefile.exists()) {
				lcMovefile.delete();
			}
			if (directoryfile.isDirectory() && directoryfile.list().length == 0) {
				directoryfile.delete();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public static String getBlockName(int userId) {
		// TODO Auto-generated method stub
		String retBlockNm = "";
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
				String qry = "select wingsname from FlatMasterTblVO where usrid =:USR_ID and ivrBnismyself=:IS_MYSELF";
				qury = session.createQuery(qry);
				qury.setInteger("USR_ID", userId);
				qury.setInteger("IS_MYSELF", 1);
				retBlockNm = (String) qury.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in getBlockName: " + ex, "error", mobiCommon.class);
			retBlockNm = "";
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return retBlockNm;
	}

	public static String getFlatno(int userId) {
		// TODO Auto-generated method stub
		String retFlatNm = "";
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
				String qry = "select flatno from FlatMasterTblVO where usrid =:USR_ID and ivrBnismyself=:IS_MYSELF";
				qury = session.createQuery(qry);
				qury.setInteger("USR_ID", userId);
				qury.setInteger("IS_MYSELF", 1);
			retFlatNm = (String) qury.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in getBlockName: " + ex, "error", mobiCommon.class);
			retFlatNm = "";
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return retFlatNm;
	}
	public static List<FlatMasterTblVO> blockDetailData(int userId) {
		// TODO Auto-generated method stub
		List<FlatMasterTblVO> flatMastObj = new ArrayList<FlatMasterTblVO>();
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
				String qry = "From FlatMasterTblVO where usrid =:USR_ID";
				qury = session.createQuery(qry);
				qury.setInteger("USR_ID", userId);
				flatMastObj = qury.list();
		} catch (Exception ex) {
			log.logMessage("Exception found in blockDetailData: " + ex, "error", mobiCommon.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return flatMastObj;
	}

	public static  MvpGatepassDetailTbl getGatepassid(String passno) {
		// TODO Auto-generated method stub
		MvpGatepassDetailTbl gatepassdetail =new MvpGatepassDetailTbl();
		Integer retpassid;
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
				String qry = " from MvpGatepassDetailTbl where gatepassNo =:GATEPASS_NO ";
				qury = session.createQuery(qry);
				qury.setString("GATEPASS_NO", passno);

				gatepassdetail = (MvpGatepassDetailTbl) qury.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in getGatepassid: " + ex, "error", mobiCommon.class);
			retpassid = 0;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return gatepassdetail;
	}

	public static Integer checkgatepass_in_out(MvpGatepassDetailTbl pIntdupobj) {
		// TODO Auto-generated method stub
			UserMasterTblVo userDetails = null;
			String result="";
			int count=0;
			boolean dup_mobno=true;
			int usercount=0;
		    Session session = null;
		    Transaction tx = null;
		    Query query = null;
		    try {
		    	userDetails = new UserMasterTblVo();
		    	session = HibernateUtil.getSession();
		    	String qry = "";

		    		qry = " SELECT count(passId)  FROM GatepassEntryTblVO where passId="+pIntdupobj.getPassId()+"";
		    		query = session.createQuery(qry);
		    	count = ((Number) query.uniqueResult()).intValue();


		    } catch (HibernateException ex) {
		      ex.printStackTrace();
		      Commonutility.toWriteConsole("Exception Found in residentxlsUploadCreation : "+ex);
		      dup_mobno = false;
		    } finally {
		    	if(session!=null){session.clear(); session.close();session =null;} query = null;
		    }
		    return count;
	}
	public static  GatepassEntryTblVO getGatepasstatus_in_out(Integer passid) {
		// TODO Auto-generated method stub
		GatepassEntryTblVO gatepassinoutdetail =new GatepassEntryTblVO();
		Integer retpassid;
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
				String qry = " from GatepassEntryTblVO where passId ="+passid+" order by entryDateTime desc limit 1 ";
				qury = session.createQuery(qry);

				qury.setMaxResults(1);
				gatepassinoutdetail = (GatepassEntryTblVO) qury.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in getGatepassid: " + ex, "error", mobiCommon.class);
			retpassid = 0;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return gatepassinoutdetail;
	}
	public static String getVisitorName(int passId) {
		// TODO Auto-generated method stub
		String retBlockNm = "";
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
				String qry = "select visitorImage from MvpGatepassDetailTbl where passId ="+passId+" ";
				qury = session.createQuery(qry);

				retBlockNm = (String) qury.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in getBlockName: " + ex, "error", mobiCommon.class);
			retBlockNm = "";
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return retBlockNm;
	}

	public static Integer checkgatepass_no(String gatepassno) {
		// TODO Auto-generated method stub
			int count=0;
			boolean dup_mobno=true;
			int usercount=0;
		    Session session = null;
		    Transaction tx = null;
		    Query query = null;
		    try {
		    	session = HibernateUtil.getSession();
		    	String qry = "";

		    		qry = " SELECT count(gatepassNo)  FROM MvpGatepassDetailTbl where gatepassNo='"+gatepassno+"'";
		    		query = session.createQuery(qry);
		    	count = ((Number) query.uniqueResult()).intValue();


		    } catch (HibernateException ex) {
		      ex.printStackTrace();
		      Commonutility.toWriteConsole("Exception Found in residentxlsUploadCreation : "+ex);
		      dup_mobno = false;
		    } finally {
		    	if(session!=null){session.clear(); session.close();session =null;} query = null;
		    }
		    return count;
	}

	public static Integer checkgatepass_in_out_passtype2(MvpGatepassDetailTbl pIntdupobj) {
		// TODO Auto-generated method stub
			UserMasterTblVo userDetails = null;
			String result="";
			int count=0;
			boolean dup_mobno=true;
			int usercount=0;
		    Session session = null;
		    Transaction tx = null;
		    Query query = null;
		    try {
		    	userDetails = new UserMasterTblVo();
		    	session = HibernateUtil.getSession();
		    	String qry = "";

		    		qry = " SELECT count(passId)  FROM GatepassEntryTblVO where passId="+pIntdupobj.getPassId()+" ";
		    		query = session.createQuery(qry);
		    	count = ((Number) query.uniqueResult()).intValue();


		    } catch (HibernateException ex) {
		      ex.printStackTrace();
		      Commonutility.toWriteConsole("Exception Found in residentxlsUploadCreation : "+ex);
		      dup_mobno = false;
		    } finally {
		    	if(session!=null){session.clear(); session.close();session =null;} query = null;
		    }
		    return count;
	}
	
	public static String getchatDateTime(Date entrytime){
		String finalDate="";
		try{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		   Date date = new Date();
		   String curdate=dateFormat.format(date);
		   date = dateFormat.parse(curdate);
		   if((entrytime.getDate()==date.getDate()) && (entrytime.getYear()==date.getYear()) && (entrytime.getMonth()==date.getMonth())){
			   finalDate=timeFormat.format(entrytime);
		   }else{
			   finalDate=dateFormat.format(entrytime);
		   }

		    System.out.println("====finalDate====="+finalDate);
		}catch(Exception e){
			System.out.println("===mobiCommon====convertDateToDateTimeDiff====="+e);
		}
		return finalDate;
	}
	
}
