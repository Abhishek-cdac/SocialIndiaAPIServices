package com.pack.commonvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

public class FlatMasterTblVO implements Serializable {

	private Integer flat_id;
	private UserMasterTblVo usrid;
	private String wingsname;
	private String flatno;
	private Integer status;
	private Integer entryby;
	private Date entryDatetime;
	private Date modifyDatetime;
	private Integer ivrBnismyself;

	public Integer getFlat_id() {
		return flat_id;
	}

	public void setFlat_id(Integer flat_id) {
		this.flat_id = flat_id;
	}



	public UserMasterTblVo getUsrid() {
		return usrid;
	}

	public void setUsrid(UserMasterTblVo usrid) {
		this.usrid = usrid;
	}

	public String getWingsname() {
		return wingsname;
	}

	public void setWingsname(String wingsname) {
		this.wingsname = wingsname;
	}

	public String getFlatno() {
		return flatno;
	}

	public void setFlatno(String flatno) {
		this.flatno = flatno;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEntryby() {
		return entryby;
	}

	public void setEntryby(Integer entryby) {
		this.entryby = entryby;
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

	public Integer getIvrBnismyself() {
		return ivrBnismyself;
	}

	public void setIvrBnismyself(Integer ivrBnismyself) {
		this.ivrBnismyself = ivrBnismyself;
	}

	
}
