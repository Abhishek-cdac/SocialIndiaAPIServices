package com.socialindiaservices.vo;

import java.util.Date;

public class MvpDonationAttachTblVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer donataionAttchId;
	private MvpDonationTbl donateId;
	private String attachName;
	private Integer status;
	private String thumbImage;
	private Integer fileType;
	private Date entryDateime;
	private Date modifyDatetime;

	public MvpDonationAttachTblVo() {
	}

	public Integer getDonataionAttchId() {
		return donataionAttchId;
	}

	public void setDonataionAttchId(Integer donataionAttchId) {
		this.donataionAttchId = donataionAttchId;
	}

	public MvpDonationTbl getDonateId() {
		return donateId;
	}

	public void setDonateId(MvpDonationTbl donateId) {
		this.donateId = donateId;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Date getEntryDateime() {
		return entryDateime;
	}

	public void setEntryDateime(Date entryDateime) {
		this.entryDateime = entryDateime;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	

}
