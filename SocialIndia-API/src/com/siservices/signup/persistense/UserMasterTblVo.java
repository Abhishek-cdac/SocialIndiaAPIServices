package com.siservices.signup.persistense;


import java.io.Serializable;
import java.util.Date;

import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.KnownusTblVO;
import com.pack.commonvo.MvpBloodGroupTbl;
import com.pack.commonvo.MvpTitleMstTbl;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.SocietyMstTbl;



public class UserMasterTblVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int userId;
	private String userName;
	private String lastName;
	private String firstName;
	private String password;
	private String encryprPassword;
	private String flatNo;
	private String dob;
	private String mobileNo;
	private String bloodType;
	private String emailId;
	private String address1;
	private String address2;
	private String gender;
	// private int idCardType;
	private String idProofNo;
	private String occupation;
	private int membersInFamily;
	private int loginCount;
	private Integer statusFlag;
	private Date loginDatetime;
	private int entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	private String groupUniqId;
	private String pinCode;
	private String noofblocks;
	private int accessChannel;
	private int noofflats;

	private String isdCode;
	private String imageNameWeb;
	private String imageNameMobile;

	private GroupMasterTblVo groupCode;
	private CountryMasterTblVO countryId;
	private StateMasterTblVO stateId;
	private CityMasterTblVO cityId;
	private SocietyMstTbl societyId;
//	private PostalCodeMasterTblVO pstlId;
	private Integer pstlId;
	private CommittteeRoleMstTbl roleId;
	private IDCardMasterTblVO iVOcradid;

	private Integer parentId;
	private Integer parentChildFlag;
	private Integer ivrBnISONLINEFLG;
	private Integer ivrBnPASSWORD_FLAG;
	private Integer loggedOut;
	private Integer numOfLogins = 2;
	private Integer currentLogins = 0;
	private Date resetDatetime;
	
	private Integer socialType;
	private String socialId;
	private String socialProfileUrl;
  
	private Integer mobileOtpVerifyFlag;
	private Integer emailVerifyFlag;
  
  
	private KnownusTblVO iVOKNOWN_US_ID;
	private MvpBloodGroupTbl bloodGroupId;
	private MvpTitleMstTbl titleId;
	private MvpFamilymbrTbl fmbrTntId;

	private String lvrDispname;
	
	public UserMasterTblVo() {
	}

	public Integer getIvrBnISONLINEFLG() {
		return ivrBnISONLINEFLG;
	}

	public void setIvrBnISONLINEFLG(Integer ivrBnISONLINEFLG) {
		this.ivrBnISONLINEFLG = ivrBnISONLINEFLG;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	public Date getLoginDatetime() {
		return loginDatetime;
	}

	public void setLoginDatetime(Date loginDatetime) {
		this.loginDatetime = loginDatetime;
	}

	public int getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(int entryBy) {
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

	public GroupMasterTblVo getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(GroupMasterTblVo groupCode) {
		this.groupCode = groupCode;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		lvrDispname += " "+ lastName;
		if(Commonutility.checkempty(lvrDispname)) {
			lvrDispname =lvrDispname.trim();
		}
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdProofNo() {
		return idProofNo;
	}

	public void setIdProofNo(String idProofNo) {
		this.idProofNo = idProofNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		lvrDispname = firstName;
		if(Commonutility.checkempty(lvrDispname)) {
			lvrDispname =lvrDispname.trim();
		}
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public int getMembersInFamily() {
		return membersInFamily;
	}

	public void setMembersInFamily(int membersInFamily) {
		this.membersInFamily = membersInFamily;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public String getGroupUniqId() {
		return groupUniqId;
	}

	public void setGroupUniqId(String groupUniqId) {
		this.groupUniqId = groupUniqId;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public SocietyMstTbl getSocietyId() {
		return societyId;
	}

	public void setSocietyId(SocietyMstTbl societyId) {
		this.societyId = societyId;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public CountryMasterTblVO getCountryId() {
		return countryId;
	}

	public void setCountryId(CountryMasterTblVO countryId) {
		this.countryId = countryId;
	}

	public StateMasterTblVO getStateId() {
		return stateId;
	}

	public void setStateId(StateMasterTblVO stateId) {
		this.stateId = stateId;
	}

	public CityMasterTblVO getCityId() {
		return cityId;
	}

	public void setCityId(CityMasterTblVO cityId) {
		this.cityId = cityId;
	}

//	public PostalCodeMasterTblVO getPstlId() {
//		return pstlId;
//	}
//
//	public void setPstlId(PostalCodeMasterTblVO pstlId) {
//		this.pstlId = pstlId;
//	}

	public String getImageNameWeb() {
		return imageNameWeb;
	}

	public void setImageNameWeb(String imageNameWeb) {
		this.imageNameWeb = imageNameWeb;
	}

	public String getImageNameMobile() {
		return imageNameMobile;
	}

	public void setImageNameMobile(String imageNameMobile) {
		this.imageNameMobile = imageNameMobile;
	}

	public CommittteeRoleMstTbl getRoleId() {
		return roleId;
	}

	public void setRoleId(CommittteeRoleMstTbl roleId) {
		this.roleId = roleId;
	}

	public String getNoofblocks() {
		return noofblocks;
	}

	public void setNoofblocks(String noofblocks) {
		this.noofblocks = noofblocks;
	}

	public int getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(int accessChannel) {
		this.accessChannel = accessChannel;
	}

	public int getNoofflats() {
		return noofflats;
	}

	public void setNoofflats(int noofflats) {
		this.noofflats = noofflats;
	}

	public IDCardMasterTblVO getiVOcradid() {
		return iVOcradid;
	}

	public void setiVOcradid(IDCardMasterTblVO iVOcradid) {
		this.iVOcradid = iVOcradid;
	}

	public String getIsdCode() {
		return isdCode;
	}

	public void setIsdCode(String isdCode) {
		this.isdCode = isdCode;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getParentChildFlag() {
		return parentChildFlag;
	}

	public void setParentChildFlag(Integer parentChildFlag) {
		this.parentChildFlag = parentChildFlag;
	}

	public String getEncryprPassword() {
		return encryprPassword;
	}

	public void setEncryprPassword(String encryprPassword) {
		this.encryprPassword = encryprPassword;
	}

	public Integer getIvrBnPASSWORD_FLAG() {
		return ivrBnPASSWORD_FLAG;
	}

	public void setIvrBnPASSWORD_FLAG(Integer ivrBnPASSWORD_FLAG) {
		this.ivrBnPASSWORD_FLAG = ivrBnPASSWORD_FLAG;
	}

	public Integer getSocialType() {
		return socialType;
	}

	public void setSocialType(Integer socialType) {
		this.socialType = socialType;
	}


	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getSocialProfileUrl() {
		return socialProfileUrl;
	}

	public void setSocialProfileUrl(String socialProfileUrl) {
		this.socialProfileUrl = socialProfileUrl;
	}

	public Integer getMobileOtpVerifyFlag() {
		return mobileOtpVerifyFlag;
	}

	public void setMobileOtpVerifyFlag(Integer mobileOtpVerifyFlag) {
		this.mobileOtpVerifyFlag = mobileOtpVerifyFlag;
	}

	public Integer getEmailVerifyFlag() {
		return emailVerifyFlag;
	}

	public void setEmailVerifyFlag(Integer emailVerifyFlag) {
		this.emailVerifyFlag = emailVerifyFlag;
	}

	public KnownusTblVO getiVOKNOWN_US_ID() {
		return iVOKNOWN_US_ID;
	}

	public void setiVOKNOWN_US_ID(KnownusTblVO iVOKNOWN_US_ID) {
		this.iVOKNOWN_US_ID = iVOKNOWN_US_ID;
	}

	public MvpBloodGroupTbl getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(MvpBloodGroupTbl bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public MvpTitleMstTbl getTitleId() {
		return titleId;
	}

	public void setTitleId(MvpTitleMstTbl titleId) {
		this.titleId = titleId;
	}

	public MvpFamilymbrTbl getFmbrTntId() {
		return fmbrTntId;
	}

	public void setFmbrTntId(MvpFamilymbrTbl fmbrTntId) {
		this.fmbrTntId = fmbrTntId;
	}

	public String getLvrDispname() {
		return lvrDispname;
	}

	public void setLvrDispname(String lvrDispname) {
		this.lvrDispname = lvrDispname;
	}

	public Integer getLoggedOut() {
		return loggedOut;
	}

	public void setLoggedOut(Integer loggedOut) {
		this.loggedOut = loggedOut;
	}

	public Integer getNumOfLogins() {
		return numOfLogins;
	}

	public void setNumOfLogins(Integer numOfLogins) {
		this.numOfLogins = numOfLogins;
	}

	public Integer getCurrentLogins() {
		return currentLogins;
	}

	public void setCurrentLogins(Integer currentLogins) {
		this.currentLogins = currentLogins;
	}

	public Date getResetDatetime() {
		return resetDatetime;
	}

	public void setResetDatetime(Date resetDatetime) {
		this.resetDatetime = resetDatetime;
	}

	public Integer getPstlId() {
		return pstlId;
	}

	public void setPstlId(Integer pstlId) {
		this.pstlId = pstlId;
	}

	@Override
	public String toString() {
		return "UserMasterTblVo [userId=" + userId + ", userName=" + userName + ", lastName=" + lastName
				+ ", firstName=" + firstName + ", password=" + password + ", encryprPassword=" + encryprPassword
				+ ", flatNo=" + flatNo + ", dob=" + dob + ", mobileNo=" + mobileNo + ", bloodType=" + bloodType
				+ ", emailId=" + emailId + ", address1=" + address1 + ", address2=" + address2 + ", gender=" + gender
				+ ", idProofNo=" + idProofNo + ", occupation=" + occupation + ", membersInFamily=" + membersInFamily
				+ ", loginCount=" + loginCount + ", statusFlag=" + statusFlag + ", loginDatetime=" + loginDatetime
				+ ", entryBy=" + entryBy + ", entryDatetime=" + entryDatetime + ", modifyDatetime=" + modifyDatetime
				+ ", groupUniqId=" + groupUniqId + ", pinCode=" + pinCode + ", noofblocks=" + noofblocks
				+ ", accessChannel=" + accessChannel + ", noofflats=" + noofflats + ", isdCode=" + isdCode
				+ ", imageNameWeb=" + imageNameWeb + ", imageNameMobile=" + imageNameMobile + ", groupCode=" + groupCode
				+ ", countryId=" + countryId + ", stateId=" + stateId + ", cityId=" + cityId + ", societyId="
				+ societyId + ", pstlId=" + pstlId + ", roleId=" + roleId + ", iVOcradid=" + iVOcradid + ", parentId="
				+ parentId + ", parentChildFlag=" + parentChildFlag + ", ivrBnISONLINEFLG=" + ivrBnISONLINEFLG
				+ ", ivrBnPASSWORD_FLAG=" + ivrBnPASSWORD_FLAG + ", loggedOut=" + loggedOut + ", numOfLogins="
				+ numOfLogins + ", currentLogins=" + currentLogins + ", resetDatetime=" + resetDatetime
				+ ", socialType=" + socialType + ", socialId=" + socialId + ", socialProfileUrl=" + socialProfileUrl
				+ ", mobileOtpVerifyFlag=" + mobileOtpVerifyFlag + ", emailVerifyFlag=" + emailVerifyFlag
				+ ", iVOKNOWN_US_ID=" + iVOKNOWN_US_ID + ", bloodGroupId=" + bloodGroupId + ", titleId=" + titleId
				+ ", fmbrTntId=" + fmbrTntId + ", lvrDispname=" + lvrDispname + "]";
	}
	
	
}
