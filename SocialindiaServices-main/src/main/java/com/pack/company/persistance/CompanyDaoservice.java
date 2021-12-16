package com.pack.company.persistance;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.commonvo.CompanyMstTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.CommonDao;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class CompanyDaoservice implements CompanyDao{
		

	@Override
	public boolean updateCompanyInfo(String pIntLabrupdQry) {
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
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
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
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return totcnt;
	}

	@Override
	public int saveCompanyInfo_int(CompanyMstTblVO pIntcmprobj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pIntcmprobj);
			locLbrid=pIntcmprobj.getCompanyId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in companyDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", LaborDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
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
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		
	}

}
