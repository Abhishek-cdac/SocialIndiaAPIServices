package com.siservices.uam.persistense;


import java.io.Serializable;
import java.util.Date;


public class RightsMasterTblVo implements Serializable {

  private static final long serialVersionUID = 1L;
  private int rightsId;
  private Integer rightsAdd;
  private Integer rightsEdit;
  private Integer rightsDelete;
  private Integer rightsView;
  private Integer updateBy;
  private Date entryDatetime;
  private Date modifyDatetime;
  private int entryBy;
  private GroupMasterTblVo groupCode;
  private MenuMasterTblVo menuId;

  public int getRightsId() {
    return rightsId;
  }

  public void setRightsId(int rightsId) {
    this.rightsId = rightsId;
  }

  public Integer getRightsAdd() {
    return rightsAdd;
  }

  public void setRightsAdd(Integer rightsAdd) {
    this.rightsAdd = rightsAdd;
  }

  public Integer getRightsEdit() {
    return rightsEdit;
  }

  public void setRightsEdit(Integer rightsEdit) {
    this.rightsEdit = rightsEdit;
  }

  public Integer getRightsDelete() {
    return rightsDelete;
  }

  public void setRightsDelete(Integer rightsDelete) {
    this.rightsDelete = rightsDelete;
  }

  public Integer getRightsView() {
    return rightsView;
  }

  public void setRightsView(Integer rightsView) {
    this.rightsView = rightsView;
  }

  public Integer getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(Integer updateBy) {
    this.updateBy = updateBy;
  }

  public int getEntryBy() {
    return entryBy;
  }

  public void setEntryBy(int ival) {
    this.entryBy = ival;
  }

  public GroupMasterTblVo getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(GroupMasterTblVo groupCode) {
    this.groupCode = groupCode;
  }

  public MenuMasterTblVo getMenuId() {
    return menuId;
  }

  public void setMenuId(MenuMasterTblVo menuId) {
    this.menuId = menuId;
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

}
