package com.pack.tenderVO.persistance;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.resident.persistance.ResidentDaoservice;
import com.pack.tenderVO.TenderDocTblVO;
import com.pack.tenderVO.TenderTblVO;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class TenderDaoservice implements TenderDao {
	
	@Override
	public int toInsertTender(TenderTblVO pTendervoobj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locTenderid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pTendervoobj);
			locTenderid=pTendervoobj.getTenderId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in TenderDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", TenderDaoservice.class);
			locTenderid=-1;
			return locTenderid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locTenderid;
	}

	@Override
	public boolean toUpdateTender(String pTenderupdqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pTenderupdqry);
		return locRtnSts;		
	}

	@Override
	public boolean toDeactiveEvent(String pEventdactqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEventdactqry);
		return locRtnSts;	
	}

	@Override
	public boolean toDeleteTender(String pTenderDlQry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pTenderDlQry);
		return locRtnSts;	
	}
	
	
	

	@Override
	public boolean toDeleteTenderDispTbl(String pTenderdispdlqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pTenderdispdlqry);
		return locRtnSts;	
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
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			session.close(); session = null;
		}
		return totcnt;
	}

	@Override
	public int saveTenderDoc_intRtn(TenderDocTblVO prTendervoobj) {
		// TODO Auto-generated method stub
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locDocid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(prTendervoobj);
			locDocid=prTendervoobj.getTenderDocId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in TenderDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", TenderDaoservice.class);
			locDocid=-1;
			return locDocid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locDocid;
	}

	@Override
	public boolean deleteTenderDocdblInfo(int pIntTenderDocid) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		String locDlqry=null;
		try{			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			locDlqry="delete from TenderDocTblVO where mvpTenderTbl = "+pIntTenderDocid+"";
			Query q = session.createQuery(locDlqry);
			
			q.executeUpdate();
			tx.commit();	
			return true;
		}catch(Exception e){
			if (tx != null) {
				tx.rollback();
			}			
			return false;
		}finally{
			if(session!=null){session.flush();session.clear();session.close();session=null;}if(tx!=null){tx=null;}
		}
	}

}
