package com.pack.expencevo;
// default package
// Generated Aug 17, 2016 5:12:09 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MvpExpenceTbl generated by hbm2java
 */
public class ExpenceTblVO implements java.io.Serializable {

	private Integer expnId;
	private String expenceFor;
	private Integer noOfProduct;
	private Double productPerAmt;
	private Double expenceTotAmt;
	private String descr;
	private Integer expenceStatus;
	private Integer entryByGrp;
	private Integer entryBy;
	private Date entryDatetime;
	private Date modifyDatetime;
	
	private UserMasterTblVo usrid;
	

	

	public Integer getExpnId() {
		return this.expnId;
	}

	public void setExpnId(Integer expnId) {
		this.expnId = expnId;
	}

	public String getExpenceFor() {
		return this.expenceFor;
	}

	public void setExpenceFor(String expenceFor) {
		this.expenceFor = expenceFor;
	}

	public Integer getNoOfProduct() {
		return this.noOfProduct;
	}

	public void setNoOfProduct(Integer noOfProduct) {
		this.noOfProduct = noOfProduct;
	}

	
	public Double getProductPerAmt() {
		return productPerAmt;
	}

	public void setProductPerAmt(Double productPerAmt) {
		this.productPerAmt = productPerAmt;
	}

	public Double getExpenceTotAmt() {
		return expenceTotAmt;
	}

	public void setExpenceTotAmt(Double expenceTotAmt) {
		this.expenceTotAmt = expenceTotAmt;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getExpenceStatus() {
		return this.expenceStatus;
	}

	public void setExpenceStatus(Integer expenceStatus) {
		this.expenceStatus = expenceStatus;
	}

	public Integer getEntryByGrp() {
		return this.entryByGrp;
	}

	public void setEntryByGrp(Integer entryByGrp) {
		this.entryByGrp = entryByGrp;
	}

	

	

	public Integer getEntryBy() {
		return entryBy;
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

	public UserMasterTblVo getUsrid() {
		return usrid;
	}

	public void setUsrid(UserMasterTblVo usrid) {
		this.usrid = usrid;
	}
	
	

}
