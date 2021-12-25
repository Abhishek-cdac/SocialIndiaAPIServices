package com.mobi.publishskillvo;

import java.io.Serializable;
import java.util.Date;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

public class PublishSkillTblVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer pubSkilId;	
	private String title;
	private Integer duration;
	private Integer durationFlg;
	private String avbilDays;
	private String briefDesc;
	private Float fees;
	private Integer status;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	private UserMasterTblVo userId;
	private FeedsTblVO feedId;
	private CategoryMasterTblVO categoryId;
	private SkillMasterTblVO skillId;
	
	public PublishSkillTblVO(){
		
	}

	public Integer getPubSkilId() {
		return pubSkilId;
	}

	public void setPubSkilId(Integer pubSkilId) {
		this.pubSkilId = pubSkilId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getDurationFlg() {
		return durationFlg;
	}

	public void setDurationFlg(Integer durationFlg) {
		this.durationFlg = durationFlg;
	}

	public String getAvbilDays() {
		return avbilDays;
	}

	public void setAvbilDays(String avbilDays) {
		this.avbilDays = avbilDays;
	}

	public String getBriefDesc() {
		return briefDesc;
	}

	public void setBriefDesc(String briefDesc) {
		this.briefDesc = briefDesc;
	}

	public Float getFees() {
		return fees;
	}

	public void setFees(Float fees) {
		this.fees = fees;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public UserMasterTblVo getUserId() {
		return userId;
	}

	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}

	public FeedsTblVO getFeedId() {
		return feedId;
	}

	public void setFeedId(FeedsTblVO feedId) {
		this.feedId = feedId;
	}

	public CategoryMasterTblVO getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(CategoryMasterTblVO categoryId) {
		this.categoryId = categoryId;
	}

	public SkillMasterTblVO getSkillId() {
		return skillId;
	}

	public void setSkillId(SkillMasterTblVO skillId) {
		this.skillId = skillId;
	}

	

}
