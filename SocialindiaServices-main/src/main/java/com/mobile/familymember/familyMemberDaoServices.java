package com.mobile.familymember;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobile.profile.MvpUsrSkillTbl;
import com.mobile.profile.profileDaoServices;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class familyMemberDaoServices implements familyMemberDao {
	Log log = new Log();

	@Override
	public List<MvpFamilymbrTbl> getFamilyMembersList(String userId,
			String timestamp, String startlim, String totalrow,int societyId,String andQueryCondition) {
		List<MvpFamilymbrTbl> familyMemberList = new ArrayList<MvpFamilymbrTbl>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpFamilymbrTbl where status=:STATUS and userId.userId=:USER_ID  and entryDatetime <='"+timestamp+"' "+andQueryCondition;
			Query qy = session.createQuery(qry);
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("STATUS", 1);
			//qy.setInteger("SOCIETY_ID", societyId);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//Date startDate = df.parse(timestamp);
			//qy.setDate("ENTRY_DATE", startDate);
			familyMemberList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFamilyMembersList======" + ex);
			log.logMessage(
					"familyMemberDaoServices Exception getFamilyMembersList : "
							+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return familyMemberList;
	}

	@Override
	public boolean deleteFamilyMember(String userId, String familyid) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		UserMasterTblVo userdata = new UserMasterTblVo();
		Transaction tx = null;
		 String qry="";
		try {
			tx = session.beginTransaction();
			qry= " from UserMasterTblVo where parentId=:USER_ID and  fmbrTntId=:FAMILY_ID ";	
		      Query qy1 = session.createQuery(qry);
		      qy1.setInteger("USER_ID", Integer.parseInt(userId));
		      qy1.setInteger("FAMILY_ID", Integer.parseInt(familyid));
		      userdata = (UserMasterTblVo) qy1.uniqueResult();
		      if(userdata!=null){
		    	  Query qy2 = session
							.createQuery("update UserMasterTblVo set  statusFlag=:STATUS "
									+ " where parentId=:PARENT_ID and  fmbrTntId=:FAMILY_ID");
					
					qy2.setInteger("PARENT_ID", Integer.parseInt(userId));
					qy2.setInteger("FAMILY_ID", Integer.parseInt(familyid));
					qy2.setInteger("STATUS", 3); 
					qy2.executeUpdate();
					Query qy = session
							.createQuery("update MvpFamilymbrTbl set status=:STATUS "
									+ "  where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
					qy.setInteger("STATUS", 0);
					qy.setInteger("USER_ID", Integer.parseInt(userId));
					qy.setInteger("FAMILY_ID", Integer.parseInt(familyid));
					qy.executeUpdate();
		      }else{
		    	  Query qy3 = session
							.createQuery("delete from  MvpFamilymbrTbl "
									+ "  where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
					qy3.setInteger("USER_ID", Integer.parseInt(userId));
					qy3.setInteger("FAMILY_ID", Integer.parseInt(familyid));
					qy3.executeUpdate();
		      }
			
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("profileDaoServices Exception getUserPrfileList : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public Integer getFamilyMembersAddDetails(MvpFamilymbrTbl familtMemberMst) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		int famId=0;
		try {
			tx = session.beginTransaction();
			session.save(familtMemberMst);
			famId=familtMemberMst.getFmbrTntId();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("familyMemberDaoServices Exception getUserPrfileList : "
					+ ex, "error", familyMemberDaoServices.class);
			System.out.println("Exception found getFamilyMembersAddDetails  : " + ex);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return famId;
	}

	@Override
	public MvpFamilymbrTbl getFamilyMembersEditDetails(String userId,
			String familyid) {
		MvpFamilymbrTbl familyMemberMst = new MvpFamilymbrTbl();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpFamilymbrTbl where status=:STATUS and userId.userId=:USER_ID and fmbrTntId =:FAMILY_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("FAMILY_ID", Integer.parseInt(familyid));
			qy.setInteger("STATUS", 1);
			familyMemberMst = (MvpFamilymbrTbl) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFamilyMembersEditDetails======" + ex);
			log.logMessage("familyMemberDaoServices Exception getFamilyMembersEditDetails : "
							+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return familyMemberMst;
	}

	@Override
	public boolean updateFamilyMemberDetails(
			MvpFamilymbrTbl familtMemberMst,String relationId,String titlename,String bloodid,String age,String is_invited) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Query qy=null;
		Query qy1=null;
		try {
			tx = session.beginTransaction();
			System.out.println("is_invited---------"+is_invited);
			if(is_invited!=null && is_invited.equalsIgnoreCase("1")){
			 qy = session
					.createQuery("update MvpFamilymbrTbl set  titleId=:NAME_TITLE, fmbrName=:FAMILY_NAME,"
							+ "fmbrGender=:GENDER,fmbrAge=:AGE,bloodGroupId=:BLOOD_GRP,iVOKNOWN_US_ID=:RELATION_ID"
							+ " where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			
			}else {
				is_invited="0";
				 qy = session
							.createQuery("update MvpFamilymbrTbl set  titleId=:NAME_TITLE, fmbrName=:FAMILY_NAME, fmbrPhNo='"+familtMemberMst.getFmbrPhNo()+"', "
									+ " fmbrGender=:GENDER,fmbrAge=:AGE,bloodGroupId=:BLOOD_GRP,iVOKNOWN_US_ID=:RELATION_ID"
									+ " where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			}
			 qy1 = session
						.createQuery("update UserMasterTblVo set  titleId=:NAME_TITLE, firstName=:FAMILY_NAME,"
								+ "gender=:GENDER,bloodGroupId=:BLOOD_GRP,iVOKNOWN_US_ID=:RELATION_ID"
								+ " where parentId=:USER_ID and  fmbrTntId=:FAMILY_ID and statusFlag<>:STATUS ");
			 
			 /*if(familtMemberMst.getFmbrPhNo()!=null && familtMemberMst.getFmbrPhNo()!="" && is_invited.equalsIgnoreCase("0")){
					qy.setString("PHONE_NO", familtMemberMst.getFmbrName());
					}else{
						qy.setString("PHONE_NO",null);	
					}*/
			 System.out.println("qy---------------"+qy);
			if(titlename!=null && !titlename.equalsIgnoreCase("")){
				System.out.println("titlename------------"+titlename);
			qy.setInteger("NAME_TITLE", Integer.parseInt(titlename));
			}else{
				qy.setString("NAME_TITLE", null);
			}
			
			if(familtMemberMst.getFmbrName()!=null && !familtMemberMst.getFmbrName().equalsIgnoreCase("")){
			qy.setString("FAMILY_NAME", familtMemberMst.getFmbrName());
			}else{
				qy.setString("FAMILY_NAME",null);	
			}
			System.out.println("familtMemberMst------------"+familtMemberMst);
			if(familtMemberMst.getFmbrGender()!=null && !familtMemberMst.getFmbrGender().equalsIgnoreCase("")){
			qy.setString("GENDER", familtMemberMst.getFmbrGender());
			}else{
				qy.setString("GENDER", null);	
			}if(age!=null && !age.equalsIgnoreCase("")){
				System.out.println("age-------"+familtMemberMst.getFmbrAge());
			qy.setInteger("AGE", familtMemberMst.getFmbrAge());
			}else{
				qy.setString("AGE", null);
			}
			if(bloodid!=null && !bloodid.equalsIgnoreCase("")){
			qy.setInteger("BLOOD_GRP", Integer.parseInt(bloodid));
			}else{
				qy.setString("BLOOD_GRP", null);	
			}
			if(relationId!=null && !relationId.equalsIgnoreCase("")){
			qy.setInteger("RELATION_ID", Integer.parseInt(relationId));
			}else{
				qy.setString("RELATION_ID", null);
			}
			qy.setInteger("USER_ID", familtMemberMst.getUserId().getUserId());
			System.out.println("familtMemberMst.getFmbrTntId()----------------"+familtMemberMst.getFmbrTntId());
			qy.setInteger("FAMILY_ID", familtMemberMst.getFmbrTntId());
			//qy.setDate("MODY_DATETIME", date1);
			
			
			//qy1.setInteger("STATUS", 1);
			if(titlename!=null && !titlename.equalsIgnoreCase("")){
			qy1.setInteger("NAME_TITLE", Integer.parseInt(titlename));
			}else{
				qy1.setString("NAME_TITLE", null);
			}
			if(familtMemberMst.getFmbrName()!=null && !familtMemberMst.getFmbrName().equalsIgnoreCase("")){
			qy1.setString("FAMILY_NAME", familtMemberMst.getFmbrName());
			}else{
				qy1.setString("FAMILY_NAME",null);	
			}if(familtMemberMst.getFmbrGender()!=null && !familtMemberMst.getFmbrGender().equalsIgnoreCase("")){
			qy1.setString("GENDER", familtMemberMst.getFmbrGender());
			}else{
				qy1.setString("GENDER", null);	
			}
			if(bloodid!=null && !bloodid.equalsIgnoreCase("")){
			qy1.setInteger("BLOOD_GRP", Integer.parseInt(bloodid));
			}else{
				qy1.setString("BLOOD_GRP", null);	
			}
			if(relationId!=null && !relationId.equalsIgnoreCase("")){
			qy1.setInteger("RELATION_ID", Integer.parseInt(relationId));
			}else{
				qy1.setString("RELATION_ID", null);
			}
			qy1.setInteger("USER_ID", familtMemberMst.getUserId().getUserId());
			qy1.setInteger("FAMILY_ID", familtMemberMst.getFmbrTntId());
			qy1.setInteger("STATUS",3);
			
			System.out.println("qy-----------"+qy);
			qy.executeUpdate();
			qy1.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("ex----------------------"+ex);
			ex.printStackTrace();
			result = false;
			log.logMessage("profileDaoServices Exception getUserPrfileList : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public boolean updateFamilyRelationInfo(String userId, String familyid,
			String relationId) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update MvpFamilymbrTbl set iVOKNOWN_US_ID=:RELATION_ID "
							+ " where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			
			Query qy1 = session
					.createQuery("update UserMasterTblVo set iVOKNOWN_US_ID=:RELATION_ID "
							+ " where userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("FAMILY_ID", Integer.parseInt(familyid));
			qy.setInteger("RELATION_ID", Integer.parseInt(relationId));
			qy1.setInteger("USER_ID", Integer.parseInt(userId));
			qy1.setInteger("FAMILY_ID", Integer.parseInt(familyid));
			qy1.setInteger("RELATION_ID", Integer.parseInt(relationId));
			qy.executeUpdate();
			qy1.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("profileDaoServices Exception updateFamilyRelationInfo : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public boolean familyMemberLoginInvite(UserMasterTblVo userMst,String userId, String familyid,String invite_revoke) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			session.save(userMst);
			System.out.println("==dsf=df=d==");
			Query qy = session
					.createQuery("update MvpFamilymbrTbl set isInvited=:IS_INVITE "
							+ " where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("FAMILY_ID", Integer.parseInt(familyid));
			//qy.setInteger("STATUS", 2);
			qy.setInteger("IS_INVITE", Integer.parseInt(invite_revoke));
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("profileDaoServices Exception familyMemberLoginInvite : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public MvpFamilymbrTbl getFamilyInfoForUserTable(String userId, String familyid) {
		MvpFamilymbrTbl familyMember = new MvpFamilymbrTbl();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpFamilymbrTbl where userId.userId=:USER_ID and fmbrTntId=:FAMILY_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("FAMILY_ID", Integer.parseInt(familyid));
			//qy.setInteger("STATUS", 1);
			familyMember = (MvpFamilymbrTbl) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFamilyInfoForUserTable======" + ex);
			log.logMessage(
					"familyMemberDaoServices Exception getFamilyInfoForUserTable : "
							+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return familyMember;
	}

	@Override
	public boolean familyMemberLoginRevoke(String userId, String familyid,
			String invite_revoke) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			System.out.println("==dsf=df=d==");
			Query qy = session
					.createQuery("update MvpFamilymbrTbl set isInvited=:IS_INVITE "
							+ " where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("FAMILY_ID", Integer.parseInt(familyid));
			//qy.setInteger("STATUS", 2);
			qy.setInteger("IS_INVITE", Integer.parseInt(invite_revoke));
			
			Query qy1 = session
					.createQuery("update UserMasterTblVo set  statusFlag=:STATUS "
							+ " where parentId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			
			qy1.setInteger("USER_ID", Integer.parseInt(userId));
			qy1.setInteger("FAMILY_ID", Integer.parseInt(familyid));
			qy1.setInteger("STATUS", Integer.parseInt(invite_revoke));
			qy.executeUpdate();
			qy1.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("profileDaoServices Exception familyMemberLoginRevoke : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public boolean checkFamilyInfo(String userId, String familyid) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		MvpFamilymbrTbl familyMst = new MvpFamilymbrTbl();
		Transaction tx = null;
		 String qry="";
		try {
			tx = session.beginTransaction();
			qry= " from MvpFamilymbrTbl where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID ";	
		      Query qy1 = session.createQuery(qry);
		      qy1.setInteger("USER_ID", Integer.parseInt(userId));
		      qy1.setInteger("FAMILY_ID", Integer.parseInt(familyid));
		      familyMst = (MvpFamilymbrTbl) qy1.uniqueResult();
		      if(familyMst!=null){
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
			log.logMessage("profileDaoServices Exception checkFamilyInfo : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public MvpFamilymbrTbl checkFamilyMobileExist(String userId, String mobile) {
		Session session = HibernateUtil.getSession();
		MvpFamilymbrTbl familyMst = new MvpFamilymbrTbl();
		 String qry="";
		try {
			qry= " from MvpFamilymbrTbl where userId.userId=:USER_ID and  fmbrPhNo=:MOBILE and isInvited=:IS_INVITED";	
		      Query qy1 = session.createQuery(qry);
		      qy1.setInteger("USER_ID", Integer.parseInt(userId));
		      qy1.setString("MOBILE", mobile);
		      qy1.setInteger("IS_INVITED", 1);
		      qy1.setMaxResults(1);
		      familyMst = (MvpFamilymbrTbl) qy1.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			log.logMessage("profileDaoServices Exception checkFamilyMobileExist : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			session.close();
		}
		return familyMst;
	}

	@Override
	public boolean updateFamilyInfo(String userId, String mobile, int familyId) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			System.out.println("==dsf=df=d==");
			Query qy = session
					.createQuery("update MvpFamilymbrTbl set status=:STATUS "
							+ " where userId.userId=:USER_ID and  fmbrTntId=:FAMILY_ID");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("FAMILY_ID", familyId);
			qy.setInteger("STATUS", 1);
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("profileDaoServices Exception updateFamilyInfo : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

}
