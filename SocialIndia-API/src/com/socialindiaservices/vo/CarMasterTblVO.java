package com.socialindiaservices.vo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;

public class CarMasterTblVO implements Serializable{
	
	private int carId;
	private String carModel;
	private String carNumber;
	private String preference;
	private int status;
	private Date entryDateTime;
	private Date modifyDateTime;
	private GroupMasterTblVo entryByGroup;
	private UserMasterTblVo entryBy;
	
	public CarMasterTblVO(){}
	public  CarMasterTblVO(String carModel, String carNumber,String preference,int status){
		this.carModel=carModel;
		this.carNumber=carNumber;
		this.preference=preference;
		this.status=status;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	
	
}
