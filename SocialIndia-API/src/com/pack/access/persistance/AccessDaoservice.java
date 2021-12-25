package com.pack.access.persistance;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.accessvo.AccessDetailsVO;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class AccessDaoservice implements AccessDao{

	//private Transaction tx;
	Log log = new Log();
	public int saveAccessInfo(AccessDetailsVO accessObj) {
		
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(accessObj);
			locLbrid=accessObj.getId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception :AccessDaoservice saveAccessInfo() : "+e, "error", AccessDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if(tx!=null){tx=null;}
		}
		return locLbrid;
	}

	public boolean verificationIPInfo(String ipaddress, String pGmtTime, String pClienttime) {
		AccessDetailsVO accessInfo=new AccessDetailsVO();
		 Session session = null;
		    Transaction tx = null;		      
		    Query query =null, qy = null;
		    try {
		    session = HibernateUtil.getSession();
		    if(session.isConnected()){
		    	 tx=session.beginTransaction();
		    	 String qry = "from AccessDetailsVO where ipaddress=:IP_ADDRESS";
		    	 query = session.createQuery(qry);
			     query.setString("IP_ADDRESS",ipaddress);
			     List<AccessDetailsVO> accessInfos = query.list();			    	
		    	if(accessInfos!=null && !accessInfos.isEmpty()){
		    		String qry1="update AccessDetailsVO set accessCount=:ACCESS_COUNT+1,client=:CILENT_TIME,gmtTime=:GMT_TIME where ipaddress=:IP_ADDRESS"; 
		    		qy = session.createQuery(qry1);
//		    		System.out.println("ipaddress : ["+ipaddress+"]");
//		    		System.out.println(accessInfo);
		    		qy.setInteger("ACCESS_COUNT", accessInfo.getAccessCount()); 
		    	    qy.setString("CILENT_TIME", pClienttime); 
		    		qy.setString("GMT_TIME", pGmtTime); 
		    		qy.setString("IP_ADDRESS", accessInfo.getIpaddress());
		    		qy.executeUpdate();
		    		tx.commit();
		    	}else{
		    		return false;
		    	}
		    }
		    }catch(Exception ex){
		    	log.logMessage("Exception :AccessDaoservice verificationIPInfo() :"+ex, "error", AccessDaoservice.class);
		    	//ex.printStackTrace();
		    	return false;
		    }finally{
		    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}if(tx!=null){tx=null;}qy =null;
		    }
		return true;
	}

	@Override
	public boolean verificationUnique(String ipaddress, int uniqueId) {
		 Session session = null;
		 Transaction tx = null;	      
		    Query qy =null;
		    try {
		    session = HibernateUtil.getSession();
		    if(session.isConnected()){
		    	tx=session.beginTransaction();
		    	 String qry1="update AccessDetailsVO set usrId=:UNIQUE_ID where ipaddress=:IP_ADDRESS"; 
	    		  qy = session.createQuery(qry1);
	    			 qy.setInteger("UNIQUE_ID", uniqueId); 
	    			 qy.setString("IP_ADDRESS", ipaddress);
	    			 qy.executeUpdate();	    			 
	    			 tx.commit();
		    }
		    }catch(Exception ex){
		    	log.logMessage("Exception :AccessDaoservice verificationUnique: "+ex, "error", AccessDaoservice.class);
		    }finally{
		    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		    	if(tx!=null){tx=null;}qy =null;
		    }
		
		return true;
	}

		@Override
	public int getInitTotal(String sql) {
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(sql);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return totcnt;
	}
		public int getTotalFilter(String sqlQueryFilter) {
			int totcnt = 0;
			Session session = HibernateUtil.getSession();
			try {
				Query qy = session.createQuery(sqlQueryFilter);
				totcnt = ((Number) qy.uniqueResult()).intValue();				
			} catch (Exception ex) {
				ex.printStackTrace();

			} finally {
				if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			}
			return totcnt;
		}

}
