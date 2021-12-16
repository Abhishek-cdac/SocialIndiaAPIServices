package com.mobi.carpooling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.CarMasterTblVO;
import com.socialindiaservices.vo.CarPoolBookingTblVO;
import com.socialindiaservices.vo.CarPoolingTblVO;
import com.socialindiaservices.vo.FacilityBookingTblVO;

public class CarPoolDaoServices implements CarPoolDao{
	Log log = new Log();

	@Override
	public List getCarPoolingList(String qry, String startlim, String totalrow,String timestamp) {
		// TODO Auto-generated method stub
		List<FacilityBookingTblVO> facilityList=new ArrayList<FacilityBookingTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//Date startDate = df.parse(timestamp);
			//qy.setDate("ENTRY_DATE", startDate);
			facilityList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getCarPoolingList======" + ex);
			log.logMessage("CarPoolDaoServices Exception getCarPoolingList : "
							+ ex, "error", CarPoolDaoServices.class);
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
	public Integer saveCarPoolingObject(CarPoolingTblVO carpoolObj,CarMasterTblVO carMasterObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int carpoolId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			Query sql=session.createQuery("from CarMasterTblVO where carNumber=:CAR_NUMBER ");
			sql.setString("CAR_NUMBER", carMasterObj.getCarNumber());
			CarMasterTblVO carmasterselobj= (CarMasterTblVO) sql.uniqueResult();
			if(carmasterselobj!=null && carmasterselobj.getEntryBy().getUserId()!=carpoolObj.getEntryBy().getUserId()){
				carpoolId = -2;
			}else{
			if(carmasterselobj!=null && carmasterselobj.getCarId()>0){
				Query carmas=session.createQuery("update CarMasterTblVO set carModel=:CAR_MODEL,preference=:PREFFERENCE where carNumber=:CAR_NUMBER");
				carmas.setString("CAR_MODEL",carMasterObj.getCarModel());
				carmas.setString("CAR_NUMBER",carMasterObj.getCarNumber());
				carmas.setString("PREFFERENCE",carMasterObj.getPreference());
				carmas.executeUpdate();
				carpoolObj.setCarId(carmasterselobj);
			}else{
				session.save(carMasterObj);
				carpoolObj.setCarId(carMasterObj);
			}
			
			session.save(carpoolObj);
			carpoolId = carpoolObj.getPoolId();
			
			
			tx.commit();
			}
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			carpoolId = -1;
			System.out.println("Step -1 : Exception found CarPoolDaoServices.saveCarPoolingObject() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveCarPoolingObject() : "+ex, "error", CarPoolDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return carpoolId;
	}

	@Override
	public boolean updateCarPoolingObject(CarPoolingTblVO carpoolObj,CarMasterTblVO carMasterObj) {
		// TODO Auto-generated method stub
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query carmas=session.createQuery("update CarMasterTblVO set carModel=:CAR_MODEL,preference=:PREFFERENCE where carNumber=:CAR_NUMBER and entryBy.userId=:USER_ID");
			carmas.setString("CAR_MODEL",carMasterObj.getCarModel());
			carmas.setString("CAR_NUMBER",carMasterObj.getCarNumber());
			carmas.setString("PREFFERENCE",carMasterObj.getPreference());
			carmas.setInteger("USER_ID",carMasterObj.getEntryBy().getUserId());
			int isupdate=carmas.executeUpdate();
			
			if(isupdate>0){
			Query qy = session
					.createQuery("update CarPoolingTblVO set youWillBe=:YOU_WILL_BE, returnTrip=:RETURN_TRIP, tripFromCity=:TRIP_FROM,"
							+ "tripToCity=:TRIP_TO,tripStartDate=:TRIP_START_DATE,tripEndDate=:TRIP_END_DATE,tripFrequency=:TRIP_FREQUENCY,additionalinfo=:ADDITIONAL_INFO,"
							+ "seatsAvaliable=:SEAT_AVALIABLE,days=:DAYS "
							+ " where  poolId=:POOLING_ID");
			qy.setInteger("YOU_WILL_BE", carpoolObj.getYouWillBe());
			qy.setInteger("RETURN_TRIP", carpoolObj.getReturnTrip());
			qy.setString("TRIP_FROM",carpoolObj.getTripFromCity());
			qy.setString("DAYS",carpoolObj.getDays());
			qy.setString("TRIP_TO",carpoolObj.getTripToCity());
			qy.setTimestamp("TRIP_START_DATE",carpoolObj.getTripStartDate());
			qy.setTimestamp("TRIP_END_DATE",carpoolObj.getTripEndDate());
			qy.setInteger("TRIP_FREQUENCY",carpoolObj.getTripFrequency());
			qy.setString("ADDITIONAL_INFO",carpoolObj.getAdditionalinfo());
			qy.setInteger("SEAT_AVALIABLE", carpoolObj.getSeatsAvaliable());
			qy.setInteger("POOLING_ID", carpoolObj.getPoolId());
			qy.executeUpdate();
			tx.commit();
			result = true;
			}else{
				result = false;
				if (tx != null) {
					tx.rollback();
				}
			}
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("CarPoolDaoServices Exception updateCarPoolingObject : "
					+ ex, "error", CarPoolDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public Integer saveCarBookingObject(CarPoolBookingTblVO carBookingObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int carpoolId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(carBookingObj);
			carpoolId=carBookingObj.getBookingId();
			tx.commit();			
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			carpoolId = -1;
			System.out.println("Step -1 : Exception found CarPoolDaoServices.saveCarBookingObject() : "+ex);
			logwrite.logMessage("Step -1 : Exception found saveCarBookingObject() : "+ex, "error", CarPoolDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return carpoolId;
	}

	@Override
	public boolean updateCarBookingObject(CarPoolBookingTblVO carBookingObj) {
		// TODO Auto-generated method stub
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update CarPoolBookingTblVO set poolId.poolId=:POOL_ID, bookedBy=:BOOKED_BY, comments=:COMMENTS,"
							+ " where  bookingId=:BOOKING_ID");
			qy.setParameter("POOL_ID", carBookingObj.getPoolId());
			qy.setParameter("BOOKED_BY", carBookingObj.getBookedBy());
			qy.setString("COMMENTS", carBookingObj.getComments());
			qy.setInteger("BOOKING_ID", carBookingObj.getBookingId());
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("CarPoolDaoServices Exception updateCarBookingObject : "
					+ ex, "error", CarPoolDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public Long getCarBookingCount(CarPoolBookingTblVO carBookingObj) {
		// TODO Auto-generated method stub
		long result = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session
					.createQuery("select count(*) from CarPoolBookingTblVO where  poolId=:POOLING_ID and bookedBy=:BOOKED_BY and status=:STATUS");
			qy.setParameter("POOLING_ID", carBookingObj.getPoolId());
			qy.setParameter("BOOKED_BY", carBookingObj.getBookedBy());
			qy.setInteger("STATUS", 1);
			long res=(Long) qy.uniqueResult();
			System.out.println("res---------------------"+res);
			result = res;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			result = 0;
			log.logMessage("CarPoolDaoServices Exception updateCarBookingObject : "
					+ ex, "error", CarPoolDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public boolean runupdateQueryCarPoolingTable(String sql) {
		// TODO Auto-generated method stub
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query qy = session.createQuery(sql);
			int isupdate=qy.executeUpdate();
			tx.commit();
			if(isupdate>0){
			result = true;
			}else{
				result = false;
			}
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("CarPoolDaoServices Exception runupdateQueryCarPoolingTable : "
					+ ex, "error", CarPoolDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public List getCarMAsterList(String qry) {
		// TODO Auto-generated method stub
		List<CarMasterTblVO> carMasterList=new ArrayList<CarMasterTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			carMasterList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getCarMAsterList======" + ex);
			log.logMessage("CarPoolDaoServices Exception getCarMAsterList : "
							+ ex, "error", CarPoolDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return carMasterList;
	}

}
