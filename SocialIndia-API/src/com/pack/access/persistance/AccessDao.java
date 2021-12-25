package com.pack.access.persistance;

import com.pack.accessvo.AccessDetailsVO;

public interface AccessDao {
	public int saveAccessInfo(AccessDetailsVO accessObj);
	public boolean verificationIPInfo(String ipaddress,String pGmtTime, String pClienttime);
	public boolean verificationUnique(String ipaddress,int uniqueId);
	int getInitTotal(String getInitTotal);
	

}
