package com.mobi.easypayvo;

import java.util.Date;

/**
 * MvpTransactionTbl generated by hbm2java
 */
public class MvpTransactionTbl implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer tranId;
	private String orderNo;
	private String agRef;
	private String pgRef;
	private String session;
	private Integer serviceType;
	private Float txnAmount;
	private Date txnDatetime;
	private Date gmtDatetime;
	private Integer utilityPaymentStatus;
	private Integer pamentGatewayStatus;
	private Integer finalStatus;
	private Integer userId;
	private Integer paymentType;
	private Integer maintenanceId;
	private String remarksMsg;
	private Date entryDateTime;
	private Date modifyDateTime;

	public MvpTransactionTbl() {
	}

	public Integer getTranId() {
		return this.tranId;
	}

	public void setTranId(Integer tranId) {
		this.tranId = tranId;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAgRef() {
		return this.agRef;
	}

	public void setAgRef(String agRef) {
		this.agRef = agRef;
	}

	public String getPgRef() {
		return this.pgRef;
	}

	public void setPgRef(String pgRef) {
		this.pgRef = pgRef;
	}

	public String getSession() {
		return this.session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public Float getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(Float txnAmount) {
		this.txnAmount = txnAmount;
	}

	public Date getTxnDatetime() {
		return this.txnDatetime;
	}

	public void setTxnDatetime(Date txnDatetime) {
		this.txnDatetime = txnDatetime;
	}

	public Date getGmtDatetime() {
		return gmtDatetime;
	}

	public void setGmtDatetime(Date gmtDatetime) {
		this.gmtDatetime = gmtDatetime;
	}

	public Integer getUtilityPaymentStatus() {
		return this.utilityPaymentStatus;
	}

	public void setUtilityPaymentStatus(Integer utilityPaymentStatus) {
		this.utilityPaymentStatus = utilityPaymentStatus;
	}

	public Integer getPamentGatewayStatus() {
		return this.pamentGatewayStatus;
	}

	public void setPamentGatewayStatus(Integer pamentGatewayStatus) {
		this.pamentGatewayStatus = pamentGatewayStatus;
	}

	public Integer getFinalStatus() {
		return this.finalStatus;
	}

	public void setFinalStatus(Integer finalStatus) {
		this.finalStatus = finalStatus;
	}

	public Date getEntryDateTime() {
		return this.entryDateTime;
	}

	public void setEntryDateTime(Date entryDateTime) {
		this.entryDateTime = entryDateTime;
	}

	public Date getModifyDateTime() {
		return this.modifyDateTime;
	}

	public void setModifyDateTime(Date modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getRemarksMsg() {
		return remarksMsg;
	}

	public void setRemarksMsg(String remarksMsg) {
		this.remarksMsg = remarksMsg;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getMaintenanceId() {
		return maintenanceId;
	}

	public void setMaintenanceId(Integer maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	

}
