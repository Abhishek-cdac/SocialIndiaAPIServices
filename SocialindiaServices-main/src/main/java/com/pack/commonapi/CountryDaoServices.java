package com.pack.commonapi;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;



import com.pack.commonvo.CountryMasterTblVO;
import com.sisocial.load.HibernateUtil;

public class CountryDaoServices implements CountryDao {

	@Override
	public List<CountryMasterTblVO> getcountryList() {
		// TODO Auto-generated method stub
		List<CountryMasterTblVO> cntrymst = new ArrayList<CountryMasterTblVO>();
		Session session = HibernateUtil.getSession();
	    try {
	      String qry = " from CountryMasterTblVO";
	      Query qy = session.createQuery(qry);
	      cntrymst = qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    } finally {
	      session.close();
	      
	    }
	    return cntrymst;
	}

}
