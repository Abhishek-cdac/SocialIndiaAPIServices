package com.si.onewaycommunication;

import java.util.List;

import com.socialindiaservices.vo.OneWayCommunicationTblVO;

public interface OneWayCommunicationDao {

	public List<OneWayCommunicationTblVO> getAccessFlags(String sid,String rights);
	
	public int setAccessFlags(OneWayCommunicationTblVO tblVO);

}
