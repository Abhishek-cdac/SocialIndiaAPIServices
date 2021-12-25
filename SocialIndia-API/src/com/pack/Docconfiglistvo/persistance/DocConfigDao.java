package com.pack.Docconfiglistvo.persistance;

import com.pack.commonvo.DoctypMasterTblVO;

public interface DocConfigDao {
	public boolean toDeactivatedoctypetype(String locUpdQry);
	public int toInsertdoctypetype(DoctypMasterTblVO doctypetypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistDocumenttypelistname(String lvrdoctypetypetitle);
	
}