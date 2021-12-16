package com.mobi.easypayvo.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.mobi.easypayvo.MvpPaygateTbl;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.MaintenanceBillTaxDtlsTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.MvpDonationAttachTblVo;

public class MaintenanceFeeDaoServices implements MaintenanceFeeDao {
	Log log = new Log();

	@Override
	public List<MaintenanceFeeTblVO> maintenanceBillSearchList(String searchQury, String startlimit, String totalrow,String locTimeStamp) {
		// TODO Auto-generated method stub
		List<MaintenanceFeeTblVO> maintenanceList = new ArrayList<MaintenanceFeeTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			System.out.println("Qry---------->>from MaintenanceFeeTblVO where statusFlag=:STATUS_FLAG " + searchQury + " and entryDatetime <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') order by modifyDatetime desc");
			Query qy = session.createQuery("from MaintenanceFeeTblVO where statusFlag=:STATUS_FLAG " + searchQury + " and entryDatetime <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') order by modifyDatetime desc");
			qy.setInteger("STATUS_FLAG", 1);
			qy.setFirstResult(Integer.parseInt(startlimit));
			if(totalrow!=null && !totalrow.equalsIgnoreCase("0")){
			qy.setMaxResults(Integer.parseInt(totalrow));
			}
			maintenanceList = qy.list();
			System.out.println("maintenanceList size======" + maintenanceList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("maintenanceBillSearchList======" + ex);
			log.logMessage("Exception in maintenanceBillSearchList : "+ ex, "error", MaintenanceFeeDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return maintenanceList;
	}

	@Override
	public List<MaintenanceBillTaxDtlsTblVO> getMNTNCBillTaxDetails() {
		List<MaintenanceBillTaxDtlsTblVO>  billTaxDtlsTblList = null;
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery("From MaintenanceBillTaxDtlsTblVO");
			billTaxDtlsTblList = qy.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("getPaygateDetails======" + ex);
			log.logMessage("Exception getMNTNCBillTaxDetails : "+ex, "error", EasyPaymentDaoServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return billTaxDtlsTblList;
	}
	
	@Override
	public List<DocumentManageTblVO> documnetAttachList(String maintenanceId,int rid) {
		// TODO Auto-generated method stub
		List<DocumentManageTblVO> attachList = new ArrayList<DocumentManageTblVO>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		int maintenId = 0;
		int userId = 0;
		try {
			qry = "from DocumentManageTblVO where maintenanceId.maintenanceId=:MAINTENANCE_ID and userId.userId=:USR_ID and statusFlag=:STATUS";
			Query qy = session.createQuery(qry);
			if (Commonutility.checkempty(maintenanceId)){
				maintenId = Integer.parseInt(maintenanceId);
			} 
			if (Commonutility.checkIntempty(rid)){
				userId = rid;
			}
			qy.setInteger("MAINTENANCE_ID", maintenId);
			qy.setInteger("USR_ID", userId);
			qy.setInteger("STATUS", 1);
			attachList = qy.list();
			System.out.println("attachList=>"+attachList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("DocumentManageTblVO documnetAttachList======" + ex);
			log.logMessage("Exception documnetAttachList: "+ ex, "error", MaintenanceFeeDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return attachList;
	}
}
