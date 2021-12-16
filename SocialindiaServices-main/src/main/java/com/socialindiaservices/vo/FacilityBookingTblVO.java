package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class FacilityBookingTblVO {
	
	private Integer bookingId;
	private String title;
	private String place;
	private String description;
	private Date startTime;
	private Date endTime;
	private String contactNo;
	private int statusFlag;
	private int bookingStatus;
	private Date entryDatetime;
	private Date modifyDatetime;
	private FacilityMstTblVO facilityId;
	private UserMasterTblVo entryBy;
	private UserMasterTblVo updatedBy;
	private UserMasterTblVo bookedBy;
	private String commiteecomment;
	public FacilityBookingTblVO(){}

	
	
	public String getCommiteecomment() {
		return commiteecomment;
	}



	public void setCommiteecomment(String commiteecomment) {
		this.commiteecomment = commiteecomment;
	}



	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}


	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public int getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(int bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
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

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}

	public UserMasterTblVo getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserMasterTblVo updatedBy) {
		this.updatedBy = updatedBy;
	}

	public UserMasterTblVo getBookedBy() {
		return bookedBy;
	}

	public void setBookedBy(UserMasterTblVo bookedBy) {
		this.bookedBy = bookedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FacilityMstTblVO getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(FacilityMstTblVO facilityId) {
		this.facilityId = facilityId;
	}

}
