package com.siservices.committeeMgmt.persistense;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;

public class committeeServices  implements committeeDao{

	@Override
	public UserMasterTblVo getCommitteeDetailView(int committeeId) {
		UserMasterTblVo userData = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
		
		try {
			String qry = "From UserMasterTblVo where  userId=:UNIQUE_ID and statusFlag=:STATUS_FLAG";
			Query qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", committeeId);
			qy.setInteger("STATUS_FLAG", 1);
			System.out.println("----------gettownship------------" + qy);
			userData = (UserMasterTblVo) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("getCommitteeDetailView Exception======" + ex);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return userData;
	}
	
	@Override
	public String userCreation(UserMasterTblVo userMaster) {
		 UserMasterTblVo userDetails = new UserMasterTblVo();
		 String result="";
		 System.out.println("=============================");
		 int count=0;
		 int mobCount=0;
		 List<UserMasterTblVo> userDetailsListmob = new ArrayList<UserMasterTblVo>();
		 int usercount=0;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    try {
		     /* String qry = " SELECT count(emailId)  FROM UserMasterTblVo where emailId=:EMAIL_Id"
				          + " and statusFlag=:STATUS_FLAG";;
		      System.out.println("unam---" +  userMaster.getEmailId());
		      System.out.println("Qry---" + qry);

		      Query query = session.createQuery(qry);
		      query.setString("EMAIL_Id", userMaster.getEmailId());
		      query.setInteger("STATUS_FLAG", 1);
		      System.out.println("=================ds=dd=============");
		       count = ((Number) query.uniqueResult()).intValue();
		       System.out.println("===============");
		      System.out.println("userDetails======" + count);
		      if(count>0){
		    	  result="EmailExist" ;
		      }*/
		    	  /*String qry1 = " SELECT count(mobileNo)  FROM UserMasterTblVo where mobileNo=:MOBILE_NO"
				          + " and statusFlag=:STATUS_FLAG";;*/
		      String qry1 = "from UserMasterTblVo  where mobileNo='"+userMaster.getMobileNo()+"' and societyId="+userMaster.getSocietyId().getSocietyId()+" and statusFlag=1";		      
		      Query query1 = session.createQuery(qry1);
		     /* query1.setString("MOBILE_NO", userMaster.getMobileNo());
		      query1.setInteger("STATUS_FLAG", 1);*/		    
		       userDetailsListmob = query1.list();		       
		       if(userDetailsListmob.size()>0){
		    	   result="mobnoExist" ;  
		       }
		       
			if (userDetailsListmob.size() == 0) {
				try {
					tx = session.beginTransaction();
					session.save(userMaster);
					tx.commit();
					result = "insertsuccess";
				} catch (HibernateException ex) {
					if (tx != null) {
						tx.rollback();
					}
					ex.printStackTrace();
					result = "error";
				}
			}
		    } catch (HibernateException ex) {
		      ex.printStackTrace();
		      System.out.println("==============userCreation====HibernateException="+ex);
		    } finally {
		    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		    }
		    return result;
		  }

	@Override
	public boolean committeeInfoUpdate(UserMasterTblVo userMaster,
			int townshipid, int societyid, int committeeid) {
		 boolean result = false;
		 Commonutility.toWriteConsole("Step 1 : Committee update will Start - Twonshipid : "+townshipid);
		 Commonutility.toWriteConsole("Step 2 : Committee update will Start - User / Committee Id : "+userMaster.getUserId());
		    try {
		      Session session = HibernateUtil.getSession();
		      Transaction tx = null;
		      tx=session.beginTransaction();
		      try {
		    	  String qry="update UserMasterTblVo set dob=:DOB,gender=:GENDER, roleId=:COMMITEE_ROLE_ID ,firstName=:F_NAME ,"
		    	  		+ "lastName=:L_NAME,accessChannel=:ACCESS_CHANNEL,emailId=:EMAIL_ID  where userId=:UNIQ_ID";		      
		      Query qy = session.createQuery(qry);		     
		      qy.setString("F_NAME", userMaster.getFirstName());
		      qy.setString("GENDER", userMaster.getGender());
		      qy.setString("EMAIL_ID", userMaster.getEmailId());
		      qy.setString("L_NAME", userMaster.getLastName());
		      qy.setString("DOB", userMaster.getDob());
		      qy.setInteger("COMMITEE_ROLE_ID", committeeid);
		      qy.setInteger("UNIQ_ID", userMaster.getUserId());
		      qy.setInteger("ACCESS_CHANNEL", userMaster.getAccessChannel());
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		     
		      } catch (HibernateException ex) {
		        if (tx != null) {
		          tx.rollback();
		        }		
		        Commonutility.toWriteConsole("Step -1 : Committee update Exeption found in "+getClass().getSimpleName()+".class : "+ex);
		        result = false;
		      } finally {
		    	  if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		      }
		    } catch (Exception ex) {
		    	 Commonutility.toWriteConsole("Step -2 : Committee update Exeption found in "+getClass().getSimpleName()+".class : "+ex);	
		    }
		    Commonutility.toWriteConsole("Step 4 : Committee update will End - result : "+result);			
		    return result;
	}

}
