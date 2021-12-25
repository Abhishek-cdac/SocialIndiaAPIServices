package com.pack.commonvo;

import java.io.Serializable;
import java.util.Date;

public class CompanyMstTblVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer companyId;
	private StateMasterTblVO stateId;
//	private PostalCodeMasterTblVO pstlId;
	private Integer pstlId;
	private CityMasterTblVO cityId;
	private CountryMasterTblVO countryId;
	private String companyName;
	private String companyUniqId;
	private String companyEmail;
	private String address1;
	private String address2;
	private String isdCode;
	private String mobileNo;
	private String cmpnyRegno;
	private String keyforSrch;
	private Integer verifyStatus;
	private String descr;
	private String imageNameMob;
	private String imageNameWeb;
	private Integer status;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;

	

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	
	

	public StateMasterTblVO getStateId() {
		return stateId;
	}

	public void setStateId(StateMasterTblVO stateId) {
		this.stateId = stateId;
	}

//	public PostalCodeMasterTblVO getPstlId() {
//		return pstlId;
//	}
//
//	public void setPstlId(PostalCodeMasterTblVO pstlId) {
//		this.pstlId = pstlId;
//	}

	public CityMasterTblVO getCityId() {
		return cityId;
	}

	public void setCityId(CityMasterTblVO cityId) {
		this.cityId = cityId;
	}

	public CountryMasterTblVO getCountryId() {
		return countryId;
	}

	public void setCountryId(CountryMasterTblVO countryId) {
		this.countryId = countryId;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyUniqId() {
		return this.companyUniqId;
	}

	public void setCompanyUniqId(String companyUniqId) {
		this.companyUniqId = companyUniqId;
	}

	public String getCompanyEmail() {
		return this.companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getIsdCode() {
		return this.isdCode;
	}

	public void setIsdCode(String isdCode) {
		this.isdCode = isdCode;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getCmpnyRegno() {
		return this.cmpnyRegno;
	}

	public void setCmpnyRegno(String cmpnyRegno) {
		this.cmpnyRegno = cmpnyRegno;
	}

	public String getKeyforSrch() {
		return this.keyforSrch;
	}

	public void setKeyforSrch(String keyforSrch) {
		this.keyforSrch = keyforSrch;
	}

	public Integer getVerifyStatus() {
		return this.verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getImageNameMob() {
		return this.imageNameMob;
	}

	public void setImageNameMob(String imageNameMob) {
		this.imageNameMob = imageNameMob;
	}

	public String getImageNameWeb() {
		return this.imageNameWeb;
	}

	public void setImageNameWeb(String imageNameWeb) {
		this.imageNameWeb = imageNameWeb;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
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

	public Integer getPstlId() {
		return pstlId;
	}

	public void setPstlId(Integer pstlId) {
		this.pstlId = pstlId;
	}
}
