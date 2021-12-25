package com.pack.donation;



import java.io.File;
import java.util.List;

import com.pack.timelinefeedvo.FeedattchTblVO;
import com.socialindiaservices.vo.MvpDonationAttachTblVo;
import com.socialindiaservices.vo.MvpDonationInstitutionTbl;
import com.socialindiaservices.vo.MvpDonationItemTypeTblVo;
import com.socialindiaservices.vo.MvpDonationTbl;

public interface DonationDao {

	public List getGatepassList(String qry, String startlim,String totalrow,String timestamp);
	
	public List getGatepassMasterList(String qry, String startlim,String totalrow,String timestamp);
	
	public Integer saveDonationBookingData(MvpDonationTbl DonationObj);
	
	public boolean updateDonationData(MvpDonationTbl DonationObj);
	
	public boolean deleteDonationId(String userId,String wishlistid);
	
	public List<MvpDonationInstitutionTbl> donationToList();

	public boolean donationAttachInsert(int donationUniqId,List<File> fileUpload, 
			List<String> fileUploadContentType,List<String> fileUploadFileName);

	public MvpDonationTbl getDonationData(int donationUniqId, int rid);

	public MvpDonationAttachTblVo getDonationAttachData(String attachId);

	public boolean deleteDonationAttach(String attachId);

	public List<MvpDonationTbl> donationSearchList(String searchQury, String startlimit, String text, String locTimeStamp);

	public List<MvpDonationAttachTblVo> getDonationAttachList(String attachId);

	public MvpDonationItemTypeTblVo itemTypeGetDetails(MvpDonationItemTypeTblVo itemTypeObj);

	public int deletFeedAttach(int feedId);

	public int editFeedAttach(FeedattchTblVO feedAttchObj);

}
