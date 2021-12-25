package com.siservices.societyMgmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class societyMgmtDaoServices implements societyMgmtDao {
	//Singleton pattern [Start]
	private static societyMgmtDao ivrSdaosrc = new societyMgmtDaoServices();
	
	public static societyMgmtDao getInstanceSocymgmtdao() {
		return ivrSdaosrc;
	}
	//Singleton pattern [End]
	Log log=new Log();
	@Override
	public int getInitTotal(String sqlQuery) {		
		int totcnt = 0;
		Session session = null;Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQuery);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.logMessage("Exception found in getInitTotal Exception : "+ex, "error", societyMgmtDaoServices.class);	

		} finally {
			if(session!=null){session.flush(); session.clear(); session.close();session = null;} qy = null;
		}
		return totcnt;
	}

	@Override
	public int gettotalcount(String sql) {
		int totcnt = 0;
	    Session session = null;
	    Query qy = null;
	    try {
	    	session = HibernateUtil.getSession();
	        qy = session.createQuery(sql);
	        totcnt = ((Number) qy.uniqueResult()).intValue();	        
	    } catch (Exception ex) {	      
	      log.logMessage("Exception found in gettotalcount Exception : "+ex, "error", societyMgmtDaoServices.class);	
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close();session = null;} qy = null;
	    }
	    return totcnt;
	}
	
	@Override
	public int getTotalFilter(String sqlQueryFilter) {		
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			 log.logMessage("Exception found in gettotalcount Exception : "+ex, "error", societyMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close();session = null;} qy = null;
		}
		return totcnt;
	}
	
	@Override
	public List<SocietyMstTbl> getsocietyDetailList(String qry, int pstartrow, int totrow) {
		List<SocietyMstTbl> societyData = new ArrayList<SocietyMstTbl>();
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();			
			qy = session.createQuery(qry).setFirstResult(pstartrow).setMaxResults(totrow);				
			societyData = qy.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			 log.logMessage("Exception found in getsocietyDetailList Exception : "+ex, "error", societyMgmtDaoServices.class);			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close();session = null;} qy = null;
		}
		return societyData;
	}

	@Override
	public int societyInfoSave(SocietyMstTbl societyMst,UserMasterTblVo userInfo,String flatnames,MvpSocietyAccTbl societyAccData) {
		Session session = null;
		Transaction tx = null;
		Query qy = null;
		boolean result=false;
		int uniqueVal=-1;
		SocietyWingsDetailTbl societyWing=new SocietyWingsDetailTbl();
		int count = 0;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			String query = " SELECT count(societyName)  FROM SocietyMstTbl where societyName=:SOCIETY_NAME";	
			qy = session.createQuery(query);
			qy.setString("SOCIETY_NAME", societyMst.getSocietyName());
			count = ((Number) qy.uniqueResult()).intValue();	      
			if(count>0){
	    	  uniqueVal=-2;
			}
		}catch(HibernateException ex){			
			log.logMessage("Exception   societyInfoSave : "+ex, "error", societyMgmtDaoServices.class);
		}
		Date date;
		CommonUtils comutil=new CommonUtilsServices();
		date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		if ((count == 0)) {
			try {
				session.save(societyMst);
				userInfo.setSocietyId(societyMst);
				session.save(userInfo);
				uniqueVal = societyMst.getSocietyId();
				if (flatnames != null) {
					String spli[] = flatnames.split("!_!");
					for (int i = 0; i < spli.length; i++) {
						societyWing = new SocietyWingsDetailTbl();
						String temp = spli[i];
						if (Commonutility.checkempty(temp)) {
							societyWing.setSocietyId(societyMst);
							societyWing.setStatus(1);
							societyWing.setEnrtyBy(1);
							societyWing.setWingsName(temp);
							societyWing.setEntryDate(date);
							session.save(societyWing);
						}
					}
					}
					if(societyAccData!=null){						
						societyAccData.setSocietyId(societyMst);
						societyAccData.setUserId(userInfo);
						societyAccData.setEntryDatetime(date);
						societyAccData.setStatusFlag(1);
						societyAccData.setEntryBy(1);
						session.save(societyAccData);						
					}					
					tx.commit();
					
				} catch (Exception e) {
					if (tx != null) {
						tx.rollback();
					}
					System.out.println("Exception found in societyInfoSave : "+e);
					log.logMessage("Exception   societyInfoSave : "+e, "error", societyMgmtDaoServices.class);
					 uniqueVal=-3;					
				} finally {
					if(session!=null){session.flush(); session.clear();session.close();session=null;}
					if(tx!=null){tx=null;}
				}	
				}
		return uniqueVal;
	}

	@Override
	public SocietyMstTbl getSocietyDetailView(int societyid) {
		SocietyMstTbl societyData = null;
		Session session = null;
		Query qy = null;
		try {
			societyData = new SocietyMstTbl();
			session = HibernateUtil.getSession();	
			String qry = "From SocietyMstTbl where  societyId=:UNIQUE_ID and statusFlag=:STATUS_FLAG";
			qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", societyid);
			qy.setInteger("STATUS_FLAG", 1);			
			societyData = (SocietyMstTbl) qy.uniqueResult();
		} catch (HibernateException ex) {			
			System.out.println("Exception Found societyMgmtDaoServices.getSocietyDetailView : " + ex);
		} finally {
			if(session!=null){session.flush(); session.clear();session.close();session=null;}
		}
		return societyData;
	}

	@Override
	public int societyInfoUpdate(SocietyMstTbl societyMst,int townshipId,String flatnames,MvpSocietyAccTbl societyAccData,String emailId,int locGrpcode) {
		Session session = null;
		Transaction tx = null;
		boolean result=false;
		int count = 0,uniqueVal=-1;
		SocietyWingsDetailTbl societyWing=new SocietyWingsDetailTbl();
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			 String query = " SELECT count(societyName)  FROM SocietyMstTbl where societyName=:SOCIETY_NAME "
			 		+ "and societyId<>:SOCIETY_ID";	      
	      Query qy = session.createQuery(query);
	      qy.setString("SOCIETY_NAME", societyMst.getSocietyName());
	      qy.setInteger("SOCIETY_ID", societyMst.getSocietyId());
	       count = ((Number) qy.uniqueResult()).intValue();	      
	      if(count>0){
	    	  uniqueVal=-2;
	      }
		}catch(HibernateException ex){
			System.out.println("Exception Found in societyMgmtDaoServices.societyInfoUpdate() check 1 : "+ex);
		}finally{
			
		}
		String qry = null;
		 if((count==0)){
				try {
					session = HibernateUtil.getSessionFactory().openSession();
					tx = session.beginTransaction();
					 if(societyMst.getLogoImage()!=null && !societyMst.getLogoImage().equalsIgnoreCase("")&&
							 societyMst.getIcoImage().equalsIgnoreCase("")){
					 qry="update SocietyMstTbl set GSTIN=:GSTIN, HSN=:HSN, societyName=:SOCIETY_NAME,noOfMemebers=:NO_OF_MEMBERS,"
					    	  		+ "townshipId=:TOWNSHIP_ID,noOfBlocksWings=:NO_OF_BLOCKS_WINGS,registerNo=:REGISTER_NO,"
					    	  		+ "logoImage=:SOCIETY_LOGO_NAME,"
					    	  		+ "imprintName=:IMPRINT_NAME,colourCode=:COLOUR_CODE where societyId=:SOCIETY_ID";
					 }else if(societyMst.getIcoImage()!=null && !societyMst.getIcoImage().equalsIgnoreCase("")&&
							 societyMst.getLogoImage().equalsIgnoreCase("") ){
						 qry="update SocietyMstTbl set GSTIN=:GSTIN, HSN=:HSN, societyName=:SOCIETY_NAME,noOfMemebers=:NO_OF_MEMBERS,"
					    	  		+ "townshipId=:TOWNSHIP_ID,noOfBlocksWings=:NO_OF_BLOCKS_WINGS,registerNo=:REGISTER_NO,"
					    	  		+ "icoImage=:SOCIETY_ICO_NAME,"
					    	  		+ "imprintName=:IMPRINT_NAME,colourCode=:COLOUR_CODE where societyId=:SOCIETY_ID"; 
					 }else if(societyMst.getLogoImage()!=null && !societyMst.getLogoImage().equalsIgnoreCase("") &&
							 societyMst.getIcoImage()!=null && !societyMst.getIcoImage().equalsIgnoreCase("")){
						 qry="update SocietyMstTbl set GSTIN=:GSTIN, HSN=:HSN, societyName=:SOCIETY_NAME,noOfMemebers=:NO_OF_MEMBERS,"
					    	  		+ "townshipId=:TOWNSHIP_ID,noOfBlocksWings=:NO_OF_BLOCKS_WINGS,registerNo=:REGISTER_NO,"
					    	  		+ "logoImage=:SOCIETY_LOGO_NAME,icoImage=:SOCIETY_ICO_NAME,"
					    	  		+ "imprintName=:IMPRINT_NAME,colourCode=:COLOUR_CODE where societyId=:SOCIETY_ID"; 
					 }else{
						 qry="update SocietyMstTbl set GSTIN=:GSTIN, HSN=:HSN, societyName=:SOCIETY_NAME,noOfMemebers=:NO_OF_MEMBERS,"
					    	  		+ "townshipId=:TOWNSHIP_ID,noOfBlocksWings=:NO_OF_BLOCKS_WINGS,registerNo=:REGISTER_NO,"
					    	  		+ "imprintName=:IMPRINT_NAME,colourCode=:COLOUR_CODE where societyId=:SOCIETY_ID";
					 }
					
					Query qy = session.createQuery(qry);
					
					 qy.setString("GSTIN", societyMst.getGstin());
					 qy.setString("HSN", societyMst.getHsn());
					 
					 qy.setString("SOCIETY_NAME", societyMst.getSocietyName());
					 qy.setInteger("NO_OF_MEMBERS", societyMst.getNoOfMemebers());
				      qy.setInteger("SOCIETY_ID", societyMst.getSocietyId());
				      qy.setInteger("TOWNSHIP_ID", townshipId);
				      qy.setString("NO_OF_BLOCKS_WINGS", societyMst.getNoOfBlocksWings());
				      qy.setString("REGISTER_NO", societyMst.getRegisterNo());
				      qy.setString("IMPRINT_NAME", societyMst.getImprintName());
				      qy.setString("COLOUR_CODE", societyMst.getColourCode());
				      if(societyMst.getLogoImage()!=null && !societyMst.getLogoImage().equalsIgnoreCase("")){				    	 
				    	  qy.setString("SOCIETY_LOGO_NAME", societyMst.getLogoImage());
				      }
				      if(societyMst.getIcoImage()!=null && !societyMst.getIcoImage().equalsIgnoreCase("")){
				    	  qy.setString("SOCIETY_ICO_NAME", societyMst.getIcoImage());
				      }
				      qy.executeUpdate();
				     if(societyAccData!=null){
				    	 String qry3="update MvpSocietyAccTbl set bankName=:BANK_NAME,bankAccName=:BANK_ACC_NAME,"
				    	 		+ "ifscCode=:IFSC_CODE,bankAccNo=:ACC_NUMBER where societyId=:SOCIETY_ID";	
				    	 Query qy3 = session.createQuery(qry3);
				    	 qy3.setString("BANK_NAME", societyAccData.getBankName());
				    	 qy3.setString("BANK_ACC_NAME", societyAccData.getBankAccName());
				    	 qy3.setString("IFSC_CODE", societyAccData.getIfscCode());
				    	 qy3.setString("ACC_NUMBER", societyAccData.getBankAccNo());
				    	 qy3.setInteger("SOCIETY_ID", societyMst.getSocietyId());
				    	 qy3.executeUpdate();
				     }
				    	 String qry4="update UserMasterTblVo set emailId=:EMAIL_ID where societyId=:SOCIETY_ID"
				    	 		+ " and groupCode=:GROUP_CODE and statusFlag=:STATUS_FLAG";
					    	 Query qy4 = session.createQuery(qry4);
					    	 qy4.setString("EMAIL_ID", emailId);
					    	 qy4.setInteger("SOCIETY_ID", societyMst.getSocietyId());
					    	 qy4.setInteger("STATUS_FLAG", 1);
					    	 qy4.setInteger("GROUP_CODE", locGrpcode);
					    	 qy4.executeUpdate();
				    if(flatnames!=null){
				   String qry2="delete from SocietyWingsDetailTbl  where societyId=:SOCIETY_ID";
				   Query qy1 = session.createQuery(qry2);
				   qy1.setInteger("SOCIETY_ID", societyMst.getSocietyId());
				   qy1.executeUpdate();				 
				   if(flatnames!=null && flatnames.contains("!_!")){
					   String spli[] = flatnames.split("!_!");
					   for(int i=0;i<spli.length;i++){
							societyWing=new SocietyWingsDetailTbl();
							String temp=spli[i];
							if(Commonutility.checkempty(temp)){
							societyWing.setSocietyId(societyMst);
							societyWing.setStatus(1);
							societyWing.setEnrtyBy(1);
							CommonUtils comutil=new CommonUtilsServices();
							Date date;
							date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
							societyWing.setWingsName(temp);
							societyWing.setEntryDate(date);
							session.save(societyWing);
							}
						}
				   } else{
					   if(flatnames!=null && !flatnames.equalsIgnoreCase("null") && !flatnames.equalsIgnoreCase("")){
					    societyWing=new SocietyWingsDetailTbl();
						String temp=flatnames;
						societyWing.setSocietyId(societyMst);
						societyWing.setStatus(1);
						societyWing.setEnrtyBy(1);
						CommonUtils comutil=new CommonUtilsServices();
						Date date;
						date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
						societyWing.setWingsName(temp);
						societyWing.setEntryDate(date);
						session.save(societyWing);
					   }
				   }
				   
				   }
				    tx.commit();
				      uniqueVal=1;
				} catch (Exception e) {
					if (tx != null) {
						tx.rollback();
					}
					System.out.println("Exception found in societyInfoUpdate : "+e);
					log.logMessage("Exception   societyInfoUpdate : "+e, "error", societyMgmtDaoServices.class);
					 uniqueVal=-3;
					
				} finally {
					if(session!=null){session.flush();session.clear();session.close();session=null;}
					if(tx!=null){tx=null;}
				}	
		}
		return uniqueVal;
	}


	@Override
	public int delete(String socyteaid) {
		Session session = null;
		Transaction tx = null;
		int result=-1;
			session = HibernateUtil.getSessionFactory().openSession();
				try {
					session = HibernateUtil.getSessionFactory().openSession();
					tx = session.beginTransaction();
					
				    String qry4="update SocietyMstTbl set statusFlag=:STATUS where societyId=:SOCIETY_ID";
					Query qy4 = session.createQuery(qry4);
					qy4.setInteger("STATUS", 0);
					qy4.setInteger("SOCIETY_ID", Integer.parseInt(socyteaid));
					result = qy4.executeUpdate();
				    tx.commit();
				} catch (Exception e) {
					if (tx != null) {
						tx.rollback();
					}
					System.out.println("Exception found in delete socytea : "+e);
					log.logMessage("Exception delete socytea : "+e, "error", societyMgmtDaoServices.class);
					result=-3;
					
				} finally {
					if(session!=null){session.flush();session.clear();session.close();session=null;}
					if(tx!=null){tx=null;}
				}	
		return result;
	}

	
	@Override
	public boolean generateNewSocietyPassword(int societyuniId,
			SocietyMstTbl societydata, String activeKey) {
		boolean result=false;
		Session session = null;
		Transaction tx = null;
		try{
			
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			 String query = " update SocietyMstTbl set activationKey=:ACTIVATION_KEY,oldActivationKey=:OLD_ACTIVATION_KEY"
			 		+ " where  societyId=:UNIQ_ID";	     
	      Query qy = session.createQuery(query);
	      qy.setString("ACTIVATION_KEY", activeKey);
	      qy.setString("OLD_ACTIVATION_KEY", societydata.getActivationKey());
	      qy.setInteger("UNIQ_ID", societyuniId);
	      qy.executeUpdate();
	      tx.commit();
	      result=true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Exception found in societyInfoUpdate : "+e);
			log.logMessage("Exception   societyInfoUpdate : "+e, "error", societyMgmtDaoServices.class);
			result=false;		 
	}finally{
		if(session!=null){session.flush();session.clear();session.close();session=null;}
		if(tx!=null){tx=null;}
	}
		return result; 
	}

	@Override
	public UserMasterTblVo getSocietyPrintData(int societyid,int groupcode) {
		UserMasterTblVo userListData = null;
		Session session = null;		
		try {
			userListData = new UserMasterTblVo();
			session = HibernateUtil.getSession();	
			String qry = "From UserMasterTblVo where  societyId=:UNIQUE_ID and groupCode=:GROUP_CODE  and statusFlag=:STATUS_FLAG";
			Query qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", societyid);
			qy.setInteger("GROUP_CODE", groupcode);
			qy.setInteger("STATUS_FLAG", 1);
			userListData =  (UserMasterTblVo) qy.uniqueResult();
			//System.out.println("===========userListData=================="+userListData.getUserName());
			//System.out.println("===========userListData=================="+userListData.getSocietyId().getActivationKey());
		} catch (HibernateException ex) {			
			System.out.println("getSocietyPrintData HibernateException======" + ex);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return userListData;
	}

	@Override
	public List<SocietyWingsDetailTbl> getSocietyWingsDetailView(int societyid) {
		List<SocietyWingsDetailTbl> societyWingList = null;
		Session session = null;		
		try {
			session = HibernateUtil.getSession();
			String qry = "from SocietyWingsDetailTbl where  societyId="+societyid+" and status=1";
			//Query qy = session.createQuery(qry);
			//qy.setInteger("UNIQUE_ID", societyid);
			//qy.setInteger("STATUS_FLAG", 1);			
			//log.logMessage("Exception   getSocietyWingsDetailView : "+qy, "info", societyMgmtDaoServices.class);
			societyWingList =  (List<SocietyWingsDetailTbl>) session.createQuery(qry).list();
		} catch (HibernateException ex) {			
			System.out.println("Exception   getSocietyWingsDetailView : " + ex);
			log.logMessage("Exception   getSocietyWingsDetailView : "+ex, "error", societyMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return societyWingList;
	}

	@Override
	public MvpSocietyAccTbl getSocietyAccData(int societyid) {
		MvpSocietyAccTbl societyAcc=new MvpSocietyAccTbl();
		Session session = null;
		
		try {
			session = HibernateUtil.getSession();
			String qry = "From MvpSocietyAccTbl where  societyId=:UNIQUE_ID and statusFlag=:STATUS_FLAG";
			Query qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", societyid);
			qy.setInteger("STATUS_FLAG", 1);			
			societyAcc = (MvpSocietyAccTbl) qy.uniqueResult();
		} catch (HibernateException ex) {			
			log.logMessage("Exception   getSocietyAccData : "+ex, "error", societyMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return societyAcc;
	}

	@Override
	public UserMasterTblVo getSocietyUserRegisterData(int societyid, int grpCode) {
		UserMasterTblVo lvruamvo =null;
		Session session = null;		
		try {		
			session = HibernateUtil.getSession();
			String qry = "from UserMasterTblVo where societyId="+societyid+" and groupCode="+grpCode+" and statusFlag=1";
			Query qy1 = session.createQuery(qry);			
			lvruamvo = (UserMasterTblVo) qy1.uniqueResult();
			return lvruamvo;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception   getSocietyUserRegisterData : " + ex);
			log.logMessage("Exception   getSocietyUserRegisterData : "+ex, "error", societyMgmtDaoServices.class);
			return lvruamvo;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		
	}

	@Override
	public String getsocietyemailid(int societyid, int grpcode) {
		String email="";
		Session session = null;
		
		try {
			session = HibernateUtil.getSession();
			String qry = "select emailId From UserMasterTblVo where  societyId=:SOCIETY_ID and groupCode=:GRP_CODE";
			Query qy = session.createQuery(qry);
			qy.setInteger("SOCIETY_ID", societyid);
			qy.setInteger("GRP_CODE", grpcode);
			//qy.setInteger("STATUS_FLAG", 1);			
			email = (String) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("Exception   getsocietyemailid : " + ex);
			log.logMessage("Exception   getsocietyemailid : "+ex, "error", societyMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
		}
		return email;
	}
	
	@Override
	  public String getactiveKey(String query) {
	    // TODO Auto-generated method stub
	    String geactivekeyGroup = "";
	    Session session = HibernateUtil.getSession();
	    try {
	      Query qy = session.createQuery(query);
	      geactivekeyGroup = qy.uniqueResult().toString();
	      System.out.println("getexistGroup---------" + geactivekeyGroup);
	    } catch (HibernateException ex) {
	      ex.printStackTrace();
	      log.logMessage("Exception in existGroupName()----> " + ex, "error", societyMgmtDaoServices.class);
	    } finally {
	      session.close();
	    }
	    return geactivekeyGroup;
	  }

	@Override
	public int socetyid(String socetysql) {
		 // TODO Auto-generated method stub
	    int socetyid = 0;
	    Session session = HibernateUtil.getSession();
	    try {
	      Query qy = session.createQuery(socetysql);
	      socetyid = ((Number) qy.uniqueResult()).intValue();			
	      System.out.println("socetyid---------" + socetyid);
	    } catch (HibernateException ex) {
	      ex.printStackTrace();
	      log.logMessage("Exception in existGroupName()----> " + ex, "error", societyMgmtDaoServices.class);
	    } finally {
	      session.close();
	    }
	    return socetyid;
	}


}
