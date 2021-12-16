package com.pack.Worktypelistvo.persistance;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.commonvo.ResponseMsgTblVo;
import com.pack.Responsecodelist.ResponsecodelistUtility;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.commonvo.EduMstrTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.KnownusTblVO;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.commonvo.MvpBloodGroupTbl;
import com.pack.commonvo.MvpTitleMstTbl;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.expencevo.persistance.ExpenceDaoservice;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.siservices.common.common;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class WorktypeDaoservice implements WorktypeDao{
	
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
			log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
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
	public int  toInsertWorkType(LaborWrkTypMasterTblVO WorkTypeTblObj) {
		// TODO Auto-generated method stub
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locidcardid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(WorkTypeTblObj);
			locidcardid=WorkTypeTblObj.getWrktypId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in WorktypeDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
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
				log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
				return getexistGroup;
			} finally {
				if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return getexistGroup;
		  
	}




	@Override
	public int toInsertbloodtype(MvpBloodGroupTbl BloodTypeTblObj) {
		// TODO Auto-generated method stub
		
			// TODO Auto-generated method stub
			Log log=null;
			Session session = null;
			Transaction tx = null;
			int locidcardid=-1;
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();			
				session.save(BloodTypeTblObj);
				locidcardid=BloodTypeTblObj.getBloodGroupId();
				tx.commit();
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				log=new Log();
				System.out.println("Exception found in WorktypeDaoservice.class : "+e);
				log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
				locidcardid=-1;
				return locidcardid;
			} finally {
				if(session!=null){session.close();session=null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return locidcardid;
		}
	

	@Override
	public String toExistBloodtypelistname(String lvrWorkTypetitle) {
		// TODO Auto-generated method stub
		  String query = "SELECT CASE WHEN COUNT(*)=0 THEN 'NEW' ELSE "
		          + "'ALREADY EXISTS' END FROM MvpBloodGroupTbl where bloodGroupName='"
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
				log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
				return getexistGroup;
			} finally {
				if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return getexistGroup;
		  
	}




	@Override
	public int toInserttitle(MvpTitleMstTbl TitleTblObj) {
		// TODO Auto-generated method stub
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locidcardid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(TitleTblObj);
			locidcardid=TitleTblObj.getTitleId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in WorktypeDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
			locidcardid=-1;
			return locidcardid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locidcardid;
	}




	@Override
	public String toExistTitlelistname(String lvrTypetitle) {
		// TODO Auto-generated method stub
		 String query = "SELECT CASE WHEN COUNT(*)=0 THEN 'NEW' ELSE "
		          + "'ALREADY EXISTS' END FROM MvpTitleMstTbl where description='"
		          + lvrTypetitle + "'";
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
				log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
				return getexistGroup;
			} finally {
				if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return getexistGroup;
	}




	@Override
	public int toInsertknownus(KnownusTblVO KnownusTblObj) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locidcardid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(KnownusTblObj);
			locidcardid=KnownusTblObj.getiVOKNOWN_US_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in WorktypeDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
			locidcardid=-1;
			return locidcardid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locidcardid;
	}




	@Override
	public String toExistKnownuname(String lvrTypetitle) {
		// TODO Auto-generated method stub
		 String query = "SELECT CASE WHEN COUNT(*)=0 THEN 'NEW' ELSE "
		          + "'ALREADY EXISTS' END FROM KnownusTblVO where iVOKNOWNUS='"
		          + lvrTypetitle + "'";
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
				log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
				return getexistGroup;
			} finally {
				if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return getexistGroup;
	}
	
	@Override
	public String toExistResponselistname(String lvrTyperesponsemsg) {
		// TODO Auto-generated method stub
		
		 String query = "SELECT CASE WHEN COUNT(*)=0 THEN 'NEW' ELSE "
		          + "'ALREADY EXISTS' END FROM ResponseMsgTblVo where message='"
		          + lvrTyperesponsemsg + "'";
		  Log log=null;
			Session session = null;
			Transaction tx = null;
			Query qy = null;
			String getexistGroup = "";
			try {
				session = HibernateUtil.getSession();
				qy = session.createQuery(query);
				getexistGroup = qy.uniqueResult().toString();
			} catch (Exception e) {
				log=new Log();
				System.out.println("Exception found in WorktypeDaoservice.class : "+e);
				log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
				return getexistGroup;
			} finally {
				if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
				if(tx!=null){tx=null;}	log=null;		
			}
			return getexistGroup;
	}
	@Override
	public int toInsertResponseMsg(ResponseMsgTblVo ResponsemsgTblObj) {
		// TODO Auto-generated method stub
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locidcardid=-1;
		try {
			log=new Log();
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(ResponsemsgTblObj);
			locidcardid = ResponsemsgTblObj.getUniqId();
			String respcode = Commonutility.toGenerateID("R",locidcardid,"5");
			String locUpdQry = "update ResponseMsgTblVo set responseCode ='"+respcode+"' where uniqId ="+locidcardid+"";
			log.logMessage("Step 3 : responsecode  Update Query : "+locUpdQry, "info", WorktypeDaoservice.class);
			tx = session.beginTransaction();
			Query lvrQryobj = session.createQuery(locUpdQry);
			lvrQryobj.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			
			System.out.println("Exception found in WorktypeDaoservice.class : "+e);
			log.logMessage("Exception : "+e, "error", WorktypeDaoservice.class);
			locidcardid=-1;
			return locidcardid;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locidcardid;
	}
}
