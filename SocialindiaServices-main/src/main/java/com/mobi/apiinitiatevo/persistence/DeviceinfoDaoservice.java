package com.mobi.apiinitiatevo.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.apiinitiatevo.DeviceinfoAccesslogVO;
import com.social.load.HibernateUtil;
import com.social.utils.Log;

public class DeviceinfoDaoservice implements DeviceinfoDao{

	@Override
	public int toInsertDeviceinfo(DeviceinfoAccesslogVO deviceinfoObj) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locDeviceid = -1;
		try {
			log.logMessage("Enter into  toInsertDeviceinfo" , "info", DeviceinfoDaoservice.class);
			session = HibernateUtil.getSessionFactory().openSession();
			System.out.println("AF sessinon");
			log.logMessage("Enter into  toInsertDeviceinfo:::" + deviceinfoObj.getDevicekey() , "info", DeviceinfoDaoservice.class);
			tx = session.beginTransaction();
			session.save(deviceinfoObj);
			locDeviceid = deviceinfoObj.getUniqid();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			locDeviceid = -1;
			log.logMessage("Exception found in toInsertDeviceinfo: " + ex, "error", DeviceinfoDaoservice.class);
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return locDeviceid;
	}

}
