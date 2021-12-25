package com.mobi.publishskillvo.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.publishskillvo.PublishSkillTblVO;
import com.pack.utilitypkg.Commonutility;
import com.social.load.HibernateUtil;
import com.social.utils.Log;

public class PubilshSkillDaoServices implements PubilshSkillDao {

	@Override
	public int insetPublishSkill(PublishSkillTblVO pubObj) {
		Log log = new Log();
		Session session = null;
		Transaction tx = null;
		int retUniqId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pubObj);
			retUniqId = pubObj.getPubSkilId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception insetPublishSkill: "+e, "error", PubilshSkillDaoServices.class);
			retUniqId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return retUniqId;
	}

	@Override
	public boolean editPublishSkill(PublishSkillTblVO pubObj) {
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    try {
	    	log = new Log();
	    	System.out.println("editPublishSkill Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	String updateQry = "";
	    	if (pubObj.getFeedId() != null) {
	    		if (Commonutility.checkIntempty(pubObj.getFeedId().getFeedId())) {
		    		updateQry +="feedId.feedId=:FEEDID,";
		    	}
	    	}	    	
	    	System.out.println("---------qw-1");
	    	if (pubObj.getCategoryId() != null) {
	    		if (Commonutility.checkIntempty(pubObj.getCategoryId().getiVOCATEGORY_ID())) {
		    		updateQry +="categoryId.iVOCATEGORY_ID=:CATEID,";
		    	}
	    	}
	    	
	    	System.out.println("--------q--13");
	    	if (pubObj.getSkillId() != null) {
	    		if (Commonutility.checkIntempty(pubObj.getSkillId().getIvrBnSKILL_ID())) {
		    		updateQry +="skillId.ivrBnSKILL_ID=:SKILID,";
		    	}
	    	}
	    	
	    	System.out.println("----we------1");
	    	if (Commonutility.checkempty(pubObj.getTitle())) {
	    		updateQry +="title=:TITLE,";
	    	}
	    	if (Commonutility.checkIntempty(pubObj.getDuration())) {
	    		updateQry +="duration=:DURATION,";
	    	}
	    	System.out.println("------d----1");
	    	if (Commonutility.checkIntempty(pubObj.getDurationFlg())) {
	    		updateQry +="durationFlg=:DURATIONFLG,";
	    	}
	    	if (Commonutility.checkempty(pubObj.getAvbilDays())) {
	    		updateQry +="avbilDays=:AVADAYS,";
	    	}
	    	System.out.println("----------13");
	    	if (Commonutility.checkempty(pubObj.getBriefDesc())) {
	    		updateQry +="briefDesc=:DESC,";
	    	}
	    	System.out.println("----------12");
	    	if (pubObj.getFees() != null) {
	    		updateQry +="fees=:FEES,";
	    	}
	    	System.out.println("----------1");
	    	qy = session.createQuery("update PublishSkillTblVO set " + updateQry + " modifyDatetime=:MODYDATETIME where pubSkilId=:PUBSKILID and status=:STATUS");	 	    	
	    	qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setInteger("STATUS", 1);
	    	qy.setInteger("PUBSKILID", pubObj.getPubSkilId());
	    	if (pubObj.getFeedId() != null) {
	    		if (Commonutility.checkIntempty(pubObj.getFeedId().getFeedId())) {
		    		qy.setInteger("FEEDID", pubObj.getFeedId().getFeedId());
		    	}
	    	}
	    	if (pubObj.getCategoryId() !=null) {
	    		if (Commonutility.checkIntempty(pubObj.getCategoryId().getiVOCATEGORY_ID())) {
		    		qy.setInteger("CATEID", pubObj.getCategoryId().getiVOCATEGORY_ID());
		    	}
	    	}	    	
	    	if (pubObj.getSkillId() != null) {
	    		if (Commonutility.checkIntempty(pubObj.getSkillId().getIvrBnSKILL_ID())) {
		    		qy.setInteger("SKILID", pubObj.getSkillId().getIvrBnSKILL_ID());
		    	}
	    	}
	    	
	    	if (Commonutility.checkempty(pubObj.getTitle())) {
	    		qy.setString("TITLE", pubObj.getTitle());
	    	}
	    	if (Commonutility.checkIntempty(pubObj.getDuration())) {
	    		qy.setInteger("DURATION", pubObj.getDuration());
	    	}
	    	if (Commonutility.checkIntempty(pubObj.getDurationFlg())) {
	    		qy.setInteger("DURATIONFLG", pubObj.getDurationFlg());
	    	}
	    	if (Commonutility.checkempty(pubObj.getAvbilDays())) {
	    		qy.setString("AVADAYS", pubObj.getAvbilDays());
	    	}
	    	if (Commonutility.checkempty(pubObj.getBriefDesc())) {
	    		qy.setString("DESC", pubObj.getBriefDesc());
	    	}
	    	if (pubObj.getFees() != null) {
	    		qy.setFloat("FEES", pubObj.getFees());
	    	}
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			 log.logMessage("Exception found in  editPublishSkill : "+ex, "error", PubilshSkillDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    return result;
	}

	@Override
	public List<Object[]> publishSkilSrchList(String searchQuery,
			String timestamp, int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> resultListObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into PubilshSkillDaoServices " , "info", PubilshSkillDaoServices.class);			
			/*String sqlQuery = "select pubtl.pub_skil_id,pubtl.title,pubtl.duration,pubtl.duration_flg,pubtl.avbil_days,pubtl.brief_desc, "
					+ "pubtl.fees,pubtl.status,pubtl.entry_by,pubtl.enrty_datetime, pubtl.category_id,cat.category_name, pubtl.skill_id, "
					+ "skltb.skill_name, pubtl.user_id,usrtb.username,usrtb.mobile_no,usrtb.email_id,usrtb.image_name_mobile,cty.city_name,pubtl.feed_id  "
					+ "from  mvp_publish_skill_tbl pubtl "
					+ "left join category_mst_tbl cat on pubtl.category_id=cat.category_id "
					+ "left join  mvp_skill_tbl skltb on pubtl.skill_id=skltb.skill_id "
					+ "left join usr_reg_tbl usrtb on pubtl.user_id=usrtb.usr_id "
					+ "left join city_mst_tbl cty on usrtb.city_id=cty.city_id  "
					+ "where pubtl.status=1 " + searchQuery + " limit " + startLimit +"," + endLimit;*/
			String sqlQuery = "select pubtl.pub_skil_id,pubtl.title,pubtl.duration,pubtl.duration_flg,pubtl.avbil_days,pubtl.brief_desc, "
					+ "pubtl.fees,pubtl.status,pubtl.entry_by,pubtl.enrty_datetime, pubtl.category_id,cat.category_name, pubtl.skill_id,"
					+ " skltb.skill_name, pubtl.user_id,usrtb.username,usrtb.mobile_no,usrtb.email_id,usrtb.image_name_mobile,cty.city_name,pubtl.feed_id,"
					+ "usrflt.WINGS_NAME, usrflt.FALTNO, usrflt.IS_MYSELF, usrtb.FNAME, usrtb.LNAME from  mvp_publish_skill_tbl pubtl left join"
					+ " category_mst_tbl cat on pubtl.category_id=cat.category_id left join"
					+ "  mvp_skill_tbl skltb on pubtl.skill_id=skltb.skill_id left join "
					+ "usr_reg_tbl usrtb on pubtl.user_id=usrtb.usr_id left join"
					+ " city_mst_tbl cty on usrtb.city_id=cty.city_id left join "
					/*+ "usr_flat_detail_tbl usrflt on pubtl.USER_ID=usrflt.USR_ID where pubtl.status=1 and usrflt.IS_MYSELF=1  " + searchQuery + " limit " + startLimit +"," + endLimit;*/
					+ "usr_flat_detail_tbl usrflt on pubtl.USER_ID=usrflt.USR_ID where pubtl.status=1 and (usrflt.IS_MYSELF=1 or usrflt.IS_MYSELF is null)  " + searchQuery + " order by pubtl.MODIFY_DATETIME desc";
			System.out.println("publishSkilSrchList  sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("pubtl.pub_skil_id", Hibernate.TEXT)
					.addScalar("pubtl.title", Hibernate.TEXT)
					.addScalar("pubtl.duration", Hibernate.TEXT)
					.addScalar("pubtl.duration_flg", Hibernate.TEXT)
					.addScalar("pubtl.avbil_days", Hibernate.TEXT)
					.addScalar("pubtl.brief_desc", Hibernate.TEXT)
					.addScalar("pubtl.fees", Hibernate.TEXT)
					.addScalar("pubtl.status", Hibernate.TEXT)
					.addScalar("pubtl.entry_by", Hibernate.TEXT)
					.addScalar("pubtl.enrty_datetime", Hibernate.TEXT)
					.addScalar("pubtl.category_id", Hibernate.TEXT)
					.addScalar("cat.category_name", Hibernate.TEXT)
					.addScalar("pubtl.skill_id", Hibernate.TEXT)
					.addScalar("skltb.skill_name", Hibernate.TEXT)
					.addScalar("pubtl.user_id", Hibernate.TEXT)
					.addScalar("usrtb.username", Hibernate.TEXT)
					.addScalar("usrtb.mobile_no", Hibernate.TEXT)
					.addScalar("usrtb.email_id", Hibernate.TEXT)
					.addScalar("usrtb.image_name_mobile", Hibernate.TEXT)
					.addScalar("cty.city_name", Hibernate.TEXT)
					.addScalar("pubtl.feed_id", Hibernate.TEXT)
					.addScalar("usrflt.WINGS_NAME", Hibernate.TEXT)
					.addScalar("usrflt.FALTNO", Hibernate.TEXT)
					.addScalar("usrflt.IS_MYSELF", Hibernate.TEXT)
					.addScalar("usrtb.FNAME", Hibernate.TEXT)
					.addScalar("usrtb.LNAME", Hibernate.TEXT);
			resultListObj = queryObj.list();
			System.out.println(queryObj.toString());
			log.logMessage("Enter into PubilshSkillDaoServices size :" + resultListObj.size(), "info", PubilshSkillDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in PubilshSkillDaoServices :" + ex, "error", PubilshSkillDaoServices.class);
			resultListObj = null;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return resultListObj;
	}

	@Override
	public boolean deletepublish(int pubSkilId) {
		 Session session = null;
		    Transaction tx = null;
		    Log log = new Log();
		    Query qy = null;
		    boolean result = false;
		    try {	    	
		    	System.out.println("publish skill Delete [Start].");
		    	session = HibernateUtil.getSession();
		    	tx = session.beginTransaction();
		    	qy = session.createQuery("update PublishSkillTblVO set status=:STATUS,modifyDatetime=:MODYDATETIME where  pubSkilId=:PUBSKILID");	 
			      qy.setInteger("STATUS", 0);
			      qy.setInteger("PUBSKILID", pubSkilId);
			      qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
		    	qy.executeUpdate();
		    	tx.commit();
		    	result = true;
		    } catch (Exception ex) {
		    	if (tx != null) {tx.rollback();}	
		    	result = false;
				log.logMessage("deletepublish Exception found in  : "+ex, "error", PubilshSkillDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
				log = null; qy = null;
		    }
		    System.out.println("Step 3 :publish block [End].");
		    return result;
	}

	@Override
	public PublishSkillTblVO getPublishSkilData(int pubskilId,int rid) {
		// TODO Auto-generated method stub
		PublishSkillTblVO publishDataObj = new PublishSkillTblVO();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
			try {
				String qry = "From PublishSkillTblVO where pubSkilId=:PUB_SKIL_ID "
						+ " and userId=:USER_ID and status=:STATUS ";
				Query qy = session.createQuery(qry);
				qy.setInteger("PUB_SKIL_ID",pubskilId);
				qy.setInteger("USER_ID",rid);
				qy.setInteger("STATUS",1);
				publishDataObj = (PublishSkillTblVO) qy.uniqueResult();
			} catch(Exception ex) {
				log.logMessage("Exception found in getPublishSkilData :" + ex, "error", PubilshSkillDaoServices.class);
				publishDataObj = null;
			} finally {
				session.flush();session.clear(); session.close();session = null;
			}
		return publishDataObj;
	}

}
