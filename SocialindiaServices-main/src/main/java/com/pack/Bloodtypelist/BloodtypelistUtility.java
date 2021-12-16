package com.pack.Bloodtypelist;

import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.commonvo.MvpBloodGroupTbl;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class BloodtypelistUtility {

	public static String toInsertBloodtype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrbloodtypetitle = null, lvrbloodtypedesc = null, lvrbloodtypeshdesc = null,lvrbloodtypestatus = null,lvrbloodtypefiledata=null,lvrbloodtypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrbloodtypeimgscnd = null, lvrbloodtypeimgscnddata = null, lvrbloodtypeimgtrd = null, lvrbloodtypeimgtrddata = null, lvrbloodtypeimgfrth = null, lvrbloodtypeimgfrthdata = null ;
		int locbloodtypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		MvpBloodGroupTbl bloodtypeTblObj=null;
		WorktypeDao lvrbloodtypedaoobj=null;
		try {	
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			bloodtypeTblObj= new MvpBloodGroupTbl();
			logwrite.logMessage("Step 2 : Work Type Insert Block.", "info", BloodtypelistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrbloodtypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "bloodtypename");
			lvrbloodtypestatus = "1";
			bloodtypeTblObj.setBloodGroupName(lvrbloodtypetitle);
			bloodtypeTblObj.setStatus(Integer.parseInt(lvrbloodtypestatus));
			
			//----------- bloodtype Insert Start-----------			
			logwrite.logMessage("Step 3 : bloodtype Detail insert will start.", "info", BloodtypelistUtility.class);
			lvrbloodtypedaoobj = new WorktypeDaoservice();
			lvrexistname = lvrbloodtypedaoobj.toExistBloodtypelistname(lvrbloodtypetitle);
			if (lvrbloodtypetitle != null && lvrbloodtypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			locbloodtypeid = lvrbloodtypedaoobj.toInsertbloodtype(bloodtypeTblObj);
			}
			System.out.println(locbloodtypeid+": id bloodtype");
			logwrite.logMessage("Step 4 : bloodtype Detail insert End bloodtype Id : "+locbloodtypeid, "info", BloodtypelistUtility.class);
			// -----------bloodtype Insert end------------		
			if (locbloodtypeid>0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO MVP_BLOOD_GROUP_TBL (BLOOD_GROUP_ID, BLOOD_GROUP_NAME, STATUS) VALUES("+locbloodtypeid+", '"+lvrbloodtypetitle+"', 1);";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+locbloodtypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locbloodtypeid;
			}
			logwrite.logMessage("Step 7 : bloodtype Insert Block End.", "info", BloodtypelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EbloodtypeUtility.toInsertbloodtype() : "+e);
			logwrite.logMessage("Exception found in EbloodtypeUtility.toInsertbloodtype() : "+e, "error", BloodtypelistUtility.class);
			locFtrnStr="error!_!"+locbloodtypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; bloodtypeTblObj = null; lvrbloodtypedaoobj = null;
			locFtrnStr = null;lvrbloodtypedaoobj=null;lvrbloodtypefiledata=null;bloodtypeTblObj=null;lvrbloodtypeshdesc = null;
		}
	}
	

	
	public static String toDeactiveBloodtype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrbloodtypestatus = null, lvrbloodtypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean bloodtypeUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrbloodtypedaoobj = null;
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			logwrite.logMessage("Step 2 : bloodtype Deactive Block Start.", "info", BloodtypelistUtility.class);
			
			lvrbloodtypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "bloodtypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrbloodtypestatus = "0";
			locUpdQry = "update MvpBloodGroupTbl set status ="+Integer.parseInt(lvrbloodtypestatus)+" where bloodGroupId ="+Integer.parseInt(lvrbloodtypeid)+"";
			logwrite.logMessage("Step 3 : bloodtype Deactive Update Query : "+locUpdQry, "info", BloodtypelistUtility.class);
			lvrbloodtypedaoobj = new WorktypeDaoservice();
			bloodtypeUpdsts = lvrbloodtypedaoobj.toDeactivateWorkType(locUpdQry);
			
			if(bloodtypeUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_blood_group_tbl set STATUS ="+Integer.parseInt(lvrbloodtypestatus)+" where BLOOD_GROUP_ID ="+Integer.parseInt(lvrbloodtypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffbloodtype Deactive Block End.", "info", BloodtypelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in CommitteeUtility.toDeactivebloodtype() : "+e);
			logwrite.logMessage("Exception found in CommitteeUtility.toDeactivebloodtype() : "+e, "error", BloodtypelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrbloodtypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactiveBloodtype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrbloodtypestatus = null, lvrbloodtypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean bloodtypeUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrbloodtypedaoobj = null;
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			logwrite.logMessage("Step 2 : bloodtype Deactive Block Start.", "info", BloodtypelistUtility.class);
			
			lvrbloodtypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "bloodtypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrbloodtypestatus = "1";
			locUpdQry = "update MvpBloodGroupTbl set status ="+Integer.parseInt(lvrbloodtypestatus)+" where bloodGroupId ="+Integer.parseInt(lvrbloodtypeid)+"";
			logwrite.logMessage("Step 3 : bloodtype Deactive Update Query : "+locUpdQry, "info", BloodtypelistUtility.class);
			lvrbloodtypedaoobj = new WorktypeDaoservice();
			bloodtypeUpdsts = lvrbloodtypedaoobj.toDeactivateWorkType(locUpdQry);
			
			if(bloodtypeUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_blood_group_tbl set STATUS ="+Integer.parseInt(lvrbloodtypestatus)+" where BLOOD_GROUP_ID ="+Integer.parseInt(lvrbloodtypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffbloodtype Deactive Block End.", "info", BloodtypelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in StaffworktypeUtility.toDeactivebloodtype() : "+e);
			logwrite.logMessage("Exception found in StaffworktypeUtility.toDeactivebloodtype() : "+e, "error", BloodtypelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrbloodtypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
