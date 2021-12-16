package com.pack.committeelistvo.persistance;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;

public interface CommitteeroleDao {
	public boolean toDeactivatecmtroletype(String locUpdQry);
	public int toInsertcmtroletype(CommittteeRoleMstTbl cmtroletypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistcommitteelistname(String lvrcategorytypetitle);
	
}