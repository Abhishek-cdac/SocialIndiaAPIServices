package com.siservices.emailvo.persistance;

import com.pack.utilitypkg.CommonDao;

public class EmailDaoservice implements EmailDao {
	
	@Override
	public boolean toUpdateEmail(String locUpdQry){
		System.out.println("test update success");
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(locUpdQry);
		return locRtnSts;
		
	}
	

}
