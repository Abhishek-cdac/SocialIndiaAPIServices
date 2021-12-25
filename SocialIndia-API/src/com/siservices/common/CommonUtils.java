package com.siservices.common;

import java.util.Date;

public interface CommonUtils {
	
	 public Date getStrCurrentDateTime(String dateformattype);
	 
	 public String getRandomval(String type, int dataLength) ;
	 
	 public String stringToMD5(String password);
	

}
