package com.socialindia.generalmgnt;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.json.JSONObject;








import com.pack.commonvo.CityMasterTblVO;
import com.pack.labor.LaborUtility;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.persistence.LaborDao;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;
import com.socialindia.generalmgnt.persistance.StaffSlryTblVO;

public class SalaryUtility {
	private static String ivrparams;
	static StaffSlryTblVO salaryinfo =new StaffSlryTblVO();
	static StaffMasterTblVo staffinfo =new StaffMasterTblVo();		
	static int count;
	static generalmgmtDao staff =new generalmgntDaoServices();
	public static JSONObject toSelectSalaryStaff(JSONObject pDataJson) {	
		CommonUtils locCommutillObj =null;
		Log log=null;
		String locvrLBR_SERVICE_ID=null;
		int locvrStaff_ID;
		JSONObject finalJson = new JSONObject();
		try {
			
			System.out.println("---update salaryutility---"+pDataJson);
			locvrLBR_SERVICE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ivrDecissBlkflag");
			locvrStaff_ID = (int) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffid");
			System.out.println("servicecode   " + locvrLBR_SERVICE_ID);
			System.out.println("locvrStaff_ID   " + locvrStaff_ID);
			count =staff.salarycreate(locvrStaff_ID);			
			if(count == 0)
			{				
				staffinfo =staff.editstaff(locvrStaff_ID);				
				finalJson.put("staffid", staffinfo.getStaffID());
				finalJson.put("staffName", staffinfo.getStaffName());
				finalJson.put("staffEmail", staffinfo.getStaffEmail());
				finalJson.put("staffMobno", staffinfo.getStaffMobno());
				finalJson.put("staffGender", staffinfo.getStaffGender());				
				finalJson.put("monthlySalary", Commonutility.toCheckNullEmpty(0.0));				
				finalJson.put("extraWages", Commonutility.toCheckNullEmpty(0.0));				
				return finalJson;
			}
			else
			{				
				salaryinfo =staff.editselectsalary(locvrStaff_ID);				
				finalJson.put("staffid", salaryinfo.getStaffID().getStaffID());
				finalJson.put("staffName", Commonutility.toCheckNullEmpty(salaryinfo.getStaffID().getStaffName()));
				finalJson.put("staffEmail", Commonutility.toCheckNullEmpty(salaryinfo.getStaffID().getStaffEmail()));
				finalJson.put("staffMobno", Commonutility.toCheckNullEmpty(salaryinfo.getStaffID().getStaffMobno()));
				finalJson.put("staffGender", Commonutility.toCheckNullEmpty(salaryinfo.getStaffID().getStaffGender()));		
				if(salaryinfo.getMonthlySalary()!=null) {
					BigDecimal lvrBgdc= new BigDecimal(salaryinfo.getMonthlySalary());
					lvrBgdc = lvrBgdc.setScale(2,BigDecimal.ROUND_HALF_UP);
					finalJson.put("monthlySalary", String.valueOf(lvrBgdc));	
				} else {
					finalJson.put("monthlySalary","");
				}
				if(salaryinfo.getExtraWages()!=null) {
					BigDecimal lvrBgdc= new BigDecimal(salaryinfo.getExtraWages());
					lvrBgdc = lvrBgdc.setScale(2,BigDecimal.ROUND_HALF_UP);
					finalJson.put("extraWages", String.valueOf(lvrBgdc));	
				} else {
					finalJson.put("extraWages", "");	
				}
							
							

				return finalJson;
			}
					
		} catch (Exception e) {
			System.out.println("Exception found in salaryUtility.toInsrtLabor : "+e);
			log.logMessage("Exception : "+e, "error", SalaryUtility.class);
			return finalJson;
		} finally {
			 locCommutillObj =null;
			 locvrLBR_SERVICE_ID=null;
		}
		
		
	}
	
	public static String convertHexToStringValue(String hex) {
	    StringBuilder stringbuilder = new StringBuilder();
	    char[] hexData = hex.toCharArray();
	    for (int count = 0; count < hexData.length - 1; count += 2) {
	        int firstDigit = Character.digit(hexData[count], 16);
	        int lastDigit = Character.digit(hexData[count + 1], 16);
	        int decimal = firstDigit * 16 + lastDigit;
	        stringbuilder.append((char)decimal);
	    }
	    return stringbuilder.toString();
	}
	
	public static boolean toUpdtStaff(JSONObject pDataJson) {
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null,locvrLBR_GENDER = null; 
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null, locvrLBR_RATING = null, locvrENTRY_BY = null;
		int locvrStaff_ID;
		int locvrLBR_COUNTRY_ID1;
		int locvLBR_STATE_ID1;
		int locvrLBR_CITY_ID1;
		int locvrID_CARD_TYP;
		JSONObject finalJson = new JSONObject();
		boolean result = false;
		try {
			
		} catch (Exception e) {
			System.out.println("exception--- "+e);
		} finally {

		}
		return result;
		
	}
	
	public static boolean toDltStaff(JSONObject pDataJson) {
		boolean result = false;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null,locvrLBR_GENDER = null; 
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null, locvrLBR_RATING = null, locvrENTRY_BY = null;
		int locvrStaff_ID;
		int locvrLBR_COUNTRY_ID1;
		int locvLBR_STATE_ID1;
		int locvrLBR_CITY_ID1;
		int locvrID_CARD_TYP;
		try {
			
			return result;
		} catch (Exception e) {
			return result;
		} finally {

		}
	}
	public static String toDeActLabor(JSONObject pDataJson, Session pSession) {
		try {
			return "success";
		} catch (Exception e) {
			return "error";
		} finally {

		}
	}

	public static String getIvrparams() {
		return ivrparams;
	}

	public static void setIvrparams(String ivrparams) {
		SalaryUtility.ivrparams = ivrparams;
	}

	public static String toInsrtsalary(JSONObject locObjRecvdataJson) {
		// TODO Auto-generated method stub
		// Insert
		CommonUtils locCommutillObj =null;
		Log log=null; LaborDao locLabrObj=null;			
		int locvrLBR_STAFF_ID;		
		Double locvrSAL_MONTHLY = 0.0;
		Double locvrSAL_EXTRA = 0.0;
		boolean result= false;
		try {
			
			locCommutillObj = new CommonUtilsDao();
			log=new Log();
			 locvrLBR_STAFF_ID = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "staffid");						 
			String locvrSAL_MONTHLY_str = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "monthlySalary");						
			if(locvrSAL_MONTHLY_str!=null && !locvrSAL_MONTHLY_str.equalsIgnoreCase("null") && !locvrSAL_MONTHLY_str.equalsIgnoreCase("")){
				locvrSAL_MONTHLY=Double.parseDouble(locvrSAL_MONTHLY_str);
			 }
			 String locvrSAL_EXTRA_STR = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "extraWages");			
			 if(locvrSAL_EXTRA_STR!=null && !locvrSAL_EXTRA_STR.equalsIgnoreCase("null") && !locvrSAL_EXTRA_STR.equalsIgnoreCase("")){
				 locvrSAL_EXTRA =Double.parseDouble(locvrSAL_EXTRA_STR);
			 }
			 String locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "entryby");			
			 StaffSlryTblVO locObjsalaryprfvo=new StaffSlryTblVO();
			 StaffMasterTblVo locObjstaffprfvo = new StaffMasterTblVo();
			 locObjstaffprfvo.setStaffID(locvrLBR_STAFF_ID);
			 locObjsalaryprfvo.setStaffID(locObjstaffprfvo);
			 locObjsalaryprfvo.setMonthlySalary(locvrSAL_MONTHLY);
			 locObjsalaryprfvo.setExtraWages(locvrSAL_EXTRA);
			 locObjsalaryprfvo.setStatus(1);
			/* UserMasterTblVo locObjentrybyvo =new UserMasterTblVo();
			 locObjentrybyvo.setEntryBy(locvrENTRY_BY);*/
			 locObjsalaryprfvo.setEntryby(Integer.parseInt(locvrENTRY_BY));	
			 SimpleDateFormat locSmft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 locObjsalaryprfvo.setEntryDatetime(locSmft.parse(locSmft.format(new Date())));
			 locObjsalaryprfvo.setModifyDatetime(locSmft.parse(locSmft.format(new Date())));
			 result =staff.saveSalaryInfo(locObjsalaryprfvo);
			 System.out.println("result  "+result);
//			 locLabrObj=new LaborDaoservice();
//			 locLabrObj.saveLaborInfo(locObjsalaryprfvo);
			 
			 return "success";
		} catch (Exception e) {
			System.out.println("Exception found in salaryUtility.toInsrtsalary : "+e);
			log.logMessage("Exception : "+e, "error", SalaryUtility.class);
			return "error";
		} finally {
			 locCommutillObj =null; locLabrObj=null;			 
		}
	}

}
