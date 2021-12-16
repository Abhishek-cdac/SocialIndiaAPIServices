package com.mobi.messagevo;

import java.util.Date;

public class GrpChatAttachTblVO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer uniqId;
	private GroupChatTblVO chatId;
	private String attachName;
	private String thumpImage;
	private Integer fileType;	
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public GrpChatAttachTblVO() {
		
	}

	public Integer getUniqId() {
		return uniqId;
	}

	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}	

	public GroupChatTblVO getChatId() {
		return chatId;
	}

	public void setChatId(GroupChatTblVO chatId) {
		this.chatId = chatId;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public String getThumpImage() {
		return thumpImage;
	}

	public void setThumpImage(String thumpImage) {
		this.thumpImage = thumpImage;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
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
