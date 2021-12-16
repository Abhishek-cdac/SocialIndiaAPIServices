package com.mobi.notification;

import java.util.List;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.socialindiaservices.vo.NotificationStatusTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public interface NotificationDao {
	public NotificationStatusTblVO getnotificationStatusbyQuery(String sql);
	public List getnotificationlist(int rid,String startlim, String totalrow,String search);
	
	public List getnotificationlistByQuery(String Query,String startlim, String totalrow);
	
	public boolean insertnewNotificationDetails(UserMasterTblVo toUserId,String descr,Integer tblrefFlag,Integer tblrefFlagType,Integer tblrefID,UserMasterTblVo entryBy,String additionalData );
	
	public boolean updateNotificationDetails(Integer tblrefFlag,Integer tblrefID,String additionalData );
	
	public boolean deleteNotificationDetails(Integer tblrefFlag,Integer tblrefID );

}
