package com.siservices.uam.persistense;


import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;



public class GroupMasterTblVo implements Serializable {

  private static final long serialVersionUID = 1L;
  private int groupCode;
  private String groupName;
  private String groupAliasName;
  private String statusFlag;
  private int accessMedia;  
  private UserMasterTblVo entryBy;
  private Integer updateBy;
  private Date entryDatetime;
  private Date modifyDatetime;

  
  public String getGroupAliasName() {
	return groupAliasName;
}

public void setGroupAliasName(String groupAliasName) {
	this.groupAliasName = groupAliasName;
}

public int getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(int groupCode) {
    this.groupCode = groupCode;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getStatusFlag() {
    return statusFlag;
  }

  public void setStatusFlag(String statusFlag) {
    this.statusFlag = statusFlag;
  }

  public UserMasterTblVo getEntryBy() {
    return entryBy;
  }

  public void setEntryBy(UserMasterTblVo entryBy) {
    this.entryBy = entryBy;
  }

  public Integer getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(Integer updateBy) {
    this.updateBy = updateBy;
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

public int getAccessMedia() {
	return accessMedia;
}

public void setAccessMedia(int accessMedia) {
	this.accessMedia = accessMedia;
}

}
