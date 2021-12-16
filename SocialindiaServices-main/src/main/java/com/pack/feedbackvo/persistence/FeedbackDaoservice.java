package com.pack.feedbackvo.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.feedbackvo.FeedbackTblVO;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class FeedbackDaoservice implements FeedbackDao {
	
	@Override
	public int toInsertFeedback(FeedbackTblVO pFeedbckvoobj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pFeedbckvoobj);
			locLbrid=pFeedbckvoobj.getIvrBnFEEDBK_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in FeedbackDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", FeedbackDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}			
		}
		return locLbrid;
	}

	@Override
	public boolean toUpdateFeedback(String pFeedupdqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pFeedupdqry);
		return locRtnSts;		
	}

	@Override
	public boolean toDeactiveFeedback(String pFeedbckdactqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pFeedbckdactqry);
		return locRtnSts;	
	}

}
