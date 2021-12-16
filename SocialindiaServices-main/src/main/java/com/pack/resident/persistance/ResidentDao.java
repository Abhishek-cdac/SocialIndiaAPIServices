package com.pack.resident.persistance;

import java.util.List;

import com.pack.commonvo.FlatMasterTblVO;
import com.pack.residentvo.UsrUpldfrmExceFailedTbl;
import com.pack.residentvo.UsrUpldfrmExceTbl;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;

public interface ResidentDao {
	
	
	public int saveResidentInfo_int(UserMasterTblVo pIntcmprobj,
			String famName, String famEmailId, String famMobileNo,
			String famISD, String fammemtyp, String famprfaccess);

	public boolean updateResidentInfo(String pIntLabrupdQry, String famName,
			String famMobileNo, String famEmailId, int restid, String famISD,
			String fammemtyp, String famprfaccess,String fmbruniqueid);

	public int getInitTotal(String sql);

	public int getTotalFilter(String sqlQueryFilter);

	public boolean deleteFlatdblInfo(int pIntLborid);

	public int saveResidentFlatInfo_intRtn(FlatMasterTblVO pIntFlatObj);

	public boolean duplicateResident_id(UserMasterTblVo pIntdupobj);

	public int saveUplFilepath(UsrUpldfrmExceTbl locObjuplFilevo);

	public int saUplErr_Row(UsrUpldfrmExceFailedTbl locObjUplErrvo);

	public List<MvpFamilymbrTbl> getUserFamilyList(int userId);

	public boolean updateProductOrdertbl(String pIntLabrupdQry);
}
