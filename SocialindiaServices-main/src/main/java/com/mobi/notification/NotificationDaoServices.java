package com.mobi.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.common.CommonMobiDaoService;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.NotificationStatusTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class NotificationDaoServices implements NotificationDao{

	@Override
	public NotificationStatusTblVO getnotificationStatusbyQuery(String sql) {
		// TODO Auto-generated method stub
		Session session = null;
		Query qy = null;
		boolean result = false;
		String qry = "";
		session = HibernateUtil.getSession();
		Log log = new Log();
		NotificationStatusTblVO notifiobj=new NotificationStatusTblVO();
		try {

		
			qy = session.createQuery(sql);
			qy.setMaxResults(1);
			notifiobj = (NotificationStatusTblVO) qy.uniqueResult();
		} catch (HibernateException ex) {
			System.out.println(" getnotificationStatusbyQuery=====" + ex);
			log.logMessage(" Exception in NotificationDaoServices ---------- getnotificationStatusbyQuery : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				session = null;
			}
			qy = null;
		}
		return notifiobj;
	}

	@Override
	public List getnotificationlist(int rid,String startlim, String totalrow,String search) {
		// TODO Auto-generated method stub
		Session session = null;
		Query qy = null;
		boolean result = false;
		String qry = "";
		session = HibernateUtil.getSession();
		Log log = new Log();
		List<NotificationTblVO> notifiList=new ArrayList<NotificationTblVO>();
		try {
			String sql="from NotificationTblVO where userId.userId=:USER_ID and statusFlag=:STATUS "+search+" order by notificationId desc";
			qy = session.createQuery(sql);
			qy.setInteger("USER_ID", rid);
			qy.setInteger("STATUS", 1);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			notifiList = qy.list();
		} catch (HibernateException ex) {
			System.out.println(" getnotificationStatusbyQuery=====" + ex);
			log.logMessage(" Exception in NotificationDaoServices ---------- getnotificationlist : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				session = null;
			}
			qy = null;
		}
		return notifiList;
	}

	@Override
	public List getnotificationlistByQuery(String Query, String startlim,
			String totalrow) {
		// TODO Auto-generated method stub
		Session session = null;
		Query qy = null;
		boolean result = false;
		String qry = "";
		session = HibernateUtil.getSession();
		Log log = new Log();
		List<NotificationTblVO> notifiList=new ArrayList<NotificationTblVO>();
		try {
			qy = session.createQuery(Query);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			notifiList = qy.list();
		} catch (HibernateException ex) {
			System.out.println(" getnotificationStatusbyQuery=====" + ex);
			log.logMessage(" Exception in NotificationDaoServices ---------- getnotificationlistByQuery : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				session = null;
			}
			qy = null;
		}
		return notifiList;
	}

	@Override
	public boolean insertnewNotificationDetails(UserMasterTblVo toUserId,
			String descr, Integer tblrefFlag, Integer tblrefFlagType,
			Integer tblrefID, UserMasterTblVo entryBy, String additionalData) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		CommonUtils common=new CommonUtils();
		ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
		boolean issaved=false;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			
			Date curDate=new Date();
			NotificationTblVO notificationdet = new NotificationTblVO();
			notificationdet.setUserId(toUserId);
			String randnumber=common.randomString(Integer.parseInt(rb.getString("Notification.random.string.length")));
			notificationdet.setUniqueId(randnumber);
			notificationdet.setReadStatus(Integer.parseInt(rb.getString("Initial.read.Status")));
			notificationdet.setSentStatus(Integer.parseInt(rb.getString("Initial.sent.Status")));
			notificationdet.setStatusFlag(Integer.parseInt(rb.getString("Initial.statusflag")));
			
			System.out.println("tblrefFlag-----------"+tblrefFlag);
			if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.complaints"))){
				if(descr!=null && descr.length()>0){
					notificationdet.setDescr(descr);
				}else{
				notificationdet.setDescr(rb.getString("notification.cmplt.raise"));
				}
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.complaints")));
			}
			else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.skill"))){
				notificationdet.setDescr(rb.getString("notification.skill.interest"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.skill")));
			}
			else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.event"))){
				notificationdet.setDescr(rb.getString("notification.event.raise"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.event")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.feedback"))){
				notificationdet.setDescr(rb.getString("notification.feedback.raise"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.feedback")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.document"))){
				notificationdet.setDescr(rb.getString("notification.document"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.document")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.fourm"))){
				notificationdet.setDescr(rb.getString("notification.fourm.raise"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.fourm")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.facility"))){
				System.out.println("3333333333=============="+Commonutility.stringToInteger(rb.getString("notification.reflg.facility")));
				if(tblrefFlagType==1){
					notificationdet.setDescr(rb.getString("notification.facility.raise"));
				}else{
					notificationdet.setDescr(descr);
				}
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.facility")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.tender"))){
				notificationdet.setDescr(rb.getString("notification.tender.raise"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.tender")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.expense"))){
				notificationdet.setDescr(rb.getString("notification.expense.raise"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.expense")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.timelinefeed"))){
				notificationdet.setDescr(rb.getString("notification.timelinefeed.raise"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.timelinefeed")));
			}else if(tblrefFlag==Commonutility.stringToInteger(rb.getString("notification.reflg.timelineimagemontoring"))){
				notificationdet.setDescr(rb.getString("notification.timelineimagemontoring.raise"));
				notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.timelineimagemontoring")));
			}
			if(descr!=null && descr.length()>0){
				notificationdet.setDescr(descr);
			}
			
			notificationdet.setTblrefFlag(tblrefFlag);	
			notificationdet.setTblrefFlagType(tblrefFlagType);
			notificationdet.setTblrefID(tblrefID);
			notificationdet.setEntryBy(entryBy);
			notificationdet.setEntryDatetime(curDate);
			notificationdet.setAdditionalData(additionalData);
			
			session.save(notificationdet);
			tx.commit();
			issaved=true;
			System.out.println("notificationdet.getNotificationId--------------"+notificationdet.getNotificationId());
		}catch(HibernateException ex){
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("===========insertnewNotificationDetails Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return issaved;
	}

	@Override
	public boolean updateNotificationDetails(Integer tblrefFlag,
			Integer tblrefID, String additionalData) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		boolean issaved=false;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			String sql="update NotificationTblVO set additionalData=:ADDITIONAL_DATA where tblrefFlag=:REFTYPE and tblrefID=:REFID";
			Query qy = session.createQuery(sql);
			qy.setString("ADDITIONAL_DATA", additionalData);
			qy.setInteger("REFTYPE", tblrefFlag);
			qy.setInteger("REFID", tblrefID);
			qy.executeUpdate();
			tx.commit();
			issaved=true;
		}catch(HibernateException ex){
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("===========insertnewNotificationDetails Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return issaved;
	}

	@Override
	public boolean deleteNotificationDetails(Integer tblrefFlag,
			Integer tblrefID) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		boolean issaved=false;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			String sql="delete from NotificationTblVO where tblrefFlag=:REFTYPE and tblrefID=:REFID";
			Query qy = session.createQuery(sql);
			qy.setInteger("REFTYPE", tblrefFlag);
			qy.setInteger("REFID", tblrefID);
			qy.executeUpdate();
			tx.commit();
			issaved=true;
		}catch(HibernateException ex){
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("===========insertnewNotificationDetails Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return issaved;
	}

}
