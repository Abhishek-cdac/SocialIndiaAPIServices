package com.social.billmaintenanceDao;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.commonvo.FlatMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.billmaintenanceVO.BillmaintenanceVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public class BillMaintenanceDaoServices implements BillMaintenanceDao{

	@Override
	public boolean insertbilldata(BillmaintenanceVO maintanencebilldata, Session session) {
		
		boolean issaved = false;
		int id = 0;
		try {
			session.save(maintanencebilldata);
			issaved = true;
		} catch (HibernateException ex) {
			System.out.println("===========BillMaintenanceDaoServices Insert================"+ex);
			id = 0;
			return issaved;
		}
		return issaved;
	
	}

	@Override
	public boolean toDeleteEvent(String qry) {
		boolean locRtn= false;
			Session session = null;
			Transaction tx = null;
			Query lvrQryobj = null;
			try {
				session = HibernateUtil.getSession();
				tx = session.beginTransaction();
				lvrQryobj = session.createQuery(qry);
				lvrQryobj.executeUpdate();
				tx.commit();
				locRtn= true;				
			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				Commonutility.toWriteConsole("HibernateException bill delete Query : "+e);
				locRtn= false;				
			} finally {
				if(session!=null){session.flush();session.clear();session.close();session=null;}
				lvrQryobj = null;
			}		
			return locRtn;
	
	}

	@Override
	public String checkDuplicate(String qry) {
		System.out.println("qry--------qry -" + qry);
		 // TODO Auto-generated method stub
	    String socetyid = null;
	    Session session = null;
	    Query qy =null;
	    try {
	      session = HibernateUtil.getSession();
	      qy = session.createQuery(qry);
	      socetyid = (String) qy.uniqueResult();	
	   
	    } catch (HibernateException ex) {
	      ex.printStackTrace();
	      System.out.println("HibernateException--------ex -" + ex);
	      return ex.getMessage();
	    } finally {
	      session.close();
	    }
	    return socetyid;
	
	}

	@Override
	public boolean checksocietytown(String townMst,String socty) {
		Session session = HibernateUtil.getSession();		
		String qry = "";
		Query query = null;
		boolean statusflg = false;
	    SocietyMstTbl soctyMgmt=null;
	    try {
			session = HibernateUtil.getSession();			
	    	qry = " from SocietyMstTbl where  societyUniqyeId='"+socty+"' and townshipId.townshipUniqueId='"+townMst+"' and statusFlag=1 ";	 
	    	Commonutility.toWriteConsole("qry : "+qry);
	    	query = session.createQuery(qry);	
	    	soctyMgmt = (SocietyMstTbl) query.uniqueResult();
	    	
			if (soctyMgmt != null) {
				Commonutility.toWriteConsole("soctyMgmt : "+soctyMgmt.getActivationKey());
				statusflg = true;
			} else {
				Commonutility.toWriteConsole("ELSE soctyMgmt : "+soctyMgmt);
				statusflg = false;
			}	    	
	    } catch (HibernateException ex) {
	    	 Commonutility.toWriteConsole("Exception Found checkSocietyMstTbl()  : "+ex);
	    	 return false;
	    } finally {
	    	if(session!=null){ session.flush();session.clear(); session.close(); session = null;}query = null;
			 
	    }
	    return statusflg;
	}

	@Override
	public boolean toInsertmaintenance(MaintenanceFeeTblVO maintanencefee,Session session) {
		// TODO Auto-generated method stub
		//Session session =null;
		boolean issaved=false;
		//Transaction tx = null;
		try {
			//tx = session.beginTransaction();
			//session = HibernateUtil.getSession();
			session.save(maintanencefee);
			//tx.commit();
			issaved=true;
		} catch(HibernateException ex){
			System.out.println("===========insertMaintenanceFeeTbl Insert================"+ex);
		}
		return issaved;
	
	}

	@Override
	public Integer togetusrvalue(String societyid, String flatno) {
		Integer idval=0;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    String qry="";	
	    Query query = null;
	    FlatMasterTblVO flatMgmt=new FlatMasterTblVO();
	    try {
	    	session = HibernateUtil.getSession();
	    	tx=session.beginTransaction();
	/*    	qry = "select usrid from FlatMasterTblVO where  flatno='"+flatno.trim()+"' and "
	    		  +"usrid.societyId.societyUniqyeId='"+societyid+"' and status=1";	 */
	    	qry="SELECT user_flt.usr_id FROM society_mst_tbl scty LEFT JOIN usr_reg_tbl user ON scty.society_id = user.society_id LEFT JOIN usr_flat_detail_tbl user_flt ON user_flt.usr_id = user.usr_id WHERE user_flt.faltno = '"+flatno+"' AND scty.`SOCIETY_UNIQUE_ID` = '"+societyid+"'";
	    	query = session.createSQLQuery(qry).addScalar("USR_ID", Hibernate.INTEGER);
	    	query.setMaxResults(1);
	    	idval = (Integer) query.uniqueResult();
	    	if(idval!=null){
	    	}else{
	    		idval=0;
	    	}
	    } catch (HibernateException ex) {
	    	Commonutility.toWriteConsole("Exception Found checkSocietyMstTbl()  : "+ex);
	    	 return idval;
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}query = null;
			 
	    }
	    return idval;
	
	}

}
