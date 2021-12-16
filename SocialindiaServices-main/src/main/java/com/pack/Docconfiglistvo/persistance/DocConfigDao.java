package com.pack.Docconfiglistvo.persistance;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;

public interface DocConfigDao {
	public boolean toDeactivatedoctypetype(String locUpdQry);
	public int toInsertdoctypetype(DoctypMasterTblVO doctypetypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistDocumenttypelistname(String lvrdoctypetypetitle);
	
}