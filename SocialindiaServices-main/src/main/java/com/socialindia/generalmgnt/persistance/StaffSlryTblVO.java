package com.socialindia.generalmgnt.persistance;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class StaffSlryTblVO {
	private Integer staffSlryId;
	private Integer entryby;
	private StaffMasterTblVo staffID;
	private Double monthlySalary;
	private Double extraWages;
	private Integer status;
	private Date entryDatetime;
	private Date modifyDatetime;

	/*public StaffSlryTblVO() {
	}

	public StaffSlryTblVO(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public MvpStaffSlryTbl(UsrRegTbl usrRegTbl,
			MvpStaffProfTbl mvpStaffProfTbl, Float monthlySalary,
			Float extraWages, Integer status, Date entryDatetime,
			Date modifyDatetime) {
		this.usrRegTbl = usrRegTbl;
		this.mvpStaffProfTbl = mvpStaffProfTbl;
		this.monthlySalary = monthlySalary;
		this.extraWages = extraWages;
		this.status = status;
		this.entryDatetime = entryDatetime;
		this.modifyDatetime = modifyDatetime;
	}*/

	public Integer getStaffSlryId() {
		return this.staffSlryId;
	}

	public void setStaffSlryId(Integer staffSlryId) {
		this.staffSlryId = staffSlryId;
	}



	

	

	
	

	public Integer getEntryby() {
		return entryby;
	}

	public void setEntryby(Integer entryby) {
		this.entryby = entryby;
	}

	public StaffMasterTblVo getStaffID() {
		return staffID;
	}

	public void setStaffID(StaffMasterTblVo staffID) {
		this.staffID = staffID;
	}

	

	public Double getMonthlySalary() {
		return monthlySalary;
	}

	public void setMonthlySalary(Double monthlySalary) {
		this.monthlySalary = monthlySalary;
	}

	public Double getExtraWages() {
		return extraWages;
	}

	public void setExtraWages(Double extraWages) {
		this.extraWages = extraWages;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getEntryDatetime() {
		return this.entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public Date getModifyDatetime() {
		return this.modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

}
