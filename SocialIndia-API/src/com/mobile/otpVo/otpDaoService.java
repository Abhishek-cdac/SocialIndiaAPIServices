package com.mobile.otpVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobile.profile.profileDaoServices;
import com.mobile.signup.signUpDaoServices;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class otpDaoService implements  otpDao {
	Log log=new Log();
	
	@Override
	public boolean checkTownshipKey( String userId,String townshipKey) {
	/*	UserMasterTblVo userData = new UserMasterTblVo();
		Session session = null;
		Query qy = null;
		String qry="";
		session = HibernateUtil.getSession();*/	
		boolean result = true;
		try {
			
		/*	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
				qry = "from UserMasterTblVo where societyId.townshipId.status=:STATUS and  societyId.townshipId.activationKey=:TOWNSHIP_KEY"
						+ " and  userId=:USER_ID ";
			}else{
			 qry = "from UserMasterTblVo where societyId.townshipId.status=:STATUS and  societyId.townshipId.activationKey=:TOWNSHIP_KEY";
			}
			System.out.println(userId+"qry------------"+qry);
			 qy = session.createQuery(qry);
			qy.setString("TOWNSHIP_KEY",townshipKey);
			qy.setMaxResults(1);
			qy.setInteger("STATUS", 1);
			 if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
			 qy.setInteger("USER_ID", Integer.parseInt(userId));
			      }
			userData = (UserMasterTblVo) qy.uniqueResult();
			if(userData!=null){
				result=true;
			}else{
				result=false;
			}*/
		} catch (HibernateException ex) {
			System.out.println(" getOtpTblInfo=checkTownshipKey=====" + ex);
			 log.logMessage("otpDaoService Exception checkTownshipKey : "+ex, "error", otpDaoService.class);
		} finally {
			/*if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;*/
		}
		return result;
	}
	
	@Override
	public boolean checkTownshipKeyWithoutRid(String townshipKey) {
		boolean result = true;
		
		/*TownshipMstTbl townShipData = new TownshipMstTbl();
		Session session = null;
		Query qy = null;
		
		try {
			session = HibernateUtil.getSession();			
			String qry = "From TownshipMstTbl where status=:STATUS and activationKey=:TOWNSHIP_KEY";
			qy = session.createQuery(qry);
			qy.setString("TOWNSHIP_KEY",townshipKey);	
			qy.setInteger("STATUS", 1);
			townShipData = (TownshipMstTbl) qy.uniqueResult();
			if(townShipData!=null){
				result=true;
			}else{
				result=false;
			}
		} catch (HibernateException ex) {
			System.out.println(" getOtpTblInfo======" + ex);
			 log.logMessage("otpDaoService Exception checkTownshipKey : "+ex, "error", otpDaoService.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}*/
		return result;
	}
	@Override
	public UserMasterTblVo checkUserInfo(String mobno, String userId) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	System.out.println("=====dfsdfds===="+userId);
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and mobileNo=:MOBILE_NO and  userId=:USER_ID";
	    	}else{
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and mobileNo=:MOBILE_NO";
	    	}
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setString("MOBILE_NO", mobno);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkUserInfo======" + ex);
			 log.logMessage("otpDaoService Exception checkUserInfo : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}
	@Override
	public String getSysParamsInfo(String len) {
		MvpSystemParameterTbl sysParamInfo = new MvpSystemParameterTbl();
		Session session = HibernateUtil.getSession();
		String result="";
		try {
			String qry = "select value From MvpSystemParameterTbl where  status=:STATUS and key=:KEY ";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setString("KEY", len);
			qy.setMaxResults(1);
			System.out.println("----------getSysParamsInfo------------" + qy);
			result = (String) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println(" getSysParamsInfo======" + ex);
			 log.logMessage("otpDaoService Exception getSysParamsInfo : "+ex, "error", otpDaoService.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
		}
		return result;
	}
	@Override
	public boolean updateOtpUser(String mobno, String userId,
			String password, String type,int otpcount) {
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
	    	Date date=new Date();
			CommonUtils comutil=new CommonUtilsServices();
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			if(type.equalsIgnoreCase("2")){    
	    		qy = session.createQuery("update MvpUserOtpTbl set otp=:OTP,otpCount=:OTP_COUNT+1,otpGeneratedTime=:OTP_GEN_TIME where status=:STATUS and  mobileNo=:MOBILE ");
	    		 }else if(type.equalsIgnoreCase("1")){
	    			 qy = session.createQuery("update MvpUserOtpTbl set otp=:OTP,otpCount=:OTP_COUNT+1,otpGeneratedTime=:OTP_GEN_TIME where status=:STATUS and  mobileNo=:MOBILE ");	 
	    		 }
		    //  qy.setInteger("USER_ID", Integer.parseInt(userId));
		     // if(type.equalsIgnoreCase("2")){
		      qy.setInteger("OTP_COUNT", otpcount);
		    // }
		      qy.setString("OTP", password);
		      qy.setString("MOBILE", mobno);
		      qy.setInteger("STATUS", 1);
		      qy.setTimestamp("OTP_GEN_TIME", date);
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
	public MvpUserOtpTbl checkUserOtpValidate(String mobno, String userId,
			String otp, String otpfor) {
		MvpUserOtpTbl userOtpInfo = new MvpUserOtpTbl();
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		 tx = session.beginTransaction();
		 String qry="";
	    try {
	    	System.out.println("=====userId==="+userId);
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from MvpUserOtpTbl where status=:STATUS   and mobileNo=:MOBILE_NO and otp=:OTP and userId=:USER_ID";
	    	}else{
	    		qry= " from MvpUserOtpTbl where status=:STATUS  and mobileNo=:MOBILE_NO and otp=:OTP";
	    	}
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      //qy.setInteger("ACT_STATUS", 0);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setString("OTP", otp);
	      qy.setString("MOBILE_NO", mobno);
	      userOtpInfo = (MvpUserOtpTbl) qy.uniqueResult();
	      if(userOtpInfo!=null){
	    	 Query  qy1 = session.createQuery("update MvpUserOtpTbl set status=:STATUS  where  mobileNo=:MOBILE_NO");	 
			     qy1.setInteger("STATUS", 0);
			    // qy1.setInteger("OTP_COUNT", 1);
		    	 qy1.setString("MOBILE_NO", mobno);
		    	 qy1.executeUpdate();
		    	/* Query  qy2 = session.createQuery("update UserMasterTblVo set mobileOtpVerifyFlag=:MOBILE_OTP_VERIFY"
		    	 		+ "  where  userId=:USER_ID ");	 
		    	 qy2.setInteger("USER_ID",Integer.parseInt(userId));
			     qy2.setInteger("MOBILE_OTP_VERIFY", 1);
			     //qy2.setInteger("STATUS_FLAG", 1);
			     qy2.executeUpdate();
			     System.out.println("=====userId==="+userId);*/
		    	tx.commit();
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkUserOtpValidate======" + ex);
			 log.logMessage("otpDaoService Exception checkUserOtpValidate : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userOtpInfo;
	}
	@Override
	public List<UserMasterTblVo> getUserDeatils(String mobno, String userId,int committeeid,int residentid) {
		List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where statusFlag=:ACT_STATUS and  mobileNo=:MOBILE_NO and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)  and userId=:USER_ID ";
	    	}else{
	    		qry= " from UserMasterTblVo where statusFlag=:ACT_STATUS and  mobileNo=:MOBILE_NO and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)";
	    	}
	      Query qy = session.createQuery(qry);
	     // qy.setInteger("STATUS", 1);
	      qy.setInteger("ACT_STATUS", 0);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setString("MOBILE_NO", mobno);
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeid);
	      qy.setInteger("RESIDENT_GRP_CODE", residentid);
	     // qy.setString("OTP", otp);
	      userInfoList =  qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getUserDeatils======" + ex);
			 log.logMessage("otpDaoService Exception getUserDeatils : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfoList;
	}
	@Override
	public MvpUserOtpTbl checkOtpInfo(String mobile) {
		MvpUserOtpTbl userOtpInfo = new MvpUserOtpTbl();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	System.out.println("=====dfsdfds===="+mobile);
	    		qry= " from MvpUserOtpTbl where (status=:STATUS or status=:STATUS_EXCEED) and  mobileNo=:MOBILE";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("STATUS_EXCEED",2);
	      qy.setMaxResults(1);
	      qy.setString("MOBILE", mobile);
	      userOtpInfo = (MvpUserOtpTbl) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkOtpInfo======" + ex);
			 log.logMessage("otpDaoService Exception checkOtpInfo : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userOtpInfo;
	}
	@Override
	public boolean insertOtpInfo(MvpUserOtpTbl userOtpInfo) {
		 boolean result=false;
		Session session = HibernateUtil.getSession();
		System.out.println("======userOtpInfo=gdf=="+userOtpInfo.getOtp());
		 Transaction tx = null;
		 try {		    		   
       	  tx = session.beginTransaction();
           session.save(userOtpInfo);
           tx.commit();
           result = true;
} catch (HibernateException ex) {	
	if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
      result = false;
	System.out.println("Exception found insertOtpInfo  : "+ex);
} finally {
	if(session!=null){session.flush();session.clear();session.close();session=null;}
}		   
return result;
	}
	@Override
	public UserMasterTblVo checkUserMobileInfo(String mobno, String userId,
			String otp, String otpfor) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and mobileNo=:MOBILE_NO  and userId=:USER_ID";
	    	}else{
	    		qry= " from UserMasterTblVo where statusFlag=:STATUS  and mobileNo=:MOBILE_NO ";
	    	}
	      Query qy = session.createQuery(qry);
	      if(otpfor.equalsIgnoreCase("1")){
	      qy.setInteger("STATUS", 0);
	      }else{
	    	  qy.setInteger("STATUS", 1);
	      }
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setString("MOBILE_NO", mobno);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkUserOtpValidate======" + ex);
			 log.logMessage("otpDaoService Exception checkUserOtpValidate : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userdata;
	}
	@Override
	public MvpFamilymbrTbl checkFamilyInfoOtp(String mobileno,String otpfor) {
		MvpFamilymbrTbl userfamily = new MvpFamilymbrTbl();
		String result="";
		Session session = HibernateUtil.getSession();
		 String qry="";  String qry2="";
	    try {
	    		qry= " from MvpFamilymbrTbl where status=:STATUS  and fmbrPhNo=:MOBILE_NO ";
	      Query qy = session.createQuery(qry);
	      if(otpfor.equalsIgnoreCase("1") || otpfor.equalsIgnoreCase("3")){
	    	  qy.setInteger("STATUS", 0);
	      }else{
	    	  qy.setInteger("STATUS",1);
	      }
	      qy.setString("MOBILE_NO", mobileno);
	      userfamily = (MvpFamilymbrTbl) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkFamilyInfo======" + ex);
			 log.logMessage("otpDaoService Exception checkFamilyInfo : "+ex, "error", signUpDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userfamily;
	}
	@Override
	public boolean checkSocietyKey(String societyKey,String userId) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		 String qry="";
	    try {
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS and   userId=:USER_ID ";	
	    	}else{
	    		qry= " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS ";
	    	}
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("SOCIETY_KEY", societyKey);
	      qy.setMaxResults(1);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    	  qy.setInteger("USER_ID", Integer.parseInt(userId));
		      }
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
	public boolean checkSocietyKeyWithoutRid(String societyKey) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		 String qry="";
	    try {
	    	qry= " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("SOCIETY_KEY", societyKey);
	      qy.setMaxResults(1);
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      if(userInfo!=null){
	    	  result=true;
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	 result=false;
	    	System.out.println(" checkSocietyKeyWithoutRid======" + ex);
			 log.logMessage("profileDaoServices Exception checkSocietyKeyWithoutRid : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public UserMasterTblVo checkSocietyKeyForList(String societyKey,
			String userId) {
		UserMasterTblVo userData = new UserMasterTblVo();
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		 String qry="";
		 System.out.println("checkSocietyKeyForList() ============dsf==============");
	    try {
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS and   userId=:USER_ID ";	
	    	}else{
	    		qry= " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS ";
	    	}
	    	
	    	System.out.println("checkSocietyKeyForList(): qry = "+qry);
	    	
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setString("SOCIETY_KEY", societyKey);
	      qy.setMaxResults(1);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    	  qy.setInteger("USER_ID", Integer.parseInt(userId));
		      }
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	 result=false;
	    	System.out.println(" checkSocietyKeyForList======" + ex);
			 log.logMessage("profileDaoServices Exception checkSocietyKeyForList : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfo;
	}

	@Override
	public boolean updateOtpUserStaFlg(String mobile) {
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
		    	qy = session.createQuery("update MvpUserOtpTbl set status=:STATUS  where status=:UPDATE_STATUS and mobileNo=:MOBILE ");	 
			     qy.setString("MOBILE", mobile);
			     qy.setInteger("STATUS", 2);
			     qy.setInteger("UPDATE_STATUS", 1);
		    	qy.executeUpdate();
		    	tx.commit();
		    	result = true;
		    } catch (HibernateException ex) {
		    	if (tx != null) {tx.rollback();}	
		    	result = false;
		    	System.out.println(" updateOtpUserStaFlg======" + ex);
				 log.logMessage("otpDaoService Exception updateOtpUserStaFlg : "+ex, "error", otpDaoService.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
				log = null; qy = null;
		    }
		    System.out.println("Step 3 :user block [End].");
		    return result;
	}

	@Override
	public MvpUserOtpTbl getOtpValidateInfo(String mobno, String userId, String otpfor,String otp) {
		MvpUserOtpTbl userOtpInfo = new MvpUserOtpTbl();
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		 tx = session.beginTransaction();
		 String qry="";
	    try {
	    	System.out.println("=====userId==="+userId);
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from MvpUserOtpTbl where status=:STATUS and mobileNo=:MOBILE_NO and otp=:OTP  and userId=:USER_ID";
	    	}else{
	    		qry= " from MvpUserOtpTbl where status=:STATUS and mobileNo=:MOBILE_NO and otp=:OTP ";
	    	}
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setString("OTP", otp);
	      qy.setString("MOBILE_NO", mobno);
	      qy.setMaxResults(1);
	      System.out.println("=====userId==="+userId);
	      userOtpInfo = (MvpUserOtpTbl) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getOtpValidateInfo======" + ex);
			 log.logMessage("otpDaoService Exception getOtpValidateInfo : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userOtpInfo;
	}

	@Override
	public boolean checkFamailyOtpValidate(String mobno, String userId,
			String otp, String otpfor) {
		MvpUserOtpTbl userOtpInfo = new MvpUserOtpTbl();
		Integer userid=0;
		boolean result=false;
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		 tx = session.beginTransaction();
		 String qry="";
	    try {
	    	System.out.println("=====userId==="+userId);
	    		
	    		qry="SELECT famil.USR_ID FROM `mvp_user_otp_tbl` ot left join mvp_familymbr_tbl famil on ot.usr_id=famil.usr_id and ot.status=1 where famil.FMBR_PH_NO='"+mobno+"' and ot.otp='"+otp+"'";
	      Query qy = session.createSQLQuery(qry).addScalar("USR_ID", Hibernate.INTEGER);
	     /* qy.setInteger("STATUS", 1);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setString("OTP", otp);
	      qy.setString("MOBILE_NO", mobno);
	      System.out.println("=====userId==="+userId);*/
	      userid =  (Integer) qy.uniqueResult();
	      System.out.println("======================================userid==================="+userid);
	      if(userid>0){
	    	 Query  qy1 = session.createQuery("update MvpUserOtpTbl set status=:STATUS  where  userId=:USER_ID ");	 
			     qy1.setInteger("USER_ID",userid);
			     qy1.setInteger("STATUS", 0);
			    // qy1.setInteger("OTP_COUNT", 1);
		    	qy1.executeUpdate();
		    	
		    	/* Query  qy2 = session.createQuery("update MvpFamilymbrTbl set mobileOtpFlg=:MOBILE_OTP_VERIFY"
		    	 		+ "  where  userId=:USER_ID ");	 
		    	 qy2.setInteger("USER_ID",userid);
			     qy2.setInteger("MOBILE_OTP_VERIFY", 1);
			     //qy2.setInteger("STATUS_FLAG", 1);
			     qy2.executeUpdate();
			     System.out.println("=====userId==="+userId);*/
		    	tx.commit();
		    	result=true;
		    	System.out.println("=====userId==="+userId);
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkUserOtpValidate======" + ex);
			 log.logMessage("otpDaoService Exception checkUserOtpValidate : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

	@Override
	public String getOtpValidateForFamily(String mobno, String userId,
			String otpfor,String otp) {
		MvpUserOtpTbl userOtpInfo = new MvpUserOtpTbl();
		String otpGenTime = null;
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		 tx = session.beginTransaction();
		 String qry="";
	    try {
	    	System.out.println("=====userId==="+userId);
	    	/*if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from MvpUserOtpTbl where status=:STATUS and userId.mobileNo=:MOBILE_NO  and userId=:USER_ID";
	    	}else{
	    		qry= " from MvpUserOtpTbl where status=:STATUS and userId.mobileNo=:MOBILE_NO ";
	    	}*/
	    	//qry="SELECT 'OTP_GENERATED_TIME' FROM `mvp_user_otp_tbl` ot left join mvp_familymbr_tbl famil on ot.usr_id=famil.usr_id and ot.status=1 where famil.FMBR_PH_NO='"+mobno+"'";
	    	qry="SELECT * FROM `mvp_user_otp_tbl` ot left join mvp_familymbr_tbl famil on ot.usr_id=famil.usr_id and ot.status=1 where famil.FMBR_PH_NO='"+mobno+"' and ot.otp='"+otp+"'";
	    	Query qy = session.createSQLQuery(qry).addScalar("OTP_GENERATED_TIME", Hibernate.TEXT);
	     /* qy.setInteger("STATUS", 1);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	     // qy.setString("OTP", otp);
	      qy.setString("MOBILE_NO", mobno);
	      System.out.println("=====userId==="+userId);*/
	      otpGenTime =  (String) qy.uniqueResult();
	      System.out.println("==========otpGenTime================="+otpGenTime);
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getOtpValidateInfo======" + ex);
			 log.logMessage("otpDaoService Exception getOtpValidateInfo : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return otpGenTime;
	}

	@Override
	public List<UserMasterTblVo> getParentDetails(String parentid,
			String userId, int committeeid, int residentid,String mob) {
		List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where statusFlag=:ACT_STATUS and mobileNo=:MOBILE and  parentId=:Parent_ID and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)  and userId=:USER_ID ";
	    	}else{
	    		qry= " from UserMasterTblVo where statusFlag=:ACT_STATUS and mobileNo=:MOBILE and  parentId=:Parent_ID and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)";
	    	}
	      Query qy = session.createQuery(qry);
	     // qy.setInteger("STATUS", 1);
	      qy.setInteger("ACT_STATUS", 0);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setInteger("Parent_ID", Integer.parseInt(parentid));
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeid);
	      qy.setInteger("RESIDENT_GRP_CODE", residentid);
	      qy.setString("MOBILE", mob);
	      qy.setMaxResults(1);
	      userInfoList =  qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getUserDeatils======" + ex);
			 log.logMessage("otpDaoService Exception getUserDeatils : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfoList;
	}

	@Override
	public UserMasterTblVo getParentForRidDetails(String parentid,
			String userId, int committeeid, int residentid) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    	if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	    		qry= " from UserMasterTblVo where  userId=:Parent_ID and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)  and userId=:USER_ID ";
	    	}else{
	    		qry= " from UserMasterTblVo where userId=:Parent_ID and  (groupCode=:COMMITTEE_GRP_CODE or groupCode=:RESIDENT_GRP_CODE)";
	    	}
	      Query qy = session.createQuery(qry);
	     // qy.setInteger("STATUS", 1);
	     // qy.setInteger("ACT_STATUS",1);
	      if(userId!=null && userId!="" && !userId.equalsIgnoreCase("")){
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      }
	      qy.setInteger("Parent_ID", Integer.parseInt(parentid));
	      qy.setInteger("COMMITTEE_GRP_CODE", committeeid);
	      qy.setInteger("RESIDENT_GRP_CODE", residentid);
	     // qy.setString("OTP", otp);
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getParentForRidDetails======" + ex);
			 log.logMessage("otpDaoService Exception getParentForRidDetails : "+ex, "error", otpDaoService.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfo;
	}

	@Override
	public UserMasterTblVo checkDupicateMobile(String societykey, String mobile) {
		UserMasterTblVo userInfo=new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		System.out.println("===========mobile======="+mobile);
		System.out.println("=========fdg========="+societykey);
		 String qry="";
	    try {
	    	qry= " from UserMasterTblVo where  mobileNo=:MOBILE_NO and societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS ";
	      Query qy = session.createQuery(qry);
	      //qy.setInteger("STATUS", 1);
	      qy.setString("SOCIETY_KEY", societykey);
	      qy.setString("MOBILE_NO", mobile);
	      qy.setInteger("STATUS", 1);
	      qy.setMaxResults(1);
	      userInfo =  (UserMasterTblVo) qy.uniqueResult();
	      System.out.println("===========userInfo=========="+userInfo);
	      
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" checkDupicateMobile======" + ex);
			 log.logMessage("profileDaoServices Exception checkDupicateMobile : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfo;
	}

	@Override
	public UserMasterTblVo checkUserDetails(String userId) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		String qry="";
		int usrId = 0;
	    try {
	      System.out.println("=====dfsdfds login id===="+userId);
	      if (Commonutility.checkempty(userId)) {
	    	usrId = Integer.parseInt(userId);
	      }
	      qry= " from UserMasterTblVo where statusFlag=:STATUS and userId=:USER_ID";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("STATUS", 1);
	      qy.setInteger("USER_ID", usrId);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      System.out.println(" checkUserDetails======" + ex);
		  log.logMessage("otpDaoService Exception checkUserDetails : "+ex, "error", otpDaoService.class);
	    } finally {
	      if(session!=null){
	    	session.flush();session.clear(); session.close(); session = null;
		  }
	    }
	    return userdata;
	}

	@Override
	public boolean checkTownshipKeyWithEmailOrMobno(String townshipKey,
			String emailMobno) {
		// TODO Auto-generated method stub
		boolean result = true;
	/*	UserMasterTblVo townShipData = new UserMasterTblVo();
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();	
			String qry= " from UserMasterTblVo where statusFlag=:STATUS and (mobileNo=:MOBILE_NO or emailId=:EMAIL_ID) and societyId.townshipId.activationKey=:TOWNSHIP_KEY";
			qy = session.createQuery(qry);
			qy.setString("MOBILE_NO",emailMobno);
			qy.setString("EMAIL_ID",emailMobno);
			qy.setString("TOWNSHIP_KEY",townshipKey);	
			qy.setInteger("STATUS", 1);
			townShipData = (UserMasterTblVo) qy.uniqueResult();
			if(townShipData!=null){
				result=true;
			}else{
				result=false;
			}
		} catch (HibernateException ex) {
			System.out.println(" getOtpTblInfo======" + ex);
			 log.logMessage("otpDaoService Exception checkTownshipKey : "+ex, "error", otpDaoService.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}*/
		return result;
	}


}
