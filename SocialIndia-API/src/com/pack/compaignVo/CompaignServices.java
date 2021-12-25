package com.pack.compaignVo;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpDonationInstitutionTbl;

public class CompaignServices implements CompaignDao{

	@Override
	public int insertdate(MvpDonationInstitutionTbl donateObj) {

		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locnewid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(donateObj);
			locnewid=donateObj.getInstitutionId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in CompaignServices.class : "+e);
			log.logMessage("Exception : "+e, "error", CompaignServices.class);
			locnewid=-1;
			return locnewid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locnewid;
	
	
	}

}
