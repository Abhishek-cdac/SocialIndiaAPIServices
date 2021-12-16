package com.socialindiaservices.vo;

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;

public class CarPoolingTblVO {
	
	private int poolId;
	private int youWillBe;
	private int returnTrip;
	private String tripFromCity;
	private String tripToCity;
	private String tripfromPlaceId;
	private String triptoPlaceId;
	private Date tripStartDate;
	private Date tripEndDate;
	private CarMasterTblVO carId;
	private String days;
	private int tripFrequency;
	private String additionalinfo;
	private int seatsAvaliable;
	private int status;
	private GroupMasterTblVo entryByGroup;
	private UserMasterTblVo entryBy;
	private Date entryDateTime;
	private Date modifyDateTime;
	
	public CarPoolingTblVO(){}

	public int getPoolId() {
		return poolId;
	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}

	public int getYouWillBe() {
		return youWillBe;
	}

	public void setYouWillBe(int youWillBe) {
		this.youWillBe = youWillBe;
	}

	public int getReturnTrip() {
		return returnTrip;
	}

	public void setReturnTrip(int returnTrip) {
		this.returnTrip = returnTrip;
	}

	public String getTripFromCity() {
		return tripFromCity;
	}

	public void setTripFromCity(String tripFromCity) {
		this.tripFromCity = tripFromCity;
	}

	public String getTripToCity() {
		return tripToCity;
	}

	public void setTripToCity(String tripToCity) {
		this.tripToCity = tripToCity;
	}

	public Date getTripStartDate() {
		return tripStartDate;
	}

	public void setTripStartDate(Date tripStartDate) {
		this.tripStartDate = tripStartDate;
	}

	public Date getTripEndDate() {
		return tripEndDate;
	}

	public void setTripEndDate(Date tripEndDate) {
		this.tripEndDate = tripEndDate;
	}

	public String getTripfromPlaceId() {
		return tripfromPlaceId;
	}

	public void setTripfromPlaceId(String tripfromPlaceId) {
		this.tripfromPlaceId = tripfromPlaceId;
	}

	public String getTriptoPlaceId() {
		return triptoPlaceId;
	}

	public void setTriptoPlaceId(String triptoPlaceId) {
		this.triptoPlaceId = triptoPlaceId;
	}

	public CarMasterTblVO getCarId() {
		return carId;
	}

	public void setCarId(CarMasterTblVO carId) {
		this.carId = carId;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public int getTripFrequency() {
		return tripFrequency;
	}

	public void setTripFrequency(int tripFrequency) {
		this.tripFrequency = tripFrequency;
	}

	public String getAdditionalinfo() {
		return additionalinfo;
	}

	public void setAdditionalinfo(String additionalinfo) {
		this.additionalinfo = additionalinfo;
	}

	public int getSeatsAvaliable() {
		return seatsAvaliable;
	}

	public void setSeatsAvaliable(int seatsAvaliable) {
		this.seatsAvaliable = seatsAvaliable;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GroupMasterTblVo getEntryByGroup() {
		return entryByGroup;
	}

	public void setEntryByGroup(GroupMasterTblVo entryByGroup) {
		this.entryByGroup = entryByGroup;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
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
	

}
