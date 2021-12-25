package com.pack.commonvo;

import java.io.Serializable;

public class CityMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int cityId;
	private StateMasterTblVO stateId;
	private String cityName;
	
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public StateMasterTblVO getStateId() {
		return stateId;
	}
	public void setStateId(StateMasterTblVO stateId) {
		this.stateId = stateId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
