package com.social.billmaintenanceDao;

import org.hibernate.Session;

import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.social.billmaintenanceVO.BillmaintenanceVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;


public interface BillMaintenanceDao {
	
	boolean insertbilldata(BillmaintenanceVO maintanencebilldata,Session session);
	boolean toDeleteEvent(String qry);
	String checkDuplicate(String qry);
	boolean checksocietytown(String socty,String townMst);
	boolean toInsertmaintenance(MaintenanceFeeTblVO maintanencefee,Session session);
	Integer togetusrvalue(String societyid,String flatno);
}
