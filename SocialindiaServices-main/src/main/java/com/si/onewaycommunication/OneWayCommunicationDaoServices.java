package com.si.onewaycommunication;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindia.generalmgnt.generalmgntDaoServices;
import com.socialindiaservices.vo.OneWayCommunicationTblVO;

public class OneWayCommunicationDaoServices implements OneWayCommunicationDao {
	Log log = new Log();

	@Override
	public List<OneWayCommunicationTblVO> getAccessFlags(String sid,String rights) {
		Transaction tx = null;
		OneWayCommunicationTblVO tblVO = null;
		
		List<OneWayCommunicationTblVO> resultList = new ArrayList<OneWayCommunicationTblVO>();
		Session session = HibernateUtil.getSession();
		String qry = "";
		try {
			qry = "FROM OneWayCommunicationTblVO where sid=:S_ID and rights=:rights";
			Query qy = session.createQuery(qry);
			qy.setString("S_ID", sid);
			qy.setString("rights", rights);
			resultList = qy.list();
			System.out.println("getAccessFlags sid=>" + sid + " => " + resultList.size());
			
			if(resultList.size() == 0){
				tx = session.beginTransaction();
				tblVO = new OneWayCommunicationTblVO();
				
				tblVO.setSid(sid);
				tblVO.setBuysell(1);
				tblVO.setComplaints(1);
				tblVO.setEasypay(1);
				tblVO.setFacilitybooking(1);
				tblVO.setGatepass(1);
				tblVO.setMydocuments(1);
				tblVO.setOnlineusers(1);
				tblVO.setPersonalevents(1);
				tblVO.setProfileinfo(1);
				tblVO.setSharecare(1);
				tblVO.setShophome(1);
				tblVO.setSkillhelp(1);			
				tblVO.setTimelinefeeds(1);
				
				tblVO.setPublishasbuyerorseller(1);
				tblVO.setWhatsonstore(1);
				tblVO.setCarpooling(1);
				tblVO.setDonate(1);
				tblVO.setPublishskils(1);
				tblVO.setSkilldirctory(1);
				tblVO.setMyservicebooking(1);
				tblVO.setKnowledgebase(1);
				tblVO.setPersonalbriefcase(1);
				tblVO.setSocietyinfo(1);
				tblVO.setSearchdocument(1);
				tblVO.setNewbooking(1);
				tblVO.setCreateinvitation(1);
				tblVO.setIssuegatepass(1);
				tblVO.setFeeds(1);
				tblVO.setOnlineuser(1);
				tblVO.setBrowsebycategory(1);
				tblVO.setSearchwithkeywords(1);
				tblVO.setRateyourexperience(1);
				tblVO.setReportproblem(1);
				tblVO.setMypassport(1);
				tblVO.setUtilitypayments(1);
				tblVO.setPostcomplaint(1);
				
				session.save(tblVO);
				tx.commit();
				
				resultList.add(tblVO);
				System.out.println("getAccessFlags sid=>" + sid + " => " + resultList.size());
			}
			
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			System.out.println("OneWayCommunicationDaoServices getAccessFlags======"+ ex);
			log.logMessage("OneWayCommunicationDaoServices getAccessFlags: "+ ex, "error", OneWayCommunicationDaoServices.class);
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
		}
		
		return resultList;
	}

	@Override
	public int setAccessFlags(OneWayCommunicationTblVO tblVO) {
		int id = -1;
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		String qry;
		Query qy = null;
		Common lvrCmn = new CommonDao();
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();

			String lvrSltqry = "OneWayCommunicationTblVO where sid = '"+ tblVO.getSid() + "'";
			Long locnt = (Long) lvrCmn.getuniqueColumnVal(lvrSltqry,"count(*)", "", "");
			Commonutility.toWriteConsole("setAccessFlags locnt : " + locnt);
			if (locnt > 0) {
				tx = session.beginTransaction();
				
				qry = "update OneWayCommunicationTblVO set buysell="+ tblVO.getBuysell() + ","+
						"sharecare="+ tblVO.getSharecare() + "," +
						"skillhelp="+ tblVO.getSkillhelp() + ","+
						"mydocuments="+ tblVO.getMydocuments() + ","+
						"facilitybooking="+ tblVO.getFacilitybooking() + ","+ 
						"personalevents="+ tblVO.getPersonalevents() +","+ 
						"gatepass="+ tblVO.getGatepass() + ","+ 
						"profileinfo="+ tblVO.getProfileinfo() +","+ 
						"timelinefeeds="+ tblVO.getTimelinefeeds() + ","+ 
						"onlineusers="+ tblVO.getOnlineusers() +","+ 
						"shophome="+ tblVO.getShophome() + ","+ 
						"easypay="+ tblVO.getEasypay() +","+ 
						"complaints="+ tblVO.getComplaints() +","+
						"publishasbuyerorseller="+tblVO.getPublishasbuyerorseller() +","+
						"whatsonstore="+ tblVO.getWhatsonstore()+","+
						"carpooling="+ tblVO.getCarpooling()+","+
						"donate="+ tblVO.getDonate()+","+
						"publishskils="+ tblVO.getPublishskils()+","+
						"skilldirctory="+ tblVO.getSkilldirctory()+","+
						"myservicebooking="+ tblVO.getMyservicebooking()+","+
						"knowledgebase="+ tblVO.getKnowledgebase()+","+
						"personalbriefcase="+ tblVO.getPersonalbriefcase()+","+
						"societyinfo="+ tblVO.getSocietyinfo()+","+
						"searchdocument="+ tblVO.getSearchdocument()+","+
						"newbooking="+ tblVO.getNewbooking()+","+
						"createinvitation="+ tblVO.getCreateinvitation()+","+
						"issuegatepass="+ tblVO.getIssuegatepass()+","+
						"feeds="+ tblVO.getFeeds()+","+
						"onlineuser="+ tblVO.getOnlineuser()+","+
						"browsebycategory="+ tblVO.getBrowsebycategory()+","+
						"searchwithkeywords="+ tblVO.getSearchwithkeywords()+","+
						"rateyourexperience="+ tblVO.getRateyourexperience()+","+
						"reportproblem="+ tblVO.getReportproblem()+","+
						"mypassport="+ tblVO.getMypassport()+","+
						"utilitypayments="+ tblVO.getUtilitypayments()+","+
						"rights='"+ tblVO.getRights()+"',"+
						"postcomplaint="+ tblVO.getPostcomplaint()+ " where sid = '"+ tblVO.getSid() +"'";
				
				System.out.println("setAccessFlags qry : " + qry);

				qy = session.createQuery(qry);
				id = qy.executeUpdate();
				tx.commit();
			} else {
				tx = session.beginTransaction();
				
				tblVO.setBuysell(1);
				tblVO.setComplaints(1);
				tblVO.setEasypay(1);
				tblVO.setFacilitybooking(1);
				tblVO.setGatepass(1);
				tblVO.setMydocuments(1);
				tblVO.setOnlineusers(1);
				tblVO.setPersonalevents(1);
				tblVO.setProfileinfo(1);
				tblVO.setSharecare(1);
				tblVO.setShophome(1);
				tblVO.setSkillhelp(1);			
				tblVO.setTimelinefeeds(1);
				
				tblVO.setPublishasbuyerorseller(1);
				tblVO.setWhatsonstore(1);
				tblVO.setCarpooling(1);
				tblVO.setDonate(1);
				tblVO.setPublishskils(1);
				tblVO.setSkilldirctory(1);
				tblVO.setMyservicebooking(1);
				tblVO.setKnowledgebase(1);
				tblVO.setPersonalbriefcase(1);
				tblVO.setSocietyinfo(1);
				tblVO.setSearchdocument(1);
				tblVO.setNewbooking(1);
				tblVO.setCreateinvitation(1);
				tblVO.setIssuegatepass(1);
				tblVO.setFeeds(1);
				tblVO.setOnlineuser(1);
				tblVO.setBrowsebycategory(1);
				tblVO.setSearchwithkeywords(1);
				tblVO.setRateyourexperience(1);
				tblVO.setReportproblem(1);
				tblVO.setMypassport(1);
				tblVO.setUtilitypayments(1);
				tblVO.setPostcomplaint(1);
				session.save(tblVO);
				id = tblVO.getPk();
				tx.commit();
			}
		} catch (Exception ex) {
			if (tx != null) {
				id = -1;
				tx.rollback();
			}
			id = -1;
			System.out.println("setAccessFlags : Exception found setAccessFlags() : "+ ex);
			logwrite.logMessage("setAccessFlags : Exception found setAccessFlags() : "+ ex, "error", generalmgntDaoServices.class);

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
			logwrite = null;
		}
		Commonutility.toWriteConsole("setAccessFlags id : " + id);
		return id;
	}
}
