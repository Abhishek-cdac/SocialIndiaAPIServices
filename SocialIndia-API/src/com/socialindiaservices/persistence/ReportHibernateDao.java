package com.socialindiaservices.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pack.reconcilevo.ReconicileresultTblVo;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.sisocial.load.HibernateUtil;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;
import com.socialindiaservices.vo.MaintenanceFileUploadTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class ReportHibernateDao implements ReportDao{

	@Override
	public List selectMerchantPdf(String qry) {
		// TODO Auto-generated method stub
		List<MerchantTblVO> merchantList=new ArrayList<MerchantTblVO>();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 merchantList = qy.list();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
		}
		return merchantList;
	}

	@Override
	public List selectstaffPdf(String qry) {
		// TODO Auto-generated method stub
		List<StaffMasterTblVo>staffList=new ArrayList<StaffMasterTblVo>();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 staffList = qy.list();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
		}
		return staffList;
	}

	@Override
	public GroupMasterTblVo selectGroupByQry(String qry) {
		// TODO Auto-generated method stub
		GroupMasterTblVo groupobj=new GroupMasterTblVo();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 qy.setMaxResults(1);
			 groupobj = (GroupMasterTblVo) qy.uniqueResult();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
		}
		return groupobj;
	}

	@Override
	public List selectuserMstPdf(String qry) {
		// TODO Auto-generated method stub
		List<UserMasterTblVo> userList=new ArrayList<UserMasterTblVo>();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 userList = qy.list();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
		}
		return userList;
	}

	@Override
	public List selectTownshipMgmtByQry(String qry) {
		// TODO Auto-generated method stub
		List<TownshipMstTbl> townshipList=new ArrayList<TownshipMstTbl>();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 townshipList = qy.list();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
		}
		return townshipList;
	}

	@Override
	public List selectDocumentPdf(String qry) {
		// TODO Auto-generated method stub
		List<MaintenanceFileUploadTblVO> townshipList=null;
		Session session = null;
		try{
			townshipList=new ArrayList<MaintenanceFileUploadTblVO>();
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 townshipList = qy.list();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
		}
		return townshipList;
	}

	@Override
	public List selectReconicile(String qry) {

		// TODO Auto-generated method stub
		List<ReconicileresultTblVo> reconicileresult=null;
		Session session = null;
		try{
			reconicileresult=new ArrayList<ReconicileresultTblVo>();
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 reconicileresult = qy.list();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			if(session!=null){
				session.flush();session.clear();session.close();session = null;
			}
		}
		return reconicileresult;
	
	}

}
