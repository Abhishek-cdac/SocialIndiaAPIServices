package com.pack.commonvo;

import java.io.Serializable;

public class CategoryMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iVOCATEGORY_ID;
	private String iVOCATEGORY_NAME;
	private Integer iVOACT_STAT;
	private String categoryImageName;
	private String categoryDescription;
	private String iVOCTG_SHTFORM;
	private Integer ivrCatetype;
	public Integer getiVOCATEGORY_ID() {
		return iVOCATEGORY_ID;
	}
	public void setiVOCATEGORY_ID(Integer iVOCATEGORY_ID) {
		this.iVOCATEGORY_ID = iVOCATEGORY_ID;
	}
	public String getiVOCATEGORY_NAME() {
		return iVOCATEGORY_NAME;
	}
	public void setiVOCATEGORY_NAME(String iVOCATEGORY_NAME) {
		this.iVOCATEGORY_NAME = iVOCATEGORY_NAME;
	}
	public Integer getiVOACT_STAT() {
		return iVOACT_STAT;
	}
	public void setiVOACT_STAT(Integer iVOACT_STAT) {
		this.iVOACT_STAT = iVOACT_STAT;
	}
	public String getiVOCTG_SHTFORM() {
		return iVOCTG_SHTFORM;
	}
	public void setiVOCTG_SHTFORM(String iVOCTG_SHTFORM) {
		this.iVOCTG_SHTFORM = iVOCTG_SHTFORM;
	}
	
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	public String getCategoryImageName() {
		return categoryImageName;
	}
	public void setCategoryImageName(String categoryImageName) {
		this.categoryImageName = categoryImageName;
	}
	public Integer getIvrCatetype() {
		return ivrCatetype;
	}
	public void setIvrCatetype(Integer ivrCatetype) {
		this.ivrCatetype = ivrCatetype;
	}
	
	
}
