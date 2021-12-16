package com.mobi.common;

import java.util.List;

import org.json.JSONArray;

import com.mobi.commonvo.PrivacyInfoTblVO;
import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;

public interface CommonMobiDao {
	
	public boolean checkSocietyKey(String societyKey,String userId);
	
	public boolean checkTownshipKey(String townshipKey,String userId);
	
	public int getTotalCountSqlQuery(String sqlQuery);
	
	public int getTotalCountQuery(String query);
	
	public UserMasterTblVo getProfileDetails(int rid);
	
	public UserMasterTblVo adminContactNo(String adminName);
	
	public UserMasterTblVo getUserInfo(String mobileNo);
	
	public FeedattchTblVO getAttachmentDetails(int attachId);
	
	public boolean removeFeedViewId(int feedId);
	
	public JSONArray getloginverify(String userid,String committee,String resident,String profileimgPath,String societyimgPath);
	
	public int[] getCommiteResiIds(String committee,String resident);
	
	public List<WhyShouldIUpdateTblVO> whyShouldIUpdateContent();
	
	public CategoryMasterTblVO getCategoryDetails(int cateId);
	
	public PrivacyInfoTblVO getDefaultPrivacyFlg(int userId);
	
	public int insertPrivacyFlg(PrivacyInfoTblVO privacyObj);
	
	public boolean updatePrivacyFlg(PrivacyInfoTblVO privacyObj);
	
	public int notifictionFlgGet(String mobileno);
	
	public boolean flatDetailsUpdate(int rid,int flatId);
	
	public boolean updateTableByQuery(String query);
	
	public List<UserMasterTblVo> getUserRegisterDeatils(String sqlQuery);
	
	public ComplaintsTblVO getcomplaintMastTblById(int complaintId);
	
	public SocietyMstTbl getSocietymstDetail(String societyKey);
		

}
