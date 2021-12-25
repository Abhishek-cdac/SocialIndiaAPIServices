package com.socialindiaservices.persistence;

import java.util.List;

import com.siservices.uam.persistense.GroupMasterTblVo;

public interface ReportDao {
	public List selectMerchantPdf(String qry);
	public List selectstaffPdf(String qry);
	public GroupMasterTblVo selectGroupByQry(String qry);
	public List selectuserMstPdf(String qry);
	public List selectTownshipMgmtByQry(String qry);
	public List selectDocumentPdf(String qry);
	public List selectReconicile(String qry);
	
}
