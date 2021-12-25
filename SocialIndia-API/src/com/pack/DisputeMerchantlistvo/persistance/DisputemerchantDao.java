package com.pack.DisputeMerchantlistvo.persistance;

import com.pack.laborvo.LaborProfileTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.socialindiaservices.vo.DisputeRiseTbl;
import com.socialindiaservices.vo.MerchantTblVO;

public interface DisputemerchantDao {
	public boolean toDeactivateWorkType(String locUpdQry);
	public int toInsertCmplt(DisputeRiseTbl DisputeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistWorktypelistname(String lvrWorkTypetitle);
	public boolean toDeletedispute(String locDlQry);
	public boolean toClosedCmplt(String locDlQry);
	public UserMasterTblVo getusermasterData(Integer uniqadminId,
			Integer groupcode);
	public MerchantTblVO getMerchantmasterData(Integer uniqadminId,
			Integer groupcode);
	public UserMasterTblVo getResidentmasterData(String DisputeUsrid,
			Integer groupcode);
	public LaborProfileTblVO getLabormasterData(Integer uniqadminId,
			Integer groupcode); 
	
}