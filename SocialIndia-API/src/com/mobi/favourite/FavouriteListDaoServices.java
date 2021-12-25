package com.mobi.favourite;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.commonvo.PrivacyInfoTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.MvpFavouriteTbl;

public class FavouriteListDaoServices implements FavouriteListDao{

	Log log=new Log();
	@Override
	public MvpFavouriteTbl insertMvpFavouriteTbl(MvpFavouriteTbl favouriteObj) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		try{
			tx = session.beginTransaction();
			 session.save(favouriteObj);
			 tx.commit();
		}catch (Exception ex){
			if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
			System.out.println("FavouriteListDaoServices found insertMvpFavouriteTbl  : "+ex);
			 log.logMessage("FavouriteListDaoServices Exception insertMvpFavouriteTbl : "+ex, "error", FavouriteListDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return favouriteObj;
	}
	@Override
	public boolean updateMvpFavouriteTblByQuery(String query) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSession();
		 Transaction tx = null;
		 boolean isupdate=false;
		try{
			tx = session.beginTransaction();
			Query qy = session.createQuery(query);
			int update=qy.executeUpdate();
			tx.commit();
			System.out.println("update---------------"+update);
			if(update>0){
				isupdate=true;
			}
		}catch (Exception ex){
			if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
			System.out.println("FavouriteListDaoServices found updateMvpFavouriteTblByQuery  : "+ex);
			 log.logMessage("FavouriteListDaoServices Exception updateMvpFavouriteTblByQuery : "+ex, "error", FavouriteListDaoServices.class);
		}finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}	log=null;
		}
		return isupdate;
	}
	
	@Override
	public boolean updateFavouriteMenu(PrivacyInfoTblVO privacyObj) {
		Session session = HibernateUtil.getSession();
	    boolean result=false;
	    Transaction tx = null;
	    Log log=new Log();
	    try {
	      tx = session.beginTransaction();
	      String updateQuery = "";
  	      if (Commonutility.checkempty(privacyObj.getFavouriteMenuIds())) {
  	    	updateQuery = "favouriteMenuIds=:FAVOURITE_MENU_IDS,";
  	      } else {
  	    	updateQuery = "favouriteMenuIds=''"+",";
  	      }
  	      Query qy = session.createQuery("update PrivacyInfoTblVO set " + updateQuery + " modifyDatetime=:MODYDATETIME where usrId=:USERID and status=:STATUS");
	  	  if (Commonutility.checkempty(privacyObj.getFavouriteMenuIds())) {
	  		qy.setString("FAVOURITE_MENU_IDS", privacyObj.getFavouriteMenuIds());
	  	  }
	  	  qy.setInteger("USERID", privacyObj.getUsrId().getUserId());
	  	  qy.setInteger("STATUS", 1);
	      qy.setTimestamp("MODYDATETIME", Commonutility.enteyUpdateInsertDateTime());
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (Exception ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("Exception found in updateFavouriteMenu : "+ex, "error", FavouriteListDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear();session.close();session = null;}
			if(tx!=null){tx=null;}	log=null;
	    }
	    return result;
	}

}
