package com.mobile.infolibrary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobile.familymember.familyMemberDaoServices;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.DocumentManageTblVO;

public class InfoLibraryDaoServices implements InfoLibraryDao{
	Log log = new Log();

	@Override
	public List getDocumentList(String qry, String startlim, String totalrow ,Integer societyId) {
		// TODO Auto-generated method stub
		//List<DocumentManageTblVO> documentList = new ArrayList<DocumentManageTblVO>();
		List documentList = new ArrayList<>();
		Session session = HibernateUtil.getSession();
		try {
			
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			documentList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getDocumentList======" + ex);
			log.logMessage("InfoLibraryDaoServices Exception getDocumentList : "
							+ ex, "error", InfoLibraryDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return documentList;
	}

	@Override
	public boolean deleteInfoDocument(String userId, String docId) {
		// TODO Auto-generated method stub
		boolean result = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update DocumentShareTblVO set status=:STATUS "
							+ "  where userId.userId=:USER_ID and  documentId=:DOC_ID");
			qy.setInteger("STATUS", 0);
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("DOC_ID", Integer.parseInt(docId));
			int isupdated=qy.executeUpdate();
			tx.commit();
			System.out.println("isupdated==========="+isupdated);
			if(isupdated==0){
				return false;
			}else{
				result = true;
			}
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("profileDaoServices Exception getUserPrfileList : "
					+ ex, "error", familyMemberDaoServices.class);
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		}
		return result;
	}

	@Override
	public List getDocumentimageList(String qry) {
		// TODO Auto-generated method stub
		List<DocumentManageTblVO> documentList = new ArrayList<DocumentManageTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			System.out.println("qy-----------------"+qy);
			documentList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getDocumentList======" + ex);
			log.logMessage("InfoLibraryDaoServices Exception get Documentimage List : "
							+ ex, "error", InfoLibraryDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return documentList;
	}

}
