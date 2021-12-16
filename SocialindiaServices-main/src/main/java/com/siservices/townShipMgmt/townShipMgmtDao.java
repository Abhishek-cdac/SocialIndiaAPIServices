package com.siservices.townShipMgmt;

import java.util.List;

import com.mobi.contactusreportissuevo.ReportIssueTblVO;
import com.pack.residentvo.UsrUpldfrmExceFailedTbl;
import com.siservices.uam.persistense.TownshipMstTbl;


public interface townShipMgmtDao {
	
	 public int getInitTotal(String sqlQuery);
	 
	 public int gettotalcount(String sql);
	 
	 public int getTotalFilter(String sqlQueryFilter);
	 
	 public List<TownshipMstTbl> getTownShipDetailList();
	 
	 public List<TownshipMstTbl> getTownShipDetailListPagenation(String qry,int prmStrow, int prmTotrow);
	 
	 public int townShipInfoSave(TownshipMstTbl township);
	 
	 public int townShipEditUpdate(TownshipMstTbl township,int townuniqIdold,String countryCode,String stateCode,String cityCode,String postalCode);
	 
	 public TownshipMstTbl getTownShipDetailView(String townshipuniId);
	 
	 public boolean generateNewTownshipPassword(int townshipuniId,TownshipMstTbl towndata,String activeKey);
	 public List<TownshipMstTbl> getTownShipDetailListByQry(String qry,int startrowfrom,int totalNorow);
	 
	 public List<UsrUpldfrmExceFailedTbl> getUploaderrorDetailListByQry(String qry,int startrowfrom,int totalNorow);

	List<ReportIssueTblVO> getReportissueDetailListByQry(String qry,
			int startrowfrom, int totalNorow);
}
