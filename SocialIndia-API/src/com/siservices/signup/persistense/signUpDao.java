package com.siservices.signup.persistense;

public interface signUpDao {

		public boolean checkActiveKey(String activeKey);
		
		public String saveUserInfo(UserMasterTblVo userMaster);
		
		public boolean userProfileUpdate(UserMasterTblVo userMaster,String countryid,String stateid,String cityid,String postalcode,String idcardtype);
}
