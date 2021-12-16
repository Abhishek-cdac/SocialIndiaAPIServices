package com.mobi.contents;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.committeeDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ContentDaoServices implements ContentDao{
	Log log = new Log();
	
	@Override
	public List getFlashNewsList(String qry) {
		// TODO Auto-generated method stub
		List<FlashNewsTblVO> flashNewsList=new ArrayList<FlashNewsTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			flashNewsList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getCarPoolingList======" + ex);
			log.logMessage("ContentDaoServices Exception getFlashNewsList : "
							+ ex, "error", ContentDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return flashNewsList;
	}

	@Override
	public int getInitTotal(String preNewscntqry) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(preNewscntqry);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
		}
		return totcnt;
	}

	@Override
	public int toInsertFlashNew(FlashNewsTblVO flashObj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locnewid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(flashObj);
			locnewid=flashObj.getNewsId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in FlashNewsTblVO.class : "+e);
			log.logMessage("Exception : "+e, "error", ContentDaoServices.class);
			locnewid=-1;
			return locnewid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locnewid;
	
	}

	@Override
	public List<MvpFaqMstTbl> getFrequentQuestList(int rid, String searchQryText) {
		// TODO Auto-generated method stub
		List<MvpFaqMstTbl> freQuestList=new ArrayList<MvpFaqMstTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery("from MvpFaqMstTbl where "+searchQryText+"");
			freQuestList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("exception in getFrequentQuestList======" + ex);
			log.logMessage("ContentDaoServices Exception getFrequentQuestList : "
							+ ex, "error", ContentDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return freQuestList;
	}

}
