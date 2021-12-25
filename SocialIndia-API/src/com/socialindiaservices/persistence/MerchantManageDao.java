package com.socialindiaservices.persistence;

import java.util.List;

import org.hibernate.Session;

import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantStockDetailTblVO;
import com.socialindiaservices.vo.MerchantTblVO;
import com.socialindiaservices.vo.MerchantUtilitiImageTblVO;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public interface MerchantManageDao {
	boolean insertMerchantTbl(MerchantTblVO merchantObj,Session session);
	boolean insertMerchantStockDetailTbl(MerchantStockDetailTblVO merchantstockObj,Session session);
	MerchantTblVO getMerchantTblObjByQuery(String qry,Session session);
	List getMerchantStockDetailTblObjByQuery(String qry,Session session);
	boolean updateMerchantTbl(MerchantTblVO merchantObj,Session session);
	boolean deleteStockDetailBymerchantId(MerchantTblVO merchantObj,Session session);
	boolean deleteMerchantTblObjByQuery(String qry,Session session);
	boolean insertMerchantUtilitiTbl(MerchantUtilitiTblVO merUtilitiTblObj,Session session);
	boolean insertMerchantUtilitiImageTbl(MerchantUtilitiImageTblVO merUtilitiTblImageObj,Session session);
	MerchantUtilitiImageTblVO getMerchantImageTblObjByQuery(String qry,Session session);
	MerchantUtilitiTblVO getMerchantUtilitiTblObjByQuery(String qry,Session session);
	boolean updateMerchantUtilitiTbl(MerchantUtilitiTblVO merUtilitiTblObj,Session session);
	List deleteMerchantUtilitiImageTbl(MerchantUtilitiImageTblVO merUtilitiTblImageObj,Session session);
	boolean deleteMerchantUtilitiTbl(MerchantUtilitiTblVO merUtilitiTblObj,Session session);
	boolean insertMerchantissueTbl(MerchantIssueTblVO mechtissuetbl,Session session);
}
