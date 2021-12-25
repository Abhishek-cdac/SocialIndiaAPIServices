package com.pack.enewsvo.persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.enewsvo.EeNewsDocTblVO;
import com.pack.enewsvo.EeNewsTblVO;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class EeNewsDaoService implements EeNewsDao{

	@Override
	public int toInserteNews(EeNewsTblVO pEnewsobj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locEvntid = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pEnewsobj);
			locEvntid=pEnewsobj.getIvrBnENEWS_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in EeNewsDaoService.class : "+e);
			log.logMessage("Exception : "+e, "error", EeNewsDaoService.class);
			locEvntid=-1;
			return locEvntid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locEvntid;
	}

	@Override
	public boolean toUpdateeNews(String pEnewsupdqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEnewsupdqry);
		return locRtnSts;
	}

	@Override
	public boolean toDeactivateeNews(String pEnewsdactveqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEnewsdactveqry);
		return locRtnSts;
	}

	@Override
	public boolean toDeleteeNews(String pEnewsdeleteqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEnewsdeleteqry);
		return locRtnSts;
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
	public int getTotalFilter(String preNewsfilterqry) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(preNewsfilterqry);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
		}
		return totcnt;
	}

	@Override
	public int saveEnewsDoc_intRtn(EeNewsDocTblVO prEnewsvoobj) {
		// TODO Auto-generated method stub
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locDocid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(prEnewsvoobj);
			locDocid=prEnewsvoobj.getEnewuniqId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in EeNewsDaoService : "+e);
			log.logMessage("Exception : "+e, "error", EeNewsDaoService.class);
			locDocid=-1;
			return locDocid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			if(tx!=null){tx=null;}
			
		}
		return locDocid;
	}

	@Override
	public boolean toDeleteEnewImgTbl(String pEnewsimgupdqry) {
		// TODO Auto-generated method stub
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pEnewsimgupdqry);
		return locRtnSts;	
	}

	@Override
	public boolean toDeleteEnewImgupdTbl(String lvrEnewsid) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		String locDlqry=null;
		try{			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			locDlqry="delete from EeNewsDocTblVO where Enewid = "+Integer.parseInt(lvrEnewsid)+"";
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


