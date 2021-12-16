package com.pack.commonapi;

import java.util.List;

import com.pack.commonvo.CountryMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.RightsMasterTblVo;

public interface CountryDao {
	
	 public List<CountryMasterTblVO> getcountryList();
}
