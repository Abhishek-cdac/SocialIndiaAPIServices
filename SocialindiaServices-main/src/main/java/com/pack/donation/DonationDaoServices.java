package com.pack.donation;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.common.mobiCommon;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.townShipMgmt.townShipMgmtDao;
import com.siservices.townShipMgmt.townShipMgmtDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpDonationAttachTblVo;
import com.socialindiaservices.vo.MvpDonationInstitutionTbl;
import com.socialindiaservices.vo.MvpDonationItemTypeTblVo;
import com.socialindiaservices.vo.MvpDonationTbl;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;


public class DonationDaoServices extends ActionSupport implements DonationDao{
	Log log = new Log();
	@Override
	public List getGatepassList(String qry, String startlim,String totalrow,String timestamp) {
		// TODO Auto-generated method stub
		List<MvpGatepassDetailTbl> GatepassList=new ArrayList<MvpGatepassDetailTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			GatepassList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getGatepassList======" + ex);
			log.logMessage("DonationDaoServices Exception getGatepassList : "
							+ ex, "error", DonationDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return GatepassList;
	}
	@Override
	public List getGatepassMasterList(String qry, String startlim,
			String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<MvpGatepassDetailTbl> GatepassMasterList=new ArrayList<MvpGatepassDetailTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(qry);
			qy.setFirstResult(Integer.parseInt(startlim));
			qy.setMaxResults(Integer.parseInt(totalrow));
			GatepassMasterList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getGatepassMasterList======" + ex);
			log.logMessage("DonationDaoServices Exception getGatepassMasterList : "
							+ ex, "error", DonationDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return GatepassMasterList;
	}
	@Override
	public Integer saveDonationBookingData(MvpDonationTbl DonationObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int bookingId = -1;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(DonationObj);
			bookingId = DonationObj.getDonateId();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			bookingId = -1;
			System.out.println("Step -1 : Exception found DonationDaoServices.staffCreation() : "+ex);
			logwrite.logMessage("Step -1 : Exception found staffCreation() : "+ex, "error", DonationDaoServices.class);

		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return bookingId;
	}
	@Override
	public boolean updateDonationData(MvpDonationTbl DonationObj) {
		// TODO Auto-generated method stub
		boolean result = false;
		
		Transaction tx = null;
		String appendimg="";
		Query qy = null;
		Session session = null;
		try {
		    log = new Log();
		    System.out.println("updateDonationData Start.");
		    session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String updateQry = "";
			if (DonationObj.getFeedId() != null) {
	    		if (Commonutility.checkIntempty(DonationObj.getFeedId().getFeedId())) {
		    		updateQry +="feedId.feedId=:FEED_ID,";
		    	}
	    	}	
	    	if (DonationObj.getMvpDonationCategoryMstTbl() != null) {
	    		if (Commonutility.checkIntempty(DonationObj.getMvpDonationCategoryMstTbl().getiVOCATEGORY_ID())) {
		    		updateQry +="mvpDonationCategoryMstTbl.iVOCATEGORY_ID=:CATEGORY_ID,";
		    	}
	    	}
	    	if (DonationObj.getMvpDonationSubcategoryMstTbl() != null) {
	    		if (Commonutility.checkIntempty(DonationObj.getMvpDonationSubcategoryMstTbl().getIvrBnSKILL_ID())) {
		    		updateQry +="mvpDonationSubcategoryMstTbl.ivrBnSKILL_ID=:SUBCATEGORY_ID,";
		    	}
	    	}
	    	if (Commonutility.checkempty(DonationObj.getTitle())) {
	    		updateQry +="title=:TITLE,";
	    	}
	    	if (Commonutility.checkIntempty(DonationObj.getQuantity())) {
	    		updateQry +="quantity=:QUANTITY,";
	    	}
	    	if (Commonutility.checkIntempty(DonationObj.getItemType())) {
	    		updateQry +="itemType=:ITEM_TYPE,";
	    	}
	    	if (Commonutility.checkempty(DonationObj.getDescription())) {
	    		updateQry +="description=:DESCRIPTION,";
	    	}
	    	if (Commonutility.checkempty(DonationObj.getOtherDescription())) {
	    		updateQry +="otherDescription=:OTHER_DESCRIPTION,";
	    	}
	    	if (DonationObj.getDonateTo() != null) {
	    		if (Commonutility.checkIntempty(DonationObj.getDonateTo().getInstitutionId())) {
		    		updateQry +="donateTo.institutionId=:DONATE_TO,";
		    	}
	    	}
	    	if (DonationObj.getAmount() != null) {
	    		updateQry +="amount=:AMOUNT,";
	    	}
			qy = session.createQuery("update MvpDonationTbl set " + updateQry + " modifyDate=:MODIFY_DATE where donateId=:DONATE_ID and status=:STATUS");
			qy.setTimestamp("MODIFY_DATE", DonationObj.getModifyDate());
	    	qy.setInteger("STATUS", 1);
	    	qy.setInteger("DONATE_ID", DonationObj.getDonateId());
	    	if (DonationObj.getFeedId() != null) {
	    		if (Commonutility.checkIntempty(DonationObj.getFeedId().getFeedId())) {
		    		qy.setInteger("FEED_ID", DonationObj.getFeedId().getFeedId());
		    	}
	    	}
	    	if (DonationObj.getMvpDonationCategoryMstTbl() !=null) {
	    		if (Commonutility.checkIntempty(DonationObj.getMvpDonationCategoryMstTbl().getiVOCATEGORY_ID())) {
		    		qy.setInteger("CATEGORY_ID", DonationObj.getMvpDonationCategoryMstTbl().getiVOCATEGORY_ID());
		    	}
	    	}	    	
	    	if (DonationObj.getMvpDonationSubcategoryMstTbl() != null) {
	    		if (Commonutility.checkIntempty(DonationObj.getMvpDonationSubcategoryMstTbl().getIvrBnSKILL_ID())) {
		    		qy.setInteger("SUBCATEGORY_ID", DonationObj.getMvpDonationSubcategoryMstTbl().getIvrBnSKILL_ID());
		    	}
	    	}
	    	
	    	if (Commonutility.checkempty(DonationObj.getTitle())) {
	    		qy.setString("TITLE", DonationObj.getTitle());
	    	}
	    	if (Commonutility.checkIntempty(DonationObj.getQuantity())) {
	    		qy.setInteger("QUANTITY", DonationObj.getQuantity());
	    	}
	    	if (Commonutility.checkIntempty(DonationObj.getItemType())) {
	    		qy.setInteger("ITEM_TYPE", DonationObj.getItemType());
	    	}
	    	if (Commonutility.checkempty(DonationObj.getDescription())) {
	    		qy.setString("DESCRIPTION", DonationObj.getDescription());
	    	}
	    	if (Commonutility.checkempty(DonationObj.getOtherDescription())) {
	    		qy.setString("OTHER_DESCRIPTION", DonationObj.getOtherDescription());
	    	}
	    	if (DonationObj.getDonateTo() != null) {
	    		if (Commonutility.checkIntempty(DonationObj.getDonateTo().getInstitutionId())) {
	    			qy.setInteger("DONATE_TO", DonationObj.getDonateTo().getInstitutionId());
		    	}
	    	}
	    	if (DonationObj.getAmount() != null) {
	    		qy.setFloat("AMOUNT", DonationObj.getAmount());
	    	}
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("DonationDaoServices Exception updateDonationData : "
					+ ex, "error", DonationDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
		}
		return result;
	}
	
	@Override
	public boolean deleteDonationId(String userId, String donationid) {
		// TODO Auto-generated method stub
		 Session session = HibernateUtil.getSession();
		    boolean result=false;
		    Transaction tx = null;
		    CommonUtils comutil=new CommonUtilsServices();
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update MvpDonationTbl set status=:STATUS  "
		              + " where   donateId=:donate_Id");
		      qy.setInteger("STATUS", 0);

		      qy.setInteger("donate_Id", Integer.parseInt(donationid));
		      int isupdate=qy.executeUpdate();
		      if(isupdate>0){

		    	  Query qy1 = session
				          .createQuery("update FeedsTblVO set feedStatus=0  "
				              + " where   feedType = 4 and feedTypeId ="+donationid+"");
				      int isupdate1=qy1.executeUpdate();
		      tx.commit();
		      result = true;
		      }else{
		    	  tx.rollback();
		    	  result = false;
		      }
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("DonationDaoServices Exception deleteGatepassBookingId : "+ex, "error", DonationDaoServices.class);
		    } finally {
		    	if(session!=null){ session.flush();session.clear();session.close(); session = null; }
		    }
		    return result;
	}
	
	@Override
	public List<MvpDonationInstitutionTbl> donationToList() {
		// TODO Auto-generated method stub
		List<MvpDonationInstitutionTbl> retunObj = new ArrayList<MvpDonationInstitutionTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery("from MvpDonationInstitutionTbl where status=:STATUS");
			qy.setInteger("STATUS", 1);
			retunObj = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getCarPoolingList======" + ex);
			log.logMessage("Exception donationToList : "
							+ ex, "error", DonationDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return retunObj;
	}
	
	@Override
	public boolean donationAttachInsert(int donationUniqId,List<File> fileUpload, 
			List<String> fileUploadContentType,List<String> fileUploadFileName) {
		// TODO Auto-generated method stub
		boolean result = false;
		mobiCommon mobCom=new mobiCommon();
		townShipMgmtDao townShip=new townShipMgmtDaoServices();
		HashMap<String,File> imageMap=new HashMap<String,File>();
		HashMap<String,File> videoMap=new HashMap<String,File>();
		MvpDonationAttachTblVo saveAttachData = new MvpDonationAttachTblVo();
		FunctionUtility commonutil = new FunctionUtilityServices();
		Session session = null;
		Transaction tx = null;
		try {
			 session = HibernateUtil.getSessionFactory().openSession();
			 tx = session.beginTransaction();
			 if(fileUpload != null && fileUpload.size()>0) {
			 for(int i=0;i<fileUpload.size();i++) {
        	 File uploadedFile1 = fileUpload.get(i);
        	 String fileName1 = fileUploadFileName.get(i);
             String limgName1 = FilenameUtils.getBaseName(fileName1);
 		 	 String limageFomat1 = FilenameUtils.getExtension(fileName1);
 		 	 Integer fileType = mobCom.getFileExtensionType(limageFomat1);
 		 	 //String sqlQry="SELECT max(donateId)+1 FROM MvpDonationTbl ";
 		 	 //int attach_count = townShip.getInitTotal(sqlQry);
 		 	 System.out.println("===attach_count=="+commonutil.uniqueNoFromDate());
 		 	   if(fileType==2) {
 		 		videoMap.put(limgName1+"_"+commonutil.uniqueNoFromDate()+"."+limageFomat1, uploadedFile1);
        	   } else {
        		imageMap.put(limgName1+"_"+commonutil.uniqueNoFromDate()+"."+limageFomat1, uploadedFile1);	 
        	   }
			 }   
 		 	 System.out.println("========donationAttachInsert===videoMap=====size======="+videoMap.size());
        	 System.out.println("=========donationAttachInsert==videoMap========="+videoMap);
        	 System.out.println("=========donationAttachInsert===imageMap========"+imageMap);
        	 System.out.println("========donationAttachInsert===imageMap=====size======"+imageMap.size());
        	 if(videoMap.size()>0){
        	 for (Entry<String, File> entry : videoMap.entrySet()) {
        		 saveAttachData=new MvpDonationAttachTblVo();
        		 MvpDonationTbl donationUniqObj = new MvpDonationTbl();
        		 donationUniqObj.setDonateId(donationUniqId);
        		 saveAttachData.setDonateId(donationUniqObj);
        		 saveAttachData.setStatus(1);
        		 saveAttachData.setEntryDateime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        		 saveAttachData.setModifyDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        		 System.out.println("Key = " + entry.getKey());
        		 System.out.println("Value = " + entry.getValue());
        		 String mapFileName=entry.getKey();
        		 String mapVideoName = FilenameUtils.getBaseName(mapFileName);
        		 saveAttachData.setFileType(2);  
        		 saveAttachData.setAttachName(mapFileName);
	 		 	 String destPath = getText("external.imagesfldr.path")+"donation/videos/"+donationUniqId;
	 		 	 File destFile  = new File(destPath, mapFileName);
  		       	 FileUtils.copyFile( entry.getValue(), destFile);
  		         saveAttachData.setThumbImage(mapVideoName+".jpg");
  		       	 
  		       	 if(imageMap.containsKey(mapVideoName+".jpeg")) {
  		       	  File ImageFileName=imageMap.get(mapVideoName+".jpeg");
  		       	  String destPathThumb =getText("external.imagesfldr.path")+"donation/thumbimage/"+donationUniqId;
        		  File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
        		  System.out.println("===dfg=f==iiiiii=");
	       	      FileUtils.copyFile(ImageFileName, destFileThumb);
	       	      imageMap.remove(mapVideoName+".jpeg");
  		       	   }
  		       	  System.out.println("===dfg=f==zzzzzzzzzzzzzzzzzz=");
	       	      session.save(saveAttachData);
        	 }
        	 } if(imageMap.size()>0) {
        	 for (Entry<String, File> imageentry : imageMap.entrySet()) {
        		 saveAttachData=new MvpDonationAttachTblVo();
        		 MvpDonationTbl donationUniqObj = new MvpDonationTbl();
        		 donationUniqObj.setDonateId(donationUniqId);
        		 saveAttachData.setDonateId(donationUniqObj);
        		 saveAttachData.setStatus(1);
        		 saveAttachData.setEntryDateime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        		 saveAttachData.setModifyDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
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
	        			   saveAttachData.setFileType(1); 
	        			   saveAttachData.setAttachName(ImageFileName);
	        			   int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
		        		   int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
		        		   String imageHeightPro=getText("thump.img.height");
		        		   String imageWidthPro=getText("thump.img.width");
		        		   String destPath =getText("external.imagesfldr.path")+"donation/web/"+donationUniqId;
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
		        			   String limgSourcePath=getText("external.imagesfldr.path")+"donation/web/"+donationUniqId+"/"+ImageFileName;
	    		 		 		String limgDisPath = getText("external.imagesfldr.path")+"donation/mobile/"+donationUniqId;
	    		 		 	 	String limgName1 = FilenameUtils.getBaseName(ImageFileName);
	    		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
	    		 	    	 	String limageFor = "all";
	    		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
	        		   }else{
	        			   saveAttachData.setFileType(3); 
	        			   saveAttachData.setAttachName(ImageFileName);
	        			   String destPath =getText("external.imagesfldr.path")+"donation/mobile/"+donationUniqId;
    	        		   File destFile  = new File(destPath, ImageFileName);
    		       	       FileUtils.copyFile(ImageFilePath, destFile);
    		       	       String otherdest =getText("external.imagesfldr.path")+"donation/web/"+donationUniqId;
    		       	       File destFileOther  = new File(otherdest, ImageFileName);
  		       	    	   FileUtils.copyFile(ImageFilePath, destFileOther);
	        		   }
	        		 System.out.println("saveAttachData--------" +saveAttachData);
	        		 session.save(saveAttachData);
	        		 System.out.println("saveAttachData getDonataionAttchId--------" +saveAttachData.getDonataionAttchId());
        	 }
        	 }
        	 tx.commit();
        	 result = true;
			} else {
				if (tx != null) {
					tx.rollback();
				}
			 result = false;	
			}
		} catch (Exception ex) {
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			System.out.println("donationAttachInsert======" + ex);
			log.logMessage("Exception donationAttachInsert : "+ex, "error", DonationDaoServices.class);
			result = false;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;}
		}
		return result;
	}
	
	@Override
	public MvpDonationTbl getDonationData(int donationUniqId, int rid) {
		// TODO Auto-generated method stub
		MvpDonationTbl donationData = new MvpDonationTbl();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
			try {
				String qry = "from MvpDonationTbl where donateId=:DONATE_ID and userId.userId=:USR_ID "
						+ " and status=:STATUS";
				Query qy = session.createQuery(qry);
				qy.setInteger("DONATE_ID",donationUniqId);
				qy.setInteger("USR_ID",rid);
				qy.setInteger("STATUS",1);
				donationData = (MvpDonationTbl) qy.uniqueResult();
			} catch(Exception ex) {
				log.logMessage("Exception found in getDonationData :" + ex, "error", DonationDaoServices.class);
				donationData = null;
			} finally {
				session.flush();session.clear(); session.close();session = null;
			}
		return donationData;
	}
	
	@Override
	public MvpDonationAttachTblVo getDonationAttachData(String attachId) {
		// TODO Auto-generated method stub
		MvpDonationAttachTblVo getDataObj = new MvpDonationAttachTblVo();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
			try {
				String qry = "from MvpDonationAttachTblVo where status=:STATUS and donataionAttchId=:DONATION_ATTACH_ID";
				Query qy = session.createQuery(qry);
				qy.setInteger("DONATION_ATTACH_ID",Commonutility.stringToInteger(attachId));
				qy.setInteger("STATUS",1);
				getDataObj = (MvpDonationAttachTblVo) qy.uniqueResult();
			} catch(Exception ex) {
				log.logMessage("Exception found in getDonationAttachData :" + ex, "error", DonationDaoServices.class);
				getDataObj = null;
			} finally {
				session.flush();session.clear(); session.close();session = null;
			}
		return getDataObj;
	}
	@Override
	public boolean deleteDonationAttach(String attachId) {
		// TODO Auto-generated method stub
		  Session session = HibernateUtil.getSession();
		  boolean result=false;
		  Transaction tx = null;
		    try {
		      tx = session.beginTransaction();
		      Query qy = session.createQuery("delete from MvpDonationAttachTblVo where status=:STATUS and donataionAttchId=:DONATION_ATTACH_ID");
		      qy.setInteger("STATUS", 1);
		      qy.setInteger("DONATION_ATTACH_ID",Commonutility.stringToInteger(attachId));
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("AddPostServices Exception deleteAttachInfo : "+ex, "error", DonationDaoServices.class);
		    } finally {
			  session.close();
			}
			  return result;
	}
	@Override
	public List<MvpDonationTbl> donationSearchList(String searchQury,
			String startlimit, String totalrow, String locTimeStamp) {
		// TODO Auto-generated method stub
		 List<MvpDonationTbl> donationList = new ArrayList<MvpDonationTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery("from MvpDonationTbl where status=:STATUS " + searchQury + " and entryDate <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') order by donateId desc");
			qy.setInteger("STATUS", 1);
			qy.setFirstResult(Integer.parseInt(startlimit));
			if(totalrow!=null && !totalrow.equalsIgnoreCase("0")){
			qy.setMaxResults(Integer.parseInt(totalrow));
			}
			donationList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" donationSearchList======" + ex);
			log.logMessage("Exception in donationSearchList : "
							+ ex, "error", DonationDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return donationList;
	}
	@Override
	public List<MvpDonationAttachTblVo> getDonationAttachList(String attachId) {
		// TODO Auto-generated method stub
		List<MvpDonationAttachTblVo> attachList = new ArrayList<MvpDonationAttachTblVo>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = "from MvpDonationAttachTblVo where donateId.donateId=:DONATE_ID and status=:STATUS";
			Query qy = session.createQuery(qry);
			System.out.println("seconddd");
			qy.setInteger("DONATE_ID", Integer.parseInt(attachId));
			qy.setInteger("STATUS", 1);
			System.out.println("===varDonatoinAttach=qry=="+ qry);
			attachList = qy.list();
			System.out.println("attachList=>"+attachList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getDonationAttachList======" + ex);
			log.logMessage("Exception getDonationAttachList: "+ ex, "error", DonationDaoServices.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return attachList;
	}
	@Override
	public MvpDonationItemTypeTblVo itemTypeGetDetails(MvpDonationItemTypeTblVo itemTypeObj) {
		// TODO Auto-generated method stub
		MvpDonationItemTypeTblVo itemTypeData = new MvpDonationItemTypeTblVo();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
			try {
				String qry = "from MvpDonationItemTypeTblVo where status=:STATUS and itemtypeId=:ITEM_TYPE_ID";
				Query qy = session.createQuery(qry);
				qy.setInteger("ITEM_TYPE_ID",itemTypeObj.getItemtypeId());
				qy.setInteger("STATUS",1);
				itemTypeData = (MvpDonationItemTypeTblVo) qy.uniqueResult();
			} catch(Exception ex) {
				log.logMessage("Exception found in itemTypeGetDetails :" + ex, "error", DonationDaoServices.class);
				itemTypeData = null;
			} finally {
				session.flush();session.clear(); session.close();session = null;
			}
		return itemTypeData;
	}
	@Override
	public int deletFeedAttach(int feedId) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		  int  result=0;
		    Transaction tx = null;
		    try {
		      tx = session.beginTransaction();
		      Query qy = session.createQuery("delete FeedattchTblVO where ivrBnFEED_ID=:FEED_ID");
		      qy.setInteger("FEED_ID",feedId);
		      //qy.setString("ATTACH_NAME", attachName);
		      result = qy.executeUpdate();
		      tx.commit();
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = 0;
		      log.logMessage("AddPostServices Exception deletFeedAttach : "+ex, "error", DonationDaoServices.class);
		    } finally {
		      session.close();
		    }
		    return result;
	}
	
	@Override
	public int editFeedAttach(FeedattchTblVO feedAttchObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		int attcahId = 0;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(feedAttchObj);
			attcahId=1;
			tx.commit();			
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			attcahId = -1;
			System.out.println("Step -1 : Exception found EventDaoServices.saveCreateNewEvent() : "+ex);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return attcahId;	
	}

}
