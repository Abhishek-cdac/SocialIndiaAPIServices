package com.mobi.contactusreportissuevo.persistence;

import com.mobi.contactusreportissuevo.ReportIssueTblVO;

public interface ReportissueDao {
	
	public int toInsertReportissue(ReportIssueTblVO reportIssueObj);
	
	public Integer toGetUserId(String mobile);
	
	public boolean updateOTPIssue(int userId);

}
