package com.mobile.signup;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;

public interface signUpDao {
	
	public UserMasterTblVo checkUserSignUpInfo(String mobile,int committeeCode,int residentCode,String userid,String otpfor);

	public boolean checkUserOtpVerify(String mobile,int committeeCode,int residentCode,String userid,String otpfor);

	public boolean checkAccessMedia(String mobile);
	
	public boolean checkUserActSts(String mobile);
	
	public boolean checkUserBlockedActSts(String mobile,String pass,int committeeCode,int residentCode);
	
	public UserMasterTblVo getFamilyDetailUserInfo(int useridParendid);
	
	public boolean checFamilyOtpVerify(String mobile,String otpfor);
	
	 public boolean updateOtpUser(String mobno, String userId,String password,int otpcount);

	 
	public String checkFamilyInfo(String mobile,String otpfor);
	
	public boolean checkFamilyActiveSts(String mobile);
	
	public MvpFamilymbrTbl checkFamilyAccess(String mobile,String otpfor);
	
	public UserMasterTblVo getUserInfo(int userid);
}
