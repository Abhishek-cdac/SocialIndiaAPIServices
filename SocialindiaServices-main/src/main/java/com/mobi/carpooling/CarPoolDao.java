package com.mobi.carpooling;

import java.util.List;

import com.socialindiaservices.vo.CarMasterTblVO;
import com.socialindiaservices.vo.CarPoolBookingTblVO;
import com.socialindiaservices.vo.CarPoolingTblVO;


public interface CarPoolDao {
	public List getCarPoolingList(String qry, String startlim,String totalrow,String timestamp);
	public Integer saveCarPoolingObject(CarPoolingTblVO carpoolObj,CarMasterTblVO carMasterObj);
	public boolean updateCarPoolingObject(CarPoolingTblVO carpoolObj,CarMasterTblVO carMasterObj);
	public Integer saveCarBookingObject(CarPoolBookingTblVO carBookingObj);
	public boolean updateCarBookingObject(CarPoolBookingTblVO carBookingObj);
	public Long getCarBookingCount(CarPoolBookingTblVO carBookingObj);
	public boolean runupdateQueryCarPoolingTable(String sql);
	public List getCarMAsterList(String qry);
}
