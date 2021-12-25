package com.mobi.event;

import java.util.List;

import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.socialindiaservices.vo.FacilityBookingTblVO;
import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public interface EventDao {
	
	public FunctionMasterTblVO getfunctionMasterObjByQry(String qry);
	public FunctionTemplateTblVO getfunctionTemplateObjByQry(String qry);
	public Integer saveCreateNewEvent(EventTblVO eventObj);
	public int updateRsvp(String query);
	public List getEventDisplayList(String qry, String startlim,String totalrow,String timestamp);
	public List<Object[]> getEventContributionSearchList(String qry, String startlim);
	public boolean updateEvent(EventTblVO eventObj);
	public int toInsertEventDispTbl(EventDispTblVO prEventdsipvoobj);
	
	public FacilityBookingTblVO getfacilityObjByBookingId(int bookingId);
	public EventTblVO geteventobjectByQuery(String qury);

}
