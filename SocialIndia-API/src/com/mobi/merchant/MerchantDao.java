package com.mobi.merchant;

import java.util.List;

import com.pack.feedbackvo.FeedbackTblVO;
import com.socialindiaservices.vo.MerchantIssuePostingTblVO;
import com.socialindiaservices.vo.MerchantProductItemsTblVO;
import com.socialindiaservices.vo.MerchantProductOrderTblVO;
import com.socialindiaservices.vo.MerchantTblVO;
import com.socialindiaservices.vo.MerchantrRatingTblVO;

public interface MerchantDao {
	public List getMerchantCategoryList(String qry,String startlim,String totalrow);
	public List<Object[]> getMobMerchantSearchList(String qry, String startlim);
	public List<Object[]> getMobMerchantIssueSearchList(String qry);
	public MerchantTblVO getMerchantobject(int merchantId);
	public boolean saveFeedbackTblObj(FeedbackTblVO feedbackobj);
	public boolean saveMerchantIssueTblObj(MerchantIssuePostingTblVO mrchIssueobj);
	public boolean saveorderTblObj(MerchantProductOrderTblVO orderobj);
	public boolean saveitemTblObj(MerchantProductItemsTblVO itemobj);
	public List getMerchantTableList(String qry, String startlim,String totalrow);
	public List getAllMerchantCategoryList(String qry,String totalrow);
	public boolean savemerchIssueTblObj(MerchantIssuePostingTblVO issueobj);
	public boolean savemerchRatingTblObj(MerchantrRatingTblVO ratingobj);
	public boolean executecommonProcedure(String sql);
}
