package com.socialindia.generalmgnt;

import java.util.List;

import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;
import com.socialindia.generalmgnt.persistance.StaffSlryTblVO;
import com.socialindia.generalmgnt.persistance.StaffWrkTblVO;
import com.socialindiaservices.vo.BiometricDatabaseInfoTblVO;
import com.socialindiaservices.vo.BiometricUserInfoTblVO;
import com.socialindiaservices.vo.ManageGSTTblVO;

public interface generalmgmtDao {
	 public int staffCreation(StaffMasterTblVo staffMaster);
	
	 public int bioMetricDbCreation(BiometricDatabaseInfoTblVO tblVO);
	 
	 public int manageGSTInfo(ManageGSTTblVO tblVO);
	 
	 public ManageGSTTblVO getGSTInfo();
	 
	 public BiometricDatabaseInfoTblVO getBioMetricDB(int socyteaId);
	 
	 public List<StateMasterTblVO> commonStateData(int countryidkey);
	 
	 public List<CityMasterTblVO> commonCityData(int stateidkey);
	 
	 public int getInitTotal(String sqlQuery);
	 
	 public int getTotalFilter(String sqlQueryFilter);
	 
	 public List<StaffMasterTblVo> getUserDetailList(String likeqry, int pstartrow, int totrow);
	 
	 public int gettotalcount(String sql);
	 
	 public StaffMasterTblVo editstaff(int userId);
	 
	 public StaffMasterTblVo updateStaff(int locvrStaff_ID,String locvrLBR_NAME,String locvrLBR_EMAIL,String locvrLBR_PH_NO,String locvrLBR_GENDER,int locvrID_CARD_TYP,String locvrID_CARD_NO,String locvrLBR_ADD_1,int locvrLBR_COUNTRY_ID1,int locvLBR_STATE_ID1,int locvrLBR_CITY_ID1, String locvrLBR_ISD, int locvrCAT_ID, int locvrPIN_CODE, int locvrWORK_HOUR, String imageweb, String staffImageFileName);
	
	 public boolean deletestaff(int locvrStaff_ID);
	
	 public StaffSlryTblVO editSalary(int locvrStaff_ID);
	
	 public int salarycreate(int locvrStaff_ID);
	
	 public boolean saveSalaryInfo(StaffSlryTblVO locObjsalaryprfvo);
	
	 public StaffSlryTblVO editselectsalary(int locvrStaff_ID);
	
	 public int workalertcreate(int locvrStaff_ID);
	
	 public StaffWrkTblVO editselectworkalt(int locvrStaff_ID);
	
	 public boolean saveWorkInfo(StaffWrkTblVO locObjworkprfvo);
	 
	 public StaffMasterTblVo getregistertable(int userid);
	
	 public StaffMasterTblVo updatestaffmgnt(StaffMasterTblVo stafftable, String categ_st, String pstl_st);
	 
	 public List<BiometricUserInfoTblVO> getUserDetailList_like_limt(String qry, int startrowfrom, int totrow);
	 
	 public List<BiometricUserInfoTblVO> getUserDetailList_like(String qry);
	 
}
