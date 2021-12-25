package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class CarPoolBookingTblVO {
	private int bookingId;
	private CarPoolingTblVO poolId;
	private UserMasterTblVo bookedBy;
	private String comments;
	private int status;
	private Date entryDateTime;
	private Date modifyDateTime;

	public CarPoolBookingTblVO(){}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public CarPoolingTblVO getPoolId() {
		return poolId;
	}

	public void setPoolId(CarPoolingTblVO poolId) {
		this.poolId = poolId;
	}

	public UserMasterTblVo getBookedBy() {
		return bookedBy;
	}

	public void setBookedBy(UserMasterTblVo bookedBy) {
		this.bookedBy = bookedBy;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getEntryDateTime() {
		return entryDateTime;
	}

	public void setEntryDateTime(Date entryDateTime) {
		this.entryDateTime = entryDateTime;
	}

	public Date getModifyDateTime() {
		return modifyDateTime;
	}

	public void setModifyDateTime(Date modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
