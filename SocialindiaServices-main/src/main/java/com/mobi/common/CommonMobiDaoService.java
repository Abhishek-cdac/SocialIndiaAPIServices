package com.mobi.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.commonvo.PrivacyInfoTblVO;
import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.timelinefeedvo.FeedattchTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.timelinefeedvo.FeedsViewTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.townShipMgmt.townShipMgmtDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.NotificationStatusTblVO;

public class CommonMobiDaoService implements CommonMobiDao {
	Log log=new Log();
	@Override
	public boolean checkSocietyKey(String societyKey, String userId) {
		UserMasterTblVo userInfo = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
		boolean result = false;
		String qry = "";
		try {
			if (userId != null && userId != "" && !userId.equalsIgnoreCase("")) {
				System.out.println("with usr id ###########");
				qry = " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS and   userId=:USER_ID ";
			} else {
				System.out.println("with out usr id ###########");
				qry = " from UserMasterTblVo where societyId.activationKey=:SOCIETY_KEY and societyId.statusFlag=:STATUS ";
			}
			System.out.println("query :" + qry);
			Query qy = session.createQuery(qry);
			qy.setInteger("STATUS", 1);
			qy.setString("SOCIETY_KEY", societyKey);
			qy.setMaxResults(1);
			if (userId != null && userId != "" && !userId.equalsIgnoreCase("")) {
				qy.setInteger("USER_ID", Integer.parseInt(userId));
			}
			userInfo = (UserMasterTblVo) qy.uniqueResult();
			if (userInfo != null) {
				System.out.println("ridddddddd:" + userInfo.getUserId());
				result = true;
			} else {
				System.out.println("ridddddddd  null statussssss false" );
				result = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
			System.out.println(" checkSocietyKey======" + ex);
			log.logMessage("checkSocietyKey Exception checkSocietyKey : " + ex,
					"error", CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return result;
	}

	@Override
	public int getTotalCountSqlQuery(String sqlQuery) {
		int returnVal = 0;
		Log log = new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			log.logMessage("Enter into getTotalCountSqlQuery sqlQuery :"
					+ sqlQuery, "info", CommonMobiDaoService.class);
			tx = session.beginTransaction();
			Query queryObj = session.createSQLQuery(sqlQuery);
			returnVal = ((Number) queryObj.uniqueResult()).intValue();
			log.logMessage("Enter into getTotalCountSqlQuery total count :"
					+ returnVal, "info", CommonMobiDaoService.class);
			tx.commit();
		} catch (Exception ex) {
			log.logMessage("Exception found in getTotalCountSqlQuery :" + ex,
					"error", CommonMobiDaoService.class);
			returnVal = 0;
		} finally {
			if(session!=null){ session.flush();session.clear();session.close(); session = null; }
			if (tx != null) {
				tx = null;
			}
			log = null;
		}
		return returnVal;
	}

	@Override
	public boolean checkTownshipKey(String townshipKey, String userId) {
		boolean result = true;
		/*UserMasterTblVo userData = new UserMasterTblVo();
		Session session = null;
		Query qy = null;
		
		String qry = "";
		session = HibernateUtil.getSession();
		Log log = new Log();
		try {

			if (userId != null && userId != "" && !userId.equalsIgnoreCase("")) {
				qry = "from UserMasterTblVo where societyId.townshipId.status=:STATUS and  societyId.townshipId.activationKey=:TOWNSHIP_KEY"
						+ " and  userId=:USER_ID ";
			} else {
				qry = "from UserMasterTblVo where societyId.townshipId.status=:STATUS and  societyId.townshipId.activationKey=:TOWNSHIP_KEY";
			}
			qy = session.createQuery(qry);
			qy.setString("TOWNSHIP_KEY", townshipKey);
			qy.setMaxResults(1);
			qy.setInteger("STATUS", 1);
			if (userId != null && userId != "" && !userId.equalsIgnoreCase("")) {
				qy.setInteger("USER_ID", Integer.parseInt(userId));
			}
			userData = (UserMasterTblVo) qy.uniqueResult();
			if (userData != null) {
				result = true;
			} else {
				result = false;
			}
		} catch (HibernateException ex) {
			System.out.println(" checkTownshipKey=====" + ex);
			log.logMessage(" Exception checkTownshipKey : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				session = null;
			}
			qy = null;
		}*/
		return result;
	}

	@Override
	public UserMasterTblVo getProfileDetails(int rid) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
		try {
			String qry = " from UserMasterTblVo where statusFlag=:STATUS  and  userId=:USER_ID";
			Query quryObj = session.createQuery(qry);
			quryObj.setInteger("STATUS", 1);
			quryObj.setInteger("USER_ID", rid);
			userdata = (UserMasterTblVo) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getProfileDetails======" + ex);
			log.logMessage("Exception getProfileDetails : " + ex, "error",
					CommonMobiDaoService.class);
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
	public UserMasterTblVo adminContactNo(String adminName) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
		try {
			String qry = " from UserMasterTblVo where userName=:USERNAME";
			Query quryObj = session.createQuery(qry);
			quryObj.setString("USERNAME", adminName);
			userdata = (UserMasterTblVo) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" adminContactNo======" + ex);
			log.logMessage("Exception adminContactNo : " + ex, "error",
					CommonMobiDaoService.class);
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
	public UserMasterTblVo getUserInfo(String mobileNo) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
		try {
			System.out.println("Enter into get Parent id");
			String qry = " from UserMasterTblVo where mobileNo=:MOBILENO";
			Query quryObj = session.createQuery(qry);
			quryObj.setString("MOBILENO", mobileNo);
			userdata = (UserMasterTblVo) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" ######## getParentId ########" + ex);
			log.logMessage("Exception getParentId : " + ex, "error",
					CommonMobiDaoService.class);
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
	public FeedattchTblVO getAttachmentDetails(int attachId) {
		FeedattchTblVO attachData = new FeedattchTblVO();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
		try {
			log.logMessage("Enter into getAttachmentDetails attachId: "
					+ attachId, "info", CommonMobiDaoService.class);
			String qry = " from FeedattchTblVO where ivrBnSTATUS=:STATUS  and  ivrBnATTCH_ID=:ATTACHID";
			Query quryObj = session.createQuery(qry);
			quryObj.setInteger("STATUS", 1);
			quryObj.setInteger("ATTACHID", attachId);
			attachData = (FeedattchTblVO) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.logMessage("Exception getAttachmentDetails : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return attachData;
	}

	@Override
	public boolean removeFeedViewId(int feedId) {
		Session session = HibernateUtil.getSession();
		Log log = new Log();
		boolean retValues = false;
		// FeedsViewTblVO
		try {
			log.logMessage("Enter into feed view ids remove", "info",
					CommonMobiDaoService.class);
			FeedsViewTblVO feedviewObj = new FeedsViewTblVO();
			FeedsTblVO feedObj = new FeedsTblVO();
			feedObj.setFeedId(feedId);
			feedviewObj.setFeedId(feedObj);
			session.delete(feedviewObj);
		} catch (Exception ex) {
			log.logMessage("Exception removeFeedViewId : " + ex, "error",
					CommonMobiDaoService.class);
		}
		return retValues;
	}

	@Override
	public int getTotalCountQuery(String query) {
		int returnVal = 0;
		Session session = null;
		Query quey = null;
		Log log = new Log();
		try {
			session = HibernateUtil.getSession();
			quey = session.createQuery(query);
			returnVal = ((Number) quey.uniqueResult()).intValue();
		} catch (Exception ex) {
			log.logMessage("Exceeption found getTotalCountQuery : " + ex,
					"error", CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				session = null;
			}
			quey = null;
		}
		return returnVal;
	}

	@Override
	public JSONArray getloginverify(String userid, String committee,
			String resident, String profileimgPath, String societyimgPath) {
		Session session = null;
		Log log = new Log();
		JSONArray jsonArry = new JSONArray();
		List<UserMasterTblVo> userInfoList = new ArrayList<UserMasterTblVo>();
		profileDao profile = new profileDaoServices();
		try {
			int committeeGrpcode = 0;
			int residenteGrpcode = 0;
			session = HibernateUtil.getSession();
			int commiteResidArr[] = getCommiteResiIds(committee,resident);
			if (commiteResidArr.length == 2) {
				committeeGrpcode = commiteResidArr[0];
				residenteGrpcode = commiteResidArr[1];
			}
			JsonpackDao jsonPack = new JsonpackDaoService();
			jsonArry = jsonPack.loginUserDetail(userid, profileimgPath, societyimgPath, committeeGrpcode, residenteGrpcode);
			
//			userInfoList = profile.getUserSocietyDeatils(userid,committeeGrpcode, residenteGrpcode, "");
//			System.out.println("==userInfoList=====" + userInfoList.size());
//			 
//			if (userInfoList != null && userInfoList.size() > 0) {
//				UserMasterTblVo userObj;
//				for (Iterator<UserMasterTblVo> it = userInfoList.iterator(); it.hasNext();) {
//					userObj = it.next();
//					JSONObject jsonOb = new JSONObject();
//					jsonOb = jsonPack.loginUserDetail_t(userObj, profileimgPath, societyimgPath, committeeGrpcode, residenteGrpcode);
//					jsonArry.put(jsonOb);					
//				}
//			} else {
//				jsonArry = null;
//			}
		} catch (Exception ex) {
			log.logMessage("Exceeption found getloginverify : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				session = null;
			}
		}
		return jsonArry;
	}

	@Override
	public int[] getCommiteResiIds(String committee, String resident) {
		int[] returnVal = new int[2];
		Session session = null;
		Query quey = null;
		Log log = new Log();
		try {
			log.logMessage("Enter into getCommiteResiIds : " ,"info", CommonMobiDaoService.class);
			session = HibernateUtil.getSession();
			String locSlqry = "";
			GroupMasterTblVo locGrpMstrVOobj= new GroupMasterTblVo();
			uamDao uam = new uamDaoServices();
			int committeeGrpcode = 0;
			int residenteGrpcode = 0;
			locSlqry = "from GroupMasterTblVo where upper(groupName) = upper('"+ committee + "') or groupName=upper('" + committee + "')";
			quey = session.createQuery(locSlqry);
			locGrpMstrVOobj = (GroupMasterTblVo) quey.uniqueResult();
			if (locGrpMstrVOobj != null) {
				committeeGrpcode = locGrpMstrVOobj.getGroupCode();
				log.logMessage("=====>committeeGrpcode : " + committeeGrpcode,"info", CommonMobiDaoService.class);
			} else {// new group create
				committeeGrpcode = uam.createnewgroup_rtnId(committee);
				log.logMessage("new group create committeeGrpcode : " + committeeGrpcode,"info", CommonMobiDaoService.class);
			}
			locSlqry = "from GroupMasterTblVo where upper(groupName) = upper('"+ resident + "') or groupName=upper('" + resident + "')";
			quey = session.createQuery(locSlqry);
			locGrpMstrVOobj = (GroupMasterTblVo) quey.uniqueResult();
			if (locGrpMstrVOobj != null) {
				residenteGrpcode = locGrpMstrVOobj.getGroupCode();
				log.logMessage("=====>residenteGrpcode : " + residenteGrpcode,"info", CommonMobiDaoService.class);
			} else {// new group create
				residenteGrpcode = uam.createnewgroup_rtnId(resident);
				log.logMessage("new group create residenteGrpcode : " + residenteGrpcode,"info", CommonMobiDaoService.class);
			}
			returnVal[0] = committeeGrpcode;
			returnVal[1] = residenteGrpcode;
		} catch (Exception ex) {
			log.logMessage("Exceeption found getCommiteResiIds : " + ex,"error", CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				session = null;
			}
			quey = null;
		}
		return returnVal;
	}

	@Override
	public List<WhyShouldIUpdateTblVO> whyShouldIUpdateContent() {
		Session session = null;
		Log log = new Log();
		List<WhyShouldIUpdateTblVO> listObj = new ArrayList<WhyShouldIUpdateTblVO>();
		try {
			session = HibernateUtil.getSession();
			String qry = " from WhyShouldIUpdateTblVO where status=:STATUS";
			Query quryObj = session.createQuery(qry);
			quryObj.setInteger("STATUS", 1);
			listObj = quryObj.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.logMessage("Exception whyShouldIUpdateContent : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return listObj;
	}

	@Override
	public CategoryMasterTblVO getCategoryDetails(int cateId) {
		CategoryMasterTblVO catedata = new CategoryMasterTblVO();
		Session session = null;
		Log log = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
			String qry = " from CategoryMasterTblVO where iVOACT_STAT=:STATUS  and  iVOCATEGORY_ID=:CATEID";
			Query quryObj = session.createQuery(qry);
			quryObj.setInteger("STATUS", 1);
			quryObj.setInteger("CATEID", cateId);
			catedata = (CategoryMasterTblVO) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.logMessage("Exception getCategoryDetails : " + ex, "error",CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return catedata;
	}

	@Override
	public PrivacyInfoTblVO getDefaultPrivacyFlg(int userId) {
		PrivacyInfoTblVO privacydata = new PrivacyInfoTblVO();
		Session session = HibernateUtil.getSession();
		Log log = new Log();
		try {
			String qry = " from PrivacyInfoTblVO where status=:STATUS  and  usrId=:USERID";
			Query quryObj = session.createQuery(qry);
			quryObj.setInteger("STATUS", 1);
			quryObj.setInteger("USERID", userId);
			privacydata = (PrivacyInfoTblVO) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			privacydata = null;
			log.logMessage("Exception DefaultPrivacyFlg : " + ex, "error",
					CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return privacydata;
	}

	@Override
	public int insertPrivacyFlg(PrivacyInfoTblVO privacyObj) {
		Log log= new Log();
		Session session = null;
		Transaction tx = null;
		int privacyId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(privacyObj);
			privacyId = privacyObj.getUniqId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception insertPrivacyFlg: "+e, "error", CommonMobiDaoService.class);
			privacyId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;		
		}
		return privacyId;
	}

	@Override
	public boolean updatePrivacyFlg(PrivacyInfoTblVO privacyObj) {
		Session session = HibernateUtil.getSession();
	    boolean result=false;
	    Transaction tx = null;
	    Log log=new Log();
	    try {
	      tx = session.beginTransaction();
	      String updateQuery = "";
	      System.out.println("-----::" + privacyObj.getPrivacyFlg());
  	      if (privacyObj.getPrivacyFlg() != 0) {
  	    	updateQuery += "privacyFlg=:PRIVACYFLG,";
  	      }
  	      if (Commonutility.checknull(privacyObj.getUsrIds())) {
  	    	updateQuery += "usrIds=:USRIDS,";
  	      }
  	      if (Commonutility.checkIntnull(privacyObj.getStatus())) {
  	    	updateQuery += "status=:STATUS,";
  	      }
  	      Query qy = session.createQuery("update PrivacyInfoTblVO set  " + updateQuery + " modifyDatetime=:MODYDATETIME where usrId=:USERID");
  	      if (privacyObj.getPrivacyFlg() != 0) {
  	    	qy.setInteger("PRIVACYFLG", privacyObj.getPrivacyFlg());
  	      }  	      
	  	  if (privacyObj.getUsrId().getUserId() != 0) {
	  		qy.setInteger("USERID", privacyObj.getUsrId().getUserId());
	  	  }
	  	  if (Commonutility.checkempty(privacyObj.getUsrIds())) {
	  		qy.setString("USRIDS", privacyObj.getUsrIds());
	  	  }
	  	  if (Commonutility.checkIntnull(privacyObj.getStatus())) {
	  		qy.setInteger("STATUS", privacyObj.getStatus());
  	      }
	      qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("Exception found in updatePrivacyFlg : "+ex, "error", CommonMobiDaoService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear();session.close();session = null;}
			if(tx!=null){tx=null;}	log=null;
	    }
	    return result;
	}

	@Override
	public int notifictionFlgGet(String mobileno) {
		int returnVal = 0;
		Log log= new Log();
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			NotificationStatusTblVO notifyobj=new NotificationStatusTblVO();
			log.logMessage("Enter into notifictionFlgGet" , "info", CommonMobiDaoService.class);
			tx = session.beginTransaction();
			Query queryObj = session.createQuery("from NotificationStatusTblVO where mobileNo = '"+mobileno+"'");
			notifyobj = (NotificationStatusTblVO) queryObj.uniqueResult();
			if(notifyobj!=null){
				returnVal = notifyobj.getNotificationFlag();
			}else{
				returnVal = 0;
			}
			log.logMessage("Enter into notifictionFlgGet total count :" + returnVal, "info", CommonMobiDaoService.class);
			tx.commit();
		} catch(Exception ex) {
			log.logMessage("Exception found in notifictionFlgGet :" + ex, "error", CommonMobiDaoService.class);
			returnVal = 0;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return returnVal;
	}

	@Override
	public boolean flatDetailsUpdate(int userid,int flatId) {
		Session session = HibernateUtil.getSession();
	    boolean result=false;
	    Transaction tx = null;
	    Log log=new Log();
	    try {
	      log.logMessage("Enter into flat update", "error", CommonMobiDaoService.class);
	      tx = session.beginTransaction();	      
  	      Query qy = session.createQuery("update FlatMasterTblVO set ivrBnismyself=:ISMYSELF,modifyDatetime=:MODYDATETIME where flat_id=:FLATID");  	      
  	      qy.setInteger("ISMYSELF", 1);
  	      qy.setInteger("FLATID", flatId);
	      qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	      qy.executeUpdate();
	      Query qry = session.createQuery("update FlatMasterTblVO set ivrBnismyself=:ISMYSELF,modifyDatetime=:MODYDATETIME where usrid =:USR_ID and flat_id!=:FLATID");  	      
	      qry.setInteger("ISMYSELF", 0);
	      qry.setInteger("USR_ID", userid);
	      qry.setInteger("FLATID", flatId);
	      qry.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	      qry.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("Exception found in flatDetailsUpdate : "+ex, "error", CommonMobiDaoService.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear();session.close();session = null;}
			if(tx!=null){tx=null;}	log=null;
	    }
	    return result;
	}

	@Override
	public boolean updateTableByQuery(String query) {
		// TODO Auto-generated method stub
		boolean locRtn= false;
		Session session = null;
		Transaction tx = null;
		Query lvrQryobj = null;
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			lvrQryobj = session.createQuery(query);
			lvrQryobj.executeUpdate();
			tx.commit();
			locRtn= true;				
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			Commonutility.toWriteConsole("HibernateException Common Update Query : "+e);
			locRtn= false;				
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			lvrQryobj = null;
		}		
		return locRtn;
	}

	@Override
	public List<UserMasterTblVo> getUserRegisterDeatils(String sqlQuery) {
		// TODO Auto-generated method stub
		List<UserMasterTblVo> userInfoList=new ArrayList<UserMasterTblVo>();
		Session session = HibernateUtil.getSession();
	    try {
	      Query qy = session.createQuery(sqlQuery);
	      userInfoList = qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getUserSocietyDeatils======" + ex);
			 log.logMessage("profileDaoServices Exception getUserSocietyDeatils : "+ex, "error", profileDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return userInfoList;
	}

	@Override
	public ComplaintsTblVO getcomplaintMastTblById(int complaintId) {
		// TODO Auto-generated method stub
		ComplaintsTblVO complaintTbl = new ComplaintsTblVO();
		Session session = null;
		Log log = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
			String qry = " from ComplaintsTblVO where complaintsId=:COMPLAINT_ID";
			Query quryObj = session.createQuery(qry);
			quryObj.setInteger("COMPLAINT_ID", complaintId);
			complaintTbl = (ComplaintsTblVO) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.logMessage("Exception getcomplaintMastTblById : " + ex, "error",CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return complaintTbl;
	}

	@Override
	public SocietyMstTbl getSocietymstDetail(String societyKey) {
		// TODO Auto-generated method stub
		SocietyMstTbl societyTbl = new SocietyMstTbl();
		Session session = null;
		Log log = null;
		try {
			log = new Log();
			session = HibernateUtil.getSession();
			String qry = " from SocietyMstTbl where activationKey=:ACTIVATION_KEY";
			Query quryObj = session.createQuery(qry);
			quryObj.setString("ACTIVATION_KEY", societyKey);
			societyTbl = (SocietyMstTbl) quryObj.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.logMessage("Exception getSocietymstDetail : " + ex, "error",CommonMobiDaoService.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return societyTbl;
	}

	
	

	

}
