package com.socialindiaservices.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.MaintenanceFileUploadTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class DocumentUtilitiHibernateDao implements DocumentUtilitiDao{
	
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");

	@Override
	public boolean insertDocumentManageTbl(DocumentManageTblVO documentmng,Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(documentmng);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========insertDocumentManageTbl Insert================"+ex);
		}
		return issaved;
	}

	@Override
	public boolean insertDocumentShareTbl(DocumentShareTblVO documentShare,Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			String sql="select count(*) FROM DocumentShareTblVO where documentId.documentId=:DOCUMENT_ID and userId.userId=:USER_ID";
			 Query qy = session.createQuery(sql);
			 qy.setInteger("DOCUMENT_ID", documentShare.getDocumentId().getDocumentId());
			 qy.setInteger("USER_ID", documentShare.getUserId().getUserId());
			 long count=(long) qy.uniqueResult();
			 System.out.println("count-----------"+count);
			 if(count==0){
				 session.save(documentShare);
				 issaved=true;
			 }else{
				 issaved=false;
			 }
			
		}catch(HibernateException ex){
			issaved=false;
			System.out.println("===========insertDocumentShareTbl Insert================"+ex);
		}
		return issaved;
	}

	@Override
	public boolean insertNotificationTbl(NotificationTblVO notificationobj,Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(notificationobj);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========insertDocumentShareTbl Insert================"+ex);
		}
		return issaved;
	}

	@Override
	public boolean insertMaintenanceFeeTbl(MaintenanceFeeTblVO maintanencefee,Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(maintanencefee);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========insertMaintenanceFeeTbl Insert================"+ex);
		}
		return issaved;
	}

	@Override
	public List getdocumentmanagegroupBy(String qry) {
		// TODO Auto-generated method stub
		Session session = null;
		List<DocumentManageTblVO> documentmngList=new ArrayList<DocumentManageTblVO>();
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			//String qry="FROM DocumentManageTblVO group by "+columname+"";
			 Query qy = session.createQuery(qry);
			 documentmngList = qy.list();
		}catch(HibernateException ex){
			System.out.println("===========insertMaintenanceFeeTbl Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return documentmngList;
	}

	@Override
	public List getdocumentshareGroupByQry(String qry) {
		// TODO Auto-generated method stub
		Session session = null;
		List<DocumentManageTblVO> documentmngList=new ArrayList<DocumentManageTblVO>();
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			//String qry="FROM DocumentManageTblVO group by "+columname+"";
			 Query qy = session.createQuery(qry);
			 documentmngList = qy.list();
		}catch(HibernateException ex){
			System.out.println("===========insertMaintenanceFeeTbl Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return documentmngList;
	}

	@Override
	public boolean deleteDocumentManageTbl(Integer documentId,Integer docSubFolder,Integer ivrEntryByusrid, String lvrShareusrid) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		boolean issaved=false;
		CommonUtils common=new CommonUtils();
		DocumentManageTblVO documentmng=new DocumentManageTblVO();
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			String sql="FROM DocumentManageTblVO where documentId=:DOCUMENT_ID";
			 Query qy = session.createQuery(sql);
			 qy.setInteger("DOCUMENT_ID", documentId);
			 documentmng=(DocumentManageTblVO) qy.uniqueResult();
			 Date dat=new Date();
			if(docSubFolder!=null && docSubFolder==1){
				String qry="update MaintenanceFeeTblVO set statusFlag=0,modifyDatetime=:MODIFY_TIME where maintenanceId=:MAINTENANCE_ID";
				Query upq=session.createQuery(qry);
				upq.setTimestamp("MODIFY_TIME", dat);
				upq.setInteger("MAINTENANCE_ID", documentmng.getMaintenanceId().getMaintenanceId());
				upq.executeUpdate();
			}
			String qry1="update DocumentManageTblVO set statusFlag=0,modifyDatetime=:MODIFY_TIME where documentId=:DOCUMENT_ID";
			Query upq1=session.createQuery(qry1);
			upq1.setTimestamp("MODIFY_TIME", dat);
			upq1.setInteger("DOCUMENT_ID", documentId);
			upq1.executeUpdate();
			System.out.println("qry1--------"+upq1.toString());
			Commonutility.toWriteConsole("lvrShareusrid ::::::::::"+lvrShareusrid);
			if(documentmng!=null && documentmng.getDocFolder()==2){
				String qry2="update DocumentShareTblVO set status=0, modifyDatetime=:MODIFY_TIME where documentId.documentId=:DOCUMENT_ID and userId.userId=:SHAREDUSERID";
				Query upq2=session.createQuery(qry2);
				upq2.setTimestamp("MODIFY_TIME", dat);
				upq2.setInteger("DOCUMENT_ID", documentId);
				Commonutility.toWriteConsole(lvrShareusrid);
				if(lvrShareusrid!=null && Commonutility.checkempty(lvrShareusrid)){
					upq2.setInteger("SHAREDUSERID", Commonutility.stringToInteger(lvrShareusrid));
				} else {					
				}
				
				upq2.executeUpdate();
				String todelfolder=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.generaldoc.fldr")+rb.getString("external.inner.webpath")+lvrShareusrid+"/"+documentmng.getDocDateFolderName();
				String todelfile=documentmng.getDocFileName();
				common.deleteIfFileExist(todelfolder, todelfile);
			}
			
			tx.commit();
			issaved=true;
		}catch(HibernateException ex){
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("===========insertDocumentManageTbl Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return issaved;
	}

	@Override
	public UserMasterTblVo getUserDetailByMobileNo(String mobilno) {
		// TODO Auto-generated method stub
		UserMasterTblVo userdtil=new UserMasterTblVo();
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			String qry="FROM UserMasterTblVo where mobileNo=:MOBILE_NO";
			 Query qy = session.createQuery(qry);
			 qy.setString("MOBILE_NO", mobilno);
			 qy.setMaxResults(1);
			 userdtil = (UserMasterTblVo) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getUserDetailByMobileNo Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return userdtil;
	}

	@Override
	public DocumentManageTblVO getdocumentmanagebydocId(
			DocumentManageTblVO documentmng) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			String qry="FROM DocumentManageTblVO where documentId=:DOCUMENT_ID";
			 Query qy = session.createQuery(qry);
			 qy.setInteger("DOCUMENT_ID", documentmng.getDocumentId());
			 qy.setMaxResults(1);
			 documentmng = (DocumentManageTblVO) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getUserDetailByMobileNo Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return documentmng;
	}

	@Override
	public boolean updateDocumentManageTbl(DocumentManageTblVO documentmng,Session session) {
		// TODO Auto-generated method stub
		boolean isupdate=false;
		try{
			Date dat=new Date();			
			String docfilename=documentmng.getDocFileName();
			if(docfilename!=null && docfilename.length()>0){
			String qry="update DocumentManageTblVO set docTypId.ivrBnDOC_TYP_ID=:DOCUMENT_TYPE,docFileName=:DOC_FILE_NAME,subject=:SUBJECT,descr=:DESCRIPTION,emailStatus=:EMAIL_STATUS,modifyDatetime=:MODIFY_TIME where documentId=:DOCUMENT_ID";
			Query upq=session.createQuery(qry);
			upq.setInteger("DOCUMENT_TYPE", documentmng.getDocTypId().getIvrBnDOC_TYP_ID());
			upq.setString("DOC_FILE_NAME", documentmng.getDocFileName());
			upq.setString("SUBJECT", documentmng.getSubject());
			upq.setString("DESCRIPTION",documentmng.getDescr());
			upq.setInteger("EMAIL_STATUS", documentmng.getEmailStatus());
			upq.setTimestamp("MODIFY_TIME", dat);
			upq.setInteger("DOCUMENT_ID", documentmng.getDocumentId());
			upq.executeUpdate();
			}else{
				String qry="update DocumentManageTblVO set docTypId.ivrBnDOC_TYP_ID=:DOCUMENT_TYPE,subject=:SUBJECT,descr=:DESCRIPTION,emailStatus=:EMAIL_STATUS,modifyDatetime=:MODIFY_TIME where documentId=:DOCUMENT_ID";
				Query upq=session.createQuery(qry);
				upq.setInteger("DOCUMENT_TYPE", documentmng.getDocTypId().getIvrBnDOC_TYP_ID());
				//upq.setString("DOC_FILE_NAME", documentmng.getDocFileName());
				upq.setString("SUBJECT", documentmng.getSubject());
				upq.setString("DESCRIPTION",documentmng.getDescr());
				upq.setInteger("EMAIL_STATUS", documentmng.getEmailStatus());
				upq.setTimestamp("MODIFY_TIME", dat);
				upq.setInteger("DOCUMENT_ID", documentmng.getDocumentId());
				upq.executeUpdate();
			}
			isupdate=true;
		}catch(HibernateException ex){
			isupdate=false;
			System.out.println("===========getUserDetailByMobileNo Insert================"+ex);
		}
		return isupdate;
	}

	@Override
	public List getdocumentsharetblByDocId(String qry) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		List<DocumentShareTblVO> documentshareList=new ArrayList<DocumentShareTblVO>();
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 documentshareList = qy.list();
		}catch(HibernateException ex){
			System.out.println("===========insertMaintenanceFeeTbl Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return documentshareList;
	}

	@Override
	public boolean editUpdateMaintenanceFeeTbl( MaintenanceFeeTblVO maintanencefee, Session session) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		try{
			 Date dat=new Date();
			System.out.println("getMaintenanceId-------------"+ maintanencefee.getMaintenanceId());
			String qry="update MaintenanceFeeTblVO set serviceCharge=:SERVICE_CHARGE,billDate=:BILL_DATE,municipalTax=:MUNICIPAL_TAX,penalty=:PENALTY,waterCharge=:WATER_CHARGE,"
					+ "nonOccupancyCharge=:NON_OCCUPANCY_CHARGE,culturalCharge=:CULTURAL_CHARGE,sinkingFund=:SINKING_FUND,repairFund=:REPAIR_FUND,insuranceCharges=:INSURANCE_CHARGE,"
					+ "playZoneCharge=:PLAY_ZONE,majorRepairFund=:MAJOR_REPAIR_FUND,interest=:INTEREST,totbillamt=:TOTAL_AMOUNT,modifyDatetime=:MODIFY_TIME where maintenanceId=:MAINTENANCE_ID";
			tx = session.beginTransaction();
			Query upq = session.createQuery(qry);
			upq.setFloat("SERVICE_CHARGE", maintanencefee.getServiceCharge());
			upq.setDate("BILL_DATE", maintanencefee.getBillDate());
			upq.setFloat("MUNICIPAL_TAX", maintanencefee.getMunicipalTax());
			upq.setFloat("PENALTY",maintanencefee.getPenalty());
			upq.setFloat("WATER_CHARGE", maintanencefee.getWaterCharge());
			upq.setFloat("NON_OCCUPANCY_CHARGE", maintanencefee.getNonOccupancyCharge());
			upq.setFloat("CULTURAL_CHARGE", maintanencefee.getCulturalCharge());
			upq.setFloat("SINKING_FUND", maintanencefee.getSinkingFund());
			upq.setFloat("REPAIR_FUND", maintanencefee.getRepairFund());
			upq.setFloat("INSURANCE_CHARGE", maintanencefee.getInsuranceCharges());
			upq.setFloat("PLAY_ZONE", maintanencefee.getPlayZoneCharge());
			upq.setFloat("MAJOR_REPAIR_FUND", maintanencefee.getMajorRepairFund());
			upq.setFloat("INTEREST", maintanencefee.getInterest());
			upq.setFloat("TOTAL_AMOUNT", maintanencefee.getTotbillamt());
			upq.setTimestamp("MODIFY_TIME", dat);
			upq.setInteger("MAINTENANCE_ID", maintanencefee.getMaintenanceId());
			upq.executeUpdate();
			tx.commit();
		}catch(HibernateException ex){
			System.out.println("===========getUserDetailByMobileNo Insert================"+ex);
		}
		return true;
	}

	@Override
	public boolean updateMtDocumentManageTbl(DocumentManageTblVO documentmng,Session session) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		try{
			 Date dat=new Date();
			System.out.println("getDocumentId-------------"+ documentmng.getDocumentId());
			String qry="update DocumentManageTblVO set docDateFolderName=:FOLDER_DATE,docFileName=:DOC_FILE_NAME,emailStatus=:EMAIL_STATUS,modifyDatetime=:MODIFY_TIME where documentId=:DOCUMENT_ID";
			tx = session.beginTransaction();
			Query upq=session.createQuery(qry);
			upq.setString("FOLDER_DATE", documentmng.getDocDateFolderName());
			upq.setString("DOC_FILE_NAME", documentmng.getDocFileName());
			upq.setInteger("EMAIL_STATUS", documentmng.getEmailStatus());
			upq.setTimestamp("MODIFY_TIME", dat);
			upq.setInteger("DOCUMENT_ID", documentmng.getDocumentId());
			upq.executeUpdate();
			tx.commit();
		}catch(HibernateException ex){
			System.out.println("===========getUserDetailByMobileNo Insert================"+ex);
		}
		return true;
	}

	@Override
	public DocumentShareTblVO getdocumentsharetblByQuery(String qry) {
		// TODO Auto-generated method stub
		Session session = null;
		DocumentShareTblVO documentshare=new DocumentShareTblVO();
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 documentshare= (DocumentShareTblVO) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getdocumentsharetblByQuery Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return documentshare;
	}

	@Override
	public boolean insertNotificationTblByValue(int userid, String desc,int entryBy, int pTblflg, int pTbrowid) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		Session session = null;
		try{
			System.out.println("Notification insert to table **************** ");
			session = HibernateUtil.getSessionFactory().openSession();
			NotificationTblVO notificationobj=new NotificationTblVO();
			UserMasterTblVo userobj=new UserMasterTblVo();
			UserMasterTblVo entryobj=new UserMasterTblVo();
			CommonUtils common=new CommonUtils();
			String randnumber=common.randomString(Integer.parseInt(rb.getString("Notification.random.string.length")));
			Date curDate=new Date();
			userobj.setUserId(userid);
			entryobj.setUserId(entryBy);
			notificationobj.setUserId(userobj);
			notificationobj.setReadStatus(Integer.parseInt(rb.getString("Initial.read.Status")));
			notificationobj.setSentStatus(Integer.parseInt(rb.getString("Initial.sent.Status")));
			notificationobj.setStatusFlag(Integer.parseInt(rb.getString("Initial.statusflag")));
			notificationobj.setDescr(desc);		
			notificationobj.setEntryBy(entryobj);
			notificationobj.setEntryDatetime(curDate);
			notificationobj.setTblrefFlag(pTblflg);
			notificationobj.setTblrefID(pTbrowid);
			session.save(notificationobj);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========insertDocumentShareTbl Insert================"+ex);
		}finally{
			if(session!=null){session.close();session = null;}
		}
		return issaved;
	}

	@Override
	public UserMasterTblVo getUserDetailByquery(String qry) {
		// TODO Auto-generated method stub
		UserMasterTblVo userdtil=new UserMasterTblVo();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			 Query qy = session.createQuery(qry);
			 qy.setMaxResults(1);
			 userdtil = (UserMasterTblVo) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getUserDetailByMobileNo Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return userdtil;
	}

	@Override
	public boolean insertIntoDocumentManageTbl(DocumentManageTblVO documentmng) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		boolean issaved=false;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			System.out.println("documentmng"+documentmng.getDocTypId().getIvrBnDOC_TYP_ID());
			session.save(documentmng);
			tx.commit();
			issaved=true;
		}catch(HibernateException ex){
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("===========insertDocumentManageTbl Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return issaved;
	}
	@Override
	public UserMasterTblVo getUserDetailBydetails(int id,String mobilno,String flatno) {
		// TODO Auto-generated method stub
		UserMasterTblVo userdtil=null;
		Session session = null;
		Transaction tx = null;
		try{
			userdtil=new UserMasterTblVo();
			session = HibernateUtil.getSessionFactory().openSession();
			String qry="FROM UserMasterTblVo where socialId.societyId=:SOCIETY_ID and mobileNo=:MOBILE_NO and flatNo=:FLAT_NO";
			 Query qy = session.createQuery(qry);
			 qy.setInteger("SOCIETY_ID", id);
			 qy.setString("MOBILE_NO", mobilno);
			 qy.setString("FLAT_NO", flatno);
			 qy.setMaxResults(1);
			 userdtil = (UserMasterTblVo) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getUserDetailByMobileNo Insert================"+ex);
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return userdtil;
	}

	@Override
	public int insertMaintenanceFilesTbl(MaintenanceFileUploadTblVO maintanencefiles, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		int id=0;
		try{
			session.save(maintanencefiles);
			id=maintanencefiles.getFileUpId();
			System.out.println("===========id id================"+id);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========MaintenanceFileUploadTblVO Insert================"+ex);
			id=0;
			return id;
		}
		return id;
	
	}

	@Override
	public boolean updatemaintenancefileTbl(String qry, Session session) {
		boolean locRtn= false;
//		Session session = null;
//		Transaction tx = null;
		Query lvrQryobj = null;
		System.out.println("sql------------------------------- "+qry);
		try {
//			session = HibernateUtil.getSession();
//			tx = session.beginTransaction();
			lvrQryobj = session.createQuery(qry);
			lvrQryobj.executeUpdate();
//			tx.commit();
			locRtn= true;				
		} catch (HibernateException e) {
//			if (tx != null) {
//				tx.rollback();
//			}
			Commonutility.toWriteConsole("HibernateException Common Update Query : "+e);
			locRtn= false;				
		} finally {
//			if(session!=null){session.flush();session.clear();session.close();session=null;}
			lvrQryobj = null;
		}		
		return locRtn;
}

}
