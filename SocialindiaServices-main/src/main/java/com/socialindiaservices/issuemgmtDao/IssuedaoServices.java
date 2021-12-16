package com.socialindiaservices.issuemgmtDao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.function.persistence.FunctionDaoservices;
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;

public class IssuedaoServices implements IssueDao{

	@Override
	public int saveIssue(IssueTblVO locissvoObj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locfunid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(locissvoObj);
			locfunid=locissvoObj.getIssueId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in IssuedaoServices : "+e);
			log.logMessage("Exception : "+e, "error", IssuedaoServices.class);
			locfunid=-1;
			return locfunid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locfunid;
	}

}
