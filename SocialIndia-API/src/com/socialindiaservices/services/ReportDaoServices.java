package com.socialindiaservices.services;

import java.util.List;

import com.siservices.uam.persistense.GroupMasterTblVo;
import com.socialindiaservices.persistence.ReportDao;
import com.socialindiaservices.persistence.ReportHibernateDao;

public class ReportDaoServices implements ReportServices{
	private ReportDao dao;

	public ReportDaoServices(){
		dao=new ReportHibernateDao();
	}
	
	@Override
	public List selectMerchantPdf(String qry) {
		// TODO Auto-generated method stub
		return dao.selectMerchantPdf(qry);
	}

	@Override
	public List selectstaffPdf(String qry) {
		// TODO Auto-generated method stub
		return dao.selectstaffPdf(qry);
	}

	@Override
	public GroupMasterTblVo selectGroupByQry(String qry) {
		// TODO Auto-generated method stub
		return dao.selectGroupByQry(qry);
	}

	@Override
	public List selectuserMstPdf(String qry) {
		// TODO Auto-generated method stub
		return dao.selectuserMstPdf(qry);
	}

	@Override
	public List selectTownshipMgmtByQry(String qry) {
		// TODO Auto-generated method stub
		return dao.selectTownshipMgmtByQry(qry);
	}

	@Override
	public List selectDocumentPdf(String qry) {
		// TODO Auto-generated method stub
		return dao.selectDocumentPdf(qry);
	}
	public List selectReconicile(String qry) {
		// TODO Auto-generated method stub
		return dao.selectReconicile(qry);
	}
	
}
