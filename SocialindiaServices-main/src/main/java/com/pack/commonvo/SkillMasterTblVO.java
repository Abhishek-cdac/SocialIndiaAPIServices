package com.pack.commonvo;

import java.io.Serializable;
import java.util.Date;

public class SkillMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnSKILL_ID;
	private String ivrBnSKILL_NAME;
	private Integer ivrBnACT_STAT;
	private Date entryDatetime;
	private CategoryMasterTblVO ivrBnCategoryid;
	private String ivrcreatorby;
	private Integer ivrcreateflg;
	
	public Integer getIvrBnSKILL_ID() {
		return ivrBnSKILL_ID;
	}
	public void setIvrBnSKILL_ID(Integer ivrBnSKILL_ID) {
		this.ivrBnSKILL_ID = ivrBnSKILL_ID;
	}
	public String getIvrBnSKILL_NAME() {
		return ivrBnSKILL_NAME;
	}
	public void setIvrBnSKILL_NAME(String ivrBnSKILL_NAME) {
		this.ivrBnSKILL_NAME = ivrBnSKILL_NAME;
	}
	public Integer getIvrBnACT_STAT() {
		return ivrBnACT_STAT;
	}
	public void setIvrBnACT_STAT(Integer ivrBnACT_STAT) {
		this.ivrBnACT_STAT = ivrBnACT_STAT;
	}
	public CategoryMasterTblVO getIvrBnCategoryid() {
		return ivrBnCategoryid;
	}
	public void setIvrBnCategoryid(CategoryMasterTblVO ivrBnCategoryid) {
		this.ivrBnCategoryid = ivrBnCategoryid;
	}
	public Date getEntryDatetime() {
		return entryDatetime;
	}
	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}
	public String getIvrcreatorby() {
		return ivrcreatorby;
	}
	public void setIvrcreatorby(String ivrcreatorby) {
		this.ivrcreatorby = ivrcreatorby;
	}
	public Integer getIvrcreateflg() {
		return ivrcreateflg;
	}
	public void setIvrcreateflg(Integer ivrcreateflg) {
		this.ivrcreateflg = ivrcreateflg;
	}

	
	
}
