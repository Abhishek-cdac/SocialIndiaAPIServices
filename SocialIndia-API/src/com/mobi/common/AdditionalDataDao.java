package com.mobi.common;

import com.mobi.networkUserListVO.MvpNetworkTbl;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.FacilityBookingTblVO;

public interface AdditionalDataDao {
	public String formAdditionalDataForFacilityBookingTbl(FacilityBookingTblVO facitityObj);
	public String formAdditionalDataForComplaintMastTbl(ComplaintsTblVO complaintsObj);
	public String formAdditionalDataForDocumentTbl(DocumentManageTblVO documentObj,DocumentShareTblVO documentshareObj);
	public String formAdditionalDataForFeedTbl(FeedsTblVO feedTblObj);
	public String formAdditionalDataForEventTbl(EventTblVO eventObj);
	public String formAdditionalDataForNetworkTbl(MvpNetworkTbl networkObj,int societyId,UserMasterTblVo userMst);

}
