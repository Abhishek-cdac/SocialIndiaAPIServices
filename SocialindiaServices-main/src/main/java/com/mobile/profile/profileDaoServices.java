package com.mobile.profile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobile.otpVo.MvpUserOtpTbl;
import com.mobile.otpVo.otpDaoService;
import com.mobile.signup.signUpDaoServices;
import com.pack.commonvo.FlatMasterTblVO;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.forum.forumServices;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.townShipMgmt.townShipMgmtDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class profileDaoServices implements  profileDao{
	Log log=new Log();

	@Override
	public UserMasterTblVo getUserPrfileInfo(String userId) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and  userId=:USER_ID";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkUserInfo======" + ex);
			 log.logMessage("profileDaoServices Exception profileDaoServices : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}

	@Override
	public int getInitTotal(String sqlQuery) {		
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQuery);
			totcnt = ((Number) qy.uniqueResult()).intValue();		
		} catch (Exception ex) {			
			System.out.println("Exceeption found in townShipMgmtDaoServices.getInitTotal() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getInitTotal() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.clear();session.close();session = null;}
			qy = null;
		}
		return totcnt;
	}

	@Override
	public List<MvpUsrSkillTbl> getUserPrfileList(String userId,String timestamp, String startlim, String totalrow) {
		List<MvpUsrSkillTbl> userSkillList = new ArrayList<MvpUsrSkillTbl>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from MvpUsrSkillTbl where skillSts=:STATUS and userId.userId=:USER_ID and enrtyDatetime <='"+timestamp+"'";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      qy.setInteger("STATUS", 1);
	      qy.setFirstResult(Integer.parseInt(startlim));
	      qy.setMaxResults(Integer.parseInt(totalrow));
	     // qy.setDate("ENTRY_DATE", startDate);
	    //  qy.setString("ENTRY_DATE", STR_TO_DATE('yyyy-MM-dd HH:mm:ss','%Y-%m-%d %H:%i:%S'));
	      userSkillList = qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkUserInfo======" + ex);
			 log.logMessage("profileDaoServices Exception getUserPrfileList : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userSkillList;
	}

	@Override
	public boolean deletePersonalSkill(String userId, String pskill_uid) {
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("delete from  MvpUsrSkillTbl "
		              + "  where userId.userId=:USER_ID and  uniqueId=:SKILL_UID");
		      qy.setInteger("USER_ID", Integer.parseInt(userId));
		      qy.setInteger("SKILL_UID", Integer.parseInt(pskill_uid));
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("profileDaoServices Exception deletePersonalSkill : "+ex, "error", profileDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session = null;}
		    }
		    return result;
	}
	@Override
	public boolean updatePersonalSkill(MvpUsrSkillTbl userSkillInfo) {
		 boolean result=false;
			Session session = HibernateUtil.getSession();
			 Transaction tx = null;
			 try {		    		   
	       	  tx = session.beginTransaction();
	           session.save(userSkillInfo);
	           tx.commit();
	           result = true;
	} catch (HibernateException ex) {	
		if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
		System.out.println("Exception found updatePersonalSkill  : "+ex);
	} finally {
		if(session!=null){session.flush();session.clear();session.close();session=null;}
	}		   
	return result;
	}

	@Override
	public boolean deleteExistPersonalSkill(String userId,String skillid,String categoryid) {
		System.out.println("=======userId==="+userId);
		MvpUsrSkillTbl myskillMst=new MvpUsrSkillTbl();
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("from  MvpUsrSkillTbl where ivrBnSKILL_ID.ivrBnSKILL_ID=:SKILL_ID and iVOCATEGORY_ID.iVOCATEGORY_ID=:CATE_ID "
		          		+ " and userId.userId=:USER_ID and skillSts=:STATUS");
		      qy.setInteger("USER_ID", Integer.parseInt(userId));
		      qy.setInteger("SKILL_ID", Integer.parseInt(skillid));
		      qy.setInteger("CATE_ID", Integer.parseInt(categoryid));
		      qy.setInteger("STATUS", 1);
		      myskillMst=(MvpUsrSkillTbl) qy.uniqueResult();
		      if(myskillMst!=null){
		      result = true;
		      }else{
		    	 result = false;
		      }
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("profileDaoServices Exception deleteExistPersonalSkill : "+ex, "error", profileDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session = null;}
		    }
		    return result;
	}

	@Override
	public UserMasterTblVo verifyLoginDetail(String userId, String password) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS and (groupCode.groupCode='5' or groupCode.groupCode='6') and  (mobileNo=:USER_NAME or emailId=:USER_NAME)  and password=md5(:PASSWORD) ";	
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("USER_NAME", userId);
	      qy.setString("PASSWORD", password);
	     // qy.setInteger("VERIFY_OTP_FLAG", 1);
	      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" verifyLoginDetail======" + ex);
			 log.logMessage("profileDaoServices Exception verifyLoginDetail : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}

	@Override
	public UserMasterTblVo checkAccessPermission(String userId, String password,
			int committeeid, int residentid) {
		UserMasterTblVo userData=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and (mobileNo=:USER_NAME or emailId=:USER_NAME) "
	    				+ " and password=md5(:PASSWORD) "
	    				+ "and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE) "
	    				+ "and groupCode.accessMedia in (2,3)";	
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeid);
	      qy.setInteger("RESIDENT_GRP_CODE", residentid);
	      qy.setString("USER_NAME", userId);
	      qy.setString("PASSWORD", password);
	      qy.setMaxResults(1);	      
	      userData = (UserMasterTblVo) qy.uniqueResult();
	     
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkAccessPermission======" + ex);
			 log.logMessage("profileDaoServices Exception signUpDaoServices : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userData;
	}

	@Override
	public List<UserMasterTblVo> getUserSocietyDeatils(String username,int committeeid,int residentid,String otpfor) {
		List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and (mobileNo=:USER_NAME or emailId=:USER_NAME) and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)";
	      Query qy = session.createQuery(qry);
	      if(otpfor.equalsIgnoreCase("1")){
	      qy.setInteger("STATUS", 0);
	      }else{
	    	  qy.setInteger("STATUS", 1);
	      }
	      System.out.println("==otpfor=="+otpfor);
	      qy.setString("USER_NAME", username);
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeid);
	      qy.setInteger("RESIDENT_GRP_CODE", residentid);
	      userInfoList =  qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getUserSocietyDeatils======" + ex);
			 log.logMessage("profileDaoServices Exception getUserSocietyDeatils : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfoList;
	}

	@Override
	public boolean checkSocietyKey(String societykey,String emailid) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS and   emailId=:EMAIL_ID ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("SOCIETY_KEY", societykey);
	      qy.setString("EMAIL_ID", emailid);
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      if(userInfo!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	 result=false;
	    	System.out.println(" checkSocietyKey======" + ex);
			 log.logMessage("profileDaoServices Exception checkSocietyKey : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean checkCreateLoginDetails(String soc_type,String soc_id,String soc_fname,String soc_lname,String soc_profile_url,String emailid,String password,String mobileno,int userid) {
		Session session = HibernateUtil.getSession();
		boolean result=false;
		Transaction tx=null;
	    try {
	    	/*	qry= " from UserMasterTblVo where  mobileNo=:MOBILE_NO and   emailId=:EMAIL_ID ";
	      Query qy = session.createQuery(qry);
	      qy.setString("MOBILE_NO",mobileno);
	      qy.setString("EMAIL_ID", emailid);
	      //qy.setInteger("STATUS", 1);
	      System.out.println("========userInfo======="+userInfo.getUserId());
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      if(userInfo!=null){*/
	    	Query qy1 = null;
	    	  tx = session.beginTransaction();
	    	  if(mobileno!=null && !mobileno.equalsIgnoreCase("") && !mobileno.equalsIgnoreCase("null")){
		       qy1= session
		          .createQuery("update UserMasterTblVo set statusFlag='"+1+"',emailVerifyFlag=:EMAIL_VERIFY, mobileOtpVerifyFlag=:OTP_VERIFY ,  "
		              + " password=md5(:PASSWORD),firstName=:FIRST_NAME,lastName=:LAST_NAME,socialType=:SOCIAL_TYPE,"
		              + " socialId=:SOCIAL_ID, socialProfileUrl=:SOCIAL_PROFILE_URL,encryprPassword=:ENCRY_PASSWORD, "
		              + "  emailId=:EMAIL_ID where  userId='"+userid+"'  and mobileNo=:MOBILE_NO ");
	    	  }else{
	    		  qy1= session
	    		          .createQuery("update UserMasterTblVo set statusFlag='"+1+"',emailVerifyFlag=:EMAIL_VERIFY, mobileOtpVerifyFlag=:OTP_VERIFY ,  "
	    		              + " password=md5(:PASSWORD),firstName=:FIRST_NAME,lastName=:LAST_NAME,socialType=:SOCIAL_TYPE,"
	    		              + " socialId=:SOCIAL_ID, socialProfileUrl=:SOCIAL_PROFILE_URL,encryprPassword=:ENCRY_PASSWORD, "
	    		              + "  emailId=:EMAIL_ID where  userId='"+userid+"' ");
	    	  }
		      qy1.setString("PASSWORD", password);
		      qy1.setString("ENCRY_PASSWORD", EncDecrypt.encrypt(password));
		      qy1.setString("FIRST_NAME", soc_fname);
		      qy1.setString("LAST_NAME", soc_lname);
		      qy1.setInteger("SOCIAL_TYPE", Integer.parseInt(soc_type));
		      qy1.setString("SOCIAL_ID", soc_id);
		      qy1.setInteger("EMAIL_VERIFY", 1);
		      qy1.setInteger("OTP_VERIFY", 1);
		      qy1.setString("SOCIAL_PROFILE_URL", soc_profile_url);
		      if(mobileno!=null && !mobileno.equalsIgnoreCase("") && !mobileno.equalsIgnoreCase("null")){
		      qy1.setString("MOBILE_NO",mobileno);
		      }
		      qy1.setString("EMAIL_ID",emailid);
		      qy1.executeUpdate();
		      tx.commit(); 
	    	  result=true;
	     /* }else{
	    	  result=false;
	      }*/
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	 result=false;
	    	System.out.println(" checkCreateLoginDetails======" + ex);
			 log.logMessage("profileDaoServices Exception checkCreateLoginDetails : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public UserMasterTblVo checkSiLoginDetails(String mobileno, String emailid) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where mobileNo=:MOBILE_NO and statusFlag=:FLAG";
	      Query qy = session.createQuery(qry);
	      qy.setString("MOBILE_NO", mobileno);
	      qy.setInteger("FLAG", 0);
	      qy.setMaxResults(1);
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkSiLoginDetails======" + ex);
			 log.logMessage("profileDaoServices Exception checkSiLoginDetails : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfo;
	}

	@Override
	public boolean updateCreateLoginDetails(String societykey, String emailid) {
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update UserMasterTblVo set statusFlag=:STATUS, "
		              + " modifyDatetime=:MODY_DATETIME where societyId.activationKey=:SOCIETY_KEY and  emailId=:EMAIL_ID and statusFlag!=3");
		      qy.setInteger("STATUS", 1);
		      qy.setString("SOCIETY_KEY", societykey);
		      qy.setString("EMAIL_ID", emailid);
		      qy.setTimestamp("MODY_DATETIME", date1);
		      qy.executeUpdate();
		      tx.commit();
		      System.out.println("===da32432443232te1===="+date1);
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      System.out.println("=====HibernateException===updateCreateLoginDetails========"+ex);
		      log.logMessage("profileDaoServices Exception updateCreateLoginDetails : "+ex, "error", profileDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session = null;}
		    }
		    return result;
	}

	@Override
	public boolean emailVerifivcation(String email, String flag,String userid) {
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update UserMasterTblVo set statusFlag=:STATUS, emailVerifyFlag=:EMAIL_VERIFY_FLAG "
		              + "  where emailId=:EMAIL_ID and userId=:USER_Id");
		      qy.setInteger("STATUS", 1);
		      qy.setString("EMAIL_ID", email);
		      qy.setInteger("EMAIL_VERIFY_FLAG", 1);
		      qy.setInteger("USER_Id", Integer.parseInt(userid));
		      //qy.setDate("MODY_DATETIME", date1);
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      System.out.println("=====HibernateException===emailVerifivcation========"+ex);
		      log.logMessage("profileDaoServices Exception emailVerifivcation : "+ex, "error", profileDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session = null;}
		    }
		    return result;
	}

	@Override
	public boolean checkMobnoOtp(String mob, String otp) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		MvpUserOtpTbl otpMst=new MvpUserOtpTbl();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and mobileNo=:MOBILE_NO  ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("MOBILE_NO", mob);
	      qy.setMaxResults(1);
	      userInfo=  (UserMasterTblVo) qy.uniqueResult();
	      System.out.println("===userInfo==dd="+userInfo.getUserId());
	      if(userInfo!=null){
	    	  qry= " from MvpUserOtpTbl where status=:STATUS  and otp=:OTP and  mobileNo=:MOBILE order by entryDatetime desc";
		      Query qy1 = session.createQuery(qry);
		      qy1.setInteger("STATUS", 0);
		     qy1.setString("OTP", otp);  
		     qy1.setString("MOBILE", userInfo.getMobileNo());
		     qy1.setMaxResults(1);
		     otpMst=  (MvpUserOtpTbl) qy1.uniqueResult();
		     if(otpMst!=null){
		    	 result=true;
		     }else{
		    	 result=false; 
		     }
	      }
	    } catch (Exception ex) {
	    	 result=false; 
	    	ex.printStackTrace();
	    	System.out.println(" checkMobnoOtp======" + ex);
			 log.logMessage("profileDaoServices Exception checkMobnoOtp : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean updateNewPassword(String mob, String pass) {
		boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    Date date1;
	    CommonUtils comutil=new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	    try {
	      tx = session.beginTransaction();
	      Query qy = session
	          .createQuery("update UserMasterTblVo set  statusFlag=:STATUS,  "
	              + "password=md5(:PASSWORD) where mobileNo=:MOBILE_NO and (statusFlag=1 or statusFlag=0)");
	      qy.setInteger("STATUS", 1);
	      qy.setString("MOBILE_NO", mob);
	      qy.setString("PASSWORD",pass);
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("profileDaoServices Exception updateNewPassword : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear();session.close();session = null;}
	    }
	    return result;
	}

	@Override
	public int getInitTotalViaSql(String sqlQuery) {
		// TODO Auto-generated method stub
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createSQLQuery(sqlQuery);
			totcnt = ((Number) qy.uniqueResult()).intValue();		
		} catch (Exception ex) {			
			System.out.println("Exceeption found in townShipMgmtDaoServices.getInitTotal() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getInitTotal() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.clear();session.close();session = null;}
			qy = null;
		}
		return totcnt;
	}

	@Override
	public boolean checkSICreateLoginDetails(String mobileno, String emailid,
			String pass,int userid) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		Transaction tx=null;
		 Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		 String qry="";
	    try {
	    		/*qry= " from UserMasterTblVo where mobileNo=:MOBILE_NO and   emailId=:EMAIL_ID ";
	      Query qy = session.createQuery(qry);
	      qy.setString("MOBILE_NO",mobileno);
	      qy.setString("EMAIL_ID", emailid);
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      System.out.println("====userInfo===="+userInfo.getUserId());
	      if(userInfo!=null){
	    	  System.out.println("====userInfo===="+userInfo.getUserId());*/
	    	System.out.println("============sdf==============");
	    	  tx = session.beginTransaction();
		      Query qy1 = session
		          .createQuery("update UserMasterTblVo set password=md5(:PASSWORD) , statusFlag='"+1+"', "
		              + " mobileOtpVerifyFlag=:OTP_VERIFY ,  emailId='"+emailid+"'  where userId='"+userid+"'  and mobileNo=:MOBILE_NO and statusFlag=0");
		      qy1.setString("MOBILE_NO",mobileno);
		      qy1.setInteger("OTP_VERIFY",1);
		      qy1.setString("PASSWORD",pass);
		      qy1.executeUpdate();
		      System.out.println("==ee==========sdf==============");
		      tx.commit(); 
	    	  result=true;
	     /* }else{
	    	  result=false;
	      }*/
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	 result=false;
	    	System.out.println(" checkCreateLoginDetails======" + ex);
			 log.logMessage("profileDaoServices Exception checkCreateLoginDetails : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean verifyLoginDetailWithOtp(String userId,
			String password) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS and mobileOtpVerifyFlag=:VERIFY_OTP_FLAG  and mobileNo=:USER_NAME   and password=md5(:PASSWORD) ";	
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("VERIFY_OTP_FLAG", 1);
	      qy.setString("USER_NAME", userId);
	      qy.setString("PASSWORD", password);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	      if(userdata!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	result=false;
	    	System.out.println(" verifyLoginDetailWithOtp======" + ex);
			 log.logMessage("profileDaoServices Exception verifyLoginDetailWithOtp : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean verifyLoginDetailWithEmail(String userId, String password) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		//qry= " from UserMasterTblVo where statusFlag=:STATUS and (groupCode.groupCode='5' or groupCode.groupCode='6') and emailVerifyFlag=:VERIFY_EMAIL_FLAG  and emailId=:USER_NAME   and password=md5(:PASSWORD) ";
	    	qry= " from UserMasterTblVo where statusFlag=:STATUS and (groupCode.groupCode='5' or groupCode.groupCode='6') and emailId=:USER_NAME   and password=md5(:PASSWORD) ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
//	      qy.setInteger("VERIFY_EMAIL_FLAG", 1);
	      qy.setString("USER_NAME", userId);
	      qy.setString("PASSWORD", password);
	      qy.setMaxResults(1);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	      if(userdata!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	result=false;
	    	System.out.println(" verifyLoginDetailWithEmail======" + ex);
			 log.logMessage("profileDaoServices Exception verifyLoginDetailWithEmail : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean checkCreateLoginForFamilyDetails(String soc_type,
			String soc_id, String soc_fname, String soc_lname,
			String soc_profile_url, String emailid, String password,
			String mobileno) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		Transaction tx=null;
		 String qry="";
	    try {
	    	/*	qry= " from UserMasterTblVo where  mobileNo=:MOBILE_NO and   emailId=:EMAIL_ID ";
	      Query qy = session.createQuery(qry);
	      qy.setString("MOBILE_NO",mobileno);
	      qy.setString("EMAIL_ID", emailid);
	      //qy.setInteger("STATUS", 1);
	      System.out.println("========userInfo======="+userInfo.getUserId());
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      if(userInfo!=null){*/
	    	  tx = session.beginTransaction();
		      Query qy1 = session
		          .createQuery("update MvpFamilymbrTbl set status='"+1+"',EmailVerifyFlg=:EMAIL_VERIFY,  "
		              + " mobileOtpFlg=:MOBILE_VERIFY, fmbrPswd=md5(:PASSWORD),fmbrName=:FIRST_NAME,socialType=:SOCIAL_TYPE,"
		              + " socialId=:SOCIAL_ID, socialProfileUrl=:SOCIAL_PROFILE_URL, "
		              + "  fmbrEmail=:EMAIL_ID where fmbrPhNo=:MOBILE_NO ");
		      qy1.setString("PASSWORD", password);
		    //  qy1.setString("ENCRY_PASSWORD", EncDecrypt.encrypt(password));
		      qy1.setString("FIRST_NAME", soc_fname);
		    //  qy1.setString("LAST_NAME", soc_lname);
		      qy1.setInteger("SOCIAL_TYPE", Integer.parseInt(soc_type));
		      qy1.setString("SOCIAL_ID", soc_id);
		      qy1.setInteger("EMAIL_VERIFY", 1);
		      qy1.setInteger("MOBILE_VERIFY", 1);
		      qy1.setString("SOCIAL_PROFILE_URL", soc_profile_url);
		      qy1.setString("MOBILE_NO",mobileno);
		      qy1.setString("EMAIL_ID",emailid);
		      qy1.executeUpdate();
		      tx.commit(); 
	    	  result=true;
	     /* }else{
	    	  result=false;
	      }*/
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	 result=false;
	    	System.out.println(" checkCreateLoginForFamilyDetails======" + ex);
			 log.logMessage("profileDaoServices Exception checkCreateLoginForFamilyDetails : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public MvpFamilymbrTbl checkSiLoginForFamilyDetails(String mob, String email) {
		MvpFamilymbrTbl familyMst=new MvpFamilymbrTbl();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from MvpFamilymbrTbl where fmbrPhNo=:MOBILE_NO and status=:FLAG";
	      Query qy = session.createQuery(qry);
	      qy.setString("MOBILE_NO", mob);
	      qy.setInteger("FLAG", 0);
	      familyMst =  (MvpFamilymbrTbl) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkSiLoginForFamilyDetails======" + ex);
			 log.logMessage("profileDaoServices Exception checkSiLoginForFamilyDetails : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return familyMst;
	}

	@Override
	public boolean emailForFamilyVerifivcation(String email, String flag,
			String userid) {
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update MvpFamilymbrTbl set status=:STATUS, EmailVerifyFlg=:EMAIL_VERIFY_FLAG "
		              + "  where fmbrEmail=:EMAIL_ID and userId=:USER_Id");
		      qy.setInteger("STATUS", 1);
		      qy.setString("EMAIL_ID", email);
		      qy.setInteger("EMAIL_VERIFY_FLAG", 1);
		      qy.setInteger("USER_Id", Integer.parseInt(userid));
		      //qy.setDate("MODY_DATETIME", date1);
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      System.out.println("=====HibernateException===emailForFamilyVerifivcation========"+ex);
		      log.logMessage("profileDaoServices Exception emailForFamilyVerifivcation : "+ex, "error", profileDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session = null;}
		    }
		    return result;
	}

	@Override
	public boolean checkSICreateForFamilyLoginDetails(String mob, String email,
			String pass, int userid) {
		Session session = HibernateUtil.getSession();
		boolean result=false;
		Transaction tx=null;
		 Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		 String qry="";
	    try {
	    		/*qry= " from UserMasterTblVo where mobileNo=:MOBILE_NO and   emailId=:EMAIL_ID ";
	      Query qy = session.createQuery(qry);
	      qy.setString("MOBILE_NO",mobileno);
	      qy.setString("EMAIL_ID", emailid);
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      System.out.println("====userInfo===="+userInfo.getUserId());
	      if(userInfo!=null){
	    	  System.out.println("====userInfo===="+userInfo.getUserId());*/
	    	  tx = session.beginTransaction();
		      Query qy1 = session
		          .createQuery("update MvpFamilymbrTbl set fmbrPswd=md5(:PASSWORD), status='"+1+"', "
		              + "  mobileOtpFlg=:MOBILE_VERIFY,  fmbrEmail='"+email+"'  where userId='"+userid+"' "
		              		+ " and fmbrPhNo=:MOBILE_NO ");
		      qy1.setString("MOBILE_NO",mob);
		      qy1.setString("PASSWORD",pass);
		      qy1.setInteger("MOBILE_VERIFY", 1);
		      qy1.executeUpdate();
		      tx.commit(); 
	    	  result=true;
	     /* }else{
	    	  result=false;
	      }*/
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	 result=false;
	    	System.out.println(" checkSICreateForFamilyLoginDetails======" + ex);
			 log.logMessage("profileDaoServices Exception checkSICreateForFamilyLoginDetails : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean emailExistCheck(String email, String flag, String userid) {
		Session session = HibernateUtil.getSession();
		 String qry="";
			UserMasterTblVo userInfo=new UserMasterTblVo();
		 boolean result=false;
	    try {
	    		qry= " from UserMasterTblVo where emailId=:EMAIL_ID and userId=:USER_Id";
	      Query qy = session.createQuery(qry);
	      qy.setString("EMAIL_ID", email);
	      qy.setInteger("USER_Id", Integer.parseInt(userid));
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      if(userInfo!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	result=false;
	    	System.out.println(" emailExistCheck======" + ex);
			 log.logMessage("profileDaoServices Exception emailExistCheck : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean verifyLoginDetailForFamilyWithEmail(String userId,
			String password) {
		MvpFamilymbrTbl familydata = new MvpFamilymbrTbl();
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from MvpFamilymbrTbl where status=:STATUS and EmailVerifyFlg=:VERIFY_EMAIL_FLAG  and fmbrEmail=:USER_NAME   and  fmbrPswd=md5(:PASSWORD) ";	
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("VERIFY_EMAIL_FLAG", 1);
	      qy.setString("USER_NAME", userId);
	      qy.setString("PASSWORD", password);
	      familydata = (MvpFamilymbrTbl) qy.uniqueResult();
	      if(familydata!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	result=false;
	    	System.out.println(" verifyLoginDetailForFamilyWithEmail======" + ex);
			 log.logMessage("profileDaoServices Exception verifyLoginDetailForFamilyWithEmail : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public List<UserMasterTblVo> getUserSocietyForFamilyDeatils(
			String userid, int committeeid, int residentid) {
		List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where (statusFlag=:STATUS or statusFlag=:ACT_STATUS) and userId=:USER_ID and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 0);
	      qy.setInteger("ACT_STATUS", 1);
	      qy.setInteger("USER_ID", Integer.parseInt(userid));
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeid);
	      qy.setInteger("RESIDENT_GRP_CODE", residentid);
	      userInfoList =  qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getUserSocietyForFamilyDeatils======" + ex);
			 log.logMessage("profileDaoServices Exception getUserSocietyForFamilyDeatils : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfoList;
	}

	@Override
	public boolean updateImageName(String fileName, String userid) {
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update UserMasterTblVo set "
		              + " imageNameWeb=:FILENAME , imageNameMobile=:FILENAME where userId=:USER_ID ");
		      qy.setString("FILENAME", fileName);
		      qy.setInteger("USER_ID", Integer.parseInt(userid));
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      System.out.println("=====HibernateException===updateImageName========"+ex);
		      log.logMessage("profileDaoServices Exception updateImageName : "+ex, "error", profileDaoServices.class);
		    } finally {
		      session.close();
		    }
		    return result;
	}

	@Override
	public boolean checkSkillExist(String userId, String skillid) {
		 boolean result = false;
		 MvpUsrSkillTbl userSkillMst=new MvpUsrSkillTbl();
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("from  MvpUsrSkillTbl "
		              + "  where userId.userId=:USER_ID and  uniqueId=:SKILL_UID");
		      qy.setInteger("USER_ID", Integer.parseInt(userId));
		      qy.setInteger("SKILL_UID", Integer.parseInt(skillid));
		      userSkillMst=(MvpUsrSkillTbl) qy.uniqueResult();
		      if(userSkillMst!=null){
		      result = true;
		      }else{
		    	  result = false;
		      }
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("profileDaoServices Exception checkSkillExist : "+ex, "error", profileDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session = null;}
		    }
		    return result;
	}

	@Override
	public boolean checkProfileInfo(String userId,String email) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
		 boolean result=false;
	    try {
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and  userId=:USER_ID and emailId=:EMAIL";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      qy.setString("EMAIL", email);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	      if(userdata!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	result=false;
	    	System.out.println(" checkProfileInfo======" + ex);
			 log.logMessage("profileDaoServices Exception checkProfileInfo : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public boolean updateUserData(String rid, String profile_name,
			String profile_email, String profile_proof_id,
			String profile_proof_value) {
		boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    Query qy=null;
	    try {
	      tx = session.beginTransaction();
	      System.out.println("===profile_email==="+profile_email);
	      if(profile_email.equalsIgnoreCase("")){
	    	  System.out.println("===profile_email==hhhhhhhh=");
	       qy = session
	          .createQuery("update UserMasterTblVo set "
	              + " firstName=:FNAME , iVOcradid=:proof_Id , idProofNo=:proof_value where userId=:USER_ID ");
	      }else{
	    	  System.out.println("===profile_email==ssssssssssss=");
	    	  qy = session
	    	          .createQuery("update UserMasterTblVo set "
	    	              + " firstName=:FNAME , iVOcradid=:proof_Id , idProofNo=:proof_value ,"
	    	              + " emailId=:EMAIL , emailVerifyFlag=:EMAIL_VERIFY_FLG where userId=:USER_ID "); 
	    	  
	    	  
	      }
	      qy.setInteger("USER_ID", Integer.parseInt(rid));
	      qy.setString("FNAME", profile_name);
	      if(profile_proof_id!=null && !profile_proof_id.equalsIgnoreCase("")){
	      qy.setInteger("proof_Id", Integer.parseInt(profile_proof_id));
	      }else{
	    	  qy.setString("proof_Id", null);
	      }
	      if(profile_proof_value!=null && !profile_proof_value.equalsIgnoreCase("")){
	    	  qy.setString("proof_value", profile_proof_value);  
		      }else{
		    	  qy.setString("proof_value", null);
		      }
	    
	      if(!profile_email.equalsIgnoreCase("") && profile_email!=null ){
	    	  qy.setString("EMAIL", profile_email);  
	    	  qy.setInteger("EMAIL_VERIFY_FLG", 0);  
	      }
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      System.out.println("=====HibernateException===updateUserData========"+ex);
	      log.logMessage("profileDaoServices Exception updateUserData : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear();session.close();session = null;}
	    }
	    return result;
	}

	@Override
	public UserMasterTblVo getUserInfo(int userid) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		String qry="";
	    try {
	      qry= " from UserMasterTblVo where statusFlag=:STATUS and userId=:USER_ID";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("USER_ID", userid);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
			log.logMessage("profileDaoServices Exception getUserInfo : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}

	@Override
	public FlatMasterTblVO getFlatUserInfo(int userid) {
		FlatMasterTblVO flatUserdata = new FlatMasterTblVO();
		Session session = HibernateUtil.getSession();
		String qry="";
	    try {
	      qry= " from FlatMasterTblVO where usrid =:USR_ID and status=:STATUS and ivrBnismyself=:IS_MYSELF";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("USR_ID", userid);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("IS_MYSELF", 1);
	      flatUserdata = (FlatMasterTblVO) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
			log.logMessage("profileDaoServices Exception getFlatUserInfo : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return flatUserdata;
	}

	@Override
	public List getRestPasswordUsers(String mob, String pass) {
		// TODO Auto-generated method stub
	    List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from UserMasterTblVo where mobileNo=:MOBILE_NO and (statusFlag=1 or statusFlag=0)";
	      Query qy = session.createQuery(qry);
	      qy.setString("MOBILE_NO", mob);
	      userInfoList =  qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getUserSocietyForFamilyDeatils======" + ex);
			 log.logMessage("profileDaoServices Exception getUserSocietyForFamilyDeatils : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfoList;
	}

	

}
