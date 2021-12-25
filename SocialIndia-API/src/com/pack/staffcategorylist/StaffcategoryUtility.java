package com.pack.staffcategorylist;

import java.util.ResourceBundle;

import org.json.JSONObject;

import com.pack.commonvo.StaffCategoryMasterTblVO;
import com.pack.staffcatglistvo.persistance.StaffcategoryDao;
import com.pack.staffcatglistvo.persistance.StaffcategoryDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class StaffcategoryUtility {

	public static String toInsertcategory(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcategorytypetitle = null, lvrcategorytypedesc = null, lvrcategorytypeshdesc = null,lvrcategorytypestatus = null,lvrcategorytypefiledata=null,lvrcategorytypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrcategorytypeimgscnd = null, lvrcategorytypeimgscnddata = null, lvrcategorytypeimgtrd = null, lvrcategorytypeimgtrddata = null, lvrcategorytypeimgfrth = null, lvrcategorytypeimgfrthdata = null ;
		int loccategorytypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		StaffCategoryMasterTblVO categorytypeTblObj=null;
		StaffcategoryDao lvrcategorytypedaoobj=null;
		try {			
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			categorytypeTblObj= new StaffCategoryMasterTblVO();
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite.logMessage("Step 2 : staffcategorytype Insert Block.", "info", StaffcategoryUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrcategorytypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "categoryname");
			lvrcategorytypestatus = "1";
			categorytypeTblObj.setiVOstaffcategory(lvrcategorytypetitle);
			categorytypeTblObj.setiVOstaffcategsts(Integer.parseInt(lvrcategorytypestatus));
			//----------- categorytype Insert Start-----------			
			logwrite.logMessage("Step 3 : staffcategorytype Detail insert will start.", "info", StaffcategoryUtility.class);
			lvrcategorytypedaoobj = new StaffcategoryDaoservice();
			lvrexistname = lvrcategorytypedaoobj.toExiststaffCategoryname(lvrcategorytypetitle);
			if (lvrcategorytypetitle != null && lvrcategorytypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
				loccategorytypeid = lvrcategorytypedaoobj.toInsertcategorytype(categorytypeTblObj);
			}
			System.out.println(loccategorytypeid+": id categorytype");
			logwrite.logMessage("Step 4 : categorytype Detail insert End categorytype Id : "+loccategorytypeid, "info", StaffcategoryUtility.class);
			// -----------categorytype Insert end------------		
			if (loccategorytypeid > 0) {
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO `staff_category_tbl` (`STAFF_CATEGORY_ID`,`STAFF_CATEGORY`, `ENTRY_BY`,`STATUS`) VALUES("+loccategorytypeid+",'"+lvrcategorytypetitle+"', '"+lvrCrntusrid+"','"+lvrcategorytypestatus+"')";
			//	Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!" + loccategorytypeid;
			} else if (lvrexistname.equalsIgnoreCase("ALREADY EXISTS")) {
				locFtrnStr = "input!_!" + lvrexistname;
			} else {
				locFtrnStr = "error!_!" + loccategorytypeid;
			}
			logwrite.logMessage("Step 7 : categorytype Insert Block End.", "info", StaffcategoryUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EcategorytypeUtility.toInsertcategorytype() : "+e);
			logwrite.logMessage("Exception found in EcategorytypeUtility.toInsertcategorytype() : "+e, "error", StaffcategoryUtility.class);
			locFtrnStr="error!_!"+loccategorytypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; categorytypeTblObj = null; lvrcategorytypedaoobj = null;
			locFtrnStr = null;lvrcategorytypedaoobj=null;lvrcategorytypefiledata=null;categorytypeTblObj=null;lvrcategorytypeshdesc = null;
		}
	}
	

	
	public static String toDeactivecategory(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcategorytypestatus = null, lvrcategorytypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean categorytypeUpdsts = false;
		String locUpdQry=null;
		StaffcategoryDao lvrcategorytypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : staffcategorytype Deactive Block Start.", "info", StaffcategoryUtility.class);
			
			lvrcategorytypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "categoryid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrcategorytypestatus = "0";
			locUpdQry = "update StaffCategoryMasterTblVO set iVOstaffcategsts ="+Integer.parseInt(lvrcategorytypestatus)+" where iVOstaffcategid ="+Integer.parseInt(lvrcategorytypeid)+"";
			logwrite.logMessage("Step 3 : staffcategorytype Deactive Update Query : "+locUpdQry, "info", StaffcategoryUtility.class);
			lvrcategorytypedaoobj = new StaffcategoryDaoservice();
			categorytypeUpdsts = lvrcategorytypedaoobj.toDeactivatecategorytype(locUpdQry);
			
			if(categorytypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffcategorytype Deactive Block End.", "info", StaffcategoryUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in StaffcategoryUtility.toDeactivecategorytype() : "+e);
			logwrite.logMessage("Exception found in StaffcategoryUtility.toDeactivecategorytype() : "+e, "error", StaffcategoryUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrcategorytypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactivecategory(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcategorytypestatus = null, lvrcategorytypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean categorytypeUpdsts = false;
		String locUpdQry=null;
		StaffcategoryDao lvrcategorytypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : staffcategorytype Deactive Block Start.", "info", StaffcategoryUtility.class);
			
			lvrcategorytypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "categoryid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrcategorytypestatus = "1";
			locUpdQry = "update StaffCategoryMasterTblVO set iVOstaffcategsts ="+Integer.parseInt(lvrcategorytypestatus)+" where iVOstaffcategid ="+Integer.parseInt(lvrcategorytypeid)+"";
			logwrite.logMessage("Step 3 : categorytype Deactive Update Query : "+locUpdQry, "info", StaffcategoryUtility.class);
			lvrcategorytypedaoobj = new StaffcategoryDaoservice();
			categorytypeUpdsts = lvrcategorytypedaoobj.toDeactivatecategorytype(locUpdQry);
			
			if(categorytypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffcategorytype Deactive Block End.", "info", StaffcategoryUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in StaffcategoryUtility.toDeactivecategorytype() : "+e);
			logwrite.logMessage("Exception found in StaffcategoryUtility.toDeactivecategorytype() : "+e, "error", StaffcategoryUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrcategorytypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
