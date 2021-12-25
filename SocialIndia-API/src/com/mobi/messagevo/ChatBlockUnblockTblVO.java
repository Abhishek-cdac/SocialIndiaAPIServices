package com.mobi.messagevo;

import java.io.Serializable;
import java.util.Date;


public class ChatBlockUnblockTblVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int fromUsrId;
	private int toUsrId;
	private Date modifyDatetime;
	private boolean blocked;
	
	public ChatBlockUnblockTblVO(){
		
	}

	public int getFromUsrId() {
		return fromUsrId;
	}

	public void setFromUsrId(int fromUsrId) {
		this.fromUsrId = fromUsrId;
	}

	public int getToUsrId() {
		return toUsrId;
	}

	public void setToUsrId(int toUsrId) {
		this.toUsrId = toUsrId;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
