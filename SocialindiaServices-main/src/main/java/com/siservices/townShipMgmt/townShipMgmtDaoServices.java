package com.siservices.townShipMgmt;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.contactusreportissuevo.ReportIssueTblVO;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.residentvo.UsrUpldfrmExceFailedTbl;
import com.pack.utilitypkg.Commonutility;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class townShipMgmtDaoServices  implements townShipMgmtDao{
	Log log=new Log();
	@Override
	public int getInitTotal(String sqlQuery) {		
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQuery);
			totcnt = ((Number) qy.uniqueResult()).intValue();		
		} catch (Exception ex) {			
			System.out.println("Exceeption found in townShipMgmtDaoServices.getInitTotal() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getInitTotal() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}
		return totcnt;
	}
	
	

	@Override
	public int gettotalcount(String sql) {		
		int totcnt = 0;
	    Session session = null;Query qy = null;
	    try {
	    	session = HibernateUtil.getSession();
	        qy = session.createQuery(sql);
	        totcnt = ((Number) qy.uniqueResult()).intValue();	        
	    } catch (Exception ex) {
	    	System.out.println("Exceeption found in townShipMgmtDaoServices.gettotalcount() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.gettotalcount() : "+ex, "error", townShipMgmtDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
	    }
	    return totcnt;
	}
	
	@Override
	public int getTotalFilter(String sqlQueryFilter) {		
		int totcnt = 0;
		Session session = null;Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {			
			System.out.println("Exceeption found in townShipMgmtDaoServices.getTotalFilter() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getTotalFilter() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}
		return totcnt;
	}
	
	@Override
	public List<TownshipMstTbl> getTownShipDetailList() {
		List<TownshipMstTbl> townShipData = null;
		Session session = null;
		Query qy = null;
		try {
			townShipData = new ArrayList<TownshipMstTbl>();
			session = HibernateUtil.getSession();
			String qry = "From TownshipMstTbl";
			qy = session.createQuery(qry);			
			townShipData = qy.list();
		} catch (HibernateException ex) {
			System.out.println("Exceeption found in townShipMgmtDaoServices.getTownShipDetailList() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getTownShipDetailList() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}
		return townShipData;
	}

	@Override
	public List<TownshipMstTbl> getTownShipDetailListPagenation(String qry,int prmStrow, int prmTotrow) {
		List<TownshipMstTbl> townShipData = null;
		Session session = null;
		Query qy = null;
		try {
			townShipData = new ArrayList<TownshipMstTbl>();
			session = HibernateUtil.getSession();
//			String qry = "From TownshipMstTbl";
			System.out.println("qry----------- "+qry);
			qy = session.createQuery(qry);			
			townShipData = qy.setFirstResult(prmStrow).setMaxResults(prmTotrow).list();
		} catch (HibernateException ex) {
			System.out.println("Exceeption found in townShipMgmtDaoServices.getTownShipDetailList() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getTownShipDetailList() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}
		return townShipData;
	}
	
	@Override
	public int townShipInfoSave(TownshipMstTbl township) {
		Session session = null;
		Transaction tx = null;		
		int uniqueVal=-1;
		int count = 0,countmob=0;			
		try{
			 session = HibernateUtil.getSessionFactory().openSession();
			 tx = session.beginTransaction();	
			 String query = " SELECT count(townshipName)  FROM TownshipMstTbl where townshipName=:TOWN_SHIP_NAME_CHECK";			 
			 Query qy = session.createQuery(query);
			 qy.setString("TOWN_SHIP_NAME_CHECK", township.getTownshipName());			 
			 count = ((Number) qy.uniqueResult()).intValue();	       
	      if(count>0){
	    	  uniqueVal=-2 ;
	      }
			
		}catch(HibernateException ex){
			System.out.println("Exceeption found in townShipMgmtDaoServices.townShipInfoSave() cxheck1 : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.townShipInfoSave() cxheck1 : "+ex, "error", townShipMgmtDaoServices.class);
		}
		try{
			 String query1 = " SELECT count(mobileNo)  FROM TownshipMstTbl where mobileNo=:TOWN_SHIP_NAME_MOBILE";			 
			 Query qy1 = session.createQuery(query1);
			 qy1.setString("TOWN_SHIP_NAME_MOBILE", township.getMobileNo());			 
			 countmob = ((Number) qy1.uniqueResult()).intValue();			 			
			 if (countmob>0) {
				 uniqueVal=-3 ;
			 }
			
		} catch(HibernateException ex) {
			System.out.println("Exceeption found in townShipMgmtDaoServices.townShipInfoSave() cxheck2 : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.townShipInfoSave() cxheck2 : "+ex, "error", townShipMgmtDaoServices.class);
		}
		if((count==0) && (countmob==0)){		
			try {						
				session.save(township);
				uniqueVal=township.getTownshipId();				
				tx.commit();
			} catch (Exception e) {
				if (tx != null) {tx.rollback();}
				System.out.println("Exception found in LaborDaoservice : "+e);
				log.logMessage("Exception : "+e, "error", LaborDaoservice.class);
				return uniqueVal=-1;
			
			} finally {
				if(session!=null){session.flush();session.clear();session.close();session = null;}
				if(tx!=null){tx=null;}
		}	
		} else{
			if(session!=null){session.flush();session.clear();session.close();session = null;}
		}
		return uniqueVal;
	}

	@Override
	public TownshipMstTbl getTownShipDetailView(String townshipuniId) {
		TownshipMstTbl townShipData = new TownshipMstTbl();
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();			
			String qry = "From TownshipMstTbl where  townshipId=:UNIQUE_ID";
			qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", Integer.parseInt(townshipuniId));			
			townShipData = (TownshipMstTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			System.out.println("Exceeption found in townShipMgmtDaoServices.getTownShipDetailView() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getTownShipDetailView() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}
		return townShipData;
	}

	@Override
	public int townShipEditUpdate(TownshipMstTbl township, int townuniqIdold,String countryCode,String stateCode,String cityCode,String postalCode) {
		Session session = null;
		Transaction tx = null;		
		int uniqueVal=-1;
		int count = 0,countmob=0;
		Query qySlqry = null, qy1 =null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			String query = " SELECT count(townshipName)  FROM TownshipMstTbl where townshipName=:TOWN_SHIP_NAME_CHECK"
			 		+ " and  townshipId<>:UNIQ_ID";	      
			qySlqry = session.createQuery(query);
			qySlqry.setString("TOWN_SHIP_NAME_CHECK", township.getTownshipName());
			qySlqry.setInteger("UNIQ_ID", townuniqIdold);	      
			count = ((Number) qySlqry.uniqueResult()).intValue();	       
			if(count>0){
	    	  uniqueVal=-2 ;
			}		  
		}catch(HibernateException ex){
			System.out.println("Exceeption found in townShipMgmtDaoServices.townShipEditUpdate() check 1: "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.townShipEditUpdate() check 1 : "+ex, "error", townShipMgmtDaoServices.class);
		}finally{			
			qySlqry = null;
		}
		try{
			 String query1 = " SELECT count(mobileNo)  FROM TownshipMstTbl where mobileNo=:TOWN_SHIP_NAME_MOBILE and townshipId<>:UNIQ_ID";	      
			 qy1 = session.createQuery(query1);
			 qy1.setString("TOWN_SHIP_NAME_MOBILE", township.getMobileNo());
			 qy1.setInteger("UNIQ_ID", townuniqIdold);			 
			 countmob = ((Number) qy1.uniqueResult()).intValue();			 
	      if(countmob>0){
	    	  uniqueVal=-3 ;
	      }
			
		}catch(HibernateException ex){
			System.out.println("Exceeption found in townShipMgmtDaoServices.townShipEditUpdate() check 2: "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.townShipEditUpdate() check 2 : "+ex, "error", townShipMgmtDaoServices.class);
		}
		if(count==0 && countmob==0){
			Query qy = null;
			try{
				String qry;
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();			 
				if(township.getTownshiplogoname()!=null && !township.getTownshiplogoname().equalsIgnoreCase("") && township.getTownshipiconame().equalsIgnoreCase("")) {					
					qry="update TownshipMstTbl set townshipName=:TOWNSHIP_NAME,noOfSociety=:NO_OF_SOCIETY, noOfFlats=:NO_OF_FLATS ,"
					 		+ "townshipcolourcode=:COLOUR_CODE ,countryId=:COUNTRY_CODE ,stateId=:STATE_CODE,cityId=:CITY_CODE,"
					 		+ "builderName=:BUILDER_NAME,isdCode=:ISD_CODE,mobileNo=:MOBILE_NO,landMark=:LAND_MARK,emailId=:EMAIL_ID,imprintName=:IMPRINT_NAME,"
					 		+ "pstlId=:POSTAL_CODE,address=:ADDRESS,townshiplogoname=:TOWNSHIP_LOGO_NAME"
				    	  		+ " where townshipId=:UNIQ_ID_OLD"; 
				} else if (township.getTownshipiconame()!=null && !township.getTownshipiconame().equalsIgnoreCase("") && township.getTownshiplogoname().equalsIgnoreCase("")) {				  
					qry="update TownshipMstTbl set townshipName=:TOWNSHIP_NAME,noOfSociety=:NO_OF_SOCIETY, noOfFlats=:NO_OF_FLATS ,"
					 		+ "townshipcolourcode=:COLOUR_CODE ,countryId=:COUNTRY_CODE ,stateId=:STATE_CODE,cityId=:CITY_CODE,"
					 		+ "builderName=:BUILDER_NAME,isdCode=:ISD_CODE,mobileNo=:MOBILE_NO,landMark=:LAND_MARK,emailId=:EMAIL_ID,imprintName=:IMPRINT_NAME,"
					 		+ "pstlId=:POSTAL_CODE,address=:ADDRESS,townshipiconame=:TOWNSHIP_ICO_NAME"
				    	  		+ " where townshipId=:UNIQ_ID_OLD"; 
				}else if(township.getTownshiplogoname()!=null && !township.getTownshiplogoname().equalsIgnoreCase("") && township.getTownshipiconame()!=null && !township.getTownshipiconame().equalsIgnoreCase("")){				  
				 	qry="update TownshipMstTbl set townshipName=:TOWNSHIP_NAME,noOfSociety=:NO_OF_SOCIETY, noOfFlats=:NO_OF_FLATS ,"
					 		+ "townshipcolourcode=:COLOUR_CODE ,countryId=:COUNTRY_CODE ,stateId=:STATE_CODE,cityId=:CITY_CODE,"
					 		+ "builderName=:BUILDER_NAME,isdCode=:ISD_CODE,mobileNo=:MOBILE_NO,landMark=:LAND_MARK,emailId=:EMAIL_ID,imprintName=:IMPRINT_NAME,"
					 		+ "pstlId=:POSTAL_CODE,address=:ADDRESS,townshiplogoname=:TOWNSHIP_LOGO_NAME,townshipiconame=:TOWNSHIP_ICO_NAME"
				    	  		+ " where townshipId=:UNIQ_ID_OLD"; 
				}else{				  
				 	qry="update TownshipMstTbl set townshipName=:TOWNSHIP_NAME,noOfSociety=:NO_OF_SOCIETY, noOfFlats=:NO_OF_FLATS ,"
						 + "builderName=:BUILDER_NAME,isdCode=:ISD_CODE,mobileNo=:MOBILE_NO,landMark=:LAND_MARK,emailId=:EMAIL_ID,imprintName=:IMPRINT_NAME,"
					 		+ "townshipcolourcode=:COLOUR_CODE ,countryId=:COUNTRY_CODE ,stateId=:STATE_CODE,cityId=:CITY_CODE,"
					 		+ "pstlId=:POSTAL_CODE,address=:ADDRESS"
				    	  		+ " where townshipId=:UNIQ_ID_OLD";
				}		       
				qy = session.createQuery(qry);
				qy.setString("TOWNSHIP_NAME", township.getTownshipName());
				qy.setInteger("NO_OF_SOCIETY", township.getNoOfSociety());
				qy.setInteger("NO_OF_FLATS", township.getNoOfFlats());
				qy.setString("BUILDER_NAME", township.getBuilderName());
				qy.setString("ISD_CODE", township.getIsdCode());
				qy.setString("MOBILE_NO", township.getMobileNo());
				qy.setString("EMAIL_ID", township.getEmailId());
				qy.setString("LAND_MARK", township.getLandMark());
				qy.setString("IMPRINT_NAME", township.getImprintName());
				if(township.getTownshiplogoname()!=null && !township.getTownshiplogoname().equalsIgnoreCase("")){		    	  
					qy.setString("TOWNSHIP_LOGO_NAME", township.getTownshiplogoname());
				}
		      if(township.getTownshipiconame()!=null && !township.getTownshipiconame().equalsIgnoreCase("")){
		    	  qy.setString("TOWNSHIP_ICO_NAME", township.getTownshipiconame());
		      }		     
		      qy.setString("COLOUR_CODE", township.getTownshipcolourcode());
		      qy.setInteger("UNIQ_ID_OLD", townuniqIdold);
		      qy.setString("ADDRESS", township.getAddress());
		      if (!Commonutility.toCheckisNumeric(countryCode)) {		    	
		    	  qy.setInteger("COUNTRY_CODE", (Integer) null);
		      } else {					 
					 int countrycode=Integer.parseInt(countryCode);
					  qy.setInteger("COUNTRY_CODE", countrycode);
		      }	
		      
		      if(!Commonutility.toCheckisNumeric(stateCode)){
		    	  qy.setString("STATE_CODE", null);
				 }else{
					 int statecode=Integer.parseInt(stateCode);
					 qy.setInteger("STATE_CODE", statecode);
				 }	
		    
		      if(!Commonutility.toCheckisNumeric(cityCode)){
		    	  qy.setString("CITY_CODE", null);
				 }else{
					  int citycode=Integer.parseInt(cityCode);	
					  qy.setInteger("CITY_CODE", citycode);
				 }	
		      if(!Commonutility.toCheckisNumeric(postalCode)){
		    	  qy.setString("POSTAL_CODE", null);
				 }else{
					 int pin=Integer.parseInt(postalCode);
					  qy.setInteger("POSTAL_CODE", pin);
				 }	
		      qy.executeUpdate();
		      tx.commit();
		      uniqueVal=1;
		}catch(HibernateException ex){
			System.out.println("Exceeption found in townShipMgmtDaoServices.townShipEditUpdate() check 3: "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.townShipEditUpdate() check 3 : "+ex, "error", townShipMgmtDaoServices.class);	
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			if(tx!=null){tx=null;}qy = null;
		}	
		}
		return uniqueVal;
	}

	@Override
	public boolean generateNewTownshipPassword(int townshipuniId,TownshipMstTbl towndata,String activeKey) {
		boolean result=false;
		Session session = null;
		Transaction tx = null;
		Query qy = null;
		try{
			 System.out.println("Step 1 : Generate New Activation Key. Start");
			 session = HibernateUtil.getSessionFactory().openSession();
			 tx = session.beginTransaction();			
			 String query = " update TownshipMstTbl set activationKey=:ACTIVATION_KEY,oldActivationKey=:OLD_ACTIVATION_KEY"
			 		+ " where  townshipId=:UNIQ_ID";	      
			 qy = session.createQuery(query);
			 qy.setString("ACTIVATION_KEY", activeKey);
			 qy.setString("OLD_ACTIVATION_KEY", towndata.getActivationKey());
			 qy.setInteger("UNIQ_ID", townshipuniId);
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
			if(session!=null){session.flush();session.clear();session.close();session = null;}if(tx!=null){tx=null;}qy = null;
		}
		System.out.println("Step 2 : Generate New Activation Key. END");
		return result; 
}

	@Override
	public List<TownshipMstTbl> getTownShipDetailListByQry(String qry,int startrowfrom,int totalNorow) {
		// TODO Auto-generated method stub
		List<TownshipMstTbl> townShipData = null;
		Session session = null;
		Query qy = null;
		try {
			townShipData = new ArrayList<TownshipMstTbl>();
			session = HibernateUtil.getSession();
			qy = session.createQuery(qry);
			qy.setFirstResult(startrowfrom);
			qy.setMaxResults(totalNorow);
			townShipData = qy.list();
		} catch (HibernateException ex) {
			System.out.println("Exceeption found in townShipMgmtDaoServices.getTownShipDetailListByQry() : "+ex);
			log.logMessage("Exceeption found townShipMgmtDaoServices.getTownShipDetailListByQry() : "+ex, "error", townShipMgmtDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session = null;}
			qy = null;
		}
		return townShipData;
	}



	@Override
	public List<UsrUpldfrmExceFailedTbl> getUploaderrorDetailListByQry(
			String qry, int startrowfrom, int totalNorow) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				List<UsrUpldfrmExceFailedTbl> UplErrDate = null;
				Session session = null;
				Query qy = null;
				try {
					UplErrDate = new ArrayList<UsrUpldfrmExceFailedTbl>();
					session = HibernateUtil.getSession();
					qy = session.createQuery(qry);
					qy.setFirstResult(startrowfrom);
					qy.setMaxResults(totalNorow);
					UplErrDate = qy.list();
				} catch (HibernateException ex) {
					System.out.println("Exceeption found in townShipMgmtDaoServices.getUploaderrorDetailListByQry() : "+ex);
					log.logMessage("Exceeption found townShipMgmtDaoServices.getUploaderrorDetailListByQry() : "+ex, "error", townShipMgmtDaoServices.class);
				} finally {
					if(session!=null){session.flush();session.clear();session.close();session = null;}
					qy = null;
				}
				return UplErrDate;
}
	@Override
	public List<ReportIssueTblVO> getReportissueDetailListByQry(
			String qry, int startrowfrom, int totalNorow) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				List<ReportIssueTblVO> REpissueData = null;
				Session session = null;
				Query qy = null;
				try {
					REpissueData = new ArrayList<ReportIssueTblVO>();
					session = HibernateUtil.getSession();
					qy = session.createQuery(qry);
					qy.setFirstResult(startrowfrom);
					qy.setMaxResults(totalNorow);
					REpissueData = qy.list();
				} catch (HibernateException ex) {
					System.out.println("Exceeption found in townShipMgmtDaoServices.getReportissueDetailListByQry() : "+ex);
					log.logMessage("Exceeption found townShipMgmtDaoServices.getReportissueDetailListByQry() : "+ex, "error", townShipMgmtDaoServices.class);
				} finally {
					if(session!=null){session.flush();session.clear();session.close();session = null;}
					qy = null;
				}
				return REpissueData;
}
}
