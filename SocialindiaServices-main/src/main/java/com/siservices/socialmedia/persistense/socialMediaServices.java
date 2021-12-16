package com.siservices.socialmedia.persistense;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.siservices.signup.persistense.ActivationKeyVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.RightsMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class socialMediaServices implements socialMediaDao {

	Log log = new Log();

	@Override
	public boolean checktwId(String twitterId) {
		UserMasterTblVo userDetails = new UserMasterTblVo();
		boolean result = false;
		System.out.println("=============================");
		int count = 0;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			String qry = " SELECT count(socialId)  FROM MvpSocialProTbl where socialId=:TWITTER_ID"
					+ " and actSts=:STATUS_FLAG";
			;
			System.out.println("unam---" + twitterId);
			System.out.println("Qry---" + qry);

			Query query = session.createQuery(qry);
			query.setString("TWITTER_ID", twitterId);
			query.setInteger("STATUS_FLAG", 1);
			System.out.println("=================ds=dd=============");
			count = ((Number) query.uniqueResult()).intValue();
			System.out.println("===============");
			System.out.println("userDetails======" + count);
			if (count > 0) {
				result = true;
			} else {
				result = false;
			}

		} catch (Exception ex) {
			System.out.println("=========" + ex);

		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return result;
	}

	@Override
	public boolean saveTwitterInfo(UserMasterTblVo userData, String picurl,
			Integer socialtype) {
		boolean result = false;
		MvpSocialProTbl socialpro = new MvpSocialProTbl();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			System.out.println("=======fddf=sf=====");
			tx = session.beginTransaction();
			session.save(userData);
			if (userData != null) {
				socialpro.setUserId(userData);
				socialpro.setPictureUrl(picurl);
				socialpro.setActSts(1);
				socialpro.setSocialId(userData.getUserName());
				socialpro.setEmailId(userData.getEmailId());
				socialpro.setEnrtyDatetime(userData.getEntryDatetime());
				socialpro.setSocialNw(String.valueOf(socialtype));
				session.save(socialpro);
				tx.commit();
				result = true;
			}

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			result = false;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return result;
	}

	@Override
	public List<RightsMasterTblVo> getUserRightstwitter(int groupcode) {
		List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
		Session session = HibernateUtil.getSession();

		try {
			String qry = "from RightsMasterTblVo where groupCode=:GROUP_CODE"
					+ " order by menuId.rootDesc ASC";
			Query query = session.createQuery(qry);
			query.setInteger("GROUP_CODE", groupcode);

			// q.setInteger("GROUP_CODE", userInfo.getGroupId().getGroupCode());
			rightsList = query.list();
			System.out.println("rightsList=========" + rightsList.size());
		} catch (Exception ex) {
			System.out.println("exception occur=============" + ex);
			ex.printStackTrace();
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return rightsList;
	}

	public boolean checkfacebookId(String facebookId) {
		boolean result = false;
		System.out.println("=============================");
		int count = 0;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			String qry = " SELECT count(socialId)  FROM MvpSocialProTbl where socialId=:FACEBOOK_ID"
					+ " and actSts=:STATUS_FLAG";
			System.out.println("unam---" + facebookId);
			System.out.println("Qry---" + qry);

			Query query = session.createQuery(qry);
			query.setString("FACEBOOK_ID", facebookId);
			query.setInteger("STATUS_FLAG", 1);
			System.out.println("=================ds=dd=============");
			count = ((Number) query.uniqueResult()).intValue();
			System.out.println("===============");
			System.out.println("userDetails======" + count);
			if (count > 0) {
				result = true;
			} else {
				result = false;
			}

		} catch (Exception ex) {
			System.out.println("=========" + ex);

		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return result;
	}

	@Override
	public String savefacebookInfo(UserMasterTblVo userData, String picurl,
			Integer socialtype,String societyKey,String gender) {
		String result = "";
		SocietyMstTbl societyMaster = new SocietyMstTbl();
		MvpSocialProTbl socialpro =new MvpSocialProTbl();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			System.out.println("=====fb update===picurl==" + picurl);
			tx = session.beginTransaction();
			int count = 0;
			int mobCount = 0;
			try {
				System.out.println("=======");
		    	System.out.println("========society active key="+societyKey);
		      String qry = "From SocietyMstTbl  where activationKey=:ACTIVATION_KEY";
		      Query qy = session.createQuery(qry);
		      qy.setString("ACTIVATION_KEY", societyKey);
		     // qy.setInteger("STATUS_FLAG", 1);
		      societyMaster = (SocietyMstTbl) qy.uniqueResult();
		     
		    } catch (HibernateException ex) {
		      ex.printStackTrace();
		      //result = false;
		      System.out.println("=====socialMaster============"+ex);
		    }
			
			String qry = " SELECT count(emailId)  FROM UserMasterTblVo where emailId=:EMAIL_Id"
					+ " and statusFlag=:STATUS_FLAG";
			System.out.println("unam---" + userData.getEmailId());
			System.out.println("Qry---" + qry);

			Query query = session.createQuery(qry);
			query.setString("EMAIL_Id", userData.getEmailId());
			query.setInteger("STATUS_FLAG", 1);
			System.out.println("=================ds=dd=============");
			count = ((Number) query.uniqueResult()).intValue();
			System.out.println("===============");
			System.out.println("userDetails======" + count);
			if (count > 0) {
				result = "EmailExist";
			}
			String qry1 = " SELECT count(mobileNo)  FROM UserMasterTblVo where mobileNo=:MOBILE_NO"
					+ " and statusFlag=:STATUS_FLAG";
			;
			System.out.println("unam---" + userData.getMobileNo());
			System.out.println("Qry---" + qry1);

			Query query1 = session.createQuery(qry1);
			query1.setString("MOBILE_NO", userData.getMobileNo());
			query1.setInteger("STATUS_FLAG", 1);
			System.out.println("=================ds=dd=============");
			mobCount = ((Number) query1.uniqueResult()).intValue();
			System.out.println("==========mobCount=====" + mobCount);
			if (mobCount > 0) {
				result = "mobnoExist";
			}
			if((societyMaster!=null)&&((count > 0) || (mobCount > 0))){
				System.out.println("==============already exist=========================");
				result = "alreadyExist";
			}else{
			if ((societyMaster!=null)) {
				try {
					userData.setSocietyId(societyMaster);
					session.save(userData);
					if (userData != null) {
						socialpro.setUserId(userData);
						socialpro.setPictureUrl(picurl);
						socialpro.setGender(gender);
						socialpro.setFirstName(userData.getFirstName());
						socialpro.setLastName(userData.getLastName());
						socialpro.setAddress(userData.getAddress1());
						socialpro.setActSts(1);
						socialpro.setSocialId(userData.getUserName());
						socialpro.setEmailId(userData.getEmailId());
						socialpro.setEnrtyDatetime(userData.getEntryDatetime());
						socialpro.setSocialNw(String.valueOf(socialtype));
						session.save(socialpro);
						tx.commit();
						result = "success";
					}
				} catch (HibernateException ex) {
					if (tx != null) {
						tx.rollback();
					}
					ex.printStackTrace();
					result = "error";
				}
				}else{
					result = "activekeyerror";
				}
			}
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			result = "error";
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return result;
	}
}
