package com.mobi.utils;

import java.util.Date;

import org.json.JSONArray;



public interface FunctionUtility {
	
	public String getPostedDateTime(Date entryDate);
	
	public String getMothName(int monthNum);
	
	public String likeFormat(long value);
	
	public String likeCountFormat(Long value);
	
	public String longToString (Long value);
	
	public String getShortTime(String timeStamp);
	
	public String getLongTime(String timeStamp);
		
	public String uniqueNoFromDate();
	
	public String jsnArryIntoStrBasedOnComma(JSONArray jsnArryObj);//OP like 1,2,3
	
	public String trimSplEscSrchdata (String srchdata);
	
	public String dataSplitbyHyphen (String data);
	
	public String dataSplitbyspace (String data);
	
	public String hypenadd (String data);
}
