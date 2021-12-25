package com.mobi.feedvo.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import com.mobi.carpooling.CarPoolDaoServices;
import com.mobi.common.AdditionalDataDao;
import com.mobi.common.AdditionalDataDaoServices;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.commonvo.PrivacyInfoTblVO;
import com.mobi.feedvo.FeedCommentTblVO;
import com.mobi.feedvo.FeedLikeUnlikeTBLVO;
import com.mobi.notification.NotificationDao;
import com.mobi.notification.NotificationDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.timelinefeedvo.FeedsViewTblVO;
import com.pack.utilitypkg.Commonutility;
import com.pack.utilitypkg.ImageCompress;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.common.CommonDaoService;
import com.social.common.CommonService;
import com.social.load.HibernateUtil;
import com.social.utils.Log;

public class FeedDAOService extends ActionSupport implements FeedDAO {
	Log log = new Log();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int commentPost(FeedCommentTblVO commentpostObj) {
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locCommentId = -1;
		try {
			log.logMessage("Enter into  CommentDAOService", "info",
					FeedDAOService.class);
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(commentpostObj);
			locCommentId = commentpostObj.getCommentId();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			locCommentId = -1;
			log.logMessage("Exception found in commentPost: " + ex, "error",
					FeedDAOService.class);
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
		return locCommentId;
	}

	@Override
	public boolean commentDelete(FeedCommentTblVO commentDelObj) {
		Session session = HibernateUtil.getSession();
		boolean result = false;
		Transaction tx = null;
		Log log = new Log();
		try {
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update FeedCommentTblVO set status=:STATUS, modifyDatetime=:MODY_DATETIME where commentId=:COMMENTID and feedId=:FEEDID and usrId=:USERID");
			qy.setInteger("COMMENTID", commentDelObj.getCommentId());
			qy.setInteger("FEEDID", commentDelObj.getFeedId());
			qy.setInteger("USERID", commentDelObj.getUsrId());
			qy.setInteger("STATUS", 2);
			qy.setTimestamp("MODY_DATETIME",
					Commonutility.enteyUpdateInsertDateTime());
			qy.executeUpdate();

			Query upd = session
					.createQuery("update FeedsTblVO set  modifyDatetime=:MODY_DATETIME,commentCount=commentCount-1 where feedId=:FEEDID");
			upd.setInteger("FEEDID", commentDelObj.getFeedId());
			upd.setTimestamp("MODY_DATETIME",
					Commonutility.enteyUpdateInsertDateTime());
			upd.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("Exception found in commentDelete : " + ex, "error",
					FeedDAOService.class);
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
		return result;
	}

	@Override
	public List<Object[]> commentList(FeedCommentTblVO commentListObj,
			String timestamp, int startLimit, int endLimit) {
		Log log = new Log();
		List<Object[]> commentObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into bookingList ", "info",
					FeedDAOService.class);
			String sqlQuery = "select cm.comment_id,cm.usr_id,cm.comment,cm.modify_datetime,usr.fname,usr.image_name_web,cm.entry_datetime,usr.SOCIAL_PICTURE "
					+ "from mvp_feed_comment cm "
					+ "left join usr_reg_tbl usr on cm.usr_id=usr.usr_id where cm.status = '1' and cm.feed_id='"
					+ commentListObj.getFeedId()
					+ "' and cm.entry_datetime <=str_to_date('"
					+ timestamp
					+ "','%Y-%m-%d %H:%i:%S') limit "
					+ startLimit
					+ ","
					+ endLimit;
			System.out.println("commentList sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("comment_id", Hibernate.INTEGER)
					.addScalar("usr_id", Hibernate.INTEGER)
					.addScalar("comment", Hibernate.TEXT)
					.addScalar("modify_datetime", Hibernate.TIMESTAMP)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_web", Hibernate.TEXT)
					.addScalar("entry_datetime", Hibernate.TIMESTAMP)
					.addScalar("SOCIAL_PICTURE", Hibernate.TEXT);
			commentObj = queryObj.list();
			log.logMessage(
					"Enter into commentList size had:" + commentObj.size(),
					"info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in commentList :" + ex, "error",
					FeedDAOService.class);
			commentObj = null;
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
		return commentObj;
	}
	
	@Override
	public List<Object[]> latestCommentList(FeedCommentTblVO commentListObj,
			String timestamp, int startLimit, int endLimit) {
		Log log = new Log();
		List<Object[]> commentObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into bookingList ", "info",
					FeedDAOService.class);
			String sqlQuery = "select cm.comment_id,cm.usr_id,cm.comment,cm.modify_datetime,usr.fname,usr.image_name_web,cm.entry_datetime,usr.SOCIAL_PICTURE "
					+ "from mvp_feed_comment cm "
					+ "left join usr_reg_tbl usr on cm.usr_id=usr.usr_id where cm.status = '1' and cm.feed_id='"
					+ commentListObj.getFeedId()
					+ "' order by cm.comment_id desc limit "
					+ startLimit
					+ ","
					+ endLimit;
			System.out.println("commentList sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("comment_id", Hibernate.INTEGER)
					.addScalar("usr_id", Hibernate.INTEGER)
					.addScalar("comment", Hibernate.TEXT)
					.addScalar("modify_datetime", Hibernate.TIMESTAMP)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_web", Hibernate.TEXT)
					.addScalar("entry_datetime", Hibernate.TIMESTAMP)
					.addScalar("SOCIAL_PICTURE", Hibernate.TEXT);
			commentObj = queryObj.list();
			log.logMessage(
					"Enter into commentList size had:" + commentObj.size(),
					"info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in commentList :" + ex, "error",
					FeedDAOService.class);
			commentObj = null;
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
		return commentObj;
	}

	@Override
	public int getTotalCountSqlQuery(String sqlQuery) {
		int returnVal = 0;
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getTotalCountSqlQuery sqlQuery :"
					+ sqlQuery, "info", FeedDAOService.class);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery);
			returnVal = ((Number) queryObj.uniqueResult()).intValue();
			log.logMessage("Enter into getTotalCountSqlQuery total count :"
					+ returnVal, "info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in getTotalCountSqlQuery :" + ex,
					"error", FeedDAOService.class);
			returnVal = 0;
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
		return returnVal;
	}

	@Override
	public int commentLikeUnlike(FeedLikeUnlikeTBLVO likeUnlikeObj) {
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locLikeId = -1;
		try {
			log.logMessage("Enter into  CommentDAOService", "info",
					FeedDAOService.class);
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(likeUnlikeObj);
			locLikeId = likeUnlikeObj.getLikeId();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			locLikeId = -1;
			log.logMessage("Exception found in commentPost: " + ex, "error",
					FeedDAOService.class);
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
		return locLikeId;
	}

	@Override
	public boolean likeUnlikeCountUpdate(int feedId, int likeStatus) {
		boolean result = false;
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		try {
			tx = session.beginTransaction();
			String query = "";
			if (likeStatus == 1) {
				query = "update  mvp_timeline_feed set like_count=like_count+1  where feed_id='"
						+ feedId + "'";
			} else if (likeStatus == 2) {
				query = "update  mvp_timeline_feed set like_count=like_count-1  where feed_id='"
						+ feedId + "' and like_count <> 0";
			}
			Query qy = session.createSQLQuery(query);
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("Exception found in likeUnlikeCountUpdate : " + ex,
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
		return result;
	}

	@Override
	public List<Object[]> likeCountList(int feedId) {
		Log log = new Log();
		List<Object[]> likeListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into likeCountList ", "info",
					FeedDAOService.class);
			String sqlQuery = "select tfd.like_count,group_concat(usr.USERNAME) from usr_reg_tbl usr "
					+ "inner join mvp_feed_likes fd on fd.USR_ID=usr.usr_id "
					+ "left join mvp_timeline_feed tfd on tfd.feed_id=fd.feed_id "
					+ "where fd.feed_id='"
					+ feedId
					+ "' and like_count <> 0 order by tfd.usr_id desc limit 10";
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("like_count", Hibernate.LONG)
					.addScalar("group_concat(usr.USERNAME)", Hibernate.TEXT);
			likeListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage(
					"Enter into likeCountList size had:" + likeListObj.size(),
					"info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in likeCountList :" + ex, "error",
					FeedDAOService.class);
			likeListObj = null;
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
		return likeListObj;
	}

	@Override
	public boolean feedDelete(FeedsTblVO feedObj) {
		Session session = null;
		Transaction tx = null;
		Log log = new Log();
		Query qy = null;
		boolean result = false;
		try {
			System.out.println("feed Delete [Start].");
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			qy = session
					.createQuery("update FeedsTblVO set feedStatus=:STATUS,modifyDatetime=:MODYDATETIME where  feedId=:FEEDID and usrId=:USERID ");
			qy.setInteger("STATUS", 0);
			qy.setInteger("FEEDID", feedObj.getFeedId());
			qy.setInteger("USERID", feedObj.getUsrId().getUserId());
			qy.setTimestamp("MODYDATETIME",
					Commonutility.enteyUpdateInsertDateTime());
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = false;
			System.out.println(" updateOtpUser======" + ex);
			log.logMessage("feedDelete Exception found in  : " + ex, "error",
					FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			log = null;
			qy = null;
		}
		System.out.println("Step 3 :user block [End].");
		return result;
	}
	
	@Override
	public boolean feedDelete(String feed_id, String usr_id) {
		Session session = null;
		Transaction tx = null;
		Log log = new Log();
		Query qy = null;
		boolean result = false;
		try {
			System.out.println("feed Delete [Start].");
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			qy = session
					.createQuery("update FeedsTblVO set feedStatus=:STATUS,modifyDatetime=:MODYDATETIME where  feedId=:FEEDID and usrId=:USERID ");
			qy.setInteger("STATUS", 0);
			qy.setInteger("FEEDID", Commonutility.stringToInteger(feed_id));
			qy.setInteger("USERID", Commonutility.stringToInteger(usr_id));
			qy.setTimestamp("MODYDATETIME",
					Commonutility.enteyUpdateInsertDateTime());
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = false;
			System.out.println(" updateOtpUser======" + ex);
			log.logMessage("feedDelete Exception found in  : " + ex, "error",
					FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			log = null;
			qy = null;
		}
		System.out.println("Step 3 :user block [End].");
		return result;
	}

	@Override
	public List<Object[]> feedList(String whereQuery, int rid, int startlimit) {
		Log log = new Log();
		List<Object[]> feedListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into bookingList ", "info",
					FeedDAOService.class);
			String sqlQuery = "select  feed_id,feed_typ,attname,urls_thumb_img,urls_title,urls_pageurl,feed_category,feed_title,feed_stitle, feed_desc, "
					+ "feed_time,amount,post_as,feed_privacy_flg,entry_by,fname,image_name_mobile,feed_location,feed_msg, is_myfeed,is_shared, "
					+ "originator_name,originator_id,like_unlike_flg,like_count,share_count,comment_count, feed_typ_id,share_entryby,feed_view_id "
					+ "from  (select  feed.entry_datetime feed_entry_datetime, case when timeview.usr_id=-2 and "
					+ "(select count(*) from mvp_network_tbl nwt where (nwt.PARENT_USR_ID=timeview.entry_by and  nwt.CONNECTED_USR_ID='"
					+ rid
					+ "') or "
					+ "( nwt.PARENT_USR_ID='"
					+ rid
					+ "' and  nwt.CONNECTED_USR_ID=timeview.entry_by))>=1 then 1 when timeview.usr_id!=-2 then 1 else 0 end as flg, feed.feed_id feed_id, feed.feed_typ feed_typ, "
					+ "group_concat(concat(ifnull(feedatt.uniq_id,''),'<!_!>',ifnull(feedatt.attach_name,''),'<!_!>',ifnull(feedatt.thump_image,''),'<!_!>',ifnull(feedatt.file_type,'')) separator '<SP>') attname, "
					+ "feed.urls_thumb_img urls_thumb_img,  feed.urls_title urls_title, feed.urls_pageurl urls_pageurl,  feed.feed_category feed_category, feed.feed_title feed_title,  feed.feed_stitle feed_stitle,  feed.feed_desc feed_desc, feed.feed_time feed_time,  feed.amount amount,  feed.post_as post_as, feed.feed_privacy_flg feed_privacy_flg,  feed.entry_by entry_by,  usr.fname fname, usr.image_name_mobile image_name_mobile, feed.feed_location feed_location,  feed.feed_msg feed_msg,  feed.is_myfeed is_myfeed,  timeview.is_shared is_shared,  feed.originator_name originator_name, feed.originator_id originator_id,  feedlks.like_unlike_flg like_unlike_flg,  feed.like_count like_count,  feed.share_count share_count,  feed.comment_count comment_count,  feed.feed_typ_id feed_typ_id, timeview.entry_by  share_entryby, timeview.uniq_id feed_view_id "
					+ "from mvp_timeline_feed feed inner join mvp_timeline_view_tbl timeview on feed.feed_id = timeview.feed_id and feed.feed_status=1 and feed.society_id=timeview.society_id left  join society_mst_tbl scty on scty.society_id=feed.society_id left join mvp_feed_attch_tbl feedatt on feed.feed_id =feedatt.feed_id left join  mvp_feed_likes feedlks on feed.feed_id = feedlks.feed_id left join  usr_reg_tbl usr  on feed.usr_id = usr.usr_id  "
					+ "where  ("
					+ whereQuery
					+ " "
					+ "group by feed.feed_id ) tt where flg=1 order by feed_entry_datetime desc limit "
					+ startlimit + ",10";
			System.out.println("Feed List New sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("feed_id", Hibernate.TEXT)
					.addScalar("feed_typ", Hibernate.TEXT)
					.addScalar("attname", Hibernate.TEXT)
					.addScalar("urls_thumb_img", Hibernate.TEXT)
					.addScalar("urls_title", Hibernate.TEXT)
					.addScalar("urls_pageurl", Hibernate.TEXT)
					.addScalar("feed_category", Hibernate.TEXT)
					.addScalar("feed_title", Hibernate.TEXT)
					.addScalar("feed_stitle", Hibernate.TEXT)
					.addScalar("feed_desc", Hibernate.TEXT)
					.addScalar("feed_time", Hibernate.TIMESTAMP)
					.addScalar("amount", Hibernate.FLOAT)
					.addScalar("post_as", Hibernate.TEXT)
					.addScalar("feed_privacy_flg", Hibernate.INTEGER)
					.addScalar("entry_by", Hibernate.INTEGER)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_mobile", Hibernate.TEXT)
					.addScalar("feed_location", Hibernate.TEXT)
					.addScalar("feed_msg", Hibernate.TEXT)
					.addScalar("is_myfeed", Hibernate.INTEGER)
					.addScalar("is_shared", Hibernate.INTEGER)
					.addScalar("originator_name", Hibernate.TEXT)
					.addScalar("originator_id", Hibernate.INTEGER)
					.addScalar("like_unlike_flg", Hibernate.INTEGER)
					.addScalar("like_count", Hibernate.LONG)
					.addScalar("share_count", Hibernate.LONG)
					.addScalar("comment_count", Hibernate.LONG)
					.addScalar("feed_typ_id", Hibernate.INTEGER)
					.addScalar("share_entryby", Hibernate.INTEGER)
					.addScalar("feed_view_id", Hibernate.INTEGER);
			feedListObj = queryObj.list();

			log.logMessage(
					"Enter into feedList size had:" + feedListObj.size(),
					"info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedList :" + ex, "error",
					FeedDAOService.class);
			feedListObj = null;
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
		return feedListObj;
	}

	@Override
	public int feedInsert(String users, FeedsTblVO feedInsertObj,
			List<File> fileUpload, List<String> fileUploadContentType,
			List<String> fileUploadFileName) {
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locTimelineFeedid = -1;
		HashMap<String, File> imageFileMap = new HashMap<String, File>();
		HashMap<String, File> videoFileMap = new HashMap<String, File>();
		FunctionUtility commonutil = new FunctionUtilityServices();
		CommonMobiDao commonServ = new CommonMobiDaoService();
		mobiCommon mobComm = new mobiCommon();
		try {
			log.logMessage("Ente into feedd insert", "info",
					FeedDAOService.class);
			boolean commitFlg = true;
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			log.logMessage("Ente into feedd privacy flg", "info",
					FeedDAOService.class);
			if (feedInsertObj.getUsrId() != null
					|| feedInsertObj.getUsrId().getUserId() != 0) {
				PrivacyInfoTblVO privacyObj = new PrivacyInfoTblVO();
				privacyObj = commonServ.getDefaultPrivacyFlg(feedInsertObj
						.getUsrId().getUserId());
				if (privacyObj != null) {
					feedInsertObj.setFeedPrivacyFlg(privacyObj.getPrivacyFlg());
					if (privacyObj.getPrivacyFlg() == 4) {
						if (Commonutility.checkempty(users)) {
						} else {
							users = privacyObj.getUsrIds();
						}
					}
				} else {
					feedInsertObj
							.setFeedPrivacyFlg(Commonutility
									.stringToInteger(getText("default.privacy.category")));
					log.logMessage("no default privacy flg", "info",
							FeedDAOService.class);
				}

			}
			if (feedInsertObj != null) {
				long dafltVal = 0;
				feedInsertObj.setLikeCount(dafltVal);
				feedInsertObj.setShareCount(dafltVal);
				feedInsertObj.setCommentCount(dafltVal);
			}
			feedInsertObj
					.setFeedTime(Commonutility.enteyUpdateInsertDateTime());
			System.out.println("timeeeeeeeeeeeee:"
					+ feedInsertObj.getFeedTime());
			session.save(feedInsertObj);
			locTimelineFeedid = feedInsertObj.getFeedId();
			System.out.println("locTimelineFeedid :::::::" + locTimelineFeedid);
			if (locTimelineFeedid != -1) {
				if (fileUpload != null && fileUpload.size() > 0) {
					System.out.println("=========fileUpload========="
							+ fileUpload.size());
					for (int i = 0; i < fileUpload.size(); i++) {
						File mapFileUpload = fileUpload.get(i);
						System.out.println("==========ss===" + mapFileUpload);
						String mapFileName = fileUploadFileName.get(i);
						System.out.println("==========mapFileName==="
								+ mapFileName);
						String attachFileName = FilenameUtils
								.getBaseName(mapFileName);
						String attachFileExtension = FilenameUtils
								.getExtension(mapFileName);
						String uniqFileName = attachFileName + "_"
								+ commonutil.uniqueNoFromDate() + "."
								+ attachFileExtension;
						System.out.println("======uniqFileName====="
								+ uniqFileName);
						System.out.println("======attachFileExtension====="
								+ attachFileExtension);
						Integer fileType = mobComm
								.getFileExtensionType(attachFileExtension);
						if (fileType == 2) {
							videoFileMap.put(uniqFileName, mapFileUpload);
						} else {
							imageFileMap.put(uniqFileName, mapFileUpload);
						}
					}
					FeedsTblVO feedIdObj = new FeedsTblVO();
					feedIdObj.setFeedId(locTimelineFeedid);
					if (videoFileMap.size() > 0) {
						log.logMessage("Enter into video write feed post",
								"info", FeedDAOService.class);
						String videoPath = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.videopath")
								+ locTimelineFeedid + "/";
						String videoPathThumb = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.video.thumbpath")
								+ locTimelineFeedid + "/";
						log.logMessage(videoPath + " :path: " + videoPathThumb,
								"info", FeedDAOService.class);
						for (Entry<String, File> entry : videoFileMap
								.entrySet()) {
							log.logMessage("Ente  feed attach video filename:"
									+ entry.getKey(), "info",
									FeedDAOService.class);
							log.logMessage("Ente feed attach video filen:"
									+ entry.getValue(), "info",
									FeedDAOService.class);
							String videoName = entry.getKey();
							String videoThumbName = FilenameUtils
									.getBaseName(videoName) + ".jpeg";
							System.out.println("videoThumbName:"
									+ videoThumbName);
							try {
								System.out
										.println("service video file start time-------------"
												+ new Date());
								File destFile = new File(videoPath, videoName);
								FileUtils.copyFile(entry.getValue(), destFile);
								System.out
										.println("service video file start time 2222222222-------------"
												+ new Date());
								if (imageFileMap.containsKey(videoThumbName)) {

									File imageThumb = imageFileMap
											.get(videoThumbName);
									File destThumbFile = new File(
											videoPathThumb, videoThumbName);

									FileUtils.copyFile(imageThumb,
											destThumbFile);
									System.out
											.println("service video file end time 1111-------------"
													+ new Date());
									imageFileMap.remove(videoThumbName);
									System.out
											.println("service video file end time-------------"
													+ new Date());
								} else {
									log.logMessage(
											"######### Thump image not found #########",
											"info", FeedDAOService.class);
								}
							} catch (Exception ex) {
								commitFlg = false;
								log.logMessage(
										"Exception ffound in video file upload : "
												+ ex, "error",
										FeedDAOService.class);
							}
							FeedattchTblVO feedAttchObj = new FeedattchTblVO();
							feedAttchObj.setIvrBnFEED_ID(feedIdObj);
							feedAttchObj.setIvrBnATTACH_NAME(videoName);
							feedAttchObj.setIvrBnTHUMP_IMAGE(videoThumbName);
							feedAttchObj.setIvrBnFILE_TYPE(2);
							feedAttchObj.setIvrBnSTATUS(1);
							feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
									.enteyUpdateInsertDateTime());
							feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
									.enteyUpdateInsertDateTime());
							session.save(feedAttchObj);
						}
						log.logMessage("End video write feed post", "info",
								FeedDAOService.class);
					}
					if (imageFileMap.size() > 0) {
						log.logMessage("Enter into image write feed post",
								"info", FeedDAOService.class);
						String imagePathWeb = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.img.webpath")
								+ locTimelineFeedid + "/";
						String imagePathMobi = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.img.mobilepath")
								+ locTimelineFeedid + "/";
						for (Entry<String, File> entry : imageFileMap
								.entrySet()) {
							log.logMessage("Ente  feed attach image filename:"
									+ entry.getKey(), "info",
									FeedDAOService.class);
							log.logMessage("Ente feed attach image filen:"
									+ entry.getValue(), "info",
									FeedDAOService.class);
							String imageName = entry.getKey();
							String imgBaseName = FilenameUtils
									.getBaseName(imageName);
							String imgExtenName = FilenameUtils
									.getExtension(imgBaseName);
							File imgFilePath = imageFileMap.get(imageName);
							Integer fileType = mobComm
									.getFileExtensionType(imgExtenName);
							FeedattchTblVO feedAttchObjImg = new FeedattchTblVO();
							feedAttchObjImg.setIvrBnFEED_ID(feedIdObj);
							feedAttchObjImg.setIvrBnSTATUS(1);
							feedAttchObjImg.setIvrBnATTACH_NAME(imageName);
							feedAttchObjImg
									.setIvrBnENTRY_DATETIME(Commonutility
											.enteyUpdateInsertDateTime());
							feedAttchObjImg
									.setIvrBnMODIFY_DATETIME(Commonutility
											.enteyUpdateInsertDateTime());
							if (fileType == 1) {
								feedAttchObjImg.setIvrBnFILE_TYPE(1);
								log.logMessage("imageName web :" + imageName,
										"info", FeedDAOService.class);
								// for web, image upload original size
								try {
									File destFileWeb = new File(imagePathWeb,
											imageName);
									File destFileMobi = new File(imagePathMobi, imageName);
									FileUtils.copyFile(entry.getValue(),
											destFileWeb);
									FileUtils.copyFile( entry.getValue(),
											destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage(
											"Exception found in image file upload for web : "
													+ ex, "error",
											FeedDAOService.class);
								}
								// for mobile, compress image upload
								int imgWitdh = 0;
								int imgHeight = 0;
								int imgOriginalWidth = mobiCommon
										.getImageWidth(imgFilePath);
								int imgOriginalHeight = mobiCommon
										.getImageHeight(imgFilePath);
								String imageHeightPro = getText("thump.img.height");
								String imageWidthPro = getText("thump.img.width");
								if (imgOriginalWidth > Commonutility
										.stringToInteger(imageWidthPro)) {
									imgWitdh = Commonutility
											.stringToInteger(imageWidthPro);
								} else {
									imgWitdh = imgOriginalWidth;
								}
								if (imgOriginalHeight > Commonutility
										.stringToInteger(imageHeightPro)) {
									imgHeight = Commonutility
											.stringToInteger(imageHeightPro);
								} else {
									imgHeight = imgOriginalHeight;
								}
								String imgSourcePath = imagePathWeb + imageName;
								String imgName = FilenameUtils
										.getBaseName(imageName);
								String imageFomat = FilenameUtils
										.getExtension(imageName);
								String imageFor = "all";
								log.logMessage("imageName for web compress : "
										+ imageName, "info",
										FeedDAOService.class);
								ImageCompress.toCompressImage(imgSourcePath,
										imagePathMobi, imgName, imageFomat,
										imageFor, imgWitdh, imgHeight);
							} else {
								feedAttchObjImg.setIvrBnFILE_TYPE(3);
								try {
									File destFileWeb = new File(imagePathWeb,
											imageName);
									File destFileMobi = new File(imagePathMobi,
											imageName);
									FileUtils.copyFile(entry.getValue(),
											destFileWeb);
									FileUtils.copyFile(entry.getValue(),
											destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage(
											"Exception found in image file upload for type 3 : "
													+ ex, "error",
											FeedDAOService.class);
								}
							}
							session.save(feedAttchObjImg);
						}
						log.logMessage("End image write feed post", "info",
								FeedDAOService.class);
					}
				} else {
					log.logMessage("File upload empty", "info",
							FeedDAOService.class);
				}
				int feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
				int rid = feedInsertObj.getUsrId().getUserId();
				FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
				int sociteyId = feedInsertObj.getSocietyId().getSocietyId();
				int isShareflg = 0;
				if (Commonutility.checkempty(users) && feedPrivacy == 4) {
					log.logMessage("Feed view insert start privacy flg == 3",
							"info", FeedDAOService.class);
					System.out.println("locTimelineFeedid-----------------"
							+ locTimelineFeedid);
					feedViewObj = feedInsertViewDataPack(rid,
							locTimelineFeedid, rid, sociteyId, isShareflg, -1,
							0);
					if (feedViewObj != null) {
						session.save(feedViewObj);
						log.logMessage(
								"Feed post own view id="
										+ feedViewObj.getUniqId(), "info",
								FeedDAOService.class);
					} else {
						commitFlg = false;
					}
					if (commitFlg) {
						int friendUserId = 0;
						if (!users.contains(",")) {
							users += ",";
						}
						String[] userIdArr = users.split(",");
						log.logMessage("Total number of friends id="
								+ userIdArr.length, "info",
								FeedDAOService.class);
						for (int i = 0; i < userIdArr.length; i++) {
							log.logMessage(i
									+ "<==Size ::single friends list viewId:"
									+ userIdArr[i], "info",
									FeedDAOService.class);
							if (Commonutility.checkempty(userIdArr[i])
									&& Commonutility
											.toCheckisNumeric(userIdArr[i])) {
								friendUserId = Commonutility
										.stringToInteger(userIdArr[i]);
								feedViewObj = feedInsertViewDataPack(
										friendUserId, locTimelineFeedid, rid,
										sociteyId, isShareflg, -1, 0);
								if (feedViewObj != null) {
									session.save(feedViewObj);
									System.out.println("0------");
									if (!tx.wasCommitted()) {
										tx.commit();
									}
									// tx.commit();
									System.out.println("--------0");

									NotificationDao notificationHbm = new NotificationDaoServices();
									CommonMobiDao commonHbm = new CommonMobiDaoService();
									UserMasterTblVo userMasteriterObj;
									userMasteriterObj = commonHbm
											.getProfileDetails(friendUserId);
									AdditionalDataDao additionalDatafunc = new AdditionalDataDaoServices();
									System.out
											.println("feedViewObj.getFeedId()------------------------"
													+ feedViewObj.getFeedId()
															.getFeedId());
									FeedDAO feedhbm = new FeedDAOService();
									FeedsTblVO feedobj = new FeedsTblVO();
									feedobj = feedhbm.feedData(feedViewObj
											.getFeedId().getFeedId());
									System.out
											.println("feedViewObj.getFeedType()------------------------"
													+ feedViewObj.getFeedId()
															.getFeedType());
									String additionaldata = additionalDatafunc
											.formAdditionalDataForFeedTbl(feedobj);
									System.out
											.println("formAdditionalDataForFeedTbl------------------"
													+ additionaldata);
									notificationHbm
											.insertnewNotificationDetails(
													userMasteriterObj, "", 8,
													1, feedobj.getFeedId(),
													feedInsertObj.getUsrId(),
													additionaldata);

									log.logMessage("Feed post friend view id="
											+ feedViewObj.getUniqId(), "info",
											FeedDAOService.class);
								} else {
									commitFlg = false;
									break;
								}
							}
						}
					}
					log.logMessage("End ::: Feed insert friendsTBL viewId::",
							"info", FeedDAOService.class);
				} else {
					log.logMessage("Feed view insert start privacy flg == 1,3",
							"info", FeedDAOService.class);
					if (Commonutility.checkIntempty(feedPrivacy)) {
						log.logMessage("End Self insert for privacy flg : "
								+ feedPrivacy, "info", FeedDAOService.class);
						if (feedPrivacy == 1 || feedPrivacy == 4) {
							feedViewObj = feedInsertViewDataPack(rid,
									locTimelineFeedid, rid, sociteyId,
									isShareflg, -1, 0);
						} else {
							int feedView = -2;
							if (feedPrivacy == 2) {
								feedView = -1;
							} else if (feedPrivacy == 3) {
								feedView = -2;
							}
							feedViewObj = feedInsertViewDataPack(feedView,
									locTimelineFeedid, rid, sociteyId,
									isShareflg, -1, 0);
						}
						if (feedViewObj != null) {
							session.save(feedViewObj);
							log.logMessage("Feed post Self insert view id="
									+ feedViewObj.getUniqId(), "info",
									FeedDAOService.class);
							System.out.println("0------");
							tx.commit();
							System.out.println("--------0");
						} else {
							commitFlg = false;
						}
						log.logMessage("End Self insert for privacy flg : "
								+ feedPrivacy, "info", FeedDAOService.class);

					}
				}
			} else {
				commitFlg = false;
			}
			log.logMessage("commitFlg value:" + commitFlg, "info",
					FeedDAOService.class);
			if (commitFlg) {

			} else {
				if (tx != null) {
					tx.rollback();
				}
				if (locTimelineFeedid != -1) {
					feedFileUploadDelete(locTimelineFeedid);
				}
				locTimelineFeedid = -1;
			}

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (locTimelineFeedid != -1) {
				feedFileUploadDelete(locTimelineFeedid);
			}
			log = new Log();
			log.logMessage("Exception found in feed insert : " + e, "error",
					FeedDAOService.class);
			locTimelineFeedid = -1;
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
		return locTimelineFeedid;
	}

	public boolean feedFileUploadDelete(int feedId) {
		boolean retValue = true;
		Log log = new Log();
		try {
			log.logMessage(
					"Enter into FeedFileUpload insert folder rollback Delete : ",
					"info", FeedDAOService.class);
			String imagePathWeb = getText("external.imagesfldr.path")
					+ getText("external.uploadfile.feed.img.webpath") + feedId;
			String imagePathMobi = getText("external.imagesfldr.path")
					+ getText("external.uploadfile.feed.img.mobilepath")
					+ feedId;
			String videoPath = getText("external.imagesfldr.path")
					+ getText("external.uploadfile.feed.videopath") + feedId;
			String videoPathThumb = getText("external.imagesfldr.path")
					+ getText("external.uploadfile.feed.video.thumbpath")
					+ feedId;

			File fileWeb = new File(imagePathWeb);
			File fileMobi = new File(imagePathMobi);
			File fileVideo = new File(videoPath);
			File fileVideoThumb = new File(videoPathThumb);
			if (fileWeb.isDirectory()) {
				FileUtils.deleteDirectory(fileWeb);
			}
			if (fileMobi.isDirectory()) {
				FileUtils.deleteDirectory(fileMobi);
			}
			if (fileVideo.isDirectory()) {
				FileUtils.deleteDirectory(fileVideo);
			}
			if (fileVideoThumb.isDirectory()) {
				FileUtils.deleteDirectory(fileVideoThumb);
			}
			log.logMessage(
					"FeedFileUpload insert folder rollback Delete success: ",
					"info", FeedDAOService.class);
		} catch (Exception ex) {
			retValue = false;
			log.logMessage(
					"Exception found in feedFileUpload insert folder rollback Delete : "
							+ ex, "error", FeedDAOService.class);
		}
		return retValue;
	}

	public FeedsViewTblVO feedInsertViewDataPack(int userId, int feedId,
			int entryBy, int societyId, int isShareflg, int feedPrivacy,
			int parentViewId) {
		/**
		 * feedPrivacy its only in shared feed only -1-->original_feed,1-only me
		 * ,2-public ,3-n/w friends,4-custom
		 */
		Log log = new Log();
		FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
		try {
			log.logMessage("Enter into feed view data pack ", "info",
					FeedDAOService.class);
			FeedsTblVO feedObjV = new FeedsTblVO();
			// UserMasterTblVo userMsrObjV = new UserMasterTblVo();
			SocietyMstTbl socityObj = new SocietyMstTbl();
			// if(userId!=0){
			// userMsrObjV.setUserId(Commonutility.insertchkintnull(userId));
			// feedViewObj.setUsrId(Commonutility.insertchkintnull(userId));
			// }
			FeedDAO feedHbm = new FeedDAOService();
			System.out.println("feedId-----------------" + feedId);
			// feedObjV=feedHbm.feedData(feedId);
			// if(feedObjV==null){
			// feedObjV = new FeedsTblVO();
			String countFeed = "select count(*) from FeedsTblVO where feedId="
					+ feedId;
			CommonService commonHbm = new CommonDaoService();
			int countfd = commonHbm.gettotalcount(countFeed);
			System.out.println("countfd--------------------" + countfd);
			if (countfd > 0) {
				feedObjV = feedData(feedId);
			} else {
				feedObjV.setFeedId(feedId);
			}
			System.out.println("feedObjV = new FeedsTblVO();-----------------");
			// }
			feedViewObj.setUsrId(userId);
			feedViewObj.setFeedId(feedObjV);
			feedViewObj.setEntryBy(entryBy);
			socityObj.setSocietyId(societyId);// parentViewId
			feedViewObj.setSocietyId(socityObj);
			if (parentViewId != 0) {
				feedViewObj.setParentViewId(parentViewId);
			} else {
				feedViewObj.setParentViewId(0);
			}
			if (isShareflg != 0) {
				feedViewObj.setSharedPrivacyFlg(feedPrivacy);
			} else {
				feedViewObj.setSharedPrivacyFlg(-1);
			}
			feedViewObj.setIsShared(isShareflg);
			feedViewObj.setStatus(1);
			feedViewObj.setEntryDatetime(Commonutility
					.enteyUpdateInsertDateTime());
			// System.out.println("feedViewObj---------------"+feedViewObj.getFeedId().getFeedId());
		} catch (Exception e) {
			log.logMessage("Exception feedInsertViewdatapack: " + e, "error",
					FeedDAOService.class);
			feedViewObj = null;
		} finally {
			log = null;
		}
		return feedViewObj;
	}

	@Override
	public boolean feedEdit(FeedsTblVO feedInsertObj, String users,
			JSONArray removeAttachIdsObj, List<File> fileUpload,
			List<String> fileUploadContentType, List<String> fileUploadFileName) {
		Session session = null;
		Transaction tx = null;
		Log log = new Log();
		Query qy = null;
		boolean result = false;
		boolean editCommitFlg = true;
		HashMap<String, File> imageFileMap = new HashMap<String, File>();
		HashMap<String, File> videoFileMap = new HashMap<String, File>();
		FunctionUtility commonutil = new FunctionUtilityServices();
		mobiCommon mobComm = new mobiCommon();
		try {
			System.out.println("=====feedInsertObj==" + feedInsertObj);
			System.out.println("feed edit start");
			System.out.println("feedInsertObj userid -----------"
					+ feedInsertObj.getUsrId().getUserId());
			System.out.println("feedInsertObj socityid -----------"
					+ feedInsertObj.getSocietyId().getSocietyId());
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String updateQuery = "";
			int feedPrivacy = 0;
			FeedsTblVO getFeedDataObj = new FeedsTblVO();
			System.out
					.println("feedInsertObj.getFeedId() feedid data==============="
							+ feedInsertObj.getFeedId());
			getFeedDataObj = feedData(feedInsertObj.getFeedId());
			System.out
					.println("getFeedDataObj.getFeedType() getFeedType==============="
							+ getFeedDataObj.getFeedType());
			if (getFeedDataObj.getFeedType() == 2) {
				System.out
						.println("Enter if part for privacy flag===============");
				feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
			} else {
				System.out
						.println("Enter else part for privacy flag===============");
				PrivacyInfoTblVO privacyObj = new PrivacyInfoTblVO();
				CommonMobiDao commonServNew = new CommonMobiDaoService();
				privacyObj = commonServNew.getDefaultPrivacyFlg(feedInsertObj
						.getUsrId().getUserId());
				if (privacyObj != null) {
					feedInsertObj.setFeedPrivacyFlg(privacyObj.getPrivacyFlg());
					feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
					if (privacyObj.getPrivacyFlg() == 4) {
						if (Commonutility.checkempty(users)) {
						} else {
							users = privacyObj.getUsrIds();
						}
					}
				} else {
					feedInsertObj
							.setFeedPrivacyFlg(Commonutility
									.stringToInteger(getText("default.privacy.category")));
					feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
					log.logMessage("no default privacy flg", "info",
							FeedDAOService.class);
				}

			}
			int feedId = feedInsertObj.getFeedId();
			System.out.println("=====feedId==" + feedId);
			System.out.println("=====feedPrivacy==" + feedPrivacy);
			if (Commonutility.checkempty(feedInsertObj.getFeedMsg())) {
				updateQuery += "feedMsg=:FEEDMSG,";
			}
			if (feedInsertObj != null
					&& feedInsertObj.getFeedPrivacyFlg() != null
					&& feedInsertObj.getFeedPrivacyFlg() != 0) {
				updateQuery += "feedPrivacyFlg=:PRIVACYFLG,";
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsThumbImg())) {
				updateQuery += "urlsThumbImg=:URLTHUMB,";
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsTitle())) {
				updateQuery += "urlsTitle=:URLTITLE,";
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsPageurl())) {
				updateQuery += "urlsPageurl=:PAGEURL,";
			}

			if (Commonutility.checkempty(feedInsertObj.getFeedDesc())) {
				updateQuery += "feedDesc=:FEEDDESC,";
			}

			if (Commonutility.checkempty(feedInsertObj.getFeedStitle())) {
				updateQuery += "feedStitle=:FEEDSTITLE,";
			}

			if (Commonutility.checkempty(feedInsertObj.getFeedTitle())) {
				updateQuery += "feedTitle=:FEEDTITLE,";
			}

			if (feedInsertObj != null && feedInsertObj.getAmount() != null
					&& feedInsertObj.getAmount() != 0) {
				updateQuery += "amount=:AMOUNT,";
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedCategory())) {
				updateQuery += "feedCategory=:FEEDCATEGORY,";
			}

			if (updateQuery.contains(",")) {
				updateQuery = updateQuery
						.substring(0, updateQuery.length() - 1);
			}
			System.out.println("updateQuery::" + updateQuery);
			updateQuery = "update FeedsTblVO set "
					+ updateQuery
					+ ",modifyDatetime=:MODIFYDATE,feedTime=:FEEDTIME where feedId=:FEEDID";

			qy = session.createQuery(updateQuery);
			qy.setInteger("FEEDID", feedInsertObj.getFeedId());
			if (Commonutility.checkempty(feedInsertObj.getFeedMsg())) {
				qy.setString("FEEDMSG", feedInsertObj.getFeedMsg());
			}
			if (feedInsertObj.getFeedPrivacyFlg() != 0) {
				System.out.println("------9i8 :"
						+ feedInsertObj.getFeedPrivacyFlg());
				qy.setInteger("PRIVACYFLG", feedInsertObj.getFeedPrivacyFlg());
			}
			System.out.println("0000000");
			if (Commonutility.checkempty(feedInsertObj.getUrlsThumbImg())) {
				qy.setString("URLTHUMB", feedInsertObj.getUrlsThumbImg());
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsTitle())) {
				qy.setString("URLTITLE", feedInsertObj.getUrlsTitle());
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsPageurl())) {
				qy.setString("PAGEURL", feedInsertObj.getUrlsPageurl());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedDesc())) {
				qy.setString("FEEDDESC", feedInsertObj.getFeedDesc());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedStitle())) {
				qy.setString("FEEDSTITLE", feedInsertObj.getFeedStitle());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedTitle())) {
				qy.setString("FEEDTITLE", feedInsertObj.getFeedTitle());
			}
			if (feedInsertObj != null && feedInsertObj.getAmount() != null
					&& feedInsertObj.getAmount() != 0) {
				qy.setFloat("AMOUNT", feedInsertObj.getAmount());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedCategory())) {
				qy.setString("FEEDCATEGORY", feedInsertObj.getFeedCategory());
			}
			System.out.println("99999");// FEEDTIME
			qy.setTimestamp("MODIFYDATE",
					Commonutility.enteyUpdateInsertDateTime());
			qy.setTimestamp("FEEDTIME",
					Commonutility.enteyUpdateInsertDateTime());
			System.out.println("------oip");
			qy.executeUpdate();
			// ###### file upload Start ######
			if (fileUpload != null && fileUpload.size() > 0) {
				log.logMessage("Enter into fileUpload feed edit", "info",
						FeedDAOService.class);
				for (int i = 0; i < fileUpload.size(); i++) {
					File mapFileUpload = fileUpload.get(i);
					String mapFileName = fileUploadFileName.get(i);
					String attachFileName = FilenameUtils
							.getBaseName(mapFileName);
					String attachFileExtension = FilenameUtils
							.getExtension(mapFileName);
					String uniqFileName = attachFileName + "_"
							+ commonutil.uniqueNoFromDate() + "."
							+ attachFileExtension;
					Integer fileType = mobComm
							.getFileExtensionType(attachFileExtension);
					log.logMessage("uniqFileName: " + uniqFileName, "info",
							FeedDAOService.class);
					log.logMessage("fileType: " + fileType, "info",
							FeedDAOService.class);
					if (fileType == 2) {
						videoFileMap.put(uniqFileName, mapFileUpload);
					} else {
						imageFileMap.put(uniqFileName, mapFileUpload);
					}
				}
				FeedsTblVO feedIdObj = new FeedsTblVO();
				feedIdObj.setFeedId(feedId);
				if (videoFileMap.size() > 0) {
					log.logMessage("Enter into video write feed edit", "info",
							FeedDAOService.class);
					String videoPath = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.videopath")
							+ feedId + "/";
					String videoPathThumb = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.video.thumbpath")
							+ feedId + "/";
					log.logMessage(videoPath + " :path: " + videoPathThumb,
							"info", FeedDAOService.class);
					for (Entry<String, File> entry : videoFileMap.entrySet()) {
						log.logMessage("Ente  feed attach video filename:"
								+ entry.getKey(), "info", FeedDAOService.class);
						log.logMessage(
								"Ente feed attach video filen:"
										+ entry.getValue(), "info",
								FeedDAOService.class);
						String videoName = entry.getKey();
						String videoThumbName = FilenameUtils
								.getBaseName(videoName) + ".jpeg";
						try {
							File destFile = new File(videoPath, videoName);
							FileUtils.copyFile(entry.getValue(), destFile);
							if (imageFileMap.containsKey(videoThumbName)) {
								File imageThumb = imageFileMap
										.get(videoThumbName);
								File destThumbFile = new File(imageThumb,
										videoPathThumb);
								FileUtils.copyFile(imageThumb, destThumbFile);
								imageFileMap.remove(videoThumbName);
							}
						} catch (Exception ex) {
							editCommitFlg = false;
							log.logMessage(
									"Exception ffound in video file upload : "
											+ ex, "error", FeedDAOService.class);
						}
						FeedattchTblVO feedAttchObj = new FeedattchTblVO();
						feedAttchObj.setIvrBnFEED_ID(feedIdObj);
						feedAttchObj.setIvrBnATTACH_NAME(videoName);
						feedAttchObj.setIvrBnTHUMP_IMAGE(videoThumbName);
						feedAttchObj.setIvrBnFILE_TYPE(2);
						feedAttchObj.setIvrBnSTATUS(1);
						feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						session.save(feedAttchObj);
					}
					log.logMessage("End video write feed post", "info",
							FeedDAOService.class);
				}
				if (imageFileMap.size() > 0) {
					log.logMessage("Enter into image write feed post", "info",
							FeedDAOService.class);
					String imagePathWeb = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.img.webpath")
							+ feedId + "/";
					String imagePathMobi = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.img.mobilepath")
							+ feedId + "/";
					for (Entry<String, File> entry : imageFileMap.entrySet()) {
						log.logMessage("Ente  feed attach image filename:"
								+ entry.getKey(), "info", FeedDAOService.class);
						log.logMessage(
								"Ente feed attach image filen:"
										+ entry.getValue(), "info",
								FeedDAOService.class);
						String imageName = entry.getKey();
						String imgBaseName = FilenameUtils
								.getBaseName(imageName);
						String imgExtenName = FilenameUtils
								.getExtension(imgBaseName);
						File imgFilePath = imageFileMap.get(imageName);
						Integer fileType = mobComm
								.getFileExtensionType(imgExtenName);
						FeedattchTblVO feedAttchObj = new FeedattchTblVO();
						feedAttchObj.setIvrBnFEED_ID(feedIdObj);
						feedAttchObj.setIvrBnSTATUS(1);
						feedAttchObj.setIvrBnATTACH_NAME(imageName);
						feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						if (fileType == 1) {
							feedAttchObj.setIvrBnFILE_TYPE(1);
							log.logMessage("imageName web :" + imageName,
									"info", FeedDAOService.class);
							// for web, image upload original size
							try {
								File destFileWeb = new File(imagePathWeb,
										imageName);
								FileUtils.copyFile(entry.getValue(),
										destFileWeb);
							} catch (Exception ex) {
								editCommitFlg = false;
								log.logMessage(
										"Exception found in image file upload for web : "
												+ ex, "error",
										FeedDAOService.class);
							}
							// for mobile, compress image upload
							int imgWitdh = 0;
							int imgHeight = 0;
							int imgOriginalWidth = mobiCommon
									.getImageWidth(imgFilePath);
							int imgOriginalHeight = mobiCommon
									.getImageHeight(imgFilePath);
							String imageHeightPro = getText("thump.img.height");
							String imageWidthPro = getText("thump.img.width");
							if (imgOriginalWidth > Commonutility
									.stringToInteger(imageWidthPro)) {
								imgWitdh = Commonutility
										.stringToInteger(imageWidthPro);
							} else {
								imgWitdh = imgOriginalWidth;
							}
							if (imgOriginalHeight > Commonutility
									.stringToInteger(imageHeightPro)) {
								imgHeight = Commonutility
										.stringToInteger(imageHeightPro);
							} else {
								imgHeight = imgOriginalHeight;
							}
							String imgSourcePath = imagePathWeb + imageName;
							String imgName = FilenameUtils
									.getBaseName(imageName);
							String imageFomat = FilenameUtils
									.getExtension(imageName);
							String imageFor = "all";
							log.logMessage("imageName for web compress : "
									+ imageName, "info", FeedDAOService.class);
							ImageCompress.toCompressImage(imgSourcePath,
									imagePathMobi, imgName, imageFomat,
									imageFor, imgWitdh, imgHeight);
						} else {
							feedAttchObj.setIvrBnFILE_TYPE(3);
							try {
								File destFileWeb = new File(imagePathWeb,
										imageName);
								File destFileMobi = new File(imagePathMobi,
										imageName);
								FileUtils.copyFile(entry.getValue(),
										destFileWeb);
								FileUtils.copyFile(entry.getValue(),
										destFileMobi);
							} catch (Exception ex) {
								editCommitFlg = false;
								log.logMessage(
										"Exception found in image file upload for type 3 : "
												+ ex, "error",
										FeedDAOService.class);
							}
						}
						session.save(feedAttchObj);
					}
					log.logMessage("End image write feed post", "info",
							FeedDAOService.class);
				}
			} else {
				log.logMessage("File upload empty", "info",
						FeedDAOService.class);
			}
			// ###### file upload end ######
			// ###### remove attachment Start ######
			CommonMobiDao commonServ = new CommonMobiDaoService();
			System.out.println("removeAttachIdsObj obj------------------- "
					+ removeAttachIdsObj);
			if (removeAttachIdsObj != null) {
				log.logMessage("Remove attachment Start", "info",
						FeedDAOService.class);
				String removeAttachIds = commonutil
						.jsnArryIntoStrBasedOnComma(removeAttachIdsObj);
				if (Commonutility.checkempty(removeAttachIds)) {
					if (!removeAttachIds.contains(",")) {
						removeAttachIds += ",";
					}
					if (removeAttachIds.contains(",")) {
						String[] removeIds = removeAttachIds.split(",");
						for (int j = 0; j < removeIds.length; j++) {
							String attchId = removeIds[j];
							if (Commonutility.checkempty(attchId)
									&& Commonutility.toCheckisNumeric(attchId)) {
								log.logMessage(
										"Enter into AttachmentDetails attachId: "
												+ Commonutility
														.stringToInteger(attchId),
										"info", FeedDAOService.class);
								FeedattchTblVO attachDelObj = new FeedattchTblVO();
								String qry = " from FeedattchTblVO where ivrBnSTATUS='1'  and  ivrBnATTCH_ID='"
										+ Commonutility
												.stringToInteger(attchId) + "'";
								System.out
										.println("FeedattchTblVO--before qry-----------------"
												+ qry);
								qy = session.createQuery(qry);
								attachDelObj = (FeedattchTblVO) qy
										.uniqueResult();
								log.logMessage(
										"End of queery execution of AttachmentDetails",
										"info", FeedDAOService.class);
								// FeedattchTblVO attachDelObj =
								// commonServ.getAttachmentDetails(Commonutility.stringToInteger(attchId));
								System.out
										.println("attachDelObj-------------------"
												+ attachDelObj);
								if (attachDelObj != null) {
									int fileType = attachDelObj
											.getIvrBnFILE_TYPE();
									System.out
											.println("fileType-------------------"
													+ fileType);
									File deleteFile = null;
									File deleteFileTwo = null;
									if (fileType == 1 || fileType == 3) {
										String imagePathWeb = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.img.webpath")
												+ feedId + "/";
										String imagePathMobi = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.img.mobilepath")
												+ feedId + "/";
										deleteFile = new File(
												imagePathWeb
														+ attachDelObj
																.getIvrBnATTACH_NAME());
										deleteFileTwo = new File(
												imagePathMobi
														+ attachDelObj
																.getIvrBnATTACH_NAME());
									} else if (fileType == 2) {
										String videoPath = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.videopath")
												+ feedId + "/";
										String videoPathThumb = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.video.thumbpath")
												+ feedId + "/";
										deleteFile = new File(
												videoPath
														+ attachDelObj
																.getIvrBnATTACH_NAME());
										deleteFileTwo = new File(
												videoPathThumb
														+ attachDelObj
																.getIvrBnATTACH_NAME());
									}
									try {
										if (deleteFile != null
												&& deleteFile.exists()) {
											FileUtils.forceDelete(deleteFile);
										}
										if (deleteFileTwo != null
												&& deleteFileTwo.exists()) {
											FileUtils
													.forceDelete(deleteFileTwo);
										}
									} catch (Exception ex) {
										log.logMessage(
												"feedEdit Exception found in file delete fileType="
														+ fileType + " :: "
														+ ex, "error",
												FeedDAOService.class);
									}
									String query = "delete FeedattchTblVO where ivrBnATTCH_ID=:ATTACHID";
									System.out
											.println("delete query-------------------"
													+ query
													+ "attchId------"
													+ attchId);
									Query qury = session.createQuery(query);
									qury.setInteger("ATTACHID", Commonutility
											.stringToInteger(attchId));
									qury.executeUpdate();
								}
							}
						}
					}
				} else {
					log.logMessage("Remove attachment JSONArray Not found",
							"info", FeedDAOService.class);
				}
			} else {
				log.logMessage("Remove attachment Not found", "info",
						FeedDAOService.class);
			}
			log.logMessage("End remove attachment ", "info",
					FeedDAOService.class);
			// ###### remove attachment end ######
			// ###### view tbl insert Start ######
			log.logMessage("View tbl insert Start ", "info",
					FeedDAOService.class);
			int isShareflg = 0;
			if (feedPrivacy != 0) {
				if (feedInsertObj != null && feedInsertObj.getFeedId() != null
						&& feedInsertObj.getFeedId() != 0) {
					try {
						log.logMessage("Enter into view feedid delete feedid:"
								+ feedInsertObj.getFeedId(), "info",
								FeedDAOService.class);
						String query = "delete FeedsViewTblVO where feedId.feedId=:FEEDID";
						Query qury = session.createQuery(query);
						qury.setInteger("FEEDID", feedInsertObj.getFeedId());
						int res = qury.executeUpdate();
						System.out.println("######### res :" + res);
						// editCommitFlg = true;
					} catch (Exception ex) {
						editCommitFlg = false;
						log.logMessage("Exception found in viewids remove:"
								+ ex, "error", FeedDAOService.class);
					}
				} else {
					editCommitFlg = false;
				}
				System.out.println("editCommitFlg-------------------"
						+ editCommitFlg);
				log.logMessage("End view id delete ", "info",
						FeedDAOService.class);
				FeedsViewTblVO feedviewObj = new FeedsViewTblVO();
				System.out.println("end of delete -------------------");
				System.out.println("feedInsertObj.getUsrId()--------"
						+ feedInsertObj.getUsrId());
				int societyId = feedInsertObj.getSocietyId().getSocietyId();
				System.out.println("societyId-------------------" + societyId);
				int userIdForView = 0;
				if (feedPrivacy == 2) {
					userIdForView = -1;
				} else if (feedPrivacy == 3) {
					userIdForView = -2;
				} else {
					userIdForView = feedInsertObj.getUsrId().getUserId();
				}
				System.out
						.println("feedInsertObj.getEntryBy()-------------------"
								+ feedInsertObj.getEntryBy());
				feedviewObj = feedInsertViewDataPack(userIdForView, feedId,
						feedInsertObj.getEntryBy(), societyId, isShareflg, -1,
						0);
				session.save(feedviewObj);
				System.out.println(editCommitFlg);
				if (Commonutility.checkempty(users)) {
					if (editCommitFlg) {
						if (!users.contains(",")) {
							users += ",";
						}
						if (users.contains(",")) {
							String[] viewIdsArry = users.split(",");
							log.logMessage("Feed viewid length:"
									+ viewIdsArry.length, "info",
									FeedDAOService.class);
							for (int i = 0; i < viewIdsArry.length; i++) {
								int viewId = 0;
								try {
									if (Commonutility
											.checkempty(viewIdsArry[i])
											&& Commonutility
													.toCheckisNumeric(viewIdsArry[i])) {
										log.logMessage(
												"Enter into view tbl insert id:"
														+ viewIdsArry[i],
												"info", FeedDAOService.class);
										viewId = Commonutility
												.stringToInteger(viewIdsArry[i]);
										feedviewObj = feedInsertViewDataPack(
												viewId, feedId,
												feedInsertObj.getEntryBy(),
												societyId, isShareflg, -1, 0);
										session.save(feedviewObj);
									}
								} catch (Exception ex) {
									editCommitFlg = false;
									log.logMessage(
											"Exception found in viewids insert:"
													+ ex, "error",
											FeedDAOService.class);
								}
							}
						}
					}
				}
				System.out
						.println("feedInsertObj.getEntryBy()------End-------------"
								+ feedInsertObj.getEntryBy());
			} else {

			}
			log.logMessage("View tbl insert End ", "info", FeedDAOService.class);
			// ###### view tbl insert end ######
			if (editCommitFlg) {
				tx.commit();
			} else {
				if (tx != null) {
					tx.rollback();
				}
				result = false;
			}
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = false;
			log.logMessage("feedEdit Exception found in  : " + ex, "error",
					FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			log = null;
			qy = null;
		}
		return result;
	}

	@Override
	public UserMasterTblVo getFeedPostedUserDetail(int userId) {
		Log log = new Log();
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from UserMasterTblVo where statusFlag=:STATUS  and  userId=:USER_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setInteger("USER_ID", userId);
			userdata = (UserMasterTblVo) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" checkUserInfo======" + ex);
			log.logMessage("Exception foun in getFeedPostedUserDetail : " + ex,
					"error", FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return userdata;
	}

	@Override
	public String totalNoNetworkUser(int rid) {
		Log log = new Log();
		String returnVal = "";
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into likeCountList ", "info",
					FeedDAOService.class);
			String sqlQuery = "select count(NETWORK_ID) from mvp_network_tbl where PARENT_USR_ID='"
					+ rid + "' and CONN_STS_FLG='1'";
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery).addScalar(
					"count(NETWORK_ID)", Hibernate.TEXT);
			returnVal = queryObj.uniqueResult().toString();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into totalNoNetworkUser total user:"
					+ returnVal, "info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in totalNoNetworkUser :" + ex,
					"error", FeedDAOService.class);
			returnVal = "";
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
		return returnVal;
	}

	@Override
	public List<Object[]> networkConectedList(int userid, String timestamp,
			int startLimit, int endLimit, String searchText) {
		Log log = new Log();
		List<Object[]> nwListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into likeCountList ", "info",
					FeedDAOService.class);
			String sqlQuery = "select (case when nwtbl.parent_usr_id= '"
					+ userid
					+ "' then (select usr_id from usr_reg_tbl where usr_id=nwtbl.connected_usr_id) "
					+ "when nwtbl.connected_usr_id= '"
					+ userid
					+ "' then (select usr_id from usr_reg_tbl where usr_id=nwtbl.parent_usr_id) end)as usr_id ,"
					+ "(case when nwtbl.parent_usr_id= '"
					+ userid
					+ "' then (select fname from usr_reg_tbl where usr_id=nwtbl.connected_usr_id) "
					+ "when nwtbl.connected_usr_id= '"
					+ userid
					+ "' then (select fname from usr_reg_tbl where usr_id=nwtbl.parent_usr_id) end)as fname ,"
					+ "(case when nwtbl.parent_usr_id= '"
					+ userid
					+ "' then (select image_name_mobile from usr_reg_tbl where usr_id=nwtbl.connected_usr_id) "
					+ "when nwtbl.connected_usr_id= '"
					+ userid
					+ "' then (select image_name_mobile from usr_reg_tbl where usr_id=nwtbl.parent_usr_id) end)as image_name_mobile ,"
					+ "(case when nwtbl.parent_usr_id= '"
					+ userid
					+ "' then (select flat_no from usr_reg_tbl where usr_id=nwtbl.connected_usr_id) "
					+ "when nwtbl.connected_usr_id= '"
					+ userid
					+ "' then (select flat_no from usr_reg_tbl where usr_id=nwtbl.parent_usr_id) end)as flat_no ,"
					+ "nwtbl.CONN_STS_FLG,(case when nwtbl.parent_usr_id= '"
					+ userid
					+ "' then (select SOCIAL_PICTURE from usr_reg_tbl where usr_id=nwtbl.connected_usr_id) "
					+ "when nwtbl.connected_usr_id= '"
					+ userid
					+ "' then (select SOCIAL_PICTURE from usr_reg_tbl where usr_id=nwtbl.parent_usr_id) end)as SOCIAL_PICTURE "
					+ "from usr_reg_tbl usrtbl inner join  mvp_network_tbl nwtbl on ( usrtbl.usr_id=parent_usr_id or usrtbl.usr_id=connected_usr_id) "
					+ "where usrtbl.usr_id='"
					+ userid
					+ "' and (usrtbl.group_id='5' or group_id='6') and  nwtbl.CONN_STS_FLG ='1' and nwtbl.ENTRY_DATETIME<STR_TO_DATE('"
					+ timestamp
					+ "','%Y-%m-%d %H:%i:%S')"
					+ " and concat(ifnull(usrtbl.flat_no,''),ifnull(usrtbl.fname,'')) like '%"
					+ searchText
					+ "%'   order by nwtbl.MODIFY_DATETIME desc limit "
					+ startLimit + "," + endLimit;

			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("usr_id", Hibernate.TEXT)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_mobile", Hibernate.TEXT)
					.addScalar("flat_no", Hibernate.TEXT)
					.addScalar("nwtbl.CONN_STS_FLG", Hibernate.TEXT)
					.addScalar("SOCIAL_PICTURE", Hibernate.TEXT);
			nwListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage(
					"Enter into networkConectedList size :" + nwListObj.size(),
					"info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in networkConectedList :" + ex,
					"error", FeedDAOService.class);
			nwListObj = null;
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
		return nwListObj;
	}

	@Override
	public int feedInsertAttachment(FeedattchTblVO feedAttach) {
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locTimelineFeedid = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(feedAttach);
			locTimelineFeedid = feedAttach.getIvrBnATTCH_ID();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception feedInsertAttachment: " + e, "error",
					FeedDAOService.class);
			locTimelineFeedid = -1;
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
		return locTimelineFeedid;
	}

	@Override
	public int feedInsertView(int userId, int feedId, int entryBy) {
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locviewFeedid = -1;
		FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			FeedsTblVO feedObjV = new FeedsTblVO();
			UserMasterTblVo userMsrObjV = new UserMasterTblVo();
			userMsrObjV.setUserId(userId);
			feedObjV.setFeedId(feedId);
			feedViewObj.setFeedId(feedObjV);
			feedViewObj.setUsrId(userId);
			feedViewObj.setEntryBy(entryBy);
			feedViewObj.setEntryDatetime(Commonutility
					.enteyUpdateInsertDateTime());
			session.save(feedViewObj);
			locviewFeedid = feedViewObj.getUniqId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception feedInsertView: " + e, "error",
					FeedDAOService.class);
			locviewFeedid = -1;
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
		return locviewFeedid;
	}

	@Override
	public Session feedAttachUpload(Session session, int feedId,
			List<File> fileUpload, List<String> fileUploadContentType,
			List<String> fileUploadFileName) {
		Log log = new Log();
		boolean commitFlg = true;
		HashMap<String, File> imageFileMap = new HashMap<String, File>();
		HashMap<String, File> videoFileMap = new HashMap<String, File>();
		FunctionUtility commonutil = new FunctionUtilityServices();
		mobiCommon mobComm = new mobiCommon();
		try {
			log.logMessage("Enter into feedAttachUpload", "info",
					FeedDAOService.class);
			if (fileUpload.size() > 0) {
				for (int i = 0; i < fileUpload.size(); i++) {
					File mapFileUpload = fileUpload.get(i);
					String mapFileName = fileUploadFileName.get(i);
					String attachFileName = FilenameUtils
							.getBaseName(mapFileName);
					String attachFileExtension = FilenameUtils
							.getExtension(mapFileName);
					String uniqFileName = attachFileName + "_"
							+ commonutil.uniqueNoFromDate() + "."
							+ attachFileExtension;
					Integer fileType = mobComm
							.getFileExtensionType(attachFileExtension);
					if (fileType == 2) {
						videoFileMap.put(uniqFileName, mapFileUpload);
					} else {
						imageFileMap.put(uniqFileName, mapFileUpload);
					}
				}
				FeedsTblVO feedIdObj = new FeedsTblVO();
				feedIdObj.setFeedId(feedId);
				if (videoFileMap.size() > 0) {
					log.logMessage("Enter into video write feedAttachUpload",
							"info", FeedDAOService.class);
					String videoPath = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.videopath")
							+ feedId + "/";
					String videoPathThumb = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.video.thumbpath")
							+ feedId + "/";
					log.logMessage(videoPath + " :path: " + videoPathThumb,
							"info", FeedDAOService.class);
					for (Entry<String, File> entry : videoFileMap.entrySet()) {
						log.logMessage("Ente  feed attach video filename:"
								+ entry.getKey(), "info", FeedDAOService.class);
						log.logMessage(
								"Ente feed attach video filen:"
										+ entry.getValue(), "info",
								FeedDAOService.class);
						String videoName = entry.getKey();
						String videoThumbName = FilenameUtils
								.getBaseName(videoName) + ".jpeg";
						try {
							File destFile = new File(videoPath, videoName);
							FileUtils.copyFile(entry.getValue(), destFile);
							if (imageFileMap.containsKey(videoThumbName)) {
								File imageThumb = imageFileMap
										.get(videoThumbName);
								File destThumbFile = new File(imageThumb,
										videoPathThumb);
								FileUtils.copyFile(imageThumb, destThumbFile);
								imageFileMap.remove(videoThumbName);
							}
						} catch (Exception ex) {
							commitFlg = false;
							log.logMessage(
									"Exception found in video file upload feedAttachUpload: "
											+ ex, "error", FeedDAOService.class);
						}
						FeedattchTblVO feedAttchObj = new FeedattchTblVO();
						feedAttchObj.setIvrBnFEED_ID(feedIdObj);
						feedAttchObj.setIvrBnATTACH_NAME(videoName);
						feedAttchObj.setIvrBnTHUMP_IMAGE(videoThumbName);
						feedAttchObj.setIvrBnFILE_TYPE(2);
						feedAttchObj.setIvrBnSTATUS(1);
						feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						if (commitFlg) {
							session.save(feedAttchObj);
						}
					}
					log.logMessage("End video write feedAttachUpload", "info",
							FeedDAOService.class);
				}
				if (imageFileMap.size() > 0) {
					log.logMessage("Enter into image write feed post", "info",
							FeedDAOService.class);
					String imagePathWeb = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.img.webpath")
							+ feedId + "/";
					String imagePathMobi = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.img.mobilepath")
							+ feedId + "/";
					for (Entry<String, File> entry : imageFileMap.entrySet()) {
						log.logMessage(
								"Ente  feed attach image filename feedAttachUpload:"
										+ entry.getKey(), "info",
								FeedDAOService.class);
						log.logMessage(
								"Ente feed attach image filen feedAttachUpload:"
										+ entry.getValue(), "info",
								FeedDAOService.class);
						String imageName = entry.getKey();
						String imgBaseName = FilenameUtils
								.getBaseName(imageName);
						String imgExtenName = FilenameUtils
								.getExtension(imgBaseName);
						File imgFilePath = imageFileMap.get(imageName);
						Integer fileType = mobComm
								.getFileExtensionType(imgExtenName);
						FeedattchTblVO feedAttchObj = new FeedattchTblVO();
						feedAttchObj.setIvrBnFEED_ID(feedIdObj);
						feedAttchObj.setIvrBnSTATUS(1);
						feedAttchObj.setIvrBnATTACH_NAME(imageName);
						feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						if (fileType == 1) {
							feedAttchObj.setIvrBnFILE_TYPE(1);
							log.logMessage("imageName web feedAttachUpload:"
									+ imageName, "info", FeedDAOService.class);
							// for web, image upload original size
							try {
								File destFileWeb = new File(imagePathWeb,
										imageName);
								// File destFileMobi = new File(imagePathMobi,
								// imageName);
								FileUtils.copyFile(entry.getValue(),
										destFileWeb);
								// FileUtils.copyFile( entry.getValue(),
								// destFileMobi);
							} catch (Exception ex) {
								commitFlg = false;
								log.logMessage(
										"Exception found in image file upload for web feedAttachUpload: "
												+ ex, "error",
										FeedDAOService.class);
							}
							// for mobile, compress image upload
							int imgWitdh = 0;
							int imgHeight = 0;
							int imgOriginalWidth = mobiCommon
									.getImageWidth(imgFilePath);
							int imgOriginalHeight = mobiCommon
									.getImageHeight(imgFilePath);
							String imageHeightPro = getText("thump.img.height");
							String imageWidthPro = getText("thump.img.width");
							if (imgOriginalWidth > Commonutility
									.stringToInteger(imageWidthPro)) {
								imgWitdh = Commonutility
										.stringToInteger(imageWidthPro);
							} else {
								imgWitdh = imgOriginalWidth;
							}
							if (imgOriginalHeight > Commonutility
									.stringToInteger(imageHeightPro)) {
								imgHeight = Commonutility
										.stringToInteger(imageHeightPro);
							} else {
								imgHeight = imgOriginalHeight;
							}
							String imgSourcePath = imagePathWeb + imageName;
							String imgName = FilenameUtils
									.getBaseName(imageName);
							String imageFomat = FilenameUtils
									.getExtension(imageName);
							String imageFor = "all";
							log.logMessage(
									"imageName for web compress feedAttachUpload: "
											+ imageName, "info",
									FeedDAOService.class);
							ImageCompress.toCompressImage(imgSourcePath,
									imagePathMobi, imgName, imageFomat,
									imageFor, imgWitdh, imgHeight);
						} else {
							feedAttchObj.setIvrBnFILE_TYPE(3);
							try {
								File destFileWeb = new File(imagePathWeb,
										imageName);
								File destFileMobi = new File(imagePathMobi,
										imageName);
								FileUtils.copyFile(entry.getValue(),
										destFileWeb);
								FileUtils.copyFile(entry.getValue(),
										destFileMobi);
							} catch (Exception ex) {
								commitFlg = false;
								log.logMessage(
										"Exception found in image file upload for feedAttachUpload type 3 : "
												+ ex, "error",
										FeedDAOService.class);
							}
						}
						if (commitFlg) {
							session.save(feedAttchObj);
						}
					}
					log.logMessage(
							"End image write feed post feedAttachUpload",
							"info", FeedDAOService.class);
				}
			} else {
				log.logMessage("File upload empty feedAttachUpload", "info",
						FeedDAOService.class);
			}
			if (!commitFlg) {
				session = null;
			}
		} catch (Exception ex) {
			log.logMessage("Exception feedAttachUpload: " + ex, "error",
					FeedDAOService.class);
		}
		return session;
	}

	@Override
	public FeedsTblVO getFeedDetails(int rid) {
		FeedsTblVO feedMst = new FeedsTblVO();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from FeedsTblVO where feedStatus=:STATUS  and feedType=:FEED_TYP and feedTypeId=:FEED_TYP_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setInteger("FEED_TYP", 1);
			qy.setInteger("FEED_TYP_ID", rid);
			feedMst = (FeedsTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFeedDetails======" + ex);
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
	public FeedCommentTblVO commentData(int cmntId) {
		Log log = new Log();
		FeedCommentTblVO commentObj = new FeedCommentTblVO();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into commentData ", "info",
					FeedDAOService.class);
			String sqlQuery = " from FeedCommentTblVO where status=:STATUS  and  comment_id=:CMT_ID";
			System.out.println("commentList sqlQuery::" + sqlQuery);
			Query quryObj = session.createQuery(sqlQuery);
			quryObj.setInteger("STATUS", 1);
			quryObj.setInteger("CMT_ID", cmntId);
			commentObj = (FeedCommentTblVO) quryObj.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in commentData :" + ex, "error",
					FeedDAOService.class);
			commentObj = null;
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
		return commentObj;
	}

	@Override
	public FeedsTblVO getFeedDetailsByEventIdRid(int eventId, int feedType,
			int rid) {
		// TODO Auto-generated method stub
		FeedsTblVO feedMst = new FeedsTblVO();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from FeedsTblVO where feedStatus=:STATUS  and  feedType=:FEED_TYPE and feedTypeId=:EVENT_ID and usrId.userId=:RID";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setInteger("FEED_TYPE", feedType);
			qy.setInteger("EVENT_ID", eventId);
			qy.setInteger("RID", rid);
			feedMst = (FeedsTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFeedDetails======" + ex);
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
	public FeedsTblVO getFeedDetailsByEventId(int eventId, int feedType) {
		// TODO Auto-generated method stub
		FeedsTblVO feedMst = new FeedsTblVO();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from FeedsTblVO where feedStatus=:STATUS  and  feedType=:FEED_TYPE and feedTypeId=:EVENT_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setInteger("FEED_TYPE", feedType);
			qy.setInteger("EVENT_ID", eventId);
			feedMst = (FeedsTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFeedDetails======" + ex);
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
	public List<Object[]> networkConectedAndSocityIdList(int userid,
			String socityId, String timestamp, int startLimit, int endLimit,
			String searchTxt) {
		Log log = new Log();
		List<Object[]> nwSidListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into likeCountList ", "info",
					FeedDAOService.class);
			// sqlQuery =
			// "select usr_id,society_id,nwfrd,flat_no,image_name_mobile,mobile_no,fname from (select usrtbl.usr_id usr_id,usrtbl.society_id society_id ,case when (select count(*) from mvp_network_tbl where ((parent_usr_id=31 and connected_usr_id=usrtbl.usr_id) or (parent_usr_id=usrtbl.usr_id and connected_usr_id=31) ) and 	entry_datetime<='2016-11-19 09:58:52')>0 then 1 when (select count(*) from mvp_network_tbl where ((parent_usr_id=31 and connected_usr_id=usrtbl.usr_id) or (parent_usr_id=usrtbl.usr_id and connected_usr_id=31) ) and 	entry_datetime>'2016-11-19 09:58:52')>0 then -1 else 0 end nwfrd, usrtbl.flat_no flat_no,usrtbl.image_name_mobile image_name_mobile,usrtbl.mobile_no mobile_no,usrtbl.fname fname from usr_reg_tbl usrtbl left join society_mst_tbl scty on usrtbl.society_id=scty.society_id where scty.activation_key='karkzd0lptfrhmo4jd26de9rb' and usrtbl.usr_id!=31)tt where nwfrd in(0,1) order by nwfrd desc limit 0,10";

			String sqlQuery = "select usr_id,fname,image_name_mobile,flat_no,nwfrd ,social_picture "
					+ "from (select usrtbl.usr_id usr_id,usrtbl.society_id society_id ,case when (select count(*) from mvp_network_tbl "
					+ "where ((parent_usr_id= '"
					+ userid
					+ "' and connected_usr_id=usrtbl.usr_id) or "
					+ "(parent_usr_id=usrtbl.usr_id and connected_usr_id= '"
					+ userid
					+ "') ) and 	entry_datetime<=STR_TO_DATE('"
					+ timestamp
					+ "','%Y-%m-%d %H:%i:%S'))>0 then 1 when (select count(*) from mvp_network_tbl where ((parent_usr_id='"
					+ userid
					+ "' "
					+ "and connected_usr_id=usrtbl.usr_id) or (parent_usr_id=usrtbl.usr_id and connected_usr_id= '"
					+ userid
					+ "'		) ) "
					+ "and 	entry_datetime>STR_TO_DATE('"
					+ timestamp
					+ "','%Y-%m-%d %H:%i:%S'))>0 then -1 else 0 end nwfrd, usrtbl.flat_no flat_no,usrtbl.image_name_mobile image_name_mobile,"
					+ " usrtbl.mobile_no mobile_no,usrtbl.fname fname,usrtbl.social_picture from usr_reg_tbl "
					+ "usrtbl left join society_mst_tbl scty on usrtbl.society_id=scty.society_id "
					+ "where scty.activation_key='"
					+ socityId
					+ "' and usrtbl.usr_id!= '"
					+ userid
					+ "' and (usrtbl.group_id='5' or group_id='6') and usrtbl.ACT_STS=1 and concat(ifnull(usrtbl.flat_no,''),ifnull(usrtbl.fname,'')) like '%"
					+ searchTxt
					+ "%')tt where nwfrd in(0,1) order by nwfrd desc limit "
					+ startLimit + "," + endLimit;
			/*
			 * String sqlQuery =
			 * "select usr_id,fname,image_name_mobile,flat_no,nwfrd " +
			 * "from (select usrtbl.usr_id usr_id,usrtbl.society_id society_id ,case when (select count(*) from mvp_network_tbl "
			 * + "where ((parent_usr_id= '" + userid +
			 * "' and connected_usr_id=usrtbl.usr_id) or " +
			 * "(parent_usr_id=usrtbl.usr_id and connected_usr_id= '" + userid +
			 * "') ) " + "and 	entry_datetime<=STR_TO_DATE('" + timestamp +
			 * "','%Y-%m-%d %H:%i:%S'))>0 then 1 when (select count(*) from mvp_network_tbl where ((parent_usr_id=31 "
			 * +
			 * "and connected_usr_id=usrtbl.usr_id) or (parent_usr_id=usrtbl.usr_id and connected_usr_id= '"
			 * + userid + "'		) ) " + "and 	entry_datetime>STR_TO_DATE('" +
			 * timestamp +
			 * "','%Y-%m-%d %H:%i:%S'))>0 then -1 else 0 end nwfrd, usrtbl.flat_no flat_no,usrtbl.image_name_mobile image_name_mobile, "
			 * +
			 * "usrtbl.mobile_no mobile_no,usrtbl.fname fname from usr_reg_tbl usrtbl "
			 * +
			 * "left join society_mst_tbl scty on usrtbl.society_id=scty.society_id "
			 * + "where scty.activation_key='" + socityId +
			 * "' and usrtbl.usr_id!= '" + userid + "' " +
			 * "and concat(ifnull(usrtbl.flat_no,''),ifnull(usrtbl.fname,'')) like '%"
			 * + searchTxt + "%')tt " +
			 * "where nwfrd in(0,1) order by nwfrd desc limit " + startLimit +
			 * "," + endLimit;
			 */
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("usr_id", Hibernate.TEXT)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_mobile", Hibernate.TEXT)
					.addScalar("flat_no", Hibernate.TEXT)
					.addScalar("nwfrd", Hibernate.TEXT)
					.addScalar("SOCIAL_PICTURE", Hibernate.TEXT);
			nwSidListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into networkConectedList size :"
					+ nwSidListObj.size(), "info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in networkConectedList :" + ex,
					"error", FeedDAOService.class);
			nwSidListObj = null;
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
		return nwSidListObj;
	}

	@Override
	public List<Object[]> feedListProc(int rid, String socityKey,
			String timeStamp, int feedType, int postBy, String feedCategory,
			int startLimit, int endLimit) {
		Log log = new Log();
		List<Object[]> feedListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into feedListProc ", "info",
					FeedDAOService.class);
			log.logMessage("rid :" + rid, "info", FeedDAOService.class);
			log.logMessage("socityKey :" + socityKey, "info",
					FeedDAOService.class);
			log.logMessage("feedType :" + feedType, "info",
					FeedDAOService.class);
			log.logMessage("postBy :" + postBy, "info", FeedDAOService.class);
			log.logMessage("feedCategory :" + feedCategory, "info",
					FeedDAOService.class);
			log.logMessage("timeStamp :" + timeStamp, "info",
					FeedDAOService.class);
			log.logMessage("startLimit :" + startLimit, "info",
					FeedDAOService.class);
			log.logMessage("endLimit :" + endLimit, "info",
					FeedDAOService.class);
			String sqlQuery = "call PROC_FEEDTIMELINE_VIEW(49,'YPKHMJSZ80K2T2ZWZB87CPG1L','2016-11-18 13:16:21')";
			// pr_typ,pr_catg,pr_PRVCY_FLG
			// CALL
			// PROC_FEED_TIMELINE_VIEW(49,'YPKHMJSZ80K2T2ZWZB87CPG1L','2016-11-18
			// 13:16:21',NULL,NULL,NULL,0,10);
			sqlQuery = "CALL PROC_FEED_TIMELINE_VIEW(:FEEDID,:USERID,:SOCITYKEY,:TIMESTAMP,:FEEDTYPE,:FEEDCATEGORY,:PRIVACYFLAG,:STARTLIMIT,:ENDLIMIT,:STATUSFLG,:SEARCH)";
			// sqlQuery =
			// "call PROC_FEEDTIMELINE_VIEW(:USERID,:SOCITYKEY,:TIMESTAMP)";
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();

			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("feed_id", Hibernate.INTEGER)
					.addScalar("feed_typ", Hibernate.INTEGER)
					.addScalar("attname", Hibernate.TEXT)
					.addScalar("urls_thumb_img", Hibernate.TEXT)
					.addScalar("urls_title", Hibernate.TEXT)
					.addScalar("urls_pageurl", Hibernate.TEXT)
					.addScalar("feed_category", Hibernate.TEXT)
					.addScalar("feed_title", Hibernate.TEXT)
					.addScalar("feed_stitle", Hibernate.TEXT)
					.addScalar("feed_desc", Hibernate.TEXT)
					.addScalar("feed_time", Hibernate.TIMESTAMP)
					.addScalar("amount", Hibernate.FLOAT)
					.addScalar("post_as", Hibernate.TEXT)
					.addScalar("feed_privacy_flg", Hibernate.INTEGER)
					.addScalar("entry_by", Hibernate.INTEGER)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_mobile", Hibernate.TEXT)
					.addScalar("feed_location", Hibernate.TEXT)
					.addScalar("feed_msg", Hibernate.TEXT)
					.addScalar("is_myfeed", Hibernate.INTEGER)
					.addScalar("is_shared", Hibernate.INTEGER)
					.addScalar("originator_name", Hibernate.TEXT)
					.addScalar("originator_id", Hibernate.INTEGER)
					.addScalar("like_unlike_flg", Hibernate.INTEGER)
					.addScalar("like_count", Hibernate.LONG)
					.addScalar("share_count", Hibernate.LONG)
					.addScalar("comment_count", Hibernate.LONG)
					.addScalar("feed_typ_id", Hibernate.INTEGER)
					.addScalar("share_entryby", Hibernate.INTEGER)
					.addScalar("feedviewid", Hibernate.INTEGER)
					.addScalar("custom_friends", Hibernate.TEXT)
					.addScalar("social_picture", Hibernate.TEXT)
					.addScalar("additional_data", Hibernate.TEXT);

			queryObj.setString("FEEDID", "");
			queryObj.setInteger("USERID", rid);
			queryObj.setString("SOCITYKEY", socityKey);
			queryObj.setString("TIMESTAMP", timeStamp);
			if (Commonutility.checkIntempty(feedType)) {
				queryObj.setString("FEEDTYPE",
						Commonutility.intToString(feedType));
			} else {
				queryObj.setString("FEEDTYPE", "");
			}
			if (Commonutility.checkIntempty(postBy)) {
				queryObj.setString("PRIVACYFLAG",
						Commonutility.intToString(postBy));
			} else {
				queryObj.setString("PRIVACYFLAG", "");
			}
			if (Commonutility.checkempty(feedCategory)) {
				queryObj.setString("FEEDCATEGORY", feedCategory);
			} else {
				queryObj.setString("FEEDCATEGORY", "");
			}
			queryObj.setString("STARTLIMIT",
					Commonutility.intToString(startLimit));
			System.out.println("Commonutility.intToString(endLimit):"
					+ Commonutility.intToString(endLimit));
			queryObj.setString("ENDLIMIT", Commonutility.intToString(endLimit));
			// queryObj.setString("ENDLIMIT", "1");
			queryObj.setInteger("STATUSFLG", 0);
			queryObj.setString("SEARCH", "");
			System.out.println("status flag=------------" + 0);
			System.out.println("hellllo");
			feedListObj = queryObj.list();

			System.out.println(queryObj.toString());
			log.logMessage(
					"Enter into feedListProc size :" + feedListObj.size(),
					"info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedListProc :" + ex, "error",
					FeedDAOService.class);
			feedListObj = null;
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
		return feedListObj;
	}

	@Override
	public int feedListTotalCountGet(int rid, String socityKey,
			String timeStamp, String feedType, int postBy, String feedCategory,
			String search) {
		Log log = new Log();
		int retCount = 0;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into feedListTotalCountGet ", "info",
					FeedDAOService.class);
			log.logMessage("rid :" + rid, "info", FeedDAOService.class);
			log.logMessage("socityKey :" + socityKey, "info",
					FeedDAOService.class);
			log.logMessage("feedType :" + feedType, "info",
					FeedDAOService.class);
			log.logMessage("postBy :" + postBy, "info", FeedDAOService.class);
			log.logMessage("feedCategory :" + feedCategory, "info",
					FeedDAOService.class);
			log.logMessage("timeStamp :" + timeStamp, "info",
					FeedDAOService.class);
			String sqlQuery = "call PROC_FEED_TIMELINE_VIEW(49,'YPKHMJSZ80K2T2ZWZB87CPG1L','2016-11-18 13:16:21')";
			sqlQuery = "CALL PROC_FEED_TIMELINE_VIEW(:FEEDID,:USERID,:SOCITYKEY,:TIMESTAMP,:FEEDTYPE,:FEEDCATEGORY,:PRIVACYFLAG,:STARTLIMIT,:ENDLIMIT,:STATUSFLG,:SEARCH)";
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery).addScalar("cnt",
					Hibernate.INTEGER);

			queryObj.setString("FEEDID", "");
			queryObj.setInteger("USERID", rid);
			queryObj.setString("SOCITYKEY", socityKey);
			queryObj.setString("TIMESTAMP", timeStamp);
			if (Commonutility.checkempty(feedType)) {
				queryObj.setString("FEEDTYPE", feedType);
			} else {
				queryObj.setString("FEEDTYPE", "");
			}
			if (Commonutility.checkIntempty(postBy)) {
				queryObj.setString("PRIVACYFLAG",
						Commonutility.intToString(postBy));
			} else {
				queryObj.setString("PRIVACYFLAG", "");
			}
			if (Commonutility.checkempty(feedCategory)) {
				queryObj.setString("FEEDCATEGORY", feedCategory);
			} else {
				queryObj.setString("FEEDCATEGORY", "");
			}
			queryObj.setString("STARTLIMIT", "");
			queryObj.setString("ENDLIMIT", "");
			queryObj.setInteger("STATUSFLG", 1);
			if (Commonutility.checkempty(search)) {
				queryObj.setString("SEARCH", search);
			} else {
				queryObj.setString("SEARCH", "");
			}
			System.out.println("status flag=------------" + 1);
			// retCount =((Number) queryObj.uniqueResult()).intValue();
			// retCount =(int) queryObj.uniqueResult();
			System.out.println("############ 5 #######");
			retCount = ((Number) queryObj.uniqueResult()).intValue();
			System.out.println("----sdsd---:::##### :" + retCount);
			System.out.println(retCount);
			log.logMessage("Enter into feedListTotalCountGet totoal count:"
					+ retCount, "info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedListTotalCountGet :" + ex,
					"error", FeedDAOService.class);
			retCount = 0;
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
		return retCount;
	}

	@Override
	public boolean feedViewDelete(FeedsViewTblVO feedViewObj, int feedDelFlg) {
		/**
		 * feedDelFlg 1 - my feed, 2- own share
		 */
		Session session = null;
		Transaction tx = null;
		Log log = new Log();
		Query qy = null;
		boolean result = false;
		try {
			boolean commitlg = false;
			log.logMessage("enter feed view  Delete feedDelFlg:" + feedDelFlg,
					"info", FeedDAOService.class);
			System.out.println("---:" + feedViewObj.getFeedId().getFeedId());
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String query = "";
			if (Commonutility.checkIntempty(feedDelFlg)) {
				if (feedDelFlg == 1) {
					query = "delete FeedsViewTblVO where feedId=:FEEDID";
				} else if (feedDelFlg == 2) {
					query = "update FeedsViewTblVO set status=:STATUS,modifyDatetime=:MODYDATETIME where uniqId=:UNIQID";
				}

			}
			log.logMessage("feed view  Delete query:" + query, "info",
					FeedDAOService.class);
			qy = session.createQuery(query);
			if (Commonutility.checkIntempty(feedDelFlg)) {
				boolean flg = false;
				if (feedDelFlg == 1) {
					System.out.println("feedViewObj.getFeedId().getFeedId():"
							+ feedViewObj.getFeedId().getFeedId());
					qy.setInteger("FEEDID", feedViewObj.getFeedId().getFeedId());
					flg = true;
				} else if (feedDelFlg == 2) {
					System.out.println("elle shere");
					qy.setInteger("STATUS", 0);
					qy.setInteger("UNIQID", feedViewObj.getUniqId());
					qy.setTimestamp("MODYDATETIME",
							Commonutility.enteyUpdateInsertDateTime());
					flg = true;
				}
				if (flg) {
					qy.executeUpdate();
					commitlg = true;
				}

			}
			if (Commonutility.checkIntempty(feedDelFlg) && commitlg) {
				if (feedDelFlg == 2 && feedViewObj.getParentViewId() > 0) {
					log.logMessage("feed view child Delete ", "info",
							FeedDAOService.class);
					query = "update FeedsViewTblVO set status=:STATUS,modifyDatetime=:MODYDATETIME where parentViewId=:PARENTVIEWID";
					qy = session.createQuery(query);
					qy.setInteger("STATUS", 0);
					qy.setInteger("PARENTVIEWID", feedViewObj.getParentViewId());
					qy.setTimestamp("MODYDATETIME",
							Commonutility.enteyUpdateInsertDateTime());
					qy.executeUpdate();
					commitlg = true;
				}
			}
			log.logMessage("end feed view Delete commitlg:" + commitlg, "info",
					FeedDAOService.class);
			if (commitlg) {
				System.out.println("-----e");
				tx.commit();
				System.out.println("----d-e");
				result = true;
				System.out.println("-d----e");
			} else {
				if (tx != null) {
					tx.rollback();
				}
			}
			System.out.println("-----hrllll");
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = false;
			log.logMessage("feed View Delete Exception found in  : " + ex,
					"error", FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			log = null;
			qy = null;
		}
		return result;
	}

	@Override
	public FeedsTblVO feedData(int feedId) {
		Log log = new Log();
		FeedsTblVO feedDataObj = new FeedsTblVO();
		Session session = HibernateUtil.getSession();
		try {
			log.logMessage("Enter into feedData ", "info", FeedDAOService.class);
			String sqlQuery = " from FeedsTblVO where feedStatus=:STATUS  and  feedId=:FEEDID";
			System.out.println("feedData sqlQuery::" + sqlQuery);
			Query quryObj = session.createQuery(sqlQuery);
			quryObj.setInteger("STATUS", 1);
			quryObj.setInteger("FEEDID", feedId);
			feedDataObj = (FeedsTblVO) quryObj.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedData :" + ex, "error",
					FeedDAOService.class);
			feedDataObj = null;
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			log = null;
		}
		return feedDataObj;
	}

	@Override
	public FeedsViewTblVO feedViewData(int feedViewId) {
		Log log = new Log();
		FeedsViewTblVO feedViewDataObj = new FeedsViewTblVO();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into feedViewData ", "info",
					FeedDAOService.class);
			String sqlQuery = " from FeedsViewTblVO where status=:STATUS  and  uniqId=:UNIQID";
			System.out.println("feedViewData sqlQuery::" + sqlQuery);
			Query quryObj = session.createQuery(sqlQuery);
			quryObj.setInteger("STATUS", 1);
			quryObj.setInteger("UNIQID", feedViewId);
			feedViewDataObj = (FeedsViewTblVO) quryObj.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedViewData :" + ex, "error",
					FeedDAOService.class);
			feedViewDataObj = null;
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
		return feedViewDataObj;
	}

	@Override
	public boolean shareFeed(int rid, int feedId, int feedPrivacy,
			int socityId, String users, int parentViewId) {
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		boolean retValue = false;
		try {
			log.logMessage("Enter into  shareFeed", "info",
					FeedDAOService.class);
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			boolean commitFlg = true;
			int isShareflg = 1;
			FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
			if (Commonutility.checkempty(users) && feedPrivacy == 4) {
				log.logMessage("Feed view share start privacy flg == 4",
						"info", FeedDAOService.class);
				int retParentViewId = -1;
				feedViewObj = feedInsertViewDataPack(rid, feedId, rid,
						socityId, isShareflg, feedPrivacy, parentViewId);

				if (feedViewObj != null) {
					session.save(feedViewObj);
					System.out
							.println("111111111111111111111111111rid------------"
									+ rid);
					retParentViewId = feedViewObj.getUniqId();
					log.logMessage(
							"Feed share own view id=" + feedViewObj.getUniqId(),
							"info", FeedDAOService.class);
				} else {
					commitFlg = false;
				}
				if (commitFlg) {
					int friendUserId = 0;
					if (!users.contains(",")) {
						users += ",";
					}
					String[] userIdArr = users.split(",");
					log.logMessage("Total number of friends id="
							+ userIdArr.length, "info", FeedDAOService.class);
					for (int i = 0; i < userIdArr.length; i++) {
						log.logMessage(i
								+ "<==Size ::single friends list viewId:"
								+ userIdArr[i], "info", FeedDAOService.class);
						if (Commonutility.checkempty(userIdArr[i])
								&& Commonutility.toCheckisNumeric(userIdArr[i])) {
							friendUserId = Commonutility
									.stringToInteger(userIdArr[i]);
							feedViewObj = feedInsertViewDataPack(friendUserId,
									feedId, rid, socityId, isShareflg,
									feedPrivacy, retParentViewId);
							if (feedViewObj != null) {
								session.save(feedViewObj);

								System.out
										.println("22222222222222222222222222friendUserId-------------"
												+ friendUserId);
								System.out
										.println("22222222222222222222222222friendUserId-------------"
												+ feedViewObj.getFeedId());
								System.out
										.println("22222222222222222222222222friendUserId-------------"
												+ feedViewObj.getFeedId()
														.getFeedId());
								UserMasterTblVo fromUserObj = new UserMasterTblVo();
								CommonMobiDao commonHbm = new CommonMobiDaoService();
								fromUserObj = commonHbm.getProfileDetails(rid);

								NotificationDao notificationHbm = new NotificationDaoServices();
								UserMasterTblVo userMasteriterObj;
								userMasteriterObj = commonHbm
										.getProfileDetails(friendUserId);
								AdditionalDataDao additionalDatafunc = new AdditionalDataDaoServices();
								String additionaldata = additionalDatafunc
										.formAdditionalDataForFeedTbl(feedViewObj
												.getFeedId());
								System.out
										.println("formAdditionalDataForFeedTbl------------------"
												+ additionaldata);
								String notifdesc = "";
								if (feedViewObj.getFeedId().getFeedType() == 1
										|| feedViewObj.getFeedId()
												.getFeedType() == 2
										|| feedViewObj.getFeedId()
												.getFeedType() == 7
										|| feedViewObj.getFeedId()
												.getFeedType() == 8
										|| feedViewObj.getFeedId()
												.getFeedType() == 9
										|| feedViewObj.getFeedId()
												.getFeedType() == 11
										|| feedViewObj.getFeedId()
												.getFeedType() == 12) {
									notifdesc = "You have tagged a "
											+ getText("notification.reflg."
													+ feedViewObj.getFeedId()
															.getFeedType()
													+ ".desc")
											+ " by "
											+ Commonutility
													.toCheckNullEmpty(fromUserObj
															.getFirstName())
											+ Commonutility
													.toCheckNullEmpty(fromUserObj
															.getLastName());
								}
								notificationHbm.insertnewNotificationDetails(
										userMasteriterObj, notifdesc, 8, 1,
										feedId, fromUserObj, additionaldata);

								log.logMessage("Feed share friend view id="
										+ feedViewObj.getUniqId(), "info",
										FeedDAOService.class);
							} else {
								commitFlg = false;
								break;
							}
						}
					}
					tx.commit();
				}
				log.logMessage("End ::: Feed share friendsTBL viewId::",
						"info", FeedDAOService.class);
			} else {
				log.logMessage("Feed view insert start privacy flg == 1,3",
						"info", FeedDAOService.class);
				if (feedPrivacy == 1 || feedPrivacy == 2 || feedPrivacy == 3
						|| feedPrivacy == 4) {
					log.logMessage("End Self share insert for privacy flg : "
							+ feedPrivacy, "info", FeedDAOService.class);
					if (feedPrivacy == 1 || feedPrivacy == 4) {
						feedViewObj = feedInsertViewDataPack(rid, feedId, rid,
								socityId, isShareflg, feedPrivacy, parentViewId);
					} else {
						int feedView = -2;
						if (feedPrivacy == 2) {
							feedView = -1;
						} else if (feedPrivacy == 3) {
							feedView = -2;
						}
						feedViewObj = feedInsertViewDataPack(feedView, feedId,
								rid, socityId, isShareflg, feedPrivacy,
								parentViewId);
					}
					if (feedViewObj != null) {
						session.save(feedViewObj);
						tx.commit();
						log.logMessage("Feed share Self insert view id="
								+ feedViewObj.getUniqId(), "info",
								FeedDAOService.class);
					} else {
						commitFlg = false;
					}
					log.logMessage("End Self insert for share privacy flg : "
							+ feedPrivacy, "info", FeedDAOService.class);

				}
			}
			if (commitFlg) {
				// FeedsTblVO feedObj = new FeedsTblVO();// feedId
				// feedObj.setIsShared(1);
				// feedObj.setFeedId(feedId);
				// System.out.println("------ sharre feedId:" + feedId);
				// commitFlg = updateFeed(feedObj);
				// System.out.println("7777777");
			}
			if (commitFlg) {
				// tx.commit();
				retValue = true;
			} else {
				retValue = false;
			}
			System.out.println("-------:: ppp ");
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception found in commentPost: " + ex, "error",
					FeedDAOService.class);
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
		return retValue;
	}

	@Override
	public boolean updateFeed(FeedsTblVO feedInsertObj) {
		Session session = HibernateUtil.getSession();
		boolean result = false;
		Transaction tx = null;
		Log log = new Log();
		Query qy = null;
		try {
			tx = session.beginTransaction();
			String updateQuery = "";
			// int feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
			int feedId = feedInsertObj.getFeedId();
			System.out.println("=====feedId==" + feedId);
			if (Commonutility.checkempty(feedInsertObj.getFeedMsg())) {
				updateQuery += "feedMsg=:FEEDMSG,";
			}
			System.out.println("----1----");
			if (Commonutility.checkIntnull(feedInsertObj.getFeedPrivacyFlg())) {
				if (feedInsertObj.getFeedPrivacyFlg() != 0) {
					updateQuery += "feedPrivacyFlg=:PRIVACYFLG,";
				}
			}

			System.out.println("---2-----");
			if (Commonutility.checkempty(feedInsertObj.getUrlsThumbImg())) {
				updateQuery += "urlsThumbImg=:URLTHUMB,";
			}
			System.out.println("----3----");
			if (Commonutility.checkempty(feedInsertObj.getUrlsTitle())) {
				updateQuery += "urlsTitle=:URLTITLE,";
			}
			System.out.println("-----4---");
			if (Commonutility.checkempty(feedInsertObj.getUrlsPageurl())) {
				updateQuery += "urlsPageurl=:PAGEURL,";
			}
			System.out.println("----5----");
			if (Commonutility.checkempty(feedInsertObj.getFeedDesc())) {
				updateQuery += "feedDesc=:FEEDDESC,";
			}
			System.out.println("----6----");
			if (Commonutility.checkempty(feedInsertObj.getFeedStitle())) {
				updateQuery += "feedStitle=:FEEDSTITLE,";
			}
			System.out.println("------7--");
			if (Commonutility.checkempty(feedInsertObj.getFeedTitle())) {
				updateQuery += "feedTitle=:FEEDTITLE,";
			}
			System.out.println("-----8---" + feedInsertObj.getAmount());
			if (feedInsertObj != null && feedInsertObj.getAmount() != null
					&& feedInsertObj.getAmount() != 0) {
				updateQuery += "amount=:AMOUNT,";
			}
			System.out.println("---9-----");
			if (Commonutility.checkempty(feedInsertObj.getFeedCategory())) {
				updateQuery += "feedCategory=:FEEDCATEGORY,";
			}
			System.out.println("-----78---");
			if (Commonutility.checkIntnull(feedInsertObj.getIsShared())) {
				updateQuery += "isShared=:ISSHARED,";
			}
			System.out.println("------er--");
			updateQuery = "update FeedsTblVO set " + updateQuery
					+ " modifyDatetime=:MODIFYDATE where feedId=:FEEDID";
			System.out.println("updateQuery:" + updateQuery);
			qy = session.createQuery(updateQuery);
			qy.setInteger("FEEDID", feedInsertObj.getFeedId());
			if (Commonutility.checkempty(feedInsertObj.getFeedMsg())) {
				qy.setString("FEEDMSG", feedInsertObj.getFeedMsg());
			}
			if (Commonutility.checkIntnull(feedInsertObj.getFeedPrivacyFlg())) {
				if (feedInsertObj.getFeedPrivacyFlg() != 0) {
					qy.setInteger("PRIVACYFLG",
							feedInsertObj.getFeedPrivacyFlg());
				}
			}

			if (Commonutility.checkempty(feedInsertObj.getUrlsThumbImg())) {
				qy.setString("URLTHUMB", feedInsertObj.getUrlsThumbImg());
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsTitle())) {
				qy.setString("URLTITLE", feedInsertObj.getUrlsTitle());
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsPageurl())) {
				qy.setString("PAGEURL", feedInsertObj.getUrlsPageurl());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedDesc())) {
				qy.setString("FEEDDESC", feedInsertObj.getFeedDesc());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedStitle())) {
				qy.setString("FEEDSTITLE", feedInsertObj.getFeedStitle());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedTitle())) {
				qy.setString("FEEDTITLE", feedInsertObj.getFeedTitle());
			}
			if (feedInsertObj != null && feedInsertObj.getAmount() != null
					&& feedInsertObj.getAmount() != 0) {
				qy.setFloat("AMOUNT", feedInsertObj.getAmount());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedCategory())) {
				qy.setString("FEEDCATEGORY", feedInsertObj.getFeedCategory());
			}
			if (Commonutility.checkIntnull(feedInsertObj.getIsShared())) {
				qy.setInteger("ISSHARED", feedInsertObj.getIsShared());
			}
			qy.setTimestamp("MODIFYDATE",
					Commonutility.enteyUpdateInsertDateTime());
			System.out.println("------::entryyyrtyyr");
			qy.executeUpdate();
			System.out.println("------");
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("Exception found in updateFeed : " + ex, "error",
					FeedDAOService.class);
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
		return result;
	}

	@Override
	public Object[] sharedfeedDetails(int rid, int feedId, int feedViewId) {
		Log log = new Log();
		List<Object[]> feedListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into bookingList ", "info",
					FeedDAOService.class);
			String sqlQuery = "select  feed_id,feed_typ,attname,urls_thumb_img,urls_title,urls_pageurl,feed_category,feed_title,feed_stitle, feed_desc, "
					+ "feed_time,amount,post_as,feed_privacy_flg,entry_by,fname,image_name_mobile,feed_location,feed_msg, is_myfeed,is_shared, "
					+ "originator_name,originator_id,like_unlike_flg,like_count,share_count,comment_count, feed_typ_id,share_entryby,feed_view_id "
					+ "from  (select  feed.entry_datetime feed_entry_datetime, case when timeview.usr_id=-2 and "
					+ "(select count(*) from mvp_network_tbl nwt where (nwt.PARENT_USR_ID=timeview.entry_by and  nwt.CONNECTED_USR_ID=49) or "
					+ "( nwt.PARENT_USR_ID=49 and  nwt.CONNECTED_USR_ID=timeview.entry_by))>=1 then 1 when timeview.usr_id!=-2 then 1 else 0 end as flg, feed.feed_id feed_id, feed.feed_typ feed_typ, "
					+ "group_concat(concat(ifnull(feedatt.uniq_id,''),'<!_!>',ifnull(feedatt.attach_name,''),'<!_!>',ifnull(feedatt.thump_image,''),'<!_!>',ifnull(feedatt.file_type,'')) separator '<SP>') attname, "
					+ "feed.urls_thumb_img urls_thumb_img,  feed.urls_title urls_title, feed.urls_pageurl urls_pageurl,  feed.feed_category feed_category, feed.feed_title feed_title,  feed.feed_stitle feed_stitle,  feed.feed_desc feed_desc, feed.feed_time feed_time,  feed.amount amount,  feed.post_as post_as, feed.feed_privacy_flg feed_privacy_flg,  feed.entry_by entry_by,  usr.fname fname, usr.image_name_mobile image_name_mobile, feed.feed_location feed_location,  feed.feed_msg feed_msg,  feed.is_myfeed is_myfeed,  timeview.is_shared is_shared,  feed.originator_name originator_name, feed.originator_id originator_id,  feedlks.like_unlike_flg like_unlike_flg,  feed.like_count like_count,  feed.share_count share_count,  feed.comment_count comment_count,  feed.feed_typ_id feed_typ_id, timeview.entry_by  share_entryby, timeview.uniq_id feed_view_id "
					+ "from mvp_timeline_feed feed inner join mvp_timeline_view_tbl timeview on feed.feed_id = timeview.feed_id and feed.feed_status=1 and feed.society_id=timeview.society_id left  join society_mst_tbl scty on scty.society_id=feed.society_id left join mvp_feed_attch_tbl feedatt on feed.feed_id =feedatt.feed_id left join  mvp_feed_likes feedlks on feed.feed_id = feedlks.feed_id left join  usr_reg_tbl usr  on feed.usr_id = usr.usr_id  "
					+ "where  (timeview.usr_id =49 or timeview.usr_id=-1 or   timeview.usr_id=-2 ) and scty.activation_key='YPKHMJSZ80K2T2ZWZB87CPG1L' and feed.entry_datetime<=str_to_date('2016-11-18 13:16:21','%Y-%m-%d %H:%i:%S') "
					+ "group by feed.feed_id ) tt where flg=1 order by feed_entry_datetime desc limit 0,10";
			System.out.println("Feed List New sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("feed_id", Hibernate.INTEGER)
					.addScalar("feed_typ", Hibernate.INTEGER)
					.addScalar("attname", Hibernate.TEXT)
					.addScalar("urls_thumb_img", Hibernate.TEXT)
					.addScalar("urls_title", Hibernate.TEXT)
					.addScalar("urls_pageurl", Hibernate.TEXT)
					.addScalar("feed_category", Hibernate.TEXT)
					.addScalar("feed_title", Hibernate.TEXT)
					.addScalar("feed_stitle", Hibernate.TEXT)
					.addScalar("feed_desc", Hibernate.TEXT)
					.addScalar("feed_time", Hibernate.TIMESTAMP)
					.addScalar("amount", Hibernate.FLOAT)
					.addScalar("post_as", Hibernate.TEXT)
					.addScalar("feed_privacy_flg", Hibernate.INTEGER)
					.addScalar("entry_by", Hibernate.INTEGER)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_mobile", Hibernate.TEXT)
					.addScalar("feed_location", Hibernate.TEXT)
					.addScalar("feed_msg", Hibernate.TEXT)
					.addScalar("is_myfeed", Hibernate.INTEGER)
					.addScalar("is_shared", Hibernate.INTEGER)
					.addScalar("originator_name", Hibernate.TEXT)
					.addScalar("originator_id", Hibernate.INTEGER)
					.addScalar("like_unlike_flg", Hibernate.INTEGER)
					.addScalar("like_count", Hibernate.LONG)
					.addScalar("share_count", Hibernate.LONG)
					.addScalar("comment_count", Hibernate.LONG)
					.addScalar("feed_typ_id", Hibernate.INTEGER)
					.addScalar("share_entryby", Hibernate.INTEGER)
					.addScalar("feed_view_id", Hibernate.INTEGER);
			feedListObj = queryObj.list();

			log.logMessage(
					"Enter into feedList size had:" + feedListObj.size(),
					"info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedList :" + ex, "error",
					FeedDAOService.class);
			feedListObj = null;
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
		return null;
	}

	@Override
	public boolean shareFeedCountUpdate(int feedId) {
		boolean result = false;
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println("----- share upate");
			tx = session.beginTransaction();
			String query = "update  mvp_timeline_feed set share_count=share_count+1  where feed_id='"
					+ feedId + "'";
			System.out.println("query:::" + query);
			Query qy = session.createSQLQuery(query);
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
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
		return result;
	}

	@Override
	public boolean sharedFeedPrivacyEdit(int rid, int feedId, int feedViewId,
			int feedPrivacy, int societyId, String users) {
		Session session = null;
		Transaction tx = null;
		Log log = new Log();
		Query qy = null;
		boolean result = false;
		boolean editCommitFlg = true;
		try {
			System.out.println("=====sharedFeedPrivacyEdit== feedViewId:"
					+ feedViewId);
			System.out.println("feed edit start");
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			log.logMessage("share feed View tbl insert Start ", "info",
					FeedDAOService.class);
			if (feedId != 0) {
				String sqlQuery = "";
				if (feedPrivacy == 4) {
					sqlQuery = "delete FeedsViewTblVO where feedId.feedId=:FEEDID and entryBy=:ENTRYBY";
				} else {
					// "update FeedCommentTblVO set status=:STATUS, modifyDatetime=:MODY_DATETIME where commentId=:COMMENTID and feedId=:FEEDID and usrId=:USERID");
					sqlQuery = "update FeedsViewTblVO set sharedPrivacyFlg=:SHAREDPRIVACYFLG,usrId=:USRID where uniqId=:UNIQID and entryBy=:ENTRYBY";
				}
				qy = session.createQuery(sqlQuery);
				if (feedPrivacy == 4) {
					qy.setInteger("FEEDID", feedId);
					qy.setInteger("ENTRYBY", rid);
				} else {
					qy.setInteger("SHAREDPRIVACYFLG", feedPrivacy);
					qy.setInteger("UNIQID", feedViewId);
					qy.setInteger("ENTRYBY", rid);
					int userid = -2;
					if (feedPrivacy == 2) {
						userid = -1;
					} else if (feedPrivacy == 3) {
						userid = -2;
					} else {
						userid = rid;
					}
					qy.setInteger("USRID", userid);
				}
				qy.executeUpdate();
				if (feedPrivacy == 4) {
					int isShareflg = 1;
					FeedsViewTblVO feedviewObj = new FeedsViewTblVO();
					feedviewObj = feedInsertViewDataPack(rid, feedId, rid,
							societyId, isShareflg, feedPrivacy, 0);
					session.save(feedviewObj);
					System.out.println(editCommitFlg);
					if (Commonutility.checkempty(users)) {
						if (editCommitFlg) {
							if (!users.contains(",")) {
								users += ",";
							}
							if (users.contains(",")) {
								String[] viewIdsArry = users.split(",");
								log.logMessage(
										"share feed privacy edit viewid length:"
												+ viewIdsArry.length, "info",
										FeedDAOService.class);
								for (int i = 0; i < viewIdsArry.length; i++) {
									int viewId = 0;
									try {
										if (Commonutility
												.checkempty(viewIdsArry[i])
												&& Commonutility
														.toCheckisNumeric(viewIdsArry[i])) {
											log.logMessage(
													"Enter into share feed privacy edit view tbl insert id:"
															+ viewIdsArry[i],
													"info",
													FeedDAOService.class);
											viewId = Commonutility
													.stringToInteger(viewIdsArry[i]);
											feedviewObj = feedInsertViewDataPack(
													viewId, feedId, rid,
													societyId, isShareflg,
													feedPrivacy, 0);
											session.save(feedviewObj);
										}
									} catch (Exception ex) {
										editCommitFlg = false;
										log.logMessage(
												"Exception found in share feed privacy edit viewids insert:"
														+ ex, "error",
												FeedDAOService.class);
									}
								}
							}
						}
					}
				}

			} else {
				editCommitFlg = false;
			}
			log.logMessage(
					"share feed privacy edit View tbl insert End editCommitFlg:"
							+ editCommitFlg, "info", FeedDAOService.class);
			// ###### view tbl insert end ######
			if (editCommitFlg) {
				tx.commit();
			} else {
				if (tx != null) {
					tx.rollback();
				}
				result = false;
			}
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = false;
			log.logMessage("share feed privacy edit Exception found in  : "
					+ ex, "error", FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			log = null;
			qy = null;
		}
		return result;
	}

	@Override
	public List<Object[]> feedDetailsProc(int rid, String socityKey,
			int feedId, String search) {
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Object[] retObj = null;
		List<Object[]> feedListObj = new ArrayList<Object[]>();
		Transaction tx = null;
		try {
			if (search != null && search.length() > 0) {
			} else {
				search = "";
			}
			log.logMessage("Enter into feedListTotalCountGet ", "info",
					FeedDAOService.class);
			String sqlQuery = "call PROC_FEEDTIMELINE_VIEW(49,'YPKHMJSZ80K2T2ZWZB87CPG1L','2016-11-18 13:16:21')";
			System.out.println("CALL PROC_FEED_TIMELINE_VIEW("
					+ Commonutility.intToString(feedId) + "," + rid + ",'"
					+ socityKey + "','','','','','','',2,'" + search + "')");
			sqlQuery = "CALL PROC_FEED_TIMELINE_VIEW(:FEEDID,:USERID,:SOCITYKEY,:TIMESTAMP,:FEEDTYPE,:FEEDCATEGORY,:PRIVACYFLAG,:STARTLIMIT,:ENDLIMIT,:STATUSFLG,:SEARCH)";
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("feed_id", Hibernate.INTEGER)
					.addScalar("feed_typ", Hibernate.INTEGER)
					.addScalar("attname", Hibernate.TEXT)
					.addScalar("urls_thumb_img", Hibernate.TEXT)
					.addScalar("urls_title", Hibernate.TEXT)
					.addScalar("urls_pageurl", Hibernate.TEXT)
					.addScalar("feed_category", Hibernate.TEXT)
					.addScalar("feed_title", Hibernate.TEXT)
					.addScalar("feed_stitle", Hibernate.TEXT)
					.addScalar("feed_desc", Hibernate.TEXT)
					.addScalar("feed_time", Hibernate.TIMESTAMP)
					.addScalar("amount", Hibernate.FLOAT)
					.addScalar("post_as", Hibernate.TEXT)
					.addScalar("feed_privacy_flg", Hibernate.INTEGER)
					.addScalar("entry_by", Hibernate.INTEGER)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_mobile", Hibernate.TEXT)
					.addScalar("feed_location", Hibernate.TEXT)
					.addScalar("feed_msg", Hibernate.TEXT)
					.addScalar("is_myfeed", Hibernate.INTEGER)
					.addScalar("is_shared", Hibernate.INTEGER)
					.addScalar("originator_name", Hibernate.TEXT)
					.addScalar("originator_id", Hibernate.INTEGER)
					.addScalar("like_unlike_flg", Hibernate.INTEGER)
					.addScalar("like_count", Hibernate.LONG)
					.addScalar("share_count", Hibernate.LONG)
					.addScalar("comment_count", Hibernate.LONG)
					.addScalar("feed_typ_id", Hibernate.INTEGER)
					.addScalar("share_entryby", Hibernate.INTEGER)
					.addScalar("feedviewid", Hibernate.INTEGER)
					.addScalar("custom_friends", Hibernate.TEXT)
					.addScalar("social_picture", Hibernate.TEXT)
					.addScalar("additional_data", Hibernate.TEXT);
			System.out.println("------e:" + feedId);
			queryObj.setString("FEEDID", Commonutility.intToString(feedId));
			System.out.println("---e---e");
			queryObj.setInteger("USERID", rid);
			queryObj.setString("SOCITYKEY", socityKey);
			queryObj.setString("TIMESTAMP", "");
			queryObj.setString("FEEDTYPE", "");
			queryObj.setString("PRIVACYFLAG", "");
			queryObj.setString("FEEDCATEGORY", "");
			queryObj.setString("STARTLIMIT", "");
			queryObj.setString("ENDLIMIT", "");
			queryObj.setInteger("STATUSFLG", 2);
			queryObj.setString("SEARCH", search);
			System.out.println("status flag=------------" + 2);
			// retCount =((Number) queryObj.uniqueResult()).intValue();
			System.out.println("7777777");
			// retObj = (Object[]) queryObj.uniqueResult();
			System.out.println("0000---");
			feedListObj = queryObj.list();
			// int fg = (int)queryObj.uniqueResult();
			// System.out.println("#####:" + fg);
			System.out.println("0000-rt--");
			log.logMessage("Enter into feedDetailsProc totoal size:"
					+ feedListObj.size(), "info", FeedDAOService.class);
			System.out.println(Arrays.toString(feedListObj.toArray()));

			// System.out.println(queryObj.toString());
			log.logMessage("Enter into feedDetailsProc totoal size:"
					+ feedListObj.size(), "info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedDetailsProc :" + ex,
					"error", FeedDAOService.class);
			retObj = null;
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
		return feedListObj;

	}

	@Override
	public int getFeedViewId(int rid, int socityID, int feedId,
			int useridColumn, int isShared) {
		Session session = null;
		Transaction tx = null;
		int retValue = 0;
		Log log = new Log();
		FeedsViewTblVO FeedsViewTblVO = new FeedsViewTblVO();
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String query = "from FeedsViewTblVO where entryBy=" + rid
					+ " and usrId=" + useridColumn + " and feedId.feedId="
					+ feedId + " and societyId.societyId=" + socityID
					+ " and isShared=" + isShared;
			System.out.println("--qq--");
			Query qy = session.createQuery(query);
			System.out.println("---w-");
			FeedsViewTblVO = (FeedsViewTblVO) qy.uniqueResult();
			if (FeedsViewTblVO != null) {
				retValue = FeedsViewTblVO.getUniqId();
			} else {
				retValue = 0;
			}
			System.out.println("-----");
			// tx.commit();
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

	@Override
	public List<Object[]> feedListProcDetails(int rid, String socityKey,
			String timeStamp, String feedType, int postBy, String feedCategory,
			int startLimit, int endLimit, String search) {
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Object[] retObj = null;
		List<Object[]> feedListObj = new ArrayList<Object[]>();
		Transaction tx = null;
		try {
			if (search != null && search.length() > 0) {
			} else {
				search = "";
			}
			log.logMessage("Enter into feedListProcDetails ", "info",
					FeedDAOService.class);
			log.logMessage("Enter into feedListTotalCountGet ", "info",
					FeedDAOService.class);
			log.logMessage("rid :" + rid, "info", FeedDAOService.class);
			log.logMessage("socityKey :" + socityKey, "info",
					FeedDAOService.class);
			log.logMessage("feedType :" + feedType, "info",
					FeedDAOService.class);
			log.logMessage("postBy :" + postBy, "info", FeedDAOService.class);
			log.logMessage("feedCategory :" + feedCategory, "info",
					FeedDAOService.class);
			log.logMessage("timeStamp :" + timeStamp, "info",
					FeedDAOService.class);
			log.logMessage("startLimit :" + startLimit, "info",
					FeedDAOService.class);
			log.logMessage("endLimit :" + endLimit, "info",
					FeedDAOService.class);
			String sqlQuery = "call PROC_FEED_TIMELINE_VIEW(49,'YPKHMJSZ80K2T2ZWZB87CPG1L','2016-11-18 13:16:21')";
			sqlQuery = "CALL PROC_FEED_TIMELINE_VIEW(:FEEDID,:USERID,:SOCITYKEY,:TIMESTAMP,:FEEDTYPE,:FEEDCATEGORY,:PRIVACYFLAG,:STARTLIMIT,:ENDLIMIT,:STATUSFLG,:SEARCH)";
			System.out.println("sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("feed_id", Hibernate.INTEGER)
					.addScalar("feed_typ", Hibernate.INTEGER)
					.addScalar("attname", Hibernate.TEXT)
					.addScalar("urls_thumb_img", Hibernate.TEXT)
					.addScalar("urls_title", Hibernate.TEXT)
					.addScalar("urls_pageurl", Hibernate.TEXT)
					.addScalar("feed_category", Hibernate.TEXT)
					.addScalar("feed_title", Hibernate.TEXT)
					.addScalar("feed_stitle", Hibernate.TEXT)
					.addScalar("feed_desc", Hibernate.TEXT)
					.addScalar("feed_time", Hibernate.TIMESTAMP)
					.addScalar("amount", Hibernate.FLOAT)
					.addScalar("post_as", Hibernate.TEXT)
					.addScalar("feed_privacy_flg", Hibernate.INTEGER)
					.addScalar("entry_by", Hibernate.INTEGER)
					.addScalar("fname", Hibernate.TEXT)
					.addScalar("image_name_mobile", Hibernate.TEXT)
					.addScalar("feed_location", Hibernate.TEXT)
					.addScalar("feed_msg", Hibernate.TEXT)
					.addScalar("is_myfeed", Hibernate.INTEGER)
					.addScalar("is_shared", Hibernate.INTEGER)
					.addScalar("originator_name", Hibernate.TEXT)
					.addScalar("originator_id", Hibernate.INTEGER)
					.addScalar("like_unlike_flg", Hibernate.INTEGER)
					.addScalar("like_count", Hibernate.LONG)
					.addScalar("share_count", Hibernate.LONG)
					.addScalar("comment_count", Hibernate.LONG)
					.addScalar("feed_typ_id", Hibernate.INTEGER)
					.addScalar("share_entryby", Hibernate.INTEGER)
					.addScalar("feedviewid", Hibernate.INTEGER)
					.addScalar("custom_friends", Hibernate.TEXT)
					.addScalar("social_picture", Hibernate.TEXT)
					.addScalar("additional_data", Hibernate.TEXT);
			// System.out.println("------e:" + feedId);
			queryObj.setString("FEEDID", "");
			queryObj.setInteger("USERID", rid);
			queryObj.setString("SOCITYKEY", socityKey);
			queryObj.setString("TIMESTAMP", timeStamp);
			if (Commonutility.checkempty(feedType)) {
				queryObj.setString("FEEDTYPE", feedType);
			} else {
				queryObj.setString("FEEDTYPE", "");
			}
			if (Commonutility.checkIntempty(postBy)) {
				queryObj.setString("PRIVACYFLAG",
						Commonutility.intToString(postBy));
			} else {
				queryObj.setString("PRIVACYFLAG", "");
			}
			if (Commonutility.checkempty(feedCategory)) {
				queryObj.setString("FEEDCATEGORY", feedCategory);
			} else {
				queryObj.setString("FEEDCATEGORY", "");
			}
			System.out.println("----- hello limit:" + startLimit);
			System.out.println("----- hello limit::::"
					+ Commonutility.intToString(startLimit));
			if (Commonutility.checkIntnull(startLimit)) {
				System.out.println("-----limit");
				String startLm = Integer.toString(startLimit);
				queryObj.setString("STARTLIMIT", startLm);
			} else {
				System.out.println("limt else");
				queryObj.setString("STARTLIMIT", "0");
			}
			// queryObj.setString("STARTLIMIT", "0");
			queryObj.setString("ENDLIMIT", "10");
			queryObj.setInteger("STATUSFLG", 0);
			queryObj.setString("SEARCH", search);
			System.out.println("status flag=------------" + 0);
			// retCount =((Number) queryObj.uniqueResult()).intValue();
			System.out.println("7777777");
			// retObj = (Object[]) queryObj.uniqueResult();
			System.out.println("0000---");
			feedListObj = queryObj.list();
			// int fg = (int)queryObj.uniqueResult();
			// System.out.println("#####:" + fg);
			System.out.println("0000-rt--" + feedListObj.size());
			log.logMessage("Enter into feedDetailsProc totoal size:"
					+ feedListObj.size(), "info", FeedDAOService.class);
			System.out.println(Arrays.toString(feedListObj.toArray()));

			// System.out.println(queryObj.toString());
			log.logMessage("Enter into feedDetailsProc totoal size:"
					+ feedListObj.size(), "info", FeedDAOService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in feedDetailsProc :" + ex,
					"error", FeedDAOService.class);
			retObj = null;
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
		return feedListObj;
	}

	@Override
	public int getIsFeedLikedOrNot(int feedId, int userId) {
		// FeedLikeUnlikeTBLVO
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locLiked = -1;
		try {
			log.logMessage("Enter into  CommentDAOService", "info",
					FeedDAOService.class);
			FeedLikeUnlikeTBLVO feedliked = new FeedLikeUnlikeTBLVO();
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Query qury = session
					.createQuery(" from FeedLikeUnlikeTBLVO where usrId=:USRID and feedId=:FEEDID");
			qury.setInteger("USRID", userId);
			qury.setInteger("FEEDID", feedId);
			feedliked = (FeedLikeUnlikeTBLVO) qury.uniqueResult();
			if (feedliked != null) {
				if (feedliked.getUsrId() != 0) {
					locLiked = feedliked.getLikeId();
				}
			}
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception found in commentPost: " + ex, "error",
					FeedDAOService.class);
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
		return locLiked;
	}

	@Override
	public boolean feedLikeUnlikeUpdate(int likeID, int likeFlg) {
		boolean result = false;
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("enter into update feedLikeUnlikeDelete", "info",
					FeedDAOService.class);
			tx = session.beginTransaction();
			String query = "update FeedLikeUnlikeTBLVO set likeUnlikeFlg=:LIKEUNLIKEFLG where likeId=:LIKEID";
			log.logMessage("like update query:" + query, "info",
					FeedDAOService.class);
			Query qy = session.createSQLQuery(query);
			qy.setInteger("LIKEID", likeID);
			qy.setInteger("LIKEUNLIKEFLG", likeFlg);
			System.out.println("--------");
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("Exception found in feedLikeUnlikeDelete : " + ex,
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
		return result;
	}

	@Override
	public boolean feedLikeUnlikeUpdateAg(int likeID, int likeFlg) {
		Session session = HibernateUtil.getSession();
		boolean result = false;
		Transaction tx = null;
		Log log = new Log();
		try {
			System.out.println("-----1-------");
			tx = session.beginTransaction();
			Query qy = session
					.createQuery("update FeedLikeUnlikeTBLVO set likeUnlikeFlg=:LIKEFLG where likeId=:LIKEID");
			System.out.println("------2------");
			qy.setInteger("LIKEID", likeID);
			qy.setInteger("LIKEFLG", likeFlg);
			System.out.println("-------3-----");
			qy.executeUpdate();
			tx.commit();
			System.out.println("------4------");
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
			log.logMessage("Exception found in feedLikeUnlikeUpdateAg : " + ex,
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
		return result;
	}

	@Override
	public FeedsViewTblVO getFeedViewDetails(int retFeedViewId) {
		// TODO Auto-generated method stub
		FeedsViewTblVO feedViewMst = new FeedsViewTblVO();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from FeedsViewTblVO where status=:STATUS and uniqId=:UNIQ_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setInteger("UNIQ_ID", retFeedViewId);
			feedViewMst = (FeedsViewTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFeedDetails======" + ex);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return feedViewMst;

	}

	@Override
	public boolean commentFeedCountUpdate(int feedId) {
		// TODO Auto-generated method stub
		boolean result = false;
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Date date1;
		CommonUtils comutil = new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println("----- share upate");
			tx = session.beginTransaction();
			String query = "update  mvp_timeline_feed set COMMENT_COUNT=COMMENT_COUNT+1  where feed_id='"
					+ feedId + "'";
			System.out.println("query:::" + query);
			Query qy = session.createSQLQuery(query);
			qy.executeUpdate();
			tx.commit();
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			result = false;
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
		return result;
	}

	@Override
	public List getFeedAttachmentObjByQuery(String query) {
		// TODO Auto-generated method stub
		List<FeedattchTblVO> feedAttachList = new ArrayList<FeedattchTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(query);
			feedAttachList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getCarPoolingList======" + ex);
			log.logMessage("CarPoolDaoServices Exception getCarPoolingList : "
					+ ex, "error", CarPoolDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return feedAttachList;
	}

	@Override
	public List getFeedSearchDetails(String query) {
		// TODO Auto-generated method stub
		List<FeedsTblVO> feedMst = new ArrayList<FeedsTblVO>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(query);
			feedMst = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFeedDetails======" + ex);
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
	public int getFeedIsLikedFlg(int feedId, int userId) {
		// FeedLikeUnlikeTBLVO
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locLiked = 0;
		try {
			log.logMessage("Enter into  CommentDAOService", "info",
					FeedDAOService.class);
			FeedLikeUnlikeTBLVO feedliked = new FeedLikeUnlikeTBLVO();
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			System.out.println("feedId-----" + feedId);
			System.out.println("userId-----" + userId);
			Query qury = session
					.createQuery(" from FeedLikeUnlikeTBLVO where usrId=:USRID and feedId=:FEEDID");
			qury.setInteger("USRID", userId);
			qury.setInteger("FEEDID", feedId);
			feedliked = (FeedLikeUnlikeTBLVO) qury.uniqueResult();
			if (feedliked != null) {
				if (feedliked.getUsrId() != 0) {
					locLiked = feedliked.getLikeUnlikeFlg();
					if (locLiked == 0 || locLiked == 2) {
						locLiked = 0;
					}
				}
			}
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception found in commentPost: " + ex, "error",
					FeedDAOService.class);
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
		return locLiked;
	}

	@Override
	public int feedInsertPrivacyflag(String users, FeedsTblVO feedInsertObj,
			List<File> fileUpload, List<String> fileUploadContentType,
			List<String> fileUploadFileName, int privacyflag, String userArr) {
		// TODO Auto-generated method stub
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int locTimelineFeedid = -1;
		HashMap<String, File> imageFileMap = new HashMap<String, File>();
		HashMap<String, File> videoFileMap = new HashMap<String, File>();
		FunctionUtility commonutil = new FunctionUtilityServices();
		CommonMobiDao commonServ = new CommonMobiDaoService();
		mobiCommon mobComm = new mobiCommon();
		try {
			log.logMessage("Ente into feedd insert", "info",
					FeedDAOService.class);
			boolean commitFlg = true;
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			log.logMessage("Ente into feedd privacy flg", "info",
					FeedDAOService.class);
			if (feedInsertObj.getUsrId() != null
					|| feedInsertObj.getUsrId().getUserId() != 0) {
				PrivacyInfoTblVO privacyObj = new PrivacyInfoTblVO();
				/*
				 * privacyObj = commonServ.getDefaultPrivacyFlg(feedInsertObj
				 * .getUsrId().getUserId());
				 */
				
				
				privacyObj.setPrivacyFlg(privacyflag);
				
				if (privacyObj != null) {
					feedInsertObj.setFeedPrivacyFlg(privacyObj.getPrivacyFlg());
					if (privacyObj.getPrivacyFlg() == 4) {
					/*	if (Commonutility.checkempty(users)) {
						} else {*/
							userArr=userArr.substring(1,userArr.length()-1);
							System.out.println("userArr--------"+userArr);
							privacyObj.setUsrIds(userArr);
							users = privacyObj.getUsrIds();
						//}
					}
				} else {
					feedInsertObj
							.setFeedPrivacyFlg(Commonutility
									.stringToInteger(getText("default.privacy.category")));
					log.logMessage("no default privacy flg", "info",
							FeedDAOService.class);
				}

			}
			if (feedInsertObj != null) {
				long dafltVal = 0;
				feedInsertObj.setLikeCount(dafltVal);
				feedInsertObj.setShareCount(dafltVal);
				feedInsertObj.setCommentCount(dafltVal);
			}
			feedInsertObj
					.setFeedTime(Commonutility.enteyUpdateInsertDateTime());
			System.out.println("timeeeeeeeeeeeee:"
					+ feedInsertObj.getFeedTime());
			session.save(feedInsertObj);
			locTimelineFeedid = feedInsertObj.getFeedId();
			System.out.println("locTimelineFeedid :::::::" + locTimelineFeedid);
			if (locTimelineFeedid != -1) {
				if (fileUpload != null && fileUpload.size() > 0) {
					System.out.println("=========fileUpload========="
							+ fileUpload.size());
					for (int i = 0; i < fileUpload.size(); i++) {
						File mapFileUpload = fileUpload.get(i);
						System.out.println("==========ss===" + mapFileUpload);
						String mapFileName = fileUploadFileName.get(i);
						System.out.println("==========mapFileName==="
								+ mapFileName);
						String attachFileName = FilenameUtils
								.getBaseName(mapFileName);
						String attachFileExtension = FilenameUtils
								.getExtension(mapFileName);
						String uniqFileName = attachFileName + "_"
								+ commonutil.uniqueNoFromDate() + "."
								+ attachFileExtension;
						System.out.println("======uniqFileName====="
								+ uniqFileName);
						System.out.println("======attachFileExtension====="
								+ attachFileExtension);
						Integer fileType = mobComm
								.getFileExtensionType(attachFileExtension);
						if (fileType == 2) {
							videoFileMap.put(uniqFileName, mapFileUpload);
						} else {
							imageFileMap.put(uniqFileName, mapFileUpload);
						}
					}
					FeedsTblVO feedIdObj = new FeedsTblVO();
					feedIdObj.setFeedId(locTimelineFeedid);
					if (videoFileMap.size() > 0) {
						log.logMessage("Enter into video write feed post",
								"info", FeedDAOService.class);
						String videoPath = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.videopath")
								+ locTimelineFeedid + "/";
						String videoPathThumb = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.video.thumbpath")
								+ locTimelineFeedid + "/";
						log.logMessage(videoPath + " :path: " + videoPathThumb,
								"info", FeedDAOService.class);
						for (Entry<String, File> entry : videoFileMap
								.entrySet()) {
							log.logMessage("Ente  feed attach video filename:"
									+ entry.getKey(), "info",
									FeedDAOService.class);
							log.logMessage("Ente feed attach video filen:"
									+ entry.getValue(), "info",
									FeedDAOService.class);
							String videoName = entry.getKey();
							String videoThumbName = FilenameUtils
									.getBaseName(videoName) + ".jpeg";
							System.out.println("videoThumbName:"
									+ videoThumbName);
							try {
								System.out
										.println("service video file start time-------------"
												+ new Date());
								File destFile = new File(videoPath, videoName);
								FileUtils.copyFile(entry.getValue(), destFile);
								System.out
										.println("service video file start time 2222222222-------------"
												+ new Date());
								if (imageFileMap.containsKey(videoThumbName)) {

									File imageThumb = imageFileMap
											.get(videoThumbName);
									File destThumbFile = new File(
											videoPathThumb, videoThumbName);

									FileUtils.copyFile(imageThumb,
											destThumbFile);
									System.out
											.println("service video file end time 1111-------------"
													+ new Date());
									imageFileMap.remove(videoThumbName);
									System.out
											.println("service video file end time-------------"
													+ new Date());
								} else {
									log.logMessage(
											"######### Thump image not found #########",
											"info", FeedDAOService.class);
								}
							} catch (Exception ex) {
								commitFlg = false;
								log.logMessage(
										"Exception ffound in video file upload : "
												+ ex, "error",
										FeedDAOService.class);
							}
							FeedattchTblVO feedAttchObj = new FeedattchTblVO();
							feedAttchObj.setIvrBnFEED_ID(feedIdObj);
							feedAttchObj.setIvrBnATTACH_NAME(videoName);
							feedAttchObj.setIvrBnTHUMP_IMAGE(videoThumbName);
							feedAttchObj.setIvrBnFILE_TYPE(2);
							feedAttchObj.setIvrBnSTATUS(1);
							feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
									.enteyUpdateInsertDateTime());
							feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
									.enteyUpdateInsertDateTime());
							session.save(feedAttchObj);
						}
						log.logMessage("End video write feed post", "info",
								FeedDAOService.class);
					}
					if (imageFileMap.size() > 0) {
						log.logMessage("Enter into image write feed post",
								"info", FeedDAOService.class);
						String imagePathWeb = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.img.webpath")
								+ locTimelineFeedid + "/";
						String imagePathMobi = getText("external.imagesfldr.path")
								+ getText("external.uploadfile.feed.img.mobilepath")
								+ locTimelineFeedid + "/";
						for (Entry<String, File> entry : imageFileMap
								.entrySet()) {
							log.logMessage("Ente  feed attach image filename:"
									+ entry.getKey(), "info",
									FeedDAOService.class);
							log.logMessage("Ente feed attach image filen:"
									+ entry.getValue(), "info",
									FeedDAOService.class);
							String imageName = entry.getKey();
							String imgBaseName = FilenameUtils
									.getBaseName(imageName);
							String imgExtenName = FilenameUtils
									.getExtension(imgBaseName);
							File imgFilePath = imageFileMap.get(imageName);
							Integer fileType = mobComm
									.getFileExtensionType(imgExtenName);
							FeedattchTblVO feedAttchObjImg = new FeedattchTblVO();
							feedAttchObjImg.setIvrBnFEED_ID(feedIdObj);
							feedAttchObjImg.setIvrBnSTATUS(1);
							feedAttchObjImg.setIvrBnATTACH_NAME(imageName);
							feedAttchObjImg
									.setIvrBnENTRY_DATETIME(Commonutility
											.enteyUpdateInsertDateTime());
							feedAttchObjImg
									.setIvrBnMODIFY_DATETIME(Commonutility
											.enteyUpdateInsertDateTime());
							if (fileType == 1) {
								feedAttchObjImg.setIvrBnFILE_TYPE(1);
								log.logMessage("imageName web :" + imageName,
										"info", FeedDAOService.class);
								// for web, image upload original size
								try {
									File destFileWeb = new File(imagePathWeb,
											imageName);
									// File destFileMobi = new
									// File(imagePathMobi, imageName);
									FileUtils.copyFile(entry.getValue(),
											destFileWeb);
									// FileUtils.copyFile( entry.getValue(),
									// destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage(
											"Exception found in image file upload for web : "
													+ ex, "error",
											FeedDAOService.class);
								}
								// for mobile, compress image upload
								int imgWitdh = 0;
								int imgHeight = 0;
								int imgOriginalWidth = mobiCommon
										.getImageWidth(imgFilePath);
								int imgOriginalHeight = mobiCommon
										.getImageHeight(imgFilePath);
								String imageHeightPro = getText("thump.img.height");
								String imageWidthPro = getText("thump.img.width");
								if (imgOriginalWidth > Commonutility
										.stringToInteger(imageWidthPro)) {
									imgWitdh = Commonutility
											.stringToInteger(imageWidthPro);
								} else {
									imgWitdh = imgOriginalWidth;
								}
								if (imgOriginalHeight > Commonutility
										.stringToInteger(imageHeightPro)) {
									imgHeight = Commonutility
											.stringToInteger(imageHeightPro);
								} else {
									imgHeight = imgOriginalHeight;
								}
								String imgSourcePath = imagePathWeb + imageName;
								String imgName = FilenameUtils
										.getBaseName(imageName);
								String imageFomat = FilenameUtils
										.getExtension(imageName);
								String imageFor = "all";
								log.logMessage("imageName for web compress : "
										+ imageName, "info",
										FeedDAOService.class);
								ImageCompress.toCompressImage(imgSourcePath,
										imagePathMobi, imgName, imageFomat,
										imageFor, imgWitdh, imgHeight);
							} else {
								feedAttchObjImg.setIvrBnFILE_TYPE(3);
								try {
									File destFileWeb = new File(imagePathWeb,
											imageName);
									File destFileMobi = new File(imagePathMobi,
											imageName);
									FileUtils.copyFile(entry.getValue(),
											destFileWeb);
									FileUtils.copyFile(entry.getValue(),
											destFileMobi);
								} catch (Exception ex) {
									commitFlg = false;
									log.logMessage(
											"Exception found in image file upload for type 3 : "
													+ ex, "error",
											FeedDAOService.class);
								}
							}
							session.save(feedAttchObjImg);
						}
						log.logMessage("End image write feed post", "info",
								FeedDAOService.class);
					}
				} else {
					log.logMessage("File upload empty", "info",
							FeedDAOService.class);
				}
				int feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
				int rid = feedInsertObj.getUsrId().getUserId();
				FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
				int sociteyId = feedInsertObj.getSocietyId().getSocietyId();
				int isShareflg = 0;
				if (Commonutility.checkempty(users) && feedPrivacy == 4) {
					log.logMessage("Feed view insert start privacy flg == 3",
							"info", FeedDAOService.class);
					System.out.println("locTimelineFeedid-----------------"
							+ locTimelineFeedid);
					feedViewObj = feedInsertViewDataPack(rid,
							locTimelineFeedid, rid, sociteyId, isShareflg, -1,
							0);
					if (feedViewObj != null) {
						session.save(feedViewObj);
						log.logMessage(
								"Feed post own view id="
										+ feedViewObj.getUniqId(), "info",
								FeedDAOService.class);
					} else {
						commitFlg = false;
					}
					if (commitFlg) {
						int friendUserId = 0;
						if (!users.contains(",")) {
							users += ",";
						}
						String[] userIdArr = users.split(",");
						log.logMessage("Total number of friends id="
								+ userIdArr.length, "info",
								FeedDAOService.class);
						for (int i = 0; i < userIdArr.length; i++) {
							log.logMessage(i
									+ "<==Size ::single friends list viewId:"
									+ userIdArr[i], "info",
									FeedDAOService.class);
							if (Commonutility.checkempty(userIdArr[i])
									&& Commonutility
											.toCheckisNumeric(userIdArr[i])) {
								friendUserId = Commonutility
										.stringToInteger(userIdArr[i]);
								feedViewObj = feedInsertViewDataPack(
										friendUserId, locTimelineFeedid, rid,
										sociteyId, isShareflg, -1, 0);
								if (feedViewObj != null) {
									session.save(feedViewObj);
									System.out.println("0------");
									if (!tx.wasCommitted()) {
										tx.commit();
									}
									// tx.commit();
									System.out.println("--------0");

									NotificationDao notificationHbm = new NotificationDaoServices();
									CommonMobiDao commonHbm = new CommonMobiDaoService();
									UserMasterTblVo userMasteriterObj;
									userMasteriterObj = commonHbm
											.getProfileDetails(friendUserId);
									AdditionalDataDao additionalDatafunc = new AdditionalDataDaoServices();
									System.out
											.println("feedViewObj.getFeedId()------------------------"
													+ feedViewObj.getFeedId()
															.getFeedId());
									FeedDAO feedhbm = new FeedDAOService();
									FeedsTblVO feedobj = new FeedsTblVO();
									feedobj = feedhbm.feedData(feedViewObj
											.getFeedId().getFeedId());
									System.out
											.println("feedViewObj.getFeedType()------------------------"
													+ feedViewObj.getFeedId()
															.getFeedType());
									String additionaldata = additionalDatafunc
											.formAdditionalDataForFeedTbl(feedobj);
									System.out
											.println("formAdditionalDataForFeedTbl------------------"
													+ additionaldata);
									notificationHbm
											.insertnewNotificationDetails(
													userMasteriterObj, "", 8,
													1, feedobj.getFeedId(),
													feedInsertObj.getUsrId(),
													additionaldata);

									log.logMessage("Feed post friend view id="
											+ feedViewObj.getUniqId(), "info",
											FeedDAOService.class);
								} else {
									commitFlg = false;
									break;
								}
							}
						}
					}
					
					if (!tx.wasCommitted()) {
						tx.commit();
					}
					log.logMessage("End ::: Feed insert friendsTBL viewId::",
							"info", FeedDAOService.class);
				} else {
					log.logMessage("Feed view insert start privacy flg == 1,3",
							"info", FeedDAOService.class);
					if (Commonutility.checkIntempty(feedPrivacy)) {
						log.logMessage("End Self insert for privacy flg : "
								+ feedPrivacy, "info", FeedDAOService.class);
						if (feedPrivacy == 1 || feedPrivacy == 4) {
							feedViewObj = feedInsertViewDataPack(rid,
									locTimelineFeedid, rid, sociteyId,
									isShareflg, -1, 0);
						} else {
							int feedView = -2;
							if (feedPrivacy == 2) {
								feedView = -1;
							} else if (feedPrivacy == 3) {
								feedView = -2;
							}
							feedViewObj = feedInsertViewDataPack(feedView,
									locTimelineFeedid, rid, sociteyId,
									isShareflg, -1, 0);
						}
						if (feedViewObj != null) {
							session.save(feedViewObj);
							log.logMessage("Feed post Self insert view id="
									+ feedViewObj.getUniqId(), "info",
									FeedDAOService.class);
							System.out.println("0------");
							tx.commit();
							System.out.println("--------0");
						} else {
							commitFlg = false;
						}
						log.logMessage("End Self insert for privacy flg : "
								+ feedPrivacy, "info", FeedDAOService.class);

					}
				}
			} else {
				commitFlg = false;
			}
			log.logMessage("commitFlg value:" + commitFlg, "info",
					FeedDAOService.class);
			if (commitFlg) {

			} else {
				if (tx != null) {
					tx.rollback();
				}
				if (locTimelineFeedid != -1) {
					feedFileUploadDelete(locTimelineFeedid);
				}
				locTimelineFeedid = -1;
			}

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (locTimelineFeedid != -1) {
				feedFileUploadDelete(locTimelineFeedid);
			}
			log = new Log();
			log.logMessage("Exception found in feed insert : " + e, "error",
					FeedDAOService.class);
			locTimelineFeedid = -1;
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
		return locTimelineFeedid;
	}

	@Override
	public boolean feedEditPrivacyflag(FeedsTblVO feedInsertObj, String users,
			JSONArray removeAttachIdsObj, List<File> fileUpload,
			List<String> fileUploadContentType,
			List<String> fileUploadFileName) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		Log log = new Log();
		Query qy = null;
		boolean result = false;
		boolean editCommitFlg = true;
		HashMap<String, File> imageFileMap = new HashMap<String, File>();
		HashMap<String, File> videoFileMap = new HashMap<String, File>();
		FunctionUtility commonutil = new FunctionUtilityServices();
		mobiCommon mobComm = new mobiCommon();
		try {
			System.out.println("=====feedInsertObj==" + feedInsertObj);
			System.out.println("feed edit start");
			System.out.println("feedInsertObj userid -----------"
					+ feedInsertObj.getUsrId().getUserId());
			System.out.println("feedInsertObj socityid -----------"
					+ feedInsertObj.getSocietyId().getSocietyId());
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String updateQuery = "";
			int feedPrivacy = 0;
			FeedsTblVO getFeedDataObj = new FeedsTblVO();
			System.out
					.println("feedInsertObj.getFeedId() feedid data==============="
							+ feedInsertObj.getFeedId());
			getFeedDataObj = feedData(feedInsertObj.getFeedId());
			System.out
					.println("getFeedDataObj.getFeedType() getFeedType==============="
							+ getFeedDataObj.getFeedType());
			/*if (getFeedDataObj.getFeedType() == 2) {
				System.out
						.println("Enter if part for privacy flag===============");
				feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
			} else {
				System.out
						.println("Enter else part for privacy flag===============");
				PrivacyInfoTblVO privacyObj = new PrivacyInfoTblVO();
				CommonMobiDao commonServNew = new CommonMobiDaoService();
				privacyObj = commonServNew.getDefaultPrivacyFlg(feedInsertObj
						.getUsrId().getUserId());
				
			privacyObj.setPrivacyFlg(privacyflag);
				
				if (privacyObj != null) {
					feedInsertObj.setFeedPrivacyFlg(privacyObj.getPrivacyFlg());
					if (privacyObj.getPrivacyFlg() == 4) {
						userIdArr=userIdArr.substring(1,userIdArr.length()-1);
							System.out.println("userIdArr--------"+userIdArr);
							privacyObj.setUsrIds(userIdArr);
							users = privacyObj.getUsrIds();
					}
				} else {
					feedInsertObj
							.setFeedPrivacyFlg(Commonutility
									.stringToInteger(getText("default.privacy.category")));
					log.logMessage("no default privacy flg", "info",
							FeedDAOService.class);
				}
				
				if (privacyObj != null) {
					feedInsertObj.setFeedPrivacyFlg(privacyObj.getPrivacyFlg());
					feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
					if (privacyObj.getPrivacyFlg() == 4) {
						if (Commonutility.checkempty(users)) {
						} else {
							users = privacyObj.getUsrIds();
						}
					}
				} else {
					feedInsertObj
							.setFeedPrivacyFlg(Commonutility
									.stringToInteger(getText("default.privacy.category")));
					feedPrivacy = feedInsertObj.getFeedPrivacyFlg();
					log.logMessage("no default privacy flg", "info",
							FeedDAOService.class);
				}

			}*/
			int feedId = feedInsertObj.getFeedId();
			System.out.println("=====feedId==" + feedId);
			System.out.println("=====feedPrivacy==" + feedPrivacy);
			if (Commonutility.checkempty(feedInsertObj.getFeedMsg())) {
				updateQuery += "feedMsg=:FEEDMSG,";
			}
			/*if (feedInsertObj != null
					&& feedInsertObj.getFeedPrivacyFlg() != null
					&& feedInsertObj.getFeedPrivacyFlg() != 0) {
				updateQuery += "feedPrivacyFlg=:PRIVACYFLG,";
			}*/
			if (Commonutility.checkempty(feedInsertObj.getUrlsThumbImg())) {
				updateQuery += "urlsThumbImg=:URLTHUMB,";
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsTitle())) {
				updateQuery += "urlsTitle=:URLTITLE,";
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsPageurl())) {
				updateQuery += "urlsPageurl=:PAGEURL,";
			}

			if (Commonutility.checkempty(feedInsertObj.getFeedDesc())) {
				updateQuery += "feedDesc=:FEEDDESC,";
			}

			if (Commonutility.checkempty(feedInsertObj.getFeedStitle())) {
				updateQuery += "feedStitle=:FEEDSTITLE,";
			}

			if (Commonutility.checkempty(feedInsertObj.getFeedTitle())) {
				updateQuery += "feedTitle=:FEEDTITLE,";
			}

			if (feedInsertObj != null && feedInsertObj.getAmount() != null
					&& feedInsertObj.getAmount() != 0) {
				updateQuery += "amount=:AMOUNT,";
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedCategory())) {
				updateQuery += "feedCategory=:FEEDCATEGORY,";
			}
			if (Commonutility.checkempty(feedInsertObj.getAdditionalData())) {
				updateQuery += "additionalData=:ADDITIONALDATA,";
			}

			if (updateQuery.contains(",")) {
				updateQuery = updateQuery
						.substring(0, updateQuery.length() - 1);
			}
			System.out.println("updateQuery::" + updateQuery);
			updateQuery = "update FeedsTblVO set "
					+ updateQuery
					+ ",modifyDatetime=:MODIFYDATE,feedTime=:FEEDTIME where feedId=:FEEDID";

			qy = session.createQuery(updateQuery);
			qy.setInteger("FEEDID", feedInsertObj.getFeedId());
			if (Commonutility.checkempty(feedInsertObj.getFeedMsg())) {
				qy.setString("FEEDMSG", feedInsertObj.getFeedMsg());
			}
			/*if (feedInsertObj.getFeedPrivacyFlg() != 0) {
				System.out.println("------9i8 :"
						+ feedInsertObj.getFeedPrivacyFlg());
				qy.setInteger("PRIVACYFLG", feedInsertObj.getFeedPrivacyFlg());
			}*/
			System.out.println("0000000");
			if (Commonutility.checkempty(feedInsertObj.getUrlsThumbImg())) {
				qy.setString("URLTHUMB", feedInsertObj.getUrlsThumbImg());
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsTitle())) {
				qy.setString("URLTITLE", feedInsertObj.getUrlsTitle());
			}
			if (Commonutility.checkempty(feedInsertObj.getUrlsPageurl())) {
				qy.setString("PAGEURL", feedInsertObj.getUrlsPageurl());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedDesc())) {
				qy.setString("FEEDDESC", feedInsertObj.getFeedDesc());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedStitle())) {
				qy.setString("FEEDSTITLE", feedInsertObj.getFeedStitle());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedTitle())) {
				qy.setString("FEEDTITLE", feedInsertObj.getFeedTitle());
			}
			if (feedInsertObj != null && feedInsertObj.getAmount() != null
					&& feedInsertObj.getAmount() != 0) {
				qy.setFloat("AMOUNT", feedInsertObj.getAmount());
			}
			if (Commonutility.checkempty(feedInsertObj.getFeedCategory())) {
				qy.setString("FEEDCATEGORY", feedInsertObj.getFeedCategory());
			}
			if (Commonutility.checkempty(feedInsertObj.getAdditionalData())) {
				qy.setString("ADDITIONALDATA", feedInsertObj.getAdditionalData());
			}
			System.out.println("99999");// FEEDTIME
			qy.setTimestamp("MODIFYDATE",
					Commonutility.enteyUpdateInsertDateTime());
			qy.setTimestamp("FEEDTIME",
					Commonutility.enteyUpdateInsertDateTime());
			System.out.println("------oip");
			qy.executeUpdate();
			// ###### file upload Start ######
			if (fileUpload != null && fileUpload.size() > 0) {
				log.logMessage("Enter into fileUpload feed edit", "info",
						FeedDAOService.class);
				for (int i = 0; i < fileUpload.size(); i++) {
					File mapFileUpload = fileUpload.get(i);
					String mapFileName = fileUploadFileName.get(i);
					String attachFileName = FilenameUtils
							.getBaseName(mapFileName);
					String attachFileExtension = FilenameUtils
							.getExtension(mapFileName);
					String uniqFileName = attachFileName + "_"
							+ commonutil.uniqueNoFromDate() + "."
							+ attachFileExtension;
					Integer fileType = mobComm
							.getFileExtensionType(attachFileExtension);
					log.logMessage("uniqFileName: " + uniqFileName, "info",
							FeedDAOService.class);
					log.logMessage("fileType: " + fileType, "info",
							FeedDAOService.class);
					if (fileType == 2) {
						videoFileMap.put(uniqFileName, mapFileUpload);
					} else {
						imageFileMap.put(uniqFileName, mapFileUpload);
					}
				}
				FeedsTblVO feedIdObj = new FeedsTblVO();
				feedIdObj.setFeedId(feedId);
				if (videoFileMap.size() > 0) {
					log.logMessage("Enter into video write feed edit", "info",
							FeedDAOService.class);
					String videoPath = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.videopath")
							+ feedId + "/";
					String videoPathThumb = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.video.thumbpath")
							+ feedId + "/";
					log.logMessage(videoPath + " :path: " + videoPathThumb,
							"info", FeedDAOService.class);
					for (Entry<String, File> entry : videoFileMap.entrySet()) {
						log.logMessage("Ente  feed attach video filename:"
								+ entry.getKey(), "info", FeedDAOService.class);
						log.logMessage(
								"Ente feed attach video filen:"
										+ entry.getValue(), "info",
								FeedDAOService.class);
						String videoName = entry.getKey();
						String videoThumbName = FilenameUtils
								.getBaseName(videoName) + ".jpeg";
						try {
							File destFile = new File(videoPath, videoName);
							FileUtils.copyFile(entry.getValue(), destFile);
							if (imageFileMap.containsKey(videoThumbName)) {
								File imageThumb = imageFileMap
										.get(videoThumbName);
								File destThumbFile = new File(imageThumb,
										videoPathThumb);
								FileUtils.copyFile(imageThumb, destThumbFile);
								imageFileMap.remove(videoThumbName);
							}
						} catch (Exception ex) {
							editCommitFlg = false;
							log.logMessage(
									"Exception ffound in video file upload : "
											+ ex, "error", FeedDAOService.class);
						}
						FeedattchTblVO feedAttchObj = new FeedattchTblVO();
						feedAttchObj.setIvrBnFEED_ID(feedIdObj);
						feedAttchObj.setIvrBnATTACH_NAME(videoName);
						feedAttchObj.setIvrBnTHUMP_IMAGE(videoThumbName);
						feedAttchObj.setIvrBnFILE_TYPE(2);
						feedAttchObj.setIvrBnSTATUS(1);
						feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						session.save(feedAttchObj);
					}
					log.logMessage("End video write feed post", "info",
							FeedDAOService.class);
				}
				if (imageFileMap.size() > 0) {
					log.logMessage("Enter into image write feed post", "info",
							FeedDAOService.class);
					String imagePathWeb = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.img.webpath")
							+ feedId + "/";
					String imagePathMobi = getText("external.imagesfldr.path")
							+ getText("external.uploadfile.feed.img.mobilepath")
							+ feedId + "/";
					for (Entry<String, File> entry : imageFileMap.entrySet()) {
						log.logMessage("Ente  feed attach image filename:"
								+ entry.getKey(), "info", FeedDAOService.class);
						log.logMessage(
								"Ente feed attach image filen:"
										+ entry.getValue(), "info",
								FeedDAOService.class);
						String imageName = entry.getKey();
						String imgBaseName = FilenameUtils
								.getBaseName(imageName);
						String imgExtenName = FilenameUtils
								.getExtension(imgBaseName);
						File imgFilePath = imageFileMap.get(imageName);
						Integer fileType = mobComm
								.getFileExtensionType(imgExtenName);
						FeedattchTblVO feedAttchObj = new FeedattchTblVO();
						feedAttchObj.setIvrBnFEED_ID(feedIdObj);
						feedAttchObj.setIvrBnSTATUS(1);
						feedAttchObj.setIvrBnATTACH_NAME(imageName);
						feedAttchObj.setIvrBnENTRY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						feedAttchObj.setIvrBnMODIFY_DATETIME(Commonutility
								.enteyUpdateInsertDateTime());
						if (fileType == 1) {
							feedAttchObj.setIvrBnFILE_TYPE(1);
							log.logMessage("imageName web :" + imageName,
									"info", FeedDAOService.class);
							// for web, image upload original size
							try {
								File destFileWeb = new File(imagePathWeb,
										imageName);
								FileUtils.copyFile(entry.getValue(),
										destFileWeb);
							} catch (Exception ex) {
								editCommitFlg = false;
								log.logMessage(
										"Exception found in image file upload for web : "
												+ ex, "error",
										FeedDAOService.class);
							}
							// for mobile, compress image upload
							int imgWitdh = 0;
							int imgHeight = 0;
							int imgOriginalWidth = mobiCommon
									.getImageWidth(imgFilePath);
							int imgOriginalHeight = mobiCommon
									.getImageHeight(imgFilePath);
							String imageHeightPro = getText("thump.img.height");
							String imageWidthPro = getText("thump.img.width");
							if (imgOriginalWidth > Commonutility
									.stringToInteger(imageWidthPro)) {
								imgWitdh = Commonutility
										.stringToInteger(imageWidthPro);
							} else {
								imgWitdh = imgOriginalWidth;
							}
							if (imgOriginalHeight > Commonutility
									.stringToInteger(imageHeightPro)) {
								imgHeight = Commonutility
										.stringToInteger(imageHeightPro);
							} else {
								imgHeight = imgOriginalHeight;
							}
							String imgSourcePath = imagePathWeb + imageName;
							String imgName = FilenameUtils
									.getBaseName(imageName);
							String imageFomat = FilenameUtils
									.getExtension(imageName);
							String imageFor = "all";
							log.logMessage("imageName for web compress : "
									+ imageName, "info", FeedDAOService.class);
							ImageCompress.toCompressImage(imgSourcePath,
									imagePathMobi, imgName, imageFomat,
									imageFor, imgWitdh, imgHeight);
						} else {
							feedAttchObj.setIvrBnFILE_TYPE(3);
							try {
								File destFileWeb = new File(imagePathWeb,
										imageName);
								File destFileMobi = new File(imagePathMobi,
										imageName);
								FileUtils.copyFile(entry.getValue(),
										destFileWeb);
								FileUtils.copyFile(entry.getValue(),
										destFileMobi);
							} catch (Exception ex) {
								editCommitFlg = false;
								log.logMessage(
										"Exception found in image file upload for type 3 : "
												+ ex, "error",
										FeedDAOService.class);
							}
						}
						session.save(feedAttchObj);
					}
					log.logMessage("End image write feed post", "info",
							FeedDAOService.class);
				}
			} else {
				log.logMessage("File upload empty", "info",
						FeedDAOService.class);
			}
			// ###### file upload end ######
			// ###### remove attachment Start ######
			CommonMobiDao commonServ = new CommonMobiDaoService();
			System.out.println("removeAttachIdsObj obj------------------- "
					+ removeAttachIdsObj);
			if (removeAttachIdsObj != null) {
				log.logMessage("Remove attachment Start", "info",
						FeedDAOService.class);
				String removeAttachIds = commonutil
						.jsnArryIntoStrBasedOnComma(removeAttachIdsObj);
				if (Commonutility.checkempty(removeAttachIds)) {
					if (!removeAttachIds.contains(",")) {
						removeAttachIds += ",";
					}
					if (removeAttachIds.contains(",")) {
						String[] removeIds = removeAttachIds.split(",");
						for (int j = 0; j < removeIds.length; j++) {
							String attchId = removeIds[j];
							if (Commonutility.checkempty(attchId)
									&& Commonutility.toCheckisNumeric(attchId)) {
								log.logMessage(
										"Enter into AttachmentDetails attachId: "
												+ Commonutility
														.stringToInteger(attchId),
										"info", FeedDAOService.class);
								FeedattchTblVO attachDelObj = new FeedattchTblVO();
								String qry = " from FeedattchTblVO where ivrBnSTATUS='1'  and  ivrBnATTCH_ID='"
										+ Commonutility
												.stringToInteger(attchId) + "'";
								System.out
										.println("FeedattchTblVO--before qry-----------------"
												+ qry);
								qy = session.createQuery(qry);
								attachDelObj = (FeedattchTblVO) qy
										.uniqueResult();
								log.logMessage(
										"End of queery execution of AttachmentDetails",
										"info", FeedDAOService.class);
								// FeedattchTblVO attachDelObj =
								// commonServ.getAttachmentDetails(Commonutility.stringToInteger(attchId));
								System.out
										.println("attachDelObj-------------------"
												+ attachDelObj);
								if (attachDelObj != null) {
									int fileType = attachDelObj
											.getIvrBnFILE_TYPE();
									System.out
											.println("fileType-------------------"
													+ fileType);
									File deleteFile = null;
									File deleteFileTwo = null;
									if (fileType == 1 || fileType == 3) {
										String imagePathWeb = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.img.webpath")
												+ feedId + "/";
										String imagePathMobi = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.img.mobilepath")
												+ feedId + "/";
										deleteFile = new File(
												imagePathWeb
														+ attachDelObj
																.getIvrBnATTACH_NAME());
										deleteFileTwo = new File(
												imagePathMobi
														+ attachDelObj
																.getIvrBnATTACH_NAME());
									} else if (fileType == 2) {
										String videoPath = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.videopath")
												+ feedId + "/";
										String videoPathThumb = getText("external.imagesfldr.path")
												+ getText("external.uploadfile.feed.video.thumbpath")
												+ feedId + "/";
										deleteFile = new File(
												videoPath
														+ attachDelObj
																.getIvrBnATTACH_NAME());
										deleteFileTwo = new File(
												videoPathThumb
														+ attachDelObj
																.getIvrBnATTACH_NAME());
									}
									try {
										if (deleteFile != null
												&& deleteFile.exists()) {
											FileUtils.forceDelete(deleteFile);
										}
										if (deleteFileTwo != null
												&& deleteFileTwo.exists()) {
											FileUtils
													.forceDelete(deleteFileTwo);
										}
									} catch (Exception ex) {
										log.logMessage(
												"feedEdit Exception found in file delete fileType="
														+ fileType + " :: "
														+ ex, "error",
												FeedDAOService.class);
									}
									String query = "delete FeedattchTblVO where ivrBnATTCH_ID=:ATTACHID";
									System.out
											.println("delete query-------------------"
													+ query
													+ "attchId------"
													+ attchId);
									Query qury = session.createQuery(query);
									qury.setInteger("ATTACHID", Commonutility
											.stringToInteger(attchId));
									qury.executeUpdate();
								}
							}
						}
					}
				} else {
					log.logMessage("Remove attachment JSONArray Not found",
							"info", FeedDAOService.class);
				}
			} else {
				log.logMessage("Remove attachment Not found", "info",
						FeedDAOService.class);
			}
			log.logMessage("End remove attachment ", "info",
					FeedDAOService.class);
			// ###### remove attachment end ######
			// ###### view tbl insert Start ######
			log.logMessage("View tbl insert Start ", "info",
					FeedDAOService.class);
			int isShareflg = 0;
			if (feedPrivacy != 0) {
				if (feedInsertObj != null && feedInsertObj.getFeedId() != null
						&& feedInsertObj.getFeedId() != 0) {
					try {
						log.logMessage("Enter into view feedid delete feedid:"
								+ feedInsertObj.getFeedId(), "info",
								FeedDAOService.class);
						String query = "delete FeedsViewTblVO where feedId.feedId=:FEEDID";
						Query qury = session.createQuery(query);
						qury.setInteger("FEEDID", feedInsertObj.getFeedId());
						int res = qury.executeUpdate();
						System.out.println("######### res :" + res);
						// editCommitFlg = true;
					} catch (Exception ex) {
						editCommitFlg = false;
						log.logMessage("Exception found in viewids remove:"
								+ ex, "error", FeedDAOService.class);
					}
				} else {
					editCommitFlg = false;
				}
				System.out.println("editCommitFlg-------------------"
						+ editCommitFlg);
				log.logMessage("End view id delete ", "info",
						FeedDAOService.class);
				FeedsViewTblVO feedviewObj = new FeedsViewTblVO();
				System.out.println("end of delete -------------------");
				System.out.println("feedInsertObj.getUsrId()--------"
						+ feedInsertObj.getUsrId());
				int societyId = feedInsertObj.getSocietyId().getSocietyId();
				System.out.println("societyId-------------------" + societyId);
				int userIdForView = 0;
				if (feedPrivacy == 2) {
					userIdForView = -1;
				} else if (feedPrivacy == 3) {
					userIdForView = -2;
				} else {
					userIdForView = feedInsertObj.getUsrId().getUserId();
				}
				System.out
						.println("feedInsertObj.getEntryBy()-------------------"
								+ feedInsertObj.getEntryBy());
				feedviewObj = feedInsertViewDataPack(userIdForView, feedId,
						feedInsertObj.getEntryBy(), societyId, isShareflg, -1,
						0);
				session.save(feedviewObj);
				System.out.println(editCommitFlg);
				if (Commonutility.checkempty(users)) {
					if (editCommitFlg) {
						if (!users.contains(",")) {
							users += ",";
						}
						if (users.contains(",")) {
							String[] viewIdsArry = users.split(",");
							log.logMessage("Feed viewid length:"
									+ viewIdsArry.length, "info",
									FeedDAOService.class);
							for (int i = 0; i < viewIdsArry.length; i++) {
								int viewId = 0;
								try {
									if (Commonutility
											.checkempty(viewIdsArry[i])
											&& Commonutility
													.toCheckisNumeric(viewIdsArry[i])) {
										log.logMessage(
												"Enter into view tbl insert id:"
														+ viewIdsArry[i],
												"info", FeedDAOService.class);
										viewId = Commonutility
												.stringToInteger(viewIdsArry[i]);
										feedviewObj = feedInsertViewDataPack(
												viewId, feedId,
												feedInsertObj.getEntryBy(),
												societyId, isShareflg, -1, 0);
										session.save(feedviewObj);
									}
								} catch (Exception ex) {
									editCommitFlg = false;
									log.logMessage(
											"Exception found in viewids insert:"
													+ ex, "error",
											FeedDAOService.class);
								}
							}
						}
					}
				}
				System.out
						.println("feedInsertObj.getEntryBy()------End-------------"
								+ feedInsertObj.getEntryBy());
			} else {

			}
			log.logMessage("View tbl insert End ", "info", FeedDAOService.class);
			// ###### view tbl insert end ######
			if (editCommitFlg) {
				tx.commit();
			} else {
				if (tx != null) {
					tx.rollback();
				}
				result = false;
			}
			result = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = false;
			log.logMessage("feedEdit Exception found in  : " + ex, "error",
					FeedDAOService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
			log = null;
			qy = null;
		}
		return result;
	}

	@Override
	public FeedsTblVO getFeedDetailsByOfferCarpoolId(int carpoolId) {
		FeedsTblVO feedMst = new FeedsTblVO();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from FeedsTblVO where feedStatus=:STATUS and feedType=:FEED_TYP and feedTypeId=:FEED_TYP_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setInteger("FEED_TYP", 76);
			qy.setInteger("FEED_TYP_ID", carpoolId);
			feedMst = (FeedsTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFeedDetailsByOfferCarpoolId======" + ex);
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
	public FeedsTblVO getFeedDetailsByFacilityBookId(int facBookId) {
		FeedsTblVO feedMst = new FeedsTblVO();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = " from FeedsTblVO where feedStatus=:STATUS and feedType=:FEED_TYP and feedTypeId=:FEED_TYP_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setInteger("FEED_TYP", 77);
			qy.setInteger("FEED_TYP_ID", facBookId);
			
			if(qy.uniqueResult() !=null )
				feedMst = (FeedsTblVO) qy.uniqueResult();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getFeedDetailsByFacilityBookId======" + ex);
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

}
