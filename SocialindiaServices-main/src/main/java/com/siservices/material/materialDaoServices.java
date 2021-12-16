package com.siservices.material;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.materialVo.MvpMaterialTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class materialDaoServices implements  materialDao {
	Log log=new Log();
	@Override
	public String materialCreationForm(MvpMaterialTbl materialMst) {
	
			Session session = null;
			Transaction tx = null;
			String result="";
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			try {
				session.save(materialMst);
				result="success";
				tx.commit();
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				System.out.println("Exception found in materialDaoServices : "+e);
				log.logMessage("materialDaoServices Exception materialCreationForm : "+e, "error", materialDaoServices.class);
				result="error";
				
			} finally {
				if(session!=null){session.flush();session.clear();session.close();session=null;}
				if(tx!=null){tx=null;}
			}	
			
			return result;
		}

	@Override
	public MvpMaterialTbl getMaterialView(int materialid) {
		MvpMaterialTbl materialMst = new MvpMaterialTbl();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From MvpMaterialTbl where materialId=:MATERIAL_ID and status=:STATUS";
			Query qy = session.createQuery(qry);
			qy.setInteger("MATERIAL_ID", materialid);
			qy.setInteger("STATUS", 1);
			System.out.println("----------getMaterialView------------" + qy);
			materialMst = (MvpMaterialTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println(" getMaterialView======" + ex);
			 log.logMessage("materialDaoServices Exception getMaterialView : "+ex, "error", materialDaoServices.class);
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		}
		return materialMst;
	}

	@Override
	public boolean materialDelete(int materialId) {
		 boolean result = false;
		    Session session = HibernateUtil.getSession();
		    Transaction tx = null;
		    Date date1;
		    System.out.println("=======materialId======"+materialId);
		    CommonUtils comutil=new CommonUtilsServices();
			date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		    try {
		      tx = session.beginTransaction();
		      Query qy = session
		          .createQuery("update MvpMaterialTbl set status=:STATUS_FLAG,  "
		              + " modifyDatetime=:MODY_DATETIME where materialId=:MATERIAL_ID");
		      qy.setInteger("STATUS_FLAG", 0);
		      qy.setInteger("MATERIAL_ID", materialId);
		      qy.setTimestamp("MODY_DATETIME", date1);
		      qy.executeUpdate();
		      tx.commit();
		      result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) {
		        tx.rollback();
		      }
		      ex.printStackTrace();
		      result = false;
		      log.logMessage("materialDaoServices Exception materialDelete : "+ex, "error", materialDaoServices.class);
		    } finally {
		    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
		    }
		    return result;
	}

	@Override
	public boolean getmaterialUpdate(MvpMaterialTbl materialMst) {
		boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    Date date1;	 
	    CommonUtils comutil=new CommonUtilsServices();
		date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	    try {
	      tx = session.beginTransaction();
	      Query qy = session.createQuery("update MvpMaterialTbl set materialCategoryId=:CATEGORY_ID, materialName=:MATERIAL_NAME, "
	              + " totalQnty=:TOTAL_QNTY,usedQnty=:USED_QNTY,materialQnty=:MATERIAL_QNTY,materialPrice=:MATERIAL_PRICE, "
	              + " purchaseDate=:PURCHASE_DATE,modifyDatetime=:MODY_DATETIME,materialDesc=:MATERIAL_DESC"
	              + " where materialId=:MATERIAL_ID");
	      qy.setInteger("MATERIAL_ID", materialMst.getMaterialId());
	      qy.setInteger("CATEGORY_ID", materialMst.getMaterialCategoryId().getMaterialCategoryId());
	      qy.setString("MATERIAL_NAME", materialMst.getMaterialName());
	      qy.setInteger("TOTAL_QNTY", materialMst.getTotalQnty());
	      qy.setInteger("USED_QNTY", materialMst.getUsedQnty());
	      qy.setInteger("MATERIAL_QNTY", materialMst.getMaterialQnty());
	      qy.setFloat("MATERIAL_PRICE", materialMst.getMaterialPrice());
	      qy.setDate("PURCHASE_DATE", materialMst.getPurchaseDate());
	      qy.setString("MATERIAL_DESC", materialMst.getMaterialDesc());
	      qy.setTimestamp("MODY_DATETIME", date1);
	      qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (HibernateException ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      ex.printStackTrace();
	      result = false;
	      log.logMessage("materialDaoServices Exception getmaterialUpdate : "+ex, "error", materialDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    }
	    return result;
	}
	

}
