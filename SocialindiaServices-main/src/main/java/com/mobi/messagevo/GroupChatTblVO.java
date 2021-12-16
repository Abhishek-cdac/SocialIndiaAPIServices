package com.mobi.messagevo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class GroupChatTblVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer chatId;
	private GroupChatMstrTblVO grpChatId;
	private UserMasterTblVo userId;
	private String msgContent;
	private String urlThumbImage;
	private String urlTitle;
	private String pageUrl;	
	private Integer msgType;
	private Integer readFlg;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public GroupChatTblVO() {
		
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public GroupChatMstrTblVO getGrpChatId() {
		return grpChatId;
	}

	public void setGrpChatId(GroupChatMstrTblVO grpChatId) {
		this.grpChatId = grpChatId;
	}

	public UserMasterTblVo getUserId() {
		return userId;
	}

	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}	

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getUrlThumbImage() {
		return urlThumbImage;
	}

	public void setUrlThumbImage(String urlThumbImage) {
		this.urlThumbImage = urlThumbImage;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}


	public Integer getReadFlg() {
		return readFlg;
	}

	public void setReadFlg(Integer readFlg) {
		this.readFlg = readFlg;
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
