package com.mobi.easypayvo.persistence;

import java.util.List;

import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.MaintenanceBillTaxDtlsTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public interface MaintenanceFeeDao {

	public List<MaintenanceFeeTblVO> maintenanceBillSearchList(String searchQury,
			String startlimit, String text, String locTimeStamp);

	public List<DocumentManageTblVO> documnetAttachList(String maintenanceId, int rid);
	
	public List<MaintenanceBillTaxDtlsTblVO> getMNTNCBillTaxDetails();

}
