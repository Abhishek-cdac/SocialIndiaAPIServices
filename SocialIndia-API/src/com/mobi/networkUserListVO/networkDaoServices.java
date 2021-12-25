package com.mobi.networkUserListVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class networkDaoServices implements  networkDao{
	Log log = new Log();
	@Override
	public List<MvpNetworkTbl> getnetworkList(String userId,
			String timestamp, String startlim, String totalrow,int societyId) {
		List<MvpNetworkTbl> networkUsrList = new ArrayList<MvpNetworkTbl>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpNetworkTbl where  usrParentUsrId.userId=:USER_ID and usrParentUsrId.societyId=:SOCIETY_ID and entryDatetime <=:ENTRY_DATE ";
			Query qy = session.createQuery(qry);
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("SOCIETY_ID", societyId);
			//qy.setInteger("STATUS", 1);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = df.parse(timestamp);
			qy.setTimestamp("ENTRY_DATE", startDate);
			networkUsrList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getnetworkList======" + ex);
			log.logMessage("networkDaoServices Exception getnetworkList : "
							+ ex, "error", networkDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return networkUsrList;
	}
	@Override
	public boolean inviteNetwork(String userId, String nwuserId, String type) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update MvpNetworkTbl set connStsFlg=:REQUEST_TYPE "
							+ "  where usrParentUsrId.userId=:USER_ID and  usrConnectedUsrId=:NETWORK_USER_ID");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("NETWORK_USER_ID", Integer.parseInt(nwuserId));
			qy.setInteger("REQUEST_TYPE", Integer.parseInt(type));
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			System.out.println(" inviteNetwork======" + ex);
			log.logMessage("networkDaoServices Exception inviteNetwork : "
					+ ex, "error", networkDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return result;
	}
	@Override
	public List<Object[]> getnetworkListSearch(String qry,
			 String startlim,String totalrow) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getnetworkListSearch " , "info", networkDaoServices.class);			
			
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(qry)
					.addScalar("USR_ID", Hibernate.INTEGER)
					.addScalar("FNAME", Hibernate.TEXT)
					.addScalar("LNAME", Hibernate.TEXT)
					.addScalar("IMAGE_NAME_MOBILE", Hibernate.TEXT)
					.addScalar("SOCIAL_PICTURE", Hibernate.TEXT)
					.addScalar("LAST_LOGIN", Hibernate.TIMESTAMP)
					.addScalar("ONLINE_STATUS", Hibernate.INTEGER)
					.addScalar("CONN_STS_FLG", Hibernate.INTEGER)
					.addScalar("PARENT_USR_ID", Hibernate.INTEGER)
					.addScalar("CONNECTED_USR_ID", Hibernate.INTEGER)
					.addScalar("NETWORK_ID",Hibernate.INTEGER);
			queryObj.setFirstResult(Integer.parseInt(startlim));
			queryObj.setMaxResults(Integer.parseInt(totalrow));
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into getnetworkListSearch size :" + resultListObj.size(), "info", networkDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in getnetworkListSearch :" + ex, "error", networkDaoServices.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
		
	}
	@Override
	public MvpNetworkTbl checkInviteExist(String fromid, String toid,
			String request) {
		MvpNetworkTbl networkMst =new MvpNetworkTbl();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpNetworkTbl where  ((usrParentUsrId.userId=:FROM_ID and  usrConnectedUsrId.userId=:TO_ID) or (usrParentUsrId.userId=:TO_ID and  usrConnectedUsrId.userId=:FROM_ID))";
			Query qy = session.createQuery(qry);
			qy.setInteger("FROM_ID", Integer.parseInt(fromid));
			qy.setInteger("TO_ID", Integer.parseInt(toid));
			networkMst = (MvpNetworkTbl) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" checkInviteExist======" + ex);
			log.logMessage("networkDaoServices Exception checkInviteExist : "
							+ ex, "error", networkDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return networkMst;
	}
	@Override
	public boolean insertInvite(MvpNetworkTbl networkmst) {
		// TODO Auto-generated method stub
				Session session = null;
				Transaction tx = null;
				Log logwrite = null;
				boolean isinsert=false;
				try {
					logwrite = new Log();
					session = HibernateUtil.getSession();
					tx = session.beginTransaction();
					session.save(networkmst);
					tx.commit();	
					isinsert=true;
				} catch (Exception ex) {			
					if (tx != null) {
						tx.rollback();
					}
					isinsert=false;
					System.out.println("Step -1 : Exception found insertInvite.insertInvite() : "+ex);
					logwrite.logMessage("Step -1 : Exception found insertInvite() : "+ex, "error", networkDaoServices.class);
					
				} finally {
					if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
					if (tx != null) {tx = null;} logwrite = null;
				}
				    return isinsert;
	}
	@Override
	public boolean checkApproveStatus(String fromid, String toid,
			String request) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update MvpNetworkTbl set connStsFlg=:REQUEST_TYPE "
							+ "  where usrParentUsrId.userId=:FROM_ID and  usrConnectedUsrId.userId=:TO_ID");
			qy.setInteger("FROM_ID", Integer.parseInt(fromid));
			qy.setInteger("TO_ID", Integer.parseInt(toid));
			qy.setInteger("REQUEST_TYPE", Integer.parseInt(request));
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			System.out.println(" checkApproveStatus======" + ex);
			log.logMessage("networkDaoServices Exception checkApproveStatus : "
					+ ex, "error", networkDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return result;
	}
	@Override
	public boolean deleteNetwork(String fromid, String toid, String request) {
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("delete from MvpNetworkTbl  "
							+ "  where usrParentUsrId.userId=:FROM_ID and  usrConnectedUsrId.userId=:TO_ID");
			qy.setInteger("FROM_ID", Integer.parseInt(fromid));
			qy.setInteger("TO_ID", Integer.parseInt(toid));
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			System.out.println(" checkApproveStatus======" + ex);
			log.logMessage("networkDaoServices Exception checkApproveStatus : "
					+ ex, "error", networkDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return result;
	}
	@Override
	public MvpNetworkTbl getNetWorkDetailById(int networkId) {
		// TODO Auto-generated method stub
		MvpNetworkTbl networkUsrObj = new MvpNetworkTbl();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpNetworkTbl where  networkId=:NETWORK_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("NETWORK_ID", networkId);
			networkUsrObj = (MvpNetworkTbl) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getnetworkList======" + ex);
			log.logMessage("networkDaoServices Exception getnetworkList : "
							+ ex, "error", networkDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return networkUsrObj;
	}
	@Override
	public MvpNetworkTbl getNetWorkDetailByUsers(String fromid, String toid,
			String request) {
		// TODO Auto-generated method stub
		MvpNetworkTbl networkUsrObj = new MvpNetworkTbl();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = "from MvpNetworkTbl where  usrParentUsrId.userId=:FROM_ID and  usrConnectedUsrId.userId=:TO_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("FROM_ID", Integer.parseInt(fromid));
			qy.setInteger("TO_ID", Integer.parseInt(toid));
			qy.setMaxResults(1);
			networkUsrObj = (MvpNetworkTbl) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getnetworkList======" + ex);
			log.logMessage("networkDaoServices Exception getnetworkList : "
							+ ex, "error", networkDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return networkUsrObj;
	}
	@Override
	public int getnetworkListSearchcount(String qry) {
		// TODO Auto-generated method stub
		Log log= new Log();
		int totalcunt=0;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getnetworkListSearch " , "info", networkDaoServices.class);			
			
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(qry)
					.addScalar("TOTALCOUNT", Hibernate.INTEGER);
			totalcunt = (Integer) queryObj.uniqueResult();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into getnetworkListSearch size :" + totalcunt, "info", networkDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in getnetworkListSearch :" + ex, "error", networkDaoServices.class);
			totalcunt=0;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return totalcunt;
	}

}
