package com.mobi.messagevo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class GroupChatMemTblVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer memberId;
	private GroupChatMstrTblVO grpChatId;
	private UserMasterTblVo usrId;
	private Integer status;
	private Integer createrId;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public GroupChatMemTblVO() {
		
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public GroupChatMstrTblVO getGrpChatId() {
		return grpChatId;
	}

	public void setGrpChatId(GroupChatMstrTblVO grpChatId) {
		this.grpChatId = grpChatId;
	}

	public UserMasterTblVo getUsrId() {
		return usrId;
	}

	public void setUsrId(UserMasterTblVo usrId) {
		this.usrId = usrId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}	

	public Integer getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
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
