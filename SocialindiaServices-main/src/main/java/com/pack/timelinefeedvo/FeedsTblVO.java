package com.pack.timelinefeedvo; 

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;

public class FeedsTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer feedId;
	private Integer feedType;
	private Integer feedTypeId;
	private String feedMsg;
	private String urlsThumbImg;
	private String urlsTitle;
	private String urlsPageurl;
	private String feedCategory;
	private String feedTitle;
	private String feedStitle;
	private String feedDesc;
	private Date feedTime;
	private Float amount;
	private String postAs;
	private Integer postBy;
	private String feedLocation;
	private Integer isMyfeed;
	private Integer isShared;
	private String originatorName;
	private Integer originatorId;
	private Long likeCount;
	private Long shareCount;
	private Long commentCount;
	private Integer feedPrivacyFlg;
	private Integer feedStatus;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	private UserMasterTblVo usrId;
	private SocietyMstTbl societyId;
	private String additionalData;
	
	public FeedsTblVO(){}

	public Integer getFeedId() {
		return feedId;
	}

	public void setFeedId(Integer feedId) {
		this.feedId = feedId;
	}

	public Integer getFeedType() {
		return feedType;
	}
		
	public Integer getFeedTypeId() {
		return feedTypeId;
	}

	public void setFeedTypeId(Integer feedTypeId) {
		this.feedTypeId = feedTypeId;
	}

	public void setFeedType(Integer feedType) {
		this.feedType = feedType;
	}

	public String getFeedMsg() {
		return feedMsg;
	}

	public void setFeedMsg(String feedMsg) {
		this.feedMsg = feedMsg;
	}	

	public String getUrlsThumbImg() {
		return urlsThumbImg;
	}

	public void setUrlsThumbImg(String urlsThumbImg) {
		this.urlsThumbImg = urlsThumbImg;
	}

	public String getUrlsTitle() {
		return urlsTitle;
	}

	public void setUrlsTitle(String urlsTitle) {
		this.urlsTitle = urlsTitle;
	}

	public String getUrlsPageurl() {
		return urlsPageurl;
	}

	public void setUrlsPageurl(String urlsPageurl) {
		this.urlsPageurl = urlsPageurl;
	}

	public String getFeedCategory() {
		return feedCategory;
	}

	public void setFeedCategory(String feedCategory) {
		this.feedCategory = feedCategory;
	}

	public String getFeedTitle() {
		return feedTitle;
	}

	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}

	public String getFeedStitle() {
		return feedStitle;
	}

	public void setFeedStitle(String feedStitle) {
		this.feedStitle = feedStitle;
	}

	public String getFeedDesc() {
		return feedDesc;
	}

	public void setFeedDesc(String feedDesc) {
		this.feedDesc = feedDesc;
	}

	public Date getFeedTime() {
		return feedTime;
	}

	public void setFeedTime(Date feedTime) {
		this.feedTime = feedTime;
	}

//	public float getAmount() {
//		return amount;
//	}
//
//	public void setAmount(float amount) {
//		this.amount = amount;
//	}
	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
	
	public String getPostAs() {
		return postAs;
	}

	

	public void setPostAs(String postAs) {
		this.postAs = postAs;
	}

	public Integer getPostBy() {
		return postBy;
	}

	public void setPostBy(Integer postBy) {
		this.postBy = postBy;
	}

	public String getFeedLocation() {
		return feedLocation;
	}

	public void setFeedLocation(String feedLocation) {
		this.feedLocation = feedLocation;
	}

	public Integer getIsMyfeed() {
		return isMyfeed;
	}

	public void setIsMyfeed(Integer isMyfeed) {
		this.isMyfeed = isMyfeed;
	}

	public Integer getIsShared() {
		return isShared;
	}

	public void setIsShared(Integer isShared) {
		this.isShared = isShared;
	}

	public String getOriginatorName() {
		return originatorName;
	}

	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}

	public Integer getOriginatorId() {
		return originatorId;
	}

	public void setOriginatorId(Integer originatorId) {
		this.originatorId = originatorId;
	}

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public Long getShareCount() {
		return shareCount;
	}

	public void setShareCount(Long shareCount) {
		this.shareCount = shareCount;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getFeedPrivacyFlg() {
		return feedPrivacyFlg;
	}

	public void setFeedPrivacyFlg(Integer feedPrivacyFlg) {
		this.feedPrivacyFlg = feedPrivacyFlg;
	}

	public Integer getFeedStatus() {
		return feedStatus;
	}

	public void setFeedStatus(Integer feedStatus) {
		this.feedStatus = feedStatus;
	}

	public Integer getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
	}

	public Date getEntryDatetime() {
		return entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public UserMasterTblVo getUsrId() {
		return usrId;
	}

	public void setUsrId(UserMasterTblVo usrId) {
		this.usrId = usrId;
	}

	public SocietyMstTbl getSocietyId() {
		return societyId;
	}

	public void setSocietyId(SocietyMstTbl societyId) {
		this.societyId = societyId;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	
}