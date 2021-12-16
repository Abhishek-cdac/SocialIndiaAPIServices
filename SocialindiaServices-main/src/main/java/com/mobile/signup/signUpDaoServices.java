package com.mobile.signup;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobile.otpVo.otpDaoService;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class signUpDaoServices implements  signUpDao {
	Log log=new Log();
	@Override
	public UserMasterTblVo checkUserSignUpInfo(String mobile,
			int committeeCode, int residentCode,String userId,String otpfor) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS   and mobileNo=:MOBILE_NO and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE) and  userId=:USER_ID";
	    	}else{
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS    and mobileNo=:MOBILE_NO and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)";	
	    	}
	      Query qy = session.createQuery(qry);
	      if(otpfor.equalsIgnoreCase("1")){
	      qy.setInteger("STATUS", 0);
	      }else{
	    	 qy.setInteger("STATUS", 1);
	      }
	    	// qy.setInteger("ACT_STATUS", 1);
	       if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
		      qy.setInteger("USER_ID", Integer.parseInt(userId));
		      }
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeCode);
	      qy.setInteger("RESIDENT_GRP_CODE", residentCode);
	      qy.setString("MOBILE_NO", mobile);
	      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" signUpDaoServices======" + ex);
			 log.logMessage("otpDaoService Exception signUpDaoServices : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}
	@Override
	public boolean checkAccessMedia(String mobile) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where (statusFlag=:STATUS or statusFlag=:ACT_STATUS)  and mobileNo=:MOBILE_NO  and groupCode.accessMedia in (2,3)";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 0);
	      qy.setInteger("ACT_STATUS", 1);
	      qy.setString("MOBILE_NO", mobile);
	      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	      if(userdata!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	 result=false;
	    	ex.printStackTrace();
	    	System.out.println(" signUpDaoServices======" + ex);
			 log.logMessage("otpDaoService Exception signUpDaoServices : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
	
	@Override
	public boolean updateOtpUser(String mobno, String userId,
			String password, int otpcount) {
		UserMasterTblVo userdata = new UserMasterTblVo();
	    Session session = null;
	    Transaction tx = null;
	    Query qy = null;boolean result = false;
	    GroupMasterTblVo groupmang = null;
	    try {
	    	log = new Log();
	    	System.out.println("Step 1 :  user block [Start].");
	    	groupmang = new GroupMasterTblVo();
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	Date date;
			CommonUtils comutil=new CommonUtilsServices();
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	    			 qy = session.createQuery("update MvpUserOtpTbl set otp=:OTP,otpCount=:OTP_COUNT+1, otpGeneratedTime=:OTP_GEN_TIME where "
	    			 		+ "status=:STATUS "
	    			 		+ "and "
	    			 		+ "mobileNo=:MOBILE ");	 
		      qy.setString("MOBILE", mobno);
		      qy.setString("OTP", password);
		      qy.setParameter("OTP_COUNT", otpcount);
		      qy.setTimestamp("OTP_GEN_TIME", date);
		      qy.setInteger("STATUS", 1);
		      
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (HibernateException ex) {
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
	    	System.out.println(" updateOtpUser======" + ex);
			 log.logMessage("otpDaoService Exception updateOtpUser : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    System.out.println("Step 3 :user block [End].");
	    return result;
	}
	@Override
	public String checkFamilyInfo(String mobile,String otpfor) {
		MvpFamilymbrTbl userfamily = new MvpFamilymbrTbl();
		String result="";
		Session session = HibernateUtil.getSession();
		 String qry="";  String qry2="";
	    try {
	    		qry= " from MvpFamilymbrTbl where  fmbrPhNo=:MOBILE_NO and status=:STATUS";
	      Query qy = session.createQuery(qry);
	      if(otpfor.equalsIgnoreCase("1")){
	    	 qy.setInteger("STATUS", 0);
	      }else{
	    	  qy.setInteger("STATUS", 1);
	      }
	      qy.setString("MOBILE_NO", mobile);
	      userfamily = (MvpFamilymbrTbl) qy.uniqueResult();
	      if(userfamily!=null){
	    	  result="success";
	      }else{
	    	  result="mobilenotexist";
	      }
	    } catch (Exception ex) {
	    	 result="error";
	    	ex.printStackTrace();
	    	System.out.println(" checkFamilyInfo======" + ex);
			 log.logMessage("otpDaoService Exception checkFamilyInfo : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
	@Override
	public MvpFamilymbrTbl checkFamilyAccess(String mobile,String otpfor) {
		MvpFamilymbrTbl userfamily = new MvpFamilymbrTbl();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	  qry= " from MvpFamilymbrTbl where status=:STATUS  and fmbrPhNo=:MOBILE_NO  and fmbrProfAcc=:PROFILE_ACCESS"
	    	  		+ " and isInvited=:IS_INVITE and userId.groupCode.accessMedia in (2,3)";  
	    	  Query qy = session.createQuery(qry);
	    	  if(otpfor.equalsIgnoreCase("1")){
	    		  qy.setInteger("STATUS", 0);
	    	  }else{
	    		  qy.setInteger("STATUS", 1);
	    	  }
		      qy.setInteger("PROFILE_ACCESS", 1);
		      qy.setInteger("IS_INVITE", 1);
		      qy.setString("MOBILE_NO", mobile);
		      userfamily = (MvpFamilymbrTbl) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" signUpDaoServices======" + ex);
			 log.logMessage("otpDaoService Exception signUpDaoServices : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userfamily;
	}
	@Override
	public UserMasterTblVo getUserInfo(int userid) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where (statusFlag=:STATUS or statusFlag=:ACT_STATUS)  and userId=:USER_ID ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("ACT_STATUS", 0);
	      qy.setInteger("USER_ID", userid);
	      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getUserInfo======" + ex);
			 log.logMessage("otpDaoService Exception getUserInfo : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}

	@Override
	public boolean checkUserOtpVerify(String mobile,
			int committeeCode, int residentCode,String userId,String otpfor) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		System.out.println("==========otpfor===================="+otpfor);
		boolean result = false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	if(otpfor!=null && otpfor!="" && !otpfor.equalsIgnoreCase("")  ){
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS and mobileNo=:MOBILE_NO and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE) ";
	    	}else{
	    		qry= " from UserMasterTblVo where  statusFlag=:STATUS and  mobileNo=:MOBILE_NO and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)";	
	    	}
	      Query qy = session.createQuery(qry);
	      
	     // qy.setInteger("MOBILE_OTP_VERIFY", 1);
	       /*if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
		      qy.setInteger("USER_ID", Integer.parseInt(userId));
		      }*/
	       /*if(otpfor!=null && otpfor.equalsIgnoreCase("1")){
			 qy.setInteger("MOBILE_OTP_VERIFY", 0);
			// qy.setInteger("VERIFY_EMAIL_FLAG", 0);
	       }else if(otpfor!=null && otpfor.equalsIgnoreCase("2")){
	    	   qy.setInteger("MOBILE_OTP_VERIFY", 1);
	    	  // qy.setInteger("VERIFY_EMAIL_FLAG", 1);
	       }else{
	    	   qy.setInteger("MOBILE_OTP_VERIFY", 1);
	    	   //qy.setInteger("VERIFY_EMAIL_FLAG", 1);
	       }*/
	      if(otpfor.equalsIgnoreCase("1")){
	      qy.setInteger("STATUS", 0);
	      }else{
	    	  qy.setInteger("STATUS", 1);
	      }
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeCode);
	      qy.setInteger("RESIDENT_GRP_CODE", residentCode);
	      qy.setString("MOBILE_NO", mobile);
	      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	      if(userdata!=null){
	    	  result = true;
	      }else{
	    	  result = false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	result = false;
	    	System.out.println(" signUpDaoServices======" + ex);
			 log.logMessage("otpDaoService Exception signUpDaoServices : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
	@Override
	public boolean checkFamilyActiveSts(String mobile) {
		MvpFamilymbrTbl userfamily = new MvpFamilymbrTbl();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry=""; 
	    try {
	    		qry= " from MvpFamilymbrTbl where status=:STATUS  and fmbrPhNo=:MOBILE_NO ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("MOBILE_NO", mobile);
	      userfamily = (MvpFamilymbrTbl) qy.uniqueResult();
	      if(userfamily!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	result=false;
	    	ex.printStackTrace();
	    	System.out.println(" checkFamilyActiveSts======" + ex);
			 log.logMessage("otpDaoService Exception checkFamilyActiveSts : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
	@Override
	public boolean checFamilyOtpVerify(String mobile, String otpfor) {
		MvpFamilymbrTbl userfamily = new MvpFamilymbrTbl();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	  qry= " from MvpFamilymbrTbl where status=:STATUS  and fmbrPhNo=:MOBILE_NO  and fmbrProfAcc=:PROFILE_ACCESS"
	    	  		+ " and isInvited=:IS_INVITE and userId.groupCode.accessMedia in (2,3)";  
	    	  Query qy = session.createQuery(qry);
	    	  if(otpfor.equalsIgnoreCase("1")){
	    		  qy.setInteger("STATUS", 0);
	    	  }else{
	    		  qy.setInteger("STATUS", 1);
	    	  }
		      qy.setInteger("PROFILE_ACCESS", 1);
		      qy.setInteger("IS_INVITE", 1);
		      qy.setString("MOBILE_NO", mobile);
		      userfamily = (MvpFamilymbrTbl) qy.uniqueResult();
		      if(userfamily!=null){
		    	  result=true;
		      }else{
		    	  result=false;
		      }
	    } catch (Exception ex) {
	    	result=false;
	    	ex.printStackTrace();
	    	System.out.println(" checFamilyOtpVerify======" + ex);
			 log.logMessage("otpDaoService Exception checFamilyOtpVerify : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
	@Override
	public boolean checkUserActSts(String mobile) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS   and mobileNo=:MOBILE_NO  ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("MOBILE_NO", mobile);
	      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	      if(userdata!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	 result=false;
	    	ex.printStackTrace();
	    	System.out.println(" signUpDaoServices======" + ex);
			 log.logMessage("otpDaoService Exception signUpDaoServices : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
	@Override
	public UserMasterTblVo getFamilyDetailUserInfo(int useridParendid) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS   and parentId=:PARENT_ID  ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 0);
	      qy.setInteger("PARENT_ID", useridParendid);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	 result=false;
	    	ex.printStackTrace();
	    	System.out.println(" getFamilyDetailUserInfo======" + ex);
			 log.logMessage("signUpDaoServices Exception getFamilyDetailUserInfo : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}
	@Override
	public boolean checkUserBlockedActSts(String userid,String pass,int committeeCode,int residentCode) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
			qry= " from UserMasterTblVo where statusFlag=:STATUS and  (mobileNo=:USER_NAME or emailId=:USER_NAME) "
					+ " and password=md5(:PASSWORD)  and (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE) ";	
		      Query qy = session.createQuery(qry);
		      qy.setInteger("STATUS", 2);
		      qy.setString("USER_NAME", userid);
		      qy.setString("PASSWORD", pass);
		      qy.setInteger("COMMITTEE_GRP_CODE", committeeCode);
		      qy.setInteger("RESIDENT_GRP_CODE", residentCode);
		      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	      if(userdata!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	 result=false;
	    	ex.printStackTrace();
	    	System.out.println(" checkUserBlockedActSts======" + ex);
			 log.logMessage("signUpDaoServices Exception checkUserBlockedActSts : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
}
