package com.socialindia.generalmgnt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;
import com.socialindia.generalmgnt.persistance.StaffSlryTblVO;
import com.socialindia.generalmgnt.persistance.StaffWrkTblVO;
import com.socialindiaservices.vo.BiometricDatabaseInfoTblVO;
import com.socialindiaservices.vo.BiometricUserInfoTblVO;
import com.socialindiaservices.vo.ManageGSTTblVO;

public class generalmgntDaoServices implements generalmgmtDao {

	@Override
	public int staffCreation(StaffMasterTblVo staffMaster) {		
		int staff_id = 0;
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		String lvrSltqry = null;
		Common lvrCmn = new CommonDao();
		try {
			logwrite = new Log();                                      
			session = HibernateUtil.getSession();
			if (staffMaster.getCompanyId()!=null) {
				lvrSltqry = "StaffMasterTblVo where staffMobno = '"+staffMaster.getStaffMobno()+"' and  companyId = "+staffMaster.getCompanyId().getCompanyId();
			} else {
				lvrSltqry = "StaffMasterTblVo where staffMobno = '"+staffMaster.getStaffMobno()+"'";
			}
			
			Long  locnt = (Long) lvrCmn.getuniqueColumnVal(lvrSltqry,"count(*)","","");
			Commonutility.toWriteConsole("locnt : "+locnt);
			if (locnt>0) {
				staff_id = -2;
			} else {
				tx = session.beginTransaction();
				session.save(staffMaster);
				staff_id = staffMaster.getStaffID();
				tx.commit();		
			}
				
		} catch (Exception ex) {			
			if (tx != null) {
				staff_id = -1; tx.rollback();
			}staff_id = -1;
			System.out.println("Step -1 : Exception found generalmgntDaoServices.staffCreation() : "+ex);
			logwrite.logMessage("Step -1 : Exception found staffCreation() : "+ex, "error", generalmgntDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		    return staff_id;
	}
	
	@Override
	public int bioMetricDbCreation(BiometricDatabaseInfoTblVO tblVO) {		
		int id = 0;
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		String qry;
		Query qy = null;
		Common lvrCmn = new CommonDao();
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			
			String lvrSltqry = "BiometricDatabaseInfoTblVO where socyteaId = '"+tblVO.getSocyteaId()+"'";
			Long  locnt = (Long) lvrCmn.getuniqueColumnVal(lvrSltqry,"count(*)","","");
			Commonutility.toWriteConsole("locnt : "+locnt);
			if (locnt>0) {
				tx = session.beginTransaction();
				qry = "update BiometricDatabaseInfoTblVO set bioHostname='"+tblVO.getBioHostname()+"', bioUsername='"+ tblVO.getBioUsername() + "', bioPassword='"+ tblVO.getBioPassword() + "', bioDatabase='"
		  	             + tblVO.getBioDatabase() + "', bioPort='"+ tblVO.getBioPort()+"' where socyteaId = '"+tblVO.getSocyteaId()+"'";
				System.out.println("qry : "+qry);
				qy = session.createQuery(qry);
				id = qy.executeUpdate();				
				tx.commit();
			} else {
				tx = session.beginTransaction();
				session.save(tblVO);
				id = tblVO.getID();
				tx.commit();	
			}
		} catch (Exception ex) {			
			if (tx != null) {
				id = -1; tx.rollback();
			}id = -1;
			System.out.println("Step -1 : Exception found generalmgntDaoServices.bioMetricDbCreation() : "+ex);
			logwrite.logMessage("Step -1 : Exception found bioMetricDbCreation() : "+ex, "error", generalmgntDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		Commonutility.toWriteConsole("bioMetricDbCreation id : "+id);
		    return id;
	}
	
	@Override
	public int manageGSTInfo(ManageGSTTblVO tblVO) {		
		int id = 0;
		Session session = null;
		Transaction tx = null;
		Log logwrite = null;
		String qry;
		Query qy = null;
		Common lvrCmn = new CommonDao();
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			
			String lvrSltqry = "ManageGSTTblVO ";
			Long  locnt = (Long) lvrCmn.getuniqueColumnVal(lvrSltqry,"count(*)","","");
			Commonutility.toWriteConsole("locnt : "+locnt);
			if (locnt>0) {
				tx = session.beginTransaction();
				qry = "update ManageGSTTblVO set gstNum='"+tblVO.getGstNum()+"', minGstAmt='"+ tblVO.getMinGstAmt() + "', minAmt='"+ tblVO.getMinAmt() + "' ";
				System.out.println("qry : "+qry);
				qy = session.createQuery(qry);
				id = qy.executeUpdate();
				id = 1;
				tx.commit();
			} else {
				tx = session.beginTransaction();
				session.save(tblVO);
				id = tblVO.getID();
				tx.commit();	
			}
		} catch (Exception ex) {			
			if (tx != null) {
				id = -1; tx.rollback();
			}id = -1;
			System.out.println("Step -1 : Exception found generalmgntDaoServices.manageGSTInfo() : "+ex);
			logwrite.logMessage("Step -1 : Exception found manageGSTInfo() : "+ex, "error", generalmgntDaoServices.class);
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;} logwrite = null;
		}
		Commonutility.toWriteConsole("manageGSTInfo id : "+id);
		    return id;
	}
	
	
	@Override
	public ManageGSTTblVO getGSTInfo() {
		Commonutility.toWriteConsole("In getGSTInfo ");
		ManageGSTTblVO infoTblVO = new ManageGSTTblVO();
		Session session = HibernateUtil.getSession();
		Log logwrite =null;
		Query qy = null;
	    try {
	    	logwrite = new Log();
	    	session = HibernateUtil.getSession();
	    	String qry = " from ManageGSTTblVO ";
	    	qy = session.createQuery(qry);
	    	infoTblVO = (ManageGSTTblVO) qy.uniqueResult();
	    } catch (Exception ex) {
	    	infoTblVO = null;
	    	System.out.println("Step -1 :Exception found generalmgntDaoServices.getGSTInfo() : "+ex);
	    	logwrite.logMessage("Step -1 :Exception getGSTInfo() : "+ex, "error", generalmgntDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    	logwrite = null; qy = null;
	    }
	    Commonutility.toWriteConsole("getGSTInfo infoTblVO : "+infoTblVO);
	    return infoTblVO;
	}

	@Override
	public BiometricDatabaseInfoTblVO getBioMetricDB(int socyteaId) {
		Commonutility.toWriteConsole("getBioMetricDB socyteaId : "+socyteaId);
		BiometricDatabaseInfoTblVO infoTblVO = new BiometricDatabaseInfoTblVO();
		Session session = HibernateUtil.getSession();
		Log logwrite =null;
		Query qy = null;
	    try {
	    	logwrite = new Log();
	    	session = HibernateUtil.getSession();
	    	String qry = " from BiometricDatabaseInfoTblVO where socyteaId=:SOCYTEA_ID";
	    	qy = session.createQuery(qry);
	    	qy.setInteger("SOCYTEA_ID", socyteaId);
	    	infoTblVO = (BiometricDatabaseInfoTblVO) qy.uniqueResult();
	    } catch (Exception ex) {
	    	infoTblVO = null;
	    	System.out.println("Step -1 :Exception found generalmgntDaoServices.getBioMetricDB() : "+ex);
	    	logwrite.logMessage("Step -1 :Exception getBioMetricDB() : "+ex, "error", generalmgntDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    	logwrite = null; qy = null;
	    }
	    Commonutility.toWriteConsole("getBioMetricDB infoTblVO : "+infoTblVO);
	    return infoTblVO;
	}

	@Override
	public List<StateMasterTblVO> commonStateData(int countryidkey) {		
		List<StateMasterTblVO> statmst = null;
		Session session = null;Query qy = null;
		Log logwrite = null;
		try {
			logwrite = new Log();
			statmst = new ArrayList<StateMasterTblVO>();
			session = HibernateUtil.getSession();
			qy = session.createQuery(" from StateMasterTblVO where countryId=:COUNTRY_ID");
			qy.setInteger("COUNTRY_ID", countryidkey);
			statmst = qy.list();
		} catch (Exception ex) {
			System.out.println("Step -1 : Exception found generalmgntDaoServices.commonStateData() : "+ex);
			logwrite.logMessage("Step -1 : Exception found commonStateData() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;} qy = null; logwrite = null;
		}
		return statmst;
	}

	@Override
	public List<CityMasterTblVO> commonCityData(int stateidkey) {		
		return null;
	}

	@Override
	public int getInitTotal(String sqlQuery) {
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		Log logwrite = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQuery);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (Exception ex) {			
			System.out.println("Step -1 : Exception found generalmgntDaoServices.getInitTotal() : "+ex);
			logwrite.logMessage("Step -1 : Exception found getInitTotal() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			logwrite = null; qy = null;
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		Log logwrite = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (Exception ex) {
			System.out.println("Step -1 : Exception found generalmgntDaoServices.getTotalFilter() : "+ex);
			logwrite.logMessage("Step -1 : Exception found getTotalFilter() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			logwrite = null; qy = null;
		}
		return totcnt;
	}

	
	@Override
	public List<StaffMasterTblVo> getUserDetailList(String qry, int pstartrow, int totrow) {
		List<StaffMasterTblVo> userdata = new ArrayList<StaffMasterTblVo>();
		Session session = null;
		Query qy = null;
		Log logwrite = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			/*if(plikepartQry!=null && !plikepartQry.equalsIgnoreCase("")){
				qy = session.createQuery("From StaffMasterTblVo where status=:STATUS "+plikepartQry).setFirstResult(pstartrow).setMaxResults(totrow);
			}else{
				qy = session.createQuery("From StaffMasterTblVo where status=:STATUS ").setFirstResult(pstartrow).setMaxResults(totrow);
			}
			
			qy.setString("STATUS", "1");*/
			qy = session.createQuery(qry).setFirstResult(pstartrow).setMaxResults(totrow);				
			userdata = qy.list();
		} catch (Exception ex) {
			System.out.println("Step -1 : Exception found generalmgntDaoServices.getUserDetailList() : "+ex);
			logwrite.logMessage("Step -1 : Exception found getUserDetailList() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			logwrite = null; qy = null;
		}
		return userdata;
	}

	@Override
	public int gettotalcount(String sql) {
		int totcnt = 0;
		Session session = null;
		Log logwrite = null;
		Query qy = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			qy = session.createQuery(sql);
			totcnt = ((Number) qy.uniqueResult()).intValue();
		} catch (Exception ex) {
			System.out.println("Step -1 : Exception found generalmgntDaoServices.gettotalcount() : "+ex);
			logwrite.logMessage("Step -1 : Exception found gettotalcount() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if (session != null) { session.flush(); session.clear(); session.close(); session = null; }
			logwrite = null; qy = null;
		}
		return totcnt;
	}
	
	@Override
	public StaffMasterTblVo editstaff(int userId) {		
		StaffMasterTblVo userdata = null;
		Session session = null;
		Log logwrite = null;
		Query qy = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();			
			qy = session.createQuery("From StaffMasterTblVo where staffID=:STAFF_ID");
			qy.setInteger("STAFF_ID", userId);
			userdata = (StaffMasterTblVo) qy.uniqueResult();
		} catch (Exception ex) {
			System.out.println("Step -1 : Exception found generalmgntDaoServices.editstaff() : "+ex);
			logwrite.logMessage("Step -1 : Exception found editstaff() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if (session != null) { session.flush(); session.clear(); session.close(); session = null; }
			logwrite = null; qy = null;
		}
		return userdata;
	}

	@Override
	public StaffMasterTblVo updateStaff(int locvrStaff_ID,String locvrLBR_NAME, String locvrLBR_EMAIL,String locvrLBR_PH_NO, String locvrLBR_GENDER,
			int locvrID_CARD_TYP, String locvrID_CARD_NO,String locvrLBR_ADD_1, int locvrLBR_COUNTRY_ID1,int locvLBR_STATE_ID1, int locvrLBR_CITY_ID1,
			String locvrLBR_ISD, int locvrCAT_ID, int locvrPIN_CODE,int locvrWORK_HOUR,String imageweb,String staffImageFileName) {		
		StaffMasterTblVo stafflist = null;
		Session session = null;
		Transaction tx = null;
		Query qy = null;
		String qry = null;
		Log logwrite = null;
	      try {
	    	  logwrite = new Log();
	    	  session = HibernateUtil.getSession();
	    	  tx = session.beginTransaction();	    	 
			if (staffImageFileName != null) {
	    		   qry = "update StaffMasterTblVo set staffName='"+locvrLBR_NAME + "', staffEmail='"+ locvrLBR_EMAIL + "'," + " staffMobno='"+ locvrLBR_PH_NO + "', staffGender='" + locvrLBR_GENDER + "', staffAddr='"
	  	                + locvrLBR_ADD_1 +"',iVOcradid="+ locvrID_CARD_TYP +",staffIdcardno='" + locvrID_CARD_NO +"',staffCountry="+ locvrLBR_COUNTRY_ID1 +",staffState="
	  	                + locvLBR_STATE_ID1 +",staffCity="    + locvrLBR_CITY_ID1  +  ",ISDcode='"+locvrLBR_ISD+"',iVOstaffcategid="+locvrCAT_ID+",pstlId="+locvrPIN_CODE+",Workinghours="
	  	                +locvrWORK_HOUR+",imageNameWeb='"+staffImageFileName+"',imageNameMobile='"+staffImageFileName+"'"+ " where staffID="+ locvrStaff_ID+ " ";
			} else {
	    		   qry = "update StaffMasterTblVo set companyId='"+2+"',staffName='"+ locvrLBR_NAME + "', staffEmail='"+ locvrLBR_EMAIL + "'," + " staffMobno='"+ locvrLBR_PH_NO + "', staffGender='"
	  	                + locvrLBR_GENDER + "', staffAddr='"+ locvrLBR_ADD_1 +"',iVOcradid="+ locvrID_CARD_TYP +",staffIdcardno='" + locvrID_CARD_NO +"',staffCountry="+ locvrLBR_COUNTRY_ID1 +",staffState="
	  	                + locvLBR_STATE_ID1 +",staffCity="    + locvrLBR_CITY_ID1  +  ",ISDcode='"+locvrLBR_ISD+"',iVOstaffcategid="+locvrCAT_ID+",pstlId="+locvrPIN_CODE+",Workinghours="
	  	                +locvrWORK_HOUR+""+ " where staffID="+ locvrStaff_ID+ " ";
			}	       	      
			qy = session.createQuery(qry);
			qy.executeUpdate();
			tx.commit();   
			stafflist = new StaffMasterTblVo();
	      } catch (Exception ex) {
	        if (tx != null) { tx.rollback(); }
	        System.out.println("Step -1 : Exception found generalmgntDaoServices.updateStaff() : "+ex);
			logwrite.logMessage("Step -1 : Exception found updateStaff() : "+ex, "error", generalmgntDaoServices.class);
	      } finally {
	    	  if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    	  tx = null; qy = null; qry = null;
	      }	   	 
	      return stafflist;
	}

	@Override
	public boolean deletestaff(int locvrStaff_ID) {
		boolean result = false;
	    Session session = null;
	    Transaction tx = null;
	    Query qy = null;
	    Log logwrite = null;
	    try {
	    	logwrite = new Log();
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	qy = session.createQuery("update StaffMasterTblVo set status='0' " + "  where staffID=" + locvrStaff_ID + "");
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	if (tx != null) { tx.rollback();}
	    	System.out.println("Step -1 : Exception found generalmgntDaoServices.deletestaff() : "+ex);
			logwrite.logMessage("Step -1 : Exception found deletestaff() : "+ex, "error", generalmgntDaoServices.class);
	    	result = false;
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    	tx = null; qy = null; logwrite = null;
	    }
	    return result;
	}

	@Override
	public StaffSlryTblVO editSalary(int locvrStaff_ID) {
		StaffSlryTblVO userdata = null;
		Session session = null;
		Log logwrite = null;
		Query qy = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			qy = session.createQuery("From StaffSlryTblVO where staffID=:UNI_ID");
			qy.setInteger("UNI_ID", locvrStaff_ID);			
			userdata = (StaffSlryTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			System.out.println("Step -1 : Exception found generalmgntDaoServices.salarycreate() : "+ex);
			logwrite.logMessage("Step -1 : Exception found salarycreate() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			logwrite = null; qy = null;
		}
		return userdata;
	}

	@Override
	public int salarycreate(int locvrStaff_ID) {	 
		int count=0;		
		Session session = null;
		Query query = null;
		Log logwrite = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();			
			query = session.createQuery("SELECT count(staffSlryId)  FROM StaffSlryTblVO where staffID=" + locvrStaff_ID + "");
			count = ((Number) query.uniqueResult()).intValue();
		} catch (Exception ex) {			
			System.out.println("Step -1 : Exception found generalmgntDaoServices.salarycreate() : "+ex);
			logwrite.logMessage("Step -1 : Exception found salarycreate() : "+ex, "error", generalmgntDaoServices.class);		
		} finally {
			  if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			  query = null; logwrite = null;
		}
		 return count;
	}

	@Override
	public boolean saveSalaryInfo(StaffSlryTblVO locObjsalaryprfvo) {		
		Log log = null;
		Session session = null;
		Transaction tx = null;
		int count = 0;	
		Query query = null;
		try {
			  log = new Log();
			  session = HibernateUtil.getSession();
		      String qry = " SELECT count(STAFF_ID)  FROM StaffSlryTblVO where staffID=:STAFF_ID"+ " and status=:STATUS";;
		      query = session.createQuery(qry);
		      query.setInteger("STAFF_ID", locObjsalaryprfvo.getStaffID().getStaffID());
		      query.setInteger("STATUS", 1);
		      count = ((Number) query.uniqueResult()).intValue();
		      if(count>0){
		    	  Query qy = null;
		    	  try {
				      tx = session.beginTransaction();
				      qy = session.createQuery("update StaffSlryTblVO set extraWages= "+locObjsalaryprfvo.getExtraWages()+",monthlySalary="+locObjsalaryprfvo.getMonthlySalary()+"  where staffID="+ locObjsalaryprfvo.getStaffID().getStaffID() + "");
				      qy.executeUpdate();
				      tx.commit();
				      return true;
				    } catch (Exception ex) {
				      if (tx != null) {
				        tx.rollback();
				      }				     
				      return false;
				    }
		      } else if(count<=0 ){
					try {
						tx = session.beginTransaction();
						session.save(locObjsalaryprfvo);
						tx.commit();		
						 return true;
					} catch (Exception ex) {
						if (tx != null) {
							tx.rollback();
						}	
						return false;
					}
		       }else{
		    	   try {
						tx = session.beginTransaction();
						session.save(locObjsalaryprfvo);
						tx.commit();	
						 return true;
					} catch (Exception ex) {
						if (tx != null) {
							tx.rollback();
						}	
						return false;
					}
		       }
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception saveSalaryInfo() : "+e, "error", generalmgntDaoServices.class);
			return false;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if(tx!=null){tx=null;} query = null;			
		}		
	}

	@Override
	public StaffSlryTblVO editselectsalary(int locvrStaff_ID) {
		StaffSlryTblVO staffdata = new StaffSlryTblVO();
		Session session = null;
		Log logwrite = null;
		Query qy = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			qy = session.createQuery("From StaffSlryTblVO where staffID=:STAFF_ID");
			qy.setInteger("STAFF_ID", locvrStaff_ID);			
			staffdata = (StaffSlryTblVO) qy.uniqueResult();
		} catch (Exception ex) {			
			System.out.println("Step -1 : Exception found generalmgntDaoServices.editselectsalary() : "+ex);
			logwrite.logMessage("Step -1 : Exception found editselectsalary() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			logwrite = null; qy = null; 
		}
		return staffdata;
	}

	
	public int workalertcreate(int locvrStaff_ID) {		
		 int count=0;
		 Session session = null;
		 Query query = null;
		 Log logwrite = null;
		 try {
			logwrite = new Log();
			session = HibernateUtil.getSession();	
			String qry1 = " SELECT count(staffWrkId)  FROM StaffWrkTblVO where staffID="+ locvrStaff_ID + "";
			query = session.createQuery(qry1);
			count = ((Number) query.uniqueResult()).intValue();
		 } catch (Exception ex) {
			System.out.println("Step -1 : Exception found generalmgntDaoServices.workalertcreate() : "+ex);
			logwrite.logMessage("Step -1 : Exception found workalertcreate() : "+ex, "error", generalmgntDaoServices.class);
			count=0;
		 } finally {
			if (session != null) { session.flush(); session.clear(); session.close(); session = null; }
			logwrite = null;query = null;
		 }		   
		return count;
	}

	@Override
	public StaffWrkTblVO editselectworkalt(int locvrStaff_ID) {
		StaffWrkTblVO wkalertdata = null;
		Session session = null;
		Log logwrite = null;
		Query qy = null;
		String qry = null;
		try {
			logwrite = new Log();
			session = HibernateUtil.getSession();
			qry = "From StaffWrkTblVO where staffID=:STAFF_ID";
			qy = session.createQuery(qry);
			qy.setInteger("STAFF_ID", locvrStaff_ID);			
			wkalertdata = (StaffWrkTblVO) qy.uniqueResult();
		} catch (Exception ex) {
			System.out.println("Exception found generalmgntDaoServices.editselectworkalt() : "+ex);
			logwrite.logMessage("Exception found editselectworkalt() : "+ex, "error", generalmgntDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			logwrite = null; qy = null;
			qry = null;
		}
		return wkalertdata;
	}

	@Override
	public boolean saveWorkInfo(StaffWrkTblVO locObjworkprfvo) {
		Log log = null;
		Session session = null;
		Transaction tx = null;
		int count = 0;	
		Query query = null;
		try {
			  log = new Log();
			  session = HibernateUtil.getSession();
		      String qry = " SELECT count(staffID)  FROM StaffWrkTblVO where staffID=:STAFF_ID"+ " and status=:STATUS";		 		      
		      query = session.createQuery(qry);
		      query.setInteger("STAFF_ID", locObjworkprfvo.getStaffID().getStaffID());
		      query.setInteger("STATUS", 1);
		      count = ((Number) query.uniqueResult()).intValue();
			  SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");				
			  String gg=df.format(locObjworkprfvo.getWorkStartDate());
			  String gg1=df.format(locObjworkprfvo.getWorkEndDate());
		      if(count>0){// Update
		    	  try {
				      tx = session.beginTransaction();
				      Query qy = session.createQuery("update StaffWrkTblVO set workStartDate= '"+gg+"',workEndDate='"+gg1+"',workDtl='"+locObjworkprfvo.getWorkDtl()+"', weeklyoff='"+locObjworkprfvo.getWeeklyoff()+"' where staffID="+ locObjworkprfvo.getStaffID().getStaffID() + "");				    
				      qy.executeUpdate();
				      tx.commit();
				      return true;
				   } catch (Exception ex) {
				      if (tx != null) { tx.rollback(); }				    
				      return false;
				   }finally{
					   
				   }
		       } else if (count <= 0) {// Insert
		    	  try {
					tx = session.beginTransaction();
					session.save(locObjworkprfvo);
					tx.commit();
		    	  } catch (Exception ex) {
					if (tx != null) { tx.rollback(); }
		    	  }
		       } else{// Insert
				try {
					tx = session.beginTransaction();
					session.save(locObjworkprfvo);
					tx.commit();
		    	  } catch (Exception ex) {
					if (tx != null) { tx.rollback(); }
		    	  }
			}
		}catch (Exception e) {
		  if (tx != null) { tx.rollback(); }
		  System.out.println("Exception found in generalDaoservice : "+e);
		  log.logMessage("Exception : "+e, "error", generalmgmtDao.class);
		  return false;
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if(tx!=null){tx=null;}
			query = null;
		}
		return true;
	}

	@Override
	public StaffMasterTblVo getregistertable(int userid) {
		StaffMasterTblVo staffdata = new StaffMasterTblVo();
		Session session = HibernateUtil.getSession();
		Log logwrite =null;
		Query qy = null;
	    try {
	    	logwrite = new Log();
	    	session = HibernateUtil.getSession();
	    	String qry = " from StaffMasterTblVo where staffID=:	STAFF_ID";
	    	qy = session.createQuery(qry);
	    	qy.setInteger("STAFF_ID", userid);
	    	staffdata = (StaffMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	System.out.println("Step -1 :Exception found generalmgntDaoServices.getregistertable() : "+ex);
	    	logwrite.logMessage("Step -1 :Exception getregistertable() : "+ex, "error", generalmgntDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    	logwrite = null; qy = null;
	    }
	    return staffdata;
	}
	
	
	@Override
	public StaffMasterTblVo updatestaffmgnt(StaffMasterTblVo stafftable,String categ_st,String pstl_st) {
		StaffMasterTblVo stafflist = null;
		Session session = null;
		Transaction tx = null;
		String qry;
		Query qy = null;
		String lvrSltqry = null;
		Common lvrCmn = new CommonDao();
	      try {	 
	    	  stafflist = new StaffMasterTblVo();
	    	  session = HibernateUtil.getSession();
	    	  if (stafftable.getCompanyId()!=null) {
					lvrSltqry = "StaffMasterTblVo where staffMobno = '"+stafftable.getStaffMobno()+"' and  companyId = "+stafftable.getCompanyId().getCompanyId() + "and staffID <> "+stafftable.getStaffID();
			  } else {
					lvrSltqry = "StaffMasterTblVo where staffMobno = '"+stafftable.getStaffMobno()+"' and staffID <> "+stafftable.getStaffID();
			  }
	    	  Long  locnt = (Long) lvrCmn.getuniqueColumnVal(lvrSltqry,"count(*)","","");
			  Commonutility.toWriteConsole("locnt : "+locnt);
			  if (locnt>0) {
				  stafflist = null;
			  } else {
				  tx = session.beginTransaction();
					if (stafftable.getImageNameWeb() != null) {    		  					 
		    		  if(categ_st.equalsIgnoreCase("") || categ_st.equalsIgnoreCase("0") || !Commonutility.toCheckisNumeric(categ_st)){							
		    			  			qry = "update StaffMasterTblVo set companyId='"+stafftable.getCompanyId().getCompanyId()+"', staffName='"+ stafftable.getStaffName() + "', staffEmail='"+ stafftable.getStaffEmail() + "'," + " staffMobno='"
				  	                + stafftable.getStaffMobno() + "', staffGender='"+ stafftable.getStaffGender() + "', staffAddr='" +stafftable.getStaffAddr() +"'"
				  	                + ",iVOcradid="+ stafftable.getiVOcradid().getiVOcradid()+ ",staffIdcardno='" + stafftable.getStaffIdcardno() +"',staffCountry="+ stafftable.getStaffCountry()+""
				  	                + ",staffState="+ stafftable.getStaffState() +""+ ",staffCity="    + stafftable.getStaffCity()  +  ""+ ",ISDcode='"+stafftable.getISDcode()+"', pstlId="+pstl_st+""+ ",Workinghours="
				  	                + stafftable.getWorkinghours()+","+ " imageNameWeb='"+stafftable.getImageNameWeb()+"',imageNameMobile='"+stafftable.getImageNameWeb()+"'"		  	                			  	                	
			  	                 	+ " where staffID=" + stafftable.getStaffID()+ " ";
	    				 } else {	
	    					 System.out.println("else : "+stafftable.getImageNameWeb() );
	    					 		qry = "update StaffMasterTblVo set companyId='"+stafftable.getCompanyId().getCompanyId()+"', staffName='"+ stafftable.getStaffName() + "', staffEmail='"+ stafftable.getStaffEmail() + "'," + " staffMobno='"
	    			  	                + stafftable.getStaffMobno() + "', staffGender='"+ stafftable.getStaffGender() + "', staffAddr='" +stafftable.getStaffAddr() +"'"
	    			  	                + ",iVOcradid="+ stafftable.getiVOcradid().getiVOcradid() +""+ ",staffIdcardno='" + stafftable.getStaffIdcardno() +"',staffCountry="+ stafftable.getStaffCountry()+""
	    			  	                + ",staffState="+ stafftable.getStaffState() +""+ ",staffCity="    + stafftable.getStaffCity()  +  ""
	    			  	                + ",ISDcode='"+stafftable.getISDcode()+"',iVOstaffcategid="+Integer.parseInt(categ_st)+""+ ",pstlId="+pstl_st+""
	    			  	                + ",Workinghours="+stafftable.getWorkinghours()+","+ " imageNameWeb='"+stafftable.getImageNameWeb()+"',imageNameMobile='"+stafftable.getImageNameWeb()+"'"
	    		  	                	+ " where staffID=" + stafftable.getStaffID()+ " ";
	    				 }
					} else {    
						 System.out.println("222 else : "+stafftable.getImageNameWeb() );
						if (categ_st!=null && Commonutility.toCheckisNumeric(categ_st) && !categ_st.equalsIgnoreCase("0") && !categ_st.equalsIgnoreCase("")) {
							 System.out.println("222 else - if: "+stafftable.getImageNameWeb() );	
							qry = "update StaffMasterTblVo set companyId='"+stafftable.getCompanyId().getCompanyId()+"', staffName='"+ stafftable.getStaffName() + "', staffEmail='"+ stafftable.getStaffEmail() + "'," + " staffMobno='"
			  	                + stafftable.getStaffMobno() + "', staffGender='"+ stafftable.getStaffGender() + "', staffAddr='" +stafftable.getStaffAddr() +"'"
			  	                + ",iVOcradid="+ stafftable.getiVOcradid().getiVOcradid() +""+ ",staffIdcardno='" + stafftable.getStaffIdcardno() +"',staffCountry="+ stafftable.getStaffCountry()+""
			  	                + ",staffState="+ stafftable.getStaffState() +",staffCity=" + stafftable.getStaffCity()  +  ""+ ",ISDcode='"+stafftable.getISDcode()+"'"
			  	                + ",iVOstaffcategid="+Integer.parseInt(categ_st)+" ,pstlId="+pstl_st+""+ ",Workinghours="+stafftable.getWorkinghours()+""+ " where staffID="+ stafftable.getStaffID()+ "  ";
						} else {
							
							Integer VOcradid = null;
							if(stafftable.getiVOcradid() != null){
								VOcradid = stafftable.getiVOcradid().getiVOcradid();
							}
							
							 System.out.println("222 else - else: "+stafftable.getImageNameWeb() );
							 System.out.println("222 else - stafftable.getCompanyId(): "+stafftable.getCompanyId() );
//							 System.out.println("222 else - stafftable.getiVOcradid().getiVOcradid() : "+stafftable.getiVOcradid().getiVOcradid() );
								qry = "update StaffMasterTblVo set companyId='"+stafftable.getCompanyId().getCompanyId()+"', staffName='"+ stafftable.getStaffName() + "', staffEmail='"+ stafftable.getStaffEmail() + "',staffMobno='"
				  	             + stafftable.getStaffMobno() + "', staffGender='"+ stafftable.getStaffGender()+"', staffAddr='" +stafftable.getStaffAddr() +"'"
				  	             + ",iVOcradid="+ VOcradid+",staffIdcardno='" + stafftable.getStaffIdcardno() +"',staffCountry="+ stafftable.getStaffCountry()+""
				  	             + ",staffState="+ stafftable.getStaffState() +",staffCity="+ stafftable.getStaffCity()  + ",ISDcode='"+stafftable.getISDcode()+"'"
				  	             + ",pstlId="+pstl_st+",Workinghours="+stafftable.getWorkinghours() +" where staffID=" + stafftable.getStaffID()+ " ";
		    		  }	    		  
		    	  }	 
					System.out.println("qry : "+qry);
					qy = session.createQuery(qry);
					qy.executeUpdate();				
					tx.commit();
					stafflist = stafftable;
			  }			  	    	 		
	      } catch (Exception ex) {
	    	  ex.printStackTrace();
	    	  Commonutility.toWriteConsole("Exception found in "+getClass().getSimpleName()+".class updatestaffmgnt () : "+ex);
				if (tx != null) {
					tx.rollback();
				}
				
	      } finally {
	    	  if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    	  if (tx != null) {tx = null;} qy = null;
	      }
	    
	  
	    return stafflist;
	}
	
	@Override
	public List<BiometricUserInfoTblVO> getUserDetailList_like_limt(String qry, int startrowfrom, int totrow) {
		List<BiometricUserInfoTblVO> userdata = new ArrayList<BiometricUserInfoTblVO>();
		Session session = null;
		Query qy = null;
		try {
			Commonutility.toWriteConsole("getUserDetailList_like_limt"+qry);
			session = HibernateUtil.getSession();			
			qy = session.createQuery(qry);	
			userdata = qy.setFirstResult(startrowfrom).setMaxResults(totrow).list();
		} catch (HibernateException ex) {			
			Commonutility.toWriteConsole("Exception found in UuamDaoServices.class getUserDetailList_like() : " + ex);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}qy = null;
		}		
		return userdata;
	}
	
	
	
	@Override
	public List<BiometricUserInfoTblVO> getUserDetailList_like(String qry) {
		Commonutility.toWriteConsole("getUserDetailList_like"+qry);
		List<BiometricUserInfoTblVO> userdata = new ArrayList<BiometricUserInfoTblVO>();
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();			
			qy = session.createQuery(qry);	
			userdata = qy.list();
		} catch (HibernateException ex) {			
			Commonutility.toWriteConsole("Exception found in generalmgmtDaoService.class getUserDetailList_like() : " + ex);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}qy = null;
		}		
		return userdata;
	}
	
	
//      public String bioiNSERTUserInfo(){
//		
//		Connection conn = null;
//		BiometricDatabaseInfoTblVO databaseInfoTblVO = getBioMetricDB(4);
//		String dbURL = "jdbc:sqlserver://"+databaseInfoTblVO.getBioHostname()+":"+databaseInfoTblVO.getBioPort()+";databaseName="+databaseInfoTblVO.getBioDatabase()+";integratedSecurity=true;";
//		conn = DriverManager.getConnection(dbURL, databaseInfoTblVO.getBioUsername(), databaseInfoTblVO.getBioPassword());
//            
//        if (conn != null) {    
//        	
//		        
//		     try (   
//				
//				
//				 final Statement statement1 = conn.createStatement();
//			     final PreparedStatement insertStatement = 
//			     connection2.prepareStatement("insert into table2 values(?, ? , ? ,? ,? ,? ,? , ? , ? , ? , ? , ? , ? , ? , ? ,?)"))      //insert into
//			   {
//			       try (final ResultSet resultSet =
//			         statement1.executeQuery("select Id, societyId,ResidentId,ResidentName,MobileNo,Email,RecordStatus,Location,IsSendPushNotification,insert_date,DeviceLogId,UserId,LogDate,StatusCode,Duration,DeviceId,VerificationMode from biom_user_info"))
//			     {
//			        while (resultSet.next())
//			          {
//			            // Get the values from the table1 record
//			        	final int Id = resultSet.getInt("Id");
//			            final String societyId = resultSet.getString("societyId");
//			            final String ResidentId = resultSet.getString("ResidentId");
//			            final String ResidentName = resultSet.getString("ResidentName");
//			            final String MobileNo = resultSet.getString("MobileNo");
//			            final String Email = resultSet.getString("Email");
//			            final String RecordStatus = resultSet.getString("RecordStatus");
//			            final String Location = resultSet.getString("Location");
//			            final String IsSendPushNotification = resultSet.getString("IsSendPushNotification");
//			            final String insert_date = resultSet.getString("insert_date");
//			            final String DeviceLogId = resultSet.getString("DeviceLogId");
//			            final String LogDate = resultSet.getString("LogDate");
//			            final String StatusCode = resultSet.getString("StatusCode");
//			            final String Duration = resultSet.getString("Duration");
//			            final String DeviceId = resultSet.getString("DeviceId");
//			            final String VerificationMode = resultSet.getString("VerificationMode");
//			            
//			            // Insert a row with these values into table2
//			            insertStatement.clearParameters();
//			            insertStatement.setInt(1, Id);
//			            insertStatement.setString(2, societyId);
//			            insertStatement.setString(3, ResidentId);
//			            insertStatement.setString(4, ResidentName);
//			            insertStatement.setString(5, MobileNo);
//			            insertStatement.setString(6, Email);
//			            insertStatement.setString(7, RecordStatus);
//			            insertStatement.setString(8, Location);
//			            insertStatement.setString(9, IsSendPushNotification);
//			            insertStatement.setString(10, insert_date);
//			            insertStatement.setString(11, DeviceLogId);
//			            insertStatement.setString(12, LogDate);
//			            insertStatement.setString(13, StatusCode);
//			            insertStatement.setString(14, Duration);
//			            insertStatement.setString(15, DeviceId);
//			            insertStatement.setString(16, VerificationMode);
//			            insertStatement.executeUpdate();
//			          
//			         }
//			    }
//			}
//		
//	}
//}
}	
