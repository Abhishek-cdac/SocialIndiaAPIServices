package com.siservices.uam.persistense;

import java.io.Serializable;
import java.util.Date;

public class MenuMasterTblVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int menuId;
	private String menuDesc;
	private String menuPath;
	private String menuType;
	private int subMenuId;
	private String rootDesc;
	private int entryBy;
	private Integer updateBy;
	private Date entryTime;
	private Date updateTime;
	private String menuClass;

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getMenuPath() {
		return menuPath;
	}

	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public int getSubMenuId() {
		return subMenuId;
	}

	public void setSubMenuId(int subMenuId) {
		this.subMenuId = subMenuId;
	}

	public String getRootDesc() {
		return rootDesc;
	}

	public void setRootDesc(String rootDesc) {
		this.rootDesc = rootDesc;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(int entryBy) {
		this.entryBy = entryBy;
	}

	public String getMenuClass() {
		return menuClass;
	}

	public void setMenuClass(String menuClass) {
		this.menuClass = menuClass;
	}

}
