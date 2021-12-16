package com.siservices.socialmedia.persistense;

import java.util.List;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.RightsMasterTblVo;

public interface socialMediaDao {
	
	public boolean checktwId(String twitterId);
	
	public boolean saveTwitterInfo(UserMasterTblVo userData,String picurl,Integer socialtype);
	
	public String savefacebookInfo(UserMasterTblVo userData,String picurl,Integer socialtype,String societyKey,String gender);

	public List<RightsMasterTblVo> getUserRightstwitter(int groupcode);
	
	public boolean checkfacebookId(String facebookId);
}
