package com.socialindiaservices.services;

import java.util.List;

import org.hibernate.Session;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.socialindiaservices.persistence.DocumentUtilitiDao;
import com.socialindiaservices.persistence.DocumentUtilitiHibernateDao;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.MaintenanceFileUploadTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class DocumentUtilitiDaoServices implements DocumentUtilitiServices{
	private DocumentUtilitiDao dao;
	
	public DocumentUtilitiDaoServices(){
		dao=new DocumentUtilitiHibernateDao();
	}

	@Override
	public boolean insertDocumentManageTbl(DocumentManageTblVO documentmng,Session session) {
		// TODO Auto-generated method stub
		return dao.insertDocumentManageTbl(documentmng,session);
	}

	@Override
	public boolean insertDocumentShareTbl(DocumentShareTblVO documentShare,Session session) {
		// TODO Auto-generated method stub
		return dao.insertDocumentShareTbl(documentShare,session);
	}

	@Override
	public boolean insertNotificationTbl(NotificationTblVO notificationobj,Session session) {
		// TODO Auto-generated method stub
		return dao.insertNotificationTbl(notificationobj,session);
	}

	@Override
	public boolean insertMaintenanceFeeTbl(MaintenanceFeeTblVO maintanencefee,Session session) {
		// TODO Auto-generated method stub
		return dao.insertMaintenanceFeeTbl(maintanencefee,session);
	}

	@Override
	public List getdocumentmanagegroupBy(String columname) {
		// TODO Auto-generated method stub
		return dao.getdocumentmanagegroupBy(columname);
	}

	@Override
	public List getdocumentshareGroupByQry(String qry) {
		// TODO Auto-generated method stub
		return dao.getdocumentshareGroupByQry(qry);
	}

	@Override
	public boolean deleteDocumentManageTbl(Integer documentId, Integer docSubFolder,Integer ivrEntryByusrid, String lvrShareusrid) {
		// TODO Auto-generated method stub
		return dao.deleteDocumentManageTbl(documentId, docSubFolder,ivrEntryByusrid, lvrShareusrid);
	}

	@Override
	public UserMasterTblVo getUserDetailByMobileNo(String mobilno) {
		// TODO Auto-generated method stub
		return dao.getUserDetailByMobileNo(mobilno);
	}

	@Override
	public DocumentManageTblVO getdocumentmanagebydocId(
			DocumentManageTblVO documentmng) {
		// TODO Auto-generated method stub
		return dao.getdocumentmanagebydocId(documentmng);
	}

	@Override
	public boolean updateDocumentManageTbl(DocumentManageTblVO documentmng,Session session) {
		// TODO Auto-generated method stub
		return dao.updateDocumentManageTbl(documentmng,session);
	}

	@Override
	public List getdocumentsharetblByDocId(String qry) {
		// TODO Auto-generated method stub
		return dao.getdocumentsharetblByDocId(qry);
	}

	@Override
	public boolean editUpdateMaintenanceFeeTbl(
			MaintenanceFeeTblVO maintanencefee,Session session) {
		// TODO Auto-generated method stub
		return dao.editUpdateMaintenanceFeeTbl(maintanencefee,session);
	}

	@Override
	public boolean updateMtDocumentManageTbl(DocumentManageTblVO documentmng,Session session) {
		// TODO Auto-generated method stub
		return dao.updateMtDocumentManageTbl(documentmng,session);
	}

	@Override
	public DocumentShareTblVO getdocumentsharetblByQuery(String qry) {
		// TODO Auto-generated method stub
		return dao.getdocumentsharetblByQuery(qry);
	}

	@Override
	public boolean insertNotificationTblByValue(int userid, String desc,int entryBy, int pTblflg, int pTbrowid) {
		// TODO Auto-generated method stub
		return dao.insertNotificationTblByValue(userid, desc,entryBy, pTblflg, pTbrowid);
	}

	@Override
	public UserMasterTblVo getUserDetailByquery(String qry) {
		// TODO Auto-generated method stub
		return dao.getUserDetailByquery(qry);
	}

	@Override
	public boolean insertIntoDocumentManageTbl(DocumentManageTblVO documentmng) {
		// TODO Auto-generated method stub
		return dao.insertIntoDocumentManageTbl(documentmng);
	}

	@Override
	public UserMasterTblVo getUserDetailBydetails(int socetyid,
			String xlmobileno, String xlflatno) {
		// TODO Auto-generated method stub
		return dao.getUserDetailBydetails(socetyid,xlmobileno,xlflatno);
	}
	@Override
	public int insertMaintenanceFilesTbl(MaintenanceFileUploadTblVO maintanencefiles,Session session) {
		// TODO Auto-generated method stub
		return dao.insertMaintenanceFilesTbl(maintanencefiles,session);
	}
	@Override
	public boolean updatemaintenancefileTbl(String  qry, Session session) {
		// TODO Auto-generated method stub
		return dao.updatemaintenancefileTbl(qry, session);
	}



}
