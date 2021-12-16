package com.pack.reconcilevo.persistance;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.expencevo.persistance.ExpenceDaoservice;
import com.pack.reconcilevo.CyberplatrecondataTblVo;
import com.pack.reconcilevo.CyberplatsetmtfileTblVo;
import com.pack.reconcilevo.PaygaterecondataTblVo;
import com.pack.reconcilevo.PaygatesetmtfileTblVo;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ReconcileDaoService implements ReconcileDao {
  Log log = new Log();

  @Override
  public boolean toInsertPaygateReconData(PaygaterecondataTblVo prPaygatereconobj,Session txsession) {
    // TODO Auto-generated method stub
    boolean issaved=false;
    
    try {
      txsession.save(prPaygatereconobj);
      
      issaved = true;
      //tx.commit();
    } catch (Exception e) {
      log = new Log();
      System.out
          .println("Exception found in toInsertPaygateReconData ReconcileDaoService.class : "
              + e);
      log.logMessage("Exception in toInsertPaygateReconData : " + e, "error",
          ReconcileDaoService.class);
      return issaved;
    } finally {
      log = null;
    }
    return issaved;
  }

  @Override
  public boolean toInsertCyberplatReconData(
      CyberplatrecondataTblVo prCyberplatreconobj,Session txsession) {
    // TODO Auto-generated method stub

    boolean issaved=false;
    try {
      txsession.save(prCyberplatreconobj);
      issaved = true;
    } catch (Exception e) {
     
      log = new Log();
      System.out
          .println("Exception found in toInsertCyberplatReconData ReconcileDaoService.class : "
              + e);
      log.logMessage("Exception in toInsertCyberplatReconData : " + e, "error",
          ReconcileDaoService.class);
      return issaved;
    } finally {
     
      log = null;
    }
    return issaved;
  }

  @Override
  public boolean toUpdateReconcileData(String prReconupdqry) {
    // TODO Auto-generated method stub
    CommonDao locCmdo = new CommonDao();
    boolean locRtnSts = locCmdo.commonUpdate(prReconupdqry);
    return locRtnSts;
  }

  @Override
  public List<PaygatesetmtfileTblVo> togetPaygateReconcilFiles() {
    // TODO Auto-generated method stub

    List<PaygatesetmtfileTblVo> pgstmtfile = new ArrayList<PaygatesetmtfileTblVo>();
    Session session = null;
    try {
      session = HibernateUtil.getSession();
      String qry = "from PaygatesetmtfileTblVo where ivrBnSTATUS=1 and ivrBnExSTATUS=0 ";
      Query qy1 = session.createQuery(qry);
      pgstmtfile = qy1.list();

      return pgstmtfile;
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out
          .println("Exception   togetPaygateReconcilFiles ReconcileDaoService : "
              + ex);
      log.logMessage("Exception   togetPaygateReconcilFiles : " + ex, "error",
          ReconcileDaoService.class);
      return pgstmtfile;
    } finally {
      if (session != null) {
        session.flush();
        session.clear();
        session.close();
        session = null;
      }
    }

  }

  @Override
  public List<CyberplatsetmtfileTblVo> togetCyberplatReconcilFiles() {
    // TODO Auto-generated method stub

    List<CyberplatsetmtfileTblVo> cpstmtfile = new ArrayList<CyberplatsetmtfileTblVo>();
    Session session = null;
    try {

      session = HibernateUtil.getSession();
      String qry = "from CyberplatsetmtfileTblVo where ivrBnSTATUS=1 and ivrBnExctSTATUS=0 ";
      Query qy1 = session.createQuery(qry);
      cpstmtfile = qy1.list();

      return cpstmtfile;
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out
          .println("Exception   togetCyberplatReconcilFiles ReconcileDaoService : "
              + ex);
      log.logMessage("Exception   togetCyberplatReconcilFiles : " + ex,
          "error", ReconcileDaoService.class);
      return cpstmtfile;
    } finally {
      if (session != null) {
        session.flush();
        session.clear();
        session.close();
        session = null;
      }
    }
  }

  @Override
  public boolean updateReconFileStatus(String updQuery) {
    // TODO Auto-generated method stub
    CommonDao locCmdo = new CommonDao();
    boolean locRtnSts = locCmdo.commonUpdate(updQuery);
    return locRtnSts;
  }

  @Override
  public String toCallReconcileProc(int userId) {
    // TODO Auto-generated method stub
    Session session = null;
    Query callStoredProcedure_MYSQL = null;
    Transaction tx = null;
    try {
      session = HibernateUtil.getSession();
      tx = session.beginTransaction();
      callStoredProcedure_MYSQL = session.createSQLQuery("CALL proc_socailindia_settle(:USERID)");
      callStoredProcedure_MYSQL.setInteger("USERID", userId);
      Commonutility.toWriteConsole("CALL proc_socailindia_settle : SUCCESSFULLY CALLED");
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out
          .println("Exception   toCallReconcileProc ReconcileDaoService : "
              + ex);
      log.logMessage("Exception   toCallReconcileProc : " + ex, "error",
          ReconcileDaoService.class);
      return "Error";

    } finally {
      callStoredProcedure_MYSQL = null;
      session.flush();
      session.clear();
      session.close();
      session = null;
    }
    return "SUCCESS";
  }

  public int toInsertPaygateData(PaygatesetmtfileTblVo prPaygatereconobj) {
	    // TODO Auto-generated method stub
	   
	    Session session = null;
	    Transaction tx = null;
	    int locPaygatReconid=-1;
	    try {
	            session = HibernateUtil.getSessionFactory().openSession();
	            tx = session.beginTransaction();                        
	            session.save(prPaygatereconobj);
	            locPaygatReconid=prPaygatereconobj.getIvrBnPAYGATE_SETTLE_ID();
	            tx.commit();
	    } catch (Exception e) {
	            if (tx != null) {
	                    tx.rollback();
	            }
	            log=new Log();
	            System.out.println("Exception found in toInsertPaygateData ReconcileDaoService.class : "+e);
	            log.logMessage("Exception in toInsertPaygateData : "+e, "error", ReconcileDaoService.class);
	            locPaygatReconid=-1;
	            return locPaygatReconid;
	    } finally {
	            if(session!=null){session.close();session=null;}
	            if(tx!=null){tx=null;}  log=null;               
	    }
	    return locPaygatReconid;
	  }

@Override
public int toInsertcyberplateData(CyberplatsetmtfileTblVo prcyberplatereconobj) {
	
	  Session session = null;
	    Transaction tx = null;
	    int locPaygatReconid=-1;
	    try {
	            session = HibernateUtil.getSession();
	            tx = session.beginTransaction();                        
	            session.save(prcyberplatereconobj);
	            locPaygatReconid=prcyberplatereconobj.getIvrBnCBPLT_FILE_ID();
	            tx.commit();
	    } catch (Exception e) {
	            if (tx != null) {
	                    tx.rollback();
	            }
	            log=new Log();
	            System.out.println("Exception found in CyberplatsetmtfileTblVo ReconcileDaoService.class : "+e);
	            log.logMessage("Exception in toInsertcyberplateData : "+e, "error", ReconcileDaoService.class);
	            locPaygatReconid=-1;
	            return locPaygatReconid;
	    } finally {
	            if(session!=null){session.close();session=null;}
	            if(tx!=null){tx=null;}  log=null;               
	    }
	    return locPaygatReconid;
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

}
