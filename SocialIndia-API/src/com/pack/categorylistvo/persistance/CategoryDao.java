package com.pack.categorylistvo.persistance;

import com.pack.commonvo.CategoryMasterTblVO;

public interface CategoryDao {
	
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	
	public int getInitTotal(String sql);

	public int getTotalFilter(String sqlQueryFilter);
	
	public boolean toDeactivatecategorytype(String locUpdQry);

	public int toInsertcategorytype(CategoryMasterTblVO categorytypeTblObj);
	
	public String toExistCategoryname(String lvrcategorytypetitle);

}