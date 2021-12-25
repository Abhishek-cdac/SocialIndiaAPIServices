package com.mobi.merchant;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.feedbackvo.FeedbackTblVO;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantCategoryTblVO;
import com.socialindiaservices.vo.MerchantIssuePostingTblVO;
import com.socialindiaservices.vo.MerchantProductItemsTblVO;
import com.socialindiaservices.vo.MerchantProductOrderTblVO;
import com.socialindiaservices.vo.MerchantTblVO;
import com.socialindiaservices.vo.MerchantrRatingTblVO;

public class MerchantDaoServices implements MerchantDao{
	Log log = new Log();
	
	@Override
	public List getMerchantCategoryList(String qry,String startlim, String totalrow) {
		// TODO Auto-generated method stub
		List<MerchantCategoryTblVO> merchantCategoryList=new ArrayList<MerchantCategoryTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			merchantCategoryList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getMerchantCategoryList======" + ex);
			log.logMessage("MerchantDaoServices Exception getMerchantCategoryList : "
							+ ex, "error", MerchantDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return merchantCategoryList;
	}

	@Override
	public List<Object[]> getMobMerchantSearchList(String qry, String startlim) {
		// TODO Auto-generated method stub
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getMobMerchantSearchList " , "info", MerchantDaoServices.class);			
			
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(qry)
					.addScalar("MRCHNT_ID", Hibernate.INTEGER)
					.addScalar("SHOP_NAME", Hibernate.TEXT)
					.addScalar("MERCH_DESCRIPTION", Hibernate.TEXT)
					.addScalar("STORE_IMAGE", Hibernate.TEXT)
					.addScalar("OFFER_IMAGES", Hibernate.TEXT)
					.addScalar("STORE_LOCATION", Hibernate.TEXT)
					.addScalar("RATING", Hibernate.INTEGER)
					.addScalar("MRCHNT_PH_NO", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into getMobMerchantSearchList size :" + resultListObj.size(), "info", MerchantDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in getMobMerchantSearchList :" + ex, "error", MerchantDaoServices.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public List<Object[]> getMobMerchantIssueSearchList(String qry) {
		// TODO Auto-generated method stub
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getMobMerchantIssueSearchList " , "info", MerchantDaoServices.class);			
			
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(qry)
					.addScalar("MRCHNT_ID", Hibernate.INTEGER)
					.addScalar("SHOP_NAME", Hibernate.TEXT)
					.addScalar("MERCH_DESCRIPTION", Hibernate.TEXT)
					.addScalar("STORE_IMAGE", Hibernate.TEXT)
					.addScalar("ISSUE_ID", Hibernate.INTEGER)
					.addScalar("DESCRIPTION", Hibernate.TEXT)
					.addScalar("RATING", Hibernate.INTEGER)
					.addScalar("STORE_LOCATION", Hibernate.TEXT);
			//queryObj.setFirstResult(Integer.parseInt(startlim));
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into getMobMerchantIssueSearchList size :" + resultListObj.size(), "info", MerchantDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in getMobMerchantIssueSearchList :" + ex, "error", MerchantDaoServices.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public MerchantTblVO getMerchantobject(int merchantId) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		MerchantTblVO merchantobj=new MerchantTblVO();
		try {
			String qry="from MerchantTblVO where mrchntId=:MERCHANT_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("MERCHANT_ID", merchantId);
			qy.setMaxResults(1);
			merchantobj = (MerchantTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getMerchantCategoryList======" + ex);
			log.logMessage("MerchantDaoServices Exception getMerchantCategoryList : "
							+ ex, "error", MerchantDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return merchantobj;
	}

	@Override
	public boolean saveFeedbackTblObj(FeedbackTblVO feedbackobj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isinsert=false;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(feedbackobj);
			tx.commit();	
			isinsert=true;
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			isinsert=false;
			System.out.println("Step -1 : Exception found MerchantDaoServices.saveFeedbackTblObj() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveFeedbackTblObj() : "+ex, "error", MerchantDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return isinsert;
	}

	@Override
	public boolean saveMerchantIssueTblObj(
			MerchantIssuePostingTblVO mrchIssueobj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isinsert=false;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(mrchIssueobj);
			tx.commit();	
			isinsert=true;
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			isinsert=false;
			System.out.println("Step -1 : Exception found MerchantDaoServices.saveMerchantIssueTblObj() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveMerchantIssueTblObj() : "+ex, "error", MerchantDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return isinsert;
	}

	@Override
	public boolean saveorderTblObj(MerchantProductOrderTblVO orderobj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isinsert=false;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(orderobj);
			tx.commit();	
			isinsert=true;
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			isinsert=false;
			System.out.println("Step -1 : Exception found MerchantDaoServices.saveorderTblObj() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveorderTblObj() : "+ex, "error", MerchantDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return isinsert;
	}


	@Override
	public boolean saveitemTblObj(MerchantProductItemsTblVO itemobj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isinsert=false;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(itemobj);
			tx.commit();	
			isinsert=true;
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			isinsert=false;
			System.out.println("Step -1 : Exception found MerchantDaoServices.saveorderTblObj() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveorderTblObj() : "+ex, "error", MerchantDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return isinsert;
	}

	@Override
	public List getMerchantTableList(String qry, String startlim,
			String totalrow) {
		// TODO Auto-generated method stub
		List<MerchantTblVO> merchantTblList=new ArrayList<MerchantTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			merchantTblList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getMerchantTableList======" + ex);
			log.logMessage("MerchantDaoServices Exception getMerchantTableList : "
							+ ex, "error", MerchantDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return merchantTblList;
	}

	@Override
	public List getAllMerchantCategoryList(String qry, String totalrow) {
		// TODO Auto-generated method stub
		List<MerchantCategoryTblVO> merchantCategoryList=new ArrayList<MerchantCategoryTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setMaxResults(Integer.parseInt(totalrow));
			merchantCategoryList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getMerchantCategoryList======" + ex);
			log.logMessage("MerchantDaoServices Exception getMerchantCategoryList : "
							+ ex, "error", MerchantDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return merchantCategoryList;
	}

	@Override
	public boolean savemerchIssueTblObj(MerchantIssuePostingTblVO issueobj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isinsert=false;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(issueobj);
			tx.commit();	
			isinsert=true;
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			isinsert=false;
			System.out.println("Step -1 : Exception found MerchantDaoServices.savemerchIssueTblObj() : "+ex);
			logwrite.logMessage("Step -1 : Exception found savemerchIssueTblObj() : "+ex, "error", MerchantDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return isinsert;
	}

	@Override
	public boolean savemerchRatingTblObj(MerchantrRatingTblVO ratingobj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isinsert=false;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(ratingobj);
			String sql="select avg(mrchRating) from MerchantrRatingTblVO where mrchntId.mrchntId=:MERCHANT_ID";
			Query qy = session.createQuery(sql);
			qy.setInteger("MERCHANT_ID", ratingobj.getMrchntId().getMrchntId());
			double rating=(Double) qy.uniqueResult();
			float rat=(float) rating;
			qy = null;
			sql="update MerchantTblVO set rating=:RATING where mrchntId=:MERCHANT_ID";
			qy = session.createQuery(sql);
			qy.setFloat("RATING", rat);
			qy.setInteger("MERCHANT_ID", ratingobj.getMrchntId().getMrchntId());
			int exec=qy.executeUpdate();
			if(exec>0){
			tx.commit();
			isinsert=true;
			}else{
				isinsert=false;
				tx.rollback();
			}
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			isinsert=false;
			System.out.println("Step -1 : Exception found MerchantDaoServices.savemerchIssueTblObj() : "+ex);
			logwrite.logMessage("Step -1 : Exception found savemerchIssueTblObj() : "+ex, "error", MerchantDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return isinsert;
	}

	@Override
	public boolean executecommonProcedure(String sql) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isinsert=false;
		logwrite = new Log();
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			PreparedStatement st = session.connection().prepareStatement("{call PROC_MRCH_LIB_RATING()}");
            st.execute();
			tx.commit();
			isinsert=true;
		}catch (Exception ex){
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Step -1 : Exception found MerchantDaoServices.executecommonProcedure() : "+ex);
			logwrite.logMessage("Step -1 : Exception found executecommonProcedure() : "+ex, "error", MerchantDaoServices.class);
		}finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		return isinsert;
	}

}
