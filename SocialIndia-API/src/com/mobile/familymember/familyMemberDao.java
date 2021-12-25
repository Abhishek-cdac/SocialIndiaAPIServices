package com.mobile.familymember;

import java.util.List;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;

public interface familyMemberDao {
	
	
	public List<MvpFamilymbrTbl> getFamilyMembersList(String userId,String timestamp, String startlim,String totalrow,int societyId,String andQueryCondition);
	
	public boolean deleteFamilyMember(String userId,String familyid);
	
	public boolean checkFamilyInfo(String userId,String familyid);
	
	public MvpFamilymbrTbl checkFamilyMobileExist(String userId,String mobile);
	
	public boolean updateFamilyInfo(String userId,String mobile,int familyId);
	
	public Integer getFamilyMembersAddDetails(MvpFamilymbrTbl familtMemberMst);
	
	public MvpFamilymbrTbl getFamilyMembersEditDetails(String userId,String familyid);
	
	public boolean updateFamilyMemberDetails(MvpFamilymbrTbl familtMemberMst,String relationId,String titlename,String bloodid,String age,String is_invited);

	public boolean updateFamilyRelationInfo(String userId,String familyid,String relationId);
	
	public boolean familyMemberLoginInvite(UserMasterTblVo userMst,String userId,String familyid,String invite_revoke);
	
	public boolean familyMemberLoginRevoke(String userId,String familyid,String invite_revoke);
	
	public  MvpFamilymbrTbl getFamilyInfoForUserTable(String userId,String familyid);
}
