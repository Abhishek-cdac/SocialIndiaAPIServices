package com.mobile.wishlist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobile.familymember.familyMemberDaoServices;
import com.mobile.profile.profileDaoServices;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class wishDaoServices implements wishDao{
	Log log=new Log();
	@Override
	public List<MvpWishlistTbl> getWishList(String userId, String timestamp,
			String startlim, String totalrow) {
		List<MvpWishlistTbl> wishList = new ArrayList<MvpWishlistTbl>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from MvpWishlistTbl where actFlg=:STATUS and userId.userId=:USER_ID and enrtyDatetime <='"+timestamp+"'";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      qy.setInteger("STATUS", 1);
	      qy.setFirstResult(Integer.parseInt(startlim));
	      qy.setMaxResults(Integer.parseInt(totalrow));
	     // qy.setInteger("SOCIETY_ID", societyId);
	     /* DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      Date startDate = df.parse(timestamp);
	      qy.setDate("ENTRY_DATE", startDate);*/
	      wishList = qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getWishList======" + ex);
			 log.logMessage("wishDaoServices Exception getWishList : "+ex, "error", wishDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return wishList;
	}
	@Override
	public boolean deleteWishListData(String userId, String wishlistid) {
	    Session session = HibernateUtil.getSession();
	    boolean result=false;
	    Transaction tx = null;
	    try {
	      tx = session.beginTransaction();
	      Query qy = session
	          .createQuery("delete from  MvpWishlistTbl  "
	              + "  where userId.userId=:USER_ID and  wishlistId=:WISHLIST_ID");
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      qy.setInteger("WISHLIST_ID", Integer.parseInt(wishlistid));
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("profileDaoServices Exception deleteWishListData : "+ex, "error", wishDaoServices.class);
	    } finally {
	      session.close();
	    }
	    return result;
	}
	@Override
	public List<MvpWishlistTbl> getWishListAdded(String userId) {
		List<MvpWishlistTbl> wishList = new ArrayList<MvpWishlistTbl>();
		Session session = HibernateUtil.getSession();
		 String qry="";
	    try {
	    		qry= " from MvpWishlistTbl where actFlg=:STATUS and userId.userId=:USER_ID ";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      qy.setInteger("STATUS", 1);
	      wishList = qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	System.out.println(" getWishListAdded======" + ex);
			 log.logMessage("wishDaoServices Exception getWishListAdded : "+ex, "error", wishDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return wishList;
	}
	@Override
	public boolean insertWishList(MvpWishlistTbl wishlistMst) {
		 boolean result=false;
			Session session = HibernateUtil.getSession();
			 Transaction tx = null;
			 try {		    		   
	       	  tx = session.beginTransaction();
	           session.save(wishlistMst);
	           tx.commit();
	           result = true;
	} catch (HibernateException ex) {	
		if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
		System.out.println("wishDaoServices found insertWishList  : "+ex);
		 log.logMessage("wishDaoServices Exception insertWishList : "+ex, "error", wishDaoServices.class);
	} finally {
		if(session!=null){session.flush();session.clear();session.close();session=null;}
	}		   
	return result;
	}
	@Override
	public boolean checkWishExist(String userId, String categoryid) {
		MvpWishlistTbl wishMst =new MvpWishlistTbl();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		 String qry="";
	    try {
	    		qry= " from MvpWishlistTbl where  userId.userId=:USER_ID and iVOCATEGORY_ID.iVOCATEGORY_ID=:CATE_ID";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      qy.setString("CATE_ID", categoryid);
	      wishMst=(MvpWishlistTbl) qy.uniqueResult();
	      if(wishMst!=null){
	    	  result=true; 
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	result=false;
	    	ex.printStackTrace();
	    	System.out.println(" getWishList======" + ex);
			 log.logMessage("wishDaoServices Exception getWishList : "+ex, "error", wishDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}
	@Override
	public boolean checkExistWish(String userId, String wishlistid) {
		MvpWishlistTbl wishMst =new MvpWishlistTbl();
		Session session = HibernateUtil.getSession();
		boolean result=false;
		 String qry="";
	    try {
	    		qry= " from MvpWishlistTbl where  userId.userId=:USER_ID and wishlistId=:WISH_ID";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("USER_ID", Integer.parseInt(userId));
	      qy.setString("WISH_ID", wishlistid);
	      wishMst=(MvpWishlistTbl) qy.uniqueResult();
	      if(wishMst!=null){
	    	  result=true; 
	      }else{
	    	  result=false;
	      }
	    } catch (Exception ex) {
	    	result=false;
	    	ex.printStackTrace();
	    	System.out.println(" checkExistWish======" + ex);
			 log.logMessage("wishDaoServices Exception checkExistWish : "+ex, "error", wishDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    }
	    return result;
	}

}
