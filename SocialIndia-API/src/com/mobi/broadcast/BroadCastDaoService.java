package com.mobi.broadcast;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pack.eventvo.EventTblVO;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.DocumentManageTblVO;

public class BroadCastDaoService implements BroadCastDao{
	Log log = new Log();
	@Override
	public List getEventBbroadcastSearchList(String qry, String startlim,String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<EventTblVO> eventList=new ArrayList<EventTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			if(totalrow!=null && !totalrow.equalsIgnoreCase("0")){
			qy.setMaxResults(Integer.parseInt(totalrow));
			}
			eventList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEventBbroadcastSearchList======" + ex);
			log.logMessage("BroadCastDaoService Exception getEventBbroadcastSearchList : "
							+ ex, "error", BroadCastDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return eventList;
	}
	@Override
	public List getEventBbroadcastDocuumentSearchList(String qry,
			String startlim, String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<DocumentManageTblVO> eventDocumentList=new ArrayList<DocumentManageTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			if(totalrow!=null && !totalrow.equalsIgnoreCase("0")){
			qy.setMaxResults(Integer.parseInt(totalrow));
			}
			eventDocumentList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEventBbroadcastSearchList======" + ex);
			log.logMessage("BroadCastDaoService Exception getEventBbroadcastDocuumentSearchList : "
							+ ex, "error", BroadCastDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return eventDocumentList;
	}
	

}
