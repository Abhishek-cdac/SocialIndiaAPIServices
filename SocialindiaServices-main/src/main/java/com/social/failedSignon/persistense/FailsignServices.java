package com.social.failedSignon.persistense;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.social.failedSignonvo.FailedSignOnTbl;
import com.social.utils.Log;

public class FailsignServices implements FailsignDao{
	
	Log log=new Log();
	@Override
	public String failCreationForm(FailedSignOnTbl fail) {
		Session session = null;
		Transaction tx = null;
		String result="";
		session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();
		try {
			session.save(fail);
			result="success";
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in failedsignServices : "+e);
			log.logMessage("failedsign Services Exception : "+e, "error", FailsignServices.class);
			result="error";
			
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}
		}	
		
		return result;
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
}
