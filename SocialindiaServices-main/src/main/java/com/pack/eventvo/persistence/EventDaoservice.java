package com.pack.eventvo.persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class EventDaoservice implements EventDao {
	
	@Override
	public int toInsertEvent(EventTblVO pFeedbckvoobj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locEvntid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pFeedbckvoobj);
			locEvntid=pFeedbckvoobj.getIvrBnEVENT_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in EventDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", EventDaoservice.class);
			locEvntid=-1;
			return locEvntid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locEvntid;
	}

	@Override
	public boolean toUpdateEvent(String pEvntupdqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEvntupdqry);
		return locRtnSts;		
	}

	@Override
	public boolean toDeactiveEvent(String pEventdactqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEventdactqry);
		return locRtnSts;	
	}

	@Override
	public boolean toDeleteEvent(String pEventDlQry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEventDlQry);
		return locRtnSts;	
	}
	
	
	@Override
	public int toInsertEventDispTbl(EventDispTblVO pEventDsipvoobj) {
		
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locEvntDispid=-1;
		try {
			
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pEventDsipvoobj);
			locEvntDispid=pEventDsipvoobj.getIvrBnEVENT_ID().getIvrBnEVENT_ID();
			tx.commit();
		} catch (Exception e) {
			log=new Log();
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in EventDaoservice.toInsertEventDispTbl() : "+e);
			log.logMessage("Exception : "+e, "error", EventDaoservice.class);
			locEvntDispid=-1;
			return locEvntDispid;
		} finally {
			if(session!=null){session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locEvntDispid;
	}

	@Override
	public boolean toDeleteEventDispTbl(String pEvntdispdlqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEvntdispdlqry);
		return locRtnSts;	
	}

	@Override
	public int getInitTotal(String sql) {
		int totcnt = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query qy = session.createQuery(sql);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			System.out.println("Exception found in EventDaoservices.class getInitTotal : "+ex);

		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		int totcnt = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			System.out.println("Exception found in EventDaoservices.class getTotalFilter : "+ex);

		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
		}
		return totcnt;
	}

}
