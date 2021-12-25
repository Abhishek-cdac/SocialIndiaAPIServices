package com.pack.enewsvo;

import java.io.Serializable;
import java.util.Date;


public class EeNewsDocTblVO implements Serializable {

	private Integer EnewuniqId;
	private EeNewsTblVO Enewid;
	private String imgname;
	private Integer status;
	private Integer entryBy;
	private Date entryDatetime;
	public Integer getEnewuniqId() {
		return EnewuniqId;
	}
	public void setEnewuniqId(Integer enewuniqId) {
		EnewuniqId = enewuniqId;
	}
	
	public EeNewsTblVO getEnewid() {
		return Enewid;
	}
	public void setEnewid(EeNewsTblVO enewid) {
		Enewid = enewid;
	}
	public String getImgname() {
		return imgname;
	}
	public void setImgname(String imgname) {
		this.imgname = imgname;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(Integer entryBy) {
		this.entryBy = entryBy;
	}
	public Date getEntryDatetime() {
		return entryDatetime;
	}
	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}
	
	
	
}
