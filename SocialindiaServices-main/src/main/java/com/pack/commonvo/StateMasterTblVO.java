package com.pack.commonvo;

import java.io.Serializable;

public class StateMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer stateId;
	private CountryMasterTblVO countryId;
	private String stateName;
	
	public Integer getStateId() {
		return stateId;
	}
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	public CountryMasterTblVO getCountryId() {
		return countryId;
	}
	public void setCountryId(CountryMasterTblVO countryId) {
		this.countryId = countryId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
}
