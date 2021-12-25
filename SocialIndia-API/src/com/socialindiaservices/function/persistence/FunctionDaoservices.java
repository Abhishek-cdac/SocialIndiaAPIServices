package com.socialindiaservices.function.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public class FunctionDaoservices implements FunctionDao{

	@Override
	public int saveFunctiontxt(FunctionTemplateTblVO inrlocfun) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locfunid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(inrlocfun);
			locfunid=inrlocfun.getFunctionTempId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in FunctionDaoservices : "+e);
			log.logMessage("Exception : "+e, "error", FunctionDaoservices.class);
			locfunid=-1;
			return locfunid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locfunid;
	}

	@Override
	public int saveFunctionMas(FunctionMasterTblVO locfdbkvoObj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locfuntxtid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(locfdbkvoObj);
			locfuntxtid=locfdbkvoObj.getFunctionId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in FunctionDaoservices : "+e);
			log.logMessage("Exception : "+e, "error", FunctionDaoservices.class);
			locfuntxtid=-1;
			return locfuntxtid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locfuntxtid;
	}
	

}
