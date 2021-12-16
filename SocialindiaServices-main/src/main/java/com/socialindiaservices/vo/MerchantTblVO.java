package com.socialindiaservices.vo;

import java.util.Date;

import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.socialindiaservices.common.CommonUtils;

public class MerchantTblVO {
	CommonUtils common=new CommonUtils();
	
	private Integer mrchntId;
	private GroupMasterTblVo groupCode;
//	private PostalCodeMasterTblVO pstlId;
	private Integer pstlId;
	private MerchantCategoryTblVO mrchCategoryId;
	private String mrchntName;
	private String mrchntFname;
	private String mrchntLname;
	private String mrchntEmail;
	private String mrchntPswd;
	private String mrchntPhNo;
	private String mrchntIsdCode;
	private String mrchntAdd1;
	private String mrchntAdd2;
	private CityMasterTblVO cityId;
	private StateMasterTblVO stateId;
	private CountryMasterTblVO countryId;
	private String keyForSearch;
	private String storeLocation;
	private String storeOpentime;
	private String merchDescription;
	private String storeClosetime;
	private String website;
	private String shopName;
	private Integer mrchntActSts;
	private UserMasterTblVo entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	private String countryName;
	private String stateName;
	private String cityName;
	private String entryDatetimeFormat;
	
	private String storeimage;
	private float rating;
	

	public MerchantTblVO() {
	}

	public Integer getMrchntId() {
		return mrchntId;
	}

	public void setMrchntId(Integer mrchntId) {
		this.mrchntId = mrchntId;
	}

	public GroupMasterTblVo getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(GroupMasterTblVo groupCode) {
		this.groupCode = groupCode;
	}

//	public PostalCodeMasterTblVO getPstlId() {
//		return pstlId;
//	}
//
//	public void setPstlId(PostalCodeMasterTblVO pstlId) {
//		this.pstlId = pstlId;
//	}

	public MerchantCategoryTblVO getMrchCategoryId() {
		return mrchCategoryId;
	}

	public void setMrchCategoryId(MerchantCategoryTblVO mrchCategoryId) {
		this.mrchCategoryId = mrchCategoryId;
	}

	public String getMrchntName() {
		return mrchntName;
	}

	public void setMrchntName(String mrchntName) {
		this.mrchntName = mrchntName;
	}

	public String getMrchntFname() {
		return mrchntFname;
	}

	public void setMrchntFname(String mrchntFname) {
		this.mrchntFname = mrchntFname;
	}

	public String getMrchntLname() {
		return mrchntLname;
	}

	public void setMrchntLname(String mrchntLname) {
		this.mrchntLname = mrchntLname;
	}

	public String getMrchntEmail() {
		return mrchntEmail;
	}

	public void setMrchntEmail(String mrchntEmail) {
		this.mrchntEmail = mrchntEmail;
	}

	public String getMrchntPswd() {
		return mrchntPswd;
	}

	public void setMrchntPswd(String mrchntPswd) {
		this.mrchntPswd = mrchntPswd;
	}

	public String getMrchntPhNo() {
		return mrchntPhNo;
	}

	public void setMrchntPhNo(String mrchntPhNo) {
		this.mrchntPhNo = mrchntPhNo;
	}

	public String getMrchntIsdCode() {
		return mrchntIsdCode;
	}

	public void setMrchntIsdCode(String mrchntIsdCode) {
		this.mrchntIsdCode = mrchntIsdCode;
	}

	public String getMrchntAdd1() {
		return mrchntAdd1;
	}

	public void setMrchntAdd1(String mrchntAdd1) {
		this.mrchntAdd1 = mrchntAdd1;
	}

	public String getMrchntAdd2() {
		return mrchntAdd2;
	}

	public void setMrchntAdd2(String mrchntAdd2) {
		this.mrchntAdd2 = mrchntAdd2;
	}

	public CityMasterTblVO getCityId() {
		return cityId;
	}

	public void setCityId(CityMasterTblVO cityId) {
		this.cityId = cityId;
	}

	public StateMasterTblVO getStateId() {
		return stateId;
	}

	public void setStateId(StateMasterTblVO stateId) {
		this.stateId = stateId;
	}

	public CountryMasterTblVO getCountryId() {
		return countryId;
	}

	public void setCountryId(CountryMasterTblVO countryId) {
		this.countryId = countryId;
	}

	public String getKeyForSearch() {
		return keyForSearch;
	}

	public void setKeyForSearch(String keyForSearch) {
		this.keyForSearch = keyForSearch;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	public String getStoreOpentime() {
		return storeOpentime;
	}

	public void setStoreOpentime(String storeOpentime) {
		this.storeOpentime = storeOpentime;
	}

	public String getMerchDescription() {
		return merchDescription;
	}

	public void setMerchDescription(String merchDescription) {
		this.merchDescription = merchDescription;
	}

	public String getStoreClosetime() {
		return storeClosetime;
	}

	public void setStoreClosetime(String storeClosetime) {
		this.storeClosetime = storeClosetime;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getMrchntActSts() {
		return mrchntActSts;
	}

	public void setMrchntActSts(Integer mrchntActSts) {
		this.mrchntActSts = mrchntActSts;
	}

	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
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

	public String getCountryName() {
		if(countryId!=null){
			countryName=countryId.getCountryName();
		}else{
			countryName="";
		}
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		if(stateId!=null){
			stateName=stateId.getStateName();
		}else{
			stateName="";
		}
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		if(cityId!=null){
			cityName=cityId.getCityName();
		}else{
			cityName="";
		}
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getEntryDatetimeFormat() {
		if(entryDatetime!=null){
			entryDatetimeFormat=common.dateToString(entryDatetime, "dd-MM-yyyy hh:mm:ss");
		}else{
			entryDatetimeFormat="";
		}
		return entryDatetimeFormat;
	}

	public void setEntryDatetimeFormat(String entryDatetimeFormat) {
		this.entryDatetimeFormat = entryDatetimeFormat;
	}

	public String getStoreimage() {
		return storeimage;
	}

	public void setStoreimage(String storeimage) {
		this.storeimage = storeimage;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public Integer getPstlId() {
		return pstlId;
	}

	public void setPstlId(Integer pstlId) {
		this.pstlId = pstlId;
	}
	

	
}
