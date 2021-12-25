package com.pack.staffcatglistvo.persistance;

import com.pack.commonvo.StaffCategoryMasterTblVO;

public interface StaffcategoryDao {
	public boolean toDeactivatecategorytype(String locUpdQry);
	public int toInsertcategorytype(StaffCategoryMasterTblVO categorytypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExiststaffCategoryname(String lvrcategorytypetitle);
	
}