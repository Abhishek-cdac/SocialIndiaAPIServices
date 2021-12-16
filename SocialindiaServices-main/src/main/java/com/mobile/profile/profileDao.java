package com.mobile.profile;

import java.util.Date;
import java.util.List;

import com.pack.commonvo.FlatMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.socialindia.generalmgnt.staffUpdate;

public interface profileDao {
	
	 public UserMasterTblVo getUserPrfileInfo(String userId);

	public boolean checkProfileInfo(String userId,String email);

	 public boolean updateUserData(String rid,String profile_name,String profile_email,String profile_proof_id,String profile_proof_value);
	 
	 public boolean deletePersonalSkill(String userId,String skillid);
	 
	 public boolean checkSkillExist(String userId,String skillid);
	 
	 public int getInitTotal(String sqlQuery);
	 
	 public List<MvpUsrSkillTbl> getUserPrfileList(String userId,String timestamp, String startlim,String totalrow);
	 
	 public boolean updateImageName(String fileName,String userid);
	 
	 public boolean updatePersonalSkill(MvpUsrSkillTbl userSkillInfo);
	 
	 public boolean deleteExistPersonalSkill(String userId,String skillid,String categoryid);
	 
	 public UserMasterTblVo verifyLoginDetail(String userId,String password);
	 
	 public boolean verifyLoginDetailWithOtp(String userId,String password);
	 
	 public boolean verifyLoginDetailForFamilyWithEmail(String userId,String password);
	 
	 public boolean verifyLoginDetailWithEmail(String userId,String password);
	 
	 public UserMasterTblVo checkAccessPermission(String userId,String password,int committeeid,int residentid);
	 
	 public List<UserMasterTblVo> getUserSocietyDeatils(String username,int committeeid,int residentid,String otpfor);
	 
	 public List<UserMasterTblVo> getUserSocietyForFamilyDeatils(String username,int committeeid,int residentid);
	 
	 public boolean checkSocietyKey(String societykey,String emailid);
	 
	 public boolean checkCreateLoginForFamilyDetails(String soc_type,String soc_id,String soc_fname,String soc_lname,String soc_profile_url,String emailid,String password,String mobileno);
	 
	 public boolean checkCreateLoginDetails(String soc_type,String soc_id,String soc_fname,String soc_lname,String soc_profile_url,String emailid,String password,String mobileno,int userid);
	 
	 public boolean updateCreateLoginDetails(String societykey,String emailid);
	 
	 public UserMasterTblVo checkSiLoginDetails(String mobileno,String emailid);
	 
	 public boolean emailVerifivcation(String email,String flag,String userid);
	 
	 public boolean emailExistCheck(String email,String flag,String userid);
	 
	 
	 public boolean emailForFamilyVerifivcation(String email,String flag,String userid);
	 
	 public boolean checkSICreateLoginDetails(String mob,String email,String pass,int userid);
	 
	 public boolean checkSICreateForFamilyLoginDetails(String mob,String email,String pass,int userid);
	 
	 
	 public MvpFamilymbrTbl checkSiLoginForFamilyDetails(String mob,String email);
	 
	 public boolean checkMobnoOtp(String mob,String otp);
	 
	 
	 public boolean updateNewPassword(String mob,String pass);
	 public int getInitTotalViaSql(String sqlQuery);

	 public UserMasterTblVo getUserInfo(int userid);

	 public FlatMasterTblVO getFlatUserInfo(int userid);
	 
	 public List getRestPasswordUsers(String mob,String pass);
}
