package com.mobi.messagevo.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.jsonpack.JsonSimplepackDaoService;
import com.mobi.messagevo.ChatAttachTblVO;
import com.mobi.messagevo.ChatBlockUnblockTblVO;
import com.mobi.messagevo.ChatTblVO;
import com.mobi.messagevo.GroupChatMemTblVO;
import com.mobi.messagevo.GroupChatMstrTblVO;
import com.mobi.messagevo.GroupChatTblVO;
import com.mobi.messagevo.GrpChatAttachTblVO;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.social.load.HibernateUtil;
import com.social.utils.Log;

public class MessageDAOService extends ActionSupport implements MessageDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int chatInsert(ChatTblVO chatobj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName) {
		Log log= new Log();
		mobiCommon mobCom=new mobiCommon();
		FunctionUtility commonutil = new FunctionUtilityServices();
		int chatId = -1;
		HashMap<String,File> imageMap=new HashMap<String,File>();
		HashMap<String,File> videoMap=new HashMap<String,File>();
		ChatAttachTblVO chatAttObj = new ChatAttachTblVO();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {		    		   
      	  tx = session.beginTransaction();
          session.save(chatobj);
          chatId=chatobj.getChatId();
          System.out.println("=====chatId===="+chatId);
          if (fileUpload.size()>0) {
        	  for(int i=0;i<fileUpload.size();i++) {
        		File uploadedFile1 = fileUpload.get(i);
 	        	String fileName1 = fileUploadFileName.get(i);
 	        	String limgName1 = FilenameUtils.getBaseName(fileName1);
 	 		 	String limageFomat1 = FilenameUtils.getExtension(fileName1);
 	 		 	Integer fileType=mobCom.getFileExtensionType(limageFomat1);
 	 		 	System.out.println("===attach_count== "+commonutil.uniqueNoFromDate());
	 		 	if (fileType==2) {
	 		 		videoMap.put(limgName1+"_"+commonutil.uniqueNoFromDate()+"."+limageFomat1, uploadedFile1);
	        	} else {
	        		imageMap.put(limgName1+"_"+commonutil.uniqueNoFromDate()+"."+limageFomat1, uploadedFile1);	 
	        	}
        	  }
        	  System.out.println("===========videoMap=====size======="+videoMap.size());
        	  System.out.println("=========d==========="+videoMap);
        	  System.out.println("=========d===imageMap========"+imageMap);
        	  System.out.println("===========imageMap=====size======"+imageMap.size());
        	  if (videoMap.size()>0) {
        		 for (Entry<String, File> entry : videoMap.entrySet()) {
        			chatAttObj = new ChatAttachTblVO(); 
        			chatAttObj.setChatId(chatobj);
        			chatAttObj.setStatus(1);
        			chatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
					chatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
					System.out.println("Key = " + entry.getKey());
        		    System.out.println("Value = " + entry.getValue());
        		    String mapFileName=entry.getKey();
        		    String mapVideoName = FilenameUtils.getBaseName(mapFileName);
        		    System.out.println("=====mapVideoName==ssss=="+mapVideoName);
        		    chatAttObj.setFileType(2);
        		    chatAttObj.setAttachName(mapFileName);
        		    String destPath = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.videopath") + chatId;
	 		 	 	System.out.println("=====compId==sssssssssssssss==2=");
  	        		File destFile  = new File(destPath, mapFileName);
  		       	    FileUtils.copyFile( entry.getValue(), destFile);
					chatAttObj.setThumpImage(mapVideoName+".jpg");
					if(imageMap.containsKey(mapVideoName+".jpeg")) {
						File ImageFileName=imageMap.get(mapVideoName+".jpeg");	
						String destPathThumb = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.video.thumbpath") + chatId;
						File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
		       	    	FileUtils.copyFile(ImageFileName, destFileThumb);
		       	    	System.out.println("=============g=======================");
		       	    	imageMap.remove(mapVideoName+".jpeg");
					}
					session.save(chatAttObj);
        		 }
        		 System.out.println("======sssssssssssssss after=====imageMap=====size======"+imageMap.size());
        	  } 
        	  if (imageMap.size()>0) {
        		 for (Entry<String, File> imageentry : imageMap.entrySet()) {
        			chatAttObj = new ChatAttachTblVO();
        			chatAttObj.setChatId(chatobj);
        			chatAttObj.setStatus(1);
        			chatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
        			chatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
        			System.out.println("Key = " + imageentry.getKey());
        		    System.out.println("Value = " + imageentry.getValue());
        		    String ImageFileName=imageentry.getKey();
        		    String mapVideoName = FilenameUtils.getBaseName(ImageFileName);
        			String limageFomat = FilenameUtils.getExtension(ImageFileName);  
        		    Integer fileType=mobCom.getFileExtensionType(limageFomat);
        		    File ImageFilePath=imageMap.get(ImageFileName);
	        		int lneedWidth=0,lneedHeight = 0;
	        		if (fileType==1) {
	        			System.out.println("==ImageFileName=="+ImageFileName);
	        			chatAttObj.setFileType(1);
	        			chatAttObj.setAttachName(ImageFileName);
	        			int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
		        		int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
		        		String imageHeightPro=getText("thump.img.height");
		        		String imageWidthPro=getText("thump.img.width");
		        		String destPath = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.img.webpath") + chatId;
		        		File destFile  = new File(destPath, ImageFileName);
		       	    	FileUtils.copyFile(ImageFilePath, destFile);
	        			 if(imgHeight>Integer.parseInt(imageHeightPro)) {
	        				 lneedHeight = Integer.parseInt(imageHeightPro);	
	    	        		 //mobile - small image
	        			 } else {
	        				 lneedHeight = imgHeight;	  
	        			 }
	        			 if(imgwidth>Integer.parseInt(imageWidthPro)) {
	        				 lneedWidth = Integer.parseInt(imageWidthPro);	  
	        			 } else {
	        				 lneedWidth = imgwidth;
	        			 }
	        			String limgSourcePath=getText("external.imagesfldr.path") + getText("external.uploadfile.msg.img.webpath") + chatId + "/" +ImageFileName;
	        			String limgDisPath = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.img.mobilepath") + chatId;
	        			String limgName1 = FilenameUtils.getBaseName(ImageFileName);
		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
		 	    	 	String limageFor = "all";
		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
	        		} else {
	        			chatAttObj.setFileType(3); 
	        			chatAttObj.setAttachName(ImageFileName);
	        			String destPath =getText("external.imagesfldr.path") + getText("external.uploadfile.msg.img.mobilepath") + chatId;
 	        		   	File destFile  = new File(destPath, ImageFileName);
 		       	    	FileUtils.copyFile(ImageFilePath, destFile);
 		       	    	String otherdest =getText("external.imagesfldr.path") + getText("external.uploadfile.msg.img.webpath") + chatId;
 		       	    	File destFileOther  = new File(otherdest, ImageFileName);
		       	    	FileUtils.copyFile(ImageFilePath, destFileOther);
	        		}
	        		session.save(chatAttObj);
        		 }
        	  }
          }
          tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			chatId = -1;
			System.out.println("MessageDAOService found chatInsert  : "+ex);
			log.logMessage("MessageDAOService Exception chatInsert : "+ex, "error", MessageDAOService.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}	
		return chatId;
		
		/*Log log= new Log();
		Session session = null;
		Transaction tx = null;
		int chatId = -1;
		HashMap<String,File> imageFileMap = new HashMap<String,File>();
		HashMap<String,File> videoFileMap = new HashMap<String,File>();		
		FunctionUtility commonutil = new FunctionUtilityServices();
		mobiCommon mobComm=new mobiCommon();
		try {
			log.logMessage("Ente into chat insert", "info", MessageDAOService.class);
			boolean commitFlg = true;
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();			
			session.save(chatobj);
			chatId=chatobj.getChatId();
			if (chatId != -1) {
				if (fileUpload.size() > 0) {
					for(int i=0;i<fileUpload.size();i++){
						File mapFileUpload = fileUpload.get(i);
						String mapFileName = fileUploadFileName.get(i);
						System.out.println("mapFileName--------------" + mapFileName +"-----fileUploadFileName.get(i)---" + fileUploadFileName.get(i));
						String attachFileName = FilenameUtils.getBaseName(mapFileName);
						String attachFileExtension = FilenameUtils.getExtension(mapFileName);
						System.out.println("attachFileExtension--------------" + attachFileExtension +"-----FilenameUtils.getExtension(mapFileName)---" + FilenameUtils.getExtension(mapFileName));
						String uniqFileName = attachFileName + "_" + commonutil.uniqueNoFromDate() + "." + attachFileExtension;													
			            Integer fileType = mobComm.getFileExtensionType(attachFileExtension);
			            if (fileType == 2) {
			            	videoFileMap.put(uniqFileName, mapFileUpload);
			            } else {
			            	imageFileMap.put(uniqFileName, mapFileUpload);
			            }
					}
					System.out.println("===========videoFileMap=====size======="+videoFileMap.size());
		        	 System.out.println("=========d==========="+videoFileMap);
		        	 System.out.println("=========d===imageFileMap========"+imageFileMap);
		        	 System.out.println("===========imageFileMap=====size======"+imageFileMap.size());
					ChatAttachTblVO chatAttObj = new ChatAttachTblVO();					
					if (videoFileMap.size() > 0) {
						log.logMessage("Enter into video write sendMsg post", "info", MessageDAOService.class);
						String videoPath = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.videopath") + chatId + "/";
						String videoPathThumb = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.video.thumbpath") + chatId + "/";
						log.logMessage(videoPath + " :path: " + videoPathThumb, "info", MessageDAOService.class);
						for (Entry<String, File> entry : videoFileMap.entrySet()) {
							log.logMessage("Ente  sendMsg attach video filename:" + entry.getKey(), "info", MessageDAOService.class);
							log.logMessage("Ente sendMsg attach video filen:" + entry.getValue(), "info", MessageDAOService.class);
							String videoName = entry.getKey();
							String videoThumbName = FilenameUtils.getBaseName(videoName) + ".jpg";
							System.out.println("SendMsg videoThumbName:" + videoThumbName);
							try {
								File destFile  = new File(videoPath, videoName);
								FileUtils.copyFile( entry.getValue(), destFile);
								if (imageFileMap.containsKey(videoThumbName)) {
									File imageThumb = imageFileMap.get(videoThumbName);
									File destThumbFile  = new File(videoPathThumb, videoThumbName);
									FileUtils.copyFile(imageThumb, destThumbFile);
									imageFileMap.remove(videoThumbName);
								} else {
									log.logMessage("######### SendMsg Thump image not found #########", "info", MessageDAOService.class);
								}
							} catch (Exception ex) {
								commitFlg = false;
								log.logMessage("Exception SendMsg ffound in video file upload : "+ex, "error", MessageDAOService.class);
							}
							chatAttObj.setChatId(chatobj);
							chatAttObj.setAttachName(videoName);
							chatAttObj.setThumpImage(videoThumbName);
							chatAttObj.setFileType(2);
							chatAttObj.setStatus(1);
							chatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							chatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							session.save(chatAttObj);
						}
						log.logMessage("End video write SendMsg post", "info", MessageDAOService.class);
					}
					if (imageFileMap.size() > 0) {
						log.logMessage("Enter into image write feed post", "info", MessageDAOService.class);
						String imagePathWeb = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.img.webpath") + chatId + "/";
						String imagePathMobi = getText("external.imagesfldr.path") + getText("external.uploadfile.msg.img.mobilepath") + chatId + "/";
						for (Entry<String, File> entry : imageFileMap.entrySet()) {
							log.logMessage("Ente  SendMsg attach image filename:" + entry.getKey(), "info", MessageDAOService.class);
							log.logMessage("Ente SendMsg attach image filen:" + entry.getValue(), "info", MessageDAOService.class);
							String imageName = entry.getKey();
							String imgBaseName = FilenameUtils.getBaseName(imageName);
							String imgExtenName = FilenameUtils.getExtension(imgBaseName);
							File imgFilePath = imageFileMap.get(imageName);
							Integer fileType=mobComm.getFileExtensionType(imgExtenName);
							
							chatAttObj.setChatId(chatobj);
							chatAttObj.setAttachName(imageName);
							chatAttObj.setFileType(1);
							chatAttObj.setStatus(1);
							chatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							chatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							if (fileType == 1) {
								chatAttObj.setFileType(1);
								log.logMessage("SendMsg imageName web :" + imageName, "info", MessageDAOService.class);
								// for web, image upload original size
								try {
									File destFileWeb  = new File(imagePathWeb, imageName);
//									File destFileMobi  = new File(imagePathMobi, imageName);
									FileUtils.copyFile( entry.getValue(), destFileWeb);
//									FileUtils.copyFile( entry.getValue(), destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage("Exception SendMsg found in image file upload for web : "+ex, "error", MessageDAOService.class);
								}
								// for mobile, compress image upload
								int imgWitdh = 0;
								int imgHeight = 0;
								int imgOriginalWidth = mobiCommon.getImageWidth(imgFilePath);
								int imgOriginalHeight = mobiCommon.getImageHeight(imgFilePath);
								String imageHeightPro=getText("thump.img.height");
				        		String imageWidthPro=getText("thump.img.width");
								if (imgOriginalWidth>Commonutility.stringToInteger(imageWidthPro)) {
									imgWitdh = Commonutility.stringToInteger(imageWidthPro);
								} else {
									imgWitdh = imgOriginalWidth;
								}
								if (imgOriginalHeight>Commonutility.stringToInteger(imageHeightPro)) {
									imgHeight = Commonutility.stringToInteger(imageHeightPro);
								} else {
									imgHeight = imgOriginalHeight;
								}
								String imgSourcePath = imagePathWeb + imageName;
	    		 		 	 	String imgName = FilenameUtils.getBaseName(imageName);
	    		 		 	 	String imageFomat = FilenameUtils.getExtension(imageName);		 	    			 	    	 
	    		 	    	 	String imageFor = "all";
	    		 	    	 	log.logMessage("SendMsg imageName for web compress : " + imageName, "info", MessageDAOService.class);
	    		 	    	 	ImageCompress.toCompressImage(imgSourcePath, imagePathMobi, imgName, imageFomat, imageFor, imgWitdh, imgHeight);	    		 	    	 	
							} else {
								chatAttObj.setFileType(3);
								try {
									File destFileWeb  = new File(imagePathWeb, imageName);
									File destFileMobi  = new File(imagePathMobi, imageName);
									FileUtils.copyFile( entry.getValue(), destFileWeb);
									FileUtils.copyFile( entry.getValue(), destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage("Exception found in SendMsg image file upload for type 3 : "+ex, "error", MessageDAOService.class);
								}								
							}
							session.save(chatAttObj);
						}
						log.logMessage("End image write SendMsg post", "info", MessageDAOService.class);
					}
				} else {
					log.logMessage("SendMsg File upload empty", "info", MessageDAOService.class);
				}
			} else {
				commitFlg = false;
			}
			if (commitFlg) {
				tx.commit();
			} else {
				if (tx != null) {
					tx.rollback();
				}
				if (chatId != -1) {
					chatId = -1;
				}
				
			}
			
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			log.logMessage("Exception : "+e, "error", MessageDAOService.class);
			chatId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return chatId;
	*/}

	@Override
	public boolean deleteGroup(GroupChatMstrTblVO groupObj) {
		Session session = null;
	    Transaction tx = null;
	    Log log = new Log();
	    boolean result = false;
	    try {	    	
	    	System.out.println("group Delete Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	//Query qury = session.createQuery("update GroupChatMstrTblVO set status=:STATUS,modifyDatetime=:MODYDATETIME where grpChatId=:GROUPID AND createrId=:CREATERID");
	    	//qury.setInteger("GROUPID", groupObj.getGrpChatId());  
	    	//qury.setInteger("STATUS", 0);
	    	//qury.setInteger("CREATERID", groupObj.getCreaterId().getUserId());
	    	//qury.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	    	Query qury = session.createQuery("delete from GroupChatMemTblVO where grpChatId.grpChatId='"+groupObj.getGrpChatId()+"' and usrId.userId ='"+groupObj.getCreaterId().getUserId()+"'");
	    	qury.executeUpdate();
	    	tx.commit();
	    	System.out.println("-----deleteGroup success-------");
	    	result = true;
	    } catch (Exception ex) {
	    	if (tx != null) {tx.rollback();}
	    	result = false;
	    	System.out.println(" deleteGroup======" + ex);
			 log.logMessage("Exception found in  deleteGroup : "+ex, "error", MessageDAOService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null;
	    }
	    return result;
	}

	@Override
	public boolean updateGroupPicture(GroupChatMstrTblVO groupObj) {
		Session session = null;
	    Transaction tx = null;
	    Log log = new Log();
	    Query qy = null;boolean result = false;
	    try {	    	
	    	System.out.println("group update Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	qy = session.createQuery("update GroupChatMstrTblVO set grpImageWeb=:IMAGEWEB,grpImageMobile=:IMAGEMOBILE,modifyDatetime=:MODYDATETIME where grpChatId=:GROUPID and createrId=:CREATERID and status=:STATUS");	 
//	    	qy.setString("GROUPNAME", groupObj.getGrpName());
	    	qy.setString("IMAGEWEB", groupObj.getGrpImageWeb());
	    	qy.setString("IMAGEMOBILE", groupObj.getGrpImageMobile());
	    	qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setInteger("GROUPID", groupObj.getGrpChatId());
	    	qy.setInteger("STATUS", 1);
		    qy.setInteger("CREATERID", groupObj.getCreaterId().getUserId());		    
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
	    	System.out.println(" updateGroupNamePicture======" + ex);
			 log.logMessage("Exception found in  updateGroupNamePicture : "+ex, "error", MessageDAOService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    return result;
	}

	@Override
	public int addGroupMember(GroupChatMemTblVO groupMemObj) {
		Log log= new Log();
		Session session = null;
		Transaction tx = null;
		int memberId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(groupMemObj);
			memberId=groupMemObj.getMemberId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception found in MessageDAOService.class : "+e);
			log.logMessage("Exception : "+e, "error", MessageDAOService.class);
			memberId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return memberId;
	}

	@Override
	public boolean removeGroupMember(GroupChatMemTblVO groupMemObj) {
		Session session = null;
	    Transaction tx = null;
	    Log log = new Log();
	    Query qy = null;boolean result = false;
	    try {	    	
	    	System.out.println("group member remove Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	qy = session.createQuery("update GroupChatMemTblVO set status=:STATUS,modifyDatetime=:MODYDATETIME where grpChatId=:GROUPID and usrId=:USERID");	 
		      qy.setInteger("STATUS", 0);
		      qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
		      qy.setInteger("GROUPID", groupMemObj.getGrpChatId().getGrpChatId());
		      qy.setInteger("USERID", groupMemObj.getUsrId().getUserId());		      
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (HibernateException ex) {
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			 log.logMessage("Exception found in  removeGroupMember : "+ex, "error", MessageDAOService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    return result;
	}

	@Override
	public int createGroup(GroupChatMstrTblVO groupObj) {
		Log log= new Log();
		Session session = null;
		Transaction tx = null;
		int grpChatId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(groupObj);
			grpChatId=groupObj.getGrpChatId();
			System.out.println("grpChatId::" + grpChatId);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception createGroupfound in MessageDAOService.class : "+e);
			log.logMessage("Exception createGroup: "+e, "error", MessageDAOService.class);
			grpChatId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return grpChatId;
	}

	@Override
	public int insertGroupMember(GroupChatMemTblVO groupMemObj) {
		Log log= new Log();
		Session session = null;
		Transaction tx = null;
		int memChatId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(groupMemObj);
			memChatId=groupMemObj.getMemberId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception insertGroupMember in MessageDAOService.class : "+e);
			log.logMessage("Exception insertGroupMember: "+e, "error", MessageDAOService.class);
			memChatId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return memChatId;
	}

	@Override
	public int groupChatInsert(GroupChatTblVO grpChatObj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName) {
		Log log= new Log();
		mobiCommon mobCom=new mobiCommon();
		FunctionUtility commonutil = new FunctionUtilityServices();
		int grpChatId = -1;
		HashMap<String,File> imageMap=new HashMap<String,File>();
		HashMap<String,File> videoMap=new HashMap<String,File>();
		GrpChatAttachTblVO grpChatAttObj = new GrpChatAttachTblVO();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {		    		   
      	  tx = session.beginTransaction();
          session.save(grpChatObj);
          grpChatId=grpChatObj.getChatId();
          System.out.println("=====grpChatId===="+grpChatId);
          if (fileUpload.size()>0) {
        	  for(int i=0;i<fileUpload.size();i++) {
        		File uploadedFile1 = fileUpload.get(i);
 	        	String fileName1 = fileUploadFileName.get(i);
 	        	String limgName1 = FilenameUtils.getBaseName(fileName1);
 	 		 	String limageFomat1 = FilenameUtils.getExtension(fileName1);
 	 		 	Integer fileType=mobCom.getFileExtensionType(limageFomat1);
 	 		 	System.out.println("===attach_count== "+commonutil.uniqueNoFromDate());
	 		 	if (fileType==2) {
	 		 		videoMap.put(limgName1+"_"+commonutil.uniqueNoFromDate()+"."+limageFomat1, uploadedFile1);
	        	} else {
	        		imageMap.put(limgName1+"_"+commonutil.uniqueNoFromDate()+"."+limageFomat1, uploadedFile1);	 
	        	}
        	  }
        	  System.out.println("===========videoMap=====size======="+videoMap.size());
        	  System.out.println("=========d==========="+videoMap);
        	  System.out.println("=========d===imageMap========"+imageMap);
        	  System.out.println("===========imageMap=====size======"+imageMap.size());
        	  if (videoMap.size()>0) {
        		 for (Entry<String, File> entry : videoMap.entrySet()) {
        			grpChatAttObj = new GrpChatAttachTblVO();
        			grpChatAttObj.setChatId(grpChatObj);
					grpChatAttObj.setStatus(1);
					grpChatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
					grpChatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
					System.out.println("Key = " + entry.getKey());
        		    System.out.println("Value = " + entry.getValue());
        		    String mapFileName=entry.getKey();
        		    String mapVideoName = FilenameUtils.getBaseName(mapFileName);
        		    System.out.println("=====mapVideoName==ssss=="+mapVideoName);
        		    grpChatAttObj.setAttachName(mapFileName);
					grpChatAttObj.setFileType(2);
        		    String destPath = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.videopath") + grpChatId;
	 		 	 	System.out.println("=====compId==sssssssssssssss==2=");
  	        		File destFile  = new File(destPath, mapFileName);
  		       	    FileUtils.copyFile( entry.getValue(), destFile);
  		       	    grpChatAttObj.setThumpImage(mapVideoName+".jpg");
					if(imageMap.containsKey(mapVideoName+".jpeg")) {
						File ImageFileName=imageMap.get(mapVideoName+".jpeg");	
						String destPathThumb = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.video.thumbpath") + grpChatId;
						File destFileThumb  = new File(destPathThumb, mapVideoName+".jpg");
		       	    	FileUtils.copyFile(ImageFileName, destFileThumb);
		       	    	System.out.println("=============g=======================");
		       	    	imageMap.remove(mapVideoName+".jpeg");
					}
					session.save(grpChatAttObj);
        		 }
        		 System.out.println("======sssssssssssssss after=====imageMap=====size======"+imageMap.size());
        	  } 
        	  if (imageMap.size()>0) {
        		 for (Entry<String, File> imageentry : imageMap.entrySet()) {
        			grpChatAttObj = new GrpChatAttachTblVO();
        			grpChatAttObj.setChatId(grpChatObj);
					grpChatAttObj.setStatus(1);
					grpChatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
					grpChatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
        			System.out.println("Key = " + imageentry.getKey());
        		    System.out.println("Value = " + imageentry.getValue());
        		    String ImageFileName=imageentry.getKey();
        		    String mapVideoName = FilenameUtils.getBaseName(ImageFileName);
        			String limageFomat = FilenameUtils.getExtension(ImageFileName);  
        		    Integer fileType=mobCom.getFileExtensionType(limageFomat);
        		    File ImageFilePath=imageMap.get(ImageFileName);
	        		int lneedWidth=0,lneedHeight = 0;
	        		if (fileType==1) {
	        			System.out.println("==ImageFileName=="+ImageFileName);
						grpChatAttObj.setFileType(1);
						grpChatAttObj.setAttachName(ImageFileName);
	        			int imgHeight=mobiCommon.getImageHeight(ImageFilePath);
		        		int imgwidth=mobiCommon.getImageWidth(ImageFilePath);
		        		String imageHeightPro=getText("thump.img.height");
		        		String imageWidthPro=getText("thump.img.width");
		        		String destPath = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.img.webpath") + grpChatId;
		        		File destFile  = new File(destPath, ImageFileName);
		       	    	FileUtils.copyFile(ImageFilePath, destFile);
	        			 if(imgHeight>Integer.parseInt(imageHeightPro)) {
	        				 lneedHeight = Integer.parseInt(imageHeightPro);	
	    	        		 //mobile - small image
	        			 } else {
	        				 lneedHeight = imgHeight;	  
	        			 }
	        			 if(imgwidth>Integer.parseInt(imageWidthPro)) {
	        				 lneedWidth = Integer.parseInt(imageWidthPro);	  
	        			 } else {
	        				 lneedWidth = imgwidth;
	        			 }
	        			String limgSourcePath=getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.img.webpath") + grpChatId + "/" +ImageFileName;
	        			String limgDisPath = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.img.mobilepath") + grpChatId;
	        			String limgName1 = FilenameUtils.getBaseName(ImageFileName);
		 		 	 	String limageFomat1 = FilenameUtils.getExtension(ImageFileName);		 	    			 	    	 
		 	    	 	String limageFor = "all";
		        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName1, limageFomat1, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
	        		} else {
	        			grpChatAttObj.setFileType(3); 
	        			grpChatAttObj.setAttachName(ImageFileName);
	        			String destPath =getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.img.mobilepath") + grpChatId;
 	        		   	File destFile  = new File(destPath, ImageFileName);
 		       	    	FileUtils.copyFile(ImageFilePath, destFile);
 		       	    	String otherdest =getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.img.webpath") + grpChatId;
 		       	    	File destFileOther  = new File(otherdest, ImageFileName);
		       	    	FileUtils.copyFile(ImageFilePath, destFileOther);
	        		}
	        		session.save(grpChatAttObj);
        		 }
        	  }
          }
          tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			grpChatId = -1;
			System.out.println("MessageDAOService found groupChatInsert  : "+ex);
			log.logMessage("MessageDAOService Exception groupChatInsert : "+ex, "error", MessageDAOService.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}	
		return grpChatId;
		
		
		/*Log log= new Log();
		Session session = null;
		Transaction tx = null;
		int grpChatId = -1;
		HashMap<String,File> imageFileMap = new HashMap<String,File>();
		HashMap<String,File> videoFileMap = new HashMap<String,File>();		
		FunctionUtility commonutil = new FunctionUtilityServices();
		mobiCommon mobComm=new mobiCommon();
		try {
			log.logMessage("Ente into Group chat insert", "info", MessageDAOService.class);
			boolean commitFlg = true;
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(grpChatObj);
			grpChatId=grpChatObj.getChatId();
			if (grpChatId != -1) {
				if (fileUpload.size() > 0) {
					for(int i=0;i<fileUpload.size();i++){
						File mapFileUpload = fileUpload.get(i);
						String mapFileName = fileUploadFileName.get(i);
						System.out.println("grp mapFileName--------------" + mapFileName +"-----fileUploadFileName.get(i)---" + fileUploadFileName.get(i));
						String attachFileName = FilenameUtils.getBaseName(mapFileName);
						String attachFileExtension = FilenameUtils.getExtension(mapFileName);
						System.out.println("grp attachFileExtension--------------" + attachFileExtension +"-----FilenameUtils.getExtension(mapFileName)---" + FilenameUtils.getExtension(mapFileName));
						String uniqFileName = attachFileName + "_" + commonutil.uniqueNoFromDate() + "." + attachFileExtension;													
			            Integer fileType = mobComm.getFileExtensionType(attachFileExtension);
			            if (fileType == 2) {
			            	videoFileMap.put(uniqFileName, mapFileUpload);
			            } else {
			            	imageFileMap.put(uniqFileName, mapFileUpload);
			            }
					}
					System.out.println("===========videoFileMap=====size======="+videoFileMap.size());
		        	 System.out.println("=========d==========="+videoFileMap);
		        	 System.out.println("=========d===imageFileMap========"+imageFileMap);
		        	 System.out.println("===========imageFileMap=====size======"+imageFileMap.size());
					GrpChatAttachTblVO grpChatAttObj = new GrpChatAttachTblVO();					
					if (videoFileMap.size() > 0) {
						log.logMessage("Enter into video write GroupChatMsg post", "info", MessageDAOService.class);
						String videoPath = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.videopath") + grpChatId + "/";
						String videoPathThumb = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.video.thumbpath") + grpChatId + "/";
						log.logMessage(videoPath + " :path: " + videoPathThumb, "info", MessageDAOService.class);
						for (Entry<String, File> entry : videoFileMap.entrySet()) {
							log.logMessage("Ente  GroupChatMsg attach video filename:" + entry.getKey(), "info", MessageDAOService.class);
							log.logMessage("Ente GroupChatMsg attach video filen:" + entry.getValue(), "info", MessageDAOService.class);
							String videoName = entry.getKey();
							String videoThumbName = FilenameUtils.getBaseName(videoName) + ".jpg";
							System.out.println("GroupChatMsg videoThumbName:" + videoThumbName);
							try {
								File destFile  = new File(videoPath, videoName);
								FileUtils.copyFile( entry.getValue(), destFile);
								if (imageFileMap.containsKey(videoThumbName)) {
									File imageThumb = imageFileMap.get(videoThumbName);
									File destThumbFile  = new File(videoPathThumb, videoThumbName);
									FileUtils.copyFile(imageThumb, destThumbFile);
									imageFileMap.remove(videoThumbName);
								} else {
									log.logMessage("######### GroupChatMsg Thump image not found #########", "info", MessageDAOService.class);
								}
							} catch (Exception ex) {
								commitFlg = false;
								log.logMessage("Exception GroupChatMsg ffound in video file upload : "+ex, "error", MessageDAOService.class);
							}
							grpChatAttObj.setChatId(grpChatObj);
							grpChatAttObj.setAttachName(videoName);
							grpChatAttObj.setThumpImage(videoThumbName);
							grpChatAttObj.setFileType(2);
							grpChatAttObj.setStatus(1);
							grpChatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							grpChatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							session.save(grpChatAttObj);
						}
						log.logMessage("End video write GroupChatMsg post", "info", MessageDAOService.class);
					}
					if (imageFileMap.size() > 0) {
						log.logMessage("Enter into image write GroupChatMsg post", "info", MessageDAOService.class);
						String imagePathWeb = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.img.webpath") + grpChatId + "/";
						String imagePathMobi = getText("external.imagesfldr.path") + getText("external.uploadfile.groupmsg.img.mobilepath") + grpChatId + "/";
						for (Entry<String, File> entry : imageFileMap.entrySet()) {
							log.logMessage("Ente  GroupChatMsg attach image filename:" + entry.getKey(), "info", MessageDAOService.class);
							log.logMessage("Ente GroupChatMsg attach image filen:" + entry.getValue(), "info", MessageDAOService.class);
							String imageName = entry.getKey();
							String imgBaseName = FilenameUtils.getBaseName(imageName);
							String imgExtenName = FilenameUtils.getExtension(imgBaseName);
							File imgFilePath = imageFileMap.get(imageName);
							Integer fileType=mobComm.getFileExtensionType(imgExtenName);
							
							grpChatAttObj.setChatId(grpChatObj);
							grpChatAttObj.setAttachName(imageName);
							grpChatAttObj.setFileType(1);
							grpChatAttObj.setStatus(1);
							grpChatAttObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
							grpChatAttObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
							if (fileType == 1) {
								grpChatAttObj.setFileType(1);
								log.logMessage("GroupChatMsg imageName web :" + imageName, "info", MessageDAOService.class);
								// for web, image upload original size
								try {
									File destFileWeb  = new File(imagePathWeb, imageName);
//									File destFileMobi  = new File(imagePathMobi, imageName);
									FileUtils.copyFile( entry.getValue(), destFileWeb);
//									FileUtils.copyFile( entry.getValue(), destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage("Exception GroupChatMsg found in image file upload for web : "+ex, "error", MessageDAOService.class);
								}
								// for mobile, compress image upload
								int imgWitdh = 0;
								int imgHeight = 0;
								int imgOriginalWidth = mobiCommon.getImageWidth(imgFilePath);
								int imgOriginalHeight = mobiCommon.getImageHeight(imgFilePath);
								String imageHeightPro=getText("thump.img.height");
				        		String imageWidthPro=getText("thump.img.width");
								if (imgOriginalWidth>Commonutility.stringToInteger(imageWidthPro)) {
									imgWitdh = Commonutility.stringToInteger(imageWidthPro);
								} else {
									imgWitdh = imgOriginalWidth;
								}
								if (imgOriginalHeight>Commonutility.stringToInteger(imageHeightPro)) {
									imgHeight = Commonutility.stringToInteger(imageHeightPro);
								} else {
									imgHeight = imgOriginalHeight;
								}
								String imgSourcePath = imagePathWeb + imageName;
	    		 		 	 	String imgName = FilenameUtils.getBaseName(imageName);
	    		 		 	 	String imageFomat = FilenameUtils.getExtension(imageName);		 	    			 	    	 
	    		 	    	 	String imageFor = "all";
	    		 	    	 	log.logMessage("GroupChatMsg imageName for web compress : " + imageName, "info", MessageDAOService.class);
	    		 	    	 	ImageCompress.toCompressImage(imgSourcePath, imagePathMobi, imgName, imageFomat, imageFor, imgWitdh, imgHeight);	    		 	    	 	
							} else {
								grpChatAttObj.setFileType(3);
								try {
									File destFileWeb  = new File(imagePathWeb, imageName);
									File destFileMobi  = new File(imagePathMobi, imageName);
									FileUtils.copyFile( entry.getValue(), destFileWeb);
									FileUtils.copyFile( entry.getValue(), destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage("Exception found in GroupChatMsg image file upload for type 3 : "+ex, "error", MessageDAOService.class);
								}								
							}
							session.save(grpChatAttObj);
						}
						log.logMessage("End image write GroupChatMsg post", "info", MessageDAOService.class);
					}
				} else {
					log.logMessage("GroupChatMsg File upload empty", "info", MessageDAOService.class);
				}
				
			} else {
				commitFlg = false;
			}
			if (commitFlg) {
				tx.commit();
			} else {
				if (tx != null) {
					tx.rollback();
				}
				if (grpChatId != -1) {
					grpChatId = -1;
				}
			}
			
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			log.logMessage("Exception found in insertGroup Msg: "+e, "error", MessageDAOService.class);
			grpChatId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return grpChatId;*/
	}

	@Override
	public List<Object[]> privateChatList(int userid, String timestamp,
			int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into privateChatList " , "info", MessageDAOService.class);			
			String sqlQuery = "select cht.to_usr_id,ur.username,ur.image_name_mobile,cht.entry_datetime "
					   + " from mvp_chat_tbl cht "
					   + "left join usr_reg_tbl ur on ur.usr_id=cht.TO_USR_ID "
					   + "where  cht.from_usr_id='" +userid+ "' and cht.entry_datetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') order by cht.entry_datetime desc limit "+startLimit+","+endLimit;
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("cht.to_usr_id", Hibernate.TEXT)
					.addScalar("ur.username", Hibernate.TEXT)
					.addScalar("ur.image_name_mobile", Hibernate.TEXT)
					.addScalar("cht.entry_datetime", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into privateChatList size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in privateChatList :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public List<Object[]> groupChatList(int userid, String timestamp,
			int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into groupChatList " , "info", MessageDAOService.class);			
			String sqlQuery = "select grp_mem.grp_chat_id,grp_mstr.grp_name,grp_mstr.grp_image_mobile, "                           
						+ "(select entry_datetime from mvp_grpchat_dtl_tbl where grp_chat_id=grp_mem.grp_chat_id order by entry_datetime desc limit 1) "
						+ "from mvp_grpchat_dtl_tbl grp_dtl "
						+ "left join mvp_grpchat_mem_tbl grp_mem "
						+ "on grp_dtl.user_id=grp_mem.usr_id "
						+ "left join mvp_grpchat_mstr_tbl grp_mstr "
						+ "on grp_mem.grp_chat_id=grp_mstr.grp_chat_id "
						+ "where grp_mem.usr_id='" +userid+ "' and grp_dtl.entry_datetime<=STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') order by grp_dtl.entry_datetime desc limit "+startLimit+","+endLimit;
			sqlQuery = "select tt.grp_chat_id,tt.grp_name,tt.grp_image,tt.msg_time,tt.grp_mem_date "
						+ "from (select grp_mem.grp_chat_id as grp_chat_id ,grp_mstr.grp_name as grp_name,grp_mstr.grp_image_mobile as grp_image, (select entry_datetime from mvp_grpchat_dtl_tbl where grp_chat_id=grp_mem.grp_chat_id order by entry_datetime desc limit 1) as msg_time, grp_mem.entry_datetime as grp_mem_date "
						+ "from mvp_grpchat_dtl_tbl grp_dtl  "
						+ "left join mvp_grpchat_mem_tbl grp_mem on grp_dtl.user_id=grp_mem.usr_id "
						+ "left join mvp_grpchat_mstr_tbl grp_mstr on grp_mem.grp_chat_id=grp_mstr.grp_chat_id "
						+ "where grp_mem.usr_id='" +userid+ "'  group by grp_mstr.grp_chat_id  "
						+ "order by case when  msg_time is not null then msg_time else grp_mem.entry_datetime end desc)tt "
						+ "where case when tt.msg_time is not null then tt.msg_time<=str_to_date('" + timestamp + "','%Y-%m-%d %H:%i:%S') else tt.grp_mem_date<=str_to_date('" + timestamp + "','%Y-%m-%d %H:%i:%S') end limit "+startLimit+","+endLimit;
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("tt.grp_chat_id", Hibernate.TEXT)
					.addScalar("tt.grp_name", Hibernate.TEXT)
					.addScalar("tt.grp_image", Hibernate.TEXT)
					.addScalar("tt.grp_mem_date", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into groupChatList size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in groupChatList :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public List<Object[]> privateChatListSearch(int userid, String srchValue,
			String timestamp, int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into privateChatListSearch " , "info", MessageDAOService.class);			
			String sqlQuery = "select ur.usr_id,ur.username,ur.image_name_mobile,cht.entry_datetime "
						+ "from mvp_chat_tbl cht "
						+ "left join usr_reg_tbl ur on ur.usr_id=cht.to_usr_id "
						+ "where ur.username like '%" +srchValue+ "%' and cht.from_usr_id='" +userid+ "' and cht.entry_datetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') order by cht.entry_datetime desc limit "+startLimit+","+endLimit;
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("ur.usr_id", Hibernate.TEXT)
					.addScalar("ur.username", Hibernate.TEXT)
					.addScalar("ur.image_name_mobile", Hibernate.TEXT)
					.addScalar("cht.entry_datetime", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into privateChatListSearch size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in privateChatListSearch :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public List<Object[]> groupChatListSearch(int userid, String srchValue,
			String timestamp, int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into groupChatListSearch " , "info", MessageDAOService.class);			
			String sqlQuery = "select grp_mem.grp_chat_id,grp_mstr.grp_name,grp_mstr.grp_image_mobile, "                           
						+ "(select entry_datetime from mvp_grpchat_dtl_tbl where grp_chat_id=grp_mem.grp_chat_id order by entry_datetime desc limit 1) "
						+ "from mvp_grpchat_dtl_tbl grp_dtl "
						+ "left join mvp_grpchat_mem_tbl grp_mem "
						+ "on grp_dtl.user_id=grp_mem.usr_id "
						+ "left join mvp_grpchat_mstr_tbl grp_mstr "
						+ "on grp_mem.grp_chat_id=grp_mstr.grp_chat_id "
						+ "where grp_mem.usr_id='" +userid+ "' and grp_dtl.entry_datetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') order by grp_dtl.entry_datetime desc limit "+startLimit+","+endLimit;
			sqlQuery = "select tt.grp_chat_id,tt.grp_name,tt.image_mobile,tt.msg_time "
					+ "from (select grp_mem.grp_chat_id as grp_chat_id ,grp_mstr.grp_name as grp_name,grp_mstr.grp_image_mobile as image_mobile, "
					+ "(select entry_datetime from mvp_grpchat_dtl_tbl where grp_chat_id=grp_mem.grp_chat_id order by entry_datetime desc limit 1) as msg_time, "
					+ "grp_mem.entry_datetime as grp_mem_date from mvp_grpchat_dtl_tbl grp_dtl  "
					+ "left join mvp_grpchat_mem_tbl grp_mem on grp_dtl.user_id=grp_mem.usr_id "
					+ "left join mvp_grpchat_mstr_tbl grp_mstr on grp_mem.grp_chat_id=grp_mstr.grp_chat_id "
					+ "where grp_mem.usr_id='" +userid+ "' and grp_mstr.grp_name like '%" + srchValue + "%' group by grp_mstr.grp_chat_id  "
					+ "order by case when  msg_time is not null then msg_time else grp_mem.entry_datetime end desc)tt "
					+ "where case when tt.msg_time is not null then  tt.msg_time<=str_to_date('" + timestamp + "','%Y-%m-%d %H:%i:%S') else tt.grp_mem_date<=str_to_date('" + timestamp + "','%Y-%m-%d %H:%i:%S') end limit "+startLimit+","+endLimit;
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("tt.grp_chat_id", Hibernate.TEXT)
					.addScalar("tt.grp_name", Hibernate.TEXT)
					.addScalar("tt.image_mobile", Hibernate.TEXT)
					.addScalar("tt.msg_time", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into groupChatListSearch size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in groupChatListSearch :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public List<Object[]> privateChatMsgList(int userid, int toId,
			String timestamp, int startLimit, int endLimit,String locSearchText) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		String srchQry = "";
		try {
			if (Commonutility.checkempty(locSearchText)) {
				srchQry = " and (chtdtl.msg_content like ('%" + locSearchText + "%') or usr.fname like ('%" + locSearchText + "%') or usr.lname like ('%" + locSearchText + "%'))";
			} else {
				srchQry = "";
			}
			log.logMessage("Enter into privateChatMsgList " , "info", MessageDAOService.class);		
			String sqlQuery = "select chtdtl.chat_id,chtdtl.from_usr_id,usr.image_name_mobile,chtdtl.msg_content,chtdtl.msg_type,chtdtl.entry_datetime,"
					+ "group_concat(concat(ifnull(chtatt.uniq_id,''),ifnull(chtatt.attach_name,''),ifnull(chtatt.thump_image,''),ifnull(chtatt.file_type,'')) separator '<sp>') as attach_name,"
					+ "chtdtl.URLS_THUMB_IMG,chtdtl.URLS_TITLE,chtdtl.URLS_PAGEURL,chtdtl.TO_USR_ID "
						+ "from mvp_chat_tbl chtdtl "
						+ " left join mvp_chat_attch_tbl chtatt on chtdtl.chat_id=chtatt.chat_id"
						+ " left join usr_reg_tbl usr on usr.usr_id=chtdtl.to_usr_id "
						+ " where ((from_usr_id='" +userid+ "' and to_usr_id='" +toId+ "') or "
						+ " (from_usr_id='" +toId+ "' and to_usr_id='" +userid+ "')) and chtdtl.status=1 and chtdtl.entry_datetime<=STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') "+ srchQry +"  "
						+ " group by chtdtl.chat_id "
						+ "order by chtdtl.chat_id desc limit "+startLimit+","+endLimit;
			System.out.println("Chat insert sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("chtdtl.chat_id", Hibernate.TEXT)
					.addScalar("chtdtl.from_usr_id", Hibernate.TEXT)
					.addScalar("usr.image_name_mobile", Hibernate.TEXT)
					.addScalar("chtdtl.msg_content", Hibernate.TEXT)
					.addScalar("chtdtl.msg_type", Hibernate.TEXT)
					.addScalar("chtdtl.entry_datetime", Hibernate.TEXT)
					.addScalar("attach_name", Hibernate.TEXT)
					.addScalar("chtdtl.URLS_THUMB_IMG", Hibernate.TEXT)
					.addScalar("chtdtl.URLS_TITLE", Hibernate.TEXT)
					.addScalar("chtdtl.URLS_PAGEURL", Hibernate.TEXT)
					.addScalar("chtdtl.TO_USR_ID", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into privateChatMsgList size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in privateChatMsgList :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public List<Object[]> groupChatMsgList(int groupId,
			String timestamp, int startLimit, int endLimit,String locSearchText) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		String srchQry = "";
		try {
			if (Commonutility.checkempty(locSearchText)) {
				srchQry = " and (chtdtl.msg_content like ('%" + locSearchText + "%') or usr.fname like ('%" + locSearchText + "%') or usr.lname like ('%" + locSearchText + "%'))";
			} else {
				srchQry = "";
			}
			log.logMessage("Enter into groupChatMsgList " , "info", MessageDAOService.class);			
			String sqlQuery = "select chtdtl.chat_id,chtdtl.user_id,usr.image_name_mobile,chtdtl.msg_content,chtdtl.msg_type,chtdtl.entry_datetime,"
					+ "group_concat(concat(ifnull(grpchtatt.uniq_id,''),ifnull(grpchtatt.attach_name,''),ifnull(grpchtatt.thump_image,''),ifnull(grpchtatt.file_type,'')) separator '<sp>') as attach_name,"
					+ "chtdtl.URLS_THUMB_IMG,chtdtl.URLS_TITLE,chtdtl.URLS_PAGEURL ,chtdtl.GRP_CHAT_ID "
						+ " from mvp_grpchat_dtl_tbl chtdtl "
						+ " left join mvp_grp_chat_attch_tbl grpchtatt on chtdtl.chat_id=grpchtatt.chat_id"
						+ " left join usr_reg_tbl usr on usr.usr_id=chtdtl.user_id "
						+ " where chtdtl.grp_chat_id='" + groupId + "' and chtdtl.status=1 and chtdtl.entry_datetime<=STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') "+ srchQry +" "
						+ " group by chtdtl.chat_id"
						+ " order by chtdtl.chat_id desc limit "+startLimit+","+endLimit;
			System.out.println("Group Chat insert  sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("chtdtl.chat_id", Hibernate.TEXT)
					.addScalar("chtdtl.user_id", Hibernate.TEXT)
					.addScalar("usr.image_name_mobile", Hibernate.TEXT)
					.addScalar("chtdtl.msg_content", Hibernate.TEXT)
					.addScalar("chtdtl.msg_type", Hibernate.TEXT)
					.addScalar("chtdtl.entry_datetime", Hibernate.TEXT)
					.addScalar("attach_name", Hibernate.TEXT)
					.addScalar("chtdtl.URLS_THUMB_IMG", Hibernate.TEXT)
					.addScalar("chtdtl.URLS_TITLE", Hibernate.TEXT)
					.addScalar("chtdtl.URLS_PAGEURL", Hibernate.TEXT)
					.addScalar("chtdtl.GRP_CHAT_ID", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into groupChatMsgList size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in groupChatMsgList :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public boolean updateGroupName(GroupChatMstrTblVO groupObj) {
		Session session = null;
	    Transaction tx = null;
	    Log log = new Log();
	    Query qy = null;boolean result = false;
	    String srchQry = "";
	    try {	    	
	    	System.out.println("group updateGroupName Start.");
	    	System.out.println("groupObj.getGrpName()-----1-------" + groupObj.getGrpName());
	    	if (Commonutility.checkempty(groupObj.getGrpImageMobile()) && Commonutility.checkempty(groupObj.getGrpImageWeb())) {
	    		srchQry = " grpImageMobile='"+groupObj.getGrpImageMobile()+"',grpImageWeb='"+groupObj.getGrpImageWeb()+"',";
	    	}
	    	System.out.println("srchQry-----2-------" + srchQry);
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	qy = session.createQuery("update GroupChatMstrTblVO set "+srchQry+" grpName=:GROUPNAME,modifyDatetime=:MODYDATETIME where grpChatId=:GROUPID and createrId.userId=:CREATERID and status=:STATUS");	 
	    	qy.setString("GROUPNAME", groupObj.getGrpName());
	    	qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setInteger("GROUPID", groupObj.getGrpChatId());
	    	qy.setInteger("STATUS", 1);
		    qy.setInteger("CREATERID", groupObj.getCreaterId().getUserId());		    
	    	int updateCnt = qy.executeUpdate();
	    	tx.commit();
	    	if (updateCnt == 0) {
	    		result = false;
	    	} else {
	    		result = true;
	    	}
	    	System.out.println("groupObj.getGrpName()-----3-------" + groupObj.getGrpName());
	    	System.out.println("groupObj.getGrpName()-----4-------" + groupObj.getGrpChatId());
	    } catch (Exception ex) {
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
	    	System.out.println(" updateGroupName======" + ex);
			 log.logMessage("Exception found in  updateGroupName : "+ex, "error", MessageDAOService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    return result;
	}
	
	
	@Override
	public boolean blockChat(ChatBlockUnblockTblVO chatObj) {
	    Log log = new Log();
	    boolean result = false;
	    try {	    	
	    	System.out.println("blockChat() Start.");
	    	System.out.println("blockChat.getFromUsrId()-----1-------" + chatObj.getFromUsrId() + "	blockChat.getToUsrId()-----1-------" + chatObj.getToUsrId());
	    	
	    	List<Object[]> list = selectBlockChat(chatObj);
	    	
	    	if(list !=null && list.size() > 0){
	    		result = updateBlockChat(chatObj);
	    	}
	    	else{
	    		int row = insertBlockChat(chatObj);
	    		if (row == -1) {
		    		result = false;
		    	} else {
		    		result = true;
		    	}
	    	}
	    } catch (Exception ex) {
	    	result = false;
	    	System.out.println(" blockChat() ======" + ex);
			log.logMessage("Exception found in  blockChat() : "+ex, "error", MessageDAOService.class);
	    } finally {
	    }
	    
	    return result;
	}
	
	public List<Object[]> selectBlockChat(ChatBlockUnblockTblVO chatObj) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into selectBlockChat() " , "info", MessageDAOService.class);	
			System.out.println("selectBlockChat.getFromUsrId()-----1-------" + chatObj.getFromUsrId() + "	selectBlockChat.getToUsrId()-----1-------" + chatObj.getToUsrId());
			String sqlQuery = "select * "
						+ "from mvp_chat_block_unblock cht "
						+ "where cht.FROM_USR_ID =" + chatObj.getFromUsrId() + " and cht.TO_USR_ID =" +chatObj.getToUsrId();
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("cht.FROM_USR_ID", Hibernate.INTEGER)
					.addScalar("cht.TO_USR_ID", Hibernate.INTEGER)
					.addScalar("cht.IS_BLOCKED", Hibernate.BOOLEAN);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into selectBlockChat() size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in selectBlockChat() :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}
	
	public boolean updateBlockChat(ChatBlockUnblockTblVO chatObj) {
		Session session = null;
	    Transaction tx = null;
	    Log log = new Log();
	    Query qy = null;
	    boolean result = false;
	    try {	    	
	    	System.out.println("updateBlockChat Start.");
	    	System.out.println("updateBlockChat.getFromUsrId()-----1-------" + chatObj.getFromUsrId() + "	updateBlockChat.getToUsrId()-----1-------" + chatObj.getToUsrId());
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	qy = session.createQuery("update ChatBlockUnblockTblVO set blocked=:IS_BLOCKED, modifyDatetime=:MODIFY_DATETIME where toUsrId=:TO_USR_ID and fromUsrId=:FROM_USR_ID ");
	    	qy.setBoolean("IS_BLOCKED", chatObj.getBlocked());
	    	qy.setTimestamp("MODIFY_DATETIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setInteger("TO_USR_ID", chatObj.getToUsrId());
	    	qy.setInteger("FROM_USR_ID", chatObj.getFromUsrId());
	    	int updateCnt = qy.executeUpdate();
	    	tx.commit();
	    	
	    	if (updateCnt == 0) {
	    		result = false;
	    	} else {
	    		result = true;
	    	}
	    } catch (Exception ex) {
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
	    	System.out.println(" updateBlockChat======" + ex);
			log.logMessage("Exception found in  updateBlockChat : "+ex, "error", MessageDAOService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    return result;
	}
	
	public int insertBlockChat(ChatBlockUnblockTblVO chatObj) {
		Log log= new Log();
		Session session = null;
		Transaction tx = null;
		int id = -1;
		try {
			System.out.println("insertBlockChat Start.");
			
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(chatObj);
			id=chatObj.getId();
			tx.commit();
			
			System.out.println("insertBlockChat End.");
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log=new Log();
			System.out.println("Exception insertBlockChat in MessageDAOService.class : "+e);
			log.logMessage("Exception insertBlockChat: "+e, "error", MessageDAOService.class);
			id=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return id;
	}
	
	@Override
	public List<Object[]> onlineUserList(int userid, String timestamp,
			int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into groupChatMsgList " , "info", MessageDAOService.class);			
			String sqlQuery = "select (case when ntwrk.parent_usr_id=usr.usr_id then  ntwrk.connected_usr_id when ntwrk.connected_usr_id=usr.usr_id then ntwrk.parent_usr_id end) as connected_usr, "
					+ "(case when ntwrk.parent_usr_id=usr.usr_id then (select username from usr_reg_tbl where usr_id=ntwrk.connected_usr_id) when ntwrk.connected_usr_id=usr.usr_id then (select username from usr_reg_tbl where usr_id=ntwrk.parent_usr_id) end) as username, "
					+ "(case when ntwrk.parent_usr_id=usr.usr_id then (select image_name_mobile from usr_reg_tbl where usr_id=ntwrk.connected_usr_id) when ntwrk.connected_usr_id=usr.usr_id then (select image_name_mobile from usr_reg_tbl where usr_id=ntwrk.parent_usr_id) end) as image_mobile, "
					+ "usr.online_status "
					+ "from usr_reg_tbl usr "
					+ "inner join mvp_network_tbl ntwrk on (usr.usr_id=ntwrk.parent_usr_id or usr.usr_id=ntwrk.connected_usr_id) "
					+ "where ntwrk.conn_sts_flg=1 and  usr.usr_id='" + userid + "' and ntwrk.entry_datetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') limit " + startLimit + "," + endLimit + " ";
			System.out.println("groupChatMsgList  sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("connected_usr", Hibernate.TEXT)
					.addScalar("username", Hibernate.TEXT)
					.addScalar("image_mobile", Hibernate.TEXT)
					.addScalar("usr.online_status", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into groupChatMsgList size :" + resultListObj.size(), "info", MessageDAOService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in groupChatMsgList :" + ex, "error", MessageDAOService.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public List<Object[]> getmessageContactsByProc(int userId, int chatType,int startLimit,String searchByName) {
		// TODO Auto-generated method stub
		Log log = new Log();
		List<Object[]> chatObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			//chat_type=== 1-All,2-friends,3-group
			String sqlQuery = "CALL PROC_CHAT_DTL(:USERID,:CHAT_TYPE,:SEARCH_BY_NAME)";
			// "call PROC_FEEDTIMELINE_VIEW(:USERID,:SOCITYKEY,:TIMESTAMP)";
			System.out.println("sqlQuery::" + sqlQuery);
			System.out.println("chatType-----------"+chatType);
			System.out.println("userId---------"+userId);
			System.out.println("searchByName--------"+searchByName);
			tx = session.beginTransaction();

			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("USERNAME", Hibernate.TEXT)
					.addScalar("PROFILE_IMAGE", Hibernate.TEXT)
					.addScalar("SOCIAL_IMAGE", Hibernate.TEXT)
					.addScalar("ONLINE_STATUS", Hibernate.TEXT)
					.addScalar("FROM_USER", Hibernate.TEXT)
					.addScalar("TO_USER", Hibernate.TEXT)
					.addScalar("MESSAGE", Hibernate.TEXT)
					.addScalar("MSG_TYPE", Hibernate.TEXT)
					.addScalar("READ_FLAG", Hibernate.TEXT)
					.addScalar("STATUS", Hibernate.TEXT)
					.addScalar("ENTRY_DATETIME", Hibernate.TIMESTAMP)
					.addScalar("CHAT_FLAG", Hibernate.TEXT)
					.addScalar("ID", Hibernate.TEXT)
					.addScalar("USR_ID", Hibernate.TEXT)
					.addScalar("GRP_USER", Hibernate.TEXT)
					.addScalar("CREATER_ID", Hibernate.TEXT);
			
			queryObj.setInteger("USERID", userId);
			queryObj.setInteger("CHAT_TYPE", chatType);
			queryObj.setString("SEARCH_BY_NAME", searchByName);
			//queryObj.setString("STARTLIMIT",Commonutility.intToString(startLimit));
			chatObj = queryObj.list();

			System.out.println(queryObj.toString());
			log.logMessage(
					"Enter into getmessageContactsByProc size :" + chatObj.size(),
					"info", MessageDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in getmessageContactsByProc :" + ex, "error",
					MessageDAOService.class);
			chatObj = null;
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			if (tx != null) {
				tx = null;
			}
			log = null;
		}
		return chatObj;
	}

	@Override
	public List<Object[]> getNotificationmessageContactsByProc(int userId) {
		// TODO Auto-generated method stub
		Log log = new Log();
		List<Object[]> chatObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			//chat_type=== 1-All,2-friends,3-group
			String sqlQuery = "CALL PROC_CHAT_UNREAD_MSG(:USERID)";
			// "call PROC_FEEDTIMELINE_VIEW(:USERID,:SOCITYKEY,:TIMESTAMP)";
			System.out.println("sqlQuery::" + sqlQuery);
			System.out.println("userId---------"+userId);
			tx = session.beginTransaction();

			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("USERNAME", Hibernate.TEXT)
					.addScalar("PROFILE_IMAGE", Hibernate.TEXT)
					.addScalar("SOCIAL_IMAGE", Hibernate.TEXT)
					.addScalar("ONLINE_STATUS", Hibernate.TEXT)
					.addScalar("FROM_USER", Hibernate.TEXT)
					.addScalar("TO_USER", Hibernate.TEXT)
					.addScalar("MESSAGE", Hibernate.TEXT)
					.addScalar("MSG_TYPE", Hibernate.TEXT)
					.addScalar("READ_FLAG", Hibernate.TEXT)
					.addScalar("STATUS", Hibernate.TEXT)
					.addScalar("ENTRY_DATETIME", Hibernate.TIMESTAMP)
					.addScalar("CHAT_FLAG", Hibernate.TEXT)
					.addScalar("ID", Hibernate.TEXT)
					.addScalar("TOTAL_UNREAD_MSG", Hibernate.TEXT)
					.addScalar("TOTAL_UNREAD_CHATS", Hibernate.TEXT);
			
			queryObj.setInteger("USERID", userId);
			chatObj = queryObj.list();

			System.out.println(queryObj.toString());
			log.logMessage(
					"Enter into getmessageContactsByProc size :" + chatObj.size(),
					"info", MessageDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in getmessageContactsByProc :" + ex, "error",
					MessageDAOService.class);
			chatObj = null;
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			if (tx != null) {
				tx = null;
			}
			log = null;
		}
		return chatObj;
	}

	@Override
	public List<ChatTblVO> getchatInsertDetails(int chatId) {
		// TODO Auto-generated method stub
		Log log = new Log();
		List<ChatTblVO> resChatdata = new ArrayList<ChatTblVO>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			String sqlQuery = "from ChatTblVO where chatId=:CHAT_ID and status=:STATUS";
			tx = session.beginTransaction();
			Query qy = session.createQuery(sqlQuery);
			qy.setInteger("CHAT_ID", chatId);
			qy.setInteger("STATUS", 1);
			resChatdata = qy.list();
			System.out.println("resChatdata----------size---" + resChatdata.size());
			log.logMessage("Enter into getchatInsertDetails size :" + resChatdata.size(),
					"info", MessageDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in getchatInsertDetails :" + ex, "error",
					MessageDAOService.class);
			resChatdata = null;
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			if (tx != null) {
				tx = null;
			}
			log = null;
		}
		return resChatdata;
	}
	
	@Override
	public List<GroupChatTblVO> getGroupChartInsertDetails(int chatId) {
		// TODO Auto-generated method stub
		Log log = new Log();
		List<GroupChatTblVO> grpChatdata = new ArrayList<GroupChatTblVO>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			String sqlQuery = "from GroupChatTblVO where chatId=:CHAT_ID and status=:STATUS";
			tx = session.beginTransaction();
			Query qy = session.createQuery(sqlQuery);
			qy.setInteger("CHAT_ID", chatId);
			qy.setInteger("STATUS", 1);
			grpChatdata = qy.list();
			System.out.println("grpChatdata----------size---" + grpChatdata.size());
			log.logMessage("Enter into getGroupChartInsertDetails size :" + grpChatdata.size(),
					"info", MessageDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in getGroupChartInsertDetails :" + ex, "error",
					MessageDAOService.class);
			grpChatdata = null;
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			if (tx != null) {
				tx = null;
			}
			log = null;
		}
		return grpChatdata;
	}

	@Override
	public JSONObject jsonChatPack(List<ChatTblVO> sendChatMsgObj, String profileimgPath,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb) {
		// TODO Auto-generated method stub
		JSONObject jsonPack = new JSONObject();
		Log log= new Log();
		FunctionUtility commonFn = new FunctionUtilityServices();
		try {
			if (sendChatMsgObj != null) {
				ChatTblVO chatDataObj = new ChatTblVO();
				for (Iterator<ChatTblVO> it = sendChatMsgObj.iterator();it.hasNext();) {
					chatDataObj = it.next();
					String msgIdPath = "";
					if (Commonutility.checkIntempty(chatDataObj.getChatId())) {
						jsonPack.put("msgid", chatDataObj.getChatId());
						msgIdPath = chatDataObj.getChatId() + "/";
					} else {
						jsonPack.put("msgid", "");
					}
					String imgMobilename = "";
					if (chatDataObj.getFromUsrId() != null) {
						jsonPack.put("usrid", chatDataObj.getFromUsrId().getUserId());
						imgMobilename = chatDataObj.getFromUsrId().getImageNameMobile();
					} else {
						jsonPack.put("usrid", "");
					}
					if (Commonutility.checkempty(profileimgPath) && Commonutility.checkempty(imgMobilename)) {
						profileimgPath = profileimgPath + Commonutility.stringToStringempty(imgMobilename) ;
						jsonPack.put("usr_img",profileimgPath);					
					} else {
						jsonPack.put("usr_img","");
					}
					if (Commonutility.checkempty(chatDataObj.getMsgContent())) {
						jsonPack.put("content", chatDataObj.getMsgContent());
					} else {
						jsonPack.put("content", "");
					}
					JSONArray dataJsonArry = new JSONArray();
					String urlThumbnail = null;
					String urlTittle = null;
					String pageurl = null;
					if (Commonutility.checkempty(chatDataObj.getUrlThumbImage())) {
						urlThumbnail = Commonutility.stringToStringempty(chatDataObj.getUrlThumbImage());
					}else{
						urlThumbnail = "";
					}
					if (Commonutility.checkempty(chatDataObj.getUrlTitle())) {
						urlTittle = Commonutility.stringToStringempty(chatDataObj.getUrlTitle());
					}else{
						urlTittle = "";
					}
					if (Commonutility.checkempty(chatDataObj.getPageUrl())) {
						pageurl = Commonutility.stringToStringempty(chatDataObj.getPageUrl());
					}else{
						pageurl = "";
					}
					if (Commonutility.checkempty(urlThumbnail) && Commonutility.checkempty(urlTittle) && Commonutility.checkempty(pageurl)) {
						ArrayList<String> thumbList = new ArrayList<String>();
						JsonSimplepackDaoService jsonsimlObj = new JsonSimplepackDaoService();
						thumbList = jsonsimlObj.spTabSplitIntoArraylist(urlThumbnail);
						ArrayList<String> pageTittleList = new ArrayList<String>();
						pageTittleList = jsonsimlObj.spTabSplitIntoArraylist(urlTittle);
						ArrayList<String> pageurlList = new ArrayList<String>();
						pageurlList = jsonsimlObj.spTabSplitIntoArraylist(pageurl);					
						System.out.println("uu---"+thumbList.size());
						System.out.println("---io--:"+pageTittleList.size());
						System.out.println("--kl--:"+pageurlList.size());
						if (thumbList.size() == pageTittleList.size()) {
							if ( pageTittleList.size() == pageurlList.size()) {
								for (int i=0;i<thumbList.size();i++) {
									JSONObject dataJsonObj = new JSONObject();
									dataJsonObj.put("thumb_img", thumbList.get(i));
									dataJsonObj.put("title", pageTittleList.get(i));
									dataJsonObj.put("pageurl", pageurlList.get(i));
									dataJsonArry.put(dataJsonObj);
									dataJsonObj=null;
								}
							}						
						}
						if (dataJsonArry != null) {
							jsonPack.put("urls",dataJsonArry);
						} else {
							jsonPack.put("urls",dataJsonArry);
						}
					} else {
						dataJsonArry = new JSONArray();
						jsonPack.put("urls",dataJsonArry);
					}
					if (Commonutility.checkIntnull(chatDataObj.getMsgType())) {
						jsonPack.put("msgtype", chatDataObj.getMsgType());
					} else {
						jsonPack.put("msgtype", "");
					}
					JSONArray jsArr = new JSONArray();
					JSONObject locJsonObj = new JSONObject();
					int varDonatoinAttach = chatDataObj.getChatId();
					
					JSONObject comVido = new JSONObject();
					JSONObject images = new JSONObject();
					JSONArray imagesArr = new JSONArray();
					JSONArray comVidoArr = new JSONArray();
					boolean fg = false;
					List<ChatAttachTblVO> chatAttachList = new ArrayList<ChatAttachTblVO>();
					chatAttachList = getChatAttachList(varDonatoinAttach);
					System.out.println("===chatAttachList==="+chatAttachList.size());
					if (chatAttachList != null) {
						ChatAttachTblVO chatAttachObj = new ChatAttachTblVO();
						for (Iterator<ChatAttachTblVO> itObj = chatAttachList.iterator();itObj.hasNext();) {
							chatAttachObj = itObj.next();
							//String fileList = chatAttachObj.getAttachName();
							fg = true;
							//if (Commonutility.checkempty(fileList)) {
								/*if (fileList.contains("<SP>")) {
									String[] fileArr = fileList.split("<SP>");
									if (fileArr.length == 4) {
										String fileType = fileArr[3];
										if (Commonutility.checkempty(fileType) && Commonutility.checkLengthNotZero(fileType)) {									
											fileType = fileType.trim();
											fg = true;
											if (fileType.equalsIgnoreCase("2")) {										
												locJsonObj.put("img_id", Commonutility.stringToStringempty(fileArr[0]));
												locJsonObj.put("img_url", imagePathMobi + msgIdPath + Commonutility.stringToStringempty(fileArr[1]));
												jsArr.put(locJsonObj);
											} else if (fileType.equalsIgnoreCase("3")) {
												locJsonObj.put("video_id", Commonutility.stringToStringempty(fileArr[0]));
												locJsonObj.put("video_url", videoPath + msgIdPath +  Commonutility.stringToStringempty(fileArr[1]));
												locJsonObj.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(fileArr[2]));
												jsArr.put(locJsonObj);
											} else if (fileType.equalsIgnoreCase("4")) {
												locJsonObj.put("url_id", Commonutility.stringToStringempty(fileArr[0]));
												locJsonObj.put("pageurl", videoPath + msgIdPath +  Commonutility.stringToStringempty(fileArr[1]));
												locJsonObj.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(fileArr[2]));
												jsArr.put(locJsonObj);
											}									
										}
									}
								} else {*/
									images = new JSONObject();
									comVido = new JSONObject();
										if (chatAttachObj.getFileType() == 1) {
											if (chatAttachObj.getFileType()!=null) {
											images.put("img_id",String.valueOf(chatAttachObj.getUniqId()));
											} else {
											images.put("img_id","");
											} if(chatAttachObj.getAttachName()!=null) {
											images.put("img_url",imagePathMobi + msgIdPath + chatAttachObj.getAttachName());
											} else {
											images.put("img_url","");
											}
										}
										if (chatAttachObj.getFileType() == 2) {
											System.out.println("==thumb_img===="+ chatAttachObj.getThumpImage());
											if (chatAttachObj.getFileType()!=null) {
											comVido.put("video_id",String.valueOf(chatAttachObj.getUniqId()));
											} else {
											comVido.put("video_id","");
											} if (chatAttachObj.getThumpImage()!=null) {
											comVido.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(chatAttachObj.getThumpImage()));
											} else {
											comVido.put("thumb_img","");
											} if (chatAttachObj.getAttachName()!=null) {
											comVido.put("video_url", videoPath + msgIdPath +  Commonutility.stringToStringempty(chatAttachObj.getAttachName()));
											} else {
											comVido.put("video_url","");
											}
										}
										if (images!=null && images.length()>0) {
											imagesArr.put(images);
											jsonPack.put("images",imagesArr);
										} else {
											jsonPack.put("images",imagesArr); 
										}
										if (comVido!=null && comVido.length()>0) {
											comVidoArr.put(comVido);
											jsonPack.put("videos",comVidoArr);
										} else {
											jsonPack.put("videos",comVidoArr); 
										}
								//}
							//}
						}
						if (fg==false) {
							jsonPack.put("images",imagesArr);
							jsonPack.put("videos",comVidoArr);
						}
					}
					//jsonPack.put("images",locJsonObj);
					//jsonPack.put("videos",jsArr);
					if(chatDataObj.getEntryDatetime() != null){
						String lastSeen = Commonutility.stringToStringempty(chatDataObj.getEntryDatetime().toString()) ;
						String shortTime = "";
						if (Commonutility.checkempty(lastSeen)) {
							shortTime = commonFn.getShortTime(lastSeen);
							lastSeen = commonFn.getLongTime(lastSeen);
						}					
						jsonPack.put("short_time",shortTime);
						jsonPack.put("full_time",lastSeen);
					} else {
						jsonPack.put("short_time","");
						jsonPack.put("full_time","");
					}
				}
			}
		} catch (Exception ex) {
			log.logMessage("Exception found in jsonChatPack:" +ex, "error",MessageDAOService.class);
			jsonPack = null;
		}
		System.out.println("############## jsonPack ##########");
		System.out.println("jsonPack:" + jsonPack);
		return jsonPack;
	}

	public List<ChatAttachTblVO> getChatAttachList(int varDonatoinAttach) {
		// TODO Auto-generated method stub
		Log log= new Log();
		List<ChatAttachTblVO> attachList = new ArrayList<ChatAttachTblVO>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = "from ChatAttachTblVO where chatId.chatId=:CHAT_ID and status=:STATUS";
			Query qy = session.createQuery(qry);
			qy.setInteger("CHAT_ID", varDonatoinAttach);
			qy.setInteger("STATUS", 1);
			attachList = qy.list();
			System.out.println("attachList===========>"+attachList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getChatAttachList======" + ex);
			log.logMessage("Exception getChatAttachList: "+ ex, "error", MessageDAOService.class);
		} finally {
			if (session != null) {session.flush();session.clear();session.close();session = null;
			}
		}
		return attachList;
	}

	@Override
	public JSONObject jsonGroupChatPack(List<GroupChatTblVO> sendGrpChatMsgObj, String profileimgPath,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb) {
		// TODO Auto-generated method stub
		JSONObject jsonPack = new JSONObject();
		Log log= new Log();
		FunctionUtility commonFn = new FunctionUtilityServices();
		try {
			if (sendGrpChatMsgObj != null) {
				GroupChatTblVO grpChatDataObj = new GroupChatTblVO();
				for (Iterator<GroupChatTblVO> it = sendGrpChatMsgObj.iterator();it.hasNext();) {
					grpChatDataObj = it.next();
					String msgIdPath = "";
					if (Commonutility.checkIntempty(grpChatDataObj.getChatId())) {
						jsonPack.put("msgid", grpChatDataObj.getChatId());
						msgIdPath = grpChatDataObj.getChatId() + "/";
					} else {
						jsonPack.put("msgid", "");
					}
					String imgMobilename = "";
					if (grpChatDataObj.getUserId() != null) {
						jsonPack.put("usrid", grpChatDataObj.getUserId().getUserId());
						imgMobilename = grpChatDataObj.getUserId().getImageNameMobile();
					} else {
						jsonPack.put("usrid", "");
					}
					if (Commonutility.checkempty(profileimgPath) && Commonutility.checkempty(imgMobilename)) {
						profileimgPath = profileimgPath + Commonutility.stringToStringempty(imgMobilename) ;
						jsonPack.put("usr_img",profileimgPath);					
					} else {
						jsonPack.put("usr_img","");
					}
					if (Commonutility.checkempty(grpChatDataObj.getMsgContent())) {
						jsonPack.put("content", grpChatDataObj.getMsgContent());
					} else {
						jsonPack.put("content", "");
					}
					JSONArray dataJsonArry = new JSONArray();
					String urlThumbnail = null;
					String urlTittle = null;
					String pageurl = null;
					if (Commonutility.checkempty(grpChatDataObj.getUrlThumbImage())) {
						urlThumbnail = Commonutility.stringToStringempty(grpChatDataObj.getUrlThumbImage());
					}else{
						urlThumbnail = "";
					}
					if (Commonutility.checkempty(grpChatDataObj.getUrlTitle())) {
						urlTittle = Commonutility.stringToStringempty(grpChatDataObj.getUrlTitle());
					}else{
						urlTittle = "";
					}
					if (Commonutility.checkempty(grpChatDataObj.getPageUrl())) {
						pageurl = Commonutility.stringToStringempty(grpChatDataObj.getPageUrl());
					}else{
						pageurl = "";
					}
					if (Commonutility.checkempty(urlThumbnail) && Commonutility.checkempty(urlTittle) && Commonutility.checkempty(pageurl)) {
						ArrayList<String> thumbList = new ArrayList<String>();
						JsonSimplepackDaoService jsonsimlObj = new JsonSimplepackDaoService();
						thumbList = jsonsimlObj.spTabSplitIntoArraylist(urlThumbnail);
						ArrayList<String> pageTittleList = new ArrayList<String>();
						pageTittleList = jsonsimlObj.spTabSplitIntoArraylist(urlTittle);
						ArrayList<String> pageurlList = new ArrayList<String>();
						pageurlList = jsonsimlObj.spTabSplitIntoArraylist(pageurl);					
						System.out.println("uu---"+thumbList.size());
						System.out.println("---io--:"+pageTittleList.size());
						System.out.println("--kl--:"+pageurlList.size());
						if (thumbList.size() == pageTittleList.size()) {
							if ( pageTittleList.size() == pageurlList.size()) {
								for (int i=0;i<thumbList.size();i++) {
									JSONObject dataJsonObj = new JSONObject();
									dataJsonObj.put("thumb_img", thumbList.get(i));
									dataJsonObj.put("title", pageTittleList.get(i));
									dataJsonObj.put("pageurl", pageurlList.get(i));
									dataJsonArry.put(dataJsonObj);
									dataJsonObj=null;
								}
							}						
						}
						if (dataJsonArry != null) {
							jsonPack.put("urls",dataJsonArry);
						} else {
							jsonPack.put("urls",dataJsonArry);
						}
					} else {
						dataJsonArry = new JSONArray();
						jsonPack.put("urls",dataJsonArry);
					}
					/*JSONArray dataJsonArry = new JSONArray();
					if (Commonutility.checkempty(grpChatDataObj.getUrlThumbImage()) && Commonutility.checkempty(grpChatDataObj.getUrlTitle()) 
							&& Commonutility.checkempty(grpChatDataObj.getPageUrl())) {
						JSONObject dataJsonObj = new JSONObject();
						dataJsonObj.put("thumb_img", grpChatDataObj.getUrlThumbImage());
						dataJsonObj.put("title", grpChatDataObj.getUrlTitle());
						dataJsonObj.put("pageurl", grpChatDataObj.getPageUrl());
						dataJsonArry.put(dataJsonObj);
						dataJsonObj=null;
						jsonPack.put("urls",dataJsonArry);
					} else {
						dataJsonArry = new JSONArray();
						jsonPack.put("urls",dataJsonArry);
					}*/
					if (Commonutility.checkIntnull(grpChatDataObj.getMsgType())) {
						jsonPack.put("msgtype", grpChatDataObj.getMsgType());
					} else {
						jsonPack.put("msgtype", "");
					}
					JSONArray jsArr = new JSONArray();
					JSONObject locJsonObj = new JSONObject();
					int varDonatoinAttach = grpChatDataObj.getChatId();
					
					JSONObject comVido = new JSONObject();
					JSONObject images = new JSONObject();
					JSONArray imagesArr = new JSONArray();
					JSONArray comVidoArr = new JSONArray();
					boolean fg = false;
					List<GrpChatAttachTblVO> grpChatAttachList = new ArrayList<GrpChatAttachTblVO>();
					grpChatAttachList = getGroupChatAttachList(varDonatoinAttach);
					System.out.println("===grpChatAttachList==="+grpChatAttachList.size());
					if (grpChatAttachList != null) {
						GrpChatAttachTblVO grpChatAttachObj = new GrpChatAttachTblVO();
						for (Iterator<GrpChatAttachTblVO> itObj = grpChatAttachList.iterator();itObj.hasNext();) {
							grpChatAttachObj = itObj.next();
							fg = true;
							images = new JSONObject();
							comVido = new JSONObject();
								if (grpChatAttachObj.getFileType() == 1) {
									if (grpChatAttachObj.getFileType()!=null) {
										images.put("img_id",String.valueOf(grpChatAttachObj.getUniqId()));
									} else {
										images.put("img_id","");
									} if(grpChatAttachObj.getAttachName()!=null) {
										images.put("img_url",imagePathMobi + msgIdPath + grpChatAttachObj.getAttachName());
									} else {
										images.put("img_url","");
									}
								}
								if (grpChatAttachObj.getFileType() == 2) {
										System.out.println("==thumb_img===="+ grpChatAttachObj.getThumpImage());
									if (grpChatAttachObj.getFileType()!=null) {
										comVido.put("video_id",String.valueOf(grpChatAttachObj.getUniqId()));
									} else {
										comVido.put("video_id","");
									} if (grpChatAttachObj.getThumpImage()!=null) {
										comVido.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(grpChatAttachObj.getThumpImage()));
									} else {
										comVido.put("thumb_img","");
									} if (grpChatAttachObj.getAttachName()!=null) {
										comVido.put("video_url", videoPath + msgIdPath +  Commonutility.stringToStringempty(grpChatAttachObj.getAttachName()));
									} else {
										comVido.put("video_url","");
									}
								}
									if (images!=null && images.length()>0) {
										imagesArr.put(images);
										jsonPack.put("images",imagesArr);
									} else {
										jsonPack.put("images",imagesArr); 
									}
									if (comVido!=null && comVido.length()>0) {
										comVidoArr.put(comVido);
										jsonPack.put("videos",comVidoArr);
									} else {
										jsonPack.put("videos",comVidoArr); 
									}
						}
						if (fg==false) {
							jsonPack.put("images",imagesArr);
							jsonPack.put("videos",comVidoArr);
						}
					}
					//jsonPack.put("images",locJsonObj);
					//jsonPack.put("videos",jsArr);
					if (grpChatDataObj.getEntryDatetime() != null) {
						String lastSeen = Commonutility.stringToStringempty(grpChatDataObj.getEntryDatetime().toString()) ;
						String shortTime = "";
						if (Commonutility.checkempty(lastSeen)) {
							shortTime = commonFn.getShortTime(lastSeen);
							lastSeen = commonFn.getLongTime(lastSeen);
						}					
						jsonPack.put("short_time",shortTime);
						jsonPack.put("full_time",lastSeen);
					} else {
						jsonPack.put("short_time","");
						jsonPack.put("full_time","");
					}
				}
			}
		} catch (Exception ex) {
			log.logMessage("Exception found in jsonGroupChatPack:" +ex, "error",MessageDAOService.class);
			jsonPack = null;
		}
		System.out.println("############## jsonPack ##########");
		System.out.println("jsonPack:" + jsonPack);
		return jsonPack;
	}

	public List<GrpChatAttachTblVO> getGroupChatAttachList(int varDonatoinAttach) {
		// TODO Auto-generated method stub
		Log log = new Log();
		List<GrpChatAttachTblVO> attachList = new ArrayList<GrpChatAttachTblVO>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = "from GrpChatAttachTblVO where chatId.chatId=:CHAT_ID and status=:STATUS";
			Query qy = session.createQuery(qry);
			qy.setInteger("CHAT_ID", varDonatoinAttach);
			qy.setInteger("STATUS", 1);
			attachList = qy.list();
			System.out.println("getGroupChatAttachList===========>"
					+ attachList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getGroupChatAttachList======" + ex);
			log.logMessage("Exception getGroupChatAttachList: " + ex, "error",
					MessageDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return attachList;
	}

	@Override
	public int deleteMemberList(String deletQry) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = new Log();
	    int result = -1;
	    try {	    	
	    	System.out.println("Enter-----------deleteMemberList---------.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	Query qury = session.createQuery(deletQry);	 
	    	qury.executeUpdate();
	    	tx.commit();
	    	System.out.println("deleteMemberList deelte success");
	    	result = 1;
	    } catch (Exception ex) {
	    	if (tx != null) {tx.rollback();}
	    	result = -1;
	    	System.out.println(" deleteGroup======" + ex);
			 log.logMessage("Exception found in  deleteGroup : "+ex, "error", MessageDAOService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null;
	    }
	    return result;
	}

	@Override
	public JSONObject getGroupNameDetails(List<Object[]> chatListObj) {
		// TODO Auto-generated method stub
		Log log= new Log();
		JSONObject jsonPack = new JSONObject();
		try {
			if (chatListObj != null) {
			Object[] objList;
			for(Iterator<Object[]> it=chatListObj.iterator();it.hasNext();) {
				objList = it.next();
				jsonPack = new JSONObject();
				
				if(objList[0]!=null){
					jsonPack.put("group_name",objList[0]);
				}else{
					jsonPack.put("group_name","");
				}
				if(objList[3]!=null){
					jsonPack.put("online_status",objList[3]);
				}else{
					jsonPack.put("online_status","");
				}
				if(objList[4]!=null){
					jsonPack.put("from_id",objList[4]);
				}else{
					jsonPack.put("from_id","");
				}
				if(objList[5]!=null){
					jsonPack.put("to_id",objList[5]);
				}else{
					jsonPack.put("to_id","");
				}
				if(objList[6]!=null){
					jsonPack.put("message",objList[6]);
				}else{
					jsonPack.put("message","");
				}
				if(objList[10]!=null){
					
					jsonPack.put("message_time",mobiCommon.getchatDateTime((java.util.Date)objList[10]));
				}else{
					jsonPack.put("message_time","");
				}
				
				if(objList[8]!=null){
					jsonPack.put("read_status",objList[8]);
				}else{
					jsonPack.put("read_status","");
				}
				
				if(objList[1]!=null && objList[11]!=null && objList[5]!=null && objList[11].toString().equals("3")){
					jsonPack.put("profile_picture", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/chat/mobile/"+objList[5]+"/"+objList[1]);
				}else if(objList[1]!=null && (objList[4]!=null || objList[5]!=null)){
					String useri="";
					if(objList[4]!=null){
						useri=objList[4].toString();
					}else if(objList[5]!=null){
						useri=objList[5].toString();
					}
					jsonPack.put("profile_picture", getText("SOCIAL_INDIA_BASE_URL")  +"/templogo/profile/mobile/"+useri+"/"+objList[1]);	
				}else if(objList[2]!=null){
					jsonPack.put("profile_picture", objList[2]);	
				}else{
					jsonPack.put("profile_picture","");
				}
				if(objList[11]!=null){
					jsonPack.put("chat_type",objList[11]);
				}else{
					jsonPack.put("chat_type","");
				}
				
				if(objList[13]!=null){
					jsonPack.put("group_contact_id",objList[13]);
				}else{
					jsonPack.put("group_contact_id","");
				}
				
				if(objList[14]!=null) {
					String userData = objList[14].toString();
					if (Commonutility.checkempty(userData)) {
						jsonPack.put("grp_user",userData);
					} else {
						jsonPack.put("grp_user","");
					}
				} else {
					jsonPack.put("grp_user","");
				}
				
				if(objList[15]!=null){
					jsonPack.put("creator_id",objList[15]);
				}else{
					jsonPack.put("creator_id","");
				}
				
			}
		} else {
			jsonPack = null;
		}
		} catch(Exception ex) {
			log.logMessage("Exception found in getGroupNameDetails :" + ex, "error", MessageDAOService.class);
			jsonPack = null;
		} finally {
		}
		return jsonPack;
	}

}
