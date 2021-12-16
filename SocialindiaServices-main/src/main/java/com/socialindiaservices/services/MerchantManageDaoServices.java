package com.socialindiaservices.services;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.socialindiaservices.persistence.MerchantManageDao;
import com.socialindiaservices.persistence.MerchantManageHibernateDao;
import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantStockDetailTblVO;
import com.socialindiaservices.vo.MerchantTblVO;
import com.socialindiaservices.vo.MerchantUtilitiImageTblVO;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public class MerchantManageDaoServices implements MerchantManageServices{

	private MerchantManageDao dao;
	public MerchantManageDaoServices(){
		dao=new MerchantManageHibernateDao();
	}
	@Override
	public boolean insertMerchantTbl(MerchantTblVO merchantObj, Session session) {
		// TODO Auto-generated method stub
		return dao.insertMerchantTbl(merchantObj, session);
	}
	@Override
	public boolean insertMerchantStockDetailTbl(
			MerchantStockDetailTblVO merchantstockObj, Session session) {
		// TODO Auto-generated method stub
		return dao.insertMerchantStockDetailTbl(merchantstockObj, session);
	}
	@Override
	public MerchantTblVO getMerchantTblObjByQuery(String qry, Session session) {
		// TODO Auto-generated method stub
		return dao.getMerchantTblObjByQuery(qry, session);
	}
	@Override
	public List getMerchantStockDetailTblObjByQuery(
			String qry, Session session) {
		// TODO Auto-generated method stub
		return dao.getMerchantStockDetailTblObjByQuery(qry, session);
	}
	@Override
	public boolean updateMerchantTbl(MerchantTblVO merchantObj, Session session) {
		// TODO Auto-generated method stub
		return dao.updateMerchantTbl(merchantObj, session);
	}
	@Override
	public boolean deleteStockDetailBymerchantId(MerchantTblVO merchantObj,
			Session session) {
		// TODO Auto-generated method stub
		return dao.deleteStockDetailBymerchantId(merchantObj, session);
	}
	@Override
	public boolean deleteMerchantTblObjByQuery(String qry, Session session) {
		// TODO Auto-generated method stub
		return dao.deleteMerchantTblObjByQuery(qry, session);
	}
	@Override
	public boolean insertMerchantUtilitiTbl(
			MerchantUtilitiTblVO merUtilitiTblObj, Session session) {
		// TODO Auto-generated method stub
		return dao.insertMerchantUtilitiTbl(merUtilitiTblObj, session);
	}
	@Override
	public boolean insertMerchantUtilitiImageTbl(
			MerchantUtilitiImageTblVO merUtilitiTblImageObj, Session session) {
		// TODO Auto-generated method stub
		return dao.insertMerchantUtilitiImageTbl(merUtilitiTblImageObj, session);
	}
	@Override
	public MerchantUtilitiImageTblVO getMerchantImageTblObjByQuery(String qry,
			Session session) {
		// TODO Auto-generated method stub
		return dao.getMerchantImageTblObjByQuery(qry, session);
	}
	@Override
	public MerchantUtilitiTblVO getMerchantUtilitiTblObjByQuery(String qry,
			Session session) {
		// TODO Auto-generated method stub
		return dao.getMerchantUtilitiTblObjByQuery(qry, session);
	}
	@Override
	public boolean updateMerchantUtilitiTbl(
			MerchantUtilitiTblVO merUtilitiTblObj, Session session) {
		// TODO Auto-generated method stub
		return dao.updateMerchantUtilitiTbl(merUtilitiTblObj, session);
	}
	@Override
	public List deleteMerchantUtilitiImageTbl(
			MerchantUtilitiImageTblVO merUtilitiTblImageObj, Session session) {
		// TODO Auto-generated method stub
		return dao.deleteMerchantUtilitiImageTbl(merUtilitiTblImageObj, session);
	}
	@Override
	public boolean deleteMerchantUtilitiTbl(
			MerchantUtilitiTblVO merUtilitiTblObj, Session session) {
		// TODO Auto-generated method stub
		return dao.deleteMerchantUtilitiTbl(merUtilitiTblObj, session);
	}
	@Override
	public boolean insertMerchantissueTbl(MerchantIssueTblVO mechtissuetbl,
			Session session) {
		// TODO Auto-generated method stub
		return dao.insertMerchantissueTbl(mechtissuetbl, session);
	}
	
	
	
}
