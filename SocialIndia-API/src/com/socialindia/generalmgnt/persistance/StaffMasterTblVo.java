package com.socialindia.generalmgnt.persistance;

import java.util.Date;

import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

public class StaffMasterTblVo {
	private int staffID;
	  private String staffName;
	  private String staffEmail;
	  private String staffGender;
	  private String staffage;
	  private String staffMobno;
	  private String staffAddr;
	  private Integer staffCountry;
	  private Integer staffState;
	  private Integer staffCity;
	  private IDCardMasterTblVO iVOcradid;
	  private String staffIdcardno;
	  private int status;
	  private UserMasterTblVo entryby;
	  private Date entryDatetime;
	  private Date modifyDatetime; 
//	 // private UserMasterTblVo entryBy;
	 private CityMasterTblVO cityId;
	 private String ISDcode;
	 private Integer Workinghours;
	 private StaffCategoryMasterTblVO iVOstaffcategid;
//	 private PostalCodeMasterTblVO pstlId;
	 private Integer pstlId;
	
	 private String imageNameWeb;
	  private String imageNameMobile;
	private CompanyMstTblVO companyId;
	
	
	
	public CompanyMstTblVO getCompanyId() {
		return companyId;
	}
	public void setCompanyId(CompanyMstTblVO companyId) {
		this.companyId = companyId;
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
	public int getStaffID() {
		return staffID;
	}
	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffEmail() {
		return staffEmail;
	}
	public void setStaffEmail(String staffEmail) {
		this.staffEmail = staffEmail;
	}
	public String getStaffGender() {
		return staffGender;
	}
	public void setStaffGender(String staffGender) {
		this.staffGender = staffGender;
	}
	public String getStaffage() {
		return staffage;
	}
	public void setStaffage(String staffage) {
		this.staffage = staffage;
	}
	public String getStaffMobno() {
		return staffMobno;
	}
	public void setStaffMobno(String staffMobno) {
		this.staffMobno = staffMobno;
	}
	public String getStaffAddr() {
		return staffAddr;
	}
	public void setStaffAddr(String staffAddr) {
		this.staffAddr = staffAddr;
	}
	public Integer getStaffCountry() {
		return staffCountry;
	}
	public void setStaffCountry(Integer staffCountry) {
		this.staffCountry = staffCountry;
	}
	public Integer getStaffState() {
		return staffState;
	}
	public void setStaffState(Integer staffState) {
		this.staffState = staffState;
	}
	public Integer getStaffCity() {
		return staffCity;
	}
	public void setStaffCity(Integer staffCity) {
		this.staffCity = staffCity;
	}
	public IDCardMasterTblVO getiVOcradid() {
		return iVOcradid;
	}
	public void setiVOcradid(IDCardMasterTblVO iVOcradid) {
		this.iVOcradid = iVOcradid;
	}
	public String getStaffIdcardno() {
		return staffIdcardno;
	}
	public void setStaffIdcardno(String staffIdcardno) {
		this.staffIdcardno = staffIdcardno;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UserMasterTblVo getEntryby() {
		return entryby;
	}
	public void setEntryby(UserMasterTblVo entryby) {
		this.entryby = entryby;
	}
	public CityMasterTblVO getCityId() {
		return cityId;
	}
	public void setCityId(CityMasterTblVO cityId) {
		this.cityId = cityId;
	}
	public String getISDcode() {
		return ISDcode;
	}
	public void setISDcode(String iSDcode) {
		ISDcode = iSDcode;
	}
	public Integer getWorkinghours() {
		return Workinghours;
	}
	public void setWorkinghours(Integer workinghours) {
		Workinghours = workinghours;
	}
	public StaffCategoryMasterTblVO getiVOstaffcategid() {
		return iVOstaffcategid;
	}
	public void setiVOstaffcategid(StaffCategoryMasterTblVO iVOstaffcategid) {
		this.iVOstaffcategid = iVOstaffcategid;
	}
//	public PostalCodeMasterTblVO getPstlId() {
//		return pstlId;
//	}
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
//	public CityMasterTblVO getCityIdobj() {
//		return cityIdobj;
//	}
//	public void setCityIdobj(CityMasterTblVO cityIdobj) {
//		this.cityIdobj = cityIdobj;
//	}
	public Integer getPstlId() {
		return pstlId;
	}
	public void setPstlId(Integer pstlId) {
		this.pstlId = pstlId;
	}
}
