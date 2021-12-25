package com.social.failedSignon.persistense;

import com.social.failedSignonvo.FailedSignOnTbl;

public interface FailsignDao {

	String failCreationForm(FailedSignOnTbl fail);

	int getInitTotal(String sql);
	

}
