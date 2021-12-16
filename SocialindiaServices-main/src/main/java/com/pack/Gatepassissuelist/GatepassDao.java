package com.pack.Gatepassissuelist;



import java.util.List;

import com.pack.eventvo.EventTblVO;
import com.socialindiaservices.vo.FacilityBookingTblVO;
import com.socialindiaservices.vo.GatepassEntryTblVO;
import com.socialindiaservices.vo.MvpGatepassDetailTbl;

public interface GatepassDao {

	public List getGatepassList(String qry, String startlim,String totalrow,String timestamp);
	public List getGatepassMasterList(String qry, String startlim,String totalrow,String timestamp);
	public Integer saveGatepassBookingData(MvpGatepassDetailTbl GatepassObj);
	public boolean updateGatepassBookingData(MvpGatepassDetailTbl GatepassObj);
	public boolean deleteGatepassBookingId(String userId,String wishlistid);
	public List getBookedGatepassList(String qry);
	public MvpGatepassDetailTbl getgatepassobjectByQuery(String qury);
	public int updateRsvp(String query);
	public Integer saveGatepassEntryData(GatepassEntryTblVO GateentryObj);
}
