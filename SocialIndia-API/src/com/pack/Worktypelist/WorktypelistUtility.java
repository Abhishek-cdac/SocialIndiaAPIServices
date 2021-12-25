package com.pack.Worktypelist;

import java.util.ResourceBundle;

import org.json.JSONObject;

import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class WorktypelistUtility {
	static ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	public static String toInsertWorktype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrWorkTypetitle = null, lvrWorkTypedesc = null, lvrWorkTypeshdesc = null,lvrWorkTypestatus = null,lvrWorkTypefiledata=null,lvrWorkTypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrWorkTypeimgscnd = null, lvrWorkTypeimgscnddata = null, lvrWorkTypeimgtrd = null, lvrWorkTypeimgtrddata = null, lvrWorkTypeimgfrth = null, lvrWorkTypeimgfrthdata = null ;
		int locWorkTypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		LaborWrkTypMasterTblVO WorkTypeTblObj=null;
		WorktypeDao lvrWorkTypedaoobj=null;
		try {		
			//ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			WorkTypeTblObj= new LaborWrkTypMasterTblVO();
			logwrite.logMessage("Step 2 : Work Type Insert Block.", "info", WorktypelistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrWorkTypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "worktypename");
			lvrWorkTypestatus = "1";
			WorkTypeTblObj.setIVOlbrWORK_TYPE(lvrWorkTypetitle);
			WorkTypeTblObj.setStatus(Integer.parseInt(lvrWorkTypestatus));
			if (Commonutility.checkempty(lvrCrntusrid)) {
				WorkTypeTblObj.setIvrEntryby(Integer.parseInt(lvrCrntusrid));
			} else {
				WorkTypeTblObj.setIvrEntryby(null);
			}			
		String lvrCrnttime = Commonutility.getCurrentDate("yyyy-MM-dd HH:mm:ss");
			//----------- WorkType Insert Start-----------			
			logwrite.logMessage("Step 3 : WorkType Detail insert will start.", "info", WorktypelistUtility.class);
			lvrWorkTypedaoobj = new WorktypeDaoservice();
			lvrexistname = lvrWorkTypedaoobj.toExistWorktypelistname(lvrWorkTypetitle);
			if (lvrWorkTypetitle != null && lvrWorkTypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
				locWorkTypeid = lvrWorkTypedaoobj.toInsertWorkType(WorkTypeTblObj);
			}
			System.out.println(locWorkTypeid+": id WorkType");
			logwrite.logMessage("Step 4 : WorkType Detail insert End WorkType Id : "+locWorkTypeid, "info", WorktypelistUtility.class);
			// -----------WorkType Insert end------------		
			if (locWorkTypeid>0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO labour_work_type_tbl (WORK_TYPE_ID,WORK_TYPE, STATUS,ENTRY_BY,`ENTRY_DATETIME`, `MODIFY_DATETIME`) VALUES("+locWorkTypeid+",'"+lvrWorkTypetitle+"', '"+lvrWorkTypestatus+"','"+lvrCrntusrid+"','"+lvrCrnttime+"','"+lvrCrnttime+"')";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+locWorkTypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locWorkTypeid;
			}
			logwrite.logMessage("Step 7 : WorkType Insert Block End.", "info", WorktypelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EWorkTypeUtility.toInsertWorkType() : "+e);
			logwrite.logMessage("Exception found in EWorkTypeUtility.toInsertWorkType() : "+e, "error", WorktypelistUtility.class);
			locFtrnStr="error!_!"+locWorkTypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; WorkTypeTblObj = null; lvrWorkTypedaoobj = null;
			locFtrnStr = null;lvrWorkTypedaoobj=null;lvrWorkTypefiledata=null;WorkTypeTblObj=null;lvrWorkTypeshdesc = null;
		}
	}
	

	
	public static String toDeactiveworktype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrWorkTypestatus = null, lvrWorkTypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean WorkTypeUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrWorkTypedaoobj = null;
		
		try {
			
			logwrite = new Log();
			logwrite.logMessage("Step 2 : WorkType Deactive Block Start.", "info", WorktypelistUtility.class);
			
			lvrWorkTypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "worktypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrWorkTypestatus = "0";
			locUpdQry = "update LaborWrkTypMasterTblVO set status ="+Integer.parseInt(lvrWorkTypestatus)+" where wrktypId ="+Integer.parseInt(lvrWorkTypeid)+"";
			logwrite.logMessage("Step 3 : WorkType Deactive Update Query : "+locUpdQry, "info", WorktypelistUtility.class);
			lvrWorkTypedaoobj = new WorktypeDaoservice();
			WorkTypeUpdsts = lvrWorkTypedaoobj.toDeactivateWorkType(locUpdQry);
			String textvalue = "update labour_work_type_tbl set STATUS = "+Integer.parseInt(lvrWorkTypestatus)+" where WORK_TYPE_ID = "+Integer.parseInt(lvrWorkTypeid)+"";
			if(WorkTypeUpdsts){
				String filepath = rb.getString("external.mobiledbfldr.path");
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffWorkType Deactive Block End.", "info", WorktypelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in CommitteeUtility.toDeactiveWorkType() : "+e);
			logwrite.logMessage("Exception found in CommitteeUtility.toDeactiveWorkType() : "+e, "error", WorktypelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrWorkTypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactiveworktype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrWorkTypestatus = null, lvrWorkTypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean WorkTypeUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrWorkTypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : WorkType Deactive Block Start.", "info", WorktypelistUtility.class);
			
			lvrWorkTypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "worktypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrWorkTypestatus = "1";
			locUpdQry = "update LaborWrkTypMasterTblVO set status ="+Integer.parseInt(lvrWorkTypestatus)+" where wrktypId ="+Integer.parseInt(lvrWorkTypeid)+"";
			logwrite.logMessage("Step 3 : WorkType Deactive Update Query : "+locUpdQry, "info", WorktypelistUtility.class);
			lvrWorkTypedaoobj = new WorktypeDaoservice();
			WorkTypeUpdsts = lvrWorkTypedaoobj.toDeactivateWorkType(locUpdQry);
			String textvalue = "update labour_work_type_tbl set STATUS = "+Integer.parseInt(lvrWorkTypestatus)+" where WORK_TYPE_ID = "+Integer.parseInt(lvrWorkTypeid)+"";
			if(WorkTypeUpdsts) {
				String filepath = rb.getString("external.mobiledbfldr.path");
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffWorkType Deactive Block End.", "info", WorktypelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in StaffworktypeUtility.toDeactiveWorkType() : "+e);
			logwrite.logMessage("Exception found in StaffworktypeUtility.toDeactiveWorkType() : "+e, "error", WorktypelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrWorkTypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
