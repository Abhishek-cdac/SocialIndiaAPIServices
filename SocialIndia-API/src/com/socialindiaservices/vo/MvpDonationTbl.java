package com.socialindiaservices.vo;
// default package
// Generated Jan 5, 2017 11:25:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

/**
 * MvpDonationTbl generated by hbm2java
 */
public class MvpDonationTbl implements java.io.Serializable {

	private Integer donateId;
	private UserMasterTblVo userId;
	private SkillMasterTblVO mvpDonationSubcategoryMstTbl;
	private CategoryMasterTblVO  mvpDonationCategoryMstTbl;
	private FeedsTblVO feedId;
	private String title;
	private Integer quantity;
	private Integer itemType;
	private String description;
	private String otherDescription;
	private String imageName;
	private MvpDonationInstitutionTbl donateTo;

	private Float amount;
	private String paymentmode;
	private Integer status;
	private UserMasterTblVo entryBy;
	private Date entryDate;
	private Date modifyDate;

	public MvpDonationTbl() {
	}

	public MvpDonationTbl(Date modifyDate) {
		this.modifyDate = modifyDate;
	}


	public Integer getDonateId() {
		return this.donateId;
	}

	public void setDonateId(Integer donateId) {
		this.donateId = donateId;
	}



	public SkillMasterTblVO getMvpDonationSubcategoryMstTbl() {
		return mvpDonationSubcategoryMstTbl;
	}

	public void setMvpDonationSubcategoryMstTbl(
			SkillMasterTblVO mvpDonationSubcategoryMstTbl) {
		this.mvpDonationSubcategoryMstTbl = mvpDonationSubcategoryMstTbl;
	}

	public CategoryMasterTblVO getMvpDonationCategoryMstTbl() {
		return mvpDonationCategoryMstTbl;
	}

	public void setMvpDonationCategoryMstTbl(
			CategoryMasterTblVO mvpDonationCategoryMstTbl) {
		this.mvpDonationCategoryMstTbl = mvpDonationCategoryMstTbl;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getItemType() {
		return this.itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOtherDescription() {
		return this.otherDescription;
	}

	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}

	public String getImageName() {
		return this.imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}




	public MvpDonationInstitutionTbl getDonateTo() {
		return donateTo;
	}

	public void setDonateTo(MvpDonationInstitutionTbl donateTo) {
		this.donateTo = donateTo;
	}

	public String getPaymentmode() {
		return this.paymentmode;
	}

	public UserMasterTblVo getUserId() {
		return userId;
	}

	public void setUserId(UserMasterTblVo userId) {
		this.userId = userId;
	}

	public FeedsTblVO getFeedId() {
		return feedId;
	}

	public void setFeedId(FeedsTblVO feedId) {
		this.feedId = feedId;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public void setPaymentmode(String paymentmode) {
		this.paymentmode = paymentmode;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}



	public UserMasterTblVo getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(UserMasterTblVo entryBy) {
		this.entryBy = entryBy;
	}

	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
