package com.socialindiaservices.facilityVo;

import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.socialindiaservices.vo.FacilityMstTblVO;


public interface facilityDao {
	  public int toInsertFacility(FacilityMstTblVO prEventvoobj);

	public int toInsertwhyshould(WhyShouldIUpdateTblVO whyshouldObj);

}
