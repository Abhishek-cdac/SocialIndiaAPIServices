package com.pack.laborvo.persistence;

import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;



public interface LaborDao {
	public boolean saveLaborInfo(LaborProfileTblVO pIntLabrobj);
	public int saveLaborInfo_int(LaborProfileTblVO pIntLabrobj);
	public boolean deActiveLaborInfo(LaborProfileTblVO pIntLabrobj);
	public boolean delteLaborInfo(LaborProfileTblVO pIntLabrobj);
	public boolean updateLaborInfo(String pIntLabrupdQry);
	public int getInitTotal(String sql);
	public int getTotalFilter(String sqlQueryFilter);
	
	
	public int saveLaborSkilInfo_intRtn(LaborSkillTblVO pIntLborSkilObj);
	public boolean deleteLabrSkillInfo(int pIntLborid);
}
