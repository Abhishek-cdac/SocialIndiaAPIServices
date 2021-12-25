package com.socialindia.generalmgnt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.json.JSONObject;

import com.pack.laborvo.persistence.LaborDao;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;
import com.socialindia.generalmgnt.persistance.StaffWrkTblVO;

public class WorkUtility {
	private static String ivrparams;
	static StaffWrkTblVO workinfo =new StaffWrkTblVO();
	static StaffMasterTblVo staffinfo =new StaffMasterTblVo();
	//static CityMasterTblVO locCustBnObj =new CityMasterTblVO();
	
	static int count;
	static generalmgmtDao staff =new generalmgntDaoServices();
	public static JSONObject toSelectWorkalertStaff(JSONObject pDataJson) {	
		CommonUtils locCommutillObj =null;
		Log log=null;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null, locvrID_CARD_TYP = null;
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null, locvrLBR_RATING = null, locvrENTRY_BY = null;
		int locvrStaff_ID;
		JSONObject finalJson = new JSONObject();
		try {			
			locvrLBR_SERVICE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ivrDecissBlkflag");
			locvrStaff_ID = (int) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffid");
			System.out.println("servicecode   " + locvrLBR_SERVICE_ID);
			System.out.println("locvrStaff_ID   " + locvrStaff_ID);
			count =staff.workalertcreate(locvrStaff_ID);
			System.out.println("count-- "+count);
			if(count == 0)
			{				
				staffinfo =staff.editstaff(locvrStaff_ID);				
				finalJson.put("staffid", staffinfo.getStaffID());
				finalJson.put("staffName", staffinfo.getStaffName());
				finalJson.put("staffEmail", staffinfo.getStaffEmail());
				finalJson.put("staffMobno", staffinfo.getStaffMobno());
				finalJson.put("WorkStartdate", "");
				
				finalJson.put("Workenddate", "");
				finalJson.put("Workdetails", "");
				finalJson.put("weeklyoff", "");				
				return finalJson;
			}else {
				System.out.println("else--count val > 0 ");
				workinfo =staff.editselectworkalt(locvrStaff_ID);				
				finalJson.put("staffid", workinfo.getStaffID().getStaffID());
				finalJson.put("staffName", workinfo.getStaffID().getStaffName());
				finalJson.put("staffEmail", workinfo.getStaffID().getStaffEmail());
				finalJson.put("staffMobno", workinfo.getStaffID().getStaffMobno());
				finalJson.put("weeklyoff", workinfo.getWeeklyoff());
				finalJson.put("WorkStartdate", workinfo.getWorkStartDate());
				finalJson.put("Workenddate", workinfo.getWorkEndDate());
				finalJson.put("Workdetails", workinfo.getWorkDtl());				
				return finalJson;
			}			
		} catch (Exception e) {
			System.out.println("Exception found in salaryUtility.toInsrtLabor : "+e);
			log.logMessage("Exception : "+e, "error", SalaryUtility.class);
			return finalJson;
		} finally {
			 locCommutillObj =null;
			 locvrLBR_SERVICE_ID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null; locvrLBR_PH_NO = null; locvrID_CARD_TYP = null;
			 locvrID_CARD_NO = null; locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrLBR_KYC_NAME = null; locvrLBR_RATING = null; locvrENTRY_BY = null;
		}
		
		
	}
	
	public static String toInsrtwrkAlert(JSONObject locObjRecvdataJson) {
		ResourceBundle ivrRbuilder = ResourceBundle.getBundle("applicationResources");
		// TODO Auto-generated method stub
		// Insert
		CommonUtils locCommutillObj =null;
		Log log=null; LaborDao locLabrObj=null;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null, locvrID_CARD_TYP = null;
		
		String locvrLBR_WRKALTMSG=null,locvrSTART_DATE = null, locvrEND_DATE = null, locvrWeek_off = null, locvrLBR_RATING = null;		
		int locvrLBR_STAFF_ID;
	
		//Date locvrSTART_DATE;
		//Date locvrEND_DATE;
		boolean result= false;
		try {
			
			locCommutillObj = new CommonUtilsDao();
			log=new Log();						
			locvrLBR_STAFF_ID = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "staffid");
			System.out.println("vv "+locObjRecvdataJson);			 			 
			locvrLBR_WRKALTMSG  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "Workdetails");					
			locvrSTART_DATE = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "WorkStartdate");
			System.out.println("*** **  "+locvrSTART_DATE);
			locvrEND_DATE = (String)  Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "Workenddate");
			locvrWeek_off =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "weeklyoff");
			 String locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "entryby");			
			 String dateStr = locvrSTART_DATE;
			 String dateStr1 = locvrEND_DATE;	
			 DateFormat formatter = new SimpleDateFormat(ivrRbuilder.getString("calander.format.date"));
			 Date Startdate = (Date) formatter.parse(dateStr);
			 Date Enddate = (Date) formatter.parse(dateStr1);			
			 StaffWrkTblVO locObjworkprfvo=new StaffWrkTblVO();
			 StaffMasterTblVo locObjstaffprfvo = new StaffMasterTblVo();
			 locObjstaffprfvo.setStaffID(locvrLBR_STAFF_ID);
			 locObjworkprfvo.setStaffID(locObjstaffprfvo);
			 locObjworkprfvo.setWorkDtl(locvrLBR_WRKALTMSG);
			 locObjworkprfvo.setWorkStartDate(Startdate);
			 locObjworkprfvo.setWorkEndDate(Enddate);
			 locObjworkprfvo.setWeeklyoff(locvrWeek_off);
			 locObjworkprfvo.setStatus(1);			
			 locObjworkprfvo.setEntryBy(Integer.parseInt(locvrENTRY_BY));	
			 SimpleDateFormat locSmft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 locObjworkprfvo.setEntryDatetime(locSmft.parse(locSmft.format(new Date())));	
			 System.out.println("Test ----"+locObjworkprfvo.getWorkStartDate());
			 result =staff.saveWorkInfo(locObjworkprfvo);			 			 
			 return "success";
		} catch (Exception e) {
			System.out.println("Exception found in WorkUtility.toupdate : "+e);
			log.logMessage("Exception : "+e, "error", WorkUtility.class);
			return "error";
		} finally {
			 locCommutillObj =null; locLabrObj=null;
			 locvrLBR_SERVICE_ID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null; locvrLBR_PH_NO = null; locvrID_CARD_TYP = null;
			
			 locvrSTART_DATE = null; locvrEND_DATE = null;  locvrWeek_off = null; locvrLBR_RATING = null; 
			 ivrRbuilder = null;
		}
	}

	public static String getIvrparams() {
		return ivrparams;
	}

	public static void setIvrparams(String ivrparams) {
		WorkUtility.ivrparams = ivrparams;
	}
	

}
