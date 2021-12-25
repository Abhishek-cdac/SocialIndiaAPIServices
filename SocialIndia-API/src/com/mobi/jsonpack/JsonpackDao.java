package com.mobi.jsonpack;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.addpost.MvpAdpostTbl;
import com.mobi.publishskillvo.PublishSkillTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.socialindiaservices.vo.MvpDonationTbl;

public interface JsonpackDao {
	
	public JSONArray laborRegDetail(List<Object[]> laborDetailObj,String imagepath);
	
	public JSONArray bookingListDetil(List<Object[]> bookingListObj,String imagepath);
	
	public JSONArray feedCommentListDetil(List<Object[]> feedCommentListObj,String externalUserImagePath);
	
	public JSONArray feedListDetails(List<Object[]> feedListListObj);	
	
	public JSONArray privacyListCategory();
	
	public JSONArray privacyListUsers(List<Object[]> privacyListUserObj,String imagePath);
	
	public JSONArray listOfGroupPrivateContact(List<Object[]> privacyListUserObj,String imagePath);
	
	public JSONArray listOfGroupMsg(List<Object[]> groupMsgObj,String imagePath);
	
	public JSONArray listOfGrpMsgPrivateMsg(List<Object[]> privateMsgObj,String profileimgPath,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb,int groupContactFlg);
	
	public JSONObject loginUserDetail_t(UserMasterTblVo userObj,String profileImgPath,String societyImgPath,int committeeGrpcode,int residenteGrpcode);
	
	public JSONArray loginUserDetail(String userid,String profileImgPath,String societyImgPath,int committeeGrpcode,int residenteGrpcode);
	
	public JSONArray userOnlineDetails(List<Object[]> userOnlineListObj,String imagePath);

	public JSONObject getAdsPostJasonpackValues(MvpAdpostTbl adPost);

	public JSONObject publishSkilJasonpackValues(PublishSkillTblVO publishSkilObj);
	
	public JSONObject eventTableJasonpackValues(int eventId);

	public JSONObject jsonFeedDetailsPack(Object[] objList, String imagePathWeb,String imagePathMobi, String videoPath, String videoPathThumb,String profileimgPath);

	public JSONObject donationAdditonalJasonpackValues(MvpDonationTbl donationGetObj);
	
	
	
	
	
}
