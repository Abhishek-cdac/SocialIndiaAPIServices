package com.mobi.feedvo;

import java.io.Serializable;
import java.util.Date;

public class FeedLikeUnlikeTBLVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer likeId;
	private Integer usrId;
	private Integer feedId;
	private Integer feedFlg;
	private Integer likeUnlikeFlg;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;	
	
	public FeedLikeUnlikeTBLVO(){}

	public Integer getLikeId() {
		return likeId;
	}

	public void setLikeId(Integer likeId) {
		this.likeId = likeId;
	}

	public Integer getUsrId() {
		return usrId;
	}

	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}

	public Integer getFeedId() {
		return feedId;
	}

	public void setFeedId(Integer feedId) {
		this.feedId = feedId;
	}

	public Integer getFeedFlg() {
		return feedFlg;
	}

	public void setFeedFlg(Integer feedFlg) {
		this.feedFlg = feedFlg;
	}

	public Integer getLikeUnlikeFlg() {
		return likeUnlikeFlg;
	}

	public void setLikeUnlikeFlg(Integer likeUnlikeFlg) {
		this.likeUnlikeFlg = likeUnlikeFlg;
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
