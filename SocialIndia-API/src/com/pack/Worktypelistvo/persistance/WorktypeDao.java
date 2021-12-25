package com.pack.Worktypelistvo.persistance;

import com.mobi.commonvo.ResponseMsgTblVo;
import com.pack.commonvo.KnownusTblVO;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.commonvo.MvpBloodGroupTbl;
import com.pack.commonvo.MvpTitleMstTbl;

public interface WorktypeDao {
	public boolean toDeactivateWorkType(String locUpdQry);
	public int toInsertWorkType(LaborWrkTypMasterTblVO WorkTypeTblObj);
	public boolean updateCompanyInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	public String toExistWorktypelistname(String lvrWorkTypetitle);
	public int toInsertbloodtype(MvpBloodGroupTbl BloodTypeTblObj);
	public String toExistBloodtypelistname(String lvrWorkTypetitle);
	public int toInserttitle(MvpTitleMstTbl TitleTblObj);
	public String toExistTitlelistname(String lvrTypetitle);
	public int toInsertknownus(KnownusTblVO KnownusTblObj);
	public String toExistKnownuname(String lvrTypetitle);
	String toExistResponselistname(String lvrTyperesponse);
	int toInsertResponseMsg(ResponseMsgTblVo ResponsemsgTblObj);
	
}