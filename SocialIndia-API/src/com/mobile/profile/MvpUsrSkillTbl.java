package com.mobile.profile;

// default package
// Generated Sep 27, 2016 7:58:17 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MvpUsrSkillTbl generated by hbm2java
 */
public class MvpUsrSkillTbl implements java.io.Serializable {

	private Integer uniqueId;
	private SkillMasterTblVO ivrBnSKILL_ID;
	private String skillName;
	private UserMasterTblVo userId;
	private CategoryMasterTblVO iVOCATEGORY_ID;
	private Integer skillSts;
	private Date enrtyDatetime;
	private Date modifyDatetime;

	public MvpUsrSkillTbl() {
	}

	public MvpUsrSkillTbl(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public MvpUsrSkillTbl(SkillMasterTblVO mvpSkillTbl, UserMasterTblVo usrRegTbl,
			CategoryMasterTblVO categoryMstTbl, Integer skillSts,
			Date enrtyDatetime, Date modifyDatetime) {
		this.ivrBnSKILL_ID = ivrBnSKILL_ID;
		this.userId = userId;
		this.iVOCATEGORY_ID = iVOCATEGORY_ID;
		this.skillSts = skillSts;
		this.enrtyDatetime = enrtyDatetime;
		this.modifyDatetime = modifyDatetime;
	}

	public Integer getUniqueId() {
		return this.uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	

	public Integer getSkillSts() {
		return this.skillSts;
	}

	public void setSkillSts(Integer skillSts) {
		this.skillSts = skillSts;
	}

	public Date getEnrtyDatetime() {
		return this.enrtyDatetime;
	}

	public void setEnrtyDatetime(Date enrtyDatetime) {
		this.enrtyDatetime = enrtyDatetime;
	}

	public Date getModifyDatetime() {
		return this.modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	public SkillMasterTblVO getIvrBnSKILL_ID() {
		return ivrBnSKILL_ID;
	}

	public void setIvrBnSKILL_ID(SkillMasterTblVO ivrBnSKILL_ID) {
		this.ivrBnSKILL_ID = ivrBnSKILL_ID;
	}

	public UserMasterTblVo getUserId() {
		return userId;
	}

	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}

	public CategoryMasterTblVO getiVOCATEGORY_ID() {
		return iVOCATEGORY_ID;
	}

	public void setiVOCATEGORY_ID(CategoryMasterTblVO iVOCATEGORY_ID) {
		this.iVOCATEGORY_ID = iVOCATEGORY_ID;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

}
