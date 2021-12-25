package com.pack.merchantCategorylistvo.persistance;


import com.socialindiaservices.vo.MerchantCategoryTblVO;

public interface merchantCategoryDao {
	public boolean toDeactivatemerchantCategory(String locUpdQry);
	public int toInsertmerchantCategory(MerchantCategoryTblVO merchantCategoryTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistmerchantCategorylistname(String lvrmerchantCategorytitle);
	
}