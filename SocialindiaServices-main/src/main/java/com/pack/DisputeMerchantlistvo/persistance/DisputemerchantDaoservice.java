package com.pack.DisputeMerchantlistvo.persistance;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.commonvo.EduMstrTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.expencevo.persistance.ExpenceDaoservice;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.utilitypkg.CommonDao;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.DisputeRiseTbl;
import com.socialindiaservices.vo.MerchantTblVO;

public class DisputemerchantDaoservice implements DisputemerchantDao{
	
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
			System.out.println("Exception found in WorktypeDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", DisputemerchantDaoservice.class);
			return false;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return true;
	}

	
	

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
	public int  toInsertCmplt(DisputeRiseTbl DisputeTblObj) {
		// TODO Auto-generated method stub
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locidcardid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(DisputeTblObj);
			locidcardid=DisputeTblObj.getDisputeId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in WorktypeDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", DisputemerchantDaoservice.class);
			locidcardid=-1;
			return locidcardid;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locidcardid;
	}




	@Override
	public boolean toDeactivateWorkType(String locUpdQry) {
		// TODO Auto-generated method stub
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(locUpdQry);
		return locRtnSts;
	}




	@Override
	public String toExistWorktypelistname(String lvrWorkTypetitle) {
		// TODO Auto-generated method stub
		  String query = "SELECT CASE WHEN COUNT(*)=0 THEN 'NEW' ELSE "
		          + "'ALREADY EXISTS' END FROM LaborWrkTypMasterTblVO where IVOlbrWORK_TYPE='"
		          + lvrWorkTypetitle + "'";
		  Log log=null;
			Session session = null;
			Transaction tx = null;
			Query qy = null;
			String getexistGroup = "";
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				qy = session.createQuery(query);
				getexistGroup = qy.uniqueResult().toString();	
				System.out.println("--- "+getexistGroup);
			} catch (Exception e) {
				log=new Log();
				System.out.println("Exception found in WorktypeDaoservice.class : "+e);
				log.logMessage("Exception : "+e, "error", DisputemerchantDaoservice.class);
				return getexistGroup;
			} finally {
				if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return getexistGroup;
		  
	}




	@Override
	public boolean toDeletedispute(String locDlQry) {
		
		// TODO Auto-generated method stub
				CommonDao locCmdo=new CommonDao();
				boolean locRtnSts=locCmdo.commonUpdate(locDlQry);
				return locRtnSts;	
	}




	@Override
	public boolean toClosedCmplt(String locDlQry) {
		// TODO Auto-generated method stub
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(locDlQry);
		return locRtnSts;	
	}




	@Override
	public UserMasterTblVo getusermasterData(Integer uniqadminId,
			Integer groupcode) {
		// TODO Auto-generated method stub
		UserMasterTblVo userListData = null;
		Session session = null;		
		try {
			userListData = new UserMasterTblVo();
			session = HibernateUtil.getSession();	
			String qry = "From UserMasterTblVo where  userId=:UNIQUE_ID   and statusFlag=:STATUS_FLAG";
			Query qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", uniqadminId);
			
			qy.setInteger("STATUS_FLAG", 1);
			userListData =  (UserMasterTblVo) qy.uniqueResult();
			//System.out.println("===========userListData=================="+userListData.getUserName());
			//System.out.println("===========userListData=================="+userListData.getSocietyId().getActivationKey());
		} catch (HibernateException ex) {			
			System.out.println("getuserData HibernateException======" + ex);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return userListData;
	}




	@Override
	public MerchantTblVO getMerchantmasterData(Integer uniqadminId,
			Integer groupcode) {
		// TODO Auto-generated method stub
		MerchantTblVO userListData = null;
		Session session = null;		
		try {
			userListData = new MerchantTblVO();
			session = HibernateUtil.getSession();	
			String qry = "From MerchantTblVO where  mrchntId=:UNIQUE_ID   and mrchntActSts=:STATUS_FLAG";
			Query qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", uniqadminId);
			
			qy.setInteger("STATUS_FLAG", 1);
			userListData =  (MerchantTblVO) qy.uniqueResult();
			//System.out.println("===========userListData=================="+userListData.getUserName());
			//System.out.println("===========userListData=================="+userListData.getSocietyId().getActivationKey());
		} catch (HibernateException ex) {			
			System.out.println("getuserData HibernateException======" + ex);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return userListData;
	}




	@Override
	public UserMasterTblVo getResidentmasterData(String DisputeUsrid,
			Integer groupcode) {
		// TODO Auto-generated method stub
		UserMasterTblVo userListData = null;
		Session session = null;		
		Integer DispUsrid=Integer.parseInt(DisputeUsrid);
		try {
			userListData = new UserMasterTblVo();
			session = HibernateUtil.getSession();	
			String qry = "From UserMasterTblVo where    userId="+DisputeUsrid+" and statusFlag=1 ";
			Query qy = session.createQuery(qry);
			/*qy.setInteger("UNIQUE_ID", DispUsrid);
			qy.setInteger("STATUS_FLAG", 1);*/
			userListData =  (UserMasterTblVo) qy.uniqueResult();
		} catch (HibernateException ex) {			
			System.out.println("getuserData HibernateException======" + ex);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return userListData;
	}




	@Override
	public LaborProfileTblVO getLabormasterData(Integer uniqlaborId,
			Integer groupcode) {
		// TODO Auto-generated method stub
		LaborProfileTblVO userListData = null;
		Session session = null;		
		
		try {
			userListData = new LaborProfileTblVO();
			session = HibernateUtil.getSession();	
			String qry = "From LaborProfileTblVO where    ivrBnLBR_ID="+uniqlaborId+" and ivrBnLBR_STS=1 ";
			Query qy = session.createQuery(qry);
			/*qy.setInteger("UNIQUE_ID", DispUsrid);
			qy.setInteger("STATUS_FLAG", 1);*/
			userListData =  (LaborProfileTblVO) qy.uniqueResult();
		} catch (HibernateException ex) {			
			System.out.println("getuserData HibernateException======" + ex);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return userListData;
	}

	
	
}
