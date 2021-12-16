package com.pack.audittrialvo.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.pack.audittrialvo.AuditlogTblVO;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class AuditLogDaoservice implements AuditLogDao{

	@Override
	public int toWriteAudit(AuditlogTblVO pAuditobj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pAuditobj);
			locLbrid=pAuditobj.getIvrBnAUDIT_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in AuditLogDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", AuditLogDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if(tx!=null){tx=null;}			
		}
		return locLbrid;
	}
	

}
