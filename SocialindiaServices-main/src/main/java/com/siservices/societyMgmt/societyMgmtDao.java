package com.siservices.societyMgmt;

import java.util.List;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;

public interface societyMgmtDao {

	
	public int getInitTotal(String sqlQuery);
	 
	 public int gettotalcount(String sql);
	 
	 public int getTotalFilter(String sqlQueryFilter);
	 
	 public List<SocietyMstTbl> getsocietyDetailList(String qry,int pstartrow, int ptotrow);
	 
	 public UserMasterTblVo getSocietyPrintData(int societyid,int groupcode);
	 
	 public int societyInfoSave(SocietyMstTbl societyMst,UserMasterTblVo userInfo,String flatnames,MvpSocietyAccTbl societyAccData);
	 
	 public SocietyMstTbl getSocietyDetailView(int societyid);
	 
	 public List<SocietyWingsDetailTbl> getSocietyWingsDetailView(int societyid);
	 
	 public MvpSocietyAccTbl getSocietyAccData(int societyid);
	 
	 public UserMasterTblVo getSocietyUserRegisterData(int societyid,int grpCode);
	 
	 public int societyInfoUpdate(SocietyMstTbl societyMst,int townshipId,String flatnames,MvpSocietyAccTbl societyAccData,String emailId,int locGrpcode);
	 
	 public boolean generateNewSocietyPassword(int societyuniId,SocietyMstTbl societydata,String activeKey);
	 
	 public String getsocietyemailid(int societyid,int grpcode);
	 public String getactiveKey(String sqlQuery);

	public int socetyid(String socetysql);
	
	public int delete(String societyid);
}
