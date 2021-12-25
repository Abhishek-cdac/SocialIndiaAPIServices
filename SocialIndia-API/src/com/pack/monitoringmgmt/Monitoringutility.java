package com.pack.monitoringmgmt;

import org.json.JSONObject;

import com.pack.timelinefeedvo.persistence.FeedDaoService;
import com.pack.timelinefeedvo.persistence.FeedsDao;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.Log;

public class Monitoringutility {
	public static String toBlockedContent(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null;
		String lvrTblflg = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Feed Content Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "contentid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrFeedstatus = "0";
		
			
			locUpdQry = "update FeedsTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnFEED_ID ="+Integer.parseInt(lvrFeedid)+"";
			logwrite.logMessage("Step 3 : Feed Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			lvrFeedsDaoobj = new FeedDaoService();
			FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffFeed Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in Monitoringutility.toBlockedContent() : "+e);
			logwrite.logMessage("Exception found in Monitoringutility.toBlockedContent() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
	}
	
	public static String toUnblockedContent(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null;
		String lvrTblflg = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Feed Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "contentid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrFeedstatus = "1";
			locUpdQry = "update FeedsTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnFEED_ID ="+Integer.parseInt(lvrFeedid)+"";
			logwrite.logMessage("Step 3 : Feed Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			lvrFeedsDaoobj = new FeedDaoService();
			FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffFeed Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toUnblockedContent() : "+e);
			logwrite.logMessage("Exception found in toUnblockedContent() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
	}

	public static String toActivatedDeactiveUser(JSONObject prDatajson, String actdctflg) {
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvruserid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : User Activation Start.", "info", Monitoringutility.class);
			
			lvruserid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "usrid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			//lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");	
			locUpdQry = "update UserMasterTblVo set statusFlag ="+Integer.parseInt(actdctflg)+" where userId ="+Integer.parseInt(lvruserid)+"";
			logwrite.logMessage("Step 3 :  User Activation query: "+locUpdQry, "info", Monitoringutility.class);
			lvrFeedsDaoobj = new FeedDaoService();
			FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 :  User Activation Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in Monitoringutility.toActivatedUser: "+e);
			logwrite.logMessage("Exception found in Monitoringutility.toActivatedUser: "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toBlockedImage(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null;
		String lvrTblflg = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Feed Image Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imageid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrFeedstatus = "0";
		
			
			locUpdQry = "update FeedattchTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnATTCH_ID ="+Integer.parseInt(lvrFeedid)+"";
			logwrite.logMessage("Step 3 : Feed Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			lvrFeedsDaoobj = new FeedDaoService();
			FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffFeed Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in Monitoringutility.toBlockedImage() : "+e);
			logwrite.logMessage("Exception found in Monitoringutility.toBlockedImage() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
	}
	
	public static String toUnblockedImage(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null;
		String lvrTblflg = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Feed image Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imageid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrFeedstatus = "1";
			locUpdQry = "update FeedattchTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnATTCH_ID ="+Integer.parseInt(lvrFeedid)+"";
			logwrite.logMessage("Step 3 : Feed Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			lvrFeedsDaoobj = new FeedDaoService();
			FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffFeed Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toUnblockedContent() : "+e);
			logwrite.logMessage("Exception found in toUnblockedContent() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
	}
	
	public static String toDeleteSingonfailureRept(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrsgfailstatus = null, lvrsgfailid = null;
		String lvrTblflg = null;
		Log logwrite = null;
		boolean sgfailUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrsgfailsDaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : sgfail Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrsgfailid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "sigonfailureid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrsgfailstatus = "2";
			locUpdQry = "update FailedSignOnTbl set status ="+Integer.parseInt(lvrsgfailstatus)+" where id ="+Integer.parseInt(lvrsgfailid)+"";
			logwrite.logMessage("Step 3 : sgfail Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			lvrsgfailsDaoobj = new FeedDaoService();
			sgfailUpdsts = lvrsgfailsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(sgfailUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staff Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toUnblockedContent() : "+e);
			logwrite.logMessage("Exception found in toUnblockedContent() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrsgfailsDaoobj = null;lvrTblflg=null;
		}
	}


    public static String toBlockedImageprocall (JSONObject prDatajson){
    	String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null;
		String lvrTblflg = null;
		String lvrTblname = null,mastertblid=null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry = null;
		String locUpdTimeQry = null;
		FeedsDao lvrFeedsDaoobj = null;
		
		try {
			logwrite = new Log();
			Commonutility.toWriteConsole("Step 2 : Image Monitoring Image Deactive Block Start.");
			logwrite.logMessage("Step 2 : Image Monitoring Image Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imageid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrTblname = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tablename");
			mastertblid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "mastertblid");
			// tabale name
			lvrFeedstatus = "0";
		
			if(lvrTblname!=null && lvrTblname.equalsIgnoreCase("MVP_COMPLAINTS_ATTCH_TBL")) {
				locUpdQry = "update ComplaintattchTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnATTCH_ID ="+Integer.parseInt(lvrFeedid)+"";
				locUpdTimeQry="update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedType =7 and feedId="+mastertblid+"";
			}else if(lvrTblname!=null && lvrTblname.equalsIgnoreCase("MVP_EVENT_TBL")) {
				locUpdQry = "update EventTblVO set ivrBnEVENTSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where feedType ="+Integer.parseInt(lvrFeedid)+"";
				locUpdTimeQry="update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedType in (8,9,10) and feedId="+mastertblid+"";
			}else if(lvrTblname!=null && lvrTblname.equalsIgnoreCase("MVP_TENDER_DOC_TBL")) {
				locUpdQry = "update TenderDocTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where tenderDocId ="+Integer.parseInt(lvrFeedid)+"";
			}else if(lvrTblname!=null && lvrTblname.equalsIgnoreCase("MVP_FEED_ATTCH_TBL")) {
				locUpdQry = "update FeedattchTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnATTCH_ID ="+Integer.parseInt(lvrFeedid)+"";
				locUpdTimeQry="update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedType in (1,2) and feedId="+mastertblid+"";
			}else if(lvrTblname!=null && lvrTblname.equalsIgnoreCase("MVP_ENEWS_IMAGE_TBL")) {
				locUpdQry = "update EeNewsDocTblVO set status ="+Integer.parseInt(lvrFeedstatus)+" where EnewuniqId ="+Integer.parseInt(lvrFeedid)+"";
			}else if(lvrTblname!=null && lvrTblname.equalsIgnoreCase("MVP_EVOTING_IMAGE_TBL")) {
				locUpdQry = "update MvpEvotingImageTbl set statusFlag ="+Integer.parseInt(lvrFeedstatus)+" where evotingImageId ="+Integer.parseInt(lvrFeedid)+"";
			}else{
				locUpdQry = null;
			}
			
			Commonutility.toWriteConsole("Step 3 : Image Monitoring Image Deactive Update Query : "+locUpdQry);
			logwrite.logMessage("Step 3 : Image Monitoring Image Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			if (locUpdQry!=null) {
				lvrFeedsDaoobj = new FeedDaoService();
				FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			} else {
				FeedUpdsts = false;
			}
			// Feed table update
			if (locUpdTimeQry!=null) {
				lvrFeedsDaoobj = new FeedDaoService();
				boolean lvrsupst = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdTimeQry);
				Commonutility.toWriteConsole("Step 4 : Image Monitoring Image Deactive Update Query locUpdTimeQry : "+locUpdTimeQry);
				logwrite.logMessage("Step 4 : Image Monitoring Image Deactive Update Query locUpdTimeQry : "+locUpdTimeQry, "info", Monitoringutility.class);
				Commonutility.toWriteConsole("Step 4.1 : Image Monitoring Image Deactive Update Query locUpdTimeQry status  : "+lvrsupst);
				logwrite.logMessage("Step 4.1 : Image Monitoring Image Deactive Update Query locUpdTimeQry status  : "+lvrsupst,"info",Monitoringutility.class);
			} else {				
			}
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 5 : Image Monitoring Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in Monitoringutility.toBlockedImage() : "+e);
			logwrite.logMessage("Exception found in Monitoringutility.toBlockedImage() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
    }

    public static String toUnblockedImageprocall(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null,mastertblid=null;
		String lvrTblflg = null,locUpdTimeQry=null;
		String lvrTblname = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Feed image Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imageid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrTblname = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tablename");
			mastertblid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "mastertblid");
			// table name
			
			lvrFeedstatus = "1";
		
			if(lvrTblname!=null && lvrTblname.trim().equalsIgnoreCase("MVP_COMPLAINTS_ATTCH_TBL")) {
				locUpdQry = "update ComplaintattchTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnATTCH_ID ="+Integer.parseInt(lvrFeedid)+"";
				locUpdTimeQry="update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedType =7 and feedId="+mastertblid+"";
			}else if(lvrTblname!=null && lvrTblname.trim().equalsIgnoreCase("MVP_EVENT_TBL")) {
				locUpdQry = "update EventTblVO set ivrBnEVENTSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnEVENT_ID ="+Integer.parseInt(lvrFeedid)+"";
				locUpdTimeQry="update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedType in (8,9,10) and feedId="+mastertblid+"";
			}else if(lvrTblname!=null && lvrTblname.trim().equalsIgnoreCase("MVP_TENDER_DOC_TBL")) {
				locUpdQry = "update TenderDocTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where tenderDocId ="+Integer.parseInt(lvrFeedid)+"";
				
			}else if(lvrTblname!=null && lvrTblname.trim().equalsIgnoreCase("MVP_FEED_ATTCH_TBL")) {
				locUpdQry = "update FeedattchTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnATTCH_ID ="+Integer.parseInt(lvrFeedid)+"";
				locUpdTimeQry="update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedType in (1,2) and feedId="+mastertblid+"";
			}else if(lvrTblname!=null && lvrTblname.trim().equalsIgnoreCase("MVP_ENEWS_IMAGE_TBL")) {
				locUpdQry = "update EeNewsDocTblVO set status ="+Integer.parseInt(lvrFeedstatus)+" where EnewuniqId ="+Integer.parseInt(lvrFeedid)+"";
				
			}else if(lvrTblname!=null && lvrTblname.trim().equalsIgnoreCase("MVP_EVOTING_IMAGE_TBL")) {
				locUpdQry = "update MvpEvotingImageTbl set statusFlag ="+Integer.parseInt(lvrFeedstatus)+" where evotingImageId ="+Integer.parseInt(lvrFeedid)+"";
				
			}else{
				
			}
		
			logwrite.logMessage("Step 3 : Feed Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			if (locUpdQry!=null && !locUpdQry.equalsIgnoreCase("") && !locUpdQry.equalsIgnoreCase("null")) {
				lvrFeedsDaoobj = new FeedDaoService();
				FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
				
			} else {
				FeedUpdsts = false;
			}
			if (locUpdTimeQry!=null) {						
				lvrFeedsDaoobj = new FeedDaoService();
				lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdTimeQry);
				logwrite.logMessage("Step 4 : Feed Deactive Update Query locUpdTimeQry : "+locUpdTimeQry, "info", Monitoringutility.class);
			} else {				
			}
			
			
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 5 : Image Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toUnblockedContent() : "+e);
			logwrite.logMessage("Exception found in toUnblockedContent() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
	}

    public static String toBlockedContentprocall(JSONObject prDatajson){
    	String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null;
		String lvrTblflg = null;
		String lvrTbltblname = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Content monitoring Deactive Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "contentid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrTbltblname = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tablename");
			lvrFeedstatus = "0";
											
			if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_TIMELINE_FEED")) {
				locUpdQry = "update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedId ="+Integer.parseInt(lvrFeedid)+"";
			
			} else if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_EVENT_TBL")) {
				locUpdQry = "update EventTblVO set ivrBnEVENTSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnEVENT_ID ="+Integer.parseInt(lvrFeedid)+"";
				
			} else if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_TENDER_TBL")) {
				locUpdQry = "update TenderTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where tenderId ="+Integer.parseInt(lvrFeedid)+"";
				
			}  else if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_EVOTING_MST_TBL")) {
				lvrFeedstatus = "2";
				locUpdQry = "update MvpEvotingMstTbl set statusFlag ="+Integer.parseInt(lvrFeedstatus)+" where evotingId ="+Integer.parseInt(lvrFeedid)+"";
				
			} else {
				
			}
			
			
			logwrite.logMessage("Step 3 : Content monitoring Deactive Update Query : "+locUpdQry, "info", Monitoringutility.class);
			lvrFeedsDaoobj = new FeedDaoService();
			FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : Content monitoring Deactive Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in Monitoringutility.toBlockedContent() : "+e);
			logwrite.logMessage("Exception found in Monitoringutility.toBlockedContent() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
	
    }

    public static String toUnblockedContentprocall(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrFeedstatus = null, lvrFeedid = null;
		String lvrTblflg = null;
		String lvrTbltblname = null;
		Log logwrite = null;
		boolean FeedUpdsts = false;
		String locUpdQry=null;
		FeedsDao lvrFeedsDaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Content monitoring unblocked / Active Block Start.", "info", Monitoringutility.class);
			
			lvrFeedid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "contentid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrTblflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tableflg");
			lvrTbltblname = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tablename");
			lvrFeedstatus = "1";
						
			
			if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_TIMELINE_FEED")) {
				locUpdQry = "update FeedsTblVO set feedStatus ="+Integer.parseInt(lvrFeedstatus)+" where feedId ="+Integer.parseInt(lvrFeedid)+"";
			
			} else if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_EVENT_TBL")) {
				locUpdQry = "update EventTblVO set ivrBnEVENTSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where ivrBnEVENT_ID ="+Integer.parseInt(lvrFeedid)+"";
				
			} else if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_TENDER_TBL")) {
				locUpdQry = "update TenderTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrFeedstatus)+" where tenderId ="+Integer.parseInt(lvrFeedid)+"";
				
			}  else if(lvrTbltblname!=null && lvrTbltblname.trim().equalsIgnoreCase("MVP_EVOTING_MST_TBL")) {
				lvrFeedstatus = "1";
				locUpdQry = "update MvpEvotingMstTbl set statusFlag ="+Integer.parseInt(lvrFeedstatus)+" where evotingId ="+Integer.parseInt(lvrFeedid)+"";
				
			} else {
				
			}
			
			logwrite.logMessage("Step 3 : Content monitoring unblocked / active Update Query : "+locUpdQry, "info", Monitoringutility.class);
			lvrFeedsDaoobj = new FeedDaoService();
			FeedUpdsts = lvrFeedsDaoobj.toDeactivateTimelineFeeds(locUpdQry);
			
			if(FeedUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : Content monitoring unblocked / Active Block End.", "info", Monitoringutility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toUnblockedContent() : "+e);
			logwrite.logMessage("Exception found in toUnblockedContent() : "+e, "error", Monitoringutility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrFeedsDaoobj = null;lvrTblflg=null;
		}
	}
}
