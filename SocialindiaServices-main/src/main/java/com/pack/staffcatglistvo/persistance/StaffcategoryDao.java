package com.pack.staffcatglistvo.persistance;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;

public interface StaffcategoryDao {
	public boolean toDeactivatecategorytype(String locUpdQry);
	public int toInsertcategorytype(StaffCategoryMasterTblVO categorytypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExiststaffCategoryname(String lvrcategorytypetitle);
	
}