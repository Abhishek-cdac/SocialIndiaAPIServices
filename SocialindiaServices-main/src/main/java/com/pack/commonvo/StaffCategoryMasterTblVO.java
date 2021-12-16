package com.pack.commonvo;

import java.io.Serializable;

public class StaffCategoryMasterTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iVOstaffcategid;
	private String iVOstaffcategory;
	private Integer iVOstaffcategsts;
	public Integer getiVOstaffcategid() {
		return iVOstaffcategid;
	}
	public void setiVOstaffcategid(Integer iVOstaffcategid) {
		this.iVOstaffcategid = iVOstaffcategid;
	}
	public String getiVOstaffcategory() {
		return iVOstaffcategory;
	}
	public void setiVOstaffcategory(String iVOstaffcategory) {
		this.iVOstaffcategory = iVOstaffcategory;
	}
	public Integer getiVOstaffcategsts() {
		return iVOstaffcategsts;
	}
	public void setiVOstaffcategsts(Integer iVOstaffcategsts) {
		this.iVOstaffcategsts = iVOstaffcategsts;
	}	
	
	
	
}
