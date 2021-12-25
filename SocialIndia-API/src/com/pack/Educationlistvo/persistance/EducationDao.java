package com.pack.Educationlistvo.persistance;

import com.pack.commonvo.EduMstrTblVO;

public interface EducationDao {
	public boolean toDeactivateEducationType(String locUpdQry);
	public int toInsertEducationType(EduMstrTblVO EducationTypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistEducationtypelistname(String lvrEducationTypetitle);
	
}