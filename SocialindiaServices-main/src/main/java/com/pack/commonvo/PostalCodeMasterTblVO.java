package com.pack.commonvo;

import java.io.Serializable;

public class PostalCodeMasterTblVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer pstlId;
	private CityMasterTblVO cityId;
	private String pstlCode;
	
	
	public Integer getPstlId() {
		return pstlId;
	}
	public void setPstlId(Integer pstlId) {
		this.pstlId = pstlId;
	}
	public CityMasterTblVO getCityId() {
		return cityId;
	}
	public void setCityId(CityMasterTblVO cityId) {
		this.cityId = cityId;
	}
	public String getPstlCode() {
		return pstlCode;
	}
	public void setPstlCode(String pstlCode) {
		this.pstlCode = pstlCode;
	}
	
}
