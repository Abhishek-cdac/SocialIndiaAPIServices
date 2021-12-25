package com.mobi.addpost;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.timelinefeedvo.FeedsViewTblVO;
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

public class AddPostServices extends ActionSupport implements AddPostDao{
	//delete query for addposter delete
	 Log log=new Log();
	 JsonpackDao jsonPack = new JsonpackDaoService();
	@Override
	public boolean updateAddpostVal(String userId,String postalId){
	 boolean result = false;
	
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    Date date1;
	    CommonUtils comutil=new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	    try {
	      tx = session.beginTransaction();
	      Query qy = session.createQuery("update MvpAdpostTbl set actSts=:ACT_STS where postUniqueId=:POST_UNIQUE_ID and  userId.userId=:USR_ID");
	      qy.setInteger("ACT_STS", 0);
	      qy.setInteger("POST_UNIQUE_ID",Integer.parseInt(postalId));
	      qy.setInteger("USR_ID", Integer.parseInt(userId));
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("profileDaoServices Exception getUserPrfileList : "+ex, "error", AddPostServices.class);
	    } finally {
	    	session.flush();session.clear(); session.close();session = null;
	    }
	    return result;
	}
	
	@Override
	public MvpAdpostTbl mvpAddPostDetail(String userId,String postalId) {
		MvpAdpostTbl materialMst = new MvpAdpostTbl();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From MvpAdpostTbl where postUniqueId=:POST_UNIQUE_ID and  userId.userId=:USR_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("POST_UNIQUE_ID",Integer.parseInt(postalId));
			qy.setInteger("USR_ID",Integer.parseInt(userId) );
			System.out.println("----------getMaterialView------------" + qy);
			materialMst = (MvpAdpostTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println(" getMaterialView======" + ex);
			 log.logMessage("materialDaoServices Exception getMaterialView : "+ex, "error", AddPostServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return materialMst;
	}
	
	@Override
	public List<MvpAdpostTbl> getAddPostList(MvpAdpostTbl mvpAdpObj)
	{
		List<MvpAdpostTbl> materialMst = new ArrayList<MvpAdpostTbl>() ;
		//Session session = HibernateUtil.getSession();
		try
		{
			String qry="From MvpAdpostTbl where  postUniqueId=:POST_UNIQUE_ID and  userId.userId=:USR_ID and ";
			
		}catch(Exception e)
		{
			
		}
	
		
		return materialMst;
		
	}

	@Override
	public boolean adPostInsert(MvpAdpostTbl adPost,
			List<File> fileUpload, List<String> fileUploadContentType,
			List<String> fileUploadFileName,String category_name) {
		 boolean result=false;  mobiCommon mobCom=new mobiCommon();
			JSONObject jsonObj = new JSONObject();
			 List<File> fileUploadLat = new ArrayList<File>();
			 HashMap<String,File> imageMap=new HashMap<String,File>();
			 HashMap<String,File> videoMap=new HashMap<String,File>();
			 townShipMgmtDao townShip=new townShipMgmtDaoServices();
		 MvpAdpostAttchTbl adpostAttachMst=new MvpAdpostAttchTbl();
		 MvpAdpostTbl adPostInfo=new MvpAdpostTbl();
		 FeedDAO feed=new FeedDAOService();
		 UserMasterTblVo userMst=new UserMasterTblVo();
		 SocietyMstTbl societyMst=new SocietyMstTbl();
		 profileDao profile=new profileDaoServices();
		 FeedsTblVO feedMst=new FeedsTblVO();
			Session session = HibernateUtil.getSession();
			 Transaction tx = null;
			 System.out.println("===========adPost============"+adPost.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
			 try {		    		   
	       	  tx = session.beginTransaction();
	           session.save(adPost);
	           int addId=adPost.getPostUniqueId();
	           System.out.println("=====addId===="+addId);
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
	 		 	 String sqlQry="SELECT max(postUniqueId)+1 FROM MvpAdpostTbl ";
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
	        		 adpostAttachMst=new MvpAdpostAttchTbl();
	        		 adPostInfo.setPostUniqueId(addId);
	        		 adpostAttachMst.setPostUniqueId(adPostInfo);
	        		 adpostAttachMst.setStatus(1);
	        		 adpostAttachMst.setEntryDatetime(date1);
	        		    System.out.println("Key = " + entry.getKey());
	        		    System.out.println("Value = " + entry.getValue());
	        		    String mapFileName=entry.getKey();
	        		    String mapVideoName = FilenameUtils.getBaseName(mapFileName);
	        		    adpostAttachMst.setIvrBnFILE_TYPE(2);  
	        		    adpostAttachMst.setAttachName(mapFileName);
		 		 	 	String destPath =getText("external.imagesfldr.path")+"addpost/videos/"+addId;
	  	        		   File destFile  = new File(destPath, mapFileName);
	  		       	    	FileUtils.copyFile( entry.getValue(), destFile);
	  		       	    	adpostAttachMst.setThumbImage(mapVideoName+".jpg");
	  		       	    	if(imageMap.containsKey(mapVideoName+".jpeg")){
	  		       	   File ImageFileName=imageMap.get(mapVideoName+".jpeg");
	  		       	String destPathThumb =getText("external.imagesfldr.path")+"addpost/thumbimage/"+addId;
	        		   File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
	        		   System.out.println("===dfg=f==iiiiii=");
		       	    	FileUtils.copyFile(ImageFileName, destFileThumb);
		       	    	imageMap.remove(mapVideoName+".jpeg");
	  		       	    	}
	  		       	   System.out.println("===dfg=f==zzzzzzzzzzzzzzzzzz=");
		       	     session.save(adpostAttachMst);
	        	 }
	        	 } if(imageMap.size()>0){
	        	 for (Entry<String, File> imageentry : imageMap.entrySet()) {
	        		 adpostAttachMst=new MvpAdpostAttchTbl();
	        		 adPostInfo.setPostUniqueId(addId);
	        		 adpostAttachMst.setPostUniqueId(adPostInfo);
	        		 adpostAttachMst.setStatus(1);
	        		 adpostAttachMst.setEntryDatetime(date1);
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
		        			   adpostAttachMst.setIvrBnFILE_TYPE(1); 
		        			   adpostAttachMst.setAttachName(ImageFileName);
		        			   int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
			        		   int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
			        		   String imageHeightPro=getText("thump.img.height");
			        		   String imageWidthPro=getText("thump.img.width");
			        		   String destPath =getText("external.imagesfldr.path")+"addpost/web/"+addId;
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
			        			   String limgSourcePath=getText("external.imagesfldr.path")+"addpost/web/"+addId+"/"+ImageFileName;
		    		 		 		String limgDisPath = getText("external.imagesfldr.path")+"addpost/mobile/"+addId;
		    		 		 	 	String limgName1 = FilenameUtils.getBaseName(ImageFileName);
		    		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
		    		 	    	 	String limageFor = "all";
		    		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
		        		   }else{
		        			   adpostAttachMst.setIvrBnFILE_TYPE(3); 
		        			   adpostAttachMst.setAttachName(ImageFileName);
		        			   String destPath =getText("external.imagesfldr.path")+"addpost/mobile/"+addId;
	    	        		   File destFile  = new File(destPath, ImageFileName);
	    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
	    		       	     String otherdest =getText("external.imagesfldr.path")+"addpost/web/"+addId;
	    		       	     File destFileOther  = new File(otherdest, ImageFileName);
	  		       	    	FileUtils.copyFile(ImageFilePath, destFileOther);
		        		   }
		        		 session.save(adpostAttachMst);
	        	 }
	        	 }
	         }
	         System.out.println("=============adPost.getiVOCATEGORY_ID()==============="+adPost.getiVOCATEGORY_ID());
	         System.out.println("=============adPost.getiVOCATEGORY_ID()==============="+adPost.getiVOCATEGORY_ID().getiVOACT_STAT());
	         System.out.println("=============adPost.getiVOCATEGORY_ID()==============="+adPost.getiVOCATEGORY_ID().getiVOCATEGORY_NAME());
	        	 Date date;
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	        	 userMst.setUserId(adPost.getUserId().getUserId());
					feedMst.setUsrId(userMst);
					feedMst.setFeedType(1);
					feedMst.setIsMyfeed(1);
					feedMst.setIsShared(0);
					feedMst.setFeedPrivacyFlg(2);
					feedMst.setFeedStatus(1);
					if(adPost.getPrice()!=null){
						System.out.println("df.format(adPost.getPrice()------111------- ");
					double test = adPost.getPrice();
					DecimalFormat df = new DecimalFormat("#.00");
					df.setMaximumFractionDigits(2);
					System.out.println("df.format(adPost.getPrice()------------- " + df.format(test));
					//System.out.println("df.format(adPost.getPrice()------------- " + df.format(adPost.getPrice()));
					feedMst.setFeedTitle(df.format(test));
					}
					if((adPost.getPostTitle()!=null)){
					feedMst.setFeedStitle(adPost.getPostTitle());
					}
					if(adPost.getShortDesc()!=null && adPost.getDescr()!=null){
					feedMst.setFeedDesc(adPost.getShortDesc()+"<br>"+adPost.getDescr());
					} 
					if(category_name!=null){
					feedMst.setFeedCategory(category_name);
					}
					feedMst.setFeedTypeId(adPost.getPostUniqueId());
					feedMst.setFeedTime(date);
					feedMst.setEntryDatetime(date);
					feedMst.setModifyDatetime(date);
					System.out.println("=========fgh===========");
					userMst=profile.getUserPrfileInfo(String.valueOf(adPost.getUserId().getUserId()));
					System.out.println("=========fguserMsth==========="+userMst);
					societyMst.setSocietyId(userMst.getSocietyId().getSocietyId());
					feedMst.setSocietyId(societyMst);
					feedMst.setEntryBy(adPost.getUserId().getUserId());
					feedMst.setPostBy(adPost.getUserId().getUserId());
					if(userMst!=null && userMst.getFirstName()!=null && !userMst.getFirstName().equalsIgnoreCase("")){
						feedMst.setOriginatorName(Commonutility.toCheckNullEmpty(userMst.getFirstName())+Commonutility.toCheckNullEmpty(userMst.getLastName()));
						}
						if(userMst!=null && userMst.getCityId()!=null && userMst.getCityId().getCityName()!=null && !userMst.getCityId().getCityName().equalsIgnoreCase("")){
							feedMst.setFeedLocation(userMst.getCityId().getCityName());
						}
						feedMst.setOriginatorId(userMst.getUserId());
					int feedid=feed.feedInsert("",feedMst,fileUpload,fileUploadContentType,fileUploadFileName);
					System.out.println("=============feedid============"+feedid);
					 Query qy = session.createQuery("update MvpAdpostTbl set feedId=:FEED_ID "
					      		+ " where postUniqueId=:AD_POST_ID and  userId.userId=:USR_ID");
			         qy.setInteger("FEED_ID",feedid);
				      qy.setInteger("AD_POST_ID",adPost.getPostUniqueId());
				      qy.setInteger("USR_ID", adPost.getUserId().getUserId());
				      qy.executeUpdate();
	        	 
	         
	           tx.commit();
	           result = true;
	        	 
	        
	} catch (HibernateException ex) {	
		if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
		System.out.println("AddPostServices found compliantsPostInsert  : "+ex);
		 log.logMessage("AddPostServices Exception compliantsPostInsert : "+ex, "error", AddPostServices.class);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		if(session!=null){session.flush();session.clear();session.close();session=null;}
	}		   
	return result;
	}

	@Override
	public boolean checkAddPostExist(String addId, String rid) {
		MvpAdpostTbl addPostMst = new MvpAdpostTbl();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		String qry = "";
		try {
			qry = " from MvpAdpostTbl where userId.userId=:USER_ID and actSts<>:STATUS and postUniqueId=:ADD_ID";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("USER_ID", Integer.parseInt(rid));
			qy.setInteger("ADD_ID", Integer.parseInt(addId));
			qy.setInteger("STATUS", 0);
			qy.setMaxResults(1);
			addPostMst = (MvpAdpostTbl) qy.uniqueResult();
			if(addPostMst!=null){
				result=true;
			}else{
				result=false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result=false;
			System.out.println("AddPostServices  checkAddPostExist======" + ex);
			log.logMessage(
					"AddPostServices Exception checkAddPostExist : "
							+ ex, "error", AddPostServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return result;
	}

	@Override
	public MvpAdpostAttchTbl getDeleteAddPostList(String attachId) {
		MvpAdpostAttchTbl adPostAttach=new MvpAdpostAttchTbl();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpAdpostAttchTbl where adpostAttchId=:ATTACH_ID";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("ATTACH_ID", Integer.parseInt(attachId));
			//qy.setInteger("STATUS", 1);
			adPostAttach = (MvpAdpostAttchTbl) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("AddPostServices  getDeleteAddPostList======" + ex);
			log.logMessage(
					"AddPostServices Exception getDeleteAddPostList : "
							+ ex, "error", AddPostServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return adPostAttach;
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
		      Query qy = session.createQuery("delete from MvpAdpostAttchTbl where adpostAttchId=:ATTACH_ID and status=:STATUS");
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
		      log.logMessage("AddPostServices Exception deleteAttachInfo : "+ex, "error", ComplaintsDaoServices.class);
		    } finally {
		      session.close();
		    }
		    return result;
	}

	@Override
	public boolean addPostUpdate(List<File> fileUpload,
			List<String> fileUploadContentType,
			List<String> fileUploadFileName, String title,String ads_titile,String category_name,float amount,String desc,String addId, String userid,JSONArray jsonArr,String feed_id,String category) {
		 boolean result=false;  mobiCommon mobCom=new mobiCommon();
			 HashMap<String,File> imageMap=new HashMap<String,File>();
			 HashMap<String,File> videoMap=new HashMap<String,File>();
			 townShipMgmtDao townShip=new townShipMgmtDaoServices();
				FeedDAO feed=new FeedDAOService();
		 MvpAdpostAttchTbl adpostAttachMst=new MvpAdpostAttchTbl();
		 ComplaintsDao complains=new ComplaintsDaoServices();
		 AddPostDao adPost =new  AddPostServices();
			FeedsTblVO feedMst=new FeedsTblVO();
			FeedsTblVO feedInfo=new FeedsTblVO();
		 MvpAdpostTbl adPostInfo=new MvpAdpostTbl();
			Session session = HibernateUtil.getSession();
			 Transaction tx = null;
			 try {
	       	  tx = session.beginTransaction();
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
	 		 	 String sqlQry="SELECT max(postUniqueId)+1 FROM MvpAdpostTbl ";
	 		 	  int attach_count = townShip.getInitTotal(sqlQry);
	 		 	  System.out.println("===attach_count=="+attach_count);
	 		 	 if(fileType==2){
	 		 		videoMap.put(limgName1+"_"+attach_count+"."+limageFomat1, uploadedFile1);
	        		 }else{
	        			 imageMap.put(limgName1+"_"+attach_count+"."+limageFomat1, uploadedFile1);	 
	        		 }
	        	 }
	        	 if(videoMap.size()>0){
	        	 for (Entry<String, File> entry : videoMap.entrySet()) {
	        		 adpostAttachMst=new MvpAdpostAttchTbl();
	        		 adPostInfo.setPostUniqueId(Integer.parseInt(addId));
	        		 adpostAttachMst.setPostUniqueId(adPostInfo);
	        		 adpostAttachMst.setStatus(1);
	        		 adpostAttachMst.setEntryDatetime(date1);
	        		    System.out.println("Key = " + entry.getKey());
	        		    System.out.println("Value = " + entry.getValue());
	        		    String mapFileName=entry.getKey();
	        		    String mapVideoName = FilenameUtils.getBaseName(mapFileName);
	        		    adpostAttachMst.setIvrBnFILE_TYPE(2);   
	        		    adpostAttachMst.setAttachName(mapFileName);
		 		 	 	String destPath =getText("external.imagesfldr.path")+"addpost/videos/"+addId;
	  	        		File destFile  = new File(destPath, mapFileName);
	  		       	    FileUtils.copyFile( entry.getValue(), destFile);
	  		       	    adpostAttachMst.setThumbImage(mapVideoName+".jpg");
	  		       	        
	  		       	    if(imageMap.containsKey(mapVideoName+".jpeg")){
	  		       	   File ImageFileName=imageMap.get(mapVideoName+".jpeg");
	  		       	String destPathThumb =getText("external.imagesfldr.path")+"addpost/thumbimage/"+addId;
	        		   File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
		       	    	FileUtils.copyFile(ImageFileName, destFileThumb);		       	    
		       	    	imageMap.remove(mapVideoName+".jpeg");
	  		       	    	}
		       	     session.save(adpostAttachMst);		       	    
	        	 }
	        	 } if(imageMap.size()>0){
	        	 for (Entry<String, File> imageentry : imageMap.entrySet()) {
	        		 adpostAttachMst=new MvpAdpostAttchTbl();
	        		 adPostInfo.setPostUniqueId(Integer.parseInt(addId));
	        		 adpostAttachMst.setPostUniqueId(adPostInfo);
	        		 adpostAttachMst.setStatus(1);
	        		 adpostAttachMst.setEntryDatetime(date1);
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
		        			   adpostAttachMst.setIvrBnFILE_TYPE(1); 
		        			   adpostAttachMst.setAttachName(ImageFileName);
		        			   int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
			        		   int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
			        		   String imageHeightPro=getText("thump.img.height");
			        		   String imageWidthPro=getText("thump.img.width");
			        		   String destPath =getText("external.imagesfldr.path")+"addpost/web/"+addId;
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
			        			   String limgSourcePath=getText("external.imagesfldr.path")+"addpost/web/"+addId+"/"+ImageFileName;
		    		 		 		String limgDisPath = getText("external.imagesfldr.path")+"addpost/mobile/"+addId;
		    		 		 	 	String limgName1 = FilenameUtils.getBaseName(ImageFileName);
		    		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
		    		 	    	 	String limageFor = "all";
		    		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
							}else{
		        			   adpostAttachMst.setIvrBnFILE_TYPE(3); 
		        			   adpostAttachMst.setAttachName(ImageFileName);
		        			   String destPath =getText("external.imagesfldr.path")+"addpost/mobile/"+addId;
	    	        		   File destFile  = new File(destPath, ImageFileName);
	    		       	    	FileUtils.copyFile(ImageFilePath, destFile);
	    		       	    	String otherdest =getText("external.imagesfldr.path")+"addpost/web/"+addId;
	    		       	     File destFileOther  = new File(otherdest, ImageFileName);
	  		       	    	FileUtils.copyFile(ImageFilePath, destFileOther);
		        		   }
		        		 session.save(adpostAttachMst);	        		 
	        	 }
	        	 }
	         }
	         
	         System.out.println("Enter edit ads post--query-----------------------");
	         Query qy = session.createQuery("update MvpAdpostTbl set iVOCATEGORY_ID=:CATEGORY_ID, postTitle=:TITLE, descr=:DESC,shortDesc=:S_TITLE,  "
	 	      		+ " price=:AMOUNT where postUniqueId=:AD_POST_ID and  userId.userId=:USR_ID");
	 	     qy. setInteger("CATEGORY_ID", Integer.parseInt(category));
	         qy.setString("TITLE", title);
	 	      qy.setString("DESC", desc);
	 	     qy.setString("S_TITLE",ads_titile);
	 	     if(amount!=0){
	 	    qy.setFloat("AMOUNT",amount);
	 	     }else{
	 	    	qy.setString("AMOUNT",null);
	 	     }
	 	      qy.setInteger("AD_POST_ID",Integer.parseInt(addId));
	 	      qy.setInteger("USR_ID", Integer.parseInt(userid));
	 	      qy.executeUpdate(); 	      	 	     

				feedMst=complains.getFeedIdValue(feed_id);
				Date date;
				date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				if(amount!=0){
					System.out.println("Feed edit amount------------");
					double test = amount;
				DecimalFormat df = new DecimalFormat("#.00");
				df.setMaximumFractionDigits(2);
				System.out.println(df.format(amount));

				System.out.println("After Feed edit amount------------" + df.format(amount));
				feedInfo.setFeedTitle(df.format(amount));
				}
				if(title!=null){
				feedInfo.setFeedStitle(title);
				}
				if(category_name!=null){
				feedInfo.setFeedCategory(category_name);
				}
				if(ads_titile!=null){
				feedInfo.setFeedDesc(ads_titile+"<br>"+desc);
				}
							
				feedInfo.setFeedTime(Commonutility.enteyUpdateInsertDateTime());
				feedInfo.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
				feedInfo.setFeedId(feedMst.getFeedId());
				feedInfo.setFeedPrivacyFlg(2);
				feedInfo.setUsrId(feedMst.getUsrId());
				feedInfo.setSocietyId(feedMst.getSocietyId());
				feedInfo.setEntryBy(feedMst.getEntryBy());
				System.out.println("Enter edit ads post--feed-----------------------");
				boolean res=feed.feedEdit(feedInfo,"",null,null,null,null);
				System.out.println("=========f=======res======="+res);
				
				tx.commit();
		        result = true;
				
				if (jsonArr != null) {
		 	    	 /* Remove feed attachment */
		        	    File deleteFile = null;
						File deleteFileTwo = null;
						File deleteVideoFile = null;
						File deleteVideoFileTwo = null;
						int getQryCnt = 0;
							String imagePathDelWeb = getText("external.imagesfldr.path")
									+ getText("external.uploadfile.feed.img.webpath")
									+ feed_id + "/";
							String imagePathDelMobi = getText("external.imagesfldr.path")
									+ getText("external.uploadfile.feed.img.mobilepath")
									+ feed_id + "/";
							deleteFile = new File(imagePathDelWeb);
							deleteFileTwo = new File(imagePathDelMobi);
							String videoDelPath = getText("external.imagesfldr.path")
									+ getText("external.uploadfile.feed.videopath")
									+ feed_id + "/";
							String videoPathDelThumb = getText("external.imagesfldr.path")
									+ getText("external.uploadfile.feed.video.thumbpath")
									+ feed_id + "/";
							deleteVideoFile = new File(videoDelPath);
							deleteVideoFileTwo = new File(videoPathDelThumb);
						try {
							System.out.println("deleteFile exists---Enter 0-----------");
							if (deleteFile != null
									&& deleteFile.exists()) {
								System.out.println("deleteFile exists---Enter 1-----------");
								FileUtils.forceDelete(deleteFile);
							}
							System.out.println("deleteFile exists---Enter 2-----------");
							if (deleteFileTwo != null
									&& deleteFileTwo.exists()) {
								System.out.println("deleteFile exists---Enter 3-----------");
								FileUtils
										.forceDelete(deleteFileTwo);
							}
							System.out.println("deleteFile exists---Enter 4-----------");
							if (deleteVideoFile != null
									&& deleteVideoFile.exists()) {
								System.out.println("deleteFile exists---Enter 5-----------");
								FileUtils.forceDelete(deleteVideoFile);
							}
							System.out.println("deleteFile exists---Enter 6----------");
							if (deleteVideoFileTwo != null
									&& deleteVideoFileTwo.exists()) {
								System.out.println("deleteFile exists---Enter 7-----------");
								FileUtils
										.forceDelete(deleteVideoFileTwo);
							}
							System.out.println("deleteFile exists---Enter 8-----------");
														
							getQryCnt = deletFeedAttach(Integer.parseInt(feed_id));							
							System.out.println("getQryCnt----------" + getQryCnt);
						} catch (Exception ex) {
							log.logMessage(
									"Ads post file delete and delet attach fileType="
											+ ex, "error",
									FeedDAOService.class);
						}
						
						 
					if (getQryCnt > 0 && result == true) {
						List<MvpAdpostAttchTbl> getAttachListData = new ArrayList<MvpAdpostAttchTbl>();
						getAttachListData = adpostAttachedList(Integer.parseInt(addId));
						System.out.println("getAttachListData---------------- " + getAttachListData.size());
						
						if (getAttachListData.size() > 0) {
							MvpAdpostAttchTbl adspostAttchObj;
							for (Iterator<MvpAdpostAttchTbl> it = getAttachListData.iterator(); it.hasNext();) {
								adspostAttchObj = it.next();
								if (adspostAttchObj.getIvrBnFILE_TYPE() == 1 || adspostAttchObj.getIvrBnFILE_TYPE() == 3) {
									String addlimgSourcePath=getText("external.imagesfldr.path")+"addpost/web/"+addId;
									String feedimagePathWeb = getText("external.imagesfldr.path") + getText("external.uploadfile.feed.img.webpath")+ feed_id;
									File srcFolder2 = new File(addlimgSourcePath);
							    	File destFolder2 = new File(feedimagePathWeb);
									boolean fileResult2 = Commonutility.copyFolder(srcFolder2,destFolder2);
									
					 		 		String addlimgDisPath = getText("external.imagesfldr.path")+"addpost/mobile/"+addId;
					 		 		String feedlimgDisPath = getText("external.imagesfldr.path")+ getText("external.uploadfile.feed.img.mobilepath")+ feed_id;
					 		 		File srcFolder3 = new File(addlimgDisPath);
							    	File destFolder3 = new File(feedlimgDisPath);
									boolean fileResult3 = Commonutility.copyFolder(srcFolder3,destFolder3);
								}
								if (adspostAttchObj.getIvrBnFILE_TYPE() == 2) {
									String addVideosSrcPath =getText("external.imagesfldr.path")+"addpost/videos/"+addId;
									String feedvideoPath = getText("external.imagesfldr.path")+ getText("external.uploadfile.feed.videopath")+ feed_id;
									File srcFolder = new File(addVideosSrcPath);
							    	File destFolder = new File(feedvideoPath);
									boolean fileResult = Commonutility.copyFolder(srcFolder,destFolder);
									
									String addVideosSrcThumb =getText("external.imagesfldr.path")+"addpost/thumbimage/"+addId;
									String feedvideoPathThumb = getText("external.imagesfldr.path")+ getText("external.uploadfile.feed.video.thumbpath")+ feed_id;
									File srcFolder1 = new File(addVideosSrcThumb);
							    	File destFolder1 = new File(feedvideoPathThumb);
									boolean fileResult1 = Commonutility.copyFolder(srcFolder1,destFolder1);
								}
								
								FeedattchTblVO feedAttchObj = new FeedattchTblVO();
								FeedsTblVO feedidObj = new FeedsTblVO();
					       	    feedidObj.setFeedId(Integer.parseInt(feed_id));
								feedAttchObj.setIvrBnFEED_ID(feedidObj);
								feedAttchObj.setIvrBnATTACH_NAME(adspostAttchObj.getAttachName());
								feedAttchObj.setIvrBnTHUMP_IMAGE(adspostAttchObj.getThumbImage());
								feedAttchObj.setIvrBnFILE_TYPE(adspostAttchObj.getIvrBnFILE_TYPE());
								feedAttchObj.setIvrBnSTATUS(1);
								feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
										.enteyUpdateInsertDateTime());
								feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
										.enteyUpdateInsertDateTime());
								int feedattachRes = editFeedAttach(feedAttchObj);
								if (feedattachRes > 0) {
									result = true;
								} else {
									result = false;
								}						
							}
						}
					}
				 }
	 	      
	           
	        	 
	        
	} catch (HibernateException ex) {	
		if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
		System.out.println("AddPostServices found compliantsPostInsert  : "+ex);
		 log.logMessage("AddPostServices Exception compliantsPostInsert : "+ex, "error", AddPostServices.class);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		if(session!=null){session.flush();session.clear();session.close();session=null;}
	}		   
	return result;
	}

	@Override
	public List<MvpAdpostTbl> getAdPostList(String userId, String timestamp,
			String startlim, String totalrow,String globalSearch,String whSrch) {
		List<MvpAdpostTbl> adPostList = new ArrayList<MvpAdpostTbl>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpAdpostTbl where "+whSrch+" and actSts<>:STATUS and entryDatetime <='"+timestamp+"' "+globalSearch+" "
					+ " order by modifyDatetime desc";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("STATUS", 0);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			adPostList = qy.list();
			System.out.println("adPostList=>"+adPostList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("AddPostServices  getAdPostList======" + ex);
			log.logMessage(
					"AddPostServices Exception getAdPostList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return adPostList;
	}

	@Override
	public List<MvpAdpostAttchTbl> getAdPostAttachList(String attachId) {
		List<MvpAdpostAttchTbl> adPostAttachList=new ArrayList<MvpAdpostAttchTbl>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpAdpostAttchTbl where postUniqueId=:POST_UNIQUE_ID";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("POST_UNIQUE_ID", Integer.parseInt(attachId));
			//qy.setInteger("STATUS", 1);
			adPostAttachList = qy.list();
			System.out.println("adPostAttachList=>"+adPostAttachList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("AddPostServices  getAdPostAttachList======" + ex);
			log.logMessage(
					"AddPostServices Exception getAdPostAttachList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return adPostAttachList;
	}

	@Override
	public List<MvpAdpostTbl> getAdPostDetailForUniqueList(String userId,
			String ad_id) {
		List<MvpAdpostTbl> adPostList = new ArrayList<MvpAdpostTbl>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from MvpAdpostTbl where userId.userId=:USER_ID and actSts<>:STATUS and postUniqueId=:AD_ID "
					+ " order by modifyDatetime desc";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("USER_ID", Integer.parseInt(userId));
			qy.setInteger("AD_ID", Integer.parseInt(ad_id));
			qy.setInteger("STATUS", 0);
			adPostList = qy.list();
			System.out.println("adPostList=>"+adPostList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("AddPostServices  getAdPostDetailForUniqueList======" + ex);
			log.logMessage(
					"AddPostServices Exception getAdPostDetailForUniqueList : "
							+ ex, "error", AddPostServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return adPostList;
	}

	@Override
	public int getadpostFeedViewId(int rid, Integer societyId, Integer feedId,
			int userid, Integer isShared,String searchFlag) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		int retValue = 0;
		Log log = new Log();
		String whQry = "";
		FeedsViewTblVO feedvo=new FeedsViewTblVO();
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			if (searchFlag.equalsIgnoreCase("2")) {
				whQry = " entryBy="
						+ rid + " and usrId=" + userid
						+ " and feedId.feedId=" + feedId
						+ " and societyId.societyId=" + societyId + " and isShared="
						+ isShared;
			} else {
				whQry = " entryBy="
						+ rid + " and usrId=" + userid
						+ " and feedId.feedId=" + feedId
						+ " and societyId.societyId=" + societyId + " and isShared="
						+ isShared;
			}
			String query = "from FeedsViewTblVO where " + whQry + "";
			System.out.println("--qq--");
			Query qy = session.createQuery(query);
			System.out.println("---w-");
			feedvo = (FeedsViewTblVO) qy.uniqueResult();
			System.out.println("-----");
			// tx.commit();
			if(feedvo!=null){
				retValue=feedvo.getUniqId();
			}
			System.out.println("0000");
			log.logMessage("###### retValue : " + retValue, "info",
					FeedDAOService.class);
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			retValue = 0;
			log.logMessage("Exception found in feedShareCountUpdate : " + ex,
					"error", FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			if (tx != null) {
				tx = null;
			}
			log = null;
		}
		return retValue;
	
	}
	
	public int deletFeedAttach(int feedid){
		Session session = HibernateUtil.getSession();
		  int  result=0;
		    Transaction tx = null;
		    try {
		      tx = session.beginTransaction();
		      Query qy = session.createQuery("delete FeedattchTblVO where ivrBnFEED_ID=:FEED_ID");
		      qy.setInteger("FEED_ID",feedid);
		      result = qy.executeUpdate();
		      tx.commit();
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = 0;
		      log.logMessage("AddPostServices Exception deletFeedAttach : "+ex, "error", ComplaintsDaoServices.class);
		    } finally {
		      session.close();
		    }
		    return result;
	}
	
	public List<MvpAdpostAttchTbl> adpostAttachedList(int addPostId) {
		List<MvpAdpostAttchTbl> adPostAttachList = new ArrayList<MvpAdpostAttchTbl>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = "from MvpAdpostAttchTbl where status=:STATUS and postUniqueId.postUniqueId='"+addPostId+"'";
			Query qy = session.createQuery(qry);
			System.out.println("get mvpaddpost attach list--------- " + qry);
			qy.setInteger("STATUS", 1);
			adPostAttachList = qy.list();
			System.out.println("adPostAttachList=>"+adPostAttachList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("AddPostServices  adpostAttachedList======" + ex);
			log.logMessage(
					"AddPostServices Exception adpostAttachedList : "
							+ ex, "error", ComplaintsDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return adPostAttachList;
	}
	
	public int editFeedAttach(FeedattchTblVO feedAttObj){
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int bookingId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(feedAttObj);
			bookingId=1;
			tx.commit();			
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			bookingId = -1;
			System.out.println("Step -1 : Exception found EventDaoServices.saveCreateNewEvent() : "+ex);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return bookingId;	
	}

	@Override
	public boolean additionalFeedUpdate(MvpAdpostTbl adPostMst) {
		// TODO Auto-generated method stub
		boolean updateFlg = false;
		JSONObject jsonAddpostObj = new JSONObject();
		FeedsTblVO feedMst = new FeedsTblVO();
		FeedDAO feed=new FeedDAOService();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			jsonAddpostObj = jsonPack.getAdsPostJasonpackValues(adPostMst);
			feedMst = feed.getFeedDetailsByEventId(adPostMst.getPostUniqueId(),1);
			if (jsonAddpostObj != null && jsonAddpostObj.length() > 0 && feedMst!=null) {
				System.out.println("jsonAddpostObj toString--if--------" + jsonAddpostObj.toString());
				//feedMst.setAdditionalData(jsonAddpostObj.toString());
				Query qy = session.createQuery("update FeedsTblVO set additionalData=:ADDITIONAL_DATA where feedId=:FEED_ID");
				System.out.println("feedMst.getFeedId()---------" + feedMst.getFeedId());
				 qy.setInteger("FEED_ID",feedMst.getFeedId());
			     qy.setString("ADDITIONAL_DATA",jsonAddpostObj.toString());
			      //qy.setInteger("USR_ID", userId);
			     qy.executeUpdate();
			     tx.commit();
			     updateFlg = true;
			} else {
				jsonAddpostObj = new JSONObject();
				System.out.println("jsonAddpostObj toString--else-------" + jsonAddpostObj.toString());
				feedMst.setAdditionalData(jsonAddpostObj.toString());
			}
		} catch (HibernateException ex) {
		      if (tx != null) {
			        tx.rollback();
			      }
			      ex.printStackTrace();
			      updateFlg = false;
			      log.logMessage("addPostServices Exception additionalFeedUpdate : "+ex, "error",AddPostServices.class);
		    } finally {
		    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		    }
		    return updateFlg;
	}
	
}
