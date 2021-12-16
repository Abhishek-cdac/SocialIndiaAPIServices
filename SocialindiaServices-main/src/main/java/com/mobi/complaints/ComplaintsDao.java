package com.mobi.complaints;

import java.io.File;
import java.util.List;











import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;

public interface ComplaintsDao {
	
	public List<ComplaintsTblVO> getComplaintList(String userId,String timestamp, String startlim,String totalrow,String searchtext,String statusflg,String complaintId);

	public List<ComplaintattchTblVO> getComplaintAttachList(int attachId);
	
	public boolean complaintsDelete(String rid,String complintId);
	
	public int compliantsPostInsert(ComplaintsTblVO compliantMst, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName);

	public boolean compliantsUpdate( List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName,String title,String desc, String post_to,String compliantdid,String userid,JSONArray jsonarr,String feed_id);
	
	public boolean compliantsUpdate(String compliantdid,String userid);
	
	public ComplaintattchTblVO getDeleteCompliantList(String attachId);
	
	public boolean deleteAttachInfo(String attachId);
	
	public FeedsTblVO getFeedIdValue(String compliantId);
	
	public Integer getCompliantDetails(String compliantId,String userId);

	public List<ComplaintsTblVO> getComplaintListPost_1(String userId,String timestamp, String startlim, String totalrow,int societyidval,String searchtext,String statusflg,String post_to,String complaintId);

	public JSONObject complaintJasonpackValues(ComplaintsTblVO complainUniqId);

	public ComplaintsTblVO ComplaintUniqResult(int complainUniqId, int rid);

	public boolean updateFeedIdtoComplaint(int complaintGetObj,int retFeedId,int rid);

	public boolean additionalFeedUpdate(JSONObject jsonComplaintObj,int retFeedId);
	
	public ComplaintsTblVO ComplaintUniqResultByComplaintId(int complainUniqId);
}
