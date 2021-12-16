package com.mobi.jsonpack;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;

public interface JsonSimplepackDao {
	
	public JSONArray feedListDetails(List<Object[]> feedListListObj,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb,String profileimgPath);
	
	public JSONArray spTagSplitIntoJsonArray(String splitData,String imageFilePath);
	
	public ArrayList<String> spTabSplitIntoArraylist(String splitData);
	
	public JSONArray whyIupdateDetails(List<WhyShouldIUpdateTblVO> listObj);
	
	public JSONArray pubSkillListDetails(List<Object[]> pubSkillListObj,String profileimgPath);
	
	public JSONArray packSingleArry(String data);
	
	public JSONObject feedDetailsPack(Object[] objList,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb,String profileimgPath);
	
	public JSONArray feedListSearchDetails(List<FeedsTblVO> feedListListObj,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb,String profileimgPath);

}
