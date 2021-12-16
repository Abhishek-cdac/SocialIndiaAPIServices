package com.pack.Notificationlistvo.persistance;


public interface NotificationDao {
	public boolean toDeactivateNotifyType(String locUpdQry);
	//public int toInsertEducationType(EduMstrTblVO EducationTypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistEducationtypelistname(String lvrEducationTypetitle);
	public String getInitSQLTotal(String sql);
	
}