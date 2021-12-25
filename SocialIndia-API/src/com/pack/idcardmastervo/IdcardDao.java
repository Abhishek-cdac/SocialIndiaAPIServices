package com.pack.idcardmastervo;

import com.pack.commonvo.IDCardMasterTblVO;

public interface IdcardDao {
	public boolean toDeactivateIdcardtype(String locUpdQry);
	public int toInsertIdcardtype(IDCardMasterTblVO IdcardtypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistIdcardname(String lvrIdcardtypetitle);
	
}