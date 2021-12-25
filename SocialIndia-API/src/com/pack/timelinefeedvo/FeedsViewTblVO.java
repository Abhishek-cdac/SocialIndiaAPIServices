package com.pack.timelinefeedvo;

import java.util.Date;

import com.siservices.uam.persistense.SocietyMstTbl;

public class FeedsViewTblVO {

	private static final long serialVersionUID = 1L;
	private Integer uniqId;	
	private Integer parentViewId;
	private Integer usrId;
	private Integer sharedPrivacyFlg;
	private Integer isShared;
	private Integer entryBy;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	private FeedsTblVO feedId;
//	private UserMasterTblVo usrId;
	private SocietyMstTbl societyId;
	
	public FeedsViewTblVO(){
		
	}

	public Integer getUniqId() {
		return uniqId;
	}

	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}			

	public Integer getParentViewId() {
		return parentViewId;
	}

	public void setParentViewId(Integer parentViewId) {
		this.parentViewId = parentViewId;
	}

	public Integer getSharedPrivacyFlg() {
		return sharedPrivacyFlg;
	}

	public void setSharedPrivacyFlg(Integer sharedPrivacyFlg) {
		this.sharedPrivacyFlg = sharedPrivacyFlg;
	}

	public Integer getIsShared() {
		return isShared;
	}

	public void setIsShared(Integer isShared) {
		this.isShared = isShared;
	}

	public Integer getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
	}	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public FeedsTblVO getFeedId() {
		return feedId;
	}

	public void setFeedId(FeedsTblVO feedId) {
		this.feedId = feedId;
	}

	public Integer getUsrId() {
		return usrId;
	}

	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}

	public SocietyMstTbl getSocietyId() {
		return societyId;
	}

	public void setSocietyId(SocietyMstTbl societyId) {
		this.societyId = societyId;
	}
	
	
	
}
