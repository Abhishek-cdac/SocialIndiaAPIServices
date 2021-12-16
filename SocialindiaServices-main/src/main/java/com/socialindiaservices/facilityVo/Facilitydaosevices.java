package com.socialindiaservices.facilityVo;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.FacilityMstTblVO;

public class Facilitydaosevices implements facilityDao {

	@Override
	public int toInsertFacility(FacilityMstTblVO facilityobj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locFacid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(facilityobj);
			locFacid=facilityobj.getFacilityId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in facilityobj.class : "+e);
			log.logMessage("Exception : "+e, "error", Facilitydaosevices.class);
			locFacid=-1;
			return locFacid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locFacid;
	
	}

	public int toInsertwhyshould(WhyShouldIUpdateTblVO whyshouldObj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locwhyid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(whyshouldObj);
			locwhyid=whyshouldObj.getUniqId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in whyshould.class : "+e);
			log.logMessage("Exception : "+e, "error", Facilitydaosevices.class);
			locwhyid=-1;
			return locwhyid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locwhyid;
	
	
	}
	

}
