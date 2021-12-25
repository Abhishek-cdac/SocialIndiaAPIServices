package com.mobi.skillhelp.persistence;

import java.util.Date;
import java.util.List;

import com.mobi.skillhelpvo.ServiceBookingVO;
import com.pack.laborvo.LaborProfileTblVO;

public interface SkillhelpDao {

	public List<Object[]> skillDirectoryLaborData(int rid);

	public int getTotalCountSqlQuery(String sqlQuery);

	public List<Object[]> testskillDirectoryLaborData(String sqlQuery,int start,int end,int rid);

	public int bookigNewInsert(ServiceBookingVO bookingnewObj);

	public boolean bookigUpdate(ServiceBookingVO bookingnewObj);

	public boolean bookingDelete(ServiceBookingVO bookingDelObj);

	public List<Object[]> bookingList(ServiceBookingVO bookingnewObj,Date timestamp,int startLimit,int endLimit);

	public List<Object[]> bookingListTest(ServiceBookingVO bookingnewObj,String timestamp,int startLimit,int endLimit);

	public LaborProfileTblVO Laborgetmobno(Integer ivrBnLBR_ID);




}
