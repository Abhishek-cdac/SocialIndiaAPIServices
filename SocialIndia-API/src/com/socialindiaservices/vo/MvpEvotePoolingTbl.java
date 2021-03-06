package com.socialindiaservices.vo;

// default package
// Generated Dec 8, 2016 4:01:38 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MvpEvotePoolingTbl generated by hbm2java
 */
public class MvpEvotePoolingTbl implements java.io.Serializable {

	private Integer votingId;
	private UserMasterTblVo usrRegTbl;
	private Date votingDate;
	private Integer statusflag;
	private Integer votingStatus;
	private MvpEvotingMstTbl evotingMstId;

	public MvpEvotePoolingTbl() {
	}

	public MvpEvotePoolingTbl(UserMasterTblVo usrRegTbl, Date votingDate,
			Integer statusflag, Integer votingStatus,MvpEvotingMstTbl evotingMstId) {
		this.usrRegTbl = usrRegTbl;
		this.votingDate = votingDate;
		this.statusflag = statusflag;
		this.votingStatus = votingStatus;
		this.evotingMstId=evotingMstId;
	}

	public Integer getVotingId() {
		return this.votingId;
	}

	public void setVotingId(Integer votingId) {
		this.votingId = votingId;
	}

	public UserMasterTblVo getUsrRegTbl() {
		return this.usrRegTbl;
	}

	public void setUsrRegTbl(UserMasterTblVo usrRegTbl) {
		this.usrRegTbl = usrRegTbl;
	}

	public Date getVotingDate() {
		return this.votingDate;
	}

	public void setVotingDate(Date votingDate) {
		this.votingDate = votingDate;
	}

	public Integer getStatusflag() {
		return this.statusflag;
	}

	public void setStatusflag(Integer statusflag) {
		this.statusflag = statusflag;
	}

	public Integer getVotingStatus() {
		return this.votingStatus;
	}

	public void setVotingStatus(Integer votingStatus) {
		this.votingStatus = votingStatus;
	}

	public MvpEvotingMstTbl getEvotingMstId() {
		return evotingMstId;
	}

	public void setEvotingMstId(MvpEvotingMstTbl evotingMstId) {
		this.evotingMstId = evotingMstId;
	}
	

}
