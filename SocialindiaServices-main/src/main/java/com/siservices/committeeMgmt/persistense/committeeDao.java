package com.siservices.committeeMgmt.persistense;

import com.siservices.signup.persistense.UserMasterTblVo;

public interface committeeDao  {
	
	public UserMasterTblVo getCommitteeDetailView(int committeeId);
	
	
	 public String userCreation(UserMasterTblVo userMaster);
	
	public boolean committeeInfoUpdate(UserMasterTblVo userMaster,int townshipid,int societyid,int committeeid);

}
