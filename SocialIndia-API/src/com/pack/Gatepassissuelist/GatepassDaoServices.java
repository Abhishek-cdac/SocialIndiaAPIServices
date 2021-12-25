package com.pack.Gatepassissuelist;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.event.EventDaoServices;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.GatepassEntryTblVO;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;


public class GatepassDaoServices implements GatepassDao{
	Log log = new Log();
	@Override
	public List getGatepassList(String qry, String startlim,String totalrow,String timestamp) {
		// TODO Auto-generated method stub
		List<MvpGatepassDetailTbl> GatepassList=new ArrayList<MvpGatepassDetailTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			GatepassList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getGatepassList======" + ex);
			log.logMessage("GatepassDaoServices Exception getGatepassList : "
							+ ex, "error", GatepassDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return GatepassList;
	}
	@Override
	public List getGatepassMasterList(String qry, String startlim,
			String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<MvpGatepassDetailTbl> GatepassMasterList=new ArrayList<MvpGatepassDetailTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			GatepassMasterList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getGatepassMasterList======" + ex);
			log.logMessage("GatepassDaoServices Exception getGatepassMasterList : "
							+ ex, "error", GatepassDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return GatepassMasterList;
	}
	@Override
	public Integer saveGatepassBookingData(MvpGatepassDetailTbl GatepassObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int bookingId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(GatepassObj);
			bookingId = GatepassObj.getPassId();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			bookingId = -1;
			System.out.println("Step -1 : Exception found generalmgntDaoServices.staffCreation() : "+ex);
			logwrite.logMessage("Step -1 : Exception found staffCreation() : "+ex, "error", GatepassDaoServices.class);

		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return bookingId;
	}
	@Override
	public boolean updateGatepassBookingData(MvpGatepassDetailTbl GatepassObj) {
		// TODO Auto-generated method stub
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update MvpGatepassDetailTbl set visitorName='"+GatepassObj.getVisitorName()+"', dateOfBirth='"+GatepassObj.getDateOfBirth()+"', email='"+GatepassObj.getEmail()+"',"
							+ "mobileNo='"+GatepassObj.getMobileNo()+"',idCardNumber='"+GatepassObj.getIdCardNumber()+"',mvpIdcardTbl="+GatepassObj.getMvpIdcardTbl().getiVOcradid()+",issueDate='"+GatepassObj.getIssueDate()+"',issueTime='"+GatepassObj.getIssueTime()+"',"
							+ " expiryDate='"+GatepassObj.getExpiryDate()+"',description='"+GatepassObj.getDescription()+"',noOfAccompanies="+GatepassObj.getNoOfAccompanies()+",vehicleNumber='"+GatepassObj.getVehicleNumber()+"',entryBy="+GatepassObj.getEntryBy().getUserId()+" where  passId="+GatepassObj.getPassId()+"");
			/*qy.setString("TITLE", GatepassObj.getTitle());
			qy.setString("PLACE", GatepassObj.getPlace());
			qy.setString("DESCRIPTION",GatepassObj.getDescription());
			qy.setTimestamp("START_TIME",GatepassObj.getStartTime());
			qy.setTimestamp("END_TIME",GatepassObj.getEndTime());
			qy.setString("CONTACT_NO",GatepassObj.getContactNo());
			qy.setInteger("STATUS_FLAG",GatepassObj.getStatusFlag());
			qy.setInteger("Gatepass_ID",GatepassObj.getGatepassId().getGatepassId());
			qy.setInteger("BOOKING_ID", GatepassObj.getBookingId());*/
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("GatepassDaoServices Exception updateGatepassBookingData : "
					+ ex, "error", GatepassDaoServices.class);
		} finally {
			session.close();
		}
		return result;
	}
	@Override
	public boolean deleteGatepassBookingId(String userId, String GatepassBookingId) {
		// TODO Auto-generated method stub
		 Session session = HibernateUtil.getSession();
		    boolean result=false;
		    Transaction tx = null;
		    CommonUtils comutil=new CommonUtilsServices();
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update GatepassBookingTblVO set statusFlag=:STATUS  "
		              + " where bookedBy.userId=:USER_ID and  bookingId=:Gatepass_BOOKING_ID");
		      qy.setInteger("STATUS", 0);
		      qy.setInteger("USER_ID", Integer.parseInt(userId));
		      qy.setInteger("Gatepass_BOOKING_ID", Integer.parseInt(GatepassBookingId));
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
		      log.logMessage("GatepassDaoServices Exception deleteGatepassBookingId : "+ex, "error", GatepassDaoServices.class);
		    } finally {
		    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		    }
		    return result;
	}
	@Override
	public List getBookedGatepassList(String qry) {
		// TODO Auto-generated method stub
		List<MvpGatepassDetailTbl> GatepassList=new ArrayList<MvpGatepassDetailTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			GatepassList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getGatepassList======" + ex);
			log.logMessage("GatepassDaoServices Exception getGatepassList : "
							+ ex, "error", GatepassDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return GatepassList;
	}
	@Override
	public MvpGatepassDetailTbl getgatepassobjectByQuery(String qury) {
		// TODO Auto-generated method stub
		MvpGatepassDetailTbl gatepassObj=new MvpGatepassDetailTbl();
			Session session = HibernateUtil.getSession();
			try {
				Query qy = session.createQuery(qury);
				gatepassObj = (MvpGatepassDetailTbl) qy.uniqueResult();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(" getFacilityList======" + ex);
				log.logMessage("gatepassDaoservice Exception getgatepassObjectByQuery : "
								+ ex, "error", EventDaoServices.class);
			} finally {
				if (session != null) {
					session.flush();
					session.clear();
					session.close();
					session = null;
				}
			}
			return gatepassObj;
		}
	@Override
	public int updateRsvp(String query) {
		// TODO Auto-generated method stub
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
	public Integer saveGatepassEntryData(GatepassEntryTblVO GateentryObj) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				Session session = null;
				Transaction tx = null;
				Log logwrite = null;
				int bookingId = 0;
				try {
					logwrite = new Log();
					session = HibernateUtil.getSession();
					tx = session.beginTransaction();
					session.save(GateentryObj);
					bookingId = GateentryObj.getEntryId();
					tx.commit();
				} catch (Exception ex) {
					if (tx != null) {
						tx.rollback();
					}
					bookingId = -1;
					System.out.println("Step -1 : Exception found GatepassDaoServices.saveGatepassEntryData() : "+ex);
					logwrite.logMessage("Step -1 : Exception found staffCreation() : "+ex, "error", GatepassDaoServices.class);

				} finally {
					if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
					if (tx != null) {tx = null;} logwrite = null;
				}
				    return bookingId;
	}


}
