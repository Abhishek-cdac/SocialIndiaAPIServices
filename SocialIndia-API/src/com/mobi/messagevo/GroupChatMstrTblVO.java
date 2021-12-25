package com.mobi.messagevo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class GroupChatMstrTblVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer grpChatId;
	private String grpName;
	private String grpImageWeb;
	private String grpImageMobile;
	private Integer status;
	private UserMasterTblVo createrId;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public GroupChatMstrTblVO(){
		
	}

	public Integer getGrpChatId() {
		return grpChatId;
	}

	public void setGrpChatId(Integer grpChatId) {
		this.grpChatId = grpChatId;
	}

	public String getGrpName() {
		return grpName;
	}
	
	public String getGrpImageWeb() {
		return grpImageWeb;
	}

	public void setGrpImageWeb(String grpImageWeb) {
		this.grpImageWeb = grpImageWeb;
	}

	public String getGrpImageMobile() {
		return grpImageMobile;
	}

	public void setGrpImageMobile(String grpImageMobile) {
		this.grpImageMobile = grpImageMobile;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public UserMasterTblVo getCreaterId() {
		return createrId;
	}

	public void setCreaterId(UserMasterTblVo createrId) {
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
