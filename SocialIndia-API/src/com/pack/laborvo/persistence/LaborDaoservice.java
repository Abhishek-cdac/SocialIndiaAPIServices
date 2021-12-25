package com.pack.laborvo.persistence;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class LaborDaoservice implements LaborDao{

	@Override
	public boolean saveLaborInfo(LaborProfileTblVO pIntLabrobj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(pIntLabrobj);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in LaborDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", LaborDaoservice.class);
			return false;
		} finally {
			if(session!=null){session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}

		}
		return true;
	}

	@Override
	public boolean deActiveLaborInfo(LaborProfileTblVO pIntLabrobj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delteLaborInfo(LaborProfileTblVO pIntLabrobj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateLaborInfo(String pIntLabrupdQry) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pIntLabrupdQry);
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
			if(session!=null){session.flush();session.clear();session.clear(); session.close();session=null;}
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		int totcnt = 0;
		Session session = null;
		Log log=null;
		try {
			log=new Log();
			session = HibernateUtil.getSession();
			Query qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (Exception ex) {
			System.out.println("Exception found in LaborDaoservice : "+ex);
			log.logMessage("Exception : "+ex, "error", LaborDaoservice.class);

		} finally {
			if(session!=null){session.flush();session.clear();session.clear(); session.close();session=null;}log=null;
		}
		return totcnt;
	}

	@Override
	public int saveLaborInfo_int(LaborProfileTblVO pIntLabrobj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		Query lvrQry = null;
		try {
			session = HibernateUtil.getSession();
			String sl = "select count(*) from LaborProfileTblVO where ivrBnLBR_PH_NO = '"+pIntLabrobj.getIvrBnLBR_PH_NO() +"' and ivrBnID_CARD_TYP = "+pIntLabrobj.getIvrBnID_CARD_TYP() +" and ivrBnID_CARD_NO = '"+pIntLabrobj.getIvrBnID_CARD_NO() +"' and ivrBnLBR_PSTL_ID = "+pIntLabrobj.getIvrBnLBR_PSTL_ID()+"";
			lvrQry = session.createQuery(sl);			
			Long lveExstlbrcnt = (Long) lvrQry.uniqueResult();
			Commonutility.toWriteConsole("labour count : "+lveExstlbrcnt);
			if (lveExstlbrcnt<=0){
				tx = session.beginTransaction();
				session.save(pIntLabrobj);
				locLbrid=pIntLabrobj.getIvrBnLBR_ID();
				tx.commit();
			} else {
				locLbrid=-2;
			}
			
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in LaborDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", LaborDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.flush();session.clear();session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}

		}
		return locLbrid;
	}

	@Override
	public int saveLaborSkilInfo_intRtn(LaborSkillTblVO pIntLabrobj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(pIntLabrobj);
			locLbrid=pIntLabrobj.getIvrBnLBR_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in LaborDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", LaborDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.flush();session.clear();session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}

		}
		return locLbrid;
	}

	@Override
	public boolean deleteLabrSkillInfo(int pIntLborid) {
		Session session = null;
		Transaction tx = null;
		String locDlqry=null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			locDlqry="delete from LaborSkillTblVO where ivrBnLBR_ID = "+pIntLborid+"";
			Query q = session.createQuery(locDlqry);
			System.out.println("*****=== "+locDlqry);
			q.executeUpdate();
			tx.commit();
			return true;
		}catch(Exception e){
			if (tx != null) {
				tx.rollback();
			}
			return false;
		}finally{
			if(session!=null){session.flush();session.clear();session.clear(); session.close();session=null;}
		}

	}

}
