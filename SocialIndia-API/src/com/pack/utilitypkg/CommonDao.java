package com.pack.utilitypkg;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class CommonDao implements Common {

	@Override
	public boolean commonUpdate(String sql) {
		boolean locRtn= false;
			Session session = null;
			Transaction tx = null;
			Query lvrQryobj = null;
			try {
				session = HibernateUtil.getSession();
				tx = session.beginTransaction();
				lvrQryobj = session.createQuery(sql);
				 int lvrEuprst = lvrQryobj.executeUpdate();
				tx.commit();
				if (lvrEuprst>=1) {
					locRtn= true;	
				} else {
					locRtn= false;	
				}						
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
	public String commonDelete(int sno, String tblpojoname, String whr) {
		String pojonam = "";
		String wherecolumn = "";
		Query q = null;

		pojonam = tblpojoname;
		wherecolumn = whr;
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			q = session.createQuery("delete from " + pojonam + " where " + wherecolumn + "=" + sno + "");
			q.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			// log.error("Exception Common Update Query - "+q+" \t- Exception-"+e);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return "Successfully Deleted";
	}

	
	public UserMasterTblVo commoncheck( String emailrMobile) {
			boolean result=false;
			UserMasterTblVo userDetails = null;
		    Session session = null;
		    try {
		    	session = HibernateUtil.getSession();
		    	String qry = " from UserMasterTblVo where (emailId=:EMAIL_MOBILE" + " or mobileNo=:EMAIL_MOBILE)  and " +" statusFlag=:STATUS_FLAG";		     		    

		    	Query query = session.createQuery(qry);
		    	query.setString("EMAIL_MOBILE", emailrMobile);		      
		    	query.setInteger("STATUS_FLAG", 1);
		    	userDetails = (UserMasterTblVo) query.list().get(0);
		    	//userDetails = (UserMasterTblVo) query.uniqueResult();		     		     
		     /* if(userDetails!=null){
		    	  result=true;  
		      }else{
		    	  result=false;
		      }*/
		    } catch (Exception ex) {		      
		      result=false;
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session=null;}
		    }			
		    return userDetails;	
	}
	
	public boolean checkusername(String oldpassword,int userid){
			boolean result=false;
		 	UserMasterTblVo userDetails = null;
		    Session session = null;
		    Query query = null;
		    try {
		    	userDetails = new UserMasterTblVo();
		    	session = HibernateUtil.getSession();
		    	String qry = " from UserMasterTblVo where password=:OLD_PASSWORD"
		    		  	 + " and userId=:USER_ID and"
		    		     +" statusFlag=:STATUS_FLAG";		     		  
		    	query = session.createQuery(qry);
		    	query.setString("OLD_PASSWORD", oldpassword);
		    	query.setInteger("STATUS_FLAG", 1);
		    	query.setInteger("USER_ID", userid);
		    	userDetails = (UserMasterTblVo) query.uniqueResult();		     
		    	if (userDetails!=null) {
		    		result=true;  
		    	} else {
		    		result=false;
		    	}
		    } catch (HibernateException ex) {		      
		    	result=false;
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session=null;}
		    }			
		    return result;			
	}
	
	@Override
	public Object getuniqueColumnVal(String tblname, String sltcolname, String wherecolumn, String val) {
		 Log log = new Log();
		 Object retval = null;
		 Session session = null;
		 Query qy = null;
	    try {
	    	session = HibernateUtil.getSession();	      
	    	if((wherecolumn!=null && !wherecolumn.equalsIgnoreCase("") && !wherecolumn.equalsIgnoreCase("null")) &&(val!=null && !val.equalsIgnoreCase("") && !val.equalsIgnoreCase("null"))){
	    		String qry = "select " + sltcolname + " from " + tblname + " where " + wherecolumn + "=:PVALUE";
	    		qy = session.createQuery(qry);
	    		qy.setString("PVALUE", val);
	    	  
	    	} else {
	    	  String qry = "select " + sltcolname + " from " + tblname + "";
	    	  qy = session.createQuery(qry);
	    	}	      
	    	qy.setMaxResults(1);
	    	retval = (Object) qy.uniqueResult();	      
	    } catch (Exception ex) {	    
	      Commonutility.toWriteConsole("Exeption found in getuniqueColumnVal:" + ex);
	      log.logMessage("Exception in getSinglevalfromtbl()--->" + ex, "error", CommonDao.class);
	      retval = null;
	    }finally{
	    	if(session!=null){session.flush();session.clear();session.close();session=null;}qy = null;
	    }
	    return retval;
	  }

	@Override
	public String getDateobjtoFomatDateStr(Date pDataobj, String pDateformat) {
		SimpleDateFormat locSimpFrmt=null;
		String locRtnDatestr=null;
		try{
			if(pDataobj!=null){
				if(pDateformat!=null && !pDateformat.equalsIgnoreCase("null") && !pDateformat.equalsIgnoreCase("")){
					locSimpFrmt=new SimpleDateFormat(pDateformat);
				}else{
					locSimpFrmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}				
				locRtnDatestr=locSimpFrmt.format(pDataobj);
			}else{
				locRtnDatestr="";
			}
			
			return locRtnDatestr;
		}catch(Exception e){
			locRtnDatestr="";
			return locRtnDatestr;
		}finally{
			locSimpFrmt=null;locRtnDatestr=null;
		}						
	}

	@Override
	public String getGroupcodeexistornew(String pGrpName) {
		GroupMasterTblVo locGrpMstrVOobj =null;
		String locSlqry = null;
		Query locQryrst = null;
		Session locSession = null; 
		String lvrGrpcode = null;
		Log logWrite = null;
		try{
			locSession = HibernateUtil.getSession();
			logWrite = new Log();
			 // Select Group Code on Labor	
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";			 
			 locQryrst=locSession.createQuery(locSlqry);			
			 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
			 if(locGrpMstrVOobj!=null){
				 lvrGrpcode = Commonutility.toCheckNullEmpty(locGrpMstrVOobj.getGroupCode());				 			
			 }else{// new group create
				 uamDao uam=new uamDaoServices();
				 int locGrpcode = uam.createnewgroup_rtnId(pGrpName);
				 lvrGrpcode = Commonutility.toCheckNullEmpty(locGrpcode);				 			 			
			 }
			 return lvrGrpcode;
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found CommonDao.getGroupcodeexistornew() : "+e);
			logWrite.logMessage("Exception found CommonDao.getGroupcodeexistornew() : "+e, "error", CommonDao.class);
			lvrGrpcode = "error";
			return lvrGrpcode;
		}finally{
			if(locSession!=null){locSession.flush();locSession.clear();locSession.close(); locSession= null;}
			logWrite = null;
		}		
	}

	@Override
	public String commonLarmctDelete(int sno, String tblpojoname, String whr,int pGrpNme) {
			String pojonam = "";
			String wherecolumn = "";
			Query q = null;

			pojonam = tblpojoname;
			wherecolumn = whr;
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				q = session.createQuery("delete from " + pojonam + " where " + wherecolumn + "=" + sno + "and ivrGrpcodeobj="+pGrpNme);
				q.executeUpdate();
				tx.commit();
			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace();
				// log.error("Exception Common Update Query - "+q+" \t- Exception-"+e);
			} finally {
				if(session!=null){session.flush();session.clear();session.close();session=null;}
			}
			return "Successfully Deleted";
	}

	
	
}
