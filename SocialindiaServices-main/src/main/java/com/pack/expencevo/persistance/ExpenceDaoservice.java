package com.pack.expencevo.persistance;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.expencevo.ExpenceTblVO;
import com.pack.tenderVO.TenderDocTblVO;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ExpenceDaoservice implements ExpenceDao {
	
	@Override
	public int toInsertExpence(ExpenceTblVO pExpencevoobj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locExpenceid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pExpencevoobj);
			locExpenceid=pExpencevoobj.getExpnId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in ExpenceDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", ExpenceDaoservice.class);
			locExpenceid=-1;
			return locExpenceid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locExpenceid;
	}

	@Override
	public boolean toUpdateExpence(String prExpenceupdqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(prExpenceupdqry);
		return locRtnSts;		
	}

	@Override
	public boolean toDeactiveExpence(String pEventdactqry) {
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
		Session session = null;
		Log log = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
			Query qy = session.createQuery(sql);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (Exception ex) {
			System.out.println("Exception found in ExpenceDaoservice getInitTotal: "+ex);
			log.logMessage("Exception getInitTotal : "+ex, "error", ExpenceDaoservice.class);

		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session=null;}log = null;
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		int totcnt = 0;
		Session session = null;
		Log log = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
			Query qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (Exception ex) {			
			System.out.println("Exception found in ExpenceDaoservice getTotalFilter: "+ex);
			log.logMessage("Exception getTotalFilter : "+ex, "error", ExpenceDaoservice.class);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session=null;}log = null;
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
			System.out.println("Exception found in ExpenceDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", ExpenceDaoservice.class);
			locDocid=-1;
			return locDocid;
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session=null;}
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
			e.printStackTrace();
			return false;
		}finally{
			if(session!=null){session.flush();session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}
		}
	}

}
