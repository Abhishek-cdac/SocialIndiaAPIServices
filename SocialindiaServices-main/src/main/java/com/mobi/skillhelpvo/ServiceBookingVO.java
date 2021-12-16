package com.mobi.skillhelpvo;

import java.io.Serializable;
import java.util.Date;

import com.pack.feedbackvo.FeedbackTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

public class ServiceBookingVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer bookingId;
	private LaborProfileTblVO labourId;
	private UserMasterTblVo usrId;
	private FeedbackTblVO feedbackId;
	private String problem;
	private Date preferedDate;
	private Date startTime;
	private Date endTime;
	private Float serviceCost;
	private Integer entryBy;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	public ServiceBookingVO(){}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public LaborProfileTblVO getLabourId() {
		return labourId;
	}

	public void setLabourId(LaborProfileTblVO labourId) {
		this.labourId = labourId;
	}

	public UserMasterTblVo getUsrId() {
		return usrId;
	}

	public void setUsrId(UserMasterTblVo usrId) {
		this.usrId = usrId;
	}

	public FeedbackTblVO getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(FeedbackTblVO feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Date getPreferedDate() {
		return preferedDate;
	}

	public void setPreferedDate(Date preferedDate) {
		this.preferedDate = preferedDate;
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

	public Float getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(Float serviceCost) {
		this.serviceCost = serviceCost;
	}

	public Integer getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
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
