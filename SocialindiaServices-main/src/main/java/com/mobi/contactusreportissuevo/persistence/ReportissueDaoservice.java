package com.mobi.contactusreportissuevo.persistence;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.contactusreportissuevo.ReportIssueTblVO;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.social.load.HibernateUtil;
import com.social.utils.Log;

public class ReportissueDaoservice implements ReportissueDao{
	
	
	public ReportissueDaoservice() {
		// TODO Auto-generated constructor stub
		System.out.println("ReportissueDaoservice class called");
	}

	
	@Override
	public int toInsertReportissue(ReportIssueTblVO reportIssueObj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locReportIssueid = -1;
		try {
			log.logMessage("Enter into  ReportissueDaoservice" , "info", ReportissueDaoservice.class);
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(reportIssueObj);
			locReportIssueid = reportIssueObj.getRepId();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			locReportIssueid = -1;
			log.logMessage("Exception found in toInsertReportissue: " + ex, "error", ReportissueDaoservice.class);
			ex.printStackTrace();
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return locReportIssueid;
	}

	@Override
	public boolean updateOTPIssue(int userId) {
	    Session session = null;
	    Transaction tx = null;
	    Query qy = null;boolean result = false;
	    Log log=new Log();
	    try {
	    	System.out.println("Step 1 : update otp issue block [Start].");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	Date date=new Date();
			CommonUtils comutil=new CommonUtilsServices();
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			qy = session.createQuery("update MvpUserOtpTbl set status=:STATUS,modifyDatetime=:MODYTIME where userId=:UID and status=:STATUSCHK");
		      qy.setInteger("UID", userId);
		      qy.setInteger("STATUSCHK", 2);
		      qy.setInteger("STATUS", 3);
		      qy.setTimestamp("MODYTIME", date);
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (HibernateException ex) {
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
	    	System.out.println(" updateOtpUser======" + ex);
			 log.logMessage("updateOTPIssue Exception updateOtpUser : "+ex, "error", ReportissueDaoservice.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    System.out.println("Step 3 :user block [End].");
	    return result;
	}

	@Override
	public Integer toGetUserId(String mobile) {
		Session session = HibernateUtil.getSession();
		Integer result=-1;
		Log log=new Log();
		try {
			String qry = "select userId From UserMasterTblVo where  mobileNo=:MOBILENO and statusFlag=:STATUS";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setString("MOBILENO", mobile);
			qy.setMaxResults(1);
			System.out.println("----------getSysParamsInfo------------" + qy);
		//	useridList = qy.list();
			result = (Integer) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println(" toGetUserId======" + ex);
			 log.logMessage("Exception toGetUserId : "+ex, "error", ReportissueDaoservice.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
		}
		return result;
	}

}
