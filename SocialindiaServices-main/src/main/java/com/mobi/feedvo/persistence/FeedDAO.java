package com.mobi.feedvo.persistence;

import java.io.File;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONArray;

import com.mobi.feedvo.FeedCommentTblVO;
import com.mobi.feedvo.FeedLikeUnlikeTBLVO;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.timelinefeedvo.FeedsViewTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

public interface FeedDAO {
	
	public List<Object[]> feedList(String whereQuery,int rid,int startLimit);
	
	public List<Object[]> feedListProc(int rid,String socityKey,String timeStamp,int feedType,int postBy,String feedCategory,int startLimit,int endLimit);
	
	public List<Object[]> feedListProcDetails(int rid,String socityKey,String timeStamp,String feedType,int postBy,String feedCategory,int startLimit,int endLimit,String search);
	
	public int feedListTotalCountGet(int rid,String socityKey,String timeStamp,String feedType,int postBy,String feedCategory,String search);
	
	public int feedInsert(String users,FeedsTblVO feedInsertObj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName);
	
	public int feedInsertPrivacyflag(String users,FeedsTblVO feedInsertObj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName,int privacyflag,String userIdArr);
	public boolean feedEditPrivacyflag(FeedsTblVO feedInsertObj,String users,JSONArray removeAttachIdsObj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName);
	
	public int feedInsertAttachment(FeedattchTblVO feedAttach);
	
	public int feedInsertView(int userId, int feedId ,int entryBy);
	
	public boolean feedEdit(FeedsTblVO feedInsertObj,String users,JSONArray removeAttachIdsObj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName);
	
	public Session feedAttachUpload(Session session,int feedId,List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName);
	
	public boolean feedDelete(FeedsTblVO feedObj);
	
	public boolean feedDelete(String feed_id, String user_id);
	
	public boolean feedViewDelete(FeedsViewTblVO feedViewObj,int feedDelFlg);
	
	public FeedsTblVO feedData(int feedId);
	
	public FeedsViewTblVO feedViewData(int feedViewId);
	
	public int commentPost(FeedCommentTblVO commentpostObj);
	
	public boolean commentDelete(FeedCommentTblVO commentDelObj);
	
	public List<Object[]> commentList(FeedCommentTblVO commentListObj,String timestamp,int startLimit,int endLimit);
	
	public List<Object[]> latestCommentList(FeedCommentTblVO commentListObj,String timestamp,int startLimit,int endLimit);
	
	public FeedCommentTblVO commentData(int cmntId);
	
	public int getTotalCountSqlQuery(String sqlQuery);
	
	public int commentLikeUnlike(FeedLikeUnlikeTBLVO likeUnlikeObj);
	
	public boolean likeUnlikeCountUpdate(int feedId ,int likeStatus);
	
	public boolean feedLikeUnlikeUpdate(int likeID,int likeFlg);
	
	public boolean feedLikeUnlikeUpdateAg(int likeID,int likeFlg);
	
	public List<Object[]> likeCountList(int feedId);
	
	public UserMasterTblVo getFeedPostedUserDetail(int userId );
	
	public String totalNoNetworkUser(int rid);
	
	public List<Object[]> networkConectedList(int userid,String timestamp,int startLimit,int endLimit,String searchText);
	
	public List<Object[]> networkConectedAndSocityIdList(int userid,String socityId,String timestamp,int startLimit,int endLimit,String searchTxt);
	
	public FeedsTblVO getFeedDetails(int rid);
	
	public boolean shareFeed(int rid,int feedId,int privacy,int socityId,String users,int parentViewId);
	
	public FeedsTblVO getFeedDetailsByEventIdRid(int eventId,int feedType,int rid);
	
	public FeedsTblVO getFeedDetailsByEventId(int eventId,int feedType);
	
	public FeedsTblVO getFeedDetailsByOfferCarpoolId(int carpoolId);
	
	public FeedsTblVO getFeedDetailsByFacilityBookId(int facBookId);
	
	public boolean updateFeed(FeedsTblVO feedObj);
	
	public Object[] sharedfeedDetails(int rid,int feedId,int feedViewId);
	
	public boolean shareFeedCountUpdate(int feedId);
	
	public boolean sharedFeedPrivacyEdit(int rid,int feedid,int feedViewId,int feedPrivacy,int societyId,String users);
	
	public List<Object[]> feedDetailsProc(int rid,String socityKey,int feedId,String search);
	
	public int getFeedViewId(int rid,int socityID,int feedId,int useridColumn,int isShared);
	
	public int getIsFeedLikedOrNot(int feedId,int userId);
	
	public int getFeedIsLikedFlg(int feedId,int userId);

	public FeedsViewTblVO getFeedViewDetails(int retFeedViewId);
	
	public boolean commentFeedCountUpdate(int feedId);
	
	public List getFeedAttachmentObjByQuery(String query);
	
	public List getFeedSearchDetails(String query);
	
}
