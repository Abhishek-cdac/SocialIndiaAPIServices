package com.mobile.facilityBooking;

import java.util.List;

import com.socialindiaservices.vo.FacilityBookingTblVO;

public interface FacilityDao {
	
	public List getFacilityList(String qry, String startlim,String totalrow,String timestamp);
	public List getFacilityMasterList(String qry, String startlim,String totalrow,String timestamp);
	public Integer saveFacilityBookingData(FacilityBookingTblVO facilityObj);
	public boolean updateFacilityBookingData(FacilityBookingTblVO facilityObj);
	public boolean deleteFacilityBookingId(String userId,String wishlistid);
	public List getBookedFacilityList(String qry);
	public FacilityBookingTblVO getFacilityBookingObjbyQuery(String query);
	public int checkCntBookingData(String rid, String startDateTime, String endDateTime,String facilityId);

}
