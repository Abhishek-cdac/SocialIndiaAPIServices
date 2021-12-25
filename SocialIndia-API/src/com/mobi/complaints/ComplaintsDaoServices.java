package com.mobi.complaints;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.townShipMgmt.townShipMgmtDao;
import com.siservices.townShipMgmt.townShipMgmtDaoServices;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ComplaintsDaoServices extends ActionSupport implements ComplaintsDao  {

	Log log = new Log();
	@Override
	public List<ComplaintsTblVO> getComplaintList(String userId,
			String timestamp, String startlim, String totalrow,String searchtext,String statusflg,String complaintId) {
		List<ComplaintsTblVO> complaintList = new ArrayList<ComplaintsTblVO>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		String statusflag="";
		try {
			String serachcrit="";
			if(complaintId!=null && !complaintId.equalsIgnoreCase("")&& complaintId.length()>0){
				serachcrit+=" and complaintsId='"+complaintId+"'";
			}
			
			if(statusflg!=null && !statusflg.equalsIgnoreCase("null")&& statusflg.equalsIgnoreCase("1"))
			{
				statusflag = "in(1)";
			}
			else if(statusflg!=null && !statusflg.equalsIgnoreCase("null")&& statusflg.equalsIgnoreCase("2"))
					{
				statusflag = "in(2,3)";
					}
			else
			{
				statusflag = "in(1,2,3)";
			}
			if(searchtext!=null && !searchtext.equalsIgnoreCase("null") && !searchtext.equalsIgnoreCase("")){
			qry = " from ComplaintsTblVO where usrRegTblByFromUsrId.userId=:USER_ID and status "+statusflag+" and compliantsToFlag <> '0' and entryDatetime <='"+timestamp+"' "+serachcrit
					+ "  and (" + "complaintsTitle like ('%" + searchtext + "%') or " 
			             + " complaintsDesc like ('%" + searchtext + "%') or "
			               + " closereason like ('%" + searchtext + "%')"
			             + ") order by complaintsId desc";
		}
			else
			{
				qry = " from ComplaintsTblVO where usrRegTblByFromUsrId.userId=:USER_ID and status "+statusflag+" and  compliantsToFlag <> '0' and entryDatetime <='"+timestamp+"' "+serachcrit
						+ "  order by complaintsId desc";
			}
			
			System.out.println("qry   "+qry);
			
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = df.parse(timestamp);
			qy.setDate("ENTRY_DATE", startDate);
			System.out.println("startDate=>"+startDate);*/
			complaintList = qy.list();
			System.out.println("complaintList=>"+complaintList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("complainDaoServices  complaintList======" + ex);
			log.logMessage(
					"complainDaoServices Exception getFamilyMembersList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return complaintList;
	}
	@Override
	public boolean complaintsDelete(String rid,String complaintsID){
	 boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    Date date1;
	    CommonUtils comutil=new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	    try {
	      tx = session.beginTransaction();
	      Query qy = session.createQuery("update ComplaintsTblVO set status=:STATUS where complaintsId=:COMPLAINTS_ID and  usrRegTblByFromUsrId.userId=:USR_ID");
	      qy.setInteger("STATUS", 0);
	      qy.setInteger("COMPLAINTS_ID",Integer.parseInt(complaintsID));
	      qy.setInteger("USR_ID", Integer.parseInt(rid));
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("ComplaintsDaoServices Exception getUserPrfileList : "+ex, "error", ComplaintsDaoServices.class);
	    } finally {
	    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
	    }
	    return result;
	}
	@Override
	public int compliantsPostInsert(ComplaintsTblVO compliantMst, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName){
		 boolean result=false;  mobiCommon mobCom=new mobiCommon();
			JSONObject jsonObj = new JSONObject();
			int feedid=0;
			int compId = 0;
			 List<File> fileUploadLat = new ArrayList<File>();
			 HashMap<String,File> imageMap=new HashMap<String,File>();
			 HashMap<String,File> videoMap=new HashMap<String,File>();
		 ComplaintattchTblVO compliantsAttachMst=new ComplaintattchTblVO();
		 townShipMgmtDao townShip=new townShipMgmtDaoServices();
		 FeedDAO feed=new FeedDAOService();
		 profileDao profile=new profileDaoServices();
		 FeedsTblVO feedMst=new FeedsTblVO();
		 UserMasterTblVo userMst=new UserMasterTblVo();
		 SocietyMstTbl societyMst=new SocietyMstTbl();
			Session session = HibernateUtil.getSession();
			 Transaction tx = null;
			 try {		    		   
	       	  tx = session.beginTransaction();
	           session.save(compliantMst);
	           compId=compliantMst.getComplaintsId();
	           System.out.println("=====compId===="+compId);
	          // uploadUrl=
	           Date date1;
	   	    CommonUtils comutil=new CommonUtilsServices();
	   		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	         if(fileUpload.size()>0){
	        	 for(int i=0;i<fileUpload.size();i++){
	        		
	        		 File uploadedFile1 = fileUpload.get(i);
	        	 String fileName1 = fileUploadFileName.get(i);
	           String limgName1 = FilenameUtils.getBaseName(fileName1);
	 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileName1);
	 		 	  Integer fileType=mobCom.getFileExtensionType(limageFomat1);
	 		 	  String sqlQry="SELECT max(ivrBnATTCH_ID)+1 FROM ComplaintattchTblVO ";
	 		 	  int attach_count = townShip.getInitTotal(sqlQry);
	 		 	  System.out.println("===attach_count=="+attach_count);
	 		 	 if(fileType==2){
	 		 		videoMap.put(limgName1+"_"+attach_count+"."+limageFomat1, uploadedFile1);
	        		 }else{
	        			 imageMap.put(limgName1+"_"+attach_count+"."+limageFomat1, uploadedFile1);	 
	        		 }
	        	 }
	        	 System.out.println("===========videoMap=====size======="+videoMap.size());
	        	 System.out.println("=========d==========="+videoMap);
	        	 System.out.println("=========d===imageMap========"+imageMap);
	        	 System.out.println("===========imageMap=====size======"+imageMap.size());
	        	 if(videoMap.size()>0){
	        	 for (Entry<String, File> entry : videoMap.entrySet()) {
	        		 compliantsAttachMst=new ComplaintattchTblVO();
	        		   compliantsAttachMst.setIvrBnCOMPLAINTS_ID(compId);
	    	           compliantsAttachMst.setIvrBnSTATUS(1);
	    	           compliantsAttachMst.setIvrBnENTRY_DATETIME(date1);
	        		    System.out.println("Key = " + entry.getKey());
	        		    System.out.println("Value = " + entry.getValue());
	        		    String mapFileName=entry.getKey();
	        		    String mapVideoName = FilenameUtils.getBaseName(mapFileName);
	        		    System.out.println("=====mapVideoName==ssss=="+mapVideoName);
		 		 	 	compliantsAttachMst.setIvrBnFILE_TYPE(2);   
		 		 	 	compliantsAttachMst.setIvrBnATTACH_NAME(mapFileName);
		 		 	 	String destPath =getText("external.imagesfldr.path")+"complaint/videos/"+compId;
		 		 	 	System.out.println("=====compId==sssssssssssssss==2=");
	  	        		   File destFile  = new File(destPath, mapFileName);
	  		       	    	FileUtils.copyFile( entry.getValue(), destFile);
	  		       	    	compliantsAttachMst.setThumbImage(mapVideoName+".jpg");
	  		       	    	if(imageMap.containsKey(mapVideoName+".jpeg")){
	  		       	   File ImageFileName=imageMap.get(mapVideoName+".jpeg");
	  		       	String destPathThumb =getText("external.imagesfldr.path")+"complaint/thumbimage/"+compId;
	        		   File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
		       	    	FileUtils.copyFile(ImageFileName, destFileThumb);
		       	    	System.out.println("=============g=======================");
		       	    	imageMap.remove(mapVideoName+".jpeg");
	  		       	    	}
		       	     session.save(compliantsAttachMst);
	        	 }
	        	 System.out.println("======sssssssssssssss after=====imageMap=====size======"+imageMap.size());
	        	 } if(imageMap.size()>0){
	        	 for (Entry<String, File> imageentry : imageMap.entrySet()) {
	        		 compliantsAttachMst=new ComplaintattchTblVO();
	        		   compliantsAttachMst.setIvrBnCOMPLAINTS_ID(compId);
	    	           compliantsAttachMst.setIvrBnSTATUS(1);
	    	           compliantsAttachMst.setIvrBnENTRY_DATETIME(date1);
	        		    System.out.println("Key = " + imageentry.getKey());
	        		    System.out.println("Value = " + imageentry.getValue());
	        		    String ImageFileName=imageentry.getKey();
	        		    String mapVideoName = FilenameUtils.getBaseName(ImageFileName);
	        			String limageFomat = FilenameUtils.getExtension(ImageFileName);  
	        		    Integer fileType=mobCom.getFileExtensionType(limageFomat);
	        		    File ImageFilePath=imageMap.get(ImageFileName);
		        		int lneedWidth=0,lneedHeight = 0;
		        		 if(fileType==1){
		        			   System.out.println("==ImageFileName=="+ImageFileName);
		        			   compliantsAttachMst.setIvrBnFILE_TYPE(1); 
		        			   compliantsAttachMst.setIvrBnATTACH_NAME(ImageFileName);
		        			   int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
			        		   int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
			        		   String imageHeightPro=getText("thump.img.height");
			        		   String imageWidthPro=getText("thump.img.width");
			        		   String destPath =getText("external.imagesfldr.path")+"complaint/web/"+compId;
	    	        		   File destFile  = new File(destPath, ImageFileName);
	    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
			        			   if(imgHeight>Integer.parseInt(imageHeightPro)){
			        				   lneedHeight = Integer.parseInt(imageHeightPro);	
			    	        		 //mobile - small image
			        			   }else{
			        				   lneedHeight = imgHeight;	  
			        			   }
			        			   if(imgwidth>Integer.parseInt(imageWidthPro)){
			        				   lneedWidth = Integer.parseInt(imageWidthPro);	  
			        			   }else{
			        				   lneedWidth = imgwidth;
			        			   }
			        			   String limgSourcePath=getText("external.imagesfldr.path")+"complaint/web/"+compId+"/"+ImageFileName;
		    		 		 		String limgDisPath = getText("external.imagesfldr.path")+"complaint/mobile/"+compId;
		    		 		 	 	String limgName1 = FilenameUtils.getBaseName(ImageFileName);
		    		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
		    		 	    	 	String limageFor = "all";
		    		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
		        		   }else{
		        			   compliantsAttachMst.setIvrBnFILE_TYPE(3); 
		        			   compliantsAttachMst.setIvrBnATTACH_NAME(ImageFileName);
		        			   String destPath =getText("external.imagesfldr.path")+"complaint/mobile/"+compId;
	    	        		   File destFile  = new File(destPath, ImageFileName);
	    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
	    		       	     String otherdest =getText("external.imagesfldr.path")+"complaint/web/"+compId;
	    		       	     File destFileOther  = new File(otherdest, ImageFileName);
	  		       	    	FileUtils.copyFile(ImageFilePath, destFileOther);
		        		   }
		        		 session.save(compliantsAttachMst);
	        	 }
	        	 }
	         }
	        	 /*Date date;
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	        	 userMst.setUserId(compliantMst.getUsrRegTblByFromUsrId().getUserId());
					feedMst.setUsrId(userMst);
					feedMst.setFeedType(7);
					feedMst.setIsMyfeed(0);
					feedMst.setIsShared(0);
//					feedMst.setFeedPrivacyFlg(2);
					feedMst.setFeedStatus(1);
					feedMst.setFeedTitle(compliantMst.getComplaintsTitle());
					feedMst.setFeedDesc(compliantMst.getComplaintsDesc());
					feedMst.setFeedTypeId(compliantMst.getComplaintsId());
					feedMst.setFeedTime(date);
					feedMst.setEntryDatetime(date);
					feedMst.setFeedCategory("Complaint");
					System.out.println("=========fgh===========");
					userMst=profile.getUserPrfileInfo(String.valueOf(compliantMst.getUsrRegTblByFromUsrId().getUserId()));
					System.out.println("=========fguserMsth==========="+userMst);
					societyMst.setSocietyId(userMst.getSocietyId().getSocietyId());
					feedMst.setSocietyId(societyMst);
					feedMst.setEntryBy(compliantMst.getUsrRegTblByFromUsrId().getUserId());
					if(userMst!=null && userMst.getFirstName()!=null && !userMst.getFirstName().equalsIgnoreCase("")){
					feedMst.setOriginatorName(userMst.getFirstName());
					}
					if(userMst!=null && userMst.getCityId()!=null && userMst.getCityId().getCityName()!=null && !userMst.getCityId().getCityName().equalsIgnoreCase("")){
						feedMst.setFeedLocation(userMst.getCityId().getCityName());
					}
					feedMst.setOriginatorId(userMst.getUserId());
					 feedid=feed.feedInsert("",feedMst,fileUpload,fileUploadContentType,fileUploadFileName);
					System.out.println("=============feedid============"+feedid);*/
				/*	 Query qy = session.createQuery("update ComplaintsTblVO set feedId=:FEED_ID "
					      		+ " where complaintsId=:COMPLAINTS_ID and  usrRegTblByFromUsrId.userId=:USR_ID");
			         qy.setInteger("FEED_ID",feedid);
				      qy.setInteger("COMPLAINTS_ID",compliantMst.getComplaintsId());
				      qy.setInteger("USR_ID", compliantMst.getUsrRegTblByFromUsrId().getUserId());
				      qy.executeUpdate();*/
	         
	         //if(feedid>0){
	        
		      
	           tx.commit();
	           result = true;
	       //  }  	 
	        
	} catch (HibernateException ex) {	
		if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      compId = 0;
		System.out.println("ComplaintsDaoServices found compliantsPostInsert  : "+ex);
		 log.logMessage("ComplaintsDaoServices Exception compliantsPostInsert : "+ex, "error", ComplaintsDaoServices.class);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		if(session!=null){session.flush();session.clear();session.close();session=null;}
	}		   
	return compId;
	}
	@Override
	public List<ComplaintattchTblVO> getComplaintAttachList(int attachId) {
		List<ComplaintattchTblVO> complaintAttachList = new ArrayList<ComplaintattchTblVO>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from ComplaintattchTblVO where ivrBnCOMPLAINTS_ID=:COMPLIANT_ID";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("COMPLIANT_ID", attachId);
			//qy.setInteger("STATUS", 1);
			complaintAttachList = qy.list();
			System.out.println("complaintAttachList=>"+complaintAttachList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("complainDaoServices  getComplaintAttachList======" + ex);
			log.logMessage(
					"complainDaoServices Exception getComplaintAttachList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return complaintAttachList;
	}
	@Override
	public boolean compliantsUpdate( List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName,String title, String desc, String post_to,
			String compliantdid, String userid,JSONArray jsonarr,String feed_id) {
		
		boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    HashMap<String,File> imageMap=new HashMap<String,File>();
		 HashMap<String,File> videoMap=new HashMap<String,File>();
	 ComplaintattchTblVO compliantsAttachMst=new ComplaintattchTblVO();
	 townShipMgmtDao townShip=new townShipMgmtDaoServices();
	 ComplaintsDao complains=new ComplaintsDaoServices();
	 FeedDAO feed=new FeedDAOService();
	 FeedsTblVO feedMst=new FeedsTblVO();
		FeedsTblVO feedInfo=new FeedsTblVO();
	    Date date1;
	    CommonUtils comutil=new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	    try {
	    	 mobiCommon mobCom=new mobiCommon();
	      tx = session.beginTransaction();
	      if(fileUpload.size()>0){
	        	 for(int i=0;i<fileUpload.size();i++){
	        		
	        		 File uploadedFile1 = fileUpload.get(i);
	        	 String fileName1 = fileUploadFileName.get(i);
	           String limgName1 = FilenameUtils.getBaseName(fileName1);
	 		 	 	String limageFomat1 = FilenameUtils.getExtension(fileName1);
	 		 	  Integer fileType=mobCom.getFileExtensionType(limageFomat1);
	 		 	 String sqlQry="SELECT max(ivrBnATTCH_ID)+1 FROM ComplaintattchTblVO ";
	 		 	  int attach_count = townShip.getInitTotal(sqlQry);
	 		 	  System.out.println("===attach_count=="+attach_count);
	 		 	 if(fileType==2){
		 		 		videoMap.put(limgName1+"_"+attach_count+"."+limageFomat1, uploadedFile1);
		        		 }else{
		        			 imageMap.put(limgName1+"_"+attach_count+"."+limageFomat1, uploadedFile1);	 
		        		 }
	        	 }
	        	 System.out.println("===========videoMap=====size======="+videoMap.size());
	        	 System.out.println("=========d==========="+videoMap);
	        	 System.out.println("=========d===imageMap========"+imageMap);
	        	 System.out.println("===========imageMap=====size======"+imageMap.size());
	        	 if(videoMap.size()>0){
	        	 for (Entry<String, File> entry : videoMap.entrySet()) {
	        		 compliantsAttachMst=new ComplaintattchTblVO();
	        		   compliantsAttachMst.setIvrBnCOMPLAINTS_ID(Integer.parseInt(compliantdid));
	    	           compliantsAttachMst.setIvrBnSTATUS(1);
	    	           compliantsAttachMst.setIvrBnENTRY_DATETIME(date1);
	        		    System.out.println("Key = " + entry.getKey());
	        		    System.out.println("Value = " + entry.getValue());
	        		    String mapFileName=entry.getKey();
	        		    String mapVideoName = FilenameUtils.getBaseName(mapFileName);
		 		 	 	compliantsAttachMst.setIvrBnFILE_TYPE(2);   
		 		 	 	compliantsAttachMst.setIvrBnATTACH_NAME(mapFileName);
		 		 	 	String destPath =getText("external.imagesfldr.path")+"complaint/videos/"+compliantdid;
	  	        		   File destFile  = new File(destPath, mapFileName);
	  		       	    	FileUtils.copyFile( entry.getValue(), destFile);
	  		       	    	compliantsAttachMst.setThumbImage(mapVideoName+".jpg");
	  		       	    	
	  		       		if(imageMap.containsKey(mapVideoName+".jpeg")){
	 	  		       	   File ImageFileName=imageMap.get(mapVideoName+".jpeg");
	 	  		       	String destPathThumb =getText("external.imagesfldr.path")+"complaint/thumbimage/"+compliantdid;
	 	        		   File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
	 		       	    	FileUtils.copyFile(ImageFileName, destFileThumb);
	 		       	    	System.out.println("=============g=======================");
	 		       	    	imageMap.remove(mapVideoName+".jpeg");
	 	  		       	    	}
		       	     session.save(compliantsAttachMst);
	        	 }
	        	 } if(imageMap.size()>0){
	        	 for (Entry<String, File> imageentry : imageMap.entrySet()) {
	        		 compliantsAttachMst=new ComplaintattchTblVO();
	        		   compliantsAttachMst.setIvrBnCOMPLAINTS_ID(Integer.parseInt(compliantdid));
	    	           compliantsAttachMst.setIvrBnSTATUS(1);
	    	           compliantsAttachMst.setIvrBnENTRY_DATETIME(date1);
	        		    System.out.println("Key = " + imageentry.getKey());
	        		    System.out.println("Value = " + imageentry.getValue());
	        		    String ImageFileName=imageentry.getKey();
	        		    String mapVideoName = FilenameUtils.getBaseName(ImageFileName);
	        			String limageFomat = FilenameUtils.getExtension(ImageFileName);  
	        		    Integer fileType=mobCom.getFileExtensionType(limageFomat);
	        		    File ImageFilePath=imageMap.get(ImageFileName);
		        		int lneedWidth=0,lneedHeight = 0;
		        		 if(fileType==1){
		        			   System.out.println("==ImageFileName=="+ImageFileName);
		        			   compliantsAttachMst.setIvrBnFILE_TYPE(1); 
		        			   compliantsAttachMst.setIvrBnATTACH_NAME(ImageFileName);
		        			   int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
			        		   int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
			        		   String imageHeightPro=getText("thump.img.height");
			        		   String imageWidthPro=getText("thump.img.width");
			        		   String destPath =getText("external.imagesfldr.path")+"complaint/web/"+compliantdid;
	    	        		   File destFile  = new File(destPath, ImageFileName);
	    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
			        			   if(imgHeight>Integer.parseInt(imageHeightPro)){
			        				   lneedHeight = Integer.parseInt(imageHeightPro);	
			    	        		 //mobile - small image
			        			   }else{
			        				   lneedHeight = imgHeight;	  
			        			   }
			        			   if(imgwidth>Integer.parseInt(imageWidthPro)){
			        				   lneedWidth = Integer.parseInt(imageWidthPro);	  
			        			   }else{
			        				   lneedWidth = imgwidth;
			        			   }
			        			   String limgSourcePath=getText("external.imagesfldr.path")+"complaint/web/"+compliantdid+"/"+ImageFileName;
		    		 		 		String limgDisPath = getText("external.imagesfldr.path")+"complaint/mobile/"+compliantdid;
		    		 		 	 	String limgName1 = FilenameUtils.getBaseName(ImageFileName);
		    		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
		    		 	    	 	String limageFor = "all";
		    		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
		        		   }else{
		        			   compliantsAttachMst.setIvrBnFILE_TYPE(3); 
		        			   compliantsAttachMst.setIvrBnATTACH_NAME(ImageFileName);
		        			   String destPath =getText("external.imagesfldr.path")+"complaint/mobile/"+compliantdid;
	    	        		   File destFile  = new File(destPath, ImageFileName);
	    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
	    		       	     String otherdest =getText("external.imagesfldr.path")+"complaint/web/"+compliantdid;
	    		       	     File destFileOther  = new File(otherdest, ImageFileName);
	  		       	    	FileUtils.copyFile(ImageFilePath, destFileOther);
		        		   }
		        		 session.save(compliantsAttachMst);
	        	 }
	        	 }
	         }
	      
	      Query qy = session.createQuery("update ComplaintsTblVO set complaintsTitle=:TITLE, complaintsDesc=:DESC, compliantsToFlag=:POST_TO "
	      		+ " where complaintsId=:COMPLAINTS_ID and  usrRegTblByFromUsrId.userId=:USR_ID");
	      qy.setString("TITLE", title);
	      qy.setString("DESC", desc);
	      qy.setInteger("POST_TO", Integer.parseInt(post_to));
	      qy.setInteger("COMPLAINTS_ID",Integer.parseInt(compliantdid));
	      qy.setInteger("USR_ID", Integer.parseInt(userid));
	      qy.executeUpdate();
	      
	     // Integer feedId=complains.getCompliantDetails(compliantdid,userid);
/*	      Date date;
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			System.out.println("===feed id===");
			feedMst=complains.getFeedIdValue(String.valueOf(feed_id));
			
			feedInfo.setFeedTitle(title);
			feedInfo.setFeedDesc(desc);
			feedInfo.setFeedTime(date);
			feedInfo.setEntryDatetime(date);
			feedInfo.setFeedId(feedMst.getFeedId());
			feedInfo.setFeedPrivacyFlg(2);
			System.out.println("========ff======"+feedInfo.getFeedDesc());
			System.out.println("========ff======"+feedInfo.getFeedTitle());
			boolean res=feed.feedEdit(feedInfo,"",jsonarr,fileUpload,fileUploadContentType,fileUploadFileName);
			System.out.println("=========f=======res======="+res);*/
	      
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("ComplaintsDaoServices Exception compliantsUpdate : "+ex, "error", ComplaintsDaoServices.class);
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
	    }
	    return result;
	}
	@Override
	public boolean compliantsUpdate(String compliantdid, String userid) {
		ComplaintsTblVO complaintMst = new ComplaintsTblVO();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		String qry = "";
		try {
			qry = " from ComplaintsTblVO where usrRegTblByFromUsrId.userId=:USER_ID and status<>:STATUS and complaintsId=:COMP_ID";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("USER_ID", Integer.parseInt(userid));
			qy.setInteger("COMP_ID", Integer.parseInt(compliantdid));
			qy.setInteger("STATUS", 0);
			qy.setMaxResults(1);
			complaintMst = (ComplaintsTblVO) qy.uniqueResult();
			if(complaintMst!=null){
				result=true;
			}else{
				result=false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result=false;
			System.out.println("complainDaoServices  complaintList======" + ex);
			log.logMessage(
					"complainDaoServices Exception getFamilyMembersList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return result;
	}
	@Override
	public ComplaintattchTblVO getDeleteCompliantList(String attachId) {
		ComplaintattchTblVO complaintAttachMst = new ComplaintattchTblVO();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from ComplaintattchTblVO where ivrBnATTCH_ID=:ATTACH_ID";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("ATTACH_ID", Integer.parseInt(attachId));
			//qy.setInteger("STATUS", 1);
			complaintAttachMst = (ComplaintattchTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("complainDaoServices  getDeleteCompliantList======" + ex);
			log.logMessage(
					"complainDaoServices Exception getDeleteCompliantList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return complaintAttachMst;
	}
	@Override
	public boolean deleteAttachInfo(String attachId) {
		  Session session = HibernateUtil.getSession();
		  boolean  result=false;
		    Transaction tx = null;
		    Date date1;
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		    try {
		      tx = session.beginTransaction();
		      Query qy = session.createQuery("delete from ComplaintattchTblVO where ivrBnATTCH_ID=:ATTACH_ID and ivrBnSTATUS=:STATUS");
		      qy.setInteger("STATUS", 1);
		      qy.setInteger("ATTACH_ID",Integer.parseInt(attachId));
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("ComplaintsDaoServices Exception deleteAttachInfo : "+ex, "error", ComplaintsDaoServices.class);
		    } finally {
		    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		    }
		    return result;
	}
	@Override
	public FeedsTblVO getFeedIdValue(String feedId) {
		FeedsTblVO feedMst = new FeedsTblVO();
		Session session = HibernateUtil.getSession();
		int feed_id=0;
		String qry = "";
		try {
			qry = " from FeedsTblVO where feedId=:FEED_ID ";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("FEED_ID", Integer.parseInt(feedId));
			feedMst = (FeedsTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("complainDaoServices  getFeedIdValue======" + ex);
			log.logMessage(
					"complainDaoServices Exception getFeedIdValue : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return feedMst;
	}
	@Override
	public Integer getCompliantDetails(String compliantId, String userId) {
		ComplaintsTblVO complaintMst = new ComplaintsTblVO();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		Integer  feedId=0;
		String qry = "";
		try {
			qry = " from ComplaintsTblVO where usrRegTblByFromUsrId.userId=:USER_ID  and complaintsId=:COMP_ID";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("COMP_ID", Integer.parseInt(compliantId));
			qy.setMaxResults(1);
			complaintMst = (ComplaintsTblVO) qy.uniqueResult();
			feedId=complaintMst.getFeedId();
		} catch (Exception ex) {
			ex.printStackTrace();
			result=false;
			System.out.println("complainDaoServices  getCompliantDetails======" + ex);
			log.logMessage(
					"complainDaoServices Exception getCompliantDetails : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return feedId;
	}
	
	@Override
	public List<ComplaintsTblVO> getComplaintListPost_1(String userId,
			String timestamp, String startlim, String totalrow,int societyidval,String searchtext,String statusflg,String post_to,String complaintId) {
		List<ComplaintsTblVO> complaintList = new ArrayList<ComplaintsTblVO>();
		Session session = HibernateUtil.getSession();
		String qry = "",statusflag="";
		
		try {
			String serachcrit="";
			if(complaintId!=null && !complaintId.equalsIgnoreCase("")&& complaintId.length()>0){
				serachcrit+=" and complaintsId='"+complaintId+"'";
			}
			if(statusflg!=null && !statusflg.equalsIgnoreCase("null") &&  statusflg.equalsIgnoreCase("1"))
			{
				statusflag = "in(1)";
			}
			else if(statusflg!=null && !statusflg.equalsIgnoreCase("null") &&  statusflg.equalsIgnoreCase("2"))
					{
				statusflag = "in(2,3)";
					}
			else
			{
				statusflag = "in(1,2,3)";
				
			}
			String complntTo = "";
			if(post_to!=null && !post_to.equalsIgnoreCase("null") && post_to.equalsIgnoreCase("1")){
				complntTo = " compliantsToFlag in(1,2) ";
				
			}else if(post_to!=null && !post_to.equalsIgnoreCase("null") && post_to.equalsIgnoreCase("0")){
				complntTo = " usrRegTblByFromUsrId.userId="+Integer.parseInt(userId)+" and compliantsToFlag in(0) ";
				
			}
			
			if(searchtext!=null && !searchtext.equalsIgnoreCase("null") && !searchtext.equalsIgnoreCase("")){
				qry = " from ComplaintsTblVO where  usrRegTblByFromUsrId.societyId.societyId=" +societyidval+" and status "+statusflag+" and "+complntTo+" and entryDatetime <='"+timestamp+"' "+serachcrit
						+ "  and (" + "complaintsTitle like ('%" + searchtext + "%') or " 
				             + " complaintsDesc like ('%" + searchtext + "%') or "
				              + " closereason like ('%" + searchtext + "%')"
				             + ")order by complaintsId desc";
				
			}else{
					qry = " from ComplaintsTblVO where usrRegTblByFromUsrId.societyId.societyId=" +societyidval+" and status "+statusflag+" and "+complntTo+" and entryDatetime <='"+timestamp+"'"+serachcrit
							+ " order by complaintsId desc";
			}

			System.out.println("qry111   "+qry);
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			//qy.setInteger("USER_ID", Integer.parseInt(userId));
			
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = df.parse(timestamp);
			qy.setDate("ENTRY_DATE", startDate);
			System.out.println("startDate=>"+startDate);*/
			complaintList = qy.list();
			System.out.println("complaintList=>"+complaintList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("complainDaoServices  complaintList======" + ex);
			log.logMessage(
					"complainDaoServices Exception getFamilyMembersList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return complaintList;
	}
	@Override
	public JSONObject complaintJasonpackValues(ComplaintsTblVO complainUniqId) {
		// TODO Auto-generated method stub
		JSONObject finalJsonarr = new JSONObject();
		Log log= new Log();
		Date lvrEntrydate = null;
		Common locCommonObj=new CommonDao();
		List<ComplaintattchTblVO> complaintsAttachList=new ArrayList<ComplaintattchTblVO>();
		try {
			if (complainUniqId != null) {
				if(complainUniqId.getComplaintsId()!=null){
					finalJsonarr.put("complaint_id", Commonutility.toCheckNullEmpty((complainUniqId.getComplaintsId())));
					}else{
						finalJsonarr.put("complaint_id", "");
					}
				if(complainUniqId.getComplaintsDesc()!=null){
					finalJsonarr.put("desc", Commonutility.toCheckNullEmpty(complainUniqId.getComplaintsDesc()));
					}else{
						finalJsonarr.put("desc", "");
					}
				if(complainUniqId.getComplaintsDesc()!=null){
					finalJsonarr.put("committee_desc", Commonutility.toCheckNullEmpty(complainUniqId.getClosereason()));
					}else{
						finalJsonarr.put("committee_desc", "");
					}
				if(complainUniqId.getComplaintsTitle()!=null){
					finalJsonarr.put("title", Commonutility.toCheckNullEmpty(complainUniqId.getComplaintsTitle()));
					}else{
						finalJsonarr.put("title", "");
					}
				if(Commonutility.toCheckNullEmpty(complainUniqId.getCompliantsToFlag()).equalsIgnoreCase("0")){
					finalJsonarr.put("post_to",  Commonutility.toCheckNullEmpty(complainUniqId.getCompliantsToFlag()));
					finalJsonarr.put("email_id", Commonutility.toCheckNullEmpty((complainUniqId.getComplaintsOthersEmail())));
					}
				else if(Commonutility.toCheckNullEmpty(complainUniqId.getCompliantsToFlag()).equalsIgnoreCase("1")){
					finalJsonarr.put("post_to", Commonutility.toCheckNullEmpty(complainUniqId.getCompliantsToFlag()));
					}
				else{
						finalJsonarr.put("post_to", Commonutility.toCheckNullEmpty(complainUniqId.getCompliantsToFlag()));
					}
				if(complainUniqId.getStatus()!=null){
					finalJsonarr.put("status", Commonutility.toCheckNullEmpty(complainUniqId.getStatus()));
					}else{
						finalJsonarr.put("status", "");
					}
				lvrEntrydate = complainUniqId.getEntryDatetime();
				
				if(complainUniqId.getEntryDatetime()!=null){
					finalJsonarr.put("post_date", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
					}else{
						finalJsonarr.put("post_date", "");
					}
				
				finalJsonarr.put("profile_name", Commonutility.toCheckNullEmpty(complainUniqId.getUsrRegTblByFromUsrId().getFirstName()));
				finalJsonarr.put("profile_location", Commonutility.toCheckNullEmpty(complainUniqId.getUsrRegTblByFromUsrId().getAddress1()));
				finalJsonarr.put("usr_id", Commonutility.toCheckNullEmpty(complainUniqId.getUsrRegTblByFromUsrId().getUserId()));
				if(complainUniqId.getUsrRegTblByFromUsrId().getImageNameMobile()!=null){
					finalJsonarr.put("profile_pic",Commonutility.toCheckNullEmpty(getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+complainUniqId.getUsrRegTblByFromUsrId().getImageNameMobile()));}
					else
					{
						finalJsonarr.put("profile_pic","");
					}
				
				finalJsonarr.put("feed_id", Commonutility.toCheckNullEmpty(complainUniqId.getFeedId()));
			int varComplaintAttach = complainUniqId.getComplaintsId();
			
			JSONObject comVido = new JSONObject();
			JSONObject images = new JSONObject();
			JSONArray imagesArr = new JSONArray();
			JSONArray comVidoArr = new JSONArray();
			
			boolean flag=false;
			complaintsAttachList= getComplaintAttachList(varComplaintAttach);
			System.out.println("===complaintsAttachList==="+complaintsAttachList.size());
			ComplaintattchTblVO complaintsAttachObj;
			for (Iterator<ComplaintattchTblVO> itObj = complaintsAttachList
					.iterator(); itObj.hasNext();) {
				complaintsAttachObj = itObj.next();
				flag=true;
				images = new JSONObject();
				comVido = new JSONObject();
					if(complaintsAttachObj.getIvrBnFILE_TYPE() == 1)
					{
						if(complaintsAttachObj.getIvrBnFILE_TYPE()!=null){
						images.put("img_id",String.valueOf(complaintsAttachObj.getIvrBnATTCH_ID()));
						}else{
							images.put("img_id","");
						}if(complaintsAttachObj.getIvrBnATTACH_NAME()!=null){
						images.put("img_url",getText("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/mobile/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
						}else{
							images.put("img_url","");
						}
					}
					 if(complaintsAttachObj.getIvrBnFILE_TYPE() == 2)
					{
						System.out.println("==thumb_img===="+complaintsAttachObj.getThumbImage());
						if(complaintsAttachObj.getIvrBnFILE_TYPE()!=null){
						comVido.put("video_id",String.valueOf(complaintsAttachObj.getIvrBnATTCH_ID()) );
						}else{
							comVido.put("video_id","");
						}if(complaintsAttachObj.getThumbImage()!=null){
						comVido.put("thumb_img", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/thumbimage/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getThumbImage());
						}else{
							comVido.put("thumb_img","");
						}if(complaintsAttachObj.getIvrBnATTACH_NAME()!=null){
						comVido.put("video_url", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/videos/"+complaintsAttachObj.getIvrBnCOMPLAINTS_ID()+"/"+complaintsAttachObj.getIvrBnATTACH_NAME());
						}else{
							comVido.put("video_url","");
						}
					}
					 
					 if(images!=null && images.length()>0){
					 imagesArr.put(images);
					 finalJsonarr.put("images",imagesArr);
					 }else{
						 finalJsonarr.put("images",imagesArr); 
					 }
					 if(comVido!=null && comVido.length()>0){
					 comVidoArr.put(comVido);
					finalJsonarr.put("videos",comVidoArr);
					 }else{
						 finalJsonarr.put("videos",comVidoArr); 
					 }
				}				
			if(flag==false){
				finalJsonarr.put("images",imagesArr);
				finalJsonarr.put("videos",comVidoArr);
			}
				}
		} catch(Exception ex) {
			log.logMessage("Exception found in complaintJasonpackValues:" +ex, "error", ComplaintsDaoServices.class);
			finalJsonarr = null;
		} 
		return finalJsonarr;
	}
	
	public ComplaintsTblVO ComplaintUniqResult(int complainUniqId, int rid) {
		// TODO Auto-generated method stub
		ComplaintsTblVO complaintData = new ComplaintsTblVO();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
			try {
				String qry = "From ComplaintsTblVO where complaintsId=:COMPLAINTS_ID and usrRegTblByFromUsrId.userId=:USR_ID"
						+ " and status=:STATUS ";
				Query qy = session.createQuery(qry);
				qy.setInteger("COMPLAINTS_ID",complainUniqId);
				qy.setInteger("USR_ID",rid);
				qy.setInteger("STATUS",1);
				complaintData = (ComplaintsTblVO) qy.uniqueResult();
			} catch(Exception ex) {
				log.logMessage("Exception found in getPublishSkilData :" + ex, "error", ComplaintsDaoServices.class);
				complaintData = null;
			} finally {
				session.flush();session.clear(); session.close();session = null;
			}
		return complaintData;
	}
	@Override
	public boolean updateFeedIdtoComplaint(int complaintGetObj,int retFeedId,int rid) {
		boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    try {
	      tx = session.beginTransaction();
	      Query qy = session.createQuery("update ComplaintsTblVO set feedId=:FEED_ID where complaintsId=:COMPLAINTS_ID and  usrRegTblByFromUsrId.userId=:USR_ID");
	      qy.setInteger("FEED_ID", retFeedId);
	      qy.setInteger("COMPLAINTS_ID",complaintGetObj);
	      qy.setInteger("USR_ID", rid);
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("updateFeedIdtoComplaint Exception: "+ex, "error", ComplaintsDaoServices.class);
	    } finally {
	    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
	    }
	    return result;
	}
	@Override
	public boolean additionalFeedUpdate(JSONObject jsonComplaintObj,int retFeedId) {
		// TODO Auto-generated method stub
		boolean updateFlg = false;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			if (jsonComplaintObj != null && jsonComplaintObj.length() > 0) {
				System.out.println("jsonComplaintObj toString--if--------" + jsonComplaintObj.toString());
				Query qy = session.createQuery("update FeedsTblVO set additionalData=:ADDITIONAL_DATA where feedId=:FEED_ID");
				System.out.println("retFeedId---------" + retFeedId);
				 qy.setInteger("FEED_ID",retFeedId);
			     qy.setString("ADDITIONAL_DATA",jsonComplaintObj.toString());
			     qy.executeUpdate();
			     tx.commit();
			     updateFlg = true;
			} else {
				jsonComplaintObj = new JSONObject();
				updateFlg = false;
			}
		} catch (HibernateException ex) {
		      if (tx != null) {
			        tx.rollback();
			      }
			      ex.printStackTrace();
			      updateFlg = false;
			      log.logMessage("addPostServices Exception additionalFeedUpdate : "+ex, "error", ComplaintsDaoServices.class);
		} finally {
	    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
	    }
		return updateFlg;
	}
	@Override
	public ComplaintsTblVO ComplaintUniqResultByComplaintId(int complainUniqId) {
		// TODO Auto-generated method stub
		ComplaintsTblVO complaintData = new ComplaintsTblVO();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
			try {
				String qry = "From ComplaintsTblVO where complaintsId=:COMPLAINTS_ID";
				Query qy = session.createQuery(qry);
				qy.setInteger("COMPLAINTS_ID",complainUniqId);
				complaintData = (ComplaintsTblVO) qy.uniqueResult();
			} catch(Exception ex) {
				log.logMessage("Exception found in ComplaintUniqResultByComplaintId :" + ex, "error", ComplaintsDaoServices.class);
				complaintData = null;
			} finally {
				session.flush();session.clear(); session.close();session = null;
			}
		return complaintData;
	}

}
