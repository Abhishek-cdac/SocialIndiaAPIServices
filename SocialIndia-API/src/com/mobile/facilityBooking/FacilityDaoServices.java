package com.mobile.facilityBooking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.FacilityBookingTblVO;
import com.socialindiaservices.vo.FacilityMstTblVO;

public class FacilityDaoServices implements FacilityDao{
	Log log = new Log();
	@Override
	public List getFacilityList(String qry, String startlim,String totalrow,String timestamp) {
		// TODO Auto-generated method stub
		List<FacilityBookingTblVO> facilityList=new ArrayList<FacilityBookingTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			facilityList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityList======" + ex);
			log.logMessage("FacilityDaoServices Exception getFacilityList : "
							+ ex, "error", FacilityDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return facilityList;
	}
	@Override
	public List getFacilityMasterList(String qry, String startlim,
			String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<FacilityMstTblVO> facilityMasterList=new ArrayList<FacilityMstTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			facilityMasterList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityMasterList======" + ex);
			log.logMessage("FacilityDaoServices Exception getFacilityMasterList : "
							+ ex, "error", FacilityDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return facilityMasterList;
	}
	@Override
	public Integer saveFacilityBookingData(FacilityBookingTblVO facilityObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int bookingId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(facilityObj);
			bookingId = facilityObj.getBookingId();
			tx.commit();			
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			bookingId = -1;
			System.out.println("Step -1 : Exception found generalmgntDaoServices.staffCreation() : "+ex);
			logwrite.logMessage("Step -1 : Exception found staffCreation() : "+ex, "error", FacilityDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return bookingId;
	}
	@Override
	public boolean updateFacilityBookingData(FacilityBookingTblVO facilityObj) {
		// TODO Auto-generated method stub
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update FacilityBookingTblVO set title=:TITLE, place=:PLACE, description=:DESCRIPTION,"
							+ "startTime=:START_TIME,endTime=:END_TIME,contactNo=:CONTACT_NO,statusFlag=:STATUS_FLAG,facilityId=:FACILITY_ID, commiteecomment=:COMMITEE_COMMENTS, bookingStatus=:BOOKING_STATUS"
							+ " where  bookingId=:BOOKING_ID");
			qy.setString("TITLE", facilityObj.getTitle());
			qy.setString("PLACE", facilityObj.getPlace());
			qy.setString("DESCRIPTION",facilityObj.getDescription());
			qy.setTimestamp("START_TIME",facilityObj.getStartTime());
			qy.setTimestamp("END_TIME",facilityObj.getEndTime());
			qy.setString("CONTACT_NO",facilityObj.getContactNo());
			qy.setInteger("FACILITY_ID",facilityObj.getFacilityId().getFacilityId());
			qy.setInteger("BOOKING_ID", facilityObj.getBookingId());
			
			qy.setInteger("STATUS_FLAG",facilityObj.getStatusFlag());
			qy.setInteger("BOOKING_STATUS", facilityObj.getBookingStatus());
			qy.setString("COMMITEE_COMMENTS", facilityObj.getCommiteecomment());
			
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("FacilityDaoServices Exception updateFacilityBookingData : "
					+ ex, "error", FacilityDaoServices.class);
		} finally {
			session.close();
		}
		return result;
	}
	@Override
	public boolean deleteFacilityBookingId(String userId, String facilityBookingId) {
		// TODO Auto-generated method stub
		 Session session = HibernateUtil.getSession();
		    boolean result=false;
		    Transaction tx = null;
		    CommonUtils comutil=new CommonUtilsServices();
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update FacilityBookingTblVO set statusFlag=:STATUS  "
		              + " where bookedBy.userId=:USER_ID and  bookingId=:FACILITY_BOOKING_ID");
		      qy.setInteger("STATUS", 0);
		      qy.setInteger("USER_ID", Integer.parseInt(userId));
		      qy.setInteger("FACILITY_BOOKING_ID", Integer.parseInt(facilityBookingId));
		      int isupdate=qy.executeUpdate();
		      if(isupdate>0){
		      tx.commit();
		      result = true;
		      }else{
		    	  tx.rollback();
		    	  result = false;
		      }
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("FacilityDaoServices Exception deleteFacilityBookingId : "+ex, "error", FacilityDaoServices.class);
		    } finally {
		    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		    }
		    return result;
	}
	@Override
	public List getBookedFacilityList(String qry) {
		// TODO Auto-generated method stub
		List<FacilityBookingTblVO> facilityList=new ArrayList<FacilityBookingTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			facilityList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityList======" + ex);
			log.logMessage("FacilityDaoServices Exception getFacilityList : "
							+ ex, "error", FacilityDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return facilityList;
	}
	@Override
	public FacilityBookingTblVO getFacilityBookingObjbyQuery(String query) {
		// TODO Auto-generated method stub
		FacilityBookingTblVO facilityBookingObj=new FacilityBookingTblVO();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(query);
			facilityBookingObj = (FacilityBookingTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFacilityMasterList======" + ex);
			log.logMessage("FacilityDaoServices Exception getFacilityMasterList : "
							+ ex, "error", FacilityDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return facilityBookingObj;
	}
	@Override
	public int checkCntBookingData(String rid, String startDateTime, String endDateTime,String facilityId) {
		// TODO Auto-generated method stub
		int retCntVal = 0;
		Session session = null;
		Query qy = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startTime = df.parse(startDateTime);
			Date endTime = df.parse(endDateTime);
			
			String query = "SELECT count(facilityId.facilityId) FROM FacilityBookingTblVO WHERE  (((startTime <= '" + startDateTime + "' and endTime >= '" + startDateTime + "') or (startTime <= '" + endDateTime + "' and endTime >= '" + endDateTime + "')) or  (('" + startDateTime + "' <= startTime and '" + endDateTime + "' >= startTime) or ('" + startDateTime + "' <= endTime and '" + endDateTime + "' >= endTime))) and facilityId.facilityId = '"+Integer.parseInt(facilityId)+"' and statusFlag = 1 and bookingStatus = 1";
			System.out.println("Facility [New] :  " + query);
			//Commonutility.toWriteConsole("Facility [New] : "+query);
			/*String query = "select count(facilityId.facilityId) from FacilityBookingTblVO where statusFlag=1 and bookedBy.userId='"+Integer.parseInt(rid)+"' "
					+ "and (( DATE(startTime) between date('" + startDateTime + "') and date('" + endDateTime + "')) and ( DATE(endTime) between date('" + startDateTime + "') and date('" + endDateTime + "'))) "
							+ "and bookingStatus in(0,1) and facilityId.facilityId='"+Integer.parseInt(facilityId)+"' group by facilityId.facilityId ";*/
			/*String query = "select count(facilityId.facilityId) from FacilityBookingTblVO  where statusFlag=1 and bookedBy.userId="+Integer.parseInt(rid)+" "
					+ " and ((startTime BETWEEN '"+startDateTime+"' and '"+endDateTime+"') and (endTime BETWEEN '"+startDateTime+"' and '"+endDateTime+"')) "
					+ " and bookingStatus in(0,1) group by facilityId.facilityId ";*/
			session = HibernateUtil.getSession();
			qy = session.createQuery(query);
			//System.out.println("Enter checkCntBookingData==============222======== " + qy);
			if (qy.uniqueResult() == null || qy.uniqueResult().toString().equalsIgnoreCase("null") 
					|| qy.uniqueResult().toString().equalsIgnoreCase("")) {
				retCntVal = 0;
			} else {
				retCntVal = ((Number) qy.uniqueResult()).intValue();
			}
			System.out.println("Enter checkCntBookingData=============3======== " + retCntVal);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" checkCntBookingData======" + ex);
			log.logMessage("FacilityDaoServices Exception checkCntBookingData : "
							+ ex, "error", FacilityDaoServices.class);
			retCntVal = 0;
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			qy = null;
		}
		return retCntVal;
	}

}

