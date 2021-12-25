package com.socialindia.generalmgnt;

import org.hibernate.Session;
import org.json.JSONObject;

import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.labor.LaborUtility;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.CommonUtils;
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;

public class StaffUtility {
	private static String ivrparams;
	static StaffMasterTblVo staffinfo =new StaffMasterTblVo();
	//static CityMasterTblVO locCustBnObj =new CityMasterTblVO();
	static generalmgmtDao staff =new generalmgntDaoServices();
	public static JSONObject toSelectStaff(JSONObject pDataJson) {	
		CommonUtils locCommutillObj =null;
		Log log=null;
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null, locvrID_CARD_TYP = null;
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null, locvrLBR_RATING = null, locvrENTRY_BY = null;
		int locvrStaff_ID;
		JSONObject finalJson = new JSONObject();
		try {
			System.out.println("---update staffutility---"+pDataJson);
			locvrLBR_SERVICE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ivrDecissBlkflag");
			locvrStaff_ID = (int) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffid");
			staffinfo =staff.editstaff(locvrStaff_ID);			
//			if(staffinfo.getPstlId()!=null && staffinfo.getCityId()!=null){
			if(staffinfo.getCityId()!=null){
				CityMasterTblVO loc_cityBnobj=staffinfo.getCityId();
				finalJson.put("staffState", loc_cityBnobj.getStateId().getStateName());
				finalJson.put("staffCity",loc_cityBnobj.getCityName());
				finalJson.put("staffCityId",loc_cityBnobj.getCityId());
				finalJson.put("staffStateId",loc_cityBnobj.getStateId().getStateId());
				finalJson.put("staffCountryId",loc_cityBnobj.getStateId().getCountryId().getCountryId());
				finalJson.put("staffCountry",loc_cityBnobj.getStateId().getCountryId().getCountryName());
				System.out.println(loc_cityBnobj.getStateId().getCountryId().getCountryName());
			}
					
			finalJson.put("staffid", staffinfo.getStaffID());
			finalJson.put("staffName", staffinfo.getStaffName());
			finalJson.put("staffEmail", staffinfo.getStaffEmail());
			finalJson.put("staffIsdcode", staffinfo.getISDcode());
			finalJson.put("staffMobno", staffinfo.getStaffMobno());
			if(staffinfo.getStaffGender()!=null){
				finalJson.put("staffGender", staffinfo.getStaffGender());
			} else{
				finalJson.put("staffGender", "");
			}
			
			
			if(staffinfo.getiVOcradid()!=null){
				IDCardMasterTblVO idcardObj =staffinfo.getiVOcradid();	
				finalJson.put("staffIdcardtype", staffinfo.getiVOcradid().getiVOcradid());
				finalJson.put("staffIdcardno", idcardObj.getiVOcradname());
			}else{
				finalJson.put("staffIdcardtype",0);
				finalJson.put("staffIdcardno", "");
			}
			finalJson.put("staffAddr", staffinfo.getStaffAddr());
			finalJson.put("staffCountryId", staffinfo.getStaffCountry());		
			if(staffinfo.getStaffState() == null)
			{				
				finalJson.put("staffState", "");
			}
			else
			{
			}
			if(staffinfo.getStaffCity() == null)
			{
				finalJson.put("staffCity", "");
			}
			else
			{
				
			}
			if(staffinfo.getStaffCity() == null)
			{				
				finalJson.put("staffCityId", 0);
			}
			else
			{							
			}			
			if(staffinfo.getStaffState() == null)
			{				
				finalJson.put("staffStateId", 0);
			}
			else
			{								
			}			
			if(staffinfo.getStaffCountry() == null)
			{				
				finalJson.put("staffCountryId", 0);
			}
			else
			{							
			}if(staffinfo.getStaffCountry() == null)
			{				
				finalJson.put("staffCountry", "");
			}
			else
			{							
			}
			
			StaffCategoryMasterTblVO categoryObj =null;
			if(staffinfo.getiVOstaffcategid() == null)
			{							
				finalJson.put("staffcategoryid", "");	
				finalJson.put("staffcategory", "");
			}
			else
			{		
				categoryObj =staffinfo.getiVOstaffcategid();				
				finalJson.put("staffcategoryid",Commonutility.toCheckNullEmpty(staffinfo.getiVOstaffcategid().getiVOstaffcategid()));
				finalJson.put("staffcategory",categoryObj.getiVOstaffcategory());
			}							
			finalJson.put("idproofno",staffinfo.getStaffIdcardno());
			System.out.println("workinghours------"+staffinfo.getWorkinghours());
			if(staffinfo.getWorkinghours() == null)
			{				
				finalJson.put("workinghours", 0);
			}
			else
			{				
				finalJson.put("workinghours",staffinfo.getWorkinghours());
			}
			
			
			PostalCodeMasterTblVO pstlObj = null;
			if(staffinfo.getPstlId() == null)
			{
				finalJson.put("pincode", 0);
			}
			else
			{
//				pstlObj = staffinfo.getPstlId();
//				finalJson.put("pincode",pstlObj.getPstlCode());
				finalJson.put("pincode",staffinfo.getPstlId());
			}
//						
//			if(staffinfo.getPstlId() == null)
//			{
//				finalJson.put("pincodeid", 0);
//			}
//			else
//			{
//				finalJson.put("pincodeid", pstlObj.getPstlId());
//			}
			finalJson.put("pincodeid", "");
			
			if(staffinfo.getCompanyId()!=null){
				CompanyMstTblVO companyid=staffinfo.getCompanyId();
				finalJson.put("companyid",String.valueOf(staffinfo.getCompanyId().getCompanyId()));
				finalJson.put("companyname",companyid.getCompanyName());		
			}else{
				finalJson.put("companyid",String.valueOf("0"));
				finalJson.put("companyname","");
			}
			String staffImageFileName =staffinfo.getImageNameWeb();
			if (staffImageFileName != null && staffImageFileName != "") {
				finalJson.put("staffimage", staffinfo.getImageNameWeb());
			} else {				
				finalJson.put("staffimage", "");								
			}		
			 return finalJson;
		} catch (Exception e) {
			log.logMessage("Exception : "+e, "error", LaborUtility.class);
			return finalJson;
		} finally {
			 locCommutillObj =null;
			 locvrLBR_SERVICE_ID=null;locvrLBR_NAME = null; locvrLBR_EMAIL = null; locvrLBR_PH_NO = null; locvrID_CARD_TYP = null;
			 locvrID_CARD_NO = null; locvrLBR_ADD_1 = null; locvrLBR_ADD_2 = null; locvrLBR_CITY_ID = null; locvLBR_STATE_ID = null;
			 locvrLBR_COUNTRY_ID = null; locvLBR_STS = null; locvrLBR_KYC_NAME = null; locvrLBR_RATING = null; locvrENTRY_BY = null;
		}
		
	}
	
	public static StaffMasterTblVo toUpdtStaff(JSONObject pDataJson) {
		String locvrLBR_SERVICE_ID=null,locvrLBR_NAME = null, locvrLBR_EMAIL = null, locvrLBR_PH_NO = null,locvrLBR_GENDER = null; 
		String locvrID_CARD_NO = null, locvrLBR_ADD_1 = null, locvrLBR_ISD = null, locvrLBR_CITY_ID = null, locvLBR_STATE_ID = null;
		String locvrLBR_COUNTRY_ID = null, locvLBR_STS = null, locvrLBR_KYC_NAME = null, locvrLBR_RATING = null, locvrENTRY_BY = null;
		int locvrStaff_ID;
		String locvrLBR_COUNTRY_ID1;
		String locvLBR_STATE_ID1;
		String locvrLBR_CITY_ID1;
		String locvrCAT_ID;
		String locvrPIN_CODE;
		String locvrWORK_HOUR_str=null;
		JSONObject finalJson = new JSONObject();
		boolean result = false;
		StaffMasterTblVo staffupdate =new StaffMasterTblVo();
		try {
			System.out.println("pDataJson Update Action   "+pDataJson);
			locvrLBR_SERVICE_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ivrDecissBlkflag");
			locvrStaff_ID = (int) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffid");			
			locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffName");
			locvrLBR_EMAIL = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffEmail");
			locvrLBR_PH_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffMobno");
			locvrLBR_GENDER = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffGender");
			String locvrID_CARD_TYP = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffIdcardtype");
			locvrID_CARD_NO = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffIdcardno");
			locvrLBR_ADD_1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffAddr");
			locvrLBR_COUNTRY_ID1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffCountry");
			locvLBR_STATE_ID1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffState");
			locvrLBR_CITY_ID1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffCity");
			locvrLBR_ISD = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffIsdcode");
			locvrCAT_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffcategory");
			locvrPIN_CODE = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffpincode");
			
			System.out.println("locvrPIN_CODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+locvrPIN_CODE);
			
			locvrWORK_HOUR_str = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffworkinghours");
			String imageweb = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imageweb");
			String staffImageFileName = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"staffImageFileName");
			String staffcompanyid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"companyid");
		//	int comid=Integer.parseInt(staffcompanyid);
			StaffMasterTblVo stafftable = new StaffMasterTblVo();
			stafftable.setStaffID(locvrStaff_ID);
			stafftable.setStaffName(locvrLBR_NAME);
			stafftable.setStaffEmail(locvrLBR_EMAIL);
			stafftable.setStaffMobno(locvrLBR_PH_NO);
			stafftable.setStaffGender(locvrLBR_GENDER);
			CompanyMstTblVO cmpyObj =new CompanyMstTblVO();
			if(!Commonutility.toCheckisNumeric(staffcompanyid)){
				stafftable.setCompanyId(null);
			 }else{
				 cmpyObj.setCompanyId(Integer.parseInt(staffcompanyid));
				 stafftable.setCompanyId(cmpyObj);
			 }	
			IDCardMasterTblVO idcardObj = new IDCardMasterTblVO();			
			if(!Commonutility.toCheckisNumeric(locvrID_CARD_TYP) || locvrID_CARD_TYP.equalsIgnoreCase("0")){
				stafftable.setiVOcradid(null);
			 }else{
				 idcardObj.setiVOcradid(Integer.parseInt(locvrID_CARD_TYP));
				 stafftable.setiVOcradid(idcardObj);
			 }	
			stafftable.setStaffIdcardno(locvrID_CARD_NO);
			stafftable.setStaffAddr(locvrLBR_ADD_1);
			if(!Commonutility.toCheckisNumeric(locvrLBR_COUNTRY_ID1)){
				stafftable.setStaffCountry(null);
			} else {
				 stafftable.setStaffCountry(Integer.parseInt(locvrLBR_COUNTRY_ID1));
			}
			
			if (!Commonutility.toCheckisNumeric(locvLBR_STATE_ID1)){
				stafftable.setStaffState(null);
			} else {
				 stafftable.setStaffState(Integer.parseInt(locvLBR_STATE_ID1));
			}
			
			if (!Commonutility.toCheckisNumeric(locvrLBR_CITY_ID1)){
				stafftable.setStaffCity(null);
			 }else{
				 
				 stafftable.setStaffCity(Integer.parseInt(locvrLBR_CITY_ID1));
			 }
			
			stafftable.setISDcode(locvrLBR_ISD);			
			StaffCategoryMasterTblVO categoryinfo = new StaffCategoryMasterTblVO();						
			PostalCodeMasterTblVO pstlcodeObj = new PostalCodeMasterTblVO();
			String pstl_st = null;
			if(!Commonutility.toCheckisNumeric(locvrPIN_CODE)){				
				pstl_st = null;
			}else{			
				pstl_st =locvrPIN_CODE;
			}	
			if(!Commonutility.toCheckisNumeric(locvrWORK_HOUR_str)){
				stafftable.setWorkinghours(null);
			 }else{
				 stafftable.setWorkinghours(Integer.parseInt(locvrWORK_HOUR_str));
			 }	
			
			if (staffImageFileName != null && staffImageFileName!= "") {
				staffImageFileName = staffImageFileName.replaceAll(" ", "_");				
			}
			stafftable.setImageNameWeb(staffImageFileName);
			stafftable.setImageNameMobile(staffImageFileName);	
			System.out.println("10****");
			staffupdate = staff.updatestaffmgnt(stafftable,locvrCAT_ID,pstl_st);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception--- "+e);
		} finally {
			locvrLBR_SERVICE_ID="";
			locvrLBR_NAME = "";locvrLBR_EMAIL = "";locvrLBR_GENDER = "";locvrID_CARD_NO = "";locvrLBR_ADD_1 = "";
			locvrLBR_ISD = "";locvrLBR_CITY_ID = "";locvLBR_STATE_ID = "";locvrLBR_COUNTRY_ID = "";locvLBR_STS = ""; 
			locvrLBR_KYC_NAME = "";locvrLBR_RATING = "";locvrENTRY_BY = "";locvrLBR_COUNTRY_ID1="";locvLBR_STATE_ID1="";
			locvrLBR_CITY_ID1="";locvrCAT_ID="";locvrPIN_CODE="";locvrWORK_HOUR_str="";
		}
		return staffupdate;
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
			locvrStaff_ID = (int) Commonutility.toHasChkJsonRtnValObj(pDataJson, "staffid");
			result = staff.deletestaff(locvrStaff_ID);
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
		StaffUtility.ivrparams = ivrparams;
	}

	

	

	
	
}
