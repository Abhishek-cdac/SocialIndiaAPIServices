package com.mobi.feedvo;

import java.io.Serializable;
import java.util.Date;

public class FeedCommentTblVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Integer commentId;
	private Integer feedId;
	private Integer usrId;
	private String comment;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public FeedCommentTblVO(){
		
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getFeedId() {
		return feedId;
	}

	public void setFeedId(Integer feedId) {
		this.feedId = feedId;
	}

	public Integer getUsrId() {
		return usrId;
	}

	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
	
	
}
