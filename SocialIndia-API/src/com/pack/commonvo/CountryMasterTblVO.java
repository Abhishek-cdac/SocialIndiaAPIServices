package com.pack.commonvo;

import java.io.Serializable;

public class CountryMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int countryId;
	private String countryName;
	private String alphaCode;
	private Integer numericCode;	
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getAlphaCode() {
		return alphaCode;
	}
	public void setAlphaCode(String alphaCode) {
		this.alphaCode = alphaCode;
	}
	public Integer getNumericCode() {
		return numericCode;
	}
	public void setNumericCode(Integer numericCode) {
		this.numericCode = numericCode;
	}	
	
}
