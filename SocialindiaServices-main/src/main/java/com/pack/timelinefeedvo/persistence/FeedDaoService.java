package com.pack.timelinefeedvo.persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class FeedDaoService implements FeedsDao{

	@Override
	public int toInsertTimelineFeeds(FeedsTblVO pFeedsobj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locTimelineFeedid = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pFeedsobj);
			locTimelineFeedid=pFeedsobj.getFeedId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in EeNewsDaoService.class : "+e);
			log.logMessage("Exception : "+e, "error", FeedDaoService.class);
			locTimelineFeedid=-1;
			return locTimelineFeedid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locTimelineFeedid;
	}

	@Override
	public boolean toUpdateTimelineFeeds(String pFeedsupdqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pFeedsupdqry);
		return locRtnSts;
	}

	@Override
	public boolean toDeactivateTimelineFeeds(String pFeedsdactveqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pFeedsdactveqry);
		return locRtnSts;
	}

	@Override
	public boolean toDeleteTimelineFeeds(String pFeedsdeleteqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pFeedsdeleteqry);
		return locRtnSts;
	}

	@Override
	public int getInitTotal(String prFeedscntqry) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(prFeedscntqry);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			session.close(); session = null;
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String prFeedsfilterqry) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(prFeedsfilterqry);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			session.close(); session =null;
		}
		return totcnt;
	}

}
