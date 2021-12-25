package com.socialindiaservices.services;

import java.util.List;

import org.hibernate.Session;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.MaintenanceFileUploadTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public interface DocumentUtilitiServices {
	boolean insertDocumentManageTbl(DocumentManageTblVO documentmng,Session session);
	boolean insertDocumentShareTbl(DocumentShareTblVO documentShare,Session session);
	boolean insertNotificationTbl(NotificationTblVO notificationobj,Session session);
	boolean insertMaintenanceFeeTbl(MaintenanceFeeTblVO maintanencefee,Session session);
	List getdocumentmanagegroupBy(String columname);
	List getdocumentshareGroupByQry(String qry);
	boolean deleteDocumentManageTbl(Integer documentId,Integer docSubFolder,Integer ivrEntryByusrid, String lvrshareUsrid);
	
	UserMasterTblVo getUserDetailByMobileNo(String mobilno);
	UserMasterTblVo getUserDetailByquery(String qry);
	
	DocumentManageTblVO getdocumentmanagebydocId(DocumentManageTblVO documentmng);
	boolean updateDocumentManageTbl(DocumentManageTblVO documentmng,Session session);
	List getdocumentsharetblByDocId(String qry);
	boolean editUpdateMaintenanceFeeTbl(MaintenanceFeeTblVO maintanencefee,Session session);
	
	boolean updateMtDocumentManageTbl(DocumentManageTblVO documentmng,Session session);
	DocumentShareTblVO getdocumentsharetblByQuery(String qry);
	boolean insertNotificationTblByValue(int userid,String desc,int entryBy, int tblflg, int tblrowid);
	
	boolean insertIntoDocumentManageTbl(DocumentManageTblVO documentmng);
	UserMasterTblVo getUserDetailBydetails(int socetyid, String xlmobileno,
			String xlflatno);
	int insertMaintenanceFilesTbl(MaintenanceFileUploadTblVO maintanencefiles,Session session);
	boolean updatemaintenancefileTbl (String qry, Session session);

}
