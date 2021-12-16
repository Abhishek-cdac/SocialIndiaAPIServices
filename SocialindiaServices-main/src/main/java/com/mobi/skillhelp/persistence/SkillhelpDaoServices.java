package com.mobi.skillhelp.persistence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.common.mobiCommon;
import com.mobi.skillhelpvo.ServiceBookingVO;
import com.mobile.otpVo.otpDaoService;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;

public class SkillhelpDaoServices implements SkillhelpDao{

	@Override
	public List<Object[]> skillDirectoryLaborData(int rid) {
		Log log= new Log();
		List<Object[]> skillDirObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			String qry= " from LaborProfileTblVO where ivrBnENTRY_BY=:ENTRYBY and ivrBnLBR_STS=:STATUS";
		      Query qy = session.createQuery(qry);
		      qy.setInteger("ENTRYBY", rid);
		      qy.setInteger("STATUS", 1);
		      skillDirObj = qy.list();
		} catch(Exception ex) {
			log.logMessage("Exception found in skillDirectoryLaborData :" + ex, "error", SkillhelpDaoServices.class);
			skillDirObj = null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return skillDirObj;
	}

	@Override
	public int getTotalCountSqlQuery(String sqlQuery) {
		int returnVal = 0;
		Log log= new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getTotalCountSqlQuery sqlQuery :" +sqlQuery , "info", SkillhelpDaoServices.class);
//			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery);
			returnVal = ((Number) queryObj.uniqueResult()).intValue();
			log.logMessage("Enter into getTotalCountSqlQuery total count :" + returnVal, "info", SkillhelpDaoServices.class);
//			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in getTotalCountSqlQuery :" + ex, "error", SkillhelpDaoServices.class);
			returnVal = 0;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return returnVal;
	}

	@Override
	public List<Object[]> testskillDirectoryLaborData(String sqlQuery,int start,int end,int rid) {
		Log log= new Log();
		List<Object[]> skillDirObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into skillDirectoryLaborData sqlQuery :" +sqlQuery , "info", SkillhelpDaoServices.class);
			tx = session.beginTransaction();
			String locQueryAuto = "select lbr.lbr_id, lbr.lbr_name, lbr.lbr_email, lbr.lbr_ph_no, lbr.image_name_web, lbr.image_name_mobile,"
					+ " lbr.lbr_descp, cty.city_name, stt.state_name, cnt.country_name, lbr.lbr_rating, lbr.cost, lbr.cost_per_time, lbr.experience,"
					+ " catg.category_name, skill.skill_name, case when  fvurt.user_id="+rid+" then fvurt.status else '' end favstatus"
					+ " from mvp_lbr_reg_tbl lbr"
					+ " left join mvp_lbr_skill_tbl lbr_skill on lbr.lbr_id = lbr_skill.lbr_id"
					+ " left join category_mst_tbl catg on lbr_skill.category_id = catg.category_id"
					+ " left join mvp_skill_tbl skill on lbr_skill.skill_id = skill.skill_id"
					+ " left join city_mst_tbl cty on lbr.city_id = cty.city_id"
					+ " left join state_mst_tbl stt on lbr.state_id = stt.state_id"
					+ " left join country_mst_tbl cnt on lbr.country_id = cnt.country_id "
					+ "left join mvp_favourite_tbl fvurt on lbr.lbr_id=fvurt.FAVOURITE_TYPE_ID"
					+ " where lbr.lbr_sts =1 " + sqlQuery + " group by lbr.lbr_id order by lbr_skill.MODIFY_DATETIME desc limit " + start + " , " + end + " ";
			log.logMessage("skillDirectoryLaborData sqlQuery :" +locQueryAuto , "info", SkillhelpDaoServices.class);
			Query queryObj = session.createSQLQuery(locQueryAuto)
					.addScalar("lbr.lbr_id", Hibernate.TEXT)
					.addScalar("lbr.lbr_name", Hibernate.TEXT)
					.addScalar("lbr.lbr_email", Hibernate.TEXT)
					.addScalar("lbr.lbr_ph_no", Hibernate.TEXT)
					.addScalar("lbr.image_name_web", Hibernate.TEXT)
					.addScalar("lbr.image_name_mobile", Hibernate.TEXT)
					.addScalar("lbr.lbr_descp", Hibernate.TEXT)
					.addScalar("cty.city_name", Hibernate.TEXT)
					.addScalar("stt.state_name", Hibernate.TEXT)
					.addScalar("cnt.country_name", Hibernate.TEXT)
					.addScalar("lbr.lbr_rating", Hibernate.TEXT)
					.addScalar("lbr.cost", Hibernate.TEXT)
					.addScalar("lbr.cost_per_time", Hibernate.TEXT)
					.addScalar("lbr.experience", Hibernate.TEXT)
					.addScalar("catg.category_name", Hibernate.TEXT)
					.addScalar("skill.skill_name", Hibernate.TEXT)
					.addScalar("favstatus", Hibernate.TEXT);
			skillDirObj = queryObj.list();
			log.logMessage("Enter into skillDirectoryLaborData size :" + skillDirObj.size(), "info", SkillhelpDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in skillDirectoryLaborData :" + ex, "error", SkillhelpDaoServices.class);
			skillDirObj = null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return skillDirObj;
	}

	@Override
	public int bookigNewInsert(ServiceBookingVO bookingnewObj) {
		Log log=new Log();
		Session session =null;
		Transaction tx = null;
		int bookingId = -1;
		try {
			log.logMessage("Enter into insert bookigNewInsert", "info", SkillhelpDaoServices.class);
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(bookingnewObj);
			bookingId = bookingnewObj.getBookingId();
			tx.commit();
		} catch(Exception ex) {
			bookingId = 0;
			log.logMessage("Exception found in bookigNewInsert " + ex, "error", SkillhelpDaoServices.class);
		}
		return bookingId;
	}

	@Override
	public boolean bookigUpdate(ServiceBookingVO bookingnewObj) {
	    Session session = null;
	    Transaction tx = null;
	    Query qy = null;
	    boolean result = false;
	    Log log = new Log();
	    try {
	    	System.out.println("Start bookig Update ");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	Date date;
	    	CommonUtils comutil=new CommonUtilsServices();
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			String updateQuery = "";
			if(bookingnewObj.getServiceCost() != null && bookingnewObj.getServiceCost() !=0) {
				updateQuery += " serviceCost=:SERVICECOST,";
			}
			if (bookingnewObj.getStartTime() != null) {
				updateQuery += " startTime=:STARTTIME,";
			}
			if (bookingnewObj.getEndTime() != null) {
				updateQuery += " endTime=:ENDTIME,";
			}
			if (Commonutility.checkIntempty(bookingnewObj.getStatus())) {
				updateQuery += " status=:STATUS,";
			}
			if (bookingnewObj.getFeedbackId() != null && bookingnewObj.getFeedbackId().getIvrBnFEEDBK_ID() != 0) {
				updateQuery += " feedbackId=:FEEDBACKID,";
			}
			qy = session.createQuery("update ServiceBookingVO set " + updateQuery + " modifyDatetime=:MODIYDATE where bookingId=:BOOKINGID");
			if(bookingnewObj.getServiceCost() != null && bookingnewObj.getServiceCost() !=0) {
				qy.setFloat("SERVICECOST", bookingnewObj.getServiceCost());
			}
			if (bookingnewObj.getStartTime() != null) {
				qy.setTimestamp("STARTTIME", bookingnewObj.getStartTime());
			}
			if (bookingnewObj.getEndTime() != null) {
				qy.setTimestamp("ENDTIME", bookingnewObj.getEndTime());
			}
			if (Commonutility.checkIntempty(bookingnewObj.getStatus())) {
				qy.setInteger("STATUS", bookingnewObj.getStatus());
			}
			if (bookingnewObj.getFeedbackId() != null && bookingnewObj.getFeedbackId().getIvrBnFEEDBK_ID() != 0) {
				qy.setInteger("FEEDBACKID", bookingnewObj.getFeedbackId().getIvrBnFEEDBK_ID());
			}
		    qy.setTimestamp("MODIYDATE", date);
		    qy.setInteger("BOOKINGID", bookingnewObj.getBookingId());
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (HibernateException ex) {
	    	if (tx != null) {tx.rollback();}
	    	result = false;
			 log.logMessage("bookigUpdate Exception updateOtpUser : "+ex, "error", SkillhelpDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    System.out.println("Step 3 :user block [End].");
	    return result;
	}

	@Override
	public boolean bookingDelete(ServiceBookingVO bookingDelObj) {
		Session session = null;
	    Transaction tx = null;
	    Query qy = null;
	    boolean result = false;
	    Log log = new Log();
	    try {
	    	System.out.println("Start bookig Update ");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	Date date;
	    	CommonUtils comutil=new CommonUtilsServices();
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			qy = session.createQuery("update ServiceBookingVO set status=:STATUS,modifyDatetime=:MODIYDATE where bookingId=:BOOKINGID");
		    qy.setInteger("STATUS", 0);
		    qy.setTimestamp("MODIYDATE", date);
		    qy.setInteger("BOOKINGID", bookingDelObj.getBookingId());
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (HibernateException ex) {
	    	if (tx != null) {tx.rollback();}
	    	result = false;
			 log.logMessage("bookigUpdate Exception updateOtpUser : "+ex, "error", SkillhelpDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
	    System.out.println("Step 3 :user block [End].");
	    return result;
	}

	@Override
	public List<Object[]> bookingList(ServiceBookingVO bookingnewObj,
			Date timestamp, int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> bookingObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into bookingList " , "info", SkillhelpDaoServices.class);
			String sqlQuery ="SELECT bok.booking_id, bok.problem, bok.prefered_date, bok.start_time, bok.end_time, bok.service_cost, bok.ENTRY_DATETIME, lbr.LBR_ID, lbr.LBR_NAME, lbr.IMAGE_NAME_WEB, lbr.LBR_STS, cty.CITY_NAME, catg.category_name, skill.SKILL_NAME, fdbk.FEEDBK_TXT, fdbk.RATING"
							 +" FROM mvp_service_booking_tbl bok "
							 +" LEFT JOIN mvp_lbr_reg_tbl lbr ON lbr.LBR_ID = bok.LABOUR_ID"
							 +" LEFT JOIN city_mst_tbl cty ON cty.CITY_ID = lbr.CITY_ID"
							 +" LEFT JOIN mvp_lbr_skill_tbl skl ON skl.LBR_ID = bok.LABOUR_ID"
							 +" LEFT JOIN category_mst_tbl catg ON catg.CATEGORY_ID = skl.CATEGORY_ID"
							 +" LEFT JOIN mvp_skill_tbl skill ON skill.SKILL_ID = skl.SKILL_ID"
							 +" LEFT JOIN mvp_feedback_tbl fdbk ON fdbk.FEEDBK_ID = bok.FEEDBACK_ID"
							 +" WHERE bok.USR_ID ='"+bookingnewObj.getUsrId()+"' group by lbr.lbr_id"
							 +" LIMIT 0 , 20";
			// +" WHERE bok.USR_ID =:USERID"
			System.out.println("sqlQuery::" + sqlQuery);
//			sqlQuery = sqlQuery.toLowerCase();
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery);
//			queryObj.setParameter("USERID", bookingnewObj.getUsrId());
			bookingObj = queryObj.list();
//			bookingObj = session.createSQLQuery(sqlQuery).list();
			log.logMessage("Enter into bookingList size had:" + bookingObj.size(), "info", SkillhelpDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in bookingList :" + ex, "error", SkillhelpDaoServices.class);
			bookingObj = null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return bookingObj;
	}

	@Override
	public List<Object[]> bookingListTest(ServiceBookingVO bookingnewObj,
			String timestamp, int startLimit, int endLimit) {
		Log log= new Log();
		List<Object[]> bookingObj = new ArrayList<Object[]>();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into bookingList " , "info", SkillhelpDaoServices.class);
			String sqlQuery = "select bok.booking_id, bok.problem, bok.prefered_date, bok.start_time, bok.end_time, bok.service_cost, bok.entry_datetime, "
					+ "lbr.lbr_id, lbr.lbr_name, lbr.image_name_mobile, bok.status, cty.city_name, catg.category_name, skill.skill_name, fdbk.feedbk_txt, fdbk.rating "
					+ "from mvp_service_booking_tbl bok  "
					+ "left join mvp_lbr_reg_tbl lbr on lbr.lbr_id = bok.labour_id "
					+ "left join city_mst_tbl cty on cty.city_id = lbr.city_id "
					+ "left join mvp_lbr_skill_tbl skl on skl.lbr_id = bok.labour_id "
					+ "left join category_mst_tbl catg on catg.category_id = skl.category_id "
					+ "left join mvp_skill_tbl skill on skill.skill_id = skl.skill_id "
					+ "left join mvp_feedback_tbl fdbk on fdbk.feedbk_id = bok.feedback_id "
					+ "where bok.usr_id ='" + bookingnewObj.getUsrId().getUserId() + "' and bok.status <> '0' and bok.entry_datetime<str_to_date('" + timestamp + "','%Y-%m-%d %H:%i:%S') group by bok.booking_id order by bok.entry_datetime desc limit " + startLimit + " , " + endLimit + " ";
			// +" WHERE bok.USR_ID =:USERID"
			System.out.println("booking list sqlQuery::" + sqlQuery);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery)
					.addScalar("booking_id", Hibernate.INTEGER)
					.addScalar("problem", Hibernate.TEXT)
					.addScalar("prefered_date", Hibernate.TIMESTAMP)
					.addScalar("start_time", Hibernate.TIMESTAMP)
					.addScalar("end_time", Hibernate.TIMESTAMP)
					.addScalar("service_cost", Hibernate.FLOAT)
					.addScalar("ENTRY_DATETIME", Hibernate.TIMESTAMP)
					.addScalar("LBR_ID", Hibernate.INTEGER)
					.addScalar("LBR_NAME", Hibernate.TEXT)
					.addScalar("IMAGE_NAME_MOBILE", Hibernate.TEXT)
					.addScalar("bok.status", Hibernate.INTEGER)
					.addScalar("CITY_NAME", Hibernate.TEXT)
					.addScalar("category_name", Hibernate.TEXT)
					.addScalar("SKILL_NAME", Hibernate.TEXT)
					.addScalar("FEEDBK_TXT", Hibernate.TEXT)
					.addScalar("RATING", Hibernate.INTEGER);
//			queryObj.setParameter("USERID", bookingnewObj.getUsrId());
			bookingObj = queryObj.list();
//			bookingObj = session.createSQLQuery(sqlQuery).list();
			log.logMessage("Enter into bookingList size had:" + bookingObj.size(), "info", SkillhelpDaoServices.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in bookingList :" + ex, "error", SkillhelpDaoServices.class);
			bookingObj = null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return bookingObj;
	}

	@Override
	public LaborProfileTblVO Laborgetmobno(Integer lbrid) {
		// TODO Auto-generated method stub

		LaborProfileTblVO labourdetails =new LaborProfileTblVO();
		Integer retpassid;
		Log log = null;
		Session session = null;
		Query qury = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
				String qry = " from LaborProfileTblVO where ivrBnLBR_ID ="+lbrid+" ";
				qury = session.createQuery(qry);
				labourdetails = (LaborProfileTblVO) qury.uniqueResult();
		} catch (Exception ex) {
			log.logMessage("Exception found in getGatepassid: " + ex, "error", mobiCommon.class);
			retpassid = 0;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			qury = null;
			log = null;
		}
		return labourdetails;

}
}
