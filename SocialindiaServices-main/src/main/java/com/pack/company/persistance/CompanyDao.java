package com.pack.company.persistance;

import com.pack.commonvo.CompanyMstTblVO;

public interface CompanyDao {
	public int saveCompanyInfo_int(CompanyMstTblVO pIntcmprobj);	
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	
	public boolean deleteLabrSkillInfo(int pIntLborid);
}