package com.pack.complaintsvo.persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.enewsvo.persistence.EeNewsDaoService;
import com.pack.eventvo.EventDispTblVO;
import com.pack.laborvo.LaborDetailsTblVO;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ComplaintsDaoservice implements ComplaintsDao {
	
	@Override
	public int toInsertComplaint(ComplaintsTblVO pFeedbckvoobj) {
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locEvntid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pFeedbckvoobj);
			locEvntid=pFeedbckvoobj.getComplaintsId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in ComplaintsDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", ComplaintsDaoservice.class);
			locEvntid=-1;
			return locEvntid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locEvntid;
	}

	@Override
	public boolean toUpdateComplaint(String pCmpltupdqry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pCmpltupdqry);
		return locRtnSts;		
	}

	@Override
	public boolean toDeactiveComplaint(String pCmpltvoobj) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pCmpltvoobj);
		return locRtnSts;	
	}

	@Override
	public boolean toDeleteComplaint(String pCmpltDlQry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pCmpltDlQry);
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
			log.logMessage("Exception : "+e, "error", ComplaintsDaoservice.class);
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
			System.out.println("Exception found in ComplaintsDaoservice.getInitTotal() : "+ex);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session=null;}
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
			System.out.println("Exception found in ComplaintsDaoservice.getTotalFilter() : "+ex);

		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session=null;}
		}
		return totcnt;
	}

	@Override
	public LaborDetailsTblVO getregistertable(int userid) {
		// TODO Auto-generated method stub
		LaborDetailsTblVO labordata = new LaborDetailsTblVO();
		Session session = null;
	    try {
	    	session = HibernateUtil.getSession();
	    	String qry = " from LaborDetailsTblVO where ivrBnLBR_ID=:USER_ID";
	    	Query qy = session.createQuery(qry);
	    	qy.setInteger("USER_ID", userid);
	    	labordata = (LaborDetailsTblVO) qy.uniqueResult();
	    } catch (Exception ex) {
	    	System.out.println("Exception found in ComplaintsDaoservice.getregistertable() : "+ex);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session=null;}
	      
	    }
	    return labordata;
	}

	@Override
	public int savecmpltattachfile(ComplaintattchTblVO iocCmpltattachObj) {
		// TODO Auto-generated method stub
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locDocid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(iocCmpltattachObj);
			locDocid=iocCmpltattachObj.getIvrBnATTCH_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in ComplaintsDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", ComplaintsDaoservice.class);
			locDocid=-1;
			return locDocid;
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locDocid;
	}

}
