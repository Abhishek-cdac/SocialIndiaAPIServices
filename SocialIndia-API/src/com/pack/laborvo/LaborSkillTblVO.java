package com.pack.laborvo;

import java.io.Serializable;
import java.util.Date;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;

public class LaborSkillTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnLBR_SKILL_ID;
	private Integer ivrBnLBR_ID;
	//private Integer ivrBnLBR_CATEG_ID;
	private SkillMasterTblVO ivrBnSKILL_ID;
	private Integer ivrBnLBR_SKILL_STS;
	private Date ivrBnENTRY_DATETIME;

	private CategoryMasterTblVO iVOCATEGORY_ID;

	public Integer getIvrBnLBR_SKILL_ID() {
		return ivrBnLBR_SKILL_ID;
	}
	public void setIvrBnLBR_SKILL_ID(Integer ivrBnLBR_SKILL_ID) {
		this.ivrBnLBR_SKILL_ID = ivrBnLBR_SKILL_ID;
	}
	public Integer getIvrBnLBR_ID() {
		return ivrBnLBR_ID;
	}
	public void setIvrBnLBR_ID(Integer ivrBnLBR_ID) {
		this.ivrBnLBR_ID = ivrBnLBR_ID;
	}
//	public Integer getIvrBnLBR_CATEG_ID() {
//		return ivrBnLBR_CATEG_ID;
//	}
//	public void setIvrBnLBR_CATEG_ID(Integer ivrBnLBR_CATEG_ID) {
//		this.ivrBnLBR_CATEG_ID = ivrBnLBR_CATEG_ID;
//	}

	public Integer getIvrBnLBR_SKILL_STS() {
		return ivrBnLBR_SKILL_STS;
	}
	public SkillMasterTblVO getIvrBnSKILL_ID() {
		return ivrBnSKILL_ID;
	}
	public void setIvrBnSKILL_ID(SkillMasterTblVO ivrBnSKILL_ID) {
		this.ivrBnSKILL_ID = ivrBnSKILL_ID;
	}
	public void setIvrBnLBR_SKILL_STS(Integer ivrBnLBR_SKILL_STS) {
		this.ivrBnLBR_SKILL_STS = ivrBnLBR_SKILL_STS;
	}
	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}
	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}
	public CategoryMasterTblVO getiVOCATEGORY_ID() {
		return iVOCATEGORY_ID;
	}
	public void setiVOCATEGORY_ID(CategoryMasterTblVO iVOCATEGORY_ID) {
		this.iVOCATEGORY_ID = iVOCATEGORY_ID;
	}


}
