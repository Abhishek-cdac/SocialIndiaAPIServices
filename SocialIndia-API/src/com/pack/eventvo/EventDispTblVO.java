package com.pack.eventvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;

public class EventDispTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnEVENT_DISP_ID;
	//private Integer ivrBnEVENT_ID;
	private UserMasterTblVo ivrBnUAMMSTtbvoobj;
	private GroupMasterTblVo ivrBnGROUPMSTvoobj;
	private Integer ivrBnEVENTSTATUS;
	private Integer ivrBnENTRY_BY;
	private Date ivrBnENTRY_DATETIME;
	private float contributeAmount;
	private int rsvpFlag;
	private EventTblVO ivrBnEVENT_ID;

	
	
	public EventTblVO getIvrBnEVENT_ID() {
		return ivrBnEVENT_ID;
	}

	public void setIvrBnEVENT_ID(EventTblVO ivrBnEVENT_ID) {
		this.ivrBnEVENT_ID = ivrBnEVENT_ID;
	}

	public Integer getIvrBnEVENT_DISP_ID() {
		return ivrBnEVENT_DISP_ID;
	}

	public void setIvrBnEVENT_DISP_ID(Integer ivrBnEVENT_DISP_ID) {
		this.ivrBnEVENT_DISP_ID = ivrBnEVENT_DISP_ID;
	}

/*	public Integer getIvrBnEVENT_ID() {
		return ivrBnEVENT_ID;
	}

	public void setIvrBnEVENT_ID(Integer ivrBnEVENT_ID) {
		this.ivrBnEVENT_ID = ivrBnEVENT_ID;
	}*/

	public Integer getIvrBnEVENTSTATUS() {
		return ivrBnEVENTSTATUS;
	}

	public void setIvrBnEVENTSTATUS(Integer ivrBnEVENTSTATUS) {
		this.ivrBnEVENTSTATUS = ivrBnEVENTSTATUS;
	}

	public Integer getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}

	public void setIvrBnENTRY_BY(Integer ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}

	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}

	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}

	public UserMasterTblVo getIvrBnUAMMSTtbvoobj() {
		return ivrBnUAMMSTtbvoobj;
	}

	public void setIvrBnUAMMSTtbvoobj(UserMasterTblVo ivrBnUAMMSTtbvoobj) {
		this.ivrBnUAMMSTtbvoobj = ivrBnUAMMSTtbvoobj;
	}

	public GroupMasterTblVo getIvrBnGROUPMSTvoobj() {
		return ivrBnGROUPMSTvoobj;
	}

	public void setIvrBnGROUPMSTvoobj(GroupMasterTblVo ivrBnGROUPMSTvoobj) {
		this.ivrBnGROUPMSTvoobj = ivrBnGROUPMSTvoobj;
	}

	public float getContributeAmount() {
		return contributeAmount;
	}

	public void setContributeAmount(float contributeAmount) {
		this.contributeAmount = contributeAmount;
	}

	public int getRsvpFlag() {
		return rsvpFlag;
	}

	public void setRsvpFlag(int rsvpFlag) {
		this.rsvpFlag = rsvpFlag;
	}
	

}
