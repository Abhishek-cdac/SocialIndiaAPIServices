package com.siservices.signup.persistense;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;

public class signUpDaoServices implements signUpDao {

	@Override
	public boolean checkActiveKey(String activeKey) {
		ActivationKeyVO activekey = new ActivationKeyVO();
		boolean result = false;
		    Session session = HibernateUtil.getSession();
		    try {
		      String qry = "From ActivationKeyVO  where statusFlag=:STATUS_FLAG and  activationKey=:ACTIVATION_KEY";
		      Query qy = session.createQuery(qry);
		      qy.setString("ACTIVATION_KEY", activeKey);
		      qy.setInteger("STATUS_FLAG", 1);
		      activekey = (ActivationKeyVO) qy.uniqueResult();
		      
		     
		    } catch (HibernateException ex) {
		      ex.printStackTrace();
		      result = false;
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session=null;}
		    }
		    if(activekey!=null){
		    	result = true;  
		      }else{
		    	  result = false;
		      }
		    return result;
		  }

	@Override
	public String saveUserInfo(UserMasterTblVo userMaster) {
		/*Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			System.out.println("=======fddf=sf=====");
			tx = session.beginTransaction();
			session.save(userMaster);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
		return true;*/
		 UserMasterTblVo userDetails = new UserMasterTblVo();
		 String result="";
		 int count=0;
		 int mobCount=0;
		 int usercount=0;
		    Session session = null;
		    Transaction tx = null;
		    try {
		    	session = HibernateUtil.getSession();
		      String qry = " SELECT count(emailId)  FROM UserMasterTblVo where emailId=:EMAIL_Id"
				          + " and statusFlag=:STATUS_FLAG";;
		      System.out.println("unam---" +  userMaster.getEmailId());
		      System.out.println("Qry---" + qry);

		      Query query = session.createQuery(qry);
		      query.setString("EMAIL_Id", userMaster.getEmailId());
		      query.setInteger("STATUS_FLAG", 1);
		       count = ((Number) query.uniqueResult()).intValue();
		      if(count>0){
		    	  result="EmailExist" ;
		      }
		    	  String qry1 = " SELECT count(mobileNo)  FROM UserMasterTblVo where mobileNo=:MOBILE_NO"
				          + " and statusFlag=:STATUS_FLAG";;		      
		      Query query1 = session.createQuery(qry1);
		      query1.setString("MOBILE_NO", userMaster.getMobileNo());
		      query1.setInteger("STATUS_FLAG", 1);
		      mobCount = ((Number) query1.uniqueResult()).intValue();
		       if(mobCount>0){
		    	   result="mobnoExist" ;  
		       }
		       String qry2 = " SELECT count(userName)  FROM UserMasterTblVo where userName=:USER_NAME"
				          + " and statusFlag=:STATUS_FLAG";;		      
		      Query query2 = session.createQuery(qry2);
		      query2.setString("USER_NAME", userMaster.getUserName());
		      query2.setInteger("STATUS_FLAG", 1);		      
		      usercount = ((Number) query2.uniqueResult()).intValue();		      
		       if(usercount>0){
		    	   result="userExist" ;  
		       }
		       if((count==0) && (mobCount==0) && (usercount==0)){
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
		    	System.out.println("Exception found signUpDaoService saveUserInfo() : "+ex);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session=null;}
		    }		   
		    return result;
	}

	@Override
	public boolean userProfileUpdate(UserMasterTblVo userMaster, String countryid,
			String stateid, String cityid, String postalcode, String idcardtype) {
		 	boolean result = false;				
		    try {
		      Session session = HibernateUtil.getSession();
		      Transaction tx = null;
		      tx=session.beginTransaction();
		      String qry="";
		      try {
		    	  if(userMaster.getImageNameMobile()!=null && userMaster.getImageNameWeb()!=null){
		    		   qry="update UserMasterTblVo set firstName=:F_NAME,lastName=:L_NAME, dob=:DOB,gender=:GENDER,"
				    	  		+ "iVOcradid=:ID_CARD_TYPE,idProofNo=:ID_PROOF_NO ,flatNo=:FLAT_NO , occupation=:OCCUPATION,"
				    	  		+ "bloodType=:BLOOD_TYPE,membersInFamily=:FAMILY_MEMBERS,countryId=:COUNTRY_CODE ,"
				    	  		+ "imageNameWeb=:WEB_IMAGE,imageNameMobile=:MOBILE_IMAGE,noofflats=:NO_OF_FLATS,accessChannel=:ACCESSMEDIA,"
				    	  		+ "stateId=:STATE_CODE,cityId=:CITY_CODE,pstlId=:POSTAL_CODE,address1=:ADDRESS1,"
				    	  		+ "address2=:ADDRESS2  where userId=:UNIQ_ID"; 
		    	  }else{
		    		  qry="update UserMasterTblVo set firstName=:F_NAME,lastName=:L_NAME, dob=:DOB,gender=:GENDER,"
		    	  		+ "iVOcradid=:ID_CARD_TYPE,idProofNo=:ID_PROOF_NO ,flatNo=:FLAT_NO , occupation=:OCCUPATION,"
		    	  		+ "bloodType=:BLOOD_TYPE,membersInFamily=:FAMILY_MEMBERS,countryId=:COUNTRY_CODE ,"
		    	  		+ "stateId=:STATE_CODE,cityId=:CITY_CODE,pstlId=:POSTAL_CODE,address1=:ADDRESS1,noofflats=:NO_OF_FLATS,accessChannel=:ACCESSMEDIA,"
		    	  		+ "address2=:ADDRESS2  where userId=:UNIQ_ID";
		    	  }		       
		      Query qy = session.createQuery(qry);		      
		      qy.setInteger("UNIQ_ID", userMaster.getUserId());
		      qy.setString("F_NAME", userMaster.getFirstName());
		      qy.setString("L_NAME", userMaster.getLastName());
		      qy.setString("DOB", userMaster.getDob());
		      qy.setString("GENDER", userMaster.getGender());
		      if(userMaster.getImageNameMobile()!=null){
		    	  qy.setString("MOBILE_IMAGE", userMaster.getImageNameMobile());
		      } if(userMaster.getImageNameWeb()!=null){
		    	  qy.setString("WEB_IMAGE", userMaster.getImageNameWeb());
		      }
		     qy.setString("ID_PROOF_NO", userMaster.getIdProofNo());
		     qy.setString("FLAT_NO", userMaster.getFlatNo());
		      String comqry="";
		      qy.setString("OCCUPATION", userMaster.getOccupation());
		      qy.setString("BLOOD_TYPE", userMaster.getBloodType());
		      qy.setInteger("FAMILY_MEMBERS", userMaster.getMembersInFamily());
		      qy.setInteger("NO_OF_FLATS", userMaster.getNoofflats());
		      qy.setInteger("ACCESSMEDIA", userMaster.getAccessChannel());
		     
		      
		      if(!Commonutility.toCheckisNumeric(idcardtype)|| idcardtype.equalsIgnoreCase("0")){
		    	 
		    	  qy.setString("ID_CARD_TYPE", null);
				 }else{
					 int idtype=Integer.parseInt(idcardtype);
					  qy.setInteger("ID_CARD_TYPE", idtype);
				 }
		     
		      if(!Commonutility.toCheckisNumeric(countryid) || countryid.equalsIgnoreCase("0")){
		    	  qy.setString("COUNTRY_CODE", null);
				 }else{
					 int countrycode=Integer.parseInt(countryid);
					  qy.setInteger("COUNTRY_CODE", countrycode);
				 }			     
		      if(!Commonutility.toCheckisNumeric(stateid) || stateid.equalsIgnoreCase("0")){
		    	  qy.setString("STATE_CODE", null);
				 }else{
					 int statecode=Integer.parseInt(stateid);
					 qy.setInteger("STATE_CODE", statecode);
				 }	
		    
		      if(!Commonutility.toCheckisNumeric(cityid) || cityid.equalsIgnoreCase("0")){
		    	  qy.setString("CITY_CODE", null);
				 }else{
					  int citycode=Integer.parseInt(cityid);	
					  qy.setInteger("CITY_CODE", citycode);
				 }	
		      if(!Commonutility.toCheckisNumeric(postalcode) || postalcode.equalsIgnoreCase("0")){
		    	  qy.setString("POSTAL_CODE",null);
				 }else{
					 int pin=Integer.parseInt(postalcode);
					  qy.setInteger("POSTAL_CODE", pin);
				 }	
		    
		     
		      qy.setString("ADDRESS1", userMaster.getAddress1());
		      qy.setString("ADDRESS2", userMaster.getAddress2());		      
		      qy.executeUpdate();
		      	tx.commit();
		      	result = true;
		     
		      } catch (HibernateException ex) {
		        if (tx != null) {
		          tx.rollback();
		        }		        
		        result = false;
		      } finally {
		    	  if(session!=null){session.flush();session.clear();session.close();session=null;}
		      }
		    } catch (Exception ex) {
		    	  System.out.println("Exception found signUpDaoService userProfileUpdate() : "+ex);
		    }
		  
		    return result;
	}
	  
}
