package com.siservices.forumVo;

// default package
// Generated Jul 21, 2016 6:06:03 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MvpFourmDiscussTbl generated by hbm2java
 */
public class MvpFourmDiscussTbl implements java.io.Serializable {

	private Integer discussId;
	private MvpFourmTopicsTbl topicsId;
	private UserMasterTblVo userId;
	private String discussionDesc;
	private Integer status;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;

	public MvpFourmDiscussTbl() {
	}

	public MvpFourmDiscussTbl(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public MvpFourmDiscussTbl(MvpFourmTopicsTbl mvpFourmTopicsTbl,
			String discussionDesc, UserMasterTblVo discussUsrId, Integer status,
			Integer entryBy, Date entryDatetime, Date modifyDatetime) {
		this.topicsId = mvpFourmTopicsTbl;
		this.discussionDesc = discussionDesc;
		this.userId = discussUsrId;
		this.status = status;
		this.entryBy = entryBy;
		this.entryDatetime = entryDatetime;
		this.modifyDatetime = modifyDatetime;
	}

	public Integer getDiscussId() {
		return this.discussId;
	}

	public void setDiscussId(Integer discussId) {
		this.discussId = discussId;
	}

	public MvpFourmTopicsTbl getTopicsId() {
		return topicsId;
	}

	public void setTopicsId(MvpFourmTopicsTbl topicsId) {
		this.topicsId = topicsId;
	}

	public String getDiscussionDesc() {
		return this.discussionDesc;
	}

	public void setDiscussionDesc(String discussionDesc) {
		this.discussionDesc = discussionDesc;
	}


	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
	}

	public Date getEntryDatetime() {
		return this.entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModifyDatetime() {
		return this.modifyDatetime;
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

}