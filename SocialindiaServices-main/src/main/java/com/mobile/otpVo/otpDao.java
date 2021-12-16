package com.mobile.otpVo;

import java.util.Date;
import java.util.List;

import com.mobi.networkUserListVO.MvpNetworkTbl;
import com.siservices.materialVo.MvpMaterialTbl;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;

public interface otpDao {
	
	
	
	public boolean checkTownshipKey( String userId,String townshipKey);
	
	public boolean checkTownshipKeyWithoutRid(String townshipKey);
	
	public UserMasterTblVo checkSocietyKeyForList(String societykey,String userId);
	
	public UserMasterTblVo checkUserDetails(String userId);
	
	public UserMasterTblVo checkDupicateMobile(String societykey,String mobile);
	
	public boolean checkSocietyKey(String societyKey,String userId);
	
	public boolean checkSocietyKeyWithoutRid(String societyKey);
	
	 public UserMasterTblVo checkUserInfo(String mobno, String userId);

	 public String getSysParamsInfo(String otpLen);
	 
	 public boolean updateOtpUser(String mobno, String userId,String password,String type,int otpcount);
	 
	 public boolean updateOtpUserStaFlg(String mobile);
	 
	 public MvpUserOtpTbl getOtpValidateInfo(String mobno, String userId,String otpfor,String otp);
	 
	 public String getOtpValidateForFamily(String mobno, String userId,String otpfor,String otp);
	 
	 public MvpUserOtpTbl checkUserOtpValidate(String mobno, String userId,String otp,String otpfor);
	 
	 public boolean checkFamailyOtpValidate(String mobno, String userId,String otp,String otpfor);
	 
	 public UserMasterTblVo checkUserMobileInfo(String mobno, String userId,String otp,String otpfor);
	 
	 public List<UserMasterTblVo> getUserDeatils(String mobno, String userId,int committeeid,int residentid);
	 
	 public List<UserMasterTblVo> getParentDetails(String parentid, String userId,int committeeid,int residentid,String mob);
	 
	 public UserMasterTblVo getParentForRidDetails(String parentid, String userId,int committeeid,int residentid);
	 
	 public MvpUserOtpTbl checkOtpInfo( String mobile);
	 
	 public boolean insertOtpInfo(MvpUserOtpTbl userOtpInfo);
	 
	 public MvpFamilymbrTbl checkFamilyInfoOtp(String mobileno,String otpfor);
	 
	 public boolean checkTownshipKeyWithEmailOrMobno(String townshipKey,String emailMobno);
	 
	 
}
