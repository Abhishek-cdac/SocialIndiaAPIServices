package com.socialindiaservices.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantStockDetailTblVO;
import com.socialindiaservices.vo.MerchantTblVO;
import com.socialindiaservices.vo.MerchantUtilitiImageTblVO;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public class MerchantManageHibernateDao implements MerchantManageDao{

	@Override
	public boolean insertMerchantTbl(MerchantTblVO merchantObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(merchantObj);
			issaved=true;
		}catch(HibernateException ex){
		}
		return issaved;
		
	}

	@Override
	public boolean insertMerchantStockDetailTbl(
			MerchantStockDetailTblVO merchantstockObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(merchantstockObj);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========insertMerchantTbl Insert================"+ex);
		}
		return issaved;
	}

	@Override
	public MerchantTblVO getMerchantTblObjByQuery(String qry, Session session) {
		// TODO Auto-generated method stub
		MerchantTblVO merchantObj=new MerchantTblVO();
		try{
			 Query qy = session.createQuery(qry);
			 qy.setMaxResults(1);
			 merchantObj= (MerchantTblVO) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getdocumentsharetblByQuery Insert================"+ex);
		}
		return merchantObj;
	}

	@Override
	public List getMerchantStockDetailTblObjByQuery(
			String qry, Session session) {
		// TODO Auto-generated method stub
		List<MerchantStockDetailTblVO> merchantObj=new ArrayList<MerchantStockDetailTblVO>();
		try{
			 Query qy = session.createQuery(qry);
			 merchantObj= qy.list();
		}catch(HibernateException ex){
			System.out.println("===========getdocumentsharetblByQuery Insert================"+ex);
		}
		return merchantObj;
	}

	@Override
	public boolean updateMerchantTbl(MerchantTblVO merchantObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		String updq=null;
		try{
			
			if(merchantObj.getStoreimage()!=null && !merchantObj.getStoreimage().equalsIgnoreCase("null") && !merchantObj.getStoreimage().equalsIgnoreCase("")){
				 updq="update MerchantTblVO set mrchntName=:MERCHANT_NAME,mrchCategoryId.mrchCategoryId=:MERCHANT_CATEGORY,mrchntEmail=:MRCHANT_EMAIL,"
						+ "mrchntIsdCode=:ISD_CODE,mrchntPhNo=:PH_NO,shopName=:SHOP_NAME,storeLocation=:STORE_LOCATION,storeOpentime=:STORE_OPEN_TIME,"
						+ "storeClosetime=:STORE_CLOSE_TIME,keyForSearch=:KEY_FOR_SEARCH,mrchntAdd1=:ADDR_ONE,mrchntAdd2=:ADDR_TWO,countryId=:CNTRY_ID,stateId=:STATE_ID,"
						+ "cityId=:CITI_ID,pstlId=:PSTL_ID,merchDescription=:MERCHANT_DESCRIPTION,website=:WEB_SITE,storeimage=:STORE_IMG  where mrchntId=:MERCHANT_UNI_ID";
			}
			else{
			 updq="update MerchantTblVO set mrchntName=:MERCHANT_NAME,mrchCategoryId.mrchCategoryId=:MERCHANT_CATEGORY,mrchntEmail=:MRCHANT_EMAIL,"
					+ "mrchntIsdCode=:ISD_CODE,mrchntPhNo=:PH_NO,shopName=:SHOP_NAME,storeLocation=:STORE_LOCATION,storeOpentime=:STORE_OPEN_TIME,"
					+ "storeClosetime=:STORE_CLOSE_TIME,keyForSearch=:KEY_FOR_SEARCH,mrchntAdd1=:ADDR_ONE,mrchntAdd2=:ADDR_TWO,countryId=:CNTRY_ID,stateId=:STATE_ID,"
					+ "cityId=:CITI_ID,pstlId=:PSTL_ID,merchDescription=:MERCHANT_DESCRIPTION,website=:WEB_SITE  where mrchntId=:MERCHANT_UNI_ID";
			}
			Query qr=session.createQuery(updq);
			System.out.println("merchantObj.getMrchntId()-----------------------"+merchantObj.getMrchntId());
			qr.setString("MERCHANT_NAME", merchantObj.getMrchntName());
			if(merchantObj.getMrchCategoryId()!=null){
			qr.setInteger("MERCHANT_CATEGORY", merchantObj.getMrchCategoryId().getMrchCategoryId());
			}else{
				qr.setString("MERCHANT_CATEGORY", null);	
			}
			qr.setString("MRCHANT_EMAIL",merchantObj.getMrchntEmail());
			qr.setString("ISD_CODE",merchantObj.getMrchntIsdCode());
			qr.setString("PH_NO",merchantObj.getMrchntPhNo());
			qr.setString("SHOP_NAME", merchantObj.getShopName());
			qr.setString("STORE_LOCATION",merchantObj.getStoreLocation());
			qr.setString("STORE_OPEN_TIME",merchantObj.getStoreOpentime());
			qr.setString("STORE_CLOSE_TIME",merchantObj.getStoreClosetime());
			qr.setString("KEY_FOR_SEARCH",merchantObj.getKeyForSearch());
			qr.setString("ADDR_ONE",merchantObj.getMrchntAdd1());
			qr.setString("ADDR_TWO",merchantObj.getMrchntAdd2());
			if(merchantObj.getCountryId()!=null){
				qr.setInteger("CNTRY_ID", merchantObj.getCountryId().getCountryId());
			}else{
				qr.setString("CNTRY_ID", null);
			}
			if(merchantObj.getStateId()!=null){
				qr.setInteger("STATE_ID", merchantObj.getStateId().getStateId());
			}else{
				qr.setString("STATE_ID", null);
			}
			if(merchantObj.getCityId()!=null){
				qr.setInteger("CITI_ID", merchantObj.getCityId().getCityId());
			}else{
				qr.setString("CITI_ID", null);
			}
			
			if(merchantObj.getPstlId()!=null){
//				qr.setInteger("PSTL_ID", merchantObj.getPstlId().getPstlId());
				qr.setInteger("PSTL_ID", merchantObj.getPstlId());
			}else{
				qr.setString("PSTL_ID", null);
			}
			if(merchantObj.getStoreimage()!=null){
				qr.setString("STORE_IMG", merchantObj.getStoreimage());
			}
			
			qr.setString("MERCHANT_DESCRIPTION",merchantObj.getMerchDescription());
			qr.setString("WEB_SITE",merchantObj.getWebsite());
			
			qr.setInteger("MERCHANT_UNI_ID", merchantObj.getMrchntId());
			qr.executeUpdate();
			
			issaved=true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return issaved;
	}

	@Override
	public boolean deleteStockDetailBymerchantId(MerchantTblVO merchantObj,
			Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			String updq="delete from MerchantStockDetailTblVO where mrchntId.mrchntId=:MERCHANT_UNI_ID";
			Query qr=session.createQuery(updq);
			qr.setInteger("MERCHANT_UNI_ID", merchantObj.getMrchntId());
			qr.executeUpdate();
			issaved=true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return issaved;
	}

	@Override
	public boolean deleteMerchantTblObjByQuery(String qry, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			Query qr=session.createQuery(qry);
			qr.executeUpdate();
			issaved=true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return issaved;
	}

	@Override
	public boolean insertMerchantUtilitiTbl(
			MerchantUtilitiTblVO merUtilitiTblObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(merUtilitiTblObj);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========insertMerchantTbl Insert================"+ex);
		}
		return issaved;
	}

	@Override
	public boolean insertMerchantUtilitiImageTbl(
			MerchantUtilitiImageTblVO merUtilitiTblImageObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(merUtilitiTblImageObj);
			issaved=true;
		}catch(HibernateException ex){
			System.out.println("===========MerchantUtilitiImageTblVO Insert================"+ex);
		}
		return issaved;
	}

	@Override
	public MerchantUtilitiImageTblVO getMerchantImageTblObjByQuery(String qry,
			Session session) {
		// TODO Auto-generated method stub
		MerchantUtilitiImageTblVO merchantObj=new MerchantUtilitiImageTblVO();
		try{
			 Query qy = session.createQuery(qry);
			 qy.setMaxResults(1);
			 merchantObj= (MerchantUtilitiImageTblVO) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getdocumentsharetblByQuery Insert================"+ex);
		}
		return merchantObj;
	}

	@Override
	public MerchantUtilitiTblVO getMerchantUtilitiTblObjByQuery(String qry,
			Session session) {
		// TODO Auto-generated method stub
		MerchantUtilitiTblVO merchantObj=new MerchantUtilitiTblVO();
		try{
			 Query qy = session.createQuery(qry);
			 qy.setMaxResults(1);
			 merchantObj= (MerchantUtilitiTblVO) qy.uniqueResult();
		}catch(HibernateException ex){
			System.out.println("===========getdocumentsharetblByQuery Insert================"+ex);
		}
		return merchantObj;
	}

	@Override
	public boolean updateMerchantUtilitiTbl(
			MerchantUtilitiTblVO merUtilitiTblObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			String updq="update MerchantUtilitiTblVO set broucherName=:BROUCHER_NAME,offerName=:OFFER_NAME,offerPrice=:OFFER_PRICE,"
					+ "actualPrice=:ACTUAL_PRICE,description=:DESCRIPTION,offerValidFrom=:OFFER_VALID_FROM,offerValidTo=:OFFER_VALID_TO,modifyDatetime=:MODIFY_TIME,"
					+ "mrchntId.mrchntId=:MERCHANT_ID  where merchantUtilId=:MERCHANT_UTIL_ID";
			Query qr=session.createQuery(updq);
			System.out.println("merchantObj.getMrchntId()-----------------------"+merUtilitiTblObj.getMerchantUtilId());
			qr.setString("BROUCHER_NAME", merUtilitiTblObj.getBroucherName());
			qr.setString("OFFER_NAME",merUtilitiTblObj.getOfferName());
			qr.setFloat("OFFER_PRICE",merUtilitiTblObj.getOfferPrice());
			qr.setFloat("ACTUAL_PRICE",merUtilitiTblObj.getActualPrice());
			qr.setString("DESCRIPTION", merUtilitiTblObj.getDescription());
			qr.setDate("OFFER_VALID_FROM", merUtilitiTblObj.getOfferValidFrom());
			qr.setDate("OFFER_VALID_TO", merUtilitiTblObj.getOfferValidTo());
			qr.setTimestamp("MODIFY_TIME",merUtilitiTblObj.getModifyDatetime());
			qr.setInteger("MERCHANT_ID", merUtilitiTblObj.getMrchntId().getMrchntId());
			qr.setInteger("MERCHANT_UTIL_ID", merUtilitiTblObj.getMerchantUtilId());
			
			qr.executeUpdate();
			issaved=true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return issaved;
	}

	@Override
	public List deleteMerchantUtilitiImageTbl(
			MerchantUtilitiImageTblVO merUtilitiTblImageObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		List<MerchantUtilitiImageTblVO> merchantImagelist=new ArrayList<MerchantUtilitiImageTblVO>();
		try{
			String selq="FROM MerchantUtilitiImageTblVO where merchantUtilId.merchantUtilId=:MERCHANT_UTIL_ID";
			Query sqr=session.createQuery(selq);
			sqr.setInteger("MERCHANT_UTIL_ID", merUtilitiTblImageObj.getMerchantUtilId().getMerchantUtilId());
			merchantImagelist=sqr.list();
			
			String updq="delete from MerchantUtilitiImageTblVO where merchantUtilId.merchantUtilId=:MERCHANT_UTIL_ID";
			Query qr=session.createQuery(updq);
			qr.setInteger("MERCHANT_UTIL_ID", merUtilitiTblImageObj.getMerchantUtilId().getMerchantUtilId());
			qr.executeUpdate();
			issaved=true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return merchantImagelist;
	}

	@Override
	public boolean deleteMerchantUtilitiTbl(
			MerchantUtilitiTblVO merUtilitiTblObj, Session session) {
		// TODO Auto-generated method stub
		boolean issaved=false;
		List<MerchantUtilitiImageTblVO> merchantImagelist=new ArrayList<MerchantUtilitiImageTblVO>();
		try{
			String updq="delete from MerchantUtilitiTblVO where merchantUtilId=:MERCHANT_UTIL_ID";
			Query qr=session.createQuery(updq);
			qr.setInteger("MERCHANT_UTIL_ID", merUtilitiTblObj.getMerchantUtilId());
			qr.executeUpdate();
			issaved=true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return issaved;
	}

	@Override
	public boolean insertMerchantissueTbl(MerchantIssueTblVO mechtissuetbl,Session session) {

		// TODO Auto-generated method stub
		boolean issaved=false;
		try{
			session.save(mechtissuetbl);
			issaved=true;
		}catch(HibernateException ex){		
		}
		return issaved;
		
	
	}

}
