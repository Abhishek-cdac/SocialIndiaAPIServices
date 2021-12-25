package com.social.utils;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.socialindiaservices.vo.BiometricDatabaseInfoTblVO;

public class BioMetricThread extends Thread {

	int hour = 1;
	static boolean stop = true;
	
	@Override
	public void run() {
		BiometricUtility.toWriteConsole("BioMetricThread processing - run() "+ Thread.currentThread().getName());
		
		while (stop) {
			try {
				// Get database connection, delete unused data from DB
				doWork();
				
				// wait for 2 hours
				BiometricUtility.toWriteConsole(new Date() + " BioMetricThread processing - before sleep() "+ Thread.currentThread().getName());
				Thread.sleep(((1000 * 60) * 60) * hour);
				BiometricUtility.toWriteConsole(new Date() + " BioMetricThread processing - after sleep() "+ Thread.currentThread().getName());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void doWork() throws InterruptedException {
		BiometricUtility.toWriteConsole("BioMetricThread processing - doWork():Start "+ Thread.currentThread().getName());
		
		//delete old data
		boolean delete = false;
		
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			session.createQuery("delete from BiometricUserInfoTblVO").executeUpdate();
			tx.commit();
			delete = true;
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			BiometricUtility.toWriteConsole("Step -1 : Exception found BioMetricThread delete data : "+ex);
			logwrite.logMessage("Step -1 : Exception found BioMetricThread delete data : "+ex, "error", BioMetricThread.class);
			delete = false;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		
		
		//insert new data
		if(delete){
			BiometricUtility biometricUtility = new BiometricUtility();
			
			List<BiometricDatabaseInfoTblVO> list = biometricUtility.getBioMetricRemoteHost();
			for (int i = 0; i < list.size(); i++) {
				BiometricDatabaseInfoTblVO tblVO = list.get(i);
				biometricUtility = new BiometricUtility();
				biometricUtility.biometricUtility(tblVO.getBioHostname(),tblVO.getBioPort(),tblVO.getBioUsername(),tblVO.getBioPassword(),tblVO.getBioDatabase(),tblVO.getSocyteaId());
			}
		}
		
		BiometricUtility.toWriteConsole("BioMetricThread processing - doWork():End "+ Thread.currentThread().getName());
	}

}