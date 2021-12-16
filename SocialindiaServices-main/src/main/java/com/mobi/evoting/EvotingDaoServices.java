package com.mobi.evoting;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpEvotePoolingTbl;
import com.socialindiaservices.vo.MvpEvotingImageTbl;
import com.socialindiaservices.vo.MvpEvotingMstTbl;

public class EvotingDaoServices implements EvotingDao{

	Log log=new Log();
	@Override
	public MvpEvotingMstTbl insertEvotingMstTbl(MvpEvotingMstTbl evotingObj) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		try{
			tx = session.beginTransaction();
			 session.save(evotingObj);
			 tx.commit();
		}catch (Exception ex){
			if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found insertEvotingMstTbl  : "+ex);
			 log.logMessage("EvotingDaoServices Exception insertEvotingMstTbl : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return evotingObj;
	}
	@Override
	public MvpEvotingImageTbl insertEvotingImageTbl(
			MvpEvotingImageTbl evotingimageObj) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		try{
			tx = session.beginTransaction();
			 session.save(evotingimageObj);
			 tx.commit();
		}catch (Exception ex){
			if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found insertEvotingImageTbl  : "+ex);
			 log.logMessage("EvotingDaoServices Exception insertEvotingImageTbl : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return evotingimageObj;
	}
	@Override
	public boolean updateEvotingMstTbl(MvpEvotingMstTbl evotingObj) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		 boolean isupdate=false;
		try{
			tx = session.beginTransaction();
			/*String sql="update MvpEvotingMstTbl set titile=:TITLE,description=:DESCRIPTION,modifyBy=:MODIFYBY,modifyDatetime=:MODIFY_DATE "
					+ "where evotingId=:EVOTINGID and publishFlag=:PUBLISH_FLAG and usrRegTbl.userId=:USERID";*/
			String sql="update MvpEvotingMstTbl set titile='"+evotingObj.getTitile()+"',description='"+evotingObj.getDescription()+"',"
					+ "modifyBy='"+evotingObj.getModifyBy().getUserId()+"' "
					+ "where evotingId="+evotingObj.getEvotingId()+" and publishFlag='"+evotingObj.getPublishFlag()+"' and usrRegTbl.userId="+evotingObj.getModifyBy().getUserId();
			Query qy = session.createQuery(sql);
			/*qy.setString("TITLE", evotingObj.getTitile());
			qy.setString("DESCRIPTION", evotingObj.getDescription());
			qy.setInteger("MODIFYBY", evotingObj.getModifyBy().getUserId());
			qy.setInteger("EVOTINGID", evotingObj.getEvotingId());
			qy.setInteger("PUBLISH_FLAG", evotingObj.getPublishFlag());
			qy.setTimestamp("MODIFY_DATE",  evotingObj.getModifyDatetime());
			qy.setInteger("USERID", evotingObj.getModifyBy().getUserId());*/
			System.out.println("evotingObj.getModifyDatetime()-------------"+evotingObj.getModifyDatetime());
			int update=qy.executeUpdate();
			tx.commit();
			System.out.println("update---------------"+update);
			if(update>0){
				isupdate=true;
			}
		}catch (Exception ex){
			if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found updateEvotingMstTbl  : "+ex);
			 log.logMessage("EvotingDaoServices Exception updateEvotingMstTbl : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return isupdate;
	}
	@Override
	public MvpEvotingImageTbl selectEvotingImageTblbyQuery(String qry) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 boolean isupdate=false;
		 MvpEvotingImageTbl evotingobj=new MvpEvotingImageTbl();
		try{
			Query qy = session.createQuery(qry);
			qy.setMaxResults(1);
			evotingobj=(MvpEvotingImageTbl)qy.uniqueResult();
		}catch (Exception ex){
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found selectEvotingImageTblbyQuery  : "+ex);
			 log.logMessage("EvotingDaoServices Exception selectEvotingImageTblbyQuery : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			log=null;
		}
		return evotingobj;
	}
	@Override
	public boolean updateEvotingMstTblbyQuery(String qry) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		 boolean isupdate=false;
		try{
			tx = session.beginTransaction();
			Query qy = session.createQuery(qry);
			int update=qy.executeUpdate();
			tx.commit();
			if(update>0){
				isupdate=true;
			}
		}catch (Exception ex){
			if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found insertEvotingMstTbl  : "+ex);
			 log.logMessage("EvotingDaoServices Exception insertEvotingMstTbl : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return isupdate;
	}
	@Override
	public List getEvotingMastergList(String qry, String startlim, String totalrow,
			String timestamp) {
		// TODO Auto-generated method stub
		List<MvpEvotingMstTbl> evotingMstList=new ArrayList<MvpEvotingMstTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			evotingMstList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEvotingMastergList======" + ex);
			log.logMessage("EvotingDaoServices Exception getEvotingMastergList : "
							+ ex, "error", EvotingDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return evotingMstList;
	}
	@Override
	public long getEvotingCountbyQuery(String qry) {
		// TODO Auto-generated method stub
		long evotingCount=0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			evotingCount = (Long)qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEvotingCountbyQuery======" + ex);
			log.logMessage("EvotingDaoServices Exception getEvotingCountbyQuery : "
							+ ex, "error", EvotingDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return evotingCount;
	}
	@Override
	public List<Object[]> getEvotingMastergListbysqlQuery(String qry, String startlim,
			String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		try {
			Query queryObj = session.createSQLQuery(qry)
					.addScalar("rid", Hibernate.INTEGER)
					.addScalar("creator_emailid", Hibernate.TEXT)
					.addScalar("mobileno", Hibernate.TEXT)
					.addScalar("firstName", Hibernate.TEXT)
					.addScalar("lastName", Hibernate.TEXT)
					.addScalar("imagename", Hibernate.TEXT)
					.addScalar("socialImage", Hibernate.TEXT)
					.addScalar("EVOTING_ID", Hibernate.INTEGER)
					.addScalar("TITILE", Hibernate.TEXT)
					.addScalar("DESCRIPTION", Hibernate.TEXT)
					.addScalar("STATUS_FLAG", Hibernate.INTEGER)
					.addScalar("PUBLISH_FLAG", Hibernate.INTEGER)
					.addScalar("IMAGE_NAME", Hibernate.TEXT)
					.addScalar("AGREE", Hibernate.INTEGER)
					.addScalar("DISAGREE", Hibernate.INTEGER)
					.addScalar("NEED_MEETING", Hibernate.INTEGER)
					.addScalar("NO_VOTE", Hibernate.INTEGER)
					.addScalar("TOTAL_VOTING_STATUS", Hibernate.INTEGER)
					.addScalar("entryTime", Hibernate.TEXT)
					.addScalar("EVOTING_IMG_ID", Hibernate.INTEGER);
			queryObj.setFirstResult(Integer.parseInt(startlim));
			queryObj.setMaxResults(Integer.parseInt(totalrow));
			resultListObj = queryObj.list();
			System.out.println("resultListObj--------------"+resultListObj.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEvotingMastergList======" + ex);
			log.logMessage("EvotingDaoServices Exception getEvotingMastergList : "
							+ ex, "error", EvotingDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return resultListObj;
	}
	@Override
	public List<Object[]> getResidentEvotingMastergListbysqlQuery(String qry,
			String startlim, String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		try {
			Query queryObj = session.createSQLQuery(qry)
					.addScalar("rid", Hibernate.INTEGER)
					.addScalar("creator_emailid", Hibernate.TEXT)
					.addScalar("mobileno", Hibernate.TEXT)
					.addScalar("firstName", Hibernate.TEXT)
					.addScalar("lastName", Hibernate.TEXT)
					.addScalar("imagename", Hibernate.TEXT)
					.addScalar("socialImage", Hibernate.TEXT)
					.addScalar("EVOTING_ID", Hibernate.INTEGER)
					.addScalar("TITILE", Hibernate.TEXT)
					.addScalar("DESCRIPTION", Hibernate.TEXT)
					.addScalar("STATUS_FLAG", Hibernate.TEXT)
					.addScalar("PUBLISH_FLAG", Hibernate.INTEGER)
					.addScalar("IMAGE_NAME", Hibernate.TEXT)
					.addScalar("VOTING_DATE", Hibernate.TEXT)
					.addScalar("VOTING_STATUS", Hibernate.TEXT)
					.addScalar("VOTING_BY", Hibernate.TEXT)
					.addScalar("entryTime", Hibernate.TEXT)
					.addScalar("EVOTING_IMG_ID", Hibernate.TEXT);
			queryObj.setFirstResult(Integer.parseInt(startlim));
			queryObj.setMaxResults(Integer.parseInt(totalrow));
			resultListObj = queryObj.list();
			System.out.println("resultListObj--------------"+resultListObj.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEvotingMastergList======" + ex);
			log.logMessage("EvotingDaoServices Exception getEvotingMastergList : "
							+ ex, "error", EvotingDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return resultListObj;
	}
	@Override
	public MvpEvotingMstTbl selectEvotingmstTblbyQuery(String qry) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 boolean isupdate=false;
		 MvpEvotingMstTbl evotingobj=new MvpEvotingMstTbl();
		try{
			Query qy = session.createQuery(qry);
			qy.setMaxResults(1);
			evotingobj=(MvpEvotingMstTbl)qy.uniqueResult();
		}catch (Exception ex){
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found selectEvotingmstTblbyQuery  : "+ex);
			 log.logMessage("EvotingDaoServices Exception selectEvotingmstTblbyQuery : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			log=null;
		}
		return evotingobj;
	}
	@Override
	public MvpEvotePoolingTbl insertEvotingPoolingTbl(
			MvpEvotePoolingTbl evotingObj) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		try{
			tx = session.beginTransaction();
			 session.save(evotingObj);
			 tx.commit();
		}catch (Exception ex){
			if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found insertEvotingPoolingTbl  : "+ex);
			 log.logMessage("EvotingDaoServices Exception insertEvotingPoolingTbl : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return evotingObj;
	}
	@Override
	public Object[] getEvotingCountByQuery(String Sql) {
		// TODO Auto-generated method stub
		Object[] resultObjects = null;
		Session session = HibernateUtil.getSession();
		try {
			Query queryObj = session.createSQLQuery(Sql)
					.addScalar("AGREE", Hibernate.INTEGER)
					.addScalar("DISAGREE", Hibernate.INTEGER)
					.addScalar("NEED_MEETING", Hibernate.INTEGER)
					.addScalar("NO_VOTE", Hibernate.INTEGER)
					.addScalar("TOTAL_VOTE", Hibernate.INTEGER);
			queryObj.setMaxResults(1);
			resultObjects = (Object[]) queryObj.uniqueResult();
			System.out.println("resultObj--------------"+resultObjects.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getEvotingMastergList======" + ex);
			log.logMessage("EvotingDaoServices Exception getEvotingMastergList : "
							+ ex, "error", EvotingDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return resultObjects;
	}
	@Override
	public List<MvpEvotingImageTbl> selectEvotingmstTblListbyQuery(String qry) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 boolean isupdate=false;
		 
		 List<MvpEvotingImageTbl> evotingListobj=new ArrayList<MvpEvotingImageTbl>();
		try{
			Query qy = session.createQuery(qry);
			qy.setMaxResults(1);
			evotingListobj=qy.list();
		}catch (Exception ex){
		      ex.printStackTrace();
			System.out.println("EvotingDaoServices found selectEvotingmstTblListbyQuery  : "+ex);
			 log.logMessage("EvotingDaoServices Exception selectEvotingmstTblListbyQuery : "+ex, "error", EvotingDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			log=null;
		}
		return evotingListobj;
	}

}
