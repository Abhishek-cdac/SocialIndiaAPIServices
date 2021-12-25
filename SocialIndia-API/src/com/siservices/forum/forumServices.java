package com.siservices.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.forumVo.MvpFourmDiscussTbl;
import com.siservices.forumVo.MvpFourmTopicsTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class forumServices implements forumDao{
	Log log=new Log();
	@Override
	public String forumCreationForm(MvpFourmTopicsTbl forumTopicMst) {
		Session session = null;
		Transaction tx = null;
		String result="";
		session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();
		try {
						
			session.save(forumTopicMst);
			result="success";
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in forumServices : "+e);
			log.logMessage("forumServices Exception forumCreationForm : "+e, "error", forumServices.class);
			result="error";
			
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}
		}	
		
		return result;
	}
	@Override
	public boolean forumDelete(int topicsId) {
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update MvpFourmTopicsTbl set status=:STATUS_FLAG,  "
		              + " modifyDatetime=:MODY_DATETIME where topicsId=:TOPICS_ID");
		      qy.setInteger("STATUS_FLAG", 0);
		      qy.setInteger("TOPICS_ID", topicsId);
		      qy.setTimestamp("MODY_DATETIME", date1);
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("forumServices Exception forumDelete : "+ex, "error", forumServices.class);
		    } finally {
		    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		    }
		    return result;
	}
	@Override
	public List<MvpFourmDiscussTbl> forumViewAction(int topicsId) {
				List<MvpFourmDiscussTbl> forumDiscussList = new ArrayList<MvpFourmDiscussTbl>();
				Session session = HibernateUtil.getSession();
				try {
					String qry = "From MvpFourmDiscussTbl where topicsId=:TOPICS_ID and status=:STATUS ORDER BY entryDatetime DESC";
					Query qy = session.createQuery(qry);
					qy.setInteger("TOPICS_ID", topicsId);
					qy.setInteger("STATUS", 1);					
					forumDiscussList = qy.list();	
				} catch (Exception ex) {
					Commonutility.toWriteConsole("Exception forumServices.class forumViewAction() : "+ex);
					 log.logMessage("forumServices Exception forumViewAction : "+ex, "error", forumServices.class);
				} finally {
					if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
				}
				return forumDiscussList;
			}
	@Override
	public MvpFourmTopicsTbl getforumtopicsdetails(int topicsId) {
		MvpFourmTopicsTbl forumtopicsMst = new MvpFourmTopicsTbl();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From MvpFourmTopicsTbl where topicsId=:TOPICS_ID and status=:STATUS";
			Query qy = session.createQuery(qry);
			qy.setInteger("TOPICS_ID", topicsId);
			qy.setInteger("STATUS", 1);
			forumtopicsMst = (MvpFourmTopicsTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println(" getforumtopicsdetails======" + ex);
			 log.logMessage("forumServices Exception getforumtopicsdetails : "+ex, "error", forumServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return forumtopicsMst;
	}
	@Override
	public boolean forumCreationComments(MvpFourmDiscussTbl forumDiscussMst) {
		Session session = null;
		Transaction tx = null;
		 boolean result = false;
		session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();
		try {
			session.save(forumDiscussMst);
			result=true;
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in forumCreationComments : "+e);
			log.logMessage("forumServices Exception forumCreationComments : "+e, "error", forumServices.class);
			result=false;
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if(tx!=null){tx=null;}
		}	
		
		return result;
	}
	@Override
	public int gettopicsCount(int topicsId) {
		Session session = HibernateUtil.getSession();
		int totcnt = 0;
		try {
			String qry = "select count(*) From MvpFourmDiscussTbl where topicsId=:TOPICS_ID and status=:STATUS";
			Query qy = session.createQuery(qry);
			qy.setInteger("TOPICS_ID", topicsId);
			qy.setInteger("STATUS", 1);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (HibernateException ex) {			
			 log.logMessage("forumServices Exception gettopicsCount : "+ex, "error", forumServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return totcnt;
	}
	@Override
	public boolean getforumtopicsUpdate(MvpFourmTopicsTbl forumTopicsMst) {
		boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    Date date1;	  
	    CommonUtils comutil=new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	    try {
	      tx = session.beginTransaction();
	      Query qy = session.createQuery("update MvpFourmTopicsTbl set topicsName=:TOPICS_NAME, keyForSearch=:KEY_SEARCH, "
	              + " validOnDate=:VALID_DATE,topicsDesc=:DESCRIPTION,  modifyDatetime=:MODY_DATETIME"
	              + " where topicsId=:TOPICS_ID");
	      qy.setInteger("TOPICS_ID", forumTopicsMst.getTopicsId());
	      qy.setString("TOPICS_NAME", forumTopicsMst.getTopicsName());
	      qy.setString("KEY_SEARCH", forumTopicsMst.getKeyForSearch());
	      qy.setString("DESCRIPTION", forumTopicsMst.getTopicsDesc());
	      qy.setDate("VALID_DATE", forumTopicsMst.getValidOnDate());
	      qy.setTimestamp("MODY_DATETIME", date1);
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("forumServices Exception getforumtopicsUpdate : "+ex, "error", forumServices.class);
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    }
	    return result;
	}

}
