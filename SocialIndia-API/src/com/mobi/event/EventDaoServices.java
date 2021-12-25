package com.mobi.event;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.eventvo.persistence.EventDaoservice;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.FacilityBookingTblVO;
import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public class EventDaoServices implements EventDao{

	Log log = new Log();
	@Override
	public FunctionMasterTblVO getfunctionMasterObjByQry(String qry) {
		// TODO Auto-generated method stub
		FunctionMasterTblVO functinMasterobj=new FunctionMasterTblVO();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			functinMasterobj = (FunctionMasterTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityList======" + ex);
			log.logMessage("EventDaoServices Exception getfunctionMasterObjByQry : "
							+ ex, "error", EventDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return functinMasterobj;
	}
	@Override
	public FunctionTemplateTblVO getfunctionTemplateObjByQry(String qry) {
		// TODO Auto-generated method stub
		FunctionTemplateTblVO functemplateinTobj=new FunctionTemplateTblVO();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			functemplateinTobj = (FunctionTemplateTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityList======" + ex);
			log.logMessage("EventDaoServices Exception getfunctionTemplateObjByQry : "
							+ ex, "error", EventDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return functemplateinTobj;
	}
	@Override
	public Integer saveCreateNewEvent(EventTblVO eventObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int bookingId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(eventObj);
			bookingId=eventObj.getIvrBnEVENT_ID();
			tx.commit();			
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			bookingId = -1;
			System.out.println("Step -1 : Exception found EventDaoServices.saveCreateNewEvent() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveCreateNewEvent() : "+ex, "error", EventDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return bookingId;	
	}
	@Override
	public int updateRsvp(String query) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int bookingId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			Query qy=session.createQuery(query);
			bookingId=qy.executeUpdate();
			tx.commit();			
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			bookingId = -1;
			System.out.println("Step -1 : Exception found EventDaoServices.saveCreateNewEvent() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveCreateNewEvent() : "+ex, "error", EventDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return bookingId;
	}
	@Override
	public List getEventDisplayList(String qry, String startlim,
			String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<EventDispTblVO> eventDispList=new ArrayList<EventDispTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			eventDispList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEventDisplayList======" + ex);
			log.logMessage("EventDaoServices Exception getEventDisplayList : "
							+ ex, "error", EventDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return eventDispList;
	}
	@Override
	public List<Object[]> getEventContributionSearchList(String qry,
			String startlim) {
		// TODO Auto-generated method stub
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getEventContributionSearchList " , "info", EventDaoServices.class);			
			System.out.println("qry-------------"+qry);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(qry)
					.addScalar("event_id", Hibernate.INTEGER)
					.addScalar("function_id", Hibernate.TEXT)
					.addScalar("function_name", Hibernate.TEXT)
					.addScalar("template_id", Hibernate.TEXT)
					.addScalar("template_msg", Hibernate.TEXT)
					.addScalar("descp", Hibernate.TEXT)
					.addScalar("attending", Hibernate.TEXT)
					.addScalar("INVITER", Hibernate.TEXT)
					.addScalar("INVITER_ID", Hibernate.TEXT)
					.addScalar("INVITER_IMG", Hibernate.TEXT)
					.addScalar("facility_booking_id", Hibernate.TEXT)
					.addScalar("location", Hibernate.TEXT)
					.addScalar("location_details",Hibernate.TEXT)
					.addScalar("date",Hibernate.TEXT)
					.addScalar("starttime",Hibernate.TEXT)
					.addScalar("endtime",Hibernate.TEXT)
					.addScalar("EVENT_FILE_NAME",Hibernate.TEXT)
					.addScalar("is_rsvp",Hibernate.TEXT)
					.addScalar("will_attend",Hibernate.TEXT)
					.addScalar("willnot_attend",Hibernate.TEXT)
					.addScalar("maybe_attend",Hibernate.TEXT)
					.addScalar("contribute_amt",Hibernate.TEXT)
					.addScalar("EVENT_LOCATION",Hibernate.TEXT)
					.addScalar("LAT_LONG",Hibernate.TEXT)
					.addScalar("ENDDATE",Hibernate.TEXT)
					.addScalar("socialimage",Hibernate.TEXT)
					.addScalar("event_title",Hibernate.TEXT)
					.addScalar("facility_id",Hibernate.TEXT);
			queryObj.setFirstResult(Integer.parseInt(startlim));
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into getEventContributionSearchList size :" + resultListObj.size(), "info", EventDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in getEventContributionSearchList :" + ex, "error", EventDaoServices.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}
	@Override
	public boolean updateEvent(EventTblVO eventObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		boolean isupdate = false;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String sql="";
			Query qy=null;
			System.out.println("eventObj-------------"+eventObj);
			String eventUpload = "";
			if (eventObj.getIvrBnEVENT_FILE_NAME() != null) {
				eventUpload = ",ivrBnEVENT_FILE_NAME=:FILE_NAME";
			}
			//if(eventObj!=null && eventObj.getIvrBnEVENT_FILE_NAME()!=null && eventObj.getIvrBnEVENT_FILE_NAME().length()>0){
				sql="update EventTblVO set ivrBnEVENT_TITLE=:EVENT_TITLE,ivrBnSHORT_DESC=:SHORT_DESCRIPTION "+eventUpload+",ivrBnEVENT_DESC=:DESCRIPTION,"
						+ "ivrBnSTARTDATE=:EVENT_START_DATE,ivrBnENDDATE=:EVENT_END_DATE,ivrBnSTARTTIME=:EVENT_START_TIME,ivrBnENDTIME=:EVENT_END_TIME,"
						+ "faciltyTemplateId.facilityId=:FACILITY_ID,ivrBnISRSVP=:IS_RSVP,"
						+ "ivrBnEVENT_LOCATION=:EVENT_LOCATION,ivrBnLAT_LONG=:LATITUDE_LONGITUDE,faciltyBookingId=:FACILITY_BOOKING_ID,"
						+ "ivrBnEVENTT_TYPE=:EVENT_TYPE,functionTemplateId=:FUNCTION_TEMPLATE_ID,ivrBnFUNCTION_ID=:FUNCTION_ID where ivrBnEVENT_ID=:EVENT_ID";
				qy=session.createQuery(sql);
				qy.setString("EVENT_TITLE", eventObj.getIvrBnEVENT_TITLE());
				qy.setString("SHORT_DESCRIPTION", eventObj.getIvrBnSHORT_DESC());
				qy.setString("DESCRIPTION", eventObj.getIvrBnEVENT_DESC());
				if (eventObj.getIvrBnEVENT_FILE_NAME() != null) {
					qy.setString("FILE_NAME", eventObj.getIvrBnEVENT_FILE_NAME());
				}
				
				qy.setString("EVENT_START_DATE", eventObj.getIvrBnSTARTDATE());
				qy.setString("EVENT_END_DATE", eventObj.getIvrBnENDDATE());
				qy.setString("EVENT_START_TIME", eventObj.getIvrBnSTARTTIME());
				qy.setString("EVENT_END_TIME", eventObj.getIvrBnENDTIME());
				qy.setParameter("FACILITY_BOOKING_ID", eventObj.getFaciltyBookingId());
				System.out.println(" eventObj.getFaciltyBookingId()--------------"+ eventObj.getFaciltyBookingId());
				if(eventObj.getFaciltyTemplateId()!=null){
				qy.setInteger("FACILITY_ID", eventObj.getFaciltyTemplateId().getFacilityId());
				}else{
					qy.setString("FACILITY_ID", null);
				}
				qy.setString("EVENT_LOCATION", eventObj.getIvrBnEVENT_LOCATION());
				qy.setString("LATITUDE_LONGITUDE", eventObj.getIvrBnLAT_LONG());
				qy.setInteger("IS_RSVP", eventObj.getIvrBnISRSVP());
				qy.setInteger("EVENT_ID", eventObj.getIvrBnEVENT_ID());
				qy.setInteger("EVENT_TYPE", eventObj.getIvrBnEVENTT_TYPE());
				qy.setParameter("FUNCTION_TEMPLATE_ID", eventObj.getFunctionTemplateId());
				qy.setParameter("FUNCTION_ID", eventObj.getIvrBnFUNCTION_ID());
				
			/*}else{
				sql="update EventTblVO set ivrBnEVENT_TITLE=:EVENT_TITLE,ivrBnSHORT_DESC=:SHORT_DESCRIPTION,ivrBnEVENT_DESC=:DESCRIPTION,"
						+ "ivrBnSTARTDATE=:EVENT_START_DATE,ivrBnENDDATE=:EVENT_END_DATE,ivrBnSTARTTIME=:EVENT_START_TIME,ivrBnENDTIME=:EVENT_END_TIME,"
						+ "faciltyTemplateId.facilityId=:FACILITY_ID,ivrBnISRSVP=:IS_RSVP,"
						+ "ivrBnEVENT_LOCATION=:EVENT_LOCATION,ivrBnLAT_LONG=:LATITUDE_LONGITUDE,faciltyBookingId=:FACILITY_BOOKING_ID,ivrBnEVENTT_TYPE=:EVENT_TYPE,"
						+ "functionTemplateId=:FUNCTION_TEMPLATE_ID,ivrBnFUNCTION_ID=:FUNCTION_ID"
						+ " where ivrBnEVENT_ID=:EVENT_ID";
				qy=session.createQuery(sql);
				System.out.println("--------"+eventObj.getIvrBnEVENT_TITLE());
				System.out.println("--------"+eventObj.getIvrBnSHORT_DESC());
				System.out.println("--------"+eventObj.getIvrBnSHORT_DESC());
				System.out.println("--------"+eventObj.getIvrBnSHORT_DESC());
				System.out.println("--------"+eventObj.getIvrBnLAT_LONG());
				System.out.println("--------"+eventObj.getIvrBnISRSVP());
				System.out.println("--------"+eventObj.getIvrBnEVENT_ID());
				
				qy.setString("EVENT_TITLE", eventObj.getIvrBnEVENT_TITLE());
				qy.setString("SHORT_DESCRIPTION", eventObj.getIvrBnSHORT_DESC());
				qy.setString("DESCRIPTION", eventObj.getIvrBnEVENT_DESC());
				qy.setString("EVENT_START_DATE", eventObj.getIvrBnSTARTDATE());
				qy.setString("EVENT_END_DATE", eventObj.getIvrBnENDDATE());
				qy.setString("EVENT_START_TIME", eventObj.getIvrBnSTARTTIME());
				qy.setString("EVENT_END_TIME", eventObj.getIvrBnENDTIME());
				if(eventObj.getFaciltyTemplateId()!=null){
				qy.setInteger("FACILITY_ID", eventObj.getFaciltyTemplateId().getFacilityId());
				}else{
					qy.setString("FACILITY_ID", null);
				}
				qy.setParameter("FACILITY_BOOKING_ID", eventObj.getFaciltyBookingId());
				System.out.println(" eventObj.getFaciltyBookingId()--------------"+ eventObj.getFaciltyBookingId());
				qy.setString("EVENT_LOCATION", eventObj.getIvrBnEVENT_LOCATION());
				qy.setString("LATITUDE_LONGITUDE", eventObj.getIvrBnLAT_LONG());
				qy.setInteger("IS_RSVP", eventObj.getIvrBnISRSVP());
				qy.setInteger("EVENT_ID", eventObj.getIvrBnEVENT_ID());
				qy.setInteger("EVENT_TYPE", eventObj.getIvrBnEVENTT_TYPE());
				qy.setParameter("FUNCTION_TEMPLATE_ID", eventObj.getFunctionTemplateId());
				qy.setParameter("FUNCTION_ID", eventObj.getIvrBnFUNCTION_ID());
			}*/
			System.out.println("eventObj.getFunctionTemplateId()----------------------"+eventObj.getFunctionTemplateId());
			int updstatus=qy.executeUpdate();
			if(updstatus>0){
				isupdate=true;
			}else{
				isupdate=false;
			}
			tx.commit();			
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			isupdate = false;
			System.out.println("Step -1 : Exception found EventDaoServices.saveCreateNewEvent() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveCreateNewEvent() : "+ex, "error", EventDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return isupdate;
	}
	@Override
	public int toInsertEventDispTbl(EventDispTblVO prEventdsipvoobj) {
		// TODO Auto-generated method stub
		Log log=null;
		Session session = null;
		Transaction tx = null;
		int locEvntDispid=-1;
		try {
			
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(prEventdsipvoobj);
			locEvntDispid=prEventdsipvoobj.getIvrBnEVENT_ID().getIvrBnEVENT_ID();
			tx.commit();
		} catch (Exception e) {
			log=new Log();
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in EventDaoservice.toInsertEventDispTbl() : "+e);
			log.logMessage("Exception : "+e, "error", EventDaoservice.class);
			locEvntDispid=-1;
			return locEvntDispid;
		} finally {
			if(session!=null){session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return locEvntDispid;
	}
	@Override
	public FacilityBookingTblVO getfacilityObjByBookingId(int bookingId) {
		// TODO Auto-generated method stub
		FacilityBookingTblVO facilityBookingobj=new FacilityBookingTblVO();
		Session session = HibernateUtil.getSession();
		try {
			String qry="from FacilityBookingTblVO where bookingId=:BOOKING_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("BOOKING_ID", bookingId);
			facilityBookingobj = (FacilityBookingTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityList======" + ex);
			log.logMessage("EventDaoServices Exception getfunctionTemplateObjByQry : "
							+ ex, "error", EventDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return facilityBookingobj;
	}
	@Override
	public EventTblVO geteventobjectByQuery(String qury) {
		// TODO Auto-generated method stub
		EventTblVO eventobj=new EventTblVO();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qury);
			eventobj = (EventTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityList======" + ex);
			log.logMessage("EventDaoServices Exception geteventobjectByQuery : "
							+ ex, "error", EventDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return eventobj;
	}

}
