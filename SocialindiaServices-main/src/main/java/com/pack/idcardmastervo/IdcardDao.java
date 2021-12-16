package com.pack.idcardmastervo;

import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;

public interface IdcardDao {
	public boolean toDeactivateIdcardtype(String locUpdQry);
	public int toInsertIdcardtype(IDCardMasterTblVO IdcardtypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistIdcardname(String lvrIdcardtypetitle);
	
}