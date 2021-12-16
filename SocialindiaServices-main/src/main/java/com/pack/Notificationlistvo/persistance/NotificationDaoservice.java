package com.pack.Notificationlistvo.persistance;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.commonvo.EduMstrTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class NotificationDaoservice implements NotificationDao{
	
	public boolean saveLaborInfo(LaborProfileTblVO pIntLabrobj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pIntLabrobj);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			Commonutility.toWriteConsole("Exception found in EducationDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", NotificationDaoservice.class);
			return false;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return true;
	}

	
	

	@Override
	public boolean updateCompanyInfo(String pIntLabrupdQry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pIntLabrupdQry);
		return locRtnSts;
	}

	@Override
	public int getInitTotal(String sql) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(sql);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return totcnt;
	}
/*
	@Override
	public int  toInsertEducationType(EduMstrTblVO EducationtypeTblObj) {
		// TODO Auto-generated method stub
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locidcardid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(EducationtypeTblObj);
			locidcardid=EducationtypeTblObj.getiVoEDU_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			Commonutility.toWriteConsole("Exception found in EducationDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", NotificationDaoservice.class);
			locidcardid=-1;
			return locidcardid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locidcardid;
	}*/




	@Override
	public boolean toDeactivateNotifyType(String locUpdQry) {
		// TODO Auto-generated method stub
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(locUpdQry);
		return locRtnSts;
	}




	@Override
	public String toExistEducationtypelistname(String lvrEducationTypetitle) {
		// TODO Auto-generated method stub
		  String query = "SELECT CASE WHEN COUNT(*)=0 THEN 'NEW' ELSE "
		          + "'ALREADY EXISTS' END FROM EduMstrTblVO where iVoEDU_NAME='"
		          + lvrEducationTypetitle + "'";
		  Log log=null;
			Session session = null;
			Transaction tx = null;
			Query qy = null;
			String getexistGroup = "";
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				qy = session.createQuery(query);
				getexistGroup = qy.uniqueResult().toString();
			} catch (Exception e) {
				log=new Log();
				Commonutility.toWriteConsole("Exception found in EducationDaoservice.class : "+e);
				log.logMessage("Exception : "+e, "error", NotificationDaoservice.class);
				return getexistGroup;
			} finally {
				if(session!=null){session.flush();session.clear();session.close();session=null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return getexistGroup;
		  
	}

	@Override
	public String getInitSQLTotal(String sql) {
		String totcnt = null;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createSQLQuery(sql);
			totcnt = ((String) qy.uniqueResult());		
			/*List<User> users = (List<User>) query.list();
		    return users;*/
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return totcnt;
	}
	
}
