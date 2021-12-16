package com.siservices.common;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

public interface CommonUtils {
	
	 public Date getStrCurrentDateTime(String dateformattype);
	 
	 public String getRandomval(String type, int dataLength) ;
	 
	 public String stringToMD5(String password);
	

}
