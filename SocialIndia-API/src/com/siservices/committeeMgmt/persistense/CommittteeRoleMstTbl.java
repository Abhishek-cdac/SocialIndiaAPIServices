package com.siservices.committeeMgmt.persistense;

// default package
// Generated Jul 2, 2016 4:59:50 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * CommittteeRoleMstTbl generated by hbm2java
 */
public class CommittteeRoleMstTbl implements java.io.Serializable {

	private Integer roleId;
	private String roleName;
	private String roleDescp;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	private Integer status;
	public CommittteeRoleMstTbl() {
	}

	public CommittteeRoleMstTbl(String roleDescp, Date modifyDatetime) {
		this.roleDescp = roleDescp;
		this.modifyDatetime = modifyDatetime;
	}

	public CommittteeRoleMstTbl(String roleName, String roleDescp,
			Integer entryBy, Date entryDatetime, Date modifyDatetime) {
		this.roleName = roleName;
		this.roleDescp = roleDescp;
		this.entryBy = entryBy;
		this.entryDatetime = entryDatetime;
		this.modifyDatetime = modifyDatetime;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescp() {
		return this.roleDescp;
	}

	public void setRoleDescp(String roleDescp) {
		this.roleDescp = roleDescp;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	

}