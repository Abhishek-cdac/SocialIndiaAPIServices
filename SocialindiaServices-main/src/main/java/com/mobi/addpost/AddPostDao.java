package com.mobi.addpost;

import java.io.File;
import java.util.List;

import org.json.JSONArray;

import com.pack.complaintVO.ComplaintsTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

public interface AddPostDao {
	public boolean updateAddpostVal(String ridVale,String adVale);

	public MvpAdpostTbl mvpAddPostDetail(String ridVale, String adVale);
	
	public  List<MvpAdpostTbl> getAddPostList(MvpAdpostTbl mvpObject);
	
	public boolean adPostInsert(MvpAdpostTbl adPost, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName,String category_name);
	
	public boolean checkAddPostExist(String addId,String rid);
	
	public MvpAdpostAttchTbl getDeleteAddPostList(String attachId);
	
	public boolean deleteAttachInfo(String attachId);
	
	public List<MvpAdpostTbl> getAdPostDetailForUniqueList(String userId,String ad_id);
	
	public List<MvpAdpostTbl> getAdPostList(String userId,String timestamp, String startlim,String totalrow,String globalSearch,String whSrch);
	
	public boolean addPostUpdate(List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName,String title,String ads_titile,String ad_category,float amount,String desc,String compliantdid,String userid,JSONArray jsonarr,String feed_id,String category);
	
	public List<MvpAdpostAttchTbl> getAdPostAttachList(String attachId);

	public int getadpostFeedViewId(int rid, Integer societyId,Integer feedId, int userid, Integer isShared,String searchFlag);

	public boolean additionalFeedUpdate(MvpAdpostTbl adPostMst);
}
