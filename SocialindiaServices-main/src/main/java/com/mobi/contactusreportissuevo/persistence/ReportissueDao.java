package com.mobi.contactusreportissuevo.persistence;

import java.util.List;

import com.mobi.contactusreportissuevo.ReportIssueTblVO;
import com.mobile.otpVo.MvpUserOtpTbl;

public interface ReportissueDao {
	
	public int toInsertReportissue(ReportIssueTblVO reportIssueObj);
	
	public Integer toGetUserId(String mobile);
	
	public boolean updateOTPIssue(int userId);

}
