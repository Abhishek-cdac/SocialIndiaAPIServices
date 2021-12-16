package com.mobi.commonvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class PrivacyInfoTblVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer uniqId;
	private UserMasterTblVo usrId; 
	private Integer privacyFlg;
	private String usrIds; 
	private String favouriteMenuIds;
	private Integer status; 
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public PrivacyInfoTblVO() {
		
	}
	public Integer getUniqId() {
		return uniqId;
	}
	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}
	public UserMasterTblVo getUsrId() {
		return usrId;
	}
	public void setUsrId(UserMasterTblVo usrId) {
		this.usrId = usrId;
	}
	public Integer getPrivacyFlg() {
		return privacyFlg;
	}
	public void setPrivacyFlg(Integer privacyFlg) {
		this.privacyFlg = privacyFlg;
	}
	public String getUsrIds() {
		return usrIds;
	}
	public void setUsrIds(String usrIds) {
		this.usrIds = usrIds;
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
	public String getFavouriteMenuIds() {
		return favouriteMenuIds;
	}
	public void setFavouriteMenuIds(String favouriteMenuIds) {
		this.favouriteMenuIds = favouriteMenuIds;
	}	
	

}
